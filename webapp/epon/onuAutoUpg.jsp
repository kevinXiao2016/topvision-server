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
    import js.tools.ipText
    module epon
    css css/white/disabledStyle
</Zeta:Loader>

<script type="text/javascript">
var entityId = '${entityId}';
var ponList = ${ponOnuProfileList};
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
if(ponList.join("") == "false"){
	ponList = new Array();
}

var profileListInit = ${profileList};
if(profileListInit.join("") == "false"){
	profileListInit = new Array();
}
var onuTypeInt = ${onuTypeMap};
onuTypeInt["CC8800A"] = 241;
onuTypeInt["CC8800B"] = 242;
var onuTypeStr = new Array();
for(var x in onuTypeInt){
	if(typeof x == "string"){
		if(parseInt(onuTypeInt[x]) < 241 ){
			if(onuTypeInt[x] == 255 || onuTypeInt[x] == 0){
				onuTypeStr[onuTypeInt[x]] = "ANY";
			}else{
				onuTypeStr[onuTypeInt[x]] = "PN"+x;
			}
		}else{
			onuTypeStr[onuTypeInt[x]] = x;
		}
	}
}

/* [[profileId, profileName, proOnuType, proHwVersion, proNewVersion, proMode, proBoot, proApp, proWebs, proOther, proPers
  , proBandStat, [{slotNo: , ponNo: }, {}, {}...]], [], []...] */
var proData = new Array();
var proStore;
var proGrid;
//[{slotNo: , slotId: , ponNo: , ponId: , proList: [profileId, profileId, ...]}, {}, {}...]
var ponData = new Array();
var ponStore;
var ponGrid;
//[[{onuId:, onuNo:, onuType:, onuHwVersion:, onuSoftwareVersion:, onlineStat:, onuProMatch:0, onuLoc}, {}, {}...], [], []]
//0:待升级，1:已升级，2:不匹配
var upgData = new Array();
var upgStore;
var upgGrid;

var proId = 0;
var ponId = 0;
var changeFlag = 0;

var profileMaxNum = 20;
//模板冲突关系列表 [[null], [2,3], [1,3], [1,2],...] 共profileMaxNum + 1 项，第一项不用
var conflicted = new Array();

/**
 *  Profile Grid
 */
function loadProData(){
	proData = profileListInit.slice(0);
	setTimeout(function(){
		conflicted = new Array();
		for(var k=0; k<profileMaxNum + 1; k++){
			conflicted[k] = new Array();
		}
		for(var a=0,n=proData.length; a<n; a++){
			for(var b=0; b<n; b++){
				if(proData[a][0] != proData[b][0] && proData[a][0] < profileMaxNum + 1 && proData[b][0] < profileMaxNum + 1){
					if(proData[a][2] == proData[b][2] && proData[a][3] == proData[b][3]){
						conflicted[proData[a][0]].push(proData[b][0]);
						conflicted[proData[b][0]].push(proData[a][0]);
					}
				}
			}
		}
	}, 0);
}

function upgTimeRender(value, meta, record){
	if(value == null || value.length == 0){
		return "--";
	}else{
		return value;
	}
}

