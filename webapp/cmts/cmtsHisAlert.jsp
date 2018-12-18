<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    plugin DateTimeField
    MODULE  CMC
    import js/nm3k/nm3kPickDate
    plugin Nm3kDropTree
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
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
var cmcId = <s:property value="cmcId"/>;
var productType = '${productType}';
//开始时间与结束时间控件
var stTime,etTime
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');

function onRefreshClick() {
	store.reload();
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
function alertTypeChanged(obj) {
	if (obj.options[obj.selectedIndex].value == 1) {
		location.href = '/cmts/alert/showCmtsAlert.tv?module=7&type=1&cmcId=' + cmcId+'&productType=' + productType;
	}  else if (obj.options[obj.selectedIndex].value == 3) {
		location.href = '/cmts/alert/showCmtsHistoryAlert.tv?module=7&type=3&cmcId=' + cmcId+'&productType=' + productType;
	} 
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
	$.ajax({
		url: '/cmts/alert/getAllCmtsAlertType.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {name:I18N.entity.alert.All, value:"all",children:[]};
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

var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "/images/s.gif";
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
	return String.format('<img src="/images/fault/level{0}.gif" alt="{1}" nm3kTip="{1}" class="nm3kTip" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		value, record.data.levelName, record.data.message);
}
function showHisAlertDetail(alertId){
	window.parent.createDialog("alertDlg", I18N.CMC.title.alertProperty, 800, 500, "alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
}

function renderOpeartion(value, p, record){
	var alertId = record.data.alertId;
	var status = record.data.status;
    return String.format("<a href='javascript:;' onclick='showHisAlertDetail(\"{0}\")'>@CMC.title.view@</a>", alertId);
}

Ext.onReady(function () {
	//加载告警类型
	initData();
	<%-- var endTime = '<%=st%>';
	var startTime = GetDateStr(-7);
	$('#startTime').val(startTime);
	$('#endTime').val(endTime); --%>
	
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
	
	var w = document.body.clientWidth - 30;
	var h = $(window).height() - 105;
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
		{header: I18N.EVENT.levelHeader, width: 100, dataIndex: 'levelId', hidden: true, renderer: renderLevelName},	
		{header: '<div class="txtCenter">'+I18N.COMMON.description+'</div>', width: 300, groupable:false, dataIndex: 'levelId', align:'left', renderer: renderLevel},
		{header: '<div class="txtCenter">'+I18N.ALERT.alertType+'</div>', width: 220, align:'left', dataIndex: 'typeName'},	
		{header: '<div class="txtCenter">'+I18N.EVENT.sourceHeader+'</div>', width: 150, sortable:true, align:'left', menuDisabled : false,dataIndex: 'host'},
		{header: I18N.EVENT.timeHeader, width: 160, sortable:true, menuDisabled : false,dataIndex: 'firstTimeStr'},
		{header: I18N.ALERT.clearPerson, width: 100, dataIndex: 'clearUser'},	
		{header: I18N.ALERT.clearTime, width: 160, sortable:true, menuDisabled : false,dataIndex: 'clearTimeStr'},
		{header: I18N.CHANNEL.operation, width: 80, align: 'center', sortable:false, dataIndex: 'op', renderer: renderOpeartion}
	];

	store = new Ext.data.JsonStore({
	    url: '/cmts/alert/getCmcHistoryAlertList.tv?cmcId=' + cmcId,
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['alertId', 'typeName', 'host', 'levelId', 'levelName', 
	    	'name', 'message', 'firstTimeStr', 'lastTimeStr','status', 'confirmUser', 'confirmTimeStr', 'clearUser', 'clearTimeStr']
	});

	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer',
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm, sm: sm,cls:'normalTable edge10',title:I18N.ALERT.historyAlertInfoList,
		viewConfig:{forceFit:true},region: 'center',
		bbar: buildPagingToolBar() });
		
	store.setBaseParam("startTime", lastWeek);
	store.setBaseParam("endTime", currentTime);
	store.load({params:{start:0, limit: pageSize}});
	
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
					<td>
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
					<td>
						<div id="putSel"></div>
					</td>
					<td class="rightBlueTxt w70">
						@entity.alert.reason@:
					</td>
					<td>
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
					<td class="rightBlueTxt">
						@entity.alert.current@/@entity.alert.history@:
					</td>
					<td>
						<select onchange="alertTypeChanged(this);" class="normalSelW132">
							<option value="1" <s:if test="type==1">selected</s:if>>@entity.alert.currentAlert@</option>
							<option value="3" <s:if test="type==3">selected</s:if>>@entity.alert.historyAlert@</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
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