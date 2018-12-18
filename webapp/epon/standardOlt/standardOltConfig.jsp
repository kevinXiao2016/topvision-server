<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    import js.tools.ipText
    import epon.javascript.OltConfigInfo
    import epon.javascript.InbandVlanComboBox
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	.x-combo-list-item{ text-overflow: clip; white-space:normal; word-break:break-all; word-wrap:break-word; border-bottom:1px dotted #ccc; padding:4px 2px;}
	span.normalInputDisabled{ border:none;}
</style>
<script type="text/javascript">
</script>
<script type="text/javascript" defer>
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var entityId = '${entityId}',
    snmpVersion = '${snmpParam.version}',
    entitySnmpVersion = '${oltAttribute.topSysSnmpVersion}',
    oltName = '${oltAttribute.oltName}',
    entityContact = '${entity.sysContact}',
    name = '${entity.name}',
    roomName = '${entity.sysLocation}',
    //rackNum = '${oltAttribute.topSysOltRackNum}',
    //frameNum = '${oltAttribute.topSysOltFrameNum}',
    note = '${entity.note}',
	emsIpAddress = '${entity.ip}',
	emsReadCommunity = "${snmpParam.community}",           //注意,读写共同体，支持特殊字符，这里只能用双引号.双引号已经转义为&quot;单引号不转义;
	emsWriteCommunity = "${snmpParam.writeCommunity}",     //同上;
	securityLevel = '${snmpParam.securityLevel}',
	authProtocol = '${snmpParam.authProtocol}',
	username = '${snmpParam.username}',
	authPassword = '${snmpParam.authPassword}',
	privProtocol = '${snmpParam.privProtocol}',
	privPassword = '${snmpParam.privPassword}',
	EMPTY_IP_MASK_ADDR = "0.0.0.0";
	

/*************************************************
 	@never used  fixed at V2 
 ************************************************/
function emsSnmpChanged(obj) {
    switch (Zeta$('emsSnmpVersion').value) {
        case '0':
        case '1':
            $(".v2setting").show();
            $(".v3setting").hide();
            break;
        case '3':
        	$(".v3setting").show();
            $(".v2setting").hide();
            break;
    }
}

function authProtocolChanged() {
	var authProtocol = Zeta$('authProtocol').value;
	if(authProtocol == 'NOAUTH'){
		$('#privProtocol').val('NOPRIV');
		$('#privProtocol').attr("disabled",true);
		$("#authPassword").attr("disabled",true);
		$("#privPassword").attr("disabled",true);
	}else{
		$('#privProtocol').attr("disabled",false);
		$("#authPassword").attr("disabled",false);
		$("#privPassword").attr("disabled",false);
	}
}

/************************************************
 		DEFINTITION EXECUTOR CLASS
*************************************************/
var Executor = new Function

// basic
Executor.prototype.oltName
Executor.prototype.entityContact
Executor.prototype.alias
Executor.prototype.location 
Executor.prototype.rack
Executor.prototype.frame
Executor.prototype.frameEnd
Executor.prototype.note
// server snmp
Executor.prototype.emsIpAddress
Executor.prototype.serverSnmpVersion
Executor.prototype.readCommunity
Executor.prototype.writeCommunity
//V3 params
Executor.prototype.securityLevel
Executor.prototype.authProtocol
Executor.prototype.username
Executor.prototype.authPassword
Executor.prototype.privProtocol
Executor.prototype.privPassword
// vectors
Executor.prototype.modifiedVecotr = new Array
Executor.prototype.processResults = new Array

/************************************************
 		initialize all ip filed object
*************************************************/
Executor.prototype.init_ipFields = function (o){
	for(var i=0 ; i<o.length ; i++){
		new ipV4Input(o[i].elementId,o[i].innerId).setValue(o[i].defaultValue);
	}
	setIpWidth("all", 180);
}

/************************************************
	initialize all selected elements
*************************************************/
Executor.prototype.init_selectors = function() {
    $('#emsSnmpVersion').val('${snmpParam.version}');
    emsSnmpChanged();

    var e2 = Zeta$('authProtocol');
    for (var i = 0; i < e2.options.length; i++) {
        if (e2.options[i].value == '${snmpParam.authProtocol}') {
            e2.selectedIndex = i;
            break;
        }
    }

    var e3 = Zeta$('privProtocol');
    for (var i = 0; i < e3.options.length; i++) {
        if (e3.options[i].value == '${snmpParam.privProtocol}') {
            e3.selectedIndex = i;
            break;
        }
    }
    authProtocolChanged();
}

/*******************************************
 		SEND AJAX REQUEST
 *****************************************/
Executor.prototype.request = function(url,data,successFn,errorHandler,isAsync){
	try{
		$.ajax({
			url : url, 
			cache : false, 
			method: 'post' ,// dataType : 'json',
			data : data,
			success : successFn, 
			async:!isAsync,
			error : function(e){
				if(typeof errorHandler == 'string')
					executor.request_error(errorHandler)
				else
					errorHandler.apply(window,[e])
			}
		})
	} catch(e){}
}


Executor.prototype.request_error = function(message){
	window.parent.showMessageDlg(I18N.COMMON.message, message);
}

var Result = new Function
var result = new Result

/******************************************************
		call back when nochange config success
******************************************************/
Result.prototype.saveNochangeConfigSuccess = function(json){
	 var flag = 0;
     if ( json.message ) {
         window.parent.showMessageDlg(I18N.COMMON.message, json.message);
     } else if (inbandParamModifiedFlag > 0 || outbandParamModifiedFlag > 0) {
         flag = 1;
         window.top.closeWaitingDlg();
         window.top.createDialog('modifyEmsIp', I18N.config.page.emsParamModify, 310, 150, '/config/showmModifyEmsIpJSP.tv?entityId=' + entityId + '&inbandIp=' + inbandIpInput + '&outbandIp=' + outbandIpInput + '&entityIp=' + emsIpAddressInput +
                '&snmpCommunity=' + snmpCommunity + '&snmpWriteCommunity=' + snmpWriteCommunity, null, true, true);
     }
     else if (inbandParamModifiedFlag == 0 && outbandParamModifiedFlag == 0) {
    	 top.afterSaveOrDelete({
			title: I18N.COMMON.tip,
			html: I18N.config.page.modifySuccess
		});
         //window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.modifySuccess);
     }
     if (flag == 0) {
         window.location.reload();
     }
}

/******************************************************
		call back when basicInfo config success
******************************************************/

Result.prototype.saveBasicInfoSuccess = function(json){
	//json = Ext.decode(json)
	if(json == "success")
		executor.processResults.push("basicInfo")
	else
		executor.processResults.push("ConfigError"+Math.random())
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
	}
}


