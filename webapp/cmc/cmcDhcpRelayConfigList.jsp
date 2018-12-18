<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-versioncontrol.js"></script>
<style type="text/css">

#DHCP_GLOBAL_CONFIG select {
    width:110px;
}
</style>
<script type="text/javascript">
vcEntityKey = 'cmcId';
var _DEVICETYPE_ = ["","CM","HOST","MTA","STB"];
var _CABLESOURCEVERIFY_ = ["", "ON", "OFF"];
var _POLICY_ = ["", "primary", "policy", "strict"];
var entityId = ${entityId};
var cmcId = ${cmcId};
var cmcDhcpBaseConfig = ${cmcDhcpBaseConfig};
var grid = null;
var store = null;
var virtualIpGrid = null;
var virtualIpStore = null;
var cableSourceVerify;
var topPanel = null;//顶部的那个基本配置;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;

/**
 * 新增Relay配置
 */
function createDhcpRelayConfig(){
	var win = window.top.createDialog('createRelayConfig', 'DHCP Relay', 800, 500, 'cmc/dhcprelay/showDhcpRelayConfigBaseStep.tv?entityId='+entityId + '&cmcId=' + cmcId + '&action=1', null, true, true);
	win.on('close', function(){
        window.parent.dhcpRelay = null;
        window.parent.dhcpBaseConfig = null;
        window.parent.intIpList = null;
    });
}
/**
 * 修改Relay配置
 */
function modifyDhcpRelayConfig(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var record = sm.getSelected();  
        var bundleInterface = record.data.topCcmtsDhcpBundleInterface;
        var win = window.top.createDialog('createRelayConfig', 'DHCP Relay', 800, 500, 'cmc/dhcprelay/showDhcpRelayConfigBaseStep.tv?entityId='+entityId+
	    		'&bundleInterface='+ bundleInterface + '&cmcId=' + cmcId + '&action=2', null, true, true);
        win.on('close', function(){
        	window.parent.dhcpRelay = null;
            window.parent.dhcpBaseConfig = null;
            window.parent.intIpList = null;
        });
    } else {
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.pleaseSelectFirst);
    }
}
/**
 * 删除Relay配置
 */
function deleteDhcpRelayConfig(){
	var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
    	var record = sm.getSelected();  
        var bundleInterface = record.data.topCcmtsDhcpBundleInterface;
		window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.sureToDeleteConfig, function (type) {	
			if(type == "ok"){
				window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
				$.ajax({
		              url: '/cmc/dhcprelay/deleteDhcpRelayConfig.tv?entityId='+entityId+"&bundleInterface=" +bundleInterface,
		              type: 'post',
		              dataType:'json',
		              success: function(response) {
		                    if(response.message == "success"){  
		                    	window.parent.closeWaitingDlg();
		                    	top.afterSaveOrDelete({
		               				title: I18N.COMMON.tip,
		               				html: '<b class="orangeTxt">'+I18N.COMMON.deleteSuccess+'</b>'
		               			});
		                        store.reload();
		                     }else{
		                         window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.delfailed );
		                     }
		                }, error: function(response) {
		                    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.delfailed );
		                }, cache: false
	            });
			}		    
		});
    } else {
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.pleaseSelectFirst);
    }
}
/**
 * Add option60
 */
function addDhcpOption60(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var record = sm.getSelected();  
        var bundleInterface = record.data.topCcmtsDhcpBundleInterface;
        if(record.data.topCcmtsDhcpOption60Str.length >= 4){
        	window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.dhcpRelayOptionNumTip);
        	return;
        }
        var deviceType = record.data.topCcmtsDhcpHelperDeviceType;
        var win = window.top.createDialog('dhcpOption60Modify', 'DHCP Relay', 600, 370, 
        		'cmc/dhcprelay/showAddOption60Config.tv?entityId='+entityId+
                '&bundleInterface='+ bundleInterface + '&deviceType=' + deviceType, null, true, true);
        win.on('close', function(){
            window.parent.dhcpRelay = null;
        });
    } else {
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.pleaseSelectFirst);
    }
}
/**
 * 添加 DHCP 服务器 
 */
