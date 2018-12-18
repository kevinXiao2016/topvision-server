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
var cameraSwitch = '${cameraSwitch}';
</script>
<script type="text/javascript" defer>
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var entityId = '${entityId}',
    snmpVersion = '${snmpParam.version}',
    entitySnmpVersion = '${oltAttribute.topSysSnmpVersion}',
    oltName = '${oltAttribute.oltName}',
    name = '${entity.name}',
    roomName = '${entity.sysLocation}',
    rackNum = '${oltAttribute.topSysOltRackNum}',
    frameNum = '${oltAttribute.topSysOltFrameNum}',
    note = '${entity.note}',
	inbandIp = '${oltAttribute.inbandIp}',
	inbandMask = '${oltAttribute.inbandMask}',
	inbandMac = '${formatedInbandMac}',
	outbandIp = '${oltAttribute.outbandIp}',
	outbandMask = '${oltAttribute.outbandMask}',
	outbandMac = '${formatedOutbandMac}',
	inbandPortIndex = '${oltAttribute.inbandPortIndex}',
	//inbandMaxBw = '${oltAttribute.topSysInBandMaxBw}',
	inbandVlanId = '${oltAttribute.inbandVlanId}',
	emsIpAddress = '${entity.ip}',
	emsReadCommunity = "${snmpParam.community}",           //注意,读写共同体，支持特殊字符，这里只能用双引号.双引号已经转义为&quot;单引号不转义;
	emsWriteCommunity = "${snmpParam.writeCommunity}",     //同上;
	securityLevel = '${snmpParam.securityLevel}',
	authProtocol = '${snmpParam.authProtocol}',
	username = '${snmpParam.username}',
	authPassword = '${snmpParam.authPassword}',
	privProtocol = '${snmpParam.privProtocol}',
	privPassword = '${snmpParam.privPassword}',
	ipAddress = '${oltAttribute.snmpHostIp}',
	manageHostMask = '${oltAttribute.hostIpMask}',
	community = '${oltAttribute.topSysWriteCommunity}',
	readCommunity = '${oltAttribute.topSysReadCommunity}',
	oltVlanInterfaceList = Ext.decode('${oltVlanInterfaceList}'),
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
// ip-mask
Executor.prototype.inbandIp
Executor.prototype.outbandIp
Executor.prototype.inbandMask
Executor.prototype.outbandMask
Executor.prototype.inbandMac
Executor.prototype.outbandMac
// gate
Executor.prototype.outbandGate
Executor.prototype.inbandGate
// device snmp
Executor.prototype.snmpHostIp
Executor.prototype.manageHostMask
Executor.prototype.entitySnmpVersion
Executor.prototype.snmpCommunity
Executor.prototype.snmpWriteCommunity
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
// inband params
Executor.prototype.inbandPort
Executor.prototype.maxband
Executor.prototype.inbandVlanId
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

    /* var el = Zeta$('securityLevel');
    for (var i = 0; i < el.options.length; i++) {
        if (el.options[i].value == '${snmpParam.securityLevel}') {
            el.selectedIndex = i;
            break;
        }
    } */
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
		call back when ip-mask config success
******************************************************/
Result.prototype.saveInbandConfigSuccess = function(json){
    //json = Ext.decode(json)
    if(json.message == "success")
		executor.processResults.push("inbandIpMaskSegment")
	else
		executor.processResults.push("ConfigError"+Math.random())
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
	}
}

Result.prototype.saveOutbandConfigSuccess = function(json){
    //json = Ext.decode(json)
	if(json.message == "success")
		executor.processResults.push("outbandIpMaskSegment")
	else
    	executor.processResults.push("ConfigError"+Math.random())
    if(executor.processResults.length == executor.modifiedVecotr.length){
        executor.showSaveResult()
    }
}

/******************************************************
		call back when gate config success
******************************************************/
Result.prototype.saveGateConfigSuccess = function(json){
	executor.processResults.push("gateSegment")
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
	}
}

