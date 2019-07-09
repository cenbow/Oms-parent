/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

// Sample desktop configuration
MyDesktop = new Ext.app.App({
	init :function(){
		Ext.QuickTips.init();
	},

	getModules : function(){
		// 按权限
		return getRoleWindow();
	},

    // config for the start menu
	getStartConfig : function(){
		
		var no = GetCookie('49BAC005-7D5B-4231-8CEA-16939BEACD67');
		no = no ? no : "";
        return {
            title: '欢迎回来:'+name+' ('+ no+')',
            iconCls: 'user',
            toolItems: toolMenu
        };
	}
});

var toolMenu = [/*{
	    text:'设置背景',
	    iconCls:'settings',
	    handler:function(){
	    	showBackGround();
	    },
	    scope:this
	},'-',{
	    text:'修改密码',
	    iconCls:'pass',
	    handler:function(){
	    	setPass();
	    },
	    scope:this
	},*/'-',{
	    text:'注销登录',
	    iconCls:'logout',
	    handler:function(){
	    	winOut();
	    },
	    scope:this
	}];

/*
 * Example windows
 */
MyDesktop.GridWindow = Ext.extend(Ext.app.Module, {
	id:'grid-win',
    init : function(){
        this.launcher = {
            text: '全部',
            iconCls:'icon-grid',
            handler:this.open,
            scope: this
        }
    },
	createWindow : function(name,title,url) {
		ShortCut(name,title,url);
	},
	open : function(){
    	winSubmit(0);
    }
});

// 提交页面
function winSubmit(flag) {
	var form = document.form;
   	form.action = path + "/main";
   	document.getElementById('flag').value = flag;
   	form.submit();
}

// 登出
function winOut() {

	Ext.MessageBox.confirm( "请确认", "您确定要注销登录吗?", function(button,text){   
    	if(button=="yes") {
    		var form = document.form;
			form.action = path + logout;
			form.submit();
    	} 
	} );   
}

// 根据权限获取对应的数据
function getRoleWindow() {
	// 创建菜单
	return createLauncher();
}

// 获取Tab的子Tab
function getTabItems(urls) {
	if (urls) {
		var data = urls.split(";");
		
		var items = [];
		for (var i = 0; i < data.length; i++) {
			var h = data[i].split(",");
			items.push({
              	title: h[0],
             	header:false,
          		html : '<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="iframe-'+h[1]+'" src="'+h[2]+'" width="100%" height="100%"></iframe>',
              	border:false
         	});
		}
		
		return items;
	} 
	
	return [];
}

// 创建Tab
function CreateTab(id, title, urls) {
	var newWind = MyDesktop.getDesktop().getWindow(id);
	var exist=newWind;
	
	if (!newWind) {
		newWind = MyDesktop.getDesktop().createWindow({
      			id: id,
                title: title,
                width:1000,
                height:550,
                iconCls: 'tabs',
                shim:false,
                animCollapse:false,
                border:false,
                constrainHeader:true,
                layout: 'fit',
                items:
                    new Ext.TabPanel({
                        activeTab:0,
                        items: getTabItems(urls)
                    })
            });
	}
	newWind.show();
}

