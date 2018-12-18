var CASCADE_LOCK, CONTEXTMENU_LOCK;
function rowClick(mirror_grid, rowIndex) {
	mirrorSelected = rowIndex;
	var $data = mirrorListJson.data[rowIndex];
	if (mirrorListJson.data.length != 0) {
		noSelected = false;
		clearPage();
		clearPage2();
		var inPortList = $data.sniMirrorGroupSrcInPortIndexList;
		var outPortList = $data.sniMirrorGroupSrcOutPortIndexList;
		var inportNum = 0;
		var outportNum = 0;
		var bothportNum = 0;
		
		$.each(inPortList,function(i,$portIndex){
			var inportId = IndexToId($portIndex);
			var $port = selectionModel.getSelectedItem(inportId);
			$("#" + inportId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}1.png)',$port.portSubType));
			divCache2.add(inportId);
			inportNum++;
		});
		$.each(outPortList,function(i,$portIndex){
			var outportId = IndexToId($portIndex);
			var $port = selectionModel.getSelectedItem(outportId);
			//判断端口类型,面板中显示端口状态
			if (divCache2.contains(outportId)) {
				$("#" + outportId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}3.png)',$port.portSubType));
				inportNum--;
				bothportNum++;
			} else {
				$("#" + outportId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}2.png)',$port.portSubType));
				divCache2.add(outportId);
				outportNum++;
			}
		});
		
		//目标端口
		var groupDstPort = $data.sniMirrorGroupDstPortIndexList;
		var destPortName = $data.destPortName;
		var	dstPortInGrid = "@MIRROR.notAssigned@";
		$.each(groupDstPort,function(i,$portIndex){
			var dstportId = IndexToId($portIndex);
			var $port = selectionModel.getSelectedItem(dstportId);
			$("#" + dstportId).css('backgroundImage',String.format('url(/epon/image/{0}/{0}4.png)',$port.portSubType));
			divCache2.add(dstportId);
			if(destPortName != null){
				dstPortInGrid = "sni" + " " + destPortName;
			}else{
				var array = dstportId.splt("_");
				dstPortInGrid = "sni" + " " + array[1] + "/" + array[2];
			}
		});
		mirror_attrStore = {
			'@MIRROR.mirrorId@' : $data.sniMirrorGroupIndex,
			'@MIRROR.mirrorName@' : $data.sniMirrorGroupName,
			'@MIRROR.targetPort@' : dstPortInGrid,
			'@MIRROR.inflowNum@' : inportNum,
			'@MIRROR.outflowNum@' : outportNum,
			'@MIRROR.ioflowNum@' : bothportNum
		}
	} else {
		noSelected = true;
		mirror_attrStore = {
			'@MIRROR.mirrorId@' : "",
			'@MIRROR.mirrorName@' : "",
			'@MIRROR.targetPort@' : "",
			'@MIRROR.inflowNum@' : "",
			'@MIRROR.outflowNum@' : "",
			'@MIRROR.ioflowNum@' : ""
		}
		clearPage()
		clearPage2()
	}
	mirror_attr_grid.setSource(mirror_attrStore);
}

