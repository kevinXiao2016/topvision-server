<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var vlanList = ${vlanList};
var entityId = ${entityId};
var topCcmtsEthIpAddr = '${topCcmtsEthIpAddr}';
var topCcmtsEthIpMask = '${topCcmtsEthIpMask}';
function cancelClick() {
	window.top.closeWindow('modifyCmcSystemIpInfo');
}
//检查是否与CCMTS上的其他IP地址冲突, 是：true， 否：false
function isConflictInDevice(ipAddrCheck,vlanList, oldIp){
    for(v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
        if(ipTmp == oldIp){
            continue;
        }
        if(ipTmp==undefined||ipTmp==""||ipTmp=="-"||maskTmp==undefined||maskTmp==""||maskTmp=="-"){
            continue;
        }
        if(ipAddrCheck.isSubnetConflict(ipTmp, maskTmp)){
            return true;
        }
    }
    return false;
}
function saveClick(){
    var ipAddr = getIpValue("topCcmtsEthIpAddrText");
    var ipMask = getIpValue("topCcmtsEthIpMaskText");
    var check = new IpAddrCheck(ipAddr, ipMask);
    if(!check.checkIp()){
        window.parent.showMessageDlg("@CMC.title.tip@", "@VLAN.subIpTip@");
        return;
    }
    if(!check.checkMask()){
        window.parent.showMessageDlg("@CMC.title.tip@", "@VLAN.maskInputTip@");
        return;
    }
    if(!check.isHostIp()){
        window.parent.showMessageDlg("@CMC.title.tip@", "@CMC.tip.hostIpTip@");
        return;
    }
    if(isConflictInDevice(check, vlanList, topCcmtsEthIpAddr)){
        window.parent.showMessageDlg("@CMC.title.tip@", "@VLAN.conflictIpAddrTip@");
        return ;
    }
	window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.tip.modifyingSystemIp@", 'ext-mb-waiting');
	$.ajax({
    	url:'/cmc/config/modifyCmcSystemIpInfo.tv?entityId='+ entityId,
    	data:{
    	    topCcmtsEthIpAddr: ipAddr,
    	    topCcmtsEthIpMask: ipMask
    	},
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			//window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifySystemIpInfoSuccess@");
	  			window.parent.closeWaitingDlg();
	  			top.afterSaveOrDelete({
	   				title: '@CMC.tip.tipMsg@',
	   				html: '<b class="orangeTxt">@CMC.tip.modifySystemIpInfoSuccess@</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifySystemIpInfoFail@");
	  		}
			cancelClick();
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifySystemIpInfoFail@");
	  	},
	  	cache:false
    });
}
function dataload(){
	topCcmtsEthIpAddrText = new ipV4Input("topCcmtsEthIpAddrText","span1");
	topCcmtsEthIpAddrText.width(130);
	topCcmtsEthIpAddrText.setValue(topCcmtsEthIpAddr);
	
	topCcmtsEthIpMaskText = new ipV4Input("topCcmtsEthIpMaskText","span2");
	topCcmtsEthIpMaskText.width(130);
	topCcmtsEthIpMaskText.setValue(topCcmtsEthIpMask);
}

</script>
</head>
<body class="openWinBody" 	onload="dataload()">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						@CMC.label.IP@:
	                 </td>
	                 <td>
						<span id="span1"></span>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">
	             		@CMC.label.iPMask@:
	             	</td>
	             	<td>
	             		<span id="span2"></span>
	             	</td>
	             </tr>
	         </tbody>
        </table>
        <div class="noWidthCenterOuter clearBoth">
		    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		        <li><a  onclick="saveClick()" id=saveBtn href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.edit@</span></a></li>
		        <li><a onclick="cancelClick()"  href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@CMC.button.cancel@</span></a></li>
		    </ol>
		</div>
	</div>
	
</body>
</Zeta:HTML>