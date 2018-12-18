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
    PLUGIN  LovCombo
    module  network
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<style type="text/css">
#topTipDiv{ position:absolute; top:35px; right:10px; border:1px solid #ccc; padding:5px; background:#fff; border-radius:8px; -webkit-border-radius:8px; -moz-border-radius:8px; -webkit-box-shadow:0px 2px 10px #888;-moz-box-shadow:0px 2px 10px #888;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var baseGrid;
var regionId = "";
var state;
var start = 0;
var entityType;
var baseStore;
var dir = "DESC";
var sort = "upgradeTime"
var sm;
var interval;
var refreshTime;
var jobId = ${ jobId };
var versionName = '${ versionName }';
var tipDiv = null;
var statusData = ${ upgradeStatus }
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
function onRefreshClick(){
	searchClick()
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format("@resources/COMMON.displayPerPage@", pageSize), '-'
    ]});
    return pagingToolbar;
}

function manuRender(value, p, record) {
	var ipString = record.data.ipString;
	var entityId = record.data.entityId;
	var typeId = record.data.typeId;
	if(operationDevicePower){
		str = "<a href='javascript:;' onClick='showResult(" + entityId + ",\"" + jobId +  "\")'>@sendConfig.viewResult@</a> " + 
		"/ <a href='javascript:;' onClick='upgradeSingleNow(" + entityId + ",\"" + jobId +  "\")'>@batchUpgrade.upgradeImmediately@</a> " + 
		"/ <a href='javascript:;' onClick='deleteSingleEntity()'>@resources/COMMON.delete@</a>"
	}else{
		str = "<a href='javascript:;' onClick='showResult(" + entityId + ",\"" + jobId +  "\")'>@sendConfig.viewResult@</a> "
	}
    return String.format(str);
}

function stateRender(value, p, record) {
	if(record.data.upgradeStatus == 110){
		if(record.data.upgradeNote == 'null'){
			return record.data.upgradeStatusString + '[0%]';
		}else{
			return record.data.upgradeStatusString + '[' + record.data.upgradeNote +  ']';
		}
	}else{
		return record.data.upgradeStatusString;
	}
}

function ipRender(value, p, record) {
	return record.data.ipString;
}

function dtRender(value, p, record) {
	return record.data.dtString;
}

function upgradeTimeRender(value, p, record) {
	return record.data.upgradeTimeString;
}

function showResult(entityId, jobId){
	window.top.createDialog('result', "@batchUpgrade.upgradeResult@", 800, 500,'/upgrade/showUpgradeResult.tv?entityId=' + entityId + '&jobId=' + jobId, null, true, true);
}

