<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
</Zeta:Loader>
<style type="text/css">
.color-area { border:1px solid #333;}
</style>
<script type="text/javascript" src="../js/ext/ux/SliderTip.js"></script>
<script type="text/javascript">
var second = '@label.seconds@';
var minute = '@label.minutes@';
var Kbps = 1024;
var Mbps = 1024 * Kbps;
var Gbps = 1024 * Mbps;
var RATE_CONSTANTS = {name: ['2G', '1G', '800M', '500M', '200M', '100M', '50M', '20M',
							 '10M', '5M', '2M', '1M', '512K', '256K', '128K', '100K',
							 '64K', '32K', '16K', '1K', '0'],
					  value: [2*Gbps, Gbps, 800*Mbps, 500*Mbps, 200*Mbps, 100*Mbps,
					  		 50*Mbps, 20*Mbps, 10*Mbps, 5*Mbps, 2*Mbps, Mbps, 512*Kbps, 256*Kbps,
					  		 128*Kbps, 100*Kbps, 64*Kbps, 32*Kbps, 16*Kbps, Kbps, 0]};

var folderId = '<s:property value="topoFolder.folderId"/>';
var refreshInterval = <s:property value="topoFolder.refreshInterval"/>;
var entityLabel = '<s:property value="topoFolder.entityLabel"/>';
var linkLabel = '<s:property value="topoFolder.linkLabel"/>';

var slider;

var curEntityLabel = entityLabel;
var curLinkLabel = null;

var cpuLabels = [];
var cpuColors = [];

var memLabels = [];
var memColors = [];

var flowLabels = [];
var flowColors = [];

var rateLabels = [];
var rateColors = [];

function selectColor0() {
	selectColor(0);
}
function selectColor1() {
	selectColor(1);
}
function selectColor2() {
	selectColor(2);
}
function selectColor3() {
	selectColor(3);
}
function selectColor4() {
	selectColor(4);
}

function selectRateColor0() {
	selectRateColor(0);
}
function selectRateColor1() {
	selectRateColor(1);
}
function selectRateColor2() {
	selectRateColor(2);
}
function selectRateColor3() {
	selectRateColor(3);
}

function selectCpuColor0() {
	selectCpuColor(0);
}
function selectCpuColor1() {
	selectCpuColor(1);
}
function selectCpuColor2() {
	selectCpuColor(2);
}
function selectCpuColor3() {
	selectCpuColor(3);
}

function selectColor(index) {
	window.top.createDialog('colorDlg', '@topoLabel.colorPicker@', 320, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			Zeta$('color' + index).style.backgroundColor = color;
		}
	});
}

function selectColor(index) {
	window.top.createDialog('colorDlg', '@topoLabel.colorPicker@', 320, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			Zeta$('cpuColor' + index).style.backgroundColor = color;
		}
	});
}

function selectRateColor(index) {
	window.top.createDialog('colorDlg', '@topoLabel.colorPicker@', 320, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.top.ZetaCallback != null && window.top.ZetaCallback.type == 'colorpicker') {
			var color = window.top.ZetaCallback.color;
			Zeta$('ratecolor' + index).style.backgroundColor = color;
		}
	});
}

