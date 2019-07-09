/**
 *缺货列表 
 ***/
Ext.define('MB.view.orderQuestionList.ShortageQuestionGrid', {
	extend : "Ext.grid.Panel",
	alias : 'widget.shortageQuestionGrid',
	store: "ShortageQuestionStore",
	autoRender:true,
	columnLines: true,
	hidden:true,
	width: '100%',
	height:600,
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	id:'shortageQuestion_gridss_id',
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		me.columns = [
	  		{ xtype: 'rownumberer', header : "序号", width: 40, sortable: false},
			{ id : 'customCode', header : "SKU码", align : "center", width : 100, dataIndex : 'customCode'},
			{ id : 'depotCode', header : "发货仓库编码", align : "center", width : 120, dataIndex : 'depotCode'},
			{ id : 'deliverySn', header : "交货单编码", align : "center", width : 120, dataIndex : 'deliverySn'},
			{ id : 'lackReason', header : "缺货原因", align : "center", width : 160, dataIndex : 'lackReason'}       
		];
		me.dockedItems = [{
			xtype: 'pagingtoolbar',
			store: 'ShortageQuestionStore',
			dock: 'bottom',
			displayInfo: true	
		}],
		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	}

});

