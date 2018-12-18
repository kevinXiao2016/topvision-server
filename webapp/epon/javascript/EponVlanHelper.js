var CASCADE_LOCK, CONTEXTMENU_LOCK;
function contextHandler(node,e) {
    if(!e){
        currentId = this.id;
        e = node;
    }else{
        currentId = node;
    }
    selectionModel.reset();
    if(e.type == 'click' ){
        var tempPort = selectionModel.getSelectedItem();
        var portIndex = tempPort.portIndex;
        if (ctrlFlag == 0) {//  如果没有按ctrl键，则清除之前的样式
            clearPage();
            indexCache =[];
        }
        if (divCache.contains(this.id)) {
            removeObj(divCache, this.id);
            indexCache.remove(portIndex);
            this.style.border = "1px solid transparent";
        } else {
            divCache.add(this.id);          //并记录本次div 的id,放入divCache中
            indexCache.add(portIndex);      //记录本次端口Index,放入indexCache中 
            this.style.border = "1px solid "+divStyle.click;
        }
        //如果加了锁就不执行了，表示的是 context触发的click
        if( CONTEXTMENU_LOCK ){
        	return;
        }
    }else{
    	preventBubble(e);
    	CONTEXTMENU_LOCK = true;
    	$("#"+currentId).click();
    }
    
    //如果没有端口被选中
    if(indexCache.length == 0){
        var items = [];
        return displayService(items,e);
    }

    if (!CASCADE_LOCK) {
	    CASCADE_LOCK = true;
		preventBubble(e);
		if (noSelected == false) {
			var tempPort = selectionModel.getSelectedItem();
			var portIndex = tempPort.portIndex;
			var taggedFlag = 0; // 记录选中的tagged端口个数
			var untaggedFlag = 0;// 记录选中的untagged端口个数
			var taggedList = vlanListJson.data[vlanSelected].taggedPortIndexList;
			var untaggedList = vlanListJson.data[vlanSelected].untaggedPortIndexList;
			for (i = 0; i < indexCache.length; i++) {
				for (j = 0; j < taggedList.length; j++) {
					if (indexCache[i] == taggedList[j]) {
						taggedFlag = taggedFlag + 1;
					}
				}
				for (k = 0; k < untaggedList.length; k++) {
					if (indexCache[i] == untaggedList[k]) {
						untaggedFlag = untaggedFlag + 1;
					}
				}
			}
			if (taggedFlag == indexCache.length) {
				displayService(buildSniLeaveVlanMenu2Items(),e);
			} else if (untaggedFlag == indexCache.length) {
			    displayService(buildSniLeaveVlanMenu1Items(),e);
			} else if (taggedFlag == 0 && untaggedFlag == 0) {
			    displayService(buildSniToVlanMenuItems(),e);
			} else {
			    var items = [];
			    displayService(items,e);
			}
		}
		CASCADE_LOCK = false;
	}
    CONTEXTMENU_LOCK = false;
}

function rowClick(vlan_grid, rowIndex) {
	clearTreePanel();
	rowSelected = rowIndex;
	var record = vlan_grid.getStore().getAt(rowSelected);
	for (i = 0; i < vlanListJson.data.length; i++) {
		if (record.data.vlanIndex == vlanListJson.data[i].vlanIndex) {
			vlanSelected = i;
		}
	}
	if (modeFlag == 2) {
		$("#device_text").find("div").css({
			backgroundColor : 'transparent'
		});
	}
	if (vlanListJson.data.length != 0) {
		noSelected = false;
		vlan_attrStore = {
			"VLAN ID" : vlanListJson.data[vlanSelected].vlanIndex,
			'@VLAN.vlanDesc@' : vlanListJson.data[vlanSelected].oltVlanName,
			'@VLAN.vlanBdMode@' : floodMode[vlanListJson.data[vlanSelected].topMcFloodMode],
			'@VLAN.uPortNum@' : vlanListJson.data[vlanSelected].untaggedPortIndexList.length,
			'@VLAN.tPortNum@' : vlanListJson.data[vlanSelected].taggedPortIndexList.length
		}
		clearPage();
		clearPage2();
		var taggedPorts = vlanListJson.data[vlanSelected].taggedPortIndexList;
		var untaggedPorts = vlanListJson.data[vlanSelected].untaggedPortIndexList;
		$.each(taggedPorts,function(i,$portIndex){
			var tagId = IndexToId($portIndex);
			var $port = selectionModel.getSelectedItem(tagId);
			$("#" + tagId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}2.png)',$port.portSubType));
			tagId && divCache2.add(tagId);
		});
		$.each(untaggedPorts,function(i,$portIndex){
			var untagId = IndexToId($portIndex);
			var $port = selectionModel.getSelectedItem(untagId);
			$("#" + untagId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}1.png)',$port.portSubType));
			divCache2.add(untagId);
		});
		vlan_attr_grid.setSource(vlan_attrStore);
		// /定位到TEXT文本区
		if (modeFlag == 2) {
			$textblock = $('#TextVlan' + vlanListJson.data[vlanSelected].vlanIndex);
			$textblock.css("backgroundColor" , 'gray');
			$("#device_text").scrollTop(-1000);
			var textTop = $textblock.position().top;
			$("#device_text").scrollTop(textTop);
		}
	} else {
		noSelected = true;
		vlan_attrStore = {
			"VLAN ID" : "",
			'@VLAN.vlanDesc@' : "",
			'@VLAN.uPortNum"/>' : "",
			'@VLAN.tPortNum"/>' : ""
		}
		clearPage();
		clearPage2();
		vlan_attr_grid.setSource(vlan_attrStore);
		if (modeFlag == 2) {
			$("#device_text").empty();
		}
	}
}

