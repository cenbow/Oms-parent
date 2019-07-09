Ext.define("MB.view.returnOrder.ReturnGoodsList", {
	extend: "Ext.grid.Panel",
	alias: 'widget.returnGoodsList',
	id:"returnGoodsList",
	requires : ['Ext.grid.plugin.CellEditing'],
	store: "ReturnGoodsStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	listeners:{
				  /*'headerclick':function(ct,column,e,t,opts) { 
				  	var flag=true;
				  	if(column.fullColumnIndex==0){//申请退单才会触发表头复选框点击事件，退单详情不让点击
				  		var items=ct.up('grid').getSelectionModel().selected.items;
				  		if(items.length==0){
				  			var orderPays={};
				  			var center={};
				  			if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
				  				center=Ext.getCmp('returnForm');
							}else{
								center=Ext.getCmp('returnCenter')
							}
							orderPays=center.getForm().reader.rawData.orderPays;
				  			if(orderPays){
				  				for(var i=0;i<orderPays.length;i++){
				  					if(center.getForm().findField("setPaymentNum"+(i+1)))
				  					center.getForm().findField("setPaymentNum"+(i+1)).setValue(orderPays[i].payTotalfee);
				  				}
				  			}
				  		}
				  	}
				  },*/
			      'selectionchange':function(sm,rowIndex,record,d){
			      //获取选中的行
				    var returnCenter=Ext.getCmp('returnCenter');
					var returnPaySetModule=Ext.getCmp('returnPaySetModule');
					var items=this.getSelectionModel().selected.items;
					var goodData=[];
					for(var i=0;i<items.length;i++){
						goodData.push(items[i].data);
					}
					returnPaySetModule.paySet(goodData);
					if(items.length>0){
						var rawData={};
			  			if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
				  				center=Ext.getCmp('returnForm');
							}else{
								center=Ext.getCmp('returnCenter')
							}
						if(center){
							rawData=center.getForm().reader.rawData;
							if(items.length==rawData.returnGoods.length){//复选框全部选中时
								var orderPays=rawData.orderPays;
								if(orderPays){
					  				for(var i=0;i<orderPays.length;i++){
					  					if(center.getForm().findField("setPaymentNum"+(i+1)))
					  					center.getForm().findField("setPaymentNum"+(i+1)).setValue(orderPays[i].payTotalfee);
					  				}
					  			}
					  			return;
							}
						}
					}
			      },
			      'cellclick' : function(grid, rowIndex, columnIndex, e) { 
//				    console.dir(rowIndex);
//				    console.dir(columnIndex);
//				    console.dir(e);
				    //获取选中的行
				    var rows=grid.getSelectionModel().selected.items;
				    
				    var returnCenter=Ext.getCmp('returnCenter');
					var returnPaySetModule=Ext.getCmp('returnPaySetModule');
					var items=grid.getSelectionModel().selected.items;
					var goodData=[];
					for(var i=0;i<items.length;i++){
						goodData.push(items[i].data);
					}
					returnPaySetModule.paySet(goodData);
				    
				    var len=rows.length;
				    var editFlag=false;
				    for(var i=0;i<len;i++){
				    	//判断当前行的id是否在选中行里
				    	if(e.id==rows[i].id){
				    		editFlag=true;
				    		break;
				    	}
				    }
				    return editFlag;
				}  
	},
		    
//	selType: "checkboxmodel",
	resizable: true,
//	forceFit: true,
	viewConfig:{
		forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
		scrollOffset: 0 //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
	},
