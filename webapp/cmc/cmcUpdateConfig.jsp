<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<TITLE>Software Upgrading</TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.workbench.resources"
    var="workbench" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources"
    var="cmc" /> 
<fmt:setBundle basename="com.topvision.platform.zetaframework.base.resources"
    var="base" /> 
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link href="css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link rel="STYLESHEET" type="text/css"
    href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="STYLESHEET" type="text/css"
    href="../css/<%= cssStyleName %>/mytheme.css" />


    
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.platform.zetaframework.base.resources,com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/FileUpload/TopvisionUpload.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="js/progressBarScript.js"></script>
<link rel="stylesheet" href="/js/webupload/webuploader.css" />
<script type="text/javascript" src="/js/webupload/webuploader.min.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<style type=text/css>
    #submit_btn {
    	width: 80px;height: 50px;background: url(../image/power.png) no-repeat center;
    	cursor: pointer;display: block;font-size: 0;line-height: 0;text-indent: -9999px;
    }
    .pbar {
		position: absolute;
		left: 20px;
		top: 206px;
		width: 270px;
		height: 22px
	}
	
	.percent {
		position: absolute;
		left: 150px;
		top: 209px;
		z-index: 1000000;
	}
</style>
<script type="text/javascript">
var uploader = null;
var progressInterval = null;
var fp = null ;
var cmcId = '<s:property value="cmcId"/>';
var fileType = '${fileType}';
var fileSize = 0;
var chooseFileName = "";
var flash;
var tftpStatus ;
function upload(obj){
    window._fileName = $('#destFileName').val();
    if(!tftpStatus){
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.tftpServerIsClosed, null);
    }else{
        if (isValid()) {    	    
    	   //Ext.getCmp('destFileName').regex = null;
		   if($('.ui-progressbar-value')){$('.ui-progressbar-value').css('width', '0%')}
		   if (!chooseFileName.length>0){
			   window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.selectFileFirst, null); 
		   }else if(fileSize > 50 * 1024 * 1024 ) {
		  	    window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.fileSizeBiggerThan50M, null);  
		   }else if(fileSize == 0){
		      	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.fileSizeIs0, null);  
		   }else{
			   $("#uploadBT").html('<span><i class="miniIcoInport"></i><fmt:message bundle="${cmc}" key="CMC.button.uploading"/></span>').attr("disabled", true);	
		        flash.upload("/cmc/updateCmc.tv?cmcId="+cmcId+"&destFileName="+ $('#destFileName').val() + "&fileType=" + fileType);
		        show(cmcId);
		   }
        }
    }
}

Ext.onReady(function () {
	//$('#progress').children('.pbar').children('.ui-progressbar-value').css('width', '100%');
	Ext.QuickTips.init();
   /*  flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        chooseFileName = obj.name;
        if(obj.size > 100000000){
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.CMC.tip.select100MFile, obj.name));
            return;
        }else{
            $("#fileName").val(chooseFileName);
            //MODIFY BY JAY EMS-9219 【EMS-V2.3.2.1-CC8800C-B】：设备快照软件升级中，更改选择的升级文件，保存名未同步更新。
            //if($('#destFileName').val().length<1){
    			$('#destFileName').val(chooseFileName);
            //}
        }
        isValid();
    };
    flash.onComplete =function(result){
    	$("#destFileName").val(window._fileName);
    	window.tranparent = false;
		if(window.parent.progressTimer){
			window.parent.stopProgress();
		};
	    if(result=="updateSuccess"){
	    	$("#uploadBT").attr("disabled",false).html('<span><i class="miniIcoInport"></i><fmt:message bundle="${cmc}" key="CMC.button.updateDevice"/></span>');
	    	$('#progress').children('.pbar').children('.ui-progressbar-value').css('width', '100%');
	    	$('#progress').children('.percent').html(I18N.CMC.tip.uploadComplete);
	    	top.afterSaveOrDelete({
   				title: I18N.CMC.tip.tipMsg,
   				html: '<b class="orangeTxt">' + I18N.CMC.tip.uploadSuccessResetToUpdate + '</b>'
   			});
	    	cancelclick();
	       	return;
	    }
	    //Ext.getCmp('destFileName').regex = /^[a-zA-Z0-9.()_-]+$/;
	    $("#uploadBT").attr("disabled",false).html('<span><i class="miniIcoInport"></i><fmt:message bundle="${cmc}" key="CMC.label.importConfig"/></span>');
		$('#progress').children('.percent').html(I18N.CMC.tip.uploadFail);
	    if(result=="entityBusy"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deviceisInUpdating,'error');
	    } else if(result=="fileTransTimeout"){
	   	 	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.uploadFileError,'error');
	    } else if(result=="eraseError"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.eraseFileError,'error');
	    } else if(result == "snmpTimeout"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.snmpRequestTimeout,'error');
	    }
    }; 
    tftpStatus = getTftpServerStatus();*/
});  

