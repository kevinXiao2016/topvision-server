var term;
var status; // 表示当前处于哪个状态

var lastQuestionStr = '';
var keyStack = [];
$(function() {
	// 初始化页面
	$('#cmd-container').height($('body').height()-40);
	term = $('#cmd-container').terminal(function(command, term) {
		if(command.endWith('\t')) {
			command = trimLeft(command);
		} else {
			command = $.trim(command);
		}
		
		if(command.startWith('open')) {
			ip = $.trim(command.split(' ')[1]);
			// 尝试telnet登录设备
			connect(ip);
		} else if(!ip) {
			// don't do anything
		} else if(status == 'username') {
            sendUsername(command);
        } else if(status == 'password' || status == 'enable password' || status == 'login password') {
            sendPassword(command);
        } else if(status == 'view' && (command === 'exit' || command === 'quit')) {
        	//closeConnect();
        	sendCommand(command);
        } else if(command === 'cls') {
        	term.exec('clear');
        } else if(status !== 'error' || command !== ''){
            sendCommand(command);
        }
	}, {
        greetings: '',
        name: 'demo',
        height: $('#cmd-container').height()-20,
        prompt: 'Telnet>',
        exit: false,
        keydown: function(e, term) {
        	if(e.keyCode == 9) {
        		// tab
        		return false;
        		// 只有进入视图后才能使用
//        		if(status == '' || status === 'error' || status === 'username' || status === 'password') {
//        			return
//        		}
//        		if(term.get_command()!= '') {
//        			term.exec(term.get_command() + '\t');
//        		}
        	}
        },
        keypress: function(e, term) {
        	if(e.keyCode == 63) {
        		// ?
        		// 只有进入视图后才能使用
        		if(status == '' || status === 'error' || status === 'username' || status === 'password') {
        			return
        		}
        		term.exec(term.get_command() + '?');
        	}
        }
    });
	
	// 如果有初始化ip，则需要尝试直接登录
	// 从设备telnet连接配置中获取对应ip的登录信息
	if(ip) {
		autoLogin(ip, interfaceStr);
	} else {
		term.insert('open ', {
            raw: true
        });
		$('.clipboard').focus();	
	}
});

function trimLeft(str) {
	var i = 0;
	for(var len=str.length; i<len; i++) {
		if(str[i] !== ' ') {
			break;
		}
	}
	return str.substr(i);
}

var inputQueue = [];
function autoLogin(ip, interfaceStr) {
	$.ajax({
        url: '/system/telnet/loadLoginConfig.tv',
        cache: false, 
        async: false,
        method:'post',
        data: {
        	entityIp: ip
        },
        success: function(loginInfo) {
        	if(loginInfo) {
        		window.loginInfo = loginInfo;
        		term.exec('open ' + loginInfo.ipString);
        		var count = 0;
        		
        		window.autoLoginInterval = setInterval(function() {
        			if(term.paused()) {
        				return;
        			}
        			// 什么情况下跳出循环
        			// 1、出错
        			// 2、有用户名和密码，没有enable密码，不需要进入CC/ONU视图，进入了view视图
        			// 3、有用户名、密码、enable密码，不需要进入CC/ONU视图，进入 了config视图
        			// 4、有用户名、密码、enable密码，需要进入CC/ONU视图，进入 了cc/ONU视图
        			// 5、出现故障，没有正确退出，已经重复10次
        			if(status === 'error' 
        				|| (loginInfo.userName && loginInfo.password && !loginInfo.enablePassword && !interfaceStr && status === 'view')
    					|| (loginInfo.userName && loginInfo.password && loginInfo.enablePassword && !interfaceStr && status === 'enable')
    					|| (loginInfo.userName && loginInfo.password && loginInfo.enablePassword && interfaceStr && status === 'otherView')
    					|| ++count > 10) {
        				clearInterval(window.autoLoginInterval);
        				$('.clipboard').focus()
        			} else if(status === 'username') {
        				term.exec(loginInfo.userName);
        			} else if(status === 'enable password') {
        				term.exec(loginInfo.enablePassword);
        			} else if(status === 'login password') {
        				term.exec(loginInfo.password);
        			} else if(status === 'password') {
        				term.exec(loginInfo.password);
        			} else if(status === 'view' && loginInfo.enablePassword) {
        				term.exec('enable');
        			} else if(status === 'enable' && interfaceStr) {
        				term.exec('configure terminal');
        			} else if(status === 'configure terminal' && interfaceStr) {
        				term.exec(interfaceStr);
        			}
        		}, 500);
        		
        	} else {
        		delayExcute(function() {
        			// 进行登录尝试, 连接指定设备
        			term.exec('open ' + ip);
        			$('.clipboard').focus()
        		});
        	}
        },
        error: function(){
        	// 告知无法获取登录配置
        	delayExcute(function() {
    			// 进行登录尝试, 连接指定设备
    			term.exec('open ' + ip);
    			$('.clipboard').focus()
    		});
        }
    });
}

