<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    plugin DateTimeField
	module  network
	import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var baseGrid;
var startTime;
var endTime;
var dir = "DESC";
var sort = "startTime"
var entityTypes = ${entityTypes};
var statusData = ${ upgradeStatus }
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format("@resources/COMMON.displayPerPage@", pageSize), '-'
    ]});
    return pagingToolbar;
}

function query(){
	var entityName = $("#entityName").val();
	if(entityName != null && entityName != ''){
		baseStore.setBaseParam('entityName', entityName);
	}else{
		baseStore.setBaseParam('entityName', null);
	}
	var uplinkDevice = $("#uplinkDevice").val();
	if(uplinkDevice != null && uplinkDevice != ''){
		baseStore.setBaseParam('uplinkDevice', uplinkDevice);
	}else{
		baseStore.setBaseParam('uplinkDevice', null);
	}
	/* var manageIp = $("#manageIp").val();
	if(manageIp != null && manageIp != ''){
		baseStore.setBaseParam('manageIp', manageIp);
	}else{
		baseStore.setBaseParam('manageIp', null);
	} */
	var mac = $("#mac").val();
	if(mac != '' && mac != null){
		baseStore.setBaseParam('mac', mac);
	}else{
		baseStore.setBaseParam('mac', null);
	}
	var jobName = $("#jobName").val();
	if(jobName != '' && jobName != null){
		baseStore.setBaseParam('jobName', jobName);
	}else{
		baseStore.setBaseParam('jobName', null);
	}
	
	var typeId = $("#entityType").val();
	var status = $("#status").val();
	baseStore.setBaseParam('typeId', typeId);
	baseStore.setBaseParam('status', status);
	baseStore.setBaseParam('startTime', Ext.util.Format.date(startTime.getValue(), 'Y-m-d H:i:s'));
	baseStore.setBaseParam('endTime', Ext.util.Format.date(endTime.getValue(), 'Y-m-d H:i:s'));
	baseStore.setBaseParam('start', 0);
	baseStore.setBaseParam('limit', pageSize);
    baseStore.load();
}

function stateRender(data, metadata, record, rowIndex, columnIndex, store) {
	var cellValue = data;
    metadata.attr = ' ext:qtip="' + record.data.statusString + '"';
    return record.data.statusString;
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

function renderStartTime(value, p, record){
	return record.data.startTimeString;
}

function renderEndTime(value, p, record){
	return record.data.endTimeString;
}

$(function(){
	Ext.QuickTips.init();
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 230;
	var baseColumns = [
	           	{header: '<div class="txtCenter">@COMMON.alias@</div>',  width: w * 0.1, menuDisabled: true, sortable : true, remoteSort: true, align: 'left', dataIndex: 'entityName'},
	           	{header: "@resources/COMMON.uplinkDevice@", width: 100, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'uplinkDevice'},
	           	{header: "MAC",  width: 100, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'mac'},
	           	{header: "@batchTopo.entityType@", width: w * 0.1, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'typeName'},
	           	{header: "@batchUpgrade.originVersion@",  width:  w * 0.09, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'originVersion', renderer:addCellTooltip},
           		{header: "@batchUpgrade.upgradeVersion@", width:  w * 0.09, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'upgradeVersion', renderer:addCellTooltip},
	           	{header: '<div class="txtCenter">@batchUpgrade.uplinkEntityName@</div>',  width:  w * 0.1, align: 'left', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'upLinkEntityName'},
	           	{header: "@batchUpgrade.job@", width:  w * 0.09, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'jobName'},
	           	{header: "@batchTopo.retry@", width:  w * 0.07, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'retryTimes'},
	           	{header: "@batchUpgrade.upgradeStartName@",  width:  w * 0.12, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'startTime', renderer : renderStartTime},
           		{header: "@batchUpgrade.upgradeEndName@", width:  w * 0.12, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'endTime', renderer : renderEndTime},
	           	{header: "@batchUpgrade.upgradeStatus@",  width:  w * 0.08, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'status' , renderer : stateRender}
	           	];
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/upgrade/getUpgradeRecord.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityName','manageIp','uplinkDevice','mac','typeName', 'originVersion',
	             'upgradeVersion','upLinkEntityName', 'retryTimes','startTime', 'endTime',
	             'startTimeString', 'endTimeString','status','typeId', 'recordId', 'statusString', 'jobName']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 80;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		title:"@batchUpgrade.upgradeRecord@",
		cls: 'normalTable',
		loadMask: true,
		border: true, 
		height: h,
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load({params:{start:0, limit: pageSize}});
	
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
	    searchFunction : query
	})
	
	$('#startTime').val(startTime);
	$('#endTime').val(endTime);
	
	//buildStatusSelect();
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [baseGrid,
	            {region: 'north',
	    			contentEl:'topPart', 
	    			height:80,
	    			border:false
	    		}]
	});
	
	buildEntityTypeSelect();
	query()
})

function buildStatusSelect(){
	var statusPosition = Zeta$('status');
	$.each(statusData, function(index, status){
		var option = document.createElement('option');
        option.value = status.id;
        option.text = status.displayName;
        try {
        	statusPosition.add(option, null);
        } catch(ex) {
        	statusPosition.add(option);
        }
	})
}

function buildEntityTypeSelect(){
	var deviceTypePosition = Zeta$('entityType');
	for(var i = 0; i < entityTypes.length; i++){
		if(entityTypes[i].typeId != 255){
			var option = document.createElement('option');
	        option.value = entityTypes[i].typeId;
	        option.text = entityTypes[i].displayName;
	        try {
	        	deviceTypePosition.add(option, null);
	        } catch(ex) {
	        	deviceTypePosition.add(option);
	        }
		}
  }
}

</script>
</head>
<body class="whiteToBlack">
<div id="topPart" class="edge10">
<div class="formtip" id="tips" style="display: none"></div>
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt" width="35">@NETWORK.alias@:</td>
				<td width="130">
					<input type="text" id="entityName" name="entityName" class="normalInput w130" />
				</td>
				<!-- <td class="rightBlueTxt" width="84">@batchUpgrade.uplinkEntityName@:</td>
				<td width="130">
					<input type="text" id="upLinkEntityName" name="upLinkEntityName" class="normalInput w130" />
				</td> -->
				<td class="rightBlueTxt w60">@resources/COMMON.uplinkDevice@:</td>
				<td width="160">
					<input type="text" id="uplinkDevice" name="uplinkDevice" class="normalInput" style="width:158px;" />
				</td>
				<td class="rightBlueTxt" width="62">@batchUpgrade.upgradeStatus@:</td>
				<td>
					<select class="normalSel" style="width:158px;" id="status">
					<option value="-1">@sendConfig.pleaseChoose@</option>
					<option value="1">@UPGRADE_SUCCESS@</option>
					<option value="2">@UPGRADE_FAIL@</option>
					</select>
				</td>
				<td class="rightBlueTxt" width="60">@batchTopo.entityType@:</td>
					<td>
					<select class="normalSel w132" id="entityType">
					<option value="">@sendConfig.pleaseChoose@</option>
					</select>
					</td>
			</tr>
			<tr>	
			<td class="rightBlueTxt">MAC:</td>
				<td>
					<input type="text" id="mac" name="mac" class="normalInput w130" />
				</td>
				<td class="rightBlueTxt">@batchUpgrade.job@:</td>
				<td>
					<input type="text" id="jobName" name="jobName" class="normalInput w130" />
				</td>
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
				<td></td>
				<td>    
					<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@resources/COMMON.query@</b></span></a>                  
				</td>
			</tr>
		</table>
</div>
</body>
</Zeta:HTML>