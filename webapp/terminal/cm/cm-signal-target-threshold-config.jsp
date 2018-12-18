<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jQuery
    library zeta
    module pnmp
    css css/ext-plugin
    import js/ext/ux/Portal
    import js/ext/ux/PortalColumn
    import js/ext/ux/Portlet 
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import terminal.cm.js.cm-target-threshold
</Zeta:Loader>
<style type="text/css">
.badColor {
	color: #E83C23;
}
.marginalColor {
	color: #f38900;
}
.healthColor {
	color: #449d44;
}
.configTable td{
	height: 29px;
	padding: 4px 3px;
}
</style>

<script type="text/javascript">

Ext.onReady(function() {
	var leftPortletItems = {
		columnWidth: .50,
        style:'padding:5px',
        items: [{
            id:'portlet-upTx',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: loadThresholdData
	        }],
            title: '@pnmp.upTxPower@',
            autoScroll:true,
            contentEl:'upTx-chart-container'
        }, {
            id: 'portlet-upSnr',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: loadThresholdData
	        }],
            title: '@pnmp.upSnr@',
            autoScroll: true,
            contentEl: 'upSnr-chart-container'
        }]
	};
	
	var rightPortletItems = {
		columnWidth: .50,
        style:'padding:5px',
        items: [{
            id:'portlet-downTx',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: loadThresholdData
	        }],
            title: '@pnmp.downRePower@',
            autoScroll:true,
            contentEl:'downTx-chart-container'
        }, {
            id: 'portlet-downSnr',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: loadThresholdData
	        }],
            title: '@pnmp.downSnr@',
            autoScroll: true,
            contentEl: 'downSnr-chart-container'
        }]
	};
	
	var portletItems = [leftPortletItems, rightPortletItems];
	
	var headerPanel = new Ext.BoxComponent({
		region: 'north', 
		el: 'header', 
		margins: '0 0 0 0', 
		height: 40, 
		maxSize: 40
	});
	var viewport = new Ext.Viewport({
		layout: 'border',
        items:[{
        	xtype: 'portal', 
        	region: 'center', 
        	margins: '0 0 0 0', 
        	border: false, 
        	items: portletItems
        }]
    });
});	
$(function() {
	loadThresholdData();
})
</script>
</head>
<body class="bodyGrayBg">
	<div class='formtip' id='tips' style="display: none"></div>
	<div id="upTx-chart-container">
		
		<div class="edgeTB10LR20">
			<table id="upSendPower-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td></td>
					<td><input class="normalInput" id='upSendPower_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dBmV</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='upSendPower_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dBmV</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.low@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='upSendPower_tooLow_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.high@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='upSendPower_tooHigh_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('upSendPower');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="upSnr-chart-container">
		<div class="edgeTB10LR20">
			<table id="upSnr-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#62;=</td>
					<td> <input class="normalInput" id='upSnr_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.noiseHigh@:</td>
					<td> &#60;</td>
					<td><input class="normalInput" id='upSnr_bad_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('upSnr');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="downTx-chart-container">
		<div class="edgeTB10LR20">
			<table id="downRePower-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td></td>
					<td><input class="normalInput" id='downRePower_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dBmV</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='downRePower_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dBmV</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.low@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='downRePower_tooLow_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.high@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='downRePower_tooHigh_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('downRePower');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="downSnr-chart-container">
		<div class="edgeTB10LR20">
			<table id="downSnr-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#62;=</td>
					<td> <input class="normalInput" id='downSnr_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.noiseHigh@:</td>
					<td> &#60;</td>
					<td><input class="normalInput" id='downSnr_bad_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dBmV </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('downSnr');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
</body>
</Zeta:HTML>