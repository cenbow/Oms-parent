Ext.Loader.setConfig({
	enabled: true
});

Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.selection.CheckboxModel',
    'MB.ComboModel',
    'MB.Channel',
    'MB.Shop',
    'MB.OrderInfoQuery',
    'MB.Area',
    'Ext.dom.Element'
]);

var orderInfoForm = null;
var shippingIdOptionCombo = null;
var countryCombo = null;
var districtCombo = null;
var provinceCombo = null;
var cityCombo = null;
var myMask1 = null;

var transTypeOptionCombo = null;

var orderStatusOptionCombo = null;

var payStatusOptionCombo = null;

var  shipStatusOptionCombo = null;

var orderViewOptionCombo = null;

var orderInfoOperationReasonComb = null;

var columns = null;
var pageSize = 20;


var orderInfoGrid = null;
var orderInfoJsonStore = null;

var isHistory = 0;

var settleType = $("#settleType").val()

var orderSnValue="";
 
var cannelOrderInfoWin = null;

if("orderInfoSettle" == settleType ){//待订单结算	
	var orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc?type=orderInfoSettle";
} else{
	var orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc";
}


var batchUnconfirmUrl = basePath + "/custom/orderStatus/batchUnConfirm";

Ext.onReady(function(){
	Ext.QuickTips.init();

	var batchLockUrl = basePath + "/custom/orderStatus/batchLock.spmvc";
	var batchUnLockUrl = basePath + "/custom/orderStatus/batchUnlock.spmvc";
	var batchConfirmUrl = basePath + "/custom/orderStatus/batchConfirm";
	
	var batchswdiUrl = basePath + "/custom/orderStatus/swdi";
	
	var batchCancelUrl = basePath + "/custom/orderStatus/batchCancel";
	
	var batchIssuedUrl = basePath + "/custom/orderStatus/batchToErp";
	
	var getAreaUrl = basePath + "custom/orderInfo/getArea";

	var orderTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_type
	});
	//订单类型
	var orderTypeOptionCombo = createComboBoxLocal(orderTypeDataStore,'orderInfoPageOrderType', 'n','v', 'local','订单类型', 230, 80, 'orderType', '请选择订单类型');
	
	//订单种类
	var orderTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_category
	});
	var orderCategoryOptionCombo = createComboBoxLocal(orderTypeDataStore, 'orderInfoPageOrderCategory', 'n','v', 'local', "订单种类", 230, 80, "orderCategory","请选择订单种类");

	//交易类型
	var transTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.trans_type
	});

	transTypeOptionCombo = createComboBoxLocal(transTypeDataStore, 'orderInfoPageTransType', 'n','v', "local", "交易类型", 230, 80, "transType","请选择支付类型");
	//订单状态
	var orderStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_status
	});
	orderStatusOptionCombo= createComboBoxLocal(orderStatusDataStore, 'orderInfoPageOrderStatus', 'n','v', "local", "订单状态", 230, 80, "orderStatus","请选择订单状态");
	
	//订单来源媒体
	var refererDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.referer_type
	});

	var refererOptionCombo= createComboBoxLocal(refererDataStore, 'orderInfoPageReferer', 'n','v', "local", "referer", 230, 80, "referer","请选择订单来源媒体");
	//团购订单
	var isGroupDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.group_order
	});

	var isGroupOptionCombo= createComboBoxLocal(isGroupDataStore, 'orderInfoPageIsGroup', 'n','v', "local", "团购订单", 230, 80, "isGroup","请选择");
	//含预售商品
	
	var isAdvanceDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pre_group_order
	});
	
	var isAdvanceOptionCombo= createComboBoxLocal(isAdvanceDataStore, 'orderInfoPageIsAdvance', 'n','v', "local", "含预售商品", 230, 80, "isAdvance","请选择");
	//显示类型
	var orderViewDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_view
	});
	 orderViewOptionCombo= createComboBoxLocal(orderViewDataStore, 'orderInfoPageOrderView', 'n','v', "local", "显示类型", 230, 80, "orderView","请选择");
	//默认显示类型：1,显示有效订单
	orderViewOptionCombo.setValue('0');
	
	//支付方式
	var payIdDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pay_id
	});
	var payIdOptionCombo= createComboBoxLocal(payIdDataStore, 'orderInfoPagePayId', 'n','v', "local", "支付方式", 230, 80, "payId","请选择支付方式");
	//支付状态
	var payStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pay_status
	});
	 payStatusOptionCombo= createComboBoxLocal(payStatusDataStore, 'orderInfoPagePayStatus', 'n','v', "local", "支付状态", 230, 80, "payStatus","请选择支付状态");
	//发货状态
	var shipStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.ship_status
	});
	shipStatusOptionCombo= createComboBoxLocal(shipStatusDataStore, 'orderInfoPageShipStatus', 'n','v', "local", "发货状态", 230, 80, "shipStatus","请选择发货状态");
	//快递种类
	var shippingIdDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.shipping_id
	});
	
	shippingIdOptionCombo= createComboBoxLocal(shippingIdDataStore, 'orderInfoPageShippingId', 'n','v', "local", "承运商", 230, 80, "shippingId","请选择承运商");
	
	//查询条件：source
	var sourceDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.source
	});
	
	sourceOptionCombo = createComboBoxLocal(sourceDataStore, 'orderInfoPageSourceId', 'n','v', "local", "source", 230, 80, "source","请选择");
	
	//分仓状态
	var depotStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.depot_status
	});
	
	var depotStatusOptionCombo= createComboBoxLocal(depotStatusDataStore, 'orderInfoPageDepotStatus', 'n','v', "local", "分仓状态", 230, 80, "depotStatus","请选择分仓状态");
	//一级订单来源
	var channelTypeStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.channel_type
	});
	var changeChannelType = function (combo, record,index) {
		channelCombo.clearValue();
		channelCombo.setValue();
			//改变二级菜单
			if(combo.value==null) {
				channelStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType=0';
			}else {
				channelStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType='+combo.value;
			}
			channelStore.load();
	};
	
	//订单来源
	var channelTypeCombo = createComboBoxLocal(channelTypeStore,'orderInfoPageOrderFromFirst', 'n','v', 'local','订单来源', 230, 80, 'orderFromFirst', '请选择渠道类型');
	// 添加change监听事件
	channelTypeCombo.addListener('change',changeChannelType);

	var channelStore= new Ext.data.Store({
		model:"MB.Channel",
		proxy: {
			type: 'ajax',
			url:basePath+'custom/common/getChannelInfos?channelType=0', //这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
			reader:  {type:'json'}
		}
	});
	var changeChannelCode = function (combo, record, index) {
		shopCombo.setValue('');
		orderMemberOptionCombo.setValue(-1);
		if(combo.value==null) {
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops";
		}else {
			if(combo.value=="TB_CHANNEL_CODE" || combo.value=="TMALL_CHANNEL_CODE" || combo.value=="TBFX_CHANNEL_CODE") {
				orderMemberDataStore.loadData(getdata(SELECT.taobao),false);
			} else if(combo.value=="BG_CHANNEL_CODE") {
				orderMemberDataStore.loadData(getdata(SELECT.mall),false);
			} else {
				orderMemberDataStore.removeAll();
				orderMemberDataStore.loadData([]);
			}
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops?channelCode="+ combo.value;
		}
		shopStore.load();

	};
	//渠道
	var channelCombo = createComboBoxLocal(channelStore,"orderInfoPageOrderFromSec",'channelTitle','chanelCode',"remote", "渠道", 230, 80, "orderFromSec","请选择渠道");
	channelCombo.addListener('change',changeChannelCode);

	var shopStore= new Ext.data.Store({
		model:"MB.Shop",
		proxy:{
			type: 'ajax',
			url: basePath+'custom/common/getChannelShops' ,
			reader: {
				type:'json'
			}
		}
	});
	
	//渠道店铺
	var shopCombo = createComboBoxLocalByMult(shopStore,"orderInfoPageOrderFrom",'shopTitle','shopCode',"remote", "渠道店铺",  300, 80, "orderFroms","请选择渠道店铺");
	
	//时间查询
	var selectTimeType = [
		{v: 'addTime', n: '下单时间'},
		{v: 'confirmTime', n: '确认时间'},
		{v: 'payTime', n: '付款时间'},
	//	{v: 'shippingTime', n: ''},
		{v: 'deliveryTime', n: '发货时间'},
		{v: 'clearTime', n: '结算时间'}
	];
	var timeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: selectTimeType
	});
	var timeOptionCombo= createComboBoxLocal(timeDataStore, "orderInfoPageSelectTimeType", 'n','v', "local", "时间类型", 230, 80, "selectTimeType","请选择时间类型");
	timeOptionCombo.setValue("addTime");

	// 国家区域
	var countryStore = new Ext.data.Store({
		model:"MB.Area",
		proxy:{
			type: 'ajax',
			url: getAreaUrl + "?type=0",
			reader: {
				type:'json'
			}
		}
	});
	var countryChannelType = function (combo, record,index) {
		provinceCombo.clearValue();
		districtCombo.clearValue();
		cityStore.removeAll();
		cityStore.loadData([]);
		areaStore.removeAll();
		areaStore.loadData([]);
		if(combo.value!=null && combo.value != '') {
			provinceCombo.getStore().getProxy().url = getAreaUrl + "?type=1&regionId=" + combo.value;
			provinceCombo.getStore().load();
		}
	};
	countryCombo = createComboBoxLocal(countryStore,'orderInfoPageCountry', 'regionName','regionId', 'remote','国家', 230, 80, 'country', '请选择国家');
	// 添加change监听事件
	countryCombo.addListener('change',countryChannelType);
	// 省级区域
	var provinceStore = new Ext.data.Store({
		model:"MB.Area",
		proxy:{
			type: 'ajax',
			url: '',
			reader: {
				type:'json'
			}
		}
	});
	
	var provinceChannelType = function (combo, record, index) {
		cityCombo.clearValue();
		districtCombo.clearValue();
		cityStore.removeAll();
		cityStore.loadData([]);
		areaStore.removeAll();
		areaStore.loadData([]);
		if(combo.value!=null && combo.value != '') {
			cityCombo.getStore().getProxy().url = getAreaUrl + "?type=2&regionId=" + combo.value;
			cityCombo.getStore().load();
		}
	};
	provinceCombo = createComboBoxLocal(provinceStore,'orderInfoPageProvince', 'regionName','regionId', 'remote','省份', 230, 80, 'province', '请选择省');
	// 添加change监听事件
	provinceCombo.addListener('change',provinceChannelType);
	
	// 市级区域
	var cityStore = new Ext.data.Store({
		model:"MB.Area",
		proxy:{
			type: 'ajax',
			url: '',
			reader: {
				type:'json'
			}
		}
	});
	
	var cityChannelType = function (combo, record, index) {
		districtCombo.clearValue();
		areaStore.removeAll();
		areaStore.loadData([]);
		if(combo.value!=null && combo.value != '') {
			districtCombo.getStore().getProxy().url= getAreaUrl + "?type=3&regionId=" + combo.value;
			districtCombo.getStore().load();
		}
	};
	cityCombo = createComboBoxLocal(cityStore,'orderInfoPageCity', 'regionName','regionId', 'remote','城市', 230, 80, 'city', '请选择城市');
	// 添加change监听事件
	cityCombo.addListener('change',cityChannelType);
	// 乡镇区域
	var areaStore = new Ext.data.Store({
		model:"MB.Area",
		proxy:{
			type: 'ajax',
			url: '',
			reader: {
				type:'json'
			}
		}
	});
	
	districtCombo = createComboBoxLocal(areaStore,'orderInfoPageDistrict', 'regionName','regionId', 'remote','地区', 230, 80, 'district', '请选择地区');
	
	// 每页页码列表
	var queryPageStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.query_pagesize_type
	});
	var changePageSize = function (combo, record, index) {
		if (orderInfoGrid != null && orderInfoGrid != 'undefined') {
			var newPageSize = parseInt(record);
			if(null != record && pageSize != newPageSize){
				pageSize = newPageSize;
				search();
			}
		}
	};
	
	//显示每页条数
	var queryPageCombo= createComboBoxLocal(queryPageStore, "orderInfoPageQueryPageArray", 'n','v', "local", "每页显示", 210, 80, "queryPageArray","请选择每页显示条数");
	queryPageCombo.addListener('change',changePageSize);
	queryPageCombo.setValue(pageSize.toString());

	//会员等级
	var orderMemberDataStore=new Ext.data.ArrayStore( {
		autoDestroy: true,
		fields : [ 'text', 'filed' ],
		autoLoad :false,
		data:[]
	});
	orderMemberOptionCombo=createComboBoxLocal(orderMemberDataStore, "orderInfoPageUseLevel", "filed", "text", "local", "渠道会员等级", 230, 80, "useLevel","请选择"); 
	var collapseResize = false;
	
	var orderInfoOperationReasonDataStore= new Ext.data.Store({
		model : "MB.OrderCustomDefine",
		proxy : {
			type : 'ajax',
			actionMethods : {
				read : 'POST'
			},
			url : basePath + 'custom/common/getOrderCustomDefine',
			reader : {
				type : 'json'
			}
		}

	});
	
	orderInfoOperationReasonComb =createComboBoxLocal(orderInfoOperationReasonDataStore, "orderInfoOperationReason", "name", "code", "remote", "操作原因", 400, 80, "code","请选择"); 
	
	orderInfoForm = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
