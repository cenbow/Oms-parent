Ext.define("MB.view.goodsReturnChange.GoodsReturnChangeSouth", {
	extend: "Ext.grid.Panel",
	alias: 'widget.goodsReturnChangeSouth',
	id:'goodsReturnChangeSouth',
	store: Ext.create('Ext.data.Store', {
			model: "Ext.data.Model",
			proxy: {
				type: 'ajax',
				url: basePath + 'custom/commonLog/getGoodsReturnChangeAction',
				actionMethods: {
					read: 'POST'
				},
				reader: {
					rootProperty: 'root',
					totalProperty: 'totalProperty'
				},
				simpleSortMode: true
			},
			autoLoad : false
		}),
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: true,
//	collapsible: true,
	initComponent: function () {
		var clientWidth = document.body.clientWidth;
		this.columns = [
			{ text: '操作者', width: clientWidth * 0.08, dataIndex: 'actionUser' },
			{ text: '操作时间', width: clientWidth * 0.14, align: 'center', dataIndex: 'logTime' ,
				renderer:function(value){ 
					if(value){
		            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
		             return createTime;  
					}
		         }
			},
			{ text: '处理状态', width: clientWidth * 0.08, align: 'center', dataIndex: 'status',
				renderer: function (v) {
					if(v=='0'){
						return '已取消';
					}else if(v=='1'){
						return '待沟通';
					}else if(v=='2'){
						return '已完成';
					}else if(v=='3'){
						return '待处理';
					}else{
						return '';
					}
				}
			},
			/*{ text: '付款状态', width: clientWidth * 0.08,align: 'center', dataIndex: 'payStatus',
				renderer: function (v) {
					return getPayStatus(v);
				}
			},
			{ text: '发货状态', width: clientWidth * 0.08,align: 'center', dataIndex: 'shippingStatus',
				renderer: function (v) {
					return getShipStatus(v);
				}
			},*/
			{
				text: '备注',
				width: clientWidth * 0.6,
				dataIndex: 'actionNote',
				renderer: function (v) {
					return '<div>' + v + '</div>';
				}
			}
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		this.callParent(arguments);
	}
});