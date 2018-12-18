/* ==========================================================
 * START:  这段内容是主页自己的功能函数区域
 * ========================================================== */

/**
* 切换顶层模块，并保存当前选中模块
* @param {String} moduleName 模块名称
* @param {String} action 模块对应菜单的url
*/
function showNaviBar(moduleName, action) {
    $.ajax({
		url: '/userPreference/saveUserPreference.tv',
		cache: false,
		data: {
			key : "core.lastSelectedNaviBar", 
			value : moduleName
		},
		success: function() {
			Zeta$('menuFrame').src = action + "?r="+Math.random();
		}
	});
}

/**
* 显示全局管理视图
*/
function showGmView() {
	addView("gmview", I18N.COMMON.gmView, "icoH1", "system/gmView.jsp");
}

/**
* 将当前页面设置为默认起始页
*/
function setHomeClick() {
	var home = null;
	var frame = getActiveFrame();
	var src = frame.location.href;
	if (isTabbed) {
		var tab = contentPanel.getActiveTab();
		home = {id: tab.id, title: tab.title, icon: tab.iconCls, url: src};
	} else {
		home = curView;
	}
	var msg = String.format(I18N.COMMON.setHomeAnswer, home.title,"");
	showConfirmDlg(I18N.COMMON.tip, msg, function(type) {
		if (type == 'yes') {
			setCookieValue(userName + '.home', home);
		}
	});
}

/**
* 展示首页的加载遮罩
*/
function setLoadingProgress(progress) {
	if (loadingBarEl == null) {
	    loadingBarEl = DOC.getElementById('loading-indicator');
	}
	loadingBarEl.style.width = parseInt(360 * progress / 100);
	if (progress >= 100) {
	    Ext.get('loading-mask').fadeOut({remove: true});
	}
}

/**
* 展示修改密码页面
*/
function setPasswdClick() {
	window.top.createDialog('modalDlg', I18N.MAIN.setPasswd, 600, 370, 'system/popModifyPasswd.tv', null, true, true);
}

/**
* 展示修改个人资料页面
*/
function modifyPersonalInfoClick() {
	createDialog('modalDlg', I18N.MAIN.personalInfo, 800, 500, 'system/showPersonalInfo.tv', null, true, true);
}

/**
* 展示系统运行信息页面
*/
function onShowRuntimeClick() {
	addView("onshowRuntime", I18N.MAIN.systemRuntime, "apListIcon", "system/showRuntimeInfo.tv");
}

/**
* 展示关于页面
*/
function onAboutClick() {
	createDialog("modalDlg", '@about.title@', 600, 370, "system/showAbout.tv", null, true, true);
}

/**
* 展示license页面
*/
function onLicenseClick() {	
	window.parent.addView("licensePage", I18N.COMMON.license, "icoI10", "system/showLicense.tv");
}

/**
* 展示帮助页面
*/
function onHelpClick() {
	window.open("help/tutorial.html", "help");
}

/**
* 首页注销登录
*/
function onLogoffClick() {
	showConfirmDlg(I18N.COMMON.confirm, I18N.COMMON.exitConfirm, function(type) {
		if (type == 'yes') {
			location.href = "logoff.tv";
		}
	});
}

/**
* 首页注销登录，在用户管理页删除当前用户后触发
*/
function onLogoffClickNotConfirm() {
	location.href = "/logoff.tv";
}

/**
* 展示我的桌面页面
*/
function showMyDesktop() {	
	addView("mydesktop", I18N.COMMON.myDesktop, "mydesktopIcon", "portal/showMydesktop.tv");
}

/**
* 展示与我们联系页面
*/
function onContactClick() {
	createDialog('modalDlg', I18N.COMMON.contactUs, 500, 300, 'system/popContactUs.jsp', null, true, true);
}

/**
* 定制我的桌面
*/
function customDesktopClick() { 
	createDialog('modalDlg', I18N.MAIN.customMyDesktop, 800, 500, 'portal/showPopPortletItems.tv', null, true, true);
}

/**
* 开启/关闭声音提示
* @param {Boolean} flag 开启/关闭声音提示
*/
function openSoundTip(flag) {
	musicFlag = !musicFlag;
	$.ajax({
		url : '/workbench/modifyUserSoundStatus.tv',
		cache:false,
		data: {
			userSoundStatus: musicFlag
		},
		success:function(){
			if (musicFlag) {
				playMusic( musicUrl );
			} else {
				stopIntervalPlayMusic();
				stopMusic();
			}
		},error:function(){
			//Mark by @bravin 对于 mainNoMenu的情况,由于菜单的显示与否由Ext控制，所以这里要进行rollback处理
		}
	})
}

