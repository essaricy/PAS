$.fn.ajaxGet = function(options) {
	console.log('ajaxGet');
	$.fn.ajaxWrapper({
	  type: 'GET',
	  url: options.url,
	  onSuccess: options.onSuccess,
	  onFail: options.onFail,
	  onComplete: options.onComplete
  });
}

$.fn.ajaxWrapper = function(options) {
  console.log('ajaxWrapper ' + JSON.stringify(options));

  validateSettings();

  $.ajax({
	url: options.url,
	context: document.body
  }).done(function(result) {
    console.log(options.url + ': done: ' + JSON.stringify(result));
    if (options.onSuccess) {
      options.onSuccess(result);
    }
  }).fail(function(error) {
    console.log(options.url + ': fail: ' + JSON.stringify(error));
    if (options.onFail) {
      options.onFail(error);
    }
  }).always(function() {
	console.log(options.url + ': always: ');
	if (options.onComplete) {
	  options.onComplete();
	}
  });

  function validateSettings() {
  }
};