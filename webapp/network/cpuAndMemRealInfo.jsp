<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"></link>
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.graph-div {
	font-family: sans-serif;
	font-size: 16px;
}
</style>

<!-- 
<script type='text/javascript' src='../dwr/engine.js'> </script>
<script type='text/javascript' src='../dwr/interface/RealTimeCpuAndMem.js'> </script>
<script type='text/javascript' src='../dwr/util.js'> </script>
 -->

<!--[if IE]><script language="javascript" type="text/javascript" src="../js/flot/excanvas.pack.js"></script><![endif]-->
<script language="javascript" type="text/javascript"
	src="../js/flot/jquery.js"></script>
<script language="javascript" type="text/javascript"
	src="../js/flot/jquery.flot.pack.js"></script>

<script language="javascript" type="text/javascript">
var entityId = <s:property value="entityId"/>;
var entityIp = '<s:property value="ip"/>';
var refreshInterval = 15 * 1000;
var timer= null;

var startTime = 0;
var endTime = 0;
var maxTimes = 50; // 一个时间段内同时看见的连续最大次数

function onPrintClick() {
	window.print();
}

function refreshBoxChanged(box) {
	var t = parseInt(box.options[box.selectedIndex].value) * 1000;
	if (t == refreshInterval) {
		return;
	}
	refreshInterval = t;
	doOnunload();
	if (t > 0) {
		doOnload();
	}
}

function doOnload() {
	timer = setInterval('requestToRemote()', refreshInterval);
	//init();
	//startDwr();
}
function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
	//stopDwr();
}
function requestToRemote() {
  	$.ajax({url: '../entity/getRealCpuUsage.tv', data: {entityId : entityId},
  		success: scCallback, error: fnCallback, dataType: 'json', cache: false});
}
function scCallback(json) {
	if (json) {
		if (json.time >= endTime) {
			cpu = [];
			mem = [];
			startTime = json.time;
			endTime = startTime + maxTimes * refreshInterval;
			//cpuPlot.getAxes().xaxis.min = memPlot.getAxes().xaxis.min = startTime;
			//cpuPlot.getAxes().xaxis.max = memPlot.getAxes().xaxis.max = endTime;
			
			cpuPlot = $.plot($("#placeholder"),
			       [ { data: cpu, label: I18N.cpuAndMemRealInfo.cpuUsage+"(%)" }],
			       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
			         yaxis: { min: 0, max: 100},     
			         grid: { hoverable: true},
			         legend: { position: 'ne' } });
			memPlot = $.plot($("#placeholder1"),
			      [ { data: mem, label: I18N.cpuAndMemRealInfo.usedMem +"(KBytes)"}],
			      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
			        yaxis: { min: 0, max: memTotal},
			        grid: { hoverable: true},
			        legend: { position: 'ne' } });
		}
		cpu.push([json.time, json.cpu]);
		mem.push([json.time, json.mem]);
        cpuPlot.draw();
        memPlot.draw();
	}
}
function fnCallback(response) {
}

/* dwr start */
var dwrStarted = false;
function init() {
	dwr.engine.setActiveReverseAjax(true);
}
function receiveCpuAndMem(messages) {
  //alert(messages);
  var chatlog = "";
  for (var data in messages) {
    chatlog = "<div>" + dwr.util.escapeHtml(messages[data].text) + "</div>" + chatlog;
  }
  //dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
}
function startDwr() {
	//var text = dwr.util.getValue("text");
	//dwr.util.setValue("text", "");
	dwrStarted = true;
	RealTimeCpuAndMem.getCpuAndMem(entityId);
}
function stopDwr() {
	if (dwrStarted) {
		RealTimeCpuAndMem.stop(entityId);
		dwrStarted = false;
	}
}
/* dwr end */
</script>
</head>
<body style="margin: 10px;" onload="doOnload()" onunload="doOnunload()">
	<table width=100% cellspacing=0 cellpadding=0>
		<tr id=headerbar>
			<td></td>
			<td width=600px height=50px>
				<table width=100% cellspacing=0 cellpadding=0>
					<tr>
						<td width=80px><fmt:message key="WorkBench.deviceAddress" bundle="${resource}" />:</td>
						<td width=150px><input class=iptxt style="width: 150px"
							id="ip" type=text value="<s:property value="ip"/>" readonly>
						</td>
						<td width=80px>&nbsp;</td>
						<td width=80px><fmt:message key="cpuAndMemRealInfo.refreshRate" bundle="${resource}" />:</td>
						<td width=110px><select id="refreshBox" style="width: 80px"
							onchange="refreshBoxChanged(this)">
								<option value="10">10</option>
								<option value="15" selected>15</option>
								<option value="20">20</option>
								<option value="30">30</option>
								<option value="60">60</option>
						</select>&nbsp;<fmt:message key="cpuAndMemRealInfo.sec" bundle="${resource}" /></td>
						<td align=right style="padding-right: 7px;"></td>
					</tr>
				</table></td>
			<td></td>
		</tr>
		<tr>
			<td colspan=3 align=center>
				<div id="placeholder" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br />
				<div id="placeholder1" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br />
			<br /></td>
		</tr>
	</table>
	<script id="source" language="javascript" type="text/javascript">
