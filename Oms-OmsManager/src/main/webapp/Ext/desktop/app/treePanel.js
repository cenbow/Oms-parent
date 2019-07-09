
function createNode(id, text, link, leaf, expanded) {
	var node = {};
	
	node.id = id;
	node.text = text;
	node.link = link;
	node.leaf = leaf == false ? leaf : true;
	node.expanded = expanded ? expanded : false;
	
	return node;
}

function createNode(obj) {
	
	var node = {};
	
	node.id = obj.id;
	node.text = obj.text;
	node.link = obj.link;
	node.leaf = obj.leaf == false ? obj.leaf : true;
	node.expanded = obj.expanded == true ? obj.expanded : false;
	
	return node;
}

// 获取循环父id获取对应的子菜单
function getMenuDataByParentIdAll(id) {
	var data = [];
	for (var i = 0, leng = menuData.length; i < leng; i++) {
		var temp = menuData[i];
		var mid = temp.id;
		// 以-为分隔符号
		var mdata = mid.split("-");
		if (mdata.length == 2 && mdata[0] == id && (temp.right && auth.indexOf("," + temp.right + ",") != -1)) {
			//var node = createNode(temp);
			// 还有子项
			/*if (node.leaf == false) {
				var child = getMenuDataByParentId(mdata[1]);
				node.children = child;
				setChildren(child);
			}*/
			
			data.push(temp);
		}
	}
	
	setChildren(data);
	return data;
}

function setChildren(child) {
	for (var i = 0; i < child.length; i++) {
		var temp = child[i];
		if (temp.leaf == false) {
			var mid = temp.id;
			// 以-为分隔符号
			var mdata = mid.split("-");
			if ((temp.right && auth.indexOf("," + temp.right + ",") != -1)) {
				var child = getMenuDataByParentId(mdata[1]);
				temp.children = child;
				setChildren(child);
			}
		}
	}
}

function getChildrenByType(type) {
	var child = [];
	
	var d = {};
	d = getMenuDataById(type);
	if (d && d.id) {
		d = createNode(d);
		
		var pid = d.id;
		var ids = pid.split("-");
		var data = getMenuDataByParentIdAll(ids[ids.length - 1]);
		
		d.children = data;
	}
	child.push(d);
	return child;
}

function getMainTab() {
	var mt = Ext.getCmp('mainTab');
	return mt;
}

function getTab(id) {
	return Ext.getCmp(id);
}

function createMainTab(obj) {
	var mt = getMainTab();
	var tab = mt.add(obj);
	var ifrm = document.getElementById("iframe-" + obj.id);
	if (ifrm) {
		ifrm.src = obj.url;
	}
	setTimeout(function(){
		mt.setActiveTab(tab);
	}, 500);
	
}

// 关闭某个Tab
function closeTab(id) {
	var mt = getMainTab();
	var tab = getTab(id);
	mt.remove(tab);
}

function createNodeTab(id, title, url) {
	return {
		id: id,
		title: title,
		url:url,
		closable: true,
		html: '<iframe id="iframe-'+id+'" src="' + url + '" frameborder="0" scrolling="auto" style="border:0px none;height:100%; width:100%;"></iframe>'
	}
}

function openMainTab(n) {
	if(n.leaf == true){
   		var mt = getMainTab();
   		
   		var url = n.link;
   		if (!url) {
   			url = n.attributes.link;
   		}
   		
   		if (url && url.indexOf("://") == -1) {
			url = path + url;
		}
   		
		/*if (url && url.substr(0,7) != "http://") {
			url = path + url;
		}*/
		
		var node = createNodeTab(n.id, n.text,url);
      	createMainTab(node);
	}
}

function createTreePanel(type) {
	//menuData = parent.menuData;
	//var childrenData = getChildrenByType(type);
	
	var childrenData =[];
	var treePanel = new Ext.tree.AsyncTreeNode({
		expanded: true,
		children: childrenData
	});
	var west = {
		region: 'west',
	 	collapsible: true,
       	title: '操作列表',
    	xtype: 'treepanel',
      	collapsible:true,
      	autoScroll:true,
      	width:200,
		minSize: 200,
  		maxSize: 200,
  		lines:true,//节点之间连接的横竖线 
	 	split:true,
	  	loader: new Ext.tree.TreeLoader(),
	 	root: treePanel,
	 	rootVisible: false,
	 	bodyStyle:'height:100%',
	 	margins: '0 0 0 0',
	 	listeners:{
	    	click: function(n) {
	        	openMainTab(n);
	     	}
	 	}
	};
	
	var center = {
    	region: 'center',
  		id:'mainTab',
   		xtype: 'tabpanel',
   		enableTabScroll:true,
   		defaults: {autoScroll:true},
   		bodyStyle:'height:100%',
     	activeTab :0,
     	margins: '0 0 0 0',
     	/*items:[{
        	title:'欢迎',
        	html:'您好，感谢你使用本系统！'
     	}],*/
     	plugins: new Ext.ux.TabCloseMenu({
    		closeTabText:'关闭标签',
      		closeOtherTabsText:'关闭其他标签',
       		closeAllTabsText:'关闭所有标签'
   	})
	};
	
	var panel = new Ext.Panel({  
        layout : 'border',
        items : [west,center]  
    });
	var view = new Ext.Viewport({
		layout:'fit',
		items:[panel]	
	});
	
	/*var view = new Ext.Viewport({
		layout:'border',
		items : [west,center]
	});*/
	if (flag) {
		if (childrenData.length > 0) {
			openMainTab(childrenData[0]);
		}
	}
	return view;
}

// 获取url中的参数
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
