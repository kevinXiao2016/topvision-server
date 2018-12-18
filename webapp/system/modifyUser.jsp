<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
	library ext
    library Zeta
    module  resources
    plugin DropdownTree
    import js.jquery.nm3kToolTip
</Zeta:Loader>
<%-- <script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script> --%>
<style>
.zebraTableRows td{padding:3px;}
</style>
<script>
var tempText = "";
var tempId = "";
var userEx = ${userJson};
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
//选择职位
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
//选择部门
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
//选择地域
function selectRegionClick() {
    window.parent.createDialog('regionDlg', I18N.SYSTEM.selectRegion, 600, 370, "system/popRegion.jsp", null, true, false,
            function() {
                var clipboard = window.parent.ZetaCallback;
                if (clipboard.type == 'ok') {
                    Zeta$('regionId').value = clipboard.selectedItemId;
                    Zeta$('regionName').value = clipboard.selectedItemText;
                }
                window.parent.ZetaCallback = null;
            });
}
//清除部门
function clearDepart(){
	$("#departmentName").val("");
	$("#departmentId").val(0);
}
//清除职位
function clearPost(){
	$("#placeName").val("");
	$("#placeId").val(0);
}
function okClick() {
	if(userEx.userId!=2){
		//获得可选根地域集合
		var rootRegionIds = $root_regions.getSelectedIds();
	    if(!rootRegionIds.length){
	    	window.top.showMessageDlg("@COMMON.tip@", "@SYSTEM.plsselectatleast1region@");
	        return;
	    }
	    $('#rootRegionIds').val(rootRegionIds);
	  	//获取当前根地域
	    var curRegionId = $cur_root_regions.getSelectedIds();
	    if(!curRegionId.length){
	    	window.top.showMessageDlg("@COMMON.tip@", "@SYSTEM.plsselectcurrentregion@");
	        return;
	    }else{
	    	curRegionId = curRegionId[0];
	    }
	    $('#curRegionId').val(curRegionId);
	}else{
		
	}
    
	//工作电话验证
	var workPhone = Zeta$('workPhone');
	if ( workPhone.value.trim() != '' && isNotPhoneNumber(workPhone.value.trim())) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.workTelephoneWrong);
		return;
	}

	//家庭电话验证
	var homePhone = Zeta$('homePhone');
	
	if ( homePhone.value.trim() != '' && isNotPhoneNumber(homePhone.value.trim())) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.homeTelephoneWrong);
		return;
	}

	//手机验证
	var mobilePhone = Zeta$('mobilePhone');
	if ( mobilePhone.value.trim() != '' && !mobileCheck(mobilePhone.value.trim())) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.cellphoneWrong);
		return;
	}
	//email验证
	var email = Zeta$('email');
	if (email.value.trim() != '' && !isEmail(email.value.trim())) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.emailWrong);
		return;
	}
	$.ajax({
		url: 'updateUserDetail.tv', type: 'POST',
		data: jQuery(userForm).serialize(),
		success: function(json) {
			window.top.getFrame('userList').onRefreshClick();
			top.afterSaveOrDelete({
		      title: I18N.COMMON.tip,
		      html: '<b class="orangeTxt">'+ I18N.COMMON.modifySuccess +'</b>'
		    });
			cancelClick();
		}, error: function(json) {
			window.top.showErrorDlg();
		}, cache: false
	});
}

function backText(s){
    var name;
    var id;
    if(s == 1){
		name = $("#regionName");
		id = $("#regionId");
	}else if(s == 2){
		name = $("#departmentName");
		id = $("#departmentId");
	}else if(s == 3){
		name = $("#placeName");
		id = $("#placeId");
	}
	tempText = name.val();
	tempId = id.val();
}
function keyup(s){
    var name;
    var id;
    if(s == 1){
		name = $("#regionName");
		id = $("#regionId");
	}else if(s == 2){
		name = $("#departmentName");
		id = $("#departmentId");
	}else if(s == 3){
		name = $("#placeName");
		id = $("#placeId");
	}
    name.val(tempText);
	id.val(tempId);
}

