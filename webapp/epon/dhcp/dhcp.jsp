<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module oltdhcp
    IMPORT js/nm3k/Nm3kSwitch
    IMPORT js/components/segmentButton/SegmentButton
    IMPORT epon/dhcp/dhcp
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	#bottomMask{ width: 100%; height: 100%; overflow:hidden; position:absolute; top:40px; left:0; z-index: 999;
		filter: alpha(opacity=90); -webkit-opacity: 0.9; -moz-opacity: 0.9; -ms-opacity: 0.9; -o-opacity: 0.9; opacity: 0.9;
	}
	#bottomMask span{ padding: 10px; border: 1px solid #eee; background: #000; display:inline-block; color: #fff; margin-top: 180px;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var topOltDhcpEnable = parseInt('${topOltDhcpEnable}',10); //开关的状态, 1表示开，0表示关;
	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
	
	function authLoad() {
		if(!refreshDevicePower) {
			$("#refreshDhcpBtn").attr("disabled",true);
		}
	} 
</script>

</head>
<body class="whiteToBlack" onload="authLoad()">
	<div id="topPart">
		<ul class="leftFloatUl edge10">
			<li class="rightBlueTxt pT5">@oltdhcp.dhcpSwitch@@COMMON.maohao@</li>
			<li id="putSwitch" class="pT3 pR10"></li>
			<li>
				<a id="refreshDhcpBtn" href="javascript:;" class="normalBtn" onclick="refreshDhcpData();"><span><i class="miniIcoEquipment"></i>@oltdhcp.synDhcpData@</span></a>
			</li>
		</ul>
		<div class="clearBoth mL10" id="putTabDiv"></div>
	</div>
	<div id="mainPart">
		<iframe id="pageFrame" name="pageFrame" frameborder="0" width="700" height="400"></iframe>
	</div>
</body>
</Zeta:HTML>