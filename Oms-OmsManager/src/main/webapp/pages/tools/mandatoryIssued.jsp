<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<html>
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
	<head>
		<title>强制下发</title>
	</head>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
	<script type="text/javascript" src="<%=basePath%>/js/commonModel.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"></script>
	<script type="text/javascript">
		var display = "1";
		var isHistory = 0;
		//var order_info_url = basePath + "/custom/orderInfo/orderInfoDetail?orderSn=";
	//	var batchConfirmAuth = ButtonDis("${batchConfirm}");
		
	</script>
	<body>
		<!--批量锁定 -->
	<%-- <input type="hidden" id="batchLock" name="batchLock" value="${batchLock}"/> --%>
		<!--批量解锁 -->
	<%-- <input type="hidden" id="batchUnlock" name="batchUnlock" value="${batchUnlock}"/> --%>
		<!--批量确定 -->
	<%-- <input type="hidden" id="batchConfirm" name="batchConfirm" value="${batchConfirm}"/> --%>
		
		<!--  待结算订单列表类型  -->
	<%-- <input type="hidden" id="settleType" name="settleType" value="${settleType}"/> --%>
	</body>
	</body>
	<script type="text/javascript" src="<%=basePath%>/pages/tools/mandatoryIssued.js"></script>
</html>