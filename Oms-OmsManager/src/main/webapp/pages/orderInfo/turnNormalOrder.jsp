<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>返回正常单</title>
<script type="text/javascript">
var typeItems = new Array();
var desc;
</script>
</head>
<body>
	<c:set var="num" value="1" />
	<c:forEach items="${list}" var="type" >
		<script type="text/javascript">
			var index = "${num}";
			var checked = false;
			if (index == 1) {
				var checked = true;
				desc = "${type.desc}";
			} 
			var typeObj = {
				name: "type",
				inputValue: "${type.type}",
				boxLabel: '${type.name}',
				checked: checked
			};
			typeItems.push(typeObj);
		</script>
		<c:set var="num" value="${num + 1}" />
	</c:forEach>
</body>
<script type="text/javascript">
var basePath = '<%=basePath%>';
Ext.Loader.setConfig({enabled: true});
Ext.require(['*']);
/* 
 * 这段代码好像没有用到  注释掉
 Ext.onReady(function() {
	var occItems = new Array();
	// 全流通-正常单、问题单 不占库存
	if (parent.referer != '全流通') {
		var occ = {
			name: "hasOccupyStock",
			inputValue: "1",//实际值
			boxLabel: "是",//显示值
			checked: true //默认
		};
		occItems.push(occ);
	}
	var occ = { name: "hasOccupyStock", inputValue: "0", boxLabel: "否" };
	occItems.push(occ);
	var showStatus = new Ext.form.RadioGroup({
		name: "hasOccupyStock",
		margin: '4 1 1 1',
		fieldLabel: "是否占用库存",
		width: 200,
		items: occItems
	});
	var typeRadio = new Ext.form.RadioGroup({
		name: "type",
		margin: '4 1 1 1',
		fieldLabel: "选择操作类别",
		width: 420,
		vertical: true,
		items: typeItems
	});
	var formPanel =new Ext.form.Panel({
		id : 'question_order_form',
		frame : true,
		width:  '100%',
		bodyStyle:'padding:5px 5px 0',
		height: 212,
		url : basePath+"/custom/orderStatus/normal.spmvc",
		labelAlign : 'center',
		items : [{
			xtype : 'textareafield',
			grow : true,
			name : 'message',
			fieldLabel: '操作备注',
			anchor : '80%'
		} , showStatus , {
			xtype : 'textfield',
			anchor : '100%',
			html: '<font color="red">' + desc + '</font>'
		} , typeRadio , {
			xtype: 'hidden',
			fieldLabel: '订单编号',
			name: 'orderSn',
			value: parent.orderSn
		} , {
			xtype:'hidden',
			fieldLabel: '是否历史订单',
			name: 'isHistory',
			value: parent.isHistory
		}],
		buttons : [{
			text: '保存',
			margin: 10,
			handler:function(){
				if(!formPanel.getForm().isValid()){
					alertMsg("验证", "请检查数据是否校验！");
					return;
				}
				var payStatus = "${orderInfo.payStatus}";
				var payTotalfee= "${orderInfo.payTotalFee}";
				var totalPayable= "${orderInfo.totalPayable}";
				if (payStatus == 1) {//部分付款
					payTotalfee = numFixed(payTotalfee); //payTotalfee
					if (payStatus == 0 && payTotalfee > 0) {
						var needpay = payTotalfee < 0 ? 0.00 : payTotalfee;
						alert("该订单还有￥" + needpay + "元未付款，需等待付款后才能发货，故此单暂时不能返回正常单。");
						return;
					} else if (payTotalfee < 0) {
						var lastpay = numFixed(totalPayable);
						var pp = lastpay < 0 ? 0.00 : lastpay;
						alert("该订单还有￥" + pp + "元未付款，需等待付款后才能发货，故此单暂时不能返回正常单。");
						return;
					}
				}
				var json = {
					success: function(formPanel, action){
						if (action.result.success  == "false") {
							errorMsg("结果", action.result.msg);
						} else{
							try{
								parent.refresh();
							} catch(e) {
								parent.jsonStore.load();
							}
							parent.FormEditWin.close();
						}
					},
					failure: function(formPanel, action){
						errorMsg("结果", action.result.msg);
					}
					,waitMsg:'Loading...'
				};
				formPanel.getForm().submit(json);
			}
		},{
			text: '关闭',
			margin: 10,
			handler:function(){
				formPanel.destroy();
				parent.FormEditWin.close();
			}
		}],
		buttonAlign: 'center'
	});
	formPanel.render(Ext.getBody());
}); */
</script>
</html>