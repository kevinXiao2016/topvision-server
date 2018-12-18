window.onerror= function(e,a){
	//alert(e+'  '+a)
}
window.onunload =  function(){
	try{
		window.parent.stopProgress();
		window.close();
	}catch(e){
		//TODO 控制台
	}
}

//标识是否是第一个数据，注意清零
var FirstFlag;
var UPLOAD_FINISHED = 100;

function show(a,b,c,fileSize){
	FirstFlag = true;
	window.tranparent = true;
	window.percent = 0;
	animateProgressStart();
	window.parent.startProgess(function(){
    	_progress(a,b,c,fileSize);
    }, 15000 );
}

function _progress(a,b,c,fileSize,vPb){
	 window._fl = $.ajax({url:'/epon/checkFileSize.tv',method:'post',cache:false,dataType:'plain',
		data:{entityId:a,filePath:b,destFileName:c},
		success:function(res){
			if("fileNoStart" == res){return;}
			if('getFileSizeWrong'==res){return;}
			if(FirstFlag){
				window.flagData = res;
				FirstFlag = false;
				return;
			}
			if(window.flagData == res)return;
			var _percent = 100*res/fileSize;//记录本次的百分比
			_percent = Math.floor(_percent);
			percent = _percent > percent ? _percent : percent;//保证在取的过程中，数只增大不减小。
			animateProgress(_percent);
		},
		error:function(){}
	});
}


function animateProgressStart(){
	window.DEFAULT_CIR_ICON =  $(".openWinHeader .rightCirIco").attr("class");
	$(".openWinHeader .rightCirIco").attr("class","rightCirIco cirIco").html('<div class="loadingCirIco"></div>');
}
function animateProgressEnd(cssName){
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

