/**
 * 
 */

var nm3kAlertSend={nm3kAlertSendMsg:null,nm3kAlertSendIp:null,nm3kAlertSendMac:null,
			nm3kAlertTime:null,nm3kAlertTypeName:null}

function sendAlertMsgToUsers(data){
	var size,event,record,clear,nm3kAlertTypeId;
	for ( var i=0; i<data.length; i++){
		event = data[i];
		switch(event.levelId){
	        case 1:event.levelId="@ALERT.one@";
				break;
			case 2:event.levelId="@ALERT.two@";
				break;
			case 3:event.levelId="@ALERT.three@";
				break;
			case 4:event.levelId="@ALERT.four@";
				break;
			case 5:event.levelId="@ALERT.five@";
				break;
			case 6:event.levelId="@ALERT.five@";
			    break;
			}
		}
		if (event.levelId !=0) {
			record = event;
			nm3kAlertSend.nm3kAlertSendMsg = "@ALERT.appear@@COMMON.maohao@" + record.message + "\n"
					+ "@ALERT.level@@COMMON.maohao@" + record.levelId;
			nm3kAlertSend.nm3kAlertSendIp = (record.host == null ? '': record.host);
			nm3kAlertSend.nm3kAlertSendMac = record.source;
			nm3kAlertSend.nm3kAlertTime = record.firstTimeStr;
			nm3kAlertSend.nm3kAlertTypeName = record.typeName;
			nm3kAlertTypeId = record.typeId;
		} else if (event.levelId == 0) {
			clear = event;
			nm3kAlertSend.nm3kAlertSendMsg = "@ALERT.clear@@COMMON.maohao@" + clear.clearMessage;
			nm3kAlertSend.nm3kAlertSendIp = (clear.host == null ? '': clear.host);
			nm3kAlertSend.nm3kAlertSendMac = clear.source;
			nm3kAlertSend.nm3kAlertTime = clear.firstTimeStr;
			nm3kAlertSend.nm3kAlertTypeName = clear.typeName;
			nm3kAlertTypeId = clear.typeId;
		} 
//		else if (event.levelId == 0 && event.message == "@ALERT.snmpNormal@") {
//			nm3kAlertSend.nm3kAlertSendMsg = null;
//		}
		//    window.currentAlertRecord = record;
		if (nm3kAlertSend.nm3kAlertSendMsg != null) {
			$.ajax({
				url : '../fault/getSendingInfoOfUsers.tv',
				async : false,
				data : {
					AlertTypeId : nm3kAlertTypeId
				},
				success : function(msg) {
					if (msg) {
						sendMsg(msg, nm3kAlertSend);
					}
				},
				error : function() {
				}
			})
		}

	}

	function sendMsg(msg, sendMsg) {
		var userNameNeedAlert = new Array();
		var emailNeedAlert = new Array();
		var SMSNeedAlert = new Array();
		var chooseAlert = new Array();
		for (var i = 0; i < msg.data.length; i++) {
			userNameNeedAlert[i] = msg.data[i].userName;
			emailNeedAlert[i] = msg.data[i].email;
			SMSNeedAlert[i] = msg.data[i].mobile;
			chooseAlert[i] = msg.data[i].choose;
		}
		$.ajax({
			url : '../fault/sendAlertInfo.tv',
			async : false,
			data : {
				"userNameNeedAlert" : userNameNeedAlert,
				"emailNeedAlert" : emailNeedAlert,
				"SMSNeedAlert" : SMSNeedAlert,
				"chooseAlert" : chooseAlert,
				MsgNeedAlert : sendMsg.nm3kAlertSendMsg + "\n" + "IP:"
						+ sendMsg.nm3kAlertSendIp + "\n" + "@COMMON.time@@COMMON.maohao@"
						+ sendMsg.nm3kAlertTime
			},
			traditional : true,
			success : function() {
			},
			error : function() {
			}
		})
	}
