<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript">
//var folderId = ${folderId};
var cmcId = ${cmcId};
var folderList = ${folderList};

function saveClick(){
	 var deviceName = $("#deviceName").val();
	 if( !V.isAnotherName(deviceName) ){
		 $("#deviceName").focus();
		 return;		 
	 }
	 var newFolderId = $("#folderId").val();
	 window.top.showWaitingDlg('@COMMON.wait@',  '@IPTOPO.editDevice@...')
	 $.ajax({
        url: '/cmc/modifyDeviceInfo.tv',
        data: {
        	cmcId : cmcId,
        	folderId : newFolderId,
       		deviceName : deviceName
        },
        method: 'POST',
        dataType: "json",
        success: function(text) {
           	window.parent.showMessageDlg('@COMMON.tip@', '@IPTOPO.modifyInfoSuccess@',null,function(){
	           	cancelClick();
           	});
           	top.getActiveFrame().frames["putGridFrame"].refresh(); 
        }, 
        error: function(text) {
        	window.parent.showMessageDlg('@COMMON.tip@', '@IPTOPO.modifyInfoFailed@');
    	}, 
    	cache: false
	});
}

function cancelClick(){
	window.top.closeWindow("editDeviceInfo");
}

$(document).ready(function(){
	//填充CMC子类型及CMTS下拉框
	$.each(folderList, function(index, folder){
		$('#folderId').append('<option value="'+folder.id +'">'+ folder.name +'</option>');
	});
	//$('#folderId').val(folderId);
});

</script>
</HEAD>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@IPTOPO.editDevice@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edgeTB10LR20 pT60">
			<table class="zebraTableRows">
				<tr>
					 <td class="rightBlueTxt" width="180">
	                    <label for="deviceName">@COMMON.alias@:</label>
	                </td>
	                <td>
						<input id="deviceName" toolTip='@COMMON.anotherName@' class="normalInput w200" name="deviceName" value="${deviceName}" maxlength="63"/>
	                </td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="180">
						<label for="folderId">@IPTOPO.deviceArea@:</label>
					</td>
					<td>
						<select id="folderId" name="folderId" class="normalSel w200"></select>
					</td>
				</tr>
			</table>
		</div>
		<div class="pT40">
			<Zeta:ButtonGroup>
				<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
				<Zeta:Button onClick="cancelClick()" icon="miniIcoWrong">@COMMON.close@</Zeta:Button>
			</Zeta:ButtonGroup>
		</div>
</body>
</Zeta:HTML>