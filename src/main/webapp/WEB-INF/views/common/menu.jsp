<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!-- #Top Bar -->
<style>
.cell img {
	float: left;
    border-top-left-radius:5px 5px;
    border-top-right-radius:5px 5px;
    position: static;
    border-radius:  50%;
    width: 40px;
    height: 40px;
}

.cell .text{
    height: 100%;
    float: left;
    top: 0;
    box-sizing:border-box;
    -moz-box-sizing:border-box;
    -webkit-box-sizing:border-box;
    padding: 8px;
    text-align: left;
}
</style>
<section>
  <sec:authentication property="details.imageUrl" var="imageUrl"/>
  <sec:authentication property="details.employeeId" var="employeeId" />
  <sec:authentication property="name" var="userName" />
  <sec:authentication property="details.band" var="band" />
  <sec:authentication property="details.designation" var="designation" />
  <sec:authentication property="details.location" var="location" />
  <sec:authentication property="details.joinedDate" var="joinedDate" />
  <sec:authentication property="authorities" var="privilige" />
  

  <!-- Left Sidebar -->
  <aside id="leftsidebar" class="sidebar">
    <!-- User Info -->
    <div class="user-info">
      <div class="image">
        <img src="${imageUrl}" width="48" height="48" alt="${userName}" />
      </div>
      <div class="info-container">
        <div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${userName}</div>
        <div class="email">${designation}</div>
      </div>
    </div>
    <!-- #User Info -->
    <!-- Menu -->
    <div class="menu">
      <ul class="list">
        <li class="header">MAIN NAVIGATION</li>
        <li class="active">
          <a href="<%=request.getContextPath()%>/dashboard">
            <i class="material-icons">dashboard</i>
            <span>Dashboard</span>
          </a>
        </li>
        <sec:authorize access="hasAuthority('MANAGER')">
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">album</i>
            <span>Goal Setting</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/admin/template/list">Template Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/template/assign">Template Assignment</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/assignment/list">Manage Assignments</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/reports">Reports</a>
            </li>
          </ul>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAuthority('EMPLOYEE')">
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">flag</i>
            <span>Appraisal</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/employee/assignment/active">My Appraisals</a>
            </li>
          </ul>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAuthority('ADMIN')">
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">settings</i>
            <span>Admin</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/admin/cycles/list">Cycle Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/goal/list">Goal Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/template/list">Template Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/employee/mgmt">Employee Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/reports">Reports</a>
            </li>
          </ul>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAuthority('SUPPORT')">
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">build</i>
            <span>Support</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/support/sessions/active">Active Sessions</a>
            </li>
            <%-- <li>
              <a href="<%=request.getContextPath()%>/support/cache/objects">Cached Objects</a>
            </li> --%>
          </ul>
        </li>
        </sec:authorize>
      </ul>
    </div>
    <!-- #Menu -->
  </aside>
  <!-- #END# Left Sidebar -->
  <!-- Right Sidebar -->
  <aside id="rightsidebar" class="right-sidebar">
    <ul class="nav nav-tabs tab-nav-right" role="tablist">
      <li role="presentation" class="active"><a href="#settings" data-toggle="tab">About</a></li>
    </ul>
    <div class="tab-content">
      <div role="tabpanel" class="tab-pane active" id="settings">
        <div class="demo-settings">
          <div class="logo">
       		<img src="<%=request.getContextPath()%>/images/logo.png"  style="float: left;" width="82px" />
       		<a href="javascript:void(0);"><b>SOFTVISION</b></a><br/>
    	    <small>PMS - Performance Management System</small>
	      </div>
	      <div style="clear: both;"></div>
          <ul class="setting-list">
            <li>
              <span>
                This product is a collaborative effort of HR-Team and Java Community under the leadership of 
                <label>Nithya Somaiah</label> and 
                <label>Naveed A Hagalwadi</label> of Mysore Studio, Softvision.
              </span>
            </li>
	      </ul>
		  <p style="clear: both;">Designed, Arch and Developed By</p>
		  <ul class="setting-list">
			<li>
			  <div class="span8 cell">
			    <img src="https://opera.softvision.com/Content/Core/img/Profile/1136.jpg">
			    <div class="text">Srikanth Ragi</div>
			  </div>
      	    </li>
		  </ul>
		  <p style="clear: both; margin-top: 50px;">Full Stack Developers</p>
		  <ul class="setting-list">
			<li style="clear: both;">
			  <div class="span8 cell">
			    <img src="https://opera.softvision.com/Content/Core/img/Profile/2388.jpg">
			    <div class="text">Mallikarjun Gongati</div>
			  </div>
      	    </li>
      		<li style="clear: both;">
			  <div class="span8 cell">
			    <img src="https://opera.softvision.com/Content/Core/img/Profile/2006.jpg">
			    <div class="text">Rohit Ramesh</div>
			  </div>
      	    </li>
		  </ul>
		  <p style="clear: both; margin-top: 50px;">Designed Using</p>
		  <ul class="setting-list">
			<li><span>AdminBSB - Material Design</span></li>
		  </ul>
		  <p>Version</p>
		  <ul class="setting-list">
			<li><span>2.12</span></li>
		  </ul>
		</div>
      </div>
	</div>
  </aside>
  <!-- #END# Right Sidebar -->
</section>
<script>
</script>