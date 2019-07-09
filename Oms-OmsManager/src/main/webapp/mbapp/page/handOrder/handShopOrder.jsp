<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
<head>
<title>代打单</title>
<style type="text/css">
body {
	background: none;
}

.even {
	background-color: #CCFFFF;
}

.odd {
	background-color: #FFFFFF;
}

.tableSty {
	border: solid 1px #c8c8c8;
	border-collapse: collapse;
	font-family: \'宋体\', \'Arial\';
	font-size: 12px;
	background: #fff;
	color: #000;
	width: 100%;
}
.child-row .x-grid-cell { background-color: #F08080; color: #000000; }
.adult-row .x-grid-cell { background-color: #ffffff; color: #000000; }
</style>
<script type="text/javascript">
	var basePath = '<%=basePath%>';
</script>
<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"  defer="defer"></script>
<script type="text/javascript" src="<%=basePath%>/js/commonModel.js"></script>
</head>
<body>
	用户ID：<input type="text" value="" id="userId" name="userId" />
	<button onclick="copyText()">代打单</button>
</body>
<script type="text/javascript">

function copyText() {
	var userId = $('input[name=userId]').val();
	console.dir(userId);
	if(userId == null || userId == ""){
		alert("用户ID不能为空!");
		return false;
	}
	var myUrl = basePath + 'custom/handOrder/shopHandOrderCheck';
	
	$.ajax({
		url: myUrl,
		type: 'get',
		timeout: 1000,
		data: {"userId":userId},
		success: function (data, status) {
			console.dir(data);
			if (data.isOk == 1) {
				var url = 'http://pact.chlitina.com.cn/login/index?shopCode=' + userId + '&insteadUserId=' + data.data
						+ '&time=' + new Date().getTime();
				console.dir(url);
				window.open(url);
			} else {
				alert(data.message);
			}
		},
		fail: function (err, status) {
			console.dir(err);
		}
	});
}

function toVaild(){
	var val = document.getElementById("userId").value;
	if(val && val != ""){
		return true;
	} else {
		alert("用户ID不能为空!");
		return false;
	}
}
</script>
</html>