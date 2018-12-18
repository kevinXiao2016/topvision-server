<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<style type="text/css">
table tr{
	height: 26px;
}
.labelTd{
	width: 130px;
	text-align:center;
}
.valueTd{
	width: 200px;
}
</style>

<script type="text/javascript">
var recordTypeJson = ${recordTypeJson};
var eventLevelStore = null;
var combo = null;

function eventTypeDscr(evtType){
	var str;
	if(evtType == "localnonvol"){
		str = I18N.syslog.localnonvolType;
	} else if(evtType == "localvolatile"){
		str = I18N.syslog.localvolatileType;
	} else if(evtType == "traps"){
		str = I18N.syslog.trapsType;
	} else if(evtType == "syslog"){
		str = I18N.syslog.syslogType;
	}
	return str;
}

function eventLevelDscr(eventIndex){
	var str;
	switch(eventIndex){
	case 1:
		str =  I18N.syslog.emergencyLevel;
		break;
	case 2:
		str =  I18N.syslog.alertLevel;
		break;
	case 3:
		str =  I18N.syslog.criticalLevel;
		break;
	case 4:
		str =  I18N.syslog.errorLevel;
		break;
	case 5:
		str =  I18N.syslog.warningLevel;
		break;
	case 6:
		str =  I18N.syslog.notificationLevel;
		break;
	case 7:
		str =  I18N.syslog.informationalLevel;
		break;
	case 8:
		str =  I18N.syslog.debugLevel;
		break;
	case 28:
		str =  I18N.syslog.noneLevel;
		break;
	}
	return str;
}

function update(){
	var entityId = recordTypeJson.entityId;
	var topCcmtsSyslogRecordType = recordTypeJson.topCcmtsSyslogRecordType;
	var topCcmtsSyslogMinEvtLvl = combo.getValue();
	//验证topCcmtsSyslogMinEvtLvl是否为数字
	var reg = /^\d$/;
	if(!reg.test(topCcmtsSyslogMinEvtLvl)){
		var text = $("#eventLevel").val();
		if(text==I18N.syslog.emergencyLevel){
			topCcmtsSyslogMinEvtLvl = 1;
		}else if(text==I18N.syslog.alertLevel){
			topCcmtsSyslogMinEvtLvl = 2;
		}else if(text==I18N.syslog.criticalLevel){
			topCcmtsSyslogMinEvtLvl = 3;
		}else if(text==I18N.syslog.errorLevel){
			topCcmtsSyslogMinEvtLvl = 4;
		}else if(text==I18N.syslog.warningLevel){
			topCcmtsSyslogMinEvtLvl = 5;
		}else if(text==I18N.syslog.notificationLevel){
			topCcmtsSyslogMinEvtLvl = 6;
		}else if(text==I18N.syslog.informationalLevel){
			topCcmtsSyslogMinEvtLvl = 7;
		}else if(text==I18N.syslog.debugLevel){
			topCcmtsSyslogMinEvtLvl = 8;
		}else if(text==I18N.syslog.noneLevel){
			topCcmtsSyslogMinEvtLvl = 28;
		}
	}
	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.savingConfig);
	$.ajax({
		url: '/cmc/updateSyslogEvtLvl.tv',
    	type: 'POST',
    	data: {entityId:entityId, topCcmtsSyslogRecordType:topCcmtsSyslogRecordType, topCcmtsSyslogMinEvtLvl:topCcmtsSyslogMinEvtLvl},
   		success: function() {
   			//更新成功后
   			window.parent.closeWaitingDlg();
   			//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.syslog.updateSuccess);
   			top.afterSaveOrDelete({
   				title: I18N.CMC.tip.tipMsg,
   				html: '<b class="orangeTxt">' + I18N.syslog.updateSuccess + '</b>'
   			});
   			cancleClick();
   		}, error: function() {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
   			cancleClick();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function cancleClick() {
	window.parent.getWindow("syslogManagement").body.dom.firstChild.contentWindow.config_store.reload();
	window.parent.closeWindow("updateRecordTypeLvl");
}

Ext.onReady(function() {
	$("#recordType").text(eventTypeDscr(recordTypeJson.topCcmtsSyslogRecordType));
	$("#eventLevel").val(eventLevelDscr(recordTypeJson.topCcmtsSyslogMinEvtLvl));
	//获取所有的事件等级
	eventLevelStore = new Ext.data.JsonStore({
	    url: '/cmc/getAllEventLevel.tv',
        fields: ['eventIndex', 'event'],
        sortInfo:{field:'eventIndex',direction:'asc'}
	});	
	eventLevelStore.load();
	//生成带有所有的事件等级的下拉菜单
	combo = new Ext.form.ComboBox({
        store: eventLevelStore,
        valueField: 'eventIndex',
        displayField: 'event',
        triggerAction: 'all',
        editable: false,
        mode: 'local',
        applyTo : 'eventLevel'
	});
});

</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    <fmt:message bundle='${cmc}' key='syslog.recordType'/>:
	                </td>
	                <td id="recordType">
	                    
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <fmt:message bundle='${cmc}' key='syslog.eventLevel'/>:
	                </td>
	                <td>
	                   <input id="eventLevel" type="text" disabled="disabled" style="width: 150px;"/>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a onclick="update()" id="loadData" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i><fmt:message bundle='${cmc}' key='CMC.button.save'/></span></a></li>
		         <li><a id="cancel" onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></span></a></li>
		     </ol>
		</div>
	</div>

	
</body>
</html>