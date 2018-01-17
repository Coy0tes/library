<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>图书馆管理系统</title>
	<%@ include file="/webpage/include/head.jsp"%>
	<script src="${ctxStatic}/common/inspinia.js?v=3.2.0"></script>
	<script src="${ctxStatic}/common/contabs.js"></script> 
    <meta name="keywords" content="图书馆管理系统">
    
    <script type="text/javascript">
	$(document).ready(function() {
		 if('${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}' == '天蓝主题'){
		    // 蓝色主题
	        $("body").removeClass("skin-2");
	        $("body").removeClass("skin-3");
	        $("body").addClass("skin-1");
		 }else  if('${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}' == '橙色主题'){
		    // 黄色主题
	        $("body").removeClass("skin-1");
	        $("body").removeClass("skin-2");
	        $("body").addClass("skin-3");
		 }else {
			// 默认主题
	        $("body").removeClass("skin-2");
	        $("body").removeClass("skin-3");
	        $("body").removeClass("skin-1");
		 };
	 });
			
	</script>
	
	<script type="text/javascript">
		$(function(){
			$(".navbar-default .nav > li > a").click(function(){
				$(".navbar-default .nav > li > a").removeClass("active");
				$(this).addClass("active");
			});
		});
	</script>

</head>

<body class="fixed-sidebar full-height-layout gray-bg">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <%-- 
	                        <div class="dropdown profile-element">
	                            <span><img alt="image" class="img-circle" style="height:64px;width:64px;" src="${fns:getUser().photo }" /></span>
	                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
	                                <span class="clear">
	                               <span class="block m-t-xs"><strong class="font-bold">${fns:getUser().name}</strong></span>
	                               <span class="text-muted text-xs block">${fns:getUser().roleNames}<b class="caret"></b></span>
	                                </span>
	                            </a>
	                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
	                                <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a>
	                                </li>
	                                <li><a href="${ctx}/logout">安全退出</a>
	                                </li>
	                            </ul>
	                        </div> 
                        --%>
                        <h2 style="color:white">会泽县以礼中学图书室管理系统</h2>
                        <div class="logo-element">JP
                        </div>
                    </li>
     
                    <t:menu  menu="${fns:getTopMenu()}"></t:menu>

                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header">
                    	<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#">
                    		<i class="fa fa-bars"></i> 
                    	</a>
				       	
                    </div>
                    <ul class="nav navbar-top-links navbar-right" style="margin-right:20px;">
                        <li class="dropdown" style="margin-left:10px;">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                <i class="fa fa-bell"></i> <span class="label label-primary">${fn:length(lists)}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-alerts">
                             	<c:forEach items="${lists}" var="p">
	                          		 <li>
	                                     <div class="text-center link-block">
	                          				<font style="color:#18a689;font-weight:bold;">${p.title}</font>
	                          				&nbsp;&nbsp;&nbsp;&nbsp;
	                          				${p.date }
	                          				<a class="J_menuItem" href="${ctx}${p.url}">
	                                          <strong>查看</strong>
	                                          <i class="fa fa-angle-right"></i>
	                                      </a>
	                                     </div>
	                                 </li>
                        		</c:forEach>
                            	<c:if test="${fn:length(lists) == 0}">
                            		暂无数据
                            	</c:if>
                                <%-- <li>
                                    <div class="text-center link-block">
                                                                                                             您有${count}条未读消息 <a class="J_menuItem" href="">
                                            <strong>查看所有 </strong>
                                            <i class="fa fa-angle-right"></i>
                                        </a>
                                    </div>
                                </li> --%>
                            </ul>
                        </li>
                        <li class="dropdown" style="margin-left:10px;">
                        	<span>
                        		<img alt="image" class="img-circle" style="height:50px;width:50px;" src="${fns:getUser().photo }" />
                        	</span>
                        </li>
                        
                        <%-- <li style="margin-left:10px;">
                        	<span class="text-muted text-xs block">角色：${fns:getUser().roleNames}</span>
                        </li> --%>
                        
                        <li class="dropdown" style="margin-left:10px;">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                 <span class="text-muted text-xs block">${fns:getUser().name}<b class="caret"></b></span>
                            </a>
                            <ul class="dropdown-menu">
                               <%--  <li>
                                	<a href="#">${fns:getUser().roleNames}</a>
                                </li> --%>
                                <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a>
                                </li>
                                <li><a href="${ctx}/logout">安全退出</a>
                                </li>
                            </ul>
                        </li>
                        
                         <li class="dropdown" style="margin-left:10px;">
                            <a href="${ctx}/logout" class=" roll-right">退出</a>
                         </li>
                      
                    </ul>
                </nav>
            </div>
            <div class="row content-tabs" style="display: none;">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab" data-id="${ctx}/home">首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose"  data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="${ctx}/logout" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/home" frameborder="0" data-id="${ctx}/home" seamless></iframe>
            </div>
            <div class="footer">
            </div>
        </div>
        <!--右侧部分结束-->
       
    </div>
</body>

</html>