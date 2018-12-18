<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
</Zeta:Loader>
<head>
<script type="text/javascript" src="../js/jquery/Nm3kTabBtn.js"></script>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '<s:property value="entityId"/>';
var grid = null;
var store = null;
var toolbar= null;
var freqData = new Array();
var ccmtsSpectrumGpListJson = ${ccmtsSpectrumGpListJson}? ${ccmtsSpectrumGpListJson} : new Array();
var ccmtsSpectrumGpJson = ${ccmtsSpectrumGpJson}? ${ccmtsSpectrumGpJson} : new Array();
var groupModifyFlag = '<s:property value="groupModifyFlag"/>';
var groupId = '<s:property value="groupId"/>';
var maxFreqFrequency = null;
var minFreqFrequency = null;

function saveClick() {
	var groupId = $("#groupId").val();
	var adminStatus = $("input[name='adminStatus']:checked").val();
	var hopPeriod = $("#hopPeriod").val();
	if(!checkHopPeriodValue(hopPeriod)){
		$("#hopPeriod").focus();
		return;
	}
	var maxHopLimit = $("#maxHopLimit").val();
	if(!checkMaxHopLimitValue(maxHopLimit)){
		$("#maxHopLimit").focus();
		return;
	}
	var snrThres1 = $("#snrThres1").val()*10;
	if(!checkSnrThresValue(snrThres1)){
		$("#snrThres1").focus();
		return;
	}
	var snrThres2 = $("#snrThres2").val()*10;
	if(!checkSnrThresValue(snrThres2)){
		$("#snrThres2").focus();
		return;
	}
	var fecThresCorrect1 = $("#fecThresCorrect1").val();
	if(!checkFecThresValue(fecThresCorrect1)){
		$("#fecThresCorrect1").focus();
		return;
	}
	var fecThresCorrect2 = $("#fecThresCorrect2").val();
	if(!checkFecThresValue(fecThresCorrect2)){
		$("#fecThresCorrect2").focus();
		return;
	}
	var fecThresUnCorrect1 = $("#fecThresUnCorrect1").val();
	if(!checkFecThresValue(fecThresUnCorrect1)){
		$("#fecThresUnCorrect1").focus();
		return;
	}
	var fecThresUnCorrect2 = $("#fecThresUnCorrect2").val();
	if(!checkFecThresValue(fecThresUnCorrect2)){
		$("#fecThresUnCorrect2").focus();
		return;
	}
	var groupPolicy = $("input[name='groupPolicy']:checked").val();
	var groupPriority1st = $("#groupPriority1st").val();
	var groupPriority2st = $("#groupPriority2st").val();
	var groupPriority3st = $("#groupPriority3st").val();
	var freqList = new Array();
	for(i=0;i<freqData.length;i++){
		freqList[i] = freqData[i][2]+"_"+freqData[i][3]*1000000+"_"+freqData[i][4]*1000000+"_"+freqData[i][5]*10;
	}
	var freqListString = freqList.join(",");
	if(groupModifyFlag==1){
		window.parent.showWaitingDlg("@CMC.tip.waiting@" , "@CMC.GP.saving@" , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/ccmtsspectrumgp/modifyDeviceGroup.tv?m=" + Math.random(),
			method:"post",
			success:function(response){
				if(response.responseText == "success"){
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.modifyGpSuccess@");
					var a = window.parent.getWindow("ccmtsSpectrumGroupMgmt").body.dom.firstChild.contentWindow.store;
					a.reload();
					cancelClick();
				}else{
	   				window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.modifyGpFail@");
				}
			},failure:function (response) {
	            window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.modifyGpFail@");
	        },params : {
	  			entityId : entityId,
	  			groupId : groupId,
	  			adminStatus : adminStatus,
	  			hopPeriod : hopPeriod,
	  			maxHopLimit : maxHopLimit,
	  			snrThres1 : snrThres1,
	  			snrThres2 : snrThres2,
	  			fecThresCorrect1 : fecThresCorrect1,
	  			fecThresCorrect2 : fecThresCorrect2,
	  			fecThresUnCorrect1 : fecThresUnCorrect1,
	  			fecThresUnCorrect2 : fecThresUnCorrect2,
	  			groupPolicy : groupPolicy,
	  			groupPriority1st : groupPriority1st,
	  			groupPriority2st : groupPriority2st,
	  			groupPriority3st : groupPriority3st,
	  			freqListString : freqListString
	  		}
		});
	}else{
		window.parent.showWaitingDlg("@CMC.tip.waiting@" , "@CMC.GP.saving@" , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/ccmtsspectrumgp/addDeviceGroup.tv?m=" + Math.random(),
			method:"post",
			success:function(response){
				if(response.responseText == "success"){
					window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.AddGroupSuccess@");
					var a = window.parent.getWindow("ccmtsSpectrumGroupMgmt").body.dom.firstChild.contentWindow.store;
					a.reload();
					cancelClick();
				}else{
	   				window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.AddGroupFail@");
				}
			},failure:function (response) {
	            window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.AddGroupFail@");
	        },params : {
	  			entityId : entityId,
	  			groupId : groupId,
	  			adminStatus : adminStatus,
	  			hopPeriod : hopPeriod,
	  			maxHopLimit : maxHopLimit,
	  			snrThres1 : snrThres1,
	  			snrThres2 : snrThres2,
	  			fecThresCorrect1 : fecThresCorrect1,
	  			fecThresCorrect2 : fecThresCorrect2,
	  			fecThresUnCorrect1 : fecThresUnCorrect1,
	  			fecThresUnCorrect2 : fecThresUnCorrect2,
	  			groupPolicy : groupPolicy,
	  			groupPriority1st : groupPriority1st,
	  			groupPriority2st : groupPriority2st,
	  			groupPriority3st : groupPriority3st,
	  			freqListString : freqListString
	  		}
	  	});
	}
}

