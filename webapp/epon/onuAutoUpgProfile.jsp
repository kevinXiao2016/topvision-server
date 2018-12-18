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
    plugin DateTimeField
    import js.tools.ipText
    IMPORT js.zetaframework.VersionControl 
    module epon
</Zeta:Loader>
<style type="text/css">
.fileElement{position: absolute; filter: alpha(opacity = 0); opacity:0; -moz-opacity:0;width: 75px; height: 22px; cursor: hand; left: 484px;}
#hwSel,#fileSel{display:none;position:absolute;width:200;z-index:100;border:1px solid #cccccc;background-color:white;overflow-y:auto;'}
</style>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var proId = <s:property value="profileId" />;
var onuTypeInt = ${onuTypeMap};
var proDataHtmlId = ['proIdSel', 'proName', 'onuType', 'onuHwVersion', 'proNewVersion', 'proMode', 'boot', 'app', 'webs', 'other', 'pers'];
var proData = ${profileList};
if(proData.join("") == "false"){
	proData = new Array();
}

var bandData = new Array();
var bandStore;
var bandGrid;
var updateTime;

var onuUpgradeFileList = new Array();
var fileStrList = new Array();
var fileStrNameList = new Array();

var onuHwList = ${onuHardwareVersionList};
if(onuHwList.join("") == "false"){
	onuHwList = new Array();
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
function loadBandData(){
	bandData = new Array();
	for(var a=0; a<proData.length; a++){
		if(proData[a][0] == proId){
			var d = proData[a][12];
			if( d != null){
				for(var b=0; b<d.length; b++){
					bandData.push([proId, d[b].slotNo, d[b].ponNo]);
				}
			}
			break;
		}
	}
	if(bandData.length == 0){
		$("#tipSpan").hide();
		$(".modifyClass").attr("readonly", false).css("border", "1px solid #8bb8f3");
	}else{
		$("#tipSpan").show();
		$(".modifyClass").attr("readonly", true).css("border", "1px solid #cccccc");
	}
	bandStore.loadData(bandData);
}
function noBackspace(){
	if(event && event.keyCode == 8 && bandData.length > 0){
		return false;
	}
}
function loadBandGrid(){
	var cm = [ {header: I18N.onuAutoUpg.he.ponPort, id:'id1', dataIndex: 'ponNo', align: 'center',
					sortable: true, width:80, renderer: ponRender}
			 , {header: I18N.COMMON.manu, id:'id2', align: 'center', width:80, renderer: bandHandler}];
		bandStore = new Ext.data.SimpleStore({
		data : bandData,
		fields: ['profileId', 'slotNo', 'ponNo']
		});
		bandGrid = new Ext.grid.GridPanel({
			stripeRows:true,cls:"normalTable",bodyCssClass: 'normalTable',
			id : 'BandGrid',
			title : I18N.onuAutoUpg.tit.ponBound,
			renderTo : 'bandGridDiv',
			border : true,
			frame : false,
			autoScroll : true,
			width : 220,
			height : 290,
			store : bandStore,
			columns : cm,
			viewConfig:{ forceFit: true },
			selModel : new Ext.grid.RowSelectionModel({
				listeners : {
					'selectionchange' : function(){
					}
				}
			}),
			listeners : {
				'viewready' : function(){
					initProIdSel();
				},
				'rowcontextmenu': function(grid,rowIndex,e){
					if(!grid.getSelectionModel().isSelected(rowIndex)){
						grid.getSelectionModel().selectRow(rowIndex, false);
					}
					bandMenu.showAt(e.getXY());
				}
			}
	});
}
var bandMenu = new Ext.menu.Menu({items: []});
bandMenu.add([
    {text : I18N.onuAutoUpg.ac.unbandSel ,icon:"", handler: function(){bandOutThePon(1);}},
    {text : I18N.onuAutoUpg.ac.unbandAll ,icon:"", handler: function(){bandOutThePon(0);}}
]);
function ponRender(v, c, record){
	return record.data.slotNo + " / " + v;
}
function bandHandler(v, c, record){
	return String.format("<img src='{0}' title='{1}' onclick='bandOutThePon(1)'>", "/images/delete.gif", I18N.onuAutoUpg.ac.unbound);
}
function bandOutThePon(s){//0:all, 1:解除选中行
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.unbandConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		var list = new Array();
		if(s == 0){
			for(var a=0;a<bandData.length; a++){
				list.push(bandData[a]);
			}
		}else if(s == 1){
			var sel = bandGrid.getSelectionModel().selections.items;
			if(sel[0]){
				for(var a=0; sel[a]; a++){
					list.push([sel[a].data.profileId, sel[a].data.slotNo, sel[a].data.ponNo]);
				}
			}else{
				return;
			}
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.unbandings);
		var re = {flag: false};
		unbandMgmt(list, re);
	});
}
function unbandMgmt(list, re){
	if(list.length > 0){
		unbandAPon(list, re);
	}else if(re.flag){
		var msg = "";
		var sucList = new Array();
		for(var x in re){
			if(x.length == 3 && x.indexOf("/") > -1){
				if(!re[x] || re[x] == 'success'){
					sucList.push(x);
				}else{
					msg += "<nobr>" + String.format(I18N.onuAutoUpg.tip.batchUnbandFailed, x, proId) + re[x] + "</nobr><br>";
				}
			}
		}
		if(sucList.length > 0){
			msg += String.format(I18N.onuAutoUpg.tip.batchUnbandSuccess, sucList.join("、"));
		}
		proIdSelChange();
		window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.refreshThisWindow(msg);
	}
}
function unbandAPon(list, re){
	re.flag = true;
	var par = {entityId: entityId, profileId: list[0][0], slotNo: list[0][1], ponNo: list[0][2]};
	Ext.Ajax.request({
		url : '/onu/unbandOnuAutoUpgProfile.tv?r=' + Math.random(),
		success : function(response) {
			re[list[0][1] + "/" + list[0][2]] = response.responseText || 'success';
			if(!response.responseText){
				for(var a=0,al=proData.length; a<al; a++){
					if(proData[a][0] == list[0][0]){
						var d = proData[a][12];
						for(var b=0,bl=d.length; b<bl; b++){
							if(d[b].slotNo == list[0][1] && d[b].ponNo == list[0][2]){
								proData[a][12].splice(b, 1);
								break;
							}
						}
						break;
					}
				}
			}
			list.splice(0, 1);
			unbandMgmt(list, re);
		},
		failure : function(response) {
			re[list[0][1] + "/" + list[0][2]] = response.responseText || 'unknow error';
			list.splice(0, 1);
			unbandMgmt(list, re);
		},
		params: par
	});
}

