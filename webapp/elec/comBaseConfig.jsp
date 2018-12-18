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
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
var comIndex = <s:property value="comIndex"/>;
var des = '<s:property value="comInfoDes"/>';
var buad = '<s:property value="comInfoBuad"/>';
var type = '<s:property value="comInfoType"/>';
var mode = '<s:property value="comInfoMode"/>';
var dataBit = '<s:property value="comInfoDataBit"/>';
var startBit = '<s:property value="comInfoStartBit"/>';
var endBit = '<s:property value="comInfoEndBit"/>';


Ext.onReady(function(){
    initButton("all");

    //初始化波特率
    var buadParam = ["", "b110", "b300", "b600", "b1200", "b2400", "b4800", "b9600", "b14400", "b19200", "b38400", "b57600", "b115200"];
    var bu = $("#buad");
    for(var a=1; a<buadParam.length; a++){
        bu.append(String.format("<option value={0}>{1}</option>", a, buadParam[a]));
    }

    //初始化数据
    $("#des").val(des);
    if(buad && !isNaN(buad)){
	    $("#buad").val(parseInt(buad));
    }else{
        $("#buad").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
    }
    if(type && !isNaN(type)){
        $("#type").val(parseInt(type));
    }else{
    	$("#type").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
    }
    if(mode && !isNaN(mode)){
        $("#mode").val(parseInt(mode));
    }else{
    	$("#mode").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
    }
    if(dataBit && !isNaN(dataBit)){
    	$("#dataBit").val(parseInt(dataBit));
    }else{
    	$("#dataBit").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
    }
    if(endBit && !isNaN(endBit)){
        $("#endBit").val(parseInt(endBit));
    }else{
        $("#endBit").prepend("<option value=-1>"+ I18N.COMMON.select +"...</option>").val(-1);
    }
    $("#startBit").text(startBit ? startBit : 1);
});

function saveClick() {
    if(!checkedDes()){
        $("#des").focus();
        return;
    }
    var buadTmp = $("#buad").val();
    var typeTmp = $("#type").val();
    var modeTmp = $("#mode").val();
    var desTmp = $("#des").val();
    var dataBitTmp = $("#dataBit").val();
    var endBitTmp = $("#endBit").val();
    if(typeTmp < 0){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectCom);
        return;
    }
    if(buadTmp < 0){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectBAUD);
        return;
    }
    if(modeTmp < 0){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectParrot);
        return;
    }
    if(dataBitTmp < 0){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectData);
        return;
    }
    if(endBitTmp < 0){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectStop);
        return;
    }
    if(buadTmp == buad && desTmp == des && modeTmp == mode && typeTmp == type && dataBitTmp == dataBit && endBitTmp == endBit){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.cfgSave);
        return;
    }
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving, 'ext-mb-waiting');
    var par = {entityId: entityId, comIndex: comIndex, comInfoType: typeTmp, comInfoBuad: buadTmp, comInfoMode: modeTmp, comInfoDes: desTmp,
    	    comInfoDataBit: dataBitTmp, comInfoEndBit: endBitTmp};
    var url = '/epon/elec/modifyComBase.tv?r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: par,
        success : function(response) {
            if(response.responseText == 'success'){
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfComOk);
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
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfComEr1);
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfComEr2);
	}
}
function cancelClick() {
    window.parent.closeWindow('comBaseConfig');
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
	var par = {entityId: entityId, comIndex: comIndex};
	var url = '/epon/elec/refreshComBase.tv?r=' + Math.random();
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
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.deviceNotConnect);
    }else{
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
    }
}

function checkedDes(){
	var p = $("#des");
    p.css("border", "1px solid #8bb8f3");
    if(!p.val()){
        return true;
    }
    var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d\s,;!#$%^&=+])+$/ig;
    if(!reg.exec(p.val())){
        p.css("border", "1px solid #ff0000");
        return false;
    }
    return true;
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
        <table cellspacing=30 cellpadding=0>
            <tr><td align=right><fmt:message bundle="${i18n}" key="ELEC.descMsg" />:
            </td><td colspan=3>
                <input id=des style='overflow-y:auto;width:290px;border:1px solid #8bb8f3;' maxlength=63
                    title='<fmt:message bundle="${i18n}" key="ELEC.notSupportCn" />' 
                    onkeyup='checkedDes()' onblur='checkedDes()' />
            </td></tr>
            <tr><td align=right width=60><fmt:message bundle="${i18n}" key="ELEC.comType" />:
            </td><td width=100>
                <select id=type style='width:100px;border:1px solid #8bb8f3;'>
                    <option value=1>RS-232</option>
                    <option value=2>RS-485</option>
                </select>
            </td><td align=right width=60><fmt:message bundle="${i18n}" key="ELEC.data" />:
            </td><td width=70>
                <select id=dataBit style='width:70px;'>
                    <option value=5>5</option>
                    <option value=6>6</option>
                    <option value=7>7</option>
                    <option value=8>8</option>
                </select>
            </td></tr>
            <tr><td align=right><fmt:message bundle="${i18n}" key="ELEC.baud" />:
            </td><td>
                <select id=buad style='width:100px;border:1px solid #8bb8f3;'></select>
            </td><td align=right><fmt:message bundle="${i18n}" key="ELEC.start" />:
            </td><td>
                <span id=startBit></span>
            </td></tr>
            <tr><td align=right><fmt:message bundle="${i18n}" key="ELEC.paritytype" />:
            </td><td>
                <select id=mode style='width:100px;border:1px solid #8bb8f3;'>
                    <option value=1><fmt:message bundle="${i18n}" key="ELEC.parit1" /></option>
                    <option value=2><fmt:message bundle="${i18n}" key="ELEC.parit2" /></option>
                    <option value=3><fmt:message bundle="${i18n}" key="ELEC.parit3" /></option>
                </select>
            </td><td align=right><fmt:message bundle="${i18n}" key="ELEC.stop" />:
            </td><td>
                <select id=endBit style='width:70px;'>
                    <option value=1>1</option>
                    <option value=2>2</option>
                </select>
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