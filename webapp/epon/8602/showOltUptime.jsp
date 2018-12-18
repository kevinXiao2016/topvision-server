<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Device Up Time</title>
<%@ include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="portal"/>
</head>
<body>
	<div id=deviceUpTimer align=center  class="deviceUpTimer">
		<div class="dayDiv4" id="dayContainer">
			<img id="day4" border=0 hspace=3 src="/images/0.png"><img id="day3" border=0 hspace=3 src="/images/0.png"><img id="day2" border=0 hspace=3 src="/images/0.png"><img id="day1" border=0 hspace=3 src="/images/0.png">
			<span class="timeTxt"><fmt:message bundle="${portal}" key="ENTITYSNAP.deviceUpTime.day" /></span>
		</div>
		<div class="hourDiv">
			<img id="hour2" border=0 hspace=3 src="/images/0.png"><img id="hour1" border=0 hspace=3 src="/images/0.png">
			<span class="timeTxt"><fmt:message bundle="${portal}" key="ENTITYSNAP.deviceUpTime.hour" /></span>
		</div>		
			
		<div class="minuteDiv">
			<img id="min2" border=0 hspace=3 src="/images/0.png"><img id="min1" border=0 hspace=3 src="/images/0.png">
			<span class="timeTxt"><fmt:message bundle="${portal}" key="ENTITYSNAP.deviceUpTime.minute" /></span>
		</div>
		
		<div class="secondsDiv">
			<img id="sec2" border=0 hspace=3 src="/images/0.png"><img id="sec1" border=0 hspace=3 src="/images/0.png">
			<span class="timeTxt"><fmt:message bundle="${portal}" key="ENTITYSNAP.deviceUpTime.seconds" /></span>
		</div>
	</div>
	<div id=deviceUpTimerMask  align=center style="display:none">
			<font style='font-size:25'><fmt:message bundle="${portal}" key="entitySnapPage.deviceLinkDown" /></font>
	</div>
</body>
</html>