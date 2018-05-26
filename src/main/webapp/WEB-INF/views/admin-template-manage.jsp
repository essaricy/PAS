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

                <h3>Choose Assessments and Parameters</h3>
                <fieldset>
                  <div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12" style="padding:0px">
	                <div class="panel-group" id="Goals_Accordion" role="tablist" aria-multiselectable="true">
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
	var refId="${param.refId}";

	template.id=id;
	template.headers=[];

	$('#Template_Form').formValidator({
      onFinishing: function () {
    	template.name=$('#Template_Name').val();
    	$('.weightage').each(function(index, weightage) {
    		template.headers[index].weightage=$(this).val();
    	});
    	$.fn.ajaxPost({url : '<%=request.getContextPath()%>/api/template/save', data: template});
      },
	});

	// Load all the goals with parameters
	var url=null;
	if (id == 0) {
	  if (refId == '') {
	    url='<%=request.getContextPath()%>/api/template/new';
	  } else {
	    url='<%=request.getContextPath()%>/api/template/copy/' + refId;
	  }
	} else {
	  url='<%=request.getContextPath()%>/api/template/list/'+id;
	}

	$.fn.ajaxGet({
	  url: url,
	  onSuccess: createTemplateView,
	  onFail: onLoadError,
	  onError: onLoadError
    });
  });

  function createTemplateView(result) {
	template=result;

	var Goals_Accordion = $('#Goals_Accordion');
	$(Goals_Accordion).empty();

	$('#Template_Name').val(template.name);

	$(template.headers).each(function (index, header) {
	  var headingId='heading_' + (index+1);
	  var collapseId='collapse_' + (index+1);
      var panel=$('<div class="panel">');

	  var heading=$('<div class="panel-heading" role="tab" id="' + headingId + '">');
      var title=$('<h4 class="panel-title">'
		+ '<a role="button" data-toggle="collapse" data-parent="#Goals_Accordion" href="#' + collapseId + '" aria-expanded="true" aria-controls="' + collapseId + '">'
		+ header.goalName
		+ '</a>');
	  $(heading).append(title);

	  var collapse=$('<div id="' + collapseId + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="' + headingId + '">');
	  var panelBody=$('<div class="panel-body">');
	  $(panelBody).append('<div class="row clearfix"><div class="col-lg-10 col-md-10 col-sm-10 col-xs-10 form-control-label"><label for="Weightage' + (index+1) + '">Weightage</label></div><div class="col-lg-2 col-md-2 col-sm-2 col-xs-2 col-xs-2"><div class="form-group form-float"><div class="form-line"><input type="number" id="Weightage' + (index+1) + '" class="form-control weightage" name="weightage' + (index+1) + '" value="' + header.weightage + '" required></div></div></div></div>');
	  var table=$('<table class="table table-striped table-hover">');
	  var tbody=$('<tbody>');

	  $(header.details).each(function (jindex, detail) {
        var tr=$('<tr>');
        $(tr).append('<td>' + detail.paramName + '</td>');

        var checkbox=null;
        if (detail.apply=='Y') {
	      checkbox=$('<input type="checkbox" checked>');
        } else {
	      checkbox=$('<input type="checkbox">');
        }

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
	        $(template.headers).each(function (aindex, aHeader) {
	    	  $(aHeader.details).each(function (bindex, aDetail) {
	    	    if (aDetail.paramId == detail.paramId) {
	    	    	aDetail.apply=(checked)?"Y":"N";
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
	});
  }

  function onLoadError(error) {
	console.log('error=' + JSON.stringify(error));
  }

  </script>

</html>