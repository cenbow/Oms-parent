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
          <td  align="center"><h3>类型：</h3></td>
            <td  align="left">
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="orderSn" checked onclick="snChange(0)" id="sn0"/>订单</label>
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="orderFlag" value="returnSn" onclick="snChange(1)" id="sn1"/>退单</label>	
            </td>
          </tr>
          <tr class="tr25">
            <td width="15%" class="tdBg" align="center"><h3>订单号：</h3></td>
            <td style="height:80px; border:solid 1px #abadb3;text-indent: 12px;" align="left"><textarea style="width:600px;height:60px; border:solid 1px #abadb3;"  id="orderSn" name="orderSn" ></textarea></td>
          </tr>
          <tr class="tr25">
            <td  align="center"><h3>开始时间：</h3></td>
            <td class="tdW18 td12" align="left"><input class="input1" style="width:300px;"  id="beginTime" name="beginTime" /></td>
          </tr>
          <tr class="tr25">
            <td  align="center"><h3>结束时间：</h3></td>
            <td class="tdW18 td12" align="left">
            	<input class="input1" style="width:300px;"  id="endTime" name="endTime"  />
            </td>
          </tr>
          <tr class="tr25">
          <!-- <td  align="center"><h3>是否历史数据：</h3></td>
            <td  align="left">
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="isHistory" value="0" checked onclick="radioChange(0)" id="isHistory0"/>近三个月数据</label>
            <label><input type="radio" style="width:40px; border:solid 1px #abadb3;"  name="isHistory" value="1" onclick="radioChange(1)" id="isHistory1"/>历史数据</label>	
            </td> -->
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
	Date.prototype.Format =function(format)
	{
	var o = {
	"M+" : this.getMonth()+1, //month
	"d+" : this.getDate(), //day
	"h+" : this.getHours(), //hour
	"m+" : this.getMinutes(), //minute
	"s+" : this.getSeconds(), //second
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter
	"S" : this.getMilliseconds() //millisecond
	};
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	(this.getFullYear()+"").substr(4- RegExp.$1.length));
	for(var k in o)if(new RegExp("("+ k +")").test(format))
	format = format.replace(RegExp.$1,
	RegExp.$1.length==1? o[k] :
	("00"+ o[k]).substr((""+ o[k]).length));
	return format;
	};
	
  var date=new Date();
  var thisTime=date.Format("yyyy-MM-dd hh:mm:ss");
  $("#endTime").val(thisTime);
 // alert(date.getTime());//7776000000(90天)
  var d=new Date(date.getTime()-86400000);
  var minTime=d.Format("yyyy-MM-dd hh:mm:ss");
  $("#beginTime").val(minTime);
  $("#beginTime").attr({
	    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss"})'
	  });
  $("#endTime").attr({
	    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss"})'
	  });
  //alert(d.Format("yyyy-MM-dd HH:mm:ss"))
  /* var sns=$("#orderSn").val();
    	var param=$('#orderClear').serialize();
    	$.ajax({
    	     type: 'POST',
    	     async:false,
    	     url: basePath+"api/orderClear/uploadOrderSnToMQ" ,
    	    data: param ,
    	    dataType: "json",
    	    success: function(data){
    	    	alert(data.message);
    	    } ,
    	    error:function(){
    	    	alert("error");
    	    }

    	});  */
  $("#search").click(function(){
    	/* var beginTime=new Date($("#beginTime").val());
    	var endTime=new Date($("#endTime").val()); */
    	/*if(endTime.getTime()<beginTime.getTime()){
    		alert("开始时间不能大于结束时间！");
    		return ;
    	} */
    	var param={};
     	param.beginTime=$("#beginTime").val();
    	param.endTime=$("#endTime").val();
    	param.orderFlag=$('input:radio[name="orderFlag"]:checked').val();
    	/* param.isHistory=$('input:radio[name="isHistory"]:checked').val(); */
    	param.sns=$('#orderSn').val(); 
    	$.ajax({
    	     type: 'POST',
    	     async:false,
    	     url: basePath+"custom/orderReturn/orderExpressPull" ,
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
  
  /* $("input[name='isHistory']").change(function(){
	  alert($("input[name='isHistory']").val());
	  if($("input[name='isHistory']").val()=="0"){
		  $("input[name='isHistory']")
  	  }
  }) */
});

function radioChange(isHistory){
	$("input[name='isHistory']").attr("checked",false);
	if(isHistory==0){
		$("#isHistory0").attr("checked",true);
		/* var date=new Date();
		  var thisTime=date.Format("yyyy-MM-dd hh:mm:ss");
		  $("#endTime").val(thisTime);
		  alert(date.getTime());//7776000000(90天)
		  var d=new Date(date.getTime()-7776000000);
		  var minTime=d.Format("yyyy-MM-dd hh:mm:ss");
		  $("#beginTime").val(minTime);
		  $("#beginTime").attr({
			    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss",minDate:"'+minTime+'",maxDate:"'+thisTime+'"})'
			  });
		  $("#endTime").attr({
			    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss",minDate:"'+minTime+'",maxDate:"'+thisTime+'"})'
			  }); */
	}else{
		$("#isHistory1").attr("checked",true);
		/* var date=new Date();
		  //alert(date.getTime());//7776000000(90天)
		  var d=new Date(date.getTime()-7776000000);
		  var maxTime=d.Format("yyyy-MM-dd hh:mm:ss");
		  var minTime=new Date(date.getTime()-7776000000-7776000000);
		  $("#beginTime").val(minTime.Format("yyyy-MM-dd hh:mm:ss"));
		  $("#endTime").val(maxTime);
		  $("#beginTime").attr({
			    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss",maxDate:"'+maxTime+'"})'
			  });
		  $("#endTime").attr({
			    "onClick" : 'WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss",maxDate:"'+maxTime+'"})'
			  }); */
	}
}

function snChange(type){
	$("input[name='sn']").attr("checked",false);
	if(type==0){
		$("#sn0").attr("checked",true);
	}else{
		$("#sn1").attr("checked",true);
	}
}
</script>
</html>