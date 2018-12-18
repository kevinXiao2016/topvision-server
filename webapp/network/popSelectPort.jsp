<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var ifIndex = <s:property value="linkEx.srcPortIndex"/>;
Ext.BLANK_IMAGE_URL = '../images/s.gif';
function doOnload(){
	var opt = Zeta$('ifIndexs').options;
	len=opt.length;
	for(i=0;i<len;i++) {
	   if(opt[i].value == ifIndex) {
	       opt[i].selected=true;
	       break;
	   }
	}
}
function onPortChanged(box) {
	ifIndex = parseInt(box.options[box.selectedIndex].value);
}
function okClick() {
	window.top.ZetaCallback = {type:'ok', ifIndex:ifIndex};
	window.top.closeWindow('modalDlg');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('modalDlg');
}
</script>
</HEAD>
<body class="openWinBody" onload="doOnload()">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20  pT40">	
		 <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt withoutBorderBottom" width="160">
						<fmt:message bundle="${res}" key="COMMON.portHeader"/>:
	                 </td>
	                 <td class="withoutBorderBottom">
						<select id="ifIndexs" class="normalSel" onchange="onPortChanged(this);" style="width: 220px">
								<option value="0"><fmt:message bundle="${res}" key="topo.popSelectPort.choose"/></option>
								<s:iterator value="ports">
									<option value="<s:property value="ifIndex"/>">
										<s:property value="ifName" />
									</option>
								</s:iterator>
						</select>
	                 </td>
	             </tr>
			</tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig"  onclick="okClick()"><span><i class="miniIcoData"></i><fmt:message bundle="${res}" key="COMMON.save"/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${res}" key="COMMON.cancel"/></span></a></li>
		     </ol>
		</div>
	</div>
</body>
</HTML>
