window.onerror= function(e,a){
}
window.onunload =  function(){
	window.parent.stopProgress();
	window.close();
}

//标识是否是第一个数据，注意清零
var FirstFlag;

function show(cmcId){
	FirstFlag = true;
	window.tranparent = true;
	window.percent = 0;
	$('.percent').html('<b>0%</b>');
    jQuery.fn.anim_progressbar = function (aOptions) {
        // def values
        var iCms = 1000;//second
        var iMms = 60 * iCms;//minute
        var iHms = 3600 * iCms;//hour
        var iDms = 24 * 3600 * iCms;//day
        // def options
        var aDefOpts = {
            start: new Date(), // now
            finish: new Date().setTime(new Date().getTime() + 60 * iCms), // now + 60 sec
            interval: 10000
        }
        var aOpts = jQuery.extend(aDefOpts, aOptions);
        var vPb = this;

        // each progress bar
        return this.each(
            function() {
                var iDuration = aOpts.finish - aOpts.start;
                // calling original progressbar
                $(vPb).children('.pbar').progressbar();
                	window.parent.startProgess(function(){
                    	_progress(cmcId, vPb);
                    },aOpts.interval)
            }
        );
    }
    $('#progress').anim_progressbar({interval: 10000});
}

function _progress(cmcId, vPb){
	 window._fl = $.ajax({url:'/cmc/getCmcUpdateProgress.tv',method:'post',cache:false,dataType:'json',
		data:{cmcId:cmcId},
		success:function(res){
			if('error'==res.progress ){
				return;}
			if(FirstFlag){
				window.flagData = res.progress;
				FirstFlag = false;
				if (_percent <= 100) {
					$(vPb).children('.percent').html('<b>' + res.progress + '%</b>');
					$(vPb).children('.pbar').children('.ui-progressbar-value').css('width', res.progress + '%');
				}
				return;
			}
			if(window.flagData == res.progress)return;
			//记录本次的百分比
			var _percent = res.progress;
			if(_percent <= 100){
				_percent = Math.floor(_percent);
				//保证在取的过程中，数只增大不减小。
				percent = _percent > percent ? _percent : percent;
				$(vPb).children('.percent').html('<b>'+percent+'%</b>');
				$(vPb).children('.pbar').children('.ui-progressbar-value').css('width', percent+'%');
			}			
		},
		error:function(){}
	});
}