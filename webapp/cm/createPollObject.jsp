<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.ems.cm.domain.CmPollObject"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<fmt:setBundle basename="com.topvision.ems.cm.resources" var="cm"/>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    PLUGIN  LovCombo
    LIBRARY FileUpload
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
var objectId = '${objectId}';


function getCreateMode(){
    return $("input[name=createMode]").filter(":checked").val();    
}

$(function(){
    var tree,treeLoader;
    var oltSm,ccSm;
    var flash;
    // --------------------------- Function Define -------------------------------
    function getSelectedTreeNode(){
        return tree.getSelectionModel().getSelectedNode();
    }
    
	$("#downLoadFile").click(function(){
    	window.location.href="/cmpoll/downLoadCmImportTemplate.tv";
	});
    
    function closeWindow(){
        window.top.closeWindow('createPollObjectWin');  
    }
    
    function showPanel(id){
        var panels=['panel_area','panel_olt','panel_cc','panel_excel'];
        var grid = Ext.getCmp(id+"_grid");
        if(grid){
            setTimeout(function(){
	            grid.getView().refresh();
            },100)
        }
        for(var i=0;i<panels.length;i++){
            if(id==panels[i]){
                $("#"+id).show(); 
            }else{
                $("#"+panels[i]).hide(); 
            }
        }
        $("#chooseFileFileUpload").css({display:"none"});
        if( id == "panel_excel" ){
        	$("#chooseFileFileUpload").css({display:""});
        } 
    }
    
    function createByArea(){
        create($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_AREA%>',[getSelectedTreeNode().id]);
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
    
    function createByOlt(){
        var rs=oltSm.getSelections();
        var ids=[];
        for(var i=0;i<rs.length;i++){
            ids[i]=rs[i].data.deviceObjId;
        }
        create($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_OLT%>',ids);
    }
    
    function createByCC(){
        var rs=ccSm.getSelections();
        var ids=[];
        for(var i=0;i<rs.length;i++){
            ids[i]=rs[i].data.deviceObjId;
        }
        create($.trim($("#objName").val()),'<%=CmPollObject.OBJECT_TYPE_CCMTS%>',ids);
    }
    
    function createByExcel(){
        var filePath = $.trim($("#filePath").val());
        if(filePath != ''){
           var objName = $.trim($("#objName").val());
           if(isNameExist(objName)){
               window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollObjectExist);
               return;
           }
           Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + I18N.cmPoll.readFile);
           var url = "/cmpoll/scanCmImportInfos.tv";
           var data = {
        		   objectName : objName
           };
           flash.upload(url,data); 
        }
    }
    
    function create(objName,objType,ids){
        if(isNameExist(objName)){
            window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pollObjectExist);
            return;
        }
        $.ajax({
            url: '/cmpoll/createPollObjectInfo.tv',
            type: 'GET',
            dataType:"json",
            data:{
                'objectName' : objName,
                'deviceIds' : ids.join("$"),
                'objectType' : objType
            },
            cache: false, 
            success: function(response) {
                if(response.result == "true"){
                }else{
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.addFail);
                }
               closeWindow();
            }, 
            error: function(response) {
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.addFail);
                closeWindow();
            }
        });
    }
    
    function initAreaTree(){
        treeLoader = new Ext.tree.TreeLoader({dataUrl:'/cmpoll/loadTopoFolder.tv?hasSubnet=true'});
        treeLoader.on('load', function() {
            //tree.getRootNode().select();
        });
        tree = new Ext.tree.TreePanel({
            el:'topoTree', 
            border:false,
            trackMouseOver:false, 
            lines:true, 
            rootVisible:false, 
            enableDD:false,
            loader:treeLoader 
        });
        var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: 'source'});
        tree.setRootNode(root);
        tree.render();
        root.expand();
    }
    
    function initOltGrid(){
        oltSm = new Ext.grid.CheckboxSelectionModel({}); 
        var oltStore = new Ext.data.JsonStore({
            url: ('/cmpoll/loadDeviceList.tv?objMode=olt'),
            root: 'data',
            fields: ['deviceObjId','deviceName','deviceType','deviceTypeToString']
        });
        
        var oltCm = new Ext.grid.ColumnModel([ oltSm,
			{header: "@resources/Config.oltConfigFileImported.deviceName@", sortable: false, align: 'center', width:130,dataIndex: 'deviceName'},
			{header: "@resources/MAIN.templateRootNode@",  sortable: false,align: 'center',width:130,dataIndex: 'deviceTypeToString'}
		]);
        new Ext.grid.GridPanel({
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',
            store:oltStore, sm:oltSm,cm:oltCm,
            id : "panel_olt_grid",forceLayout :true,
            renderTo:'panel_olt',
            viewConfig : {forceFit: true}
        });
        oltStore.load();
    }
    
    function initCCGrid(){
        ccSm = new Ext.grid.CheckboxSelectionModel({}); 
        var ccStore = new Ext.data.JsonStore({
            url: ('/cmpoll/loadDeviceList.tv?objMode=cc'),
            root: 'data',
            fields: ['deviceObjId','deviceName','deviceType','deviceTypeToString']
        });
        
        var ccCm = new Ext.grid.ColumnModel([ ccSm,
			{header: I18N.Config.oltConfigFileImported.deviceName, sortable: false, align: 'center', dataIndex: 'deviceName'},
			{header: I18N.MAIN.templateRootNode,  sortable: false, align: 'center', dataIndex: 'deviceTypeToString'}
		]);
        new Ext.grid.GridPanel({
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',forceLayout :true,
            store:ccStore, sm:ccSm,cm:ccCm,renderTo:'panel_cc',id:"panel_cc_grid",
            viewConfig : {forceFit: true}
        });
        ccStore.load();
    }
    
    function initExcelUpload(){
        flash = new TopvisionUpload("chooseFile");
        flash.onSelect = function(obj){
            var fileSize = obj.size;
            var chooseFileName = obj.name;
            if(fileSize > 100*1024*1024){
                window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.importFileTooBig);
                return;
            }else{
                $("#filePath").val(chooseFileName);
            }
        };
        flash.onComplete =function(result){
            Ext.getBody().unmask();
            result = Ext.util.JSON.decode(result);
            if(result.message == "FileWrong"){
                window.top.showMessageDlg(I18N.COMMON.tip,I18N.cmPoll.fileError );
            }else if(result.message == "macWrong"){
            	var tip = I18N.cmPoll.macError + ':' + result.wrongMac;
            	window.top.showMessageDlg(I18N.COMMON.tip, tip);
            }else if(result.message == "success"){
            	var successTip = I18N.cmPoll.importCmSuccess + ":" + result.number ;
                window.top.showMessageDlg(I18N.COMMON.tip, successTip);
                closeWindow();
            }else{
            	window.top.showMessageDlg(I18N.COMMON.tip,I18N.cmPoll.importError);
            }
        };
    }
    
    // ---------------------------------------------------------------------------
    initAreaTree();
    initOltGrid();
    initCCGrid();
    initExcelUpload();
    
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
                    createByArea();
                }
            }else if(m=='panel_olt'){
                if(oltSm.getSelections().length==0){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseOlt);
                }else{
                    createByOlt();
                }
            }else if(m=='panel_cc'){
                if(ccSm.getSelections().length==0){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseCcmts);
                }else{
                    createByCC();
                }
            }else if(m=='panel_excel'){
                if($.trim($("#filePath").val())==""){
                    window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseExcelFile);
                }else{
                    createByExcel();
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
    showPanel("panel_area");
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
			<div class="openWinTip">@cmPoll.newPollObject@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
						<tr>
							<td width="100" class="rightBlueTxt">@cmPoll.pollObjectName@:</td>
							<td><input type="text" id="objName" class="normalInput" tooltip='<fmt:message bundle='${cm}' key='cmPoll.pollObjectNameNotLongerThan32'/>'></td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@cmPoll.addStyle@:</td>
							<td>
								<label><input type="radio" name="createMode"
									value="panel_area" checked="checked"> @COMMON.region@</label> 
								<% if(uc.hasSupportModule("olt")){%>
								<label><input
									type="radio" name="createMode" value="panel_olt"> OLT</label>
								<%}%> 
								<label><input
									type="radio" name="createMode" value="panel_cc"> CMTS</label> <label><input
									type="radio" name="createMode" value="panel_excel">Excel</label></td>
						</tr>
					</table>
					<table width="100%" cellspacing="3" cellpadding="3">
						<tr>
							<td>
								<div id="panel_area" style="display: none; height: 185px;"
									class="TREE-CONTAINER">
									<div id="topoTree" style="height: 100%; overflow: auto;"></div>
								</div>
								<div id="panel_olt" style="display: none; overflow: auto; height: 185px;"></div>
								<div id="panel_cc"  style="display: none; overflow: auto; height: 185px;"></div>
								<div id="panel_excel" style="height: 185px;">
									<input type="text" style="width: 150px;" disabled class="normalInput" id="filePath">&nbsp;
									<button id="chooseFile" class="BUTTON75">@cmPoll.chooseExcelFile@</button>
									<button id="downLoadFile" onclick="">@cmPoll.templateDownLoad@</button>
									<div class="yellowTip">
										<b class="orangeTxt">@cmPoll.templateImportTipsTitle@</b>
										<p class="pT10">@cmPoll.templateImportTipsLine0@</p>
										<p class="pT10">@cmPoll.templateImportTipsLine1@</p>
										<p>@cmPoll.templateImportTipsLine2@</p>
										<p>@cmPoll.templateImportTipsLine3@</p>
										<p>@cmPoll.templateImportTipsLine4@</p>
										<p>@cmPoll.templateImportTipsLine5@</p>
										<p>@cmPoll.templateImportTipsLine6@</p>
									</div>
								</div>
							</td>
						</tr>
					</table>
			</form>
		</div>

		<!-- 第三部分，按钮组合 -->
		<Zeta:ButtonGroup>
			<Zeta:Button id="btnSubmit" icon="miniIcoAdd" onClick="">@COMMON.create@</Zeta:Button>
			<Zeta:Button id="btnCancel" icon="miniIcoForbid" onClick="">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
