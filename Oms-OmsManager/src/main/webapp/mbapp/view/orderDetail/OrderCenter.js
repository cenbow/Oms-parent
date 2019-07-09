Ext.define("MB.view.orderDetail.OrderCenter", {
	extend: "Ext.form.Panel",
	alias: 'widget.orderCenter',
	id:'orderCenter',
	//title: '订单详情',
	width: '100%',
	frame: true,
	bodyStyle: {
		padding: '10px'
	},
	autoWidth:true,
	autoHeight:true,
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	//store: 'Demos',
	initComponent: function () {
		var me = this;
		this.items = [
		{
			xtype:'orderSetModule',
			titleCollapse:true
		} , {
			xtype:'shipSetModule',
			titleCollapse:true
		}
		/*, {
			xtype:'otherSetModule',
			titleCollapse:true
			
		}*/
		,{
			xtype : 'form',
			id: 'otherSetModule',
			itemId: 'otherSetModule',
			width: '100%',
			title:'其他信息',
			margin:5,
			bodyPadding:5,
			title:'其他信息',
			head:true,
			fieldDefaults: {
				labelAlign: 'right'
			},
			collapsible:true,
			items : [ {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'invType', fieldLabel : "发票类型", columnWidth: .33},
					{xtype : "displayfield", name : 'invPayee', fieldLabel : "发票抬头", columnWidth: .33 } ,
					{xtype : "displayfield", name : 'invContent', fieldLabel : "发票内容", columnWidth: .33 }
				]
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'postscript', fieldLabel : "客户留言", columnWidth: 1 }
				]
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				defaultType: 'displayfield',
				items:  [ {
					xtype : "displayfield",
					name : 'howOos',
					fieldLabel : "缺货处理",
					value: '',
					columnWidth: 1
				} ]
			} ],
			tools : [ {
				type: 'gear',
				tooltip : '修改其他信息',
				action: 'otherEdit',
				scope: me
			} ]
		}
		, {
			xtype:'goodSetModule',
//			collapsed: true,//初始不展开
			titleCollapse:true
		}, {
			xtype:'deliverySetModule',
//			collapsed: true,//初始不展开
			titleCollapse:true
		}, {
			xtype:'paySetModule',
			titleCollapse:true
//			collapsed: true//初始不展开
		}
		];
