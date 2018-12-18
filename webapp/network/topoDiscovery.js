
/**
 * author:niejun
 *
 * topology discovery wizard.
 */
Ext.BLANK_IMAGE_URL = "../images/s.gif";
var grid = null;
var store = null;
var tree = null;
var curIndex = 0;
var requestInterval = null;
var autoScroll = true;
var progressText = null;
var progressBar = null;
var progressMsgBox = null;
var progressTime = null;
var progressCompleted = false;
function setAutoScroll() {
	autoScroll = !autoScroll;
}
function showHelp() {
	window.open("../help/index.jsp?module=topologyWizard", "help");
}
function navClick(incr) {
	var oldIndex = curIndex;
	curIndex = curIndex + incr;
	if (curIndex == 1 || curIndex == 3) {
		// hide host service discovery
		if (incr > 0) {
			curIndex++;
		} else {
			curIndex--;
		}
	}
	Zeta$("contentstep" + oldIndex).swapNode(Zeta$("contentstep" + curIndex));
	Zeta$("prevBtn").disabled = (curIndex == 0);
	Zeta$("nextBtn").disabled = (curIndex == 5);
	if (curIndex == 0) {
		Zeta$("newMsg").innerText = I18N.NETWORK.inputHostAndNetwork;
	} else {
		if (curIndex == 1) {
			Zeta$("newMsg").innerText = I18N.NETWORK.selectService;
			loadCallback();
			Zeta$("discoveryService").checked = discoveryServiceFlag;
		} else {
			Zeta$("finishBtn").disabled = false;
			if (curIndex == 2) {
				Zeta$("newMsg").innerText = I18N.NETWORK.snmpv1Setting;
				Zeta$("onlyDiscoverySnmp").checked = onlySnmpFlag;
			} else if (curIndex == 3) {
				Zeta$("newMsg").innerText = I18N.NETWORK.telnetSetting;
				Zeta$("telnetDetected").checked = enableTelnetDetect;
				Zeta$("sshDetected").checked = enableSshDetect;
			} else if (curIndex == 4) {
				Zeta$("newMsg").innerText = I18N.NETWORK.otherSetting;
				Zeta$("nextBtn").disabled = true;
				Zeta$("autoDiscovery").checked = enableAutoDiscovery;
			}
		}
	}
}
var timeCount = 0;
function successCallback(response) {
	var json = Ext.util.JSON.decode(response.responseText);
	if (json.msg != "") {
		progressMsgBox.insertAdjacentHTML("BeforeEnd", json.msg);
		if (autoScroll) {
			progressMsgBox.doScroll("scrollbarPageDown");
		}		
	}	
	if (json.progress == 100) {
		Zeta$('progressMarquee').stop();
		Zeta$("finishBtn").disabled = false;
		Zeta$("cancelButton").disabled = false;
		Zeta$("finishBtn").value = "\u5b8c\u6210";
		progressCompleted = true;
		destroyRequestInterval();
	}
}
function failureCallback(response) {
	var json = Ext.util.JSON.decode(response.responseText);
	if (json.progress == 100) {
		Zeta$("finishBtn").disabled = false;
		Zeta$("cancelButton").disabled = false;
		Zeta$("finishBtn").value = "\u5b8c\u6210";
		progressCompleted = true;
		destroyRequestInterval();
	} else {
		progressMsgBox.insertAdjacentHTML("BeforeEnd", json.msg);
		if (autoScroll) {
			progressMsgBox.doScroll("scrollbarPageDown");
		}
	}
}
function doDiscoveryProgress() {
	timeCount = timeCount + 1;
	progressTime.innerText = String.format(I18N.NETWORK.topoTimeUsed, timeCount);
	if (timeCount % 3 == 1) {
		Ext.Ajax.request({url:"getDiscoveryProgress.tv", failure:failureCallback, success:successCallback});
	}
}
function destroyRequestInterval() {
	if (requestInterval != null) {
		clearInterval(requestInterval);
		requestInterval = null;
		Zeta$('progressMarquee').stop();
	}
}
function getSelectedItemId() {
	var modal = tree.getSelectionModel().getSelectedNode();
	var itemId = null;
	if (modal != null) {
		itemId = modal.id;
	}
	return itemId;
}
function getSelectedTreeItem() {
	return tree.getSelectionModel().getSelectedNode();
}
function getServicePort() {
	var ports = "";
	var count = store.getCount();
	var j = 0;
	for (var i = 0; i < count; i++) {
		var r = store.getAt(i).data;
		if (r.scanned) {
			ports = ports + "&topoParam.servicePorts[" + j + "]=" + r.port;
			j++;
		}
	}
	return ports;
}
function getCommunities() {
	var communities = "";
	var s = Zeta$("communities").value;
	if (s.trim() != "") {
		var arr = s.split(",");
		if (arr.length == 0) {
			arr = s.split(" ");
		}
		for (var i = 0; i < arr.length; i++) {
			communities = communities + "&topoParam.communities[" + i + "]=" + arr[i];
		}
	}
	return communities;
}
function getSeeds() {
	var seeds = "";
	var s = getIpText('seed');
	if (s.trim() != "") {
		var arr = s.split(",");
		if (arr.length == 0) {
			arr = s.split(" ");
		}
		for (var i = 0; i < arr.length; i++) {
			//seeds = seeds + "&topoParam.seeds[" + i + "]=" + arr[i];
		}
	}
	return seeds;
}
function finishClick() {
	var folderItem = getSelectedTreeItem();
	if (discoverying == false) {
		var seed = Zeta$("seed").value.trim();
		var target = Zeta$("target").value.trim();
		if (seed == '' && target == '') {
			Zeta$("contentstep" + curIndex).swapNode(Zeta$("contentstep0"));
			curIndex = 0;
			Zeta$("prevBtn").disabled = true;
			Zeta$("nextBtn").disabled = false;			
			Zeta$("target").focus();
			return;			
		}
	
		discoverying = true;
		progressCompleted = false;
		Zeta$("contentstep" + curIndex).swapNode(Zeta$("contentstep5"));
		curIndex = 5;
		Zeta$("prevBtn").disabled = true;
		Zeta$("nextBtn").disabled = true;
		Zeta$("newMsg").innerText = "\u53d1\u73b0\u72b6\u6001";

		var buffer = new Zeta$StringBuffer();
		buffer.append("topoParam.target=");
		buffer.append(target);
		buffer.append("&topoParam.excludeTarget=");
		buffer.append(Zeta$("excludeTarget").value);
		buffer.append("&topoParam.seed=");
		buffer.append(seed);
		buffer.append("&topoParam.folderId=");
		buffer.append(folderItem.id);
		if (false) {
			buffer.append("&topoParam.discoveryService=true");
			buffer.append(getServicePort());
		} else {
			buffer.append("&topoParam.discoveryService=false");
		}
		buffer.append("&topoParam.onlyDiscoverySnmp=");
		buffer.append(onlySnmpFlag);
		buffer.append("&topoParam.community=");
		buffer.append(Zeta$("communities").value);
		buffer.append(getCommunities());
		buffer.append("&topoParam.snmpPort=");
		buffer.append(Zeta$("snmpPort").value);
		buffer.append("&topoParam.snmpTimeout=");
		buffer.append(Zeta$("snmpTimeout").value);
		buffer.append("&topoParam.snmpRetry=");
		buffer.append(Zeta$("snmpRetry").value);
		buffer.append("&topoParam.telnetDetected=");
		buffer.append(enableTelnetDetect);
		buffer.append("&topoParam.sshDetected=");
		buffer.append(enableSshDetect);
		buffer.append("&topoParam.username=");
		buffer.append(Zeta$("username").value);
		buffer.append("&topoParam.passwd=");
		buffer.append(Zeta$("passwd").value);
		buffer.append("&topoParam.cmdPrompt=");
		buffer.append(Zeta$("cmdPrompt").value);
		buffer.append("&topoParam.loginPrompt=");
		buffer.append(Zeta$("loginPrompt").value);
		buffer.append("&topoParam.passwdPrompt=");
		buffer.append(Zeta$("passwdPrompt").value);
		buffer.append("&topoParam.pingTimeout=");
		buffer.append(Zeta$("pingTimeout").value);
		buffer.append("&topoParam.hasFirewall=false");
		buffer.append("&topoParam.discoveryWireless=false");
		buffer.append("&topoParam.autoCreateMonitor=");
		buffer.append(autoCreateMonitor);
		buffer.append("&topoParam.autoRefeshIpAddrBook=");
		buffer.append(autoRefeshIpAddrBook);
		buffer.append("&topoParam.autoDiscovery=");
		buffer.append(enableAutoDiscovery);
		buffer.append("&topoParam.autoDiscoveryTarget=");
		buffer.append("&topoParam.autoDiscoveryInterval=");
		buffer.append(Zeta$("autoDiscoveryInterval").value*3600000);
		Ext.Ajax.request({url:"startTopologyDiscovery.tv", method:"POST", params: buffer.toString(),
		success:function (response) {
			Zeta$("cancelButton").disabled = true;
			Zeta$("finishBtn").disabled = true;
			requestInterval = setInterval("doDiscoveryProgress()", 1000);
		}, failure:function (response) {
			Zeta$("prevBtn").disabled = false;
			window.top.showMessageDlg(I18N.COMMON.error, I18N.COMMON.operationFailure, "error");
		}});
	} else {
		if (progressCompleted) {
			var tabId = 'topo' + folderItem.id;
			var frame = window.top.getFrame(tabId);
			if (frame == null) {
				window.top.addView(tabId, I18N.NETWORK.networkTopo, "topoLeafIcon", 
					"topology/getTopoMapByFolderId.tv?folderId=" + folderItem.id);
			} else {
				window.top.setActiveTab(tabId);
				try {
					frame.onRefreshClick();
				} catch (err) {
				}
			}
			frame = window.top.getMenuFrame();
			if (frame != null) {
				try {
					frame.onRefreshFolderTreeClick();
				} catch (err) {
				}
			}
			window.top.closeWindow("modalDlg");
		}
	}
}
function cancelClick() {
	destroyRequestInterval();
	window.top.closeWindow("modalDlg");
}
function renderScanned(value, p, record) {
	return value ? "<input type=checkbox checked style=\"height:16px\">" : "<input type=checkbox style=\"height:16px\">";
}
function renderType(value, p, record) {
	switch (value) {
	  case 0:
		return I18N.NETWORK.generalService;
	  case 1:
		return I18N.NETWORK.databaseService;
	  case 2:
		return I18N.NETWORK.middlewareService;
	  case 3:
		return I18N.NETWORK.mailService;
	  default:
		return I18N.NETWORK.otherService;
	}
}
function loadCallback() {
	grid.getView().refresh();
}
var ServiceRecord = Ext.data.Record.create([{name:"name", type:"string"}, {name:"port", type:"int"}, {name:"scanned", type:"boolean"}]);
function addServiceClick() {
	var sr = new ServiceRecord({name:"", port:0, scanned:false, type:10});
	grid.stopEditing();
	store.insert(0, sr);
	grid.startEditing(0, 0);
}
function removeServiceClick() {
	store.remove(store.getAt(grid.getSelectionModel().getSelectedCell()[0]));
}
var selectAll = true;
function selectAllClick() {
	selectAll = !selectAll;
	var count = store.getCount();
	if (selectAll) {
		Zeta$("selectAllBtn").value = I18N.COMMON.deselectAll;
		for (var i = 0; i < count; i++) {
			store.getAt(i).data.scanned = true;
		}
	} else {
		Zeta$("selectAllBtn").value = I18N.COMMON.selectAllRow;
		for (var i = 0; i < count; i++) {
			store.getAt(i).data.scanned = false;
		}
	}
	grid.getView().refresh();
}
function startAutoDiscovery() {
	enableAutoDiscovery = Zeta$("autoDiscovery").checked;
	Zeta$("autoDiscoveryInterval").disabled = !enableAutoDiscovery;
}
function startAutoRefeshIpAddrBook() {
	autoRefeshIpAddrBook = Zeta$("autoRefeshIpAddrBook").checked;
}
function startAutoCreateMonitor() {
	autoCreateMonitor = Zeta$("autoCreateMonitor").checked;
}
function getDiscoveryState() {
	/*progressMsgBox.insertAdjacentHTML("BeforeEnd", "<br>......");
	if (autoScroll) {
		progressMsgBox.doScroll("scrollbarPageDown");
	}*/
	Ext.Ajax.request({url:"getDiscoveryState.tv", failure:function () {
	}, success:function (response) {
		var json = Ext.util.JSON.decode(response.responseText);
		if (json.state) {
			clearInterval(requestInterval);
			requestInterval = null;
			discoverying = false;
			Zeta$("prevBtn").disabled = false;
		}
	}});
}

