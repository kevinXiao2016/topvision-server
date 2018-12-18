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
    css css/bootstrap
</Zeta:Loader>

<style type="text/css">
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

<script type="text/javascript">
var url =  '${favouriteFolder.url}';
	$(function(){
		$('#basic-addon1').text(window.location.origin + '/')
		$('#url').val(url.replace(/http:\/\/[\w\.-]+:\d+\//, ''))
	})
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
function okClick() {
	var el = $('#name').val();
	var reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{1,32}$/;
	if( !reg.test(el) ){
		$("#name").focus();
		return;
	}
	
	var el1 = Zeta$('url');
	if (el1.value.trim() == '' || el1.value.trim().indexOf('://')!=-1) {
		el1.focus();
		return;
	}
	
	$.ajax({
		url: 'updateFavouriteFolder.tv', 
		type: 'POST',
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
		}, 
		dataType: 'plain', 
		cache: false
	});
}
</script>
</head>
	<body class="openWinBody">
		<form id="folderForm" name="folderForm">
			<input type="hidden" name="favouriteFolder.folderId" value="${favouriteFolder.folderId}" />
			<div class="openWinHeader">
			    <div class="openWinTip">@input.linkNameNotNull@</div>
			    <div class="rightCirIco pageCirIco"></div>
			</div>
			<div class="edge10 pT20">
			     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			         <tbody>
			             <tr>
			                 <td class="rightBlueTxt" width="140">
								<label for="name">@td.label.name@</label>
			                 </td>
			                 <td>
								<input style="width:310px" id="name" name="favouriteFolder.name" value='${favouriteFolder.name}' class="form-control input-sm" type="text" maxlength=32	  	
								toolTip="@input.max32@" />
			                 </td>
			             </tr>
			             <tr class="darkZebraTr">
			                 <td class="rightBlueTxt">
								<label for="url">URL:</label>
			                 </td>
			                 <td>
			                 	<div class="input-group input-group-sm" style="width:310px;">
								  	<span class="input-group-addon" id="basic-addon1"></span>
								  	<input id="url" name="favouriteFolder.url" type="text" class="form-control" placeholder="favouriteUrl" aria-describedby="basic-addon1" toolTip="@input.linkAddrNotNull@"></input>
								</div>
			                 </td>
			             </tr>
			         </tbody>
			     </table>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
				         <li><a onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@button.save@</span></a></li>
				         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@button.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</form>
	</body>
</Zeta:HTML>