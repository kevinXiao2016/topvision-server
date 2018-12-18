<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<style type="text/css">
.fileElement{position:absolute;filter:alpha(opacity=0); opacity:0; -moz-opacity:0; width:75px;height:22px;cursor:hand;left:355;}
#hwSel,#fileSel{display:none;position:absolute;width:200;z-index:100;border:none;background-color:white;overflow-y:auto;'}
</style>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE EPON
    plugin DateTimeField
    IMPORT js.tools.ipText
    IMPORT js.zetaframework.VersionControl 
</Zeta:Loader>
<script type="text/javascript">
var entityId = ${entityId};
var onuTypeInt = ${onuTypeMap};
var proList = ${profileIdList};
if(proList.join("") == "false"){
	proList = new Array();
}
var onuUpgradeFileList = new Array();
var fileStrList = new Array();
var fileStrNameList = new Array();

var onuHwList = ${onuHardwareVersionList};
if(onuHwList.join("") == "false"){
	onuHwList = new Array();
}

function initTime(){
	updateTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : null,
		editable: true,
		renderTo:"updateTimeInput",
		emptyText:'@ONU.upgradeTimeTip@',
	    blankText:'@ONU.upgradeTimeTip@',
	    format : 'Y-m-d H:i',  //日期格式
	    minValue : '2000-01-01 00:00',
	    maxValue : '2038-12-31 23:59',
	    listeners: {
	    	"select": function () {
	    		/* var selectTime = Ext.util.Format.date(window.updateTime.getValue(), 'Y-m-d H:i');
	    		var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i');
	    		if(selectTime < currentTime){
	    			alert("select time error !")
	    		} */
	    	}
	    }
	});
}

function initFileList(){
	var params = {entityId : entityId}
	Ext.Ajax.request({
		url : '/onu/loadOnuUpgFileList.tv?&r=' + Math.random(),
		success : function(response) {
			var v = Ext.util.JSON.decode(response.responseText);
			if(v.data){
				onuUpgradeFileList = v.data;
				for(var a=0; a<onuUpgradeFileList.length; a++){
					fileStrList.push(onuUpgradeFileList[a].filePath + onuUpgradeFileList[a].fileName);
					fileStrNameList.push(onuUpgradeFileList[a].fileName);
				}
			}else{
				setTimeout(function(){
					initFileList();
				}, 15000);
			}
		},
		failure : function(response) {
			setTimeout(function(){
				initFileList();
			}, 5000);
		},
		params: params
	});
}

