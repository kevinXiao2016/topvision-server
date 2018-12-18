<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/nocache.inc" %>
<%@ include file="/include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>  
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
Ext.Ajax.timeout = 100000;
var entityId = <s:property value="entityId"/>;
var uniIndex = <s:property value = "uniIndex"/>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var uniIgmpValue = ${uniIgmpValue};
if(uniIgmpValue.join("") == "false"){
	uniIgmpValue = new Array();
}
//转换列表
var transRule = ${transList};
if(transRule.join("") == "false"){
	transRule = new Array();
}
var transGrid;
var transStore;
var transData = new Array();
//MVID
var mvidDataTmp = ${uniMvidList};
if(mvidDataTmp.join("") == "false"){
	mvidDataTmp = new Array();
}
var mvidData = mvidDataTmp.slice(0);


//Trans列表
function transContext(grid,rowIndex,e){
	grid.selModel.selectRow(rowIndex,true);
	transMenu.showAt(e.getXY());
}
var transMenu = new Ext.menu.Menu({
    id:'transMenu',
    enableScrolling: false,
    items:[{
        id:'id1',
        text: I18N.UNI.mdfUniTransRule,
        disabled:!operationDevicePower,
        handler : showIgmpTransJsp
    }]
});
function showIgmpTransJsp(){
	window.parent.createDialog("igmpTranslation", I18N.UNI.mvlanTransRuleMgmt , 450, 380, "epon/igmp/showIgmpTranslation.tv?entityId="+entityId,
			 null, true, true);
}
function ruleToData(){
	var j = 0;
	for(var k=0; k<transRule.length; k++){
		for(var t=8; 17-2*t<transRule[k].length; t--){
			transData[j] = new Array();
			transData[j][0] = transRule[k][0];
			transData[j][1] = transRule[k][17-2*t];
			transData[j][2] = transRule[k][18-2*t];
			transData[j][3] = (17 - transRule[k].length)/2;
			j++;
		}
	}
}
function loadTransGrid(){
	if(transGrid==null || transGrid==undefined){
		ruleToData();
	}
	transStore = new Ext.data.SimpleStore({
		data : transData,
		fields : ['transId','transOldId','transNewId','leaveNum']
	});
	transGrid = new Ext.grid.GridPanel({
		title: I18N.UNI.mvlanTransRuleList ,
		id : 'TransGrid',
		renderTo : 'transGrid',
		store : transStore,
		width : 260,
		height : 320,
		frame : false,
		autoScroll : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: I18N.UNI.transRuleId ,
				dataIndex: 'transId',
				align: 'center',
				width: 90
			},{
				header: I18N.IGMP.originId,
				dataIndex: 'transOldId',
				align: 'center',
				width: 70
			},{
				header: I18N.IGMP.newMvlanId,
				dataIndex: 'transNewId',
				align: 'center',
				width: 80
			},{
				header: I18N.IGMP.restRules,
				dataIndex: 'leaveNum',
				align: 'center',
				width: 30,
				hidden: true
			}],
		listeners: {
			'rowcontextmenu': function(grid,rowIndex,e){
				transContext(grid,rowIndex,e);
	        },
	        'dblclick':{
	           	fn : doubleClick,
				scope : this
	        }
		}
	});
}
function doubleClick(){
	var selectedRow = transGrid.getSelectionModel().getSelected();
	var transIdTemp = selectedRow.get('transId');
	if(operationDevicePower){
		$("#uniTransId").val(transIdTemp);
		$("#saveBt").attr("disabled",false);
	}
}
/**
 * onReady
 */
Ext.onReady(function(){
 	loadTransGrid();
	$("#uniMaxNum").val(uniIgmpValue[0]);
	var tmpAA = [1, 2, 3];
	if(!uniIgmpValue[1] || tmpAA.indexOf(uniIgmpValue[1]) == -1){
		uniIgmpValue[1] = 1;
	}
	if(uniIgmpValue[1]==2){
		$("#uniTransId").val(uniIgmpValue[2]);
	}
	$("#uniMode").val(uniIgmpValue[1]);
	uniModeChange();
	$("#uniMaxNum").focus();
	initButton("all");
	resetBtClick(); 
});

