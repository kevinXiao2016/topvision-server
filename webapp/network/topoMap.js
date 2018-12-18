/**
 * Topology Map.
 *
 * author:niejun
 */
var scrollX = 0;
var scrollY = 0;
var scrollFlag = false;

// micro graph
var scalexMin = 5;
var scaleyMin = 5;
var scalexMax = 5;
var scaleyMax = 5;
var scaleyOffset = 5;
var microSquare = null;
var microGraph = null;
var microArrow = null;
var microContainer = null;
var microVisible = false;

var editTopoPower = true;
var remoteMode = false;
var dispatcherTimer = null;

var cluetipShowDelaying = 1000;
var cluetipHideDelaying = 0;
var cluetipCloseTimeout = 15000;
// click or hover
var cluetipShowType = 'hover';
// can be 'show' or 'slideDown' or 'fadeIn'
var cluetipEffectType = 'show';
var displayCluetip = true;

var zgraph = null;
var mapMenu = null;
var entityMenu = null;
var linkMenu = null;
var shapeMenu = null;
var vmlNodeMenu = null;
var vmlTextMenu = null;
var vmlPicMenu = null;
var vmlLineMenu = null;
var folderMenu = null;

var pasteMenu;
var logicPaneItem;
var physicalPaneItem;
var portFlowItem;
var interfacesItem;
var cpuItem;
var openChildFolder;
var openInNewWindItem;
var openHrefItem;
var folderStatItem;
var setTextMenuItem;
var resizeMenuItem;
var setStrokeMenuItem;
var backSuperiorItem;
var addLinkMenuItem;

var microGraphBt = null;
var toolbts = null;
var templateBt = null;
var entityTemplateTree = null;
var templateDlg = null;
var drawingBt = null;
var drawingDlg = null;
var jbGraph = null;

var displayLink = true;
var displayEntity = true;

var displayAlertIcon = true;
var displayNoSnmpEntity = true;
var displayRouter = true;
var displaySwitch = true;
var displayL3switch = true;
var displayServer = true;
var displayDesktop = true;
var displayOthers = true;
var entityForOrgin = 0;
var entityCount = 0;

var zooming = false;
var zoomByWheel = false;
var zoomByAnimate = false;
var zoomIndex = 0;

function getCluetipEnabled() {
	return displayCluetip;
}

function setCluetipEnabled(flag) {
	displayCluetip = flag;
}
function doMouseWheel() {
	if (zoomByWheel && !zoom.zooming) {
		var z = 1;
		z = zoom.size + event.wheelDelta/1200.0;
		if (z < 0.1) {
			z = 0.1
		}
		if (z > 5) {
			z = 5;
		}
		z =  Math.round(z * 100)/100.0;
		zoomMap(z);
	}
	return false;
}

/**
 * SCROLLED HANDLER
 * @param obj
 */
function topoMapScrolled(obj) {
	if (scrollFlag && microVisible) {
		var l = obj.scrollWidth - obj.clientWidth - 18;
		var x = scalexMin, y = scaleyMin;
		if (l > 0) {
			x = parseInt(scalexMax * obj.scrollLeft / l);
			if (x < scalexMin) {
				x = scalexMin;
			}
			if (x > scalexMax) {
				x = scalexMax;
			}
		}
		l = obj.scrollHeight - obj.clientHeight - 18;
		if (l > 0) {
			y = parseInt(scaleyMax * obj.scrollTop / l);
			if (y > scaleyMax) {
				y = scaleyMax;
			}
			if (y < scaleyMin) {
				y = scaleyMin;
			}
		}
		microSquare.style.left = x;
		microSquare.style.top = y;
	}
	scrollFlag = true;
}
function findFromMicroGraph(obj, evt) {
	var x = parseInt((evt.x - 5) * getMapWidth() / obj.clientWidth);
	var y = parseInt((evt.y - 31) * getMapHeight() / obj.clientHeight);
	//var src = document.elementFromPoint(x, y + 26);
}

/***
 * micro graph click handler
 */
function onShowMicroGraphClick() {
	showMicroGraph();
	microGraphBt.setChecked(microVisible);
}
/**
 * show micro graph
 */
function showMicroGraph() {
	microVisible = !microVisible;
	var containerOffsetTop = jbGraph.getHeight();
	if (remoteMode) {
		containerOffsetTop = 0;
	}
	if (microVisible) {
		var w = 276;
		var h = 178;

		microGraph.style.width = w;
		microGraph.style.height = h;
		microGraph.style.backgroundImage = 'url(../tempfile/map/' + topoFolderId + '.png)';

		microContainer.style.left = 0;
		microContainer.style.top = containerOffsetTop;
		microContainer.style.display = 'block';

		microArrow.className = 'microGraphBt1';
		microArrow.style.left = microContainer.clientWidth - 16;
		microArrow.style.top = containerOffsetTop + microContainer.clientHeight - 16;
		microArrow.title = I18N.NETWORK.hideMicroGraph;
	} else {
		microContainer.style.display = 'none';
		microGraph.style.backgroundImage = 'url(../images/s.gif)';
		microArrow.className = 'microGraphBt';
		microArrow.style.left = 0;
		microArrow.style.top = containerOffsetTop;
		microArrow.title = I18N.NETWORK.showMicroGraph;

	}
}

function microSquareMouseDown(source, evt) {
	microSquare.style.cursor = 'url(../css/images/closedhand.cur)';
}
function microSquareMouseUp(source, evt) {
	microSquare.style.cursor = 'url(../css/images/openhand.cur)';
}
function initMicroGraph() {
	microContainer = Zeta$('microContainer-div');
	microGraph = Zeta$('microGraph-div');
	microSquare = Zeta$("microSquare-div");
	microArrow = Zeta$('microArrow-div');

	switch (zoom.size) {
		case 0.1:
			zoomIndex = 1;
			zoom.scaleIndex = 0;
			break;
		case 0.25:
			zoomIndex = 2;
			zoom.scaleIndex = 1;
			break;
		case 0.5:
			zoomIndex = 3;
			zoom.scaleIndex = 2;
			break;
		case 0.75:
			zoomIndex = 4;
			zoom.scaleIndex = 3;
			break;
		case 1:
			zoomIndex = 5;
			zoom.scaleIndex = 4;
			break;
		case 1.25:
			zoomIndex = 6;
			zoom.scaleIndex = 5;
			break;
		case 1.5:
			zoomIndex = 7;
			zoom.scaleIndex = 6;
			break;
		case 2:
			zoomIndex = 8;
			zoom.scaleIndex = 7;
			break;
		case 3:
			zoomIndex = 9;
			zoom.scaleIndex = 8;
			break;
		case 4:
			zoomIndex = 10;
			zoom.scaleIndex = 9;
		case 5:
			zoomIndex = 11;
			zoom.scaleIndex = 10;
			break;
	}
	var el = Zeta$('zoomImg'+zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-on.gif';
	}

	$('#microSquare-div').draggable({opacity: 0.50, helper: 'clone', containment: 'parent',
		start: function(e, ui) {
		},
		stop: function(e, ui) {
			scrollFlag = false;
			this.style.left = ui.position.left;
			this.style.top = ui.position.top;
			if (ui.position.left <= scalexMin) {
				zgraph.scrollLeft = 0;
			} else {
				zgraph.scrollLeft = parseInt(getMapWidth() * ui.position.left / microGraph.clientWidth);
			}
			if (ui.position.top <= scaleyMin) {
				zgraph.scrollTop = 0;
			} else {
				zgraph.scrollTop = parseInt(getMapHeight() * ui.position.top / microGraph.clientHeight);
			}
		}
	});
	if (isSubnetMap) {
		microArrow.style.display = 'none';
	} else {
		microArrow.style.display = '';
		microArrow.style.top = jbGraph.getHeight();
	}
}
function initialize() {
	zgraph = initGraph("zetaGraph");
	setDrawingCallback(requestMapNode);
	setDeleteCallback(onDeleteClick);
	setShapeCallback(vmlFocusedCallback, vmlBluredCallback);
	setShapeMovedCallback(nodeCoordChanged);

	if (remoteMode) {
		$('#zetaGraph').bind('mouseup', function(evt) {
			if (evt.target.id == 'zetaGraph') {
				disselectShapeNode();
			}
		});
		return;
	}

	buildToolbar();
//	initMicroGraph();
	if (document.body.clientHeight > 26) {
		zgraph.style.height = (document.body.clientHeight - jbGraph.getHeight()) + 'px';
	}
	// setup graph event lisnter
	$('#zetaGraph').bind('contextmenu', function(evt) {
		evt.preventDefault();
		if (curDrawingType == CURSOR_TYPE.POINTER || curDrawingType == CURSOR_TYPE.SQUARE) {
			var src = evt.target;
			if (src.id == 'zetaGraph' || src.id == 'bus' || src.id == 'busStart' || src.id == 'busEnd') {
				showContextMenu(evt);
			} else {
				var count = 0;
				while (!Zeta$defined(src.objType) || count > 2) {
					src = src.parentNode;
					count++;
				}
				var objType = src.objType;
				if (objType == USEROBJ_TYPE.ENTITY) {
					showEntityMenu(evt);
				} else if (objType == USEROBJ_TYPE.LINK) {
					showLinkMenu(evt);
				} else if (objType == USEROBJ_TYPE.SHAPE) {
					showNodeMenu(src, evt);
				} else if (objType == USEROBJ_TYPE.FOLDER) {
					showFolderMenu(evt);
				}
			}
		}
	});

	$('#zetaGraph').bind('mousedown', function(evt) {
		renameNodeFinished();
		if (curDrawingType == CURSOR_TYPE.MOVE) {
			scrollFlag = true;
			scrollX = event.x;
			scrollY = event.y;
			zgraph.style.cursor = 'url(../css/images/closedhand.cur)';
		} else if (curDrawingType == CURSOR_TYPE.POINTER || curDrawingType == CURSOR_TYPE.SQUARE) {
			if (linkAdding) {
				drawLinkCompleted();
			}
		} else if (EventUtils.isLeftButton(event)) {
			if (curDrawingType >= SHAPE_TYPE.SOLID_LINE && curDrawingType <= SHAPE_TYPE.TEXTRECT) {
				if (evt.pageX < zgraph.clientWidth && evt.pageY < (zgraph.clientHeight + 25)) {
					drawingShape(curDrawingType);
				}
			}
		}
	});

	$('#zetaGraph').bind('mouseup', function(evt) {
		if (linkAdding) {
			linkAdding = false;
		}
		if (curDrawingType == CURSOR_TYPE.MOVE) {
			scrollFlag = false;
			zgraph.style.cursor = 'url(../css/images/openhand.cur)';
		} if (curDrawingType == CURSOR_TYPE.POINTER || curDrawingType == CURSOR_TYPE.SQUARE) {
			if (evt.target.id == 'zgraph') {
				disselectShapeNode();
			}
		} else if (EventUtils.isLeftButton(event)) {
			if (curDrawingType == SHAPE_TYPE.CLOUDY) {
				insertCloudy();
			} else if (curDrawingType == SHAPE_TYPE.SUBNET) {
				insertSubnet();
			} else if (curDrawingType == SHAPE_TYPE.HREF) {
				insertHrefFolder();
			} else if (curDrawingType == SHAPE_TYPE.TEXT) {
				insertText();
			}
		}
	});

	$('#zetaGraph').bind('mousemove', function(evt) {
		if (curDrawingType == CURSOR_TYPE.MOVE) {
			if (scrollFlag) {
				var l = zgraph.scrollLeft + event.x - scrollX;
				if (l < 0) {
					l = 0;
				}
				zgraph.scrollLeft = l;
				l = zgraph.scrollTop + event.y - scrollY;
				if (l < 0) {
					l = 0;
				}
				zgraph.scrollTop = l;
				scrollX = event.x;
				scrollY = event.y;
			}
		} else if (curDrawingType >= SHAPE_TYPE.SOLID_LINE) {
			window.top.setCoordInfo((this.scrollLeft + event.clientX) + ' : '
				+ (this.scrollTop + event.clientY));
		}
	});
}

function buildToolbar() {
	jbGraph = new Ext.Toolbar();
	jbGraph.render('toolbar');
//	microGraphBt = new Ext.menu.CheckItem({text: I18N.NETWORK.microGraph, checked: false, handler: showMicroGraph});
	if (editTopoPower) {
		jbGraph.add({text: I18N.COMMON.create, iconCls: 'bmenu_new', handler: onNewEntityClick});
	}
	jbGraph.add(new Ext.Toolbar.SplitButton(
		{text: I18N.COMMON.print, handler: onPrintClick, iconCls: 'bmenu_print', menu:{items:[
		{text: '<b>'+I18N.COMMON.print+'</b>', handler: onPrintClick},
		{text: I18N.COMMON.printPreview, handler: onPrintPreviewClick}]}}), '-'
	);
	if (editTopoPower) {
		if (!isSubnetMap) {
			jbGraph.add(
				new Ext.Toolbar.SplitButton({text: I18N.COMMON.save, handler: onSaveClick, iconCls: 'bmenu_save',
					menu: {items:[
					{text: '<b>' + I18N.NETWORK.saveCoord + '</b>', handler: onSaveClick},
					{text:I18N.NETWORK.saveAsPicture, handler: onSaveAsPictureClick}]}})
			);
		}
		jbGraph.add({text:I18N.COMMON.remove, iconCls:'bmenu_delete', handler:onDeleteClick}, '-');
	}
	var fitems = [
		{text: '<b>'+I18N.COMMON.fullFind +'</b>', handler: onFindClick},
		{text:I18N.COMMON.similarFind, handler: onFind1Click}
	];
	if (!isSubnetMap) {
		fitems[fitems.length] = '-';
		fitems[fitems.length] = {text: I18N.GRAPH.findCascadeOrigin, handler: onFindCascadeClick};
	}
	jbGraph.add(
		new Ext.Toolbar.SplitButton(
			{iconCls: 'bmenu_find', text: I18N.COMMON.find, handler: onFindClick, menu: {items: fitems}}
		)
	);
	if (!isSubnetMap) {
		if (editTopoPower) {
			templateBt = new Ext.menu.CheckItem({text:I18N.COMMON.template, checked: false, handler: onTemplateToggleClick});
			drawingBt = new Ext.menu.CheckItem({text: '<b>'+I18N.NETWORK.drawTools+'</b>', checked: false, handler: onDrawingToggleClick});
			jbGraph.add(
				new Ext.Toolbar.SplitButton(
				{text: I18N.COMMON.tool, handler: onDrawingToggleClick,
					iconCls:'bmenu_tool', menu:{items:[
					drawingBt,
					templateBt, '-',
//                    microGraphBt,
//					{text: I18N.NETWORK.synMicroGraph, handler: onSynMicroGraphClick}, '-',
					{text:I18N.COMMON.select, menu:[
					{text:I18N.NETWORK.selectAll, iconCls:'bmenu_selectall', handler:onSelectAllClick}, '-',
					{text:I18N.NETWORK.selectAllEntity,handler:onSelectAllEntityClick},
					{text:I18N.NETWORK.selectAllLink,handler:onSelectAllLinkClick}, '-',
					{text:I18N.NETWORK.selectSnmpEntity,handler:onSelectSnmpEntityClick},
					{text:I18N.NETWORK.selectNotSnmpEntity,handler:onSelectNotSnmpEntityClick}, '-',
					{text: I18N.NETWORK.selectHasLinkEntity, handler: onSelectHasLinkClick},
					{text: I18N.NETWORK.selectNoLinkEntity, handler: onSelectNoLinkClick}]},
				{text: I18N.COMMON.display, menu:[
					{text:I18N.NETWORK.selectNotSnmpEntity, handler: onDisplayNoSnmpEntityClick, checked: displayNoSnmpEntity}]},
				{iconCls: 'bmenu_find', text:I18N.GRAPH.zoom, menu:[
					{text:'25%', group:'zoom1', handler:onZoomFitClick2},
			    	{text:'50%', group:'zoom1', handler:onZoomFitClick3},
			    	{text:'75%', group:'zoom1', handler:onZoomFitClick4},
			    	{text:'100%', group:'zoom1', handler:onZoomFitClick5},
			    	{text:'125%', group:'zoom1', handler:onZoomFitClick6},
			    	{text:'150%', group:'zoom1', handler:onZoomFitClick7},
			    	{text:'200%', group:'zoom1', handler:onZoomFitClick8},
			    	{text:'300%', group:'zoom1', handler:onZoomFitClick9}, '-',
			    	{text:I18N.NETWORK.customLevel, handler : onZoomCustomClick}]}, '-',
					{text:I18N.NETWORK.labelAndLegend, handler: onLabelAndLegendClick}
					]}})
			);
		}
		jbGraph.add(new Ext.Toolbar.SplitButton(
			{text: I18N.COMMON.arrange, iconCls: 'bmenu_arrange', handler: onDefaultArrangeClick,
				menu: {items: [
				{text:'<b>'+I18N.NETWORK.defaultArrange+'</b>', handler: onDefaultArrangeClick},
				{text:I18N.NETWORK.circular, handler: onCircularArrangeClick}
				]}
			})
		);
	}
	jbGraph.add(
			'-', {text:I18N.COMMON.view, iconCls:'bmenu_view',
				menu: {items:[
				{text: '<b>'+I18N.NETWORK.topoMap+'</b>', group:'view1', checked:true, checkHandler:onViewByTopoMapClick},
				{text: I18N.NETWORK.icon, group:'view1', checked:false, checkHandler:onViewByIconClick},
				{text: I18N.NETWORK.detail, group:'view1', checked:false, checkHandler: onViewByDetailClick}, '-',
				{text: I18N.COMMON.openInNewWindow, handler: openFolderInNewWindow}]}
			}, '-',
			{text:I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler: onRefreshClick}
		);
	jbGraph.doLayout();
}

