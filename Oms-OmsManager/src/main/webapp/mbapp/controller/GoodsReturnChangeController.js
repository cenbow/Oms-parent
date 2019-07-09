Ext.define('MB.controller.GoodsReturnChangeController', {
	extend : 'Ext.app.Controller',
	stores : [/*'OrderActions',*/'GoodsReturnChangeStatusStore','GoodsReturnChangeGoodsList'],
	models : ['OrderDetailModel','OrderAction','CommonStatusModel','GoodDetailModel','PayDetailModel','GoodsReturnChangeGoodsList'],
	views : [/*'goodsReturnChange.GoodsReturnChangeWest',*/'goodsReturnChange.GoodsReturnChangeCenter','goodsReturnChange.GoodsReturnChangeSouth',
			 'orderDetail.OrderSetModule','goodsReturnChange.GoodsReturnChangeSetModule','common.GoodsReturnChangeStatus',
			 'orderDetail.GoodSetModule','orderDetail.GoodDetail','orderDetail.PaySetModule','orderDetail.PayDetail',
			 'goodsReturnChange.GoodsSetModule','goodsReturnChange.GoodsList','MB.view.goodsReturnChange.GoodsReturnChangePicSetModule'
	],
	init : function() {
		var me = this;
		me.control({
			'refundContent button[action=searchAction]' : {
				click : this.search
			},
			'refundContent button[action=exportOrderRefund]':{
				click:this.exportOrderRefund
			},
			'goodsReturnChangeCenter button[action=uploadImage]':{
				click:this.uploadImage
			},
			'goodsReturnChangeCenter button[action=resetImage]':{
				click:this.resetImage
			},
		});
	},
	onLaunch : function() {
		var form = Ext.getCmp('goodsReturnChangeCenter');
		form.initData();
		var me = this;
		me.expandImageList();
		//退换单日志延迟加载
		var goodsReturnChangeSouth = Ext.ComponentQuery.query("goodsReturnChangeSouth")[0];
		goodsReturnChangeSouth.collapse();
		goodsReturnChangeSouth.addListener('expand',function(){
			goodsReturnChangeSouth.store.on('beforeload', function (store){
				params = {
						"orderSn" : orderSn,
						"isHistory" : isHistory
				};
				Ext.apply(store.proxy.extraParams, params);
			});
			goodsReturnChangeSouth.getStore().load();
		});
		
	},
	search:function(){
		Ext.getCmp('refundContent').search();
	},
	exportOrderRefund:function(){
		Ext.getCmp('refundContent').exportOrderRefund();
	}, 
	//图片上传
	uploadImage : function(btn){
		if(goodsReturnChangeId == ''){//申请号id
			Ext.msgBox.remainMsg('上传图片', "请将申请单保存后上传图片操作", Ext.MessageBox.ERROR);
			return;
		}
		var me = this;
		var imagePanel = btn.up('goodsReturnChangePicSetModule');
		var goodsReturnChangeImage = imagePanel.getForm().findField('returnChangeImage').getValue();
		//判断是否图片格式
		var rightTail = ".jpeg|.gif|.jpg|.png|.bmp|.pic|";
		var p = goodsReturnChangeImage.lastIndexOf(".");
		var tail = goodsReturnChangeImage.substring(p,goodsReturnChangeImage.length)+"|";
		tail = tail.toLowerCase();
		if(!(rightTail.indexOf(tail)>-1)){
			Ext.msgBox.remainMsg("警告", "仅支持.jpeg/.gif/.jpg/.png/.bmp/.pic格式文件", Ext.MessageBox.INFO);
			return;
		}
		if(goodsReturnChangeImage == ''){
			Ext.msgBox.remainMsg("警告", "请选择退货图片信息上传", Ext.MessageBox.INFO);
			return;
		}
		imagePanel.getForm().submit({
			url : basePath + '/custom/goodsReturnChange/uploadGoodsReturnChangeImage',
			params : {
				"goodsReturnChangeId" : goodsReturnChangeId
			},
			waitMsg: 'Uploading your photo...',
			success: function(fp, action) {
				if (action.result.success) {
					Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
					me.loadImageList();
				} else {
					Ext.msgBox.remainMsg('上传图片', action.result.errorMessage, Ext.MessageBox.ERROR);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('上传图片', action.result.errorMessage, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		});
	},
	//重置上传图片
	resetImage : function(btn){
		Ext.getCmp('goodsReturnChangeCenter').down('goodsReturnChangePicSetModule').getForm().findField('returnChangeImage').reset();
	},
	//展开Panel 加载图片数据
	expandImageList:function(){
		//展开原订单信息，后台发送请求
		var imageObj = Ext.getCmp('goodsReturnChangeCenter').down('goodsReturnChangePicSetModule');
		var me = this;
		imageObj.collapse();
		imageObj.addListener('expand',function(){
			me.loadImageList();
		});
	},
	//从后台获取图片数据
	loadImageList:function(){
		//展开原订单信息，后台发送请求
		var imageObj = Ext.getCmp('goodsReturnChangeCenter').down('goodsReturnChangePicSetModule');
		var me = this;
		var json = {
			url : basePath + '/custom/goodsReturnChange/getReturnChangeImageList',
			params : {
				"orderSn" : orderSn
			},
			waitMsg: 'loading image...',
			success : function(formPanel, action) {
				var returnChangeImageList = action.result.returnChangeImageList;
				var length = returnChangeImageList.length;
				console.dir(returnChangeImageList);
				console.dir(length);
				var imageList = '';
				if(length > 0){
					for(var i=0;i<length;i++){
						var url = returnChangeImageList[i];
						imageList += "<img src='"+url+"' onclick='showBigPic(this)' class='returnImage' />";
						if((i +1) % 6 == 0){
							imageList += "<br />";
						}
					}
					imageObj.down('panel').update(imageList);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('加载图片异常', action.result.errorMessage, Ext.MessageBox.ERROR);
			}
		};
		imageObj.getForm().submit(json);
		//设置滚动条 滑动到底
		/*var goodsReturnChangeSetModule = Ext.getCmp('goodsReturnChangeSetModule').body.dom;//退换货申请信息
	 	var orderSetModule = Ext.getCmp('orderSetModule').body.dom;//原订单信息
	 	var goodSetModule = Ext.getCmp('goodSetModule').body.dom;//商品信息
	 	var paySetModule = Ext.getCmp('paySetModule').body.dom;//付款信息
	 	var goodsReturnChangeGoodsSetModule = Ext.getCmp('goodsReturnChangeGoodsSetModule').body.dom;//退货商品信息
		var goodsReturnChangeCenter=Ext.getCmp('goodsReturnChangeCenter').body.dom;//主要展示区域对象（滚动条所在对象）
	 	var scrollTop=goodsReturnChangeSetModule.scrollHeight+50+orderSetModule.scrollHeight+40+goodSetModule.scrollHeight+40
	 				+paySetModule.scrollHeight+40+goodsReturnChangeGoodsSetModule.scrollHeight+200;
	 	goodsReturnChangeCenter.scrollTop = scrollTop;*/
	}
});
