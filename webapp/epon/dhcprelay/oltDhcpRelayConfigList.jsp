<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
#DHCP_GLOBAL_PANEL {padding-left:2px;padding-top:15px;}
#DHCP_GLOBAL_CONFIG select {width:110px;}
</style>
<script type="text/javascript">
var _DEVICETYPE_ = ["","CM","HOST","MTA","STB"];
var _CABLESOURCEVERIFY_ = ["", "ON", "OFF"];
var _POLICY_ = ["", "primary", "policy", "strict"];
var entityId = ${entityId};
var grid = null;
var store = null;
var virtualIpGrid;
var cableSourceVerify;
var dhcpRelayEnable = '${dhcpRelaySwitch}';
var cameraSwitch = '${cameraSwitch}';
/**
 * 新增Relay配置
 */
function createDhcpRelayConfig(){
	var win = top.createDialog('createRelayConfig', 'DHCP Relay', 800, 500, '/epon/dhcprelay/showModifyDhcpRelayConfig.tv?entityId=' + entityId +"&action=1", null, false, true);
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
        var win = window.top.createDialog('createRelayConfig', 'DHCP Relay', 800, 500, '/epon/dhcprelay/showModifyDhcpRelayConfig.tv?entityId='+entityId+
	    		'&bundleInterface='+ bundleInterface + '&action=2', null, false, true);
        win.on('close', function(){
        	window.parent.dhcpRelay = null;
            window.parent.dhcpBaseConfig = null;
            window.parent.intIpList = null;
        });
    } else {
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.pleaseSelectFirst);
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
		window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.sureToDeleteConfig, function (type) {
		  if(type == 'ok'){
		  
			window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.DHCPRELAY.configuring, 'ext-mb-waiting');
		    $.ajax({
		      url: '/epon/dhcprelay/deleteDhcpRelayConfig.tv?entityId='+entityId+"&bundleInterface=" +bundleInterface,
		      type: 'post',
		      dataType:'json',
		      success: function(response) {
		            if(response.message == "success"){      
		            	top.closeWaitingDlg();
		              	  top.nm3kRightClickTips({
		      				title: I18N.RECYLE.tip,
		      				html: I18N.COMMON.deleteSuccess
		      			  });
		               // window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.delsuccess);
		                setTimeout(function (){
		                	window.top.closeWaitingDlg(I18N.RECYLE.tip);
		                	onRefresh();
		                }, 500);
		             }else{
		                 window.parent.showMessageDlg(I18N.RECYLE.tip, DHCPRELAY.delfailed );
		             }
		        }, error: function(response) {
		            window.parent.showMessageDlg(I18N.RECYLE.tip, DHCPRELAY.delfailed );
		        }, cache: false
		    });
		}
		});
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
    var vlan = record.data.vlanMapStr;
    var bundleArray = bundle.replace("bundle", "").split(".");
    var bundleId;
    if(bundleArray.length > 1){
        bundleId = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
    }else{
        bundleId = bundleArray[0];
    }
    return bundleId + "&nbsp&nbsp&nbsp&nbsp" + I18N.DHCPRELAY.policy + "@COMMON.maohao@" + _POLICY_[policy] + 
        "&nbsp&nbsp&nbsp&nbsp" + I18N.DHCPRELAY.dhcpCableSourceVerify + "@COMMON.maohao@" 
        + _CABLESOURCEVERIFY_[cableSourceVerify] + "&nbsp&nbsp&nbsp&nbsp VLAN: " +vlan;
}
function dhcpRelaySwitch(c, bool){
	var enable = bool ? 1 : 2;
	var tip = ["", I18N.DHCPRELAY.open, I18N.DHCPRELAY.close];
	if(dhcpRelayEnable != enable){
		window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, String.format(I18N.DHCPRELAY.switchDhcpRelayTip, tip[enable]), function (type) {			
			if(type=="ok"){
				window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.DHCPRELAY.configuring, 'ext-mb-waiting');
		    	$.ajax({
		            url: '/epon/dhcprelay/modifyDhcpRelaySwitch.tv?entityId='+entityId+"&dhcpRelaySwitch="+enable,
		            type: 'post',
		            success: function(response) {
		                  if(response.message == "success"){             
		                	  window.top.closeWaitingDlg(I18N.COMMON.waiting);
		                	  dhcpRelayEnable = enable;
		                  }else{
		                	  window.parent.showMessageDlg(I18N.RECYLE.tip, String.format(I18N.DHCPRELAY.switchDhcpRelayFailure, tip[enable]));
		                	  Ext.getCmp("relay-open").setValue(dhcpRelayEnable==1? true: false);
		                	  Ext.getCmp("relay-close").setValue(!(dhcpRelayEnable==1)? true: false);
		                  }
		              }, error: function(response) {
		            	  window.parent.showMessageDlg(I18N.RECYLE.tip, String.format(I18N.DHCPRELAY.switchDhcpRelayFailure, tip[enable]));
		            	  Ext.getCmp("relay-open").setValue(dhcpRelayEnable==1? true: false);
		              }, cache: false
		          });
		    }else{
		    	Ext.getCmp("relay-open").setValue(dhcpRelayEnable==1? true: false);
		    	Ext.getCmp("relay-close").setValue(!(dhcpRelayEnable==1)? true: false);
		    }
		});
	}
	if(bool){
		Ext.getCmp("addDhcpBundleTbar").enable();
        Ext.getCmp("modifyDhcpBundleTbar").enable();
        Ext.getCmp("delDhcpBundleTbar").enable();
	}else{
		Ext.getCmp("addDhcpBundleTbar").disable();
        Ext.getCmp("modifyDhcpBundleTbar").disable();
        Ext.getCmp("delDhcpBundleTbar").disable();		
	}   
}
function createDhcpRelayConfigGrid(){
    var columns = [              
              {header: "bundle", align:"center", sortable:false, hidden:true, dataIndex:"topCcmtsDhcpBundleInterface",renderer: renderBundle},
              {header: I18N.DHCPRELAY.entitytype,  align:"center", sortable:false, dataIndex:"deviceTypeStr"},
              {header: "Option60",  align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Str", renderer:changeLine},
              {header: I18N.DHCPRELAY.dhcpServer,  align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIpAddr", renderer:changeLine},
              {header: I18N.DHCPRELAY.dhcpGiaddr,  align:"center", sortable:false, dataIndex:"topCcmtsDhcpGiAddress"}
              
              ];
    var cm = new Ext.grid.ColumnModel({
    	columns:columns
    });
    var toolbar = [
        {xtype: 'label',text:"@DHCPRELAY.dhcpRelayConfig@" ,width: 20},
        {xtype: 'tbspacer', width: 20},
        {xtype: 'radio', width: 20, id:"relay-open",handler: dhcpRelaySwitch, name:'switcher',value: 1},
        {xtype:'label',text: I18N.DHCPRELAY.openDhcpRelay},
        {xtype: 'radio', width: 20, id:"relay-close", name:'switcher',value: 2},
        {xtype:'label',text: I18N.DHCPRELAY.closeDhcpRelay},
        {xtype: 'tbspacer', width: 20},
        {text: I18N.DHCPRELAY.add, id:'addDhcpBundleTbar',  iconCls: "bmenu_new", handler: createDhcpRelayConfig},
        {text: I18N.DHCPRELAY.modify, id: 'modifyDhcpBundleTbar',iconCls: "bmenu_edit", handler: modifyDhcpRelayConfig},
        {text: I18N.DHCPRELAY.deleteCfg, id: 'delDhcpBundleTbar',iconCls: "bmenu_delete", handler: deleteDhcpRelayConfig},
        '->',
        {text: I18N.DHCPRELAY.refreshDataFromEntity, id: 'refreshDhcpRelay', iconCls:"bmenu_equipment", handler: refreshDhcpRelayConfig}
	];
    var reader=new Ext.data.JsonReader({
        root:'data',
        fields:[
                'policy',
                'cableSourceVerify',
                'topCcmtsDhcpBundleInterface', 
                'topCcmtsDhcpHelperDeviceType', 
                'topCcmtsDhcpOption60Str', 
                'topCcmtsDhcpHelperIpAddr', 
                'topCcmtsDhcpGiAddress', 
                'topCcmtsDhcpGiAddrMask',
                'deviceTypeStr',
                'vlanMapStr'
                ]
    });
    store=new Ext.data.GroupingStore({  
    	id:'GroupStore', 
    	url:"/epon/dhcprelay/getDhcpRelayConfigList.tv?entityId=" + entityId,
        reader: reader,
        remoteSort: false,
        remoteGroup: false,
        sortInfo:{field: 'topCcmtsDhcpBundleInterface', direction: 'ASC'},
        groupField:'topCcmtsDhcpBundleInterface'
    });
    grid = new Ext.grid.GridPanel({
        stripeRows:true,region: "center",bodyCssClass: 'normalTable',
        store: store,
        tbar: toolbar,
        colModel: cm,
        view: new Ext.grid.GroupingView({forceFit: true})
    });
    store.load();
    
    var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
			{region:'north',
	 		 height: 37,
	 		 contentEl :'header',
	 		 cls:'clear-x-panel-body',
	 		 autoScroll: false
		}]
	}); 
}
function onRefresh(){
	store.reload();
}
//刷新DHCP Relay（bundle）配置
function refreshDhcpRelayConfig(){
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.EPON.loading, 'ext-mb-waiting');
    $.ajax({
      url: '/epon/dhcprelay/refreshDhcpRelayConfigFromDevice.tv?entityId='+entityId,
      type: 'post',
      success: function(response) {
            if(response.message == "success"){                  
                store.reload();
                window.parent.closeWaitingDlg();
             }else{
                 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.refreshFailure);
             }
        }, error: function(response) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.refreshFailure);
        }, cache: false
    });
}
Ext.onReady(function (){
    createDhcpRelayConfigGrid();
    Ext.getCmp("relay-open").setValue(dhcpRelayEnable==1? true: false);
    Ext.getCmp("relay-close").setValue(!(dhcpRelayEnable==1)? true: false);
    if(dhcpRelayEnable==1){
        Ext.getCmp("addDhcpBundleTbar").enable();
        Ext.getCmp("modifyDhcpBundleTbar").enable();
        Ext.getCmp("delDhcpBundleTbar").enable();
    }else{
        Ext.getCmp("addDhcpBundleTbar").disable();
        Ext.getCmp("modifyDhcpBundleTbar").disable();
        Ext.getCmp("delDhcpBundleTbar").disable();      
    }
    
    //操作权限控制
    if(!operationDevicePower){
        Ext.getCmp("addDhcpBundleTbar").disable();
        Ext.getCmp("modifyDhcpBundleTbar").disable();
        Ext.getCmp("delDhcpBundleTbar").disable();  
        Ext.getCmp("relay-open").disable();
        Ext.getCmp("relay-close").disable();
	 }
});
</script>
</head>
<body class="whiteToBlack">
    <div id=header>
       <%@ include file="/epon/inc/navigator.inc"%>
    </div>
</body>
</Zeta:HTML>