function loadProGrid(){
	var cm = [ {header: I18N.onuAutoUpg.he.proId, id:'id1', dataIndex: 'profileId', align: 'center', sortable: true, width:50}
			 , {header: I18N.onuAutoUpg.he.proName, id:'id2', dataIndex: 'profileName', align: 'center', sortable: true, width:120}
			 , {header: "@onuAutoUpg.he.startTime@", id:'id14', dataIndex: 'proUpgTime', align: 'center', sortable: true, width:120, renderer : upgTimeRender}
			 , {header: I18N.onuAutoUpg.he.onuType, id:'id3', dataIndex: 'proOnuType', align: 'center',
				 sortable: true, width:80, renderer: proOnuTypeRender}
			 , {header: I18N.onuAutoUpg.he.hwVersion, id:'id4', dataIndex: 'proHwVersion', align: 'center', sortable: true, width:110}
			 , {header: I18N.onuAutoUpg.he.newVersion, id:'id5', dataIndex: 'proNewVersion', align: 'center', sortable: true, width:110}
			 , {header: I18N.onuAutoUpg.he.mode, id:'id6', dataIndex: 'proMode', align: 'center',
				 sortable: true, width:60, renderer: proModeRender}
			 , {header: I18N.onuAutoUpg.he.bandStat, id:'id7', dataIndex: 'proBandStat',
				 align: 'center', width:75, renderer: proBandStatRender}
			 , {header: 'BOOT', id:'id8', dataIndex: 'proBoot', align: 'center', hidden: true, width:150}
			 , {header: 'APP', id:'id9', dataIndex: 'proApp', align: 'center', hidden: true, width:150}
			 , {header: 'WEBS', id:'id10', dataIndex: 'proWebs', align: 'center', hidden: true, width:150}
			 , {header: 'OTHER', id:'id11', dataIndex: 'proOther', align: 'center', hidden: true, width:150}
			 , {header: 'PERS', id:'id12', dataIndex: 'proPers', align: 'center', hidden: true, width:150}
			 , {header: I18N.COMMON.manu, id:'id13', align: 'center', width:90, renderer: proHandler}];
	proStore = new Ext.data.SimpleStore({
		data : proData,
		fields: ['profileId', 'profileName', 'proOnuType','proHwVersion','proNewVersion', 'proMode', 'proBoot', 'proApp',
		 		'proWebs', 'proOther', 'proPers', 'proBandStat', 'proBandPonIdList', 'proUpgTime']
	});
	var sty = "background:transparent;border:0px;margin-left:10px;width:70px;cursor:hand;height:20px;";
	//var src = "/images/add.png";
	var hander = "onmouseover='addBtMouseOver(this)' onmouseout='addBtMouseOut(this)'";
	var addProfileBt=null;
	if(operationDevicePower){		
		/* addProfileBt = String.format("<button style='{0}' onclick='addProfileBtClick()' {2}><img src='{1}' />&nbsp;&nbsp;{3}</button>",
				sty, "/images/add.png", hander, I18N.onuAutoUpg.co.create); */
	}else{
		$("#topAddBtn").hide();
		/* addProfileBt = String.format("<button style='{0}'><img src='{1}' />&nbsp;&nbsp;{2}</button>",
				sty, "/images/addDisable.png", I18N.onuAutoUpg.co.create); */
	}
	proGrid = new Ext.grid.GridPanel({
		id : 'ProGrid',
		//title : I18N.onuAutoUpg.tit.upgProList + addProfileBt,
		title : I18N.onuAutoUpg.tit.upgProList,
		renderTo : 'proGridDiv',
		border : true,
		frame : false,
		autoScroll : true,
		cls: 'normalTable',
		viewConfig:{
			forceFit:true
		},
		height : 130,
		store : proStore,
		columns : cm,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				selectionchange : function(){
					var sel = proGrid.getSelectionModel().getSelected();
					if(sel){
						var tmpId = sel.data.profileId;
						if(tmpId > 0 && tmpId < profileMaxNum + 1){
							var tmpF = ++changeFlag;
							setTimeout(function(){
								if(tmpF == changeFlag){
									proId = tmpId;
									gridSelectionChange();
								}
							}, 300);
						}
					}
				}
			}
		}),
		listeners : {
			dblclick : function(){
				setTimeout(function(){
					if(proId){
						modifyProfile(proId);
					}
				}, 170);
			}
		}
	});
}
var addBtMouseFlag = true;
function addBtMouseOver(el){
	addBtMouseFlag = false;
	$(el).css({'background-color': '#dbf7fd', 'font-size' : 13});
}
function addBtMouseOut(el){
	addBtMouseFlag = true;
	setTimeout(function(){
		if(addBtMouseFlag){
			$(el).css({'background-color': 'transparent', 'font-size' : 12});
		}
	}, 80);
}
function proOnuTypeRender(v, c, record){
	return onuTypeStr[v] ? onuTypeStr[v] : I18N.onuAutoUpg.tip.unknowType;
}
function proModeRender(v, c, record){
	var mode = parseInt(v);
	var tmpV = 'CTC';
	switch(v){
		case 4:
			tmpV = "CTC";
			break;
		case 16:
			tmpV = "baseline";
			break;
		case 32:
			tmpV = "extend";
			break;
		case 64:
			tmpV = "auto";
			break;
	}
	var tmpS = v == 4 ? I18N.onuAutoUpg.tip.ctcTip : I18N.onuAutoUpg.tip.tkTip;
	return String.format("<span title='{1}'>{0}</span>", tmpV, tmpS);
}
function proBandStatRender(v, c, record){
	return v == 1 ? I18N.onuAutoUpg.pro.bound : I18N.onuAutoUpg.pro.unbound;
}
function proHandler(v, c, record){
	if(operationDevicePower){
		var re = String.format("<a href='javascript:;' title='{1}' onclick='modifyProfile({0})' >@COMMON.edit@</a>",
					record.data.profileId, I18N.onuAutoUpg.ac.modifyPro);
		if(record.data.proBandStat != 1){
			re += String.format(" / <a href='javascript:;' title='{1}' onclick='deleteProfile({0})' >@COMMON.delete@</a>",
					record.data.profileId, I18N.onuAutoUpg.ac.deletePro);
		}
	}else{
		var re = String.format("<span class='lightGrayTxt' title='{0}' >@COMMON.edit@</span>", I18N.onuAutoUpg.ac.modifyPro);
		if(record.data.proBandStat != 1){
			re += String.format(" / <span class='lightGrayTxt' title='{0}'>@COMMON.delete@</span>",I18N.onuAutoUpg.ac.deletePro);
		}
	}
	return re;
}
function modifyProfile(profileId){
	window.top.createDialog('onuAutoUpgProfile', I18N.onuAutoUpg.tit.upgPro, 800, 500, '/onu/showOnuAutoUpgProfile.tv?entityId='
			+ entityId + '&profileId=' + profileId, null, true, true);
}
function deleteProfile(profileId){
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.mes.delProConfirm, profileId), function(type) {
		if (type == 'no') {
			return false;
		}else{
			window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.onuAutoUpg.mes.deletingPro, profileId), 'ext-mb-waiting');
			var params = {entityId : entityId, profileId : profileId}
			Ext.Ajax.request({
				url : '/onu/delOnuAutoUpgProfile.tv?&r=' + Math.random(),
				success : function(response) {
					if(response.responseText){
						refreshThisWindow(I18N.onuAutoUpg.mes.delProFailed, false);
					}else{
						refreshThisWindow(I18N.onuAutoUpg.mes.delProSuc, true);
					}
				},
				failure : function(response) {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.delProFailed);
				},
				params: params
			});
		}
	});
}
function addProfileBtClick(){
	var tmpA = new Array();
	for(var i=0; i<profileMaxNum; i++){
		tmpA[i] = true;
	}
	for(var a=0; a<proData.length; a++){
		tmpA[proData[a][0]] = false;
	}
	for(var b=0; b<tmpA.length; b++){
		if(tmpA[b]){
			window.top.createDialog('onuAutoUpgAddPro', I18N.onuAutoUpg.ac.addPro, 800, 600, '/onu/showOnuAutoUpgAddPro.tv?entityId='
					+ entityId, null, true, true);
			return;
		}
	}
	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.tip.addProFull, profileMaxNum));
}
//end of proGrid
/**
 * PON Grid
 */
