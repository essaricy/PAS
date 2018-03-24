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
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/favicon.ico">
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
        <h2>DASHBOARD</h2>
      </div>

	  <div class="row clearfix PhasewiseEmployeeStatusCount_Row">
	  </div>
      <!-- Basic Example -->
      <div class="row clearfix">
      	<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
		  <div class="card">
		    <!-- user profile image -->
            <img class="card-img-top" src="${imageUrl}" alt="${userName} Profile Picture" style="width:100%">
            <div class="card-body text-center">
              <!-- user name -->
              <h4 class="card-title">${userName}</h4>
              <!-- job title / company name -->
              <p class="card-text font-bold col-pink">${designation}</p>
              <h6>${employeeId} | ${band}</h6>
              <p>Joined On: <fmt:formatDate pattern="MMM dd, yyyy" value="${joinedDate}" /></p>
              <p>&nbsp;</p>
            </div>
          </div>
      	</div>
      	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9">
      	</div>
      </div>

      <!-- Basic Example -->
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <!-- <div class="card">
            <div class="header">
              <h2>Employee Report for the current Appraisal Cycle</h2>
            </div>
            <div class="body">
              <table class="table table-striped table-condensed">
              	<thead>
              	  <tr>
              	  	<th>Manager</th>
              	  	<th>Employee ID</th>
              	  	<th>Employee Name</th>
              	  	<th>Phase 1</th>
              	  	<th>Phase 2</th>
              	  	<th>Final Score</th>
              	  </tr>
              	</thead>
              </table>
            </div>
          </div> -->
        </div>
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
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-Sprite-Preloaders/jquery.preloaders.min.js"></script>
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
    console.log('isAdmin? ' + isAdmin);
    console.log('isManager? ' + isManager);

    var getPhasewiseStatusCount=function() {
   	  $.fn.ajaxGet({
        url: '<%=request.getContextPath()%>/report/manager/phase/status/count',
        onSuccess: function(phasewiseEmployeeStatusCounts) {
      	var phasewiseEmployeeStatusCountRow = $('.PhasewiseEmployeeStatusCount_Row');
      	$(phasewiseEmployeeStatusCounts).each(function(index, phasewiseEmployeeStatusCount) {
            var phaseStatus=getPhaseAssignmentStatus(phasewiseEmployeeStatusCount.phaseAssignmentStatus.code);
            var iconName=phaseStatus.icon;
            var count=phasewiseEmployeeStatusCount.count;
            console.log(iconName);

            var colDiv=$('<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">');
            //var colDiv=$('<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">');
            //var colDiv=$('<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">');
            //var colDiv=$('<div>');
            var infoBoxDiv=$('<div class="info-box hover-zoom-effect ' + phaseStatus.colorClass + '">');
            var iconDiv=$('<div class="icon">');
            var icon=$('<i class="material-icons">' + iconName + '</i>');
            var contentDiv=$('<div class="content">');
            var textDiv=$('<div class="text">' + phaseStatus.name + '</div>');
            var countDiv=$('<div class="number">' + count + '</div>');

            $(phasewiseEmployeeStatusCountRow).append(colDiv);
            $(colDiv).append(infoBoxDiv);
            $(infoBoxDiv).append(iconDiv);
            $(iconDiv).append(icon);
            $(infoBoxDiv).append(contentDiv);
            $(contentDiv).append(textDiv);
            $(contentDiv).append(countDiv);
      	});
        }
      });
    }

    if (isManager) {
    	getPhasewiseStatusCount();
    }
    <%-- $.fn.ajaxGet({
  	  url: '<%=request.getContextPath()%>/dummy/delay/SUCCESS/5'
    }); --%>
  });
  </script>
</body>
</html>