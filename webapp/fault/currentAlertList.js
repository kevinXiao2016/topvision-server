var store = null;
var grid = null;
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(grid.getEl().dom, wnd.document, true);
	/*var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}*/
}
//打印预览;
function onPrintClickView() {
	var wnd = window.open();
	showPrintWnd(grid.getEl().dom, wnd.document);
}
function showPrintWnd(obj, doc, isPrint) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
	doc.write('<html><head>');
	doc.write('<title></title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/alertPrint.css"/>');
	Zeta$NoHeaderAndFooter(doc);
	doc.write('</head>');
	doc.write('<body class="edge10"><div class="center">');
	doc.write(obj.innerHTML);
	if(isPrint === true){
		doc.write('</div><script type="text/javascript">window.print()<\/script></body>');
	}else{
		doc.write('</div></body>');
	}
	doc.write('</html>');
	doc.close();
}
function doRefresh() {
	store.reload();
}
function onRefreshClick() {
	var level = $('#alertLevel').val();
	var typeId = $("#putSel").data("value");
	var hostDevice = $('#hostDevice').val();
	if(userAlert){
		store.load({params: {level:level,typeIdList:typeId,hostDevice:hostDevice,start: 0, limit: pageSize}});
	}else{
		store.load({params: {level:level,typeId:typeId,hostDevice:hostDevice,start: 0, limit: pageSize}});
	}
	
}
function renderLevelName(value, p, record) {
	return record.data.levelName;
}
function renderLevel(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{1}">{2}',
		record.data.level, record.data.levelName, record.data.message);
}
function renderNote(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle class="nm3kTip" nm3kTip="{1}">{2}',
		record.data.level, value, value);
}
function renderStatus(value, p, record) {
	var tip = '<p>@resources/FAULT.confirmStatus@@COMMON.maohao@@resources/FAULT.unconfirmed@</p>', imgUrl = '../images/fault/unconfirm.png';
	if(record.data.status == '1'){
		//已确认
		imgUrl = '../images/fault/confirm.png';
		tip = '<p>@resources/FAULT.confirmStatus@@COMMON.maohao@@resources/FAULT.confirmed@</p>';
		tip += '<p><br>@resources/FAULT.checkCustomer@@COMMON.maohao@'+record.data.confirmUser+'</p>';
		tip += '<p><br>@resources/FAULT.checkMessage@@COMMON.maohao@'+record.data.confirmMsg+'</p>';
		tip += '<p><br>@resources/FAULT.checkTime@@COMMON.maohao@'+record.data.confirmTime+'</p>';
	}
	return String.format('<img hspace=5 src="{0}" border=0 align="absmiddle" class="nm3kTip" nm3kTip="{1}">', imgUrl, tip);
}

function renderExportConfirmStatus(value) {
	if(value == '1'){
		return '@resources/FAULT.confirmed@';
	} else {
		return '@resources/FAULT.unconfirmed@';
	}
}

function renderClearStatus(value, p, record){
	var tip = '<p>@resources/FAULT.clearStatus@@COMMON.maohao@@resources/FAULT.unclear@</p>', imgUrl = '../images/fault/unconfirm.png';
	if (record.data.status == '2' || record.data.status == '3') {
		//已清除
		imgUrl = '../images/fault/confirm.png';
		tip = '<p><br>@resources/FAULT.clearStatus@@COMMON.maohao@@resources/FAULT.cleared@</p>';
		tip += '<p><br>@resources/FAULT.clearCustomer@@COMMON.maohao@'+record.data.clearUser+'</p>';
		tip += '<p><br>@resources/FAULT.clearMessage@@COMMON.maohao@'+record.data.clearMsg+'</p>';
		tip += '<p><br>@resources/FAULT.clearTime@@COMMON.maohao@'+record.data.clearTime+'</p>';
	}
	return String.format('<img hspace=5 src="{0}" border=0 align="absmiddle" class="nm3kTip" nm3kTip="{1}">', imgUrl, tip);
}

function renderExportClearStatus(value) {
	if(value == '2' || value == '3'){
		return '@resources/FAULT.cleared@';
	} else {
		return '@resources/FAULT.unclear@';
	}
}

