<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>EQAM CONFIG</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 自定义js引入 -->
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>

<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

    <script type="text/javascript">
        var baseStore=null;
        var baseGrid=null;
        var ipqamStreamMapInfo;
        var ipqamPidMapsNum = 0;
        var ipqamPidMapString;
        var ipqamStreamType;
        var pidMapNum;
        var totalMapNum = 512;
        var rePidMapNum;
        var normalToolbar;
        var ipqamPidMapInfoList = {'data':[], item_num: 0};
        var ipqamPidMapInfo = {'data':[], item_num: 0};
        function getIpqamPidMapInfo () {
            var tmpIpqamPidMapInfos = ipqamPidMapString.split(',');
            for (var i = 1; i < tmpIpqamPidMapInfos.length - 1; i = i + 2) {
                ipqamPidMapInfo.data[ipqamPidMapsNum] = {};
                ipqamPidMapInfo.data[ipqamPidMapsNum].ipqamOldPid = tmpIpqamPidMapInfos[i];
                ipqamPidMapInfo.data[ipqamPidMapsNum].ipqamNewPid = tmpIpqamPidMapInfos[i + 1];
                ipqamPidMapInfo.data[ipqamPidMapsNum].ipqamPidId = ipqamPidMapInfo.item_num + 1;
                ipqamPidMapInfo.item_num++;
                ipqamPidMapsNum++;
            };
            ipqamPidMapInfoList = cloneJSON(ipqamPidMapInfo);
        }
        function addPidMap () {
            if(ipqamPidMapInfoList.item_num == rePidMapNum){
                window.parent.showMessageDlg(I18N.COMMON.waiting,String.format(I18N.CMC.ipqam.pidMappingSetLimited,totalMapNum));
                return;
            }
            var pidMapRecord = {
                ipqamOldPid: 0,
                ipqamNewPid: 8191,
                ipqamPidId: ipqamPidMapInfoList.item_num + 1
            };
            
            var p = new baseStore.recordType(pidMapRecord);
            baseGrid.stopEditing();
            baseStore.insert(0, p);
            baseGrid.startEditing(0, 0);
            ipqamPidMapInfoList.data.push(p.data);
            ipqamPidMapInfoList.item_num += 1;
        }
        function deletePidMap () {
            var sm = baseGrid.getSelectionModel();
            if (sm != null && sm.hasSelection()) {
                var recordArray = sm.getSelections();
                for (var i = 0; i < recordArray.length; i++) {
                    for (var j = 0; j < ipqamPidMapInfoList.data.length; j++) {
                        if(ipqamPidMapInfoList.data[j].ipqamPidId == recordArray[i].data.ipqamPidId){
                            ipqamPidMapInfoList.data.splice(j,0);
                            ipqamPidMapInfoList.item_num--;
                            break;
                        }
                    };
                    baseStore.remove(recordArray[i]);
                };
            } else{
                window.parent.showMessageDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.selectPidTip);
                return;
            }
        }
        function resetPidMap () {
            baseStore.loadData(ipqamPidMapInfo)
        }
        function saveClick () {
            var modifiedChannelInfo = [];
            var ipqamOldPids = [];
            var ipqamNewPids = [];
            var num = 0;
            Ext.each(baseStore.data.items, function(record){
                num += 1;
                ipqamOldPids.push(record.data.ipqamOldPid);
                ipqamNewPids.push(record.data.ipqamNewPid);
                modifiedChannelInfo.push(record.data.ipqamOldPid);
                modifiedChannelInfo.push(record.data.ipqamNewPid);
            });
            for (var i = 0; i < ipqamOldPids.length - 1; i++) {
                for (var j = i + 1; j < ipqamOldPids.length; j++) {
                    if(ipqamOldPids[i] == ipqamOldPids[j]){
                        window.parent.showMessageDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.inPidNoSame);
                        return;
                    }
                };
            };
            for (var i = 0; i < ipqamNewPids.length - 1; i++) {
                for (var j = i + 1; j < ipqamNewPids.length; j++) {
                    if(ipqamNewPids[i] == ipqamNewPids[j] && ipqamNewPids[i] != 8191){
                        window.parent.showMessageDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.outPidNoSame);
                        return;
                    }
                };
            };
            modifiedChannelInfo.unshift(num * 2);
            window.top.ipqamPidMapInfo = modifiedChannelInfo.join(',');
            window.parent.closeWindow('pidMapModify');
        }
        function cancelClick(){
            window.top.ipqamPidMapInfo = ipqamPidMapString;
            window.parent.closeWindow('pidMapModify');
        }
        function bulidToolbar(){
            if(ipqamStreamType == 1 || ipqamStreamType == 3){
                return normalToolbar;
            }else{
                return null;
            }
        }
        function renderIpqamPid (value, p, record) {
            if(ipqamStreamType == 1 || ipqamStreamType == 3){
                return value;
            }else{
                return '<font color=gray>' + value + '</font>';
            }
        }
        function createPidMapList(){
            var w = 280;
            var h = 190;
            var sm = new Ext.grid.CheckboxSelectionModel();
            var baseColumns = [
                sm,
                {header: 'InPut PID',sortable: true,width:w/2, align: 'center', dataIndex: 'ipqamOldPid', renderer: renderIpqamPid,
                    editor: new Ext.grid.GridEditor(new Ext.form.TextField({
                        allowBlank: false,
                        selectOnFocus: true
                        })),menuDisabled:true},
                {header: 'OutPut PID', sortable: true,width:w/2,align: 'center', dataIndex: 'ipqamNewPid', renderer: renderIpqamPid,
                    editor: new Ext.grid.GridEditor(new Ext.form.TextField({
                        allowBlank: false,
                        selectOnFocus: true
                        })),menuDisabled:true}
                ];

            baseStore = new Ext.data.JsonStore({
                root:"data",
                proxy:new Ext.data.MemoryProxy(ipqamPidMapInfoList),
                fields: ['ipqamPidId', 'ipqamOldPid','ipqamNewPid']
            });

            normalToolbar = [
                {text: I18N.CMC.ipqam.addPidMappings, iconCls:'tbar_add', id: 'addProgramTbar', handler: addPidMap},
                {text: I18N.CMC.ipqam.delPidMappings, iconCls:'tbar_del', id: 'deleteTbar', handler: deletePidMap},
                {text: I18N.CMC.ipqam.resetModify, iconCls:'tbar_reset', id: 'resetTbar', handler: resetPidMap}
            ];
            
            var baseCm = new Ext.grid.ColumnModel(baseColumns);
            
            baseGrid = new Ext.grid.EditorGridPanel({
                id: 'baseGridContainer',
                width: '100%',
                height: h,
                border: true, 
                store: baseStore,
                cm: baseCm,
                autofill: true,
                sm: sm,
                cls:'normalTable',
                tbar: bulidToolbar(),
                //title: '',pid映射列表
                renderTo: 'pidMapInf-div'
            });
            baseStore.load();

            // Grid的事件
            var beforeEditValue;
            baseGrid.on('beforeedit', function(e){
                beforeEditValue = e.value;
                if(ipqamStreamType != 1 && ipqamStreamType != 3){
                    return false;
                }
                return true;
            });
            baseGrid.on('afteredit', function(e){
                var record = e.record;
                var field = e.field;
                var newValue = e.value;
                if ( newValue != '' && !isNumber(newValue)) {
                    record.set(e.field, beforeEditValue);
                    window.parent.showMessageDlg(I18N.COMMON.waiting, "PID"+I18N.CMC.ipqam.formatError);
                    return;
                }
                if(newValue < 0 || newValue > 8191){
                    var tipStr = I18N.CMC.ipqam.pidLimited;
                    record.set(e.field, beforeEditValue);
                    window.parent.showMessageDlg(I18N.COMMON.waiting, tipStr);
                    return;
                }
            });
        }

        // 用途：检查数据是否符合非负整数格式
        function isNumber(number) {
            var reg = /^[0-9]+$/;
            return reg.test(number);
        } 

        function cloneJSON(para){
            var rePara = null;
            var type = Object.prototype.toString.call(para);
            if(type.indexOf("Object") > -1){
                rePara = jQuery.extend(true, {}, para);
            }else if(type.indexOf("Array") > 0){
                rePara = [];
                jQuery.each(para, function(index, obj){
                    rePara.push(jQuery.cloneJSON(obj));
                });
            }else{
                rePara = para;
            }
            return rePara;
        }

        function parameterRequest(strName){
            var strHref =document.location.href;
            var intPos = strHref.indexOf("?");  
            var strRight = strHref.substr(intPos + 1);
            var arrTmp = strRight.split("&");
            for(var i = 0; i < arrTmp.length; i++)
            {  
                var arrTemp = arrTmp[i].split("=");
                if(strName){
                    if(arrTemp[0].toUpperCase() == strName.toUpperCase()) 
                        return arrTemp[1];
                }
            }
            return "";
        }

        $(function() {
            ipqamPidMapString = '${ipqamPidMapString}';
            ipqamStreamType ='${ipqamStreamType}';
            pidMapNum =  '${pidMapNum}';
            rePidMapNum = totalMapNum - pidMapNum;
            getIpqamPidMapInfo();
            createPidMapList();
        });
    </script>
</head>
  <body class="newBody">
    <div>
        <div id="pidMapInf-div" style="width:98%;height:190;"></div>
    </div>
    <div align=right style="margin-right:8px;margin-bottom: 10px; margin-top: 15px;">
        <button id=saveBtn class=BUTTON75
            onMouseOver="this.className='BUTTON_OVER75'"
            onMouseOut="this.className='BUTTON75'"
            onmousedown="this.className='BUTTON_PRESSED75'"
            onclick="saveClick()"><fmt:message bundle="${cmc }" key="CMC.text.modify"/></button>&nbsp;
        <button class=BUTTON75
            onMouseOver="this.className='BUTTON_OVER75'"
            onMouseOut="this.className='BUTTON75'"
            onmousedown="this.className='BUTTON_PRESSED75'"
            onclick="cancelClick()"><fmt:message bundle="${cmc }" key="CMC.button.cancel" /> </button>
    </div>
  </body>
</html>