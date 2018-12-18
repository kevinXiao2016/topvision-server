<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<%@include file="/include/cssStyle.inc"%>
	<head>
	<Zeta:Loader>
	    library Jquery
	    library Ext
	    library Zeta
		module  performance
		CSS performance/css/performanceGlobalConfig
		IMPORT performance/nbiTargetConfig
		IMPORT js/json2
	</Zeta:Loader>
	</head>
	<body class="whiteToBlack">
		<div id="topPart"></div>
		<div id="mainPart" class="edge10"></div>
	</body>
</Zeta:HTML>