<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resNetwork"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<Zeta:Loader>
	css css.main
	css css.reset
	library Jquery
	library ext
	library zeta
	module network
	import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js.nm3k.menuNewTreeTip
</Zeta:Loader>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<style type="text/css">
.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoFolderIcon1 {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoFolderIcon5 {
	background-image: url(../images/network/subnet.gif) !important;
}

.topoFolderIcon6 {
	background-image: url(../images/network/cloudy16.gif) !important;
}

.topoFolderIcon7 {
	background-image: url(../images/network/href.png) !important;
}

.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>
<script type="text/javascript">
//不选，默认添加到默认地域下
var selectedFolderId = 10;
var tree = null;

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
	buildFolderTree();
});
function doOnload() {
	Zeta$('name').focus();
}

//删除地域
function okClick() {
	if($("a.selectedTree").length != 1){
		top.afterSaveOrDelete({
			title : '@COMMON.tip@',
			html : '@NETWORK.pleaseSelectDeleteFolder@'
			
		})
		return;
	}
	
	//获取选中的地域
	if(selectedFolderId=='10'){
		window.parent.showMessageDlg('@resources/COMMON.tip@',"@NETWORK.notDeleteDefaultFolder@");
		return;
	}
	//获取选中地域的名称
	var folderName = '';
	$('a.linkBtn').each(function(){
		if(this.id==selectedFolderId){
			folderName = this.innerHTML;
		}
	});
	//弹窗提示是否删除该地域会删除其下的所有子节点
	window.parent.showConfirmDlg('@resources/COMMON.tip@','@NETWORK.deleteTopoFolderConfirm@', function(type) {
		if (type == 'no'){return;}
		//执行删除操作
		$.ajax({
			url: '/topology/deleteTopoFolder.tv',
	    	type: 'POST',
	    	data: {folderId : selectedFolderId, name : folderName},
	    	dataType:"json",
	   		success: function(json) {
	   			if(json.success){
	   				top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@resources/topo.virtualDeviceList.removeSuccess@</b>'
		   			});
	   			}else{
	   				top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@resources/topo.virtualDeviceList.removeFail@</b><br/>'+json.message
		   			});
	   			}
	   			buildFolderTree();
	   			//刷新父页面的树结构
	   			parent.frames["menuFrame"].refreshTree();
	   		}, error: function(json) {
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		}); 
	});
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}
function addEnterKey() {
	if (event.keyCode==KeyEvent.VK_ENTER) {
		okClick();
	}
}
</script>
</head>
<body class="openWinBody" onkeydown="addEnterKey(event)">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	@NETWORK.selectDeleteFolderTip@
	    	@NETWORK.selectDeleteFolderLimit@
	    </div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	<form id="folderForm">
		<div class="edgeTB10LR20">
		    <p class="pT10 pB5">@NETWORK.pleaseSelectDeleteFolder@:</p>
		    <div id="topoTree" class="edge5 threeFeBg" style=" height: 270px;border: 1px solid #CCC;overflow: auto;">
		    	<div>
					<ul id="tree" class="filetree">
					</ul>
				</div>
		    </div>
		    <div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i>@resources/COMMON.delete@</span></a></li>
			         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>

	
</body>
</Zeta:HTML>
