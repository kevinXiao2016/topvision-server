<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    library zeta
    library FileUpload
    library ext
    module network
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<style type="text/css">
.rightConner{ position: absolute; bottom:10px; right:10px;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script> 
<script type="text/javascript">
var uploader = null;
var fileName = '';
var baseGrid = null;
var flag = 0;//标志目前列表中数据 0:导入成功  1:导入失败   2:未皮配
var flash;
/* $( DOC ).ready(function(){    
    flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        var chooseFileName = obj.name;
        if(obj.size > 100*1024*1024){
            window.parent.showMessageDlg('@COMMON.tip@', String.format('@NAMEIMPORT.select100MFile@', obj.name));
            return;
        }else{
            $("#fileInput").val(chooseFileName);
        }
    };
    flash.onComplete =function(result){
    	result = Ext.util.JSON.decode(result);     
        Ext.getBody().unmask();
        if(result.message == "FileWrong"){
            window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
            return;
        }else if(result.message == "ValueWrong"){
        	window.parent.showMessageDlg('@COMMON.tip@', '@sendConfig.importValueError@' + result.colunmn);
            return;
        }else if(result.message == "success"){
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.importSuccess@');
        	window.top.getActiveFrame().onRefreshClick();
        	cancelClick();
            return;
        }else{
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
        	return;
        }
    };
    
}); 

function okBtClick(){
	var overwrite = $("#overwirte").attr("checked");
	var fileName = $("#fileInput").val();
	if( fileName ){
	   Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + '@NAMEIMPORT.readingFile@');
	   flash.upload("/entity/telnetLogin/importTelnetLogin.tv?overwrite=" + overwrite);
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.chooseFile@');
	}
}*/

function cancelClick() {
    window.top.closeWindow('telnetLoginImport');
}

function downloadTemplate(){
	window.location.href="entity/import/downLoadTemplate.tv";
}
$(function(){
	newCreateUpload();
});
//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '../../js/webupload/Uploader.swf',
	    server: '/entity/telnetLogin/importTelnetLogin.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    accept: {
	    	extensions: 'xls,xlsx'
	    },
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 100*1024*1024,
	    auto: false,
	    timeout: 0/* ,
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
			$('#fileNameInput').val(file.name);
			window.fileName = file.name;
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, result) {
		//拿到返回值，再去做其它事情;
		//console.log(result)
		clearMask();
        if(result.message == "FileWrong"){
            window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
            return;
        }else if(result.message == "ValueWrong"){
        	window.parent.showMessageDlg('@COMMON.tip@', '@sendConfig.importValueError@' + result.colunmn);
            return;
        }else if(result.message == "success"){
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.importSuccess@');
        	window.top.getActiveFrame().onRefreshClick();
        	cancelClick();
            return;
        }else{
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
        	return;
        }
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
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
	var overwrite = $("#overwirte").attr("checked");
	var fileName = $("#fileNameInput").val();
	if( fileName ){
		showBodyMask('@NAMEIMPORT.readingFile@');
		uploader.options.formData = {
			overwrite: overwrite
		}
		uploader.upload();
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.chooseFile@');
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
	<body class=openWinBody>
		<div id="step1">
			<div class=formtip id=tips style="display: none"></div>
			<div class="openWinHeader">
				<div class="openWinTip">@sendConfig.importLoginInfo@</div>
				<div class="rightCirIco folderCirIco"></div>
			</div>

			<div class="edge10 pT5">
				<form onsubmit="return false;">
					<table class="mCenter zebraTableRows">
						<%-- <tr>
							<td width="260" class="rightBlueTxt"><label id="fileLabel"
								style='width: 60px;'>@NAMEIMPORT.selectFile@:</label></td>
							<td>
								<div id='file_div' style='margin-top: 5px;'>
									<input id="fileInput" class="normalInputDisabled floatLeft"
										type="text" disabled="disabled" /> <a id="chooseFile"
										href="javascript:;" class="nearInputBtn"><span>@NAMEIMPORT.import@ Excel</span></a>
								</div>
							</td>
						</tr> --%>
						<tr>
							<td width="260" class="rightBlueTxt">@NAMEIMPORT.selectFile@:</td>
							<td width="260">
								<input id="fileNameInput" class="normalInputDisabled floatLeft" type="text" disabled="disabled" />
								<div class="btns">
			                		<div id="picker">@NAMEIMPORT.import@ Excel</div>
			                	</div>
							</td>
							<td>
								<a href="javascript: downloadTemplate();" class="normalBtn" style="margin-right: 2px;"><span><i class="miniIcoArrDown"></i>@NAMEEXPORT.templateDownload@</span></a>
							</td>
						</tr>
						<tr class="darkZebraTr" align="center" id="deleteForm">
							<td class="rightBlueTxt">@sendConfig.overwrite@:</td>
							<td align="left" colspan="2"><input type="checkbox" class="normalInput" id="overwirte" checked="checked"/></td>
						</tr>
					</table>
				</form>
			</div>

			<Zeta:ButtonGroup>
				<li>
					<a href="javascript:;" class="normalBtnBig" onclick="startUpload()"><span><i class="miniIcoInport"></i>@resources/COMMON.importAction@</span></a>
				</li>
				<%-- <Zeta:Button onClick="okBtClick()" icon="miniIcoInport">@resources/COMMON.importAction@</Zeta:Button> --%>
				<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
			</Zeta:ButtonGroup>
			<div class="edge10 pT20">
				<div class="yellowTip">
					<b class="orangeTxt">@NAMEIMPORT.importRule@:</b>
					<p class="pT10">@sendConfig.rule1@</p>
					<p>@sendConfig.rule2@</p>
					<p>@sendConfig.rule3@</p>
					<p>@sendConfig.rule4@</p>
					<p>@sendConfig.rule5@</p>
					<p>@sendConfig.rule6@</p>
				</div>
			</div>
		</div>
	</body>
</Zeta:HTML>