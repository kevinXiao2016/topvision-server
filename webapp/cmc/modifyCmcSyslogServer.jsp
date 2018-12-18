<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title>Cmc vlan</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<%= request.getParameter("entityId")%>';
var index = '<%= request.getParameter("index")%>';
var ip = '<%= request.getParameter("ip")%>';
var existedIps = '<%= request.getParameter("existedIps")%>';
var existedIpArray = existedIps.split(',');
function cancelClick(){
	window.top.closeWindow('modifySyslogServer');
}

function trim(s) {
	s = s.replace(/^\s+/, "");
	return s.replace(/\s+$/, "");
}
//验证输入的IP地址是否合法，不能为广播地址，多播地址或全0
function validate() {
	var reg;
	var ip = trim(getIpValue("syslogIp"));

	//验证IP地址格式是否合法
	reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if (!reg.test(ip)) {
		window.parent.showMessageDlg(I18N.CMC.title.tip,
				I18N.syslog.ipError1);
		return false;
	}
	//验证是否为全0
	if (ip == "0.0.0.0") {
		window.parent.showMessageDlg(I18N.CMC.title.tip,
				I18N.syslog.ipError2);
		return false;
	}
	//验证是否为多播,多播地址范围为224.0.0.0～239.255.255.255
	reg = /(\d{1,3})/;
	var ipPart1 = reg.exec(ip)[0];
	if (224 <= ipPart1 && ipPart1 < 240) {
		window.parent.showMessageDlg(I18N.CMC.title.tip,
				I18N.syslog.ipError3);
		return false;
	}
	//验证是否为广播，广播地址的限制规则为：
    //必须为A,B,C类地址中的一个
    //屏蔽如下IP段： 191.255.0.0       192.0.0.0-192.0.0.255       223.255.255.0 
    var ip1 = ip.split(".")[0];
    var ip2 = ip.split(".")[1];
    var ip3 = ip.split(".")[2];
    if((1<=ip1 && ip1<=126) || (128<=ip1 && ip1<=223)){
        if((ip=="191.255.0.0") || ((ip1==192)&&(ip2==0)&&(ip3==0)) || (ip=="223.255.255.0") ){
            return window.parent.showMessageDlg(I18N.CMC.title.tip,
                    I18N.syslog.ipError4, "tip", function() {
                    });
        }
    }else{
        return window.parent.showMessageDlg(I18N.CMC.title.tip,
                I18N.syslog.ipError5, "tip", function() {
                });
    }
	return true;
}
function okClick(){
	if (!validate()) {return;}
	var syslogIp = getIpValue("syslogIp");
	if(!ipIsFilled("syslogIp")){
	    window.parent.showMessageDlg("@CMC.title.tip@",
	            "@syslog.ipError1@");
	    return ;
	}
	if(!checkIsNomalIp(syslogIp)){
        window.parent.showMessageDlg("@CMC.title.tip@",
                "@syslog.ipError5@");
        return ;
    }
	if(syslogIp === ip){
		window.parent.showMessageDlg("@CMC.title.tip@", "@text.modifySuccessTip@");
        cancelClick();
        return
	}
	if($.inArray(syslogIp, existedIpArray) !== -1){
		window.parent.showMessageDlg("@CMC.title.tip@", I18N.syslog.serverIpExisted);
		return ;
	}
	window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.text.beingModifiedConfig@");
    $.ajax({
        url : '/cmc/modifySyslogServer.tv',
        type : 'POST',
        data : {
        	entityId : entityId,
            topCcmtsSyslogServerIndex : index,
            topCcmtsSyslogServerIpAddr : syslogIp
        },
        success : function() {
            //window.parent.showMessageDlg("@CMC.title.tip@", "@text.modifySuccessTip@");
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@text.modifySuccessTip@</b>'
   			});
            cancelClick();
        },
        error : function() {
            window.parent.showMessageDlg("@CMC.title.tip@", "@text.modifyFailureTip@");
            cancelClick();
        },
        cache : false
    });
}
function changeButton(){
	var syslogIp = getIpValue("syslogIp");
	if(checkIsNomalIp(syslogIp) && syslogIp != ip){
		$("#okBt").attr("disabled", false);
	}else{
		$("#okBt").attr("disabled", true);
	}
}
$(function (){
	var syslogIp = new ipV4Input("syslogIp","syslogIp-span");
	syslogIp.width(200);
	setIpValue("syslogIp", ip);
});
</script>
</head>
<body class="openWinBody" >
    <div class="openWinHeader">
        <div class="openWinTip"></div>
        <div class="rightCirIco equipmentCirIco"></div>
    </div>
    <div class="edgeTB10LR20 pT60">
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr>
                    <td class="rightBlueTxt w200">
                        @syslog.ipAddress_maohao@
                    </td>
                    <td>
                        <span id="syslogIp-span"></span>
                    </td>
                </tr>                
            </tbody>
        </table>
        <div class="noWidthCenterOuter clearBoth">
             <ol class="upChannelListOl pB0 pT80 noWidthCenter">
                 <li><a id=saveBt onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
                 <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
             </ol>
        </div>
    </div>
</body>
</Zeta:HTML>