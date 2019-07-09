/**
 * 区域信息查询条件
 */
var pageSize = 20;
Ext.define("MB.view.regionManagement.RegionQueryPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.regionQueryPanlView',
	frame : true,
	head : true,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		labelAlign : 'right'
	},
	collapsible : true,
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 80,
			margin: '20 20 0 0',
			items: [{
				xtype : 'countryRegionCombo',
				id : 'regionQueryPanlViewCountryRegion',
				name : 'countryRegion',
				width : 200,
				columnWidth : .25
			},{
				xtype : 'provinceRegionCombo',
				id : 'regionQueryPanlViewProvinceRegion',
				name : 'provinceRegion',
				width : 200,
				columnWidth : .25
			},{
				xtype : 'cityRegionCombo',
				id : 'regionQueryPanlViewCityRegion',
				name : 'cityRegion',
				width : 200,
				columnWidth : .25
			},{
				xtype : 'districtRegionCombo',
				id : 'regionQueryPanlViewDistrictRegion',
				name : 'districtRegion',
				width : 200,
				columnWidth : .25
			}]
		}];
		me.buttons = [ {
			text : '添加下级区域',
			action : 'openAdd'
		}, {
			text : '查询',
			action : 'search'
		}, {
			text : '重置',
			action : 'reset'
		} ];
		me.callParent(arguments);
	}
});

