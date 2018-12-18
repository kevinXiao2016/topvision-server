<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
    css css/white/disabledStyle
    IMPORT epon.javascript.BatchDeployHelper
</Zeta:Loader>
<style type="text/css">
.linkBox{ border:1px solid #ccc; display:block; width:16px; height:16px; overflow:hidden; text-align:center; float:left; margin-right:2px; background:#fff; cursor:pointer;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var needArray = new Array();
	var excludeAaray = new Array();
	
	function checkExpressionInput(value){
		if(parse(value) != 0){
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.expressionTip@",null,function(){
				$("#expressionInput").focus();
			});
			return false;
		}
		return true;
	}
	
	function checkExperssExists(value){
		if($.inArray(value, needArray ) > -1 ){
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.bindRepeat@", null, function(){
				$("#expressionInput").focus();
			});
			return false;
		}
		if($.inArray(value, excludeAaray ) > -1 ){
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.excludeRepeat@", null, function(){
				$("#expressionInput").focus();
			});
			return false;
		}
		return true;
	}
	
	//添加到待绑定端口列表;
	function addNeeds(){
		var v = $("#expressionInput").val().trim();
		if(!checkExperssExists(v)){
			return false;
		}
		if(checkExpressionInput(v)){
			var str = '<span class="deleteSpan">';
				str += v;
				str += '<a href="javascript:;" name="'+ v +'" class="deleteSpanClose nm3kTip" nm3kTip="@BATCH.delThMark@" onclick="deleteSpan(this, 1)"></a></span>';
			$("#needBindingPort").append(str);
			needArray.push(v);
			$("#expressionInput").val("");
		}else{
			return;
		}
	}
	//添加到排除端口列表
	function addExcludes(){
		var v = $("#expressionInput").val().trim();
		if(!checkExperssExists(v)){
			return false;
		}
		if(checkExpressionInput(v)){
			var str = '<span class="deleteSpan">';
				str += v;
				str += '<a href="javascript:;" name="'+ v +'" class="deleteSpanClose nm3kTip" nm3kTip="@BATCH.delThMark@" onclick="deleteSpan(this, 2)"></a></span>';
			$("#excludeBindingPort").append(str);
			excludeAaray.push(v);
			$("#expressionInput").val("");
		}else{
			return;
		}
	}
	
	function deleteSpan(_this, flag){
		$(_this).parent().remove();
		if($("#nm3kTip").length > 0){
			$("#nm3kTip").hide();
		}
		if(flag == 1){
			needArray.remove($(_this).attr("name"));
		}else{
			excludeAaray.remove($(_this).attr("name"));
		}
	}
	
	function cancelClick() {
		window.top.closeWindow('batchPerfCollect');
	}
	
	function save(status){
		if(needArray.length > 0){
			var needExpression = needArray.join("_");
			var excludeExpression = excludeAaray.join("_");
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/saveBatchPerfCollect.tv',
				type : 'post',
				data : {
					entityId : entityId,
					status :　status,
					needBinds : needExpression,
					excudeBinds : excludeExpression
				},dataType: 'json',
				success : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", String.format("@BATCH.batchbindsuccess@", json.successCount, json.failedCount), null, function(){
						cancelClick();
					} );
					
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.bindFaild@");
				},
				cache : false
			}); 
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.pleasAdd2BindList@",null,function(){
				$("#expressionInput").focus();
			});
			return;
		}
	}
	
	function authLoad(){
		if(!operationDevicePower){
			$("#batchOpen").attr("disabled",true);
			$("#batchClose").attr("disabled",true);
		}
	}
	
	
</script>
</head>
<body class="openWinBody" onload="authLoad()">
	<div class="openWinHeader">
	    <div class="openWinTip">@BATCH.bindTitle@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edge10">
		<div class="yellowTip">@BATCH.tip1@ @BATCH.tip2@ @BATCH.tip3@ @BATCH.tip4@ @BATCH.tip5@</div>
	</div>
	<div class="edgeTB10LR20 pT10">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt withoutBorderBottom" width="160">@COMMON.portEl@:</td>
	                 <td class="withoutBorderBottom">
						<input id="expressionInput" type="text" class="normalInput" style="width:292px;height:22px;" class="songTi" />
	                 </td>
	             </tr>
	             <tr>
	             	<td style="padding-top:0; padding-bottom:8px;"></td>
	             	<td style="padding-top:5px; padding-bottom:8px;">
	             		<ul class="leftFloatUl">
	                 		<li>
	                 			<a href="javascript:;" class="normalBtn" onclick="addNeeds()"><span>@BATCH.add2bindList@</span></a> 
	                 		</li>
	                 		<li>
	                 			<a href="javascript:;" class="normalBtn" onclick="addExcludes()"><span>@BATCH.add2ExcludeList@</span></a> 
	                 		</li>
	                 	</ul>
	             	</td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@BATCH.bindList@:</td>
	             	<td>
	             		<div id="needBindingPort"></div>
	             	</td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@BATCH.excludeList@:</td>
	             	<td>
	             		<div id="excludeBindingPort"></div>
	             	</td>
	             </tr>
			</tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		     <li><a href="javascript:;" id='batchClose' class="normalBtnBig" onclick="save(2)"><span><i class="miniIcoSave"></i>@BATCH.close@</span></a></li>
		         <li><a href="javascript:;" id='batchOpen' class="normalBtnBig" onclick="save(1)"><span><i class="miniIcoSave"></i>@BATCH.open@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>