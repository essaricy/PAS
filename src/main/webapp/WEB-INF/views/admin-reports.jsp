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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-rateyo/jquery.rateyo.min.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
  </head>
  <style>
  .list-group-item {
  	font-size: 13px;
  }
  .selected {
  	background-color: #607D8B;
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
        <h2>Admin Reports
          <!-- <small>View status of the current employees those are assigned to you, change manager, enable forms, view assessment or conclude</small> -->
        </h2>
      </div>
	  <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
              <div class="media">
                <div class="media-left">
                  <a href="<%=request.getContextPath()%>/admin/report/appraisal/overview"><i class="material-icons md-48">view_list</i></a>
                </div>
                <div class="media-body">
                  <h4 class="media-heading">
                    <a href="<%=request.getContextPath()%>/admin/report/appraisal/overview">Appraisal Overview Report</a>
                  </h4>
                  This report contains all the employees scores for all cycles. This report shows only those employees who have been submitted to you as a second level manager.
                </div>
              </div>
              <div class="media">
                <div class="media-left">
                  <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/cycle"><i class="material-icons md-48">photo</i></a>
                </div>
                <div class="media-body">
                  <h4 class="media-heading">
                    <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/cycle">Appraisal Status Report (Cycle)</a>
                  </h4>
                  This report lists appraisal status of all the employees at the cycle-level.
                </div>
              </div>
              <div class="media">
                <div class="media-left">
                  <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/phase/assigned"><i class="material-icons md-48">photo_size_select_large</i></a>
                </div>
                <div class="media-body">
                  <h4 class="media-heading">
                    <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/phase/assigned">Appraisal Status Report (Phase-wise - Assigned)</a>
                  </h4>
                  This report lists appraisal status of all the employees for whom template has been assigned.
                </div>
              </div>
              <div class="media">
                <div class="media-left">
                  <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/phase/unassigned"><i class="material-icons md-48">photo_size_select_small</i></a>
                </div>
                <div class="media-body">
                  <h4 class="media-heading">
                    <a href="<%=request.getContextPath()%>/admin/report/appraisal/status/phase/unassigned">Appraisal Status Report (Phase-wise - Unassigned)</a>
                  </h4>
                  This report lists all the employees for whom template has not been assigned.
                </div>
              </div>
              <div class="media">
                <div class="media-left">
                  <a href="<%=request.getContextPath()%>/admin/report/appraisal/score"><i class="material-icons md-48">apps</i></a>
                </div>
                <div class="media-body">
                  <h4 class="media-heading">
                    <a href="<%=request.getContextPath()%>/admin/report/appraisal/score/phase">All-Inclusive Report</a>
                  </h4>
                  This report lists scores of all employees in an appraisal phase. We can view the ratings and comments of both employees and managers.
                </div>
              </div>
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
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-rateyo/jquery.rateyo.min.js"></script>

<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/tooltips-popovers.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>

<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-card.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-report-score-cycle.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/modals.js"></script>
<script>
$(function () {
});
</script>
</html>
