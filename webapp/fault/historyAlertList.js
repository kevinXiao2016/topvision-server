/**
 * author: niejun
 *
 * Event detail.
 */
var store = null;
var grid;
var eventContextMenu;
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
	}	*/
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
    store.reload();
}
function showHistoryAlertDetail(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selection = sm.getSelected();
		showHistoryAlertProperty(selection.data.alertId);
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@resources/FAULT.pleaseSelectAlert@");
	}
}
function onExportAlertClick() {
}
function renderLevelName(value, p, record) {
	return record.data.levelName;
}
function renderLevel(value, p, record) {
	return String.format('<img hspace=5 src=\"../images/fault/level{0}.gif\" border=0 align=absmiddle nm3kTip="{1}" class="nm3kTip mL10">{2}',
		record.data.level, record.data.levelName, record.data.message);
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
function showHistoryAlertProperty(alertId) {
	window.parent.createDialog("alertDlg", "@resources/FAULT.alertProperty@", 800, 500,
		"alert/showHistoryAlertDetail.tv?alertId=" + alertId, null, true, true);
}
function onPropertyEventClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		var r = sm.getSelected();
		showHistoryAlertProperty(r.data.alertId);
	}
}
function buildEventContextMenu() {
	eventContextMenu = new Ext.menu.Menu({id: "eventContextMenu", items: [
		{text: "@COMMON.refresh@", iconCls: 'bmenu_refresh', handler: onRefreshClick},
		'-', {text:"@COMMON.property@", handler: onPropertyEventClick}]});
}

var curLevel = 0;
var curType = 0;
var curFlag = true;
function showCurrentAlert() {
	var t = window.parent.contentPanel.getActiveTab();
	t.alertType = 'cur';
	if(curLevel > 0) {
		curFlag = true;
	} else {
		curFlag = false;
	}	
	var url = '';
	//在前面告警和历史告警之间切换时，保持查询参数不变
	var level =  $('#alertLevel').val();
	var typeId =  $("#putSel").data("value");
	var hostDevice = $("#hostDevice").val();
	var startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	var endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	url = 'showCurrentAlertList.tv?level=' + level + '&typeId=' + typeId
		+ "&hostDevice=" + hostDevice + "&startTime=" + startTime
		+ "&endTime=" + endTime;
	if(userAlert){
		url = 'showCurrentAlertList.tv?level=' + level + '&typeId=0'
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
function renderManu(v,m,r){
	return String.format("<a href='javascript:;' onClick='doViewOperation({0})'>@COMMON.view@</a>",r.id);
}

function doViewOperation(alertId){
	showHistoryAlertProperty(alertId);
}

Ext.onReady(function () {
	//加载告警类型
	if(!userAlert){
		initData();
	}else{ //关注告警;
		initAttentionData();
	}
	
    Ext.Ajax.timeout = 600000;
    function buildPagingToolBar() {
		pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize, store:store, 
			displayInfo:true, 
			items:["-", buildPageBox(pageSize), '-',
			   {text : "@resources/COMMON.printPreview@", iconCls :"bmenu_find", menu : [{
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
				},
			   {text: "@COMMON.print@", iconCls : 'bmenu_print', menu : [{
					text : '@resources/COMMON.printCurrentPage@',
					handler : function(){
						printFn(true);
					}
				},{
					text : '@resources/COMMON.printAllPage@',
					handler : function(){
						printFn(true, "all");
					}
				}]}, {
					text: "@COMMON.exportExcel@", 
					iconCls: 'bmenu_print',
					handler: function() {
						top.ExcelUtil.exportGridToExcel(grid, '@ALERT.historyAlertInfoList@');
					}
				},'-',
				{text: "@resources/FAULT.alertProperty@", iconCls:'bmenu_page', handler: showHistoryAlertDetail}
			]});
		return pagingToolbar;
	}
    
    var cmConfig = CustomColumnModel.init(saveColumnId,columnModels,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'firstTime', direction: 'DESC'};
		
	store = new Ext.data.JsonStore({
	    url: storeUrl,idProperty:'alertId',
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['alertId', 'typeName', 'host', 'level', 'levelName', 'cmcMac','parentId','parentName',
	    	'clearUser', 'message', 'firstTime', 'lastTime', 'clearTime' , 'entityId' , 'entityType',
	    	'entityName', 'nativeMessage']
	});
	store.setDefaultSort(sortInfo.field, sortInfo.direction);

	
	
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 90;
	
	grid = new Ext.grid.GridPanel({
		id: 'extGridContainer', 
		store: store, 
		cls:"normalTable edge10",
		stripeRows:true,
		renderTo: "grid-container",
		bodyCssClass: 'normalTable',
		border: true,
		title : "@ALERT.historyAlertInfoList@",
		loadMask: true,
		viewConfig: {forceFit: true},
		cm:cm, 
		enableColumnMove : true,
		listeners : {
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
			}
        },
		bbar: buildPagingToolBar()
	});

	/*new Ext.Viewport({
	     layout: "border",
	     items: [
        	{region:'north',
	 		contentEl :'topPart',
	 		height: 80,
	 		border: false,
	 		cls:'clear-x-panel-body'
        	},grid
        ]
	}); */
	 
	//new Ext.Viewport({layout:"fit", items:[grid]});
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
            //在当前告警页面没有查询出数据,但是直接切换到历史告警页面的情况下
            if (records && records.length > 0) {
            	top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
        }
	});
});
function viewCmtsSnap(cmcId, manageIp) {
	var recNum = grid.getStore().find("entityId",cmcId);//grid.getSelectionModel().getSelected()
	var record = grid.getStore().getAt(recNum);
    window.parent.addView('entity-' + cmcId, record.data.host.split("[")[1].split("]")[0], 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
}

function renderDevice(v,c,r){
	var entityType = r.data.entityType;
	if(EntityType.isCmtsType(entityType)){
	    // CMTS
		var formatStr = "<a href='#' onclick='viewCmtsSnap(\"{0}\",\"{1}\")'>{2}</a>";
		return String.format(formatStr, r.data.entityId, r.data.cmtsIp, v);
	}else if (EntityType.isCcmtsWithAgentType(entityType)) {
	    // CCMTS-B
		var $source = r.data.host;
		if( $source.indexOf("[") > -1 ){
			return "<a href='#' onclick='showDevice("+r.data.entityId+",\"" + escape(r.data.entityName) + "\"," + entityType + ")'>"+v+"</a>"
		}else{
			return v;
		}
	}else{
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
			createPrintTable(isPrint, r, 0, 1);
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
		str +=   '<td align="center">' + data.clearUser + '</td>';
		str +=   '<td align="center">' + data.clearTime + '</td>';
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
function levelToStr(num){
	var str = String.format('<img src="../images/fault/level{0}.gif" />', num);
	return str;
}

function reSize(){
	//调整grid-container的宽高及grid
	$('#grid-container').height($(document).height()-$('#topPart').outerHeight()-5);
	grid.setHeight($('#grid-container').height());
}