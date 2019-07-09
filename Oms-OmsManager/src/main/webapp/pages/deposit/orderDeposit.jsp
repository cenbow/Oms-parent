<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<html>
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
	<head>
		<title>保证金调整单</title>
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
	<%-- 	<!--批量锁定 -->
		<input type="hidden" id="batchLock" name="batchLock" value="${batchLock}"/>
		<!--批量解锁 -->
		<input type="hidden" id="batchUnlock" name="batchUnlock" value="${batchUnlock}"/>
		<!--批量确定 -->
		<input type="hidden" id="batchConfirm" name="batchConfirm" value="${batchConfirm}"/> --%>
	</body>
	
	<!-- D:\chenrui\workspace\Update_OmsManager\src\main\webapp\pages\orderTicket\orderTicket.js -->
	
	<script type="text/javascript" src="<%=basePath%>/pages/deposit/orderDeposit.js"></script>
</html>