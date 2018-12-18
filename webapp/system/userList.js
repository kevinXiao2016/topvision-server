/**
 * author: niejun
 *
 * User management.
 */
var store = null;
var grid = null;
function renderStatus(value, p, record){
	if (record.data.status==1) {
		return String.format('<img nm3kTip="@COMMON.start@" class="nm3kTip" src="../images/yes.png" border=0 align=absmiddle>',
			value);
	} else {
		return String.format('<img nm3kTip="@COMMON.stop@" class="nm3kTip" src="../images/wrong.png" border=0 align=absmiddle>',
			 value);
	}
}
function lastLoginTime(value, p, record){
    if (record.data.lastLoginIp=='') {
        return "";
    } else {
        return value;
    }
}
//多IP登陆;
function renderMultpleIp(v, p, r){
	if(v){
		return String.format('<img class="cursorPointer" name="on" src="../images/speOn.png" alt="@COMMON.open@" onclick="allowMutilIpLogin({0},this)" />', r.data.userId);
	}else{
		return String.format('<img class="cursorPointer" name="off" src="../images/speOff.png" alt="@COMMON.close@" onclick="allowMutilIpLogin({0},this)" />', r.data.userId);
	}
}
//session超时时间;
function renderTimeout(v, p, r){
	if(v === -1){
		return "@COMMON.noSystemTimeout@"
	}
	return v;
}
function allowMutilIpLogin(userId,$dom){
	var allowMutliIpLogin = false;//通过读取name中的on和off来判断，修改该值时，记得修改name的值;
	switch($dom.name){
		case "off":
			allowMutliIpLogin = true;
			break;
	}
	$.ajax({
		url:"../system/allowMutliIpLogin.tv",cache:false,
		data:{
			userId:userId, allowMutliIpLoginStatus:allowMutliIpLogin
		},success:function(){
			if(allowMutliIpLogin){
				$dom.name = "on";
				$dom.src = "../images/speOn.png";
				$dom.alt = "@COMMON.open@"; 
			}else{
				$dom.name = "off";
				$dom.src = "../images/speOff.png"
				$dom.alt = "@COMMON.close@";
			}
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : "@COMMON.updateSuccessfully@"
			});
		},error:function(){
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : "@COMMON.updatefailure@"
			});
		}
	})
}
//设置系统超时时间;
function onSetSystemTime(){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	top.createDialog("setSessionTime", "@COMMON.setSessionTime@", 600, 370, '/system/showSessionTime.tv?userId='+record.data.userId);
}

function renderLockStatus(value, p, record){
    if (record.data.locked==false) {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/common/miniIcon/unlock.png" border=0 align=absmiddle>',
            I18N.SYSTEM.unlocked);  
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/common/miniIcon/lock.png" border=0 align=absmiddle>',
            I18N.SYSTEM.locked);   
    }
}

function buildPagingToolBar() {
	var states = [
        ['page25', '25', '25'],
        ['page50', '50', '50'],
        ['page75', '75', '75'],
        ['page100', '100', '100']
    ];
    var pageStore = new Ext.data.SimpleStore({
        fields: ['abbr', 'size'],
        data: states
    });
	var combo = new Ext.form.ComboBox({
		store: pageStore,
		displayField:'size',
		typeAhead: true,
		mode:'local',
		editable:false,
		triggerAction:'all',
		selectOnFocus:true,
		width: 80
	});
	combo.setValue(''+pageSize);
	combo.on('select', function(combo, record, index) {
		if(record.data.size == pageSize) return;
		Ext.Ajax.request({url: '../system/setPageSize.tv',
			success: function() {
				pagingToolbar.pageSize = pageSize = parseInt(record.data.size);
				pagingToolbar.doLoad(0);
			},
			failure: function(){},
			params: {pageSize:record.data.size}
		});
	});
	pagingToolbar = new Ext.PagingToolbar({pageSize:pageSize, store:store, displayInfo:true,
		items:['-', I18N.COMMON.perPage, combo, I18N.COMMON.tiao]});
	return pagingToolbar;
}

function onSetRoleClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
        var v = sm.getSelected();
        //window.parent.createDialog('roleDlg', I18N.SYSTEM.selectRole, 600, 370, 'system/popRole.jsp?roleName='+escape(escape(v.json.roleName))+'&userId='+v.json.userId, null, true, true, setRoleCallback);
        window.parent.createDialog('roleDlg', I18N.SYSTEM.selectRole, 600, 370, '/system/showSetRole.tv?roleName='+escape(escape(v.json.roleName))+'&userId='+v.json.userId, null, true, true, setRoleCallback);
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseSelectUser);
	}
}
function setRoleCallback() {
	var clipboard = window.parent.ZetaCallback;
	if (clipboard && clipboard.type == 'ok') {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		var userIds = [];
		for (var i = 0; i < selections.length; i++) {
			userIds[i] = selections[i].data.userId;
		}
		Ext.Ajax.request({url: '../system/setUserRole.tv',
		   success: function() {onRefreshClick();},
		   failure: function(){window.parent.showErrorDlg();},
		   params: {userIds : userIds, roleIds : clipboard.selectedItemId}
		});
	}
	window.parent.ZetaCallback = null;
}

