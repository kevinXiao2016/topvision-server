$(function(){
    if(testProjectSupport){
        tab1 = new Nm3kTabBtn({
            renderTo:"putTab",
            callBack:"showTab",
            tabs:["@COMMON.all@","@CATV.OutputGainControl@","@CATV.Alarmthresholdconfig@"]
        });
        tab1.init();
    }
    
    //加载基本数据
    initData();
    //加载权限
    initPower();
});

function initData(){
    $.ajax({
        url:"/onu/catv/getOnuCatvConfigInfo.tv?onuId="+onuId,
        method:"post",cache: false,dataType:'json',
        success:function (json) {
            onuIndex = json.onuIndex;
            switchCATV = json.onuCatvOrConfigSwitch;
            renderCATVSwitch(switchCATV);
            $("input:radio[name='gainControlType']").each(function (){
                if($(this).val() == json.onuCatvOrConfigGainControlType){
                    $(this).attr("checked","checked")
                }
            })
            $("input[name='gainControlType']").change(function(){
                radioFn();
            })
            radioFn();
            $("#agcUpValue").val(json.onuCatvOrConfigAGCUpValue/10);
            $("#agcRange").val(json.onuCatvOrConfigAGCRange/10);
            $("#mgcTxAttenuation").val(json.onuCatvOrConfigMGCTxAttenuation/10);
            
            $("#inputLO").val(json.onuCatvOrConfigInputLO/10);
            $("#inputHI").val(json.onuCatvOrConfigInputHI/10);
            $("#outputLO").val(json.onuCatvOrConfigOutputLO/10);
            $("#outputHI").val(json.onuCatvOrConfigOutputHI/10);
            $("#voltageLO").val(json.onuCatvOrConfigVoltageLO/10);
            $("#voltageHI").val(json.onuCatvOrConfigVoltageHI/10);
            $("#temperatureLO").val(json.onuCatvOrConfigTemperatureLO/10);
            $("#temperatureHI").val(json.onuCatvOrConfigTemperatureHI/10);
            $("#collectTime").html(json.collectTime);
        }
    });
}

function initPower(){
    if(!refreshDevicePower){
        $("#refreshBtn").attr("disabled",true);
    }
    if(!operationDevicePower){
        $("#modifyConfig").attr("disabled",true);
    }
    if(!testProjectSupport){
        $("#shoresholdConfig").hide();
    }
}

function renderCATVSwitch(){
    var tpl = new Ext.Template(
        '<ol class="leftFloatOl">',
            '<li class="blueTxt pL20 pT5">@CATV.switch@@COMMON.maohao@</li>',
            '<li class="pT3 pL5">{0:this.renderCATV}</li>',
        '</ol>');  
    tpl.renderCATV = function(v){
        if(v == 2){
            return '<img class="scrollBtn" alt="off" src="/images/speOff.png" />';
        }else{
            return '<img class="scrollBtn" alt="on" src="/images/speOn.png" />';
        }
    }
    tpl.compile();
    tpl.append('putTab',[switchCATV]);
    
    $(".scrollBtn").on("click",function(){
        var $me = $(this),
            alt = $me.attr("alt");
        
        switch(alt){
            case 'on':
                $me.attr({alt:'off', src:'/images/speOff.png'});
                switchInput(0);
            break;
            case 'off':
                $me.attr({alt:'on', src:'/images/speOn.png'});
                switchInput(1);
            break;
        }       
    });
     
    switchInput(switchCATV);
}

function showTab(index){
    if(index == 0){
        $("div.jsShow").css({display:"block"});
    }else{
        $("div.jsShow").css({display:"none"});
        $("div.jsShow").eq(index-1).fadeIn();
    }
}

//点击CATV开关,控制所有的input是否disabled;系统一加载也要判断一次;
function switchInput(num){
	if(num == 1){
		$(":text").removeAttr("disabled");
		$(":radio").removeAttr("disabled");
	}else{
		$(":text").attr("disabled","disabled");
		$(":radio").attr("disabled","disabled");
	}
}

function radioFn(){
	var v = $("input[name='gainControlType']:checked").val();
	showHideTbody(v);
}
//通过判断单选按钮的value，来显示或者隐藏tbody的内容;
function showHideTbody(value){

	switch(value){
		case '0':
			$(".jsTbody tbody:eq(1)").css({display:'none'});
			$(".jsTbody tbody:eq(2)").css({display:'none'});
		break;
		case '1':
			$(".jsTbody tbody:eq(1)").css({display:''});
			$(".jsTbody tbody:eq(2)").css({display:'none'});
		break;
		case '2':
			$(".jsTbody tbody:eq(1)").css({display:'none'});
			$(".jsTbody tbody:eq(2)").css({display:''});
		break;
	}
}