function searchClick(){
	var name = $("#entityName").val();
	var ip = $("#ip").val();
	var mac = $("#mac").val();
	var statusIdString = Ext.getCmp("upgradeStatusSelect").getCheckedValue();
    baseStore.setBaseParam('jobId', jobId);
    baseStore.setBaseParam('sort', sort);
    baseStore.setBaseParam('dir', dir);
    baseStore.setBaseParam('start', 0);
    baseStore.setBaseParam('limit', pageSize);

    baseStore.setBaseParam('name', name);
    baseStore.setBaseParam('ip', ip);
    baseStore.setBaseParam('mac', mac);
    baseStore.setBaseParam('statusIds', statusIdString);
    baseStore.load();
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

$(function(){
	Ext.QuickTips.init();
	$("#topSideTip").hover(function(){
		if( tipDiv === null && $("#topTipDiv").length == 0){
			tipDiv = '<div id="topTipDiv">' + 
			'@batchUpgrade.upgradeTip1@<br>' + 
			    '@batchUpgrade.upgradeTip2@<br>' + 
			    '@batchUpgrade.upgradeTip3@<br>' + 
				"@batchUpgrade.upgradeTip4@" +
			'</div>'
			$("body").append(tipDiv);
		}else{
			$("#topTipDiv").stop(true, true).fadeIn('slow');
		}
	},function(){
		$("#topTipDiv").stop(true, true).fadeOut('slow');
	})
	sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 470;
	var baseColumns = [
	           		sm,
	           	    {header: '<div class="txtCenter">@COMMON.alias@</div>', menuDisabled: true, sortable : true, remoteSort: true, width: w * 0.25, align: 'left', dataIndex: 'name'},
	           		{header: "IP", width: 120, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'ip'},
	           		{header: "MAC", width: 120, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'mac'},
	                {header: "@batchTopo.entityType@", width: w * 0.15, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'typeName'},
	                //{header: "@batchUpgrade.upgradeSoftVersion@", width: w * 0.2, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'upgradeVersion', renderer:addCellTooltip},
	                {header: "@batchUpgrade.upgradeStatus@", width: w * 0.3, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'upgradeStatus', renderer : stateRender},
	                {header: "@batchUpgrade.upgradeTime@", width: w * 0.2, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'upgradeTime', renderer : upgradeTimeRender},
	                {header: "@batchTopo.retry@", width: w * 0.1, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'retryTimes'},
	           		{header: "@batchtopo.operate@", width: 200, dataIndex: 'entityId', renderer : manuRender}
	           	];
	
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/upgrade/getUpgradeEntity.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityId', 'name','ip', 'mac','typeName', 'retryTimes', 'upgradeVersion', 'upgradeStatus', 'upgradeTimeString', 'resultId','typeId', 'upgradeNote', 'upgradeTime', 'upgradeStatusString']
	});
	
	if(operationDevicePower){
		tbar = new Ext.Toolbar({
		    items: [
		      {text: '@sendConfig.addEntity@', id: 'showEntityList', iconCls: 'bmenu_new', handler: showEntityList}, 
		      '-', 
		      {text: '@batchUpgrade.upgradeImmediately@', id: 'upgradeNow', iconCls: 'bmenu_equipment', handler: upgradeNow}, 
		      '-', 
		      {text: '@batchUpgrade.upgradeImmediatelyAll@', id: 'upgradeAllNow', iconCls: 'bmenu_equipment', handler: upgradeAllNow}, 
		      '-', 
		      {text: '@resources/COMMON.delete@', id: 'deleteEntity', iconCls: 'bmenu_delete', handler: deleteEntity} , 
		      '-' ,"@batchUpgrade.upgradeVersion@",
		      {xtype: 'tbtext', text: versionName} 
		    ]
		  });
	}else{
		tbar = new Ext.Toolbar({
		    items: [
		      "@batchUpgrade.upgradeVersion@",
		      {xtype: 'tbtext', text: versionName} 
		    ]
		  });
	}
	
		
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 80;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		//title:"@batchUpgrade.upgradeEntityList@",
		cls: 'normalTable',
		border: true, 
		height: h,
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		sm: sm,
		tbar : tbar,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.setBaseParam('jobId', jobId);
	baseStore.setBaseParam('sort', sort);
	baseStore.setBaseParam('dir', dir);
	baseStore.setBaseParam('start', start);
	baseStore.setBaseParam('limit', pageSize);
	baseStore.load();
	
	/* // 初始化查询条件
	$.ajax({
		url: '/upgrade/getUpgradeStatusTree.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {text:"@COMMON.all@", value:"0",child:[]};
			response.unshift(all);
			var s = new Nm3kLevelSelect({
		         id : "upgradeStatus",
		         renderTo : "putSel",
		         width : 150,
		         subWidth : 300,
		         firstSelectValue : 0,
		         dataArr : response
		    })
		    s.init(); 
		}
	}); */
	var entityTypeCombox = new Ext.ux.form.LovCombo({
		id : 'upgradeStatusSelect',
		renderTo : "putSel",
		hideOnSelect : true,
		editable : false,
		width : 202,
		store : new Ext.data.JsonStore({
			url : "/upgrade/getUpgradeStatusTree.tv",
			root : 'data',
			autoLoad : true,
			fields : [ "id", "displayName" ]
		}),
		displayField : 'displayName',
		valueField : 'id',
		triggerAction : 'all',
		emptyText : '@sendConfig.pleaseChoose@',
		mode : 'local',
		beforeBlur : function() {
		}
	});
	
	new Ext.Viewport({
	    layout: 'border',
	    items: [baseGrid,
	            {region: 'north',
	    			contentEl:'topPart', 
	    			height:50,
	    			border:false
	    		}]
	});
	
})

function showEntityList(){
	window.parent.createDialog("modalDlg", "@sendConfig.chooseEntity@", 800, 500,
			"/upgrade/showEntityList.tv?jobId=" + jobId, null, true, true);
}

function upgradeAllNow(){
	var lastParams, mac, ip, name, statusIds;
	if(baseStore.lastOptions && baseStore.lastOptions.params){
		lastParams = baseStore.lastOptions.params;
		mac = lastParams.mac
		ip = lastParams.ip
		name = lastParams.name
		statusIds = lastParams.statusIds
	}
	window.parent.showConfirmDlg("@resources/COMMON.tip@", "@batchUpgrade.chooseEntityNumIs@" + baseStore.totalLength + "@batchUpgrade.chooseEntityConfrim@", function(button, text) {
		if (button == "yes") {
			$.ajax({
		        url: '/upgrade/upgradeAllNow.tv',
		        type: 'post',
		        dataType:"json",
		        data:{jobId: jobId,
		        	statusIds: statusIds,
		        	mac: mac,
		        	ip: ip,
		        	name: name,
		        	},
		        cache: false, 
		        success: function(response) {
		        	setTimeout(onRefreshClick,2000)
		        }, 
		        error: function(response) {
		        	
		        }
		    });	
		}
    });
}

