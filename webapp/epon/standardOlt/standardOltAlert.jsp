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
    module epon
    import js/nm3k/nm3kPickDate
    plugin Nm3kDropTree
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var currentId = '<s:property value="currentId"/>';
var type = '<s:property value="type"/>';
var store,	grid;
var pageSize = <%= uc.getPageSize() %>;
<%
	boolean confirmAlertPower = uc.hasPower("confirmAlert");
	boolean clearAlertPower = uc.hasPower("clearAlert");
%>
var confirmAlertPower = <%=uc.hasPower("confirmAlert")%>;
var clearAlertPower = <%=uc.hasPower("clearAlert")%>;
//开始时间与结束时间控件
var stTime,etTime
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');
//从默认地域跳转时会携带此参数
var alertId = '${alertId}';

function initData() {
	// 初始化查询条件
	Ext.Ajax.request({
		url: '/epon/alert/loadAlertQueryInitData.tv?r=' + Math.random(),
		success: function(response) {
			var json = Ext.decode(response.responseText)
			// 告警等级
			var severity = json[0].severity
			for (var i = 0, len = severity.length; i < len; i++) {
				$('#alertSeverity').append('<option value="' + severity[i].levelId + '">' + severity[i].name + '</option>')
			}
		}
	});
	
	//初始化告警类型
	$.ajax({
		url: '/epon/alert/getAllOltAlertType.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {name:"@oltAlert.all@", value:"all",children:[]};
			response.unshift(all);
			
			$("#putSel").nm3kDropTree({
				width: 150,
				subWidth : 360,
				inputWidth : 220,
				searchBtnTxt : "@COMMON.search@",
				firstSelectValue : 'all',
				dataArr: response
			});
			/* var s = new Nm3kLevelSelect({
		         id : "alertType",
		         renderTo : "putSel",
		         width : 150,
		         subWidth : 400,
		         firstSelectValue : 'all',
		         dataArr : response
		    })
		    s.init(); */ 
		}
	});
}
function searchAlarmClick() {
	var startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	var endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
    store.baseParams={
   		start: 0,
   		limit: pageSize,	
        startTime: startTime,
		endTime: endTime,
		entityId:entityId,
		levelId: $('#alertSeverity').val(),
		typeId: $("#putSel").data("value"),
		message: $('#alertReason').val()
    }
	store.load({
		callback: function(records) {
			if (records && records.length == 0) {
				top.nm3kAlertTips({
					title:I18N.COMMON.message,
					html:"<b class='orangeTxt'>" + I18N.oltAlert.noAlertData + "</b>",
					okBtnTxt:I18N.COMMON.OK
				})
				//window.parent.showMessageDlg(I18N.COMMON.message, I18N.oltAlert.noAlertData)
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	})
}
function getLastAlertId() {
	var model =  grid.getSelectionModel()
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false)
		} else {
			model.selectLastRow(false)
		}
		var record = model.getSelected()
		return record.data.alertId
	}
	return 0
}

function printClick() {
	var wnd = window.open()
	showPrintWnd(Zeta$('alert-div'), wnd.document)
	var print = function() {
		wnd.print()
		wnd.close()
	}
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print()
	}	
}
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open()
		doc = wnd.document
	} else {
		doc.open()
	}
	doc.write('<html>')
	doc.write('<head>')
	doc.write('<title>'+I18N.oltAlert.deviceAlert+'[<s:property value="entity.ip"/>]</title>')
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />')
	doc.write('<link rel="stylesheet" type="text/css" href="/css/print.css"/>')
	Zeta$NoHeaderAndFooter(doc)
	doc.write('</head>')
	doc.write('<body style="margin:50px;"><center>')
	doc.write(obj.innerHTML)
	doc.write('</center></body>')
	doc.write('</html>')
	doc.close()
}
function onRefreshClick() {
	store.reload()
}
function alertTypeChanged(obj) {
	if ( $(obj).val() == 1) {
		location.href = '/epon/standardOlt/showStandardOltAlert.tv?module=5&type=1&entityId=' + entityId + '&entityType=olt'
	}  else {
		location.href = '/epon/standardOlt/showStandardOltAlertHistory.tv?module=5&type=3&entityId=' + entityId + '&entityType=olt'
	}
}
function buildPageBox(page) {
	return String.format(I18N.COMMON.pagingTempl , page)
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value;
	if(p == pageSize) return
	pageSize = parseInt(p)
	pagingToolbar.pageSize = pageSize
	pagingToolbar.doLoad(0)
}

