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
    plugin DropdownTree
    module  network
    import js/nm3k/nm3kPickDate
    css css/white/disabledStyle
    PLUGIN DropdownTree
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var baseGrid;
var start = 0;
var folderId = "";
var baseStore;
var dir = "DESC";
var sort = "dt"
var sm;
var interval;
var refreshTime;
var entityTypes = ${entityTypes};
var state = "";
var entityType = "";
var entityTypeEl
var regionTree;

function onRefreshClick(){
	searchClick()
}
function onRefreshPage(){
	window.location.reload();
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ],listeners:{
    	beforechange :function(){
    		$("#deleteBtn").attr({disabled : "disabled"});
    	}
    }});
    return pagingToolbar;
}

function manuRender(value, p, record) {
	var entityId = record.data.entityId;
    return String.format("<a href='javascript:;' onClick='showResult(" + entityId + ")'>@sendConfig.viewResult@</a> / <a href='javascript:;' onClick='sendConfigById()'>@sendConfig.sendConfig@</a> / <a href='javascript:;' onClick='deleteSingleEntity()'>@COMMON.delete@</a>");
}

function stateRender(value, p, record) {
	var state = record.data.state;
	if(state == 1){
		return String.format('<font color="green">@sendConfig.notSend@</font>');
	}else if(state == 2){
		return String.format('<font color="#C07877">@sendConfig.loginFail@</font>');
	}else if(state == 3){
		return String.format('<font color="red">@sendConfig.sendFail@</font>'); 
	}else if(state == 4){
		return "@sendConfig.sendSuccess@";
	}else if(state == 5){
		return String.format('<font color="#C098F0">@sendConfig.writeFail@</font>');
	}else if(state == 6){
		return String.format('<font color="#C098F0">@sendConfig.waitWrite@</font>');
	}return "";
}

function dtRender(value, p, record) {
	var state = record.data.state;
	if(state == 1){
		return String.format('<font color="green">@sendConfig.notSend@</font>');
	}else{
		return record.data.dtString;
	}
}

function showResult(entityId){
	window.top.createDialog('result', "@sendConfig.sendResult@", 800, 500,'/entity/commandSend/showCommandResult.tv?entityId=' + entityId, null, true, true);
}

function searchClick(){
	folderId = regionTree.getSelectedIds();
	state = $("#sendStatus").val();

    var typeIds = entityTypeEl.getSelectedIds();
    if (typeIds.length > 0) {
        entityType = typeIds[0];
    } else {
        entityType = null;
    }
	
	baseStore.setBaseParam('regionId', folderId);
	baseStore.setBaseParam('state', state);
	baseStore.setBaseParam('typeId', entityType);
	baseStore.setBaseParam('sort', sort);
	
    baseStore.load({params: {sort: sort, dir:dir, start: start, limit:pageSize},callback:function(){
    	disabledToolbarBtn(baseGrid.getSelectionModel().getSelections().length);
    }});
}
function disabledToolbarBtn(num){
	var $deleteBtn = $("#deleteBtn")
	if(num > 0){
		$deleteBtn.removeAttr("disabled");
	}else{
		$deleteBtn.attr({disabled : "disabled"});
	}
}
$(function(){
	Ext.QuickTips.init();
	sm = new Ext.grid.CheckboxSelectionModel({
		listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(baseGrid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(baseGrid.getSelectionModel().getSelections().length);
	        }
	    }	
	}); 
	var w = $(window).width() - 30;
	var baseColumns = [
	           		sm,
	           	    {header: "@NETWORK.alias@",  width: parseInt(w/8), align: 'center', dataIndex: 'name'},
	           		{header: "@resources/COMMON.uplinkDevice@", width: parseInt(w/10), align: 'center', menuDisabled: false, hideable : false, sortable : true, remoteSort: true, 
	           	    	dataIndex: 'uplinkDevice'},
	           		{header: "MAC", width: parseInt(w/10), align: 'center', menuDisabled: false, hideable : false, sortable : true, remoteSort: true, dataIndex: 'mac'},
	                {header: "@batchTopo.entityType@", width: parseInt(w/10), align: 'center', dataIndex: 'typeName'},
	                {header: "@sendConfig.entityFolder@", width: parseInt(w/10), align: 'center', dataIndex: 'folderName'},
	                {header: "@sendConfig.sendstate@", width: parseInt(w/10), align: 'center', dataIndex: 'state', renderer : stateRender},
	                {header: "@sendConfig.sendTime@", width: parseInt(w/8), align: 'center', menuDisabled: false, hideable : false, sortable : true, 
	                	remoteSort: true, dataIndex: 'dt', renderer : dtRender},
	           		{header: "@batchtopo.operate@", width:parseInt(w/6), dataIndex: 'resultId', fixed:true, renderer : manuRender}
	           	];
	
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/entity/commandSend/loadCommandSendEntityList.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityId', 'name','ip','uplinkDevice','ipString', 'mac','typeName', 'folderName', 'state', 'dt', 'dtString', 'resultId']
	});
	
		
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 80;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		title:"@sendConfig.entityList@",
		cls: 'normalTable',
		border: true, 
		height: h,
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		sm: sm,
		renderTo:'grid',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load({params:{sort: sort, dir:dir, start: start, limit: pageSize}});
	
	/*new Ext.Viewport({
	    layout: 'border',
	    items: [baseGrid,
	            {region: 'north',
	    			contentEl:'topPart', 
	    			height:50,
	    			border:false
	    		}]
	});*/
	regionTree = $('#region_tree').dropdowntree({
		width: 200
	}).data('nm3k.dropdowntree');
	regionTree.checkAllNodes();

    resize();

    $(window).bind('resize', function(e){
        throttle(resize, 50)();
    });

    function resize(){
        $('#grid').height($('body').height()-$('#topPart').outerHeight());
        baseGrid.setSize($('#grid').width(),$('#grid').height());
    }

    function throttle(fn, delay){
        var timer = null;
        return function(){
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function(){
                fn.apply(context, args);
            }, delay);
        }
    }
	
	buildEntityTypeSelect();
})

