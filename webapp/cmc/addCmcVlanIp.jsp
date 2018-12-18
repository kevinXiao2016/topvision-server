<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import js.tools.ipText
    import js.tools.ipAddrCheck
</Zeta:Loader>
<script type="text/javascript">
var cmcId = '${cmcId}';
var cmcVisIp;
var cmcVisMask;
var topCcmtsVlanIndex = '${topCcmtsVlanIndex}';
var priIpExist = '${priIpExist}';
Ext.onReady(function(){
	cmcVisIp = new ipV4Input("cmcVisIp","span1");
	cmcVisIp.width(120);
	cmcVisIp.bgColor("white");
	cmcVisIp.height("18px");
	cmcVisMask = new ipV4Input("cmcVisMask","span2");
	cmcVisMask.width(120);
	cmcVisMask.bgColor("white");
	cmcVisMask.height("18px");
});
function checkIpInput(ip, mask){
    if(!checkedIpValue(ip) || !checkedIpMask(mask)){
        return false;
    }
    if(!checkIsNomalIp(ip)){
        return false;
    }
    return true;
}
function saveClick(){
	var vlanId = $("#vlanId").val();
	var cmcVisIp = getIpValue("cmcVisIp");
	var cmcVisMask = getIpValue("cmcVisMask");
	var ipAddrCheck = new IpAddrCheck(cmcVisIp, cmcVisMask);
	if (!ipAddrCheck.checkMask() || cmcVisMask == "255.255.255.255"){
	    return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.maskInputTip@");
	}
	if (!ipAddrCheck.isHostIp()){
	    return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.subIpTip@");
	}
	window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.addingSubIp@' , 'ext-mb-waiting');
	$.ajax({
        url: '/cmcVlan/addCmcVlanIp.tv',
        data: "cmcId=" + cmcId +"&topCcmtsVlanIndex=" + topCcmtsVlanIndex +"&cmcVlanIp=" + cmcVisIp+"&cmcVlanMask=" +cmcVisMask+"&ipType="+priIpExist,
        dataType:"text",
        success: function(text) {
        	if(text == 'vlanIpExist'){
        		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanIpExist@')
        	}else{
        		//window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.addSuc@')
       		 	top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">@VLAN.addSuc@</b>'
       			}); 
       		 	window.top.closeWaitingDlg();
       		 	cancelClick();
        	}
        }, error: function(text) {
        	window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.addSubVlanIpFail@')
    }, cache: false
    });
}
function cancelClick() {
    window.top.closeWindow('addCmcVlanIp');
}
function autoLoad(){
	if(priIpExist == 2){
		$('#ipType').html('Secondary');
	}else{
		$('#ipType').html('Primary');
	}
}
</script>
</head>
<body class="openWinBody" onload="autoLoad()">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
		
	<div class="edgeTB10LR20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    VLAN ID:
	                </td>
	                <td>
	                    ${topCcmtsVlanIndex}
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    @VLAN.subVlanIp@:
	                </td>
	                <td>
	                   <span id="span1"></span>
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    @VLAN.mask@:
	                </td>
	                <td>
	                    <span id="span2"></span>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    @VLAN.ipType@:
	                </td>
	                <td>
	                   <div id="ipType"></div>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id=saveBt onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>


</body>
</Zeta:HTML>