<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    module camera
</Zeta:Loader>
<script type="text/javascript">
var noteReg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,120}$/;

	function okClick(){
		var mac = "${mac}";
		var type = "${type}";
		var note = $("#note").val();
		
		// validate
		if(!note || $.trim(note).toLowerCase() === 'null' || !noteReg.test(note)){
	        return $("#note").focus();
	    }
		
		$.ajax({
			url:"/camera/modifyPhyInfo.tv", cache:false,type:"post",
			data:{
				mac: mac,
				type : type,
				note : note
			},success:function(){
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: "@CAMERA.mdfPhyOk@"
				});
				cancelClick();
			},error:function(){
				top.showMessageDlg("@COMMON.error@", "@CAMERA.mdfPhyEr@");
			}
		})
	}
	
	function cancelClick(){
		top.closeWindow("editcameraInfo");
	}
</script>
</head>
<body class="openWinBody" >
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@CAMERA.editPhy@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w200">MAC</td>
				<td >${mac}</td>
			</tr>
			<tr class="darkZebraTr">
				<td  class="rightBlueTxt">@CAMERA.type@</td>
				<td>${type}</td>
			</tr>
			<tr>
				<td  class="rightBlueTxt">@COMMON.note@</td>
				<td><input type="text" class="normalInput w200" id="note" value="${note}" tooltip="@CAMERA.noteTip@" maxlength="120"/></td>
			</tr>
		</table>
	</div>
	
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick()" icon="miniIcoSaveOK">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>