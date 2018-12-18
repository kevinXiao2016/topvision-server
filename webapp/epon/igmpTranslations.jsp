<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var entityId = <s:property value="entityId"/>;
var transGrid;
var transStore;
var transData = new Array();
var transRule = ${transList} ? ${transList} : new Array();
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var modifyFlag;

function refreshClick(){
	var params = {
		entityId : entityId
	};
	var url = '/epon/igmp/refreshIgmpTranslation.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.fetchIgmpTranslate, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpTranslateError)
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpTranslateOk)
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchIgmpTranslateError)
		},
		params : params
	});
}
function cancelClick(){
	window.parent.closeWindow('igmpTranslation');
}
function okClick(){
	modifyFlag = true;
	var flagNum = -1;
	var oldFlagNum = -1;
	var transId = parseInt($("#transRuleId").val());
	var oldId = parseInt($("#transOldId").val());
	var newId = parseInt($("#transNewId").val());
	if(!checkedRuleId() || !checkedOldId() || !checkedNewId()){
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
		return;
	}
	var tempTransRule = transRule.slice();
	for(var k=0; k<transRule.length; k++){
		if(transRule[k][0] == transId){
			for(var u=0; 2*u<transRule[k].length-1; u++){
				if(transRule[k][2*u+1] == oldId){
					oldFlagNum = u;
				}
			}
			flagNum = k;
		}
	}
	if(flagNum == -1){//新增一个rule列表
		modifyFlag = false;
		flagNum = transRule.length;
		transRule[flagNum] = new Array();
		transRule[flagNum] = [transId,oldId,newId];
	}else if(oldFlagNum == -1){//新增一条rule
		transRule[flagNum].push(oldId);
		transRule[flagNum].push(newId);
	}else{//修改一条rule
		transRule[flagNum][2*oldFlagNum+1] = oldId;
		transRule[flagNum][2*oldFlagNum+2] = newId;
	}
	//剩余的转换前、后ID赋值为0
	var oldIdList = [0,0,0,0,0,0,0,0];
	var newIdList = [0,0,0,0,0,0,0,0];
	for(var j=0; 2*j<transRule[flagNum].length-1; j++){
		oldIdList[j] = transRule[flagNum][2*j+1];
		newIdList[j] = transRule[flagNum][2*j+2];
	}
	var params = {
		entityId : entityId,
		transId : transId,
		transOldIdList : oldIdList,
		transNewIdList : newIdList
	};
	if(modifyFlag){//modify
		var url = '/epon/igmp/modifyIgmpVlanTrans.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.mdfingTranslate+ transId +':'+ oldId +'->'+ newId + '...', 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!');
					transRule = tempTransRule;
					ruleToData();
					transStore.loadData(transData);
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfTranslate + transId +':'+ oldId +'->'+ newId +  I18N.COMMON.ok + '!');
				$("#transRuleId").val("");
				$("#transOldId").val("");
				$("#transNewId").val("");
				// 添加成功时，修改页面数据。
				ruleToData();
				transStore.loadData(transData);
				$("#transListText").html(I18N.IGMP.mdfTranslateTip)
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfTranslate + transId +':'+ oldId +'->'+ newId +  I18N.COMMON.error + '!');
				transRule = tempTransRule;
				ruleToData();
				transStore.loadData(transData);
			},
			params : params
		});
	}else{//add
		var url = '/epon/igmp/addIgmpVlanTrans.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.IGMP.addingTranslate + transId +':'+ oldId +'->'+ newId + '...', 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!')
					transRule = tempTransRule;
					ruleToData();
					transStore.loadData(transData);
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addTranslate  + transId +':'+ oldId +'->'+ newId + I18N.COMMON.ok + '!')
				$("#transRuleId").val("")
				$("#transOldId").val("")
				$("#transNewId").val("")
				// 添加成功时，修改页面数据。
				ruleToData()
				transStore.loadData(transData)
				$("#transListText").html(I18N.IGMP.mdfTranslateTip)
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addTranslate  + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!')
				transRule = tempTransRule;
				ruleToData();
				transStore.loadData(transData);
			},
			params : params
		});
	}
}
function deleteTransRule(transId,oldId,newId){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.IGMP.cfmDelTranslate , function(type) {
		if (type == 'no'){return;}
		modifyFlag = true;
		var flagNum = -1;
		var oldIdList = [0,0,0,0,0,0,0,0];
		var newIdList = [0,0,0,0,0,0,0,0];
		var tempTransRule = transRule.slice();
		for(var k=0; k<transRule.length; k++){
			if(transRule[k][0] == transId){
				flagNum = k;
			}
		}
		if(flagNum == -1){//避免空指针
			if(transGrid.getSelectionModel().getSelected()!=null && transGrid.getSelectionModel().getSelected()!=undefined){
				var aa = transGrid.getSelectionModel().getSelected();
				transId = aa.get('transId');
				oldId = aa.get('transOldId');
				for(var k=0; k<transRule.length; k++){
					if(transRule[k][0] == transId){
						flagNum = k;
					}
				}
			}else{
				return;
			}
		}
		if(transRule[flagNum].length == 3){
			modifyFlag = false;
			transRule[flagNum] = new Array();
		}else{
			var tempRuleFlagNum = -1;
			for(var j=0; 2*j<transRule[flagNum].length-1; j++){
				if(parseInt(oldId) == parseInt(transRule[flagNum][2*j+1])){
					oldIdList[j] = 0;
					newIdList[j] = 0;
					tempRuleFlagNum = j;
				}else{
					oldIdList[j] = transRule[flagNum][2*j+1];
					newIdList[j] = transRule[flagNum][2*j+2];
				}
			}
			if(tempRuleFlagNum != -1){
				transRule[flagNum].splice(2*tempRuleFlagNum+1, 2);
			}
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.delingTranslate + transId +':'+ oldId +'->'+ newId + '...', 'ext-mb-waiting');
		if(modifyFlag){//modify
			var url = '/epon/igmp/modifyIgmpVlanTrans.tv?r=' + Math.random();
			var params = {
					entityId : entityId,
					transId : transId,
					transOldIdList : oldIdList,
					transNewIdList : newIdList
			};
			Ext.Ajax.request({
				url : url,
				success : function(response) {
					if(response.responseText != 'success'){
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!');
						transRule = tempTransRule;
						ruleToData();
						transStore.loadData(transData);
						return;
					}
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!');
					// 删除成功时，修改页面数据。
					ruleToData();
					transStore.loadData(transData);
				},
				failure : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!');
					transRule = tempTransRule;
					ruleToData();
					transStore.loadData(transData);
				},
				params : params
			});
		}else{//delete
			var url = '/epon/igmp/deleteIgmpVlanTrans.tv?r=' + Math.random();
			var params = {
					entityId : entityId,
					transId : transId
			};
			Ext.Ajax.request({
				url : url,
				success : function(response) {
					if(response.responseText != 'success'){
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!')
						transRule = tempTransRule;
						ruleToData();
						transStore.loadData(transData);
						return;
					}
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.ok + '!')
					// 添加成功时，修改页面数据。
					ruleToData();
					transStore.loadData(transData);
				},
				failure : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTranslate + transId +':'+ oldId +'->'+ newId + I18N.COMMON.error + '!')
					transRule = tempTransRule;
					ruleToData();
					transStore.loadData(transData);
				},
				params : params
			});//end request
		}//end else
	});//end confirm
}
function ruleToData(){
	var j = 0;
	transData = new Array();
	for(var k=0; k<transRule.length; k++){
		for(var t=0; 2*t+1<transRule[k].length; t++){
			transData[j] = new Array();
			transData[j][0] = transRule[k][0];
			transData[j][1] = transRule[k][2*t+1];
			transData[j][2] = transRule[k][2*t+2];
			transData[j][3] = 8 - (transRule[k].length - 1)/2;
			j++;
		}
	}
	transData.sort(function(a, b){
		if(parseInt(a[0]) == parseInt(b[0])){
			return parseInt(a[1]) - parseInt(b[1]);
		}else{
			return parseInt(a[0]) - parseInt(b[0]);
		}
	});
}
function doubleClick(){
	var selectedRow = transGrid.getSelectionModel().getSelected();
	var transIdTemp = selectedRow.get('transId');
	var oldIdTemp = selectedRow.get('transOldId');
	var newIdTemp = selectedRow.get('transNewId');
	$("#transRuleId").val(transIdTemp);
	$("#transOldId").val(oldIdTemp);
	$("#transNewId").val(newIdTemp);
	shaixuan();
	changeBt();
	transRuleIdChange();
}
function changeBt(){
 	var ruleId = $("#transRuleId").val();
	var oldId = $("#transOldId").val();
	var newId = $("#transNewId").val();
	if(ruleId==null || ruleId=="" || oldId==null || oldId=="" || newId==null || newId==""){
		$("#saveBt").attr("disabled",true);
		return;
	}
	var nowLength = 0;
	var leaveNum = 8;
	if(transStore.data.items[0]!=null && transStore.data.items[0]!=undefined){
		nowLength = transStore.data.items.length;
	}
	var tempLength = transRule.length;
	for(var x=0; x<tempLength; x++){
		if(transRule[x][0] == parseInt(ruleId)){
			leaveNum = 8 - ((transRule[x].length - 1)/2);
		}
	}
	//各种情况的按钮灰亮及变化
	if(!checkedRuleId() || !checkedOldId() || !checkedNewId() || newId==oldId){
		$("#saveBt").attr("disabled",true);
		return;
	}
	if(leaveNum == 0){
		if(nowLength == 1 && transStore.data.items[0].json[0]==ruleId && transStore.data.items[0].json[1]==oldId
				&& transStore.data.items[0].json[2]!=newId){
			$("#saveBt").text(I18N.IGMP.mdRules)
			$("#saveBt").attr("disabled",false)
			return;
		}
		$("#saveBt").attr("disabled",true)
		return;
	}
	if(nowLength == 0){
		$("#saveBt").text(I18N.IGMP.addRule)
		$("#saveBt").attr("disabled",false)
		return;
	}
	if(transStore.data.items[0].json[0]==ruleId && transStore.data.items[0].json[1]==oldId){
		if(transStore.data.items[0].json[2]==newId){
			$("#saveBt").attr("disabled",true)
			return
		}else{
			$("#saveBt").text(I18N.IGMP.mdRules)
			$("#saveBt").attr("disabled",false)
			return
		}
	}
	$("#saveBt").text(I18N.IGMP.addRule)
	$("#saveBt").attr("disabled",false);
}