function renderOperation(value, p, record){
	var str = '<a class="yellowLink" href="javascript:;" onclick="showAlertDetail()" >@resources/COMMON.look@</a>';
	if(record.data.status != '1'){
		str += ' / <a class="yellowLink" href="javascript:;" onclick="onConfirmAlarmClick()" >@resources/COMMON.confirm@</a>'
	}
	if(record.data.status != '2' && record.data.status != '3'){
		str += ' / <a class="yellowLink" href="javascript:;" onclick="onClearAlarmClick()" >@resources/COMMON.clear@</a>'
	}
	return str;
}

function getLastAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false);
		} else {
			model.selectLastRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}
function getNextAlertId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasNext()) {
			model.selectNext(false);
		} else {
			model.selectFirstRow(false);
		}
		var record = model.getSelected();
		return record.data.alertId;
	}
	return 0;
}

function onClearAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 2 || selections[i].data.status == 3){
				 window.parent.showMessageDlg("@COMMON.tip@", "@fault/ALERT.alertCleared@");
				 return;
			}
		}
		window.parent.showTextAreaDlg("@COMMON.clear@", "@resources/EVENT.clearMessage@<br />@COMMON.remarks@", function (type, text) {
			if (type == "cancel") {
				return;
			}
			if( !V.isRemarks(text) ){
				window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
				return;
			}
			var selections = sm.getSelections();
			var alertIds = [];
			for (var i = 0; i < selections.length; i++) {
				alertIds[i] = selections[i].data.alertId;
			}
			Ext.Ajax.request({url:"../fault/clearAlert.tv", method: "post",
			success:function (response) {
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.clearSuccess@");
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@ALERT.clearSuccess@</b>'
	   			});
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message: text}});
		});
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	/* 已经确认的告警可以重新确认  EMS-6754
	for (var i = 0; i < selections.length; i++) {
		 if(selections[i].data.status == 1){
			 window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.alertIsConfirmed@");
			 return;
		 }
	}
	*/
	if (sm != null && sm.hasSelection()) {
		for(var i = 0; i < selections.length; i++){
			if(selections[i].data.status == 1){
				 window.parent.showMessageDlg("@COMMON.tip@", "@fault/ALERT.alertConfirmed@");
				 return;
			}
		}
		window.parent.showTextAreaDlg("@COMMON.confirm@", "@resources/EVENT.confirmMessage@<br />@COMMON.remarks@", function (type, text) {
		if (type == "cancel") {
			return;
		}
		if( !V.isRemarks(text) ){
			window.parent.showMessageDlg("@COMMON.tip@", "@resources/alert.confirmAlertMessage2@");
			return;
		}
		var selections = sm.getSelections();
		var len = selections.length;
		var alertIds = [];
		for (var i = 0; i < len; i++) {
			alertIds[i] = selections[i].data.alertId;
		}
		Ext.Ajax.request({url:"../fault/confirmAlert.tv", method: "post",
			success:function (response) {
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.confirmSuccess@");
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@ALERT.confirmSuccess@</b>'
	   			});
				onRefreshClick();
			}, failure:function () {
			}, params:{alertIds: alertIds, message: text}});
	});
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}
var eventContextMenu = null;
//var eventFindEntityItem = null;
var eventFindDesktopItem = null;
var eventMoreItem = null;
var eventConfirmItem = null;
var eventClearItem = null;
var eventPropertyItem = null;