/******************************************************
		call back when inband param config success
******************************************************/
Result.prototype.saveInbandParamConfigSuccess = function(json){
	executor.processResults.push("inbandParamSegment")
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
		call back when basic config success
******************************************************/
Result.prototype.saveEntitySnmpConfigSuccess = function(json){
	executor.processResults.push("entitySnmpSegment")
	if(executor.processResults.length == executor.modifiedVecotr.length){
		executor.showSaveResult()
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
			case "gateSegment":
				if(executor.processResults.indexOf(sendItem) != -1)
					result += "<div>"+ I18N.config.page.gatewaySetSuccess + "</div>"
				else
					result += "<div style='color:red'>"+ I18N.config.page.gatewaySetFail +"</div>"
				break
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
			case "inbandIpMaskSegment":
				if(executor.processResults.indexOf(sendItem) != -1){
					result += "<div>"+I18N.config.inbandParam.setSuccess+"</div>"
					isModifiedIO = true;
				}else
					result += "<div style='color:red'>"+I18N.config.inbandParam.setFail+"</div>"
				break
			case "outbandIpMaskSegment":
                if(executor.processResults.indexOf(sendItem) != -1){
                	isModifiedIO = true;
                    result += "<div>"+I18N.config.outbandParam.setSuccess+"</div>"
                }else
                    result += "<div style='color:red'>"+I18N.config.outbandParam.setFail+"</div>"
                break
			case "serverSnmpSegment":
				if(executor.processResults.indexOf(sendItem) != -1){
					result += "<div>"+I18N.config.emsSnmpInfo.setSuccess+"</div>"
					//window.location.reload();
				}else
					result += "<div style='color:red'>"+I18N.config.emsSnmpInfo.setFail+"</div>"
				break
			case "entitySnmpSegment":
				if(executor.processResults.indexOf(sendItem) != -1){
					result += "<div>"+I18N.config.deviceSnmpInfo.setSuccess+"</div>"
					//window.location.reload();
				}else
					result += "<div style='color:red'>"+I18N.config.deviceSnmpInfo.setFail+"</div>"
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
	if (!ipIsFilled("emsIpAddress")){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputEmsIp)  &&  Zeta$("emsIpAddress").focus()  &&   false
	}
	//add by flackyang 2013-09-10
	// modify by fanzidong,IP地址校验与新建设备IP规则一致
    if (!top.IpUtil.isValidDeviceIp(this.emsIpAddress)){  
    	window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.emsIpAddressCheck);
    	return false;
    }
    //if( this.alias == null || this.alias.trim() == '' || this.alias.trim().length > 63 || !validateAlias(this.alias.trim())){
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
	/***
	不进行检查
	if(isEmsIpAddressConflict){
		window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.existEmsIp);
		return Zeta$("emsIpAddress").focus() && false;
	}
	*/
	if (!this.alias) {
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputAlias)  &&  false
	}
	return true
}

Executor.prototype.check_basic = function(){
	//比较一下,当前模块是否有改动
	if(oltName == this.oltName)
		return true
	else
		this.modifiedVecotr.push("basicSegment")
	
	//设备名称与联系人非中文验证
	var reg = /^[a-zA-Z0-9-\s_:;\/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/


   /*  var tempName = this.oltName.replace(/\s/g,"");//这里是去掉空字符
    if(tempName == ""){
    	return Zeta$("oltName").focus();
    }
    var tempAlias = this.alias.replace(/\s/g,"");//这里是去掉空字符
    if(tempAlias == ""){
    	return Zeta$("name").focus();
    } */
    if(!Validator.isAnotherName($("#oltName").val())){
    	return Zeta$("oltName").focus() && false;
    }
 
  	//数据非空验证
    if ( !this.oltName) 
       	return  window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputName)  &&  false
	return true
}

