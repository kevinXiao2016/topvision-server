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
    plugin DropdownTree
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var folderId = "";
var ip = "";
var mac = "";
var typeId = "";
var createTime = "";
var endTime = "";
var baseGrid;
var sm;
var dir = "DESC";
var sort = "createTime"
var regionTree;
	var entityTypes = ${entityTypes};
var entityTypeEl;
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ]});
    return pagingToolbar;
}

function closeClick() {
	window.parent.closeWindow('modalDlg');
}

function save(){
	if(sm.getSelections().length==0){
        window.top.showMessageDlg(I18N.COMMON.tip, "@offManagement.selectDevice@");
        return;
    }
	
	var rs=sm.getSelections();
    var entityIds=[];
    for(var i = 0; i < rs.length; i++){
    	entityIds[i]=rs[i].data.entityId;
    }
	
	$.ajax({
        url: '/entity/commandSend/addSendConfigEntity.tv',
        type: 'post',
        dataType:"json",
        data:{'entityIds':entityIds.join("$")},
        cache: false, 
        success: function(response) {
            if(response.result == "true"){
            }else{
            }
            window.top.getActiveFrame().onRefreshClick();
            closeClick();
        }, 
        error: function(response) {
        	closeClick();
        }
    });
}

function getMonthStartDate(){ 
	var now = new Date(); //当前日期 
	var nowDayOfWeek = now.getDay(); //今天本周的第几天 
	var nowDay = now.getDate(); //当前日 
	var nowMonth = now.getMonth(); //当前月 
	var nowYear = now.getYear(); //当前年 
	nowYear += (nowYear < 2000) ? 1900 : 0; //
	var monthStartDate = new Date(nowYear, nowMonth, 1); 
	return formatDate(monthStartDate); 
}

function getWeekStartDate() { 
	var now = new Date(); //当前日期 
	var nowDayOfWeek = now.getDay(); //今天本周的第几天 
	var nowDay = now.getDate(); //当前日 
	var nowMonth = now.getMonth(); //当前月 
	var nowYear = now.getYear(); //当前年 
	nowYear += (nowYear < 2000) ? 1900 : 0; //

	var lastMonthDate = new Date(); //上月日期 
	lastMonthDate.setDate(1); 
	lastMonthDate.setMonth(lastMonthDate.getMonth()-1); 
	var lastYear = lastMonthDate.getYear(); 
	var lastMonth = lastMonthDate.getMonth();
	var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek); 
	return formatDate(weekStartDate); 
}

//格局化日期：yyyy-MM-dd 
function formatDate(date) { 
	var myyear = date.getFullYear(); 
	var mymonth = date.getMonth()+1; 
	var myweekday = date.getDate();
	if(mymonth < 10){ 
		mymonth = "0" + mymonth; 
	} 
	if(myweekday < 10){ 
		myweekday = "0" + myweekday; 
	} 
	return (myyear+"-"+mymonth + "-" + myweekday); 
}

//获取今天，昨天，明天;
function getDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    if(m.toString().length == 1){m = "0"+m;}
    var d = dd.getDate();
    if(d.toString().length == 1){d = "0"+d;}
    return y + "-" + m + "-" + d;
}

function searchClick(){
	folderId = regionTree.getSelectedIds();
	name = $("#alias").val();
	mac = $("#mac").val();
	ip = $("#ip").val();
	var typeIds = entityTypeEl.getSelectedIds();
    if (typeIds.length > 0) {
        typeId = typeIds[0];
    } else {
        typeId = null;
    }
	var time = $("#time").val();
	var dd = new Date();
	if(time == 0){
	    createTime = getDateStr(0);
	    endTime = ""
	}else if(time == 1){
		createTime = getDateStr(-1);
		endTime = getDateStr(0);
	}else if(time == 2){
		createTime = getWeekStartDate()
		endTime = ""
	}else if(time == 3){
		createTime = getMonthStartDate()
		endTime = ""
	}else {
		createTime = ""
		endTime = ""
	}
    baseStore.setBaseParam('regionId', folderId);
    baseStore.setBaseParam('mac', mac);
    baseStore.setBaseParam('ip', ip);
    baseStore.setBaseParam('typeId', typeId);
    baseStore.setBaseParam('name', name);
    baseStore.setBaseParam('createTime', createTime);
    baseStore.setBaseParam('endTime', endTime);
    baseStore.setBaseParam('sort', sort);
    baseStore.load({params: {regionId: folderId, mac: mac, ip: ip, typeId: typeId, name: name, createTime: createTime, endTime: endTime, sort: sort, dir:dir, start:0, limit:pageSize}});
}

