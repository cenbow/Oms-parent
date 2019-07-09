Ext.define("MB.view.regionManagement.RegionQueryViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.regionQueryViewport',
	requires: [
	           'MB.view.regionManagement.RegionQueryPanlView',
	           'MB.view.regionManagement.RegionQueryGridView',
	           'MB.store.CountryRegionStore',
		       'MB.model.QueryRegionModel',
		       'MB.view.regionManagement.CountryRegionCombo',
		       'MB.store.ProvinceRegionStore',
		       'MB.view.regionManagement.ProvinceRegionCombo',
		       'MB.store.CityRegionStore',
		       'MB.view.regionManagement.CityRegionCombo',
		       'MB.store.DistrictRegionStore',
		       'MB.view.regionManagement.DistrictRegionCombo',
		       'MB.model.RegionModel',
		       'MB.store.RegionStore',
		       'MB.view.regionManagement.RegionAdd',
		       'MB.model.ComboModel',
		       'MB.store.IsCodStore',
		       'MB.view.regionManagement.IsCodCombo',
		       'MB.store.CodPosStore',
		       'MB.view.regionManagement.CodPosCombo',
		       'MB.store.IsCacStore',
		       'MB.view.regionManagement.IsCacCombo',
		       'MB.store.IsVerifyTelStore',
		       'MB.view.regionManagement.IsVerifyTelCombo',
		       'MB.view.regionManagement.RegionEdit'],
	items : [ {
		title : '查询条件',
		xtype: 'regionQueryPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		title : '查询列表',
		xtype: 'regionQueryGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});