<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>


<html>
	<head>
	
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/ext5.1/resources/css/custom-styles.css" />
	<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>

		<title>订单列表页面</title>
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

	<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script> 
	<script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/common.js"></script>
	<script type="text/javascript">
	
	var display = "1";
	var isHistory = 0;
	
	var basePath = '<%=basePath%>';
	
	var batchConfirmAuth = ButtonDis("${batchConfirm}");
	
	//var batchConfirmAuth = false;

	</script>
	</head>
	<body>
	
			<!--批量锁定 -->
			<input type="hidden" id="batchLock" name="batchLock" value="${batchLock}"/>
			<!--批量解锁 -->
			<input type="hidden" id="batchUnlock" name="batchUnlock" value="${batchUnlock}"/>
			<!--批量确定 -->
			<input type="hidden" id="batchConfirm" name="batchConfirm" value="${batchConfirm}"/>
			
			<!--  待结算订单列表类型  -->
			<input type="hidden" id="settleType" name="settleType" value="${settleType}"/>
	</body>
		<script type="text/javascript" src="<%=basePath%>/pages/orderInfoList/orderInfoListPage.js"></script>

<script>


  var order_info_url = '<%=basePath%>' + '/custom/orderInfo/orderDetail?masterOrderSn=';

  var settleType = $("#settleType").val();


</script>

</html>