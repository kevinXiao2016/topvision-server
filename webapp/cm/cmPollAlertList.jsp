<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
	plugin DateTimeField
	module cm
	import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript" src="/js/nm3k/Nm3kSelect.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.fault.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
// for tab changed start
var startTime;
var endTime;
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end
var userId = <%= uc.getUser().getUserId() %>;
var confirmAlertPower = <%= uc.hasPower("confirmAlert")%>;
var clearAlertPower = <%= uc.hasPower("clearAlert")%>;
var pageSize = <%= uc.getPageSize() %>;
var storeUrl = '/cmpoll/getCurrentCmPollAlertList.tv';

var store = null;
var grid = null;
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(grid.getEl().dom, wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
}
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<html>');
	doc.write('<head>');
	doc.write('<title></title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/report.css"/>');
	Zeta$NoHeaderAndFooter(doc);
	doc.write('</head>');
	doc.write('<body style="margin:50px;"><center>');
	doc.write(obj.innerHTML);
	doc.write('</center></body>');
	doc.write('</html>');
	doc.close();
}
function doRefresh() {
	store.reload();
}
function onRefreshClick() {
	var level = $('#alertLevel').val();
	var typeId = $('#alertType').val();
	var hostDevice = $('#hostDevice').val();
	var sTime = Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s');
	var eTime =  Ext.util.Format.date(endTime.getValue(), 'Y-m-d H:i:s');
	store.load({params: {level:level,typeId:typeId,hostDevice:hostDevice,startTime:sTime,endTime:eTime,start: 0, limit: pageSize}});
}
function renderLevelName(value, p, record) {
	return record.data.levelName;
}
function renderLevel(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{1}">{2}',
		value, record.data.levelName, record.data.message);
}
function renderNote(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{1}">{2}',
		record.data.level, value, value);
}
function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"../images/fault/confirm.png\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{0}">', I18N.FAULT.confirmed);
	} else {
		return String.format('<img hspace=5 src=\"../images/fault/unconfirm.png\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{0}">', I18N.FAULT.unconfirmed);	
	}
}
function getLastAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false);
		} else {
			model.selectLastRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}
function getNextAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasNext()) {
			model.selectNext(false);
		} else {
			model.selectFirstRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}
function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.COMMON.clear, I18N.EVENT.clearMessage, function (type, text) {
			if (type == "cancel") {
				return;
			}
			if(text.length > 255){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
				return;
			}
			var selections = sm.getSelections();
			var alertIds = [];
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId;
			}
			Ext.Ajax.request({url:"../fault/clearAlert.tv", method: "post",
			success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message: text}});
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.FAULT.pleaseSelectAlert);
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	/* for (var i = 0; i < selections.length; i++) {
		 if(selections[i].data.status == 1){
			 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.alertIsConfirmed);
			 return;
		 }
	} */
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 1){
				 window.parent.showMessageDlg("@COMMON.tip@", "@fault/ALERT.alertConfirmed@");
				 return;
			}
		}
		window.parent.showTextAreaDlg(I18N.COMMON.confirm, I18N.EVENT.confirmMessage, function (type, text) {
		if (type == "cancel") {
			return;
		}
		if(text.length > 255){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
			return;
		}
		var selections = sm.getSelections();
		var len = selections.length;
		var alertIds = [];
		for (var i = 0; i < len; i++) {
			alertIds[i] = selections[i].data.alertId;
		}
		Ext.Ajax.request({url:"../fault/confirmAlert.tv", method: "post",
			success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message: text}});
	});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.FAULT.pleaseSelectAlert);
	}
}
var eventContextMenu = null;
var eventFindDesktopItem = null;
var eventMoreItem = null;
var eventConfirmItem = null;
var eventClearItem = null;
var eventPropertyItem = null;
var queryVisible = false;
function buildEventContextMenu() {
	eventConfirmItem = new Ext.menu.Item({text:I18N.EVENT.confirm, handler: onConfirmAlarmClick});
	eventClearItem = new Ext.menu.Item({text:I18N.EVENT.clear, handler: onClearAlarmClick});
	eventPropertyItem = new Ext.menu.Item({text:I18N.COMMON.property, handler: onPropertyEventClick});
	eventContextMenu = new Ext.menu.Menu({id:"eventContextMenu", 
		enableScrolling: enableMenuScrolling, minWidth: 160, items:[
		eventConfirmItem, eventClearItem, "-", 
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick},
		'-', eventPropertyItem]});
}

function onPropertyEventClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		var r = sm.getSelected();
		window.top.createDialog("modalDlg", I18N.FAULT.alertProperty, 540, 400, "alert/showAlertDetail.tv?alertId=" + r.data.alertId, null, true, true);	
	}
}
function onShowQueryClick() {
	queryVisible = !queryVisible;
	Zeta$('query-div').style.display = queryVisible ? '' : 'none';
}