/******************************************************
	 inband ip_mask_mac segment modified
******************************************************/
Executor.prototype.check_inbandInfo = function(){
	if( inbandIp == this.inbandIp  && inbandMask == this.inbandMask && inbandVlanId == this.inbandVlanId)
	 	return true
	else
		this.modifiedVecotr.push("inbandIpMaskSegment")
	//数据非空验证
    if (!ipIsFilled("inbandIp")){
    	return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputInbandIp) && ipFocus("inbandIp") && false;
    }
	//add by flackyang 2013-09-10
	//modify by fanzidong,修改校验规则
    if (!ipIsFilled("inbandMask")){
    	return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputInbandMask) && ipFocus("inbandMask1") && false;
    }
    if(this.inbandIp == EMPTY_IP_MASK_ADDR && this.inbandMask == EMPTY_IP_MASK_ADDR){
		return true;
	}else if(this.inbandIp == EMPTY_IP_MASK_ADDR && this.inbandMask != EMPTY_IP_MASK_ADDR){
		return window.parent.showMessageDlg(I18N.COMMON.message, "@config.page.inBandIpAndMask@") && false;
	}else if (this.inbandMask == EMPTY_IP_MASK_ADDR || !checkedIpMask(this.inbandMask)){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inbandMaskError) && false;
	}
        
 	/* if (inbandMaxBw != this.maxband && isNaN(this.maxband) || this.maxband < 1 || this.maxband > 3000)
        return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.maxBandError) && Zeta$("maxband").focus()	&&  false */
    if (!this.inbandVlanId) 
        return window.parent.showMessageDlg(I18N.COMMON.message,  I18N.config.page.inputMgmtVlanId) &&   false
    if(this.inbandIp.substring(0,3) == "127"){
    	return window.parent.showMessageDlg(I18N.COMMON.message,  I18N.config.inbandParam.ipConflict127) &&   false
    }
 	
    if (!top.IpUtil.isValidDeviceIp(this.inbandIp)){  
    	window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inbandIpCheck);
    	return false;
    }
	 //如果当前网段与vlan虚接口的某一个网段相同，则错误
    if(isConflictWithVlanInterface(this.inbandIp,this.inbandMask))
      	return window.parent.showMessageDlg(I18N.COMMON.message,  I18N.config.page.vlanInterfaceConflict) &&   false
    return true
}

/******************************************************
     outband ip_mask_mac segment modified
******************************************************/
Executor.prototype.check_outbandInfo = function(){
	if( outbandMask == this.outbandMask   &&  outbandIp == this.outbandIp )
		return true
	else
		this.modifiedVecotr.push("outbandIpMaskSegment")
	//数据非空验证
	if (!ipIsFilled("outbandIp")) {
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputOutbandIp) && Zeta$("outbandIp1").focus() && false;
	}
	//add by flackyang 2013-09-10
    //modify by fanzidong,修改校验规则
	if (!ipIsFilled("outbandMask")){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputOutbandMask) && Zeta$("outbandMask1").focus() && false;
	}
	if(this.outbandIp == EMPTY_IP_MASK_ADDR && this.outbandMask == EMPTY_IP_MASK_ADDR){
			return true;
	}else if(this.outbandIp == EMPTY_IP_MASK_ADDR && this.outbandMask != EMPTY_IP_MASK_ADDR){
		return window.parent.showMessageDlg(I18N.COMMON.message, "@config.page.outBandIpAndMask@") && false;
	}else if (this.outbandMask == EMPTY_IP_MASK_ADDR || !checkedIpMask(this.outbandMask)){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.outbandMaskError) && false;
	}
	if (!top.IpUtil.isValidDeviceIp(this.outbandIp)){  
	   	window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.outbandIpCheck);
	   	return false;
	}
	//如果当前网段与vlan虚接口的某一个网段相同，则错误
    if(isConflictWithVlanInterface(this.outbandIp,this.outbandMask))
      	return window.parent.showMessageDlg(I18N.COMMON.message,  I18N.config.page.vlanInterfaceConflictOutband) &&   false
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
		 entity snmp segment modified
