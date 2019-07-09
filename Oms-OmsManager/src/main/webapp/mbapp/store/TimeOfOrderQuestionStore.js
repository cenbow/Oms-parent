/**
 *时间类型
 ***/

//时间查询
var selectTimeTypeOfOrderQuestion = [
	{v: 'addTime', n: '订单生成时间'},
	{v: 'questionTime', n: '问题时间'}

	
];

Ext.define("MB.store.TimeOfOrderQuestionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: selectTimeTypeOfOrderQuestion
});