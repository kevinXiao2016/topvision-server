//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store: store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}
function authRender(value, meta, record){
	if(value != null){
		return authArray[value];
	}else{
		return "--";
	}
}
function manuRender(value, meta, record){
	var profileId = record.data.profileId;
	return String.format("<a href='javascript:;' onClick='bindGroup({0})'>@igmp.bindGroup@</a> / <a href='javascript:;' onClick='editProfile()'>@COMMON.edit@</a> / <a href='javascript:;' onClick='deleteProfile({0})'>@COMMON.delete@</a>",profileId); 
}

function bindGroup(profileId){
	window.top.createDialog('relateGroup', "@igmp.profileBind@", 800, 600, "/epon/igmpconfig/showProfileRelaGroup.tv?entityId="+entityId+"&profileId="+profileId, null, true, true);
}
function editProfile(){
	openBg();
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		createOpenLayer({
			type  : 'edit',
			title : '@igmp.editCtcProfile@',
			desc  : '@igmp.editCtcProfileDesc@',
			data  : record.data
		});
	}
}
function deleteProfile(profileId){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteProfile@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteCtcProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileId : profileId
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.deleteProfileS@</b>'
       			});
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteProfileF@");
			},
			cache : false
		});
	});
}

function batchDeleteProfile(){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.delSelectedProfile@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		var profileIds = getSelectedProfileIds();
		$.ajax({
			url : '/epon/igmpconfig/batchDeleteCtcProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				profileIds : profileIds
			},
			dataType :　'text',
			success : function(json) {
				if(json != "none"){//出现删除失败
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@tip.failDeleteProfiles@' + json + '</b>'
	       			});
				}else{//全部删除成功
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@tip.deleteProfileS@</b>'
	       			});
				}
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteProfileF@");
			},
			cache : false
		});
	});
}

function addCtcProfile(jsonData){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addCtcProfile.tv',
		type : 'POST',
		data : jsonData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addProfileS@</b>'
   			});
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addProfileF@");
		},
		cache : false
	});
}

function refreshCtcProfile(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshCtcProfile.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//需要提供可控组播模板的修改
function modifyCtcProfile(){
	var validate = validateData('edit');
	if( validate === false){
		return;
	}
	var profileData = {
		"ctcProfile.entityId"        : entityId,
		"ctcProfile.profileId"       : $("#profileId").val(),
		"ctcProfile.profileDesc"     : $("#profileDesc").val(), 
		"ctcProfile.profileAuth"     : $("#profileAuth").val(),
		"ctcProfile.previewTime"     : $("#previewTime").val(), 
		"ctcProfile.previewInterval" : $("#previewInterval").val(),
	    "ctcProfile.previewCount"    : $("#previewCount").val(),  
		"ctcProfile.profileName"     : $("#profileName").val()
	};
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyCtcProfile.tv',
		type : 'POST',
		data : profileData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.editProfileS@</b>'
   			});
			refreshPage();
			closeFakeWindow();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.editProfileF@");
		},
		cache : false
	});
}