function cancelClick() {
	window.parent.closeWindow('showNewDeviceGroup');	
}

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
	}
}

function initData(){
	//初始化中心频点提示框
	changeMaxWidth();
	
	//初始化优先级下拉框
	var groupPriority2st = $("#groupPriority2st");
	$("#groupPriority2st option").remove();
	groupPriority2st.append(String.format("<option value={0}>{1}</option>", 1,"@CMCPE.frequency@"));
	groupPriority2st.append(String.format("<option value={0}>{1}</option>", 2,"@CMC.GP.width@"));
	groupPriority2st.append(String.format("<option value={0}>{1}</option>", 3,"@CMC.GP.modulation@"));
	
	var groupPriority3st = $("#groupPriority3st")
	$("#groupPriority3st option").remove();
	groupPriority3st.append(String.format("<option value={0}>{1}</option>", 1,"@CMCPE.frequency@"));
	groupPriority3st.append(String.format("<option value={0}>{1}</option>", 3,"@CMC.GP.modulation@"));
	if(groupModifyFlag==1){
		//修改跳频组，初始化
		var position = $("#groupId");
		position.append(String.format("<option value={0}>{0}</option>", groupId));
		$("#groupId").attr("disabled",true);
		$("input[name='adminStatus'][value="+ccmtsSpectrumGpJson.adminStatus+"]").attr("checked",true);
		$("input[name='groupPolicy'][value="+ccmtsSpectrumGpJson.groupPolicy+"]").attr("checked",true);
		$("#groupPriority1st").val(ccmtsSpectrumGpJson.groupPriority1st);
		selectPriority1Change();
		$("#groupPriority2st").val(ccmtsSpectrumGpJson.groupPriority2st);
		$("#groupPriority3st").val(ccmtsSpectrumGpJson.groupPriority3st);
		
		$("#hopPeriod").val(ccmtsSpectrumGpJson.hopPeriod);
		$("#maxHopLimit").val(ccmtsSpectrumGpJson.maxHopLimit);
		$("#snrThres1").val(ccmtsSpectrumGpJson.snrThres1/10);
		$("#snrThres2").val(ccmtsSpectrumGpJson.snrThres2/10);
		$("#fecThresCorrect1").val(ccmtsSpectrumGpJson.fecThresCorrect1);
		$("#fecThresCorrect2").val(ccmtsSpectrumGpJson.fecThresCorrect2);
		$("#fecThresUnCorrect1").val(ccmtsSpectrumGpJson.fecThresUnCorrect1);
		$("#fecThresUnCorrect2").val(ccmtsSpectrumGpJson.fecThresUnCorrect2);
		
		for(i=0;i<ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList.length;i++){
			freqData[i] = new Array();
			freqData[i][0] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].entityId;
			freqData[i][1] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].groupId;
			freqData[i][2] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].freqIndex;
			freqData[i][3] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].freqFrequency/1000000;
			freqData[i][4] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].freqMaxWidth/1000000;
			freqData[i][5] = ccmtsSpectrumGpJson.ccmtsSpectrumGpFreqList[i].freqPower/10;
		}
		store.loadData(freqData);
	}else{
		//新建跳频组，初始化跳频组ID下拉框
		var position = $("#groupId")
		for(var j=1; j<33; j++){
			var groupAddFlag = true
			for(var k=0; k<ccmtsSpectrumGpListJson.length; k++){
				if(ccmtsSpectrumGpListJson[k].groupId == j){
					groupAddFlag = false
				}
			}
			if(groupAddFlag){
				position.append(String.format("<option value={0}>{0}</option>", j))
			}
		}
		
		//给出优先级默认值
		selectPriority1Change();
	}
	
	authLoad();
}