//		bodyPadding: 10,
//		height : '700',
		frame: true,
		collapsible: true,
		title: "订单列表",
		fieldDefaults: {
			labelAlign: 'right'//,
		//	labelWidth: 100
			//labelStyle: 'font-weight:bold'
		},
	/*	collapse: function(direction, animate) {
		        var me = this,
		            collapseDir = direction || me.collapseDirection,
		            ownerCt = me.ownerCt,
		            layout = me.ownerLayout,
		            rendered = me.rendered;
		        if (me.isCollapsingOrExpanding) {
		            return me;
		        }
		        if (arguments.length < 2) {
		            animate = me.animCollapse;
		        }
		        if (me.collapsed || me.fireEvent('beforecollapse', me, direction, animate) === false) {
		            return me;
		        }
		        if (layout && layout.onBeforeComponentCollapse) {
		            if (layout.onBeforeComponentCollapse(me) === false) {
		                return me;
		            }
		        }
		        if (rendered && ownerCt && me.isPlaceHolderCollapse()) {
		            return me.placeholderCollapse(direction, animate);
		        }
		        me.collapsed = collapseDir;
		        if (rendered) {
		            me.beginCollapse();
		        }
		        me.getInherited().collapsed = true;
		        me.fireHierarchyEvent('collapse');
		        if (rendered) {
		            me.doCollapseExpand(1, animate);
		        }
		        
		  
		        
		        return me;
		    },
		    expand: function(animate) {
		        var me = this,
		            layout = me.ownerLayout,
		            rendered = me.rendered;
		        if (me.isCollapsingOrExpanding) {
		            return me;
		        }
		        if (!arguments.length) {
		            animate = me.animCollapse;
		        }
		        if (!me.collapsed && !me.floatedFromCollapse) {
		            return me;
		        }
		        if (me.fireEvent('beforeexpand', me, animate) === false) {
		            return me;
		        }
		        if (layout && layout.onBeforeComponentExpand) {
		            if (layout.onBeforeComponentExpand(me) === false) {
		                return me;
		            }
		        }
		        delete me.getInherited().collapsed;
		        if (rendered && me.isPlaceHolderCollapse()) {
		            return me.placeholderExpand(animate);
		        }
		        me.restoreHiddenDocked();
		        if (rendered) {
		            me.beginExpand();
		        }
		        me.collapsed = false;
		        if (me.rendered) {
		            me.doCollapseExpand(2, animate);
		        }
		        return me;
		    },*/
	
		    
		    
	/*	collapse: function(direction, animate) {
			collapseResize = true;
			var me = this,
			collapseDir = direction || me.collapseDirection,
			ownerCt = me.ownerCt;
			if (me.isCollapsingOrExpanding) {
				return me;
			}
			if (arguments.length < 2) {
				animate = me.animCollapse;
			}
			if (me.collapsed || me.fireEvent('beforecollapse', me, direction, animate) === false) {
				return me;
			}
			if (ownerCt && me.isPlaceHolderCollapse()) {
				return me.placeholderCollapse(direction, animate);
			}
			me.collapsed = collapseDir;
			me.beginCollapse();
			me.getHierarchyState().collapsed = true;
			me.fireHierarchyEvent('collapse');
			me.doCollapseExpand(1, animate);
		},
		expand: function(animate) {
			collapseResize = true;
			var me = this;
			if (me.isCollapsingOrExpanding) {
				return me;
			}
			if (!arguments.length) {
				animate = me.animCollapse;
			}
			if (!me.collapsed && !me.floatedFromCollapse) {
				return me;
			}
			if (me.fireEvent('beforeexpand', me, animate) === false) {
				return me;
			}
			delete this.getHierarchyState().collapsed;
			if (me.isPlaceHolderCollapse()) {
				return me.placeholderExpand(animate);
			}
			me.restoreHiddenDocked();
			me.beginExpand();
			me.collapsed = false;
			return me.doCollapseExpand(2, animate);
		},*/
		items: [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [{ //line 1
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageOrderSn',
					name: 'orderSn',
					fieldLabel : "订单号",
					width : 230
				},{
					xtype : 'textfield',
					labelWidth: 80,
					fieldLabel : '外部交易号',
					id:'orderInfoPageOrderOutSn',
					name: 'orderOutSn',
					width : 230
				},{	
					xtype : 'textfield',
					labelWidth: 80,
					fieldLabel : '订单号集合',
					id:'orderInfoPageOrderSnArr',
					name: 'orderSnArr',
					hidden:true,
					width : 230
					
				},
				orderCategoryOptionCombo,  //订单种类
				orderTypeOptionCombo   //订单类型
		   ]
		}, { //line 2
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [channelTypeCombo,
			        channelCombo,
			        shopCombo,
			        refererOptionCombo
			  /*      ,{
						xtype: 'radiogroup',
						id:'orderInfoPageListDataType',
						width : 240,
						name:'listDataType',
						items: [
							{boxLabel: '近三个月数据', name: 'listDataType',inputValue:'newDate', checked: true},
							{boxLabel: '历史数据', name: 'listDataType',inputValue:'historyDate'}
						],
						listeners: {
							'change' : function (v , a) {
								if(a.listDataType=="newDate") {
									isHistory = 0;
								} else {
									isHistory = 1;
								}
							}
						}
					}*/
			        
				]
		}, { //line 3
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [ isGroupOptionCombo,
			         isAdvanceOptionCombo,
			         orderViewOptionCombo, 
			         transTypeOptionCombo
         
			         ]
		}, {  //line 4
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [ orderStatusOptionCombo,
			         payStatusOptionCombo,
			         shipStatusOptionCombo,
			         depotStatusOptionCombo, 
					{
						xtype:'checkboxfield',
						fieldLabel:'第三方发货',
						name:'deliveryType',
						id:'orderInfoDeliveryType',
						inputValue:'3',//将checkboxfield传递到后台的值定义为3,
						checked:false,
						labelWidth: 80,
						labelAlign: 'right'
					}/*,{
				xtype: 'radiogroup',
				id:'orderInfoPageListDataType',
				width : 240,
				name:'listDataType',
				items: [
					{boxLabel: '近三个月数据', name: 'listDataType',inputValue:'newDate', checked: true},
					{boxLabel: '历史数据', name: 'listDataType',inputValue:'historyDate'}
				],
				listeners: {
					'change' : function (v , a) {
						if(a.listDataType=="newDate") {
							isHistory = 0;
						} else {
							isHistory = 1;
						}
					}
				}*/
			//}
		    ]
		}, {//line 5
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [/*{
					xtype : 'textfield',
					fieldLabel : '操作人',
					id:'exchangeOrderInfoFormLockUserName',
					name: 'lockUserName',
					width : 230,
					labelWidth:80
				} , */
				timeOptionCombo, {
					xtype: 'textfield',
					id:"orderInfoPageStartTime",				
//					cls:'Wdate',
					width : 230,
					name: 'startTime',
					fieldLabel: '开始时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}}
				} , {
					xtype: 'textfield',
					id:"orderInfoPageEndTime",
//					cls:'Wdate',
					width : 230,
					name: 'endTime',
					fieldLabel: '结束时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				} ,
				 {
					xtype: 'radiogroup',
					id:'orderInfoPageListDataType',
					width : 240,
					name:'listDataType',
					items: [
						{boxLabel: '近三个月数据', name: 'listDataType',inputValue:'newDate', checked: true},
						{boxLabel: '历史数据', name: 'listDataType',inputValue:'historyDate'}
					],
					listeners: {
						'change' : function (v , a) {
							if(a.listDataType=="newDate") {
								isHistory = 0;
							} else {
								isHistory = 1;
							}
						}
					}
				},
				
				{
					id : 'showHiddenCondition',
					width : 180,
					xtype : "button",
					text: "展示更多检索条件",
					handler: function(b, c) {
						showHiddenCondition(b, c);
					}
				}
			]
		} , {//line 6
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			id:"moreSelect1",
			hidden:true,
			items: [{
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageUserName',
					name: 'userName',
					fieldLabel : "下单人",
					width : 230
				} , {
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageConsignee',
					name: 'consignee',
					fieldLabel : "收货人",
					width : 230
				} , {
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageTel',
					name: 'tel',
					fieldLabel : "收货人电话",
					width : 230
				} , {
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageMobile',
					name: 'mobile',
					fieldLabel : "收货人手机",
					width : 230
				}
			/**,{
				width : 200,
				lableWidth: 60,
				xtype : "textfield",
				id:'OrderInfoPage.address',
				name: 'address',
				disabled: true,
				fieldLabel : "收货地址"
			}**/]
		} , {
			//line 7
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			id:"moreSelect2",
			hidden:true,
			items: [
			        countryCombo , 
			        provinceCombo ,
			        cityCombo, 
			        districtCombo
			]
		} , { //line 8
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			id:"moreSelect3",
			hidden:true,
			items: [
		/*	        {
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageGoodsSn',
					name: 'goodsSn',
					fieldLabel : "商品款号",
					width : 230
				} ,*/
				{
					width : 230,
					labelWidth: 80,
					xtype : "textfield",
					id:'orderInfoPageSkuSn',
					name: 'skuSn',
					fieldLabel : "6位或11位码"
				} , {
					xtype : 'textfield',
					fieldLabel : '邮费金额',
					id:'orderInfoPageShippingTotalFee',
					name: 'shippingTotalFee',
					width : 230,
					labelWidth:80
				} , {
					xtype : "textfield",
					labelWidth: 80,
					id:'orderInfoPageInvoiceNo',
					name: 'invoiceNo',
					fieldLabel : "快递单号",
					width : 230
				},  payIdOptionCombo  // 支付方式
				
				/*   ,{
					xtype: 'radiogroup',
					id:'orderInfoPageListDataType',
					width : 240,
					name:'listDataType',
					items: [
						{boxLabel: '近三个月数据', name: 'listDataType',inputValue:'newDate', checked: true},
						{boxLabel: '历史数据', name: 'listDataType',inputValue:'historyDate'}
					],
					listeners: {
						'change' : function (v , a) {
							if(a.listDataType=="newDate") {
								isHistory = 0;
							} else {
								isHistory = 1;
							}
						}
					}
				}*/
				
				
			]
		}, { //line 9
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			id:"moreSelect4",
			hidden:true,
			items: [
			    //承运商
				shippingIdOptionCombo,
				sourceOptionCombo//source
			//	  orderMemberOptionCombo,  // 渠道会员等级
			//	  refererOptionCombo, //referer
				
			    
			]
		}
		
	],
	buttons : [queryPageCombo,
	           
	      {
		
			   text: '输入订单号查询',
	           handler: function () {
	        	
	           	FormEditWin.showAddDirWin( "popWins", basePath+"/custom/orderInfo/exportTemplate.spmvc","questionCode","导出模版", 600, 400);
	   	
	           }
	     },
	           
	     {
			text : '查询',
			columnWidth : .1,
			handler : search,
		}, {
			text : '重置',
			handler : function () {
				resetButton(orderInfoForm);
	
		     	Ext.getCmp("orderInfoPageOrderCategory").setValue(-1);
				Ext.getCmp("orderInfoPageTransType").setValue(-1);
				Ext.getCmp("orderInfoPageOrderStatus").setValue(-1);
				Ext.getCmp("orderInfoPageReferer").setValue(-1);
				Ext.getCmp("orderInfoPageIsGroup").setValue(-1);
				Ext.getCmp("orderInfoPageIsAdvance").setValue(-1);
				Ext.getCmp("orderInfoPageOrderView").setValue(-1);
				Ext.getCmp("orderInfoPagePayId").setValue(-1);
				Ext.getCmp("orderInfoPagePayStatus").setValue(-1);
				Ext.getCmp("orderInfoPageShipStatus").setValue(-1);
				Ext.getCmp("orderInfoPageShippingId").setValue(-1);
				Ext.getCmp("orderInfoPageDepotStatus").setValue(-1);
				Ext.getCmp("orderInfoPageOrderType").setValue(-1);
				Ext.getCmp("orderInfoPageUserName").setValue("");
			//	Ext.getCmp("orderInfoPageGoodsSn").setValue("");
				Ext.getCmp("orderInfoPageSkuSn").setValue("");
				
				orderViewOptionCombo.setValue('0');
				orderMemberOptionCombo.setValue(-1);
				channelTypeCombo.setValue(0);
				channelCombo.setValue('');
				shopCombo.setValue('');
				countryCombo.setValue('');
				provinceCombo.setValue('');
				cityCombo.setValue('');
				districtCombo.setValue('');
				Ext.getCmp("orderInfoPageQueryPageArray").setValue(pageSize.toString());
				timeOptionCombo.setValue("addTime");
				
				Ext.getCmp("orderInfoPageMobile").setValue("");
				Ext.getCmp("orderInfoPageTel").setValue("");
				Ext.getCmp("orderInfoPageInvoiceNo").setValue("");
			}
		}, {
			 id: "orderInfoExportBtn",
			text : '导出',
			menu: {
			    items: [
    	            {
    	                text: '默认模版',
    	                handler: function () {
    	                	exportAjax("0");
    	                }
    	            }, {
    	                text: '财务模版',
    	                handler: function () {	               
    	                	exportAjax("1");               	
    	                }	
    	            },  {
    	                text: '物流模版',
    	                handler: function () {           
    	                	exportAjax("2");     	
    	                }	
    	            }
    	           
			    ]
			}
		},{
			id:"orderInfoSellteExportBtn",
			text : '待结算订单导出',
			columnWidth : .1,
			hidden:true,
			handler : exportOrdersettle
		}],
		buttonAlign: 'right',
		listeners: {
			afterRender: function(thisForm, options){
				this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
					enter: function(){  
						search();
					}
				});
			},
			resize: function(){
				if (collapseResize) {
					setResize(orderInfoForm,orderInfoGrid, null, 5);
				}
			}
		}
	});
	var selModel = Ext.create('Ext.selection.CheckboxModel', {
		listeners: {
			selectionchange: function(sm, selections, aa) {
				
				orderInfoGrid.down('#confirmButton').setDisabled((selections.length === 0 || batchConfirmAuth)); //确认按钮;
				
				orderInfoGrid.down('#unConfirmButton').setDisabled((selections.length === 0 || batchConfirmAuth)); //确认按钮;
				
				orderInfoGrid.down('#cancelButton').setDisabled((selections.length === 0 || batchConfirmAuth)); //按钮;
				
				orderInfoGrid.down('#issuedButton').setDisabled((selections.length === 0 || batchConfirmAuth)); //下发分配;
				
	     		orderInfoGrid.down('#updateStateButton').setDisabled((selections.length === 0 || batchConfirmAuth)); //更新发货状态
				
			}
		}
	});

	orderInfoJsonStore = createObjectGridStoreLazy(orderInfoListUrl, pageSize, "MB.OrderInfoQuery");
	columns = [
				{ id : 'orderSn', header : "订单号", align : "center", width : 120, dataIndex:'orderSn',locked:true, renderer: fixOrderSn,sortable:false},
				{ id : 'orderOutSn', header : "交易号", align : "center", width : 220, dataIndex : 'orderOutSn',locked:true, renderer: fixOrderOutSn,sortable:false},
				{ id : 'relatingExchangeSn', header : "换货单原订单号", align : "center", width : 120, dataIndex : 'relatingExchangeSn', renderer: fixRelatingExchangeSn,sortable:false},
				{ id : 'orderType', header : "订单类型", align : "center", width : 120, dataIndex : 'orderTypeStr',sortable:false},
				{ id : 'questionStatus', header : "问题单状态", align : "center", width : 80, dataIndex : 'questionStatus', renderer: fixQuestionStatus,sortable:false},
			//	{ id : 'timeoutStatus', header : "超时单状态", align : "center", width : 80, dataIndex : 'timeoutStatus', renderer: fixTimeoutStatus,sortable:false},
				{ id : 'lockStatus', header : "锁定状态", align : "center", width : 100, dataIndex : 'lockStatus', renderer: fixLockStatus,sortable:false},
				{ id : 'orderStatus',header : "订单状态", align : "center", sortable : true,  width : 180, renderer: editOrderStatus,sortable:false},
				{ id : 'addTime', header : "下单时间", align : "center", width : 160, dataIndex : 'addTime'},
				{ id : 'deliveryTime', header : "发货时间", align : "center", width : 160, dataIndex : 'deliveryTime'},
				{ id : 'orderCategoryStr', header : "订单种类", align : "center", width : 80, dataIndex : 'orderCategoryStr',sortable:false},
				{ id : 'transTypeStr',header : "交易类型", align : "center", sortable : true,  width : 80, dataIndex : 'transTypeStr',sortable:false},
				{ id : 'channelName', header : "订单来源", align : "center", width : 150, dataIndex : 'channelName',sortable:false},
				{ id : 'referer', header : "referer", align : "center", width : 150, dataIndex : 'referer',sortable:false},
				//{ id : 'userName', header : "下单人", align : "center", width : 120, dataIndex : 'userName',sortable:false},
				{ id : 'goodsCount', header : "订单商品数量", align : "center", width : 85, dataIndex : 'goodsCount',sortable:false},
				{ id : 'totalFee', header : "订单总金额", align : "center", width : 120, dataIndex : 'totalFee',sortable:false},
				{ id : 'goodsAmount', header : "商品总金额", align : "center", width : 80, dataIndex : 'goodsAmount',sortable:false},		
				{ id : 'totalPayable', header : "订单应付款金额", align : "center", width : 120, dataIndex : 'totalPayable',sortable:false},
				{ id : 'moneyPaid', header : "订单已付款金额", align : "center", width : 120, dataIndex : 'moneyPaid',sortable:false},
				{ id : 'surplus', header : "余额", align : "center", width : 80, dataIndex : 'surplus',sortable:false},
			//	{ id : 'totalFee', header : "总金额", align : "center", width : 80, dataIndex : 'totalFee',sortable:false},
				{ id : 'settlementPrice', header : "财务价格", align : "center", width : 80, dataIndex : 'settlementPrice',sortable:false},
				{ id : 'bonus', header : "红包金额", align : "center", width : 80, dataIndex : 'bonus',sortable:false},
		//		{ id : 'consignee', header : "收件人", align : "center", width : 250, dataIndex : 'consignee'},
				{ id : 'shippingTotalFee', header : "邮费金额", align : "center", width : 80, dataIndex : 'shippingTotalFee',sortable:false},
				{ id : 'calculateDiscount', header : "折扣率", align : "center", width : 80, dataIndex : 'calculateDiscount',sortable:false},
				{ id : 'useLevelStr', header : "渠道会员等级类型", align : "center", width : 120, dataIndex : 'useLevelStr',sortable:false},
				{ id : 'confirmTime', header : "订单确认时间", align : "center", width : 160, dataIndex : 'confirmTime',sortable:false},
				{ id : 'returnSn', header : "订单关联退单号", align : "left", width : 200, dataIndex : 'returnSn',sortable:false},

				{ id : 'integralMoney', header : "使用积分金额", align : "left", width : 200, dataIndex : 'integralMoney',sortable:false}
				
				
			];
	var dItems = [{
		xtype: 'toolbar',
		dock: 'bottom',
		ui: 'footer',
		layout: {
			pack: 'center'
		},
		items: [
		  {
			minWidth: 80,
			text: '确认',
			itemId: 'confirmButton',
		//	tooltip:'确认订单',
			disabled: true,
			handler: batchConfirm
		  }
		  ,  {
				minWidth: 80,
				text: '未确认',
				itemId: 'unConfirmButton',
			//	tooltip:'确认订单',
				disabled: true,
				handler: batchUnConfirm
			  }
			  ,  
	
		  {
			minWidth: 80,
			text: '取消订单',
			itemId: 'cancelButton',
		//	tooltip:'确认订单',
			disabled: true,
			handler: batchCancel
		},{
			minWidth: 80,
			text: '下发分配',
			itemId: 'issuedButton',
		//	tooltip:'下发分配',
			disabled: true,
			handler: batchIssued
		},{
			minWidth: 80,
			text: 'SWDI',
			itemId: 'updateStateButton',
		//	tooltip:'同步分仓发货信息',
			disabled: true,
			handler: batchswdi
		}
		  
		  ]
	}];
	
	orderInfoGrid = createGridPanelByExt4('orderInfoQuery', null, null, orderInfoJsonStore,
			pageSize, null, columns, selModel, dItems);
	
	var mainPanel = Ext.create(Ext.panel.Panel, {
		renderTo: Ext.getBody(),
		//title: "订单列表",
		width: '100%',
		//bodyPadding: 2,
		//resizable: false,
		//autoHeight: true,
		items: [orderInfoForm, orderInfoGrid]
	});
	
	//查询翻页后带入查询数据
	orderInfoJsonStore.on('beforeload', function (store, options){
		
		console.dir(orderInfoJsonStore.data);
		
		var searchParams = getFormParams(orderInfoForm);
		var userName = Ext.getCmp("orderInfoPageUserName").getValue();
		var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();
		var shippingId = shippingIdOptionCombo.getValue();
		var country = countryCombo.getValue();
		var province = provinceCombo.getValue();
		var city = cityCombo.getValue();
		var district = districtCombo.getValue();
	//	var goodsSn = Ext.getCmp("orderInfoPageGoodsSn").getValue();
		var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
		searchParams.userName = userName;
		searchParams.consignee = consignee;
		searchParams.shippingId = shippingId;
		searchParams.country = country;
		searchParams.province = province;
		searchParams.city = city;
		searchParams.district = district;
	//	searchParams.goodsSn = goodsSn;
		searchParams.skuSn = skuSn;
	
		searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
		searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
		searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
		//勾选三方发货
		var deliveryType = Ext.getCmp("orderInfoDeliveryType").getValue();
		if(deliveryType){
			searchParams.deliveryType = '3';
		}else{
			searchParams.deliveryType = '';
		}
		//source
		var source = sourceOptionCombo.getValue();
		searchParams.source = source;
		
		Ext.apply(store.proxy.extraParams, searchParams);
	});
	
	function editOrderStatus(value, cellmeta, record) {
		var orderStatus = record.get('orderStatus');
		var payStatus = record.get('payStatus');
		var shipStatus = record.get('shipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	}
	function showHiddenCondition(b, c){
		// 显示 隐藏 
		collapseResize = false;
		
	//	alert(Ext.getCmp("moreSelect1").isHidden());
		if(Ext.getCmp("moreSelect1").isHidden()) {

			b.setText('隐藏更多检索条件');
	
	//		Ext.getCmp("moreSelect1").setDisabled(false);
			
	//		Ext.getCmp("moreSelect1").setVisible(true);
	//		Ext.getCmp("moreSelect2").setDisabled(false);
		
	//		Ext.getCmp("moreSelect2").setVisible(true);
	//		Ext.getCmp("moreSelect3").setDisabled(false);
			
	//		Ext.getCmp("moreSelect3").setVisible(true);
//			Ext.getCmp("moreSelect4").setVisible(true);
	//		Ext.getCmp("moreSelect4").setDisabled(false);
	
			
			Ext.getCmp("moreSelect1").setHidden(false);
			Ext.getCmp("moreSelect2").setHidden(false);
			Ext.getCmp("moreSelect3").setHidden(false);
			Ext.getCmp("moreSelect4").setHidden(false);

		}else {
		
			b.setText('展示更多检索条件');
	
	//		Ext.getCmp("moreSelect1").setDisabled(true);
		
	//		Ext.getCmp("moreSelect1").setVisible(false);
	//		Ext.getCmp("moreSelect2").setDisabled(true);
	//		Ext.getCmp("moreSelect2").setVisible(false);
	//		Ext.getCmp("moreSelect3").setDisabled(true);
//			Ext.getCmp("moreSelect3").setVisible(false);
	//		Ext.getCmp("moreSelect4").setDisabled(true);
	//		Ext.getCmp("moreSelect4").setVisible(false);
			
			Ext.getCmp("moreSelect1").setHidden(true);
			Ext.getCmp("moreSelect2").setHidden(true);
			Ext.getCmp("moreSelect3").setHidden(true);
			Ext.getCmp("moreSelect4").setHidden(true);
		
		}
		setResize(orderInfoForm,orderInfoGrid, null, 5);
	}
	
	function batchConfirm() {
		//批量确认被选择的行订单
		var selModel = orderInfoGrid.getSelectionModel();
		if (selModel.hasSelection()) {
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				var orderSn = records[i].get("orderSn");
				var orderStatus = records[i].get("orderStatus");
				if (orderStatus == 0) {
					if(orderSn && orderSn != ''){
						orderSns.push(orderSn);
					}
				}
			}
			if(orderSns && orderSns.length > 0){
				promptMsg("确认被选择的订单！","备注：", function(btn, text) {
					if (btn == "ok") {
						createAjaxData("batchConfirm", batchConfirmUrl, confirmSuccFun,doFailurefun, {"orderSns":orderSns,"message": text}, 100000);
					}
				});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有未确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量确认的订单!");
		}	
	}
	
	/**
	 *取消订单 
	 ***/
	function batchCancel() {
		
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
		//	var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注
			
		//	var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("orderSn");
				
				//orderSns+=orderSn+",";
				orderSns.push(orderSn);
			
			}
			if(orderSns && orderSns.length > 0){
				
				orderSnValue=orderSns; //赋值全局变量
				
				FormEditWin.showAddDirWin( "popWins", basePath+"/custom/orderInfo/batchCancelPage.spmvc","questionCode","批量取消订单", 550, 330);
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量取消的订单!");
		}
		
		
	
		
		// 请求参数赋值
	/*	orderInfoOperationReasonComb.getStore().on('beforeload', function (store){
			params = { "type" : 8};
			Ext.apply(store.proxy.extraParams, params);
		});*/
			
	/*	cannelOrderInfoWin = Ext.create('Ext.window.Window', {
			id: 'orderInfoChannelWindow',
			height: 230,
			width: 400,
			title : '取消订单',
			scrollable: true,
			bodyPadding: 5,
			constrain: true,
			closable: false,
			closeAction : 'close',
			plain: true,
			maskDisabled : false ,
			modal : true ,
			items: [{
				xtype : 'form',
				items: [{
					    id:'orderInfoPageMessage',
						xtype: 'textarea',
						name : 'message',
						fieldLabel : '备注',
						labelWidth : 40,
						width: 250,
						height : 100
					},orderInfoOperationReasonComb
				]
			}],
			buttons : [
				{ text: "保存", handler : batchCancelAjax },
				{ text: "关闭", handler : function () { 
					
				//	 Ext.getCmp("orderInfoChannelWindow").close();
					this.up("window").close(); 
					
				}}
			]
		});
		cannelOrderInfoWin.show();
	*/
	}

	/***
	 *下发erp 
	 ***/
	function batchIssued(){
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
		//	var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注	
		//	var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("orderSn");
				
			//	var orderStatus = records[i].get("orderStatus"); //订单状态 ;
			//	var payStatus = records[i].get("payStatus"); //支付总状态;
			//	var shipStatus = records[i].get("shipStatus"); //发货总状态;
		
				//orderStatus : 1,已确认， payStatus：0,未付款，shipStatus:0,未发货;
			//	if (orderStatus == 0 &&  0 == shipStatus) {
				//	if(orderSn && orderSn != ''){
				orderSns.push(orderSn);
				//	}
			//	}
			}
			if(orderSns && orderSns.length > 0){
			//	promptMsg("确认被选择的订单！","备注：", function(btn, text) {
				//	if (btn == "ok") {
						createAjaxData("batchToErp", batchIssuedUrl, confirmSuccFun,doFailurefun, {"orderSns":orderSns}, 100000);
				//	}
			//	});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
		
	}
	
	
	
	
	function batchUnConfirm(){
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
		//	var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注	
		//	var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("orderSn");
				
			//	var orderStatus = records[i].get("orderStatus"); //订单状态 ;
			//	var payStatus = records[i].get("payStatus"); //支付总状态;
			//	var shipStatus = records[i].get("shipStatus"); //发货总状态;
		
				//orderStatus : 1,已确认， payStatus：0,未付款，shipStatus:0,未发货;
			//	if (orderStatus == 0 &&  0 == shipStatus) {
				//	if(orderSn && orderSn != ''){
				orderSns.push(orderSn);
				//	}
			//	}
			}
			if(orderSns && orderSns.length > 0){
			//	promptMsg("确认被选择的订单！","备注：", function(btn, text) {
				//	if (btn == "ok") {
						createAjaxData("batchUnConfirm", batchUnconfirmUrl, confirmSuccFun,doFailurefun, {"orderSns":orderSns}, 100000);
				//	}
			//	});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
		
	}
	/***
	 *订单分仓发货
	 ***/
	function batchswdi(){
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("orderSn");
				
				orderSns.push(orderSn);
			}
			if(orderSns && orderSns.length > 0){
						createAjaxData("batchswdi", batchswdiUrl, confirmSuccFun,doFailurefun, {"orderSn":orderSns}, 100000);
		
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
		
	}
	
	function batchCancelAjax(){
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
			var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注
			
			var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("orderSn");
		//		var orderStatus = records[i].get("orderStatus"); //订单状态 ;
			//	var payStatus = records[i].get("payStatus"); //支付总状态;
			//	var shipStatus = records[i].get("shipStatus"); //发货总状态;
		
				//orderStatus : 1,已确认， payStatus：0,未付款，shipStatus:0,未发货;
			//	if (orderStatus == 0 &&  0 == shipStatus) {
				//	if(orderSn && orderSn != ''){
				orderSns.push(orderSn);
				//	}
			//	}
			}
			if(orderSns && orderSns.length > 0){
			//	promptMsg("确认被选择的订单！","备注：", function(btn, text) {
				//	if (btn == "ok") {
						createAjaxData("batchCancel", batchCancelUrl, confirmSuccFun, doFailurefun, {"orderSns":orderSns,"message": text,"code":code}, 100000);
				//	}
			//	});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量取消的订单!");
		}
		
	}
	
	function confirmSuccFun (id, response, opts) {
		
		if("batchCancel"==id){
			
			var respText = Ext.JSON.decode(response.responseText);
			if(respText.success == "true"){
				orderInfoJsonStore.load();
			
				alertMsg("结果", "订单已放入队列！")
				cannelOrderInfoWin.close();
			}else{
				orderInfoJsonStore.load();
				errorMsg("结果", respText.msg);
				cannelOrderInfoWin.close();
			}
			
		} else {
			var respText = Ext.JSON.decode(response.responseText);
			if(respText.success == "true"){
				alertMsg("结果","订单已放入队列！")
				orderInfoJsonStore.load();
			}else{
				orderInfoJsonStore.load();
				errorMsg("结果", respText.msg);
			}
		}
		
	}
	
	
	
	function doFailurefun(){
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
	
	function search(){
		var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(orderInfoForm, initParams);

		Ext.getCmp("orderInfoPageOrderSnArr").setValue("");
		var userName = Ext.getCmp("orderInfoPageUserName").getValue();
		var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();

		var shippingId = shippingIdOptionCombo.getValue();
		var country = countryCombo.getValue();
		var province = provinceCombo.getValue();
		var city = cityCombo.getValue();
		var district = districtCombo.getValue();
	//	var goodsSn = Ext.getCmp("orderInfoPageGoodsSn").getValue();
		var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
		searchParams.userName = userName;
		searchParams.consignee = consignee;
		searchParams.shippingId = shippingId;
		searchParams.country = country;
		searchParams.province = province;
		searchParams.city = city;
		searchParams.district = district;
	//	searchParams.goodsSn = goodsSn;
		searchParams.skuSn = skuSn;
		searchParams.orderSnArr="";
		searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
		searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
		searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
		//判断是否勾选第三方发货
		var deliveryType = Ext.getCmp("orderInfoDeliveryType").getValue();
		if(deliveryType){
			searchParams.deliveryType = '3';
		}else{
			searchParams.deliveryType = '';
		}
		//source
		var source = sourceOptionCombo.getValue();
		searchParams.source = source;
		
		orderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		orderInfoGrid.store.pageSize = pageSize;
		orderInfoGrid.store.load({params : searchParams});
	}
	
	function exportRecord() {// 导数据
		var searchParams = getFormParams(orderInfoForm);
		if (orderInfoGrid.store.totalCount > 10000) {
			confirmMsg("确认","根据查询结果导出数据条数大于10000条，要继续导出吗?", function(btn) {
				if (btn == "yes") {
					var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
					myMask.show(); 
					Ext.Ajax.request({
						timeout: 1800000,//1800秒 半小时
						url: basePath+"custom/orderInfo/orderInfoExportCsvFile.spmvc",
						params: searchParams,
						success: function(response){
							if (myMask != undefined){ myMask.hide();}
							var obj = Ext.decode(response.responseText);
							var path = obj.data.path;
							var fileName = obj.data.fileName;
							
							if(obj.isok==true){
								window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
							}else{
								Ext.Msg.alert("错误", "导出失败");
							}
						},
						failure:function(){
							if (myMask != undefined){ myMask.hide();}
							Ext.Msg.alert("错误", "导出失败");
						} 
					});
				} else {
					return ;
				}
			});
		} else {
			var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
			myMask.show(); 
			Ext.Ajax.request({
				timeout: 1800000,//1800秒 半小时
				url: basePath+"custom/orderInfo/orderInfoExportCsvFile.spmvc",
				params:searchParams,
				success: function(response){
					if (myMask != undefined){ myMask.hide();}
					var obj = Ext.decode(response.responseText);
					var path = obj.data.path;
					var fileName = obj.data.fileName;
					if(obj.isok==true){
						window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
					}else{
						alert("导出失败");
					}
				},
				failure:function(){
					if (myMask != undefined){ myMask.hide();}
					alert("导出失败");
				} 
			});
		}
	}
	function fixOrderSn(value) {
		if("1" == display && value != undefined && value != null ){
			var url = order_info_url + value +'&isHistory=' + isHistory;
			return "<a href=" +url + " target='_blank'  >" + value + "</a>";
		} else {
			return '<font>' + value + '</font>';
		}
	}
	
	function fixLockStatus(v, md, r) {
		var str = v;
		if (v == 0) {
			str = '<font style="color:green;">未锁定</font>';
		} else {
			var userName = r.get('lockUserName');
			if (v == 10000 && (userName == undefined || userName == null || userName == '')) {
				str = '<font style="color:red;">其他管理员锁定</font>';
			} else {
				//str = '<font style="color:red;">'+ userName +'</font>';
				str = '<font style="color:red;">已锁定</font>';
			}
		}
		return str;
	}
	
	function fixOrderOutSn(value, md, r) {
		if (value != undefined && value != null ) {
			var orderSn =  r.get('orderSn');
			var url = order_info_url + orderSn +"&isHistory=" + isHistory;
			return "<a href=" +url + " target='_blank' >" + value + "</a>";
		}
	}
	function fixRelatingExchangeSn(value, md, r) {
		if (value != undefined && value != null ) {
			var orderSn =  r.get('relatingExchangeSn');
			var orderType =  r.get('orderType');
			if (orderType && orderType == '2') {
				var url = order_info_url + orderSn + "&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			}
		}
	}
	function fixQuestionStatus(v) {
		var str = "";
		if (v == 0) {
			str = '<font style="color:green;">正常单</font>';
		} else {
			str = '<font style="color:red;">问题单</font>';
		}
		return str;
	} 
	function fixTimeoutStatus(v) {
		var str = "";
		if (v == 0) {
			str = '<font style="color:green;">正常单</font>';
		} else {
			str = '<font style="color:red;">超时单</font>';
		}
		return str;
	}
	
	window.onresize=function(){
		setResize(orderInfoForm,orderInfoGrid, null, 5);
	};
	setResize(orderInfoForm,orderInfoGrid, null, 5);
	
	
	 //货到付款待收款订单
	if(1==settleType) {
		
	//	Ext.getCmp("orderInfoExportBtn").setHidden(false);
	//	Ext.getCmp("orderInfoSellteExportBtn").setHidden(false);

		searchForCashOnDelivery();
	}
	
	//待订单结算	
	if("orderInfoSettle"==settleType) {
		searchOrderSettle();
		Ext.getCmp("orderInfoExportBtn").setHidden(true);
		Ext.getCmp("orderInfoSellteExportBtn").setHidden(false);
	}
	
	
});