function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 2 || selections[i].data.status == 3){
				 window.parent.showMessageDlg(I18N.COMMON.message, "@fault/ALERT.alertCleared@");
				 return;
			}
		}
		window.parent.showTextAreaDlg(I18N.oltAlert.cleanAlert, I18N.oltAlert.desc, function (type, text) {
			if (type == "cancel") {
				return
			}
			if(text.length > 255){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
				return;
			}
			var alertIds = []
			var selections = sm.getSelections()
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId
			}
			Ext.Ajax.request({url:"/fault/clearAlert.tv", method:"post", success:function (response) {
				onRefreshClick()
			}, failure:function () {
			}, params:{alertIds: alertIds, message:text}})
		})
	} else {
		window.parent.showMessageDlg(I18N.COMMON.message, I18N.oltAlert.selectAlertData)
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel()
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 1){
				 window.parent.showMessageDlg(I18N.COMMON.message, "@fault/ALERT.alertConfirmed@");
				 return;
			}
		}
		window.parent.showTextAreaDlg(I18N.oltAlert.confirmAlert, I18N.oltAlert.desc, function (type, text) {
			if (type == "cancel") {
				return
			}
			if(text.length > 255){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
				return;
			}
			var alertIds = []
			var selections = sm.getSelections()
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId
			}
			Ext.Ajax.request({url:"/fault/confirmAlert.tv", method:"post",
				success:function (response) {
					onRefreshClick()
				}, failure:function () {
				}, params:{alertIds: alertIds, message: text}})
		})
	} else {
		window.parent.showMessageDlg(I18N.COMMON.message, I18N.oltAlert.selectAlertData)
	}
}
function buildPagingToolBar() {
        pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:['-', buildPageBox(pageSize), '-'
	    ]});
	    if(!confirmAlertPower){
	    	Ext.getCmp('confirm').hide();
	    }
	    if(!clearAlertPower){
	    	Ext.getCmp('clear').hide();
	    }
	return pagingToolbar;
}

