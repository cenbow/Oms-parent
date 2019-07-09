/**
 * 是否支持自提
 */
Ext.define("MB.view.regionManagement.IsCacCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isCacCombo',
	store : 'MB.store.IsCacStore',
	name : 'isCac',
	hiddenName : 'isCac',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});