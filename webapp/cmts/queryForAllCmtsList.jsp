<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources,com.topvision.ems.network.resources,com.topvision.ems.cmts.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var loopbackStatus;
var loopbackStatusIcomcls;

function doRefresh() {
	store.reload();
}

function viewCmtsSnap(cmcId, name) {
    window.parent.addView('entity-' + cmcId, name, 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ]});
    return pagingToolbar;
}

function renderSysStatus(value, p, record) {
    if (record.data.topCcmtsSysStatus == '1') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                I18N.label.online);
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                I18N.label.offline);
    }
}

function onAddToGoogleClick() {
    window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
}

function cmtsDetailInfo() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    viewCmtsSnap(record.data.cmcId, record.data.nmName);
}

function buildEntityInput() {
    return '<td width=120 align=center>' + I18N.CMTS.entityName + '</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 80px" id="name"></td>'
}

function buildConnectPersonInput() {
    return '<td width=120 align=center>' + I18N.CMTS.contactPerson + '</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 80px" id="connectPerson"></td>'
}

function buildIpInput() {
    return '<div id="cmcIpAddress"><td width=40px align="right">IP</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 100px"  id="ipAddress"></td></div>'
}

function refreshCmts() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;

    var cmcType = record.json.cmcDeviceStyle;
    var entityId = record.json.cmcId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CMTS.confirmRefreshCmts, function(button, text) {
        if (button == "yes") {
            window.parent.showWaitingDlg(I18N.COMMON.waiting, String.format(I18N.CCMTS.refreshingCcmts), 'waitingMsg', 'ext-mb-waiting');
            $.ajax({
            	url:'/cmts/refreshCmts.tv?entityId='+ entityId,
                type:'POST',
                dateType:'json',
                success:function(response) {
                    if (response == "success") {
                       //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCmtsSuccess);
                       window.parent.closeWaitingDlg();
                    	top.afterSaveOrDelete({
               				title: '@COMMON.tip@',
               				html: '<b class="orangeTxt">'+ I18N.CCMTS.refreshCmtsSuccess + '</b>'
               			});
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCmtsFail);
                    }
                    doRefresh();
                },
                error:function() {
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCmtsFail);
                },
                cache:false
            });
        }
    });
}

function renderSysUpTime(value, p, record) {
	var sysUpTime = record.data.topCcmtsSysUpTime;
	if(sysUpTime != null && sysUpTime > 0){
		var time = record.data.topCcmtsSysUpTime * 10;
	    var dt = record.data.dt;
	    var timeString;
	    var realtime = time + parseInt((new Date()).getTime() - dt.time);
	    timeString = arrive_timer_format(realtime / 1000)
	}else{
		timeString = I18N.label.deviceLinkDown;
	}
    return timeString;
}
function arrive_timer_format(s) {
	var t
    if (s > -1) {
        hour = Math.floor(s / 3600);
        min = Math.floor(s / 60) % 60;
        sec = Math.floor(s) % 60;
        day = parseInt(hour / 24);
        if (day > 0) {
            hour = hour - 24 * day
            t = day + I18N.label.day + hour + I18N.label.hours
        } else {
            t = hour + I18N.label.hours
        }
        if (min < 10) {
            t += "0"
        }
        t += min + I18N.label.minutes
        if (sec < 10) {
            t += "0"
        }
        t += sec + I18N.label.seconds
    }
    return t
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}
function manuRender(v,m,r){
	return String.format("<a href='javascript:;' onClick='showViewOperation({0})'>@COMMON.view@</a>  / <a href='javascript:;' class='withSub'  onClick = 'showMoreOperation({0},event)'>@COMMON.other@</a>",r.id);
}
function showViewOperation(rid){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	viewCmtsSnap(record.data.cmcId, record.data.nmName);
}

