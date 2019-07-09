 Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'ext4/ux');
// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');

var sm;
var columns = null;
var tbar;
var pageSize=20;
Ext.require([
	'Ext.grid.*', 
	'Ext.data.*',
	'Ext.util.*',
	'Ext.ux.PreviewPlugin',
	'Ext.ModelManager',
	'Ext.tip.QuickTipManager',
	'Ext.ux.*',
	'Ext.selection.CheckboxModel',
	'Ext.panel.*',
	'Ext.toolbar.*',
	'Ext.button.*',
	'Ext.container.ButtonGroup',
	'Ext.layout.container.Table'
]);
Ext.onReady(function() {
		var exchangeOrderInfoListUrl = basePath+"/custom/exchangeorder/exchangeOrderInfoList.spmvc";
		var exchangeGridpanel = null;
		var exchangeJsonStore = null;
		// 将创建组建过程封装   data 需要自己创建
		Ext.define('orderParam', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'v', type: 'string'},
				{name: 'n',  type: 'string'}
			]
		});
		//订单状态
		var orderStatusDataStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: SELECT.order_status
		});
		var orderStatusOptionCombo= createComboBoxLocal(orderStatusDataStore, "exchangeOrderInfoForm.orderStatus", 'n','v', "local", "订单状态", 200, 60, "orderStatus","请选择订单状态");

		//支付状态
		var payStatusDataStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: SELECT.pay_status
		});
		var payStatusOptionCombo= createComboBoxLocal(payStatusDataStore, "exchangeOrderInfoForm.payStatus", 'n','v', "local", "支付状态", 220, 80, "payStatus","请选择支付状态");

		//发货状态
		var shipStatusDataStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: SELECT.ship_status
		});
		var shipStatusOptionCombo= createComboBoxLocal(shipStatusDataStore, "exchangeOrderInfoForm.shipStatus", 'n','v', "local", "发货状态", 220, 80, "shipStatus","请选择发货状态");
		
		//referer
		var refererDataStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: SELECT.referer_type
		});

		var refererOptionCombo = createComboBoxLocal(refererDataStore, "exchangeOrderInfoForm.referer", 'n','v', "local", "referer", 200, 60, "referer","请选择订单来源媒体");
		//订单来源
		//关联订单来源1 
		var channelTypeStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: SELECT.channel_type
		});
		var changeChannelType = function (combo, record,index) {
			orderRelatedSourceOptionCombo2.clearValue();
			orderRelatedSourceOptionCombo2.setValue('');
				//改变二级菜单
				if(combo.value==null) {
					orderRelatedSourceDataStore2.getProxy().url=basePath+'custom/orderQuestion/getchanels.spmvc?channelType=0';
				}else {
					orderRelatedSourceDataStore2.getProxy().url=basePath+'custom/orderQuestion/getchanels.spmvc?channelType='+combo.value;
				}
				orderRelatedSourceDataStore2.load();
		};
		var orderRelatedSourceOptionCombo = createComboBoxLocal(channelTypeStore,'exchangeOrderInfoForm.orderFromFirst', 'n','v', 'local','订单来源', 250, 60, 'orderFromFirst', '请选择渠道类型');
		// 添加change监听事件
		orderRelatedSourceOptionCombo.addListener('change',changeChannelType);

		//关联订单来源2  ajax 加载
		Ext.define('OrderForm', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'chanelCode',type: 'string'},
				{name: 'channelTitle',type: 'string'}
			]
		});
		//关联订单来源2  ajax 加载
		var orderRelatedSourceDataStore2= new Ext.data.Store({
			model:"OrderForm",
			proxy: {
				type: 'ajax',
				url:basePath+'custom/orderQuestion/getchanels.spmvc?channelType=0', //这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
				reader:  {type:'json'}
			}
		});
		
		var changeChannelCode = function (combo, record, index) {
			orderRelatedSourceOptionCombo3.clearValue();
				orderRelatedSourceOptionCombo3.setValue('');
				if(combo.value==null) {
					orderRelatedSourceDataStore3.getProxy().url=basePath+"custom/orderQuestion/getchanelShops.spmvc";
				}else {
					orderRelatedSourceDataStore3.getProxy().url=basePath+"custom/orderQuestion/getchanelShops.spmvc?channelCode="+ combo.value;
					}
				orderRelatedSourceDataStore3.load();
		};
		
		var orderRelatedSourceOptionCombo2 = createComboBoxLocal(orderRelatedSourceDataStore2,"exchangeOrderInfoForm.orderFromSec",'channelTitle','chanelCode',"remote", "", 170, 1, "orderFromSec","请选择渠道");
		orderRelatedSourceOptionCombo2.addListener('change',changeChannelCode);
		
		//关联订单来源3  ajax 加载
		Ext.define('OrderShopForm', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'shopCode', type: 'string'},
				{name: 'shopTitle',  type: 'string'}
			]
		});
		
		var orderRelatedSourceDataStore3= new Ext.data.Store({
			model:"OrderShopForm",
			proxy:{
				type: 'ajax',
				url:basePath+'custom/orderQuestion/getchanelShops.spmvc' ,
				reader: {
					type:'json'
				}
			}
		});
		var orderRelatedSourceOptionCombo3=createComboBoxLocal(orderRelatedSourceDataStore3,"exchangeOrderInfoForm.orderFrom",'shopTitle','shopCode',"remote", "", 170,1, "orderFrom", "请选择店铺渠道");
		
		orderRelatedSourceDataStore2.on('load', function (store, records, successful, options) {
			if (successful && Ext.typeOf(orderRelatedSourceOptionCombo2.getPicker().loadMask) !== "boolean") {
				orderRelatedSourceOptionCombo2.getPicker().loadMask.hide();  
			}
		});
		orderRelatedSourceDataStore3.on('load', function (store, records, successful, options) {  
			if (successful && Ext.typeOf(orderRelatedSourceOptionCombo3.getPicker().loadMask) !== "boolean") {
				orderRelatedSourceOptionCombo3.getPicker().loadMask.hide();  
			}
		});
		
		var exchangeOrderInfoFormPanel =new Ext.form.Panel({
					id : 'exchangeOrderInfoForm',
					frame : true,
					width:  '100%',
					url : '',
					labelAlign : 'center',
					items : [{//第一行
						frame : true,
						xtype:'container',
						layout : "column", // 从左往右的布局
						items : [{
								layout : "form", // 从上往下的布局
								xtype : "textfield",
								id:'exchangeOrderInfoForm.orderSn',
								name: 'orderSn',
								fieldLabel : "换货订单号",
								labelWidth:70,
								width : 200,
							} , {
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '关联订单号',
								id:'exchangeOrderInfoForm.relatingExchangeSn',
								name: 'relatingExchangeSn',
								labelWidth:70,
								width : 200,
							} , {
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '退货关联退单号',
								id:'exchangeOrderInfoForm.relatingReturnSn',
								name: 'relatingReturnSn',
								labelWidth:90,
								width : 200,
							} , {
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '退款关联退单号',
								id:'exchangeOrderInfoForm.relatingRemoneySn',
								name: 'relatingRemoneySn',
								labelWidth:90,
								width : 200,
							} , {
								width : 260,
								xtype: 'radiogroup',
								name:'listDataType',
								items: [
									{boxLabel: '近三个月数据', name: 'listDataType',inputValue:'newDate', checked: true},
									{boxLabel: '历史数据', name: 'listDataType',inputValue:'historyDate'},
								]
							}
						]
					} , {//第二行
						frame : true,
						xtype:'container',
						layout : "column",
						items : [
							orderStatusOptionCombo,
							payStatusOptionCombo,
							shipStatusOptionCombo
						] 
					} , {
						frame : true,
						xtype:'container',
						layout : "column", 
						items : [
							//关联订单来源
							orderRelatedSourceOptionCombo,
							orderRelatedSourceOptionCombo2,
							orderRelatedSourceOptionCombo3,
							refererOptionCombo
						] 
				} , {//第三行
							frame : true,
							xtype:'container',
							layout : "column",
							items : [{
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '操作人',
								id:'exchangeOrderInfoForm.lockUserName',
								name: 'lockUserName',
								width : 180,
								labelWidth:40
							} , {
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '下单人',
								labelWidth:40,
								id:'exchangeOrderInfoForm.userName',
								name: 'userName',
								width : 180
							} , {
								layout : "form",
								xtype : 'textfield',
								fieldLabel : '收货人',
								labelWidth:40,
								id:'exchangeOrderInfoForm.consignee',
								name: 'consignee',
								width : 180
							} , {
								xtype : 'textfield',
								width : 250,
								anchor : '90%',
								html : '下单开始时间 : <input type="text" id="exchangeOrderInfoForm.startTime" size="20" style="height:22;" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
							} , {
								xtype : 'textfield',
								width : 250,
								anchor : '90%',
								html : '结束时间: <input type="text" id="exchangeOrderInfoForm.endTime" size="20" style="height:22;" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
							}] 
						}],
					buttons : [{
						text : '查询',
						columnWidth : .1,
						handler : search,
					}, {
						text : '重置',
						handler : function () {
							Ext.getDom("exchangeOrderInfoForm.startTime").value="";
							Ext.getDom("exchangeOrderInfoForm.endTime").value="";
							orderStatusOptionCombo.setValue(-1);
							payStatusOptionCombo.setValue(-1);
							shipStatusOptionCombo.setValue(-1);
							refererOptionCombo.setValue('');
							//三级联动
							
							resetButton(exchangeOrderInfoFormPanel);
							Ext.getDom("exchangeOrderInfoForm.startTime").value="";
							Ext.getDom("exchangeOrderInfoForm.endTime").value="";
							Ext.getCmp("exchangeOrderInfoForm.orderStatus").setValue(-1);
							Ext.getCmp("exchangeOrderInfoForm.referer").setValue(-1);
							Ext.getCmp("exchangeOrderInfoForm.payStatus").setValue(-1);
							Ext.getCmp("exchangeOrderInfoForm.shipStatus").setValue(-1);
							orderRelatedSourceOptionCombo.setValue(0);
							orderRelatedSourceOptionCombo2.setValue('');
							orderRelatedSourceOptionCombo3.setValue('');
						}
					}, {
						text : '导出',
						handler : exportRecord,
					}],
					buttonAlign: 'right',
					listeners: {  
						afterRender: function(thisForm, options){  
							this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
								enter: function(){
								}
							});
						}
					}
				});
				
				Ext.define('exchangeOrderInfoForm', {
					extend: 'Ext.data.Model',
					fields: [
						{ name: 'orderSn' },
						{ name: 'relatingExchangeSn' },
						{ name: 'relatingReturnSn'},
						{ name: 'relatingRemoneySn'},
						{ name: 'orderStatus'},
						{ name: 'listDataType'},
						{ name: 'payStatus'},
						{ name : 'shipStatus' },
						{ name: 'startTime'},
						{ name: 'endTime'},
						{ name: 'orderFormFirst'},
						{ name: 'orderFormSec'},
						{ name: 'orderFrom' },
						//上述查询的记录
						//下述为结果记录
						{ name: 'addTime'},
						{ name: 'processStatusStr'},
						{ name: 'referer'},
						{ name: 'goodsCount'},
						{ name: 'totalFee'},
						{ name: 'moneyPaid'},
						{ name: 'lockStatus'},
						{ name: 'operator'},
						{ name: 'operate'},
						{ name : 'lockUserName'},
						{ name : 'orderOutSn' },
						{ name: 'orderCategoryStr'},
						{ name: 'shippingTime'},
						{ name : 'transTypeStr' },
						{ name: 'userName' },
						{ name: 'totalPayable'},
						{ name: 'moneyPaid'},
						{ name: 'channelName'},
						{ name: 'selectTimeType' },
						{ name : 'orderType'},
						{ name : 'questionStatus'},
						{ name : 'timeoutStatus'},
						{ name : 'surplus'}
						
					]
				});
				exchangeJsonStore = createObjectGridStore(exchangeOrderInfoListUrl, pageSize, "exchangeOrderInfoForm");
				columns = [
					{ id : 'orderSn', header : "换货订单号", align : "center", width : 120, dataIndex : 'orderSn',renderer: function(value, cellmeta, record) {
						if("1" == $("#display").val()){
							return "<a href='"+basePath+'/custom/exchangeorder/index.spmvc?order_sn='+value+"&step=modify"+"' target='_blank'>"+value+"</a>";		
						} else {
							return '<font>'+value+'</font>';
						}
					} },
					{ id : 'relatingExchangeSn', header : "关联订单号", align : "center", width : 120, dataIndex : 'relatingExchangeSn' ,renderer: function(value, cellmeta, record) {
						if("1" == $("#display").val()){
							return "<a href='"+basePath+'/custom/orderInfo/orderInfoDetail.spmvc?orderSn='+value+"&isHistory=0"+"' target='_blank' >"+value+"</a>";
						} else {
							return '<font>'+value+'</font>';
						}
					}},
					{ id : 'relatingReturnSn', header : "退货关联退单号", align : "center", width : 120, dataIndex : 'relatingReturnSn' ,renderer: function(value, cellmeta, record) {
						var url = "";
						if (null != value && value != '') {
							if("1" == $("#orderReturnDisplay").val()){
								url = "<a href='"+basePath+'/custom/orderReturn/returnOrderInfo?returnSn='+value+"' target='_blank' >"+value+"</a>";
							} else {
								url = '<font>'+value+'</font>';
							}
						}
						return url;
					}},
					{ id : 'relatingRemoneySn', header : "退款关联退单号", align : "center", width : 120, dataIndex : 'relatingRemoneySn', renderer: function(value, cellmeta, record) {
						var url = "";
						if (null != value && value != '') {
							if("1" == $("#orderReturnDisplay").val()){
								url = "<a href='"+basePath+'/custom/orderReturn/returnOrderInfo?returnSn='+value+"' target='_blank' >"+value+"</a>";
							} else {
								url = '<font>'+value+'</font>';
							}
						}
						return url;
					}},
					{ id : 'processStatusStr', header : "换单状态", align : "center", width : 160, dataIndex : 'processStatusStr',renderer: editExchangeOrderStatus},
					{ id : 'add_time', header : "下单时间", align : "center", width : 160, dataIndex : 'addTime'},
					{ id : 'lockStatus', header : "锁定状态", align : "center", width : 85, dataIndex : 'lockStatus', renderer: function(v, md, r) {
						var str = v;
						if (v == 0) {
							str = '<font style="color:green;">未锁定</font>';
						} else {
							var userName = r.get('lockUserName');
							str = '<font style="color:red;">'+ userName +'</font>';
						}
						return str;
					} },
					{ id : 'channelName', header : "订单来源", align : "center", width : 150, dataIndex : 'channelName' },
					{ id : 'referer',header : "referer", align : "center", sortable : true,  width : 120, dataIndex : 'referer' },
					{ id : 'userName', header : "下单人", align : "center", width : 120, dataIndex : 'userName'},
					{ id : 'goodsCount', header : "商品总数", align : "center", width : 120, dataIndex : 'goodsCount'},
					{ id : 'totalFee', header : "总金额", align : "center", width : 120, dataIndex : 'totalFee'},
					{ id : 'moneyPaid', header : "已付金额", align : "center", width : 120, dataIndex : 'moneyPaid'},
					{ id : 'totalPayable', header : "应付金额", align : "center", width : 120, dataIndex : 'totalPayable'},
					{ id : 'surplus', header : "余额", align : "center", width : 80, dataIndex : 'surplus'}
					];

				sm = Ext.create('Ext.selection.CheckboxModel');
				exchangeGridpanel = createISSGridPanel('exchangeOrderInfo_gridss_id','订单查询', 500, exchangeJsonStore, pageSize, '', columns, null, null, '');
				var exchangePanel = new Ext.panel.Panel( {
					layout : 'column',
					items : [ exchangeOrderInfoFormPanel, exchangeGridpanel]
				});
				exchangePanel.render(Ext.getBody());
				
				//查询翻页后带回查询数据
				exchangeJsonStore.on('beforeload', function (store, options){
					var searchParams = {};
					searchParams["startTime"] = Ext.get("exchangeOrderInfoForm.startTime").dom.value;
					searchParams["endTime"] = Ext.get("exchangeOrderInfoForm.startTime").dom.value;

					var formValues=exchangeOrderInfoFormPanel.getForm().getValues();//获取表单中的所有Name键/值对对象
					var extend=function(o,n,override){
						for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
					};
					extend(searchParams,formValues);
					Ext.apply(store.proxy.extraParams, searchParams); 
				});
	/**
	 * 查询
	 */
	function search(){
		var searchParams = {start : 0, limit : pageSize };
		searchParams["startTime"] = Ext.get("exchangeOrderInfoForm.startTime").dom.value;
		searchParams["endTime"] = Ext.get("exchangeOrderInfoForm.endTime").dom.value;
//		if(Ext.get("area").dom.value!=""){
//			searchParams["district"] = Ext.get("area").dom.value;
//			}
//			if(Ext.get("city").dom.value!=""){
//			searchParams["city"] = Ext.get("city").dom.value;
//			}
//			if(Ext.get("province").dom.value!=""){
//			searchParams["province"] = Ext.get("province").dom.value;
//			}
//			if(Ext.get("country").dom.value!=""){
//			searchParams["country"] = Ext.get("country").dom.value;
//			}
		var formValues=exchangeOrderInfoFormPanel.getForm().getValues();//获取表单中的所有Name键/值对对象
		var extend=function(o,n,override){
			for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
		};
		extend(searchParams,formValues);
		exchangeGridpanel.store.load({params : searchParams});
	}
	function exportRecord() {// 导数据
		var params = {"startTime" : Ext.get("exchangeOrderInfoForm.startTime").dom.value, "endTime" : Ext.get("exchangeOrderInfoForm.endTime").dom.value};
		var formValues=exchangeOrderInfoFormPanel.getForm().getValues();//获取表单中的所有Name键/值对对象
		var extend=function(o,n,override){
			for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
		};
		extend(params,formValues);
		var str = jQuery.param(params);
		var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
		myMask.show();
		Ext.Ajax.request({
			timeout: 100000,//100秒
			url:basePath+"/custom/exchangeorder/exchangeorderInfoExportCsvFile.spmvc",
			params:str,
			success:function(response){
				if (myMask != undefined){ myMask.hide();}
				 var obj = Ext.decode(response.responseText);
				 if(obj.isok==true){
						window.location.href=basePath+"custom/downloadFile.spmvc?path="+obj.data;
					}else{
						alert("导出失败");
					};
			},
			failure:function(){
				if (myMask != undefined){ myMask.hide();}
				alert("导出失败");
			} 
		});
	}
	//处理城市 的三级联动
	Ext.Ajax.request( {
		url : basePath + 'custom/orderInfo/getArea.spmvc',
		method : 'get',
		success : function(response) {
			var respText = Ext.JSON.decode(response.responseText);
			var h="<option style='width:160px;' value='0'>请选择</option>";
			var p="<option style='width:160px;' value='0'>请选择</option>";
			var c="<option style='width:160px;' value='0'>请选择</option>";
			var a="<option style='width:160px;' value='0'>请选择</option>";
			if(respText.isok){
				$.each(respText.data.SystemRegionCountryList, function(i,val){  
					h+="<option style='width:160px;' value='"+val.regionId+"'>"+val.regionName+"</option>";
				});
				$.each(respText.data.SystemRegionProvinceList, function(i,val){  
					p+="<option style='width:160px;' bb='"+val.parentId+"' value='"+val.regionId+"'>"+val.regionName+"</option>";
				}); 
				$.each(respText.data.SystemRegionCityList, function(i,val){  
					c+="<option style='width:160px;' cc='"+val.parentId+"' value='"+val.regionId+"'>"+val.regionName+"</option>";
				}); 
				$.each(respText.data.SystemRegionAreaList, function(i,val){  
					a+="<option style='width:160px;' dd='"+val.parentId+"' value='"+val.regionId+"'>"+val.regionName+"</option>";
				}); 
				$("#country").html(h);
				$("#province").html(p);
				$("#city").html(c);
				$("#area").html(a);
				getarea();  
			}else{
				alert(respText.message);
			}
		},
		failure : function(response) {
			alert("失败");
		}
	});
		
	function editExchangeOrderStatus(value, cellmeta, record) {
		var orderStatus = record.get('orderStatus');
		var payStatus = record.get('payStatus');
		var shipStatus = record.get('shipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	}

	window.onresize=function(){
		setResize(exchangeOrderInfoFormPanel,exchangeGridpanel, null, 4);
	};
	setResize(exchangeOrderInfoFormPanel,exchangeGridpanel, null, 4);
});

var q;
var w;
var e;

function getarea(){
	q=$("#province").find("option");
	w=$("#city").find("option");
	e=$("#area").find("option");
}

function a(){
	$("#province").html(q);
	$("#city").html(w);
	$("#area").html(e);
	alert($("#province").html());
	var a=$("#country").find("option:selected").val();
	$("#province option[bb!="+a+"]").remove();
	$("#province").prepend("<option value='0'>请选择</option>");
}
function b(){
	$("#province").html(q);
	$("#city").html(w);
	$("#area").html(e);
	var a=$("#province").find("option:selected").val();
	$("#city option[cc!="+a+"]").remove();
	$("#city").prepend("<option value='0'>请选择</option>");
}
function c(){
	$("#province").html(q);
	$("#city").html(w);
	$("#area").html(e);
	var a=$("#city").find("option:selected").val();
	$("#area option[dd!="+a+"]").remove();
	$("#area").prepend("<option value='0'>请选择</option>");
}