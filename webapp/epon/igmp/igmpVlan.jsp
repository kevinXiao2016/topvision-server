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
    module igmpconfig
    CSS css/white/disabledStyle
    IMPORT epon/igmp/customOltIgmp
    IMPORT epon/igmp/igmpVlan
</Zeta:Loader>
<style type="text/css">
.frameBody .x-panel-body{ background:transparent;}
#ygddfdiv{background:#F7F7F7; opacity:0.8; filter:alpha(opacity=80); border-width:1px !important;}
</style>
<script type="text/javascript">
	var entityId = parent.entityId;
	var mode = parent.mode;
	var cm,store,grid;
	//通过这个对象缓存起来端口类型的json数据,null表示还没有去后台取过;
	//如果取过数据，则保存相应json替换null;
	var oSelect = {
		GE : null,
		XE : null,
		ETHAGG : null
	}
</script>
</head>
<body class="frameBody"></body>
</Zeta:HTML>