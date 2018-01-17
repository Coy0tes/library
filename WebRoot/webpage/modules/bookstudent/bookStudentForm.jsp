<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!------------------页面判断以及校验START------------------- 

1、提交校验：
	1）、判断学生信息是否为空
	2）、图书信息不能为空
2、用到的函数：
	1）、回车键触发事件
			keyDown(e)
	 
	3）、空行判断,有空行返回true,否则返回false
			nullrow()
	4)、借书不成功，当前数量处理，-1
			bookByNum()
	5)、学生借书详情
			xiangqing(dataBookStudent)
	6）、最大可借图书数量判断, true为可借，false为不可借
			maxbook(idx)
	7）、清空子表数据
			resetRow(idx)
3、btn按钮查询事件：
	校验：
		赋值学生信息，先清空学生信息表跟主表id
		判断学生在借书籍数量，超过规定的数量后不可借书
		提交空值校验
4、addRow
	1）、校验：
		对所有子表input添加只读属性
		对当前正在输入的isbn的input移除只读属性，并添加聚焦事件
		对当前正在输入的isbn的input的change事件
		最大借书数量判断
		录入重复判断
	2）、根据ISBN号查询书籍的判断
		书的三种状态，除了在库状态，其他条件都要添加 清空行（resetRow(idx);） 跟隐藏域当前在借数量-1（bookByNum();）的函数
		如果未查询到相关isbn码的书，要添加 清空行（resetRow(idx);） 跟隐藏域当前在借数量-1（bookByNum();）的函数
		重复添加isbn，要添加 清空行（resetRow(idx);） 跟隐藏域当前在借数量-1（bookByNum();）的函数
		
	3）、查询图书返回值判断
		书本状态
		有没有所查询的书籍
		
	



主表：
	根据学生学号查询主表，如果没有查到，则清空后（包括主表id）重新赋值父表信息，提交存储状态为插入操作
					如果查到，则清空后（包括主表id）重新赋值父表信息（包括父表id），提交存储状态为更新操作
detail子表：
	只显示当前在借的书籍
history子表：
	只显示目前在借的书籍


------------------页面判断以及校验END-------------------  -->


