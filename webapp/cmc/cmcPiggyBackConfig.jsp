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
<script type="text/javascript">
var cmcId = "${cmcId}";
var cmcSysConfig = ${cmcSysConfig};
function closeClick(){
	window.parent.closeWindow('cmcPiggyBackConfig');
}
function saveClick(){
	var piggyBack = $(":radio[name='piggyBackSwitch'][checked=true]").val();	
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
        url: "/cmc/config/modifyCmcPiggyBackConfig.tv?cmcId=" + cmcId + "&piggyBack=" + piggyBack,
        type: "POST",
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
	if(cmcSysConfig){
		$(":radio[name='piggyBackSwitch']").each(function (){
			if(this.value == cmcSysConfig.topCcmtsSysCfgPiggyback){
				this.checked = true;
			}
		});
	}
});
</script>
</head>
<body class="POPUP_WND" 
    style="padding-top: 15px; padding-left: 15px; padding-right: 15px; padding-bottom: 15px">
    <fieldset style="background-color: #ffffff; width: 315px; height: 50px;" align="center">
    <div align="center" style="margin-top:15px">
        <label>piggyback:</label>
        <label><input type="radio" name="piggyBackSwitch" value="1" /><fmt:message bundle="${cmc}" key="CMC.select.open" /></label>&nbsp;&nbsp;
        <label><input type="radio" name="piggyBackSwitch" value="2" checked="true" /><fmt:message bundle="${cmc}" key="CMC.select.close" /></label>
    </div>
    </fieldset>
    <div style="padding-top: 15px; padding-left: 15px;">
        <label><fmt:message bundle="${cmc}" key="CMC.title.tip" />:</label><br/>
        <label><fmt:message bundle="${cmc}" key="CMC.tip.piggybackTip" /></label>
    </div>
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