function loadPonData(){
	ponData = ponList.slice(0);
	var tmp = new Array();
	for(var a=0; a<ponData.length; a++){
		var s = ponData[a].slotId;
		if(tmp.indexOf(s) == -1){
			tmp.push(s);
		}
	}
	/*if(tmp.length > 2){
		ponGrid.getView().collapseAllGroups();
	}*/
}
function loadPonGrid(){
	var reader = new Ext.data.JsonReader({
		root: "data",
		fields: [{name: 'slotNo'}, {name: 'slotId'}, {name: 'ponNo'}, {name: 'ponId'}, {name: 'proList'}]
	});
	var cm = new Ext.grid.ColumnModel([
	    {header: I18N.onuAutoUpg.he.ponSlot, dataIndex:'slotNo', width:120, align: 'center',sortable: false,resizable: true, id:'id1',
		    menuDisabled :true},
		{header: I18N.onuAutoUpg.he.portNum,dataIndex:'ponNo',width:120, align: 'left',sortable: false,resizable: true, id: 'id2',
			menuDisabled :true, renderer: ponRender}
	]);
	ponStore = new Ext.data.GroupingStore({
	 	data: {data: ponData},
		remoteGroup: false,
		remoteSort: false,
		reader: reader,
		sortInfo: {field: 'slotNo', direction: "ASC"},
		groupField: 'slotNo',
		groupOnSort: false
	});
	ponGrid = new Ext.grid.GridPanel({
        border:true, 
        region:'center',
        width: 300,
        height: 240,
  	    store : ponStore,
	    title : I18N.onuAutoUpg.he.ponList, 
        cm : cm,
        hideHeaders: true,
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect: true,
            listeners : {
            	'selectionchange' : function (){
    				var sel = ponGrid.getSelectionModel().getSelected();
    				if(sel){
						var tmpP = sel.data.ponId;
						if(tmpP){
							var tmpF = ++changeFlag;
							setTimeout(function(){
								if(tmpF == changeFlag){
									ponId = tmpP;
									gridSelectionChange();
								}
							}, 300);
						}
        			}
    			}
            }
        }),
      	view: new Ext.grid.GroupingView({
            forceFit: true,
	        hideGroupedColumn: true,
	        enableNoGroups: true
        }),
  		renderTo: "ponGridDiv",
  		listeners : {
			'viewready' : function(){
				initButton('all');
				if(window.parent.onuAutoUpgTmpData){
					var tmpPonId = window.parent.onuAutoUpgTmpData.ponId;
					var tmpProId = window.parent.onuAutoUpgTmpData.proId;
					setTimeout(function(){
						if(tmpPonId){
							try{
								var num1 = ponStore.find("ponId", tmpPonId);
								ponId = tmpPonId;
								ponGrid.getSelectionModel().selectRow(num1);
								ponGrid.getView().focusRow(num1);
							}catch(e){
								ponId = 0;
								window.parent.onuAutoUpgTmpData.ponId = null;
							}
						}
						if(tmpProId){
							try{
								var num2 = proStore.find("profileId", tmpProId);
								proId = tmpProId;
								proGrid.getSelectionModel().selectRow(num2);
								proGrid.getView.focusRow(num2);
							}catch(e){
								proId = 0;
								window.parent.onuAutoUpgTmpData.proId = null;
							}
						}
						gridSelectionChange();
					}, 300);
				}
			},
			'rowcontextmenu': function(grid,rowIndex,e){
				return;
				var model = ponGrid.getSelectionModel();
				var tmpF = ++changeFlag;
				setTimeout(function(){
					if(tmpF == changeFlag){
						model.selectRow(rowIndex);
						if(proId){
							var sel = model.getSelected();
							var slotNum = sel.data.slotNo;
							var ponNum = sel.data.ponNo;
							var items = [];
							//(1:false, 2:true); 0:pon绑定; 1:slot绑定数; 2:slot全绑定; 3:olt绑定数; 4:olt全绑定;
							var flag = [0, 0, 0, 0, 0];
							var thisPonTotleNum = 0;
							var n = ponData.length;
							for(var k=0; k<n; k++){
								if(ponData[k].slotNo == slotNum){
									thisPonTotleNum++;
								}
							}
							if(!thisPonTotleNum){
								thisPonTotleNum = 8;
							}
							for(var i=0; i<n; i++){
								if(slotNum == ponData[i].slotNo){
									if(!flag[0] && ponNum == ponData[i].ponNo){
										if(ponData[i].proList.indexOf(proId) == -1){
											flag[0] = 1;
											flag[2] = 1;
											flag[4] = 1;
										}else{
											flag[0] = 2;
											flag[1]++;
											flag[3]++;
										}
									}else if(!flag[1] || !flag[2]){
										if(ponData[i].proList.indexOf(proId) == -1){
											flag[2] = 1;
											flag[4] = 1;
										}else{
											flag[1]++;
											flag[3]++;
										}
									}
								} else if(!flag[3] || !flag[4]){
									if(ponData[i].proList.indexOf(proId) == -1){
										flag[4] = 1;
									} else {
										flag[3]++;
									}
								}
								if(flag[1] == thisPonTotleNum){
									flag[2] = 2;
								}
								if(flag[3] == n){
									flag[4] = 2;
								}
							}
							var iconStr = {band: "/css/office2007/dd/drop-add.gif", unband: "/css/office2007/dd/drop-no.gif",
											restart: "/images/silk/reload.png"};
							var itText = {restart: "<font style='color:blue;'>" + I18N.onuAutoUpg.ac.restart + "</font>", 
											unband: "<font style='color:red;'>" + I18N.onuAutoUpg.ac.unband + "</font>",
											band: "<font style='color:green;'>" + I18N.onuAutoUpg.ac.band + "</font>"};
							if(flag[0] == 2){//PON绑定
								items.push({text : itText.restart + I18N.onuAutoUpg.tit.ponPortUpg ,icon: iconStr.restart,
									handler: function(){restartOnuAutoUpg(2, sel.data.slotNo, sel.data.ponNo);}});
								items.push({text : itText.unband + I18N.onuAutoUpg.tit.ponPortBand ,icon: iconStr.unband,
									handler: function(){unbandOnuAutoUpg(2, sel.data.ponId);}});
								items.push("-");
							}else{//PON未绑
								items.push({text : itText.band + I18N.onuAutoUpg.tit.thisPon ,icon: iconStr.band, 
									handler: function(){bandOnuAutoUpg(2, sel.data.ponId);}});
								items.push("-");
							}
							if(flag[1] > 0){//solt有绑定
								items.push({text : itText.restart + I18N.onuAutoUpg.tit.ponSlotUpg ,icon: iconStr.restart, 
									handler: function(){restartOnuAutoUpg(1, sel.data.slotNo, 0);}});
								items.push({text : itText.unband + I18N.onuAutoUpg.tit.ponSlotBand ,icon: iconStr.unband, 
									handler: function(){unbandOnuAutoUpg(1, sel.data.slotId);}});
							}
							if(flag[2] == 1){//slot未全绑
								items.push({text : itText.band + I18N.onuAutoUpg.tit.thisSlot ,icon: iconStr.band, 
									handler: function(){bandOnuAutoUpg(1, sel.data.slotId);}});
								items.push("-");
							}
							if(flag[3] > 0){//olt有绑定
								/* 暂时不支持重启全设备
								items.push({text : itText.restart + I18N.onuAutoUpg.tit.deviceUpg ,icon: iconStr.restart, 
									handler: function(){restartOnuAutoUpg(0, 0, 0);}}); */
								items.push({text : itText.unband + I18N.onuAutoUpg.tit.deviceBand ,icon: iconStr.unband, 
									handler: function(){unbandOnuAutoUpg(0, entityId);}});
							}
							if(flag[4] == 1){//olt未全绑
								items.push({text : itText.band + I18N.onuAutoUpg.tit.thisDevice ,icon: iconStr.band, 
									handler: function(){bandOnuAutoUpg(0, entityId);}});
							}
							if(operationDevicePower){
								loadPonMenu(items);
								ponMenu.showAt(e.getXY());
							}
						}
					}
				}, 200);
			}
  	  	}
    });
}
var ponMenu;
function loadPonMenu(items){
	ponMenu = new Ext.menu.Menu({items : items});
	loadPonMenu = function(items){
		ponMenu.removeAll();
		ponMenu.add(items);
	}
}
function ponRender(v, c, record){
	var tmpId = "ponSpan_" + record.data.ponId;
	var re = String.format("<span id={2} class=ponSpanClass>{1} / {0}</span>", v, record.data.slotNo, tmpId);
	if(proId){
		re += initBandBt(record.data);
	}
	return re;
}
function initBandBt(data){
	var flag = data.proList.indexOf(proId) == -1;
	var tmp1 = flag ? 'blue' : 'green';
	var tmp2 = flag ? I18N.onuAutoUpg.tip.thisPonUnband + proId : I18N.onuAutoUpg.tip.thisPonBound + proId;
	var hander = flag ? 'bandInThisPon()' : 'bandOutThisPon()';
	var tmp3 = String.format("class=BUTTON75 style='margin-left:15px;' onclick='{0}'", hander);
	var tmp4 = flag ? I18N.onuAutoUpg.tip.bandThis : I18N.onuAutoUpg.tip.unbandThis;
	if(operationDevicePower){
		return String.format("<span class=bandBtClass><font style='color:{0};'>{1}</font><button {2}>{3}</button></span>",
				tmp1, tmp2, tmp3, tmp4);
	}else{
		return String.format("<span class=bandBtClass><font style='color:{0};'>{1}</font><button {2} disabled>{3}</button></span>",
				tmp1, tmp2, tmp3, tmp4);
	}
}
//end of ponGrid
/**
 * UPG Grid
 */
