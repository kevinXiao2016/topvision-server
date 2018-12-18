var percent = 0;
var progressInterval = null;
$(function(){
	createUpload();
});

//创建上传控件;
function createUpload(){
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: '/epon/upLoadFileToFile.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    /* accept: {
	    	extensions: 'xlsx'
	    }, */
	    duplicate: true,
	    resize: false,
	    fileNumLimit: 1,
	    fileSizeLimit: 50*1024*1024,
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
		window.fileSize = file.size;
		window.chooseFileName = file.name;
		var $fileName = $('#destFileName');
		//保存名，没有填写的时候，填入文件名;
		if($fileName.val() == '' && file && file.name){
			$fileName.val(file.name)
		}
		//上传框中传入文件名+后缀;
		if(file && file.name){
			$('#fileNameInput').val(file.name);
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
		response = response.result;
		//console.log('uploadSuccess已上传');
		if(response == "success"){
			//将进度条设置成100%;
			$(".uploadBar .uploadBarInner").width("100%");
            top.showMessageDlg('@COMMON.tip@', '@SERVICE.uploadOk@' ,null,function(){
               top.getWindow("fileManage").body.dom.firstChild.contentWindow.store.load();
               top.closeWindow('uploadfile');
            })
            return;
        }
		var $tooltip = $("#uploadTooltip");
		switch(response){
			case 'inProgress':
				$tooltip.html("@SERVICE.uploadBusy@");
	            top.showMessageDlg('@COMMON.tip@', "@SERVICE.uploadBusy@" ,'error');
				break;
			case 'fileNotExists':
				$tooltip.html("@SERVICE.uploadAg@");
	            top.showMessageDlg('@COMMON.tip@', "@SERVICE.uploadAg@" ,'error');
				break;
			case 'fileNoTime':
				$tooltip.html("@SERVICE.uploadSlow@");
	            top.showMessageDlg('@COMMON.tip@', "@SERVICE.uploadSlow@" ,'info');
				break;
			case 'fileNoResponse':
				$tooltip.html("@SERVICE.uploadTimeout@");
	            top.showMessageDlg('@COMMON.tip@', "@SERVICE.uploadTimeout@" ,'info');
				break;
			default:
				$tooltip.html("@SERVICE.uploadEr@");
            	top.showMessageDlg('@COMMON.tip@', '@SERVICE.uploadEr@','error');
				break;
		}
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	uploader.on( 'uploadComplete', function( file ) {
		console.log('uploadComplete')
		var json = uploader.getStats();
		if(json && json.successNum){
			//console.log( String.format('成功上传{0}个文件', json.successNum) )
		}
		if(json && json.uploadFailNum){
			//console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
		}
		resetUpload();
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
		resetUpload();
	});
}
function startUpload(){
	window.chooseFilePath =  Ext.getCmp('fileCombo').getValue();
	window._fileName = $('#destFileName').val();
	if (window.chooseFilePath) {
    	if(!/^[a-zA-Z0-9.()_-]{1,31}$/.test(_fileName)){
    		window.top.showMessageDlg('@COMMON.tip@', "@SERVICE.destFileTip@" ,null);
    	}else if (!chooseFileName.length > 0){
            top.showMessageDlg('@COMMON.tip@', '@SERVICE.plsSelectFileFirst@' ,null)
        //判断文件重名是否覆盖，这里fileNameArray里面是空，此处有bug;    
		}else if(fileNameArray.indexOf(( chooseFilePath + _fileName))!=-1){
	      	top.showConfirmDlg('@COMMON.tip@', '@SERVICE.fileOverName@' , function(type) {
	            if(type=='yes'){
	            	startUploading();
	            	intervalGetProgress();
                }
	    	})
        }else{
        	startUploading();
        	intervalGetProgress();
        }
    }else{
    	top.showMessageDlg("@COMMON.tip@","@SERVICE.plsChooseFilePath@");
    }
}
function startUploading(){
	if(uploader != null){
		showLoadingCir();
		showBodyMask('@UPLOAD.loading@');
		uploader.options.formData = {
			entityId: window.entityId,
			filePath: window.chooseFilePath,
			destFileName: window._fileName
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
//显示loading的圆圈;
function showLoadingCir(){
	$(".openWinHeader .rightCirIco").attr("class","rightCirIco cirIco").html('<div class="loadingCirIco"></div>');
}
//清除掉loading的圆圈;
function clearLoadingCir(){
	$(".openWinHeader .rightCirIco").empty().addClass('pageCirIco').removeClass('cirIco');
}
function resetInputDom(){
	$('#destFileName, #fileNameInput').val('');
}
function resetUpload(){
	clearMask();
	resetInputDom();
	clearIntervalGetProgress();
	clearLoadingCir();
}
//每隔15秒去获取一下进度;
function intervalGetProgress(){
	getProgress();
	window.progressInterval = setInterval(function(){
		getProgress();
	}, 15000);
}
function clearIntervalGetProgress(){
	clearInterval(window.progressInterval);
}
//获取进度，界面上进度条展示;
function getProgress(){
	$.ajax({
		url: '/epon/checkFileSize.tv',
		method: 'post',
		cache: false,
		dataType: 'plain',
		data: {
			entityId: window.entityId,
			filePath: Ext.getCmp('fileCombo').getValue(),
			destFileName: $('#destFileName').val()
		},
		success: function(res){
			if("fileNoStart" == res){return;}
			if('getFileSizeWrong'==res){return;}
			//一开始总是会返回进度100%;
			if(res >= window.fileSize){ return;}
				
			var _percent = 100*res/window.fileSize;//记录本次的百分比
			_percent = Math.floor(_percent);
			percent = _percent > percent ? _percent : percent;//保证在取的过程中，数只增大不减小。
			console.log('res: ' + res + " fileSize: " + window.fileSize + " _percent: " + _percent)
			$(".uploadBar .uploadBarInner").width(_percent + "%");
			
			/*
			if(FirstFlag){
				window.flagData = res;
				FirstFlag = false;
				return;
			}
			if(window.flagData == res)return;
			var _percent = 100*res/fileSize;//记录本次的百分比
			_percent = Math.floor(_percent);
			percent = _percent > percent ? _percent : percent;//保证在取的过程中，数只增大不减小。
			animateProgress(_percent);*/
		},
		error:function(){}
	});
}

















