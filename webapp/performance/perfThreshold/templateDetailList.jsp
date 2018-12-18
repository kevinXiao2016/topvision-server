<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Ext
    library Jquery    
    library Zeta
    module  performance
    import performance.js.perfThresholdGeneralMethod
    import  performance.js.templateDetailList
</Zeta:Loader>
<style type="text/css">
body{

}
#grid{
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
.detailA{
	text-decoration: underline;
	color: #FF7E00;
	cursor: pointer;
}
.detailA:HOVER {
	text-decoration: none;
	color: #FF7E00;
	cursor: pointer;
}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var templateType = ${templateType};
var entityIds = '${entityIds}';
</script>
<body class="whiteToBlack">

	<div id="content">
		<div id="toolbar"></div>
		<div id="grid"></div>
	</div>
	
	<div id="frameOuter" class="cover" style="display:none; z-index:3;">
		<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
	</div>
</body>
</Zeta:HTML>
