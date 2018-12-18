<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CM
</Zeta:Loader>
<script type="text/javascript">
var readCommunity = '<s:property value="readCommunity"/>';
var writeCommunity = '<s:property value="writeCommunity"/>';
var ipMode = '<s:property value="ipMode"/>';
function okClick(){
	var readCommunity = $("#readCommunity").val();
	var writeCommunity = $("#writeCommunity").val();
	var ipMode = $("#ipMode").val();
	var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/
	if(readCommunity.length<1){
		//window.top.showMessageDlg("@COMMON.tip@", "@cmPoll.pollReadCommunityNotNull@");
		$("#readCommunity").attr('tooltip','@cmPoll.pollReadCommunityNotNull@');
		$("#readCommunity").focus();
		return false;
	}else{
		$("#readCommunity").attr('tooltip','@cmPoll.pollReadCommunityNotLongerThan32@');
	}
	if(writeCommunity.length<1){
		//window.top.showMessageDlg("@COMMON.tip@", "@cmPoll.pollWriteCommunityNotNull@");
		$("#writeCommunity").attr('tooltip','@cmPoll.pollWriteCommunityNotNull@');
		$("#writeCommunity").focus();
		return false;
	}else{
		$("#writeCommunity").attr('tooltip','@cmPoll.pollWriteCommunityNotLongerThan32@');
	}
	if(writeCommunity.length>32 || !reg.test(writeCommunity)){
		//window.top.showMessageDlg("@COMMON.tip@", "@cmPoll.pollWriteCommunityNotLongerThan32@");
		$("#writeCommunity").attr('tooltip','@cmPoll.pollWriteCommunityNotLongerThan32@');
		$("#writeCommunity").focus();
		return false;
	}
	if(readCommunity.length>32 || !reg.test(readCommunity)){
		//window.top.showMessageDlg("@COMMON.tip@", "@cmPoll.pollReadCommunityNotLongerThan32@");
		$("#readCommunity").attr('tooltip','@cmPoll.pollReadCommunityNotLongerThan32@');
		$("#readCommunity").focus();
		return false;
	}
	$.ajax({
		url: '/cmpoll/modifyCmPollConfig.tv?readCommunity=' + readCommunity + '&writeCommunity=' + writeCommunity + '&ipMode=' + ipMode,
	      type: 'post',
	      success: function(response) {
	  	    	if(response.success){
	  	    		top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@cmPoll.modifySuccess@</b>'
	   	   			});
	  	    	}else{
	  	    		window.parent.showMessageDlg("@COMMON.tip@", "@cmPoll.modifyFail@");
	  	    	}
			}, error: function(response) {
				window.parent.showMessageDlg("@COMMON.tip@", "@cmPoll.modifyFail@");
			}, cache: false
	}); 
}

Ext.onReady(function() {
	$("#readCommunity").val(readCommunity);
	$("#writeCommunity").val(writeCommunity);
	$("#ipMode").val(ipMode);
})
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<form onsubmit="return false;">
			
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="6" class="txtLeftTh">@cmPoll.cmConfig@</th>
					</tr>
				</thead>
				<tr>
					<td width=120 class="rightBlueTxt"><label for="name">@cmPoll.readCommunity@: <font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" style="width: 180px" id="readCommunity" tooltip="@cmPoll.pollWriteCommunityNotLongerThan32@" /></td>
					<td width=120 class="rightBlueTxt">
						<label for="name">@cmPoll.writeCommunity@: <font color=red>*</font></label>
					</td>
					<td>
						<input type="text" class="normalInput" style="width: 180px" id="writeCommunity" tooltip="@cmPoll.pollWriteCommunityNotLongerThan32@"/>
					</td>
					<td width=120  class="rightBlueTxt">@cmPoll.getIpStyle@: <font color=red>*</font></td>
					<td>
						<select id="ipMode" class="modifiedFlag normalSel" class="normalSel" disabled style="width: 180px">
							<option value="1">@cmPoll.byDhcp@</option>
							<option value="2" selected>@cmPoll.byNM3000@</option>
						</select>
					</td>
				</tr>
			</table>
			<Zeta:ButtonGroup>
				<Zeta:Button onClick="okClick();" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			</Zeta:ButtonGroup>
		</form>
	</div>
	
	
</body>
</Zeta:HTML>