function onNewUserClick() {
	window.top.createDialog("modalDlg", I18N.SYSTEM.newUser, 800, 500, "system/showNewUser.tv", null, true, true);
}
function onStartClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var userIds = [];
		var userNames =  [];
		for (var i = 0; i < selections.length; i++) {
			if(selections[i].data.status == 1){
				window.parent.showMessageDlg(I18N.COMMON.tip, selections[i].data.userName + " @SYSTEM.alreadyStart@");
				return;
			}
			userIds[i] = selections[i].data.userId;
			userNames[i] = selections[i].data.userName;
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.confirmStartUser, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../system/startUser.tv',
			   success: function() {onRefreshClick();},
			   failure: function(){window.parent.showErrorDlg();},
			   params: {userIds : userIds, userNames : userNames}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseSelectUser);
	}
}
function onStopClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var userIds = [];
		for (var i = 0; i < selections.length; i++) {
			if(selections[i].data.status == 0){
				window.parent.showMessageDlg(I18N.COMMON.tip, selections[i].data.userName + " @SYSTEM.alreadyStop@");
				return;
			}
			if(selections[i].data.userId == 2){
				return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.adminUserNotStop);
			}else{
				userIds.add(selections[i].data.userId);
			}
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.confirmStopUser, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../system/stopUser.tv',
			   success: onRefreshClick,
			   failure: function(){window.parent.showErrorDlg();},
			   params: {userIds : userIds}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseSelectUser);
	}
}
function onChangePwdClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var userId = selections[0].data.userId;
		var userName = selections[0].data.userName;
		window.top.createDialog('modifyUserPwd', I18N.MAIN.setPasswd, 600, 370, '/system/loadModifyPwd.tv?userId='+userId+"&userName="+userName, null, true, true);
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseSelectUser);
	}
}

function onSelectAllClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}	
}

function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		if(selections.length>=2){
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.UserList.selectUserToOperation);
        	return;
		}
		var userIds = [];
		var userName = selections[0].data.userName;
		if(userName=='admin'){
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.UserList.cannotDeleteSystemAdministrator);
        	return;
		}
		userIds[0] = selections[0].data.userId;
		
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.confirmDeleteUser, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../system/deleteUser.tv',
			   success: function() {
				   top.afterSaveOrDelete({
				      title: I18N.COMMON.tip,
				      html: '<b class="orangeTxt">'+ I18N.COMMON.deleteSuccess +'</b>'
				    });
				   if(userIds[0] == userId){
					   window.top.onLogoffClickNotConfirm();
				   }else{
					   onRefreshClick(); 
				   }
			   },
			   failure: function(){window.parent.showErrorDlg();},
			   params: {userIds : userIds}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.pleaseSelectUser);
	}
}
function onRefreshClick() {
	store.reload();
}
function onPropertyClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		window.top.showModalDlg(I18N.SYSTEM.userProperty, 800, 500, '/system/showModifyUser.tv?userId=' + r.data.userId);
	}
}
function nameHeader(value, cellmate, record) {
	return getI18NUserString(value);
}

function renderSee(v,m,r){
	return String.format('<a href="javascript:;" onclick="onPropertyClick()">@COMMON.edit@</a> / <a href="javascript:;" onclick="onDeleteClick()">@COMMON.del@</a> / <a href="javascript:;" class="withSub" onclick="otherFn(event)">@COMMON.others@</a>',r.data.userId);	
}

//查看;
function seeFn(userId){
	window.top.showModalDlg(I18N.SYSTEM.userProperty, 800, 500,	'system/modifyUser.tv?userId=' + userId);
}
//其它;
function otherFn(e){	
	e.getXY = function(){return [e.clientX,e.clientY];};
	mainMenu.showAt(e.getXY());
}

function onUnlockClick(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		$.ajax({
			url: '/system/unlockUser.tv',cache:false,
			data:{userName : r.data.userName},
			success:function(){
			    onRefreshClick();
				//window.parent.showMessageDlg(I18N.COMMON.tip, "@user.unlockOk@");
				top.afterSaveOrDelete({
	   				title: I18N.COMMON.tip,
	   				html: '<b class="orangeTxt">@user.unlockOk@</b>'
	   			});
			},error:function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, "@user.unlockEr@");
			}
			
		})
		
	}
}

