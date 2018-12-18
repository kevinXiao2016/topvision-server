<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
    library Ext
    library Zeta
    module  workbench
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<style type="text/css"> 
.favouriteFolderIcon {background-image: url(../images/workbench/folder.gif) !important;}
.favouriteLinkIcon {background-image: url(../images/workbench/link.gif) !important;}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
Ext.onReady(function(){
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadFavouriteFolder.tv?hasFavouriteLink=false'});
    tree = new Ext.tree.TreePanel({
        el:'folderTree', height: 134, useArrows:useArrows, autoScroll:true, animate:animCollapse, border: false,
        trackMouseOver:trackMouseOver, lines: true, rootVisible: false, containerScroll: true, enableDD: false,
        loader: treeLoader
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Favourite Folder Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    
    setTimeout("doOnload()", 500);
});

function doOnload() {
	Zeta$('name').focus();
}

function okClick() {
	var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,32}$/;
	var inputName = $("#name").val();
	if( !reg.test(inputName) ){
		$("#name").focus();
		return;
	}
	
	var el = Zeta$('name');
	
	var node = tree.getSelectionModel().getSelectedNode();
	if(node!=null){
	var length = node.childNodes.length;
	if(length > 0){
		for(i = 0; i < length; i++)
			{
				if(el.value.trim() == node.childNodes[i].text)
					{
					window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.fileFolderExist);
					return;
					}
			} 
		}
	}
	var itemId = '0';
	var share = '0';
	if (node != null) {
		itemId = node.id;
		if (node.attributes.share) {
			share = '1';
		}
	}
	var params = "favouriteFolder.superiorId=" + itemId + '&favouriteFolder.shared=' + share;
	Ext.Ajax.request({url: 'createFavouriteFolder.tv', method:'POST', params:params, form:'folderForm',
	   success: function(response) {
	   		try { 
	   			top.afterSaveOrDelete({
	   				title: I18N.COMMON.tip,
	   				html: '<b class="orangeTxt">' + I18N.COMMON.addSuccess + '</b>'    
	   			});
	   			var frame = window.parent.getNavigationFrame();
				frame.refreshFavouriteFolder();
			} catch (err) {
			}			
			cancelClick();		   			
	   },
	   failure: function(response) {window.parent.showErrorDlg();}
	});	
}

function cancelClick() {
	window.top.closeWindow("modalDlg");
}
</script> 
</head>
<body class="openWinBody">
	<form id="folderForm" onsubmit="return false;">
		<div class="openWinHeader">
			<div class="openWinTip">@span.createFavorites@</div>
			<div class="rightCirIco folderCirIco"></div>		
		</div>
		
		<div class="edgeTB10LR20">
			<table cellpadding="0" cellspacing="0" rules="none" border="0" width="512" class="mCenter tdEdged4">
				<tr>
					<td width="70">
						<label for="name">@td.label.name@<font color=red>*</font></label>
					</td>
					<td>
						<input id=name name="favouriteFolder.name" value='' class="normalInput w440" type=text maxlength=32	  	
						toolTip='@td.favouriteFolder.name@<br />@input.max32@' />
					</td>
				</tr>
				<tr>
					<td colspan="2" class="pT10">@td.chooseFavoritesAddr@</td>
				</tr>
				<tr>
					<td colspan="2">
						<div id="folderTree" class="folderTreeContainer h136 clear-x-panel-body"></div>
					</td>
				</tr>
			</table>
			
			<ol class="upChannelListOl pB0 pT10 noWidthCenter">
				<li><a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoAdd"></i>@COMMON.create@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@button.cancel@</span></a></li>
			</ol>
		</div>
		
		<script type="text/javascript" src="../js/jquery/nm3kToolTip.js" ></script>
	</form>	
</body>
</Zeta:HTML>