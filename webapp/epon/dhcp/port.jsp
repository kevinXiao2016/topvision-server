<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module oltdhcp
</Zeta:Loader>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var entityId = '${entityId}';
    var portProtIndex = '${portProtIndex}';
    var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
    var cm,columnModels,sm,store,grid,viewPort,tbar;
    
    Ext.onReady(function(){
        columnModels = [
            {header: "@oltdhcp.portType@", width:100, dataIndex: "portTypeName", align: "center", sortable: true},
            {header: "@oltdhcp.portNo@", width:100, dataIndex: "portName", align: "center", sortable: true},
            {header: "@oltdhcp.cascadePort@", width:100, dataIndex: "topOltDhcpPortCascade", align: "center", sortable: true,renderer:cascadeRender},
            {header: "@oltdhcp.cascadeProcess@", width:100, dataIndex: "topOltDhcpPortTrans", align: "center", sortable: true,renderer:transRender},
            {header: "@oltdhcp.trustPort@", width:100, dataIndex: "topOltDhcpPortTrust", align: "center", sortable: true,renderer:trustRender},
            {header: "@COMMON.manu@", width:100, dataIndex: "dataIndex5", align: "center", sortable: false, renderer:manuRender}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : false
            },
            columns: columnModels
        });
        store = new Ext.data.JsonStore({
            url: "/epon/oltdhcp/loadOltDhcpPortList.tv",
            root: "data",
            totalProperty: "rowCount",
            remoteSort: true,
            fields: ["entityId","topOltDhcpPortProtIndex","topOltDhcpPortTypeIndex","topOltDhcpSlotIndex",
            	"topOltDhcpPortIndex","portName","portTypeName","topOltDhcpPortCascade","topOltDhcpPortTrans","topOltDhcpPortTrust"]
        });
        //store.setDefaultSort("createTime", "ASC");
        //store.load({params: {entityId:entityId,portProtIndex:portProtIndex,start:0,limit: 999999}});
        store.baseParams={
       		entityId:entityId,
       		portProtIndex:portProtIndex
        }
        store.load();
        tbar = new Ext.Toolbar({
            items : [{
                text : "@COMMON.fetch@",
                cls: 'pL10 pT5 pB5',
                iconCls : "miniIcoEquipment",
                disabled : !refreshDevicePower,
                handler: function(){
                	refreshPort();
                }
            }]
        });
        grid = new Ext.grid.GridPanel({
        	margins: '0 10 0 10',
            stripeRows:true,
            cls:"normalTable",
            bodyCssClass: "normalTable",
            region: "center",
            store: store,
            tbar : tbar,
            cm: cm,
            viewConfig:{ forceFit: true }
        });
        viewPort = new Ext.Viewport({
            layout: "border",
            items: [grid]
        });
    
    });//end document.ready;
    function manuRender(value,p,record){
    	if(operationDevicePower) {
    		var type = record.data.topOltDhcpPortTypeIndex;
    		slot = record.data.topOltDhcpSlotIndex;
    		port = record.data.topOltDhcpPortIndex;
	    	var str = String.format('<a href="javascript:;" onclick="editFn({0},{1},{2})">@COMMON.edit@</a>',type,slot,port);
	    	return str;
    	} else {
    		return '<span>@COMMON.edit@</span>';
    	}
    	
    }
    //编辑;
    function editFn(type,slot,port){
    	top.createDialog("modalDlg", "@oltdhcp.editPort@", 800, 500, 
    			"/epon/oltdhcp/showModifyOltDhcpPort.tv?entityId="+entityId+"&&portProtIndex="+portProtIndex+
    					"&&portTypeIndex="+type+"&&slotIndex="+slot+"&&portIndex="+port, null, true, true,function(){
    		onRefreshClick();
    	});
    }
    
    function cascadeRender(value,p,record){
    	var cascade;
    	switch(value){
	    	case 1:
	    		cascade = '<img src="../../images/fault/trap_on.png" border=0 align=absmiddle>';
	    		break;
	    	case 2:
	    		cascade = '<img src="../../images/fault/trap_off.png" border=0 align=absmiddle>';
	    		break;
	    	default:
	    		cascade = "--"
    	}
    	return cascade;
    }
    
    function transRender(value,p,record){
    	var cascade = record.data.topOltDhcpPortCascade;
    	var trans;
    	if(cascade == 1){
    		switch(value){
	    	case 1:
	    		trans = "transparent";
	    		break;
	    	case 2:
	    		trans = "captured";
	    		break;
	    	default:
	    		trans = "--"
    		}
    	}else{
    		trans = "--"
    	}
    	return trans;
    }
    
    function trustRender(value,p,record){
    	var cascade = record.data.topOltDhcpPortCascade;
    	var trust;
    	if(cascade == 2){
    		switch(value){
	    	case 1:
	    		trust = '<img src="../../images/fault/trap_on.png" border=0 align=absmiddle>';
	    		break;
	    	case 2:
	    		trust = '<img src="../../images/fault/trap_off.png" border=0 align=absmiddle>';
	    		break;
	    	default:
	    		trust = "--"
    		}
    	}else{
    		trust = "--"
    	}
    	return trust;
    }
    
    function refreshPort(){
    	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshPort@....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/refreshOltDhcpPortList.tv",
	        method:"post",
	        data : {
				entityId : entityId,
				portProtIndex : portProtIndex
			},
	        dataType:'text',
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">@oltdhcp.refreshPort@@oltdhcp.success@！</b>'
	            });
	            window.location.reload();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshPort@@oltdhcp.fail@！");
	        }
	    });
    }
    
    function onRefreshClick(){
    	store.reload();
    }
</script>
</head>
    <body class="grayBg">
    
    </body>
</Zeta:HTML>