function findEntity() {	createDialog("findEntityDlg", I18N.MAIN.findEntity, 350, 400, 'resources/showFindEntity.tv', null, false, true);}
/*
 * 在拓扑图中定位设备
 */
function goToEntity(folderId, name, entityId) {
	var id = "topo" + folderId;
	var tab = contentPanel.getItem(id);
	if (tab) {
		contentPanel.setActiveTab(id);
		try {
			var frame = getFrame(id);
			frame.goToEntity(entityId);
		} catch (err) {}
	} else {
		addView(id, name, "topoRegionIcon", String.format("topology/showNewTopoDemo.tv?folderId={0}&entityId={1}", folderId, entityId));
	}
}
/*
 * 设备拓扑定位时的处理器
 * response: name:网络名称，entityId：需要定位的设备id，foldId：地域
 */

function goToEntityCallback(response) {
	if (response.responseText == "") {
		return;
	}
	var json = Ext.decode(response.responseText);
	if (json.folder != null && json.folder.length > 0) {
		//----当前视图是否是地域视图---//
		/**该步骤在处理时做了优化，所有的操作都经由goToEntity来做*/
		if (isTabbed) {//是拓扑图的列表查看;
			for (var i = 0; i < json.folder.length; i++) {
				//-----如果是则直接定位---//
				goToEntity(json.folder[i].folderId, json.folder[i].name, json.folder[i].entityId);
				break;
			}
		} else {
				//----否则判断地域视图是否打开如果未打开则新开一个地域视图---//
			var frame = getFrame('topo' + json.folder[0].folderId);
			if (frame != null) {
				//---调用地域视图的定位方法,并只展示第一个需要定位的设备---//
				frame.goToEntity(json.folder[0].entityId);
			} else {
				addView('topo' + json.folder[0].folderId, json.folder[0].name, "topoRegionIcon",
					//String.format("topology/getTopoMapByFolderId.tv?folderId={0}&entityId={1}", json.folder[0].folderId, json.folder[0].entityId));
					  String.format("topology/showNewTopoDemo.tv?folderId={0}&entityId={1}", json.folder[0].folderId, json.folder[0].entityId));
			}
		}
	} else {
		showMessageDlg(I18N.COMMON.tip, I18N.MAIN.notfoundInFolder);
	}
};//end function;
function goToEntityCallback2(response) {
	if (response.responseText == "") {
		return;
	}
	var json = Ext.decode(response.responseText);
	if (json.folder != null && json.folder.length > 0) {
		//----当前视图是否是地域视图---//
		/**该步骤在处理时做了优化，所有的操作都经由goToEntity来做*/
		if (isTabbed) {
			for (var i = 0; i < json.folder.length; i++) {
				//-----如果是则直接定位---//
				goToEntity(json.folder[i].folderId, json.folder[i].name, json.folder[i].entityId);
				break;
			}
		} else {
				//----否则判断地域视图是否打开如果未打开则新开一个地域视图---//
			var frame = getFrame('topo' + json.folder[0].folderId);
			if (frame != null) {
				//---调用地域视图的定位方法,并只展示第一个需要定位的设备---//
				frame.goToEntity(json.folder[0].entityId);
			} else {
				addView('topo' + json.folder[0].folderId, json.folder[0].name, "topoRegionIcon",
					//String.format("topology/getTopoMapByFolderId.tv?folderId={0}&entityId={1}", json.folder[0].folderId, json.folder[0].entityId));
					  String.format("topology/showNewTopoDemo.tv?folderId={0}&entityId={1}", json.folder[0].folderId, json.folder[0].entityId));
			}
		}
	} else {
		Ext.MessageBox.confirm(I18N.COMMON.tip, '@network/topo.addEq@', function(btn){
			if(btn == "yes"){
				window.createDialog('topoFolderTree', "@network/NETWORK.addToTopo@", 800, 500, 'network/popFolderTree.jsp', null, true, true,function(){
					var callbackObj = window.top.ZetaCallback
					if (callbackObj == null || callbackObj.type != 'ok') {return;}
					var selectedItemId = callbackObj.selectedItem.itemId
					var selectedItemName = callbackObj.selectedItem.itemName
					if (selectedItemId == null) {return}
					var entityIds = [];
					//equId是传入后台，又传出来的设备id;
					entityIds.push(json.equId);					
					Ext.Ajax.request({url: '../entity/moveEntityFromRecyle.tv',
					   success: function() {
						   var $url = String.format("topology/showNewTopoDemo.tv?folderId={0}", selectedItemId);
						   window.parent.addView("topo" + selectedItemId, selectedItemName, "topoRegionIcon", $url, null, true);
					   },
					   failure: function(){
						   window.top.showMessageDlg("@network/COMMON.error@", "@RESOURCES/RECYLE.moveEntityFailure@", 'error')
					   },
					   params: {destFolderId:selectedItemId, entityIds:entityIds}
					})
					window.top.ZetaCallback = null
				});
			}
		});
	}
};//end goToEntityCallback2;

function locateEntityByIp(ip) {
	sendRequest("network/findEntity.tv", "GET", {ip: ip}, goToEntityCallback, defaultFailureCallback);
}

//上面这个函数当设备没有在拓扑文件夹中的时候，只会给出提示信息。需要可以直接增加进入拓扑文件夹,因此增加一个函数;
function locateEntityByIp_Id(ip,paraId){
	sendRequest("network/findEntity.tv", "GET", {ip: ip, equId: paraId}, goToEntityCallback2, defaultFailureCallback);	
}

function discoveryEntityAgain(entityId, ip, successCallback) {
	if(ip != null) {
		window.parent.showWaitingDlg("@COMMON.wait@", String.format("@network/NETWORK.reTopoing@", ip),'waitingMsg','ext-mb-waiting');
	}else{
		window.parent.showWaitingDlg("@COMMON.wait@","@network/NETWORK.reTopoingA@",'waitingMsg','ext-mb-waiting');
	}
	// 重新拓扑之前，重置超时时间
	Ext.Ajax.timeout = 1800000;
	sendRequest('/topology/discoveryEntityAgain.tv', 'POST', {entityId: entityId}, function(response) {
			window.top.closeWaitingDlg();
			var commonResponse = Ext.decode(response.responseText);
	        if(commonResponse.code == 200){
	        	successCallback && successCallback();
	        	//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.refreshEntitySuccess@");
	        	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
	   			});
	        }else{
	        	window.top.showMessageDlg("@COMMON.tip@", commonResponse.msg);
	        }
	}, function() {
		window.top.closeWaitingDlg();
		window.top.showMessageDlg("@COMMON.tip@", "@COMMON.timeout@");
	});
}