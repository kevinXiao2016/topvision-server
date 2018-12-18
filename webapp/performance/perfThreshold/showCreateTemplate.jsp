<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css performance.css.perfThresholdCommonStyle
    library Ext
    library Zeta
    module  performance
    import js.json2
    import performance.js.templateGeneralDomain
    import performance.js.perfThresholdGeneralMethod
    import performance.js.createTemplate
</Zeta:Loader>
<script src="/performance/js/jquery-1.8.3.js"></script>
<script src="/performance/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
.x-panel-tbar{ border-top:1px solid #d0d0d0} 
.normalTable .x-panel-tbar table td{ height:auto;}
</style>

<script type="text/javascript">
var allEntityTypes = ${allEntityTypes};
var originalFrame = '${originalFrame}';
//类型与对应指标数量
var targetCountJson = ${targetCountJson};
//模块支持情况,在单独安装CC模块的时候使用
var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
var eponSupport = <%= uc.hasSupportModule("olt")%>;

</script>
<body>
	<div class=formtip id=tips style="display: none"></div>
	<div id="toolbar"></div>
	<div id="content">
		<table class="contentTable" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td width=200 align="right" class="labelTd">@performance/Performance.templateName@:</td>
					<td width=200><input type="text" id="perfTemplateName" style="width:180px;" maxlength="32" toolTip="@Performance.perfNameTip@"/></td>
					<td class="tipTd vertical-middle">
						<span id="existed" style="display:none;">
							<img src="/images/performance/error.png" alt="" />
							<span>@performance/Performance.templateExisted@</span>
						</span>
						<span id="templateDesc">@performance/Performance.templateDesc@</span>
					</td>
				</tr>
				<tr>
					<td align="right" class="labelTd">@performance/Performance.templateType@:</td>
					<td>
						<select id="templateType"></select>
					</td>
					<td class="tipTd">@performance/Performance.templateTypeDesc@</td>
				</tr>
				<tr>
					<td align="right" class="labelTd">@tip.setToSubDftTmp@:</td>
					<td>
						<input type="checkbox" id="defaultTempCbx"/>
						<span style="display:none;">
							<select id="select_10000" style="width:163px;" >
	            			</select>
	            			<select id="select_60000" style="display:none;width:163px;">
	            			</select>
						</span>
					</td>
					<td class="tipTd">*@tip.subToolTip@</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div id="perfGrid"></div>
	
</body>
</Zeta:HTML>