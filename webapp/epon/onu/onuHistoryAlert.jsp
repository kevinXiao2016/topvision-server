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
    IMPORT epon/onu/onuDeleteTrap
    plugin Nm3kDropTree
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/nm3k/Nm3kLevelSelect.js"></script>
<script type="text/javascript">
var store,	grid;
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end
var onuId = "${onuId}";
var type = "${type}";
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

function initData() {
	// 初始化查询条件
	Ext.Ajax.request({
		url: '/epon/alert/loadAlertQueryInitData.tv?r=' + Math.random(),
		success: function(response) {
			var json = Ext.decode(response.responseText);
			// 告警等级
			var severity = json[0].severity;
			for (var i = 0, len = severity.length; i < len; i++) {
				$('#alertSeverity').append('<option value="' + severity[i].levelId + '">' + severity[i].name + '</option>');
			}
		}
	});
	
	//初始化告警类型
	$.ajax({
		url: '/onu/getOnuAlertType.tv',
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
		    s.init();  */
		}
	});
}

function searchAlarmClick() {
     store.baseParams={
    	startTime: stTime.value,
    	endTime: etTime.value,
    	onuId: onuId,
		entityId:${onu.parentId},
 		levelId: $('#alertSeverity').val(),
 		typeId: $("#putSel").data("value"),
 		message: $('#alertReason').val()
   	}
	store.load({
		callback: function(records) {
			if (records && records.length === 0) {
				top.nm3kAlertTips({
					title:I18N.COMMON.tip,
					html:"<b class='orangeTxt'>"+ I18N.oltAlert.noAlertData + "</b>"
				});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.oltAlert.noAlertData);
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	});
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
function printClick() {
	var wnd = window.open();
	showPrintWnd(Zeta$('alert-div'), wnd.document);
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
	doc.write('<title>'+I18N.oltAlert.deviceAlert+'[<s:property value="entity.ip"/>]</title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="/css/print.css"/>');
	Zeta$NoHeaderAndFooter(doc);
	doc.write('</head>');
	doc.write('<body style="margin:50px;"><center>');
	doc.write(obj.innerHTML);
	doc.write('</center></body>');
	doc.write('</html>');
	doc.close();
}
function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 100;
	grid.setWidth(w);
	grid.setHeight(h);
}
function alertTypeChanged(obj) {
	if ( $(obj).val() == 1) {
		location.href = '/onu/showOnuAlert.tv?module=7&onuId=${onu.entityId}';
	}  else {
		location.href = '/onu/showOnuAlertHistory.tv?module=7&type=3&onuId=${onu.entityId}';
	}
}
function buildPageBox(page) {
	return String.format(I18N.COMMON.pagingTempl , page)
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
}

function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.oltAlert.clearAlert, I18N.oltAlert.desc, function (type, text) {
			if (type == "cancel") {
				return;
			}
			var sm = eventPanel.getSelectionModel();
			var r = sm.getSelected();
			Ext.Ajax.request({url:"/fault/clearAlert.tv", method:"post", success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertId: r.data.alertId, message:text}});
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.oltAlert.selectAlertData);
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.oltAlert.confirmAlert, I18N.oltAlert.desc, function (type, text) {
		if (type == "cancel") {
			return;
		}
		var sm = grid.getSelectionModel();
		var r = sm.getSelected();
		Ext.Ajax.request({url:"/fault/confirmAlert.tv", method:"post",
			success:function (response) {
				onRefreshClick();
			}, failure:function () {
			}, params:{alertId: r.data.alertId, message: text}});
	});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.oltAlert.selectAlertData);
	}
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
		displayInfo:true, items:['-', buildPageBox(pageSize)
	]});
	return pagingToolbar;
}

