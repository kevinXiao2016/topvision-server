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
    css css/white/disabledStyle
    css js/dropdownTree/css/zTreeStyle/zTreeStyle
    module epon
    import js.jquery.Nm3kTabBtn
    import js.ext.ux.PagingMemoryProxy
    import js/dropdownTree/jquery.ztree.all-3.5.min
    import epon.javascript.SlotConstant
    import epon.vlan.oltVlanUtil
    import epon.vlan.vlan
    import epon.vlan.sniVlan
    import epon.vlan.ponVlan
    import epon.vlan.uniVlan
    import epon.vlan.oltVlanList
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<style type="text/css">
#newtree a.selectedTree{ background-color:#369; color:#fff;}
.left-LR{
	width:200px;
}
.line-LR{
	left: 200px;
}
.right-LR{
	margin-left: 208px;
}
.x-panel-body.x-panel-body-noheader.x-panel-body-noborder{
	background: none;
}
.x-grid-with-col-lines .x-grid3-row td.x-grid3-cell{
	border-color: #D0D0D0;
} 
.halfFloatdDiv{
	float: left;
	width: 49%;
	height: 45%;
	padding: 0;
}
.halfFloatdDiv .innerContainer{
	padding: 30px 10px 10px 10px;
}
.x-grid3-header-offset { padding-left:0px;}
.x-grid3-row td{ padding-left:0px;}
.wordBreakDiv{ white-space:normal; word-break:break-all; word-wrap:break-word;}
.topLine{ border-top:1px solid #d0d0d0;}
</style>
<script type="text/javascript">
var entityName = '${entityName}';
var entityId = ${entityId};
var pageSize = <%= uc.getPageSize() %>;
var cameraSwitch = '${cameraSwitch}';
var slotTypeMapping = ${slotTypeMapping};
var typeId = '${entity.typeId}';
</script>

</head>
<body class="newBody">
	<div id="header-container">
		<div id="header">
			<%@ include file="/epon/inc/navigator.inc"%>
		</div>
	
		<div id="putTab" class="edge10">
		</div>
	</div>
	
	<div id="center-container" style="height:100%;">
		<div class="topLine pT0 clearBoth tabBody" id="vlanGridContainer" style="height:100%;">
		</div>
		
		<div class="topLine pT0 clearBoth tabBody" id="sniGridContainer" style="display:none;height:100%;">
		</div>
		
		<div class="topLine pT0 clearBoth tabBody" id="ponGridContainer" style="display:none;height:100%;">
		</div> 
		
		<div class="topLine pT0 clearBoth tabBody" id="uniGridContainer" style="display:none;height:100%;">
			<div class="wrapWH100percent" style="height:100%;">
				<div class="left-LR obliqueFringeBg">
					<p class="pannelTit">ONU</p>
					<div id="uniTreeContainer" class="clear-x-panel-body" style="position:relative;"></div>
				</div>
				<div class="line-LR"></div>
				<div class="right-LR" id="uniGridPanelContainer">
				</div>
			</div>
		</div> 
	</div>

</body>
</Zeta:HTML>