function applyLinkLabel() {
	var flag = Zeta$('flowLabel').checked;
	if (flag) {
		for (var i = 1; i < 4; i++) {
			var el = Zeta$('flow' + (i - 1));
			var el1 = Zeta$('flow' + i);
			var l = parseInt(el.options[el.selectedIndex].value);
			var l1 = parseInt(el1.options[el1.selectedIndex].value);
			if (l1 > l) {
				window.top.showMessageDlg('@COMMON.tip@',  '@topoLabel.tip.bandwidth@');
				return;
			}	
		}	
	} else {
		for (var i = 1; i < 4; i++) {
			var el = Zeta$('rate' + (i - 1));
			var el1 = Zeta$('rate' + i);
			var l = parseInt(el.options[el.selectedIndex].value);
			var l1 = parseInt(el1.options[el1.selectedIndex].value);
			if (l1 > l) {
				window.top.showMessageDlg('@COMMON.tip@', '@topoLabel.tip.speed@');
				return;
			}	
		}	
	}

	if(flag) {
		curLinkLabel = 'linkflow';
		for (var i = 0; i < 4; i++) {
			var el = Zeta$('flow' + i);
			flowLabels[i] = el.options[el.selectedIndex].value;
			flowColors[i] = Zeta$('color' + i).style.backgroundColor;
		}		
	} else {
		curLinkLabel = 'linkrate';
		for (var i = 0; i < 4; i++) {
			var el = Zeta$('rate' + i);
			rateLabels[i] = el.options[el.selectedIndex].value;
			rateColors[i] = Zeta$('ratecolor' + i).style.backgroundColor;
		}		
	}
	
	$.ajax({url: 'updateTopoLabel.tv', type: 'POST', 
		data: {labelValues : (flag ? flowLabels : rateLabels),
			   labelColors : (flag ? flowColors : rateColors),
			   labelType: curLinkLabel, folderId: folderId},
		success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame != null) {
				try {
					frame.linkLabel = curLinkLabel;
				} catch (err) {
				}
			}
			window.top.showMessageDlg('@COMMON.tip@', '@topoLabel.tip.lineMarkModifySuccess@');
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}

function applyEntityLabel() {
	for (var i = 1; i < 4; i++) {
		var el = Zeta$('cpu' + (i - 1));
		var el1 = Zeta$('cpu' + i);
		var l = parseInt(el.options[el.selectedIndex].value);
		var l1 = parseInt(el1.options[el1.selectedIndex].value);		
		if (l1 > l) {
			//window.top.showMessageDlg('@COMMON.tip@', '@topoLabel.tip.usage@');
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '@topoLabel.tip.usage@'
			});
			return;
		}	
	}

	var flag = Zeta$('cpuLabel').checked;  
	if(flag) {
		curEntityLabel = 'cpu';	
		for (var i = 0; i < 4; i++) {
			var el = Zeta$('cpu' + i);
			cpuLabels[i] = el.options[el.selectedIndex].value;
			cpuColors[i] = Zeta$('cpuColor' + i).style.backgroundColor;
		}
	} else {
		curEntityLabel = 'mem';	
		for (var i = 0; i < 4; i++) {
			var el = Zeta$('cpu' + i);
			memLabels[i] = el.options[el.selectedIndex].value;
			memColors[i] = Zeta$('cpuColor' + i).style.backgroundColor;
		}
	}

	$.ajax({url: 'updateTopoLabel.tv', type: 'POST', 
		data: {labelValues : (flag ? cpuLabels : memLabels), 
			   labelColors : (flag ? cpuColors : memColors),
			   labelType: curEntityLabel, folderId: folderId},
		success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame != null) {
				try {
					frame.entityLabel = curEntityLabel;
				} catch (err) {
				}
			}
			//window.top.showMessageDlg('@COMMON.tip@', '@topoLabel.tip.deviceMarkModifySuccess@');
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '@topoLabel.tip.deviceMarkModifySuccess@'
			});
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}

function resetEntityLabel() {
	var clickId = $(':radio["utilityLabel"]:checked').attr('id');
	switch(clickId){
		case 'cpuLabel':
			resetCPU();
			break;
		case 'memLabel':
			resetMEM();
			break;
	}
}
function resetCPU(){
	cpuLabels[0] = 80;
	cpuLabels[1] = 70;
	cpuLabels[2] = 60;
	cpuLabels[3] = 0;

	cpuColors[0] = '#ff0000';
	cpuColors[1] = '#ff8000';
	cpuColors[2] = '#88e4e5';
	cpuColors[3] = '#96e54d';
	
	curEntityLabel = null;
	Zeta$('cpuLabel').checked = true;
	setCurEntityLabel('cpu');
}
function resetMEM(){
	memLabels[0] = 80;
	memLabels[1] = 50;
	memLabels[2] = 30;
	memLabels[3] = 0;

	memColors[0] = '#ff0000';
	memColors[1] = '#ff8000';
	memColors[2] = '#88e4e5';
	memColors[3] = '#96e54d';
	
	curEntityLabel = null;
	Zeta$('memLabel').checked = true;
	setCurEntityLabel('mem');
}


