<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library zeta
	library jquery
	module mobile
</Zeta:Loader>
<style type="text/css">
.readonlyInput {
	background-color: #DCDCDC;
}
#formChanged input[type="text"]{
	width: 380px;
	padding-left: 5px;
}
</style>

<script type="text/javascript">

	function cancelClick() {
		window.top.closeWindow('modifyMobileDeviceType');
	}
	
	function changeCheck() {
		var changeTag = false;
		if (frequency != $("#frequency").val()
				|| powerlevel != $("#powerlevel").val()) {
			changeTag = true;
		} else {
			changeTag = false;
		}

		if (changeTag == true) {
			$("#saveBtn").attr("disabled", false);
		} else {
			$("#saveBtn").attr("disabled", true);
		}
	}
	
	function saveClick() {
		object = Zeta$('frequency').value.trim();
		if (object == null || object.length > 512) {
			window.top.showMessageDlg('@RECYLE.tip@',  '@frquencyError@');
			return;
		}
		if (object == null || object.length <1 ) {
			window.top.showMessageDlg('@RECYLE.tip@', '@frquencyNotNull@');
			return;
		}
		
		object = Zeta$('powerlevel').value.trim();
		if (object == null || object.length > 512) {
			window.top.showMessageDlg('@RECYLE.tip@', '@powerlevelError@');
			return;
		}
		if (object == null || object.length < 1) {
			window.top.showMessageDlg('@RECYLE.tip@', '@powerlevelNull@');
			return;
		}

		
		$.ajax({
			url : '/mobile/modifyMobileDeviceType.tv',
			type : 'post',
			data : jQuery(formChanged).serialize(),
			success : function(response) {
				top.afterSaveOrDelete({
	   				title: '@RECYLE.tip@',
	   				html: '<b class="orangeTxt">' + '@text.modifySuccessTip@' + '</b>'
	   			});
				if(window.parent.getFrame("mobileDeviceConfigDlg")!= undefined){
					window.parent.getFrame("mobileDeviceConfigDlg").onRefreshClick();
				}
				cancelClick();
			},
			error : function(response) {
				window.parent.showMessageDlg('@RECYLE.tip@',
						'@text.modifyFailureTip@');
			},
			cache : false
		});
			
		
	}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT20">
		<form name="formChanged" id="formChanged">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt" width="120">@deviceType@:</td>
						<td>
							<input class="normalInputDisabled" type="hidden" id="typeId" 
								name="typeId"
								value='${typeId}'/>
							<input class="normalInputDisabled" type="text" id="deviceType" 
								name="deviceType"
								value='${deviceType}' readonly="readonly" />
						</td>
						<td>
							
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt" width="120">@corporation@:</td>
						<td>
							<input class="normalInputDisabled" type="text" id="corporation" 
								name="corporation"
								value='${corporation}' readonly="readonly" />
						</td>
						<td>
							
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">
							<span style="color:red;">*</span>
							@frequency@:
						</td>
						<td>
							<input class="normalInput" type="text" id="frequency" name="frequency"
								value='${frequency}'
								toolTip='@frequencyOrder@' />
						</td>
						<td>
							MHz
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">
							<span style="color:red;">*</span>
							@powerlevel@:
						</td>
						<td>
							<input class="normalInput" type="text" id="powerlevel" name="powerlevel"
							value='${powerlevel}'
							toolTip='@powerlevelOrder@' />
						</td>
						<td>
							dBuV
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
    	<ol class="upChannelListOl pB0 pT30 noWidthCenter">
    		<li>
    			<a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
    				<span><i class="miniIcoData"></i>@COMMON.save@</span>
    			</a>
    		</li>
         	<li>
         		<a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
         			<span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
         		</a>
         	</li>
		</ol>
	</div>
</body>
</Zeta:HTML>
