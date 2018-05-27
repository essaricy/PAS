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
        <h2>Employee Score Report
          <small>View scores of the employee in a cycle.</small>
        </h2>
      </div>
      <%@include file="common/no-cycle.jsp" %>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-50 col-sm-12 col-xs-12">
          <div class="card employee_phase_assessment_card">
            <div class="header"><h2>Phase-wise Score Report of {employeeName} for the Cycle {cycle}</h2></div>
            <div class="body">
            </div>
          </div>
        </div>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-50 col-sm-12 col-xs-12">
          <div class="card employee_assessment_card">
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
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-assessments3.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/modals.js"></script>

<script>
$(function () {
  var aid='${param.aid}';
  var eid='${param.eid}';

  $('.employee_assessment_card').hide();

  $.fn.employeePhaseAssessmentsReport=function( options ) {
	var settings=$.extend({
	    contextPath: null,
	    url: null,
	}, options );

    var obj=$(this);
    // Get employee information
    // Get Appraisal Cycle information
	$.fn.ajaxGet({
		url: settings.contextPath + settings.url,
		onSuccess: renderAllPhaseAssessments,
		onError: showEmployeePhaseScoreError
  	});

	function renderAllPhaseAssessments(phaseAssessments) {
	  if (phaseAssessments == null || phaseAssessments.length == 0) {
          $(cardBody).append('<p class="font-bold col-pink">There were no phase assignments found for this employee for this appraisal cycle</p>');
      } else {
          var table=$('<table class="table table-bordered table-striped table-hover dataTable">');
          $(obj).find('.body').append(table);

          var thead=$('<thead>');
          var theadRow=$('<tr>');
          $(theadRow).append('<th width="15%">Phase</th>');
          //$(theadRow).append('<th width="50%">Goal</th>');
          $(theadRow).append('<th width="50%">Assessed By</th>');
          $(theadRow).append('<th width="15%">Score</th>');
          $(table).append(theadRow);
          
          var tbody=$('<tbody>');
          $(table).append(tbody);
          $(phaseAssessments).each(function (index, phaseAssessment) {
        	//console.log(JSON.stringify(phaseAssessment));

        	var phase=phaseAssessment.phase;
        	var ea=phaseAssessment.employeeAssignment;
        	var assignedBy=ea.assignedBy;

        	var row=$('<tr>');
        	var linkTd=$('<td width="15%">');
        	var link=$('<a href="#">');
        	$(linkTd).click(function() {
        	  //showPhaseAssessment(phaseAssessment);
        	});

        	$(row).append(linkTd);
        	$(linkTd).append(link);
        	$(link).append(phase.name);

            //$(row).append('<td width="15%">' + phase.name + '</td>');
            //$(row).append('<td width="50%">' + phase.name + '</td>');
            $(row).append('<td width="50%">' + assignedBy.fullName + '</td>');
            $(row).append('<td width="15%">' + ea.score + '</td>');
            $(table).append(row);
          });
  	  }
	}

	function showPhaseAssessment(phaseAssessment) {
	  $('.employee_assessment_card').show();
	  console.log('phaseAssessment.employeeAssignment.assignmentId=' + phaseAssessment.employeeAssignment.assignmentId);
	  $('.employee_assessment_card').renderAssessment({
		role: 'Manager',
	    contextPath: '<%=request.getContextPath()%>',
	    //useData: phaseAssessment
	    url: '<%=request.getContextPath()%>/api/assessment/list/phase/byAssignId/' + phaseAssessment.employeeAssignment.assignmentId,
	  });
	}

	function showEmployeePhaseScoreError(error) {
		console.log('error=' + error);
  	}
  }

  $('.employee_phase_assessment_card').employeePhaseAssessmentsReport({
    contextPath: '<%=request.getContextPath()%>',
    url: '/api/manager/report/cycle/assessments/' + aid + '/' + eid
  });

});
</script>
</html>
