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
    IMPORT epon.javascript.BatchDeployHelper
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.linkBox{ border:1px solid #ccc; display:block; width:16px; height:16px; overflow:hidden; text-align:center; float:left; margin-right:2px; background:#fff; cursor:pointer;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var profileIndex = '${profileIndex}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var needArray = new Array();
	var excludeAaray = new Array();
	
	function checkExpressionInput(value){
		if(parse(value,4) != 0){
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
		window.top.closeWindow('profileBind');
	}
	
	function savePorfileBind(){
		var $pvid = $("#pvidIpt").val();
		if(isNaN($pvid) || $pvid>4094 || $pvid <0){
			return top.showMessageDlg("@COMMON.tip@", "@BATCH.illeagalPvid@");
		}
		if(needArray.length > 0){
			var needExpression = needArray.join("_");
			var excludeExpression = excludeAaray.join("_");
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/univlanprofile/saveProfileBind.tv',
				type : 'post',
				data : {
					entityId : entityId,
					profileIndex :　profileIndex,
					needBinds : needExpression,
					uniPvid :  $pvid,
					excudeBinds : excludeExpression
				},
				dataType: 'json',
				success : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", String.format("@BATCH.batchbindsuccess@", json.successCount, json.failedCount), null, function(){
						top.frames["frameuniVlanProfile"].refreshGridData();
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
	$(function(){
		var $mode;
		switch(${profileMode}){
			case 0 : $mode =  "@PROFILE.modeNone@"; break;
			case 1 : $mode =  "@PROFILE.modeTransparent@"; break;
			case 2 : $mode =  "@PROFILE.modeTag@"; break;
			case 3 : $mode =  "@PROFILE.modeTranslate@"; break;
			case 4 : $mode =  "@PROFILE.modeAgg@"; break;
			case 5 : $mode =  "@PROFILE.modeTrunk@"; break;
		}
		$("#vlanMode").text( $mode );
	});
	function authLoad(){
		if(!operationDevicePower){
			$("#savePorfileBind").attr("disabled",true);
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
	             	<td class="rightBlueTxt">@PROFILE.vlanMode@@COMMON.maohao@</td>
	             	<td>
	             		<div id="vlanMode"></div>
	             	</td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">PVID@COMMON.maohao@</td>
	             	<td>
	             		<input id="pvidIpt" class="normalInput" style="width:292px;height:22px;" type="text" tooltip="@RULE.vlanInputTip@"/>
	             	</td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt withoutBorderBottom" width="160">@COMMON.portEl@@COMMON.maohao@</td>
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
	             	<td class="rightBlueTxt">@BATCH.bindList@@COMMON.maohao@</td>
	             	<td>
	             		<div id="needBindingPort"></div>
	             	</td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@BATCH.excludeList@@COMMON.maohao@</td>
	             	<td>
	             		<div id="excludeBindingPort"></div>
	             	</td>
	             </tr>
	            
			</tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a href="javascript:;" id='savePorfileBind' class="normalBtnBig" onclick="savePorfileBind()"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>