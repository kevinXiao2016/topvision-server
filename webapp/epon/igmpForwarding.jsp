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
var entityId = ${entityId}
var forwardingType = 0;
var titleStr = [I18N.IGMP.slotLoc , I18N.IGMP.ponLoc , I18N.IGMP.onuLoc, I18N.IGMP.uniLoc]
var totleStr = [I18N.IGMP.boardCount , I18N.IGMP.portCount , I18N.IGMP.onuCount, I18N.IGMP.uniCount]
var slotList = ${slotList};
var ponList = ${ponList};

var proxy = ${proxyListObject};
proxy = proxy.join("")=="false" ? new Array() : proxy;
var proxyData = new Array();
var proxyStore;
var proxyGrid;
var proxyList = new Array();
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var data = new Array();
var store;
var grid;
var uniData = new Array();
var uniStore;
var uniGrid;

Ext.Ajax.timeout = 2000000;

function loadData(){
	var sel = proxyGrid.getSelectionModel().getSelected();
	if(sel){
		var url = '/epon/igmp/getIgmpForwarding.tv';
		var params;
		var proxyId = sel.data.proxyId;
		params = {forwardingType : forwardingType, entityId : entityId, proxyId : proxyId};
		if(forwardingType > 0){
			params.slotNo = $("#slotSel").find("option:selected").text();
			if(forwardingType > 1){
				params.portNo = $("#ponSel").find("option:selected").text();
			}
		}
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				var demoData = response.responseText.replace("[", "").replace("]", "").split(",");
				$("#totleNum").text(demoData.shift());
				if(forwardingType == 3){
					uniData = new Array();
					var tmpL = demoData.length;
					for(var k=0; 5*k<tmpL; k++){
						uniData[k] = new Array();
						uniData[k][0] = parseInt(demoData[5*k].replace(/[^/d]/,""));
						uniData[k][1] = demoData[5*k+1]+"#"+demoData[5*k+2]+"#"+demoData[5*k+3]+"#"+demoData[5*k+4];
					}
					uniStore.loadData(uniData);
				}else{
					if(forwardingType > 0){
						var slotNo = $("#slotSel").find("option:selected").text();
						var ponNo = $("#ponSel").find("option:selected").text();
						for(var t=0; t<demoData.length; t++){
							if(forwardingType > 1){
								demoData[t] = ponNo + ":" + demoData[t];
							}
							demoData[t] = slotNo + "/" + demoData[t];
						}
					}
					data = new Array();
					var tmpL = parseInt(demoData.length / 4);
					if(tmpL != 0){
						for(var i=0; i<tmpL; i++){
							data[i] = new Array();
							for(var x=0; x<4; x++){
								data[i][x] = demoData[4*i + x];
							}
						}
					}
					var tmpl = demoData.length % 4;
					if(tmpl != 0){
						data[tmpL] = new Array();
						for(var y=0; y<tmpl; y++){
							data[tmpL][y] = demoData[parseInt(4*tmpL + y)];
						}
						for(var y=tmpl; y<4; y++){
							data[tmpL][y] = '';
						}
					}
					store.loadData(data);
				}
				var text = I18N.IGMP.mvlan + sel.data.proxyId;
				if(forwardingType > 0){
					text = text + I18N.IGMP.onBoard  + $("#slotSel").find("option:selected").text();
				}
				if(forwardingType > 1){
					text = text + I18N.IGMP.dePon + $("#ponSel").find("option:selected").text();
				}
				text = text + I18N.IGMP.de
				$("#totleNumLoc").text(text);
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchErrorRefresh)
			},
			params : params
		});
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.plsSelectMvlan)
	}
}
function loadGrid(){
	store = new Ext.data.SimpleStore({
		data : data,
		fields : ['loc0', 'loc1', 'loc2', 'loc3']
	});
	grid = new Ext.grid.GridPanel({
		stripeRows:true,cls:"normalTable edge10",bodyCssClass: 'normalTable',
		id : 'gridXX',
		renderTo : 'gridDiv',
		title : "&nbsp;&nbsp;" + titleStr[forwardingType],
		store : store,
		width : 605,
		height : 430,
		frame : false,
		autoScroll : true,
		border : false,
		hideHeaders : true,
		selModel : new Ext.grid.CellSelectionModel({
			singleSelect : true
		}),
		columns: [{header : I18N.COMMON.location , dataIndex : 'loc0', align : 'center', width : 145, sortable : true}
				 ,{header : I18N.COMMON.location , dataIndex : 'loc1', align : 'center', width : 145, sortable : true}
				 ,{header : I18N.COMMON.location , dataIndex : 'loc2', align : 'center', width : 145, sortable : true}
				 ,{header : I18N.COMMON.location , dataIndex : 'loc3', align : 'center', width : 145, sortable : true}
			  	 ]
	});
}

