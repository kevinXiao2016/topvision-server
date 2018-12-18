<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<TITLE>Download Config</TITLE>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript" src="main.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var cmcId = ${cmcId};
var filename;
var isBusy = 0;
//只能输入英文字母、数字、下划线
function validateFileName(val){ 
    if(!val || typeof val != "string"){
        return false;
    }
    var reg = /^[a-zA-Z0-9_\.]{1,32}$/;
    return reg.test(val.trim());
}
function uploadToTftp(){
    filename = $("#fileName").val();
    if(isBusy == 1){
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.isExportingTip);
        return;
    }
    if(!validateFileName(filename)){
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.inputCharaterNumber_32);
        return ;
    }
    isBusy = 1;
    window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.tip.uploadingToTftp, 'ext-mb-waiting');  
    $.ajax({
        url:'cmc/uploadConfigFile.tv?cmcId='+ cmcId + "&filename=" + filename,
        type:'POST',
        success:function(response){
            isBusy = 0;
            if(response == "entityBusy"){               
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.isBusy,'error');
            }else if(response == "fileTransTimeout"){
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.uploadFileError,'error');
            }else if(response == "snmpTimeout"){
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.snmpRequestTimeout,'error');
            }else if(response == "updateSuccess"){
                window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CMC.tip.downloadConfigToLocal, function(type) {
                    if (type == 'no') {
                        return;
                    }
                    downloadToPc();
                    cancelClick();
                });
            }
        },
        error:function(){
            isBusy = 0;
        },
        cache:false
    });
}
function downloadToPc(){
    window.parent.location.href="cmc/downloadConfigFile.tv?cmcId=" + cmcId+ "&filename=" + filename;
}
function cancelClick() {
    window.top.closeWindow('downloadConfig');
}
</script>
</HEAD>
<BODY class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt withoutBorderBottom" width="200">
						 <fmt:message bundle="${cmc}" key="CMC.label.saveName"/>:
	                 </td>
	                 <td class="withoutBorderBottom">
						<input class="normalInput" id="fileName" toolTip='<fmt:message bundle="${cmc}" key="CMC.tip.inputCharaterNumber_32" />'
                            name="cmcUpChannelBaseShowInfo.channelId" style="width: 200px; align: center" value="config" />
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		 <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id=saveBtn onclick="uploadToTftp()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoExport"></i><fmt:message bundle="${cmc}" key="CMC.label.exportConfig"/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
		     </ol>
		</div>
	</div>

	

   
</BODY>
</HTML>