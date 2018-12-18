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
    module  network
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var regionId = "";
var ip = "";
var mac = "";
var typeId = "";
var createTime = "";
var endTime = "";
var baseGrid;
var sm;
var dir = "DESC";
var sort = "createTime"
var jobId = ${ jobId };
var entityTypes = ${entityTypes};
var entityType = ${ entityType };

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format("@resources/COMMON.displayPerPage@", pageSize), '-'
    ]});
    return pagingToolbar;
}

function closeClick() {
	window.parent.closeWindow('modalDlg');
}

function save(){
	if(sm.getSelections().length==0){
        window.top.showMessageDlg("@resources/COMMON.tip@", "@offManagement.selectDevice@");
        return;
    }
	
	var rs=sm.getSelections();
    var entityIds=[];
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
	
	$.ajax({
        url: '/upgrade/addEntityToJob.tv',
        type: 'post',
        dataType:"json",
        data:{'entityIds':entityIds.join("$"),
        	jobId : jobId},
        cache: false, 
        success: function(response) {
            window.top.getActiveFrame().onRefreshClick();
            closeClick();
        }, 
        error: function(response) {
        	closeClick();
        }
    });
}

function searchClick(){
	name = $("#alias").val();
	mac = $("#mac").val();
	ip = $("#ip").val();
	typeId = $("#entityType").val();
	if(typeId > 0){
		baseStore.load({params: {mac: mac, ip: ip, typeId: typeId, entityType: entityType, name: name, jobId: jobId, sort: sort, dir:dir, start:0, limit:pageSize}});
	}else{
		baseStore.load({params: {mac: mac, ip: ip, typeId: typeId, entityType: entityType, name: name, jobId: jobId, sort: sort, dir:dir, start:0, limit:pageSize}});
	}
}

function renderCreateTime(value, p, record){
	return record.data.sysUpTime;
} 

function renderSysStatus(value, p, record) {
    if (record.data.status == '1') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                "@label.online@");
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                "@resources/COMMON.offline@");
    }
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

$(function(){
	Ext.QuickTips.init();
	sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 230;
	var baseColumns = [
	           		sm,
	           		{header: "@resources/SYSTEM.status@", width: parseInt(w/15), align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'status', renderer: renderSysStatus},
	           	    {header: '<div class="txtCenter">@COMMON.alias@</div>',  width: parseInt(w/8), align: 'left', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'name'},
	           		{header: "IP", width: 100, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'ip'},
	           		{header: "MAC", width: 100, align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'mac'},
	                {header: "@batchTopo.entityType@", width: parseInt(w/8), align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'typeName'},
	                {header: "@batchUpgrade.softVersion@", width: parseInt(w/8), align: 'center', menuDisabled: true, sortable : true, remoteSort: true, dataIndex: 'softwareVersion', renderer:addCellTooltip},
	                {header: "@batchtopo.createtime@", width: parseInt(w/5), menuDisabled: true, sortable : true, remoteSort: true, align: 'center', dataIndex: 'createTime', renderer : renderCreateTime}
	           	];
	
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/upgrade/getEntity.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityId', 'name','ip', 'mac','typeName', 'status', 'createTime', 'sysUpTime', 'softwareVersion']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 90;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		border: true, 
		height: h - 30,
		renderTo: "grid",
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		sm: sm,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load({params:{entityType: entityType, sort: sort, dir:dir, jobId: jobId, start:0, limit: pageSize}});
	
	buildEntityTypeSelect();
	
})
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

function buildEntityTypeSelect(){
	var deviceTypePosition = Zeta$('entityType');
	for(var i = 0; i < entityTypes.length; i++){
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
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge5">
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt w30">@NETWORK.alias@:</td>
				<td width="120">
					<input type="text" id="alias" name="alias" value="" class="normalInput w100" />
				</td>
				<td class="rightBlueTxt w30">IP:</td>
				<td width="120">
					<input type="text" id="ip" name="ip" value="" class="normalInput w100" />
				</td>
				<td class="rightBlueTxt w30">MAC:</td>
				<td width="120">
					<input type="text" id="mac" name="mac" value="" class="normalInput w100" />
				</td>
				<td class="rightBlueTxt">@batchTopo.entityType@:</td>
									<td>
									<select id="entityType" class="normalSel w150">
										<option value="0">@sendConfig.pleaseChoose@</option>
									</select>
								</td>
				<td width="3">
				</td>
				<td class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="searchClick()"><span><i class="miniIcoSearch"></i>@resources/COMMON.query@</span></a>                  
				</td>
			</tr>
		</table>
	</div>
	<div id="grid" style="padding:5px"></div>
	<div class="edgeTB10LR20 pT5">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id=saveButton href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@resources/COMMON.ok@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@resources/COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>