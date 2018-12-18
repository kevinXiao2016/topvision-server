$(function(){
	//实现导出EXCEL方法
	$('#exportBtn').on('click', exportExcel);
	
	// 初始化上传文件控件
	initUploadComponent();
	
	//导入事件绑定
	$('#importBtn').bind('click', importExcel);
	
});

/**
 * 导出网管结构数据的EXCEL文件
 */
function exportExcel(){
	//下发生成EXCEL文件请求
	window.top.showWaitingDlg('等待框', '正在导出，请稍等。');
	var generateFilePromise = $.post('/export/generateExportExcel.tv');
	//生成EXCEL文件成功
	generateFilePromise.done(function(response){
		window.top.closeWaitingDlg('等待框');
		if(response.error){
			window.top.showMessageDlg('错误', 'EXCEL文件生成失败，请重新导出！');
		}else{
			// 发送下载请求
			window.location.href="/export/downloadGenerateFile.tv?fileName="+response.fileName;
		}
	});
	//生成EXCEL文件失败
	generateFilePromise.fail(function(){
		window.top.closeWaitingDlg('等待框');
		window.top.showMessageDlg('错误', 'EXCEL文件生成失败，请重新导出！');
	});
}

/**
 * 初始化上传文件组件
 */
function initUploadComponent(){
	$("#localFile").fakeUpload("init",{    
		"tiptext": '不支持空文件' ,
		"width":400,
		"btntext": '选择' ,
		"checkfn":function(filePath,name){
			//获取文件名
			var reg = /[^\\]+\.[\w]+$/;
			var fileName = reg.exec(filePath);
			$("#localFile").find("input[type='text']").val(filePath);
			return true;
		}
	}); 
}

/**
 * 导入excel文件
 */
function importExcel(){
	// 检查是否可以导入
	$.ajax({
		url : '/entity/checkBatchActionAvailable.tv',
		cache : false,
		success : function(msg) {
			if (msg == "currentTopoBusy") {
				return window.parent.showMessageDlg('@COMMON.tip@', '@batchTopo.systemTopoBusy@');
			} else if (msg == "currentTopoStart") {
				// 可以导入
				window.top.showWaitingDlg('@COMMON.waiting@', '正在导入文件');
				//监听导入进展
				listenImportDwr();
				$.ajaxFileUpload({
					url:'/import/importEntireExcel.tv',
					secureuri:false,
					fileElementId:'localFile',
					data: {
						jconnectionId: WIN.top.GLOBAL_SOCKET_CONNECT_ID
					},
					dataType: 'json',
					success: function (data, status){
						clearUpload();
						window.parent.closeWaitingDlg();
						if(data.error){
							//移除监听
							window.top.removeCallback('importEntireExcel', 666);
							//导入文件失败，告知原因
							window.parent.showMessageDlg('@COMMON.waiting@', data.error);
						}
			      	}, error: function (data, status, e){
			      		clearUpload();
			        	window.parent.closeWaitingDlg();
			        },cache: false,
					complete: function (XHR, TS) {
						XHR = null ; 
						//cancleClick(); 
					}
				})
			}
		},
		error : function() {
			window.parent.showMessageDlg('@COMMON.tip@', '@batchTopo.execFail@');
		}
	});
}

function clearUpload(){
	$("#localFile").find("input[type=file]").val('');
	$("#localFile").find("input[type=text]").val('');
}

function listenImportDwr(){
	//首先清空进度内容
	$('#importDetail-well').empty();
	window.top.addCallback("importEntireExcel", function(status) {
		$('#importDetail-well').append(String.format('<p>{0}</p>', status));
	});
}