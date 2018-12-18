<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
    IMPORT system.userList
</Zeta:Loader>
<style type="text/css">
.bmenu_reset {background-image: url(../images/system/reset.gif) !important;}
.bmenu_role {background-image: url(../images/system/setRole.gif) !important;}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var userPower = <%= uc.hasPower("userManagement") %>;
var pageSize = <%= uc.getPageSize() %>;
var userId = <%= uc.getUserId() %>;
function tabActivate() {
	window.top.setStatusBarInfo('', '');
}
</script>
</head><body class="whiteToBlack">
</body>
</Zeta:HTML>