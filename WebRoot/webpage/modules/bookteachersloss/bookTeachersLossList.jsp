<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>遗失赔偿管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var data = ${fns:toJson(bookStudentDetail)};
			console.info(data);
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>遗失赔偿管理列表 </h5>
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
	<form:form id="searchForm" modelAttribute="bookStudentDetail" action="${ctx}/bookteachersloss/bookteachersloss/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>ISBN编号：</span>
				<form:input path="isbn" htmlEscape="false" maxlength="64" placeholder="请输入完整的ISBN号" class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>索书号：</span>
				<form:input path="ssh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>书名：</span>
				<form:input path="name" htmlEscape="false" maxlength="500"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>借出时间：</span>
				<form:input path="jcsj" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
		</div>
		<div style="padding-top:3px">
			<span>教师姓名：</span>
				<form:input path="teachersname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
			<span>学号：</span>
				<form:input path="teacherbh" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
				&nbsp;&nbsp;
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="bookteachersloss:bookteachersloss:add">
				<table:addRow url="${ctx}/bookteachersloss/bookteachersloss/form" title="遗失赔偿管理" height="85%" width="85%"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="bookteachersloss:bookteachersloss:edit">
			    <table:editRow url="${ctx}/bookteachersloss/bookteachersloss/form" title="遗失赔偿管理" id="contentTable" height="85%" width="85%"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission> --%>
			<shiro:hasPermission name="bookteachersloss:bookteachersloss:del">
				<table:delRow url="${ctx}/bookteachersloss/bookteachersloss/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookteachersloss:bookteachersloss:import">
				<table:importExcel url="${ctx}/bookteachersloss/bookteachersloss/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bookteachersloss:bookteachersloss:export">
	       		<table:exportExcel url="${ctx}/bookteachersloss/bookteachersloss/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column teachersname">教师姓名</th>
				<th  class="sort-column teacherbh">工号</th>
				<th  class="sort-column isbn">ISBN编号</th>
				<th  class="sort-column ssh">索书号</th>
				<th  class="sort-column booktype">图书分类</th>
				<th  class="sort-column name">书名</th>
				<th  class="sort-column author">作者</th>
				<th  class="sort-column cbs">出版社</th>
				<th  class="sort-column price">价格</th>
				<th  class="sort-column jcsj">借出时间</th>
				<th  class="sort-column yhrq">应还日期</th>
				<th  class="sort-column ghsj">归还时间</th>
				<th  class="sort-column pcsj">赔偿时间</th>
				<th  class="sort-column pcje">赔偿金额</th>
				<th  class="sort-column cqts">超期天数</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="bookStudentDetail">
			<tr>
				<td> <input type="checkbox" id="${bookStudentDetail.id}" class="i-checks"></td>
				<td>
					${bookStudentDetail.teachersname}
				</td>
				<td>
					${bookStudentDetail.teacherbh}
				</td>
				<td>
					${bookStudentDetail.isbn}
				</td>
				<td>
					${bookStudentDetail.ssh}
				</td>
				<td>
					${bookStudentDetail.booktype}
				</td>
				<td>
					${bookStudentDetail.name}
				</td>
				<td>
					${bookStudentDetail.author}
				</td>
				<td>
					${bookStudentDetail.cbs}
				</td>
				<td>
					${bookStudentDetail.price}
				</td>
				<td>
					${bookStudentDetail.jcsj}
				</td>
				<td>
					${bookStudentDetail.yhrq}
				</td>
				<td>
					${bookStudentDetail.ghsj}
				</td>
				<td>
					${bookStudentHistory.pcsj}
				</td>
				<td>
					${bookStudentHistory.pcje}
				</td>
				<td>
					${bookStudentDetail.cqts}
				</td>
				<td>
					<shiro:hasPermission name="bookteachersloss:bookteachersloss:view">
						<a href="#" onclick="openDialogView('查看遗失赔偿管理', '${ctx}/bookteachersloss/bookteachersloss/form?id=${bookStudentDetail.id}','85%', '85%')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<%-- <shiro:hasPermission name="bookteachersloss:bookteachersloss:edit">
    					<a href="#" onclick="openDialog('修改遗失赔偿管理', '${ctx}/bookteachersloss/bookteachersloss/form?id=${bookStudentDetail.id}','85%', '85%')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission> --%>
    				<c:if test="${bookStudentDetail.remarks != '1'}">
	    				<shiro:hasPermission name="bookteachersloss:bookteachersloss:edit">
	    					<a href="${ctx}/bookteachersloss/bookteachersloss/loss?id=${bookStudentDetail.id}" onclick="return confirmx('确认该图书遗失？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 图书遗失</a>
	    				</shiro:hasPermission>
    				</c:if>
    				<shiro:hasPermission name="bookteachersloss:bookteachersloss:edit">
    					<a href="#" onclick="openDialog('赔偿管理', '${ctx}/bookteachersloss/bookteachersloss/form?id=${bookStudentDetail.id}','85%', '85%')" class="btn btn-success btn-xs" style="background-color:green" > 赔偿图书</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bookteachersloss:bookteachersloss:del">
						<a href="${ctx}/bookteachersloss/bookteachersloss/delete?id=${bookStudentDetail.id}" onclick="return confirmx('确认要删除该遗失赔偿管理吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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