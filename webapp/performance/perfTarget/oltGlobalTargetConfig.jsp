<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    CSS    performance/css/performanceGlobalConfig
    IMPORT performance/nm3kBatchData
    import performance.js.globalTargetConfigCommon
    import performance.js.oltGlobalTargetConfig
</Zeta:Loader>
<style type="text/css">
	
</style>
<script type="text/javascript">
	var oBackData = ${oltTargetJson};
	var isBindDftTemp = ${isBindDftTemp};
	var isPerfThdOn = ${isPerfThdOn};
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart"></div>
	<div id="mainPart" class="edge10"></div>
</body>
</Zeta:HTML>