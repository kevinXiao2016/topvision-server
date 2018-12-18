<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head><title>Upload Backgroud Image</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
function fileChanged(box) {
	var v = box.value;
	Zeta$('f_file').value = v;
	var patn = /\.jpg$|\.jpeg$|\.gif$/i;
	if(patn.test(v)){
		Zeta$('preview').src = v;
		Zeta$('okBt').disabled = false;	
	} else if (v != '') {
		Zeta$('okBt').disabled = true;	
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.imageFileError);		
	}
}
function drawImage(img) {
	var image=new Image();
	image.src=img.src;
	if(image.width>0 && image.height>0){
		if (image.width <= 346 && image.height <= 133) {
			img.width = image.width;
			img.height = image.height;			
		} else {
			var s1 = image.width / 346;
			var s2 = image.height / 133;
			if (s1 < s2) {s1 = s2;}
			img.width = parseInt(image.width / s1);
			img.height = parseInt(image.height / s1);
		}
	}
}
function okClick() {
	Zeta$('uploadForm').submit();
}
function backClick() {
	location.href = '../include/showImageChooser.tv';
}
function cancelClick() {
	window.parent.closeWindow("modalDlg");
}
function fclick(obj,e){ 
  var el = ZetaUtils.getSrcElement(e);
  obj.style.top = getY(el) - 2;
  obj.style.left = getX(el); 
}
</script>
</head><body class=POPUP_WND>
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td style="padding:10px 10px 0 10px" valign=top>
  <table width=100% height=100% cellspacing=0 cellpadding=0>
	<tr><td height=20><fmt:message key="Common.pleaseChooseUploadFile" bundle="${resource}"/>:</td></tr>
	<tr><td height=25><div>
	<form id="uploadForm" action="../topology/uploadFolderBg.tv" method="POST" enctype="multipart/form-data" target="uploadFrame">
	<input type="hidden" name="module" value="component">
	<input class=iptxt readonly id="f_file" value="" style="width:270px;">&nbsp;<input type="button" value="<fmt:message key="COMMON.browse" bundle="${resource}"/>"
	  onmouseover="fclick(t_file,event);this.className='BUTTON_OVER75'"
	  class=BUTTON75 onMouseOut="this.className='BUTTON75'">
	<input id="t_file" type="file" onchange="fileChanged(this)" name="upload"
	style="position:absolute;filter:alpha(opacity=0);opacity:0;width:70px" hidefocus>
	</form></div></td></tr>
	<tr><td height=20><fmt:message key="Common.previewSurportIEMaxthon" bundle="${resource}"/>:</td></tr>
	<tr><td class=TREE-CONTAINER align=center style="padding:0"><img id=preview 
	src="../images/s.gif" border=0 onload="javascript:drawImage(this);"></td></tr>
  </table>
</td></tr>
<tr><td height=30 align=right style="padding:10px"><button class=BUTTON75
  onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
  onclick="backClick()"><fmt:message key="Common.back" bundle="${resource}"/></button>&nbsp;<button id="okBt" class=BUTTON75
  onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
  onclick="okClick()" disabled><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;<button class=BUTTON75
  onMouseOver="this.className='BUTTON_OVER75'"
  onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button>
</td></tr></table>
<iframe name="uploadFrame" width=0 height=0></iframe>
</body></html>



