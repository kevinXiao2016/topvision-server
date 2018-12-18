//初始化ONU IGMP配置数据
function initOnuConfig(onuConfig){
	if(onuConfig == null || typeof(onuConfig) == 'undefined'){ //如果没有加载到数据;
		withOutOnuData();
	}else{
		setOnuData(onuConfig);
	}
	
}
//ONU IGMP配置 没有加载到数据;
function withOutOnuData(){
	var err = '<b class="orangeTxt">@onuIgmp.withoutData@</b>';
	$("#onuName").html(err);
	$("#putOnuMode").html(err);
	$("#putOnuFastLeave").html(err);
	$("#onuApplyBtn").attr({disabled:'disabled'});
}
//ONU IGMP配置 加载到了数据;
function setOnuData(onuConfig){
	$("#onuName").text(onuConfig.onuName);
	var obj = {
		onuMode    : onuConfig.onuMode,
		onuModeArr : [{
			text  : 'ctc',
			value : 1
		},{
			text  : 'snooping',
			value : 2
		},{
			text  : 'disabled',
			value : 3
		}]
	}
	var tpl = new Ext.XTemplate(
		'<select class="normalSel w120" id="onuMode">',
			'<tpl for="onuModeArr">',
				'<option value="{value}" {[values.value == parent.onuMode ? "selected=selected" : ""]}>{text}</option>',
		    '</tpl>',
		'</select>'
	);
	tpl.overwrite('putOnuMode', obj);
	
	var obj2 = {
		onuFastLeave    : onuConfig.onuFastLeave,
		onuFastLeaveArr : [{
			text  : 'enable',
			value : 1
		},{
			text  : 'disable',
			value : 2
		}]
	}
	var tpl2 = new Ext.XTemplate(
		'<select class="normalSel w120" id="onuFastLeave">',
			'<tpl for="onuFastLeaveArr">',
				'<option value="{value}" {[values.value == parent.onuFastLeave ? "selected=selected" : ""]}>{text}</option>',
			'</tpl>',
		'</select>'
	);
	tpl2.overwrite('putOnuFastLeave', obj2);
	$("#onuApplyBtn").removeAttr('disabled');	
	
}
//获取ONU IGMP基本配置
function loadOnuIgmpConfig(){
	$.ajax({
		url : '/epon/igmpconfig/loadOnuIgmpConfig.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			onuIndex : onuIndex
		},
		dataType :　'json',
		success : function(json) {
			initOnuConfig(json);
		},
		error : function(json) {
			initOnuConfig(null);
		},
		cache : false
	});	
}

//修改ONU IGMP配置
function modifyOnuIgmpConfig(){
	var onuIgmp = {
		"onuConfig.entityId" : entityId,
		"onuConfig.onuIndex" : onuIndex,
		"onuConfig.onuMode" : $("#onuMode").val(),
		"onuConfig.onuFastLeave" : $("#onuFastLeave").val()
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyOnuIgmpConfig.tv',
		type : 'POST',
		data : onuIgmp,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.saveOnuIgmpS@</b>'
   			});
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.saveOnuIgmpF@");
		},
		cache : false
	});
}

