<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
            module gpon
            IMPORT js/jquery/nm3kPassword
        </Zeta:Loader>
        <script type="text/javascript">
            var topSIPPstnUserPotsIdx = "${topSIPPstnUser.topSIPPstnUserPotsIdx}";
            var topSIPPstnUserForwardType = "${topSIPPstnUser.topSIPPstnUserForwardType}";
            var topSIPPstnUserPwd = "${topSIPPstnUser.topSIPPstnUserPwd}";
            var onuId = "${topSIPPstnUser.onuId}";
            var entityId = "${topSIPPstnUser.entityId}";
            
            var pass1 = new nm3kPassword({
                id : "pass1",
                renderTo : "password",
                toolTip : "@GPON.tip1To24@",
                width : 138,
                value : "",
                firstShowPassword : true,
                disabled : false,
                maxlength : 24
            })
            
            $(function(){
            	pass1.init();
            	pass1.setValue(topSIPPstnUserPwd);
            	$("#callTransferType").val(topSIPPstnUserForwardType);
            	if(topSIPPstnUserForwardType == 2){
            		$("#callForwardTimeBlock").css('display' ,'');
            	}else{
            		$("#callForwardTimeBlock").css('display' ,'none');
            	}
		    });
            
            //保存;
            function saveClick() {
                var telNo = $("#telNo").val();
                var username = $("#username").val();
                //var password = $("#password").val();
                var password = pass1.getValue();
                var servicesDataTemplate = $("#servicesDataTemplate").val();
                var digitalChartTemplate = $("#digitalChartTemplate").val();
                var callTransferType = $("#callTransferType").val();
                var transferNum = $("#transferNum").val();
                var forwardTime = $("#callForwardTime").val();
                
                if(!telNoCheck(telNo)) {
                	$("#telNo").focus();
                	return ;
                }
                if(!usernameCheck(username)) {
                	$("#username").focus();
                	return ;
                }
                if(!passwordCheck(password)) {
                	$("#pass1").focus();
                	return ;
                }
                if(!transferNumberCheck(transferNum)) {
            	    $("#transferNum").focus();
                    return ;
                }
                if(callTransferType == 2) {
                	if(!forwardTimeCheck(forwardTime)) {
                        $("#callForwardTime").focus();
                        return;
                    }
                }else{
                	forwardTime = "";
                }
                window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
                $.ajax({
                    url: '/gpon/onuvoip/modifyGponOnuPotsConfig.tv',
                    type: 'POST',
                    data: {
                        'entityId' : window.entityId, 
                        'onuId' : window.onuId, 
                        'topSIPPstnUserPotsIdx' : window.topSIPPstnUserPotsIdx , 
                        'topSIPPstnUserTelno': telNo,
                        'topSIPPstnUserName' : username,
                        'topSIPPstnUserPwd' : password,
                        'topSIPPstnUserSipsrvId' : servicesDataTemplate,
                        'topSIPPstnUserDigitmapId' : digitalChartTemplate,
                        'topSIPPstnUserForwardType' : callTransferType,
                        'topSIPPstnUserTransferNum' : transferNum,
                        'topSIPPstnUserForwardTime' : forwardTime
                    },
                    success: function() {
                        top.nm3kRightClickTips({
                            title: "@COMMON.tip@",
                            html: "@resources/COMMON.saveSuccess@"
                        });
                        try{
                            top.getActiveFrame().reloadData();
                        }catch(Exception){
                            
                        }
                        cancelClick();
                    }, 
                    error: function(e) {
                        window.top.showMessageDlg("@COMMON.tip@", "@resources/SYSTEM.saveFailure@");
                    },
                    cache: false
                });
                
                
            }
            //取消;
            function cancelClick() {
                top.closeWindow("potsUserConfig");
            }
            
            function telNoCheck(telNo) {
            	//1-64位数字
            	var reg = /^\d{1,64}$/;
            	return reg.test(telNo);
            }
            
            function usernameCheck(username) {
            	//校验用户姓名：1-24位，支持字母、数字、下划线
            	reg = /^\w{1,24}$/;
            	return reg.test(username);
            }
            
            function passwordCheck(password) {
            	//以字母开头，1-24位，支持字母、数字、下划线
            	reg = /^\w{1,24}$/;
            	return reg.test(password);
            }
            
            function transferNumberCheck(transferNumber) {
            	//1-64位数字
           		var reg = /^\d{1,64}$/;
                return reg.test(transferNumber.replace(/(^\s*)|(\s*$)/g, ""));
            }
            
            function forwardTimeCheck(forwardTime) {
            	//5-55s
            	var reg = /^[1-9]*[1-9][0-9]*$/;
            	if(reg.test(forwardTime)){
            		if(forwardTime>4 && forwardTime<56){
                        return true;
                    }
            	}
            	return false;
            }
            
            function transferTypeSelectChange() {
            	var transferType = $("#callTransferType").val();
            	if(transferType == 2) {
            		$("#callForwardTimeBlock").css('display' ,'');  
            	}else{
            		$("#callForwardTimeBlock").css('display' ,'none'); 
            	}
            	
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">

            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 {1}">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="rightBlueTxt" width="140">@COMMON.required@@GPON.potsPortNum@</td>
                    <td width="190">
                        <input id="potsIdx" type="text" class="w180 normalInputDisabled" readonly="readonly" value="${topSIPPstnUser.topSIPPstnUserPotsIdx}" />
                    </td>
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.sipUserphone@</td>
                    <td>
                        <input id="telNo" type="text" toolTip="@GPON.telTip@" class="w180 normalInput" value="${topSIPPstnUser.topSIPPstnUserTelno}" />
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.sipUsername@</td>
                    <td width="190">
                        <input id="username" type="text" toolTip="@GPON.tip1To24@" class="w180 normalInput" value="${topSIPPstnUser.topSIPPstnUserName }"/>
                    </td>
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.sipUserpassword@</td>
                    <td id="password">
                       <%--  <input id="password" type="password" toolTip=@GPON.tip1To24@ class="w180 normalInput" value="${topSIPPstnUser.topSIPPstnUserPwd }" />
                  --%>   
                    
                  </td>
                </tr>
                <tr>
                    <td class="rightBlueTxt" width="140">@onuvoip.sipServicesDataTemplate@</td>
                    <td>
                        <s:select theme="simple" id="servicesDataTemplate" class="w180 normalSel" list="topSIPPstnUser.topSIPSrvProfInfos" listKey="topSIPSrvProfIdx" listValue="topSIPSrvProfName + '(' + topSIPSrvProfIdx + ')'" value="topSIPPstnUser.topSIPPstnUserSipsrvId" headerKey="0" headerValue="@GPON.unbound@"/>
                    </td>
                    <td class="rightBlueTxt" width="140">@onuvoip.digitalChartTemplate@</td>
                    <td>
                        <s:select theme="simple" id="digitalChartTemplate" class="w180 normalSel" list="topSIPPstnUser.digitMapProfInfos" listKey="topDigitMapProfIdx" listValue="topDigitMapProfName + '(' + topDigitMapProfIdx + ')'" value="topSIPPstnUser.topSIPPstnUserDigitmapId" headerKey="0" headerValue="@GPON.unbound@"/>
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.callTransferType@</td>
                    <td width="190">
                        <select id="callTransferType" class="w180 normalSel" onchange="transferTypeSelectChange()">
                            <option value="0">disable</option>
                            <option value="1">busy</option>
                            <option value="2">no reply</option>
                            <option value="3">unconditional</option>
                        </select>
                    </td>
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.callTransferNumber@</td>
                    <td>
                        <input id="transferNum" type="text" toolTip="@GPON.telTip@" class="w180 normalInput" value="${topSIPPstnUser.topSIPPstnUserTransferNum }" />
                    </td>
                </tr>
                <tr id="callForwardTimeBlock">
                    <td class="rightBlueTxt" width="140">@COMMON.required@@onuvoip.callForwardTime@</td>
                    <td>
                        <input id="callForwardTime" type="text" toolTip="@GPON.tipForwardTime@" class="w180 normalInput" value="${topSIPPstnUser.topSIPPstnUserForwardTime }" />
                    </td>
                </tr>
            </table>
        </div>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT60 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
                            <span><i class="miniIcoData"></i>@COMMON.save@</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
                            <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                        </a>
                    </li>
                </ol>
            </div>
        </div>
    </body>
</Zeta:HTML>