function renderNote(value, p, record) {
	return String.format('<img src="/images/fault/level{0}.gif" nm3kTip="{1}" class="nm3kTip mL10" border=0 hspace=0 align=absmiddle>&nbsp;{2}',
		record.data.level, record.data.levelName, record.data.message);
}
function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"/images/fault/confirm.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', I18N.oltAlert.checked);
	} else {
		return String.format('<img hspace=5 src=\"/images/fault/unconfirm.png\" border=0 align=absmiddle nm3kTip="{0}" class="nm3kTip">', I18N.oltAlert.noCheck);	
	}
}
function renderOpeartion(value, p, record){
	return String.format("<a href='javascript:;' onClick='showHistoryAlertDetail({0})'>@COMMON.view@</a> ",record.id);
}
function showHistoryAlertDetail(alertId){
	window.parent.createDialog("alertDlg",  "@resources/FAULT.alertProperty@", 800, 500,
		"alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
}


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
	
	var columnModels = [
		{header: '<div class="txtCenter">'+I18N.oltAlert.alertReason+'</div>', width: 240, sortable: false, align: 'left', dataIndex: 'message', renderer: renderNote},
		{header: '<div class="txtCenter">'+I18N.oltAlert.type+'</div>', sortable: false, align: 'left', dataIndex: 'typeName'},	
		{header: I18N.oltAlert.createTime,  sortable: false, align: 'center', dataIndex: 'firstTime'},
		{header: I18N.oltAlert.lastTime,  sortable: false, align: 'center', dataIndex: 'lastTime'},
		{header: I18N.oltAlert.confirmUser,  sortable: false, align: 'center', dataIndex: 'confirmUser'},
		{header: I18N.oltAlert.confirmTime,  sortable: false, align: 'center', dataIndex: 'confirmTime'},
		{header: I18N.oltAlert.clearUser ,  sortable: false, align: 'center', dataIndex: 'clearUser'},
		{header: I18N.oltAlert.clearTime ,  sortable: false, align: 'center', dataIndex: 'clearTime'},
		{header: I18N.COMMON.manu,  align: 'center', sortable:false, dataIndex: 'op', renderer: renderOpeartion}
	];
	store = new Ext.data.JsonStore({
	    url: '/onu/loadOnuHistoryAlert.tv',
	    baseParams: {
	    	entityId:${onu.parentId},
	    	onuId: onuId
	    },
	    root: 'data', totalProperty: 'rowCount',idProperty: 'alertId',
	    fields: ['alertId', 'typeName', 'host', 'level', 'levelName', 
	    	'name', 'message', 'firstTime','lastTime', 'status', 'confirmUser', 'confirmTime', 'clearUser', 'clearTime']
	});

	var cm = new Ext.grid.ColumnModel(columnModels);
	grid = new Ext.grid.GridPanel({
	    stripeRows:true,cls:"normalTable edge10",
   		region: "center",title : "@oltAlert.historyAlertList@",
   		bodyCssClass: 'normalTable',
	    id: 'extGridContainer', 
	    viewConfig:{ forceFit: true },
		store: store, cm: cm, bbar: buildPagingToolBar()
	});
	
	store.setBaseParam("startTime", lastWeek);
	store.setBaseParam("endTime", currentTime);
	store.load({params:{start:0, limit: pageSize}});
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
        	{region:'north',
	 		height:100,
	 		contentEl :'topPart',
	 		border: false,
	 		cls:'clear-x-panel-body',
	 		autoScroll: false
        	}
        ]
	}); 
});
</script>
</head>
<body class=whiteToBlack >
<div id="topPart">	
	<table width=100% cellspacing=0 cellpadding=0 style="minWidth:890px;">
			<tr>
				<td><%@ include file="navigator.inc"%></td>
			</tr>
			<tr>
				<td>
					<table width=100% cellspacing="5px" cellpadding="10px">
						<tr>
							<td class="rightBlueTxt w70">@oltAlert.level@:</td>
							<td class="rightBlueTxt w70">
								<select id="alertSeverity" class="normalSel w150">
									<option value="all">@oltAlert.all@</option>
								</select>
							</td>
							<td class="rightBlueTxt w70">@oltAlert.alertType@:</td>
							<td class="w70">
								<div id="putSel"></div>
								<%-- <select id="alertType" class="normalSel w130">
									<option value="all">@oltAlert.all@</option>
								</select> --%>
							</td>
							<td class="rightBlueTxt w70">@oltAlert.alertReason@:</td>
	                        <td width="160px;">
								<input id="alertReason" type="text" class="normalSel w130" />
							</td>
							<td rowspan="2" style="vertical-align: middle;text-align: left;">
								<ol class="upChannelListOl pB0">
									<li>
										<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchAlarmClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
									</li>
								</ol>
	                        </td>
						</tr>
						<tr>
							<td class="rightBlueTxt w70">@oltAlert.startTime@:</td>
							<td>
								<div id="startTime"></div>
							<!-- <input id="startTime" type="text" class="normalInput w130" onclick="WdatePicker({isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
							</td>
							<td class="rightBlueTxt w70">@oltAlert.endTime@:</td>
							<td>
								<div id="endTime"></div>
							<!-- <input id="endTime" type="text" class="normalInput w130" onclick="WdatePicker({isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> -->
							</td>
	                        <td class="rightBlueTxt w70">@FAULT/ALERT.currentORhistory@:</td>
	                        <td>
		                        <select class="normalSel w130" onchange="alertTypeChanged(this);">
									<option value="1" <s:if test="type==1">selected</s:if>>@oltAlert.curAlert@</option>
									<option value="3" <s:if test="type==3">selected</s:if>>@oltAlert.historyAlert@</option>
								</select>
							</td>
							<td></td>
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