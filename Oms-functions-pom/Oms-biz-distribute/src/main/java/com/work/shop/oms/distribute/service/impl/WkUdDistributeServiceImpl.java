package com.work.shop.oms.distribute.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderQuestionMapper;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.ship.bean.DistributeRequest;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.ship.service.WkUdDistributeService;

/**
 * 供应商订单分仓结果服务
 * @author lemon
 */
@Service
public class WkUdDistributeServiceImpl implements WkUdDistributeService {

	private Logger log = Logger.getLogger(WkUdDistributeServiceImpl.class);

	@Resource(name = "noticeDistributeProducerJmsTemplate")
	private JmsTemplate jmsTemplate;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private OrderDistributeService orderDistributeService;

	/**
	 * 接受供应商分仓结果数据	
	 * @param depots 分仓结果
	 * @return boolean
	 */
	@Override
	public boolean depot(final List<WkUdDistribute> depots) {
		String msg = null;
		try {
			msg = JSON.toJSONString(depots);
			final String msg1 = msg;
			log.info("os收到分仓数据" + msg);
			jmsTemplate.send(new TextMessageCreator(msg1));
			log.debug("发送到MQ结束" + msg);
		} catch (Exception e) {
			log.error("os收到分仓数据异常",e);
			if(msg == null) {
				return false;
			}
			for (int i = 0; i < 3; i++) {
				try {
					jmsTemplate.send(new TextMessageCreator(msg));
					continue;
				}catch (Exception e1) {
					log.error("os收到分仓数据重试异常",e1);
				}
			}
			return false;
		}
		return true;
	}

    /**
     *  订单/交货单分仓
     *  orderSn不为空走交货单分配
     *  orderSn为空走订单分配
     * @param distributeRequest
     */
    @Override
    public ReturnInfo<Boolean> depotByOrder(DistributeRequest distributeRequest) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(0, "分配失败");
        if (distributeRequest == null) {
            returnInfo.setMessage("参数不能为空");
             return returnInfo;
        }

        String masterOrderSn = distributeRequest.getMasterOrderSn();
        if (StringUtils.isBlank(masterOrderSn)) {
            returnInfo.setMessage("订单号不能为空");
            return returnInfo;
        }

        String deoptCode = distributeRequest.getDeoptCode();
        if (StringUtils.isBlank(deoptCode)) {
            returnInfo.setMessage("仓库编码不能为空");
            return returnInfo;
        }

        String orderSn = distributeRequest.getOrderSn();
        if (StringUtils.isBlank(orderSn)) {
            //交货单号为空，整单分配
            returnInfo = artificialDepotByMasterOrderSn(distributeRequest);
        } else {
            //交货单分配
            returnInfo = artificialDepotByOrderSn(distributeRequest, null);
        }


        return returnInfo;
    }

    /**
     * 整单分配
     * @param distributeRequest
     * @return
     */
    private ReturnInfo<Boolean> artificialDepotByMasterOrderSn(DistributeRequest distributeRequest) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(0, "分配失败");
        List<OrderDistribute> orderDistributes = orderDistributeService.selectEffectiveDistributes(distributeRequest.getMasterOrderSn());
        if (StringUtil.isListNull(orderDistributes)) {
            returnInfo.setMessage("无有效交货单信息");
            return returnInfo;
        }

        String errMsg = "";
        for (OrderDistribute orderDistribute : orderDistributes) {
            //循环交货单分配
            String orderSn = orderDistribute.getOrderSn();
            distributeRequest.setOrderSn(orderSn);
            ReturnInfo<Boolean> info = artificialDepotByOrderSn(distributeRequest, orderDistribute);
            if (info == null) {
                errMsg +=  "交货单" + orderSn + ":分配异常" + ";";
            } else if (info.getIsOk() == 0) {
                errMsg +=  info.getMessage() + ";";
            }
        }

        if (StringUtils.isBlank(errMsg)) {
            errMsg = "订单" + distributeRequest.getMasterOrderSn() + "分配成功";
            returnInfo.setIsOk(1);
            returnInfo.setData(true);
        }
        returnInfo.setMessage(errMsg);
        return returnInfo;
    }

    /**
     * 交货单分配
     * @param distributeRequest
     */
    private ReturnInfo<Boolean> artificialDepotByOrderSn(DistributeRequest distributeRequest, OrderDistribute orderDistribute) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(0, "分配失败");
        String orderSn = distributeRequest.getOrderSn();
        if (StringUtils.isBlank(orderSn)) {
            returnInfo.setMessage("交货单号为空");
            return returnInfo;
        }

        String errMsg = "交货单" + distributeRequest.getOrderSn() + ":";

        //获取交货单信息
        if (orderDistribute == null) {
            orderDistribute = orderDistributeService.selectDistributesByOrderSn(orderSn);
        }
        if (orderDistribute == null) {
            returnInfo.setMessage(errMsg + "信息异常");
            return returnInfo;
        }

        //校验交货单分仓状态
        Byte depotStatus = orderDistribute.getDepotStatus();
        //已分仓不做分仓
        if (depotStatus != 0) {
            returnInfo.setMessage(errMsg + "已分仓");
            return returnInfo;
        }

        //校验交货单商品信息
        MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
        MasterOrderGoodsExample.Criteria criteria = goodsExample.createCriteria();
        String masterOrderSn = distributeRequest.getMasterOrderSn();
        if (StringUtils.isNotBlank(masterOrderSn)) {
            criteria.andMasterOrderSnEqualTo(masterOrderSn);
        }
        criteria.andOrderSnEqualTo(distributeRequest.getOrderSn());
        List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);

        if (StringUtil.isListNull(goodsList)) {
            returnInfo.setMessage(errMsg + "交货单无商品信息");
            return returnInfo;
        }

        //组装数据
        List<WkUdDistribute> distributeList = new ArrayList<WkUdDistribute>();
        for (MasterOrderGoods goods : goodsList) {
            WkUdDistribute distribute = new WkUdDistribute();
            distribute.setCreateDate(new Date());
            distribute.setDistWarehCode(distributeRequest.getDeoptCode());
            distribute.setRcvWarehcode(distributeRequest.getDeoptCode());
            distribute.setGoodsNumber(goods.getGoodsNumber() + "");
            distribute.setOuterCode(distributeRequest.getOrderSn());
            distribute.setProdId(goods.getCustomCode());
            distributeList.add(distribute);
        }
        boolean result = depot(distributeList);
        if (result) {
            returnInfo.setIsOk(1);
            returnInfo.setData(true);
            returnInfo.setMessage(errMsg + "分配成功");
        }

        return returnInfo;
    }

}
