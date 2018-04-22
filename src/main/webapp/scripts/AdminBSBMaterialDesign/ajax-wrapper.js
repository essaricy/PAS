$.fn.ajaxGet = function(options) {
  $.fn.ajaxWrapper({
    type: 'GET',
    url: options.url,
    onSuccess: options.onSuccess,
    onFail: options.onFail,
    onComplete: options.onComplete
  });
};

$.fn.ajaxPut = function(options) {
  $.fn.ajaxWrapper({
    type: 'PUT',
    url: options.url,
    refresh: options.refresh,
    onSuccess: options.onSuccess,
    onFail: options.onFail,
    onComplete: options.onComplete
  });
};

$.fn.ajaxPost = function(options) {
  $.fn.ajaxWrapper({
    type: 'POST',
    url: options.url,
    refresh: options.refresh,
    data: options.data,
    contentType: 'application/json',
    onSuccess: options.onSuccess,
    onFail: options.onFail,
    onComplete: options.onComplete
  });
};

$.fn.ajaxDelete = function(options) {
  $.fn.ajaxWrapper({
    type: 'DELETE',
    url: options.url,
    refresh: options.refresh,
    //data: options.data,
    //contentType: 'application/json',
    onSuccess: options.onSuccess,
    onFail: options.onFail,
    onComplete: options.onComplete
  });
};

$.fn.ajaxWrapper = function(options) {
  console.log('ajaxWrapper ' + JSON.stringify(options));

  validateSettings();

  $.ajax({
  type: options.type,
  url: options.url,
  data: (options.data) ? JSON.stringify(options.data): "",
  contentType: options.contentType,
  //context: document.body
  }).done(function(result) {
    if (options.type == 'GET') {
      if (options.onSuccess) {
        options.onSuccess(result);
      }
      if (options.onError) {
        options.onError(result);
      }
    } else if (options.type == 'POST') {
      if (result != null && result.code != 'SUCCESS') {
        if (options.onFail) {
          options.onFail(result.message, result.content);
        } else {
          var text=(result.message)?result.message:"Failed to save data.";
          swal({ title: "Failed!", text: text, type: "error"});
        }
      } else {
        if (options.onSuccess) {
          options.onSuccess(result.message, result.content);
        } else {
          var text=(result.message)?result.message:"Data has been saved successfully.";
          var refresh = (options.refresh)? ((options.refresh == "no") ? false: true) : true;
          swal({ title: "Updated!", text: text, type: "success"}, function () { if(refresh) { location.reload();} }); 
        }
      }
    } else if (options.type == 'DELETE') {
      if (result != null && result.code != 'SUCCESS') {
        if (options.onFail) {
        options.onFail(result.message, result.content);
      } else {
        var text=(result.message)?result.message:"Failed to save data.";
          swal({ title: "Failed!", text: text, type: "error"});
      }
      } else {
        if (options.onSuccess) {
          options.onSuccess(result.message, result.content);
        } else {
          var text=(result.message)?result.message:"Data has been deleted successfully.";
          swal({ title: "Deleted!", text: text, type: "success"}, function () { location.reload(); });
        }
      }
    } else if (options.type == 'PUT') {
      if (result != null && result.code != 'SUCCESS') {
        if (options.onFail) {
          options.onFail(result.message, result.content);
        } else {
          var text=(result.message)?result.message:"Failed to save data.";
          swal({ title: "Failed!", text: text, type: "error"});
        }
      } else {
        if (options.onSuccess) {
          options.onSuccess(result.message, result.content);
        } else {
          var text=(result.message)?result.message:"Data has been saved successfully.";
          var refresh = (options.refresh)? ((options.refresh == "no") ? false: true) : true;
          swal({ title: "Updated!", text: text, type: "success"}, function () { if(refresh) { location.reload();} }); 
        }
      }
    }
  }).fail(function(error) {
    console.log(options.url + ': fail: ' + JSON.stringify(error));
    if (options.onFail) {
      options.onFail(error);
    } else {
      if (error.responseJSON) {
      var errorJson=error.responseJSON;
      swal({ title: errorJson.message, text: errorJson.status + " " + errorJson.error, type: "error"});
      }
    }
  }).always(function() {
    if (options.onComplete) {
      options.onComplete();
    }
  });

  function validateSettings() {
  }
};