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
  /* .progress-bar {
    -webkit-transition: width 2.5s ease;
    transition: width 2.5s ease;
  } */
  .progress-bar {
    text-transform: uppercase;
  }
  .info-box {
    cursor: pointer;
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
        <h2>DASHBOARD</h2>
      </div>

	  <div class="row clearfix PhasewiseEmployeeStatusCount_Row">
	  </div>

      <!-- Basic Example -->
      <div class="row clearfix">
      	<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
		  <div class="card">
		    <!-- user profile image -->
            <img class="card-img-top" src="${imageUrl}" alt="${userName} Profile Picture" style="width:100%">
            <div class="card-body text-center">
              <!-- user name -->
              <h4 class="card-title">${userName}</h4>
              <!-- job title / company name -->
              <p class="card-text font-bold col-pink">${designation}</p>
              <h6>${employeeId} | ${band}</h6>
              <%-- <p>Joined On: <fmt:formatDate pattern="MMM dd, yyyy" value="${joinedDate}" /></p> --%>
              <p>&nbsp;</p>
            </div>
          </div>
      	</div>
      	<div class="col-lg-9 col-md-6 col-sm-6 col-xs-12">
      	  <div class="card">
      	    <div class="header"><h2>Current Appraisal Status</h2>
      	    </div>
      	    <div class="body">
      	      <table class="table table-striped" width="100%" id="CurrentAppraisalStatus_Table">
      	        <thead>
      	          <tr>
      	            <th>Phase</th>
      	            <th>Progress</th>
      	          </tr>
      	        </thead>
      	        <tbody>
      	        </tbody>
      	      </table>
      	    </div>
       	  </div>
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
        url: '<%=request.getContextPath()%>/api/manager/report/phase/status/count',
        onSuccess: function(phasewiseEmployeeStatusCounts) {
      	var phasewiseEmployeeStatusCountRow = $('.PhasewiseEmployeeStatusCount_Row');
      	  $(phasewiseEmployeeStatusCounts).each(function(index, phasewiseEmployeeStatusCount) {
            var phaseStatus=getPhaseAssignmentStatus(phasewiseEmployeeStatusCount.phaseAssignmentStatus.code);
            var iconName=phaseStatus.icon;
            var count=phasewiseEmployeeStatusCount.count;

            var colDiv=$('<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">');
            var infoBoxDiv=$('<div class="info-box hover-zoom-effect hover-expand-effect ' + phaseStatus.colorClass + '">');
            var iconDiv=$('<div class="icon">');
            //var infoBoxDiv=$('<div class="info-box hover-zoom-effect ">');
            //var iconDiv=$('<div class="icon ' + phaseStatus.colorClass + '">');
            var icon=$('<i class="material-icons">' + iconName + '</i>');
            var contentDiv=$('<div class="content">');
            var textDiv=$('<div class="text">' + phaseStatus.name + '</div>');
            var countDiv=$('<div class="number">' + count + '</div>');
            $(infoBoxDiv).click(function () {
              location.href='<%=request.getContextPath()%>/manager/assignment/list';
            });

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

    var getCurrentAppraisalStatus=function() {
   	  $.fn.ajaxGet({
        url: '<%=request.getContextPath()%>/api/employee/report/cycle/status',
        onSuccess: function(phaseAssignments) {
          var currentAppraisalStatus_Table = $('#CurrentAppraisalStatus_Table');
          var tbody=$(currentAppraisalStatus_Table).find('tbody');
          $(phaseAssignments).each(function(index, phaseAssignment) {
        	var employeeAssignment = phaseAssignment.employeeAssignments[0];
            var phaseStatus=getPhaseAssignmentStatus(employeeAssignment.status);

            var row=$('<tr>');
            var phaseNameTd=$('<td width="20%">');
            $(phaseNameTd).append(phaseAssignment.phase.name);

            var percentage=100;
            var progressClass="bg-grey";
            var progressAnimationClass="";
            var progressText="Not assigned yet";
            if (phaseStatus != null && phaseStatus != PhaseAssignmentStatus.NOT_INITIATED) {
              percentage=(phaseStatus.code/PhaseAssignmentStatus.CONCLUDED.code)*100;
              progressClass=phaseStatus.colorClass;
              progressText=phaseStatus.description;

              if (phaseStatus != PhaseAssignmentStatus.CONCLUDED) {
                progressAnimationClass="progress-bar-striped active";
              }
            }
            var progressTd=$('<td>');
            var progressDiv=$('<div class="progress">');
            var progressBarDiv=$('<div class="progress-bar ' + progressClass + " " + progressAnimationClass + '" role="progressbar" '
            		+ 'aria-valuenow="' + percentage + '" aria-valuemin="0" '
            		+ 'aria-valuemax="100">');
            $(progressDiv).tooltip({container: 'body'});
            $(progressBarDiv).animate({width: percentage + "%"}, 1000);

            $(row).append(phaseNameTd);
            $(tbody).append(row);
            $(progressDiv).append(progressBarDiv);
            $(progressTd).append(progressDiv);
            $(progressTd).append('<small>' + progressText + '</small>');
            $(row).append(progressTd);            
        	});
          }
        });
      }

    if (isManager) {
    	getPhasewiseStatusCount();
    }
   	getCurrentAppraisalStatus();
    <%-- $.fn.ajaxGet({
  	  url: '<%=request.getContextPath()%>/dummy/delay/SUCCESS/5'
    }); --%>
  });
  </script>
</body>
</html>