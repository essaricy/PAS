$(function () {
  var assignmentId='${param.aid}';

  var ROLE_EMPLOYEE='Employee';
  var ROLE_MANAGER='Manager';

  var ACTION_SAVE='Save';
  var ACTION_SUBMIT='Submit';
  var ACTION_SAVE_REVIEW='Save Review';
  var ACTION_SUBMIT_REVIEW='Submit Review';
  var ACTION_AGREE='Agree';
  var ACTION_DISAGREE='Disagree';
  var ACTION_UPDATE_REVIEW='Update Review';
  var ACTION_CONCLUDE='Conclude';

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
      renderTemplateInfo();

      updateModelByFormStatus();
      renderAllAssessments();
      renderActionButtons();
    }

    function renderTitles() {
      var employeeName= assignment.assignedTo.fullName;

      $(cardHeader).append('<h2>' + phase.name + ' assessment for ' + employeeName + '</h2>'
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
      var table=$('<table class="table table-striped" style="overflow: auto;">');
      var thead=$('<thead>');
      var tbody=$('<tbody>');
      var tfoot=$('<tfoot>');
      var headRow=$('<tr>');

      $(cardBody).append(divRow);
      $(divRow).append(table);
      $(table).append(thead);
      $(thead).append(headRow);
      $(headRow).append('<th>Goal</th>');
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
          if (detail != null && detail.apply=='Y') {
            $(ol).append('<li>' + detail.paramName + "</li>");
          }
        });
      });

      var linkTd=$('<td item-id="' + goalId + '" style="max-width: 150px;">');
      $(linkTd).append(link);
      $(row).append(linkTd);
      $(row).append('<td class="weightage" style="max-width: 60px; word-break: break-all;">' + weightage + '</td>');
        $(tbody).append(row);
      });
      $(tfoot).append('<tr><th>&nbsp</th><th>' + totalWeightage + '</th></tr>');
    }

    function updateModelByFormStatus() {
      assignment.render={ buttons: [] };
    var role=options.role;

      if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING) {
        if (role == ROLE_EMPLOYEE) {
          var assessHeader=getBlankAssessHeader();
          assessHeaders[assessHeaders.length]=assessHeader;
          assessHeader.render=getSelfRatingRenderer(true);
          assignment.render.buttons.push(ACTION_SAVE);
          assignment.render.buttons.push(ACTION_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED) {
        if (role == ROLE_EMPLOYEE) {
          var assessHeader=assessHeaders[0];
        assessHeader.render=getSelfRatingRenderer(true);
          assignment.render.buttons.push(ACTION_SAVE);
          assignment.render.buttons.push(ACTION_SUBMIT);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
      var assessHeader=assessHeaders[0];
      assessHeader.render=getSelfRatingRenderer(false);
        if (role == ROLE_MANAGER) {
          assessHeader=getBlankAssessHeader();
          assessHeaders[assessHeaders.length]=assessHeader;
          assessHeader.render=getManagerRatingRenderer(true);

        assignment.render.buttons.push(ACTION_SAVE_REVIEW);
        assignment.render.buttons.push(ACTION_SUBMIT_REVIEW);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
        var assessHeader=assessHeaders[0];
        assessHeader.render=getSelfRatingRenderer(false);
        if (role == ROLE_MANAGER) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.render=getManagerRatingRenderer(true);

          assignment.render.buttons.push(ACTION_SAVE_REVIEW);
          assignment.render.buttons.push(ACTION_SUBMIT_REVIEW);
        }
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED) {
      var assessHeader=assessHeaders[0];
      assessHeader.render=getSelfRatingRenderer(false);

      var assessHeader=assessHeaders[assessHeaders.length-1];
      assessHeader.render=getManagerRatingRenderer(false);
        
      if (role == ROLE_EMPLOYEE) {
          assignment.render.buttons.push(ACTION_AGREE);
          assignment.render.buttons.push(ACTION_DISAGREE);
        } else if (role == ROLE_MANAGER) {
        }
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_AGREED) {
        var assessHeader=assessHeaders[0];
        assessHeader.render=getSelfRatingRenderer(false);

        assessHeader=assessHeaders[assessHeaders.length-1];
        assessHeader.render=getManagerRatingRenderer(false);
        if (role == ROLE_MANAGER) {
          assignment.render.buttons.push(ACTION_CONCLUDE);
        }
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED) {
        var assessHeader=assessHeaders[0];
        assessHeader.render=getSelfRatingRenderer(false);

        if (role == ROLE_EMPLOYEE) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.render=getManagerRatingRenderer(false);
          assignment.render.buttons.push(ACTION_AGREE);
        } else if (role == ROLE_MANAGER) {
          assessHeader=assessHeaders[assessHeaders.length-1];
          assessHeader.render=getManagerRatingRenderer(true);

          assignment.render.buttons.push(ACTION_UPDATE_REVIEW);
        }
      } else if (status == PhaseAssignmentStatus.CONCLUDED) {
        var assessHeader=assessHeaders[0];
        assessHeader.render=getSelfRatingRenderer(false);

        assessHeader=assessHeaders[assessHeaders.length-1];
        assessHeader.render=getManagerRatingRenderer(false);
      }
    }

    function renderAllAssessments() {
      var table=$(card).find('.body').find('table');
      var thead=$(table).find('thead');
      var tbody=$(table).find('tbody');
      var tfoot=$(table).find('tfoot');
      
      $(assessHeaders).each(function(index, assessHeader) {
        var render=assessHeader.render;
        var renderRating=render.rating;
        var renderComments=render.comments;
        var renderScore=render.score;
        
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderRating.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderComments.name + '</th>');
        $(thead).find('th:last').after('<th class="' + status.colorClass + '">' + renderScore.name + '</th>');

        $(tbody).find('tr').each(function (index, row) {
          var assessDetail=assessHeader.assessDetails[index];
          var ratingParams={
            data: assessDetail, property: 'rating', sProperty: 'score', enable: render.editable,
            rClass: renderRating.className, wClass: 'weightage', sClass: renderScore.className
          };
          var commentsParams= {
            data: assessDetail, property: 'comments', enable: render.editable, cClass: renderComments.className
          };
          var scoreParams={
          data: assessDetail, property: 'score', enable: render.editable, sClass: renderScore.className
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

      var ratingTd=$('<td class="' + rClass + '" style="min-width: 110px; word-break: break-all;">');
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

      var commentsTd=$('<td style="min-width: 100px; max-width: 350px;">');
      if (enable) {
        var comments=$('<textarea class="' + cClass + '" rows="6" cols="30">' + (value==null?"":value) + '</textarea>');
        $(comments).bind('input propertychange', function() {
          // Update modal
          data[property]=this.value;
        });
        $(comments).append(comments);
        $(commentsTd).append(comments);
      } else {
      $(commentsTd).text(value);
      }
      return commentsTd;
    }

    function getScoreCell(scoreParams) {
      //var enable=scoreParams.enable;
      var sClass=scoreParams.sClass;

      var data=scoreParams.data;
      var property=scoreParams.property;
      var value=data[property];

      var scoreTd=$('<td class="' + sClass + '" style="min-width: 80px; word-break: break-all;">');
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
      var currentForm=assessHeaders[assessHeaders.length-1];

      if(buttonName==ACTION_SAVE) {
        url=options.contextPath + '/assessment/phase/save';
        $.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonName==ACTION_SUBMIT) {
        url=options.contextPath + '/assessment/phase/submit';
        swal({
          title: "Are you sure?", text: "Do you want to submit your appraisal form to your Manager? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonName==ACTION_SAVE_REVIEW) {
        url=options.contextPath + '/assessment/phase/save-review';
        $.fn.ajaxPost({ url: url, data: currentForm });
      } else if(buttonName==ACTION_SUBMIT_REVIEW) {
        url=options.contextPath + '/assessment/phase/submit-review';
        swal({
          title: "Are you sure?", text: "Do you want to submit your review? Please make sure that you have completed everything. Once submitted, this cannot be undone.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Submit!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonName==ACTION_AGREE) {
        swal({
          title: "Are you sure?", text: "Do you agree with your manager review? This will conclude the assignment and cannot be reverted!!!", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Agree!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/agree';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonName==ACTION_DISAGREE) {
        swal({
          title: "Are you sure?", text: "Please follow the escalation procedures sent by the HR. You may come back here and AGREE once the escalation has been resolved.", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Escalate!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
        url=options.contextPath + '/assessment/phase/disagree';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if(buttonName==ACTION_UPDATE_REVIEW) {
        swal({
          title: "Are you sure?", text: "Do you want to update the review?", type: "warning",
          showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Update!", closeOnConfirm: false, showLoaderOnConfirm: true
        }, function () {
          url=options.contextPath + '/assessment/phase/update-review';
          $.fn.ajaxPost({ url: url, data: currentForm });
        });
      } else if (buttonName==ACTION_CONCLUDE) {
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
