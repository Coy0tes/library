<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>账号信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${agencyUser.loginName}')}//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			$("#inputForm").validate().element($("#loginName"));			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="agencyUser" action="${ctx}/agencyuser/agencyUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>登录名：</label></td>
					<td class="width-35">
						<c:if test="${empty agencyUser.id}">
							<form:input path="loginName" htmlEscape="false"    class="form-control required"/>
						</c:if>
						<c:if test="${not empty agencyUser.id}">
							<form:input path="loginName" htmlEscape="false"    class="form-control required" readonly="true"/>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><c:if test="${empty agencyUser.id}"><font color="red">*</font></c:if>密码：</label></td>
					<td class="width-35">
					    <input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty agencyUser.id?'required':''}"/>
						<c:if test="${not empty agencyUser.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
					</td>
				</tr>
				<tr>
					<td class="active"><label class="pull-right"><c:if test="${empty agencyUser.id}"><font color="red">*</font></c:if>确认密码:</label></td>
		            <td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  class="form-control ${empty agencyUser.id?'required':''}" value="" maxlength="50" minlength="3" equalTo="#newPassword"/></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>姓名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>电话：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
					<td class="width-35">
						<form:input path="email" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用户状态：</label></td>
					<td class="width-35">
						<form:select path="userStatus" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('user_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					 <td class="active"><label class="pull-right"><font color="red">*</font>用户角色:</label></td>
			         <td colspan="3">
			         	<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="i-checks required"  />
			         	<label id="roleIdList-error" class="error" for="roleIdList"></label>
			         </td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>