<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	css css.main
	library Jquery
	library ext
	library zeta
	module platform
    import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
</Zeta:Loader>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<style type="text/css"> 
.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var cmcId = '<s:property value="cmcId"/>';
var typeId = '<s:property value="typeId"/>';
var cmcName = '<s:property value="cmcName"/>';
var selectedFolderId = -1;
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
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
function okClick() {
/* 	var node = tree.getSelectionModel().getSelectedNode();
	if(node == null) {
		window.parent.showMessageDlg(I18N.NETWORK.tip, I18N.NETWORK.plsSelectFolder);
		return
	}
 */	//获取选中地域ID
	if(selectedFolderId==-1){
		window.parent.showMessageDlg('@COMMON.tip@',I18N.NETWORK.plsSelectFolder);
		return;
	}
	//获取选中地域的文本
	var folderName = '';
	$('a.linkBtn').each(function(){
		if(this.id==selectedFolderId){
			folderName = this.innerHTML;
		}
	});
	Ext.Ajax.request({url: '/cmc/moveToTopoFromOnuView.tv?cmcId=' + cmcId + '&typeId=' + typeId + '&folderId=' + selectedFolderId,
	   method:'POST', 
	   success: function(response) {
			try {
				response = Ext.decode(response.responseText)
				if(response.success===true){
					//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.addSuccess);
					top.afterSaveOrDelete({
   						title: I18N.CMC.tip.tipMsg,
   						html: '<b class="orangeTxt">' + I18N.CMC.text.addSuccess + '</b>'
   					});
					//window.parent.addView("topo" + selectedFolderId, folderName, "topoRegionIcon", "topology/getTopoMapByFolderId.tv?folderId=" + selectedFolderId,null,true);
					window.parent.addView("topo" + selectedFolderId, folderName, "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=" + selectedFolderId,null,true);
					cancelClick();		   			
				}else if(response.success == 'exists'){
					window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, "@COMMON.entityExistInTopo@");
				}else{
                    window.top.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.addToTopoFail);
                }
			} catch (err) {}
	   },
	   failure: function(response) {
		   window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.addFailure);
	   }
	});	
}
function cancelClick() {
	window.top.closeWindow("moveToTopo");
}
function addEnterKey() {
	if (event.keyCode==KeyEvent.VK_ENTER) {
		okClick();
	}
}
</script>
</head>
<body class="whiteToBlack" onkeydown="addEnterKey(event)">
	<form id="folderForm">
		<div class="openWinHeader">
		    <div class="openWinTip">@epon/ONU.selectTopoFolderLoc@:</div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edge10">
			<div id="topoTree" style=" height: 240px;border: 1px solid #CCC;overflow: auto;">
				<ul id="tree" class="filetree">
				</ul>
			</div>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			         <li><a onmouseup="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
			         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>
	

</body>
</Zeta:HTML>