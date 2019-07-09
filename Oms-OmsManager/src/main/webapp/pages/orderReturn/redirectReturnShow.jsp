<%@page import="com.work.shop.oms.bean.SystemOmsResource"%>
<%@page import="java.util.List"%>
<%@page import="com.work.shop.oms.common.utils.ConfigCenter"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>

<html>
<head>
<title>退单</title>
<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.gzjs"></script>
<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript">
	Ext.Loader.setConfig({ enabled: true, disableCaching: false });
	Ext.BLANK_IMAGE_URL='ext5.1/resources/images/s.gif';
	var basePath = '<%=basePath%>';
	var order_info_url = '<%=basePath%>' + 'custom/orderInfo/orderDetail';
	
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
	var order_return_url ='<%=basePath%>' + 'custom/orderReturn/orderReturnPage?returnSn=';
	
	var relatingOrderSn = "${relatingOrderSn}";
	var returnSn = "${returnSn}";
	var returnType = "${returnType}";
	var masterOrderSn = "${masterOrderSn}";
	var siteCode = "${siteCode}";
	var auth = new Object();
	var rolePrefix = "return_info_";
	var changePaymentNumFlag=true;
	var editPaymentNumFlag=true;
	var returnPayEditFlag=true;
   <%
   		String pageNo = "return_list";
		HttpSession Session = request.getSession();
		List<SystemOmsResource> resources = (List<SystemOmsResource>)Session.getAttribute(pageNo);
		for (SystemOmsResource resource : resources) {
			String resourceName = resource.getResourceName();
	%>
		var param = '<%=resource.getResourceCode()%>';
		auth[param] = true;
	<%
		}
	%>
</script>
<style>
.icon-grid {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/grid.png) !important;
}

.add {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/add.gif) !important;
}

.option {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/plugin.gif) !important;
}

.remove {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif) !important;
}

.save {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/save.gif) !important;
}

.delete {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif) !important;
}

.edit {
	background-image: url(<%=basePath%>/ext5.1/shared/icons/fam/user_edit.png) !important;
}

.x-grid-rowbody p {
	margin: .5em 0;
}

.returnImage {
	width: 150px;
	height: 160px;
	padding: 5px;
}

.delIcon{
	padding: 5px;
}
</style>
</head>
<body>
</body>
<script type="text/javascript">
var clientWidth = document.body.clientWidth;
function numFixed(num, n) {
	n = n || 2;
	var number = new Number(num);
	return number.toFixed(n);
}

function delReturnImg(url){
	if(url){
		var array = url.split("|");
		var param = {'url':array[0],'returnSn':array[1]};
		Ext.Ajax.request({
			url : basePath + '/custom/orderReturn/delReturnImg',
			params : param,
			timeout : 90000,
			data : "json",
			success : function(response) {
				var result = Ext.JSON.decode(response.responseText);
				console.dir(result);
				if (result.code=='1') {
					Ext.getCmp('returnShow').loadImageList();
				}else{
					Ext.msgBox.remainMsg("结果", result.msg,Ext.MessageBox.ERROR);
				}
			},
			failure : function(response) {
				var result = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg("结果", result.msg,Ext.MessageBox.ERROR);
			}
		});
	}else{
		Ext.msgBox.remainMsg('错误', '获取图片地址异常！',Ext.MessageBox.ERROR);
	}
} 
</script>
<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>
<script type="text/javascript" src="<%=basePath%>/mbapp/jsPages/redirectReturnShow.js"></script>
</html>