function loadProxyData(){
	var tmpI = 0;
	for(var x=0; x<2001; x++){
		if(proxy[x] != ""){
			proxyData[tmpI]= new Array();
			proxyData[tmpI][0] = x;
			proxyData[tmpI][1] = proxy[x].split("#")[0];
			proxyData[tmpI][2] = proxy[x].split("#")[1];
			proxyList[tmpI] = x;
			tmpI++;
		}
	}
	proxyData.sort(function(a, b){
		return a[0] - b[0];
	});
	proxyList.sort(function(a, b){
		return a - b;
	});
}
function loadProxyGrid(){
	proxyStore = new Ext.data.SimpleStore({
		data : proxyData,
		fields : ['proxyId', 'proxyIp', 'proxyName']
	});
	proxyGrid = new Ext.grid.GridPanel({
		stripeRows:true,cls:"normalTable edge10",bodyCssClass: 'normalTable',
		id : 'proxyGrid',
		renderTo : 'ProxyListGrid',
		title : I18N.IGMP.mvlanList,
		store : proxyStore,
		viewConfig:{forceFit: true},
		width : 200,
		height : 465,
		frame : false,
		autoScroll : true,
		border : true,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				'selectionchange' : proxyChange
			}
		}),
		columns: [{
				header: I18N.IGMP.mvlanId,
				dataIndex: 'proxyId',
				align: 'center',
				width: 75
			},{
				header: I18N.IGMP.mvlanIp,
				dataIndex: 'proxyIp',
				align: 'center',
				width: 100
			},{
				header: I18N.IGMP.mvlanName,
				dataIndex: 'proxyName',
				align: 'center',
				hidden : true,
				width: 120
		}],
		listeners : {
			'viewready' : function(){
				if(proxyData.length > 0){
					proxyGrid.getSelectionModel().selectRow(0, true);
				}
			},
			'rowcontextmenu': function(grid,rowIndex,e){
				proxyGridContextMenu(grid,rowIndex,e);
			}
		},
		bbar: new Ext.Toolbar({
			scope : this,   
	 	    items :[
				{
					text : I18N.IGMP.mvlanId + ":",
					tooltip: I18N.IGMP.mvlanId
				},{
		 	        xtype:"textfield", 
		 	        id:"proxySearch",
		            width:60,
		            enableKeyEvents:true,
		         	listeners : {
		            	'keyup' : function(){
		            		var tempId = $("#proxySearch").val();
		            		if(tempId.length > 4){
		            			tempId = tempId.substring(0,4);
		            			$("#proxySearch").val(tempId);
			            	}
		            		$("button.x-btn-text","#proxySearchBt").attr("disabled",false);
		            		if(tempId!=null && tempId!="" && tempId!=undefined){
			            		var reg = /^([1-9][0-9]{0,3})+$/ig;
			            		if(!reg.exec(tempId) || parseInt(tempId)>2000){
			            			$("button.x-btn-text","#proxySearchBt").attr("disabled",true);
									return;
				            	}
			            	}
			            	$("button.x-btn-text","#proxySearchBt").text(I18N.COMMON.show)
			            	if(proxyList.indexOf(tempId) > -1){
			            		$("button.x-btn-text","#proxySearchBt").text(I18N.COMMON.selected)
				            }
			       		}
		       		}
	 	        },'-',{
	 	        xtype:"button",
	 	        text :I18N.COMMON.selected, 
				id : "proxySearchBt",
	 	 	    width : 70,
	 	 	   	listeners : {
	 	          	'click' : function(){
		 	          	var tempId = $("#proxySearch").val();
		 	          	if(tempId==null || tempId=="" || tempId==undefined){
		 	          		proxyStore.loadData(proxyData);
		 	          		if(proxyData.length > 0){
			 	          		proxyGrid.getSelectionModel().selectRow(0, true);
			 	          	}
			 	        }
		 	          	var reg = /^([1-9][0-9]{0,3})+$/ig;
	            		if(!reg.exec(tempId) || parseInt(tempId)>2000){
							return;
		            	}
		            	var tmpS = $("button.x-btn-text","#proxySearchBt").text();
		            	if(tmpS == I18N.COMMON.show){
		            		proxyStore.filterBy(function(record){
			 					return ("" + record.data.proxyId).indexOf("" + tempId) > -1;
			 				});
			            }else if(tmpS == I18N.COMMON.selected){
			            	proxyStore.loadData(proxyData);
			            	proxyGrid.getSelectionModel().selectRow(proxyList.indexOf(tempId), true);
			            	proxyGrid.getView().focusRow(proxyList.indexOf(tempId), false);
				        }
	 	       		}
	 	       }
	 		}]
	 	})
	});
}
function proxyChange(){
	var sel = proxyGrid.getSelectionModel().getSelected();
	if(proxyStore.indexOf(sel) == -1){
		$("#proxyIdText").text(I18N.IGMP.mvlan)
		$("#totleNum").text("");
		if(forwardingType == 3){
			uniData = new Array();
			uniStore.loadData(uniData);
		}else{
			data = new Array();
			store.loadData(data);
		}
	}else{
		$("#proxyIdText").text(I18N.IGMP.mvlan + sel.data.proxyId);
		if(forwardingType == 0){
			loadData();
			return false;
		}
	}
}
function loadUniGrid(){
	uniStore = new Ext.data.SimpleStore({
		data : uniData,
		fields : ['onuNo','uniNoList']
	});
	uniGrid = new Ext.grid.GridPanel({
		id : 'uniGrid',
		renderTo : 'gridDiv',
		title : "&nbsp;&nbsp;" + titleStr[forwardingType],
		store : uniStore,
		width : 605,
		height : 360,
		frame : false,
		autoScroll : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: "ONU",
				dataIndex: 'onuNo',
				align: 'center',
				sortable : true,
				width: 85,
				renderer : function(value, cellmate, record){
					return $("#slotSel").find("option:selected").text() + " / " + 
							$("#ponSel").find("option:selected").text() + " : " + value;
				}
			},{
				header: I18N.IGMP.uni,
				dataIndex: 'uniNoList',
				align: 'center',
				width: 500,
				renderer : function(value, cellmeta, record) {
					return uniGridRender(value);
				}
		}]
	});
}
function uniGridRender(v){
	var tmpList = v.split("#");
	tmpList = changeUniData(tmpList);
	var r = "";
	var tmpL = tmpList.length;
	if(tmpL > 0 && tmpL < 9){
		tmpList.sort(function(a, b){
			return parseInt(a) - parseInt(b);
		});
		for(var x=0; x<tmpL; x++){
			r = addUpPortImg(r);
			r += "<span style='width:40px;text-align:left;'>" + tmpList[x] + "</span>";
		}
		if(tmpL < 7){
			for(var x=tmpL; x<8; x++){
				r += "<span style='width:56px;'></span>";
			}
		}
	}else if(tmpL > 8 && tmpL < 33){
		for(var t=0; t<4; t++){
			for(var k=0; k<8; k++){
				if(tmpList.indexOf(8*t + k) > -1){
					r = addUpPortImg(r);
				}else{
					r = addDownPortImg(r);
				}
				r += "<span style='width:40px;text-align:left;'>" + (8*t + k + 1) + "</span>";
			}
			r += "<br>";
		}
	}
	return r;
}
function addUpPortImg(r){
	r += "<img src='/images/network/port/up.png' title=@IGMP.portInMvlan@ />";
	return r;
}
function addDownPortImg(r){
	r += "<img src='/images/network/port/down.png' title=@IGMP.portNotInMvlan@ />";
	return r;
}
function changeUniData(l){
	var tmpl = new Array();
	for(var x=0; x<l.length; x++){
		var k = changeData(l[x]);
		if(k.length > 0){
			for(var y=0; y<k.length; y++){
				tmpl.push(33 - k[y] - 8*x);
			}
		}
	}
	return tmpl;
}
function changeData(n){
	var l = new Array();
	for(var x=1; x<9; x++){
		var xx = tooMi(8 - x);
		if(n > xx || n == xx){
			l.push(x);
			n = n - xx;
		}
	}
	return l;
}
function tooMi(x){
	var a = 1;
	if(x > 0){
		for(var k=0; k<x; k++){
			a = a * 2;
		}
	}
	return a;
}