function changeMaxWidth(){
	var maxWidth = $("#freqMaxWidth").val();
	maxWidth = Number(maxWidth);
	maxFreqFrequency = 65 - maxWidth/2;
	minFreqFrequency = 5 + maxWidth/2;
	$("#freqFrequency").attr("toolTip",String.format("@CMC.GP.freqFrequencynote@",minFreqFrequency,maxFreqFrequency));
}

Ext.onReady(function() {
	toolbar = [ {
			xtype : "label",
			text : "@CMC.GP.freqFrequency@"
		},
		"<input type='text' id='freqFrequency' toolTip='@CMC.GP.freqFrequencynote@' class='normalInput w50' />",
		{
			xtype : "label",
			text : "Mhz"
		}, "-",{
			xtype : "label",
			text : "@CMC.GP.freqMaxWidth@"
		},
		"<select type='text' class='normalSel w50' id='freqMaxWidth' onchange='changeMaxWidth()'><option value ='1.6' >1.6</option><option value ='3.2' >3.2</option><option value ='6.4' >6.4</option></select>",
		{
			xtype : "label",
			text : "Mbps"
		}, "-",{
			xtype : "label",
			text : "@CMC.GP.freqPower@"
		},
		"<input type='text' class='normalInput w50' id='freqPower' title = '@CMC.GP.freqPowernote@' toolTip='@CMC.GP.freqPowernote@'/>",
		{
			xtype : "label",
			text : "@{unitConfigConstant.elecLevelUnit}@"
		}, "-", {
			text : "@CMC.text.dhcpAdd@",
			iconCls : 'bmenu_new',
			disabled : !operationDevicePower,
			handler : addFreq
	} ]
	
	var cm = [ {
			header : '@CMC.GP.freqFrequency@'+'(Mhz)',
			width : 120,
			sortable : true,
			dataIndex : 'freqFrequency'
		}, {
			header : '@CMC.GP.freqMaxWidth@'+'(Mbps)',
			width : 150,
			sortable : true,
			dataIndex : 'freqMaxWidth'
		}, {
			header : '@CMC.GP.freqPower@'+'(@{unitConfigConstant.elecLevelUnit}@)',
			width : 120,
			sortable : true,
			dataIndex : 'freqPower'
		}, {
			header : '@CHANNEL.operation@',
			width : 120,
			sortable : true,
			dataIndex : 'op',
			renderer : opeartionRender
	} ];

	store = new Ext.data.SimpleStore({
		data : freqData,
		fields : ['entityId','groupId','freqIndex','freqFrequency','freqMaxWidth','freqPower']
	});

	grid = new Ext.grid.GridPanel({
			title : "@CMC.GP.freq@",
			store : store,
			cls : 'normalTable',
			height : 180,
			//width:780,
			border : true,
			viewConfig:{
				forceFit:true
			},
			animCollapse : false,
			trackMouseOver : trackMouseOver,
			columns : cm,
			bbar : new Ext.Toolbar({
				items : toolbar
			}),
			renderTo : "freqGrid"
	});
	initData();
});

