<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.ems.cm.domain.CmPollObject"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.cm.resources" var="cm"/>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CM
</Zeta:Loader>
<style type="text/css">
.topoFolderIcon {background-image: url(/images/network/topoFolder.gif) !important;}
.topoFolderIcon1 {background-image: url(/images/network/topoicon.gif) !important;}
.topoFolderIcon5 {background-image: url(/images/network/subnet.gif) !important;}
.topoFolderIcon6 {background-image: url(/images/network/cloudy16.gif) !important;}
.topoFolderIcon7 {background-image: url(/images/network/href.png) !important;}
.topoFolderIcon20 {background-image: url(/images/network/region.gif) !important;}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">

$(function(){
    var tree,treeLoader;
    var oltSm,ccSm,cmSm;
    var pollObj;
    var oldPollObjName;
    // --------------------------- Function Define -------------------------------
    function loadPollObject(){
        $.ajax({
              url: '/cmpoll/queryPollObjectByObjId.tv',
              type: 'post',
              dataType:"json",
              async:false,
              data:{
                objectId : '${objectId}'  
              },
              cache: false, 
              success: function(response) {
                  pollObj=response.data;
                  $("#objName").val(pollObj.objectName);
                  oldPollObjName=pollObj.objectName;
                  var t=pollObj.objectType;
                  if(t=='<%=CmPollObject.OBJECT_TYPE_AREA%>'){
                      enableModeRadio("panel_area");
                      showPanel("panel_area");
                  }else if(t=='<%=CmPollObject.OBJECT_TYPE_OLT%>'){
                      enableModeRadio("panel_olt");
                      showPanel("panel_olt");
                  }else if(t=='<%=CmPollObject.OBJECT_TYPE_CCMTS%>'){
                      enableModeRadio("panel_cc");
                      showPanel("panel_cc");
                  }else if(t=='<%=CmPollObject.OBJECT_TYPE_EXCEL%>'){
                      enableModeRadio("panel_excel");
                      showPanel("panel_excel");
                  }
              }
        });
    }
    
    function enableModeRadio(v){
        $("input[name=createMode]").not("input[value="+v+"]").attr("disabled",true);
        $("input[name=createMode]").filter("input[value="+v+"]").attr("checked",true);
    }
    
    function getSelectedTreeNode(){
        return tree.getSelectionModel().getSelectedNode();
    }
    
    function getCreateMode(){
        return $("input[name=createMode]").filter(":checked").val();    
    }
    
    function closeWindow(){
        window.top.closeWindow('modifyPollObjectWin'); 
    }
    
    function showPanel(id){
        var panels=['panel_area','panel_olt','panel_cc', 'panel_excel'];
        for(var i=0;i<panels.length;i++){
            if(id==panels[i]){
                $("#"+id).show(); 
            }else{
                $("#"+panels[i]).hide(); 
            }
        }
    }
    
    function updateByArea(){
        update($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_AREA%>',[getSelectedTreeNode().id]);
    }
    
    function updateByOlt(){
        var rs=oltSm.getSelections();
        var ids=[];
        for(var i=0;i<rs.length;i++){
            ids[i]=rs[i].data.deviceObjId;
        }
        update($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_OLT%>',ids);
    }
    
    function updateByCC(){
        var rs=ccSm.getSelections();
        var ids=[];
        for(var i=0;i<rs.length;i++){
            ids[i]=rs[i].data.deviceObjId;
        }
        update($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_CCMTS%>',ids);
    }
    
    // 监控对象名是否已存在
    function isNameExist(objName){
        var ret=false;
        $.ajax({
            url: '/cmpoll/isNameExist.tv',
            type: 'post',
            async:false,
            dataType:"json",
            cache: false, 
            data:{
                'pollObjName':objName
            },
            success: function(response) {
                if(response.success&&response.isExist){
                    ret=true;
                }
            }
        });
        return ret;
    }
 
    function update(objName,objType,ids){
        if(oldPollObjName!=objName && isNameExist(objName)){
            window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollObjectExist);
            return;
        }
        $.ajax({
            url: '/cmpoll/updatePollObjectInfo.tv',
            type: 'post',
            dataType:"json",
            data:{
                'objectId':pollObj.objectId,
                'objectName':objName,
                'objectType':objType,
                'deviceIds':ids.join("$")
            },
            cache: false, 
            success: function(response) {
                if(response.result == "true"){
                	top.afterSaveOrDelete({
                		 title: '@COMMON.tip@',        
                		 html: '<b class="orangeTxt">@resources/COMMON.modifySuccess@</b>'    
                	});
                }else{
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
                }
                closeWindow();
            }, 
            error: function(response) {
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
                closeWindow();
            }
        });
    }
    
    function updateByCm(){
        var rs=cmSm.getSelections();
        var ids=[];
        for(var i=0;i<rs.length;i++){
            ids[i]=rs[i].data.deviceObjId;
        }
        update($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_EXCEL%>',ids);
    }
    
    function initAreaTree(){
        treeLoader = new Ext.tree.TreeLoader({dataUrl:'/cmpoll/loadTopoFolder.tv?hasSubnet=true'});
        tree = new Ext.tree.TreePanel({
            el:'topoTree', 
            border:false,
            trackMouseOver:false, 
            lines:true, 
            rootVisible:false, 
            enableDD:false,
            loader:treeLoader 
        });
        if(pollObj.objectType=='<%=CmPollObject.OBJECT_TYPE_AREA%>'){
            tree.on('load', function(node) {
                var ids=pollObj.deviceIds;
                for(var i=0;i<ids.length;i++){
                    if(node.id==ids[i]){
                        node.select();
                        break;
                    }
                }
            });
        }
        var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: 'source'});
        tree.setRootNode(root);
        tree.render();
        root.expand();
    }
    
    function initOltGrid(){
        oltSm = new Ext.grid.CheckboxSelectionModel({
            
        }); 
        var oltStore = new Ext.data.JsonStore({
            url: ('/cmpoll/loadDeviceList.tv?objMode=olt'),
            root: 'data',
            fields: ['deviceObjId','deviceName','deviceType','deviceTypeToString']
        });
        
        if(pollObj.objectType=='<%=CmPollObject.OBJECT_TYPE_OLT%>'){
            oltStore.on('load', function(store,records) {
                var ids=pollObj.deviceIds;
                var selRecords=[];
                for(var i=0;i<ids.length;i++){
                    var id=ids[i];
                    for(var j=0;j<records.length;j++){
                        if(id==records[j].data.deviceObjId){
                            selRecords.push(records[j]);
                            break;
                        }
                    }
                }
                oltSm.selectRecords(selRecords);
            });
        }
        
        var oltCm = new Ext.grid.ColumnModel([ 
                                           oltSm,
                                           {
                                               header: I18N.Config.oltConfigFileImported.deviceName, 
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceName'
                                           },
                                           {
                                               header: I18N.MAIN.templateRootNode,  
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceTypeToString'
                                           }
                                           ]);
        new Ext.grid.GridPanel({
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',
            store:oltStore, 
            sm:oltSm,
            cm:oltCm,
            height:185, viewConfig : {forceFit: true},
            renderTo:'panel_olt'
        });
        oltStore.load();
    }
    
    function initCCGrid(){
        ccSm = new Ext.grid.CheckboxSelectionModel({
            
        }); 
        var ccStore = new Ext.data.JsonStore({
            url: ('/cmpoll/loadDeviceList.tv?objMode=cc'),
            root: 'data',
            fields: ['deviceObjId','deviceName','deviceType','deviceTypeToString']
        });
        
        if(pollObj.objectType=='<%=CmPollObject.OBJECT_TYPE_CCMTS%>'){
            ccStore.on('load', function(store,records) {
                var ids=pollObj.deviceIds;
                var selRecords=[];
                for(var i=0;i<ids.length;i++){
                    var id=ids[i];
                    for(var j=0;j<records.length;j++){
                        if(id==records[j].data.deviceObjId){
                            selRecords.push(records[j]);
                            break;
                        }
                    }
                }
                ccSm.selectRecords(selRecords);
            });
        }
        
        var ccCm = new Ext.grid.ColumnModel([ 
                                           ccSm,
                                           {
                                               header: I18N.Config.oltConfigFileImported.deviceName, 
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceName'
                                           },
                                           {
                                               header: I18N.MAIN.templateRootNode, 
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceTypeToString'
                                           }
                                           ]);
        new Ext.grid.GridPanel({
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',forceLayout :true,
            store:ccStore, 
            sm:ccSm,
            cm:ccCm,
            height:185,viewConfig : {forceFit: true},
            renderTo:'panel_cc'
        });
        ccStore.load();
    }
    
    function initCmGrid(){
        cmSm = new Ext.grid.CheckboxSelectionModel({
        }); 
        var objectId = ${objectId}
        var cmStore = new Ext.data.JsonStore({
            url: ('/cmpoll/loadDeviceList.tv?objMode=excel&objectId=' + objectId),
            root: 'data',
            fields: ['deviceObjId','deviceName','deviceTypeToString']
        });
        
        if(pollObj.objectType=='<%=CmPollObject.OBJECT_TYPE_EXCEL%>'){
            cmStore.on('load', function(store,records) {
                var ids=pollObj.deviceIds;
                var selRecords=[];
                for(var i=0;i<ids.length;i++){
                    var id=ids[i];
                    for(var j=0;j<records.length;j++){
                        if(id==records[j].data.deviceObjId){
                            selRecords.push(records[j]);
                            break;
                        }
                    }
                }
                cmSm.selectRecords(selRecords);
            });
        }
        
        var cmCm = new Ext.grid.ColumnModel([ 
                                           cmSm,
                                           {
                                               header: "MAC", 
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceName'
                                           },
                                           {
                                               header: "IP", 
                                               sortable: false, 
                                               align: 'center', 
                                               width:130,
                                               dataIndex: 'deviceTypeToString'
                                           }
                                           ]);
        new Ext.grid.GridPanel({
            store:cmStore, 
            sm:cmSm,
            cm:cmCm,
            height:185,
            renderTo:'panel_excel'
        });
        cmStore.load();
    }
    
    // ---------------------------------------------------------------------------
    loadPollObject();
    initAreaTree();
    initOltGrid();
    initCCGrid();
    initCmGrid();
    
    $("#btnSubmit").click(function(){
        var objName=$.trim($("#objName").val());
        if(!objName){
            //window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseEnterObjectName);
        	$("input#objName").focus();
        }else{
        	//监控对象名最长为32字节
        	if(objName.length>32){
        		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollObjectNameNotLongerThan32);
        		$("input#objName").focus();
        		return false;
        	}
            var m=getCreateMode();
            if(m=="panel_area"){
                if(getSelectedTreeNode()==null){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseRegion);
                }else{
                    updateByArea();
                }
            }else if(m=='panel_olt'){
                if(oltSm.getSelections().length==0){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseOlt);
                }else{
                    updateByOlt();
                }
            }else if(m=='panel_cc'){
                if(ccSm.getSelections().length==0){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseCcmts);
                }else{
                    updateByCC();
                }
            }else if(m=='panel_excel'){
                if(cmSm.getSelections().length==0){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseCm);
                }else{
                    updateByCm();
                }
            }
        }
    });
    
    $('#btnCancel').click(function(){
        closeWindow();
    });
    
    $("input[name=createMode]").click(function(){
        showPanel(this.value);    
    });
    
    $("#objName").focus();
});

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});

