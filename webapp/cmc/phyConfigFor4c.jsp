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
    module cmc
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.epon.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var cmcId = '<s:property value="cmcId"/>';
var typeId = '<s:property value="typeId"/>'
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var phyConfig = ${cmcPhyConfigListJson};
var _MEDIA = ["", I18N.CMC.text.copper, I18N.CMC.text.fiber];
var _DUPLEXMODE = ["", I18N.cmcSni.fullDuplexMod, I18N.cmcSni.halfDuplexMod];
function doOnload(){
	if(EntityType.isCC8800C_B(typeId)){
    	$("#phy2").css('display', 'none');
    }
    for(var i = 0 ; i < phyConfig.length ; i++){
        if(phyConfig[i].phyIndex == 1){
            $("#topCcmtsSniPhy1Int").val(phyConfig[i].topCcmtsSniInt);
            if(phyConfig[i].topCcmtsSniCurrentMedia && phyConfig[i].topCcmtsSniDuplexMod){
                $("#topCcmtsSniPhy1Stat").text(_MEDIA[phyConfig[i].topCcmtsSniCurrentMedia] + _DUPLEXMODE[phyConfig[i].topCcmtsSniDuplexMod]);
            }           
        }else if(phyConfig[i].phyIndex == 2){
            $("#topCcmtsSniPhy2Int").val(phyConfig[i].topCcmtsSniInt);
            if(phyConfig[i].topCcmtsSniCurrentMedia && phyConfig[i].topCcmtsSniDuplexMod){
                $("#topCcmtsSniPhy2Stat").text(_MEDIA[phyConfig[i].topCcmtsSniCurrentMedia] + _DUPLEXMODE[phyConfig[i].topCcmtsSniDuplexMod]);
            }
        }
    } 
    
}
function saveClick() {
    var sniPhy1Config = $("#topCcmtsSniPhy1Int").val();
    var sniPhy2Config = $("#topCcmtsSniPhy2Int").val();
    if(EntityType.isCC8800C_B(typeId)){
    	sniPhy2Config = '';
    }
    window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.GP.configing,'waitingMsg','ext-mb-waiting');
    $.ajax({
        url:'/cmc/sni/modifyCmcSniPhyConfig.tv?cmcId=' + cmcId + '&sniPhy1Config=' + sniPhy1Config + "&sniPhy2Config=" + sniPhy2Config,
        type:'POST',
        dateType:'text',
        success:function(response) {
            if (response == "success") {       
            	window.parent.closeWaitingDlg();
                // window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setSniPhySuccess);
            	top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">' + I18N.cmcSni.setSniPhySuccess + '</b>'
       			});
            } else {
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setSniPhyFail);
            	/* top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">' + I18N.cmcSni.setSniPhyFail + '</b>'
       			}); */
            }
            cancelClick();
        },
        error:function() {
        	/* top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">' + I18N.cmcSni.setSniPhyFail + '</b>'
   			}); */
            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setSniPhyFail);
        },
        cache:false
    });
}

function cancelClick() {
    window.parent.closeWindow('cmcPhyConfigFor4c'); 
}
function refreshClick(){
    window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.refreshData,'waitingMsg','ext-mb-waiting');
    $.ajax({
        url:'/cmc/sni/refreshPhyConfigFor4c.tv?cmcId=' + cmcId,
        type:'POST',
        dateType:'text',
        success:function(response) {
            if (response.message == "success") {
                phyConfig = response.data;
                doOnload();
                //window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.text.refreshSuccessTip);
                window.parent.closeWaitingDlg();
                top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">' + I18N.text.refreshSuccessTip + '</b>'
       			});
            } else {
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.text.refreshFailureTip );
            }
        },
        error:function() {
            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.text.refreshFailureTip );
        },
        cache:false
    });
}
function authLoad(){
    if(!operationDevicePower){
        $("#topCcmtsSniPhy1Int").attr("disabled",true);
        $("#topCcmtsSniPhy2Int").attr("disabled",true);
        $("#saveBt").attr("disabled",true);
    }
    
    if(!refreshDevicePower){
        $("#refreshBt").attr("disabled",true);
    }
}
</script>
</head>
<body class="openWinBody" onload="doOnload();authLoad()">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div class="edgeTB10LR20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="200">
	                    @cmcSni.phy1Select@
	                </td>
	                <td width="160">
	                    <select id="topCcmtsSniPhy1Int" style="width: 135px" class="normalSel">
						<option value="1">@cmcSni.phySelect1@</option>
						<option value="2">@cmcSni.phySelect2@</option>
						<option value="3">@cmcSni.phySelect3@</option>
						<option value="4">@cmcSni.phySelect4@</option>
						<option value="5">@cmcSni.phySelect5@</option>
						</select>
	                </td>
	                <td width="30">@CCMTS.entityStatus@:</td>
	                <td id="topCcmtsSniPhy1Stat"></td>
	            </tr>
	            <tr class="darkZebraTr" id="phy2">
	                <td class="rightBlueTxt">
	                    @cmcSni.phy2Select@
	                </td>
	                <td>
	                   <select id="topCcmtsSniPhy2Int" style="width: 135px"  class="normalSel">
						<option value="1">@cmcSni.phySelect1@</option>
						<option value="2">@cmcSni.phySelect2@</option>
						<option value="3">@cmcSni.phySelect3@</option>
						<option value="4">@cmcSni.phySelect4@</option>
						<option value="5">@cmcSni.phySelect5@</option>
					   </select>
	                </td>
	                <td>@CCMTS.entityStatus@:</td>
	                <td id="topCcmtsSniPhy2Stat"></td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id="refreshBt" onclick="refreshClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@BUTTON.fetch@</span></a></li>
		         <li><a id="saveBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML> 