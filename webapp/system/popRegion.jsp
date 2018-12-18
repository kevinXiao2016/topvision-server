<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
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
<style type="text/css"> 
.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>

<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<script>
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
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
	//获取选中项及其text
	if(selectedFolderId==-1){
		window.parent.showMessageDlg('@COMMON.tip@',"@WorkBench.pleaseChooseFolder@");
	}
	//获取选中地域的文本
	var folderName = '';
	$('a.linkBtn').each(function(){
		if(this.id==selectedFolderId){
			folderName = this.innerHTML;
		}
	});
	window.parent.ZetaCallback = {type:'ok', selectedItemId : selectedFolderId, selectedItemText : folderName};
	window.parent.closeWindow('regionDlg');
}
function cancelClick() {
	window.parent.ZetaCallback = {type:'cancel'};
	window.parent.closeWindow('regionDlg');
}
</script>	  
</head>
<body class="whiteToBlack">
	<div style="padding: 10px 10px 0;">
		<p class="blueTxt">@resources/SYSTEM.regionList@:</p>
		<div id="topoTree" style=" height: 240px;border: 1px solid #CCC;overflow: auto;">
			<ul id="tree" class="filetree">
			</ul>
		</div>
		
		<div class="noWidthCenterOuter clearBoth pT10">
		    <ol class="upChannelListOl noWidthCenter">
		        <li>
		            <a id=okBt onclick="okClick()" href="javascript:;" class="normalBtnBig">
		                <span>
		                    <i class="miniIcoSaveOK">
		                    </i>
		                    @COMMON.ok@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a onclick="cancelClick()" href="javascript:;" class="normalBtnBig">
		                <span>
		                	<i class="miniIcoForbid"></i>
		                	@COMMON.cancel@
		                </span>
		            </a>
		        </li>        
		    </ol>
		</div>
		
	</div>
</body>
</Zeta:HTML>
