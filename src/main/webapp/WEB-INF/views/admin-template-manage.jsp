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
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css"/>
    <!-- Bootstrap Select Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css">

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
        <h2>Appraisal Cycle
          <small>Add, Edit or Delete appraisal cycles, appraisal phases or eligibility criterion</small>
        </h2>
      </div>

      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="header">
              <h2>Template</h2>
            </div>
            <div class="body">
              <form id="Appr_Cycle_Form" method="POST">
                <h3>Details</h3>
                <fieldset style="margin-top: 90px;">
                  <div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
                    <label for="AppraisalCycle_Name">Employee Name</label>
                  </div>
                  <div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
		            <div class="form-group form-float">
		              <div class="form-line">
		                <input type="text" id="" class="form-control" name="minmaxlength"  minlength="3" required value="" placeholder="Please enter the name">
                      </div>
                    </div>
                  </div>
                </fieldset>

                <h3>Competency Assessment</h3>
                <fieldset>
                 	<div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12" style="padding:0px">
	                    <div class="panel-group" id="accordion_1" role="tablist" aria-multiselectable="true">
	                        <div class="panel panel-primary">
	                            <div class="panel-heading" role="tab" id="headingOne_1">
	                                <h4 class="panel-title">
	                                    <a role="button" data-toggle="collapse" data-parent="#accordion_1" href="#collapseOne_1" aria-expanded="true" aria-controls="collapseOne_1">
	                                        Client Orientation/Customer Focus
	                                    </a>
	                                </h4>
	                            </div>
	                            <div id="collapseOne_1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne_1">
	                                <div class="panel-body">
	                                    <div>
		                                    <div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px;">
								                Establishes and maintains effective relationship with both internal and external customers and gains their trust
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
							                	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Understands and Proactively addresses customer needs in a timely manner by following the Client/Softvision Process
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Knowledge of Softvision and Customer processes (software development processes)
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Number of customer accolades
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
							        	</div>
	                                </div>
	                            </div>
	                        </div>
	                        <div class="panel panel-primary">
	                            <div class="panel-heading" role="tab" id="headingTwo_1">
	                                <h4 class="panel-title">
	                                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion_1" href="#collapseTwo_1" aria-expanded="false"
	                                       aria-controls="collapseTwo_1">
	                                       Project Management
	                                    </a>
	                                </h4>
	                            </div>
	                            <div id="collapseTwo_1" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo_1">
	                                <div class="panel-body">
	                                <div class="clearfix"></div>
	                                	<div>
		                                    <div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Ability to identify, plan, procure and utilize resources within the scope of the project
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Ability to identify, mitigate and manage risks in the project. a) Planning and Control b) Resource Management
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Knowledge of PM basics like Initiation, Planning, Monitoring, Negotiation skills, budgeting,Tracking and Closure
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Concepts and application of PM disciplines like estimation, scope, time, cost and risk mgmt
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Application of Softvision PM Process and Client PM Process
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Process compliance in terms of non compliance
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Consitency in communicatoion - gaps btw onshore communication etc
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
								        	<div class="col-lg-11 col-md-11 col-sm-9 col-xs-8" style="margin-bottom: 0px;margin-top: 20px">
								                Frequency of communication within the team - internal and external
							                  </div>
		                                    <div class="col-lg-1 col-md-1 col-sm-3 col-xs-4">
		                                    	<div class="demo-switch">
								                	<div class="demo-switch-title"></div>
			                                        <div class="switch">
			                                            <label><input type="checkbox" checked><span class="lever switch-col-green"></span></label>
			                                        </div>
		                                        </div>
							                </div>
								        	<div class="clearfix"></div>
							        	</div>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
                </fieldset>
              </form>
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
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/dropzone/dropzone.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/multi-select/js/jquery.multi-select.js"></script>


  <!-- Waves Effect Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
  <!-- Autosize Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
  <!-- Moment Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
  <!-- Bootstrap Material Datetime Picker Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>

  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
  <!-- Jquery Validation Plugin Css -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
  <!-- JQuery Steps Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-steps/jquery.steps.js"></script>
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>

  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/forms/basic-form-elements.js"></script> --%>
  <!-- Demo Js -->
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>  --%>

  <script>
  $(function () {
	var table = '#AppraisalPhase_Table';
	var id="${param.id}";

	$('#Appr_Cycle_Form').formValidator({
      onFinishing: function () {
    	var apprCycle={};
    	var phases=[];
    	apprCycle.name=$('#AppraisalCycle_Name').val();
    	apprCycle.startDate=$('#AppraisalCycle_StartDate').val();
    	apprCycle.endDate=$('#AppraisalCycle_EndDate').val();
    	apprCycle.cutoffDate=$('#AppraisalCycle_EligibilityDate').val();
    	
		$('#AppraisalPhase_Table > tbody tr').each(function(index, row) {
			var $tds = $(this).find('td')
			console.log('index : ' + index);
			var phase={};
			phase.name=$tds.eq(0).text();
			phase.startDate=$tds.eq(1).text();
			phase.endDate=$tds.eq(2).text();
			phases[phases.length]=phase;
		});
    	apprCycle.phases=phases;

        console.log('serailize form : ' + JSON.stringify(apprCycle));
      },
      onFinished: function() {
    	  alert('show result based on rest response');
      }
	});

    if (id != 0) {
      var url = '<%=request.getContextPath()%>/appriasal/list/' + id;

      $.get(url, {sid: new Date().getTime()}, function() {})
      .done(function(result) {
        if (result) {
          console.log(JSON.stringify(result));
          $('#AppraisalCycle_Name').val(result.name);
          $('#AppraisalCycle_StartDate').val(result.startDate);
          $('#AppraisalCycle_EndDate').val(result.endDate);
          $('#AppraisalCycle_EligibilityDate').val(result.cutoffDate);
	      $(result.appraisalPhases).each(function(index, phase) {
	    	  appendRow(table, phase);
	      });
       	}
      });
    }

	$('#AppraisalPhase_Add').click(function() {
	  console.log('clicked CompetencyParam_Add');
	  // Validate the input fields
	  appendRow(table, {id:0, name: $('#AppraisalPhase_Name').val(),
		  startDate: $('#AppraisalPhase_StartDate').val(), endDate: $('#AppraisalPhase_EndDate').val()});

   	  $('#AppraisalPhase_Name').val('');
   	  $('#AppraisalPhase_StartDate').val('');
   	  $('#AppraisalPhase_EndDate').val('');
	});

    function appendRow(tableSelector, phase) {
   	  console.log('phase=' + JSON.stringify(phase));
   	  var row=$('<tr>');
      $(row).append('<td item-id=' + phase.id
        + ' item-name=' + phase.name
        + ' item-startDate=' + phase.startDate
        + ' item-endDate=' + phase.endDate
        + '>' + phase.name + '</td>');
      $(row).append('<td>' + phase.startDate + '</td>');
      $(row).append('<td>' + phase.endDate + '</td>');
      $(tableSelector).find('tbody:last-child').append(row);
   	}

	$('#AppraisalCycle_StartDate').change(function(e, date) {
	  $('#AppraisalCycle_EndDate').bootstrapMaterialDatePicker('setMinDate', moment(date).add(1, 'day'));
	  $('#AppraisalCycle_EligibilityDate').bootstrapMaterialDatePicker('setMaxDate', moment(date).subtract(1, 'day'));
	});

    $('#AppraisalPhase_StartDate').change(function(e, date) {
      $('#AppraisalPhase_EndDate').bootstrapMaterialDatePicker('setMinDate', moment(date).add(1, 'day'));
	});
  });

  </script>

</html>