******************************************************/
Executor.prototype.check_entity_snmp = function(){
	if(ipAddress == this.snmpHostIp &&  manageHostMask == this.manageHostMask && entitySnmpVersion == this.entitySnmpVersion){
		return true;
	}else{
		this.modifiedVecotr.push("entitySnmpSegment");
	}
	if (!ipIsFilled("ipAddress")){
		 return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputMgmtIp) && false;
	}
	//add by flackyang 2013-09-10
    //modify by fanzidong,修改校验规则
	if (!ipIsFilled("manageHostMask")){
		 return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.inputMgmtMask) && false;
	}
	if(this.snmpHostIp == '0.0.0.0' && this.manageHostMask == '0.0.0.0'){
		return true;
	}else if(this.snmpHostIp == '0.0.0.0' && this.manageHostMask != '0.0.0.0'){
		return window.parent.showMessageDlg(I18N.COMMON.message, "@config.page.hostIpAndMask@") && false;
	}else if (this.manageHostMask == '0.0.0.0' || !checkedIpMask(this.manageHostMask)){
		return window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.mgmtMaskError) && false;
	}
	if (!top.IpUtil.isValidDeviceIp(this.snmpHostIp)){  
	    window.parent.showMessageDlg(I18N.COMMON.message, "@config.page.snmpHostIpCheck@");
	    return false;
	}
	return true
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
	/* var reg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/]{1,63}$/;
	if( !reg.test(name) ){
		$("#name").focus();
		return;
	} */
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

	this.alias = $('#name').val().replace(/\s/g,"")

	this.rack = $("#rackList").val()
	this.frame = $("#frameList").val()
	/*if(checkFrameNo()){
		$("#frameList").focus();
		return;
	}
	if(checkRackNo(this.rack)){
		$("#rackList").focus();
		return;
	} */
	this.desc = $("#desc").val()
	this.note = $("#note").val().trim();
	// inband
	this.inbandIp =  getIpValue("inbandIp")
    this.inbandMask = getIpValue("inbandMask")
    //this.maxband = $("#maxband").val()
    //设备上使用 0 代表没有配置
    this.inbandVlanId = Ext.getCmp("inbandVlanIdCombo").getValue() || 0
    //outband
    this.outbandIp = getIpValue("outbandIp")
    this.outbandMask = getIpValue("outbandMask")
    // device snmp
	this.snmpHostIp = getIpValue("ipAddress")
   	this.manageHostMask = getIpValue("manageHostMask") 
   	if(Zeta$('snmpVersion').value == 1){
   		this.entitySnmpVersion = 2
   	}else{
   		this.entitySnmpVersion = Zeta$('snmpVersion').value
   	}
	//this.snmpCommunity = $('#topSysReadCommunity').val()
	//this.snmpWriteCommunity = $('#topSysWriteCommunity').val()
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
	// inband ip-mask 
	if(!this.check_inbandInfo()) return
	// ouband ip-mask 
	if(!this.check_outbandInfo()) return
	//带内带外不能在同一个网段内
	if(!this.check_common()) return
	// server snmp 
	if(!this.check_server_snmp()) return
	// entity snmp 
	if(!this.check_entity_snmp()) return

	//FIXME 没有任何改动的时候全部下发下去，有改动的时候却只下发某一些改变，这个是否合理
	//FIXME 如果修改了网关,需要在所有设置之前进行提示
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.config.page.setting)
	if(this.modifiedVecotr.length == 0)
		if(executor.save_nochange_config())  return
	if(this.modifiedVecotr.indexOf("basicInfo") != -1)
		executor.save_basicInfo_config()
	if(this.modifiedVecotr.indexOf("basicSegment") != -1)
		executor.save_basic_config();
	if(this.modifiedVecotr.indexOf("inbandIpMaskSegment") != -1)
		executor.save_inband_config() //顺带把带内参数也设置了
	if(this.modifiedVecotr.indexOf("outbandIpMaskSegment") != -1)
	    executor.save_outband_config() //顺带把带外参数也设置了
	if(this.modifiedVecotr.indexOf("serverSnmpSegment") != -1)
		executor.save_server_snmp_config()
	if(this.modifiedVecotr.indexOf("entitySnmpSegment") != -1)
		executor.save_entity_snmp_config() 
}

