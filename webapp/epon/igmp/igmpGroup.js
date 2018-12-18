//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store: store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}

function manuRender(value, meta, record){
	var joinMode = record.data.joinMode;
	if(joinMode == 1){
		return String.format("<a href='javascript:;' onClick='cancelPreJoin()'>@igmp.cancelPrejoin@</a> / <a href='javascript:;' onClick='deleteGroup()'>@COMMON.delete@</a>");
	}else if(joinMode == 2){
		return String.format("<a href='javascript:;' onClick='editGroup()'>@COMMON.edit@</a> / <a href='javascript:;' onClick='deleteGroup()'>@COMMON.delete@</a>");
	}else{
		return String.format("<a href='javascript:;' onClick='deleteGroup()'>@COMMON.delete@</a>");
	}
}
function cancelPreJoin(){
	var selectRec = grid.getSelectionModel().getSelected();
	var preJoinData = {
			"groupInfo.entityId"   : window.entityId,
			"groupInfo.vlanId"     : selectRec.data.vlanId,
			"groupInfo.groupId"    : selectRec.data.groupId,
			"groupInfo.groupIp"    : selectRec.data.groupIp,
			"groupInfo.groupSrcIp" : selectRec.data.groupSrcIp,
			"groupInfo.joinMode"   : 2
		}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyGroupPreJoin.tv',
		type : 'POST',
		data : preJoinData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
				title: "@COMMON.tip@",
				html: '<b class="orangeTxt">@tip.cancelPreJoinS@</b>'
			});
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.cancelPreJoinF@");
		},
		cache : false
	});
}
	
function deleteGroup(groupId){
	var selectRec = grid.getSelectionModel().getSelected();
	var toDeletaData = {
			"groupInfo.entityId"   : window.entityId,
			"groupInfo.vlanId"     : selectRec.data.vlanId,
			"groupInfo.groupId"    : selectRec.data.groupId,
			"groupInfo.groupIp"    : selectRec.data.groupIp,
			"groupInfo.groupSrcIp" : selectRec.data.groupSrcIp
		}
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteGroup@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteVlanGroup.tv',
			type : 'POST',
			data : toDeletaData,
			dataType :　'json',
			success : function() {
//				store.reload();
				refreshPage();
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.delGroupS@</b>'
       			});
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.delGroupF@");
			},
			cache : false
		});
	});
}

function batchDeleteGroup(){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.delSelectedGroup@", function(type) {
		if (type == 'no') {
			return;
		}
		var groupInfos = getSelectedGroups();
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/batchDeleteVlanGroup.tv',
			type : 'POST',
			data : {
				entityId : window.entityId,
				groupInfos : JSON.stringify(groupInfos)
			},
			dataType :　'text',
			success : function(json) {
				if (json == "error"){//后台出现错误
					top.showMessageDlg("@COMMON.tip@", "@tip.delGroupF@");
					return;
				}else if(json != "none"){//出现删除失败
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@confirm.failDeleteGroups@' + json + '</b>'
	       			});
				}else{//全部删除成功
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@tip.delGroupS@</b>'
	       			});
				}
//				store.reload();
				refreshPage();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.delGroupF@");
			},
			cache : false
		});
	});
}