function cancelClick(){
	window.parent.closeWindow('onuAutoUpgProfile');
}

function modifyBtClick(){
	if(bandData.length){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.modifyTip);
		return;
	}
	var profileId = $("#proIdSel").val();
	var proName = $("#proName").val();
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
	if(!proNewVersion || proNewVersion == ""){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.mustSelVersion);
		return;
	}
	if(!checkedAllInput()){
		return;
	}
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
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		var uploadFile = needToUpload([pers, webs, boot, app, other]);
		if(uploadFile.length > 0){
			submitFile();
			return;
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.modifying);
		var params = {entityId : entityId, profileId : profileId, profileName : proName, proNewVersion : proNewVersion, proMode : proMode,
			proHwVersion: onuHwVersion, proOnuType : onuType, proPers : pers, proWebs : webs, proOther : other, proBoot : boot, proApp : app,
			onuAutoUpgFlag : 1, proUpgTime : updateTime}
		Ext.Ajax.request({
			url : '/onu/modifyOnuAutoUpgProfile.tv?&r=' + Math.random(),
			success : function(response) {
				if(response.responseText){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyProFailed);
				}else if(window.parent.getWindow){
					if(window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.refreshThisWindow){
						window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow.refreshThisWindow();
					}
					//location.href = '/onu/showOnuAutoUpgMessage.tv?messageList=success&entityId=' + entityId + '&onuAutoUpgFlag=1';
					//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyProSuc);
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">@onuAutoUpg.mes.modifyProSuc@</b>'
		       	    });
					cancelClick();
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyProFailed);
				}
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.modifyProFailed);
			},
			params: params
		});
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
		+ "&onuAutoUpgFlag=1" + "&proUpgTime=" + updateTime; //1:modify, 0:add
	if(needToUploads.length > 0){
		var tmpSt = ['pers', 'webs', 'boot', 'app', 'other'];
		for(var i=0; i<tmpSt.length; i++){
			if(needToUploads.indexOf(tmpSt[i]) == -1){
				form.removeChild(document.getElementById("selectFile" + tmpSt[i]));
			}
		}
	}
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.modifying);
	form.submit();
}
function deleteBtClick(){
	if(bandData.length){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.tip.deleteTip);
		return;
	}
	var profileId = $("#proIdSel").val();
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.mes.delProConfirm, profileId), function(type) {
		if (type == 'no') {
			return;
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.onuAutoUpg.mes.deletingPro, profileId));
		var params = {entityId : entityId, profileId : profileId}
		Ext.Ajax.request({
			url : '/onu/delOnuAutoUpgProfile.tv?&r=' + Math.random(),
			success : function(response) {
				if(response.responseText){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.delProFailed);
				}else if(window.parent.getWindow){
					window.parent.getWindow("onuAutoUpg").body.dom.firstChild.contentWindow
						.refreshThisWindow(I18N.onuAutoUpg.mes.delProSuc);
					cancelClick();
				}
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.delProFailed);
			},
			params: params
		});
	});
}

