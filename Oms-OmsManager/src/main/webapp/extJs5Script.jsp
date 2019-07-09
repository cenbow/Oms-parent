<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="com.work.shop.oms.common.utils.ConfigCenter"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/ext5.1/resources/css/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/ext5.1/resources/css/custom-styles.css" />
<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />

<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.js"></script>

<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript" src="<%=basePath%>/ext5.1/msg-box.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/js/iss.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath%>/ext5.1/options-toolbar.js"></script> --%>

<%-- <script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script > --%>
<script type="text/javascript" src="<%=basePath%>/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"></script> --%>

<script type="text/javascript">
Ext.Loader.setConfig({ enabled: true, disableCaching: false }); 
<%
	String order_manager_path = ConfigCenter.getProperty("order_manager_url");
%>
	Ext.BLANK_IMAGE_URL='ext5.1/resources/images/s.gif';
	var basePath = '<%=basePath%>';

	var order_info_url = '<%=basePath%>' + 'custom/orderInfo/orderDetail?masterOrderSn=';
	//http://localhost:8080/OmsManager/custom/orderInfo/orderDetail?orderSn=140429767144&isHistory=0
	<%-- var order_info_url = '<%=order_manager_path%>' + 'manager/orderInfo?sn='; --%>
	<%-- var order_return_url = '<%=order_manager_path%>' + '/manager/returnOrderInfo?rsn='; --%>
	var order_return_url ='<%=basePath%>' + 'custom/orderReturn/orderReturnPage?returnSn=';
	var order_return_pay_url='<%=order_manager_path%>'+'jsp/manager/order/orderRefundInfo.jsp?sn=';
	var editColor = "#FFFF00;";//编辑区域颜色统一设置
</script>

<style>
.icon-grid {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/grid.png) !important;
}
.add {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/add.gif) !important;
}
.option {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/plugin.gif) !important;
}
.remove {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif) !important;
}
.save {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/save.gif) !important;
}
.delete {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/delete.gif) !important;
}
.edit {
	background-image:url(<%=basePath%>/ext5.1/shared/icons/fam/user_edit.png) !important;
}
.x-grid-rowbody p {
	margin: .5em 0;
}
.returnImage {
	width:150px;
	height:160px;
	padding:5px;
}
</style>