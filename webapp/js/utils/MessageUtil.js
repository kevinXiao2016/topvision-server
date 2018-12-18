function getMessageFrame() {
	if (messageFrame == null) {
		messageFrame = ZetaUtils.getIframe("messageReceiver");
	}
	return messageFrame;
}
function getReceiverFrame() {
	if (messageFrame == null) {
		messageFrame = ZetaUtils.getIframe("messageReceiver");
	}
	return messageFrame;
}
//add by victor@20121225增加消息推送时的消息分发
var CALLBACK_CONTEXT = {};
function addCallback(type, callback, id) {
    var key;
    if (id) {
        key = type + "." + id;
    } else {
        key = type;
    }
    CALLBACK_CONTEXT[key] = callback;
}
function removeCallback(type, id) {
    var key;
    if (id) {
        key = type + "." + id;
    } else {
        key = type;
    }
    CALLBACK_CONTEXT[key] = null;
}
function callback(id, type, data) {
    var key;
    if (id) {
        key = type + "." + id;
    } else {
        key = type;
    }
    var callback = CALLBACK_CONTEXT[key];
    if (callback) {
        try {
            callback(data);
        } catch (err) {
            CALLBACK_CONTEXT[key] = null;
        }
    }
}
function onShowMessangerClick() {
	showMessanger();
}

function closeMessanger() {
	$.xTipWin.close($.xTipWin.opts);
	stopMusic()
}
function hideMessanger() {	
	closeMessanger();
}

function registerMessageReceiver( $receiveMessages ){
	WIN.top.GLOBAL_SOCKET_CONNECT_ID = random( 25 );
	GLOBAL_SOCKET = new TopvisionWebSocket('messagePusher', {
		params: {
			JCONNECTID : WIN.top.GLOBAL_SOCKET_CONNECT_ID,
			'userId' :userId
		},
	    onmessage: function (messagestr) {
	    	var $message = Ext.decode( messagestr );
			$receiveMessages( $message );
	    },
	    onclose: function() {
	    },
	    onerror: function(err) {
	    }
	});
}
function random(len) {
　　len = len || 32;
   /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
　　var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    
　　var $maxPos = $chars.length;
　　var $data = '';
　　for (i = 0; i < len; i++) {
　　　　$data += $chars.charAt(Math.floor(Math.random() * $maxPos));
　　}
　　return $data;
}

function stopEventDispatcher() {
	if (pingTimer != null) {
		clearInterval(pingTimer);
		pingTimer = null;
	}
}