function renderNote(value, p, record) {
	return String.format('<img src="/images/fault/level{0}.gif" nm3kTip="{1}" class="nm3kTip" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.level, record.data.levelName, record.data.message);
}
function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"/images/fault/confirm.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', I18N.oltAlert.checked);
	} else {
		return String.format('<img hspace=5 src=\"/images/fault/unconfirm.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', I18N.oltAlert.noCheck);	
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

function manuRenderer(v,m,r){
    var htmldetail = String.format("<a href='javascript:;' onClick='doViewOperation({0})'>@COMMON.view@</a>",r.id);
    var htmlconfirm = "";
    if (r.data.status != '1') {
        htmlconfirm = String.format(" / <a href='javascript:;' onClick = 'doConfirmOperation({0})'>@oltAlert.confirm@</a>",r.id);
    }
    var htmlclear = "";
    if(r.data.status != '2' && r.data.status != '3'){
        htmlclear = String.format("/<a href='javascript:;' onClick='doClearOperation({0});'>@COMMON.clear@</a>",r.id);
    }
	return htmldetail + htmlconfirm + htmlclear;
}
function doViewOperation(alertId){
    var rec = grid.getStore().getById(alertId);
	grid.getSelectionModel().clearSelections();
	grid.getSelectionModel().selectRecords([rec]);
	window.parent.createDialog("modalDlg", I18N.oltAlert.alertProperty, 800, 500,
		"alert/showAlertDetail.tv?alertId=" + alertId, null, true, true);
}
function doClearOperation(alertId){
	window.parent.showTextAreaDlg(I18N.oltAlert.cleanAlert, I18N.oltAlert.desc, function (type, text) {
        if (type == "cancel") { return; }
        if(text.length > 255){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
			return;
		}
        Ext.Ajax.request({url:"/fault/clearAlert.tv", method:"post", success:function (response) {
            onRefreshClick()
        }, failure:function () {
        }, params:{alertIds: [alertId], message:text}})
    })
}
function doConfirmOperation(alertId){
	var record = grid.getStore().getById(alertId);  // Get the Record
	if(record.data.status == 1){
	   	return window.parent.showMessageDlg(I18N.COMMON.message, I18N.oltAlert.checkedAlert)
	}
	window.parent.showTextAreaDlg(I18N.oltAlert.confirmAlert, I18N.oltAlert.desc, function (type, text) {
	    if (type == "cancel") {
	        return
	    }
	    if(text.length > 255){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.alert.confirmAlertMessage);
			return;
		}
	    Ext.Ajax.request({url:"/fault/confirmAlert.tv", method:"post",
	        success:function (response) {
	            onRefreshClick()
	        }, failure:function () {
	        }, params:{alertIds: [alertId], message: text}})
	});
}
function renderStatus(value, p, record) {
	//完善tip信息：
	//未确认时只需显示确认状态
	//已确认需要显示确认状态、确认用户、确认时间
	var tip = '<p>@resources/FAULT.confirmStatus@@COMMON.maohao@@resources/FAULT.unconfirmed@</p>', imgUrl = '/images/fault/unconfirm.png';
	if(record.data.status == '1'){
		//已确认
		imgUrl = '/images/fault/confirm.png';
		tip = '<p>@resources/FAULT.confirmStatus@@COMMON.maohao@@resources/FAULT.confirmed@</p>';
		tip += '<p><br>@resources/FAULT.checkCustomer@@COMMON.maohao@'+record.data.confirmUser+'</p>';
		tip += '<p><br>@resources/FAULT.checkMessage@@COMMON.maohao@'+record.data.confirmMsg+'</p>';
		tip += '<p><br>@resources/FAULT.checkTime@@COMMON.maohao@'+record.data.confirmTime+'</p>';
	}
	return String.format('<img hspace=5 src="{0}" border=0 align="absmiddle" class="nm3kTip" nm3kTip="{1}">', imgUrl, tip);
}

