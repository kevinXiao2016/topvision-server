<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css">
<div class="edge10" id="config_grid"></div>
<div class="params_div">
	<fieldset>
		<legend id="syslogConfigParams" ></legend>
		<table class="syslog_table">
			<tr>
				<td class="labelTd" id="maxnum_td"></td>
				<td class="valueTd">
					<input class="normalInput configInput" id="topCcmtsSyslogMaxnum" type="text"/>
					<span id="onlyApplyToLocal" class="params-tips"></span>
				</td>
				<td class="labelTd" id="trotInvl_td"></td>
				<td class="valueTd">
					<input class="normalInput configInput" id="topCcmtsSyslogTrotInvl" type="text"/>
					<span id="topCcmtsSyslogTrotInvl_unit"></span>
				</td>
			</tr>
			<tr>
				<td class="labelTd" id="trotTrhd_td"></td>
				<td class="valueTd">
					<input class="normalInput configInput" id="topCcmtsSyslogTrotTrhd" type="text"/>
				</td>
				<td class="labelTd" id="switch_td"></td>
				<td class="valueTd" style="vertical-align: middle;">
					<input id="on" type="radio" name="topCcmtsSyslogSwitch" value="1" style="vertical-align: middle;"/>
					<span id="on_span"></span>
					<input id="off" type="radio" name="topCcmtsSyslogSwitch" value="0" checked style="margin-left: 20px;vertical-align: middle;"/>
					<span id="off_span"></span>
				</td>
			</tr>
			<tr>
				<td class="labelTd" id="threshold_td"></td>
				<td class="valueTd">
					<input id="topCcmtsSyslogTrotMode" type="text" style="width: 200px;" />
				</td>
				<td class="valueTd" colspan="2">
					<span id="configTrotMode" class="params-tips"></span>
				</td>
			</tr>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl noWidthCenter" style="left: 95%;">
		         <li><a id="saveConfig" onclick="saveConfig()" href="javascript:;" class="normalBtn"><span></span></a></li>
		     </ol>
		</div>
	</fieldset>
</div>
<div class="noWidthCenterOuter clearBoth">
     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
         <li><a id="config_loadData" onclick="getEntityData()" href="javascript:;" class="normalBtnBig"><span></span></a></li>
         <li><a id="config_cancel" href="javascript:;" class="normalBtnBig" onclick="cancleClick()"><span></span></a></li>
     </ol>
</div>
<script type="text/javascript">
var config_store = null;
var entityId = ${entityId};
var trotModeStore = null;
var trotModeCombo = null;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var cmcId = ${cmcId};
var cmcType = ${cmcType};
var syslogII = ${syslogII};

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

function getLevelNameById(id) {
	var str;
    switch(id){
    case 1:
        str = 'emergency';
        break;
    case 2:
        str = 'alert';
        break;
    case 3:
        str = 'critical';
        break;
    case 4:
        str = 'error';
        break;
    case 5:
        str = 'warning';
        break;
    case 6:
        str = 'notice';
        break;
    case 7:
        str = 'information';
        break;
    case 8:
        str = 'debug';
        break;
    case 28:
        str =  I18N.syslog.noneLevel;
        break;
    }
    return str;
}

