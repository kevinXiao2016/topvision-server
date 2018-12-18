<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = '${entityId}'; 
var portId = '${portId}'; 
var portType = '${portType}'; 
function saveClick(){
	var startTime = $("#startTime").val();
	var endTime = $('#endTime').val();
	if(!checkInput(startTime) || startTime<1 || startTime>1440){
		Zeta$("startTime").focus();
		return ;
	}
	if(!checkInput(endTime) || endTime<1 || endTime>10080){
		Zeta$("endTime").focus();
		return ;
	}
	window.parent.showConfirmDlg(I18N.EPON.tip, I18N.EPON.portPerfParaConfig, function(type) {
        if (type == 'no')
            return                    
        window.top.showWaitingDlg(I18N.EPON.wait, I18N.EPON.portPerfParaSetting, 'ext-mb-waiting');
        if(portType == 'sni'){
        	$.ajax({
                url: '/epon/modifySni15MinPerfStatus.tv',  type: 'POST',
                data: "entityId=" + entityId + "&sniId=" + portId + "&startTime="+startTime +"&endTime="+endTime,
                success: function(json) {
                    if (json.message == 'success') {
                		window.parent.showMessageDlg(I18N.EPON.tip, I18N.EPON.stastic15Ok)
                		cancelClick();
                	} else {
                    	if(json.message == '0'){
                    		window.parent.showMessageDlg(I18N.EPON.tip,I18N.EPON.port15PerfCollection);
                        }else{
                        	window.parent.showMessageDlg(I18N.EPON.tip,String.format(I18N.EPON.settingPort15En , json.message));
                        }
                		cancelClick();
                		//olt.slotList[currentId.split("_")[1]-1].portList[currentId.split("_")[2]-1].sniPerfStats15minuteEnable = sni15MinPerfStatusTemp;                    		
                	}
                }, error: function() {
                	window.parent.showMessageDlg(I18N.EPON.tip,I18N.EPON.stastic15Error);
                	cancelClick();
            	}, dataType: 'json', cache: false
            })
        }else if(portType == 'pon'){
        	$.ajax({
                url: '/epon/modifyPon15MinPerfStatus.tv',  type: 'POST',
                data: "entityId=" + entityId + "&ponId=" + portId + "&startTime="+startTime +"&endTime="+endTime,
                success: function(json) {
                    if (json.message == 'success') {
                		window.parent.showMessageDlg(I18N.EPON.tip, I18N.EPON.stastic15Ok)
                		cancelClick();
                	} else {
                    	if(json.message == '0'){
                    		window.parent.showMessageDlg(I18N.EPON.tip,I18N.EPON.port15PerfCollection);
                        }else{
                        	window.parent.showMessageDlg(I18N.EPON.tip,String.format(I18N.EPON.settingPort15En , json.message));
                        }
                		cancelClick();
                		//olt.slotList[currentId.split("_")[1]-1].portList[currentId.split("_")[2]-1].sniPerfStats15minuteEnable = sni15MinPerfStatusTemp;                    		
                	}
                }, error: function() {
                	window.parent.showMessageDlg(I18N.EPON.tip,I18N.EPON.stastic15Error);
                	cancelClick();
            	}, dataType: 'json', cache: false
            })
         }
    })
}
function cancelClick() {
	window.parent.closeWindow('portPerfCycle');	
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 200px; boder: 5px;">
		<fieldset
			style='width: 100%; height: 110px; background-color: #ffffff'>
			<table cellspacing=20 cellpadding=8>
				<tr height=20px>
					<td width=145px align=right><label>
						<fmt:message bundle="${i18n}" key="EPON.startCollectionTime" />:</label></td>
					<td align=center>
					<input type="text" id="startTime" value="15"
					onfocus="inputFocused('startTime', '<fmt:message bundle="${i18n}" key="EPON.startTimeTip" />', 'iptxt_focused')"
					size=20  maxlength=4 onblur="inputBlured(this, 'startTime');" onclick="clearOrSetTips(this);" >&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.M" /></td>
				</tr>
				<tr height=20px>
					<td width=145px align=right><label>
						<fmt:message bundle="${i18n}" key="EPON.endCollectionTime" />:</label></td>
			        <td align=center><input type="text" id="endTime" value="1440" size=20 maxlength=5
			        onfocus="inputFocused('endTime', '<fmt:message bundle="${i18n}" key="EPON.endTimeTip" />', 'iptxt_focused')"
							onblur="inputBlured(this, 'endTime');" onclick="clearOrSetTips(this);">&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.M" /></td>
				</tr>
			</table>
		</fieldset>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<button id="okBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle="${i18n}" key="COMMON.save" /></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>

	</div>
</BODY>
</HTML>