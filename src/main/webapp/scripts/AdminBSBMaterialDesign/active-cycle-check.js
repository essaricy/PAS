$.fn.activeCycleCheck=function(options) {
  $.fn.ajaxGet({
  url: options.contextPath + '/appraisal/get/active',
  onSuccess: onSuccess,
  onError: onError,
  });

  function onSuccess(cycle) {
  if (cycle == null || cycle == "" || cycle == "undefined") {
    if (options.errorMessage) {
    showErrorCard(options.errorMessage);
    } else {
    showErrorCard("You can see this section only when the Appraisal Cycle is ACTIVE");
    }
  } else {
    if (options.onAvailable) {
    options.onAvailable(cycle);
    }
  }
  }

  function onError(error) {
  showErrorCard(error);
  }

  function showErrorCard(errorMessage) {
    $('#NoCycleAvailable').find('.body').text(errorMessage);
    $('#NoCycleAvailable').siblings().hide();
    $('#NoCycleAvailable').show();
  }

};