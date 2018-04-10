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
          $(theadRow).append('<th width="35%">Employee ID</th>');
          $(theadRow).append('<th width="50%">Employee Name</th>');
          $(theadRow).append('<th width="15%">Score</th>');

          $(employeeAssignments).each(function(jindex, ea) {
            var assignedTo=ea.assignedTo;
            var assignedBy=ea.assignedBy;
            var phaseStatus=getPhaseAssignmentStatus(ea.status);
            var row=$(tbody).find('tr:has(td:first:contains("' + assignedTo.EmployeeId + '"))');
            if(row.length == 0) {
              row=$('<tr>');
              $(row).append('<td item-id="' + ea.assignmentId + '">' + assignedTo.EmployeeId + '</td>');
              $(row).append('<td>' + assignedTo.FirstName + ' ' + assignedTo.LastName + '</td>');
            }
            if (phaseStatus == PhaseAssignmentStatus.CONCLUDED) {
              var cycleScoreLink=$('<a href="#" data-toggle="modal" data-target="#EmployeePhaseAssignments_Modal" >');
              $(cycleScoreLink).append(ea.score.toFixed(2));
              $(cycleScoreLink).attr('data-employee-id', assignedTo.EmployeeId);
              $(cycleScoreLink).attr('data-employee-name', assignedTo.FirstName + ' ' + assignedTo.LastName);

              var scoreTd=$('<td>');
              $(scoreTd).append(cycleScoreLink);
              $(row).append(scoreTd);
              $(cycleScoreLink).click(function() {
                showPhaseScores(ea.assignmentId);
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

    function showPhaseScores(assignmentId) {
      $.fn.ajaxGet({
        url: settings.contextPath + '/manager/report/cycle/phases/' + assignmentId,
        onSuccess: showEmployeePhaseScores,
        onError: showEmployeePhaseScoreError
      });
    }

    $("#EmployeePhaseAssignments_Modal" ).on('shown.bs.modal', function(e) {
      var $invoker = $(e.relatedTarget);
      $('#EmployeePhaseAssignments_Title').text($invoker.attr('data-employee-name'));
    });

    function showEmployeePhaseScores(employeePhaseAssignments) {
      if (employeePhaseAssignments == null || employeePhaseAssignments.length == 0) {
        swal({ title: "Failed!", text: 'There are no phase scores found for this employee', type: "error"});
      } else {
        var tbody=$('#EmployeePhaseAssignments_Table tbody');
        $(tbody).empty();
        $(employeePhaseAssignments).each(function (index, employeePhaseAssignment) {
          var row=$('<tr>');
          $(row).append('<td>' + employeePhaseAssignment.phase.name + '</td>');
          $(row).append('<td>' + employeePhaseAssignment.assignedBy.FirstName + ' '
              + employeePhaseAssignment.assignedBy.LastName + '</td>');
          $(row).append('<td>' + getPhaseStatusLabel(employeePhaseAssignment.status) + '</td>');
          $(row).append('<td><b>' + employeePhaseAssignment.score.toFixed(2) + '</b></td>');
          $(tbody).append(row);
        });
        $('#EmployeePhaseAssignments_Modal').show();
      }
    }

    function showEmployeePhaseScoreError(error) {
      swal({ title: "Failed!", text: JSON.stringify(error), type: "error"});
    }

    function onErrorScoreReport(error) {
      showErrorCard('Errors occurred while retreiving report. Cause: ' + JSON.stringify(error));
    }
}});
