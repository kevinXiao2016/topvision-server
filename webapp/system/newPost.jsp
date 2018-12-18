<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<TITLE><fmt:message bundle="${resources}" key="SYSTEM.newPost" /></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(../images/system/icoH7.png) !important;}
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

Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
var root = null;
var msg = I18N.SYSTEM.postIsEmpty;
Ext.onReady(function(){
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'loadAllPost.tv'});
    tree = new Ext.tree.TreePanel({
        el: 'postTree', useArrows:false, animate:true,  border: true, padding:'10px',
        trackMouseOver: trackMouseOver, height: 240,
        lines: true, rootVisible: false, autoScroll: true, enableDD: false,
        loader: treeLoader,
        listeners : {
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
    root = new Ext.tree.AsyncTreeNode({text: 'Post Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    tree.expandAll();
    
    //setTimeout("doFocusing()", 500);
});

function doFocusing() {
    return  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.postduplicate);
}

function onNameFocus() {
	var el = Zeta$('name');
    inputFocused('name',I18N.SYSTEM.postIsEmpty , 'iptxt_focused')
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
            msg = I18N.SYSTEM.postIsExist;
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
  	//验证职位名称
    var el = Zeta$('name');
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
        if (checkSame(node,el)) {
            return;
        }
	} else {
//        itemId = tree.getSelectionModel().tree.root.id;
        if (checkSame(tree.getSelectionModel().tree.root,el)) {
            return;
        }
    }
	var params = "post.superiorId=" + itemId + '&post.name=' + el.value + '&post.note=';
	Ext.Ajax.request({url: 'createPost.tv', method:'POST', params:params,
	   success: function(response) {
            var json = Ext.decode(response.responseText);
            if (json.exists) {
                root.reload();
                msg = I18N.SYSTEM.postIsExist2
                doFocusing();
                return;
            }
            var frame = window.parent.getFrame('postList');
			if (frame != null) {
				frame.onRefreshClick();
			}
			top.afterSaveOrDelete({
       	      title: I18N.COMMON.tip,
       	      html: '<b class="orangeTxt">'+I18N.COMMON.addSuccess+'</b>'
       	    });
            root.reload();
            Zeta$('name').value = '';
//			cancelClick();
	   },
	   failure: function(response) {
		   window.parent.showErrorDlg();
	   }
	});
    tree.getSelectionModel().unselect(node);
}

function showHelp() {
	window.open('../help/index.jsp?module=newPost', 'help');
}

function lastClick() {
	location.href = '../new.jsp';
}

function cancelClick() {
    window.parent.closeWindow("modalDlg");
}

function addEnterKey(e) {
    var event = window.event||e; // for firefox
    if (event.keyCode==KeyEvent.VK_ENTER) {
		okClick();
    }
    return false;
}
</script>
</HEAD>
	<body class="openWinBody" onkeydown="addEnterKey(event);">
		<div class="openWinHeader">	
		    <div class="openWinTip">
		    	<p><b class="orangeTxt"><fmt:message bundle="${resources}" key="SYSTEM.post" /></b></p>
		    	<p><span id="newMsg"><fmt:message bundle="${resources}" key="SYSTEM.createAPost" /></span></p>
		    </div>	
		    <div class="rightCirIco userCirIco"></div>	
		</div>
	    <div class="edgeTB10LR20">
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		                 <td class="rightBlueTxt" width="200">
		                    <label for="name"><fmt:message bundle="${resources}" key="COMMON.name1" /><font color=red>*</font></label>
		                 </td>
		                 <td>
		                     <input style="width:330px" id=name name="department.name" value='' class=normalInput type=text maxlength=24	  	
								toolTip='<fmt:message bundle="${resources}" key="SYSTEM.postIsEmpty" />' />
		                 </td>
		             </tr>		           
		         </tbody>
		     </table>
	    	<p class="mT10 pB10"><fmt:message bundle="${resources}" key="SYSTEM.selectParentPost" /></p>
	    	<div id="postTree" class="clear-x-panel-body threeFeBg" style="width:100%;"></div>
	    	<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">			
			         <li><a onclick="okClick();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i><fmt:message bundle="${resources}" key="COMMON.create" /></span></a></li>
			         <li><a onclick="cancelClick();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="COMMON.cancel" /></span></a></li>			
			     </ol>			
			</div>				    	
		</div>
<%-- 	
<div class=formtip id=tips style="display: none"></div>
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td class=WIZARD-HEADER>
	<table width=100%><tr><td><font style="font-weight:bold"><fmt:message bundle="${resources}" key="SYSTEM.post" /></font><br><br>
	&nbsp;&nbsp;<span id="newMsg"><fmt:message bundle="${resources}" key="SYSTEM.createAPost" /></span></td>
	<td align=right class=WIZARD-RHEADER><img src="../images/system/role48.gif" border=0></td></tr></table>
</td></tr>
<tr><td style="padding:10px 10px 0 10px">
<table width=100% height=100% cellspacing=0 cellpadding=0>
	<tr><td width=70 height=20 valign=top><label for="name"><fmt:message bundle="${resources}" key="COMMON.name1" /><font color=red>*</font></label></td>
	<td height=20><INPUT style="width:330px" id=name name="department.name" value='' class=iptxt type=text maxlength=15	  	
		onfocus="onNameFocus();"
		onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);"></td></tr>
	<tr><td height=5 colspan=2></td></tr>			
	<tr><td height=25 colspan=2><fmt:message bundle="${resources}" key="SYSTEM.selectParentPost" /></td></tr>
	<tr><td colspan=2 valign=top><div id="postTree" class=TREE-CONTAINER style="width:100%;"></div></td></tr>
</table>	
</td></tr>
<tr><td height=40 style="padding:10px">
	<table width=100% cellspacing=0 cellpadding=0>
	<tr><td align=right>
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onmouseup="okClick();"><fmt:message bundle="${resources}" key="COMMON.add" /></button>&nbsp;<button class=BUTTON75
    onMouseOver="this.className='BUTTON_OVER75'" onMouseDown="this.className='BUTTON_PRESSED75'" 
    onMouseOut="this.className='BUTTON75'" onclick="cancelClick();"><fmt:message bundle="${resources}" key="COMMON.off" /></button></td>
	</tr></table>
</td></tr>
</table> --%>
</BODY></HTML>
