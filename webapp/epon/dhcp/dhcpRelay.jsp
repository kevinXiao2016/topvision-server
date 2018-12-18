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
    import epon/dhcp/dhcpRelay
</Zeta:Loader>
<script type="text/javascript">
    var pageSize = <%= uc.getPageSize() %>;
    var entityId = '${entityId}';
    var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
    var cm, columnModels, sm, store, grid, tbar, cm2, columnModels, sm2, store2, grid2, tbar2;
</script>
</head>
    <body class="grayBody clear-x-panel-body"></body>
</Zeta:HTML>