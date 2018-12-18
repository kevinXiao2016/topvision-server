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
        items: ["-", String.format(I18N.DHCP.everyPage, pageSize)]});
   return pagingToolbar;
}
function AddDhcpServer(){ 
	 window.top.createDialog('dhcpGiaddrAdd', I18N.DHCP.addBand, 400, 300, 'epon/dhcp/showOltDhcpGiaddrAdd.tv?entityId=' + entityId, null, true, true);
}
function modifyConfig(onuType,dhcpGiaddrIndex,dhcpGiaddrVid,dhcpGiaddrIp){
	window.top.createDialog('dhcpGiaddrModify', I18N.DHCP.modifyBand, 400, 300, 'epon/dhcp/showOltDhcpGiaddrModify.tv?onuType='+onuType+'&dhcpGiaddrIndex=' + dhcpGiaddrIndex+'&dhcpGiaddrVid=' + dhcpGiaddrVid+'&dhcpGiaddrIp=' + dhcpGiaddrIp + '&entityId=' + entityId, null, true, true);
}
function saveClick(){
	 
}
function cancelClick() {
	window.parent.closeWindow('dhcpIpMacDynamic');	
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
	var w = document.body.clientWidth - 24;
	var h = document.body.clientHeight - 50;
	var cm = new Ext.grid.ColumnModel([
	     {header: 'ID', dataIndex:'topOltDHCPIpMacIdx',width:80,align:"center"},                               	
	     {header: I18N.DHCP.staticIp, dataIndex:'topOltDHCPIpAddr',width:130,align:"center"},
	     {header: I18N.DHCP.headMac, dataIndex:'topOltDHCPMacAddr',width:150,align:"center"},
	     {header: I18N.DHCP.onuAddr, dataIndex:'topOltDHCPOnuMacAddr',width:150,align:"center"}	 
	     ]);
     
	  store = new Ext.data.JsonStore({
          url: 'epon/dhcp/loadDhcpIpMacStaticConfig.tv',
          totalProperty: "totalProperty",   
          root: 'data',            
          remoteSort: false,
          fields: ['topOltDHCPIpMacIdx','topOltDHCPIpAddr', 'topOltDHCPMacAddr', 'topOltDHCPOnuMacAddr']
      });
      store.load();
      grid = new Ext.grid.GridPanel({
              height:h,
              width: w,
              renderTo: 'dhcpStatic',
              store: store,
              bbar: buildPagingToolBar(),
              cm: cm
          });
})
</script>
</HEAD>
<BODY style="margin: 15pt 8pt 8pt 8pt;" class=POPUP_WND>
	<div class=formtip id=tips style="display: none"></div>
	<div id="dhcpStatic"></div>
			<div align="right" style="padding-right: 8px; padding-top: 5px;">
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
</BODY>
</HTML>