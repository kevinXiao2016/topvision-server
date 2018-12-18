<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module network
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var jobId = '<s:property value="jobId"/>';

$(function(){
	$.ajax({
		url: "/upgrade/getUpgradeResult.tv?entityId=" + entityId + "&jobId=" + jobId,
		type: "post",
		success: function(response){
			if(response == 'null' || response.length < 5){
				$("#inputText").val("");
			}else{
				$("#inputText").val(response);
			}
		}
	})
}) 

function closeClisk(){
	window.parent.closeWindow('result');
}
</script>
</head>
<body class="whiteToBlack">
	<div id = "inputDiv" class="jsShow" style="margin: 5px; width: 99%">
		<textarea style="width: 98%; height:380px;"  id="inputText" class="normalInput" readonly="readonly">
		</textarea>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		     	 <li><a onclick="closeClisk()" href="javascript:;" id="close" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>