<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var entityId = '${entityId}';
var snmpParam = ${snmpJson};
var emsIpAddress = '${emsCmcIpAddress}';
var emsReadCommunity = null;
var emsWriteCommunity = null;

function cancelClick() {
	window.top.closeWindow('emsBasicInfo');
}
function existChinese(val){
    var reg = /[^\x00-\xff]+| +/
    return reg.test(val);
}
function saveClick(){
    var read = $('#emsReadCommunity').val();
    var write = $('#emsWriteCommunity').val();
    var ipAddr = getIpValue("emsIpAddressTest");
    if(!ipIsFilled("emsIpAddressTest")){
        window.parent.showMessageDlg("@CMC.title.tip@",
                "@syslog.ipError1@");
        return ;
    }
    if(!checkIsNomalIp(ipAddr)){
        window.parent.showMessageDlg("@CMC.title.tip@",
                "@syslog.ipError5@");
        return ;
    }
    /* if(read.length <= 0 || existChinese(read)){
        $('#emsReadCommunity').focus();
        return;
    }
    if(write.length <= 0 || existChinese(write)){
        $('#emsWriteCommunity').focus();
        return;
    } */
    if( !V.isSnmpCommunity(read) ){
    	$('#emsReadCommunity').focus();
        return;
    }
    if( !V.isSnmpCommunity(write) ){
    	$('#emsWriteCommunity').focus();
        return;
    }
    
	window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.tip.modifyEmsConfig@", 'ext-mb-waiting');
	$.ajax({
    	url:'/cmc/config/modifyEmsConfigInfo.tv?entityId='+ entityId,
    	data: {
    	    emsCmcIpAddress: ipAddr,
    	    emsCmcReadCommunity: read,
    	    emsCmcWriteCommunity: write
    	},
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response = "success"){
	  			//window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifyEmsConfigSuccess@");
	  			window.top.closeWaitingDlg();
	  			top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@CMC.tip.modifyEmsConfigSuccess@</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.tip.modifyEmsConfigFail@");
	  		}
			cancelClick();
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifyEmsConfigFail@");
	  	},
	  	cache:false
    });
}
function dataload(){
	emsIpAddressTest = new ipV4Input("emsIpAddressTest","span1");
	emsIpAddressTest.width(200);
	emsIpAddressTest.setValue(emsIpAddress);
	emsReadCommunity = snmpParam.community;
	emsWriteCommunity = snmpParam.writeCommunity;
	$('#emsReadCommunity').val(emsReadCommunity);
	$('#emsWriteCommunity').val(emsWriteCommunity);
}
$(function(){
	dataload()
})
</script>
</head>
<body class="openWinBody">
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
						@CMC.tip.emsManageIp@
	                 </td>
	                 <td>
						<span id="span1"></span>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						@CMC.tip.emsReadCommunity@
	                 </td>
	                 <td>
						<Zeta:Password 
						id="emsReadCommunity"  maxlength="31"
						tooltip='@epon/config.page.communityTip2@'
						width="200" />
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						@CMC.tip.emsWriteCommunity@
	                 </td>
	                 <td>
						<Zeta:Password 
						id="emsWriteCommunity" maxlength="31"
						tooltip='@epon/config.page.communityTip2@'
						width="200" />
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		 <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id=saveBtn  onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</form>
	
</body>
</Zeta:HTML>