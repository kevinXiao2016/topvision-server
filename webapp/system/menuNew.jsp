<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css" />

<style type="text/css">
#tree a, #tree span.folder{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.openIcon" />.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.closeIcon" />.png) no-repeat;}
</style>

<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var userId = <%=uc.getUser().getUserId()%>;
var roleManagement = <%=uc.hasPower("roleManagement")%>;
var userManagement = <%=uc.hasPower("userManagement")%>;
var logManagement = <%=uc.hasPower("logManagement")%>;

var departmentManagement = <%=uc.hasPower("departmentManagement")%>;
var postManagement = <%=uc.hasPower("postManagement")%>;
function newRole() {
	window.top.createDialog("modalDlg", I18N.SYSTEM.newRole, 600, 370, 
		"system/showNewRole.tv?superiorId=0", null, true, true);
}
function showUserView() {
    window.parent.addView("userList",  I18N.SYSTEM.userManagement, "tabIcoH3", "system/showUserList.tv");
}
function showUserGroupView() {
    window.parent.addView("userGroupList",  I18N.SYSTEM.userGroupManagement, "userIcon", "system/userGroupList.jsp");
}
function showRoleView() {
    window.parent.addView("roleList", I18N.SYSTEM.roleManagement, "tabIcoH2", "system/showPowerForRole.tv");
}
function showGmView() {
    window.parent.addView("gmview",  I18N.COMMON.gmView, "tabIcoH1", "system/gmView.jsp");
}
function showLogView() {
    window.parent.addView("logview", I18N.MODULE.logManagement, "tabIcoH4", "system/showLogList.tv");
}
function showEnginesView() {
    window.parent.addView("enginesview", I18N.SYSTEM.engineManagement, "engineIcon", "system/engineList.jsp");
}
function showKnowledgeBase() {
    window.parent.addView("knowledgeBase",  I18N.SYSTEM.knowledgeBase, "knowledgeBaseIcon", "system/repository.jsp");
}
function newUser() {
    window.parent.createDialog("modalDlg",  I18N.SYSTEM.newUser, 800, 500, "system/showNewUser.tv", null, true, true);
}
function showDepartmentManager() {
    window.parent.addView("departmentList",  I18N.MODULE.departmentManagement, "tabIcoH6", "system/departmentList.jsp");
}
function showPostManager() {
    window.parent.addView("postList", I18N.MODULE.postManagement, "tabIcoH7", "system/postList.jsp");
}
function showCmServiceType() {
	window.parent.addView("cmServiceType", I18N.SYSTEM.cmServiceType, "tabIcoH7", "cm/showCmServiceType.tv");
}

function showTagManagementView() {
	window.parent.addView("tagManagementView",  I18N.SYSTEM.tagManagement, "tabIcoH7", "onu/showTagManagementView.tv");
}

function showResourceCategory() {
    window.parent.addView("resourceCategory",  I18N.SYSTEM.resourceClassification, "postIcon", "template/showResourceCategory.tv");
}

function showExpander(view, expander) {
    var el = Zeta$(view);
    var visible = (el.style.display == "");
    visible = !visible;
    setExpanderVisible(view, expander, visible);
    setCookieValue(userName + expander, visible);
}
function setExpanderVisible(view, expander, visible) {
    var o = Zeta$(expander);
    o.className = visible ? "NAVI_EXPANDER_UP" : "NAVI_EXPANDER_DOWN";
    o.title = visible ?  I18N.MAIN.collapseTitle :  I18N.MAIN.expandTitle;
    Zeta$(view).style.display = visible ? "" : "none";
}
/* cookie */
function getCookieValue(name, defaultValue) {
    return getCookie(name, defaultValue);
}
function setCookieValue(name, value) {
    setCookie(name, value);
}
function clearCookieValue(name) {
    delCookie(name);
}
function showOperationLogView() {
	window.parent.addView("operationView",  I18N.SYSTEM.configLog, "tabIcoH5", "system/showOperationList.tv");
}

</script>