//	collapsible: true,
	initComponent: function () {
		if(returnType!=2){
			this.selModel=new Ext.selection.CheckboxModel({checkOnly:true}),
			this.plugins={
				ptype: 'cellediting',
				clicksToEdit: 1,
				listeners: {
					beforeedit: 'onCellBeforeEdit',
					afteredit : 'onCellAfterEdit'
				}
			}
		}
		this.columns = [
			{ text: '商品名称', width: 150,align: 'center', dataIndex: 'goodsName',
				renderer: function(value, meta, record) {
					if(value){
					    var max = 15;  //显示多少个字符
					    meta.tdAttr = 'data-qtip="' + value + '"';
					    return value.length < max ? value :value.substring(0, max - 3) + '...';
					}else {
						return '';
					}
				}
			},	    
			{  dataIndex: 'extensionId' ,sortable:false,menuDisabled : true,hidden:true},
			{ text: '商品属性', width: 75,align: 'center', dataIndex: 'extensionCode' ,
	        	renderer : function (value) { 
					if(value == 'group'){
						return "套装";
					}else if(value.indexOf('gif') != -1){
						return "赠品";
					}else{
						return "普通商品";
					}
				}},
			{ text: '货号',align: 'center', dataIndex: 'goodsSn' ,sortable:false,menuDisabled : true},
			{ header: '规格', columns: 
								[ {
									header : "颜色",
									width : 80,
									dataIndex: 'goodsColorName',
									sortable : false,
									menuDisabled : true,
									renderer: function(value, meta, record) {
									    if(value){
									    var max = 15;  //显示多少个字符
									    meta.tdAttr = 'data-qtip="' + value + '"';
									    return value.length < max ? value :value.substring(0, max - 3) + '...';
									    }
										return ' ';
									}
									
								} , {
									header : "尺寸",
									width : 80,
									dataIndex: 'goodsSizeName',
									sortable : false,
									menuDisabled : true,
									renderer: function(value, meta, record) {
									    if(value){
									    var max = 15;  //显示多少个字符
									    meta.tdAttr = 'data-qtip="' + value + '"';
									    return value.length < max ? value :value.substring(0, max - 3) + '...';
									    }else{
									    	return '';
									    }
									}
									
								} ]
							},
			{ text: '企业SKU码', width: 120,align: 'center', dataIndex: 'customCode',sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    var max = 15;  //显示多少个字符
				    meta.tdAttr = 'data-qtip="' + value + '"';
				    if(value)return value.length < max ? value :value.substring(0, max - 3) + '...';
				
				}},
			{ text: '商品价格', width: 75,align: 'center', dataIndex: 'marketPrice' ,sortable:false,menuDisabled : true,
					renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '成交价格', width: 75,align: 'center', dataIndex: 'goodsPrice' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '财务价格', width: 75,align: 'center', dataIndex: 'settlementPrice' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '分摊金额', width: 75,align: 'center', dataIndex: 'shareBonus',sortable:false,menuDisabled : true ,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '购买数量', width: 75,align: 'center', dataIndex: 'goodsBuyNumber' ,sortable:false,menuDisabled : true},
			{ text: '财务分摊金额', width: 95,align: 'center', dataIndex: 'shareSettle' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},
			/*{ text: '折扣', width: 75,align: 'center', dataIndex: 'discount' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},*/

			{ text: '所属发货仓', width: 75,align: 'center', dataIndex: 'osDepotCode' ,sortable:false,menuDisabled : true},
			{ text: '门店退货量', width: 75,align: 'center', dataIndex: 'shopReturnCount' ,sortable:false,menuDisabled : true},
			{ text: '已退货量', width: 75,align: 'center', dataIndex: 'havedReturnCount' ,sortable:false,menuDisabled : true},
			{ text: '退货量', width: 75,align: 'center', dataIndex: 'canReturnCount' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 0,
						allowDecimals : false
					}/*,
				renderer: function(value, meta, record) {
						var goodsNumber = record.get("goodsBuyNumber");
						var shopReturnCount = record.get("shopReturnCount");
						var havedReturnCount = record.get("havedReturnCount");
//						console.dir(9999);
						var canReturnCount=goodsNumber-shopReturnCount-havedReturnCount;
						if(canReturnCount<0){
							Ext.Msg.show({
								title:'退货',
								text:'退货商品数量不能多于购买商品数量！',
								buttons:Ext.MessageBox.ERROR,
								fn:function(){
								if(btn == 'yes'){
									window.location.reload();
								}
								}
							});
							Ext.msgBox.remainMsg('退货', "退货商品数量不能多于购买商品数量！", Ext.MessageBox.ERROR,function(){
								window.location.reload();
							});
							Ext.Msg.buttonText.yes='确定';
						    Ext.Msg.buttonText.no="取消";
						    Ext.Msg.show({
						        title:"退货",
						        modal:true,
						        msg:'退货商品数量不能多于购买商品数量！',
						        icon:Ext.Msg.Info,
//						        multiline:false,
//						        prompt:true,
						        fn:function callback(btn,text){
						            if(btn=="yes"){
						                window.location.reload();
						            }
						        },
						        buttons:Ext.Msg.YESNO,
						        width:300
						    });
						}
						return canReturnCount;
				}*/
			},
