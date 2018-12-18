<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    css css.reset
    library Jquery
    library ext
    library zeta
    module epon
</Zeta:Loader>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<style type="text/css">
.clickable{
    cursor: pointer;
}
</style>
<script type="text/javascript">
var ON_IMG_SRC = "/images/performance/on.png";
var OFF_IMG_SRC = "/images/performance/off.png";
var ON = 1;
var OFF = 0;

var entityId = '${entityId}';
var onuGlobalCfg = ${onuGlobalCfg};

$(function(){
	//TODO 初始化开关状态
	/* if(onuGlobalCfg.wanSwitch != 1){
		document.getElementById('wanSwitch').src = OFF_IMG_SRC;
	}
	if(onuGlobalCfg.wlanSwitch != 1){
        document.getElementById('wlanSwitch').src = OFF_IMG_SRC;
    }
	if(onuGlobalCfg.catvSwitch != 1){
        document.getElementById('catvSwitch').src = OFF_IMG_SRC;
    } */
	
	/* $('.switchImg').on('click', function(e){
		var target = e.target;
		if(target.src.indexOf(ON_IMG_SRC)!=-1){
			target.src = OFF_IMG_SRC;
		}else{
			target.src = ON_IMG_SRC;
		}
	}) */
	
	if(onuGlobalCfg.wanSwitch == 1){
    	$('#wanOn').attr("checked", true)
    }else{
    	$('#wanOff').attr("checked", true)
    }
    if(onuGlobalCfg.wlanSwitch == 1){
    	$('#wlanOn').attr("checked", true)
    }else{
    	$('#wlanOff').attr("checked", true)
    }
    if(onuGlobalCfg.catvSwitch == 1){
    	$('#catvOn').attr("checked", true)
    }else{
    	$('#catvOff').attr("checked", true)
    }
})

function saveClick(){
	/* var wanSwitch = document.getElementById('wanSwitch').src.indexOf(ON_IMG_SRC) != -1 ? ON : OFF,
	    wlanSwitch = document.getElementById('wlanSwitch').src.indexOf(ON_IMG_SRC) != -1 ? ON : OFF,
	    catvSwitch = document.getElementById('catvSwitch').src.indexOf(ON_IMG_SRC) != -1 ? ON : OFF; */
	var wanSwitch =  $("input[name='wan']:checked").val() == 1?ON : OFF,
		wlanSwitch = $("input[name='wlan']:checked").val() == 1?ON : OFF,
		catvSwitch = $("input[name='catv']:checked").val() == 1?ON : OFF;
	//TODO 保存开关状态
	window.top.showWaitingDlg('@COMMON.wait@', '@EPON.modifyOnuGlobalCfging@', 'ext-mb-waiting');
	$.ajax({
        url : "/epon/modifyOnuGlobalCfg.tv",
        cache: false,
        data: {
        	wanSwitch: wanSwitch,
        	wlanSwitch: wlanSwitch,
        	catvSwitch: catvSwitch,
        	entityId: entityId
        },
        dataType:'json',
        success : function(json) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '@EPON.modifyOnuGlobalCfgOk@'
            });
            cancleClick();
        },
        error : function(json) {
            window.top.closeWaitingDlg();
            window.top.showMessageDlg('@COMMON.tip@', '@EPON.modifyOnuGlobalCfgErr@' + json.message);
        }
    }); 
}

function cancleClick(){
	window.top.closeWindow("onuGlobalConfig");
}
</script>

</head>
<body class="openWinBody" >
    <div class="openWinHeader">
        <div class="openWinTip">@ONU/ONU.globalconfigTip@</div>
        <div class="rightCirIco earthCirIco"></div>
    </div>
    <div class="edge10" style="margin-top: 20px;">
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr>
                    <!-- <td class="rightBlueTxt" width="250"><label>WAN@COMMON.maohao@</label></td>
                    <td><img id="wanSwitch" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle /></td> -->
                    <td class="rightBlueTxt" width="200">
	                    <label for="wan">WAN@COMMON.maohao@</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="wan" checked="checked" id="wanOff" value="0" />@ONU/ONU.onlylocalmgmt@
						<input type="radio" name="wan" id="wanOn" value="1" />@ONU/ONU.remotemgmt@
	                </td>
                </tr>
                <tr class="darkZebraTr">
                    <!-- <td class="rightBlueTxt"><label>WLAN@COMMON.maohao@</label></td>
                    <td><img id="wlanSwitch" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle /></td> -->
                    <td class="rightBlueTxt" width="200">
	                    <label for="wlan">WLAN@COMMON.maohao@</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="wlan" checked="checked" id="wlanOff" value="0" />@ONU/ONU.onlylocalmgmt@
						<input type="radio" name="wlan" id="wlanOn" value="1" />@ONU/ONU.remotemgmt@
	                </td>
                </tr>
                <tr>
                    <!-- <td class="rightBlueTxt"><label>CATV@COMMON.maohao@</label></td>
                    <td><img id="catvSwitch" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle /></td> -->
                    <td class="rightBlueTxt" width="200">
	                    <label for="catv">CATV@COMMON.maohao@</label>
	                </td>
	                <td colspan="2">
						<input type="radio" name="catv" checked="checked" id="catvOff" value="0" />@ONU/ONU.onlylocalmgmt@
						<input type="radio" name="catv" id="catvOn" value="1" />@ONU/ONU.remotemgmt@
	                </td>
                </tr>
            </tbody>
        </table>
        <div class="noWidthCenterOuter clearBoth">
            <ol class="upChannelListOl pB0 pT50 noWidthCenter">
                <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoSave"></i>@COMMON.confirm@</span></a></li>
                <li><a href="javascript:;" class="normalBtnBig" onclick="cancleClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
            </ol>
        </div>
    </div>
</body>
</Zeta:HTML>