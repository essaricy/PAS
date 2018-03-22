<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

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

      <!-- Basic Example -->
      <div class="row clearfix PhasewiseEmployeeStatusCount_Row">
      </div>

      <!-- Basic Example -->
      <div class="row clearfix">
        <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
          <div class="card">
            <div class="header bg-orange">
              <h2>
                ${userName}
                <small>${designation}, ${location}</small>
              </h2>
            </div>
            <div class="body">
              <div class="row clearfix">
                <div class="col-md-6">Employee #</div><div class="col-md-6">${employeeId}</div>
                <div class="col-md-6">Band</div><div class="col-md-6">${band}</div>
                <div class="col-md-6">Joined Date</div><div class="col-md-6"><fmt:formatDate pattern="MMM dd, yyyy" value="${joinedDate}" /></div>
              </div>
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
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-Sprite-Preloaders/jquery.preloaders.min.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>

  <script type="text/javascript">
  $(function () {
	<%-- $.fn.ajaxGet({
  	  url: '<%=request.getContextPath()%>/dummy/delay/SUCCESS/5'
    }); --%>
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
          var infoBoxDiv=$('<div class="info-box hover-zoom-effect">');
          var iconDiv=$('<div class="icon ' + phaseStatus.colorClass + '">');
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
          /* $(phasewiseEmployeeStatusCountRow).append(
            '<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">' + 
            '  <div class="info-box hover-zoom-effect">' +
            '    <div class="icon bg-pink">' +
            '      <i class="material-icons">email</i>' +
            '    </div>' +
            '    <div class="content">' +
            '      <div class="text">MESSAGES</div>' +
            '    <div class="number">15</div>' +
            '  </div>' +
            '</div>'); */
    	});
      }
    });
  });
  </script>
</body>
</html>