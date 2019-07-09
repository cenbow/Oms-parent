/*
 * 退单详情展示Model定义
 */
Ext.define('MB.model.ReturnCommonModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'returnSn' },//退单号
		{ name : 'returnDesc' },//退单备注
		{ name : 'relatingOrderSn' },//关联订单号
		{ name : 'returnType' },//退单类型
		{ name : 'returnOrderStatus' },//退单状态
		{ name : 'isGoodReceived' },//是否收货
		{ name : 'chasedOrNot' },//是否追单
		{ name : 'returnSettlementType' },//预付款/保证金
		{ name : 'returnExpress' },//配送方式
		{ name : 'returnInvoiceNo' },//退货单快递单号
		{ name : 'processType' },//处理状态
		{ name : 'returnReason' },//退单原因
		{name:'addTime',convert:function(value){ 
			if(value){
            var addTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return addTime;  
			}
         } },//退单时间
         {name:'checkInTime',convert:function(value){ 
        	 if(value){
        		 var checkInTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
        		 return checkInTime;  
        	 }
         } },//入库时间
         {name:'clearTime',convert:function(value){ 
        	 if(value){
        		 var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
        		 return createTime;  
        	 }
         } },//结算时间
		{ name : 'newOrderSn' },//换货订单
		{ name : 'depotCode' },//退货仓库
		
		{ name : 'returnTotalFee' },//退款总金额
		{ name : 'returnGoodsMoney' },//退商品金额（已减折让）
		{ name : 'totalPriceDifference' },//退商品总差价
		{ name : 'returnShipping' },//退配送费用
		{ name : 'returnOtherMoney' },//退其他费用
		{ name : 'returnBonusMoney' }//退红包金额
		]
});