Ext.onReady(function () {
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:"../topology/loadTopoFolder.tv?hasTopoFolder=false"});
	treeLoader.on("load", function () {
		var n = tree.getNodeById(folderId);
		if (n) {
			n.select();
		}
	});
	tree = new Ext.tree.TreePanel({el:"topoTree", useArrows:useArrows, autoScroll:true, animate:true, 
		border: false, trackMouseOver:false, lines:true, rootVisible:false, 
		containerScroll: true, enableDD:false, loader:treeLoader});
	var root = new Ext.tree.AsyncTreeNode({text:"Topo Folder Tree", draggable:false, id:"source"});
	tree.setRootNode(root);
	tree.render();
	root.expand();
	//var reader = new Ext.data.JsonReader({totalProperty:"rowCount", idProperty:"name", root:"data", fields:[{name:"name"}, {name:"port"}, {name:"type"}, {name:"scanned"}]});
	//var groupTpl = "{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"" + I18N.COMMON.items + "\" : \"" + I18N.COMMON.item + "\"]})";
	//store = new Ext.data.GroupingStore({url:"../network/loadNetworkServices.tv", root:"data", totalProperty:"rowCount", reader:reader, remoteSort:false, remoteGroup:false, sortInfo:{field:"name", direction:"ASC"}, groupField:"type", groupOnSort:false});
	//store.setDefaultSort("name", "asc");
	//var cm = new Ext.grid.ColumnModel([{header:I18N.COMMON.nameHeader, dataIndex:"name", groupable:false, width:100, editor:new Ext.form.TextField({allowBlank:false, minLength:1, maxLength:32})}, {header:I18N.COMMON.portHeader, dataIndex:"port", groupable:false, width:80, editor:new Ext.form.NumberField({allowBlank:false, allowDecimals:false, allowNegative:false, minValue:1, maxValue:65535, selectOnFocus:true, nanText:"1<=n<=65535"})}, {header:I18N.COMMON.typeHeader, dataIndex:"type", width:120, renderer:renderType}, {id:"scanned", header:I18N.NETWORK.scannedHeader, align:"center", dataIndex:"scanned", type:"boolean", width:80, renderer:renderScanned, editor:new Ext.form.Checkbox()}]);
	//cm.defaultSortable = true;
	//grid = new Ext.grid.EditorGridPanel({border:true, clicksToEdit:2, store:store, cm:cm, view:new Ext.grid.GroupingView({forceFit:true, hideGroupedColumn:true, enableNoGroups:true, groupTextTpl:groupTpl}), trackMouseOver:false, loadMask:{msg:I18N.COMMON.loading}, width:414, height:266});
	//grid.render("services-div");
	//store.load();
	
	/*var store1 = new Ext.data.GroupingStore({url:"../network/loadNetworkServices.tv", root:"data", totalProperty:"rowCount", reader:reader, remoteSort:false, remoteGroup:false, sortInfo:{field:"name", direction:"ASC"}, groupField:"type", groupOnSort:false});
	store1.setDefaultSort("name", "asc");
	var cm = new Ext.grid.ColumnModel([{header:I18N.COMMON.nameHeader, dataIndex:"name", groupable:false, width:100, editor:new Ext.form.TextField({allowBlank:false, minLength:1, maxLength:32})}, {header:I18N.COMMON.portHeader, dataIndex:"port", groupable:false, width:80, editor:new Ext.form.NumberField({allowBlank:false, allowDecimals:false, allowNegative:false, minValue:1, maxValue:65535, selectOnFocus:true, nanText:"1<=n<=65535"})}, {header:I18N.COMMON.typeHeader, dataIndex:"type", width:120, renderer:renderType}, {id:"scanned", header:I18N.NETWORK.scannedHeader, align:"center", dataIndex:"scanned", type:"boolean", width:80, renderer:renderScanned, editor:new Ext.form.Checkbox()}]);
	cm.defaultSortable = true;
	var grid1 = new Ext.grid.EditorGridPanel({border:true, clicksToEdit:2, store:store, cm:cm, view:new Ext.grid.GroupingView({forceFit:true, hideGroupedColumn:true, enableNoGroups:true, groupTextTpl:groupTpl}), trackMouseOver:false, loadMask:{msg:I18N.COMMON.loading}, width:414, height:266});
	grid1.render("snmp3-div");
	store1.load();*/

	progressMsgBox = Zeta$("discoverMsgBox");
	progressBar = Zeta$("progressBar-div");
	progressTime = Zeta$("discoveryTime");
	if (discoverying) {
		Zeta$("contentstep" + curIndex).swapNode(Zeta$("contentstep5"));
		curIndex = 5;
		Zeta$("prevBtn").disabled = true;
		Zeta$("nextBtn").disabled = true;
		Zeta$("newMsg").innerText = "\u53d1\u73b0\u72b6\u6001";
		progressTime.innerText = '';
		progressMsgBox.innerHTML = topologyUser + " \u6b63\u5728\u53d1\u73b0\u7f51\u7edc\u8bbe\u5907, \u8bf7\u7a0d\u7b49...";
		requestInterval = setInterval("getDiscoveryState()", 3000);
	}
	Zeta$('autoDiscoveryInterval').value = autoDiscoveryInterval;
	Zeta$('seed').value = seed;
});
