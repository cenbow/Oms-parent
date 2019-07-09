<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%-- <link rel="stylesheet" type="text/css" src="<%=basePath%>/css/style.css"> --%>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css" />
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
	<script type="text/javascript" src="<%=basePath%>/js/my97/WdatePicker.js"></script>
	<base href="<%=basePath%>">
	<script type="text/javascript">
	var basePath='<%=basePath%>'; 
	</script>
</head>
<body>
	<div id="list1" style="padding-top:8px;">
    <div style="margin-top:10px; border:solid 1px #c8c8c8;">
    <form id="orderClear">
        <table width="100%" style="border-spacing:0px;border-collapse: collapse;font-family: '宋体','Arial';font-size: 12px;background: #fff;color: #000;">
          <td  align="center"><h3>状态：</h3></td>
            <td  align="left">
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="0" checked onclick="snChange(0)" id="sn0"/>入库</label>
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="1" onclick="snChange(1)" id="sn1"/>结算</label>	
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="2" onclick="snChange(2)" id="sn2"/>积分增减</label>	
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="3" onclick="snChange(3)" id="sn2"/>批量生成退单</label>	
            </td>
          </tr>
          <td  align="center"><h3>类型：</h3></td>
            <td  align="left">
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderType" value="0" checked onclick="typeChange(0)" id="orderType0"/>订单</label>
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderType" value="1" onclick="typeChange(1)" id="orderType1"/>退单</label>	
            </td>
          </tr>
          <tr class="tr25">
            <td width="15%" class="tdBg" align="center"><h3>单号：</h3></td>
            <td style="height:80px; border:solid 1px #abadb3;text-indent: 12px;" align="left"><textarea style="width:600px;height:60px; border:solid 1px #abadb3;"  id="returnSns" name="returnSns" ></textarea></td>
          </tr>
         
        </table>
	    <div style="margin-top:20px; text-align:center; width:100%;">
	    	<input type="submit" class="btn1" value="确定"  id="search"/> &nbsp; &nbsp; 
	    	<input type="button" class="btn1" value="重置" id="reset" />
	    </div>
	    </form>
    </div>
   
</div>
</body>
<script>


$(document).ready(function(){

	$("#search").click(function(){
    	/* var beginTime=new Date($("#beginTime").val());
    	var endTime=new Date($("#endTime").val()); */
    	/*if(endTime.getTime()<beginTime.getTime()){
    		alert("开始时间不能大于结束时间！");
    		return ;
    	} */
    	var param={};
    	param.bussType=$('input:radio[name="orderFlag"]:checked').val();
    	param.orderType=$('input:radio[name="orderType"]:checked').val();
    	param.returnSns=$('#returnSns').val(); 
    	if(!$('#returnSns').val()){
    		alert("请填写单号，单号间用“,”隔开");
    	}
    	$.ajax({
    	     type: 'POST',
    	     async:false,
    	     url: basePath+"custom/orderReturn/pushOrderReturnToERP" ,
    	     data: param ,
    	     dataType: "json",
    	     success: function(data){
    	    	alert(data.message);
    	     } ,
    	     error:function(textStatus, errorThrown){
    	    	alert("error");
    	     }

    	});
  });
  
  //重置
  $("#reset").click(function(){
    	/* var beginTime=$("#beginTime").val();
    	var endTime=$("#endTime").val();
    	if(endTime.getTime()>beginTime.getTime()){
    		
    	}else{
    		alert("开始时间不能大于结束时间！");
    	} */
  });
  
 
});


function snChange(type){
	$("input[name='sn']").attr("checked",false);
	if(type==0){
		$("#sn0").attr("checked",true);
	}else if(type==1){
		$("#sn1").attr("checked",true);
	}else if(type==2){
		$("#sn2").attr("checked",true);
	}else{
		$("#sn3").attr("checked",true);
	}
}

function typeChange(type){
	$("input[name='orderType']").attr("checked",false);
	if(type==0){
		$("#orderType0").attr("checked",true);
	}else{
		$("#orderType1").attr("checked",true);
	}
}
</script>
</html>