function buildPageBox(page) {
	return String.format(I18N.COMMON.displayPerPage, page);
}

var curFlag = true;
function alertViewerChanged(obj) {
	var t = window.parent.contentPanel.getActiveTab();
	t.alertType = 'his';
	var url = '';
	var curLevel = $('#alertLevel').val();
	var curType = $('#alertType').val();
	if (obj.selectedIndex == 0) {
		url = '/cmpoll/getCurrentCmPollAlertList.tv';
		if (curFlag) {
			url = '/cmpoll/getCurrentCmPollAlertList.tv?' + curLevel;
		} else {
			url = '/cmpoll/getCurrentCmPollAlertList.tv?typeId=' + curType;
		}		
	} else {
		url = '/cmpoll/showCmPollHistoryAlert.tv';
		if (curFlag) {
			url = '/cmpoll/showCmPollHistoryAlert.tv?level=' + curLevel;
		} else {
			url = '/cmpoll/showCmPollHistoryAlert.tv?typeId=' + curType;
		}		
	}
	location.href = url;
}
function showAlertDetail(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selection = sm.getSelected();
		window.parent.createDialog("modalDlg", I18N.FAULT.alertProperty, 800, 500,
				"alert/showAlertDetail.tv?alertId=" + selection.data.alertId, null, true, true);
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.FAULT.pleaseSelectAlert);
	}
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:["-",  buildPageBox(pageSize),'-', 
		{text: I18N.COMMON.print, iconCls:'bmenu_print', handler: onPrintClick}, '-',
		{text: I18N.FAULT.alertProperty, iconCls:'bmenu_arrange', handler: showAlertDetail}, '-'
	]});
	return pagingToolbar;
}



Ext.onReady(function () {
	Ext.QuickTips.init()
	
	Ext.BLANK_IMAGE_URL = "../images/s.gif";
	//加载告警类型
	initData();
	store = new Ext.data.JsonStore({
	    url: storeUrl,
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    listeners:{
	    	'load':function(){
	    		grid.getStore().each(function(record){
	 					return true;
	 			});
	    		setTimeout(function(){
	    			 try {
	    				//得到表格的EL对象
						var gridEl = this.grid.getEl();
						if (gridEl != null) {
							//得到表格头部的全选CheckBox框
							var hd = gridEl.select('div.x-grid3-hd-checker');
							hd.removeClass('x-grid3-hd-checker-on');
						}
					} catch (ex) {
					}
				}, 100);
	    	}
	    },
	    fields: ['alertId', 'typeId', 'typeName', 'host', 'source', 'level', 'levelName', 'status',
	    	'confirmUser', 'message', 'firstTime','lastTime', 'confirmTime',"oid","entityType","entityId"]
	});
	
	var lastMonth = new Date();
	var current = new Date();
	lastMonth.setTime(lastMonth.getTime()-(30*24*60*60*1000));
	var minDate = new Date();
	minDate.setTime(0); 
	startTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : lastMonth,
		editable: false,
	    minValue:minDate,
	    renderTo: 'startTime',
	    maxValue:current,
	    listeners: {
			"select": function(){
				endTime.setMinValue(startTime.getValue());
				if(startTime.getValue() > endTime.getValue()){
					startTime.setValue(endTime.getValue());
				}
			}
		}
	});
	endTime = new Ext.ux.form.DateTimeField({
		width:160,
		value : current,
		editable: false,
	    minValue:lastMonth,
	    renderTo: 'endTime',
	    listeners: {
			"select": function(){
				startTime.setMaxValue(endTime.getValue());
			}
		}
	});  
	
	nm3kPickData({
	    startTime : startTime,
	    endTime : endTime,
	    searchFunction : searchClick
	})
	
	var cm = new Ext.grid.ColumnModel(columnModels);
	
	var w = document.body.clientWidth - 30;
	var h = $(window).height() -90;

	grid = new Ext.grid.GridPanel({store: store, animCollapse: animCollapse,
		title: I18N.ALERT.currentAlertInfoList,
		border: false,
		margins:'0px 10px 0px 10px',
		region: 'center',
		cls:'normalTable',
		trackMouseOver: trackMouseOver, 
		cm: cm,
		sm: sm,
	 	bbar: buildPagingToolBar(),
	 	listeners: {
	            'render': function(grid) {
	            }
	 	},
	 	viewConfig:{
	 		forceFit:true
	 	}
 	});
	if(!confirmAlertPower){
    Ext.getCmp('confirm').hide();
    }
    if(!clearAlertPower){
    Ext.getCmp('clear').hide();
    }

	/* grid.on("rowcontextmenu", function (grid, rowIndex, e) {
		e.preventDefault();
		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		if (eventContextMenu == null) {
			buildEventContextMenu();
		}
		var r = sm.getSelected();
		if (r.data.typeId > 500 && r.data.typeId < 600) {
		} else {
		}		
		if(confirmAlertPower){
			eventConfirmItem.enable();
		}else{
			eventConfirmItem.disable(); 
		}
		if(clearAlertPower){
			eventClearItem.enable();
		}else{
			eventClearItem.disable(); 
		}
		eventPropertyItem.enable();
		eventContextMenu.showAt(e.getPoint());
	}); */
	
	store.setBaseParam('startTime', Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s'));	
	store.setBaseParam('endTime', Ext.util.Format.date(endTime.getValue(), 'Y-m-d H:i:s'));	
	store.load({params: {start: 0, limit: pageSize}});
	
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [grid,
	            {region: 'north',
	    			contentEl:'topPart', 
	    			height:90,
	    			border:false
	    		}]
	});
});

