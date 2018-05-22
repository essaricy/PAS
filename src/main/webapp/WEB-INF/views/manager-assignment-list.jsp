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
        <h2>Employee Assignments
          <small>View status of the current employees those are assigned to you, change manager, enable forms, view assessment or conclude</small>
        </h2>
      </div>
      <%@include file="common/no-cycle.jsp" %>
    </div>
    <!-- Large Size -->
    <div class="modal" id="TemplateModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="TemplateModal_Title"></h4>
          </div>
          <div class="modal-body">
            <div class="row clearfix">
              <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 goals_container">
              </div>
              <div class="col-lg-8 col-md-8 col-sm-12 col-xs-12 goal_params_container">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

	<!-- Large Size -->
	<div class="modal fade" id="EmployeeSearchModal" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="EmployeeSearchModalLabel"></h4>
				</div>
				<div class="modal-body">
					<div class="row clearfix">
						<div class="table-responsive">
							<table id="SearchRoleTable"
								class="table table-bordered table-striped table-hover dataTable">
								<thead>
									<tr>
										<th>&nbsp;</th>
										<th>Employee ID</th>
										<th>First Name</th>
										<th>Last Name</th>
										<th>Band</th>
										<th>Designation</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" id="AssignToAnotherManager"
						class="btn btn-primary waves-effect">Assign</button>
					<button type="button" id="SubmitToNextLevelManager"
						class="btn btn-primary waves-effect">Assign</button>
					<button type="button" class="btn btn-primary waves-effect"
						data-dismiss="modal">Close</button>
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
<!-- Slimscroll Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
<!-- Waves Effect Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
<!-- Autosize Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
<!-- Moment Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
<!-- SweetAlert Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/tooltips-popovers.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/extensions/export/dataTables.buttons.min.js"></script>

<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-assignments-manager.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/modals.js"></script>
<script>
$(function () {
  var MANAGER_ROLE_ID='Manager';

  $('.container-fluid').managerAssignment({
	contextPath: '<%=request.getContextPath()%>',
  	url: '<%=request.getContextPath()%>/assignment/manager/list',
  });

  var assignmentId=0;
  $( "#EmployeeSearchModal" ).on('shown.bs.modal', function(e) {
    var $invoker = $(e.relatedTarget);
	assignmentId=$invoker.attr('item-id');
	var type=$invoker.attr('item-type');
	if (type == 'AssignToAnotherManager') {
	  $('#AssignToAnotherManager').show();
	  $('#SubmitToNextLevelManager').hide();
	  $('#EmployeeSearchModalLabel').text('Assign To Another Manager');
	} else if (type == 'SubmitToNextLevelManager') {
	  $('#AssignToAnotherManager').hide();
	  $('#SubmitToNextLevelManager').show();
	  $('#EmployeeSearchModalLabel').text('Assign To Next Level Manager');
	}
    $($.fn.dataTable.tables( true )).css('width', '100%');
    $($.fn.dataTable.tables( true )).DataTable().columns.adjust().draw();
  });

  $('#SearchRoleTable').DataTable({
    responsive: true,
    paging: true,
	searching: true,
	ordering: true,
	info: true,
    "ajax": "<%=request.getContextPath()%>/employee/role-search/" + MANAGER_ROLE_ID,
    "sAjaxDataProp":"",
	"columns": [
	  { "data": "employeeId" },
      { "data": "employeeId" },
      { "data": "firstName" },
      { "data": "lastName" },
      { "data": "band" },
      { "data": "designation" },
    ],
    "aoColumnDefs": [
      {
        "aTargets": [0],
        "mData": "employeeId",
        "mRender": function (data, type, full) {
          return '<input name="SelectedManagerId" type="radio" id="radio_' + data + '" value="' + data + '" class="radio-col-red"><label for="radio_' + data + '"></label>';
         }
     }
    ]
  });

  $('#AssignToAnotherManager').click(function() {
    var SelectedManagerId = $('input[name=SelectedManagerId]:checked').val();
    if (SelectedManagerId) {
      $.fn.ajaxPut({url: '<%=request.getContextPath()%>/assignment/manager/change/phase-assign/' + assignmentId + '/' + SelectedManagerId});
    }
  });

  $('#SubmitToNextLevelManager').click(function() {
	var SelectedManagerId = $('input[name=SelectedManagerId]:checked').val();
    if (SelectedManagerId) {
      $.fn.ajaxPut({url: '<%=request.getContextPath()%>/assignment/manager/cycle/submit/' + assignmentId + '/' + SelectedManagerId});
    }
  });
});

</script>
</html>