function resetLinkLabel() {
	rateLabels[0] = 100*Mbps;
	rateLabels[1] = 10*Mbps;
	rateLabels[2] = Mbps;
	rateLabels[3] = 0;

	rateColors[0] = '#ff0000';
	rateColors[1] = '#ff8000';
	rateColors[2] = '#0000ff';
	rateColors[3] = '#14a600';

	curLinkLabel = null;
	Zeta$('rateLabel').checked = true;
	setCurLinkLabel('linkrate');
}

function setCurEntityLabel(label) {
	curEntityLabel = label;
	if(curEntityLabel == 'cpu') {
		for (var i = 0; i < 4; i++) {
			Zeta$('cpu' + i).selectedIndex = cpuLabels[i];
			Zeta$('cpuColor' + i).style.backgroundColor = cpuColors[i];
		}
	} else {
		for (var i = 0; i < 4; i++) {
			Zeta$('cpu' + i).selectedIndex = memLabels[i];
			Zeta$('cpuColor' + i).style.backgroundColor = memColors[i];
		}	
	}
}

function setCurLinkLabel(label) {
	curLinkLabel = label;
	if (curLinkLabel == 'linkflow') {
		Zeta$('flow-div').style.display = '';
		Zeta$('rate-div').style.display = 'none';
	} else {
		Zeta$('flow-div').style.display = 'none';
		Zeta$('rate-div').style.display = '';
	}

	if (curLinkLabel == 'linkflow') {
		for (var i = 0; i < 4; i++) {
			Zeta$('flow' + i).selectedIndex = flowLabels[i];
			Zeta$('color' + i).style.backgroundColor = flowColors[i];
		}
	} else {
		for (var i = 0; i < 4; i++) {
			var el = Zeta$('rate' + i);
			var options = el.options;
			for (var j = 0; j < options.length; j++) {
				if (options[j].value == rateLabels[i]) {
					el.selectedIndex = j;
					break;
				}
			}
			Zeta$('ratecolor' + i).style.backgroundColor = rateColors[i];
		}	
	}
}

function doOnload() {
	<s:iterator value="flowLabels">
	flowLabels[flowLabels.length] = <s:property value="value"/>;
	flowColors[flowColors.length] = '<s:property value="color"/>';
	</s:iterator>
	
	<s:iterator value="rateLabels">
	rateLabels[rateLabels.length] = <s:property value="value"/>;
	rateColors[rateColors.length] = '<s:property value="color"/>';
	</s:iterator>	
	
	<s:iterator value="cpuLabels">
	cpuLabels[cpuLabels.length] = <s:property value="value"/>;
	cpuColors[cpuColors.length] = '<s:property value="color"/>';
	</s:iterator>
	
	<s:iterator value="memLabels">
	memLabels[memLabels.length] = <s:property value="value"/>;
	memColors[memColors.length] = '<s:property value="color"/>';
	</s:iterator>
	
	if (entityLabel == 'cpu') {
		Zeta$('cpuLabel').checked = true;
	} else {
		Zeta$('memLabel').checked = true;
	}

	if (linkLabel == 'linkflow') {
		Zeta$('flowLabel').checked = true;
	} else {
		Zeta$('rateLabel').checked = true;
	}

	setTimeout(function() {
		setCurLinkLabel(linkLabel);
		setCurEntityLabel(entityLabel);
	}, 500);
}

function setRefreshMapInterval(i) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.refreshInterval=' + i;
	$.ajax({url: '../topology/updateFolderRefreshInterval.tv', type: 'POST', 
		data: params, success: function() {
			var frame = window.top.getFrame('topo' + folderId);
			if (frame != null) {
				frame.setMapRefreshInterval(i, folderId);
			}
		}, error: function() {
		}, dataType: 'plain', cache: false});
}

function resetInterval() {
	//slider.setValue(60);
	Zeta$('mapRefreshBox').value = '60';
	refreshInterval = 60000;
	setRefreshMapInterval(refreshInterval);
	$("#mapRefreshBox").removeClass("normalInputRed");
}

