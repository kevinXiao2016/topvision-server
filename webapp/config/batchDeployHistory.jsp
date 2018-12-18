<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
    module batchdeploy
    import js.zetaframework.component.NetworkNodeSelector static
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
//开始时间与结束时间控件
var stTime,etTime;

function lookSuccessFn(id){
	var data = store.getById(id).data;
	var batchDeployId = data.batchDeployId;
	var date = data.startTimeString;
	var entityName = data.entityName;
	top.createDialog("lookBatchDeploySuccess", "@successRecords@",  800, 500, "/batchdeploy/showBatchDeploySuccess.tv?batchDeployId="+batchDeployId+"&entityName="+entityName+"&recordTime="+date, null, true, true);
}
function lookFaultFn(id){
	var data = store.getById(id).data;
	var batchDeployId = data.batchDeployId;
	var date = data.startTimeString;
	var entityName = data.entityName;
	top.createDialog("lookBatchDeploySuccess", "@failedRecords@",  800, 500, "/batchdeploy/showBatchDeployFault.tv?batchDeployId="+batchDeployId+"&entityName="+entityName+"&recordTime="+date, null, true, true);
}
function srender(v,m,r){
	return String.format("<a href='javascript:;' class='yellowLink' onclick='lookSuccessFn({0})'>{1}</a>",r.id, v);
}
function frender(v,m,r){
	return String.format("<a href='javascript:;' class='yellowLink' onclick='lookFaultFn({0})'>{1}</a>",r.id, v);
}
function trender(v,m,r){
	if(v == 10001){
		return "@univlanbind@";
	}else if(v == 10002){
		return "@univlanunbind@";
	}else if(v == 10003){
		return "@perf15Enable@";
	}else if(v == 10004){
		return "@uniRateLimit@";
	}else if(v == 10005){
		return "@perf15Disable@";
	}
}

Ext.onReady(function(){
	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	window.stTime = new Ext.ux.form.DateTimeField({
		width:150,
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
	window.etTime = new Ext.ux.form.DateTimeField({
		width:150,
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
	
	var cm = new Ext.grid.ColumnModel([
		{header:"<div class='txtCenter'>@COMMON.entity@</div>", dataIndex:"entityName"},
		{header:"<div class='txtCenter'>@successCount@</div>", dataIndex:"sucessCount",width:30,renderer: srender},
		{header:"<div class='txtCenter'>@failureCount@</div>", dataIndex:"failureCount",width:30, renderer: frender},
		{header:"<div class='txtCenter'>@duration@</div>", dataIndex:"duration", width:80},
		{header:"<div class='txtCenter'>@COMMON.operator@</div>", dataIndex:"operator", width:80},
		{header:"<div class='txtCenter'>@operationType@</div>", dataIndex:"typeId", width:80, renderer: trender},
		{header:"<div class='txtCenter'>@COMMON.note@</div>", dataIndex:"comment", width:80},
		{header:"<div class='txtCenter'>@execTime@</div>", dataIndex:"startTimeString", width:80}
	]);//end cm;
	store = new Ext.data.JsonStore({
		totalProperty: "count",
		idProperty:"batchDeployId",
		root:"data",
		url : "/batchdeploy/queryForBatchHistory.tv",
		baseParams : {
			entityId : '${entityId}',
			limit : pageSize
		},
		fields: [
			{name: "batchDeployId"},
			{name: "entityName"},
			{name: "sucessCount"},
			{name: "failureCount"},
			{name: "operator"},
			{name: "duration"},
			{name: "startTimeString"},
			"typeId",
			"comment"
		]
	});
		
	var grid = new Ext.grid.GridPanel({
		region:"center",
		stripeRows:true,
		title: "@resultTable@",
		enableColumnMove: false,
		enableColumnResize: true,
		margins:"0px 10px 0px 10px",
		bbar: new Ext.PagingToolbar({
			pageSize: pageSize,
			store: store,
			displayInfo: true,
			emptyMsg: "@noresult@",
			cls: 'extPagingBar'
		}),
		cls: 'normalTable',
		store: store,
		cm : cm,
		viewConfig:{
			forceFit: true
		}
	});	

	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	    		region: "north",
				contentEl: "topPart",
				height:80,
				border:false
				} ,grid]
	});
	store.reload();
	
	var selector = new NetworkNodeSelector({
		renderTo:"selectorCont",
		id: 'select_olt'
	});
	selector.setValue('${entityId}' || -1);
});//end document.ready;

function query(){
	var entityId = $("#select_olt").val();
	var typeId = $("#typeId").val();
	var operator = $("#operator").val();
	var st = stTime.value;
	var et = etTime.value;
	var params = {};
	params.entityId = entityId;
	params.typeId = typeId;
	if( st ){
		if(!et){
			top.showMessageDlg("@COMMON.tip@","@selectEndTime@")
			return $("#et").focus();
		}
		params.st = st;
		params.et = et;
	}
	params.operator = operator;
	params.start = 0;
	params.limit = pageSize;
	store.on("beforeload",function(){
        store.baseParams=params;
	});
	store.reload({params: params});
}
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10">
		<table class="topSearchTable" cellpadding="0" cellspacing="0">
			<tr>
				<td class="rightBlueTxt">@COMMON.entity@: </td>
				<td class="pR10">
					<div id="selectorCont" class="w150"></div>
				</td>
				<td class="rightBlueTxt pL10">@COMMON.type@: </td>
				<td class="pR10">
					<select class="normalSel" style="width:150px" id="typeId">
						<option value="-1">@COMMON.all@</option>
						<option value="10001">@univlanbind@</option>
						<option value="10002">@univlanunbind@</option>
						<option value="10004">@uniRateLimit@</option>
						<option value="10003">@perf15Enable@</option>
						<option value="10005">@perf15Disable@</option>
					</select>
				</td>
				<td class="rightBlueTxt" >@COMMON.operator@: </td>
				<td class="pR10">
					<input type="text" id="operator" class="normalInput w140" />
				</td>
				<td rowspan="2">
					<a href="javascript:;" class="normalBtn" onclick="query();"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt"><label>@COMMON.startTime@:</label></td>
				<td class="pR10">
					<div id="startTime"></div>
				</td>
				<td class="rightBlueTxt"><label>@COMMON.endTime@:</label></td>
				<td>
					<div id="endTime"></div>
				</td>
				<td></td><td></td>
			</tr>
		</table>
	</div>
</body>
</Zeta:HTML>