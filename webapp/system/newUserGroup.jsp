<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css" />

<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script src="../js/jquery/jquery.js"></script>
<script src="../js/zeta-core.js"></script>
<script>
    function doOnload() {
        setTimeout("doFocusing()", 500);
    }

    function doFocusing() {
        Zeta$('name').focus();
    }

    function okClick() {
        var el = Zeta$('name');
        if (el.value.trim() == '') {
            el.focus();
            return;
        }
        $.ajax({
            url : 'createUserGroup.tv',
            type : 'POST',
            data : jQuery(userGroupForm).serialize(),
            success : function() {
                var frame = window.top.getFrame('userGroupList');
                if (frame != null) {
                    try {
                        frame.onRefreshClick();
                    } catch (err) {
                    }
                }
                cancelClick();
            },
            error : function() {
                window.top.showErrorDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.newUserGroupFail" />!');
            },
            dataType : 'plain',
            cache : false
        });
    }

    function cancelClick() {
        window.top.closeWindow("modalDlg");
    }

    function addEnterKey(e) {
        var event = window.event || e; // for firefox
        if (event.keyCode == KeyEvent.VK_ENTER) {
            okClick();
        }
    }
</script>
</HEAD>
<BODY class=POPUP_WND onload="doOnload();"
    onkeydown="addEnterKey(event);">
    <div class=formtip id=tips style="display: none"></div>
    <table width=100% height=100% cellspacing=0 cellpadding=0>
        <tr>
            <td class=WIZARD-HEADER>
                <table width=100%>
                    <tr>
                        <td><font style="font-weight: bold"><fmt:message bundle="${sys}" key="sys.userGroup" /></font><br>
                        <br> &nbsp;&nbsp;<span id="newMsg"><fmt:message bundle="${sys}" key="sys.newUserGroup" /></span></td>
                        <td align=right class=WIZARD-RHEADER><img
                            src="../images/system/post48.gif" border=0></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td style="padding: 10px 10px 0 10px">
                <form id="userGroupForm" name="userGroupForm">
                    <table width=100% height=100% cellspacing=0
                        cellpadding=0>
                        <tr>
                            <td width=70 height=20 valign=top><label
                                for="name"><fmt:message bundle="${sys}" key="sys.name" />: <font color=red>*</font></label></td>
                            <td height=20><INPUT id=name
                                name="userGroup.name" class=iptxt
                                type=text style="width: 330px"
                                maxlength=24
                                onfocus="inputFocused('name', '<fmt:message bundle="${sys}" key="sys.nameNotEmpty" />', 'iptxt_focused')"
                                onblur="inputBlured(this, 'iptxt');"
                                onclick="clearOrSetTips(this);"></td>
                        </tr>
                        <tr>
                            <td height=8 colspan=2></td>
                        </tr>
                        <tr>
                            <td valign=top><fmt:message bundle="${sys}" key="sys.description" />:</td>
                            <td valign=top><textarea id="note"
                                    class=iptxa rows="5"
                                    name="userGroup.description"
                                    style="width: 330px; overflow: auto;"
                                    onfocus="inputFocused('note', '', 'iptxa_focused')"
                                    onblur="inputBlured(this, 'iptxa');"
                                    onclick="clearOrSetTips(this);"></textarea></td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <tr>
            <td height=40 style="padding: 10px" align=right>
                <button type="button" class=BUTTON75
                    onMouseOver="this.className='BUTTON_OVER75'"
                    onMouseOut="this.className='BUTTON75'"
                    onMouseDown="this.className='BUTTON_PRESSED75'"
                    onmouseup="okClick()"><fmt:message bundle="${sys}" key="sys.done" /></button>&nbsp;
                <button class=BUTTON75 type="button"
                    onMouseOver="this.className='BUTTON_OVER75'"
                    onMouseDown="this.className='BUTTON_PRESSED75'"
                    onMouseOut="this.className='BUTTON75'"
                    onclick="cancelClick();"><fmt:message bundle="${sys}" key="mibble.cancel" /></button>
            </td>
        </tr>
    </table>
</BODY>
</HTML>
