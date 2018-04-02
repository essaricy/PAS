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
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css"/>
    <!-- Bootstrap Select Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css">

    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
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
              <h2>Manage Appraisal Cycle</h2>
            </div>
            <div class="body">
              <form id="Appr_Cycle_Form" method="POST">
                <h3>Create Cycle</h3>
                <fieldset>

                  <div class="row clearfix">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 form-control-label">
                      <label for="Cycle_Name">Cycle Name</label>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 col-xs-7">
		              <div class="form-group form-float">
		                <div class="form-line">
		                  <input type="text" id="Cycle_Name" class="form-control" minlength="3" required autofocus >
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row clearfix">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 form-control-label">
                      <label for="Cycle_StartDate">Start Date</label>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 col-xs-7">
		              <div class="form-group form-float">
		                <div class="form-line">
		                  <input type="text" id="Cycle_StartDate" name="startDate" class="form-control datepicker" required >
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row clearfix">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 form-control-label">
                      <label for="Cycle_EndDate">End Date</label>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 col-xs-7">
		              <div class="form-group form-float">
		                <div class="form-line">
		                  <input type="text" id="Cycle_EndDate" name="endDate" class="form-control datepicker" required >
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row clearfix">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 form-control-label">
                      <label for="Cycle_EligibilityDate">Eligible Before</label>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 col-xs-7">
		              <div class="form-group form-float">
		                <div class="form-line">
		                  <input type="text" id="Cycle_EligibilityDate" name="eligibilityDate" class="form-control datepicker" required >
                        </div>
                      </div>
                    </div>
                  </div>

                </fieldset>

                <h3>Define Phases</h3>
                <fieldset>
                  <div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
                    <div class="form-group">
                      <div class="form-line">
                        <input type="text" id="Phase_Name" class="form-control" placeholder="Phase Name" autofocus >
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
                    <div class="form-group">
                      <div class="form-line">
                        <input type="text" id="Phase_StartDate" class="form-control datepicker" placeholder="Start Date" >
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
                    <div class="form-group">
                      <div class="form-line">
                        <input type="text" id="Phase_EndDate" class="form-control datepicker" placeholder="End Date" >
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
                    <button type="button" id="Phase_Add" class="btn btn-primary btn-lg m-l-15 waves-effect">Add</button>
                  </div>
                  <table id="Phase_Table" class="table table-striped">
                    <thead>
                      <tr>
                        <th>Phase Name</th>
                        <th>Start Date</th>
                        <th>End Date</th>
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
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/dropzone/dropzone.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/multi-select/js/jquery.multi-select.js"></script> --%>


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
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-Sprite-Preloaders/jquery.preloaders.min.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>
  <!-- <script src="AdminBSBMaterialDesign/js/pages/forms/basic-form-elements.js"></script> -->
  <!-- Demo Js -->
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script> --%>

  <script>
  $(function () {
	var table = '#Phase_Table';
	var id="${param.id}";

	$('#Appr_Cycle_Form').formValidator({
      onFinishing: function () {
    	var apprCycle={};
    	var phases=[];
    	apprCycle.id=id;
    	apprCycle.name=$('#Cycle_Name').val();
    	apprCycle.status='DRAFT';
    	apprCycle.startDate=$('#Cycle_StartDate').val();
    	apprCycle.endDate=$('#Cycle_EndDate').val();
    	apprCycle.cutoffDate=$('#Cycle_EligibilityDate').val();
    	
		$('#Phase_Table > tbody tr').each(function(index, row) {
			var $tds = $(this).find('td')
			var phase={};
			phase.id=$tds.eq(0).attr('item-id');
			phase.name=$tds.eq(0).text();
			phase.startDate=$tds.eq(1).text();
			phase.endDate=$tds.eq(2).text();
			phases[phases.length]=phase;
		});
    	apprCycle.phases=phases;
    	$.fn.ajaxPost({url : '<%=request.getContextPath()%>/appraisal/update', data: apprCycle});
      },
	});

    if (id != 0) {
      $.fn.ajaxGet({
    	url: '<%=request.getContextPath()%>/appraisal/list/' + id,
    	onSuccess: function(result) {
          if (result) {
            $('#Cycle_Name').val(result.name);
            $('#Cycle_StartDate').val(result.startDate);
            $('#Cycle_EndDate').val(result.endDate);
            $('#Cycle_EligibilityDate').val(result.cutoffDate);
      	    $(result.phases).each(function(index, phase) {
      	      appendRow(table, phase);
      	    });
       	  }
    	}
      });
    }

    $('.datepicker').bootstrapMaterialDatePicker({format: 'DD/MM/YYYY', clearButton: true, weekStart: 1, time: false });
	$('#Phase_Add').click(function() {
	  // Validate the input fields
	  appendRow(table, {id:0, name: $('#Phase_Name').val(),
		  startDate: $('#Phase_StartDate').val(), endDate: $('#Phase_EndDate').val()});

   	  $('#Phase_Name').val('');
   	  $('#Phase_StartDate').val('');
   	  $('#Phase_EndDate').val('');
	});

    function appendRow(tableSelector, phase) {
   	  var row=$('<tr>');
      $(row).append('<td item-id=' + phase.id
        + ' item-name=' + phase.name
        + ' item-startDate=' + phase.startDate
        + ' item-endDate=' + phase.endDate
        + '>' + phase.name + '</td>');
      $(row).append('<td>' + phase.startDate + '</td>');
      $(row).append('<td>' + phase.endDate + '</td>');
      $(''+tableSelector).find('tbody:last-child').append(row);
   	}

	$('#Cycle_StartDate').change(function(e, date) {
	  $('#Cycle_EndDate').bootstrapMaterialDatePicker('setMinDate', moment(date).add(1, 'day'));
	  //$('#Cycle_EligibilityDate').bootstrapMaterialDatePicker('setMaxDate', moment(date).subtract(1, 'day'));
	});

	$('#Cycle_EndDate').change(function(e, date) {
	  $('#Cycle_EligibilityDate').val("");
	  $('#Cycle_EligibilityDate').bootstrapMaterialDatePicker('setMaxDate', moment(date).subtract(1, 'month'));
	});

    $('#Phase_StartDate').change(function(e, date) {
      $('#Phase_EndDate').bootstrapMaterialDatePicker('setMinDate', moment(date).add(1, 'day'));
	});
  });

  </script>

</html>