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
var pageSize = <%= uc.getPageSize() %>;
var userId = <%= uc.getUser().getUserId() %>;
var cmcId = <s:property value="cmcId"/>;
vcEntityKey = 'cmcId';
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
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
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
		location.href = 'showCmcAlert.tv?module=12&type=1&cmcId=' + cmcId+'&productType=<s:property value="#parameters.productType" />';
	}  else if (obj.options[obj.selectedIndex].value == 3) {
		location.href = 'showCmcHistoryAlert.tv?module=12&type=3&cmcId=' + cmcId+'&productType=<s:property value="#parameters.productType" />';
	}  
}
function showCurrentAlert() {
	location.href = 'showCmcAlert.tv?module=12&type=1&cmcId=' + cmcId+'&productType=<s:property value="#parameters.productType" />';
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:['-', buildPageBox(pageSize)
	]});
	return pagingToolbar;
}

function renderNote(value, p, record) {
	return String.format('<img src="/images/fault/level{0}.gif" nm3kTip="{1}" class="nm3kTip mL10" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.levelId, record.data.levelName, record.data.message);
}
function renderOpeartion(value, p, record){
	var alertId = record.data.alertId;
    return String.format("<a href='javascript:;' onclick='showHistoryAlertDetail(\"{0}\")'>@CMC.title.view@</a>" , 
            alertId);
   
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
       store.baseParams={
     		start: 0,
 			limit: pageSize,
 			levelId: $('#alertSeverity').val(),
 			typeId: $("#putSel").data("value"),
 			message: $('#alertReason').val(),
 			startTime: stTime.value,
 			endTime: etTime.value
      };
	store.load({
		callback: function(records) {
			if (records && records.length == 0) {
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
	window.parent.createDialog("alertDlg", "@CMC.title.alertProperty@", 800, 500, "alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
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
Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
Ext.onReady(function () {
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
				var startTime = stTime;
				var endTime = etTime;
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

	//var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [
		{header: '<div class="txtCenter">'+"@entity.alert.reason@"+'</div>', width: 250, align:'left',sortable: false, dataIndex: 'message', renderer: renderNote},
		{header: '<div class="txtCenter">'+"@entity.alert.type@"+'</div>', width: 120, align:'left', sortable: false, dataIndex: 'typeName'},	
		{header: "@entity.alert.firstTimeStr@", width: 120, sortable: false, dataIndex: 'firstTimeStr'},
		{header: "@entity.alert.lastTimeStr@", width: 120, sortable: false, dataIndex: 'lastTimeStr'},
		{header: "@entity.alert.confirmUser@", width: 80, sortable: false, dataIndex: 'confirmUser'},
		{header: "@entity.alert.confirmTime@", width: 140, sortable: false, dataIndex: 'confirmTimeStr'},
		{header: "@entity.alert.CancelUser@", width: 80, sortable: false, align: 'center', dataIndex: 'clearUser'},
		{header: "@entity.alert.CancelTime@", width: 120, sortable: false, align: 'center', dataIndex: 'clearTimeStr'},
		{header: "@CHANNEL.operation@", width: 60, align: 'center', sortable:false, dataIndex: 'op', renderer: renderOpeartion}
	];

	store = new Ext.data.JsonStore({
	    url: 'getCmcHistoryAlertList.tv?cmcId=' + cmcId,
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['alertId', 'typeName', 'host', 'levelId', 'levelName', 
	    	'name', 'message', 'firstTimeStr', 'lastTimeStr','status', 'confirmUser', 'confirmTimeStr', 'clearUser', 'clearTimeStr']
	});
    //store.setDefaultSort('firstTimeStr', 'desc');    

	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({id: 'extGridContainer', region: "center",
		animCollapse: animCollapse, trackMouseOver: trackMouseOver, border: true, 
		store: store, cm: cm, title:"@entity.alert.historyAlert@",cls: "edge10 normalTable",
		viewConfig:{
			forceFit:true
		},
		bbar: buildPagingToolBar()
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
	store.setBaseParam("startTime", lastWeek);
	store.setBaseParam("endTime", currentTime);	
	store.load({params:{start:0, limit: pageSize}});
});
</script>
</head>
<body class="newBody" onresize="doOnResize();">
	<div id="topPart">
		<div id="header">
			<%@ include file="entity.inc"%>
		</div>
		<div class="edgeAndClearFloat">
			<table width="960" cellpadding="0" cellspacing="0" border="0" rules="none" class="topSearchTable">
				<tr>
					<td class="rightBlueTxt w70">@entity.alert.level@:</td>
					<td class="w172">
						<select id="alertSeverity" style="width: 150px">
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
					<td class="w172">
						<div id="putSel"></div>
					</td>
					<td class="rightBlueTxt w70">
						@entity.alert.reason@:
					</td>
					<td class="w172">
						<input id="alertReason" type="text" class="normalInput w130" />
					</td>
					<td rowspan="2">
						<ol class="upChannelListOl pB0">
							<li>
								<%-- <a id="btn1" href="javascript:;" class="normalBtnBig" onclick="searchAlarmClick()"><span><i class="miniIcoSearch"></i>@entity.alert.queryButton@</span></a> --%>
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
		<a id="btn1" href="javascript:;" class="normalBtn" onclick="showCurrentAlert()"><span><i class="miniIcoSearch"></i><b>@epon/COMMON.show@@fault/currentAlert@</b></span></a>
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