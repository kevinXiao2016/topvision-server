//--------------------------------菜单start--------------------------------------------------------------
var modifyMirrorNameMenu = new Ext.menu.Menu({
    id : 'modifyMirrorNameMenu',
    enableScrolling : false,
    items : [ {
        id : 'setMirrorName',
        autoScroll : false,
        text : I18N.MIRROR.modifyMirrorName,
        disabled : !operationDevicePower,
        handler : setMirrorName
    } ]
});
//--------------------------------菜单end------------------------------------------------------------

//--------------------------------菜单items start----------------------------------------------------
function buildInportSingleSniMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'inportJoinOutpotSingle',
        autoScroll : false,
        text : I18N.MIRROR.joinOutFlow,
        disabled : !operationDevicePower,
        handler : inportJoinOutpotSingle
    };
    items[items.length] = {
        id : 'inportLeaveMirrorSingle',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : inportLeaveMirrorSingle
    };
    items[items.length] = {
        id : 'inportChangeToDstpot',
        autoScroll : false,
        text : I18N.MIRROR.translate2Target,
        disabled : !operationDevicePower,
        handler : inportChangeToDstpot
    };
    return items
}

function buildInportMultiMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'inportJoinOutpot',
        autoScroll : false,
        text : I18N.MIRROR.joinOutFlow,
        disabled : !operationDevicePower,
        handler : inportJoinOutpot
    };
    items[items.length] = {
        id : 'inportLeaveMirror',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : inportLeaveMirror
    };
    return items
}

function buildOutportSingleSniMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'outportJoinInpotSingle',
        autoScroll : false,
        text : I18N.MIRROR.joinInFlow,
        disabled : !operationDevicePower,
        handler : outportJoinInpotSingle
    };
    items[items.length] = {
        id : 'outportLeaveMirrorSingle',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : outportLeaveMirrorSingle
    };
    items[items.length] = {
        id : 'outportChangeToDstpot',
        autoScroll : false,
        text : I18N.MIRROR.translate2Target,
        disabled : !operationDevicePower,
        handler : outportChangeToDstpot
    };
    return items
}

function buildOutportMultiMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'outportJoinInpot',
        autoScroll : false,
        text : I18N.MIRROR.joinInFlow,
        disabled : !operationDevicePower,
        handler : outportJoinInpot
    };
    items[items.length] = {
        id : 'outportLeaveMirror',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : outportLeaveMirror
    };
    return items
}

function buildBlankportSingleSniMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'blankJoinInpotSingle',
        autoScroll : false,
        text : I18N.MIRROR.joinInFlow,
        disabled : !operationDevicePower,
        handler : blankJoinInpotSingle
    };
    items[items.length] = {
        id : 'blankJoinOutpotSingle',
        autoScroll : false,
        text : I18N.MIRROR.joinOutFlow,
        disabled : !operationDevicePower,
        handler : blankJoinOutpotSingle
    };
    items[items.length] = {
        id : 'blankChangeToDstpot',
        autoScroll : false,
        text : I18N.MIRROR.translate2Target,
        disabled : !operationDevicePower,
        handler : blankChangeToDstpot
    };
    return items
}

function buildBlankportMultiMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'blankJoinInpot',
        autoScroll : false,
        text : I18N.MIRROR.joinInFlow,
        disabled : !operationDevicePower,
        handler : blankJoinInpot
    };
    items[items.length] = {
        id : 'blankChangeToOutpot',
        autoScroll : false,
        text : I18N.MIRROR.joinOutFlow,
        disabled : !operationDevicePower,
        handler : blankChangeToOutpot
    };
    return items
}

function buildBothportSingleSniMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'bothportLeaveInport',
        autoScroll : false,
        text : I18N.MIRROR.exitInFlow,
        disabled : !operationDevicePower,
        handler : bothportLeaveInport
    };
    items[items.length] = {
        id : 'bothportLeaveOutport',
        autoScroll : false,
        text : I18N.MIRROR.exitOutFlow,
        disabled : !operationDevicePower,
        handler : bothportLeaveOutport
    };
    items[items.length] = {
        id : 'bothportLeaveport',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : bothportLeaveport
    };
    items[items.length] = {
        id : 'bothportChangeToDstpot',
        autoScroll : false,
        text : I18N.MIRROR.translate2Target,
        disabled : !operationDevicePower,
        handler : bothportChangeToDstpot
    };
    return items
}

function buildBothportMultiMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'bothportMultiLeaveInport',
        autoScroll : false,
        text : I18N.MIRROR.exitInFlow,
        disabled : !operationDevicePower,
        handler : bothportMultiLeaveInport
    };
    items[items.length] = {
        id : 'bothportMultiLeaveOutport',
        autoScroll : false,
        text : I18N.MIRROR.exitOutFlow,
        disabled : !operationDevicePower,
        handler : bothportMultiLeaveOutport
    };
    items[items.length] = {
        id : 'bothportMultiLeaveport',
        autoScroll : false,
        text : I18N.MIRROR.exitMirror,
        disabled : !operationDevicePower,
        handler : bothportMultiLeaveport
    };
    return items
}

//SNI端口反键菜单
function buildSniToVlanMenuItems() {
    var items = [];
    items[items.length] = {
        id : 'sniToVlan',
        autoScroll : false,
        text : I18N.MIRROR.joinWlan,
        menu : [ {
            id : 'sniToUntagged',
            text : 'UNTAGGED',
            enableScrolling : false,
            disabled : !operationDevicePower,
            handler : sniToUntagged
        }, {
            id : 'sniToTagged',
            text : 'TAGGED',
            enableScrolling : false,
            disabled : !operationDevicePower,
            handler : sniToTagged
        } ]
    };
    return items
}

//tagged端口右键菜单
function buildSniLeaveVlanMenu2Items() {
    var items = [];
    items[items.length] = {
        id : 'sniModeSwitch2',
        autoScroll : false,
        text : I18N.MIRROR.switch2UT,
        disabled : !operationDevicePower,
        handler : sniModeSwitch2
    };
    items[items.length] = {
        id : 'sniLeaveVlan2',
        autoScroll : false,
        text : I18N.MIRROR.exitWlan,
        disabled : !operationDevicePower,
        handler : sniLeaveVlan2
    };
    return items;
}

//untagged端口右键菜单
function buildSniLeaveVlanMenu1Items() {
    var items = [];
    items[items.length] = {
        id : 'sniModeSwitch',
        autoScroll : false,
        text : I18N.MIRROR.switch2Tag,
        disabled : !operationDevicePower,
        handler : sniModeSwitch
    };
    items[items.length] = {
        id : 'sniLeaveVlan',
        autoScroll : false,
        text : I18N.MIRROR.exitWlan,
        disabled : !operationDevicePower,
        handler : sniLeaveVlan
    };
    return items;
}
//--------------------------------菜单items end------------------------------------------------------

//-----------------菜单中调用的方法start--------------------------------------------------------------
function sniLeaveVlan2() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPort = $vlanGroup.taggedPort;
    // tagged端口
    var untaggedPort = $vlanGroup.untaggedPort;
    // untagged端口
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口列表
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口列表
    for (i = 0; i < indexCache.length; i++) {
        taggedPortIndexList.remove(indexCache[i])
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitWlan + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function() {
        	top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">@MIRROR.exitWlanOk@</b>'
   			});
            loadVlanListJson();
            vlan_store.loadData(vlanListJson.data);
            vlan_grid.selModel.selectRow(vlanSelected, true);
            rowClick(vlan_grid, vlanSelected);
            $("#vlanIndex").val("");
            indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitWlanErro);
        }
    })
}
function sniModeSwitch2() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPort = $vlanGroup.taggedPort;
    // tagged端口
    var untaggedPort = $vlanGroup.untaggedPort;
    // untagged端口
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口列表
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口列表
    for (i = 0; i < indexCache.length; i++) {
        taggedPortIndexList.remove(indexCache[i])
        untaggedPortIndexList.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.switch2UT + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function() {
        	top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">'+I18N.MIRROR.switch2UTOk+'</b>'
   			});
            loadVlanListJson();
            vlan_store.loadData(vlanListJson.data);
            vlan_grid.selModel.selectRow(vlanSelected, true);
            rowClick(vlan_grid, vlanSelected);
            $("#vlanIndex").val("");
            indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.switch2UTError);
        }
    })
}
function sniLeaveVlan() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPort = $vlanGroup.taggedPort;
    // tagged端口
    var untaggedPort = $vlanGroup.untaggedPort;
    // untagged端口
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口列表
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口列表
    for (i = 0; i < indexCache.length; i++) {
        untaggedPortIndexList.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitWlan + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function() {
        	top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">'+I18N.MIRROR.exitWlanOk+'</b>'
   			});
            loadVlanListJson();
            vlan_store.loadData(vlanListJson.data);
            vlan_grid.selModel.selectRow(vlanSelected, true);
            rowClick(vlan_grid, vlanSelected);
            $("#vlanIndex").val("");
            indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitWlanError)
        }
    })
}

