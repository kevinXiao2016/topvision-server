<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>rename</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
    href="../css/<%= cssStyleName %>/mytheme.css" />
    <fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var nmName = '<s:property value="nmName"/>';
var entityId = '<s:property value="entityId"/>';
var cmcId = '<s:property value="cmcId"/>';
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
function initData(){
    //操作权限----------------------------
    var ids = new Array();
    ids.add("ccName");
    ids.add("saveButton");
    operationAuthInit(operationDevicePower,ids);
    //-----------------------------------
    Zeta$('ccName').value = nmName;
}

function closeClick() {
    window.parent.closeWindow('renameCmcJSP');
}
//只能输入英文、数字、下划线和左斜线
function validate(str){  
    var reg = /^([a-z._|~`{}<>''""?:\\\/\(\)\[\]\-\d,;!#*$^=])+$/ig;  
    return reg.test(str);  
} 
    
function renameCC() {
    var newName = Zeta$('ccName').value;
    newName = newName.replace(/\r\n|\n/g,"");
    if(newName.length>63){
        window.parent.showMessageDlg(I18N.MENU.tip,I18N.topoGraph.note1,"error");
        onRefreshClick();
        return;
    }
    if(nmName == '' || nmName == null || nmName.length > 63){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.modifyNmNameTip);
        return;
    }
    window.parent.showWaitingDlg(I18N.COMMON.waiting,I18N.CCMTS.renaming, 'ext-mb-waiting');
    $.ajax({url: '/entity/renameEntity.tv', type: 'POST',  cache: false, dataType: 'plain',
        data: {entityId: entityId, name: newName},
        success: function() {
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.renameSuccess);
            window.parent.getActiveFrame().refreshClick();
            closeClick();
        },
        error: function() {
        	window.parent.showMessageDlg(I18N.COMMON.error,I18N.CCMTS.renameError);
        }
    });
}
</script>
</head>
<body class="openWinBody" onload="initData()">
    <div class="openWinHeader">
        <div class="openWinTip"></div>
        <div class="rightCirIco pageCirIco"></div>
    </div>
    <div class="edgeTB10LR20 pT30">
        <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr>
                    <td class="rightBlueTxt withoutBorderBottom" width="200">
                        <fmt:message
                        bundle="${cmcRes}" key="CMC.label.deviceName" />:
                    </td>
                    <td class="withoutBorderBottom">
                        <input class="normalInput modifiedFlag" style="width:200px" id="ccName" name="ccName" maxlength=63
                         toolTip='<fmt:message bundle="${cmcRes}" key="CCMTS.modifyNmNameTip" />'
                        />
                    </td>
                </tr>               
            </tbody>
        </table>
        <div class="noWidthCenterOuter clearBoth">
             <ol class="upChannelListOl pB0 pT30 noWidthCenter">
                 <li><a  onclick="renameCC()" id=saveButton href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${emsRes}" key="COMMON.save" /></span></a></li>
                 <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${emsRes}" key="COMMON.close" /></span></a></li>
             </ol>
        </div>
    </div>
</body>
</html>