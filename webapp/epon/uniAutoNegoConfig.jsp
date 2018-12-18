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
    CSS css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var uniId = '<s:property value="uniId"/>';
var onuType = '<s:property value="onuType"/>';
var uniAutoMode = '<s:property value="oltUniExtAttribute.topUniAttrAutoNegotiationAdvertisedTechAbilityInteger"/>';
uniAutoMode = parseInt(uniAutoMode) > -1 ? uniAutoMode : 0;
var uniAutoNegoEnable = '<s:property value="uniAutoNegoEnable" />';
uniAutoNegoEnable = parseInt(uniAutoNegoEnable) == 2 ? 2 : 1;
var uniFlowCtrl = '<s:property value="oltUniExtAttribute.flowCtrl" />';
uniFlowCtrl = parseInt(uniFlowCtrl) == 1 ? 1 : 2;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
//页面控制参数
var needToSet = new Array();
var wrongTip = new Array();
var uniAutoModeJspData = uniAutoMode;
var MTK_ONU_TYPE = [37,38];

Ext.BLANK_IMAGE_URL = '../images/s.gif';
function saveClick(){
	var nego = $("#open").attr("checked") ? 1 : 2;
	var flow = $("#flowOpen").attr("checked") ? 1 : 2;
	var mode = $("#uniAutoType").val();
	if(nego != uniAutoNegoEnable){
		needToSet.push(0);
	}
	if(nego == 2){
		if(mode != uniAutoMode){
			needToSet.push(2);
		}
	}
	if(flow != uniFlowCtrl){
		needToSet.push(1);
	}
	if(nego == 2 && (uniAutoMode % 2 == 1 || uniAutoMode == -1) && uniFlowCtrl == 1 && mode % 2 == 0 && flow == 2){
		if(needToSet.indexOf(0) > -1){
			needToSet = new Array();
			needToSet.push(0);
		}else{
			needToSet = new Array();
		}
		needToSet.push(1);
		needToSet.push(2);
	}
	if(needToSet.length == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.cfgSaved )
	}else{
		showWaitingDlg(I18N.COMMON.wait, I18N.UNI.settingUniWorkMode , 'ext-mb-waiting');
		setControl();
	}
}
function setControl(){
	if(needToSet.length > 0){
		if(needToSet[0] == 0){
			setAutoEnable();
		}else if(needToSet[0] == 1){
			setFlowEnable();
		}else if(needToSet[0] == 2){
			setWorkMode();
		}
	}else{
		needToSet = new Array();
		if(wrongTip.length > 0){
			window.parent.showMessageDlg(I18N.COMMON.tip, wrongTip.join("!"));
			wrongTip = new Array();
		}else{
			wrongTip = new Array();
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.saveOk )
			cancelClick();
		}
	}
}
function setAutoEnable(){
	var tmpEnable = $("#close").attr("checked") ? 2 : 1;
	$.ajax({
        url: '/onu/setUniAutoNegoActive.tv',
        data: "uniId=" + uniId + "&entityId=" + entityId + "&uniAutoNegoEnable=" + tmpEnable,
        success: function(json) {
        	if (json.message) {
        		setFailed(I18N.UNI.setUniAutoNegEr + ':' + json.message);
        		return;
        	}
        	setEntityProperty("uniAutoNegotiationEnable",tmpEnable);
        	uniAutoNegoEnable = tmpEnable;
        	setAfter();
        }, error: function() {
        	setFailed(I18N.UNI.setUniAutoNegEr + "!");
    	}, cache: false
    });
}