function delVlan(list, re, deleteVlans) {
	var vlanIndex = list.toString();
	$.ajax({
		url : "/epon/vlan/deleteOltVlan.tv",
		data : "entityId=" + entityId + "&vidListStr=" + vlanIndex
				+ "&deleteVlans=" + deleteVlans,
		method : "post",
		success : function(text) {
			closeDelWaitingDlg();
			if (text == 'success') {
				top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: I18N.VLAN.selectVlanDelSuc
       			});
			} else if (text == 'deleteFail') {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.VLAN.selectVlanDelFail);
			} else {
				// 没能删除成功的vlan(10个以内)
				var temp = text.substring(1, text.length - 1);
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(
						"@VLAN.selectVlanDelSubFail@", temp));
			}
			loadVlanGlobalInfo();
			loadVlanListJson();
			vlan_store.loadData(vlanListJson.data);
			vlan_grid.selModel.selectRow(0, false);
			rowClick(vlan_grid, 0);
			$("#vlanIndex").val("");
			if (modeFlag == 2) {
				changeToText();
			}
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip,
					I18N.VLAN.delVlanError, 'error');
			loadVlanGlobalInfo();
			loadVlanListJson();
			vlan_store.loadData(vlanListJson.data);
			vlan_grid.selModel.selectRow(0, false);
			rowClick(vlan_grid, 0);
			$("#vlanIndex").val("");
			if (modeFlag == 2) {
				changeToText();
			}
		},
		cache : false
	});
}

function clearBorderStyle(id) {
	$temStyle = $("#" + id);
	if ($temStyle.css("borderColor") != divStyle.click) {
		$temStyle.animate({
			opacity : 0.7
		}, {
			duration : "fast"
		// complete : checkAnimateStyle
		});
		$temStyle.css({
			border : '0px solid ' + divStyle.hover
		});
	}
}

function clearPage(cid) {
	while (divCache.length != 0) {
		var divCacheObject = divCache.pop();
		if (cid != divCacheObject) {// 如果点击的是当前div，则不做修改
			$temStyle = $("#" + divCacheObject);
			if ("OLT" == divCacheObject) {
				$temStyle.css({
					border : '2px solid transparent'
				});
				return;
			}
			var arr = divCacheObject.split('_');
			if (arr[2] > 0) {
				$temStyle.css({
					opacity : 1,
					border : '1px solid transparent'
				});
			} else if (arr[2] == 0 && arr[1] < 19 && arr[0] != 'power'
					&& arr[0] != 'fan') {// 是板卡
				$temStyle.css({
					opacity : 0.7,
					border : '2px solid #71787e'
				});
			} else {// 是风扇或者电源
				$temStyle.css({
					opacity : 0.7,
					border : '2px solid transparent'
				});
			}
			if ($temStyle.css("opacity") == 1 && $temStyle.css("height") == ""+ coordinateParam.slotHeight + "px") {
				$temStyle.css({
					"opacity" : 0.7
				});
			}
		}
	}
}