function addDhcpServer(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
    	var record = sm.getSelected();  
    	var serverSize = record.data.serverSize;
    	if(serverSize >= 5){
    		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.dhcpRelayServerNumTip);
    		return;
    	}        
        var bundleInterface = record.data.topCcmtsDhcpBundleInterface;
        var deviceType = record.data.topCcmtsDhcpHelperDeviceType;
        var win = window.top.createDialog('dhcpServerModify', 'DHCP Relay', 600, 370, 
                'cmc/dhcprelay/showAddDhcpServerConfig.tv?entityId='+entityId+ '&cmcId=' + cmcId + 
                '&bundleInterface='+ bundleInterface + '&deviceType=' + deviceType, null, true, true);
        win.on('close', function(){
            window.parent.dhcpRelay = null;
        });
    } else {
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.pleaseSelectFirst);
    }
}
function changeLine(value, cellmate, record){
    //var temp = value.split(",");
    var str = "<table>";
    if(value){
        for(var i = 0; i<value.length; i++){
            str = str + "<tr>" + "<td align='center'>" + value[i] + "</td>" + "</tr>";
        }
    }
    str = str + "</table>";
    return str;
}
function renderBundle(value, cellmate, record){
    var bundle = record.data.topCcmtsDhcpBundleInterface;
    var policy = record.data.policy;
    var cableSourceVerify = record.data.cableSourceVerify;
    var bundleArray = bundle.replace("bundle", "").split(".");
    var bundleId;
    if(bundleArray.length > 1){
        bundleId = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
    }else{
        bundleId = bundleArray[0];
    }
    return "Relay " + bundleId + "&nbsp&nbsp&nbsp&nbsp" + I18N.CMC.text.policy + ":" + _POLICY_[policy] + 
        "&nbsp&nbsp&nbsp&nbsp" + I18N.CMC.label.dhcpCableSourceVerify + ":" 
        + _CABLESOURCEVERIFY_[cableSourceVerify];
}
function renderDeviceType(value, cellmate, record){
    return _DEVICETYPE_[value];
}
function vlanTagRender(value, cell, record){
	if(value == "0"){
		return "-";
	}else{
		return value;
	}
}
function vlanPriorityRender(value, cell, record){
	if(record.data.vlanTag == "0"){
		return "-"
	}else{
		return value;
	}
}
function cableSourceVerify_swichter(c,bool){
	cableSourceVerify = bool ? 1 : 2;
}
/**
 *全局配置Panel
 */
