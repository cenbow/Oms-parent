function getById(id) {
	var value = Ext.fly(id).getValue().trim();
	Ext.getCmp(id).setValue(value);
	return value;
}

function isNum(n) {
	var re = /^[1-9]\d*$/;
	if (!re.exec(n)) {
		return false;
	}
	return true;
}
function numFixed(num, n){
	n = n || 2;
	var number = new Number(num);
	return number.toFixed(n);
}

function checkFormat(result) {
	var re = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	if(re.test(result)) {
		if (result == 0 || result == 0.0 || result == 0.00)
			return false;
		else
			return true;
	} else {
		return false;
	}
}

function checkAmount(result) {
	var re = /(^[1-9]\d*$)/;
	if(re.test(result)) {
		return true;
	} else {
		return false;
	}
}

// 获取金额显示格式
function getMoneyFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>￥"+getMoney(value)+"</div>";
	} else {
		return "";
	}
}

function getPercentFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>"+getMoney(value)+"%</div>";
	} else {
		return "";
	}
}

// 获取左对齐格式
function getLeftFormate(value) {
	return "<div style='text-align:left;'>"+value+"</div>";
}

// 获取右对齐格式
function getRightFormate(value) {
	return "<div style='text-align:right;'>"+value+"</div>";
}

// 获取字符日期
function getStringDate(value) {
	if (value) {
		value = value.substring(0, 19);
	}
	return value;
}

function formatDate(millisecond){
	if (millisecond) {
		var dt = new Date(millisecond);
		return dt.format('Y-m-d');
	}
}

function formatDateformate(millisecond, formate){
	if (millisecond) {
		var dt = new Date(millisecond);
		return dt.format(formate);
	}
}

// 获取2位小数点四舍五入
function changeTwoDecimal(x) {
	var f_x = parseFloat(x);
	if (isNaN(f_x)) {
		return 0.00;
	}
	var f_x = Math.round(x*100)/100;
	return f_x;
} 

function numFixed(num, n){
	n = n || 2;
	var number = new Number(num);
	return number.toFixed(n);
}

// 获取2位金额
function getMoney(value) {
	if (isNaN(value)) {
		return value;
	} else {
		var f = parseFloat(value);
		var f = numFixed(f, 2);
		return f;
	}
	/*var s = value.toString();
	
	var index = s.indexOf(".");
	
	if (index == -1) {
		if (s) {
			s =  s + ".00";
		} else {
			s = "0.00"
		}
	} else {
		var leng = s.substring(index + 1);
		
		if (leng.length == 1) {
			s = s + "0";
		} else {
			s = s.substring(0, index + 3);
		}
	}
	
	return s;*/
}

// 设置序号
var record_start = 0;

function getRecordStart() {
	return record_start;
}

function setRecordStart(start) {
	return record_start = start;
}

// 为了与后台的Page分页对象一致才设置
function setDefaultPage(start, limit, store) {
	setRecordStart(start);
	store.setBaseParam("start", start);
	store.setBaseParam("limit", limit);
	
	/*var pageNo = 1;
	if (start > 0) {
		pageNo = start / limit + 1;
	}
	
	store.setBaseParam("cp",pageNo);
	store.setBaseParam("pp",limit);
	store.setBaseParam("page.pageNo", pageNo);
	store.setBaseParam("page.pageSize", limit);*/
}

// 创建JsonStore
function createJsonStore(root, fields ,url) {
	var data = {};
	data.root = root;
	data.fields = fields;
	if (url) {
		data.url = url;
	}
	return new Ext.data.JsonStore(data);
}

// 创建ComboBox
function createComboBox(store, id, displayField, valueField, mode, fieldLabel, width, name, emptyText) {
	if (width == null || width == '') {
		width = 150;
	}
	return new Ext.form.ComboBox({
		store: store,
//		id: id,
		name: name,
		displayField: displayField,
		valueField: valueField, 
		queryMode: mode,
		width: width,
		triggerAction: 'all',
		selectOnFocus:true,
		allowBlank:true,
		forceSelection:false,
		fieldLabel: fieldLabel,
		hiddenName: name,
		emptyText: emptyText,
		lastQuery:''  ,
		editable: false // 不可输入
	});
}

