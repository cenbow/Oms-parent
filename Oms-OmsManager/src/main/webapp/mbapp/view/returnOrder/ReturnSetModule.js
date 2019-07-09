Ext.define("MB.view.returnOrder.ReturnSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.returnSetModule',
	id:"returnSetModule",
	width: '100%',
	frame: true,
	title:'基本信息&nbsp;&nbsp;&nbsp;',
	head:true,
	margin:10,
    bodyPadding:10,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		var me=this;
		this.items = [ 
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'returnSn', fieldLabel : "退单号", columnWidth: .33,renderer: function(value) {
					if (value != undefined && value != null ) {
						var url = order_return_url + value +"&isHistory=0";
						return "<a href=" +url + " target='_blank' >" + value + "</a>";
					}
				}},
				//1退货单、2拒收入库单、3普通退款单 4额外退款单
			        {xtype : "displayfield", name : 'returnType', fieldLabel : "退单类型", columnWidth: .33,
			        	renderer : function (value) { 
							if(value == 1){
								return "退货退款单";
							}else if(value == 2){
								return "拒收入库单";
							}else if(value == 3){
								return "无货退款单";
							}else if(value == 4){
								return "额外退款单";
							}
						}
			        },
			        {xtype : "displayfield", name : 'returnStatusDisplay', fieldLabel : "退单状态",style:'color:Red', columnWidth: .33,
			        	renderer : function (value) { 
			        		return "<span style='color:red;'>"+value+"</span>";
			        	}
			        }
				
				]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
			        {xtype : "displayfield", name : 'relatingOrderSn', fieldLabel : "关联订单号", columnWidth: .33,renderer: function(value) {
						if (value != undefined && value != null ) {
							var url = order_info_url +"?masterOrderSn="+masterOrderSn+"&orderSn=1"+ value +"&isHistory=0";
							return "<a href=" +url + " target='_blank' >" + value + "</a>";
						}
					}},
					{xtype : "displayfield", name : 'channelName', fieldLabel : "渠道来源", columnWidth: .33},
					{xtype : "commonhaverefund", name : 'haveRefund', fieldLabel : "是否退款", columnWidth: .24,labelWidth: 100},
					{xtype : "displayfield", columnWidth: .04} ,
			        {xtype : "button" , text :"更新",columnWidth: .05,id:'updateHaveRefund', action: 'updateHaveRefund',labelAlign: 'right'}
			        ]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
					{xtype : "commonsystemShipping", name : 'returnExpress',fieldLabel : "退货承运商", columnWidth: .24,labelWidth: 100},
					{xtype : "displayfield", columnWidth: .04} ,
			        {xtype : "button" , text :"更新",columnWidth: .05,id:'updateExpress', action: 'updateExpress',labelAlign: 'right'},
			        {xtype : "textfield", name : 'returnInvoiceNo', fieldLabel : "退货面单号", columnWidth: .24 },
			        {xtype : "displayfield", columnWidth: .04} ,
			        {xtype : "button" , text :"更新",columnWidth: .05,id:'updateInvoiceNo', action: 'updateInvoiceNo',labelAlign: 'right'}
			        
			       ]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
			        {xtype : "textfield", name : 'newOrderSn', fieldLabel : "换货单号", columnWidth: .33},
					{xtype : "commonsettlementType", name : 'returnSettlementType', fieldLabel : "退款类型", columnWidth: .33,labelWidth: 100},
			        //0无操作，1退货，2修补，3销毁，4换货
			        {xtype : "commonprocessType", name : 'processType', fieldLabel : "处理方式", columnWidth: .33,labelWidth: 100 }
			        ]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
			        {xtype : "displayfield", name : 'addTime', fieldLabel : "退单时间", columnWidth: .33},
			        {xtype : "displayfield", name : 'checkInTime', fieldLabel : "入库时间", columnWidth: .33 },
			        {xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .33 }
			        ]
		} ,		
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
			        {xtype : "commonreturnerpwarehouse",name : 'depotCode', fieldLabel : "退货仓库",  columnWidth: .24,labelWidth: 100 },
			        {xtype : "displayfield", columnWidth: .04} ,
			        {xtype : "button" , text :"更新",columnWidth: .05,id:'updateDepotCode', action: 'updateDepotCode',labelAlign: 'right'},
			        {xtype : "orderCustomDefineCombo", name : 'returnReason', fieldLabel : "退单原因", columnWidth: .33,labelWidth: 100 }
			        ]
		} ,		
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
					{xtype : "textfield", name : 'returnDesc', fieldLabel : "退单备注", columnWidth: .66 }
					
			        ]
		}
		];
		this.tools=[
			 {
					type: 'gear',
					id:'returnSetModuleG',
		            tooltip : '修改',
		            action: 'orderReturnEdit',
		            hidden:true,
		            //handler: me.onCloseClick,
		            scope: me
				}];
		var me = this;
        me.callParent(arguments);
	},
    onCloseClick: function () {
        if (this.ownerCt) {
            this.ownerCt.remove(this, true);
        }
    },
    builtParam: function(type,form){
    	var params = form.getValues();
		var returnForm=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnForm=Ext.getCmp('returnForm');
		}else{
			returnForm=Ext.getCmp('returnCenter');
		}
		var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
		
		if(type == 'updateHaveRefund'){
			params['createOrderReturn.haveRefund']=params.haveRefund;
			params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
			params['createOrderReturn.returnSn']=returnCommon.returnSn;
		}else{
			params['createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
			params['createOrderReturnShip.returnExpress']=params.returnExpress;
			params['createOrderReturnShip.returnInvoiceNo']=params.returnInvoiceNo;
			params['createOrderReturnShip.depotCode']=params.depotCode;
		}
		params.orderReturnSn=returnCommon.returnSn;//退单Sn
		return params;
    },initData: function(returnCommon){
    	this.getForm().setValues(returnCommon);
    }
});