package com.work.shop.oms.orderop.service;

import com.work.shop.oms.bean.SettleOrderInfo;
import com.work.shop.oms.common.bean.OptionPointsBean;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 平台用户积分服务
 * @author lemon
 *
 */
public interface UserPointsService {

	
	/**
	 * 赠送消费积分(暂时提供给订单结算和换货单结算)
	 * @param uid - 用户id
	 * @param points - 积分
	 */
	public ReturnInfo<String> giveUserPoints(String uid,Integer points,String orderSn);
	
	/**
	 * 扣减积分（积分使用）
	 * @param uid - 用户id
	 * @param points - 积分
	 * @param dealCode
	 * @return
	 */
	public ReturnInfo<String> deductionsPoints(String uid, Integer points,String dealCode);

	/**
	 * 积分赠送、积分扣减(新)
	 * @param settleOrderInfo
	 * @return
	 */
	public ReturnInfo<String> processUserPoints(SettleOrderInfo settleOrderInfo);
	
	
	/**
	 * 查询用户点数
	 * @param userName
	 * @param orderAmount
	 * @return
	 */
	public Integer searchUserPoints(String userId, Double orderAmount);

	/**
	 * 操作点数
	 * 	userId:用户id 必须
	 * 	orderAmount:订单金额 必须
	 * 	orderNo：订单编号 必须
	 * 	orderPoints:订单使用点数 必须
	 * 	orderBv:订单bv 必须
	 * 	orderType: 操作类型  1冻结资金、2释放资金、3消费资金、4退返资金
	 * 
	 * @param pointsBean
	 * @return
	 */
	public ReturnInfo<Integer> optionPoints(OptionPointsBean pointsBean);
	
	/**
	 * 新美力 bvvalue 使用
	 * @param uid 用户ID
	 * @param bvValue bv值
	 * @param dealCode 流水号
	 * @param bvStatus 处理Bv状态 0：赠送; 1：扣减
	 * @return
	 */
	public ReturnInfo<String> customerUseBv(String uid, Integer bvValue,String dealCode, String bvStatus);
	
	/**
	 * 克缇丽娜 bvvalue 使用
	 * @param uid 用户ID
	 * @param bvValue bv值
	 * @param dealCode 流水号
	 * @param bvStatus 处理Bv状态 0：赠送; 1：扣减
	 * @return
	 */
	public ReturnInfo<String> customerUseBvByKT(String uid, Integer bvValue,String dealCode, String bvStatus);
}