//创建ComboBox(加载本地数据 Model为Local模式)

function createComboBoxLocal(store, id, displayField, valueField, mode, fieldLabel, width, labelWidth, name, emptyText) {
	
	if (width == null || width == '') {
		width = 150;
	}
	if (labelWidth == null || labelWidth == '') {
		labelWidth = 70;
	}
	if (mode == null || mode == '') {
		mode = 'local';
	}
	var selectWidth = 'width:' + (width-labelWidth-26);
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
		displayField: displayField,
		valueField: valueField, 
		queryMode: mode,
		width: width,
		//fieldStyle: 'width:100',
		fieldStyle: selectWidth,
		labelWidth: labelWidth,
		triggerAction: 'all',
		hideMode: 'offsets',
//		selectOnFocus:false,
//		allowBlank:true,
		forceSelection:true,
//		typeAhead: true,
		fieldLabel: fieldLabel,
		hiddenName: name,
		emptyText: emptyText,
		editable: false // 不可输入
	});
}

// 创建ComboBox
function createComboBoxNoHidden(store, id, displayField, valueField, mode, fieldLabel, width, name, emptyText) {
	var w = width ? width : 150;
	return new Ext.form.ComboBox({
		store: store,
	    id: id,
		name: name,
		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		width: w,
		triggerAction: 'all',
		selectOnFocus:true,
		allowBlank:true,
		forceSelection:true,
		fieldLabel: fieldLabel,
		emptyText: emptyText,
		editable: false // 不可输入
	});
}

//创建ComboBox
function createComboBox2(store, id, displayField, valueField, mode, fieldLabel, name, emptyText) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		triggerAction: 'all',
		selectOnFocus:true,
		allowBlank:true,
		forceSelection:true,
		fieldLabel: fieldLabel,
		hiddenName: name,
		emptyText: emptyText,
		editable: false // 不可输入
	});
}

// 创建分页
function createPagingToolbar(store, limit,bbarItems,iid) {
	return new Ext.PagingToolbar({
		id:iid,
		pageSize: limit,
		store: store,
		displayInfo: true,
		displayMsg: '显示 {0} - {1}条记录 共 {2}条',
		style:"padding:0 15px",
		emptyMsg: "没有记录可以显示",
		beforePageText:"页数",
		afterPageText : '共 {0}页',
		firstText : '第一页',
		prevText : '上一页',
		nextText : '下一页',
		lastText : '最后一页',
		refreshText : '刷新',
		items:bbarItems,
		doLoad : function(start){
 			record_start = start;
 			setDefaultPage(start, limit, store);
			store.load();
 		}
	});
}

// 创建分页
function createLocalPagingToolbar(store, limit) {
	return new Ext.PagingToolbar({
		pageSize: limit,
		store: store,
		displayInfo: true,
		displayMsg: '显示 {0} - {1}条记录 共 {2}条',
		style:"padding:0 15px",
		emptyMsg: "没有记录可以显示",
		beforePageText:"页数",
		afterPageText : '共 {0}页',
		firstText : '第一页',
		prevText : '上一页',
		nextText : '下一页',
		lastText : '最后一页',
		refreshText : '刷新',
		doLoad : function(start){
			setDefaultPage(start, limit, this.store);
 			record_start = start;
			getPageList(start);
 		},
		getParams : function(){
			return this.paramNames || this.store.baseParams;
		},
		onLoad:function (store,r,o)//重写OnLoad
		{
			if(!this.rendered){
				this.dsLoaded = [store, r, o];
				return;
			}
			var p = this.getParams();
			//this.cursor = (o.params && o.params[p.start]) ? o.params[p.start] : 0;
			this.cursor = p.start ? p.start : 0;
			var d = this.getPageData(), ap = d.activePage, ps = d.pages;
	
			this.afterTextItem.setText(String.format(this.afterPageText, d.pages));
			this.inputItem.setValue(ap);
			this.first.setDisabled(ap == 1);
			this.prev.setDisabled(ap == 1);
			this.next.setDisabled(ap == ps);
			this.last.setDisabled(ap == ps);
			this.refresh.enable();
			this.updateInfo();
			this.fireEvent('change', this, d);
		}
	});
}

