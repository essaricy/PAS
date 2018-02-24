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
    <!-- JQuery DataTable Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css"/>
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
        <h2>Employee Management
          <small>Add, Lookup and Sync employees from SV Project</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
              <!-- Nav tabs -->
              <ul class="nav nav-tabs tab-nav-right tab-col-deep-orange" role="tablist">
                <li role="presentation" class="active"><a href="#Lookup_Employees" data-toggle="tab">LOOK UP</a></li>
                <li role="presentation"><a href="#Sync_Employees" data-toggle="tab">Sync Employees</a></li>
              </ul>
               <!-- Tab panes -->
               <div class="tab-content">
                 <div role="tabpanel" class="tab-pane fade in active" id="Lookup_Employees">
                   <div class="row clearfix">
                     <div class="col-sm-2">&nbsp;</div>
                     <div class="col-sm-6">
                       <div class="form-group">
                         <div class="form-line">
                           <input type="text" id="SearchBy" class="form-control" placeholder="Enter part of employee's first name or last name" autofocus />
                         </div>
                         <div class="help-info">Min. 3 characters</div>
                       </div>
                     </div>
                     <div class="col-sm-2">
                       <button type="button" id="Search" class="btn bg-orange btn-circle waves-effect waves-circle waves-float">
                         <i class="material-icons">search</i>
                       </button>
                     </div>
                     <div class="col-sm-2">&nbsp;</div>
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
                             <th>Location</th>
                           </tr>
                         </thead>
                         <tbody>
                         </tbody>
                       </table>
                     </div>
                   </div>
                 </div>
                 <div role="tabpanel" class="tab-pane fade" id="Sync_Employees">
                   <p>This features can be used to Sync all the employee information from SV project to this system.</p>
                 </div>
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <!-- Jquery Core Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery/jquery.min.js"></script>
  <!-- Bootstrap Core Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/js/bootstrap.js"></script>
  <!-- Select Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/js/bootstrap-select.js"></script>
  <!-- Slimscroll Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
  <!-- Jquery Validation Plugin Css -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
  <!-- JQuery Steps Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-steps/jquery.steps.js"></script>
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
  <!-- Waves Effect Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
  <!-- Jquery DataTable Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js"></script> --%>
  <!-- Autosize Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
  <!-- Moment Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
  <!-- Bootstrap Material Datetime Picker Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>

  <script>
  $(function () {
	var searchText='qqqq';
	$('#SearchTable').DataTable({
        responsive: true,
        paging: false,
		searching: false,
		ordering: true,
		info: true,
        "ajax": "<%=request.getContextPath()%>/employee/search/" + searchText,
        "sAjaxDataProp":"",
		"columns": [
            { "data": "EmployeeId" },
            { "data": "FirstName" },
            { "data": "LastName" },
            { "data": "EmploymentType" },
            { "data": "Band" },
            { "data": "Designation" },
            { "data": "HiredOn" },
            { "data": "Location" }
        ],
    });

    $('#Search').click(function() {
      var searchBy=$('#SearchBy');
      searchText=$.trim(searchBy.val());
      if (searchText.length < 3) {
    	$(searchText).focus();
      } else {
    	console.log('searchText=' + searchText);
    	var table = $('#SearchTable').DataTable();
    	//table.ajax.reload();
    	table.ajax.url("<%=request.getContextPath()%>/employee/search/" + searchText).load();
      }
    });
  });
  </script>
</body>
</html>