/**
 * 筛选规则
 */
function shaixuan(){
	var ruleId =  $("#transRuleId").val();
	var oldId =  $("#transOldId").val();
	var newId =  $("#transNewId").val();
	if(ruleId!="" && ruleId!=null){
		if(oldId!="" && oldId!=null){
			transStore.filterBy(function(record){
				return record.get('transId')==ruleId && record.get('transOldId')==oldId;
			});
			return;
		}
		if(newId!="" && newId!=null){
			transStore.filterBy(function(record){
				return record.get('transId')==ruleId && record.get('transNewId')==newId;
			});
			return;
		}
		transStore.filterBy(function(record){
			return record.get('transId')==ruleId;
		});
	}else{
		if(oldId!="" && oldId!=null && newId!="" && newId!=null){
			transStore.filterBy(function(record){
				return record.get('transOldId')==oldId && record.get('transNewId')==newId;
			});
			return;
		}
		if(oldId!="" && oldId!=null){
			transStore.filterBy(function(record){
				return record.get('transOldId')==oldId;
			});
			return;
		}
		if(newId!="" && newId!=null){
			transStore.filterBy(function(record){
				return record.get('transNewId')==newId;
			});
			return;
		}
		transStore.filterBy(function(record){
			return true;
		});
	}
}
function transRuleIdChange(){
	var tempRuleId = $("#transRuleId").val();
	//执行筛选
	shaixuan();
	if(tempRuleId == ""){
		$("#transListText").html(I18N.IGMP.mdfTranslateTip)
		transStore.loadData(transData)
	}else{
		if(!checkedRuleId()){
			$("#transListText").html(I18N.IGMP.ruleIdTip)
			changeBt();
			return;
		}
		if(transStore.data.items[0]==null || transStore.data.items[0]==undefined){
			var tempA = -1;
			for(var t=0; t<transRule.length; t++){
				if(transRule[t][0] == tempRuleId){
					tempA = t;
				}
			}
			if(tempA == -1){
				$("#transListText").html(I18N.IGMP.ruleSeg1 + "8" + I18N.IGMP.ruleSeg2 )
			}else{
				var a = 8 - (transRule[tempA].length - 1)/2
				$("#transListText").html(I18N.IGMP.ruleSeg1 + a +I18N.IGMP.ruleSeg2)
			}
		}else{
			var a = transStore.data.items[0].json[3]
			$("#transListText").html(I18N.IGMP.ruleSeg1 + a +I18N.IGMP.ruleSeg2)
		}
	}
	changeBt();
}
function checkedRuleId(){
	$("#transRuleId").css("border","1px solid #8bb8f3");
	var ruleId = $("#transRuleId").val();
	var reg = /^([1-9][0-9]{0,1})+$/;
	if(!reg.exec(ruleId) || ruleId=="" || parseInt(ruleId)>64 || ruleId==null || ruleId==undefined){
		$("#transRuleId").css("border","1px solid #FF0000");
		return false;
	}
	return true;
}
function transOldIdChange(){
	shaixuan();
	if($("#transOldId").val()=="" || $("#transOldId").val()==null || $("#transOldId").val()==undefined){
		$("#saveBt").attr("disabled",true);
		return;
	}
	if(!checkedOldId()){
		$("#saveBt").attr("disabled",true);
		return;
	}
	changeBt();
}
function checkedOldId(){
	$("#transOldId").css("border","1px solid #8bb8f3");
	var oldId = $("#transOldId").val();
	var reg = /^([1-9][0-9]{0,3})+$/;
	if(!reg.exec(oldId) || oldId=="" || parseInt(oldId)>4094 || oldId==null || oldId==undefined){
		$("#transOldId").css("border","1px solid #FF0000");
		return false;
	}
	return true;
}
function transNewIdChange(){
	shaixuan();
	if($("#transNewId").val()=="" || $("#transNewId").val()==null || $("#transNewId").val()==undefined){
		$("#saveBt").attr("disabled",true);
		return;
	}
	if(!checkedNewId()){
		$("#saveBt").attr("disabled",true);
		return;
	}
	changeBt();
}
function checkedNewId(){
	$("#transNewId").css("border","1px solid #8bb8f3");
	var newId = $("#transNewId").val();
	var reg = /^([1-9][0-9]{0,3})+$/;
	if(!reg.exec(newId) || newId=="" || parseInt(newId)>4094 || newId==null || newId==undefined){
		$("#transNewId").css("border","1px solid #FF0000");
		return false;
	}
	return true;
}
var transMenu = new Ext.menu.Menu({
    id:'transMenu',
    enableScrolling: false,
    items:[{
        id: I18N.IGMP.mdfTranslate,
        text:I18N.IGMP.mdfTranslate,
        handler : doubleClick
    },{
        id: I18N.IGMP.delTranslate,
        text: I18N.IGMP.delTranslate,
        handler : function(){
        	var transIdTemp = transGrid.getSelectionModel().getSelected().get('transId');
        	var oldIdTemp = transGrid.getSelectionModel().getSelected().get('transOldId');
        	var newIdTemp = transGrid.getSelectionModel().getSelected().get('transNewId');
			deleteTransRule(transIdTemp,oldIdTemp,newIdTemp);
	    }
    }]
});
function loadTransGrid(){
	transStore = new Ext.data.SimpleStore({
		data : transData, idProperty: "transId",
		fields : ['transId','transOldId','transNewId','leaveNum']
	});
	transGrid = new Ext.grid.GridPanel({
	    stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		id : 'transGrid',
		renderTo : 'transGridPanel',
		store : transStore,
		height : 190,
		frame : false,
		autoScroll : true,
		border : true,
		selModel : new Ext.grid.RowSelectionModel({singleSelect : true}),
		viewConfig:{forceFit: true},
		columns: [{
				header: I18N.IGMP.translateId,dataIndex: 'transId'
			},{
				header: I18N.IGMP.originId,dataIndex: 'transOldId'
			},{
				header: I18N.IGMP.newMvlanId,dataIndex: 'transNewId'
			},{
				header: I18N.IGMP.restRules,dataIndex: 'leaveNum',hidden: true
			},{
				header: I18N.COMMON.manu, dataIndex: 'id',
				renderer : function(value, cellmeta, record) {
					if(operationDevicePower){
						return String.format("<a href='javascript:;' onclick='deleteTransRule(\"{0}\",\"{1}\",\"{2}\")'>@COMMON.delete@</a>"+
						        " / <a href='javascript:;' onclick='onModifyClick(\"{3}\")'>@COMMON.modify@</a>"+
						        " / <a href='javascript:;' onclick='onOtherClick(\"{0}\",event)'>@COMMON.other@</a>"
								,record.data.transId,record.data.transOldId,record.data.transNewId, record.id);
					}else{
						return String.format( "<a href='javascript:;'>@COMMON.delete@</a>"+
				        	" / <a href='javascript:;' onclick='onModifyClick(\"{3}\")'>@COMMON.modify@</a>"+
				        	" / <a href='javascript:;' onclick='onOtherClick(\"{0}\",event)'>@COMMON.other@</a>"
							,record.data.transId,record.data.transOldId,record.data.transNewId, record.id );
					}
				}
			}]
	});
}
function onOtherClick(rid,event){
    var record = transGrid.getStore().getById(rid);  // Get the Record
    transGrid.getSelectionModel().selectRecords([record]);
	transMenu.showAt([event.clientX,event.clientY]);
}
function onModifyClick(rid){
    var record = transGrid.getStore().getById(rid);  // Get the Record
    transGrid.getSelectionModel().selectRecords([record]);
	doubleClick();
}
Ext.onReady(function(){
	ruleToData();
	loadTransGrid();
});

