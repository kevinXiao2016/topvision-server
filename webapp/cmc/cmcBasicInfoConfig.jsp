<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = '${cmcId}';
var cmcAttribute = ${cmcAttrJson};
var entityId = '${entityId}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var changeEmsName=<%=uc.hasPower("changeEmsName")%>;
var pageId= '${pageId}';
var isCcmtsWithAgentType = null;
var regNum = 60;
Ext.onReady(function(){
	initData();
})
function initData(){
	if(changeEmsName){
        $('#alias').removeClass("normalInputDisabled");
        $('#alias').addClass("normalInput");
        $('#alias').attr("disabled",false);
    }else{
        $('#alias').removeClass("normalInput");
        $('#alias').addClass("normalInputDisabled");
        $('#alias').attr("disabled",true);
    }
	$("#alias").val(cmcAttribute.nmName);
	$("#ccName").val(cmcAttribute.topCcmtsSysName);
	$("#ccSysLocation").val(cmcAttribute.topCcmtsSysLocation);
	$("#ccSysContact").val(cmcAttribute.topCcmtsSysContact);
}

function closeClick() {
    window.parent.closeWindow('basicInfoMgmt');
}
function validateAlias(str){
    var reg = /^[\w\d\u4e00-\u9fa5\[\]\(\)-\/]*$/;
    return reg.test(str);
}

//只能输入英文、数字、下划线和左斜线
function validate(str) {
    var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!%#*$^=])+$/ig;
    return reg.test(str);
}

//只能输入非空格字符,contact和location校验
function validateLocationInfo(str){
// 	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$@#!=+|?<>\[\]\{\}\\`~]*$/
// 	if(str.indexOf(" ")>-1 || !reg.test(str)){
// 		return true;
// 	}else{
// 		return false;
// 	}
//     return false;
    var reg=/^[\w@.\/\(\)\-_\s]*$/;
    return reg.test(str);
}

//只能输入非空格字符,contact和location校验
function validateContactInfo(str){
    /*   
	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$@!=+|?<>\[\]\{\}\\`~]+$/
	if(str.indexOf(" ")>-1 || !reg.test(str)){
		return true;
	}else{
		return false;
	}
    */
    // 字母、数字、下滑线、. 、@
    var reg=/^[\w@.\/\(\)\-_\s]*$/;
    return reg.test(str);
}
	
function ccBasicInfoConfig() {
	var alias = $("#alias").val();
	/* if(alias == '' || alias == null  || alias.length > 63 || !validateAlias(alias)){
		$("#alias").focus();
		return;
	} */
	if( !Validator.isAnotherName(alias) ){
		$("#alias").focus();
		return;
	}
	
	var ccName = $("#ccName").val();
	if(ccName == '' || ccName == null || !validate(ccName) || ccName.length > regNum){
		$("#ccName").focus();
		return;
	}
	var ccSysContact = $("#ccSysContact").val();
	if(ccSysContact == null || !validateContactInfo(ccSysContact) || ccSysContact.length < 0 || ccSysContact.length > regNum){
		$("#ccSysContact").focus();
		return;
	}
	var ccSysLocation = $("#ccSysLocation").val();
	var t=validateLocationInfo(ccSysLocation);
	if( ccSysLocation == null || !validateLocationInfo(ccSysLocation) || ccSysContact.length < 0 || ccSysLocation.length > regNum){
		$("#ccSysLocation").focus();
		return;
	}
	$.ajax({
		  url: '/cmc/modifyCcmtsBasicInfo.tv',
		  data: {
			  'cmcId' : cmcId,
			  'cmcName' : ccName,
			  'ccSysContact' : ccSysContact,
			  'ccSysLocation' : ccSysLocation,
			  'alias' : alias
		  },
  	      type: 'post',
  	      success: function(response) {
	  	    	if(response == "success"){
	  	    		top.afterSaveOrDelete({
	  	   				title: '@COMMON.tip@',
	  	   				html: '<b class="orangeTxt">' + I18N.CCMTS.modifyCcmtsBasicInfoSuccess + '</b>'
	  	   			});
	  	    		window.parent.getFrame("entity-" + cmcId).reload();
	  	    	}else{
	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.modifyCcmtsBasicInfoError);
	  	    	}
                try {
                    window.parent.getActiveFrame().refreshClick();
                } catch(ex) {
                }
	  	    	closeClick();
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.modifyCcmtsBasicInfoError);
			}, cache: false
		});
}
function authLoad(){
	if(!operationDevicePower){
	    $("#ccName").attr("disabled",true);
	    $("#ccSysLocation").attr("disabled",true);
	    $("#ccSysContact").attr("disabled",true);
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@CMC.text.setCcmtsBasecfg@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
			<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w200">@RESOURCES/COMMON.alias@:</td>
				<td ><input class="normalInput w200" id="alias" maxlength="63" tooltip="@COMMON.anotherName@" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt w200">@CMC.label.labelHardwareName@:</td>
				<td ><input class="normalInput w200" id="ccName" maxlength="60" tooltip="@CCMTS.renameTipB@" /></td>
			</tr>
			<tr  class="darkZebraTr">
				<td class="rightBlueTxt">@CCMTS.entityLocation@:</td>
				<td><input class="normalInput w200" id="ccSysLocation" maxlength="60" tooltip="@CCMTS.LocationInfo@" />
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@CCMTS.contactPerson@:</td>
				<td><input class="normalInput w200" id="ccSysContact" maxlength="60" tooltip="@CCMTS.ContactInfo@)" />
				</td>
			</tr>
		</table>
		</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBt" onClick="ccBasicInfoConfig()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="closeClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>