function showContextMenu(evt) {
	if (mapMenu == null) {
		pasteMenu = new Ext.menu.Item({text: I18N.COMMON.paste, handler: onPasteClick, disabled: true});
		backSuperiorItem = new Ext.menu.Item({text: I18N.NETWORK.returnSuperiorFolder, handler: onReturnSuperiorClick, disabled: true});
		var items = [];
		items[items.length] = backSuperiorItem;
		items[items.length] = '-';
		if (!isSubnetMap) {
			items[items.length] = {text:I18N.COMMON.view, id:'view-menu', disabled: isSubnetMap, menu:[
	        	{text:I18N.NETWORK.topoMap, checked:true, group:'view', handler:onViewByTopoMapClick},
				{text:I18N.NETWORK.icon, checked:false, group:'view', handler:onViewByIconClick},
	        	{text:I18N.NETWORK.detail, checked:false, group:'view', handler:onViewByDetailClick}, '-',
	        	{text: I18N.COMMON.openInNewWindow, handler: openFolderInNewWindow}
	        ]};
			items[items.length] = {text: I18N.GRAPH.layout, disabled: isSubnetMap, menu: [
				{text:I18N.NETWORK.defaultArrange, handler: onDefaultArrangeClick},
				{text:I18N.NETWORK.circular, handler: onCircularArrangeClick}
			]};
		}
		items[items.length] = {text: I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler:onRefreshClick};
		if (editTopoPower) {
			items[items.length] = '-';
			if(!isSubnetMap) {
				items[items.length] = {text: I18N.COMMON.select, disabled: isSubnetMap, menu:[
					{text:I18N.COMMON.selectAll, iconCls:'bmenu_selectall', handler: onSelectAllClick}, '-',
					{text:I18N.NETWORK.selectAllEntity, handler: onSelectAllEntityClick},
					{text:I18N.NETWORK.selectAllLink, handler: onSelectAllLinkClick}, '-',
					{text:I18N.NETWORK.selectSnmpEntity,handler: onSelectSnmpEntityClick},
					{text:I18N.NETWORK.selectNotSnmpEntity,handler: onSelectNotSnmpEntityClick}, '-',
					{text: I18N.NETWORK.selectHasLinkEntity, handler: onSelectHasLinkClick},
					{text: I18N.NETWORK.selectNoLinkEntity, handler: onSelectNoLinkClick}]};
				items[items.length] = {text:I18N.COMMON.display, disabled: isSubnetMap, menu:[
					{text:I18N.NETWORK.selectNotSnmpEntity, handler: onDisplayNoSnmpEntityClick, checked: displayNoSnmpEntity}]};
				items[items.length] = {id:'zoomMenu', iconCls: 'bmenu_find', text:I18N.GRAPH.zoom, disabled: isSubnetMap, menu:[
					{text:'25%', group:'zoom', handler: onZoomFitClick2},
			    	{text:'50%', group:'zoom', handler: onZoomFitClick3},
			    	{text:'75%', group:'zoom', handler:onZoomFitClick4},
			    	{text:'100%', group:'zoom', handler:onZoomFitClick5},
			    	{text:'125%', group:'zoom', handler:onZoomFitClick6},
			    	{text:'150%', group:'zoom', handler:onZoomFitClick7},
			    	{text:'200%', group:'zoom', handler:onZoomFitClick8},
			    	{text:'300%', group:'zoom', handler:onZoomFitClick9}, '-',
			    	{text:I18N.NETWORK.customLevel, handler : onZoomCustomClick}]};
			    items[items.length] = '-';
				if (editTopoPower) {
					items[items.length] = pasteMenu;
				}
				items[items.length] = {text:I18N.NETWORK.saveAsPicture, handler: onSaveAsPictureClick};
		    } else {
		    	if (editTopoPower) {
					items[items.length] = pasteMenu;
				}
		    }
		}
		items[items.length] = '-';
		items[items.length] = {text:I18N.NETWORK.labelAndLegend, handler: onLabelAndLegendClick};
		items[items.length] = {text: I18N.NETWORK.property, handler: onPropertyClick};
		mapMenu = new Ext.menu.Menu({id: 'map-menu', enableScrolling: enableMenuScrolling,
			minWidth: 180, ignoreParentClicks: true, items: items});
	}
	var clipboard = getZetaClipboard();
	if (clipboard != null && clipboard.src != topoFolderId && clipboard.target == 'topoFolder') {
		pasteMenu.enable();
	} else {
		pasteMenu.disable();
	}
	if (superiorFolderId > 1) {
		backSuperiorItem.enable();
	} else {
		backSuperiorItem.disable();
	}
	mapMenu.showAt([event.x, event.y]);
}
function showEntityMenu() {
	if (entityMenu == null) {
	    logicPaneItem = new Ext.menu.Item({text:I18N.NETWORK.logicPane, handler: onLogicPaneClick});
		physicalPaneItem = new Ext.menu.Item({text:I18N.NETWORK.physicalPane, handler: onPhysicalPaneClick});
		portFlowItem = new Ext.menu.Item({text:I18N.NETWORK.portRealFlow, handler: onPortRealStateClick, disabled:true});
		interfacesItem = new Ext.menu.Item({text: I18N.NETWORK.mibInfo, handler: onMibBrowseClick, disabled: true});
		cpuItem = new Ext.menu.Item({text: I18N.NETWORK.cpuMemInfo, handler: onCpuMemClick, disabled: true});

	    var entityViewMenu = [
			{text:I18N.NETWORK.entitySnap, handler: onEntitySnapClick},
			{text:I18N.COMMON.configDetail, handler: onViewConfigClick},
			{text:I18N.NETWORK.entityAlarm, handler: onViewAlarmClick}, '-',
			interfacesItem, '-',
			//logicPaneItem, physicalPaneItem, '-',
			cpuItem, portFlowItem
		];
		var items = [];
		if (editTopoPower && !isSubnetMap) {
			items[items.length] = {text: I18N.COMMON.createLink, handler : onAddLinkClick};
			items[items.length] = '-';
		}
		items[items.length] = {text: I18N.COMMON.openURL, handler: onOpenEntityURL};
		items[items.length] = {text: I18N.COMMON.openURLInNewWindow, handler: onOpenEntityURLInNewWindow};
		items[items.length] = '-';
		items[items.length] = {text: I18N.COMMON.view, menu : entityViewMenu};
		items[items.length] = {text: I18N.NETWORK.tool, menu:[
        	{text: I18N.NETWORK.ping, handler: onPingClick},
        	{text: I18N.NETWORK.tracert, handler: onTracertClick},
        	//{text: I18N.NETWORK.telnet, handler: onTelnetClick},
        	{text: I18N.NETWORK.nativeTelnet, handler: onNativeTelnetClick}
        ]};
		items[items.length] = {text: I18N.NETWORK.discoveryAgain, iconCls:'bmenu_refresh', handler: onDiscoveryAgainClick};
		if (editTopoPower) {
			items[items.length] = '-';
			var editMenu = [
				{text: I18N.COMMON.copy, handler: onCopyClick},
				{text: I18N.COMMON.remove, iconCls:'bmenu_delete', handler: onDeleteClick}, '-',
	        	{text:I18N.COMMON.moveTo, handler:onMoveToClick}];
	        if (googleSupported) {
	        	editMenu[editMenu.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick};
	        }
	        editMenu[editMenu.length] = '-';
	        editMenu[editMenu.length] = {text:I18N.GRAPH.fixLocation, handler: fixEntityClick, disabled: isSubnetMap};
			items[items.length] = {text:I18N.COMMON.edit, menu: editMenu};
		}
		if (!isSubnetMap) {
			items[items.length] = {text:I18N.GRAPH.align, menu:[
				{text:I18N.GRAPH.horizontalAlign, handler:onHorizontalAlignClick},
				{text:I18N.GRAPH.verticalAlign, handler:onVerticalAlignClick},
	        	{text:I18N.GRAPH.leftAlign, handler:onLeftAlignClick},
	        	{text:I18N.GRAPH.rightAlign, handler:onRightAlignClick}]};
	        items[items.length] = {text:I18N.COMMON.select, menu:[
				{text:I18N.GRAPH.selectAdjacent, handler: onSelectAdjacentClick},
				{text:I18N.GRAPH.selectSameType, handler: onSelectSameTypeClick}]};
			items[items.length] = {text: I18N.COMMON.display, menu:[
				{text: I18N.GRAPH.depth1, handler: onDisplayDepth1Click},
				{text: I18N.GRAPH.depth2, handler: onDisplayDepth2Click},
				{text: I18N.GRAPH.depth3, handler: onDisplayDepth3Click}, '-',
				{text: I18N.GRAPH.cancelCascadeDisplay, handler: onCancelCascadeClick}
			]};
		}
		items[items.length] = '-';
		items[items.length] = {text: I18N.NETWORK.property, handler:onEntityPropertyClick};
		entityMenu = new Ext.menu.Menu({ id: 'entity-menu', minWidth: 180,
			enableScrolling: enableMenuScrolling, ignoreParentClicks: true, items: items});
	}
	var entity = nodeElMap.get(linkSrcEntityId);
	if (entity.snmpSupport) {
		physicalPaneItem.enable();
		logicPaneItem.enable();
		portFlowItem.enable();
		interfacesItem.enable();
		cpuItem.enable();
	} else {
		portFlowItem.disable();
		interfacesItem.disable();
		physicalPaneItem.disable();
		logicPaneItem.disable();
		cpuItem.disable();
	}
	entityMenu.showAt([event.x, event.y]);
}
function showNodeMenu(src, evt) {
	// display menu in vml map node
	if (shapeMenu == null) {
		var items = [];
		if (editTopoPower) {
			addLinkMenuItem = new Ext.menu.Item({text: I18N.GRAPH.addLink, handler: onAddLinkClick});
			setTextMenuItem = new Ext.menu.Item({text: I18N.GRAPH.setText, handler : onTextVmlClick});
			resizeMenuItem = new Ext.menu.Item({text:I18N.GRAPH.resize, handler : onResizeVmlClick});
			setStrokeMenuItem = new Ext.menu.Item({text: I18N.GRAPH.setStrokeWeight, handler: onSetStrokeWeightClick});
			items[items.length] = addLinkMenuItem;
			items[items.length] = '-';
			items[items.length] = {text: I18N.COMMON.openURL, handler: onOpenURLClick};
			items[items.length] = {text: I18N.COMMON.openURLInNewWindow, handler: onOpenURLInNewWindowClick};
			items[items.length] = '-';
			items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler : onDeleteVmlClick};
			items[items.length] = '-';
			items[items.length] = setTextMenuItem;
			items[items.length] = setStrokeMenuItem;
			items[items.length] = resizeMenuItem;
			items[items.length] = '-';
		}
		items[items.length] = {text: I18N.COMMON.property, handler : onVmlPropertyClick};
		shapeMenu = new Ext.menu.Menu({id: 'shape-menu', ignoreParentClicks: true, items: items});
	}
	var entity = src;
	while (entity.figureType == null) {
		entity = src.parentNode;
	}
	if (entity != null) {
		if (entity.figureType == SHAPE_TYPE.TEXT || entity.figureType == SHAPE_TYPE.TEXTRECT) {
			setTextMenuItem.enable();
			if (entity.figureType == SHAPE_TYPE.TEXTRECT) {
				setStrokeMenuItem.enable();
				resizeMenuItem.enable();
			} else {
				setStrokeMenuItem.disable();
				resizeMenuItem.disable();
			}
			addLinkMenuItem.disable();
		} else {
			setTextMenuItem.disable();
			if (entity.figureType == SHAPE_TYPE.PICTURE) {
				setStrokeMenuItem.disable();
				resizeMenuItem.disable();
				addLinkMenuItem.enable();
			} else {
				setStrokeMenuItem.enable();
				resizeMenuItem.enable();
				addLinkMenuItem.disable();
			}
		}
	}
	shapeMenu.showAt([event.x, event.y]);
}

function showFolderMenu(evt) {
	if (folderMenu == null) {
		var items = [];
		openChildFolder = new Ext.menu.Item({text: I18N.NETWORK.openChildGraph, handler: onOpenFolderClick});
		openHrefItem = new Ext.menu.Item({text: I18N.NETWORK.openRemoteFolder, handler: onOpenRemoteFolderClick});
		folderStatItem = new Ext.menu.Item({text: I18N.NETWORK.networkStatInfo, handler: onStatFolderClick});
		if (editTopoPower) {
			items[items.length] = {text: I18N.NETWORK.addLink, handler: onAddLinkClick};
			items[items.length] = '-';
		}
		items[items.length] = openChildFolder;
		items[items.length] = openHrefItem;
		items[items.length] = {text: I18N.COMMON.openURLInNewWindow, handler: onOpenHrefInNewWindowClick};
		//items[items.length] = '-';
		//items[items.length] = folderStatItem;
		items[items.length] = '-';
		if (editTopoPower) {
			items[items.length] = {text:I18N.COMMON.edit, menu:[
				{text: I18N.COMMON.remove, iconCls:'bmenu_delete', handler: onDeleteFolderClick},
				{text: I18N.COMMON.rename, handler: onRenameFolderClick}, '-',
	        	{text:I18N.GRAPH.fixLocation, handler: fixFolderClick}]};
		}
		items[items.length] = {text:I18N.GRAPH.align, menu:[
			{text:I18N.GRAPH.horizontalAlign, handler: onHorizontalAlignClick},
			{text:I18N.GRAPH.verticalAlign, handler: onVerticalAlignClick},
        	{text:I18N.GRAPH.leftAlign, handler: onLeftAlignClick},
        	{text:I18N.GRAPH.rightAlign, handler: onRightAlignClick}]};
        items[items.length] = {text:I18N.COMMON.select, disabled: isSubnetMap, menu:[
			{text:I18N.GRAPH.selectAdjacent, handler:onHorizontalAlignClick},
			{text:I18N.GRAPH.selectSameType, handler: onVerticalAlignClick}]};
		items[items.length] = {text: I18N.COMMON.display, disabled: isSubnetMap, menu:[
			{text: I18N.GRAPH.depth1, handler: onHorizontalAlignClick},
			{text: I18N.GRAPH.depth2, handler: onVerticalAlignClick},
			{text: I18N.GRAPH.depth3, handler: onVerticalAlignClick}, '-',
			{text: I18N.GRAPH.cancelCascadeDisplay, handler: onCancelCascadeClick}
		]};

		items[items.length] = '-';
		items[items.length] = {text:I18N.NETWORK.property, handler: onFolderPropertyClick};
		folderMenu = new Ext.menu.Menu({id: 'folder-menu', ignoreParentClicks: true, items: items});
	}
	var entity = nodeElMap.get(linkSrcEntityId);
	if (entity != null && entity.folderType == 7) {
		openChildFolder.disable();
		folderStatItem.disable();
		openHrefItem.enable();
	} else {
		openChildFolder.enable();
		folderStatItem.enable();
		openHrefItem.disable();
	}
	folderMenu.showAt([event.x, event.y]);
}

function showLinkMenu(evt) {
	if (linkMenu == null) {
		var items = [];
		//items[items.length] = {text:I18N.NETWORK.linkFlowAanlyse, handler : onLinkFlowAnalyseClick};
		if (editTopoPower) {
			//items[items.length] = '-';
			items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteLinkClick};
			//items[items.length] = {text: I18N.NETWORK.bindLink, handler: onBindLinkClick};
			items[items.length] = '-';
		}
		items[items.length] = {text: I18N.COMMON.property, handler : onLinkPropertyClick};
		linkMenu = new Ext.menu.Menu({id: 'link-menu', ignoreParentClicks: true, items: items});
	}
	linkMenu.showAt([evt.pageX, evt.pageY]);
}

function nodeCoordChanged(id, x, y) {
}

function onSelectAdjacentClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		var filters = new Zeta$H();
		var depth = depthForOrgin;
		depthForOrgin = 1;
		countForOrgin = 0;
		findEntityByOrgin(filters, linkSrcEntityId);
		depthForOrgin = depth;
		filters.eachKey(function(v) {
			var n = nodeElMap.get(v);
			if (n != null) {
				n.className = 'ui-selected';
			}
		});
	}
}

function onSelectSameTypeClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		nodeElMap.eachValue(function(v) {
			if (v.objType == USEROBJ_TYPE.ENTITY && v.entityType == el.entityType) {
				v.className = 'ui-selected';
			}
		});
	}
}

function onReturnSuperiorClick() {
	Ext.menu.MenuMgr.hideAll();
	addView('topo' + superiorFolderId, superiorFolderName, 'topoLeafIcon',
		'topology/getTopoMapByFolderId.tv?folderId=' + superiorFolderId);
}
function onSynMicroGraphClick() {
 	$.ajax({url: '../topology/synMicroGraph.tv',
 		dataType: 'plain', cache: false,
 		data: {folderId: topoFolderId, mapWidth: getMapWidth(), mapHeight: getMapHeight()},
    	success: function() {},
    	error: function() {}
    });
}

function openFolderInNewWindow() {
	window.open('getTopoMapByFolderId.tv?remoteMode=true&folderId=' + topoFolderId);
}

function deleteEntityInPage(entityIds) {
	for (var i = 0; i < entityIds.length; i++) {
		var e = nodeElMap.get(entityIds[i]);
		if (e != null) {
			zgraph.removeChild(e);
		}
	}
}
function updateEntityStateInPage(entityId, state) {
	var el = Zeta$('alert' + entityId);
	if (el != null && el.alert != state) {
		el.style.cursor = (state == 0 ? 'default' : 'pointer');
		el.src = levelImgs[state].src;
		el.alert = state;
	}
}
function updateEntityName(id, n, url) {
	var el = nodeElMap.get(id);
	if (el) {
		el.name = n;
		el.url = url;
		el.children[3].innerText = n;
	}
}

function onNewEntityClick() {
	window.top.createDialog('modalDlg', I18N.COMMON.newEntity, 420, 280, 'entity/showNewEntity.tv?folderId=' + topoFolderId, null, true, true);
}

function onNewMonitorClick() {
    var el = nodeElMap.get(linkSrcEntityId);
	window.parent.newMonitor(linkSrcEntityId,el.ip);
}
function onSaveClick() {
	var length = nodeElMap.size();
	if (length == 0) {return;}
	var x = []; var y = []; var entityIds = [];
	var count = 0;
	if (length > 100) {
		showWaitingDlg(I18N.NETWORK.saveEntityCoord, 'ext-mb-waiting');
	}
	nodeElMap.eachValue(function(v) {
		if (v.tagName == 'line') {
			var arr = ('' + v.from).split(',');
			v.coordX = x[count] = parseInt(arr[0].replace('pt', ''));
			v.coordY = y[count] = parseInt(arr[1].replace('pt',''));
		} else {
			v.coordX = x[count] = parseInt(v.offsetLeft);
			v.coordY = y[count] = parseInt(v.offsetTop);
		}
		entityIds[count] = v.entityId;
		count++;
	});
	var failureCallback = function(){
		if (length > 100) {
			closeWaitingDlg();
		}
		showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.saveFailure, 'error');
	};
	var successCallback = function() {
		if (length > 100) {
			closeWaitingDlg();
		}
	};
	sendRequest('../topology/saveEntiyCoordinateByIds.tv', 'POST',
		{folderId: topoFolderId, zoomSize: zoom.size, entityIds: entityIds, x: x, y: y},
		successCallback, failureCallback);
}

function onDeleteLinkClick() {
	showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.deleteEntity, function(type) {
		if (type == 'no') {return;}
		var linkIds = [focusLinkId];
		var failureCallback = function() {
			showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.deleteEntityFailure, 'error');
		};
		var successCallback = function(){
			var el = linkElMap.get(focusLinkId);
			linkElMap.remove(focusLinkId);
			if(el != null) {
				zgraph.removeChild(el);
			}
		};
		param = {folderId: topoFolderId, linkIds: linkIds};
		sendRequest('../topology/deleteEntiyOrLinkById.tv', 'POST', param,
			successCallback, failureCallback);
	});
}
function onBindLinkClick() {
	Ext.menu.MenuMgr.hideAll();
	addView('linkinfo-' + focusLinkId, I18N.NETWORK.bindLink, 'linkIcon',
		'link/showLinkInfo.tv?linkId=' + focusLinkId);
}
function onDeleteClick() {
	var nodes = getSelectedNodeId();
	if (nodes[0].length == 0 && nodes[1].length == 0 && nodes[2].length == 0) {
		showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSelected);
	} else {
		showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.deleteEntity, function(type) {
			if (type == 'no') {return;}
			if (nodes[0].length > 10) {
				showWaitingDlg(I18N.NETWORK.deletingEntityAndLink);
			}
			var failureCallback = function() {
				if (nodes[0].length > 10) {
					closeWaitingDlg();
				}
				window.top.showErrorDlg();
			};
			var successCallback = function() {
				if (isSubnetMap) {
					refreshSubnet();
					return;
				}

				var entityId = 0;
				var el = null;
				for (var i = 0; i < nodes[1].length; i++) {
					entityId = nodes[1][i];
					el = linkElMap.get(entityId);
					linkElMap.remove(entityId);
					if(el != null) {
						zgraph.removeChild(el);
					}
				}

				for (var i = 0; i < nodes[0].length; i++) {
					entityId = nodes[0][i];
					el = nodeElMap.get(entityId);
					nodeElMap.remove(entityId);
					if (el != null) {
						deleteLinkByEntity(entityId);
						zgraph.removeChild(el);
					}
				}
				entityCount = entityCount - nodes[0].length;
				if (nodes[0].length > 10) {
					closeWaitingDlg();
				}
			};
			param = {
				folderId: topoFolderId
			};
			if (nodes[0].length > 0) {
				param.entityIds = nodes[0];
			}
			if (nodes[1].length > 0) {
				param.linkIds = nodes[0];
			}
			if (nodes[2].length > 0) {
				param.folderIds = nodes[0];
			}
			sendRequest('../topology/deleteEntiyOrLinkById.tv', 'POST', param,
				successCallback, failureCallback);
		});
	}
}
function onPrintClick(){
	var wnd = window.open();
	showPrintWnd(zgraph, wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
}
function onPrintPreviewClick(){
	showPrintWnd(zgraph, null);
}
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn chemas-microsoft-com:vml" xml:lang="en" lang="en">');
	doc.write('<head>');
	doc.write('<style>v\\:* {behavior:url(#default#VML);}</style>');
	doc.write('<title>' + topoFolderName + '</title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/zeta-graph.css"/>');
	doc.write('</head>');
	doc.write('<body class=zetaGraph style="padding:0px;margin:0px;');
	if (backgroundFlag) {
		if (displayGrid) {
			doc.write('background:' + backgroundGrid);
		} else {
			doc.write('background:' + backgroundColor + ' ' + backgroundImg);
			if (backgroundPosition == 0) {
				doc.write(' no-repeat');
			} else if (backgroundPosition == 1) {
				doc.write(' center 0 no-repeat');
			}
		}
	} else {
		doc.write('background:' + backgroundColor);
	}
	doc.write('">');
	doc.write(obj.innerHTML);
	doc.write('</body>');
	doc.write('</html>');
	doc.close();
}
function encodeGraph2XML() {
	return '';
}
function decodeXML2Graph(xml) {
	/*if (document.implementation && document.implementation.createDocument)
	{
	xmlDoc = document.implementation.createDocument("", "", null);
	xmlDoc.onload = createTable;
	}
	else if (window.ActiveXObject)
	{
	xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	xmlDoc.onreadystatechange = function () {
		if (xmlDoc.readyState == 4) createTable();
	};*/
}
function onFindClick() {
	showInputDlg(I18N.NETWORK.findEntity, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim();
		if(match=='') {showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag = true;
		var gotoEl = null;
		nodeElMap.eachValue(function(v) {
			if (v.objType == USEROBJ_TYPE.ENTITY || v.objType == USEROBJ_TYPE.FOLDER) {
				if ((v.entityName && v.entityName == match) ||
					(v.ip && v.ip == match)) {
					goToEntity(v.nodeId);
					if (flag) {
						flag = false;
					}
				}
			}
		});
		if (flag) {
			showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);
		}
	});
}
function onFind1Click() {
	showInputDlg(I18N.NETWORK.findEntity, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim();
		if(match=='') {showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag = true;
		var gotoEl = null;
		nodeElMap.eachValue(function(v) {
			if (v.objType == USEROBJ_TYPE.ENTITY || v.objType == USEROBJ_TYPE.FOLDER) {
				if ((v.entityName && v.entityName.indexOf(match) != -1) ||
					(v.ip && v.ip.indexOf(match) != -1)) {
					v.className = 'ui-gotoed';
					if (flag) {
						gotoEl = v;
						flag = false;
					}
				} else {
					v.className = 'ui-unselecting';
				}
			}
		});
		if (flag) {
			showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);
		} else {
			zgraph.scrollLeft = gotoEl.offsetLeft;
			zgraph.scrollTop = gotoEl.offsetTop;
		}
	});
}
function onRefreshClick() {
	if (isSubnetMap) {
		refreshSubnet();
	} else {
		refreshMap();
	}
}
function onTemplateToggleClick(item, pressed){
	if (pressed) {
		if (templateDlg == null) {
			entityTemplateTree = new Ext.tree.TreePanel({el: 'template-div', autoScroll: true,
				useArrows:useArrows, animate: animCollapse, border: false,  singleExpand: true,
				lines: true, rootVisible: false, enableDrag:true, trackMouseOver: trackMouseOver,
				loader: new Ext.tree.TreeLoader({dataUrl:'../entity/loadEntityTemplate.tv?hasRoot=false'})
			});
			var templateRootNode = new Ext.tree.AsyncTreeNode({text: I18N.MAIN.templateRootNode,
			draggable:false, id:'entityTemplateSource'});
			entityTemplateTree.setRootNode(templateRootNode);
			entityTemplateTree.render();
			templateRootNode.expand();

			Ext.dd.Registry.register('zetaGraph');
			initializeMapDropZone();

			var y = parseInt((document.body.clientHeight - 400) / 2);
			y = y < 30 ? 30 : y;
			templateDlg = new Ext.Window({id:'tmDlg', title:I18N.NETWORK.templateTitle, layout:'fit',
				minWidth: 120, width:180, minHeight: 200, height:420, plain:true, closable:true, closeAction:'hide', resizable: true, stateful:false,
				shadow:false, items:[entityTemplateTree]});
			templateDlg.on('hide', doTemplateClosing);
			templateDlg.setPosition(document.body.clientWidth - 230, y);
		} else {
			entityTemplateTree.getRootNode().reload();
		}
		templateDlg.show();
	} else {
		templateDlg.hide();
	}
}
function initializeMapDropZone() {
	var dropZone = new Ext.dd.DropZone(Ext.get("zetaGraph"), {
		ddGroup : "TreeDD",

		onNodeOver : function(target, dd, e, data) {
			return Ext.dd.DropZone.prototype.dropAllowed;
		},

		onNodeDrop : function(target, dd, e, data){
			var successCallback = function(response) {
				if (isSubnetMap) {
					refreshSubnet();
					return;
				}
				var json = Ext.util.JSON.decode(response.responseText);
				createEntity(json);
			};
			sendRequest('../entity/createEntityByTypeId.tv', 'GET',
				{typeId:data.node.id, coordx:event.x, coordy:event.y, folderId:topoFolderId},
				successCallback, defaultFailureCallback);
			return true;
		}
	});
}
function onDrawingToggleClick(item, pressed) {
	if (pressed) {
		if (drawingDlg == null) {
			var y = parseInt((document.body.clientHeight - 360) / 2);
			y = y < 30 ? 30 : y;
			var drawingsPanel = new Ext.Panel({el:'drawings-div', border:false, plain:false});
			drawingDlg = new Ext.Window({id:'drawingsDlg', layout: 'fit',
				title:I18N.NETWORK.drawingTitle, width:70, minWidth: 70, height: 360, minHeight: 200, plain:true,
				resizable: true, stateful:false, closable:true, items:[drawingsPanel],
				shadow:false});
			drawingDlg.on('hide', doDrawingDlgClosing);
			drawingDlg.setPosition(10, y);
		}
		drawingDlg.show();
	} else {
		drawingDlg.hide();
	}
}

function doDrawingDlgClosing() {
	drawingBt.setChecked(false);
	setDrawingType(CURSOR_TYPE.POINTER);
	return false;
}

function doTemplateClosing() {
	templateBt.setChecked(false);
	return false;
}

function onMaxViewClick(item) {
	window.parent.enableMaxView();
}

function onDefaultArrangeClick() {arrangeTopoMap(0);}
function onCircularArrangeClick() {arrangeTopoMap(1);}
function onTreeArrangeClick() {arrangeTopoMap(2);}
function onReverseTreeArrangeClick() {arrangeTopoMap(3);}
function onEastTreeArrangeClick() {arrangeTopoMap(5);}
function onWestTreeArrangeClick() {arrangeTopoMap(6);}
function onSymmetricArrangeClick() {arrangeTopoMap(4);}
function onHierarchicArrangeClick() {arrangeTopoMap(7);}

function onViewByTopoMapClick() {}
function onViewByIconClick() {
	location.href = 'getTopoMapByFolderId.tv?viewType=icon&folderId=' + topoFolderId;
}
function onViewByDetailClick() {
	location.href = 'getTopoMapByFolderId.tv?viewType=detail&folderId=' + topoFolderId;
}

function onDeleteVmlClick() {
	var node = null;
	if (linkSrcEntityId) {
		node = nodeElMap.get(linkSrcEntityId);
		if (node != null && node.objType == USEROBJ_TYPE.SHAPE && node.figureType != SHAPE_TYPE.PICTURE) {
			node = null;
		}
	}
	if (focusedShapeId > 0) {
		node = nodeElMap.get(focusedShapeId);
	}
	if (node) {
		showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.deleteVmlNode, function(type) {
			if (type == 'no') {return;}
			var scallback = function() {
				if (node.figureType == SHAPE_TYPE.PICTURE) {
					deleteLinkByEntity(node.nodeId);
				}
				zgraph.removeChild(node);
				nodeElMap.remove(node.nodeId);
			};
			sendRequest('../topology/deleteVmlById.tv', "GET",
				{nodeId: node.nodeId, folderId: topoFolderId},
				scallback, defaultFailureCallback);
		});
	}
}
function onResizeVmlClick() {
	resizeVmlNode(focusedShapeId);
}
function onTextVmlClick() {
	var e = nodeElMap.get(focusedShapeId);
	if (e.figureType == SHAPE_TYPE.TEXTRECT) {
		setShapeNodeText(focusedShapeId);
	} else {
		setTextBoxText(focusedShapeId);
	}
}
function onFillColorVmlClick() {
	createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		if (window.parent.ZetaCallback != null && window.parent.ZetaCallback.type == 'colorpicker') {
			var e = nodeElMap.get(focusedShapeId);
			var scallback = function() {e.fillcolor = window.parent.ZetaCallback.color;};
			sendRequest('../topology/setMapNodeFillColor.tv', "GET",
				('mapNode.nodeId='+e.nodeId+'&mapNode.fillColor='+window.parent.ZetaCallback.color),
				scallback, defaultFailureCallback);
		}
	});
}
function onSetStrokeWeightClick() {
	var e = nodeElMap.get(focusedShapeId);
	if (e) {
		showInputDlg(I18N.COMMON.inputTitle, I18N.NETWORK.inputStrokeWeightMsg, function(type, text) {
			if (type == 'cancel') {return;}
			var match = text.trim();
			if (isNaN(match)) {
				showMessageDlg(I18N.COMMON.error, I18N.COMMON.parameterIllegal, 'error');
			} else {
				var successCallback = function() {e.strokeweight = match;};
				sendRequest('../topology/setMapNodeStrokeWeight.tv', 'POST',
					'mapNode.nodeId=' + e.nodeId + '&mapNode.strokeWeight=' + match,
					successCallback, defaultFailureCallback);
			}
		});
	}
}
function onEnableDashedClick() {
	var e = nodeElMap.get(focusedShapeId);
	if (e) {
		var scallback = function() {
			var stroke = document.createElement("v:stroke");
			stroke.dashstyle = 'Dash';
			e.appendChild(stroke);
		};
		sendRequest('../topology/setMapNodeDashedBorder.tv', "GET",
			('mapNode.nodeId='+e.nodeId+'&mapNode.dashed=true'),
			scallback, defaultFailureCallback);
	}
}
function onEnableShadowClick() {
	var e = nodeElMap.get(focusedShapeId);
	if (e) {
		var scallback = function() {
			if (e.children.length == 1 && e.children[0].tagName == 'shadow') {
				e.removeChild(e.children[0]);
			} else {
				var shadow = document.createElement('v:shadow');
				shadow.on='T';
				shadow.type='single';
				shadow.color='#b3b3b3';
				shadow.offset='4px,4px';
				e.appendChild(shadow);
			}
		};
		sendRequest('../topology/setMapNodeDashedBorder.tv', "GET",
			('mapNode.nodeId='+e.nodeId+'&mapNode.shadow=true'),
			scallback, defaultFailureCallback);
	}
}

