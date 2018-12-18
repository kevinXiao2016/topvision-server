<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
</Zeta:Loader>
<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var entityId = '<s:property value="entityId"/>';
var status = '<s:property value="status"/>';
var period = '<s:property value="period"/>';
var perfType = '<s:property value="perfType"/>';
var cmcType = '<s:property value="cmcType"/>';
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
function cancelClick() {
	window.top.closeWindow('perfConfig');
}
function isIntNumber(number){
	var reg = /^[1-9]\d*$/;
	return reg.test(number);
}
function statusChange(){
	if($('#status').val()=="true"){
		$("#period").attr("disabled", false);
	}else{
		$("#period").attr("disabled", true);
	}
}
function changeCheck(){
	var changeTag = false;	
	if(($('#status').val() != status || $('#period').val() != period) 
		&&!($('#status').val() == "true" && $('#period').val()==0) ){
		changeTag = true;
	}
	if($('#status').val() == 'false' &&  $('#status').val() == status ){
		changeTag = false;
		}
	if(changeTag){
		$("#saveBtn").removeClass("disabledAlink");
	}else{
		$("#saveBtn").addClass("disabledAlink");
	}

}

function saveClick(){
	if($("#saveBtn").hasClass("disabledAlink")){
		return;
	}
	status = $('#status').val();
	if (status == "true") {
		if(!isIntNumber($('#period').val())||$('#period').val()<10||$('#period').val()>86400){
			window.top.showMessageDlg("@CMC.tip.tipMsg@", String.format("@CMC.tip.collectPeriod@", 10, 86400),null,function(){
				$('#period').focus();
			});
			return;
		}
		if(perfType != 'CC_DOL'){
			period = $('#period').val() * 1000;
		}else{
			period = $('#period').val()
		}
	}
	$.ajax({
    	url:'/cmcperf/perfConf.tv?cmcId='+ cmcId + '&cmcType=' + cmcType+ '&status=' + status + "&period=" + period + "&perfType=" + perfType + "&entityId=" + entityId,
	  	type:'POST',
	  	dateType:'json',
	  	success:function(response){
	  		//response = eval("("+response+")");
	  		if(response.message = "success"){
	  			//window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.perfConfigSaveSuccess@");
	  			top.afterSaveOrDelete({
	   				title: '@CMC.tip.tipMsg@',
	   				html: '<b class="orangeTxt">@CMC.tip.perfConfigSaveSuccess@</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.perfConfigSaveFail@");
	  		}
			cancelClick();
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.perfConfigSaveFail@");
	  	},
	  	cache:false
    });
}
function onload(){
	if(perfType != 'CC_DOL'){
		period = period/1000;
		}
	$('#period').val(period);
	if($('#status').val()=="true"){
		$("#period").attr("disabled", false);
	}else{
		$("#period").attr("disabled", true);
	}
	$("#saveBtn").addClass("disabledAlink");
	//$("#period").focus()
	
	//设备操作权限--------------------
	var ids = new Array();
	ids.add("status");
	ids.add("period");
	operationAuthInit(operationDevicePower,ids);
	//-------------------------------
}

$(document).ready(function(){
	 onload();
})

</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@CMC.text.setUtilizationMonitor@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10 pT50">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt w180">@CMC.label.status@:</td>
					<td>
						<select id="status" class="normalSel w200"  onchange="changeCheck();statusChange()">
							<option value="true" <s:if test="status==true">selected</s:if>>@CMC.text.open@</option>
							<option value="false" <s:if test="period==false">selected</s:if>>@CMC.text.close@</option>
						</select>
					</td>	
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt w180">@CMC.label.samplingPeriod@:</td>
					<td><input class="normalInput w200"  id="period" maxlength="5" onkeyup="changeCheck()" toolTip ="@CMC.tip.collectPeriodTip@"/></td>
				</tr>
			</table>
		</form>
	</div>
	<%-- <Zeta:ButtonGroup>
		<Zeta:Button onClick="saveClick()" icon="miniIcoSaveOK">@CMC.button.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup> --%>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT70 noWidthCenter">
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="saveClick()"  id="saveBtn">
	                <span><i class="miniIcoSave"></i>@BUTTON.apply@</span>
	            </a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
	                <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
	            </a>
	        </li>
	    </ol>
	</div>
</body>
</Zeta:HTML>