function loadUpgData(data){
	var sel = ponGrid.getSelectionModel().getSelected();
	var ponIdTmp = 0;
	if(sel){
		ponIdTmp = sel.data.ponId;
	}
	var proIndex = -1;
	for(var a=0; a<proData.length; a++){
		if(proData[a][0] == proId){
			proIndex = a;
			break;
		}
	}
	if(proIndex > -1 && ponIdTmp > 0){
		//$("#upgGridDiv").empty();
		$("#upgGridTable tbody").empty();
		upgData = new Array();
		upgData[0] = new Array();
		upgData[1] = new Array();
		upgData[2] = new Array();
		var tmpP = sel.data.slotNo + " / " + sel.data.ponNo;
		for(var i=0; i<data.data.length; i++){
			data.data[i].onuLoc = tmpP + ' : ' + data.data[i].onuNo;
			var $profile = proData[proIndex];
			var $onu = data.data[i];
			/*if(($profile[2] == "none" || !$profile[2] || $onu.onuType == $profile[2]
				|| (onuTypeStr[$profile[2]] && onuTypeStr[$profile[2]] == data.data[i].onuType)) 
					&& (!$profile[3] || data.data[i].onuHwVersion == $profile[3])){
				if(data.data[i].onuSoftwareVersion == $profile[4]){
					data.data[i].onuProMatch = 1;
				}else{
					data.data[i].onuProMatch = 0;
				}
			}else{
				data.data[i].onuProMatch = 2;
			}*/
			if($onu.onuType == onuTypeStr[$profile[2]] && ( $profile[3] ? $onu.onuHwVersion == $profile[3] : true )){
				if( ( $profile[4] ? $onu.onuSoftwareVersion == $profile[4] : true ) /* && ( $profile[3] ? $onu.onuHwVersion == $profile[3] : true ) */ ){
					$onu.onuProMatch = 1;
				}else{
					$onu.onuProMatch = 0;
				}
				
				/*if($onu.onuHwVersion == $profile[3] && $onu.onuSoftwareVersion == $profile[4]){
					$onu.onuProMatch = 1;
				}else{
				}*/
			}else{
				$onu.onuProMatch = 2;
			}
			upgData[data.data[i].onuProMatch].push(data.data[i]);
		}
		loadUpgGrid();
	}
}
function loadUpgGrid(){
	var p = $("#upgGridTable tbody")
	for(var a=0; a<3; a++){
		var le = upgData[a].length;
		if(le > 0){
			var sHead = initUpgHeader(a, le)
			p.append(sHead);
			for(var b=0; b<le; b++){
				var sBody = initUpgOnu(a, upgData[a][b]);
				p.append(sBody);
			}
		}
	}
}
function initUpgHeader(a, le){
	var str = [I18N.onuAutoUpg.co.waitUpg, I18N.onuAutoUpg.co.alreadyUpg, I18N.onuAutoUpg.co.notMatch];
	var color = ['blue', 'green', 'gray'];
	var re = String.format("<tr><td colspan='5'><div flag='expend' id='upgHeaderDiv{0}' " +
			"style='cursor:hand;height:25px;background-color:#effefe;'>", a);
	re += String.format("<font style='margin-left:5px;' color={1}>{0}</font> {3}: {2}",
			str[a], color[a], le, I18N.onuAutoUpg.co.onuTotalNum);
	re += "</div></td></tr>";
	return re;
}
function initUpgOnu(a, d){
	var str = [I18N.onuAutoUpg.co.waitUpg, I18N.onuAutoUpg.co.alreadyUpg, I18N.onuAutoUpg.co.notMatch];
	var color = ['blue', 'green', 'gray'];
	var re = String.format("<tr class='upgOnuClass{0}'>", a);
	re += String.format("<td class='wordBreak' width='77' style='padding:0px; margin-left:10px;color:{1};' title='{2} ONU'>{0}</td>", d.onuLoc, color[a], str[a]);
	re += String.format("<td class='wordBreak' width='77' style='padding:0px;' title='{0}'>{0}</td>", d.onuType || "none");
	re += String.format("<td class='wordBreak' width='77' style='padding:0px;' title='{0}'>{0}</td>", d.onuHwVersion || I18N.onuAutoUpg.co.unknown);
	re += String.format("<td class='wordBreak' width='160' style='padding:0px;' title='{0}'>{0}</td>", d.onuSoftwareVersion || I18N.onuAutoUpg.co.unknown);
	var src = d.onlineStat == 1 ? '/images/network/port/up.png' : '/images/network/port/down.png';
	var ti = d.onlineStat == 1 ? I18N.onuAutoUpg.co.online : I18N.onuAutoUpg.co.offline;
	re += String.format("<td class='wordBreak' align='center' style='padding:0px;' ><img src='{0}' title='{1}'  /></td></tr>", src, ti);
	return re;
}
function upgHeaderClick(a){
	var d = $("#upgHeaderDiv" + a);
	var i = $("#upgHeaderImg" + a);
	if(d.attr("flag") == "expend"){
		$(".upgOnuClass" + a).hide();
		d.attr("flag", "coll").attr("title", I18N.onuAutoUpg.co.clickToExpand);
		i.attr("src", "/css/office2007/tree/elbow-plus-nl.gif");
	}else{
		var obj = $(".upgOnuClass" + a);
		obj.css("display","block");
		d.attr("flag", "expend").attr("title", I18N.onuAutoUpg.co.clickToCollapse);
		i.attr("src", "/css/office2007/tree/elbow-minus-nl.gif");
	}
}
//end of upgGrid
//grid change
function gridSelectionChange(){
	if(proId){
		$(".bandBtClass").remove();
		for(var a=0; a<ponData.length; a++){
			$("#ponSpan_" + ponData[a].ponId).after(initBandBt(ponData[a]));
		}
		initButton(75);
		if(ponId){
			var tmpF = ++changeFlag;
			$.ajax({
				url : '/onu/loadOnuAutoUpgInfo.tv?ponId=' + ponId,cache:false,
				success : function(data) {
					if(tmpF == changeFlag){
						loadUpgData(data);
					}
				}
			});
		}
	}
}
//end of grid change

