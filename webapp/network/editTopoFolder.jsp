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
    import js.jquery.nm3kToolTip
</Zeta:Loader>
<style type="text/css">
.folderTree .folderTree-body{
	height: 300px;
	border-bottom: none;
}
p.orangeTxt{
	font-weight: bold;
}
</style>

<script type="text/javascript">
var entityId = '${entityId}';
var action = '${action}';
var entityIdStr = '${entityIdStr}';
if(entityIdStr){
	var entityIds = entityIdStr.split(',');
}

$(function(){
	url='/topology/fetchEntityLocatedFolders.tv';
	if(entityId){
		url+='?entityId='+entityId;
	}
	var $region_tree = $('#region_tree').dropdowntree({
		withSelect: false,
		width: '100%',
		url: url
	}).data('nm3k.dropdowntree');
	
	//展开勾选节点
	expandCheckedNodes();
	
	//根据不同的action进行不同的初始化操作
	if(action=='editTopoFolder'){ 
		//表明是正常的编辑单个设备与地域之间的关系
		//设置正确的标题信息
		$('#openWinTip').text('@topo.editSnapFolder@');
		//绑定正确的方法
		$('#okBt').bind('click', setEntityLocatedFolders);
	}else if(action=='batchEditFolder'){ 
		//批量编辑地域
		//设置正确的标题信息
		$('#openWinTip').text('@topo.batcheditfolderBar@');
		//绑定正确的方法
		$('#okBt').bind('click', setEntitiesLocatedFolders);
	}else if(action=='batchInsertFolder'){
		//批量加入地域
		//设置正确的标题信息
		$('#openWinTip').text('@topo.batchaddfolderBar@');
		//绑定正确的方法
		$('#okBt').bind('click', addEntitiesToFolders);
	}else if(action=='batchRemoveFolder'){
		//批量移出地域
		//设置正确的标题信息
		$('#openWinTip').text('@topo.batchremovefolderBar@');
		//绑定正确的方法
		$('#okBt').bind('click', removeEntitiesFromFolders);
	}
	
	$('#cancelBt').bind('click', function(){
		window.top.closeWindow('editTopoFolder');
	});
	
	function expandCheckedNodes(){
		//折叠所有节点
		$region_tree.$tree.expandAll(false);
		
		//modify by fanzidong，需要打开根节点，否则后续打开会有问题
		var rootNode = $region_tree.$tree.getNodes()[0];
		$region_tree.$tree.expandNode(rootNode, true, false, false, false);
		
		//找到所有选中节点
		var checkNodes = $region_tree.getCheckedNodes();
		//遍历这些节点，找到这些节点的父节点，展开这些父节点
		var curNode, pNode;
		for(var i=0, len=checkNodes.length; i<len; i++){
			curNode = checkNodes[i];
			if(curNode.pId!=null){
				pNode = $region_tree.$tree.getNodeByParam('id', curNode.pId);
				if(pNode!=null){
					$region_tree.$tree.expandNode(pNode, true, false, false, false);
				}
			}else{
				//为根节点，展开
				$region_tree.$tree.expandNode(curNode, true, false, false, false);
			}
		}
	}
	
	function setEntityLocatedFolders(){
		//获取选中地域
		var selectedFolderIds = $region_tree.getSelectedIds();
		if(!selectedFolderIds.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.plsFolder@');
			return;
		}
	  	
	  	$.ajax({
		  url: '/entity/setEntityLocatedFolders.tv',
		  data: {entityId: entityId, folderIds: selectedFolderIds},
		  dataType: 'json',
		  success: function(){
			  top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '<b class="orangeTxt">@topo.editfoldersuccess@</b>'
	          });
			  window.top.closeWindow('editTopoFolder');
		  },
		  error: function(){},
		  cache: false
	  	});
	}
	
	function setEntitiesLocatedFolders(){
		//获取选中地域
		var selectedFolderIds = $region_tree.getSelectedIds();
		if(!selectedFolderIds.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.plsFolder@');
			return;
		}
		
		$.ajax({
		  url: '/entity/setEntitiesLocatedFolders.tv',
		  data: {entityIds: entityIds, folderIds: selectedFolderIds},
		  dataType: 'json',
		  success: function(){
			  top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '<b class="orangeTxt">@topo.batcheditfoldersuccess@</b>'
	          });
			  window.top.closeWindow('editTopoFolder');
		  },
		  error: function(){},
		  cache: false
	  	});
	}
	
	function addEntitiesToFolders(){
		//获取选中地域
		var selectedFolderIds = $region_tree.getSelectedIds();
		if(!selectedFolderIds.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.plsFolder@');
			return;
		}
		
		$.ajax({
		  url: '/entity/addEntitiesToFolders.tv',
		  data: {entityIds: entityIds, folderIds: selectedFolderIds},
		  dataType: 'json',
		  success: function(){
			  top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '<b class="orangeTxt">@topo.batchaddfoldersuccess@</b>'
	          });
			  window.top.closeWindow('editTopoFolder');
		  },
		  error: function(){},
		  cache: false
	  	});
	}
		
	function removeEntitiesFromFolders(){
		//获取选中地域
		var selectedFolderIds = $region_tree.getSelectedIds();
		if(!selectedFolderIds.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.plsFolder@');
			return;
		}
		
		$.ajax({
		  url: '/entity/removeEntitiesFromFolders.tv',
		  data: {entityIds: entityIds, folderIds: selectedFolderIds},
		  dataType: 'json',
		  success: function(json){
			  var msg = '<p><b class="orangeTxt">@topo.batchremovefoldersuccess@.</b></p>';
			  if(json.failedIds && json.failedNames){
				  msg += '<p><b class="orangeTxt">@topo.batchremovefolderfailId@' + json.failedNames + '</b></p>'; 
			  }
			  top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: msg
	          });
			  window.top.closeWindow('editTopoFolder');
		  },
		  error: function(){},
		  cache: false
	  	});
	}
	
})
</script>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip" id="openWinTip">@NETWORK.moveToSelectedTopoFolder@
		</div>
		<div class="rightCirIco earthCirIco"></div>		
	</div>
	<div class="edge10">
		<div id="region_tree"></div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			<ol class="upChannelListOl pB0 pT10 noWidthCenter">
				<li><a id="okBt" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
				<li><a id="cancelBt" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>			
			</ol>
		</div>
	</div>
</body>
</Zeta:HTML>