/**
* 此处如此写是因为会导致只支持IE，而不支持其他浏览器，需要修改
*/
function initPlayer(url) {
	document.getElementById('music-div').innerHTML = '<embed id="chime" name="chime" hidden=true autostart=true loop="false" src="' + url + '">';
}

/**
* 播放音乐
* @param {String} url 音乐文件地址
*/
function playMusic(url) {
	if (url == '') {
		return;
	}
	initPlayer(url);
	player = document.getElementById('chime');
	//musicUrl = url;
	if (musicFlag) {
		//player.src = musicUrl;
		try {
			player.play();
		} catch (err) {
			var i = 0;
		}
	}
}

/**
* 停止播放音乐
*/
function stopMusic() {
	if (player == null) {
		return;
	}
	musicUrl = '';
	try {
		$("#chime").remove();
		//player.stop();
	} catch (err) {
	}
}

/**
* 保存导航动画选项
*/
function saveNavCartoon(){
	var cartoonFlag = !navCartoonFlag;
	$.ajax({
		url : '/workbench/modifyNavCartoonStatus.tv',
		data: {
			navCartoonStatus : cartoonFlag
		},
		cache:false,
		success:function(){
			navCartoonFlag = cartoonFlag;
		},error:function(){
			
		}
	});
}

/**
* 展示个性化页面
*/
function showPersonalize() {
	createDialog("modalDlg", I18N.COMMON.personalize, 872, 595, 'workbench/loadUserPreferences.tv', null, true, true);
}
function showSystemPreferences(title) {
	createDialog('modalDlg', title, 600, 400, 'param/showSystemPreferemces.tv', null, true, true);
}
function showNetworkPreferences() {
	createDialog('modalDlg', I18N.MenuItem.showNetworkPreferences, 600, 400, 'param/showSystemPreferemces.tv', null, true, true);
}
function showSecurityConfig(title, url) {
	createDialog("modalDlg", title, 600, 400, url, null, true, true);
}
function renderNote(value, p, record) {
	return String.format("<img src=\"images/fault/level{0}.gif\" title=\"{1}\" border=0 align=absmiddle>&nbsp;{2}",
		value, record.data.levelName, record.data.message);
}
function renderHost(value, p, record) {
	return (record.data.host == null || record.data.host == '') ? record.data.source : record.data.host;
}
function renderStatus(value, p, record) {
	return String.format("<img hspace=5 src=\"images/fault/level{0}.gif\" title=\"{1}\" border=0 align=absmiddle>{2}",
		record.data.level, record.data.levelName, value);
}
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
            cancelBtnCallBack:function(){
            	stopMusic();
            	stopIntervalPlayMusic();
            },
            closeBtnCallBack: function(){
            	stopMusic();
            	stopIntervalPlayMusic();
            },
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
    
