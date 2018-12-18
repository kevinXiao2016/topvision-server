<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/nocache.inc" %>
<%@ include file="../include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>  
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<%@ include file="../include/tabPatch.inc" %>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var uniId = '<s:property value="uniId"/>';
var realIndex = '<s:property value="realIndex"/>';
var tabNum = <s:property value="tabNum"/>;
var onuId = '<s:property value="onuId"/>';
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var onuObject;
var tabs;
var uniAutoNegoEnableFlag;
var uniAutoNegoStatusFlag;
Ext.onReady(function () {
	var w = document.body.clientWidth - 210;
	var h = document.body.clientHeight - 62;
	Ext.Ajax.request({
		url:"/onu/onuObjectCreate.tv?onuId="+onuId,
		method:"post",
		success:function (response) {
			json = Ext.util.JSON.decode(response.responseText);
			onuObject = json;
			var rootNode = new Ext.tree.TreeNode({text: onuObject.onuName, id: "onuDevice", icon:'image/router_16.gif'});
			for(var i = 0; i < onuObject.onuUniPortList.length; i++){
				var uniNode = new Ext.tree.TreeNode({text: "uni"+onuObject.onuUniPortList[i].uniRealIndex, id: "onuUni_" + onuObject.onuUniPortList[i].uniRealIndex, icon:'image/port_48.gif'});
				rootNode.appendChild(uniNode);
			}//end for
			uniAutoNegoStatusFlag = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationLocalTechAbility;
			$("#MacType").val(uniAutoNegoStatusFlag);
			var uniAutoNegoEnable = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationEnable;
		 	if (uniAutoNegoEnable == 1){
		 		uniAutoNegoEnableFlag = false;
		 	}else if (uniAutoNegoEnable == 2){
		 		uniAutoNegoEnableFlag = true;
		 	}
		 	$("#uniAutoNego").attr("checked",uniAutoNegoEnableFlag);
		    var onuTree = new Ext.tree.TreePanel({
		        id : 'onuTree',
		        useArrows : useArrows, 
		        renderTo : 'onuTreePanel',
		        animate: animCollapse, 
		        trackMouseOver: trackMouseOver,
		        border: true, 
		        lines: true, 
		        enableDD: false, 
		        autoScroll: true,
		        height:h,
		        width:190,
		        root : rootNode,
		        listeners : {
                    'click' : {
                        fn : onUniNodeClick,
                        scope : this
                    }
                }
		    });
		    onuTree.getRootNode().expand();
		    Ext.getCmp("onuTree").getNodeById("onuUni_"+realIndex).select();
		},
		failure:function () {
			window.parent.showMessageDlg(I18N.COMMON.tip,I18N.ONU.loadOnuDataError);
		}});
	tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: h,
        activeTab:tabNum,
        frame:true,
        defaults:{autoHeight: true},
        items:[
            {itemId:'u1',contentEl: 'uniRateTab', title: I18N.ONU.portLimit},
            {itemId:'u2',contentEl: 'uniStormTab', title: I18N.ONU.broadcastStorm},
            {itemId:'u3',contentEl: 'uniAutoNegotiationTab', title: I18N.ONU.portAutoNego},
            {itemId:'u4',contentEl: 'uniMacTab', title: I18N.ONU.macAddrMgmt}
        ]
    });
});
function onUniNodeClick(node) {
    Ext.getCmp("onuTree").getNodeById(node.attributes.id).select();
    var uniDivId = node.attributes.id;
	if(uniDivId.substring(3,6) == 'Uni'){		
		switchUniAttr(node.attributes.id);
	}
}
function switchUniAttr(nodeId) {
 	realIndex = nodeId.substring(7);
 	uniId = onuObject.onuUniPortList[realIndex-1].uniId;
 	uniAutoNegoStatusFlag = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationLocalTechAbility;
 	$("#MacType").val(uniAutoNegoStatusFlag);
 	var uniAutoNegoEnable = onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationEnable;
 	if (uniAutoNegoEnable == 1){uniAutoNegoEnableFlag = false;}
 	else if(uniAutoNegoEnable == 2){uniAutoNegoEnableFlag = true;}
 	$("#uniAutoNego").attr("checked",uniAutoNegoEnableFlag);
}
function saveClick() {
	if('u1'==getActiveTabId()){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.disableConfig);
	}else if('u2'==getActiveTabId()){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.disableConfig);
	}else if('u3'==getActiveTabId()){
		var uniAutoNego = $("#uniAutoNego").attr("checked");
		var setUniAutoNegoStat = $("#MacType").val();
		var status;
		//UNI自协商使能设置
		if(uniAutoNego==true){
			status=2;
		}else{
			status=1;
		}
		if(status != onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationEnable){
		showWaitingDlg(I18N.COMMON.wait,I18N.ONU.setUniAutoNego, 'ext-mb-waiting');
		$.ajax({
            url: '/onu/setUniAutoNegoActive.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&uniId="+uniId+"&uniAutoNegoEnable=" + status,
            success: function() {
                if(status=2){uniAutoNegoEnableFlag = true;}
                else if (status=1){uniAutoNegoEnableFlag = false;}
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setSuccess);
            }, error: function() {
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setFail);
        	}, cache: false
        });
		}//end UNI自协商使能设置
		//UNI自协商状态设置
		if(setUniAutoNegoStat != onuObject.onuUniPortList[realIndex - 1].uniAutoNegotiationLocalTechAbility){
		showWaitingDlg(I18N.COMMON.wait,I18N.ONU.setUniNegoStatus, 'ext-mb-waiting');
		$.ajax({
            url: '/onu/setUniAutoNegotiationStat.tv',
            type: 'POST',
            data: "entityId=" + entityId + "&uniId="+uniId+"&uniAutoNegotiationStatus=" + setUniAutoNegoStat,
            success: function() {
            	uniAutoNegoStatusFlag = setUniAutoNegoStat;
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setSuccess);
            }, error: function() {
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setFail);
        	}, cache: false
        });	
		}//end UNI自协商状态设置
	}else if('u4'==getActiveTabId()){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.disableConfig);
	}else{
		//Exception
	}
}

