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
.timeline {
  list-style: none;
  padding: 20px 0 20px;
  position: relative;
}
.timeline:before {
  top: 0;
  bottom: 0;
  position: absolute;
  content: " ";
  width: 3px;
  background-color: #eeeeee;
  left: 50%;
  margin-left: -1.5px;
}
.timeline > li {
  margin-bottom: 20px;
  position: relative;
}
.timeline > li:before,
.timeline > li:after {
  content: " ";
  display: table;
}
.timeline > li:after {
  clear: both;
}
.timeline > li:before,
.timeline > li:after {
  content: " ";
  display: table;
}
.timeline > li:after {
  clear: both;
}
.timeline > li > .timeline-panel {
  width: 50%;
  float: left;
  border: 1px solid #d4d4d4;
  border-radius: 2px;
  padding: 20px;
  position: relative;
  -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);
}
.timeline > li.timeline-inverted + li:not(.timeline-inverted),
.timeline > li:not(.timeline-inverted) + li.timeline-inverted {
margin-top: -60px;
}

.timeline > li:not(.timeline-inverted) {
padding-right:90px;
}

.timeline > li.timeline-inverted {
padding-left:90px;
}
.timeline > li > .timeline-panel:before {
  position: absolute;
  top: 26px;
  right: -15px;
  display: inline-block;
  border-top: 15px solid transparent;
  border-left: 15px solid #ccc;
  border-right: 0 solid #ccc;
  border-bottom: 15px solid transparent;
  content: " ";
}
.timeline > li > .timeline-panel:after {
  position: absolute;
  top: 27px;
  right: -14px;
  display: inline-block;
  border-top: 14px solid transparent;
  border-left: 14px solid #fff;
  border-right: 0 solid #fff;
  border-bottom: 14px solid transparent;
  content: " ";
}
.timeline > li > .timeline-badge {
  color: #fff;
  width: 50px;
  height: 50px;
  line-height: 50px;
  font-size: 1.4em;
  text-align: center;
  position: absolute;
  top: 16px;
  left: 50%;
  margin-left: -25px;
  background-color: #999999;
  /* z-index: 100; */
  border-top-right-radius: 50%;
  border-top-left-radius: 50%;
  border-bottom-right-radius: 50%;
  border-bottom-left-radius: 50%;
}
.timeline > li.timeline-inverted > .timeline-panel {
  float: right;
}
.timeline > li.timeline-inverted > .timeline-panel:before {
  border-left-width: 0;
  border-right-width: 15px;
  left: -15px;
  right: auto;
}
.timeline > li.timeline-inverted > .timeline-panel:after {
  border-left-width: 0;
  border-right-width: 14px;
  left: -14px;
  right: auto;
}
.timeline-badge.primary {
  background-color: #2e6da4 !important;
}
.timeline-badge.success {
  background-color: #3f903f !important;
}
.timeline-badge.warning {
  background-color: #f0ad4e !important;
}
.timeline-badge.danger {
  background-color: #d9534f !important;
}
.timeline-badge.info {
  background-color: #5bc0de !important;
}
.timeline-title {
  margin-top: 0;
  color: inherit;
}
.timeline-body > p,
.timeline-body > ul {
  margin-bottom: 0;
}
.timeline-body > p + p {
  margin-top: 5px;
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
 	  <div class="row clearfix">
 	    <div class="card timeline_card">
 	      <div class="header">
 	      </div>
 	      <div class="body">
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
  <!-- Moment Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>

  <script type="text/javascript">
  $(function () {
	$.fn.ajaxGet({
	  url: '<%=request.getContextPath()%>/api/admin/report/activity/timeline/active',
	  onSuccess: renderActivityTimeline,
	  onError: function (error) {
		  console.log("onError");
	  },
	});
  });

  function renderActivityTimeline(assignmentAudits) {
	  console.log(assignmentAudits);
	  var card = $('.timeline_card');
	  var cardBody=$(card).find('.body');
	  var row=$('<div class="row">');
	  var ul=$('<ul class="timeline">');

	  $(cardBody).append(row);
	  $(row).append(ul);

	  $(assignmentAudits).each(function (index, assignmentAudit) {
		  delete assignmentAudit.phase.startDate;
		  delete assignmentAudit.phase.endDate;

		  delete assignmentAudit.template.updatedBy;
		  delete assignmentAudit.template.updatedAt;
		  delete assignmentAudit.template.headers;

		  delete assignmentAudit.assignedTo.firstName;
		  delete assignmentAudit.assignedTo.lastName;
		  delete assignmentAudit.assignedTo.employmentType;
		  delete assignmentAudit.assignedTo.designation;
		  delete assignmentAudit.assignedTo.band;
		  delete assignmentAudit.assignedTo.hiredOn;
		  delete assignmentAudit.assignedTo.location;
		  delete assignmentAudit.assignedTo.loginId;

		  delete assignmentAudit.assignedBy.firstName;
		  delete assignmentAudit.assignedBy.lastName;
		  delete assignmentAudit.assignedBy.employmentType;
		  delete assignmentAudit.assignedBy.designation;
		  delete assignmentAudit.assignedBy.band;
		  delete assignmentAudit.assignedBy.hiredOn;
		  delete assignmentAudit.assignedBy.location;
		  delete assignmentAudit.assignedBy.loginId;
	  });
	  
	  $(assignmentAudits).each(function (index, assignmentAudit) {
		  var li=$('<li>');
		  if (index %2 == 0) $(li).addClass("timeline-inverted");

		  var badge=('<div class="timeline-badge"><i class="glyphicon glyphicon-check"></i></div>');
		  var panel=$('<div class="timeline-panel">');
		  var panelHeading=$('<div class="timeline-heading">');
		  var title=$('<h4 class="timeline-title">');
		  var panelHeadingPara=$('<p>');
		  var panelSmallText=$('<small class="text-muted">');
		  var panelBody=$('<div class="timeline-body">');

		  $(ul).append(li);
		  $(li).append(badge);
		  $(li).append(panel);
		  $(panel).append(panelHeading);
		  $(panelHeading).append(title);

		  /* var previousAudit = getPreviousAudit(index, assignmentAudit, assignmentAudits);
		  if (previousAudit == null) {
			  $(title).append('<p>No previous Audit</p>');
		  } else {
			  $(title).append('<p>' + getIdentifiedChange(assignmentAudit, previousAudit) + '</p>');
		  } */
		  $(title).append('<p>' + assignmentAudit.assignedTo.fullName + '</p>');

		  //$(title).append("Title");
		  $(panelHeading).append(panelHeadingPara);
		  //$(panelHeading).append('<p><small class="text-muted"><i class="glyphicon glyphicon-time"></i> 11 hours ago via Twitter</small></p>');
		  $(panelHeadingPara).append(panelSmallText);
		  $(panelSmallText).append('<i class="glyphicon glyphicon-time">');
		  $(panelSmallText).append(' ' + moment(assignmentAudit.timestamp).format("DD/MM/YYYY hh:mm  A"));
		  $(panel).append(panelBody);

		  var previousAudit = getPreviousAudit(index, assignmentAudit, assignmentAudits);
		  if (previousAudit != null) {
			  $(panelBody).append('<p>' + getIdentifiedChange(assignmentAudit, previousAudit) + '</p>');
		  }
	  });
  }
  
  function getPreviousAudit(currentIndex, currentAssignmentAudit, assignmentAudits) {
	  if (currentIndex+1 < assignmentAudits.length) {
		  for (var index = currentIndex+1; index < assignmentAudits.length; index++) {
			  var assignmentAudit = assignmentAudits[index];
			  if (currentAssignmentAudit.phase.id == assignmentAudit.phase.id
					  && currentAssignmentAudit.assignedTo.employeeId == assignmentAudit.assignedTo.employeeId) {
				  return assignmentAudit;
			  }
		}
	  }
	  return null;
  }
  
  function getIdentifiedChange(current, previous) {
	  var identifiedChange = null;
	  if (current.template.id != previous.template.id) {
		  identifiedChange = "Template Changed";
	  } else if (current.assignedBy.employeeId != previous.assignedBy.employeeId) {
		  identifiedChange = "Assessing manager changed from " + previous.assignedBy.fullName + " to " + current.assignedBy.fullName;
	  } else if (current.status != previous.status) {
		  var status = getPhaseAssignmentStatus(current.status);
		  identifiedChange = current.phase.name + ": " + status.description;
	  } else if (current.score != previous.score) {
		  identifiedChange = "Score Changed from " + previous.score + " to " + current.score;
	  } else {
		  //identifiedChange = "Action : " + current.action;
	  }
	  //return identifiedChange + JSON.stringify(current) + JSON.stringify(previous);
	  return identifiedChange;
  }


  </script>
</body>
</html>