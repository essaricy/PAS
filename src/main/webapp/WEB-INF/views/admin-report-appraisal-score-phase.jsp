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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-rateyo/jquery.rateyo.min.css"/>
<!-- JQuery DataTable Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css" />
<!-- Custom Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
<!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
<!-- Bootstrap Select Css -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css" />
<style>
.self-score, .manager-score, .assess-heading {
  font-weight: bold;
}
</style>
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
		<h2>All-Inclusive Report
		  <small>This report lists scores of all employees in an appraisal phase. We can view the ratings and comments of both employees and managers.</small>
		</h2>
	  </div>
	<div class="row clearfix">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	  <div class="card">
		<div class="body">
		  <div class="row clearfix">
			<div class="col-sm-2">Cycle Id</div>
			  <div class="col-sm-2">
				<select id="SelectedCycleId" class="form-control show-tick">
				  <option value="-1">&nbsp;</option>
				</select>
			  </div>
			  <div class="col-sm-2">&nbsp;</div>
				<div class="col-sm-2">Phase Id</div>
				  <div class="col-sm-2">
					<select id="SelectedPhaseId" class="form-control show-tick">
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
					<table id="AllEmployeeScoreTable" class="table table-bordered table-striped table-hover dataTable">
					  <thead>
						<tr>
						  <th>Employee Id</th>
						  <th>Employee Name</th>
						  <th>Manager Id</th>
						  <th>Manager Name</th>
						  <th>Status</th>
						  <th>Employee Score</th>
						  <th>Manager Score</th>
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


    <!-- Large Size -->
    <div class="modal" id="AssessmentModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-body">
            <div class="card assessment_card">
              <div class="header">
              </div>
              <div class="body">
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

    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-rateyo/jquery.rateyo.min.js"></script>
    <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/ckeditor/ckeditor.js"></script>
    <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-assessments3.js"></script>

	<script>
  $(function () {
    var url = '<%=request.getContextPath()%>/api/appraisal/list/';
    $.get(url, {sid: new Date().getTime()}, function() {}).done(function(result) {
	  $.each(result,function(index, value) {
		$('#SelectedCycleId').append('<option value="'+value.id+'">'+value.name+'</option>');
		$("#SelectedCycleId").selectpicker("refresh");
	  });
    });

    $("#SelectedCycleId").change(function() {
      var id = $('option:selected', this).val();
      if (id != 0) {
       	$('#SelectedPhaseId').html('');
       	$('#SelectedPhaseId').append('<option value="-1">&nbsp;</option>');
	      var url_phase = '<%=request.getContextPath()%>/api/appraisal/list/' + id;
	      $.get(url_phase, {sid: new Date().getTime()}, function() {})
	      .done(function(result) {
	        if (result) {
		   	  $(result.phases).each(function(index, phase) {
		   	    $('#SelectedPhaseId').append('<option value="'+phase.id+'">'+phase.name+'</option>');
		   	    $("#SelectedPhaseId").selectpicker("refresh");
		   	  });
	        }
	      });
	      $("#SelectedPhaseId").selectpicker("refresh");
        }
    });

    var cycleId='0';
	var phaseId='0';
	$('#AllEmployeeScoreTable').DataTable({
      responsive: true,
      paging: true,
		searching: true,
		ordering: true,
		info: true,
      "ajax": "<%=request.getContextPath()%>/api/admin/report/phase/score/" + cycleId + "/" + phaseId,
      "sAjaxDataProp":"",
		"columns": [
          { "data": "assignedTo.employeeId" },
          { "data": "assignedTo.fullName" },
          { "data": "assignedBy.employeeId" },
          { "data": "assignedBy.fullName" },
          { "data": "status" },
          { "data": "score" },
          { "data": "managerScore" },
      ],
      columnDefs: [
    	  { 
              targets: 0,
              searchable: true,
              orderable: true,
              render: function (data, type, full, meta) {
            	  return '<a href="#AssessmentModal" data-toggle="modal" '
            	    + ' data-assignment-id="' + full.assignmentId + '" '
            	    + '>'
            	    + data
            	    + '</a>';
              }
          },{ 
              targets: 4,
              searchable: true,
              orderable: true,
              render: function (data, type, full, meta) {
                 return getPhaseStatusLabel(data);
              }
          }, { 
              targets: [5, 6],
              searchable: true,
              orderable: true,
              render: function (data, type, full, meta) {
                 return (data <= 0)? "-" : data.toFixed(2);
              }
          }
      ],
      dom: 'Bfrtip',
      buttons: [
          'excel', 'print'
      ]
  });

  $('#SearchPhase').click(function() {
    var SelectedCycleId=$('#SelectedCycleId');
    var SelectedPhaseId=$('#SelectedPhaseId');
    cycleId=$.trim(SelectedCycleId.val());
    phaseId=$.trim(SelectedPhaseId.val());
    if (cycleId ==-1) {
      $(SelectedCycleId).focus();
    } else {
	  var table = $('#AllEmployeeScoreTable').DataTable();
	  if (phaseId > 0) {
		table.ajax.url("<%=request.getContextPath()%>/api/admin/report/phase/score/" + cycleId + "/" + phaseId).load();
	  } else {
	    $(SelectedPhaseId).focus();
	  }
	}
  });

  $('#AssessmentModal').on('shown.bs.modal', function(e) {
	  var assignmentId = $(e.relatedTarget).data('assignment-id');
	  console.log('assignmentId= ' + assignmentId);

	  $('.assessment_card .header').html('');
	  $('.assessment_card .body').html('');
	  $('.assessment_card').renderAssessment({
		role: 'Admin',
		contextPath: '<%=request.getContextPath()%>',
		url: '<%=request.getContextPath()%>/api/admin/report/phase/audit/byAssignId/' + assignmentId,
		showSeeGoals: false
	  });
   }) ;
});
  
</script>
</body>
</html>