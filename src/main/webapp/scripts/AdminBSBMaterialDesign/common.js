var AppraisalCycleStatus = {
  DRAFT : {
    code : "DRAFT",
    name : "Draft",
    label : "Draft",
    colorClass : "bg-amber",
    icon: "drafts"
  },
  READY : {
    code : "READY",
    name : "Ready",
    label : "Ready",
    colorClass : "bg-lime",
    icon: "mail"
  },
  ACTIVE : {
    code : "ACTIVE",
    name : "Active",
    label : "Activate",
    colorClass : "bg-green",
    icon: "flag"
  },
  COMPLETE : {
    code : "COMPLETE",
    name : "Complete",
    label : "Mark Complete",
    colorClass : "bg-blue-grey",
    icon: "archive"
  },
};

var PhaseAssignmentStatus = {
  NOT_INITIATED : {
    code : 0,
    name : "Not Initiated",
    colorClass : "bg-grey",
    icon : "content_paste",
    progressCode : 1,
    description : "Appraisal form has not been initiated"
  },
  SELF_APPRAISAL_PENDING : {
    code : 50,
    name : "Self-Appraisal Pending",
    colorClass : "bg-amber",
    icon : "assignment_late",
    progressCode : 2,
    description : "Initiated and self appraisal is pending"
  },
  SELF_APPRAISAL_SAVED : {
    code : 100,
    name : "Self-Appraisal in Progress",
    colorClass : "bg-lime",
    icon : "assignment_late",
    progressCode : 2,
    description : "Self appraisal is in progress"
  },
  SELF_APPRAISAL_REVERTED : {
    code : 125,
    name : "Self-Appraisal Reverted",
    colorClass : "bg-orange",
    icon : "assignment_return",
    progressCode : 2,
    description : "Self appraisal was submitted but it has been reverted by the manager"
  },
  MANAGER_REVIEW_PENDING : {
    code : 150,
    name : "Review Pending",
    colorClass : "bg-pink",
    icon : "rate_review",
    progressCode : 3,
    description : "Self appraisal is submitted and awaiting manager review"
  },
  MANAGER_REVIEW_SAVED : {
    code : 200,
    name : "Review in Progress",
    colorClass : "bg-light-green",
    icon : "rate_review",
    progressCode : 3,
    description : "Manager review is in progress"
  },
  MANAGER_REVIEW_SUBMITTED : {
    code : 250,
    name : "Reviewed",
    colorClass : "bg-indigo",
    icon : "assignment_ind",
    progressCode : 4,
    description : "Manager review is completed"
  },
  EMPLOYEE_AGREED : {
    code : 300,
    name : "Agreed with Review",
    colorClass : "bg-green",
    icon : "mood",
    progressCode : 5,
    description : "Employee agreed to the manager's review"
  },
  EMPLOYEE_ESCALATED : {
    code : 350,
    name : "Escalated",
    colorClass : "bg-red",
    icon : "mood_bad",
    progressCode : 5,
    description : "Employee disagreed to the manager's review and escalated"
  },
  CONCLUDED : {
    code : 500,
    name : "Concluded",
    colorClass : "bg-purple",
    icon : "assignment_turned_in",
    progressCode : 6,
    description : "Assessment is concluded"
  },
};

var CycleAssignmentStatus = {
  NOT_INITIATED : {
    code : 0,
    name : "Not Initiated",
    colorClass : "bg-grey",
    description : "Created but not effective"
  },
  ABRIDGED : {
    code : 100,
    name : "Abridged",
    colorClass : "bg-amber",
    description : "Phases summarized and cycle assessment available"
  },
  CONCLUDED : {
    code : 500,
    name : "Concluded",
    colorClass : "bg-deep-purple",
    description : "Assessment is frozen"
  },
};

function getAppraisalCycleStatus(code) {
  for (var key in AppraisalCycleStatus) {
  var data=AppraisalCycleStatus[key];
  if (code == data.code) {
    return data;
    }
  }
  return null;
}