function searchInExport(){
	var searchParams = getFormParams(orderInfoForm);
	var userName = Ext.getCmp("orderInfoPageUserName").getValue();
	var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();
	var shippingId = shippingIdOptionCombo.getValue();
	var country = countryCombo.getValue();
	var province = provinceCombo.getValue();
	var city = cityCombo.getValue();
	var district = districtCombo.getValue();
//	var goodsSn = Ext.getCmp("orderInfoPageGoodsSn").getValue();
	var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
	searchParams.userName = userName;
	searchParams.consignee = consignee;
	searchParams.shippingId = shippingId;
	searchParams.country = country;
	searchParams.province = province;
	searchParams.city = city;
	searchParams.district = district;
	//searchParams.goodsSn = goodsSn;
	searchParams.skuSn = skuSn;
	searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
	searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
	searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
	//勾选三方发货
	var deliveryType = Ext.getCmp("orderInfoDeliveryType").getValue();
	if(deliveryType){
		searchParams.deliveryType = '3';
	}else{
		searchParams.deliveryType = '';
	}
	//source
	var source = sourceOptionCombo.getValue();
	searchParams.source = source;
	return searchParams;
}

function exportAjax(exportTemplateType){
	var searchParams = searchInExport();
	//var exportTemplateType = Ext.getCmp('orderInfo.exportTemplateType').value;	
	var url = basePath
			+ '/custom/orderInfo/orderInfoExportCsvFile.spmvc?exportTemplateType='+exportTemplateType;

	Ext.getCmp('orderInfoExportBtn').setDisabled(true);

	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在导出...',
	    target : orderInfoForm
	});
	
	myMask.show();
	Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : url,
			method : 'post',
			timeout : 7200000,
			method : 'post',
			params: searchParams,
			success : function(response) {

				if (myMask != undefined){ 
					myMask.hide();
				}
				var obj = Ext.decode(response.responseText);
				var path = obj.data.path;
				var fileName = obj.data.fileName;
				
				if(obj.isok==true){
					window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
				}else{
					Ext.Msg.alert("错误", "导出失败");
				}
				Ext.getCmp('orderInfoExportBtn').setDisabled(false);
			},
			failure : function(response) {
				myMask.hide();
				Ext.Msg.alert("验证", "失败");
				Ext.getCmp('orderInfoExportBtn').setDisabled(false);
			}
	});
}


