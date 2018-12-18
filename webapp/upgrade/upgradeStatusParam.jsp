<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	library Jquery
	library ext
	library zeta
	module network
</Zeta:Loader>
<script type="text/javascript">
var jobId = ${ jobId };
var statusData = ${ upgradeStatus }
var entityIds = ${ entityIds }
Ext.onReady(function(){
	//加上所有状态类型
	var statusStr='';
	$.each(statusData, function(index, status){
		statusStr += String.format('<span class="block100"><input class="statusCbx" id="statusCbx_{0}" type="checkbox" value="{0}"/><label>{1}</label></span><br>', status.id, status.displayName);
	})
	$('#allTypesP').append(statusStr);
	//填充选中的设备类型
	$.each(statusData, function(index, status){
		$('#statusCbx_'+status.id).attr('checked',true);
	})
	if($('.statusCbx').length===$('.statusCbx:checked').length){
		$('#allTypeCbx').attr('checked', true);
	}else{
		$('#allTypeCbx').attr('checked', false);
	}
	$('#allTypeCbx').bind('click', function(){
		if($(this).attr("checked")){
			$('#allTypesP').find(".statusCbx").attr("checked", true);
		}else{
			$('#allTypesP').find(".statusCbx").attr("checked", false);
		}
	});
	$('#allTypesP').bind('click', function(){
		if($('.statusCbx').length===$('.statusCbx:checked').length){
			$('#allTypeCbx').attr('checked', true);
		}else{
			$('#allTypeCbx').attr('checked', false);
		}
	}); 
});

function cancelClick() {
	window.top.closeWindow("upgradeStatus");
}

function okClick(){
	var statusIds = [];
	$('.statusCbx:checked').each(function(){
		statusIds.push($(this).val());
	})
	if(entityIds != null && entityIds !='' && typeof(entityIds) != "undefined"){
		$.ajax({
        url: '/upgrade/upgradeNow.tv',
        type: 'post',
        dataType:"json",
        data:{jobId: jobId,
        	entityIds: entityIds,
        	statusIds: statusIds.join("$")},
        cache: false, 
        success: function(response) {
        	cancelClick();
        }, 
        error: function(response) {
        }
    }); 
	}else{
		$.ajax({
	        url: '/upgrade/upgradeAllNow.tv',
	        type: 'post',
	        dataType:"json",
	        data:{jobId: jobId,
	        	statusIds: statusIds.join("$")},
	        cache: false, 
	        success: function(response) {
	        	
	        }, 
	        error: function(response) {
	        	
	        }
	    });
	}
}
</script>
</head>
<%-- <body class="openWinBody" >
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	配置需要升级的设备状态
	    </div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	<form id="folderForm">
		<div class="edgeTB10LR20">
		    <p class="pT10 pB5"></p>
		    <div id="tree" class="edge5 threeFeBg putTree" style=" height: 270px;border: 1px solid #CCC;overflow: auto;">
					<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="150"></td>
							<td class="pR10">
								<p><span class="block100"><input id="allTypeCbx" type="checkbox" /><label>@batchtopo.selectall@</label></span></p>
								<p id="allTypesP"></p>
							</td>
						</tr>
					</tbody>
				</table>
		    </div>
		    <div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@resources/COMMON.ok@</span></a></li>
			         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i>@resources/COMMON.close@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>
</body> --%>
</Zeta:HTML>
