<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module oltdhcp
    IMPORT js/nm3k/Nm3kSwitch
    IMPORT js/utils/IpUtil
    IMPORT js/EntityType
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var entityId = '${entityId}';
    var sourceVerifyEnable = parseInt('${sourceVerifyEnable}',10); //开关的状态, 1表示开，0表示关;
    var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
    var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
    var cm,columnModels,sm,store,grid,viewPort,tbar,bbar;
    
    Ext.onReady(function(){
    	var imgBtn = new Nm3kSwitch("putSwitch", sourceVerifyEnable, {
    		yesNoValue: [1, 2],
    		afterChangeCallback: function(selectValue){
    			setStaticIpSwitch(selectValue);
    		}
    	});
    	imgBtn.init();
    	if(!operationDevicePower) {
    		imgBtn.setDisabled(true);
    	}
    	
        columnModels = [
            {header: "@DHCP.ipAddress@", width:100, dataIndex: "ipIndex", align: "center", sortable: true},
            {header: "@DHCP.mask@", width:100, dataIndex: "maskIndex", align: "center", sortable: true},
            {header: "@EPON.slot@", width:100, dataIndex: "topOltDhcpStaticIpSlot", align: "center", sortable: true,renderer:slotRender},
            {header: "@EPON.port@", width:100, dataIndex: "topOltDhcpStaticIpPort", align: "center", sortable: true},
            {header: "ONU", width:100, dataIndex: "topOltDhcpStaticIpOnu", align: "center", sortable: true},
            {header: "@COMMON.manu@", width:100, dataIndex: "dataIndex5", align: "center", sortable: false,renderer:manuRender}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : false
            },
            columns: columnModels
        });
        store = new Ext.data.JsonStore({
            url: "/epon/oltdhcp/loadOltDhcpStaticIpList.tv",
            root: "data",
            totalProperty: "ipCount",
            remoteSort: true,
            baseParams: {entityId:entityId},
            fields: ["entityId","ipIndex","maskIndex","topOltDhcpStaticIpSlot","topOltDhcpStaticIpPort","topOltDhcpStaticIpOnu","typeId"]
        });
        //store.setDefaultSort("dataIndex1", "ASC");
        store.load({params: {entityId:entityId,start:0,limit: pageSize}});
        createToolbar();
        bbar = new Ext.PagingToolbar({
            id: "extPagingBar",
            pageSize: pageSize,
            store: store,
            displayInfo: true,
            items: ["-", String.format("@COMMON.displayPerPage@", pageSize), "-"]
        });
        grid = new Ext.grid.GridPanel({
        	margins: '0 10 0 10',
            stripeRows:true,
            cls:"normalTable",
            bodyCssClass: "normalTable",
            region: "center",
            store: store,
            bbar : bbar,
            tbar : tbar,
            cm: cm,
            viewConfig:{ forceFit: true }
        });
        viewPort = new Ext.Viewport({
            layout: "border",
            items: [{
            	border: false,
            	region: "north",
            	contentEl: 'topPart',
            	height: 30
            },grid]
        });
        loadOltSlotIdList();
    });//end document.ready;
    
    function setStaticIpSwitch(selectValue) {
    	var tip;
    	if(selectValue == 1){
    		tip = "@oltdhcp.open@";
    	}else {
    		tip = "@oltdhcp.close@";
    	}
		window.top.showWaitingDlg("@COMMON.wait@", tip + "@oltdhcp.staticIpConfig@", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/modifySourceVerifyEnable.tv?entityId="+entityId+"&&sourceVerifyEnable="+selectValue,
	        method:"post",cache: false,dataType:'text',
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">'+tip+'@oltdhcp.staticIpConfig@@oltdhcp.success@！</b>'
	            });
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", tip+"@oltdhcp.staticIpConfig@@oltdhcp.fail@！");
	        }
	    });
    }
    
    
    
    function createToolbar(){
    	tbar = new Ext.Toolbar({
            items : [{
                html : "@EPON.bdSlot@@COMMON.maohao@",
                xtype : "component",
                cls: 'blueTxt'
            },{
            	xtype : "component",
            	html: "<select id='slotContainer' class='normalSel w100' onchange='slotSelected()'><option value=''>@COMMON.select@</option></select>"
            },{
                html : "@EPON.port@@COMMON.maohao@",
                xtype : "component",
                cls: 'blueTxt mL10'
            },{
            	xtype : "component",
            	html: "<select id='portContainer' class='normalSel w100' onchange='portSelected()'><option value=''>@COMMON.select@</option></select>"
            },{
                html : "ONU@COMMON.maohao@",
                xtype : "component",
                cls: 'blueTxt mL10'
            },{
            	xtype : "component",
            	html: "<select id='onuContainer' class='normalSel w100' onchange='onuSelected()'><option value=''>@COMMON.select@</option></select>"
            },{
            	html : "IP@COMMON.maohao@",
                xtype : "component",
                cls: 'blueTxt mL10'
            },{
            	xtype : "component",
            	html: '<input id="ipInput" type="text" class="normalInput w120"/>'
            },{
            	html : "@DHCP.mask@@COMMON.maohao@",
                xtype : "component",
                cls: 'blueTxt mL10'
            },{
            	xtype : "component",
            	html: '<input id="maskInput" type="text" class="normalInput w120"/>'
            },{
            	text: '@COMMON.add@',
            	iconCls: 'miniIcoAdd',
            	cls: 'mL5',
            	disabled: !operationDevicePower,
            	handler: addAntiStaticIp
            }]
        });
    }
    
    function refresh(){
    	 window.top.showWaitingDlg("@COMMON.wait@", "@DHCP.refreshStaticIp@", 'ext-mb-waiting');
    	    $.ajax({
    	        url:"/epon/oltdhcp/refreshOltDhcpStaticIpList.tv?entityId="+entityId,
    	        method:"post",cache: false,dataType:'text',
    	        success:function (text) {
    	            window.top.closeWaitingDlg();
    	            top.afterSaveOrDelete({
    	                   title: '@COMMON.tip@',
    	                   html: '<b class="orangeTxt">@DHCP.refreshStaticIpSuc@</b>'
    	            });
    	            window.location.reload();
    	        },error:function(){
    	        	window.top.closeWaitingDlg();
    	            window.top.showMessageDlg("@COMMON.tip@", "@DHCP.refreshStaticIpFailed@");
    	        }
    	    });
    }
    
    function manuRender(value,p,record){
    	if(operationDevicePower) {
    		var entityId = record.data.entityId;
        	var ipIndex = record.data.ipIndex;
        	var maskIndex = record.data.maskIndex;
        	var str = String.format('<a href="javascript:;" onclick="deletFn({0},\'{1}\',\'{2}\')">@COMMON.delete@</a>',entityId,ipIndex,maskIndex);
        	return str;
    	} else {
    		return '<span>@COMMON.delete@</span>';
    	}
    	
    }
    
    function slotRender(value,p,record){
        var typeId=record.data.typeId;
        if(EntityType.is8602G_OLT(typeId)){
            value=1;
        }
        return value;
    }
    
    function deletFn(entityId,ipIndex,maskIndex) {
    	window.top.showConfirmDlg("@COMMON.tip@", "@DHCP.delThisConfirm@", function(type) {
			if (type == 'no') {return;}
			window.top.showWaitingDlg("@COMMON.wait@", "@DHCP.deleting@",'waitingMsg','ext-mb-waiting');
			Ext.Ajax.request({
				url: '/epon/oltdhcp/deleteOltDhcpStaticIp.tv',
				success: function() {
					reload();
			    	window.top.closeWaitingDlg();
			    	top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@DHCP.delSuc@</b>'
	   		    	});
		    	},
			    failure: function(){
			    	window.top.closeWaitingDlg();
			    	window.top.showMessageDlg("@COMMON.error@", "@DHCP.delFailed@！", 'error');
			    },
			    params: {entityId:entityId,ipIndex:ipIndex,maskIndex:maskIndex}
			});			
		});
    }
    
    function reload() {
    	var staticIpSlot = $("select#slotContainer").val();
    	var staticIpPort = $("select#portContainer").val();
    	var staticIpOnu = $("select#onuContainer").val();
    	store.load({
    		params: {
	    		entityId:entityId,
	    		staticIpSlot:staticIpSlot,
	    		staticIpPort:staticIpPort,
	    		staticIpOnu:staticIpOnu,
	    		start:0,
	    		limit: pageSize
    		}
    	});
    }
    
    function loadOltSlotIdList() {
    	$.ajax({
	        url:"/epon/oltdhcp/loadOltSlotIdList.tv?entityId="+entityId,
	        method:"post",cache: false,dataType:'text',
	        success:function (data) {
	            data = JSON.parse(data);
	            for(var i = 0;i < data.length ;i++){             
	            	$("select#slotContainer").append("<option value="+data[i]+">"+data[i]+"</option>");
		        }
	        },
	        error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@DHCP.loadSlotFailed@");
	        }
	    });
    }
    
    function loadOltSlotPonIdList() {
    	var staticIpSlot = $("select#slotContainer").val();
    	$.ajax({
	        url:"/epon/oltdhcp/loadOltSlotPonIdList.tv?entityId="+entityId+"&staticIpSlot="+staticIpSlot,
	        method:"post",cache: false,dataType:'text',
	        success:function (data) {
	            data = JSON.parse(data);
	            for(var i = 0;i < data.length ;i++){             
	            	$("select#portContainer").append("<option value="+data[i]+">"+data[i]+"</option>");
		        }
	        },
	        error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@DHCP.loadPortFailed@");
	        }
	    });
    }
    
    function loadOltSlotPonOnuIdList() {
    	var staticIpSlot = $("select#slotContainer").val();
    	var staticIpPort = $("select#portContainer").val();
    	$.ajax({
	        url:"/epon/oltdhcp/loadOltSlotPonOnuIdList.tv?entityId="+entityId+"&staticIpSlot="+staticIpSlot+"&staticIpPort="+staticIpPort,
	        method:"post",cache: false,dataType:'text',
	        success:function (data) {
	            data = JSON.parse(data);
	            for(var i = 0;i < data.length ;i++){             
	            	$("select#onuContainer").append("<option value="+data[i]+">"+data[i]+"</option>");
		        }
	        },
	        error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@DHCP.loadOnuFailed@");
	        }
	    });
    }
    
    function slotSelected() {
    	//清空端口号和onu下拉框
    	$("select#portContainer").empty();
    	$("select#onuContainer").empty();
    	$("select#portContainer").append("<option value=''>@COMMON.select@</option>");
    	$("select#onuContainer").append("<option value=''>@COMMON.select@</option>");
    	//重新加载列表
    	store.baseParams={
   			entityId: entityId, 
   			staticIpSlot: $("select#slotContainer").val(),
   			staticIpPort: $("select#portContainer").val(),
   			staticIpOnu: $("select#onuContainer").val(),
   			start: 0,
   			limit: pageSize
   		};
    	store.load();
    	//设置端口下拉框
    	loadOltSlotPonIdList();
    }
    
    function portSelected() {
    	//清空onu下拉框
    	$("select#onuContainer").empty();
    	$("select#onuContainer").append("<option value=''>@COMMON.select@</option>");
    	//重新加载列表
    	store.baseParams={
   			entityId: entityId, 
   			staticIpSlot: $("select#slotContainer").val(),
   			staticIpPort: $("select#portContainer").val(),
   			staticIpOnu: $("select#onuContainer").val(),
   			start: 0,
   			limit: pageSize
   		};
    	store.load();
    	//设置ONU下拉框
    	loadOltSlotPonOnuIdList();
    }
    
    function onuSelected() {
    	//重新加载列表
    	store.baseParams={
   			entityId: entityId, 
   			staticIpSlot: $("select#slotContainer").val(),
   			staticIpPort: $("select#portContainer").val(),
   			staticIpOnu: $("select#onuContainer").val(),
   			start: 0,
   			limit: pageSize
   		};
    	store.load();
    }
    
    function addAntiStaticIp() {
    	var staticIpSlot = $("select#slotContainer").val();
    	var staticIpPort = $("select#portContainer").val();
    	var staticIpOnu = $("select#onuContainer").val();
    	var ipIndex = $("#ipInput").val();
    	var maskIndex = $("#maskInput").val();
    	//校验
    	if(staticIpSlot == null || staticIpSlot == '') {
    		return window.top.showMessageDlg("@COMMON.tip@", "@DHCP.pleaseSelectSlot@");
    	}
    	if(staticIpPort == null || staticIpPort == '') {
    		return window.top.showMessageDlg("@COMMON.tip@", "@DHCP.pleaseSelectPort@");
    	}
    	if(staticIpOnu == null || staticIpOnu == '') {
    		return window.top.showMessageDlg("@COMMON.tip@", "@DHCP.pleaseSelectOnu@");
    	}
    	if(!IpUtil.isIpAddress(ipIndex)) {
    		return window.top.showMessageDlg("@COMMON.tip@", "@DHCP.pleaseEnterCorrectIP@");
    	}
    	if(!IpUtil.isMaskAddress(maskIndex)) {
    		return window.top.showMessageDlg("@COMMON.tip@", "@DHCP.pleaseEnterCorrectMask@");
    	}
    	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@",'waitingMsg','ext-mb-waiting');
    	$.ajax({
	        url:"/epon/oltdhcp/addOltDhcpStaticIp.tv",
	        method:"post",
	        data: {
	        	entityId:entityId,
	        	staticIpSlot:staticIpSlot,
	        	staticIpPort:staticIpPort,
	        	staticIpOnu:staticIpOnu,
	        	ipIndex:ipIndex,
	        	maskIndex:maskIndex
	        },
	        cache: false,
	        dataType:'text',
	        success:function (data) {
	        	window.top.closeWaitingDlg();
	            data = JSON.parse(data);
	            if(data.msg == "SUCCESS") {
	            	store.reload();
	            	top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@DHCP.addStaticIpSuc@</b>'
	   		    	});
	            } else if(data.msg == "Exist"){
	            	window.top.showMessageDlg("@COMMON.tip@", "@DHCP.StaticIpExist@");
	            } else if(data.msg == "Full") {
	            	window.top.showMessageDlg("@COMMON.tip@", "@DHCP.StaticIpFull@");
	            } else {
	            	window.top.showMessageDlg("@COMMON.tip@", "@DHCP.addStaticIpFailed@");
	            }
	        },
	        error:function(){
	        	window.top.closeWaitingDlg();
	            window.top.showMessageDlg("@COMMON.tip@", "@DHCP.addStaticIpFailed@");
	        }
	    });
    }
    
    function authLoad() {
		if(!refreshDevicePower) {
			$("#refreshBtn").attr("disabled",true);
		}
	} 
</script>
</head>
    <body class="grayBg  clear-x-panel-body" onload="authLoad()">
    	<ul id="topPart" class="leftFloatUl pL10 pR10">
    		<li class="blueTxt pT5">
    			@DHCP.staticIpSwitch@@COMMON.maohao@
    		</li>
    		<li id="putSwitch" class="pT4 pR10">
    			
    		</li>
    		<li>
    			<a id="refreshBtn" href="javascript:;" onclick="refresh();" class="normalBtn"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a>
    		</li>
    	</ul>
    </body>
</Zeta:HTML>