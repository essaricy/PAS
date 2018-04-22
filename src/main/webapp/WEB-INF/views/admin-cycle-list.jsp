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
        <h2>Appraisal Cycle Management
          <small>Add and update appraisal cycles and phases. Create Drafts, Kick Off or Complete Appraisal Cycle</small>
        </h2>
      </div>
      <div class="row clearfix">
        <!-- Linked Items -->
        <div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
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
        <div class="col-lg-7 col-md-7 col-sm-12 col-xs-12">
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
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-card.js"></script>
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
      var appraisalCycleStatus=getAppraisalCycleStatus(dataItem.status);
      $(this).append('<span class="badge ' + appraisalCycleStatus.colorClass + '">' + appraisalCycleStatus.code + '</span>');
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

  var status=getAppraisalCycleStatus(item.status);
  //var button=$('<button class="btn bg-light-blue waves-effect">' + status.label + '</button>');
  var button=$('<button class="btn bg-light-blue waves-effect">');
  if (status == AppraisalCycleStatus.DRAFT) {
	$(button).text(AppraisalCycleStatus.READY.label);
	$(button).click(function() {
      ready(item.id);
	});
  } else if (status == AppraisalCycleStatus.READY) {
	$(button).text(AppraisalCycleStatus.ACTIVE.label);
	$(button).click(function() {
      activate(item.id);
	});
  } else if (status == AppraisalCycleStatus.ACTIVE) {
	$(button).text(AppraisalCycleStatus.COMPLETE.label);
	$(button).click(function() {
	  complete(item.id);
	});
  }
  $('.appr_cycle_card .header .dropdown').empty();
  if (status != AppraisalCycleStatus.COMPLETE) {
    $('.appr_cycle_card .header .dropdown').append(button);
  }
}

function ready(itemId) {
  swal({
	title: "Are you sure?", text: "You cannot edit the cycle after its in READY. Do you want to change this cycle from DRAFT to READY?", type: "warning",
	showCancelButton: true, confirmButtonColor: "#DD6B55",
    confirmButtonText: "Yes, Change it!", closeOnConfirm: false, showLoaderOnConfirm: true
  }, function () {
    $.fn.ajaxPut({url: '<%=request.getContextPath()%>/appraisal/ready/' + itemId});
  });
}
function activate(itemId) {
  swal({
	title: "Are you sure?", text: "Do you really want to activate this appraisal cycle?", type: "warning",
	showCancelButton: true, confirmButtonColor: "#DD6B55",
    confirmButtonText: "Yes, Activate!", closeOnConfirm: false, showLoaderOnConfirm: true
  }, function () {
    $.fn.ajaxPut({url: '<%=request.getContextPath()%>/appraisal/activate/' + itemId});
  });
}
function complete(itemId) {
  swal({
	title: "Are you sure?", text: "Do you really want to complete this appraisal cycle? Please make sure that al eligible employees have completed thier appraisals.", type: "warning",
	showCancelButton: true, confirmButtonColor: "#DD6B55",
    confirmButtonText: "Yes, Complete!", closeOnConfirm: false, showLoaderOnConfirm: true
  }, function () {
    $.fn.ajaxPut({url: '<%=request.getContextPath()%>/appraisal/complete/' + itemId});
  });
}

</script>
</html>
