<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js/raphael/raphael
    module network
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
function cancelClick(){
	window.top.closeWindow('renameFolder');
}

var folderId = '${folderId}';
var oldName = '${oldName}';
var superiorId = '${superiorId}';
var topoFolderId = '${topoFolderId}';

function okClick(){
	var newName = $("#folderNameInput").val();

	//名称必须为1-24位的合法字符
	var reg = /^[a-zA-Z\d\u4e00-\u9fa5-_]{1,24}$/;
	if(!reg.test($.trim(newName))){
		window.top.showMessageDlg("@MENU.tip@", "@topo.newTopoFolder.nameTip@");
		return;
	}
	$.ajax({url: '../topology/renameTopoFolder.tv', type: 'POST', cache: false, 
		dataType: 'json',
		data: {superiorId: superiorId, folderId: folderId, oldName:oldName, name: newName},
		success: function(json) {
			if (json.exists) {//数据库中已经有这个名字了;
				window.top.showMessageDlg("@MENU.tip@", "@NETWORK.folderExist@");
				window.cancelClick();
				return;
			}
			//刷新左侧topo树菜单;
			top.refreshTopoTree();
			try{
				top.frames["frametopo"+topoFolderId].renameFolderNameFn(newName);
			}catch(err){}
			window.cancelClick();
		},
		error: function() {
		}
	});
	
}

</script>        
</head>
<body>
	<div class="openWinHeader">
	    <div class="openWinTip">@NETWORK.modifyFolderTitle@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT30">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt withoutBorderBottom w160">@NETWORK.alias@:</td>
	                <td class="withoutBorderBottom">
	                    <input class="normalInput modifiedFlag w200" id="folderNameInput" maxlength=24 value="${oldName}"
			             toolTip='@topo.newTopoFolder.nameTip@' />
	                </td>
	            </tr>	            
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoSaveOK"></i>@COMMON.save@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick();"><span>@COMMON.cancel@</span></a></li>
		     </ol>
		
		</div>
			    
	</div>
</body>
</Zeta:HTML>
