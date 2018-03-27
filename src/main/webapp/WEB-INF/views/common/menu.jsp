<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!-- #Top Bar -->
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
        <%-- <c:if test="${empty imageUrl}">
          <img src="<%=request.getContextPath()%>/images/user.png" width="48" height="48" alt="${userName}" />
        </c:if>
        <c:if test="${not empty imageUrl}">
          <img src="${imageUrl}" width="48" height="48" alt="${userName}" />
        </c:if> --%>
      </div>
      <div class="info-container">
        <div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${userName}</div>
        <div class="email">${designation}</div>
        <%-- <div class="btn-group user-helper-dropdown">
          <i class="material-icons" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">keyboard_arrow_down</i>
          <ul class="dropdown-menu pull-right">
            <li><a href="javascript:void(0);"><i class="material-icons">person</i>Profile</a></li>
            <li role="seperator" class="divider"></li>
            <li><a href="<%=request.getContextPath()%>/logout"><i class="material-icons">input</i>Sign Out</a></li>
          </ul>
        </div> --%>
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
              <a href="<%=request.getContextPath()%>/admin/template/assign">Template Assignment</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/assignment/list">Manage Assignments</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/report/phase-score">Phase Score Report</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/manager/report/cycle-score">Cycle Score Report</a>
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
              <a href="<%=request.getContextPath()%>/employee/assignment/list">My Appraisals</a>
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
              <a href="<%=request.getContextPath()%>/admin/cycles/list">Appraisal Cycles</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/goal/list">Goal Settings</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/employee/manage">Employee Management</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/appraisal/status">Appraisal Status Report</a>
            </li>
          </ul>
        </li>
        </sec:authorize>
      </ul>
    </div>
    <!-- #Menu -->
    <!-- Footer -->
<!--     <div class="legal">
      <div class="copyright">
        Designed Using <a href="javascript:void(0);">AdminBSB - Material Design</a>.
      </div>
      <div class="version">
        <b>Version: </b> 0.A
      </div>
    </div>
 -->    <!-- #Footer -->
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
          <ul class="setting-list">
            <li>
              <span>
                This product is a collaborative effort of HR-Ops and Java Community under the leadership of 
                <label>Nithya Somaiah</label> and 
                <label>Naveed A Hagalwadi</label> of Mysore Studio, Softvision.
              </span>
            </li>
	      </ul>
		  <p>Designed, Arch and Developed By</p>
		  <ul class="setting-list">
			<li><span>Srikanth Ragi</span></li>
		  </ul>
		  <p>Full Stack Developers</p>
		  <ul class="setting-list">
			<li><span>Mallikarjun Gongati</span></li>
			<li><span>Rohit Ramesh</span></li>
		  </ul>
		  <p>Designed Using</p>
		  <ul class="setting-list">
			<li><span>AdminBSB - Material Design</span></li>
		  </ul>
		  <p>Version</p>
		  <ul class="setting-list">
			<li><span>0.C</span></li>
		  </ul>
		</div>
      </div>
	</div>
  </aside>
  <!-- #END# Right Sidebar -->
</section>
<script>
</script>