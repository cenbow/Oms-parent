package com.work.shop.oms.mq.listener;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderBillList;
import com.work.shop.oms.bean.OrderBillListExample;
import com.work.shop.oms.bean.OrderSettleBillExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.dao.OrderBillListMapper;
import com.work.shop.oms.dao.OrderSettleBillMapper;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.vo.SettleBillQueue;

/**
 * 订单结算账单消费
 */
@Service("settleOrderBillConsumer")
public class SettleOrderBillConsumer extends Consumer {

	private static Logger log = Logger.getLogger(SettleOrderBillConsumer.class);

	@Resource
	private OrderReturnStService orderReturnStService;
	
	//@Resource
	private OrderSettleService orderSettleService;
	
	@Resource(name="orderSettleBillMapper")
	private OrderSettleBillMapper orderSettleBillMapper;

	@Resource(name="orderBillListMapper")
	private OrderBillListMapper orderBillListMapper;
	
	@Override
	public String getDATA(String text) {
		String billNo = "";
		long startTime = System.currentTimeMillis();
		try {
			log.info("settleOrderBillConsumer-settleObj:" + text);
			SettleBillQueue settleObj = JSON.parseObject(text, SettleBillQueue.class);
			billNo = settleObj.getBillNo();
			OrderBillList orderBillList = orderBillListMapper.selectByPrimaryKey(settleObj.getBillNo());
			if(orderBillList == null){
				throw new RuntimeException("调整单据批次号数据库不存在"+settleObj.getBillNo());
			}
			if(orderBillList.getIsSync().intValue() == 2){
				throw new RuntimeException("结算单据已作废");
			}
			if(orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.ORDER_SETTLE.intValue()
					||orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.ORDER_DELIVERY_PAY.intValue()){
				//订单结算、货到付款结算
			    orderSettleService.callSettleOrder(settleObj);
			}else if(orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.RETURN_SETTLE.intValue()){
				//退单结算
				orderReturnStService.callSettleReturnSharePay(settleObj);
			} else if(orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.DEPOSIT_SETTLE.intValue()){
				//保证金
				orderReturnStService.updateDeposit(settleObj);
			}else if(orderBillList.getBillType().intValue() == ConstantValues.ORDER_SETTLE_BILL_TYPE.ORDER_LOG_SETTLE.intValue()){
				//调整日志
				orderReturnStService.updateOrderInfoOrOrderReturnLog(settleObj);
			}else{
				throw new RuntimeException("无法解析结算单据类型："+orderBillList.getBillType());
			}
			
		} catch (Exception e) {
			log.error("settleOrderBillConsumer.exception-settleObj:" + text+",msg:"+e.getMessage(),e);
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}finally{
			//回写调整单数据 - 同步标志
			OrderSettleBillExample orderSettleBillExample = new OrderSettleBillExample();
			OrderSettleBillExample.Criteria criteria = orderSettleBillExample.or();
			criteria.andBillNoEqualTo(billNo);
//			boolean updateFlag = true;//是否更新order_bill_list里面的同步状态，只有当所有的调整单都执行完毕后才开始更新（即所有的调整单的状态都不是0未定义的时候）
	//		andResultStatusNotEqualTo(ConstantValues.YESORNO_NO.byteValue());
//			List<OrderSettleBill> succSettleOrder = orderSettleBillMapper.selectByExample(orderSettleBillExample);
			int totalSettleBillNum = orderSettleBillMapper.countByExample(orderSettleBillExample);//总结算条数
			if(totalSettleBillNum > 0){
			    criteria.andResultStatusEqualTo(ConstantValues.YESORNO_YES.byteValue());
				int successNum = orderSettleBillMapper.countByExample(orderSettleBillExample);//结算成功条数
				
				OrderSettleBillExample billExample = new OrderSettleBillExample();
				billExample.or().andBillNoEqualTo(billNo).andResultStatusEqualTo(ConstantValues.YESORNO_NO.byteValue());
				
				int unExecutedNum =orderSettleBillMapper.countByExample(billExample);//未执行的调整单数目
				
				
				/*for(OrderSettleBill orderSettleBill:succSettleOrder){
					if(orderSettleBill.getResultStatus()==ConstantValues.YESORNO_YES.byteValue()){
						successNum++;
					}
					if(orderSettleBill.getResultStatus().intValue() == ConstantValues.YESORNO_NO.intValue()){
						updateFlag = false;
						break;
					}
				}*/
				OrderBillListExample orderBillListExample = new OrderBillListExample();
				orderBillListExample.or().andBillNoEqualTo(billNo);
				OrderBillList updateOrderBillList = new OrderBillList();
				if(unExecutedNum <= 0){
					if(successNum == totalSettleBillNum){//全部同步成功
						updateOrderBillList.setIsSync(ConstantValues.SYNC_SUCCESS.byteValue());
					}else if(successNum != 0){//部分同步
						updateOrderBillList.setIsSync(ConstantValues.SYNC_SUCCESS_PARTIAL.byteValue());
					}else{//同步失败
						updateOrderBillList.setIsSync(ConstantValues.SYNC_FAIL.byteValue());
					}
					log.info("settleOrderBillConsumer-updateOrderBillList:" + updateOrderBillList.getIsSync());
					updateOrderBillList.setUpdateTime(new Date());
					orderBillListMapper.updateByExampleSelective(updateOrderBillList, orderBillListExample);
					log.info("settleOrderBillConsumer-updateOrderBillList:同步更新状态成功");
					
				}
			}
			log.info("settleOrderBillConsumer：billNo："+billNo+",结算耗时："+(System.currentTimeMillis()-startTime)+"ms");
		}
		return null;
	}

}