// 创建ajax请求
function createAjax(url, successfun, failurefun, params, timeout) {
	if (!timeout) {
		// 默认超时10分钟
		timeout = 600000;
	}
	Ext.Ajax.request({
		url: url,
		waitMsg : '请稍等.....',
		timeout:timeout,
		success: function(response,opts){
			if (successfun) {
				successfun(response, opts);
			}
		},
		failure: function(response,opts){
			if (failurefun) {
				failurefun(response,opts);
			} else {
				createAlert("提示信息", "请求数据失败!");
			}
		},
		//scriptTag: true,
		params: params
	});
}

/**
 * id : id
 * title : 标题
 * url : 地址
 * width : 宽度
 * height : 高度
 */
function showWindow(id,title,url, w, h, x,y) {
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;

	return new Ext.Window( {
		id:id,
		title : title,
		width : w,
		height: h,
		x:x,
		y:y,
		iconCls: 'tabs',
		shim:false,
		animCollapse:false,
		modal : true,	 //window窗体后面变灰,不可编辑
		resizable:false,
		border:false,
		//maximizable:true,
		constrainHeader:true,
		layout: 'fit',
		html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	});
}

function showWindowUrl(url,id, title, x, y, w, h, button) {
	var data = {
		id:id,
		title: title,
		layout:'fit',
		plain: true,
		modal:true,
		resizable:false,
		closeAction: 'hide',
		buttonAlign:"center",
		buttons: button,
		html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	};
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;

	data.x = x;
	data.y = y;
	data.width = w;
	data.height = h;
	
	return new Ext.Window(data);
}

function showWindowForm(title, x, y, w, button) {
	w = w ? w : 800;
	//height = height ? height : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	return new Ext.Window( {
		title: title,
		x:x,
		y:y,
 		layout:'fit',
 	 	width:w,
 		autoHeight:true,
 	 	closeAction:'hide',
 		plain: true,
		modal:true,
		resizable:false,
		buttonAlign:"center",
 		buttons: button
	});
}

/**
 * 创建表格 附带分页条
 * id
 * title
 */
function createGridPanel(id, title, columns,store,limit, renderTo, sm, tb, tbar,bbarItems,iid) {
	return new Ext.grid.GridPanel(getGridPanelModel(id, title, columns, store,limit, renderTo, sm, tb, tbar, bbarItems, iid));
}

// 创建表格,传入对象
function createGridPanelByModel(model) {
	return new Ext.grid.GridPanel(model);
}

function getGridPanelModel(id, title, columns, store, limit, renderTo, sm, tb, tbar, bbarItems, iid) {
	var model = {
		id:id,
		title:title,
		height: 500,
		columns:columns,
		store:store,
		//loadMask: true,  //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false,  //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		viewConfig : {
			//forceFit:true,
			layout : function() {
				if (!this.mainBody) {
					return; // not rendered
				}
				var g = this.grid;
				var c = g.getGridEl();
				var csize = c.getSize(true);
				var vw = csize.width;
				if (!g.hideHeaders && (vw < 20 || csize.height < 20)) { 
					return;
				}
				if (g.autoHeight) {
					if (this.innerHd) {
						this.innerHd.style.width = (vw) + 'px';
					}	 
				} else {	 
					this.el.setSize(csize.width, csize.height);
					var hdHeight = this.mainHd.getHeight();
					var vh = csize.height - (hdHeight);
					this.scroller.setSize(vw, vh);
					if (this.innerHd) {
						this.innerHd.style.width = (vw) + 'px';
					}
				}
				if (this.forceFit) {
					if (this.lastViewWidth != vw) {
						this.fitColumns(false, false);
						this.lastViewWidth = vw;
					}
				} else {
					this.autoExpand();
					this.syncHeaderScroll();
				}
				this.onLayout(vw, vh);
			},
			getColumnStyle : function(colIndex, isHeader) {
				var colModel  = this.cm,
				colConfig = colModel.config,
				style = isHeader ? '' : colConfig[colIndex].css || '',
				align = colConfig[colIndex].align;
				if(Ext.isChrome){
					style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex))-2+'px');
				}else{
					style += String.format("width: {0};", this.getColumnWidth(colIndex));
				}
				if (colModel.isHidden(colIndex)) {
					style += 'display: none; ';
				}
				if (align) {
					style += String.format("text-align: {0};", align);
				}
				return style;
			}
		},
		renderTo: renderTo
	};
	if (sm) {
		model.sm = sm;
	}
	if (tb) {
		model.bbar = tb;
	} else {
		if (limit > 0 ) {
			model.bbar = createPagingToolbar(store, limit,bbarItems,iid);
		}
	}
	if (tbar) {
		model.tbar = tbar;
	}
	return model;
}
// grid默认选中
function onChecked(grid, store, sm) {
	grid.getStore().on('load', function(s, records) {
		for (var i = 0; i < store.getCount(); i++) {
			sm.selectRow(i, true);
		}
	});
}

