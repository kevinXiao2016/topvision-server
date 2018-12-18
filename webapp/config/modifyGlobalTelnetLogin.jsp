<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module network
</Zeta:Loader>
<script type="text/javascript">
var ON_IMG_SRC = "/images/performance/on.png";
var OFF_IMG_SRC = "/images/performance/off.png";
var ON = 1;
var OFF = 0;
function closeClick() {
	window.parent.closeWindow('modifyGlobalPassword');
}

function save(){
	var userName = $("#userName").val();
	var password = $("#password").val();
    var enablePassword = $("#enablePassword").val();
    var isAAA = document.getElementById('isAAA').src.indexOf(ON_IMG_SRC) != -1;
	
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

	$.ajax({
		url: "/entity/telnetLogin/modifyGlobalTelnetLogin.tv",
		type: "post",
		dataType: "json",
        data:{
            userName:userName,
            password:password,
            enablePassword:enablePassword,
            isAAA:isAAA
        },
		success: function(){
			window.top.getActiveFrame().onRefreshClick();
			window.top.getActiveFrame().setUsername(userName);
			closeClick()
		}
 	}) 
}

$(function(){
    $.ajax({
        url: "/entity/telnetLogin/getGlobalTelnetLogin.tv",
        type: "post",
        dataType: "json",
        success: function(response){
            userName = response.userName;
            password = response.password;
            enablePassword = response.enablePassword;
            isAAA = response.isAAA;

            $("#userName").val(userName);
            $("#password").val(password);
            $("#enablePassword").val(enablePassword);
            if(!isAAA){
                document.getElementById('isAAA').src = OFF_IMG_SRC;
            }
            $('.switchImg').on('click', function(e){
                var target = e.target;
                if(target.src.indexOf(ON_IMG_SRC)!=-1){
                    target.src = OFF_IMG_SRC;
                }else{
                    target.src = ON_IMG_SRC;
                }
            })
        }
    })
})
</script>
</head>
<body class="openWinBody">
<div class="formtip" id="tips" style="display: none"></div>
	<div class="openWinHeader">
	    <div class="openWinTip">@resources/telnet.modifyDefaultEntityConfig@</div>
	    <div class="rightCirIco pageCirIco"></div> 
	</div>
	<div class="edgeTB10LR20 pT30">
	    <table class="zebraTableRows">
	            <tr>
	                <td class="rightBlueTxt">@resources/SYSTEM.operName@:<font color=red>*</font></td>
	                <td>
	                    <input class="normalInput modifiedFlag w200" id="userName" maxlength='64' tooltip='@sendConfig.userNameTip@'/>
	                </td>
	            </tr>	
	            
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">@resources/SYSTEM.passWord@<font color=red>*</font></td>
	                <td>
	                	<Zeta:Password width="202px" id="password" maxlength='64' tooltip='@sendConfig.passwordTip@'/>
	                </td>
	            </tr>

            <tr>
                <td class="rightBlueTxt">Enable@resources/SYSTEM.passWord@<font color=red>*</font></td>
                <td>
                    <Zeta:Password width="202px" id="enablePassword" maxlength='64' tooltip='@sendConfig.enablePasswordTip@'/>
                </td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt" width="176"><label>@resources/SYSTEM.isAAAMaohao@</label></td>
                <td><img id="isAAA" class="clickable switchImg" src="/images/performance/on.png" border=0 align=absmiddle /></td>
            </tr>
        </table>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="save()" id=saveButton href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.edit@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>