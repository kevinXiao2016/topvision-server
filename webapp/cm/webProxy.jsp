<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
	<title>Cm List</title>
	<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
	    library ext
		library Jquery
	    library zeta
	    module cmc
	    IMPORT cm/webProxy
	    IMPORT js/nm3k/Nm3kSwitch
	</Zeta:Loader>
	<script type="text/javascript">
		var cmId = '${cmId}';
		var heartbeatId;
		var stepComplete = false; //所有连接过程是否成功;
		var tb, interval;
	</script>
	</head>
	<body class="whiteToBlack">
		<div id="topPart">
			<div id="putTbar"></div>
		</div>
		<iframe name="webProxyFrame" id="webProxyFrame" width="100%" height="100%" frameborder="0" src="/cm/webProxyConnect.jsp"></iframe>
	</body>
</Zeta:HTML>