function showMessanger() {
    var str = '<p style="padding-bottom:5px; margin-bottom:10px; color:#f00; border-bottom:1px solid #ccc;" class="wordBreak"><b style="cursor:pointer;" onclick="viewAllSecurity()" >'
        + nm3kAlertObj.msg +'</b></p><table  width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">'
        str += '<tr><td width="70" valign="top">@td.alertnotify.deviceAddress@</td><td class="wordBreak"><a class="yellowLink" href="javascript:;" onclick="showAlertDevice({0},{1},\'{2}\')">' + nm3kAlertObj.add +'</a></td></tr>';
	    if (nm3kAlertObj.mac.indexOf('ONU') == 0) {
	    	//加链接前判断该onu是否存在数据库
    		str += '<tr><td valign="top">@td.alertnotify.source@</td><td class="wordBreak"><a class="yellowLink" href="javascript:;" onclick="showOnuInfo({0},\'{3}\')">' + nm3kAlertObj.mac +'</a></td></tr>';
		}else {
			str += '<tr><td valign="top">@td.alertnotify.source@</td><td class="wordBreak">' + nm3kAlertObj.mac + '</td></tr>';
		}
        str += '<tr><td valign="top">@td.alertnotify.occuretime@</td><td class="wordBreak">'+ nm3kAlertObj.time +'</td></tr>';
        str += '</table>';
        str = String.format(str, nm3kAlertObj.entityId, nm3kAlertObj.parentId,nm3kAlertObj.add,nm3kAlertObj.mac.slice(nm3kAlertObj.mac.indexOf(':')+1));
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
 * 跳转到onu设备视图
 * @param onuId
 * @param onuName
 */
function showOnuInfo(onuId,onuName){
	$.ajax({
		url: '/onu/isOnuExist.tv',
		cache: false,
		dataType: 'json',
		data : {
			onuId: onuId
		},
		success: function(data){
			if (data.exist) {
				window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
			} else {
				window.parent.showMessageDlg('@COMMON.tip@', '@device.notExist@');
			}
		},
		error: function(e){
			window.parent.showMessageDlg('@COMMON.tip@', '@onuAjaxError@');
		}
	});
	
}


//每隔一段时间，去访问一下界面上是否有没有关闭的告警弹出框，即#nm3kMsgAlert，如果有，那么再次播放声音;
function intervalPlayMusic(){
	stopIntervalPlayMusic();
	var d = new Date();
	var now = d.getTime();
	var timeInterval = now - oAlertSoundInterval.lastTime;
	//console.log(timeInterval)
	if(timeInterval < 0){ //手动调整了客户端时间;
		oAlertSoundInterval.lastTime = now;
	}else if(timeInterval >= oAlertSoundInterval.soudInterval ){ //时间间隔达到了;
		oAlertSoundInterval.lastTime = now;
		if(musicFlag && $("#nm3kMsgAlert").length > 0){
			//播放一次声音;
			var music = 'sounds/' + oAlertSound[nm3kAlertObj.levelId];
    		playMusic(music);
		}
	}
	oAlertSoundInterval.timeOut = window.setTimeout(function(){
		intervalPlayMusic();
	}, oAlertSoundInterval.timeIntrerval);
};
function startIntervalPlayMusic(){
	oAlertSoundInterval.timeOut = window.setTimeout(function(){
		var d = new Date();
		oAlertSoundInterval.lastTime = d.getTime();
		intervalPlayMusic();    				
	}, oAlertSoundInterval.timeIntrerval);
}
function stopIntervalPlayMusic(){
	window.clearTimeout(oAlertSoundInterval.timeOut);
	oAlertSoundInterval.timeOut = null;
}

function clearDiskBillboard(noticeId) {
	$.ajax({
		url: '/billboard/clearNoticeById.tv',
		cache: false,
		dataType: 'json',
		data: {
			noticeId: noticeId
		},
		success: function(list){
			//initializeBillboard();
			//window.location.reload();
			for(var i = 0; i < billboardStore.length; i++){
				if (billboardStore[i].noticeId == noticeId) {
					billboardStore.splice(i,1);
				}
			}
			if (billboardStore.length === 0) {
				$("#topMsgTips").find(".topMsgTipsClose").click();
			}else {
				showBillboard();
			}
		},
		error: function(e){
			//console.log("load billboard error",e);
		}
	});
}

/**
* 添加新公告到公告牌
* @param {Object} notice
*/
function pasteToBillboard(notice){
	billboardStore.push( notice );
	showBillboard();
}

/**
* 显示出公告牌
*/
function showBillboard(){
	if(billboardStore.length > 0){
		var notice = billboardStore.last();
		tMsg = new TopMsgTips({ 
			user: notice.username,
			time: notice.createTimeString,
			content: notice.content,
			nowPage: billboardStore.length,
			allPage: billboardStore.length
		});
		tMsg.init();
	}else{
		afterSaveOrDelete({
			title:I18N.COMMON.tip,			
			html: I18N.COMMON.withoutBillBoard 
		});
	}
}

/**
* 当用户进入页面的时候初始化公告信息，显示最近一条公告
*/
function initializeBillboard(){
	$.ajax({
		url: '/billboard/loadAllValidNotice.tv',
		cache: false,
		dataType: 'json',
		success: function(list){
			billboardStore = list.data;
			if( billboardStore.length>0 ){
				showBillboard();
			}
		},
		error: function(e){
			//console.log("load billboard error",e);
		}
	});
}

/**
* 锁住屏幕
*/
function lockScreen(){
	$.ajax({
		url:"lockScreen.tv",
		cache:false,
		success:function(){
			ignoreTimeout = false;
			$("#lockScreenTip").empty();
			$("#lockPass").val("");
			$("#lockScreenDiv").fadeIn();
		}
	})
}

/**
* 解锁屏幕
*/
function openLockScreen(){
	var password = $('#lockPass').val().trim();
	if(password == ""){
		$('#lockPass').focus();
		$("#lockScreenTip").text("@COMMON.WrongPass@");
		return;
	}
	$.ajax({
		url : 'unlockScreen.tv',
		type : 'POST',
		data : {
			newPwd: password
		},
		success : function(json) {
			ignoreTimeout = true;
			$("#lockScreenDiv").fadeOut();
		},
		error : function(json) {
			$('#lockPass').focus();
			$("#lockScreenTip").text("@COMMON.WrongPass@");
		},
		cache : false
	});
}

/**
* 在锁屏时输入密码后通过敲击"enter"键触发事件
*/
function addEnterKey(e) {
	var event = window.event||e; // for firefox
	if (event.keyCode==KeyEvent.VK_ENTER) {
		openLockScreen();
	}
}

/**
* 展示切换根地域页面
*/
function switchRootFolder(){
	window.top.createDialog('modalDlg', I18N.MenuItem.switchRootFolder, 800, 500, '/system/showSwitchRootFolder.tv', null, true, true);
}

/**
* 获取主框架的src,便于放入收藏夹
* @return 主框架的url
*/
function getMainFrameUrl(){
	var itemId = "frame"+contentPanel.getActiveTab().id;
	var $frame = window.top.frames[itemId];
	var $url;
	if($frame.location){
		$url = $frame.location.href;
	}else if($frame.contentWindow){
		$url = $frame.contentWindow.location.href;
	}else{
		$url = $frame.src;
	}
	return $url
};

/**
* 获取主框架的标题名便于收藏
* @return 主框架的标题名
*/
function getMainFrameTitle(){
	var title = contentPanel.getActiveTab().title;
	return title;
}

/**
* 展示新建链接页面
*/
function getMainFrameToFavorite(){
	var $menu = $("#menuBtn_workbench");
	if($menu.length == 1){
		$menu.click();
	}
	top.createDialog("modalDlg", I18N.MODULE.newLink, 600, 370, "workbench/newFavouriteLinkJsp.tv", null, true, true);
}

/**
* 构建左侧菜单区域
*/
function bulidNavigatorPanel(){
    var leftMargin = "0 0 0 " + ZetaGUI.viewMargin.left;
    var leftMargin1 = "0 4 0 " + ZetaGUI.viewMargin.left;
    
    naviPanel = new Ext.Panel({
    	id: "navi-panel", 
    	title: I18N.SYSTEM.Workbench, 
    	region: "west",
        contentEl: "navi-div", 
        useSplitTips: false, 
        split: ZetaGUI.enableSplit,
        width: 200, 
        minWidth: 200,  
        maxWidth: 200,
        margins: 0, 
        cmargins: 0, 
        draggable: ZetaGUI.enableDraggable && false,
        border: ZetaGUI.enableBorders, 
        frame: ZetaGUI.roundedBorders,
        collapsible: ZetaGUI.enableCollapsible
    });
}

/**
* 构建中央内容区域
*/
function bulidCenterContentPanel (home){
    //区分用户是否选择了不显示为Tabpanel
    if (isTabbed) {
        closeManyTabInformed = alarmWhenCloseManyTab;
        var items = [];
        items[0] = {
        	id: home.id, 
        	title: home.title, 
        	closable: false, 
        	iconCls: home.icon,
            html: '<iframe name="frame' + home.id + '" id="frame' + home.id + '" width=100% height=100% frameborder=0 src="' + home.url + '"></iframe>',
            listeners: {
                'activate': viewActivated,
                'deactivate': viewDeactivated
            }
        };
        contentPanel = new Ext.TabPanel({
        	id: 'cpId', 
        	region : "center",
            resizeTabs: ZetaGUI.resizeTabs,
            minTabWidth: ZetaGUI.minTabWidth, 
            tabWidth: ZetaGUI.tabWidth,
            enableTabScroll: true, 
            plain: ZetaGUI.tabPlain, 
            draggable: ZetaGUI.enableDraggable,
            border: ZetaGUI.tabBorders, 
            frame: ZetaGUI.roundedBorders,
            defaults: {
            	autoScroll: true, 
            	padding: "0px"
            },
            plugins: [new Ext.ux.TabCloseMenu()],
            items: items,
            listeners: {
            	'tabChange': function() {
            		var id = contentPanel.getActiveTab().getId();
            		displayHelp(id);
            	}
            }
        });
        contentPanel.on('beforeremove', function(tp, tab) {
            var fid = "frame" + tab.id;
            try {
                var frame = ZetaUtils.getIframe(fid);
                if (frame.tabRemoved) {
                    frame.tabRemoved();
                }
            } catch (err) {}
            // for memory leak
            var frame = ZetaUtils.getIframeById(fid);
            if (frame != null) {
                frame.src = "javascript:false";
            }
        });
    } else {
        var html = '<iframe id="contentFrame" name="contentFrame" width=100% height=100% src="' + home.url + '" frameborder=0></iframe>';
        contentPanel = new Ext.Panel({
        	title: home.title, 
        	iconCls: home.icon,
            border: ZetaGUI.enableBorders, 
            frame: ZetaGUI.roundedBorders,
            collapsible: false, 
            region: "center", 
            plain: true, 
            html: html
        });
    }
    contentView = new Ext.Panel({
    	id: "content-panel", 
    	layout: "border", 
    	region: "center",
        border: false, 
        margins: "0 0 0 0", 
        items: [contentPanel]
    });
}

/**
* 回到起始页面
*/
function goToStartPage() {
	if (isTabbed) {
		contentPanel.setActiveTab(0);
	} else {
		addView(homeView.id, homeView.title, homeView.icon, homeView.url);
	}
}

/**
* 页面unload事件
*/
function doMainOnUnload() {	
	stopEventDispatcher(); 
}

/**
* 页面beforeunload事件
*/
window.onbeforeunload = function (e) {
	var evt = ZetaUtils.getEvent(e);
	var n = evt.screenX - window.screenLeft;
	var b = n > document.documentElement.scrollWidth - 20;
	if (b && evt.clientY < 0 || evt.altKey) {
		try {
			getReceiverFrame().doExit();
		} catch (err) {
		}
		$.ajax({
			url: "logoffQuietly.tv", 
			type: "GET", 
			async: false, 
			cache: false, 
			timeout: 3000
		});
	}
};

/**
* 整体框架刷新，包括菜单页面和当前激活页面
*/
function onRefreshClick() {
	var frame = getMenuFrame();
	if (frame != null && frame.doRefresh) {
		frame.doRefresh();
	}
	frame = getActiveFrameById();
	if (frame != null) {
		if (frame.doRefresh) {
			frame.doRefresh();
		} else {
			frame.src = frame.src;
		}
	}
}

/**
* 页面初始化
*/
$(function(){
	// add by fanzidong@20170623 珠江数码专用，获取关注告警，进行告警弹窗关注
	if(supportGZ) {
		try{
			$.ajax({
				url: '/fault/loadConcernAlertTypes.tv',
				cache:false, 
				method:'post',
				success: function(json) {
					if(json) {
						concernAlerts = json;
					}
				},
				error: function(){
					
				}
			});
		}catch(e) {
			
		}
	}
	
	if(firstDayOfWeek===''){
		firstDayOfWeek = 1;
	}
	
	//初始化所有告警声音
	$.get('/fault/fetchAllAlertSoundsName.tv', function(sounds){
        oAlertSound = sounds;
    })
    
    //
    $("body").delegate("#cpId","mousedown",function(){
        setZetaClickOltLink("");
    })
    $("body").delegate("a.x-tab-strip-close","mousedown",function(){
        setZetaClickOltLink("");
    })
    
	nm3k.icoCartoon = navCartoonFlag;//顶部图标是否具有动画效果; 
	nm3k.selectedIndex = 0;//点击选中的是哪个;
	nm3k.hoverTop = -65;//hover的top高度;
	nm3k.selectedTop = -130;//选中的top高度;
	$("#menuDl .menuBtn:eq(0)").css("top",nm3k.selectedTop).css("cursor","default").attr("name","sel");//默认第一个选中;
	//alert(lastSelectedNaviBar)
	getHeadIco();
	function getHeadIco(){
		$sel = $("#menuDl ul[alt='" + lastSelectedNaviBar + "']");
		if($sel.length != 1){return;}
		$("#menuDl .menuBtn").css({cursor:"pointer",top:0}).removeAttr("name");		
		$sel.css({top:nm3k.selectedTop, cursor:"default"}).attr("name","sel");
		$sel.parent().prev().addClass("selelctedLine");//把前后两个dt都变黑;
		$sel.parent().next().addClass("selelctedLine");
		var num = $("#menuDl .menuBtn").index($sel);
		nm3k.selectedIndex = num;
		
	}
	//移动-移开-点击头部大图标上;
	$("#menuDl .menuBtn").mouseenter(function(){
		if($(this).attr("name") == "sel"){
			return false;
		}
		if(nm3k.icoCartoon){//如果有动画效果;				
			$(this).stop().animate({"top":nm3k.hoverTop},300);
		}else{
			$(this).css("top",nm3k.hoverTop);
		};//end if;
	}).mouseleave(function(){
		if($(this).attr("name") == "sel"){
			return false;
		}
		if(nm3k.icoCartoon){//如果有动画效果;
			$(this).stop().animate({"top":0},300);
		}else{
			$(this).css("top",0);
		};//end if;
	}).click(function(){
		var nowIndex = $("#menuDl .menuBtn").index(this);
		if(nm3k.selectedIndex == nowIndex){
			//对着已经选择的点击;
			return;
		}
		$("#menuDl .menuBtn").css("cursor","pointer");
		$(this).css("cursor","default");
		$(this).attr("name","sel");//name值为sel,则表示这个是选中的;
		$("#menuDl dt").removeClass("selelctedLine");
		$(this).parent().prev().addClass("selelctedLine");//把前后两个dt都变黑;
		$(this).parent().next().addClass("selelctedLine");
		$("#menuDl .menuBtn:eq("+ nm3k.selectedIndex +")").removeAttr("name");
		if(nm3k.icoCartoon){//如果有动画效果;
			$("#menuDl .menuBtn:eq("+ nm3k.selectedIndex +")").stop().animate({"top":0},300);
			$(this).stop().animate({"top":nm3k.selectedTop},300);
		}else{
			$("#menuDl .menuBtn:eq("+ nm3k.selectedIndex +")").css("top",0);
			$(this).css("top",nm3k.selectedTop);
		};//end if;
		nm3k.selectedIndex = nowIndex;
		var txt = $(this).find("li:eq(0)").text();
		$("#navi-panel span").text(txt);
		
	});
	
	//开启，关闭 导航动画;
	$("#navCartoon").live("mousedown",function(){
		if(nm3k.icoCartoon){
			nm3k.icoCartoon = false;				
		}else{
			nm3k.icoCartoon = true;
		}
	});//end live;
	
	//修正firefox 中高度不自适应;
	function correctHeight(){
		$("#navi-div").height();
	}
	
	//点击一下，红色提示消失;
	$("#menu_tips").live("click",function(){
		redTip({display:"hide"});
	})
	$("#helpCir").click(function(){
		var $me = $(this),
		    src = "help/"+ window.language +"/" + $me.data("src"),
		    $body = $('body'),
			offset = 100,
		    w = $body.width() - offset,
		    h = $body.height() - offset,
		    w2 = w < 800 ? 800 : w, //最小尺寸 800 * 500;
		    h2 = h < 500 ? 500 : h;
		
		$me.css({display: 'none'}); 
		createDialog('help', '@COMMON.help@', w2, h2, src, null, true, true, function(){
			$me.css({display: 'block'});
		});
		
	})
	
	//Modify by Victor@20140911李翔改，如果上次记录的导航栏在本次无权限等原因不显示时，直接显示工作台（所有用户有这个权限）
	if( $("#menuDl ul[alt='" + lastSelectedNaviBar + "']").length != 1 ){
        window.lastSelectedNaviBar = 'Workbench';
        $('#menuFrame').attr("src","workbench/show" + window.lastSelectedNaviBar + ".tv");
    } 
	
	//如果是锁屏状态,则登陆时就锁定屏幕
	if(isLockScreen){
		lockScreen();
	}
	coordInfoElement = Zeta$("coordInfo");
	mapInfoElement = Zeta$("mapInfo");
});//end document.ready;

Ext.onReady(function(){
	//30 days
	var cp = new Ext.state.CookieProvider({expires: new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 30))});
	Ext.state.Manager.setProvider(cp);
	Ext.MessageBox.minWidth = 280;
	Ext.QuickTips.init();
	
	bulidNavigatorPanel();
	
	startServerPingDispatch();
	
	//初始化公告板
	initializeBillboard();
	setLoadingProgress(100);
	
	//设置正确的左侧标题栏文字;
	$sel = $("#menuDl ul[alt='" + lastSelectedNaviBar + "']");
	icoTxt = $sel.find("li:eq(0)").text();
	$("#navi-panel span").text(icoTxt);
});//end document.ready;

