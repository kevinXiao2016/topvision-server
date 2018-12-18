function renderCommon(value){
	return value || '--';
}

function renderEponMac(value, m, record){
	if(record.data.onuEorG == 'E'){
		return renderCommon(value);
	}else {
		return '--';
	}
}

function renderGponSn(value, m, record){
	if(record.data.onuEorG == 'G'){
		return renderCommon(value);
	}else {
		return '--';
	}
}

function renderName(value,m,record){
	if(!record.data.attention){
		 return String.format('<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', value);
	}else{
		return String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', value);
	}
}

function renderOnline(value, m, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<p class="nm3kTip" nm3kTip="{0}"> <img src="../images/fault/trap_off.png" border=0 align=absmiddle> <font>{1}</font></p>',
			"@COMMON.offline@", renderOffRenson(record.data.lastOfflineReason));	
	}
}

function renderLaser(value){
	if (value == 1) {
		return '<img nm3kTip=@COMMON.laserOn@ class=nm3kTip src="../images/fault/trap_on.png" border=0 align=absmiddle>';	
	} else if (value == 2){
		return '<img nm3kTip=@COMMON.laserOff@ class=nm3kTip src="../images/fault/trap_off.png" border=0 align=absmiddle>';	
	}else{
		return '--'
	}
}

function renderOffRenson(value){
	var offReason;
	switch(value)
	{
		case 1 : offReason = '@ONU.OFFLINE_POWER_OFF@';break;
		case 2 : offReason = '@ONU.OFFLINE_FIBER_BREAK@';break;
		case 3 : offReason = '@ONU.OFFLINE_RESET@';break;
		case 4 : offReason = '@ONU.OFFLINE_DEREGISTER@'; break;
		case 5 : offReason = '@ONU.OFFLINE_OTHER@'; break;
		default: offReason = '--';
	}
	return offReason;
}

function renderOnOffTime(value, m, record){
	var onuRunTime = record.data.onuRunTime;
	return "<label class='onuOnOffRecordsTip'>" + onuRunTime + "</label> " + "<input class='onuIdValue' type='text' style='display:none' value='" + record.data.onuId + "'/>";
}

function renderOlt(value,m,record){
    var disValue = record.data.manageName+ "("+ value+")";
	return String.format('<a href="#" onclick=\'showOlt("' + record.data.entityId + '","' + escape(record.data.entityIp) + '");\'>{0}</a>',
	        disValue);
	
}

function renderLogicLoc(value, m, record){
	return getLocationByIndex(value, "onu");
}

function renderCpe(value, p, record, rowIndex){
	var id = 'expanderSpan' + rowIndex;
	return "<span id='" + id + "' class='cpeCollapse' onclick='clickCpe()'></span><span class='cpeNum'>" + value + "</span>" ;
}

function macTypeRender(v){
	switch(v){
	case 1:
		return '@ONU.static@';
	case 2:
		return '@ONU.dynamic@';
	default:
		return v;
	}
}

function cpeTypeRender(v){
	switch(v){
	case 1:return "host";
	case 2:return "mta";
	case 3:return "stb";	
	default:return "--";
	}
}

//实时速率
function currentRateRender(value){
	if(value === null || value === -1 || value === 0){
		return "--Mbps";
	}else{
		return value+"Mbps";
	}
}

//侧滑栏功率值颜色渲染
function renderOnuPonRec0(v,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="ONU_PON_RE_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				$("#onuPonRevPower").text("--").css("color","black");
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				$("#onuPonRevPower").text(v+"dBm").css("color","red");
			} else{
				$("#onuPonRevPower").text(v+"dBm").css("color","black");
			}
		}
	}
}

function renderOnuPonTrans0(v,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="ONU_PON_TX_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				$("#onuPonTransPower").text("--").css("color","black");
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				$("#onuPonTransPower").text(v+"dBm").css("color","red");
			} else{
				$("#onuPonTransPower").text(v+"dBm").css("color","black");
			}
		}
	}
}

function renderOltPonRec0(v,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="OLT_PONLLID_RE_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				$("#oltPonRevPower").text("--").css("color","black");
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				$("#oltPonRevPower").text(v+"dBm").css("color","red");
			} else{
				$("#oltPonRevPower").text(v+"dBm").css("color","black");
			}
		}
	}
}

function renderOltPonTrans0(v,r) {
	if(r.data.onuOperationStatus != 1 ||  v == null ||  v == 0){
		$("#oltPonTransPower").text("--");
	} else{
		$("#oltPonTransPower").text(v+"dBm");
	}
}

function renderCATVRec0(v,r){
	v = (Number(v)/10).toFixed(2);
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId==="ONU_CATV_RX_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || v == 0|| v < -20){
				$("#onuCatvOrInfoRxPower").text("--").css("color","black");
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				$("#onuCatvOrInfoRxPower").text(v+"dBm").css("color","red");
			} else{
				$("#onuCatvOrInfoRxPower").text(v+"dBm").css("color","black");
			}
		}
	}
}