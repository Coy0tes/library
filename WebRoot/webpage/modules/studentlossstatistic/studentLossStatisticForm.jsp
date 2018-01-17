<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生遗失记录管理</title>
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
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="bookStudentHistory" action="${ctx}/bookstudenthistory/bookStudentHistory/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">ISBN编号：</label></td>
					<td class="width-35">
						<form:input path="isbn" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">索书号：</label></td>
					<td class="width-35">
						<form:input path="ssh" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">图书分类：</label></td>
					<td class="width-35">
						<form:input path="booktype" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">书名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">作者：</label></td>
					<td class="width-35">
						<form:input path="author" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">出版社：</label></td>
					<td class="width-35">
						<form:input path="cbs" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">借出时间：</label></td>
					<td class="width-35">
						<form:input path="jcsj" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">应还日期：</label></td>
					<td class="width-35">
						<form:input path="yhrq" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">归还时间：</label></td>
					<td class="width-35">
						<form:input path="ghsj" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">赔偿时间：</label></td>
					<td class="width-35">
						<form:input path="pcsj" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">赔偿金额：</label></td>
					<td class="width-35">
						<form:input path="pcje" htmlEscape="false"    class="form-control "/>
					</td>
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">超期天数：</label></td>
					<td class="width-35">
						<form:input path="cqts" htmlEscape="false"    class="form-control "/>
					</td>
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生信息：</label></td>
					<td class="width-35">
						<form:input path="studentName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">学生学号：</label></td>
					<td class="width-35">
						<form:input path="studentXh" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生班级：</label></td>
					<td class="width-35">
						<form:input path="classesName" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>