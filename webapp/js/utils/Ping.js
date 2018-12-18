function pingHandler() {
	var url = "ping.tv";
	if(ignoreTimeout){
		url += "?ignoreTimeout";
	}
    $.ajax({url: url, dataType: 'json', cache: false, success: pingSCCallback, error: pingFCallback});
}

function pingSCCallback(json) {
	pingFailureCount = 0;
	if (isServerShutDown) {
		location.href = "showlogon.tv";
	}
}
function pingFCallback(json) {
	pingFailureCount++;
	if (pingFailureCount == 1) {
		setTimeout("pingHandler()", 5000);
	} else if (pingFailureCount == 2) {
		isServerShutDown = true;
		showMessageDlg(I18N.COMMON.tip, I18N.COMMON.serverDisconnected);
		window.focus();
	}
}

function startServerPingDispatch() {
	if (pingTimer == null) {
		pingTimer = setInterval("pingHandler()", pingInterval);
	}
}