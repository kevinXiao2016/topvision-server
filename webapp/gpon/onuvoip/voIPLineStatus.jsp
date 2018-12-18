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
	        $(function(){
	            //加载基本数据
	            initData();
	            //加载权限
	            initPower();
	        });
	        
	        function initData(){
	        	$.ajax({
                    url:"/gpon/onuvoip/loadVoIPLineStatus.tv?onuId="+onuId,
                    method:"post",cache: false,dataType:'json',
                    success:function (json) {
                        $("#topVoIPLineCodec").text(json.topVoIPLineCodecString);
                        $("#topVoIPLineServStatus").text(json.topVoIPLineServStatusString);
                        $("#topVoIPLineSessType").text(json.topVoIPLineSessTypeString);
                        $("#topVoIPLineState").text(json.topVoIPLineStateString);
                    }
                });
	        }
	        
	        function refresh(){
	        	window.top.showWaitingDlg("@COMMON.wait@", "@onuvoip.refreshvoipline@", 'ext-mb-waiting');
                $.ajax({
                    url:"/gpon/onuvoip/refreshVoIPLineStatus.tv",
                    data: {
                        onuId: onuId
                    },
                    method:"post",cache: false,dataType:'text',
                    success:function (text) {
                        window.top.closeWaitingDlg();
                        top.afterSaveOrDelete({
                               title: '@COMMON.tip@',
                               html: '<b class="orangeTxt">@onuvoip.refreshvoiplinesuccess@</b>'
                        });
                        window.location.reload();
                    },error:function(){
                        window.parent.showMessageDlg("@COMMON.tip@", "@onuvoip.refreshvoiplinefail@");
                    }
                }); 
	        }
        
	        function initPower(){
	        	if(!refreshDevicePower){
	                $("#refreshButton").attr("disabled",true);
	            }
	        }
	        
            function cancelClick() {
                top.closeWindow("voipLineStatus");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@onuvoip.voipline@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT20">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@onuvoip.EncodingMode@@COMMON.maohao@</td>
                        <td>
                            <span id="topVoIPLineCodec"></span>
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@onuvoip.voipServicestate@@COMMON.maohao@</td>
                        <td>
                            <span id="topVoIPLineServStatus"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt" width="180">@onuvoip.Sessiontype@@COMMON.maohao@</td>
                        <td>
                            <span id="topVoIPLineSessType"></span>
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@onuvoip.voiplineStatus@@COMMON.maohao@</td>
                        <td>
                            <span id="topVoIPLineState"></span>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT40 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" id="refreshButton" onclick = "refresh()">
                            <span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span>
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