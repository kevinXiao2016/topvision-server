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
    module fault
    import js.customColumnModel
    import fault.alertCommon
    import fault.currentAlertList
    import js/nm3k/nm3kPickDate
    import js/nm3k/Nm3kTools
    plugin Nm3kDropTree
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/zetaframework/jquery.autocomplete.min.js"></script>
<style type="text/css">
html, body{height: 100%;}
.toggle-link{
	line-height: 20px;
	color: #B3711A;
	padding: 3px 5px;
	display: inline-block;
}
.toggle-link:hover{
	color: #B3711A;
}
#grid-container{height:500px;}
</style>
<script type="text/javascript">
var userAlert = Nm3kTools.tools.StringToBoolean('${userAlert}');
//var alertId = ${alertId};
var alertId = window.parent.alertId;
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	//doRefresh();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end
var saveColumnId = 'currentAlertList';//保存表头数据的id;
var userId = <%= uc.getUser().getUserId() %>;
var confirmAlertPower = <%= uc.hasPower("confirmAlert")%>;
var clearAlertPower = <%= uc.hasPower("clearAlert")%>;
var curLevel = <s:property value="level"/>;
var curType = <s:property value="typeId"/>;
var pageSize = <%= uc.getPageSize() %>;
var storeUrl = '../alert/getCurrentAlertList.tv';

var beginTime = '${startTime}';
var stopTime = '${endTime}';
var hostDevice = '${hostDevice}';
var isSimpleSearch = '${simpleModeFlag}' ? ${simpleModeFlag} : true;
var ipNameList = ${ipNameJson};
//开始时间与结束时间控件
var stTime,etTime;
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');

$(function(){
	//ONU删除 重载store;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){   
		trapStoreRefresh(data);
	});
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
	});
});
//重载store;
function trapStoreRefresh(data){
	if(store){
		store.reload();
	}
}

/* if(beginTime == '' || beginTime == null){
	beginTime = lastWeek;
}

if(stopTime == '' || stopTime == null){
	stopTime = currentTime;
} */
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
var columnModels = [
	/* {header: "@resources/EVENT.levelHeader@", width: 20, dataIndex: 'level', hidden: true, renderer: renderLevelName}, */	
	{header: '<div class="txtCenter">@ALERT.alertReason@</div>',width: 260, sortable:false, groupable:false, align:'left', dataIndex: 'message', exportDataIndex: 'nativeMessage', renderer: renderLevel},
	{header: '<div class="txtCenter">@ALERT.alertType@</div>', width: 120, sortable:false, align:'left', dataIndex: 'typeName'},	
	{header: '<div class="txtCenter">@resources/EVENT.sourceHeader@</div>', width: 100, sortable:true, align:'left', dataIndex: 'host', renderer:renderDevice},
	{header: "@resources/EVENT.timeHeader@", width: 120, sortable:true, dataIndex: 'firstTime'},
	{header: "@resources/EVENT.lastTime@", width: 120, sortable:true, dataIndex: 'lastTime'},
	{header: "@resources/FAULT.checkStatus@", width: 70, dataIndex: 'status', renderer: renderStatus, exportRenderer: renderExportConfirmStatus},	
	{header: "@resources/FAULT.clearStatus@", width: 70, dataIndex: 'status', renderer: renderClearStatus, exportRenderer: renderExportClearStatus},
	{header: "@resources/FAULT.operation@", width: 140, exportable: false, dataIndex: 'status', renderer: renderOperation, fixed:true}
];

	
//初始化告警类型
var	ALERT_TYPE_OPTION_FMT = '<option value="{0}" {2}>{1}</option>';
var	NOT_SELECTED = "";
var	SELECTED = " selected";

function initData() {
	//初始化查询条件
	$.ajax({
		url: '../alert/getAlertTypeTree.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {name:"@ALERT.all@", value:"0",children:[]};
			response.unshift(all);

			$("#putSel").nm3kDropTree({
				width: 150,
				subWidth : 360,
				inputWidth : 220,
				searchBtnTxt : "@COMMON.search@",
				firstSelectValue : curType,
				dataArr: response
			});
			
		}
	});
}
//关注告警的数据;
function initAttentionData(){
	$.ajax({
		url: '../fault/loadAlertType.tv?loadUserAlertType=true',
		cache:false,
		async: false, //注意这里必须是同步;
		dataType:'json',
		success: function(response) {
			
			//转换一次，将所有的value转换成Array，并且存储子集的id
			changeParentNodeValue(response);
			valueToString(response);
			
			$("#putSel").nm3kDropTree({
				width: 150,
				subWidth : 360,
				inputWidth : 220,
				searchBtnTxt : "@COMMON.search@",
				settings : {
					data :　{
			    		key : {
			    			name : 'text'
			    		}
		    		}
				},
				//firstSelectValue : response[0].value.split(','),
				dataArr: response
			});
			
		}
	});
}