Ext.onReady(function() {
	//获取用户设置的主页
	var home = getCookieValue(userName + '.home');
    if (home == null) {
        home = {id: 'mydesktop', title: I18N.COMMON.myDesktop, icon: 'mydesktopIcon', url: 'portal/showMydesktop.tv'};
    }
    homeView = home;
    
    bulidCenterContentPanel(home);
	
    //TODO pageSize不能硬编码
	eventStore = new Ext.data.JsonStore({
		url: 'alert/loadRecentAlert.tv?pageSize=25',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['alertId', 'entityId', 'name', 'host', 'source', 'typeId', 'typeName', 'message', 'level', 'levelName', 'firstTimeStr']
	});
	eventStore.setDefaultSort('firstTimeStr', 'DESC');

	var eventMargins = "0 "  + ZetaGUI.viewMargin.right + " 0 0";
	var eventMargins1 = "4 "  + ZetaGUI.viewMargin.right + " 0 0";
	headerPanel = new Ext.BoxComponent({region: "north", el: "headerbar", margins: "0 0 0 0",
		height: ZetaGUI.headerHeight, maxSize: ZetaGUI.headerHeight, stateful: false});
	
	var bottomMargin = ZetaGUI.viewMargin.bottom + " 0 0 0";
	previousView.push({id: home.id, title: home.title, icon: home.icon, url: home.url});
	if (usedFirstly) {
		previousView.push({id: 'welcome', title: I18N.COMMON.welcome, icon: 'welcome-tabicon', url: 'workbench/welcome.jsp'});
		curView.id = 'welcome';
		curView.title = I18N.COMMON.welcome;
		curView.icon = 'welcome-tabicon';
		curView.url = 'workbench/welcome.jsp';
		//startTopoDiscovery();
	} else {
		curView.id = home.id;
		curView.title = home.title;
		curView.icon = home.icon;
		curView.url = home.url;
	}
	try{
		contentPanel.setActiveTab(home.id);
	}catch(e){
		
	}
	// show view and layout
	viewport = new Ext.Viewport({
	    title: "Topvision NM3000", 
	    layout: "border",
		items: [headerPanel, naviPanel, contentView], 
		renderTo: Ext.getBody()
	});
});