Executor.prototype.check_common = function(){
	/**如果带内带外都为0则表示带内带外没有配置，是用的虚接口访问的，这是允许的 */
	if(this.inbandIp == this.outbandIp && this.inbandIp== EMPTY_IP_MASK_ADDR)return true;
    //判断带内带外网管是否在一个子网内
    if (this.inbandIp && this.outbandIp)
        if (ipAAndipB(this.inbandIp, this.inbandMask) == ipAAndipB(this.outbandIp, this.outbandMask)) 
           return  window.parent.showMessageDlg(I18N.COMMON.message, I18N.config.page.lanError)   &&   false
    return true
}

/******************************************************
			判断是否和虚接口在同一网段
******************************************************/
function isConflictWithVlanInterface(ip,mask){
	if(ip == EMPTY_IP_MASK_ADDR && mask == EMPTY_IP_MASK_ADDR){
		return false;
	}
	for(var i=0;i<oltVlanInterfaceList.length;i++){
		var vIp = oltVlanInterfaceList[i].ip;
		var vMask = oltVlanInterfaceList[i].mask;
		if(ipAAndipB(ip,mask) == ipAAndipB(vIp,mask) || ipAAndipB(ip,vMask) == ipAAndipB(vIp,vMask)){
			return true;
		}
	}
	return false;
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
	    //data.alias = this.alias
	    data.rack = this.rack || 0
	    data.frame = this.frame || 0
	    data.desc = this.desc
	    //data.note = this.note
	var url = "/config/saveBasicConfig.tv"
	//send request
	executor.request(url,data,result.saveBasicConfigSuccess,result.emptyFn)
}

//带内带外ip/mask
Executor.prototype.save_inband_config = function(){
    // request params
    var newIp = this.inbandIp;
    var data = new Object
    	data.entityId = entityId
    	//FIXME 理论上如果带内地址没有做修改是不必下发的
		data.inbandIp =   this.inbandIp
		data.oldIp = inbandIp
	    data.inbandMask = this.inbandMask
	    data.inbandVlanId = this.inbandVlanId
		//为了方便存数据库,都下发到设备上
		//data.maxBand = this.maxband
	    //如果带外ip与mask都没有被修改,则不下发带外信息
	    if(outbandIp != this.outbandIp || outbandMask != this.outbandMask) {
	    	data.outbandIp = this.outbandIp 
			data.outbandMask = this.outbandMask
		}
	var url = "/config/saveInbandParamAndIP.tv"
	//send request
	executor.request(url,data,result.saveInbandConfigSuccess,result.emptyFn)
	return;
    /* $.ajax({
        url: '/config/saveInbandParamAndIP.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:data,
        success: function(json) {
            if (json.message == "success") {
            	window.parent.showConfirmDlg("@COMMON.tip@","@config.inbandParam.modifyEmsIpTip@", function(type) {
                    if (type == 'no'){return;}
                    $.ajax({
                        url: '/config/updateEmsIpAddress.tv?entityId='+ entityId +'&newIp='+ newIp +'&r=' + Math.random(),
                        type: 'POST',
                        dataType:'text',
                        success: function(text) {
                            if (text == "success") {
                            	window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifySuccess@");
                            	window.location.reload();
                            }else if(text == "fail"){
                                window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifyFail@");
                            }
                        },error : function(){
                        	window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifyFail@");
                		}
                    });
           		})
            } else {
                window.top.showMessageDlg("@COMMON.tip@", "@config.inbandParam.setFail@");
            }
        },error : function(){
        	window.top.showMessageDlg("@COMMON.tip@", "@config.inbandParam.setFail@");
		}
    }); */
}

