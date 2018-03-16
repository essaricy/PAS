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
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css" />
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
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
        <h2>Competency Assessment Templates
          <small>Create or update the assessment templates that fits to your project or employee groups</small>
        </h2>
      </div>
      <div class="row clearfix">
        <!-- Linked Items -->
        <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <div class="card templates_card">
            <div class="header">
              <h2>Templates</h2>
              <ul class="header-dropdown m-r--5">
                <li class="dropdown">
                </li>
              </ul>
            </div>
            <div class="body">
            </div>
          </div>
        </div>
        <div class="col-lg-8 col-md-8 col-sm-12 col-xs-12">
          <div class="card template_header_card">
            <div class="header">
              <h2>Competency Assessments</h2>
            </div>
            <div class="body">
            </div>
          </div>
          <div class="card template_detail_card">
            <div class="header">
              <h2>Assessment Parameters</h2>
            </div>
            <div class="body">
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
<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script>
$('.templates_card').cardManager({
  type: 'list-with-links',
  loadUrl: '<%=request.getContextPath()%>/template/list',
  manageUrl: '<%=request.getContextPath()%>/admin/template/manage',
  deleteUrl: '<%=request.getContextPath()%>/template/delete',
  menuActions: ["Add", "Update", "Delete"],
  onClickCallback: renderTemplateInformation,
});

var headerCard=$('.template_header_card');
var detailCard=$('.template_detail_card');
$(headerCard).hide();
$(detailCard).hide();

function renderTemplateInformation(template) {
  $(detailCard).hide();
  $(headerCard).show();
  $(headerCard).find('.header h2').empty();
  $(headerCard).find('.body').empty();

  $(headerCard).find('.header h2').append(template.name);
  $(headerCard).find('.header h2').append('<small>Updated By <code>' + template.updatedBy + '</code> on ' + template.updatedAt +'</small>');

  var table=$('<table class="table table-striped">');
  $(table).append('<thead><tr><th>Competency Assessment</th><th>Weightage</th></tr></thead>');
  var tbody=$('<tbody>');
  $(table).append(tbody);
  $(headerCard).find('.body').append(table);

  $(template.headers).each(function (index, header) {
	if (header.weightage != 0) {
	  var row=$('<tr>');
	  var goalNameTd=$('<td>');
	  var goalNameLink=$('<a href="#">');
	  $(goalNameLink).append(header.goalName);
	  $(goalNameTd).append(goalNameLink);
	  $(row).append(goalNameTd);
	  $(row).append('<td><b>' + header.weightage + '%</b></td>');
      $(tbody).append(row);
      $(goalNameLink).click(function() {
    	  $(detailCard).show();
    	  $(detailCard).find('.header h2').empty();
    	  $(detailCard).find('.body').empty();

    	  $(detailCard).find('.header h2').append(header.goalName);
    	  var detailTable=$('<table class="table table-striped">');
    	  var detailTbody=$('<tbody>');
    	  $(detailTable).append(detailTbody);
    	  $(detailCard).find('.body').append(detailTable);
    	  $(header.details).each(function (index, detail) {
    		  if (detail.apply=='Y') {
    		    $(detailTbody).append('<tr><td>' + detail.paramName + '</td><td><div class="switch pull-right"><label><input type="checkbox" disabled checked><span class="lever switch-col-green"></span></label></div></td></tr>');
    		  } else {
    		    $(detailTbody).append('<tr><td>' + detail.paramName + '</td><td><div class="switch pull-right"><label><input type="checkbox" disabled><span class="lever switch-col-green"></span></label></div></td></tr>');
    		  }
    	  });
      });
	}
  });
}
</script>
</html>