function sniModeSwitch() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPort = $vlanGroup.taggedPort;
    // tagged端口
    var untaggedPort = $vlanGroup.untaggedPort;
    // untagged端口
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口列表
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口列表
    for (i = 0; i < indexCache.length; i++) {
        taggedPortIndexList.add(indexCache[i]);
        untaggedPortIndexList.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.switch2Tag + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function() {
        	top.afterSaveOrDelete({
   				title: "@COMMON.tip@",
   				html: '<b class="orangeTxt">'+I18N.MIRROR.switch2TagOk+'</b>'
   			});
            loadVlanListJson();
            vlan_store.loadData(vlanListJson.data);
            vlan_grid.selModel.selectRow(vlanSelected, true);
            rowClick(vlan_grid, vlanSelected);
            $("#vlanIndex").val("");
            indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.switch2TagError)
        }
    })
}

function sniToTagged() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口
    for (i = 0; i < indexCache.length; i++) {
        taggedPortIndexList.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinWlan + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function(text) {
            top.afterSaveOrDelete({
       			title: I18N.COMMON.tip,
       			html: '<b class="orangeTxt">'+I18N.MIRROR.joinWlanOk+'</b>'
            });
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.joinWlanOk)
            loadVlanListJson();
            vlan_store.loadData(vlanListJson.data);
            vlan_grid.selModel.selectRow(vlanSelected, true);
            rowClick(vlan_grid, vlanSelected);
            $("#vlanIndex").val("");
            indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.switch2TagError)
        }
    })
}

function sniToUntagged() {
    var entityId = getEntityId();
    var $vlanGroup = vlanListJson.data[vlanSelected];
    var vlanIndex = $vlanGroup.vlanIndex;
    var taggedPortIndexList = $vlanGroup.taggedPortIndexList.slice();
    // tagged端口
    var untaggedPortIndexList = $vlanGroup.untaggedPortIndexList.slice();
    // untagged端口
    for (i = 0; i < indexCache.length; i++) {
        untaggedPortIndexList.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinWlan + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/vlan/updateTagStatus.tv?entityId=" + entityId + "&vlanIndex=" + vlanIndex
                + "&taggedPortIndexList=" + taggedPortIndexList + "&untaggedPortIndexList=" + untaggedPortIndexList,
        success : function(text) {
            	top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">'+I18N.MIRROR.joinWlanOk+'</b>'
       			});
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.joinWlanOk);
                loadVlanListJson();
                vlan_store.loadData(vlanListJson.data);
                vlan_grid.selModel.selectRow(vlanSelected, true);
                rowClick(vlan_grid, vlanSelected);
                $("#vlanIndex").val("");
                indexCache = [];
        },
        error : function(text) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.MIRROR.switch2UTError);
        }
    })
}

function bothportMultiLeaveport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitMirror + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitMirror + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
        }
    })
}

function bothportMultiLeaveOutport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitOutFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitOutFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitOutFlow + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitOutFlow + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitOutFlow + I18N.COMMON.error + '!')
        }
    })
}

function bothportMultiLeaveInport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitInFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitInFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
        }
    })
}

function bothportChangeToDstpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        dstport = [];
        dstport.add(indexCache[i]);
        inport.remove(indexCache[i]);
        outport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.translate2Target + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text== "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.translate2Target + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
        }
    })
}

function bothportLeaveport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitMirror + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitMirror + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
        }
    })
}

function bothportLeaveOutport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitOutFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitOutFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitOutFlow + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
        }
    })
}

function bothportLeaveInport() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitInFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        // async: false,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitInFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitInFlow + I18N.COMMON.error + '!')
        }
    })
}

function blankChangeToOutpot() {
    var entityId = getEntityId();
    var dstportOct = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortList;
    var tempArray = dstportOct.split(":");
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    if (tempArray[2] != "00") {
        for (i = 0; i < indexCache.length; i++) {
            outport.add(indexCache[i]);
        }
        showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinOutFlow + '...', 'ext-mb-waiting');
        $.ajax({
            url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                    + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                    + "&sniMirrorGroupDstPortIndexList=" + dstport,
            method : "post",
            success : function(text) {
                if (text == "success") {
                	top.afterSaveOrDelete({
           				title: "@COMMON.tip@",
           				html: I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!'
           			});
                    //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!')
                    loadMirrorListJson();
                    rowClick(mirror_grid, mirrorSelected);
                    indexCache = [];
                } else {
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!')
                }
            },
            error : function(text) {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!')
            }
        })
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.plsAssignTarget);
    }
}

