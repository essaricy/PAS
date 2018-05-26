$(function () {
  var ROLE_EMPLOYEE='Employee';
  var ROLE_MANAGER='Manager';

  var AssessmentAction = {
	  EMPLOYEE_SAVE:		{ label: 'Save', 		icon: 'done'},
	  EMPLOYEE_SUBMIT:		{ label: 'Submit', 		icon: 'done_all'},
	  EMPLOYEE_AGREE:		{ label: 'Agree', 		icon: 'mood'},
	  EMPLOYEE_DISAGREE:	{ label: 'Disagree', 	icon: 'mood_bad'},

	  MANAGER_REVERT:		{ label: 'Revert', 		icon: 'undo'},
	  MANAGER_SAVE: 		{ label: 'Save', 		icon: 'done'},
	  MANAGER_SUBMIT:		{ label: 'Submit', 		icon: 'done_all'},
	  MANAGER_UPDATE:		{ label: 'Update', 		icon: 'update'},
	  MANAGER_CONCLUDE:		{ label: 'Conclude', 	icon: 'lock'},
  };

  var RATING_TYPES=[
  	{rName: 'Employee Rating: ', rClass: 'employee-rating', sName: 'Employee Score: ', sClass: 'employee-score', cName: 'Employee Comments: ', cClass: 'employee-comments'},
  	{rName: 'Manager Rating: ', rClass: 'manager-rating', sName: 'Manager Score: ', sClass: 'manager-score', cName: 'Manager Comments: ', cClass: 'manager-comments'}
  ];

  $.fn.renderAssessment=function( options ) {
    var settings=$.extend({
      contextPath: null,
      url: null,
      role: ROLE_EMPLOYEE,
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
    var assessHeaders=null;

    function parseResult(result) {
      updateData(result);
      renderAssessments();
    }

    function updateData(result) {
      assignment=result.employeeAssignment;
      phase=result.phase;
      templateHeaders=result.templateHeaders;
      status=getPhaseAssignmentStatus(assignment.status);
      assessHeaders=result.assessHeaders;
    }

    function renderAssessments() {
      renderTitles();
      renderGoalPopup();
      renderAssessmentHeaders();
      updateModelByFormStatus();
      renderAssessmentDetails();
      renderOverallScore();
      renderActionButtons();
    }

    function renderTitles() {
      var employeeName= assignment.assignedTo.fullName;
      $(cardHeader).append('<h2>' + phase.name + ' Assessment: ' + employeeName + '</h2>'
        + 'Phase starts on <code>' + phase.startDate + '</code> and ends on <code>' + phase.endDate + '</code><br/>'
        + 'This form status is ' + getPhaseStatusLabel(status.code) + '<br/>'
      );
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

    function renderAssessmentHeaders() {
	  var phaseStatus=getPhaseAssignmentStatus(status.code);
	  var panelColor=phaseStatus.colorClass.replace('bg', 'panel-col');
	  var iconColor=phaseStatus.colorClass.replace('bg', 'col');
	  var modal=$('#GoalParamsModal');
      var rowDiv=$('<div class="row clearfix">');
      var colDiv=$('<div class="col-xs-12 ol-sm-12 col-md-12 col-lg-12">');
      var panelGroupDiv=$('<div class="panel-group" id="assessment_accordion" role="tablist" aria-multiselectable="true">');

      $(cardBody).append(rowDiv);
      $(rowDiv).append(colDiv);
	  $(colDiv).append(panelGroupDiv);

	  $(templateHeaders).each(function(index, templateHeader) {
		var goalId=templateHeader.goalId;
	    var goalName=templateHeader.goalName;
	    var weightage=templateHeader.weightage;
		    
	    var headingId="Heading_" + goalId;
	    var collapseId="Collapse_" + goalId;

	    var panelDiv=$('<div class="panel ' + panelColor + '">');
        var panelHeading=$('<div class="panel-heading" role="tab" id="' + headingId + '">');
        var panelTitleHeading=$('<h4 class="panel-title">');
        var panelTitleLink=$('<a role="button" style="display: inline-block;" data-toggle="collapse" href="#' + collapseId + '" aria-expanded="true" aria-controls="' + collapseId + '">');
        var spanWeightage=$('<span class="p-t-5 p-r-5" style="float: right;">');
        var spanWeightageValue=$('<span class="weightage">');
        var seeGoalsLink=$('<small style="cursor: pointer; font-size: 50%;" data-toggle="modal" data-target="#' + $(modal).attr('id') + '">');
        $(seeGoalsLink).click(function() {
          $(modal).find('.modal-title').text(goalName);
          $(modal).find('.modal-body').empty();
          var ol=$('<ol>');
          $(modal).find('.modal-body').append(ol);
          $(templateHeader.details).each(function (index, detail) {
            if (detail != null && detail.apply=='Y') {
              $(ol).append('<li>' + detail.paramName + "</li>");
            }
          });
        });

	    $(panelGroupDiv).append(panelDiv);
	    $(panelDiv).append(panelHeading);
        $(panelHeading).append(panelTitleHeading);
	    $(panelTitleHeading).append(panelTitleLink);
	    $(panelTitleHeading).append(seeGoalsLink);
	    $(seeGoalsLink).append('See Goals');
        $(panelTitleHeading).append(spanWeightage);
        $(spanWeightage).append(spanWeightageValue);
        $(spanWeightageValue).append(weightage);
        $(spanWeightage).append('%');
        $(panelTitleLink).append(getGoalIcon(goalName, iconColor));
        $(panelTitleLink).append(goalName);

        var panelCollapse=$('<div id="' + collapseId + '" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="' + headingId + '">');
        var panelBody=$('<div class="panel-body">');
        $(panelCollapse).append(panelBody);
        $(panelDiv).append(panelCollapse);
	  });
    }

    function updateModelByFormStatus() {
      assignment.render={ buttons: [] };
      var role=options.role;

      if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING) {
        if (role == ROLE_EMPLOYEE) {
          var assessHeader=getBlankAssessHeader();
          assessHeader.editable=true;
          assessHeaders[0]=assessHeader;
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SAVE);
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
        if (role == ROLE_EMPLOYEE) {
          var assessHeader=assessHeaders[0];
          assessHeader.editable=true;
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SAVE);
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED) {
        if (role == ROLE_EMPLOYEE) {
          var assessHeader=assessHeaders[0];
          assessHeader.editable=true;
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SAVE);
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        if (role == ROLE_MANAGER) {
          assessHeader=getBlankAssessHeader();
          assessHeaders[assessHeaders.length]=assessHeader;
          assessHeader.editable=true;
          assignment.render.buttons.push(AssessmentAction.MANAGER_REVERT);
          assignment.render.buttons.push(AssessmentAction.MANAGER_SAVE);
          assignment.render.buttons.push(AssessmentAction.MANAGER_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        if (role == ROLE_MANAGER) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.editable=true;
          assignment.render.buttons.push(AssessmentAction.MANAGER_SAVE);
          assignment.render.buttons.push(AssessmentAction.MANAGER_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        var assessHeader=assessHeaders[assessHeaders.length-1];
        assessHeader.editable=false;
        if (role == ROLE_EMPLOYEE) {
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_AGREE);
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_DISAGREE);
        } else if (role == ROLE_MANAGER) {
        }
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_AGREED) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        assessHeader=assessHeaders[assessHeaders.length-1];
        assessHeader.editable=false;
        if (role == ROLE_MANAGER) {
          assignment.render.buttons.push(AssessmentAction.MANAGER_CONCLUDE);
        }
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        if (role == ROLE_EMPLOYEE) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.editable=false;
          assignment.render.buttons.push(AssessmentAction.EMPLOYEE_AGREE);
        } else if (role == ROLE_MANAGER) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.editable=true;
          assignment.render.buttons.push(AssessmentAction.MANAGER_UPDATE);
        }
      } else if (status == PhaseAssignmentStatus.CONCLUDED) {
        var assessHeader=assessHeaders[0];
        assessHeader.editable=false;
        assessHeader=assessHeaders[assessHeaders.length-1];
        assessHeader.editable=false;
      }
    }

    function renderAssessmentDetails() {
      $(assessHeaders).each(function(index, assessHeader) {
    	var editable=assessHeader.editable;
        var ratingType=RATING_TYPES[index];
        var panelBodyDivs=$('.panel-body');

        if (panelBodyDivs.length != assessHeader.assessDetails.length) {
       	  throw "There is a mismatch in the number of goals vs number of assess details. Cannot render";
        }
        $(panelBodyDivs).each(function (index, panelBodyDiv) {
      	  var assessDetail=assessHeader.assessDetails[index];

     	  var assessDetailTable=$('<table width="100%" class="table table-striped">');
      	  var ratingScoreRow=$('<tr>');
      	  $(assessDetailTable).append(ratingScoreRow);

      	  $(ratingScoreRow).append('<td width="17%" class="assess-heading">' + ratingType.rName + '</td>');
      	  var ratingCell=$('<td width="65%">');
      	  $(ratingScoreRow).append(ratingCell);
      	  $(ratingCell).append(getRatingContainer(assessDetail, ratingType, editable));

      	  $(ratingScoreRow).append('<td width="15%" class="assess-heading">' + ratingType.sName + '</td>');
      	  var scoreCell=$('<td width="3%">');
      	  $(ratingScoreRow).append(scoreCell);
      	  $(scoreCell).append(getScoreContainer(ratingType.sClass, assessDetail.score));
      	  $(panelBodyDiv).append(assessDetailTable);

      	  var commentsRow=$('<tr>');
      	  $(commentsRow).append(getCommentsCell(assessDetail, ratingType, editable));
    	  $(assessDetailTable).append(commentsRow);
    	  
    	  var id='CKEDITOR_' + assessDetail.id + '_' + assessDetail.templateHeaderId;
    	  CKEDITOR.replace(id);
        });
      });
    }

    function getGoalIcon(goalName, iconColor) {
   	  var goalIcon=$('<i class="material-icons">')
    	if (goalName.indexOf('Client Orientation') != -1) {
    	  $(goalIcon).append('my_location');
    	} else if (goalName.indexOf('Communication') != -1) {
    	  $(goalIcon).append('record_voice_over');
    	} else if (goalName.indexOf('Project Management') != -1) {
    	  $(goalIcon).append('group');
    	} else if (goalName.indexOf('Leadership') != -1) {
    	  $(goalIcon).append('school');
    	} else if (goalName.indexOf('Business Domain') != -1) {
    	  $(goalIcon).append('business_center');
    	} else if (goalName.indexOf('Process Awareness') != -1) {
    	  $(goalIcon).append('verified_user');
    	} else if (goalName.indexOf('Corporate Initiatives') != -1) {
    	  $(goalIcon).append('business');
    	} else if (goalName.indexOf('Individual Goals') != -1) {
    	  $(goalIcon).append('person_pin');
    	} else if (goalName.indexOf('Technical Solutions') != -1) {
    	  $(goalIcon).append('devices');
    	} else {
    	  $(goalIcon).append('album');
    	}
    	//$(goalIcon).addClass(iconColor);
    	return goalIcon;
    }

    function renderOverallScore() {
      var overallEmployeeScoreRow=$('<div class="row clearfix align-right m-r-10">');
      var table=$('<table class="table table-striped">');
      var empRow=$('<tr>');
      $(empRow).append('<td width="90%">Overall Employee Score:</td>');
      $(empRow).append('<td class="overall-employee-score font-bold font-20">0.00</td>');
      $(table).append(empRow);

      var addManagerScore=false;
      var role=options.role;
      if (role == ROLE_EMPLOYEE) {
        if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED
        	|| status == PhaseAssignmentStatus.EMPLOYEE_AGREED
    		|| status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED
    		|| status == PhaseAssignmentStatus.CONCLUDED) {
        	addManagerScore=true;
        }
      } else if (role == ROLE_MANAGER) {
    	if (status != PhaseAssignmentStatus.NOT_INITIATED
        	|| status != PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
    		|| status != PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
        	addManagerScore=true;
        }	
      }
	  if (addManagerScore) {
	  	var mgrRow=$('<tr>');
	   	$(mgrRow).append('<td width="90%">Overall Manager Score:</td>');
	   	$(mgrRow).append('<td class="overall-manager-score font-bold font-20">0.00</td>');
	   	$(table).append(mgrRow);
	  }
	  $(overallEmployeeScoreRow).append(table);
	  $(card).find('.body').append(overallEmployeeScoreRow);

	  $(assessHeaders).each(function(index, assessHeader) {
	    var editable=assessHeader.editable;
	    var ratingType=RATING_TYPES[index];
	    updateOverallScores(ratingType.sClass);
	  });
    }

    function renderActionButtons() {
      var render=assignment.render;
      var buttonsRow=$('<div class="row clearfix">');
      var buttonsDiv=$('<div class="action-buttons pull-right">');
      $(render.buttons).each(function(index, buttonAction) {
    	var buttonName=buttonAction.label;
    	var buttonIcon=buttonAction.icon;

    	var button=$('<button type="button" class="btn btn-primary waves-effect">');
    	$(button).append('<i class="material-icons">' + buttonIcon + '</i>');
    	$(button).append('<span>' + buttonName + '</span>');
        $(buttonsDiv).append(button);
        $(button).click(function() {
          sendAssessmentForm(buttonAction);
        });
      });
      $(buttonsRow).append(buttonsDiv);
      $(card).find('.body').append(buttonsRow);
    }

    function getRatingContainer(assessDetail, ratingType, enable) {
   	  var rClass=ratingType.rClass;
      var sClass=ratingType.sClass;
	  var value=assessDetail.rating;

   	  var ratingContainer=$('<span class="' + rClass + '">');
	  rateYoOptions.readOnly=!enable;
	  if (value >=0) {
	    rateYoOptions.rating=value;
	  }
	  rateYoOptions.onSet=function (rating, rateYoInstance) {
	    var currentWeightage=$(ratingContainer).closest('.panel').find('.panel-heading').find('.weightage').text();
	    var weightedRating=(rating==0)? 0: ((rating*currentWeightage)/100);
	    $(ratingContainer).closest('tr').find('.' + sClass).text(weightedRating.toFixed(2));
	    updateOverallScores(sClass);

	    // Update modal
	    assessDetail.rating=rating;
	    assessDetail.score=weightedRating.toFixed(2);
	  }
	  $(ratingContainer).rateYo(rateYoOptions);
	  return ratingContainer;
	}

    function getScoreContainer(className, value) {
      var score=$('<span class="' + className + '">');
      $(score).text(value.toFixed(2));
      return score;
    }

    function getCommentsCell(assessDetail, ratingType, enable) {
      var cName=ratingType.cName;
	  var cClass=ratingType.cClass;
	  var value=assessDetail.comments;
	  var id='CKEDITOR_' + assessDetail.id + '_' + assessDetail.templateHeaderId;

	  var commentsTd=$('<td colspan="4">');
	  $(commentsTd).append('<label class="assess-heading">' + cName + '</label>');
	  $(commentsTd).append('<br/>');

	  /*console.log('<textarea id="' + id + '" ' + (enable? "": "readonly") + ' class="' + cClass
			  + '" style="width:100%;">' + (value==null?"":value) + '</textarea>');*/

	  var comments=$('<textarea id="' + id + '" ' + (enable? "": "readonly") + ' class="' + cClass
			  + '" style="width:100%;">' + (value==null?"":value) + '</textarea>');
      /*$(comments).bind('input propertychange', function() {
        // Update modal
        assessDetail.comments=this.value;
        console.log('CKEDITOR DATA=' + CKEDITOR.instances[id].getData());
      });*/
      $(commentsTd).append(comments);
      // CKEDITOR.replace('CKEDITOR_2532');
      /*if (enable) {
        var comments=$('<textarea class="' + cClass + '" rows="5" maxlength="2000" style="width:100%;">' + (value==null?"":value) + '</textarea>');
        $(comments).bind('input propertychange', function() {
          // Update modal
          assessDetail.comments=this.value;
        });
        $(commentsTd).append(comments);
      } else {
        var valueSpan=$('<span>');
        $(valueSpan).text(value);
        $(commentsTd).append(valueSpan);
      }*/
      return commentsTd;
    }

    function updateOverallScores(sClass) {
      var overallScore=0;
      $('.' + sClass).each(function(index, td) {
      	overallScore+=parseFloat($(td).text()); 
      });
      $('.overall-' + sClass).text(overallScore.toFixed(2));
    }

    function getBlankAssessHeader() {
      var assessHeader={};
      assessHeader.id=0;
      assessHeader.assignId=assignment.assignmentId;
      var assessDetails=[];
      assessHeader.assessDetails=assessDetails;
      $(templateHeaders).each(function(index, templateHeader) {
        var assessDetail={};
        assessDetail.id=0;
        assessDetail.templateHeaderId=templateHeader.id;
        assessDetail.rating=0;
        assessDetail.comments='';
        assessDetail.score=0;
        assessDetails[assessDetails.length]=assessDetail;
      });
      //assessHeaders[assessHeaders.length]=assessHeader;
      return assessHeader;
    }

    function sendAssessmentForm(buttonAction) {
      var url=null;
      var currentForm=assessHeaders[assessHeaders.length-1];
      $(currentForm.assessDetails).each(function (index, assessDetail) {
    	var id='CKEDITOR_' + assessDetail.id + '_' + assessDetail.templateHeaderId;
    	assessDetail.comments=CKEDITOR.instances[id].getData();
      });

      if(buttonAction==AssessmentAction.EMPLOYEE_SAVE) {
        url=options.contextPath + '/assessment/phase/save';
        $.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonAction==AssessmentAction.EMPLOYEE_SUBMIT) {
        url=options.contextPath + '/assessment/phase/submit';
        swal({
          title: "Are you sure?", text: "Do you want to submit your appraisal form to your Manager? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonAction==AssessmentAction.MANAGER_REVERT) {
        url=options.contextPath + '/assessment/phase/revert';
        swal({
          title: "Are you sure?", text: "This allows the employee to modify his/her appraisal form which includes ratings and comments. Do you want to revert this appraisal form?", type: "warning",
            showCancelButton: true, confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, Revert!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonAction==AssessmentAction.MANAGER_SAVE) {
          url=options.contextPath + '/assessment/phase/save-review';
          $.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonAction==AssessmentAction.MANAGER_SUBMIT) {
        url=options.contextPath + '/assessment/phase/submit-review';
        swal({
          title: "Are you sure?", text: "Do you want to submit your review? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonAction==AssessmentAction.EMPLOYEE_AGREE) {
        swal({
          title: "Are you sure?", text: "Do you agree with your manager review? This will conclude the assignment and cannot be reverted!!!", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Agree!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/agree';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonAction==AssessmentAction.EMPLOYEE_DISAGREE) {
        swal({
          title: "Are you sure?", text: "Please follow the escalation procedures sent by the HR. You may come back here and AGREE once the escalation has been resolved.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Escalate!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/disagree';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonAction==AssessmentAction.MANAGER_UPDATE) {
        swal({
          title: "Are you sure?", text: "Do you want to update the review?", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Update!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/update-review';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if (buttonAction==AssessmentAction.MANAGER_CONCLUDE) {
        swal({
          title: "Are you sure?", text: "Please make sure you have set the goals for the next phase for this employee!", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Conclude!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/conclude';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      }
    }

    function onErrorAssessments(error) {
      showErrorCard('Errors occurred while retreiving assessment information. Cause: ' + JSON.stringify(error));
    }
  };

});