// 创建点击不取消其他选择的checkbox
function createMoreCheckboxSelectionModel() {
	return new Ext.grid.CheckboxSelectionModel({
		handleMouseDown: function(g, rowIndex, e){
			if(e.button !== 0 || this.isLocked()){
				return;
			}
			var view = this.grid.getView();
			if(e.shiftKey && !this.singleSelect && this.last !== false){
				var last = this.last;
				this.selectRange(last, rowIndex, e.ctrlKey);
				this.last = last;
				view.focusRow(rowIndex);
			}
		}
	});
}

function createAlert(msg, message, fn) {
	Ext.MessageBox.alert(msg, message, fn);
}

//if (!Ext.grid.GridView.prototype.templates) {
//	Ext.grid.GridView.prototype.templates = {};
//}
//Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(
//	 '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,
//	 '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,
//	 '</td>'
//);

function createOpenUrl(id) {
	var url = "initExpressDetail?invoiceNo=" + id;
	// initExpressDetail "快件明细"
	var obj = parent.createNodeTab(id, id, url);
	parent.createMainTab(obj);
}

function createOpenUrlById(id, url) {
	url += id;
	var obj = parent.createNodeTab(id, id, url);
	parent.createMainTab(obj);
}

Array.prototype.contains = function(item){
	return RegExp(item).test(this); 
};

function createCode() {
	var code = "";
	var codeLength = 6;
	var selectChar = new Array(0,1,2,3,4,5,6,7,8,9,"A","B","C","D","E","F","G","H","I","G","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");

	for(var i = 0; i < codeLength; i++) {
		var charIndex = Math.floor(Math.random()*32);
		code += selectChar[charIndex];
	}
	if(code.length != codeLength)
		createCode();
	return code;
}


// 获取系统字典
function getSysCodeString(value, store) {
	var count = store.getCount();
	for (var i = 0; i < count; i++) {
		var record = store.getAt(i);
		if (record.get("codeCd") == value) {
			return record.get("codeDisplay");
		}
	}
	return value;
}

// 成员详细信息
function doShowUserInfo(userId) {
	var userName = escape(userId);
	var url = path + "/desktop/jsp/bgpay/user/userInfoTab.jsp?userId=" + userName;
	var obj = parent.createNodeTab("userInfoTab-" + userId, "会员信息-" + userId, url);
	if (parent) {
		parent.createMainTab(obj);
	} else {
		createMainTab(obj);
	}
}

// 提交form
function doSubmit(url, succFun) {
	var bsForm = selectform.getForm();
	bsForm.url = url;
	bsForm.submit({
		success: function(frm, action) {
			createAlert('信息提示', "操作成功!" );
			if (succFun) {
				succFun(frm, action);
			}
		},
		failure: function(frm, action) {
			var msg = "操作失败!";
			var result = action.result;
			if (result) {
				if (result.msg) {
					msg = result.msg;
				}
			}
			createAlert('信息提示', msg);
		}
	});
}