var cmtsmenu = new Ext.menu.Menu({
    id:"cmtsmenu",
    enableScrolling:false,
    minWidth:160,
    shadow:false,
    items:[
        {text:I18N.CMTS.cmtsMessage,id:"cmtsDetailInfo",handler:cmtsDetailInfo},
        {
            id:'addCmtsToTopo',
            text:I18N.NETWORK.addToTopo,
            disabled:!topoGraphPower,
            handler:function() {
            	var sm = grid.getSelectionModel();
            	if (sm != null && sm.hasSelection()) {
            		window.top.createDialog('topoFolderTree', I18N.NETWORK.addToTopo, 800, 500, 'network/popFolderTree.jsp', null, true, true,
            			function(){
            				var callbackObj = window.top.ZetaCallback
            				if (callbackObj == null || callbackObj.type != 'ok') {return;}
            				var selectedItemId = callbackObj.selectedItem.itemId
            				var selectedItemName = callbackObj.selectedItem.itemName
            				if (selectedItemId == null) {return}
            				var selections = sm.getSelections()
            				var entityIds = []
            				for (var i = 0; i < selections.length; i++) {
            					entityIds[i] = selections[i].data.cmcId
            				}
            				Ext.Ajax.request({url: '../entity/moveEntityFromRecyle.tv',
            				   success: function() {
            					   window.parent.addView("topo" + selectedItemId, selectedItemName, "topoRegionIcon", "topology/getTopoMapByFolderId.tv?folderId=" + selectedItemId,null,true);
            				   },
            				   failure: function(){
            					   window.top.showMessageDlg(I18N.RECYLE.error, I18N.RECYLE.moveEntityFailure, 'error')
            				   },
            				   params: {destFolderId:selectedItemId, entityIds:entityIds}
            				})
            				window.top.ZetaCallback = null
            		})
            	}	
            }
        },
        '-',
        {text: "@RESOURCES/COMMON.realias@", iconCls: 'bmenu_rename', handler: renameEntity},//修改别名
        {text:I18N.CMTS.refreshCmts,id:"refreshCmts",handler:refreshCmts}
    ]});