Result.prototype.saveBasicConfigSuccess = function(json){
	//json = Ext.decode(json)
	if(json.message == "ok")
		executor.processResults.push("basicSegment")
	else
		executor.processResults.push("ConfigError"+Math.random())
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
	}
}

/******************************************************
		call back when basic config success
******************************************************/
Result.prototype.saveServerSnmpConfigSuccess = function(json){
	if(json == 'success'){
		executor.processResults.push("serverSnmpSegment")
		if(executor.processResults.length == executor.modifiedVecotr.length){
			executor.showSaveResult()
		}
	}else if(json == 'ipExistDevice'){
		window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipExistDevice@");
	}else if(json == 'ipExistMore'){
		window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipExistMore@");
	}
	
}

/******************************************************
		request error/failure
******************************************************/
Result.prototype.emptyFn = function(){
	executor.processResults.push("ConfigError"+Math.random())
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
	}
}

/******************************************************
		show all request's result
******************************************************/
Executor.prototype.showSaveResult = function(){
	window.top.closeWaitingDlg()
	var result = "",
		isModifiedIO = false;
	while(executor.modifiedVecotr.length > 0) {
		var sendItem = executor.modifiedVecotr.pop()
		switch(sendItem) {
			case "basicInfo":
				if(executor.processResults.indexOf(sendItem) != -1)
					result += "<div>"+ I18N.config.basicInfo.setBasicSuccess +"</div>"
				else
					result += "<div style='color:red'>"+I18N.config.basicInfo.setBasicFail+"</div>"
				break
			case "basicSegment":
				if(executor.processResults.indexOf(sendItem) != -1)
					result += "<div>"+ I18N.config.basicInfo.setSuccess +"</div>"
				else
					result += "<div style='color:red'>"+I18N.config.basicInfo.setFail+"</div>"
				break
			case "serverSnmpSegment":
				if(executor.processResults.indexOf(sendItem) != -1){
					result += "<div>"+I18N.config.emsSnmpInfo.setSuccess+"</div>"
					//window.location.reload();
				}else
					result += "<div style='color:red'>"+I18N.config.emsSnmpInfo.setFail+"</div>"
				break
		}
	}
	window.parent.showMessageDlg(I18N.COMMON.message, result || I18N.config.page.noChange,"tip",function(){
		if(isModifiedIO){
			window.top.showMessageDlg("@COMMON.tip@", "@config.page.ipModifyTip@");
		}
		window.location.reload();
	})
}

