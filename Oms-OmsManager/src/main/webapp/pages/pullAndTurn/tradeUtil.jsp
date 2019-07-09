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

<script src="<%=basePath%>/pages/pullAndTurn/tradeUtilNew.js" type="text/javascript"></script>
<script type="text/javascript">
var basePath='<%=basePath%>'; 
</script>
<title>转单-拉单</title>
<style type="text/css">
 /* .upload-icon {  
            background: url('extjs/images/access/tree/folder.gif') no-repeat 0 0 !important;  
        }  
     .x-form-file-wrap {  
        position: relative;  
        height: 22px;  
    }  
    .x-form-file-wrap .x-form-file {  
        position: absolute;  
        right: 0;  
        -moz-opacity: 0;  
        filter:alpha(opacity: 0);  
        opacity: 0;  
        z-index: 2;  
        height: 22px;  
    }  
    .x-form-file-wrap .x-form-file-btn {  
        position: absolute;  
        right: 0;  
        z-index: 1;  
    }  
    .x-form-file-wrap .x-form-file-text {  
        position: absolute;  
        left: 0;  
        z-index: 3;  
        color: #777;  
    }  
         */ 
</style>
</head>
<body>

<div id='form-ct'></div>
</body>
</html>