Ext.define("MB.view.orderDetail.PayDetail", {
	extend: "Ext.grid.Panel",
	alias: 'widget.payDetail',
	store: Ext.create('Ext.data.Store', {
		model: "MB.model.PayDetailModel"
		}),
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	resizable: true,
	forceFit: true,
	initComponent: function () {
		var me = this;
		me.columns = [
			{ text: '付款单编号', width: 100,align: 'center', dataIndex: 'masterPaySn' ,sortable:false,menuDisabled : true },
			{
				text: '支付方式',
				width: 100,
//				align: 'center',
				dataIndex: 'payName',
				sortable: false,
				height : 20,
				menuDisabled : true
				/*renderer: function(value, metaData, record) {
					var id = Ext.id();
					setTimeout(function() {
						var panel = Ext.create('Ext.panel.Panel', {
							bodyBorder:false,
							border:false,
							style:'border-width:0 0 0 0;',
							bodyPadding: 0,
							width: 100,
							height: 18,
							layout: 'column',
							columnWidth : 1,
							items : [{
								xtype : 'displayfield',
								columnWidth : .6,
								value : value,
							} , {
								columnWidth : .4,
								padding: 0,
								text : '修改',
								margin: '0 0 0 1',
								xtype: 'button',
								buttonConfig: {
									text : '修改',
								},
								handler: function () {
									 me.updatePayInfo(record)
								}
							}]
						});
						if (Ext.get(id)) {
							panel.render(Ext.get(id));
						}
					}, 1);
					return '<div id="' + id + '"></div>';
				}*/
			},
			{ text: '付款备注', width: 100,align: 'center', dataIndex: 'payNote' ,sortable:false,menuDisabled : true},
//			{ text: '使用红包', width: 100,align: 'center', dataIndex: 'bonusName' ,sortable:false,menuDisabled : true},
//			{ text: '余额支付', width: 80,align: 'center', dataIndex: 'surplus' ,sortable:false,menuDisabled : true},
			{ text: '付款总金额', width: 80,align: 'center', dataIndex: 'payTotalfee' ,sortable:false,menuDisabled : true},
			//支付状态；0，未付款；1，付款中；2，已付款；3，已结算;4,待确认
			{ text: '支付状态', width: 80,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true,
				renderer : function (value) { 
						if(value==0){
							return "<span style='color:red;'>未付款</span>";
						}else if(value==1){
							return "<span style='color:red;'>付款中</span>";
						}else if(value==2){
							return "<span style='color:red;'>已付款</span>";
						}else if(value==3){
							return "<span style='color:red;'>已结算</span>";
						}else if(value==4){
							return "<span style='color:red;'>待确认</span>";
						}else{
							return value;
						}
					}
			},
			{ text: '支付时间', width: 100,align: 'center', dataIndex: 'payTime' ,sortable:false,menuDisabled : true },
			{ text: '付款单生成时间', width: 100,align: 'center', dataIndex: 'createTime' ,sortable:false,menuDisabled : true },
			{ text: '付款最后期限', width: 100,align: 'center', dataIndex: 'payLasttime' ,sortable:false,menuDisabled : true },
			{
				text: '操作',
				width: 120,
				height : 20,
				dataIndex: 'payStatus',
				renderer: function(value, metaData, record, rowIndex) {
					var id = Ext.id();
					var pay = record.get('pay');
					var unpay = record.get('unpay');
					console.dir(record);
					var editPay = true;
					var editUnpay = true;
					if (pay == 1 && !auth['order_info_pay']) {
						editPay = false;
					}
					if (unpay == 1 && !auth['order_info_unpay']) {
						editUnpay = false;
					}
					// 支付状态和权限
					setTimeout(function() {
						var panel = Ext.create('Ext.panel.Panel', {
							bodyPadding: 0,
							border:false,
							baseCls: 'my-panel-no-border',
//							width: 100,
							height: 20,
							layout: 'column',
							columnWidth : 1,
							items: [{
								columnWidth: 1,
								xtype: 'segmentedbutton',
								allowToggle: false,
								items: [{
									text: '已付款',
									disabled: editPay,
									handler: function () {
										 me.doPay(rowIndex)
									}
								}, {
									text: '未付款',
									disabled: editUnpay,
									handler: function () {
										 me.doUnPay(rowIndex)
									}
								}]
							}]
						});
						if (Ext.get(id)) {
							panel.render(Ext.get(id));
						}
					}, 1);
					return '<div id="' + id + '"></div>';
				}
			}
		];
		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	doPay: function(rowIndex){ // 已付款
		this.payOperation(rowIndex, 'pay', '已付款');
	},
	doUnPay: function(rowIndex){ // 未付款
		this.payOperation(rowIndex, 'unPay', '未付款');
	},
	payOperation : function(rowIndex, payType, payName) {
		var record = this.getStore().getAt(rowIndex);
		console.dir(record);
		var paySn = record.get('paySn');
		var orderSn = record.get('orderSn');
		var params = {"orderSn" : orderSn, "paySn" : paySn};
		Ext.Ajax.request({
			url: basePath + '/custom/orderStatus/' + payType,
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.success == "true") {
					Ext.msgBox.msg('支付单' + payName, results.msg, Ext.MessageBox.INFO);
					var orderForm = Ext.getCmp('orderCenter');
					orderForm.initData(orderForm);
				} else {
					Ext.msgBox.remainMsg('支付单' + payName, results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				console.dir(response);
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	},
	updatePayInfo : function (record) {
		var win = Ext.widget("paymentEdit");
		var paySn = record.get("paySn");
		win.down('paymentEditForm').getForm().findField('paySn').setValue(paySn);
		win.show();
	}
});