function authLoad(){
	var ids = new Array();
	ids.add("transOldId");
	ids.add("transNewId");
	ids.add("transRuleId");
	ids.add("saveBt");
	//ids.add("refreshBt");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
<body class="openWinBody" onkeydown="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.mvlanTranslateRules@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="w100 rightBlueTxt">@IGMP.origin@ MVID&nbsp;&nbsp;</td>
					<td><input id="transOldId" class="normalInput" maxlength=4 onkeyup=transOldIdChange() tooltip='@IGMP.transIdTip@' /></td>
					<td class="rightBlueTxt w150">@IGMP.newMvlanId@ &nbsp;&nbsp;</td>
					<td><input id="transNewId" class="normalInput" maxlength=4 onkeyup=transNewIdChange() tooltip='@IGMP.transIdTip@' /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@IGMP.translateList@ ID&nbsp;&nbsp;</td>
					<td><input id="transRuleId" class="normalInput" maxlength=2 onkeyup="transRuleIdChange()" tooltip='@IGMP.ruleIdTitle@' /></td>
					<td id="transListText" class="rightBlueTxt">@IGMP.mdfTranslateTip@</td>
					<td><button id=saveBt class="normalBtn" disabled onclick="okClick()">@IGMP.addRule@</button></td>
				</tr>
				<tr>
					<td colspan=4>
						<div id="transGridPanel"></div>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>