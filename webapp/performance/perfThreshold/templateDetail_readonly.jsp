<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<script src="/performance/js/jquery-ui-1.10.3.custom.min.js"></script>
<Zeta:Loader>
	css performance.css.perfThresholdCommonStyle
    library Ext
    library Zeta
    module  performance
    import js.json2
    import performance.js.templateGeneralDomain
    import performance.js.perfThresholdGeneralMethod
    import performance.js.templateDetail_readonly
</Zeta:Loader>
<style type="text/css">
</style>

<script type="text/javascript">
var perfThresholdTemplate = ${perfThresholdTemplateJson};
var perfThresholdRules = ${perfThresholdRuleJson};
var perfTargetPair = ${perfTargetJson};
var perfThresholdTargets = ${perfThresholdTargets};
</script>
<body class="whiteToBlack">
	<div id="toolbar"></div>
	
	<div id="content">
		<table class="contentTable" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td width=200 align="right" class="labelTd">@performance/Performance.templateName@:</td>
					<td width=200><span id="perfTemplateName"></span></td>
					<td class="tipTd vertical-middle">@performance/Performance.templateDesc@</td>
				</tr>
				<tr>
					<td align="right" class="labelTd">@performance/Performance.templateType@:</td>
					<td><span id="templateType"></span></td>
					<td class="tipTd">@performance/Performance.templateTypeDesc@</td>
				</tr>
				<tr style="display: none;">
					<td align="right" class="labelTd">@tip.subDftTmp@:</td>
					<td><span id="subType"></span></td>
					<td class="tipTd">
						<span id="normalTypeTip">*@tip.subToolTip@</span>
						<span id="templateOnlyOne" style="display:none;" class="vertical-middle">
							<img src="/images/performance/error.png" alt="" />
							<span>@tip.oneOneDftTmp@</span>
						</span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="flag">@tip.perfTargetList@</div>
	<div id="perfGrid"></div>
	
</body>
</Zeta:HTML>