var RESPONSE_STATUS = {
	NOT_FOUND : 404,
	INTERVAL_SERVER_ERROR : 500
};
(function(){
	// private function
	//var _listen_ = function (){
		if(typeof jQuery != 'undefined' || typeof $ != 'undefined') {
			$.ajaxSetup({
				cache:false,type:"POST",
				complete : function (XHR, TS) { XHR = null;}
			});
			//后端异常转换
			var ajax = $.ajax;
			$.ajax = function( s ){
				if(typeof s =='object' && s.error && typeof s.error == 'function'){
					var errorHnd = s.error;
					s.error = function( xhr ){
						if ( xhr.status == RESPONSE_STATUS.INTERVAL_SERVER_ERROR ){
							var respTex = xhr.responseText;
							if( respTex.substring(0,2) == "{}" ){
								respTex = respTex.substring(2);
							}
							var firstToken = respTex.indexOf("{");
							var data = window["eval"]("(" + respTex.substring(firstToken) + ")");
							//var exception = data.exception
							//copy attributes to data from exception: message,stackTrace,cause, cutsomAttribute, etc.
							/*for(var prop in exception){
								data[prop] = exception[prop];
							}*/
							errorHnd( data, xhr.status);
						} else {
							errorHnd( xhr, xhr.status );
						}
					}
					//ARRAY BUG
					/*if(s.data){
						var data = s.data;
						for(var param in data){
							if(data[param] instanceof Array){
								data[param] = data[param].toString(); 
							}
						}
					}*/
				}
				ajax( s );
			};
			//attr返回checked的问题处理,还没有做好@mark by bravin
			/*var $attr = $.fn.attr;
			$.fn.attr = function(){
			    var result = $attr(arguments);
			    var requestProperty = arguments[0];
			    if(typeof requestProperty == 'string'){
			        if("checked" === requestProperty.toLowerCase()){
			            alert(result)
			            if( result == "checked" ){
			                return true;
			            }else{
			                return false;
			            }
			        }
			    }
			    return result;
			}*/
		}
		//TODO 前端消息推送封装
	//}();
	// listen
	/*try{
		window.addEventListener("onload",_listen_)
	}catch(e){
		window.attachEvent("onload",_listen_)
	}*/
})()