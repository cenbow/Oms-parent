<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
	<head>
		<title>退单结算页面</title>
		<script type="text/javascript">
			var relatingOrderSn = "";
			var returnSn = "";
			var returnType = "";
			var basePath="<%=basePath%>";
		</script>
		<script type="text/javascript" src="<%=basePath%>/mbapp/refundList.js"></script>
	</head>
<body>
</body>
</html>