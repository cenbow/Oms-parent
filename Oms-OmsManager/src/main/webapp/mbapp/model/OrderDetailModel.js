/*
 * 订单详情展示Model定义
 */
Ext.define('MB.model.OrderDetailModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'orderSn' },//订单号
		{ name : 'orderOutSn' },//外部交易号
		{ name : 'referer'},//订单来源
		{ name : 'orderStatusName'},//订单状态
		{name:'clearTime',convert:function(value){ 
			if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}
         } },//结算时间
		{ name : 'userName' },//购货人
		{ name : 'addTime',convert:function(value){  
           if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}  
         } 
         },//下单时间
//		{ name : 'transType' },//订单交易类型
		{ name : 'transTypeStr' },//订单交易类型
//		{ name : 'depotStatus'},//分仓发货状态
//		{ name : 'orderType'},//订单类型
		{ name : 'orderTypeStr'},//订单类型
		{ name : 'relatingExchangeSn'}, // 关联原订单号
		{ name : 'returnSn'}, // 订单关联退单号
//		{ name : 'orderCategory'},//订单种类
		{ name : 'orderCategoryStr'},//订单种类
		{ name : 'isGroup'},//团购订单
		{ name : 'isAdvance'},//订单含预售商品
		{ name : 'cancelReason'},//取消订单原因
//		{ name : 'questionReason'},//问题单原因
		{ name : 'reason'},//原因
		
//		{ name : 'consignee'}//收货人
		
		{ name : 'invType'},//发票类型
		{ name : 'invPayee'},//发票抬头
		{ name : 'invContent'},//发票内容
		{ name : 'howOos'},//缺货处理
		{ name : 'postscript'},//客户给商家的留言
		
		
		{ name : 'goodsAmount'},//商品总金额
		{ name : 'totalPriceDiscount'},//商品总价折扣
		{ name : 'tax'},//发票税额
		{ name : 'discount'},//折让金额
		{ name : 'shippingTotalFee'},//配送费用
		{ name : 'moneyPaid'},//已付款金额
		{ name : 'insureTotalFee'},//保价费用
		{ name : 'surplus'},//使用余额
		{ name : 'payTotalFee'},//支付费用
		{ name : 'integralMoney'},//使用积分
		{ name : 'masterOrderSn' },
		{ name : 'totalFee'},//订单总金额
		{ name : 'payTotalFee'}//应付款金额
		
		
		]
		
});