/**
 * 创建普通GridPanel对象
 * @param id
 * @param title
 * @param height
 * @param store
 * @param pageSize
 * @param renderTo
 * @param cm
 * @param sm
 * @param tbar
 * @param autoExpandCol
 * @returns {Ext.grid.GridPanel}
 */
function createISSGridPanel(id, title, height, store, pageSize, renderTo, columns, sm, tbar, autoExpandCol) {
	var model = {
		id: id,
		title: title,
//		forceFit: true,
		iconCls: 'grid',
		height: height ? height : '500px',
		frame : true,
		width:  '100%',
		autoWidth: true,
		store: store,
		loadMask: true, //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false, //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
//		disableSelection : true,
		frame : true,
		tbar: tbar,
		// grid columns
		columns: columns,
		selModel: sm
	};
	if (pageSize) {
		model.bbar = new MbPagingToolbar(store, pageSize);
	}
	if (autoExpandCol) {
		model.autoExpandColumn = autoExpandCol; //自动伸展，占满剩余区域
	}
	return new Ext.grid.Panel(model);
}

function createExt4GridPanel(id, title, height, store, pageSize, renderTo, cm, sm, tbar, autoExpandCol) {
	var model = {
		id: id,
		title: title,
		height: height ? height : '500px',
		store: store,
		loadMask: true, //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false, //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		disableSelection : true,
		frame : true,
		tbar: tbar,
		// grid columns
		cm : cm,
		sm: sm
	};
	if (pageSize) {
		model.bbar = new MbPagingToolbar(store, pageSize);
	}
	if (autoExpandCol) {
		model.autoExpandColumn = autoExpandCol; //自动伸展，占满剩余区域
	}
	return new Ext.grid.GridPanel (model);
}

/**
 * 创建ObjectGridStore
 * url
 * pageSize
 * record
 */
function createObjectGridStore(url, pageSize, record) {
	//加载列表数据
	var objectGridProxy = new Ext.data.HttpProxy( {
		url : url,
		type: 'ajax',
		timeout: 100000,   // 100seconds
		actionMethods: {
			read: 'POST'
		},
		reader: {
			root: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
	});
	var gridStore = new Ext.data.Store( {
		pageSize: 20,
		model: record ,
		remoteSort: true,
		autoLoad : {
			params : {
				start : 0,
				limit : pageSize
			}
		},
		proxy :  objectGridProxy
	});
	return gridStore;
}


/**
 * 创建ObjectGridStore 不自动加载数据
 * url
 * pageSize
 * record
 */
function createObjectGridStoreLazy(url, pageSize, record) {
	//加载列表数据
	var objectGridProxy = new Ext.data.HttpProxy( {
		url : url,
		type: 'ajax',
		timeout: 1800000,   // 100seconds
		actionMethods: {
			read: 'POST'
		},
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
	});
	var gridStore = new Ext.data.Store( {
		pageSize: 20,
		model: record ,
		remoteSort: true,
		autoLoad : false,
		proxy :  objectGridProxy
	});
	return gridStore;
}
/**
 * 创建普通FormPanel对象
 * @param id
 * @param url
 * @param layout
 * @param items
 * @param buttons
 * @returns {___anonymous18237_18468}
 */
function createIssFormPanelModel(id, url, layout, width, height, items, buttons, labelAlign, buttonAlign, searchPage) {
	var model ={
		id : id,
		frame : true,
//		bodyStyle : 'padding:5px 5px 0',
//		keys:{
//			key: Ext.EventObject.ENTER,
//			fn: function(btn,e){
////				if (searchPage) {
////					search();
////				}
//				search();
//			}
//		},
		width: width ? width : '100%',
		//height: height ? height : 150,
		autoHeight: true,
		url : url,
		labelAlign : labelAlign,
		items : items,
		buttons : buttons,
		buttonAlign: buttonAlign,
		listeners: {  
			afterRender: function(thisForm, options){  
				this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {  
					enter: function(){  
						alert('1111');
		            }
		            // scope: this  
		         });  
		     }  
		 }
	};
	if (layout || layout != 'form') {
		layout : layout;
	}
	return new Ext.form.Panel( model );
}

