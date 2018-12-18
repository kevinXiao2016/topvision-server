<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript">
var vlanConfigType = 'pon';
var heigh = 0;
var wid = 0;
</script>
<Zeta:Loader>
	LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE epon
    IMPORT epon.ponVlanCommon
    IMPORT js.ext.ux.PagingMemoryProxy
</Zeta:Loader>
<style>
select[disabled]{ background:url("css/white/normalInputDisabled.png") repeat; height:22px; border:1px solid #B7B8CA;}
</style>
<link rel="stylesheet" type="text/css" href="../css/VerticalTabPanel.css" />
<script type="text/javascript" src="/js/ext/ux/VerticalTabPanel.js"></script>
<script type="text/javascript" src="/epon/ponVlanTransMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanAggrMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanTrunkMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanQinqMode.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = '${entity.entityId}';
var portType = '${ponPortType}';
var ponId = '${ponId}';
var tabNum = ${tabNum};
var tipNum = '${tipNum}' || 0;
tabNum = tabNum < 4 ? tabNum : 0;
var currentId = '${currentId}';
var pageSize = <%= uc.getPageSize() %>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var is8602G = '${is8602G}';

function buildPagingToolBar( $store ) {
	pagingToolbar = new Ext.PagingToolbar({pageSize: pageSize , store: $store , displayInfo:false, cls:"extPagingBar"});
	return pagingToolbar;
}
function llidModeChange() {
    window.top.createDialog('ponVlanLlidConfig', I18N.VLAN.cfgMgmtLLID , 1010, 550, 
    	    '/epon/vlan/showPonVlanLlidConfig.tv?tabNum=' + tabNum+'&tipNum='+tipNum + '&entityId=' + entityId + '&ponId=' + ponId + '&currentId=' + currentId, 
    	    null, true, true);
    cancelClick();
}
function cancelClick() {
    window.parent.closeWindow('ponVlanConfig');
}
Ext.onReady(function() {
    var w = document.body.clientWidth - 250;
    var h = document.body.clientHeight - 50;
    h = 430 
    var items2;
    if(is8602G=='true'){
    	items2=[
                {itemId:'x1',contentEl: 'ponVlanTransTab', title: I18N.VLAN.translate},
                {itemId:'x3',contentEl: 'ponVlanTransparentTab', title: I18N.VLAN.vlanTansparent},
                {itemId:'x4',contentEl: 'ponVlanQinqTab', title: 'QinQ'}
            ]
    }else{
    	items2=[
                {itemId:'x1',contentEl: 'ponVlanTransTab', title: I18N.VLAN.translate},
                {itemId:'x2',contentEl: 'ponVlanNto1Tab', title:  I18N.VLAN.aggregate},
                {itemId:'x3',contentEl: 'ponVlanTransparentTab', title: I18N.VLAN.vlanTansparent},
                {itemId:'x4',contentEl: 'ponVlanQinqTab', title: 'QinQ'}
            ]
    };
    tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: h,
        activeTab:tabNum,
        frame:true,
        defaults:{autoHeight: true},
        items:items2,
        listeners:{
        	tabchange: function(){
				if(transGrid && aggrSvlanGrid && aggrCvlanGrid  && qinqSvlanGrid && qinqCvlanGrid ){
					var a = tabs.getActiveTab().itemId;
					if(a == 'x1'){
						transStore.loadData(transData);
						transGrid.getSelectionModel().selectFirstRow();
					}else if(a == 'x2'){
						aggrSvlanStore.loadData(aggrSvlanData);
						aggrSelectFirstRow();
						aggrCvlanReload();
					}else if(a == 'x3'){
						loadTransparentData();
						transparentGrid.getSelectionModel().selectFirstRow();
					}else if(a == 'x4'){
						qinqSvlanStore.loadData(qinqSvlanData);
						qinqSelectFirstRow();
						qinqCvlanReload();
					}
					tabNumChange(a);
				}
			}
   	   }
    });
    tips = new Ext.TabPanel({
    	renderTo: 'tip',
    	width:220,
    	height:h,
    	activeTab: parseInt(tipNum),
    	frame:true,
    	defaults:{autoHeight: true},
    	items:[
			{itemId:'t1',contentEl: 'tip1Tab', title: "@COMMON.note@"},
			{itemId:'t2',contentEl: 'tip2Tab', title: 'CVID'},
			{itemId:'t3',contentEl: 'tip3Tab', title: 'SVID'}
   	    ],
   	 	listeners:{
   	 		tabchange: function(){
					if(cvidModeGrid!=null && cvidModeGrid!=undefined && svidModeGrid!=null && svidModeGrid!=undefined){
						var a = tips.getActiveTab().itemId;
						if(a == 't2' && !cvidLoadFlag){
							var tmpF = true;
							setTimeout(function(){
								if(tmpF){
									$("#loadingDiv").show();
								}
							}, 500);
							loadCvidStore();
							tmpF = false;
							$("#loadingDiv").hide();
						}else if(a == 't3' && !svidLoadFlag){
							var tmpF = true;
							setTimeout(function(){
								if(tmpF){
									$("#loadingDiv").show();
								}
							}, 500);
							loadSvidStore();
							tmpF = false;
							$("#loadingDiv").hide();
						}
					}
			}
	   	}
    });
	// CVID和SVID列表
	loadCsvidMode();

	// VLAN转换
	loadTransMode();
    
    // VLAN聚合
    loadAggrMode();
    
    // VLAN Trunk
   // loadTrunkMode();
    
    // QinQ
    loadQinqMode();

    //transparent
    loadTransparentGrid();
    var $itemId = tabs.get( tabNum ).itemId;
    tabNumChange( $itemId );
});

