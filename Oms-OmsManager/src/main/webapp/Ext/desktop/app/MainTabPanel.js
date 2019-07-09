Ext.onReady(function() { 
	
	/** 
	 *定义右侧面版 
	 */  
	Ext.define('mainTabPanel', {  
        extend: 'Ext.tab.Panel', 
        //重写页面加载方法，在该方法中，定义一个iframe，用来装载JSP页面  
        loadPage:function(url, id, title, icon, reload){  
            var tab = this.getComponent(id);  
            if (tab) {  
                this.setActiveTab(tab);  
            } else {  
            	if (url.indexOf("http://") == -1) {
            		// 应用内的地址请求
            		url = path + "/" + url;
            	}
                var p = this.add(new Ext.panel.Panel({  
                    id:id,  
                    title:title,  
                    closable:true,  
                    icon:icon,  
                    html:'<iframe src="' + url + '"width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'  
                }));  
                this.setActiveTab(p);  
            }  
        }  
     });
	
	/** 
     *创建顶部面板 
     */  
	var topPanel = Ext.create('Ext.panel.Panel', {  
		region : 'north',  
		height : 55  
    }); 
	
	/** 
	 *定义顶左侧面板 
	 */  
	var leftPanel = Ext.create('Ext.panel.Panel', {  
    	region : 'west',  
        title : '导航栏',  
        width : 230,  
        layout : 'accordion',  
        split:true,  
        expanded:true,
        collapsible : true//是否可以折叠收缩  
     });
	
	/** 
	 *创建中间面板 
	 */  
	var centerPanel = Ext.create('mainTabPanel', {  
		region : 'center',  
		layout : 'fit',  
		tabWidth : 120,  
     	items : []  
    });
	
	/** 
	 * 创建视图 
	*/  
    Ext.create('Ext.container.Viewport', {  
		layout : 'border',  
		renderTo : Ext.getBody(),  
		items : [leftPanel, centerPanel ]  
	});  
    
    /** 
     * 组建树 
     */  
    var buildTree = function(data) { 
        return Ext.create('Ext.tree.Panel', {  
        	rootVisible : false,  
        	border : false,  
        	store : Ext.create('Ext.data.TreeStore', {  
             	root : {  
             		expanded : true,  
                 	children : data  
             	}  
        	}),  
        	listeners : {  
            	'itemclick' : function(view, record, item, index, e) {  
                 	var leaf = record.get('leaf');  
                    if (leaf) {  
                        
                    	var id = record.get('id');  
                		var text = record.get('text');
                    	var url = record.get('url');
                        
                    	if (url) {
                    		centerPanel.loadPage(url,'menu' + id, text);
                    	}
                     	
                    }  
            	},  
            	scope : this  
        	}  
        });  
    };  
    
    // 获取菜单
    var menuObj = getMenuObj(mid);
    var treeDataObj = getTreeMenu(menuObj);
    var treeData = [treeDataObj];
    
    Ext.each(treeData, function(el) {  
    	var panel = Ext.create('Ext.panel.Panel', {  
        	id : el.id,  
          	title : el.text,  
         	layout : 'fit'  
      	});  
    	panel.add(buildTree(el));  
    	leftPanel.add(panel);  
    }); 
});

// 获取节点本身
function getMenuObj(id) {
	var treeAllMenu = parent.menuData;
	
	for (var i = 0; i < treeAllMenu.length; i++) {
		var tempData = treeAllMenu[i];
		
		if (tempData.id == id) {
			var node = createNode(tempData);
			return node;
		}
	}
	
	return {"id":id};
}

// 获取传入id的子菜单
function getTreeMenu(datas) {
	var treeAllMenu = parent.menuData;
	
	var children = [];
	for (var i = 0; i < treeAllMenu.length; i++) {
		var tempData = treeAllMenu[i];
		var tempId = tempData.id;
		
		if (datas.id != tempId) {
			
			var index = tempId.lastIndexOf("-");
			
			if (index > -1) {
				var ids = tempId.substring(0,index);
				
				if (datas.id == ids) {
					var node = createNode(tempData);
					children.push(node);
					
					if (node.leaf == false) {
						getTreeMenu(node);
					}
				}
			}
		}
	}
	
	datas.children = children;
	return datas;
}

// 创建节点
function createNode(obj) {
	var url = obj.url;
//	if (url != undefined && url != null && url != '') {
//		if (obj.url.indexOf("http://") == -1) {
//			url = parent.path + url;
//		}
//	}
	var node = {};
	node.id = obj.id;
	node.text = obj.name;
	node.url = url;
	node.leaf = obj.leaf == false ? obj.leaf : true;
	node.expanded = obj.expanded == true ? obj.expanded : false;
	
	return node;
}

//获取url中的参数
function getParam(paramName) {
   	var paramValue = "";
 	var isFound = false;
 	if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=")>1) {
  		var arrSource = unescape(this.location.search).substring(1,this.location.search.length).split("&");
      	var i = 0;
    	while (i < arrSource.length && !isFound) {
         	if (arrSource[i].indexOf("=") > 0) {
           		if (arrSource[i].split("=")[0].toLowerCase()==paramName.toLowerCase()) {
                	 paramValue = arrSource[i].split("=")[1];
             		isFound = true;
              	}
         	}
 			i++;
		}   
	}
	return paramValue;
}