/******************************************************
		 basicInfo modified 
******************************************************/
Executor.prototype.check_basicInfo = function(){
	if(emsIpAddress == this.emsIpAddress  && name == this.alias && note == this.note)
		return true
	else
		this.modifiedVecotr.push("basicInfo")
	if (this.emsIpAddress  && !ipIsFilled("emsIpAddress")){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputEmsIp)  &&  Zeta$("emsIpAddress").focus()  &&   false
	}
    if (!ipCheck(this.emsIpAddress)){  //add by flackyang 2013-09-10
    	window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.emsIpAddressCheck);
    	return false;
    }
    if( !Validator.isAnotherName(this.alias) ){	
		return Zeta$("name").focus()  &&   false
	}
	//增加对Ip修改的限制与
	var data = new 	Object
	    data.emsIpAddress = this.emsIpAddress
	var url = '/config/checkEmsIpAddress.tv'
	var isEmsIpAddressConflict = false;
	if(emsIpAddress != this.emsIpAddress){
		executor.request(url,data,function(json){
			if(json == 'exist'){
				isEmsIpAddressConflict = true;
			}
	     },null,true);
	}
	if (!this.alias) {
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputAlias)  &&  false
	}
	return true
}

Executor.prototype.check_basic = function(){
	//比较一下,当前模块是否有改动
	if(oltName == this.oltName && entityContact == this.entityContact
			&&  roomName == this.location)
		return true
	else
		this.modifiedVecotr.push("basicSegment")
	
	//设备名称与联系人非中文验证
	var reg = /^[a-zA-Z0-9-\s_:;\/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/
	if ( this.location && !reg.test(this.location)) {
    	return Zeta$("location").focus() && false
    }
    if ( this.entityContact && !reg.test(this.entityContact)) {
    	return Zeta$("entityContact").focus() && false
    }
    if(!Validator.isAnotherName($("#oltName").val())){
    	return Zeta$("oltName").focus() && false;
    }
 
  	//数据非空验证
    if ( !this.oltName) 
       	return  window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputName)  &&  false
	return true
}



/******************************************************
		 server snmp segment modified
******************************************************/
Executor.prototype.check_server_snmp = function(){
	if(snmpVersion == this.serverSnmpVersion && emsReadCommunity == this.readCommunity && emsWriteCommunity == this.writeCommunity
			&& authProtocol == this.authProtocol && username == this.username
			&& authPassword == this.authPassword && privProtocol == this.privProtocol && privPassword == this.privPassword )
		return true
	else
		this.modifiedVecotr.push("serverSnmpSegment")
        
	//var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/
	//在V2C下判断共同体输入合法性
	if(this.serverSnmpVersion != 3){
		if ( this.readCommunity == "" || !V.isSnmpCommunity( this.readCommunity )) {
	    	return Zeta$("community").focus() && false
	    }
		if ( this.writeCommunity == "" || !V.isSnmpCommunity( this.writeCommunity )) {
	    	return Zeta$("writeCommunity").focus() && false
	    }
	}
	//在V3模式下判断用户名密码输入合法性
	if(this.serverSnmpVersion == 3){
		if ( this.username == "" || !V.isName( this.username ) ) {
	    	return Zeta$("username").focus() && false
	    }
		if ( this.authPassword == "" || !V.isName( this.authPassword ) || this.authPassword.length < 8) {
	    	return Zeta$("authPassword").focus() && false
	    }
		if ( this.privPassword == "" || !V.isName( this.privPassword ) || this.privPassword.length < 8) {
	    	return Zeta$("privPassword").focus() && false
	    }
	}
	return true
	//TODO community is not nessary?
}


/******************************************************
验证输入的IP不能为广播地址，多播地址 add by flackyang 2013-09-10
*******************************************************/
function ipCheck(ip){
	//验证是否为多播,多播地址范围为224.0.0.0～239.255.255.255
	reg = /(\d{1,3})/;
	var ipPart1 = reg.exec(ip)[0];
	if (224 <= ipPart1 && ipPart1 < 240) {
		return false;
	}
	//验证是否为广播
	if (ip == "255.255.255.255") {
		return false;
	}
	return true;
}