//带内带外ip/mask
Executor.prototype.save_outband_config = function(){
    // request params
    var newIp = this.outbandIp;
    var data = new Object
        data.entityId = entityId
        //如果带外ip与mask都没有被修改,则不下发带外信息
        data.outbandIp = this.outbandIp 
        data.outbandMask = this.outbandMask
        data.oldIp = outbandIp
    var url = "/config/saveOutbandParamAndIP.tv"
    // send request
    executor.request(url,data,result.saveOutbandConfigSuccess,result.emptyFn)
	return;
    $.ajax({
        url: '/config/saveOutbandParamAndIP.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:data,
        success: function(json) {
            if (json.message == "success") {
            	window.parent.showConfirmDlg("@COMMON.tip@","@config.outbandParam.modifyEmsIpTip@", function(type) {
                    if (type == 'no'){return;}
                    $.ajax({
                        url: '/config/updateEmsIpAddress.tv?entityId='+ entityId +'&newIp='+ newIp +'&r=' + Math.random(),
                        type: 'POST',
                        dataType:'text',
                        success: function(text) {
                            if (text == "success") {
                            	top.afterSaveOrDelete({
                        			title: I18N.COMMON.tip,
                        			html: "@config.emsSnmpInfo.ipModifySuccess@"
                        		});
                            	//window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifySuccess@");
                            	window.location.reload();
                            }else if(text == "fail"){
                                window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifyFail@");
                            }
                        },error : function(){
                        	window.top.showMessageDlg("@COMMON.tip@", "@config.emsSnmpInfo.ipModifyFail@");
                		}
                    });
           		})
            } else {
                window.top.showMessageDlg("@COMMON.tip@", "@config.outbandParam.setFail@");
            }
        },error : function(){
        	window.top.showMessageDlg("@COMMON.tip@", "@config.outbandParam.setFail@");
		}
    });
}

//网管snmp信息
Executor.prototype.save_server_snmp_config = function(){
	var data = new Object
	data.entityId = entityId;
	//data.emsIpAddress = this.emsIpAddress;
	//data.alias = this.alias;
	//data.note = this.note;
	//data.oldIp = emsIpAddress;
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
	/* if(emsIpAddress == this.emsIpAddress){
		tip = '@config.basicInfo.modifyServerSNMPTip@';
	}else{
		tip = '@config.basicInfo.modifyServerIpTip@';
	} */
	var url = "/config/saveServerSnmp.tv"
	// send request
	window.parent.showConfirmDlg("@COMMON.tip@",tip, function(type) {
         if (type == 'no'){return;}
         executor.request(url,data,result.saveServerSnmpConfigSuccess,result.emptyFn) 
	})
	
}

//设备snmp信息
Executor.prototype.save_entity_snmp_config = function(){
	var data = new Object
		data.entityId = entityId
		data.manageHostMask = this.manageHostMask
		data.snmpVersion =  this.entitySnmpVersion
		//data.snmpCommunity = this.snmpCommunity
		//data.snmpWriteCommunity = this.snmpWriteCommunity
		data.snmpHostIp = this.snmpHostIp
	var url = "/config/saveEntitySnmp.tv"
	// send request
	executor.request(url,data,result.saveEntitySnmpConfigSuccess,result.emptyFn)
}

/*****************************************
 		APPLICATION ENTRY
 *****************************************/