// 创建windows
function ShortCut(id,titles,url, width, height, max, min) {
	var newWind = MyDesktop.getDesktop().getWindow(id);
    var exist=newWind;
    if(!newWind) {
    	var w = width;
    	var h = height;
    	var bl = false;
    	if (!w) {
    		w = Ext.getBody().getWidth();
    		bl = true;
    	}
    	
    	if (!h) {
    		h = Ext.getBody().getHeight() - 30;
    		bl = true;
    	}
    	newWind = MyDesktop.getDesktop().createWindow({
        	id:id,
       		title:titles,
       		width:w,
         	height:h,
         	iconCls: 'tabs',
      		shim:true,
      		modal : true,
      		maximized:bl,
      		maximizable:max,
      		minimizable:min,
         	animCollapse:false,
         	expandOnShow:true,
         	resizable:false,
          	border:false,
          	closeAction:'close', 
        	constrainHeader:true,
       		layout: 'fit',
          	html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="iframe-'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
 		});
 	}
 	
 	if (Ext.isIE) {
		setTimeout(function(){
	    	newWind.show();   
	    	//newWind.maximize();                               
		}, 500);
		
		if(!exist) { 
	     	var wbody = Ext.getCmp(newWind.id);
	     	wbody.body.mask('Loading', 'x-mask-loading');
	      	setTimeout(function(){
	      		wbody.body.unmask();                                    
			}, 1000);
			
	     	newWind.addListener('resize',function(){
	        	var Obj=Ext.get("iframe-"+id);
	         	Obj.setHeight(newWind.getInnerHeight());
	         	Obj.setWidth(newWind.getInnerWidth())                
	  		});
		}
	} else {
		newWind.show();
		//newWind.maximize();
	}
}

function bb(obj){//解决动态绑定闭包问题要用到函数
 	this.clickFunc = function(){
   		menuOpen(obj);//调用相应的函数
	}
}

function menuOpen(obj) {
	if (obj.type == 1) {
		// 树形菜单
		var url = path + treePanel + "?type=" + obj.id + "&flag=" + obj.type;
		ShortCut("win-" + obj.id, obj.text, url);
	} else if (obj.type == 2) {
		// Tab形式
	} else if (obj.type == 3) {
		var url = obj.link;
		
		if (url) {
			url = path + url;
		}
		ShortCut("win-" + obj.id, obj.text, url);
	}
}

function createMenu(data) {
	var items = [];
	for (var i = 0; i < data.length; i++) {	
		var item = {};
		var tempData = data[i];
		
		item.text = tempData.text;
		item.iconCls = "bogus";
		
		var tp = new bb(tempData);
		item.handler = tp.clickFunc;
			
		//item.scope = obj;
		item.windowId = "tab-" + tempData.id;
		
		items.push(item);
	}
	
	return items;
}

function createLauncher() {
	var data = getMenuData();
	
	var items = [];
	
	if (data && data.length > 0) {
		for(var i = 0, leng = data.length; i < leng; i++) {
			var temp = data[i];
			
			var lanuer = {};
			lanuer.text = temp.text;
			lanuer.iconCls  = "bogus";
			//lanuer.scope = obj;
		
			if (temp.son) {
				var count = temp.son.length;
				if (count > 0) {
					lanuer.handler = function() {
						return false;
					};
					var menu_items = createMenu(temp.son);
					lanuer.menu = {items: menu_items};
				} else {
					var tp = new bb(temp);
					lanuer.handler = tp.clickFunc;
				}
			}
			
			items.push(lanuer);
		}
	}
	
	return items;
}

// 进入设置密码
function setPass() {
	var url = path + updatePass;
	ShortCut("win-password", "修改密码", url,400,200,false,false);
}

// 跳转到设置背景图片
function showBackGround() {
	var url = path + setBackGroundPath;
	ShortCut("dataview","设置背景", url ,580,350, false, false);
}

// 获取cookie中的背景图片
function setBackGround() {
	var backGround = GetCookie('BackGround');
	backGround = "/images/back.jpg";
	if (backGround) {
		setBackGroundToCookie(backGround);
	}
}

// 设置背景，并将地址保存到cookie中
function setBackGroundToCookie(name,obj) {
	
	// 设置到cookie
	var expdate = new Date();         
	//当前时间加上两周的时间         
	expdate.setTime(expdate.getTime() + 14 * (24 * 60 * 60 * 1000));         
	if (obj) {
		obj.SetCookie("BackGround", name, expdate);
		var url = parent.path + backGroundFile + name;
		parent.document.body.style.background = "#3d71b8 url("+url+") left top";
	} else {
		SetCookie("BackGround", name, expdate);
		var url = path + name;
		document.body.style.background = "#3d71b8 url("+url+") left top";
	}
}
setBackGround();