/** 窗口自适应 （只有一个Form、一个Grid）
 * formPanel
 * gridPanel
 * excessWidth 多余宽度
 * excessHeight 多余高度
 */
function setResize(formPanel,gridPanel,excessWidth, excessHeight) {
	var formHeight = formPanel.getHeight();
	var clientHeight = document.body.clientHeight;
	var clientWidth = document.body.clientWidth;
	gridPanel.setHeight(clientHeight-formHeight-excessHeight);
	gridPanel.setWidth('100%');
	formPanel.setWidth('100%');
}

/**
 * 普通更新一条记录（根据ID更新）
 * columnId
 * GridPanel
 * url
 */
function updateRecord(columnId, GridPanel, url, winId, title, width, height) {
	if (!url) {
		alert('请求路径为空！');
	}
	var selModel = GridPanel.getSelectionModel();
	if (selModel.hasSelection()) {
		var records = selModel.getSelections();
		if (records.length > 1) {
			createAlert('error','只能选择一条记录');
			return ;
		}
		var selectId = records[0].get(id);
		if(selectId != ""){
			 FormEditWin.showAddDirWin("popWins",url + "?id="+selectId, winId , title, width, height);
		}else{
			alertMsg("结果", "请选择需要查看的行！");
		}
	}
}

/**
 * 删除grid中选择的记录并刷新grid
 * @param columnId
 * @param url
 * @param gridPanel
 * @param refreshStore
 */
function deleteRecords(columnId, url, gridPanel, refreshStore) {
	var selModel = gridPanel.getSelectionModel();
	if (selModel.hasSelection()) {
		confirmMsg("确认","您确定要删除选择的记录吗?", function(btn) {
			if (btn == "yes") {
				var records = selModel.getSelections();
				var ids = [];
				for ( var i = 0; i < recs.length; i++) {
					ids.push(records[i].get(columnId));
				}
				if(ids != ""){//批量删除
					Ext.Ajax .request({
						waitMsg : '请稍等.....',
						url : url,
						method : 'post',
						params : {
							ids : ids
						},
						success : function(response) {
							var respText = Ext.util.JSON.decode(response.responseText);
							if(respText.success){ //成功
								Ext.Msg.alert('结果',respText.msg,function(xx){refreshStore.reload();});
							}else{
								errorMsg("结果", respText.msg);
							}
						},
						failure : function(response, options) {
							var respText = Ext.util.JSON.decode(response.responseText); 
							errorMsg("结果", respText.msg);
						}
					});
				}
			}
		});
	} else {
		alertMsg("错误", "请选择要批量删除的行!");
	}
}

/**
 * 对gridPanel中column 添加双击事件
 * @param gridPanel
 * @param rowIndex
 * @param e
 * @param url
 * @param title
 * @param params
 */
function addDblclickById(gridPanel ,rowIndex , e, url, title, params) {
	gridPanel.on('rowdblclick', function(gridPanel ,rowIndex ,e){
		var selectionModel = gridPanel.getSelectionModel();
		var record = selectionModel.getSelected();
		var id = record.data['id'];
		FormEditWin.showAddDirWin("popWins",url+id+"&ticketType=2","pop_message_winID", title, 860,560);
	});
}

/**
 * 查询数据
 * @param formPanel
 * @param gridPanel
 * @param params
 */
function searchData(formPanel, gridPanel, params) {
	var formValues=formPanel.getForm().getValues();//获取表单中的所有Name键/值对对象
	
	//gridPanel.store.baseParams = formValues;
	var extend=function(o,n,override){
		for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
	};
	extend(params,formValues);
	gridPanel.store.load({params : params});
}

/**
 * form表单重置
 * @param formPanel
 */
function resetButton(formPanel) {
	formPanel.form.reset();
}

//分页工具条
var MbPagingToolbar = function(store, pageSize) {
	MbPagingToolbar.superclass.constructor.call(this, {
		store: store,
		pageSize:pageSize,
		displayInfo: true,
		displayMsg: "当前显示从{0}条到{1}条,共{2}条",
		emptyMsg: ""
		//emptyMsg: "<span style='color:red;font-style:italic;'>对不起,没有找到数据</span>"
	});
};

