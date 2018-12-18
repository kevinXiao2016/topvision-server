<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
    import js.zetaframework.component.NetworkNodeSelector
</Zeta:Loader>
<style type="text/css">

</style>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/zetaframework/Validator.js"></script>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;
var ipEntityList = ${ipEntityJson};
var lastRow = null;
var store = null;
var grid = null;
var entityTypes = ${entityTypes};
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({
        id : 'extPagingBar',
        pageSize : pageSize,
        store : store,
        displayInfo : true,
        items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
            '-' ]
    });
    return pagingToolbar;
}

function showCmc8800BSnap(entityId,entityName){
    window.parent.addView('entity-' + entityId, entityName, 'entityTabIcon',
            '/cmc/showCmcPortal.tv?cmcId=' + cmcId);
}

function showEntitySnap(entityId, entityName) {
    window.parent.addView('entity-' + entityId, entityName, 'entityTabIcon',
            'portal/showEntitySnapJsp.tv?entityId=' + entityId);
}

function renderName(value, p, record){
    if(EntityType.isCcmtsWithAgentType(record.data.typeId)){
        return  String.format('<a href="#" onclick="showCmc8800BSnap({0}, \'{1}\')">{2}</a>' + '&nbsp;&nbsp;',
                record.data.entityId, record.data.entityName, value);
    }
    return  String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>' + '&nbsp;&nbsp;',
            record.data.entityId, record.data.entityName, value);
}
function countRender(value, p, record){
    var allNum = record.data.onlineNum + record.data.offlineNum + record.data.otherNum;
    return String.format(record.data.onlineNum + '/' + record.data.offlineNum + '/' + record.data.otherNum + '/' + allNum);
}
function cpeCountRender(value, p, record){
    var allNum = record.data.onlineNum + record.data.offlineNum + record.data.otherNum;
    return String.format(record.data.cpeInteractiveNum + '/' + record.data.cpeBroadbandNum + '/' + record.data.cpeMtaNum + '/' + record.data.cpeNum);
}
function cmCountRender(value, p, record){
    var allNum = record.data.onlineNum + record.data.offlineNum + record.data.otherNum;
    return String.format(record.data.interactiveNum + '/' + record.data.broadbandNum + '/' + record.data.mtaNum + '/' + record.data.integratedNum);
}
function cpuRender(value, p, record){
    var cpu = parseInt(record.data.cpuRate * 100);
    if(cpu >= 0){
        return String.format(cpu + '%');
    } else {
        return String.format('-');
    }
}
function renderTime(value, p, record){
    var timeString = record.data.timeString;
    if(timeString != null && timeString != ""){
        return timeString;
    }else{
        return String.format("-");
    }
}
function buildCmMac(){
    return '<div id="cmMac"><td width=40px align="right">CM MAC:</td>&nbsp;' +
            '<td><input class="normalInput mR10" type=text style="width: 100px"  id="macAddress"></td></div>'
}

function buildEntityName(){
    return '<div id="cmMac"><td width=40px align="right">@COMMON.alias@:</td>&nbsp;' +
            '<td><input class="normalInput mR10" type=text style="width: 100px"  id="entityName"></td></div>'
}

function buildDeviceTypeSelect() {
    var head = '<td style="width: 40px;"  align="right">' + I18N.CCMTS.entityStyle + ':' + '</td>&nbsp;' +
            '<td style="width: 100px;">' +
            '<select id="deviceType" class="normalSel" style="width: 100px;">' +
            '<option value="0" selected>' + I18N.COMMON.pleaseSelect + '</option>';
    var tail = '</select></td>' ;
    var options = "";
    for(var i = 0; i <entityTypes.length; i ++){
        options += '<option value="' +entityTypes[i].typeId+ '">' + entityTypes[i].displayName + '</option>';
    }
    return head + options + tail;
}

