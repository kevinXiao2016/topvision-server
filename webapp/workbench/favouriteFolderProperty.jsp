<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module workbench
</Zeta:Loader> 
<script type="text/javascript" >

function cancelClick() {
	window.parent.closeWindow('modalDlg');
}
function okClick() {
	var el = $('#name').val();
	var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,32}$/;
	if( !reg.test(el) ){
		$("#name").focus();
		return;
	}
	
	$.ajax({url: 'updateFavouriteFolder.tv', type: 'POST',
		data: jQuery(folderForm).serialize(),
		success: function() {
			try {
				window.top.getMenuFrame().refreshFavouriteFolder();
			} catch (err) {
			}
			cancelClick();
		}, error: function() {
			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : "@resources/COMMON.operationFailure@"
			})
		}, dataType: 'plain', cache: false});
}
</script>
</head>
<body class="openWinBody">
	<form id="folderForm" name="folderForm">
		<input type=hidden name="favouriteFolder.folderId" value="${favouriteFolder.folderId}" />
		<div class="openWinHeader">
		    <div class="openWinTip">@td.favouriteFolder.name@</div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		
		<div class="edgeTB10LR20 pT60">
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		                 <td class="rightBlueTxt withoutBorderBottom" width="140">
							<label for="name">@td.label.name@</label>
		                 </td>
		                 <td class="withoutBorderBottom">
							<input style="width:310px" id=name name="favouriteFolder.name" value="${favouriteFolder.name}"
								class="normalInput" type=text maxlength=32 toolTip="@input.max32@" />
		                 </td>
		             </tr>
		         </tbody>
		     </table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
			         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@button.save@</span></a></li>
			         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@button.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>
</body>
</Zeta:HTML>