function goToEntity(folderId, name, entityId) {
	var id = "topo-" + folderId;
	var tab = window.parent.contentPanel.getItem(id);
	if (tab) {
		window.parent.contentPanel.setActiveTab(id);
		var frame = window.parent.getFrame(id);
		frame.goToEntity(entityId);
	} else {
		window.parent.addView(id, name, "topo-tabicon", String.format("topology/getTopoMapByFolderId.tv?folderId={0}&entityId={1}", folderId, entityId));
	}
}
function goToEntityCallback(response) {
	if (response.responseText == "") {
		return;
	}
	var json = Ext.util.JSON.decode(response.responseText);
	if (json.folder != null && json.folder.length > 0) {
		if (window.parent.isTabbed) {
			for (var i = 0; i < json.folder.length; i++) {
				goToEntity(json.folder[i].folderId, json.folder[i].name, json.folder[i].entityId);
				break;
			}
		} else {
			window.parent.addView(json.folder[0].folderId, json.folder[0].name, "topo-tabicon",
				String.format("topology/getTopoMapByFolderId.tv?folderId={0}&entityId={1}", json.folder[0].folderId, json.folder[0].entityId));
		}
	} else {
		showMessageDlg("@COMMON.tip@", "@resources/MAIN.notfoundInFolder@");
	}
}
function onFindEntityEventClick() {
	Ext.menu.MenuMgr.hideAll();
	var sm = grid.getSelectionModel();
	var r = sm.getSelected();
	Ext.Ajax.request({url:"../network/findEntity.tv", method:"post",
		success: goToEntityCallback,
		failure:function () {
		}, params: {ip:r.data.host}});	
}
function onPropertyEventClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		var r = sm.getSelected();
		window.top.createDialog("modalDlg", "@resources/FAULT.alertProperty@", 540, 400, "alert/showAlertDetail.tv?alertId=" + r.data.alertId, null, true, true);	
	}
}
var queryVisible = false;
function onShowQueryClick() {
	queryVisible = !queryVisible;
	Zeta$('query-div').style.display = queryVisible ? '' : 'none';
}

function buildPageBox(page) {
	return String.format("@COMMON.displayPerPage@", page);
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);	
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
}

var curFlag = true;
function showHistoryAlert() {
	var t = window.parent.contentPanel.getActiveTab();
	t.alertType = 'his';
	var url = '';
	//在前面告警和历史告警之间切换时，保持查询参数不变
	var level =  $('#alertLevel').val();
	var typeId = $("#putSel").data("value");;
	var hostDevice = $("#hostDevice").val();
	var startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	var endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	url = 'showHistoryAlertList.tv?level=' + level + '&typeId=' + typeId
		+ "&hostDevice=" + hostDevice + "&startTime=" + startTime
		+ "&endTime=" + endTime;
	if (userAlert) {
		url = 'showHistoryAlertList.tv?level=' + level + '&typeId=0'
				+ "&hostDevice=" + hostDevice + "&startTime=" + startTime
				+ "&endTime=" + endTime + "&userAlert=true";
	}
	location.href = url;
}
function queryByLevel(level) {
	curFlag = true;
	curLevel = level;
	curType = 0;
	store.setBaseParam('level', curLevel);
	store.setBaseParam('typeId', curType);
	store.load({params: {start: 0, limit: pageSize}});
}
function queryByAlertType(type) {
	curFlag = false;
	curType = type;
	curLevel = 0;
	store.setBaseParam('level', curLevel);
	store.setBaseParam('typeId', curType);
	store.load({params: {start: 0, limit: pageSize}});
}
function showAlertDetail(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selection = sm.getSelected();
		window.parent.createDialog("modalDlg", "@resources/FAULT.alertProperty@", 800, 500,
				"alert/showAlertDetail.tv?alertId=" + selection.data.alertId, null, true, true);
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}