function renderClearStatus(value, p, record){
	var tip = '<p>@resources/FAULT.clearStatus@@COMMON.maohao@@resources/FAULT.unclear@</p>', imgUrl = '/images/fault/unconfirm.png';
	if (record.data.status == '2' || record.data.status == '3') {
		//已清除
		imgUrl = '/images/fault/confirm.png';
		tip = '<p><br>@resources/FAULT.clearStatus@@COMMON.maohao@@resources/FAULT.cleared@</p>';
		tip += '<p><br>@resources/FAULT.clearCustomer@@COMMON.maohao@'+record.data.clearUser+'</p>';
		tip += '<p><br>@resources/FAULT.clearMessage@@COMMON.maohao@'+record.data.clearMsg+'</p>';
		tip += '<p><br>@resources/FAULT.clearTime@@COMMON.maohao@'+record.data.clearTime+'</p>';
	}
	return String.format('<img hspace=5 src="{0}" border=0 align="absmiddle" class="nm3kTip" nm3kTip="{1}">', imgUrl, tip);
}
$(document).ready(function () { 
    initData();
    
  	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	stTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : lastWeek,
		maxValue: current,
		editable: false,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s',  //日期格式
	    listeners: {
	    	"select": function(){
	    		//修改结束时间的最小值限制
	    		etTime.setMinValue(stTime.getValue());
	    		//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
				var startTime = stTime.value;
	    		if(!etTime.value) return;
				var	endTime = etTime.value;
	    		if(startTime<=endTime){
	    			stTime.removeClass('x-form-invalid');
	    			etTime.removeClass('x-form-invalid');
	    		}
			}
		}
	});
	etTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : currentTime,
		minValue:stTime.getValue(),
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s',  //日期格式
	    listeners: {
			"select": function(){
				//修改结束时间的最大值限制
				stTime.setMaxValue(etTime.getValue());
				//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
				if(!stTime.value) return;
				var startTime = stTime.value;
				var endTime = etTime.value;
	    		if(startTime<=endTime){
	    			stTime.removeClass('x-form-invalid');
	    			etTime.removeClass('x-form-invalid');
	    		}
			}
		}
	});
	//构造查询方式
	nm3kPickData({
		startTime : stTime,
		endTime : etTime,
		searchFunction : searchAlarmClick
	})
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
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [sm,
     		{header: '<div class="txtCenter">'+I18N.oltAlert.alertReason+'</div>',width:250, sortable: false, dataIndex: 'message', align: 'left', renderer: renderNote},
     		{header: '<div class="txtCenter">'+I18N.oltAlert.type+'</div>', sortable: false, align: 'left', dataIndex: 'typeName'},	
     		{header: I18N.oltAlert.createTime,  sortable: false, align: 'center', dataIndex: 'firstTime'},
     		{header: I18N.oltAlert.lastTime, sortable: false, align: 'center', dataIndex: 'lastTime'},
     		/* {header: I18N.oltAlert.status, sortable: false, align:'center', dataIndex: 'status', renderer: renderStatus}, */	
     		{header: "@resources/FAULT.checkStatus@", width: 70, dataIndex: 'status', renderer: renderStatus},	
     		{header: "@resources/FAULT.clearStatus@", width: 70, dataIndex: 'status', renderer: renderClearStatus},
     		/* {header: I18N.oltAlert.confirmUser,  sortable: false, align: 'center', dataIndex: 'confirmUser'},
     		{header: I18N.oltAlert.confirmTime, sortable: false, align: 'center', dataIndex: 'confirmTime'}, */
     		{header: I18N.COMMON.manu,width:150,  align: 'center', sortable:false, dataIndex: 'op', renderer: manuRenderer , fixed : true}
     	];
    store = new Ext.data.JsonStore({
	    url: '/alert/loadEntityAlert.tv?r=' + Math.random(),
	    baseParams: {
	    	type: type,
	    	entityId: entityId
	    	//startTime: $('#startTime').val(),
	    	//endTime: $('#endTime').val()
	    },
	    root: 'data', totalProperty: 'rowCount',idProperty: 'alertId',
	    fields: ['alertId', 'typeName', 'host', 'level', 'levelName', 'name', 'message', 'firstTime','lastTime','status', 'confirmUser', 'confirmTime', 'clearUser', 'clearTime', 'confirmMsg', 'clearMsg']
	});
    store.on("load",function(){
        setTimeout(function(){
        	var gridEl = this.grid.getEl();//得到表格的EL对象
    		var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
    		hd.removeClass('x-grid3-hd-checker-on');
         },100)
	})
    
	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({
	    stripeRows:true,cls:"normalTable edge10",bodyCssClass: 'normalTable',
   		region: "center",title : "@oltAlert.currentAlertList@",
	    id: 'extGridContainer', 
	    viewConfig:{ forceFit: true },
		store: store, cm: cm, sm: sm, bbar: buildPagingToolBar()
	});
	
	store.setBaseParam("startTime", lastWeek);
	store.setBaseParam("endTime", currentTime);
	store.load({params:{start:0, limit: pageSize, alertId : alertId}, callback : function(records){
		if(records && records.length < 1 && alertId.length > 0){ //有alertId说明是从拓扑图点击告警图标跳转过来的,跳转过来查询到的record.length应该是1;
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '@oltAlert.gotoHistory@'
			})
		}
	}});
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
         	{region:'north',
	 		height:110,
	 		contentEl :'topPart',
	 		border: false,
	 		cls:'clear-x-panel-body'
         	}
         ]
	}); 
});

