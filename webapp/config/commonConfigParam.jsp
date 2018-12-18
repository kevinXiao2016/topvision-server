<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
    module network
    IMPORT js/nm3k/Nm3kSwitch
</Zeta:Loader>
<script type="text/javascript">
var supportCmc = false;
<% if(uc.hasSupportModule("cmc")){%>
	supportCmc = true;
<% } %>
var sendCommandInterval = '<s:property value="sendCommandInterval"/>';
var pollInterval = '<s:property value="pollInterval"/>';
var autoSendConfigSwitch = ('<s:property value="autoSendConfigSwitch"/>' == 'true') ? 'true' : 'false';
var failAutoSendConfigSwitch = ('<s:property value="failAutoSendConfigSwitch"/>' == 'true') ? 'true' : 'false';
var imgBtn, failAutoImgBtn;

function isDigit(s) { 
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(s)) 
	return false; 
	return true; 
}

function save(){
	var sendConfigInterval = $("#sendConfigInterval").val();
    var pollInterval = $("#pollInterval").val();
    var autoSendConfigSwitch = imgBtn.getValue();
    var failAutoSendConfigSwitch = failAutoImgBtn.getValue();
	if(!isDigit(sendConfigInterval) || parseInt(sendConfigInterval, 10) < 10 || parseInt(sendConfigInterval, 10) > 5000){
		$("#sendConfigInterval").focus();
		return;
	}
	if(!isDigit(pollInterval) || parseInt(pollInterval, 10) < 1 || parseInt(pollInterval, 10) > 240){
		$("#pollInterval").focus();
		return;
	}
	$.ajax({
		  url: '/entity/commonConfig/modifyParamConfig.tv',
	      type: 'post',
	      data: {
	    	  sendCommandInterval: sendConfigInterval,
	    	  pollInterval: pollInterval * 1000 * 60,
	    	  autoSendConfigSwitch: autoSendConfigSwitch,
	    	  failAutoSendConfigSwitch : failAutoSendConfigSwitch
	      },
	      success: function(response) {
	    	  if(response == "success"){
  	  	    		window.location.reload();
  	  	    	}else{
  	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updatefailure);
  	  	    	}
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updatefailure);
			}, cache: false
		});
}


$(function(){
	if(!supportCmc){
		$('.jsNotSupportCmc').css({visibility: 'hidden'})
	}
	$("#sendConfigInterval").val(sendCommandInterval);
    $("#pollInterval").val(pollInterval / 1000 / 60)
    
    imgBtn = new Nm3kSwitch("putImgBtn", autoSendConfigSwitch, {
    	yesNoValue : ['true', 'false']
    });
    failAutoImgBtn = new Nm3kSwitch("failAutoImgBtn", failAutoSendConfigSwitch, {
    	yesNoValue : ['true', 'false']
    });
    imgBtn.init();
    failAutoImgBtn.init();
})
</script>
</head>
<body class="whiteToBlack">
	<div class="formtip" id="tips" style="display: none"></div>
	<div class="edge10">
			<p class="flagP clearBoth"><span class="flagInfo">@sendConfig.paramConfig@</span></p>
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th class="txtRightTh" style="text-align: right"></th>
					 <th colspan="5" style="text-align:left;"></th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt" width="220">@sendConfig.commandSendInterval@@COMMON.maohao@
					 </td>
					 <td><input id="sendConfigInterval" type="text" class="normalInput w120" tooltip='@sendConfig.sendIntervalTip@'/> @topoDiscovery.Millisecond@</td>
					 <td class="rightBlueTxt" width="220"><div class="jsNotSupportCmc">@sendConfig.autosendconfig@@COMMON.maohao@</div></td>
					 <td>
					 	<div id="putImgBtn" class="jsNotSupportCmc"></div>
					 </td>
				</tr>
				<tr>
					 <td class="rightBlueTxt" width="220"><div>@sendConfig.failAutoSend@@COMMON.maohao@</div></td>
					 <td>
					 	<div id="failAutoImgBtn"></div>
					 </td>
					  <td class="rightBlueTxt" width="180">@sendConfig.sendInterval@@COMMON.maohao@
					 </td>
					 <td><input id="pollInterval" type="text" class="normalInput w120" tooltip='@sendConfig.pollIntervalTip@'/> @label.minutes@</td>
				</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="save()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
			<div id='dataPolicyConfig' class="pB10"></div>
	</div>
	</body>
</Zeta:HTML>