//刷新ONU IGMP配置
function refreshOnuIgmpConfig(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshOnuIgmpConfig.tv',
		type : 'POST',
		data : {
			entityId : entityId, 
			onuIndex : onuIndex
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			//window.location.reload();
			loadOnuIgmpConfig();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//创建UNI端口树
function createTree(uniList){
	$("#tree").empty();
	var tpl = new Ext.XTemplate(
		'<tpl for=".">',
			'<li><a href="javascript:;" class="{[xindex === 1 ? "selectedTree" : ""]} linkBtn" alt="{portIndex}">{portName}</a></li>',
		'</tpl>'
	);
	tpl.overwrite('tree', uniList);
	
	$("#tree").treeview({ 
		animated: "fast"
	});	//end treeview;
	
	//默认选中第一个端口并触发点击事件
	if(uniList.length > 0){
		clickTree(uniList[0].portIndex, uniList[0].portName);
		loadAllCtcProfile();
	}
	
	//树菜单点击事件;
	$("#tree").delegate('a','click',function(){
		$("#tree a").removeClass("selectedTree");
		$(this).addClass("selectedTree");
		var portIndex = $(this).attr("alt"); 
		clickTree(portIndex, $(this).text());
	})
}
function updataPortNumText(str){
	$(".jsPortNum").text(str);
}

//点击UNI端口树结点时执行操作
function clickTree(portIndex, portName){
	//保存当前选择的UNI口索引
	window.currentIndex = portIndex;
	updataPortNumText(portName)
	//获取指定UNI端口IGMP基本配置
	loadUniIgmpConfig(portIndex);
	//获取UNI端口绑定可控模板配置
	loadUniBindProfile(portIndex);
}
//获取指定UNI端口IGMP基本配置
function loadUniIgmpConfig(uniIndex){
	$.ajax({
		url : '/epon/igmpconfig/loadUniIgmpConfig.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			uniIndex : uniIndex
		},
		dataType :　'json',
		success : function(json) {
			initUniConfig(json);
		},
		error : function(json) {
			initUniConfig(null);
		},
		cache : false
	});	
}

//获取UNI端口绑定可控模板配置
function loadUniBindProfile(uniIndex){
	store.setBaseParam('uniIndex', uniIndex);
	store.load();
}

//初始化UNI IGMP配置
function initUniConfig(uniConfig){
	if(uniConfig == null || typeof(uniConfig) == 'undefined'){ //如果没有加载到数据;
		withoutUniData();
	}else{
		setUniData(uniConfig);
	}
}
//没有加载到uni igmp的数据;
function withoutUniData(){
	var err = '<b class="orangeTxt">@onuIgmp.withoutData@</b>';
	$("#putMaxGroupNum").html(err);
	$("#putVlanList").html(err);
	$("#putVlanMode").html(err);
	$("#uniApplyBtn").attr({disabled:'disabled'});
	$("#vlanTransBtn").attr({disabled:'disabled'});
}
function setUniData(uniConfig){
	var inputStr = String.format('<input value="{0}" type="text" maxlength="2" class="normalInput w120" id="maxGroupNum" tooltip="@igmp.tip.tip28@" />', uniConfig.maxGroupNum);
	$("#putMaxGroupNum").html(inputStr);
	var vlanListValue = tansToValue(uniConfig.uniVlanArray);
	
	var vlanListStr = String.format('<input value="{0}" type="text" class="normalInput w120" id="vlanList" tooltip="@igmp.tip.tip29@" />', vlanListValue);
	$("#putVlanList").html(vlanListStr);
	var tpl = new Ext.XTemplate(
		'<select class="normalSel w120" id="vlanMode" >',
			'<tpl for="vlanModeArr">',
				'<option value="{value}" {[values.value == parent.vlanMode ? "selected=selected" : ""]}>{text}</option>',
			'</tpl>',
		'</select>'
	);
	var obj = {
		vlanMode    : uniConfig.vlanMode,
		vlanModeArr : [{
			text  : 'strip',
			value : 1
		},{
			text  : 'translation',
			value : 2
		},{
			text  : 'keep',
			value : 3
		}]
	}
	tpl.overwrite('putVlanMode', obj);
	$("#uniApplyBtn").removeAttr("disabled");
	setVlanTransBtnDisplay();
}
function tansToValue(arr){
	//后台给过来的数组非常奇葩，length不足32位会自动补0,先要去除所有的0;
	for(var i=arr.length-1; i>=0; i--){
		if(arr[i] == 0){
			arr.splice(i,1);
		}
	}
	if(arr.length === 0){
		return "";
	}else{
		var str = convertVlanArrayToAbbr(arr);
		return str;
	}
}
/**
 * 将数组形式的VLAN ID转换成缩略的展示形式
 * @param vlanArray
 * @return {String} 缩略形式字符串
 */
function convertVlanArrayToAbbr(vlanArray){
	if(!vlanArray || vlanArray.length <=1 ){
		return vlanArray;
	}
	var parts = [];
	parts[0] = {};
	parts[0].start = vlanArray[0];
	parts[0].end = vlanArray[0];
	
	var curPartIndex=0;
	for(var i=1, len=vlanArray.length; i<len; i++){
		if(vlanArray[i] - vlanArray[i-1] == 1){
			//连续
			parts[curPartIndex].end = vlanArray[i];
		}else{
			//不连续
			++curPartIndex
			parts[curPartIndex] = {};
			parts[curPartIndex].start = vlanArray[i];
			parts[curPartIndex].end = vlanArray[i];
		}
	}
	var outArr = [];
	for(var i=0; i<=curPartIndex; i++){
		if(parts[i].start == parts[i].end){
			outArr.push(parts[i].start);
		}else{
			outArr.push(parts[i].start+'-'+parts[i].end);
		}
	}
	return outArr.join(',');
}

//根据VLAN模式select的值，显示或隐藏VLAN转换配置的值;
//只有在UniVlanMode 为translation模式下才能进行配置;
function setVlanTransBtnDisplay(){
	var $vlanTransBtn = $("#vlanTransBtn");
	if($("#vlanMode").length == 1 && $("#vlanMode").val() == 2){
		$vlanTransBtn.removeAttr('disabled');
	}else{
		$vlanTransBtn.attr({disabled: 'disabled'});
	}
}

//检查UNI VLAN序列输入
function checkVlanInput(v){
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
    if (reg.exec(v)) {
    	var tmp = v.replace(new RegExp('-', 'g'), ',');
    	var tmpA = tmp.split(',');
    	var tmpAl = tmpA.length;
		for(var i=0; i<tmpAl; i++){
			if(parseInt(tmpA[i]) > 4094 || parseInt(tmpA[i]) < 1){
				return false;
			}
		}
    	return true;
    }
    return false;
}
//解析逗号和连字符组成的字符串为数组
function changeToArray(v){
	var re = new Array();
	var t = v.split(",");
	var tl = t.length;
	for(var i=0; i<tl; i++){
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if(ttI > 0){
			var ttt = tt.split("-");
			if(ttt.length == 2){
				var low = parseInt(parseInt(ttt[0]) > parseInt(ttt[1]) ? ttt[1] : ttt[0]);
				var tttl = Math.abs(parseInt(ttt[0]) - parseInt(ttt[1]));
				for(var u=0; u<tttl + 1; u++){
					re.push(low + u);
				}
			}
		}else if(ttI == -1){
			re.push(parseInt(tt));
		}
	}
	var rel = re.length;
	if(rel > 1){
		var o = {};
		for(var k=0; k<rel; k++){
			o[re[k]] = true;
		}
		re = new Array();
		for(var x in o){
			if (x > 0 && o.hasOwnProperty(x)) {
				re.push(parseInt(x)); 
			} 
		}
		re.sort(function(a, b){
			return a - b;
		});
	}
	return re;
}

//修改UNI口IGMP基本配置
function modifyUniIgmpConfig(){
	var $maxGroupNum   = $("#maxGroupNum"),
	    maxGroupNumVal = $maxGroupNum.val(), 
	    numReg         = /^\d{1,2}$/,
	    flag           = false;
	
	if( numReg.test(maxGroupNumVal) ){
		if(maxGroupNumVal >= 0 && maxGroupNumVal <= 64){
			flag = true;
		}
	}
	if(!flag){
		$maxGroupNum.focus();
		return;
	}
	
	var vlanInput = $("#vlanList").val();
	var uniVlanArray = [];
	//VLAN序列可以不用配置(或者取消VLAN序列配置)
	if(vlanInput != ''){
		if(!checkVlanInput(vlanInput)){
			$("#vlanList").focus();
			return;
		}
		uniVlanArray = changeToArray(vlanInput);
		if(uniVlanArray.length > 32){
			$("#vlanList").focus();
			return;
		}
	}
	var uniIgmp = {
		"uniConfig.entityId"    : entityId,
		"uniConfig.uniIndex"    : window.currentIndex,
		"uniConfig.maxGroupNum" : maxGroupNumVal,
		"uniConfig.vlanMode"    : $("#vlanMode").val(),
		"uniConfig.uniVlanArray": uniVlanArray
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/modifyUniIgmpConfig.tv',
		type : 'POST',
		data : uniIgmp,
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.saveUniIgmpConfigS@</b>'
   			});
			setVlanTransBtnDisplay();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.saveUniIgmpConfigF@");
		},
		cache : false
	});
}

