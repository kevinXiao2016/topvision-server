<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css">
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">

    function tabActivate() {
        window.top.setStatusBarInfo('', '');
    }
    function tabDeactivate() {
    }
    function tabRemoved() {
    }
    function tabShown() {
        window.top.setStatusBarInfo('', '');
    }

    var rolePower = <%=uc.hasPower("roleManagement")%>;
    var tree = null;
    function buildTree() {
        tree = new dhtmlXTreeObject("roleTree", "100%", "100%", 0);
        tree.setImagePath("../js/dhtmlx/tree/imgs/vista/");
        tree.loadXML("loadAllRole.tv", loadRoleCallback);

        tree.setOnClickHandler(function(id) {
            selectRoleChanged(id);
        });
        tabShown();
    }

    function loadRoleCallback() {
        tree.selectItem(<s:property value="roleId"/>);
        selectRoleChanged(<s:property value="roleId"/>);
    }

    function selectRoleChanged(id) {
        if (rolePower) {
            if (id == 1) {
                Zeta$('removeBt').disabled = true;
                Zeta$('saveBt').disabled = true;
            } else {
                Zeta$('removeBt').disabled = false;
                Zeta$('saveBt').disabled = false;
            }
        }
        $.ajax({
            url : 'loadNaviButtonByRole.tv',
            type : 'GET',
            data : {
                roleId : id
            },
            success : function(json) {
                var buttons = Zeta$N('naviButton');
                for ( var i = 0; i < buttons.length; i++) {
                    buttons[i].checked = false;
                }
                for ( var i = 0; i < json.length; i++) {
                    Zeta$(json[i].name).checked = true;
                }
            },
            error : function() {
                window.top.showErrorDlg();
            },
            dataType : 'json',
            cache : false
        });
    }

    function onRefreshClick() {
        tree.setXMLAutoLoading("loadAllRole.tv");
        tree.refreshItem(0);
        Zeta$('saveBt').disabled = true;
        Zeta$('removeBt').disabled = true;
    }

    function newRole() {
        window.top.createDialog("modalDlg", '<fmt:message bundle="${sys}" key="sys.newRole" />', 420, 280,
                "system/showNewRole.tv?superiorId=0", null, true, true);
    }

    function deleteRole() {
        var selectedId = tree.getSelectedItemId();
        if (selectedId != null) {
            if (selectedId == 1) {
                return;
            }
            window.top.showConfirmDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.confirmRemoveRole" />', function(type) {
                if (type == 'no') {
                    return;
                }
                $.ajax({
                    url : 'deleteRole.tv',
                    type : 'GET',
                    data : {
                        roleId : selectedId
                    },
                    success : function() {
                        tree.deleteItem(selectedId);
                        tree.selectItem(1);
                        Zeta$('saveBt').disabled = true;
                        Zeta$('removeBt').disabled = true;
                    },
                    error : function() {
                        window.top.showErrorDlg();
                    },
                    dataType : 'plain',
                    cache : false
                });
            });
        } else {
            window.top.showMessageDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.selRoleToDel" />');
        }
    }

    function savePower() {
        var selectedId = tree.getSelectedItemId();
        if (selectedId != null) {
            var buttons = Zeta$N('naviButton');
            var ids = [];
            for ( var i = 0; i < buttons.length; i++) {
                if (buttons[i].checked) {
                    ids[ids.length] = buttons[i].value;
                }
            }
            $.ajax({
                url : 'saveRoleNaviPower.tv',
                type : 'GET',
                data : {
                    roleId : selectedId,
                    naviIds : ids
                },
                success : function() {
                    window.top.showMessageDlg('<fmt:message bundle="${sys}" key="mibble.tip" />', '<fmt:message bundle="${sys}" key="sys.saveSuccess" />');
                },
                error : function() {
                    window.top.showErrorDlg();
                },
                dataType : 'plain',
                cache : false
            });
        }
    }

    function viewPower() {
        var selectedId = tree.getSelectedItemId();
        if (selectedId == null) {
            selectedId = 1;
        }
        location.href = 'showPowerForRole.tv?roleId=' + selectedId;
    }

    function doOnResize() {
        var h = document.body.clientHeight - 120;
        if (h < 300) {
            h = 300;
        }
        Zeta$('roleTree').style.height = Zeta$('powerTree').style.height = h;
    }
    function doOnload() {
        if (!rolePower) {
            Zeta$('newBt').disabled = true;
            Zeta$('removeBt').disabled = true;
            Zeta$('saveBt').disabled = true;
        }
        doOnResize();
        buildTree();
    }
</script>
</head>
<body class=BLANK_WND onload="doOnload();" onresize=doOnResize();>
    <center>
        <table cellspacing=0 cellpadding=0>
            <tr>
                <td colspan=3>&nbsp;</td>
            </tr>
            <tr>
                <td height=25px width=300px><fmt:message bundle="${sys}" key="sys.roleList" />:</td>
                <td width=30px></td>
                <td width=400px>
                    <div class=ultab>
                        <ul>
                            <li class=selected><a href=""><fmt:message bundle="${sys}" key="sys.nav" /></a></li>
                            <li><a href="#" onclick="viewPower();"><fmt:message bundle="${sys}" key="sys.optPro" /></a></li>
                        </ul>
                    </div>
                </td>
            </tr>
            <tr>
                <td><div id="roleTree" class=TREE-CONTAINER style="width: 300px; height: 300px;"></div></td>
                <td></td>
                <td><div id="powerTree" class=TREE-CONTAINER style="width: 400px; height: 300px;">
                        <table>
                            <s:iterator value="naviBars">
                                <tr>
                                    <Td>
                                    <input id="<s:property value="name"/>" name="naviButton" type=checkbox value="<s:property value="naviId"/>">
                                    <label for="<s:property value="name"/>">
                                    <img src="../<s:property value="icon16"/>" border=0 align=absmiddle>&nbsp;
                                    <s:property value="displayName" />
                                    </label>
                                    </Td>
                                </tr>
                            </s:iterator>
                        </table>
                    </div></td>
            </tr>
            <tr>
                <Td height=35>
                    <button id=newBt class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'" onMouseOut="this.className='BUTTON95'" onMouseDown="this.className='BUTTON_PRESSED95'" onclick="newRole()"><fmt:message bundle="${sys}" key="sys.newRole" /></button>&nbsp;
                    <button id=removeBt disabled class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'" onMouseOut="this.className='BUTTON95'" onMouseDown="this.className='BUTTON_PRESSED95'" onclick="deleteRole()"><fmt:message bundle="${sys}" key="sys.removeRole" /></button>
                </Td>
                <td></td>
                <td align=right>
                    <button id=saveBt disabled class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'" onMouseOut="this.className='BUTTON95'" onMouseDown="this.className='BUTTON_PRESSED95'" onclick="savePower()"><fmt:message bundle="${sys}" key="sys.save" /></button>&nbsp;
                    <button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'" onMouseOut="this.className='BUTTON95'" onMouseDown="this.className='BUTTON_PRESSED95'" onclick="onRefreshClick()"><fmt:message bundle="${sys}" key="sys.refresh" /></button>
                </td>
            </tr>
        </table>
    </center>
</body>
</html>
