<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>
<%@ include file="/extJs5.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树形菜单</title>
<script type="text/javascript">
var path = '<%=path%>';
</script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/Ext/desktop/app/menu.js"></script> --%>
<script type="text/javascript" src="<%=path%>/Ext/desktop/app/MainTabPanel.js"></script>
</head>
<body>
<div id="main" style="width:100%;height:100%;"></div>
<script type="text/javascript">
	var mid = getParam("id");
	//var flag = getParam("flag");
	
	//var path = "<%=path%>";
	//var menuData = [];
	
	/*Ext.onReady(function(){  
	    var panel = createTreePanel(type);
	    if (Ext.isIE) {
		    setTimeout(function(){
	    		panel.show();                                 
			}, 500);
		} 
	});  */
</script>
</body>

</html>