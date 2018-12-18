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
    IMPORT js/jquery/Nm3kTabBtn
</Zeta:Loader>
<style type="text/css">
.jsType {
	display: none;
}
</style>
<script type="text/javascript">
var pageId = '${pageId}';
var mac = '${entity.mac}';
var entityId = '${entityId}';
var entityTypeId = '${entity.typeId}';
var onuId = '${onuId}';
var macList = '${onuMacsJson}';
var snList = '${onuSnJson}';
var onuIndex = '${onuIndex}';
var onuIndexString = '${onuIndexString}';
var ponAuthType = '${ponAuthType}';

var needValidate = {
	type1 : {
		validataMac : { //需要验证MAC;
			inputId : 'replaceMac',  //输入框的id;
		    errTip  : '@epon/onu.replace.macInvalid@'
		},
		isSpecialMac : { //不能是00:00:00:00:00:00
			inputId : 'replaceMac',  //输入框的id;
		    errTip  : '@epon/onu.replace.macInvalid2@'
		}
	},
	type3_1 : {  //ponAuthType为3，认证方式MAC;
		validataMac : { //需要验证MAC;
			inputId : 'replaceMixMac',  //输入框的id;
			errTip  : '@epon/onu.replace.macInvalid@'
		},
		isSpecialMac : { //不能是00:00:00:00:00:00
			inputId : 'replaceMixMac',  //输入框的id;
		    errTip  : '@epon/onu.replace.macInvalid2@'
		}
	},
	type3_2 : { //ponAuthType为3，认证方式SN;
		validateSn  : { //需要验证SN;
			inputId : 'replaceMixSn',   //sn的输入框id;
		    errTip  : '@epon/onu.replace.snInvalid@'
		}
	},
	type3_3 : {
		validateSn  : { //需要验证SN;
			inputId : 'replaceMixSn',
			errTip  : '@epon/onu.replace.snInvalid@'
		},          
	    validatePass : { //需要验证密码;
	    	inputId : 'replaceMixPwd',
	    	errTip  : '@epon/onu.replace.pwdInvalid@'
	    }          
	},
	type4 : { //ponAuthType为4;
		validateSn  : { //需要验证SN;
			inputId : 'replaceSn',   //sn的输入框id;
			errTip  : '@epon/onu.replace.snInvalid@'
		}
	},
	type5 : {
		validateSn  : { //需要验证SN;
			inputId : 'replaceSn1',
			errTip  : '@epon/onu.replace.snInvalid@'
		},          
	    validatePass : { //需要验证密码;
	    	inputId : 'replacePwd',
	    	errTip  : '@epon/onu.replace.pwdInvalid@'
	    }   
	}
}; 
//验证password;
function checkedPw(pw){
    var reg = /^([0-9a-z])+$/ig;
    if(pw == null || pw == "" || pw == undefined){
        return false;
    }else{
        if(pw.length < 13 && reg.exec(pw)){
            return true;
        }
    }
    return false;
}
//验证sn;
function checkedSn(sn){
    var reg = /^([0-9a-z])+$/ig;
    if(sn == null || sn == "" || sn == undefined){
        return false;
    }else{
        if(sn.length < 25 && reg.exec(sn)){
            return true;
        }
    }
    return false;
}

function cancelClick() {
	window.top.closeWindow('replaceOnu');
}

/* function validateMacExists(str){
	var exist = false, macArray = JSON.parse(macList);
	for (var i=0; i<macArray.length; i++) {
	   if (macArray[i].toUpperCase() == str) {
			 exist = true;
			 break;
	   }
	}
	return exist;
} */

