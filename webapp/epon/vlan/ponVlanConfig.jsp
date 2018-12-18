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
    css js/dropdownTree/css/zTreeStyle/zTreeStyle
    module epon
    import js.ext.ux.PagingMemoryProxy
    import js/dropdownTree/jquery.ztree.all-3.5.min
    import epon.vlan.oltVlanUtil
    import epon.vlan.ponVlanTransparent
    import epon.vlan.ponVlanQinQ
    import epon.vlan.ponVlanTransform
    import epon.vlan.ponVlanAggregation
    import epon.vlan.ponVlanSCVID
    import epon.vlan.ponVlanConfig
</Zeta:Loader>
<style type="text/css">
#newtree a.selectedTree{ background-color:#369; color:#fff;}
.x-panel-body.x-panel-body-noheader.x-panel-body-noborder{
	background: none;
}
.x-grid-with-col-lines .x-grid3-row td.x-grid3-cell{
	border-color: #D0D0D0;
} 
.halfFloatdDiv{
	float: left;
	width: 49.5%;
	height: 49.5%;
	padding: 0;
}
.halfFloatdDiv .innerContainer{
	padding: 30px 3px 10px 3px;
}
#qinqSvlanGrid{
	border-right: 1px solid #red;
}
</style>
<script type="text/javascript">
var entityId = ${entityId};
var ponId = ${ponId};
var portIndex = ${portIndex};
var pageSize = ${pageSize};
</script>
</head>

