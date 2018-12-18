var store = null;
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(grid.getEl().dom, wnd.document);
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
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<html>');
	doc.write('<head>');
	doc.write('<title></title>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	doc.write('<link rel="stylesheet" type="text/css" href="../css/report.css"/>');
	Zeta$NoHeaderAndFooter(doc);
	doc.write('</head>');
	doc.write('<body style="margin:50px;"><center>');
	doc.write(obj.innerHTML);
	doc.write('</center></body>');
	doc.write('</html>');
	doc.close();
}
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		window.parent.showConfirmDlg("@COMMON.tip@", "@ALERT.confirmDeleteEvent@", function (type, text) {
			if (type == "no") {
				return;
			}
			var eventIdIds = [];
			var selections = sm.getSelections();
			for (var i = 0; i < selections.length; i++) {
				eventIdIds[i] = selections[i].data.eventId;
			}
			Ext.Ajax.request({url:"../fault/clearEvent.tv", method:"post", success:function (response) {
				onRefreshClick();
				window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.deleteSuccess@");
			}, failure:function () {
				window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.deleteFail@");
			}, params:{eventIdIds: eventIdIds}});
		});
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseEvent@");
	}
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({pageSize: pageSize, store:store, displayInfo:true,
		items: ["-", String.format("@COMMON.pagingTip@", pageSize), '-']});
	return pagingToolbar;
}
function onExportClick() {}
function onRefreshClick() {
	store.reload();
}
function onMaxViewClick() {}
Ext.onReady(function () {
	Ext.QuickTips.init();
	var reader = 
		new Ext.data.JsonReader({
			totalProperty:"rowCount", 
			idProperty : 'eventId',
			id:"monitorId", 
			root:"data", 
			fields:[
			    {name:"eventId"}, 
			    {name:"note"}, 
			    {name:"typeName"}, 
			    {name:"host"}, 
			    {name:"date"}, 
			    {name:"time"}]
			});
	store = new Ext.data.GroupingStore({
		url:storeUrl, 
		reader:reader, 
		remoteGroup:false,
		remoteSort:false, 
		sortInfo:{field:"date", direction:"DESC"},
		groupField:"host", 
		groupOnSort:false
	});
		
	var cm = new Ext.grid.ColumnModel(columnModels);
	var groupTpl = "{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"" + "@COMMON.items@" + "\" : \"" + "@COMMON.items@" + "\"]})";
	grid = new Ext.grid.GridPanel({
		stripeRows:true,region: "center",bodyCssClass: 'normalTable',
		store:store, 
		cm:cm, 
		view:new Ext.grid.GroupingView({
			forceFit:true, 
			hideGroupedColumn:true, 
			enableNoGroups:true, 
			groupTextTpl:groupTpl
		}), 
		tbar:myToolbar, 
		bbar:buildPagingToolBar(), 
		renderTo:document.body
	});
	new Ext.Viewport({layout:"fit", items:[grid]});
	store.load({params:{start:0, limit: pageSize}});
});

function manuRenderer(v,m,r){
    if (clearevent) {
        return String.format(" <a href='javascript:;' onClick='doClearOperation(\"{0}\");'>@COMMON.delete@</a>",r.id);
    } else {
        return "";
    }
}
function doClearOperation(rid){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	onDeleteClick();
}
