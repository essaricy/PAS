<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>Softvision | PMS</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/favicon.ico">
    <!-- Bootstrap Core Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/css/bootstrap.css">
    <!-- Waves Effect Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.css"/>
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
        <h2>SYNC Employee Data
          <small>SYNC Employees data from upstream system i.e. SVProject</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">

              <div class="row clearfix m-l-20">
                <blockquote>
                  <p>This features can be used to Sync all the employee information from SV project to this system.</p>
                  <p>After SYNC</p>
                  <footer class="font-bold col-green">New employees will be added</footer>
                  <footer class="font-bold col-orange">Existing employees will be updated</footer>
                  <footer class="font-bold col-red">Terminated employees will be deactivated</footer>
                </blockquote>
              </div>
              <div class="row clearfix">
                <div class="col-lg-4 col-md-4 col-sm-3 col-xs-2">&nbsp;</div>
                <div class="col-lg-4 col-md-4 col-sm-6 col-xs-8">
               	  <button type="button" id="Sync" class="btn btn-lg btn-primary waves-effect">
               	    <i class="material-icons">sync</i>
               	    <span>Start SYNC</span>
               	  </button>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-3 col-xs-2">&nbsp;</div>
              </div>
              <div class="row clearfix sync_result_row">
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                  <button id="Show_Sync_Results_Success" class="btn btn-success waves-effect" type="button">Successful<span class="badge"></span></button>
                </div>
				<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                  <button id="Show_Sync_Results_Error" class="btn btn-warning waves-effect pull-right" type="button">Errors<span class="badge"></span></button>
                </div>
              </div>
              <div class="row clearfix sync_result_row">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                  <ul class="list-group" id="Sync_Results_Success" style="display: none;">
		          </ul>
		          <ul class="list-group" id="Sync_Results_Error" style="display: none;">
		          </ul>
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
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
  <!-- Waves Effect Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
  <!-- Autosize Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script>
  $(function () {
    $('.sync_result_row').hide();
    $('#Sync').click(function() {
	  swal({
    	title: "Are you sure?", text: "Do you want to sync data of all employees from SV Project to this system? This may take few minutes!!", type: "warning",
    	showCancelButton: true, confirmButtonColor: "#DD6B55",
    	confirmButtonText: "Yes, Proceed!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
	    $('.sync_result_row').hide();
    	$.fn.ajaxPut({
    	  url: '<%=request.getContextPath()%>/employee/sync',
    	  //refresh: "no",
    	  onSuccess: function (message, result) {
    		var results_container_error=$('#Sync_Results_Error');
    		var results_container_Success=$('#Sync_Results_Success');
      		$(results_container_error).empty();
      		$(results_container_Success).empty();
      		$("#sync_employee_tab").show();
      		$(result).each(function(index, aResult) {
      		  if (aResult.code=='SUCCESS') {
      		    $(results_container_Success).append('<li class="list-group-item bg-green">' + aResult.message + '<span class="pull-right"><i class="material-icons">done</i></span></li>');
      		  } else {
      			$(results_container_error).append('<li class="list-group-item bg-red">' + aResult.message + '<span class="pull-right"><i class="material-icons">error_outline</i></span></li>');
      		  }
      		});
       	  	swal({ title: "Sync Completed!", text: "", type: "success"}, function () { }); 
    	  },
    	  onComplete : function () {
    		$('.sync_result_row').show();
    		$('#Show_Sync_Results_Error').find('.badge').text($('#Sync_Results_Error').find('li').length);
    		$('#Show_Sync_Results_Success').find('.badge').text($('#Sync_Results_Success').find('li').length);
	      }
    	});
      });
    });

    $('#Show_Sync_Results_Success').click(function() {
    	$('#Sync_Results_Success').show();
    	$('#Sync_Results_Error').hide();
    });
    $('#Show_Sync_Results_Error').click(function() {
    	$('#Sync_Results_Success').hide();
    	$('#Sync_Results_Error').show();
    });
  });
  </script>
</body>
</html>