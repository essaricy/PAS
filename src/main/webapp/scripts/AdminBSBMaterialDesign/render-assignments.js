$(function () {
  $.fn.renderAssignment=function( options ) {
    var settings=$.extend({
      contextpath: null,
	  url: null,
	  role: 'Employee',
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
          if (options.role == 'Manager') {
        	$(cycleTabPanel).append('<p class="font-bold col-pink">No Employees have been assigned to you for this cycle.</p>');
          } else {
        	$(cycleTabPanel).append('<p class="font-bold col-pink">Nothing has been assigned to you for this cycle.</p>');
          }
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
        	$(row).append('<td>' + getCycleStatus(ea.status) + '</td>');
        	if (options.cycleActionCell) {
       		  $(row).append(options.cycleActionCell(options.role, ea));
        	} else {
        	  $(row).append('<td>-</td>');
        	}
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
        	if (options.role == 'Manager') {
              $(phaseTabPanel).append('<p class="font-bold col-pink">No Employees have been assigned to you for this phase.</p>');
            } else {
           	  $(phaseTabPanel).append('<p class="font-bold col-pink">Nothing has been assigned to you for this phase.</p>');
            }
          } else {
            var table=$('<table class="table table-striped">');
            var thead=$('<thead>');
            var theadRow=$('<tr>');

            var tbody=$('<tbody>');
            $(employeeAssignments).each(function(index, ea) {
          	  var assignedTo=ea.assignedTo;
          	  var assignedBy=ea.assignedBy;
          	  var status=getPhaseAssignmentStatus(ea.status)

          	  var row=$('<tr>');
          	  $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
          	  $(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
          	  $(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
          	  $(row).append('<td>' + ea.assignedAt + '</td>');
          	  $(row).append('<td>' + getPhaseStatusLabel(status) + '</td>');
          	  $(row).append(getPhaseActionCell(options.role, ea));
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
    function getPhaseActionCell(role, ea) {
    	var status=ea.status;
    	var id=ea.assignmentId;
    	var td=$('<td>');
    	//var phaseStatus = getPhaseAssignmentStatus(code);

    	if (status == PhaseAssignmentStatus.ASSIGNED.code) {
    	  if (role == 'Manager') {
    		$(td).append(getAssignToManagerButton(id));
    		$(td).append('&nbsp;');
    		$(td).append(getEnableFormButton(id));
    	  } else {
    		$(td).append('-');
    	  }
    	} else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.code
    			|| status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED.code) {
    	  if (role == 'Manager') {
    		$(td).append(getSendReminderButton(id));
    	  } else {
    		$(td).append(getFillFormButton(id));
    	  }
    	} else if (status == PhaseAssignmentStatus.REVIEW_PENDING.code) {
    	  if (role == 'Manager') {
   		    $(td).append(getFillReviewButton(id));
    	  } else {
    		$(td).append(getViewFormButton(id, role));
    	  }
    	} else if (status == PhaseAssignmentStatus.REVIEW_COMPLETED.code) {
    	  if (role == 'Manager') {
    		$(td).append(getFreezeButton(id));
    	  } else {
    		$(td).append(getViewFormButton(id, role));
    	  }
    	} else if (status == PhaseAssignmentStatus.FROZEN.code) {
    	  $(td).append(getViewFormButton(id, role));
    	}
    	return td;
      }

      function getAssignToManagerButton(id) {
        var assignToManagerButton=$('<button class="btn btn-xs btn-info waves-effect" title="Assign to another manager" data-toggle="modal" data-target="#largeModal" item-id="' + id + '"><i class="material-icons">trending_flat</i></button>');
  		return assignToManagerButton;
      }

      function getEnableFormButton(id) {
        var enableFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Enable Employee Self-Submission"><i class="material-icons">assignment_returned</i></button>');
  		$(enableFormButton).click(function() {
  		  enableSelfSubmission(id);
  		});
  		return enableFormButton;
      }

      function getSendReminderButton(id) {
        var reminderButton=$('<button class="btn btn-xs btn-info waves-effect" title="Send a reminder"><i class="material-icons">assignment_late</i></button>');
        $(reminderButton).click(function() {
  	    });
   	    return reminderButton;
      }

      function getFillFormButton(id) {
        var fillFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Complete Self-appraisal"><i class="material-icons">assignment</i></button>');
        $(fillFormButton).click(function() {
          location.href=settings.contextpath + '/employee/assessment?aid=' + id;
  	    });
  	    return fillFormButton;
      }

      function getFillReviewButton(id) {
    	var fillReviewButton=$('<button class="btn btn-xs btn-info waves-effect" title="Complete Review"><i class="material-icons">assignment</i></button>');
    	$(fillReviewButton).click(function() {
          location.href=settings.contextpath + '/manager/assessment?aid=' + id;
    	});
    	return fillReviewButton;
      }

      function getViewFormButton(id, role) {
    	var viewFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="View Appraisal Form"><i class="material-icons">assignment_ind</i></button>');
        $(viewFormButton).click(function() {
          if (role == 'Manager') {
        	location.href=settings.contextpath + '/manager/assessment?aid=' + id;
          } else if (role == 'Employee') {
        	location.href=settings.contextpath + '/employee/assessment?aid=' + id;
          } 
  	    });
  	    return viewFormButton;
      }

      function getFreezeButton(id) {
        var freezeButton=$('<button class="btn btn-xs btn-info waves-effect" title="Freeze"><i class="material-icons">assignment_turned_in</i></button>');
        $(freezeButton).click(function() {
          freezeAssignment(id);
  	    });
  	    return freezeButton;
      }

      function enableSelfSubmission(id) {
        swal({
    	  title: "Are you sure?", text: "Do you want to enable self-submission for this employee for this phase?", type: "warning",
    	    showCancelButton: true, confirmButtonColor: "#DD6B55",
    		confirmButtonText: "Yes, Enable it!", closeOnConfirm: false
    	}, function () {
    	  $.fn.ajaxPut({
    	    url: settings.contextpath + '/assignment/manager/change/phase-status/enable/' + id
    	  });
        });
      }

      function freezeAssignment(id) {
        swal({
    	  title: "Are you sure?", text: "Do you want to Freeze this assignment? This cannot be undone!!!", type: "warning",
    	    showCancelButton: true, confirmButtonColor: "#DD6B55",
    		confirmButtonText: "Yes, Freeze it!", closeOnConfirm: false
    	}, function () {
    	  $.fn.ajaxPut({
    	    url: settings.contextpath + '/assignment/manager/change/phase-status/freeze/' + id
    	  });
        });
      }

    function onErrorAssignedEmployees(error) {
    	console.log('error=' + error);
    }
  };
});