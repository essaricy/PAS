(function ( $ ) {

  $.fn.fillData = function( options ) {
    // This is the easiest way to have default options.
    var settings = $.extend({
      // These are the defaults.
      url: null,
      fillType: 'list',

      listDetailCard: null,
      listDetailUrl: null,
      //listDetailCallback: null,

      afterLoadCallback: null,
      successCallback: null,
      failedCallback: null,
    }, options );

    validateSettings();

    function validateSettings() {
      if (!settings.url) {
        throw "fillData: settings.url is not provided";
      }
      if (!settings.fillType) {
          throw "fillData: settings.fillType is not provided";
      }
    }

    if (settings.fillType == 'list') {
    	list(this);
    }

    function list(obj) {
      var url = settings.url;
      var header = $(obj).find('.header');
      var body = $(obj).find('.body');
      var listGroup = $(body).find('.list-group');

      $.get(url, {sid: new Date().getTime()}, function() {})
      .done(function(result) {
        $(listGroup).empty();
        if (result) {
        	for (index=0; index<result.length;index++) {
        		var item=result[index];
        		var link=$('<a href="#" class="list-group-item">');
        		$(link).attr('item-id', item['id']);
        		$(link).attr('item-name', item['name']);
        		$(link).text(item['name']);
        		$(link).val(item['name']);

        		if (index==0) {
        			$(link).click();
        		}
        		$(listGroup).append(link);
        	}
        	$(listGroup).find('.list-group-item').click(function() {
        		$(this).addClass('active').siblings().removeClass('active');
        		var itemId = $(this).attr('item-id');

        		if (settings.listDetailCard && settings.listDetailUrl) {
        			var listDetailCard = settings.listDetailCard;
        			var listDetailUrl = settings.listDetailUrl;
        			//settings.listDetailCallback($(this));
        			var listDetailCardBody = $(listDetailCard).find(".body");
        			var ol = $(listDetailCardBody).find("ol");
        			$(ol).empty();

        			$.get(listDetailUrl + '/' + itemId, {sid: new Date().getTime()}, function() {})
        	      	.done(function(result) {
        	      		for (index=0; index<result.length;index++) {
        	        		var item=result[index];
        	        		$(ol).append('<li item-id="' + item.id + ' item-name="' + item.name + '">' + item.name + '</li>');
        	      		}
        	      	});
        		}
        	});
        }
      })
      .fail(function(result) {
        if (settings.failedCallback) {
          settings.failedCallback();
        }
      })
      .always(function(result) {
    	  $(listGroup).find('.list-group-item:first').click();
      });
    };
  };
}( jQuery ));