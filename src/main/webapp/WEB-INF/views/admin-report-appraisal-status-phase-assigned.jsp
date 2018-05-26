<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.css" />
<!-- Animation Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css" />
<!-- Sweetalert Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css" />
<!-- JQuery DataTable Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css" />
<!-- Custom Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
<!-- Bootstrap Select Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css" />
</head>
</head>

<body class="theme-red">
  <!-- Header -->
  <%@include file="common/header.jsp"%>
  <!-- =============================================== -->
  <!-- Left side column. contains the sidebar -->
  <%@include file="common/menu.jsp"%>
  <!-- =============================================== -->
  <section class="content">
	<div class="container-fluid">
	  <div class="block-header">
		<h2>Appraisal Status Report (Phase-wise - Assigned) <small>This report lists appraisal status of all the employees for whom template has been assigned.</small></h2>
	  </div>
	<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	  <div class="card">
		<div class="body">
		  <div class="row clearfix">
			<div class="col-sm-2">Cycle Id</div>
			  <div class="col-sm-2">
				<select id="cyclesPhaseSelectId" class="form-control show-tick">
				  <option value="-1">&nbsp;</option>
				</select>
			  </div>
			  <div class="col-sm-2">&nbsp;</div>
				<div class="col-sm-2">Phase Id</div>
				  <div class="col-sm-2">
					<select id="phaseSelectId" class="form-control show-tick">
					  <option value="-1">&nbsp;</option>
					</select>
				  </div>
				  <div class="col-sm-2">
					<button type="button" id="SearchPhase" class="btn bg-orange btn-circle waves-effect waves-circle waves-float">
					  <i class="material-icons">search</i>
					</button>
				  </div>
				  <div class="col-sm-2">&nbsp;</div>
				</div>
				<div class="row clearfix">
				  <div class="table-responsive">
					<table id="SearchPhaseTable" class="table table-bordered table-striped table-hover dataTable">
					  <thead>
						<tr>
						  <th>Id</th>
						  <th>Employee</th>
						  <th>Manager</th>
						  <th>Template</th>
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
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/js/bootstrap-select.js"></script>
	<!-- Slimscroll Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
	<!-- Sweet Alert Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
	<!-- Waves Effect Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
	<!-- Jquery DataTable Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
	<!-- Autosize Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
	<!-- Moment Plugin Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
	<!-- Custom Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
	<!-- Demo Js -->
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>

	<script	src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
	<script	src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>

	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/dataTables.buttons.min.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.flash.min.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.print.min.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/jszip.min.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/pdfmake.min.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/vfs_fonts.js"></script>
	<script	src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/buttons.html5.min.js"></script>
	<script>
  $(function () {
    var url = '<%=request.getContextPath()%>/api/appraisal/list/';
    $.get(url, {sid: new Date().getTime()}, function() {}).done(function(result) {
	  $.each(result,function(index, value) {
		$('#cyclesPhaseSelectId').append('<option value="'+value.id+'">'+value.name+'</option>');
		$("#cyclesPhaseSelectId").selectpicker("refresh");
	  });
    });

    $("#cyclesPhaseSelectId").change(function() {
      var id = $('option:selected', this).val();
      if (id != 0) {
       	$('#phaseSelectId').html('');
       	$('#phaseSelectId').append('<option value="-1">&nbsp;</option>');
	      var url_phase = '<%=request.getContextPath()%>/api/appraisal/list/' + id;
	      $.get(url_phase, {sid: new Date().getTime()}, function() {})
	      .done(function(result) {
	        if (result) {
		   	  $(result.phases).each(function(index, phase) {
		   	    $('#phaseSelectId').append('<option value="'+phase.id+'">'+phase.name+'</option>');
		   	    $("#phaseSelectId").selectpicker("refresh");
		   	  });
	        }
	      });
	      $("#phaseSelectId").selectpicker("refresh");
        }
    });
    var cycleId='0';
	var phaseId='0';
	$('#SearchPhaseTable').DataTable({
      responsive: true,
      paging: true,
		searching: true,
		ordering: true,
		info: true,
      "ajax": "<%=request.getContextPath()%>/api/admin/report/phase/" + cycleId + "/" + phaseId,
      "sAjaxDataProp":"",
		"columns": [
          { "data": "assignedTo.employeeId" },
          { "data": "assignedTo.fullName" },
          { "data": "assignedBy" },
          { "data": "template.name" },
          { "data": "assignedAt" },
          { "data": "status" },
      ],
      columnDefs: [
    	  { 
              targets: 2,
              searchable: true,
              orderable: true,
              render: function(data, type, full, meta){
                 return data.employeeId + '-'+data.fullName;
              }
          },
          { 
              targets: 5,
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

  $('#SearchPhase').click(function() {
    var cyclesPhaseSelectId=$('#cyclesPhaseSelectId');
    var phaseSelectId=$('#phaseSelectId');
    cycleId=$.trim(cyclesPhaseSelectId.val());
    phaseId=$.trim(phaseSelectId.val());
    if (cycleId ==-1) {
      $(cyclesPhaseSelectId).focus();
    } else {
	  var table = $('#SearchPhaseTable').DataTable();
	  if (phaseId > 0) {
	    table.ajax.url("<%=request.getContextPath()%>/api/admin/report/phase/" + cycleId + "/" + phaseId).load();
	  } else {
	    $(phaseSelectId).focus();
	  }
	}
  });
});
</script>
</body>
</html>