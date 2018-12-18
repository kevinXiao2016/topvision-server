<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<head>
 <Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
    import js/nm3k/Nm3kSwitch
    import system/telnet/telnetClientMgt
</Zeta:Loader>
<script type="text/javascript">
var recordState = '${recordState}';
var clientType = '${clientType}';
var timeout = '${timeout}';
</script>
</head>
<body class="openWinBody">
    <div class=formtip id=tips style="display: none"></div>
    <div class="openWinHeader"> 
        <div class="openWinTip">
        </div>  
        <div class="rightCirIco flagCirIco"></div>  
    </div>
    <div class="edgeTB10LR20 pB0 pT40">
         <form id="unitConfigForm" name="unitConfigForm">
             <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                 <tbody>
                     <tr>
                        <td class="rightBlueTxt" width="250">@TelnetClient.clientType@@COMMON.maohao@</td>
                         <td>
                            <span class="columnSpan">
	                            <input type="radio" id="inner-client" name="clientType" value="1" checked="checked"/>
                                <label for="inner-client">@TelnetClient.innerType@</label>
                            </span>
                            <span class="columnSpan">
                                <input type="radio" id="external-client" name="clientType" value="2" />
                                <label for="external-client">@TelnetClient.externalType@</label>
                            </span>
                         </td>
                     </tr>
                     <tr class="darkZebraTr">
                        <td class="rightBlueTxt">@TelnetClient.recordEnable@@COMMON.maohao@</td>
                        <td>
                            <div id="record-state-container"></div>
                        </td>
                     </tr>
                     <tr>
                        <td class="rightBlueTxt" width="250">@TelnetClient.timeout@@COMMON.maohao@</td>
                         <td>
                            <input id="timeout" class="normalInput w150" type="text" maxlength="5" tooltip="1000-30000" />ms
                         </td>
                     </tr>
                 </tbody>
             </table>
         </form>
    </div>
    
    <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
        <ol class="upChannelListOl pB0 pT40 noWidthCenter">
            <li>
                <a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a>
            </li>
            <li>
                <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
            </li>
        </ol>
    </div>
    
</body>
</Zeta:HTML>