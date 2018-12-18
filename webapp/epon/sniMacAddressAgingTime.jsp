<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>
var sniAddrAgingTime = '<s:property value="sniMacAddrAgingTime"/>'
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function initData() {
	if(sniAddrAgingTime == -1){
		$('#sniMacAddressAgingTime').val()
	}else{
		$('#sniMacAddressAgingTime').val(sniAddrAgingTime)
	}
	//操作权限-------------------------------
	if(!operationDevicePower){
	    $("#sniMacAddressAgingTime").attr("disabled",true);
	    $("#sysArpAgingTime").attr("disabled",true);
	    R.saveBtn.setDisabled(true);
	}
	//--------------------------------------
}
function checkValid() {
	var agingTime = parseInt($('#sniMacAddressAgingTime').val())
	var reg = /^([0-9])+$/
	if (!reg.exec(agingTime) || agingTime < 120 || agingTime > 50000) {//验证为：0或者120-50000;
		if(agingTime != 0){
			$("#sniMacAddressAgingTime").focus()
			return false
		}
	}
	var arpAgingTime = parseInt($("#sysArpAgingTime").val());
	if(!/^([0-9])+$/.exec(arpAgingTime) || arpAgingTime < 1 || arpAgingTime > 60){
	    $("#sysArpAgingTime").focus()
		return false
	}
	if( agingTime && ( agingTime < arpAgingTime*60 ) ){
		window.parent.showMessageDlg('@COMMON.tip@', '@ONU.sniMacAgingTimeTip@')
	    $("#sniMacAddressAgingTime").focus()
		return false
	}
	return true
}
function flashTip(){
	$("#topTips").stop().css({opacity:1}).animate({opacity:0},"fast",function(){
		$(this).animate({opacity:1},"fast");
	})
}
function saveClick() {
	if ( !checkValid() ){
		return
	}
	var macV = Number($("#sniMacAddressAgingTime").val());
	var arpV = Number($("#sysArpAgingTime").val());
	if(macV !=0){//mac老化时间不等于0的情况下必须大于等于arp老化实际的2倍;
	 if( macV < arpV*60*2 ){
			$("#sniMacAddressAgingTime").focus();
			flashTip();
			return;
		} 
	}
	window.top.showWaitingDlg('@COMMON.wait@', '@SERVICE.setingMacAgeTime@', 'ext-mb-waiting')
	$.ajax({
		url : '/epon/saveSniMacAddressAgingTime.tv?r=' + Math.random(),
		cache:false,
		method:'post',
		data: {entityId : entityId, 
		      sniMacAddrAgingTime : $('#sniMacAddressAgingTime').val(),
		      topSysArpAgingTime : $("#sysArpAgingTime").val()},
		success : function(text) {
			if(text == 'success'){
				window.parent.showMessageDlg('@COMMON.tip@', '@SERVICE.setMacAgeOk@')
				cancelClick();
			}else{
				window.parent.showMessageDlg('@COMMON.tip@', '@SERVICE.setMacAgeError@')
			}
		},
		error : function() {
			window.parent.showMessageDlg('@COMMON.tip@', '@SERVICE.setMacAgeError@')
		}
	})
}
function cancelClick() {
	window.parent.closeWindow('sniMacAddressAgingTime')
}
</script>
</head>
<body class=openWinBody onload="initData()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">
			<p>@SERVICE.setAgeTime@</p>
			<p id="topTips">@SERVICE.setAgeTimeTip@</p>
		</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" >
				<tr>
					<td class="withoutBorderBottom rightBlueTxt w200">@SERVICE.macAgeTime@:</td>
					<td class="withoutBorderBottom"><input id="sniMacAddressAgingTime" class="normalInput w200" tooltip="@SERVICE.range120to50000@" maxlength="5" />&nbsp;&nbsp;@COMMON.S@</td>
				</tr>
				<tr>
				<td class="withoutBorderBottom rightBlueTxt w200">@SERVICE.arpAgeTime@:</td>
				<td class="withoutBorderBottom">
					<input id="sysArpAgingTime" class="normalInput w200" value="${topSysArpAgingTime}"
						tooltip="@SERVICE.range60@"
						 maxlength="2" />&nbsp;&nbsp;@COMMON.M@ 
					</td>
				</tr>
			</table>
		</form>
	</div>

 	<Zeta:ButtonGroup>
		<Zeta:Button id="saveBtn" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	
</body>
</Zeta:HTML>