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
    import network.batchAutoDiscovery.netSegment
    import js.jquery.nm3kToolTip
</Zeta:Loader>
<style type="text/css">
html, body {
    height: 100%;
}
.has-error{
	border: 1px solid #ff0000 !important;
	background: none;
}
table.topTipTable td{ color:#555;}
#treeDemo{
  width: 300px;
  display: none;
  position: absolute;
	z-index: 100;
}
.folderTree .folderTree-body{
	height: 200px;
}
</style>

<script type="text/javascript">
var netSegment = '${netSegmentJson}',	
	succStr = '', 
	errorStr = '',
	action = '${action}',
	regionTree;
function cancel(){
	window.top.closeWindow("netSegment");
}

$(function(){
	//如果是编辑网段页面，则需要选择相应网段
	var url = undefined;
	if(action==='modify'){
		url =  '/topology/fetchNetSegmentFolders.tv?topoFolderId=' + JSON.parse(netSegment).folderId;
	}
	
	regionTree = $('#region_tree').dropdowntree({
		multi: false,
		width: 300,
		url: url
	}).data('nm3k.dropdowntree');
	regionTree.refresh();
})

$(function(){
	var options = {};
	succStr = (action==='modify') ? "@batchtopo.modifynetworksegmentsuccessful@" : "@batchtopo.addnetworksegmentsucessful@";
	errorStr = (action==='modify') ? "@batchtopo.modifynetworksegmentfail@" : "@batchtopo.addnetworksegmentfail@";
	if(action==='modify'){
		options = $.parseJSON(netSegment);
		$('#name').val(options.name);
		$('#ipInfo').val(options.ipInfo);
		$('#autoScanSel').val(options.autoDiscovery);
	}else{
		options = {};
	}
	options.action = action;
	netSegment = new NetSegment(options);
	
	$('#name').bind('change blur keyup', function(){
		netSegment.name = $('#name').val();
		var result = netSegment.nameValidate();
		refreshInputState('#name', result);
	})
	
	$('#ipInfo').bind('change blur keyup', function(){
		netSegment.ipInfo = $('#ipInfo').val();
		var result = netSegment.ipInfoValidate();
		refreshInputState('#ipInfo', result);
	})
	
	$('#save').bind('click', function(){
		var selectedFolderId = regionTree.getSelectedIds();
		if(!selectedFolderId.length){
			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.selectRegion@');
			return;
		}
		
		netSegment.name = $('#name').val();
		netSegment.ipInfo = $('#ipInfo').val();
		netSegment.folderId = selectedFolderId;
		netSegment.autoDiscovery = $('#autoScanSel').val();
		netSegment.save(function(err){
			if(err){
				//告知操作失败
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: errorStr
				});
			}
			//告知操作成功及关闭本窗口
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: succStr
			});
			//刷新父页面的store
			window.top.getActiveFrame().batchTopo.reloadStore();
			cancel();
		});
	});
	
	function refreshInputState(selector, state){
		if(state==true){
			$(selector).removeClass('normalInputRed');
		}else{
			$(selector).addClass('normalInputRed');
		}
	}
})
</script>
<body class="openWinBody">
	<div id="treeDemo" class="folderTree">
      <div class="folderTree-body"></div>
    </div>
    
	<div class="openWinHeader" style="height:100px;">
		<div class="openWinTip">
			<p class="pB5"><b>@batchtopo.tip1@@COMMON.maohao@</b></p>
			<table cellpadding="0" cellspacing="0" border="0" class="topTipTable">
				<tr>
					<td class="pL10" width="55"><b class="orangeTxt">@batchtopo.mask@ </b></td>
					<td><b class="f14">172.168.0.1/24</b></td>
				</tr>
				<tr>
					<td class="pL10"><b class="orangeTxt">@batchtopo.star@ </b></td>
					<td><b class="f14">172.17.2.*</b> @batchtopo.or@ <b class="f14">172.17.*.1</b></td>
				</tr>
				<tr>
					<td class="pL10"><b class="orangeTxt">@batchtopo.underlined@ </b></td>
					<td><b class="f14">172.17.2.1-255</b> @batchtopo.or@ <b class="f14">172.17.1-255.2</b></td>
				</tr>
			</table>
		</div>
	    <div class="rightCirIco linkCirIco" style="top:19px;"></div>
	</div>
	
	<div id="content" class="edgeTB10LR20">
		<form id="netSegmentForm">
		<input type="hidden" id="id" name="netSegment.id" value="-1"/>
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="200">@COMMON.name@:</td>
					<td><input type="text" class="normalInput w300" id="name" name="netSegment.name" toolTip="@COMMON.anotherName@" maxlength="63"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@batchtopo.networksegment@:</td>
					<td><input type="text" class="normalInput w300" id="ipInfo" name="netSegment.ipInfo" toolTip="@COMMON.networksegment@" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@COMMON.folder@:</td>
					<td>
						<div id="region_tree"></div>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@batchtopo.autoscanswitch@:</td>
					<td>
						<select class="normalSel" id="autoScanSel" name="netSegment.autoDiscovery">
							<option value="1">@COMMON.open@</option>
							<option value="0">@COMMON.close@</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>	
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" id="save"><span><i class="miniIcoAdd"></i>@COMMON.confirm@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancel()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
</body>
</Zeta:HTML>