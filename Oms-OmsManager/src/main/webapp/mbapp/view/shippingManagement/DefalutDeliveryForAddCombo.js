/**
 * 新增面板入参：是否默认配送方式
 */
Ext.define("MB.view.shippingManagement.DefalutDeliveryForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.defalutDeliveryForAddCombo',
	store : 'MB.store.DefalutDeliveryForAddStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>是否默认配送方式</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});