//Ext.namespace("ISS");//命名空间
//Ext.BLANK_IMAGE_URL = 'js/ext3.4/resources/images/default/s.gif';
Ext.QuickTips.init();

// 扩展窗体
FormEditWin = function(){
	var curFormWin = null;
	return {
		width : 600,
		height : 400,
		//添加弹出窗体 -  url链接地址,winID窗口ID,title窗口标题
		showAddDirWin: function(parentNode,url,winID,title,widthV,heightV){
			var win=this.createWin(winID,title,url,function(){
				parentNode.reload();} ,widthV,heightV);
			cnode=parentNode;
			win.show();
		},
		showAddDirWinS: function(parentNode,url,winID,title,widthV,heightV){
			var win=this.createWinS(winID,title,url,function(){
				parentNode.reload();} ,widthV,heightV);
			cnode=parentNode;
			win.show();
		},
		showWin: function(parentNode,url,winID,title,widthV,heightV,scrolling){
			var win=this.createShowWin(winID,title,url,function(){
				parentNode.reload();} ,widthV,heightV,scrolling);
			cnode=parentNode;
			win.show();
		},
		showOrderWinS: function(winID,url,widthV,heightV) {
			var win = this.createOrderWinS(winID,url,widthV,heightV);
			win.show();
		},
		newWin: function(url,winID,title,widthV,heightV){
			var wins=this.createWin(winID,title,url,function(){ } ,widthV,heightV);
			wins.show();
		},
		newWinS:function(url,winID,title,widthV,heightV){
			var wins=this.createWinS(winID, title, url, function(){} ,widthV,heightV);
			wins.show();
		},
		createWin : function(winId, winTitle, iframePage, closeFun,widthV,heightV) {
			// 供各类型窗口创建时调用
			var win = Ext.getCmp(winId);
			if (!win) {
				win = new Ext.Window({
					id : winId,
					title :winTitle,
					width : widthV,
					height : heightV,
				//	maximizable : true,
					modal : true,
					html : "<iframe width='100%' height='100%' name='"+winId+"' frameborder='0' scrolling='auto' src='"
							+ iframePage + "'></iframe>"
				});
				this.reloadNavNode = closeFun;
			}
			curFormWin = win;
			return win;
		},
		createWinS: function(winId, winTitle, iframePage, closeFun, widthV, heightV) {
			// 供各类型窗口创建时调用
			var win = Ext.getCmp(winId);
			if (!win) {
				win = new Ext.Window({
					id : winId,
					title :winTitle,
					width : widthV,
					height : heightV,
				//	maximizable : true,
					modal : true,
					html : "<iframe width='100%' height='100%'   name='"+winId+"'  frameborder='0' scrolling='no' src='"
							+ iframePage + "'></iframe>"
				});
				this.reloadNavNode = closeFun;
			}
			curFormWin = win;
			return win;
		},
		createShowWin : function(winId, winTitle, iframePage, closeFun,widthV,heightV,scrolling) {
			// 供各类型窗口创建时调用
			var win = Ext.getCmp(winId);
			if (!win) {
				win = new Ext.Window({
					id : winId,
					title :winTitle,
					width : widthV,
					height : heightV,
				//	maximizable : true,
					modal : true,
					html : "<iframe width='100%' height='100%' name='"+winId+"' onunload='nodeReload();'  frameborder='0' scrolling='"+ scrolling+"' src='"
							+ iframePage + "'></iframe>"
				});
				this.reloadNavNode = closeFun;
			}
			curFormWin = win;
			return win;
		},
		createOrderWinS: function(winId, iframePage, widthV,heightV) {
			// 供各类型窗口创建时调用
			var win = Ext.getCmp(winId);
			if (!win) {
				win = new Ext.Window({
					id : winId,
					//title :winTitle,
					width : widthV,
					height : heightV,
					modal : true,
					html : "<iframe width='100%' height='100%'   name='"+winId+"'  frameborder='0' scrolling='no' src='"
							+ iframePage + "'></iframe>"
				});
				this.reloadNavNode = function(){};
			}
			curFormWin = win;
			return win;
		},
		reloadNavNode : function() {
		},
		close : function() {
			if(curFormWin){
				curFormWin.close();
			}
		},
		reload:function(){
			//cnode.reload();
			//alert(this.currNode);
			//this.currNode.reload();
		}
	};
}();