//考虑刷新后页面数据的更新问题
//刷新UNI端口IGMP配置
function refreshUniIgmpConfig(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshUniIgmpConfig.tv',
		type : 'POST',
		data : {
			entityId : entityId, 
			uniIndex : window.currentIndex
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			loadUniIgmpConfig(currentIndex);
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//控制只有在VLANMODE为translate时显示
//显示UNI端口VLAN转换页面配置
function showUniVlanTrans(){
	var uniName = $("#tree .selectedTree").text();
	top.createDialog('uniVlanTrans', '@onuIgmp.vlanTransConfig@', 800, 500, "/epon/igmpconfig/showUniVlanTrans.tv?entityId="+entityId+"&uniIndex="+window.currentIndex + "&uniName=" + uniName, null, true, true);
}

//UNI端口绑定可控组播模板列表栏操作
function manuRender(value, meta, record){
	var uniIndex = record.data.uniIndex;
	var profileId = record.data.profileId;
	return String.format("<a href='javascript:;' onClick='deleteBindProfile({0},{1})'>@COMMON.delete@</a>",uniIndex,profileId);  
}

//删除UNI端口绑定可控组播模板
function deleteBindProfile(uniIndex, profileId){
	top.showConfirmDlg("@COMMON.tip@", "@confirm.deleteBindProfile@", function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/igmpconfig/deleteUniBindProfile.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				profileId : profileId
			},
			dataType :　'json',
			success : function() {
				top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: '<b class="orangeTxt">@tip.deleteBindProfileS@</b>'
       			});
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@tip.deleteBindProfileF@");
			},
			cache : false
		});
	});
}

