<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
	library ext
    library Zeta
    module  network
    plugin DropdownTree
</Zeta:Loader>
<style type="text/css">
.folderTree .folderTree-body{
	height: 300px;
	border-bottom: none;
}
</style>

<script type="text/javascript">
var userId = ${userContext.userId};
$(function(){
	var $cur_root_regions = $('#cur_root_regions').dropdowntree({
		width: '100%',
		multi:false,
		withSelect: false,
		url: '/topology/fetchSwithRootFolders.tv?userId='+userId
	}).data('nm3k.dropdowntree');
	
	$('#okBt').bind('click', function(){
		var folderId = $cur_root_regions.getSelectedIds();
		if(!folderId.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.plsFolder@');
			return;
		}
		if(userId==2){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.admincantchange@');
			return;
		}
		
		$.ajax({
		  url: '/system/switchRoorFolder.tv',
		  data: {userId: userId, folderId: folderId[0]},
		  dataType: 'json',
		  success: function(){
			  window.top.location.reload();
		  },
		  error: function(){
			  window.top.showMessageDlg('@COMMON.tip@', '@COMMON.changefailed@');
				return;
		  },
		  cache: false
	  	});
	});
	
	$('#cancelBt').bind('click', function(){
		window.top.closeWindow('modalDlg');
	});
})
</script>

<body class="openWinBody">
	<div class="openWinHeader">
		<!-- <div class="openWinTip" id="openWinTip">@NETWORK.moveToSelectedTopoFolder@</div> -->
		<div class="rightCirIco earthCirIco"></div>		
	</div>
	<div class="edge10">
		<div id="cur_root_regions"></div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			<ol class="upChannelListOl pB0 pT10 noWidthCenter">
				<li><a id="okBt" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
				<li><a id="cancelBt" href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>			
			</ol>
		</div>
	</div>
</body>
</Zeta:HTML>