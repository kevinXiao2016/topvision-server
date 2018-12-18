<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY EXT
	LIBRARY JQUERY 
	LIBRARY ZETA
    MODULE  network
    import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js/tools/treeBuilder
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<style type="text/css"> 
.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>
<script type="text/javascript">
var selectedFolderId = -1;

/**
 * 构建地域树结构
 */
function buildFolderTree(){
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../topology/loadTopoFolder.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(json) {
			bulidTree(json, '');
			treeExpand();
			var CLASSES = {
					open: "open",
					closed: "closed",
					expandable: "expandable",
					expandableHitarea: "expandable-hitarea",
					lastExpandableHitarea: "lastExpandable-hitarea",
					collapsable: "collapsable",
					collapsableHitarea: "collapsable-hitarea",
					lastCollapsableHitarea: "lastCollapsable-hitarea",
					lastCollapsable: "lastCollapsable",
					lastExpandable: "lastExpandable",
					last: "last",
					hitarea: "hitarea"
				};
			 $("div.hitarea:gt(0)").each(function(){
				 $(this)
					.parent()
					// swap classes for hitarea
					.find(">.hitarea")
						.swapClass( CLASSES.collapsableHitarea, CLASSES.expandableHitarea )
						.swapClass( CLASSES.lastCollapsableHitarea, CLASSES.lastExpandableHitarea )
					.end()
					// swap classes for parent li
					.swapClass( CLASSES.collapsable, CLASSES.expandable )
					.swapClass( CLASSES.lastCollapsable, CLASSES.lastExpandable )
					// find child lists
					.find( ">ul" )
					// toggle them
					.heightHide( false, null );
			 })
			//为树绑定事件
			$('a.linkBtn').each(function(){
   				$(this).bind('click', function(){
   					selectedFolderId = $(this).attr("id");
   				});
   			});
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

Ext.onReady(function(){
	buildFolderTree()
});
function getSelectedItemId() {
	/* var modal = t.getSelectionModel().getSelectedNode();
	var item = null
	if (modal != null) {
		item = {
				itemId : modal.id,
				itemName : modal.text
		}
	} */
	//获取选中地域的文本
	var folderName = '';
	$('a.linkBtn').each(function(){
		if(this.id==selectedFolderId){
			folderName = this.innerHTML;
		}
	});
	var item = {
		itemId : selectedFolderId,
		itemName : folderName
	};
	return item
}
function okClick() {
	var item = getSelectedItemId();// folder id
	if(item.itemId == -1) {
		top.afterSaveOrDelete({
			title : "@COMMON.tip@",
			html : "@NETWORK.plsSelectFolder@"
		})
		//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.plsSelectFolder@");
		return
	}
	window.top.ZetaCallback = {type:'ok', selectedItem:item};
	window.top.closeWindow('topoFolderTree');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('topoFolderTree');
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">@NETWORK.moveToSelectedTopoFolder@
		</div>
		<div class="rightCirIco earthCirIco"></div>		
	</div>
	<div class="edge10">
		<div id="topoTree" style=" height: 240px;border: 1px solid #CCC;overflow: auto;">
			<ul id="tree" class="filetree">
			</ul>
		</div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			<ol class="upChannelListOl pB0 pT20 noWidthCenter">
				<li><a id="okBt" href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>			
			</ol>
		</div>
	</div>

	
</body>
</Zeta:HTML>