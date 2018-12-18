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
		    CSS css/animate
		    import js/raphael/raphael
		    import js/raphael/RaphaelCir2
		    import cm/webProxyConnect
		</Zeta:Loader>
		<link rel="stylesheet" type="text/css" href="./webProxyConnect.css" />
	</head>
	<body class="whiteToBlack edge20" style="background:#222; background:#151618">
		<div class="folders">
			<div id="errFloder" class="folder pointer" onclick="showErrorWin()">
				<div class="folder-title"></div>
				<div class="folder-body"></div>
				<p class="txtCenter pT5">@webProxy.log@</p>
			</div>
			<div id="progressFloder" class="folder pointer mT30" onclick="showProgressWin()">
				<div class="folder-title"></div>
				<div class="folder-body"></div>
				<p class="txtCenter pT5">@webProxy.progress@</p>
			</div>
			<div id="heartFloder" class="folder pointer mT30" onclick="showHeartWin()">
				<div class="folder-title"></div>
				<div class="folder-body"></div>
				<p class="txtCenter pT5">@webProxy.heartbeat@</p>
			</div>
		</div>
	</body>
</Zeta:HTML>