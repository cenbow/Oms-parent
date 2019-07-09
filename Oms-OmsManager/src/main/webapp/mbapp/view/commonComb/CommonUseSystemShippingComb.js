Ext.define("MB.view.commonComb.CommonUseSystemShippingComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.commonUseSystemShippingComb',
	store: Ext.create('Ext.data.Store', {
		fields : [ {
			name : 'shippingId'
		} , {
			name : 'shippingCode'
		} , {
			name : 'shippingName'
		}],
		autoLoad: false,
		proxy : {
			type : 'ajax',
			actionMethods : {
				read : 'POST'
			},
			reader : {
				type : 'json'
			}
		}
	}),
	displayField : 'shippingName',
	valueField : 'shippingCode',
	queryMode : 'remote',
	fieldLabel : '<b>配送方式</b>',
	emptyText : '请选择配送方式',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});