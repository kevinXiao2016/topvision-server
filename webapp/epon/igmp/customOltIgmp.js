function closeFakeWindow(){
	var $fakeExtWindow = $("#fakeExtWindow");
	$fakeExtWindow.hide();
	$("#mask").css({display:'none'});
	window.parent.displayMask('none');
}
function openBg(){
	var $mask = $("#mask");
	if($mask.length === 0){
		$('body').append('<div id="mask" class="fakeMask"></div>');
	}
	$("#mask").css({display:'block'});
	window.parent.displayMask('block');
}
function openFakeExtWin(){
	var $fakeExtWindow = $("#fakeExtWindow");
	if($fakeExtWindow.length === 0){
		$('body').append('<div id="fakeExtWindow" class="fakeExtWindow corner3"></div>');
	}else{
		$fakeExtWindow.empty().show();
	}
}

//根据端口类型获取端口列表
/*
 * {
 *     portType	
 *     entityId
 *     successCallBack
 *     failCallBack
 * }
 */
function loadSniListByType(o){
	$.ajax({
		url : '/epon/igmpconfig/loadSniListByType.tv',
		type : 'POST',
		data : {
			entityId : o.entityId,
			portType : o.portType
		},
		dataType :　'json',
		success : function(json) {
			if( o.successCallBack ){
				o.successCallBack(json);
			}
		},
		error : function(json) {
			if( o.failCallBack ){
				o.failCallBack(json);
			}
		},
		cache : false,
		async : o.async === false ? false : true
	});
}

/**
 * 获取SNI端口列表
 * @param o
 * @returns
 */
function loadSniList(o){
	$.ajax({
		url : '/epon/igmpconfig/loadSniList.tv',
		type : 'POST',
		data : {
			entityId : o.entityId
		},
		dataType :　'json',
		success : function(json) {
			if( o.successCallBack ){
				o.successCallBack(json);
			}
		},
		error : function(json) {
			if( o.failCallBack ){
				o.failCallBack(json);
			}
		},
		cache : false,
		async : o.async === false ? false : true
	});
}

//获取上行聚合端口时使用
/*
 * {
 *     portType	
 *     entityId
 *     successCallBack
 *     failCallBack
 * }
 */
function loadSniAggList(o){
	$.ajax({
		url : '/epon/igmpconfig/loadSniAggList.tv',
		type : 'POST',
		data : {
			entityId : o.entityId,
			portType : o.portType
		},
		dataType :　'json',
		success : function(json) {
			if( o.successCallBack ){
				o.successCallBack(json);
			}
		},
		error : function(json) {
			if( o.failCallBack ){
				o.failCallBack(json);
			}
		},
		cache : false,
		async : o.async === false ? false : true
	});
}
//验证是否是数字(不能大于10位,实际上我们目前验证没有超过7位数字);
function validateNumber(v){
	var regExp = /^\d{1,10}$/;
	return regExp.test(v);
}
//验证是否是在某个范围;
function validateRange(v,arr){
	if( v >= arr[0] && v <= arr[1] ){
		return true;
	}
}
function customValidateFn(arr){
	for(var i=0,len=arr.length; i<len; i++){
		var o   = arr[i],
		    $id = $("#"+o.id), 
		    v   = $id.val();
		if( validateNumber(v) ){//必须是数字;
			if( !validateRange(v, o.range) ){//验证数字范围;
				return $id;
			}
			if(i === len-1){ //最后一个也验证通过了;
				return true;
			}
		}else{
			return $id;
		}
	}
};