//		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "result",
			model : 'MB.model.OrderDetailModel'
		});
		this.callParent(arguments);
	},
	addOrderQuestion: function(form, orderInfo, action) {
		var questionStatus=orderInfo.questionStatus;
		if (orderInfo.logqDesc != null && orderInfo.logqDesc != '') {
			var questionType = eval(orderInfo.logqDesc);
			console.dir(eval(questionType));
			var questionArr = new Array();
			questionArr.push('问题单');
			questionArr.push({ xtype: 'tbspacer' });
			questionType.forEach(function (obj, index) {
				questionArr.push({ text:obj, margins: '50 50 1 5'});
			});
			form.owner.toolbar = Ext.widget({
				xtype: 'toolbar',
//				border: true,
				rtl: false,
				id: 'orderToolbar',
				floating: true,
				fixed: true,
				preventFocusOnActivate: true,
				draggable: {
					constrain: true
				},
				constraintInsets: '0 -' + (Ext.getScrollbarSize().width + 5) + ' 0 0'
			});
			
			form.owner.toolbar.add(questionArr); 
			form.owner.toolbar.show();
			form.owner.toolbar.anchorTo(
				document.body,
				Ext.optionsToolbarAlign || 'tr-tr',
				[-(Ext.getScrollbarSize().width + 5), 50],
				true,
				true
			);
//				Ext.getCmp('orderQuestionType').hidden=true;
//				onLaunch
//				Ext.getCmp('orderQuestionType').render();
//				Ext.getCmp('orderQuestionType').reset();   
//				Ext.getCmp('orderQuestionType').hide();  
//				Ext.getCmp('orderQuestionType').show();
		} else {
			if (form.owner.toolbar) {
				form.owner.toolbar.hide();
			}
		}
	},
	initData : function (orderForm) {
		var me = this;
		var deliveryGrid = me.down("deliveryDetail");
		var payGrid = me.down("payDetail");
		var goodGrid = me.down("goodDetail");
		me.load({
			url : basePath + '/custom/orderInfo/getOrderDetail',
			params : {
				"orderSn" : orderSn,
				"isHistory": isHistory
			},
			timeout:90000,
			success : function(opForm, action) {
				// 数据加载成功后操作
				var orderInfo = opForm.reader.rawData.result;
				var orderNorth = me.up('orderDetailView').down('orderNorth');
				var orderSetModule = me.down("orderSetModule");
				var shipSetModule = me.down("shipSetModule");
				var paySetModule = me.down('paySetModule');
				var goodSetModule = me.down('goodSetModule')
				var otherSetModule = me.down('#otherSetModule')
				shipSetModule.getForm().findField("encodeMobile").setValue('*****');
				shipSetModule.getForm().findField("encodeTel").setValue('*****');
				orderSetModule.getForm().findField("ordertotalstatusStr")
					.setValue(getCombineStatus(orderInfo.orderStatus,orderInfo.payStatus,orderInfo.shipStatus) );
				channelCode = orderInfo.orderFrom;
				userId = orderInfo.userId;
				activityType = orderInfo.activityType;
				transType = orderInfo.transType;
				orderType = orderInfo.orderType;

				// 展示订单费用信息
				var showTotalFee = '<div style= "float: right; color: #666;">'
					+ '商品总金额：  ' + numFixed(orderInfo.goodsAmount, 2)
					+ ' 元 - 折让：  ' + numFixed(orderInfo.discount, 2)
					+ ' 元 + 综合税费：  ' + numFixed(orderInfo.tax, 2)
					+ ' 元 + 配送费用：  ' + numFixed(orderInfo.shippingTotalFee, 2)
					/*+ ' 元 + 保价费用：  ' + numFixed(orderInfo.insureTotalFee, 2)
					+ ' 元 + 支付费用：  ' + numFixed(orderInfo.payTotalFee, 2)*/
					+ ' 元</div>';
				paySetModule.down("#showTotalFee").update(showTotalFee);
				var totalFee = '<div style= "float: right; color: #666;">= 订单总金额：   ' + numFixed(orderInfo.totalFee, 2) +'元</div>';
				paySetModule.down("#totalFee").update(totalFee);
				var exchangeReturnFee = "";
				// 换货订单按正常订单展示
				if (orderInfo.orderType == 2) {
					orderNorth.down("#onlyReturn").show();
//					exchangeReturnFee = '- 换货时退货转入款：' + orderInfo.exchangeReturnFee + '元';
				} else {
					orderNorth.down("#onlyReturn").hide();
				}
				var showTotalPayable = '<div style= "float: right; color: #666;">'
					+ '- 已付款金额：  ' + numFixed(orderInfo.moneyPaid, 2)
					+ exchangeReturnFee
					+ ' 元 - 使用余额：  ' + numFixed(orderInfo.surplus, 2)
					+ ' 元 - 使用红包【'+ orderInfo.bonusName +'】：  ' + numFixed(orderInfo.bonus, 2)
					+ ' 元</div>';
				paySetModule.down("#showTotalPayable").update(showTotalPayable);
				var totalPayable = '<div style= "float: right; color: #666;">= 应付款金额：   ' + numFixed(orderInfo.totalPayable, 2) +'元</div>';
				paySetModule.down("#totalPayable").update(totalPayable);
				console.dir(opForm.reader.rawData);
				var returnFee = opForm.reader.rawData.returnFee;
				// 退货单信息
				var returnTransPrice = '<div style= "float: right; color: #666;">所退商品的总金额：   ' + numFixed(returnFee.returnallmoney, 2)
					+ ' 元  - 所退商品的总折让值：' + numFixed(returnFee.returnalldiscount, 2)
					+ ' 元  = 所退商品成交价总金额：' + numFixed(returnFee.returntransallmoney, 2)
					+' 元</div>';
				paySetModule.down("#returnTransPrice").update(returnTransPrice);
				var returnSettPrice = '<div style= "float: right; color: #666;">- 所退商品分摊红包总金额：   ' + numFixed(returnFee.returnbonusallmoney, 2)
				+ '元 = 所退商品财务总金额：' + numFixed(returnFee.returnsettlemoney, 2)
				+' 元</div>';
				paySetModule.down("#returnSettPrice").update(returnSettPrice);
				var toolbar = orderNorth.down('toolbar');
				var child = toolbar.child('button') 
				var buttons = toolbar.items.items;
				var statusUtils = opForm.reader.rawData.statusUtils;
				console.dir(statusUtils);
				orderSetModule.tools[1].enable();
				if (statusUtils['consignee'] == 1) {
					shipSetModule.tools[1].enable();
				} else {
					shipSetModule.tools[1].disable();
				}
				if (statusUtils['consignee'] == 1) {
					shipSetModule.tools[1].enable();
				} else {
					shipSetModule.tools[1].disable();
				}
				if (statusUtils['decode'] == 1) {
					me.down('#decodeLinkMobile').enable();
				} else {
					me.down('#decodeLinkMobile').disable();
				}
				if (statusUtils['other'] == 1) {
					otherSetModule.tools[1].enable();
				} else {
					otherSetModule.tools[1].disable();
				}
				if (statusUtils['goods'] == 1) {
					goodSetModule.tools[1].enable();
				} else {
					goodSetModule.tools[1].disable();
				}
				if (statusUtils['money'] == 1) {
					paySetModule.tools[1].enable();
				} else {
					paySetModule.tools[1].disable();
				}
				buttons.forEach(function (item, index) {
					var value = item.value;
					// 权限可用 false 状态 1 按钮可点击
					var itemId = item.itemId;
					var orderStatus = statusUtils[value];
					console.dir("itemId" + itemId);
					console.dir(value);
					console.dir("orderStatus" + orderStatus);
					if (itemId == 'conUnConMenu' || itemId == 'norQuesMenu'
							|| itemId == 'occRealMenu' || itemId == 'cancelMenu') {
						var buttonMenu = statusUtils[itemId];
						var orderStatus = statusUtils[buttonMenu.action];
						item.value = buttonMenu.action;
						item.setText(buttonMenu.name);
						item.setTooltip(buttonMenu.name);
						if (itemId == 'occRealMenu' || (orderStatus == 1)) {
							item.enable();
						} else {
							item.disable();
						}
					} else if (orderStatus == 1) {
						item.enable();
					} else {
						if ('addrefuse' == value || 'addReturn' == value 
								|| 'addExtra' == value || 'addExchang' == value) {
							if (orderStatus == 1) {
								item.enable();
							} else {
								item.disable();
							}
						} else if ('occupy' == value || 'release' == value) {
							item.enable();
						} else {
							item.disable();
						}
					}
				});
				me.addOrderQuestion(opForm, orderInfo, action);
				deliveryGrid.store.loadData(opForm.reader.rawData.delivery);
				payGrid.store.loadData(opForm.reader.rawData.payDetail);
				goodGrid.store.loadData(opForm.reader.rawData.goodDetail);
				var goodsTotal = 0.000000;
				goodGrid.getStore().each(function(record) {
					goodsTotal += record.get("subTotal");
				});
				var total = '<div style= "float: right;color:black;font-weight:bold">总计：' + goodsTotal +' 元</div>';
				goodSetModule.down("#total").update(total);
				// 
				var orderActionGrid = me.up('orderDetailView').down('orderSouth');
				orderActionGrid.store.on('beforeload', function (store){
					params = {"orderSn" : orderSn, "histroy": isHistory};
					Ext.apply(store.proxy.extraParams, params);
				});
				orderActionGrid.getStore().load();
			},
			failure : function(opForm, action) {
				Ext.msgBox.remainMsg('加载订单数据异常', action.response.statusText, Ext.MessageBox.ERROR);
			}
		});
		
	}
});