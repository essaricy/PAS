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
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css" />
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
    <!-- JQuery DataTable Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
     <!-- Bootstrap Select Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css"  />
    <!-- EasyAutocomplete-1.3.5 -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/easy-autocomplete.min.css"/>
    
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
        <h2>Appraisal Status Report (Cycle)
          <small>This report lists appraisal status of all the employees at the cycle-level.</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
			  <div class="row clearfix">
	            <div class="col-sm-2">&nbsp;</div>
	            <div class="col-sm-6">
				  <select id="cyclesSelectId" class="form-control show-tick">
	                <option value="-1"> Please select </option>
	              </select> 
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
                        <th>Id</th>
                        <th>Employee</th>
                        <th>Manager</th>
                        <th>Assigned On</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
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
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-card.js"></script>
  <!-- EasyAutocomplete-1.3.5 -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/jquery.easy-autocomplete.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/dataTables.buttons.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.flash.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.print.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/jszip.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/pdfmake.min.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/vfs_fonts.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.html5.min.js"></script>
  

  <script>
  $(function () {
    var url = '<%=request.getContextPath()%>/appraisal/list/';
    $.get(url, {sid: new Date().getTime()}, function() {}).done(function(result) {
	  $.each(result,function(index, value) {
		var flag=(value.status=="ACTIVE")?'Selected':' ';
		$('#cyclesSelectId').append('<option value="'+value.id+'"'+flag+'>'+value.name+'</option>');
		$("#cyclesSelectId").selectpicker("refresh");
	  });
    });

	var cycleId='0';
	$('#SearchTable').DataTable({
      responsive: true,
      paging: true,
	  searching: true,
	  ordering: true,
	  info: true,
      "ajax": "<%=request.getContextPath()%>/assignment/admin/cycle/" + cycleId,
      "sAjaxDataProp":"",
	  "columns": [
        { "data": "assignedTo.employeeId" },
        { "data": "assignedTo.fullName" },
        { "data": "assignedBy.fullName" },
        { "data": "assignedAt" },
        { "data": "status" },
      ],
      columnDefs: [
    	{
    		targets: 4,
            searchable: true,
            orderable: true,
            render: function(data, type, full, meta){
              return getPhaseStatusLabel(data);
            }
          }
        ],
        dom: 'Bfrtip',
        buttons: [
            'excel', 'print'
        ]
    });

    $('#Search').click(function() {
      var searchBy=$('#cyclesSelectId');
      cycleId=$.trim(searchBy.val());
      if (cycleId ==-1) {
        $(cycleId).focus();
      } else {
    	var table = $('#SearchTable').DataTable();
    	table.ajax.url("<%=request.getContextPath()%>/assignment/admin/cycle/" + cycleId).load();
      }
    });
  });
  </script>
</body>
</html>