//待结算订单导出
function exportOrdersettle(){
	
	var searchParams = searchInExport();
	var url = basePath+ '/custom/orderInfo/orderInfoOrdersettleExportCsvFile.spmvc?exportTemplateType=1';

//	Ext.getCmp('orderInfoExportBtn').setDisabled(true);
	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在导出...',
	    target : orderInfoForm
	});
	myMask.show();
	
	Ext.Ajax.request({
		waitMsg : '请稍等.....',
		url : url,
		method : 'post',
		timeout : 7200000,
		method : 'post',
		params: searchParams,
		success : function(response) {

			if (myMask != undefined){ 
				myMask.hide();
			}
			var obj = Ext.decode(response.responseText);
			var path = obj.data.path;
			var fileName = obj.data.fileName;
			
			if(obj.isok==true){
				window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
			}else{
				Ext.Msg.alert("错误", "导出失败");
			}
			Ext.getCmp('orderInfoExportBtn').setDisabled(false);
		},
		failure : function(response) {
			myMask.hide();
			Ext.Msg.alert("验证", "失败");
			Ext.getCmp('orderInfoExportBtn').setDisabled(false);
		}
	});
	
}

function searchForCashOnDelivery(){

	var initParams = {start : 0, limit : pageSize };
	var searchParams = getFormParams(orderInfoForm);
	
	/**
	 * 
    AND oi.trans_type = 2
	AND oi.order_status = 1
	AND oi.pay_status = 0
	AND oi.ship_status = 3
	*/
	
	var transType =2;
	transTypeOptionCombo.setValue(2);
	
	var orderStatus =1;
	orderStatusOptionCombo.setValue(1);

	var payStatus = 0;
	payStatusOptionCombo.setValue(0);
	
	var shipStatus  = 3;
	shipStatusOptionCombo.setValue(3);

	var orderView = 0;
	orderViewOptionCombo.setValue(0);

	searchParams.transType = transType;
	searchParams.orderStatus = orderStatus;
	
	searchParams.payStatus = payStatus;
	searchParams.shipStatus = shipStatus;
	searchParams.orderView = orderView;
	
	orderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
	orderInfoGrid.store.pageSize = pageSize;
	orderInfoGrid.store.load({params : searchParams});
	
}

