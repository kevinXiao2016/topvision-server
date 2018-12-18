<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
    MODULE  fault
    import js.customColumnModel
    import fault.alertCommon
    import fault.historyAlertList
    import js/nm3k/nm3kPickDate
    import js/nm3k/Nm3kTools
    plugin Nm3kDropTree
</Zeta:Loader>
<style type="text/css">
.toggle-link{
	line-height: 20px;
	color: #B3711A;
	padding: 3px 5px;
	display: inline-block;
}
.toggle-link:hover{
	color: #B3711A;
}
</style>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/zetaframework/jquery.autocomplete.min.js"></script>
<script type="text/javascript">
var userAlert = Nm3kTools.tools.StringToBoolean('${userAlert}');
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
var saveColumnId = 'historyAlertList';//保存表头数据的id;
var curLevel = ${level};
var curType = ${typeId};
var pageSize = <%= uc.getPageSize() %>;
var storeUrl = "../alert/loadHistoryAlertList.tv";

var beginTime = '${startTime}';
var stopTime = '${endTime}';
var hostDevice = '${hostDevice}';
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

if(beginTime == '' || beginTime == null){
	beginTime = lastWeek;
}

if(stopTime == '' || stopTime == null){
	stopTime = currentTime;
}

var columnModels = [
	/* {header: "@resources/EVENT.levelHeader@", width: 100, dataIndex: 'level', hidden: true, renderer: renderLevelName, sortable:true}, */ 	
	{header: '<div class="txtCenter">@ALERT.alertReason@</div>', width: 300, groupable:false, dataIndex: 'nativeMessage', align:'left', renderer: renderLevel, sortable:false},
	{header: '<div class="txtCenter">@ALERT.alertType@</div>', width: 200, align:'left', dataIndex: 'typeName', sortable:false},	
	{header: '<div class="txtCenter">@resources/EVENT.sourceHeader@</div>', width: 130,align:'left',dataIndex: 'host',renderer:renderDevice, sortable:true},
	{header: "@resources/EVENT.timeHeader@", width: 150,dataIndex: 'firstTime', sortable:true},
	{header: "@resources/EVENT.lastTime@", width: 150,dataIndex: 'lastTime', sortable:true},
	{header: "@ALERT.clearPerson@", width: 100, dataIndex: 'clearUser', sortable:true},	
	{header: "@ALERT.clearTime@", width: 150,dataIndex: 'clearTime', sortable:true},
	{header: "@COMMON.manu@", width: 80, dataIndex: 'clearTime', exportable: false, renderer: renderManu, fixed:true}
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
			var all = {name: "@ALERT.all@", value:"0",children:[]};
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


function searchClick(){
	//组装查询条件
	var queryData = {
		start: 0,
		limit: pageSize
	};
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
	
	//清除信息
	queryData.clearMessage = $('#clearMessage').val();
	//清除用户
	queryData.clearUser = $('#clearUser').val();
	//清除开始时间
	queryData.clearStartTime = Ext.util.Format.date(window.clearStartTime.getValue(), 'Y-m-d H:i:s');
	//清除结束时间
	queryData.clearEndTime = Ext.util.Format.date(window.clearEndTime.getValue(), 'Y-m-d H:i:s');
	
	if(!validate()) return;
	
	$.extend(store.baseParams, queryData);
	
	store.load({
		callback: function(records) {
			if (records && records.length == 0) {
				top.nm3kAlertTips({
					title:"@COMMON.tip@",
					html:'<b class="orangeTxt">'+ "@ALERT.SearchNoAlert@" + "!</b>"
				})
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	});
	
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
}

Ext.onReady(function () {
	$('#alertLevel').val(curLevel);
	
	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	stTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'  //日期格式
	});
	etTime = new Ext.ux.form.DateTimeField({
		width:150,
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'  //日期格式
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
	
	reSize();
});

function showCmcPortal(cmcId,cmcName){
	window.parent.addView('entity-' + cmcId, cmcName, 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId);
}

function showOnuInfo(onuId,onuName){
    window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
}
</script>
</head>
<body class="whiteToBlack">
<div id="topPart">
	<div class="edgeAndClearFloat">
			<table class="topSearchTable">
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
					<td id="putSel"></td>
					
					<td class="rightBlueTxt w70">@RESOURCES/MAIN.relaDevice@:</td>
					<td class="w200">
						<input type="text" id="hostDevice" class="normalInput" style="width:155px;" value="" />
					</td>
				</tr>
				<tr id="clearTr">
					<td class="rightBlueTxt w70">@resources/FAULT.clearMessage@:</td>
					<td>
						<input type="text" id="clearMessage" class="normalInput" style="width:155px;" value="" />
					</td>
					<td class="rightBlueTxt w70">@resources/FAULT.clearCustomer@:</td>
					<td><input id="clearUser" type="text" class="normalInput" style="width:150px;"/></td>
					<td class="rightBlueTxt w70">@resources/FAULT.clearTime@:</td>
					<td>
						<table>
							<tr>
								<td><div id="clearStartTime"></div></td>
								<td>-</td>
								<td><div id="clearEndTime"></div></td>
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
										<!-- <li>
											<a id="queryToggle" href="javascript:;" class="toggle-link" onclick="toggleQueryModel()">@resources/COMMON.simpleQuery@</a>
										</li> -->
									</ol>
								</td>
							</tr>
						</table>
					</td>
				</tr> 
				<tr>
					
				</tr>
			</table> 
		</div>
	</div>
	
	<div id="grid-container"></div>
	
	<div id="fowardHistoryAlert" style="position:absolute; z-index:2; right:10px; top:10px;">
		<a id="btn1" href="javascript:;" class="normalBtn" onclick="showCurrentAlert()"><span><i class="miniIcoSearch"></i><b>@epon/COMMON.show@@currentAlert@</b></span></a>
	</div>
	
	<dl id="alertLegend" class="legent r10" style="position:absolute; z-index:2; right:10px; top:115px;">
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
		<%-- --%>
</body>
</Zeta:HTML>