<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
 	library ext
    library jquery
    library zeta
	module cmc
	css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 5.0;
var _frequencyMax = 65.0;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var cmcId = <s:property value="cmcId"/>;
var cmcMac = '<s:property value="cmcMac"/>';
var entityId = '<s:property value="entityId"/>';
var productType ='<s:property value="productType"/>';
var channelId = '<s:property value="channelId"/>';
var channelIndex = '<s:property value="channelIndex"/>';
var cmcPortId = <s:property value="cmcPortId"/>;
var upChannelWidth = <s:property value="cmcUpChannelBaseShowInfo.channelWidth"/>;
var upChannelFrequery = '${cmcUpChannelBaseShowInfo.channelFrequency/1000000}';
var upChannelModulationProfile = <s:property value="cmcUpChannelBaseShowInfo.channelModulationProfile"/>;
var ifAdminStatus = '<s:property value="cmcUpChannelBaseShowInfo.ifAdminStatus"/>';
var docsIf3SignalPower = '${cmcUpChannelBaseShowInfo.docsIf3SignalPower/10}';
var channelMode = '${cmcUpChannelBaseShowInfo.channelExtMode}';
var channelListObject = ${channelListObject};
var downChannelListObject = channelListObject.downList;
var upChannelListObject = channelListObject.upList;
var productType = <s:property value="productType"/>;
var rangingBackoffStart = '${cmcUpChannelBaseShowInfo.channelRangingBackoffStart}';
var rangingBackoffEnd = '${cmcUpChannelBaseShowInfo.channelRangingBackoffEnd}';
var txBackoffStart ='${cmcUpChannelBaseShowInfo.channelTxBackoffStart}';
var txBackoffEnd = '${cmcUpChannelBaseShowInfo.channelTxBackoffEnd}';
var supportFunction = ${supportFunction};
console.log(supportFunction)
var powerMax;
var powerMin;
var REJECTED = 65536;
//频率提示范围 
var frequencyMinTip;
var frequencyMaxTip;

//初始化电平的最大值 
powerMax = parsePower(26);
powerMin = parsePower(-13);

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == '@spectrum/spectrum.dbuv@' && powerValue != ''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

/* 
//add by flackyang 2013-09-09
//初始跳频组id
var chnlGroupId ='${chnlGroupInfo.chnlGroupId}';
//初始第一备份调制方式
var secondProf = '${chnlGroupInfo.chnlSecondaryProf}';
//初始第二备份调制方式
var thirdProf = '${chnlGroupInfo.chnlTertiaryProf}';
//待添加的第一备份调制方式和第二备份调制方式选项
var optionObj = [
  	{priority:30,optionString:'<option value="1" priority="30">QPSK-Fair-Scdma</option>'},
  	{priority:25,optionString:'<option value="2" priority="25">QAM16-Fair-Scdma</option>'},
  	{priority:15,optionString:'<option value="3" priority="15">QAM64-Fair-Scdma</option>'},
  	{priority:10,optionString:'<option value="4" priority="10">QAM256-Fair-Scdma</option>'},
  	{priority:29,optionString:'<option value="5" priority="29">QPSK-Good-Scdma</option>'},
  	{priority:24,optionString:'<option value="6" priority="24">QAM16-Good-Scdma</option>'},
  	{priority:14,optionString:'<option value="7" priority="14">QAM64-Good-Scdma</option>'},
  	{priority:9,optionString:'<option value="8" priority="9">QAM256-Good-Scdma</option>'},
  	{priority:13,optionString:'<option value="9" priority="13">QAM64-Best-Scdma</option>'},
  	{priority:10,optionString:'<option value="10" priority="9">QAM256-Best-Scdma</option>'},
  	{priority:29,optionString:'<option value="11" priority="29">QPSK-Good-Atdma</option>'},
  	{priority:24,optionString:'<option value="12" priority="24">QAM16-Good-Atdma</option>'},
  	{priority:14,optionString:'<option value="13" priority="14">QAM64-Good-Atdma</option>'},
  	{priority:9,optionString:'<option value="14" priority="9">QAM256-Good-Atdma</option>'}
  ];
 */
 
