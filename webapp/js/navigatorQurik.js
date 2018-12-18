//识别呈现引擎和浏览器以及平台

var client = function(){ 
	var engine = { 
		//呈现引擎 
		ie: 0, 
		gecko: 0, 
		webkit: 0, 
		khtml: 0, 
		opera: 0, 

		//具体的版本号 
		ver: null 
	}; 

	var browser = {         
		//浏览器         
		ie: 0,         
		firefox: 0,         
		safari: 0,         
		konq: 0,         
		opera: 0,         
		chrome: 0,         

		//具体的版本         
		ver: null     
	};

	//在此检测呈现引擎、平台和设备 
	return { 
		engine : engine,
		browser: browser
	};
}();

(function(){
	var ua = navigator.userAgent;
	if (window.opera){ 
		client.engine.ver = client.browser.ver = window.opera.version(); 
		client.engine.opera = client.browser.opera = parseFloat(engine.ver);
	} else if (/AppleWebKit\/(\S+)/.test(ua)){ 
		client.engine.ver = RegExp["$1"]; 
		client.engine.webkit = parseFloat(engine.ver);

		//确定是Chrome还是Safari 
		if (/Chrome\/(\S+)/.test(ua)){ 
			client.browser.ver = RegExp["$1"]; 
			client.browser.chrome = parseFloat(browser.ver); 
		} else if (/Version\/(\S+)/.test(ua)){ 
			client.browser.ver = RegExp["$1"]; 
			client.browser.safari = parseFloat(browser.ver); 
		} else { 
			//近似地确定版本号 
			var safariVersion = 1; 
			if (engine.webkit < 100){ 
				safariVersion = 1; 
			} else if (engine.webkit < 312){ 
				safariVersion = 1.2; 
			} else if (engine.webkit < 412){ 
				safariVersion = 1.3; 
			} else { 
				safariVersion = 2;
			} 
			client.browser.safari = client.browser.ver = safariVersion;
		}
	} else if (/KHTML\/(\S+)/.test(ua)) { 
		client.engine.ver = client.browser.ver = RegExp["$1"]; 
		client.engine.khtml = client.browser.konq = parseFloat(client.engine.ver);
	} else if (/rv:([^\)]+)\) Gecko\/\d{8}/.test(ua)){ 
		client.engine.ver = RegExp["$1"]; 
		client.engine.gecko = parseFloat(client.engine.ver);

		//确定是不是Firefox 
		if (/Firefox\/(\S+)/.test(ua)){ 
			client.browser.ver = RegExp["$1"]; 
			client.browser.firefox = parseFloat(client.browser.ver); 
		}
	} else if (/MSIE ([^;]+)/.test(ua)){ 
		client.engine.ver = client.browser.ver = RegExp["$1"];
		client.engine.ie = client.browser.ie = parseFloat(client.engine.ver); 
	}
})();