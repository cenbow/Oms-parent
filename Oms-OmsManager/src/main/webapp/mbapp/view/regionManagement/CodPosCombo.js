/**
 * 是否支持POS刷卡
 */
Ext.define("MB.view.regionManagement.CodPosCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.codPosCombo',
	store : 'MB.store.CodPosStore',
	name : 'codPos',
	hiddenName : 'codPos',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});