<html>
<head>
	<title>学生借书管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		
			// 有没有加载学生信息判断
			if($(".studentid").val()==""){
				top.layer.msg("请查询学生信息！", {icon:2});
				return false;
			}
			
			// 有没有图书信息判断
			if($(".isbn").val()==""){
				top.layer.msg("图书信息不能为空！", {icon:2});
				return false;
			} 
			
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
			
			
			// 将fouce交点添加到学生学号
			$("input[name='student.xh']").focus();
		});
		
		
		//查询学生基本信息
		function btn(){
			var xh = $(".xh").val();
			$.ajax({
				url:"${ctx}/bookstudent/bookStudent/getXh",
				data:{"student.xh":xh},
				type:"post",
				datatype:"json",
				success:function(data){
					console.info(data);
					// 加载赋值学生信息
					if(data.students){
						// 每次重新获取学生信息，就先清空一次主表id
						$("#id").val("");
						if(data.bookStudent!=0){
							$("#id").val(data.bookStudent.id);
						}
						$("input[name='student.id']").val(data.students.id);
						$("input[name='student.xh']").val(data.students.xh);
						$("input[name='student.name']").val(data.students.name);
						$("input[name='student.classid.name']").val(data.students.classid.name);
						$("#yajin").val(data.canshu.yajin);

						//加载学生已借书籍信息
						// 加载前先清空
						$("#bookStudentHistoryList").empty();
						if(data.bookStudent!=0){
							var dataBookStudent = data.bookStudent.bookStudentDetailList;
							
							// 在借书籍子表信息
							xiangqing(dataBookStudent);
							
						}
						
						
						// 使用隐藏域存放当前学生借书的数量，跟系统参数中最大借书数量
						// 将该生的借书数量跟参数的最多借书数量附给隐藏域，查询书籍的时候调用该生的借书数量来做判断，防止下一次借书时超过限制但是会借书成功
						// 隐藏域中学生借书数量是动态变化的，先从后台查询该生借了几本书，然后前台添加时再加1
						$("#hiddenStudentBooksShuliang").val(data.studentBooksShuliang);
						$("#hiddenCanshuMaxNum").val(data.canshu.booknum);
						var hiddenStudentBooksShuliang = $("#hiddenStudentBooksShuliang").val(); 
						
						// 根据学生借书数量，判断是否还能继续借书
						if(data.canshu.booknum > hiddenStudentBooksShuliang){
							// 判断有没有填写书籍信息，有信息则新加一行，没有就不加行
							var isnull = nullrow();
							if(!isnull){
								// 查询到学生后子表新增一行
								addRow('#bookStudentDetailList', bookStudentDetailRowIdx, bookStudentDetailTpl);
								// 焦点放到新加的isbn上
								$('input[name="bookStudentDetailList['+bookStudentDetailRowIdx+'].isbn"]').removeAttr("readonly").focus();
							}else{
								
								$('input[name="bookStudentDetailList['+bookStudentDetailRowIdx+'].isbn"]').removeAttr("readonly").focus();
							}
							
						}else{
							top.layer.msg("该生此次借书已达到规定的"+data.canshu.booknum+"本数量，请该生还书后再借！", {icon:2});
						}
						
						
					}else{
						
						//如果没有找到学生信息，重置为空
						top.layer.msg("没有找到该生信息,请核实学号后重试！", {icon:2});
						$("#id").val("");
						$("input[name='student.id']").val("");
						$("input[name='student.xh']").val("");
						$("input[name='student.name']").val("");
						$("input[name='student.classid.name']").val("");
						$("#yajin").val("");
					}
				}
			}); 
		}
			
		
		
		function addRow(list, idx, tpl, row){
			
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			
			// 给所有子表行加只读属性
			$(list+idx).find("input").attr("readonly","true");
			
			// 检测 bookStudentDetailList{{idx}}_isbnFouce 的 change 事件
			$("#bookStudentDetailList"+idx+"_isbn").change(function(){

			// 最大借阅数量判断
			var ismax = maxbook(idx);
			if(!ismax){
				return false;
			}	
					
			// 重复录入判断事件
			/* var boo = repeat($("#bookStudentDetailList"+idx+"_isbn").val(),idx);
			if(!boo){
				
				// 借书不成功，隐藏域当前在借数量-1
				bookByNum();
				
				return false;
			} */
		
			// 图书查询
			var isbn = $("#bookStudentDetailList"+idx+"_isbn").val();
			if(isbn){
				$.ajax({
					url:"${ctx}/books/books/getByIsbn",
					data:{isbn:isbn},
					type:"post",
					datatype:"json",
					success:function(data){
						// 判断返回值是否为空，空则提示没有此书
						if(data.books){
							
							// 判断该书是否已借完
							if(data.books.numIn>0){
							
								$("#bookStudentDetailList"+idx+"_bookid").val(data.books.id);
								$("#bookStudentDetailList"+idx+"_isbn").val(data.books.isbn);
								$("#bookStudentDetailList"+idx+"_ssh").val(data.books.ssh);
								$("#bookStudentDetailList"+idx+"_booktype").val(data.books.booktype);
								$("#bookStudentDetailList"+idx+"_name").val(data.books.name);
								$("#bookStudentDetailList"+idx+"_author").val(data.books.author);
								$("#bookStudentDetailList"+idx+"_cbs").val(data.books.cbs);
								$("#bookStudentDetailList"+idx+"_price").val(data.books.price);
								$("#bookStudentDetailList"+idx+"_jcsj").val(data.books.jcsj);
								$("#bookStudentDetailList"+idx+"_yhrq").val(data.books.yhrq);
								$("#bookStudentDetailList"+idx+"_ghsj").val(data.books.ghsj);
								$("#bookStudentDetailList"+idx+"_cqts").val(data.books.cqts);
								
								
								// 查询到图书后，idx先加1，子表新增一行，添加聚焦事件
								idx = idx + 1;
								addRow('#bookStudentDetailList', idx, bookStudentDetailTpl);
								$("#bookStudentDetailList"+idx+"_isbn").removeAttr("readonly").focus();
							}else{
								top.layer.msg("该书已借完", {icon:2});
								
								// 借书不成功，隐藏域当前在借数量-1
								bookByNum();
							}
								
						}else{
							// 如果没有找到信息，则重置为空
							top.layer.msg("没有找到该图书!",{icon:2});
							
							// 借书不成功，隐藏域当前在借数量-1
							bookByNum();
							
							// 调用清空行
							resetRow(idx);
						}
						
						
					},error:function(e){
						  console.log("error:"+e);
					}
				}); 
			}
					
			});
			
		}
		

		// 1、回车键触发事件
		function keyDown(e) {
			var ev= window.event||e;
			//13是键盘上面固定的回车键
			if (ev.keyCode == 13) {
				
				// 执行的方法
				btn();
			  }
		 }
		
		// 2、重复录入判断
		function repeat(isbn,idx){
			// 获取全部ISBN号单个循环
			$(".isbn").not($("#bookStudentDetailList"+idx+"_isbn")).each(function(){
				var isbn2 = ($(this).val());
				if(isbn2 == isbn){
					top.layer.msg("不能重复录入！", {icon:2});
					// 调用清空行数据
					resetRow(idx);
					
					// 获取隐藏域已借出数量，因为借书不成功，所以-1，如果借书成功，则+1
					bookByNum();
					
					return false;
				}
			})
			return true;
		}

		// 借书不成功，当前数量处理，-1
		function bookByNum(){
			var hiddenStudentBooksShuliang = parseInt($("#hiddenStudentBooksShuliang").val());
			hiddenStudentBooksShuliang -=1;
			$("#hiddenStudentBooksShuliang").val(hiddenStudentBooksShuliang);
		}
		
		// 空行判断,有空行返回true,否则返回false
		function nullrow(){
			var isnull = false;
			if($(".isbn").length>0){
				$(".isbn").each(function(){
					if(!$(this).val()){
						isnull = true;
						return false;
					}
				});
			}
			return isnull;
		}	
		
		
		// 学生借书详情
		function xiangqing(dataBookStudent){
			//加载学生已借书籍信息
			for (var i=0; i<dataBookStudent.length; i++){
				addRow('#bookStudentHistoryList', bookStudentHistoryRowIdx, bookStudentHistoryTpl, dataBookStudent[i]);
				bookStudentHistoryRowIdx = bookStudentHistoryRowIdx + 1;
			}
		}
		
			
		// 最大可借图书数量判断, true为可借，false为不可借
		function maxbook(idx){
			var isMax = true;
			// 隐藏域动态赋值
			var hiddenStudentBooksShuliang1 = $("#hiddenStudentBooksShuliang").val();
			var hiddenStudentBooksShuliang1 = parseInt(hiddenStudentBooksShuliang1);
			hiddenStudentBooksShuliang1+=1;
			
			var hiddenCanshuMaxNum = $("#hiddenCanshuMaxNum").val();
			
			if(hiddenStudentBooksShuliang1<=hiddenCanshuMaxNum){
				
				// 借书数量+1
				$("#hiddenStudentBooksShuliang").val(hiddenStudentBooksShuliang1);
				
			}else{
				top.layer.msg("超出最大借书数量，请该生还书后再借！", {icon:2});
				// 调用清空行
				resetRow(idx);
				isMax = false;
			}
			return isMax;
		}
		
		// 清空子表数据
		function resetRow(idx){
			$("#bookStudentDetailList"+idx+"_bookid").val("");
			$("#bookStudentDetailList"+idx+"_isbn").val("");
			$("#bookStudentDetailList"+idx+"_ssh").val("");
			$("#bookStudentDetailList"+idx+"_booktype").val("");
			$("#bookStudentDetailList"+idx+"_name").val("");
			$("#bookStudentDetailList"+idx+"_author").val("");
			$("#bookStudentDetailList"+idx+"_cbs").val("");
			$("#bookStudentDetailList"+idx+"_price").val("");
			$("#bookStudentDetailList"+idx+"_jcsj").val("");
			$("#bookStudentDetailList"+idx+"_yhrq").val("");
			$("#bookStudentDetailList"+idx+"_ghsj").val("");
			$("#bookStudentDetailList"+idx+"_cqts").val("");
			
		}
		
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="bookStudent" action="${ctx}/bookstudent/bookStudent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="hiddenStudentBooksShuliang">
		<input type="hidden" id="hiddenCanshuMaxNum">
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学生学号：</label></td>
					<td class="width-35">
						<form:hidden path="student.id" htmlEscape="false"    class="form-control required studentid"/>
						<form:input path="student.xh" htmlEscape="false" style="width:50%" placeholder="输入学号后回车即可查询" onkeydown="keyDown(event)" class="form-control xh"/>
						<input type="button" value="查询" onclick="btn();"/>
					</td>
		  		</tr>
		  		<tr>
					<td class="width-15 active"><label class="pull-right">学生姓名：</label></td>
					<td class="width-35">
						<form:input path="student.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学生班级：</label></td>
					<td class="width-35">
						<form:input path="student.classid.name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">借书押金(元)：</label></td>
					<td class="width-35">
						${canshu.yajin }
					</td>
				</tr>
				<tr>
					<td ><label class="pull-right"><font style="color:red">*</font>温馨提示：</label></td>
					<td><strong>请先输入学号，按回车键自动加载完学生信息，确认无误后可直接扫描图书的ISBN号，等图书信息自动加载后，可继续扫描，确认信息无误后可点击确定提交</strong></td>
				</tr>
		 	</tbody>
		</table>
		
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">学生借书：</a>
                </li>
				<li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">学生在借书籍：</a>
                </li>
            </ul>
            
            <div class="tab-content">
				<div id="tab-1" class="tab-pane active">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#bookStudentDetailList', bookStudentDetailRowIdx, bookStudentDetailTpl);bookStudentDetailRowIdx = bookStudentDetailRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>ISBN编号</th>
						<th>索书号</th>
						<th>图书分类</th>
						<th>书名</th>
						<th>作者</th>
						<th>出版社</th>
						<th>价格</th>
						<th>借出时间</th>
						<th>应还日期</th>
						<th>归还时间</th>
						<th>超期天数</th>
					</tr>
				</thead>
				<tbody id="bookStudentDetailList">
				</tbody>
			</table>
			<script type="text/template" id="bookStudentDetailTpl">//<!--
				<tr id="bookStudentDetailList{{idx}}">
					<td class="hide">
						<input id="bookStudentDetailList{{idx}}_id" name="bookStudentDetailList[{{idx}}].id" type="hidden" value="{{row.id}}" class="id"/>
						<input id="bookStudentDetailList{{idx}}_delFlag" name="bookStudentDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td  class="hide">
						<input id="bookStudentDetailList{{idx}}_bookid" name="bookStudentDetailList[{{idx}}].bookid" type="text" value="{{row.bookid}}"    class="form-control bookid"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_isbn" placeholder="点击后扫描" name="bookStudentDetailList[{{idx}}].isbn" type="text" value="{{row.isbn}}"    class="form-control isbn"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_ssh" name="bookStudentDetailList[{{idx}}].ssh" type="text" value="{{row.ssh}}"    class="form-control ssh"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_booktype" name="bookStudentDetailList[{{idx}}].booktype" type="text" value="{{row.booktype}}"    class="form-control booktype"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_name" name="bookStudentDetailList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control name"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_author" name="bookStudentDetailList[{{idx}}].author" type="text" value="{{row.author}}"    class="form-control author"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_cbs" name="bookStudentDetailList[{{idx}}].cbs" type="text" value="{{row.cbs}}"    class="form-control cbs"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_price" name="bookStudentDetailList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control price"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_jcsj" name="bookStudentDetailList[{{idx}}].jcsj" type="text" value="{{row.jcsj}}"    class="form-control jcsj"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_yhrq" name="bookStudentDetailList[{{idx}}].yhrq" type="text" value="{{row.yhrq}}"    class="form-control yhrq"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_ghsj" name="bookStudentDetailList[{{idx}}].ghsj" type="text" value="{{row.ghsj}}"    class="form-control ghsj"/>
					</td>
					
					
					<td>
						<input id="bookStudentDetailList{{idx}}_cqts" name="bookStudentDetailList[{{idx}}].cqts" type="text" value="{{row.cqts}}"    class="form-control cqts"/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var bookStudentDetailRowIdx = 0, bookStudentDetailTpl = $("#bookStudentDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(bookStudent.bookStudentDetailList)};
					var data = ${fns:toJson(bookStudent.bookStudentDetailList)};
					for (var i=0; i<data.length; i++){
					//	addRow('#bookStudentDetailList', bookStudentDetailRowIdx, bookStudentDetailTpl, "");
						bookStudentDetailRowIdx = bookStudentDetailRowIdx + 1;
					}
				});
			</script>
			</div>
				<div id="tab-2" class="tab-pane">
			<!-- <a class="btn btn-white btn-sm" onclick="addRow('#bookStudentHistoryList', bookStudentHistoryRowIdx, bookStudentHistoryTpl);bookStudentHistoryRowIdx = bookStudentHistoryRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a> -->
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>ISBN编号</th>
						<th>索书号</th>
						<th>图书分类</th>
						<th>书名</th>
						<th>作者</th>
						<th>出版社</th>
						<th>价格</th>
						<th>借出时间</th>
						<th>应还日期</th>
						<th>归还时间</th>
						<th>超期天数</th>
					</tr>
				</thead>
				<tbody id="bookStudentHistoryList">
				</tbody>
			</table>
			<script type="text/template" id="bookStudentHistoryTpl">//<!--
				<tr id="bookStudentHistoryList{{idx}}">
					<td class="hide">
						<input id="bookStudentHistoryList{{idx}}_id" name="bookStudentHistoryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="bookStudentHistoryList{{idx}}_delFlag" name="bookStudentHistoryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_isbn" name="bookStudentHistoryList[{{idx}}].isbn" type="text" value="{{row.isbn}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_ssh" name="bookStudentHistoryList[{{idx}}].ssh" type="text" value="{{row.ssh}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_booktype" name="bookStudentHistoryList[{{idx}}].booktype" type="text" value="{{row.booktype}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_name" name="bookStudentHistoryList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_author" name="bookStudentHistoryList[{{idx}}].author" type="text" value="{{row.author}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_cbs" name="bookStudentHistoryList[{{idx}}].cbs" type="text" value="{{row.cbs}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_price" name="bookStudentHistoryList[{{idx}}].price" type="text" value="{{row.price}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_jcsj" name="bookStudentHistoryList[{{idx}}].jcsj" type="text" value="{{row.jcsj}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_yhrq" name="bookStudentHistoryList[{{idx}}].yhrq" type="text" value="{{row.yhrq}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_ghsj" name="bookStudentHistoryList[{{idx}}].ghsj" type="text" value="{{row.ghsj}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="bookStudentHistoryList{{idx}}_cqts" name="bookStudentHistoryList[{{idx}}].cqts" type="text" value="{{row.cqts}}"    class="form-control "/>
					</td>
					
				</tr>//-->
			</script>
			<script type="text/javascript">
				var bookStudentHistoryRowIdx = 0, bookStudentHistoryTpl = $("#bookStudentHistoryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(bookStudent.bookStudentDetailList)};
					for (var i=0; i<data.length; i++){
						addRow('#bookStudentHistoryList', bookStudentHistoryRowIdx, bookStudentHistoryTpl, data[i]);
						bookStudentHistoryRowIdx = bookStudentHistoryRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
	</form:form>
</body>
</html>