Ext.onReady(function(){
	loadProxyData();
	loadProxyGrid();
	initSlotSel();
	typeChange();
	$("#totleNumText").text(totleStr[forwardingType]);
	$("#proxySearch").focus();
});
function initSlotSel(){
	var position = Zeta$("slotSel");
    position.options.length = 0;
    for (var i = 0; i < slotList.length; i++) {
        if(slotList[i] != 0){
	        var option = document.createElement('option');
	        option.value = slotList[i];
	        option.text = i;
	        try {
	            position.add(option, null);
	        } catch(ex) {
	            position.add(option);
	        }
        }
    }
    if(position.options.length == 0){
    	var option = document.createElement('option');
        option.value = -1;
        option.text = I18N.IGMP.noBoard
        try {
            position.add(option, null);
        } catch(ex) {
            position.add(option);
        }
    }
	slotChange();
}
function slotChange(){
	var tmpV = $("#slotSel").val();
	var tmpI = slotList.indexOf(tmpV);
	if(tmpV != -1){
		var position = Zeta$("ponSel");
	    position.options.length = 0;
	    for (var i = 0; i < ponList[tmpI].length; i++) {
	        if(ponList[tmpI][i] != 0){
		        var option = document.createElement('option');
		        option.value = ponList[tmpI][i];
		        option.text = i;
		        try {
		            position.add(option, null);
		        } catch(ex) {
		            position.add(option);
		        }
	        }
	    }
	}
	if(tmpV == -1 || Zeta$("ponSel").options.length == 0){
		var option = document.createElement('option');
	    option.value = -1;
	    option.text = I18N.IGMP.noPon
	    try {
	        position.add(option, null);
	    } catch(ex) {
	        position.add(option);
	    }
	}
}

