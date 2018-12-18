<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<script type="text/javascript">
var tmpText = "";
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
//email 验证
function isEmail(str){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return reg.test(str);
}
//电话号码验证
function isNotPhoneNumber(phoneNumber){
    var reg = /[^0-9^\-]/;
    return reg.test(phoneNumber);
}
function mobileCheck(mobileNumber){
	var reg=/^1\d{10}$/;
	return reg.test(mobileNumber);
}
function selectPostClick() {
    window.parent.createDialog('postDlg', I18N.SYSTEM.selectPost, 600, 370, "system/popPost.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('placeId').value = clipboard.selectedItemId;
                    Zeta$('placeName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
function selectDepartmentClick() {
    window.parent.createDialog('departmentDlg', I18N.SYSTEM.selectDepartment, 600, 370, "system/popDepartment.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('departmentId').value = clipboard.selectedItemId;
                    Zeta$('departmentName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
function okClick() {
	//工作电话验证
	var workPhone = Zeta$('workPhone');
	if ( workPhone.value.trim() != '' && isNotPhoneNumber(workPhone.value.trim())) {
		top.afterSaveOrDelete({
          title: I18N.COMMON.tip,
          html: '<b class="orangeTxt">'+I18N.WorkBench.workTelephoneWrong+'</b>'
        });
		$('#workPhone').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.workTelephoneWrong);
		return;
	}

	//家庭电话验证
	var homePhone = Zeta$('homePhone');
	
	if ( homePhone.value.trim() != '' && isNotPhoneNumber(homePhone.value.trim())) {
		top.afterSaveOrDelete({
          title: I18N.COMMON.tip,
          html: '<b class="orangeTxt">'+I18N.WorkBench.homeTelephoneWrong+'</b>'
        });
		$('#homePhone').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.homeTelephoneWrong);
		return;
	}

	//手机验证
	var mobilePhone = Zeta$('mobilePhone');
	if ( mobilePhone.value.trim() != '' && !mobileCheck(mobilePhone.value.trim())) {
		top.afterSaveOrDelete({
          title: I18N.COMMON.tip,
          html: '<b class="orangeTxt">'+I18N.WorkBench.cellphoneWrong+'</b>'
        });
		$('#mobilePhone').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.cellphoneWrong);
		return;
	}

	//email验证
	var email = Zeta$('email');
	if (email.value.trim() != '' && !isEmail(email.value.trim())) {
		top.afterSaveOrDelete({
          title: I18N.COMMON.tip,
          html: '<b class="orangeTxt">'+I18N.WorkBench.emailWrong+'</b>'
        });
		$('#email').focus();
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.emailWrong);
		return;
	}
	
	var $sessionTime= $("#sessionTime"),
    $timeout = $sessionTime.val(),
    pass = Validator.isSystemTimeout($timeout);
	if( !pass ){
		$sessionTime.focus();
		return;	
	}
	
	$.ajax({
		url: 'updateUserPersonaInfo.tv', type: 'POST',
		data: jQuery(userForm).serialize(),
		success: function(json) {
			if(window.top.getFrame('userList')){
				window.top.getFrame('userList').onRefreshClick();
			}
			top.afterSaveOrDelete({
	          title: I18N.COMMON.tip,
	          html: '<b class="orangeTxt">'+I18N.COMMON.modifySuccess+'</b>'
	        });
			cancelClick();
		}, error: function(json) {
			window.top.showErrorDlg();
		}, cache: false
	});
}
Ext.onReady(function(){
	$("#name").focus();
});

function keydown(s){
	var aa;
	if(s == 1){
		aa = $("#departmentName");
	}else if(s == 2){
		aa = $("#placeName");
	}
	tmpText = aa.val();
}
function keyup(s){
	var aa;
	if(s == 1){
		aa = $("#departmentName");
	}else if(s == 2){ 
		aa = $("#placeName");
	}
	aa.val(tmpText);
}

//修改多IP登陆;
function changeIPLoad(me){
	var isOn = true;
	if(me.name == "off"){
		isOn = false;
	}
	if(isOn){
		$("#allowMutliIpLogin").val(false);
		me.name = "off";
		me.src = "../images/speOff.png";
	}else{
		$("#allowMutliIpLogin").val(true);
		me.name = "on";
		me.src = "../images/speOn.png";
	}
}
</script>
</head><body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco userCirIco"></div>
	</div>
	<div class=formtip id=tips style="display: none"></div>
	<form id="userForm" name="userForm">
		<input type=hidden id="departmentId" name="userEx.departmentId" value="<s:property value="userEx.departmentId"/>" /> 
		<input type=hidden id="placeId" name="userEx.placeId" value="<s:property value="userEx.placeId"/>" /> 
		<%-- <input type=hidden id="placeId" name="userEx.userName" value="<s:property value="userEx.userName"/>" /> --%>
		<input type=hidden id="roleIds" name="userEx.roleIds" value="<s:property value="userEx.roleIds"/>" />
		<input type=hidden name="userEx.userGroupId" value="<s:property value="userEx.userGroupId" />" /> 
		<input type=hidden id="userId" name="userEx.userId" value='<s:property value="userEx.userId"/>' />
		<input type=hidden id="allowMutliIpLogin" name="userEx.allowMutliIpLogin" value="<s:property value="userEx.allowMutliIpLogin"/>" /> 

		<div class="edgeTB10LR20 pT40">
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		                 <td class="rightBlueTxt" width="110">
							<label for="userName"><fmt:message key="td.label.userName" bundle="${workbench}"/></label>
		                 </td>
		                 <td>
							<input style="width: 200px;" 
							id="userName" name="userEx.userName" readonly
							value='<s:property value="userEx.userName"/>' class="normalInputDisabled" type=text
							maxlength=32 />
		                 </td>
		                 <td class="rightBlueTxt" width="110">
							@workbench/userEx.familyName@
		                 </td>
		                 <td>
							<input style="width: 200px;" name="userEx.familyName" id="name"
							value='<s:property value="userEx.familyName"/>' class="normalInput"
							type=text maxlength=32 />
		                 </td>
		             </tr>
		              <tr class="darkZebraTr">
		                 <td class="rightBlueTxt">
							@workbench/userEx.workTelphone@
		                 </td>
		                 <td>
							<input style="width: 200px;"
							name="userEx.workTelphone" id="workPhone"
							value='<s:property value="userEx.workTelphone"/>' class="normalInput"
							type=text maxlength=16 />
		                 </td>
		                 <td class="rightBlueTxt">
							@workbench/userEx.homeTelphone@
		                 </td>
		                 <td>
							<input style="width: 200px;" name="userEx.homeTelphone"
							id="homePhone" value='<s:property value="userEx.homeTelphone"/>'
							class="normalInput" type=text maxlength=16 />
		                 </td>
		             </tr>
		              <tr>
		                 <td class="rightBlueTxt">
							@workbench/userEx.mobile@
		                 </td>
		                 <td>
							<input style="width: 200px;" name="userEx.mobile"
							id="mobilePhone" value='<s:property value="userEx.mobile"/>'
							class="normalInput" type=text maxlength=16/>
		                 </td>
		                 <td class="rightBlueTxt">
							@workbench/userEx.email@
		                 </td>
		                 <td>
							<input style="width: 200px;" name="userEx.email" id="email"
							value='<s:property value="userEx.email"/>' class="normalInput" type=text
							maxlength=32 />
		                 </td>
		             </tr>
		              <tr class="darkZebraTr">
		                 <td class="rightBlueTxt">
							@workbench/input.departmentName@
		                 </td>
		                 <td>
							<input style="width: 143px;float:left" id="departmentName" 
							name="userEx.departmentName"
							value='<s:property value="userEx.departmentName"/>' class="normalInput"
							type=text onkeyup=keyup(1)
							onfocus="inputFocused('departmentName', '<fmt:message key="onfocus.pleaseInputDepartmentName" bundle="${workbench}"/>', 'normalInputFocus'),keydown(1)"
							onblur="inputBlured(this, 'normalInput');"
							onclick="clearOrSetTips(this);" />
							<a id="selectRoleBt" onclick="selectDepartmentClick();" href="javascript:;" class="nearInputBtn"><span><fmt:message key="button.select" bundle="${workbench}"/></span></a>
		                 </td>
		                 <td class="rightBlueTxt">
							<label for="placeName"><fmt:message key="td.label.placeName" bundle="${workbench}"/></label>
		                 </td>
		                 <td>
							<input style="width: 143px; float:left" id="placeName"
							name="userEx.placeName" value='<s:property value="userEx.placeName"/>'
							class="normalInput" type=text onkeyup=keyup(2)
							onfocus="inputFocused('placeName', '<fmt:message key="onfocus.pleaseSelectPlaceName" bundle="${workbench}"/>', 'normalInputFocus'),keydown(2)"
							onblur="inputBlured(this, 'normalInput');"
							onclick="clearOrSetTips(this);" />
							<a id="selectRoleBt" onclick="selectPostClick();" href="javascript:;" class="nearInputBtn"><span><fmt:message key="button.select" bundle="${workbench}"/></span></a>
		                 </td>
		             </tr>
		             <tr>
		             	<td class="rightBlueTxt">@SYSTEM.multipleIP@:</td>
		             	<td>
		             		<s:if test="userEx.allowMutliIpLogin">
		             			<img src="../images/speOn.png" name="on" onclick="changeIPLoad(this)" class="cursorPointer" />
		             		</s:if>
		             		<s:else>
		             			<img src="../images/speOff.png" name="off" onclick="changeIPLoad(this)" class="cursorPointer" />
		             		</s:else>
		             		
		             	</td>
		             	<td class="rightBlueTxt">@SYSTEM.userSessionTime@:</td>
		             	<td>
		             		<input id="sessionTime" style="ime-mode:disabled" type="text" class="normalInput w200" value="<s:property value="userEx.timeout"/>" 
		             		name="userEx.timeout" toolTip="@SYSTEM.sessionTimeTip@" /> @platform/sys.minutes@
		             	</td>
		             </tr>
		         </tbody>
		     </table>
				<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
			         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
			         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>
</body>
</Zeta:HTML>
