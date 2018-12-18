<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<HTML><HEAD>
  <TITLE>Create</TITLE>
  <link rel="STYLESHEET" type="text/css" href="../css/gui.css">
  <link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
  <link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
  <link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>  
  <fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
  <script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
  <script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>  
  <script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
  <script type="text/javascript" src="../js/zeta-core.js"></script>
  <script>
  	setEnabledContextMenu(true);
  	function closeDlg() {
  		try {
  			window.top.closeWindow('modalDlg');
  		} catch(err) {
  		}
  	}
  	
  	function showHelp() {
		window.open('help/index.jsp?module=new', 'help');
  	}
  </script>
</HEAD>
<BODY class=POPUP_WND>
<table cellspacing=0 cellpadding=0 width=100% height=100%>
	<tr><td class=WIZARD-HEADER>
		<table width=100%><tr><td><font style="font-weight:bold"><fmt:message key="Common.pleaseChooseGuide" bundle="${resource}"/></font><br><br>
		&nbsp;&nbsp;<span id="newMsg">&nbsp;</span></td>
		<td align=right class=WIZARD-RHEADER><img src="../images/wizard.gif" border=0></td></tr></table>
	</td></tr>
	<tr><td height=30 style="padding-left:10px"><fmt:message key="Common.guide" bundle="${resource}"/>:</td></tr>
	<tr><td style="padding:0 10px 0 10px"><div class=TREE-CONTAINER style="width:100%; height:100%;">
		<div id="topoTree" style="width:100%; height:100%;"></div></div>
	</td></tr>
	<tr><td height=30 style="padding:10px">
		<table width=100% cellspacing=0 cellpadding=0>
		<tr><td><img style="cursor:hand" src="../images/help.gif" border=0 qtip="<fmt:message key="COMMON.help" bundle="${resource}"/>" onclick="showHelp()"></td>
		<td align=right ><button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
  			onMouseOut="this.className='BUTTON75'" disabled><fmt:message key="SYSTEM.upStep" bundle="${resource}"/></button><button 
  			class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
  			onMouseOut="this.className='BUTTON75'" onclick="nextClick()" disabled id="nextBt"><fmt:message key="SYSTEM.nextStep" bundle="${resource}"/></button>&nbsp;
		<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
  			onMouseOut="this.className='BUTTON75'" disabled><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;<button 
  			class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
  			onMouseOut="this.className='BUTTON75'" onclick="closeDlg()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button></td></tr>
		</table>
	</td></tr>
</table>

<script>
<!--
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
setEnabledContextMenu(true);
Ext.onReady(function(){
	Ext.QuickTips.init();
	var myTreeLoader = new Ext.tree.TreeLoader({dataUrl:'tree-wizard.json'});
	myTreeLoader.on("load", function(treeLoader, node) {
		tree.getSelectionModel().select(node.firstChild);
   		Zeta$('newMsg').innerText = I18N.Common.createA + node.firstChild.text;
   		Zeta$('nextBt').disabled = false;		
    }, this);
    tree = new Ext.tree.TreePanel({
        el:'topoTree', useArrows:useArrows, autoScroll:true, animate:true, border: false,
        trackMouseOver:false,
        lines: true, rootVisible: false, enableDD: false,
        loader: myTreeLoader
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Wizard Tree', draggable:false, id:'wizard'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    
    tree.on('click', function(n){
    	var sn = this.selModel.selNode || {};
    	if(n.leaf){
    		Zeta$('newMsg').innerText = I18N.Common.createA + n.text;
    		Zeta$('nextBt').disabled = false;
    	} else {
    		Zeta$('newMsg').innerText = ' ';
    		Zeta$('nextBt').disabled = true;
    	}
    });    
});

function nextClick() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node != null) {
		location.href = node.attributes.url;
		window.top.setWindowTitle('modalDlg', I18N.COMMON.create + node.text);
	}
}
-->
</script>
</BODY></HTML>
