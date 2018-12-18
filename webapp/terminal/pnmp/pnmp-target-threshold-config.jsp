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
    import terminal.pnmp.js.pnmp-target-threshold
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
        items: [
//            {
//            id:'portlet-mtc',
//            tools: [{
//	        	id: 'refresh',
//		        qtip:'@COMMON.refresh@',
//	            handler: loadThresholdData
//	        }],
//            title: '@pnmp.mtc@',
//            autoScroll:true,
//            contentEl:'mtc-chart-container'
//        },
        {
            id:'portlet-mtr',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
		        handler: loadThresholdDataMtr
	        }],
            title: '@pnmp.mtr@',
            autoScroll:true,
            contentEl:'mtr-chart-container'
        },{
            id:'portlet-preMTTER',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
		        handler: loadThresholdDataPre
	        }],
            title: '@pnmp.premtter@',
            autoScroll:true,
            contentEl:'preMTTER-chart-container'
        }, {
                id:'portlet-PPESR',
                tools: [{
                    id: 'refresh',
                    qtip:'@COMMON.refresh@',
                    handler: loadThresholdDataPPESR
                }],
                title: '@pnmp.ppesr@',
                autoScroll:true,
                contentEl:'PPESR-chart-container'
            }/* , {
        	id: 'portlet-mrLevel',
        	tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
		        handler: loadThresholdData
	        }],
            title: '@pnmp.mrLevel@',
            autoScroll: true,
            contentEl: 'mrLevel-chart-container'
        } */]
	};
	
	var rightPortletItems = {
		columnWidth: .50,
        style:'padding:5px',
        items: [{
            id: 'portlet-nmter',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
		        handler: loadThresholdDataNmter
	        }],
            title: '@pnmp.nmter@',
            autoScroll: true,
            contentEl: 'nmter-chart-container'
        }, {
            id: 'portlet-postMTTER',
            tools: [{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
		        handler: loadThresholdDataPost
	        }],
            title: '@pnmp.postmtter@',
            autoScroll: true,
            contentEl: 'postMTTER-chart-container'
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
	<div id="mtr-chart-container">
		<div class="openWinHeader" style="height: 80px;">
		    <div class="openWinTip">
		    	<p>MTR, Main Tap Ratio</p>
		    	<p>@pnmp.mtrDesc@</p>
		    	<p class="marginalColor">@pnmp.marginalDesc@</p>
		    	<p class="badColor">@pnmp.badDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="mtr-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#62;</td>
					<td> <input class="normalInput" id='mtr_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@" />dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginal@:</td>
					<td></td>
					<td><input class="normalInput" id='mtr_marginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='mtr_marginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.bad@:</td>
					<td> &#60;</td>
					<td><input class="normalInput" id='mtr_bad_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('mtr');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="mtc-chart-container" style="display: none">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>MTC: Main Tap Compression</p>
		    	<p>@pnmp.mtcDesc@</p>
		    	<p class="badColor">@pnmp.mtcBadDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="mtc-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#60;=</td>
					<td> <input class="normalInput" id='mtc_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.irreparable@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='mtc_bad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('mtc');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="nmter-chart-container">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>NMTTER, Not Main Tap to Total Energy Ratio</p>
		    	<p>@pnmp.nmtterDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="nmtter-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='nmtter_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginalDistortion@:</td>
					<td></td>
					<td><input class="normalInput" id='nmtter_marginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='nmtter_marginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.badDistortion@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='nmtter_bad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('nmtter');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="preMTTER-chart-container">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>PreMTTER, Pre-Main Tap to Total Energy Ratio</p>
		    	<p>@pnmp.premtterDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="premtter-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='premtter_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginalDelay@:</td>
					<td></td>
					<td><input class="normalInput" id='premtter_marginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='premtter_marginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.badDelay@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='premtter_bad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('premtter');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="postMTTER-chart-container">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>PostMTTER, Post-Main Tap to Total Energy Ratio</p>
		    	<p>@pnmp.postmtterDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="postmtter-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='postmtter_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginalMrLevel@:</td>
					<td></td>
					<td><input class="normalInput" id='postmtter_marginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='postmtter_marginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.badMrLevel@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='postmtter_bad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('postmtter');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="PPESR-chart-container">
		<div class="openWinHeader" style="height: 100px;">
		    <div class="openWinTip">
		    	<p>PPESR, Pre-Post Energy Symmetry Ratio</p>
		    	<p>@pnmp.ppesrDesc1@</p>
		    	<p>@pnmp.ppesrDesc2@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="ppesr-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="badColor">@pnmp.badMrLeveltrend@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='ppesr_mrlevelBad_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginalMrLevelTrend@:</td>
					<td></td>
					<td><input class="normalInput" id='ppesr_mrlevelMarginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='ppesr_mrlevelMarginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="healthColor">@pnmp.noTrend@:</td>
					<td></td>
					<td><input class="normalInput" id='ppesr_health_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='ppesr_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginalGroupDelay@:</td>
					<td></td>
					<td><input class="normalInput" id='ppesr_delayMarginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='ppesr_delayMarginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.badGroupDelay@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='ppesr_delayBad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('ppesr');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<%-- <div id="mrLevel-chart-container">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p class="marginalColor">@pnmp.marginalDesc@</p>
		    	<p class="badColor">@pnmp.badDesc@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table id="mrlevel-table" class="configTable" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="healthColor">@pnmp.health@:</td>
					<td width="10"> &#60;</td>
					<td> <input class="normalInput" id='mrlevel_health_highValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td class="marginalColor">@pnmp.marginal@:</td>
					<td></td>
					<td><input class="normalInput" id='mrlevel_marginal_lowValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
					<td width="10"> ~ </td>
					<td><input class="normalInput" id='mrlevel_marginal_highValue' maxlength=7  tooltip="@pnmp.inputDesc1@"/>dB</td>
				</tr>
				<tr>
					<td class="badColor">@pnmp.bad@:</td>
					<td> &#62;</td>
					<td><input class="normalInput" id='mrlevel_bad_lowValue' maxlength=7  tooltip="@pnmp.inputDesc2@"/>dB </td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><a id="simple-query" href="javascript:saveClick('mrlevel');" class="normalBtn" onclick=""><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div> --%>
</body>
</Zeta:HTML>