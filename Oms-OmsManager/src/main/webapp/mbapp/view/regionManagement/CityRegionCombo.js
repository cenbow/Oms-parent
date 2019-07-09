/**
 * 查询条件：城市下拉菜单
 */
Ext.define("MB.view.regionManagement.CityRegionCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.cityRegionCombo',
	store : 'MB.store.CityRegionStore',
	name : 'cityRegion',
	displayField : 'regionName',
	valueField : 'regionId',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>城市</b>',
	hiddenName : 'cityRegion',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, record,index){
			var districtRegionCombo = Ext.widget("districtRegionCombo");
			var districtRegionDataStore = districtRegionCombo.store;
			districtRegionDataStore.getProxy().url=basePath+'custom/common/getRegionQueryCondition.spmvc?parentId='+combo.value;
			districtRegionDataStore.load();
			Ext.getCmp('regionQueryPanlViewDistrictRegion').setValue('');
		}
	}
});