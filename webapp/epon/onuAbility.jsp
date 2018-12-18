<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var onuId = '<s:property value = "oltOnuCapability.onuId"/>';
var gePortList = <s:property value = "oltOnuCapability.onuGePortList"/>;
var fePortList = <s:property value = "oltOnuCapability.onuFePortList"/>;

function cancelClick() {
	window.parent.closeWindow('onuAbility');
}
var onuEncryptMode = ["aes128", "ctcTripleChurning", I18N.EPON.other];
var onuFEC = [I18N.COMMON.close, I18N.COMMON.open , I18N.COMMON.close];
function loadData(){
	$("#mode").text(onuEncryptMode[<s:property value = "oltOnuCapability.onuEncryptMode"/>]);
	$("#fec").text(onuFEC[<s:property value = "oltOnuCapability.onuFecEnable"/>]);
	if(gePortList[0] > 0){
		$("#gePort").text(changeToString(gePortList).toString());
	}
	if(fePortList[0] > 0){
		$("#fePort").text(changeToString(fePortList).toString());
	}
}
function changeToString(list){
	var re = new Array();
	if(list.length > 1){
		list.sort(function(a, b){
			return a - b;
		});
		var f = 0;
		var n = 0;
		var ll = list.length;
		for(var i=1; i<ll; i++){
			if(list[i] == list[i - 1] + 1){
				n++;
			}else{
				re = _changeToString(re, f, n, list);
				f = i;
				n = 0;
			}
			if(i == ll - 1){
				re = _changeToString(re, f, n, list);
			}
		}
	}else if(list.length == 1){
		re.push(list[0]);
	}
	return re;
}
function _changeToString(re, f, n, list){
	if(n == 0){
		re.push(list[f]);	
	}else if(n == 1){
		re.push(list[f]);
		re.push(list[f + 1]);
	}else{
		re.push(list[f] + "-" + list[f + n]);
	}
	return re;
}
</script>
</head>
<body class="openWinBody"  onload="loadData();">
	<div class="edgeTB10LR20 pT40">
		<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt" width="30%">@ONU.portCountGE@:</td>
				<td width="20%"><s:property value = "oltOnuCapability.onuGePortNum"/></td>
				<td class="rightBlueTxt"  width="20%">@ONU.portCountFE@:</td>
				<td width="30%"><s:property value = "oltOnuCapability.onuFePortNum"/></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@ONU.portNumGE@:</td>
				<td><div id = "gePort"></div></td>
				<td class="rightBlueTxt">@ONU.portNumFE@:</td>
				<td><div id="fePort"></div></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ONU.upstreamQueue@:</td>
				<td><s:property value = "oltOnuCapability.onuQueueNumUplink"/></td>
				<td class="rightBlueTxt">@ONU.downstreamQueue@:</td>
				<td><s:property value = "oltOnuCapability.onuQueueNumDownlink"/></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@ONU.upstreamMaxQueue@:</td>
				<td><s:property value = "oltOnuCapability.onuMaxQueueNumUplink"/></td>
				<td class="rightBlueTxt">@ONU.downstreamMaxQueue@:</td>
				<td><s:property value = "oltOnuCapability.onuMaxQueueNumDownlink"/></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ONU.FECEnable@:</td>
				<td colspan="3"><div id="fec"></div></td>
			</tr>			
		</table>
		<ol class="upChannelListOl pB0 pT10 noWidthCenter">
			<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@BUTTON.close@</span></a></li>			
		</ol>
	</div>
</body>
</Zeta:HTML>