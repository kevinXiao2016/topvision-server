<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head><title>Color Selector</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/colorpicker/dhtmlxcolorpicker.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/dhtmlx/colorpicker/dhtmlxcolorpicker1.0.js"></script>
</head><body class="POPUP_WND whiteToBlack" onload="initColorPalete()">
<center>
<div id="CPcont"></div>
</center>
<style type="text/css">
.cs_ButtonCancel2{ text-indent: 11px;}
.cs_CustomColorAdd_Mini{ text-indent: 20px;}
</style>
<script>
window.dhx_globalImgPath="../js/dhtmlx/colorpicker/imgs/";
var I18N = {};
I18N.COMMON = {
	red:'<fmt:message bundle="${res}" key="colorPicker.red"/>',
	green:'<fmt:message bundle="${res}" key="colorPicker.green"/>',
	blue:'<fmt:message bundle="${res}" key="colorPicker.blue"/>',
	hue:'<fmt:message bundle="${res}" key="colorPicker.hue"/>',
	sat:'<fmt:message bundle="${res}" key="colorPicker.saturation"/>',
	lum:'<fmt:message bundle="${res}" key="colorPicker.luminance"/>',
	addToCustomColor:'<fmt:message bundle="${res}" key="colorPicker.addToCustomColor"/>',
	ok:'<fmt:message bundle="${res}" key="COMMON.ok"/>',
	cancel:'<fmt:message bundle="${res}" key="COMMON.cancel"/>'		
};
function initColorPalete(){
  	var cpx = new dhtmlXColorPicker('CPcont', false, true);
  	cpx.language = {
		labelHue : '<fmt:message bundle="${res}" key="colorPicker.hue"/>',
		labelSat : '<fmt:message bundle="${res}" key="colorPicker.saturation"/>',
		labelLum : '<fmt:message bundle="${res}" key="colorPicker.luminance"/>',
		labelRed : '<fmt:message bundle="${res}" key="colorPicker.red"/>',
		labelGreen : '<fmt:message bundle="${res}" key="colorPicker.green"/>',
		labelBlue : '<fmt:message bundle="${res}" key="colorPicker.blue"/>',
		btnAddColor : '<fmt:message bundle="${res}" key="colorPicker.addToCustomColor"/>',
		btnSelect : '<fmt:message bundle="${res}" key="COMMON.ok"/>',
		btnCancel : '<fmt:message bundle="${res}" key="COMMON.cancel"/>'
  	};	
	cpx.setCustomColors("#ff00ff,#00aabb,#6633ff");
	cpx.setOnSelectHandler(setColor);
	//cpx.setOnCancelHandler(setCancel);
	cpx.init();	
}

function setColor(color) {
	window.parent.ZetaCallback = {type:'colorpicker', color: color};
	window.parent.closeWindow('colorDlg');
}
function cancelCallback() {
	window.parent.ZetaCallback = null;
	window.parent.closeWindow('colorDlg');
}
</script>
</body></html>
