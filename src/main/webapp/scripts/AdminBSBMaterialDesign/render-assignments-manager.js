$(function () {
  $.fn.managerAssignment=function( options ) {
    var settings=$.extend({
      contextPath: null,
    url: null,
  }, options );

    var obj=$(this);

    $.fn.ajaxGet({
      url: settings.url,
      onSuccess: renderAssignments,
      onError: onErrorAssignedEmployees,
      onComplete: renderComplete
    });

    function renderAssignments(data) {
      if (data == null || data.length == 0) {
      showErrorCard('There are no assignments found');
      } else{
      renderAssignedEmployees(data);
      }
    }

    function renderAssignedEmployees(data) {
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
        var tabsHeadings=$('<ul class="nav nav-tabs tab-col-orange" role="tablist">'
          + '<li role="presentation" class="active"><a href="#PhasesTab_' + cycle.id +'" data-toggle="tab">Phases</a></li>'
          + '<li role="presentation"><a href="#CycleTab_' + cycle.id +'" data-toggle="tab">Cycle</a></li>');
        var tabContent=$('<div class="tab-content">');

        // Render Phases Tab - START
        var phaseTabPanel=$('<div role="tabpanel" class="tab-pane fade in active" id="PhasesTab_' + cycle.id +'">');
        var phaseAssignments=cycleAssignment.phaseAssignments;
        $(phaseAssignments).each(function(jindex, phaseAssignment) {
          var phase=phaseAssignment.phase;
          var phaseTitle=$('<h5 class="font-underline col-cyan">Phase: ' + phase.name + '</h5>');
          var employeeAssignments=phaseAssignment.employeeAssignments;
          if (employeeAssignments.length==0) {
            $(phaseTabPanel).append(phaseTitle);
            $(phaseTabPanel).append('<p class="font-bold col-pink">No Employees were assigned to you for this phase.</p>');
          } else {
            var table=$('<table class="table table-striped" style="table-layout: fixed;">');
            var thead=$('<thead>');
            var theadRow=$('<tr>');
            var tbody=$('<tbody>');
            $(employeeAssignments).each(function(kindex, ea) {
              var assignedTo=ea.assignedTo;
              var assignedBy=ea.assignedBy;
              var row=$('<tr>');
              $(row).append('<td class="hidden-xs" item-id="' + ea.assignmentId + '">' + assignedTo.employeeId + '</td>');
              $(row).append('<td>' + assignedTo.fullName + '</td>');
              $(row).append(getTemplateCell(ea.template));
              $(row).append('<td class="hidden-xs">' + ea.assignedAt + '</td>');
              $(row).append('<td>' + getPhaseStatusLabel(ea.status) + '</td>');
              $(row).append(getPhaseActionCell(ea));
              $(tbody).append(row);
            });
            $(table).append(thead);
            $(thead).append(theadRow);
            $(theadRow).append('<th class="hidden-xs">Employee #</th>');
            $(theadRow).append('<th>Employee Name</th>');
            $(theadRow).append('<th class="hidden-xs">Goal Template</th>');
            $(theadRow).append('<th class="hidden-xs">Assigned On</th>');
            $(theadRow).append('<th>Status</th>');
            $(theadRow).append('<th>Action</th>');
            $(table).append(tbody);
            $(phaseTabPanel).append(phaseTitle);
            $(phaseTabPanel).append(table);
          }
        });
        // Render Phases Tab - END

        // Render CYCLE Tab - START
        var cycleTabPanel=$('<div role="tabpanel" class="tab-pane fade out" id="CycleTab_' + cycle.id +'">');
        var employeeAssignments=cycleAssignment.employeeAssignments;
        if (employeeAssignments.length==0) {
          $(cycleTabPanel).append('<p class="font-bold col-pink">No Employees have been assigned to you for this cycle.</p>');
        } else {
          var table=$('<table class="table table-striped" style="table-layout: fixed;">');
          var thead=$('<thead>');
          var theadRow=$('<tr>');
          $(table).append(thead);
          $(thead).append(theadRow);
          $(theadRow).append('<th class="hidden-xs">Employee #</th>');
          $(theadRow).append('<th>Employee Name</th>');
          $(theadRow).append('<th class="hidden-xs">Assigned On</th>');
          $(theadRow).append('<th>Status</th>');
          $(theadRow).append('<th>Action</th>');
          var tbody=$('<tbody>');
          $(employeeAssignments).each(function(jindex, ea) {
            var assignedTo=ea.assignedTo;
            var assignedBy=ea.assignedBy;
            var row=$('<tr>');
            $(row).append('<td class="hidden-xs" item-id="' + ea.assignmentId + '">' + assignedTo.employeeId + '</td>');
            $(row).append('<td>' + assignedTo.fullName + '</td>');
            $(row).append('<td class="hidden-xs">' + ea.assignedAt + '</td>');
            $(row).append('<td>' + getCycleStatusLabel(ea.status) + '</td>');
            $(row).append(getCycleActionCell(ea));
            $(tbody).append(row);
          });
          $(table).append(tbody);
          $(cycleTabPanel).append(table);
        }
        // Render CYCLE Tab - END

        // Append nodes
        $(obj).append(cardRow);
        $(cardRow).append(cardColumn);
        $(cardColumn).append(card);
        $(card).append(cardHeader);
        $(cardHeader).append(cardTitle);
        $(card).append(cardBody);
        $(cardBody).append(tabsHeadings);
        $(cardBody).append(tabContent);
        $(tabContent).append(cycleTabPanel);
        $(tabContent).append(phaseTabPanel);
      }
    }

    function getCycleActionCell(ea) {
      var status=ea.status;
      var id=ea.assignmentId;
      var td=$('<td>');

      if (status == CycleAssignmentStatus.NOT_INITIATED.code) {
        $(td).append('&nbsp;');
      } else if (status == CycleAssignmentStatus.ABRIDGED.code) {
        $(td).append(getUpRootButton(id));
      } else if (status == CycleAssignmentStatus.CONCLUDED.code) {
        //$(td).append(getViewFormButton(id));
        $(td).append('&nbsp;');
      }
      return td;
    }

    function getPhaseActionCell(ea) {
      var status=ea.status;
      var id=ea.assignmentId;
      var td=$('<td>');

      if (status == PhaseAssignmentStatus.NOT_INITIATED.code) {
        $(td).append(getChangeManagerButton(id));
        $(td).append('&nbsp;');
        $(td).append(getEnableFormButton(id));
        $(td).append('&nbsp;');
        $(td).append(getDeleteAssignmentButton(id));
      } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.code
          || status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.code
          || status == PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED.code) {
    	$(td).append(getChangeManagerButton(id));
        $(td).append('&nbsp;');
        $(td).append(getSendReminderButton(id));
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.code) {
   	    $(td).append(getRevertToSelfSubmissionButton(id));
    	$(td).append('&nbsp;');
    	$(td).append(getChangeManagerButton(id));
        $(td).append('&nbsp;');
        $(td).append(getPerformReviewButton(id));
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.code) {
        $(td).append(getPerformReviewButton(id));
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_AGREED.code) {
        $(td).append(getViewFormButton(id));
        $(td).append('&nbsp;');
        $(td).append(getConcludeButton(id));
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED.code
          || status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED.code
          || status == PhaseAssignmentStatus.CONCLUDED.code) {
        $(td).append(getViewFormButton(id));
      } else {
        $(td).append('-');
      }
      return td;
    }

    function getTemplateCell(template) {
      var td=$('<td class="hidden-xs">');
      var templateLink=$('<a href="#" data-toggle="modal" data-target="#TemplateModal">');
      $(templateLink).append(template.name);
      $(templateLink).click(function (e) {
        e.preventDefault();
        showTemplate(template.id)
      });
      $(td).append(templateLink);
      return td;
    }

    function showTemplate(templateId) {
      $.fn.ajaxGet({
        url: settings.contextPath + '/template/list/' + templateId,
        onSuccess: function(template) {
          $('#TemplateModal_Title').text(template.name);

          var goalsContainer=$('.goals_container');
          $(goalsContainer).empty();
          var listGroup=$('<ul class="list-group">');

          $(template.headers).each(function(index, header) {
            var link=$('<a href="#" class="template-header-link list-group-item">');
            $(link).attr('item-id', header.id);
            $(link).text(header.goalName);
            $(link).click(function(e) {
              e.preventDefault();
              $(this).addClass('active').siblings().removeClass('active');
              // Show items in child container
              var goalParamsContainer=$('.goal_params_container');
              $(goalParamsContainer).empty();
              var ol=$("<ol>");
              $(header.details).each(function (index, detail) {
                if (detail != null && detail.apply=='Y') {
                  $(ol).append('<li item-id="' + detail.id + '">' + detail.paramName + '</li>');
                }
              });
              $(goalParamsContainer).append(ol);
            });
            $(listGroup).append(link);
          });
          $(goalsContainer).append(listGroup);
          $('.template-header-link')[0].click();
        }
      });
    }

    function getUpRootButton(id) {
      var submitToNextLevelManagerButton=$('<button class="btn btn-xs btn-info waves-effect" '
        + 'title="Submit to Next Level Manager" data-toggle="modal" data-target="#EmployeeSearchModal" '
        + 'item-id="' + id + '" item-type="SubmitToNextLevelManager"><i class="material-icons">call_merge</i></button>');
      $(submitToNextLevelManagerButton).tooltip({container: 'body'});
      return submitToNextLevelManagerButton;
    }

    function getChangeManagerButton(id) {
      var assignToManagerButton=$('<button class="btn btn-xs btn-info waves-effect" '
        + 'title="Assign to Another Manager" data-toggle="modal" data-target="#EmployeeSearchModal" '
        + 'item-id="' + id + '" item-type="AssignToAnotherManager"><i class="material-icons">trending_flat</i></button>');
      $(assignToManagerButton).tooltip({container: 'body'});
      return assignToManagerButton;
    }

    function getEnableFormButton(id) {
      var enableFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Enable Employee Self-Submission"><i class="material-icons">assignment_returned</i></button>');
      $(enableFormButton).tooltip({container: 'body'});
      $(enableFormButton).click(function() {
        enableSelfSubmission(id);
      });
      return enableFormButton;
    }

    function getDeleteAssignmentButton(id) {
    	var deleteButton=$('<button class="btn btn-xs btn-info waves-effect" title="Delete Assignment"><i class="material-icons">delete_forever</i></button>');
        $(deleteButton).tooltip({container: 'body'});
        $(deleteButton).click(function() {
          deleteAssignment(id);
        });
        return deleteButton;
    }

    function getSendReminderButton(id) {
      var reminderButton=$('<button class="btn btn-xs btn-info waves-effect" title="Send a reminder"><i class="material-icons">assignment_late</i></button>');
      $(reminderButton).tooltip({container: 'body'});
      $(reminderButton).click(function() {
        sendReminderToSubmit(id);
      });
      return reminderButton;
    }

    function getRevertToSelfSubmissionButton(id) {
        var button=$('<button class="btn btn-xs btn-info waves-effect" title="Revert"><i class="material-icons">undo</i></button>');
        $(button).tooltip({container: 'body'});
        $(button).click(function() {
          revertToSelfSubmission(id);
        });
        return button;
    }

    function getPerformReviewButton(id) {
      var fillReviewButton=$('<button class="btn btn-xs btn-info waves-effect" title="Perform Review"><i class="material-icons">assignment</i></button>');
      $(fillReviewButton).tooltip({container: 'body'});
      $(fillReviewButton).click(function() {
        location.href=settings.contextPath + '/manager/assessment?aid=' + id;
      });
      return fillReviewButton;
    }

    function getViewFormButton(id) {
      var viewFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="View Appraisal Form"><i class="material-icons">assignment_ind</i></button>');
      $(viewFormButton).tooltip({container: 'body'});
      $(viewFormButton).click(function() {
        location.href=settings.contextPath + '/manager/assessment?aid=' + id;
      });
      return viewFormButton;
    }

    function getConcludeButton(id) {
      var concludeButton=$('<button class="btn btn-xs btn-info waves-effect" title="Conclude"><i class="material-icons">assignment_turned_in</i></button>');
      $(concludeButton).tooltip({container: 'body'});
      $(concludeButton).click(function() {
        concludePhase(id);
      });
      return concludeButton;
    }

    function enableSelfSubmission(id) {
      swal({
        title: "Are you sure?",
        text: "Do you want to enable self-submission for this employee for this phase?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55", confirmButtonText: "Yes, Enable!",
        closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
        $.fn.ajaxPut({
          url: settings.contextPath + '/assignment/manager/change/phase-status/enable/' + id
        });
      });
    }

    function sendReminderToSubmit(id) {
      swal({
        title: "Are you sure?", text: "Do you want to send a reminder to submit the self appraisal?", type: "warning",
        showCancelButton: true, confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Send Reminder!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
        $.fn.ajaxPut({
          url: settings.contextPath + '/assignment/manager/reminder/tosubmit/' + id
        });
      });
    }

    function revertToSelfSubmission(id) {
      swal({title: "Are you sure?", text: "This allows the employee to modify his/her appraisal form which includes ratings and comments. Do you want to revert this appraisal form?", type: "warning",
        showCancelButton: true, confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Revert!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
        $.fn.ajaxPut({
          url: settings.contextPath + '/assignment/manager/change/phase-status/revert/' + id
        });
      });
    }

    function deleteAssignment(id) {
      swal({
    	title: "Are you sure?", text: "Do you really want to delete this assignment? This cannot be undone!", type: "warning",
        showCancelButton: true, confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Delete!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
        var url=settings.contextPath + '/assignment/manager/delete/' + id
        $.fn.ajaxDelete({ url: url });
      });
    }

    function concludePhase(id) {
      swal({
        title: "Are you sure?", text: "Please make sure you have set the goals for the next phase for this employee!", type: "warning",
        showCancelButton: true, confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Conclude!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
        var url=settings.contextPath + '/assignment/manager/change/phase-status/conclude/' + id
        $.fn.ajaxPut({
          url: url
        });
      });
    }

    function onErrorAssignedEmployees(error) {
      showErrorCard('Errors occurred while retreiving assignment information. Cause: ' + JSON.stringify(error));
    }

    function renderComplete() {
      if (settings.onComplete) {
        settings.onComplete();
      }
    }

  };
});