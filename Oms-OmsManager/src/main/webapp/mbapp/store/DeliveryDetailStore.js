var msgTip;
Ext.define("MB.store.DeliveryDetailStore", {
	extend: "Ext.data.Store",
	id:'deliveryDetailStore',
	baseParams:{orderSn:120428767078,isHistroy:0},
	model: "MB.model.DeliveryDetailModel",
	listeners:{  
             /*beforeload:function(){  
                   msgTip = Ext.MessageBox.show({  
                   title:'提示',  
                   msg:'load......'  
                });  
       }  */
   }  
});

