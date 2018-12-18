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
    module cmc
    import js.tools.ipText
    import js.tools.ipAddrCheck
</Zeta:Loader>
<script type="text/javascript">
var cmcId = '${cmcId}';
var cmcVisIp;
var cmcVisMask;
var topCcmtsVlanIndex = '${topCcmtsVlanIndex}';
var ipType = '${ipType}';
var dhcpAlloc = '${dhcpAlloc}';
var cmcVlanIp = '${cmcVlanIp}';
var secVidIndex = '${secVidIndex}';
var cmcVlanMask = '${cmcVlanMask}';
var option60 = '${option60}';
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
Ext.onReady(function(){
	cmcVisIp = new ipV4Input("cmcVisIp","span1");
	cmcVisIp.width(180);
	cmcVisIp.setValue(cmcVlanIp);
	cmcVisMask = new ipV4Input("cmcVisMask","span2");
	cmcVisMask.width(180);
	cmcVisMask.setValue(cmcVlanMask);
	$(".zebraTableRows tr:odd").addClass("darkZebraTr");
	autoLoad();
});

function saveClick(){
    var vlanId = topCcmtsVlanIndex;
    var cmcVisIp = getIpValue("cmcVisIp");
    var cmcVisMask = getIpValue("cmcVisMask");  
    var ipAddrCheck = new IpAddrCheck(cmcVisIp, cmcVisMask);
    if(ipType == 1){
        var option60 = $("#option60").val().replace(/\s/g,"");
        var dhcpAlloc = $("#dhcpAlloc").val();
        if(dhcpAlloc == 1){
            var reg = /^[A-Za-z0-9]+$/;
            if(option60 == null || option60==""){
                return $("#option60").focus();
            }
            if(!reg.test(option60)){
                return $("#option60").focus();
            }
        }else{
        	if (!ipAddrCheck.checkMask() || cmcVisMask == "255.255.255.255"){
                return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.maskInputTip@");
            }
            if (!ipAddrCheck.isHostIp()){
                return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.subIpTip@");
            }
            if (!ipIsFilled("cmcVisIp") && dhcpAlloc != 1){
                return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.subIpTip@");
            }
            if (!ipIsFilled("cmcVisMask") && dhcpAlloc != 1 ){
                return window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.maskInputTip@");
            }
            option60 = "";
        }
        showWaitingDlg('@COMMON.wait@', '@VLAN.modifingSubVlanIp@' , 'ext-mb-waiting');
        $.ajax({
            url: '/cmcVlan/modifyVlanPriIpDhcpCfg.tv',
            data: "cmcId=" + cmcId +"&topCcmtsVlanIndex=" + vlanId +"&cmcVlanIp=" + cmcVisIp+"&cmcVlanMask=" +cmcVisMask+"&ipType="+
                  ipType+"&option60="+ option60 +"&dhcpAlloc="+dhcpAlloc,
            dataType:"text",
            success: function(text) {
            	if(text == 'vlanIpExist'){
            		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanIpExist@')
           		}else{
                	//window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.modifySubVlanIpSuc@')
	                top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
	   					html: '<b class="orangeTxt">@VLAN.modifySubVlanIpSuc@</b>'
	   				});
	                window.top.closeWaitingDlg();
                	cancelClick();
           		}
            }, 
            error: function(text) {
                window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.modifySubVlanIpFail@')
        	}, 
        	cache: false
        });
    }else{
        showWaitingDlg('@COMMON.wait@', '@VLAN.modifingSubVlanIp@' , 'ext-mb-waiting');
        $.ajax({
            url: '/cmcVlan/modifyVlanSubIp.tv',
            data: "cmcId=" + cmcId +"&topCcmtsVlanIndex=" + vlanId +"&cmcVlanIp=" + cmcVisIp +"&cmcVlanMask=" +cmcVisMask+"&ipType="+ipType+'&secVidIndex='+ secVidIndex,
            dataType:"text",
            success: function(text) {
            	if(text == 'vlanIpExist'){
            		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.vlanIpExist@')
           		}else{
           		//window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.modifySubVlanIpSuc@')
	                top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
	   					html: '<b class="orangeTxt">@VLAN.modifySubVlanIpSuc@</b>'
	   				});
	                window.top.closeWaitingDlg();
                	cancelClick();
           		}
            }, error: function(text) {
                window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.modifySubVlanIpFail@')
        }, cache: false
       });
    }
}
function dhcpChange(){
	var dhcpAlloc = $("#dhcpAlloc").val();
	if(dhcpAlloc == 1){//开启,设置成不可以输入;
		var option60 = $("#option60").val().replace(/\s/g,"");
		$("#option60").attr("disabled",false).removeClass("normalInputDisabled");
		if(option60 == ""){
			$("#option60").val("docsis");
		}
		$("#opt60").attr("disabled",false);
		
		cmcVisIp.setDisabled(true);
		cmcVisMask.setDisabled(true);
		/* $("#ipLabel").attr("disabled",true);
		$("#span1").attr("disabled",true);
		setIpEnable("span1",false);
		$("#maskLabel").attr("disabled",true);
		$("#span2").attr("disabled",true);
		setIpEnable("span2",false);  */
	}else{//关闭;
		$("#option60").val("");
		$("#option60").attr("disabled",true).addClass("normalInputDisabled");
		$("#opt60").attr("disabled",true);
		
		cmcVisIp.setDisabled(false);
		cmcVisMask.setDisabled(false);
		
		/* $("#ipLabel").attr("disabled",false);
		$("#span1").attr("disabled",false);
		setIpEnable("span1",true);
		$("#maskLabel").attr("disabled",false);
		$("#span2").attr("disabled",false);
		setIpEnable("span2",true);  */
	}
}
function cancelClick() {
    window.top.closeWindow('modifyCmcVlanIp');
}
function autoLoad(){
	if(ipType == 1){
		if(dhcpAlloc == 2 || dhcpAlloc == 0){
			Zeta$('dhcpAlloc').value = 2;
		}else{
			Zeta$('dhcpAlloc').value = 1;
		}
	}
	dhcpChange();
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20 pT10">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<s:if test="ipType == 1">
				<tr>
					<td class="rightBlueTxt" id="vlanLabel" width="200">VLAN ID:</td>
					<td >
						${topCcmtsVlanIndex}
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" id = "ipLabel">@VLAN.subVlanIp@:</td>
					<td >
						<span id="span1"></span>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"  id="maskLabel">@VLAN.mask@:</td>
					<td >
						<span id="span2"></span>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@VLAN.dhcpClient@:</td>
					<td >
						<select id="dhcpAlloc" onchange="dhcpChange();"  style="width: 180px" class="normalSel">
                        	<option value="1">@COMMON.open@</option>
                        	<option value="2">@COMMON.close@</option>
                    	</select>
					</td>
				</tr>
				<tr id="opt60">
					<td class="rightBlueTxt" >option60:</td>
					<td >
						<input type="text" id="option60"  value="${option60}"  maxlength="16" class="normalInput"
						 tooltip="@VLAN.option60Tip@"  style="width: 180px;" />
					</td>
				</tr>
				</s:if>
				<s:else>
				<tr>
					<td class="rightBlueTxt" width="200">VLAN ID:</td>
					<td >
						${topCcmtsVlanIndex}
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" >@VLAN.subVlanIp@:</td>
					<td >
						<span id="span1"></span>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" >@VLAN.mask@:</td>
					<td >
						<span id="span2"></span>
					</td>
				</tr>
				</s:else>
			</table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a  id=saveBt onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
			         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
		
		
</body>
</Zeta:HTML>