function validateMacAddress(macaddr)
{
    var reg1 = /^([A-Fa-f0-9]{2,2}\:){0,5}[A-Fa-f0-9]{0,2}$/;
    var reg2 = /^([A-Fa-f0-9]{2,2}\-){0,5}[A-Fa-f0-9]{0,2}$/;
    var reg3 = /^([A-Fa-f0-9]{4,4}\:){0,2}[A-Fa-f0-9]{0,4}$/;
    var reg4 = /^([A-Fa-f0-9]{4,4}\-){0,2}[A-Fa-f0-9]{0,4}$/;
    var reg5 = /^([A-Fa-f0-9]{4,4}\.){0,2}[A-Fa-f0-9]{0,4}$/;
    if (reg1.test(macaddr)) {
        return true;
    } else if (reg2.test(macaddr)) {
        return true;
    } else if (reg3.test(macaddr)) {
        return true;
    } else if (reg4.test(macaddr)) {
        return true;
    } else if (reg5.test(macaddr)) {
        return true;
    } else {
        return false;
    }
}
function queryCmRealTimeUserNum(){
    lastRow = null;
    var cmMac = Zeta$('macAddress').value;
    var entityName = Zeta$('entityName').value;
    var deviceType = Zeta$('deviceType').value;
    var pattern = /^[a-z0-9:]{1,17}$/i;
    if(entityName != "" && !V.isAnotherName(entityName, null)) {
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.queryNmNameTip);
    }
    /*  if(cmMac.length != 0 && cmMac.match(pattern)==null){
     Ext.Msg.alert(I18N.COMMON.tip,I18N.CCMTS.macErrorMessage);
     return;
     } */
    if(!V.isMac(cmMac) && cmMac != '' && cmMac != null){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.macError);
        return;
    }
    store.on("beforeload",function(){
        store.baseParams={cmMac: cmMac, name: entityName, entityType: deviceType};
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
    store.load({params: {cmMac: cmMac, name: entityName, entityType: deviceType}});
}
Ext.onReady(function() {
    var expander = new Ext.ux.grid.RowExpander({
        id : 'expander' ,
        dataIndex : 'entityId',
        enableCaching : false,
        tpl : ''
    });
    var w = $(window).width();
    var h = $(window).height();
    var columns = [ expander,{
        header : I18N.CCMTS.entityName,
        width : w/15,
        sortable : true,
        align : 'center',
        dataIndex : 'entityName',
        renderer : renderName
    },
        {
            header : I18N.COMMON.manageIp,
            width : w/15,
            sortable : true,
            align : 'center',
            dataIndex : 'entityIp'
        }, {
            header : I18N.cpuUtility,
            width : w/15,
            sortable : true,
            align : 'center',
            dataIndex : 'cpuRate',
            renderer : cpuRender
        }, {
            header : I18N.CMCPE.CMClassification,
            width : w/8,
            sortable : true,
            align : 'center',
            dataIndex : 'interactiveNum',
            renderer : cmCountRender
        }, {
            header : I18N.CMCPE.CPEClassification,
            width : w/8,
            sortable : true,
            align : 'center',
            dataIndex : 'cpeNum',
            renderer : cpeCountRender
        }, {
            header : I18N.CMCPE.COUNT,
            width : w/9,
            sortable : true,
            align : 'center',
            dataIndex : 'count',
            renderer : countRender
        }, {
            header : I18N.cmcUserNum.lastCollectTime,
            width : w/8,
            sortable : true,
            align : 'center',
            dataIndex : 'timeString' ,
            renderer : renderTime
        }];

    store = new Ext.data.JsonStore({
        url : '/cmCpe/loadAllDeviceCmNum.tv',
        root : 'data',
        totalProperty: 'rowCount',
        fields : [ 'entityId', 'entityName', 'entityIp', 'cpuRate',
            'interactiveNum', 'broadbandNum', 'mtaNum',
            'integratedNum', 'onlineNum', 'offlineNum', 'otherNum',
            'cpeInteractiveNum', 'cpeBroadbandNum', 'cpeMtaNum', 'cpeNum', 'timeString' ]
    });
    var cm = new Ext.grid.ColumnModel(columns);

    var toolbar = new Ext.Toolbar();
    var toolbars = [
        buildEntityName(),
        buildCmMac(),
        buildDeviceTypeSelect(),
        {
            xtype:"button",
            text:I18N.entity.alert.queryButton,
            iconCls:"bmenu_find",
            margins : " 0 3 0 3",
            handler: function(){
                queryCmRealTimeUserNum();
            }
        }
        /*
         ,'-',{
         text : I18N.RECYLE.refresh,
         iconCls : 'bmenu_refresh',
         handler : function() {
         window.location.reload();
         }} */
    ];
    toolbar.add(toolbars);
    toolbar.doLayout();

    grid = new Ext.grid.GridPanel({
        id : 'extGridContainer',
        /* width : w,
         height : h, */
        bodyCssClass : 'normalTable',
        plugins: expander,
        border : false,
        stripeRows:true,
        region : 'center',
        store : store,
        tbar : new Ext.Toolbar({
            cls : 'ccmtsListToolBar',
            items : toolbars
        }),
        cm : cm,
        viewConfig : {
            forceFit: true
        },
        renderTo : document.body,
        loadMask : true,
        bbar : buildPagingToolBar()
    });
    store.load({
        params : {
            start : 0,
            limit : pageSize
        }
    });
    new Ext.Viewport({layout: 'border', items: [grid]});

    expander.on("beforeexpand",function(expander,r,body,row){
        if(lastRow != null){
            expander.collapseRow(lastRow);
        }
        lastRow = row;
        var html = '';
        var entityId = r.data.entityId;
        var entityIp = r.data.entityIp;
        $.ajax({
            url : "/cmCpe/loadCcmtsCmNumInfo.tv?entityId=" + entityId,
            type: 'POST',
            async:false,
            cache : false,
            dataType:'json',
            error : function() {
                var tpl1 = new Ext.Template(
                        '<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">' +  I18N.CMC.text.equipment + entityIp + I18N.CMC.title.detailInfo + '</span></p>' +
                                "<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
                                "<thead>" +
                                "<tr>" +
                                "<th align='center'>CMTS</th>" +
                                "<th align='center'>"+I18N.CCMTS.upStreamChannel+"</th>" +
                                "<th align='center' width='130px'>"+I18N.CMCPE.OperatMode+"</th>" +
                                "<th align='center'>"+I18N.CMCPE.frequency+"</th> " +
                                "<th align='center' width='240px'>"+I18N.CMCPE.CMClassification+"</th>" +
                                "<th align='center' width='210px'>"+I18N.CMCPE.CPEClassification+"</th>" +
                                "<th align='center' width='130px'>"+I18N.CMCPE.COUNT+"</th>" +
                                "<th align='center'>"+I18N.CHANNEL.snr+"</th>" +
                                "</tr>" +
                                "</thead>" +
                                "<tbody id='tbody-append-child'></tbody></table></div>"

                );

                expander.tpl = tpl1;
                expander.tpl.compile();
            },
            success : function(realTimeCmNumJson) {
                if(realTimeCmNumJson == null || realTimeCmNumJson.data == null){
                    var tpl1 = new Ext.Template(
                            '<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">' +  I18N.CMC.text.equipment + entityIp + I18N.CMC.title.detailInfo + '</span></p>' +
                                    "<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
                                    "<thead>" +
                                    "<tr>" +
                                    "<th align='center'>CMTS</th>" +
                                    "<th align='center'>"+I18N.CCMTS.upStreamChannel+"</th>" +
                                    "<th align='center' width='130px'>"+I18N.CMCPE.OperatMode+"</th>" +
                                    "<th align='center'>"+I18N.CMCPE.frequency+"</th> " +
                                    "<th align='center' width='240px'>"+I18N.CMCPE.CMClassification+"</th>" +
                                    "<th align='center' width='210px'>"+I18N.CMCPE.CPEClassification+"</th>" +
                                    "<th align='center' width='130px'>"+I18N.CMCPE.COUNT+"</th>" +
                                    "<th align='center'>"+I18N.CHANNEL.snr+"</th>" +
                                    "</tr>" +
                                    "</thead>" +
                                    "<tbody id='tbody-append-child'></tbody></table></div>"

                    );

                    expander.tpl = tpl1;
                    expander.tpl.compile();
                }else{
                    $.each(realTimeCmNumJson.data,function(){
                        var cmcHtml = '';
                        var cmcCmReatimeNumList = this.cmcCmReatimeNumList;
                        var cmcIndexString = this.cmcIndexString;
                        var size = cmcCmReatimeNumList.length;
                        for(var i = 0; i < size; i++){
                            cmcHtml = cmcHtml +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].channelString + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].channelModulationProfileString + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].frequencyString + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].interactiveNum + "/"  + cmcCmReatimeNumList[i].broadbandNum + "/"
                                    + cmcCmReatimeNumList[i].mtaNum + "/"  + cmcCmReatimeNumList[i].integratedNum + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].cpeInteractiveNum + "/"  + cmcCmReatimeNumList[i].cpeBroadbandNum + "/"
                                    + cmcCmReatimeNumList[i].cpeMtaNum + "/" + cmcCmReatimeNumList[i].cpeNum  + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].onlineNum + "/" + cmcCmReatimeNumList[i].offlineNum + "/" + cmcCmReatimeNumList[i].otherNum + "/" +
                                    (cmcCmReatimeNumList[i].onlineNum + cmcCmReatimeNumList[i].offlineNum + cmcCmReatimeNumList[i].otherNum ) + "</td>" +
                                    "<td align='center'>" + cmcCmReatimeNumList[i].snrString + "</td>" +
                                    "</tr>";
                        }
                        cmcHtml = "<td rowspan='" + size + "' align='center'>" + cmcIndexString + "</td>" + cmcHtml;
                        html = html + cmcHtml;
                    });

                    var tpl1 = new Ext.Template(
                            '<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">' +  I18N.CMC.text.equipment + entityIp + I18N.CMC.title.detailInfo + '</span></p>' +
                                    "<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
                                    "<thead>" +
                                    "<tr>" +
                                    "<th align='center'>CMTS</th>" +
                                    "<th align='center'>"+I18N.CCMTS.upStreamChannel+"</th>" +
                                    "<th align='center' width='130px'>"+I18N.CMCPE.OperatMode+"</th>" +
                                    "<th align='center'>"+I18N.CMCPE.frequency+"</th> " +
                                    "<th align='center' width='240px'>"+I18N.CMCPE.CMClassification+"</th>" +
                                    "<th align='center' width='210px'>"+I18N.CMCPE.CPEClassification+"</th>" +
                                    "<th align='center' width='130px'>"+I18N.CMCPE.COUNT+"</th>" +
                                    "<th align='center'>"+I18N.CHANNEL.snr+"</th>" +
                                    "</tr>" +
                                    "</thead>" +
                                    "<tbody id='tbody-append-child'>" + html + "</tbody></table></div>"

                    );

                    expander.tpl = tpl1;
                    expander.tpl.compile();
                }

            }
        });
    });

});
</script>
</head>
<body class="whiteToBlack">
</body>
</Zeta:HTML>
