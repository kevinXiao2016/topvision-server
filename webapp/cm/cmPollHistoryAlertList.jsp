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
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
<%
boolean confirmAlertPower = uc.hasPower("confirmAlert");
boolean clearAlertPower = uc.hasPower("clearAlert");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String st = sdf.format(new Date());
%>
var confirmAlertPower = <%=uc.hasPower("confirmAlert")%>;
var clearAlertPower = <%=uc.hasPower("clearAlert")%>;
var pageSize = <%= uc.getPageSize() %>;
var userId = <%= uc.getUser().getUserId() %>;
var startTime;
var endTime;
function onRefreshClick() {
	store.reload();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
	grid.setWidth(w);
	grid.setHeight(h);
}
function buildPageBox(page) {
	return String.format(I18N.COMMON.displayPerPage, page);
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
}
var curFlag = true;
function alertViewerChanged(obj) {
	var t = window.parent.contentPanel.getActiveTab();
	t.alertType = 'his';
	var url = '';
	var curLevel = $('#alertLevel').val();
	var curType = $('#alertType').val();
	if (obj.selectedIndex == 0) {
		url = '/cmpoll/showCmPollAlertList.tv';
		if (curFlag) {
			url = '/cmpoll/showCmPollAlertList.tv?' + curLevel;
		} else {
			url = '/cmpoll/showCmPollAlertList.tv?typeId=' + curType;
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
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:['-', buildPageBox(pageSize)
	]});
	return pagingToolbar;
}