</script>

</head>
<body class=whiteToBlack >
<div id="topPart">	
	<table width=100% cellspacing=0 cellpadding=0>
		<tr>
			 <%@ include file="/epon/inc/navigator_standardOlt.inc"%>
		</tr>
		<tr>
			<td>
				<table class="topSearchTable">
					<tr>
						<td class="rightBlueTxt w70">@oltAlert.level@:</td>
						<td>
							<select id="alertSeverity" class="normalSel w150">
								<option value="all">@oltAlert.all@</option>
							</select>
						</td>
						<td class="rightBlueTxt w70">@oltAlert.alertType@:</td>
						<td>
							<div id="putSel"></div>
							<%-- <select id="alertType"  class="normalSel w130">
								<option value="all">@oltAlert.all@</option>
							</select> --%>
						</td>
						<td class="rightBlueTxt w70">@oltAlert.alertReason@:</td>
						<td width="160px;">
							<input id="alertReason"  class="normalInput w130" type="text" />
						</td>
						<td rowspan="3" colspan="2" style="vertical-align: middle;text-align: left;">
							<ol class="upChannelListOl pB0">
								<li>
								<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchAlarmClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
								</li>
								<% if (confirmAlertPower) { %>
								<li>
									<a id="btn2" href="javascript:;" class="normalBtn " onclick="onConfirmAlarmClick()"><span><i class="miniIcoCorrect"></i>@oltAlert.confirm@</span></a>
								</li>
								<% } if (clearAlertPower) { %>
								<li>
									<a id="btn3" href="javascript:;" class="normalBtn " onclick="onClearAlarmClick()"><span><i class="miniIcoClose"></i>@COMMON.clear@</span></a>
								</li>
								<% } %>
							</ol>
						</td>
					</tr>
					<tr  style="margin-top: 10px;"></tr>
					<tr>
						<td class="rightBlueTxt w70">@oltAlert.startTime@:</td>
						<td>
							<div id="startTime"></div>
						<!-- <input id="startTime"  class="normalInput w130" type="text" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endTime\')}', isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
						</td>
						<td class="rightBlueTxt w70">@oltAlert.endTime@:</td>
						<td>
							<div id="endTime"></div>
						<!-- <input id="endTime" type="text"  class="normalInput w130" onclick="WdatePicker({minDate: '#F{$dp.$D(\'startTime\')}',isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
						</td>
						<td class="rightBlueTxt w70">@FAULT/ALERT.currentORhistory@:</td>
						<td>
	                        <select class="normalSel w130" onchange="alertTypeChanged(this);">
								<option value="1" <s:if test="type==1">selected</s:if>>@oltAlert.curAlert@</option>
								<option value="3" <s:if test="type==3">selected</s:if>>@oltAlert.historyAlert@</option>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>
	
	<dl class="legent r10" style="position:absolute; z-index:2; right:10px; top:110px;">
		<dt class="mR5">@FAULT/WorkBench.alertLegend@:</dt>
			<dd class="mR2"><img src="/images/fault/level6.png" border="0" alt="" /></dd>
			<dd class="mR10">@FAULT/WorkBench.emergencyAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level5.png" border="0" alt="" /></dd>
			<dd class="mR10">@FAULT/WorkBench.seriousAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level4.png" border="0" alt="" /></dd>
			<dd class="mR10">@FAULT/WorkBench.mainAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level3.png" border="0" alt="" /></dd>
			<dd class="mR10">@FAULT/WorkBench.minorAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level2.png" border="0" alt="" /></dd>
			<dd class="mR10">@FAULT/WorkBench.generalAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level1.png" border="0" alt="" /></dd>
			<dd>@FAULT/WorkBench.message@</dd>
	</dl>
</body>
</Zeta:HTML>