<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>账号信息管理</title>
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
		<h5>账号信息列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}" hideType="1"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
		<form:form id="searchForm" modelAttribute="agencyUser" action="${ctx}/agencyuser/agencyUser/" method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
			<div class="form-group">
					<span>登录名：</span>
						<form:input path="loginName" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
					&nbsp;&nbsp;
					<span>姓名：</span>
						<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
			 </div>	
		</form:form>
		<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="agencyuser:agencyUser:add">
				<table:addRow url="${ctx}/agencyuser/agencyUser/form" title="账号信息" width="400px" height="620px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="agencyuser:agencyUser:edit">
			    <table:editRow url="${ctx}/agencyuser/agencyUser/form" title="账号信息" id="contentTable" width="400px" height="620px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="agencyuser:agencyUser:del">
				<table:delRow url="${ctx}/agencyuser/agencyUser/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="agencyuser:agencyUser:import">
				<table:importExcel url="${ctx}/agencyuser/agencyUser/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="agencyuser:agencyUser:export">
	       		<table:exportExcel url="${ctx}/agencyuser/agencyUser/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column loginName">登录名</th>
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column email">邮箱</th>
				<th  class="sort-column userStatus">用户状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="agencyUser">
			<tr>
				<td> <input type="checkbox" id="${agencyUser.id}" class="i-checks"></td>
				<td>
					${agencyUser.loginName}
				</td>
				<td>
					${agencyUser.name}
				</td>
				<td>
					${agencyUser.email}
				</td>
				<td>
					<c:if test="${agencyUser.userStatus=='zc'}">
				    	<font style="color:#1a7bb9;">${fns:getDictLabel(agencyUser.userStatus, 'user_status', '')}</font>
				    </c:if>
					<c:if test="${agencyUser.userStatus=='ty'}">
				    	<font style="color:#c9302c;">${fns:getDictLabel(agencyUser.userStatus, 'user_status', '')}</font>
				    </c:if>
				</td>
				<td>
					<shiro:hasPermission name="agencyuser:agencyUser:view">
						<a href="#" onclick="openDialogView('查看账号信息', '${ctx}/agencyuser/agencyUser/form?id=${agencyUser.id}','400px', '620px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="agencyuser:agencyUser:edit">
    					<a href="#" onclick="openDialog('修改账号信息', '${ctx}/agencyuser/agencyUser/form?id=${agencyUser.id}','400px', '620px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="agencyuser:agencyUser:del">
						<a href="${ctx}/agencyuser/agencyUser/delete?id=${agencyUser.id}" onclick="return confirmx('确认要删除该账号信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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