function refreshGroup(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshVlanGroup.tv',
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

function joinModeRender(value, meta, record){
	if(value == 1){
		return "preJoin";
	}else if(value == 2){
		return "no preJoin";
	}else{
		return "--";
	}
}

function aliasRender(value, meta, record){
	if(value == null || value == ""){
		return "--";
	}else{
		return value;
	}
}

function stateRender(value, meta, record){
	if(value === null || value === ''){
		return "--";
	}else if(value === 0){
		return "no active";
	}else{
		return "active";
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
		    {header: '@igmp.vlanGroupId@', dataIndex: 'groupId', width:60},
		    {header: '@igmp.vlan@', dataIndex: 'vlanId', width:80},
		    {header: '<div class="txtCenter">@igmp.vlanGroupDesc@</div>', dataIndex: 'groupDesc', align:'left', width:150},
		    {header: '<div class="txtCenter">@igmp.vlanGroupAlias@</div>', dataIndex: 'groupName', align:'left', width:150, renderer : aliasRender},
		    {header: '<div class="txtCenter">@igmp.groupIp@</div>', dataIndex: 'groupIp', align:'left', width:140},
		    {header: '<div class="txtCenter">@igmp.gourpSourceIp@</div>', dataIndex: 'groupSrcIp', align:'left', width:140},
		    {header: '@igmp.maxBw2@(kps)', dataIndex: 'groupMaxBW', width:100},
		    {header: '@igmp.prejoinStatus@', dataIndex: 'joinMode', renderer : joinModeRender, width:100},
		    {header: '@igmp.activeStatus@', dataIndex: 'groupState', width:100, renderer : stateRender},
		    {header: '@COMMON.manu@', dataIndex: 'op', width: 180, fixed:true, renderer : manuRender, width:140}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadVlanGroupList.tv',
		fields : ["entityId", "vlanId", "groupId","groupIp","groupSrcIp","groupDesc","groupMaxBW","groupName", "joinMode", "groupState"],
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
			text : '@IGMP.batchImportGroup@',
			iconCls : 'bmenu_inport',
			handler : importGroups
		},'-',{
			cls   :'blueTxt',
			text  : '@igmp.prejoinStatus@@COMMON.maohao@',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
	     	html  : '<select id="preJoinSel" class="w100 normalSel" onchange="changePreJoin()">' + 
	     		    '<option value="-1">@COMMON.pleaseSelect@...</option>' + 
					'<option value="2">no preJoin</option>' + 
					'<option value="1">preJoin</option>' + 
					'</select>'
		},{
			xtype : 'tbspacer', 
			width : 10
		},{
			cls   :'blueTxt',
			text  : '@igmp.activeStatus@@COMMON.maohao@',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
	     	html  : '<select id="activeStatus" class="w100 normalSel">' + 
	     			'<option value="-1">@COMMON.pleaseSelect@...</option>' + 
					'<option value="0">no active</option>' + 
					'<option value="1">active</option>' + 
					'</select>'
		},{
			xtype : 'tbspacer', 
			width : 10
		},{
			xtype : 'button',
			text : '@BUTTON.search@',
			iconCls : 'bmenu_find',
			handler : queryVlanGroup
		},'-',{
			xtype : 'button',
			text : '@igmp.fetchGroup@',
			iconCls : 'bmenu_equipment',
			handler : refreshGroup
		},'-',{
			xtype : 'button',
			text : '@IGMP.importAlias@',
			iconCls : 'bmenu_inport',
			handler : importAlias
		},'-',{
			id : "batchDelete",
			xtype : 'button',
			text : '@igmp.batchDelete@',
			iconCls : 'bmenu_delete',
			handler : function(){
				batchDeleteGroup();
			},
			disabled: true
		}],
		region : 'center',
		stripeRows   : true,
		bbar : buildPagingToolBar(),
//		sm : new Ext.grid.RowSelectionModel({
//			singleSelect : true
//		}),
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	
	store.load({params: {start: 0, limit: pageSize}});

	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});// end document.ready;
