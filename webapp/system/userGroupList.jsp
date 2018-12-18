<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css">
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
</head><body class=BLANK_WND style="padding: 10px;" onresize="doOnresize();">
<center>
<table width=780px cellspacing=0 cellpadding=0>
<tr><td width=350px height=30px><fmt:message bundle="${sys}" key="sys.groupList" />:</td>
<td width=30>&nbsp;</td>
<td>	
	<div class=ultab>
		<ul>
			<li id="folderTab" class=selected><a href="javascript: setActiveTab('folder');"><fmt:message bundle="${sys}" key="sys.topoFolder" /></a></li>
		</ul>
	</div>
</td></tr>

<tr><td><div id="groupList-div"></div></td>
<td></td>
<td width=400px valign=top>
	<div id="tree-div" class=TREE-CONTAINER></div>
</td></tr>

<tr>
<td height=35>
<button type="button" class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
  onMouseDown="this.className='BUTTON_PRESSED95'"
  onMouseOut="this.className='BUTTON95'" onclick="newUserGroup()"><fmt:message bundle="${sys}" key="sys.newUserGroup" /></button>&nbsp;<button 
  type="button" class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
  onMouseDown="this.className='BUTTON_PRESSED95'"
  onMouseOut="this.className='BUTTON95'" onclick="deleteUserGroup()"><fmt:message bundle="${sys}" key="sys.removeUserGroup" /></button>
</td><td></td>
<td align=right>
<button type="button" class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
  onMouseDown="this.className='BUTTON_PRESSED95'"
  onMouseOut="this.className='BUTTON95'" onclick="saveGroupPower()"><fmt:message bundle="${sys}" key="sys.saveGroupPro" /></button>&nbsp;
<button type="button" class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
  onMouseDown="this.className='BUTTON_PRESSED95'"
  onMouseOut="this.className='BUTTON95'" onclick="onRefreshClick()"><fmt:message bundle="${sys}" key="sys.refresh" /></button>    
</td></tr>
</table></center>
<script type="text/javascript">
function newUserGroup() {
	window.top.createDialog("modalDlg", '<fmt:message bundle="${sys}" key="sys.newUserGroup" />', 420, 300, 'system/newUserGroup.jsp', null, true, true);
}

function deleteUserGroup() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		window.top.showConfirmDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.confirmRemoveUG" />', function (type) {
			if (type == "no") {
				return;
			}
			Ext.Ajax.request({url: "deleteUserGroup.tv", success:function () {
				grid.getStore().reload({callback: loadGroupCallback});
			}, failure:function () {
				window.top.showErrorDlg();
			}, params:{userGroupIds: [r.data.userGroupId]}});
		});
	} else {
		window.top.showMessageDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.selUG" />');
	}
}

function onRefreshClick() {
	grid.getStore().reload({callback: loadGroupCallback});
}
function doOnresize() {
	var w = document.body.clientWidth - 295;
	var h = document.body.clientHeight - 90;
	if (h < 200) {
		h = 200;
	}
	grid.setHeight(h);
	Zeta$('tree-div').style.height = h;
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
var grid;
var tree2;
var curUserGroup = 0;
var curIndex = "folder";
function setActiveTab(index) {
	if (curIndex == index) {
		return;
	}
	document.getElementById(curIndex + 'Tab').className = '';
	document.getElementById(index + 'Tab').className = 'selected';
	curIndex = index;
	refreshFolder(curUserGroup);
}

Ext.onReady(function() {
	var w = document.body.clientWidth - 295;
	var h = document.body.clientHeight - 90;
	if (h < 200) {
		h = 200;
	}
	Zeta$('tree-div').style.height = h;
    
	var store = new Ext.data.JsonStore({
	    url: 'loadUserGroups.tv',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['userGroupId', 'name', 'description']
	});
    store.setDefaultSort('name', 'asc');

    var cm = new Ext.grid.ColumnModel([
    	{header: '<fmt:message bundle="${sys}" key="sys.ugName" />', dataIndex: 'name', width: 120},
    	{header: '<fmt:message bundle="${sys}" key="sys.description" />', dataIndex: 'description', width: 225}
    ]);
    cm.defaultSortable = false;

	grid = new Ext.grid.GridPanel({width: 350, height: h, border: true,
        store: store, cm: cm, trackMouseOver: trackMouseOver, 
        sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
        renderTo: 'groupList-div'
    });

	tree2=new dhtmlXTreeObject("tree-div","100%","100%",0);
	tree2.setImagePath("../js/dhtmlx/tree/imgs/vista/");
	tree2.enableCheckBoxes(1);
	//tree2.enableThreeStateCheckboxes(true);
	
	grid.on("rowclick", function (grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);	
		curUserGroup = record.data.userGroupId;
 		refreshFolder(curUserGroup);
	});	

	store.load({callback: loadGroupCallback});  
});
function loadGroupCallback() {
	var record = grid.getStore().getAt(0);
	if (record) {
		grid.getSelectionModel().selectRow(0);
		curUserGroup = record.data.userGroupId;
		refreshFolder(curUserGroup);
	}
}
function refreshFolder(userGroupId) {
	if (curIndex == 'folder') {
		tree2.setXMLAutoLoading("loadGroupFolders.tv?userGroupId=" + userGroupId);
	} else if (curIndex == 'server') {
		tree2.setXMLAutoLoading("../server/loadServGroupByGroup.tv?userGroupId=" + userGroupId);
	}
	tree2.refreshItem(0);	
}
function saveGroupPower() {
	if (curUserGroup > 0) {
		var arr = tree2.getAllChecked();
		var ids = arr.split(',');
		if (ids.length > 0) {
			if (curIndex == 'folder') {
				$.ajax({url: 'saveGroupFolderPower.tv', type: 'POST',
					data: {userGroupId: curUserGroup, folderIds: ids},
					success: function() {
				   		window.top.showMessageDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.saveUGPermSuccess" />');
					}, error: function() {
						window.top.showErrorDlg();
					}, dataType: 'plain', cache: false});				
			} else if (curIndex == 'server') {
				$.ajax({url: '../server/saveGroupServerPower.tv', type: 'POST',
					data: {userGroupId: curUserGroup, serverGroupIds: ids},
					success: function() {
				   		window.top.showMessageDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.saveUGPermSuccess" />');
					}, error: function() {
						window.top.showErrorDlg();
					}, dataType: 'plain', cache: false});				
			}			
		}
	}
}
</script>
</body></html>
