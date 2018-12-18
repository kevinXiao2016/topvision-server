<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    plugin LovCombo
    plugin DropdownTree
    module network
    import js/customColumnModel
    import network.entityAction
    import js/json2
    import js/entityType
    import topo.entityList
</Zeta:Loader>
<style>
html, body{height:100%;}
#query-container{height:40px;padding-top: 15px;}
.queryTable td.rightBlueTxt{padding: 2px 0 2px 10px;}
</style>
<script type="text/javascript">
var saveColumnId = 'entityList';//保存表头数据的id;
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var pageSize = <%= uc.getPageSize() %>;
var dispatcherInterval = <%= SystemConstants.getInstance().getLongParam("entitySnap.refresh.interval", 60000L) %>;
var dispatcherTimer = null;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var showCartoon = '${showCartoon}' == 'false' ? false : true;//如果是false,那么没有设备的时候会出现提示信息;
</script>
</head>
<body class="whiteToBlack">
	<div id="query-container">
		<table class="queryTable">
			<tr>
				<td class="rightBlueTxt">@COMMON.alias@@COMMON.maohao@</td>
				<td><input type="text" class="normalInput w110 " id="alias" /></td>
				<td class="rightBlueTxt">@resources/COMMON.uplinkDevice@@COMMON.maohao@</td>
				<td><input type="text" class="normalInput w110" id="ip" /></td>
				<td class="rightBlueTxt">@COMMON.entityType@@COMMON.maohao@</td>
				<td><div id="deviceType_div"></div></td>
				<td class="rightBlueTxt">@COMMON.upperDevice@@COMMON.maohao@</td>
				<td><input type="text" class="normalInput w110" id="upperName" /></td>
				<td class="rightBlueTxt">@COMMON.folder@@COMMON.maohao@</td>
				<td><div id="region_tree"></div></td>
				<td class="rightBlueTxt">
					<a id="query_button" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
				</td>
			</tr>
		</table>
	</div>
	<div id="grid-container"></div>
</body>
</Zeta:HTML>
