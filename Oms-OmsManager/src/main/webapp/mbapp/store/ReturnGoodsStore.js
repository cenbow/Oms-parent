Ext.define("MB.store.ReturnGoodsStore", {
	extend: "Ext.data.Store",
	id:"returnGoodsStore",
	model: "MB.model.ReturnGoodsModel"
	/*listener:{
		//定义store时
		load :function(rcds){
		var grid=Ext.getCmp('returnGoodsList'); 
		grid.getSelectionModel().selectAll();
		}
	}*/
	/*data:result,
	proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'comon'
        }
    }*/
});


