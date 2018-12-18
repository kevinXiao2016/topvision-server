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
    module cmc
    import js.tools.ipText
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<script type="text/javascript">
	var uploader = null;
	var fileName = '';
	$(function(){
		newCreateUpload();
	});
	//创建上传控件;
	function newCreateUpload(){
		uploader = WebUploader.create({
		    swf: '../js/webupload/Uploader.swf',
		    server: '/cm/scanCmServiceTypeImport.tv',
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
			clearMask();
	        if(result.message == "FileWrong"){
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.fileFormatError);
	            return;
	        }else if (result.message == "fileNameWrong"){
	        	window.parent.showMessageDlg(I18N.COMMON.tip, "@CM.filenameError@" + "&nbsp" + I18N.CCMTS.errorLine 
	        			+ result.errorLine);
	        	return;
	        }else if(result.message == "serviceTypeWrong"){
	        	window.parent.showMessageDlg(I18N.COMMON.tip, "@CM.serviceTypeerror@" + "&nbsp<br>" + I18N.CCMTS.errorLine 
	        			+ result.errorLine);
	        	return;
	        }else if(result.message == "success"){
	        	var successTip = I18N.CMC.tip.importFileComplete + result.number + "<br>" ;
	            top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">' + successTip + '</b>'
	   			});
	            cancelClick();
	            return
	        }else{
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.title.importfail);
	        	return
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



/* var flash;
$( DOC ).ready(function(){    
    flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        var chooseFileName = obj.name;
        if(obj.size > 100*1024*1024){
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.CMC.tip.select100MFile, obj.name));
            return;
        }else{
            $("#fileInput").val(chooseFileName);
        }
    };
    flash.onComplete =function(result){
    	result = Ext.util.JSON.decode(result);     
        Ext.getBody().unmask();
        if(result.message == "FileWrong"){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.fileFormatError);
            return;
        }else if (result.message == "fileNameWrong"){
        	window.parent.showMessageDlg(I18N.COMMON.tip, "@CM.filenameError@" + "&nbsp" + I18N.CCMTS.errorLine 
        			+ result.errorLine);
        	return;
        }else if(result.message == "serviceTypeWrong"){
        	window.parent.showMessageDlg(I18N.COMMON.tip, "@CM.serviceTypeerror@" + "&nbsp<br>" + I18N.CCMTS.errorLine 
        			+ result.errorLine);
        	return;
        }else if(result.message == "success"){
        	var successTip = I18N.CMC.tip.importFileComplete + result.number + "<br>" ;
            top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">' + successTip + '</b>'
   			});
            cancelClick();
            return
        }else{
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.title.importfail);
        	return
        }
    };
}); 

function okBtClick(){
	var deleteStatus = $("#deleteForm :checked").val();
	var fileName = $("#fileInput").val();
	if( fileName ){
	   Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + I18N.CMC.tip.readingFile);
	   flash.upload("/cm/scanCmServiceTypeImport.tv?deleteStatus=" + deleteStatus);
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cm.chooseExcelFile);
	}
}*/

function cancelClick() {
    window.top.closeWindow('importCmServiceType');
}

//文件下载;
function downLoadFile(){
	window.location.href="cm/downLoadCmServiceTypeTemplate.tv";
}
//保存;
function startUpload(){
	var deleteStatus = $("#deleteForm :checked").val();
	var fileName = $("#fileNameInput").val();
	if( fileName ){
	   showBodyMask('@UPLOAD.loading@');
	   uploader.options.formData = {
			  deleteStatus: deleteStatus
		}
	   uploader.upload();
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cm.chooseExcelFile);
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
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@CM.importServiceType@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10 pT5">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<%-- <tr>
						<td width="260" class="rightBlueTxt">
							<label id="fileLabel" style='width: 60px;'>@CMC.label.selectFile@:</label>
						</td>
						<td align="center" colspan="2">
							<div id='file_div' style='margin-top: 5px;'>
								<input id="fileInput" class="normalInputDisabled floatLeft" type="text" disabled="disabled" /> 
								<a id="chooseFile" href="javascript:;" class="nearInputBtn">
									<span>@COMMON.browse@...</span>
								</a>
							</div>
						</td>
					</tr> --%>
					<tr>
						<td class="rightBlueTxt">@COMMON.required@@CMC.label.selectFile@:</td>
						<td width="240">
							<input id="fileNameInput" class="normalInputDisabled floatLeft" type="text" disabled="disabled" />
							<div class="btns">
                    			<div id="picker">@CMC.title.importExcel@</div>
                    		</div>
						</td>
						<td>
							<a id="downLoadFile" href="javascript:;" class="normalBtn" onclick="downLoadFile()">
								<span><i class="miniIcoArrDown"></i>@CMC.title.downloadTemplate@</span>
							</a>
						</td>
					</tr>
					<tr class="darkZebraTr" align="center" id="deleteForm">
						<td width="260" class="rightBlueTxt">
							<label style='width: 60px;'>@CMC.label.deleteOldInfo@:</label>
						</td>
						<td align="left"  colspan="2">
							<input name="deleteOldRatio" type="radio" value="true" />@CMC.text.yes@&nbsp;&nbsp; 
							<input name="deleteOldRatio" type="radio" value="false" checked />@CMC.text.no@
						</td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 第三部分，按钮组合 -->
		<Zeta:ButtonGroup>
			<li>
				<a href="javascript:;" class="normalBtnBig" onclick="startUpload()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a>
			</li>
			<%-- <Zeta:Button onClick="okBtClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button> --%>
			<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
		<!-- 导入模板说明 -->
		<div class="edge10 pT20">
				<div class="yellowTip">
					<b class="orangeTxt">@cm.cmImportTipsTitle@</b>
					<p class="pT10">@CM.importServiceTypetip1@</p>
					<p>@CM.importServiceTypetip2@</p>
					<p>@CM.importServiceTypetip3@</p>
					<p>@CM.importServiceTypetip4@</p>
					<p>@CM.importServiceTypetip5@</p>
					<p>@CM.importServiceTypetip6@</p>
					<p>@CM.importServiceTypetip7@</p>
					<p>@CM.importServiceTypetip8@</p>
					<p>@CM.importServiceTypetip9@</p>
				</div>
			</div>
	</body>
</Zeta:HTML>