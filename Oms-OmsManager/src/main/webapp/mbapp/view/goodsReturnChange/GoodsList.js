Ext.define("MB.view.goodsReturnChange.GoodsList", {
	extend: "Ext.grid.Panel",
	alias: 'widget.goodsReturnChangeGoodsList',
	id:"goodsReturnChangeGoodsList",
//	requires : ['Ext.grid.plugin.CellEditing'],
	store: "GoodsReturnChangeGoodsList",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
//	listeners:{},
		    
//	selType: "checkboxmodel",
	resizable: true,
//	forceFit: true,
	viewConfig:{
		forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
		scrollOffset: 0 //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
	},
//	collapsible: true,
	initComponent: function () {
		
		this.columns = [
			{ text: '申请流水号', width: 80,align: 'center', dataIndex: 'id' ,sortable:false,menuDisabled : true},
			{ text: '类型', width: 80,align: 'center', dataIndex: 'returnType' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    if(value==1){
				    	return "退货";
				    }else if(value==2){
				    	return "换货";
				    }
				}
			},
			{ text: '商品名称', width: 150,align: 'center', dataIndex: 'goodsName' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    var max = 15;  //显示多少个字符
				    meta.tdAttr = 'data-qtip="' + value + '"';
				    return value.length < max ? value :value.substring(0, max - 3) + '...';}},
				    
			{  dataIndex: 'extensionId' ,sortable:false,menuDisabled : true,hidden:true},
			{ text: '货号',align: 'center', dataIndex: 'goodsSn' ,sortable:false,menuDisabled : true},
			{ header: '规格', columns: 
								[ {
									header : "颜色",
									width : 80,
									dataIndex: 'colorName',
									sortable : false,
									align: 'center',
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
									dataIndex: 'sizeName',
									sortable : false,
									align: 'center',
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
			{ text: '企业SKU码', width: 120,align: 'center', dataIndex: 'custumCode',sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    var max = 15;  //显示多少个字符
				    meta.tdAttr = 'data-qtip="' + value + '"';
				    if(value)return value.length < max ? value :value.substring(0, max - 3) + '...';
				
				}},
			{ text: '商品价格', width: 100,align: 'center', dataIndex: 'goodsPrice' ,sortable:false,menuDisabled : true,
					renderer : function (value) {return "￥"+parseFloat(value).toFixed(2)+"元"}
			},
			{ text: '成交价格', width: 100,align: 'center', dataIndex: 'transactionPrice' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+parseFloat(value).toFixed(2)+"元"}
			},
			{ text: '商品促销', width: 100,align: 'center', dataIndex: 'promotionDesc' ,sortable:false,menuDisabled : true},
			{ text: '打折卷', width: 80,align: 'center', dataIndex: 'useCard',sortable:false,menuDisabled : true },
			{ text: '折让', width: 80,align: 'center', dataIndex: 'discount' ,sortable:false,menuDisabled : true},
			{ text: '订购数量', width: 95,align: 'center', dataIndex: 'goodsNumber' ,sortable:false,menuDisabled : true},
			/*{ text: '折扣', width: 75,align: 'center', dataIndex: 'discount' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},*/

			{ text: '退换数', width: 80,align: 'center', dataIndex: 'returnSum' ,sortable:false,menuDisabled : true},
			{ text: '退换货原因', width: 150,align: 'center', dataIndex: 'reason' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    if(value==1){
				    	return "商品质量不过关";
				    }else if(value==2){
				    	return "商品在配送中损坏";
				    }else if(value==3){
				    	return "商品与描述不符";
				    }else if(value==4){
				    	return "尚未收到商品";
				    }else if(value==5){
				    	return "其他（请具体说明）";
				    }
				}
			},
			{ text: '说明', width: 120,align: 'center', dataIndex: 'explain' ,sortable:false,menuDisabled : true,
				renderer: function (value,meta) {
					meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';
//					return '<div>' + value + '</div>';
					return value;
				}
			},
			{ text: '吊牌', width: 80,align: 'center', dataIndex: 'tagType' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    if(value==1){
				    	return "吊牌完好";
				    }else if(value==2){
				    	return "吊牌破损";
				    }else if(value==3){
				    	return "无吊牌";
				    }
				}
			},
			{ text: '外观', width: 80,align: 'center', dataIndex: 'exteriorType' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    if(value==1){
				    	return "外观完好";
				    }else if(value==2){
				    	return "外观有破损";
				    }else if(value==3){
				    	return "外观有污渍";
				    }
				}
			},
			{ text: '赠品', width: 80,align: 'center', dataIndex: 'giftType' ,sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
				    if(value==1){
				    	return "赠品完好";
				    }else if(value==2){
				    	return "赠品破损";
				    }else if(value==3){
				    	return "赠品不全";
				    }else if(value==4){
				    	return "未收到赠品";
				    }
				}
			}
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		
		this.callParent(arguments);
	}
});