Ext.extend(MbPagingToolbar, Ext.PagingToolbar, {
});

function createAjaxData(id,url, successfun, failurefun, params, timeout) {
	if (!timeout) {
		// 默认超时10分钟
		timeout = 600000;
	}
	Ext.Ajax.request({
		url: url,
		method : 'POST',
		waitMsg : '请稍等.....',
		timeout:timeout,
		success: function(response,opts){
			if (successfun) {
				successfun(id,response, opts);
			}
		},
		failure: function(response,opts){
			if (failurefun) {
				failurefun(id,response,opts);
			} else {
				createAlert("提示信息", "请求数据失败!");
			}
		},
		//scriptTag: true,
		params: params
	});
}

/**
*  格式化price
*/
function fmoney(s, n)  {  
	   n = n > 0 && n <= 20 ? n : 2;  
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
	   var l = s.split(".")[0].split("").reverse(),  
	   r = s.split(".")[1];  
	   t = "";  
	   for(i = 0; i < l.length; i ++ )  
	   {  
	      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
	   }  
	   return t.split("").reverse().join("") + "." + r;  
}  


function checkFormatInput(result) {
	var re = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	if(re.test(result)) {
		if (result == 0 || result == 0.0 || result == 0.00)
			return true;
		else
			return true;
	} else {
		return false;
	}
}

/**
 * 详情页button 可操作状态
 * @param status
 * @returns {Boolean}
 */
function ButtonDis(status) {
	if (status == '0' || status == false) {
		return true;
	} else {
		return false;
	}
}


/**
 * 创建普通GridPanel对象
 * @param id
 * @param title
 * @param height
 * @param store
 * @param pageSize
 * @param renderTo
 * @param cm
 * @param sm
 * @param tbar
 * @param autoExpandCol
 * @param layout
 * @param region
 * @returns {Ext.grid.GridPanel}
 */
function createISSGridPanelByLayout(id, title, height, store, pageSize, renderTo, columns, sm, tbar, autoExpandCol,layout,region,hidden) {
	var model = {
		id: id,
		title: title,
//		forceFit: true,
		iconCls: 'grid',
		height: height ? height : '500px',
		frame : true,
		width:  '100%',
		hidden:hidden,
		autoWidth: true,
		resizable:false,
		scroll:false, 
		viewConfig: {
			style: { overflow: 'auto', overflowX: 'hidden' }
		},
		store: store,
		loadMask: true, //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false, //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
//		disableSelection : true,
		frame : true,
		tbar: tbar,
		region: region,
		layout: layout,
		// grid columns
		columns: columns,
		selModel: sm
	};
	if (pageSize) {
		model.bbar = new MbPagingToolbar(store, pageSize);
	}
	if (autoExpandCol) {
		model.autoExpandColumn = autoExpandCol; //自动伸展，占满剩余区域
	}
	return new Ext.grid.Panel(model);
}

/**
 * 
 * @param id
 * @param title
 * @param height
 * @param store
 * @param pageSize
 * @param renderTo
 * @param columns
 * @param sm
 * @param dItems
 * @param hidden
 * @returns {Ext.grid.Panel}
 */
function createGridPanelByExt4(id, title, height, store, pageSize, renderTo, columns, sm, dItems, hidden) {
	var model = {
			id:id,
			store: store,
			columns : columns,
			autoRender:true,
			columnLines: true,
			width: '100%',
			height: height ? height : '500px',
			loadMask: true, //读取数据时的遮罩和提示功能
			frame: true
	};
	if (title) {
		model.title = title;
		model.iconCls='icon-grid';
	}
	if (dItems) {
		// inline buttons
		model.dockedItems=dItems;
	}
	if (pageSize) {
		model.bbar = new MbPagingToolbar(store, pageSize);
	}
	if (sm) {
		model.selModel=sm;
	}
	if (renderTo) {
		model.renderTo = renderTo;
	}
	if (hidden) {
		model.hidden = hidden;
	}
	var viewConfig = {enableTextSelection: true};
	model.viewConfig = viewConfig;
	return new Ext.grid.Panel(model);
}


