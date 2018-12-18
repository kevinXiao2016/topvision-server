<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
    module platform
</Zeta:Loader>
<script type="text/javascript">
var name = '${entity.name}';
var entityId = '${entityId}';
var macList = '${onuMacsJson}';
var cmcIndex = '${cmcIndex}';
var cmcId = '${cmcId}';
var entityTypeId = '${entity.typeId}';
var pageId = 'framecmcList';
 
function cancelClick() {
	window.top.closeWindow('replaceCmcII');
}
//只能MAC地址
function validate(str){
 
}

function validateMacExists(str){
	var exist = false, macArray = JSON.parse(macList);
	for (var i=0; i<macArray.length; i++) {
	   if (macArray[i].toUpperCase() == str) {
			 exist = true;
			 break;
	   }
	}
	return exist;
}

function replaceMacFunc(replaceMac,forceReplace){
	window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
	$.ajax({
        url: '/cmc/replace/replaceCmcII.tv',
        data:{
        	entityId:entityId,
        	cmcId : cmcId,
        	cmcIndex : cmcIndex,
        	cmcMac : replaceMac,
        	forceReplace : forceReplace
        },
        type : 'POST',
        success: function(response) {
	        window.top.closeWaitingDlg();
	        if(response == 'success'){
	            top.afterSaveOrDelete({
   				   title: '@COMMON.tip@',
   				   html: '<b class="orangeTxt">@cmc/CCMTS.replace.success@</b>'
   			    });
	            top.frames[pageId].refreshClick();
	            cancelClick();
	        } else if(response == 'cmcforcereplaceerror'){
	        	// 未能正常解绑定设备，强制替换失败，请重新拓扑OLT后进行重试
	        	window.parent.showMessageDlg('@COMMON.error@', '@cmc/CCMTS.replace.forcereplaceerror@');
	        } else if(response == 'seterror'){
	        	// 强制替换失败，请重新拓扑OLT后进行重试
	        	window.parent.showMessageDlg('@COMMON.error@', '@cmc/CCMTS.replace.seterror@');
	        } else if(response == 'error'){
	        	// 替换失败
	            window.parent.showMessageDlg('@COMMON.error@', '@cmc/CCMTS.replace.error@');
	        }
        },
        error: function(response) {
            window.parent.showMessageDlg('@COMMON.error@', '@cmc/CCMTS.replace.error@');
        },
        cache: false
    });
}



function replace() {
    // 校验MAC地址合法性
	var inputMac   = $("#replaceMac").val(), 
	    replaceMac = Validator.formatMac(inputMac);
    
	if(!Validator.isMac(replaceMac)){
		$("#replaceMac").focus();
		top.afterSaveOrDelete({
			title : '@COMMON.tip@',
			html  : '@cmc/CCMTS.replace.macInvalid@'
		});
		return false;
	}
	if(replaceMac === '00:00:00:00:00:00'){
		$("#replaceMac").focus();
		top.afterSaveOrDelete({
			title : '@COMMON.tip@',
			html  : '@cmc/CCMTS.replace.macInvalid2@'
		});
		return false; 
	}
	// replaceMac = $("#replaceMac").val().toUpperCase();
	// 校验MAC地址唯一性
	if( replaceMac == Validator.formatMac('${entity.mac}') ){
		window.parent.showMessageDlg(I18N.COMMON.tip, '@cmc/CCMTS.replace.macnochange@');
		return;
	}
	var oMacList = Ext.decode(macList);
	if(oMacList[replaceMac]){//强制替换;
		//如果typeId 不一样，直接提示：系统中存在这个设备，两种设备类型不相同，不支持替换;
		if(entityTypeId != oMacList[replaceMac].typeId){
			var tipStr = String.format('@cmc/CCMTS.replace.forcereplacetip@',inputMac,oMacList[replaceMac].name);
			top.showMessageDlg('@COMMON.tip@', tipStr, 'error2',function(){});
			return;
		}
	    var msg = String.format('@cmc/CCMTS.replace.forcereplacetip2@',inputMac,oMacList[replaceMac].name);
	    window.parent.showConfirmDlg(I18N.COMMON.tip, msg, function(button, text) {
	        if (button == "yes") {
	        	replaceMacFunc(replaceMac,1);
	        }
		})
	} else{
		replaceMacFunc(replaceMac,0);
	}
}
</script>
	</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<div class="openWinTip">@cmc/CCMTS.replace.replace@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT30">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0"
				border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt withoutBorderBottom w160">MAC:</td>
						<td class="withoutBorderBottom"><input
							class="normalInput modifiedFlag w200" id="replaceMac"
							value="${entity.mac}" maxlength="63" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="replace()" icon="miniIcoData">@cmc/CCMTS.replace.replace@</Zeta:Button>
			<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>