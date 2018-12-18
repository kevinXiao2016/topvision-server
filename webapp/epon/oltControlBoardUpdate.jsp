<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library FileUpload
    module epon
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<script type="text/javascript" src="js/progressBarScript.js"></script>
<script type="text/javascript">
var fp = null ;
var entityId = '${entityId}';
var boardId = '${boardId}'; 
var deviceType = '${oltAttribute.oltType}';
var updateType = null;
var fileSize = 0;
var chooseFileName = "";
var chooseFilePath = "";
var softType = initSoftType(deviceType);

function initSoftType(deviceType){
	var softType = null;
	if(deviceType == 'PN8602'){
		softType = [
		    [ 'mpu',  'mpu'],
		    [ 'epu',  'epu'],
		    [ 'gpu',  'gpu'],
		    [ 'bootrom','bootrom']
		];
	}else if(deviceType == 'PN8602-E'){
		softType = [
    	    [ 'meu',  'meu'],
    	    [ 'bootrom','bootrom']
		];
	} else if(deviceType == 'PN8602-EF'){
		softType = [
    	    [ 'mef',  'mef'],
    	    [ 'bootrom','bootrom']
		];
	} else if(deviceType == 'PN8602-G'){
		softType = [
		    	    [ 'mgu',  'mgu'],
		    	    [ 'bootrom','bootrom']
				];
	}else{
		softType = [
		    [ 'mpu',  'mpu'],
		    [ 'mpub',  'mpub'],
		    [ 'geu',  'geu'],
		    [ 'xgu',  'xgu'],
		    [ 'epu',  'epu'],
		    [ 'gpu',  'gpu'],
		    [ 'bootrom','bootrom'],
		    [ 'bootrom_mpub','bootrom_mpub']
		];
	}
	return softType;
}

var upSoftType = new Ext.data.SimpleStore({
    data : softType,
    fields : ['value','field']
}); 

