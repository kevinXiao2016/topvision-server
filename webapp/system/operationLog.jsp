<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<%@include file="../include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<title><fmt:message bundle="${resources}" key="SYSTEM.operationLog"/></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css"
      href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css"
      href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
	.x-grid3-cell-inner, .x-grid3-hd-inner {  white-space:normal; word-break:break-all; word-wrap:break-word;}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/wlan/error.js?v=ap2983"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zetaframework/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var grid = null;
var store = null;
var ipData = ${entityIpList};
var clientIpData = ${clientIpList};
var operNameData = ${userList};
function onRefreshClick() {
    store.reload();
}

function renderResult(value, p, record) {
    var resultCode = record.data.status;
    if (resultCode == 1)
    {
        return String.format('<img src="../images/yes.png" class="nm3kTip" border=0 align=absmiddle nm3ktip="{0}">', I18N.COMMON.success);
    } else {
        return String.format('<img src="../images/wrong.png" class="nm3kTip"border=0 align=absmiddle nm3ktip="{0}">', I18N.COMMON.failure);
    }
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({
    	id: 'extPagingBar', 
    	pageSize: pageSize,
    	store:store,
        displayInfo: true, 
        items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize)
    ]});
    return pagingToolbar;
}

function renderIp(value, p, record){
	if(value == ""){
		return "--";
	}
	return value;
}

function operAction(value, cellmate, record){
	return getI18NOperActionString(value);
}
Ext.BLANK_IMAGE_URL = "../images/s.gif";
Ext.onReady(function () {
    var w = document.body.clientWidth - 24;
    var h = $("window").height() - 90;
    var cm = new Ext.grid.ColumnModel([
        {header:I18N.SYSTEM.entityIp, dataIndex:'entityIp',width:150,align:"center",renderer: renderIp},
        {header:I18N.SYSTEM.operName, dataIndex:'operName',width:150,align:"center"},
        {header:I18N.SYSTEM.clientIpAddress, dataIndex:'clientIpAddress',width:150,align:"center"},
        {header:'<div style="text-align:center">'+I18N.SYSTEM.operAction +'</div>' , dataIndex:'operAction',width:360,align:'left',renderer:operAction},
        {header:I18N.SYSTEM.operTimeString , dataIndex:'operTimeString',width:150,align:"center"},
        {header:I18N.SYSTEM.operStatus , dataIndex:'status',width:150,align:"center",renderer:renderResult}
    ]);
    store = new Ext.data.JsonStore({
        url: 'system/loadOperationLogList.tv',
        totalProperty: "totalProperty",
        root: 'data',
        remoteSort: false,
        fields: ['entityIp', 'operName', 'clientIpAddress', 'operTimeString', 'status', 'operAction']
    });
    store.load({params: {start:0,limit:pageSize},
        callback : function(records, options, success) {
            $("#entityIp").autocomplete(ipData,{width:260});
            $("#clientIp").autocomplete(clientIpData,{width:260});
            $("#operName").autocomplete(operNameData,{width:260});
        }});
    var toolbar = [
        {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}
    ];
    grid = new Ext.grid.GridPanel({
        height:300,
        width:w,
        cls:"normalTable",
        stripeRows:true,
		region: "center",
        store: store,
        bbar: buildPagingToolBar(),
        viewConfig:{
        	forceFit : true,	
        },        
        cm: cm
    });
    var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [	grid,
	             	{region:'north',
		 	 		height:60,
		 	 		contentEl :'search',
		 	 		border: false,
		 	 		cls:'clear-x-panel-body',
		 	 		autoScroll: true
		          	}
	             ]
	});  
    var body = new Ext.form.FormPanel({
        id:"hbox",
        //renderTo:"hbox",
        height:h,
        width:w,
        layout:{
            type:'hbox',
            align:'stretchmax',
            pack:'start'
            //, padding:5
        },
        frame:false,
        border:false,
        defaults:{
            layout:'form',
            frame:false,
            border:false
        },
        tbar: toolbar,
        flex:1,
        items:[
            {width:10},
            {
                height:Ext.getBody().getHeight() - 38,
                width:w,
                layout:{
                    type:'vbox',
                    align:'stretchmax',
                    pack:'start'
                    //, padding:5
                },
                defaults:{
                    layout:'fit',
                    frame:false,
                    border:false
                },
                flex:1,
                items:[
                    {height:50,
                        contentEl:'search'
                    },
                    {
                        width:w,
                        height:h,
                        border:false,
                        flex:1,
                        items:[{el:'grid',frame:false,border:false}]
                    }
                ]
            },{width:10}
        ]
    });
   // new Ext.Viewport({layout:'fit', items:[body]});
    
    store.load({params:{start:0, limit: pageSize}});
    
});
function onSeachClick() {
    var entityIp = $("#entityIp").val();
    var clientIp = $("#clientIp").val();
    var status = $("#status").val();
    var operName = $("#operName").val();
    store.on("beforeload", function() {
        store.baseParams = {entityIp: entityIp,status: status,clientIp:clientIp,operName:operName,start:0,limit:pageSize};
    });
    store.load({params: {entityIp: entityIp,status: status,clientIp:clientIp,operName:operName,start:0,limit:pageSize}});
}
</script>
</head>
<body class="whiteToBlack">
<div id="search">
    <table width=100% cellspacing=0 cellpadding=0 class="noWrap">
        <tr>
            <td height=50px valign=middle
                style="pading-top: 10px; pading-left: 10px;">
                <table width=100% cellspacing=0 cellpadding=0>
                    <tr>
                        <td class="pL10 rightBlueTxt" width="50"><fmt:message bundle="${resources}" key="SYSTEM.entityIp" />:
                        </td>
                        <td width="140">
                        	<input
                                type="text" name="entityIp" id="entityIp" class="normalInput"
                                style="width: 140px;" />
                        </td>
                        <td class="rightBlueTxt" width="70"><fmt:message bundle="${resources}" key="SYSTEM.clientIpAddress" />:
                        </td>
                        <td width="140">
                        	<input
                                type="text" name="clientIp" id="clientIp" class="normalInput"
                                style="width: 140px;" />
                        </td>
                        <td width="70" class="rightBlueTxt">
                        	<fmt:message bundle="${resources}" key="SYSTEM.operName" />:
                        </td>
                        <td width="140">
                            <input type="text" name="operName" id="operName"  class="normalInput"
                                               style="width: 140px;" /></td>
                        <td width="70" class="rightBlueTxt">
                        	<fmt:message bundle="${resources}" key="SYSTEM.operStatus" />:
                        </td>
                        <td width="170">
                            <select id="status" style="width: 160px;" class="normalSel">
                            <option value="0" selected><fmt:message bundle="${resources}" key="COMMON.pleaseSelect" /></option>
                            <option value="1"><fmt:message bundle="${resources}" key="COMMON.success" /></option>
                            <option value="2"><fmt:message bundle="${resources}" key="COMMON.failure" /></option>
                        </select></td>
                        <td align=left>
                        	<a onclick="onSeachClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i><fmt:message bundle="${resources}" key="COMMON.query" /></span></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
<div id="grid"></div>
</body>
</html>