/**
 * transparent
 */
var transparentData = new Array();
var transparentStore;
var transparentGrid;
function loadTransparentData(){
	$.ajax({
		url: '/epon/vlan/loadTransparentData.tv',cache:false, dataType:'json',async:false,
	    success: function(json) {
	    	transparentData = json;
	    	if(transparentData.join("") == "false"){
	    		transparentData = new Array();
			}
	    	transparentStore.loadData( transparentData );
	    },
	    error: function(){
	   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.contentError);
	    },
	   	data: {entityId : entityId, ponId: ponId}
	});
}
function loadTransparentGrid(){
	transparentStore = new Ext.data.ArrayStore({
		proxy : new Ext.ux.data.PagingMemoryProxy( transparentData ),
		baseParams: { start : 0,limit : pageSize },
		fields : [ 'transparentId', 'transparentMode']
	});
	transparentGrid = new Ext.grid.GridPanel({
		//stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		//viewConfig:{forceFit: true},
		renderTo : "transparentList",
		bbar: buildPagingToolBar( transparentStore ),
		height : 235,
		width : 515,
		frame : false,
		border : false,
		autoScroll : true,
		columns : [ {
			header : "@VLAN.transparent@ VLAN ID",
			dataIndex : "transparentId",
			width : 180,
			align : "center"
		},{
			header : "@VLAN.transparentMode@",
			dataIndex : "transparentMode",
			width : 160,
			align : "center",
			renderer : function(value, cellmeta, record){
				var color = value ? 'red' : 'green';
				var str = value ? 'untag' : 'tag';
				return String.format("<font style='color:{0}'>{1}</font>", color, str);
			}
		}, {
			header : "@COMMON.manu@",
			dataIndex : "id",
			width : 130,
			align : "center",
			renderer : function(value, cellmeta, record) {
				return String.format("<img src='/images/delete.gif' onclick='deleteTransparent({0})'/>", record.data.transparentId);
			}
		} ],
		store : transparentStore,
		listeners : {
			viewready :function() {
				loadTransparentData();
			}
		}
	});
}
function deleteTransparent(id){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.transparentDeleting, 'ext-mb-waiting');
		if(transparentData.length == 1){//del
			$.ajax({
				url: '/epon/vlan/delTransparentRule.tv?r=' + Math.random(),
			    success: function() {
			    	transparentData = new Array();
			    	transparentStore.loadData(transparentData);
			    	$("#transparentId").val("");
			    	loadScvidData();
			    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelSuc);
			    },
			    error: function(){
			   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelFailed);
			    },
			   	data: {entityId : entityId, ponId: ponId}
			});
		}else{//modify
			var sel = transparentGrid.getSelectionModel().getSelected();
			if(sel){
				var tmpD = sel.data.transparentId;
				var idList = new Array();
				var modeList = new Array();
				var tmpIndex = -1;
				for(var a=0,n=transparentData.length; a<n; a++){
					var tmpA = transparentData[a][0];
					if(tmpA == tmpD){
						tmpIndex = a;
						continue;
					}
					idList.push(tmpA);
					if(transparentData[a][1] == 1){
						modeList.push(tmpA);
					}
				}
				if(tmpIndex > -1){
					$.ajax({
						url: '/epon/vlan/modifyTransparentRule.tv?r=' + Math.random(),
					    success: function() {
					    	transparentData.splice(tmpIndex, 1);
					    	transparentStore.remove(sel);
					    	if(!transparentStore.getAt(0)){
						    	$("#transparentId").val("");
					    		transparentStore.loadData(transparentData);
						    }
					    	loadScvidData();
					    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelSuc);
					    },
					    error: function(){
					   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelFailed);
					    },
					   	data: {
						   	entityId : entityId, 
						   	ponId: ponId, 
						   	transparentIdStr: idList.join(","), 
						   	transparentModeStr: modeList.join(",")
						}
					});
				}
			}
		}
	});
}