/**
 * onReady
 */
Ext.onReady(function(){
	loadPonData();
	loadPonGrid();
	loadProData();
	loadProGrid();
});

/**
 * set
 */
function bandInThisPon(){
	setTimeout(function(){
		var pp = ponGrid.getSelectionModel().getSelected().data;
		var tmpF = 0;
		for(var a=0,n=pp.proList.length; a<n; a++){
			if(conflicted[pp.proList[a]].indexOf(proId) > -1){
				tmpF = pp.proList[a];
				break;
			}
		}
		if(tmpF){
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.mes.conflictBand, pp.slotNo, pp.ponNo, tmpF, proId));
			return;
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.onuAutoUpg.mes.banding, proId, pp.slotNo, pp.ponNo));
		var params = {entityId: entityId, profileId: proId, slotNo: pp.slotNo, ponNo: pp.ponNo};
		Ext.Ajax.request({
			url : '/onu/bandOnuAutoUpgProfile.tv?r=' + Math.random(),
			success : function(response) {
				if(response.responseText){
					refreshThisWindow(String.format(I18N.onuAutoUpg.mes.bandFailed, proId, pp.slotNo, pp.ponNo), false);
				}else{
					refreshThisWindow(String.format(I18N.onuAutoUpg.mes.bandSuc, proId, pp.slotNo, pp.ponNo), true);
				}
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						String.format(I18N.onuAutoUpg.mes.bandFailed, proId, pp.slotNo, pp.ponNo));
			},
			params: params
		});
	}, 50);
}
function bandOutThisPon(){
	setTimeout(function(){
		var pp = ponGrid.getSelectionModel().getSelected().data;
		window.parent.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.onuAutoUpg.mes.unbanding, proId, pp.slotNo, pp.ponNo));
		var params = {entityId: entityId, profileId: proId, slotNo: pp.slotNo, ponNo: pp.ponNo};
		Ext.Ajax.request({
			url : '/onu/unbandOnuAutoUpgProfile.tv?r=' + Math.random(),
			success : function(response) {
				if(response.responseText){
					refreshThisWindow(String.format(I18N.onuAutoUpg.mes.unbandFailed, proId, pp.slotNo, pp.ponNo), false);
				}else{
					refreshThisWindow(String.format(I18N.onuAutoUpg.mes.unbandSuc, proId, pp.slotNo, pp.ponNo), true);
				}
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						String.format(I18N.onuAutoUpg.mes.unbandFailed, proId, pp.slotNo, pp.ponNo));
			},
			params: params
		});
	}, 50);
}
function restartOnuAutoUpg(s, slotNum, ponNum){//0:olt, 1:slot, 2:pon
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.restartConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		var par = {entityId: entityId, profileId: proId, slotNo: 0, ponNo: 0};
		if(s > 0){
			par.slotNo = slotNum;
		}
		if(s > 1){
			par.ponNo = ponNum;
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.restarting);
		Ext.Ajax.request({
			url : '/onu/restartOnuAutoUpg.tv?r=' + Math.random(),
			success : function(response) {
				if(response.responseText){
					refreshThisWindow(I18N.onuAutoUpg.mes.restartFailed, false);
				}else{
					refreshThisWindow(I18N.onuAutoUpg.mes.restartSuc, true);
				}
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.restartFailed);
			},
			params: par
		});
	});
}
function bandOnuAutoUpg(s, id){//0:olt, 1:slot, 2:pon
	if(s == 2){
		bandInThisPon();
		return;
	}
	if(proId){
		var tmpProId = proId;
		var list = new Array();
		if(s == 1 || s == 0){
			var sel = ponGrid.getSelectionModel().getSelected();
			if(sel){
				var slotNoTmp = sel.data.slotNo;
				var conflictList = conflicted[tmpProId];
				var conLen = conflictList.length;
				var conflictStr = new Array();
				var portStrList = new Array();
				for(var a=0,n=ponData.length; a<n; a++){
					if((slotNoTmp == ponData[a].slotNo || s == 0) && ponData[a].proList.indexOf(tmpProId) == -1){
						var tmpFF = true;
						for(var b=0; b<conLen; b++){
							if(ponData[a].proList.indexOf(conflictList[b]) > -1){
								conflictStr.push({ponPort: ponData[a].slotNo + "/" + ponData[a].ponNo, confilictProId: conflictList[b]});
								tmpFF = false;
								break;
							}
						}
						if(tmpFF){
							list.push(ponData[a]);
							portStrList.push(ponData[a].slotNo + "/" + ponData[a].ponNo);
						}
					}
				}
				var str = "";
				for(var i=0; i<conflictStr.length; i++){
					str += String.format("<nobr>" + I18N.onuAutoUpg.tip.conflict + "</nobr><br>",
							conflictStr[i].ponPort, conflictStr[i].confilictProId, tmpProId);
				}
				if(list.length > 0){
					window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.tip.batchBandConfirm,
							tmpProId, str, portStrList.join("、")), function(type) {
						if (type == 'no') {
							return;
						}
						window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.bandings);
						var re = {flag: false};
						bandMgmt(list, re, tmpProId);
					});
				}else{
					window.parent.showMessageDlg(I18N.COMMON.tip, str);
				}
			}
		}
	}
}
function bandMgmt(list, re, tmpProId){
	if(list.length > 0){
		bandAPon(list, re, tmpProId);
	}else if(re.flag){
		var msg = "";
		var sucList = new Array();
		for(var x in re){
			if(x.length == 3 && x.indexOf("/") > -1){
				if(!re[x] || re[x] == 'success'){
					sucList.push(x);
				}else{
					msg += "<nobr>" + String.format(I18N.onuAutoUpg.tip.batchBandFailed, x, tmpProId) + re[x] + "</nobr><br>";
				}
			}
		}
		if(sucList.length > 0){
			msg += String.format(I18N.onuAutoUpg.tip.batchBandSuc, sucList.join("、"));
		}
		refreshThisWindow(msg, true);
	}
}
function bandAPon(list, re, tmpProId){
	re.flag = true;
	var params = {entityId: entityId, profileId: tmpProId, slotNo: list[0].slotNo, ponNo: list[0].ponNo}
	Ext.Ajax.request({
		url : '/onu/bandOnuAutoUpgProfile.tv?r=' + Math.random(),
		success : function(response) {
			if(response.responseText){
				re[list[0].slotNo + "/" + list[0].ponNo] = response.responseText;
			}else{
				re[list[0].slotNo + "/" + list[0].ponNo] = 'success';
			}
			list.splice(0, 1);
			bandMgmt(list, re, tmpProId);
		},
		failure : function(response) {
			re[list[0].slotNo + "/" + list[0].ponNo] = response.responseText;
			list.splice(0, 1);
			bandMgmt(list, re, tmpProId);
		},
		params: params
	});
}
function unbandOnuAutoUpg(s, id){//0:olt, 1:slot, 2:pon
	if(s == 2){
		bandOutThisPon();
		return;
	}
	if(proId){
		var tmpProId = proId;
		window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.onuAutoUpg.tip.batchUnbandConfirm, tmpProId), function(type) {
			if (type == 'no') {
				return;
			}
			var list = new Array();
			if(s == 1 || s == 0){
				var sel = ponGrid.getSelectionModel().getSelected();
				if(sel){
					var slotNoTmp = sel.data.slotNo;
					for(var a=0,n=ponData.length; a<n; a++){
						if((slotNoTmp == ponData[a].slotNo || s == 0) && ponData[a].proList.indexOf(tmpProId) > -1){
							list.push(ponData[a]);
						}
					}
					window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.unbandings);
					var re = {flag: false};
					unbandMgmt(list, re, tmpProId);
				}
			}
		});
	}
}
function unbandMgmt(list, re, tmpProId){
	if(list.length > 0){
		unbandAPon(list, re, tmpProId);
	}else if(re.flag){
		var msg = "";
		var sucList = new Array();
		for(var x in re){
			if(x.length == 3 && x.indexOf("/") > -1){
				if(!re[x] || re[x] == 'success'){
					sucList.push(x);
				}else{
					msg += "<nobr>" + String.format(I18N.onuAutoUpg.tip.batchUnbandFailed, x, tmpProId) + re[x] + "</nobr><br>";
				}
			}
		}
		if(sucList.length > 0){
			msg += String.format(I18N.onuAutoUpg.tip.batchUnbandSuc, sucList.join("、"), tmpProId);
		}
		refreshThisWindow(msg, true);
	}
}
function unbandAPon(list, re, tmpProId){
	re.flag = true;
	var params = {entityId: entityId, profileId: tmpProId, slotNo: list[0].slotNo, ponNo: list[0].ponNo}
	Ext.Ajax.request({
		url : '/onu/unbandOnuAutoUpgProfile.tv?r=' + Math.random(),
		success : function(response) {
			if(response.responseText){
				re[list[0].slotNo + "/" + list[0].ponNo] = response.responseText;
			}else{
				re[list[0].slotNo + "/" + list[0].ponNo] = 'success';
			}
			list.splice(0, 1);
			unbandMgmt(list, re, tmpProId);
		},
		failure : function(response) {
			re[list[0].slotNo + "/" + list[0].ponNo] = response.responseText;
			list.splice(0, 1);
			unbandMgmt(list, re, tmpProId);
		},
		params: params
	});
}