function showTelnetRecord() {
	window.parent.addView("telnet-record-tab", "@TelnetClient.innerTypeRecord@", "icoE4", "/system/telnet/showTelnetRecord.tv");
}

function connect(ip, callback) {
	term.pause();
	$.ajax({
        url: '/system/telnet/connect.tv',
        cache: false, 
        timeout: 10000,
        method:'post',
        data: {
        	entityIp: ip
        },
        success: function(response) {
        	term.resume();
        	term.clear();
        	analyzeEcho(response);
        	pwdTryCount = 0;
            if(callback && typeof callback === 'function') {
            	callback();
            }
        },
        error: function(){
        	term.resume();
        	echoDisconnect();
        }
    });
}

function echoDisconnect() {
	// 连接出错
	term.echo('<pre>\r@TelnetClient.disconnect@\r \r</pre>', {
        raw: true
    })
    pwdTryCount = 0;
    term.set_prompt('Telnet>');
    discernView('error');
}

function closeConnect() {
	if(!ip) {
		return
	}
	pwdTryCount = 0;
	term.pause();
	$.ajax({
        url: '/system/telnet/close.tv',
        cache: false, 
        timeout: 10000,
        method:'post',
        data: {
        	entityIp: ip
        },
        success: function() {
        	term.resume();
        	term.clear();
        	term.echo('<pre>\r@TelnetClient.disconnect@\r \r</pre>', {
                raw: true
            })
            term.set_prompt('Telnet>');
            discernView('error');
        },
        error: function(){
        	term.resume();
        }
    });
}

function sendUsername(username) {
	term.pause();
	$.ajax({
        url: '/system/telnet/sendUsername.tv',
        cache: false, 
        timeout: 10000,
        method:'post',
        data: {
        	entityIp: ip,
            command : username
        },
        success: function(response) {
        	term.resume();
        	analyzeEcho(response);
        	pwdTryCount = 0;
        },
        error: function(){
        	term.resume();
        }
    });
}

var pwdTryCount = 0;
function sendPassword(password){
	term.pause();
	lastCommand = "";
	
    $.ajax({
        url: '/system/telnet/sendPassword.tv',
        cache: false, 
        timeout: 10000,
        method:'post',
        data: {
        	entityIp: ip,
            command : password
        },
        success: function(response) {
        	term.resume();
        	analyzeEcho(response);
        	if(pwdTryCount >= 2) {
        		term.echo('<pre> \r@TelnetClient.disconnect@ \r \r </pre>', {
                    raw: true
                })
                term.set_prompt('Telnet>');
        	    discernView('Telnet>');
        	}
        	if(status === 'password' || status === 'enable password' || status === 'login password') {
        		++pwdTryCount;
        	}
        },
        error: function(){
        	term.resume();
        }
    });
}

