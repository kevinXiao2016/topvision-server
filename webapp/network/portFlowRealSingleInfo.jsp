<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"></link>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.graph-div {
	font-family: sans-serif;
	font-size: 16px;
}

.gray-a {
	color: gray;
	cursor: default;
}
</style>
<script language="javascript" type="text/javascript" src="../js/flot/jquery.js"></script>
<script language="javascript" type="text/javascript" src="../js/flot/jquery.flot.pack.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script language="javascript" type="text/javascript">
var refreshInterval = 20 * 1000;
var totalTime = 3600000;
var maxTimes = totalTime / refreshInterval;
var timer= null;
var portId = -1;

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
 	$.ajax({url: '../entity/getRealCpuUsage.tv', data: {entityId : portId},
  		success: scCallback, error: fnCallback, dataType: 'json', cache: false});
}

var startTime = 0;
var endTime = 0;
function scCallback(response) {
	if (response.responseText) {
		var json = Ext.util.JSON.decode(response.responseText);
		if (json.time >= endTime) {
			cpu = [];
			mem = [];
			startTime = json.time;
			endTime = startTime + maxTimes * refreshInterval;
			//cpuPlot.getAxes().xaxis.min = memPlot.getAxes().xaxis.min = startTime;
			//cpuPlot.getAxes().xaxis.max = memPlot.getAxes().xaxis.max = endTime;
			
			plot = $.plot($("#placeholder"),
			       [ { data: cpu, label: I18N.NETWORK.cpuUsage+"(%)" }],
			       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
			         yaxis: { min: 0, max: 100},     
			         grid: { hoverable: true},
			         legend: { position: 'ne' } });
			plot1 = $.plot($("#placeholder1"),
			      [ { data: mem, label: I18N.portFlowRealCompareInfo.usedMem+"(K)" }],
			      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
			        yaxis: { min: 0, max: memTotal},
			        grid: { hoverable: true},
			        legend: { position: 'ne' } });
		}
		cpu.push([json.time, json.cpu]);
		mem.push([json.time, json.mem]);
        plot.draw();
        plot1.draw();
	}
}
function fnCallback(response) {
}

