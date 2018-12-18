<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY ext
    LIBRARY jquery
    MODULE camera
    IMPORT js.tools.ipText static
</Zeta:Loader>

<script type="text/javascript">
	var preIp = '${ip}';
	
	var locationReg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,120}$/;
	
	$( DOC ).ready(function(){
		WIN.ipInput = new ipV4Input("ip","ipAddCt");
		ipInput.width(200);
		setIpValue("ip", preIp);
	});

	function okClick(){
		var cameraNo = "${cameraNo}";
		var ip = getIpValue("ip");
		var location = $("#location").val();
		if(!location || $.trim(location).toLowerCase() === 'null' || !locationReg.test(location)){
			return $("#location").focus();
		}
		if(!ip || !ipIsFilled("ip")){
			return top.showMessageDlg("@COMMON.error@", "@CAMERA.ipNotNull@", null, function(){
				ipFocus("ip", 1);
			});
		}
		$.ajax({
			url:"/camera/modifyCameraPlan.tv", cache:false,type:"post",
			data:{
				cameraNo: cameraNo,
				ip : ip,
				location : location
			},success:function(){
				top.nm3kRightClickTips({
    				title: "@COMMON.tip@", html: "@CAMERA.mdfPlanOk@"
    			});
				cancelClick();
			},error:function(){
				top.showMessageDlg("@COMMON.error@", "@CAMERA.mdfPlanEr@");
			}
		})
	}
	
	function cancelClick(){
		top.closeWindow("editplanningInfo");
	}
</script>
</head>
<body class="openWinBody" >
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@CAMERA.editPlanInfoTip@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w180">@CAMERA.cameraNo@:</td>
				<td >${cameraNo}</td>
			</tr>
			<tr class="darkZebraTr">
				<td  class="rightBlueTxt w180">IP:</td>
				<td>
					<%-- <input type="text" class="normalInput w200" id="ip" value="${ip}" /> --%>
					<span id="ipAddCt" ></span>
				</td>
			</tr>
			<tr>
				<td  class="rightBlueTxt w180">@COMMON.location@:</td>
				<td><input type="text" class="normalInput w200" id="location" value="${location}" tooltip="@CAMERA.noteTip@" maxlength="120"/></td>
			</tr>
		</table>
	</div>
	
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick()" icon="miniIcoSaveOK">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>