/* ==========================================================
 * END:  这段内容是主页自己的功能函数区域
 * ========================================================== */

/* ==========================================================
 * START:  这段内容是提供给单个子页面功能函数区域，与首页自己没关系，也不具备共性
 * ========================================================== */

/**
* 设置ZetaClickOltLink
* @param {Integer} id 选中的OLT的id 
*/
function setZetaClickOltLink(id){
	ZetaClickOltLink = id;
}

/**
* 获取ZetaClickOltLink
* @return ZetaClickOltLink
*/
function getZetaClickOltLink(){
	return ZetaClickOltLink;
}

/**
* 获取ZetaClipboard
* @return ZetaClipboard
*/
function getZetaClipboard() {
	return ZetaClipboard;
}

/**
* 设置ZetaClickOltLink
* @param {Object} clip 放置到剪切板中的对象 
*/
function setZetaClipboard(clip) {
	ZetaClipboard = clip;
}

/**
* 获取cookie中key为name的值，如果不存在则返回默认值
* @return cookie的值
*/
function getCookieValue(name, defaultValue) { 
	return Ext.state.Manager.get(name, defaultValue); 
}

/**
* 设置cookie
* @param {String} name cookie的key
* @param {Object} value cookie的value
*/
function setCookieValue(name, value) {	
	Ext.state.Manager.set(name, value); 
}

