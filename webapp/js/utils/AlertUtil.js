/**
* # 描述
* 这是NM3000通用告警工具类
*
* **使用范例**：
*
*     @example
* @class AlertUtil
*/

/**
* 右下角弹窗显示告警
* @param {Object} o 告警对象 title: 标题(必选), html: 内容(必选), icoCls: ICON(可选)
*/
function nm3kAlertFn(o){
    if($("#nm3kMsgAlert").length == 0){
        nm3kMsgAlert = new Nm3kMsg({
            title: o.title,
            html: o.html,               
            okBtn : true ,
            okBtnTxt: "@button.text.viewAllSecurity@",
            cancelBtn : true,
            cancelBtnTxt : "@button.text.closeMessanger@",
            icoCls : o.icoCls ? o.icoCls : "nm3kMsg-alarm",
            autoHide: false,
            id: "nm3kMsgAlert",
            cancelBtnCallBack:stopMusic,
            closeBtnCallBack: stopMusic,
            okBtnCallBack : viewAllSecurity,
            unique: true
        })
        nm3kMsgAlert.init();
    }else{
        nm3kMsgAlert.update({
            html: o.html,
            icoCls : o.icoCls ? o.icoCls : "nm3kMsg-alarm"
        })
    }
}

/**
* 展示告警信息
* @param {Object} nm3kAlertObj 告警对象 title: 标题(必选), html: 内容(必选), icoCls: ICON(可选)
*/
function showMessanger() {
    var str = '<p style="padding-bottom:5px; margin-bottom:10px; color:#f00; border-bottom:1px solid #ccc;" class="wordBreak"><b style="cursor:pointer;" onclick="viewAllSecurity()" >'
        + nm3kAlertObj.msg +'</b></p><table  width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">'
        str += '<tr><td width="70" valign="top">@td.alertnotify.deviceAddress@</td><td class="wordBreak"><a class="yellowLink" href="javascript:;" onclick="showAlertDevice({0},{1},\'{2}\')">' + IpUtil.convertIpWithAddition(nm3kAlertObj.add) +'</a></td></tr>';
        str += '<tr><td valign="top">@td.alertnotify.source@</td><td class="wordBreak">'+ nm3kAlertObj.mac +'</td></tr>';
        str += '<tr><td valign="top">@td.alertnotify.occuretime@</td><td class="wordBreak">'+ nm3kAlertObj.time +'</td></tr>';
        str += '</table>';
        str = String.format(str, nm3kAlertObj.entityId, nm3kAlertObj.parentId,nm3kAlertObj.add);
    var levelIco = "nm3kMsgAlarm" + nm3kAlertObj.levelId;
    nm3kAlertFn({
    	icoCls : levelIco,
        //title: nm3kAlertObj.msg,
        html: str
    });
}

/**
 * 跳转到对应的告警设备
 * @param entityId
 */
function showAlertDevice(entityId, parentId,host){
    if(parentId !=null && parentId != 0){
        entityId = parentId;
    }
	window.top.addView('entity-' + entityId, host.split("[")[1].split("]")[0],
			'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + entityId);
}

/**
 * 只展示最后一个告警
 * @param list
 */
function addAlert(list) {
	var event = null;
	var record = null;
	var size = list.length;
	for (var i = 0; i < size; i++) {
		event = list[i];
		if (event.levelId > 0) {
			record = event;
		} else if (event.levelId == 0) {
			if (window.currentAlertId == event.alertId) {
				window.currentAlertId = 0;
				hideMessanger();
				stopMusic();
			}
	    }
	}
	window.currentAlertRecord = record;
	if (record != null) {
		if(supportGZ) {
			if($.inArray(record.typeId, concernAlerts) == -1) {
				return;
			}
		}
	    nm3kAlertObj.msg = record.message;
	    nm3kAlertObj.add = (record.host == null ? '' : record.host);
	    nm3kAlertObj.mac = record.source;
	    nm3kAlertObj.time = record.firstTimeStr;
	    nm3kAlertObj.typeName = record.typeName;
	    nm3kAlertObj.entityId = record.entityId;
	    nm3kAlertObj.levelId = record.levelId;
	    nm3kAlertObj.parentId = record.parentId;
    	showMessanger();
    	if(musicFlag){
    		var music = 'sounds/' + oAlertSound[record.levelId];
    		playMusic(music);
    	}
    }
}

function showAlertProperty(url) {
	createDialog("modalDlg", I18N.FAULT.eventProperty, 420, 400, url, null, true, true);
}

function nm3kAlertTips(o){
	try{
		closeWaitingDlg();
	}catch(err){}
	
	var showtime = tipShowTime ? (tipShowTime * 100) : 1000;
	var nTime = o.showTime ? o.showTime : showtime;
	if($("#nm3kAlertTips").length == 0){
		nm3kObj.nm3kAlertTips = new Nm3kMsg({
			title: o.title,
			html: o.html,
			okBtnTxt: "@resources/COMMON.ok@",
			okBtn : true,
			timeLoading: true,
			unique: true,
			showTime : nTime,
			id: "nm3kAlertTips"		
		});
		nm3kObj.nm3kAlertTips.init();
	}else{
		nm3kObj.nm3kAlertTips.update({
			html: o.html,
			title: o.title
		})
	}
};//end nm3kAlertTips;
function nm3kAlertTipsDie(){
	if($("#nm3kAlertTips").length == 1){
		nm3kObj.nm3kAlertTips.die();
	}
}
function viewAllSecurity() {
	$("#nm3kMsgAlert").remove();//点击后，先将弹出框移除掉;
	stopMusic()
	currentAlertType = currentAlertType  || 0;
	if (currentAlertType >= 550 && currentAlertType <= 551) {
		addView('illeagalAlert', title, 'illeagalIcon', 'security/illegalAlert.jsp');
	} else {
		showCurrentAlert(!!window.currentAlertRecord ? window.currentAlertRecord.alertId : 0);
	}
}

function showCurrentAlert(alertId) {
	window.alertId = typeof alertId == 'number' ? alertId:0
    addView("alertViewer", I18N.EVENT.alarmViewer, "alarmTabIcon", "alert/showCurrentAlertList.tv?alertId="+window.alertId ,null,true);	
}

function isClearAlert(data) {
	return !!data.clearTime;
}

function getAlertMessage(alert) {
	if(isClearAlert(alert)) {
		return alert.clearMessage || alert.message;
	} else {
		return alert.message;
	}
}