function isValid(){
	var reg = /^[a-zA-Z0-9.()_-]+$/;
	var fileName = $("#destFileName").val();
	if(!reg.test(fileName)){
		$("#destFileName").focus();
		return false;
	}
	return true;
}

//获取TFTP服务器的启动状态
function getTftpServerStatus(){
	$.ajax({
		url: '/system/getTftpServerStatus.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			tftpStatus = json.result;
   		}, error: function(json) {
   			//获取TFTP服务器状态失败后，提示修改失败
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
	return status;
}
  
function cancelclick(){
    if (window.tranparent) {
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.notCloseWindows);
        return;
    }
	window.parent.closeWindow('uploadfile');
}
$(function(){
	tftpStatus = getTftpServerStatus();
	newCreateUpload();
});
function getProgress(){
	$.ajax({url:'/cmc/getCmcUpdateProgress.tv',method:'post',cache:false,dataType:'json',
		data:{cmcId:window.cmcId},
		success:function(res){
			if(res && res.progress){
				if(res.progress>=0 && res.progress <= 100){
					var w = res.progress + "%";
					$('.uploadBarInner').width(w);
				}else if(res.progress > 100){
					$('.uploadBarInner').width('100%');
					clearProgressInterval();
				}else{
					clearProgressInterval();
				}
			}
			
		},
		error:function(){
			clearProgressInterval();
		}
	});
}

//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: '/cmc/updateCmc.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    /* accept: {
	    	extensions: 'xls,xlsx'
	    }, */
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 100000000,
	    auto: false,
	    timeout: 0/*,
	    runtimeOrder :'flash'*/
	});
	uploader.on( 'beforeFileQueued', function( file ) {
		//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
		uploader.reset();
		//console.log('beforeFileQueued')
	});
	//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
	uploader.on( 'fileQueued', function( file ) {
		isValid();
		//上传框中传入文件名+后缀;
		if(file && file.name){
			window.chooseFileName = file.name;
			$('#fileNameInput').val(file.name);
			$('#destFileName').val(chooseFileName);
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, response) {
		clearMask();
		//拿到返回值，再去做其它事情;
		//console.log(response)
		var result = response.result;
		$("#destFileName").val(window._fileName);
    	window.tranparent = false;
		
    	
	    if(result=="updateSuccess"){
	    	//1$('#progress').children('.pbar').children('.ui-progressbar-value').css('width', '100%');
	    	$('#progress').children('.percent').html(I18N.CMC.tip.uploadComplete);
	    	top.afterSaveOrDelete({
   				title: I18N.CMC.tip.tipMsg,
   				html: '<b class="orangeTxt">' + I18N.CMC.tip.uploadSuccessResetToUpdate + '</b>'
   			});
	    	cancelclick();
	       	return;
	    }
		//$('#progress').children('.percent').html(I18N.CMC.tip.uploadFail);
		$('#fileNameInput').val('');
	    if(result=="entityBusy"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deviceisInUpdating,'error');
	    } else if(result=="fileTransTimeout"){
	   	 	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.uploadFileError,'error');
	    } else if(result=="eraseError"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.eraseFileError,'error');
	    } else if(result == "snmpTimeout"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.snmpRequestTimeout,'error');
	    } else{
	    	top.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.uploadFail);
	    }
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		clearMask();
		top.showMessageDlg(I18N.COMMON.tip, I18N.UPLOAD.fail);
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	/*uploader.on( 'uploadComplete', function( file ) {
		console.log('uploadComplete')
		var json = uploader.getStats();
		if(json && json.successNum){
			console.log( String.format('成功上传{0}个文件', json.successNum) )
		}
		if(json && json.uploadFailNum){
			console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
		}
	});*/
	uploader.on( 'error', function(err, file) {
		switch(err){
    		case 'Q_TYPE_DENIED':
    			top.showMessageDlg(I18N.COMMON.tip, I18N.UPLOAD.fileErrType ,null)
				//console.log('您选择的文件类型不符合规范');
				break;
    		case 'Q_EXCEED_SIZE_LIMIT':
    			top.showMessageDlg(I18N.COMMON.tip, I18N.UPLOAD.sizeLimit ,null)
    			//console.log('您选择的文件大小超出了范围');	
    			break;
		}
	});
}
function clearProgressInterval(){
	if(window.progressInterval != null){
		clearInterval(window.progressInterval);
		window.progressInterval = null;
		$('.uploadBarInner').width('0%');
	}
}
function startUpload(){
	if(!isValid()){
		return;
	}
	if($('#fileNameInput').val() == ''){
		top.showMessageDlg(I18N.COMMON.tip, I18N.UPLOAD.fileErrType ,null)
		return;
	}
	$('.uploadBarInner').width('0%');
	window.progressInterval = setInterval(function(){
		getProgress();
	}, 10000);	
	
	
	window._fileName = $('#destFileName').val();
    if(!tftpStatus){
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.tftpServerIsClosed, null);
    }else{
    	if(uploader != null){
    		showBodyMask(I18N.UPLOAD.loading);
    		uploader.options.formData = {
    			cmcId: window.cmcId,
    			destFileName: $('#destFileName').val(),
    			fileType: window.fileType 
    		}
    		uploader.upload();
    	}
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
<body  class="openWinBody">
	<div class="openWinHeader borderBorderNone">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle='${cmc}' key='CMC.label.attention'/>:</b><fmt:message bundle='${cmc}' key='CMC.tip.resetAttention'/></p>
	    </div>
	    <div class="rightCirIco downArrCirIco"></div>
	</div>
	<div class="uploadBar">
		<div class="uploadBarInner">
			<div class="uploaderBarInnerRight"></div>
		</div>
	</div>
	
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="150">
						<fmt:message bundle='${cmc}' key='CMC.label.saveName'/>:
	                 </td>
	                 <td>
						<input style="width:266px;" maxLength="31" type="text" class="normalInput" id="destFileName" name="destFileName" toolTip="<fmt:message bundle='${cmc}' key='CMC.tip.filenameRegTip'/>" />
	                 </td>
	             </tr>
	             <%-- <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.label.selectFile'/>:
	                 </td>
	                 <td>
						<input type="text" class="normalInputDisabled floatLeft"
							style="width: 218px;"
							id="fileName" value="<fmt:message bundle='${cmc}' key='CMC.tip.fileSize50MLimit'/>" disabled />
						<a id="chooseFile" href="javascript:;" class="nearInputBtn"><span><fmt:message bundle='${cmc}' key='CMC.button.scan'/></span></a>
	                 </td>
	             </tr> --%>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.label.selectFile'/>:
	                 </td>
	                 <td>
						<input type="text" class="normalInputDisabled floatLeft" style="width: 218px;" id="fileNameInput" placeholder="<fmt:message bundle='${cmc}' key='CMC.tip.fileSize50MLimit'/>" disabled />
						<div class="btns">
                   			<div id="picker"><fmt:message bundle='${cmc}' key='CMC.button.scan'/></div>
                   		</div>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		     	 <li>
		     	 	<a class="normalBtnBig" href="javascript:;" onclick="startUpload()"><span><i class="miniIcoInport"></i><fmt:message bundle="${cmc}" key="CMC.label.importConfig"/></span></a>
		     	 </li>
		         <%-- <li><a  id="uploadBT" onclick="upload()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoInport"></i><fmt:message bundle="${cmc}" key="CMC.label.importConfig"/></span></a></li> --%>
		         <li><a onclick="cancelclick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></span></a></li>
		     </ol>
		</div>
		
	</div>
	


	<div id="fu"></div>
	<div id="upload">
		
	</div>
	<div id="file"
		style="position: absolute; z-index: 1; left: 120px; top: 10px; width: 80px; height: 24px;">
	</div>
	
	<div style="padding-top: 20px; padding-left: 280px">
		<!-- ProgressBar -->
		<div id="progress">
			<div class="percent"></div>
	    	<div class="pbar"></div>
	    </div>
		
	</div>
</body>
</html>