<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript">
var cmcId = "${cmcId}";
var shareSecretConfig = ${cmcShareSecretConfig};
function inputStatusChangeBySwitch(){
	var v = $(":radio[name='shareSecretSwitch'][checked=true]").val();
	if(v == "2"){
		$(":radio[name='shareSecretEncryptedSwitch']").each(function (){
			this.disabled = true;
		});
		$("#sharedSecretAuthStr").attr("disabled", true);
		$("#sharedSecretCipherStr").attr("disabled", true);
	}else{
		$(":radio[name='shareSecretEncryptedSwitch']").each(function (){
            this.disabled = false;
        });
		inputStatusChangeByEncrypted();
	}
}
function inputStatusChangeByEncrypted(){
	var v = $(":radio[name='shareSecretEncryptedSwitch'][checked=true]").val();
	if(v == "7"){
		$("#sharedSecretAuthStr").attr("disabled", true);
        $("#sharedSecretCipherStr").attr("disabled", false);
	}else{
		$("#sharedSecretAuthStr").attr("disabled", false);
        $("#sharedSecretCipherStr").attr("disabled", true);
	}
}
function closeClick(){
    window.parent.closeWindow("cmcShareSecretConfig");
}
function saveClick(){   
	var data = "";
    var shareSecretSwitch = $(":radio[name='shareSecretSwitch'][checked=true]").val();
    data += "cmcShareSecretSetting.sharedSecretEnabled=" + shareSecretSwitch;
    if(shareSecretSwitch == "1"){
        var shareSecretEncryptedSwitch = $(":radio[name='shareSecretEncryptedSwitch'][checked=true]").val();
        data += "&cmcShareSecretSetting.sharedSecretEncrypted=" + shareSecretEncryptedSwitch;
        if(shareSecretEncryptedSwitch == "7"){
            data += "&cmcShareSecretSetting.sharedSecretCipherStr=" + $("#sharedSecretCipherStr").val();
        }else{
            data += "&cmcShareSecretSetting.sharedSecretAuthStr=" + $("#sharedSecretAuthStr").val();
        }
    }
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');    
    $.ajax({
        url: "/cmc/sharesecret/modifyCmcShareSecretConfig.tv?cmcId=" + cmcId,
        type: "POST",
        data: data,
        success: function (response){
            if(response == "true"){
                window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifySuccessTip);
                setTimeout(function (){
                    window.top.closeWaitingDlg(I18N.RECYLE.tip);                      
                    cancelClick();
                }, 500);  
            }else{
                window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
            }
        },
        error: function (response){
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
        },
        cache: false        
    });
}
$(function (){
    if(shareSecretConfig){
    	$(":radio[name='shareSecretSwitch']").each(function (){
    		if( this.value == shareSecretConfig.sharedSecretEnabled){
    			this.checked = true;
    		}
    	});
    	$(":radio[name='shareSecretEncryptedSwitch']").each(function (){
            if(this.value == shareSecretConfig.sharedSecretEncrypted){
                this.checked = true;
            }
        });
    	$("#sharedSecretAuthStr").val(shareSecretConfig.sharedSecretAuthStr);
    	$("#sharedSecretCipherStr").val(shareSecretConfig.sharedSecretCipherStr);   	
    }
    inputStatusChangeBySwitch();
});
</script>
</head>
<body class="POPUP_WND" 
    style="padding-top: 15px; padding-left: 15px; padding-right: 15px; padding-bottom: 15px">
    <div class=formtip id=tips style="display: none"></div>
    <fieldset style="background-color: #ffffff; width: 415px; height: 200px;" align="center">
    <div align="center" style="margin-top:15px">
        <table width=100% cellspacing=12 cellpadding=3>
            <tr>
                <td width=100px align="right">ShareSecret Switch:</td>
                <td width=230px align="left">
                    <input type="radio" name="shareSecretSwitch" value="1" onclick="inputStatusChangeBySwitch()" /><fmt:message bundle="${cmc}" key="CMC.select.open"/>&bnsp;&nbsp;
                    <input type="radio" name="shareSecretSwitch" value="2" onclick="inputStatusChangeBySwitch()" checked /><fmt:message bundle="${cmc}" key="CMC.select.close"/>
                </td>
            </tr>
            <tr>
                <td align="right">Plain/Auth:</td>
                <td align="left">
                    <input type="radio" name="shareSecretEncryptedSwitch" value="0" onclick="inputStatusChangeByEncrypted()" checked/>明文&bnsp;&nbsp;
                    <input type="radio" name="shareSecretEncryptedSwitch" value="7" onclick="inputStatusChangeByEncrypted()" />密文
                </td>
            </tr>
            <tr>
                <td align="right">Plain:</td>
                <td align="left">
                    <input type="text" id="sharedSecretAuthStr" style="width: 230px;" maxlength=16 class="iptxt"
	                    onfocus="inputFocused('sharedSecretAuthStr', '1-16 bits', 'iptxt_focused')"
	                    onblur="inputBlured(this, 'iptxt');"
	                    onclick="clearOrSetTips(this);"/>
                </td>
            </tr>
            <tr>
                <td align="right">Auth:</td>
                <td align="left">
                    <input type="text" id="sharedSecretCipherStr" style="width: 230px;" maxlength=32 class="iptxt"
                        onfocus="inputFocused('sharedSecretCipherStr', '32 bits', 'iptxt_focused')"
                        onblur="inputBlured(this, 'iptxt');"
                        onclick="clearOrSetTips(this);"/>
                </td>
            </tr>
        </table>
    </div>
    </fieldset>
    <div align="right" style="padding-top:15px;">
        <button id=save class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
            onMouseOut="this.className='BUTTON75'"
            onmousedown="this.className='BUTTON_PRESSED75'" onclick="saveClick()">
            <fmt:message bundle="${resources}" key="COMMON.save" />
        </button>
        &nbsp;&nbsp;
        <button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
            onMouseOut="this.className='BUTTON75'"
            onmousedown="this.className='BUTTON_PRESSED75'"
            onclick="closeClick();">
            <fmt:message bundle="${resources}" key="COMMON.close" />
        </button>
    </div>
</body>
</html>