function addBtClick(){
	var profileId = $("#proIdSel").val();
	var proName = $("#proName").val();
	//var updateTime = Ext.util.Format.date(window.updateTime.getValue(), 'Y-m-d H:i');
	var updateTime = window.updateTime.getValue();
	if(updateTime != null && updateTime != ""){
		updateTime = Ext.util.Format.date(window.updateTime.getValue(), 'Y-m-d H:i');
	}
	var proNewVersion = $("#proNewVersion").val();
	var proMode = $("#proMode").val();
	var onuHwVersion = $("#onuHwVersion").val();
	var onuType = $("#onuType").val();
	var pers = $("#pers").val();
	var webs = $("#webs").val();
	var other = $("#other").val();
	var boot = $("#boot").val();
	var app = $("#app").val();
	/* if(!pers){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.mustSelPers);
		return;
	} */
	if(!onuHwVersion && onuType == 0){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.mustSelOnu);
		return;
	}
	if(!proNewVersion){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.mustSelVersion);
		return;
	}
	if(!checkedAllInput()){
		return;
	}
	var uploadFile = needToUpload([pers, webs, boot, app, other]);
	
	// add by fanzidong,选择的文件必须有文件名称
	var fileNoName = false,
        fileInputs = ['pers', 'webs', 'boot', 'app', 'other'];
	for(var i=0, len=fileInputs.length; i<len; i++) {
		var fileName = $('#' + fileInputs[i]).val();
		var file = $('#selectFile' + fileInputs[i]).val();
		if(file && !fileName) {
			$('#' + fileInputs[i]).focus();
			fileNoName = true;
			break;
		}
	}
	if(fileNoName){
		return;
	}
	
	
	if(uploadFile.length > 0){
		submitFile();
		return;
	}
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.addingPro);
	var params = {entityId : entityId, profileId : profileId, profileName : proName, proNewVersion : proNewVersion, proMode : proMode,
		proHwVersion : onuHwVersion, proOnuType : onuType, proPers : pers, proWebs : webs, proOther : other, proBoot : boot, proApp : app,
		onuAutoUpgFlag : 0, proUpgTime : updateTime}
	$.ajax({
		url : '/onu/addOnuAutoUpgProfile.tv',cache:false,
		data: params,
		success : function() {
			if(window.parent.getWindow){
				if(window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.refreshThisWindow){
					window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.refreshThisWindow();
				}
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.addProSuc);
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@onuAutoUpg.mes.addProSuc@</b>'
	       	    });
				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyProFailed);
			}
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.addProFailed);
		}
	});
}
var needToUploads = new Array();
function needToUpload(list){
	var re = new Array();
	needToUploads = new Array();
	var tmpSt = ['pers', 'webs', 'boot', 'app', 'other'];
	for(var a=0; a<list.length; a++){
		if(list[a]){
			if(fileStrList.indexOf(list[a]) == -1 && fileStrNameList.indexOf(list[a]) == -1 && re.indexOf(list[a]) == -1){
				re.push(list[a]);
				needToUploads.push(tmpSt[a]);
			}
		}
	}
	return re;
}
function submitFile(){
	var form = document.getElementById("form1");
	var profileId = $("#proIdSel").val();
	var proName = $("#proName").val();
	var proNewVersion = $("#proNewVersion").val();
	var proMode = $("#proMode").val();
	var onuHwVersion = $("#onuHwVersion").val();
	var onuType = $("#onuType").val();
	var pers = $("#pers").val().split("\\");
	pers = pers[pers.length - 1];
	var webs = $("#webs").val().split("\\");
	webs = webs[webs.length - 1];
	var other = $("#other").val().split("\\");
	other = other[other.length - 1];
	var boot = $("#boot").val().split("\\");
	boot = boot[boot.length - 1];
	var app = $("#app").val().split("\\");
	app = app[app.length - 1];
	//自动升级时间
	var updateTime = window.updateTime.getValue();
	if(updateTime != null && updateTime != ""){
		updateTime = Ext.util.Format.date(window.updateTime.getValue(), 'Y-m-d H:i');
	}
	form.action = "/onu/uploadFileInOnuAutoUpg.tv?entityId=" + entityId + "&profileId=" + profileId + "&profileName=" + proName
		+ "&proNewVersion=" + proNewVersion + "&proMode=" + proMode + "&proHwVersion=" + onuHwVersion + "&proOnuType=" + onuType
		+ "&proPers=" + pers + "&proWebs=" + webs + "&proOther=" + other + "&proBoot=" + boot + "&proApp=" + app
		+ "&onuAutoUpgFlag=0" + "&proUpgTime=" + updateTime; //0:add, 1:modify
	if(needToUploads.length > 0){
		var tmpSt = ['pers', 'webs', 'boot', 'app', 'other'];
		for(var i=0; i<tmpSt.length; i++){
			if(needToUploads.indexOf(tmpSt[i]) == -1){
				form.removeChild(document.getElementById("selectFile" + tmpSt[i]));
			}
		}
	}
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.addingPro);
	form.submit();
}
function cancelClick(){
	window.parent.closeWindow('onuAutoUpgAddPro');
}

Ext.onReady(function(){
	initOnuProMode();
	initOnuTypeSel();
	$(".modifyClass").css("border", "1px solid #8bb8f3");
	initProfileId();
	$("#proName").focus();
	initFileList();
	initSelectFileBtLoc();
	initTime();
});

$(function(){
	//绑定file选择框事件
	$('input[type="file"]').change(function(){
        selectFileChange();
        //$(this).val("");
    });
})

function initOnuProMode() {
	var verControl = VersionControl.supportNode('onuUpgProfileAutoMode', entityId);
	$("#proMode").find("option[value='64']").attr("hidden",verControl.hidden);
}