function getRecordTypeII(value, p, record) {
	var data = record.data,
	   local = data.local,
	   localVolatile = data.localVolatile,
	   syslog = data.syslog,
	   traps = data.traps;
	
	var supportTypes = [];
	if(local) {
		supportTypes.push('local');
	}
	if(traps) {
        supportTypes.push('traps');
    }
	if(syslog) {
        supportTypes.push('syslog');
    }
	if(localVolatile) {
        supportTypes.push('localVolatile');
    }
	return supportTypes.join(', ');
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
		str = String.format("<a href='javascript:;' onclick='updateRecordTypeLvl(\"{0}\", \"{1}\")'>" + I18N.CMC.label.edit +"</a> ", 
                record.get("topCcmtsSyslogRecordType"), record.get("topCcmtsSyslogMinEvtLvl"));
		
		//str ="<input type='image' src='/images/system/bb.gif' onclick='updateRecordTypeLvl(\""+record.get('topCcmtsSyslogRecordType')+"\",\""+record.get('topCcmtsSyslogMinEvtLvl')+"\")'>";
	}else{
		str = "<span class='disabledTxt'>" + I18N.CMC.label.edit +"</span>";
		//str ="<input type='image' src='/images/system/bb.gif'>";
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
   			    $(":radio[name='topCcmtsSyslogSwitch']").each(function (){
                   if(this.value == cmcSyslogConfig.topCcmtsSyslogSwitch){
                        this.checked = true;
                    }
                });
   				if(cmcSyslogConfig.topCcmtsSyslogSwitch ==null && cmcSyslogConfig.topCcmtsSyslogSwitch==0){
   	   				$("#off").attr("checked",true);
   	   			}else if(cmcSyslogConfig.topCcmtsSyslogSwitch!=null && cmcSyslogConfig.topCcmtsSyslogSwitch==1){
   	   				$("#on").attr("checked",true);
   	   			}
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
	var topCcmtsSyslogSwitch = $(":radio[name='topCcmtsSyslogSwitch'][checked=true]").val();
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
		}else{
			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.syslog.pleaseSelectMode);
			return false;
		}
	}
	var topCcmtsSyslogMaxnum = $("#topCcmtsSyslogMaxnum").val();
	var topCcmtsSyslogTrotInvl = $("#topCcmtsSyslogTrotInvl").val();
	var topCcmtsSyslogTrotTrhd = $("#topCcmtsSyslogTrotTrhd").val();
	
	var data;
	if(EntityType.isCcmtsWithoutAgentType(cmcType)){
		data = {entityId:entityId, topCcmtsSyslogMaxnum:topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl:topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd:topCcmtsSyslogTrotTrhd, topCcmtsSyslogTrotMode:topCcmtsSyslogTrotMode};
	}else{
		data = {entityId:entityId, topCcmtsSyslogSwitch:topCcmtsSyslogSwitch, topCcmtsSyslogMaxnum:topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl:topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd:topCcmtsSyslogTrotTrhd, topCcmtsSyslogTrotMode:topCcmtsSyslogTrotMode};
	}
	
	if(!validate(topCcmtsSyslogMaxnum, topCcmtsSyslogTrotInvl, topCcmtsSyslogTrotTrhd)){return;}
	window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.savingConfig);
	$.ajax({
		url: '/cmc/updateCmcSyslogConfig.tv',
		data: data,
    	type: 'POST',
   		success: function() {
   			window.parent.closeWaitingDlg();
   			//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.syslog.saveSuccess);
   			top.afterSaveOrDelete({
   				title: I18N.CMC.tip.tipMsg,
   				html: '<b class="orangeTxt">' + I18N.syslog.saveSuccess +'</b>'
   			});
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
   			top.afterSaveOrDelete({
					title: I18N.COMMON.tip,
					html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshDataFromEntitySuccess + '</b>'
			});
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
	$("#config_loadData span").html('<i class="miniIcoEquipment"></i>' + I18N.CMC.title.refreshDataFromEntity);
	$("#config_cancel span").html('<i class="miniIcoWrong"></i>' + I18N.cmc.mdfDisabled);
	$("#saveConfig span").html('<i class="miniIcoData"></i>' + I18N.CMC.button.save);
	$("#configTrotMode").text(I18N.syslog.configTrotMode);
	$("#threshold_td").text(I18N.syslog.thresholdModle);
	$("#syslogConfigParams").text(I18N.syslog.configParams);
	$("#onlyApplyToLocal").text(I18N.syslog.recordNumberTip);
	$("#topCcmtsSyslogTrotInvl_unit").text(I18N.CMC.label.seconds);
	$("#maxnum_td").text(I18N.syslog.maxRecordNum);
	$("#trotInvl_td").text(I18N.syslog.trotInvl);
	$("#trotTrhd_td").text(I18N.syslog.trotTrhd);
	$("#switch_td").text(I18N.syslog.switchIsEnable);
	$("#on_span").text(I18N.syslog.switchEnable);
	$("#off_span").text(I18N.syslog.switchDisable);
}

function showSyslogI() {
	var config_cm = new Ext.grid.ColumnModel([
        {header:I18N.syslog.recordType, dataIndex: 'topCcmtsSyslogRecordType', align:"center", resizable: false, renderer: eventTypeDscr},
        {header:I18N.syslog.eventLevel, dataIndex: 'topCcmtsSyslogMinEvtLvl', align:"center", resizable: false, renderer: eventLevelDscr},
        {header:I18N.CHANNEL.operation, dataIndex: 'operator', align:"center", resizable: false, renderer: addOper}
    ]);
    
    config_store = new Ext.data.JsonStore({
        url: 'cmc/getSyslogRecordType.tv',
        baseParams: {entityId:entityId},
        root: 'data',
        totalProperty:'recordTypeNumber',
        fields:['topCcmtsSyslogRecordType','topCcmtsSyslogMinEvtLvl']
       });
    config_store.load();
    
    var bbar = new Ext.Toolbar(['->', '-',{
        iconCls : 'bmenu_miniIcoBack',
        id:'undoDefault',
        text: I18N.syslog.undoDefault ,
        handler: function(){
            window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.savingConfig);
            $.ajax({
                url: '/cmc/undoAllSyslogEvtLvls.tv',
                type: 'POST',
                data: {entityId:entityId},
                success: function() {
                    //恢复默认成功后
                    window.parent.closeWaitingDlg();
                    //刷新store
                    config_grid.store.reload();
                    top.afterSaveOrDelete({
                        title: I18N.CMC.tip.tipMsg,
                        html: '<b class="orangeTxt">' + I18N.syslog.undoSucc + '</b>'
                    });
                }, error: function() {
                    window.parent.closeWaitingDlg();
                    window.parent.showErrorDlg();
                },cache: false,
                complete: function (XHR, TS) { XHR = null }
            });
        }
    }, '-']);
                                
    var config_grid = new Ext.grid.EditorGridPanel({
        store: config_store,
        height:188,
        renderTo: 'config_grid',
        cm: config_cm,
        loadMask: true,
        bodyCssClass: 'normalTable',
        bbar: bbar,
        viewConfig:{
            forceFit:true
        }
    });
}

function showSyslogII() {
	var config_cm = new Ext.grid.ColumnModel([
        {header:I18N.syslog.eventLevel, dataIndex: 'evPriority', align:"center", resizable: false, renderer: getLevelNameById},
        {header:"<div class='txtCenter'>" + I18N.syslog.recordType + "</div>", dataIndex: 'evReporting', align:"left", resizable: false, renderer: getRecordTypeII},
        {header:I18N.CHANNEL.operation, dataIndex: 'operator', align:"center", resizable: false, renderer: renderOperationII}
    ]);
    
    config_store = new Ext.data.JsonStore({
        url: 'cmc/loadSyslogRecordTypeII.tv',
        baseParams: {entityId:entityId},
        root: 'data',
        totalProperty:'recordTypeNumber',
        fields:['evPriority', 'evReporting', 'reporting', 'local', 'traps', 'syslog', 'localVolatile']
       });
    config_store.load();
    
    var config_grid = new Ext.grid.EditorGridPanel({
        store: config_store,
        height:188,
        renderTo: 'config_grid',
        cm: config_cm,
        loadMask: true,
        bodyCssClass: 'normalTable',
        viewConfig:{
            forceFit:true
        }
    });
}

function renderOperationII(value, p, record) {
	var str;
    if(operationDevicePower){
        str = String.format("<a href='javascript:;' onclick='showOperII(\"{0}\", \"{1}\", \"{2}\", \"{3}\", \"{4}\")'>" + 
        	    I18N.CMC.label.edit +"</a> ", record.get("evPriority"), record.get("local"), record.get("traps"), 
        	    record.get("syslog"), record.get("localVolatile"));
        
    }else{
        str = "<span class='disabledTxt'>" + I18N.CMC.label.edit +"</span>";
    }
    return str;
}

function showOperII(evPriority, local, traps, syslog, localVolatile) {
    var url = 'cmc/showUpdateRecordTypeLvlII.tv?entityId=' + entityId + '&evPriority=' + evPriority + 
    		'&local=' + local + '&traps=' + traps + '&syslog=' + syslog + '&localVolatile=' + localVolatile;
    window.top.createDialog('updateRecordTypeLvlII', I18N.syslog.updateRecord, 600, 370, url, null, true, true, function() {
    	config_store.reload();
    });
}

Ext.onReady(function() {
	I18NConfig();
	if(EntityType.isCcmtsWithoutAgentType(cmcType)){
		$('#switch_td').next().andSelf().hide();
	}
	
	//获取当前设备的syslog相关配置项参数
	getSyslogParams(entityId);
	
	//TODO 根本不同版本，展示不同类型的配置表格
	if(syslogII) {
		showSyslogII();
	} else {
		showSyslogI();
	}
	
	//获取所有的越界处理方式
	trotModeStore = new Ext.data.JsonStore({
	    url: '/cmc/getAllTrotMode.tv',
        fields: ['trotModeIndex', 'trotMode'],
        sortInfo:{field:'trotModeIndex',direction:'asc'}
	});	
	trotModeStore.load();
	//生成带有所有的越界处理方式的下拉菜单
	trotModeCombo = new Ext.form.ComboBox({
        store: trotModeStore,
        valueField: 'trotModeIndex',
        disabled:!operationDevicePower,
        displayField: 'trotMode',
        triggerAction: 'all',
        editable: false,
        mode: 'local',
        applyTo : 'topCcmtsSyslogTrotMode'
	});
	
	//给输入框添加事件
	$(".configInput").bind('focus blur click',function(event){
		var msg;
		if(event.target.id=='topCcmtsSyslogMaxnum'){
			msg = I18N.syslog.topCcmtsSyslogMaxnumRule;
		}else if(event.target.id=='topCcmtsSyslogTrotInvl'){
			msg = I18N.syslog.topCcmtsSyslogTrotInvlRule;
		}else if(event.target.id=='topCcmtsSyslogTrotTrhd'){
			msg = I18N.syslog.topCcmtsSyslogTrotTrhdRule;
		}
		if (event.type == 'focus') {
		_inputFocus(event.target.id, msg);
		};
		if (event.type == 'blur') {
		 _inputBlur(this);
		};
		if (event.type == 'click') {
		 _clearOrSetTips(this);
		};
	});
	authLoad();
});

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#undoDefault").attr("disabled",true);
		$("#config_loadData").attr("disabled",false);
		$("#config_cancel").attr("disabled",false);
		$("#saveConfig").attr("disabled",true);
	}
    if(!refreshDevicePower){
        $("#config_loadData").attr("disabled",true);
    }
}
</script>
