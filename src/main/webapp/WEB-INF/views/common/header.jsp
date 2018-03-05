<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus" %>
<%@ page import="com.softvision.ipm.pms.assign.constant.AssignmentPhaseStatus" %>

<c:set var="AppraisalCycleStatus_DRAFT" value="<%=AppraisalCycleStatus.DRAFT%>"/>
<c:set var="AppraisalCycleStatus_ACTIVE" value="<%=AppraisalCycleStatus.ACTIVE%>"/>
<c:set var="AppraisalCycleStatus_COMPLETE" value="<%=AppraisalCycleStatus.COMPLETE%>"/>

<style>
</style>
<!-- Page Loader -->
<div class="page-loader-wrapper">
    <div class="loader">
        <div class="preloader">
            <div class="spinner-layer pl-red">
                <div class="circle-clipper left">
                    <div class="circle"></div>
                </div>
                <div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
            </div>
        </div>
        <p>Please wait...</p>
    </div>
</div>
<!-- #END# Page Loader -->

<!-- Top Bar -->
<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="javascript:void(0);" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
            <a href="javascript:void(0);" class="bars"></a>
            <a class="navbar-brand" href="<%=request.getContextPath()%>/dashboard">SOFTVISION</a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <!-- Call Search -->
                <li><a href="<%=request.getContextPath()%>/logout" title="Sign Out"><i class="material-icons">input</i></a></li>
                <!-- #END# Call Search -->
                <li class="pull-right"><a href="javascript:void(0);" class="js-right-sidebar" data-close="true"><i class="material-icons">more_vert</i></a></li>
            </ul>
        </div>
    </div>
</nav>

    