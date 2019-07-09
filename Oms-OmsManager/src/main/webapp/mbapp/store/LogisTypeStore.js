
/**
 *一般问题单和物流问题单 
 ***/
Ext.define("MB.store.LogisTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: selectQType
});