function initOnuTypeSel(){
	var pa = $("#onuType");
	var list = new Array();
	for(var x in onuTypeInt){
		if(typeof x == "string"){
			list[onuTypeInt[x]] = x;
		}
	}
	for(var a=0; a<list.length; a++){
		if(list[a]){
			if(a == 241)continue;
			if(list[a] == 255){
				pa.append(String.format("<option value={0}>{1}</option>", 0, "ANY"));
			}else{
				pa.append(String.format("<option value={0}>{1}</option>", a, list[a]));
			}
		}
	}
}
function initProfileId(){
	var p = $("#proIdSel");
	for(var i=1; i<21; i++){
		if(proList.indexOf(i) == -1){
			p.append(String.format("<option value={0}>{0}</option>", i));
		}
	}
}
var selectedFileId = "";
function selectFile(id){
	selectedFileId = id;
}
function selectFileChange(){
	if(selectedFileId){
		var v = $("#selectFile" + selectedFileId).val().split("\\");
		$("#" + selectedFileId).val(v[v.length - 1]);
		$("#" + selectedFileId + "FileName").val(v[v.length - 1]);
		checkedChinese(selectedFileId);
	}
}
function notToHide(s){
	if(s == 1){
		needToHide1 = false;
	}else if(s == 2){
		needToHide2 = false;
	}
}
var needToHide1 = true;
function fileFocus(id, v){
	needToHide1 = false;
	var p = $("#fileSel");
	p.empty().height(0).hide();
	selectedFileId = id;
	var len = onuUpgradeFileList.length;
	if(len > 0){
		for(var a=0;a<onuUpgradeFileList.length; a++){
			var tmpSs = onuUpgradeFileList[a].fileName;
			if(!v || tmpSs.indexOf(v) > -1){
				p.append(String.format("<span style='cursor:hand;width:100%;height:20px;padding-left:5px;padding-right:5px;' "
						+ "onclick=' fileSpanClick(this)' {1} onfocus='notToHide(1)'>{0}</span><br>", tmpSs,
						"onmouseover='fileSpanOver(this)' onmouseout='fileSpanOut(this)' onblur='fileBlur()'"));
			}else{
				len--;
			}
		}
		if(len == 0){
			p.append("<span style='width:100%;height:20px;padding-left:5px;padding-right:5px;'>"
					+ I18N.onuAutoUpg.mes.needUploadThis + "</span>");
		}
		if(len > 7){
			p.height(142);
		}else{
			p.height( 20 * onuUpgradeFileList.length );
		}
		var pos = getElPositionById(id);
		var topH = pos[1];
		if(id == "webs" || id == "other" || id == "app"){
			topH -= p.height() - 1;
		}else{
			topH += 20;
		}
		p.css({left: pos[0] + 2, top: topH}).show();
	}
}
function fileBlur(){
	needToHide1 = true;
	setTimeout(function(){
		if(needToHide1){
			$("#fileSel").hide();
		}
	}, 200);
}
function fileKeyup(id){
	var v = $("#" + id).val();
	fileFocus(id, v);
}
function fileSpanClick(el){
	$("#" + selectedFileId).val($(el).text());
	$("#fileSel").hide();
	$('#selectFile'+selectedFileId).val("");
	$('#'+selectedFileId + 'FileName').val("");
	checkedChinese(selectedFileId);
}
function fileSpanOver(el){
	$(el).css("background-color", "#cccccc");
}
function fileSpanOut(el){
	$(el).css("background-color", "white");
}
var needToHide2 = true;
function hwFocus(v){
	needToHide2 = false;
	var p = $("#hwSel");
	p.empty().height(0).hide();
	var len = onuHwList.length;
	if(len > 0){
		for(var b=0; b<onuHwList.length; b++){
			if(onuHwList[b] && (!v || onuHwList[b].indexOf(v) > -1)){
				p.append(String.format("<span style='cursor:hand;width:100%;height:20px;padding-left:5px;padding-right:5px;' "
						+ "onclick='hwSpanClick(this)' {1} onfocus='notToHide(2)'>{0}</span><br>",
						onuHwList[b], "onmouseover='hwSpanOver(this)' onmouseout='hwSpanOut(this)' onblur='hwBlur()'"));
			}else{
				len--;
			}
		}
		if(len == 0){
			p.append("<span style='width:100%;height:20px;padding-left:5px;padding-right:5px;'>"
					+ I18N.onuAutoUpg.mes.noMatchFile + "</span>");
		}
		if(len > 10){
			p.height(202);
		}
		var pos = getElPositionById("onuHwVersion");
		p.css({left: pos[0] + 2, top: pos[1] + 20}).show();
	}
}
function hwBlur(){
	needToHide2 = true;
	setTimeout(function(){
		if(needToHide2){
			$("#hwSel").hide();
		}
	}, 200);
}
function hwKeyup(){
	var v = $("#onuHwVersion").val();
	hwFocus(v);
}
function hwSpanClick(el){
	$("#onuHwVersion").val($(el).text());
	$("#hwSel").hide();
	checkedChinese("onuHwVersion");
}
function hwSpanOver(el){
	$(el).css("background-color", "#cccccc");
}
function hwSpanOut(el){
	$(el).css("background-color", "white");
}
//浏览按钮的位置重定位
function initSelectFileBtLoc(){
	var htmlId = ["pers", "boot", "app", "webs", "other"];
	for(var a=0; a<htmlId.length; a++){
		var tmpLoc = getElPositionById(htmlId[a] + "Bt");
		$("#selectFile" + htmlId[a]).css({left: (tmpLoc[0] + 2), top: (tmpLoc[1] + 1)}).mouseover(function(){
			$("#" + this.id.substring(10) + "Bt").mouseover();
		}).mouseout(function(){
			$("#" + this.id.substring(10) + "Bt").mouseout();
		});
	}
}
/*
 * 页面输入统一验证
 */
