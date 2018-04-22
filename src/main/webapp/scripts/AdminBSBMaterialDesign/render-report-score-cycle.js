$(function () {
  var rateYoOptions={ halfStar: true, starWidth: "18px", normalFill: '#CCC', ratedFill: '#4CAF50'};

  $.fn.scoreReport=function( options ) {
    var settings=$.extend({
      contextPath: null,
      url: null,
    }, options );

    var obj=$(this);
    $.fn.ajaxGet({
      url: settings.url,
      onSuccess: renderAssignments,
      onError: onErrorScoreReport
    });

    function renderAssignments(data) {
      if (data == null || data.length == 0) {
        showErrorCard('There are no scores found');
      } else{
        renderScoreReport(data);
      }
    }

    function renderScoreReport(data) {
      for (var index = 0; index < data.length; index++) {
        var cycleAssignment = data[index];
        var cycle=cycleAssignment.cycle;
        var cycleStatus=getAppraisalCycleStatus(cycle.status);
        if (cycleStatus == AppraisalCycleStatus.DRAFT) {
          continue;
        }
        var cardRow=$('<div class="row clearfix">');
        var cardColumn=$('<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">');
        var card=$('<div class="card">');
        var cardHeader=$('<div class="header">');
        $(cardHeader).addClass(cycleStatus.colorClass);
        var cardTitle=$('<h2>' + cycle.name + '</h2>');
        var cardBody=$('<div class="body">');

        var employeeAssignments=cycleAssignment.employeeAssignments;

        if (employeeAssignments == null || employeeAssignments.length == 0) {
          $(cardBody).append('<p class="font-bold col-pink">No Employees were submitted to you for this cycle.</p>');
        } else {
          var table=$('<table class="table table-bordered table-striped table-hover dataTable">');
          var thead=$('<thead>');
          var tbody=$('<tbody>');
          var theadRow=$('<tr>');
          $(theadRow).append('<th width="35%">Employee #</th>');
          $(theadRow).append('<th width="50%">Employee Name</th>');
          $(theadRow).append('<th width="15%">Score</th>');

          $(employeeAssignments).each(function(jindex, ea) {
            var assignedTo=ea.assignedTo;
            var assignedBy=ea.assignedBy;
            var phaseStatus=getPhaseAssignmentStatus(ea.status);

            var row=$(tbody).find('tr:has(td:first:contains("' + assignedTo.employeeId + '"))');
            if(row.length == 0) {
              row=$('<tr>');
              $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.employeeId + '</td>');
              $(row).append('<td>' + assignedTo.fullName + '</td>');
            }
            if (phaseStatus == PhaseAssignmentStatus.CONCLUDED) {
              var cycleScoreLink=$('<a href="#" data-toggle="modal" data-target="#EmployeePhaseAssignments_Modal" >');
              $(cycleScoreLink).append(ea.score.toFixed(2));
              $(cycleScoreLink).attr('data-employee-id', assignedTo.employeeId);
              $(cycleScoreLink).attr('data-employee-name', assignedTo.fullName);

              var scoreTd=$('<td>');
              $(scoreTd).append(cycleScoreLink);
              $(row).append(scoreTd);
              $(cycleScoreLink).click(function() {
                showPhaseScores(ea.assignmentId, assignedTo.employeeId);
              });
            } else {
              $(row).append('<td>-</td>');
            }
            $(tbody).append(row);
            $(cardBody).append(table);
            $(table).append(thead);
            $(table).append(tbody);
            $(thead).append(theadRow);
          });
          $(table).DataTable({
            responsive: true,
            paging: false,
            searching: false,
            ordering: true,
            info: true,
          });
        }
        $(obj).append(cardRow);
        $(cardRow).append(cardColumn);
        $(cardColumn).append(card);
        $(card).append(cardHeader);
        $(cardHeader).append(cardTitle);
        $(card).append(cardBody);
      }
    }

    function showPhaseScores(assignmentId, employeeId) {
      $.fn.ajaxGet({
        url: settings.contextPath + '/manager/report/cycle/assessments/' + assignmentId + '/' + employeeId,
        onSuccess: renderAllPhaseAssessments,
        onError: showEmployeePhaseScoreError
      });
    }

    $("#EmployeePhaseAssignments_Modal" ).on('shown.bs.modal', function(e) {
      var $invoker = $(e.relatedTarget);
      $('#EmployeePhaseAssignments_Title').text($invoker.attr('data-employee-name'));
    });

    function renderAllPhaseAssessments(phaseAssessments) {
      if (phaseAssessments == null || phaseAssessments.length == 0) {
        swal({ title: "Failed!", text: 'There are no assessments found for this employee', type: "error"});
      } else {
        var tbody=$('#EmployeePhaseAssignments_Table tbody');
        $(tbody).empty();
        $('.assessment_div').empty();

        $(phaseAssessments).each(function (index, phaseAssessment) {
          var phaseAssignId=phaseAssessment.phase.id;
          var employeeAssignment=phaseAssessment.employeeAssignment;

          var row=$('<tr>');
          var radioCell=$('<td>');
          var radio=$('<input name="SelectedAssignmentId" type="radio" id="radio_' + phaseAssignId + '" value="' + phaseAssignId + '" class="radio-col-red">');
          $(radio).click(function () {
            showAssessment(phaseAssessment);
          });
          $(radioCell).append(radio);
          $(radioCell).append('<label for="radio_' + phaseAssignId + '"></label>');
          $(row).append(radioCell);

          $(row).append('<td>' + phaseAssessment.phase.name + '</td>');
          $(row).append('<td>' + employeeAssignment.assignedBy.fullName + '</td>');
          $(row).append('<td>' + getPhaseStatusLabel(employeeAssignment.status) + '</td>');
          $(row).append('<td><b>' + employeeAssignment.score.toFixed(2) + '</b></td>');
          $(tbody).append(row);
        });
        $('#EmployeePhaseAssignments_Modal').show();
      }
    }

    function showAssessment(phaseAssessment) {
      var employeeAssignment=phaseAssessment.employeeAssignment;
      var templateHeaders=phaseAssessment.templateHeaders;
      var assessHeaders=phaseAssessment.assessHeaders;

      $('.assessment_div').empty();
      var carouselSlide=$('<div id="assessment" class="carousel slide" data-ride="carousel">');
      $('.assessment_div').append(carouselSlide);

      var indicators=$('<ol class="carousel-indicators">');
      var carouselInner=$('<div class="carousel-inner" role="listbox">');
      $(carouselSlide).append(indicators);
      $(carouselSlide).append(carouselInner);

      for (var index=0; index<assessHeaders.length; index++) {
        var assessHeader=assessHeaders[index];
        var item=null;

        if (index==0) {
          $(indicators).append('<li data-target="#assessment" data-slide-to="' + index + '" class="active"></li>');
          item=$('<div class="item active">');
        } else {
          $(indicators).append('<li data-target="#assessment" data-slide-to="' + index + '"></li>');
          item=$('<div class="item">');
        }

        var carousalSpan=$('<span class="carousel-span">');
        var carousalCaption=$('<div class="carousel-caption">');
        var table=$('<table class="table table-striped" style="overflow: auto;">');
        var thead=$('<thead>');
        var tbody=$('<tbody>');
        var tfoot=$('<tfoot>');
        var headRow=$('<tr>');

        $(carousalSpan).append(table);
        $(table).append(thead);
        $(thead).append(headRow);
        $(headRow).append('<th>Goal</th>');
        $(headRow).append('<th>Weightage [%]</th>');
        $(table).append(tbody);
        $(table).append(tfoot);

        var totalWeightage=0;
        $(templateHeaders).each(function(index, templateHeader) {
          var goalId=templateHeader.goalId;
          var goalName=templateHeader.goalName;
          var weightage=templateHeader.weightage;
          totalWeightage+=weightage;

          var modal=$('#GoalParamsModal');
          var row=$('<tr>');
          var goalTd=$('<td item-id="' + goalId + '" style="max-width: 150px;">');
          $(goalTd).append(goalName);
          $(row).append(goalTd);
          $(row).append('<td class="weightage" style="max-width: 60px; word-break: break-all;">' + weightage + '</td>');
          $(tbody).append(row);
        });
        $(tfoot).append('<tr><th>&nbsp</th><th>' + totalWeightage + '</th></tr>');

        // Update assess headers
        if (index==0) {
          assessHeader.render=getSelfRatingRenderer(false);
        } else {
          assessHeader.render=getManagerRatingRenderer(false);
        }

        $(item).append(carousalSpan);
        $(item).append(carousalCaption);
        $(carouselInner).append(item);

        renderAssessment(table, assessHeader);
      }

      var prevControl=$('<a class="left carousel-control" href="#assessment" role="button" data-slide="prev">');
      $(prevControl).append('<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>');
      $(prevControl).append('<span class="sr-only">Previous</span>');

      var nextControl=$('<a class="right carousel-control" href="#assessment" role="button" data-slide="next">');
      $(nextControl).append('<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>');
      $(nextControl).append('<span class="sr-only">Next</span>');

      $(carouselSlide).append(prevControl);
      $(carouselSlide).append(nextControl);
    }

    function renderAssessment(table, assessHeader) {
      var thead=$(table).find('thead');
      var tbody=$(table).find('tbody');
      var tfoot=$(table).find('tfoot');

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
        $(row).find('td:last').after(getRatingCell(table, ratingParams));
        $(row).find('td:last').after(getCommentsCell(commentsParams));
        $(row).find('td:last').after(getScoreCell(scoreParams));
      });
      $(tfoot).find('th:last').after('<th class="total-' + renderRating.className + '">&nbsp;</th>');
      $(tfoot).find('th:last').after('<th>&nbsp;</th>');
      $(tfoot).find('th:last').after('<th class="total-' + renderScore.className + '">0</th>');
      setColumnSum(table, renderScore.className);
    }

    function getRatingCell(table, ratingParams) {
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
        setColumnSum(table, sClass);

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
        var comments=$('<textarea class="' + cClass + '" rows="6" cols="30" maxlength="2000">' + (value==null?"":value) + '</textarea>');
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
      var sClass=scoreParams.sClass;
      var data=scoreParams.data;
      var property=scoreParams.property;
      var value=data[property];
      var scoreTd=$('<td class="' + sClass + '" style="min-width: 80px; word-break: break-all;">');
      $(scoreTd).text(value.toFixed(2));
      return scoreTd;
    }

    function setColumnSum(table, className) {
      var tbody=$(table).find('tbody');
      var tfoot=$(table).find('tfoot');
      var totalScore=0;
      console.log('className=' + className);
      $('.' + className).each(function(index, td) {
        totalScore+=parseFloat($(td).text()); 
      });
      $(tfoot).find('.total-' + className).text(''+totalScore.toFixed(2));
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

    function showEmployeePhaseScoreError(error) {
      swal({ title: "Failed!", text: JSON.stringify(error), type: "error"});
    }

    function onErrorScoreReport(error) {
      showErrorCard('Errors occurred while retreiving report. Cause: ' + JSON.stringify(error));
    }
  }
});
