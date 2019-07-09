Ext.define("MB.view.orderQuestionList.OrderQuestionView", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderQuestionView',
	requires: [
	           'MB.view.orderQuestionList.OrderQuestionGrid',
	           'MB.view.orderQuestionList.OrderQuestionPanl',   
	           'MB.view.orderQuestionList.ShortageQuestionGrid', 
	           'MB.view.orderQuestionList.ImportOrderLogisticsQuestion',
	    
	           	'MB.model.OrderCustomDefine',
	           	'MB.model.ComboModel'   
	  
	           
	        ],

	items : [ 
	    {
			title : '问题单列表查询',
			xtype: 'orderQuestionPanl',
			region: "north"
	    }, {
			/*xtype: 'panel',
			layout: 'column',
	
			defaultType: 'container',*/
			
	    	title : '问题单列列表',
			xtype: 'orderQuestionGrid',
			region : "south"
	    	
			
	/*		items: [{
					id: 'orderQuestionGridTable',
					columnWidth: 1,
					items:[{	
						title : '问题单列表',
						xtype: 'orderQuestionGrid'	
					}]
				},{
					id: 'shortageQuestionGridTable',
			
					items:[{		
						title : '缺货列表',
						xtype: 'shortageQuestionGrid'	
					}]
				}
			]*/
		}
	]
});