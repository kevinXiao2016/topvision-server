<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<style type="text/css">
</style>

<script type="text/javascript">
var config_store = null;
var entityId = entityId;
var trotModeStore = null;
var trotModeCombo = null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;

function validate(topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd){
	var reg;
	//验证最大记录数,为10..10000
	reg = /^\d{2,5}$/;
	if(!reg.test(topCcmtsSyslogMaxnum) || topCcmtsSyslogMaxnum<10 || topCcmtsSyslogMaxnum>10000){
		$("#topCcmtsSyslogMaxnum").focus();
		return false;
	}
	//验证时间间隔，为1..3600
	reg = /^\d{1,4}$/;
	if(!reg.test(topCcmtsSyslogTrotInvl) || topCcmtsSyslogTrotInvl > 3600 || topCcmtsSyslogTrotInvl<1){
		$("#topCcmtsSyslogTrotInvl").focus();
		return false;
	}
	//验证阈值，为0..20000
	reg = /^\d{1,5}$/;
	if(!reg.test(topCcmtsSyslogTrotTrhd) || topCcmtsSyslogTrotTrhd > 20000){
		$("#topCcmtsSyslogTrotTrhd").focus();
		return false;
	}
	return true;
}

function eventLevelDscr(value, p, record){
	var evtLvlId = record.get('topCcmtsSyslogMinEvtLvl');
	var str;
	switch(evtLvlId){
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

function trotModelDscr(trotModelIndex){
	var str;
	switch(trotModelIndex){
	case 1:
		str =  I18N.syslog.unconstranined;
		break;
	case 2:
		str =  I18N.syslog.belowThreshold;
		break;
	case 3:
		str =  I18N.syslog.stopAtThreshold;
		break;
	case 4:
		str =  I18N.syslog.inhibited;
		break;
	}
	return str;
}

function eventTypeDscr(value, p, record){
	var evtType = record.get('topCcmtsSyslogRecordType');
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

function addOper(value, p, record){
	var str;
	if(operationDevicePower){
		//str ="<input type='image' src='/images/system/bb.gif' onclick='updateRecordTypeLvl(\""+record.get('topCcmtsSyslogRecordType')+"\",\""+record.get('topCcmtsSyslogMinEvtLvl')+"\")'>";
		str ="<a href='javascript:;' onclick='updateRecordTypeLvl(\""+record.get('topCcmtsSyslogRecordType')+"\",\""+record.get('topCcmtsSyslogMinEvtLvl')+"\")'>" + I18N.CMC.label.edit+ "</a>";
	}else{
		//str ="<input type='image' src='/images/system/bb.gif'>";
		str ="<span class='disabledTxt'>" + I18N.CMC.label.edit+ "</span>";
	}
	return str;
}

function cancleClick() {
	window.parent.closeWindow("syslogManagement");
}

function updateRecordTypeLvl(topCcmtsSyslogRecordType, topCcmtsSyslogMinEvtLvl){
	var url = 'cmc/showUpdateRecordTypeLvl.tv?entityId='+entityId+'&topCcmtsSyslogRecordType='+topCcmtsSyslogRecordType+'&topCcmtsSyslogMinEvtLvl='+topCcmtsSyslogMinEvtLvl;
	window.top.createDialog('updateRecordTypeLvl', I18N.syslog.updateRecord, 600, 370, url, null, true, true);
}

function getSyslogParams(entityId){
	$.ajax({
		url: '/cmc/getSyslogParams.tv',
		data: {entityId:entityId},
    	type: 'POST',
    	dataType:"json",
   		success: function(cmcSyslogConfig) {
   			//为配置项赋值
   			if(cmcSyslogConfig!=null){
   	   			$("#topCcmtsSyslogMaxnum").val(cmcSyslogConfig.topCcmtsSyslogMaxnum);
   	   			$("#topCcmtsSyslogTrotInvl").val(cmcSyslogConfig.topCcmtsSyslogTrotInvl);
   	   			$("#topCcmtsSyslogTrotTrhd").val(cmcSyslogConfig.topCcmtsSyslogTrotTrhd);
   	   			$("#topCcmtsSyslogTrotMode").val(trotModelDscr(cmcSyslogConfig.topCcmtsSyslogTrotMode));
   			}
   		}, error: function() {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function saveConfig(){
	var topCcmtsSyslogSwitch = null;
	var topCcmtsSyslogTrotMode = trotModeCombo.getValue();
	if(topCcmtsSyslogTrotMode==""){
		var text = $("#topCcmtsSyslogTrotMode").val();
		if(text==I18N.syslog.unconstranined){
			topCcmtsSyslogTrotMode = 1;
		}else if(text==I18N.syslog.belowThreshold){
			topCcmtsSyslogTrotMode = 2;
		}else if(text==I18N.syslog.stopAtThreshold){
			topCcmtsSyslogTrotMode = 3;
		}else if(text==I18N.syslog.inhibited){
			topCcmtsSyslogTrotMode = 4;
		}
	}
	var topCcmtsSyslogMaxnum = $("#topCcmtsSyslogMaxnum").val();
	var topCcmtsSyslogTrotInvl = $("#topCcmtsSyslogTrotInvl").val();
	var topCcmtsSyslogTrotTrhd = $("#topCcmtsSyslogTrotTrhd").val();
	
	if(!validate(topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd)){return;}
	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.savingConfig);
	$.ajax({
		url: '/cmc/update8800ACmcSyslogConfig.tv',
		data: {entityId:entityId, topCcmtsSyslogMaxnum:topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl:topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd:topCcmtsSyslogTrotTrhd, topCcmtsSyslogTrotMode:topCcmtsSyslogTrotMode},
    	type: 'POST',
   		success: function() {
   			window.parent.closeWaitingDlg();
   			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.syslog.saveSuccess);
   		}, error: function() {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function getEntityData(){
	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.title.refreshDataFromEntity);
	$.ajax({
		url: '/cmc/getEntitySyslogConfigData.tv',
    	type: 'POST',
    	data: {entityId:entityId},
    	dataType: "json",
   		success: function(cmcSyslogConfig) {
   			//更新syslog配置项及记录方式
   			if(cmcSyslogConfig!=null){
   				if(cmcSyslogConfig.topCcmtsSyslogSwitch==0){
   	   				$("#off").attr("checked",true);
   	   			}else{
   	   				$("#on").attr("checked",true);
   	   			}
   	   			$("#topCcmtsSyslogMaxnum").val(cmcSyslogConfig.topCcmtsSyslogMaxnum);
   	   			$("#topCcmtsSyslogTrotInvl").val(cmcSyslogConfig.topCcmtsSyslogTrotInvl);
   	   			$("#topCcmtsSyslogTrotTrhd").val(cmcSyslogConfig.topCcmtsSyslogTrotTrhd);
   	   			$("#topCcmtsSyslogTrotMode").val(trotModelDscr(cmcSyslogConfig.topCcmtsSyslogTrotMode));
   			}
   			window.parent.closeWaitingDlg();
   			config_store.reload();
   		}, error: function() {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//国际化处理
function I18NConfig(){
	$("#config_loadData span").text(I18N.CMC.title.refreshDataFromEntity);
	$("#config_cancel span").text(I18N.cmc.mdfDisabled);
	$("#saveConfig span").text(I18N.CMC.button.save);
	$("#configTrotMode").text(I18N.syslog.configTrotMode);
	$("#threshold_td").text(I18N.syslog.thresholdModle);
	$("#syslogConfigParams").text(I18N.syslog.configParams);
	$("#maxnum_td").text(I18N.syslog.maxRecordNum);
	$("#trotInvl_td").text(I18N.syslog.trotInvl);
	$("#trotTrhd_td").text(I18N.syslog.trotTrhd);
}

Ext.onReady(function() {
	I18NConfig();
	
	//获取当前设备的syslog相关配置项参数
	getSyslogParams(entityId);
	
	var config_cm = new Ext.grid.ColumnModel([
		{header:I18N.syslog.recordType, dataIndex: 'topCcmtsSyslogRecordType', align:"center", width:180, resizable: false, renderer: eventTypeDscr},
		{header:I18N.syslog.eventLevel, dataIndex: 'topCcmtsSyslogMinEvtLvl', align:"center", width:270, resizable: false, renderer: eventLevelDscr},
       	{header:I18N.CHANNEL.operation, dataIndex: 'operator', align:"center", width:158, resizable: false, renderer: addOper}
       ]);
	config_store = new Ext.data.JsonStore({
       	url: 'cmc/getSyslogRecordType.tv',
       	baseParams: {entityId:entityId},
       	root: 'data',
       	totalProperty:'recordTypeNumber',
       	fields:['topCcmtsSyslogRecordType','topCcmtsSyslogMinEvtLvl']
       });
	config_store.load();
                          		
	var config_grid = new Ext.grid.EditorGridPanel({
       	store: config_store,
       	height:160,
       	bodyCssClass:'normalTable',
       	renderTo: 'config_grid',
       	cm: config_cm,
       	viewConfig: {
       		forceFit: true
       	},
       	loadMask: true
       });
	
	//获取所有的越界处理方式
	trotModeStore = new Ext.data.JsonStore({
	    url: '/cmc/getAllTrotMode.tv',
        fields: ['trotModeIndex', 'trotMode'],
        sortInfo:{field:'trotModeIndex',direction:'asc'}
	});	
	trotModeStore.load();
	//生成带有所有的越界处理方式的下拉菜单
	trotModeCombo = new Ext.form.ComboBox({
		id:"trotModeCombo",
        store: trotModeStore,
        disabled:!operationDevicePower,
        valueField: 'trotModeIndex',
        displayField: 'trotMode',
        triggerAction: 'all',
        editable: false,
        mode: 'local',
        applyTo : 'topCcmtsSyslogTrotMode'
	});
	authLoad();
});

function authLoad(){
	if(!operationDevicePower){
		$(".configInput").attr("disabled",true);
		$("#saveConfig").attr("disabled",true);
	}
}
</script>

</head>
<body class="openWinBody">
	<div class="edge10">
		<div class="pB5" id="config_grid"></div>
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="4" class="txtLeftTh" id="syslogConfigParams"></th>
		         </tr>
		     </thead>
		     <tbody>
		         <tr>
		             <td class="rightBlueTxt" id="maxnum_td"> 
		             </td>
		             <td>
		             	<input class="normalInput" id="topCcmtsSyslogMaxnum" type="text"/>
						<span class="params-tips">1~9999</span>
		             </td>
		             <td class="rightBlueTxt"  id="trotInvl_td">
		             </td>
		             <td>
		             	<input class="normalInput" id="topCcmtsSyslogTrotInvl" type="text"/>
						<span class="params-tips">1~3600</span>
		             </td>
		         </tr>
		         <tr>
		         	<td class="rightBlueTxt" id="trotTrhd_td"></td>
		         	<td>
		         		<input class="normalInput" id="topCcmtsSyslogTrotTrhd" type="text"/>
						<span class="params-tips">0~20000</span>
		         	</td>
		         	<td class="rightBlueTxt"></td>
		         	<td></td>
		         </tr>
		          <tr>
		         	<td class="rightBlueTxt"  id="threshold_td"></td>
		         	<td>
		         		<input class="normalInput" id="topCcmtsSyslogTrotMode" type="text" style="width: 200px;" />
		         	</td>
		         	<td colspan='2'>
		         		<span id="configTrotMode" class="params-tips"></span>
		         	</td>
		         </tr>
		         <tr>
		         	<td colspan='4'>
		         		<div class="noWidthCenterOuter clearBoth">
					    	<ol class="upChannelListOl pB0 pT0 noWidthCenter">
						    	<li><a id="saveConfig" onclick="saveConfig()" href="javascript:;" class="normalBtn"><span></span></a></li>
							</ol>
						</div>
		         	</td>
		         </tr>
		     </tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		         <li><a id="config_loadData" onclick="getEntityData()" href="javascript:;" class="normalBtnBig"><span></span></a></li>
		         <li><a onclick="cancleClick()" id="config_cancel" href="javascript:;" class="normalBtnBig"><span></span></a></li>
		     </ol>
		</div>
				
				
		
		
		
	</div>
</body>
</html>