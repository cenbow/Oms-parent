<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<html>
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
	<head>
		<title>订单结算查询</title>
	</head>
	<script type="text/javascript">
		var display = "1";
		var isHistory = 0;
		//var order_info_url = basePath + "/custom/orderInfo/orderInfoDetail?orderSn=";
		//var batchConfirmAuth = ButtonDis("${batchConfirm}");
	</script>
	<script type="text/javascript" src="<%=basePath%>/js/commonModel.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
	<body>
	
		<!-- 调整单号 -->
		<input type="hidden" id="billNo" name="billNo" value="${billNo}"/>
		
		<!-- 是否置灰 -->
		<input type="hidden" id="isDisabled" name="isDisabled" value="${isDisabled}"/>
		
		<!--二级  -->
		<input type="hidden" id="channelCode" name="channelCode" value="${channelCode}"/> 
		
		<!-- 三级 -->
		<input type="hidden" id="shopCode" name="shopCode" value="${shopCode}"/>
		
		<!-- 一级 -->
		<input type="hidden" id="orderFromFirst" name="orderFromFirst" value="${orderFromFirst}"/>
		
	</body>

	<script type="text/javascript" src="<%=basePath%>/pages/orderTicket/addOrderTicket.js"></script>
</html>