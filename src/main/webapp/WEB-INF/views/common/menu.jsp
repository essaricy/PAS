<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- #Top Bar -->
<section>
  <sec:authentication property="details.imageUrl" var="imageUrl"/>
  <sec:authentication property="details.employeeId" var="employeeId" />
  <sec:authentication property="name" var="userName" />
  <sec:authentication property="details.band" var="band" />
  <sec:authentication property="details.location" var="location" />
  <sec:authentication property="details.joinedDate" var="joinedDate" />
  

  <!-- Left Sidebar -->
  <aside id="leftsidebar" class="sidebar">
    <!-- User Info -->
    <div class="user-info">
      <div class="image">
        <c:if test="${empty imageUrl}">
          <img src="<%=request.getContextPath()%>/images/user.png" width="48" height="48" alt="${userName}" />
        </c:if>
        <c:if test="${not empty var1}">
          <img src="${imageUrl}" width="48" height="48" alt="${userName}" />
        </c:if>
      </div>
      <div class="info-container">
        <div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${userName}</div>
        <div class="email">${designation}</div>
        <div class="btn-group user-helper-dropdown">
          <i class="material-icons" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">keyboard_arrow_down</i>
          <ul class="dropdown-menu pull-right">
            <li><a href="javascript:void(0);"><i class="material-icons">person</i>Profile</a></li>
            <li role="seperator" class="divider"></li>
            <li><a href="<%=request.getContextPath()%>/logout"><i class="material-icons">input</i>Sign Out</a></li>
          </ul>
        </div>
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
              <a href="<%=request.getContextPath()%>/admin/template/assign">Assign Templates</a>
            </li>
          </ul>
        </li>
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">flag</i>
            <span>Appraisal</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/admin/cycles/list">My Appraisal</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/competency/list">Perform Assessment</a>
            </li>
          </ul>
        </li>
        <li>
          <a href="javascript:void(0);" class="menu-toggle">
            <i class="material-icons">settings</i>
            <span>Admin</span>
          </a>
          <ul class="ml-menu">
            <li>
              <a href="<%=request.getContextPath()%>/admin/cycles/list">Manage Appraisal Cycles</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/goal/list">Manage Competency Assessments</a>
            </li>
            <li>
              <a href="<%=request.getContextPath()%>/admin/employee/manage">Employee Management</a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <!-- #Menu -->
    <!-- Footer -->
    <div class="legal">
      <div class="copyright">
        Designed Using <a href="javascript:void(0);">AdminBSB - Material Design</a>.
      </div>
      <div class="version">
        <b>Version: </b> 0.A
      </div>
    </div>
    <!-- #Footer -->
  </aside>
  <!-- #END# Left Sidebar -->
  <!-- Right Sidebar -->
  <aside id="rightsidebar" class="right-sidebar">
    <ul class="nav nav-tabs tab-nav-right" role="tablist">
      <li role="presentation" class="active"><a href="#skins" data-toggle="tab">SKINS</a></li>
    </ul>
    <div class="tab-content">
      <div role="tabpanel" class="tab-pane fade in active in active" id="skins">
        <ul class="demo-choose-skin">
          <li data-theme="red" class="active">
                        <div class="red"></div>
                        <span>Red</span>
                    </li>
                    <li data-theme="pink">
                        <div class="pink"></div>
                        <span>Pink</span>
                    </li>
                    <li data-theme="purple">
                        <div class="purple"></div>
                        <span>Purple</span>
                    </li>
                    <li data-theme="deep-purple">
                        <div class="deep-purple"></div>
                        <span>Deep Purple</span>
                    </li>
                    <li data-theme="indigo">
                        <div class="indigo"></div>
                        <span>Indigo</span>
                    </li>
                    <li data-theme="blue">
                        <div class="blue"></div>
                        <span>Blue</span>
                    </li>
                    <li data-theme="light-blue">
                        <div class="light-blue"></div>
                        <span>Light Blue</span>
                    </li>
                    <li data-theme="cyan">
                        <div class="cyan"></div>
                        <span>Cyan</span>
                    </li>
                    <li data-theme="teal">
                        <div class="teal"></div>
                        <span>Teal</span>
                    </li>
                    <li data-theme="green">
                        <div class="green"></div>
                        <span>Green</span>
                    </li>
                    <li data-theme="light-green">
                        <div class="light-green"></div>
                        <span>Light Green</span>
                    </li>
                    <li data-theme="lime">
                        <div class="lime"></div>
                        <span>Lime</span>
                    </li>
                    <li data-theme="yellow">
                        <div class="yellow"></div>
                        <span>Yellow</span>
                    </li>
                    <li data-theme="amber">
                        <div class="amber"></div>
                        <span>Amber</span>
                    </li>
                    <li data-theme="orange">
                        <div class="orange"></div>
                        <span>Orange</span>
                    </li>
                    <li data-theme="deep-orange">
                        <div class="deep-orange"></div>
                        <span>Deep Orange</span>
                    </li>
                    <li data-theme="brown">
                        <div class="brown"></div>
                        <span>Brown</span>
                    </li>
                    <li data-theme="grey">
                        <div class="grey"></div>
                        <span>Grey</span>
                    </li>
                    <li data-theme="blue-grey">
                        <div class="blue-grey"></div>
                        <span>Blue Grey</span>
                    </li>
                    <li data-theme="black">
                        <div class="black"></div>
                        <span>Black</span>
                    </li>
                </ul>
            </div>
        </div>
    </aside>
    <!-- #END# Right Sidebar -->
</section>
<script>
</script>