function contextHandler(node,e) {
    if(!e){
        currentId = this.id;
        e = node;
    }else{
        currentId = node;
    }
    if(e.type == 'click'){
    	var portList = olt.slotList[currentId.split("_")[1]-1].portList;
    	
    	var tempPort = null;
    	for(var i=0; i<portList.length; i++) {
    		if(portList[i].portRealIndex == currentId.split("_")[2]) {
    			tempPort = portList[i];
    			break;
    		}
    	}
    	
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
	// 重置计时器
	timer = 0;
	var idArr = this.id.split("_");
	var nodeName = idArr[0];
	
	var portList = olt.slotList[idArr[1] - 1].portList;
	var tempPort = null;
	for(var i=0; i<portList.length; i++) {
		if(portList[i].portRealIndex == currentId.split("_")[2]) {
			tempPort = portList[i];
			break;
		}
	}
	
	
	var $isPonPortType = tempPort.sniMediaType == 3;
    //如果没有端口被选中
    if(indexCache.length == 0){
        var items = [];
        displayService(items,e);
        return;
    }
	
	if (!CASCADE_LOCK) {
	    CASCADE_LOCK = true;
		try {
			preventBubble(e);
			var clickedFlag = false;//是否已经选中了 
			//如果是PON保护组备用端口，则不显示菜单
			if (tempPort.isStandbyPort) {
				throw new Error(I18N.EPON.alternatePortDisabled);
			}
			
			if (noSelected == false) {
				var portIndex = tempPort.portIndex;
				var inportFlag = 0; //记录选中的inport端口个数
				var outportFlag = 0;//记录选中的outport端口个数
				var dstportFlag = 0;//记录选中的dstport端口个数
				var bothportFlag = 0;//记录选中的双向端口
				var dstportList = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList;
				var inportList = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList;
				var outportList = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList;
				for (i = 0; i < indexCache.length; i++) {
					for (j = 0; j < inportList.length; j++) {
						if (indexCache[i] == inportList[j]) {
							inportFlag = inportFlag + 1;
						}
					}
					for (k = 0; k < outportList.length; k++) {
						if (indexCache[i] == outportList[k]) {
							outportFlag = outportFlag + 1;
						}
					}
					for (l = 0; l < dstportList.length; l++) {
						if (indexCache[i] == dstportList[l]) {
							dstportFlag = dstportFlag + 1;
						}
					}
					for (m = 0; m < inportList.length; m++) {
						for (n = 0; n < outportList.length; n++) {
							if (indexCache[i] == inportList[m]
									&& indexCache[i] == outportList[n]) {
								bothportFlag = bothportFlag + 1;
								inportFlag = inportFlag - 1;
								outportFlag = outportFlag - 1;
							}
						}
					}
				}
				if (inportFlag == indexCache.length) {
					if (inportFlag == 1) {
						if (!$isPonPortType) {
							displayService(buildInportSingleSniMenuItems(),e);
						} else {
							displayService(buildInportMultiMenuItems(),e);
						}
					} else {
						displayService(buildInportMultiMenuItems(),e);
					}
				} else if (outportFlag == indexCache.length) {
					if (outportFlag == 1) {
						if (!$isPonPortType) {
							displayService(buildOutportSingleSniMenuItems(),e);
						} else {
							displayService(buildOutportMultiMenuItems(),e);
						}
					} else {
						displayService(buildOutportMultiMenuItems(),e);
					}
				} else if (inportFlag == 0 && outportFlag == 0 && dstportFlag == 0 && bothportFlag == 0) {
					if (indexCache.length == 1) {
						if (!$isPonPortType) {
							displayService(buildBlankportSingleSniMenuItems(),e);
						} else {
							displayService(buildBlankportMultiMenuItems(),e);
						}
					} else {
						displayService(buildBlankportMultiMenuItems(),e);
					}
				} else if (bothportFlag == indexCache.length) {
					if (indexCache.length == 1) {
						if (!$isPonPortType) {
							displayService(buildBothportSingleSniMenuItems(),e);
						} else {
							displayService(buildBothportMultiMenuItems(),e);
						}
					} else {
						displayService(buildBothportMultiMenuItems(),e);
					}
				} else {
	                var items = [];
	                displayService(items,e);
	            }
			}
		} catch (e) {
		}
		CASCADE_LOCK = false;
	}
	CONTEXTMENU_LOCK = false;
}

function loadMirrorListJson() {
	$.ajax({
		url : '/epon/mirror/loadMirrorConfigList.tv',
		type : 'POST',
		async : false,
		data : "entityId=" + entityId + "&num=" + Math.random(),
		dataType : "json",
		success : function(mirrorJson) {
			mirrorListJson = mirrorJson;
		},
		error : function(mirrorJson) {
			window.parent.showMessageDlg(I18N.COMMON.tip,
					I18N.MIRROR.loadMirrorError);
		},
		cache : false
	});
}
Ext.onReady(loadOltJson);
function loadOltJson() {
	//获取面板数据
	Ext.Ajax.request({
		url : "/epon/oltObjectCreate.tv?entityId=" + entityId,
		method : "post",
		success : function(response) {
			json = Ext.util.JSON.decode(response.responseText);
			olt.init(json);
			initialdata(olt);
			$(".fanClass, .boardClass").unbind(
					"click mouseover mouseout contextmenu");
			loadMirrorListJson();
			bulidMirrorGrid();//创建mirror列表和属性列表
		},
		failure : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip,
					I18N.EPON.loadOltError);
		}
	});
}
function bulidMirrorGrid() {
	//vlan列表
	mirror_cm = new Ext.grid.ColumnModel([ {
		header : "@MIRROR.mirrorId@",
		dataIndex : 'sniMirrorGroupIndex',
		width : 60
	}, {
		header : "@MIRROR.mirrorName@",
		dataIndex : 'sniMirrorGroupName',
		width : 100
	} ]);
	mirror_store = new Ext.data.JsonStore({
		data : mirrorListJson.data,
		fields : [ "sniMirrorGroupIndex","sniMirrorGroupName"]
	});
	mirror_grid = new Ext.grid.GridPanel({
		stripeRows : true,
		region : "center",
		bodyCssClass : 'normalTable',
		viewConfig : {
			forceFit : true
		},
		height : $("#viewLeftPartBody").height(),
		autoExpandColumn : true,
		id : "mirrorGrid",
		store : mirror_store,
		cm : mirror_cm,
		renderTo : "viewLeftPartBody",
		listeners : {
			viewready : function() {
				mirror_grid.selModel.selectRow(0, true);
				rowClick(mirror_grid, 0);
			}
		}
	});

	mirror_grid.on('rowcontextmenu', function(mirror_grid, rowIndex, e) {
		e.preventDefault();
		var sm = mirror_grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		modifyMirrorNameMenu.showAt(e.getPoint());
	});

	mirror_grid.getSelectionModel().on('rowselect',function(grid, rowIndex, e) {
		selectedIndex = rowIndex;
	});

	if (Ext.grid.PropertyColumnModel) {
		Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
			nameText : "property",
			valueText : "value"
		});
	}
	mirror_attr_grid = new Ext.grid.PropertyGrid({
		renderTo : 'viewRightPartBottomBody',
		autoHeight : true,
		border : false,
		autoScroll : true,
		hideHeaders : true,
		source : mirror_attrStore,
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
							tip.body.dom.innerHTML = store.getAt(rowIndex).get(
									'value');
						}
					}
				});
			}
		}
	});
	//表格不可编辑
	mirror_attr_grid.on("beforeedit", function(e) {
		e.cancel = true;
		return false;
	});
	//表格单击事件
	mirror_grid.on('rowclick', rowClick);
}

function containsObj(array, obj) {
	return array.indexOf(obj) != -1;
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

function updateOltMirrorListJson() {
	loadMirrorListJson();
	mirror_store.loadData(mirrorListJson.data);
	mirror_grid.selModel.selectRow(mirrorSelected, true);
	rowClick(mirror_grid, mirrorSelected);
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
		if (cid != divCacheObject) {//如果点击的是当前div，则不做修改	    
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
					&& arr[0] != 'fan') {//是板卡
				$temStyle.css({
					opacity : 0.7,
					border : '2px solid #71787e'
				});
			} else {//是风扇或者电源
				$temStyle.css({
					opacity : 0.7,
					border : '2px solid transparent'
				});
			}
			if ($temStyle.css("opacity") == 1
					&& $temStyle.css("height") == ""
							+ coordinateParam.slotHeight + "px") {
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

function bfsxOltMirror(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'ext-mb-waiting')
	$.ajax({
		url:'/epon/bfsxOltMirror.tv',cache:false,data:{
			entityId : entityId
		},success:function(){
			window.parent.closeWaitingDlg();
			window.location.reload();
		},error:function(){
			window.parent.closeWaitingDlg();
		}
	})
}