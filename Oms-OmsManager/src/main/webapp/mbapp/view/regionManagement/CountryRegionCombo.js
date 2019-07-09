/**
 * 查询条件：国家下拉菜单
 */
Ext.define("MB.view.regionManagement.CountryRegionCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.countryRegionCombo',
	store : 'MB.store.CountryRegionStore',
	name : 'countryRegion',
	displayField : 'regionName',
	valueField : 'regionId',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>国家</b>',
	hiddenName : 'countryRegion',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, record,index){
			var provinceRegionCombo = Ext.widget("provinceRegionCombo");
			var provinceRegionDataStore = provinceRegionCombo.store;
			provinceRegionDataStore.getProxy().url=basePath+'custom/common/getRegionQueryCondition.spmvc?parentId='+combo.value;
			provinceRegionDataStore.load();
			Ext.getCmp('regionQueryPanlViewProvinceRegion').setValue('');
		}
	}
});