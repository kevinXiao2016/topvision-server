<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>relatedModulationConfig</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
var profileId = <s:property value="profileId"/>;
function cancleClick(){
	window.parent.closeWindow('showModulationConfigDlg');
}
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 60;
	
	var columns = [
				{header: 'ID', width: 100, sortable: true, align: 'center', dataIndex: 'modIndex'},
				{header: I18N.CMC.label.usageCode, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModIntervalUsageCodeName'},
				{header: I18N.CMC.label.modControl, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModControlName'},
				{header: I18N.CMC.label.modulationType, width: 100, sortable:true, align : 'center', dataIndex: 'docsIfCmtsModTypeName'},
				{header: I18N.CMC.label.preambleLength, width: 100, sortable:true, align: 'center', dataIndex: 'docsIfCmtsModPreambleLenForunit'},
				{header: I18N.CMC.label.differEncodeSwitch, width: 100, sortable:true, align: 'center', dataIndex: 'docsIfCmtsModDifferentialEncodingName'},
				{header: I18N.CMC.label.maxCorrectableBytes, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModFECErrorCorrectionForunit'}	,
				{header: I18N.CMC.label.fecCorrectCodeSize, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModFECCodewordLengthForunit'},	
				{header: I18N.CMC.label.scramblerSeed, width: 100, sortable: true, align: 'center', dataIndex: 'modScramblerSeed'},
				{header: I18N.CMC.label.minSlotCount, width: 100, sortable:true, align : 'center', dataIndex: 'docsIfCmtsModMaxBurstSizeForunit'},
				{header: I18N.CMC.label.safeTime, width: 100, sortable:true, align: 'center', dataIndex: 'docsIfCmtsModGuardTimeSizeForunit'},
				{header: I18N.CMC.title.fecCodewordStatus width: 100, sortable:true, align: 'center', dataIndex: 'docsIfCmtsModLastCodewordShortenedName'},
				{header: I18N.CMC.label.Scrambler, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModScramblerName'}	,
				{header: I18N.CHANNEL.interleave, width: 100, sortable: true, align: 'center', dataIndex: 'modByteInterleaverDepth'},	
				{header: I18N.CMC.label.interSize, width: 100, sortable: true, align: 'center', dataIndex: 'modByteInterleaverBlockSize'},
				{header: I18N.CMC.label.preambleType, width: 100, sortable:true, align : 'center', dataIndex: 'docsIfCmtsModPreambleTypeName'},
				{header: I18N.CMC.label.trellisCoding, width: 100, sortable:true, align: 'center', dataIndex: 'docsIfCmtsModTcmErrorCorrectionOnName'},
				{header: I18N.CMC.label.scdmaStep, width: 100, sortable:true, align: 'center', dataIndex: 'modScdmaInterleaverStepSize'},
				{header: I18N.CMC.label.spreadSpectrumSwitch, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModScdmaSpreaderEnableName'}	,
				{header: I18N.CMC.label.scdmaSize, width: 100, sortable: true, align: 'center', dataIndex: 'modScdmaSubframeCodes'},	
				{header: I18N.CHANNEL.channelType, width: 100, sortable: true, align: 'center', dataIndex: 'docsIfCmtsModChannelTypeName'}
				
			];
	       	store = new Ext.data.JsonStore({
	       	    url: ('getModulationConfigFile.tv?cmcId=' + cmcId+"&profileId="+profileId),
	       	    root: 'data',
	       	    //remoteSort: true, 
	       	     fields: ['modIndex','docsIfCmtsModIntervalUsageCodeName', 'docsIfCmtsModControlName', 'docsIfCmtsModTypeName', 'docsIfCmtsModPreambleLenForunit', 'docsIfCmtsModDifferentialEncodingName', 
	     	    'docsIfCmtsModFECErrorCorrectionForunit', 'docsIfCmtsModFECCodewordLengthForunit','modScramblerSeed', 'docsIfCmtsModMaxBurstSizeForunit', 'docsIfCmtsModGuardTimeSizeForunit', 
	     	    'docsIfCmtsModLastCodewordShortenedName', 'docsIfCmtsModScramblerName', 'modByteInterleaverDepth', 'modByteInterleaverBlockSize', 
	     	    'docsIfCmtsModPreambleTypeName', 'docsIfCmtsModTcmErrorCorrectionOnName', 'modScdmaInterleaverStepSize', 'docsIfCmtsModScdmaSpreaderEnableName', 
	     	    'modScdmaSubframeCodes', 'docsIfCmtsModChannelTypeName']
	       	});
	store.setDefaultSort('modIndex', 'ASC');
	
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', width: w, height: h,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm,
		renderTo: 'station-div'});
    grid.on('rowdblclick', function(grid, rowIndex, e) {
      });  		
	store.load();
});
</script>
</head>
<body class=POPUP_WND style="margin-left:15px; margin-top:20px;">
	<div id="station-div"></div>
	<div align=right style="margin-right:14px; margin-top:8px">
        <button id=saveBt class=BUTTON75 type="button"
            onMouseOver="this.className='BUTTON_OVER75'"
            onMouseOut="this.className='BUTTON75'"
            onMouseDown="this.className='BUTTON_PRESSED75'"
            onclick="cancleClick()"><fmt:message bundle="${cmcRes}" key=CMC.button.close/></button>
    </div>
</body>