function showDhcpRelayGlobalConfig(){
	var w = document.body.clientWidth - 45;
	var checked = cableSourceVerify == 1? true:false;
	topPanel = new Ext.Panel({
        renderTo : "DHCP_GLOBAL_PANEL",
        height : 80,
        width : w,
        contentEl : "DHCP_GLOBAL_CONFIG",
        tbar : [
                "<span style='font-weight:bold'>" + I18N.CMC.text.basecfg + "</span>",
                {xtype: 'tbspacer', width: 20},
                //{xtype: 'radio', width: 20, handler: cableSourceVerify_swichter, name:'switcher',value: 1, checked: checked},
                //{xtype:'label',text: I18N.CMC.label.dhcpCableSourceVerifyOpen},
                //{xtype: 'radio', width: 20, name:'switcher', value: 2, checked: !checked},
                //{xtype:'label',text: I18N.CMC.label.dhcpCableSourceVerifyClose},
                '->',
                {xtype: 'button',text: I18N.CMC.title.refreshDataFromEntity, width: 20, handler: refreshDhcpBaseConfig, iconCls:"bmenu_equipment", id: 'refreshDhcpBaseConfig'},
                {xtype: 'button',text: I18N.CMC.label.modifycfg, width: 20, handler: onSaveClick, id: 'saveGlobal', iconCls:"bmenu_save"}
        ]
    });
}
function createDhcpRelayConfigGrid(){
	var w = $(window).width() * 2/3;
	var h = $(window).height() - 170;
    var columns = [              
              {header: I18N.CMC.label.config, width:w/15, align:"center", sortable:false, hidden:true, dataIndex:"bundleNum",renderer: renderBundle},
              {header: I18N.CMC.label.entitytype, width:parseInt(w/10), align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperDeviceType", renderer: renderDeviceType},
              {header: "Option60", width:parseInt(4*w/15), align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Str", renderer:changeLine},
              {header: I18N.CMC.label.dhcpServer, width:parseInt(2*w/10), align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIpAddr", renderer:changeLine},
              {header: I18N.CMC.label.dhcpGiaddr, width:parseInt(2*w/10), align:"center", sortable:false, dataIndex:"topCcmtsDhcpGiAddress"},
              {header: "VLAN Tag", width:parseInt(1*w/10), align:"center", sortable:false, dataIndex:"vlanTag", renderer: vlanTagRender},
              {header: I18N.CMC.label.vlanPriority, width:parseInt(w/10), align:"center", sortable:false, dataIndex:"vlanPriority", renderer: vlanPriorityRender}
              
              ];
    var cm = new Ext.grid.ColumnModel({
    	columns:columns
    });
    var toolbar = [
                   "<span style='font-weight:bold'>" + I18N.CMC.label.dhcpRelayConfig + "</span>",
                   {xtype: 'tbspacer', width: 20},
                   {text: I18N.CMC.label.addBundle,id: 'createDhcpRelayTbar', iconCls: "bmenu_new", handler: createDhcpRelayConfig},
                   {text: I18N.CMC.label.modifyBundle, id: 'modifyDhcpBundleTbar', iconCls: "bmenu_edit", handler: modifyDhcpRelayConfig},
                   {text: I18N.CMC.label.deleteBundle, id: 'delDhcpBundleTbar', iconCls: "bmenu_delete", handler: deleteDhcpRelayConfig}, '-',
                   {text: I18N.CMC.label.addOption60, id: 'addOption60Tbar', iconCls: "bmenu_new", handler: addDhcpOption60}, '-',
                   {text: I18N.CMC.label.addDhcpServer, id: 'addDhcpHelperTbar', iconCls: "bmenu_new", handler: addDhcpServer},
                   '->',
                   {text: I18N.CMC.title.refreshDataFromEntity, id: 'refreshDhcpRelay', iconCls:"bmenu_equipment", handler: refreshDhcpRelayConfig}
                   ];
    var reader=new Ext.data.JsonReader({
        root:'data',
        fields:[
                'bundleNum',
                'policy',
                'cableSourceVerify',
                'topCcmtsDhcpBundleInterface', 
                'topCcmtsDhcpHelperDeviceType', 
                'topCcmtsDhcpOption60Str', 
                'topCcmtsDhcpHelperIpAddr', 
                'topCcmtsDhcpGiAddress', 
                'topCcmtsDhcpGiAddrMask',
                'vlanTag',
                'vlanPriority',
                'serverSize'
                ]
    });
    store=new Ext.data.GroupingStore({  
    	id:'GroupStore', 
    	url:"/cmc/dhcprelay/getDhcpRelayConfigList.tv?entityId=" + entityId,
        reader: reader,
        remoteSort:false,
        remoteGroup:false,
        sortInfo:{field: 'bundleNum', direction: 'ASC'},
        groupField:'bundleNum'
    });
    var headerH = $("#header").outerHeight();
    var topPanelH = 80;
    var subH = $(window).height() - headerH - topPanelH - 30;
    grid = new Ext.grid.GridPanel({
        renderTo: 'dhcpConfigInfo-div',
        height:subH,
        bodyCssClass:"normalTable",
        border: true,
        trackMouseOver:false,
        store: store,
        tbar: toolbar,
        colModel: cm,
        view: new Ext.grid.GroupingView({
        	forceFit:true
        })
    });    
    grid.on('rowclick', function(grid, rowIndex, e) {
        var record = grid.getStore().getAt(rowIndex);
        var bundleInterface = record.get('topCcmtsDhcpBundleInterface');
        virtualIpStore.load({params: {'entityId':entityId, 'bundleInterface': bundleInterface}});
    });
    store.load();
}
function createVirtualIpGrid(){
	var w = document.body.clientWidth /3 - 60;
    var h = document.body.clientHeight - 170;
    var headerH = $("#header").outerHeight();
    var topPanelH = 80;
    var subH = $(window).height() - headerH - topPanelH - 30;
    var columns = [
            {header: I18N.CMC.label.IP, width:150, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpAddr"},
            {header: I18N.CMC.label.iPMask, width:150, align:"center", sortable:false, dataIndex:"topCcmtsDhcpIntIpMask"}
            ];
    var cm = new Ext.grid.ColumnModel({
        columns:columns
    });
    virtualIpStore = new Ext.data.JsonStore({  
        url:"/cmc/dhcprelay/getDhcpVirtualIpList.tv",
        root:"data",
        fields:[
                'topCcmtsDhcpIntIpIndex',
                'topCcmtsDhcpIntIpAddr', 
                'topCcmtsDhcpIntIpMask'
                ]
    });
    virtualIpStore.setDefaultSort('topCcmtsDhcpIntIpIndex', 'ASC');
    
    virtualIpGrid = new Ext.grid.GridPanel({
        renderTo: 'dhcpVirtualIp-div',
        bodyCssClass:'normalTable',
        height:subH,
        width:300,
        viewConfig:{
        forceFit:true
        },
        border: true,
        trackMouseOver:false,
        store: virtualIpStore,
        colModel: cm,
        tbar : [
                "<span style='font-weight:bold'>" + I18N.CMC.label.virtualIp + "</span>",
                {xtype: 'tbspacer', width: 20}
        ]
        
    });    
    virtualIpStore.load();
}
function loadRelayMode(){
	if(cmcDhcpBaseConfig){
		cableSourceVerify = cmcDhcpBaseConfig.cableSourceVerify;
		if(cableSourceVerify && 
                cableSourceVerify == 2){
            $(":radio[name='cableSourceVerify'][value=2]").attr("checked",true);
            
        }else{
            $(":radio[name='cableSourceVerify'][value=1]").attr("checked",true);
        }
		$("#cmRelayMode").val(cmcDhcpBaseConfig.cmMode);
	    $("#hostRelayMode").val(cmcDhcpBaseConfig.hostMode);
	    $("#mtaRelayMode").val(cmcDhcpBaseConfig.mtaMode);
	    $("#stbRelayMode").val(cmcDhcpBaseConfig.stbMode);	    
	}
}
function checkModifyBaseConfig(){
	if(cmcDhcpBaseConfig){
	    var cmRelayMode = $("#cmRelayMode").val();
	    var hostRelayMode = $("#hostRelayMode").val();
	    var mtaRelayMode = $("#mtaRelayMode").val();
	    var stbRelayMode = $("#stbRelayMode").val();
	    if(cableSourceVerify != cmcDhcpBaseConfig.cableSourceVerify){
	        $("#bmenu_save").attr("disabled", false);
	        return;
	    }
	    if(cmRelayMode != cmcDhcpBaseConfig.cmMode || hostRelayMode != cmcDhcpBaseConfig.hostMode
	            || mtaRelayMode != cmcDhcpBaseConfig.mtaMode || stbRelayMode != cmcDhcpBaseConfig.stbMode){
	        $("#bmenu_save").attr("disabled", false);
	        return ;
	    }
	    $("#bmenu_save").attr("disabled", true);
	}else{
		$("#bmenu_save").attr("disabled", false);
	}
	
}
function onRefresh(){
    location.reload();
}
function onSaveClick(){
    cableSourceVerify = $(":radio[name='cableSourceVerify'][checked=true]").val();
	var cmRelayMode = $("#cmRelayMode").val();
	var hostRelayMode = $("#hostRelayMode").val();
	var mtaRelayMode = $("#mtaRelayMode").val();
	var stbRelayMode = $("#stbRelayMode").val();
	var mode = (cmRelayMode << 6) | (hostRelayMode << 4) | (mtaRelayMode << 2) | stbRelayMode;
	var modeStr = (mode<16? ("0"+ mode.toString(16)):mode.toString(16)) + ":00";
	var str = "";
	str = "modifyCmcDhcpBaseConfig.cableSourceVerify=" + cableSourceVerify;
	str = str + "&";
	str = str + "modifyCmcDhcpBaseConfig.topCcmtsDhcpMode=" + modeStr;
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.CMC.tip.sureToModifyConfig, function (type) {
	    if(type=="ok"){
	        window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
	        $.ajax({
	          url: '/cmc/dhcprelay/modifyDhcpRelayBase.tv?entityId='+entityId,
	          type: 'post',
	          data: str,
	          success: function(response) {
	                //response = eval("(" + response + ")");
	                if(response.message == "success"){                  
	                    //window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.modifySuccess);
	                    window.parent.closeWaitingDlg();
	                    top.afterSaveOrDelete({
	           				title: I18N.RECYLE.tip,
	           				html: '<b class="orangeTxt">'+I18N.CMC.tip.modifySuccess+'</b>'
	           			});
	                 }else{
	                     window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.modifyFail);
	                 }
	            }, error: function(response) {
	                window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.modifyFail);
	            }, cache: false
	        });
	    }
	});
}
//刷新DHCP基本配置
function refreshDhcpBaseConfig(){
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
      url: '/cmc/dhcprelay/refreshDhcpBaseConfigFromDevice.tv?entityId='+entityId,
      type: 'post',
      success: function(response) {
            //response = eval("(" + response + ")");
            if(response.message == "success"){                
            	window.parent.closeWaitingDlg();
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">'+I18N.CMC.tip.refreshSuccess+'</b>'
       			});
                onRefresh();
             }else{
                 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.refreshFailure);
             }
        }, error: function(response) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.refreshFailure);
        }, cache: false
    });
}
//刷新DHCP Relay（bundle）配置
function refreshDhcpRelayConfig(){
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
      url: '/cmc/dhcprelay/refreshDhcpRelayConfigFromDevice.tv?entityId='+entityId,
      type: 'post',
      success: function(response) {
            //response = eval("(" + response + ")");
            if(response.message == "success"){   
            	window.parent.closeWaitingDlg();
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">'+I18N.CMC.tip.refreshSuccess+'</b>'
       			});
                store.reload();
             }else{
                 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.refreshFailure);
             }
        }, error: function(response) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.refreshFailure);
        }, cache: false
    });
}

