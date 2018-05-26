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
        <h2>Change Role(s)
          <small>Add MANAGER role or ADMIN role to employee(s); Remove MANAGER role or ADMIN role from employee(s);</small>
        </h2>
      </div>
      <div class="row clearfix">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
          <div class="card">
            <div class="body">
              <div class="row clearfix">
                <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 form-control-label">
                  <label for="Search_Employees">Select Employees</label>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
		          <div class="form-group form-float">
		            <div class="form-line">
		              <input type="text" id="Search_Employees" class="form-control" placeholder="Employee ID or Name" value="" autofocus>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row clearfix">
		        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		          <table class="table table-striped" id="Search_Employees_Table">
		            <thead>
		              <tr>
		                <th>Employee #</th>
		                <th>Employee Name</th>
		                <th>Is Manager?</th>
		              </tr>
		            </thead>
		            <tbody>
		            </tbody>
		          </table>
		        </div>
		      </div>
              <div class="row clearfix">
                <button id="UpdateRoles" class="btn btn-primary waves-effect pull-right m-r-20">
                  <i class="material-icons">mode_edit</i>
                  <span>Update</span>
                </button>
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
  <!-- Slimscroll Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
  <!-- Sweet Alert Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/sweetalert/sweetalert.min.js"></script>
  <!-- Waves Effect Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/node-waves/waves.js"></script>
  <!-- Jquery DataTable Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/jquery-datatable/jquery.dataTables.js"></script>
  <!-- Autosize Plugin Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/autosize/autosize.js"></script>
  <!-- Custom Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/admin.js"></script>
  <!-- Demo Js -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/js/demo.js"></script>
  
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/common.js"></script>
  <script src="<%=request.getContextPath()%>/scripts/AdminBSBMaterialDesign/ajax-wrapper.js"></script>
  <!-- EasyAutocomplete-1.3.5 -->
  <script src="<%=request.getContextPath()%>/AdminBSBMaterialDesign/plugins/EasyAutocomplete-1.3.5/jquery.easy-autocomplete.min.js"></script>
  <script>
  var availableRoles=[];
  $(function () {
    var phrase='qqqq';
	$('#UpdateRoles').hide();

    $("#Search_Employees").easyAutocomplete({
      url: function(phrase) {
        return "<%=request.getContextPath()%>/api/employee/search/" + phrase;
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

          var exists=false;
          var rows=$('#Search_Employees_Table > tbody > tr');
          for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
        	  var employeeId= $(rows[rowIndex]).find("td:first").text();
              if (employeeId == itemId) {
            	  exists=true;
            	  break;
              }
		  }
          if (!exists) {
            var row=$('<tr>');
            $(row).append('<td>' + data.employeeId + '</td>');
            $(row).append('<td>' + data.fullName + '</td>');

            var switchCol=$('<td>');
            var switchDiv=$('<div class="switch pull-left">');
            var label=$('<label>');
            var checkbox='<input type="checkbox"><span class="lever switch-col-green"></span></input>';

            $(row).append(switchCol);
            $(switchCol).append(switchDiv);
            $(switchDiv).append(label);
            $(label).append(checkbox);
            $('#Search_Employees_Table > tbody').append(row);

            $.fn.ajaxGet({
              url: '<%=request.getContextPath()%>/api/role/byEmployee/' + itemId,
              onSuccess: function(result) {
                $(result).each(function(index, role) {
                  availableRoles.push(role.RoleName);
                });

                if (availableRoles.indexOf('Manager') > -1) {
                  $(switchDiv).find('input[type=checkbox]').attr('checked', 'true');
                }
              },
              onError: function(message, content) { }
            });
            $('#UpdateRoles').show();
          }
          $("#Search_Employees").val('');
        }
      }
    });

    $('#UpdateRoles').on('click',function(event) {
      var employeeList = [];
      $('#Search_Employees_Table > tbody > tr').each(function( index ) {
        var employeeId= $(this).find("td:first").text();
        var isManager=$(this).find('input[type="checkbox"]').is(':checked');
        employeeList.push({id:employeeId,managerFlag:isManager}); 
      });

      var apiUrl='<%=request.getContextPath()%>/api/role/changeManagerRole';
      $.fn.ajaxPost({url:apiUrl, data: employeeList,refresh:'no'});
    });
  });
  </script>
</body>
</html>