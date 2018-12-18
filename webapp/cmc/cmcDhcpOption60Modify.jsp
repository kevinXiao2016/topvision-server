<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}

</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var _DEFAULTOPTION60_ = ["docsis", "pktc", "stb"];
var bundleInterface = "${bundleInterface}";
var bundleInterfaceEnd ;
var deviceType = ${deviceType};
var entityId = ${entityId};
var option60s = ${option60s}
/************************************************************************************************************
 * 数据校验
 ************************************************************************************************************/
function isCharacter(val){
    var reg = /^[A-Za-z0-9_]{1,16}$/;
    return reg.test(val);
}
function isConflict(val){
	if(option60s){
		for(var i=0; i<option60s.length; i++){
			if(option60s[i].topCcmtsDhcpOption60Str == val){
				return true;
			}
		}
	}
	return false;
}
function containsDefaultOption60(val){
	if(val.indexOf(_DEFAULTOPTION60_[0])!=-1 || val.indexOf(_DEFAULTOPTION60_[1])!=-1 || 
			val.indexOf(_DEFAULTOPTION60_[2])!=-1){
		return true;
	}else{
		return false;
	}
}
function cancelClick() {
	window.parent.closeWindow('dhcpOption60Modify');
}
function saveClick(){
	var option60Str = $("#option60Str").val();
	deviceType = $("#deviceTypeSelect").val();
	if(!isCharacter(option60Str)){
	    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.option60Format);
	    $("#option60Str").focus();
	    return;
	}
	if(isConflict(option60Str)){
	    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.option60Conflict);
	    $("#option60Str").focus();
	    return;
	}
	if(containsDefaultOption60(option60Str)){
	    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.option60ContainDef);
	    $("#option60Str").focus();
	    return;
	}
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
      url: '/cmc/dhcprelay/addDhcpOption60.tv?entityId='+entityId+"&bundleInterface=" +bundleInterface
    		  +"&deviceType=" + deviceType+ "&option60Str=" + option60Str,
      type: 'post',
      dataType:'json',
      success: function(response) {
            if(response.message == "success"){                  
                //window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addSuccess);
                window.top.closeWaitingDlg();
                top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
	   				html: '<b class="orangeTxt">'+I18N.CMC.text.addSuccess+'</b>'
   				});
                window.parent.getFrame("entity-" + entityId).onRefresh();
                cancelClick();
             }else{
                 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addFailure);
             }
        }, error: function(response) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addFailure);
        }, cache: false
    });
}
function checkIsFilled(){
	if($("#bundleSelect").val()==0 || $("#deviceTypeSelect").val()==0 || $("#option60Str").val()== null){
		return false;
	}
	return true;
}
//转换Bundle号为数字
function transferBundleToNumber(val){
    if(typeof val == "string"){
        var bundleArray = val.replace("bundle", "").split(".");
        var number;
        if(bundleArray.length > 1){
            number = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
        }else{
            number = parseInt(bundleArray[0]);
        }
        return number;
    }else{
        return val;
    }
    
}
$(function (){
    bundleInterfaceEnd = transferBundleToNumber(bundleInterface);
	$("#relayConfigIndex").val(bundleInterfaceEnd);
	
});
</script>
</HEAD>
<body  class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    Bundle ID:
	                </td>
	                <td>
	                    <input style="width:200px" id="relayConfigIndex" maxlength=36 disabled="disabled" class="normalInputDisabled" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <fmt:message bundle='${cmc}' key='CMC.label.type'/>:
	                </td>
	                <td>
	                    <select id="deviceTypeSelect" style="width: 200px;" name="type" disabled="disabled" class="" >
							<option value="0" <s:if test="deviceType==0">selected</s:if>><fmt:message bundle='${cmc}' key='CMC.text.pleaseselec'/></option>
							<option value="1" <s:if test="deviceType==1">selected</s:if>>cm</option>
							<option value="2" <s:if test="deviceType==2">selected</s:if>>host</option>
							<option value="3" <s:if test="deviceType==3">selected</s:if>>mta</option>
							<option value="4" <s:if test="deviceType==4">selected</s:if>>stb</option>
						</select>
	                </td>
	            </tr>
	                 <td class="rightBlueTxt">
	                    option60:
	                </td>
	                <td>
	                    <input id="option60Str" class="normalInput" style="width:200px;" maxlength="16" toolTip="<fmt:message bundle='${cmc}' key='CMC.text.option60Tip'/>"
					   />
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a id="okBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i><fmt:message bundle='${cmc}' key='CMC.button.modifyConfig'/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></span></a></li>
		     </ol>
		</div>
	</div>
	
	

	
</BODY>
</HTML>