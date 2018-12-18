<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
</Zeta:Loader>
<style type="text/css">
.linkBox{ border:1px solid #ccc; display:block; width:16px; height:16px; overflow:hidden; text-align:center; float:left; margin-right:2px; background:#fff; cursor:pointer;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var needArray = new Array();
	
	function checkExpressionInput(value){
		if(parse(value,4) != 0){
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.expressionTip@",null,function(){
				$("#expressionInput").focus();
			});
			return false;
		}
		return true;
	}
	//对输入的表达式进行校验  @auther zhangdongming
	function parse(expression,level){
		var NOT_EXIST = -1,
			ANY_MATCH = "*",
			ERROR_EXPRE = 1,//格式错误
			NOT_EXIST_TARGET = 2,
			UN_COMPLETELD = 3,
			PASS = 0;
		var splits = expression.split(new RegExp("[/:]","g"));
		if(level != splits.length){
			return UN_COMPLETELD;
		}
		var $result = PASS;
		for(var i=0;i<splits.length;i++){
			var split = splits[i];
			$result =  isExpre(split,i);
			if($result > 0){
				return $result;
			}
		}
		return $result;

		function isExpre(expr,level){
			if(!expr || expr.search("\w/gi") > -1){
				return ERROR_EXPRE;
			}
			if(expr.search("\\(") == 0){
				if( expr.search(")") == expr.length-1 ){
					expr = expr.replace("(|)","");
				}else{
					return ERROR_EXPRE;
				}
			}
			if(expr == ANY_MATCH ){
				return PASS;
			}
			var l1 = expr.split(",");
			for(var i=0; i<l1.length; i++){
				var seg = l1[i];
				if(/^\d+$/gi.test(seg)){
					continue;
				}
				if(/^[1-9][\d]{0,}[-][1-9][\d]{0,}$/g.test(seg)) {
					continue;
				}
				return ERROR_EXPRE;
			}
			return PASS;
		}
	}
	
	//添加到待解除绑定端口列表;
	function addNeeds(){
		var v = $("#expressionInput").val().trim();
		if($.inArray(v, needArray ) > -1 ){
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.unbindRepeat@", null, function(){
				$("#expressionInput").focus();
			});
			return false;
		}
		if(checkExpressionInput(v)){
			var str = '<span class="deleteSpan">';
				str += v;
				str += '<a href="javascript:;" name="'+ v +'" class="deleteSpanClose nm3kTip" nm3kTip="@BATCH.delThMark@" onclick="deleteSpan(this)"></a></span>';
			$("#needBindingPort").append(str);
			needArray.push(v);
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
		needArray.remove($(_this).attr("name"));
	}
	
	function cancelClick() {
		window.top.closeWindow('batchUnBind');
	}
	
	function batchUnBind(){
		if(needArray.length > 0){
			var needExpression = needArray.join("_");
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/univlanprofile/batchUnBindProfile.tv',
				type : 'post',
				dataType: 'json',
				data : {
					entityId : entityId,
					needBinds : needExpression
				},
				success : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", String.format("@BATCH.batchunbindsuccess@", json.successCount, json.failedCount), null, function(){
						top.frames["frameuniVlanProfile"].refreshGridData();
						cancelClick();
					});
					//ZetaUtils.getIframe('frameuniVlanProfile').refreshGridData();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.unbindFaild@");
				},
				cache : false
			}); 
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@BATCH.pleasAdd2UnbindList@",null,function(){
				$("#expressionInput").focus();
			});
			return;
		}
	}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@BATCH.unbindTitle@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edge10">
		<div class="yellowTip">@BATCH.tip1@ @BATCH.tip2@ @BATCH.tip3@ @BATCH.tip4@ @BATCH.tip5@</div>
	</div>
	<div class="edgeTB10LR20 pT10">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
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
	                 			<a href="javascript:;" class="normalBtn" onclick="addNeeds()"><span><i class="miniIcoAdd"></i>@BATCH.add2unbindList@</span></a> 
	                 		</li>
	                 	</ul>
	             	</td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@BATCH.unbindList@</td>
	             	<td>
	             		<div id="needBindingPort"></div>
	             	</td>
	             </tr>
			</tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="batchUnBind()"><span><i class="bmenu_miniIcoBroken"></i>@BATCH.unbind@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>