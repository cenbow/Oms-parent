/**
 *订单输入导出模版类型
 ***/
Ext.define("MB.store.OrderInfoInputExportTemplateTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_info_input_template_type
});