var cpu = []
var mem = [];
var memTotal = 512;
if ('<s:property value="memTotal"/>' != '0.0') {
	memTotal = <s:property value="memTotal"/>;
}

startTime = <s:property value="synTime"/>;
endTime = startTime + maxTimes * refreshInterval;
cpu.push([startTime, <s:property value="cpuUsage"/>]);
mem.push([startTime, <s:property value="memUsage"/>]);

function timeFormatter(val, axis) {
    var d = new Date(val);
    return d.toLocaleTimeString();
}

var cpuPlot = $.plot($("#placeholder"),
       [ { data: cpu, label: I18N.cpuAndMemRealInfo.cpuUsage+"(%)" }],
       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
         yaxis: { min: 0, max: 100},     
         grid: { hoverable: true},
         legend: { position: 'ne' } });
var memPlot = $.plot($("#placeholder1"),
      [ { data: mem, label: I18N.cpuAndMemRealInfo.usedMem+"(KBytes)" }],
      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
        yaxis: { min: 0, max: memTotal},
        grid: { hoverable: true},
        legend: { position: 'ne' } });

$("#placeholder").bind("plothover", function (event, pos, item) {
	doPlothover(event, pos, item, 0);
}); 

$("#placeholder1").bind("plothover", function (event, pos, item) {
      doPlothover(event, pos, item, 1);
});  

var previousPoint = null;         
function  doPlothover(event, pos, item, index) {
	if (item) {
          if (previousPoint != item.datapoint) {
              previousPoint = item.datapoint;
              $("#tooltip").remove();
              var x = item.datapoint[0].toFixed(2),
                  y = item.datapoint[1].toLocaleString();
              var d = new Date(parseInt(x));
              
              if (index == 0) {
              	showTooltip(item.pageX, item.pageY,
              			I18N.DeviceResponse.time+": " + d.toLocaleTimeString() + "<br>"+ I18N.cpuAndMemRealInfo.cpuUsage+": " + y + "%");
              } else if (index == 1) {
					showTooltip(item.pageX, item.pageY,
						I18N.DeviceResponse.time+": " + d.toLocaleTimeString() + "<br>"+ I18N.cpuAndMemRealInfo.usedMem+": " + y+"KBytes");              
              }
          }
      }
      else {
          $("#tooltip").remove();
          previousPoint = null;            
      }
}             

function showTooltip(x, y, contents) {
    $('<div id="tooltip">' + contents + '</div>').css( {
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        border: '1px solid gray',
        padding: '2px',
        'background-color': 'yellow',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}

function doOnBeforePrint() {Zeta$('headerbar').style.display = 'none';}
function doOnAfterPrint() {Zeta$('headerbar').style.display = 'block';}
if(document.addEventListener){
	window.addEventListener('beforeprint', doOnBeforePrint, false);
	window.addEventListener('afterprint', doOnAfterPrint, false);
}else{
	window.attachEvent('onbeforeprint', doOnBeforePrint);
	window.attachEvent('onafterprint', doOnAfterPrint);	
}
</script>
</body>
</html>
