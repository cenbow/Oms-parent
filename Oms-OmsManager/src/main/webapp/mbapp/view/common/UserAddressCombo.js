Ext.define("MB.view.common.UserAddressCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.userAddressCombo',
	store: 'UserAddressStore',
	name: 'addressId',
	displayField: 'addressName',
	valueField: 'addressId',
	queryMode: 'remote',
	width: 220,
	labelWidth: 70,
	fieldLabel: '选择收货地址',
	hiddenName: 'addressId',
	emptyText: '请选择收货地址',
	editable: false
});