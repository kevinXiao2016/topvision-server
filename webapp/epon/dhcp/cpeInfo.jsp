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
    IMPORT js/EntityType
</Zeta:Loader>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var entityId = '${entityId}';
    var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
    var cm,columnModels,sm,store,grid,viewPort,tbar;
    
    Ext.onReady(function(){
        columnModels = [
            {header: "CPE IP", width:100, dataIndex: "topOltDhcpCpeIpIndex", align: "center", sortable: true},
            {header: "CPE MAC", width:100, dataIndex: "topOltDhcpCpeMac", align: "center", sortable: true},
            {header: "CPE VLAN", width:100, dataIndex: "topOltDhcpCpeVlan", align: "center", sortable: true},
            {header: "@DHCP.cpeInterfaceType@", width:100, dataIndex: "topOltDhcpCpePortType", align: "center", sortable: true, renderer:typeRender},
            {header: "@EPON.slot@", width:100, dataIndex: "topOltDhcpCpeSlot", align: "center", sortable: true, renderer:slotRender},
            {header: "@EPON.port@", width:100, dataIndex: "topOltDhcpCpePort", align: "center", sortable: true},
            {header: "ONU", width:100, dataIndex: "onuName", align: "center", sortable: true, renderer:onuRender},
            {header: "@DHCP.tenancy@", width:100, dataIndex: "remainingTimeStr", align: "center", sortable: false}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : false
            },
            columns: columnModels
        });
        store = new Ext.data.JsonStore({
            url: "/epon/oltdhcp/loadOltDhcpCpeList.tv",
            root: "data",
            totalProperty: "cpeCount",
            remoteSort: true,
            baseParams: {entityId:entityId},
            fields: ["entityId","topOltDhcpCpeIpIndex","topOltDhcpCpeMac","topOltDhcpCpeVlan","topOltDhcpCpePortType","topOltDhcpCpeSlot",
            	"topOltDhcpCpePort","topOltDhcpCpeOnu","onuName","topOltDhcpCpeRemainingTime","remainingTimeStr","typeId"]
        });
        store.on('load', function(store,records,options) {
        	$("#cpeNum").text(store.reader.jsonData['cpeCount']);
        });
        //store.setDefaultSort("dataIndex1", "ASC");
        store.load({params: {entityId:entityId,start:0,limit: pageSize}});
        tbar = new Ext.Toolbar({
            items : [{
                text : "@COMMON.fetch@",
                cls: 'pL10 pT5 pB5',
                iconCls : "miniIcoEquipment",
                disabled : !refreshDevicePower,
                handler: refresh
            },'->',{
            	xtype: 'component',
            	cls: 'pR10',
            	html: '<label class="blueTxt">@DHCP.cpeNum@@COMMON.maohao@<b id="cpeNum"></b></label>'
            }]
        });
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
            	region: 'north',
            	contentEl: 'topPart',
            	height: 60,
            	border: false
            },grid]
        });
        
        loadOltSlotIdList();//加载槽位号
    
    });//end document.ready;
    
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
    
    function slotChange() {
    	//清空端口号和onu下拉框
    	$("select#portContainer").empty();
    	$("select#onuContainer").empty();
    	$("select#portContainer").append("<option value=''>@COMMON.select@</option>");
    	$("select#onuContainer").append("<option value=''>@COMMON.select@</option>");
    	//设置端口下拉框
    	loadOltSlotPonIdList();
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
	            window.parent.showMessageDlg("@COMMON.tip@", "DHCP.loadPortFailed");
	        }
	    });
    }
    
    function portChange() {
    	//清空onu下拉框
    	$("select#onuContainer").empty();
    	$("select#onuContainer").append("<option value=''>@COMMON.select@</option>");
    	//设置ONU下拉框
    	loadOltSlotPonOnuIdList();
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
    
    function queryFn() {
    	var cpeIpIndex = $("#cpeIpIndex").val();
    	var cpeMac = $("#cpeMac").val();
    	var cpeVlan = $("#cpeVlan").val();
    	var cpePortType = $("select#cpePortType").val();
    	var cpeSlot = $("select#slotContainer").val();
    	var cpePort = $("select#portContainer").val();
    	var cpeOnu = $("select#onuContainer").val();
    	//加载
    	store.load({
    		params: {
    			entityId: entityId,
    			cpeIpIndex: cpeIpIndex,
    			cpeMac: cpeMac,
    			cpeVlan: cpeVlan,
    			cpePortType: cpePortType,
    			cpeSlot: cpeSlot,
    			cpePort: cpePort,
    			cpeOnu: cpeOnu,
    			start: 0,
    			limit: pageSize
    			}
    	});
    }
    
	function refresh(){
		window.top.showWaitingDlg("@COMMON.wait@", "@DHCP.refreshCpe@", 'ext-mb-waiting');
		$.ajax({
			url:"/epon/oltdhcp/refreshOltDhcpCpeList.tv?entityId="+entityId,
			method:"post",cache: false,dataType:'text',
			success:function (text) {
			    window.top.closeWaitingDlg();
			    top.afterSaveOrDelete({
			           title: '@COMMON.tip@',
			           html: '<b class="orangeTxt">@DHCP.refreshCpeSuc@</b>'
			    });
			    window.location.reload();
			},error:function(){
				window.top.closeWaitingDlg();
			    window.top.showMessageDlg("@COMMON.tip@", "@DHCP.refreshCpeFailed@");
			}
		});
	}
	
	function slotRender(value,p,record){
    	var portType = record.data.topOltDhcpCpePortType;
    	//当CPE接口类型为聚合口，显示为“--”
    	if(portType == 9) {
    		return "--";
    	}
    	var typeId=record.data.typeId;
        if(EntityType.is8602G_OLT(typeId)){
            value=1;
        }
    	return value;
    }
	
	function onuRender(value,p,record){
		var portType = record.data.topOltDhcpCpePortType;
		//当CPE接口类型不为ONU时，显示为“--”
		if(portType != 10) {
			return "--";
		}
    	return record.data.topOltDhcpCpeOnu;
    }
	
	function typeRender(value,p,record) {
		var portType = record.data.topOltDhcpCpePortType;
		switch (portType){
	    	case 2:return 'FE';
	    	case 3:return 'GE';
	    	case 4:return 'XE';
	    	case 9:return 'ethagg';
	    	case 10:return 'onu';
	    	default:return 'Unknow';
	    }
	}
</script>
</head>
    <body class="grayBg  clear-x-panel-body">
    	<div id="topPart" class="pL10">
    		<table>
    			<tr>
    				<td class="rightBlueTxt">CPE IP@COMMON.maohao@</td>
    				<td class="pR10">
    					<input type="text" id="cpeIpIndex" class="normalInput w120" />
    				</td>
    				<td class="rightBlueTxt">CPE MAC@COMMON.maohao@</td>
    				<td class="pR10">
    					<input type="text" id="cpeMac" class="normalInput w120" />
    				</td>
    				<td class="rightBlueTxt">CPE VLAN@COMMON.maohao@</td>
    				<td class="pR10">
    					<input type="text" id="cpeVlan" class="normalInput w120" />
    				</td>
    				<td class="rightBlueTxt">@DHCP.cpeInterfaceType@@COMMON.maohao@</td>
    				<td>
    					<select id="cpePortType" class="normalSel w120">
    						<option value= "">@COMMON.select@</option>
    						<option value= "2">FE</option>
    						<option value= "3">GE</option>
    						<option value= "4">XE</option>
    						<option value= "9">ethagg</option>
    						<option value= "10">onu</option>
    					</select>
    				</td>
    			</tr>
    			<tr>
    				<td class="rightBlueTxt">@EPON.slot@@COMMON.maohao@</td>
    				<td class="pR10">
    					<select id="slotContainer" onchange="slotChange()" class="normalSel w120">
    						<option value="">@COMMON.select@</option>
    					</select>
    				</td>
    				<td class="rightBlueTxt">@EPON.port@@COMMON.maohao@</td>
    				<td class="pR10">
    					<select id="portContainer" onchange="portChange()" class="normalSel w120">
    						<option value="">@COMMON.select@</option>
    					</select>
    				</td>
    				<td class="rightBlueTxt">ONU@COMMON.maohao@</td>
    				<td class="pR10">
    					<select id="onuContainer" class="normalSel w120">
    						<option value="">@COMMON.select@</option>
    					</select>
    				</td>
    				<td colspan="2">
    					<a href="javascript:;" onclick="queryFn()" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
    				</td>
    			</tr>
    		</table>
    	</div>
    </body>
</Zeta:HTML>