function changePreJoin(){
	var $activeStatus = $("#activeStatus"),
	    v = parseInt($("#preJoinSel").val(), 10);
	
	if(v === 1){
		$activeStatus.val('1').attr({disabled : 'disabled'});
	}else{
		$activeStatus.removeAttr('disabled').val("-1");
	}
}
function addFn(){
	openBg();
	createOpenLayer({
		type        : 'add',
		igmpVersion : versionObj[igmpVersion],
		title       : '@igmp.addGroup@',
		desc        : '<p><b class="orangeTxt"> v2 </b>@igmp.tip.tip11@</p>' +
					  '<p><b class="orangeTxt"> v3-only </b>@igmp.tip.tip12@</p>' + 
					  '<p><b class="orangeTxt"> v3 </b>@igmp.tip.tip13@</p>'+
					  '<p>@igmp.tip.tip42@</p>'
	});
	$("#fakeExtWindow .openWinHeader").height(72);
}
function editGroup(){
	openBg();
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		createOpenLayer({
			type        : 'edit',
			igmpVersion : versionObj[igmpVersion],
			title       : '@igmp.editGroup@',
			data        : record.data,
			desc        : '<p><b class="orangeTxt"> v2 </b>@igmp.tip.tip11@</p>' +
						  '<p><b class="orangeTxt"> v3-only </b>@igmp.tip.tip12@</p>' + 
						  '<p><b class="orangeTxt"> v3 </b>@igmp.tip.tip13@</p>'
		});
	}
}
function createOpenLayer(obj){
	openFakeExtWin();
	var tpl = new Ext.XTemplate(
		'<div class="fakeExtWindowTitle"><b>{title}</b><label onclick="closeFakeWindow()"></label></div>',   
		'<div class="openWinHeader">',
	    	'<div class="openWinTip">{desc}</div>',
	    	'<div class="rightCirIco pageCirIco"></div>',
		'</div>',
		'<div class="edge10 pT20">',
			'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
			    '<tbody>',
			    	'<tr>',
			    		'<td class="rightBlueTxt">@igmp.version@:</td>',
			    		'<td colspan="3">{igmpVersion}</td>',
			    	'</tr>',
			        '<tr class="darkZebraTr">',
			            '<td class="rightBlueTxt" width="140">@igmp.vlan@:</td>',
			            '<td width="230"><select {[values.type=="edit" ? "disabled=disabled" : ""]} id="vlanInput" class="w180 normalSel"></select></td>',
			            '<td class="rightBlueTxt" width="140">@igmp.vlanGroupId@:</td>',
			            '<td><input id="groupIdInput" type="text" class="normalInput w180" toolTip="@igmp.tip.tip14@" maxlength="4"></td>',
			        '</tr>',
			        '<tr>',
			            '<td class="rightBlueTxt">@igmp.groupIp@:</td>',
			            '<td id="putAddressIp"></td>',
			            '<td class="rightBlueTxt">@igmp.gourpSourceIp@:</td>',
			            '<td id="putSourceIp"></td>',
			        '</tr>',
			        '<tr class="darkZebraTr">',
			            '<td class="rightBlueTxt">@igmp.vlanGroupDesc@:</td>',
			            '<td><input id="descInput" type="text" class="normalInput w180" toolTip="@igmp.tip.tip15@" maxlength="31"></td>',
			            '<td class="rightBlueTxt">@igmp.vlanGroupAlias@:</td>',
			            '<td><input id="aliasInput" type="text" class="normalInput w180" toolTip="@COMMON.anotherName.br@" maxlength="63"></td>',
			        '</tr>',
			        '<tr>',
				        '<td class="rightBlueTxt">@igmp.maxBw2@:</td>',
			            '<td><input id="maxBwInput" type="text" class="normalInput w180" toolTip="@igmp.tip.tip16@" value="500" maxlength="7"> kbps</td>',
			            '<td class="rightBlueTxt" width="140">@igmp.prejoin@:</td>',
			            '<td>',
			            	'<select class="normalSel w180" id="joinMode">',
			            	'<option value="2">no preJoin</option>',
			            	'<option value="1">preJoin</option>',
			            	'</select>',
			            '</td>',
			        '</tr>',
			    '</tbody>',
			'</table>',
   			'<div class="noWidthCenterOuter clearBoth">',
   		        '<ol class="upChannelListOl pB0 pT20 noWidthCenter">',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="{[values.type==="add"? "addGroup()" : "modifyVlanGroup()"]}"><span><i class="{[values.type==="add" ? "miniIcoAdd" : "miniIcoEdit"]}"></i>{[values.type==="add" ? "@BUTTON.add@" : "@COMMON.edit@"]}</span></a></li>',
   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeFakeWindow()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
   		        '</ol>',
   		     '</div>',
   		 '</div>'
   		);
	var $fakeExtWindow = Ext.get("fakeExtWindow"),
	    aPos = $fakeExtWindow.getCenterXY();
	
	tpl.overwrite($fakeExtWindow, obj);
	$fakeExtWindow.setXY([aPos[0],2]);
	createIpInput();
	if( isCreateSelect === null ){
		loadAllIgmpVlan();
	}else{
		createSel(isCreateSelect);
	}
	if(obj.type === 'edit'){
		var d = obj.data;
		$("#vlanInput").val(d.vlanId);
		$("#groupIdInput").val(d.groupId).attr({disabled : 'disabled'});
		$("#descInput").val(d.groupDesc).attr({disabled : 'disabled'});
		$("#aliasInput").val(d.groupName);
		addressIpV4.setValue(d.groupIp);
		addressIpV4.setDisabled(true);
		sourceIpV4.setValue(d.groupSrcIp);
		sourceIpV4.setDisabled(true);
		$("#maxBwInput").val(d.groupMaxBW);
		$("#joinMode").val(d.joinMode);
	}
	switch(igmpVersion){
	case 2: //v2 版本，不允许存在配置源ip的组播节目,即将源ip输入框设置为disabled，值为0.0.0.0;
		sourceIpV4.setValue("0.0.0.0");
		sourceIpV4.setDisabled(true);
		break;
	}
}
// 创建ip输入框;
function createIpInput(){
	addressIpV4 = new ipV4Input('addressIp','putAddressIp',null);
	sourceIpV4  = new ipV4Input('sourceIp','putSourceIp',null);
	addressIpV4.width(180);
	sourceIpV4.width(180);
}
//
function validateData(){
	// 验证;
	var $descInput    = $("#descInput"),  // 组播组描述;
	    $groupIdInput = $("#groupIdInput"), // 组播组ID;
	    $aliasInput   = $("#aliasInput"), // 组播组别名;
	    $maxBwInput   = $("#maxBwInput"); // 最大带宽;
	
	var flag = customValidateFn([{
		id : 'groupIdInput',
		range : [1,2000]
	},{
		id    : 'maxBwInput',
		range : [0,1000000]
	}]);
	if(flag != true){
		flag.focus();
		return false;
	}
	
	var destReg = /^[a-zA-Z\d-_]{1,31}$/;    
	if( !destReg.test($descInput.val()) ){
		$descInput.focus();
		return false;
	}
	
	//组播组别名，不需要强制添加
	if($aliasInput.val().trim() != "") {
		if( !V.isAnotherName($aliasInput.val().trim()) ){
			$aliasInput.focus();
			return false;
		}
	}
	
	if( !checkDDIsMulticast(addressIpV4.getValue()) ){
		$("#addressIp :text").eq(0).focus();
		return false;
	}
	//V2模式下，组播源IP必须为0.0.0.0
	if(igmpVersion == 2){
		if(sourceIpV4.getValue() != "0.0.0.0"){
			return false;
		}
	}else if(igmpVersion == 3){ //v3可以为0.0.0.0或者是有效在ABC类地址;
		if(sourceIpV4.getValue() != "0.0.0.0"){
			if( !checkIsNomalIp(sourceIpV4.getValue()) ){
				$("#sourceIp :text").eq(0).focus();
				return false;
			}
		}
	}else{
		if( !checkIsNomalIp(sourceIpV4.getValue()) ){
			$("#sourceIp :text").eq(0).focus();
			return false;
		}
	}
	return true;
}