function typeChange(){
	forwardingType = parseInt($("#typeSel").val());
	$("#tempTr").height(0);
	$("#searchBt").hide();
	$("#xian").hide();
	$("#slotText").hide();
	$("#slotSel").hide();
	$("#ponText").hide();
	$("#ponSel").hide();
	if(forwardingType > 1){
		$("#ponText").show();
		$("#ponSel").show();
	}
	if(forwardingType > 0){
		$("#tempTr").height(40);
		$("#xian").show();
		$("#slotText").show();
		$("#slotSel").show();
		$("#searchBt").show();
	}
	$("#totleNumText").text(totleStr[forwardingType]);
	$("#gridDiv").empty();
	if(forwardingType > 2){
		uniData = new Array();
		loadUniGrid();
	}else{
		data = new Array();
		if(forwardingType == 0 && proxyGrid.getSelectionModel().getSelected()){
			loadData();
		}
		loadGrid();
	}
}
function proxyGridContextMenu(grid,rowIndex,e){
	grid.selModel.selectRow(rowIndex,true);
	proxyMenu.showAt(e.getXY());
}
var proxyMenu = new Ext.menu.Menu({
	id:'proxyMenu',
	enableScrolling: false,
	items:[{
	    id:'igmpProxyText',
	    text: I18N.IGMP.viewMvlanInfo ,
	    handler : showIgmpProxy
	}]
});
function showIgmpProxy(){
	window.parent.createDialog("igmpProxy", I18N.IGMP.mvlanMgmt , 500, 400, "/epon/igmp/showIgmpProxy.tv?entityId="+entityId , null, true, true);
	cancelClick();
}

function cancelClick(){
	window.parent.closeWindow('igmpForwarding');
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching , 'ext-mb-waiting')
	var url = 'epon/igmp/refreshIgmpForwarding.tv'
	var params = {entityId : entityId}
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchErrorRefresh)
				return false
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk)
			if(proxyGrid.getSelectionModel().getSelected()){
				loadData();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchErrorRefresh)
		},
		params : params
	});
}
</script>

</head>
<body class="openWinBody">
	<table cellspacing=10 cellpadding=10>
	    <tr><td><select id=typeSel class="normalSel w150" onchange=typeChange()>
				<option value=0>@IGMP.viewBoardInfo@</option>
				<option value=1>@IGMP.viewPonInfo@</option>
				<option value="${forwardingType}">@IGMP.viewOnuInfo@</option>
			</select></td></tr>
		<tr>
			<td>
				<div id=ProxyListGrid></div>
			</td>
			<td>
				<div style="height: 430px; width: 610px">
					<table align=left>
						<tr id=tempTr height=40><td align=right>
							<span id=slotText style="width:80px;">@IGMP.boardId@:</span>
						</td><td>
							<select id=slotSel onchange='slotChange()' style="width:70px;display:none;">
							</select>
						</td><td align=right>
							<span id=ponText style="width:80px;">@IGMP.ponId@:</span>
						</td><td>
							<select id=ponSel style="width:70px;display:none;">
							</select>
						</td><td align=left width=500 style="padding-left:20px;">
							<button id=searchBt class=BUTTON95 type="button" onclick="loadData()">
								@COMMON.query@
							</button>
						</td></tr>
						<tr height=5px>
							<td colspan=5><hr id=xian size=1 style="color:1973b4;width:600px;">
							</td>
						</tr>
						<tr height=25>
							<td colspan=5><span id=totleNumLoc style="margin-left:10px;"></span><span id=totleNumText></span><span id=totleNum></span>
							</td>
						</tr>
						<tr>
							<td colspan=5>
								<div id='gridDiv'></div>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>