<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module fault
    import fault.event-detai
</Zeta:Loader>
<%
	boolean clearevent = uc.hasPower("clearevent");
%>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var clearevent = <%=uc.hasPower("clearevent")%>;
//setEnabledContextMenu(true);
var storeUrl = "getEventList.tv?typeId=" + <s:property value="typeId"/>;
var myToolbar = [
    {text:"@COMMON.print@", iconCls:'bmenu_print', handler:onPrintClick}, '-',
    <%if (clearevent) {%>
    {text:"@COMMON.delete@", iconCls:"bmenu_delete", handler:onDeleteClick},
    <%}%>
    /* {text:"@COMMON.exportAction, iconCls:"bmenu_export", handler:onExportClick}, "-", */ 
    {text: "@COMMON.refresh@", iconCls: 'bmenu_refresh', handler: onRefreshClick}/* , '-', */
    /* {text:"@COMMON.maxView, iconCls:'bmenu_maxview', enableToggle:true, toggleHandler:onMaxViewClick} */
];
var columnModels = [{header:"@resources/EVENT.typeHeader@", width:150, sortable:false, dataIndex:"typeName"},
    {header:"@resources/EVENT.noteHeader@", width:200, sortable:false, groupable:false, dataIndex:"note"},
    {header:"@resources/EVENT.sourceHeader@", width:120, sortable:false, dataIndex:"host"},
    {header:"@resources/EVENT.timeHeader@", width:200, sortable:false, groupable:false, dataIndex:"date"},
    {header:"@COMMON.manu@", dataIndex:"date",renderer: manuRenderer}];
</script>
</head>
<body class=whiteToBlack></body>
</Zeta:HTML>