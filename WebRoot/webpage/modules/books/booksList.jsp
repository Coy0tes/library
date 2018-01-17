<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>图书管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		
		function addssh(){
			  var str="";
			  var ids="";
			  $("tbody tr td input.i-checks:checkbox").each(function(){
			    if(true == $(this).is(':checked')){
			      str+=$(this).attr("id")+",";
			    }
			  });
			  if(str.substr(str.length-1)== ','){
			    ids = str.substr(0,str.length-1);
			  }
			  if(ids == ""){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
				return;
			  }
			  
			  // 发送请求
			openDialog('请输入索书号', '${ctx}/books/books/ssh?ids='+ids,'45%', '45%' );
			  
		}
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>图书管理列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="books" action="${ctx}/books/books/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>ISBN号：</span>
				<form:input path="isbn" htmlEscape="false" maxlength="64" placeholder="请输入完整的ISBN号" class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>索书号：</span>
				<form:input path="ssh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<%-- <span>图书分类：</span>
				<form:input path="booktype" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/> --%>
			<span>书名：</span>
				<form:input path="name" htmlEscape="false" maxlength="500"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('bookStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<%-- <shiro:hasPermission name="books:books:add">
				<table:addRow url="${ctx}/books/books/form" title="图书管理" height="85%" width="85%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission> --%>
			<shiro:hasPermission name="books:books:edit">
			    <table:editRow url="${ctx}/books/books/form" title="图书管理" id="contentTable" height="85%" width="85%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="books:books:del">
				<table:delRow url="${ctx}/books/books/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="books:books:import">
				<table:importExcel url="${ctx}/books/books/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="books:books:export">
	       		<table:exportExcel url="${ctx}/books/books/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			<shiro:hasPermission name="books:books:edit">
			    <button class="btn btn-primary btn-outline btn-sm" onclick="addssh()" > 批量增加索书号</button>
			</shiro:hasPermission>
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column isbn">ISBN号</th>
				<th  class="sort-column ssh">索书号</th>
				<th  class="sort-column booktype">图书分类</th>
				<th  class="sort-column name">书名</th>
				<th  class="sort-column author">作者</th>
				<th  class="sort-column cbs">出版社</th>
				<th  class="sort-column price">价格</th>
				<th  class="sort-column numin">在库数量</th>
				<th  class="sort-column numout">借出数量</th>
				<th  class="sort-column numloss">遗失数量</th>
				<th  class="sort-column numbroken">损坏数量</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="books">
			<tr>
				<td> <input type="checkbox" id="${books.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看图书管理', '${ctx}/books/books/form?id=${books.id}','85%', '85%')">
					${books.isbn}
				</a></td>
				<td>
					${books.ssh}
				</td>
				<td>
					${books.booktype}
				</td>
				<td>
					${books.name}
				</td>
				<td>
					${books.author}
				</td>
				<td>
					${books.cbs}
				</td>
				<td>
					${books.price}
				</td>
				<td>
					${books.numIn}
				</td>
				<td>
					${books.numOut}
				</td>
				<td>
					${books.numLoss}
				</td>
				<td>
					${books.numBroken}
				</td>
				<td>
					<shiro:hasPermission name="books:books:view">
						<a href="#" onclick="openDialogView('查看图书管理', '${ctx}/books/books/form?id=${books.id}','85%', '85%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="books:books:edit">
    					<a href="#" onclick="openDialog('修改图书管理', '${ctx}/books/books/form?id=${books.id}','85%', '85%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="books:books:del">
						<a href="${ctx}/books/books/delete?id=${books.id}" onclick="return confirmx('确认要删除该图书管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>