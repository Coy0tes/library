<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>图书分类管理</title>
	<meta name="decorator" content="default"/>
	<%-- <link rel="stylesheet" href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css">  
	<script src="${ctx}/jquery/jquery-2.1.1.min.js"></script>
	<script src="${ctxStatic}/bootstrap/3.3.4/js/bootstrap.min.js"></script> --%>
	
	
	<script type="text/javascript">
		$(document).ready(function() {
			
			// 鼠标移到之后变色
			$(".panel-default").mouseover(function(){
				$(this).css("background-color","#adf");
			});
			
			$(".panel-default").mouseout(function(){
				$(this).css("background-color","white");
			});
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>图书分类列表 </h5>
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
	<ol class="breadcrumb">
			    <li>
			 	   <a href="${ctx}/booktype/bookType/nextList?parent=${bookType.id }" title="分类号查询"><font style="color:blue">分类号首页</font></a>
			    </li>
			    <c:forEach items="${topList }" var="top">
				    ${top }
			    </c:forEach>
		    </ol>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="bookType" action="${ctx}/booktype/bookType/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
<%-- 			<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/> --%>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="booktype:bookType:add">
				<table:addRow url="${ctx}/booktype/bookType/form" title="图书分类"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="booktype:bookType:edit">
			    <table:editRow url="${ctx}/booktype/bookType/form" title="图书分类" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="booktype:bookType:del">
				<table:delRow url="${ctx}/booktype/bookType/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="booktype:bookType:import">
				<table:importExcel url="${ctx}/booktype/bookType/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="booktype:bookType:export">
	       		<table:exportExcel url="${ctx}/booktype/bookType/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <!-- <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button> -->
		
			</div>
		<!-- <div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div> -->
	</div>
	</div>
	<div class="container">
		<div class="section category-items text-center">
		    <div class="section-title">
		        <h2>图书馆分类号</h2>
		        <br>
		    </div>
		    <div class="row category-list">
		    <c:forEach items="${list }" var="bookType">
		    	<div class="col-sm-3 panel panel-default categorydiv">
			        <a  href="${ctx}/booktype/bookType/nextList?parent=${bookType.id }" title="${bookType.remarks }">
					    <div class="category-item panel-body" class="mousesj">
				    		<span hidden="hidden">${bookType.id }</span>
				            <div><span class="category-code">${bookType.name }</span></div>
				            <span class="category-title text-overflow">${fns:abbr(bookType.remarks,30)}</span>
					    </div>
			        </a>
				</div>
				
				<div class="col-sm-1"></div>
				
		    </c:forEach>
		    
		   <ins class="adsbygoogle" style="display:block" data-ad-format="fluid" data-ad-layout-key="-ft+6a+q-di+m7" data-ad-client="ca-pub-8807481079649877" data-ad-slot="1379660048"></ins>
		   <script>
		       (adsbygoogle = window.adsbygoogle || []).push({});
		   </script>
		</div>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>