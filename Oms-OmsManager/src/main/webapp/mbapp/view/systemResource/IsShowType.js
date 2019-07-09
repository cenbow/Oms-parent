/**
 *是否显示 
 ***/
Ext.define("MB.view.systemResource.IsShowType", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isShowType',
	store : 'IsShowStore',
	name : 'isShow',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>是否显示</b>',
	hiddenName : 'isShow',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});