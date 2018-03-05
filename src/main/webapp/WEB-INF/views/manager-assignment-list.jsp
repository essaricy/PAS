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
    </div>
	  	<!-- Large Size -->
		<div class="modal fade" id="largeModal" tabindex="-1" role="dialog">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="largeModalLabel">Manager Change</h4>
					</div>
					<div class="modal-body">
	                   <div class="row clearfix">
	                     <div class="table-responsive">
	                       <table id="SearchRoleTable" class="table table-bordered table-striped table-hover dataTable">
	                         <thead>
	                           <tr>
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
						<button type="button" id="AssignTo" class="btn btn-link waves-effect">Assign To</button>
						<button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
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
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-assignments.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/modals.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
<script>
$(function () {
  var MANAGER_ROLE_ID='Manager';
  $('.container-fluid').renderAssignment({
	role: 'Manager',
	contextpath: '<%=request.getContextPath()%>',
  	url: '<%=request.getContextPath()%>/assignment/manager/list',
  });
  
  var assignmentId=0;
  $( "#largeModal" ).on('shown.bs.modal', function(e) {
	  var $invoker = $(e.relatedTarget);
	  assignmentId=$invoker.attr('item-id');
      $($.fn.dataTable.tables( true ) ).css('width', '100%');
      $($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
  } );

  $('#SearchRoleTable').DataTable({
      responsive: true,
      paging: false,
		searching: false,
		ordering: true,
		info: true,
      "ajax": "<%=request.getContextPath()%>/employee/role-search/" + MANAGER_ROLE_ID,
      "sAjaxDataProp":"",
		"columns": [
          { "data": "EmployeeId" },
          { "data": "FirstName" },
          { "data": "LastName" },
          { "data": "Band" },
          { "data": "Designation" },
      ],
      columnDefs: [
          { 
              targets: 0,
              searchable: false,
              orderable: false,
              render: function(data, type, full, meta){
                 if(type === 'display'){
                    data = '<input name="SelectedManagerId" type="radio" id="radio_' + data + '" value="' + data + '" class="with-gap radio-col-red"><label for="radio_' + data + '">' + data + '</label>';      
                 }

                 return data;
              }
          }
      ]
  });
  
  $('#AssignTo').click(function() {
    var SelectedManagerId = $('input[name=SelectedManagerId]:checked').val();
    console.log('SelectedManagerId=' + SelectedManagerId);
    if (SelectedManagerId) {
    	$.fn.ajaxPut({url: '<%=request.getContextPath()%>/assignment/manager/change/assignedBy/' + assignmentId + '/' + SelectedManagerId});
    }
    });
  
});

</script>
</html>