</script>
</head>
<body class="whiteToBlack" >
    <div id=header>
        <%@ include file="entity.inc"%>
    </div>
    <div class="edge10">
	    <div id="DHCP_GLOBAL_PANEL"></div>    	    
	    
	    <div id="DHCP_GLOBAL_CONFIG">
	           <form name="formChanged" id="formChanged">
	            <table cellspacing=5 cellpadding=0 style='margin-left:20px; margin-top: 10px'>
	                <tr>
	                <td width=110  align="right"><label for="cableSourceVerify"><fmt:message bundle="${cmc}" key="CMC.label.dhcpCableSourceVerify"/>: </label></td>
	                <td>
	                    <span style="width:120px">
	                        <input name='cableSourceVerify' type="radio" value=1 /><fmt:message bundle="${cmc}" key="CMC.select.open"/>&nbsp;
	                        <input name='cableSourceVerify' type="radio" value=2 /><fmt:message bundle="${cmc}" key="CMC.select.close"/>  
	                    </span>             
	                </td>
	                <td width=35 align="right"><span>CM:</span>
	                </td>
	                <td><select id="cmRelayMode" onchange="checkModifyBaseConfig()"
	                    name="modifyCmcDhcpBaseConfig.cmMode">
	                        <option value="0"><fmt:message bundle="${cmc}" key="CMC.label.dhcpSnooping"/></option>
	                        <option value="1"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL2Relay"/></option>
	                        <option value="2"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL3Relay"/></option>
	                </select>
	                </td>
	                <td width=35 align="right"><span>HOST:</span>
	                </td>
	                <td><select id="hostRelayMode" onchange="checkModifyBaseConfig()"
	                    name="modifyCmcDhcpBaseConfig.hostMode">
	                        <option value="0"><fmt:message bundle="${cmc}" key="CMC.label.dhcpSnooping"/></option>
	                        <option value="1"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL2Relay"/></option>
	                        <option value="2"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL3Relay"/></option>
	                </select>
	                </td>
	                <td width=35 align="right"><span>MTA:</span>
	                </td>
	                <td><select id="mtaRelayMode" onchange="checkModifyBaseConfig()"
	                    name="modifyCmcDhcpBaseConfig.mtaMode">
	                        <option value="0"><fmt:message bundle="${cmc}" key="CMC.label.dhcpSnooping"/></option>
	                        <option value="1"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL2Relay"/></option>
	                        <option value="2"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL3Relay"/></option>
	                </select>
	                </td>
	                <td width=35 align="right"><span>STB:</span>
	                </td>
	                <td><select id="stbRelayMode" onchange="checkModifyBaseConfig()"
	                    name="modifyCmcDhcpBaseConfig.stbMode">
	                        <option value="0"><fmt:message bundle="${cmc}" key="CMC.label.dhcpSnooping"/></option>
	                        <option value="1"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL2Relay"/></option>
	                        <option value="2"><fmt:message bundle="${cmc}" key="CMC.label.dhcpL3Relay"/></option>
	                </select>
	                </td>
	                </tr>
	            </table>
	        </form>
	    </div>
	   <div style="margin:10px 310px 0px 0px">
	   		<div id="dhcpConfigInfo-div"  ></div>
	   </div>
	</div>	
	
	<div id="dhcpVirtualIp-div" style="position:absolute; top:137px; right:10px;"></div>	
	
	<script type="text/javascript" src="/js/jquery/jquery.wresize.js" ></script>
	<script type="text/javascript" >
	function onWindowResize(){
        var headerH = $("#header").outerHeight();
        var topPanelH = 80;
        var subH = $(window).height() - headerH - topPanelH - 30;
        var subLeftW = $(window).width() - 330;
        
        var w = $(window).width() - 20;
        var h = $(window).height();
        topPanel.setWidth(w);
        
        virtualIpGrid.setHeight(subH);
        grid.setSize(subLeftW,subH);
        
    }
	Ext.onReady(function (){
		var headerH = $("#header").outerHeight();
		var topPanelH = 80;
		var subH = $(window).height() - headerH - topPanelH - 30;
		loadRelayMode();
	    showDhcpRelayGlobalConfig();
	    createDhcpRelayConfigGrid();
	    createVirtualIpGrid();
	    onWindowResize();
		$(window).wresize(onWindowResize);//wresize;
		
		if(!operationDevicePower){
			//基本配置
	        $("[name='cableSourceVerify']").attr("disabled",true);
	        $("#cmRelayMode").attr("disabled",true);
	        $("#hostRelayMode").attr("disabled",true);
	        $("#mtaRelayMode").attr("disabled",true);
	        $("#stbRelayMode").attr("disabled",true);
	        $("#saveGlobal").attr("disabled",true);
	        Ext.getCmp('saveGlobal').disable();
	        
			Ext.getCmp('createDhcpRelayTbar').disable();
			Ext.getCmp('modifyDhcpBundleTbar').disable();
			Ext.getCmp('delDhcpBundleTbar').disable();
			Ext.getCmp('addOption60Tbar').disable();
			Ext.getCmp('addDhcpHelperTbar').disable();
		}
	})
	</script>
</body>
</html>