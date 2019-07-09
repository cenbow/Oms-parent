<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%-- <%@ include file="page.jsp"%>
<%@ include file="script.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>功能连接</title>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>


<script type="text/javascript">
	function writeCookie(obj) {
		var key = $("#cookie").val();
		if (key == 'undefined' || key == null || key == '') {
			alert('请输入cookie！');
			return;
		}
		var url = basePath + '/custom/cookie/write?key=' + key;
		createAjaxDataBySyn('writeCookie',url, successfun, null, null, null);
	}
	function successfun(id,response, opts) {
		alert('保存成功');
	} 
</script>
</head>
<body>
	<table width="100%" style="border: solid 1px #c8c8c8;border-collapse: collapse;font-family: '宋体','Arial';font-size: 12px;background: #fff;color: #000;">
		<tbody>
		<tr align="center" bgcolor="#C0D0DD">
			<td colspan="4">
				<h3>功能连接</h3>
				<input type="hidden" value="1" id="ConsigTransType">
			</td>
		</tr>
		<tr >
			<td class="tdW20" align="right">种入cookie：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<input type="text" size="50" id='cookie'><input type="button" value="保存" size="50" onclick="writeCookie(this);">
				<span id='msg' style="color: red;"></span>
			</td>
		</tr>
	 	<tr >
			<td class="tdW20" align="right">订单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init
				</a>
			</td>
		</tr>
		
		<tr >
			<td class="tdW20" align="right">退单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init
				</a>
			</td>
		</tr>
		
<%-- 		<tr >
			<td class="tdW20" align="right">换单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/exchangeorder/exchangeOrderInfoList.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/exchangeorder/exchangeOrderInfoList.spmvc?method=init
				</a>
			</td>
		</tr> --%>
		
		<tr >
			<td class="tdW20" align="right">问题单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderQuestion/orderQuestionList.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderQuestion/orderQuestionList.spmvc?method=init
				</a>
			</td>
		</tr>
		
<%-- 		<tr >
			<td class="tdW20" align="right">批量设置问题单：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderquestion/orderquestionList.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderquestion/orderquestionList.spmvc?method=init
				</a>
			</td>
		</tr> --%>
<%-- 		<tr >
			<td class="tdW20" align="right">系统资源管理：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/pages/sysResource/systemResource.jsp" target="_blank">
					<%=basePath%>/pages/sysResource/systemResource.jsp
				</a>
			</td>
		</tr> --%>
		<tr >
			<td class="tdW20" align="right">退换货申请单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/goodsReturnChange/goodsReturnChangeList.spmvc?method=init" target="_blank">
					<%=basePath%>custom/goodsReturnChange/goodsReturnChangeList.spmvc?method=init
				</a>
			</td>
		</tr> 
		
		
			</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待结算订单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=orderInfoSettle" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=orderInfoSettle
				</a>
			</td>
		</tr>
<%-- 		<tr >
			<td class="tdW20" align="right">Extjs Demo (订单展示修改)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/demo/redirectOrder?orderSn=140429760300" target="_blank">
					<%=basePath%>custom/demo/redirectOrder?orderSn=140429760300
				</a>
			</td>
		</tr> --%>
<%-- 		<tr >
			<td class="tdW20" align="right"> 订单详情：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/orderInfo/orderDetail?orderSn=140429767144&isHistory=0" target="_blank">
					<%=basePath%>custom/orderInfo/orderDetail?orderSn=140429767144&isHistory=0
				</a>
			</td>
		</tr> --%>
<%-- 		<tr >
			<td class="tdW20" align="right"> 订单退单生成：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/orderReturn/orderReturnPage?relatingOrderSn=140429767133&returnType=1" target="_blank">
					<%=basePath%>custom/orderReturn/orderReturnPage?relatingOrderSn=140429767133&returnType=1
				</a>
			</td>
		</tr> --%>
<%-- 		<tr >
			<td class="tdW20" align="right"> 订单退单详情：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/orderReturn/orderReturnPage?returnSn=TD1508013732755" target="_blank">
					<%=basePath%>custom/orderReturn/orderReturnPage?returnSn=TD1508013732755
				</a>
			</td>
		</tr> --%>