function cancelClick(){
	window.parent.onuAutoUpgTmpData = null;
	window.parent.closeWindow("onuAutoUpg");
}
function refreshClick(){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.fetchConfirm, function(type) {
		if (type == 'no') {
			return;
		}
		window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.onuAutoUpg.mes.fetching);
		Ext.Ajax.request({
			url : '/onu/refreshOnuAutoUpg.tv?entityId=' + entityId + '&r=' + Math.random(),
			success : function(response) {
				refreshThisWindow(I18N.onuAutoUpg.mes.fetchSuc, true);
			},
			failure : function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAutoUpg.mes.fetchFailed);
			}
		});
	});
}
function recordClick(){
	showOnuAutoUpgRecord(0, 0);
}
function showOnuAutoUpgRecord(profileId, slotNo){
	window.top.createDialog('onuAutoUpgRecord', I18N.onuAutoUpg.he.onuUpgrading, 600, 480, '/onu/showOnuAutoUpgRecord.tv?entityId='
			+ entityId + '&profileId=' + profileId + '&slotNo=' + slotNo, null, true, true);
}
function refreshThisWindow(msg, sucFlag){
	var tmpProId = proId;
	var tmpPonId = ponId;
	window.parent.onuAutoUpgTmpData = {proId : proId, ponId : ponId};
	if(msg){
		if(sucFlag){
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">' + msg + '</b>'
       	    });
		}else{
			window.parent.showMessageDlg(I18N.COMMON.tip, msg);
		}
	}else{
		window.parent.closeWaitingDlg();
	}
	location.reload();
}

