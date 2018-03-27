$(function () {
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
    	showErrorCard('There are no assignments found');
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

        var phaseAssignments=cycleAssignment.phaseAssignments;
        $(phaseAssignments).each(function(jindex, phaseAssignment) {
          var phase=phaseAssignment.phase;

          var table=$('<table class="table table-striped">');
          var thead=$('<thead>');
          var tbody=$('<tbody>');
          var theadRow=$('<tr>');

          $(theadRow).append('<th width="15%">Employee ID</th>');
          $(theadRow).append('<th width="35%">Employee Name</th>');
          $(theadRow).append('<th>Status</th>');
          $(theadRow).append('<th width="15%">Score</th>');

          var employeeAssignments=phaseAssignment.employeeAssignments;
          if (employeeAssignments != null) {
       	    $(employeeAssignments).each(function(kindex, ea) {
   	    	  var assignedTo=ea.assignedTo;
           	  var assignedBy=ea.assignedBy;
           	  var phaseStatus=getPhaseAssignmentStatus(ea.status);

   	    	  var row=$('<tr>');
           	  $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
              $(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
              $(row).append('<td>' + getPhaseStatusLabel(ea.status) + '</td>');
              if (phaseStatus == PhaseAssignmentStatus.CONCLUDED) {
                $(row).append('<td><b>' + ea.score + '</b></td>');
              } else {
                $(row).append('<td>-</td>');
              }
              $(tbody).append(row);
        	});
          }
          var phaseTitle=$('<h5 class="font-underline col-cyan">Phase: ' + phase.name + '</h5>');
          $(cardBody).append(phaseTitle);
          $(cardBody).append(table);
          $(table).append(thead);
          $(table).append(tbody);
          $(thead).append(theadRow);
        });

        $(obj).append(cardRow);
        $(cardRow).append(cardColumn);
        $(cardColumn).append(card);
        $(card).append(cardHeader);
        $(cardHeader).append(cardTitle);
        $(card).append(cardBody);
      }
    }

    function onErrorScoreReport(error) {
      showErrorCard('Errors occurred while retreiving report. Cause: ' + JSON.stringify(error));
    }
  }
});
