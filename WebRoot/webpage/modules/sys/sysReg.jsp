<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>企业信息注册</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	       var code = "99";
	       if(validateForm.form()){
	    	   var $dom = $("form input,select,textarea");
			   var json = {};
			   $.each($dom,function(i,v){
				  var name = $(this).attr("name");
				  var val = $(this).val();
				  json[name] = val;
			   });
			   $.ajax({
				  url:'${ctx}/sys/register/save',
				  type:'post',
				  async:false,
				  data:json,
				  success:function(data){
					  code = data;
				  }
			   }); 
	       }
		   
		   return code;
		}
		
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
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
	<script type="text/javascript">
		$(function(){
			//省级联动
			$("#province").bind('change',function(){
				$("#city").empty();
				$("#county").empty();
				$("#city").append('<option value="">---请选择---</option>');
				$("#county").append('<option value="">---请选择---</option>');
				var p = $(this).val();
				if(p){
					$.ajax({
						url:'${ctx}/sysarea/sysArea/getListsByPid',
						type:'post',
						data:{id:p},
						dataType:'json',
						success:function(data){
							var options = '';
							$.each(data,function(i,v){
								options += '<option value="'+v.id+'">'+v.name+'</option>';
							});
							$("#city").append(options);
						}
					});
				}
			});
			//市级联动
			$("#city").bind('change',function(){
				$("#county").empty();
				$("#county").append('<option value="">---请选择---</option>');
				var p = $(this).val();
				if(p){
					$.ajax({
						url:'${ctx}/sysarea/sysArea/getListsByPid',
						type:'post',
						data:{id:p},
						dataType:'json',
						success:function(data){
							var options = '';
							$.each(data,function(i,v){
								options += '<option value="'+v.id+'">'+v.name+'</option>';
							});
							$("#county").append(options);
						}
					});
				}
			});
		});
	</script>
</head>
<body class="hideScroll">
		<form id="inputForm" action="${ctx}/sys/register/save" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属省：</label></td>
					<td class="width-35">
						<select id="province" name="province" class="form-control required">
							<option value="">---请选择---</option>
							<c:forEach items="${plist}" var="p">
								<option value="${p.id}">${p.name}</option>
							</c:forEach>
						<select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属市：</label></td>
					<td class="width-35">
						<select id="city" name="city" class="form-control required">
						</select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属区县：</label></td>
					<td class="width-35">
						<select id="county" name="county" class="form-control required">
						</select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>公司名称：</label></td>
					<td class="width-35">
						<input id="name" name="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>品牌名称：</label></td>
					<td class="width-35">
						<input id="brandname" name="brandname" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">地址：</label></td>
					<td class="width-35">
						<input id="address" name="address" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">Email：</label></td>
					<td class="width-35">
						<input id="email" name="email" htmlEscape="false"    class="form-control  email"/>
					</td>
					<td class="width-15 active"><label class="pull-right">法人：</label></td>
					<td class="width-35">
						<input id="qyfr" name="qyfr" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">法人电话：</label></td>
					<td class="width-35">
						<input id="qyfrdh" name="qyfrdh" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">质量负责人：</label></td>
					<td class="width-35">
						<input id="zlfzr" name="zlfzr" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">负责人电话：</label></td>
					<td class="width-35">
						<input id="zlfzrdh" name="zlfzrdh" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">公司网站：</label></td>
					<td class="width-35">
						<input id="neturl" name="neturl" htmlEscape="false"    class="form-control  url"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">公司简介：</label></td>
					<td class="width-35" colspan="3">
						<textarea id="jianjie" name="jianjie" htmlEscape="false" rows="2"   class="form-control ">
						</textarea>
						<input type="hidden" id="shzt" name="shzt" value="dsh">
					</td>
				</tr>
		 	</tbody>
		</table>
	</form>
</body>
</html>