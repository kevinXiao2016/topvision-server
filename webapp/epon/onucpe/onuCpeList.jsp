<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
    import epon.onucpe.onuCpeList
    IMPORT epon/onu/onuDeleteTrap
</Zeta:Loader>
<style type="text/css">
#grid-container{
    height: 100%;
}
</style>
<script>
var onuId = ${onuId};
var pageSize = <%= uc.getPageSize() %>;
</script>
</head>
<body>
    <div id="header-container">
        <%@ include file="/epon/onu/navigator.inc"%>
    </div>
    <div id="grid-container" ></div>
</body>
</Zeta:HTML> 
