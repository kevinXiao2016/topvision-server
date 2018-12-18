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
<script type="text/javascript" src="../../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery.autocomplete.min.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = <s:property value='entityId'/>;
var store = null;
var grid = null;
var ipData = new Array();
var macData = new Array();
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,displayInfo:true,
        items: ["-", String.format(I18N.DHCP.everyPage, pageSize) 
       ]});
   return pagingToolbar;
}
function AddDhcpStatic(){ 
	 window.top.createDialog('dhcpIpMacStaticAdd', I18N.DHCP.addBand, 380, 240, 'epon/dhcp/showOltDhcpIpMacStaticAdd.tv?entityId=' + entityId, null, true, true);
}
function renderOperation(value, p, record){ 
	   var str ="<input type='image' src='../../images/delete.gif' onclick='deleteRule(\""+record.get('topOltDHCPIpMacIdx')+"\")'>";
	   return str;
	} 
function changeIp(){
    var inputIp = $('#ipSelect').val();
	store.filterBy(function(record){
         return record.get('topOltDHCPIpAddr').indexOf(inputIp)>-1
		})
}
function changeMac(){
    var inputMac = $('#macSelect').val();
	store.filterBy(function(record){
         return record.get('topOltDHCPMacAddr').indexOf(inputMac)>-1
		})
}
function deleteRule(dhcpIpMacStaticIdx){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.DHCP.delBandConfirm, function (type) {
       if(type=="ok"){
    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.deleting);
    				jQuery.ajax({
    					type : "POST",
    					cache:false,
    		    		url: "epon/dhcp/deleteDhcpIpMacStaticConfig.tv?entityId="+entityId+"&dhcpIpMacStaticIdx="+dhcpIpMacStaticIdx+"&c="+Math.random(),
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
	window.parent.closeWindow('dhcpIpMacStatic');	
}
function onload(){
   $("#ipSelect").autocomplete(ipData,{width:260});
}
Ext.onReady(function () {
	var w = document.body.clientWidth - 26;
	var h = document.body.clientHeight - 95;
	var cm = new Ext.grid.ColumnModel([                              	
	     {header: I18N.DHCP.staticIndex, dataIndex:'topOltDHCPIpMacIdx',width:100,align:"center"},                          	
	     {header: I18N.DHCP.staticIp, dataIndex:'topOltDHCPIpAddr',width:150,align:"center"},
	     {header: I18N.DHCP.neMax, dataIndex:'topOltDHCPMacAddr',width:150,align:"center"},
	     {header: I18N.DHCP.onuAddr, dataIndex:'topOltDHCPOnuMacAddr',width:150,align:"center"},
	     {header: I18N.COMMON.del,dataIndex:'topOltDHCPIpMacIdx',width:80,align:"center",renderer:renderOperation}	 
	     ]);
     
	  store = new Ext.data.JsonStore({
          url: 'epon/dhcp/loadDhcpIpMacStaticConfig.tv?entityId='+entityId,
          totalProperty: "totalProperty",   
          root: 'data',            
          remoteSort: false,
          fields: ['topOltDHCPIpMacIdx','topOltDHCPIpAddr', 'topOltDHCPMacAddr', 'topOltDHCPOnuMacAddr']
      });
 	  store.load({callback : function(records, options, success) {
    	   $.each(records, function(i, n) {
    		   ipData[i]=n.data.topOltDHCPIpAddr ;
    		   macData[i]=n.data.topOltDHCPMacAddr;
        	  });
    	   $("#ipSelect").autocomplete(ipData,{width:260});
    	   $("#macSelect").autocomplete(macData,{width:260});
      }}); 
      grid = new Ext.grid.GridPanel({
              height:h,
              width: w,
              renderTo: 'dhcpStatic',
              store: store,
              cm: cm
          });
})
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/dhcp/refreshDhcpIpMacStaticConfig.tv?r=' + Math.random(),
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
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND  onload="onload()">
	<div class=formtip id=tips style="display: none"></div>
	<div id="dhcpIpSelect" 
		style="border:1px solid #66B3FF;padding-right: 8px; background-color:#ffffff;padding-top: 8px;padding-left: 20px;height:40px;">
	<option value="2"><fmt:message bundle="${i18n}" key="DHCP.queryIp" />: <input type="text" id="ipSelect" value="" onkeyup="changeIp()"/>
	<label style="padding-left:30px;"><option value="2"><fmt:message bundle="${i18n}" key="DHCP.queryMac" />: </label>
		<input type="text" id="macSelect" value="" onkeyup="changeMac()"/>
	</div>
	<div id="dhcpStatic" style=" padding-top: 10px;"></div>
		<div align="right" style="padding-right: 5px; padding-top: 5px;">
			<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><option value="2"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
				&nbsp;&nbsp;
			<button id="add" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="AddDhcpStatic()"><option value="2"><fmt:message bundle="${i18n}" key="DHCP.addConfig" /></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><option value="2"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
</BODY>
</HTML>