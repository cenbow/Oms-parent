Ext.define("MB.view.common.CommonStatus", {
	extend: "Ext.form.field.ComboBox",
//	id:'commonStatus',
	alias: 'widget.commoncommonStatus',
	store: 'CommonStatusStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
//	fieldLabel: '支付方式',
	hiddenName: 'id',
	emptyText: '请选择',
	editable: false
	/*listeners : {
      afterRender : function(combo) {
      	console.dir(combo);
         var firstValue = combo.store.reader.jsonData[0].text;
         combo.setValue(firstValue);//同时下拉框会将与name为firstValue值对应的 text显示
      }
   }*/
});