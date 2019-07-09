<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>

<html>
<head>
<title>角色分派页面</title>
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
.tdBorder{
	border:solid #0066FF;
	border-width:1px 0px 0px 1px;
	padding:0px 0px;
}
</style>

<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
</head>
<body>

<form action="" id="dispatchRoleForm" method="post">
			<input type="hidden" id="roleCode" name="roleCode" value="${roleCode}" />
			
			<table cellspacing = "1" id="list-table">
				<tbody  id="dispatchRoleTbody">
				
				</tbody>
				<tr>
					<td align="center" colspan="3">
					     <input type="checkbox" name="checkAll"  id="checkAll"  onclick="checkAllSelect()" class="checkbox">全选
							&nbsp;&nbsp;&nbsp;&nbsp; 
						<input type="button" name="Submit" value="保存 "  onclick="saveRole()"  class="button"> 
					</td>
				</tr>
			</table>
</form>
</body>
</html>

<script type="text/javascript">
	
	var basePath = '<%=basePath%>';

	var roleCode = $("#roleCode").val();

	var dataUrl = basePath+ "/custom/systemResource/addSystemAclResourceByExtjs5.spmvc?method=init";
	createAjaxDataBySyn("questionData", dataUrl, doSuccessfun, doFailurefun, {"roleCode" : roleCode}, 100000);


	function doSuccessfun(id, response, opts) {
		if ("questionData" == id) {
			var htmlStr = '';
			var respText = Ext.JSON.decode(response.responseText);
			if (respText.data) {
				var data = respText.data;
				console.dir(data.length);
				for (var i = 0; i < data.length; i++) {
					var secList = data[i].list; //二级菜单数据
					htmlStr += '<tr>';
					var dataSelected = '';
					if (data[i].selected == true) {
						dataSelected = 'checked="checked"';
					}
					var tempLength = secList.length + 1;
					//一级菜单
					htmlStr += '<td width="18%" valign="top" rowspan="'
							+ tempLength
							+ '" class="tdBorder">'
							+ '<input margin-left="10px" name="resourceId" type="checkbox"    '
							+ dataSelected
							+ '  group="'
							+ data[i].resourceId
							+ '"  root="'
							+ data[i].resourceType
							+ '"  parent=""  id="'
							+ data[i].resourceId
							+ '"  class="checkbox"  onclick="checkRootThisInput(this)"  value="'
							+ data[i].resourceId + '" >' 
							+ '<label for="'+data[i].resourceId+'">'+data[i].resourceName+'</label>';
							+ '</td>';
					if (secList && secList.length > 0) {
						for (var j = 0; j < secList.length; j++) {
							var thrList = secList[j].list; //三级菜单数据
							console.dir("secList  " + secList[j]);
							var secListSelected = '';
							if (secList[j].selected == true) {
								secListSelected = 'checked="checked"';
							}
							htmlStr += '<tr><td width="24%" class="tdBorder">';
							//二级菜单
							htmlStr += '<input type="checkbox" name="resourceId" '
									+ secListSelected
									+ '  group="'
									+ data[i].resourceId
									+ '"  root="'
									+ secList[j].resourceType
									+ '"   parent="'
									+ secList[j].parentId
									+ '"  onclick="checkSecondStageThisInput(this)"  id="'
									+ secList[j].resourceId
									+ '" class="checkbox"  value="'
									+ secList[j].resourceId
									+ '">'
									+ '<label for="'+secList[j].resourceId+'">'+secList[j].resourceName+'</label>';
							htmlStr += '</td><td  class="tdBorder">';
							if (thrList && thrList.length > 0) {
								htmlStr += '[';
								for (var z = 0; z < thrList.length; z++) {
									var thrListSelected = '';
									if (thrList[z].selected == true) {
										thrListSelected = 'checked="checked"';
									}
									//三级菜单
									htmlStr += '<input type="checkbox" name="resourceId" '
											+ thrListSelected
											+ '  group="'
											+ data[i].resourceId
											+ '"   root="'
											+ thrList[z].resourceType
											+ '"  value="'
											+ thrList[z].resourceId
											+ '"   onclick="checkThirdStageThisInput(this)"   parent="'
											+ thrList[z].parentId
											+ '"  id="'
											+ thrList[z].resourceId
											+ '"  class="checkbox">'
											+ '<label for="'+thrList[z].resourceId+'">'+thrList[z].resourceName+'</label>';
									//两个一行
									if((z+1)%2==0&&z<thrList.length-1){
										htmlStr += '</br>';
									}
								}
								htmlStr += ']</td>';
							}
							htmlStr += '</tr>';
						}
					}
					//htmlStr += '</td>';
					htmlStr += '</tr>';
				}
				$("#dispatchRoleTbody").html(htmlStr);
			}
		} else if ("saveRole" == id) {
			var respText = Ext.JSON.decode(response.responseText);
			if (respText.isok) {
				alert(respText.message);
			} else {
				alert(respText.message);
			}
		}
	}

	function checkthisInput(obj) {

		if ($(obj).is(':checked') == true) {

			$("[name='resourceId']").each(function(i, val) {

				var me = $(val);

				var resourceType = me.group;

				if ($(obj).attr("id") == me.attr("parent")) {

					me.attr("checked", true);

				}

				if ($(obj).attr("id") == me.attr("parent")) {

					me.attr("checked", true);

				}
			});

		} else {

			$("[name='resourceId']").each(function(i, val) {

				var mme = $(val);

				var resourceType = mme.group;

				if ($(obj).attr("id") == mme.attr("parent")) {

					mme.attr("checked", false);
				}

				if ($(obj).attr("parent") == mme.attr("id")) {

					mme.attr("checked", false);

				}

			});

		}

	}

	//点击一级
	function checkRootThisInput(obj) {

		var root = $(obj).attr("group");

		if ($(obj).is(':checked') == true) {//选中

			$("[name='resourceId']").each(function(i, val) {
				var me = $(val);

				if (root == me.attr("group")) {
					me.attr("checked", true);
				}

			});
		} else {//不选中
			$("[name='resourceId']").each(function(i, val) {

				var me = $(val);

				if (root == me.attr("group")) {
					me.attr("checked", false);
				}

			});
		}

	}

	//点击二级
	function checkSecondStageThisInput(obj) {

		if ($(obj).is(':checked') == true) {//选中

			$("[name='resourceId']").each(function(i, val) {

				var me = $(val);

				//2 -1
				if ($(obj).attr("parent") == me.attr("id")) {

					if (me.is(':checked') == false) {
						me.attr("checked", true);
					}

				}

				//2-3
				if ($(obj).attr("id") == me.attr("parent")) {
					me.attr("checked", true);
				}

			});

		} else {//不选中
			$("[name='resourceId']").each(function(i, val) {

				var me = $(val);

				//2 -1
				if ($(obj).attr("parent") == me.attr("id")) {

					var num = checkParent(obj);

					if (num == 0) {
						me.attr("checked", false);
					}

				}

				//2-3
				if ($(obj).attr("id") == me.attr("parent")) {
					me.attr("checked", false);
				}

			});

		}

	}

	//点击三级
	function checkThirdStageThisInput(obj) {

		if ($(obj).is(':checked') == true) {//选中

			$("[name='resourceId']").each(function(i, val) {
				var me = $(val);

				//3 -2
				if ($(obj).attr("parent") == me.attr("id")) {//第三级父id==二级id

					if (me.is(':checked') == false) {
						me.attr("checked", true);
					}

					//3 - 1
					var grandpaId = me.attr("parent");
					if ($("#"+grandpaId).is(':checked') == false) {
						$("#"+grandpaId).attr("checked", true);
					}
				}

			});

		} else {//不选中

			$("[name='resourceId']").each(function(i, val) {
				var me = $(val);

				//3-2
				if ($(obj).attr("parent") == me.attr("id")) { //第三级父id==二级id

					var num = checkParent(obj);

					if (num == 0) {//当同一个父级下数量为空，关闭父级选项
						me.attr("checked", false);
					}

				}

			});

			var checkSelectedNum = checkGroup(obj, true);//还有几个被选中
			if (checkSelectedNum == 0) {
				closeAll(obj);
			}

		}

	}

	/**
	 **关闭第一级大类的所有选项;
	 **/
	function closeAll(obj) {

		var root = $(obj).attr("group");

		$("[name='resourceId']").each(function(i, val) {

			var me = $(val);

			if (root == me.attr("group")) {
				me.attr("checked", false);
			}

		});

	}

	function checkGroup(obj, t) {

		var num = 0;
		var root = $(obj).attr("group");

		$("[name='resourceId']").each(
				function(i, val) {
					var me = $(val);
					if (root == me.attr("group")
							&& "desktop_group" != me.attr("root")) { //组和资源类型
						if (me.is(':checked') == t) {
							num++;
						}
					}

				});
		return num;
	}

	/**
	 * 统计同一个父级下有几个数量 
	 * 
	 ***/
	function checkParent(obj) {
		//当前点击obj
		var num = 0;

		$("[name='resourceId']").each(function() {

			if ($(this).attr("parent") == $(obj).attr("parent")) {
				if ($(this).attr("checked")) {
					num++;
				}
			}
		});
		return num;
	}

	//保存角色和系统资源权限
	function saveRole() {

		var parm = "";

		$("input[name='resourceId']:checkbox:checked").each(function() {
			var resourceId = $(this).val();
			parm += resourceId + ",";
		});

		createAjaxData("saveRole", basePath
				+ "/custom/systemResource/saveDispatchRole.spmvc",
				doSuccessfun, doFailurefun, {
					"resourceId" : parm,
					"roleCode" : roleCode
				}, 100000);

	}

	function checkAllSelect() {

		if ($("#checkAll").is(':checked') == true) {

			$("[name='resourceId']").each(function() {

				$(this).attr("checked", true);
			});

		} else {

			$("[name='resourceId']").each(function() {

				$(this).attr("checked", false);
			});

		}

	}

	function doFailurefun(id, response, opts) {
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
</script>