function replaceMacFunc(replaceMac,forceReplace){
	window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
	$.ajax({
        url: '/onu/replace/replaceOnuEntityByMac.tv',
        data:{
        	entityId:entityId,
        	onuId : onuId,
        	onuIndex : onuIndex,
        	onuMac : replaceMac,
        	forceReplace : forceReplace
        },
        type: 'post',
        success: function(response) {
	        window.top.closeWaitingDlg();
	        if(response == 'success'){
	            top.afterSaveOrDelete({
   				   title: '@COMMON.tip@',
   				   html: '<b class="orangeTxt">@epon/onu.replace.success@</b>'
   			    });
	            top.frames.getFrame(pageId).onRefreshClick(top.frames.getFrame(pageId).grid);
	            cancelClick();
	            } else if(response == 'onuforcereplaceerror'){
	            	// 未能正常解绑定设备，强制替换失败，请重新拓扑OLT后进行重试
	        	    window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.forcereplaceerror@');
	            } else if(response == 'seterror'){
	            	// 替换失败，请重新拓扑OLT后进行重试
	        	    window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.seterror@');
	            } else if(response == 'error'){
	            	// 替换失败
	            	window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.error@');
	            }
	         
        },
        error: function(response) {
            window.top.closeWaitingDlg();
            window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.error@');
        },
        cache: false
    });
}

function replaceSnFunc(replaceSn,replacePwd,forceReplace){
	window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
	$.ajax({
        url: '/onu/replace/replaceOnuEntityBySnAndPwd.tv',
        data:{
        	entityId:entityId,
        	onuId : onuId,
        	onuIndex : onuIndex,
	        sn : replaceSn,
	        pwd : replacePwd,
        	forceReplace : forceReplace
        },
        type: 'post',
        success: function(response) {
	        window.top.closeWaitingDlg();
	        if(response == 'success'){
	            top.afterSaveOrDelete({
   				    title: '@COMMON.tip@',
   				    html: '<b class="orangeTxt">@epon/onu.replace.success@</b>'
   			    });
	            top.frames.getFrame(pageId).onRefreshClick(top.frames.getFrame(pageId).grid);
	            cancelClick();
	        } else if(response == 'onuforcereplaceerror'){
	        	// 未能正常解绑定设备，强制替换失败，请重新拓扑OLT后进行重试
        	    window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.forcereplaceerror@');
            } else if(response == 'seterror'){
            	// 替换失败，请重新拓扑OLT后进行重试
        	    window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.seterror@');
            } else if(response == 'error'){
            	// 替换失败
            	window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.error@');
            }
	        
        },
        error: function(response) {
            window.top.closeWaitingDlg();
            window.parent.showMessageDlg('@COMMON.error@', '@epon/onu.replace.error@');
        },
        cache: false
});
}



function replace(type) {
	var replaceMac,inputMac;
	if(type == 0){
		inputMac = $("#replaceMac").val();
		replaceMac = Validator.formatMac(inputMac);
	} else if(type == 1){
		inputMac = $("#replaceMixMac").val();
		replaceMac = Validator.formatMac(inputMac);
	}
	// 校验MAC地址唯一性
	if( replaceMac == Validator.formatMac('${entity.mac}') ){
		window.parent.showMessageDlg(I18N.COMMON.tip, '@epon/onu.replace.macnochange@');
		return;
	}
	var oMacList = Ext.decode(macList);
	if(oMacList[replaceMac]){//强制替换;
		//如果typeId 不一样，直接提示：系统中存在这个设备，两种设备类型不相同，不支持替换;
		if(entityTypeId != oMacList[replaceMac].typeId){
			var tipStr = String.format('@epon/onu.replace.forcereplacetip@',inputMac,oMacList[replaceMac].name);
			top.showMessageDlg('@COMMON.tip@', tipStr, 'error2',function(){});
			return;
		}
	    var msg = String.format('@epon/onu.replace.forcereplacetip2@',inputMac,oMacList[replaceMac].name);
		window.parent.showConfirmDlg(I18N.COMMON.tip, msg, function(button, text) {
	        if (button == "yes") {
	        	replaceMacFunc(replaceMac,1);
	        }
		})
	}else{//非强制替换;
		replaceMacFunc(replaceMac,0);
	}
}


