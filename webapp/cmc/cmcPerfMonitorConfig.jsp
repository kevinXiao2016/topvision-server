<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var entityId = '<s:property value="entityId"/>';
var perfCollect = '<s:property value="perfCollect"/>';
var snrPeriod='${snrPeriod}'/60/1000;
var speedPeriod='${speedPeriod}'/60/1000;
var usBitErrorRatePeriod='${usBitErrorRatePeriod}'/60/1000;

/**
 * 是否修改了值
 */
function isChange(){
    return  perfCollect!=$("#perfCollect").val()
                ||snrPeriod!=$("#perfNoisePeriod").val()
                ||speedPeriod!=$('#perfSpeedPeriod').val()
                ||usBitErrorRatePeriod!=$('#perfBitErrorPeriod').val();
}

/**
 * 设置“采集周期”是否可修改
 */
function disabledPeriod(b){
    $("#perfNoisePeriod,#perfSpeedPeriod,#perfBitErrorPeriod").attr("disabled",b);
}

function cancelClick() {
	window.top.closeWindow('perfConfig');
}

function saveClick(){
    var _perfCollect=$('#perfCollect').val();
    var _perfNoisePeriod=$('#perfNoisePeriod').val();
    var _perfBitErrorPeriod=$('#perfBitErrorPeriod').val();
    var _perfSpeedPeriod=$('#perfSpeedPeriod').val();
    
    if(!isValidPeriod(_perfNoisePeriod)){
        $('#perfNoisePeriod').focus();
        return;
    }else if(!isValidPeriod(_perfBitErrorPeriod)){
        $('#perfBitErrorPeriod').focus();
        return;
    }else if(!isValidPeriod(_perfSpeedPeriod)){
        $('#perfSpeedPeriod').focus();
        return;
    }
    
    var _param={};
    
    _param.cmcId=cmcId;
    _param.perfCollect=_perfCollect;
    if(_perfCollect=='true'){
        _param.snrPeriod=_perfNoisePeriod*60*1000;
        _param.speedPeriod=_perfSpeedPeriod*60*1000;
        _param.usBitErrorRatePeriod=_perfBitErrorPeriod*60*1000;
    }
    $.ajax({
        url:'/cmcperf/ccmtsPerfConf.tv',
        type:'post',
        dataType:'json',
        data:_param,
        cache:false,
        success:function(response) {
            if (response.message == "success") {
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.perfConfig.info.saveSuccess);
            } else if (response.message == "cmcnotexits") {
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.perfConfig.info.cmcnotexits);
            } else {
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.perfConfig.info.saveError);
            }
            cancelClick();
        }
    });
}

function isValidPeriod(number){
    var reg = /^[1-9]\d*$/;
    return reg.test(number)&&number>=5&&number<=1440;
}

$(function(){
    $("#perfNoisePeriod").val(snrPeriod);
    $('#perfBitErrorPeriod').val(usBitErrorRatePeriod);
    $('#perfSpeedPeriod').val(speedPeriod);
    
    $("#perfCollect,#perfNoisePeriod,#perfBitErrorPeriod,#perfSpeedPeriod").change(function(){
        if(isChange()){
            $("#saveBtn").attr("disabled",false);
        }else{
            $("#saveBtn").attr("disabled",true);
        }
        
        if(this.id=='perfCollect'){
            disabledPeriod(this.value=='false');
        }
    });
    
    if(perfCollect=='false'){
        disabledPeriod(true);
    }
    
    //操作设备权限-------------------------------------
    var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
    $("#perfCollect").addClass("operationDeviceClass");
    $("#saveBtn").addClass("operationDeviceClass");
    $('#perfNoisePeriod').addClass("operationDeviceClass");
    $('#perfBitErrorPeriod').addClass("operationDeviceClass");
    $('#perfSpeedPeriod').addClass("operationDeviceClass");
    if(!operationDevicePower){
        $(".operationDeviceClass").attr("disabled",true);
    }
    //--------------------------------------------------
});
</script>
</head>
<body class="openWinBody" >
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="300">
	                    <fmt:message bundle="${cmcRes}" key="CCMTS.perfCollect"/>:
	                </td>
	                <td>
	                    <select id="perfCollect" style="width: 140px;"name="perfCollect" class="normalSel">
    							<option value="true"
    								<s:if test="perfCollect==true">selected</s:if>><fmt:message bundle="${emsRes}" key="COMMON.on"/></option>
    							<option value="false"
    								<s:if test="perfCollect==false">selected</s:if>><fmt:message bundle="${emsRes}" key="COMMON.off"/></option>
    					</select>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <fmt:message bundle="${cmcRes}" key="snrPeriod"/>:
	                </td>
	                <td>
	                   <input class="normalInput" id="perfNoisePeriod"  name="perfNoisePeriod" toolTip='5-1440'
                            style="width: 140px; align: left" value="" /> <fmt:message bundle="${networkRes}" key="label.minutes"/>
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt" >
	                    <fmt:message bundle="${cmcRes}" key="errorBitPeriod"/>:
	                </td>
	                <td>
	                    <input class="normalInput" id="perfBitErrorPeriod" toolTip='5-1440'
                        name="perfBitErrorPeriod"                      
                        style="width: 140px; align: left" value="" /> <fmt:message bundle="${networkRes}" key="label.minutes"/>
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    <fmt:message bundle="${cmcRes}" key="speedPeriod"/>:
	                </td>
	                <td>
	                   <input class="normalInput" id="perfSpeedPeriod" toolTip='5-1440'
                        name="perfSpeedPeriod"
                        style="width: 140px; align: left" value="" /> <fmt:message bundle="${networkRes}" key="label.minutes"/>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a onclick="saveClick()" id=saveBtn href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${emsRes}" key="COMMON.save"/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${emsRes}" key="COMMON.close"/></span></a></li>
		     </ol>
		</div>
	</div>
</body>
</html>