function aliasRender(value, meta, record){
	if(value == null || value == ""){
		return "--";
	}else{
		return value;
	}
}
$(function(){
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	}); 
	cm = new Ext.grid.ColumnModel({
		columns: [
		    sm,
		    {header: '@igmp.profileID@', dataIndex: 'profileId', width:60},
		    {header: '<div class="txtCenter">@igmp.profileDesc@</div>', align:'left', dataIndex: 'profileDesc', width:150},
		    {header: '<div class="txtCenter">@igmp.profileAlias@</div>', align:'left', dataIndex: 'profileName',width:150, renderer : aliasRender},
		    {header: '@igmp.profileAuth@', dataIndex: 'profileAuth', renderer : authRender, width:100},
		    {header: '@igmp.previewTime@', dataIndex: 'previewTime', width:100},
		    {header: '@igmp.previewInterval@', dataIndex: 'previewInterval', width:100},
		    {header: '@igmp.previewCount@', dataIndex: 'previewCount', width:100},
		    {header: '@COMMON.manu@', fixed:true, dataIndex: 'op', renderer : manuRender, width: 180}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadCtcProfileList.tv',
		fields : ["entityId", "profileId", "profileDesc","profileAuth","previewTime","previewInterval","previewCount","profileName"],
		root : 'data',
		totalProperty: 'rowCount',
		baseParams : {
			entityId : entityId
		}
	});
	grid = new Ext.grid.GridPanel({
		margins : '10 10 0 10',
		cm     : cm,
		sm     : sm,
		store  : store,
		tbar   : [{
			xtype : 'button',
			text : '@BUTTON.add@',
			iconCls : 'bmenu_new',
			handler : function(){
				addFn();
			}
		},'-',{
			xtype : 'button',
			text : '@igmp.fetchCtcProfile@',
			iconCls : 'bmenu_equipment',
			handler : refreshCtcProfile
		},'-',{
			id : "batchDelete",
			xtype : 'button',
			text : '@igmp.batchDelete@',
			iconCls : 'bmenu_delete',
			handler : function(){
				batchDeleteProfile();
			},
			disabled: true
		}],
		region : 'center',
		stripeRows   : true,
		bbar : buildPagingToolBar(),
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	
	store.load({params: {start: 0, limit: pageSize}});
	
	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});//end document.ready;
function addFn(){
	openBg();
	createOpenLayer({
		type  : 'add',
		title : '@igmp.addIgmpProfile@',
		desc  : '@igmp.addIgmpProfile@'
	});
}
function createOpenLayer(obj){
	openFakeExtWin();
	var tpl = new Ext.XTemplate(
		'<div class="fakeExtWindowTitle"><b>{title}</b><label onclick="closeFakeWindow()"></label></div>',   
		'<div class="openWinHeader">',
	    	'<div class="openWinTip">{desc}</div>',
	    	'<div class="rightCirIco pageCirIco"></div>',
		'</div>',
		'<div class="edge10 pT30">',
			'<table id="openLayerTable" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
			    '<tbody>',
			        '<tr>',
			            '<td class="rightBlueTxt" width="140">@igmp.profileID@:</td>',
			            '<td width="210"><input {[values.type==="edit" ? "disabled=disabled" : ""]} id="profileId" toolTip="@igmp.tip.tip20@" maxlength="3" type="text" class="normalInput w180"></td>',
			            '<td class="rightBlueTxt" width="140">@igmp.profileDesc@:</td>',
			            '<td><input id="profileDesc" type="text" class="normalInput w180" toolTip="@igmp.tip.tip21@" maxlength="31"></td>',
			        '</tr>',
			        '<tr class="darkZebraTr">',
						'<td class="rightBlueTxt">@igmp.profileAlias@:</td>',
						'<td><input id="profileName" class="normalInput w180" type="text" toolTip="@COMMON.anotherName@" maxlength="63" /></td>',
			            '<td class="rightBlueTxt">@igmp.profileAuth@:</td>',
			            '<td>',
			            	'<select id="profileAuth" class="normalSel w180" onchange="showPreviewBody()">',
			            		'<option value="2">permit</option>',
			            		'<option value="1">deny</option>',
			            		'<option value="3">preview</option>',
			            	'</select>',
			            '</td>',
			        '</tr>',
			    '</tbody>',
			    '<tbody style="visibility:hidden">',
			        '<tr>',
			        	'<td class="rightBlueTxt">@igmp.previewTime@:</td>',
		            	'<td><input id="previewTime" type="text" class="normalInput w180" toolTip="@igmp.tip.tip22@" value="120" maxlength="4"> S</td>',
			            '<td class="rightBlueTxt">@igmp.previewInterval@:</td>',
			            '<td><input id="previewInterval" type="text" class="normalInput w180" value="120" toolTip="@igmp.tip.tip23@" maxlength="4"> S</td>',
			        '</tr>',
				    '<tr>',
				    	'<td class="rightBlueTxt">@igmp.previewCount@:</td>',
		            	'<td colspan="3"><input id="previewCount" type="text" class="normalInput w180" value="8" toolTip="@igmp.tip.tip24@" maxlength="3"></td>',				    	
			        '</tr>',
			    '</tbody>',
			'</table>',
   			'<div class="noWidthCenterOuter clearBoth">',
   		        '<ol class="upChannelListOl pB0 pT40 noWidthCenter">',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="{[values.type==="add" ? "addRecord()" : "modifyCtcProfile()"]}"><span><i class="{[values.type==="add" ? "miniIcoAdd" : "miniIcoEdit"]}"></i>{[values.type==="add" ? "@BUTTON.add@" : "@COMMON.edit@"]}</span></a></li>',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeFakeWindow()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
   		        '</ol>',
   		     '</div>',
   		 '</div>'
   		);
	var $fakeExtWindow = Ext.get("fakeExtWindow"),
	    aPos = $fakeExtWindow.getCenterXY();
	
	tpl.overwrite($fakeExtWindow, obj);
	$fakeExtWindow.setXY([aPos[0],2]);
	if(obj.type === "edit"){
		var d = obj.data;
		$("#profileId").val(d.profileId);
		$("#profileDesc").val(d.profileDesc);
		$("#profileName").val(d.profileName);
		$("#profileAuth").val(d.profileAuth);
		$("#previewTime").val(d.previewTime);
		$("#previewInterval").val(d.previewInterval);
		$("#previewCount").val(d.previewCount);
		showPreviewBody();
	}
}
function showPreviewBody(){
	var value = parseInt($("#profileAuth").val(), 10),
	    tbody = $("#openLayerTable tbody:eq(1)");
	
	if( value === 3){
		tbody.css({visibility : 'visible'});
	}else{
		tbody.css({visibility : 'hidden'});
	}
}
function validateData(type){
	var $profileAuth   = $("#profileAuth"),
	    profileAuthVal = parseInt($profileAuth.val(), 10),
	    arr = [{
			id    : 'profileId',      //模板id;
			range : [1,256] 
		}];
	
	if(profileAuthVal === 3){ //preview模式需要验证
		arr2 = [{
			id    : 'previewTime',    //预览最大时长;
			range : [10,6000]
		},{
			id    : 'previewInterval',//预览时间间隔;
			range : [0,7650]
		},{
			id    : 'previewCount',    //预览次数
			range : [1,255]
		}];
		arr = $.merge(arr,arr2);
	}
	var result = customValidateFn(arr);
	if(result !== true){
		result.focus();
		return false;
	}
	//验证模板描述;
	var destReg      = /^[a-zA-Z\d-_]{1,31}$/,
	    $profileDesc = $("#profileDesc"),
	    $profileName = $("#profileName");
	
	if( !destReg.test($profileDesc.val().trim()) ){
		$profileDesc.focus();
		return false;
	}
	//验证模板名称;
	if("" != $profileName.val()){
		if( !V.isAnotherName($profileName.val()) ){
			$profileName.focus();
			return false;
		}
	}
	
	var proId = parseInt($("#profileId").val(), 10);
	if(type === 'add'){
		var flag;
		loadAllProfileId(function(json){
			if(json.length > 0){
				for(var i=0,len=json.length; i<len; i++){
					if(proId == json[i]){
						flag = true;
					}
				}
			}
		});
		if(flag === true){
			var str = String.format('@tip.addProfileF2@', proId);
			top.showMessageDlg("@COMMON.tip@", str);
			return false;
		}
	}
	return true;
}
function addRecord(){
	var validate = validateData('add');
	if( validate === false){
		return;
	}
	var $profileAuth   = $("#profileAuth"),
        profileAuthVal = parseInt($profileAuth.val(), 10),
        $profileDesc   = $("#profileDesc"),
	    $profileName   = $("#profileName");
	
	var o = {
		"ctcProfile.entityId"        : entityId,
		"ctcProfile.profileId"       : $("#profileId").val(),
		"ctcProfile.profileDesc"     : $profileDesc.val(), 
		"ctcProfile.profileAuth"     : profileAuthVal,
		"ctcProfile.profileName"     : $profileName.val(),
		"ctcProfile.previewTime"     : 120,
		"ctcProfile.previewInterval" : 120,
		"ctcProfile.previewCount"    : 8,	
	};
	if(profileAuthVal === 3){ //preview模式
		o["ctcProfile.previewTime"]     = $("#previewTime").val();
		o["ctcProfile.previewInterval"] = $("#previewInterval").val();
		o["ctcProfile.previewCount"]    = $("#previewCount").val();
	}
	addCtcProfile(o);
}

//加载所有的模板ID,用于添加时校验模板ID
function loadAllProfileId(callBack){
	$.ajax({
		url : '/epon/igmpconfig/loadAllProfileId.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function(json) {
			callBack(json);
		},
		error : function(json) {
			callBack([]);
		},
		cache : false,
		async : false
	});
}

function refreshPage(){
	window.location.href = window.location.href;
}

//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
};

function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['batchDelete'], false);
    }else{
    	disabledBtn(['batchDelete'], true);
    }
};

//获取选择的模板 id数组
function getSelectedProfileIds(){
	var sm = grid.getSelectionModel();
	var selectedSections = [];
	var selectedProfileIds = [];
	if (sm != null && sm.hasSelection()) {
		selectedSections = sm.getSelections();
		selectedSections.forEach(function(value){
			selectedProfileIds.push(value.data.profileId);
		})
	}
	return selectedProfileIds;
}