//添加绑定可控组播模板
function adddUniBindProfile(){
	var uniIndex = window.currentIndex;
	var profileId = $("#profileId").val();
	if(profileId === null){
		top.showMessageDlg("@COMMON.tip@", '@tip.profileWithoutData@');
		return;
	}
	//对比store,已经添加过的，不能再添加;
	if(store.data.items.length > 0){
		for(var i=0,len=store.data.items.length; i<len; i++){
			if(store.data.items[i].json.profileId == profileId){
				var str = "@tip.profileAleadyHave@";
				top.showMessageDlg("@COMMON.tip@", str);
				return;
			}
		}
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	$.ajax({
		url : '/epon/igmpconfig/addUniBindProfile.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			uniIndex : uniIndex,
			profileId : profileId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@tip.addBindProfileS@</b>'
   			});
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@tip.addBindProfileF@");
		},
		cache : false
	});
}

//刷新UNI端口绑定可控组播模板
function refreshUniBindProfile(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshUniBindProfile.tv',
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
			store.reload();
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//在添加UNI口绑定可控组播模板时使用
//加载所有的可控组播模板
function loadAllCtcProfile(){
	$.ajax({
		url : '/epon/igmpconfig/loadAllCtcProfile.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function(json) {
			createProfileSelectOpt(json);
		},
		error : function(json) {
			
		},
		cache : false
	});
};
function createProfileSelectOpt(data){
	$("#profileId").empty();
	/*var tpl = new Ext.XTemplate(
		'<tpl for=".">',
			'<option value="{profileId}">{profileDesc}</option>',
		'</tpl>'
	);*/
	var opt = '';
	for(var i=0,len=data.length; i<len; i++){
		opt += String.format('<option value="{0}">(ID:{0}) {1}</option>', data[i].profileId, data[i].profileDesc);
	}
	$("#profileId").html(opt);
}

$(function(){
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmp.portNumber@', dataIndex: 'uniName'},
		    {header: '@igmp.controlProfileId@', dataIndex: 'profileId'},
		    {header: '@igmp.controlProfileDesc@', dataIndex: 'profileDesc'},
		    {header: '@COMMON.manu@', dataIndex: "op", width: 120, fixed:true, renderer : manuRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadUniBindProfileList.tv',
		fields : ["entityId","uniIndex","uniName", "portType","profileId","profileDesc"],
		baseParams : {
			entityId : entityId
		}
	});
	grid = new Ext.grid.GridPanel({
		cm      : cm,
		title   : '@onuIgmp.uniPortBindPro@',
		store   : store,
		margins : '0 10 10 10',
		tbar    : [{
			xtype : 'tbspacer', 
			width : 10
		},{
			cls   :'blueTxt',
			text  : 'UNI@igmp.portNumber@:',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5 jsPortNum',
			xtype : 'component',
	     	html  : ''
		},{
			xtype : 'tbspacer', 
			width : 20
		},{
			cls   :'blueTxt',
			text  : '@igmp.controlProfile@:',
			xtype : 'tbtext' 
		},{
			cls   : 'mT5 mB5',
			xtype : 'component',
	     	html  : '<select class="w160 normalSel" id="profileId"></select>'
		},{
			xtype : 'tbspacer', 
			width : 10
		},{
			xtype : 'button',
			text : '@BUTTON.add@',
			iconCls : 'bmenu_new',
			handler : adddUniBindProfile
		},'-',{
			xtype : 'button',
			text : '@COMMON.fetch@',
			iconCls : 'bmenu_equipment',
			handler : function(){
				loadAllCtcProfile();
				refreshUniBindProfile();
			}
		}],
		cls          : 'normalTable',
		region       : 'center',
		stripeRows   : true,
		viewConfig   : { forceFit:true}
	});
	
	new Ext.Viewport({
		border : false,
		layout : 'border',
		items  : [{
			split     : false,
			border    : false,
			region    : 'north',
			height    : 161,
			contentEl : 'topPart'	
		},{
			border  : true,
			region  : 'center',
			layout  : 'border',
			margins : '0 10 10 10',
			items   : [{
				cls    : 'eastBg',
				width  : 140,
				border : false,
				region : 'west',
				contentEl  : 'leftPart',
				autoScroll : true
			},{
				cls    : 'lightGrayBg',
				border : false,
				region : 'center',
				layout : 'border',
				items  : [{
					height : 156,
					border : false,
					region : 'north',
					contentEl  : 'middlePart',
					autoScroll : true
				},grid]
			}]
		}]
	});//end viewport;
	
	//初始化ONU IGMP配置
	loadOnuIgmpConfig();
	//创建UNI端口树,默认选中第一个端口并加载对应配置数据
	createTree(uniListJson);
	
});//end document.ready;