function upgradeNow(){
	if(sm.getSelections().length==0){
        window.top.showMessageDlg("@resources/COMMON.tip@", "@offManagement.selectDevice@");
        return;
    }
	var rs=sm.getSelections();
    var entityIds=[];
    var length = rs.length;
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
    window.parent.showConfirmDlg("@resources/COMMON.tip@", "@batchUpgrade.chooseEntityNumIs@" + length + "@batchUpgrade.chooseEntityConfrim@", function(button, text) {
		if (button == "yes") {
			$.ajax({
		        url: '/upgrade/upgradeNow.tv',
		        type: 'post',
		        dataType:"json",
		        data:{jobId: jobId,
		        	entityIds: entityIds},
		        cache: false, 
		        success: function(response) {
		        	setTimeout(onRefreshClick,2000)
		        }, 
		        error: function(response) {
		        	
		        }
		    });	
		}
    });
}

function upgradeSingleNow(entityId, jobId){
	window.parent.showConfirmDlg("@resources/COMMON.tip@", "@batchUpgrade.upgradeImmediately@?", function(button, text) {
		if (button == "yes") {
    $.ajax({
        url: '/upgrade/upgradeSingleNow.tv',
        type: 'post',
        dataType:"text",
        data:{jobId: jobId,
        	entityId: entityId},
        cache: false, 
        success: function(response) {
        	if(response == 'upgradeNow'){
        		window.parent.showMessageDlg('@resources/COMMON.tip@', '@batchUpgrade.upgradeNowRetryLater@');
        	}else{
        		setTimeout(onRefreshClick,2000)
        	}
        }, 
        error: function(response) {
        	
        }
    });
		}});
	
}

function setRefreshTime(){
	refreshTime = $("#refreshTime").val();
	if(refreshTime > 0){
		clearInterval(interval); 
		interval = setInterval("onRefreshClick()", refreshTime * 1000);
	}else{
		clearInterval(interval); 
	}
}


Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});

function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
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

function deleteSingleEntity(){
	deleteEntity();
}
function deleteEntity(){
	if(sm.getSelections().length==0){
        window.top.showMessageDlg("@resources/COMMON.tip@", "@offManagement.selectDevice@");
        return;
    }
	window.parent.showConfirmDlg("@resources/COMMON.tip@", "@resources/COMMON.confirmDelete@?", function(button, text) {
		if (button == "yes") {
	var rs=sm.getSelections();
    var entityIds=[];
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
	$.ajax({
        url: '/upgrade/deleteJobEntity.tv',
        type: 'post',
        dataType:"json",
        data:{'entityIds':entityIds.join("$"),
        	jobId: jobId},
        cache: false, 
        success: function(response) {
            window.top.getActiveFrame().onRefreshClick();
        }, 
        error: function(response) {
        }
    });
		}});
}
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10">
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable" width="100%">
			<tr>
			<td class="rightBlueTxt w60">@batchUpgrade.upgradeStatus@:</td>
								<td width="210" id="putSel"></td>
				<td class="rightBlueTxt w60">@NETWORK.alias@:</td>
								<td width="100">
									<input type="text" id="entityName" name="entityName" class="normalInput w100" />
								</td>
								<td class="rightBlueTxt w50">IP:</td>
								<td width="100">
									<input type="text" id="ip" name="ip" class="normalInput w100" />
								</td>
								<td class="rightBlueTxt w50">MAC:</td>
								<td width="100">
									<input type="text" id="mac" name="mac" class="normalInput w100" /></td>
				<td width="3"> 
				</td>   
				<td> 
					<a href="javascript:;" class="normalBtn" onclick="searchClick()"><span><i class="miniIcoSearch"></i>@resources/COMMON.query@</span></a>                  
				</td>
				<td>
				</td>
				<td width="50" align="right">
					<img id="topSideTip" src="../images/performance/Help.png" class="cursorPointer pR5" />
				</td>
			</tr>
		</table>
	</div>
	<div style="position: absolute; top:50px; right:10px; margin:3px">
		<span class="blueTxt">@sendConfig.refreshTime@@resources/COMMON.maohao@</span><select id="refreshTime" class="normalSel w100" onchange="setRefreshTime()"> 
				<option value="0">@sendConfig.manualRefresh@</option> 
				<option value="5">5@sendConfig.second@</option>
				<option value="10">10@sendConfig.second@</option> 
				<option value="30">30@sendConfig.second@</option> 
				<option value="60">60@sendConfig.second@</option>
			</select>
	</div>
</body>
</Zeta:HTML>