Ext.define("MB.view.common.ChannelShopComboByMult", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.channelShopComboByMult',
	store : 'ChannelShopStore',
	name : 'orderFroms',
	displayField : 'shopTitle',
	valueField : 'shopCode',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '渠道店铺',
	hiddenName : 'orderFroms',
	multiSelect : true,
	emptyText : '请选择渠道店铺',
	editable : false,
//	allowBlank: true,  
	listConfig: {  
		getInnerTpl: function() {
			return'<input type=checkbox id="{shopCode}" name="" value="{shopCode}">{shopTitle}';  
		},
		listeners:{
			itemclick:function(view, record, item, index, e, eOpts ){
				var isSelected = view.isSelected(item);  
				var checkboxs = item.getElementsByTagName("input");
				if(checkboxs!=null) {
					var checkbox = checkboxs[0];
					
					if(!isSelected) {
						checkbox.checked = true;  
					}else{
						checkbox.checked = false;  
					}
				}
			}
		}
	},
	initComponent : function() {
		this.callParent(arguments);
	}
});