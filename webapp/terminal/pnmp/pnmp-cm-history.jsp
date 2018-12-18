<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library zeta
    library jQuery
    plugin Portlet
    module pnmp
    css css/ext-plugin
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import js/jquery/zebra
    IMPORT js/components/segmentButton/SegmentButton
    import terminal/pnmp/js/pnmp-cm-history
    import terminal/pnmp/js/pnmpUtil
</Zeta:Loader>
<style type="text/css">
</style>

<script type="text/javascript">
var cmMac = '${cmMac}';

var thresholdMap = {};
var cmSignalThresholdMap = {};

Ext.onReady(function() {
	Ext.apply(Ext.QuickTips.getQuickTip(), {
	    maxWidth: 300,
	    minWidth: 100,
	    trackMouse: true
	});

	Ext.QuickTips.init();
	
	loadTargetThreshold();
	
	var leftPortletItems = {
		columnWidth: .50,
        style:'padding:5px',
        items: [{
            id:'portlet-mtr',
            title: '@pnmp.mtr@',
            autoScroll:true,
            tools: [{
            	id: 'help',
            	qtip: 'MTR<br>@pnmp.mtrDesc@'
            }],
            contentEl:'mtr-chart-container'
        }, {
            id:'portlet-preMTTER',
            title: '@pnmp.premtter@',
            autoScroll:true,
            tools: [{
            	id: 'help',
            	qtip: 'PreMTTER, Pre- Main Tap to Total Energy Ratio<br>@pnmp.premtterDesc@'
            }],
            contentEl:'preMTTER-chart-container'
        }, {
        	id:'portlet-PPESR',
            title: '@pnmp.ppesr@',
            autoScroll:true,
            tools: [{
            	id: 'help',
            	qtip: 'PPESR, Pre-Post Energy Symmetry Ratio<br>@pnmp.ppesrDesc1@<br>@pnmp.ppesrDesc2@'
            }],
            contentEl:'PPESR-chart-container'
        },{
            id:'portlet-upTx',
            title: '@pnmp.upTxPower@',
            autoScroll:true,
            contentEl:'upTx-chart-container'
        }, {
            id:'portlet-downTx',
            title: '@pnmp.downRePower@',
            autoScroll:true,
            contentEl:'downTx-chart-container'
        }]
	};
	
	var rightPortletItems = {
		columnWidth: .50,
        style:'padding:5px',
        items: [{
            id: 'portlet-nmter',
            title: '@pnmp.nmter@',
            autoScroll: true,
            tools: [{
            	id: 'help',
            	qtip: 'NMTTER, Not Main Tap to Total Energy Ratio<br>@pnmp.nmtterDesc@'
            }],
            contentEl: 'nmter-chart-container'
        }, {
            id: 'portlet-postMTTER',
            title: '@pnmp.postmtter@',
            autoScroll: true,
            tools: [{
            	id: 'help',
            	qtip: 'PostMTTER, Post- Main Tap to Total Energy Ratio<br>@pnmp.postmtterDesc@'
            }],
            contentEl: 'postMTTER-chart-container'
        }/* , {
        	id: 'portlet-mrLevel',
            title: '微反射水平',
            autoScroll: true,
            contentEl: 'mrLevel-chart-container'
        } */,{
            id: 'portlet-upSnr',
            title: '@pnmp.upSnr@',
            autoScroll: true,
            contentEl: 'upSnr-chart-container'
        }, {
            id: 'portlet-downSnr',
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
        items:[headerPanel, {
        	xtype: 'portal', 
        	region: 'center', 
        	margins: '0 0 0 0', 
        	border: false, 
        	items: portletItems
        }]
    });
	initTab();
});	

</script>
</head>
<body class="bodyGrayBg">
	<div id="header">
		<div id="putTab" class="edge10"></div>
	</div>
	<div id="mtr-chart-container">MTC(dB)</div>
	<!-- <div id="mrLevel-chart-container">MRLevel(dBc)</div> -->
	<div id="upTx-chart-container">@pnmp.upTxPower@(dBmV)</div>
	<div id="upSnr-chart-container">@pnmp.upSnr@(dB)</div>
	<div id="downTx-chart-container">@pnmp.downRePower@(dBmV)</div>
	<div id="downSnr-chart-container">@pnmp.downSnr@(dB)</div>
	<div id="nmter-chart-container">@pnmp.nmter@(dB)</div>
	<div id="preMTTER-chart-container">@pnmp.premtter@(dB)</div>
	<div id="postMTTER-chart-container">@pnmp.postmtter@(dB)</div>
	<div id="PPESR-chart-container">@pnmp.ppesr@(dB)</div>
</body>
</Zeta:HTML>