function simpleSearchClick() {
	var quickText = $('#quickText').val();
	
	var queryData = {};
	queryData.queryModel = 'quick';
	queryData.queryContent = quickText;
	queryData.start = 0;
    queryData.limit = pageSize;
    
	$.extend(store.baseParams, queryData);
	
	store.load({
		callback: function(records, options, success) {
			if (success &&records && records.length == 0) {
				top.nm3kAlertTips({
					title:"@COMMON.tip@",
					html:'<b class="orangeTxt">'+ "@ALERT.SearchNoAlert@" + "!</b>"
				});
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	} );
}


function searchClick(){
	//组装查询条件 
	var queryData = {};
	//告警等级
	queryData.level = $('#alertLevel').val();
	
	//告警类型
	if(!userAlert){
		queryData.typeId = $("#putSel").data("value");
	}else{ //如果是关注告警;
		queryData.typeIdList = $("#putSel").data("value");
	}
	
	//产生开始时间
	queryData.startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	//产生结束时间
	queryData.endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	//关联设备
	queryData.hostDevice = $('#hostDevice').val();

    queryData.start = 0;
    queryData.limit = pageSize;
	
	//高级查询的部分条件
	if(isAdvanceQuery()){
		//切换模式
		queryData.queryModel=null;
		//确认状态
		queryData.confirmStatus = $('#confirmStatusSel').val();
		if(queryData.confirmStatus == 1){
			//确认用户
			queryData.confirmUser = $('#confirmUser').val();
			//确认开始时间
			queryData.confirmStartTime = Ext.util.Format.date(window.confirmStartTime.getValue(), 'Y-m-d H:i:s');
			//确认结束时间
			queryData.confirmEndTime = Ext.util.Format.date(window.confirmEndTime.getValue(), 'Y-m-d H:i:s');
		}else{
			//update by xiaoyue 避免已确认状态下的条件影响未确认状态
			queryData.confirmUser = null;
			queryData.confirmStartTime = null;
			queryData.confirmEndTime = null;
		}
		//清除状态
		queryData.clearStatus = $('#clearStatusSel').val();
		if(queryData.clearStatus == 1){
			//清除用户
			queryData.clearUser = $('#clearUser').val();
			//清除开始时间
			queryData.clearStartTime = Ext.util.Format.date(window.clearStartTime.getValue(), 'Y-m-d H:i:s');
			//清除结束时间
			queryData.clearEndTime = Ext.util.Format.date(window.clearEndTime.getValue(), 'Y-m-d H:i:s');
		}else{
			//update by xiaoyue 避免已清楚状态下的条件影响未清除状态
			queryData.clearUser = null;
			queryData.clearStartTime = null;
			queryData.clearEndTime = null;
		}
	}
	
	if(!validate()) return;
	
	$.extend(store.baseParams, queryData);
	
	store.load({
		callback: function(records, options, success) {
			if (success &&records && records.length == 0) {
				top.nm3kAlertTips({
					title:"@COMMON.tip@",
					html:'<b class="orangeTxt">'+ "@ALERT.SearchNoAlert@" + "!</b>"
				});
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
			var sm = grid.getSelectionModel();
			if(sm.getCount() === grid.store.getCount()){
	  			sm.selectAll();
	  		}
		}
	} );
	
	
	function validate(){
		//产生时间校验
		if(queryData.startTime && !queryData.endTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterCreateEndTime@");
			return false;
		}else if(!queryData.startTime && queryData.endTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterCreateStartTime@");
			return false;
		}else if(queryData.startTime && queryData.endTime && queryData.startTime > queryData.endTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.createEndTimeLessThanStartTime@");
			return false;
		}
		//确认时间校验
		if(queryData.confirmStartTime && !queryData.confirmEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterConfirmEndTime@");
			return false;
		}else if(!queryData.confirmStartTime && queryData.confirmEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterConfirmStartTime@");
			return false;
		}else if(queryData.confirmStartTime && queryData.confirmEndTime && queryData.confirmStartTime > queryData.confirmEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.confirmEndTimeLessThanStartTime@");
			return false;
		}
		//清除时间校验
		if(queryData.clearStartTime && !queryData.clearEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterClearEndTime@");
			return false;
		}else if(!queryData.clearStartTime && queryData.clearEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.pleaseEnterClearStartTime@");
			return false;
		}else if(queryData.clearStartTime && queryData.clearEndTime && queryData.clearStartTime > queryData.clearEndTime){
			window.parent.showMessageDlg("@COMMON.tip@", "@alert.clearEndTimeLessThanStartTime@");
			return false;
		}
		return true;
	}
	
	function isAdvanceQuery(){
		return !$('#advanceSearchTable').css("display") != "none";
	}
}

Ext.onReady(function () {
		
	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	//产生时间
	window.stTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		startDay: 0,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	window.etTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	//确认时间
	window.confirmStartTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		startDay: 0,
		renderTo:"confirmStartTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	window.confirmEndTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"confirmEndTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	//清除时间
	window.clearStartTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		startDay: 0,
		renderTo:"clearStartTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	window.clearEndTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"clearEndTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	
	$('#confirmStatusSel').on('change', function(){
		var value = $('#confirmStatusSel').val();
		if(value == 1){
			$('.confirmSecondConditionTd').show();
		}else {
			$('.confirmSecondConditionTd').hide();
		}
	});
	$('#clearStatusSel').on('change', function(){
		var value = $('#clearStatusSel').val();
		if(value == 1){
			$('.clearSecondConditionTd').show();
		}else {
			$('.clearSecondConditionTd').hide();
		}
	});
	$('.confirmSecondConditionTd').hide();
	$('.clearSecondConditionTd').hide();
	
	if(isSimpleSearch) {
		showQuickModel();
	} else {
		showAdvanceModel();
	}
	
	//设置传过来的参数
	if(beginTime != '' && beginTime != null){
		stTime.setValue(beginTime);
	}
	if(stopTime != '' && stopTime != null){
		etTime.setValue(stopTime);
	}
	//构造查询方式
	nm3kPickData({
		startTime : stTime,
		endTime : etTime,
		searchFunction : searchClick
	})
	
	$('#hostDevice').val(hostDevice);
	
});

function showCmcPortal(cmcId,cmcName){
	window.parent.addView('entity-' + cmcId, cmcName, 'entityTabIcon', '/cmc/showCmcPortal.tv?cmcId=' + cmcId);	
}

function showOnuInfo(onuId,onuName){
    window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
}
</script>
<%
boolean confirmAlertPower = uc.hasPower("confirmAlert");
boolean clearAlertPower = uc.hasPower("clearAlert");
%>
</head>
<body class="whiteToBlack">
	<div id="topPart">	
		<div class="edgeAndClearFloat">
			<table id="quickSearchTable">
				<tr>
					<td>
						<input id="quickText" type="text" class="normalInput" placeHolder="@ALERT.queryTip@" maxlength="63" style="width: 330px;padding-left: 5px;"/>
					</td>
					<td colspan="4" style="padding-left:5px;">
						<ol class="upChannelListOl pB0">
							<li>
								<a id="btn1" href="javascript:;" class="normalBtn" onclick="simpleSearchClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
							</li>
							<li>
								<a href="javascript:;" class="toggle-link" onclick="toggleQueryModel()">@resources/COMMON.advanceQuery@</a>
							</li>
						</ol>
					</td>
				</tr>
			</table>
			<table id="advanceSearchTable" >
				<tr>
					<td class="rightBlueTxt w70">@ALERT.alertLevel@:</td>
					<td>
						<select id="alertLevel" class="normalSel" style="width:155px;">
							<option value="0">@ALERT.all@</option>
							<option value="6">@WorkBench.emergencyAlarm@</option>
							<option value="5">@WorkBench.seriousAlarm@</option>
							<option value="4">@WorkBench.mainAlarm@</option>
							<option value="3">@WorkBench.minorAlarm@</option>
							<option value="2">@WorkBench.generalAlarm@</option>
							<option value="1">@WorkBench.message@</option>
						</select>
					</td>
					<td class="rightBlueTxt w70">@ALERT.alertType@:</td>
					<td id="putSel"><select id="alertType" class="normalSel w150"></select></td>
					<td class="rightBlueTxt w70">@RESOURCES/MAIN.relaDevice@:</td>
					<td class="w200">
						<input id="hostDevice" type="text" class="normalInput" style="width:155px;"/>
					</td>
				</tr>
				<tr id="confirmTr">
					<td class="rightBlueTxt">@resources/FAULT.checkStatus@:</td>
					<td>
						<select id="confirmStatusSel" class="normalSel" style="width:155px;">
							<option value="-1">@resources/COMMON.pleaseSelect@</option>
							<option value="0">@resources/FAULT.unconfirmed@</option>
							<option value="1">@resources/FAULT.confirmed@</option>
						</select>
					</td>
					<td class="rightBlueTxt confirmSecondConditionTd">@resources/FAULT.checkCustomer@:</td>
					<td class="confirmSecondConditionTd"><input id="confirmUser" type="text" class="normalInput" style="width:150px;"/></td>
					<td class="rightBlueTxt confirmSecondConditionTd">@resources/FAULT.checkTime@:</td>
					<td>
						<table>
							<tr>
								<td class="confirmSecondConditionTd"><div id="confirmStartTime"></div></td>
								<td class="confirmSecondConditionTd">-</td>
								<td class="confirmSecondConditionTd"><div id="confirmEndTime"></div></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr id="clearTr">
					<td class="rightBlueTxt w70">@resources/FAULT.clearStatus@:</td>
					<td>
						<select id="clearStatusSel" class="normalSel" style="width:155px;">
							<option value="-1">@resources/COMMON.pleaseSelect@</option>
							<option value="0">@resources/FAULT.unclear@</option>
							<option value="1">@resources/FAULT.cleared@</option>
						</select>
					</td>
					<td class="rightBlueTxt w70 clearSecondConditionTd">@resources/FAULT.clearCustomer@:</td>
					<td class="clearSecondConditionTd"><input id="clearUser" type="text" class="normalInput" style="width:150px;"/></td>
					<td class="rightBlueTxt w70 clearSecondConditionTd">@resources/FAULT.clearTime@:</td>
					<td>
						<table>
							<tr>
								<td class="clearSecondConditionTd"><div id="clearStartTime"></div></td>
								<td class="clearSecondConditionTd">-</td>
								<td class="clearSecondConditionTd"><div id="clearEndTime"></div></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt w70">@resources/FAULT.firstTime@:</td>
					<td colspan="4">
						<table>
							<tr>
								<td><div id="startTime"></div></td>
								<td>-</td>
								<td><div id="endTime"></div></td>
								<td colspan="4" style="padding-left:5px;">
									<ol class="upChannelListOl pB0">
										<li>
											<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="searchClick()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
										</li>
										<li>
											<a href="javascript:;" class="toggle-link" onclick="toggleQueryModel()">@resources/COMMON.simpleQuery@</a>
										</li>
									</ol>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="fowardHistoryAlert" style="position:absolute; z-index:2; right:10px; top:10px;">
		<a id="btn1" href="javascript:;" class="normalBtn" onclick="showHistoryAlert()"><span><i class="miniIcoSearch"></i><b>@epon/COMMON.show@@historyAlert@</b></span></a>
	</div>
	
	<div id="grid-container"></div>
	
	<dl id="alertLegend" class="legent r10" style="position:absolute; z-index:2; right:10px; top:147px;">
		<dt class="mR5">@WorkBench.alertLegend@:</dt>
			<dd class="mR2"><img src="/images/fault/level6.png" border="0" alt="" /></dd>
			<dd class="mR10">@WorkBench.emergencyAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level5.png" border="0" alt="" /></dd>
			<dd class="mR10">@WorkBench.seriousAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level4.png" border="0" alt="" /></dd>
			<dd class="mR10">@WorkBench.mainAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level3.png" border="0" alt="" /></dd>
			<dd class="mR10">@WorkBench.minorAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level2.png" border="0" alt="" /></dd>
			<dd class="mR10">@WorkBench.generalAlarm@</dd>
			<dd class="mR2"><img src="/images/fault/level1.png" border="0" alt="" /></dd>
			<dd>@WorkBench.message@</dd>
	</dl>
</body>
</Zeta:HTML>