function renderNote(value, p, record) {
	return String.format('<img src="/images/fault/level{0}.gif" alt="{1}" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.levelId, record.data.levelName, record.data.message);
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
function searchAlarmClick() {
	store.setBaseParam('level', $('#alertSeverity').val());
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
function showHistoryAlertDetail(alertId){
	window.parent.createDialog("alertDlg", I18N.CMC.title.alertProperty, 600, 390, "alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
}
function GetDateStr(AddDayCount) {  
    var dd = new Date();
    dd.setDate(dd.getDate() + AddDayCount);
    var r = [];
    r.push(dd.getFullYear());
    r.push('-');
    r.push(dd.getMonth() + 1);
    r.push('-');
    r.push(dd.getDate());
    r.push(' ');
    r.push(dd.getHours() < 10 ? '0' + dd.getHours() : dd.getHours());
    r.push(':');
    r.push(dd.getMinutes() < 10 ? '0' + dd.getMinutes() : dd.getMinutes());
    r.push(':');
    r.push(dd.getSeconds() < 10 ? '0' + dd.getSeconds() : dd.getSeconds());
    return r.join('');
}



var	ALERT_TYPE_OPTION_FMT = '<option value="{0}">{1}</option>';
var	NOT_SELECTED = "";
var	SELECTED = " selected";
function initData() {
	//初始化查询条件
	Ext.Ajax.request({
		url: '/cmpoll/getCmPollAlertType.tv?r=' + Math.random(),
		success: function(response) {
			var type = Ext.decode(response.responseText);
			var arrData = [{text:I18N.ALERT.all, value:0}];
			// 告警类型
			//$('#alertType').append(String.format(ALERT_TYPE_OPTION_FMT, 'all',  I18N.ALERT.all , NOT_SELECTED)); 
			for (var i = 0, len = type.length; i < len; i++) {
				arrData.push({text:type[i].displayName, value:type[i].typeId});
				//var isSelected = NOT_SELECTED;
				//$('#alertType').append(String.format(ALERT_TYPE_OPTION_FMT, type[i].typeId, type[i].displayName, isSelected ));
			}
			var s = new Nm3kSelect({
		         id : "alertType",
		         renderTo : "putSel",
		         width : 158,
		         firstSelect : 0,
		         rowNum : 2,
		         subWidth : 400,
		         dataArr : arrData
		    })
		    s.init();
		}
	});
}

var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "/images/s.gif";
/*********************************************************************
 *onMonuseDown onHdMouseDown
 *Copy From oltAlert.jsp
 *modify by huangdongsheng
 *********************************************************************/
function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		// mouseHandled flag check for a duplicate selection (handleMouseDown) call
		if(!this.mouseHandled && row){
			//alert(this.grid.store.getCount());
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				//判断头部的全选CheckBox框是否选中，如果是，则删除
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	/**
	*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
	*由原来的t.className修改为t.className.split(' ')[0]
	*为什么呢？这个是我在快速点击头部全选CheckBox时，
	*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
	*去全选或者全选不能实现
	*/
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

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
function renderLevelName(value, p, record) {
	return record.data.levelName;
}
function renderLevel(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle title="{1}">{2}',
		value, record.data.levelName, record.data.message);
}

function renderOpeartion(value, p, record){
	var alertId = record.data.alertId;
    return String.format("<a href='javascript:;' onclick='showHisAlertDetail(\"{0}\")'>" + I18N.FAULT.alertProperty + '</a>', alertId);
}

function showHisAlertDetail(alertId){
	window.parent.createDialog("alertDlg", I18N.FAULT.alertProperty, 800, 500, "alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
}

Ext.onReady(function () {
	//加载告警类型
	initData();
	
	var w = document.body.clientWidth - 30;
	var h = $(window).height() - 105;

	//var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [
		//{header: I18N.EVENT.levelHeader, width: 100, dataIndex: 'levelId', hidden: true, renderer: renderLevelName},	
		{header: '<div class="txtCenter">'+I18N.COMMON.description+'</div>', width: 300, align: 'left', groupable:false, dataIndex: 'levelId', renderer: renderLevel},
		{header: '<div class="txtCenter">'+I18N.ALERT.alertType+'</div>', width: 220, dataIndex: 'typeName'},	
		{header: '<div class="txtCenter">'+I18N.EVENT.sourceHeader+'</div>', width: 150, sortable:true, dataIndex: 'host'},
		{header: '<div class="txtCenter">'+I18N.EVENT.timeHeader, width: 160, sortable:true, dataIndex: 'firstTimeStr'},
		{header: '<div class="txtCenter">'+I18N.ALERT.clearPerson, width: 100, dataIndex: 'clearUser'},	
		{header: '<div class="txtCenter">'+I18N.ALERT.clearTime, width: 160, sortable:true, dataIndex: 'clearTimeStr'},
		{header: I18N.Config.oltConfigFileImported.operation, width: 80, align: 'center', sortable:false, dataIndex: 'op', renderer: renderOpeartion}
	];
	
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
	    searchFunction : searchAlarmClick
	})
	
	store = new Ext.data.JsonStore({
	    url: '/cmpoll/getHistoryCmPollAlertList.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['alertId', 'typeName', 'host', 'levelId', 'levelName', 
	    	'name', 'message', 'firstTimeStr', 'lastTimeStr','status', 'confirmUser', 'confirmTimeStr', 'clearUser', 'clearTimeStr']
	});

	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer',margins:'0px 10px 0px 10px',
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm, cls:'normalTable',title:I18N.ALERT.historyAlertInfoList,
		viewConfig:{forceFit:true},region: 'center',
		bbar: buildPagingToolBar() });
	store.setBaseParam('startTime', Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s'));	
	store.setBaseParam('endTime', Ext.util.Format.date(endTime.getValue(), 'Y-m-d H:i:s'));	
	store.load({params:{start:0, limit: pageSize}});
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [grid,{
	    	region: 'north',
			height:90,
			contentEl:'topPart',
			border:false
	    }]
	});
});
</script>
</head>
<body class="whiteToBlack">
	<div class="edge10" id="topPart">
				<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
					<tr>
						<td class="rightBlueTxt w70">@fault/ALERT.alertLevel@:</td>
						<td><select id="alertSeverity" class="normalSel w160">
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
							<%-- <select id="alertType" style="width: 220px;" class="normalSel">
							</select> --%>
						</td>
						<td class="rightBlueTxt w70">
							@resources/MAIN.relaDevice@:
						</td>
						<td>
							<input type="text" id="hostDevice" name="hostDevice" value="${hostDevice}" class="normalInput w130" />
						</td>
						<td rowspan="2">
							<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchAlarmClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>      
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
						<td class="rightBlueTxt">@fault/ALERT.currentORhistory@:</td>
						<td width="160"> 
							<select onchange="alertViewerChanged(this);" class="normalSel w132">
								<option>@fault/currentAlert@</option>
								<option selected>@fault/historyAlert@</option>
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