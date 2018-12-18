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
    module cmc
    import js/nm3k/nm3kPickDate
    plugin Nm3kDropTree
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
<%
boolean confirmAlertPower = uc.hasPower("confirmAlert");
boolean clearAlertPower = uc.hasPower("clearAlert");
%>
var confirmAlertPower = <%=uc.hasPower("confirmAlert")%>;
var clearAlertPower = <%=uc.hasPower("clearAlert")%>;
var pageSize = <%=uc.getPageSize()%>;
var userId = <%=uc.getUser().getUserId()%>;
var cmcId = <s:property value="cmcId"/>;
vcEntityKey = 'cmcId';
var store = null;
var productType = '${productType}';
//开始时间与结束时间控件
var stTime,etTime
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');
var alertId = '${alertId}';

function onRefreshClick() {
	store.reload();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 105;
	grid.setWidth(w);
	grid.setHeight(h);
}
function buildPageBox(page) {
	return (''+"@entity.alert.PageShowInfo@" + page + "@entity.alert.PageItems@");
	return '<select id="pageBox" style="width:60px;" onchange="pageBoxChanged(this);"><option value="15">15</option><option value="20">20</option><option value="25">25</option><option value="50">50</option><option value="75">75</option><option value="100">100</option></select>';
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
}
function alertTypeChanged(obj) {
	if (obj.options[obj.selectedIndex].value == 1) {
		location.href = '/cmc/alert/showCmcAlert.tv?module=12&type=1&cmcId=' + cmcId+'&productType=' + productType;
	}  else if (obj.options[obj.selectedIndex].value == 3) {
		location.href = '/cmc/alert/showCmcHistoryAlert.tv?module=12&type=3&cmcId=' + cmcId+'&productType=' + productType;
	} 
}
function showHistoryAlert() {
	location.href = '/cmc/alert/showCmcHistoryAlert.tv?module=12&type=3&cmcId=' + cmcId+'&productType=' + productType;
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
function alertSetting(){
	window.parent.createDialog("modalDlg", "@CMC.title.alertConfig@", 800, 500, "cmc/alert/showAlertSetting.tv", null, true, true);
}
function showAlertDetail(alertId,recordId){
	var rec = grid.getStore().getById(recordId);
	grid.getSelectionModel().clearSelections();
	grid.getSelectionModel().selectRecords([rec]);
	window.parent.createDialog("modalDlg", "@CMC.title.alertProperty@", 800, 500, "alert/showAlertDetail.tv?alertId=" + alertId, null, true, true);
}
function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 2 || selections[i].data.status == 3){
				 window.parent.showMessageDlg("@COMMON.tip@", "@fault/ALERT.alertCleared@");
				 return;
			}
		}
		window.parent.showTextAreaDlg("@cmcAlert.clear@", "@resources/EVENT.clearMessage@<br />@COMMON.remarks@", function (type, text) {
			if (type == "cancel") {
				return;
			}
			if( !V.isRemarks(text) ){
				window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
				return;
			}
			var alertIds = [];
			var selections = sm.getSelections();
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId;
			}
			Ext.Ajax.request({url:"/fault/clearAlert.tv", method:"post", success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message:text}});
		});
	} else {
        window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}
function clearAlarm(alertId){
	window.parent.showTextAreaDlg("@cmcAlert.clear@", "@resources/EVENT.clearMessage@<br />@COMMON.remarks@", function (type, text) {
        if (type == "cancel") {
            return;
        }
        if( !V.isRemarks(text) ){
			window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
			return;
		}
        Ext.Ajax.request({url:"/fault/clearAlert.tv", method:"post", success:function (response) {
            onRefreshClick();
        }, failure:function () {
        }, params:{alertIds: [alertId], message:text}});
    });
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 1){
				 window.parent.showMessageDlg("@entity.alert.TipTitle@", "@fault/ALERT.alertConfirmed@");
				 return;
			}
		}
		window.parent.showTextAreaDlg("@entity.alert.ConfirmAlert@", "@resources/EVENT.confirmMessage@<br />@COMMON.remarks@", function (type, text) {
			if (type == "cancel") {
				return;
			}
			if( !V.isRemarks(text) ){
				window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
				return;
			}
			var alertIds = [];
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId;
			}
			Ext.Ajax.request({url:"/fault/confirmAlert.tv", method:"post",
				success:function (response) {
					onRefreshClick();
				}, failure:function () {
				}, params:{alertIds: alertIds, message: text}});
		});
	} else {
        window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}
