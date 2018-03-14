$(function () {
  var assignmentId='${param.aid}';

  $.fn.renderAssessment=function( options ) {
    var settings=$.extend({
      contextPath: null,
	  url: null,
	  role: 'Employee',
	}, options );

    var card=$(this);
    var cardHeader=$(card).find('.header');
    var cardBody=$(card).find('.body');
    var rateYoOptions={ halfStar: true, starWidth: "18px", normalFill: '#CCC', ratedFill: '#4CAF50'};

	$.fn.ajaxGet({
   	  url: settings.url,
      onSuccess: parseResult,
      onError: onErrorAssessments
    });

	var assignment=null;
	var phase=null;
    var templateHeaders=null;
	var status=null;
	var phaseAssessHeaders=null;

	function parseResult(result) {
	  updateData(result);
	  renderAssessments();
	}

	function updateData(result) {
      assignment=result.employeeAssignment;
      phase=result.phase;
      templateHeaders=result.templateHeaders;
  	  status=getPhaseAssignmentStatus(assignment.status);
  	  phaseAssessHeaders=result.phaseAssessmentHeaders;
	}

	function renderAssessments() {
	  renderTitles();
      renderGoalPopup();
      renderTemplateInfo();

      updateModelByFormStatus();
      renderAllAssessments();
      renderActionButtons();
    }

    function renderTitles() {
      $(cardHeader).append('<h2>Phase: ' + phase.name + '</h2>'
        + 'Phase starts on <code>' + phase.startDate + '</code> '
        + 'and ends on <code>' + phase.endDate + '</code><br/>'
        + 'Your form status is ' + getPhaseStatusLabel(status.code));
    }

    function renderGoalPopup() {
      var modal=$('<div class="modal fade" tabindex="-1" role="dialog" id="GoalParamsModal">');
      var modalDialog=$('<div class="modal-dialog" role="document">');
      var modalContent=$('<div class="modal-content">');
      var modalHeader=$('<div class="modal-header">');
      var modalTitle=$('<h4 class="modal-title"></h4>');
      var modalBody=$('<div class="modal-body">');
      var modalFooter=$('<div class="modal-footer">');
      var closeButton=$('<button type="button" class="btn btn-link waves-effect" data-dismiss="modal">CLOSE</button>');

      $(card).append(modal);
      $(modal).append(modalDialog);
      $(modalDialog).append(modalContent);
      $(modalContent).append(modalHeader);
      $(modalHeader).append(modalTitle);
      $(modalContent).append(modalBody);
      $(modalContent).append(modalFooter);
      $(modalFooter).append(closeButton);
    }

    function renderTemplateInfo() {
      var divRow=$('<div class="row clearfix">');
      var table=$('<table class="table table-striped">');
      var thead=$('<thead>');
      var tbody=$('<tbody>');
      var tfoot=$('<tfoot>');
      var headRow=$('<tr>');

      $(cardBody).append(divRow);
      $(divRow).append(table);
      $(table).append(thead);
      $(thead).append(headRow);
      $(headRow).append('<th>Competency Assessment</th>');
      $(headRow).append('<th>Weightage [%]</th>');
      $(table).append(tbody);
      $(table).append(tfoot);

      // Append template headers
      var totalWeightage=0;
      $(templateHeaders).each(function(index, templateHeader) {
   		var goalId=templateHeader.goalId;
   		var goalName=templateHeader.goalName;
   		var weightage=templateHeader.weightage;
   		totalWeightage+=weightage;

   		var modal=$('#GoalParamsModal');
   		var row=$('<tr>');
   		var link=$('<a data-toggle="modal" data-target="#' + $(modal).attr('id') + '">');
   		$(link).append(goalName);
   		$(link).click(function() {
   		  $(modal).find('.modal-title').text(goalName);
   		  $(modal).find('.modal-body').empty();
   		  var ol=$('<ol>');
   		  $(modal).find('.modal-body').append(ol);
   		  $(templateHeader.details).each(function (index, detail) {
		    $(ol).append('<li>' + detail.paramName + "</li>");
   		  });
   		});

   		var linkTd=$('<td item-id="' + goalId + '">');
   		$(linkTd).append(link);
   		$(row).append(linkTd);
   		$(row).append('<td class="weightage">' + weightage + '</td>');
        $(tbody).append(row);
   	  });
      $(tfoot).append('<tr><th>&nbsp</th><th>' + totalWeightage + '</th></tr>');
    }

    function updateModelByFormStatus() {
      assignment.render={ buttons: [] };
	  var role=options.role;

      if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING) {
       	if (role == 'Employee') {
       	  var phaseAssessHeader=getBlankPhaseAssessHeader();
       	  phaseAssessHeaders[phaseAssessHeaders.length]=phaseAssessHeader;
       	  phaseAssessHeader.render=getSelfRatingRenderer(true);
   	      assignment.render.buttons.push('Save');
   	      assignment.render.buttons.push('Submit');
        }
      } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
        if (role == 'Employee') {
          var phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
    	  phaseAssessHeader.render=getSelfRatingRenderer(true);
   	      assignment.render.buttons.push('Save');
   	      assignment.render.buttons.push('Submit');
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
        if (role == 'Employee') {
          var phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
    	  phaseAssessHeader.render=getSelfRatingRenderer(false);
        } else if (role == 'Manager') {
          var phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
    	  phaseAssessHeader.render=getSelfRatingRenderer(false);

          phaseAssessHeader=getBlankPhaseAssessHeader();
       	  phaseAssessHeaders[phaseAssessHeaders.length]=phaseAssessHeader;
       	  phaseAssessHeader.render=getManagerRatingRenderer(true);

     	  assignment.render.buttons.push('Save Review');
     	  assignment.render.buttons.push('Conclude');
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
        if (role == 'Employee') {
          var phaseAssessHeader=phaseAssessHeaders[0];
          phaseAssessHeader.render=getSelfRatingRenderer(false);

          var phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
          phaseAssessHeader.render=getManagerRatingRenderer(false);
        } else if (role == 'Manager') {
          var phaseAssessHeader=phaseAssessHeaders[0];
      	  phaseAssessHeader.render=getSelfRatingRenderer(false);

          phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
      	  phaseAssessHeader.render=getManagerRatingRenderer(true);

      	  assignment.render.buttons.push('Save Review');
       	  assignment.render.buttons.push('Conclude');
        }
      } else if (status == PhaseAssignmentStatus.CONCLUDED) {
        var phaseAssessHeader=phaseAssessHeaders[0];
        phaseAssessHeader.render=getSelfRatingRenderer(false);

        phaseAssessHeader=phaseAssessHeaders[phaseAssessHeaders.length-1];
        phaseAssessHeader.render=getManagerRatingRenderer(false);
      }
    }

    function renderAllAssessments() {
      var table=$(card).find('.body').find('table');
      var thead=$(table).find('thead');
      var tbody=$(table).find('tbody');
      var tfoot=$(table).find('tfoot');
    	
      $(phaseAssessHeaders).each(function(index, phaseAssessHeader) {
       	var render=phaseAssessHeader.render;
       	var renderRating=render.rating;
       	var renderComments=render.comments;
       	var renderScore=render.score;
       	
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderRating.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderComments.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderScore.name + '</th>');

        $(tbody).find('tr').each(function (index, row) {
          var phaseAssessDetail=phaseAssessHeader.phaseAssessDetails[index];
          var ratingParams={
            data: phaseAssessDetail, property: 'rating', sProperty: 'score', enable: render.editable,
            rClass: renderRating.className, wClass: 'weightage', sClass: renderScore.className
          };
          var commentsParams= {
            data: phaseAssessDetail, property: 'comments', enable: render.editable, cClass: renderComments.className
          };
          var scoreParams={
        	data: phaseAssessDetail, property: 'score', enable: render.editable, sClass: renderScore.className
          };
          $(row).find('td:last').after(getRatingCell(ratingParams));
          $(row).find('td:last').after(getCommentsCell(commentsParams));
          $(row).find('td:last').after(getScoreCell(scoreParams));
        });
        $(tfoot).find('th:last').after('<th class="total-' + renderRating.className + '">&nbsp;</th>');
        $(tfoot).find('th:last').after('<th>&nbsp;</th>');
        $(tfoot).find('th:last').after('<th class="total-' + renderScore.className + '">0</th>');

        setColumnSum(renderScore.className);
      });
    }

    function renderActionButtons() {
      var render=assignment.render;
      var buttonsDiv=$('<div class="action-buttons pull-right">');
      $(render.buttons).each(function(index, buttonName) {
    	var button=$('<button type="button" class="btn btn-primary waves-effect">' + buttonName + '</button>');
        $(buttonsDiv).append(button);
        $(button).click(function() {
          sendAssessmentForm(buttonName);
   	    });
      });
      $(card).find('.body').find('table').after(buttonsDiv);
    }

    function getRatingCell(ratingParams) {
      var enable=ratingParams.enable;
      var rClass=ratingParams.rClass;
      var wClass=ratingParams.wClass;
      var sClass=ratingParams.sClass;

      var data=ratingParams.data;
      var property=ratingParams.property;
      var sProperty=ratingParams.sProperty;
      var value=data[property];
      var sValue=data[sProperty];

      var ratingTd=$('<td class="' + rClass + '">');
      var rating=$('<div>');
      var wClassSelector='.' + wClass;
      var sClassSelector='.' + sClass;

      rateYoOptions.readOnly=!enable;
      if (value >=0) {
        rateYoOptions.rating=value;
      }
      rateYoOptions.onSet=function (rating, rateYoInstance) {
        var currentWeightage=$(ratingTd).closest('tr').find(wClassSelector).text();
        var weightedRating=(rating==0)? 0: ((rating*currentWeightage)/100);
        $(ratingTd).closest('tr').find(sClassSelector).text(weightedRating.toFixed(2));
        setColumnSum(sClass);
        
        // Update modal
        data[property]=rating;
        data[sProperty]=weightedRating.toFixed(2);
      }
      $(rating).rateYo(rateYoOptions);
      $(ratingTd).append(rating);
      return ratingTd;
    }

    function getCommentsCell(commentsParams) {
      var enable=commentsParams.enable;
      var cClass=commentsParams.cClass;

      var data=commentsParams.data;
      var property=commentsParams.property;
      var value=data[property];

   	  var commentsTd=$('<td>');
   	  if (enable) {
        var comments=$('<textarea class="' + cClass + '" rows="6" cols="30" maxlength="500">' + (value==null?"":value) + '</textarea>');
        $(comments).bind('input propertychange', function() {
          // Update modal
          data[property]=this.value;
        });
        $(comments).append(comments);
        $(commentsTd).append(comments);
   	  } else {
   		$(commentsTd).append(value);
   	  }
      return commentsTd;
    }

    function getScoreCell(scoreParams) {
      //var enable=scoreParams.enable;
      var sClass=scoreParams.sClass;

      var data=scoreParams.data;
      var property=scoreParams.property;
      var value=data[property];

   	  var scoreTd=$('<td class="' + sClass + '">');
   	  $(scoreTd).text(value.toFixed(2));
      return scoreTd;
    }

    function setColumnSum(className) {
   	  var tbody=$(card).find('.body table tbody');
      var tfoot=$(card).find('.body table tfoot');
      var totalScore=0;
      $('.' + className).each(function(index, td) {
       	totalScore+=parseFloat($(td).text());	
      });
      $(tfoot).find('.total-' + className).text(''+totalScore.toFixed(2));
    }

    function getAssessmentAction(status) {
	  var td=$('<td>');
   	  if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING) {
   		var addButton=$('<button class="btn btn-xs btn-info waves-effect" title="Add Rating"><i class="material-icons">star</i></button>')
   		$(td).append(addButton);
      } else {
    	$(td).append('-');
      }
   	  return td;
    }

    function getBlankPhaseAssessHeader() {
      var phaseAssessHeader={};
      phaseAssessHeader.id=0;
      phaseAssessHeader.assignId=assignment.assignmentId;
      var phaseAssessDetails=[];
      phaseAssessHeader.phaseAssessDetails=phaseAssessDetails;
      $(templateHeaders).each(function(index, templateHeader) {
        var phaseAssessDetail={};
        phaseAssessDetail.id=0;
        phaseAssessDetail.templateHeaderId=templateHeader.id;
        phaseAssessDetail.rating=0;
        phaseAssessDetail.comments='';
        phaseAssessDetail.score=0;
        phaseAssessDetails[phaseAssessDetails.length]=phaseAssessDetail;
      });
      //phaseAssessHeaders[phaseAssessHeaders.length]=phaseAssessHeader;
      return phaseAssessHeader;
    }

    function getSelfRatingRenderer(editable) {
      return {
    	editable: editable,
    	rating: {name: 'Self Rating', className:'self-rating'},
     	comments: {name: 'Comments', className: 'self-comments'},
     	score: {name: 'Score', className: 'self-score'},
     };
    }

    function getManagerRatingRenderer(editable) {
      return {
        editable: editable,
        rating: {name: 'Manager Rating', className:'manager-rating'},
       	comments: {name: 'Manager Comments', className: 'manager-comments'},
       	score: {name: 'Manager Score', className: 'manager-score'},
      };
    }

    function sendAssessmentForm(buttonName) {
      var url=null;
      var currentForm=phaseAssessHeaders[phaseAssessHeaders.length-1];

      if(buttonName=='Save') {
   		url=options.contextPath + '/assessment/phase/save';
   		$.fn.postJSON({ url: url, data: currentForm });
      } else if(buttonName=='Submit') {
    	url=options.contextPath + '/assessment/phase/submit';
    	swal({
          title: "Are you sure?", text: "Do you want want to submit your appraisal form to your Manager? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit it!", closeOnConfirm: false
        }, function () {
          $.fn.postJSON({ url: url, data: currentForm });
        });
   	  } else if(buttonName=='Save Review') {
   		//reivewAppraisalForm();
   		url=options.contextPath + '/assessment/phase/review';
   		$.fn.postJSON({ url: url, data: currentForm });
      } else if(buttonName=='Conclude') {
    	swal({
          title: "Are you sure?", text: "Do you want to conclude this assignment? This cannot be undone!!!", type: "warning",
            showCancelButton: true, confirmButtonColor: "#DD6B55",
        	confirmButtonText: "Yes, Enable it!", closeOnConfirm: false
        }, function () {
       	  url=options.contextPath + '/assessment/phase/conclude';
          $.fn.postJSON({ url: url, data: currentForm });
        });
      }
      //delete currentForm.render;
      //delete currentForm.status;
    }

    function onErrorAssessments(error) {
      console.log('error=' + JSON.stringify(error));
    }
  };

});
