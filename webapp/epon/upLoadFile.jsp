<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    LIBRARY FileUpload
    MODULE epon
    CSS js/webupload/webuploader
    IMPORT js/webupload/webuploader.min
    IMPORT epon/uploadFile
</Zeta:Loader>
<script type="text/javascript" src="js/progressBarScript.js"></script>
<title>@SERVICE.fileMgmt@</title>
<style type=text/css>
#submit_btn {
	width: 80px;height: 50px;background: url(../image/power.png) no-repeat center;
	cursor: pointer;display: block;font-size: 0;line-height: 0;text-indent: -9999px;
}
.pbar {position:absolute;left:15px;top:120px;width:270px;height:22px}
.percent {position:absolute;left:150px;top:124px;z-index:1000000;}
.upload{ width:414px;}
.firstLi{float:left;}
.lastLi{ float:right;}
</style>
<script type="text/javascript">
var fp = null ;
var data = ${filePath};
var fileNameArray = '${fileNameArray}';
var entityId = '${entityId}';
var filePathStore =  new Ext.data.SimpleStore({
    fields : ['value','field'],
    data : data
});
var fileSize = 0;
var chooseFileName = EMPTY;
var chooseFilePath= EMPTY;

function upload(obj){
    window._fileName = $('#destFileName').val();
    chooseFilePath =   Ext.getCmp('fileCombo').getValue();
    if (chooseFilePath) {
        //if($('.ui-progressbar-value')){$('.ui-progressbar-value').css('width', '0%')}
    	if(!/^[a-zA-Z0-9.()_-]{1,31}$/.test(_fileName)){
    		window.top.showMessageDlg(I18N.COMMON.tip, "@SERVICE.destFileTip@" ,null);
    	}else if (!chooseFileName.length>0){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.plsSelectFileFirst ,null)
		}else if(fileSize > 50 * 1024 * 1024) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.fileSizeBig ,null)
        }else if(fileSize == 0){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.fileSize0 ,null)
        }else if(fileNameArray.indexOf(( chooseFilePath + _fileName))!=-1){
	      	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.fileOverName , function(type) {
	            if(type=='yes'){
                    R.uploadBT.setDisabled( true ).setText( "@SERVICE.uploading@" );
	                thisFlash.upload("/epon/upLoadFileToFile.tv?entityId="+entityId+"&filePath="+chooseFilePath+"&destFileName="+ _fileName);
                    show(entityId,chooseFilePath, $('#destFileName').val(),fileSize);
                }
	    	})
        }else{
        	R.uploadBT.setDisabled( true ).setText( "@SERVICE.uploading@" );
            thisFlash.upload("/epon/upLoadFileToFile.tv?entityId="+entityId+"&filePath="+chooseFilePath+"&destFileName="+ _fileName);
            show(entityId,chooseFilePath, _fileName ,fileSize);
        }
    }else{
    	window.parent.showMessageDlg("@COMMON.tip@","@SERVICE.plsChooseFilePath@");
    }
}

Ext.onReady(function () {
	Ext.QuickTips.init();
 	//初始化信息提示功能
    new Ext.form.ComboBox({
        id: 'fileCombo',
        store :  filePathStore,
        valueField : "value",   
        displayField : "field",   
        forceSelection : true,   
        blankText : "@SERVICE.plsSelectPath@" ,
        emptyText : "@SERVICE.plsSelectPath@" ,    
        editable : false,   
        mode:'local',
        triggerAction : 'all',   
        allowBlank : false,       
        id:"fileCombo",
        width : 200,
        renderTo:"uploadPath"
    });
    //构造FLASH
    /* window.thisFlash = new TopvisionUpload("chooseFile");
    
    thisFlash.onSelect = function (obj){
        fileSize = obj.size;
        chooseFileName = obj.name;
        $("#fileName").val(chooseFileName);
        $('#destFileName').attr("disabled",false);
        $('#destFileName').val(chooseFileName);
    }
    
    thisFlash.onComplete = function (result){
    	result = Ext.util.JSON.decode(result);
    	result = result['result'];
        $("#destFileName").val(window._fileName)
        window.tranparent = false;
        if(result=="success"){
	        animateProgressEnd();
	        R.uploadBT.setDisabled( false ).setText( "@SERVICE.upload@" );
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.uploadOk ,null,function(){
               window.parent.getWindow("fileManage").body.dom.firstChild.contentWindow.store.load();
               window.parent.closeWindow('uploadfile');
            })
            return;
        }
        animateProgressError();
        //$('#destFileName').regex = /^[a-zA-Z0-9.()_-]+$/;
       	R.uploadBT.setDisabled( false ).setText( "@SERVICE.upload@" );
        var $tooltip = $("#uploadTooltip");
        if(result=="inProgress"){
        	$tooltip.html("@SERVICE.uploadBusy@");
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadBusy@" ,'error');
        }else if(result=="fileNotExists"){
        	$tooltip.html("@SERVICE.uploadAg@");
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadAg@" ,'error');
        }else if(result=="fileNoTime"){
        	$tooltip.html("@SERVICE.uploadSlow@");
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadSlow@" ,'info');
        }else if(result == "fileNoResponse"){
        	$tooltip.html("@SERVICE.uploadTimeout@");
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadTimeout@" ,'info');
        }else{
        	$tooltip.html("@SERVICE.uploadEr@");
            window.parent.showMessageDlg(I18N.COMMON.tip, '@SERVICE.uploadEr@','error');
        }   
        chooseFile = result;
    } */
});    

function cancelclick(){
	if(window.tranparent){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.uploadTip);
		return;
	}
	window.parent.closeWindow('uploadfile');
}

</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader borderBorderNone">
		<div class="openWinTip" id="uploadTooltip">@SERVICE.uploadFile@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="uploadBar">
		<div class="uploadBarInner">
			<div class="uploaderBarInnerRight"></div>
		</div>
	</div>

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows">
				<tr class="darkZebraTr">
					<td class="rightBlueTxt w200">@SERVICE.uploadPath@</td>
					<td><div id="uploadPath" class="w200"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@SERVICE.savingName@</td>
					<td><input type="text" id="destFileName" class="normalInput w200" tooltip="@SERVICE.destFileTip@" maxLength="31", minLength="1"/></td>
				</tr>
				<%-- <tr class="darkZebraTr">
					<td class="rightBlueTxt">@SERVICE.selectFile@:</td>
					<td><input type="text" style="width:143px;" id="fileName" class="normalInputDisabled floatLeft" value='@SERVICE.fileSize35@' disabled />
						<a  id="chooseFile" href="javascript:;" class="nearInputBtn"><span>@COMMON.browse@...</span></a></td>
				</tr> --%>
				<tr>
					<td class="rightBlueTxt">@SERVICE.selectFile@:</td>
					<td>
						<input type="text" style="width:143px;" id="fileNameInput" class="normalInputDisabled floatLeft" value='@SERVICE.fileSize35@' disabled />
						<div class="btns">
					        <div id="picker">@COMMON.browse@...</div>
					    </div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<Zeta:ButtonGroup>
		<li><a href="javascript:;" class="normalBtnBig" onclick="startUpload()"><span><i class="miniIcoArrUp"></i>@COMMON.upload@</span></a></li>
		<%-- <Zeta:Button id="uploadBT" onClick="upload()" icon="miniIcoArrUp">@COMMON.upload@</Zeta:Button> --%>
		<Zeta:Button id="closeButton" onClick="cancelclick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>