<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    css performance.css.switchButton
    import js.zetaframework.Validator
    import performance.js.switchButton
    import performance.js.perfThresholdGeneralMethod
    import performance.js.ccTemplate
</Zeta:Loader>
<style type="text/css">
body{
	
}
#searchDiv{
	height:40px;
	padding-top: 20px;
	padding-left:10px;
	vertical-align: middle;
}
#searchDiv label, #searchDiv input, #searchDiv button{
	vertical-align: middle;
}
#searchDiv input{
	margin-right: 20px;
	width: 100px;
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
.switch{
	cursor: pointer;
}
.toolImg{
	margin-right: 10px;
}
</style>
<script type="text/javascript">
//var data = ${allIp};
var pageSize = <%= uc.getPageSize() %>;
var templateType = EntityType.getCcmtsType();
var parentType = EntityType.getCCMTSAndCMTSType();
var ccmtsSubTypes = ${ccmtsSubTypes};
//模块支持情况,在单独安装CC模块的时候使用
var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
var eponSupport = <%= uc.hasSupportModule("olt")%>;
</script>
<body>
	<div id="content">
		<div id="toolbar"></div>
		<div id="searchDiv">
			<table>
				<tr>
					<td>@Performance.entityName@:</td>
					<td><input type="text" id="entityName" class="normalInput"/></td>
					<td>MAC:</td>
					<td><input type="text" id="mac" class="normalInput" toolTip="@tip.macRule@"/></td>
					<td>@Performance.isRelaTemp@:</td>
					<td><select id="tempRela" class="normalSel" style="width:80px;">
						<option value="-1">@resources/COMMON.pleaseSelect@</option>
						<option value="1">@resources/COMMON.yes@</option>
						<option value="2">@resources/COMMON.no@</option>
					</select></td>
					<td>@Performance.templateName@:</td>
					<td><input type="text" id="templateName" class="normalInput"/></td>
					<td>@Performance.entityType@:</td>
					<td>
						<select id="entityType"  style="width: 150px;" onchange="entityChanged()" class="normalSel">
				            <option value='-1' selected="selected">@tip.allType@</option>
						</select> 
					</td>
					<td><label id="optionLabel">@Performance.relaOlt@:</label></td>
					<td><span id="optionSpan"></span></td>
					<td>
						<a href="javascript:;" class="normalBtn" onclick="onSeachClick()">
				 			<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
				 		</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="grid"></div>
	</div>
	
	<div id="frameOuter" class="cover" style="display:none; z-index:3;">
		<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
	</div>
</body>
</Zeta:HTML>
