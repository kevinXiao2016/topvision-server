<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"></link>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<style>
.graph-div {
	font-family: sans-serif;
	font-size: 16px;
}
</style>
<script language="javascript" type="text/javascript" src="../js/flot/jquery.js"></script>
<script language="javascript" type="text/javascript" src="../js/flot/jquery.flot.pack.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script language="javascript" type="text/javascript">
var linkId = <s:property value="linkId"/>;
var timer= null;
var timeTotal = 3600 * 0000;
var refreshInterval = 20 * 1000;

var startTime = 0;
var endTime = 0;
var maxTimes = 100; // 一个时间段内同时看见的连续最大次数

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
}
function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
function requestToRemote() {
	$.ajax({url: 'showLinkRealFlow.tv', data: {linkId : linkId},
  		success: scCallback, error: fnCallback, dataType: 'json', cache: false});	
}

function scCallback(response) {
	if (response.responseText) {
		var json = Ext.util.JSON.decode(response.responseText);
		if (json.time >= endTime) {
			totalFlows = []
			sendFlows = [];
			receiveFlows = [];
			sendPackets = [];
			receivePackets = [];
			sendPacketRates = [];
			receivePacketRates = [];
			startTime = json.time;
			endTime = startTime + maxTimes * refreshInterval;
			
			totalPlot = $.plot($("#placeholder"),
			       [ { data: totalFlows, label: I18N.linkRealFlow.totaltraffic }],
			       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
			         yaxis: { min: 0, max: 4000},     
			         grid: { hoverable: true},
			         lines: { show: true },
			         points: { show: true },
			         legend: { position: 'ne' } });
			flowPlot = $.plot($("#placeholder1"),
			      [ { data: sendFlows, label: I18N.linkRealFlow.Sendtraffic }, { data: receiveFlows, label: I18N.linkRealFlow.Receivetraffic }],
			      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
			        yaxis: { min: 0, max: 4000},
			        grid: { hoverable: true},
			        lines: { show: true },
			        points: { show: true },        
			        legend: { position: 'ne' } });
			        
			losedPacketNumberPlot = $.plot($("#placeholder2"),
			       [ { data: sendPackets, label: I18N.linkRealFlow.Senddroppedpackets }, { data: receivePackets, label: I18N.linkRealFlow.Receivepacketloss }],
			       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
			         yaxis: { min: 0, max: 400},     
			         grid: { hoverable: true},
			         lines: { show: true },
			         points: { show: true },         
			         legend: { position: 'ne' } });
			
			losedPacketRatePlot = $.plot($("#placeholder3"),
			      [ { data: sendPacketRates, label: I18N.linkRealFlow.Sendlostreportedrate }, { data: receivePacketRates, label: I18N.linkRealFlow.Receiverlostreportedrate }],
			      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
			        yaxis: { min: 0, max: 100},
			        grid: { hoverable: true},
			        legend: { position: 'ne' } }); 
		}
		totalFlows.push([json.time, json.totalFlow]);
		sendFlows.push([json.time, json.sendFlow]);
		receiveFlows.push([json.time, json.receiveFlow]);
		sendPackets.push([json.time, json.sendLosedPacketNumber]);
		receivePackets.push([json.time, json.receiveLosedPacketNumber]);
		sendPacketRates.push([json.time, json.sendLosedPacketRate]);
		receivePacketRates.push([json.time, json.receiveLosedPacketRate]);

        totalPlot.draw();
        flowPlot.draw();
        losedPacketNumberPlot.draw();
        losedPacketRatePlot.draw();        
	}
}
function fnCallback(response) {
}
</script>
</head>
<body class=BLANK_WND style="margin: 10px;" onload="doOnload();"
	onunload="doOnunload();">

	<table width=100% cellspacing=0 cellpadding=0>
		<tr>
			<td></td>
			<td width=600px height=50px>
				<table width=100% cellspacing=0 cellpadding=0>
					<tr>
						<td width=80px><fmt:message key="topoLabel.refreshRate" bundle="${resources}" /></td>
						<td width=110px><select id="refreshBox" style="width: 80px"
							onchange="refreshBoxChanged(this)">
								<option value="10">10</option>
								<option value="15" selected>15</option>
								<option value="20">20</option>
								<option value="30">30</option>
								<option value="60">60</option>
								<option value="90">90</option>
								<option value="120">120</option>
								<option value="180">180</option>
						</select>&nbsp;<fmt:message key="label.seconds" bundle="${resources}" /></td>
						<td align=right style="padding-right: 7px;"></td>
					</tr>
				</table></td>
			<td></td>
		</tr>
		<tr>
			<td colspan=3 align=center>
				<div id="placeholder" class=graph-div
					style="width: 600px; height: 200px;"></div> <br />
			<br />
				<div id="placeholder1" class=graph-div
					style="width: 600px; height: 200px;"></div> <br />
			<br />
				<div id="placeholder2" class=graph-div
					style="width: 600px; height: 200px;"></div> <br />
			<br />
				<div id="placeholder3" class=graph-div
					style="width: 600px; height: 200px;"></div> <br />
			<br /></td>
		</tr>
	</table>

	<script id="source" language="javascript" type="text/javascript">