function setBorderStyle(id) {
	$temStyle = $("#" + id);
	if ($temStyle.css("borderColor") == divStyle.click) {
		$temStyle.css({
			opacity : 1
		});
	} else {
		$temStyle.css({
			opacity : 1,
			border : '1px solid ' + divStyle.hover
		});
	}
}
function containsObj(array, obj) {
	return array.indexOf(obj) != -1;
}

function changeToText() {
	modeFlag = 2;
	$("#device_container").css('display', 'none');
	$("#device_text").css('display', 'block');
	$("#graphModel").attr('class', '');
	$("#textModel").attr('class', 'selected');
	var sb = new Zeta$StringBuffer();
	var t = '&emsp;';
	for (i = 0; i < vlanListJson.data.length; i++) {
		sb.append('<div id=TextVlan' + vlanListJson.data[i].vlanIndex + '>');
		sb.append('<p>VLAN ID:' + vlanListJson.data[i].vlanIndex + '</p>');
		sb.append('<p>Description:' + vlanListJson.data[i].oltVlanName
						+ '</p>');
		sb.append('<p>Multicast flood mode:'
				+ floodMode[vlanListJson.data[i].topMcFloodMode] + '</p>');
		sb.append('<p>Tagged ports:</p>');
		for ( var j = 0; j < vlanListJson.data[i].taggedPortIndexList.length; j++) {
			if (j != vlanListJson.data[i].taggedPortIndexList.length - 1) {
				sb.append('<p>'
						+ t
						+ t
						+ vlanListJson.data[i].tagPortNameList[j]
						+ '<br>');
			} else {
				sb.append(t
						+ t
						+ vlanListJson.data[i].tagPortNameList[j]
						+ '</p>');
			}
		}
		sb.append('<p>Untagged ports:</p>');
		for ( var k = 0; k < vlanListJson.data[i].untaggedPortIndexList.length; k++) {
			if (k != vlanListJson.data[i].untaggedPortIndexList.length - 1) {
				sb.append('<p>'
						+ t
						+ t
						+ vlanListJson.data[i].unTagPortNameList[k]
						+ '<br>');
			} else {
				sb.append(t
						+ t
						+ vlanListJson.data[i].unTagPortNameList[k]
						+ '</p>');
			}
		}
		sb.append('</div>');
		sb.append('<p>---------------------------------------</p>');
	}
	$('#device_text').html(sb.toString());
	vlan_grid.selModel.selectRow(rowSelected, true);
	rowClick(vlan_grid, rowSelected);
}
function changeToGraph() {
	modeFlag = 1;
	$("#device_container").css("display", 'block');
	$("#device_text").css("display", 'none');
	$("#graphModel").attr('class', 'selected');
	$("#textModel").attr('class', '');
}
function checkVlanId() {
	var reg0 = /^([0-9])+$/;
	var vlanIndex = $("#vlanIndex").val();
	if (vlanIndex == "" || vlanIndex == null) {
		return false;
	} else {
		return reg0.exec(vlanIndex);
	}
}
function addVlan() {
	var vlanIndex = $("#vlanIndex").val();
	if (checkedInputList(vlanIndex)
			&& ((vlanIndex.indexOf(",") + 1) | (vlanIndex.indexOf("-") + 1))) {
		window.top.createDialog('oltVlanBatchAdd', I18N.VLAN.add + 'VLAN', 800,
				500, '/epon/vlan/showVlanBatchAdd.tv?entityId=' + entityId
						+ "&vidListStr=" + vlanIndex, null, true, true, function(){
			window.location.reload();
		});
		return;
	}
	var flag = false;// 标记新增vlanId是否存在
	modifyFlag = false;
	for (i = 0; i < vlanListJson.data.length; i++) {
		if (vlanListJson.data[i].vlanIndex == $("#vlanIndex").val()) {
			flag = true;
		}
	}
	if (checkVlanId() && 0 < vlanIndex && vlanIndex < 4095) {
		if (flag == true) {
			window.parent.showMessageDlg(I18N.COMMON.message,
					I18N.VLAN.vlanIdExist);
		} else {
			window.top.createDialog('setVlanName', I18N.VLAN.setVlan, 600, 280,
					'/epon/vlan/showVlanNameJsp.tv?entityId=' + entityId
							+ "&vlanIndex=" + vlanIndex + "&modifyFlag="
							+ modifyFlag, null, true, true);
		}
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.inputBatchTip);
	}
}
function updateOltVlanListJson(modifyFlag) {
	if (modifyFlag == 'true') {
		loadVlanListJson();
		if (modeFlag == 2) {
			changeToText();
		}
		vlan_store.loadData(vlanListJson.data);
		vlan_grid.selModel.selectRow(vlanSelected, true);
		rowClick(vlan_grid, vlanSelected);
		vlan_grid.getView().focusRow(vlanSelected);
		top.afterSaveOrDelete({
			title: I18N.COMMON.tip,
			html: I18N.VLAN.modifySuccess
		});
		$("#vlanIndex").focus();
		/*window.parent.showMessageDlg(I18N.COMMON.message,
				I18N.VLAN.modifySuccess, null, function() {
					$("#vlanIndex").focus();
				});*/
	} else if (modifyFlag == 'false') {
		loadVlanGlobalInfo();
		loadVlanListJson();
		if (modeFlag == 2) {
			changeToText();
		}
		vlan_store.loadData(vlanListJson.data);
		var j = 0;
		for (i = 0; i < vlanListJson.data.length; i++) {
			if (vlanListJson.data[i].vlanIndex == $("#vlanIndex").val()) {
				j = i;
				break;
			}
		}
		vlan_grid.selModel.selectRow(j, true);
		rowClick(vlan_grid, j);
		$("#vlanIndex").val("");
		vlan_grid.getView().focusRow(j);
		top.afterSaveOrDelete({
			title: I18N.COMMON.tip,
			html: I18N.VLAN.addSuccess
		});
		$("#vlanIndex").focus();
		/*window.parent.showMessageDlg(I18N.COMMON.message, I18N.VLAN.addSuccess,
				null, function() {
					$("#vlanIndex").focus();
				});*/
	}
}

