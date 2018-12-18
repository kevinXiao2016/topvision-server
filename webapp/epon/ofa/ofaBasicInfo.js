var basicInfoModule = {
	id : 'basicInfoConfig',
	items : [{
		id: 'platSN'
	},{
		id: 'hWVer'
	},{
		id: 'sWVer'
	},{
		id: 'moduleType'
	},{
		id: 'inputPower',
		showFormat: formatDivided10
	},{
		id: 'outputPower',
		showFormat: formatDivided10
	},{
		id: 'pump1BiasCurr'
	},{
		id: 'pump1Temp',
		showFormat: formatDivided10
	},{
		id: 'pump1Tec'
	},{
		id: 'pump2BiasCurr'
	},{
		id: 'pump2Temp',
		showFormat: formatDivided10
	},{
		id: 'pump2Tec'
	},{
		id: 'voltage5v',
		showFormat: formatDivided10
	},{
		id: 'voltage12v',
		showFormat: formatDivided10
	},{
		id: 'systemTemp',
		showFormat: formatDivided10
	},{
		id: 'outputAtt',
		showFormat: formatDivided10
	}]
}

var commonOptiTransModule = {
	id : 'commonOptiTransConfig',	
	items : [{
		id: 'deviceType'
	},{
		id: 'deviceName'
	},{
		id: 'vendorName'
	},{
		id: 'modelNumber'
	},{
		id: 'serialNumber'
	},{
		id: 'ipAddress'
	},{
		id: 'macAddress'
	},{
		id: 'deviceAcctStr'
	},{
		id: 'deviceMFD'
	}]	
}

// 初始化基本信息
function initBasicInfo(){
	$(".tabBody").css("display","none");
	$(".tabBody").eq(0).fadeIn();
	$('.tempUnit').html(tempUnit);
	
	if(commonOptiTransJson){
		var commonJson = JSON.parse(commonOptiTransJson);
		var items = commonOptiTransModule.items;
		$.each(items, function(index, item){
			$('#' + item.id).val(commonJson[item.id]);
		});
	}
	if(ofaBasicInfoJson){
		var basicInfoJson = JSON.parse(ofaBasicInfoJson);
		oldOutputAtt = basicInfoJson.outputAtt;
		var items = basicInfoModule.items;
		$.each(items, function(index, item){
			var showFormat = item.showFormat;
			if(showFormat){
				$('#' + item.id).val(showFormat(basicInfoJson[item.id]));
			}else{
				$('#' + item.id).val(basicInfoJson[item.id]);
			}
		});
	}
}

//从设备获取基本信息
function refreshBasicInfoConfig(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
        url:"/epon/ofa/refreshOFABasicInfo.tv",
        type:"post",
        data:{
        	"entityId":entityId
        },
        success:function (result){
        	top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
            });
        	window.location.reload();
        },
        error: function(message) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@COMMON.fetchBad@</b>'
            });
        }, 
    });
}

//校验数据
function checkTopOFAOutputAtt(outputAtt) {
	var isGood = true;
	outputAtt = outputAtt*10;
	if (!outputAtt || isNaN(outputAtt) || parseInt(outputAtt) < -5
			|| parseInt(outputAtt) > 30||!V.isInteger(outputAtt)||oldOutputAtt===outputAtt) {
		$("#outputAtt").focus();
		isGood = false;
		return;
	}
	return isGood;
}

// 修改OFA基本信息
function updateBasicInfoConfig() {
	if (checkTopOFAOutputAtt($("#outputAtt").val())) {
		window.top.showWaitingDlg("@COMMON.wait@", "@OFA.update@", 'ext-mb-waiting');
		$.ajax({
				url : '/epon/ofa/modifyOFABasicInfo.tv',
				type : 'post',
				data : {
					'entityId' : entityId,
					'outputAtt' : ($("#outputAtt").val()*10)
				},
				success : function(response) {
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html : '@OFA.updateThresholdConfigSuccess@'
					});
					window.location.reload();
				},
				error : function(response) {
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html : '@OFA.updateThresholdConfigFail@'
					});
				},
			});
		}
}