<%-- 		<tr >
			<td class="tdW20" align="right">订单换单申请：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/exchangeorder/exchangeDetailPage?relatingOrderSn=140429767133" target="_blank">
					<%=basePath%>custom/exchangeorder/exchangeDetailPage?relatingOrderSn=140429767133
				</a>
			</td>
		</tr>
		<tr >
			<td class="tdW20" align="right">订单换单详情：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>custom/exchangeorder/exchangeDetailPage?exchangeOrderSn=140429767133&isHistory=0" target="_blank">
					<%=basePath%>custom/exchangeorder/exchangeDetailPage?exchangeOrderSn=140429767133&isHistory=0
				</a>
			</td>
		</tr>
		 --%>
<%-- 		<tr >
			<td class="tdW20" align="right"> 系统资源：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>mbapp/systemResourcePage.jsp" target="_blank">
					<%=basePath%>mbapp/systemResourcePage.jsp
				</a>
			</td>
		</tr> --%>
	
					<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 订单结算单：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/pages/orderTicket/orderTicket.jsp" target="_blank">
					<%=basePath%>/pages/orderTicket/orderTicket.jsp
				</a>
			</td>
		</tr> 
		<%-- 
			<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 角色：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>mbapp/systemRolePage.jsp" target="_blank">
					<%=basePath%>mbapp/systemRolePage.jsp
				</a>
			</td>
		</tr>
		
		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待结算退单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=settle" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=settle
				</a>
			</td>
		</tr>
		
		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">货到付款待收款订单：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=cashOnDelivery" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=cashOnDelivery
				</a>
			</td>
		</tr>

		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待入库退单查询：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=bePutInStorage" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=bePutInStorage
				</a>
			</td>
		</tr>
		
		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待结算订单列表：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=orderInfoSettle" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=orderInfoSettle
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 退单结算方式：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>mbapp/refundList.jsp" target="_blank">
					<%=basePath%>mbapp/refundList.jsp
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 申请退换货：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>mbapp/goodsReturnChange.jsp" target="_blank">
					<%=basePath%>mbapp/goodsReturnChange.jsp
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 退单结算：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>pages/orderReturn/orderReturnTicket.jsp" target="_blank">
					<%=basePath%>pages/orderReturn/orderReturnTicket.jsp
				</a>
			</td>
		</tr>
		
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 保证金：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>pages/deposit/orderDeposit.jsp" target="_blank">
					<%=basePath%>pages/deposit/orderDeposit.jsp
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 订单和退单调整日志：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>pages/orderInfoOrOrderReturnLog/orderLog.jsp" target="_blank">
					<%=basePath%>pages/orderInfoOrOrderReturnLog/orderLog.jsp
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right"> 强制下发：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>pages/tools/mandatoryIssued.jsp" target="_blank">
					<%=basePath%>pages/tools/mandatoryIssued.jsp
				</a>
			</td>
		</tr> 
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right">  问题单列表mvc(测试) ：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderQuestion/orderQuestionListTest.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderQuestion/orderQuestionListTest.spmvc?method=init
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right">  订单列表mvc (测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoListTest.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoListTest.spmvc?method=init
				</a>
			</td>
		</tr>
		
		<tr class="tr25 isRed">
			<td class="tdW20" align="right">  退单列表mvc (测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnListTest.spmvc?method=init" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnListTest.spmvc?method=init
				</a>
			</td>
		</tr>
		
		
			</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待结算退单列表mvc(测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=settle" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnListTest.spmvc?method=init&type=settle
				</a>
			</td>
		</tr>
		
		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">货到付款待收款订单mvc(测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=cashOnDelivery" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoListTest.spmvc?method=init&type=cashOnDelivery
				</a>
			</td>
		</tr>

		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待入库退单查询mvc(测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderReturn/orderReturnList.spmvc?method=init&type=bePutInStorage" target="_blank">
					<%=basePath%>/custom/orderReturn/orderReturnListTest.spmvc?method=init&type=bePutInStorage
				</a>
			</td>
		</tr>
		
		</tr>
			<tr class="tr25 isRed">
			<td class="tdW20" align="right">待结算订单列表mvc(测试)：</td>
			<td class="td12" width="80%" align="left" colspan="3">
				<a href="<%=basePath%>/custom/orderInfo/orderInfoList.spmvc?method=init&type=orderInfoSettle" target="_blank">
					<%=basePath%>/custom/orderInfo/orderInfoListTest.spmvc?method=init&type=orderInfoSettle
				</a>
			</td>
		</tr>
		 --%>
	</tbody>
	</table>
</body>
</html>