/**
* 清除cookie中key为name的值
* @param {String} name cookie的key
*/
function clearCookieValue(name) { 
	Ext.state.Manager.clear(name); 
}

/**
* 获取ZetaCallback
* @return ZetaCallback
*/
function getZetaCallback() {
	return ZetaCallback;
}

/**
* 打开拓扑发现指导页面
*/
function startTopoDiscovery() {	
	createDialog("modalDlg", I18N.MAIN.discoveryGuide, 620, 420, "topology/showTopoDiscoveryGuide.tv", null, true, true);
}

/**
 * 打开google地图，并决定是否立即给出一个标记
 * @param markNow 是否立即给出一个标记
 */
function addToGoogleMap(markNow){
	window.parent.addView("ngm", I18N.MAIN.googleMap , "googleIcon", "google/showEntityGoogleMap.tv");
}

/**
 * 开始循环调用
 * @param _progress 循环执行的函数
 * @param intervalTime 间隔时间
 * @param isFirst 是不是第一次，第一次立即执行，后面间隔intervalTime时间
 */
function startProgess(_progress, intervalTime, isFirst){
	window.progressTimer = window.setTimeout(function(){
		try{
			_progress();
		}catch(e){}
		if(!window.progressTimer){
			return;
		}else{
			startProgess(_progress,intervalTime,true)
		}
	}, isFirst ? intervalTime :  0);
}

