<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.DhcpIntIpCfg'/></TITLE>
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
var serverIpMask = '<s:property value='ipMask' />';
var ifIndex = <s:property value='ifIndex' />;
var serverId = <s:property value='index' />;
var indexList = ${indexList};
indexList = indexList.join("")=="false" ? new Array() : indexList;
var ifIndexList = ${ifIndexList};
ifIndexList = ifIndexList.join("")=="false" ? new Array() : ifIndexList;

var ip;
var ipMask;

function saveClick(){
	if(checkTheSameId()){
		window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.cfgIsExists);
		return;
	}
	var url;
	var tmpStr;
	var tmpId = $("#index").val();
	var tmpIfIndex = $("#ifIndex").val();
	var tmpIp = ip.getValue();
	var tmpIpMask = ipMask.getValue();
	if(modifyFlag == 0){//新增
		tmpStr = I18N.CMC.text.add;
		url = '/cmc/dhcp/addDhcpIntIp.tv?r=' + Math.random();
	}else if(modifyFlag == 1){//修改
		tmpStr =I18N.CMC.text.add ;
		url = '/cmc/dhcp/modifyDhcpIntIp.tv?r=' + Math.random();
	}
	window.top.showWaitingDlg(I18N.CMC.title.wait , I18N.CMC.text.doing+ tmpStr +I18N.CMC.text.isSetting, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(text) {
			if (text.responseText != "success") {  
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr +I18N.CMC.text.isSetting);
			}else{
				window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.DhcpIntIpSetSuccess);
				var par = window.parent.getWindow("dhcpIntIp").body.dom.firstChild.contentWindow;
				if(modifyFlag == 0){
					par.data.unshift([tmpId, tmpIp, tmpIpMask, tmpIfIndex]);
					par.store.loadData(par.data);
					par.grid.getSelectionModel().selectFirstRow();
				}else if(modifyFlag == 1){
					var tmpN = par.store.indexOf(par.grid.getSelectionModel().getSelected());
					if(par.data[tmpN][0] == tmpId){
						par.data[tmpN][1] = tmpIp;
						par.data[tmpN][2] = tmpIpMask;
						par.data[tmpN][3] = tmpIfIndex;
						par.store.loadData(par.data);
						par.grid.getSelectionModel().selectRow(tmpN, true);
					}else{
						var tmpN2 = -1;
						for(var k=0; k<par.data.length; k++){
							if(par.data[k][0] == tmpId){
								tmpN2 = k;
							}
						}
						if(tmpN2 == -1){
							par.location.reload();
						}else{
							par.data[tmpN2][1] = tmpIp;
							par.data[tmpN2][2] = tmpIpMask;
							par.data[tmpN2][3] = tmpIfIndex;
							par.store.loadData(par.data);
							par.grid.getSelectionModel().selectRow(tmpN, true);
						}
					}
				}
				cancelClick();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.CMC.title.tip, tmpStr + I18N.CMC.text.isSetting);
		},
		params : {
			cmcId: cmcId,
			index: tmpId,
		    ifIndex: tmpIfIndex,
		    ip: tmpIp,
		    ipMask: tmpIpMask
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpIntIpModify');
}
Ext.onReady(function(){
	ip = new ipV4Input("ip","span1");
	ip.width(131);
	ip.onChange = function(){
		changeBt();
	}
	ipMask = new ipV4Input("ipMask","span2");
	ipMask.width(131);
	ipMask.onChange = function(){
		changeBt();
	}
	if(modifyFlag == 0){
		$("#index").attr("disabled",false);
		$("#ifIndex").attr("disabled",false);
		$("#okBt").text(I18N.CMC.text.addSetting);
	}else if(modifyFlag == 1){
		if(serverId == -1){
			$("#index").val(I18N.CMC.text.DataGetErrorInfo);
		}else{
			$("#index").val(serverId);
		}
		if(serverIp == "noData"){
			serverIp = "";
		}
		ip.setValue(serverIp);
		if(serverIpMask == "noData"){
			serverIpMask = "";
		}
		ipMask.setValue(serverIpMask);
		if(ifIndex == -1){
			ifIndex = "";
		}
		$("#ifIndex").val(ifIndex);
	}
	if(modifyFlag == 1){
		$("#index").focus();
	}else if(modifyFlag == 0){
		ipFocus("ip", 1);
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
	if($("#ifIndex").val()==null || $("#ifIndex").val()==undefined || !checkedIfIndex()){
		return false;
	}
	if($("#index").val()=="" || !checkedId()){
		$("#index").css("border","1px solid #ff0000");
		return false;
	}else{
		$("#index").css("border","1px solid #8bb8f3");
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
function checkedIfIndex(){
	var index = $("#ifIndex").val();
	return true;
}
function checkTheSame(){
	if(serverIp!=ip.getValue() || serverIpMask!=ipMask.getValue() || ifIndex!=$("#ifIndex").val() || serverId!=$("#index").val()){
		return false;
	}
	return true;
}
function checkTheSameId(){
	var id = parseInt($("#index").val());
	var ifId = parseInt($("#ifIndex").val());
	if(indexList.indexOf(id) > -1 && ifIndexList.indexOf(ifId) > -1){
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
					<td width=60px align=right>ID:</td>
			    	<td align=left><input id="index" disabled style="width:131px;border:1px solid #8bb8f3;"  onkeyup="changeBt()" /></td>
				</tr>
				<tr>
					<td width=60px align=right><label><fmt:message bundle='${cmc}' key='CMC.text.assPort'/>:</label></td>
					<td align=left>
						<input id="ifIndex" style="width:131px;border:1px solid #8bb8f3;" disabled onkeyup="changeBt()" />
					</td>
				</tr>
				<tr>
					<td align=right width=60px><fmt:message bundle='${cmc}' key='CMC.text.ipAddress'/>:</td>
                 	<td align=left><span id="span1"></span></td>
				</tr>
				<tr>
					<td align=right width=60px><fmt:message bundle='${cmc}' key='CMC.text.ipMask'/>:</td>
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