function replaceSn(type) {
	var replaceSn,
	    replacePwd;
	if(type == 1){
		replaceSn = $("#replaceSn").val();
	} else if(type == 2){
		replaceSn = $("#replaceSn1").val();
		replacePwd = $("#replacePwd").val();
	} else if(type ==3){
		replaceSn = $("#replaceMixSn").val();
	} else if(type ==4){
		replaceSn = $("#replaceMixSn").val();
		replacePwd = $("#replaceMixPwd").val();
	}
	if(replaceSn == '${oltAuthentication.topOnuAuthLogicSn}' && (replacePwd === undefined || replacePwd === '${oltAuthentication.topOnuAuthPassword}')){
		window.parent.showMessageDlg(I18N.COMMON.tip, '@epon/onu.replace.snnochange@');
		return;
	}
	var oSnList = JSON.parse(snList);
	if(oSnList[replaceSn]){//强制替换;
		//如果typeId 不一样，直接提示：系统中存在这个设备，两种设备类型不相同，不支持替换;
		if(entityTypeId != oSnList[replaceSn].typeId){
			var tipStr = String.format('@epon/onu.replace.forcereplacetip@',replaceSn,oSnList[replaceSn].name);
			top.showMessageDlg('@COMMON.tip@', tipStr, 'error2',function(){});
			return;
		}
		var msg = String.format('@epon/onu.replace.forcereplacetip2@',replaceSn,oSnList[replaceSn].name);
		window.parent.showConfirmDlg(I18N.COMMON.tip, msg, function(button, text) {
	        if (button == "yes") {
	        	replaceSnFunc(replaceSn, replacePwd, 1);
	        }
		})
	}else{//非强制替换;
		replaceSnFunc(replaceSn, replacePwd, 0);
	}
		
}
 
var mixType = 0;
function replaceMix(){
	if(mixType == 0){
		if(validateFn(needValidate.type3_1)){
			replace(1);
		};
	}else if(mixType == 1){
		if(validateFn(needValidate.type3_2)){
			replaceSn(3);
		};
	}else if(mixType == 2){
		if(validateFn(needValidate.type3_3)){
			replaceSn(4);			
		};
	}
};
function saveClick(num){
	switch(num){
		case 0:
			if(validateFn(needValidate.type1)){
				replace(num);	
			}
		break;
		case 1:
			if(validateFn(needValidate.type4)){
				replaceSn(num);
			}
		break;
		case 2:
			if(validateFn(needValidate.type5)){
				replaceSn(num);
			}	
		break;
	}
}

function validateFn(o){
	for(var i in o){
		var $input = $("#" + o[i]["inputId"]),
		    errTip = o[i]["errTip"];
		
		switch(i){
			case 'validataMac':
				var newValue = Validator.formatMac( $input.val() );
				//$input.val(newValue);
				if(!Validator.isMac(newValue)){
					$input.focus();
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html  : errTip
					});
					return false;
				}
			break;
			case 'isSpecialMac':
				var newValue = Validator.formatMac( $input.val() );
				if(newValue === '00:00:00:00:00:00'){
					$input.focus();
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html  : errTip
					});
					return false;
				}
			break;
			case 'validateSn':
				if(!checkedSn( $input.val())){
					$input.focus();
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html  : errTip
					});
					return false;
				}
			break;
			case 'validatePass':
				if(!checkedPw( $input.val() )){
					$input.focus();
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html  : errTip
					});
					return false;
				}	
			break;
		}
	}
	return true;
}
 
 

$(function(){
	//ponAuthType; 1,2是同一个,3是一种 ,4是一个，5是另一个
	$(".jsType").css({display:'none'});
	if(ponAuthType == '4'){
		$(".jsType:eq(1)").css({display:'block'});	
	}else if(ponAuthType == '5'){
		$(".jsType:eq(2)").css({display:'block'});
	}else if(ponAuthType == '3'){
		$(".jsType:eq(3)").css({display:'block'});
	}else{
		$(".jsType:eq(0)").css({display:'block'});
	}
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putTab",
	    callBack:"tabDiv",
	    tabs:["MAC","SN","SN+PASSWORD"]
	});
	tab1.init();
});//end document.ready;

