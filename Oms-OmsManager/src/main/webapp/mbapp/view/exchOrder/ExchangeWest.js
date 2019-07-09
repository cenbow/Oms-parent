Ext.define('MB.view.exchOrder.ExchangeWest', {
		extend: "Ext.panel.Panel",
//		requires:['MB.view.demo.GoodSetModule'],
		alias: 'widget.exchangeWest',
		heght:'100%',
		width:'10%',
//		autoWidth:true,
		autoHeight:true, 
		autoScroll : true,
//        title : 'Accordion and TreePanel',
        collapsible: true,
        layout: {  
                    type: 'accordion',  
                    animate: true  
                },
        initComponent: function () {
			this.items = [ {
	                title: '订单基本信息',
	                
	                listeners : {
				             'expand' : function(p) {
				             	var orderSetModule = Ext.getCmp('orderSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
	//			            	 orderCenter.scrollTop =orderSetModule.scrollHeight+shipSetModule.scrollHeight+60;
				            	 exchangeCenter.scrollTop=0;
	//			            	 console.dir(this);
				            	 orderSetModule.expand();
				            }
			         }
	            },
	             {
	                title: '订单收货人信息',
	                listeners : {
				             'expand' : function(p) {
				             	var shipSetModule = Ext.getCmp('shipSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderSetModule')+56;
				            	 shipSetModule.expand();
				            }
			         }
	            }, {
	                title: '订单其他信息',
	                listeners : {
				             'expand' : function(p) {
				             	var otherSetModule = Ext.getCmp('otherSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderSetModule')+56+this.up('exchangeWest').scrollParam('shipSetModule');
				            	 otherSetModule.expand();
				            }
			         }
	            }, {
	                title: '订单商品信息',
	                listeners : {
				             'expand' : function(p) {
				             	var goodSetModule = Ext.getCmp('goodSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderSetModule')+56+this.up('exchangeWest').scrollParam('shipSetModule')+this.up('exchangeWest').scrollParam('otherSetModule');
				            	 goodSetModule.expand();
				            }
			         }
	            }, {
	                title: '订单配送信息',
	                listeners : {
				             'expand' : function(p) {
				             	var deliverySetModule = Ext.getCmp('deliverySetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderSetModule')+56+this.up('exchangeWest').scrollParam('shipSetModule')+this.up('exchangeWest').scrollParam('otherSetModule')+this.up('exchangeWest').scrollParam('goodSetModule');
				            	 deliverySetModule.expand();
				            }
			         }
	            }, {
	                title: '订单付款信息',
	                listeners : {
				             'expand' : function(p) {
				             	var paySetModule = Ext.getCmp('paySetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderSetModule')+56+this.up('exchangeWest').scrollParam('shipSetModule')+this.up('exchangeWest').scrollParam('otherSetModule')+this.up('exchangeWest').scrollParam('goodSetModule')+this.up('exchangeWest').scrollParam('deliverySetModule');
				            	 paySetModule.expand();
				            }
			         }
	            }, {
	                title: '退单基本信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var returnSetModule = Ext.getCmp('returnSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm');
				            	 returnSetModule.expand();
				            }
			         }
	            }, {
	                title: '退单商品信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var returnGoodsSetModule = Ext.getCmp('returnGoodsSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm')+46+this.up('exchangeWest').scrollParam('returnSetModule');
				            	 returnGoodsSetModule.expand();
				            }
			         }
	            }, {
	                title: '退单付款信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var returnPayInfomation = Ext.getCmp('returnPayInfomation');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm')+46+this.up('exchangeWest').scrollParam('returnSetModule')+this.up('exchangeWest').scrollParam('returnGoodsSetModule');
				            	 returnPayInfomation.expand();
				            }
			         }
	            }, {
	                title: '退单账目信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var returnPaySetModule = Ext.getCmp('returnPaySetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm')+46+this.up('exchangeWest').scrollParam('returnSetModule')+this.up('exchangeWest').scrollParam('returnGoodsSetModule')+this.up('exchangeWest').scrollParam('returnPayInfomation');
				            	 returnPaySetModule.expand();
				            }
			         }
	            }, {
	                title: '换单基本信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var exchangeSetModule = Ext.getCmp('exchangeSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm')+this.up('exchangeWest').scrollParam('returnForm');
				            	 exchangeSetModule.expand();
				            }
			         }
	            }, {
	                title: '换单商品信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var exchangeGoodsSetModule = Ext.getCmp('exchangeGoodsSetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=this.up('exchangeWest').scrollParam('orderDetailForm')+this.up('exchangeWest').scrollParam('returnForm')+46+this.up('exchangeWest').scrollParam('exchangeSetModule');
				            	 exchangeGoodsSetModule.expand();
				            }
			         }
	            }, {
	                title: '换单付款信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var returnPaySetModule = Ext.getCmp('returnPaySetModule');
				             	 var exchangeCenter=Ext.getCmp('exchangeCenter').body.dom;
				            	 exchangeCenter.scrollTop=Ext.getCmp('exchangeCenter').body.dom.scrollHeight;
				            	 returnPaySetModule.expand();
				            }
			         }
	            }, {
	                title: '操作信息',
	                listeners : {
				             'expand' : function(p) {
				             	 var exchangeSouth = Ext.getCmp('exchangeSouth');
				            	 exchangeSouth.expand();
				            },
				            'collapse':function(){
				            	var exchangeSouth= Ext.getCmp("exchangeSouth");
				             	exchangeSouth.collapse();
				            }
			         }
	            }
			];
			this.callParent(arguments);
	},
	scrollParam:function (id){
		var height=0;
		if(id){
			if(Ext.getCmp(id).collapsed){
				height=Ext.getCmp(id).body.dom.scrollHeight+20;
			}else{
				height=Ext.getCmp(id).body.dom.scrollHeight+46;
			}
		}
		return height;
	}
	
    });