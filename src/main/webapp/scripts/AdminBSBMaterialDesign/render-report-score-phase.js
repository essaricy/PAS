$(function() {
  $.fn.scoreReport=function(options) {
    var settings = $.extend({
      contextPath : null,
      url : null,
    }, options);
    var obj = $(this);
    $.fn.ajaxGet({
      url : settings.url,
      onSuccess : renderAssignments,
      onError : onErrorScoreReport
    });
    function renderAssignments(data) {
      if (data == null || data.length == 0) {
        showErrorCard('There are no assignments found');
      } else {
        renderScoreReport(data);
      }
    }

    function renderScoreReport(data) {
      for (var index = 0; index < data.length; index++) {
        var cycleAssignment = data[index];
        var cycle = cycleAssignment.cycle;
        var cycleStatus = getAppraisalCycleStatus(cycle.status);
        if (cycleStatus == AppraisalCycleStatus.DRAFT) {
          continue;
        }
        var cardRow = $('<div class="row clearfix">');
        var cardColumn = $('<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">');
        var card = $('<div class="card">');
        var cardHeader = $('<div class="header">');
        $(cardHeader).addClass(cycleStatus.colorClass);
        var cardTitle = $('<h2>' + cycle.name + '</h2>');
        var cardBody = $('<div class="body">');

        var table = $('<table class="table table-striped">');
        var thead = $('<thead>');
        var tbody = $('<tbody>');
        var theadRow = $('<tr>');
        $(theadRow).append('<th width="15%">Employee #</th>');
        $(theadRow).append('<th width="35%">Employee Name</th>');
        var numberOfColumns = 2;
        var phaseAssignments = cycleAssignment.phaseAssignments;

        for (var jindex = 0; jindex < phaseAssignments.length; jindex++) {
          var phaseAssignment = phaseAssignments[jindex];
          var phase = phaseAssignment.phase;
          var employeeAssignments = phaseAssignment.employeeAssignments;

          if (employeeAssignments != null && employeeAssignments.length != 0) {
            $(theadRow).append('<th>' + phase.name + '</th>');
            numberOfColumns++;

            $(employeeAssignments).each(function(kindex, ea) {
              var assignedTo = ea.assignedTo;
              var assignedBy = ea.assignedBy;
              var phaseStatus = getPhaseAssignmentStatus(ea.status);
              var row = $(tbody).find('tr:has(td:first:contains("' + assignedTo.employeeId + '"))');
              if (row.length == 0) {
                row = $('<tr>');
                $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.employeeId + '</td>');
                $(row).append('<td>' + assignedTo.fullName + '</td>');
              }
              // $(row).append('<td>' + getPhaseStatusLabel(ea.status) +
              // '</td>');
              if (phaseStatus == PhaseAssignmentStatus.CONCLUDED) {
                $(row).append('<td><b>' + (ea.score).toFixed(2) + '</b></td>');
              } else {
                $(row).append('<td>-</td>');
              }
              $(tbody).append(row);
            });
            $(tbody).find('tr').each(function(rowIndex, row) {
              // add empty rows
              var numberOfEmptyCellsToAdd = numberOfColumns - $(row).find('td').length;
              for (var missingCellIndex = 0; missingCellIndex < numberOfEmptyCellsToAdd; missingCellIndex++) {
                $(row).append('<td>N/A</td>');
              }
            });
          }
          $(table).append(thead);
          $(table).append(tbody);
          $(thead).append(theadRow);
        }

        $(obj).append(cardRow);
        $(cardRow).append(cardColumn);
        $(cardColumn).append(card);
        $(card).append(cardHeader);
        $(cardHeader).append(cardTitle);
        $(card).append(cardBody);
        if ($(table).find('tbody tr').length==0) {
          $(cardBody).append('<p class="font-bold col-pink">No Employees were assigned to you for this cycle.</p>');
        } else {
          $(cardBody).append(table);
        }
      }
    }
    function onErrorScoreReport(error) {
      showErrorCard('Errors occurred while retreiving report. Cause: ' + JSON.stringify(error));
    }
  }
});