function showEntityList(){
	window.parent.createDialog("modalDlg", "@sendConfig.chooseEntity@", 1000, 600,
			"/entity/commandSend/showEntityList.tv", null, true, true);
}

function openTopFolder() {
	window.top.createDialog('topoFolderTree', '@resources/SYSTEM.selectRegion@', 800, 500,
			'network/chooseFolderTree.jsp', null, true, true, function() {
				var callbackObj = window.top.ZetaCallback
				if (callbackObj == null || callbackObj.type != 'ok') {
					return;
				}
				var selectedItemId = callbackObj.selectedItem.itemId
				var selectedItemName = callbackObj.selectedItem.itemName
				if (selectedItemId == null) {
					return
				}
				$("#region").attr("value", selectedItemName).attr("name",
						selectedItemId);
				window.top.ZetaCallback = null
			})
}

function sendConfig(){
	$.ajax({
		  url: '/entity/commandSend/sendConfig.tv',
	      type: 'post',
	      success: function(response) {
	    	  window.parent.showMessageDlg(I18N.COMMON.tip, "@sendConfig.sendConfigTip@");
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, "@sendConfig.configSendFail@!");
			}, cache: false
		});
}

function sendConfigById(){
	var se = getSelectedEntity();
	if(se){
		$.ajax({
			url: '/entity/commandSend/makeSendConfigById.tv',
		    type: 'post',
		    data: {entityId: se.entityId},
		    success: function(response) {
		    	window.parent.showMessageDlg(I18N.COMMON.tip, "@sendConfig.sendConfigTip@");
			}, 
			error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, "@sendConfig.configSendFail@!");
			}, 
			cache: false
		});
	}
}

