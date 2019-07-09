/**
 * 查询条件：区县下拉菜单
 */
Ext.define("MB.view.regionManagement.DistrictRegionCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.districtRegionCombo',
	store : 'MB.store.DistrictRegionStore',
	name : 'districtRegion',
	displayField : 'regionName',
	valueField : 'regionId',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>区县</b>',
	hiddenName : 'districtRegion',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}/*,
	listeners: {
		change : function(combo, record,index){
			var provinceRegionCombo = Ext.widget("provinceRegionCombo");
			var provinceRegionDataStore = provinceRegionCombo.store;
			provinceRegionDataStore.getProxy().url=basePath+'custom/common/geQueryRegion.spmvc?parentId='+combo.value;
			provinceRegionDataStore.load();
			Ext.getCmp('regionQueryPanlViewProvinceRegion').setValue('');
		}
	}*/
});