function confirmAlarm(alertId, status) {
	if(status == 1){
	   window.parent.showMessageDlg("@entity.alert.TipTitle@", "@entity.alert.alertIsConfirmed@");
	   return;
	}
	window.parent.showTextAreaDlg("@entity.alert.ConfirmAlert@", "@resources/EVENT.confirmMessage@<br />@COMMON.remarks@", function (type, text) {
        if (type == "cancel") {
            return;
        }
        if( !V.isRemarks(text) ){
			window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
			return;
		}
        Ext.Ajax.request({url:"/fault/confirmAlert.tv", method:"post",
            success:function (response) {
                onRefreshClick();
            }, failure:function () {
            }, params:{alertIds: [alertId], message: text}});
    });
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
function searchAlarmClick() {
	var startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	var endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	store.baseParams={
   		start: 0,
		limit: pageSize,
		levelId: $('#alertSeverity').val(),
		typeId: $("#putSel").data("value"),
		message: $('#alertReason').val(),
		startTime: startTime,
		endTime: endTime,
    };
	store.load({
		callback: function(records) {
			if (records && records.length == 0){
				top.nm3kAlertTips({
                    title:"@entity.alert.TipTitle@",
                    html:"<b class='orangeTxt'>" + "@entity.alert.SearchNoAlert@" + "</b>",
                    okBtnTxt:"@COMMON.OK@"
                });
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	});
}
function renderNote(value, p, record) {
	return String.format('<img src="/images/fault/level{0}.gif" alt="{1}" nm3kTip="{1}" class="nm3kTip" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.levelId, record.data.levelName, record.data.message);
}
function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"/images/correct.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', "@CMC.title.confirmed@");
	} else {
		return String.format('<img hspace=5 src=\"/images/wrong.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', "@CMC.title.unconfirmed@");
		record.data.confirmTimeStr == ''
	}
}
function renderOpeartion(value, p, record){
	var alertId = record.data.alertId;
	var status = record.data.status;
    var htmldetail = String.format("<a href='javascript:;' onclick='showAlertDetail(\"{0}\",\"{2}\")'>@CMC.title.view@</a>", alertId, status,record.id);
    var htmlconfirm = "";
    if (record.data.status != '1') {
        htmlconfirm = String.format("/<a href='javascript:;' onclick='confirmAlarm(\"{0}\",\"{1}\")' >@entity.alert.ConfirmButton@</a>", alertId, status, record.id);
    }
    var htmlclear = "";
    if(record.data.status != '2' && record.data.status != '3'){
        htmlclear = String.format("/<a href='javascript:;' onclick='clearAlarm(\"{0}\")' >@entity.alert.CancelButton@</a>", alertId, status,record.id);
    }
    return htmldetail + htmlconfirm + htmlclear;

}
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

var	ALERT_TYPE_OPTION_FMT = '<option value="{0}">{1}</option>';
var	NOT_SELECTED = "";
var	SELECTED = " selected";
function initData() {
	//初始化查询条件
	$.ajax({
		url: '/cmc/alert/getAllCmcAlertType.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {name:"@entity.alert.All@", value:"all",children:[]};
			response.unshift(all);
		   
			$("#putSel").nm3kDropTree({
				width: 150,
				subWidth : 360,
				inputWidth : 220,
				searchBtnTxt : "@COMMON.search@",
				firstSelectValue : 'all',
				dataArr: response
			});
			
		}
	});
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
		tip += '<p><br>@resources/FAULT.checkMessage@@COMMON.maohao@'+record.data.confirmMessage+'</p>';
		tip += '<p><br>@resources/FAULT.checkTime@@COMMON.maohao@'+record.data.confirmTimeStr+'</p>';
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
		tip += '<p><br>@resources/FAULT.clearMessage@@COMMON.maohao@'+record.data.clearMessage+'</p>';
		tip += '<p><br>@resources/FAULT.clearTime@@COMMON.maohao@'+record.data.clearTimeStr+'</p>';
	}
	return String.format('<img hspace=5 src="{0}" border=0 align="absmiddle" class="nm3kTip" nm3kTip="{1}">', imgUrl, tip);
}
Ext.onReady(function () {
	//加载告警类型
	initData();
	
	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	stTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : "",
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
		value : "",
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
	
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 105;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [sm,
		{header: '<div class="txtCenter">'+"@entity.alert.reason@"+'</div>', width: 250,align:'left', sortable:false, dataIndex: 'message', renderer: renderNote},
		{header: '<div class="txtCenter">'+"@entity.alert.type@"+'</div>', width: 120, align:'left', sortable:false, dataIndex: 'typeName'},	
		{header: "@entity.alert.firstTimeStr@", width: 140, sortable:false, dataIndex: 'firstTimeStr'},
		{header: "@entity.alert.lastTimeStr@", width: 140, sortable:false, dataIndex: 'lastTimeStr'},
		{header: "@resources/FAULT.checkStatus@", width: 70, dataIndex: 'status', renderer: renderStatus},	
 		{header: "@resources/FAULT.clearStatus@", width: 70, dataIndex: 'status', renderer: renderClearStatus},
		{header: "@CHANNEL.operation@", width: 150, align: 'center', sortable:false, dataIndex: 'op', renderer: renderOpeartion,fixed : true}];

	store = new Ext.data.JsonStore({
	    url: '/cmc/alert/getCmcAlertList.tv?cmcId=' + cmcId,
	    root: 'data', 
	    totalProperty: 'rowCount',
	    fields: ['alertId', 'typeName', 'host', 'levelId', 'levelName', 
	    	'name', 'message', 'firstTimeStr','lastTimeStr', 'status', 'confirmUser', 'confirmTimeStr','clearUser', 'confirmTime', 'clearTime', 'clearTimeStr', 'confirmMessage', 'clearMessage']
	});
    //store.setDefaultSort('firstTimeStr', 'desc');    
    store.on("load",function(){
        setTimeout(function(){
        	//得到表格的EL对象
        	var gridEl = this.grid.getEl();
        	//得到表格头部的全选CheckBox框
    		var hd = gridEl.select('div.x-grid3-hd-checker');
    		hd.removeClass('x-grid3-hd-checker-on');
         },100)
	})
	var cm = new Ext.grid.ColumnModel(columnModels);
    
    var tbar = new Ext.Toolbar({
	    items: [
	      {text: '@COMMON.confirm@', id: 'confirmAlert', iconCls: 'bmenu_correct', disabled: !confirmAlertPower, handler: onConfirmAlarmClick}, 
	      '-',       
	      {text: '@COMMON.clear@', id: 'clearAlert', iconCls: 'bmenu_delete', disabled: !clearAlertPower, handler: onClearAlarmClick}
	    ]
	 });
    
	grid = new Ext.grid.GridPanel({id: 'extGridContainer',
		border: true, title : "@entity.alert.currentAlert@",
		store: store, 
		cm: cm, 
		sm: sm, 
		tbar: tbar,
		bbar: buildPagingToolBar(),
		region: "center",
		viewConfig:{
			forceFit:true
		},
		cls: "normalTable edge10"
		});	
	
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [	grid,
	             	{region:'north',
	    	 		height:110,
	    	 		contentEl :'topPart',
	    	 		border: false,
	    	 		cls:'clear-x-panel-body',
	    	 		autoScroll: false
	             	}
	             ]
	});
		
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
    });  
			
	/* grid.on("rowdblclick", function (grid, rowIndex, e) {		
		var record = grid.getStore().getAt(rowIndex); 
		showAlertDetail(record.data.alertId, record.id);
	}); */
	store.setBaseParam("startTime", "");
	store.setBaseParam("endTime", "");
	store.load({params:{start:0, limit: pageSize, alertId : alertId}, callback : function(records){
		if(records && records.length < 1 && alertId.length > 0){ //有alertId说明是从拓扑图点击告警图标跳转过来的,跳转过来查询到的record.length应该是1;
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '@entity.alert.gotoHistory@'
			})
		}
	}});
});
</script>
</head>
<body class="newBody">
	<div id="topPart">
		<div id="header">
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<table width="960" cellpadding="0" cellspacing="0" border="0" rules="none" class="topSearchTable">
				<tr>
					<td class="rightBlueTxt w70">@entity.alert.level@:</td>
					<td width="160">
						<select id="alertSeverity" class="normalSel w150">
								<option value="all">@entity.alert.All@</option>
								<option value="6">@entity.alert.emergencyAlert@</option>
								<option value="5">@entity.alert.seriousAlert@</option>
								<option value="4">@entity.alert.mainAlert@</option>
								<option value="3">@entity.alert.warnAlert@</option>
								<option value="2">@entity.alert.generalAlert@</option>
								<option value="1">@entity.alert.promptAlert@</option>
						</select>
					</td>
					<td class="rightBlueTxt w70">@entity.alert.type@:</td>
					<td width="160">
						<div id="putSel"></div>
					</td>
					<td class="rightBlueTxt w70">
						@entity.alert.reason@:
					</td>
					<td width="160">
						<input id="alertReason" type="text" class="normalInput w130" />
					</td>
					<td rowspan="2">
						<ol class="upChannelListOl pB0">
							<li>
								<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchAlarmClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
							</li>
						</ol>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@entity.alert.searchBeginTime@:</td>
					<td>
						<div id="startTime"></div>
						<!-- <input id="startTime" type="text" class="normalInput w130" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'endTime\')}', isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
					</td>
					<td class="rightBlueTxt">@entity.alert.searchEndTime@:</td>
					<td>
						<div id="endTime"></div>
						<!-- <input id="endTime" type="text" class="normalInput w130" onclick="WdatePicker({minDate: '#F{$dp.$D(\'startTime\')}',	isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="fowardHistoryAlert" style="position:absolute; z-index:2; right:10px; top:45px;">
		<a id="btn1" href="javascript:;" class="normalBtn" onclick="showHistoryAlert()"><span><i class="miniIcoSearch"></i><b>@epon/COMMON.show@@fault/historyAlert@</b></span></a>
	</div>
	
	<dl class="legent r10" style="position:absolute; z-index:2; right:10px; top:120px;">
		<dt class="mR5">@entity.alert.alertlegend@:</dt>
		<dd class="mR2"><img src="/images/fault/level6.png" border="0" alt="" /></dd>
		<dd class="mR10">@entity.alert.emergencyAlert@</dd>
		<dd class="mR2"><img src="/images/fault/level5.png" border="0" alt="" /></dd>
		<dd class="mR10">@entity.alert.seriousAlert@</dd>
		<dd class="mR2"><img src="/images/fault/level4.png" border="0" alt="" /></dd>
		<dd class="mR10">@entity.alert.mainAlert@</dd>
		<dd class="mR2"><img src="/images/fault/level3.png" border="0" alt="" /></dd>
		<dd class="mR10">@entity.alert.warnAlert@</dd>
		<dd class="mR2"><img src="/images/fault/level2.png" border="0" alt="" /></dd>
		<dd class="mR10">@entity.alert.generalAlert@</dd>
		<dd class="mR2"><img src="/images/fault/level1.png" border="0" alt="" /></dd>
		<dd>@entity.alert.promptAlert@</dd>
	</dl>
	
</body>
</Zeta:HTML>