/**
 * 停止循环调用
 */
function stopProgress() {
	if(window.progressTimer){
		window.clearTimeout(window.progressTimer)
		window.progressTimer = null;
	}
}

/*******************************************************************
moved by @bravin
 	以下三个方法按钮的通用处理方法，从ZetaAdapter中移动过来，减少ZetaApater的体积
 ******************************************************************/
function switchMouseDown( evt ) {
	srcEL = evt.target;
	if(srcEL && srcEL.tagName && srcEL.tagName.toUpperCase() == 'BUTTON'){
		if(srcEL.className.indexOf("BUTTON_OVER") > -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON_OVER", 'BUTTON_PRESSED');
		}else if(srcEL.className.indexOf("BUTTON_PRESSED") == -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON", 'BUTTON_PRESSED');
		}
		//$(srcEL).trigger("mousedown");
	}
}
function switchMouseOver(evt) {
	srcEL = evt.target;
	if(srcEL && srcEL.tagName && srcEL.tagName.toUpperCase() == 'BUTTON'){
		if(srcEL.className.indexOf("BUTTON_PRESSED") > -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON_PRESSED",  'BUTTON_OVER');
		}else if(srcEL.className.indexOf("BUTTON_OVER") == -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON",'BUTTON_OVER');
		}
	}
}
function switchMouseOut(evt) {
	srcEL = evt.target;
	if( srcEL && srcEL.tagName && srcEL.tagName.toUpperCase() == 'BUTTON'){
		if(srcEL.className.indexOf("BUTTON_PRESSED") > -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON_PRESSED", 'BUTTON');
		}else if(srcEL.className.indexOf("BUTTON_OVER") != -1){
			srcEL.className = srcEL.className.replaceAll("BUTTON_OVER", 'BUTTON');
		}
	}
}

