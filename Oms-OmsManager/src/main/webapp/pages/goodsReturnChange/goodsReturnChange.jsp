<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
	<head>
		<title>退换货申请页面</title>
		<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
		<script type="text/javascript" src="<%=basePath%>/pages/goodsReturnChange/goodsReturnChange.js"></script>
	</head>
<body>
	<input type="hidden" id="goodsReturnChangePageOrderSn" name="" value="${orderSn}"/>
	<input type="hidden" id="goodsReturnChangePageIsHistory" name="" value="${isHistory}"/>
	<input type="hidden" id="goodsReturnChangePageGoodsReturnChangeId" name="" value="${id}"/>
</body>
</html>

	<script type="text/javascript">
		var orderSn = $("#goodsReturnChangePageOrderSn").val();		
		var isHistory = $("#goodsReturnChangePageIsHistory").val();
		var goodsReturnChangeId = $("#goodsReturnChangePageGoodsReturnChangeId").val(); //申请单ID
	</script>