function blankJoinInpot() {
    var entityId = getEntityId();
    var dstportOct = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortList;
    var tempArray = dstportOct.split(":");
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    if (tempArray[2] != "00") {
        for (i = 0; i < indexCache.length; i++) {
            inport.add(indexCache[i]);
        }
        showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinInFlow + '...', 'ext-mb-waiting');
        $.ajax({
            url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                    + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                    + "&sniMirrorGroupDstPortIndexList=" + dstport,
            method : "post",
            success : function(text) {
                if (text == "success") {
                	top.afterSaveOrDelete({
           				title: "@COMMON.tip@",
           				html: I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!'
           			});
                    //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!');
                    loadMirrorListJson();
                    rowClick(mirror_grid, mirrorSelected);
                    indexCache = [];
                } else {
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
                }
            },
            error : function(text) {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
            }
        })
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.plsAssignTarget);
    }
}

function blankChangeToDstpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        dstport = [];
        dstport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.translate2Target + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.translate2Target + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
                //转换成功，移除右侧树菜单;
                if(serviceTree){
                    serviceTree.destroy();
                    serviceTree = null;
                }
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
        }
    })
}

function blankJoinOutpotSingle() {
    var entityId = getEntityId();
    var dstportOct = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortList;
    var tempArray = dstportOct.split(":");
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    if (tempArray[2] != "00") {
        for (i = 0; i < indexCache.length; i++) {
            outport.add(indexCache[i]);
        }
        showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinOutFlow + '...', 'ext-mb-waiting');
        $.ajax({
            url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                    + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                    + "&sniMirrorGroupDstPortIndexList=" + dstport,
            method : "post",
            success : function(text) {
                if (text == "success") {
                	top.afterSaveOrDelete({
           				title: "@COMMON.tip@",
           				html: I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!'
           			});
                    //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!');
                    loadMirrorListJson();
                    rowClick(mirror_grid, mirrorSelected);
                    indexCache = [];
                } else {
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!');
                }
            },
            error : function(text) {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!');
            }
        })
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.plsAssignTarget)
    }
}

function blankJoinInpotSingle() {
    var entityId = getEntityId();
    var dstportOct = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortList;
    var tempArray = dstportOct.split(":");
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    if (tempArray[2] != "00") {
        for (i = 0; i < indexCache.length; i++) {
            inport.add(indexCache[i]);
        }
        showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinInFlow + '...', 'ext-mb-waiting');
        $.ajax({
            url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                    + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                    + "&sniMirrorGroupDstPortIndexList=" + dstport,
            method : "post",
            success : function(text) {
                if (text == "success") {
                	top.afterSaveOrDelete({
           				title: "@COMMON.tip@",
           				html: I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!'
           			});
                    //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!');
                    loadMirrorListJson();
                    rowClick(mirror_grid, mirrorSelected);
                    indexCache = [];
                } else {
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
                }
            },
            error : function(text) {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
            }
        })
    } else {
        window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.plsJoinDesPort);
    }
}

function outportLeaveMirror() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.exitMirror + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html:  I18N.MIRROR.exitMirror + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirror + I18N.COMMON.error + '!')
        }
    })
}

function outportJoinInpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinInFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html:  I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!')
        }
    })
}

function outportChangeToDstpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
        dstport = [];
        dstport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.translate2Target + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html:  I18N.MIRROR.translate2Target + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.success + '!')
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2Target + I18N.COMMON.error + '!')
        }
    })
}

function outportLeaveMirrorSingle() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, '@COMMON.on@@MIRROR.exitMirror@...');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv",
        data : {
            entityId : entityId,
            sniMirrorGroupIndex : mirrorIndex,
            sniMirrorGroupSrcOutPortIndexList : outport.join(),
            sniMirrorGroupSrcInPortIndexList : inport.join(),
            sniMirrorGroupDstPortIndexList : dstport.join()
        },
        method : "post",
        success : function(txt) {
            if (txt == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html:  '@MIRROR.exitMirror@@COMMON.success@!'
       			});
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", '@MIRROR.exitMirror@@COMMON.error@!');
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", '@MIRROR.exitMirror@@COMMON.error@!');
        }
    });
}

function outportJoinInpotSingle() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinInFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html:  I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.success + '!');
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinInFlow + I18N.COMMON.error + '!');
        }
    })
}

function inportLeaveMirror() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.MIRROR.exitingMirror, 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitMirrorOk
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorOk)
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorEr)
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorEr)
        }
    })
}

function inportJoinOutpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.MIRROR.joiningOutFlow, 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.translate2OPOk
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2OPOk)
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2OPEr)
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2OPEr)
        }
    })
}