/****************************************
			SAVE CONFIG
****************************************/
Executor.prototype.saveConfig = function(){
	executor.processResults = new Array
	executor.modifiedVecotr = new Array
	
	//对别名进行验证;
	var name = $('#name').val().trim();
	var eqName = $('#name').val();
	
	if( !Validator.isAnotherName(eqName) ){
		$("#name").focus();
		return;
	}
	
	// basic
	this.oltName = Zeta$("oltName").value.replace(/\s/g,"")
	//校验oltName是否为中文字符
	if(!checkOltName(oltName)){
		$("#oltName").focus();
		return;
	}
	this.entityContact = Zeta$("entityContact").value
	this.alias = $('#name').val().replace(/\s/g,"")
	this.location = $("#location").val()
	if(!checkOltName(location)){
		$("#location").focus();
		return;
	}
	this.desc = $("#desc").val()
	this.note = $("#note").val().trim();
	// server snmp
	this.emsIpAddress =  getIpValue("emsIpAddress")
	this.serverSnmpVersion = $("#emsSnmpVersion").val()
	this.readCommunity = $("#community").val()
	this.writeCommunity =  $("#writeCommunity").val()
	//this.securityLevel = $("#securityLevel").val()
	this.authProtocol = $("#authProtocol").val()
	this.username = $("#username").val()
	this.authPassword = $("#authPassword").val()
	this.privProtocol = $("#privProtocol").val()
	this.privPassword = $("#privPassword").val()
	
	///   BEFORE SAVE TO SERVER
	if(!this.check_basicInfo()) return
	// basic 
	if(!this.check_basic()) return
	// server snmp 
	if(!this.check_server_snmp()) return

	//FIXME 没有任何改动的时候全部下发下去，有改动的时候却只下发某一些改变，这个是否合理
	//FIXME 如果修改了网关,需要在所有设置之前进行提示
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.config.page.setting)
	if(this.modifiedVecotr.length == 0)
		if(executor.save_nochange_config())  return
	if(this.modifiedVecotr.indexOf("basicInfo") != -1)
		executor.save_basicInfo_config()
	if(this.modifiedVecotr.indexOf("basicSegment") != -1)
		executor.save_basic_config();
	if(this.modifiedVecotr.indexOf("serverSnmpSegment") != -1)
		executor.save_server_snmp_config()
}


// DEPRACEATED 不合理，为啥只是下发了 ip
// 如果没有做任何修改,仅仅只是把当前配置下下去
Executor.prototype.save_nochange_config = function(){
	return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.noChange)
    if ( !this.emsIpAddress || !ipIsFilled("emsIpAddress"))
        return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputEmsIp)  &&   Zeta$("emsIpAddress").focus()
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.config.page.setting);
    var data = new 	Object
	    data.entityId = entityId
	    data.ip = emsIpAddress
	var url = '/epon/updateEmsSnmpAttribute.tv'
    executor.request(url,data,result.saveNochangeConfigSuccess,I18N.config.page.setError)
    return
}

Executor.prototype.save_basicInfo_config = function(){
	var data = new Object
	data.entityId = entityId;
	data.emsIpAddress = this.emsIpAddress;
	data.alias = this.alias;
	data.note = this.note;
	data.oldIp = emsIpAddress;
	var tip = '';
	if(emsIpAddress == this.emsIpAddress){
		tip = '@config.basicInfo.modifyServerInfoTip@';
	}else{
		tip = '@config.basicInfo.modifyServerIpTip@';
	}
	var url = "/config/saveServerInfo.tv"
	// send request
	window.parent.showConfirmDlg("@COMMON.tip@",tip, function(type) {
         if (type == 'no'){return;}
         executor.request(url,data,result.saveBasicInfoSuccess,result.emptyFn) 
	})
	
}

//设备基本信息
Executor.prototype.save_basic_config = function(){
    // request params
    var data = new Object
    	data.entityId = entityId
	    data.oltName = this.oltName
	    data.entityContact = this.entityContact
	    //data.alias = this.alias
	    data.location = this.location
	    data.rack = this.rack || 0
	    data.frame = this.frame || 0
	    data.desc = this.desc
	    //data.note = this.note
	var url = "/config/saveBasicConfig.tv"
	//send request
	executor.request(url,data,result.saveBasicConfigSuccess,result.emptyFn)
}

