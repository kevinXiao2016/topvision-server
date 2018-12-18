var previousView = new Array(),
    nextView = new Array(),
    curView = {id : '', title : '', icon : '', url : ''};

function previousViewClick() {
	var flag = true;
	while(previousView.length > 0) {
		var v = previousView.pop();
		if (v.id != curView.id) {
			nextView.push({id: curView.id, icon: curView.icon, url: curView.url, title: curView.title});
			addView(v.id, v.title, v.icon, v.url, true);
			flag = false;
			break;
		}
	}
	if (flag && isTabbed) {
		contentPanel.setActiveTab('mydesktop');
	}
}
function nextViewClick() {
	while(nextView.length > 0) {
		var v = nextView.pop();
		if (v.id != curView.id) {
			previousView.push({id: curView.id, icon: curView.icon, url: curView.url, title: curView.title});
			addView(v.id, v.title, v.icon, v.url, true);
			break;
		}
	}
}

/**
 * 延迟的目的是为了防止跳转后导致的菜单阴影
 */
function addView(id, title, icon, url, history,forceRefresh){
	setTimeout(function(){
		_addView(id, title, icon, url, history,forceRefresh)
	},100);
}
//判断是否已经开了这个标签页;
function hasTabFn(id){
	var tab = contentPanel.getItem(id);
	if(tab == null){
		return false;
	}
	return true;
}
/**
 * 添加一个tab页（视图）
 * @param id
 * @param title
 * @param icon
 * @param url
 * @param history
 * @param forceRefresh 是否强制刷新
 */
function _addView(id, title, icon, url, history,forceRefresh) {
	if (isTabbed) {
		var tab = contentPanel.getItem(id);
		if (tab != null) {//target page is  opened
			if (tab == contentPanel.getActiveTab()) {// not normal
				var frame = getFrame(id);
				try {
					if (frame.location.href.endWith(url)) {
						if (frame.doRefresh) {
							frame.doRefresh();
						}
					} else {
						getFrameById(id).src = url;
					}
				} catch (err) {}
			} else {//target is opened but is not active
				contentPanel.setActiveTab(id);
				if(forceRefresh){
					/*if( newGetFramesById(id) && typeof(newGetFramesById(id).doRefresh) == 'function' ){//这样修改是因为有的页面forceFit设置成了true,但是页面中却没有这个函数;
						newGetFramesById(id).src = url;
						newGetFramesById(id).doRefresh(url);
					}*/
					var frame = newGetFramesById(id);
					
					if(frame){
						//leexiang 2015.05.13 在chrome和firefox中，frame有时候得到的是WINDOW有时候得到是IFRAME,如果是IFRAME就去获取一下他的window对象;
						if(frame.nodeName == "IFRAME" && frame.contentWindow){ 
							frame = frame.contentWindow;
						}
						
						frame.location.href = url;
						if(typeof frame.doRefresh === 'function'){
							frame.doRefresh(url);
						}
						contentPanel.on("activate",viewActivated);
						contentPanel.on("deactivate",viewDeactivated);
					}
					/*try{
						getFrameById(id).src = url;
						getFrameById(id).doRefresh();
					}catch(e){
						getFrame(id).src = url;
						getFrame(id).doRefresh();
					}*/
				}
			}
		} else {//target page is not opened
			if (tabMaxLimit) {
				var count = contentPanel.items.length + 1;
				if (count > MAX_TAB_NUM) {
					contentPanel.remove(1);
				}
			}
			var html = String.format('<iframe id="frame{0}" name="frame{1}" width=100% height=100% frameborder=0 src="{2}"></iframe>', id, id, url);
			var t = contentPanel.add({id: id, title: title, iconCls: icon, closable: true,
				tabTip: title, html: html,forceRefresh: forceRefresh, //在这里增加一个属性forceRefresh，便于做切换刷新;
				listeners: {
        			'activate': viewActivated,
        			'deactivate': viewDeactivated
    			}
			});
			if (switchWhenNewTab) {
				t.show();
			}
		}
	} else {
		var frame = getContentFrameById();
		if (frame != null) {
			contentPanel.setIconClass(icon);
			contentPanel.setTitle(title);
			frame.src = url;
		}
	}
	curView.id = id;
	curView.title = title;
	curView.icon = icon;
	curView.url = url;
	if (!history) {
		previousView.push({id : id, title : title, icon : icon, url : url});
	}
}