function opeartionRender(value, cellmate, record){
	var freqIndex = record.data.freqIndex;
	if(operationDevicePower){
		return String.format(
				"<img src='/images/delete.gif' title='@CMC.button.delete@'" + 
				"onclick='deleteFreq(\"{0}\")' style='cursor:pointer;'/>", freqIndex);
	}else{
		return String.format(
				"<img src='/images/deleteDisable.gif' title='@CMC.button.delete@'" + 
				"style='cursor:pointer;'/>", freqIndex);
	}
}

function deleteFreq(f){
	for(i=0;i<freqData.length;i++){
		if(freqData[i][2] == f){
			freqData.remove(freqData[i]);
			store.loadData(freqData);
		}
	}
}

function checkFreqFrequencyConflict(q,w){
	var conflictFlag = false;//默认没冲突
	for (i=0; i< freqData.length; i++){
		//已存在的中心频点范围
		var minFreqDate = (Number(freqData[i][3]) - Number(freqData[i][4]/2)).toFixed(1);
		var maxFreqDate = (Number(freqData[i][3]) + Number(freqData[i][4]/2)).toFixed(1);
		//输入的中心频点范围
		var minFreqInput = (Number(q) - w/2).toFixed(1);
	    var maxFreqInput = (Number(q) + w/2).toFixed(1);
		if(minFreqDate >= maxFreqInput || maxFreqDate <= minFreqInput){
			//如果没有交叉，继续
			continue;
		}else{
			//如果有交叉，中心频点冲突，返回
			conflictFlag = true;
			break;
		}
	}
	return conflictFlag;
}

function addFreq(){
	var freqMaxWidth = $("#freqMaxWidth").val();
	var freqFrequency = $("#freqFrequency").val();
	//校验中心频点范围
	if(!checkFreqFrequencyValue(freqFrequency)){
		$("#freqFrequency").focus();
		return;
	}
	//校验中心频点之间是否有冲突
	if(checkFreqFrequencyConflict(freqFrequency,freqMaxWidth)){
		window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.freqConflict@");
		return;
	}
	var freqPower = $("#freqPower").val();
	if(!checkFreqPowerValue(freqPower)){
		$("#freqPower").focus();
		return;
	}
	if(freqData.length >= 8){
		window.parent.showMessageDlg("@CMC.tip.tipMsg@","@CMC.GP.maxfreq@");
		return;
	}
	
	var flag = [false,false,false,false,false,false,false,false,false];
	for(i=0;i<freqData.length;i++){
		if(freqData[i][2]==1){
			flag[1] = true;
		}else if(freqData[i][2]==2){
			flag[2] = true;
		}else if(freqData[i][2]==3){
			flag[3] = true;
		}else if(freqData[i][2]==4){
			flag[4] = true;
		}else if(freqData[i][2]==5){
			flag[5] = true;
		}else if(freqData[i][2]==6){
			flag[6] = true;
		}else if(freqData[i][2]==7){
			flag[7] = true;
		}else if(freqData[i][2]==8){
			flag[8] = true;
		}
	}
	for(j=1;j<9;j++){
		if(!flag[j]){
			var index = freqData.length;
			freqData[index] = new Array();
			freqData[index][0] = entityId;
			freqData[index][2] = j;
			freqData[index][3] = freqFrequency;
			freqData[index][4] = freqMaxWidth;
			freqData[index][5] = freqPower;
			store.loadData(freqData);
			return;
		}
	}
}

function checkFreqFrequencyValue(v) {
	v = Number(v);
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v*10)|| (v > Number(maxFreqFrequency)) || v < Number(minFreqFrequency)) {
		return false;
	}else{
		return true;
	}
}

function checkFreqPowerValue(v){
	if(v == ""){
		return false;
	}
	v = Number(v);
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(Math.abs(v*10))|| (v*10 > 260) || v*10 < -130) {
		return false;
	}else{
		return true;
	}
}

function checkHopPeriodValue(v){
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 1000) || v < 25) {
		return false;
	}else{
		return true;
	}
}

function checkMaxHopLimitValue(v){
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 20) || v < 1) {
		return false;
	}else{
		return true;
	}
}

function checkSnrThresValue(v){
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 1000) || v < 10) {
		return false;
	}else{
		return true;
	}
}

function checkFecThresValue(v){
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 100) || v < 0) {
		return false;
	}else{
		return true;
	}
}