function setEntityProperty(type,tmpEnable){
	if( window.parent.getFrame("entity-" + entityId) ){
		if( window.parent.getFrame("entity-" + entityId).setEntityProperty){
	    	window.parent.getFrame("entity-" + entityId).setEntityProperty('uni',type, tmpEnable, uniId);
	    }
	}
}
function setFlowEnable(){
	var flow = $("#flowOpen").attr("checked") ? 1 : 2;
	$.ajax({
        url: '/onu/setUniAttrFlowCtrlStatus.tv',
        data: "uniId=" + uniId + "&entityId=" + entityId + "&uniFlowCtrlEnable=" + flow,
        success: function(json) {
        	if (json.message) {
        		setFailed(I18N.UNI.setFlowControlEr + '@COMMON.maohao@' + json.message);
        		return;
        	}
        	setEntityProperty("flowCtrl",flow);
        	uniFlowCtrl = flow;
        	setAfter();
        }, error: function() {
        	setFailed(I18N.UNI.setFlowControlEr + '!');
    	}, cache: false
    });
}
function setWorkMode(){
	var setUniAutoNegoStat = parseInt($("#uniAutoType").val());
	var tmpStat = $("#uniAutoType").find("option:selected").text();
	$.ajax({
        url: '/onu/setUniAutoNegoModeType.tv',
        type: 'POST',
        data: "entityId=" + entityId + "&uniId=" + uniId + "&uniAutoNegoMode=" + setUniAutoNegoStat,
        success: function(json) {
        	if (json.message) {
        		setFailed(I18N.UNI.setUniWorkModeFail);
        		return;
        	}
        	uniAutoMode = setUniAutoNegoStat;
        	setEntityProperty("uniAutoNegoString",tmpStat);
        	setAfter();
        }, error: function() {
        	setFailed(I18N.UNI.setUniWorkModeFail);
        }, cache: false
    });
}
function setFailed(str){
	wrongTip.push(str);
	setAfter();
}
function setAfter(){
	needToSet.splice(0, 1);
	setControl();
}
function loadMode(){
	var sel = $("#uniAutoType");
	if(uniFlowCtrl == 1){
		$("#flowOpen").attr("checked",true);
		$("#flowClose").attr("checked",false);
	}else{
		$("#flowOpen").attr("checked",false);
		$("#flowClose").attr("checked",true);
	}
	flowChange();
	if(uniAutoNegoEnable == 1){
		$("#open").attr("checked",true);
		$("#close").attr("checked",false);
	}else{
		$("#open").attr("checked",false);
		$("#close").attr("checked",true);
	}
	if(uniAutoMode != -1){
		sel.val(uniAutoMode);
	}
	enableClick();
}

