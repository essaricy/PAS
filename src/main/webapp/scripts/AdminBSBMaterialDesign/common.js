/*(function ( $ ) {
$('.datepicker').bootstrapMaterialDatePicker({
  format: 'DD/MM/YYYY',
  clearButton: true,
  weekStart: 1,
  time: false
});
}( jQuery ));*/


function formatDate(date) {
  return moment(date).format("DD/MM/YYYY");
}

function submitForm(url, id) {
	console.log('postData() url=' + url + ', id=' + id);
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
  console.log('tableSelector=' + tableSelector);
  console.log('name=' + name);
  var row=$('<tr>');
  row.append('<td>' + name + '</td>');
  row.append('<td><i class="material-icons" style="cursor: pointer;" onclick="deleteContainingRow(this)">delete</i></td>');
  $(tableSelector).find('tbody:last-child').append(row);
}

function setButtonWavesEffect(event) {
    $(event.currentTarget).find('[role="menu"] li a').removeClass('waves-effect');
    $(event.currentTarget).find('[role="menu"] li:not(.disabled) a').addClass('waves-effect');
}

function getPhaseStatus(code) {
  if (code == 0) {
	return '<span class="label bg-lime">Assigned</span>';
  } else if (code == 10) {
	return '<span class="label bg-orange">Self-appraisal Pending</span>';
  } else if (code == 20) {
	return '<span class="label bg-deep-orange">Awaiting Review</span>';
  } else if (code == 30) {
	return '<span class="label bg-green">Review Completed</span>';
  } else if (code == 40) {
	return '<span class="label bg-grey">Frozen</span>';
  }
  return null;
}
function getCycleStatus(code) {
  if (code == 0) {
  	return "Assigned";
  }
  return null;
}
