/**
 * 新增面板入参：是否货到付款模板
 */
Ext.define("MB.view.shippingManagement.IsReceivePrintForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isReceivePrintForAddCombo',
	store : 'MB.store.IsReceivePrintForAddStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>是否货到付款模板</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});