function setDisplayAlertIcon(flag) {
	displayAlertIcon = flag;
	if (flag) {
		nodeElMap.eachValue(function(v) {
			v.children[1].style.display = '';
		});
	} else {
		nodeElMap.eachValue(function(v) {
			v.children[1].style.display = 'none';
		});
	}
}

function onDisplayNameClick(item) {
	if(item.checked) return;
	setDisplayName(true);
}
function onDisplayIpClick(item) {
	if(item.checked) return;
	setDisplayName(false);
}
function onDisplayLinkLabelClick() {
	setDisplahLinkLabel(displayLinkLabel);
}
function setDisplahLinkLabel(flag) {
	displayLinkLabel = flag;
	if (flag) {
		linkLabelElMap.eachValue(function(v) {
			v.innerText = v.linkLabel;
		});
	} else {
		linkLabelElMap.eachValue(function(v) {
			v.innerText = '';
		});
	}
}
function setDisplahLinkShadow(flag, folderId) {
	if (folderId != topoFolderId) {
		return;
	}
	if (flag) {
		linkElMap.eachValue(function(v) {
			var shadow = document.createElement('v:shadow');
			shadow.on = 'T';
			shadow.type = linkStyle.linkShadowType;
			shadow.color = linkStyle.linkShadowColor;
			if (true) {
				shadow.offset = '1.5pt,1.5pt';
			} else {
				shadow.offset = '3pt,3pt';
				shadow.color2 = 'black';
				//shadow.offset2 = '3pt,3pt';
			}
			v.appendChild(shadow);
		});
	} else {
		linkElMap.eachValue(function(v) {
			v.removeChild(v.children[1]);
		});
	}
}
function setDisplayName(flag) {
	renamable = displayName = flag;

	if (flag) {
		zoom.textWidth = parseInt(80 * zoom.size);
	} else {
		zoom.textWidth = parseInt(100 * zoom.size);
	}
	zoom.width = zoom.textWidth + 10;

	var span = null;
	if (flag) {
		nodeElMap.eachValue(function(child) {
			if(child.objType == USEROBJ_TYPE.ENTITY) {
				child.style.width = zoom.width;
				span = child.children[3];
				span.style.width = zoom.textWidth;
				span.innerText = child.entityName;
				changeLinkByEntity(child.entityId,
					child.offsetLeft + parseInt(child.clientWidth / 2),
					child.offsetTop + parseInt(child.children[0].clientHeight / 2) + linkStyle.imgOffset);
			}
		});
	} else {
		nodeElMap.eachValue(function(child) {
			if(child.objType == USEROBJ_TYPE.ENTITY) {
				child.style.width = zoom.width;
				span = child.children[3];
				span.style.width = zoom.textWidth;
				span.innerText = child.ip;
				changeLinkByEntity(child.entityId,
					child.offsetLeft + parseInt(child.clientWidth / 2),
					child.offsetTop + parseInt(child.children[0].clientHeight / 2) + linkStyle.imgOffset);
			}
		});
	}

	if (isSubnetMap) {
		relayoutSubnet();
	}
}
function displayEntityTypeChanged(type) {
	displayEntity = (entityForOrgin == 0) && displayRouter && displaySwitch && displayL3switch
		&& displayServer && displayDesktop && displayOthers;
	changeEntityVisible();
	changeLinkVisible();

	selectAllEntityItem1.setChecked(displayEntity);

	if (selectAllEntityItem!= null){
		selectAllEntityItem.setChecked(displayEntity);
	}
}
function changeLinkVisible() {
	linkElMap.eachValue(function(node) {
		if (!displayLink || nodeElMap.get(node.srcEntityId).style.display == 'none' ||
			nodeElMap.get(node.destEntityId).style.display == 'none') {
			node.style.display = 'none';
		} else {
			if (node.style.display == 'none') {
				node.style.display = '';
			}
		}
	});
}
function changeEntityVisible() {
	var filters = null;
	if (entityForOrgin > 0) {
		filters = new Zeta$H();
		countForOrgin = 0;
		filters.put(entityForOrgin, entityForOrgin);
		findEntityByOrgin(filters, entityForOrgin);
		var n = nodeElMap.get(entityForOrgin);
		if (n != null) {
			n.className = 'ui-starting';
		}
	}
	var isFilter = (filters != null);
	var type = 0;
	var visible = false;
	nodeElMap.eachValue(function(node) {
		if (node.objType == USEROBJ_TYPE.SHAPE && node.figureType != SHAPE_TYPE.PICTURE) {
			return;
		}
		if (isFilter) {
			visible = filters.containsKey(node.nodeId);
			if (!visible) {
				node.style.display = 'none';
				return;
			}
		} else {
			visible = false;
		}
		type = node.entityType;
		if (type == 2) {
			visible = displaySwitch;
		} else if (type == 4) {
			visible = displayRouter;
		} else if (type == 6) {
			visible = displayL3switch;
		} else if (type == 72) {
			visible = displayServer;
		} else if (type == 127) {
			visible = displayDesktop;
		} else {
			visible = displayOthers;
		}
		if (visible && !displayNoSnmpEntity) {
			visible = node.snmpSupport;
		}
		node.style.display = visible ? '' : 'none';
	});
}

function onDisplayDepth1Click() {
	setDisplayDepthForEntity(linkSrcEntityId, 1);
}
function onDisplayDepth2Click() {
	setDisplayDepthForEntity(linkSrcEntityId, 2);
}
function onDisplayDepth3Click() {
	setDisplayDepthForEntity(linkSrcEntityId, 3);
}
function onCancelCascadeClick() {
	setDisplayDepthForEntity(0, 1);
}
function onFindCascadeClick() {
	if (entityForOrgin > 0) {
		var n = nodeElMap.get(entityForOrgin);
		if (n != null) {
			n.className = 'ui-starting';
		}
	}
}
function setDisplayDepthForEntity(entityId, depth) {
	entityForOrgin = entityId;
	depthForOrgin = depth;
	displayEntityTypeChanged(0);
	if(editTopoPower) {
		sendRequest('../topology/updateDisplayEntityForOrgin.tv', 'GET',
			'topoFolder.folderId=' + topoFolderId + '&topoFolder.entityForOrgin=' + entityId + '&topoFolder.depthForOrgin=' + depth,
			function(){}, function(){});
	}
}
function onDisplayEntityClick() {
	displayEntity = !displayEntity;
	if (displayEntity) {
		entityForOrgin = 0;
		depthForOrgin = 1;
	}
	displayRouter = displayEntity;
	displaySwitch = displayEntity;
	displayL3switch = displayEntity;
	displayServer = displayEntity;
	displayDesktop = displayEntity;
	displayOthers = displayEntity;

	if (displayEntity && editTopoPower) {
		sendRequest('../topology/updateDisplayAllEntity.tv', 'GET',
			'topoFolder.folderId=' + topoFolderId + '&displayAllEntity=' + displayEntity,
			function(){}, function(){});
	}
}

function onDisplayEntityClick1() {
	displayEntity = !displayEntity;
	if (displayEntity) {
		entityForOrgin = 0;
		depthForOrgin = 1;
	}
	displayRouter = displayEntity;
	displaySwitch = displayEntity;
	displayL3switch = displayEntity;
	displayServer = displayEntity;
	displayDesktop = displayEntity;
	displayOthers = displayEntity;

	if (displayEntity && editTopoPower) {
		sendRequest('../topology/updateDisplayAllEntity.tv', 'GET',
			'topoFolder.folderId=' + topoFolderId + '&displayAllEntity=' + displayEntity,
			function(){}, function(){});
	}
}

function setDisplayLink(flag) {
	displayLink = flag;
	changeLinkVisible();
}
function onDisplayGridClick(folderId, flag) {
	if (topoFolderId == folderId) {
		setDisplayGrid(flag);
	}
}

function onAddLinkClick() {
	linkAdding = true;
	zgraph.style.cursor = 'crosshair';
	drawTopoNodeLink(SHAPE_TYPE.SOLID_LINE);
}
function onMoveToClick() {
	createDialog('topoFolderTree', I18N.NETWORK.moveToTitle, 300, 330, 'network/popFolderTree.jsp', null, true, true,
		function(){
			if (window.parent.ZetaCallback.type != 'ok') {return;}
			var selectedItemId = window.parent.ZetaCallback.selectedItemId;
			if (selectedItemId == topoFolderId) {return;}
			var nodes = getSelectedEntityId();
			if (nodes.length == 0) {return;}
			var successCallback = function() {
				if (isSubnetMap) {
					refreshSubnet();
					return;
				}
				var entityId = 0;
				for (var i = 0; i < nodes.length; i++) {
					entityId = nodes[i];
					deleteLinkByEntity(entityId);
					zgraph.removeChild(nodeElMap.get(entityId));
				}
			};
			sendRequest('../entity/moveEntityById.tv', 'POST',
				{folderId:topoFolderId, destFolderId:selectedItemId, entityIds:nodes},
				successCallback, defaultFailureCallback);
			window.parent.ZetaCallback = null;
	});
}
function onPhysicalPaneClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	viewPhysicalPane(linkSrcEntityId, el.ip);
}
function onLogicPaneClick() {
	Ext.menu.MenuMgr.hideAll();
	var el = nodeElMap.get(linkSrcEntityId);
	addView('entity-' + linkSrcEntityId,
		I18N.NETWORK.entity + '[' + el.entityIp + ']',
		'entityTabIcon', 'entity/showLogicPane.tv?module=7&entityId=' + linkSrcEntityId);
}
function onSoftwareInstalledClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	createDialog("modalDlg", I18N.NETWORK.softwareInstalled + " - " + el.ip, 660, 440,
		"asset/getSoftwareInstalled.tv?entityId=" + linkSrcEntityId, null, true, true);
}
function onAssetDetailClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	createDialog("modalDlg", I18N.NETWORK.assetDetail + " - " + el.ip, 660, 450,
		"asset/getAssetDetail.tv?entityId=" + linkSrcEntityId, null, true, true);
}
function onEntityLinkTableClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		createDialog("modalDlg", I18N.NETWORK.entityLinkGraph + " - " + el.ip, 600, 400,
			"../link/getEntityLinkTableJsp.tv?entityId=" + linkSrcEntityId, null, true, true);
	}
}
function onEntityPropertyClick() {
	showProperty('entity/showEntityPropertyJsp.tv?entityId=' + linkSrcEntityId + '&folderId=' + topoFolderId);
}
function onViewAlarmClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		Ext.menu.MenuMgr.hideAll();
		addView("entity-" + linkSrcEntityId, I18N.COMMON.entity + "[" + el.ip + ']', 'entityTabIcon',
			"alert/showEntityAlertJsp.tv?module=6&entityId=" + linkSrcEntityId);
	}
}
function onReportClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	createDialog("modalDlg", I18N.COMMON.report + " - " + el.ip, 660, 450,
		"report/showReportByEntityId.tv?entityId=" + linkSrcEntityId, null, true, true);
}
function onDiscoveryAgainClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		window.top.discoveryEntityAgain(linkSrcEntityId, el.ip, function() {
		});
	}
}
function onPingClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		showPing(linkSrcEntityId, el.ip);
	}
}
function onTracertClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		showTracert(linkSrcEntityId, el.ip);
	}
}
function onTelnetClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		Ext.menu.MenuMgr.hideAll();
		showTelnet(linkSrcEntityId, el.ip);
	}
}
function onNativeTelnetClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		Ext.menu.MenuMgr.hideAll();
		window.open('telnet://' + el.ip, 'ntelnet' + linkSrcEntityId);
	}
}
function onAddToGoogleClick() {
	window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
}
function onCpuMemClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		Ext.menu.MenuMgr.hideAll();
		showCpuAndMem(linkSrcEntityId, el.ip);
	}
}
function onPortRealStateClick() {
	Ext.menu.MenuMgr.hideAll();
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		showPortRealState(linkSrcEntityId, el.ip);
	}
}
function onMibBrowseClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el != null) {
		Ext.menu.MenuMgr.hideAll();
		addView('mib' + linkSrcEntityId, I18N.COMMON.entity + '[' + el.ip + ']',
			'entityTabIcon', 'realtime/showEntityMibJsp.tv?module=4&entityId=' + linkSrcEntityId);
	}
}
function onEntitySnapClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		Ext.menu.MenuMgr.hideAll();
		showEntitySnap(linkSrcEntityId, el.ip);
	}
}
function showEntitySnap(id, ip) {
	addView('entity-' + id, I18N.COMMON.entity + '[' + ip + ']',
		'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + id);
}
function onViewConfigClick() {
	var el = nodeElMap.get(linkSrcEntityId);
	if (el) {
		Ext.menu.MenuMgr.hideAll();
		addView('entity-' + linkSrcEntityId, I18N.COMMON.entity + '[' + el.ip + ']',
			'entityTabIcon', 'entity/showEditEntityJsp.tv?module=2&entityId=' + linkSrcEntityId);
	}
}

function setAllSelected() {
	allSelected = !allSelected;
	if (allSelected) {
		selectAllCell();
	} else {
		disselectAllCell();
	}
}

function onSelectAllClick() {selectAllCell();}
function onDisselectClick() {disselectAllCell();}
function onSelectAllEntityClick() {selectAllEntity();}
function onSelectAllLinkClick() {selectAllLink();}
function onSelectSnmpEntityClick() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			v.className =  v.snmpSupport ? 'ui-selected' : 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});

	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}
function onSelectNotSnmpEntityClick() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			v.className =  v.snmpSupport ? 'ui-unselecting' : 'ui-selected';
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});

	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}
function onSelectAgentEntityClick() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			v.className =  v.agentSupport ? 'ui-selected' : 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});

	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}
function onSelectHasLinkClick() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			if (linkSrcMap.get(v.entityId).length > 0 ||
   				linkDestMap.get(v.entityId).length > 0) {
				v.className = 'ui-selected';
			} else {
   				v.className =  'ui-unselecting';
   			}
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});

	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}
function onSelectNoLinkClick() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			if (linkSrcMap.get(v.entityId).length == 0 &&
   				linkDestMap.get(v.entityId).length == 0) {
				v.className = 'ui-selected';
			} else {
   				v.className =  'ui-unselecting';
   			}
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});

	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}
function onPasteClick() {pasteEntity();}
function onCopyClick() {copyEntity();}
function onPropertyClick() {
	window.top.showProperty('topology/getTopoFolderProperty.tv?folderId=' + topoFolderId);
}
function onSaveAsPictureClick() {
	Zeta$('displayName').value = displayName;
	Zeta$('mapWidth').value = getMapWidth();
	Zeta$('mapHeight').value = getMapHeight();
	Zeta$('zoomSize').value = zoom.size;
	graphForm.action="saveTopoAsPicture.tv"
	graphForm.submit();
}
function onLabelAndLegendClick() {
	showProperty("topology/showTopoLabelJsp.tv?folderId=" + topoFolderId, I18N.NETWORK.labelAndLegend);
}
function onZoomInClick(){
	zoom.scaleIndex++;
	if (zoom.scaleIndex > 9) {
		zoom.scaleIndex--;
		return;
	}
	zoomMap(zoom.scale[zoom.scaleIndex]);
	var el = Zeta$('zoomImg' + zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-off.gif';
	}
	zoomIndex++;
	el = Zeta$('zoomImg'+zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-on.gif';
	}
}
function onZoomOutClick() {
	zoom.scaleIndex--;
	if (zoom.scaleIndex < 0) {
		zoom.scaleIndex++;
		return;
	}
	zoomMap(zoom.scale[zoom.scaleIndex]);

	var el = Zeta$('zoomImg' + zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-off.gif';
	}
	zoomIndex--;
	el = Zeta$('zoomImg'+zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-on.gif';
	}
}
function onZoomFitClick(){
	zoom.scaleIndex = 4;
	zoomMap(zoom.scale[4]);
	var el = Zeta$('zoomImg' + zoomIndex);
	if (el != null) {
		el.src = '../images/zgraph/tick-off.gif';
	}
	zoomIndex= 5;
	Zeta$('zoomImg5').src = '../images/zgraph/tick-on.gif';
}
function onZoomFitClick1(img){
	Zeta$('zoomImg1').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[0]);
	zoomIndex = 1;
}
function onZoomFitClick2(){
	Zeta$('zoomImg2').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[1]);
	zoomIndex = 2;
}
function onZoomFitClick3(){
	Zeta$('zoomImg3').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[2]);
	zoomIndex = 3;
}
function onZoomFitClick4(){
	Zeta$('zoomImg4').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[3]);
	zoomIndex = 4;
}
function onZoomFitClick5(){
	Zeta$('zoomImg5').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[4]);
	zoomIndex = 5;
}
function onZoomFitClick6(){
	Zeta$('zoomImg6').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[5]);
	zoomIndex = 6;
}
function onZoomFitClick7(){
	Zeta$('zoomImg7').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[6]);
	zoomIndex = 7;
}
function onZoomFitClick8(){
	Zeta$('zoomImg8').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
 	zoomMap(zoom.scale[7]);
 	zoomIndex = 8;
}
function onZoomFitClick9(){
	Zeta$('zoomImg9').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[8]);
	zoomIndex = 9;
}
function onZoomFitClick10(){
	Zeta$('zoomImg10').src = '../images/zgraph/tick-on.gif';
	Zeta$('zoomImg' + zoomIndex).src = '../images/zgraph/tick-off.gif';
	zoomMap(zoom.scale[9]);
	zoomIndex = 10;
}
function onZoomCustomClick(){
	showInputDlg(I18N.NETWORK.input, I18N.NETWORK.customLevelMsg, function(type, text) {
		try {
			var size = parseInt(text.trim());
			if(size>=10 && size<=500){zoomMap(size/100);}
		}catch(err){showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.inputError, 'error');}
	});
}

