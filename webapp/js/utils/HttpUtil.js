/*
 * Ajax请求方法封装
 * params中可指定data，cache，url等信息
 */
function sendRequest(url, method, param, sn, fn) {	Ext.Ajax.request({url:url, failure:fn, success:sn, params:param});}
function defaultErrorHandler() {
	Ext.MessageBox.show({title: I18N.COMMON.error, msg: I18N.COMMON.serverDisconnected,buttons: Ext.MessageBox.OK, icon: Ext.MessageBox.ERROR});
}
function defaultFailureCallback(response) {	window.parent.showMessageDlg(I18N.COMMON.error, I18N.COMMON.operationFailure, "error"); }
function defaultSuccessCallback(response) {}

/**
 * 注册使用长时间请求。
 * @param request.message 等待框的提示
 * @param request.url 请求的url
 * @param request.data 请求携带的参数
 * @param request.successMessage 请求成功的提示语
 * @param request.errorMessage 请求失败的提示语
 * @param request.proccessHandler 处理过程中接受到的数据的处理器(默认直接提示数据)
 */
function executeLongRequeset(request){
	var handler = request.handler || defaultHandler;
	addCallback("@{LongRequest.LONG_REQUEST_REGISTER}@", handler);
	showWaitingDlg("@COMMON.wait@", request.message);
	var $data = request.data || {};
	if(typeof $data == 'string'){
		$data += "&jconnectionId="+ GLOBAL_SOCKET_CONNECT_ID;
	}else{
		$data.jconnectionId = GLOBAL_SOCKET_CONNECT_ID;
	}
	$.ajax({
		url: request.url,
		data: $data,
		cache: false,
		error: errorHandler
	});
	
	function errorHandler(data){
		if(request.requestErrorHandler){
			request.requestErrorHandler(data);
		}else{
			showMessageDlg("@COMMON.error@",  request.errorMessage)
		}
	}
	
	function defaultHandler(data){
		if( data.status == "@{LongRequest.PROCESS}@"){
			if(request.proccessHandler){
				request.proccessHandler(data);
			}else{
				showWaitingDlg("@COMMON.wait@", data.data);
			}
			return;
		}
		closeWaitingDlg();
		removeCallback("@{LongRequest.LONG_REQUEST_REGISTER}@");
		if( data.status == "@{LongRequest.COMPLETE}@" ){
			if(request.requestHandler){
				request.requestHandler(data);
			}else{
				afterSaveOrDelete({
					title: "@COMMON.tip@",
					html: '<b class="orangeTxt">'+request.successMessage+'</b>'
				});
			}
		}else if(data.status == "@{LongRequest.ERROR_INTERRUPT}@"){
			errorHandler(data);
		}
	}
}