/***************************************************************
console 对象
***************************************************************/
function Console(){
	this.log = function(){};
}
if(typeof console == "undefined"){
	WIN.console = new Console();
}
//刷新左侧目录树,有的时候不能立马刷新，传了参数则表示晚一点儿刷新;
function refreshTopoTree(delayTime){
	if(arguments.length === 1){
		setTimeout(function(){
			refreshTopoTree2();
		},delayTime);
	}else{
		refreshTopoTree2();
	}
}
function refreshTopoTree2(){
	$menuBtn_topo = $("#menuBtn_topo");
	if( $menuBtn_topo.attr("name")=="sel" ){
		frames["menuFrame"].refreshTree();
	}
}
//左侧树正在删除;
function deleteTopoTree(){
	$menuBtn_topo = $("#menuBtn_topo");
	if( $menuBtn_topo.attr("name")=="sel" ){
		frames["menuFrame"].deleteIng();
	}
}
//左侧拓扑图删除结束;
function endDeleteTopo(){
	$menuBtn_topo = $("#menuBtn_topo");
	if( $menuBtn_topo.attr("name")=="sel" ){
		frames["menuFrame"].endDelete();
	}
}

function downloadInFrame(url) {
	document.getElementById('main_download_iframe').src = url;
}

function setCoordInfo(info) {
	//coordInfoElement.innerText = info;
	//原本是在状态栏显示鼠标x和y
}

function setStatusBarInfo(){}//保留的一个接口

Ext.ux.IFrameComponent = Ext.extend(Ext.BoxComponent, {
    onRender : function(ct, position) {
        this.el = ct.createChild({
            tag : "iframe",
            id : "frame" + this.id,
            frameBorder : 0,
            src : this.url
        });
   }
});

/* ==========================================================
 * END:  这段内容是提供给单个子页面功能函数区域，与首页自己没关系，也不具备共性
 * ========================================================== */

//供各功能跨frame交互数据使用
var intermediateObject = {}; 