//批量删除透传Vlan
function batchDeleteTransClick(){
	text = $("#transparentId").val();
	var deleteList = changeToArray();
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		
		if(deleteList){
			var idList = new Array();
			var modeList = new Array();
			var tmpIndex = -1;

			for(var a=0,n=transparentData.length; a<n; a++){
				var tmpA = transparentData[a][0];
				if(deleteList.contains(tmpA)){
					tmpIndex = a;
					continue;
				}
				idList.push(tmpA);
				if(transparentData[a][1] == 1){
					modeList.push(tmpA);
				}
			}
			if(tmpIndex > -1){
				window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.transparentDeleting, 'ext-mb-waiting');
				$.ajax({
					url: '/epon/vlan/modifyTransparentRule.tv?r=' + Math.random(),
				    success: function() {
				    	loadTransparentData();
				    	loadScvidData();
				    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelSuc);
				    	$("#transparentId").val("");
				    	$("#deleteTrans").attr("disabled", true).mouseout();
				    },
				    error: function(){
				   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentDelFailed);
				    },
				   	data: {
					   	entityId : entityId, 
					   	ponId: ponId, 
					   	transparentIdStr: idList.join(","), 
					   	transparentModeStr: modeList.join(",")
					}
				});
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.noSelTransparaentVlan);
				return;
			}
		}
	}); 
}

function transparentKeyup(){
	text = $("#transparentId").val();
	var bt = $("#transparentBt");
	var deleteBut = $("#deleteTrans");
	bt.attr("disabled", true);
	deleteBut.attr("disabled", true);
	if(text){
		if(checkedInputList("transparentId")){
			var list = changeToArray();
			transparentStore.filterBy(function(record){
				var $transparentId = new String(record.data.transparentId);
				if (exactFlag) {
					return $transparentId.exactMatchWith(list);//(record.data.transparentId) > -1;
				}else{
					return $transparentId.matchWith(list);//(record.data.transparentId) > -1;
				}
			});
			if(transparentStore.getCount() == 0){
				bt.attr("disabled", false);
			}else{
				deleteBut.attr("disabled", false);
			}
		}
	}else{
		transparentStore.loadData(transparentData);
	}
}
function transparentBtClick(){
	text = $("#transparentId").val();
	var idList = changeToArray();
	var modeList = new Array();
	if($("#transparentModeSel").val() == 1){
		modeList = changeToArray();
	}
	var conflictMsg = checkedTransparentConflict(idList);
	if(conflictMsg.isConflict){
		return window.parent.showMessageDlg(I18N.COMMON.tip, conflictMsg.msg);
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.transparentAdding, 'ext-mb-waiting');
	if(transparentData.length == 0){//add
		$.ajax({
			url: '/epon/vlan/addTransparentRule.tv?r=' + Math.random(),
		    success: function() {
		    	loadTransparentData();
		    	loadScvidData();
		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentAddSuc);
		    	$("#transparentId").val("");
		    	$("#transparentBt").attr("disabled", true).mouseout();
		    },
		    error: function(e){
		    	window.parent.showMessageDlg(I18N.COMMON.tip, "@VLAN.transparentAddFailed@");
		    },
		   	data: {entityId : entityId, ponId: ponId, transparentIdStr: idList.join(","), transparentModeStr: modeList.join(",")}
		});
	}else{//modify
		for(var a=0; a<transparentData.length; a++){
			if(idList.indexOf(transparentData[a][0]) == -1){
				idList.push(transparentData[a][0]);
				if(transparentData[a][1] == 1){
					modeList.push(transparentData[a][0]);
				}
			}
		}
		$.ajax({
			url: '/epon/vlan/modifyTransparentRule.tv?r=' + Math.random(),
		    success: function() {
		    	loadTransparentData();
		    	loadScvidData();
		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentAddSuc);
		    	$("#transparentId").val("");
		    	$("#transparentBt").attr("disabled", true).mouseout();
		    },
		    error: function(){
		   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.transparentAddFailed);
		    },
		   	data: {entityId : entityId, ponId: ponId, transparentIdStr: idList.join(","), transparentModeStr: modeList.join(",")}
		});
	}
}

