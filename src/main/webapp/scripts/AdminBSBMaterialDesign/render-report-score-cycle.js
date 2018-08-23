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
          $(theadRow).append('<th width="15%">Employee #</th>');
          $(theadRow).append('<th width="50%">Employee Name</th>');
          $(theadRow).append('<th width="15%">Score</th>');
          $(theadRow).append('<th width="20%">Action</th>');

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
              $(row).append('<td class="font-bold">' + ea.score.toFixed(2) + '</td>');
              var viewFormButton=$('<button class="btn btn-xs btn-info waves-effect" title="View Appraisal Forms"><i class="material-icons">assignment_ind</i></button>');
              $(viewFormButton).tooltip({container: 'body'});
              $(viewFormButton).click(function() {
                location.href=settings.contextPath + '/manager/report/score/cycle/phases?cid=' + cycle.id + '&eid=' + assignedTo.employeeId;
              });
              var actionTd=$('<td>');
              $(actionTd).append(viewFormButton);
              $(row).append(actionTd);
            } else {
              $(row).append('<td>-</td>');
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

    function showEmployeePhaseScoreError(error) {
      swal({ title: "Failed!", text: JSON.stringify(error), type: "error"});
    }

    function onErrorScoreReport(error) {
      showErrorCard('Errors occurred while retreiving report. Cause: ' + JSON.stringify(error));
    }
  }
});
