<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>登录</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="beijing">
  	
  	<div >
  		<div class="height80"></div>
    <div class="container beijingk">
    	<div class="beijing2 text-center">
    		<img src="img/beijing2.png" />
    	</div>
    	<div class="denglu">
    		<h3>用户登录</h3>
    		<div class="fenxian"></div>
    		<div class="input-group input_l">
									<span class="denglutu">
    									<img src="img/login-user2.png" alt=""><i></i>
    								</span>
									<input type="text" class="form-control"style="width: 89%;"  placeholder="请输入用户名或手机号">
									
								</div>
								<div class="input-group input_l">
									<span class="denglutu">
    									<img src="img/login-user3.png" alt=""><i></i>
    								</span>
									<input type="text" class="form-control"style="width: 90%;"   placeholder="请输入密码">
								</div>
								<div class="imm-btn"><a class="click-on" href="javascript:void(0)" id="lgBtn">立即登录</a></div>
    	</div>
    </div>
	</div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>