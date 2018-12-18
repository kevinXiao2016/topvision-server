<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="../css/zgraph.css" />
<link rel="stylesheet" type="text/css" href="../css/cluetip.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />

<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.core.js"></script>
<script type="text/javascript" src="../js/jquery/ui.draggable.js"></script>
<script type="text/javascript" src="../js/jquery/ui.selectable.js"></script>
<script type="text/javascript" src="../js/jquery/cluetip.js"></script>
<script type="text/javascript" src="../js/jquery/hoverIntent.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
</HEAD>
<script>
var dashedHelper = {border:'1px dashed black', background:'transparent'};
var squareHelper = {border:'1px solid #316AC5', background:'#D2E0F0', opacity:0.5};
var portMenu = null;
function saveClick() {
	var container = Zeta$('entityPane');
	var children = container.children;
	var size = children.length; 
	if (size > 0) {
		var x = [];
		var y = [];
		var portIds = [];
		for (var i = 0; i < size; i++) {
			if(children[i].tagName == 'DIV') {
				x[x.length] = children[i].offsetLeft;
				y[y.length] = children[i].offsetTop;
				portIds[portIds.length] = children[i].portId;
			}
		}
		$.ajax({url: 'savePaneCoord.tv', type: 'POST', data: {x: x, y: y, portIds: portIds},
			error: function() {
				window.top.showErrorDlg();
			}, dataType: 'plain', cache: false});
	}
}
var focusPortId;
function printClick() {
}
function goToEntityByPort() {
}
function showPortProperty() {
}
function showPortFlowReal() {
}
function openPort() {
}
function closePort() {
}
function showPortMenu() {
	if (portMenu == null) {
		Ext.BLANK_IMAGE_URL = '../images/s.gif';
		portMenu = new Ext.menu.Menu({id: 'port-menu', items: [
			{text: I18N.entityPhysicalPane.positiondevice, handler: goToEntityByPort},
			'-',
			{text: I18N.NETWORK.openPort, handler: openPort},
			{text: I18N.NETWORK.closePort, handler: closePort}, '-',
			{text: I18N.entityPhysicalPane.porttraffic, handler: showPortFlowReal}, '-',
			{text: I18N.MENU.property, handler: showPortProperty}
		]});
	}
	portMenu.showAt([event.x, event.y]);
}
function doOnload() {
	$("#entityPane").selectable({
		filter: 'div', cancel: 'img'
	});
	$("#entityPane").selectable("setHelper", squareHelper);
	$('#entityPane').bind('contextmenu', function(evt) {
		var el = ZetaUtils.getSrcElement(event);
		if (el.tagName == 'IMG') {
			focusPortId = el.parentNode.portId;
			showPortMenu();
			return false;
		}
		return true;	
	});
	$('#entityPane').bind('mouseup', function(evt) {
		var el = ZetaUtils.getSrcElement(event);
		if (el.tagName == 'IMG') {
			var container = Zeta$('entityPane');
			var children = container.children;
			var size = children.length; 
			if (size > 0) {
				for (var i = 0; i < size; i++) {
					children[i].className = 'ui-unselecting';
				}
			}
			focusPortId = el.parentNode.portId;
			el.parentNode.className = 'ui-selected';
		}
	});	

	var container = Zeta$('entityPane');
	var children = container.children;
	var size = children.length; 
	if (size > 0) {
		for (var i = 0; i < size; i++) {
			$('#img' + children[i].portId).cluetip({cluetipClass: 'topvision', titleAttribute: 'cluetip',
				width: 320, arrows: false, waitImage: false,
				dropShadow: false, ajaxCache: false,
				fx: {             
					open: 'show', // can be 'show' or 'slideDown' or 'fadeIn'
					openSpeed: ''
			   	},
			    hoverIntent: {
			        interval: 500,
			        timeout: 0
			    }    
			});			
		}
	}
}
</script>
<BODY onload="doOnload()" onselectstart="return false;"
	style="padding-top: 50px;">
	<div
		style="position: absolute; left: 0px; top: 15px; padding-left: 15px;">
		<%@ include file="entity.inc"%>
	</div>
	<div class=entityPane id="entityPane"
		style="position: fixed; background: url(../conf/panel/map_router.png) no-repeat">
		<s:iterator value="ports">
			<div id="port<s:property value="portId"/>"
				portId="<s:property value="portId"/>"
				style="position:absolute;left:<s:property value="x"/>;top:<s:property value="y"/>;">
				<s:if test="ifAdminStatus!=1">
					<img id="img<s:property value="portId"/>"
						cluetip="<s:property value="ifDescr"/>"
						rel="../port/loadPortTip.tv?portId=<s:property value="portId"/>"
						src="../images/network/port/rj45_gray.png">
				</s:if>
				<s:else>
					<s:if test="ifOperStatus==1">
						<img id="img<s:property value="portId"/>"
							cluetip="<s:property value="ifDescr"/>"
							rel="../port/loadPortTip.tv?portId=<s:property value="portId"/>"
							src="../images/network/port/rj45_green.png">
					</s:if>
					<s:else>
						<img id="img<s:property value="portId"/>"
							cluetip="<s:property value="ifDescr"/>"
							rel="../port/loadPortTip.tv?portId=<s:property value="portId"/>"
							src="../images/network/port/rj45_red.png">
					</s:else>
				</s:else>
			</div>
			<script>
	$('#port<s:property value="portId"/>').draggable({opacity: 0.50, scroll: true, helper: 'clone', distance: 5,
		stop: function(e, ui) {
			this.style.left = ui.position.left;
			this.style.top = ui.position.top;
		},
		start: function(e, ui) {
			ui.helper.css("border", "0");
		}
	});
</script>
		</s:iterator>
	</div>
	<script type="text/javascript" src="../js/ext/ext-base.js"></script>
	<script type="text/javascript" src="../js/ext/ext-all.js"></script>
</BODY>
</HTML>