function selectPriority1Change(){
	var groupPriority1st = $("#groupPriority1st").val();
	if(groupPriority1st == 2){//如果选择了带宽
		
		//构造第二优先级，可能为调制方式或频点，默认频点
		var groupPriority2st = $("#groupPriority2st")
		$("#groupPriority2st option").remove();
		groupPriority2st.append(String.format("<option value={0} selected='selected'>{1}</option>", 1,"@CMC.GP.frequency@"));
		groupPriority2st.append(String.format("<option value={0}>{1}</option>", 3,"@CHANNEL.modulationType@"));
		
		//构造第二优先级，只可能为调制方式
		var groupPriority3st = $("#groupPriority3st")
		$("#groupPriority3st option").remove();
		groupPriority3st.append(String.format("<option value={0} selected='selected'>{1}</option>", 3,"@CHANNEL.modulationType@"));
		groupPriority3st.append(String.format("<option value={0}>{1}</option>", 1,"@CMC.GP.frequency@"));
		
	}else if(groupPriority1st == 3){//如果选择了调制方式
		
		//构造第二优先级，只能为带宽
		var groupPriority2st = $("#groupPriority2st")
		$("#groupPriority2st option").remove();
		groupPriority2st.append(String.format("<option value={0} selected='selected'>{1}</option>", 2,"@CHANNEL.width@"));
		
		//构造第三优先级，只能为频点
		var groupPriority3st = $("#groupPriority3st")
		$("#groupPriority3st option").remove();
		groupPriority3st.append(String.format("<option value={0} selected='selected'>{1}</option>", 1,"@CMC.GP.frequency@"));
	}
}

function selectPriority2Change(){
	var groupPriority2st = $("#groupPriority2st").val();
	
	if(groupPriority2st == 1){//如果选择了频点
		var groupPriority3st = $("#groupPriority3st")
		$("#groupPriority3st option").remove();
		groupPriority3st.append(String.format("<option value={0} selected='selected'>{1}</option>", 3,"@CHANNEL.modulationType@"));
		groupPriority3st.append(String.format("<option value={0}>{1}</option>", 1,"@CMC.GP.frequency@"));
		
	}else if(groupPriority2st == 3){//如果选择了调制方式
		var groupPriority3st = $("#groupPriority3st")
		$("#groupPriority3st option").remove();
		groupPriority3st.append(String.format("<option value={0} selected='selected'>{1}</option>", 1,"@CMC.GP.frequency@"));
		groupPriority3st.append(String.format("<option value={0}>{1}</option>", 3,"@CHANNEL.modulationType@"));
	}
}

function selectPriority3Change(){
	var groupPriority3st = $("#groupPriority3st").val();
	
	if(groupPriority3st == 1){//如果选择了频点
		var groupPriority2st = $("#groupPriority2st")
		$("#groupPriority2st option").remove();
		groupPriority2st.append(String.format("<option value={0} selected='selected'>{1}</option>", 3,"@CHANNEL.modulationType@"));
		groupPriority2st.append(String.format("<option value={0}>{1}</option>", 1,"@CMC.GP.frequency@"));
	}else if(groupPriority3st == 3){//如果选择了调制方式
		var groupPriority2st = $("#groupPriority2st")
		$("#groupPriority2st option").remove();
		groupPriority2st.append(String.format("<option value={0} selected='selected'>{1}</option>", 1,"@CMC.GP.frequency@"));
		groupPriority2st.append(String.format("<option value={0}>{1}</option>", 3,"@CHANNEL.modulationType@"));
	}
}