function getSelectedEntity() {
	var sm = baseGrid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data;
	}
	return null;
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
        window.top.showMessageDlg(I18N.COMMON.tip, "@offManagement.selectDevice@");
        return;
    }
	window.parent.showConfirmDlg("@COMMON.tip@", "@resources/COMMON.confirmDelete@@COMMON.wenhao@", function(button, text) {
		if (button == "yes") {
	var rs=sm.getSelections();
    var entityIds=[];
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
	
	$.ajax({
        url: '/entity/commandSend/deleteSendConfigEntity.tv',
        type: 'post',
        dataType:"json",
        data:{'entityIds':entityIds.join("$")},
        cache: false, 
        success: function(response) {
            if(response.result == "true"){
            }else{
            }
            window.top.getActiveFrame().onRefreshClick();
        }, 
        error: function(response) {
        }
    });
		}});
}
function buildEntityTypeSelect(){
	/*var deviceTypePosition = Zeta$('entityType');
	for(var i = 0; i < entityTypes.length; i++){
        var option = document.createElement('option');
        option.value = entityTypes[i].typeId;
        option.text = entityTypes[i].displayName;
        try {
        	deviceTypePosition.add(option, null);
        } catch(ex) {
        	deviceTypePosition.add(option);
        }
  }*/
    entityTypeEl = $('#entityType').dropdowntree({
//        url: '/topology/fetchLogonUserAuthFolders.tv',
        url: '/entity/commandSend/entityTypeTree.tv',
        multi: false,
        width: 230
    }).data('nm3k.dropdowntree');
//    var nodes = entityTypeEl.getCheckedNodes();
}

function authload(){
    if(!operationDevicePower){
        $("#sendConfig").attr("disabled",true);
    }
}
</script>
</head>
<body class="whiteToBlack" onload="authload()">
	<div id="topPart" class="edge10">
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt w70">@sendConfig.sendstate@:</td>
				<td><select id="sendStatus" class="normalSel w120">
						<option value="">@sendConfig.pleaseChoose@</option>
						<option value="1">@sendConfig.notSend@</option>
						<option value="2">@sendConfig.loginFail@</option>
						<option value="3">@sendConfig.sendFail@</option>
						<option value="5">@sendConfig.writeFail@</option>
						<option value="4">@sendConfig.sendSuccess@</option>
				</select>
				</td>
				<td class="rightBlueTxt w70">@batchTopo.entityType@:</td>
				<td><%--<select id="entityType" class="normalSel w160">
						<option value="">@sendConfig.pleaseChoose@</option>
						&lt;%&ndash; <% if(uc.hasSupportModule("olt")){%>
						<option value="10001">PN8601</option>
						<option value="10002">PN8602</option>
						<option value="10003">PN8603</option>
							<% if(uc.hasSupportModule("cmc")){%>
							<option value="30001">8800A</option>
							<option value="30005">8800C-A</option>
							<%}%>
						<%}%>
						<% if(uc.hasSupportModule("cmc")){%>
						<option value="30002">8800B</option>
						<option value="30006">8800C-B</option>
						<option value="30004">8800D</option>
						<option value="30007">8800S</option>
						<%}%> &ndash;%&gt;
				</select>--%>
                    <div id="entityType"/>
				</td>
				<td class="rightBlueTxt w70">@sendConfig.entityFolder@:</td>
				<td width="170"><div id="region_tree"></div></td>
				<td class="pL5">     
					<a href="javascript:;" class="normalBtn" onclick="searchClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>                  
				</td>
				<td width="3">
				</td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="showEntityList()"><span><i class="miniIcoAdd"></i>@sendConfig.addEntity@</span></a>                  
				</td>
				<td width="3">
				</td>
				<td class="rightBlueTxt">    
					<a id="sendConfig" href="javascript:;" class="normalBtn" onclick="sendConfig()"><span><i class="miniIcoApply"></i>@sendConfig.sendNow@</span></a>                  
				</td>
				<td width="3">
				</td>
				<td class="rightBlueTxt">    
					<a id="deleteBtn" disabled="disabled" href="javascript:;" class="normalBtn" onclick="deleteEntity()"><span><i class="miniIcoClose"></i>@COMMON.delete@</span></a>                  
				</td>
			</tr>
		</table>
	</div>
    <div id="grid"></div>
	<div style="position: absolute; top:50px; right:10px;">
		<span class="blueTxt">@sendConfig.refreshTime@@COMMON.maohao@</span><select id="refreshTime" class="normalSel w100" onchange="setRefreshTime()"> 
				<option value="0">@sendConfig.manualRefresh@</option> 
				<option value="5">5@sendConfig.second@</option>
				<option value="10">10@sendConfig.second@</option> 
				<option value="30">30@sendConfig.second@</option> 
				<option value="60">60@sendConfig.second@</option>
			</select>
	</div>
</body>
</Zeta:HTML>