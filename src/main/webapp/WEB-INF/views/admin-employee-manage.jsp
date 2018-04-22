<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>Softvision | PMS</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/favicon.ico">
    <!-- Bootstrap Core Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/css/bootstrap.css">
    <!-- Waves Effect Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.css"/>
    <!-- Bootstrap Material Datetime Picker Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css" />
    <!-- Animation Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/animate-css/animate.css"/>
    <!-- Sweetalert Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.css"/>
    <!-- JQuery DataTable Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/css/dataTables.bootstrap.css"/>
    <!-- Custom Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/style.css">
    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/css/themes/all-themes.css">
     <!-- Bootstrap Select Css -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/css/bootstrap-select.css"  />
    <!-- EasyAutocomplete-1.3.5 -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/easy-autocomplete.min.css"/>
    
  </head>
  <style>
  .list-group-item {
  	font-size: 13px;
  }
  </style>
</head>

<body class="theme-red">
  <!-- Header -->
  <%@include file="common/header.jsp" %>
  <!-- =============================================== -->

  <!-- Left side column. contains the sidebar -->
  <%@include file="common/menu.jsp" %>
  <!-- =============================================== -->

  <section class="content">
    <div class="container-fluid">
      <div class="block-header">
        <h2>Employee Management
          <small>Add, Lookup and Sync employees from SV Project, view and modify roles</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
              <!-- Nav tabs -->
              <ul class="nav nav-tabs tab-nav-right tab-col-orange" role="tablist">
                <li role="presentation" class="active"><a href="#Lookup_Employees" data-toggle="tab">LOOKUP</a></li>
                <li role="presentation"><a href="#Sync_Employees" data-toggle="tab">Sync Employees</a></li>
                <li role="presentation"><a href="#Roles" data-toggle="tab">Roles</a></li>
                <li role="presentation"><a href="#Change_Roles" data-toggle="tab">Changes Roles</a></li>
              </ul>
               <!-- Tab panes -->
               <div class="tab-content">
                 <div role="tabpanel" class="tab-pane fade in active" id="Lookup_Employees">
                   <div class="row clearfix">
                     <div class="col-sm-2">&nbsp;</div>
                     <div class="col-sm-6">
                       <div class="form-group">
                         <div class="form-line">
                           <input type="text" id="SearchBy" class="form-control" placeholder="Enter Employee # or Name" autofocus />
                         </div>
                         <div class="help-info">Min. 3 characters</div>
                       </div>
                     </div>
                     <div class="col-sm-2">
                       <button type="button" id="Search" class="btn bg-orange btn-circle waves-effect waves-circle waves-float">
                         <i class="material-icons">search</i>
                       </button>
                     </div>
                     <div class="col-sm-2">&nbsp;</div>
                   </div>
                   <div class="row clearfix">
                     <div class="table-responsive">
                       <table id="SearchTable" class="table table-bordered table-striped table-hover dataTable">
                         <thead>
                           <tr>
                             <th>#</th>
                             <th>First Name</th>
                             <th>Last Name</th>
                             <th>Employment Type</th>
                             <th>Band</th>
                             <th>Designation</th>
                             <th>Hired On</th>
                             <th>Location</th>
                           </tr>
                         </thead>
                         <tbody>
                         </tbody>
                       </table>
                     </div>
                   </div>
                 </div>
                 <div role="tabpanel" class="tab-pane fade" id="Sync_Employees">
                   <p>This features can be used to Sync all the employee information from SV project to this system. After Sync, new employees will be added to system and existing employees data will be updated.</p>
                   <div class="row clearfix">
                   	 <div class="col-lg-4">&nbsp;</div>
                   	 <div class="col-lg-4" style="margin-top: 20px;">
                   	 	<button type="button" id="Sync" class="btn btn-block btn-lg btn-primary waves-effect">Sync Employee Data</button>
                   	 </div>
                     <div class="col-lg-4">&nbsp;</div>
                   </div>
                   <div class="row clearfix sync_result_row">
					 <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                   	 	<button id="Show_Sync_Results_Success" class="btn btn-success waves-effect" type="button">Successful<span class="badge"></span></button>
                   	 </div>
					 <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                   	 	<button id="Show_Sync_Results_Error" class="btn btn-warning waves-effect pull-right" type="button">Errors<span class="badge"></span></button>
                   	 </div>
                   </div>
                   <div class="row clearfix sync_result_row">
					 <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                   	 	<ul class="list-group" id="Sync_Results_Success" style="display: none;">
		                </ul>
		                <ul class="list-group" id="Sync_Results_Error" style="display: none;">
		                </ul>
                   	 </div>
                   </div>
                 </div>
                 <div role="tabpanel" class="tab-pane fade" id="Roles">
                 	<div class="body">
	                   <div class="row clearfix">
	                     <div class="col-sm-2">&nbsp;</div>
	                     <div class="col-sm-6">
							 <select id="SearchRoleBy" class="form-control show-tick">
	                        	<option value="-1"> Please select </option>
	                            <option value="Admin">Admin</option>
	                            <option value="Manager">Manager</option>
	                         </select> 
	                      </div>
	                     <div class="col-sm-2">
	                       <button type="button" id="SearchRole" class="btn bg-orange btn-circle waves-effect waves-circle waves-float">
	                         <i class="material-icons">search</i>
	                       </button>
	                     </div>
	                     <div class="col-sm-2">&nbsp;</div>
	                   </div>
                   </div>
                   <div class="row clearfix">
                     <div class="table-responsive">
                       <table id="SearchRoleTable" class="table table-bordered table-striped table-hover dataTable">
                         <thead>
                           <tr>
                             <th>#</th>
                             <th>First Name</th>
                             <th>Last Name</th>
                             <th>Band</th>
                             <th>Designation</th>
                             <th>Location</th>
                           </tr>
                         </thead>
                         <tbody>
                         </tbody>
                       </table>
                     </div>
                   </div>
                 </div>
                 
                 <div role="tabpanel" class="tab-pane fade" id="Change_Roles">
                   <div class="row clearfix">
					    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
		                  <label for="Search_Employees">Select Employees</label>
		                </div>
		                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
				          <div class="form-group form-float">
				            <div class="form-line">
				              <input type="text" id="Search_Employees" class="form-control" placeholder="Employee # or Name" value="">
		                    </div>
		                  </div>
		                </div>
		            </div>
		            <div class="row clearfix">
				        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"  id="Employee_Data">
				        </div>
				    </div>
                 </div>
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <!-- Jquery Core Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery/jquery.min.js"></script>
  <!-- Bootstrap Core Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap/js/bootstrap.js"></script>
  <!-- Select Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-select/js/bootstrap-select.js"></script>
  <!-- Slimscroll Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
  <!-- Jquery Validation Plugin Css -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-validation/jquery.validate.js"></script>
  <!-- JQuery Steps Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-steps/jquery.steps.js"></script>
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
  <!-- Waves Effect Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
  <!-- Jquery DataTable Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
  <%-- <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/skin/bootstrap/js/dataTables.bootstrap.js"></script> --%>
  <!-- Autosize Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
  <!-- Moment Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/momentjs/moment.js"></script>
  <!-- Bootstrap Material Datetime Picker Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/form-validator.js"></script>
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-Sprite-Preloaders/jquery.preloaders.min.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/render-card.js"></script>
  <!-- EasyAutocomplete-1.3.5 -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/jquery.easy-autocomplete.min.js"></script>
  

  <script>
  var availableRoles=[];
  $(function () {
	  $('a[data-toggle="tab"]').on( 'shown.bs.tab', function (e) {
	        // var target = $(e.target).attr("href"); // activated tab
	        // alert (target);
	        $($.fn.dataTable.tables( true ) ).css('width', '100%');
	        $($.fn.dataTable.tables( true ) ).DataTable().columns.adjust().draw();
	        $('.easy-autocomplete').css('width', '100%');
	    } );
	var searchText='qqqq';
	$('#SearchTable').DataTable({
        responsive: true,
        paging: false,
		searching: false,
		ordering: true,
		info: true,
        "ajax": "<%=request.getContextPath()%>/employee/search/" + searchText,
        "sAjaxDataProp":"",
		"columns": [
            { "data": "employeeId" },
            { "data": "firstName" },
            { "data": "lastName" },
            { "data": "employmentType" },
            { "data": "band" },
            { "data": "designation" },
            { "data": "hiredOn" },
            { "data": "location" }
        ],
    });
	
	$('#SearchRoleTable').DataTable({
        responsive: true,
        paging: false,
		searching: false,
		ordering: true,
		info: true,
        "ajax": "<%=request.getContextPath()%>/employee/role-search/" + searchText,
        "sAjaxDataProp":"",
		"columns": [
            { "data": "employeeId" },
            { "data": "firstName" },
            { "data": "lastName" },
            { "data": "band" },
            { "data": "designation" },
            { "data": "location" }
        ],
    });

    $('#Search').click(function() {
      var searchBy=$('#SearchBy');
      searchText=$.trim(searchBy.val());
      if (searchText.length < 3) {
    	$(searchText).focus();
      } else {
    	var table = $('#SearchTable').DataTable();
    	//table.ajax.reload();
    	table.ajax.url("<%=request.getContextPath()%>/employee/search/" + searchText).load();
      }
    });
    $('#SearchRole').click(function() {
        var searchRoleBy=$('#SearchRoleBy');
        searchText=$.trim(searchRoleBy.val());
        if (searchText ==-1) {
      	$(searchText).focus();
        } else {
      	var table = $('#SearchRoleTable').DataTable();
      	//table.ajax.reload();
      	table.ajax.url("<%=request.getContextPath()%>/employee/role-search/" + searchText).load();
        }
      });
    
    $("#Search_Employees").easyAutocomplete({
    	url: function(phrase) {
    	  return "<%=request.getContextPath()%>/employee/search/" + phrase;
    	},
    	dataType: "json",
    	getValue: function (item) { return item.fullName},
    	template: {
    	  type: "custom",
    	  method: function(value, item) {
    		return "<b>" + item.fullName + "</b> [<code>" + item.employeeId + "</code>]</small>";
    	  }
    	},
    	list: {
    	  maxNumberOfElements: 15,
    	  onClickEvent: function() {
    		var data=$("#Search_Employees").getSelectedItemData();
    		var itemId=data.employeeId;
    		var itemValue=data.fullName;
    		availableRoles=[];
    		if(!$('#Search_Employees_Table').length){
    			var detailTable=$('<table class="table table-striped" id="Search_Employees_Table">');
	    	  	var detailTbody=$('<tbody>');
	     		var detailTfoot=$('<tfoot>');
	     		var detailThead=$('<thead>')
	    	  	$(detailTable).append(detailTbody);
	    	  	$(detailTable).append(detailTfoot);
	    	  	$(detailTable).append(detailThead);
	     		$('#Employee_Data').append(detailTable);
	     		$(detailThead).append('<tr><th>id</th><th>Name</th><th>Manager?</th></tr>');
	     		$(detailTbody).append('<tr id="emp_row_' + data.employeeId + '"><td>' + data.employeeId + '</td><td>' + itemValue + '</td><td><div class="switch pull-left"><label><input type="checkbox" id="cb_' + data.employeeId + '"><span class="lever switch-col-green"></span></label></div></td></tr>');
	     		
	     		detailTfoot.append('<tr><td class="col-md-12" colspan="2"><button type="button" class="btn bg-light-blue waves-effect pull-left" id="clear">clear</button></td><td class="col-md-12"><button type="button" class="btn bg-light-blue waves-effect pull-right" id="save">Save</button></td></tr>')
	    		$("#Search_Employees").val('');
	     		
	     		$('#save').on('click',function(event) {
					var employeeList = [];
					$('tr[id^="emp_row_"]').each(function( index ) {
					 var employeeId= $( this ).find("td:first").text();
					 var isManager=$(this).find('input[type="checkbox"]').is(':checked');
					 employeeList.push({id:employeeId,managerFlag:isManager}); 
					});
       			 	var apiUrl='<%=request.getContextPath()%>/role/changeManagerRole';
       			 	$.fn.ajaxPost({url:apiUrl, data: employeeList,refresh:'no'});
				});
	     		$('#clear').on('click',function(event) {
	     			$('#Employee_Data').empty();
	     		});
    		} else{
	    		if(!$('#emp_row_'+ data.employeeId).length){
	    			var detailTbody = $('#Search_Employees_Table > tbody');
	    			$(detailTbody).append('<tr id="emp_row_' + data.employeeId + '"><td>' + data.employeeId + '</td><td>' + itemValue + '</td><td><div class="switch pull-left"><label><input type="checkbox" id="cb_' + data.employeeId + '"><span class="lever switch-col-green"></span></label></div></td></tr>');
	    		}
	    		$("#Search_Employees").val('');
    		}

    		$.fn.ajaxGet({
    			url: '<%=request.getContextPath()%>/role/byEmployee/'+itemId,
    			onSuccess: function(result) {
    				  $(result).each(function(index, role) {
    					  availableRoles.push(role.RoleName);
    				  });
    				  if(availableRoles.indexOf('Manager')>-1){
    				    $('#emp_row_'+itemId).find('input[type="checkbox"]').attr('checked', 'true');
    				  }
   				},
    			onError: function(message, content) {
    			}
    		});

    	  }	
    	}
      });

    $('.sync_result_row').hide();

    $('#Sync').click(function() {
      //var button=this;
	  swal({
    	title: "Are you sure?", text: "Do you want to sync data of all employees from SV Project to this system? This may take few minutes!!", type: "warning",
    	showCancelButton: true, confirmButtonColor: "#DD6B55",
    	confirmButtonText: "Yes, Proceed!", closeOnConfirm: false, showLoaderOnConfirm: true
      }, function () {
	    //$(button).attr('disabled', true);
	    $('.sync_result_row').hide();
    	$.fn.ajaxPut({
    	  url: '<%=request.getContextPath()%>/employee/sync',
    	  //refresh: "no",
    	  onSuccess: function (message, result) {
    		var results_container_error=$('#Sync_Results_Error');
    		var results_container_Success=$('#Sync_Results_Success');
      		$(results_container_error).empty();
      		$(results_container_Success).empty();
      		$("#sync_employee_tab").show();
      		$(result).each(function(index, aResult) {
      		  if (aResult.code=='SUCCESS') {
      		    $(results_container_Success).append('<li class="list-group-item bg-green">' + aResult.message + '<span class="pull-right"><i class="material-icons">done</i></span></li>');
      		  } else {
      			$(results_container_error).append('<li class="list-group-item bg-red">' + aResult.message + '<span class="pull-right"><i class="material-icons">error_outline</i></span></li>');
      		  }
      		});
       	  	swal({ title: "Sync Completed!", text: "", type: "success"}, function () { }); 
    	  },
    	  onComplete : function () {
    		//$(button).attr('disabled', false);
    		$('.sync_result_row').show();
    		$('#Show_Sync_Results_Error').find('.badge').text($('#Sync_Results_Error').find('li').length);
    		$('#Show_Sync_Results_Success').find('.badge').text($('#Sync_Results_Success').find('li').length);
	      }
    	});
      });
    });

    $('#Show_Sync_Results_Success').click(function() {
    	$('#Sync_Results_Success').show();
    	$('#Sync_Results_Error').hide();
    });
    $('#Show_Sync_Results_Error').click(function() {
    	$('#Sync_Results_Success').hide();
    	$('#Sync_Results_Error').show();
    });
  });
  </script>
</body>
</html>