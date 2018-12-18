<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
var comIndex = <s:property value="comIndex"/>;
var mainIp = '<s:property value="mainRemoteIp"/>';
var mainPort = '<s:property value="mainRemotePort"/>';
var backIp = '<s:property value="backRemoteIp"/>';
var backPort = '<s:property value="backRemotePort"/>';
var serverType = '<s:property value="serverType"/>';
var serverPort = '<s:property value="serverPort"/>';

Ext.onReady(function(){
    initButton("all");
    var ip1 = new ipV4Input("mainIp", "mainIp");
    var ip2 = new ipV4Input("backIp", "backIp");
    setIpWidth("all", 150);
    if(!mainIp || !checkedIpValue(mainIp)){
        mainIp = "0.0.0.0";
    }
    if(!backIp || !checkedIpValue(backIp)){
    	backIp = "0.0.0.0";
    }
    ip1.setValue(mainIp);
    ip2.setValue(backIp);
    if(mainIp == "0.0.0.0"){
        setEnable("main", false);
    }else{
		$("#mainCheck").click();
		$("#mainPort").val(mainPort);
    }
    if(backIp == "0.0.0.0"){
        setEnable("back", false);
    }else{
    	$("#backCheck").click();
    	$("#backPort").val(backPort);
    }
    if(!isNaN(serverType) && parseInt(serverType) > -1 && parseInt(serverType) < 4){
		$("#serverType").val(serverType);
    }else{
    	$("#serverType").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
	}
	if(serverPort && parseInt(serverPort) > 1024 && (serverType == 1 || serverType == 3)){
		$("#serverPort").val(serverPort);
	}
    serverTypeChange();
});

function saveClick() {
	var mChecked = $("#mainCheck").attr("checked");
	var mIp = getIpValue("mainIp");
	var mPort = $("#mainPort").val();
	var bChecked = $("#backCheck").attr("checked");
	var bIp = getIpValue("backIp");
	var bPort = $("#backPort").val();
	var sType = $("#serverType").val();
	var sPort = $("#serverPort").val();
	if(sType < 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.sltServerType);
		return;
	}
	if((sType == 1 || sType == 3) && !checkedPort("serverPort")){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.iptValidPort);
		return;
	}
	if(sType != 1 && sType != 3){
		sPort = 0;
	}
	if(mChecked){
		if(!checkedIpValue(mIp) || mIp == "0.0.0.0" || !mIp || mIp == "..."){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.iptMasterIP);
			return;
		}
		if(!checkedPort("mainPort")){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.iptMasterPort);
			return;
		}
	}else{
		mIp = "0.0.0.0";
		mPort = 0;
	}
	if(bChecked){
		if(!checkedIpValue(bIp) || bIp == "0.0.0.0" || !bIp || bIp == "..."){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.iptSlaveIP);
			return;
		}
		if(!checkedPort("backPort")){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.iptSlavePort);
			return;
		}
	}else{
		bIp = "0.0.0.0";
		bPort = 0;
	}
	if(mIp == mainIp && mPort == mainPort && backIp == bIp && backPort == bPort && serverType == sType && serverPort == sPort){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cfgSave);
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving, 'ext-mb-waiting');
    var par = {entityId: entityId, comIndex: comIndex, mainRemoteIp: mIp, mainRemotePort: mPort, backRemoteIp: bIp, backRemotePort: bPort,
    	    	serverType: sType, serverPort: sPort};
    var url = '/epon/elec/modifyComRemote.tv?r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: par,
        success : function(response) {
            if(response.responseText == 'success'){
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfRemoteSvr);
            	if(window.parent.getFrame("entity-" + entityId) != null)
                    window.parent.getFrame("entity-" + entityId).page.reload();
            	cancelClick();
            }else{
            	saveFailed(response.responseText);
            }
        },
        failure : function(response) {
        	saveFailed(response.responseText);
        }
    });
}
function saveFailed(re){
	if(re.split(":")[0] == "no response"){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfSvrEr1);
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfSvrEr2);
	}
}
function cancelClick() {
    window.parent.closeWindow('comRemoteConfig');
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
	var par = {entityId: entityId, comIndex: comIndex};
	var url = '/epon/elec/refreshComRemote.tv?r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: par,
        success : function(response) {
            if(response.responseText == 'success'){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
                location.reload();
            }else{
                refreshFailed(response.responseText);
            }
        },
        failure : function(response) {
            refreshFailed(response.responseText);
        }
    });
}
function refreshFailed(re){
	if(re.split(":")[0] == "no response"){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.deviceNotConnect );
    }else{
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
    }
}