function sendCommand(command) {
	var commandToBeSend = command;
	pwdTryCount = 0;
	if(lastQuestionStr) {
		if(commandToBeSend.startWith(lastQuestionStr)) {
			//如果没有改动之前的输入
			commandToBeSend = commandToBeSend.substr(lastQuestionStr.length)
		} else {
			// 没办法匹配，只能尝试增加相等数量的\b
			for(var i=0, len=lastQuestionStr.length; i<=len; i++) {
				commandToBeSend = '\b' + commandToBeSend;
			}
		}
	}
	
	term.pause();
	$.ajax({
        url: '/system/telnet/sendCommand.tv',
        cache: false, 
        timeout: 10000,
        method:'post',
        data: {
        	entityIp: ip,
            command : commandToBeSend
        },
        success: function(response) {
        	term.resume();
        	
        	if(command.lastIndexOf('?') === command.length - 1 || command.lastIndexOf('\t') === command.length - 1) {
        		lastQuestionStr = $.trim(term.get_command().substr(0, term.get_command().length));
        	} else {
        		lastQuestionStr = '';
        	}

        	analyzeEcho(response);
        },
        error: function() {
        	term.resume();
        	echoDisconnect();
        }
    });
}

var viewLevel = 0;
/**
 * 根据prompt来判断当前所在视图
 * @param prompt
 */
function discernView(prompt) {
	prompt = $.trim(prompt).toLowerCase();
	if(prompt.indexOf('username') !== -1) {
		status = 'username';
		viewLevel = -1;
		term.set_mask(false);
	} else if(prompt.indexOf('enable password') !== -1) {
		status = 'enable password';
		viewLevel = -1;
		term.set_mask(true);
	}else if(prompt.indexOf('login password') !== -1) {
		status = 'login password';
		viewLevel = -1;
		term.set_mask(true);
	} else if(prompt.indexOf('password') !== -1) {
		status = 'password';
		viewLevel = -1;
		term.set_mask(true);
	} else if(/^telnet\>$/.test(prompt)) {
		//Telnet>
		status = 'error';
		//TODO 显示断开连接
		ip = null;
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\(config-rsa-public-key\)\#$/.test(prompt)) {
		//config-rsa-public-key视图
		status = 'config-rsa-public-key';
		viewLevel = 3;
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\(config-rsa-key-code\)\#$/.test(prompt)) {
		//config-rsa-public-key视图
		status = 'config-rsa-key-code';
		viewLevel = 4;
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\(config\)\#$/.test(prompt)) {
		// configure terminal视图
		status = 'configure terminal';
		viewLevel = 2;
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\([\w\.\-\/\:]+\)\#$/.test(prompt)) {
		// 其他正确视图
		status = 'otherView';
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\#$/.test(prompt)) {
		// enable视图
		status = 'enable';
		viewLevel = 1;
		term.set_mask(false);
	} else if(/^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:]{1,63}\>$/.test(prompt)) {
		// view视图
		status = 'view';
		viewLevel = 0;
		term.set_mask(false);
	} else {
		status = 'error';
		//TODO 显示断开连接
		ip = null;
		term.set_mask(false);
	}
}

/**
 * 一键进入enable视图
 */
function enterEnable() {
	if(!status || status === 'username' || status.toLowerCase().indexOf('password') !== -1 || status === 'error') {
		return window.top.showMessageDlg('@COMMON.tip@', '@TelnetClient.plsTelnet@');
	}
	
	if(window.enableInterval || window.configInterval) {
		return;
	}
	
	var count = 0;
	window.enableInterval = setInterval(function() {
		if(term.paused()) {
			return;
		}
		if(++count >= 10) {
			//超过10次，直接退出
			clearInterval(window.enableInterval);
			window.enableInterval = undefined;
		}
		if(status === 'config-rsa-key-code') {
			// 特殊退出语句
			term.exec('public-key-code end');
		} else if(status === 'config-rsa-public-key') {
			// 特殊退出语句
			term.exec('peer-public-key end');
		} else if(status === 'view') {
			term.exec('enable');
		} else if(status === 'enable password') {
			term.exec(loginInfo.enablePassword);
		} else if(status === 'login password') {
			term.exec(loginInfo.password);
		} else if(status === 'password') {
			term.exec(loginInfo.password);
		} else if (status === 'enable'){
			clearInterval(window.enableInterval);
			window.enableInterval = undefined;
		} else if (status !== 'error'){
			term.exec('end');
		}
	}, 500);
	
}

