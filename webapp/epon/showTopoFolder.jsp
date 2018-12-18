<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	css css.main
	library Jquery
	library ext
	library zeta
	MODULE  network
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
<script>
var onuId = '<s:property value="onuId"/>';
var typeId = '<s:property value="typeId"/>';
var onuName = '<s:property value="onuName"/>';
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
		window.parent.showMessageDlg('@COMMON.tip@','@NETWORK.plsSelectFolder@');
		return;
	}
	//获取选中地域的文本
	var folderName = '';
	$('a.linkBtn').each(function(){
		if(this.id==selectedFolderId){
			folderName = this.innerHTML;
		}
	});
	$.ajax({
		url: '/onu/moveToTopoFromOnuView.tv', type: 'POST',
    	data:{
    		onuId: onuId,
            typeId: typeId,
            folderId: selectedFolderId,
            onuName: onuName
    	},async: false,
   		success: function(json) {
        	top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: "@epon/ONU.addOk@"
			});
  			//window.parent.showMessageDlg('@COMMON.tip@', '@epon/ONU.addOk@' );
  			window.parent.addView("topo" + selectedFolderId, folderName, "topoRegionIcon", "topology/showNewTopoDemo.tv?folderId=" + selectedFolderId,null,true);
   			cancelClick();
   		}, error: function(excp) {
   			if(excp.simpleName == "DuplicateKeyException"){
   				window.parent.showMessageDlg('@COMMON.tip@', '@COMMON.entityExistInTopo@' );
   			}else{
	   			window.parent.showMessageDlg('@COMMON.tip@', '@epon/ONU.addEr@' );
   			}
		}, cache: false
	});
	/* Ext.Ajax.request({url: '/epon/moveToTopoFromOnuView.tv',
        params : {
           onuId: onuId,
           typeId: typeId,
           folderId: selectedFolderId,
           onuName: onuName
       }, 
	   method:'POST', 
	   success: function(response) {
			try {
				response = Ext.decode(response.responseText)
				if(response.success){
					window.parent.showMessageDlg('@COMMON.tip@', I18N.ONU.addOk );
					window.parent.addView("topo" + selectedFolderId, folderName, "topoRegionIcon", "topology/getTopoMapByFolderId.tv?folderId=" + selectedFolderId,null,true);
				}
				if(response.success == 'exists'){
					window.parent.showMessageDlg('@COMMON.tip@', '@epon/ONU.entityExistTopo@' );
				}
			} catch (err) {}
			cancelClick();		   			
	   },
	   failure: function(response) {
		   window.parent.showMessageDlg('@COMMON.tip@',  '@epon/I18N.ONU.addEr@' );
	   }
	});	 */
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
	<div class="openWinHeader">
		<div class="openWinTip">@epon/ONU.selectTopoFolderLoc@:</div>
		<div class="rightCirIco earthCirIco"></div>		
	</div>
	<div class="edge10">
		<div id="topoTree" style=" height: 240px;border: 1px solid #CCC;overflow: auto;">
			<ul id="tree" class="filetree">
			</ul>
		</div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			<ol class="upChannelListOl pB0 pT20 noWidthCenter">
				<li><a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>			
			</ol>
		</div>
	</div>
	
</body>
</Zeta:HTML>