Ext.onReady(function(){
	Zeta$('mapRefreshBox').value = refreshInterval/1000;
	setRefreshMapInterval(refreshInterval);
	//$("#mapRefreshBox").attr("toolTip",tip);
    /* var tip = new Ext.ux.SliderTip({
        getText: function(slider){
            return String.format('<b>{0} @label.seconds@</b>', slider.getValue());
        }
    });
    slider = new Ext.Slider({
        renderTo: 'tip-slider',
        width: 190,
        value: (refreshInterval/1000),
        minValue: 15,
        maxValue: 180,
        plugins: tip,
        listeners: {
        	changecomplete: function(slider, newValue) {
        		Zeta$('mapRefreshBox').value = newValue + '@label.seconds@'; 
        		refreshInterval = newValue * 1000;
        		setRefreshMapInterval(refreshInterval);
        	}
        }
    }); */
    doOnload();
});

$(function(){	
	$("#form1").validate({			
		//onfocusout :false,
		rules:{
			mapRefreshBox:{required:true,range:[15,180],digits:true}
		},
		highlight: function(element, errorClass) {  		
			$(element).addClass("normalInputRed");
		},
		unhighlight: function(element, errorClass) {  
			$(element).removeClass("normalInputRed");
		}
	});//end validate;	
	
	//应用刷新频率;
	$("#submitRefreshBtn").click(function(){		
		var form = $("#form1")
		if(form.valid()){//验证通过;
			refreshInterval = $("#mapRefreshBox").val()*1000;
			//window.top.getFrame("topo"+folderId).setMapRefreshInterval(refreshInterval, folderId);
			setRefreshMapInterval(refreshInterval);
			//window.top.showMessageDlg('@COMMON.tip@', '@topoLabel.tip.Success@');
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '@topoLabel.tip.Success@'
			});
		}else{//验证不通过;
			$("#mapRefreshBox").focus();
		}
	})
});//end document.ready;



