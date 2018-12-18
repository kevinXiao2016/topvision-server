<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<%@include file="../include/meta.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="config"/>
<script type="text/javascript" 
src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = <s:property value='entityId'/>;
var sniMacAddrType = '<s:property value="sniMacAddrType"/>';
var store = null;
var grid = null;
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store, displayInfo: true,
        items: ["-", String.format(I18N.DHCP.everyPage, pageSize)
       ]});
   return pagingToolbar;
 
}
function AddDhcpServer(){
	if(store.getCount()<10){
	 	window.top.createDialog('dhcpServerAdd', I18N.DHCP.addService, 400, 280, 'epon/dhcp/showOltDhcpServerAdd.tv?entityId=' + entityId, null, true, true);
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.serviceMaxTip);
	}
}
	
function renderOperation(value, p, record){ 
	   var str ="<input type='image' src='../../images/delete.gif' onclick='deleteRule(\""+record.get('topOltDHCPServerIndex')+"\")'>";
	 //var str ="<input type='image' src='../../images/edit.gif' onclick='modifyConfig(\""+record.get('onuType')+"\",\""+record.get('topOltDHCPServerIndex')+"\",\""+record.get('topOltDHCPServerVid')+"\",\""+record.get('topOltDHCPServerIpAddr')+"\",\""+record.get('topOltDHCPServerIpMask')+"\")'>&nbsp&nbsp&nbsp<input type='image' src='../../images/delete.gif' onclick='deleteRule(\""+record.get('topOltDHCPServerIndex')+"\")'>";
	   return str;
	} 
function modifyConfig(onuType,dhcpServerIndex,dhcpServerVid,dhcpServerIp,dhcpServerIpMask){
	window.top.createDialog('dhcpServerModify', I18N.DHCP.modifyServer, 400, 280, 'epon/dhcp/showOltDhcpServerModify.tv?onuType='+onuType+'&dhcpServerIndex=' + dhcpServerIndex+'&dhcpServerVid=' + dhcpServerVid+'&dhcpServerIp=' + dhcpServerIp+'&dhcpServerIpMask=' + dhcpServerIpMask+'&entityId=' + entityId, null, true, true);
}

function deleteRule(dhcpServerIndex){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.DHCP.delThisConfirm, function (type) {
       if(type=="ok"){
    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.deleting);
    				jQuery.ajax({
    					type : "POST",
    					cache:false,
    		    		url: "epon/dhcp/deleteDhcpServerIndex.tv?entityId="+entityId+"&dhcpServerIndex="+dhcpServerIndex+"&c="+Math.random(),
    					dataType : "text",
    					success : function(text) {		 
    					    window.parent.closeWaitingDlg();
    						if (text != null && text != "deleteOK") {  
    							window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.delFailed, 'error');
    						} else {
    			                 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.delSuc);
    			                 store.load();
    					}
    					}, error: function(text) {
    						window.parent.closeWaitingDlg();
    						window.parent.showMessageDlg(I18N.COMMON.err, I18N.DHCP.delFailed, 'error')
    					}
    				});
           }
		})
}
function cancelClick() {
	window.parent.closeWindow('dhcpServerConfig');	
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
function checkMac(mac){
	var reg_name = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){0,5})+$/i;
	if(mac == "" ||mac == null){
		return false;
	}else{
		if (mac.length != 17) {
			return false;
		} else {
			if(reg_name.test(mac)){
				return true;
			}else{
				return false;
			}
		}
	}
}
Ext.onReady(function () {
	var w = document.body.clientWidth - 26;
	var h = document.body.clientHeight - 50;
	var cm = new Ext.grid.ColumnModel([
         {header: I18N.DHCP.eType, dataIndex:'onuType',width:80,align:"center"},
	     {header: 'Server ID', dataIndex:'topOltDHCPServerIndex',width:80,align:"center"},
	     {header: 'Server Vlan ID', dataIndex:'topOltDHCPServerVid',width:110,align:"center"},
	     {header: I18N.DHCP.serverAddr, dataIndex:'topOltDHCPServerIpAddr',width:130,align:"center"},
	     {header: I18N.DHCP.serverMask, dataIndex:'topOltDHCPServerIpMask',width:150,align:"center"},
	     {header: I18N.COMMON.manu,dataIndex:'topOltDHCPServerIndex',width:100,align:"center",renderer:renderOperation}	 
	     ]);
	  store = new Ext.data.JsonStore({
          url: 'epon/dhcp/loadDhcpServerBaseConfig.tv?entityId='+entityId,
          totalProperty: "totalProperty",   
          root: 'data',            
          remoteSort: false,
          fields: ['onuType','topOltDHCPServerIndex', 'topOltDHCPServerVid', 'topOltDHCPServerIpAddr', 'topOltDHCPServerIpMask','topOltDHCPServerIndex']
      });
      store.load();
      grid = new Ext.grid.GridPanel({
              height:h,
              width: w,
              renderTo: 'dhcpServer',
              store: store,
              bbar: buildPagingToolBar(),
              cm: cm
          });
})
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/dhcp/refreshDhcpServerConfig.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "refreshOK") {  
  				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr , 'error');
  			} else {
  	             window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
  	             window.location.reload();
  			} 
  		},
  		failure : function() {
  			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
  		},
  		params : {
  			entityId: entityId
  		}
  	});
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div class=formtip id=tips style="display: none"></div>
	<div id="dhcpServer"></div>
	<div align="right" style="padding-right: 5px; padding-top: 7px;">
			<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
				&nbsp;&nbsp;
			<button id="add" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="AddDhcpServer()"><fmt:message bundle="${i18n}" key="DHCP.addConfig" /></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
</BODY>
</HTML>