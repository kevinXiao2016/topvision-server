<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = '${cmcId}';
var setTime='${setTime}';

Ext.onReady(function(){
    initData();
})
function initData(){
    var day=parseInt(setTime/(24*60));
    var hour=parseInt((setTime-day*24*60)/60);
    var min=parseInt((setTime-day*24*60)%60);
    $("#clearTimeDay").val(day);
    $("#clearTimeHour").val(hour);
    $("#clearTimeMin").val(min);
}

function checkTimeFomateDay(day){
//  var t=/^([1-9]\d|[1-9]\d{2,3}|[1-3][0-9]\d{3}|4[0-2][0-9]\d{2}|43[0-1][0-9]\d|43200|0)$/;
    var dayCheck=/^(\d{1,2}|[1-2]\d{2}|3[0-5]\d|36[0-5])$/;
//    var dayCheck=/^(\d|[1-2]\d|30)$/;
    if(dayCheck.test(day)){
        return true;
    }else{
        return false;
    }

}
function checkTimeFomateHour(hour){
    var hourCheck=/^(\d|1\d|2[0-3])$/;
    if(hourCheck.test(hour)){
        return true;
    }else{
        return false;
    }
}
function checkTimeFomateMin(min){
    var minCheck=/^(\d|[1-5]\d)$/;
    if(minCheck.test(min)){
        return true;
    }else{
        return false;
    }
}

function refreshInputState(selector, state){
    if(state==true){
        $(selector).removeClass('normalInputRed');
    }else{
        $(selector).addClass('normalInputRed');
    }
}

function saveTimeConfig(){
    var day=$('#clearTimeDay').val();
    var hour=$('#clearTimeHour').val();
    var min=$('#clearTimeMin').val();
    refreshInputState('#clearTimeMin',true);
    refreshInputState('#clearTimeHour',true);
    refreshInputState('#clearTimeDay',true);
    if(!checkTimeFomateDay(day)){
        $('#clearTimeDay').focus();
        return;
    }
    if(!checkTimeFomateHour(hour)){
        $('#clearTimeHour').focus();
        return;
    }
    if(!checkTimeFomateMin(min)){
        $('#clearTimeMin').focus();
        return;
    }
    if(day==365){
        if(hour!=0){
            refreshInputState('#clearTimeHour',false);
            return;
        }else{
            refreshInputState('#clearTimeHour',true);
        }
        if(min!=0){
            refreshInputState('#clearTimeMin',false);
            return;
        }else{
            refreshInputState('#clearTimeMin',true);
        }
    }
    if(day==0&&hour==0){
        if(min<10&&min>0){
            refreshInputState('#clearTimeMin',false);
            return;
        }else{
            refreshInputState('#clearTimeMin',true);
        }
    }
    var time=day*24*60+hour*60+min*1;
    $.ajax({
        url:'/cmc/timeConfigSet.tv',
        data:{
            'cmcId':cmcId,
            'time':time
        },
        type:'post',
        success:function(data){
            if(data=="success"){
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '<b class="orangeTxt">@resources/COMMON.clearTimeSuccess@</b>'
                });
            }else{
                window.parent.showMessageDlg('@COMMON.tip@', '@resources/COMMON.clearTimeFail@');
            }   
            closeClick();
        },
        error:function(e){
            window.parent.showMessageDlg('@COMMON.tip@', '@resources/COMMON.clearTimeFail@');
        }
    })
}

function closeClick() {
    window.parent.closeWindow('modalDlg');
}
</script>
</head>
<body class=openWinBody>
    <div class=formtip id=tips style="display: none"></div>
    <div class="openWinHeader">
        <div class="openWinTip">@resources/COMMON.clearOfflineCm@</div>
        <div class="rightCirIco wheelCirIco"></div>
    </div>
    
    <div class="edge10">
            <table class="mCenter zebraTableRows" >
            <tr>
                <td class="rightBlueTxt w200">@RESOURCES/COMMON.clearOfflineCmTime@:</td>
                <td ><input class="normalInput w60" id="clearTimeDay" maxlength="10" tooltip="@RESOURCES/COMMON.clearOfflineCmTimeTooltipDay@"/><span> @RESOURCES/COMMON.clearUnitDay@</span>
                <input class="normalInput w60" id="clearTimeHour" maxlength="10" tooltip="@RESOURCES/COMMON.clearOfflineCmTimeTooltipHour@"/><span> @RESOURCES/COMMON.clearUnitHour@</span>
                <input class="normalInput w60" id="clearTimeMin" maxlength="10" tooltip="@RESOURCES/COMMON.clearOfflineCmTimeTooltipMinute@"/><span> @RESOURCES/COMMON.clearUnitMinute@</span>
                </td>                
            </tr>
            <tr class="darkZebraTr">
                <td colspan=3><div style="width:550px; overflow:hidden;margin：auto;padding-bottom:10px; color:#555;">@RESOURCES/COMMON.setClearTimeRule@</div></td>
            </tr>
        </table>
        </div>
    <Zeta:ButtonGroup>
        <Zeta:Button id="saveBt" onClick="saveTimeConfig()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
        <Zeta:Button onClick="closeClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
    </Zeta:ButtonGroup>
</body>
</Zeta:HTML>