function tabDiv(index){
	mixType = index;
	var $allbody = $("#macBody, #snBody, #passBody"),
	    $macBody = $("#macBody"),
	    $snBody = $("#snBody"),
	    $passBody = $("#passBody");
	
	$allbody.css({display:'none'});
	/* $snBody.find("tr").removeClass("darkZebraTr");
	$passBody.find("tr").removeClass("darkZebraTr"); */
	switch(index){
		case 0:
			$macBody.css("display","");
			break;
		case 1:
			$snBody.css("display","");
			break;
		case 2:
			$snBody.css("display","");
			$passBody.css("display","");
			//$passBody.addClass("darkZebraTr");
			break;
	}
}
</script>
	</head>
	<body class="openWinBody">
		<div class="openWinHeader">
			<div class="openWinTip">@epon/onu.replace.replace@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="jsType">
			<div class="edgeTB10LR20 pT30">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt" width="180">@epon/onu.replace.onuindex@:</td>
							<td>${onuIndexString}</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">MAC:</td>
							<td><input class="normalInput modifiedFlag w200"
								id="replaceMac" value="${entity.mac}"
								maxlength="63" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB0 pT40 noWidthCenter">
					<li><a onClick="saveClick(0)" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoTwoArr"></i>@epon/onu.replace.replace@</span></a></li>
					<li><a onClick="cancelClick()" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				</ol>
			</div>
		</div>

		<div class="jsType">
			<div class="edgeTB10LR20 pT30">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt" width="180">@epon/onu.replace.onuindex@:</td>
							<td>${onuIndexString}</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">SN:</td>
							<td><input class="normalInput modifiedFlag w200" maxlength="24" toolTip="@epon/onu.replace.tip1@"
								id="replaceSn" value="${oltAuthentication.topOnuAuthLogicSn}" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB0 pT40 noWidthCenter">
					<li><a onClick="saveClick(1)" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoTwoArr"></i>@epon/onu.replace.replace@</span></a></li>
					<li><a onClick="cancelClick()" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				</ol>
			</div>
		</div>

		<div class="jsType">
			<div class="edgeTB10LR20 pT30">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt" width="180">@epon/onu.replace.onuindex@:</td>
							<td>${onuIndexString}</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">SN:</td>
							<td><input class="normalInput modifiedFlag w200" maxlength="24" toolTip="@epon/onu.replace.tip1@"
								id="replaceSn1" value="${oltAuthentication.topOnuAuthLogicSn}" />
							</td>
						</tr>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt" width="180">PASSWORD:</td>
							<td><input class="normalInput modifiedFlag w200" maxlength="12" toolTip="@epon/onu.replace.tip2@"
								id="replacePwd" value="${oltAuthentication.topOnuAuthPassword}" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB0 pT20 noWidthCenter">
					<li><a onClick="saveClick(2)" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoTwoArr"></i>@epon/onu.replace.replace@</span></a></li>
					<li><a onClick="cancelClick()" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				</ol>
			</div>
		</div>

		<div class="jsType">
			<div class="edgeTB10LR20">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt" width="180">@epon/onu.replace.onuindex@:</td>
							<td>${onuIndexString}</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@epon/onu.replace.authType@:</td>
							<td id="putTab"></td>
						</tr>
					</tbody>
					<tbody id="macBody">
						<tr class="darkZebraTr">
							<td class="rightBlueTxt">MAC:</td>
							<td><input class="normalInput modifiedFlag w200"
								id="replaceMixMac"
								value="${entity.mac}" /></td>
						</tr>
					</tbody>
					<tbody id="snBody" style="display: none">
						<tr class="darkZebraTr">
							<td class="rightBlueTxt">SN:</td>
							<td><input class="normalInput modifiedFlag w200" maxlength="24" toolTip="@epon/onu.replace.tip1@"
								id="replaceMixSn" value="${oltAuthentication.topOnuAuthLogicSn}" />
							</td>
						</tr>
					</tbody>
					<tbody id="passBody" style="display: none">
						<tr>
							<td class="rightBlueTxt" width="180">PASSWORD:</td>
							<td><input class="normalInput modifiedFlag w200"
								id="replaceMixPwd" maxlength="12" toolTip="@epon/onu.replace.tip2@"
								value="${oltAuthentication.topOnuAuthPassword}" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB0 pT0 noWidthCenter">
					<li><a onClick="replaceMix()" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoTwoArr"></i>@epon/onu.replace.replace@</span></a></li>
					<li><a onClick="cancelClick()" href="javascript:;"
						class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				</ol>
			</div>
		</div>


	</body>
</Zeta:HTML>