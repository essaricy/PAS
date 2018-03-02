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
        <h2>Employee Assignments
          <small>View status of the current employees those are assigned to you, change manager, send forms</small>
        </h2>
      </div>
      <div class="row clearfix">
        <!-- Linked Items -->
        <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">
          <div class="card assignment_list_card">
          	<div class="header">Current Appraisal Cycle Name
          	</div>
            <div class="body">
	            <table id="Assignments_Table" class="table table-striped table-hover dataTable">
					<thead>
						<tr>
							<th>Phase Name</th>
							<th>Employee Name</th>
							<th>Assigned By</th>
							<th>Assigned At</th>
							<th>Status</th>
							<th>Action</th>
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
  </section>
</body>

<!-- Jquery Core Js -->
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
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/tooltips-popovers.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script>
$(function () {
  $.fn.ajaxGet({
	url: '<%=request.getContextPath()%>/appraisal/get/active',
	onSuccess: onActiveAvailable,
	onError: onError
  });
  function onActiveAvailable(result) {
    if (result== null || result=="" || result=="undefined") {
	  onError("There is no Appraisal Cycle ACTIVE right now. You can assign a template to employees only when there is an appraisal cycle is ACTIVE.");
	  return;
	}
	console.log('onActiveAvailable result=' + result);
  }
  function onError(error) {
	console.log('onError error=' + error);
	var mainCard=$('.assignment_list_card');
	$(mainCard).find('.body').empty();
	$(mainCard).find('.body').append('<p class="font-bold col-pink">' + error + '</p>');
  }

  $.fn.ajaxGet({
	url: '<%=request.getContextPath()%>/manager/assignment/current',
	onSuccess: renderAssignedEmployees,
	onError: onErrorAssignedEmployees
  });

  function renderAssignedEmployees(result) {
    var table=$('#Assignments_Table');
    $(result).each(function(index, assignment) {
      var statusId=assignment.status.code;
      var statusName=assignment.status.name;
	  var row=$('<tr>');
	  $(row).append('<td>' + assignment.phaseName + '</td>');
	  $(row).append('<td>' + assignment.assignedToName + '</td>');
	  $(row).append('<td>' + assignment.assignedByName + '</td>');
	  $(row).append('<td>' + assignment.assignedAt + '</td>');
	  $(row).append('<td>' + assignment.status.title + '</td>');
	  if (statusName == '${AssignmentPhaseStatus_ASSIGNED}') {
		var td=$('<td>');
		var assignToManagerButton=$('<button class="btn btn-xs btn-info waves-effect" title="Assign to another manager"><i class="material-icons">trending_flat</i></button>');
		var enableFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Enable Employee Self-submission"><i class="material-icons">assignment_turned_in</i></button>');
		$(enableFormButton).click(function() {
			enableSelfSubmission(assignment.assignmentId);	
		});
		//$(td).append(assignToManagerButton);
		$(td).append(enableFormButton);
		$(row).append(td);
	  } else {
		$(row).append('<td>-</td>');
	  }
	  $(table).find('tr:last').after(row);
    });
  }

  function onErrorAssignedEmployees(error) {
	  
  }
});

function enableSelfSubmission(assignmentId) {
  swal({
    title: "Are you sure?", text: "Do you want to enable self-submission for this employee for this phase?", type: "warning",
    showCancelButton: true, confirmButtonColor: "#DD6B55",
	confirmButtonText: "Yes, Enable it!", closeOnConfirm: false
  }, function () {
	$.fn.ajaxPut({
	  url: '<%=request.getContextPath()%>/manager/assignment/enableForm/' + assignmentId
	});
  });
}

</script>
</html>