//			{ text: '折算小计', width: 75,align: 'center', dataIndex: 'subTotal' ,sortable:false,menuDisabled : true},
//			{ text: '退换货原因', width: 75,align: 'center', dataIndex: 'returnReason' ,sortable:false,menuDisabled : true},

			{ text: '退差价数量', width: 75,align: 'center', dataIndex: 'priceDifferNum' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 0,
						allowDecimals : false
					}
			},
			{ text: '退差价单价', width: 75,align: 'center', dataIndex: 'priceDifference' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 0,
						decimalPrecision : 2,//精确到小数点后两位  
	                	allowDecimals : true//允许输入小数  
					}
			},
			{ text: '退差价小计', width: 75,align: 'center', dataIndex: 'priceDiffTotal' ,sortable:false,menuDisabled : true,
				renderer:function (value, cellMeta, record, rowIndex, columnIndex, store){
					var priceDifferNum=record.get('priceDifferNum');
					var priceDifference=record.get('priceDifference');
					return priceDifferNum*priceDifference;
				}
			},
			{ text: '原因', width: 100,align: 'center', dataIndex: 'returnReason' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
						xtype: 'textfield',
						allowBlank: false,
						allowDecimals : false
					}
			},
			{ text: '图片url',align: 'center', dataIndex: 'goodsThumb' ,sortable:false,menuDisabled : true,hidden:true},
			{
						text : '商品销售模式',
						align : 'center',
						dataIndex : 'salesMode',
						sortable : false,
						menuDisabled : true,
						hidden : true
			},{
						text : '供销商',
						width : 120,
						align : 'center',
						dataIndex : 'seller',
						sortable : false,
						menuDisabled : true,
						hidden : true,
						renderer : function(value, meta, record) {
							var msg = value;
							if(!msg){
								msg ="";
							}
							if (value && (value == 'MB' || value == 'HQ01') ) {
								msg = '<font style="color:green">' + value + '</font>'
							} else {
								msg = '<font style="color:red">' + value + '</font>'
							}
							return msg;

						}
					}
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		
		this.callParent(arguments);
	},
	hideColumn:function(data,grid){
		if(data.returnType==1||data.returnType==2){
			grid.columns[12].hide();//财务分摊金额
			for(var i=17;i<=19;i++){//退差价
				grid.columns[i].hide();
			}
		}else if(data.returnType==3){
			for(var i=13;i<=19;i++){
				if(i!=16){
					grid.columns[i].hide();
				}
			}
		}else{
			for(var i=12;i<=16;i++){
				grid.columns[i].hide();
			}
		}
		
	},
	subData:function(grid){
//		console.dir(grid);
	},
	onCellBeforeEdit: function (editor, ctx, eOpts) { // 商品属性初始化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		if (clickColIdx == 15) { // 可退货量
			var  goodsNumber = record.get("goodsBuyNumber"),
				 shopReturnCount = record.get("shopReturnCount"),
				 havedReturnCount = record.get("havedReturnCount");
			var	canReturnCount=goodsNumber-shopReturnCount-havedReturnCount;
			var numberf = ctx.grid.columns[16].getEditor(ctx.record);
			numberf.setMaxValue(canReturnCount, false);
		}else if(clickColIdx == 12){//退差价数量
			var  goodsNumber = record.get("goodsBuyNumber");
			var numberf = ctx.grid.columns[17].getEditor(ctx.record);
			numberf.setMaxValue(goodsNumber, false);
		}
	},
	onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
//		console.dir(editor);
//		console.dir(ctx.grid.getStore());
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
	/*	console.dir(999);
		console.dir(ctx.grid);
		var columns=ctx.grid.columnManager.columns;//editable
		for(var i=0;i<columns.length;i++){
			if(i==0){
				columns[i].header="";
			}
				columns[i].processEvent = function(type) { // 加入这一句，可以防止点中修改  
		            if (type == 'click')  
		                return false;  
		        };  
			
		}
		ctx.grid.doLayout();*/
		var record = ctx.record;
		var returnCenter=Ext.getCmp('returnCenter');
		var returnPaySetModule=Ext.getCmp('returnPaySetModule');
//		console.dir(returnType);
		var items=ctx.grid.getSelectionModel().selected.items;
		var goodData=[];
		for(var i=0;i<items.length;i++){
			goodData.push(items[i].data);
		}
		returnPaySetModule.paySet(goodData);
		
		
		/*if (clickColIdx == 16 ) { // 可退货量
			//成交价格
			var goodsPrice = ctx.record.get("goodsBuyNumber");
			var goodsNumber = ctx.record.get("goodsBuyNumber");
			var shopReturnCount = ctx.record.get("shopReturnCount");
			var havedReturnCount = ctx.record.get("havedReturnCount");
			console.dir(returnPaySetModule.getForm().findField("returnGoodsMoney"));
			returnPaySetModule.getForm().findField("returnGoodsMoney").setValue(goodsPrice*shopReturnCount+goodsPrice*havedReturnCount);
		}
		if(clickColIdx == 17 || clickColIdx == 18){//退差价数量||退差价单价
			var priceDifferNum=ctx.record.get('priceDifferNum');
			var priceDifference=ctx.record.get('priceDifference');
			ctx.record.set("priceDiffTotal", priceDifferNum*priceDifference);
		}*/
	}
});