</script>
</head>
<body class="sideMapBg">
	<div class="edge10">
		<form id="form1" name="form1" >
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
		        	<tr>
		            	<th colspan="3" class="txtLeftTh">
		            		@topoLabel.refreshRate@
		            	</th>
		        	</tr>
		    	</thead>
		    		<tr>
			    		<td width="80" align="right">@topoLabel.refreshRate@@COMMON.maohao@</td>
			    		<td width="100">
			    			<input id="mapRefreshBox" name="mapRefreshBox" type=text class="normalInput" maxlength="3" style="width: 40px; text-align:center;" toolTip='@label.inputRange@' />
			    			 @label.seconds@
			    		</td>
			    		<td>
			    			<ul class="leftFloatUl">
			    				<li>
			    					<a href="javascript:;" class="normalBtn" id="submitRefreshBtn"><span><i class="miniIcoSave"></i>@topoLabel.apply@</span></a>  
			    				</li>
			    				<li>
			    					<a onclick="resetInterval()" href="javascript:;" class="normalBtn"><span><i class="miniIcoBack"></i>@RESOURCES/SYSTEM.Reset@</span></a>
			    				</li>
			    			</ul>
			    		</td>
		    		</tr>
				<tbody>
				</tbody>
			</table>
		</form>
		<div class="pT10">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		    	<thead>
		        	<tr>
		        		<th colspan="5" class="txtLeftTh">
		        			@topoLabel.deviceMark@
		        		</th>
		        	</tr>
		        </thead>
		        <tbody>
		        	<tr>
		        		<td colspan="5">
		        			<input type=radio id="cpuLabel" name="utilityLabel"
									onclick="setCurEntityLabel('cpu');" /><label
									for="cpuLabel">@NETWORK.cpuUsage@(%)</label> &nbsp;&nbsp;<input type=radio
									id="memLabel" name="utilityLabel"
									onclick="setCurEntityLabel('mem');" /><label
									for="memLabel">@NETWORK.memUsage@(%)</label>
		        		</td>
		        	</tr>
		        	<tr>
		        		<td colspan="5">@topoLabel.useDefaultColor@</td>
		        	</tr>
		        	<tr>
		        		<td>@greater@</td>
						<td><select id="cpu0" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor0()"><div id="cpuColor0"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor0()"><span>@RESOURCES/COMMON.select@...</span></a> 
						</td>
		        	</tr>
		        	<tr>
						<td>@greater@</td>
						<td><select id="cpu1" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="cpuColor1"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor1()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
					<tr>
						<td>@greater@</td>
						<td><select id="cpu2" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="cpuColor2"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor2()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
					<tr>
						<td>@greater@</td>
						<td><select id="cpu3" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="cpuColor3"
								class="color-area" style="cursor: pointer; width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor3()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
		        	<tr>
		        		<td colspan="5">
		        			<div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
							         <li><a href="javascript:;" class="normalBtn" onclick="applyEntityLabel()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
							         <li><a href="javascript:;" class="normalBtn" onclick="resetEntityLabel()"><span><i class="miniIcoBack"></i>@RESOURCES/SYSTEM.Reset@</span></a></li>
							     </ol>
							</div>
		        		</td>
		        	</tr>
		        </tbody>
		    </table>	
	    </div>
	    <div class="pT10" style="display:none;">
	    	<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
		    	<thead>
		        	<tr>
		        		<th colspan="5" class="txtLeftTh">
		        			@NETWORK.linkLabel@
		        		</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="5">
							<input type=radio checked id="flowLabel"
							name="flowUtility" onclick="setCurLinkLabel('linkflow');" /><label
							for="flowLabel">@topoLabel.bandwidthUsage@(%)</label> &nbsp;&nbsp;<input type=radio
							id="rateLabel" name="flowUtility"
							onclick="setCurLinkLabel('linkrate');" /><label
							for="rateLabel">@topoLabel.speedRate@(bps)</label>
						</td>
					</tr>
					<tr>
						<td colspan="5">@topoLabel.useDefaultColor2@</td>
					</tr>
				</tbody>
				<tbody id="flow-div" style="display: none">
					<tr>
						<td>@greater@</td>
						<td><select id="flow0" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor0()"><div id="color0"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor0()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
					<tr>
						<td>@greater@</td>
						<td><select id="flow1" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="color1"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor1()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
					<tr>
						<td>@greater@</td>
						<td><select id="flow2" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="color2"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectColor2()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
					<tr>
						<td>@greater@</td>
						<td><select id="flow3" style="width: 75px">
								<%
								for (int i = 0; i < 100; i++) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
								}
								%>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="color3"
								class="color-area" style="cursor: pointer; width: 75px;"></div>
						</td>
						<td>
							<a onclick="selectColor3()" href="javascript:;" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>
				</tbody>
				<tbody  id="rate-div" style="display: none">
					<tr>
						<td>@greater@</td>
						<td><select id="rate0" style="width: 75px">
								<option value="2147483648">2G</option>
								<option value="1073741824">1G</option>
								<option value="838860800">800M</option>
								<option value="524288000">500M</option>
								<option value="209715200">200M</option>
								<option value="104857600">100M</option>
								<option value="52428800">50M</option>
								<option value="20971520">20M</option>
								<option value="10485760">10M</option>
								<option value="5242880">5M</option>
								<option value="2097152">2M</option>
								<option value="1048576">1M</option>
								<option value="524288">512K</option>
								<option value="262144">256K</option>
								<option value="131072">128K</option>
								<option value="102400">100K</option>
								<option value="65536">64K</option>
								<option value="32768">32K</option>
								<option value="16384">16K</option>
								<option value="1024">1K</option>
								<option value="0">0</option>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor0()"><div id="ratecolor0"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a href="javascript:;" class="normalBtn" onclick="selectRateColor0()"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>

					<tr>
						<td>@greater@</td>
						<td><select id="rate1" style="width: 75px">
								<option value="2147483648">2G</option>
								<option value="1073741824">1G</option>
								<option value="838860800">800M</option>
								<option value="524288000">500M</option>
								<option value="209715200">200M</option>
								<option value="104857600">100M</option>
								<option value="52428800">50M</option>
								<option value="20971520">20M</option>
								<option value="10485760">10M</option>
								<option value="5242880">5M</option>
								<option value="2097152">2M</option>
								<option value="1048576">1M</option>
								<option value="524288">512K</option>
								<option value="262144">256K</option>
								<option value="131072">128K</option>
								<option value="102400">100K</option>
								<option value="65536">64K</option>
								<option value="32768">32K</option>
								<option value="16384">16K</option>
								<option value="1024">1K</option>
								<option value="0">0</option>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="ratecolor1"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
							<a onclick="selectRateColor1()" href="javascript:;" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>

					<tr>
						<td>@greater@</td>
						<td><select id="rate2" style="width: 75px">
								<option value="2147483648">2G</option>
								<option value="1073741824">1G</option>
								<option value="838860800">800M</option>
								<option value="524288000">500M</option>
								<option value="209715200">200M</option>
								<option value="104857600">100M</option>
								<option value="52428800">50M</option>
								<option value="20971520">20M</option>
								<option value="10485760">10M</option>
								<option value="5242880">5M</option>
								<option value="2097152">2M</option>
								<option value="1048576">1M</option>
								<option value="524288">512K</option>
								<option value="262144">256K</option>
								<option value="131072">128K</option>
								<option value="102400">100K</option>
								<option value="65536">64K</option>
								<option value="32768">32K</option>
								<option value="16384">16K</option>
								<option value="1024">1K</option>
								<option value="0">0</option>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="ratecolor2"
								class="color-area" style="width: 75px;"></div>
						</td>
						<td>
								<a onclick="selectRateColor2()" href="javascript:;" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a>
						</td>
					</tr>

					<tr>
						<td>@greater@</td>
						<td><select id="rate3" style="width: 75px">
								<option value="2147483648">2G</option>
								<option value="1073741824">1G</option>
								<option value="838860800">800M</option>
								<option value="524288000">500M</option>
								<option value="209715200">200M</option>
								<option value="104857600">100M</option>
								<option value="52428800">50M</option>
								<option value="20971520">20M</option>
								<option value="10485760">10M</option>
								<option value="5242880">5M</option>
								<option value="2097152">2M</option>
								<option value="1048576">1M</option>
								<option value="524288">512K</option>
								<option value="262144">256K</option>
								<option value="131072">128K</option>
								<option value="102400">100K</option>
								<option value="65536">64K</option>
								<option value="32768">32K</option>
								<option value="16384">16K</option>
								<option value="1024">1K</option>
								<option value="0">0</option>
						</select>
						</td>
						<td>, @GRAPH.strokeColor@</td>
						<td onclick="selectColor3()"><div id="ratecolor3"
								class="color-area" style="cursor: pointer; width: 75px;"></div>
						</td>
						<td>
								<a onclick="selectRateColor3()" href="javascript:;" class="normalBtn"><span>@RESOURCES/COMMON.select@...</span></a> 
						</td>
					</tr>
				</tbody>
				<tbody>
					<tr>
						<td colspan="5">
							<div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
							         <li><a href="javascript:;" class="normalBtn" onclick="applyLinkLabel()"><span>@topoLabel.apply@</span></a></li>
							         <li><a href="javascript:;" class="normalBtn" onclick="resetLinkLabel()"><span>@RESOURCES/SYSTEM.Reset@</span></a></li>
							     </ol>
							</div>
						</td>
					</tr>
				</tbody>
	    	</table>
	    </div>
	    <div class="pT10">
	    	<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			     <thead>
			         <tr>
			             <th colspan="4" class="txtLeftTh">@topoLabel.warnIcon@</th>
			         </tr>
			     </thead>
			     <tbody>
			     	<tr>
						<td width="16"><img src="../images/fault/level6.gif" border=0 class="mL10"
							align=absmiddle />
						</td>
						<td class="">@GRAPH.five@</td>
						<td width="16"><img src="../images/fault/level5.gif" border=0
									align=absmiddle />
						</td>
						<td class="">@GRAPH.four@</td>
					</tr>
					<tr>
						<td><img src="../images/fault/level4.gif" border=0 class="mL10"
							align=absmiddle />
						</td>
						<td class="">@Major@</td>
						<td><img src="../images/fault/level3.gif" border=0
							align=absmiddle />
						</td>
						<td class="">@Minor@</td>
					</tr>
					<tr>
						<td><img src="../images/fault/level2.gif" border=0 class="mL10"
							align=absmiddle />
						</td>
						<td class="">@Warning@</td>
						<td><img src="../images/fault/level1.gif" border=0
							align=absmiddle />
						</td>
						<td class="">@Information@</td>
					</tr>
					<tr>
						<td><img src="../images/fault/level0.gif" border=0 align=absmiddle />
						</td>
						<td class=""  colspan="3">@topoLabel.noAlert@</td>
					</tr>
				 </tbody>
			 </table>
	    	
	    </div>
	</div>
</body>
</Zeta:HTML>
