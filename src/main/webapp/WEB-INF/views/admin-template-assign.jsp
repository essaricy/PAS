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
    <!-- EasyAutocomplete-1.3.5 -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/easy-autocomplete.min.css"/>
    <!-- Bootstrap Tagsinput Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-tagsinput/bootstrap-tagsinput.css"/>
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
        <h2>Assign Competency Assessment Templates to Employees
          <small>Assign templates to employee(s)</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-50 col-sm-12 col-xs-12">
          <div class="card template_assign_card">
            <div class="body">
              <div class="row clearfix">
			    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
                  <label for="Template_Name">Template Name</label>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
		          <div class="form-group form-float">
		            <div class="form-line">
		              <input type="text" id="Template_Name" class="form-control" placeholder="Start enetering template name" required autofocus value="">
                    </div>
                  </div>
                </div>
              </div>
              <div class="row clearfix">
			    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
                  <label for="Search_Employees">Select Employees</label>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
		          <div class="form-group form-float">
		            <div class="form-line">
		              <input type="text" id="Search_Employees" class="form-control" placeholder="Start enetering employee name" value="">
                    </div>
                  </div>
                </div>
              </div>
              <div class="row clearfix">
			    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
                  <label for="Selected_Employees_List">Selected Employees</label>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
		          <ul class="list-group" id="Selected_Employees_List">
                  </ul>
                </div>
              </div>
              <div class="row clearfix">
			    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		          <button id="Assign_Button" name="assign" class="btn btn-primary center-block">Assign</button>
                </div>
              </div>
              <div class="row clearfix">
                <ul class="list-group" id="Assignment_Results">
                </ul>
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 results_container">
                </div>
              </div>
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
<!-- EasyAutocomplete-1.3.5 -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/jquery.easy-autocomplete.min.js"></script>
<!-- EasyAutocomplete-1.3.5 -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-tagsinput/bootstrap-tagsinput.js"></script>
<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script>
var bulkAssignment={};
/* bulkAssignment.templateId=4;
bulkAssignment.cycleId=11;
bulkAssignment.employeeIds=[1136,2388,2006]; */
bulkAssignment.templateId=0;
bulkAssignment.cycleId=0;
bulkAssignment.employeeIds=[];

$(function () {
  $.fn.ajaxGet({
	url: '<%=request.getContextPath()%>/appraisal/get/active',
	onSuccess: onActiveAvailable,
	onError: onError
  });

   $("#Template_Name").easyAutocomplete({
	url: function(phrase) {
	  return "<%=request.getContextPath()%>/template/search/byName/" + phrase;
	},
	dataType: "json",
	getValue: "name",
	template: {
	  type: "custom",
	  method: function(value, item) {
		return "<b>" + item.name + "</b><small> - Updated By <code>" + item.updatedBy + "</code> on <code>" + item.updatedAt + "</code></small>";
	  }
	},
	list: {
	  onClickEvent: function() {
		bulkAssignment.templateId=$("#Template_Name").getSelectedItemData().id;
	  }	
	}
  });

  $("#Search_Employees").easyAutocomplete({
	url: function(phrase) {
	  return "<%=request.getContextPath()%>/employee/search/" + phrase;
	},
	dataType: "json",
	getValue: function (item) { return item.FirstName + " " + item.LastName},
	template: {
	  type: "custom",
	  method: function(value, item) {
		return "<b>" + item.FirstName + " " + item.LastName + "</b> [<code>" + item.EmployeeId + "</code>]</small>";
	  }
	},
	list: {
	  onClickEvent: function() {
		var data=$("#Search_Employees").getSelectedItemData();
		var itemId=data.EmployeeId;
		var itemValue=data.FirstName + " " + data.LastName;

		if(!$('#Selected_Employees_List li:contains("' + itemValue + '")').length) {
 		  // Insert your new li
 		  bulkAssignment.employeeIds.push(itemId);
 		 $('#Selected_Employees_List').append('<li class="list-group-item" item-id="' + itemId + '">' + itemValue + '<span class="pull-right"><i class="material-icons" style="cursor: pointer;" onclick="javascript: removeDataItem(this, ' + itemId + ');">clear</i></span></li>');
		}
		$("#Search_Employees").val('');
	  }	
	}
  });

  $('#Assign_Button').click(function() {
	if (bulkAssignment.templateId == 0) {
	  swal({title: "Missing Information", text: "Please select a template", type: "warning"});
    } else if (bulkAssignment.employeeIds.length == 0) {
	  swal({title: "Missing Information", text: "Please select at least an employee to assign", type: "warning"});
    } else {
      console.log(JSON.stringify(bulkAssignment));
	  $.fn.postJSON({url:'<%=request.getContextPath()%>/assignment/save/bulk', data: bulkAssignment,
		onSuccess: function(result) {
		  console.log('result=' + result);
		  var results_container=$('#Assignment_Results');
		  $(results_container).empty();
		  $(result).each(function(index, aResult) {
			if (aResult.code=='SUCCESS') {
			  $('#Assignment_Results').append('<li class="list-group-item bg-green">' + aResult.message + '<span class="pull-right"><i class="material-icons">done</i></span></li>');
			} else {
			  $('#Assignment_Results').append('<li class="list-group-item bg-red">' + aResult.message + '<span class="pull-right"><i class="material-icons">error_outline</i></span></li>');
			}
		  });
		},
		onFail: function(message, content) {
		  console.log('message=' + message);			
		}
	  });
    }
  });

  function onActiveAvailable(result) {
    if (result== null || result=="" || result=="undefined") {
	  onError("There is no Appraisal Cycle ACTIVE right now. You can assign a template to employees only when there is an appraisal cycle is ACTIVE.");
	  return;
	}
    bulkAssignment.cycleId=result.id;
	console.log('onActiveAvailable result=' + result);
  }
  function onError(error) {
	console.log('onError error=' + error);
	$('.template_assign_card .body').empty();
	$('.template_assign_card .body').append('<p class="font-bold col-pink">' + error + '</p>');
  }
});

function removeDataItem(item, itemId) {
  console.log('itemId=' + itemId);
  $(item).parent().parent().remove();
  bulkAssignment.employeeIds=$.grep(bulkAssignment.employeeIds, function(value) { return value != itemId;});
}

</script>
</html>