function saveAddGroup(){
	var $descInput    = $("#descInput"),  // 组播组描述;
	    $groupIdInput = $("#groupIdInput"), // 组播组ID;
	    $aliasInput   = $("#aliasInput"), // 组播组别名;
	    $maxBwInput   = $("#maxBwInput"); // 最大带宽;
	
	var groupData = {
		"groupInfo.entityId"   : window.entityId,
		"groupInfo.vlanId"     : $("#vlanInput").val(),
		"groupInfo.groupId"    : $("#groupIdInput").val(),
		"groupInfo.groupIp"    : addressIpV4.getValue(),
		"groupInfo.groupSrcIp" : sourceIpV4.getValue(),
		"groupInfo.groupDesc"  : $descInput.val(),
		"groupInfo.groupMaxBW" : $maxBwInput.val(),
		"groupInfo.groupName"  : $aliasInput.val(),
		"groupInfo.joinMode"   : $("#joinMode").val()
	}
	
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addVlanGroup.tv',
		type : 'POST',
		data : groupData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
					title: "@COMMON.tip@",
					html: '<b class="orangeTxt">@tip.addGroupS@</b>'
				});
			closeFakeWindow();
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addGroupF@");
		},
		cache : false
	});
}

// 添加组播组;
function addGroup(){
	// 验证;
	var validate = validateData();
	if(!validate){
		return;
	}
	//添加时，除了基本的验证，还要验证这个组播组ID是否被使用过;
	loadAllGroupId(function(json){
		if(json.length > 0){
			var id = parseInt($("#groupIdInput").val(), 10);
			for(var i=0,len=json.length; i<len; i++){
				if(json[i] == id){
					var str = String.format('@tip.addGroupF2@',id);
					top.showMessageDlg("@COMMON.tip@", str);
					return;
				}
			}
		}
		saveAddGroup();
	});
}

