<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    import gpon.onucpe.oltCpeList
</Zeta:Loader>
<style type="text/css">
#grid-container{
    height: 100%;
}
</style>
<script type="text/javascript">
var entityId = ${entityId};
var pageSize = <%= uc.getPageSize() %>;
var cameraSwitch = '<s:property value="cameraSwitch"/>';
</script>

</head>
<body class="whiteToBlack">
    <div id="header-container">
        <%@ include file="/epon/inc/navigator.inc"%>
    </div>
    <div id="grid-container" ></div>
</body>
</Zeta:HTML>