$(function(){
	if(userEx.userId!=2){
		//初始化可选根地域及当前根地域选择树
		window.$cur_root_regions = $('#cur_root_regions').dropdowntree({
			width:280,
			multi:false,
			url: '/topology/fetchUserCurRootFolders.tv?userId='+userEx.userId
		}).data('nm3k.dropdowntree');
		
		window.$root_regions = $('#root_regions').dropdowntree({
			width:280,
			yesAffectChild: true,
			noAffectChild: false,
	        yesAffectParent: false,
	        noAffectParent: true,
			onCheck: updateCheckable,
			switchMode: true,
			url: '/topology/fetchUserSwithableFolders.tv?userId='+userEx.userId
		}).data('nm3k.dropdowntree');	
		
		//设置可选根地域
		window.$root_regions && window.$root_regions.refresh();
		updateCheckable();
		//设置当前根地域的值
		$cur_root_regions.checkNodes([userEx.userGroupId]);
	}else{
		//admin的可选根地域仅为默认地域
		$('#root_regions').append('<input id="region_input" style="width: 280px;" disabled="disabled" class="normalInputDisabled">');
		$('#region_input').val(userEx.userGroupName);
		//admin的当前根地域仅为默认地域
		$('#cur_root_regions').append('<input id="cur_region_input" style="width: 280px;" disabled="disabled" class="normalInputDisabled">');
		$('#cur_region_input').val(userEx.userGroupName);
	}
	
	function updateCheckable(){
		if(window.$root_regions){
			//获取可选根地域的未被勾选的节点集合
			var nonSelectIds = $root_regions.getNotSelectedIds();
			//将当前根地域这些节点设置为不可勾选 
			$cur_root_regions.disableNodes(nonSelectIds);			
		}
	}
})
</script>
<body class="openWinBody">
	<form id="userForm" name="userForm">
		<input type=hidden id="departmentId" name="userEx.departmentId" value="<s:property value="userEx.departmentId"/>" />
		<input type=hidden id="placeId" name="userEx.placeId" value="<s:property value="userEx.placeId"/>" />
		<input type=hidden id="roleIds" name="userEx.roleIds" value="<s:property value="userEx.roleIds"/>" />
		<%-- <input type=hidden id="regionId" name="userEx.userGroupId" value="<s:property value="userEx.userGroupId"/>" /> --%>
		<input type=hidden id="userId" name="userEx.userId" value='<s:property value="userEx.userId"/>' />
		<input type="hidden" id="rootRegionIds" name="userEx.userGroupIdsStr" value="" />
		<input type="hidden" id="curRegionId" name="userEx.userGroupId" value="<s:property value="userEx.userGroupId"/>" />
		
		<div class="edgeTB10LR20 pT30">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="230"><label for="userName">@SYSTEM.userName@:</label></td>
		                <td>
		                    <input style="width: 280px;" disabled="disabled"
								id="userName" name="userEx.userName"
								value='<s:property value="userEx.userName"/>' class="normalInputDisabled" type=text
								maxlength=32 />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@SYSTEM.name@:</td>
		                <td>
		                    <input style="width: 280px;" name="userEx.familyName"
							value='<s:property value="userEx.familyName"/>' class="normalInput"
							type=text maxlength=32 />
		                </td>
		            </tr>
		             <tr>
		                <td class="rightBlueTxt" width="200">
		                    <label for="root_regions">@SYSTEM.rootRegion@:<font color=red>*</font></label>
		                </td>
		               	<td><div id="root_regions"></div></td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt" width="200">
		                    <label for="cur_root_regions">@SYSTEM.currentregion@:<font color=red>*</font></label>
		                </td>
		                <td><div id="cur_root_regions"></div></td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt" width="200">@SYSTEM.workTelephone@:</td>
		                <td>
		                    <input style="width: 280px;"
							name="userEx.workTelphone" id="workPhone"
							value='<s:property value="userEx.workTelphone"/>' class="normalInput"
							type=text maxlength=16 />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@SYSTEM.homeTelephone@:</td>
		                <td>
		                    <input style="width: 280px;" name="userEx.homeTelphone"
							id="homePhone" value='<s:property value="userEx.homeTelphone"/>'
							class="normalInput" type=text maxlength=16 />
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt" width="200">@SYSTEM.cellphone@:</td>
		                <td>
		                    <input style="width: 280px;" name="userEx.mobile"
							id="mobilePhone" value='<s:property value="userEx.mobile"/>'
							class="normalInput" type=text maxlength=16 /><font color=orange> @SYSTEM.confirmSMS@</font>
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@SYSTEM.email@:</td>
		                <td>
		                    <input style="width: 280px;" name="userEx.email" id="email"
								value='<s:property value="userEx.email"/>' class="normalInput" type=text
								maxlength=32 /><font color=orange> @SYSTEM.confirmEmail@</font>
		                </td>
		            </tr>
		            <%-- <tr>
		                <td class="rightBlueTxt" width="200">
		                    <label for="regionName">@SYSTEM.Region@:<font color=red>*</font></label>
		                </td>
		                <td>
		                    <input style="width: 222px" id="regionName" 
							name="userEx.userGroupName" toolTip='@SYSTEM.pleaseSelectCusRegion@'
							value='<s:property value="userEx.userGroupName"/>' class="normalInput floatLeft"
							type=text onkeyup=keyup(1) />
							<a id="selectRegionBt"  onclick="selectRegionClick();" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@...</span></a>
							
							
		                </td>
		            </tr> --%>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                    <label for="departmentName">@SYSTEM.department@:</label>
		                </td>
		                <td>
		                    <input disabled="disabled" style="width: 174px" id="departmentName" 
							name="userEx.departmentName" toolTip='@SYSTEM.pleaseSelectCusDepartment@'
							value='<s:property value="userEx.departmentName"/>' class="normalInputDisabled floatLeft"
							type=text onkeyup=keyup(2) />
							<a onclick="selectDepartmentClick();" id="selectDepartmentBt" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@</span></a>
							<a onclick="clearDepart()" href="javascript:;" class="nearInputBtn"><span>@COMMON.clear@</span></a>
							
					
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt" width="200">
		                    <label for="placeName">@SYSTEM.post@: </label>
		                </td>
		                <td>
		                    <input disabled="disabled" style="width: 174px" id="placeName" toolTip='@SYSTEM.pleaseSelectCusPost@'
							name="userEx.placeName" value='<s:property value="userEx.placeName"/>'
							class="normalInputDisabled floatLeft" type=text onkeyup=keyup(3) />
							<a id="selectPostBt" onclick="selectPostClick();" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@</span></a>
							<a onclick="clearPost()" href="javascript:;" class="nearInputBtn"><span>@COMMON.clear@</span></a>
							
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.save@</span></a></li>
			         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>

	</form>
</body>
</Zeta:HTML>