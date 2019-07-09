<%@page import="com.work.shop.united.client.dataobject.User"%>
<%@page import="com.work.shop.oms.utils.Constant"%>
<%@page import="com.work.shop.oms.bean.SystemOmsRole"%>
<%@page import="com.work.shop.oms.vo.AuthResource"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<link href="<%=basePath%>/Ext/desktop/app/resources/Desktop-all.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-charts/src/chart/theme/Base.js"></script>
	<script type="text/javascript">
		var basePath = '<%=basePath%>';
		var authUser = new Object();
		var roles = new Array();
		var menuData = new Array();
		<%
			HttpSession Session = request.getSession();
			User user = (User)Session.getAttribute(Constant.SESSION_USER_KEY);
			List<AuthResource> resources = (List<AuthResource>)Session.getAttribute(Constant.SESSION_RES_KEY);
			for (AuthResource resource : resources) {
				String resourceName = resource.getResourceName();
		%>
		var resourceName = '<%=resource.getResourceName()%>';
		var resourceCode = '<%=resource.getResourceCode()%>';
		var resourceType = '<%=resource.getResourceType()%>';
		var resourceUrl = '<%=resource.getResourceUrl()%>';
		var parentCode = '<%=resource.getParentCode()%>';
		var iconCls = '<%=resource.getIconCls()%>';
		var module = '<%=resource.getModule()%>';
		if (module == 'true') {
			module = true;
		} else {
			module = false;
		}
		var menu = '<%=resource.isMenu()%>';
		if (menu == 'true') {
			menu = true;
		} else {
			menu = false;
		}
		var leaf = '<%=resource.isLeaf()%>';
		if (leaf == 'true') {
			leaf = true;
		} else {
			leaf = false;
		}
		var flag = '<%=resource.getFlag()%>';
		var resource = {
			name: resourceName,
			iconCls: iconCls,
			module: module,
			id: resourceCode,
			menu: menu,
			leaf: leaf,
			flag: flag,
			url: resourceUrl,
			expanded:true
		}
		menuData.push(resource);
		<%
			}
		%>
		<%
			List<SystemOmsRole> roles = (List<SystemOmsRole>)Session.getAttribute(Constant.SESSION_ROLE_KEY);
			for (SystemOmsRole role : roles) {
		%>
			roles.push('<%=role.getRoleName()%>');
		<%
			}
		%>
		authUser.userNo = '<%=user.getEmpNo()%>';
		authUser.userName = '<%=user.getUserName()%>';
	</script>
	<script type="text/javascript" src="<%=basePath%>/Ext/desktop/app.js"></script>
	<script type="text/javascript">
		var path = "<%=basePath%>";
	</script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>订单管理系统</title>
		<style type="text/css">
			html, body {
				font:normal 12px verdana;
				margin:0;
				padding:0;
				border:0 none;
				overflow:hidden;
				height:100%;
			}
			p {
				margin:5px;
			}
			.nav {
				background-image:url(examples/shared/icons/fam/folder_go.png);
			}
			.menubar {
				border-bottom: 1px solid #AACCF6;
				color: #222222;
				cursor: pointer;
				display: block;
				height: 20px;
				width: 50px;
				line-height: 17px;
				outline-color: -moz-use-text-color;
				outline-style: none;
				outline-width: 0;
				padding: 3px;
				text-align: center;
				text-decoration: none;
				white-space: nowrap;
			}
			.menubar:hover {
 				background: #EBF3FD url(../item-over.gif) repeat-x scroll left bottom;
 				text-decoration: none;
			}
		</style>
	</head>
<body>

</body>
</html>