<body class="whiteToBlack">
	<div id="header-container" style="height: 30px;margin:10px;">
		<a id="simple-query" href="javascript:showApplyToOtherPon();" class="normalBtn" onclick=""><span><i class="miniIcoApply"></i>@VLAN.applyToOtherPon@</span></a>
		<a id="simple-query" href="javascript:getDataFormDevice();" class="normalBtn" style="margin-left:10px;" onclick=""><span><i class="miniIcoRefresh"></i>@COMMON.fetch@</span></a>
	</div>
	
	<div id="ponTree-container" style="height:100%";>
		<p class="pannelTit" id="sidePartTit">@VLAN.ponPort@</p>
		<div id="ponTreeContainer" class="clear-x-panel-body" style="position:relative;overflow: auto;">
			<ul id="ponZTree" class="ztree"></ul>
		</div>
	</div>

	<div id="center-container" class="wrapWH100percent" style="height:100%;">
			<div class="zebraTableCaption halfFloatdDiv" id="ponPassContainer" style="width:45%;">
				<div class="zebraTableCaptionTitle"><span>@VLAN.vlanTansparent@</span></div>
				<div class="innerContainer">
					<table class="queryTable">
						<tr>
							<td class="rightBlueTxt">VLAN:</td>
							<td><input type="text" class="normalInput w70" id="transparentInput" maxlength="63" tooltip='@VLAN.ruleManuTitle@'/></td>
							<td class="rightBlueTxt">@VLAN.mode@:</td>
							<td>
								<select id="transparentModeSel" class="normalSel w50">
									<option value=0 style='color:green'>tag</option>
									<option value=1 style='color:red'>untag</option>
								</select>
							</td>
							<td><a id="simple-query" href="javascript:addTransparentVlan();" class="normalBtn" onclick=""><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
							<td><a id="simple-query" href="javascript:batchDeleteTransClick();" class="normalBtn" onclick=""><span><i class="miniIcoClose"></i>@COMMON.delete@</span></a></td>
						</tr>
					</table>
					<div id="ponVlanPassGridContainer"></div>
				</div>
			</div>
			<div class="zebraTableCaption halfFloatdDiv" id="ponQinQContainer" style="width:54%;">
				<div class="zebraTableCaptionTitle"><span>QinQ</span></div>
				<div class="innerContainer">
					<table class="queryTable">
						<tr>
							<td class="rightBlueTxt" colspan="2">@VLAN.outerVlanPri@:</td>
							<td colspan="6"> 
								<input id="qinqCosCheckbox2" type="radio" name="qinqCos"
									value="2" onclick="qinqCosModeClick()" checked style="position: relative;top:2px;margin-left: 5px;"/>@VLAN.useInnerVlanPri@
								<input id=qinqUseNewPriCbx type="radio" name="qinqCos"
									value="1" onclick="qinqCosModeClick()" style="position: relative;top:2px;margin-left: 5px;"/>@VLAN.useNewVlanPri@ 
								<select id="qinqCos" disabled onchange=qinqCosChange() class="normalSel w60">
										<option value="0">0</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@VLAN.outerVlan@:</td>
							<td><input type="text" class="normalInput w40" id="qinqOuterVlanId" maxlength="63" tooltip='@COMMON.range4094@'/></td>
							<td class="rightBlueTxt">Start VLAN:</td>
							<td><input type="text" class="normalInput w40" id="qinqInnerVlanStartId" maxlength="63" tooltip='@COMMON.range4094@'/></td>
							<td class="rightBlueTxt">End VLAN:</td>
							<td><input type="text" class="normalInput w40" id="qinqInnerVlanEndId" maxlength="63" tooltip='@COMMON.range4094@'/></td>
							<td><a id="simple-query" href="javascript:addQinQRuleClick();" class="normalBtn"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
						</tr>
					</table>
					<div style="float:left;width:20%;margin-right:2px;">
						<div id='qinqSvlanGrid'></div>
					</div>
					<div style="float:left;width:79%;">
						<div id='qinqCvlanGrid'></div>
					</div>
				</div>
			</div>
			<div class="zebraTableCaption halfFloatdDiv" id="ponTransformContainer" style="width:45%;">
				<div class="zebraTableCaptionTitle"><span>VLAN@VLAN.trans@</span></div>
				<div class="innerContainer">
					<table class="queryTable">
						<tr>
							<td class="rightBlueTxt">@VLAN.originVlan@@COMMON.maohao@</td>
							<td><input id="beforeTransId" type="text" class="normalInput w50" tooltip='@COMMON.range4094@' maxlength="4" /></td>
							<td class="rightBlueTxt w100">@VLAN.translatedVlan@@COMMON.maohao@</td>
							<td><input id="afterTransId" type="text"  class="normalInput w50" tooltip='@COMMON.range4094@' maxlength="4" /></td>
							<td><a id="simple-query" href="javascript:transOkClick();" class="normalBtn" onclick=""><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
						</tr>
					</table>
					<div id="ponVlanTransformGridContainer"></div>
				</div>
			</div>
			<div class="zebraTableCaption halfFloatdDiv" id="ponUnionContainer" style="width:54%;">
				<div class="zebraTableCaptionTitle"><span>VLAN@VLAN.agg@</span></div>
				<div class="innerContainer">
					<table class="queryTable">
						<tr>
							<td class="rightBlueTxt">@VLAN.trunkedVlan@:</td>
							<td><input id="aggregationSvlanId" type="text" class="normalInput w50" tooltip='@COMMON.range4094@' maxlength="4" /></td>
							<td class="rightBlueTxt w100">@VLAN.trunkVlan@:</td>
							<td><input id="aggregationCvlanId" type="text"  class="normalInput w50" tooltip='@VLAN.filterRule@' /></td>
							<td><a id="simple-query" href="javascript:addAggrClick();" class="normalBtn" onclick=""><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
						</tr>
					</table>
					<div style="float:left;width:50%;margin-right:2px;">
						<div id='aggrSvlanGrid'></div>
					</div>
					<div style="float:left;width:49%;">
						<div id='aggrCvlanGrid'></div>
					</div>
				</div>
			</div>
	</div>
	
	<div id="extra-container" style="height:100%;">
		<div id="CVID-container" class="x-hide-display" style="height:100%;">
		</div>
		<div id="SVID-container" class="x-hide-display" style="height:100%;"></div>
	</div>
</body>
</Zeta:HTML>