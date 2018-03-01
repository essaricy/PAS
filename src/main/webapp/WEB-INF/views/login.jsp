<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>Softvision | PMS</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/favicon.ico">
    <!-- Google Fonts -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/fonts/google-Roboto.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/fonts/google-Material-Icons.css">
    <!-- Bootstrap Core Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/css/bootstrap.css">
    <!-- Waves Effect Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.css"/>
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
  </head>
  <body class="login-page">
    <div class="login-box">
      <div class="logo">
        <a href="javascript:void(0);"><b>SOFTVISION</b></a>
        <small>Admin BootStrap Based - Material Design</small>
      </div>
      <div class="card">
        <div class="body">
          <form method="POST" action="<%=request.getContextPath()%>/login">
            <div class="msg">Sign in to start your session</div>
            <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
            	<font color="red"> Your login attempt was not successful due to <br />
            	<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
            	</font>
			</c:if>
			<div class="input-group">
              <span class="input-group-addon">
                <i class="material-icons">person</i>
              </span>
              <div class="form-line">
                <input type="text" class="form-control" name="username" placeholder="Username" required autofocus value="mallikarjun.gongati">
              </div>
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <i class="material-icons">lock</i>
              </span>
              <div class="form-line">
                <input type="password" class="form-control" name="password" placeholder="Password" required value="$oftvision@123">
              </div>
            </div>
            <div class="row">
              <div class="col-xs-8 p-t-5">
                &nbsp;
              </div>
              <div class="col-xs-4 align-right">
                <button class="btn btn-block bg-pink waves-effect" type="submit">SIGN IN</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Jquery Core Js -->
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery/jquery.min.js"></script>
    <!-- Bootstrap Core Js -->
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/js/bootstrap.js"></script>
    <!-- Waves Effect Plugin Js -->
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
    <!-- Validation Plugin Js -->
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
    <!-- Custom Js -->
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  </body>
</html>