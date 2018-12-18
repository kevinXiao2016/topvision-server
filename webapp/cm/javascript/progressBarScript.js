window.onunload =  function(){
	try{
		window.parent.stopProgress();
		window.close();
	}catch(e){
		//TODO 控制台
	}
}

var DEFAULT_CIR_ICON;
var UPLOAD_FINISHED = 100;
var ERR_200 = 'means transfer error';
var firstStart = false;

function show(a, obj){
	if(!firstStart){
		firstStart = true;
		animateProgressStart();
	}
	window.parent.startProgess(function(){
    	_progress(entityId, obj);
    }, 10000 );
}

function _progress(entityId, obj){
	 window._fl = $.ajax({url:'/cmupgrade/loadUploadProcess.tv',method:'post',cache:false,dataType:'plain',
		data:{entityId:entityId},
		success:function(res){
			if(res < UPLOAD_FINISHED){
				animateProgress(res);
			}else if(res == UPLOAD_FINISHED){
				animateProgressEnd();
				obj.callback();
			}else{
				top.showMessageDlg('@COMMON.tip@', ERR_200);
			}
		},
		error:function(){}
	});
}


function animateProgressStart(){
	window.DEFAULT_CIR_ICON =  $(".openWinHeader .rightCirIco").attr("class");
	$(".openWinHeader .rightCirIco").attr("class","rightCirIco cirIco").html('<div class="loadingCirIco"></div>');
}
function animateProgressEnd(){
	if(window.parent.progressTimer){window.parent.stopProgress();};
	animateProgress( UPLOAD_FINISHED );
	$(".openWinHeader .rightCirIco").empty().attr("class",DEFAULT_CIR_ICON);
}
function animateProgressError(){
	$(".uploadBar .uploadBarInner").css("backgroundcolor", "red");
	$(".openWinHeader .rightCirIco").empty().attr("class",DEFAULT_CIR_ICON);
}
function animateProgress(w){
	$(".uploadBar .uploadBarInner").width(w + "%");
}