function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}
</script>
</head>
	<body class="openWinBody">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@cmPoll.modifyPollObject@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td width="100" class="rightBlueTxt">@cmPoll.pollObjectName@:</td>
						<td><input type="text" id="objName" class="normalInput" tooltip='<fmt:message bundle='${cm}' key='cmPoll.pollObjectNameNotLongerThan32'/>'/></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@cmPoll.addStyle@:</td>
						<td><label><input type="radio" name="createMode" value="panel_area" />@COMMON.region@</label> 
						<% if(uc.hasSupportModule("olt")){%>
							<label><input type="radio" name="createMode" value="panel_olt" /> OLT</label> 
						<%}%>
							<label><input type="radio" name="createMode" value="panel_cc" /> CMTS</label>
							<label><input type="radio" name="createMode" value="panel_excel"> EXCEL</label></td>
					</tr>
				</table>
				<table width="100%" cellspacing="3" cellpadding="3">
					<tr>
						<td>
							<div id="panel_area" style="display: none; height: 185px;"
								class="TREE-CONTAINER">
								<div id="topoTree" style="height: 100%; overflow: auto;"></div>
							</div>
							<div id="panel_olt" style="display: none;"></div>
							<div id="panel_cc" style="display: none;"></div>
							<div id="panel_excel" style="display: none;"></div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 第三部分，按钮组合 -->
		<Zeta:ButtonGroup>
			<Zeta:Button id="btnSubmit" icon="miniIcoEdit">@COMMON.modify@</Zeta:Button>
			<Zeta:Button id="btnCancel" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>