var msgTip;
Ext.define("MB.store.GoodDetailStore", {
	extend: "Ext.data.Store",
	model: "MB.model.GoodDetailModel",
	groupField:'depotCode',
	listeners:{  
             /*beforeload:function(){  
                   msgTip = Ext.MessageBox.show({  
                   title:'提示',  
                   msg:'load......'  
                });  
       }  */
   }  
});