var sm = new Ext.grid.CheckboxSelectionModel(); 
var columnModels = [sm,
	{header: I18N.EVENT.levelHeader, width: 20, dataIndex: 'level', hidden: true, renderer: renderLevelName},	
	{header: '<div class="txtCenter">'+ I18N.COMMON.description+ '</div>', width: 260, groupable:false, align:'left',dataIndex: 'level',  renderer: renderLevel},
	{header: '<div class="txtCenter">'+ I18N.ALERT.alertType+ '</div>', width: 120,align:'center', dataIndex: 'typeName'},	
	{header: '<div class="txtCenter">'+ I18N.EVENT.sourceHeader+ '</div>', width: 100, align:'center', dataIndex: 'host'},
	{header: I18N.EVENT.timeHeader, width: 140, dataIndex: 'firstTime'},
	{header: I18N.EVENT.lastTime, width: 140, dataIndex: 'lastTime'},
	{header: I18N.FAULT.checkStatus, width: 80, dataIndex: 'status', renderer: renderStatus},	
	{header: I18N.FAULT.checkCustomer, width: 80, dataIndex: 'confirmUser'},
	{header: I18N.FAULT.checkTime, width: 140, dataIndex: 'confirmTime'}
];
Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
Ext.override(Ext.grid.GridView,{  
    
    onRowSelect : function(row){  
        this.addRowClass(row, "x-grid3-row-selected");  
        var selected = 0;  
        var len = this.grid.store.getCount();  
        for(var i = 0; i < len; i++){  
            var r = this.getRow(i);  
            if(r){  
               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
            }  
        }  
          
        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
          
        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
             hd.addClass('x-grid3-hd-checker-on');   
        }  
    },  
  
    onRowDeselect : function(row){  
        this.removeRowClass(row, "x-grid3-row-selected");  
            var selected = 0;  
            var len = this.grid.store.getCount();  
            for(var i = 0; i < len; i++){  
                var r = this.getRow(i);  
                if(r){  
                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
                }  
            }  
            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
              
            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
                 hd.removeClass('x-grid3-hd-checker-on');   
            }  
    }  
});  
function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}

//初始化告警类型
var	ALERT_TYPE_OPTION_FMT = '<option value="{0}" {2}>{1}</option>';
var	NOT_SELECTED = "";
var	SELECTED = " selected";

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String st = sdf.format(new Date());
%>

function GetDateStr(AddDayCount) {  
    var dd = new Date()
    dd.setDate(dd.getDate() + AddDayCount)
    var r = []
    r.push(dd.getFullYear())
    r.push('-')
    r.push(dd.getMonth() + 1)
    r.push('-')
    r.push(dd.getDate())
    r.push(' ')
    r.push(dd.getHours() < 10 ? '0' + dd.getHours() : dd.getHours())
    r.push(':')
    r.push(dd.getMinutes() < 10 ? '0' + dd.getMinutes() : dd.getMinutes())
    r.push(':')
    r.push(dd.getSeconds() < 10 ? '0' + dd.getSeconds() : dd.getSeconds())
    return r.join('')
}

//开始时间和结束时间

function initData() {
	//初始化查询条件
	Ext.Ajax.request({
		url: '/cmpoll/getCmPollAlertType.tv?r=' + Math.random(),
		success: function(response) {
			var type = Ext.decode(response.responseText)
			// 告警类型			
			var arrData = [{text:I18N.ALERT.all, value:0}];
			for (var i = 0, len = type.length; i < len; i++) {
				var o = {};
				o.text = type[i].displayName;
				o.value = type[i].typeId;
				arrData.push(o);
			}
			
			var s = new Nm3kSelect({
		         id : "alertType",
		         renderTo : "putSel",
		         width : 158,
		         subWidth : 400,
		         firstSelect : 0,
		         rowNum : 2,
		         dataArr : arrData
		    })
		    s.init();


		}
	});
	
	//开始时间和结束时间
	$('#startTime').val(startTime);
	$('#endTime').val(endTime);
}

