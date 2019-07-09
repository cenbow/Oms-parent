/**
 *时间类型
 ***/

//时间查询
var selectTimeTypeOfOrderReturn = [
	{v: 'addTime', n: '生成时间'},
	{v: 'checkinTime', n: '入库时间'},
	{v: 'confirm_time', n: '确定时间'}
];

Ext.define("MB.store.TimeOfOrderReturnStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: selectTimeTypeOfOrderReturn
});