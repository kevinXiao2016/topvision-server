<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.DhcpGiAddrCfg'/></TITLE>
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
var serverIp = '<s:property value='ip'/>';
var serverIpMask ='<s:property value='ipMask'/>';
var serverType = "<s:property value='type' />";
var giAddrId = '<s:property value="index" />';
var dhcpBundleList = ${dhcpBundleList};
var dhcpGiAddrList = ${dhcpGiAddrList};

var ip;
var ipMask;

function saveClick(){
	var url;
	var tmpStr;
	var tmpBundle = $("#bundleSelect").val();
	var tmpIpMask = getIpValue("ipMask");
	var tmpIp = getIpValue("ip");
	if(modifyFlag == 0){//新增
		tmpStr = I18N.CMC.text.add;
		url = '/cmc/dhcp/addDhcpGiAddr.tv';
	}else if(modifyFlag == 1){//修改
		tmpStr = I18N.CMC.text.Modify;
		url = '/cmc/dhcp/modifyDhcpGiAddr.tv';
	}
	window.top.showWaitingDlg(I18N.CMC.title.wait, I18N.CMC.text.doing+ tmpStr +I18N.CMC.text.isSetting, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(text) {
			if (text.responseText != "success") {  
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetFailed);
			}else{
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetSuccess);
				cancelClick();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpBundleSetFailed);
		},
		params : {
			cmcId: cmcId,
			index: giAddrId,
		    ip: tmpIp,
		    ipMask: tmpIpMask,
		    bundle: tmpBundle,
		    type: serverType
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpGiAddrModify');
}
Ext.onReady(function(){
  	ip = new ipV4Input("ip","span1");
	ip.width(131);
	ipMask = new ipV4Input("ipMask","span2");
	ipMask.width(131);
	ip.onChange = function(){
		changeBt();
	}
	ipMask.onChange = function(){
		changeBt();
	}
	if(modifyFlag == 0){
		/* $("#index").attr("disabled",false); */
		$("#okBt").text(I18N.CMC.text.addSetting);
	}else if(modifyFlag == 1){
		if(serverIp == "noData"){
			serverIp = "";
		}
		ip.setValue(serverIp);
		if(serverIpMask == "noData"){
			serverIpMask = "";
		}
		ipMask.setValue(serverIpMask);
	}
	var bundleObject = Zeta$('bundleSelect');
	for(var i = 1,len = bundleObject.length;i <=len; i++)
    {
		bundleObject.options[1] = null;
    }
	var primaryNum;
	var policyNum;
	var strictNum;
	if (modifyFlag == 1){
		for(var i = 0; i < dhcpBundleList.length; i++) {
			var option = document.createElement('option');
			option.value = dhcpBundleList[i].topCcmtsDhcpBundleInterface;
			option.text = dhcpBundleList[i].topCcmtsDhcpBundleInterface;
			try {
				bundleObject.add(option, null);
		    } catch(ex) {
		       bundleObject.add(option);
		    }
		}
		$("#bundleSelect option").each(function(){
			if($(this).val()=='<s:property value="bundle" />'){
				$(this).attr("selected", "selected");
				$("#bundleSelect").attr("disabled", true);
			}
		});
	}else{
		for(var i = 0; i < dhcpBundleList.length; i++) {
			primaryNum = 0;
			policyNum = 0;
			strictNum = 0;
			for (j = 0; j<dhcpGiAddrList.length;j++){
				if(dhcpGiAddrList[j].topCcmtsDhcpBundleInterface == dhcpBundleList[i].topCcmtsDhcpBundleInterface){
					if(dhcpGiAddrList[j].topCcmtsDhcpGiAddrPolicyType == 1){
						primaryNum = primaryNum + 1;
					}
					if(dhcpGiAddrList[j].topCcmtsDhcpGiAddrPolicyType == 2){
						policyNum = policyNum + 1;
					}
					if(dhcpGiAddrList[j].topCcmtsDhcpGiAddrPolicyType == 3){
						strictNum = strictNum + 1;
					}
				}
			}
			if(dhcpBundleList[i].topCcmtsDhcpBundlePolicy == 2 || (dhcpBundleList[i].topCcmtsDhcpBundlePolicy == 1 && primaryNum == 0) || dhcpBundleList[i].topCcmtsDhcpBundlePolicy  == 3 && strictNum < 4){
				var option = document.createElement('option');
				option.value = dhcpBundleList[i].topCcmtsDhcpBundleInterface;
				option.text = dhcpBundleList[i].topCcmtsDhcpBundleInterface;
				try {
					bundleObject.add(option, null);
				} catch(ex) {
					 bundleObject.add(option);
				}
			}
		}	
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
	if($("#bundleSelect").val()==0){
		return false;
	}
	if(!ipIsFilled("ip")){
		setIpBorder("ip","red");
		return false;
	}else{
		setIpBorder("ip","default");
	}
	if(!ipIsFilled("ipMask")){
		setIpBorder("ipMask","red");
		return false;
	}else{
		setIpBorder("ipMask","default");
	}
	return true;
}
function checkedId(){
	var id = $("#index").val();
	var reg = /^([\d]{1,5})$/g;
	if(!isNaN(id) && reg.exec(id)){
		return true;
	}
	return false;
}
function checkTheSame(){
	/* if(serverIp!=ip.getValue() || serverType!=$("#type").val() || serverId!=$("#index").val()){ */
	if(serverIp!=ip.getValue() || serverIpMask!=ipMask.getValue()){
		return false;
	}
	return true;
}
function checkTheSameId(){
	var id = parseInt($("#index").val());
	if(indexList.indexOf(id) > -1){
		return true;
	}else{
		return false;
	}
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
							<option value="0"><fmt:message bundle='${cmc}' key='CMC.text.pleaseselec '/></option>
						</select>
					</td>
				</tr>
				<tr>
					<td><fmt:message bundle='${cmc}' key='CMC.text.RelayIp'/>:</td>
					<td align=left><span id="span1"></span></td>
				</tr>
				<tr>
					<td><fmt:message bundle='${cmc}' key='CMC.text.subNetMask'/>:</td>
					<td align=left><span id="span2"></span></td>
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