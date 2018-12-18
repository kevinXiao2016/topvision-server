// 呼叫前转类型 0-disable 1-busy;2-no reply;3-unconditional
function renderForwardType(value, p, record) {
	switch (value) {
	case 0:return 'disable';
	case 1:return 'busy';
	case 2:return 'no reply';
	case 3:return 'unconditional';
	default:
		return '--';
	}
}

//只有no reply类型下才有超时时间
function renderForwardTime(value, p, record) {
	if (record.data.topSIPPstnUserForwardType == 2) {
		return value;
	} else {
		return '--';
	}
}

// 编码模式
function renderCodec(value, p, record) {
	var codeStr = '--';
	switch (value) {
	case 0:
		codeStr = 'pcmu';
		break;
	case 1:
		codeStr = 'reserved1';
		break;
	case 2:
		codeStr = 'reserved2';
		break;
	case 3:
		codeStr = 'gsm';
		break;
	case 4:
		codeStr = 'g723';
		break;
	case 5:
		codeStr = 'dvi4-8k';
		break;
	case 6:
		codeStr = 'dvi4-16k';
		break;
	case 7:
		codeStr = 'lpc';
		break;
	case 8:
		codeStr = 'pcma';
		break;
	case 9:
		codeStr = 'g722';
		break;
	case 10:
		codeStr = 'l16-2c';
		break;
	case 11:
		codeStr = 'l16-1c';
		break;
	case 12:
		codeStr = 'qcelp';
		break;
	case 13:
		codeStr = 'cn';
		break;
	case 14:
		codeStr = 'mpa';
		break;
	case 15:
		codeStr = 'g728';
		break;
	case 16:
		codeStr = 'dvi4-11k';
		break;
	case 17:
		codeStr = 'dvi4-22k';
		break;
	case 18:
		codeStr = 'g729';
		break;
	}
	return codeStr;
}

// VOIP业务服务状态
function renderServStatus(value, p, record) {
	var serverStatusStr = '--';
	switch (value) {
	case 0:
		serverStatusStr = 'None/initial';
		break;
	case 1:
		serverStatusStr = 'Registered';
		break;
	case 2:
		serverStatusStr = 'In session';
		break;
	case 3:
		serverStatusStr = 'Failed registration - icmp error';
		break;
	case 4:
		serverStatusStr = 'Failed registration - failed tcp';
		break;
	case 5:
		serverStatusStr = 'Failed registration - failed authentication';
		break;
	case 6:
		serverStatusStr = 'Failed registration - timeout';
		break;
	case 7:
		serverStatusStr = 'Failed registration - server fail code';
		break;
	case 8:
		serverStatusStr = 'Failed invite - icmp error';
		break;
	case 9:
		serverStatusStr = 'Failed invite - failed tcp';
		break;
	case 10:
		serverStatusStr = 'Failed invite - failed authentication';
		break;
	case 11:
		serverStatusStr = 'Failed invite - timeout';
		break;
	case 12:
		serverStatusStr = 'Failed invite - server fail code';
		break;
	case 13:
		serverStatusStr = 'Port not configured';
		break;
	case 14:
		serverStatusStr = 'Config done';
		break;
	case 15:
		serverStatusStr = 'Disabled by switch';
		break;
	}
	return serverStatusStr;
}

// 会话类型
function renderSessType(value, p, record) {
	var sessTypeStr = '--';
	switch (value) {
	case 0:
		sessTypeStr = 'Idle/none';
		break;
	case 1:
		sessTypeStr = '2way';
		break;
	case 2:
		sessTypeStr = '3way';
		break;
	case 3:
		sessTypeStr = 'Fax';
		break;
	case 4:
		sessTypeStr = 'Telem';
		break;
	case 5:
		sessTypeStr = 'Conference';
		break;
	}
	return sessTypeStr;
}

// Voip线路状态
function renderLineState(value, p, record) {
	var lineStateStr = '--';
	switch (value) {
	case 0:
		lineStateStr = 'Idle, on-hook';
		break;
	case 1:
		lineStateStr = 'Off-hook dial tone';
		break;
	case 2:
		lineStateStr = 'Dialling';
		break;
	case 3:
		lineStateStr = 'Ringing or FSK alerting/data';
		break;
	case 4:
		lineStateStr = 'Audible ringback';
		break;
	case 5:
		lineStateStr = 'Connecting';
		break;
	case 6:
		lineStateStr = 'Connected';
		break;
	case 7:
		lineStateStr = 'Disconnecting, audible indication';
		break;
	case 8:
		lineStateStr = 'Receiver off hook (ROH), no tone';
		break;
	case 9:
		lineStateStr = 'ROH with tone';
		break;
	case 10:
		lineStateStr = 'Unknown or undefined';
		break;
	}
	return lineStateStr;
}

//端口使能
function renderPortAdmin(value, p, record){
	if (value == 1) {
		return String.format('<img class="switch" title="@COMMON.open@" src="/images/performance/on.png" onclick="openPortAdmin({0},{1},2)" border=0 align=absmiddle>',
				record.data.onuId,record.data.topSIPPstnUserPotsIdx);	
	} else {
		return String.format('<img class="switch" title="@COMMON.close@" src="/images/performance/off.png" onclick="openPortAdmin({0},{1},1)" border=0 align=absmiddle>',
				record.data.onuId,record.data.topSIPPstnUserPotsIdx);	
	}
}


//IP接口索引
function renderIpIndx(value, p, record){
	if (value == null || value == "") {
		return '--';
	}
	return value;
}
//IP地址
function renderIpAddress(value, p, record){
	if (value == null || value == '') {
		return '--';
	}
	return value;
}
//VLAN优先级
function renderVlanTagPriority(value, p, record){
	if (value == null || value == '') {
		return '--';
	}
	return value;
}
//VLAN优先级
function renderVlanPVid(value, p, record){
	if (value == null || value == '') {
		return '--';
	}
	return value;
}

// 从设备获取POTS数据
function refreshPotsInfo() {
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@");
	$.ajax({
		url : "/gpon/onuvoip/refreshGponOnuPotsInfo.tv",
		cache : false,
		data : {
			onuId : onuId
		},
		success : function() {
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '<b class="orangeTxt">@COMMON.fetchOk@</b>'
			});
			store.reload();
		},
		error : function() {
			window.parent.showMessageDlg("@COMMON.tip@", "@onu/EPON.dataError@!")
		}
	})
}

//POTS口 管理状态更改
function openPortAdmin(onuId,topSIPPstnUserPotsIdx,potsAdminStatus) {
	//if(!operationDevicePower)return;
    var action =  potsAdminStatus == 1 ? "@COMMON.open@" : "@COMMON.close@";
    window.parent.showConfirmDlg("@COMMON.tip@",  String.format( "@epon/SUPPLY.cfmActionPortEn@" , action ), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg("@COMMON.wait@", String.format( "@epon/SUPPLY.settingPortEn@", action), 'ext-mb-waiting');
        $.ajax({
            url: '/gpon/onuvoip/setOnuPotsAdminStatus.tv',
            data: "onuId=" + onuId + "&topSIPPstnUserPotsIdx=" + topSIPPstnUserPotsIdx + "&potsAdminStatus=" + potsAdminStatus,
            success: function(json) {
            	window.parent.closeWaitingDlg();
                if (json.message) {
                    window.parent.showMessageDlg("@COMMON.tip@", json.message);
                } else {
                	store.reload();
                }
            },
            error: function() {
                window.parent.showMessageDlg("@COMMON.tip@", action + "@epon/EPON.portEnableError@")
            },cache: false
        });
    });
}