function updataVlanVifFlag(vlanIndex) {
	for (i = 0; i < vlanListJson.data.length; i++) {
		if (vlanListJson.data[i].vlanIndex == vlanIndex) {
			if (vlanListJson.data[i].vlanVifFlag == 1) {
				vlanListJson.data[i].vlanVifFlag = 0
			} else if (vlanListJson.data[i].vlanVifFlag == 0) {
				vlanListJson.data[i].vlanVifFlag = 1
			}
		}
	}
	vlan_store.loadData(vlanListJson.data);
}
function closeDelWaitingDlg() {
	$("#delWaitingDlgDiv").hide();
	if (delWaitingDlg != null) {
		delWaitingDlg.hide();
	}
}
function delVlanMgmt(list, re, deleteVlans) {
	var listL = list.length;
	if (listL > 0) {
		delVlan(list, re, deleteVlans);
	}
}
function bulidVlanGrid() {
	// vlan列表
	vlan_cm = new Ext.grid.ColumnModel([ 
		{header : 'VLAN ID', dataIndex : 'vlanIndex',width : 60,align : 'center'}, 
		{header : "@VLAN.vlanDesc@", dataIndex : 'oltVlanName',width : 100,align : 'center'}, 
		{header : "@VLAN.vlanBdMode@", dataIndex : 'topMcFloodMode',width : 100,align : 'center',hidden : true}, 
		{header : "@VLAN.ViTag@", dataIndex : 'vlanVifFlag',width : 100,align : 'center',hidden : true} 
	]);
	vlan_store = new Ext.data.JsonStore({
		proxy : new Ext.ux.data.PagingMemoryProxy(vlanListJson.data),
		fields : ["vlanIndex","oltVlanName","topMcFloodMode","vlanVifFlag"],
		baseParams: { start : 0,limit : pageSize },
		listeners : {
			'load' : function() {
				var rowNum = this.find("vlanIndex", selectedVlanIndex);
				vlan_grid.getSelectionModel().selectRow(rowNum);
			}
		}
	});
	function buildPagingToolBar() {
		pagingToolbar = new Ext.PagingToolbar({pageSize: pageSize, store:vlan_store, displayInfo:true, cls:"extPagingBar"
			,items: ["-", String.format("@COMMON.pagingTip@", pageSize), '-']
			});
		return pagingToolbar;
	}
	vlan_grid = new Ext.grid.GridPanel({
		viewConfig : {
			forceFit : true
		},
		stripeRows : true,
		region : "center",
		bodyCssClass : 'normalTable',
		id : "vlanGrid",
		height : $("#viewLeftPartBody").height(),
		store : vlan_store,
		cm : vlan_cm,
		renderTo : "viewLeftPartBody",
		listeners : {
			viewready : function() {
				vlan_grid.getSelectionModel().selectFirstRow();
				rowClick(vlan_grid, 0);
			}
		},
		tbar : new Ext.Toolbar({
			scope : this,
			items : [ {
				xtype : "textfield",
				id : "vlanIndex",
				disabled : !operationDevicePower,
				width : 85
			}, {
				id : "addVlan",
				text : I18N.COMMON.add,
				tooltip : I18N.VLAN.addVlan,
				disabled : !operationDevicePower,
				handler : addVlan
			}, {
				id : "deleteVlan",
				text : I18N.COMMON.del,
				tooltip : I18N.VLAN.deleteVlan,
				disabled : !operationDevicePower,
				handler : deleteVlanClick
			} ]
		}),
		bbar: buildPagingToolBar()
	});

	vlan_grid.on('rowcontextmenu', function(vlan_grid, rowIndex, e) {
		e.preventDefault();
		var sm = vlan_grid.getSelectionModel();
		var record = vlan_grid.getStore().getAt(rowIndex);
		selectedVlanIndex = record.data.vlanIndex;
		var flag = record.data.vlanVifFlag;
		var logicInterface = !VersionControl.support("logicInterfaceConfig");
		var sels = sm.selections.items;
		var items = [];
		if (sels.length == 1 || !sm.isSelected(rowIndex)) {
			sm.clearSelections();
			items.push({
				id : 'setVlanName1',
				autoScroll : false,
				text : I18N.VLAN.modifyVlan,
				disabled : !operationDevicePower,
				handler : function() {
					var nodes = vlan_grid.getSelectionModel().getSelections();
					var vlanIndex = nodes[0].json.vlanIndex;
					var vlanName = nodes[0].json.oltVlanName;
					var topMcFloodMode = nodes[0].json.topMcFloodMode;
					modifyFlag = true;
					window.parent.createDialog("setVlanName","@VLAN.modifyVlan@", 600, 280,"/epon/vlan/showVlanNameJsp.tv?entityId="
									+ entityId + "&vlanIndex=" + vlanIndex
									+ "&modifyFlag=" + modifyFlag
									+ "&oltVlanName=" + vlanName
									+ "&topMcFloodMode=" + topMcFloodMode,null, true, true);
				}
			});
			if (flag && logicInterface) {
				items.push({
					id : 'setVlanVif',
					autoScroll : false,
					text : I18N.EPON.cfgVlanVI,
					handler : function() {
						var nodes = vlan_grid.getSelectionModel().getSelections();
						var vlanIndex = nodes[0].json.vlanIndex;
						top.createDialog("setVlanVif","@EPON.cfgVlanVI@", 650, 435,"/epon/vlan/setVlanVifJsp.tv?entityId="+ entityId + "&vlanIndex=" + vlanIndex,null, true, true);
					}
				});
			} else {
				if(logicInterface){
					items.push({
						id : 'createVlanVif',
						autoScroll : false,
						text : I18N.VLAN.vlanVisAdd,
						disabled : !operationDevicePower,
						handler : function() {
							var vlanVifFlag = 1;// 用于标识页面跳转：1为主ip设置，2为新增子ip，3为修改子ip
							var nodes = vlan_grid.getSelectionModel().getSelections();
							var vlanIndex = nodes[0].json.vlanIndex;
							top.createDialog("createVlanVif","@VLAN.vlanVisAdd@", 600, 240,"/epon/vlan/createVlanVifJsp.tv?entityId="+ entityId + "&vlanIndex=" + vlanIndex+ "&vlanVifFlag=" + vlanVifFlag, null,true, true);
						}
					});
				}
			}
			sm.selectRow(rowIndex);
		}
		items.push({
			id : 'deleteVlan1',
			autoScroll : false,
			text : I18N.VLAN.deleteVlan,
			disabled : !operationDevicePower,
			handler : deleteVlan
		});
		loadVlanMenu(items);
		vlanMenu.showAt(e.getPoint());
	});
	vlan_store.reload();

	vlan_grid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
		selectedIndex = rowIndex;
	});
	// vlan列表下输入框过滤
	$("#vlanIndex").keyup(function() {
		bigControlFlag++;
		var text = jQuery("#vlanIndex").val();
		if (!text) {
			$("#vlanIndex").css("border", "1px solid #8bb8f3");
			vlan_store.loadData(vlanListJson.data);
			return;
		}
		if (!checkedInputList(text)) {
			return;
		}
		var arr = changeToArray(text);
		if (arr.length < 300 || vlanListJson.data.length < 200) {
			//将查询改为从全部数据中查询,以解决页面分页导致的无法查询其他页数据问题
			var selectVlanList = new Array();
			$.each(vlanListJson.data,function(index, item){
				if(arr.indexOf(item.vlanIndex) > -1){
					selectVlanList.push(item);
				}
			})
			vlan_store.loadData(selectVlanList);
		} else {
			var tmpN = bigControlFlag;
			setTimeout(function() {
				if (tmpN == bigControlFlag) {
					/*vlan_store.filterBy(function(record) {
						return arr.indexOf(record.get("vlanIndex")) > -1;
					});*/
					//将查询改为从全部数据中查询,以解决页面分页导致的无法查询其他页数据问题
					var selectVlanList = new Array();
					$.each(vlanListJson.data,function(index, item){
						if(arr.indexOf(item.vlanIndex) > -1){
							selectVlanList.push(item);
						}
					})
					vlan_store.loadData(selectVlanList);
				}
			}, 800);
		}
		if (vlan_store.getCount() > 0) {
			vlan_grid.selModel.selectRow(0, true);
			rowClick(vlan_grid, 0);
		}
	});

	if (Ext.grid.PropertyColumnModel) {
		Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
			nameText : I18N.COMMON.property,
			valueText : I18N.COMMON.propertyValue,
			dateFormat : I18N.VLAN.format
		});
	}
	vlan_attr_grid = new Ext.grid.PropertyGrid({
		renderTo : 'viewRightPartBottomBody',
		autoHeight : true,
		hideHeaders : true,
		border : false,
		autoScroll : true,
		source : vlan_attrStore,
		listeners : {
			'render' : function(proGrid) {
				var view = proGrid.getView();
				var store = proGrid.getStore();
				proGrid.tip = new Ext.ToolTip({
					target : view.mainBody,
					delegate : '.x-grid3-row',
					trackMouse : true,
					renderTo : document.body,
					listeners : {
						beforeshow : function updateTipBody(tip) {
							var rowIndex = view.findRowIndex(tip.triggerElement);
							tip.body.dom.innerHTML = store.getAt(rowIndex).get('value');
						}
					}
				});
			}
		}
	});
	// 表格不可编辑
	vlan_attr_grid.on("beforeedit", function(e) {
		e.cancel = true;
		return false;
	});
	// 表格单击事件
	vlan_grid.on('rowclick', rowClick);
}

