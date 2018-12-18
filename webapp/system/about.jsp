<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<HEAD>
<Zeta:Loader>
    module platform
</Zeta:Loader>
<%@include file="../include/cssStyle.inc"%>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<fmt:setBundle basename="com.topvision.platform.resources" var="resource"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">
<style type="text/css">
#logo {
	background: url(../<fmt:message bundle="${resource}" key="app.logo.icon"/>) no-repeat center 0;
	width: 100%;
	height: 50px;
}
#w1200{ width:1200px; position:absolute; top:0; left:0;}
#step0, #step1{width:600px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:600px;}
#w600{ width:590px; overflow:hidden; position: relative; top:0; left:0; height:250px;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
$(function(){
	$('#licensebt').click(function(){
		$("#w1200").animate({left:-600},'fast');
	})
})

function funcShow(){
//    location.href = 'licenseAbout.tv';

	$("#w1200").animate({left:-600},'fast');
}

function lastClick() {
//    location.href = '/system/showAbout.tv';

	$("#w1200").animate({left:0},'normal');
}
$(function(){
    $.ajax({
		 url:"@about.licensetxt@",
		 async:false,
		 success: function(json){
			 $('#putTxt').html(json);
		 }
    })
	 
	
})
</script>
</HEAD>
<body class="openWinBody">
	<div style="width:100%; padding-top:10px; margin:0 auto;">
		<div id="logo"></div>
	</div>
	
	<div id="w600">
	<div id="w1200">
	<div id="step0">
	<div class="edgeTB10LR20 pT10" >
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						<fmt:message bundle="${resource}" key="version"/>
	                 </td>
	                 <td>
						V<s:property value="version.buildVersion"/>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle="${resource}" key="version.detail1"/>
	                 </td>
	                 <td>
	                 <a class="yellowLink" href="javascript:;" id="licensebt"><fmt:message bundle="${resource}" key="version.detail2"/></a>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						<fmt:message bundle="${resource}" key="version.licensedto"/>
	                 </td>
	                 <td>
						<s:property value="licView.organisation"/>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td colspan="2" class="txtCenter">
						<fmt:message bundle="${resource}" key="about.copyrights"/>
	                 </td>
	             </tr>	             
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">		
		         <li><a id="NAVIGATOR_BT" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i><fmt:message bundle="${resource}" key="window.close"/></span></a></li>
		     </ol>
		</div>
	</div>
	</div>
	<div id="step1">
	<div style="width:570px; height:210px; overflow:auto;" class="pL10" id="putTxt" >
        <!-- <iframe width=100% height=200px style=" white-space:normal; word-break:break-all; word-wrap:break-word;" frameborder=0 src="@about.licensetxt@" ></iframe>
        <hr width=595 size=1 color=black align=center noshade> -->
    </div>
    <div style="padding-top:5px;padding-left:245px; margin:0 auto;">	         
		<a onclick="lastClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i>@COMMON.back@</span></a>
	</div>
	</div>	
	</div>
	</div>
</BODY>
</Zeta:HTML>
