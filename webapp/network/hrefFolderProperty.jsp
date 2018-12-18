<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var folderId = <s:property value="topoFolder.folderId"/>;
var superiorId = <s:property value="topoFolder.superiorId"/>;
var folderName = '<s:property value="topoFolder.name"/>';
function onSaveClick() {
	var n = Zeta$('name').value.trim();
	if (n == '') {
		showMessageDlg(I18N.MENU.tip, I18N.herfFolderProperty.note1);
		return;
	}
	$.ajax({url: '../topology/updateTopoFolderOutline.tv', type: 'POST', 
		data: jQuery(folderForm).serialize(), success: function(response) {
			sycUpdateFolderName(folderId, n);
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}
function sycUpdateFolderName(id, name) {
	var frame = window.top.getFrame('topo' + superiorId); 
	if (folderName != name) {
		try {
			window.top.getMenuFrame().renameTopoFolder(id, name);
		} catch (err) {
		}
		try {
			frame.synRenameNode(id, name);
		} catch (err) {
		}
	}
	try {
		frame.synUpdateFolderHref(id, Zeta$('url').value.trim());
	} catch (err) {
	}
	window.top.showMessageDlg(I18N.MENU.tip, I18N.herfFolderProperty.modifyremotesystemsuccess);
}
function popIconFile() {
	window.top.createWindow('imageChooser', I18N.herfFolderProperty.selectIcon, 600, 400,
		'include/showImageChooser.tv??module=network', null,
		true, true, setFolderIcon);
}
function setFolderIcon() {
	var callback = window.top.getZetaCallback();
	if (callback != null && callback.type == 'image') {
		if (callback.path != null) {
			var params = 'topoFolder.folderId=' + folderId + '&topoFolder.icon=' + callback.path;
			$.ajax({url: '../topology/updateTopoFolderIcon.tv', type: 'POST', 
				data: params, success: function() {
					var el = Zeta$('icon');
					el.src = callback.path;
					var frame = window.top.getFrame('topo' + superiorId);
					if (frame != null) {
						try {
							frame.synUpdateNodeIcon(folderId, callback.path, el.width, el.height);
						} catch(err) {
						}	
					}
				}, error: function() {
					showErrorDlg();
				}, dataType: 'plain', cache: false});		
		}	
	}
}
function fixNodeLocation(obj) {
	var params = 'topoFolder.folderId=' + folderId + '&topoFolder.fixed=' + (obj.checked ? '1' : '0');
	$.ajax({url: 'updateFolderFixed.tv', type: 'POST', data: params,
	success: function() {
		var frame = window.top.getFrame('topo' + superiorId);
		if (frame) {
			try {
				frame.fixNodeLocation(folderId, obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.top.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
</script>
</head>
<body class=BLANK_WND style="padding: 5px;">
	<form id="folderForm" name="folderForm" align="center">
		<input type=hidden name="topoFolder.folderId"
			value="<s:property value="topoFolder.folderId"/>"> 
		<input type=hidden name="folderId"
			value="<s:property value="topoFolder.folderId"/>">
		<table width=300 cellspacing=5 cellpadding=0>
			<tr>
				<td align=center>
				<fieldSet>
						<legend><fmt:message key="summaryTitle" bundle="${resources}" /></legend>
						<table width=100% cellspacing=5>
							<tr>
								<td width=100px><fmt:message key="label.type" bundle="${resources}" /></td>
								<td><fmt:message key="folder.type8" bundle="${resources}" /></td>
							</tr>
							<tr>
								<td><fmt:message key="label.name" bundle="${resources}" /><font color=red>*</font>
								</td>
								<td align=right><input id="name" class=iptxt
									name="topoFolder.name" style="width: 200px;" type=text
									maxlength=24 value="<s:property value="topoFolder.name"/>">
								</td>
							</tr>
							<tr>
								<td><fmt:message key="NETWORK.href" bundle="${resources}" />:</td>
								<td align=right><textarea class=iptxa rows=3 id="url"
										name="topoFolder.url" style="width: 200px; overflow: auto">
											<s:property value="topoFolder.url" />
										</textarea></td>
							</tr>
							<tr>
								<td><fmt:message key="label.link.note" bundle="${resources}" /></td>
								<td align=right><textarea class=iptxa rows=5
										name="topoFolder.note" style="width: 200px; overflow: auto">
											<s:property value="topoFolder.note" />
										</textarea></td>
							</tr>
							<tr>
								<td colspan=2 align=right>
									<button class=BUTTON75
										onMouseOver="this.className='BUTTON_OVER75'"
										onMouseOut="this.className='BUTTON75'"
										onMouseDown="this.className='BUTTON_PRESSED75'"
										onclick="onSaveClick()"><fmt:message key="herfFolderProperty.modify" bundle="${resources}" /></button>
								</td>
							</tr>
						</table>
					</fieldSet>
				</td>
			</tr>
			<tr>
				<td align=center><fieldSet>
						<legend><fmt:message key="NETWORK.nodeProperty" bundle="${resources}" /></legend>
						<table width=100% cellspacing=5>
							<tr>
								<td><fmt:message key="herfFolderProperty.Nodeicon" bundle="${resources}" /></td>
								<td><img id="icon"
									src="<s:property value="topoFolder.icon"/>" border=0
									align=absmiddle></td>
							</tr>
							<tr>
								<td></td>
								<td align=right>
									<button id="imgBt" style="margin-left: 72px;" class=BUTTON75
										onMouseOver="this.className='BUTTON_OVER75'"
										onMouseOut="this.className='BUTTON75'"
										onMouseDown="this.className='BUTTON_PRESSED75'"
										onclick="popIconFile()"><fmt:message key="herfFolderProperty.select" bundle="${resources}" /></button>
								</td>
							</tr>
							<tr>
								<td colspan=2><input id="fixed" type=checkbox
									<s:if test='%{topoFolder.fixed=="1"}'>checked</s:if>
									onclick="fixNodeLocation(this);"><label for="fixed"><fmt:message key="NETWORK.fixNodeLocation" bundle="${resources}" /></label>
								</td>
							</tr>
						</table>
					</fieldSet>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>