function listTypeChanged(){
	
}
//数字验证
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
}
function isFloatNumber(number){
	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$|^\-\d+\.\d{1}$|^\-\d+$|^0\.\d{1}$|^0$/;
	return reg.test(number);
}
function is0_6BitsNumber(number){
	var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
    return reg.test(number);
}
function isNumber(number){
	var reg =  /^[0-9]+$/;///^[1-9]+[0-9]*]*$/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('upStreamConfig');
}

//updated by flackyang 2013-09-09
function changeCheck(){
	var changeTag = false;
	/* 	
	if(upChannelWidth != $("#upChannelWidth").val() || upChannelFrequery != $("#upChannelFrequery").val()||
			upChannelModulationProfile != $("#profile").val()||docsIf3SignalPower!= $("#docsIf3SignalPower").val()||
			ifAdminStatus!= $("#ifAdminStatus").val()|| rangingBackoffStart!=$("#rangingBackoffStart").val()||
			rangingBackoffEnd!=$("#rangingBackoffEnd").val()||txBackoffStart!=$("#txBackoffStart").val()||
			txBackoffEnd!=$("#txBackoffEnd").val()||channelMode !=$("#channelExtMode").val() || 
			chnlGroupId != $('#chnlGroup').val() || thirdProf != $('#chnlTertiaryProf').val() || 
			secondProf != $('#chnlSecondaryProf').val()
			){
			changeTag = true; 
	*/
	if(upChannelWidth != $("#upChannelWidth").val() || upChannelFrequery != $("#upChannelFrequery").val()||
		upChannelModulationProfile != $("#profile").val()||docsIf3SignalPower!= $("#docsIf3SignalPower").val()||
		ifAdminStatus!= $("#ifAdminStatus").val()|| rangingBackoffStart!=$("#rangingBackoffStart").val()||
		rangingBackoffEnd!=$("#rangingBackoffEnd").val()||txBackoffStart!=$("#txBackoffStart").val()||
		txBackoffEnd!=$("#txBackoffEnd").val()||
		(($("#channelExtMode").css("display")!="none")&&(channelMode !=$("#channelExtMode").val())))
	{
		changeTag = true;
	}else{
		changeTag = false;
	}
	if(changeTag == true){
	    if(operationDevicePower){
			R.saveBtn.setDisabled(false);
	    }
	}else{
		R.saveBtn.setDisabled(true);
	}
}
function modulationProfileChange(){
    var txt = $("#profile  option:selected").text().toUpperCase();
    if(txt.indexOf("SCDMA") != -1){
        $("#upChannelType-input").val("SCDMA");
    }else{
        $("#upChannelType-input").val("ATDMA");
    }
}
//上行信道冲突检测
function frequencyCheck(min, max, frequency, width){
    min = Math.round(min);
    max= Math.round(max);
	
	var i = 0;
	if(frequency < min || frequency > max)
		return false;
	for(i = 0; i < upChannelListObject.length; i++){
		if(frequency <= upChannelListObject[i].channelFrequency)
			break;
	}
	if(i==0){
		if(upChannelListObject.length!=0){
			if((frequency+width/2)> (upChannelListObject[i].channelFrequency - upChannelListObject[i].channelWidth/2))
				return false;
		}
	}else if( i < upChannelListObject.length){
		if(frequency+width/2 > upChannelListObject[i].channelFrequency - upChannelListObject[i].channelWidth/2||
		frequency-width/2 < upChannelListObject[i-1].channelFrequency + upChannelListObject[i-1].channelWidth/2)
			return false;
	}else{
		if(frequency-width/2 < upChannelListObject[i-1].channelFrequency + upChannelListObject[i-1].channelWidth/2)
			return false;
	}
	return true;	
}
//增加上行信道与下行信道中心频率冲突检测:上行信道中心频率可配置范围5-65(MHz)，下行信道中心频率可配置范围52-1002(MHz).
//进入52-65MHz之间时，需要判断下行信道是否占用该区域，如果占用，则判断是否冲突
//两种处理方式：使用第二种
//如果冲突，则通过修改信道的tip上下限来限制输入范围--问题：与命令行显示的输入范围不符(实际相符)
//如果冲突，则返回冲突提示--问题：提示信息与可设置范围不符

