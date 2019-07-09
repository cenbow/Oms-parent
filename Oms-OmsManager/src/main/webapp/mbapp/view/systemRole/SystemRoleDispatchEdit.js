
/****
 *角色和资源分派 
 ****/

function doSuccessfun(id, response, opts) {
	if ("questionData" == id) {
		
		var respText = Ext.JSON.decode(response.responseText);
		
		var data = respText.data;
			
		for(var i=0; i<data.length; i++){
	
			strhtml+=	
		  	'{   xtype: "panel",'+
				'id:"systemRoleDispatchEditLeft",'+
				'name: "roleCode",'+
				'fieldLabel: "角色编码",'+
				'columnWidth : .2'+
		    '},{'+
		    	'xtype: "panel",'+
		    	'id:"systemRoleDispatchEditRight",'+
		        'name: "roleName", '+
		    	'fieldLabel: "角色名称" ,'+
		  
		    	'columnWidth : .8,'+
		    	'items: ['+
		    
		    	']'+
		   ' }';
			
		}

		strhtml+= '[' +strhtml+']';

	}	

}

function doFailurefun(id, response, opts) {
	errorMsg("结果", respText.msg);
}

Ext.define("MB.view.systemRole.SystemRoleDispatchEdit", {
	extend: "Ext.window.Window",
	alias: "widget.systemRoleDispatchEdit",
	title: "编辑角色分派信息",
	width: 400,
	height: 400,
	layout: "fit",
	initComponent: function () {
		var me = this;
		me.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70
			},	
			items: [
			]
		};
		me.buttons = [
			{ text: "保存", action: "save" },
			{ text: "关闭", action: "close" }
		];
		
		me.callParent(arguments);
	}
});