function refreshClick(){
	var params = {
		entityId : entityId,
		uniIndex : uniIndex
	};
	var url = '/epon/igmp/refreshIgmpUni.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.UNI.fetchingUniIgmpInfo , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoEr);
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoOk);
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.fetchUniIgmpInfoEr);
		},
		params : params
	});
}
function cancelClick(){
	window.parent.closeWindow('igmpUniMgmt');
}
//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;
		break;
	case 2: num = (index & 0xFF0000) >> 16;
		break;
	case 3: num = (index & 0xFF00) >> 8;
		break;
	case 4: num = index & 0xFF;
		break;
	} 
	return num;
}
//通过index获得location
function getUniLocationByIndex(index){
	var t = parseInt(index / 256) + (index % 256);
	var uniLoc = "";
	var i = 4;
	while (i > 0) {
		uniLoc = getNum(t, i) + uniLoc;
		if(i & 1){
			if(i & 2){
				uniLoc = ":" + uniLoc;
			}
		}else{
			uniLoc = "/" + uniLoc;
		}
		i--;
	}
	return uniLoc;
}
function addInputKeyup(){
	var v = $("#addInput").val();
	$("#addBt").attr("disabled", true);
	$("#addBt").mouseout();
	$("#searchBt").attr("disabled", true);
	$("#searchBt").mouseout();
	if(v){
		if(checkedInputList(v)){
			if(operationDevicePower){
				$("#addBt").attr("disabled", false);
			}
			var reg = /^[0-9]+$/g;
			if(reg.exec(v) && 0 < parseInt(v) && parseInt(v) < 4095){
				$("#searchBt").attr("disabled", false);
			}
		}
	}else{
		$("#addInput").css("border", "1px solid #8bb8f3");
	}
}
function checkedInputList(v){
	$("#addInput").css("border", "1px solid #ff0000");
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
    if (reg.exec(v)) {
    	var tmp = v.replace(new RegExp('-', 'g'), ',');
    	var tmpA = tmp.split(',');
    	var tmpAl = tmpA.length;
		for(var i=0; i<tmpAl; i++){
			if(parseInt(tmpA[i]) > 4094 || tmpA[i] == 0){
				return false;
			}
		}
		$("#addInput").css("border", "1px solid #8bb8f3");
    	return true;
    }
    return false;
}
//解析逗号和连字符组成的字符串为数组
function changeToArray(v){
	var re = new Array();
	var t = v.split(",");
	var tl = t.length;
	for(var i=0; i<tl; i++){
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if(ttI > 0){
			var ttt = tt.split("-");
			if(ttt.length == 2){
				var low = parseInt(parseInt(ttt[0]) > parseInt(ttt[1]) ? ttt[1] : ttt[0]);
				var tttl = Math.abs(parseInt(ttt[0]) - parseInt(ttt[1]));
				for(var u=0; u<tttl + 1; u++){
					re.push(low + u);
				}
			}
		}else if(ttI == -1){
			re.push(parseInt(tt));
		}
	}
	var rel = re.length;
	if(rel > 1){
		var o = {};
		for(var k=0; k<rel; k++){
			o[re[k]] = true;
		}
		re = new Array();
		for(var x in o){
			if (o.hasOwnProperty(x)) {
				re.push(x); 
			} 
		}
		re.sort(function(a, b){
			return a - b;
		});
	}
	return re;
}
function addBtClick(){
	var v = $("#addInput").val();
	if(checkedInputList(v)){
		v = changeToArray(v);
		var lastOne = null;
		var tmpL = new Array();
		for(var a=0; a<v.length; a++){
			if(mvidData.indexOf(v[a]) == -1){
				if(mvidData.length < 64){
					mvidData.push(v[a]);
					addBt(v[a]);
					lastOne = v[a];
				}else{
					tmpL.push(v[a]);
				}
			}
		}
		$("#addInput").val("").keyup();
		if(lastOne){
			$("#tmpButton" + lastOne).focus();
		}
		if(tmpL.length > 0){
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.maxMvlanTip , tmpL.join("、")))
		}
	}
}
function addBt(v){
	if(operationDevicePower){
		$("#buttonDiv").append("<button id=tmpButton"+v+" class=buttonOutClass onmouseover='buttonOver(this)' onmouseout='buttonOut(this)' " +
				"onclick='deleteClick(this)' style='margin:2px;font-size:16;' title='"+ I18N.UNI.clickDel +"' >" + v + "</button>");
	}else{
		$("#buttonDiv").append("<button id=tmpButton"+v+" class=buttonOutClass  style='margin:2px;font-size:16;'>" + v + "</button>");
	}
}
function searchBtClick(){
	var v = $("#addInput").val();
	$("#tmpButton" + v).focus();
}
function resetBtClick(){
	$("#buttonDiv").empty();
	var o = {};
	for(var k=0; k<mvidDataTmp.length; k++){
		o[mvidDataTmp[k]] = true;
	}
	var re = new Array();
	for(var x in o){
		if (o.hasOwnProperty(x)) {
			re.push(x); 
		}
	}
	re.sort(function(a, b){
		return a - b;
	});
	for(var a=0; a<re.length; a++){
		addBt(re[a]);
	}
	mvidData = mvidDataTmp.slice(0);
}
function deleteClick(el){
	$(el).fadeOut(700);
	mvidData.splice(mvidData.indexOf(parseInt($(el).text())), 1);
}
function buttonOut(el){
	$(el).removeClass("buttonOverClass");
	$(el).addClass("buttonOutClass");
}
function buttonOver(el){
	$(el).removeClass("buttonOutClass");
	$(el).addClass("buttonOverClass");
}
function checkedNum(s){
	if(operationDevicePower){
		$("#saveBt").attr("disabled",false);
	}
	var reg = /^([0-9])+$/ig;
	if(s == 1){
		$("#uniMaxNum").css("border","1px solid #8bb8f3");
		var tempNum = $("#uniMaxNum").val();
		if(parseInt(tempNum)>64 || isNaN(tempNum) || !reg.exec(tempNum)){
			$("#uniMaxNum").css("border","1px solid #FF0000");
			return false;
		}
		return true;
	}else if(s == 2){
		$("#uniTransId").css("border","1px solid #8bb8f3");
		var tempNum = $("#uniTransId").val();
		var tempNumber = 0;
		transStore.filterBy(function(record){
			if(record.get('transId') == tempNum){
				tempNumber++;
			}
			return record.get('transId') == tempNum;
		});
		if(tempNumber == 0){
			transStore.filterBy(function(record){
				return true;
			});
		}
		if(parseInt(tempNum)>64 || isNaN(tempNum) || !reg.exec(tempNum)){
			$("#uniTransId").css("border","1px solid #FF0000");
			return false;
		}
		return true;
	}
}
function uniModeChange(){
	if(operationDevicePower){
		$("#saveBt").attr("disabled",false);
	}
	var uniMode = $("#uniMode").val();
	if(uniMode == 2){
		$(".transSelect").show();
		$("#transGrid").empty();
		loadTransGrid();
		$("#buttonDiv").width(380).css("border-right", "1px solid #8bb8f3");
	}else{
		$(".transSelect").hide();
		$("#buttonDiv").width(650).css("border-right", "0px");
	}
}
function saveBtClick(){
	var uniLoc = getUniLocationByIndex(uniIndex);
	var maxGroupNum = $("#uniMaxNum").val();
	var mode = $("#uniMode").val();
	var transId = 0;
	if(!checkedNum(1)){
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.maxMvlanRange );
		return;
	}else{
		maxGroupNum = parseInt(maxGroupNum);
	}
	if(mode == 2){
		if(!checkedNum(2)){
			$("#saveBt").attr("disabled",true);
			$("#saveBt").mouseout();
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.plsIptCorrectRange )
			return;
		}else{
			transId = parseInt($("#uniTransId").val());
		}
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.UNI.mdfingUniIgmpInfo , uniLoc), 'ext-mb-waiting');
	var tmpStr = mvidData.join();
	var params = {
		entityId : entityId,
		uniIndex : uniIndex,
		maxGroupNum : maxGroupNum,
		uniVlanMode : mode,
		transId : transId,
		uniPortMvidList : tmpStr
	};
	var url = '/epon/igmp/modifyIgmpMcUniConfig.tv';
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniIgmpInfoEr , uniLoc))
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniIgmpInfoOk , uniLoc))
			cancelClick();
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.UNI.mdfUniIgmpInfoEr , uniLoc))
		},
		params : params
	});
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#cancelBt").attr("disabled",false);
		$("#addInput").attr("disabled",false);
		$("#refreshBt").attr("disabled",false);
	}
}
</script>
</head>
<body class=POPUP_WND style="margin: 15px;overflow:hidden;" onload="authLoad()">
<fieldset style="width:678;height:360px;background-color:white;">
	<table>
		<tr><td>
			<table style='margin:5px;'>
				<tr style="height:35px;"><td style="width:110px;" align=center><fmt:message bundle="${i18n}" key="IGMP.maxMvlan" />
				</td><td>
					<input id="uniMaxNum" value="4" maxlength=2 style="width:40px;" onkeyup=checkedNum(1) />
				</td><td id="maxNumText" align=center width=100><fmt:message bundle="${i18n}" key="UNI.range64" />
				</td><td style="width:110px;" align=center><fmt:message bundle="${i18n}" key="UNI.mvidHandleMode" />
				</td><td>
					<select id="uniMode" style="width:60px;" onchange=uniModeChange()>
						<option value=1><fmt:message bundle="${i18n}" key="UNI.strip" /></option>
						<option value=2><fmt:message bundle="${i18n}" key="UNI.trans" /></option>
						<option value=3><fmt:message bundle="${i18n}" key="UNI.substance" /></option>
					</select>
				</td><td>
				</td><td style="width:130px;display:none;" align=center class=transSelect>&nbsp;&nbsp;&nbsp;&nbsp;
					<fmt:message bundle="${i18n}" key="UNI.mvlanTransRuleId" />
				</td><td style="display:none;" class=transSelect>
					<input id="uniTransId" maxlength=2 style="width:50px;" onkeyup=checkedNum(2) title='<fmt:message bundle="${i18n}" key="UNI.range64" />' />
				</td></tr>
			</table>
		</td></tr>
		<tr><td>
			<hr size=1 style="filter:alpha(opacity=100,opacity=5,style=1);position:absolute;width:600px;color:#1973b4;left:20px;">
		</td></tr>
		<tr><td>
			<table style="margin:5px;">
				<tr><td>
					<fmt:message bundle="${i18n}" key="UNI.mvlanNeedHandleList" /><br>
					<input id=addInput onkeyup='addInputKeyup()' style='width:137px;border:1px solid #8bb8f3;' />&nbsp;&nbsp;
					<button id=addBt class=BUTTON75 onclick='addBtClick()' disabled>
						<fmt:message bundle="${i18n}" key="SERVICE.add" />
					</button>&nbsp;&nbsp;
					<button id=searchBt class=BUTTON75 onclick='searchBtClick()' disabled>
						<fmt:message bundle="${i18n}" key="COMMON.query" />
					</button>&nbsp;&nbsp;
					<button id=resetBt class=BUTTON75 onclick='resetBtClick()' >
						<fmt:message bundle="${i18n}" key="COMMON.reset" />
					</button>
					<div id=buttonDiv style="width:380;height:280;overflow-y:auto;border-right:1px solid #8bb8f3;padding:5px;"></div>
				</td><td>
					<div style="display:none;margin-left:10px;" id=transGrid class=transSelect></div>
				</td></tr>
			</table>
		</td></tr>
	</table>
