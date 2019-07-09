Ext.define('MB.controller.HandOrderController', {
	extend : 'Ext.app.Controller',
	stores : [
	          'MB.store.HandOrderStore',
	          'MB.store.HandOrderProStatusStore',
	          'MB.store.CreateOrderStatusStore',
	          'MB.store.OnlineChannelShopStore',
	          'MB.store.HandOrderSourTypeStore'
	          ],
	models : [
	          'MB.model.HandOrderModel',
	          'MB.model.ComboModel',
	          'MB.model.OnlineChannelShopModel'
	          ],
	views : ['MB.view.handOrder.HandOrderPanlView',
	         'MB.view.handOrder.HandOrderGridView',
	         'MB.view.handOrder.ProcessStatusCombo',
	         'MB.view.handOrder.SourceTypeCombo',
	         'MB.view.handOrder.CreateOrderStatusCombo',
	         'MB.view.handOrder.OnlineChannelShopCombo',
	         'MB.view.handOrder.HandOrder',
	         'MB.view.handOrder.HandOrderListWin',
	         'MB.view.handOrder.HandOrderAddWin',
	         'MB.view.handOrder.HandOrderEditWin'
		     ],
	refs : [{
				ref : 'handOrderGridView',
				selector : 'handOrderGridView'
			},{
				ref : 'handOrderListWin',
				selector : 'handOrderListWin'
			},{
				ref : 'handOrder',
				selector : 'handOrder'
			},{
				ref : 'HandOrderAddWin',
				selector : 'HandOrderAddWin'
			},{
				ref : 'HandOrderEditWin',
				selector : 'HandOrderEditWin'
			}],
	init : function() {
		var me = this;
		me.control({
			'handOrderPanlView button[action=reset]':{
				click : this.reset
			},
			'handOrderPanlView button[action=search]':{
				click : this.search
			},'handOrderPanlView button[action=openImport]':{
				click : this.openImport
			},'handOrderListWin button[action=closeImport]':{
				click : this.closeImport
			},'handOrderListWin button[action=submitImport]':{
				click : this.submitImport
			},'handOrder button[action=closeImport]':{
				click : this.closeImport
			},'handOrder button[action=doImport]':{
				click : this.doImport
			},'HandOrderAddWin button[action=save]':{
				click : this.handAdd
			},'HandOrderAddWin button[action=close]':{
				click : this.closeImport
			},'HandOrderEditWin button[action=close]':{
				click : this.closeImport
			},'HandOrderEditWin button[action=save]':{
				click : this.editSave
			}
		});
	},
	reset: function (btn) {
		var form = btn.up('form');
		form.reset();
	},
	search: function(btn){
		var pageSize = 20;
		var form = btn.up('form');//当前按钮form
		var searchParams = getFormParams(form, null);
		console.dir(searchParams);
		var handOrderGridView = form.up('viewport').down("handOrderGridView");
		handOrderGridView.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		handOrderGridView.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		handOrderGridView.store.pageSize = pageSize;
		handOrderGridView.store.load({params : searchParams});
	},
	openImport: function(btn){
		var win = Ext.widget("handOrderListWin");
		win.show();	
	},
	closeImport: function(btn){
		var win = btn.up("window");
		win.close();
	},
	doImport: function(btn){
		var win = btn.up("window");
		var form = win.down("form");
		//非空判断
		var channelCode = form.getForm().findField('channelCode').getValue();
		var channelName = form.getForm().findField('channelCode').getRawValue();
		var myfile = form.getForm().findField('myfile').getValue();
		if(channelCode==null||channelCode==''){
			Ext.Msg.alert('提示', '请选择线上店铺！');
			form.getForm().findField('channelCode').focus();
			return;
		}
		var sourceType = form.getForm().findField('sourceType').getValue();
		if(sourceType==null||sourceType=='' || sourceType==-1){
			Ext.Msg.alert('提示', '请选择打单类型！');
			form.getForm().findField('sourceType').focus();
			return;
		}
		if(myfile==null||myfile==''){
			Ext.Msg.alert('提示', '请选择数据文件！');
			form.getForm().findField('myfile').focus();
			return;
		}else if(myfile.lastIndexOf(".csv") == -1){
			Ext.Msg.alert('提示', '选择文件不是[.csv]格式文件！');
			form.getForm().findField('myfile').focus();
			return;
		}
		//获取参数
		var formValues=form.getForm().getValues();
		formValues['channelName'] = channelName;
		//生成调整单
		form.submit( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/handOrder/doImport.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(form,action) {
				win.close();
				result = action.result;
				var batchList = result.batchList;
				console.dir(batchList);
				var store = Ext.getCmp('hardOrderListGrid').store;
				store.add(batchList);
			},
			failure : function(form,action) {
				Ext.Msg.alert('执行结果', '导入失败！');
			}
		});
	},
	handAdd:function(btn){
		var win = btn.up("window");
		var form = win.down("form");
		//获取页面所有值
		var orderId = form.getForm().findField('orderId').getValue();
		var skuId = form.getForm().findField('skuId').getValue();
		var iid = form.getForm().findField('iid').getValue();
		var outerId = form.getForm().findField('outerId').getValue();
		var title = form.getForm().findField('title').getValue();
		var goodsSn = form.getForm().findField('goodsSn').getValue();
		var price = form.getForm().findField('price').getValue();
		var num = form.getForm().findField('num').getValue();
		var extensionCode = form.getForm().findField('extensionCode').getValue();
		var province = form.getForm().findField('province').getValue();
		var city = form.getForm().findField('city').getValue();
		var county = form.getForm().findField('county').getValue();
		var district = form.getForm().findField('district').getValue();
		var address = form.getForm().findField('address').getValue();
		var goodsNum = form.getForm().findField('goodsNum').getValue();
		var orderStatus = form.getForm().findField('orderStatus').getValue();
		var payStatus = form.getForm().findField('payStatus').getValue();
		var totalFee = form.getForm().findField('totalFee').getValue();
		var shippingFee = form.getForm().findField('shippingFee').getValue();
		var payment = form.getForm().findField('payment').getValue();
		var invoiceType = form.getForm().findField('invoiceType').getValue();
		var invoiceName = form.getForm().findField('invoiceName').getValue();
		var sellerRemark = form.getForm().findField('sellerRemark').getValue();
		var remark = form.getForm().findField('remark').getValue();
		var receiverName = form.getForm().findField('receiverName').getValue();
		var receiverPhone = form.getForm().findField('receiverPhone').getValue();
		var receiverAddress = form.getForm().findField('receiverAddress').getValue();
		var createTime = form.getForm().findField('createTime').getValue();
		var payTime = form.getForm().findField('payTime').getValue();
		var modifiedTime = form.getForm().findField('modifiedTime').getValue();
		var addTime = form.getForm().findField('addTime').getValue();
		
		var channelCode = form.getForm().findField('channelCode').getValue();
		var channelName = form.getForm().findField('channelCode').getRawValue();
		var flag = form.getForm().findField('flag').getValue();
		var note = form.getForm().findField('note').getValue();
		var errorMsg = form.getForm().findField('errorMsg').getValue();
		//检查必填项
		if(orderId==''||orderId==null){
			Ext.Msg.alert('提示', '订单编号不允许为空！');
			form.getForm().findField('orderId').focus();
			return;
		}
		if(skuId==''||skuId==null){
			Ext.Msg.alert('提示', '平台商品码不允许为空！');
			form.getForm().findField('skuId').focus();
			return;
		}
		if(outerId==''||outerId==null){
			Ext.Msg.alert('提示', '11位商品码不允许为空！');
			form.getForm().findField('outerId').focus();
			return;
		}else if(outerId.length!=11){
			Ext.Msg.alert('提示', '11位商品码位数有误！');
			form.getForm().findField('outerId').focus();
			return;
		}
		if(title==''||title==null){
			Ext.Msg.alert('提示', '商品名称不允许为空！');
			form.getForm().findField('title').focus();
			return;
		}
		if(price==''||price==null){
			if(price!='0'){
				Ext.Msg.alert('提示', '商品成交价格不允许为空！');
				form.getForm().findField('price').focus();
				return;
			}
		}
		if(num==''||num==null){
			if(num!='0'){
				Ext.Msg.alert('提示', '商品数量不允许为空！');
				form.getForm().findField('num').focus();
				return;
			}
		}
		if(province==''||province==null){
			Ext.Msg.alert('提示', '地区省不允许为空！');
			form.getForm().findField('province').focus();
			return;
		}
		if(city==''||city==null){
			Ext.Msg.alert('提示', '地区市不允许为空！');
			form.getForm().findField('city').focus();
			return;
		}
		if(county==''||county==null){
			Ext.Msg.alert('提示', '地区区不允许为空！');
			form.getForm().findField('county').focus();
			return;
		}
		if(address==''||address==null){
			Ext.Msg.alert('提示', '地区详细地址不允许为空！');
			form.getForm().findField('address').focus();
			return;
		}
		if(goodsNum==''||goodsNum==null){
			if(goodsNum!='0'){
				Ext.Msg.alert('提示', '订单商品数量不允许为空！');
				form.getForm().findField('goodsNum').focus();
				return;
			}
		}
		if(shippingFee==''||shippingFee==null){
			if(shippingFee!='0'){
				Ext.Msg.alert('提示', '运费不允许为空！');
				form.getForm().findField('shippingFee').focus();
				return;
			}
		}
		if(payment==''||payment==null){
			if(payment!='0'){
				Ext.Msg.alert('提示', '订单支付金额不允许为空！');
				form.getForm().findField('payment').focus();
				return;
			}
		}
		if(receiverName==''||receiverName==null){
			Ext.Msg.alert('提示', '收货人 不允许为空！');
			form.getForm().findField('receiverName').focus();
			return;
		}
		if(receiverPhone==''||receiverPhone==null){
			Ext.Msg.alert('提示', '收货人电话不允许为空！');
			form.getForm().findField('receiverPhone').focus();
			return;
		}
		if(createTime==''||createTime==null){
			Ext.Msg.alert('提示', '订单创建时间不允许为空！');
			form.getForm().findField('createTime').focus();
			return;
		}
		if(payTime==''||payTime==null){
			Ext.Msg.alert('提示', '订单支付时间不允许为空！');
			form.getForm().findField('payTime').focus();
			return;
		}
		if(channelCode==''||channelCode==null){
			Ext.Msg.alert('提示', '线上店铺不允许为空！');
			form.getForm().findField('channelCode').focus();
			return;
		}
		if(goodsSn.length>0&&goodsSn.length!=6){
			Ext.Msg.alert('提示', '6位商品码位数有误！');
			form.getForm().findField('goodsSn').focus();
			return;
		}
		if(extensionCode==''||extensionCode==null){
			Ext.Msg.alert('提示', '商品扩展属性不允许为空！');
			form.getForm().findField('extensionCode').focus();
			return;
		}
		//把数据添加到第三方订单列表store中
		var data = [[flag,errorMsg,orderId,skuId,iid,outerId,title,goodsSn,price,num,extensionCode,province,city,county,district,address,goodsNum,orderStatus,payStatus,totalFee,shippingFee,payment,invoiceType,invoiceName,sellerRemark,remark,receiverName,receiverPhone,receiverAddress,createTime,payTime,modifiedTime,addTime,note,channelCode,channelName]];
		Ext.getCmp("hardOrderListGrid").store.insert(0,data);
		win.close();
		Ext.getCmp('hardOrderListGrid').getView().refresh();
	},
	editSave:function(btn){
		var win = btn.up("window");
		var form = win.down("form");
		//获取页面所有值
		var orderId = form.getForm().findField('orderId').getValue();
		var skuId = form.getForm().findField('skuId').getValue();
		var iid = form.getForm().findField('iid').getValue();
		var outerId = form.getForm().findField('outerId').getValue();
		var title = form.getForm().findField('title').getValue();
		var goodsSn = form.getForm().findField('goodsSn').getValue();
		var price = form.getForm().findField('price').getValue();
		var num = form.getForm().findField('num').getValue();
		var extensionCode = form.getForm().findField('extensionCode').getValue();
		var province = form.getForm().findField('province').getValue();
		var city = form.getForm().findField('city').getValue();
		var county = form.getForm().findField('county').getValue();
		var district = form.getForm().findField('district').getValue();
		var address = form.getForm().findField('address').getValue();
		var goodsNum = form.getForm().findField('goodsNum').getValue();
		var orderStatus = form.getForm().findField('orderStatus').getValue();
		var payStatus = form.getForm().findField('payStatus').getValue();
		var totalFee = form.getForm().findField('totalFee').getValue();
		var shippingFee = form.getForm().findField('shippingFee').getValue();
		var payment = form.getForm().findField('payment').getValue();
		var invoiceType = form.getForm().findField('invoiceType').getValue();
		var invoiceName = form.getForm().findField('invoiceName').getValue();
		var sellerRemark = form.getForm().findField('sellerRemark').getValue();
		var remark = form.getForm().findField('remark').getValue();
		var receiverName = form.getForm().findField('receiverName').getValue();
		var receiverPhone = form.getForm().findField('receiverPhone').getValue();
		var receiverAddress = form.getForm().findField('receiverAddress').getValue();
		var createTime = form.getForm().findField('createTime').getValue();
		var payTime = form.getForm().findField('payTime').getValue();
		var modifiedTime = form.getForm().findField('modifiedTime').getValue();
		var addTime = form.getForm().findField('addTime').getValue();
		
		var channelCode = form.getForm().findField('channelCode').getValue();
		var channelName = form.getForm().findField('channelCode').getRawValue();
		var flag = form.getForm().findField('flag').getValue();
		var note = form.getForm().findField('note').getValue();
		var errorMsg = form.getForm().findField('errorMsg').getValue();
		//检查必填项
		if(orderId==''||orderId==null){
			Ext.Msg.alert('提示', '订单编号不允许为空！');
			form.getForm().findField('orderId').focus();
			return;
		}
		if(skuId==''||skuId==null){
			Ext.Msg.alert('提示', '平台商品码不允许为空！');
			form.getForm().findField('skuId').focus();
			return;
		}
		if(outerId==''||outerId==null){
			Ext.Msg.alert('提示', '11位商品码不允许为空！');
			form.getForm().findField('outerId').focus();
			return;
		}else if(outerId.length!=11){
			Ext.Msg.alert('提示', '11位商品码位数有误！');
			form.getForm().findField('outerId').focus();
			return;
		}
		if(title==''||title==null){
			Ext.Msg.alert('提示', '商品名称不允许为空！');
			form.getForm().findField('title').focus();
			return;
		}
		if(price==''||price==null){
			if(price!='0'){
				Ext.Msg.alert('提示', '商品成交价格不允许为空！');
				form.getForm().findField('price').focus();
				return;
			}
		}
		if(num==''||num==null){
			if(num!='0'){
				Ext.Msg.alert('提示', '商品数量不允许为空！');
				form.getForm().findField('num').focus();
				return;
			}
		}
		if(province==''||province==null){
			Ext.Msg.alert('提示', '地区省不允许为空！');
			form.getForm().findField('province').focus();
			return;
		}
		if(city==''||city==null){
			Ext.Msg.alert('提示', '地区市不允许为空！');
			form.getForm().findField('city').focus();
			return;
		}
		if(county==''||county==null){
			Ext.Msg.alert('提示', '地区区不允许为空！');
			form.getForm().findField('county').focus();
			return;
		}
		if(address==''||address==null){
			Ext.Msg.alert('提示', '地区详细地址不允许为空！');
			form.getForm().findField('address').focus();
			return;
		}
		if(goodsNum==''||goodsNum==null){
			if(goodsNum!='0'){
				Ext.Msg.alert('提示', '订单商品数量不允许为空！');
				form.getForm().findField('goodsNum').focus();
				return;
			}
		}
		if(shippingFee==''||shippingFee==null){
			if(shippingFee!='0'){
				Ext.Msg.alert('提示', '运费不允许为空！');
				form.getForm().findField('shippingFee').focus();
				return;
			}
		}
		if(payment==''||payment==null){
			if(payment!='0'){
				Ext.Msg.alert('提示', '订单支付金额不允许为空！');
				form.getForm().findField('payment').focus();
				return;
			}
		}
		if(receiverName==''||receiverName==null){
			Ext.Msg.alert('提示', '收货人 不允许为空！');
			form.getForm().findField('receiverName').focus();
			return;
		}
		if(receiverPhone==''||receiverPhone==null){
			Ext.Msg.alert('提示', '收货人电话不允许为空！');
			form.getForm().findField('receiverPhone').focus();
			return;
		}
		if(createTime==''||createTime==null){
			Ext.Msg.alert('提示', '订单创建时间不允许为空！');
			form.getForm().findField('createTime').focus();
			return;
		}
		if(payTime==''||payTime==null){
			Ext.Msg.alert('提示', '订单支付时间不允许为空！');
			form.getForm().findField('payTime').focus();
			return;
		}
		if(channelCode==''||channelCode==null){
			Ext.Msg.alert('提示', '线上店铺不允许为空！');
			form.getForm().findField('channelCode').focus();
			return;
		}
		if(goodsSn.length>0&&goodsSn.length!=6){
			Ext.Msg.alert('提示', '6位商品码位数有误！');
			form.getForm().findField('goodsSn').focus();
			return;
		}
		if(extensionCode==''||extensionCode==null){
			Ext.Msg.alert('提示', '商品属性不允许为空！');
			form.getForm().findField('extensionCode').focus();
			return;
		}
		//获取record回写到store
		var record = Ext.getCmp("hardOrderListGrid").getSelectionModel().getSelection();//获取选中行
		if(record.length>0){
			record = record[0];
			record.set("orderId",orderId);
			record.set("skuId",skuId);
			record.set("iid",iid);
			record.set("outerId",outerId);
			record.set("title",title);
			record.set("goodsSn",goodsSn);
			record.set("price",price);
			record.set("num",num);
			record.set("extensionCode",extensionCode);
			record.set("province",province);
			record.set("city",city);
			record.set("county",county);
			record.set("district",district);
			record.set("address",address);
			record.set("goodsNum",goodsNum);
			record.set("orderStatus",orderStatus);
			record.set("payStatus",payStatus);
			record.set("totalFee",totalFee);
			record.set("shippingFee",shippingFee);
			record.set("payment",payment);
			record.set("invoiceType",invoiceType);
			record.set("invoiceName",invoiceName);
			record.set("sellerRemark",sellerRemark);
			record.set("remark",remark);
			record.set("receiverName",receiverName);
			record.set("receiverPhone",receiverPhone);
			record.set("receiverAddress",receiverAddress);
			record.set("createTime",createTime);
			record.set("payTime",payTime);
			record.set("modifiedTime",modifiedTime);
			record.set("addTime",addTime);
			record.set("flag",'0');
			record.set("errorMsg",'');
			record.set("note",note);
			record.set("channelCode",channelCode);
			record.set("channelName",channelName);
			win.close();
		    Ext.getCmp('hardOrderListGrid').getView().refresh();
		}
	},
	submitImport:function(btn){
		var win = btn.up("window");
		var form = win.down("form");
		//拼装参数
		var params = '[';
		var store = Ext.getCmp('hardOrderListGrid').store;
		store.each(function(record, i){
			params+='{';
			params+='"orderId":"'+record.get("orderId")+'",';
			params+='"flag":"'+record.get("flag")+'",';
			params+='"errorMsg":"'+record.get("errorMsg")+'",';
			params+='"userId":"'+record.get("userId")+'",';
			params+='"goodsInfo":"'+record.get("goodsInfo")+'",';
			params+='"masterOrderSn":"'+record.get("masterOrderSn")+'",';
			params+='"createTime":"'+record.get("createTime")+'",';
			params+='"processMessage":"'+record.get("processMessage")+'",';
			params+='"channelCode":"'+record.get("channelCode")+'",';
			params+='"channelName":"'+record.get("channelName")+'",';
			params+='"shippingAddress":"'+record.get("shippingAddress")+'",';
			params+='"sourceType":"'+record.get("sourceType")+'"';
			params+='},';
		});
		params = params.substring(0,params.length-1);
		params+=']';
		//提交导入数据
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + 'custom/handOrder/submitImport.spmvc',
			method : 'post',
			timeout:600000,
			params : {'paramsJson':params},
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.code=='0'){
					Ext.Msg.alert('执行结果', respText.msg);
					Ext.getCmp("handOrderGridView").store.reload();//刷新查询列表数据
					win.close();
				}else{
					var newStore = Ext.getCmp("hardOrderListGrid").store;
					newStore.removeAll();
					newStore.add(respText.batchList);
					Ext.getCmp('hardOrderListGrid').getView().refresh();
					Ext.Msg.alert('执行结果', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('执行结果', respText.msg);
			}
		});
	}
	
});