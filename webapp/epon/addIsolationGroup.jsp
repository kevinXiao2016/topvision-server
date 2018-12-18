<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    import js.EntityType
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = "${entityId}";
var typeId = "${typeId}";

$(function(){
	if(EntityType.is8602G_OLT(typeId)){
		$("#groupIndex").attr('tooltip', '@ISOGROUP.groupIdTipInfo@');
	}else{
		$("#groupIndex").attr('tooltip', '@ISOGROUP.groupIdTip@');
	}
})
//输入校验的整数
function checkInput(value){
	var reg1 = /^[1-4]\d*$/;
	var reg2 = /^[1-9]\d*$/;
	if(EntityType.is8602G_OLT(typeId)){
		if (reg1.exec(value) && parseInt(value) <= 4 && parseInt(value) >= 1) {
			return true;
		} else {
			return false;
		}
	}else{
		if (reg2.exec(value) && parseInt(value) <= 32 && parseInt(value) >= 1) {
			return true;
		} else {
			return false;
		}
	}	
}

/**
 * 检查index是否可用
 */
function checkIndexUseable(groupIndex){
	var useFlag = false;
	$.ajax({
		url: '/epon/isolationgroup/checkIndexUseable.tv',
		data: {
			entityId : entityId,
			groupIndex : groupIndex
		},
		type : 'post',
		dataType: 'json',
		success: function(json) {
			useFlag = json.useable;
		}, error: function() {
		   
		}, cache: false,
		async: false
	});
	return useFlag;
}

//用户输入时检查隔离组Id是否可用
function blurFn(){
	var groupIndex = $("#groupIndex").val().trim();
	if(!checkInput(groupIndex)){
		$("#errorTip").text("");
		$("#groupIndex").focus();
		return;
	}
	if(checkIndexUseable(groupIndex)){
		$("#errorTip").text("");
		$("#groupDesc").val("ISO_GROUP_" + groupIndex)
	}else{
		$("#errorTip").text("@ISOGROUP.grouIdUsed@");
		$("#groupIndex").focus();
	}
}

//验证描述
function checkGroupDesc(groupDesc){
	reg = /^[a-zA-Z\d\~\!\@\#\$\%\^\&\*\+\;\,\?\=\|\<\>\`\{\}\-_\'\"\[\]()\/\.:\\]{1,31}$/;
	if(reg.test(groupDesc)){
		return true;
	}else{
		return false;
	}
}

/**
 * 添加隔离组
 */
function saveClick(){
	var groupIndex = $("#groupIndex").val();
	var groupDesc = $("#groupDesc").val();
	if(!checkGroupDesc(groupDesc)){
		$("#groupDesc").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@",  "@ISOGROUP.onAdding@");
	$.ajax({
       url: '/epon/isolationgroup/addGroup.tv',
       data: {
    	   entityId : entityId,
    	   groupIndex : groupIndex,
		   groupDesc : groupDesc
       },
   	   type : 'post',
	   dataType: 'json',
       success: function() {
    	    top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@ISOGROUP.addSuccess@</b>'
       	    });
    	    window.parent.getFrame("entity-" + entityId).reloadData(groupIndex);
           	cancelClick();
       }, error: function() {
       		window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.addFail@",'error')
   	   }, cache: false
	});
}

function cancelClick(){
	window.top.closeWindow("addGroupPage");
}

</script>
</HEAD>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@ISOGROUP.addGroup@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT40">
		<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt w160"><label for="groupIndex">@ISOGROUP.groudId@:</label></td>
				<td>
					<input type="text"  class="normalInput w200" id="groupIndex" onblur="blurFn()">
				</td>
				<td width="150">
					<b id="errorTip" class="orangeTxt"></b>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="groupDesc">@ISOGROUP.groudDesc@:</label></td>
				<td>
					<input type="text"  class="normalInput w200" id="groupDesc" maxlength="31" tooltip="@ISOGROUP.groupDescTip@">
				</td>
				<td></td>
			</tr>
		</table>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" id="saveBt" onclick="saveClick()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>