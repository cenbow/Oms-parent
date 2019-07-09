package com.work.shop.oms.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.request.OrderManagementRequest;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.PurchaseOrderService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private PurchaseOrderMapper purchaseOrderMapper;

	@Resource
	private PurchaseOrderLineMapper purchaseOrderLineMapper;

	@Resource
	private OrderDistributeMapper orderDistributeMapper;

	@Resource
	private MasterOrderInfoDetailMapper masterOrderInfoDetailMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MasterOrderInfoService masterOrderInfoService;

	@Resource(name="jointPurchasingProcessJmsTemplate")
	private JmsTemplate jointPurchasingProcessJmsTemplate;
	
	@Override
	public ReturnInfo<String> purchaseOrderCreate(OrderManagementRequest request) {
		logger.info("采购单创建request:"  + JSON.toJSONString(request));
		ReturnInfo<String> info = new ReturnInfo<>(Constant.OS_NO, "采购单创建失败");
		if (request == null) {
			info.setMessage("参数为空");
			return info;
		}
		String orderSn = request.getShipSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			info.setMessage("参数[shipSn]为空");
			return info;
		}
		try {
			OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
			if (distribute == null) {
				info.setMessage("交货单信息不存在！");
				return info;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("masterOrderSn", distribute.getMasterOrderSn());
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				info.setMessage("订单信息不存在！");
				return info;
			}
			info = commonOrderCreate(master, distribute, request);
		} catch (Exception e) {
			logger.error("采购单[{}]创建失败：" + e.getMessage(), orderSn, e);
		} finally {
			logger.info("采购单创建response:" + JSON.toJSONString(info));
		}
		return info;
	}

	/**
	 * 订单转供应商采购单
	 * @param request
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> purchaseOrderCreateByMaster(OrderManagementRequest request) {
		logger.info("采购单创建request:"  + JSON.toJSONString(request));
		ReturnInfo<String> info = new ReturnInfo<>(Constant.OS_NO, "采购单创建失败");
		if (request == null) {
			info.setMessage("参数为空");
			return info;
		}
		String masterOrderSn = request.getMasterOrderSn();
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("参数[masterOrderSn]为空");
			return info;
		}
		info.setOrderSn(masterOrderSn);
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				info.setMessage("订单信息不存在！");
				return info;
			}
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (CollectionUtils.isEmpty(distributes)) {
				info.setMessage("交货单单信息不存在！");
				return info;
			}
			for (OrderDistribute distribute : distributes) {
				commonOrderCreate(master, distribute, request);
			}

            processPurchaseOrderFollow(master);

			info.setIsOk(Constant.OS_YES);
			info.setMessage("采购单创建成功");
		} catch (Exception e) {
			logger.error("采购单[{}]创建失败：" + e.getMessage(), masterOrderSn, e);
		} finally {
			logger.info("采购单创建response:" + JSON.toJSONString(info));
		}
		return info;
	}

	/**
	 * 处理采购单后续操作
	 * @param master
	 */
	private void processPurchaseOrderFollow(MasterOrderDetail master) {
		// 是否账期支付, 0期立即扣款
		masterOrderInfoService.processOrderPayPeriod(master.getMasterOrderSn());
	}

	/**
	 * 采购单创建
	 * @param master
	 * @param distribute
	 * @param request
	 * @return ReturnInfo<String>
	 */
	private ReturnInfo<String> commonOrderCreate(MasterOrderDetail master, OrderDistribute distribute, OrderManagementRequest request) {
		ReturnInfo<String> info = new ReturnInfo<>(Constant.OS_NO, "采购单创建失败");
		List<PurchaseOrderLine> lines = mergeOrderLines(distribute.getOrderSn());
		if (StringUtil.isListNull(lines)) {
			info.setMessage("商品明细为空");
			return info;
		}
		PurchaseOrder order = purchaseOrderMapper.selectByPrimaryKey(distribute.getOrderSn());
		if (order != null) {
			info.setMessage("采购单已经创建");
			return info;
		}
		PurchaseOrder record = new PurchaseOrder();
        fillPurchaseOrder(record, master, distribute);
        record.setOperateUser(request.getActionUser());
        fillOrderPrice(record, lines);
		purchaseOrderMapper.insertSelective(record);
		
		for (PurchaseOrderLine line : lines) {
			purchaseOrderLineMapper.insertSelective(line);
		}
		// 交货单创建拣货单标识
        updateOrderDistribute(distribute);

		//联采订单推送招采
        if (record.getOrderType() == 1) {
            pushJointPurchasing(master, request.getActionUserId());
        }

        info.setIsOk(Constant.OS_YES);
        info.setMessage("采购单创建成功");
		return info;
	}

    /**
     * 填充供应商采购单信息
     * @param record
     * @param master
     * @param distribute
     */
	private void fillPurchaseOrder(PurchaseOrder record, MasterOrderDetail master, OrderDistribute distribute) {
        record.setPurchaseOrderCode(distribute.getOrderSn());
        record.setOrderSn(distribute.getOrderSn());
        record.setAddTime(master.getAddTime());
        record.setConsignee(master.getConsignee());
        record.setCountry(master.getCountryName());
        record.setProvince(master.getProvinceName());
        record.setCity(master.getCityName());
        record.setDistrict(master.getDistrictName());
        record.setAddress(master.getAddress());getClass();
        record.setZipcode(master.getZipcode());
        record.setMasterOrderSn(distribute.getMasterOrderSn());
        record.setMobile(master.getMobile());
        record.setTel(master.getTel());
        record.setUserId(master.getUserId());
        record.setCreateTime(new Date());
        record.setVendorCode(distribute.getSupplierCode());
        //正常单
        record.setOrderType(0);

        //创建订单类型，0正常订单，1联采订单，默认正常单,转
        Integer orderType = master.getCreateOrderType();
        if (orderType == 1) {
            //协议单
            record.setOrderType(1);
        }
    }

    /**
     * 填充采购单商品数量和总金额
     * @param record
     * @param lines
     */
    private void fillOrderPrice(PurchaseOrder record, List<PurchaseOrderLine> lines) {
        int goodsCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (PurchaseOrderLine line : lines) {
            Short goodsNumber = line.getGoodsNumber();
            if (goodsNumber == null) {
                continue;
            }

            goodsCount += goodsNumber;
            BigDecimal price = line.getPrice();
            if (price != null) {
                totalPrice = totalPrice.add(price.multiply(new BigDecimal(goodsNumber)));
            }

        }
        //商品数量
        record.setGoodsCount(goodsCount);
        //订单总价
        record.setTotalFee(totalPrice);
    }

    /**
     * 交货单更新时间
     * @param distribute
     */
	private void updateOrderDistribute(OrderDistribute distribute) {
        // 交货单创建拣货单标识
        OrderDistribute updateDist = new OrderDistribute();
        updateDist.setOrderSn(distribute.getOrderSn());
        Date date = new Date();
        updateDist.setLastUpdateTime(date);
        updateDist.setUpdateTime(date);
    }
	
	/**
	 * 合并订单商品项
	 * @param orderSn
	 * @return List<PurchaseOrderLine>
	 */
	private List<PurchaseOrderLine> mergeOrderLines(String orderSn) {

		MasterOrderGoodsExample example = new MasterOrderGoodsExample();
		example.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(Constant.IS_DEL_NO);
		List<MasterOrderGoods> itemList = masterOrderGoodsMapper.selectByExample(example);
		if (StringUtil.isListNull(itemList)) {
			return null;
		}
		logger.debug("订单" + orderSn + "开始合并商品");
		Map<String, MasterOrderGoods> map = new HashMap<String, MasterOrderGoods>(Constant.DEFAULT_MAP_SIZE);
		for (MasterOrderGoods ogBean : itemList) {
			String customCode = ogBean.getCustomCode();
			if (StringUtil.isEmpty(customCode)) {
				logger.error("订单下发时合并商品异常.订单号：" + orderSn);
				return null;
			}
			if (StringUtil.isBlank(customCode)) {
				logger.error("订单" + orderSn + "商品customCode为空");
				return null;
			}
			if (!map.containsKey(customCode)) {
				map.put(customCode, ogBean);
			} else {
				MasterOrderGoods ogs = map.get(customCode);
				setGoodsNumber(ogBean, ogs);
				logger.debug("订单" + orderSn + "商品" + customCode + "合并完成");
			}
		}

		List<MasterOrderGoods> tempList = new ArrayList<MasterOrderGoods>(itemList.size());

		for (Iterator<MasterOrderGoods> iterator = map.values().iterator(); iterator.hasNext();) {
            tempList.add(iterator.next());
		}
		if (StringUtil.isListNull(tempList)) {
			logger.error("订单下发时合并商品异常.订单号：" + orderSn);
			return null;
		}

        List<PurchaseOrderLine> orderLines = new ArrayList<PurchaseOrderLine>();
		for (MasterOrderGoods item : tempList) {
			PurchaseOrderLine line = new PurchaseOrderLine();
			line.setPurchaseOrderCode(orderSn);
			line.setGoodsColorName(item.getGoodsColorName());
			line.setCustomCode(item.getCustomCode());
			line.setGoodsName(item.getGoodsName());
			line.setDepotCode(item.getDepotCode());
			line.setGoodsNumber(item.getGoodsNumber());
			line.setGoodsThumb(item.getGoodsThumb());
			line.setGoodsSizeName(item.getGoodsSizeName());
            line.setPrice(item.getCostPrice());
			orderLines.add(line);
		}
		return orderLines;
	}
	
	private void setGoodsNumber(MasterOrderGoods ogBean, MasterOrderGoods ogs) {
		ogs.setGoodsNumber((short) (ogs.getGoodsNumber() + ogBean.getGoodsNumber()));
	}

    /**
     * 联采单推送
     * @param master
     * @param actionUserId
     */
	private void pushJointPurchasing(MasterOrderDetail master, String actionUserId) {
	    Map<String, Object> paramMap = new HashMap<String, Object>(6);
        paramMap.put("masterOrderSn", master.getMasterOrderSn());
        paramMap.put("actionUser", master.getUserId());
        paramMap.put("actionUserId", actionUserId);
	    jointPurchasingProcessJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(paramMap)));
    }
}