function checkedTransparentConflict(idList){
	var msg = [];
	var isConflict = false;
	//transparent vlan id 既是SVID又是CVID
	//检测SVID的存在性和合法性，检测CVID的唯一性
	var sl = svids.length;
	var cl = cvids.length;
	var count = 0;
	for(var a=0,an=idList.length; a<an; a++){
		//SVID
		for(var b=0; b<sl; b++){
			if(svids[b][0] == idList[a]){
				count++;
				if(svids[b][1] && svids[b][1] != 3){
					//I18N.VLAN.conflictTip : VLAN {0} 在{1}上配置了{2}规则,不能重复配置
					msg.push(String.format(I18N.VLAN.conflictTip, idList[a],
							svids[b][2] ? "ONU:" + svids[b][2] : I18N.VLAN.ponPort, vlanModeString[svids[b][1]]));
					isConflict = true;
					break;
				}
			}
		}
		if(isConflict){
			continue;
		}else if(!count){
			isConflict = true;
			msg.push(String.format(I18N.VLAN.vlanCountTip, idList[a]));
			continue;
		}
		//CVID
		for(var c=0; c<cl; c++){
			if(cvids[c][0] == idList[a]){
				if(cvids[c][1] && cvids[c][1] != 3){
					//I18N.VLAN.conflictTip : VLAN {0} 在{1}上配置了{2}规则,不能重复配置
					msg.push(String.format(I18N.VLAN.conflictTip, idList[a],
							cvids[c][2] ? "ONU:" + cvids[c][2] : I18N.VLAN.ponPort, vlanModeString[cvids[c][1]]));
					isConflict = true;
					break;
				}
			}
		}
	}
	var msgL = msg.length;
	if(msgL > 0){
		msg = msg.join("<br>");
		if(msgL > 10){
			msg = "<div style='height:240px;overflow-y:auto;'>" + msg + "</div>";
		}
	}else{
		msg = "";
	}
	return {
		msg : msg,
		isConflict : isConflict
	}
}
//end of transparent

