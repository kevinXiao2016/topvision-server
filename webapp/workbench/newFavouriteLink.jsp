<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<link rel="stylesheet" type="text/css" href="/css/bootstrap.css"/>

<style type="text/css">
.favouriteFolderIcon {
	background-image: url(../images/workbench/folder.gif) !important;
}

.favouriteLinkIcon {
	background-image: url(../images/workbench/link.gif) !important;
}
.input-group-addon{
	background-color: #fff !important;
}
.input-group-sm>.input-group-addon{
	padding: 5px 2px 5px 5px !important;
	border-right: 0px;
}
.input-group-sm>.form-control{
	padding: 5px 5px 5px 0 !important;
	border-left: 0px;
}
a.normalBtnBig span{
	height: 32px !important;
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$('#basic-addon1').text(window.location.origin + '/');
	getFramePageSrc();
})
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
var folderPath = '${folderPath}';
Ext.onReady(function(){
	if(folderPath.length > 1){
		var src = top.getMainFrameUrl();
		$("#url").val(src);
		var title = top.getMainFrameTitle();
		$("#name").val(title);
	}
	
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadFavouriteFolder.tv?hasFavouriteLink=false'});
    tree = new Ext.tree.TreePanel({
        el:'folderTree', height: 104, useArrows:useArrows, autoScroll:true, animate:animCollapse, border: false,
        trackMouseOver:trackMouseOver,
        lines: true, rootVisible: false, containerScroll: true, enableDD: false,
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
	var el1 = Zeta$('url');
	if (el1.value.trim() == '' || el1.value.trim().indexOf('://')!=-1) {
		el1.focus();
		return;
	}
	var node = tree.getSelectionModel().getSelectedNode();
	if(node!=null){
	if(node.attributes.type == 1){
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.pleaseChooseFileFolder);
		return;
	}
	
	var length = node.childNodes.length;
	if(length > 0){
		for(i = 0; i < length; i++){			
			if(el.value.trim() == node.childNodes[i].text)
					{
					window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.linkExist);
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
	Ext.Ajax.request({url: 'createFavouriteLink.tv', method:'POST', params:params, form:'folderForm',
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
function getFramePageSrc(){
	var src = top.getMainFrameUrl();
	$("#url").val(src.replace(/http:\/\/[\w\.-]+:3000\//, ''));
	if($("#name").val().length == 0){
		var title = top.getMainFrameTitle();
		$("#name").val(title);
	}
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip"><fmt:message key="apan.createLink" bundle="${workbench}"/></div>
		<div class="rightCirIco linkCirIco"></div>		
	</div>
	<div class="edgeTB10LR20">
		<form id="folderForm" onsubmit="return false;">
		<table cellpadding="0" cellspacing="0" rules="none" border="0" width="512" class="mCenter tdEdged4">
			<tr>
				<td width="68"><fmt:message key="td.label.name" bundle="${workbench}"/><span class="redTxt">*</span></td>
				<td>
				    <input id="name" name="favouriteFolder.name" value='' type="text" class="form-control input-sm"
						maxlength=32 toolTip='<fmt:message key="input.linkNameNotNull" bundle="${workbench}"/><br /><fmt:message key="input.max32" bundle="${workbench}"/>' />
					
				</td>
			</tr>
			<tr>
				<td>
					URL:<span class="redTxt">*</span>
				</td>
				<td>
					<div class="input-group input-group-sm" style="width:450px;">
					  	<span class="input-group-addon" id="basic-addon1"></span>
					  	<input id="url" name="favouriteFolder.url" type="text" class="form-control" placeholder="favouriteUrl" aria-describedby="basic-addon1" toolTip='<fmt:message key="input.linkAddrNotNull" bundle="${workbench}"/>' />
					</div>
					<%-- <input id=url name="favouriteFolder.url" class="normalInput" style="width:280px;" type="text" toolTip='<fmt:message key="input.linkAddrNotNull" bundle="${workbench}"/>' /> --%>
				</td>
				<%-- <td>
					<button class="btn btn-default btn-sm" onclick="getFramePageSrc()"><fmt:message key="td.getFramePage" bundle="${workbench}"/></button>
				</td> --%>
			</tr>
			<tr>
				<td colspan="2" class="pT10"><fmt:message key="td.chooseLinkAddr" bundle="${workbench}"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="folderTree" class="folderTreeContainer clear-x-panel-body"></div>
				</td>
			</tr>
		</table>
		 <ol class="upChannelListOl pB0 noWidthCenter">
			<%-- <button class="btn btn-default btn-sm" onclick="okClick()">
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
				<fmt:message key="button.create" bundle="${workbench}"/>
			</button>
			<button class="btn btn-default btn-sm" onclick="cancelClick()">
				<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				<fmt:message key="button.cancel" bundle="${workbench}"/>
			</button> --%>
			<li><a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoAdd"></i><fmt:message key="button.create" bundle="${workbench}"/></span></a></li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i><fmt:message key="button.cancel" bundle="${workbench}"/></span></a></li>
		</ol>
		</form>
	</div>
	<div class=formtip id=tips style="display: none"></div>
	<script type="text/javascript" src="../js/jquery/jquery.js" ></script>
	<script type="text/javascript" src="../js/jquery/nm3kToolTip.js" ></script>
</body>
</html>