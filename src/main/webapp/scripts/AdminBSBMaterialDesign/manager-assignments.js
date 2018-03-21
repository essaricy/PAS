$(function () {
  $.fn.managerAssignment=function( options ) {
    var settings=$.extend({
      contextpath: null,
	  url: null,
	}, options );

    var obj=$(this);

    $.fn.ajaxGet({
   	  url: settings.url,
      onSuccess: renderAssignedEmployees,
      onError: onErrorAssignedEmployees
    });
    
    function renderAssignedEmployees(data) {
      $(data).each(function (index, cycleAssignment) {
    	var cycle=cycleAssignment.cycle;

        var cardRow=$('<div class="row clearfix">');
        var cardColumn=$('<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">');
        var card=$('<div class="card">');
        var cardHeader=$('<div class="header">');
        var cardTitle=$('<h2>' + cycle.name + '</h2><small>This appraisal cycle is <code>' + cycle.status + "</code></small>");
        var cardBody=$('<div class="body">');
        var tabsHeadings=$('<ul class="nav nav-tabs tab-col-orange" role="tablist">'
        		+ '<li role="presentation" class="active"><a href="#CycleTab_' + cycle.id +'" data-toggle="tab">Cycle</a></li>'
        		+ '<li role="presentation"><a href="#PhasesTab_' + cycle.id +'" data-toggle="tab">Phases</a></li>');
        var tabContent=$('<div class="tab-content">');

        // Render CYCLE Tab
        var cycleTabPanel=$('<div role="tabpanel" class="tab-pane fade in active" id="CycleTab_' + cycle.id +'">');
        var employeeAssignments=cycleAssignment.employeeAssignments;
        if (employeeAssignments.length==0) {
          $(cycleTabPanel).append('<p class="font-bold col-pink">No Employees have been assigned to you for this cycle.</p>');
        } else {
          var table=$('<table class="table table-striped">');
          var thead=$('<thead>');
          var theadRow=$('<tr>');
          $(table).append(thead);
          $(thead).append(theadRow);
          $(theadRow).append('<th>Employee Id</th>');
          $(theadRow).append('<th>Employee Name</th>');
          $(theadRow).append('<th>Assigned By</th>');
          $(theadRow).append('<th>Assigned On</th>');
          $(theadRow).append('<th>Status</th>');
          $(theadRow).append('<th>Action</th>');

          var tbody=$('<tbody>');
          $(employeeAssignments).each(function(index, ea) {
        	var assignedTo=ea.assignedTo;
        	var assignedBy=ea.assignedBy;

        	var row=$('<tr>');
        	$(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
        	$(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
        	$(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
        	$(row).append('<td>' + ea.assignedAt + '</td>');
        	$(row).append('<td>' + getCycleStatusLabel(ea.status) + '</td>');
        	$(row).append(getCycleActionCell(ea));
        	$(tbody).append(row);
          });
          $(table).append(tbody);
          $(cycleTabPanel).append(table);
        }

        // Render Phases Tab
        var phaseTabPanel=$('<div role="tabpanel" class="tab-pane fade out" id="PhasesTab_' + cycle.id +'">');
        var phaseAssignments=cycleAssignment.phaseAssignments;
        $(phaseAssignments).each(function(index, phaseAssignment) {
          var phase=phaseAssignment.phase;
          
          var phaseTitle=$('<h5>Phase: ' + phase.name + '</h5>');
          var employeeAssignments=phaseAssignment.employeeAssignments;

          if (employeeAssignments.length==0) {
        	$(phaseTabPanel).append(phaseTitle);
        	$(phaseTabPanel).append('<p class="font-bold col-pink">No Employees were assigned to you for this phase.</p>');
          } else {
            var table=$('<table class="table table-striped">');
            var thead=$('<thead>');
            var theadRow=$('<tr>');

            var tbody=$('<tbody>');
            $(employeeAssignments).each(function(index, ea) {
          	  var assignedTo=ea.assignedTo;
          	  var assignedBy=ea.assignedBy;

          	  var row=$('<tr>');
          	  $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
          	  $(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
          	  $(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
          	  $(row).append('<td>' + ea.assignedAt + '</td>');
          	  $(row).append('<td>' + getPhaseStatusLabel(ea.status) + '</td>');
          	  $(row).append(getPhaseActionCell(ea));
          	  $(tbody).append(row);
            });

            $(table).append(thead);
            $(thead).append(theadRow);
            $(theadRow).append('<th>Employee Id</th>');
            $(theadRow).append('<th>Employee Name</th>');
            $(theadRow).append('<th>Assigned By</th>');
            $(theadRow).append('<th>Assigned On</th>');
            $(theadRow).append('<th>Status</th>');
            $(theadRow).append('<th>Action</th>');
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
      });
    }

    function getCycleActionCell(ea) {
   	  var status=ea.status;
      var id=ea.assignmentId;
      var td=$('<td>');

      if (status == CycleAssignmentStatus.NOT_INITIATED.code) {
    	$(td).append('&nbsp;');
      } else if (status == CycleAssignmentStatus.ABRIDGED.code) {
    	$(td).append(getUpRootButton('cycle', id));
      } else if (status == CycleAssignmentStatus.CONCLUDED.code) {
    	$(td).append(getViewFormButton('cycle', id));
      }
   	  return td;
    }

    function getPhaseActionCell(ea) {
    	var status=ea.status;
    	var id=ea.assignmentId;
    	var td=$('<td>');

    	if (status == PhaseAssignmentStatus.NOT_INITIATED.code) {
    	  $(td).append(getAssignToManagerButton('phase', id));
    	  $(td).append('&nbsp;');
    	  $(td).append(getEnableFormButton('phase', id));
    	} else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.code
    			|| status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.code) {
    	  $(td).append(getSendReminderButton('phase', id));
    	} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING.code
    			|| status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.code) {
    	  $(td).append(getPerformReviewButton('phase', id));
    	} else if (status == PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED.code
    			|| status == PhaseAssignmentStatus.EMPLOYEE_AGREED.code
    			|| status == PhaseAssignmentStatus.EMPLOYEE_ESCALATED.code
    			|| status == PhaseAssignmentStatus.CONCLUDED.code) {
    	  $(td).append(getViewFormButton('phase', id));
    	} else {
    	  $(td).append('-');
    	}
    	return td;
      }

      function getUpRootButton(type, id) {
        var submitToNextLevelManagerButton=$('<button class="btn btn-xs btn-info waves-effect" ' + 
          'title="Submit to Next Level Manager" data-toggle="modal" data-target="#EmployeeSearchModal" ' + 
          'item-id="' + id + '" item-type="SubmitToNextLevelManager"><i class="material-icons">call_merge</i></button>');
  		return submitToNextLevelManagerButton;
      }

      function getAssignToManagerButton(type, id) {
    	var assignToManagerButton=$('<button class="btn btn-xs btn-info waves-effect" ' + 
    	  'title="Assign to Another Manager" data-toggle="modal" data-target="#EmployeeSearchModal" ' + 
    	  'item-id="' + id + '" item-type="AssignToAnotherManager"><i class="material-icons">trending_flat</i></button>');
  		return assignToManagerButton;
      }

      function getEnableFormButton(type, id) {
        var enableFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Enable Employee Self-Submission"><i class="material-icons">assignment_returned</i></button>');
  		$(enableFormButton).click(function() {
  		  enableSelfSubmission(type, id);
  		});
  		return enableFormButton;
      }

      function getSendReminderButton(type, id) {
        var reminderButton=$('<button class="btn btn-xs btn-info waves-effect" title="Send a reminder"><i class="material-icons">assignment_late</i></button>');
        $(reminderButton).click(function() {
          sendReminderToSubmit(id);
  	    });
   	    return reminderButton;
      }

      function getPerformReviewButton(type, id) {
    	var fillReviewButton=$('<button class="btn btn-xs btn-info waves-effect" title="Perform Review"><i class="material-icons">assignment</i></button>');
    	$(fillReviewButton).click(function() {
          location.href=settings.contextpath + '/manager/assessment/' + type + '?aid=' + id;
    	});
    	return fillReviewButton;
      }

      function getViewFormButton(type, id) {
    	var viewFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="View Appraisal Form"><i class="material-icons">assignment_ind</i></button>');
        $(viewFormButton).click(function() {
          location.href=settings.contextpath + '/manager/assessment/' + type + '?aid=' + id;
  	    });
  	    return viewFormButton;
      }

      function enableSelfSubmission(type, id) {
        swal({
    	  title: "Are you sure?", text: "Do you want to enable self-submission for this employee for this phase?", type: "warning",
    	    showCancelButton: true, confirmButtonColor: "#DD6B55",
    		confirmButtonText: "Yes, Enable!", closeOnConfirm: true
    	}, function () {
    	  $.fn.ajaxPut({
    	    url: settings.contextpath + '/assignment/manager/change/phase-status/enable/' + id
    	  });
        });
      }

      function sendReminderToSubmit(type, id) {
          swal({
      	  title: "Are you sure?", text: "Do you want to send a reminder to submit the self appraisal?", type: "warning",
      	    showCancelButton: true, confirmButtonColor: "#DD6B55",
      		confirmButtonText: "Yes, Send Reminder!", closeOnConfirm: true
      	}, function () {
      	  $.fn.ajaxPut({
      	    url: settings.contextpath + '/assignment/manager/reminder/tosubmit/' + id
      	  });
        });
      }

      function onErrorAssignedEmployees(error) {
    	console.log('error=' + error);
      }
  };
});