$(function () {
  $.fn.employeeAssignment=function( options ) {
    var settings=$.extend({
      contextPath: null,
    url: null,
  }, options );

    var obj=$(this);

    $.fn.ajaxGet({
      url: settings.url,
      onSuccess: renderAssignments,
      onError: onErrorAssignedEmployees
    });

    function renderAssignments(data) {
      if (data == null || data.length == 0) {
      showErrorCard('There are no assignments found');
      } else{
      renderEmployeeAssignments(data);
      }
    }

    function renderEmployeeAssignments(data) {
      //$(data).each(function (index, cycleAssignment) {
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
        //var cardTitle=$('<h2>' + cycle.name + '</h2><small>This appraisal cycle is <code>' + cycle.status + "</code></small>");
        var cardTitle=$('<h2>' + cycle.name + '</h2>');
        var cardBody=$('<div class="body">');
        var tabsHeadings=$('<ul class="nav nav-tabs tab-col-orange" role="tablist">'
            + '<li role="presentation" class="active"><a href="#PhasesTab_' + cycle.id +'" data-toggle="tab">Phases</a></li>'
            + '<li role="presentation"><a href="#CycleTab_' + cycle.id +'" data-toggle="tab">Cycle</a></li>'
            );
        var tabContent=$('<div class="tab-content">');

        // Render CYCLE Tab
        var cycleTabPanel=$('<div role="tabpanel" class="tab-pane fade out" id="CycleTab_' + cycle.id +'">');
        var employeeAssignment=cycleAssignment.employeeAssignment;
        if (employeeAssignment==null) {
          $(cycleTabPanel).append('<p class="font-bold col-pink">Nothing was assigned to you for this cycle.</p>');
        } else {
          var table=$('<table class="table table-striped">');
          var thead=$('<thead>');
          var theadRow=$('<tr>');
          $(table).append(thead);
          $(thead).append(theadRow);
          //$(theadRow).append('<th>Employee Id</th>');
         // $(theadRow).append('<th>Employee Name</th>');
          $(theadRow).append('<th>Assigned By</th>');
          $(theadRow).append('<th>Assigned On</th>');
          $(theadRow).append('<th>Status</th>');
          $(theadRow).append('<th>Action</th>');

          var ea=employeeAssignment;

          var tbody=$('<tbody>');
          var assignedTo=ea.assignedTo;
          var assignedBy=ea.assignedBy;
          
          var row=$('<tr>');
          //$(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
          //$(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
          $(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
          $(row).append('<td>' + ea.assignedAt + '</td>');
          $(row).append('<td>' + getCycleStatusLabel(ea.status) + '</td>');
          $(row).append(getCycleActionCell(ea));
          $(tbody).append(row);
          $(table).append(tbody);
          $(cycleTabPanel).append(table);
        }

        // Render Phases Tab
        var phaseTabPanel=$('<div role="tabpanel" class="tab-pane fade in active" id="PhasesTab_' + cycle.id +'">');
        var phaseAssignments=cycleAssignment.phaseAssignments;
        $(phaseAssignments).each(function(index, phaseAssignment) {
          var phase=phaseAssignment.phase;
          
          var phaseTitle=$('<h5 class="font-underline col-cyan">Phase: ' + phase.name + '</h5>');
          var employeeAssignments=phaseAssignment.employeeAssignments;

          if (employeeAssignments.length==0) {
          $(phaseTabPanel).append(phaseTitle);
            $(phaseTabPanel).append('<p class="font-bold col-pink">Nothing was assigned to you for this phase.</p>');
          } else {
            var table=$('<table class="table table-striped">');
            var thead=$('<thead>');
            var tbody=$('<tbody>');
            var theadRow=$('<tr>');

            $(employeeAssignments).each(function(index, ea) {
              var assignedTo=ea.assignedTo;
              var assignedBy=ea.assignedBy;

              var row=$('<tr>');
              //$(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
              //$(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
              $(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
              $(row).append('<td>' + ea.assignedAt + '</td>');
              $(row).append('<td>' + getPhaseStatusLabel(ea.status) + '</td>');
              $(row).append(getPhaseActionCell(ea));
              $(tbody).append(row);
            });

            $(table).append(thead);
            $(thead).append(theadRow);
            //$(theadRow).append('<th>Employee Id</th>');
            //$(theadRow).append('<th>Employee Name</th>');
            $(theadRow).append('<th width="30%">Assigned By</th>');
            $(theadRow).append('<th width="20%">Assigned On</th>');
            $(theadRow).append('<th width="30%">Status</th>');
            $(theadRow).append('<th width="20%">Action</th>');
            $(table).append(tbody);
            $(phaseTabPanel).append(phaseTitle);
            $(phaseTabPanel).append(table);
          } 
        });

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
      }//);
    }

    function getCycleActionCell(ea) {
      var status=ea.status;
      var id=ea.assignmentId;
      var td=$('<td>');
      $(td).append('&nbsp;');
      return td;
    }

    function getPhaseActionCell(ea) {
      var status=ea.status;
      var id=ea.assignmentId;
      var td=$('<td>');

      if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.code
          || status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.code) {
        $(td).append(getFillFormButton(id));
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.code
          || status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.code
          || status == PhaseAssignmentStatus.EMPLOYEE_AGREED.code
          || status == PhaseAssignmentStatus.CONCLUDED.code) {
        $(td).append(getViewFormButton('phase', id));
      } else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED.code) {
        $(td).append(getViewFormButton('phase', id));
          $(td).append('&nbsp;');
          $(td).append(getAgreeReviewButton(id));
        $(td).append('&nbsp;');
        $(td).append(getEscalateReviewButton(id));
      } else if (status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED.code) {
        $(td).append(getViewFormButton('phase', id));
        $(td).append('&nbsp;');
        $(td).append(getAgreeReviewButton(id));
      } else {
        $(td).append('-');
      }
      return td;
      }

      function getFillFormButton(id) {
        var fillFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Complete Self-appraisal"><i class="material-icons">assignment</i></button>');
        $(fillFormButton).tooltip({container: 'body'});
        $(fillFormButton).click(function() {
          location.href=settings.contextPath + '/employee/assessment/phase?aid=' + id;
        });
        return fillFormButton;
      }

      function getViewFormButton(type, id) {
      var viewFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="View Appraisal Form"><i class="material-icons">assignment_ind</i></button>');
      $(viewFormButton).tooltip({container: 'body'});
        $(viewFormButton).click(function() {
          location.href=settings.contextPath + '/employee/assessment/' + type + '?aid=' + id;
        });
        return viewFormButton;
      }

      function getAgreeReviewButton(id) {
        var agreeButton=$('<button class="btn btn-xs btn-info waves-effect" title="Agree with the review"><i class="material-icons">sentiment_very_satisfied</i></button>');
        $(agreeButton).tooltip({container: 'body'});
        $(agreeButton).click(function() {
          agreeReview(id);
        });
        return agreeButton;
      }

      function getEscalateReviewButton(id) {
        var escalateButton=$('<button class="btn btn-xs btn-info waves-effect" title="Disagree with the review"><i class="material-icons">sentiment_very_dissatisfied</i></button>');
        $(escalateButton).tooltip({container: 'body'});
        $(escalateButton).click(function() {
          escalateReview(id);
        });
        return escalateButton;
      }

      function agreeReview(id) {
        swal({
          title: "Are you sure?", text: "Do you agree with your manager review? This will conclude the assignment and cannot be reverted!!!", type: "warning",
            showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Agree!", closeOnConfirm: false
        }, function () {
          var url=settings.contextPath + '/assignment/employee/change/phase-status/agree/' + id
          $.fn.ajaxPut({
            url: url
          });
        });
      }

      function escalateReview(id) {
        swal({
          title: "Are you sure?", text: "Please follow the escalation procedures sent by the HR. You may come back here and AGREE once the escalation has been resolved.", type: "warning",
            showCancelButton: true, confirmButtonColor: "#DD6B55",
          confirmButtonText: "Yes, Escalate!", closeOnConfirm: false
        }, function () {
          var url=settings.contextPath + '/assignment/employee/change/phase-status/escalate/' + id
          $.fn.ajaxPut({
            url: url
          });
        });
      }

      function onErrorAssignedEmployees(error) {
      showErrorCard('Errors occurred while retreiving assignment information. Cause: ' + JSON.stringify(error));
      }
  };
});