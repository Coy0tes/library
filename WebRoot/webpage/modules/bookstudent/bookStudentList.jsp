<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生借书管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>学生借书管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="bookStudent" action="${ctx}/bookstudent/bookStudent/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>学生：</span>
				<form:input path="student.name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>学生编号：</span>
				<form:input path="student.xh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="bookstudent:bookStudent:add">
				<table:addRow url="${ctx}/bookstudent/bookStudent/form" title="学生借书管理" width="85%" height="85%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookstudent:bookStudent:edit">
			    <table:editRow url="${ctx}/bookstudent/bookStudent/form" title="学生借书管理" id="contentTable"  width="85%" height="85%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookstudent:bookStudent:del">
				<table:delRow url="${ctx}/bookstudent/bookStudent/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookstudent:bookStudent:import">
				<table:importExcel url="${ctx}/bookstudent/bookStudent/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookstudent:bookStudent:export">
	       		<table:exportExcel url="${ctx}/bookstudent/bookStudent/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-primary btn-outline btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
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
				<th  class="sort-column b.xh">学生学号</th>
				<th  class="sort-column b.name">学生</th>
				<th  class="sort-column n.name">操作员</th>
				<th  class="sort-column createDate">借出时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="bookStudent">
			<tr>
				<td> <input type="checkbox" id="${bookStudent.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学生借书管理', '${ctx}/bookstudent/bookStudent/form?id=${bookStudent.id}','85%', '85%')">
					${bookStudent.student.xh}
				</a></td>
				<td>
					${bookStudent.student.name}
				</td>
				<td>
					${bookStudent.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${bookStudent.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="bookstudent:bookStudent:view">
						<a href="#" onclick="openDialogView('查看学生借书管理', '${ctx}/bookstudent/bookStudent/form?id=${bookStudent.id}','85%', '85%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bookstudent:bookStudent:edit">
    					<a href="#" onclick="openDialog('修改学生借书管理', '${ctx}/bookstudent/bookStudent/form?id=${bookStudent.id}','85%', '85%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bookstudent:bookStudent:del">
						<a href="${ctx}/bookstudent/bookStudent/delete?id=${bookStudent.id}" onclick="return confirmx('确认要删除该学生借书管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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