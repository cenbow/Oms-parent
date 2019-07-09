<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>


<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/ext5.1/resources/css/custom-styles.css" />
<link href="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/ext5.1/ext-all.js"></script>
<script type="text/javascript" src="<%=basePath%>/ext5.1/packages/ext-theme-crisp/build/ext-theme-crisp.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/constant.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >

<script src="<%=basePath%>/pages/pullAndTurn/channelOrderNew.js" type="text/javascript"></script> 
 
<script type="text/javascript">
var basePath='<%=basePath%>'; 
</script>
<style type="text/css">
		.span0 {
			color : red;
		}
		.span1 {
		color : green;
		}
		.refresh {
    /* background-image: url("extjs/images/default/table_refresh.png") !important; */
    background-repeat: no-repeat;
}
    .x-selectable, .x-selectable * {         
        -moz-user-select: text! important ;         
        -khtml-user-select: text! important ;
        }         

</style>

<title>转单-订单管理</title>
</head>
<body>
<div id='form-ct'></div>
</body>
</html>