function doExpandTemplateClick() {
}
function doAddToMapClick() {
}
function onLinkFlowAnalyseClick() {
	Ext.menu.MenuMgr.hideAll();
	//window.open('realtime/showLinkRealFlowJsp.tv?linkId=' + focusLinkId, 'linkflow-' + focusLinkId);
	//addView('linkflow-' + focusLinkId, I18N.NETWORK.linkFlowAanlyse,
		//'flowTabIcon', 'realtime/showLinkRealFlowJsp.tv?linkId=' + focusLinkId);
}
function onLinkPropertyClick() {
	showProperty("link/showLinkPropertyJsp.tv?linkId=" + focusLinkId);
}
function onVmlPropertyClick() {
	var el = nodeElMap.get(focusedShapeId);
	if (el == null) {
		el = nodeElMap.get(linkSrcEntityId);
		if (el != null && el.figureType != SHAPE_TYPE.PICTURE) {
			el = null;
		}
	}
	if (el != null) {
		showProperty("topology/showVmlPropertyJsp.tv?nodeId=" + el.nodeId +
			'&folderId=' + topoFolderId + '&id=' + el.id);
	}
}
function onFolderPropertyClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src) {
		showProperty("topology/getTopoFolderProperty.tv?folderId=" + linkSrcEntityId);
	}
}
function onRenameFolderClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null && src.objType == USEROBJ_TYPE.FOLDER) {
		renameNodeStarted(src.children[3]);
	}
}
function onDeleteFolderClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null && src.objType == USEROBJ_TYPE.FOLDER) {
		showConfirmDlg(I18N.COMMON.tip, I18N.MAIN.confirmDelFolder, deleteFolderCallback);
	}
}
function deleteFolderCallback(type) {
	if (type == "no") {
		return;
	}
	var src = nodeElMap.get(linkSrcEntityId);
	var entityId = src.entityId;
	var name = src.entityName;
	Ext.Ajax.request({url: "../topology/deleteTopoFolder.tv",
	success: function (response) {
		var json = Ext.util.JSON.decode(response.responseText);
		if (json.success) {
			deleteLinkByEntity(entityId);
			try {
				zgraph.removeChild(src);
				var frame = getMenuFrame();
				frame.synDeleteFolder(entityId);
			} catch (err) {
			}
			nodeElMap.remove(entityId);
			window.top.removeTab('topo' + entityId);
		} else {
			if (json.child > 0) {
				window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.existEntityInFolder);
			}
		}
	}, failure:function () {
		showMessageDlg(I18N.MAIN.error, I18N.MAIN.serverDisconnected, "error");
	}, params:{folderId: entityId, name: name, superiorId: superiorFolderId}});
}

function onStatFolderClick() {
}

function onOpenEntityURLInNewWindow() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null) {
		var url = src.url == null ? '' : src.url;
		window.open(url, 'win' + src.nodeId);
	}
}

function onOpenEntityURL() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null) {
		Ext.menu.MenuMgr.hideAll();
		var url = src.url == null ? '' : src.url;
		addView("url" + src.nodeId, 'URL',  "topoLeafIcon", url);
	}
}

function onOpenURLInNewWindowClick() {
	var src = Zeta$(linkSrcEntityId);
	if (src != null) {
		var url = src.url == null ? '' : src.url;
		window.open(url, 'win' + src.nodeId);
	}
}

function onOpenURLClick() {
	var src = Zeta$(linkSrcEntityId);
	if (src != null) {
		Ext.menu.MenuMgr.hideAll();
		var url = src.url == null ? '' : src.url;
		addView("url" + src.nodeId, 'URL', "topoLeafIcon", url);
	}
}

function onOpenHrefInNewWindowClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null) {
		var url = (src.url == null ? '' : src.url);
		if (url != '' && src.folderType == 7) {
			// remote system
			if (url.indexOf("?") == -1) {
				url = url + '?remoteMode=true'
			} else {
				url = url + '&remoteMode=true';
			}
		}
		var win = window.open(url, 'win' + linkSrcEntityId);
		if (win !=  null) {
			win.focus();
		}
	}
}
function onOpenRemoteFolderClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src != null && src.url != null) {
		Ext.menu.MenuMgr.hideAll();
		var url = src.url;
		if (src.url.indexOf("?") == -1) {
			url = url + '?&remoteMode=true'
		} else {
			url = url + '&remoteMode=true';
		}
		addView("url" + linkSrcEntityId, src.entityName, "topoLeafIcon", url);
	}
}
function onOpenFolderClick() {
	var src = nodeElMap.get(linkSrcEntityId);
	if (src) {
		Ext.menu.MenuMgr.hideAll();
		addView("topo" + src.entityId, src.entityName,
			"topoLeafIcon", 'topology/getTopoMapByFolderId.tv?folderId=' + linkSrcEntityId);
	}
}

function relayoutSubnet() {
	var len = 0;
	var children = zgraph.children;
	for(var i = children.length - 1; i >= 0; i--) {
		if (children[i].objType == USEROBJ_TYPE.LINK) {
			zgraph.removeChild(children[i]);
		} else if (children[i].objType == USEROBJ_TYPE.ENTITY) {
			len++;
		}
	}
	zgraph.removeChild(Zeta$('bus'));
	zgraph.removeChild(Zeta$('busStart'));
	zgraph.removeChild(Zeta$('busEnd'));
	linkDestMap.clear;
	linkSrcMap.clear();
	linkElMap.clear();
	linkMap.clear();

	var width = zgraph.clientWidth;
	var columns = parseInt((width - offsetLeft - offsetRight) / columnWidth);
	var rows = parseInt(len / columns);
	if (len % columns > 0) {
		rows++;
	}

	createSubnetBus(width, rows, columns);

	var count = 1;
	var w = parseInt(zoom.width/2);
	len = 0;
	for(var i = children.length - 1; i >= 0; i--) {
		if (children[i].objType == USEROBJ_TYPE.ENTITY) {
			len++;
			var r = parseInt(i / columns);
			var c = parseInt(i % columns);
			if ( c == 0) {
				count = 1;
			}
			var x = entityOffsetLeft + c * columnWidth;
			var y = entityOffsetTop + offsetTop + r * rowHeight;
			children[i].style.left = x;
			children[i].style.top = y;
			var l = buildShapeNode(shapeIdSequence++, SHAPE_TYPE.SOLID_LINE, x + w, y + 30);
			l.id = 'line' + i;
			l.objType = USEROBJ_TYPE.LINK;
			l.to = (x + w) + ',' + (r * rowHeight + offsetTop);
			var arr = [];
			arr[0] = i;
			linkSrcMap.put(children[i].entityId, arr);
			zgraph.appendChild(l);
		}
	}
}

function refreshSubnet() {
	clearGraph();
	loadSubnetData(-1, true);
}
function loadSubnetData(gotoId, refreshed) {
	$.ajax({url: 'loadVertexByFolderId.tv', dataType: 'json', cache: 'false',
		data:{folderId: topoFolderId, entityLabel: entityLabel, linkLabel: linkLabel, refreshed: refreshed},
		success:function(json){
			var len = 0;
			if (json.node != null) {
				len = json.node.length;
				for (var i = 0; i < len; i++) {
					createMapNode(json.node[i]);
				}
			}
			if (json.entity != null) {
				entityCount = len = json.entity.length;
				if (len > 0) {
					var width = zgraph.clientWidth;
					var columns = parseInt((width - offsetLeft - offsetRight) / columnWidth);
					var rows = parseInt(len / columns);
					if (len % columns > 0) {
						rows++;
					}

					createSubnetBus(width, rows, columns);

					var count = 1;
					var w = parseInt(zoom.width/2);
					for (var i = 0; i < len; i++) {
						var r = parseInt(i / columns);
						var c = parseInt(i % columns);
						if ( c == 0) {
							count = 1;
						}
						var x = entityOffsetLeft + c * columnWidth;
						var y = entityOffsetTop + offsetTop + r * rowHeight;
						var l = buildShapeNode(shapeIdSequence++, SHAPE_TYPE.SOLID_LINE, x + w, y + 30);
						l.id = 'edge' + i;
						l.objType = USEROBJ_TYPE.LINK;
						l.to = (x + w) + ',' + (r * rowHeight + offsetTop);
						var arr = [];
						arr[0] = i;
						linkSrcMap.put(json.entity[i].entityId, arr);
						zgraph.appendChild(l);
						createSubnetEntity(json.entity[i], x, y);
					}
				}
			} else {
				entityCount = 0;
			}
			setTimeout('restoreTopologyState()', 3000);
			if (gotoId > 0) {
				goToEntity(gotoId);
			}
		},
		error:function(){
			showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.loadDataFailure, 'error');
		}
	});
}

function refreshMap() {
	clearGraph();
	loadMapData(0, true);
}

function loadMapData(gotoId, refreshed) {
	$.ajax({url: 'loadVertexByFolderId.tv', dataType: 'json', cache: 'false',
		data:{folderId: topoFolderId, entityLabel: entityLabel, linkLabel: linkLabel, refreshed: refreshed},
		success:function(json){
			parseTopoNode(json);
			if (gotoId > 0) {
				goToEntity(gotoId);
			}
		},
		error:function(){
			showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.loadDataFailure, 'error');
		}
	});
}

function parseTopoNode(json) {
	var len = 0;
	if (json.node != null) {
		len = json.node.length;
		for (var i = 0; i < len; i++) {
			createMapNode(json.node[i]);
		}
	}
	if (json.folder != null) {
		len = json.folder.length;
		for (var i = 0; i < len; i++) {
			createTopoFolder(json.folder[i]);
		}
	}
	if (json.entity != null) {
		entityCount = len = json.entity.length;
		for (var i = 0; i < len; i++) {
			createEntity(json.entity[i]);
		}
	} else {
		entityCount = 0;
	}
	$.ajax({url: 'loadEdgeByFolderId.tv', dataType: 'json', cache: 'false',
		data: {folderId: topoFolderId},
		success: function(json){
			if (json.link != null) {
				len = json.link.length;
				for (var i = 0; i < len; i++) {
					createLink(json.link[i]);
				}
			}
		},
		complete: function() {
			selectedCount = 0;
			displayEntityTypeChanged(0);
			tabShown();
			setTimeout('restoreTopologyState()', 3000);
		}
	});
}

function copyEntity() {
	var nodes = getSelectedEntityId();
	if (nodes.length == 0) {return;}
	setZetaClipboard({type:'copy', target:'topoFolder', src:topoFolderId, entityIds:nodes});
}
function pasteEntity() {
	var clipboard = getZetaClipboard();
	if (clipboard != null) {
		if (clipboard.src == topoFolderId) {return;}
		if (clipboard.type == 'copy') {
			var successCallback = function(response) {
				if (isSubnetMap) {
					refreshSubnet();
					return;
				}
				var json = Ext.util.JSON.decode(response.responseText);
				for (var i = 0; i < json.entity.length; i++) {
					createEntity(json.entity[i]);
				}
				for (var i = 0; i < json.link.length; i++) {
					var key = 'k' + json.link[i].srcEntityId + '_' + json.link[i].destEntityId;
					if(linkMap.containsKey(key)) continue;
					createLink(json.link[i]);
				}
				var entityIds = clipboard.entityIds;
				for (var i = 0; i < entityIds.length; i++) {
					var n = nodeElMap.get(entityIds[i]);
					if (n != null){n.className='ui-selected';}
				}
			};
			sendRequest('../entity/copyEntityById.tv', 'POST',
				{folderId:clipboard.src, destFolderId:topoFolderId, entityIds:clipboard.entityIds},
				successCallback, defaultFailureCallback);
		} else if (clipboard.type == 'cut') {
		}
		setZetaClipboard(null);
	}
}

function arrangeTopoMap(type) {
	var len = zgraph.children.length;
	if (len == 0) {
		return;
	}
	if (len > 2) {
		showWaitingDlg(I18N.NETWORK.arranging);
	}
	arrangeType = type;
	$.ajax({url:'arrangeEntityByFolderId.tv', data:{'folderId': topoFolderId, 'arrangeType':type,
		'mapWidth': getMapWidth(), 'mapHeight': getMapHeight()}, dataType:'json', cache:'false',
		success:function(json){
	   	   var el = null;
	   	   var x, y;
	   	   var size = json.length;
	       for (var i = 0; i < size; i++) {
	       	  if (!json[i].fixed) {
	       	  	  el = nodeElMap.get(json[i].entityId);
		       	  if (el != null) {
		       	  	  el.style.left = x = parseInt(json[i].x * zoom.size);
		       	  	  el.style.top = y = parseInt(json[i].y * zoom.size);
					  changeLinkByEntity(json[i].entityId, x + parseInt(el.clientWidth / 2),
					  	y + parseInt(el.children[0].clientHeight / 2 + linkStyle.imgOffset));
		       	  }
	       	  }
	       }
		},
		complete: function() {
			if (len > 2) {
				closeWaitingDlg();
			}
		}
	});
}

var currentZoomSize = 0.25;
var currentZoomTarget = 0.25;
var currentZoomInc = 0;
function animateZoom() {
	currentZoomSize = currentZoomSize + currentZoomInc;
	if (currentZoomInc > 0) {
		if (currentZoomSize > currentZoomTarget) {
			currentZoomSize = currentZoomTarget;
		}
	} else {
		if (currentZoomSize < currentZoomTarget) {
			currentZoomSize = currentZoomTarget;
		}
	}
	zoomGraph(currentZoomSize);
	if (currentZoomSize != currentZoomTarget) {
		setTimeout('animateZoom()', 200);
	}
}

function doLinkNodeMouseDown() {
	if (remoteMode || curDrawingType > 1) {return;}
	vmlFocusedCallback();
	var src = event.srcElement;
	while (src.tagName != 'line') {
		src = src.parentElement;
	}
	if (event.ctrlKey) {
		if (src.strokecolor == linkStyle.selectedColor) {
			src.strokecolor = src.oldColor;
		} else {
			src.strokecolor = linkStyle.selectedColor;
		}
	} else {
		if (event.button == MouseEvent.BUTTON1 ||
			(event.button == MouseEvent.BUTTON2 && src.strokecolor == src.oldColor)) {
			disselectAllCell();
		}
		src.strokecolor = linkStyle.selectedColor;
	}
	focusLinkId = src.linkId;
}
function doLinkNodeMouseUp() {
	if (curDrawingType > 1) {return;}
	vmlBluredCallback();
}
function doLinkNodeDblClick() {
	if (remoteMode || curDrawingType > CURSOR_TYPE.SQUARE) {
		return;
	}
	onLinkFlowAnalyseClick();
}

