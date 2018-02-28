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
              <form id="Template_Form" method="POST">
                <h3>Template Information</h3>
                <fieldset style="margin-top: 90px;">
                  <div class="col-lg-2 col-md-2 col-sm-4 col-xs-5 form-control-label">
                    <label for="Template_Name">Template Name</label>
                  </div>
                  <div class="col-lg-10 col-md-10 col-sm-8 col-xs-7">
		            <div class="form-group form-float">
		              <div class="form-line">
		                <input type="text" id="Template_Name" class="form-control" minlength="3" placeholder="Please enter the name" required autofocus value="Template 1">
                      </div>
                    </div>
                  </div>
                </fieldset>

                <h3>Choose Assessments & Parameters</h3>
                <fieldset>
                  <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12" style="padding:0px">
	                <div class="panel-group" id="Goals_Accordion" role="tablist" aria-multiselectable="true">
	                  <div class="panel">
	                    <div class="panel-heading" role="tab" id="heading_1">
	                      <h4 class="panel-title">
	                        <a role="button" data-toggle="collapse" data-parent="#Goals_Accordion" href="#collapse_1" aria-expanded="true" aria-controls="collapse_1">
	                          Client Orientation/Customer Focus
	                        </a>
	                      </h4>
	                    </div>
	                    <div id="collapse_1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading_1">
	                      <div class="panel-body">
	                        <table class="table table-striped table-hover">
	                          <tbody>
	                            <tr>
	                              <td width="90%">Establishes and maintains effective relationship with both internal and external customers and gains their trust</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Understands and Proactively addresses customer needs in a timely manner by following the Client/Softvision Process</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Knowledge of Softvision and Customer processes (software development processes)</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Number of customer accolades</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                          </tbody>
	                        </table>
	                      </div>
	                    </div>
	                  </div>
	                  <div class="panel">
	                    <div class="panel-heading" role="tab" id="heading_2">
	                      <h4 class="panel-title">
	                        <a role="button" data-toggle="collapse" data-parent="#Goals_Accordion" href="#collapse_2" aria-expanded="true" aria-controls="collapse_2">
	                          Project Management
	                        </a>
	                      </h4>
	                    </div>
	                    <div id="collapse_2" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading_2">
	                      <div class="panel-body">
	                        <table class="table table-striped table-hover">
	                          <tbody>
	                            <tr>
	                              <td width="90%">Ability to identify, plan, procure and utilize resources within the scope of the project</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Ability to identify, mitigate and manage risks in the project. a) Planning and Control b) Resource Management</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Knowledge of Softvision and Customer processes (software development processes)</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Knowledge of PM basics like Initiation, Planning, Monitoring, Negotiation skills, budgeting,Tracking and Closure</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Concepts and application of PM disciplines like estimation, scope, time, cost and risk mgmt</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                            <tr>
	                              <td width="90%">Application of Softvision PM Process and Client PM Process</td>
	                              <td width="10%"><div class="switch pull-right"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>
	                            </tr>
	                          </tbody>
	                        </table>
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
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>

  <script>
  var template={};

  $(function () {
	var id="${param.id}";

	template.id=id;
	template.headers=[];

	$('#Template_Form').formValidator({
      onFinishing: function () {
    	console.log('template=' + JSON.stringify(template));
    	$.fn.postJSON({url : '<%=request.getContextPath()%>/template/save', data: template});
      },
	});

	$.fn.ajaxGet({
	  url: '<%=request.getContextPath()%>/goal/apply-list',
	  onSuccess: renderGoals,
	  onFail: onLoadError,
	  onError: onLoadError
	});

	// Load all the goals with parameters
    if (id != 0) {
		// Select and deselect based on the goal parameters
    }
  });

  function renderGoals(result) {
	//console.log('result=' + JSON.stringify(result));
	var Goals_Accordion = $('#Goals_Accordion');
	$(Goals_Accordion).empty();

	$(result).each(function (index, goal) {
	  var headingId='heading_' + goal.id
	  var collapseId='collapse_' + goal.id
      var panel=$('<div class="panel">');

	  var heading=$('<div class="panel-heading" role="tab" id="' + headingId + '">');
      var title=$('<h4 class="panel-title">'
		+ '<a role="button" data-toggle="collapse" data-parent="#Goals_Accordion" href="#' + collapseId + '" aria-expanded="true" aria-controls="' + collapseId + '">'
		+ goal.name
		+ '</a>');
	  $(heading).append(title);

	  var collapse=$('<div id="' + collapseId + '" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="' + headingId + '">');
	  var panelBody=$('<div class="panel-body">');
	  var table=$('<table class="table table-striped table-hover">');
	  var tbody=$('<tbody>');
	  $(goal.params).each(function (jindex, param) {
        var tr=$('<tr>');
        $(tr).append('<td>' + param.name + '</td>');
	    var checkbox=$('<input type="checkbox" checked>');
	    var span=$('<span class="lever switch-col-green">');
	    var label=$('<label>');
	    var switchDiv=$('<div class="switch pull-right">');
	    var switchTd=$('<td>');
        $(label).append(checkbox);
	    $(label).append(span);
        $(switchDiv).append(label);
        $(switchTd).append(switchDiv);
        $(tr).append(switchTd);
	    $(tbody).append(tr);
	    $(checkbox).click(function() {
	      var checked=$(this).is(':checked');
	        $(template.headers).each(function (aindex, header) {
	    	  $(header.details).each(function (aindex, detail) {
	    	    if (detail.paramId == param.id) {
	    		  detail.apply=(checked)?"Y":"N";
	    		}
	    	  });
	    	});
	      });
	  });

	  $(table).append(tbody);
	  $(panelBody).append(table);
	  $(collapse).append(panelBody);

	  $(panel).append(heading);
	  $(panel).append(collapse);

	  $(Goals_Accordion).append(panel);

      // Model creation
      var templateHeader={};
      templateHeader.id=0; // TODO: change for updated one
      templateHeader.goalId=goal.id;
      templateHeader.goalName=goal.name;
      templateHeader.weightage=0; // TODO: change for updated one
      templateHeader.details=[];
	  template.headers[template.headers.length]=templateHeader;

	  $(goal.params).each(function (jindex, param) {
		  var templateDetail={};
		  templateDetail.id=0; // TODO: change for updated one
		  templateDetail.paramId=param.id;
		  templateDetail.paramName=param.name;
		  templateDetail.apply=param.applicable; // TODO: change for updated one
		  templateHeader.details[templateHeader.details.length]=templateDetail;
	  });
	});
	//console.log('template=' + JSON.stringify(template));
  }

  function onLoadError(error) {
	console.log('error=' + JSON.stringify(error));
  }

  </script>

</html>