//网管snmp信息
Executor.prototype.save_server_snmp_config = function(){
	var data = new Object
	data.entityId = entityId;
	data.serverSnmpVersion = $("#emsSnmpVersion").val();
	//V2C模式下更新参数
	if($("#emsSnmpVersion").val() == 1){
		data.readCommunity = $("#community").val()
		data.writeCommunity =  $("#writeCommunity").val()
	}
	//V3模式下更新参数
	if($("#emsSnmpVersion").val() == 3){
		//data.securityLevel = $("#securityLevel").val()
		data.authProtocol = $("#authProtocol").val()
		data.username = $("#username").val()
		data.authPassword = $("#authPassword").val()
		data.privProtocol = $("#privProtocol").val()
		data.privPassword = $("#privPassword").val()
	}
	var tip = '@config.basicInfo.modifyServerSNMPTip@';
	var url = "/config/saveServerSnmp.tv"
	// send request
	window.parent.showConfirmDlg("@COMMON.tip@",tip, function(type) {
         if (type == 'no'){return;}
         executor.request(url,data,result.saveServerSnmpConfigSuccess,result.emptyFn) 
	})
	
}

/*****************************************
 		APPLICATION ENTRY
 *****************************************/
Ext.onReady(initialize);
function initialize() {
    var ipFiledVector =[
		//--------------  ems ip -----------------//
		{elementId : 'emsIpAddress',innerId : 'span9' ,defaultValue : emsIpAddress}
		//--------------  dol ip -----------------//
	]
	// new executor 
	window.executor = new Executor
	// initailize ipfileds
	executor.init_ipFields(ipFiledVector)
	//setIpEnable('emsIpAddress',false);
    //-------  ????  -------//
    $(".modifiedFlag").change();
    //------- init selectors -------- //
    executor.init_selectors()
    
    //设备操作权限
    if(!operationDevicePower){
	    //设备基本信息
	    $("#oltName").attr("disabled",true);
	    $("#entityContact").attr("disabled",true);
	    $("#location").attr("disabled",true);
	    
	    R.modifyBt.setDisabled(true);
	}
}

function snmpVersionChanged(el){
	$("#emsSnmpVersion").val(el.value);
}
function beizhuKeyup(el){
	el.value = el.value.slice(0, 128);
}

function refreshConfigInfo(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/config/refreshOltConfigInfo.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.href = window.location.href;
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}
</script>
</head>
<body class="whiteToBlack">
<div class=formtip id=tips style="display: none"></div>
<div id=header >
    <%@ include file="/epon/inc/navigator_standardOlt.inc"%>
</div>

<div id="putTab" class="edge10"></div>

<div class="edge10 pB0 clearBoth tabBody" id="basic" style="pading-top: 15px;">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.basicInfo.textEms@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width=120 class="rightBlueTxt">@config.emsSnmpInfo.ip@:</td>
				<td><span id="span9" style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt"><label for="name">@config.basicInfo.alias@:
						<font color=red>*</font>
				</label></td>
				<td height=20><input style="width: 180px" id="name"
					class="normalInput modifiedFlag" value="${entity.name}" maxlength="63"
					tooltip="@COMMON.anotherName@" /></td>

				<td width=120 class="rightBlueTxt">@config.basicInfo.beizhu@:</td>
				<td><textarea id="note" class="normalInput modifiedFlag" rows="2"
						style="width: 180px; overflow: auto" maxlength="128"
						tooltip="@config.page.noteTip@">${entity.note}</textarea></td>
			</tr>
		</tbody>
	</table>
</div>

<!-- BASIC SEGMENT -->
<div class="edge10 pB0 clearBoth tabBody" id="basicSegment" style="pading-top: 15px;">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.basicInfo.text@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width=120 class="rightBlueTxt"><label for="oltName">@config.basicInfo.name@:
					<font color="red">*</font>
				</label></td>
				<td><input class="normalInput modifiedFlag" style="width: 180px"
					id="oltName" maxlength="63" value="${oltAttribute.oltName}"
					tooltip="@config.page.nameTip@" /></td>

				<td width=120 class="rightBlueTxt">@config.basicInfo.contactUser@:</td>
				<td><input class="normalInput modifiedFlag" id="entityContact"
					style="width: 180px;" value="${entity.sysContact}"
					tooltip="@config.page.contactUserTip@" /></td>
				<td width=120 class="rightBlueTxt">@config.basicInfo.addr@:</td>
				<td><input style="width: 180px" id="location"
					class="normalInput modifiedFlag" value="${entity.sysLocation}"
					maxlength="64" tooltip="@config.page.sysLocation@" /></td>
			</tr>
			<tr>
				<td width=120 class="rightBlueTxt">@config.basicInfo.desc@:</td>
				<td colspan="5"><textarea id=desc name="entity.sysDescr"
						onfocus='$(this).blur()' class="normalInput modifiedFlag normalInputDisabled" rows=2 disabled
						style="width: 180px; overflow: auto">${entity.sysDescr}</textarea>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<!-- SERVER SNMP SEGMENT -->