</head>
<body >
	<div class="putSlider" id="putSlider">
		<div class="sliderOuter" id="sliderOuter">
			<a id="sliderLeftBtn" href="javascript:;"></a>
			<div id="slider" class="slider">
				<span id="bar" class="bar"></span>
			</div>
			<a id="sliderRightBtn" href="javascript:;"></a>
		</div>
	</div>
	<div class="putTree" id="putTree">
		<div style="width:100%; overflow:hidden;">
			<ul id="tree" class="filetree">
				<li><span class="icoH1"><a href="javascript:;" class="linkBtn" onclick="showGmView()"><fmt:message bundle="${resources}" key="COMMON.gmView" /></a></span></li>			
				<%if (uc.hasPower("roleManagement") || uc.hasPower("userManagement") || uc.hasPower("logManagement")|| uc.hasPower("configLog")) {	%>
				<li>
					<span class="folder"><fmt:message bundle="${resources}" key="SYSTEM.management" /></span>
					<ul>
						<%if (uc.hasPower("roleManagement")) {%>
						<li><span class="icoH2"><a href="javascript:;" class="linkBtn" onclick="showRoleView()"><fmt:message bundle="${resources}" key="SYSTEM.roleManagement" /></a></span></li>
						<%}	%>
						
						<%if (uc.hasPower("userManagement")) {%>
						<li><span class="icoH3"><a href="javascript:;" class="linkBtn" onclick="showUserView()" ><fmt:message bundle="${resources}" key="SYSTEM.userManagement" /></a></span></li>
						<%}	%>
						
						<%if (uc.hasPower("logManagement")) {%>
						<li><span class="icoH4"><a href="javascript:;" class="linkBtn" onclick="showLogView()"><fmt:message bundle="${resources}" key="MODULE.logManagement" /></a></span></li>
						<%}	%>	
						
						<%if (uc.hasPower("configLog")) {%>
						<li><span class="icoH5"><a href="javascript:;" class="linkBtn" onclick="showOperationLogView()"><fmt:message bundle="${resources}" key="SYSTEM.configLog" /></a></span></li>
						<%}	%>		
					</ul>
				</li>
				<%} %>
				
				<%if (uc.hasPower("departmentManagement") || uc.hasPower("postManagement") || uc.hasPower("cmServiceType") || uc.hasPower("tagManagement")) {%>
				<li>
					<span class="folder"><fmt:message bundle="${resources}" key="SYSTEM.dataDictionary" /></span>					
					<ul>
						<%if (uc.hasPower("departmentManagement")) {%>
						<li><span class="icoH6"><a href="javascript:;" class="linkBtn" onclick="showDepartmentManager()"><fmt:message bundle="${resources}" key="SYSTEM.department" /></a></span></li>
						<%}%>
						<%if (uc.hasPower("postManagement")) {%>
						<li><span class="icoH7"><a href="javascript:;" class="linkBtn" onclick="showPostManager()"><fmt:message bundle="${resources}" key="SYSTEM.place" /></a></span></li>
						<%}%>
						<%if ((uc.hasSupportModule("cmc"))&&uc.hasPower("cmServiceType")) {%>
						<li><span class="icoH7"><a href="javascript:;" class="linkBtn" onclick="showCmServiceType()"><fmt:message bundle="${resources}" key="SYSTEM.cmServiceType" /></a></span></li>
						<%}%>
						<%if (uc.hasSupportModule("onu") && uc.hasPower("tagManagement")) {%>
						<li><span class="icoH7"><a href="javascript:;" class="linkBtn" onclick="showTagManagementView()"><fmt:message bundle="${resources}" key="userPower.tagManagement" /></a></span></li>
						<%}	%>	
					</ul>
				</li>
				<%}%>
			</ul>
		</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<%if (uc.hasPower("roleManagement")) {%>
			<li><a href="#" class="icoH8" onclick="newRole()"><fmt:message bundle="${resources}" key="SYSTEM.newRole" /></a></li>
			<%}%>
			
			<%if (uc.hasPower("userManagement")) {%>
			<li><a href="#" class="icoH9" onclick="newUser()"><fmt:message bundle="${resources}" key="SYSTEM.newUser" /></a></li>
			<%}%>
		</ol>
	</div>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>	
<script type="text/javascript">
$(function(){
	//加载树形菜单;
	$("#tree").treeview({ 
		animated: "fast",
		control:"#sliderOuter"
	});	//end treeview;
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	//点击树形节点变橙色背景;
	$(".linkBtn").live("click",function(){
		$(".linkBtn").removeClass("selectedTree");
		$(this).addClass("selectedTree");
	});//end live;
	
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; //因为#putTree{padding:10px};
		var h3 = $("#threeBoxLine").outerHeight();
		var h4 = $("#putBtn").outerHeight();
		var putTreeH = h - h1 - h2 - h3 - h4;
		if(putTreeH > 20){
			$("#putTree").height(putTreeH);
		}	
	};//end autoHeight;
	
	autoHeight();
	$(window).resize(function(){
		autoHeight();
	});//end resize;
})
</script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	
</body>
</html>