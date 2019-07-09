<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
	<head>
		<title>统一资源页面</title>
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
	var basePath = '<%=basePath%>';
	</script>
	<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
	<script type="text/javascript" src="<%=basePath%>/mbapp/jsPages/systemResourcePage.js"></script>
	</head>
<body>
</body>
	<script type="text/javascript">
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	</script>
</html>