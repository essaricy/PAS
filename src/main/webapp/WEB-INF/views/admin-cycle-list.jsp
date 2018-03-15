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
        <h2>Appraisal Cycle Managemet
          <small>Add and update appraisal cycles and phases. Create Drafts, Kick Off or Complete Appraisal Cycle</small>
        </h2>
      </div>
      <div class="row clearfix">
        <!-- Linked Items -->
        <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <div class="card appr_cycles_card">
            <div class="header">
              <h2>Cycles</h2>
              <ul class="header-dropdown m-r--5">
                <li class="dropdown">
                </li>
              </ul>
            </div>
            <div class="body">
            </div>
          </div>
        </div>
        <div class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
          <div class="card appr_cycle_card">
            <div class="header">
              <h2>Cycle Information</h2>
              <ul class="header-dropdown m-r--5">
                <li class="dropdown">
                </li>
              </ul>
            </div>
            <div class="body">
            </div>
          </div>
          <div class="card appr_phases_card">
            <div class="header">
              <h2>Phases</h2>
            </div>
            <div class="body">
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
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script>
$(function () {
});
$('.appr_cycles_card').cardManager({
  type: 'list-with-links',
  loadUrl: '<%=request.getContextPath()%>/appraisal/list',
  manageUrl: '<%=request.getContextPath()%>/admin/cycles/manage',
  deleteUrl: '<%=request.getContextPath()%>/appraisal/delete',
  onClickCallback: renderCycleInformation,
  menuActions: ["Add", "Update", "Delete"],
  renderConfigs: [
    { 
      type: 'table',
      fromNode: 'phases',
      toContainer: '.appr_phases_card .body',
      headerNames: ["Name", "Start Date", "End Date"],
	  columnMappings: ["name", "startDate", "endDate"]
    }
  ],
  afterLoadCallback: function (items, data) {
    $(items).each(function(index, item) {
      var dataItem=data[index];
      var status=dataItem.status;
      var class1=null;
      if (status == '${AppraisalCycleStatus_DRAFT}') {
      	class1='bg-amber';
      } else if (status == '${AppraisalCycleStatus_ACTIVE}') {
      	class1='bg-light-blue';
      } else if (status == '${AppraisalCycleStatus_COMPLETE}') {
      	class1='bg-grey';
      }
      $(this).append('<span class="badge ' + class1 + '">' + status + '</span>');
    });
  }
}); 

function renderCycleInformation(item) {
  $('.appr_cycle_card .header h2').text(item.name);
  $('.appr_cycle_card .body').empty();
  var table=$('<table class="table">');
  var tbody=$('<tbody>');
  $(tbody).append('<tr><td>Start Date</td><td>' + (item.startDate) + '</td></tr>');
  $(tbody).append('<tr><td>End Date</td><td>' + (item.endDate) + '</td></tr>');
  $(tbody).append('<tr><td>Eligibility Date</td><td>' + (item.cutoffDate) + '</td></tr>');
  $(table).append(tbody);
  $('.appr_cycle_card .body').append(table);

  var status=item.status;
  $('.appr_cycle_card .header .dropdown').empty();
  if (status == '${AppraisalCycleStatus_DRAFT}') {
	var button=$('<button class="activate btn bg-light-blue waves-effect">Activate</button>');
	$(button).click(function() {activate(item.id) });
	$('.appr_cycle_card .header .dropdown').append(button);
  } else if (status == '${AppraisalCycleStatus_ACTIVE}') {
    var button=$('<button class="activate btn bg-light-blue waves-effect">Complete</button>');
	$(button).click(function() {complete(item.id) });
	$('.appr_cycle_card .header .dropdown').append(button);
  } else if (status == '${AppraisalCycleStatus_COMPLETE}') {
  	class1='bg-green';
  }
}

function activate(itemId) {
  swal({
	title: "Are you sure?", text: "Do you really want to activate this appraisal cycle?", type: "warning",
	showCancelButton: true, confirmButtonColor: "#DD6B55",
    confirmButtonText: "Yes, Activate!", closeOnConfirm: false
  }, function () {
    $.fn.ajaxPut({url: '<%=request.getContextPath()%>/appraisal/activate/' + itemId});
  });
}
function complete(itemId) {
  swal({
	title: "Are you sure?", text: "Do you really want to complete this appraisal cycle? Please make sure that al eligible employees have completed thier appraisals.", type: "warning",
	showCancelButton: true, confirmButtonColor: "#DD6B55",
    confirmButtonText: "Yes, Complete!", closeOnConfirm: false
  }, function () {
    $.fn.ajaxPut({url: '<%=request.getContextPath()%>/appraisal/complete/' + itemId});
  });
}

</script>
</html>
