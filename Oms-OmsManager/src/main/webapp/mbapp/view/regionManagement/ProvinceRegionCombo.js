/**
 * 查询条件：国家下拉菜单
 */
Ext.define("MB.view.regionManagement.ProvinceRegionCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.provinceRegionCombo',
	store : 'MB.store.ProvinceRegionStore',
	name : 'provinceRegion',
	displayField : 'regionName',
	valueField : 'regionId',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>省份</b>',
	hiddenName : 'provinceRegion',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, record,index){
			var cityRegionCombo = Ext.widget("cityRegionCombo");
			var cityRegionDataStore = cityRegionCombo.store;
			cityRegionDataStore.getProxy().url=basePath+'custom/common/getRegionQueryCondition.spmvc?parentId='+combo.value;
			cityRegionDataStore.load();
			Ext.getCmp('regionQueryPanlViewCityRegion').setValue('');
		}
	}
});