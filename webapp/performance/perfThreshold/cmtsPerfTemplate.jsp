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
    import performance.js.cmtsPerfTemplate
</Zeta:Loader>
<style type="text/css">
body{
	/* IE6 不支持min-width属性，但是IE7+和W3C支持 */
	min-width:900px;
	/* 用CSS表达式让IE6也支持最小宽度 */
	_width:expression((document.documentElement.clientWidth||document.body.clientWidth)<900?"900px":"");
	/* IE6 不支持min-width属性，但是IE7+和W3C支持 */
	min-height: 500px;
	/* 用CSS表达式让IE6也支持最小高度 */
	_height:expression((document.documentElement.clientHeight||document.body.clientHeight)<500?"500px":"");
}
#searchDiv{
	height:40px;
	padding-top: 10px;
	padding-left:20px;
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
	margin: 0 10px;
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
var cmtsSubTypes = ${cmtsSubTypes};
var templateType = EntityType.getCmtsType();
</script>
<body>
	<div id="content">
		<div id="toolbar"></div>
		<div id="searchDiv">
			<table>
				<tr>
					<td>@Performance.entityName@:</td>
					<td><input type="text" id="entityName" class="normalInput"/></td>
					<td>IP:</td>
					<td><input type="text" id="mac" class="normalInput" toolTip="@tip.macRule@"/></td>
					<td>@Performance.templateName@:</td>
					<td><input type="text" id="name" class="normalInput"/></td>
					<td>@Performance.entityType@:</td>
					<td>
						<select id="entityType"  style="width: 120px;" class="normalSel">
				            <option value='-1' selected="selected">@tip.allType@</option>
						</select> 
					</td>
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