function removeTab(id) {
	if (isTabbed) {
		contentPanel.remove(id);
	}
}
function setActiveTab(id) {
	if (isTabbed) {
		contentPanel.setActiveTab(id);
	}
}
function setActiveTitle(title) {
	if (isTabbed) {
		var tab = contentPanel.getActiveTab();
		if (tab) {
			tab.setTitle(title + '&nbsp;');
		}
	} else {
		contentPanel.setTitle(title);
	}
}
function getActiveTitle() {
	if (isTabbed) {
		return contentPanel.getActiveTab().getTitle();
	} else {
		return contentPanel.getTitle();
	}
}

function viewActivated(tab) {
	try {
		var frame = getFrame(tab.id);
		if (frame.tabActivate) {
			frame.tabActivate();
		}
	} catch (err) {
		//alert(err)
	}
}
function viewDeactivated(tab) {
	try {
		var frame = getFrame(tab.id);
		if (frame.tabDeactivate) {
			frame.tabDeactivate();
		}
	} catch (err) {}
}

function getContentFrameById() {
	if (contentFrameById == null) {
		contentFrameById = Zeta$("contentFrame");
	}
	return contentFrameById;
}
function getContentFrameByName() {
	if (contentFrameByName == null) {
		contentFrameByName = ZetaUtils.getIframe("contentFrame");
	}
	return contentFrameByName;
}
function getActiveFrameById() {
	if (isTabbed) {
		var tab = contentPanel.getActiveTab();
		if (tab) {
			return document.getElementById("frame" + tab.id);
		}
	} else {
		return getContentFrameById();
	}
}
function getFrameById(frameId) {
	if (isTabbed) {
		try {
			return document.getElementById("frame" + frameId);
		} catch (err) {
			return null;
		}
	} else {
		return getContentFrameById();
	}
}
function newGetFramesById(frameId){
	if(isTabbed){
		try {
			return window.frames["frame" + frameId]
		}catch(err){
			return null;
		}
	}else{
		return getContentFrameById();
	}
}

/**
 * 得到当前激活的tab页
 * @returns
 */
function getActiveFrame() {
	if (isTabbed) {
		var tab = contentPanel.getActiveTab();
		if (tab) {
			return ZetaUtils.getIframe("frame" + tab.id);
		}
	} else {
		return getContentFrameByName();
	}
}

/**
 * 得到当前激活的tab页的ID
 * @returns
 */
function getActiveFrameId() {
	if (isTabbed) {
		var tab = contentPanel.getActiveTab();
		if (tab) {
			return tab.id;
		}
	} else {
		return "contentFrame";
	}
}

function getFrame(frameId) {
	if (isTabbed) {
		try {
			return ZetaUtils.getIframe("frame" + frameId);
		} catch (err) {
			return null;
		}
	} else {
		return getContentFrameByName();
	}
}

var menuFrameByName = null;
var naviFrameByName = null;
var propertyFrameByName = null;
function getMenuFrame() {
	if (menuFrameByName == null) {
		menuFrameByName = ZetaUtils.getIframe("menuFrame");
	}
	return menuFrameByName;
}
function getNavigationFrame() {
	if (naviFrameByName == null) {
		naviFrameByName = ZetaUtils.getIframe("menuFrame");
	}
	return naviFrameByName;
}
function getPropertyFrame() {
	if (propertyFrameByName == null) {
		propertyFrameByName = ZetaUtils.getIframe("propertyFrame");
	}
	return propertyFrameByName;
}

function onMaxViewClick() {	enableMaxView(); }

function closeTabClick() {
	if (isTabbed) {
		var item = contentPanel.getActiveTab();
		if(item.closable){
			contentPanel.remove(item);
		}
	}
}
function closeOtherTabClick() {
	if (isTabbed) {
		if (closeManyTabInformed && contentPanel.items.length > 2) {
			Ext.MessageBox.confirm(confirmText, closeOtherMsg, function(type) {
				if (type == 'yes') {
					var ctxItem = contentPanel.getActiveTab();
					contentPanel.items.each(function(item){
						if(item.closable && item != ctxItem){
							contentPanel.remove(item);
						}
					});
				}
			});
		} else {
			var ctxItem = contentPanel.getActiveTab();
			contentPanel.items.each(function(item){
				if(item.closable && item != ctxItem){
					contentPanel.remove(item);
				}
			});
		}
	}
}
function closeAllTabClick() {
	if (isTabbed) {
		if (closeManyTabInformed && contentPanel.items.length > 1) {
			Ext.MessageBox.confirm(Extux.TabCloseMenu.confirmText, Extux.TabCloseMenu.closeAllMsg, function(type) {
				if (type == 'yes') {
					contentPanel.items.each(function(item){
						if(item.closable){
							contentPanel.remove(item);
						}
					});
				}
			});
		} else {
			contentPanel.items.each(function(item){
				if(item.closable){
					contentPanel.remove(item);
				}
			});
		}
	}
}