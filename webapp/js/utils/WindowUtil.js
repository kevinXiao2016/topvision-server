/**
* # 描述
* 这是NM3000封装的弹窗工具类，在EXTJS的基础上封装完成
*
* @class WindowUtil
*/

/**
* 展示等待窗口
* @param {String} title 窗口标题
* @param {String} text 等待窗口文本
* @param {String} icon 等待窗口的图标css class
* @param {Integer} duration 每一圈持续时长
* @param {Boolean} cancelable 等待窗口能否退出
*/
function showWaitingDlg(title, text, icon, duration, cancelable) {
	text = text || title;
	icon = "waitingMsg" || icon ;
	var $cancel = typeof cancelable == 'undefined' ? true : cancelable;
    Ext.MessageBox.show({
    	title: I18N.COMMON.waiting,
    	icon: icon,
    	progressText: (text ? text : ''),
    	msg: text, 
    	width: 320, 
    	wait: true,
        waitConfig: {
        	interval: 300
        },
        buttons: $cancel ? {"cancel": Ext.MessageBox.CANCEL } : false
    });
}

/**
* 关闭等待窗口
*/
function closeWaitingDlg() { 
	Ext.MessageBox.hide();
}

/**
* 展示消息窗口
* @param {String} title 窗口标题
* @param {String} msg 窗口文本
* @param {String} type 窗口的类型，error: 错误信息/question: 疑问信息/ 其他都是提示信息
* @param {Function} fn 消息窗口打开后执行函数
*/
function showMessageDlg(title, msg, type, fn) {
	var icon = (type == "error" ? Ext.MessageBox.ERROR : (type == "question" ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
	Ext.MessageBox.show({
		title: title, 
		msg: msg, 
		buttons: Ext.MessageBox.OK, 
		icon: icon,
		fn: fn
	});
}

/**
* 关闭消息窗口
*/
function closeMessageDlg() { 
	Ext.MessageBox.hide(); 
}

/**
* 展示默认的错误窗口，错误信息为统一的"操作失败"
*/
function showErrorDlg() {
	Ext.MessageBox.show({
		title:I18N.COMMON.error, 
		msg:I18N.COMMON.operationFailure, 
		buttons:Ext.MessageBox.OK, 
		icon:Ext.MessageBox.ERROR
	});
}

/**
* 展示confirm窗口
* @param {String} title 窗口标题
* @param {String} msg 窗口内容
* @param {Function} callback 选中确定后回调
*/
function showConfirmDlg(title, msg, callback) {	
	Ext.MessageBox.confirm(title, msg, callback); 
}

/**
* 展示确定取消的confirm窗口
* @param {String} title 窗口标题
* @param {String} msg 窗口内容
* @param {Function} callback 选中确定后回调
*/
function showOkCancelConfirmDlg(title, msg, callback) {
	Ext.MessageBox.show({
		title: title, 
		msg: msg, 
		buttons: Ext.MessageBox.OKCANCEL, 
		icon: Ext.MessageBox.QUESTION, 
		fn: callback
	});
}

/**
* 展示有input输入框的窗口
* @param {String} title 窗口标题
* @param {String} msg 窗口内容
* @param {Function} callback 选中确定后回调
* @param {String} text 输入框默认文本
* @param {Object} scope 窗口作用域
* @param {Boolean} multiline 输入框是否支持多行
*/
function showInputDlg(title, msg, callback, text, scope, multiline) {
	Ext.MessageBox.prompt(title, msg, callback, scope, multiline, text);
}

/**
* 展示有textera输入框的窗口
* @param {String} title 窗口标题
* @param {String} msg 窗口内容
* @param {Function} callback 选中确定后回调
*/
function showTextAreaDlg(title, msg, callback) {
	Ext.MessageBox.show({
		title:title, 
		msg: msg, 
		width: 600, 
		height: 370,
		buttons: Ext.MessageBox.OKCANCEL, 
		multiline: true, 
		fn: callback
	});
}

/**
* 展示窗口
* @param {Object} config 窗口配置
*/
function showWindow(config) {
	new Ext.Window(config).show();
}

/**
* 创建含有指定页面的窗口
* @param {String} id 窗口id
* @param {String} title 窗口标题
* @param {Integer} width 宽度
* @param {Integer} height 高度
* @param {String} url 窗口iframe的页面url
* @param {String} icon 图标的css class
* @param {Boolean} modal 是否是模态
* @param {Boolean} resized 不知意义，也没使用
* @param {Function} closeHandler 窗口关闭后的回调函数
* @return {Ext.Window} 含有指定页面窗口的window对象
*/
function createWindow(id, title, width, height, url, icon, modal, resized, closeHandler) {
	var win = new Ext.Window({
		id: id, 
		title: title, 
		minimizable: false, 
		maximizable: true,
		closable: true, 
		shadow: ZetaGUI.windowShadow,
		width: width, 
		height: height, 
		modal: modal, 
		plain: true,
		html:"<iframe width=100% height=100% frameborder=0 src=\"" + url + "\"></iframe>"
	});
	if (icon) {
		win.setIconClass(icon);
	}
	if (closeHandler) {
		win.on("close", closeHandler);
	}
	win.show();
	return win;
}

/**
 * 使用当前页面的element元素做弹出窗口的内容，而不是一个新的HTML页面
 * @param {String} id 窗口id
 * @param {String} title 窗口标题
 * @param {Integer} width 宽度
 * @param {Integer} height 高度
 * @param {String/DOM} content 将放置在创建窗口内的HTML片段或DOM
 * @param {String} icon 图标的css class
 * @param {Boolean} modal 是否是模态
 * @param {Boolean} resized 不知意义，也没使用
 * @param {Function} closeHandler 窗口关闭后的回调函数
 * @param {Boolean} scroll 是否自动滚动
 * @return {Ext.Window} 含有指定窗口的window对象
 */
function createMessageWindow(id, title, width, height, content, icon, modal, resized, closeHandler, scroll) {
	var $config = {
			id: id, 
			title: title, 
			minimizable: false, 
			maximizable: true,
			closable: true, 
			shadow: ZetaGUI.windowShadow, 
			autoScroll: scroll, 
			resizable :false,
			width: width, 
			height: height, 
			modal: modal, 
			plain: true
		};
	if(typeof content == "string"){
		$config.html=content;
	}else{
		$config.contentEl = content;
	}
	var win = new Ext.Window($config);
	if (icon) {
		win.setIconClass(icon);
	}
	if (closeHandler) {
		win.on("close", closeHandler);
	}
	win.show();
	return win;
}

/**
 * 展示模态对话框
 * @param {String} title 窗口标题
 * @param {Integer} width 宽度
 * @param {Integer} height 高度
 * @param {String} url 窗口iframe的页面url
 * @param {Function} closeHandler 窗口关闭后的回调函数
 */
function showModalDlg(title, width, height, url, closeHandler) {
	//为url加上随机串
	if(url.indexOf("?") == -1){
		url = url + "?_topvision="+Math.random();
	}else{
		url = url + "&_topvision="+Math.random();
	}
	if (singleModalDlg == null) {
		singleModalDlg = new Ext.Window({
			id: 'modalDlg', 
			title: title, 
			width: width, 
			height: height,
			modal: true, 
			plain: true, 
			closable: true, 
			closeAction: 'hide',
			resizable: false, 
			stateful: false, 
			shadow: ZetaGUI.windowShadow,
			html: '<iframe id="modalFrame" name="modalFrame" width=100% height=100% frameborder=0 src="' + url + '"></iframe>'
		});
		singleModalDlg.on("hide", function(cmp) {
			if (singleModelFrame == null) {
				singleModelFrame = ZetaUtils.getIframeById('modalFrame');
			}
			singleModelFrame.src = 'include/blank.html';
		});
		singleModalDlg.on("beforehide", function(cmp) {
			if (singleModalCallback != null) {
				try {
					singleModalCallback();
				} catch (err) {
				}
			}
			return true;
		});
	} else {
		if (singleModelFrame == null) {
			singleModelFrame = ZetaUtils.getIframeById('modalFrame');
		}
		singleModelFrame.src = url;
		singleModalDlg.setTitle(title);
		singleModalDlg.setWidth(width);
		singleModalDlg.setHeight(height);
		singleModalDlg.center();
	}
	singleModalCallback = null;
	if (closeHandler) {
		singleModalCallback = closeHandler;
	}
	singleModalDlg.show();
}

/**
 * 隐藏指定模块窗口
 * @param {Boolean} target 为true时调用hide()，为false时调用setVisible()
 */
function hideModalDlg(target) {
	if (singleModalDlg != null) {
		if (target) {
			singleModalDlg.hide();
		} else {
			singleModalDlg.setVisible(false);
		}
	}
}

/**
 * 关闭指定模块窗口
 * @TODO 为何关闭实现和隐藏实现一样，需要关注
 * @param {Boolean} target 为true时调用hide()，为false时调用setVisible()
 */
function closeModalDlg(target) {
	if (singleModalDlg != null) {
		//singleModalDlg.close();
		if (target) {
			singleModalDlg.hide();
		} else {
			singleModalDlg.setVisible(false);
		}
	}
}

/**
 * 关闭指定窗口
 * @param {String} id 窗口id
 */
function closeMessageWindow(id){
	Ext.getCmp(id).close();
}


/**
 * 创建一个WINDOW窗口
* @param {String} id 窗口id
* @param {String} title 窗口标题
* @param {Integer} width 宽度
* @param {Integer} height 高度
* @param {String} url 窗口iframe的页面url
* @param {String} icon 图标的css class
* @param {Boolean} modal 是否是模态
* @param {Boolean} closable 能否关闭
* @param {Function} closeHandler 窗口关闭后的回调函数
 */
function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
	if (id == 'modalDlg') {
		showModalDlg(title, width, height, url, closeHandler)
	} else {
		//我想要一种800*600宽高的(mSize)，但是我又担心用户浏览器高度没有600，通过判断窗口高度如果不够，则使用500。		
		if(width == "mSize"){
			width = 800;
			if($(window).height() >= 600){				
				height = 600;
			}else{
				height = 500;				
			}
		}
		var win = new Ext.Window({
			id: id, 
			title: title, 
			width: width, 
			height: height,
			border: false,
			bodyBorder: false,
			modal: (modal == undefined ? true : modal), 
			closable: (closable == null ? true : closable),
			plain:true, 
			resizable: false, 
			stateful: false, 
			shadow: ZetaGUI.windowShadow,
			html: '<iframe width=100% height=100% id='+("frame"+id)+" name="+("frame"+id)+' frameborder=0 src="' + url + '"></iframe>'
		});
		if (typeof closeHandler != 'undefined') {
				win.on("close", closeHandler);
		}
		win.show();
		return win;
	}
}

/**
 * 关闭指定窗口
 * @param {String} id 窗口id
 */
function closeWindow(id) {
	if (id == 'modalDlg') {
		closeModalDlg();
	} else {
		var w = Ext.WindowMgr.get(id);
		if (w != null) {
			w.close();
		}
	}
}

/**
 * 设置指定窗口是否可见
 * @param {String} id 窗口id
 * @param {Boolean} visible 显示/隐藏
 */
function setWindowVisible(id, visible) {
	var w = Ext.WindowMgr.get(id);
	if (w != null) {
		w.setVisible(visible);
	}
}

/**
 * 设置指定窗口的标题
 * @param {String} id 窗口id
 * @param {String} title 新的标题
 */
function setWindowTitle(id, title) {
	var w = Ext.WindowMgr.get(id);
	if (w != null) {
		w.setTitle(title);
	}
}

/**
 * 获取指定窗口
 * @param {String} id 窗口id
 */
function getWindow(id) {
	return Ext.WindowMgr.get(id);
}

/**
 * 展示右下角的提示窗口
 * @add by fanzidong，这个方法名字不对，不应该以使用场景命名，而应该以方法具体做的事情命名，如：showMessenger
 * @param {Object} o messenger相关属性
 */
function afterSaveOrDelete(o){
	try{
		closeWaitingDlg();
	}catch(err){}
	
	var showtime = tipShowTime ? (tipShowTime * 100) : 1000;
	var nTime = o.showTime ? o.showTime : showtime;
	var autoHide = o.autoHide ? o.autoHide : true;
	var okBtnTxt = o.okBtnTxt ? o.okBtnTxt : '@resources/COMMON.ok@';
	if($("#nm3kSaveOrDelete").length == 0){
		nm3kObj.nm3kSaveOrDelete = new Nm3kMsg({
			title: o.title,
			html: o.html,
			okBtnTxt: okBtnTxt,
			okBtn : true,
			timeLoading: true,
			unique: true,
			showTime : nTime,
			autoHide : o.autoHide,
			id: "nm3kSaveOrDelete"		
		});
		nm3kObj.nm3kSaveOrDelete.init();
	}else{
		nm3kObj.nm3kSaveOrDelete.update({
			html: o.html,
			title: o.title
		})
	}	
};

function nm3kMessage(o){
	var msg = new Nm3kMsg(o);
	msg.init();
}

/**
 * 展示右下角的提示窗口
 * @add by fanzidong，这个方法不应该放在这里，这个方法初衷是在某些页面提示可以右键操作，在对应页面调用上面方法即可
 * @param {Object} o messenger相关属性
 */
function nm3kRightClickTips(o){
	try{
		closeWaitingDlg();
	}catch(err){}
	
	var showtime = tipShowTime ? (tipShowTime * 100) : 1000;
	var nTime = o.showTime ? o.showTime : showtime;
	if($("#nm3kRightClickTips").length == 0){
		nm3kObj.nm3kRightClickTips = new Nm3kMsg({
			title: o.title,
			html: o.html,
			okBtnTxt: "@resources/COMMON.ok@",
			okBtn : true,
			timeLoading: true,
			unique: true,
			showTime : nTime,
			id: "nm3kRightClickTips"		
		});
		nm3kObj.nm3kRightClickTips.init();
	}else{
		nm3kObj.nm3kRightClickTips.update({
			html: o.html,
			title: o.title
		})
	}	
};

/**
 * 展示右下角的验证提示窗口
 * @add by fanzidong，这个方法目前没有使用，看是否能提供更简洁的封装
 * @param {Object} o messenger相关属性
 */
function nm3kValidateFn(o){
	try{
		closeWaitingDlg();
	}catch(err){}
	
	var showtime = tipShowTime ? (tipShowTime * 100) : 1000;
	var nTime = o.showTime ? o.showTime : showtime;
	if($("#nm3kValidate").length == 0){
		nm3kObj.nm3kValidateTips = new Nm3kMsg({
			title: o.title,
			html: o.html,
			timeLoading: true,
			unique: true,
			showTime : nTime,
			id: "nm3kValidate"
		})
		nm3kObj.nm3kValidateTips.init();
	}else{
		nm3kObj.nm3kValidateTips.update({
			html: o.html
		})
	};
};