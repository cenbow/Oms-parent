Ext.define("MB.view.orderDetail.GoodDetail", {
	extend : "Ext.grid.Panel",
	alias : 'widget.goodDetail',
	requires: ['Ext.grid.feature.Grouping'],
	store: Ext.create('Ext.data.Store', {
		model: "MB.model.OrderAction",
		groupField:'depotCode'
	}),
	autoRender : true,
	columnLines : true,
	loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
//	frame : true,
	resizable : true,
//	forceFit : true,
	viewConfig:{
		forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
		scrollOffset: 0 //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
	},
			// collapsible: true,
	initComponent : function() {
		var me = this;
		var spancss = '<span style="color:black;font-weight:bold";>';
		var tpl = ['交货单号：'+spancss+'{[values.rows[0].get("orderSn") == null? "": values.rows[0].get("orderSn")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '发货仓：'+'{[values.rows[0].get("deliveryType")=="3"?"<span style=\'color:red;font-weight:bold\'>"+values.rows[0].get("depotCode")+"</span>":"<span style=\'color:black;font-weight:bold\'>"+values.rows[0].get("depotCode")+"</span>"]}'+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '快递单号：'+spancss+'{[values.rows[0].get("invoiceNo") == null? "": values.rows[0].get("invoiceNo")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '配送方式：'+spancss+'{[values.rows[0].get("shippingName") == null? "": values.rows[0].get("shippingName")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '配送状态：'+spancss+'{[values.rows[0].get("shippingStatusName") == null? "": values.rows[0].get("shippingStatusName")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '合计金额：'+spancss+'{rows:this.getTotal}',{
		   			getTotal : function(rows){
		   				var totalPrice = 0.000000;
		   				for(var i= 0;i<rows.length;i++){
		   					totalPrice += rows[i].get("subTotal");
		   				}
		   				return numFixed(totalPrice, 2)+'元';
		   			}
		   		}];
			//+ '  ERP发货状态：{[name != null && name != 0 && name != \'DEFAULT\' ? values.rows[0].get("shippingStatus") : "<a href=\'http://order.bang-go.com.cn/BGOrderManager/manager/erpShipInfo?shipSn=FH1204275136860001\'>ERP发货单状态查询</a>" ]}'
			;
		me.features = [ {
			// ftype: 'groupingsummary',
			ftype : 'grouping',
			// remoteRoot: 'summaryData'
			groupHeaderTpl : tpl,
			hideGroupedHeader : true,
			enableGroupingMenu : false
		} ],
		me.columns = [
				{
					text : '商品名称',
					// width : 75,
					dataIndex : 'goodsName',
					sortable : false,
					menuDisabled : true,
				},
				{
					text : '商品属性',
					width : 75,
					align : 'center',
					dataIndex : 'extensionCode',
					sortable : false,
					menuDisabled : true,
					renderer : function (value, meta, record) {
									if (value == null || value== '' || value == 'common') {
										return '普通商品';
									} else if (value == 'gift') {
										return '赠品';
									} else if (value == 'c2b') {
										return 'C2B定制';
									} else if (value == 'group') {
										return '套装';
									} else if (value == 'prize') {
										return '奖品';
									}else if (value == 'c2m') {
										return 'C2M定制';
									}
								}
				},
				{
					header : 'depotCode',
					hidden : true,
					sortable : false,
					dataIndex : 'depotCode'
				},
				{
					text : '货号',
					width : 75,
					align : 'center',
					dataIndex : 'goodsSn',
					sortable : false,
					menuDisabled : true
				},
				{
					header : '规格',
					columns : [ {
						header : "颜色",
						width : 80,
						dataIndex : 'goodsColorName',
						sortable : false,
						locked:true,
						menuDisabled : true
					}, {
						header : "尺寸",
						width : 80,
						dataIndex : 'goodsSizeName',
						sortable : false,
						locked:true,
						menuDisabled : true
					} ]
				},
				{
					text : '产品条形码',
					// width : 80,
					dataIndex : 'barcode',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '企业SKU码',
					// width : 80,
					dataIndex : 'customCode',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '商品价格',
					width : 75,
					align : 'center',
					dataIndex : 'goodsPrice',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '数量',
					width : 85,
					align : 'center',
					dataIndex : 'goodsNumber',
					sortable : false,
					menuDisabled : true,
					renderer : function(value, meta, record) {
						var returnNum = record.get('returnNum');
						var returnRemainNum = record.get('returnRemainNum');
						var lackNum = record.get('lackNum');
						var returnmsg = value;
						if (lackNum && lackNum > 0) {
							returnmsg += '</br><font style="color:red">（缺货 ' + lackNum + '）</font>'
						}
						if (returnRemainNum && returnRemainNum > 0) {
							returnmsg += '</br><font style="color:red">（待退 ' + returnRemainNum + '）</font>'
						}
						if (returnNum && returnNum > 0) {
							returnmsg += '</br><font style="color:red">（已退 ' + returnNum + '）</font>'
						}
						return returnmsg;
					}
				},
				/*{
					text : '会员价格',
					width : 75,
					align : 'center',
					dataIndex : 'goodsPrice',
					sortable : false,
					menuDisabled : true
				},*/
				{
					text : '成交价格',
					width : 75,
					align : 'center',
					dataIndex : 'transactionPrice',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '财务价格',
					width : 75,
					align : 'center',
					dataIndex : 'settlementPrice',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '分摊金额',
					width : 75,
					align : 'center',
					dataIndex : 'shareBonus',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '税费',
					width : 75,
					align : 'center',
					dataIndex : 'tax',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '税率',
					width : 75,
					align : 'center',
					dataIndex : 'taxRate',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '积分金额',
					width : 75,
					align : 'center',
					dataIndex : 'integralMoney',
					sortable : false,
					menuDisabled : true
				},
				{
					text : '商品促销',
					// width : 150,
					align : 'center',
					dataIndex : 'promotionDesc',
					sortable : false,
					menuDisabled : true
				}, { text : '打折券', width : 200,dataIndex : 'useCard',
					renderer : function (value, meta, record) {
						//获取打折券详情列表
						var couponList = record.get('couponList');
						//拼装返回链接
						var returnValue = '';
						if (value&&value!=null&&value!='') {
							var array = value.split(":");
							for (var i=0 ; i< array.length ; i++)
							{
								var flag = '0';//用于标记本轮循环中  打折券卡号是否匹配到对应的详细信息  如果没有匹配到  就显示无链接的卡号[折扣未知]
								if(couponList&&couponList!=null){
									for(var j=0 ; j<couponList.length ; j++){
										if(couponList[j].cardNo==array[i]){
											returnValue = returnValue + array[i] +'<font style="color:red">['+couponList[j].cardMoney+ '折券]</font></br>';
											flag = '1';
										}
									}
								}
								if(flag=='0'){
									returnValue = returnValue + array[i] + '[折扣未知]</br>';
								}
							} 
						}
						return returnValue;
					} 
				}, {
					text : '折让金额',
					width : 75,
					align : 'center',
					dataIndex : 'discount',
					sortable : false,
					menuDisabled : true
				},/* {
					text : '可用数量',
					width : 70,
					align : 'center',
//					dataIndex : 'allStock',
					dataIndex : 'availableNumber',
					sortable : false,
					menuDisabled : true
				}, */{
					text : '小计',
					width : 75,
					align : 'center',
					dataIndex : 'subTotal',
					sortable : false,
					menuDisabled : true
				}
		];
		
		me.bbar = [ {
			xtype : 'component',
			itemId : 'subTotal',
			margin : '0 0 0 20'
		} ];
		me.viewConfig = {
			enableTextSelection : true
		};// 设置单元格可复制
		me.callParent(arguments);
	},
	recalculateTotal : function(grid) { // 合计
//		var goodsTotal = 0.000000;
//		grid.getStore().each(function(record) {
//			goodsTotal += record.get("subTotal");
//		});
//		grid.down('#subTotal').update("<span style='color:black;font-weight:bold';>总计: " + goodsTotal + " 元</span> ");
	},
	formatECode : function (value, meta, record) {
		if (value == null || value== '' || value == 'common') {
			return '普通商品';
		} else if (value == 'gift') {
			return '赠品';
		} else if (value == 'gift1') {
			return '赠品';
		} else if (value == 'c2b') {
			return 'C2B商品';
		} else if (value == 'group') {
			return '套装';
		} else if (value == 'prize') {
			return '奖品';
		}
	}
});