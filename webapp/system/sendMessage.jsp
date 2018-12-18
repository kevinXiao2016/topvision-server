<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	module billboard
	PLUGIN RegionSelector
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript" src="sendMessage.js"></script>
<script type="text/javascript">
	var region;
/* 	$(function(){
		region = new Topvision.RegionCombo({
			renderTo : "regionSelector",
			width : 280
		})
	}) */
	function launch(){
		var v1 = $("#deadline").val(),
		v2 = $("#NoticeInput").val();
		if(v1.length < 1){
			$("#deadline").focus();
			return;
		}
		if(v2.length < 1){
			$("#NoticeInput").focus();
			return;
		}
		//var regionId =  region.folderId;
		var content = $("#NoticeInput").val();
		var deadline = $("#deadline").val();
		$.ajax({
			url:'/billboard/createNotice.tv',cache:false,
			data:{
				//regionId :  regionId,
				content : content,
				deadline : deadline
			},success:function(){
				top.afterSaveOrDelete({
			      title: '@COMMON.tip@',
			      html: '<b class="orangeTxt">@lanuchOk@</b>'
			    });
				//window.top.showMessageDlg("@COMMON.tip@", "@lanuchOk@");
				cancelClick();
			},error:function(exception){
				if(exception.simpleName == "InvalidNoticeException"){
					window.top.showMessageDlg("@COMMON.tip@", "@lessThanDealine@");
				}else{
					window.top.showMessageDlg("@COMMON.tip@", "@lanuchEr@");
				}
			}
		});
	}
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}
</script>
</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<div class="openWinTip">@messageTip@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0"
				border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt" width="140">@lanucher@@COMMON.maohao@</td>
						<td><input type="text" class="normalInputDisabled w280"
							disabled="disabled" value="<%=CurrentRequest.getCurrentUser().getUser().getUserName() %>" /></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@deadline@@COMMON.maohao@</td>
						<td><input type="text" id="deadline" class="normalInput w280" toolTip="@COMMON.canNotBeEmpty@" onclick="WdatePicker({isShowToday :false, isShowClear: false, readOnly: false, dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						</td>
					</tr>
					<!-- <tr>
						<td class="rightBlueTxt"></td>
						<td><div id="regionSelector" ></div></td>
					</tr> -->
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@content@@COMMON.maohao@</td>
						<td><textarea id="NoticeInput" maxLength="40"  toolTip="@COMMON.canNotBeEmpty@"
								class="normalInput" style="height: 40px; width: 278px;"></textarea>
							<p id="tip1" class="pT5">@contentTip@</p></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<Zeta:ButtonGroup>
				<Zeta:Button onClick="launch()" icon="miniIcoSaveOK">@lanuch@</Zeta:Button>
				<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
			</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>