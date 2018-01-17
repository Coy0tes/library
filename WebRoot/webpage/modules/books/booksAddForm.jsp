<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>图书管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var a = true;
			// 判断是否是新增，如果有id，则表明不是新增，就不用校验
			/* if(!'${books.id}'){
			
				$.ajax({
					url:"${ctx}/books/books/findRepart",
					data:{isbn:$("#isbn").val()},
					type:"post",
					dataType:"json",
					async:false,
					success:function(data){
						if(data.rep!=0){
							var msg = "ISBN号【"+$("#isbn").val()+"】已存在!";
							top.layer.msg(msg, {icon:2});
							a=false;
						}
					}
				});
			} */
			
			
			if(a){
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
			
			// 自动获取文本框
			$("#isbn").focus();
			$("#isbn").change(function(){
				/* $(this).val(""); */
			});
			
			// 编辑或者查看时页面图书类型赋值
			if($("#booktypeid")!=null){
				$("#booktype").val("${books.booktype}");
			}
			
			// 编辑页面ISBN号不能编辑
			if('${books.id}'){
				/* $("#isbn").attr("readonly","true"); */
				$("#isbn").parent().html('${books.isbn}');
			}
			
		});
		
		function bookType(){
			//openDialog('修改图书分类', '${ctx}/booktype/bookType/bookTypeList?parent=0','80%', '80%');
			/* $.post("${ctx}/booktype/bookType/bookTypeList",{parent:"0"},function(data){
				console.log(data)
			}); */
			top.layer.open({
			    type: 2,  
			    area: ['80%', '80%'],
			    title: '修改图书分类',
		        maxmin: true, //开启最大化最小化按钮
			    content: '${ctx}/booktype/bookType/bookTypeList?parent=0' ,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	 var body = top.layer.getChildFrame('body', index);
			         var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			         var inputForm = body.find('#inputForm');
			         var rtn = iframeWin.contentWindow.doSubmit();
			         var arr = rtn.split("&");
			         var code = arr[0];
			         var id = arr[1];
			         var name = arr[2];
			         if("00"==code){
			        	 $("#booktypeid").val(id);
			        	 $("#booktype").val(name);
			        	 top.layer.close(index);
			         }
				  },
				  cancel: function(index){ 
			      }
			});
		}
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="books" action="${ctx}/books/books/saveAdd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>ISBN号：</label></td>
					<td class="width-35">
						<form:input path="isbn" htmlEscape="false" maxlength="13" placeholder="可直接扫描ISBN号" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>图书数量</label></td>
					<td class="width-35">
						<form:input path="numIn" htmlEscape="false"  class="form-control digits required"/>
					</td>
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">索书号：</label></td>
					<td class="width-35">
						<form:input path="ssh" htmlEscape="false"   class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">图书分类：</label></td>
					<td class="width-35">
						<form:input path="booktypeid" type="hidden"/>
						<input id="booktype" htmlEscape="false" onclick="bookType();"  onfocus="this.blur()" placeholder="点击这里弹出图书分类" class="form-control "/>
					</td>
				</tr>
				<tr>
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
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">出版社：</label></td>
					<td class="width-35">
						<form:input path="cbs" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">价格（元）：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false"    class="form-control number"/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
			<div style="text-align:center;margin-top:20px;">
			    <a href="javascript:" style="height:35px;width:80px;" onClick="doSubmit();" class="btn btn-success"><i class="fa fa-plus"></i>&nbsp;提交</a>
			</div>
	</form:form>
</body>
</html>