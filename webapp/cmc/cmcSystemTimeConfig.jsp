<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
    href="/css/<%= cssStyleName %>/mytheme.css" />
    
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/disabledStyle.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String st = sdf.format(new Date());%>
var entityId = '${entityId}';
var config = ${currentSystemTimeConfig};
var systemTime;

Date.prototype.format = function(format)
{
     var o = {
         "M+" : this.getUTCMonth()+1, //month
         "d+" : this.getUTCDate(),    //day
         "h+" : this.getUTCHours(),   //hour
         "m+" : this.getUTCMinutes(), //minute
         "s+" : this.getUTCSeconds(), //second
         "q+" : Math.floor((this.getUTCMonth()+3)/3),  //quarter
         "S" : this.getMilliseconds() //millisecond
     }
     if(/(y+)/.test(format)) 
    	 format=format.replace(RegExp.$1,(this.getUTCFullYear()+"").substr(4 - RegExp.$1.length));
     for(var k in o)
    	 if(new RegExp("("+ k +")").test(format))
    	     format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
     return format;
}
function domainNameCheck(val){
    if(!val && val.trim().length < 0 ){
        return false;
    }
    var exp = /^([\w-]+\.)+((com)|(net)|(org)|(gov\.cn)|(info)|(cc)|(com\.cn)|(net\.cn)|(org\.cn)|(name)|(biz)|(tv)|(cn)|(mobi)|(name)|(sh)|(ac)|(io)|(tw)|(com\.tw)|(hk)|(com\.hk)|(ws)|(travel)|(us)|(tm)|(la)|(me\.uk)|(org\.uk)|(ltd\.uk)|(plc\.uk)|(in)|(eu)|(it)|(jp)|(co)|(me)|(mx)|(ca)|(ag)|(com\.co)|(net\.co)|(nom\.co)|(com\.ag)|(net\.ag)|(fr)|(org\.ag)|(am)|(asia)|(at)|(be)|(bz)|(com\.bz)|(net\.bz)|(net\.br)|(com\.br)|(de)|(es)|(com\.es)|(nom\.es)|(org\.es)|(fm)|(gs)|(co\.in)|(firm\.in)|(gen\.in)|(ind\.in)|(net\.in)|(org\.in)|(jobs)|(ms)|(com\.mx)|(nl)|(nu)|(co\.nz)|(net\.nz)|(org\.nz)|(tc)|(tk)|(org\.tw)|(idv\.tw)|(co\.uk)|(vg)|(ad)|(ae)|(af)|(ai)|(al)|(an)|(ao)|(aq)|(ar)|(as)|(au)|(aw)|(az)|(ba)|(bb)|(bd)|(bf)|(bg)|(bh)|(bi)|(bj)|(bm)|(bn)|(bo)|(br)|(bs)|(bt)|(bv)|(bw)|(by)|(cd)|(cf)|(cg)|(ch)|(ci)|(ck)|(cl)|(cm)|(cr)|(cu)|(cv)|(cx)|(cy)|(cz)|(dj)|(dk)|(dm)|(do)|(dz)|(ec)|(ee)|(eg)|(er)|(et)|(fi)|(fj)|(fk)|(fo)|(ga)|(gd)|(ge)|(gf)|(gg)|(gh)|(gi)|(gl)|(gm)|(gn)|(gp)|(gq)|(gr)|(gt)|(gu)|(gw)|(gy)|(hm)|(hn)|(hr)|(ht)|(hu)|(id)|(ie)|(il)|(im)|(iq)|(ir)|(is)|(je)|(jm)|(jo)|(ke)|(kg)|(kh)|(ki)|(km)|(kn)|(kr)|(kw)|(ky)|(kz)|(lb)|(lc)|(li)|(lk)|(lr)|(ls)|(lt)|(lu)|(lv)|(ly)|(ma)|(mc)|(md)|(mg)|(mh)|(mk)|(ml)|(mm)|(mn)|(mo)|(mp)|(mq)|(mr)|(mt)|(mu)|(mv)|(mw)|(my)|(mz)|(na)|(nc)|(ne)|(nf)|(ng)|(ni)|(no)|(np)|(nr)|(nz)|(om)|(pa)|(pe)|(pf)|(pg)|(ph)|(pk)|(pl)|(pm)|(pn)|(pr)|(ps)|(pt)|(pw)|(py)|(qa)|(re)|(ro)|(ru)|(rw)|(sa)|(sb)|(sc)|(sd)|(se)|(sg)|(si)|(sk)|(sl)|(sm)|(sn)|(sr)|(st)|(sv)|(sy)|(sz)|(td)|(tf)|(tg)|(th)|(tj)|(tl)|(tn)|(to)|(tr)|(tt)|(tz)|(ua)|(ug)|(uk)|(uy)|(uz)|(va)|(vc)|(ve)|(vi)|(vn)|(vu)|(wf)|(ye)|(yt)|(yu)|(za)|(zm)|(zw))$/;    
    return exp.test(val.trim()) && val.trim().length<=50;
}
function chechNtpServerValid(val){
	if(domainNameCheck(val) || checkIsNomalIp(val)){
		return true;
	}else{
		$("#ntpServer").focus();
		return false;
	}
}
function checkIntervalValid(interval){
	if(interval == "" || isNaN(interval) || interval < 1 || interval > 2595599){
        $("#synInterval").focus();
        return false;
    }
	return true;
}
function cancelClick(){
	window.parent.closeWindow('cmcSystemTimeConfig');
}
function saveClick(){
	var ntpServer = $("#ntpServer").val();
    var synInterval = $("#synInterval").val();
    var date = new Date($("#systemTime").val().replace("-", "/"));
    var systemTime = date.getTime()/1000 - date.getTimezoneOffset() * 60;
    var timeZone = $("#timeZone").val();
    if($(":radio[name='systemTimeType'][checked=true]").val() == "2"){
        synInterval = "0";
    }else{
    	if(!chechNtpServerValid(ntpServer) || !checkIntervalValid(synInterval)){
    		return;
    	}
    }
    var configStr = "systemTimeConfig.topCcmtsSysTimeSynInterval=" + synInterval+
    "&systemTimeConfig.topCcmtsNtpserverAddress=" + ntpServer +
    "&systemTimeConfig.topCcmtsSysTime=" + systemTime+
    "&systemTimeConfig.topCcmtsSysTimeZone=" + timeZone;
    
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
    	url: "/cmc/systemtime/modifySystemTimeConfig.tv?entityId=" + entityId,
    	type: "POST",
        data: configStr,
        success: function (response){
        	if(response.message == "success"){
        		//window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifySuccessTip);
        		window.top.closeWaitingDlg();
        		top.afterSaveOrDelete({
       				title: I18N.RECYLE.tip,
       				html: '<b class="orangeTxt">'+I18N.text.modifySuccessTip+'</b>'
       			});
                cancelClick(); 
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
function synTimeNow(){
    var ntpServer = $("#ntpServer").val();
    if(!chechNtpServerValid(ntpServer)){
    	return;
    }
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
        url: "/cmc/systemtime/modifySystemTimeConfig.tv?entityId=" + entityId + 
        		"&systemTimeConfig.topCcmtsNtpserverAddress=" + ntpServer,
        type: "POST",
        success: function (response){
            if(response.message == "success"){            	
            	systemTime = response.systemTimeConfig.topCcmtsSysTime * 1000 + response.systemTimeConfig.increaseTime;
            	var date = new Date(systemTime);
                $("#systemTime").val(date.format("yyyy-MM-dd hh:mm:ss"));
            	window.top.closeWaitingDlg(I18N.RECYLE.tip); 
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
function applyLocalTime(){
	var date = new Date();
	var time = parseInt(date.getTime()/1000 - date.getTimezoneOffset() * 60);
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
        url: "/cmc/systemtime/modifySystemTimeConfig.tv?entityId=" + entityId + 
                "&systemTimeConfig.topCcmtsSysTime=" + time,
        type: "POST",
        success: function (response){
            if(response.message == "success"){              
                systemTime = response.systemTimeConfig.topCcmtsSysTime * 1000 + response.systemTimeConfig.increaseTime;
                var date = new Date(systemTime);
                $("#systemTime").val(date.format("yyyy-MM-dd hh:mm:ss"));
                window.top.closeWaitingDlg(I18N.RECYLE.tip); 
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
//修改获取时间类型或更改输入框disabled属性
function typeChangeClick(value){
	if(value == "1"){
		$("#systemTime").attr("disabled", true);
		$("#applyLocalTime").attr("disabled", true);
		$("#synInterval").attr("disabled", false);
        $("#ntpServer").attr("disabled", false);
        $("#refreshSystemTime").attr("disabled", false);
	}else{
		$("#systemTime").attr("disabled", false);
        $("#applyLocalTime").attr("disabled", false);
        $("#synInterval").attr("disabled", true);
        $("#ntpServer").attr("disabled", true);
        $("#refreshSystemTime").attr("disabled", true);
	}
}
$(function (){
	if(typeof config != 'string'){
		systemTime = config.topCcmtsSysTime * 1000 + config.increaseTime;
		var date = new Date(systemTime);
		if(config.topCcmtsSysTimeSynInterval != 0 && config.topCcmtsNtpserverAddress.trim() != "0"){
            $(":radio[name='systemTimeType'][value='1']").attr("checked", true);
            typeChangeClick("1");
        }else{
            $(":radio[name='systemTimeType'][value='2']").attr("checked", true);
            typeChangeClick("2");
        }
		$("#ntpServer").val(config.topCcmtsNtpserverAddress);
	    $("#synInterval").val(config.topCcmtsSysTimeSynInterval);
	    $("#systemTime").val(date.format("yyyy-MM-dd hh:mm:ss"));
	    $("#timeZone").val(config.topCcmtsSysTimeZone);
	    $("#currentSystemTime").append(date.format("yyyy-MM-dd hh:mm:ss"));
	    $("#systemTime").keydown(function (event){
	    	if (!event) {
                event = window.event;
                event.cancelBubble = true;
            }else {
            	event.stopPropagation();
            }                
	    	return false;
	    });
	    $("#systemTime").bind("click", function (){
	    	$("#systemTime").focus();
	    });
	    window.top.cmcSystemTime = self.setInterval(function (){
	    	systemTime += 1000;
	    	var date = new Date(systemTime);
	    	$("#currentSystemTime").empty();
	    	$("#currentSystemTime").append(date.format("yyyy-MM-dd hh:mm:ss"));
	    }, 1000);
	    
	}
	
});
</script>
</head>
<body class="openWinBody">
<div class="edge10 pT20">
    <div class="zebraTableCaption">
        <div class="zebraTableCaptionTitle"><span>
	        <input type="radio" name="systemTimeType" value="1" 
	        onclick="typeChangeClick(this.value)" /><fmt:message bundle="${cmc}" key="SYSTEMTIME.autoGetTime"/>
        </span></div>
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" >
            <tbody>
                <tr>
                    <td class="rightBlueTxt" width=260>NTP:</td>
                    <td><input id="ntpServer" type="text" class="normalInput floatLeft" maxlength=50 toolTip='<fmt:message bundle="${cmc}" key="SYSTEMTIME.ipOrDomain"/>'/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a id="refreshSystemTime" onclick="synTimeNow()" href="javascript:;" class="nearInputBtn"><span><i class="miniIcoRefresh"></i><fmt:message bundle="${cmc}" key="SYSTEMTIME.refreshNow"/></span></a>
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="SYSTEMTIME.synInterval"/>:</td>
                    <td><input id="synInterval" type="text" class="normalInput"  maxlength=7 
                            toolTip='1-2595599'/><fmt:message bundle="${cmc}" key="SYSTEMTIME.seconds"/></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<div class="edge10 pT20">
    <div class="zebraTableCaption">
        <div class="zebraTableCaptionTitle"><span>
            <input type="radio" name="systemTimeType" value="2" 
            onclick="typeChangeClick(this.value)" /><fmt:message bundle="${cmc}" key="SYSTEMTIME.humanSetTime"/>
        </span></div>
        <table  class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" >
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width=260><fmt:message bundle="${cmc}" key="SYSTEMTIME.systemTime"/>:</td>
	                <td><input id="systemTime" type="text" class="normalInput floatLeft"
	                    onclick="WdatePicker({isShowClear: false, readOnly: true, dateFmt:'yyyy-MM-dd HH:mm:ss'})" />&nbsp;&nbsp;&nbsp;&nbsp;
	                    <a id=applyLocalTime onclick="applyLocalTime()" href="javascript:;" class="nearInputBtn"><span><i class="miniIcoEdit"></i><fmt:message bundle="${cmc}" key="SYSTEMTIME.applyLocalTime"/></span></a>
                    </td>
	            </tr>
	        </tbody>
	    </table>
    </div>    
</div>
<div class="edge10 pT20">
    <table  class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" >
        <tbody>
            <tr>
                <td class="rightBlueTxt" width=280><fmt:message bundle="${cmc}" key="SYSTEMTIME.timeZone"/>:</td>
                <td><select id="timeZone" type="text" style="width: 130px;">
                        <option value="-12">UTC-12</option>
                        <option value="-11">UTC-11</option>
                        <option value="-10">UTC-10</option>
                        <option value="-9">UTC-9</option>
                        <option value="-8">UTC-8</option>
                        <option value="-7">UTC-7</option>
                        <option value="-6">UTC-6</option>
                        <option value="-5">UTC-5</option>
                        <option value="-4">UTC-4</option>
                        <option value="-3">UTC-3</option>
                        <option value="-2">UTC-2</option>
                        <option value="-1">UTC-1</option>
                        <option value="0">UTC</option>
                        <option value="1">UTC+1</option>
                        <option value="2">UTC+2</option>
                        <option value="3">UTC+3</option>
                        <option value="4">UTC+4</option>
                        <option value="5">UTC+5</option>
                        <option value="6">UTC+6</option>
                        <option value="7">UTC+7</option>
                        <option value="8">UTC+8</option>
                        <option value="9">UTC+9</option>
                        <option value="10">UTC+10</option>
                        <option value="11">UTC+11</option>
                        <option value="12">UTC+12</option>                            
                    </select>
                </td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt"><fmt:message bundle="${cmc}" key="SYSTEMTIME.currentTime"/>:</td>
                <td><div id="currentSystemTime"></div></td>
            </tr>
        </tbody>
    </table>
</div>

<div class="noWidthCenterOuter clearBoth">
    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
        <li><a id=saveBt onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i><fmt:message bundle="${resources}" key="COMMON.save" /></span></a></li>
        <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="COMMON.cancel" /></span></a></li>
     </ol>
</div>
</body>