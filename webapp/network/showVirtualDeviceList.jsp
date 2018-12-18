<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var virtualNetId = ${virtualNetId};
var folderId = ${folderId};

function onNewEntityClick(){
	 	window.top.createDialog('modalDlg', I18N.COMMON.newEntity, 360, 230, 'entity/pushNewEntityFromVirtual.tv?virtualNetId=' + virtualNetId, null, true, true);
}

function onRefreshClick(){
	pullVirtualNetList();
}

function pullVirtualNetList(){
	$.ajax({
		url:'/virtualnet/getAllProductsInVirtualNet.tv',
		method:'post',
		cache:false,
		data:{virtualNetId : virtualNetId},
		dataType:'json',
		success:pullVirtualNetListSuccess,
		error:pullVirtualNetListFailure
	});
}

/**
 * scope:ajax object
 */
function pullVirtualNetListSuccess(json){
	if(json.data.length == 0 ){
		window.list = null;
	}else{
		window.list = json;
	}
	if(!window.first){
		window.first = true;
		createGrid();
	}else{
		store.loadData(window.list);
	}
}

function pullVirtualNetListFailure(e){
	//TODO 获取数据出错了
	alert("<fmt:message bundle="${res}" key="topo.virtualDeviceList.retry" />");
}
/**
 * SEARCH ACTI0N
 */
function onFindClick(){
	window.parent.showInputDlg(I18N.NETWORK.findEntity, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim().toLowerCase();
		if(match=='') {window.parent.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag=true;
		var records = [];
		store.each(function(record) {
            var userObj = record.data;
            if ((userObj.productName && userObj.productName.toLowerCase().indexOf(match) != -1) ||
                (userObj.productIp && userObj.productIp.indexOf(match) != -1)){
                //(userObj.sysName && userObj.sysName.toLowerCase().indexOf(match) != -1)) {
				records.push(record);
				flag = false;
			}
		});
		if (flag) {window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);}
		else {grid.getSelectionModel().selectRecords(records);}	
	});
}

Ext.onReady(function() {
	pullVirtualNetList();
});

function createGrid(){
	Ext.BLANK_IMAGE_URL = '../images/s.gif';
	var reader = new Ext.data.JsonReader({
	    idProperty: "productId",
	    root: "data",
        fields: [
			{name: 'virtualNetId'},
		    {name: 'productName'},
		    {name: 'productId'},
		    {name: 'productType'},
		    {name: 'productIp'},
		    {name: 'productIpLong'},
		    {name: 'productCreateTime'}
        ]
	});
	store = new Ext.data.GroupingStore({
		data: window.list,
       	reader: reader,
		remoteGroup: false,
		remoteSort: false,
		sortInfo: {field: 'productCreateTime', direction: "ASC"},
		groupField: 'productType',
		groupOnSort: false
    });

    var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "' + I18N.COMMON.items + '" : "'+ I18N.COMMON.item + '"]})';

	var toolbar = [
		{text:I18N.COMMON.create, handler:onNewEntityClick, iconCls:'bmenu_new'}, '-',
        {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick},
        {text: I18N.COMMON.find, iconCls: 'bmenu_find', handler:onFindClick}
	];
	var columnModels = [
	                	{header:"<fmt:message bundle="${res}" key="Config.oltConfigFileImported.deviceName" />", width:150, sortable:true, groupable:false, dataIndex:'productName'},
	                    {header: "<fmt:message bundle="${res}" key="COMMON.ip" />", width:150, sortable:true, groupable:false, dataIndex: 'productIp'},
	                    {header: "<fmt:message bundle="${res}" key="topo.virtualDeviceList.deviceNo" />", width:150, sortable:true, dataIndex:'productType',renderer:renderType},
	                    {header:"id", width:150, sortable:true, groupable:false, dataIndex:'productId'},
	                    {header:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.onlineTime" />',width:100,sortable:true,groupable:false,dataIndex:'productIpLong'},
	                    {header:'<fmt:message bundle="${res}" key="COMMON.createTime" />',width:100,sortable:true,groupable:false,dataIndex:'productCreateTime'}
	                ];
    window.grid = new Ext.grid.GridPanel({
		store:store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: false,
        columns:columnModels,

        view: new Ext.grid.GroupingView({
            forceFit:true,
            hideGroupedColumn:true,
            enableNoGroups:true,
            groupTextTpl: groupTpl
        }),

        tbar: toolbar,
		renderTo: document.body
    });
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	window.rowIndex = rowIndex;
    	grid.getSelectionModel().selectRow(rowIndex);
    	e.preventDefault();
    	var recode = grid.getStore().getAt(rowIndex).data;
    	//window.entityId = recode.productId;
    	window.productId = recode.productId;
    	window.typeId = recode.productType;
    	window.productName = recode.productName;
    	window.ip = recode.productIp;
    	entitymenu.showAt(e.getXY());
   		/* var sm = grid.getSelectionModel();
        var nodes = sm.getSelections();
        var record = grid.getStore().getAt(rowIndex);  // Get the Record
        if (nodes != null && nodes.length > 1){
            getEntityMenuMutl(record).showAt(e.getPoint());
            return;
        }
        if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		getEntityMenu(record).showAt(e.getPoint()); */
    });  
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);  // Get the Record
        showEntitySnap(record.data.entityId, record.data.ip);		
    }); 
	store.on('load', loadCallback);
	//--------CRATE LAYOUT----//
    new Ext.Viewport({layout:'fit', items:[grid]});
}

