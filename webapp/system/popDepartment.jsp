<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module workbench
</Zeta:Loader>
<head>
<style type="text/css"> 
.departmentIcon {background-image: url(../images/system/icoH6.png) !important;}
</style>
<script>
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var tree = null;
Ext.onReady(function(){
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadAllDepartment.tv'});
    tree = new Ext.tree.TreePanel({
        el:'departmentTree', useArrows:useArrows, autoScroll:true, animate:true, border: true, height:200, cls:'clear-x-panel-body', padding:'10px',
        trackMouseOver:false, lines:true, rootVisible:false, containerScroll:true, enableDD:false,
        loader: treeLoader 
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Department Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    
	tree.on('click', function(n){
		Zeta$('okBt').disabled = false;
	});    
});
function getSelectedItemId(t) {
	var modal = t.getSelectionModel().getSelectedNode();
	var itemId = null;
	if (modal != null) {
		itemId = modal.id;
	}
	return itemId;
}
function getSelectedItem(t) {
	return t.getSelectionModel().getSelectedNode();
}
function okClick() {
	var item = getSelectedItem(tree);
	if(item == null || item == ''){
		window.parent.showMessageDlg("@COMMON.tip@", "@WorBench.addDepartmentTip@");
		return 
	}
	window.parent.ZetaCallback = {type:'ok', selectedItemId : item.id, selectedItemText : item.text};
	window.parent.closeWindow('departmentDlg');
}
function cancelClick() {
	window.parent.ZetaCallback = {type:'cancel'};
	window.parent.closeWindow('departmentDlg');
}
</script>	  
</head>
<body class="openWinBody">
	<div class="edge10">
		<p class="pB10 blueTxt">@td.departmentList@</p>
		<div id="departmentTree" class="threeFeBg"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a onclick="okClick()" id=okBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@button.ok@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@button.cancel@</span></a></li>
		     </ol>
		</div>
	</div>

</body>
</Zeta:HTML>
