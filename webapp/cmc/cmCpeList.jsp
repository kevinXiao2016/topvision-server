<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module CMC
    import js/customColumnModel
    import js/placeHolderHack
    import cmc.js.cmCpeList
</Zeta:Loader>
<style type="text/css">
body,html{height:100%;overflow:hidden;}
#query-container{height: 142px;overflow:hidden;}
#advance-toolbar-div{padding:10px 0 0 5px;}
#simple-toolbar-div{padding:20px 0 0 5px;}
#queryContent{width: 330px;padding-left: 5px;}
.queryTable a{color:#B3711A;}
#grid-div{position: relative;}
#cm-num-div{position: absolute;right: 0;top:0;z-index: 9;height: 16px;padding-top: 7px;padding-bottom: 5px;vertical-align: middle;}
#cm-num-div span{padding-right: 10px;vertical-align: middle;}
#cm-num-div label{vertical-align: middle;}
#cmListSidePart{ width:380px; padding:0px 10px; height:100%; overflow:auto; background: #F4F4F4; position:absolute; top:0; right:-400px; border-left:1px solid #9a9a9a; z-index:999; opacity:0.9; filter:alpha(opacity=90);}
.positionLi{ padding-top:3px; overflow:hidden;}
.cmListLeftFloatDl{ float:left;}
.cmListLeftFloatDl dt,.cmListLeftFloatDl dd{ float:left;}
.cmListLeftFloatDl dt{ width:60px; padding-top:6px;}
.cmListLeftFloatDl dd{ margin-right:3px; padding-top:8px;}
.cmListLeftFloatDl dd.eqName{padding-top:5px; width:245px; overflow:hidden; line-height:2em;}
.cmListBody{ padding:10px; border-left:1px solid #cacaca; border-right:1px solid #cacaca; background:#fff; position:relative;}
.bottomRight{ position:absolute; bottom:2px; right:5px;}
.cmListArr{float:left;}
.cmListArr li{ float:left;}
.cmListW155{ width:155px; overflow:hidden; padding-top:5px;}
.cmListW155 p{ padding:2px;}
.cmListW155 span{ padding:2px;}
.redBg{ background:#f00; color:#fff;}
.orangeBg{ background: #f90; color:#000;}
.sideTable td{ height: 20px;}
#cmListSideArrow{ width:19px; height:140px; overflow:hidden; position: absolute; right:0px; top:0px; z-index:1000; cursor:pointer;}
#arrow{ position:absolute; top:20px; left:8px; width:4px; height:8px; overflow: hidden; }
.yellow-div{height:16px;width:16px;background-color: #DCD345;}
.green-div{height:16px;width:16px;background-color: #ffffff;}
.red-div{height:16px;width:16px;background-color: #C07877;}
.normalTable .yellow-row{background-color:#DCD345 !important;}
.normalTable .red-row{background-color:#C07877 !important;}
.normalTable .green-row{background-color:#26B064 !important;}
.normalTable .white-row{background-color:#FFFFFF !important;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}

.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
#loading {
    padding: 5px 8px 5px 26px;
    border: 1px solid #069;
    background: #F1E087 url('../images/refreshing2.gif') no-repeat 2px center;
    position: absolute;
    z-index: 999;
    top: 0px;
    left: 0px;
    display: none;
    font-weight: bold;
}
#tip-div{ position:absolute; top:120px; right:18px;}
.mainChl{
    background-color: yellow;
}
.subDashedLine{ border-bottom:1px dashed #ccc; padding-bottom:8px; padding-top:8px;}
</style>
<script src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="../js/ext/ux/RowExpander.css" />

<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var language = '<%=uc.getUser().getLanguage()%>';
/* var isSimpleSearch = '${simpleModeFlag}' ? ${simpleModeFlag} : true; */
</script>
</head>
<body class="bodyGrayBg">
	<!-- 查询DIV -->
	<div id="query-container">
		<div id="simple-toolbar-div">
			<table class="queryTable">
				<tr>
					<td><textarea class="normalInput" id="queryContent" placeHolder="@cmcpe/CCMTS.CmCpeList.fuzzyMatching@" maxlength="2000" style="width:600px;height: 100px;" ></textarea></td>
					<td><a id="simple-query" href="javascript:simpleQuery();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="cm3Tip" style="position: absolute;"></div>
</body>
</Zeta:HTML>