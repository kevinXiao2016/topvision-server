<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
</Zeta:Loader>

<style type="text/css">
.departmentIcon {background-image: url(../images/system/icoH6.png) !important;}
</style>

<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
function tabActivate() {
	window.parent.setMapAndCoordInfo('', '');
}
var treeRootNode = null;
var tree = null;
function onNewDepartmentClick() {
	var node = tree.getSelectionModel().getSelectedNode();
	var itemId = 0;
	if (node != null) {
		itemId = node.id;
	}
	window.parent.createDialog("modalDlg", I18N.SYSTEM.newDepartment, 800, 500, "system/showNewDepartment.tv?superiorId=" + itemId, null, true, true);
}
function onDeleteClick() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node != null) {
		//node.hasChildNodes()在子节点为array(0)时会返回true,增加子节点是否实际存在的判断
		if (node.hasChildNodes() && node.childNodes.length > 0) {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.hasChildrenDepartment);
		} else {
			window.top.showConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.sureDeleteDepartment, function(type) {
				if (type == 'no') {return;}
				Ext.Ajax.request({url: 'deleteDepartment.tv',
				    success: function(json) {
					json = Ext.decode(json.responseText);
					if(json.exists){
						window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.departmentUsed);
					}else{
						top.afterSaveOrDelete({
			       	      title: I18N.COMMON.tip,
			       	      html: '<b class="orangeTxt">'+I18N.COMMON.deleteSuccess+'</b>'
			       	    });
						node.remove();
					}
				},
				    failure: function(){window.top.showMessageDlg(I18N.COMMON.error, I18N.SYSTEM.errorDeleteDepartment, 'error');},
				    params: {departmentId:node.id}
				});			
			});
		}
	} else {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.selectDeleteDepartment);
	}
}
function onModifyClick() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node != null) {
	} else {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.selectModifyDepartment);
	}
}
function onRenameClick() {
}
function onRefreshClick() {
	treeRootNode.reload();
}
Ext.BLANK_IMAGE_URL = '../images/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();
	var deleteMenu = new Ext.menu.Item({text: I18N.COMMON.remove, iconCls:'bmenu_delete',
		handler:onDeleteClick});
	var mainMenu = new Ext.menu.Menu({id: 'mainMenu', items:[
		deleteMenu, //{text: 重命名, handler: onRenameClick},
		 '-',
		{text: I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler:onRefreshClick}         
	]});

	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl:'loadAllDepartment.tv'
	});
    tree = new Ext.tree.TreePanel({
        el:'departmentTree', 
        useArrows:useArrows, 
        autoScroll:true, 
        animate:true,  
        border: true,
        height:350, 
        padding:'10px 10px 10px 10px',
        trackMouseOver:false,
        lines: true, 
        rootVisible: false, 
        containerScroll: true, 
        enableDD: false,
        loader: treeLoader
    });
    treeRootNode = new Ext.tree.AsyncTreeNode({
    	text: 'Department Tree', 
    	draggable:false, 
    	id:'source'
    });
    tree.setRootNode(treeRootNode);
    tree.render();
    tree.expandAll();

	tree.on('contextmenu', function(n, e){
		if (this.getSelectionModel().isSelected(n) == false) {
			this.getSelectionModel().select(n);
		}
		mainMenu.showAt(e.getPoint());
	});
});
</script>
</head><body class="whiteToBlack">
<div class="pannelTit">
	<fmt:message bundle="${resources}" key="SYSTEM.departmentList" />
</div>
<div class="edge10">	
	<div id="departmentTree" class="clear-x-panel-body threeFeBg" style="width:100%; height:350px; margin-bottom:6px;"></div>
	<div class="yellowTip">
		@SYSTEM.deparmentTip@
	</div>
	<div class="noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl pT10 noWidthCenter">	
		     <li><a id=newBt onclick="onNewDepartmentClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i><fmt:message bundle="${resources}" key="SYSTEM.newDepartment" /></span></a></li>	
		     <li><a id=removeBt onclick="onDeleteClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i><fmt:message bundle="${resources}" key="SYSTEM.deleteDepartment" /></span></a></li>	
		     <li><a onclick="onRefreshClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoRefresh"></i><fmt:message bundle="${resources}" key="COMMON.refresh" /></span></a></li>	
		</ol>
	</div>
	
</div>

</body>
</Zeta:HTML>