function searchOrderSettle(){

	var initParams = {start : 0, limit : pageSize };
	var searchParams = getFormParams(orderInfoForm);
	
	/**
	 * 
	 * 	oi.order_status NOT IN (?, ?, ?, ?, ?)
		AND oi.order_status = 1
		AND oi.pay_status = 2
		AND oi.ship_status = 3
				
		## 2(Byte), 3(Byte), 9(Byte), 10(Byte), 11(Byte), 1(Byte), 2(Byte), 3(Byte)>
	*/
	
	var orderView = 0;
	orderViewOptionCombo.setValue(0);
	
	var orderStatus = 1;
	orderStatusOptionCombo.setValue(1);
	
	var payStatus = 2;
	payStatusOptionCombo.setValue(2);
	
	var shipStatus = 3;
	shipStatusOptionCombo.setValue(3);
	
	searchParams.orderView = orderView;
	searchParams.orderStatus=orderStatus;
	
	searchParams.payStatus=payStatus;
	
	searchParams.shipStatus=shipStatus;
	
	/*var transType =2;
	transTypeOptionCombo.setValue(2);
	
	var orderStatus =1;
	orderStatusOptionCombo.setValue(1);

	var payStatus = 0;
	payStatusOptionCombo.setValue(0);
	
	var shipStatus  = 3;
	shipStatusOptionCombo.setValue(3);
*/
	
	/*searchParams.transType = transType;
	searchParams.orderStatus = orderStatus;
	
	searchParams.payStatus = payStatus;
	searchParams.shipStatus = shipStatus;
*/
	
	orderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
	orderInfoGrid.store.pageSize = pageSize;
	orderInfoGrid.store.load({params : searchParams});
	
}
