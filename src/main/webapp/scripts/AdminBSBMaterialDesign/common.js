var PhaseAssignmentStatus={
	NOT_INITIATED:
		{code: 0, 	name: "Not Initiated", 			colorClass: "bg-grey", 			description: "Appraisal Form has not been initiated"},
	SELF_APPRAISAL_PENDING: 
		{code: 100, 	name: "Self-Appraisal Pending", colorClass: "bg-amber", 		description: "Initiated and self-appraisal is pending"},
	SELF_APPRAISAL_SAVED: 
		{code: 200, 	name: "Self-Appraisal Drafted", colorClass: "bg-orange", 		description: "self-appraisal is drafted"},
	MANAGER_REVIEW_PENDING:
		{code: 300, 	name: "Review Pending", 		colorClass: "bg-deep-orange", 	description: "Self-Appraisal is completed and manager review is pending"},
	MANAGER_REVIEW_SAVED:
		{code: 400, 	name: "Review Completed", 		colorClass: "bg-green", 		description: "Manager review is completed"},
    CONCLUDED:
		{code: 500, 	name: "Concluded", 				colorClass: "bg-deep-purple", 	description: "Assessment is concluded"},
};

var CycleAssignmentStatus={
	NOT_INITIATED:
		{code: 0, 	name: "Not Initiated",			colorClass: "bg-grey", 			description: "Created but not effective"},
	ABRIDGED:
		{code: 100, 	name: "Abridged", 			colorClass: "bg-amber", 		description: "Phases summarized and cycle assessment available"},
	CONCLUDED:
		{code: 500, 	name: "Concluded", 			colorClass: "bg-deep-purple", 	description: "Assessment is frozen"},
};

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
