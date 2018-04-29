<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
  </head>
  <style>
  .progress-bar {
    text-transform: uppercase;
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
        <h2>Overview</h2>
      </div>

 	  <div class="row clearfix PhasewiseEmployeeStatusCount_Div">
	  </div>
	  
    </div>
  </section>

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
  <!-- Validation Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>

  <c:set var="isAdmin" value="false" />
  <c:set var="isManager" value="false" />
  
  <sec:authorize access="hasAuthority('ADMIN')">
    <c:set var="isAdmin" value="true" />
  </sec:authorize>
  <sec:authorize access="hasAuthority('MANAGER')">
    <c:set var="isManager" value="true" />
  </sec:authorize>

  <script type="text/javascript">
  $(function () {
	var isAdmin=${isAdmin};
	var isManager=${isManager};

    var getPhasewiseStatusCount=function() {
   	  $.fn.ajaxGet({
        url: '<%=request.getContextPath()%>/assignment/admin/phase/status/count',
        onSuccess: function(phasewiseEmployeeStatusCounts) {
      	  $(phasewiseEmployeeStatusCounts).each(function(index, phasewiseEmployeeStatusCount) {
      	   var phasewiseEmployeeStatusCountRow = $('<div class="row clearfix PhasewiseEmployeeStatusCount_Row">');
      		  
      		console.log('Phase Name -' + phasewiseEmployeeStatusCount.appraisalPhase.name);
      		console.log('Phase Id -' + phasewiseEmployeeStatusCount.appraisalPhase.id);
      		console.log('employeeAssignmentCounts -' + phasewiseEmployeeStatusCount.employeeAssignmentCounts);
      		var maindiv=$('<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">');
      		var card=$('<div class="card">');
      		var header=$('<div class="header">');
      		$(header).append('<h2>'+phasewiseEmployeeStatusCount.appraisalPhase.name+'</h2>');
      		$(card).append(header);
      		$(maindiv).append(card);
      		$(phasewiseEmployeeStatusCountRow).append(maindiv);
      		var phaseId=phasewiseEmployeeStatusCount.appraisalPhase.id;
      		var employeeAssignmentCounts = phasewiseEmployeeStatusCount.employeeAssignmentCounts;
      		
      		var tableDiv= $('<div class="body table-responsive">');
      		var dataTable = $('<table class="table table-condensed">')
      		var tableHeader=$('<thead>');
      		var tablebody=$('<tbody>');

      		var tablerow=$('<tr>');
      		$(tablerow).append('<th>#</th>');
      		$(tablerow).append('<th>Status</th>');
      		$(tablerow).append('<th>Count</th>'); 
      		
      		$(tableHeader).append(tablerow);
      		$(dataTable).append(tableHeader);
      		$(dataTable).append(tablebody);
      		$(tableDiv).append(dataTable);
      		$(card).append(tableDiv);
      		
      		$(employeeAssignmentCounts).each(function(index, employeeAssignmentCount) {
      			console.log('employeeAssignmentCount.employeeAssignmentStatus -' + employeeAssignmentCount.employeeAssignmentStatus.code);

      			var tableBodyRow=$('<tr>');
				var sequence=index+1;
				$(tableBodyRow).append('<td>'+sequence+'</td>');
	      		$(tableBodyRow).append('<td>'+getEmployeeStatusLabel(employeeAssignmentCount.employeeAssignmentStatus.code)+'</td>');
	      		$(tableBodyRow).append('<td>'+employeeAssignmentCount.count+'</td>');
	      		$(tablebody).append(tableBodyRow);
	            
      		});
      		$('.PhasewiseEmployeeStatusCount_Div').append(phasewiseEmployeeStatusCountRow);
      	  });
        }
      });
    }

    if (isAdmin) {
    	getPhasewiseStatusCount();
    }
    
   	//getCurrentAppraisalStatus();
    <%-- $.fn.ajaxGet({
  	  url: '<%=request.getContextPath()%>/dummy/delay/SUCCESS/5'
    }); --%>
  });
  </script>
</body>
</html>