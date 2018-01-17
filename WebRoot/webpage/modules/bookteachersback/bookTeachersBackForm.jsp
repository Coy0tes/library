<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教师还书管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		var ispush = true;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(ispush){
			  if(validateForm.form()){
				  $("#inputForm").submit();
				  return true;
			  }
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
			
			/* $("#isbn").focus(); */
			
			// 教师编号事件，输入编号后，查询是否有该教师
			$("#gh").change(function(){
				var student = $("#gh").val();	
					$.ajax({
						url:"${ctx}/bookteachers/bookteachers/getByTeacher",
						data:{studentNum:student},
						type:"post",
						dataType:"json",
						success:function(data){
							console.info(data);
							// 00 借书表有学生
							// 99 借书表没有学生
							// 11 该生不存在
							if(data.code == "99"){
								top.layer.msg("在借图书没有找到该教师，请核实工号后再试！",{icon:2});
								ispush = false;
							}else if(data.code == "11"){
								top.layer.msg("没有找到该教师信息，请核实工号！");
								ispush = false;
							}else if(data.code == "00"){
								ispush = true;
							}
							
						}
					});
			})
			
			// 扫描还书，isbn的change事件
			$("#ib").change(function(){
				var isbn = $("#ib").val();
				var bh = $("#gh").val();
				
				if(bh){
					$.ajax({
						url:"${ctx}/bookteachersback/bookteachersback/getByIsbn",
						data:{isbn:isbn,teacherbh:bh},
						type:"post",
						dataType:"json",
						success:function(data){
								console.log(data);
								if(data.bookStudentDetail == "00"){
									top.layer.msg("没有相关信息，请核实教师工号或者ISBN后重试", {icon:2});
									empty();
								}else{
								
									$("#id").val(data.bookStudentDetail.id);
									$("#isbn").val(data.bookStudentDetail.isbn);
									$("#booktype").val(data.bookStudentDetail.booktype);
									$("#name").val(data.bookStudentDetail.name);
									$("#price").val(data.bookStudentDetail.price);
									$("#jcsj").val(data.bookStudentDetail.jcsj);
									$("#yhrq").val(data.bookStudentDetail.yhrq);
									$("#ghsj").val(data.bookStudentDetail.ghsj);
									$("#cqts").val(data.bookStudentDetail.cqts);
									$("#yhrq").val(data.bookStudentDetail.yhrq);
									
									$("#studentnum").val(data.bookStudentDetail.teacherbh);
									$("#studentname").val(data.bookStudentDetail.teachersname);
									
								}
								
						}
					});
				}else{
					top.layer.msg('请先输入教师工号',{icon:2});
					$("#ib").val()
				}
			});
			
			function empty(){
				$("#isbn").val("");
				$("#booktype").val("");
				$("#name").val("");
				$("#price").val("");
				$("#jcsj").val("");
				$("#yhrq").val("");
				$("#ghsj").val("");
				$("#cqts").val("");
				$("#yhrq").val("");
				
				$("#studentnum").val("");
				$("#studentname").val("");
			}
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="bookStudentBack" action="${ctx}/bookteachersback/bookteachersback/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		<!-- 图书信息 -->
		   		<tr>
		   			<td class="width-15 active"><label class="pull-right"><font style="color:red">*</font>教师工号：</label></td>
					<td class="width-35">
						<input id="gh" name="gh" htmlEscape="false"  placeholder="请先输入工号"  class="form-control required" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font style="color:red">*</font>ISBN编号：</label></td>
					<td class="width-35">
						<input id="ib" name="ib" htmlEscape="false" placeholder="再点击扫码"    class="form-control required"/>
					</td>
		   		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">ISBN编号：</label></td>
					<td class="width-35">
						<form:input path="isbn" htmlEscape="false" class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">图书分类：</label></td>
					<td class="width-35">
						<form:input path="booktype" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">书名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">价格：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">借出时间：</label></td>
					<td class="width-35">
						<form:input path="jcsj" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">应还日期：</label></td>
					<td class="width-35">
						<form:input path="yhrq" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">归还时间：</label></td>
					<td class="width-35">
						<form:input path="ghsj" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">超期天数：</label></td>
					<td class="width-35">
						<form:input path="cqts" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
		  		</tr>
		  		<%-- <tr>
					<td class="width-15 active"><label class="pull-right">超期金额：</label></td>
					<td class="width-35">
						<form:input path="cqts" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
		  		</tr> --%>
		  		<!-- 教师信息 -->
		   		<tr>
					<td class="width-15 active"><label class="pull-right">教师工号：</label></td>
					<td class="width-35">
						<input id="studentnum" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">教师姓名：</label></td>
					<td class="width-35">
						<input id="studentname" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
		<div style="text-align:center;margin-top:20px;">
		    <a href="javascript:" style="height:35px;width:80px;" onClick="doSubmit();" class="btn btn-success"><i class="fa fa-plus"></i>&nbsp;提交</a>
		</div>
		<br>
		<br>
		<div style="text-align:center;margin-top:20px;"><font style="color:red">*</font>温馨提示：<strong>请先输入教师工号，然后点击ISBN号输入框，扫码后即可查询该教师借阅信息</strong></div>
	</form:form>
</body>
</html>