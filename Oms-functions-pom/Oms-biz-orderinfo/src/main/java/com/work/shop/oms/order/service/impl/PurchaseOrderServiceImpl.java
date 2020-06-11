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
import com.work.shop.oms.utils.MathOperation;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

	@Resource(name="supplierOrderSendJmsTemplate")
	private JmsTemplate supplierOrderSendJmsTemplate;

	@Resource(name="supplierStoreOrderSendJmsTemplate")
	private JmsTemplate supplierStoreOrderSendJmsTemplate;
	/**
	 * 采购单创建
	 * @param request
	 * @return
	 */
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
			Map<String, Object> paramMap = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
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
			info.setMessage("订单号为空");
			return info;
		}
		info.setOrderSn(masterOrderSn);
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", 0);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if (master == null) {
				info.setMessage("订单信息不存在！");
				return info;
			}
			// 是否强制下发
            int purchaseOrderSend = request.getPurchaseOrderSend();
			if (purchaseOrderSend == 0) {
                if (master.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
                    info.setMessage("订单是问题单");
                    return info;
                }
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
	 * 采购单创建
	 * @param master 订单信息
	 * @param distribute 交货单信息
	 * @param request
	 * @return ReturnInfo<String>
	 */
	private ReturnInfo<String> commonOrderCreate(MasterOrderDetail master, OrderDistribute distribute, OrderManagementRequest request) {
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO, "采购单创建失败");

		String orderFrom = master.getOrderFrom();
		if (!Constant.DEFAULT_SHOP.equals(orderFrom)) {
			if(master.getCreateOrderType()!=1){
				//联采订单不扣除库存，只有正常订单才扣除供应商店铺中商品的数量
				pushPurchaseStoreOrder(distribute.getOrderSn(),distribute.getSupplierCode());
			}
			info.setMessage("店铺订单,不需要转采购订单");
            info.setIsOk(Constant.OS_YES);
			return info;
		}
		List<PurchaseOrderLine> lines = mergeOrderLines(distribute.getOrderSn());
		if (StringUtil.isListNull(lines)) {
			info.setMessage("商品明细为空");
			return info;
		}
		PurchaseOrder order = purchaseOrderMapper.selectByPrimaryKey(distribute.getOrderSn());
		if (order != null) {
			info.setMessage("已下发供应商采购单");
            info.setIsOk(Constant.OS_YES);
			return info;
		}
		PurchaseOrder record = new PurchaseOrder();
        fillPurchaseOrder(record, master, distribute);
        record.setOperateUser(request.getActionUser());
		record.setErpOrderNo(request.getErpOrderNo());
        fillOrderPrice(record, lines);
		purchaseOrderMapper.insertSelective(record);
		
		for (PurchaseOrderLine line : lines) {
			purchaseOrderLineMapper.insertSelective(line);
		}
		// 交货单创建拣货单标识
        updateOrderDistribute(distribute);

		//采购单推送供应链，超市为甲方，供应商为乙方
        //只要不是需要签章，没有签章完成的订单都下发
        boolean flag = false;

        if (request.getPurchaseOrderSend() == 1) {
            // 强制下发
            flag = true;
        } else if (master.getNeedSign() != 1) {
            // 子公司不需要签章
            flag = true;
        } else if (master.getSignStatus() == 1) {
            // 子公司已签章
            flag = true;
        }
        if (flag) {
            pushJointPurchasing(master.getMasterOrderSn(), request.getActionUser(), request.getActionUserId(), distribute.getSupplierCode(), 1);
        }

		pushPurchaseOrder(distribute.getOrderSn());

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
        record.setAddress(master.getAddress());
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
        BigDecimal totalUntaxPrice = new BigDecimal(0);
        for (PurchaseOrderLine line : lines) {
            Integer goodsNumber = line.getGoodsNumber();
            if (goodsNumber == null) {
                continue;
            }

            goodsCount += goodsNumber;
            BigDecimal price = line.getPrice();
            if (price != null) {
                // 未税商品总价
                BigDecimal goodsTotalPrice = price.multiply(new BigDecimal(goodsNumber));
                totalUntaxPrice = totalUntaxPrice.add(goodsTotalPrice);

                // 进项税
                BigDecimal inputTax = line.getInputTax();
                if (inputTax != null && inputTax.doubleValue() > 0) {
                    BigDecimal rate = MathOperation.div(inputTax.add(BigDecimal.valueOf(100)), BigDecimal.valueOf(100), 2);
                    BigDecimal totalFee = MathOperation.mul(goodsTotalPrice, rate, 2);
                    totalPrice = totalPrice.add(totalFee);
                } else {
                    totalPrice = totalPrice.add(goodsTotalPrice);
                }
            }

        }
        //商品数量
        record.setGoodsCount(goodsCount);
        // 订单未税总价
        record.setTotalUntaxFee(totalUntaxPrice);
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

        orderDistributeMapper.updateByPrimaryKeySelective(updateDist);
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
            line.setMasterOrderSn(item.getMasterOrderSn());
			line.setPurchaseOrderCode(orderSn);
			line.setGoodsColorName(item.getGoodsColorName());
            line.setGoodsSn(item.getGoodsSn());
			line.setCustomCode(item.getCustomCode());
			line.setGoodsName(item.getGoodsName());
			line.setDepotCode(item.getDepotCode());
			line.setGoodsNumber(item.getGoodsNumber());
			line.setGoodsThumb(item.getGoodsThumb());
			line.setGoodsSizeName(item.getGoodsSizeName());
            line.setPrice(MathOperation.setScale(item.getCostPrice(), 2));
            line.setSupplierCode(item.getSupplierCode());
            line.setInputTax(item.getInputTax());
            line.setOutputTax(item.getOutputTax());
			orderLines.add(line);
		}
		return orderLines;
	}
	
	private void setGoodsNumber(MasterOrderGoods ogBean, MasterOrderGoods ogs) {
		ogs.setGoodsNumber(ogs.getGoodsNumber() + ogBean.getGoodsNumber());
	}

    /**
     * 订单推送供应链
     * @param masterOrderSn 订单号
     * @param actionUser 操作人
     * @param actionUserId 操作人id
     * @param supplierCode 供应商编码
     * @param type 0买家->超市，1超市->供应商, 2 买家->店铺
     */
    @Override
	public void pushJointPurchasing(String masterOrderSn, String actionUser, String actionUserId, String supplierCode, int type) {
        //校验订单类型；联采订单供应商编码不能为空，普通订单必须为企业用户
        logger.info("订单推送供应链信息：订单号" + masterOrderSn + ",供应商编码" + supplierCode + ",推送类型" + type);
        Map<String, Object> paramMap = new HashMap<String, Object>(4);
        paramMap.put("masterOrderSn", masterOrderSn);
        paramMap.put("isHistory", 0);
        //查询主单信息（主单表、扩展表、地址信息表）
        MasterOrderDetail master = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
        if (master == null) {
            logger.error(masterOrderSn + "订单推送供应链推送：订单信息异常");
            return;
        }

        if (type == 1) {
            // 超市与供应商
        } else {
            //用户-》超市，校验是否已推送
            if (master.getPushSupplyChain() != null && master.getPushSupplyChain() == 1) {
                // 0 买家与超市、2买家与店铺
                if (type == 0 || type == 2) {
                    logger.error(masterOrderSn + "订单推送供应链推送：订单已推送供应链");
                    return;
                }
            }

            Integer createOrderType = master.getCreateOrderType();
            //联采订单，或交货单
            if (createOrderType == 1 || (type == 1 && master.getNeedSign() == 1)) {
                if (StringUtils.isBlank(supplierCode)) {
                    logger.error(masterOrderSn + "订单推送供应链推送：供应商编码为空");
                    return;
                }
                //正常订单需要签章
            } else if (createOrderType == 0 && master.getNeedSign() == 1) {
                if (StringUtils.isBlank(master.getCompanyId())) {
                    logger.error(masterOrderSn + "订单推送供应链推送：下单人不为企业用户");
                    return;
                }
            } else {
                logger.error(masterOrderSn + "订单推送供应链推送：未知订单类型");
                return;
            }
        }
        paramMap = new HashMap<String, Object>(6);
        paramMap.put("masterOrderSn", masterOrderSn);
        paramMap.put("actionUser", actionUser);

        if (StringUtils.isBlank(actionUserId)) {
            actionUserId = "000000";
        }
        paramMap.put("actionUserId", actionUserId);

        if (StringUtils.isNotBlank(supplierCode)) {
            paramMap.put("supplierCode", supplierCode);
            OrderDistributeExample distributeExample = new OrderDistributeExample();
            distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andSupplierCodeEqualTo(supplierCode);
            List<OrderDistribute> orderDistributes = orderDistributeMapper.selectByExample(distributeExample);
            if (StringUtil.isListNotNull(orderDistributes)) {
                paramMap.put("orderSn", orderDistributes.get(0).getOrderSn());
            }
        }
        paramMap.put("type", type);
        logger.info("订单推送供应链推送:" + JSONObject.toJSONString(paramMap));
	    jointPurchasingProcessJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(paramMap)));
    }

    /**
     * 更新采购单签章状态，合同号
     * @param purchaseOrder
     * @return
     */
    @Override
    public ReturnInfo<String> updatePushSupplyChain(PurchaseOrder purchaseOrder) {
        ReturnInfo<String> info = new ReturnInfo<>(Constant.OS_NO, "更新采购单签章状态失败");
        if (purchaseOrder == null) {
            info.setMessage("参数为空");
            return info;
        }

        if (StringUtils.isBlank(purchaseOrder.getPurchaseOrderCode())) {
            info.setMessage("采购单号为空");
            return info;
        }

        PurchaseOrder order = purchaseOrderMapper.selectByPrimaryKey(purchaseOrder.getPurchaseOrderCode());
        if (order == null) {
            info.setMessage("采购单信息获取失败");
            return info;
        }

		if (order.getPushSupplyChain() == null) {
			info.setMessage("签章状态为空");
			return info;
		}

        // 签章状态
        Byte signComplete = order.getSignComplete();
        if (signComplete != null && signComplete == 1) {
			info.setIsOk(Constant.OS_YES);
			info.setMessage("采购单已签章");
			return info;
		}

        try {
            int update = purchaseOrderMapper.updateByPrimaryKeySelective(purchaseOrder);
            if (update > 0) {
                info.setIsOk(Constant.OS_YES);
                info.setMessage("更新采购单签章状态,合同号成功");
            }
        } catch (Exception e) {
            logger.error("更新采购单签章状态,合同号异常" + JSONObject.toJSONString(purchaseOrder), e);
            info.setMessage("更新采购单签章状态,合同号异常");
        }

        return info;
    }

    /**
     * 根据主键更新采购单
     * @param purchaseOrder
     */
    @Override
    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderMapper.updateByPrimaryKeySelective(purchaseOrder);
    }

	/**
	 * 采购订单下发
	 * @param orderSn
	 */
	private void pushPurchaseOrder(String orderSn) {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("orderSn", orderSn);
		supplierOrderSendJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(paramMap)));
	}

	/**
	 * 采购订单下发
	 * @param orderSn
	 */
	private void pushPurchaseStoreOrder(String orderSn,String vendorCode) {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("orderSn", orderSn);
		paramMap.put("vendorCode", vendorCode);
		supplierStoreOrderSendJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(paramMap)));
	}
	/**
	 * 店铺采购扣除数量消息下发
	 * @param orderSn
	 */
	private void pushSupplierProductDecuctCount(String orderSn, String su) {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("orderSn", orderSn);
		supplierOrderSendJmsTemplate.send(new TextMessageCreator(JSONObject.toJSONString(paramMap)));
	}
    /**
     * 根据主订单号批量更新
     * @param purchaseOrder
     */
    @Override
	public void updateBatchByMasterOrderSn(PurchaseOrder purchaseOrder) {
        PurchaseOrderExample example = new PurchaseOrderExample();
        example.or().andMasterOrderSnEqualTo(purchaseOrder.getMasterOrderSn());
        purchaseOrderMapper.updateByExampleSelective(purchaseOrder, example);
    }
}
