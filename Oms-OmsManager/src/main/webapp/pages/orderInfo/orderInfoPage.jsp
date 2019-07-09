<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>

<html>
<head>
<title>订单</title>
<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.gzjs"></script>
<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript">
	Ext.Loader.setConfig({ enabled: true, disableCaching: false });
	Ext.BLANK_IMAGE_URL='ext5.1/resources/images/s.gif';
	var basePath = '<%=basePath%>';
	var order_info_url = basePath + 'custom/orderInfo/orderDetail?masterOrderSn=';
	var editColor = "#FFFF00;";//编辑区域颜色统一设置
	Ext.msgBox = function(){
	    var msgCt;
	    function createBox(t, s){
	       return '<div class="msg ' + Ext.baseCSSPrefix + 'border-box"><h3>' + t + '</h3><p>' + s + '</p></div>';
	    }
	    return {
	        msg : function(title, format) {
	        },
	        remainMsg : function(title, msg, icon) {
	            Ext.MessageBox.show({
	                title: title,
	                msg: msg,
	                buttons: Ext.MessageBox.OK,
	                icon: icon
	            });
	        }
	    };
	}();
	var order_return_url = basePath + 'custom/orderReturn/orderReturnPage?returnSn=';
	var pageNo = "order_list";
	var masterOrderSn = "${masterOrderSn}";
	var initOrderSn = "${initOrderSn}";//用于交货单初始显示  初始后置空
	var deliveryOrderSn = "";//用于记录编辑交货单商品时的交货单号
	var orderSnForOperate = "";//用于记录操作订单详情页交货单下方按钮时的交货单号
	var sonOrderEditSn = "";//用于记录编辑交货单商品时  搜索的交货单号
	var channelCode = "";
	var depotCode = "";
	var invoiceNo = "";
	/** 编辑商品类型 0：添加订单添加商品;1：修改订单商品（未下发）;2：修改订单商品（已下发）;3： 换货订单编辑商品 **/
	var editGoodsType = 1;
	var isHistory = "${isHistory}";
	var activityType = "";
	var doERP = "";
	var typeItems = new Array();
	var desc = "";
	var userId = "";
	var transType = "";
	var orderType = "";//订单类型 0，正常订单 1，补发订单 2，换货订单
	var relatingReturnSn = "";//换货单关联退单编号
	var resource = 0;
	var sonOrderListData = null;//用于保存交货单/子单列表数据
	var editDeliveryParams = "";//修改承运商入参
	var queryExpressParams = "";//查询物流信息入参
	var isVerifyStock = "1";//是否验证库存,默认1验证，0表示不验证
	var siteCode = "";
	var depotCode = "";
</script>
<style>
.icon-grid {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/grid.png)
		!important;
}

.add {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/add.gif)
		!important;
}

.option {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/plugin.gif)
		!important;
}

.remove {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif)
		!important;
}

.save {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/save.gif)
		!important;
}

.delete {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif)
		!important;
}

.edit {
	background-image:
		url(<%=basePath%>/ext5.1/shared/icons/fam/user_edit.png) !important;
}

.x-grid-rowbody p {
	margin: .5em 0;
}

.returnImage {
	width: 150px;
	height: 160px;
	padding: 5px;
}
</style>
</head>
<body>
</body>
<script>
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	function showCouponInfo(cardNo,cardLn,cardMoney,status,effectDateStr,expireTimeStr){
		//打折券详情窗口
		var win =  Ext.create('Ext.Window',{
			title:"打折券信息",   
		       width:500,       
		       height:180,         
		       layout : 'fit',
		       modal:true,
		       items:[{
					xtype: 'form',
					margin: 10,
					border: false,
					frame: true,
					defaults: {
						labelWidth: 200,
						columnWidth: 1
					},
					items : [{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [{
							xtype : "displayfield", 
							name : 'cardNo', 
							fieldLabel : "卡号", 
							columnWidth: .5
						},{
							xtype : "displayfield", 
							name : 'cardMoney', 
							fieldLabel : "折扣", 
							columnWidth: .5,
							renderer : function (value) {
								var returnValue = '';
								if(value&&value!=null&&value!=''){
									returnValue = value+'折券';
								}
								return returnValue;
							}
						}]
					},{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [{
							xtype : "displayfield", 
							name : 'cardLn', 
							fieldLabel : "批次", 
							columnWidth: .5
						},{
							xtype : "displayfield", 
							name : 'status', 
							fieldLabel : "状态", 
							columnWidth: .5,
							renderer : function (value) { 
								var returnValue = '';
								if(value=='-1'){
									returnValue = '未启用';
								}else if(value=='0'){
									returnValue = '已领用';
								}else if(value=='1'){
									returnValue = '已激活';
								}else if(value=='2'){
									returnValue = '已充值';
								}else if(value=='3'){
									returnValue = '已作废';
								}else if(value=='4'){
									returnValue = '已使用';
								}
								return returnValue;
							}
						}]
					},{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [{
							xtype : "displayfield", 
							name : 'effectDateStr', 
							fieldLabel : "卡券生效日期", 
							columnWidth: .5
						},{
							xtype : "displayfield", 
							name : 'expireTimeStr', 
							fieldLabel : "卡券过期日期", 
							columnWidth: .5
						}]
					}]
		       }],
		       buttons : [{
		    	   text: "关闭",
		    	   handler: function (btn) {
		    		   var win = btn.up("window");
		    		   win.close();
		    	   }
		       }]
		});
		//赋值
		var form = win.down('form').getForm();
		form.findField('cardNo').setValue(cardNo);
		form.findField('cardLn').setValue(cardLn);
		form.findField('cardMoney').setValue(cardMoney);
		form.findField('status').setValue(status);
		form.findField('effectDateStr').setValue(effectDateStr);
		form.findField('expireTimeStr').setValue(expireTimeStr);
		//展示
		win.show();
	}


	function numFixed(num, n){
		n = n || 2;
		var number = new Number(num);
		return number.toFixed(n);
	}
</script>
<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>
<script type="text/javascript" src="<%=basePath%>/mbapp/jsPages/orderInfoPage.js"></script>
</html>