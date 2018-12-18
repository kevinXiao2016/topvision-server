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
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = "${entityId}";
//var groupDesc = unescape(${groupDesc});

//验证描述
function checkGroupDesc(groupDesc){
	reg = /^[a-zA-Z\d\~\!\@\#\$\%\^\&\*\+\;\,\?\=\|\<\>\`\{\}\-_\'\"\[\]()\/\.:\\]{1,31}$/;
	if(reg.test(groupDesc)){
		return true;
	}else{
		return false;
	}
}

function saveClick(){
	var groupIndex = $("#groupIndex").val();
	var groupDesc = $("#groupDesc").val();
	if(!checkGroupDesc(groupDesc)){
		$("#groupDesc").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@",  "@ISOGROUP.onModifying@");
	$.ajax({
       url: '/epon/isolationgroup/modifyGroup.tv',
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
       	      	html: '<b class="orangeTxt">@ISOGROUP.modifySuccess@</b>'
       	    });
    	    window.parent.getFrame("entity-" + entityId).reloadData(groupIndex);
           	cancelClick();
       }, error: function() {
       		window.parent.showMessageDlg("@COMMON.tip@", "@ISOGROUP.modifyFail@",'error')
   	   }, cache: false
	});
}

function cancelClick(){
	window.top.closeWindow("modifyGroupPage");
}

$(function(){
	//$("#groupDesc").val($("#desc").text());
});
</script>
</HEAD>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ISOGROUP.modifyGroup@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div id="desc" style="display: none">${groupDesc}</div>
		<div class="edgeTB10LR20 pT40">
			<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt w180"><label for="groupIndex">@ISOGROUP.groudId@:</label></td>
				<td><input type="text"  class="normalInput normalInputDisabled w200" id="groupIndex" value="${groupIndex}" disabled>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@ISOGROUP.groudDesc@:</label></td>
				<td>
					<input type="text"  class="normalInput w200" id="groupDesc" maxlength="31" value="${groupDesc}" tooltip="@ISOGROUP.groupDescTip@">
				</td>
			</tr>
		</table>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT30 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" id="saveBt" onclick="saveClick()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>