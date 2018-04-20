(function ( $ ) {
  $.fn.cardManager=function( options ) {
    var settings=$.extend({
      type: null,
      loadUrl: null,
      manageUrl: null,
      deleteUrl: null,
      onClickCallback: null,

      renderConfigs: [],
      menuActions: [],

      afterLoadCallback: null,
      successCallback: null,
      failedCallback: null,
    }, options );

    validateSettings();

    var loadUrl = settings.loadUrl;

    var obj=$(this);
    var cardHeader=$(obj).find('.header');
    var cardBody=$(obj).find('.body');

    // Retrieve data from URL
    $.get(loadUrl, {sid: new Date().getTime()}, function() {})
    .done(function(data) {
      if (data) {
        if (data.length == 0) {
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
      var items = $(''+activeItemSelector);
      if (items != null && items.length != 0) {
        $(items)[0].click();
      }
      if (settings.afterLoadCallback) {
      settings.afterLoadCallback(items, data);
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

    function validateSettings() {
      if (!settings.type) {
        throw "cardManager: settings.type is not provided";
      }
      if (!settings.loadUrl) {
        throw "cardManager: settings.loadUrl is not provided";
      }
      if (!settings.manageUrl) {
        throw "cardManager: settings.manageUrl is not provided";
      }
    }

    function renderList(container, data, listingType) {
      $(container).empty();
      if (listingType == 'hyperlinks') {
        $(data).each(function (index, item) {
          // Add links to the list
        var link=$('<a href="#" class="list-group-item">');
        $(link).attr('item-id', item['id']);
        $(link).attr('item-name', item['name']);
        //var heading=$('<h4 class="list-group-item-heading">' + item['name'] + '</h4>');
        //$(link).append(heading);
        $(link).text(item['name']);
        $(container).append(link);
          $(link).click(function() {
            $(this).addClass('active').siblings().removeClass('active');
            // Show items in child container
            renderChildren(item);
            if (settings.onClickCallback) {
              settings.onClickCallback(item);
            }
          });
        });
      } else if (listingType==null) {
        var ol=$("<ol>");
        $(data).each(function (index, item) {
          $(ol).append('<li item-id="' + item.id + '" item-name="' + item.name + '">' + item.name + '</li>');
        });
        $(container).append(ol);
      }
    }

    function renderTable(config, data) {
      var container=config.toContainer;
      var headerNames=config.headerNames;
      var columnMappings=config.columnMappings;

      $(container).empty();

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
        var dataRow=$('<tr>');
        $(tbody).append(dataRow);
        $(columnMappings).each(function(index, columnMapping) {
          $(dataRow).append('<td>' + item[columnMapping] + '</td>');
        });
      });
    }

    function renderChildren(data) {
      $(settings.renderConfigs).each(function (index, config) {
        var renderType=config.type;
        var renderFromNode=config.fromNode;
        var renderToContainer=config.toContainer;
        var renderListingType=null;

        if (config.listingType) {
          renderListingType=config.listingType;
        }
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

        $(menuActions).each(function(index, menuAction) {
          if (menuAction == "Add") {
            createAddMenuAction(actionsMenu, settings.manageUrl);
          } else if (menuAction == "Update") {
            if (data.length > 0) {
              createUpdateMenuAction(actionsMenu, settings.manageUrl, itemsSelector);
            }
          } else if (menuAction == "Delete") {
            if (data.length > 0) {
              createDeleteMenuAction(actionsMenu, settings.deleteUrl, itemsSelector);
            }
          } else if (data.length > 0) {
            var callbackFunction = settings['on' + menuAction + 'Callback'];
            var menuIcon = settings[menuAction + 'Icon'];

            var add_li=$('<li>');
            var add_link=$('<a><i class="material-icons">' + menuIcon + '</i> ' + menuAction + '</a>');
            $(add_li).append(add_link);
            $(actionsMenu).append(add_li);

            if (callbackFunction) {
              console.log('callbackFunction is set' + callbackFunction);
              $(add_li).click(function(e) {
                e.preventDefault();
                var itemId=getActiveRecordId(itemsSelector);
                console.log('link clicked ' + itemId);
                callbackFunction(itemId, data);
              });
            }
          }
        });
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
            confirmButtonText: "Yes, Delete!", closeOnConfirm: false, showLoaderOnConfirm: true
          }, function () {
              $.fn.ajaxDelete({url:deleteUrl + "/" + itemId});
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