function getPhaseAssignmentStatus(code) {
  for (var key in PhaseAssignmentStatus) {
  var data=PhaseAssignmentStatus[key];
  if (code == data.code) {
    return data;
    }
  }
  return null;
}

function getCycleAssignmentStatus(code) {
  for (var key in CycleAssignmentStatus) {
  var data=CycleAssignmentStatus[key];
  if (code == data.code) {
    return data;
    }
  }
  return null;
}

function getPhaseStatusLabel(code) {
  var status = getPhaseAssignmentStatus(code);
  return (status == null) ? '' : '<span class="label ' + status.colorClass + '">' + status.name + '</span>';
}

function getCycleStatusLabel(code) {
  var status = getCycleAssignmentStatus(code);
  return (status == null) ? '' : '<span class="label ' + status.colorClass + '">' + status.name + '</span>';
}

function formatDate(date) {
  return moment(date).format("DD/MM/YYYY");
}

function submitForm(url, id) {
  var form= document.createElement('form');
  form.method= 'get';
  form.action= url;
  
  var input= document.createElement('input');
  input.type= 'hidden';
  input.name= 'id';
  input.value= id;
  form.appendChild(input);
  
  document.body.appendChild(form);
  form.submit();
}

function deleteContainingRow(obj) {
  $(obj).closest('tr').remove();
}

function appendTableRow(tableSelector, name) {
  var row=$('<tr>');
  row.append('<td>' + name + '</td>');
  row.append('<td><i class="material-icons" style="cursor: pointer;" onclick="deleteContainingRow(this)">delete</i></td>');
  $(tableSelector).find('tbody:last-child').append(row);
}

function setButtonWavesEffect(event) {
    $(event.currentTarget).find('[role="menu"] li a').removeClass('waves-effect');
    $(event.currentTarget).find('[role="menu"] li:not(.disabled) a').addClass('waves-effect');
}

function showErrorCard(errorMessage) {
  $('#NoCycleAvailable').find('.body').text(errorMessage);
  $('#NoCycleAvailable').siblings().hide();
  $('#NoCycleAvailable').show();
}

var PhaseAssignmentReportStatus = {
	NOT_ASSIGNGED : {
		code : -1,
		name : "Not Assigned",
		colorClass : "bg-blue-grey",
		icon : "assignment",
		progressCode : 1,
		description : "Template has not been assigned"
	},
	NOT_INITIATED : PhaseAssignmentStatus.NOT_INITIATED,
	SELF_APPRAISAL_PENDING : PhaseAssignmentStatus.SELF_APPRAISAL_PENDING,
	SELF_APPRAISAL_SAVED : PhaseAssignmentStatus.SELF_APPRAISAL_SAVED,
	SELF_APPRAISAL_REVERTED: PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED,
	MANAGER_REVIEW_PENDING : PhaseAssignmentStatus.MANAGER_REVIEW_PENDING,
	MANAGER_REVIEW_SAVED : PhaseAssignmentStatus.MANAGER_REVIEW_SAVED,
	MANAGER_REVIEW_SUBMITTED : PhaseAssignmentStatus.MANAGER_REVIEW_SUBMITTED,
	EMPLOYEE_AGREED : PhaseAssignmentStatus.EMPLOYEE_AGREED,
	EMPLOYEE_ESCALATED : PhaseAssignmentStatus.EMPLOYEE_ESCALATED,
	CONCLUDED : PhaseAssignmentStatus.CONCLUDED,
};

function getPhaseAssignmentReportStatus(code) {
	for ( var key in PhaseAssignmentReportStatus) {
		var data = PhaseAssignmentReportStatus[key];
		if (code == data.code) {
			return data;
		}
	}
	return null;
}

function getPhaseAssignmentReportStatusLabel(code) {
	  var status = getPhaseAssignmentReportStatus(code);
	  return (status == null) ? '' : '<span class="label ' + status.colorClass + '">' + status.name + '</span>';
}
