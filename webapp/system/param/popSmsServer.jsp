<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module resources
</Zeta:Loader>
<script type="text/javascript">
var flag=0;
Ext.onReady(function(){
    $("#state2").css({display:'none'});
	testServer(); 
	setTimeout(judgeFlag, 8000);
})

function testServer(){
	$.ajax({
        url : 'testSmsServer.tv',
        type : 'POST',
        success : function(json) {
            if (json.smsServerState=="success") {
                flag = 1;
            } else
                flag = 0;
        },
        failure : function() {
            flag = 0;
        }
    });
}

function judgeFlag(){
	   $("#state1").css({display:'none'});
	   $("#state2").css({display:'block'});
	   $("#started").attr("src", flag==1 ? "/images/fault/service_up.png" : "/images/fault/service_down.png");
	   $("#started").attr("alt",flag==1 ?"up":"down");
	   $("#started").hover(
	        function(){
	             if($(this).attr("alt")=="up"){
	                  inputFocused($(this).attr("id"), "@SYSTEM.smsServerAv@");
	             }else{
	                  inputFocused($(this).attr("id"), "@SYSTEM.smsServerUnAv@");
	             }
	        },
	        function(){
	             inputBlured(this);
	         }
	     ); 
}
function okClick() {
	var smsServerIp=Zeta$('smsIpAddr');
	var smsServerPort=Zeta$('smsServerPort');
//	var el = Zeta$('modem');
// 	if (smsServerPort.value.trim() == '') {
// 		smsServerPort.focus();
// 		return;
// 	}
	$.ajax({url: 'saveSmsServer.tv', type: 'POST',
//	data: {port: el.value.trim()},
    data:{
    	smsServerIp:smsServerIp.value.trim(),
        smsServerPort:smsServerPort.value.trim()
    },
	success: function() {
		//显示保存成功
		top.afterSaveOrDelete({
	      title: '@COMMON.tip@',
	      html: '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
	    });
		cancelClick();
	}, error: function() {
		window.parent.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function cancelClick() {
	top.closeWindow("modalDlg");
}
function doOnload() {
// 	setTimeout(function() {
// 		Zeta$('smsServerPort').focus();
// 	}, 500);
}
</script>
</head>
<body class="openWinBody" onload="doOnload()">
<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<span>@SYSTEM.SMSServerConfig@</span><br />
	    	<span><b class="orangeTxt pR10">@SYSTEM.attention@</b>@SYSTEM.note1@</span>	
	    </div>
	    <div class="rightCirIco phoneCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
         <tbody>
             <tr>
                 <td width="200" align="right">@SYSTEM.smsServiceIP@</td>
                 <td><input type=text id="smsIpAddr" name="smsIpAddr" style="width:250px;" class=normalInput maxlength=16 value="${smsServerIp}" tooltip="@SYSTEM.smsServiceIpTip@"/></td>
             </tr>
             <tr class="darkZebraTr">
                 <td width="200" align="right">@SYSTEM.smsServicePort@</td>
                 <td><input type=text id="smsServerPort" name="smsServerPort" style="width:250px;" class=normalInput maxlength=16 value="${smsServerPort}" tooltip="@SYSTEM.smsServicePortTip@"/></td>
             </tr>
         </tbody>
     </table>
</div>
<div>
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td>
                            <div class="noWidthCenterOuter clearBoth">
                                 <ol class="upChannelListOl pB0 pT0 noWidthCenter" id="state1">
                                     <li>
                                        <label>@SYSTEM.smsServercheck@...</label> 
                                     </li>
                                 </ol>
                                 <ol class="upChannelListOl pB0 pT0 noWidthCenter" id="state2" >
                                     <li>
                                        <label>@SYSTEM.smsServerState@</label> 
                                     </li>
                                     <li>
                                        <img class="statusImg" id="started" src="/images/fault/service_down.png" alt="down"/>
                                     </li>
                                 </ol>
                            </div>
                        </td>
                    </tr>
                 </tbody>
             </table>
</div>
<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body></Zeta:HTML>
