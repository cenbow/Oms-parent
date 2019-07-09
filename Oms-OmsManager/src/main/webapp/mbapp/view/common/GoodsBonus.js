Ext.define("MB.view.common.GoodsBonus", {
	extend: "Ext.grid.Panel",
	alias: 'widget.commongoodsbonus',
	title: '红包信息',
	width: '100%',
	requires : [ 'Ext.grid.plugin.CellEditing'],
	frame: true,
	store: "GoodsBonusStore",
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
//		me.listeners = {
//			selectionchange: 'onSelectionBonusTotal'
//		};
//		me.selType = 'checkboxmodel';
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
			{ text: '红包选中', hidden: true, dataIndex: 'initSelected' }, // 0
			{ text: '红包名称', width: 380, dataIndex: 'cardLnName' }, // 0
			{ text: '红包卡号', width: 280, dataIndex: 'cardNo' }, // 1
			{ text: '红包类型', width: 200, dataIndex: 'rangeName' }, // 2
			{ text: '红包面值', width: 180, dataIndex: 'cardMoney'}, // 3
			{ text: '红包生效时间', width: 180, dataIndex: 'effectDate'}, // 4
			{ text: '红包过期时间', width: 180, dataIndex: 'expireTime'}, // 5
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
			// 面值${package.cardMoney}元&nbsp;&nbsp;消费满${package.cardLimitMoney}&nbsp;元可以使用
		]
		me.callParent(arguments);
	},
	onSelectionBonusTotal : function (model, selected, eOpts) {// 已勾选红包列表
		var bonusIds = new Array();
		if (selected && selected.length > 0) {
			var bonusTotal = 0.00;
			selected.forEach(function (record, index) {
				bonusTotal += record.get("cardMoney");
				bonusIds.push(record.get("cardNo"));
			});
			this.up("form").down("ordereditlist").down("#recalculateButton").setDisabled( false );
		} else {
			this.up("form").down("ordereditlist").down("#recalculateButton").setDisabled( true );
		}
		console.dir(this);
		console.dir(this.up("form"));
		console.dir(this.up("form").query("#editGoods_bonusIds"));
		this.up("form").query("#editGoods_bonusIds")[0].setValue(bonusIds);
	},
	onCellBeforeEdit: function (editor, ctx, eOpts) { // 商品编辑属性初始化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		if (clickColIdx == 0 && parent.editGoodsType == 2) { // 商品数量
			var numberf = ctx.grid.columns[8].getEditor(ctx.record);
			numberf.setMaxValue(record.get("initGoodsNumber"), false);
		} else if (clickColIdx == 7 && parent.editGoodsType != 2) { // 颜色
			var colorCombo = ctx.grid.columns[15].getEditor(ctx.record);
			colorCombo.getStore().loadData(record.get("colorChild"), false);
		} else if (clickColIdx == 8 && parent.editGoodsType != 2) { // 尺码
			var sizeCombo = ctx.grid.columns[16].getEditor(ctx.record);
			sizeCombo.getStore().loadData(record.get("sizeChild"), false);
		}
	},
	onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		if (clickColIdx == 3 || clickColIdx == 5 || clickColIdx == 6) { // 成交价 || 红包分摊金额 ||商品数量
			var transactionPrice = ctx.record.get("transactionPrice");
			var goodsNumber = ctx.record.get("goodsNumber");
			var shareBonus = ctx.record.get("shareBonus");
			ctx.record.set("settlementPrice", numFixed(transactionPrice - shareBonus, 4));
			ctx.record.set("subTotal", transactionPrice * goodsNumber);
		}
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
		goodsForm.down('ordereditlist').down('#bonusTotal').update('红包面值合计: ' + bonusTotal + " 元");
		goodsForm.down("#bonusIds").setValue(bonusIds);
	}
});