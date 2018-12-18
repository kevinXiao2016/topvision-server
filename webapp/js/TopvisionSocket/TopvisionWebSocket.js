function TopvisionWebSocket(serviceName, config) {
	this.serviceName = serviceName;
	var defaultConfig = {
		onopen: function() {},
		onmessage: function() {},
		onclose: function() {},
		onerror: function() {}
	};
	Ext.applyIf(config, defaultConfig);
	this.config = config;
	
	this.connect();
}

TopvisionWebSocket.prototype.connect = function() {
	var _me = this;
	var serverIp = location.hostname;
	var _config = _me.config;

	// 需要判断是否支持websocket，如果不支持，使用flash版本的
	if (typeof WebSocket != 'undefined') {
	    initWebSocket();
	    _me.supportWebSocket = true;
	} else {
		// 使用flash版本
		initFlashSocket();
		_me.supportWebSocket = false;
	}
	
	function initWebSocket() {
		var url = 'ws://' + serverIp + ':3000/websocket/' + _me.serviceName;
		
		var firstParam = true;
		if(_config.params) {
			for(var key in _config.params) {
				if(firstParam) {
					url += '?' + key + '=' + _config.params[key];
					firstParam = false;
				} else {
					url += '&' + key + '=' + _config.params[key];
				}
			}
		}
		var socket = new WebSocket(url);
		_me.socket = socket;
		
		socket.onopen = function() {
	    	_config.onopen();
	    	
	    	// 开启心跳检测，以免一段时间后收不到消息自动失联
	    	heartbeat_timer = setInterval(function () {
	            keepalive(socket)
	        }, 10000);
	    	
	        function keepalive(socket) {
	        	socket.send('~H#B~');
	        }
	    };

	    socket.onmessage = function (message) {
	    	_config.onmessage(message.data);
	    };

	    socket.onclose = function() {
	    	_config.onclose();
	    	clearInterval(heartbeat_timer);
	    };

	    socket.onerror = function(err) {
	    	_config.onerror(err);
	    };
	}
	
	function initFlashSocket() {
		var socket = new TopvisionSocket(serverIp, top.socketServerPort, {
			onData:function (message) {
		    	_config.onmessage(message);
		    },
			onConnect:function(){
				_config.onopen();

				if(_config.params) {
					_me.send(_config.params);
				}
				
				
			},
			onError:function(){
				_config.onerror();
			},
			onClose:function(){
				_config.onclose();
			}
		});
		
		_me.socket = socket;
	}
	
}

TopvisionWebSocket.prototype.send = function(message) {
	if(this.supportWebSocket) {
		this.socket.send(JSON.stringify(message));
	} else {
		this.socket.sendRequest(this.serviceName, message, true);
	}
}

TopvisionWebSocket.prototype.close = function() {
	if(this.supportWebSocket) {
		this.socket.close();
	} else {
		this.socket.disconnect();
	}
}

TopvisionWebSocket.prototype.reconnect = function() {
	this.close();
	this.connect();
}