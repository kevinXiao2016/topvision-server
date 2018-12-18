<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="com.topvision.platform.util.EscapeUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
    <%@include file="../include/cssStyle.inc" %>
    <fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
    <link rel="stylesheet" type="text/css" href="../css/gui.css">
    <link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css">
    <link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
    <script type="text/javascript" src="../js/ext/ext-base.js"></script>
    <script type="text/javascript" src="../js/jquery/jquery.js"></script>
    <script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
    <script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
    <script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
    <script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
    <script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
    <script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
    <%
        String roleName = "";
        if (request.getParameter("roleName") != null) {
            roleName = "?roleName='+escape(escape('" + EscapeUtil.unescape(request.getParameter("roleName")) + "'))+'&time=" + System.currentTimeMillis();
        }
        String userId = "-1";
        if (request.getParameter("userId") != null) {
            userId = request.getParameter("userId");
        }
    %>
</HEAD>
<body class="openWinBody">
	<div class="edge10">
		<p class="pB10 blueTxt"><fmt:message bundle="${resources}" key="SYSTEM.roleList" /></p>
		<div id="postTree" class="threeFeBg"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a  onclick="okClick()" id=okBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${resources}" key="COMMON.ok" /></span></a></li>
		         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="COMMON.cancel" /></span></a></li>
		     </ol>
		</div>
				
	</div>
<script>
    Ext.BLANK_IMAGE_URL = '../images/s.gif';
    var tree = null;
    var userId = <%=userId%>;
    Ext.onReady(function() {
        var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadUserRole.tv<%=roleName%>'});
        tree = new Ext.tree.TreePanel({
            el:'postTree', useArrows:useArrows, autoScroll:true, animate:true, border: true,  height:200, cls:'clear-x-panel-body', padding:'10px',
            trackMouseOver:false, lines:true, rootVisible:false, containerScroll:true, enableDD:false,
            loader: treeLoader
        });
        var root = new Ext.tree.AsyncTreeNode({text: 'Role Tree', draggable:false, id:'source'});
        tree.setRootNode(root);
        tree.render();
        root.expand();

        tree.on('click', function(n) {
            Zeta$('okBt').disabled = false;
        });
    });
    function getSelectedItemId(t) {
        var modal = t.getSelectionModel().getSelectedNode();
        var itemId = null;
        if (modal != null) {
            itemId = modal.id;
        }
        return itemId;
    }
    function getSelectedItem(t) {
        return t.getSelectionModel().getSelectedNode();
    }
    function okClick() {
        var roles = tree.getChecked('id');
        if (roles.length == 0) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.SYSTEM.SelectRole);
            return;
        }
        if (userId == 2) {
            var isok = false;
            for (var i = 0; i < roles.length; i++) {
                if (roles[i] == 2) {
                    isok = true;
                    break;
                }
            }
            if (!isok) {
                window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.SYSTEM.noDeleteAdmin);
                return;
            }
        }
        window.parent.ZetaCallback = {type:'ok', selectedItemId : roles};
        window.parent.closeWindow('roleDlg');
    }
    function cancelClick() {
        window.parent.ZetaCallback = {type:'cancel'};
        window.parent.closeWindow('roleDlg');
    }
</script>
</body>
</HTML>