//分页工具条
var IssPagingToolbar=function(issStore, pageSize) {
	IssPagingToolbar.superclass.constructor.call(this, {
		store: issStore,
		pageSize:pageSize,
		displayInfo: true,
		displayMsg: "当前显示从{0}条到{1}条,共{2}条",
		emptyMsg: ""
		//emptyMsg: "<span style='color:red;font-style:italic;'>对不起,没有找到数据</span>"
	}); 
};

Ext.extend(IssPagingToolbar, Ext.PagingToolbar, {

});

//////////////////消息类型///////////////////////////
var sessionTimeout = "登录超时，请重新登录!";//Session Timeout! Please re-sign in!
function logout(){
	var redirect = "page/user/userLogout.ct";
	if(window.parent){
		window.parent.location.href = redirect;
	} else {
		window.location.href = redirect;
	}
}
//aler窗口
function alertMsg(title, msg){
	if(msg && msg == ''){
		logout();
	} else {
		Ext.Msg.alert(title, msg);
	}
}
function succMsg(title, msg, extGrid){
	if(msg && msg == sessionTimeout){
		logout();
	} else {
		Ext.Msg.alert(title, msg, function(){
			parent.parent.FormEditWin.close();
			extGrid.store.reload();//刷新当前页， 页数不变，即不回到第一页。
		});
	}
}

//confirm窗口
function confirmMsg(title, msg, callBack){
	Ext.Msg.confirm(title, msg, callBack);
}

//warning
function warnMsg(title, msg){
	if(msg && msg == sessionTimeout){
		logout();
	} else {
		Ext.MessageBox.show({
			title: title,
			msg: msg,
			buttons: Ext.MessageBox.OK,
			//fn: showResultMsg,
			icon: Ext.MessageBox.WARNING
		});
	}
}

//error
function errorMsg(title, msg){
	if(msg && msg == sessionTimeout){
		logout();
	} else {
		Ext.MessageBox.show({
			title: title,
			msg: msg,
			buttons: Ext.MessageBox.OK,
			//fn: showResultMsg,
			icon: Ext.MessageBox.ERROR
		});
	}
}

//返回成功的提示信息。
function showResultMsg(title, msg){
	   Ext.example.msg(title, msg);
};
	
//来自extjs 例子中的Ext.example
Ext.example = function(){
	var msgCt = null;
	function createBox(t, s){
		return ['<div class="msg">',
				'<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
				'<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
				'<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
				'</div>'].join('');
	}
	return {
		msg : function(title, format){
			if(!msgCt){
				msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
			}
			msgCt.alignTo(document, 't-t');
			var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
			var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
			m.slideIn('t').pause(1).ghost("t", {remove:true});
		},
		init : function(){
			/*
			var t = Ext.get('exttheme');
			if(!t){ // run locally?
				return;
			}
			var theme = Cookies.get('exttheme') || 'aero';
			if(theme){
				t.dom.value = theme;
				Ext.getBody().addClass('x-'+theme);
			}
			t.on('change', function(){
				Cookies.set('exttheme', t.getValue());
				setTimeout(function(){
					window.location.reload();
				}, 250);
			});*/

			var lb = Ext.get('lib-bar');
			if(lb){
				lb.show();
			}
		}
	};
}();

//Ext.example.shortBogusMarkup = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.';
//Ext.example.bogusMarkup = '<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus tincidunt diam nec urna. Curabitur velit.</p>';

Ext.onReady(Ext.example.init, Ext.example);

function getValueById(id) {
	var obj = Ext.getCmp(id);
	if (obj == null) {
		return null;
	}
	return obj.getValue();
}

Date.prototype.Format = function(fmt)
{ //author: meizz
	var o = {
	"M+" : this.getMonth()+1,				//月份   
	"d+" : this.getDate(),					//日   
	"h+" : this.getHours(),					//小时   
	"m+" : this.getMinutes(),				//分   
	"s+" : this.getSeconds(),				//秒   
	"q+" : Math.floor((this.getMonth()+3)/3),//季度   
	"S"  : this.getMilliseconds()			//毫秒 
	};
  if(/(y+)/.test(fmt))
	fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)
	if(new RegExp("("+ k +")").test(fmt))
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
  return fmt;
};

var positive_regex = /^(([1-9]+[0-9]*.{1}[0-9]+)|([0].{1}[1-9]+[0-9]*)|([1-9][0-9]*)|([0][.][0-9]+[1-9]*))$/;
var integer_regex = /^[0-9]*$/;