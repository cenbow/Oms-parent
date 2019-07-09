/**
 *每页分页数
 ****/
Ext.define('MB.store.QueryPageStore', {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.query_pagesize_type
});
