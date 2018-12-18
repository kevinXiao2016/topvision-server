var uploader = null;
var fileName = '';
$(function(){
	newCreateUpload();
});

//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: '/cmpoll/importSpecifiedCmList.tv',
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
	uploader.on( 'uploadSuccess', function(file, response) {
		//拿到返回值，再去做其它事情;
		//console.log(response)
		if(response.rowcount > 0){
            var info = '@cmPoll.importSuccess@';
            if (response.isOverLimit) {
                info = '@cmPoll.isOverLimit@'
            }
    		top.afterSaveOrDelete({
   		        title: '@COMMON.tip@',
   		        html: '<b class="orangeTxt">' + info + '</b>'
   		    });
    		top.getActiveFrame().updateRowCount(response.rowcount);
    		cancelClick();
    	}else{
    		var tip = '@cmPoll.errorData@'
    		if(response.error){
    			tip += '@cmPoll.errorCode@@COMMON.maohao@' + response.error;
    		}
    		top.showMessageDlg('@COMMON.tip@', tip);
    		clearMask();
    		$('#fileNameInput').val('');
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
	if($('#fileNameInput').val() == ''){
		top.showMessageDlg('@COMMON.tip@', '@cmPoll.pleaseSelExcel@');
		return;
	}
	if(uploader != null){
		showBodyMask('@UPLOAD.loading@');
		uploader.options.formData = {
			fileName: window.fileName
		}
		uploader.upload();
	}
}
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}