function startPortAnalyse(portIndex, desc) {
	if (portId == portIndex) {
		return;
	}
	portId = portIndex;
	Zeta$('portInfo').innerHTML = I18N.port.ifIndex+'&nbsp;&nbsp;' + portIndex + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+I18N.portFlowRealData.Description+':&nbsp;&nbsp;' + desc;
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;" onload="doOnload();"
	onunload="doOnunload();">
	<table width=100%>
		<tr>
			<td colspan=2><a
				href="showPortFlowInfo.tv?entityId=<s:property value="entityId"/>&ip=<s:property value="ip"/>"><fmt:message key="portFlowRealCompareInfo.portData" bundle="${resources}" /></a>
				<a style="margin-left: 10px"><b><fmt:message key="portFlowRealCompareInfo.Portanalysis" bundle="${resources}" /></b>
			</a> <a style="margin-left: 10px"
				href="showPortRealCompareInfo.tv?entityId=<s:property value="entityId"/>&ip=<s:property value="ip"/>"><fmt:message key="portFlowRealCompareInfo.ComparativeAnalysisofmulti-port" bundle="${resources}" /></a>
			</td>
		</tr>
		<tr>
			<td valign=top width=200px>
				<div style="margin-top: 8px;">
					<fmt:message key="portFlowRealCompareInfo.portlist" bundle="${resources}" /><br>
				</div>
				<div class=PANEL-CONTAINER style="margin-top: 3px; width: 200px;">
					<table height=600px>
						<s:iterator value="ports">
							<s:if test="ifOperStatus==1">
								<tr>
									<td width=20px align=center><img src="../images/up.gif"
										border=0 alt="UP">
									</td>
									<td width=30px><s:property value="ifIndex" />
									</td>
									<td><a title="<s:property value="ifTypeString"/>"
										href="javascript: startPortAnalyse(<s:property value="ifIndex"/>, '<s:property value="ifDescr"/>');"><s:property
												value="ifDescr" />
									</a>
									</td>
								</tr>
							</s:if>
							<s:else>
								<tr>
									<td width=20px align=center><img src="../images/down.gif"
										border=0 alt="DOWN">
									</td>
									<td width=30px><s:property value="ifIndex" />
									</td>
									<td class=gray-a title="<s:property value="ifTypeString"/>">
										<s:property value="ifDescr" />
									</td>
								</tr>
							</s:else>
						</s:iterator>
						<tr>
							<td colspan=3 valign=top height=100%>&nbsp;</td>
						</tr>
					</table>
				</div></td>
			<td width=20px>&nbsp;</td>
			<td align=center valign=top>
				<div id="portInfo" style="width: 600px; margin-bottom: 5px;">&nbsp;</div>
				<div id="placeholder" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br />
				<div id="placeholder1" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br />
				<div id="placeholder2" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br />
				<div id="placeholder3" class=graph-div
					style="width: 600px; height: 250px;"></div> <br />
			<br /></td>
		</tr>
	</table>

	<script language="javascript" type="text/javascript">
var cpu = []
var mem = [];
var plot = null;
var plot1 = null;
var memTotal = 4000000;

startTime = <s:property value="synTime"/>;
endTime = startTime + maxTimes * refreshInterval;
cpu.push([startTime, 10]);
mem.push([startTime, 10]);

function timeFormatter(val, axis) {
	var d = new Date(val);
	return d.toLocaleTimeString();
}

plot = $.plot($("#placeholder"),
       [ { data: cpu, label: I18N.linkRealFlow.totaltraffic }],
       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
         yaxis: { min: 0, max: 40000},     
         grid: { hoverable: true},
         legend: { position: 'ne' } });
plot1 = $.plot($("#placeholder1"),
      [ { data: mem, label: I18N.portFlowRealCompareInfo.Sendandreceivetraffic }],
      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
        yaxis: { min: 0, max: memTotal},
        grid: { hoverable: true},
        legend: { position: 'ne' } });
var cpuPlot1 = $.plot($("#placeholder2"),
       [ { data: cpu, label: I18N.portFlowRealCompareInfo.sendandreceivepacketloss }],
       { xaxis: { mode: 'time',  min: startTime, max: endTime, tickFormatter: timeFormatter},
         yaxis: { min: 0, max: 40000},     
         grid: { hoverable: true},
         legend: { position: 'ne' } });
var memPlot1 = $.plot($("#placeholder3"),
      [ { data: mem, label: I18N.portFlowRealCompareInfo.Sendandreceivepackets }],
      { xaxis: { mode: 'time', min: startTime, max: endTime, tickFormatter: timeFormatter},
        yaxis: { min: 0, max: memTotal},
        grid: { hoverable: true},
        legend: { position: 'ne' } });        

var previousPoint = null;
$("#placeholder").bind("plothover", function (event, pos, item) {
	doPlothover(event, pos, item, 0);
}); 

$("#placeholder1").bind("plothover", function (event, pos, item) {
      doPlothover(event, pos, item, 1);
});           

function  doPlothover(event, pos, item, index) {
	if (item) {
          if (previousPoint != item.datapoint) {
              previousPoint = item.datapoint;
              $("#tooltip").remove();
              var x = item.datapoint[0].toFixed(2),
                  y = item.datapoint[1].toFixed(2);
              var d = new Date(parseInt(x));
              
              if (index == 0) {
              	showTooltip(item.pageX, item.pageY,
              			I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br>  "+ I18N.label.cpu+ y + "%");
              } else if (index == 1) {
					showTooltip(item.pageX, item.pageY,
						I18N.linkRealFlow.Time + d.toLocaleTimeString() + "<br> "+I18N.portFlowRealCompareInfo.usedMem+": " + y + "K");              
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
