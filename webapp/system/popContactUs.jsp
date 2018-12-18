<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<TITLE></TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script>
function cancelClick() {
    window.parent.closeWindow('modalDlg');
}
</script>
</HEAD><BODY class=POPUP_WND style="padding:10px">
    <div class="productmain">
        <div class="productcontent">
            <h4>
                <fmt:message bundle="${resources}" key="COMMON.contactUs" />
            </h4>
        </div>
        <div class="porductIntro" style="padding: 0 10px">
            <P>
                <STRONG><fmt:message bundle="${resources}" key="COMMON.TOPVISION" /></STRONG>
                <BR>TOPVISION TECHNOLOGIES CO.,LTD.
            </P>
            <P>
                
                <fmt:message bundle="${resources}" key="COMMON.AfterSalesTel" />：86-10-58851682/58858351
                <BR>
                <fmt:message bundle="${resources}" key="COMMON.supportCalls" />：86-10-58858590
                <BR>
                <fmt:message bundle="${resources}" key="COMMON.companyFax" />：86-10-58858592
                <BR>
                <fmt:message bundle="${resources}" key="COMMON.companyAddress2" />
                <BR>
                <fmt:message bundle="${resources}" key="COMMON.ZipCode" />：100085 
                <BR><BR><BR><BR>
            </P>        
        </div>
        <div>
            <button style="margin-left: 380px;" class=BUTTON75 type="button"
                onMouseOver="this.className='BUTTON_OVER75'"
                onmousedown="this.className='BUTTON_PRESSED75'"
                onMouseOut="this.className='BUTTON75'" onclick="cancelClick()">
                <fmt:message bundle="${resources}" key="COMMON.close" />
            </button>
        </div>
    </div>
</BODY></HTML>