function cancelClick() {
	window.parent.closeWindow('modalDlg');	
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function autoNegRestart(){
		showWaitingDlg(I18N.COMMON.wait,I18N.ONU.setting, 'ext-mb-waiting');
		$.ajax({
			url: '/onu/setRestartUniAutoNego.tv', 
			type: 'POST',
			data: "uniId="+uniId+"&entityId="+entityId,
			success: function() {		        				
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setSuccess);
			}, 
			error: function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.setFail);
			}, cache: false
		});
}
function getSelectedItemId(t) {
	var modal = t.getSelectionModel().getSelectedNode();
	var itemId = null;
	if (modal != null) {
		itemId = modal.id;
	}
	return itemId;
}
//UNI端口自协商验证
function uniAutoNegoCheck(){
	$("#saveBt").attr("disabled",false);
	if(($("#uniAutoNego").attr("checked")== uniAutoNegoEnableFlag) && ($("#MacType").val()== uniAutoNegoStatusFlag) ){
		$("#saveBt").attr("disabled",true);
	}	
}
function getSelectedItem(t) {
	return t.getSelectionModel().getSelectedNode();
}
function getActiveTabId(){
	return tabs.getActiveTab().itemId;	
}
</script>
</head><body class=POPUP_WND style="padding:10px;">
<center>
<table cellspacing=0 cellpadding=0>
<tr><td width=200px><div id="onuTreePanel" style="margin-top: 5px;"></div></td>
<td width=20px>&nbsp;</td><Td>
<div id="tabs">
	<table><tr><td>
    <div id="uniRateTab" class="x-hide-display">
    <div style="overflow:auto; padding-left:15px; padding-top:10px;">
    	<table cellspacing=10>
    	<tr><td>
    	<fieldset style='width:400px; height:100%; margin:20px'>
    	<legend><fmt:message bundle="${i18n}" key="SERVICE.portIndirect" /></legend><table>
    		<tr><td width=180px style="padding-left:20px;"><fmt:message bundle="${i18n}" key="SERVICE.inRateRestrictEn" />:</td>
    		<td><input type=checkbox id="" value="" >
    		</td></tr>
    		<tr><td width=180px style="padding-left:20px;">CIR:</td>
    		<td><input type=text id="" value="" style="width:100px;">&nbsp; Kbps
    		</td></tr>
    		<tr><td width=180px style="padding-left:20px;">CBS:</td>
    		<td><input type=text id="" value="" style="width:100px;">&nbsp; Kbytes
    		</td></tr>
    		<tr><td width=180px style="padding-left:20px;">EBS:</td>
    		<td><input type=text id="" value="" style="width:100px">&nbsp; Kbytes</td></tr>
    		</table></fieldset></td></tr>
    	<tr><td>
    	<fieldset style='width:100%; height:100%; margin:20px'>
    	<legend><fmt:message bundle="${i18n}" key="SERVICE.portOutDirect" /></legend><table>
    		<tr><td width=180px style="padding-left:20px;"><fmt:message bundle="${i18n}" key="SERVICE.outRateRestrictEn" />:</td>
    		<td><input type=checkbox id="" value="" >
    		</td></tr>
    		<tr><td width=180px style="padding-left:20px;">PIR:</td>
    		<td><input type=text id="" value="" style="width:100px;">&nbsp; Kbps
    		</td></tr>
    		<tr><td width=180px style="padding-left:20px;">CIR:</td>
    		<td><input type=text id="" value="" style="width:100px">&nbsp; Kbps
    		</td></tr>
    		</table></fieldset></td></tr>
    	</table>
    	</div>
    </div>
</table>
</div>
</td></tr>
<tr><td style="padding-top:10px"></td>
<td style="padding-top:10px"></td>
<td align=right style="padding-top:1px" style="padding-left:120">
		<div align="right" style="margin-top:10px" style="margin-right:10px">
			<button id=saveBt disabled class=BUTTON75 type="button"
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>
				&nbsp;
			<button id=cancelBt class=BUTTON75 type="button"
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div></td></tr>
</table></center>
</body></html>