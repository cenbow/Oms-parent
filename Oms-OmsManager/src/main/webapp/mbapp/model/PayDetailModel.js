
Ext.define('MB.model.PayDetailModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'paySn' },//付款单编号
		{ name : 'payName' },//支付方式
		{ name : 'payNote'},//付款备注
		{ name : 'bonusName'},//使用红包
		{ name : 'goodsNumber'},//商品数量
		{ name : 'surplus'},//余额支付
		{ name : 'payTotalfee'},//付款总金额
		{ name : 'payStatus'},//支付状态
		{ name : 'payTime',convert:function(value){  
           if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			} 
			}
		},//支付时间
		{ name : 'userPayTime',convert:function(value){  
           if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			} 
			}},//客户支付时间
		{ name : 'payLasttime',convert:function(value){  
           if(value){
            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
             return createTime;  
			} 
			}},//付款最后期限
		{ name : 'payStatus'}//操作
		
		]
		
});