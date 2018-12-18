<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
<HEAD>

<%@include file="../include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<TITLE><fmt:message bundle="${resources}" key="SYSTEM.newDepartment" /></TITLE>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
    .departmentIcon {
        background-image: url( ../images/system/icoH6.png ) !important;
    }
</style>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var superiorId = <s:property value="superiorId"/>;
var tree = null;
var root = null;
var msg = I18N.SYSTEM.departmentIsEmpty;
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function() {
    var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadAllDepartment.tv'});
    tree = new Ext.tree.TreePanel({
        el: 'departmentTree', useArrows:false, autoScroll:true, animate:true, border: true, padding:'10px',
        trackMouseOver: trackMouseOver, height: 190,
        lines: true, rootVisible: false, autoScroll: true, enableDD: false,
        loader: treeLoader,
        listeners: {
        	containerclick : function(treeThis){
        		treeThis.getSelectionModel().clearSelections();
        	},
        	click : function(node){
        		//if(tree.getSelectionModel().getSelectedNode() == node){alert(选了同一个)}
        		 if( node.isSelected() ){//选中了同一个;
        			 node.unselect(); 
            		 return false;
        		}
        	}
        }
    });
    root = new Ext.tree.AsyncTreeNode({text: 'Department Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
	
    //setTimeout("doFocusing()", 500);
});

function doFocusing() {
    return  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.departmentduplicate);
}

function onNameFocus() {
    var el = Zeta$('name');
    inputFocused('name', I18N.SYSTEM.departmentIsEmpty, 'iptxt_focused')
}

function checkSame(node, el) {
    var childs = node.childNodes;
    if (childs != null) {
        var same = false;
        for (var i = 0; i < childs.length; i++) {
            if (childs[i].attributes.text == el.value.trim()) {
                same = true;
                break;
            }
        }
        if (same) {
            msg = I18N.SYSTEM.departmentIsExist;
            doFocusing();
            return true;
        } else {
            return false;
        }
    }
}

//是否为特殊字符
function isSpecialChar(str){
    var reg = /^[<>&$%]+$/;
    if(!reg.test(str)){
     return false;
    }
    return true;
}

function okClick() {
	//验证部门名称
    var el = Zeta$('name');
    //var reg = /^[\w-]{3,15}$/;
    var reg = /^[\w-\u4E00-\u9FA5]{3,15}$/;
    reg = /^[-_a-zA-Z0-9\u4e00-\u9fa5]{3,15}$/;
    if(!reg.test(el.value)){
    	el.focus();
        return;
	}
    
    var node = tree.getSelectionModel().getSelectedNode();
    var itemId = '0';
    if (node != null) {
        itemId = node.id;
        if (checkSame(node, el)) {
            return;
        }
    } else {
        //        itemId = tree.getSelectionModel().tree.root.id;
        if (checkSame(tree.getSelectionModel().tree.root, el)) {
            return;
        }
    }
    if(Zeta$("note").value.length > 138){
		return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.maxlength138);
	}
    var params = "department.superiorId=" + itemId + '&department.name=' + el.value +
                 '&department.note=' + Zeta$("note").value;
    Ext.Ajax.request({url: 'createDepartment.tv', method:'POST', params:params,
        success: function(response) {
            var json = Ext.decode(response.responseText);
            if (json.exists) {
                root.reload();
                msg = I18N.SYSTEM.departmentIsExist2;
                doFocusing();
                return;
            }
            var frame = window.parent.getFrame('departmentList');
            if (frame != null) {
                frame.onRefreshClick();
            }
            top.afterSaveOrDelete({
       	      title: I18N.COMMON.tip,
       	      html: '<b class="orangeTxt">'+I18N.COMMON.addSuccess+'</b>'
       	    });
            root.reload();
            Zeta$('name').value = '';
            Zeta$('note').value = '';
//			cancelClick();
        },
        failure: function(response) {
            window.parent.showErrorDlg();
        }
    });
    tree.getSelectionModel().unselect(node);
}

function showHelp() {
    window.open('../help/index.jsp?module=newDepartment', 'help');
}

function lastClick() {
    location.href = '../new.jsp';
}

function cancelClick() {
    window.parent.closeWindow("modalDlg");
}

function addEnterKey(e) {
    var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
        okClick();
    }
}
</script>
</HEAD>
<body class="openWinBody" onkeydown="addEnterKey(event)">
	<div class="openWinHeader">	
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle="${resources}" key="SYSTEM.department" /></b></p>
	    	<p><fmt:message bundle="${resources}" key="SYSTEM.createADepartment" /></p>
	    </div>	
	    <div class="rightCirIco pageCirIco"></div>	
	</div>
	<div class="edgeTB10LR20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
	                     <label for="name"><fmt:message bundle="${resources}" key="COMMON.name1" /><font color=red>*</font></label>
	                 </td>
	                 <td>
	                     <input style="width:334px;" id=name name="department.name" value='' class=normalInput
	                                         type=text maxlength=24 toolTip='<fmt:message bundle="${resources}" key="SYSTEM.departmentIsEmpty" />'
	                                          />
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
	                     <label for="note"><fmt:message bundle="${resources}" key="COMMON.description1" /></label>
	                 </td>
	                 <td>
	                     <textarea style="width:330px;height:40px;" id="note" maxLength="138" name="department.note" rows=3 class="normalInput" 
	                     toolTip='<fmt:message bundle="${resources}" key="SYSTEM.inputDepartmentDescription" />' ></textarea>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
	     <p class="mT10 pB0"><fmt:message bundle="${resources}" key="SYSTEM.selectParentDepartment" /></p>
     	<div id="departmentTree" class="clear-x-panel-body threeFeBg" style="width:100%; margin-top:10px;"></div>
		<div class="noWidthCenterOuter clearBoth pT10">	
		     <ol class="upChannelListOl noWidthCenter">	
		         <li><a href="javascript:;" class="normalBtnBig" onmouseup="okClick()"><span><i class="miniIcoAdd"></i><fmt:message bundle="${resources}" key="COMMON.create" /></span></a></li>	
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="COMMON.cancel" /></span></a></li>	
		     </ol>	
		</div>
	</div>

</body>
</HTML>