// 在添加GROUP时选择已经创建的VLAN
// 加载所有的IGMP VLAN数据
function loadAllIgmpVlan(){
	$.ajax({
		url : '/epon/igmpconfig/loadIgmpVlanList.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function(json) {
			if(json.length && json.length > 0){
				isCreateSelect = json;
				createSel(json);
			}
		},
		error : function(json) {
		},
		cache : false,
		async : false
	});
}

function createSel(json){
	var opt = '';
	for(var i=0,len=json.length; i<len; i++){
		opt += String.format('<option value="{0}">{0}</option>', json[i].vlanId);
	}
	$("#vlanInput").html(opt);
}

//修改组播组
function modifyVlanGroup(){
	var validate = validateData();
	if(!validate){
		return;
	}
	var $descInput    = $("#descInput"),  // 组播组描述;
	    $groupIdInput = $("#groupIdInput"), // 组播组ID;
	    $aliasInput   = $("#aliasInput"), // 组播组别名;
	    $maxBwInput   = $("#maxBwInput"); // 最大带宽;
	var groupData = {
		"groupInfo.entityId"   : window.entityId,
		"groupInfo.vlanId"     : $("#vlanInput").val(),
		"groupInfo.groupId"    : $("#groupIdInput").val(),
		"groupInfo.groupIp"    : addressIpV4.getValue(),
		"groupInfo.groupSrcIp" : sourceIpV4.getValue(),
		"groupInfo.groupDesc"  : $descInput.val(),
		"groupInfo.groupMaxBW" : $maxBwInput.val(),
		"groupInfo.groupName"  : $aliasInput.val(),
		"groupInfo.joinMode"   : $("#joinMode").val()
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyVlanGroup.tv',
		type : 'POST',
		data : groupData,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
				title: "@COMMON.tip@",
				html: '<b class="orangeTxt">@tip.editGroupS@</b>'
			});
			closeFakeWindow();
			refreshPage();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.editGroupF@");
		},
		cache : false
	});
}

//加载所有的组播组ID,用于添加时校验组播组ID
function loadAllGroupId(callback){
	$.ajax({
		url : '/epon/igmpconfig/loadAllGroupId.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function(json) {
			callback(json);
		},
		error : function(json) {
			callback([]);
		},
		cache : false,
		async : false
	});
}

//查询组播组信息
function queryVlanGroup(){
	//组装查询条件 
	var queryData = {};
	queryData.joinMode = $("#preJoinSel").val();
	queryData.groupState = $("#activeStatus").val();
	queryData.start = 0;
	queryData.limit = pageSize;
	$.extend(store.baseParams, queryData);
	store.load();
}

function importAlias(){
	top.createDialog("importIgmpGroupAlias", '@IGMP.importAliasTitle@',  800, 550, "/epon/igmpconfig/showIgmpGroupAliasImport.tv?entityId="+entityId, null, true, true,function(){
		store.reload();
	});
}

function importGroups(){
	top.createDialog("batchImportIgmpGroup", '@IGMP.batchImportGroup@',  800, 550, "/epon/igmpconfig/showIgmpGroupImport.tv?entityId="+entityId+"&igmpVersion="+igmpVersion , null, true, true,function(){
		store.reload();
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

//获取选中的组播组
function getSelectedGroups(){
	var sm = grid.getSelectionModel();
	var selectedSections = [];
	var selectedGroups = [];
	if (sm != null && sm.hasSelection()) {
		selectedSections = sm.getSelections();
		selectedSections.forEach(function(value){
			var groupInfo = new Object;
			groupInfo.entityId = window.entityId;
			groupInfo.vlanId=value.data.vlanId,
			groupInfo.groupId=value.data.groupId,
			groupInfo.groupIp=value.data.groupIp,
			groupInfo.groupSrcIp=value.data.groupSrcIp
			selectedGroups.push(groupInfo);
		})
	}
	return selectedGroups;
}