function checkedAllInput(){
	var ids = ['proName', 'onuHwVersion', 'proNewVersion', 'boot', 'app', 'webs', 'other', 'pers'];
	for(var a=0; a<ids.length; a++){
		var p = $("#" + ids[a]);
		var le = p.val().length;
		p.css("border", "1px solid #8bb8f3");
		if(a == 1){
			if(le > 8){
				p.css("border", "1px solid #ff0000");
				return false;
			}
		}else if(a == 2){
			if(le > 16){
				p.css("border", "1px solid #ff0000");
				return false;
			}
		}else{
			if(le > 31){
				p.css("border", "1px solid #ff0000");
				return false;
			}
		}
		if(!checkedChinese(ids[a])){
			return false;
		}
	}
	return true;
}
function checkedChinese(id){
	var p = $("#" + id);
	p.css("border", "1px solid #8bb8f3");
	if(!p.val()){
		return true;
	}
	var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!#$%^&=+])+$/ig;
	if(!reg.exec(p.val())){
		p.css("border", "1px solid #ff0000");
		return false;
	}
	return true;
}

</script>
</head>
	<body class="openWinBody">
		<div class="formtip" id="tips" style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@onuAutoUpg.he.addOnuUpgTem@,@onuAutoUpg.he.addOnuTemDesc@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="w100 rightBlueTxt">@onuAutoUpg.he.proId@:</td>
					<td colspan=3><select id=proIdSel class="normalSel" style="width:240px;"></select></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@onuAutoUpg.he.profileName@:</td>
					<td><input id=proName class="modifyClass normalInput" style="width:240px;"
						maxlength=31 tooltip='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@'
						onkeyup='checkedChinese(this.id)' onblur='checkedChinese(this.id)' />
					</td>
					<td class="rightBlueTxt w100">@onuAutoUpg.he.startTime@:</td>
						<td><div id="updateTimeInput" style="width:240px;"></div>
					</td>
				</tr>
				<!-- mode && newVersion -->
				<tr>
					<td class="rightBlueTxt">@COMMON.required@@onuAutoUpg.pro.newVersion@:</td>
					<td class="w160"><input id=proNewVersion class="modifyClass normalInput" style="width:240px;"
						maxlength=16 title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max16@'
						onkeyup='checkedChinese(this.id)' onblur='checkedChinese(this.id)' />
					</td>
					<td class="rightBlueTxt w100">@onuAutoUpg.pro.upgMode@:</td>
					<td><select id=proMode class="modifyClass normalSel" style="width:240px;">
						<option value=4>CTC</option>
						<option value=16>baseline</option>
						<option value=32>extend</option>
						<option value=64>auto</option>
					</select></td>
				</tr>
				<!-- onuType && hwVersion -->
				<tr>
					<td class="rightBlueTxt">@onuAutoUpg.pro.hwVersion@:</td>
					<td><input id=onuHwVersion class="modifyClass normalInput" style="width:240px;"
						maxlength=8 title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max8@'
						onfocus='hwFocus()' onblur='hwBlur();checkedChinese(this.id)'
						onkeyup='hwKeyup();checkedChinese(this.id)' /></td>
					<td class="rightBlueTxt w100">@COMMON.required@@onuAutoUpg.he.onuType@:</td>
					<td><select id=onuType class="modifyClass normalSel" style="width:240px;"></select>
					</td>
				</tr>
				<!-- pers -->
				<tr>
					<td class="rightBlueTxt">Pers@onuAutoUpg.pro.file@:</td>
					<td>
						<input id="persFileName" type="text" class="normalInput w160 normalInputDisabled" disabled="disabled"/>
						<button id=persBt class=BUTTON75 onclick='selectFile("pers")'>@onuAutoUpg.co.browse@</button>
					</td>
					<td class="rightBlueTxt">Pers@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
					<td>
                       <input id="pers" class="modifyClass normalInput w240" maxlength=31
                        title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                        onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                    </td>
				</tr>
				<!-- boot -->
				<tr>
					<td class="rightBlueTxt">Boot@onuAutoUpg.pro.file@:</td>
					<td>
					   <input id="bootFileName" type="text" class="normalInput w160 normalInputDisabled" disabled="disabled"/>
					   <button id="bootBt" class=BUTTON75 onclick='selectFile("boot")'>@onuAutoUpg.co.browse@</button>
					</td>
					<td class="rightBlueTxt">Boot@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                    <td>
                       <input id="boot" class="modifyClass normalInput w240" maxlength=31
                        title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                        onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                    </td>
				</tr>
				<!-- app -->
				<tr>
					<td class="rightBlueTxt">App@onuAutoUpg.pro.file@:</td>
					<td>
					   <input id="appFileName" type="text" class="normalInput w160 normalInputDisabled" disabled="disabled"/>
						<button id=appBt class=BUTTON75 onclick='selectFile("app")'>@onuAutoUpg.co.browse@</button>
					</td>
					<td class="rightBlueTxt">App@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                    <td>
                       <input id="app" class="modifyClass normalInput w240" maxlength=31
                        title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                        onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                    </td>
				</tr>
				<!-- webs -->
				<tr>
					<td class="rightBlueTxt">Webs@onuAutoUpg.pro.file@:</td>
					<td>
					   <input id="websFileName" type="text" class="normalInput w160 normalInputDisabled" disabled="disabled"/>
						<button id="websBt" class=BUTTON75 onclick='selectFile("webs")'>@onuAutoUpg.co.browse@</button>
					</td>
					<td class="rightBlueTxt">Webs@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                    <td>
                       <input id="webs" class="modifyClass normalInput w240" maxlength=31
                        title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                        onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                    </td>
				</tr>
				<!-- other -->
				<tr>
					<td class="rightBlueTxt">@onuAutoUpg.pro.otherFile@:</td>
					<td>
					   <input id="otherFileName" type="text" class="normalInput w160 normalInputDisabled" disabled="disabled"/>
						<button id="otherBt" class=BUTTON75 onclick='selectFile("other")'>@onuAutoUpg.co.browse@</button>
					</td>
					<td class="rightBlueTxt">@onuAutoUpg.pro.otherFile@@onuAutoUpg.name@:</td>
                    <td>
                       <input id="other" class="modifyClass normalInput w240" maxlength=31
                        title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                        onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                    </td>
				</tr>
			</table>
		</div>
		<div id=hwSel onfocus='notToHide(2)' style="width: 120px;"></div>
		<div id="fileSel" onfocus='notToHide(1)'></div>
		<!-- 文件上传 -->
		<form id="form1" name="form1" encType="multipart/form-data"
			method="post">
			<input id="selectFilepers" class="fileElement" name=filePers
				type="file"
				onclick='selectFile("pers")' style='top: 133' /> 
			<input
				id="selectFileboot" class="fileElement" name=fileBoot type="file"
				 onclick='selectFile("boot")'
				style='top: 160' /> 
			<input id="selectFileapp" class="fileElement"
				name=fileApp type="file" 
				onclick='selectFile("app")' style='top: 187' /> 
			<input
				id="selectFilewebs" class="fileElement" name=fileWebs type="file"
				 onclick='selectFile("webs")'
				style='top: 214' /> 
			<input id="selectFileother" class="fileElement"
				name=fileOther type="file" 
				onclick='selectFile("other")' style='top: 241' />
		</form>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="addBtClick()" icon="miniIcoAdd">@onuAutoUpg.ac.newPro@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>