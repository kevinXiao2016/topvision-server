<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery
	library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript">
var cmcIdList = '${cmcIdList}';
var folderList = ${folderList};
function saveClick(){
	 var folderId = $("#folderId").val();
	 window.top.showWaitingDlg('@COMMON.wait@',  '@IPTOPO.updateArea@...')
	 $.ajax({
        url: '/cmc/batchModifyTopFolder.tv',
        data: {
        	cmcIdList : cmcIdList,
        	folderId : folderId
        },
        method:'POST',
        dataType:"text",
        success: function(text) {
	        if(text == 'success'){
            	//window.parent.showMessageDlg('@COMMON.tip@', '@IPTOPO.updateAreaSuccess@');
            	window.top.closeWaitingDlg();
            	top.afterSaveOrDelete({
		   			title: '@COMMON.tip@',
		   			html: '<b class="orangeTxt">@IPTOPO.updateAreaSuccess@</b>'
		   		});
            	//window.parent.getActiveFrame().onRefreshClick();
            	top.frames['framebatchTopoIp'].frames["putGridFrame"].onRefreshClick();
            	// top.onRefreshClick()
            	cancelClick(); 
	        }else{
	        	window.parent.showMessageDlg('@COMMON.tip@', '@IPTOPO.updateAreaFailed@', 'error');
		    }
        }, 
        error: function(text) {
        	window.parent.showMessageDlg('@COMMON.tip@', '@IPTOPO.updateAreaFailed@', 'error');
    	}, 
    	cache: false
	});
}

function cancelClick(){
	window.top.closeWindow("modifyTopoFolder");
}

$(document).ready(function(){
	//填充CMC子类型及CMTS下拉框
	$.each(folderList, function(index, folder){
	        $('#folderId').append('<option value="'+folder.id +'">'+ folder.name +'</option>');
	});
});

</script>
</HEAD>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@IPTOPO.updateArea@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edgeTB10LR20 pT60">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt withoutBorderBottom" width="180"><label for="ip">@IPTOPO.deviceArea@:</label></td>
					<td class="withoutBorderBottom">
						<select id="folderId" name="folderId" class="normalSel w200"></select>
					</td>
				</tr>
			</table>
		</div>
		<div class="pT40">
			<Zeta:ButtonGroup>
				<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
				<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.close@</Zeta:Button>
			</Zeta:ButtonGroup>
		</div>
</body>
</Zeta:HTML>