Ext.define("MB.view.common.InvoicesOrganizationCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.invoicesorganizationcombo',
	store: 'InvoicesOrganizationStore',
	name: 'invoicesOrganization',
	displayField: 'n',
	valueField: 'v',
	queryMode: 'local',
	width: 220,
	labelWidth: 70,
	fieldLabel: '单据组织',
	hiddenName: 'reason',
	emptyText: '请选择单据组织',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});