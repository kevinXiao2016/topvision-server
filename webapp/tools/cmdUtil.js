function showPingWindow(ip) {
	window.top.createDialog("modalDlg", 'Ping' + " - " + ip, 600, 400, 
		"entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
}

function showTracetWindow(ip) {
	window.parent.createDialog("modalDlg", 'Tracert ' + ip, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + ip, null, true, true);
}

var telnetClientType = null;
function showTelnetClient(entityId, ip) {
	if(telnetClientType === null) {
		// 获取telnet客户端类型
		$.ajax({
	        url: '/system/telnet/loadTelnetClientType.tv',
	        cache: false, 
	        async: false,
	        method:'post',
	        success: function(json) {
	        	telnetClientType = json;
	        	if(telnetClientType === '2') {
	        		showNativeTelnet(ip);
	        	} else {
	        		showInnerTelnet(entityId, ip);
	        	}
	        },
	        error: function(){
	        	// 获取客户端类型出错，默认展示内置Telnet客户端
	        	showInnerTelnet(ip);
	        }
	    });
	} else {
		if(telnetClientType === '2') {
    		showNativeTelnet(ip);
    	} else {
    		showInnerTelnet(entityId, ip);
    	}
	}
}

function showNativeTelnet(ip) {
	var str;
	if( $("#telnetIframe").length == 0 ){
		str = String.format('<div id="telnetIframe" style="width:1px; height:1px; overflow:hidden;"><iframe src="telnet://{0}"></iframe></div>', ip);
		$('body').append(str);
	}else{
		str = String.format('<iframe src="telnet://{0}"></iframe>', ip);
		$("#telnetIframe").html(str);
	}
}

function showInnerTelnet(entityId, ip) {
	window.top.createDialog('modalDlg', '@network/TelnetClient.innerClient@', 800, 500, 'system/telnet/showTelnetClient.tv?entityIp=' + ip + '&entityId=' + entityId, null, true, true);
}