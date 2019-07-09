<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>

<html>
	<head>
		<title>退单列表页面</title>
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
	<script type="text/javascript">
	
	var display = "1";
	var isHistory = 0;
	
	var basePath = '<%=basePath%>';

	</script>
		
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/ext5.1/resources/css/custom-styles.css" />
	<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.js"></script>
	<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>
	<script type="text/javascript" src="<%=basePath%>/pages/orderReturnList/orderReturnListPage.js"></script>
		
	<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script> 
	<script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/common.js"></script>
		
	</head>
	<body>
		<!-- 退单列表中订单显示  -->
		<input type="hidden" id="display" name="display" value="${display}"/>
		<!-- 退单列表中退单详情显示  -->
		<input type="hidden" id="orderReturnDisplay" name="orderReturnDisplay" value="${orderReturnDisplay}"/>
		<!--  待结算退单列表  -->
		<input type="hidden" id="settleType" name="settleType" value="${settleType}"/>
	
	</body>
	<script>
	

	  var order_info_url = '<%=basePath%>' + '/custom/orderInfo/orderDetail?masterOrderSn=';
	
	var order_return_url ='<%=basePath%>' + 'custom/orderReturn/orderReturnPage?returnSn=';
	
		 var settleType = $("#settleType").val();

	</script>

</html>