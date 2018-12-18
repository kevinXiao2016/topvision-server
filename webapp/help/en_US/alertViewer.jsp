<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    css help.help
    module platform
</Zeta:Loader>
</head>
<body class="openWinBody edge10">
    <h2 class="h2_bubble">Alert Viewer</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1.Global alarm acknowledgment</h4>
    	<p class="pT5">Through global configuration settings have been cleared to state whether the alarm automatically confirmed.</p>
    	<p>Configuration <b class="blueTxt"> open </b>, the system will <b class="blueTxt"> automatic confirmation </b> has been cleared in the current alarm</p>
    	<p>Configuration <b class="blueTxt"> Close </b>, the user needs to <b class="blueTxt"> manual confirmation </ b> has been cleared in the current alarm</p>
    	<h6 class="pT40">The first step: the upper right corner click on the system "Global Management View" button</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_en_1.jpg" alt="" />
    	<h6 class="pT40">Step Two: In the global management view page click "Confirm Global Alarm" button</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_en_2.jpg" alt="" />
    	<h6 class="pT40">The third step: the pop-up page, by clicking on the "open" or "close" button to set the alarm is automatically confirmed.</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_en_3.jpg" alt="" />
    </div>
</body>
</Zeta:HTML>
