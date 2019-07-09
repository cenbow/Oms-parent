Ext.define('MB.model.OrderShipEdit', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'consignee' },				// 收货人
		{ name : 'orderSn' },				// 订单编码
		{ name : 'countryN' },				// 所在国家
		{ name : 'provinceN' },				// 所在省
		{ name : 'cityN' },					// 所在市
		{ name : 'districtN' },				// 所在区
		{ name : 'streetN' },				// 所在街道
		{ name : 'email'},					// 电子邮件
		{ name : 'address'},				// 地址
		{ name : 'zipcode'},				// 邮编
		{ name : 'tel'},					// 电话
		{ name : 'mobile'},					// 手机
		{ name : 'signBuilding'},			// 标志性建筑
		{ name : 'bestTime'}		 		// 最佳送货时间
	]
});