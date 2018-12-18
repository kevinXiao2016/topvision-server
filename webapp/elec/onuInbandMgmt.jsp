<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var onuIndex = '${onuIndex}';
var onu = ${elecOnuCapability};
onu = {}
onu.topOnuMgmtIp = "1.1.1.1"
    onu.topOnuNetMask = "255.255.255.0"
        onu.topOnuGateway= "255.255.255.0"

var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
$(document).ready(function(){
    window.ip = new ipV4Input("ipc","mgmtIP");
    ip.width(210);
    ip.bgColor("white");
    ip.height("18px");
    ip.setValue(onu.topOnuMgmtIp)
    
    window.mask = new ipV4Input("maskc","mgmtMask");
    mask.width(210);
    mask.bgColor("white");
    mask.height("18px");
    mask.setValue(onu.topOnuNetMask)
    
    window.gate = new ipV4Input("gatec","mgmtGate");
    gate.width(210);
    gate.bgColor("white");
    gate.height("18px");
    gate.setValue(onu.topOnuGateway);
});

function saveClick() {
	var _ip = getIpValue("ipc");
    var _mask = getIpValue("maskc");
    var _gate = getIpValue("gatec");
	if(!ipIsFilled("ipc")){
		ipFocus("ipc",1);
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.ipWrong );
	}
	if(!checkedIpMask(_mask)){
        ipFocus("maskc",1);
        return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.maskWrong );
	}
	if(!ipIsFilled("gatec")){
        ipFocus("gatec",1);
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.gateWrong );
	}
	if(ipAAndipB(_ip, _mask) != ipAAndipB(_gate,_mask)){
        ipFocus("gatec",1);
        return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.ipGateNot );
	}
	window.top.showWaitingDlg(I18N.COMMON.tip, I18N.ELEC.settingOnuAddr ,'ext-mb-waiting');
	$.ajax({
		url:'/epon/elec/modifyOnuInbandMgmt.tv',cache:false,
		data:{
			entityId:entityId,onuIndex: onuIndex,
			inbandIp:_ip,inbandMask:_mask,inbandGateway:_gate
		},success:function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.setOnuAddrOk );
			if(window.parent.getFrame("entity-" + entityId) != null)
                window.parent.getFrame("entity-" + entityId).page.reload();
			cancelClick();
		},error:function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.setOnuAddrEr );
		}
		
	})
    
}
function cancelClick() {
    window.parent.closeWindow('onuInbandMgmt');
}
function refreshClick(){
    
}
function authLoad(){
	if(!operationDevicePower){
		setIpEnable("mgmtIP",false);
		setIpEnable("mgmtMask",false);
		setIpEnable("mgmtGate",false);
		$("#saveBt").attr("disabled",true);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ELEC.configOutBandIp@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt w200">@ELEC.mgmtIp@@COMMON.maohao@</td>
					<td><span id="mgmtIP"></span></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@ELEC.mgmtMask@@COMMON.maohao@</td>
					<td><span id="mgmtMask"></span></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@ELEC.mgmtGate@@COMMON.maohao@</td>
					<td><span id="mgmtGate"></span></td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveClick()" icon="miniIcoSaveOK">@COMMON.saveCfg@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>