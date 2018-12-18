<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
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
    import performance.js.oltTemplate
</Zeta:Loader>
<style type="text/css">

#searchDiv{
	height:40px;
	padding-top: 10px;
	padding-left:10px;
	vertical-align: middle;
}
#searchDiv label, #searchDiv input, #searchDiv button{
	vertical-align: middle;
}
#searchDiv input{
	margin-right: 20px;
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
var templateType = EntityType.getOltType();
var parentType = EntityType.getOltType();

</script>
<body>
	<div id="content">
		<div id="toolbar"></div>
		<div id="searchDiv">
			<table>
				<tr>
					<td>@Performance.entityName@:</td>
					<td><input type="text" id="entityName" class="normalInput"/></td>
					<td>@Performance.manageIp@:</td>
					<td><input type="text" id="entityIp" class="normalInput"/></td>
					<td>@Performance.isRelaTemp@:</td>
					<td><select id="tempRela" class="normalSel" style="width:80px;">
						<option value="-1">@resources/COMMON.pleaseSelect@</option>
						<option value="1">@resources/COMMON.yes@</option>
						<option value="2">@resources/COMMON.no@</option>
					</select></td>
					<td>@Performance.templateName@:</td>
					<td><input type="text" id="templateName" class="normalInput"/></td>
					<td>
						<a href="javascript:;" class="normalBtn" onclick="onSeachClick()">
				 			<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
				 		</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="grid" class="normalTable"></div>
	</div>
	
	<div id="frameOuter" class="cover" style="display:none; z-index:3;">
		<iframe id="frameInner" name="frameInner" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
	</div>
</body>
</Zeta:HTML>
