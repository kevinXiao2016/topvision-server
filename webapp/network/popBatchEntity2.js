$(function(){
	createUpload();
	// 导入按钮功能实现
	$('#newOkBtn').bind('click',
		function() {
			// 防止重复点击
			$("#newOkBtn").attr("disabled", true).mouseout();
			setTimeout(function() {
				$("#newOkBtn").attr("disabled", false);
			}, 1500);
	
			// 校验有没有选择文件
			var ipStr = $("#fileNameInput").val();
			if (!ipStr) {
				return window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.batchTopo.checkExcel);
			}
			// 校验有没有选择设备类型
			var typeList = [];
			$.each($("input:checked"), function(idx, item) {
				typeList.push(item.value);
			});
			var typeStr = typeList.join(",");
			if (!typeStr) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.batchTopo.checkDeviceType);
				return;
			}
			// 校验SNMP标签
			if (snmpList.length == 0) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.batchTopo.checkSnmpLabel);
				return;
			}
	
			// 检验SNMP参数和PING参数
			var retryCount = $("#retryCountSel").val();
			var timeoutInput = $("#timeoutInput").val();
			var pingTimeout = $("#pingTimeout").val();
			if (!checkedTimeoutInput()) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.batchTopo.checkTheTimeout);
				return;
			}
			if (!checkedPingTimeout()) {
				window.parent.showMessageDlg(I18N.COMMON.tip,
						I18N.batchTopo.checkTheTimeout);
				return;
			}
	
			// 检查是否可以导入
			$.ajax({
				url : '/entity/checkBatchActionAvailable.tv',
				cache : false,
				success : function(msg) {
					if (msg == "currentTopoBusy") {
						return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.systemTopoBusy);
					} else if (msg == "currentTopoStart") {
						// 可以导入，组织url
						/*url = '/entity/scanEntity.tv?dwrId=999';
						url += '&entityTypeString=' + typeStr;
						url += '&snmpTabStr=' + Ext.encode(snmpList);
						url += '&retryCount=' + retryCount;
						url += '&timeoutSeconds=' + timeoutInput;
						url += '&pingTimeout=' + pingTimeout;*/
						if(uploader != null){
							showBodyMask('@UPLOAD.loading@');
							uploader.options.formData = {
								dwrId: 999,
								entityTypeString: typeStr,
								snmpTabStr: Ext.encode(snmpList),
								retryCount: retryCount,
								timeoutSeconds: timeoutInput,
								pingTimeout: pingTimeout
							}
							uploader.upload();
						}
					}
				},
				error : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.execFail);
				}
			});
		});
});//end document.ready;

function createUpload(){
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: '/entity/scanEntity.tv',
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
	    fileSizeLimit: 10000000,
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
		clearMask();
		//拿到返回值，再去做其它事情;
		//console.log(result)
		if (!result)
			return;
		//result = Ext.decode(result);
		if (result.success) {
			// 文件解析成功，已经开始发现
			var url = '/entity/showScanEntityResult.tv?dwrId=999';
			// 查看是否有非法的IP地址
			if (result.errorIpRow) {
				url += '&errorIpRow=' + result.errorIpRow.join(',');
			}
			window.parent.createDialog("scanResult", I18N.batchTopo.topoResult, 650, 470, 
					"/entity/showScanEntityResult.tv?dwrId=999", null, true, true);
			cancelClick();
		} else if (result.error) {
			// 文件解析失败
			top.showMessageDlg('@COMMON.tip@', result.error);
		}
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		//clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	uploader.on( 'uploadComplete', function( file ) {
		//console.log('uploadComplete')
		var json = uploader.getStats();
		if(json && json.successNum){
			//console.log( String.format('成功上传{0}个文件', json.successNum) )
		}
		if(json && json.uploadFailNum){
			//console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
		}
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
		clearMask();
	});
}
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}