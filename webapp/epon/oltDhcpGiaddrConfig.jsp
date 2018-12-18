<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="config"/>
<script type="text/javascript" 
src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
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
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = <s:property value='entityId'/>;
var store = null;
var grid = null;
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
        items: ["-", String.format(I18N.DHCP.everyPage, pageSize)
       ]});
   return pagingToolbar;
}
function AddDhcpGiaddr(){ 
	if(store.getCount()<5){
	 	window.top.createDialog('dhcpGiaddrAdd', I18N.DHCP.giAddrAdd, 400, 250, 'epon/dhcp/showOltDhcpGiaddrAdd.tv?entityId=' + entityId, null, true, true);
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.giAddrAddTip);
	}
}
function renderOperation(value, p, record){ 
	   var str ="<input type='image' src='../../images/edit.gif' onclick='modifyConfig(\""+record.get('onuType')+"\",\""+record.get('topOltDHCPGiaddrIndex')+"\",\""+record.get('topOltDHCPGiaddrVid')+"\",\""+record.get('topOltDHCPGiaddrIpAddr')+"\")'>&nbsp&nbsp&nbsp<input type='image' src='../../images/delete.gif' onclick='deleteRule(\""+record.get('topOltDHCPGiaddrIndex')+"\")'>";
	   return str;
	} 
function modifyConfig(onuType,dhcpGiaddrIndex,dhcpGiaddrVid,dhcpGiaddrIp){
	window.top.createDialog('dhcpGiaddrModify', I18N.DHCP.giAddrModify, 400, 250, 'epon/dhcp/showOltDhcpGiaddrModify.tv?onuType='+onuType+'&dhcpGiaddrIndex=' + dhcpGiaddrIndex+'&dhcpGiaddrVid=' + dhcpGiaddrVid+'&dhcpGiaddrIp=' + dhcpGiaddrIp + '&entityId=' + entityId, null, true, true);
}

function deleteRule(dhcpGiaddrIndex){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.DHCP.giAddrDelConfirm, function (type) {
       if(type=="ok"){
    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.giAddrDeleting);
    				jQuery.ajax({
    					type : "POST",
    					cache:false,
    		    		url: "epon/dhcp/deleteDhcpGiaddrConfig.tv?entityId="+entityId+"&dhcpGiaddrIndex="+dhcpGiaddrIndex+"&c="+Math.random(),
    					dataType : "text",
    					success : function(text) {		 
    					    window.parent.closeWaitingDlg();
    						if (text != null && text != "deleteOK") {  
    							window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.delFailed , 'error');
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
	window.parent.closeWindow('dhcpGiaddrConfig');	
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
         {header:I18N.DHCP.eType, dataIndex:'onuType',width:80,align:"center"},
	     {header:I18N.DHCP.serviceGatewayId, dataIndex:'topOltDHCPGiaddrIndex',width:80,align:"center"},
	     {header:I18N.DHCP.allow + ' Vlan ID', dataIndex:'topOltDHCPGiaddrVid',width:120,align:"center"},
	     {header:I18N.DHCP.gateWayAddr, dataIndex:'topOltDHCPGiaddrIpAddr',width:150,align:"center"},
	     {header:I18N.COMMON.modify, dataIndex:'topOltDHCPGiaddrIndex',width:100,align:"center",renderer:renderOperation}	 
	     ]);
	  store = new Ext.data.JsonStore({
          url: 'epon/dhcp/loadDhcpGiaddrConfig.tv?entityId='+entityId,
          totalProperty: "totalProperty",   
          root: 'data',            
          remoteSort: false,
          fields: ['onuType','topOltDHCPGiaddrIndex', 'topOltDHCPGiaddrVid', 'topOltDHCPGiaddrIpAddr']
      });
      store.load();
      grid = new Ext.grid.GridPanel({
              height:h,
              width: w,
              renderTo: 'dhcpGiaddr',
              store: store,
              //bbar: buildPagingToolBar(),
              cm: cm
          });
})
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/dhcp/refreshDhcpGiaddrConfig.tv?r=' + Math.random(),
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
	<div id="dhcpGiaddr"></div>
	<div align="right" style="padding-right: 5px;padding-top:8px;">
			<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
				&nbsp;&nbsp;
			<button id="add" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="AddDhcpGiaddr()"><fmt:message bundle="${i18n}" key="DHCP.addConfig" /></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
</BODY>
</HTML>