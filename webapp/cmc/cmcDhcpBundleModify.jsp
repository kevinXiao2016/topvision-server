<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.DhcpBundleCfg'/></TITLE>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/IpTextField.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var cmcId = <s:property value='cmcId'/>;
var modifyFlag = <s:property value='modifyFlag' />;
var policy = '<s:property value='type'/>';
var dhcpBundleList = ${dhcpBundleList};

function saveClick(){
	var url;
	var tmpStr;
	var bundleStr = $("#bundleSelect").val();
	var tmpType = $("#policySelect").val();
	if (modifyFlag == 0){
		var i = 0;
		for(i=0; i<dhcpBundleList.length; i++){
			if(dhcpBundleList[i].topCcmtsDhcpBundleInterface == bundleStr){
				window.top.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.bIdExists);
				return;
			}
		}
	}
	if(modifyFlag == 0){//新增
		tmpStr = I18N.CMC.text.add;
		url = '/cmc/dhcp/addDhcpBundle.tv';
	}else if(modifyFlag == 1){//修改
		tmpStr = I18N.CMC.text.Modify;
		url = '/cmc/dhcp/modifyDhcpBundle.tv';
	}
	window.top.showWaitingDlg(I18N.CMC.title.wait, I18N.CMC.text.doing+ tmpStr +I18N.CMC.text.isSetting, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(text) {
			if (text.responseText != "success") {
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetFailed);
			}else{
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetSuccess);
				window.parent.getFrame("entity-" + cmcId).onRefreshClick();
				cancelClick();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetFailed);
		},
		params : {
			cmcId: cmcId,
		    type: tmpType,
		    bundle: bundleStr
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpBundleModify');
}
Ext.onReady(function(){
	var bundlePosition = Zeta$('bundleSelect');
	for(var i = 1,len = bundlePosition.length;i <=len; i++)
    {
		bundlePosition.options[1] = null;
    }
	if(modifyFlag == 0){
		var dhcpBundleAlArray = new Array(0,0,0,0,0,0);
 		for(i = 0; i < dhcpBundleList.length; i++) {
 			if(dhcpBundleList[i].topCcmtsDhcpBundleInterface.split('.')[1] == undefined){
 				dhcpBundleAlArray[0] = 1;
 			}else{
 				len = parseInt(dhcpBundleList[i].topCcmtsDhcpBundleInterface.split('.')[1]);
 				dhcpBundleAlArray[len] = 1;
 			}
 		}
	 	if(dhcpBundleAlArray[0]==0){
		 	var option = document.createElement('option');
	 	 	option.value = 'bundle1';
	 	 	option.text = 'bundle1';
	 	 	try {
	 	 		bundlePosition.add(option, null);
	 	 	} catch(ex) {
	 	 	    bundlePosition.add(option);
	 	 	}
	 	}
	 	for(i = 1; i < 6; i++) {
	 		if(dhcpBundleAlArray[i]==0){
	 	 		var option = document.createElement('option');
	 	 		option.value = 'bundle1.'+i;
	 	 		option.text = 'bundle1.'+i;
	 	 		try {
	 	 			bundlePosition.add(option, null);
	 	 	    } catch(ex) {
	 	 	        bundlePosition.add(option);
	 	 	    }
	 		}
	 	}
		$("#okBt").text(I18N.CMC.text.addSetting);
		$("#bundleSelect").focus();
	}
	if (modifyFlag == 1){
		var option = document.createElement('option');
	 	option.value = "<s:property value='bundle' />";
	 	option.text = "<s:property value='bundle' />";
	 	option.selected = true;
	 	try {
	 		bundlePosition.add(option, null);
	 	} catch(ex) {
	 	    bundlePosition.add(option);
	 	}
		$("#bundleSelect").attr("disabled", true);
	}
});
function changeBt(){
	if(!checkIsFilled() || checkTheSame()){
		$("#okBt").attr("disabled",true);
		$("#okBt").mouseout();
		return;
	}
	$("#okBt").attr("disabled",false);
}
function checkIsFilled(){
	if($("#bundleSelect").val()==0 || $("#policySelect").val()==0){
		return false;
	}
	return true;
}
function checkTheSame(){
	if($("#policySelect").val()!=policy){
		return false;
	}
	return true;
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div>
		<fieldset
			style='width: 100%; height: 152;background-color: #ffffff'>
			<table cellspacing=12 cellpadding=0>
				<tr>
					<td>Bundle ID:</td>
					<td align=left><select id="bundleSelect" style="width: 131px;" name="bundle" onclick="changeBt()">
						<option value='0'><fmt:message bundle='${cmc}' key='CMC.text.pleaseselec'/></option>
						</select>
					</td>
				</tr>
				<tr>
					<td><fmt:message bundle='${cmc}' key='CMC.text.Policy'/></td>
					<td align=left><select id="policySelect" style="width: 131px;" name="type" onclick="changeBt()">
							<option value="0" <s:if test="type==0">selected</s:if>><fmt:message bundle='${cmc}' key='CMC.text.pleaseselec'/></option>
							<option value="1" <s:if test="type==1">selected</s:if>>primary</option>
							<option value="2" <s:if test="type==2">selected</s:if>>policy</option>
							<option value="3" <s:if test="type==3">selected</s:if>>strict</option>
						</select>
					</td>
				</tr>
			</table>
		</fieldset>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<button id="okBt" class=BUTTON75 disabled
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle='${cmc}' key='CMC.label.ModifyCfg'/></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
		</div>

	</div>
</BODY>
</HTML>