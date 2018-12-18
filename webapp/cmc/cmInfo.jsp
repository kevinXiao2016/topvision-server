<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<title><fmt:message bundle='${cmc}' key='CM.cmMessage'/></title>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>    
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmAttribute = ["cmId", "statusMacAddress", "statusIpAddress", "statusRxPower", "statusTimingOffset", "statusEqualizationData", "statusValue", "statusUnerroredsString", "docsIfCmtsCmStatusCorrectedsString", "docsIfCmtsCmStatusUncorrectablesString", "statusSignalNoise", "statusMicroreflections", "statusModulationType"];
var cmStatus = ["docsIfCmStatusModulationType", "docsIfCmStatusTxPower"];

function cancleClick(){
	window.parent.closeWindow('showCmInfoDlg');
}
Ext.onReady(function(){
	var w = document.body.clientWidth - 30;
    var h = document.body.clientHeight - 50;
    
	attrStore = {
            "CM ID": cmAttribute.cmId,
            "IP": cmAttribute.statusIpAddress,
            I18N.CMC.text.mac: cmAttribute.statusMacAddress,
            I18N.CMC.text.DeviceDescription:"",            
            I18N.CM.sysUpTime:"",
            I18N.CM.configFileName:"",
            I18N.CM.dhcpServer:"",
            I18N.CM.tftpServer:"",
            I18N.CM.timingServer:"",
            I18N.CM.downFrequency:"",
            I18N.CM.downwidth:"",
            I18N.CM.downModStyle: cmStatus.docsIfCmStatusModulationType,
            I18N.CM.downReceivePower:"",
            I18N.CM.downSpeed:"",
            I18N.CMC.text.downChannelFlow:"",
            I18N.CM.downSnr:"",
            I18N.CM.upFrequency:"",
            I18N.CM.upwidth:"",
            I18N.CM.upTiming:"",
            I18N.CM.upSpeed:"",
            I18N.CMC.text.upChannelFlow:"",           
            I18N.CM.upSendPower: cmStatus.docsIfCmStatusTxPower, 
            I18N.CMC.text.Unerroreds: cmAttribute.docsIfCmtsCmStatusUnerroredsString,
            I18N.CMC.text.correcteds: cmAttribute.docsIfCmtsCmStatusCorrectedsString, 
            I18N.CMC.text.correcteds: cmAttribute.docsIfCmtsCmStatusUncorrectablesString, 
            I18N.CM.cmStatusMessage: cmAttribute.statusRxPower,
            I18N.CMC.text.SoftwareVision:"",
            I18N.CM.softName:"",
            I18N.CM.serNum:"",
            I18N.CM.microreflections: cmAttribute.statusMicroreflections
    };
    Ext.grid.PropertyGrid.prototype.setSource = function(source) {
        delete this.propStore.store.sortInfo;
        this.propStore.setSource(source);
    };
    propertyGid = new Ext.grid.PropertyGrid({
        renderTo: 'cm_property',
        title:'',
        width:w,
        height:h,
        frame:false,
        autoScroll: false,
        source: attrStore
    });
    propertyGid.on("beforeedit", function(e) {
        e.cancel = true;
        return false;
    });
    //propertyGid.addButton(new Ext.Button,locateCm,propertyGid);
});
</script>
</head>
<body class=POPUP_WND>
	<div id="cm_property" style="margin: 10px 12px 10px 15px;"></div>
	<div align=right style="margin-right: 14px">
		<button id=saveBt class=BUTTON75 type="button"
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onMouseDown="this.className='BUTTON_PRESSED75'"
			onclick="cancleClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
	</div>
</body>
</html>