function upDownFrequencyCheck(upFrequency,width){
	var maxUF = upFrequency+width/2;
	var minUF = upFrequency-width/2;
	
	if(maxUF>=52*1000000){//进入识别区,上行信道频宽小于下行信道频宽
		var i;
		for(i = 0; i < downChannelListObject.length; i++){//由于downChannelListObject数据已存在排序ASC
			if((maxUF >= downChannelListObject[i].docsIfDownChannelFrequency- downChannelListObject[i].docsIfDownChannelWidth/2&&
					maxUF<=downChannelListObject[i].docsIfDownChannelFrequency+ downChannelListObject[i].docsIfDownChannelWidth/2)||
					(minUF >= downChannelListObject[i].docsIfDownChannelFrequency- downChannelListObject[i].docsIfDownChannelWidth/2&&
							minUF<=downChannelListObject[i].docsIfDownChannelFrequency+ downChannelListObject[i].docsIfDownChannelWidth/2)){
				return downChannelListObject[i].docsIfDownChannelId;	
// 				_frequencyMin = 
// 				break;
			}
		}
		
		/* if(i==0){
			if(downChannelListObject.length!=0){
				if(maxUF> (downChannelListObject[i].docsIfDownChannelFrequency - upChannelListObject[i].docsIfDownChannelWidth/2)){
					_frequencyMin = downChannelListObject[i].docsIfDownChannelFrequency - upChannelListObject[i].docsIfDownChannelWidth/2;
					return false;
				}
			}
		}else if( i < downChannelListObject.length){
			if(maxUF > downChannelListObject[i].docsIfDownChannelFrequency - downChannelListObject[i].docsIfDownChannelWidth/2||
				minUF < downChannelListObject[i-1].docsIfDownChannelFrequency + downChannelListObject[i-1].channelWidth/2){
	
				return false;
			}
		}else{
			if(minUF < downChannelListObject[i-1].docsIfDownChannelFrequency + downChannelListObject[i-1].docsIfDownChannelWidth/2){
	
				return false;
			}
		} */
	}
	return -1;
}
function onWidthChange(domObject){
    var width = parseInt($(domObject).val());
    frequencyMinTip = _frequencyMin + width/2000000;
    frequencyMaxTip = _frequencyMax - width/2000000;
    //inputFocused('upChannelFrequery', frequencyMinTip + '-' + frequencyMaxTip, 'iptxt_focused');
    $("#upChannelFrequery").attr("toolTip",frequencyMinTip + '-' + frequencyMaxTip);//toolTip="frequencyMinTip + '-' + frequencyMaxTip"
}
function saveClick() {
	//频道带宽
	object = Zeta$('upChannelWidth').value.trim();
	var channelWidth = new Number(object);

    //电平
	object = Zeta$('docsIf3SignalPower').value.trim();
	if ( object == '' || !isFloatNumber(object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@text.powerFormatTip@");
		return;
	}else if(object< powerMin || object > powerMax){
		window.top.showMessageDlg("@COMMON.tip@", "@text.powerRangeTip@"+ powerMin +  '-' + powerMax);
		return;
	}

	//中心频率
	object = Zeta$('upChannelFrequery').value.trim();
	var frequency = Math.round(new Number(object) * 1000000);
	if ( object != '' && !is0_6BitsNumber(object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@text.frequencyFormatTip@");
		return;

	}else if(object<frequencyMinTip || object > frequencyMaxTip){
		window.top.showMessageDlg("@COMMON.tip@", "@text.frequencyRangeTip@" + frequencyMinTip + '-' + frequencyMaxTip);
		return;
	}
	if(ifAdminStatus==1||$("#ifAdminStatus").val()==1){
		if( !frequencyCheck(_frequencyMin * 1000000,_frequencyMax * 1000000,frequency,channelWidth)){
			window.top.showMessageDlg("@COMMON.tip@", "@text.conflictTip@");
			return;
		}
		var cid = upDownFrequencyCheck(frequency,channelWidth);
		//alert(cid)
		if(cid!=-1){
			window.top.showMessageDlg("@COMMON.tip@", String.format("@text.conflictWithDSTip@", cid));
			return;
		};
	}
	//测距，数据偏移
	object = Zeta$('rangingBackoffStart').value.trim();
	//alert(!(object>=0&&16>=object)+"/"+(object != '' && !isNumber(object)));
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.rangingBackoffStart@"+"@CHANNEL.numberLimit@");
		return;
	}
	var start = parseInt(new Number(object));
	object = Zeta$('rangingBackoffEnd').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.rangingBackoffEnd@"+"@CHANNEL.numberLimit@");
		return;
	}
	var end = parseInt(new Number(object));
	if(start > end){
	    window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.rangingBackoffStart@" + "@CHANNEL.notBiggerThan@ " +
	            "@CHANNEL.rangingBackoffEnd@");
	    return;
	}
	object = Zeta$('txBackoffStart').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.txBackoffStart@"+"@CHANNEL.numberLimit@");
		return;
	}
	start = parseInt(new Number(object));
	object = Zeta$('txBackoffEnd').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.txBackoffEnd@"+"@CHANNEL.numberLimit@");
		return;
	}
	end = parseInt(new Number(object));
	if(start > end){
        window.top.showMessageDlg("@COMMON.tip@", "@CHANNEL.txBackoffStart@" + "@CHANNEL.notBiggerThan@ " + 
                "@CHANNEL.txBackoffEnd@");
        return;
    }
	/* 
	//add by flackyang 2013-09-09
	//提交时将第一备份调制方式和第二备份调制方式恢复，以便能提交值
	$('#chnlSecondaryProf').attr("disabled", false);
	$('#chnlTertiaryProf').attr("disabled", false);
	//备份调制方式必须同时设置或同时取消，不可只设置一个
	if($("#chnlTertiaryProf").val() == REJECTED && $("#chnlSecondaryProf").val() != REJECTED ||
			$("#chnlSecondaryProf").val() == REJECTED && $("#chnlTertiaryProf").val() != REJECTED){
		window.top.showMessageDlg("@COMMON.tip@", "@CMC.CHANNEL.cancelAndApply@");
		return;
	}
	*/
	var tipStr = "@text.confirmModifyTip@";
	window.top.showOkCancelConfirmDlg("@COMMON.tip@", tipStr, function (type) {
	if(type=="ok"){ 
	window.parent.showWaitingDlg("@COMMON.wait@", "@text.configuring@", 'ext-mb-waiting');
	$.ajax({
 	      url: '/cmc/channel/modifyUpStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+"&productType="+productType,
 	      type: 'post',
 	      data: jQuery(formChanged).serialize(),
 	      success: function(response) {
			//window.top.getFrame('cmcId1000').onRefreshClick();
			//在topvision-server/webapp/epon/onuView.jsp中找到
			//response = eval("(" + response + ")");
   	    	if(response.message == "success"){
   	    	    window.parent.closeWaitingDlg();
   	    	    //window.parent.showMessageDlg("@COMMON.tip@", "@text.modifySuccessTip@");
   	    		 top.afterSaveOrDelete({
    				title: '@COMMON.tip@',
    				html: '<b class="orangeTxt">@text.modifySuccessTip@</b>'
    			});
			 }else{
			    window.parent.closeWaitingDlg();
			    window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
			 }
			window.parent.getFrame("entity-" + cmcId).onRefreshClick();
			cancelClick();
		}, error: function(response) {
		    window.parent.closeWaitingDlg();
		    window.parent.showMessageDlg("@COMMON.tip@", "@text.modifyFailureTip@");
		}, cache: false
	});
	}
	});
}
/* 
//初始化跳频组设置选择  add by flackyang 2013-09-09
var	GROUP_OPTION_FMT = '<option value="{0}">{1}</option>';
function initData(){
	$.ajax({
		url : '/ccmtsspectrumgp/loadAllDeviceGroup.tv?r=' + Math.random(),
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType : 'json',
		success : function(json) {
			$('#chnlGroup').append(String.format(GROUP_OPTION_FMT, 0, "@CMC.CHANNEL.cancelGp@"));
			for(var i=0; i<json.length;i++){
				var groupId = json[i].groupId;
				if( groupId ){
					$('#chnlGroup').append(String.format(GROUP_OPTION_FMT, groupId, groupId));
				}
			}
			$('#chnlGroup').val(chnlGroupId);
		},
		error : function(json) {
			window.top.showErrorDlg();
		},
		cache : false
	});
}

//在选择主备份调制方式时动态改变第一备份调制方式的可选项      add by flackyang 2013-09-09
function changeSecondOption(){
	//先获取主备份调制方式的优先级
	var mainPriority = $("#profile option:selected").attr("priority");
	//删除第一备份调制方式下所有的选项(除了无备份调制方式外)
	$("#chnlSecondaryProf option:gt(0)").remove();
	//给第一备份添加可选项
	$.each(optionObj,function(){
		if(this.priority > mainPriority){
			$(this.optionString).appendTo("#chnlSecondaryProf");
		}
	});
	//选中第一备份调制方式的值
	if( secondProf ){
		$('#chnlSecondaryProf').val (secondProf );
	}
	//根据第一备份调制方式的值同步更改第二备份调制方式 的可选项
	changeThirdOption();
}

//在选择第一备份调制方式时更改第二备份调制方式 的可选项      add by flackyang 2013-09-09
function changeThirdOption(){
	//先获取第一备份调制方式的值
	var firstPriority = $("#chnlSecondaryProf option:selected").attr("priority");
	//删除第二备份调制方式下所有的选项(除了无备份调制方式外)
	$("#chnlTertiaryProf option:gt(0)").remove();
	//给第二备份调制方式添加可选项
	$.each(optionObj,function(){
		if(this.priority > firstPriority){
			$(this.optionString).appendTo("#chnlTertiaryProf");
		}
	}); 
	//第二备份调制方式
	if( thirdProf ){
		$('#chnlTertiaryProf').val( thirdProf );
	}
}

//恢复信道跳频状态     add by flackyang 2013-09-09
function  resetToStatic(){
	if(chnlGroupId && chnlGroupId != 0){
		window.top.showWaitingDlg("@COMMON.waiting@", "@CMC.CHANNEL.onReseting@" , 'ext-mb-waiting');
		$.ajax({
			url : '/cmc/channel/resertChnlGpToStatic.tv?r=' + Math.random(),
			data : {
				entityId: entityId,
				cmcMac : cmcMac,
				channelId : channelId
			},
			success : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@CMC.CHANNEL.resetSuccess@");
			},
			error : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@CMC.CHANNEL.resetFail@");
			}
		});
	}else{
		window.parent.showMessageDlg("@COMMON.tip@", "@CMC.CHANNEL.notApplyGp@");
		return;
	}
}
 */