</fieldset>
	<%-- <div align="right" style="padding-right:1px;padding-top:2px;">
		<button id=refreshBt class=BUTTON95 type="button" onclick="refreshClick()"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>&nbsp;&nbsp;
		<button id=saveBt class=BUTTON95 type="button" disabled	onclick="saveBtClick()"><fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>&nbsp;&nbsp;
		<button id=cancelBt class=BUTTON95 type="button" onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
	</div> --%>
	<ol class="upChannelListOl pB0 pT10 noWidthCenter">
         <li><a id="refreshBt" href="javascript:;" class="normalBtnBig" onclick="refreshClick()"><span><i class="miniIcoEquipment"></i><fmt:message bundle="${i18n}" key="COMMON.fetch" /></span></a></li>
         <li><a id="saveBt" href="javascript:;" class="normalBtnBig" onclick="saveBtClick()"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${i18n}" key="COMMON.save" /></span></a></li>
         <li><a id="cancelBt" href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i><fmt:message bundle="${i18n}" key="COMMON.cancel" /></span></a></li>
    </ol>

	
</body>
<style>
.buttonOverClass{
	width : 111px;
	border: 0px;
	height: 36px;
	background: url(/images/config/btDelOver.png) 0 0 no-repeat;
}
.buttonOutClass{
	width : 111px;
	border: 0px;
	height: 36px;
	background: url(/images/config/btDelOut.png) 0 0 no-repeat;
}
</style>
</html>