function authLoad(){
    if(!refreshDevicePower){
        $("#fetch").attr("disabled",true);
    }
}
</script>
<style type="text/css">
.leftSidePonGrid{ position: absolute; top:155px; left:10px;}
.fackTable{ margin:35px 0px 0px 310px;}
.ponSpanClass{width: 40;margin-top: 8;margin-left: 5;}
</style>
</head>
<body class="openWinBody" onload="authLoad()">
	<div style="position:absolute; top:15px; right:10px;">
		<a id="topAddBtn" onclick='addProfileBtClick()' href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@onuAutoUpg.co.create@</span></a>
	</div>
	<div class="edge10 pT20">
		<div id="proGridDiv" class="pB5"></div>	
		<div class="fackTable" style="background-color:#fff; height:210px; overflow:auto; position:relative;">
			
			<table id="upgGridTable" class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
				
			<tbody></tbody>
			</table>
		</div>
	</div>
	<div id="ponGridDiv" class="normalTable leftSidePonGrid"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	         <li><a onclick='refreshClick()' id="fetch" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a onclick='recordClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i>@onuAutoUpg.he.onuUpgrading@</span></a></li>
	         <li><a onclick='cancelClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	<table class="dataTable zebra" width="470px" style="position:absolute; top:159px; left:320px;" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
		<thead>
			<tr>
				<th class='wordBreak' width="77">@onuAutoUpg.pro.onuLocation@</th>
				<th class='wordBreak' width="77">@onuAutoUpg.pro.type@</th>
				<th class='wordBreak' width="77">@onuAutoUpg.pro.hwVersion@</th>
				<th class='wordBreak' width="160">@onuAutoUpg.he.newVersion@</th>
				<th class='wordBreak'>@onuAutoUpg.pro.onlineStat@</th>
			</tr>
		</thead>
	</table>
</body>
</Zeta:HTML>