function createSubnetEntity(jsonEntity, x, y) {
	var nodeId = jsonEntity.entityId;
	var div = document.createElement("div");
	div.style.position = "absolute";
	div.align = 'center';
	div.id = 'div' + nodeId;
	div.style.left = x;
	div.style.top = y;
	div.style.width = zoom.width;

	div.objType = USEROBJ_TYPE.ENTITY;
	div.entityId = div.nodeId = nodeId;
	div.coordX = x;
	div.coordY = y;
	div.alert = -2;
	div.snmpSupport = jsonEntity.snmpSupport;
	div.agentSupport = jsonEntity.agentSupport;
	div.ip = jsonEntity.ip;
	div.entityName = jsonEntity.name;
	div.entityType = jsonEntity.type;

	linkSrcMap.put(nodeId, []);
	linkDestMap.put(nodeId, []);

    var img = document.createElement("img");
    img.id = 'img' + nodeId;
    img.vspace = 1;
	img.border = 0;
	img.src = jsonEntity.icon;

    var img1 = document.createElement("img");
    img1.id = 'alert' + nodeId;
    img1.title = I18N.NETWORK.nodata;
    img1.src = '../images/fault/nodata.gif';
	img1.width = zoom.smallIconWidth;
	img1.height = zoom.smallIconHeight;
    img1.style.marginLeft = zoom.smallIconOffsetLeft;
    img1.style.marginBottom = zoom.smallIconOffsetBottom;
    img1.vspace = 1;
	img1.border = 0;
	if (!displayAlertIcon) {
		img1.style.display = 'none';
	}

	var span = document.createElement('span');
	span.id = 'span' + nodeId;
	span.style.width = zoom.textWidth;
	span.style.fontSize = zoom.fontSize + 'px';
	span.innerText = displayName ? jsonEntity.name : jsonEntity.ip;

	div.onmousedown = doEntityNodeMouseDown;
	div.onmouseover = doEntityNodeMouseOver;
	div.onmouseout = doEntityNodeMouseOut;
	div.ondblclick = doEntityNodeDblClick;
	div.onclick = doEntityNodeClick;

	div.appendChild(img);
	div.appendChild(img1);
    div.appendChild(document.createElement("br"));
    div.appendChild(span);
	zgraph.appendChild(div);
	nodeElMap.put(nodeId, div);

	img.cluetip = jsonEntity.name;
	img.rel = '../entity/loadEntityTip.tv?entityId=' + nodeId + '&folderId=' + topoFolderId;
	$('#img' + nodeId).cluetip({cluetipClass: 'topvision', titleAttribute: 'cluetip',
		width: 320, arrows: false, waitImage: false, ajaxCache: false,
		dropShadow: false, activation: cluetipShowType,
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	   	},
	   	delayedClose: cluetipCloseTimeout,
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled
	});
}
function justPictureSize() {
	this.coordWidth = this.width;
	this.coordHeight = this.height;
	this.width = parseInt(this.coordWidth * zoom.size);
	this.height = parseInt(this.coordHeight * zoom.size);
	if (this.width > zoom.textWidth) {
		this.parentNode.style.width = this.width + 10;
	}
	this.parentNode.children[1].style.marginLeft = -parseInt((this.clientWidth + zoom.smallIconWidth) / 2);
}
function createEntity(userObj) {
	var nodeId = userObj.entityId;
	var div = document.createElement("div");
	div.id = 'vertex' + nodeId;
	div.align = 'center';
	div.style.position = "absolute";
	div.style.left = parseInt(userObj.x * zoom.size);
	div.style.top = parseInt(userObj.y * zoom.size);

	div.style.width = zoom.textWidth + 10;

	div.objType = USEROBJ_TYPE.ENTITY;
	div.entityId = div.nodeId = nodeId;
	div.coordX = userObj.x;
	div.coordY = userObj.y;
	div.alert = -2;
	div.url = userObj.url;
	div.ip = userObj.ip;
	div.entityName = userObj.name;
	div.entityType = userObj.type;
	div.snmpSupport = userObj.snmpSupport;
	div.agentSupport = userObj.agentSupport;
	div.online = false;

	linkSrcMap.put(nodeId, []);
	linkDestMap.put(nodeId, []);

    var img = document.createElement("img");
    img.id = 'img' + nodeId;
    img.vspace = 1;
	img.border = 0;
	img.onload = justPictureSize;

    var img1 = document.createElement("img");
    img1.id = 'alert' + nodeId;
    img1.src = '../images/fault/nodata.gif';
    img1.title = I18N.NETWORK.nodata;
	img1.width = zoom.smallIconWidth;
	img1.height = zoom.smallIconHeight;
    img1.vspace = 1;
	img1.border = 0;
    img1.style.marginLeft = zoom.smallIconOffsetLeft;
    img1.style.marginBottom = zoom.smallIconOffsetBottom;
	if (!displayAlertIcon) {
		img1.style.display = 'none';
	}

	var span = document.createElement('span');
	span.id = 'span' + nodeId;
	span.innerText = displayName ? userObj.name : userObj.ip;
	span.style.width = zoom.textWidth;
	span.style.fontSize = zoom.fontSize + 'px';

	div.onmousedown = doEntityNodeMouseDown;
	div.onmouseover = doEntityNodeMouseOver;
	div.onmouseout = doEntityNodeMouseOut;
	div.ondblclick = doEntityNodeDblClick;
	div.onclick = doEntityNodeClick;

	div.appendChild(img);
	div.appendChild(img1);
    div.appendChild(document.createElement("br"));
    div.appendChild(span);

	zgraph.appendChild(div);
	nodeElMap.put(nodeId, div);

	img.cluetip = userObj.name;
	img.rel = '../entity/loadEntityTip.tv?entityId=' + nodeId + '&folderId=' + topoFolderId;
	$('#img' + nodeId).cluetip({cluetipClass: 'topvision', titleAttribute: 'cluetip',
		width: 320, arrows: false, waitImage: false, ajaxCache: false,
		dropShadow: false, activation: cluetipShowType,
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	   	},
	   	delayedClose: cluetipCloseTimeout,
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled
	});

	$('#vertex' + nodeId).draggable({opacity: 0.50, scroll: true, helper: 'clone', distance: 3,
	containment: 'parent',
		stop: function(e, ui) {
			if (ui.position.left < 0) {
				//ui.position.left = 0;
			}
			if (ui.position.top < 0) {
				//ui.position.top = 0;
			}
			this.style.left = ui.position.left;
			this.style.top = ui.position.top;
			changeLinkByEntity(this.entityId, this.offsetLeft + parseInt(this.clientWidth / 2),
				this.offsetTop + parseInt(this.children[0].clientHeight / 2) + linkStyle.imgOffset);
			nodeCoordChanged(this.nodeId, ui.position.left, ui.position.top);
		},
		start: function(e, ui) {
			ui.helper.css("border", "0");
		}
	});
	if (userObj.fixed) {
		$('#vertex' + nodeId).draggable('disable');
	}

	img.src = userObj.icon;
}
function createTopoFolder(json) {
	var nodeId = json.folderId;
	var div = document.createElement("div");
	div.id = 'vertex' + nodeId;
	div.align = 'center';
	div.style.position = "absolute";
	div.style.left = parseInt(json.x * zoom.size);
	div.style.top = parseInt(json.y * zoom.size);

	div.objType = USEROBJ_TYPE.FOLDER;
	div.entityId = div.nodeId = nodeId;
	div.entityName = json.name;
	div.ip = json.name;
	div.coordX = json.x;
	div.coordY = json.y;
	div.alert = 0;
	div.folderType = json.type;
	div.url = json.url;
	linkSrcMap.put(nodeId, []);
	linkDestMap.put(nodeId, []);

    var img = document.createElement("img");
    img.id = 'img' + nodeId;
    img.vspace = 1;
	img.border = 0;
	img.onload = justPictureSize;

    var img1 = document.createElement("img");
    img1.id = 'alert' + nodeId;
    img1.src = '../images/s.gif';
	img1.width = zoom.smallIconWidth;
	img1.height = zoom.smallIconHeight;
	img1.vspace = 1;
	img1.border = 0;
    img1.style.marginLeft = zoom.smallIconOffsetLeft;
    img1.style.marginBottom = zoom.smallIconOffsetBottom;
	if (!displayAlertIcon) {
		img1.style.display = 'none';
	}

	var span = document.createElement('span');
	span.id = 'span' + nodeId;
	span.style.width = zoom.textWidth;
	span.style.fontSize = zoom.fontSize + 'px';
	span.innerText = json.name;

	div.appendChild(img);
	div.appendChild(img1);
	div.appendChild(document.createElement('br'));
	div.appendChild(span);
	zgraph.appendChild(div);
	nodeElMap.put(nodeId, div);

	div.onmousedown = doEntityNodeMouseDown;
	div.onmouseover = doEntityNodeMouseOver;
	div.onmouseout = doEntityNodeMouseOut;
	div.onclick = doEntityNodeClick;
	div.ondblclick = doFolderNodeDblClick;

	img.cluetip = json.name;
	img.rel = '../topology/loadFolderTip.tv?folderId=' + nodeId;
	$('#img' + nodeId).cluetip({cluetipClass: 'topvision', titleAttribute: 'cluetip',
		arrows: false, dropShadow: false, ajaxCache: true, waitImage: false,
		activation: cluetipShowType,
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	    },
	    delayedClose: cluetipCloseTimeout,
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled
	});

	$('#vertex' + nodeId).draggable({opacity: 0.50, scroll: true, helper: 'clone', distance: 3,
		stop: function(e, ui) {
			if (ui.position.left < 0) {
				ui.position.left = 0;
			}
			if (ui.position.top < 0) {
				ui.position.top = 0;
			}
			this.style.left = ui.position.left;
			this.style.top = ui.position.top;
			changeLinkByEntity(this.entityId, this.offsetLeft + parseInt(this.clientWidth / 2),
				this.offsetTop + parseInt(this.children[0].clientHeight / 2) + linkStyle.imgOffset);
			nodeCoordChanged(this.nodeId, ui.position.left, ui.position.top);
		},
		start: function(e, ui) {
			ui.helper.css("border", "0");
		}
	});
	if (json.fixed) {
		$('#vertex' + nodeId).draggable('disable');
	}

	img.src = json.icon;
}
function createMapNode(userObj) {
	var s = null;
	var type = userObj.type;
	var nodeId = userObj.nodeId;
	if (type == SHAPE_TYPE.PICTURE) {
		createPicture(userObj);
		return;
	} else {
		s = buildShapeNode(nodeId, type, userObj.x, userObj.y, userObj.text);
		if (s) {
			if (type > SHAPE_TYPE.POLY_LINE && type <= SHAPE_TYPE.TEXTRECT) {
				s.fillcolor = userObj.fillColor;
			}
			s.strokecolor = userObj.strokeColor;
			s.strokeweight = userObj.strokeWeight;
			if (userObj.dashed && type != SHAPE_TYPE.DASHED_LINE) {
				var stroke = document.createElement("v:stroke");
				stroke.dashstyle = 'Dash';
				s.appendChild(stroke);
			}
		}
	}
	if (type > SHAPE_TYPE.POLY_LINE) {
		s.style.left = parseInt(userObj.x * zoom.size);
		s.style.top = parseInt(userObj.y * zoom.size);
		if (userObj.type == SHAPE_TYPE.TEXT) {
			s.setAttribute('fontSize', userObj.fontSize);
			s.style.fontSize = parseInt(userObj.fontSize * zoom.size) + 'pt';
			s.style.color = userObj.fontColor;
		} else if (userObj.type == SHAPE_TYPE.TEXTRECT) {
			s.setAttribute('fontSize', userObj.fontSize);
			s.children[1].style.fontSize = parseInt(userObj.fontSize * zoom.size) + 'pt';
			s.children[1].style.color = userObj.fontColor;
			s.style.width = parseInt(userObj.width * zoom.size);
			s.style.height = parseInt(userObj.height * zoom.size);
		} else {
			s.style.width = parseInt(userObj.width * zoom.size);
			s.style.height = parseInt(userObj.height * zoom.size);
		}
	} else {
		s.from = userObj.x * zoom.size + 'pt,' + userObj.y * zoom.size + 'pt';
		s.to = (userObj.x + userObj.width) * zoom.size + 'pt,' + (userObj.y + userObj.height) * zoom.size + 'pt';
	}

	s.setAttribute('nodeId', nodeId);
	s.setAttribute('entityId', nodeId);
	s.setAttribute('coordX', userObj.x);
	s.setAttribute('coordY', userObj.y);
	s.setAttribute('coordWidth', userObj.width);
	s.setAttribute('coordHeight', userObj.height);
	s.setAttribute('objType', USEROBJ_TYPE.SHAPE);
	s.setAttribute('figureType', userObj.type);
	s.setAttribute('url', userObj.url);
	s.setAttribute('oldColor', userObj.strokeColor);
	zgraph.appendChild(s);
	nodeElMap.put(nodeId, s);

	if (type > SHAPE_TYPE.POLY_LINE) {
		$('#vertex' + nodeId).draggable({opacity: 0.50, scroll: true, helper: 'clone', distance: 3,
			stop: function(e, ui) {
				if (ui.position.left < 0) {
					ui.position.left = 0;
				}
				if (ui.position.top < 0) {
					ui.position.top = 0;
				}
				this.style.left = ui.position.left;
				this.style.top = ui.position.top;
			},
			start: function(e, ui) {
				ui.helper.css("border", "0");
			}
		});
	}
}
function createPicture(userObj) {
	var nodeId = userObj.nodeId;
	var div = document.createElement("div");
	div.id = 'vertex' + nodeId;
	div.align = 'center';
	div.style.position = "absolute";
	div.style.left = parseInt(userObj.x * zoom.size);
	div.style.top = parseInt(userObj.y * zoom.size);
	div.style.zIndex = 4;
	div.nodeId = nodeId;
	div.entityId = nodeId;
	div.entityName = userObj.text;
	div.ip = userObj.text;
	div.coordX = userObj.x;
	div.coordY = userObj.y;
	div.objType = USEROBJ_TYPE.SHAPE;
	div.figureType = SHAPE_TYPE.PICTURE;
	linkSrcMap.put(nodeId, []);
	linkDestMap.put(nodeId, []);

    var img = document.createElement("img");
    img.id = 'img' + nodeId;
    img.vspace = 1;
	img.border = 0;
	img.onload = justPictureSize;

    var img1 = document.createElement("img");
    img1.id = 'alert' + nodeId;
    img1.src = '../images/s.gif';
    img1.vspace = 1;
	img1.border = 0;

	var span = document.createElement('span');
	span.id = 'span' + nodeId;
	span.style.width = zoom.textWidth;
	span.style.fontSize = zoom.fontSize + 'px';
	span.innerText = userObj.text;
	//span.style.display = 'none';

	div.appendChild(img);
	div.appendChild(img1);
	div.appendChild(document.createElement('br'));
	div.appendChild(span);
	zgraph.appendChild(div);
	nodeElMap.put(nodeId, div);

	div.onmousedown = doEntityNodeMouseDown;
	div.onmouseover = doEntityNodeMouseOver;
	div.onmouseout = doEntityNodeMouseOut;
	div.onclick = doEntityNodeClick;

	$('#vertex' + nodeId).draggable({opacity: 0.50, scroll:true, helper: 'clone', distance: 3,
		stop: function(e, ui) {
			if (ui.position.left < 0) {
				ui.position.left = 0;
			}
			if (ui.position.top < 0) {
				ui.position.top = 0;
			}
			this.style.left = ui.position.left;
			this.style.top = ui.position.top;
			changeLinkByEntity(this.entityId, this.offsetLeft + parseInt(this.clientWidth / 2),
				this.offsetTop + parseInt(this.children[0].clientHeight / 2) + linkStyle.imgOffset);
			nodeCoordChanged(this.nodeId, ui.position.left, ui.position.top);
		},
		start: function(e, ui) {
			ui.helper.css("border", "0");
		}
	});
	img.src = userObj.icon;
	return div;
}
function createLink(userObj) {
	var srcEntityId = userObj.srcEntityId;
	var srcEle = nodeElMap.get(srcEntityId);
	if (srcEle == null) {return null;}
	var destEntityId = userObj.destEntityId;
	var destEle = nodeElMap.get(destEntityId);
	if (destEle == null) {return null;}
	var key = 'k' + srcEntityId + '_' + destEntityId;
	if (linkMap.containsKey(key)) {
		return null;
	}

	var nodeId = userObj.linkId;
	var line = document.createElement('v:line');
	line.id = 'edge' + nodeId;

	var fromx = srcEle.offsetLeft + parseInt(srcEle.clientWidth / 2);
	var fromy = srcEle.offsetTop + parseInt(srcEle.children[0].clientHeight / 2) + linkStyle.imgOffset;
	var tox = destEle.offsetLeft + parseInt(destEle.clientWidth / 2);
	var toy = destEle.offsetTop + parseInt(destEle.children[0].clientHeight / 2) + linkStyle.imgOffset;

	line.from = fromx + 'px,' + fromy + 'px';
	line.to = tox + 'px,' + toy + 'px';
	line.strokeweight = linkStyle.width;
	line.strokecolor = line.oldColor = linkStyle.color;

	line.nodeId = line.linkId = nodeId;
	line.objType = USEROBJ_TYPE.LINK;
	line.srcEntityId = srcEntityId;
	line.destEntityId = destEntityId;

	line.onmousedown = doLinkNodeMouseDown;
	line.onmouseup = doLinkNodeMouseUp;
	line.ondblclick = doLinkNodeDblClick;

	var span = document.createElement('span');
	span.id = 'linklabel' + nodeId;
	span.className = 'edgeLabel';
	span.style.position = 'relative';
	span.style.fontSize = zoom.fontSize;
	var diff = 0;
	if (fromx > tox) {
		diff = parseInt((fromx - tox)/2) - 2*zoom.fontSize;
	} else {
		diff = parseInt((tox - fromx)/2) - 2*zoom.fontSize;
	}
	if (diff < 0) {
		diff = 2;
	}
	span.style.left = diff;
	if (fromy > toy) {
		span.style.top = parseInt((fromy - toy)/2) - zoom.fontSize;
	} else {
		span.style.top = parseInt((toy - fromy)/2) - zoom.fontSize;
	}
	span.linkId = nodeId;
	span.objType = USEROBJ_TYPE.LINK;
	span.linkLabel = '';
	line.appendChild(span);

	if (linkShadow) {
		var shadow = document.createElement('v:shadow');
		shadow.on = 'T';
		shadow.type = linkStyle.linkShadowType;
		shadow.color = linkStyle.linkShadowColor;
		if (true) {
			shadow.offset = '1.5pt,1.5pt';
		} else {
			shadow.offset = '3pt,3pt';
			shadow.color2 = 'black';
		}
		line.appendChild(shadow);
	}

	zgraph.appendChild(line);
	linkElMap.put(nodeId, line);
	linkLabelElMap.put(nodeId, span);

	line.cluetip = userObj.name;
	line.rel = '../link/loadLinkTip.tv?linkId=' + nodeId + '&folderId=' + topoFolderId;
	$('#edge' + nodeId).cluetip({cluetipClass: 'topvision', titleAttribute: 'cluetip',
		width: 300, positionBy: 'mouse', waitImage: false, arrows: false,
		dropShadow: false, ajaxCache: false,
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	    },
	    delayedClose: cluetipCloseTimeout,
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled
	});

	// add link and entity's relation
	linkMap.put(key, nodeId);
	var arr = linkSrcMap.get(srcEntityId);
	arr[arr.length] = nodeId;
	arr = linkDestMap.get(destEntityId);
	arr[arr.length] = nodeId;
	return line;
}
function fixEntityClick() {
	var node = nodeElMap.get(linkSrcEntityId);
	if (node != null) {
		var params = 'entity.entityId=' + linkSrcEntityId +
			'&entity.fixed=true' +
			'&entity.folderId=' + topoFolderId;
		$.ajax({url: '../entity/updateEntityFixed.tv', type: 'POST', data: params,
		success: function() {
			$('#vertex' + linkSrcEntityId).draggable('disable');
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
	}
}
function fixFolderClick() {
	var node = nodeElMap.get(linkSrcEntityId);
	if (node != null) {
		var params = 'topoFolder.superiorId=' + superiorFolderId + '&topoFolder.folderId=' +
		linkSrcEntityId + '&topoFolder.fixed=1';
		$.ajax({url: '../topology/updateFolderFixed.tv', type: 'POST', data: params,
		success: function() {
			$('#vertex' + linkSrcEntityId).draggable('disable');
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
	}
}
function fixNodeLocation(nodeId, flag) {
	var el = nodeElMap.get(nodeId);
	if (el != null) {
		$('#vertex' + nodeId).draggable(flag ? 'disable' : 'enable');
	}
}
function requestMapNode() {
	var vorm = curDrawingShape;
	var param = 'mapNode.folderId=' + topoFolderId + '&mapNode.type=' + curDrawingType;
	if (curDrawingType < SHAPE_TYPE.POLY_LINE && curDrawingType >= SHAPE_TYPE.SOLID_LINE) {
		var arr = ('' + vorm.from).split(',');
		var arr1 = ('' + vorm.to).split(',');
		var x = parseInt(arr[0].replace('pt', ''));
		var y = parseInt(arr[1].replace('pt',''));
		var w = parseInt(arr1[0].replace('pt','')) - x;
		var h = parseInt(arr1[1].replace('pt','')) - y;
		param = param + '&mapNode.x=' + x + '&mapNode.y=' + y + '&mapNode.width=' + w + '&mapNode.height=' + h;
	} else if (curDrawingType == SHAPE_TYPE.POLY_LINE) {
		param =  param + '&mapNode.points=' + vorm.points;
	} else if (curDrawingType == SHAPE_TYPE.TEXT) {
		param =  param + '&mapNode.x=' +
			vorm.style.left.replace("px", "") + '&mapNode.y=' + vorm.style.top.replace("px", "");
	} else {
		var w = vorm.style.width.replace("px", "");
		var h = vorm.style.height.replace("px", "");
		if (parseInt(w) < 5 && parseInt(h) < 5) {
			zgraph.removeChild(vorm);
			return;
		}
		param =  param + '&mapNode.fillColor=' + SHAPE_STYLE.fillColor + '&mapNode.x=' +
			vorm.style.left.replace("px", "") + '&mapNode.y=' + vorm.style.top.replace("px", "") +
			'&mapNode.width=' + w + '&mapNode.height=' + h;

	}
	var successCallback = function(response){
		var json = Ext.util.JSON.decode(response.responseText);
		vorm.setAttribute('nodeId', json.nodeId);
		vorm.setAttribute('entityId', json.nodeId);
		vorm.setAttribute('objType', USEROBJ_TYPE.SHAPE);
		vorm.setAttribute('figureType', curDrawingType);
		if (curDrawingType > SHAPE_TYPE.POLY_LINE) {
			$('#' + vorm.id).draggable({opacity: 0.50, scroll: true, helper: 'clone', distance: 3,
				stop: function(e, ui) {
					if (ui.position.left < 0) {
						ui.position.left = 0;
					}
					if (ui.position.top < 0) {
						ui.position.top = 0;
					}
					this.style.left = ui.position.left;
					this.style.top = ui.position.top;
				},
				start: function(e, ui) {
					ui.helper.css("border", "0");
				}
			});
		}
	};
	var failureCallback = function(){zgraph.removeChild(vorm);};
	sendRequest('../topology/insertMapNode.tv', 'POST', param,
		successCallback, failureCallback);
}

function showEntityAlarmInfo(entityId) {
	var el = nodeElMap.get(entityId);
	if (el != null) {
		createDialog("modalDlg", I18N.COMMON.alarm + " - " + el.ip, 660, 450,
			"fault/getAlarmByEntityId.tv?entityId=" + entityId, null, true, true);
	}
}
function drawTopoNodeLink(linkType) {
	var srcEle = nodeElMap.get(linkSrcEntityId);
	var x = srcEle.offsetLeft + parseInt(srcEle.clientWidth/2);
	var y = srcEle.offsetTop + linkStyle.imgOffset+parseInt(srcEle.children[0].height/2);
	shapeNodeDrawing = false;
	document.onmousemove = moveShapeWhenDrawing;
	curDrawingShape = buildShapeNode(shapeIdSequence++, linkType, x, y);
	zgraph.appendChild(curDrawingShape);
}
function doEntityNodeMouseDown() {
	if (remoteMode) return;
	Ext.menu.MenuMgr.hideAll();
	var src = event.srcElement;
	var flag = (src.tagName == 'DIV');
	if (!flag) {
		src = src.parentElement;
	}
	if (curDrawingType <= CURSOR_TYPE.SQUARE) {
		renameNodeFinished();
		if(!flag) {
			if (event.ctrlKey) {
				if (src.className == 'ui-selected') {
					src.className = 'ui-unselecting';
				} else {
					src.className = 'ui-selected';
				}
			} else {
				if (event.button == MouseEvent.BUTTON1) {
					// is drawing link
					if (linkAdding && linkSrcEntityId != src.entityId) {
						linkAdding = false;
						zgraph.style.cursor = 'default';
						src.style.cursor = 'default';
						src.children[3].style.cursor = 'default';
						addNewLink(linkSrcEntityId, src.entityId);
						drawLinkCompleted();
					} else {
						disselectAllCell();
						src.className = 'ui-selected';
					}
				} else if (event.button == MouseEvent.BUTTON2 &&
					src.className.indexOf('ui-selected') == -1) {
					// right mouse button out of the div
					disselectAllCell();
					src.className = 'ui-selected';
				}
				// select relate's link
				//selectRelatedLink(src.entityId);
			}
		}
	}
	linkSrcEntityId = src.entityId;
	if (linkSrcEntityId == goToEntityId) {
		goToEntityId = 0;
		splashStarted = false;
	}
}
function selectRelatedLink(nodeId) {
	var ls = linkSrcMap.get(nodeId);
	if (ls != null) {
		for (var i = 0; i < ls.length; i++) {
			var l = linkElMap.get(ls[i]);
			if (l) {
				l.strokecolor = linkStyle.selectedColor;
			}
		}
	}
	ls = linkDestMap.get(nodeId);
	if (ls != null) {
		for (var i = 0; i < ls.length; i++) {
			var l = linkElMap.get(ls[i]);
			if (l) {
				l.strokecolor = linkStyle.selectedColor;
			}
		}
	}
}
function doEntityNodeMouseOver() {
	if (linkAdding) {
		var src = event.srcElement;
		while (src.tagName != 'DIV') {
			src = src.parentElement;
		}
		if (linkSrcEntityId != src.entityId) {
			src.style.cursor = 'crosshair';
			src.children[3].style.cursor = 'crosshair';
		}
	}
}
function doEntityNodeMouseOut() {
	if (linkAdding) {
		var src = event.srcElement;
		while (src.tagName != 'DIV') {
			src = src.parentElement;
		}
		if (src.style.cursor == 'crosshair') {
			src.style.cursor = 'default';
			src.children[3].style.cursor = 'default';
		}
	}
}
function doEntityNodeClick() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {
		return;
	}
	var el = event.srcElement;
	var pl = el.parentElement;
	if (el.tagName == 'SPAN' && focusRenameId == pl.entityId) {
		if (renamable) {
			renameNodeStarted(el);
		}
	} else if (el.id.indexOf('alert') == 0 && el.alert > 0) {
		showEntityAlarmInfo(el.parentElement.entityId);
	}
	focusRenameId = linkSrcEntityId;
}
function doEntityNodeDblClick() {
	if (remoteMode || curDrawingType > CURSOR_TYPE.SQUARE) {
		return;
	}
	var src = event.srcElement;
	if (src.tagName == 'DIV') {
		return;
	} else {
		src = src.parentElement;
		var url = src.url;
		if (url != null && url != '') {
			Ext.menu.MenuMgr.hideAll();
			addView("url" + src.nodeId, 'URL',  "topoLeafIcon", url);
		} else {
			showEntitySnap(src.entityId, src.ip);
		}
	}
}
function renameNodeStarted(srcElement) {
	if (renaming == true || event.srcElement.tagName == 'TEXTAREA') {return;}
	renameNodeFinished();
	renameNodeId = srcElement.id;
	renaming = true;
	document.onselectstart = returnTrue;
	var box = Zeta$('renameBox');
	box.style.width = srcElement.clientWidth;
	box.style.height = srcElement.clientHeight;
	box.value = srcElement.innerText;
	srcElement.swapNode(box);
	var r = box.createTextRange();
	r.select();
	r.moveStart('character', box.value.length);
	r.collapse(true);
}
function renameNodeFinished() {
	if (renaming == false || event.srcElement.tagName == 'TEXTAREA') {return;}
	renaming = false;
	document.onselectstart = returnFalse;
	var box = Zeta$('renameBox');
	var span = Zeta$(renameNodeId);
	var oldName = span.innerText;
	var newName = box.value.trim();
	if (newName != oldName) {
		span.innerText = box.value;
		var entityId = box.parentNode.entityId;
		if (entityId < 10000000000) {
			sendRequest('../topology/renameTopoFolder.tv', 'POST',
				{superiorId: superiorFolderId, folderId: entityId, oldName: oldName, name: newName},
				function() {
					var frame = getMenuFrame();
					try {
						frame.renameTopoFolder(entityId, newName);
					} catch (err) {
					}
				},
				defaultFailureCallback);
		} else if (entityId < 30000000000) {
			var param = 'mapNode.nodeId=' + entityId+ '&mapNode.text=' + newName + '&mapNode.folderId=' + topoFolderId;
				sendRequest('../topology/setMapNodeText.tv', 'POST', param,
					defaultSuccessCallback, defaultFailureCallback);
		} else {
			sendRequest('../entity/renameEntity.tv', 'POST',
				{entityId: entityId, name: newName, folderId: topoFolderId},
				function() {
					nodeElMap.get(entityId).children[0].cluetip = newName;
				}, defaultFailureCallback);
		}
	}
	box.swapNode(span);
}
function insertPicture() {
	window.top.createWindow('imageChooser', I18N.COMMON.selectPicture, 600, 400,
		'include/showImageChooser.tv', null,
		true, true, insertPictureCallback);
}
function insertPictureCallback() {
	var callback = getZetaCallback();
	if (callback != null && callback.type == 'image') {
		$.ajax({url: 'insertMapNode.tv', type: 'POST',
			data: {'mapNode.folderId': topoFolderId, 'mapNode.type': 25, 'mapNode.x': 50, 'mapNode.y': 50,
				   'mapNode.text': 'picture', 'mapNode.icon': callback.path},
			success: function(json) {
				var div = createPicture(json);
				div.className = 'ui-gotoed';
			}, error: function() {
				showErrorDlg();
			}, dataType: 'json', cache: false});
	}
}
function insertSubnet() {
	var param = 'topoFolder.superiorId=' + topoFolderId + '&topoFolder.x=' + event.offsetX + '&topoFolder.y=' + event.offsetY;
	var scCallback = function(response) {
		var json = Ext.util.JSON.decode(response.responseText);
		createTopoFolder(json);
		try {
			getMenuFrame().doRefresh();
		} catch (err) {
		}
	};
	sendRequest('../topology/addSubnet.tv', 'POST', param, scCallback, defaultFailureCallback);
}
function insertHrefFolder() {
	var param = 'topoFolder.superiorId=' + topoFolderId + '&topoFolder.x=' + event.offsetX + '&topoFolder.y=' + event.offsetY;
	var scCallback = function(response) {
		var json = Ext.util.JSON.decode(response.responseText);
		createTopoFolder(json);
		try {
			getMenuFrame().doRefresh();
		} catch (err) {
		}
	};
	sendRequest('../topology/addHrefFolder.tv', 'POST', param, scCallback, defaultFailureCallback);
}
function insertCloudy() {
	var param = 'topoFolder.superiorId=' + topoFolderId + '&topoFolder.x=' + event.offsetX + '&topoFolder.y=' + event.offsetY;
	var scCallback = function(response) {
		var json = Ext.util.JSON.decode(response.responseText);
		createTopoFolder(json);
		try {
			getMenuFrame().doRefresh();
		} catch (err) {
		}
	};
	sendRequest('../topology/addCloudy.tv', 'POST', param, scCallback, defaultFailureCallback);
}
function insertText() {
	shapeIdSequence++;
	curDrawingShape = createTextBox(shapeIdSequence++, event.offsetX, event.offsetY);
	zgraph.appendChild(curDrawingShape);
	requestMapNode();
}
function doFolderNodeDblClick() {
	if (remoteMode || curDrawingType > CURSOR_TYPE.SQUARE) {
		return;
	}
	var src = event.srcElement;
	while (src.tagName != 'DIV') {
		src = src.parentElement;
	}
	if (src.folderType == 7) {
		addView("topo" + src.entityId, src.entityName,
			"topoLeafIcon", (src.url == null || src.url == '') ? 'include/blank.jsp' : src.url);
	} else {
		addView("topo" + src.entityId, src.entityName,
			"topoLeafIcon", "topology/getTopoMapByFolderId.tv?folderId=" + src.entityId);
	}
}
function doPicNodeMouseDown() {
	if (curDrawingType > 1) {return;}
	var src = event.srcElement;
	if(src.tagName == 'DIV') {
		disselectAllCell();
	} else {
		while (src.tagName != 'DIV') {
			src = src.parentElement;
		}
		if (event.ctrlKey) {
			if (src.className == 'ui-selected') {src.className = 'ui-unselecting';}
			else {src.className = 'ui-selected';}
		} else {
			if (event.button == MouseEvent.BUTTON1) {
				disselectAllCell();
			} else if (event.button == MouseEvent.BUTTON2 && src.className.indexOf('ui-selected') == -1) {
				disselectAllCell();
			}
			src.className = 'ui-selected';
		}
	}
}
function onTextPicClick() {
	var el = nodeElMap.get(focusedShapeId);
	if (el) {
		showInputDlg(I18N.COMMON.inputTitle, I18N.COMMON.newName, function(type, text) {
			var match = text.trim();
			if (type == 'cancel' || match=='') {return;}
			var successCallback = function() {
				el.children[3].innerText = match;
			};
			sendRequest('../topology/setMapNodeText.tv', 'POST',
				'mapNode.nodeId=' + el.nodeId+'&mapNode.text=' + match,
				successCallback, defaultFailureCallback);
		});
	}
}
function onSetTextClick() {
	setTextBoxText(focusedShapeId);
}

function deleteLinkByEntity(entityId) {
	var edge = null;
	var arr = linkSrcMap.get(entityId);
	for (var m = arr.length - 1; m >= 0; m--) {
		edge = linkElMap.get(arr[m]);
		if (edge != null) {
			zgraph.removeChild(edge);
			linkElMap.remove(arr[m]);
		}
	}
	arr = linkDestMap.get(entityId);
	for (var n = arr.length - 1; n >= 0; n--) {
		edge = linkElMap.get(arr[n]);
		if (edge != null) {
			zgraph.removeChild(edge);
			linkElMap.remove(arr[n]);
		}
	}
	linkSrcMap.remove(entityId);
	linkDestMap.remove(entityId);
}
function addNewLink(srcId, dstId) {
	var key = 'k' + srcId + '_' + dstId;
	if(linkMap.containsKey(key)) {return;}
	var name = nodeElMap.get(srcId).ip + ' - ' + nodeElMap.get(dstId).ip;
	$.ajax({url:'../link/insertNewLink.tv', data: {'srcEntityId': srcId, 'destEntityId': dstId,
		folderId: topoFolderId, name: name},
		dataType:'json', cache:'false',
		success: function(json) {
			createLink(json);
		},
		error: function(){showMessageDlg(I18N.NETWORK.error,I18N.NETWORK.addLinkFailure, 'error')}
	});
}

function zoomMap(size) {
	if(zoom.size == size) return;
   	if (zoomByAnimate) {
		currentZoomSize = zoom.size;
   		currentZoomTarget = size;
   		currentZoomInc = (currentZoomTarget - currentZoomSize)/2;
   		animateZoom();
   	} else {
   		zoomGraph(size);
   	}
   	window.top.setMapInfo(Math.round(size*100) + '%');
   	if (editTopoPower) {
		sendRequest('../topology/setTopoMapZoom.tv', 'POST',
			'topoFolder.folderId=' + topoFolderId + '&topoFolder.zoom=' + size,
			function(){}, function(){});
	}
}

function synRenameNode(id, name) {
	var el = nodeElMap.get(id);
	if (el != null) {
		el.children[3].innerText = name;
	}
}

function synDeleteNode(id) {
	var el = nodeElMap.get(id);
	if (el != null) {
		zgraph.removeChild(el);
		deleteLinkByEntity(id);
	}
}

function synUpdateFolderHref(id, url) {
	var el = nodeElMap.get(id);
	if (el != null) {
		el.url = url;
	}
}

function synUpdateNodeIcon(id, icon, w, h) {
	var n = nodeElMap.get(id);
	if (n != null) {
		var img = n.children[0];
		img.width = w;
		img.height = h;
		img.src = icon;
		changeLinkByEntity(id, n.offsetLeft + parseInt(n.clientWidth / 2),
			n.offsetTop + parseInt(img.clientHeight / 2) + linkStyle.imgOffset);
	}
}

function synUpdateLinkWidth(w) {
	linkElMap.eachValue(function(v) {
		v.strokeweight = w;
	});
}

function synUpdateLinkName(id, name) {
	var link = linkElMap.get(id);
	if (link != null) {
		link.cluetip = name;
	}
}

/**
 * default manual synchronize after clear alert.
 */
function synUpdateEntityState(id, level, levelName, msg) {
	var el = nodeElMap.get(id);
	if (el != null) {
		el.alert = level;
		if (level == 0) {
			el.children[1].title = '';
			el.children[1].src = '../images/fault/level0.gif';
		} else {
			el.children[1].title = (levelName + '\n' + msg);
			el.children[1].src = '../images/fault/level' + level + '.gif';
		}
	}
}

/**
 * auto synchronize after receive alert.
 */
function autoSynUpdateEntityState(id, level, levelName, msg) {
	var el = nodeElMap.get(id);
	if (el != null) {
		if (level == 0) {
			el.alert = 0;
			el.children[1].title = '';
			el.children[1].src = '../images/fault/level0.gif';
		} else if (level >= el.alert) {
			el.alert = level;
			el.children[1].title = (levelName + '\n' + msg);
			el.children[1].src = '../images/fault/level' + level + '.gif';
		}
	}
}

function doOnResize() {
	if (zgraph == null) {
		return;
	}
	if (drawingDlg != null && drawingDlg.isVisible()) {
		if (drawingDlg.getPosition()[0] > 300) {
			var y = parseInt((document.body.clientHeight - drawingDlg.height) / 2);
			y = y < 40 ? 40 : y;
			drawingDlg.setPosition(document.body.clientWidth - drawingDlg.width - 40, y);
		}
	}
	if (templateDlg != null && templateDlg.isVisible()) {
		if (templateDlg.getPosition()[0] > 300) {
			var y = parseInt((document.body.clientHeight - templateDlg.height) / 2);
			y = y < 40 ? 40 : y;
			templateDlg.setPosition(document.body.clientWidth - templateDlg.width - 40, y);
		}
	}
	if (!remoteMode) {
		zgraph.style.height = (document.body.clientHeight - jbGraph.getHeight()) + 'px';
	}
	if (isSubnetMap) {
		relayoutSubnet();
	}
}

function selectFillColor() {
	createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, setVmlFillColor);
}
function setVmlFillColor() {
	if (getZetaCallback() != null && getZetaCallback().type == 'colorpicker') {
		Zeta$('fillColorContainer').style.backgroundColor = SHAPE_STYLE.fillColor = getZetaCallback().color;
	}
}
function setTextBoxText(id) {
	if (id) {
		var el = nodeElMap.get(id);
		if (el == null) {
			return;
		}
		//renameNodeStarted(el);
		showInputDlg(I18N.COMMON.inputTitle, I18N.NETWORK.inputLabelMsg, function(type, text) {
			var match = text.trim();
			if (type == 'cancel' || match=='') {return;}
			var successCallback = function() {
				el.innerText = match;
			};
			sendRequest('../topology/setMapNodeText.tv', 'POST',
				'mapNode.nodeId=' + el.nodeId + '&mapNode.text=' + match,
				successCallback, defaultFailureCallback);
		});
	}
}
function setShapeNodeText(id) {
	if (id && renamable) {
		var el = nodeElMap.get(id);
		if (el == null) {
			return;
		}
		renameNodeStarted(el.children[1]);
	}
}
function resizeVmlNode(id) {
	if (id) {
		var e = nodeElMap.get(id);
		var text = e.style.width.replace('px', '') + "," + e.style.height.replace('px', '');
		showInputDlg(I18N.COMMON.inputTitle, I18N.NETWORK.inputVmlResizeMsg, function(type, text) {
			var match = text.trim();
			if (type == 'cancel' || match=='') {return;}
			var arr = match.split(',');
			if (arr.length == 2) {
				if (isNaN(arr[0]) || isNaN(arr[1])) {
					showMessageDlg(I18N.COMMON.error, I18N.COMMON.parameterIllegal, 'error');
				} else {
					var successCallback = function() {
						e.style.width = arr[0] + 'px';
						e.style.height = arr[1] + 'px';
					};
					sendRequest('../topology/setMapNodeSize.tv', 'POST',
						'mapNode.nodeId='+e.nodeId+'&mapNode.width='+arr[0]+'&mapNode.height='+arr[1],
						successCallback, defaultFailureCallback);
				}
			} else {
				showMessageDlg(I18N.COMMON.error, I18N.COMMON.parameterIllegal, 'error');
			}
		}, text, null, false);
	}
}

function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
	/*window.parent.createDialog("googleDlg", I18N.COMMON.addToGoogle, 600, 450, "google/add2GoogleMap.tv", null, true, true,
           function(){
                if (window.top.ZetaCallback.type != 'ok') {return;}
	            Ext.Ajax.request({url: '../google/saveEntity2GoogleMap.tv',
	                params: {entityId : entityId, latitude : window.top.ZetaCallback.lat,
	                	longitude : window.top.ZetaCallback.lng, zoom : window.top.ZetaCallback.zoom},
	                success: function() {},
	                failure: function(){window.parent.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.add2GoogleMapError, 'error');}
	            })
            	window.parent.ZetaCallback = null;
    });*/
}
function locateEntity(entityIp) {
	window.parent.locateEntityByIp(entityIp);
}
function setEntityInterval(entityId) {
	createDialog('modalDlg', I18N.NETWORK.pollingInterval, 420, 250,
		'entity/showPollingInterval.tv?entityId=' + entityId, null, true, true);
}
function showPortFlowInfo(entityId, entityIp) {
	addView('interfaces-' + entityId,
		I18N.NETWORK.portRealFlow + ' - ' + entityIp,
		'entityTabIcon', 'realtime/showPortFlowInfo.tv?entityId=' + entityId +
		'&ip=' + entityIp);
}
function showPortRealState(entityId, entityIp) {
	window.open('../realtime/showPortFlowInfo.tv?entityId=' + entityId + '&ip=' + entityIp, 'portreal' + entityId);
	/*addView('portreal' + entityId,
		I18N.NETWORK.portRealFlow + '[' + entityIp + ']',
		'flowTabIcon', 'realtime/showPortFlowInfo.tv?entityId=' + entityId +
		'&ip=' + entityIp);*/
}
function showCpuAndMem(entityId, entityIp) {
	window.open('../realtime/viewCpuMemInfo.tv?entityId=' + entityId + '&ip=' + entityIp, 'cpumem' + entityId);
	/*addView('cpumem' + entityId, I18N.NETWORK.cpuMemInfo + '[' + entityIp + ']',
		'flowTabIcon', 'realtime/viewCpuMemInfo.tv?entityId=' + entityId + '&ip=' + entityIp);*/
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
	window.open('../network/showTelnet.tv?entityId=' + entityId + '&ip=' + entityIp, 'telnet' + entityId);
	//createDialog("modalDlg", 'Telnet ' + entityIp, 600, 400,
		//"entity/runCmd.tv?cmd=telnet&ip=" + entityIp, null, true, true);
}
function showMibBrowser(entityId, entityIp) {
	createDialog("modalDlg", I18N.NETWORK.physicalPane + " - " + entityIp, 660, 450,
		"entity/getPhysicalPane.tv?ping=" + entityId, null, true, true);
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

/* change drawing type, cursor will be changed. */
function vmlFocusedCallback() {
	$('#zetaGraph').selectable('disable');
}
function vmlBluredCallback() {
	$('#zetaGraph').selectable('enable');
}

function setDrawingType(type) {
	if (curDrawingType == type) {
		return;
	}
	Zeta$('drawing' + curDrawingType).className = "DRAWING-TOOL";
	Zeta$('drawing' + type).className = "DRAWING-TOOL-SELECTED";
	setCursorType(type);
	if (type <= CURSOR_TYPE.MOVE) {
		try {
			window.parent.setCoordInfo('');
		} catch (err) {
		}
	}
}
function doDrawingToolOver(obj, type) {
	if (curDrawingType == type) {
		obj.className = "DRAWING-TOOL-SELECTED-OVER";
	} else {
		obj.className = "DRAWING-TOOL-OVER";
	}
}
function doDrawingToolPressed(obj, type) {
	obj.className = "DRAWING-TOOL-PRESSED";
}
function doDrawingToolSelected(obj) {
	obj.className = 'DRAWING-TOOL-SELECTED';
}
function doDrawingToolOut(obj, type) {
	if (curDrawingType == type) {
		obj.className = "DRAWING-TOOL-SELECTED";
	} else {
		obj.className = "DRAWING-TOOL";
	}
}

/* common function define */
function defaultFailureCallback(response) {
	showMessageDlg(I18N.COMMON.error, I18N.COMMON.operationFailure, 'error');
}
function defaultSuccessCallback(response) {
}
function defaultCallback() {
}
function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url, failure: fn, success: sn, params: param});
}

/* timer and timeout */
function dispatcherHandler(json) {
	var len = 0;
	var el = null;
	var obj = null;
	var arr = json.alerts;
	if (arr != null) {
		len = arr.length;
		for (var i = 0; i < len; i++) {
			obj = arr[i];
			el = nodeElMap.get(obj.entityId);
			if (el != null) {
				el.children[0].style.backgroundColor = obj.backgroundColor;
				/*if (obj.online) {
					if (!el.online) {
						el.children[0].style.filter = '';
					}
				} else {
					if (el.online) {
						el.children[0].style.filter = 'gray';
					}
				}*/
				el.online = obj.online;
				if (el.alert != obj.alert) {
					el.alert = obj.alert;
					el.children[1].title = obj.alt;
					el.children[1].src = obj.icon;
				}
			}
		}
	}
	arr = json.folders;
	if (arr != null) {
		len = arr.length;
		for (var i = 0; i < len; i++) {
			obj = arr[i];
			el = nodeElMap.get(obj.folderId);
			if (el != null && el.alert != obj.alert) {
				el.alert = obj.alert;
				el.children[1].title = obj.alt;
				el.children[1].src = obj.icon;
			}
		}
	}
	arr = json.flows;
	if (arr != null) {
		var span = null;
		len = arr.length;
		for (var i = 0; i < len; i++) {
			obj = arr[i];
			el = linkElMap.get(obj.linkId);
			if (el != null) {
				span = linkLabelElMap.get(obj.linkId);
				span.linkLabel = obj.linklabel;
				if (displayLinkLabel) {
					span.innerText = obj.linklabel;
				}
				if (obj.strokeColor) {
					if (el.strokecolor != obj.strokeColor) {
						el.strokecolor = el.oldColor = obj.strokeColor;
					}
					span.style.color = obj.strokeColor;
				} else {
					if (el.strokecolor != linkStyle.color) {
						el.strokecolor = el.oldColor = linkStyle.color;
					}
					span.style.color = linkStyle.color;
				}
			}
		}
	}
}

function restoreTopologyState() {
 	$.ajax({url: '../topology/getTopologyStateFirstly.tv', dataType: 'json', cache: false,
 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
    	success: dispatcherHandler,
    	error: defaultCallback
    });
}
function dispatcherCallback() {
 	$.ajax({url: '../topology/getTopologyStateNewly.tv', dataType: 'json', cache: false,
 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
    	success: dispatcherHandler,
    	error: defaultCallback
    });
}

function setMapRefreshInterval(i, folderId) {
	if (folderId == topoFolderId) {
		doOnunload();
		dispatcherInterval = i;
		startTimer();
	}
}

function startTimer() {
	if (dispatcherTimer == null) {
		dispatcherTimer = setInterval('dispatcherCallback()', dispatcherInterval);
	}
}

function doOnunload() {
	if (dispatcherTimer != null) {
		clearInterval(dispatcherTimer);
		dispatcherTimer = null;
	}
	destroyGraph();
}

function doOnKeyDown() {
    var code = event.keyCode;
    var ctrl = event.ctrlKey;
    if (ctrl){
		if(code == KeyEvent.VK_A) {selectAllCell();}
		else if(code == KeyEvent.VK_P){}
		else if(code == KeyEvent.VK_X){}
		else if(code == KeyEvent.VK_C){}
		else if(code == KeyEvent.VK_V){}
		return;
	}
	if (code == KeyEvent.VK_DEL){onDeleteClick();}
	else if (code == KeyEvent.VK_UP) {moveEntityUpOrDown(-1);}
	else if(code == KeyEvent.VK_DOWN) {moveEntityUpOrDown(1);}
	else if (code == KeyEvent.VK_LEFT) {moveEntityLeftOrRight(-1);}
	else if (code == KeyEvent.VK_RIGHT) {moveEntityLeftOrRight(1);}
	return false;
}

function getZetaClipboard() {
	return window.top.ZetaClipboard;
}
function setZetaClipboard(v) {
	window.top.ZetaClipboard = v;
}
function getZetaCallback() {
	return window.top.ZetaCallback;
}
function showWaitingDlg(msg, icon) {
	window.top.showWaitingDlg(null, msg, icon);
}
function closeWaitingDlg(title) {
	window.top.closeWaitingDlg(title);
}
function showErrorDlg() {
	window.top.showErrorDlg();
}
function showMessageDlg(title, msg, type) {
	window.top.showMessageDlg(title, msg, type);
}
function showConfirmDlg(title, msg, callback) {
	window.top.showConfirmDlg(title, msg, callback);
}
function showInputDlg(title, msg, callback, text, scope, multiline) {
	window.top.showInputDlg(title, msg, callback, text, scope, multiline);
}
function showTextAreaDlg(title, msg, callback) {
	window.top.showTextAreaDlg(title, msg, callback);
}
function createWindow(id, title, width, height, url, icon, modal, closeHandler, resized) {
	return window.top.createWindow(id, title, width, height, url, icon, modal, closeHandler, resized);
}
function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
	window.top.createDialog(id, title, width, height, url, icon, modal, closable, closeHandler);
}
function closeWindow(id) {
	window.top.closeWindow(id);
}
function setWindowVisible(id, visible) {
	window.top.setWindowVisible(id, visible);
}
function setWindowTitle(id, title) {
	window.top.setWindowTitle(id, title);
}
function getWindow(id) {
	window.top.getWindow(id);
}
function addView(id, title, icon, url, history) {
	window.top.addView(id, title, icon, url, history);
}
function setActiveTab(id) {
	window.top.setActiveTab(id);
}
function setActiveTitle(id, title) {
	window.top.setActiveTitle(id, title);
}
function getActiveFrameById() {
	return window.top.getActiveFrameById();
}
function getActiveFrame() {
	return window.top.getActiveFrame();
}
function getFrameById(frameId) {
	return window.top.getFrameById(frameId);
}
function getFrame(frameId) {
	return window.top.getFrame(frameId);
}
function getPropertyFrame() {
	return window.top.getPropertyFrame();
}
function getNavigationFrame() {
	return window.top.getNavigationFrame();
}
function getMenuFrame() {
	return window.top.getNavigationFrame();
}
function showProperty(url, title, expanded) {
	window.top.showProperty(url, title, expanded);
}