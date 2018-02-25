(function ( $ ) {
  $.fn.menuActions=function( options ) {
    var settings=$.extend({
      dataset: null,
      items: null,
      menuConfig: [
    	  {
    		  name: "Add",
    		  type: 'navigate',
    		  url: null,
    		  selectionRequired: false,
    		  beforeCallback: null,
    		  afterCallback: null
    	  }, {
    		  name: "Update",
    		  type: 'navigate',
    		  url: null,
    		  selectionRequired: true,
    		  beforeCallback: null,
    		  afterCallback: null,
    	  }
      ]
    }, options );

    var obj=$(this);
    var cardHeader=$(obj).find('.header');
    var cardBody=$(obj).find('.body');

    validateSettings();

    function validateSettings() {
      if (!settings.dataset) {
        throw "menuActions: settings.dataset is not provided";
      }
      if (!settings.items) {
        throw "menuActions: settings.items is not provided";
      }
      if (!settings.menuConfig) {
        throw "menuActions: settings.menuConfig is not provided";
      }
      if (!settings.menuConfig) {
        throw "menuActions: settings.menuConfig is not provided";
      }
      $(settings.menuConfig).each(function(index, menu) {
    	if (!menu.name) {
          throw "menuActions: settings.menuConfig.name is not provided @ " + index;
        }
    	if (!menu.type) {
    	  throw "menuActions: settings.menuConfig.type is not provided @ " + index;
        }
    	if (!menu.url) {
    	  throw "menuActions: settings.menuConfig.url is not provided @ " + index;
        }
      });
    }

    $(settings.menuConfig).each(function(index, menu) {
    	var name=menu.name;
    	var type=menu.type;
    	var url=menu.url;
    	var icon=menu.icon;
    	var selectionRequired=(menu.selectionRequired) ? true:false;
    	var beforeCallback=menu.beforeCallback;
    	var afterCallback=menu.afterCallback;

    	var menu_li=$('<li>');
        var menu_link=$('<a href="' + (manageUrl + "?id=0") + '"><i class="material-icons">' + icon + '</i> Add</a>');
        $(add_li).append(add_link);
        $(actionsMenu).append(add_li);

    	if (selectionRequired) {
    		
    	} else {
    		
    	}
      
    });

    console.log('Loading data from ' + loadUrl);
    // Retrieve data from URL
    $.get(loadUrl, {sid: new Date().getTime()}, function() {})
    .done(function(data) {
      //console.log('data=' + data);
      if (data) {

        console.log('data.length=' + data.length);
        if (data.length == 0) {
          //var div=$('<div class="alert alert-danger">There are no records found</div>');
          //var noRecordsNote=$('<p class="font-bold col-pink">There are no records found</p>');
          var noRecordsNote=$('<p class="col-pink">There are no records found</p>');
          $(cardBody).append(noRecordsNote);
        } 

        var activeItemSelector=null;
        if (settings.type == 'list-with-links') {
          var listGroup=$('<ul class="list-group">');
          $(obj).find(".body").append(listGroup);
          activeItemSelector='.body .list-group .list-group-item';
          // Distribute current data to the current container
          renderList(listGroup, data, 'hyperlinks');
        }
        if (activeItemSelector != null) {
          createMenuActions(data, activeItemSelector);
          if (data.length != 0) {
        	  $(activeItemSelector).find(':first').click();
          }
        }
      }
      if (settings.afterLoadCallback) {
    	  settings.afterLoadCallback($(''+activeItemSelector), data);
      }
    })
    .fail(function(result) {
      console.log('cardManager: list.fail=' + result);
      if (settings.failedCallback) {
        settings.failedCallback();
      }
    })
    .always(function(result) {
    });

    function renderList(container, data, listingType) {
      $(container).empty();
      if (listingType == 'hyperlinks') {
        $(data).each(function (index, item) {
          // Add links to the list
    	  var link=$('<a href="#" class="list-group-item">');
    	  $(link).attr('item-id', item['id']);
    	  $(link).attr('item-name', item['name']);
    	  $(link).text(item['name']);
    	  $(container).append(link);
          $(link).click(function() {
            console.log('Link clicked in list. Rendering children');
            $(this).addClass('active').siblings().removeClass('active');
            // Show items in child container
            renderChildren(item);
          });
        });
      } else if (listingType==null) {
        var ol=$("<ol>");
        $(data).each(function (index, item) {
          $(ol).append('<li item-id="' + item.id + ' item-name="' + item.name + '">' + item.name + '</li>');
        });
        $(container).append(ol);
      }
    }

    function renderTable(config, data) {
      var container=config.toContainer;
      var headerNames=config.headerNames;
      var columnMappings=config.columnMappings;

      $(container).empty();
      console.log('data=' + JSON.stringify(data));

      var table=$('<table class="table table-condensed table-hover">');
      var thead=$('<thead>');
      var tbody=$('<tbody>');
      $(container).append(table);
      $(table).append(thead);
      $(table).append(tbody);
      var headRow=$('<tr>');
      $(thead).append(headRow);
      $(headerNames).each(function(index, headerName) {
        $(headRow).append('<th>' + headerName + '</th>');
      });

      $(data).each(function (index, item) {
        console.log('item=' + item);
        var dataRow=$('<tr>');
        $(tbody).append(dataRow);
        $(columnMappings).each(function(index, columnMapping) {
          $(dataRow).append('<td>' + item[columnMapping] + '</td>');
        });
      });
    }

    function renderChildren(data) {
      console.log("renderChildren()");
      $(settings.renderConfigs).each(function (index, config) {
        var renderType=config.type;
        var renderFromNode=config.fromNode;
        var renderToContainer=config.toContainer;
        var renderListingType=null;

        if (config.listingType) {
          renderListingType=config.listingType;
        }
        console.log('renderType=' + renderType + ", renderFromNode=" + renderFromNode
        		+ ", renderToContainer=" + renderToContainer + ", renderListingType=" + renderListingType);
        if (renderType && renderFromNode && renderToContainer) {
          if (renderType == 'list') {
            renderList($(renderToContainer), data[renderFromNode], renderListingType);
          } else if (renderType == 'table') {
            renderTable(config, data[renderFromNode]);
          }
        }
      });	
    }

    function createMenuActions(data, itemsSelector) {
      var menuActions=settings.menuActions;
      if (menuActions != null && menuActions.length != 0) {
        var a=$('<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">');
        var i=$('<i class="material-icons">more_vert</i>');
        $(a).append(i);

        var actionsMenu=$('<ul class="dropdown-menu pull-right">');
        $(obj).find('.header .header-dropdown .dropdown').append(a);
        $(obj).find('.header .header-dropdown .dropdown').append(actionsMenu);

        console.log("$.inArray(Add, menuActions)?" + $.inArray("Add", menuActions))
        if ($.inArray("Add", menuActions) > -1) {
          createAddMenuAction(actionsMenu, settings.manageUrl)
        }
        if ($.inArray("Update", menuActions) > -1 && data.length > 0) {
          createUpdateMenuAction(actionsMenu, settings.manageUrl, itemsSelector);
        }
        if ($.inArray("Delete", menuActions) > -1 && data.length > 0) {
          createDeleteMenuAction(actionsMenu, settings.deleteUrl, itemsSelector);
        }
      }
    }

    function createAddMenuAction(actionsMenu, manageUrl) {
      var add_li=$('<li>');
      var add_link=$('<a href="' + (manageUrl + "?id=0") + '"><i class="material-icons">add</i> Add</a>');
      $(add_li).append(add_link);
      $(actionsMenu).append(add_li);
    }

    function createUpdateMenuAction(actionsMenu, manageUrl, itemsSelector) {
      var update_li=$('<li>');
      var update_link=$('<a href="#"><i class="material-icons">create</i> Update</a>');
      $(update_link).click(function(e) {
        e.preventDefault();
        var itemId=getActiveRecordId(itemsSelector);
        if (itemId == null) {
          swal('Please select a record to update');
        } else {
          location.href=manageUrl + "?id=" + itemId;
        }
      });
      $(update_li).append(update_link);
      $(actionsMenu).append(update_li);
    }

    function createDeleteMenuAction(actionsMenu, deleteUrl, itemsSelector) {
      var delete_li=$('<li>');
      var delete_link=$('<a><i class="material-icons">clear</i> Delete</a>');
      $(delete_link).click(function(e) {
        e.preventDefault();
        var itemId=getActiveRecordId(itemsSelector);
        if (itemId == null) {
          swal('Please select a record to delete');
        } else {
          swal(
            {
              title: "Are you sure?", text: "You will not be able to recover this data!", type: "warning",
        	  showCancelButton: true, confirmButtonColor: "#DD6B55",
        	  confirmButtonText: "Yes, delete it!", closeOnConfirm: false
        	}, function () {
        		ajaxDelete(deleteUrl + "/" + itemId, null, function() {
        		  swal({ title: "Deleted!", text: "Deleted successfully.", type: "success"}, function () {location.reload();});
        	  }, null, null);

        	  /*$.get(deleteUrl + "/" + itemId, {sid: new Date().getTime()}, function() {})
        	  .done(function(result) {
        		swal({ title: "Deleted!", text: "Deleted successfully.", type: "success"},
        		function () {
        		  location.reload();
        		});
        	  }).fail(function(result) {
        		swal("Failed!", "Errors occured while deleting", "error");
        	  }).always(function(result) {
        	  });*/
            }
          );
        }
      });
	  $(delete_li).append(delete_link);
      $(actionsMenu).append(delete_li);
    }
    
    function getActiveRecordId(itemsSelector) {
      var itemId=null;
      if ($(''+itemsSelector).filter(".active" ).length == 1) {
        itemId=$(itemsSelector).filter(".active").attr('item-id');
      }
      return itemId;
    }

  };
}( jQuery ));