Ext.onReady(initialize);
function initialize() {
	$("#snmpVersion").val('${oltAttribute.topSysSnmpVersion}');
	new InbandVlanComboBox({
		id:'inbandVlanIdCombo',
		disabled:!operationDevicePower,
		renderTo: 'inbandVlanId'
	})
    var ipFiledVector =[
		//-------------- in/out band ip-mask -----------------//
		{elementId : 'inbandIp',innerId : 'span1' ,defaultValue : inbandIp},
		{elementId : 'inbandMask',innerId : 'span2' ,defaultValue : inbandMask},
		{elementId : 'outbandIp',innerId : 'span4' ,defaultValue : outbandIp},
		{elementId : 'outbandMask',innerId : 'span5' ,defaultValue : outbandMask},
		//-------------- in/out band gate -----------------//
		//--------------  device ip-mask -----------------//
		{elementId : 'ipAddress',innerId : 'span7' ,defaultValue : ipAddress},
		{elementId : 'manageHostMask',innerId : 'span8' ,defaultValue : manageHostMask},
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
	    
	    //带内管理信息
	    //$("#maxband").attr("disabled",true);
	    setIpv4Disabled("span1",true);
	    setIpv4Disabled("span2",true);
	    
	    //带外网管参数
	    setIpv4Disabled("span4",true);
	    setIpv4Disabled("span5",true);
	    
	    //设备SNMP信息
	    setIpv4Disabled("span7",true);
	    setIpv4Disabled("span8",true);
	    $("#snmpVersion").attr("disabled",true);
	    
	    R.modifyBt.setDisabled(true);
	}
    
    if(!refreshDevicePower){
        R.refreshBt.setDisabled(true);
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
			//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
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
<body class="newBody">
<div class=formtip id=tips style="display: none"></div>
<div id=header >
    <%@ include file="/epon/inc/navigator.inc"%>
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
			<tr >
				<td width=120 class="rightBlueTxt"><label for="oltName">@config.basicInfo.name@:
					<font color="red">*</font>
				</label></td>
				<td><input class="normalInput modifiedFlag" style="width: 180px"
					id="oltName" maxlength="63" value="${oltAttribute.oltName}"
					tooltip="@config.page.nameTip@" /></td>
				
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

<!-- INBAND PARAM SEGMENT -->
<div class="edge10 pB0 clearBoth tabBody" id="inbandParamSegment">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.inbandParam.text@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<%-- <td width=120 class="rightBlueTxt">@config.inbandParam.maxBand@:<font
					color=red>*</font></td>
				<td><input style="width: 160px" id="maxband" class="normalInput"
					value="${oltAttribute.topSysInBandMaxBw}" tooltip="@config.page.maxBandTip@" /> pps</td> --%>
				<td width=100 class="rightBlueTxt">@config.inbandParam.mgmtVlanId@:<font
					color=red>*</font></td>
				<td>
					<div id=inbandVlanId class=modifiedFlag style='width: 180px'></div>
				</td>
				<td colspan="4"></td>
			</tr>
			<tr>
				<td width=120 class="rightBlueTxt">@config.inbandParam.ip@:<font
					color=red>*</font></td>
				<td><span id="span1" class="modifiedFlag"
					style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt">@config.inbandParam.mask@:<font
					color=red>*</font></td>
				<td><span id="span2" class="modifiedFlag"
					style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt">@config.inbandParam.mac@:</td>
				<td>
					<input id="inbandMac" class="normalInput normalInputDisabled" type="text" onfocus='$(this).blur()' style="width: 180px" disabled value="${formatedInbandMac}"  />
				</td>
			</tr>
		</tbody>
	</table>
</div>

<!-- IPMASK SEGMENT -->
<div class="edge10 pB0 clearBoth tabBody" id="ipMaskSegment">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.outbandParam.text@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width=120 style="padding-top: 10px" class="rightBlueTxt">@config.outbandParam.ip@:<font
					color=red>*</font></td>
				<td style="padding-top: 10px"><span id="span4"
					class="modifiedFlag" style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt" style="padding-top: 10px">@config.outbandParam.mask@:<font
					color=red>*</font></td>
				<td style="padding-top: 10px"><span id="span5"
					class="modifiedFlag" style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt" style="padding-top: 10px">@config.outbandParam.mac@:</td>
				<td style="padding-top: 10px">
					<input id=outbandMac class= "normalInput normalInputDisabled" type="text" onfocus='$(this).blur()'
					style="width: 180px" value="${formatedOutbandMac}" disabled /></td>
			</tr>
		</tbody>
	</table>
</div>

<!-- DEVICE SEGMENT -->
<div class="edge10 pB0 clearBoth tabBody" id="deviceSegment">
	<table class="dataTableRows zebra" width="100%" border="1"
		cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@config.deviceSnmpInfo.text@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width=120 class="rightBlueTxt" style="padding-top: 10px">@config.deviceSnmpInfo.ip@:</td>
				<td style="padding-top: 10px"><span id="span7"
					class="modifiedFlag" style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt" style="padding-top: 10px">@config.deviceSnmpInfo.mask@:</td>
				<td style="padding-top: 10px"><span id="span8"
					class="modifiedFlag" style='width: 180;'></span></td>
				<td width=120 class="rightBlueTxt" style="padding-top: 10px">@config.deviceSnmpInfo.version@:</td>
				<td style="padding-top: 10px"><select id="snmpVersion" class="modifiedFlag normalSel" style="width: 180px">
						<option value="2">SNMP V2C</option>
						<option value="3">SNMP V3</option>
						<option value="4">ALL(SNMP V1,V2C,V3)</option>
				</select></td>
			</tr>
			<%-- <tr id="v2setting">
                 <td width=120 class="rightBlueTxt">@config.deviceSnmpInfo.readCommunity@</td>
                 <td><input class="normalInput modifiedFlag" style="width: 180px"
                            name="oltAttribute.topSysReadCommunity" id="topSysReadCommunity"
                            value="<s:property value="oltAttribute.topSysReadCommunity"/>">
                 </td>
                 <td width=120 class="rightBlueTxt">@config.deviceSnmpInfo.writeCommunity@</td>
                 <td><input class="normalInput modifiedFlag" type=text style="width: 180px"
                            name="oltAttribute.topSysWriteCommunity"
                            id="topSysWriteCommunity"
                            value="<s:property value="oltAttribute.topSysWriteCommunity"/>">
                 </td>
             </tr> --%>
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
				<%-- <td id="emsV3setting1" class=v3setting  width=120 class="rightBlueTxt" style='display:none;'>@config.dol.level@:</td>
                 <td id="emsV3setting2" class=v3setting  style='display:none;'><select id="securityLevel"
                                                name="snmpParam.securityLevel" style="width: 180px">
                     <option value="1">NOAUTH_NOPRIV</option>
                     <option value="2">AUTH_NOPRIV</option>
                     <option value="3">AUTH_PRIV</option>
                 </select>
                 </td> --%>
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
			<%--  <tr id="emsV3setting8" style='display:none;'>
                 <td class="rightBlueTxt" width=120>@vlan/config.dol.context@:</td>
                 <td><input class=normalInput type=text style="width: 180px"
                            name="snmpParam.contextName"
                            value="<s:property value="snmpParam.contextName"/>">
                 </td>
                 <td class="rightBlueTxt" width=120>Context ID:</td>
                 <td><input class=normalInput type=text style="width: 180px"
                            name="snmpParam.contextId"
                            value="<s:property value="snmpParam.contextId"/>">
                 </td>
                 <td class="rightBlueTxt" width=120>@config.dol.engineId@:</td>
                 <td><input class=normalInput type=text style="width: 180px"
                            name="snmpParam.authoritativeEngineID"
                            value="<s:property value="snmpParam.authoritativeEngineID"/>">
                 </td>
             </tr> --%>
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
	    tabs:["@COMMON.all@","@config.basicInfo.textEms@","@config.basicInfo.text@","@config.inbandParam.text@","@config.outbandParam.text@","@config.deviceSnmpInfo.text@","@config.emsSnmpInfo.text@"]
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