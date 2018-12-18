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
        </Zeta:Loader>
        <script type="text/javascript">
            var onuId = '${onuId}';
            var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
            var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
            $(function(){
                //加载基本数据
                initData();
                //加载权限
                initPower();
            });
            
            function initData(){
            	$.ajax({
                    url:"/gpon/onuvoip/loadSipPstnUser.tv?onuId="+onuId,
                    method:"post",cache: false,dataType:'json',
                    success:function (json) {
                        $("#topSIPPstnUserTelno").val(json.topSIPPstnUserTelno);
                        $("#topSIPPstnUserName").val(json.topSIPPstnUserName);
                        $("#topSIPPstnUserPwd").val(json.topSIPPstnUserPwd);
                    }
                });
            }
            
            function initPower(){
            	if(!refreshDevicePower){
                    $("#refreshButton").attr("disabled",true);
                }
                if(!operationDevicePower){
                    $("#saveButton").attr("disabled",true);
                }
            }
            
            function saveClick(){
            	var reg = /^[0-9]\d*$/;
            	var topSIPPstnUserTelno = $("#topSIPPstnUserTelno").val();
           	    if(!reg.exec(topSIPPstnUserTelno) || topSIPPstnUserTelno.length ==0 || topSIPPstnUserTelno.length >64){
                    $("#topSIPPstnUserTelno").focus();
                    return;
                }
            	
            	var topSIPPstnUserName = $("#topSIPPstnUserName").val();
            	if(topSIPPstnUserName.length ==0 ||topSIPPstnUserName.length >24){
            		$("#topSIPPstnUserName").focus();
            		return;
            	}
            	
            	var topSIPPstnUserPwd = $("#topSIPPstnUserPwd").val();
                if(topSIPPstnUserPwd.length ==0 ||topSIPPstnUserPwd.length >24){
                    $("#topSIPPstnUserPwd").focus();
                    return;
                }
            	
            	window.top.showWaitingDlg("@COMMON.wait@", "@onuvoip.modifyvoipuser@", 'ext-mb-waiting');
                $.ajax({
                    url:"/gpon/onuvoip/modifySipPstnUser.tv",
                    data: {
                        onuId: onuId,
                        topSIPPstnUserTelno:topSIPPstnUserTelno,
                        topSIPPstnUserName:topSIPPstnUserName,
                        topSIPPstnUserPwd:topSIPPstnUserPwd
                    },
                    method:"post",cache: false,dataType:'text',
                    success:function (text) {
                        window.top.closeWaitingDlg();
                        top.afterSaveOrDelete({
                               title: '@COMMON.tip@',
                               html: '<b class="orangeTxt">@onuvoip.modifyvoipusersuccess@</b>'
                        });
                        cancelClick();
                    },error:function(){
                        window.parent.showMessageDlg("@COMMON.tip@", "@onuvoip.modifyvoipuserfail@");
                    }
                });
            }
            
            function refresh(){
            	window.top.showWaitingDlg("@COMMON.wait@", "@onuvoip.refreshvoipuser@", 'ext-mb-waiting');
                $.ajax({
                    url:"/gpon/onuvoip/refreshSipPstnUser.tv",
                    data: {
                        onuId: onuId
                    },
                    method:"post",cache: false,dataType:'text',
                    success:function (text) {
                        window.top.closeWaitingDlg();
                        top.afterSaveOrDelete({
                               title: '@COMMON.tip@',
                               html: '<b class="orangeTxt">@onuvoip.refreshvoipusersuccess@</b>'
                        });
                        window.location.reload();
                    },error:function(){
                        window.parent.showMessageDlg("@COMMON.tip@", "@onuvoip.refreshvoipuserfail@");
                    }
                }); 
            }
            
            function cancelClick() {
                top.closeWindow("sipPstnUser");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@onuvoip.sipUserConfig@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT40">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@onuvoip.sipUserphone@</td>
                        <td>
                            <input id="topSIPPstnUserTelno" type="text" toolTip="@onuvoip.sipUserphoneTip@" class="w180 normalInput" />
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@onuvoip.sipUsername@</td>
                        <td>
                            <input id="topSIPPstnUserName" type="text" toolTip="@onuvoip.sipUsernameTip@" class="w180 normalInput" />
                        </td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt" width="180">@onuvoip.sipUserpassword@</td>
                        <td>
                            <input id="topSIPPstnUserPwd" type="text" toolTip="@onuvoip.sipUsernameTip@" class="w180 normalInput" />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT40 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" id="refreshButton" onclick="refresh()">
                            <span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" class="normalBtnBig" id="saveButton" onclick="saveClick()">
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