function renderCreateTime(value, p, record){
	return record.data.sysUpTime;
} 

$(function(){
	Ext.QuickTips.init();
	sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = $(window).width() - 30;
	var baseColumns = [
	           		sm,
	           	    {header: "@NETWORK.alias@",  width: parseInt(w/8), align: 'center', dataIndex: 'name'},
	           		{header: "@resources/COMMON.uplinkDevice@", width: parseInt(w/8), menuDisabled: false, hideable : false, sortable : true, remoteSort: true, align: 'center', dataIndex: 'uplinkDevice'},
	           		{header: "MAC", width: parseInt(w/8), align: 'center', dataIndex: 'mac'},
	                {header: "@batchTopo.entityType@", width: parseInt(w/8), align: 'center', dataIndex: 'typeName'},
	                {header: "@sendConfig.entityFolder@", width: parseInt(w/8), align: 'center', dataIndex: 'location'},
	                {header: "@batchtopo.createtime@", width: parseInt(w/5), menuDisabled: false, hideable : false, sortable : true, remoteSort: true, 
	                		align: 'center', dataIndex: 'createTime', renderer : renderCreateTime}
	           	];
	
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/entity/commandSend/loadEntityList.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['entityId', 'name','ip','uplinkDevice', 'mac','typeName', 'location', 'createTime', 'sysUpTime']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 90;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		border: true, 
		height: h - 50,
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
	
	baseStore.load({params:{sort: sort, dir:dir, start:0, limit: pageSize}});
	
	regionTree = $('#region_tree').dropdowntree({
		width: 160
	}).data('nm3k.dropdowntree');
	regionTree.checkAllNodes();
	
	buildEntityTypeSelect();
	
})
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
/*	var deviceTypePosition = Zeta$('entityType');
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
        width: 200
    }).data('nm3k.dropdowntree');
}
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10">
		<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
			<tr>
				<td class="rightBlueTxt w60">@NETWORK.alias@:</td>
				<td width="160">
					<input type="text" id="alias" name="alias" value="" class="normalInput w160" />
				</td>
				<td class="rightBlueTxt w100">@resources/COMMON.uplinkDevice@:</td>
				<td width="130">
					<input type="text" id="ip" name="ip" value="" class="normalInput w120" />
				</td>
				<td class="rightBlueTxt w100">MAC:</td>
				<td width="238">
					<input type="text" id="mac" name="mac" value="" class="normalInput" style="width:200px" />
				</td>
				<td rowspan="2" class="rightBlueTxt">    
					<a href="javascript:;" class="normalBtn" onclick="searchClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>                  
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt w100">@sendConfig.entityFolder@:</td>
				<td width="170"><div id="region_tree"></div></td>
                <td class="rightBlueTxt w100">@batchtopo.createtime@:</td>
				<td><select id="time" class="normalSel w120">
						<option value="-1">@sendConfig.pleaseChoose@</option>
						<option value="0">@sendConfig.today@</option>
						<option value="1">@sendConfig.yesteday@</option>
						<option value="2">@sendConfig.thisWeek@</option>
						<option value="3">@sendConfig.thisMonth@</option>
				</select>
				</td>
				<td class="rightBlueTxt w60">@batchTopo.entityType@:</td>
				<td>
					<div id="entityType"/>
				</td>
			</tr>
		</table>
	</div>
	<div id="grid" style="padding:5px"></div>
	<div class="edgeTB10LR20 pT5">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id=saveButton href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>