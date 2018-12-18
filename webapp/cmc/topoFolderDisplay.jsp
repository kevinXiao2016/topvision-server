<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<%@include file="../include/ZetaUtil.inc"%>
		<Zeta:Loader>
			library ext
		    library jquery
		    library zeta
		    module cmc
		    import cmc.topoFolderDisplay
		    import js.jquery.dragMiddle
		</Zeta:Loader>
		<style type="text/css">
			#showOtherBtn{ position:absolute; top:2px; right:2px;}
			.rightMain-LR{ padding:0px;}
			#deviceListShowDiv{ height:100%; overflow:hidden;}
			.differentNun{ background:#f60; color:#fff; padding:0px 3px;-moz-border-radius: 8px; -khtml-border-radius: 8px; -webkit-border-radius: 8px; border-radius: 8px;}
		</style>
		<script type="text/javascript">
			var currentFolder = ${folderId};
		</script>
	</head>
	
	<body class="whiteToBlack bodyWH100percent">
		<div id="toolbar" style="height: 35px;width: 100%;"></div>
		<div class="wrapWH100percent" id="subDiv">
			<div class="left-LR" id="sidePart">
				<p class="pannelTit" id="sidePartTit">@IPTOPO.topoFolderDisplay@</p>
				<div id="deviceGridDiv" class="leftMain-LR clear-x-panel-body" style="position:relative;"></div>
			</div>
			<div class="line-LR" id="line"></div>
			<div class="right-LR" id="mainPart">
				<div id="deviceListShowDiv" class="rightMain-LR">
					<iframe id="putGridFrame" name="putGridFrame" width="100%" frameborder="0" height="100%" scrolling="auto" src=""></iframe>
				</div>
			</div>
		</div>
	</body>
	
</Zeta:HTML>
