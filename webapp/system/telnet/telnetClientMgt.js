var RECORD_ON = 1;
var RECORD_OFF = 0;
var INNER_CLIENT = 1;
var EXTERNAL_CLIENT = 2;

$(function(){
	//初始化是否开启记录的控件
	switchBtn = new Nm3kSwitch('record-state-container', recordState);
	switchBtn.init();
	
	// 赋值客户端类型
	if(clientType == INNER_CLIENT) {
		$('#inner-client').attr('checked', 'checked');
	} else if (clientType == EXTERNAL_CLIENT) {
		$('#external-client').attr('checked', 'checked');
	}
	
	// 赋值timeout
	$('#timeout').val(timeout);
})

function saveClick() {
	// 获取客户端类型的值
	var clientType = $('[name=clientType]:checked').val();
	// 获取是否开启记录的值
	var recordState = switchBtn.getValue();
	// 获取超时时间
	var timeout = $('#timeout').val();
	
	// 校验超时时间
	var validate = true;
	if(/^[1-9]\d{3,4}$/.test(timeout)) {
		timeout = parseInt(timeout, 10);
		if(timeout < 1000 || timeout > 30000) {
			validate = false;
		}
	} else {
		validate = false;
	}
	if(!validate) {
		$('#timeout').focus();
		return;
	}
	
	// 下发保存请求
	window.top.showWaitingDlg('@COMMON.wait@', '@COMMON.refreshing@', 'ext-mb-waiting');
	$.ajax({
		url: '/system/telnet/saveClientConfig.tv',
    	type: 'POST',
    	data: {
    		clientType: clientType,
    		recordState: recordState,
    		timeout: timeout
    	},
    	dataType: "json",
   		success: function(json) {
   			top.closeWaitingDlg();
   			cancelClick();
   			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@sys.saved@</b>'
 		    });
   		}, error: function(json) {
   			top.closeWaitingDlg();
   			window.top.showMessageDlg('@COMMON.tip@', '@TelnetClient.saveCfgFailed@');
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function cancelClick() {
	window.top.closeWindow('telnetClientMgt');
}