<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module platform
    import js.system.engineServerManagement
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<link rel="stylesheet" type="text/css" href="/css/engineServerStyle.css" />
<%-- <script type="text/javascript" src="/js/system/engineServerManagement.js"></script> --%>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>

<style type="text/css">
.statusTable th{
	height:23px;
	width:70px;
	text-align:center;
	background:#f0f1f3;
	color:#00156e;
}

.statusTable td{
	height: 26px;
	width: 70px;
	text-align:center;
}

body,html{ height:100%;}
.x-panel-body{ background:#fbfbfb;}
.x-grid-group-title .nm3kTip{ position:relative; top:3px;}

</style>

</head>

<body>
	<div id="form_div" class="eSM_sidePart">
		<p class="pannelTit" id="blueHeader">@engine.status@</p>
		<div class="eSM_subSide" id="eSM_subSide">
			<div class="pT10">
				<table cellpadding="0" border="1" borderColor="#d0d0d0" class="statusTable" rules="all" id="statusTable" style=" margin:0 auto;">
					<tr>
						<th>@engine.name@</th>
						<th>@engine.runTime@</th>
						<th>@engine.memUsage@</th>
						<th>@engine.threadNum@</th>
					</tr>
				</table>
			</div>
		</div>
		<div id="loading_div" style="display: none;">
				<img src="/images/blue_loading.gif" alt="" /> 
				<span>@ftp.loadingStatus@</span>
		</div>
	</div>
</body>
</Zeta:HTML>