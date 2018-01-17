<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>公众号信息管理</title>
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
		<h5>公众号信息列表 </h5>
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
	<sys:message content="${message}" hideType="1"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="sysweixin" action="${ctx}/sysweixin/sysweixin/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sysweixin:sysweixin:add">
				<table:addRow url="${ctx}/sysweixin/sysweixin/form" title="公众号信息" width="900px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysweixin:sysweixin:edit">
			    <table:editRow url="${ctx}/sysweixin/sysweixin/form" title="公众号信息" id="contentTable" width="900px"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sysweixin:sysweixin:del">
				<table:delRow url="${ctx}/sysweixin/sysweixin/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="sysweixin:sysweixin:import">
				<table:importExcel url="${ctx}/sysweixin/sysweixin/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission> --%>
			<shiro:hasPermission name="sysweixin:sysweixin:export">
	       		<table:exportExcel url="${ctx}/sysweixin/sysweixin/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th>微信号原始ID</th>
				<th>微信Appid</th>
				<th>微信Appsecret</th>
				<th>微信Token</th>
				<th>支付商户ID</th>
				<th>支付ApiKey</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysweixin">
			<tr>
				<td> <input type="checkbox" id="${sysweixin.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看公众号信息', '${ctx}/sysweixin/sysweixin/form?id=${sysweixin.id}','900px', '500px')">
					${sysweixin.weixinhao}
				</a></td>
				<td>
					${sysweixin.weixinappid}
				</td>
				<td>
					${sysweixin.weixinappsecret}
				</td>
				<td>
					${sysweixin.weixintoken}
				</td>
				<td>
					${sysweixin.mchid}
				</td>
				<td>
					${sysweixin.apikey}
				</td>
				<td>
					<shiro:hasPermission name="sysweixin:sysweixin:view">
						<a href="#" onclick="openDialogView('查看公众号信息', '${ctx}/sysweixin/sysweixin/form?id=${sysweixin.id}','900px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sysweixin:sysweixin:edit">
    					<a href="#" onclick="openDialog('修改公众号信息', '${ctx}/sysweixin/sysweixin/form?id=${sysweixin.id}','900px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="sysweixin:sysweixin:del">
						<a href="${ctx}/sysweixin/sysweixin/delete?id=${sysweixin.id}" onclick="return confirmx('确认要删除该公众号信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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