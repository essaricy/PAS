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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/star-rating-svg/star-rating-svg.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
  </head>
  <style>
  .list-group-item {
  	font-size: 13px;
  }
  .self-appraisal {
  }
  .weightage, .self-score {
  	font-weight: bold;
  }
  table > tfoot > tr > th {
    font-size: 20px;
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
        <h2>My Appraisal
          <small>View and submit the appraisal form</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card assessment_card">
            <div class="header">
              <h2>Phase Information Here</h2>
            </div>
            <div class="body">
              <div class="row clearfix">
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th>Competency Assessment</th>
                    <th>Weightage [%]</th>
                    <!-- <th>Comments</th>
                    <th>Rating</th>
                    <th>Score</th> -->
                </thead>
                <tfoot></tfoot>
                <tbody>
                </tbody>
              </table>
              </div>

              <div class="row clearfix">
              <button class="btn bg-light-blue pull-right">Submit</button>
              </button>
              <!-- Modal Dialogs ====================================================================================================================== -->
              <!-- Default Size -->
              <div class="modal fade" id="GoalModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h4 class="modal-title"></h4>
                    </div>
                    <div class="modal-body">
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>
                    </div>
                  </div>
                </div>
              </div>
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
<!-- SweetAlert Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
<!-- Validation Plugin Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/star-rating-svg/jquery.star-rating-svg.js"></script>

<!-- Custom Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/pages/ui/tooltips-popovers.js"></script>


<!-- Demo Js -->
<script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>

<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/card-manager.js"></script>
<script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-assignments.js"></script>
<script>
$(function () {
  var assignmentId='${param.assignmentId}';
  $.fn.ajaxGet({
	url: '<%=request.getContextPath()%>/assessment/list/assign/' + assignmentId,
	onSuccess: onLoadSuccess,
	onFail: onLoadFail
  });
 
  function onLoadSuccess(result) {
	var ea=result.employeeAssignment;
	var ea_status=ea.status;
	var isFormOpen = (ea_status == 10 );

	var card=$('.assessment_card');
	var table=$(card).find('.body table');
	var thead=$(table).find('thead');
	var tbody=$(table).find('tbody');
	var tfoot=$(table).find('tfoot');

	$(card).find('.header').append('<small>' + getPhaseStatus(ea_status) + '</small>');
	// Append template headers
	$(result.templateHeaders).each(function(index, templateHeader) {
		var goalId=templateHeader.goalId;
		var goalName=templateHeader.goalName;
		var weightage=templateHeader.weightage;

		var row=$('<tr>');
		var link=$('<a data-toggle="modal" data-target="#GoalModal">');
		$(link).append(goalName);
		$(link).click(function() {
		  $('#GoalModal').find('.modal-title').text(goalName);
		  $('#GoalModal').find('.modal-body').empty();
		  var ol=$('<ol>');
		  $('#GoalModal').find('.modal-body').append(ol);
		  $(templateHeader.details).each(function (index, detail) {
			  $(ol).append('<li>' + detail.paramName + "</li>");
		  });
		});
		var linkTd=$('<td item-id="' + goalId + '">');
		$(linkTd).append(link);
		$(row).append(linkTd);

		$(row).append('<td class="weightage">' + weightage + '</td>');
		$(table).find('tbody:last-child').append(row);
	});
	
	$(result.assignmentAssessments).each(function(index, aa) {
	  $(aa.employeeAssessments).each(function(jindex, ea) {
		if (jindex == 0) {
		  var classname=(ea.assessType != 0) ? "bg-light-green" : "bg-orange";
	      $(thead).find("tr:first").append(
	    		  '<th class="' + classname + '">Comments</th>' + 
	    		  '<th class="' + classname + '">Rating</th>' +
	    		  '<th class="' + classname + '">Score</th>');
		}
		$(tbody).find('tr td[item-id="' + ea.goal.id + '"]').parent().append(getCommentsCell(isFormOpen, ea.comments));
		$(tbody).find('tr td[item-id="' + ea.goal.id + '"]').parent().append(getRatingCell(isFormOpen, ea.rating));
		$(tbody).find('tr td[item-id="' + ea.goal.id + '"]').parent().append('<td class="self-score">' + ea.score + '</td>')
	  });
	});
	
	$(tfoot).append('<tr>' +
		'<th>&nbsp</th>' +
		'<th>0</th>' +
		'<th>&nbsp</th>' +
		'<th>&nbsp</th>' +
		'<th>0</th>'
	);
	setTotalScore();
	setColumnSum('.weightage', 1);
  }

  function getCommentsCell(isFormOpen, comments) {
	var commentsTd=$('<td>');
	var comments=$('<textarea maxlength="500" ' + (isFormOpen? "":"readonly") + '>');
	$(comments).append(comments);
	$(commentsTd).append(comments);
	return commentsTd;
  }

  function getRatingCell(isFormOpen, rating) {
    var ratingTd=$('<td>');
	var rating=$('<div class="starbox">');
	$(rating).starRating({
	  initialRating: rating,
	  totalStars: 5,
	  starSize: 18,
	  hoverColor: '#8BC34A',
	  ratedColor: '#4CAF50',
	  disableAfterRate: false,
	  readOnly: !isFormOpen,
	  onLeave: function(currentIndex, currentRating, el) {
	    var currentWeightage=$(el).closest('tr').find('.weightage').text();
	    if (isNaN(currentRating)) currentRating=0;
	    var weightedRating=(currentRating==0)? 0: ((currentRating*currentWeightage)/100);
		$(el).closest('tr').find('.self-score').text(weightedRating);
		setTotalScore();
	  }
	});
	$(ratingTd).append(rating);
	return ratingTd;
  }

  function setTotalScore() {
	  setColumnSum('.self-score', 4);
  }

  function setColumnSum(selector, columnIndex) {
	var tbody=$('.assessment_card .body table tbody');
	var tfoot=$('.assessment_card .body table tfoot');
    var totalScore=0;
    $('' + selector).each(function(index, td) {
    	totalScore+=parseFloat($(td).text());	
    });
	$(tfoot).find('tr th:eq(' + columnIndex + ')').text(totalScore);
  }

  function onLoadFail(error) {
	console.log('error= ' + JSON.stringify(error));
  }

});


</script>
</html>
