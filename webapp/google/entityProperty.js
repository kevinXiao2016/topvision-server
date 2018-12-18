/**
 * Entity's property.
 *
 * author:niejun
 */
function fixNodeLocation(obj) {
	var params = 'entity.entityId=' + entityId + '&entity.fixed=' + obj.checked +
		'&entity.folderId=' + folderId;
	$.ajax({url: 'updateEntityFixed.tv', type: 'POST', data: params,
	success: function() {
		var frame = window.parent.getFrame('topo' + folderId);
		if (frame != null) {
			try {
				frame.fixNodeLocation(entityId, obj.checked);
			} catch(err) {
			}	
		}		
	}, error: function() {
		window.parent.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function changeIcon() {
	window.top.createWindow('imageChooser', I18N.COMMON.selectIcon, 600, 400,
		'include/showImageChooser.tv?module=network', null,
		true, true, changeIconCallback);
}
function changeIconCallback() {
	var callback = window.top.getZetaCallback();
	if (callback != null && callback.type == 'image') {
		if (callback.path != null) {
			setEntityIcon(callback.path);	
		}	
	}	
}
function resetIcon() {
	setEntityIcon(icon48);
}
function setEntityIcon(icon) {
	var el = Zeta$('entityIcon');
	el.src = icon;
}
function changeCategory() {
	window.parent.createDialog('typeDlg', I18N.NETWORK.templateTitle, 300, 350,
			'network/popEntityTypeChooserDlg.jsp?entityId=' + entityId, null, true, true,
			updateEntityType);
}
function updateEntityType() {
	var clipboard = window.top.ZetaCallback;
	if (clipboard != null && clipboard.type == 'ok') {
		var params = 'entity.entityId=' + entityId + '&entity.typeId=' + 
		clipboard.selectedItemId;
		$.ajax({url: 'updateEntityType.tv', type: 'POST', 
			data: params, success: function() {
				window.top.ZetaCallback = null;
				location.href = 'showEntityPropertyJsp.tv?entityId=' + entityId;
			}, error: function() {
				window.parent.showErrorDlg();
			}, dataType: 'plain', cache: false});			
	}
}
function showEntityConfig() {
	window.parent.addView('entity-' + entityId, I18N.COMMON.entity + '[' + entityIp + ']',
		'entityTabIcon',
		'entity/showEditEntityJsp.tv?module=2&entityId=' + entityId);
}
function onSaveClick() {
	var n = Zeta$('nameInFolder').value.trim();
	if (n == '') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.displayNameNotNull);
		return;
	}
	var url = Zeta$('url').value.trim();
	$.ajax({url: 'updateEntityUrl.tv', type: 'POST', 
		data: jQuery(entityForm).serialize(), success: function() {
			sysUpdateEntity(entityId, n, url);
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.updateSuccessfully);
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});		
}
function viewMoreInfo() {
	window.parent.createDialog('modalDlg', entityName, 600, 450,
		'entity/viewMoreInfo.tv?entityId=' + entityId, null, true, true);
}
function sysUpdateEntity(id, name, url) {
	var frame = null;
	if (folderId == 0) {
		frame = window.top.getActiveFrame();
		if (frame != null) {
			frame.onRefreshClick();
		}
	} else {
		frame = window.top.getFrame('topo' + folderId);
		if (frame != null) {
			frame.updateNodeName(id, name, url);
		}
	}
}