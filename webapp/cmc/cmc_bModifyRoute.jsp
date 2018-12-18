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
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script>
var entityId = <%= request.getParameter("entityId") %>;
var action = <%= request.getParameter("action") %>;
var index = <%= request.getParameter("index") %>;
var topCcmtsRouteDstIp = '<%= request.getParameter("topCcmtsRouteDstIp") %>';
var topCcmtsRouteIpMask = '<%= request.getParameter("topCcmtsRouteIpMask") %>';
var topCcmtsRouteNexthop = '<%= request.getParameter("topCcmtsRouteNexthop") %>';
var vlanList = ${vlanList};

function initIpInput(){
	var destIp = new ipV4Input("topCcmtsRouteDstIp", "topCcmtsRouteDstIp-span");
	destIp.width(141);
	var ipMask = new ipV4Input("topCcmtsRouteIpMask", "topCcmtsRouteIpMask-span");
	ipMask.width(141);
	var nextHop = new ipV4Input("topCcmtsRouteNexthop", "topCcmtsRouteNexthop-span");
	nextHop.width(141);
}
function initData(){
	setIpValue("topCcmtsRouteDstIp", topCcmtsRouteDstIp);
	setIpValue("topCcmtsRouteIpMask", topCcmtsRouteIpMask);
	setIpValue("topCcmtsRouteNexthop", topCcmtsRouteNexthop);
}
function showAutoCloseMessageDlg(tip, callback, delayTime){
    var time = delayTime ? delayTime : 500;
    window.parent.showMessageDlg("@COMMON.tip@", tip);
    var f = function (){
        window.top.closeWaitingDlg();
        if(callback){
            callback();
        }       
    }
    setTimeout(f, time); 
}
//ip地址检查,正确则返回true，不正确则返回false
function pubCheckIpAddr(ipAddr, tipStr){
    if(tipStr == undefined){
        tipStr = "IP";
    }
    if(!checkedIpValue(ipAddr)){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.ipFormatError@");
        return false;
    }
    if(checkIsMulticast(ipAddr)){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.multicastErrorTip@");
        return false;
    }
    if("0.0.0.0" == ipAddr){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.ipIsZeroTip@");
        return false;
    }
    if("255.255.255.255" == ipAddr){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.ipIsBroadCastTip@");
        return false;
    }
    if(!checkIsNomalIp(ipAddr)){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.ipAddrTypeTip@");
        return false;
    }
    if(checkIsReservedIp(ipAddr)){
        window.parent.showMessageDlg("@COMMON.tip@", tipStr + "@route.ipReservedTip@");
        return false;
    }
    return true;
}
//检查是否与其它IP在同一网络 true 是，false 否
function checkIsSameNetIp(val,vlanList){
    for(v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
        if(ipTmp==undefined||ipTmp==""||ipTmp=="-"||maskTmp==undefined||maskTmp==""||maskTmp=="-"){
            continue;
        }
        if(ipAAndipB(val,maskTmp)==ipAAndipB(ipTmp,maskTmp)){
            return true;
        }
    }
    return false;
}
function checkValid(){
	var dstIp = getIpValue("topCcmtsRouteDstIp");
    var ipMask = getIpValue("topCcmtsRouteIpMask");
    var nextHop = getIpValue("topCcmtsRouteNexthop");
	if(ipMask != '255.255.255.255' &&  dstIp != '0.0.0.0' && !pubCheckIpAddr(dstIp)){
        return false;
    }
	if(!checkedIpMask(ipMask)){
		window.top.showMessageDlg("@COMMON.tip@", "@route.maskErrorTip@");
        return false;
    }
	//YangYi删除 20140327 网管侧不进行判断
	//下一跳ip地址必须和设备的一个ip在同一个网段
    //if(!checkIsSameNetIp(nextHop, vlanList)){
    //  window.top.showMessageDlg("@COMMON.tip@", "@route.nextHopNotReachTip@");
    //  return false;
    //}
	return true;
}
function addRoute(){
    var dstIp = getIpValue("topCcmtsRouteDstIp");
    var ipMask = getIpValue("topCcmtsRouteIpMask");
    var nextHop = getIpValue("topCcmtsRouteNexthop");
    if(!checkValid()){
        return;
    }
    var dataStr = "route.topCcmtsRouteIndex=" + index + "&route.topCcmtsRouteDstIp=" + dstIp +"&route.topCcmtsRouteIpMask=" + ipMask
                  +"&route.topCcmtsRouteNexthop=" + nextHop;
    window.top.showWaitingDlg("@COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
    $.ajax({
      url: '/cmc_b/route/addRouteConfig.tv?entityId='+entityId,
      type: 'post',
      data: dataStr,
      success: function(response) {
            if(response == "success"){                  
                //showAutoCloseMessageDlg("@text.modifySuccessTip@", function (){
                	//cancleClick();
                //});
                window.top.closeWaitingDlg();
                top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">@text.addSuccessTip@</b>'
       			});
            	cancleClick();
             }else{
                 window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.modifyFail@");
             }
        }, error: function(response) {
            window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.modifyFail@");
        }, cache: false
    });
}
function modifyRoute(){
	var dstIp = getIpValue("topCcmtsRouteDstIp");
	var ipMask = getIpValue("topCcmtsRouteIpMask");
	var nextHop = getIpValue("topCcmtsRouteNexthop");
	if(!checkValid()){
		return;
	}
	if(dstIp == topCcmtsRouteDstIp && ipMask == topCcmtsRouteIpMask && nextHop == topCcmtsRouteNexthop){
		return;
	}
	var dataStr = "route.topCcmtsRouteIndex=" + index +"&route.topCcmtsRouteDstIp=" + dstIp +"&route.topCcmtsRouteIpMask=" + ipMask
    +"&route.topCcmtsRouteNexthop=" + nextHop;
	window.top.showWaitingDlg("@COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
    $.ajax({
      url: '/cmc_b/route/modifyRouteconfig.tv?entityId='+entityId,
      type: 'post',
      data: dataStr,
      success: function(response) {
            if(response == "success"){                  
                showAutoCloseMessageDlg("@text.modifySuccessTip@", function (){
                	cancleClick()
                });
             }else{
                 window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.modifyFail@");
             }
        }, error: function(response) {
            window.parent.showMessageDlg("@COMMON.tip@", "@CMC.tip.modifyFail@");
        }, cache: false
    });
}
function cancleClick(){
	window.parent.closeWindow("modifyRouteView");
}
$(function (){
	initIpInput();
	initData();
	if(action == 1){
		$("#save-button").click(addRoute);		
	}else{
		$("#save-button").click(modifyRoute);
	}
	$("#cancel-button").click(cancleClick);
});
</script>    
</head>
<body class="openWinBody">
    <div class="openWinHeader">
        <div class="openWinTip"></div>
        <div class="rightCirIco wheelCirIco"></div>
    </div>
    <div class="edgeTB10LR20 pT40">
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td class="rightBlueTxt" width=200>@route.destinationIp@</td>
                <td><span id="topCcmtsRouteDstIp-span"></span></td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt">@CMC.text.subnetmask@</td>
                <td><span id="topCcmtsRouteIpMask-span"></span></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@route.nextHop@</td>
                <td><span id="topCcmtsRouteNexthop-span"></span></td>
            </tr>
        </table>
    </div>
    <div class="noWidthCenterOuter clearBoth">
	    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	        <li><a id=save-button href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>
	        <li><a id=cancel-button href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	    </ol>
	</div>
</body>
</Zeta:HTML>