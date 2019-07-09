/**
 *时间类型
 ***/

//时间查询
var selectTimeType = [
	{v: 'addTime', n: '下单时间'},
	{v: 'confirmTime', n: '确认时间'}
];

Ext.define("MB.store.TimeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: selectTimeType
});