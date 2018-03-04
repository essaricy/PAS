$(function () {
  $.fn.renderAssignment=function( options ) {
    var settings=$.extend({
	  url: null,
	  afterLoad: null,
	}, options );

    var obj=$(this);

    $.fn.ajaxGet({
   	  url: settings.url,
      onSuccess: renderAssignedEmployees,
      onError: onErrorAssignedEmployees
    });
    
    function renderAssignedEmployees(data) {
      console.log('renderAssignedEmployees()');
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
        	$(row).append('<td>' + getCycleStatus(ea.status) + '</td>');
        	if (options.cycleActionCell) {
       		  $(row).append(options.cycleActionCell(ea));
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
        	$(phaseTabPanel).append('<p class="font-bold col-pink">No Employees have been assigned to you for this phase.</p>');
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
          	  if (options.phaseActionCell) {
          		$(row).append(options.phaseActionCell(ea));
          	  } else {
          		$(row).append('<td>-</td>');
          	  }
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
    function onErrorAssignedEmployees(error) {
    	console.log('error=' + error);
    }
  };
});