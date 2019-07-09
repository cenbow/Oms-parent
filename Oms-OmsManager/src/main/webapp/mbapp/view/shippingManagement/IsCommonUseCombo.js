Ext.define("MB.view.shippingManagement.IsCommonUseCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isCommonUseCombo',
	store :  Ext.create('Ext.data.Store', {
		fields: ['v', 'n'],
		data:[['0','否'],['1','是']]
	}),
	name : 'isCommonUse',
	hiddenName : 'isCommonUse',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>是否常用</b>',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});