function refreshOnuCatvConfig(){
    window.top.showWaitingDlg("@COMMON.wait@", "@CATV.RefreshCATVconfiginfo@", 'ext-mb-waiting');
    $.ajax({
        url:"/onu/catv/refreshOnuCatvConfig.tv?entityId="+entityId+"&onuIndex="+onuIndex,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@CATV.RefreshCATVconfiginfosuccess@</b>'
            });
            window.location.reload();
        },error:function(){
            window.parent.showMessageDlg("@COMMON.tip@", "@CATV.RefreshCATVconfiginfofail@");
        }
    });
}

function saveConfig(){
	var s = $(".scrollBtn").attr("alt");
	if(s == "on"){
		switchCATV = 1;
	}else{
		switchCATV = 2;
	}
	
    var gainControlType = $("input[name='gainControlType']:checked").val();
    if(gainControlType == 1){
    	var agcUpValue = $("#agcUpValue").val()*10;
    	if(onuType=="EPON"){
    		if(!checkValue(agcUpValue,-60,20)){
        		$("#agcUpValue").focus();
        		return;
        	}	
    	}else{
    		if(!checkValue(agcUpValue,-80,20)){
        		$("#agcUpValue").focus();
        		return;
        	}
    	}    	
    	var agcRange = $("#agcRange").val()*10;
    	if(!checkValue(agcRange,0,100)){
    		$("#agcRange").focus();
    		return;
    	}
    }else if(gainControlType == 2){
    	var mgcTxAttenuation = $("#mgcTxAttenuation").val()*10;
    	if(!checkValue(mgcTxAttenuation,0,200)){
    		$("#mgcTxAttenuation").focus();
    		return;
    	}
    }
    if(testProjectSupport){
        var inputLO = $("#inputLO").val()*10;
        if(!checkValue(inputLO,-400,80)){
            $("#inputLO").focus();
            return;
        }
        var inputHI = $("#inputHI").val()*10;
        if(!checkValue(inputHI,-400,80)){
            $("#inputHI").focus();
            return;
        }
        var outputLO = $("#outputLO").val()*10;
        if(!checkValue(outputLO,0,1500)){
            $("#outputLO").focus();
            return;
        }
        var outputHI = $("#outputHI").val()*10;
        if(!checkValue(outputHI,0,1500)){
            $("#outputHI").focus();
            return;
        }
        var voltageLO = $("#voltageLO").val()*10;
        if(!checkValue(voltageLO,0,65530)){
            $("#voltageLO").focus();
            return;
        }
        var voltageHI = $("#voltageHI").val()*10;
        if(!checkValue(voltageHI,0,65530)){
            $("#voltageHI").focus();
            return;
        }
        var temperatureLO = $("#temperatureLO").val()*10;
        if(!checkValue(temperatureLO,-1280,1280)){
            $("#temperatureLO").focus();
            return;
        }
        var temperatureHI = $("#temperatureHI").val()*10;
        if(!checkValue(temperatureHI,-1280,1280)){
        	window.parent.showMessageDlg("@COMMON.tip@", "@CATV.ModifyCATVconfigfail@");
            return;
        }
        if(inputLO==0&&inputHI==0&&outputLO==0&&outputHI==0&&voltageLO==0&&voltageHI==0&&temperatureLO==0&&temperatureHI==0){
        	//如果全部设置为0，表示要恢复默认
        }else{
        	//如果不是全部为0，则要校验上下门限的大小关系
        	if(inputLO >= inputHI||outputLO >= outputHI||voltageLO >= voltageHI||temperatureLO >= temperatureHI){
        		window.parent.showMessageDlg("@COMMON.tip@", "@CATV.Thresholdcheck@");
        		return;
        	}
        }
        
    }
    window.top.showWaitingDlg("@COMMON.wait@", "@CATV.ModifyCATVconfig@", 'ext-mb-waiting');
    $.ajax({
        url:"/onu/catv/modifyOnuCatvConfig.tv",
        data: {
            onuId: onuId,
            entityId: entityId,
            onuIndex: onuIndex,
            switchCATV: switchCATV,
            gainControlType: gainControlType,
            agcUpValue: agcUpValue,
            agcRange: agcRange,
            mgcTxAttenuation:mgcTxAttenuation,
            inputLO: inputLO,
            inputHI: inputHI,
            outputLO: outputLO,
            outputHI: outputHI,
            voltageLO: voltageLO,
            voltageHI: voltageHI,
            temperatureLO: temperatureLO,
            temperatureHI: temperatureHI
        },
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@CATV.ModifyCATVconfigsuccess@</b>'
            });
        },error:function(){
            window.parent.showMessageDlg("@COMMON.tip@", "@CATV.ModifyCATVconfigfail@");
        }
    });
}

function checkValue(value,min,max){
    if(isNaN(value)||!(Math.floor(value)==value)){
        return false;
    }
    if(value>max || value<min){
        return false;
    }
    return true;
}
