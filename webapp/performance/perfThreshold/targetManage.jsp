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
    module performance
    import performance.js.targetManage
</Zeta:Loader>
<style type="text/css">

</style>
<script type="text/javascript">
	var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
</script>
</head>
<body>
	
</body>
</Zeta:HTML>
