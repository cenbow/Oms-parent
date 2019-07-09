/**
 *缺货问题单 
 ***/
Ext.define('MB.model.ShortageQuestionModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'id' },
		{ name: 'orderSn' },
		{ name: 'customCode' },
		{ name: 'questionType'},
		{ name: 'orderQuestionId'},
		{ name: 'depotCode'},
		{ name: 'deliverySn'},
		{ name: 'lackReason'}
	]
});