Ext.onReady(function(){
	//升级时间
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
	    	}
	    }
	});
	//页面初始化处理
	initOnuProMode();
	initOnuTypeSel();
	initButton('all');
	loadBandGrid();
	$("#proName").focus();
	initFileList();
	initSelectFileBtLoc();
	$(".modifyClass").keydown(noBackspace);
});

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
function initProIdSel(){
	var p = $("#proIdSel");
	for(var i=0; i<proData.length; i++){
		p.append(String.format("<option value={0}>{0}</option>", proData[i][0]));
	}
	p.val(proId);
	proIdSelChange();
}
function proIdSelChange(){
	proId = $("#proIdSel").val();
	loadBandData();
	initValue();
}
function initValue(){
	for(var a=0; a<proData.length; a++){
		if(proData[a][0] == proId){
			var d = proData[a];
			for(var b=1; b<proDataHtmlId.length; b++){
				$("#" + proDataHtmlId[b]).val(d[b]);
				//升级时间特殊处理
				updateTime.setValue(d[13]);
			}
			break;
		}
	}
}
var selectedFileId = "";
function selectFile(id){
	selectedFileId = id;
	//$("#selectFile" + id).click();
}
function selectFileChange(){
	if(selectedFileId){
		var v = $("#selectFile" + selectedFileId).val().split("\\");
		$("#" + selectedFileId).val(v[v.length - 1]);
		$('#' + selectedFileId + 'FileName').val(v[v.length - 1]);
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
	if(bandData.length > 0){
		return false;
	}
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
						+ "onclick='fileSpanClick(this)' {1} onfocus='notToHide(1)'>{0}</span><br>", tmpSs,
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
			topH -= p.height();
		}else{
			topH += 19;
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
	if(bandData.length > 0){
		return false;
	}
	$("#" + selectedFileId).val($(el).text());
	$("#fileSel").hide();
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
	if(bandData.length > 0){
		return false;
	}
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
		p.css({left: pos[0] + 2, top: pos[1] + 19}).show();
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
	if(bandData.length > 0){
		return false;
	}
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
		$("#selectFile" + htmlId[a]).css({left: (tmpLoc[0] + 2), top: (tmpLoc[1] + 2)}).mouseover(function(){
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
	var color = "cccccc";
	if(bandData.length == 0){
		color = "8bb8f3";
	}
	for(var a=0; a<ids.length; a++){
		var p = $("#" + ids[a]);
		var le = p.val().length;
		p.css("border", "1px solid #" + color);
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
	var color = "cccccc";
	if(bandData.length == 0){
		color = "8bb8f3";
	}
	p.css("border", "1px solid #" + color);
	if(!p.val()){
		return true;
	}
	var reg = /^([a-z._|~`{}<>''""?:\/\\\(\)\[\]\-\d,;!#$%^&=+])+$/ig;
	if(!reg.exec(p.val())){
		p.css("border", "1px solid #ff0000");
		return false;
	}
	return true;
}

</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@onuAutoUpg.mes.modifyProfile@,@onuAutoUpg.he.addOnuTemDesc@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10 pB0">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr>
					<td colspan="2" style="padding-bottom:10px;">
						<span>@onuAutoUpg.he.proId@:</span>
						<select id=proIdSel class="normalSel w70" onchange='proIdSelChange()'></select>
						<b id=tipSpan class="orangeTxt pL20">@onuAutoUpg.tip.modifyDelTip@</b>
					</td>
				</tr>
				<tr>
					<td width=240 valign="top">
						<div id="bandGridDiv"></div>
					</td>
					<td>
						<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" width="100%" rules="none">
							<!-- name -->
							<tr class="">
								<td width="130" class="rightBlueTxt">@onuAutoUpg.he.profileName@:</td>
								<td>
									<input id="proName" class="modifyClass normalInput w200" maxlength="31"
									title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onkeyup='checkedChinese(this.id)' onblur='checkedChinese(this.id)' />
								</td>
								<td width="150" class="rightBlueTxt">@onuAutoUpg.he.startTime@:</td>
								<td id="updateTimeInput"></td>
							</tr>
							<!-- mode && newVersion -->
							<tr>
								<td class="rightBlueTxt" width="70">@COMMON.required@@onuAutoUpg.pro.newVersion@:</td>
								<td>
									<input id="proNewVersion" class="modifyClass normalInput w200" maxlength="16"
									title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max16@' onkeyup='checkedChinese(this.id)' onblur='checkedChinese(this.id)' />
								</td>
								<td class="rightBlueTxt">@onuAutoUpg.pro.upgMode@:</td>
								<td>
									<select id="proMode" class="modifyClass w150 normalSel" >
										<option value=4>CTC</option>
										<option value=16>baseline</option>
										<option value=32>extend</option>
										<option value=64>auto</option>
									</select>
								</td>
							</tr>
							<!-- onuType && hwVersion -->
							<tr>
								<td class="rightBlueTxt">@onuAutoUpg.pro.hwVersion@:</td>
								<td>
									<input id=onuHwVersion class="modifyClass normalInput w200" maxlength=8
									title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max8@'
									onfocus='hwFocus()' onblur='hwBlur();checkedChinese(this.id)'
									onkeyup='hwKeyup();checkedChinese(this.id)' />
								</td>
								<td class="rightBlueTxt">@COMMON.required@@onuAutoUpg.he.onuType@:</td>
								<td>
									<select id=onuType class="modifyClass w150 normalSel" ></select>
								</td>
							</tr>
							<!-- pers -->
							<tr>
								<td class="rightBlueTxt">Pers@onuAutoUpg.pro.file@:</td>
								<td>
									<input id="persFileName" type="text" class="normalInput w120 normalInputDisabled" disabled="disabled"/>
									<button id="persBt" class="BUTTON75" onclick='selectFile("pers")' > @onuAutoUpg.co.browse@</button>
								</td>
								<td class="rightBlueTxt">Pers@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
								<td>
								    <input id="pers" class="modifyClass normalInput w150" maxlength="31"
                                    title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                                    onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' /> 
								</td>
							</tr>
							<!-- boot -->
							<tr>
								<td class="rightBlueTxt">Boot@onuAutoUpg.pro.file@:</td>
								<td>
								    <input id="bootFileName" type="text" class="normalInput w120 normalInputDisabled" disabled="disabled"/>
								    <button id=bootBt class=BUTTON75 onclick='selectFile("boot")'>@onuAutoUpg.co.browse@</button>
								</td>
								<td class="rightBlueTxt">Boot@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                                <td>
                                    <input id="boot" class="modifyClass normalInput w150" maxlength="31"
                                       title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                                       onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                                </td>
							</tr>
							<!-- app -->
							<tr>
								<td class="rightBlueTxt">App@onuAutoUpg.pro.file@:</td>
								<td>
								    <input id="appFileName" type="text" class="normalInput w120 normalInputDisabled" disabled="disabled"/>
								    <button id=appBt class=BUTTON75 onclick='selectFile("app")'>@onuAutoUpg.co.browse@</button>
								</td>
								<td class="rightBlueTxt">App@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                                <td>
                                    <input id="app" class="modifyClass normalInput w150" maxlength="31"
                                       title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                                       onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                                </td>
							</tr>
							<!-- webs -->
							<tr>
								<td class="rightBlueTxt">Webs@onuAutoUpg.pro.file@:</td>
								<td>
								    <input id="websFileName" type="text" class="normalInput w120 normalInputDisabled" disabled="disabled"/>
								    <button id=websBt class=BUTTON75 onclick='selectFile("webs")'>@onuAutoUpg.co.browse@</button>
								</td>
								<td class="rightBlueTxt">Webs@onuAutoUpg.pro.file@@onuAutoUpg.name@:</td>
                                <td>
                                    <input id="webs" class="modifyClass normalInput w150" maxlength=31
                                       title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                                       onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                                </td>
							</tr>
							<!-- other -->
							<tr class="withoutBorderBottom">
								<td class="rightBlueTxt">@onuAutoUpg.pro.otherFile@:</td>
								<td>
								    <input id="otherFileName" type="text" class="normalInput w120 normalInputDisabled" disabled="disabled"/>
									<button id=otherBt class=BUTTON75 onclick='selectFile("other")'>@onuAutoUpg.co.browse@</button>
								</td>
								<td class="rightBlueTxt">@onuAutoUpg.pro.otherFile@@onuAutoUpg.name@:</td>
                                <td>
                                    <input id="other" class="modifyClass normalInput w150" maxlength="31"
                                       title='@onuAutoUpg.tit.noChinese@@onuAutoUpg.tit.max31@' onfocus='fileFocus(this.id)'
                                       onblur='fileBlur();checkedChinese(this.id)' onkeyup='fileKeyup(this.id);checkedChinese(this.id)' />
                                </td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id=hwSel onfocus='notToHide(2)' style="width:120px;"></div>
		<div id=fileSel onfocus='notToHide(1)' ></div>
		<!-- 文件上传 -->
		<form id="form1" name="form1" encType="multipart/form-data" method="post">
			<input id=selectFilepers class="fileElement" name=filePers type="file"
				onchange='selectFileChange()' onclick='selectFile("pers")'
				style='top: 166px;' />
			<input id=selectFileboot class="fileElement" name=fileBoot type="file"
				onchange='selectFileChange()' onclick='selectFile("boot")'
				style='top: 198px;' />
			<input id=selectFileapp class="fileElement"  name=fileApp type="file"
				onchange='selectFileChange()' onclick='selectFile("app")'
				style='top: 230px;' />
			<input id=selectFilewebs class="fileElement"  name=fileWebs type="file"
				onchange='selectFileChange()' onclick='selectFile("webs")'
				style='top: 262px;' />
			<input id=selectFileother class="fileElement"  name=fileOther type="file"
				onchange='selectFileChange()' onclick='selectFile("other")'
				style='top: 294px;' />
		</form>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
		         <li><a onClick="modifyBtClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@onuAutoUpg.ac.modifyPro@</span></a></li>
		         <li><a onClick="deleteBtClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i>@onuAutoUpg.ac.deletePro@</span></a></li>
		         <li><a onClick="cancelClick()" href="javascript:;" class="normalBtnBig"><i class="miniIcoForbid"></i><span>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</body>
</Zeta:HTML>