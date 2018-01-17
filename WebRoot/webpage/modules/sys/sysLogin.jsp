<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

	<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>登录</title>

    <!-- Bootstrap -->
    <link href="${ctxStatic}/sysLogin/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctxStatic}/sysLogin/css/style.css" rel="stylesheet">
    <script src="${ctxStatic}/jquery/jquery-2.1.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/layer-v2.3/layer/layer.js"></script>
    <script type="text/javascript">
    	$(function(){
    		$('#username').keypress(function(e){
				if(e.keyCode == 13){
					$('#password').focus();
				}
			});
			$('#password').keypress(function(e){
				if(e.keyCode == 13){
					dosubmit();
				}
			});
    	});
    
    	function dosubmit(){
    		
    		// 非空校验
    		var username = $("#username").val();
    		var pwd = $("#password").val();
    		
    		if(username == ""){
    			top.layer.msg("用户名不能为空！");
    			return false;
    		}
    		
    		if(pwd == ""){
    			top.layer.msg("密码不能为空！");
    			return false;
    		}
    		$('#loginForm').submit();
    		return true;
    	}
    	
    </script>
  </head>
  <body class="beijing">
  	
  	<div >
  		<div class="height80"></div>
    <div class="container beijingk">
    	<div class="beijing2 text-center">
    		<img src="${ctxStatic}/sysLogin/img/beijing2.png" />
    	</div>
    	<div class="denglu">
    		<h3>用户登录</h3>
    		<div class="fenxian"></div>
    		<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		    		<div class="input-group input_l">
						<span class="denglutu">
										<img src="${ctxStatic}/sysLogin/img/login-user2.png" alt=""><i></i>
									</span>
						<input type="text"  id="username" name="username"  value="" class="form-control"style="width: 89%;"  placeholder="请输入用户名或手机号">
						
					</div>
					<div class="input-group input_l">
						<span class="denglutu">
										<img src="${ctxStatic}/sysLogin/img/login-user3.png" alt=""><i></i>
									</span>
						<input type="password" id="password" name="password" value="" class="form-control"style="width: 90%;"   placeholder="请输入密码">
					</div>
					<div class="imm-btn"><a onclick="dosubmit();" style="text-decoration: none;" class="" href="#" id="lgBtn"><font style="color:white">立即登录</font></a></div>
					<div style="margin-top:10px;">
						<c:if test="${not empty message }"><font color="color:red;">${message}</font></c:if>
					</div>
			</form>
    	</div>
    </div>
	</div>
  </body>
  
</html>
