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
	var cycle=null;
    var templateHeaders=null;
	var status=null;
	var cycleAssessHeaders=null;

	function parseResult(result) {
	  updateData(result);
	  renderAssessments();
	}

	function updateData(result) {
      assignment=result.employeeAssignment;
      cycle=result.cycle;
      cycle=result.cycle;
      templateHeaders=result.templateHeaders;
  	  status=getCycleAssignmentStatus(assignment.status);
  	  cycleAssessHeaders=result.cycleAssessmentHeaders;
	}

	function renderAssessments() {
	  if (cycle != null) {
		renderCycleTitles();  
	  } else if (cycle != null) {
		renderCycleTitles();
	  }
      renderGoalPopup();
      renderTemplateInfo();

      updateModelByFormStatus();
      renderAllAssessments();
      renderActionButtons();
    }

	function renderCycleTitles() {
	  $(cardHeader).append('<h2>Cycle: ' + cycle.name + '</h2>'
        + 'Cycle starts on <code>' + cycle.startDate + '</code> '
        + 'and ends on <code>' + cycle.endDate + '</code><br/>'
        + 'Your form status is ' + getCycleStatusLabel(status.code));
    }

    function renderCycleTitles() {
      $(cardHeader).append('<h2>Cycle: ' + cycle.name + '</h2>'
        + 'Cycle starts on <code>' + cycle.startDate + '</code> '
        + 'and ends on <code>' + cycle.endDate + '</code><br/>'
        + 'Your form status is ' + getCycleStatusLabel(status.code));
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

      if (status == CycleAssignmentStatus.SELF_APPRAISAL_PENDING) {
       	if (role == 'Employee') {
       	  var cycleAssessHeader=getBlankCycleAssessHeader();
       	  cycleAssessHeaders[cycleAssessHeaders.length]=cycleAssessHeader;
       	  cycleAssessHeader.render=getSelfRatingRenderer(true);
   	      assignment.render.buttons.push('Save');
   	      assignment.render.buttons.push('Submit');
        }
      } else if (status == CycleAssignmentStatus.SELF_APPRAISAL_SAVED) {
        if (role == 'Employee') {
          var cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
    	  cycleAssessHeader.render=getSelfRatingRenderer(true);
   	      assignment.render.buttons.push('Save');
   	      assignment.render.buttons.push('Submit');
        }
      } else if (status == CycleAssignmentStatus.MANAGER_REVIEW_PENDING) {
        if (role == 'Employee') {
          var cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
    	  cycleAssessHeader.render=getSelfRatingRenderer(false);
        } else if (role == 'Manager') {
          var cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
    	  cycleAssessHeader.render=getSelfRatingRenderer(false);

          cycleAssessHeader=getBlankCycleAssessHeader();
       	  cycleAssessHeaders[cycleAssessHeaders.length]=cycleAssessHeader;
       	  cycleAssessHeader.render=getManagerRatingRenderer(true);

     	  assignment.render.buttons.push('Save Review');
     	  assignment.render.buttons.push('Conclude');
        }
      } else if (status == CycleAssignmentStatus.MANAGER_REVIEW_SAVED) {
        if (role == 'Employee') {
          var cycleAssessHeader=cycleAssessHeaders[0];
          cycleAssessHeader.render=getSelfRatingRenderer(false);

          var cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
          cycleAssessHeader.render=getManagerRatingRenderer(false);
        } else if (role == 'Manager') {
          var cycleAssessHeader=cycleAssessHeaders[0];
      	  cycleAssessHeader.render=getSelfRatingRenderer(false);

          cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
      	  cycleAssessHeader.render=getManagerRatingRenderer(true);

      	  assignment.render.buttons.push('Save Review');
       	  assignment.render.buttons.push('Conclude');
        }
      } else if (status == CycleAssignmentStatus.CONCLUDED) {
        var cycleAssessHeader=cycleAssessHeaders[0];
        cycleAssessHeader.render=getSelfRatingRenderer(false);

        cycleAssessHeader=cycleAssessHeaders[cycleAssessHeaders.length-1];
        cycleAssessHeader.render=getManagerRatingRenderer(false);
      }
    }

    function renderAllAssessments() {
      var table=$(card).find('.body').find('table');
      var thead=$(table).find('thead');
      var tbody=$(table).find('tbody');
      var tfoot=$(table).find('tfoot');
    	
      $(cycleAssessHeaders).each(function(index, cycleAssessHeader) {
       	var render=cycleAssessHeader.render;
       	var renderRating=render.rating;
       	var renderComments=render.comments;
       	var renderScore=render.score;
       	
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderRating.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderComments.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderScore.name + '</th>');

        $(tbody).find('tr').each(function (index, row) {
          var cycleAssessDetail=cycleAssessHeader.cycleAssessDetails[index];
          var ratingParams={
            data: cycleAssessDetail, property: 'rating', sProperty: 'score', enable: render.editable,
            rClass: renderRating.className, wClass: 'weightage', sClass: renderScore.className
          };
          var commentsParams= {
            data: cycleAssessDetail, property: 'comments', enable: render.editable, cClass: renderComments.className
          };
          var scoreParams={
        	data: cycleAssessDetail, property: 'score', enable: render.editable, sClass: renderScore.className
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
   	  if (status == CycleAssignmentStatus.SELF_APPRAISAL_PENDING) {
   		var addButton=$('<button class="btn btn-xs btn-info waves-effect" title="Add Rating"><i class="material-icons">star</i></button>')
   		$(td).append(addButton);
      } else {
    	$(td).append('-');
      }
   	  return td;
    }

    function getBlankCycleAssessHeader() {
      var cycleAssessHeader={};
      cycleAssessHeader.id=0;
      cycleAssessHeader.assignId=assignment.assignmentId;
      var cycleAssessDetails=[];
      cycleAssessHeader.cycleAssessDetails=cycleAssessDetails;
      $(templateHeaders).each(function(index, templateHeader) {
        var cycleAssessDetail={};
        cycleAssessDetail.id=0;
        cycleAssessDetail.templateHeaderId=templateHeader.id;
        cycleAssessDetail.rating=0;
        cycleAssessDetail.comments='';
        cycleAssessDetail.score=0;
        cycleAssessDetails[cycleAssessDetails.length]=cycleAssessDetail;
      });
      //cycleAssessHeaders[cycleAssessHeaders.length]=cycleAssessHeader;
      return cycleAssessHeader;
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
      var currentForm=cycleAssessHeaders[cycleAssessHeaders.length-1];

      if(buttonName=='Save') {
   		url=options.contextPath + '/assessment/cycle/save';
   		$.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonName=='Submit') {
    	url=options.contextPath + '/assessment/cycle/submit';
    	swal({
          title: "Are you sure?", text: "Do you want want to submit your appraisal form to your Manager? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit!", closeOnConfirm: false
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
   	  } else if(buttonName=='Save Review') {
   		//reivewAppraisalForm();
   		url=options.contextPath + '/assessment/cycle/review';
   		$.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonName=='Conclude') {
    	swal({
          title: "Are you sure?", text: "Do you want to conclude this assignment? This cannot be undone!!!", type: "warning",
            showCancelButton: true, confirmButtonColor: "#DD6B55",
        	confirmButtonText: "Yes, Conclude!", closeOnConfirm: false
        }, function () {
       	  url=options.contextPath + '/assessment/cycle/conclude';
          $.fn.ajaxPost({ url: url, data: currentForm });
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