function checkedPort(id){
	var pa = $("#" + id);
	if(pa.css("border").indexOf("#cccccc") > -1){
	    return true;
	}
	var v = pa.val();
    pa.css("border","1px solid #8bb8f3");
    var reg = /^([0-9]{1,5})+$/;
    if(v && reg.exec(v) && parseInt(v) > 1024 && parseInt(v) < 65536){
        return true;
    }
    pa.css("border","1px solid #ff0000");
    return false;
}
function serverTypeChange(){
	var v = $("#serverType").val();
	if(v == 1 || v == 3){
		$("#serverPort").unbind("focus", blurThis).css("border", "1px solid #8bb8f3");
	}else{
	    $("#serverPort").bind("focus", blurThis).css("border", "1px solid #cccccc").blur();
	}
}
function blurThis(){
	this.blur();
}
//n:main/back, s:true/false
function setEnable(n, s){
	setIpBorder((n + "Ip"), (s ? "default" : "1px solid #cccccc"));
	setIpEnable((n + "Ip"), s);
	if(s){
		$("#" + n + "Port").unbind("focus", blurThis).css("border", "1px solid #8bb8f3");
	}else{
		$("#" + n + "Port").bind("focus", blurThis).css("border", "1px solid #cccccc").blur();
	}
	$("#" + n + "Check").attr("checked", s);
}
function checkClick(el, n){
	setEnable(n, $(el).attr("checked"));
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
		$("#refreshBt").attr("disabled",false);
	}
}
</script>
</head>
<body class=POPUP_WND style="margin: 10px;overflow:hidden;" onload="authLoad()">
    <fieldset style="width: 450px; height: 240px; background-color: white">
        <table cellspacing=20 cellpadding=0>
            <tr><td width=260><fmt:message bundle="${i18n}" key="ELEC.svrMode" />:
                <select id=serverType style='width:100px;margin-left:10px;' onchange='serverTypeChange()'>
                    <option value=0><fmt:message bundle="${i18n}" key="ELEC.disable" /></option>
                    <option value=1>tcp_server</option>
                    <option value=2>tcp_client</option>
                    <option value=3>udp</option>
                </select>
            </td><td width=180><fmt:message bundle="${i18n}" key="ELEC.svrPort" />:
                <input id=serverPort style='width:50px;margin-left:10px;border:1px solid #8bb8f3;' maxlength=5
                    title='<fmt:message bundle="${i18n}" key="ELEC.range1025" />'
                    onkeyup='checkedPort(this.id)' onblur='checkedPort(this.id)' />
            </td></tr>
            <tr><td colspan=2>
                <table>
                    <tr><td>
		                <input id=mainCheck type=checkbox onclick='checkClick(this, "main")' />
		                <span onclick='$("#mainCheck").click()' style='cursor:hand;margin:5;'><fmt:message bundle="${i18n}" key="ELEC.masterSvr" /></span>
		            </td><td><hr size=1 width=260 color=#8bb8f3>
		            </td></tr>
		        </table>
            </td></tr>
            <tr><td><span style='margin-left:30;'></span><fmt:message bundle="${i18n}" key="ELEC.ipAddr" />:<span id=mainIp style='margin-left:10px;'></span>
            </td><td><fmt:message bundle="${i18n}" key="EPON.port" />:
                <input id=mainPort style='width:50px;margin-left:10px;border:1px solid #8bb8f3;' maxlength=5
                    title='<fmt:message bundle="${i18n}" key="ELEC.range1025" />'
                    onkeyup='checkedPort(this.id)' onblur='checkedPort(this.id)' />
            </td></tr>
            <tr><td colspan=2>
                <table>
                    <tr><td>
		                <input id=backCheck type=checkbox onclick='checkClick(this, "back")' />
		                <span onclick='$("#backCheck").click()' style='cursor:hand;margin:5;'><fmt:message bundle="${i18n}" key="ELEC.slaveSvr" /></span>
		            </td><td><hr size=1 width=260 color=#8bb8f3>
		            </td></tr>
                </table>
            </td></tr>
            <tr><td><span style='margin-left:30;'></span><fmt:message bundle="${i18n}" key="ELEC.ipAddr" />:<span id=backIp style='margin-left:10px;'></span>
            </td><td><fmt:message bundle="${i18n}" key="EPON.port" />:
                <input id=backPort style='width:50px;margin-left:10px;border:1px solid #8bb8f3;' maxlength=5
                    title='<fmt:message bundle="${i18n}" key="ELEC.range1025" />'
                    onkeyup='checkedPort(this.id)' onblur='checkedPort(this.id)' />
            </td></tr>
        </table>
    </fieldset>
    <div align="right" style="padding-top: 5px;">
        <button id=refreshBt class=BUTTON95 type="button" onclick="refreshClick()">
            <fmt:message bundle="${i18n}" key="COMMON.fetch" />
        </button>&nbsp;&nbsp;
        <button id=saveBt class=BUTTON95 type="button" onclick="saveClick()">
            <fmt:message bundle="${i18n}" key="COMMON.saveCfg" />
        </button>&nbsp;&nbsp;
        <button id=cancelBt class=BUTTON95 type="button"
            onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
    </div>
</body>
</html>