$(document).ready(function(){
	frequencyMinTip = _frequencyMin + upChannelWidth/2000000;
    frequencyMaxTip = _frequencyMax - upChannelWidth/2000000;
    $("#upChannelFrequery").attr("toolTip",frequencyMinTip + '-' + frequencyMaxTip);
  	/*   
  	//加载可选的跳频组     add by flackyang 2013-09-09
	initData();
	//在选择主备份调制方式时动态改变第一备份调制方式的可选项
	changeSecondOption();
	//在选择第一备份调制方式时更改第二备份调制方式 的可选项
	changeThirdOption();
	
	//当信道关联了跳频组时,不能更改备份调制方式     add by flackyang 2013-09-09
	if(chnlGroupId && chnlGroupId != 0){
		$('#chnlSecondaryProf').attr("disabled", true);
		$('#chnlTertiaryProf').attr("disabled", true);
	}
	*/
	//控制表格变色
	$("#zebraTable tr:odd").addClass("darkZebraTr");
	
	//power tootip
	 var powerTooltip = powerMin + '-' + powerMax;
	 $("#docsIf3SignalPower").attr("toolTip",powerTooltip);
})

function authLoad(){
	/*权限控制，根据select disableInput和input normalInputDisabled非readonly属性。找到需要控制的参数
	  注意：页面上readonly属性的是需要传递到后台的。不能disable 2013年11月26日15:29:05
	  by bryan
	*/
	var sel = $("select.disableInput");
    var inp = $("input.normalInputDisabled[readonly!=readonly]");
    
    /*
    	取消readonly的输入框的toolTip提示信息
    */
    $("input.normalInputDisabled[readonly=readonly]").removeAttr("ToolTip");
    
    sel.attr("disabled",true);
    inp.attr("disabled",true);
    
    /*增加由于是私有MIB实现，部分可以显示，部分无法显示的处理。关于私有MIB实现建议在base版本中添加
    	1，在deviceVersionFunction文件中添加该参数及参数属性hidden（可以参考channelExtModeName）
    	2，在页面中该参数添加class（别忘记其所在的tr和td）
    	3，在下面添加hidden处理
    	4，如果该参数是后面可以修改的，则在savecheck时，增加非空判断
    	2013年11月26日15:47:03
    	by bryan
    */
    
	if(supportFunction){
        for(var param in supportFunction){
        	var val = supportFunction[param];        	
			if(param == "modulationProfile"){
				var fileV = $("#profile").val();				
				$("#profile option").remove();
				var re = val.split(";");
				$.each(re,function(i,v){
					var ar = v.split(",");
					var s = "<option value='"+ar[0]+"'>"+ar[1]+"</option>";
					$("#profile").append(s);
				})
				$("#profile").val(fileV);
				
			}else{
				var re = $("." + param);
	            if(val == "true" || val == true){
	                re.removeClass("disableInput normalInputDisabled");
	                re.attr("disabled",false);
	            }else if(val=="hidden"){
	            	//$("td[class*="+param+"]").css("display","none");
	            	$("."+param).css("display","none");
	            }	
			}            
        }
        //
        $("#profile").val(upChannelModulationProfile);
    }
    
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#rangingBackoffStart").attr("disabled",true);
		$('#saveBtn').attr('disabled',true);
	}
}
</script>
<title>@text.upChannelConfig@</title>
</head>
<body class="openWinBody"  onload="authLoad()">
	<div class="openWinHeader">
		<div class="openWinTip">
			@CHANNEL.id@:
			<span class="orangeTxt bigNumTip"><s:property value="CmcUpChannelBaseShowInfo.channelId"/></span>
		</div>
		<div class="rightCirIco upArrCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 pT10">
		<form name="formChanged" id="formChanged">
			<input 	name="cmcUpChannelBaseShowInfo.channelIndex"
								style="width: 150px; align: left" type="hidden"
								value=<s:property value="cmcUpChannelBaseShowInfo.channelIndex"/> />
								
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="zebraTable">
				<tr>
					<td class="rightBlueTxt" width="150">
						@CHANNEL.id@
					</td>
					<td width="200">
						<input  class="normalInputDisabled w165"
								name="cmcUpChannelBaseShowInfo.channelId"
								value=<s:property value="cmcUpChannelBaseShowInfo.channelId" /> readonly="readonly" />
					</td>
					<td class="rightBlueTxt" width="150">
						@CHANNEL.modulationProfile@
					</td>
					<td>
						<select id="profile" class="normalSel w165"
								name="cmcUpChannelBaseShowInfo.channelModulationProfile"
								onclick="changeCheck()"
								onchange="modulationProfileChange()">
						</select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt channelExtModeName">
						@CHANNEL.extChannelMode@
					</td>
					<td class="channelExtModeName">
						 <select  id="channelExtMode" class="normalSel w165 disableInput channelExtModeName" 
	                                name="cmcUpChannelBaseShowInfo.channelExtMode"
	                                onclick="changeCheck()">
                                   <option value="2"
                                       <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==2">selected</s:if>>V2</option>
                                   <option value="3"
                                       <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==3">selected</s:if>>V3</option>
                                   <!-- <option value="4"
                                       <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==4">selected</s:if>>Other</option> -->
                           </select>
					</td>
					<td class="rightBlueTxt">
						@CMC.label.bandwidth@(MHz)
					</td>
					<td>
						<select id="upChannelWidth" class="normalSel w165" name="cmcUpChannelBaseShowInfo.channelWidth"
								onchange="onWidthChange(this)" onclick="changeCheck()">
									<option value="1600000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==1600000">selected</s:if>>1.6</option>
									<option value="3200000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==3200000">selected</s:if>>3.2</option>
									<option value="6400000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==6400000">selected</s:if>>6.4</option>
								</select>
					</td>
				</tr>
				<tr >
					<td class="rightBlueTxt">
						@CHANNEL.frequency@(MHz)
					</td>
					<td>
						<input class="normalInput w165" id="upChannelFrequery"
								name="cmcUpChannelBaseShowInfo.docsIfUpChannelFrequencyForunit"
								value="<s:property value='%{cmcUpChannelBaseShowInfo.channelFrequency*1.0/1000000}'/>"
								onkeyup="changeCheck()" />
					</td>
					<td class="rightBlueTxt">
						@CCMTS.channel.adminStatus@
					</td>
					<td>
						<select id="ifAdminStatus" name="cmcUpChannelBaseShowInfo.ifAdminStatus" class="normalSel w165"
							onclick="changeCheck()">
	                        <option value="1" <s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>>@CMC.select.open@</option>
	                        <option value="2" <s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>>@CMC.select.close@</option>
	                      	<!-- <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==3">selected</s:if>>testing</option> -->
	                    </select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">
						@CHANNEL.rangingBackoffStart@
					</td>
					<td>
						<input class='normalInput w165 rangingBackoff normalInputDisabled' id="rangingBackoffStart" toolTip='0-15'
								name="cmcUpChannelBaseShowInfo.channelRangingBackoffStart" 
								value='<s:property value="cmcUpChannelBaseShowInfo.channelRangingBackoffStart"/>'
								onkeyup="changeCheck()" />
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.rangingBackoffEnd@
					</td>
					<td>
						<input class="normalInput w165 rangingBackoff normalInputDisabled" id="rangingBackoffEnd" toolTip='0-15'
								name="cmcUpChannelBaseShowInfo.channelRangingBackoffEnd"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelRangingBackoffEnd"/>'
								onkeyup="changeCheck()" />
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">
						@CHANNEL.txBackoffStart@
					</td>
					<td>
						<input class="normalInput w165 txBackoff normalInputDisabled" type=text id=txBackoffStart
								name="cmcUpChannelBaseShowInfo.channelTxBackoffStart" toolTip='0-15'
								value='<s:property value="cmcUpChannelBaseShowInfo.channelTxBackoffStart"/>'
								onkeyup="changeCheck()" />
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.txBackoffEnd@
					</td>
					<td>
						<input class="normalInput w165 txBackoff normalInputDisabled" id="txBackoffEnd"
								name="cmcUpChannelBaseShowInfo.channelTxBackoffEnd" toolTip='0-15'
								value='<s:property value="cmcUpChannelBaseShowInfo.channelTxBackoffEnd"/>'
								onkeyup="changeCheck()" />
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">
						@CHANNEL.power@(@{unitConfigConstant.elecLevelUnit}@)
					</td>
					<td>
						<input id="docsIf3SignalPower" class="normalInput w165"
                                name="cmcUpChannelBaseShowInfo.upChannelPower"
                                onkeyup="changeCheck()" toolTip=''
                                value=<s:property value="cmcUpChannelBaseShowInfo.upChannelPower"/> />
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.channelType@
					</td>
					<td>
						<input id="upChannelType-input" class="normalInputDisabled w165"
								name="cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName"
								value='<s:property value="cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName"/>'
								readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<s:if test="#request.cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName=='SCDMA'">
					<td class="rightBlueTxt">
						@CHANNEL.activeCodes@
					</td>
					<td>
						<input id='activeCode' class="normalInputDisabled w165"
								name="cmcUpChannelBaseShowInfo.channelScdmaActiveCodes"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaActiveCodes"/>'
								toolTip='0|64..66|68..70|72|74..78|80..82|84..88|90..96|98..100|102|104..106|108|110..112|114..126|128' 
								readonly="readonly"/>
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.codesPerSlot@
					</td>
					<td>
						<input class="normalInputDisabled w165" id="codesPerSlot"
								name="cmcUpChannelBaseShowInfo.channelScdmaCodesPerSlot"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaCodesPerSlot"/>'
								toolTip='0,2-32' readonly="readonly"/>
					</td>
				</tr>
				<tr>					
					<td class="rightBlueTxt">
						@CHANNEL.frameSize@
					</td>
					<td colspan="3">
						<input class="normalInputDisabled w165" id="frameSize"
								name="cmcUpChannelBaseShowInfo.channelScdmaFrameSize"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaFrameSize"/>'
								toolTip='0-32' 
								readonly="readonly"/>
					</td>
					</s:if>	
				</tr>
			</table>
		</form>
	</div>
	
	<Zeta:ButtonGroup>
		<%-- <Zeta:Button onclick="resetToStatic()" icon="miniIcoEquipment">@CMC.CHANNEL.resertGp@</Zeta:Button> --%>
		<Zeta:Button onclick="saveClick()" icon="miniIcoSave" id="saveBtn" disabled="true">@BUTTON.apply@</Zeta:Button>
		<Zeta:Button onclick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	
</body>
</Zeta:HTML>
