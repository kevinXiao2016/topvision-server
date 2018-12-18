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
    module resources
</Zeta:Loader>
<style type="text/css">
.normalTable table td.x-grid3-body-cell { height:auto;}
.normalTable table a:hover, .normalTable table a:hover b{ color:#fff !important; color:#fff;}
</style>
<script type="text/javascript">
// for tab changed start
var folderList = new Array();
var grid = null;
var store;
var data = new Array();
var fields = ['totle'];

function tabActivate() {
	window.top.setStatusBarInfo('', '');
	onRefreshClick();
}
function tabDeactivate() {
	//doOnunload();
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

var grid = null;
var store = null;
function openFolder(id, name) {
	//window.top.addView("topo" + id, name, "topoLeafIcon", "topology/getTopoMapByFolderId.tv?folderId=" + id);
	window.top.addView("topo" + id, name, "topoLeafIcon", "topology/showNewTopoDemo.tv?folderId=" + id);
}
function renderName(value, p, record){
	return String.format('<a href="#" onclick="openFolder({0}, \'{1}\')">{2}</a>',
		record.data.folderId, value, value);
}
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selection = sm.getSelected();
		var folderId = selection.data.folderId;
	} else {
		showMessageDlg('@COMMON.tip@', '@topoFolder.deleteTip@');
	}
}
function onRefreshClick() {
	data = new Array();
	init();
}
Ext.BLANK_IMAGE_URL = '../images/s.gif';

function loadGrid(){
	store = new Ext.data.SimpleStore({
	    data : data,
	    fields: fields
	});

	var tmpWidth = parseInt((document.body.offsetWidth - 30)/fields.length);
	if(tmpWidth < 30){
		tmpWidth = 30;
	}
    var cm = new Ext.grid.ColumnModel([]);
    
	/* cm.config.append({header: '@network/topoFolder.areaID@', id: 'id1', dataIndex:'folderId', width: tmpWidth, align:'center',sortable: false, menuDisabled: true}); */
	cm.config.append({header: '<div class="txtCenter">@network/topoFolder.areaName@</div>', id: 'id2', dataIndex:'name', width: tmpWidth, align:'left',sortable: false,menuDisabled: true, renderer:nameRender});
	for(var k=1; k<fields.length; k++){
		if(fields[k] != "name" && fields[k] != "folderId" && fields[k] != "totle"){
			cm.config.append({header: fields[k], id: fields[k], dataIndex:fields[k], width: tmpWidth, align:'center',sortable: false,menuDisabled: true});
		}
	}
	cm.config.append({header: '@network/topoFolder.total@', id: 'id3', dataIndex:'totle', width: tmpWidth, align:'center',sortable: false,menuDisabled: true});	
	/* cm.config.append({header: '@COMMON.opration@', dataIndex:'operate', width: tmpWidth, align:'center',sortable: false,menuDisabled: true, renderer:renderOperate});	 */
	
    cm.defaultSortable = false;
    
    var tbar= [
		{text: '@COMMON.create@', handler: onNewTopoFolderClick,cls:'mL10', iconCls:'bmenu_new'}, '-',
   		{text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: onRefreshClick}]
    
	grid = new Ext.grid.GridPanel({
		border:false, 
		store:store,
		cm:cm,
        viewConfig: {forceFit:true},
        tbar:tbar,
        bodyCssClass:"normalTable",
		renderTo: document.body
    });

   /*  grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);  // Get the Record
        openFolder(record.data.folderId, record.data.name);		
    }); */

    new Ext.Viewport({layout:'fit', items:[grid]});
}
function nameRender(value, p, record){
	var name = record.data.name;
	var nameArray = name.split("/");
	var realName = nameArray[nameArray.length-1];
	var pathStr = '';
	for(var i=0; i<nameArray.length-1; i++){
		pathStr += nameArray[i] + " / "
	}
	pathStr += '<b style="color:#f60;">'+ realName +'</b>';
	return String.format('<a style="padding-left:5px; color:#888;" href="javascript:void(0)" onclick="gotoUrl({0},\'{1}\')">{2}</a>',record.data.folderId, realName,pathStr);
}

function renderOperate(value, p, record){
	return String.format('<a href="javascript:void(0)" onclick="gotoUrl({0},\'{1}\')">@COMMON.view@</a>',record.data.folderId, record.data.name);	
};
function gotoUrl(fId,dName){
	openFolder(fId, dName);	
}

Ext.onReady(function(){
  	init();
});
function init(){
	Ext.Ajax.request({
		url : '../topology/statTopoFolderCols.tv',
		method : "post",
		success:function(response){
			var res = Ext.decode(response.responseText);
			var pLength = res.length;
			for(var k=0; k<pLength; k++){
				fields[k + 1] = res[k];
			}
			Ext.Ajax.request({
				url : '../topology/statTopoFolder.tv',
				method : "post",
				success:function(response){
					var res = Ext.decode(response.responseText);
					var pLength = res.length;
					for(var k=0; k<pLength; k++){
						data[k] = new Array();
						var tmpTotle = 0;
						for(var x in res[k]){
							data[k][fields.indexOf(x)] = res[k][x];
							if(x != "name" && x != "folderId"){
								tmpTotle = tmpTotle + parseInt(res[k][x]);
							}
						}
						data[k][0] = tmpTotle;
					}
					if(grid == null){
						loadGrid();
					}else{
						store.loadData(data);
					}
				},failure:function (response) {					
		            window.parent.showMessageDlg('@COMMON.tip@','@network/topoFolder.dataError@');
		        }
		  	});
		},failure:function (response) {
            window.parent.showMessageDlg('@COMMON.tip@','@network/topoFolder.dataError@');
        }
  	});
}
function onNewTopoFolderClick() {
	window.top.createDialog("modalDlg", '@MAIN.newTopoFolder@', 800, 500, "topology/newTopoFolderJsp.tv", null, true, true);
}
</script>
</head>
<body class="whiteToBlack">
</body>
</Zeta:HTML>
