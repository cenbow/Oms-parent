/*
 * 换单Model定义
 */
Ext.define('MB.model.ExchangeDetailModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'masterOrderSn' },//换货单号
		{ name : 'orderStatusName'},//订单状态
		{ name : 'orderStatus'},//订单状态
		{ name : 'isAgent' },//是否代理换货
		{ name : 'userName' },//收货人
		{ name : 'clearTime',convert:function(value){ 
			if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}
         }  },//结算时间
		{ name : 'addTime',convert:function(value){ 
			if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}
         }  },//下单时间
		{ name : 'payMent' },//支付方式
		{ name : 'userPayTime',convert:function(value){ 
			if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}
         }  },//付款时间
		{ name : 'orderShipName' },//配送方式
		{ name : 'deliveryTime',convert:function(value){ 
			if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			}
         }  },//发货时间
		{ name : 'referer'},//订单来源
		{ name : 'orderType'},//订单类型
		{ name : 'relatingOriginalSn'},//关联原订单号
		{ name : 'relatingReturnSn'},//关联退单号
		{ name : 'relatingRemoneySn'},//关联退款单号
		
		
		
		{ name : 'goodsAmount'},//商品总金额
		{ name : 'discount'},//折让金额
		{ name : 'shippingTotalFee'},//配送费用
		{ name : 'totalFee'},//订单总金额
		{ name : 'moneyPaid'},//已付款金额
		{ name : 'bonus'},//使用红包
		{ name : 'totalPayable'}//应再付款金额
		
		
		]
		
});

