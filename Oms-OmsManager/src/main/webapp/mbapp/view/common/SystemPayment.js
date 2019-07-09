Ext.define("MB.view.common.SystemPayment", {
	extend: "Ext.form.field.ComboBox",
//	id:'systemPayment',
	alias: 'widget.commonsystemPayment',
	store: 'SystemPaymentStore',
	name: 'payId',
	displayField: 'payName',
	valueField: 'payId',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	fieldLabel: '支付方式',
	hiddenName: 'payId',
	emptyText: '请选择支付方式',
	editable: false
	/*listeners : {
      afterRender : function(combo) {
      	console.dir(combo);
         var firstValue = combo.store.reader.jsonData[0].text;
         combo.setValue(firstValue);//同时下拉框会将与name为firstValue值对应的 text显示
      }
   }*/
});