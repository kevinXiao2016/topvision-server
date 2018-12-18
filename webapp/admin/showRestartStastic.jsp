<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin DateTimeField
    module platform
</Zeta:Loader>
<style>
.X-TOOLBAR-TITLE { background-color:red; }
body { overflow: hidden; }
#container { height : 95%; width : 100%; }
/* #toolbar2 { height:60px;vertical-align:bottom } */
.item-selected { background:url(/epon/image/checked.gif) no-repeat; }
.item-unselected { background:url(/epon/image/unchecked.gif) no-repeat; }
.ext-strict .ext-ie .x-date-menu, .ext-border-box .ext-ie8 .x-date-menu{ height: 238px; }
.x-form-spinner-splitter { position : absolute; }
.x-grid3-hd-inner .x-grid3-hd-2{ align : center;text-align: center; }
</style>
<script type="text/javascript">
var entityId = '';
var ZetaGUI = {enableWaitingDlg: false, roundedBorders: true, enableDraggable: false,
        windowShadow: true, enableBorders: true, enableCollapsible: true, enableSplit: true,
        tabPlain: false, tabBorders: true, resizeTabs: true, minTabWidth: 80, tabWidth: 150,
        NAVI_BAR_HEIGHT: 32, headerHeight: 54, viewMargin: {left: 4, right: 4, top: 0, bottom: 4}};
        
function closeWindow(id) {
    if (id == 'modalDlg') {
        closeModalDlg();
    } else {
        var w = Ext.WindowMgr.get(id);
        if (w != null) {
            w.close();
        }
    }
}
/*************************************************************
					入口
*************************************************************/
$(DOC).ready(function(){
	buildToolbar();
	
	var reader = new Ext.data.JsonReader({
	    root: "data",
        fields: [
			'deviceIndex','entityId','count','ip',"displayName","entityAlias","lastRestartTimeString","createTimeString"
        ]
	});
	store = new Ext.data.GroupingStore({
       	url: '/performance/loadRestartStatistic.tv',
       	reader: reader,
       	listeners:{
       		load : function(){
       			window.startTime = reader.jsonData.startTimeLong;
       			window.endTime = reader.jsonData.endTimeLong;
       			Ext.getCmp("startTimeField").setValue(reader.jsonData.startTime);
       			Ext.getCmp("endTimeField").setValue(reader.jsonData.endTime);
       		}
       	},
		groupField: "ip",
		autoLoad : true
    });

	var columnModels = [
		{header: "局端设备IP",width:120,sortable:true,align:'left', menuDisabled:false,dataIndex:'ip'},
		{header: "设备位置", width:150,sortable:true, align:'left',menuDisabled:false,dataIndex:'displayName'},
		{header: "设备别名", width:150,sortable:true, align:'left',menuDisabled:false,dataIndex:'entityAlias'},
		{header: "重启次数", width:120,sortable:true,align:'left', menuDisabled:false,dataIndex:'count'},
        {header: "最近启动时间", width:120,sortable:true,align:'left', menuDisabled:false,dataIndex:'lastRestartTimeString'},
		{header: "创建时间", width:120,sortable:true,align:'left', menuDisabled:false,dataIndex:'createTimeString'},
		{header: "操作",width:120,sortable:true, align:'left',menuDisabled:false,dataIndex:'count',renderer:function(v,m,r){
			return String.format("<a href='#' style='color:green' onclick='showDetail({0},{1})'>查看明细</a>",r.data.deviceIndex,r.data.entityId);
		}}
	];
    grid = new Ext.grid.GridPanel({
		store: store,
        border: true,
        columns: columnModels,
        width : document.body.offsetWidth,
        height : document.body.offsetHeight - 55,
        view: new Ext.grid.GroupingView({
            forceFit: true,hideGroupedColumn: false,enableNoGroups: true
        }),
		renderTo: container
    });
   store.setDefaultSort('count', 'DESC'); 
});