function upload() {
	$("#uploadTooltip").html("@SERVICE.uploadBinFile@");
    updateType = Ext.getCmp('upType').getValue();
    var mainType = $('#mainCheck').attr("checked");
    var backType = $('#bakcheck').attr("checked");
    var tempType = chooseFileName.split('.');
    if(!(mainType || backType)){
    	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.plsSelectUpgradType@" , null);
        return ;
    }
    if(updateType != "mpub" && updateType != "epu" && updateType != "mpu" && updateType != "geu" && updateType != "xgu" && updateType != "bootrom" 
		   && updateType != "bootrom_mpub" && updateType != "meu" && updateType != "gpu" && updateType != "mef"&& updateType != "mgu"){
        window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.plsSelectType@" , null);
        return ;
    } 
    if (!chooseFileName.length > 0) {
        window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.plsSelectFile@" , null);
        return ;
    }
    if (tempType[1] != 'bin'  ) {
        window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.fileMustBeBin@" , null)
    }else {
        if (fileSize > 50 * 1024 * 1024) {
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.fileSizeBig@" , null)
        }
        else if (fileSize == 0) {
            window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.fileSize0@" , null)
        }
        else {
        	var fileDir = getUploadDir(entityId,updateType);
			R.uploadBT.setDisabled(true).setText( "@SERVICE.uploading@" );
            if(mainType && backType){
            	 window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.uploadMessageTip, function(type) {
            		 if(type == 'no'){ 
            			R.uploadBT.setDisabled(false).setText( "@COMMON.upload@" );	 
            			return; 
            		 }
            		 flash.upload("/epon/oltUpdate.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName);
                	 show(entityId,"/" + fileDir + "/app/",updateType+'.bin',fileSize);
            	 })
             }else if(mainType && !backType){
        		 flash.upload("/epon/oltUpdate.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName);
            	 show(entityId,"/" + fileDir + "/app/",updateType+'.bin',fileSize);
             }else{
            	 flash.upload("/epon/oltUpdateBack.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName+"&backNeedUpload=" + true);
            	 show(entityId,"/" + fileDir + "/appbak/",updateType+'.bin',fileSize);
             }
        }
    }
}

Ext.onReady(function () {
    Ext.QuickTips.init();
    new Ext.form.ComboBox({
        width : 200,
   	 	id:'upType',
   	 	renderTo:"uptypeContainer",
   	 	fieldLabel: I18N.SERVICE.uploadType ,
        store : upSoftType,
        emptyText: I18N.COMMON.select ,
        mode:'local',
        triggerAction:'all',
        valueField:'value',
        displayField:'field',
        //readOnly:true,
        anchor:'55%'
   });
    newCreateUpload();
    
    /* window.flash = new TopvisionUpload("chooseFile");
    flash.onSelect =  function(obj) {
        fileSize = obj.size;
        chooseFileName = obj.name;
        $("#fileName").val(chooseFileName);
    };
    flash.onComplete = function(result) {
    	var $uploadStatus = result == "success";
    	R.uploadBT.setDisabled( false ).setText( "@SERVICE.upload@" );
        var mainType = $('#mainCheck').attr("checked");
        var backType = $('#bakcheck').attr("checked");
        window.tranparent = false;
        if(window.parent.progressTimer){
        	window.parent.stopProgress();
        }
        //如果选择了上传到主用主控
        if( mainType ){
        	if ( $uploadStatus ) {
	        	//修改 mainCheck的checked状态
        		animateProgressEnd();
	        	//如果只选择了上传到备用主控，则需要将这个文件同时下发到备用主控板上，此时不需要再次上传bin文件
                if ( backType ){
                	var fileDir = getUploadDir(entityId,updateType);
                	animateProgress(0);
                    R.uploadBT.setDisabled(true).setText( "@SERVICE.uploading@" );
                    show( entityId, "/"+ fileDir +"/appbak/", updateType+'.bin', fileSize );
                    $("#uploadTooltip").html("@SERVICE.uploadResultWait@");
                    $.ajax({
                        url: '/epon/oltUpdateBack.tv', timeout: 600000, cache: false,
                        data: {entityId : entityId, oltUpdateType : updateType, chooseFileName : chooseFileName},
                        success: function(json) {
                        	window.tranparent = false;
                        	R.uploadBT.setDisabled( false ).setText( "@SERVICE.upload@" );
                            if ( json.result == 'success' ) { //主用和备用都上传成功
       	            		 	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadMessage@" );
	       	            		cancelclick();
                            }else{
                            	showUploadMessage("@SERVICE.uploadResult@")
                            	animateProgressError();
                            }
                        }, error: function() {
                        	showUploadMessage("@SERVICE.uploadBackUpEr@");
                        	animateProgressError();
                        }
                    });
                    return;
                } else {
               	 	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadMainFileSuccess@" );
               	 	return cancelclick();
                }
        	}
        }
        //如果只上传了备用主控,则提示上传备用主控成功
        if( !mainType && backType ){
            if ( $uploadStatus ) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadBackFileSuccess@");
	        	return cancelclick();
           }
        }
   		animateProgressError();
        if (result == "inProgress") {
        	 showUploadMessage( "@SERVICE.uploadBusy@" );
        } else if (result == "fileNotExists") {
        	showUploadMessage( "@SERVICE.uploadAg@" );
        } else if (result == "fileNoTime"){
        	showUploadMessage( "@SERVICE.uploadSlow@" );
        } else if (result == "bakFailure") {
        	showUploadMessage( "@SERVICE.uploadBackUpEr@" );
        }else if(result == 'WRONG_TYPE'){
        	showUploadMessage( "@SERVICE.uploadFileTypeErr@" );
        } else {
        	showUploadMessage( "@SERVICE.uploadEr@" );
        }
    } */
});

function showUploadMessage( message ){
	$("#uploadTooltip").html( "<div class='redTxt'>" + message + "</div>" );
    window.parent.showMessageDlg(I18N.COMMON.tip, message );
}

function cancelclick(){
	if(window.tranparent){
		window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadingNotClose@" )
		return
	}
	window.parent.closeWindow('boardSoftwareUpdate')
}
function checkupbox(){
	if($(this).attr("checked")){
		window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.save2Backup@" )
	}
}

function getUploadDir(entityId,updateType){
	//上传文件之前获取
	var fileDir = "tffs0";
	$.ajax({
	        url: '/epon/getFileDir.tv',
	        type: 'POST',
	        data: "entityId=" + entityId + "&oltUpdateType=" + updateType,
	        dataType:"text",
	        success: function(text) {
	        	fileDir = text;
	        }, error: function(text) {
	    }, cache: false
	});
	return fileDir;
}

//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '/js/webupload/Uploader.swf',
	    server: '/epon/oltUpdateBack.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    accept: {
	    	extensions: 'bin'
	    },
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 50*1024*1024,
	    auto: false,
	    timeout: 0 /* ,
	    runtimeOrder :'flash' */  
	});
	uploader.on( 'beforeFileQueued', function( file ) {
		//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
		uploader.reset();
		//console.log('beforeFileQueued')
	});
	//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
	uploader.on( 'fileQueued', function( file ) {
		//上传框中传入文件名+后缀;
		if(file && file.name){
			$('#fileName').val(file.name);
			chooseFileName = file.name; 
			fileSize = file.size;
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, json) {
		$('#fileName').val('');
		clearMask();
		var $uploadStatus = json.result == "success";
        var mainType = $('#mainCheck').attr("checked");
        var backType = $('#bakcheck').attr("checked");
        window.tranparent = false;
        if(window.parent.progressTimer){
        	window.parent.stopProgress();
        }
        //如果选择了上传到主用主控
        if( mainType ){
        	if ( $uploadStatus ) {
	        	//修改 mainCheck的checked状态
        		animateProgressEnd();
	        	//如果只选择了上传到备用主控，则需要将这个文件同时下发到备用主控板上，此时不需要再次上传bin文件
                if ( backType ){
                	showBodyMask('@SERVICE.upload@');
                	var fileDir = getUploadDir(entityId,updateType);
                	animateProgress(0);
                    show( entityId, "/"+ fileDir +"/appbak/", updateType+'.bin', fileSize );
                    $("#uploadTooltip").html("@SERVICE.uploadResultWait@");
                    $.ajax({
                        url: '/epon/oltUpdateBack.tv', timeout: 600000, cache: false,
                        data: {entityId : entityId, oltUpdateType : updateType, chooseFileName : chooseFileName,backNeedUpload:false},
                        success: function(json) {
                        	window.tranparent = false;
                        	clearMask();
                            if ( json.result == 'success' ) { //主用和备用都上传成功
       	            		 	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadMessage@" );
	       	            		cancelclick();
                            }else{
                            	showUploadMessage("@SERVICE.uploadResult@")
                            	animateProgressError();
                            }
                        }, error: function() {
                        	clearMask();
                        	showUploadMessage("@SERVICE.uploadBackUpEr@");
                        	animateProgressError();
                        }
                    });
                    return;
                } else {
               	 	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadMainFileSuccess@" );
               	 	return cancelclick();
                }
        	}
        }
        //如果只上传了备用主控,则提示上传备用主控成功
        if( !mainType && backType ){
            if ( $uploadStatus ) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.uploadBackFileSuccess@");
	        	return cancelclick();
           }
        }
   		animateProgressError();
        if (json == "inProgress") {
        	 showUploadMessage( "@SERVICE.uploadBusy@" );
        } else if (json == "fileNotExists") {
        	showUploadMessage( "@SERVICE.uploadAg@" );
        } else if (json == "fileNoTime"){
        	showUploadMessage( "@SERVICE.uploadSlow@" );
        } else if (json == "bakFailure") {
        	showUploadMessage( "@SERVICE.uploadBackUpEr@" );
        }else if(json == 'WRONG_TYPE'){
        	showUploadMessage( "@SERVICE.uploadFileTypeErr@" );
        } else {
        	showUploadMessage( "@SERVICE.uploadEr@" );
        }
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	uploader.on( 'error', function(err, file) {
		switch(err){
    		case 'Q_TYPE_DENIED':
    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fileErrType@' ,null)
				//console.log('您选择的文件类型不符合规范');
				break;
    		case 'Q_EXCEED_SIZE_LIMIT':
    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.sizeLimit@' ,null)
    			//console.log('您选择的文件大小超出了范围');	
    			break;
		}
	});
}
function startUpload(){
	$("#uploadTooltip").html("@SERVICE.uploadBinFile@");
    updateType = Ext.getCmp('upType').getValue();
    var mainType = $('#mainCheck').attr("checked");
    var backType = $('#bakcheck').attr("checked");
    if(!(mainType || backType)){
    	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.plsSelectUpgradType@" , null);
        return ;
    }
    if(updateType != "mpub" && updateType != "epu" && updateType != "mpu" && updateType != "geu" && updateType != "xgu" && updateType != "bootrom" 
		&& updateType != "bootrom_mpub" && updateType != "meu" && updateType != "gpu" &&　updateType != "mef" &&　updateType != "mgu"){
	    	window.parent.showMessageDlg(I18N.COMMON.tip, "@SERVICE.plsSelectType@" , null);
	    	return ;
	}
    var fileDir = getUploadDir(entityId,updateType);
    
    uploader.options.formData = {
    	entityId: entityId,
    	oltUpdateType: updateType,
    	chooseFileName: chooseFileName
    }
    showBodyMask('@SERVICE.upload@');
    if(mainType && backType){
	   	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.uploadMessageTip, function(type) {
	   		 if(type == 'no'){ 
	   			clearMask();
	   			return; 
	   		 }
	   		 uploader.options.server = '/epon/oltUpdate.tv';
	       	 show(entityId,"/" + fileDir + "/app/",updateType+'.bin',fileSize);
	   		 uploader.upload();
	   		 //flash.upload("/epon/oltUpdate.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName);
	   	})
    }else if(mainType && !backType){
    	 uploader.options.server = '/epon/oltUpdate.tv';
		 show(entityId,"/" + fileDir + "/app/",updateType+'.bin',fileSize);
    	 uploader.upload();
		 //flash.upload("/epon/oltUpdate.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName);
    }else{
    	uploader.options.server = '/epon/oltUpdateBack.tv?&backNeedUpload=' + true;
   	 	show(entityId,"/" + fileDir + "/appbak/",updateType+'.bin',fileSize);
    	uploader.upload();
   	 	//flash.upload("/epon/oltUpdateBack.tv?entityId=" + entityId + "&oltUpdateType="+ updateType +"&chooseFileName="+chooseFileName);
    }
}
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
</script>
</head>
<body class="openWinBody">
	<div class="formtip" id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip" id="uploadTooltip">@SERVICE.uploadBinFile@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="uploadBar">
		<div class="uploadBarInner">
			<div class="uploaderBarInnerRight"></div>
		</div>
	</div>
	
	<div class="edge10">
		<table class="mCenter zebraTableRows" >
			<tr>
				<td class="w200 rightBlueTxt">@SERVICE.chooseItem@</td>
				<td><input type="checkbox" id="mainCheck" />&nbsp;@SERVICE.upload2mainFile@
				<input type="checkbox" id="bakcheck" onclick="checkupbox()" />&nbsp;@SERVICE.upload2Backup@</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@SERVICE.uploadType@</td>
				<td><div id="uptypeContainer"></div></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@SERVICE.chooseFile@</td>
				<td><input class="w180 normalInput floatLeft" id="fileName" value="@SERVICE.fileSize35@" readonly />
				<div class="btns" style="float:left;">
              		<div id="picker">@COMMON.browse@</div>
              	</div>
			</tr>
		</table>
	</div>
	
	<Zeta:ButtonGroup>
		<li>
			<li><a href="javascript:;" class="normalBtnBig" onclick="startUpload()"><span><i class="miniIcoArrUp"></i>@COMMON.upload@</span></a></li>
		</li>
		<%-- 		<Zeta:Button id="uploadBT" onClick="upload()" icon="miniIcoArrUp">@COMMON.upload@</Zeta:Button> --%>
		<Zeta:Button onClick="cancelclick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>