function viewCmtsSnap(cmcId, manageIp) {
	var recNum = grid.getStore().find("entityId",cmcId);//grid.getSelectionModel().getSelected()
	var record = grid.getStore().getAt(recNum);
    window.parent.addView('entity-' + cmcId, record.data.host.split("[")[1].split("]")[0], 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({
		id: 'extPagingBar', 
		pageSize: pageSize, 
		store:store, 
		displayInfo:true, 
		items:["-",  
		    buildPageBox(pageSize),
		    '-',{
				text: "@resources/COMMON.printPreview@", 
				iconCls: "bmenu_find", 
				menu: [{
					text : '@resources/COMMON.printPreviewCurrentPage@',
					handler : function(){
						printFn(false);
					}
				},{
					text : '@resources/COMMON.printPreviewAllPage@',
					handler : function(){
						printFn(false, "all");
					}
				}]
			}, {
				text: "@COMMON.print@", 
				iconCls: 'bmenu_print', 
				menu: [{
					text : '@resources/COMMON.printCurrentPage@',
					handler : function(){
						printFn(true);
					}
				},{
					text : '@resources/COMMON.printAllPage@',
					handler : function(){
						printFn(true, "all");
					}
				}]
			}, {
				text: "@COMMON.exportExcel@", 
				iconCls: 'bmenu_exportWithSub',
				handler: function() {
					top.ExcelUtil.exportGridToExcel(grid, '@ALERT.currentAlertInfoList@');
				}
			},
			'-',{
				text: "@resources/FAULT.alertProperty@", 
				iconCls:'bmenu_page', 
				handler: showAlertDetail
			}
		]
	});
	return pagingToolbar;
}

function manuRenderer(v,m,r){
	if(r.data.status == 1){
		return String.format("<a href='javascript:;' onClick='doViewOperation({0})'>@COMMON.view@</a> / <a href='javascript:;' onClick='doClearOperation();'>@COMMON.clear@</a>",r.id);
	}else{
		return String.format("<a href='javascript:;' onClick='doViewOperation({0})'>@COMMON.view@</a> / <a href='javascript:;' onClick='doClearOperation();'>@COMMON.clear@</a> / <a href='javascript:;' onClick = 'doConfirmOperation({0})'>@COMMON.confirm@</a>",r.id);
	}
}
function doViewOperation(alertId){
	window.parent.createDialog("modalDlg", "@resources/FAULT.alertProperty@", 800, 500, "alert/showAlertDetail.tv?alertId=" + alertId, null, true, true);	
}
function doClearOperation(rid){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	onClearAlarmClick();
}
function doConfirmOperation(rid){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	onConfirmAlarmClick();
}


Ext.onReady(function () {
	/** initialize the tooltip **/
	Ext.QuickTips.init()
	
	$('#alertLevel').val(curLevel);
	//加载告警类型
	if(!userAlert){
		initData();
	}else{ //如果是关注告警;
		initAttentionData();
	}
	
	if(alertId > 0){
		window.parent.createDialog("modalDlg", "@resources/FAULT.alertProperty@", 800, 500,"alert/showAlertDetail.tv?alertId=" + alertId, null, true, true)
		window.parent.alertId = 0
	}
	var urlStr = '../alert/getCurrentAlertList.tv';
	
	store = new Ext.data.JsonStore({
	    url: '/alert/getCurrentAlertList.tv',
	    root: 'data', idProperty: 'alertId',
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    listeners:{
	    	load: function(){
	    		grid.getStore().each(function(record){
	 				if(record.data.alertId == alertId){
	 					grid.getSelectionModel().selectRecords([record])
	 					return false;
	 				}else{
	 					return true;
	 				}
	 			});
	    	},
	    	loadexception: function(){
	    		window.parent.showMessageDlg("@COMMON.tip@", "@fault/alert.loadException@");
	    	}
	    },
	    fields: ['alertId', 'typeId', 'typeName', 'host', 'source', 'level', 'levelName', 'status','cmcMac','cmtsIp',
	             'clearUser', 'clearTime','confirmMsg', 'clearMsg', 'confirmUser', 'message', 'firstTime','lastTime',
	             'confirmTime', "oid", "entityType", "entityId", "entityName", "parentId", "parentName", 'nativeMessage',
	             'confirmStatus', 'clearStatus']
	});
	
	var cmConfig = CustomColumnModel.init(saveColumnId,columnModels,{sm:sm}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'lastTime', direction: 'ASC'};
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
	
	var tbar = new Ext.Toolbar({
	    items: [
	      {text: '@COMMON.confirm@', id: 'confirmAlert', iconCls: 'bmenu_correct', disabled: !confirmAlertPower, handler: onConfirmAlarmClick}, 
	      '-',       
	      {text: '@COMMON.clear@', id: 'clearAlert', iconCls: 'bmenu_delete', disabled: !clearAlertPower, handler: onClearAlarmClick}
	    ]
	 });
	
	grid = new Ext.grid.GridPanel({store: store,
		cls:"normalTable edge10",
		stripeRows:true,
		renderTo: "grid-container",
		bodyCssClass: 'normalTable',
		cm: cm,
		loadMask: true,
		sm: sm,title : "@ALERT.currentAlertInfoList@",
		viewConfig: {forceFit: true},
		enableColumnMove : true,
        listeners : {
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
			}
        },
        tbar: tbar,
	 	bbar: buildPagingToolBar()
 	});
	
	if(!confirmAlertPower){ Ext.getCmp('confirm').hide(); }
    if(!clearAlertPower){ Ext.getCmp('clear').hide(); }

/*    viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [
        	{
        		region:'north',
        		contentEl :'topPart',
        		height: 130,
        		border: false,
        		cls:'clear-x-panel-body'
        	},grid
        ]
	}); 
*/	curFlag = curLevel > 0;
	store.setBaseParam('level', curLevel);
	store.setBaseParam('typeId', curType);
	if(userAlert){ //如果是关注告警;
		var typeIdList = $("#putSel").data("value");
		store.setBaseParam('typeIdList', typeIdList);
	}
	store.setBaseParam('hostDevice', hostDevice);
	store.setBaseParam('startTime', beginTime);	
	store.setBaseParam('endTime', stopTime);	
	store.load({params: {start: 0, limit: pageSize},
		callback : function(records, options, success) {
            $("#hostDevice").autocomplete(ipNameList,{width:260, scroll:true});
            //在当前告警页面查询没有数据,但是又从导航树进入有数据时的情况下
            if (records && records.length > 0) {
            	top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
        }});
});

function toggleQueryModel(){
	if($('#advanceSearchTable').css("display") == "none"){
		//当前是普通模式，应当切换至高级模式
		//表格变小，图例下移，查询行下移，复杂行显示
		showAdvanceModel();
		//$queryToggle.text('@COMMON.simpleQuery@');
	}else{
		//当前是高级模式，应当切换至普通模式
		//隐藏复杂行，查询条件上移，图例上移，grid变大
		showQuickModel();
		//$queryToggle.text('@COMMON.advanceQuery@');
	}
	reSize();
}

function showQuickModel() {
	$('#quickSearchTable').show();
	$('#advanceSearchTable').hide();
	
	$('#confirmTr').hide();
	$('#clearTr').hide();
	$('#alertLegend').css({
		top: '50px'
	});
	reSize();
	saveQueryModel(true)
}

function showAdvanceModel() {
	$('#grid-container').height($('#grid-container').height()-60);
	grid.setHeight(grid.getHeight()-60);
	
	$('#quickSearchTable').hide();
	$('#advanceSearchTable').show();
	
	$('#alertLegend').css({
		top: '147px'
	});
	$('#confirmTr').show();
	$('#clearTr').show();
	reSize();
	saveQueryModel(false)
}

function saveQueryModel(simpleModeFlag) {
	$.post('/alert/saveCurrentAlertQueryModel.tv?simpleModeFlag=' + simpleModeFlag);
}

function renderDevice(v,c,r){
	var entityType = r.data.entityType;
	if (EntityType.isCmtsType(entityType)) {
		// CMTS
		var formatStr = "<a href='#' onclick='viewCmtsSnap(\"{0}\",\"{1}\")'>{2}</a>";
		return String.format(formatStr, r.data.entityId, r.data.cmtsIp, v);
	} else if (EntityType.isCcmtsWithAgentType(entityType)) {
		// CCMTS-B
		var $source = r.data.host;
		if ($source.indexOf("[") > -1) {
			return "<a href='#' onclick='showDevice(" + r.data.entityId + ",\""
					+ escape(r.data.entityName) + "\"," + entityType + ")'>"
					+ v + "</a>"
		} else {
			return v;
		}
	} else {
		// CCMTS-A
		var $source = r.data.host;
		if ($source.indexOf("[") > -1) {
			return "<a href='#' onclick='showDevice(" + r.data.parentId + ",\""
					+ escape(r.data.parentName) + "\"," + entityType + ")'>"
					+ v + "</a>"
		} else {
			return v;
		}
	}
}

function showDevice(id,en,entityType){
	var entityName = unescape(en);
	window.top.addView('entity-' + id, entityName,
			'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + id);
}

function reSize(){
	//调整grid-container的宽高及grid
	$('#grid-container').height($('body').height()-$('#topPart').outerHeight()-5);
	grid.setHeight($('#grid-container').height());
}

$(function(){
	$(window).resize(function(){
		grid.setSize($('#grid-container').width(), $('#grid-container').height());
	});
})

/* 打印页面;
 * all表示所有页面,如果不是all，则打印当前页面;
 */
function printFn(isPrint, num){
	var limitNum = (num === 'all') ? 99999999 : pageSize;
	var start = (num === 'all') ? 0 : (grid.getBottomToolbar().getPageData().activePage - 1) * pageSize;
	var tmpStore = grid.getStore();
	var tmpParam = $.extend({}, {params: tmpStore.baseParams}, tmpStore.lastOptions);
	if (tmpParam && tmpParam.params) {
		//删除分页参数会导致系统传递默认分页参数，我们将分页的start和limit设置为-1
		tmpParam.params.start = start;
		tmpParam.params.limit = limitNum;
		tmpParam.callback = function(r, opt, success){
			createPrintTable(isPrint, r, 1, 1);
		}
    }
	//重新定义一个数据源
	var tmpAllStore = new Ext.data.Store({
        proxy: tmpStore.proxy,
        reader: tmpStore.reader
    });
	tmpAllStore.load(tmpParam);
}
/*   根据数据生成table;
 *   num=1 则表示第一列的数据不打印，一般第一列有可能是勾选框;
 *   lastNum=1 则表示最后一列数据不打印，最后一列很可能是操作列，不需要打印;
 */
function createPrintTable(isPrint, r, num, lastNum){
	var str = '',
	    cm = grid.getColumnModel(),
	    num2 = 0 || lastNum;
	
	str += '<h3 class="txtCenter">'+ grid.title +'</h3>';
	str += '<table class="printTable" width="100%" border="1" borderColor="#cccccc" rules="all"><thead><tr>';
	for (var i = num || 0; i < cm.getColumnCount() - num2; i++) {
		if(!cm.isHidden(i)){//如果该列未隐藏，则统计
			str +=   '<th>' + cm.getColumnHeader(i) + '</th>'
		}
	}
	    str += '</tr></thead><tbody>';
	for(var i=0; i<r.length; i++){
		var data = r[i].data;
		str += '<tr>';
		str +=   '<td>' + levelToStr(data.level)+ " " + data.message + '</td>';
		str +=   '<td>' + data.typeName + '</td>';
		str +=   '<td>' + data.host + '</td>';
		str +=   '<td>' + data.firstTime + '</td>';
		str +=   '<td>' + data.lastTime + '</td>';
		str +=   '<td align="center">' + confirmStatusToStr(data.status) + '</td>';
		str +=   '<td align="center">' + clearStatusToStr(data.status) + '</td>';
		str += '</tr>';
	}
	str += '</tbody></table>';
	var wnd = window.open();
	printPage(str, wnd.document, isPrint);
};
function printPage(strHtml, doc, isPrint){
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">');
	doc.write('<html><head>');
	doc.write('<title></title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/alertPrint.css"/>');
	doc.write('</head>');
	doc.write('<body class="edge10"><div class="center">');
	doc.write(strHtml);
	if(isPrint === true){
		doc.write('</div><script type="text/javascript">window.print()<\/script></body>');
	}else{
		doc.write('</div></body>');
	}
	doc.write('</html>');
	doc.close();
}
//打印中【确认状态】转换为图片;
function confirmStatusToStr(num){
	var str = '<img alt="@resources/FAULT.unconfirmed@" src="../../images/wrong.png" />';
	switch(num){
	case 1:
		str = '<img alt="@resources/FAULT.confirmed@" src="../../images/correct.png" />';
		break;
	}
	return str;
}
//打印中【清除状态】转换为图片;
function clearStatusToStr(num){
	var str = '<img alt="@resources/FAULT.unclear@" src="../../images/wrong.png" />';
	switch(num){
	case 2:
	case 3:
		str = '<img alt="@resources/FAULT.cleared@" src="../../images/correct.png" />';
		break;
	}
	return str;
}
function levelToStr(num){
	var str = String.format('<img src="../images/fault/level{0}.gif" />', num);
	return str;
}
