Ext.define('MB.view.goodsReturnChange.GoodsReturnChangeWest', {
		extend: "Ext.panel.Panel",
//		requires:['MB.view.OrderSetModule'],
		alias: 'widget.goodsReturnChangeWest',
		height:'100%',
		width:'10%',
//		autoWidth:true,
		autoHeight:true, 
		autoScroll : true,
		collapsible: true,
		layout: {
			type: 'accordion',
			animate: true
		},
		/* layout:{
			type: 'vbox',	   // 子元素垂直布局
			align: 'stretch',	// 每个子元素宽度充满子容器
			padding: 5
		},*/
		initComponent: function () {
			var me = this;
			this.items = [ {
				title: '退换货申请信息',
				listeners : {
					'expand' : function(p) {
						var goodsReturnChangeSetModule = Ext.getCmp('goodsReturnChangeSetModule'); 
						var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;
						goodsReturnChangeCenter.scrollTop = 0;
						
					}
				}
			} , {
				title: '原订单信息',
				listeners : {
					'expand' : function(p) {
						var goodsReturnChangeSetModule = Ext.getCmp('goodsReturnChangeSetModule').body.dom;//基本信息对象
						var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;//主要展示区域对象（滚动条所在对象）
					 	var scrollTop=goodsReturnChangeSetModule.scrollHeight+50;
					 	goodsReturnChangeCenter.scrollTop = scrollTop;
						
					}
			}
				}, {
					title: '原订单商品信息',
					listeners : {
					 'expand' : function(p) {
					 	var goodsReturnChangeSetModule = Ext.getCmp('goodsReturnChangeSetModule').body.dom;
					 	var orderSetModule = Ext.getCmp('orderSetModule').body.dom;
						var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;//主要展示区域对象（滚动条所在对象）
					 	var scrollTop=goodsReturnChangeSetModule.scrollHeight+50+orderSetModule.scrollHeight+40;
					 	goodsReturnChangeCenter.scrollTop = scrollTop;
					 	
					 }
			 }
				}, {
					title: '原订单付款信息',
					listeners : {
					 'expand' : function(p) {
					 	var goodsReturnChangeSetModule = Ext.getCmp('goodsReturnChangeSetModule').body.dom;
					 	var orderSetModule = Ext.getCmp('orderSetModule').body.dom;
					 	var goodSetModule = Ext.getCmp('goodSetModule').body.dom;
						var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;//主要展示区域对象（滚动条所在对象）
					 	var scrollTop=goodsReturnChangeSetModule.scrollHeight+50+orderSetModule.scrollHeight+40+goodSetModule.scrollHeight+40;
					 	goodsReturnChangeCenter.scrollTop = scrollTop;
					 	
					 }
			 }
				},  {
					title: '退换货商品信息',
					listeners : {
						'expand' : function(p) {
							var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;
							goodsReturnChangeCenter.scrollTop = goodsReturnChangeCenter.scrollHeight;
							
						}
					}
				}, {
					title: '图片信息',
					listeners : {
						'expand' : function(p) {
							Ext.getCmp('goodsReturnChangeCenter').down('goodsReturnChangePicSetModule').expand();
						},
						'collapse':function(){
							Ext.getCmp('goodsReturnChangeCenter').down('goodsReturnChangePicSetModule').collapse();
						}
					}
				},{
					title: '操作信息',
					listeners : {
						'expand' : function(p) {
							Ext.getCmp('goodsReturnChangeSouth').expand();
						},
						'collapse':function(){
							Ext.getCmp('goodsReturnChangeSouth').collapse();
						}
					}
				}
			];
			this.callParent(arguments);
	}
	
	});