function inportChangeToDstpot() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.remove(indexCache[i]);
        dstport = [];
        dstport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.MIRROR.translating2Target, 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.translate2TargetOk
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2TargetOk)
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2TargetEr)
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.translate2TargetEr)
        }
    })
}

function inportLeaveMirrorSingle() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        inport.remove(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.MIRROR.exitingMirror, 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        method : "post",
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.exitMirrorOk
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorOk)
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorEr)
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.exitMirrorEr)
        }
    })
}

function inportJoinOutpotSingle() {
    var entityId = getEntityId();
    var mirrorIndex = mirrorListJson.data[mirrorSelected].sniMirrorGroupIndex;
    var dstport = mirrorListJson.data[mirrorSelected].sniMirrorGroupDstPortIndexList.slice();
    // 目的端口index列表
    var outport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcOutPortIndexList.slice();
    // out端口index列表
    var inport = mirrorListJson.data[mirrorSelected].sniMirrorGroupSrcInPortIndexList.slice();
    // in端口index列表
    for (i = 0; i < indexCache.length; i++) {
        outport.add(indexCache[i]);
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.on + I18N.MIRROR.joinOutFlow + '...', 'ext-mb-waiting');
    $.ajax({
        url : "/epon/mirror/updateMirrorPortList.tv?entityId=" + entityId + "&sniMirrorGroupIndex=" + mirrorIndex
                + "&sniMirrorGroupSrcOutPortIndexList=" + outport + "&sniMirrorGroupSrcInPortIndexList=" + inport
                + "&sniMirrorGroupDstPortIndexList=" + dstport,
        success : function(text) {
            if (text == "success") {
            	top.afterSaveOrDelete({
       				title: "@COMMON.tip@",
       				html: I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!'
       			});
                //window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.success + '!');
                loadMirrorListJson();
                rowClick(mirror_grid, mirrorSelected);
                indexCache = [];
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!');
            }
        },
        error : function(text) {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.MIRROR.joinOutFlow + I18N.COMMON.error + '!');
        }
    })
}

function setMirrorName() {
    var nodes = mirror_grid.getSelectionModel().getSelections();
    var mirrorId = nodes[0].json.sniMirrorGroupIndex;
    var mirrorName = nodes[0].json.sniMirrorGroupName;
    window.top.createDialog("setMirrorName", I18N.MIRROR.modifyMirrorName, 600, 220,
            "/epon/mirror/showMirrorNameJsp.tv?entityId=" + entityId + "&sniMirrorGroupName=" + mirrorName + "&sniMirrorGroupIndex=" + mirrorId , null, true, true);
}
//-----------------菜单中调用的方法end----------------------------------------------------------------

//-----------------工具方法start---------------------------------------------------------------------------
function showWaitingDlg(title, icon, text, duration) {
    window.top.showWaitingDlg(title, icon, text, duration);
}
function slotNumToSlotType1(slotNum) {
    return bType[olt.slotList[slotNum - 1].topSysBdPreConfigType];
}
function oltViewGetPortType(divId) {
    var arr = divId.split("_");
    if ('epua' == arr[0]) {
        return 'pon';
    } else if ('geua' == arr[0]) {
        if (olt.slotList[arr[1] - 1].portList[arr[2] - 1].sniMediaType == "fiber") {
            return 'fiber';
        } else if (olt.slotList[arr[1] - 1].portList[arr[2] - 1].sniMediaType == "twistedPair") {
            return 'sni';
        }
    } else if ('mpua' == arr[0]) {
        if (olt.slotList[arr[1] - 1].portList[arr[2] - 1].sniMediaType == "twistedPair") {
            return 'sni';
        } else if (olt.slotList[arr[1] - 1].portList[arr[2] - 1].sniMediaType == "fiber") {
            return 'fiber';
        }
    } else if ('xgua' == arr[0]) {
        return 'fiber';
    }
}
function clearPage2(cid) {
    while (divCache2.length != 0) {
        var divCacheObject = divCache2.pop();
        var array = divCacheObject.split("_");
        if (cid != divCacheObject) {// 如果点击的是当前div，则不做修改
            $temp = $("#" + divCacheObject);
            var $port =  selectionModel.getSelectedItem(divCacheObject);
            $temp.css('backgroundImage',String.format('url(/epon/image/{0}/{0}.png)',$port.portSubType));
            if ($temp.css("opacity") == 1 && $temp.css("height") == "" + coordinateParam.slotHeight + "px") {
                $temp.animate("opacity" , 0.7);
            }
        }
    }
}
//-----------------工具方法end---------------------------------------------------------------------------