function createPanel(layout,orderQuestionGridPanel,shortageQuestionGridPanel){
	
	var mainPanel = new Ext.panel.Panel( {

		layout:layout,
		items : [
		        orderQuestionGridPanel,
		         shortageQuestionGridPanel
		 ]
	});
	
	return mainPanel;
}


function createAjaxDataBySyn(id,url, successfun, failurefun, params, timeout) {
	if (!timeout) {
		// 默认超时10分钟
		timeout = 600000;
	}
	Ext.Ajax.request({
		url: url,
		method : 'POST',
		waitMsg : '请稍等.....',
		timeout:timeout,
		async : false,
		success: function(response,opts){
			if (successfun) {
				successfun(id,response, opts);
			}
		},
		failure: function(response,opts){
			if (failurefun) {
				failurefun(id,response,opts);
			} else {
				createAlert("提示信息", "请求数据失败!");
			}
		},
		//scriptTag: true,
		params: params
	});
}

//var isSelectAll = null;
function createComboBoxLocalByMult(store, id, displayField, valueField, mode, fieldLabel, width, labelWidth, name, emptyText) {
	
	if (width == null || width == '') {
		width = 150;
	}
	if (labelWidth == null || labelWidth == '') {
		labelWidth = 70;
	}
	if (mode == null || mode == '') {
		mode = 'local';
	}
	var selectWidth = 'width:' + (width-labelWidth-26);
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
		displayField: displayField,
		valueField: valueField, 
		queryMode: mode,
		width: width,
		//fieldStyle: 'width:100',
		fieldStyle: selectWidth,
		labelWidth: labelWidth,
		triggerAction: 'all',
		hideMode: 'offsets',
//		selectOnFocus:false,
//		allowBlank:true,
//		forceSelection:false,
//		typeAhead: true,
		fieldLabel: fieldLabel,
		hiddenName: name,
		emptyText: emptyText,
		multiSelect : true,
		editable: false ,// 不可输入
		listConfig: {  
			getInnerTpl: function() {
				return'<input type=checkbox id="{channelCode}" name="" value="{channelCode}">{channelName}';  
			},
			listeners:{
				itemclick:function(view, record, item, index, e, eOpts ){
					var isSelected = view.isSelected(item);  
					var checkboxs = item.getElementsByTagName("input");
				
					if(checkboxs!=null) {
						var checkbox = checkboxs[0];
						
						if(!isSelected) {
							checkbox.checked = true;  
						}else{
							checkbox.checked = false;  
						}
					}
				}
			}
		}
	});
}


function createObjectGridStoreBySort(url, pageSize, record,prop) {
	//加载列表数据
	var objectGridProxy = new Ext.data.HttpProxy( {
		url : url,
		type: 'ajax',
		actionMethods: {  
            read: 'POST'  
        }, 
		reader: {
			root: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
	});
	var gridStore = new Ext.data.Store( {
		pageSize: 20,
		model: record ,
		remoteSort: true,
		autoLoad : {
			params : {
				start : 0,
				limit : pageSize
			}
		},
		/* sorters: [{
             //排序字段。
             property: prop,
             //排序类型，默认为 ASC 
             direction: 'desc'
         }],*/
		 fields:[  
	                {name:"addTime"}], 
		proxy :  objectGridProxy
	});
	return gridStore;
}


/**
 * 获取FormPanel中查询参数
 * @param formPanel
 * @param initParams
 * @returns {Object}
 */
function getFormParams(formPanel, initParams) {
	var formValues = formPanel.getForm().getValues();//获取表单中的所有Name键/值对对象
	if (initParams == undefined || initParams == null) {
		return formValues;
	} else {
		var extend=function(o,n,override){
			for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
		};
		extend(initParams, formValues);
		return initParams;
	}
}

//prompt窗口
function promptMsg(title, msg, callBack){
//	Ext.Msg.confirm(title, msg, callBack);
	Ext.MessageBox.prompt(title, msg, callBack);
}