function loadVlanMenu(items) {
	vlanMenu = new Ext.menu.Menu({
		items : items
	});
	loadVlanMenu = function(items) {
		vlanMenu.removeAll();
		vlanMenu.add(items);
	}
}

function checkedInputList(v) {
	$("#vlanIndex").css("border", "1px solid #ff0000");
	var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	if (reg.exec(v)) {
		var tmp = v.replace(new RegExp('-', 'g'), ',');
		var tmpA = tmp.split(',');
		var tmpAl = tmpA.length;
		for ( var i = 0; i < tmpAl; i++) {
			if (parseFloat(tmpA[i]) > 4094 || tmpA[i] == 0) {
				return false;
			}
		}
		$("#vlanIndex").css("border", "1px solid #8bb8f3");
		return true;
	}
	return false;
}
// 解析逗号和连字符组成的字符串为数组
function changeToArray(v) {
	var re = new Array();
	var t = v.split(",");
	var tl = t.length;
	for ( var i = 0; i < tl; i++) {
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if (ttI > 0) {
			var ttt = tt.split("-");
			if (ttt.length == 2) {
				var low = parseFloat(parseFloat(ttt[0]) > parseFloat(ttt[1]) ? ttt[1]
						: ttt[0]);
				var tttl = Math.abs(parseFloat(ttt[0]) - parseFloat(ttt[1]));
				for ( var u = 0; u < tttl + 1; u++) {
					re.push(low + u);
				}
			}
		} else if (ttI == -1) {
			re.push(parseFloat(tt));
		}
	}
	var rel = re.length;
	if (rel > 1) {
		var o = {};
		for ( var k = 0; k < rel; k++) {
			o[re[k]] = true;
		}
		re = new Array();
		for ( var x in o) {
			if (x > 0 && o.hasOwnProperty(x)) {
				re.push(parseFloat(x));
			}
		}
		re.sort(function(a, b) {
			return a - b;
		});
	}
	return re;
}
function changeToString(list) {
	var re = new Array();
	if (list.length > 1) {
		list.sort(function(a, b) {
			return a - b;
		});
		var f = 0;
		var n = 0;
		var ll = list.length;
		for ( var i = 1; i < ll; i++) {
			if (list[i] == list[i - 1] + 1) {
				n++;
			} else {
				re = _changeToString(re, f, n, list);
				f = i;
				n = 0;
			}
			if (i == ll - 1) {
				re = _changeToString(re, f, n, list);
			}
		}
	} else if (list.length == 1) {
		re.push(list[0]);
	}
	return re;
}
function _changeToString(re, f, n, list) {
	if (n == 0) {
		re.push(list[f]);
	} else if (n == 1) {
		re.push(list[f]);
		re.push(list[f + 1]);
	} else {
		re.push(list[f] + "-" + list[f + n]);
	}
	return re;
}
function deleteVlanClick() {
	var text = $("#vlanIndex").val();
	if (!text) {
		var hasSelection = vlan_grid.getSelectionModel().hasSelection ();
		if( hasSelection ){
			deleteVlan();
		}
		return;
	}
	if (!checkedInputList(text)) {
		return;
	}
	
	var tmpL = changeToArray(text);
	var list = new Array();
	var notList = new Array();
	for ( var x = 0; x < vlanListJson.data.length; x++) {
		var v = vlanListJson.data[x].vlanIndex;
		if (tmpL.indexOf(v) > -1) {
			if (vlanListJson.data[x].vlanVifFlag == 1) {
				notList.push(v);
			} else {
				list.push(v);
			}
		}
	}
	deleteVlan_(list, notList);
}
function deleteVlan() {
	var nodes = vlan_grid.getSelectionModel().selections.items;
	var list = new Array();
	var notList = new Array();
	for ( var x = 0; nodes[x]; x++) {
		var vlanIndex = nodes[x].json.vlanIndex;
		if (nodes[x].json.vlanVifFlag == 1) {
			notList.push(vlanIndex);
		} else {
			list.push(vlanIndex);
		}
	}
	deleteVlan_(list, notList);
}
function deleteVlan_(list, notList) {
	var delList = list.slice(0);
	list = changeToString(list);
	notList = changeToString(notList);
	var str = "";
	var notL = notList.length;
	var listL = list.length;
	if (listL == 0) {
		if (notL > 0) {
			// I18N.VLAN.virConflictTip : VLAN：{0}
			// 配置了VLAN虚接口,不能直接删除,请先删除其VLAN虚接口!
			str = String.format(I18N.VLAN.virConflictTip, notList.join("、"));
			window.parent.showMessageDlg(I18N.COMMON.tip, str);
		}else{
			//没有需要删除的,给出提示
			window.parent.showMessageDlg(I18N.COMMON.tip, "@VLAN.notExists@");
		}
	} else {
		var tmpS = "VLAN:";
		var deleteVlans = list.join("、");
		tmpS += list.join("、");
		if (notL == 0) {
			str = I18N.VLAN.deleteConfirm + tmpS + "?";
		} else {
			str = "VLAN:";
			str += notList.join("、");
			// I18N.VLAN.virConflictTip2 : 配置了VLAN虚接口,不能直接删除。<br>是否删除{0}?
			str += String.format(I18N.VLAN.virConflictTip2, tmpS);
		}
		if (listL + notL > 200) {
			str = "<div style='overflow-y:auto;width:420;height:320;'>" + str
					+ "</div>";
		}
		window.parent.showConfirmDlg(I18N.COMMON.tip, str, function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.deleting,
					'ext-mb-waiting');
			var re = {
				flag : false
			};
			delVlanMgmt(delList, re, deleteVlans);
		});
	}
}
function loadVlanListJson() {
	$.ajax({
		url : '/epon/vlan/loadOltVlanConfigList.tv',
		type : 'POST',
		async : false,
		data : "entityId=" + entityId + "&num=" + Math.random(),
		dataType : "json",
		success : function(vlanJson) {
			if (vlanJson != null) {
				vlanListJson = vlanJson;
			}
		},
		error : function(vlanJson) {
			window.parent.showMessageDlg(I18N.COMMON.message,
					I18N.VLAN.vlanLoadError);
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}
function loadVlanGlobalInfo() {
	$.ajax({
		url : '/epon/vlan/loadOltVlanGlobalInfo.tv',
		type : 'POST',
		async : false,
		data : "entityId=" + entityId + "&num=" + Math.random(),
		dataType : "json",
		success : function(oltVlanAttribute) {
			oltVlanGlobalInfo = oltVlanAttribute;
			if (oltVlanGlobalInfo != null) {
				$("#maxVlanId").val(oltVlanGlobalInfo.maxVlanId);
				$("#maxSupportVlans").val(oltVlanGlobalInfo.maxSupportVlans);
				$("#createdVlanNumber")
						.val(oltVlanGlobalInfo.createdVlanNumber);
			}
		},
		error : function(oltVlanAttribute) {
			window.parent.showMessageDlg(I18N.COMMON.message,
					I18N.VLAN.vlanPerLoadError);
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}
Ext.onReady(loadOltJson);
function loadOltJson() {
	// 获取面板数据
	$.ajax({
		url : "/epon/oltObjectCreate.tv?entityId=" + entityId,dataType:'json',
		success : function(json) {
			olt.init(json);
			initialdata(olt);
			$(".fanClass, .boardClass").unbind("click mouseover mouseout contextmenu");
			loadVlanListJson();// 获取VLAN列表数据
			loadVlanGlobalInfo();// 获取VLAN全局属性
			bulidVlanGrid();// 创建vlan列表和属性列表
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.message,I18N.VLAN.oltDataError);
		}
	});
}
function removeObj(list, obj) {
	document.getElementById(obj).style.border = "0px solid  #000000";
	var index = 0;
	if (isNaN(obj)) {
		index = list.indexOf(obj);
	} else {
		index = obj;
	}
	if (index >= 0 && index < list.length) {
		for ( var i = index; i < list.length - 1; i++) {
			list[i] = list[i + 1];
		}
		list.length -= 1;
		return true;
	} else {
		return false;
	}
}

function indexOf(list, obj) {
	for ( var i = 0; i < list.length; i++) {
		if (list[i] == obj) {
			return i;
		}
	}
	return -1;
}
function portIndexToText(a, b, tag, portIndex) {
	if (tag == 1) {
		var taggedPortString = vlanListJson.data[a].taggedPort;
		var taggedPortIndexList = vlanListJson.data[a].taggedPortIndexList;
		var i = 0;
		for (i = 0; i < taggedPortIndexList.length; i++) {
			if (taggedPortIndexList[i] == portIndex) {
				break;
			}
		}
		var realTaggedPortString = taggedPortString.split(":");
		var str = realTaggedPortString[1 + 4 * i].trimZero() + "/"
				+ realTaggedPortString[2 + 4 * i].trimZero();
	} else if (tag == 0) {
		var untaggedPortString = vlanListJson.data[a].untaggedPort;
		var untaggedPortIndexList = vlanListJson.data[a].untaggedPortIndexList;
		var i = 0;
		for (i = 0; i < untaggedPortIndexList.length; i++) {
			if (untaggedPortIndexList[i] == portIndex) {
				break;
			}
		}
		var realUntaggedPortString = untaggedPortString.split(":");
		var str = realUntaggedPortString[1 + 4 * i].trimZero() + "/"
				+ realUntaggedPortString[2 + 4 * i].trimZero();
	}
	return str;
}
function clearBorderStyle(id) {
	$temStyle = $("#" + id);
	if ($temStyle.css("borderColor") != divStyle.click) {
		$temStyle.animate({
			opacity : 0.7
		}, {
			duration : "fast"
		// complete : checkAnimateStyle
		});
		$temStyle.css({
			border : '0px solid ' + divStyle.hover
		});
	}
}

function bfsxVlan(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'ext-mb-waiting')
	$.ajax({
		url:'/epon/bfsxOltVlan.tv',cache:false,data:{
			entityId : entityId
		},success:function(){
			window.parent.closeWaitingDlg();
			loadVlanListJson();
			vlan_store.loadData(vlanListJson.data);
			vlan_grid.selModel.selectRow(0, false);
			rowClick(vlan_grid, 0);
			loadVlanGlobalInfo();
		},error:function(){
			window.parent.closeWaitingDlg();
		}
	})
}
