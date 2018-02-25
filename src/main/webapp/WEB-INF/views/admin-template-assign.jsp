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
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css" />
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
  </head>
  <style>
  .list-group-item {
  	font-size: 13px;
  }
  </style>
</head>

<body class="theme-red">
  <!-- Header -->
  <%@include file="common/header.jsp" %>
  <!-- =============================================== -->

  <!-- Left side column. contains the sidebar -->
  <%@include file="common/menu.jsp" %>
  <!-- =============================================== -->

  <section class="content">
    <div class="container-fluid">
      <div class="block-header">
        <h2>Assign Competency Assessment Templates to Employees
          <small>Assign templates to employee(s)</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-50 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
              <div class="row clearfix">
			    <div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
                  <label for="Template_Name">Template Name</label>
                </div>
                <div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
		          <div class="form-group form-float">
		            <div class="form-line">
		              <input type="text" id="Template_Name" class="form-control" minlength="3" placeholder="Please enter the name" required autofocus value="Template 1">
                    </div>
                  </div>
                </div>
              </div>
              <div class="row clearfix">
                <div class="col-md-3">
                  <b>Employee Name</b>
                  <div class="input-group">
                    <div class="form-line">
                      <input type="text" class="form-control date" placeholder="Employee First or Last name">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <b>Project</b>
                  <div class="input-group">
                    <div class="form-line">
                      <input type="text" class="form-control date" placeholder="Project name">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <b>Designation</b>
                  <div class="input-group">
                    <div class="form-line">
                      <input type="text" class="form-control date" placeholder="Designation">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <b>Band</b>
                  <div class="input-group">
                    <div class="form-line">
                      <input type="text" class="form-control date" placeholder="Band">
                    </div>
                  </div>
                </div>
              </div>
              <div class="row clearfix">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-24">
                  <div class="text-right">
					<button type="button" class="btn btn-primary btn-sm m-l-15 waves-effect">Search</button>
			      </div>
                </div>
              </div>
              <div class="row clearfix">
                <div class="table-responsive">
                  <table id="SearchTable" class="table table-bordered table-striped table-hover dataTable">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Employment Type</th>
                        <th>Band</th>
                        <th>Designation</th>
                        <th>Hired On</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>1111</td>
						<td>Atul</td>
						<td>Kumar</td>
						<td>Regular Employee</td>
						<td>2Z</td>
						<td>Software Engineer</td>
						<td>2015/03/11</td>
				      </tr>
                      <tr>
                        <td>1111</td>
						<td>Atul</td>
						<td>Kumar</td>
						<td>Regular Employee</td>
						<td>2Z</td>
						<td>Software Engineer</td>
						<td>2015/03/11</td>
				      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</body>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap Core Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/js/bootstrap.js"></script>
<!-- Select Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/js/bootstrap-select.js"></script>
<!-- Slimscroll Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
<!-- Waves Effect Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
<!-- Autosize Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
<!-- Moment Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
<!-- Bootstrap Material Datetime Picker Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
<!-- SweetAlert Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
<!-- Validation Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script>
</script>
</html>