/**
 * 一键进入config视图
 */
function enterConfig() {
	if(!status || status === 'username' || status.toLowerCase().indexOf('password') !== -1 || status === 'error') {
		return window.top.showMessageDlg('@COMMON.tip@', '@TelnetClient.plsTelnet@');
	}
	
	if(window.enableInterval || window.configInterval) {
		return;
	}
	
	var count = 0;
	window.configInterval = setInterval(function() {
		if(term.paused()) {
			return;
		}
		if(++count >= 10) {
			//超过10次，直接退出
			clearInterval(window.configInterval);
			window.configInterval = undefined;
		}
		if(status === 'config-rsa-key-code') {
			term.exec('public-key-code end');
		} else if(status === 'config-rsa-public-key') {
			term.exec('peer-public-key end');
		} else if(status === 'view') {
			term.exec('enable');
		} else if(status === 'enable password') {
			term.exec(loginInfo.enablePassword);
		} else if(status === 'login password') {
			term.exec(loginInfo.password);
		} else if(status === 'password') {
			term.exec(loginInfo.password);
		} else if(status === 'enable') {
			term.exec('configure terminal');
		} else if(status === 'configure terminal') {
			clearInterval(window.configInterval);
			window.configInterval = undefined;
		} else if (status !== 'error'){
			term.exec('end');
		}
	}, 500);
}

function analyzeEcho(response) {
	var echo = '';
	var lastLine = response;
	var prompt = 'Telnet>';
	var toBeInsert = '';
	

	// 找到最后一行
    var lastBreakLineIndex = response.lastIndexOf('\r');
    if(lastBreakLineIndex !== -1 && lastBreakLineIndex !== response.length - 1) {
    	echo = response.substring(0, lastBreakLineIndex);
    	if(lastQuestionStr && echo.indexOf('\r')!== -1) {
    		echo = echo.substring(echo.indexOf('\r'));
    	}
    	lastLine = response.substring(lastBreakLineIndex+1);
    }
    
    // 根据最后一行的信息，来获取prompt
    if(lastLine.indexOf('Username') !== -1) {
    	prompt = 'Username:';
    } else if(lastLine.indexOf('username') !== -1) {
    	prompt = 'username:';
    } else if(lastLine.indexOf('Enable Password') !== -1) {
    	prompt = 'Enable Password:'
    } else if(lastLine.indexOf('Login Password') !== -1) {
    	//Login Password
    	prompt = 'Login Password:';
    } else if(lastLine.indexOf('Password') !== -1) {
    	prompt = 'Password:'
    } else if(lastLine.indexOf('>') !== -1 || lastLine.indexOf('#') !== -1) {
    	var index = lastLine.indexOf('>') !== -1 ? lastLine.indexOf('>') : lastLine.indexOf('#');
    	prompt = lastLine.substr(0, index + 1);
    	toBeInsert = lastLine.substr(index + 1)
    } else {
    	echo = lastLine;
    }
    
    
    // 回显输出
    if(echo) {
    	term.echo("<pre>" + echo + "</pre>", {
    		raw: true
    	});
    }
    if(toBeInsert) {
    	term.set_command(toBeInsert);
    }
    term.set_prompt(prompt);
    discernView(prompt);
}

function clearTerm() {
	term.exec('clear');
}

function delayExcute(fn) {
	setTimeout(function() {
		fn();
	}, 500);
}