</script>
</head>
<body class="openWinBody">
	<div class="edge10 pB0 jsShow clearBoth">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="6" class="txtLeftTh">@text.baseInfo@</th>
		         </tr>
		     </thead>
		     <tbody>
		         <tr>
		         	<td class="rightBlueTxt">@CMC.GP.groupId@:</td>
					<td><select id="groupId" style="width:52px" class="normalSel"></select></td>
					<td class="rightBlueTxt">@CMC.GP.adminStatus@:</td>
					<td>
						<input type="radio" name="adminStatus" value="1"/>@CMC.text.yes@ 
						<input type="radio" name="adminStatus" value="2" checked="checked"/>@CMC.GP.no@
					</td>
					<td width=120 class="rightBlueTxt">@CMC.GP.hopPer@:</td>
					<td><input type=text id="hopPeriod" class="normalInput w50" toolTip="@CMC.GP.hopPeriodnote@" 
					maxlength="4" value ="25" />S</td>
		         </tr>
		         <tr>
					<td width=120 class="rightBlueTxt">@CMC.GP.maxHopLimit@:</td>
					<td><input type=text id="maxHopLimit" class="normalInput w50"
						maxlength="2" value = "10" toolTip='@CMC.GP.maxHopLimitnote@'
						/></td>
					<td class="rightBlueTxt">@CMC.GP.snrfirstthreshold@:</td>
					<td><input type=text id="snrThres1" class="normalInput w50" 
						maxlength="4" value = "20" toolTip='@CMC.GP.snrfirstthrnote@'/>dB</td>
					<td class="rightBlueTxt">@CMC.GP.snrsecondthreshold@:</td>
					<td><input type=text id="snrThres2" class="normalInput w50"
						maxlength="4" value = "15" toolTip='@CMC.GP.snrfirstthrnote@'
						/>dB</td>
				 </tr>
				 <tr>
					<td class="rightBlueTxt">@CMC.GP.fecThresCorrect1@:</td>
					<td><input type=text id="fecThresCorrect1" class="normalInput w50"  
						maxlength="3" value = "12" toolTip='@CMC.GP.fecThresCorrectnote@'
						/>%</td>
					<td class="rightBlueTxt">@CMC.GP.fecThresCorrect2@:</td>
					<td><input type=text id="fecThresCorrect2" class="normalInput w50" 
						maxlength="3" value = "20" toolTip='@CMC.GP.fecThresCorrectnote@'
						/>%</td>
					<td class="rightBlueTxt">@CMC.GP.fecThresUnCorrect1@:</td>
					<td><input type=text id="fecThresUnCorrect1" class="normalInput w50"  
						maxlength="3" value = "15" toolTip='@CMC.GP.fecThresCorrectnote@'
						/>%</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CMC.GP.fecThresUnCorrect2@:</td>
					<td colspan="5"><input type=text id="fecThresUnCorrect2" class="normalInput w50" 
						maxlength="3" value = "20" toolTip='@CMC.GP.fecThresCorrectnote@'
						/>%</td>
				</tr>
		     </tbody>
	    </table> 
	</div>
	<div class="edge10 pB0 jsShow clearBoth">
		 <div id="freqGrid"></div>
	</div>
	<div class="edge10 pB0 jsShow clearBoth">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="5" class="txtLeftTh">@CMC.GP.hopPolicy@</th>
		         </tr>
		     </thead>
		     <tbody>
		     	<tr>					
					<td><input type="radio" name="groupPolicy" value="1"/>@CMC.GP.freqOnly@</td> 
					<td><input type="radio" name="groupPolicy" value="2"/>@CMC.GP.widthOnly@</td>
					<td><input type="radio" name="groupPolicy" value="3" checked= "checked"/>@CMC.GP.modulationOnly@</td>
					<td><input type="radio" name="groupPolicy" value="4"/>@CMC.GP.freqWidthOnly@</td>
					<td><input type="radio" name="groupPolicy" value="5"/>@CMC.GP.priority@</td>
				</tr>
		     </tbody>
		 </table>
	</div>
	<div class="edge10 pB0 jsShow clearBoth">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="3" class="txtLeftTh">@CMC.GP.prioritypolicy@</th>
		         </tr>
		     </thead>
		     <tbody>
		     	<tr>
					<td>
						<label class="blueTxt">@CMC.GP.Priority1st@:</label>
						<select id="groupPriority1st" style="height:19px;width:80px" onchange="selectPriority1Change()">
							<option value="2">@CHANNEL.width@</option>
							<option value="3" selected="selected">@CM.modStyle@</option>
						</select>
					</td>
					<td>
						<label class="blueTxt">@CMC.GP.Priority2st@:</label>
						<select id="groupPriority2st" style="height:19px;width:80px" onchange="selectPriority2Change()">
						</select>
					</td>
					<td>
						<label class="blueTxt">@CMC.GP.Priority3st@:</label>
						<select id="groupPriority3st" style="height:19px;width:80px" onchange="selectPriority3Change()">
						</select>
					</td>
				</tr>
		     </tbody>
		 </table>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a id="saveBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@VLAN.determine@</span></a></li>
	         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span>@CMC.button.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>