//##########################
//####### CALLBACK #########
//##########################
function loadCallback(store, records, options) {}



//##########################
//#######   RENDER   #######
//##########################
function renderType(value, p, record){
}



function renderName(value, p, record){
	return String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
		record.data.entityId, record.data.ip, value);
}
function renderSnmp(value, p, record){
	if (value) {
		return String.format('<img title="{0}" src="../images/fault/snmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img title="{0}" src="../images/fault/nosnmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderAgent(value, p, record){
	if (value) {
		return String.format('<img title="{0}" src="../images/fault/agent.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img title="{0}" src="../images/fault/noagent.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img title="{0}" src="../images/fault/online.gif" border=0 align=absmiddle>',
			I18N.COMMON.online);	
	} else {
		return String.format('<img title="{0}" src="../images/fault/offline.gif" border=0 align=absmiddle>',
			I18N.COMMON.unreachable);	
	}
}
function renderLevel(value, p, record) {
	switch (value) {
		case 1:
			return String.format('<img style="cursor:hand" title="{0}" src="../images/fault/level1.gif" border=0 align=absmiddle>', I18N.EVENT.infoLevel);			
		case 2:
			return String.format('<img style="cursor:hand" title="{0}" src="../images/fault/level2.gif" border=0 align=absmiddle>', I18N.EVENT.warningLevel);			
		case 3:
			return String.format('<img style="cursor:hand" title="{0}" src="../images/fault/level3.gif" border=0 align=absmiddle>', I18N.EVENT.minorLevel);			
		case 4:
			return String.format('<img style="cursor:hand" title="{0}" src="../images/fault/level4.gif" border=0 align=absmiddle>', I18N.EVENT.majorLevel);			
		case 5:
			return String.format('<img style="cursor:hand" title="{0}" src="../images/fault/level5.gif" border=0 align=absmiddle>', I18N.EVENT.criticalLevel);			
		default:
			return String.format('<img title="{0}" src="../images/fault/{1}.gif" border=0 align=absmiddle>', I18N.EVENT.normal, 'normal');		
	}
}

//##########################
//#######  MENU  ###########
//##########################

var entitymenu = new Ext.menu.Menu({
	id: 'entityMenu',
	enableScrolling: false,
	items:[
		{
			id:'showEntityDetail',
			text:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.showDevice" />',
			handler:function(){
				window.top.addView('product-' + productId,
						productName,
						'entityTabIcon', 'portal/showEntitySnapJsp.tv?folderId=' + folderId + '&productType=' + typeId + '&productId=' + productId);
				return false;
			}
		},
		{
			id:'addToTopo',
			text:'<fmt:message bundle="${res}" key="COMMON.addOnuToTopo" />',
			handler:function(){
				$.ajax({
					url: '/virtualnet/isProductExistsInTopo.tv',
					method:'post',
					data:{productId:productId,productType: typeId},
					cache:false,
					dataType:'json',
					success:function(json){
						if(json.success && !json.isProductExistsInTopo){
							//entityId,folderid
							$.ajax({
								url: '/virtualnet/relateProductToTopoGraph.tv',
								method:'post',
								data:{productId:productId,folderId: folderId,typeId:typeId,virtualNetEntityName:productName+' <fmt:message bundle="${res}" key="topo.virtualDeviceList.link" />',virtualNetEntityIp:ip,virtualNetId:virtualNetId},
								cache:false,
								dataType:'json',
								success:function(json){
									if(!json.success){
										window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.addTopoFail" />!')
									}
									window.parent.showConfirmDlg("<fmt:message bundle="${res}" key="topo.virtualDeviceList.addSuccess" />", "<fmt:message bundle="${res}" key="topo.virtualDeviceList.isShow" />", function(type, text){
										if(type == 'no')return;
										var gotoId = json.entityId;
										var frame = window.top.getFrame("topo"+folderId);
										if(!frame){
											window.parent.addView("topo" + folderId, "<fmt:message bundle="${res}" key="SYSTEM.Region" />["+folderId+"]", "topo-tabicon", "topology/getTopoMapByFolderId.tv?folderId=" + folderId+"&entityId="+gotoId);
										}else{
											window.parent.setActiveTab("topo"+folderId);
											frame.onRefreshClick(gotoId);
											//frame.goToEntity();
										}
									});
								},
								error:function(){}
							});
						}else{
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.addTopoFail" />!');
						}
					},
					error:function(){
						window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.addTopoFail" />!');
					}
			});
			return false;
			}
		},
		{
			id:'modifyEntityName',
			text:'<fmt:message bundle="${res}" key="COMMON.rename" />',
			handler:function(){
				window.parent.showInputDlg("<fmt:message bundle="${res}" key="topo.virtualDeviceList.rename" />", "<fmt:message bundle="${res}" key="topo.virtualDeviceList.newName" />", function(type, text) {
					if (type == 'cancel') {return;}
					//TODO 后台去查
					grid.getStore().getAt(rowIndex).set("productName", text);
					$.ajax({
						url: '/virtualnet/renameProductInVirtualNet.tv',
						data: {
							virtualNetId: virtualNetId,
							productId: productId,
							productType: typeId,
							productName: text
						},
						cache: false,
						success: function() {
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.renameSuccess" />');
							var frame = window.top.getFrame("topo"+folderId);
							if(frame){
								frame.onRefreshClick();
							}
						},
						error: function() {
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.renameSuccess" />');
						}
					});
				});
			}
		},
		{
			id:'deleteEntity',
			text:'<fmt:message bundle="${res}" key="MODULE.deleteDevice" />',
			handler:function(){
				window.parent.showConfirmDlg("<fmt:message bundle="${res}" key="topo.virtualDeviceList.deviceRemoved" />", "<fmt:message bundle="${res}" key="Config.confirmDeleteDevice" />?", function(type, text) {
					if(type == 'no')return;
					$.ajax({
						url:'/virtualnet/deleteProduct.tv',
						method:'post',
						cache:false,
						data:{productId:productId,productType:typeId},
						success:function(){
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.removeSuccess" />');
							window.location.reload();
						},
						error:function(){
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.removeFail" />')
						}
					})
				});
			}
		},
		{
			id:'resetCmcWithAgent',
			text:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.rebootCCMTS" />',
			handler:function(){
				window.parent.showConfirmDlg("<fmt:message bundle="${res}" key="topo.virtualDeviceList.deviceRemoved" />", "<fmt:message bundle="${res}" key="Config.confirmDeleteDevice" />?", function(type, text) {
					if(type == 'no')return;
					$.ajax({
						url:'/virtualnet/resetCmcWithAgent.tv',
						method:'post',
						cache:false,
						data:{productId:productId,productType:typeId},
						success:function(){
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.rebootSuccess" />');
							window.location.reload();
						},
						error:function(){
							window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip" />','<fmt:message bundle="${res}" key="topo.virtualDeviceList.rebootFail" />')
						}
					})
				});
			}
		},
		{
			id:'modifyPhysicalInterfacePreferredMode',
			text:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.setMajorPriority" />',
			handler:function(){

			}
		},
		{
			id:'DHCPRelay',
			text:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.DHCPRelay" />',
			handler:function(){

			}
		},
		{
			id:'DHCPServer',
			text:'<fmt:message bundle="${res}" key="topo.virtualDeviceList.DHCPServer" />',
			handler:function(){

			}
		}
	]
});
window.onerror = function(e){}
</script>
</head>
<body class=CONTENT_WND>
</body>
</html>
