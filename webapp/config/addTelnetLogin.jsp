<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    import js.tools.ipText
    module network
</Zeta:Loader>
<script type="text/javascript">
var ON_IMG_SRC = "/images/performance/on.png";
var OFF_IMG_SRC = "/images/performance/off.png";
var ON = 1;
var OFF = 0;
function closeClick() {
	window.parent.closeWindow('addEntityPassword');
}

function save(){
	var ip = getIpValue("ip");
	if (!checkedIpValue(ip)) {
		window.top.showMessageDlg("@COMMON.tip@", "@RESOURCES/WorkBench.ipIsInvalid@");
		return;
    }
	
	var userName = $("#userName").val();
	if(userName == ''){
		$("#userName").focus();
		return;
	}
	var password = $("#password").val();
	if(password == ''){
		$("#password").focus();
		return;
	}
	var enablePassword = $("#enablePassword").val();
	if(enablePassword == ''){
		$("#enablePassword").focus();
		return;
	}
    var isAAA = document.getElementById('isAAA').src.indexOf(ON_IMG_SRC) != -1;

	$.ajax({
		url: "/entity/telnetLogin/addTelnetLogin.tv",
		type: "post",
		dataType: "json",
        data:{
            userName:userName,
            ipString:ip,
            password:password,
            enablePassword:enablePassword,
            isAAA:isAAA
        },
		success: function(response){
			if(response.isExist){
				window.top.showMessageDlg("@COMMON.tip@", "@sendConfig.ipExist@");
				return;
			}
			window.top.getActiveFrame().onRefreshClick();
			closeClick()
		}
 	})
}

$(function(){
	var newIp = new ipV4Input("ip","span1");
	newIp.width(200);
    document.getElementById('isAAA').src = OFF_IMG_SRC;
    $('.switchImg').on('click', function(e){
        var target = e.target;
        if(target.src.indexOf(ON_IMG_SRC)!=-1){
            target.src = OFF_IMG_SRC;
        }else{
            target.src = ON_IMG_SRC;
        }
    })
})
</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@resources/telnet.addEntityConfig@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT30">
	    <table class="zebraTableRows">
	            <tr>
	                <td class="rightBlueTxt">IP:<font color=red>*</font></td>
	                <td>
	                	<span id="span1"></span>
	                </td>
	            </tr>	 
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">@resources/SYSTEM.operName@:<font color=red>*</font></td>
	                <td>
	                    <input class="normalInput modifiedFlag w200" id="userName" maxlength='64' tooltip='@sendConfig.userNameTip@'/>
	                </td>
	            </tr>	
	            
	            <tr>
	                <td class="rightBlueTxt">@resources/SYSTEM.passWord@<font color=red>*</font></td>
	                <td>
	                	<Zeta:Password id="password" maxlength='64' width="202px" tooltip='@sendConfig.passwordTip@'/>
	                </td>
	            </tr>	
	            
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">Enable @resources/SYSTEM.passWord@<font color=red>*</font></td>
	                <td>
	                	<Zeta:Password width="202px" id="enablePassword" maxlength='64' tooltip='@sendConfig.enablePasswordTip@'/>
	                </td>
	            </tr>
                <tr>
                    <td class="rightBlueTxt" width="176"><label>@resources/SYSTEM.isAAAMaohao@</label></td>
                    <td><img id="isAAA" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle /></td>
                </tr>
	    </table>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id="saveButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>