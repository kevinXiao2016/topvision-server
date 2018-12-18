/********************************************/
/*--------------------入口-------------------*/
/********************************************/
Ext.onReady(function(){
	$('#overThresholdTimes').val(overThresholdTimes);
	$('#notOverThresholdTimes').val(notOverThresholdTimes);
	$('#overThresholdPercent').val(overThresholdPercent);
	$('#notOverThresholdPercent').val(notOverThresholdPercent);
});

function reset(){
	$.ajax({
        url:"/cmcSpectrum/loadSpectrumAlertConfig.tv",
        type:"post",
        success:function (result){
        	$('#overThresholdTimes').val(result.overThresholdTimes);
        	$('#notOverThresholdTimes').val(result.notOverThresholdTimes);
        	$('#overThresholdPercent').val(result.overThresholdPercent);
        	$('#notOverThresholdPercent').val(result.notOverThresholdPercent);
        },
        error: function(message) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@COMMON.resetFail@</b>'
            });
        }, 
        cache: false     
    });
}

function save(){
	 if(!isDigit($("#overThresholdTimes").val()) || parseInt($("#overThresholdTimes").val()) <= 0  || parseInt($("#overThresholdTimes").val()) > 65535){
		 $("#overThresholdTimes").focus();
	     return;
	 } 
	 if(!isDigit($("#notOverThresholdTimes").val()) || parseInt($("#notOverThresholdTimes").val()) <= 0 || parseInt($("#notOverThresholdTimes").val()) > 65535){
		 $("#notOverThresholdTimes").focus();
	     return;
	 }
	 if(!isDigit($("#overThresholdPercent").val()) || parseInt($("#overThresholdPercent").val()) < 0 || parseInt($("#overThresholdPercent").val()) > 100 ){
		 $("#overThresholdPercent").focus();
		 return;
	 }
	 if(!isDigit($("#notOverThresholdPercent").val()) || parseInt($("#notOverThresholdPercent").val()) < 0 || parseInt($("#notOverThresholdPercent").val()) > 100 ){
		 $("#notOverThresholdPercent").focus();
		 return;
	 }
	 if(parseInt($("#overThresholdPercent").val()) < parseInt($("#notOverThresholdPercent").val())){
		 $("#overThresholdPercent").focus();
		 return;
	 }
	 $.ajax({
		 url: '/cmcSpectrum/modifySpectrumAlertConfig.tv',
	     type: 'post',
	     data: {
	    	 overThresholdTimes: $("#overThresholdTimes").val(),
	    	 notOverThresholdTimes: $("#notOverThresholdTimes").val(),
	    	 overThresholdPercent: $("#overThresholdPercent").val(),
	    	 notOverThresholdPercent: $("#notOverThresholdPercent").val()
	     },
	     success: function(response){
	    	 top.afterSaveOrDelete({
                 title: '@COMMON.tip@',
                 html: '@spectrum.saveAlertConfigSuccess@'
             });
	     },
		 error: function(response){
		  	top.afterSaveOrDelete({
		   		title: '@COMMON.tip@',
		   		html: '@spectrum.saveAlertConfigFail@'
		   	});
		 }, 
		 cache: false
     });
}

/********************************************/
/*---------------判断是否为纯数字-------------*/
/********************************************/
function isDigit(s){ 
    var patrn=/^[0-9]{1,20}$/; 
    if(!patrn.exec(s)){
        return false; 
    }else{
        return true;
    }
}