<div class="edge10 pB0 clearBoth tabBody" id="serverSNMPSegment">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.emsSnmpInfo.text@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width=120 class="rightBlueTxt">@config.emsSnmpInfo.version@:</td>
				<td><select id="emsSnmpVersion" class="normalSel"
					onchange="emsSnmpChanged();snmpVersionChanged(this)"
					style="width: 180px">
						<option value="1">SNMP V2C</option>
						<option value="3">SNMP V3</option>
						<!-- <option value="4">ALL</option> -->
				</select></td>
				<td id="emsV2Setting1" class="v2setting rightBlueTxt" width=120 class="rightBlueTxt">@config.emsSnmpInfo.readCommunity@:</td>
				<td id="emsV2Setting2" class=v2setting><Zeta:Password
						width="180" id="community" value="${snmpParam.community}"
						maxlength="31" tooltip="@config.page.communityTip2@" /></td>
				<td id="emsV2Setting3" class="v2setting rightBlueTxt" width=120>@config.emsSnmpInfo.writeCommunity@:</td>
				<td id="emsV2Setting4" class="v2setting"><Zeta:Password
						width="180" id="writeCommunity"
						value="${snmpParam.writeCommunity}" maxlength="31"
						tooltip="@config.page.communityTip2@" /></td>
				<td style='display: none;' id="emsV3setting5" class="v3setting rightBlueTxt" width=120>@config.dol.user@:</td>
				<td style='display: none;' id="emsV3setting6" class="v3setting">
					<input style="width: 180px" maxlength="31" class="normalInput"
					id="username" tooltip="@config.page.usernameTip@"
					value="${snmpParam.username}" />
				</td>
				<td style='display: none;' id="emsV3setting3" 
					width=120 class="v3setting rightBlueTxt">@config.dol.check@:</td>
				<td style='display: none;' id="emsV3setting4" class=v3setting>
					<select id="authProtocol" class="normalSel"
					onchange="authProtocolChanged();" style="width: 180px">
						<option value="NOAUTH">NOAUTH</option>
						<option value="MD5">MD5</option>
						<option value="SHA">SHA</option>
				</select>
				</td>
			</tr>
			<tr id="emsV3setting7" style='display: none;' class=v3setting>
				<td style='display: none;' id="emsV3setting1"
					width=120 class="v3setting rightBlueTxt">@config.dol.pwd@:</td>
				<td id="emsV3setting2" class=v3setting style='display: none;'><Zeta:Password
						width="180" id="authPassword"  maxlength="31"
						tooltip="@config.page.authPwdTip@"
						value="${snmpParam.authPassword}" /></td>
				<td class="rightBlueTxt" width=120>@config.dol.private@:</td>
				<td><select id="privProtocol" class="normalSel" style="width: 180px">
						<option value="NOPRIV">NOPRIV</option>
						<option value="CBC-DES">CBC-DES</option>
				</select></td>
				<td class="rightBlueTxt" width=120>@config.dol.privatePwd@:</td>
				<td><Zeta:Password width="180" id="privPassword"  maxlength="31"
						tooltip="@config.page.priPwdTip@"
						value="${snmpParam.privPassword}" /></td>
			</tr>
		</tbody>
	</table>
</div>
<Zeta:ButtonGroup>
	<Zeta:Button id="refreshBt" onClick="refreshConfigInfo()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
	<Zeta:Button id="modifyBt" onClick="executor.saveConfig()" icon="miniIcoEdit">@config.page.update@</Zeta:Button>
</Zeta:ButtonGroup>
	<script type="text/javascript" src="/js/jquery/Nm3kTabBtn.js"></script>
	<script type="text/javascript">
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putTab",
	    callBack:"msg",
	    tabs:["@COMMON.all@","@config.basicInfo.textEms@","@config.basicInfo.text@","@config.emsSnmpInfo.text@"]
	});
	tab1.init();

	function msg(index){
		switch(index){
			case 0:
				$(".tabBody").css("display","block");
				break;
			default:
				$(".tabBody").css("display","none");
				$(".tabBody").eq(index-1).fadeIn();
				break;
		}
	}
	</script>
</body>
</Zeta:HTML>