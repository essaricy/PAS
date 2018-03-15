<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>Softvision | PMS</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/favicon.ico">
    <!-- Bootstrap Core Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/css/bootstrap.css">
    <!-- Waves Effect Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.css"/>
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css" />

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
        <h2>Competency Assessment
          <small>Add, Edit or Delete competency assessments or assessment parameters</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="header">
              <h2>Manage Competency Assessment</h2>
            </div>
            <div class="body">
              <form id="Competency_Form" method="POST">
                <h3>Create Competency</h3>
                <fieldset>
                  <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
                    <label for="Competency_Name">Competency Name</label>
                  </div>
                  <div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
                    <div class="form-group form-float">
                      <div class="form-line">
		                <input type="text" id="Competency_Name" class="form-control" minlength="3" required >
		              </div>
		            </div>
                  </div>
                </fieldset>
                <h3>Add Parameters</h3>
                <fieldset>
                  <div class="col-lg-10 col-md-10 col-sm-8 col-xs-6">
                    <div class="form-group">
                      <div class="form-line">
                        <textarea id="CompetencyParam_Name" rows="2" class="form-control no-resize" placeholder="Please type what you want..."></textarea>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-2 col-md-2 col-sm-4 col-xs-6">
                    <button type="button" id="CompetencyParam_Add" class="btn btn-primary btn-lg m-l-15 waves-effect">Add</button>
                  </div>

                  <table id="CompetencyParam_Table" class="table table-striped">
                    <thead>
                      <tr>
                        <th width="90%">Parameter</th>
                        <th>Apply?</th>
                      </tr>
                    </thead>
                    <tbody>
                    </tbody>
                  </table>
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
  <!-- Jquery Validation Plugin Css -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
  <!-- JQuery Steps Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-steps/jquery.steps.js"></script>
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>

  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>

  <script>
  $(function () {
	var table = '#CompetencyParam_Table';
    var id="${param.id}";

    $('#Competency_Form').formValidator({
      onFinishing: function () {
        var goal={};
	    var params=[];
	    goal.id=id;
	    goal.name=$('#Competency_Name').val();

     	$('#CompetencyParam_Table > tbody tr').each(function(index, row) {
      	  var $tds = $(this).find('td')
          var param={};
      	  param.id=$tds.eq(0).attr('item-id');
      	  param.name=$tds.eq(0).attr('item-name');
      	  param.applicable=($tds.eq(1).find('input').is(':checked'))? 'Y' : 'N';
          params[params.length]=param;
   		});
     	goal.params=params;
      	$.fn.postJSON({url : '<%=request.getContextPath()%>/goal/update', data: goal});
      	//postJSON("<%=request.getContextPath()%>/goal/update", goal, null, null, null);
      }
    });

    if (id != 0) {
      var url = '<%=request.getContextPath()%>/goal/list/' + id;
      $.get(url, {sid: new Date().getTime()}, function() {})
      .done(function(result) {
        if (result) {
	      $('#Competency_Name').val(result.name);
	      $(result.params).each(function(index, param) {
	    	appendRow(table, param);
	      });
       	}
      });
    }

    $('#CompetencyParam_Add').click(function() {
	  // Validate the input fields
	  appendRow(table, {id:0, name: ""+$('#CompetencyParam_Name').val(), applicable: 'Y'});
   	  $('#CompetencyParam_Name').val('');
    });

    function appendRow(tableSelector, param) {
      var row=$('<tr>');
      $(row).append('<td item-id="' + param.id + '" item-name="' + param.name + '" item-applicable="' + param.applicable + '">' + param.name + '</td>');
      if (param.applicable == 'Y') {
        $(row).append('<td><div class="switch"><label><input type="checkbox" checked><span class="lever switch-col-green"></span></label></div></td>');
      } else {
    	$(row).append('<td><div class="switch"><label><input type="checkbox"><span class="lever switch-col-green"></span></label></div></td>');
      }
      $(tableSelector).find('tbody:last-child').append(row);
    }
  });
  </script>

</html>