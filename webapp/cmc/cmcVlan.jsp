<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title>Cmc vlan</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import js.tools.ipText
    import cmc.js.cmcVlanList
    css css/white/disabledStyle
</Zeta:Loader>
<style>
.bmenu_manage {
	background : url("/images/fault/action.gif") no-repeat;
}
.bmenu_remanage {
	background : url("/images/add.png") no-repeat;
}
.bmenu_cancelmanage {
	background : url("/images/minus.png") no-repeat;
}
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
</style>
<script type="text/javascript">
// for tab changed start
var cmcId = '${cmcId}';
var dispatcherInterval = <%= SystemConstants.getInstance().getLongParam("entitySnap.refresh.interval", 60000L) %>;
var dispatcherTimer = null;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
</script>
</head>
<body></body>
</Zeta:HTML>
