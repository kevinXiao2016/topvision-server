<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
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

var rolePower = <%= uc.hasPower("roleManagement") %>;
var tree = null;
var tree2 = null;
function toggle(el,state) {
    if (typeof(el) == "string") {
        el = document.getElementById(el);
    }
    if (state) {
        el.style.display = "";
    } else {
        el.style.display = "none";
    }
}

function buildTree() {
	tree = new dhtmlXTreeObject("roleTree", "100%", "100%", 0);
	tree.setImagePath("../js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	tree.loadXML("loadAllRole.tv", loadRoleCallback);
    tree.setOnClickHandler(function(id) {
        if (id == 2 || id == 1) {
            tree2.enableCheckBoxes(0);
            toggle('saveBt',false);
        } else {
            tree2.enableCheckBoxes(1);
            toggle('saveBt',true);
        }
        tree2.setXMLAutoLoading("loadPowerByRole.tv?roleId=" + id);
		tree2.refreshItem(0);
		selectRoleChanged(id);	
	});
	tree2=new dhtmlXTreeObject("powerTree","100%","100%",0);
	tree2.setImagePath("../js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	tree2.enableCheckBoxes(0);
    toggle('saveBt',false);
	tree2.enableThreeStateCheckboxes(false);

    tree2.setOnCheckHandler(function(id, state) {
            //state表示设置状态
            if (tree2.hasChildren(id)) {
                var z = tree2._globalIdStorageFind(id);
                for ( var i = 0; i < z.childNodes.length; i++) {
                    tree2.setCheck(z.childNodes[i].id, state);
                    var q = tree2._globalIdStorageFind(z.childNodes[i].id);
                    for ( var j = 0; j < q.childNodes.length; j++) {
                        tree2.setCheck(q.childNodes[j].id, state);
                    }
                }
            }
            var z = tree2._globalIdStorageFind(id);
            if (z.parentObject) {
                if (state == 1) {
                    tree2.setCheck(z.parentObject.id, state);
                }
                var q = tree2._globalIdStorageFind(z.parentObject.id);
                if (q.parentObject) {
                    if (state == 1) {
                        tree2.setCheck(q.parentObject.id, state);
                    }
                }
            }
        });
        tree2.loadXML("loadPowerByRole.tv?roleId=<s:property value="roleId"/>");
        tabShown();
    }

    function loadRoleCallback() {
        tree.selectItem(<s:property value="roleId"/>);
        selectRoleChanged(<s:property value="roleId"/>);
    }

    function selectRoleChanged(id) {
        if (rolePower) {
            if (id == 2) {
                /* Zeta$('removeBt').disabled = true;
                Zeta$('saveBt').disabled = true; */
                $("#removeBt").addClass("disabledAlink");
                $("#saveBt").addClass("disabledAlink");
            } else {
                /* Zeta$('removeBt').disabled = false;
                Zeta$('saveBt').disabled = false; */
                $("#removeBt").removeClass("disabledAlink");
                $("#saveBt").removeClass("disabledAlink");
            }
        }
    }

    function loadTreeAgain() {
        var nodes = tree.getAllChildless().split(",");
        for ( var i = 0; i < nodes.length; i++) {
            var text = tree.getItemText(nodes[i]);
            if (text == window.parent.roleTreeNodeName) {
                //select it
                tree.selectItem(nodes[i])
                //scroll to it 
                tree.focusItem(nodes[i])
                //change savebt
                if (nodes[i] == 2 || nodes[i] == 1) {
                    tree2.enableCheckBoxes(0);
                    toggle('saveBt', false);
                } else {
                    tree2.enableCheckBoxes(1);
                    toggle('saveBt', true);
                }
                //set xml path
                tree2.setXMLAutoLoading("loadPowerByRole.tv?roleId=" + nodes[i]);
                // load data from server
                tree2.refreshItem(0);
                //justify the save button status
                selectRoleChanged(nodes[i]);
                window.parent.roleTreeNodeName = null;
                break;
            }
        }
    }

    function onRefreshClick(s) {
        //window.location.reload();
        tree.setXMLAutoLoading("loadAllRole.tv");
        tree.refreshItem(0);
        setTimeout(loadTreeAgain, 500)
        /* Zeta$('saveBt').disabled = true;
        Zeta$('removeBt').disabled = true; */
        $("#removeBt").addClass("disabledAlink");
        $("#saveBt").addClass("disabledAlink");
        if (s == 0) {
        	top.afterSaveOrDelete({
       	      title: I18N.COMMON.tip,
       	      html: '<b class="orangeTxt">'+I18N.SYSTEM.refreshRoleListSuccess+'</b>'
       	    });
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.refreshRoleListSuccess);
        }
    }

    function newRole() {
        window.top.createDialog("modalDlg", I18N.SYSTEM.newRole, 600, 370, "system/showNewRole.tv?superiorId=0", null,
                true, true);
    }

    function deleteRole(obj) {
        if ($(obj).hasClass("disabledAlink")) {
            return;
        }
        var selectedId = tree.getSelectedItemId();
        if (selectedId != null) {
            if (selectedId == 1) {
                return;
            }
            window.top.showConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.confirmDeleteRole, function(type) {
                if (type == 'no') {
                    return;
                }
                $.ajax({
                    url : 'deleteRole.tv',
                    type : 'GET',
                    data : {
                        roleId : selectedId
                    },
                    success : function(json) {
                        if (json.exists) {
                            window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.roleUsed);
                        } else {
                            tree.deleteItem(selectedId);
                            tree.selectItem(1);
                            /* Zeta$('saveBt').disabled = true;
                            Zeta$('removeBt').disabled = true; */
                            $("#removeBt").addClass("disabledAlink");
                            $("#saveBt").addClass("disabledAlink");

                            tree2.setXMLAutoLoading("loadPowerByRole.tv?roleId=1");
                            tree2.refreshItem(0);
                        }
                    },
                    error : function() {
                        window.top.showErrorDlg();
                    },
                    dataType : 'json',
                    cache : false
                });
            });
        } else {
            window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.notSelectedRole);
        }
    }

    function savePower() {
        var selectedId = tree.getSelectedItemId();
        if (selectedId != null) {
            var ids = [ -1 ];
            //var arr = tree2.getAllCheckedBranches();
            var arr = tree2.getAllChecked();
            if (arr != '') {
                if (arr.charAt(arr.length - 1) == ',') {
                    arr = arr.substring(0, arr.length - 1);
                }
                ids = arr.split(',');

            }
            $.ajax({
                url : 'savePower.tv',
                type : 'POST',
                data : {
                    roleId : selectedId,
                    functionIds : ids
                },
                success : function() {
                	top.afterSaveOrDelete({
               	      title: I18N.COMMON.tip,
               	      html: '<b class="orangeTxt">'+I18N.SYSTEM.savePowerSuccess+'</b>'
               	    });
                    //window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.savePowerSuccess);
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
        location.href = 'showNaviBarsForRole.tv?roleId=' + selectedId;
    }
    function doOnResize() {
        var h = $(".wrapWH100percent").height() - 120;
        if (h < 300) {
            h = 300;
        }
        $("#roleTree, #powerTree").height(h);

    }
    function doOnload() {
        if (!rolePower) {
            /* Zeta$('newBt').disabled = true;
            Zeta$('removeBt').disabled = true;
            Zeta$('saveBt').disabled = true; */
            $("#removeBt").addClass("disabledALink");
            $("#saveBt").addClass("disabledALink");
            $("#newBt").addClass("disabledALink");
        }
        //doOnResize();
        buildTree();
    }
</script>
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<style type="text/css">
	html{ height:100%;}
	.selectedTreeRow, .dhx_selected_option { padding:2px 0px;}
	#powerTree .selectedTreeRow{ background:transparent; cursor:default; }
	#powerTree td.standartTreeRow{cursor:default !important;}
</style>
</head>
<body class="bodyWH100percent bodyGrayBg" onload="doOnload();">
	<div class="wrapWH100percent">
		<div class="left-LR w370" id="sidePart">
			<p class="pannelTit" id="sidePartTit"><fmt:message bundle="${resources}" key="SYSTEM.roleList" /></p>
			<div id="roleTree" class="leftMain-LR clear-x-panel-body"></div>
			<div class="leftBottom-LR">
				<ul id="sideBottomUl" class="upChannelListOl pT10 mL10">
					<li><a id="newBt" href="javascript:void(0)" class="normalBtnBig" onclick="newRole()"><span><i class="miniIcoAdd"></i><fmt:message bundle="${resources}" key="SYSTEM.newRole" /></span></a></li>
					<li><a id="removeBt" href="javascript:void(0)" class="normalBtnBig" onclick="deleteRole(this)"><span><i class="miniIcoClose"></i><fmt:message bundle="${resources}" key="SYSTEM.deleteRole" /></span></a></li>
					<li><a href="javascript:void(0)" class="normalBtnBig" onclick="onRefreshClick(0)"><span><i class="miniIcoRefresh"></i><fmt:message bundle="${resources}" key="COMMON.refresh" /></span></a></li>
				</ul>
			</div>
		</div>
		<div class="line-LR left370" id="line"></div>
		<div class="right-LR mL378" id="mainPart">
			<p id="rightTit" class="pannelTit"><fmt:message bundle="${resources}" key="SYSTEM.powerList" /></p>
			<div id="powerTree" class="rightMain-LR"></div>
			<div class="rightBottom-LR">
				<ul id="rightBottom" class="upChannelListOl pT10 mL10">
					<li><a id="saveBt" href="javascript:void(0)" class="normalBtnBig" onclick="savePower()"><span><i class="miniIcoData"></i><fmt:message bundle="${resources}" key="SYSTEM.savePower" /></span></a></li>
				</ul>
			</div>
		</div>
	</div>

<script type="text/javascript" src="../js/jquery/dragMiddle.js"></script>
<script type="text/javascript">
	$(function(){
		function autoHeight(){
			var h = $(".wrapWH100percent").outerHeight();
			var leftTitH  = $("#sidePartTit").outerHeight();
			var h2 = $("#sideBottomUl").outerHeight();
			var leftMainH = h - leftTitH - h2 - 10;//因为padding:5px;
			if(leftMainH < 0){ leftMainH = 200;}
			$("#roleTree").height(leftMainH);
			
			var rightTitH = $("#rightTit").outerHeight();
			var rightBottomH = $("#rightBottom").outerHeight();
			if(rightBottomH < 52){ rightBottomH = 52};
			var rightMainH = h - rightTitH - rightBottomH - 32; //因为padding:16px;
			if(rightMainH < 0){ rightMainH = 200;}
			$("#powerTree").height(rightMainH);
		}
		autoHeight();
		$(window).resize(function(){
			autoHeight();
		});//end resize;
		
	});//end document.ready;
	$(window).load(function(){
 		//左侧可以拖拽宽度;
 		var o1 = new DragMiddle({ id: "line", leftId: "sidePart", rightId: "mainPart", minWidth: 370, maxWidth:500,leftBar:true });
 		o1.init();
	});//end window.onload;
	
</script>
</body></html>
