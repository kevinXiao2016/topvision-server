<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library Ext
    library Zeta
    module  performance
    import performance/js/jquery-ui-1.10.3.custom.min
    css js/tools/css/numberInput
    css performance/css/select2
    import js.tools.numberInput
    import performance.js.select2
    import performance.js.addPerfTargetsToTemplate
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
</style>

<script type="text/javascript">
</script>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	
	<div class="openWinHeader">
	    <div class="openWinTip">@Performance.addTargetLabel@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div id="content" class="edgeTB10LR20">
	</div>	
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" onclick="addPerfTarget()"><span><i class="miniIcoAdd"></i>@tip.add@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="closeClick()"><span>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
</body>
</Zeta:HTML>