
Ext.define('MB.model.DeliveryDetailModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'depotCode' },//仓库
		{ name : 'carriers' },//承运商
		{ name : 'invoiceNo'},//快递单号
		{ name : 'custumCode'},//商品sku
		{ name : 'goodsNumber'},//商品数量
		{ name : 'orderSn'}//商品状态
		
		]
		
});