function authLoad(){
	var ids = new Array();
	//ids.add("vidUsageBt");
	//trans
	ids.add("beforeTransId");
	ids.add("afterTransId");
	//aggr
	ids.add("aggregationSvlanId");
	ids.add("aggregationCvlanId");
	//trunk
	ids.add("transparentId");
	ids.add("transparentModeSel");
	//qinq
	ids.add("qinqCosCheckbox2");
	ids.add("qinqCosCheckbox1");
	ids.add("qinqCos");
	ids.add("qinqOuterVlanId");
	ids.add("qinqInnerVlanStartId");
	ids.add("qinqInnerVlanEndId");
	operationAuthInit(operationDevicePower,ids);
	if(portType == 2) {
		var $link = $("#saveBt").prev(); 
    	if( $link.get(0).tagName == "A" ){
    		$link.hide();
    	}
    }
	if(!refreshDevicePower) {
        R.vidUsageBt.setDisabled(true);
    }
	if(!VersionControl.support("baseLlid")){
		R.saveBt.hide();
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
		<div class="edge10 pB0 pT5">
			<table>
			<tr height="400px">
				<Td>
					<div id="tabs">
						<table>
							<tr>
								<td>
<!-- ********** 转换 ************************************************************* -->
									<div id="ponVlanTransTab" class="x-hide-display">
										<div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
											<table cellspacing=7>
												<tr>
													<td colspan="3">
														<fieldset style='width: 507px; height: 295px;padding: 5px 0px 5px 5px;'>
															<legend>@VLAN.translateList@</legend>
															<div id="transIdList"></div>
														</fieldset></td>
												</tr>
												<tr>
													<td>
														<fieldset>
															<legend>@VLAN.ruleManu@</legend>
															<table>
																<tr>
																	<td class="rightBlueTxt w80">@VLAN.originVlan@ ID:</td>
																	<td><input id="beforeTransId" type="text" class="normalInput w100" tooltip='@COMMON.range4094@' maxlength="4" /></td>
																	<td class="rightBlueTxt w100">@VLAN.translatedVlan@ ID:</td>
																	<td><input id="afterTransId" type="text"  class="normalInput w100" tooltip='@COMMON.range4094@' maxlength="4" /></td>
																	<td class="w100"><button id="transSubmit" class=BUTTON75 onclick="transOkClick()" disabled>@COMMON.confirm@</button></td>
																</tr>
															</table>
														</fieldset></td>
												</tr>
											</table>
										</div>
									</div>
<!-- ********** 聚合 ************************************************************ -->
									<div id="ponVlanNto1Tab" class="x-hide-display">
										<div
											style="overflow: auto; padding-left: 10px; padding-top: 10px;">
											<table cellspacing=5>
												<tr>
													<td>
														<table>
															<tr>
																<td>
																	<fieldset style='width: 250px; height: 295px;padding: 5px 0px 5px 5px;'>
																		<legend>@VLAN.aggregatedVlan@</legend>
																		<div id='aggrSvlan_grid'></div>
																	</fieldset></td>
																<td>
																	<fieldset style='width: 255px; height: 295px;padding: 5px 0px 5px 5px;'>
																		<legend id='aggrlegend'>@VLAN.aggregatedVlanList@</legend>
																		<div id='aggrCvlan_grid'></div>
																	</fieldset></td>
															</tr>
														</table></td>
												</tr>
												<tr>
													<td>
														<table>
															<tr>
																<td>
																	<fieldset style='width: 507px;'>
																		<legend>@VLAN.ruleManu@</legend>
																		<table>
																			<tr>
																				<td class="rightBlueTxt w110">@VLAN.trunkedVlan@ ID:</td>
																				<td>
																					<input id="aggregationSvlanId" class="normalInput w60"
																						type="text" maxlength="4"
																						tooltip='@COMMON.range4094@'
																						onChange="aggregationSvlanIdChange()" />
																				</td>
																				<td class="rightBlueTxt w110">@VLAN.trunkVlan@ ID:</td>
																				<td class="w110">
																					<input id="aggregationCvlanId" type="text"
																						class="normalInput w100" tooltip='@VLAN.filterRule@'
																						onfocus="aggregationCvlanIdOn()" />
																				</td>
																				<td class="w110">
																					<button id=aggrSubmit class=BUTTON75
																						disabled onclick="aggrAdd()">@VLAN.add@</button></td>
																			</tr>
																		</table>
																	</fieldset></td>
															</tr>
														</table></td>
												</tr>
											</table>
										</div>
									</div>
<!-- ********** QinQ *********************************************************** -->
									<div id="ponVlanQinqTab" class="x-hide-display">
										<div
											style="overflow: auto; padding-left: 10px; padding-top: 10px;">
											<table cellspacing=5>
												<tr>
													<td>
														<table>
															<tr>
																<td>
																	<fieldset style='width: 150px; height: 238px;padding: 5px 0px 5px 5px;'>
																		<legend>@VLAN.outerVlanList@</legend>
																		<div id='qinqSvlan_grid'></div>
																	</fieldset></td>
																<td>
																	<fieldset style='width: 355px; height: 238px;padding: 5px 0px 5px 5px;'>
																		<legend id='qinqlegend'>@VLAN.add2OuterVlanList@</legend>
																		<div id='qinqCvlan_grid'></div>
																	</fieldset></td>
															</tr>
														</table></td>
												</tr>
												<tr>
													<td>
														<table>
															<tr>
																<td>
																	<fieldset style='width: 507px; height: 45px;'>
																		<legend>@VLAN.outerVlanPri@ </legend>
																		<table>
																			<tr>
																				<td> 
																				<input
																					id="qinqCosCheckbox2" type="radio" name="qinqCos"
																					value="2" onclick="qinqCosModeClick()" checked />@VLAN.useInnerVlanPri@
																				 <c:if test="${is8602G != 'true'}">
																				 <input
																					id="qinqCosCheckbox1" type="radio" name="qinqCos"
																					value="1" onclick="qinqCosModeClick()" />@VLAN.useNewVlanPri@
																				<select id="qinqCos" disabled onchange=qinqCosChange()
																					class="normalSel w60">
																						<option value="0">0</option>
																						<option value="1">1</option>
																						<option value="2">2</option>
																						<option value="3">3</option>
																						<option value="4">4</option>
																						<option value="5">5</option>
																						<option value="6">6</option>
																						<option value="7">7</option>
																				</select></c:if></td>
																			</tr>
																		</table>
																	</fieldset></td>
															</tr>
														</table></td>
												</tr>
												<tr>
													<td>
														<table>
															<tr>
																<td>
																	<fieldset style='width: 507px;'>
																		<legend>@VLAN.ruleManu@</legend>
																		<table>
																			<tr>
																				<td class="rightBlueTxt w80">@VLAN.outerVlan@ ID:</td>
																				<td>
																					<input id="qinqOuterVlanId"
																						class="normalInput w70" type="text" maxlength="4"
																						tooltip='@COMMON.range4094@'
																						onchange="qinqOuterIdChange()" />
																				</td>
																				<td class="rightBlueTxt w80">Start VLAN ID:</td>
																				<td>
																					<input id="qinqInnerVlanStartId"
																						class="normalInput w50" type="text" 
																						onfocus="qinqInnerIdOn()" 
																						tooltip='@COMMON.range4094@'
																						maxlength='4' />
																				</td>
																				<td class="rightBlueTxt w80">End VLAN ID:</td>
																				<td>
																					<input id="qinqInnerVlanEndId"
																						class="normalInput w50" type="text" 
																						onfocus="qinqInnerIdOn()" 
																						tooltip='@COMMON.range4094@'
																						maxlength='4' disabled />
																				</td>
																				<td>
																					<button id='qinqSubmit' class=BUTTON75
																						 disabled onclick="qinqAdd()">@VLAN.add@</button>
																				</td>
																			</tr>
																		</table>
																	</fieldset></td>
															</tr>
														</table></td>
												</tr>
											</table>
										</div>
									</div>
<!-- ********** 透传(代替Trunk的位置) ************************************************** -->
									<div id="ponVlanTransparentTab" class="x-hide-display">
										<div
											style="overflow: auto; padding-left: 10px; padding-top: 10px;">
											<table cellspacing=10>
												<tr>
													<td colspan="2">
														<fieldset style='width: 520px; height: 285px;'>
															<legend>@VLAN.transparentList@</legend>
															<div id="transparentList" style="margin-left:5px;"></div>
														</fieldset></td>
												</tr>
												<tr>
													<td>
														<fieldset>
															<legend>@VLAN.ruleManu@</legend>
															<table>
																<tr>
																	<td class="rightBlueTxt w110">@VLAN.transparent@VLAN ID:</td>
																	<td>
																		<input id="transparentId" type="text" class="normalInput w120"
																			onkeyup="transparentKeyup()" onblur='transparentKeyup()'
																			tooltip='@VLAN.ruleManuTitle@' />
																	</td>
																	<td class="rightBlueTxt w110">@VLAN.mode@:
																		<select id="transparentModeSel" class="normalSel w70">
																			<option value=0 style='color:green'>tag</option>
																			<option value=1 style='color:red'>untag</option>
																		</select>
																	</td>
																	<td class="rightBlueTxt w110">
																		<button id="transparentBt" class="BUTTON75"
																			onclick="transparentBtClick()" disabled>@VLAN.add@</button></td>
																	<td class="rightBlueTxt w110">
																		<button id="deleteTrans" class="BUTTON75"
																			onclick="batchDeleteTransClick()" disabled="disabled">@COMMON.delete@</button></td>
																</tr>
															</table>
														</fieldset></td>
												</tr>
											</table>
										</div>
									</div></td>
							</tr>
						</table>
					</div></Td>
				<td>
					<div>
						<table>
							<tr>
								<td>
									<div id="tip">
										<table>
											<tr>
												<td>
<!-- ********** 说明 ********************************************************************* -->
													<div id="tip1Tab" class="x-hide-display">
														<div
															style="overflow: auto; padding-left: 10px; padding-top: 10px;">
															<table cellspacing=0>
																<tr>
																	<td>
																		<fieldset>
																			<legend id="shuoming">@VLAN.trans@</legend>
																		</fieldset></td>
																</tr>
															</table>
														</div>
													</div>
<!-- ********** CVID ******************************************************************** -->
													<div id="tip2Tab" class="x-hide-display">
														<div
															style="overflow: auto; padding-left: 10px; padding-top: 10px;">
															<table cellspacing=0>
																<tr>
																	<td>
																		<fieldset>
																			<legend>@VLAN.cvidList@</legend>
																			<div id="cvidGrid"></div>
																		</fieldset>
																	</td>
																</tr>
																<tr>  
																	<td>
																		<fieldset>
																			<legend>@VLAN.cvidQueryFilter@</legend>
																			<table>
																				<tr>
																					<td class="blueTxt w180">CVID:
																						<input id="CvidSearchId" class="normalInput w100" 
																						type="text" onkeyup="cvidKeyup()" 
																						tooltip='@COMMON.range4094@'
																						maxlength='4' /></td>
																				</tr>
																			</table>
																		</fieldset></td>
																</tr>
															</table>
														</div>
													</div>
<!-- ********** SVID ******************************************************************** -->
													<div id="tip3Tab" class="x-hide-display">
														<div
															style="overflow: auto; padding-left: 10px; padding-top: 10px;">
															<table cellspacing=0>
																<tr>
																	<td>
																		<fieldset>
																			<legend>@VLAN.svidList@</legend>
																			<div id="svidGrid"></div>
																		</fieldset></td>
																</tr>
																<tr>
																	<td>
																		<fieldset>
																			<legend>@VLAN.svidQueryFilter@</legend>
																			<table>
																				<tr>
																					<td class="blueTxt w180">SVID:
																						<input id="SvidSearchId"
																						class="normalInput w100" type="text"
																						onkeyup="svidKeyup()" 
																						tooltip='@COMMON.range4094@'
																						maxlength='4' /></td>
																				</tr>
																			</table>
																		</fieldset></td>
																</tr>
															</table>
														</div>
													</div></td>
											</tr>
										</table>
									</div></td>
							</tr>
						</table>
					</div></td>
			</tr>
			</table>
			</div>
			<Zeta:ButtonGroup>
				<Zeta:Button id="saveBt" onClick="llidModeChange()"  icon="miniIcoSaveOK">@VLAN.cfgWithLLID@</Zeta:Button>
				<Zeta:Button id="vidUsageBt" onClick="getDataFormDevice()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
				<Zeta:Button onClick="cancelClick()" icon="miniIcoWrong">@COMMON.close@</Zeta:Button>
			</Zeta:ButtonGroup>
<div id=loadingDiv style="position:absolute;background-color:#ffffbe;left:600;top:100;display:none;">@COMMON.loading@</div>
</body>
</Zeta:HTML>