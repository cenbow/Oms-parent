<%@page import="org.apache.commons.collections.CollectionUtils"%>
<%@page import="com.work.shop.oms.bean.SystemOmsResource"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>

<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.gzjs"></script>
<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>


<html>
	<head>
		<title>换单页面</title>
	<style type="text/css">
	body{background:none;}
	.even { background-color: #CCFFFF; }  
	.odd { background-color: #FFFFFF; }
	.tableSty{
		border: solid 1px #c8c8c8;
		border-collapse: collapse;
		font-family: \'宋体\',\'Arial\';
		font-size: 12px;
		background: #fff;
		color: #000;
		width: 100%;
	}
	</style>
	
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
	<script type="text/javascript">
	function numFixed(num, n){
		n = n || 2;
		var number = new Number(num);
		return number.toFixed(n);
	}

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
</script>
	<script type="text/javascript">
	
		var basePath = '<%=basePath%>';
		var clientHeight ;
		var returnType=1;
		var orderSn = "${orderSn}";
		var returnSn = "${returnSn}";
		var masterOrderSn = "${masterOrderSn}"; 
		var channelCode = "${channelCode}";
		var exchangeOrderSn = "${exchangeOrderSn}";
		var isHistory = "${isHistory}";
		var userId = "${userId}";
		/** 编辑商品类型 0：添加订单添加商品;1：修改订单商品（未下发）;2：修改订单商品（已下发）;3： 换货订单编辑商品 **/
		var editGoodsType = 3;
		var auth = new Object();
		var resource = 1;
		var rolePrefix = "exchange_list_";
		 <%
			String pageNo = "exchange_list";
			HttpSession Session = request.getSession();
			List<SystemOmsResource> resources = (List<SystemOmsResource>)Session.getAttribute(pageNo);
			if(CollectionUtils.isNotEmpty(resources)){
				for (SystemOmsResource resource : resources) {
					String resourceName = resource.getResourceName();
			%>
				var param = '<%=resource.getResourceCode()%>';
				auth[param] = true;
			<%
				}
			}
		%>
		console.dir(auth);
		var sonOrderListData = null;
	</script>
	<%-- <script type="text/javascript" src="<%=basePath%>/mbapp/redirectExchange.js"></script> --%>
	<script type="text/javascript" src="<%=basePath%>/mbapp/jsPages/orderExchangePage.js"></script>
	</head>
<body>
</body>
</html>