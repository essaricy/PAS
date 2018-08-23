$(function () {
  $.fn.enableAutoSave=function( options ) {
    var card=$(this);
    var cardBody=$(card).find('.body');

    if (!options.status) {
      throw new Error("Status is required to enable AutoSave.");
    }
    if (!options.saveFunction) {
      throw new Error("saveFunction is required to use AutoSave.");
    }
    var status=options.status;
    var alertText=null;
    var addAutoSaveNote=false;
    var enableAutoSave=false;
    if (status == PhaseAssignmentStatus.SELF_APPRAISAL_PENDING
    		|| status == PhaseAssignmentStatus.SELF_APPRAISAL_REVERTED
    		|| status == PhaseAssignmentStatus.MANAGER_REVIEW_PENDING) {
    	addAutoSaveNote=true;
    	alertText='AutoSave is currently NOT ENABLED. It will be enabled when you save this form for the first time.';
    } else if (status == PhaseAssignmentStatus.SELF_APPRAISAL_SAVED
    		|| status == PhaseAssignmentStatus.MANAGER_REVIEW_SAVED) {
    	addAutoSaveNote=true;
    	enableAutoSave=true;
    	alertText='AutoSave is currently ENABLED. Your form will be saved automatically every minute. <i></i>';
    }
    if (addAutoSaveNote) {
      var alertDiv=$('<div>');
      $(alertDiv).addClass('alert');
      $(alertDiv).addClass('bg-orange');
      $(alertDiv).append(alertText);
      $(cardBody).prepend(alertDiv);

      if (enableAutoSave) {
   	    setInterval(options.saveFunction, 1 * 60 * 1000);
      }
    }
  };
});