function searchClick(){
	store.setBaseParam('level', $('#alertLevel').val());
	store.setBaseParam('typeId', $('#alertType').val());
	store.setBaseParam('hostDevice', $('#hostDevice').val());
	store.setBaseParam('startTime', Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s'));	
	store.setBaseParam('endTime', Ext.util.Format.date(endTime.getValue(), 'Y-m-d H:i:s'));	
	store.setBaseParam('start', 0);	
	store.setBaseParam('limit', pageSize);
	store.load({
		callback: function(records) {
			if (records && records.length == 0) {
				top.afterSaveOrDelete({
					title:I18N.COMMON.tip,
					html:'<b class="orangeTxt">'+ I18N.ALERT.SearchNoAlert + "!</b>"
				})
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.SearchNoAlert+"!");
			}
		}
	});
}

</script>
<%
boolean confirmAlertPower = uc.hasPower("confirmAlert");
boolean clearAlertPower = uc.hasPower("clearAlert");
%>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10">
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt w70">@fault/ALERT.alertLevel@:</td>
				<td><select id="alertLevel" class="normalSel w160">
						<option value="0">@fault/ALERT.all@</option>
						<option value="6">@fault/WorkBench.emergencyAlarm@</option>
						<option value="5">@fault/WorkBench.seriousAlarm@</option>
						<option value="4">@fault/WorkBench.mainAlarm@</option>
						<option value="3">@fault/WorkBench.minorAlarm@</option>
						<option value="2">@fault/WorkBench.generalAlarm@</option>
						<option value="1">@fault/WorkBench.message@</option>
				</select>
				</td>
				<td class="rightBlueTxt w70">@fault/ALERT.alertType@:</td>
				<td id="putSel">
					<%-- <select id="alertType" style="width: 200px;" class="normalSel"></select>  --%>
				</td>
				
				<td class="rightBlueTxt w70">@resources/MAIN.relaDevice@:</td>
				<td width="160">
					<input type="text" id="hostDevice" name="hostDevice" value="${hostDevice}" class="normalInput w130" />
				</td>
				<td rowspan="2">    
					<ol class="upChannelListOl pB0">
							<li>
								<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
							</li>
							<li>
								<a id="btn2" href="javascript:;" class="normalBtn " onclick="onConfirmAlarmClick()"><span><i class="miniIcoCorrect"></i>@COMMON.confirm@</span></a>
							</li>
							<li>
								<a id="btn3" href="javascript:;" class="normalBtn " onclick="onClearAlarmClick()"><span><i class="miniIcoClose"></i>@COMMON.clear@</span></a>
							</li>
						</ol>
				</td>
			</tr>
			<tr>
					<td class="rightBlueTxt">
						@fault/ALERT.beginTime@:
					</td>
					<td>
						<div id="startTime" class="w160"></div>
						</td>
					<td class="rightBlueTxt">
						@fault/ALERT.endTime@:
					</td>
					<td>
						<div id="endTime" class="w160"></div>
						</td>
					<td class="rightBlueTxt">
						@fault/ALERT.currentORhistory@:
					</td>
					<td>
						<select class="normalSel w132" onchange="alertViewerChanged(this);">
						<option selected >@fault/currentAlert@</option>
						<option>@fault/historyAlert@</option>
						</select>
					</td>
			</tr>
		</table>
	</div>
	
	
	<dl class="legent r10" style="position:absolute; z-index:2; right:10px; top:90px;">
		<dt class="mR5">@fault/WorkBench.alertLegend@:</dt>
			<dd class="mR2"><img src="/images/fault/level6.png" border="0" alt="" /></dd>
			<dd class="mR10">@fault/WorkBench.emergencyAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level5.png" border="0" alt="" /></dd>
			<dd class="mR10">@fault/WorkBench.seriousAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level4.png" border="0" alt="" /></dd>
			<dd class="mR10">@fault/WorkBench.mainAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level3.png" border="0" alt="" /></dd>
			<dd class="mR10">@fault/WorkBench.minorAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level2.png" border="0" alt="" /></dd>
			<dd class="mR10">@fault/WorkBench.generalAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level1.png" border="0" alt="" /></dd>
			<dd>@fault/WorkBench.message@</dd>
	</dl>
</body>
</Zeta:HTML>