function showDetail(deviceIndex,entiyId){
	var url = "/performance/showRestartDetail.tv?entityId=" + entiyId;
	url += "&deviceIndex=" + deviceIndex;
	url += "&startTime=" + startTime;
	url += "&endTime=" + endTime;
	createDialog("restartDetail", '设备重启报表', 670, 540,url , null, true, true);
}

function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
    if (id == 'modalDlg') {
        showModalDlg(title, width, height, url, closeHandler)
    } else {
        var win = new Ext.Window({id: id, title: title, width: width, height: height,
            border :false,
            bodyBorder  :false,
            modal: (modal == undefined ? true : modal), closable: (closable == null ? true : closable),
            plain:true, resizable: false, stateful: false, shadow: ZetaGUI.windowShadow,
            html: '<iframe width=100% height=100% frameborder=0 src="' + url + '"></iframe>'});
        if (typeof closeHandler != 'undefined') {
                win.on("close", closeHandler);
        }
        win.show();
        return win;
    }
}

function exportExcel(){
	var startTime =  $("#startTimeField").val();
	var endTime =  $("#endTimeField").val();
	window.location.href="/performance/exportRestartStatic.tv?startTime=" + startTime+ "&endTime=" + endTime; 
}

/*************************************************************
                    创建顶部工具栏以及工具栏菜单
*************************************************************/
function buildToolbar() {
	jtb = new Ext.Toolbar();
	jtb.render('toolbar');
	var items = [];
    items[items.length] = {xtype: 'tbspacer', width: 15}
    items[items.length] = '时间查询区间:';
    items[items.length] = {xtype: 'tbspacer', width: 5};
    items[items.length] = { xtype: 'datetimefield', id:"startTimeField",editable : true,validationEvent : 'keyup'}
    items[items.length] = {xtype: 'tbspacer', width: 5}// add a 10px space
    items[items.length] = '-';
    items[items.length] = {xtype: 'tbspacer', width: 5}// add a 10px space
    items[items.length] = { xtype: 'datetimefield', id:"endTimeField",editable : true}
    items[items.length] = {xtype: 'tbspacer', width: 5}
    items[items.length] = { xtype: 'button',text: '查询',iconCls:'bmenu_find', handler: query}
    items[items.length] = { xtype: 'button',text: '@COMMON.exportExcel@',iconCls:'bmenu_find', handler: exportExcel}
    /* items[items.length] = '->';
    items[items.length] = { xtype: 'checkbox',checked:true,label: '分组排列',iconCls:'bmenu_find', handler: setGroup}
    items[items.length] = { xtype: 'label',text: '分组排列',iconCls:'bmenu_find', handler: setGroup} */
	jtb.add(items);
	jtb.doLayout();
}

function query(){
	var startTime =  $("#startTimeField").val();
	var endTime =  $("#endTimeField").val();
	if(!Ext.getCmp("startTimeField").isValid()){
		showMessageDlg("提示","请输入正确的时间格式",'error',function(){
            Ext.getCmp("startTimeField").focus();
        });
        return;
	}
	if(!Ext.getCmp("endTimeField").isValid()){
        showMessageDlg("提示","请输入正确的时间格式",'error',function(){
            Ext.getCmp("endTimeField").focus();
        });
        return;
    }
	//如果只输入了结束时间没有起始时间,提示错误
	if(!startTime && endTime){
		showMessageDlg("提示","请输入查询的起始时间",'error',function(){
			Ext.getCmp("startTimeField").focus();
		});
		return;
	}
	//如果只输入了起始时间没有结束时间,提示错误
	if(startTime && !endTime){
        showMessageDlg("提示","请输入查询的结束时间",'error',function(){
            Ext.getCmp("endTimeField").focus();
        });
        return;
    }
	store.load({params: {startTime: startTime,endTime: endTime}});
}

</script>
</head>
<body class=CONTENT_WND style="overflow: hidden;">
      <div id="toolbar" style="position: absolute; left: 0; top: 0;width:100%"></div> 
      <div id="container" style="position: absolute;top:27px;"></div>
</body>
</Zeta:HTML>