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
    import performance.js.perfThresholdTemplate
</Zeta:Loader>
<style type="text/css">
body{
	min-width:900px;
	min-height: 500px;
}
.cover {
	height: 100%;
	left: 0;
	position: absolute;
	overflow: auto;
	top: 0;
	width: 100%;
	background: #FFFFFF;
}
#frameOuter {
	width: 100%;
	height: 100%;
	overflow: hidden;
}
</style>
<script type="text/javascript">
	var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
</script>
</head>
<body>
	<div id="content"></div>
	
	<div id="frameOuter" class="cover" style="display:none; z-index:3;">
		<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
	</div>
</body>
</Zeta:HTML>