function renameEntity() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.cmcId;
    window.top.createDialog('renameEntity', I18N.COMMON.realias, 600, 250,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function changeEntityName(entityId, name) {
    store.reload();
}

function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid); 
	grid.getSelectionModel().selectRecords([record]);
	cmtsmenu.showAt([event.clientX,event.clientY]);
}
function opeartionRender(value, p, record) {
    return String.format('<a href="#" onclick=\'viewCmtsSnap("' + record.data.cmcId + '","' + record.data.nmName + '");\'>{0}</a>',
            value);
}
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "/images/s.gif";
Ext.onReady(function () {
    Ext.QuickTips.init();
    var w = document.body.clientWidth;
    var h = $(window).height();
    var columns = null;
    columns = [
        {header: I18N.label.status, width: w/10, sortable:true, align : 'center', dataIndex: 'topCcmtsSysStatus',renderer: renderSysStatus},
        {id:'entityName', header: '<div style="text-align:center">'+I18N.COMMON.alias+'</div>', sortable:false, align : 'left', dataIndex: 'nmName',renderer:opeartionRender},
        {header: '<div style="text-align:center">'+I18N.CMTS.manageIP+'</div>', width: w/6, sortable:true, align : 'center', dataIndex: 'ipAddress', renderer:function(value, p, record) {
            var disValue = value;
            return '<a href="#" class=my-link onclick=\'viewCmtsSnap("' + record.data.cmcId + '","' + record.data.nmName + '");\'>' + disValue + '</a>';
        }},
        {header: I18N.CMTS.sysUpTime, width: w/6, sortable:true,align: 'center', dataIndex: 'topCcmtsSysUpTime' ,renderer: renderSysUpTime},
        {header: I18N.CCMTS.contactPerson,width: w/6, sortable:true,align: 'center', dataIndex: 'topCcmtsSysContact'},
        {header: I18N.CMTS.entityLocation,width: w/6, sortable:true,align: 'center', dataIndex: 'topCcmtsSysLocation',renderer:addCellTooltip},
        {header: I18N.CCMTS.softVersion, width:150 , sortable:true,align : 'center', dataIndex: 'topCcmtsSysSwVersion',renderer:addCellTooltip},
        {header: "@COMMON.manu@", width:120, dataIndex: '',renderer : manuRender}
    ];

    store = new Ext.data.JsonStore({
        url: '/cmts/queryCmtsList.tv?cmts=true',
        root: 'data',
        totalProperty: 'rowCount',
        idProperty:"cmcId",
        remoteSort: true,
        fields: ['cmcId', 'nmName','cmcDeviceStyle', 'cmcDeviceStyleString', 'topCcmtsSysDescr', 'topCcmtsSysMacAddr', 'topCcmtsSysLocation', 'topCcmtsSysContact',
            'topCcmtsSysRAMRatiotoString','topCcmtsSysUpTimeString','topCcmtsSysCPURatiotoString', 'topCcmtsSysFlashRatiotoString', 'topCcmtsSysStatus',
            'topCcmtsSysSwVersion', 'manageIp','interfaceInfo', 'entityId', 'ipAddress','topCcmtsSysUpTime','dt','oltVersion'],
        //排序规则
        sortData: function(field, direction) {
            var fn;
            //关键地方,重写排序排序规则
            if ("topCcmtsSysUpTimeString" == field) {
                fn = function(rec1, rec2) {
                    var now = new Date().getTime();
                    v1 = rec1.data.topCcmtsSysUpTime / 100;
                    dt1 = rec1.data.dt;
                    v1 = v1 + parseInt(( now - dt1.time) / 1000);

                    var v2 = rec2.data.topCcmtsSysUpTime / 100;
                    dt2 = rec2.data.dt;
                    v2 = v2 + parseInt((now - dt2.time) / 1000);

                    if (!v1 || !v2) {
                        return !v1 ? 1 : -1;
                    } else {
                        return v1 == v2 ? 0 : v1 > v2 ? 1 : -1;
                    }
                };
                this.data.sort(direction, fn);
            } else {
                direction = direction || 'ASC';
                var st = this.fields.get(field).sortType;
                fn = function(r1, r2) {
                    var v1 = st(r1.data[field]), v2 = st(r2.data[field]);
                    return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
                };
                this.data.sort(direction, fn);
            }
        }
    });
    store.setDefaultSort('cmcId', 'ASC');

    var cm = new Ext.grid.ColumnModel(columns);
    var toolbar = [
        buildEntityInput(),'-',
        buildConnectPersonInput(),'-',
        buildIpInput(),'-',
        {text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: queryClick}
    ];

    grid = new Ext.grid.GridPanel({id: 'extGridContainer',        
        bodyCssClass:"normalTable",
        animCollapse: animCollapse,
        trackMouseOver:trackMouseOver,
        border: false,
        region:'center',
        store: store,
        tbar: new Ext.Toolbar({
            cls:'cmtsListToolBar',
            items:toolbar
        }),
        cm: cm,
        autoExpandColumn:'entityName',
        autoExpandMin:100,
        viewConfig: {
        	hideGroupedColumn: true,
            enableNoGroups: true,
            forceFit:true
            },
        //renderTo: document.body ,
        bbar: buildPagingToolBar()
        }
    );
    
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
        e.preventDefault();
        cmtsmenu.showAt(e.getPoint());
    })
    store.load({params:{start:0, limit: pageSize}});
    
    $(".cmtsListToolBar").keydown(function(event) {
        if (event.keyCode == 13) {
            queryClick();
        }
    });
    new Ext.Viewport({
        layout: 'border',
        items: [grid]
    });
});

function queryClick() {
    var nmName = Zeta$('name').value;
    var connectPerson = Zeta$('connectPerson').value;
    var cmcIp = Zeta$('ipAddress').value;

    if (cmcIp != "" && !Validator.isFuzzyIpAddress(cmcIp)) {
        return window.parent.showMessageDlg(I18N.tip.tipMsg, I18N.tip.ipInputFormatError, "tip", function() {
            $("#ipAddress").focus();
        });
    }

    store.on("beforeload", function() {
        store.baseParams = { cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize};
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
    store.load({params: { cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize}});
}
</script>
</head>
<body class="whiteToBlack">
</body>
</Zeta:HTML>