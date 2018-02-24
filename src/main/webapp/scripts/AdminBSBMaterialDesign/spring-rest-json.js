(function ( $ ) {

  $.fn.springRestJson = function( options ) {
    // This is the easiest way to have default options.
    var settings = $.extend({
      // These are the defaults.
      type: 'table',
      url: null,
      dataSet: null,
      dataMappings: null,
      showTableOptions: null,
      initializeDataTable: true,
      afterLoadCallback: null,
      successCallback: null,
      failedCallback: null,
    }, options );

    validateSettings();

    // Data table options: "responsive", "paging", "searching", "ordering", "info", "scrollX"
    var dtResponsive=($.inArray("responsive", settings.showTableOptions) != -1)? true : false;
    var dtPaging=($.inArray("paging", settings.showTableOptions) != -1)? true : false;
    var dtSearching=($.inArray("searching", settings.showTableOptions) != -1)? true : false;
    var dtOrdering=($.inArray("ordering", settings.showTableOptions) != -1)? true : false;
    var dtInfo=($.inArray("info", settings.showTableOptions) != -1)? true : false;
    var dtScrollX=($.inArray("scrollX", settings.showTableOptions) != -1)? true : false;
    console.log('showTableOptions=' + JSON.stringify(settings.showTableOptions));

    if (settings.type == 'table') {
      table(this);
    }

    function validateSettings() {
      if (!settings.url) {
        throw "springRestJson: settings.url is not provided";
      }
      if (!settings.type) {
          throw "springRestJson: settings.type is not provided";
      }
      if (!settings.dataSet) {
          throw "springRestJson: settings.dataSet is not provided";
      }
      if (!settings.dataMappings) {
          throw "springRestJson: settings.dataMappings is not provided";
      }
    }

    function table(obj) {
      var url = settings.url;
      var extractedDataSet=null;
      //var overlay = $(obj).find(".overlay");
      var table = $(obj).find(".body table");
      var tbody = $(table).find("tbody");

      //$(overlay).show();
      //$(obj).removeClass("box-primary box-success box-danger").addClass("box-primary");
      $.get(url, {sid: new Date().getTime()}, function() {})
      .done(function(result) {
        $(tbody).empty();
        console.log('result=' + result);
        if (result["_embedded"][settings.dataSet]) {
          extractedDataSet=result["_embedded"][settings.dataSet];
		  for (index=0; index<extractedDataSet.length;index++) {
			var item=extractedDataSet[index];
			delete item["_links"];
		  }
        }
      })
      .fail(function(result) {
        console.log('springRestJson: table.fail=' + result);
        if (settings.failedCallback) {
          settings.failedCallback();
        }
        //$(obj).removeClass("box-primary box-success box-danger").addClass("box-danger");
      })
      .always(function(result) {
	    console.log('extractedDataSet=' + JSON.stringify(extractedDataSet));

    	var columns=[];
    	for (index=0; index< settings.dataMappings.length; index++) {
    	  var dataMapping = settings.dataMappings[index];
    	  columns.push({data: dataMapping});
    	}
    	console.log('dataMapping=' + JSON.stringify(dataMapping));

        //$(overlay).hide();
        if (settings.initializeDataTable) {
          $(table).DataTable({
            data: extractedDataSet,
			columns: columns,

			responsive: dtResponsive,
			paging: dtPaging,
			searching: dtSearching,
			ordering: dtOrdering,
			info: dtInfo,
			scrollX: dtScrollX
          });
          settings.initializeDataTable=false;
              /*$(obj).find('.box-tools .fa-refresh').click(function() {
                settings.initializeDataTable=false;
                loadTable(obj);
              });*/
        }
        if (settings.afterLoadCallback) {
          settings.afterLoadCallback(ticketsInQueue);
        }
      });
    };
  };
}( jQuery ));