/**
 * All entity action.
 *
 * author:niejun
 */
function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
}
function locateEntity(entityIp) {
	window.parent.locateEntityByIp(entityIp);
}
function setEntityInterval(entityId) {
	createDialog('modalDlg', I18N.NETWORK.pollingInterval, 420, 250,
		'entity/showPollingInterval.tv?entityId=' + entityId, null, true, true);
}
function showPortFlowInfo(entityId, entityIp) {
	addView('interfaces-' + entityId,'@NETWORK.portRealFlow@ - ' + entityIp, 'entityTabIcon',
			'realtime/showPortFlowInfo.tv?entityId=' + entityId + '&ip=' + entityIp);
}
function showPortRealState(entityId, entityIp) {
	window.open('../realtime/showPortFlowInfo.tv?entityId=' + entityId +
		'&ip=' + entityIp, 'portreal' + entityId);
}
function showCpuAndMem(entityId, entityIp) {
	window.open('../realtime/viewCpuMemInfo.tv?entityId=' + entityId +
		'&ip=' + entityIp, 'cpumem' + entityId);
}
function viewHostInfoClick(entityId, entityIp) {
	createDialog('hostInfoDlg', I18N.NETWORK.hostRealInfo + ' - ' + entityIp, 660, 460,
		'entity/viewHostInfo.tv?entityId='+entityId, null, true, true);
}
function showPing(entityId, entityIp) {
	createDialog("modalDlg", 'Ping ' + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
}
function showTracert(entityId, entityIp) {
	createDialog("modalDlg", 'Tracert ' + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + entityIp, null, true, true);
}
function showTelnet(entityId, entityIp) {
	createDialog("modalDlg", 'Telnet ' + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=telnet&ip=" + entityIp, null, true, true);
}
function showMibBrowser(entityId, entityIp) {
	createDialog("modalDlg", I18N.NETWORK.physicalPane + " - " + entityIp, 660, 450,
		"entity/getPhysicalPane.tv?ping=" + entityId, null, true, true);
}
function onDiscoveryAgainClick(entityId, entityIp) {
	window.top.discoveryEntityAgain(entityId, entityIp, function() {
	});
}
function onLockClick() {
}
function onNewMonitorClick() {
}
function viewMoreAlarm() {
}
function onShutdownClick() {
}
function onUnloadAgentClick() {
}
function editEntityClick() {
	location.href = '../entity/showEditEntityJsp.tv?entityId=' + entityId;
}
function updateEntityState() {
	location.href = 'showEntitySnapJsp.tv?entityId=' + entityId;
}
function cancelManagement(entityId) {
	showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.confirmCancelManagement, function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = entityId;
		Ext.Ajax.request({url: 'cancelManagement.tv',
		   success: function() {
		      location.href = 'showEntitySnapJsp.tv?entityId=' + entityId;
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});			
	});
}
function viewPhysicalPane(entityId, entityIp) {
	Ext.menu.MenuMgr.hideAll();
	addView('entity-' + entityId,
		I18N.NETWORK.entity + '[' + entityIp + ']',
		'entityTabIcon', 'entity/showPhysicalPane.tv?module=7&entityId=' + entityId);
}
function showModifyIcon(entityId) {
	createDialog('modalDlg', I18N.COMMON.selectIcon, 520, 400,
		'entity/showIconChooser.tv?entityId=' + entityId, null, true, true);		
}