var mainMenu;
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	store = new Ext.data.JsonStore({
		proxy: new Ext.data.HttpProxy({
			url: 'loadUserList.tv',
			method: 'post'
		}),
		root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    sortInfo: {field: 'userName', direction: "ASC"},
	    fields: ['userId', 'userName',"allowMutliIpLogin", "timeout", 'familyName', 'roleName', 'lastLoginIp','lastLoginTime', 'status','limitIp', 'mobile', 'email', 'createTime','userGroupName','bindIp','locked']
	});

	var cm = null;
	    cm = new Ext.grid.ColumnModel([
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.userNameHeader + '</div>', dataIndex: 'userName', align : 'left', sortable: true, width: 100},
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.familyNameHeader + '</div>', dataIndex: 'familyName', align : 'left', sortable: true, width: 100,renderer:nameHeader},
 		{header: '<div class="txtCenter">'+ I18N.SYSTEM.roleHeader + '</div>', dataIndex: 'roleName',  align : 'left', sortable: true, width: 100,renderer:nameHeader}, 
 		{header: '<div class="txtCenter">'+ I18N.SYSTEM.Region + '</div>', align : 'left', dataIndex: 'userGroupName', width: 150}, 
		{header: '<div class="txtCenter">'+ I18N.SYSTEM.lastLoginIpHeader + '</div>', align : 'left', dataIndex: 'lastLoginIp', sortable: true, width: 100},
		{header: '<div class="txtCenter">'+ I18N.SYSTEM.lastLoginTimeHeader + '</div>', align : 'left', dataIndex: 'lastLoginTime', sortable: true, width: 120},
    	//{header: '<div class="txtCenter">'+ I18N.SYSTEM.mobileHeader + '</div>', align : 'left', dataIndex: 'mobile', sortable: true, width: 100},
    	//{header: '<div class="txtCenter">'+ I18N.SYSTEM.emailHeader + '</div>', align : 'left', dataIndex: 'email', sortable: true,  width: 100},
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.bindIp + '</div>', align : 'left', dataIndex: 'bindIp', sortable: true, width: 120},
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.startStatus + '</div>', dataIndex: 'status', align : 'center', sortable: true, width: 100, renderer:renderStatus},
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.userLockStatusStatus + '</div>', dataIndex: 'locked', align : 'center', sortable: true, width: 100, renderer:renderLockStatus},
    	{header: '@SYSTEM.userSessionTime@', dataIndex:'timeout', sortable: true, width: 120, renderer:renderTimeout},
    	{header: '@SYSTEM.multipleIP@', dataIndex:'allowMutliIpLogin', sortable: true, width: 120, renderer:renderMultpleIp},
    	{header: '<div class="txtCenter">'+ I18N.SYSTEM.createTimeHeader + '</div>', dataIndex:'createTime', sortable: true, width: 120},
    	{header: '@COMMON.opration@', renderer:renderSee, width:140, align:'center', fixed:true}
    	]);
    
    	cm.defaultSortable = true;	
		grid = new Ext.grid.GridPanel({border:false, region:'center',
	        store:store, cm: cm,
	        bodyCssClass: "normalTable",stripeRows:true,
	        viewConfig: {forceFit:true, enableRowBody: false, showPreview: false},
	        loadMask : true, 
	        tbar:[{text:I18N.COMMON.create, iconCls:'bmenu_new',cls:'mL10', handler: onNewUserClick, disabled : !userPower}, 
	        	{text:I18N.SYSTEM.deleteAction, iconCls:'bmenu_delete', handler:onDeleteClick, disabled : !userPower}, '-',
	        	{text:I18N.SYSTEM.start, iconCls:'bmenu_play', handler:onStartClick, disabled : !userPower},
	        	{text:I18N.SYSTEM.stop, iconCls:'bmenu_stop', handler:onStopClick, disabled : !userPower}, '-',
	        	{text:I18N.SYSTEM.setRole, iconCls: 'bmenu_user', handler:onSetRoleClick, disabled : !userPower}, '-',
			    {text:I18N.SYSTEM.modifyPasswd, iconCls: 'bmenu_password', handler: onChangePwdClick, disabled : !userPower}, '-',
			    {text:I18N.SYSTEM.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}],
			renderTo: document.body
	    });     	
	/*}*/

	var deleteMenu = new Ext.menu.Item({text:I18N.SYSTEM.deleteAction, iconCls:'bmenu_delete', handler: onDeleteClick});
	var propertyMenu = new Ext.menu.Item({text:'<font style=\"font-weight:bold\">@COMMON.property@</font>', handler: onPropertyClick});
	mainMenu = new Ext.menu.Menu({id: 'mainMenu', minWidth: 150, items:[
		{text:"@SYSTEM.start@", handler:onStartClick, disabled : !userPower},
		{text:"@SYSTEM.stop@", handler:onStopClick, disabled : !userPower}, '-',
		{text:"@SYSTEM.unlock@", handler: onUnlockClick, hidden : !userPower}, '-',
		{text:'@COMMON.setSessionTime@', handler:onSetSystemTime},
		{text:"@SYSTEM.refresh@", iconCls:'bmenu_refresh', handler:onRefreshClick}
	]});

    new Ext.Viewport({layout:'fit', items:[grid]});
    store.load();
});