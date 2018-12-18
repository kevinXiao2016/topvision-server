<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE epon
</Zeta:Loader>
</head>
<body>
	<div id=deviceUpTimer align=center  class="deviceUpTimer">
		<div class="dayDiv4" id="dayContainer">
			<img id="day4" border=0 hspace=3 src="/images/0.png" />
			<img id="day3" border=0 hspace=3 src="/images/0.png" />
			<img id="day2" border=0 hspace=3 src="/images/0.png" />
			<img id="day1" border=0 hspace=3 src="/images/0.png" />
			<span class="timeTxt">@ENTITYSNAP.deviceUpTime.day@</span>
		</div>
		<div class="hourDiv">
			<img id="hour2" border=0 hspace=3 src="/images/0.png" /><img id="hour1" border=0 hspace=3 src="/images/0.png" />
			<span class="timeTxt">@ENTITYSNAP.deviceUpTime.hour@</span>
		</div>		
			
		<div class="minuteDiv">
			<img id="min2" border=0 hspace=3 src="/images/0.png" /><img id="min1" border=0 hspace=3 src="/images/0.png" />
			<span class="timeTxt">@ENTITYSNAP.deviceUpTime.minute@</span>
		</div>
		
		<div class="secondsDiv">
			<img id="sec2" border=0 hspace=3 src="/images/0.png" /><img id="sec1" border=0 hspace=3 src="/images/0.png" />
			<span class="timeTxt">@ENTITYSNAP.deviceUpTime.seconds@</span>
		</div>
	</div>
	<div id=deviceUpTimerMask  align=center style="display:none">
		<font style='font-size:25'>@entitySnapPage.deviceLinkDown@</font>
	</div>
</body>
</Zeta:HTML>