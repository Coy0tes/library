<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
	 	var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  loading('正在提交，请稍等...');
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function(){
			$("#name").focus();
			
			validateForm= $("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},//设置了远程验证，在初始化时必须预先调用一次。
					enname: {remote: "${ctx}/sys/role/checkEnname?oldEnname=" + encodeURIComponent("${role.enname}")}
				},
				messages: {
					name: {remote: "角色名已存在"},
					enname: {remote: "英文名已存在"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#name"));
			$("#inputForm").validate().element($("#enname"));
		});

	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="role" autocomplete="off" action="${ctx}/sys/role/save" method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>角色名称:</label></td>
		         <td class="width-35"><input id="oldName" name="oldName" type="hidden" value="${role.name}">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>英文名称:</label></td>
		         <td class="width-35"><input id="oldEnname" name="oldEnname" type="hidden" value="${role.enname}">
					<form:input path="enname" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      <tr>
		         <td class="width-15 active"><label class="pull-right">是否系统数据:</label></td>
		         <td class="width-35"><form:select path="sysData" class="form-control ">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">“是”代表此数据只有超级管理员能进行修改，“否”则表示拥有角色修改人员的权限都能进行修改</span></td>
		         <td  class="width-15" class="active"><label class="pull-right">是否可用</label></td>
		         <td class="width-35"><form:select path="useable" class="form-control ">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">“是”代表此数据可用，“否”则表示此数据不可用</span></td>
		      </tr>
		      <%-- <tr>
		         <td class="width-15 active"><label class="pull-right">数据范围:</label></td>
		         <td class="width-35"><form:select path="dataScope" class="form-control ">
					<form:options items="${fns:getDictList('sys_data_scope')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				 </td>
				 <td class="width-15 active"><label class="pull-right">备注:</label></td>
		         <td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		      </tr> --%>
			</tbody>
			</table>
			<form:hidden path="menuIds"/>
	</form:form>
</body>
</html>