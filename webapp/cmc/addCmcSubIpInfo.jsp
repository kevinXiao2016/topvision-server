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
var entityId = '${entityId}';
function cancelClick() {
	window.top.closeWindow('addCmcSubIp');
}
//检查是否与CCMTS上的其他IP地址冲突, 是：true， 否：false
function isConflictInDevice(ipAddrCheck,vlanList){
    for(v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
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
    var ipAddr = getIpValue("subIpAddress");
    var ipMask = getIpValue("subIpMask");
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
    if(isConflictInDevice(check, vlanList)){
        window.parent.showMessageDlg("@CMC.title.tip@", "@VLAN.conflictIpAddrTip@");
        return ;
    }
	window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.tip.addingSubIp@", 'ext-mb-waiting');
	$.ajax({
    	url:'/cmc/config/addCmcSubIpInfo.tv?entityId='+ entityId,
    	data: {
    	    subIpAddress: ipAddr,
    	    subIpMask: ipMask
    	},
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			//window.parent.showMessageDlg("@CMC.tip.tipMsg@",  "@CMC.tip.addCmcSubIpSuccess@");
	  			//window.parent.closeWaitingDlg();
	  			top.afterSaveOrDelete({
	   				title: '@CMC.tip.tipMsg@',
	   				html: '<b class="orangeTxt">@CMC.tip.addCmcSubIpSuccess@</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.addCmcSubIpFail@");
	  		}
			cancelClick();
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg("@CMC.tip.tipMsg@",  "@CMC.tip.addCmcSubIpFail@");
	  		cancelClick();
	  	},
	  	cache:false
    });
}

Ext.onReady(function(){
	subIpAddress = new ipV4Input("subIpAddress","span1");
	subIpAddress.width(200);
	subIpMask = new ipV4Input("subIpMask","span2");
	subIpMask.width(200);
});


</script>
</head>
<body class="openWinBody" >
	<form name="formChanged" id="formChanged">
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
			     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
			         <li><a id=saveBtn  onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
			         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
			
		</div>
	</form>	

	
</body>
</Zeta:HTML>