<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
<head>
<title>短信发送工具</title>
<style type="text/css">
body {
	background: none;
}
</style>
<script type="text/javascript">
	var basePath = '<%=basePath%>';
</script>
<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/commonModel.js"></script>
<script type="text/javascript" src="<%=basePath%>/mbapp/page/toolPage/sendMessageTool.js"></script>
</head>
<body>
</body>
</html>