var totalFlows = []
var sendFlows = [];
var receiveFlows = [];
var sendPackets = [];
var receivePackets = [];
var sendPacketRates = [];
var receivePacketRates = [];

var totalPlot = null;
var flowPlot;
var losedPacketNumberPlot;
var losedPacketRatePlot;

startTime = <s:property value="synTime"/>;
endTime = startTime + maxTimes * refreshInterval;

totalFlows.push([startTime, <s:property value="linkRealFlow.totalFlow"/>]);
sendFlows.push([startTime, <s:property value="linkRealFlow.sendFlow"/>]);
receiveFlows.push([startTime, <s:property value="linkRealFlow.receiveFlow"/>]);
sendPackets.push([startTime, <s:property value="linkRealFlow.sendLosedPacketNumber"/>]);
receivePackets.push([startTime, <s:property value="linkRealFlow.receiveLosedPacketNumber"/>]);
sendPacketRates.push([startTime, <s:property value="linkRealFlow.sendLosedPacketRate"/>]);
receivePacketRates.push([startTime, <s:property value="linkRealFlow.receiveLosedPacketRate"/>]);

function timeFormatter(val, axis) {
    var d = new Date(val);
    return d.toLocaleTimeString();
}

totalPlot = $.plot($("#placeholder"),
       [ { data: totalFlows, label: I18N.linkRealFlow.totaltraffic }],
       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
         yaxis: { min: 0, max: 4000},     
         grid: { hoverable: true},
         lines: { show: true},
         points: { show: true },
         legend: { position: 'ne' } });
flowPlot = $.plot($("#placeholder1"),
      [ { data: sendFlows, label: I18N.linkRealFlow.Sendtraffic }, { data: receiveFlows, label: I18N.linkRealFlow.Receivetraffic }],
      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
        yaxis: { min: 0, max: 4000},
        grid: { hoverable: true},
        lines: { show: true },
        points: { show: true },        
        legend: { position: 'ne' } });
        
losedPacketNumberPlot = $.plot($("#placeholder2"),
       [ { data: sendPackets, label: I18N.linkRealFlow.Senddroppedpackets }, { data: receivePackets, label: I18N.linkRealFlow.Receivepacketloss }],
       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
         yaxis: { min: 0, max: 400},     
         grid: { hoverable: true},
         lines: { show: true },
         points: { show: true },         
         legend: { position: 'ne' } });

losedPacketRatePlot = $.plot($("#placeholder3"),
      [ { data: sendPacketRates, label: I18N.linkRealFlow.Sendlostreportedrate }, { data: receivePacketRates, label: I18N.linkRealFlow.Receiverlostreportedrate }],
      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
        yaxis: { min: 0, max: 100},
        grid: { hoverable: true},
        legend: { position: 'ne' } });        

var previousPoint = null;
$("#placeholder").bind("plothover", plothover);
function plothover(event, pos, item) {
	showPlothover(event, pos, item, 0);
}

$("#placeholder1").bind("plothover", plothover1);
function plothover1(event, pos, item) {
	showPlothover(event, pos, item, 1);
}

$("#placeholder2").bind("plothover", plothover2);
function plothover2(event, pos, item) {
	showPlothover(event, pos, item, 2);
}

$("#placeholder3").bind("plothover", plothover3);
function plothover3(event, pos, item) {
	showPlothover(event, pos, item, 3);
}

function showPlothover(event, pos, item, index) {
  	if (item) {
          if (previousPoint != item.datapoint) {
              previousPoint = item.datapoint;
              $("#tooltip").remove();
              var x = item.datapoint[0].toFixed(2),
                  y = item.datapoint[1].toFixed(2);
              var d = new Date(parseInt(x));
              switch(index) {
              	case 0:
					showTooltip(item.pageX, item.pageY,
							I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.linkRealFlow.Totalflow + y + I18N.linkRealFlow.Kbitsec);              	
              		break;
				case 1:
					showTooltip(item.pageX, item.pageY,
							I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.linkRealFlow.flow + y + I18N.linkRealFlow.Kbitsec);				
              		break;
              	case 2:
					showTooltip(item.pageX, item.pageY,
							I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.linkRealFlow.Droppedpackets + y + I18N.ap.chart.single.numbers);              	
              		break;
				case 3:
					showTooltip(item.pageX, item.pageY,
							I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.linkRealFlow.Packetlossrate + y + "%");				
              		break;
				case 4:
					showTooltip(item.pageX, item.pageY,
							I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.label.cpu + y + "%");				
              		break;              		              		
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