function flowChange(){
	var sel = $("#uniAutoType"),
	    oldValue = sel.val(),
	    opt1  = ['<option value="0">@UNI.mode0@</option>',
	             '<option value="1">@UNI.mode1@</option>',
				 '<option value="2">@UNI.mode2@</option>',
				 '<option value="3">@UNI.mode3@</option>',
				 '<option value="4">@UNI.mode4@</option>',
				 '<option value="5">@UNI.mode5@</option>',
				 '<option value="6">@UNI.mode6@</option>'].join(''),
		opt2  = ['<option value="0">@UNI.mode0@</option>',
		         '<option value="1">@UNI.mode1@</option>',
		         '<option value="3">@UNI.mode3@</option>',
		         '<option value="5">@UNI.mode5@</option>'].join('');
	
	sel.empty();
	/*
	*  端口工作模式 分3种情况
	*  1开启  直接disabled;
	*  1关闭 2开启  全双工可选，半双工不可选(共3项);
	*  1关闭 2关闭  所有都可选(共6项);
	*/
	
	var flowEnable = parseInt($('#putFlow :radio:checked').val(), 10); //端口流控使能;
	
	switch(flowEnable){ 
	case 1://2开启;
		sel.html(opt2);
 		break; 
	case 2://2关闭;
		sel.html(opt1);
 		break;
 	}
	//切换的时候，先记住原来的值。切换后如果选项中有这个值，则选中;
	if(oldValue != null){
		$("#uniAutoType").find('option').each(function(i, v){
			if( $(this).val() == oldValue){
				sel.val( $(this).val() );
				return false;
			}
		})
		
	}
	enableClick();
	/* if($("#flowOpen").attr("checked")){//开启
		for(var x=1; x<optionStr.length; x += 2){
			sel.append(optionStr[x]);
		}
		if(uniAutoModeJspData % 2 == 0){
			return;
		}
	}else{
		for(var x=1; x<optionStr.length; x++){
			sel.append(optionStr[x]);
		}
	}
	sel.val(uniAutoModeJspData); */
}
function enableClick(){
	var autoNegoEnable = parseInt($("#topPart :radio:checked").val(), 10);
	switch(autoNegoEnable){
	 case 1: //1开启;
		 $("#uniAutoType").removeAttr("disabled");
		 break;
	 case 0: //1关闭;
		 $("#uniAutoType").attr("disabled", "disabled");
		 break;
	 }  
}
function cancelClick() {
	window.parent.closeWindow('uniAutoNegotiation');	
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function autoNegRestart(){
	showWaitingDlg(I18N.COMMON.wait, I18N.UNI.setting , 'ext-mb-waiting');
	$.ajax({
		url: '/onu/setRestartUniAutoNego.tv', 
		type: 'POST',
		data: "uniId="+uniId+"&entityId="+entityId,
		success: function() {		        				
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.reAutoNegOk );
		}, 
		error: function() {
			window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.UNI.reAutoNegEr );
		}, cache: false
	});
}

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.saveBt.setDisabled(true);
	}
	//enableClick();
}
$(function(){
	if(MTK_ONU_TYPE.contains(onuType)){
		$('#autoNegNote').css({display:'none'});
		$('#tip2').css({display:'none'});
	}
	if(window.uniAutoNegoEnable == 1){ //选中自协商使能开启;
		$('#open').attr({checked : 'checked'});
	}else{//选中自协商使能关闭;
		$('#close').attr({checked : 'checked'});
	}
	if(uniFlowCtrl == 1){
		$("#flowOpen").attr("checked",true);
	}else{
		$("#flowClose").attr("checked",true);
	}
	flowChange();
	authLoad();
	$("#uniAutoType").val(window.uniAutoMode);
	
	$("#topPart :radio").click(function(){
		enableClick();	
	});
	$("#putFlow :radio").click(function(){
		flowChange();
	})

});//end document.ready;

</script>
</head>
	<body class=openWinBody>
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.uniWorkModeConfig@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr valign=bottom>
						<td class="rightBlueTxt w150">@UNI.autoNegEn@</td>
						<td id="topPart">							
							<input type="radio" id="open" value="0" name='autoNegoEnable'   /> 
							<label for="open">@UNI.on@</label>
							<input type="radio" id="close" value="1" name='autoNegoEnable'  /> 
							<label for="close">@COMMON.close@</label>
							<button id="autoNegRestart" class="BUTTON75" style="margin-left: 60px; display: none;" onclick="autoNegRestart()">@UNI.reAutoNeg@</button></td>
					</tr>
					<tr valign=bottom>
						<td class="rightBlueTxt">@UNI.flowControlEn@</td>
						<td id="putFlow"><input type="radio" id="flowOpen" name='flowEnable' value="1" checked /> 
							<label for="flowOpen">@UNI.on@</label>
							<input type="radio" id="flowClose" name='flowEnable' value="2" style='margin-left: 15px;' /> 
							<label for="flowClose">@COMMON.close@</label>
					</tr>
					<tr id='autoNegNote'>
						<td class="rightBlueTxt">@UNI.workMode@</td>
						<td width=280><div id='uniAutoStatus'>
								<select id="uniAutoType" class="normalSel w180" onchange='uniAutoModeJspData = this.value;'></select>
							</div></td>
					</tr>
					<tr>
						<td colspan=2>
							<div  class="blueTxt">@UNI.autoNegNote@</div>
						</td>
					</tr>
					<tr id='tip2'>
						<td colspan=2  class="blueTxt">@UNI.autoNegNote2@</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
