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
        console.log('employeeAssignments.length=' + employeeAssignments.length);
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
          console.log('employeeAssignments.length=' + employeeAssignments.length);

          if (employeeAssignments.length==0) {
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

          	  var row=$('<tr>');
          	  $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
          	  $(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
          	  $(row).append('<td>' + assignedBy.FirstName + ' ' + assignedBy.LastName + '</td>');
          	  $(row).append('<td>' + ea.assignedAt + '</td>');
          	  $(row).append('<td>' + getPhaseStatus(ea.status) + '</td>');
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
    	var td=$('<td>');
    	console.log('getPhaseActionCell() ' + role);

    	if (status == 0) {
    	  if (role == 'Manager') {
            var assignToManagerButton=$('<button class="btn btn-xs btn-info waves-effect" title="Assign to another manager"><i class="material-icons">trending_flat</i></button>');
            var enableFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Enable Employee Self-submission"><i class="material-icons">assignment_return</i></button>');
    		$(enableFormButton).click(function() {
    		  enableSelfSubmission(ea.assignmentId);	
    		});
    		//$(td).append(assignToManagerButton);
    		$(td).append(enableFormButton);
    	  } else {
    		$(td).append('-');
    	  }
    	} else if (status == 10) {
    	  if (role == 'Manager') {
    		$(td).append('-');
    	  } else {
            var fillFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="Complete Self-appraisal"><i class="material-icons">assignment_ind</i></button>');
            $(fillFormButton).click(function() {
              //gotoSelfSubmission(ea.assignmentId);
              location.href=settings.contextpath + '/employee/assessment?assignmentId=' + ea.assignmentId;
    	    });
    	    $(td).append(fillFormButton);
    	  }
    	} else if (status == 20) {
    	  if (role == 'Manager') {
            var fillReviewButton=$('<button class="btn btn-xs btn-info waves-effect" title="Complete Review"><i class="material-icons">assignment</i></button>');
            $(fillReviewButton).click(function() {
              //gotoSelfSubmission(ea.assignmentId);
              location.href=settings.contextpath + '/assessment/manager/' + ea.assignmentId;
    	    });
    	    $(td).append(fillReviewButton);
    	  } else {
    		$(td).append('-');
    	  }
    	} else if (status == 30) {
    	  if (role == 'Manager') {
            var freezeButton=$('<button class="btn btn-xs btn-info waves-effect" title="Freeze"><i class="material-icons">assignment_turned_in</i></button>');
            $(freezeButton).click(function() {
              freezeAssignment(ea.assignmentId);
    	    });
    	    $(td).append(freezeButton);
    	  } else {
    		$(td).append('-');
    	  }
    	} else if (status == 40) {
    	  $(td).append('-');
    	}
    	return td;
      }

      function enableSelfSubmission(assignmentId) {
        swal({
    	  title: "Are you sure?", text: "Do you want to enable self-submission for this employee for this phase?", type: "warning",
    	    showCancelButton: true, confirmButtonColor: "#DD6B55",
    		confirmButtonText: "Yes, Enable it!", closeOnConfirm: false
    	}, function () {
    	  $.fn.ajaxPut({
    	    url: settings.contextpath + '/assignment/manager/change/phase-status/enable/' + assignmentId
    	  });
        });
      }

      function freezeAssignment(assignmentId) {
        swal({
    	  title: "Are you sure?", text: "Do you want to Freeze this assignment? This cannot be undone!!!", type: "warning",
    	    showCancelButton: true, confirmButtonColor: "#DD6B55",
    		confirmButtonText: "Yes, Freeze it!", closeOnConfirm: false
    	}, function () {
    	  $.fn.ajaxPut({
    	    url: settings.contextpath + '/assignment/manager/change/phase-status/freeze/' + assignmentId
    	  });
        });
      }

    function onErrorAssignedEmployees(error) {
    	console.log('error=' + error);
    }
  };
});