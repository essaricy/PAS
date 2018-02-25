$('.datepicker').bootstrapMaterialDatePicker({
  format: 'DD/MM/YYYY',
  clearButton: true,
  weekStart: 1,
  time: false
});


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

var getJSON = function (url, successCallback, errorCallback) {
  $.ajax({
    type: 'GET',
    url: url,
    success: function(result) {
      console.log(url + ': ' + JSON.stringify(result));
      if (successCallback) {
    	successCallback(result);
 	  }
    },
    error: function(error) {
      console.log('error= ' + JSON.stringify(error));
      if (errorCallback) {
      	errorCallback(error);
	  }
 	}
  });
}


var postJSON = function (url, data, successCallback, failureCallback, errorCallback) {
	sendAjax('POST',url, data, successCallback, failureCallback, errorCallback)
}

var ajaxDelete = function (url, data, successCallback, failureCallback, errorCallback) {
	sendAjax('DELETE',url, data, successCallback, failureCallback, errorCallback)
}

var sendAjax = function (type, url, data, successCallback, failureCallback, errorCallback) {
    $.ajax({
 		type: type,
 	  	contentType: 'application/json',
 	  	url: url,
 	  	data: JSON.stringify(data),
 	  	success: function(result) {
 	  	  if (result != null && result.code != 'SUCCESS') {
 	  		if (failureCallback) {
 	  		  failureCallback(result.message, result.content);
 	  		} else {
 	  			swal("Failed to save data", result.message);
 	  		}
 	  	  } else {
 	  		if (successCallback) {
 	  	      successCallback(result.content);
 	  		} else {
 	  		  swal("Data has been saved successfully.");
 	  		}
 	  	  }
 	    },
 	    error: function(error) {
        console.log('error= ' + JSON.stringify(error));
        if (errorCallback) {
      	errorCallback(error);
		  }
 	    }
 	  });
  }