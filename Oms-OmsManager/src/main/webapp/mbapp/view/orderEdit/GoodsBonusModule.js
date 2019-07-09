Ext.define("MB.view.orderEdit.GoodsBonusModule", {
	extend: "Ext.grid.Panel",
	alias: 'widget.goodsBonusModule',
	title: '红包信息',
	width: '100%',
	requires : [ 'Ext.grid.plugin.CellEditing'],
	frame: true,
	store: Ext.create('Ext.data.Store', {
		model: "Ext.data.Model"
	}),
	width : '100%',
	autoHeight:true,
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder: false,
	border: false,
	style:'border-width:0 0 0 0;',
	forceFit : true,
	viewConfig : { enableTextSelection: true },
	initComponent: function () {
		var me = this;
		me.columns = [
			{
				xtype: 'checkcolumn',
				header: '选中',
				dataIndex: 'selected',
				width: 90,
				stopSelection: false,
				listeners : {
					checkChange: me.onCheckChange
				}
			},
			{ text: '红包选中', hidden: true, dataIndex: 'initSelected' },
			{ text: '红包名称', width: 380, dataIndex: 'cardLnName' },
			{ text: '红包卡号', width: 280, dataIndex: 'cardNo' },
			{ text: '红包类型', width: 200, dataIndex: 'rangeName' },
			{ text: '红包面值', width: 180, dataIndex: 'cardMoney'},
			{ text: '红包生效时间', width: 180, dataIndex: 'effectDate'},
			{ text: '红包过期时间', width: 180, dataIndex: 'expireTime'},
			{
				text: '红包使用限制',
				width: 400,
				dataIndex: 'cardLimitMoney',
				renderer : function (value, metaData, record) {
					var cardMoney = record.get("cardMoney");
					var returnMsg = "面值￥" + cardMoney + "元&nbsp;&nbsp;消费满￥" + value + "元可以使用";
					return returnMsg;
				}
			}
		]
		me.callParent(arguments);
	},
	onCheckChange : function (cc, rowIndex, checked) {
		var grid = cc.up('grid');
		var record = grid.getStore().getAt(rowIndex);
		var initSelected = record.get("initSelected");
		if (!initSelected) {
			Ext.msgBox.remainMsg('选中红包', "红包不在有效期内", Ext.MessageBox.ERROR);
			cc.setDisabled(true);
			return ;
		}
		var selected = record.get("selected");
		// 计算红包总面值
		grid.bonusTotal(grid);
	},
	bonusTotal : function(bonusGrid) { // 商品编辑属性初始化.
		var bonusTotal = 0.0000;
		var bonusIds = new Array();
		var goodsForm = bonusGrid.up('form');
		bonusGrid.store.each(function(record, i) {
			var selected = record.get("selected");
			if (selected) {
				bonusIds.push(record.get("cardNo"));
				bonusTotal += record.get("cardMoney");
			}
		});
		console.dir(bonusIds);
		goodsForm.down('#districtGoods').down('#bonusTotal').update('红包面值合计: ' + bonusTotal + " 元");
		goodsForm.down("#bonusIds").setValue(bonusIds);
	}
});