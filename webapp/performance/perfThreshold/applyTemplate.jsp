<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    import performance.js.applyToTemplate
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/ext-formhead-linefeed.css"/>
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
#serach{
	padding-top:20px;
	padding-left:20px;
	height: 40px;
}
#serach input, #serach select{
	margin-right: 40px;
}
.gridDiv{
	margin: 10px 20px;
}
</style>

<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var templateType = ${perfThresholdTemplate.templateType};
var templateId = ${perfThresholdTemplate.templateId};
var parentType = ${perfThresholdTemplate.parentType};
var templateName = '${perfThresholdTemplate.templateName}';
var allEntityTypes = ${allEntityTypes};
//模块支持情况,在单独安装CC模块的时候使用
var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
var eponSupport = <%= uc.hasSupportModule("olt")%>;
</script>
<body>
	<div id="toolbar"></div>

	<div id="serach">
		<table>
			<tr>
				<td>@Performance.entityName@:</td>
				<td><input type="text"  id="entityName" class="normalInput"/></td>
				<td>@resources/COMMON.manageIp@:</td>
				<td><input type="text"  id="ipAddress" class="normalInput"/></td>
				<td class="cmcSearch">MAC:</td>
				<td class="cmcSearch"><input type="text"  id="mac" class="normalInput"/></td>
				<td class="cmcSearch">@Performance.entityType@:</td>
				<td class="cmcSearch">
					<select id="deviceType" class="normalSel">
					 	<option value="">@COMMON.select@</option>
					</select>
			 	</td>
			 	<td>
			 		<a href="javascript:;" class="normalBtn" onclick="searchForDevice()">
			 			<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
			 		</a>
			 	</td>
			</tr>
		</table>
	</div>
	
    <div class="gridDiv" id="deviceUnRelate"></div>
    <div class="gridDiv" id="deviceRelated"></div>
</body>
</Zeta:HTML>