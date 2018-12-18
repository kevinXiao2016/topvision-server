<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">

/*************
 * 变量定义区**
 *************/
 var currentId = 0;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var map = ${qosDeviceBaseQosMapTable} ? ${qosDeviceBaseQosMapTable} : new Array();
var entityId = ${entityId};
var onuIndex = ${onuIndex};
var onuMapRule = 1;//从数据库获取的onuMapRule
var inputFlagNum;
var divClickFlag = [false,false,false,false,false,false,false,false];
//当前页面的cos值，save的时候使用，加载页面时进行初始化
var cosQueueList = [0,0,0,0,0,0,0,0];
//当前页面的tos值，save的时候使用，加载页面时进行初始化
var tosQueueList=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
//当前页面的dscp值，save的时候使用，加载页面时进行初始化
var dscpQueueList=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
var dscpTosFlag = false;//false:tos, true:dscp
var dscpTabNum = 0;//dscp的tab页页数
var changeFlag = new Array();

function refreshClick(){
	var params = {
		entityId : entityId
	};
	var url = '/epon/qos/refreshQosOnuMap.tv?r=' + Math.random();
	window.top.showWaitingDlg(I18N.COMMON.wait , I18N.QOS.fetchingOnuQosInfo , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.fetchOnuQosInfoError)
				return
			}
			window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.QOS.fetchQueneOk)
			window.location.reload()
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.fetchQueneError)
		},
		params : params
	});
}
/***************
 * 页面初始化区*
 *************/
$(document).ready(function(){
	var deviceBaseQosMapRuleIndex = map.deviceBaseQosMapRuleIndex;
	//设定模式
	$("#qosMapRule").val(deviceBaseQosMapRuleIndex);
	switch(parseInt(deviceBaseQosMapRuleIndex)){//保证switch的是int
		case 1:			
			//初始化变量
			$.each([0,1,2,3,4,5,6,7],function(i,n){
				//初始化COS列表
				cosQueueList[i] = map.deviceBaseQosMapOctetList[i];
				//初始化本页面，cos@QOS.priority" />类型
				//div id从0开始
				$("#cosInput"+i).val(I18N.QOS.quene + cosQueueList[i]);
			});					
			break;
		case 2:
			//初始化变量
			$.each([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],function(i,n){
				//初始化TOS列表
				tosQueueList[i] = map.deviceBaseQosMapOctetList[i];
				//div id从0开始
				$("#qosOnuDscpTos").find("#select"+i).val(cosQueueList[i]);
			});
			break;
		case 3:
			//初始化16个
			$.each([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],function(i,n){
				//初始化DSCP列表
				dscpQueueList[i] = map.deviceBaseQosMapOctetList[i];
				//div id从0开始
				$("#qosOnuDscpTos").find("#select"+i).val(cosQueueList[i]);
			});
			for(var i=0;i<map.deviceBaseQosMapOctetList.length;i++){
				dscpQueueList[i] = map.deviceBaseQosMapOctetList[i];						
			}
			break;
		case 0:
			$("#errorText").html( I18N.QOS.onuNotCfgRule )
			break
		default:
			$("#errorText").html( I18N.QOS.dataErrorRefresh ).attr("color","red")
			window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.QOS.dataErrorRefresh)
	}
	/**判断当前页面的模式，以作页面调整**/
	if(deviceBaseQosMapRuleIndex == 1){
		$("#qosMapRule").val(1);
		$("#qosOnuCos").show();
	}else if(deviceBaseQosMapRuleIndex == 2){
		$("#qosMapRule").val(2);
		$("#qosOnuDscpTos").show();
	}else if(deviceBaseQosMapRuleIndex == 3){
		$("#qosMapRule").val(3);
		$("#qosOnuDscpTos").show();
	}
	qosMapRuleChange();
});
/**
 * 保存配置
 */
function saveClick(){
	window.top.showWaitingDlg( I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
	var tempMapRule = $("#qosMapRule").val();
	switch(parseInt(tempMapRule)){
		case 1:
			saveCosConfig();
			break;
		case 2:
			saveTosConfig();
			break;
		case 3:
			saveDscpConfig();
			break;
		default:
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.dataErrorRefresh);
	}
}
/**
 * 保存Cos配置信息
 * 数据直接从页面中获取
 */
function saveCosConfig(){
	var result = new Array(8);
	for(var i=0; i<8; i++){
		//获取配置信息
		var opt = $("#cosInput"+i).val();	
		result[i] = opt.substring(2);
		if(0>parseInt(result[i]) || 7<parseInt(result[i]) || result[i].length!=1 || isNaN(parseInt(result[i]))){
			$("#cosInput"+i).focus();
			window.parent.closeWaitingDlg();
			return;
		}
	}
	result = result.join();
	$.ajax({
		url:'/epon/qos/modifyOnuQosMapRule.tv',
		method:'post',
		data:'entityId='+entityId+'&deviceBaseQosMapOctetList='+result+'&onuIndex='+onuIndex+'&onuQosMapRuleIndex='+1,//1:cos
		success:function(text){
			if('success'==text){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.configOk )
 				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry)
			}
		},
		error:function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry)
		}
	});
}

/**
 * 保存Tos配置信息
 * 数据直接从页面中获取
 */
function saveTosConfig(){
	var result = tosQueueList.join();
	$.ajax({
		url:'/epon/qos/modifyOnuQosMapRule.tv',
		method:'post',
		data:'entityId='+entityId+'&deviceBaseQosMapOctetList='+result+'&onuIndex='+onuIndex+'&onuQosMapRuleIndex='+2,//2:tos
		success:function(text){
			if('success'==text){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.configOk )
 				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry )
			}
		},
		error:function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry )
		}
	});
	
}
/**
 * 保存Dscp信息
 * 数据从对象中获取，故DSCP的值每次修改后都要提交给map对象
 */
function saveDscpConfig(){
	var result = dscpQueueList.toString();	
	result = result.replaceAll(I18N.QOS.quene,"");
	$.ajax({
		url:'/epon/qos/modifyOnuQosMapRule.tv',
		method:'post',
		data:'entityId='+entityId+'&deviceBaseQosMapOctetList='+result+'&onuIndex='+onuIndex+'&onuQosMapRuleIndex='+3,//3:dscp
		success:function(text){
			if('success'==text){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.configOk )
 				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry) 				
			}
		},
		error:function(){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.QOS.cfgErrorRetry)
		}
	});
	
}
/**
 * 根据页面的优先级模式展示对应的页面布局
 */
function qosMapRuleChange(){
	var onuRule = $("#qosMapRule").val();
	if(onuRule == 1){
		$("#qosOnuCos").show();
		$("#qosOnuDscpTos").hide();
		$(".DscpClass").hide();
		inputFocus(currentId);
	}else if(onuRule == 2){
		$("#qosOnuDscpTos").show();
		$("#qosOnuCos").hide();
		$(".DscpClass").hide();
		htmlChangeTos();
	}else if(onuRule == 3){
		$("#qosOnuDscpTos").show();
		$("#qosOnuCos").hide();
		$(".DscpClass").show();
		htmlChangeDscp();
		dscpBtClick(1);
	}
}
/**
 * 修改tos页面内容
 */
function htmlChangeTos(){
	dscpTosFlag = false;
	for(var j=0;j<16;j++){
		$("#select"+j).val(tosQueueList[j]);
		$("#sel"+j).html(I18N.QOS.priority + "TOS "+j);
	}
}
/**
 * html dscp内容修改
 */
function htmlChangeDscp(){
	dscpTosFlag = true;	
	for(var j = 0; j<16; j++){
		var tempDscpId = dscpTabNum*16 + j;
		$("#sel"+j).html(I18N.QOS.priority + "DSCP "+tempDscpId);
	}
}
//选择队列的输入框获得焦点
function inputFocus(id){
    window.currentId = id;
	$(".cosSelect").hide();
	$("#v"+id).show();
	inputFlagNum = id;
	var tempId = inputFlagNum>4 ? 4: inputFlagNum;
	divClickFlag[tempId] = true;
	divClickFlag[tempId+1] = true;
	divClickFlag[tempId+2] = true;
	divClickFlag[tempId+3] = true;
	var queueNo = 0;
	for(var k = tempId; k<tempId+4; k++){
		$("#cosSelect"+k+'1').text(I18N.QOS.quene + queueNo);
		queueNo++;
		$("#cosSelect"+k+'1').show();
		$("#cosSelect"+k+'2').text(I18N.QOS.quene + queueNo);
		queueNo++;
		$("#cosSelect"+k+'2').show();
	}
}
/**
 * 选择队列
 */
function cosDivClick(id,s){
	if(divClickFlag[id]){
		var divText = $("#cosSelect"+id+s).text();
		var oldText = $("#cosInput"+inputFlagNum).val();
		$("#cosInput"+inputFlagNum).val(divText);
		cosQueueList[inputFlagNum] = parseInt(divText.substring(2));
		$(".cosSelect").hide();
		divClickFlag = [false,false,false,false,false,false,false,false];
		var newText = $("#cosInput"+inputFlagNum).val();
		if(oldText != newText){
			$("#cosInput"+inputFlagNum).css('color','green');
		}
	}
}
/**
 * dscpBt点击
 */
function dscpBtClick(a){
	dscpTabNum = a - 1;
	htmlChangeDscp();
	for(var k=0; k<16; k++){
		var tempDscp = dscpQueueList[16*a+k-16];
		$("#select"+k).val(tempDscp);
	}
	$(".DscpClass").css("color","#00156e");
	$("#dscpBt"+a).css("color","red");
}
function keyup(s){
	tempText = $("#cosInput"+s).val();
	if(!isNaN(parseInt(tempText))){
		tempText = I18N.QOS.quene + tempText;
		tempText = tempText.substring(0,3);
		$("#cosInput"+s).val(tempText);
	}
	if(tempText.length!=3 || tempText.substring(0,2)!= I18N.QOS.quene || tempText.substring(2)>7){
		$("#cosInput"+s).css('color','red');
		$("#saveBt").attr("disabled",true);
	}else{
		$("#cosInput"+s).css('color', 'green');
		$("#saveBt").attr("disabled",false);
	}
}

/**
 * 选项修改后，需要将值缓存起来
 */
function selectChange(a){
	if(dscpTosFlag){
		dscpQueueList[a+16*dscpTabNum] = $("#select"+a).val();	
	}else{
		tosQueueList[a] = $("#select"+a).val();
	}
}
/**
 * 关闭当前页面
 */
function cancelClick(){
	window.parent.closeWindow('qosOnuMap');
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#dscpBt0").attr("disabled",false);
		$("#dscpBt1").attr("disabled",false);
		$("#dscpBt2").attr("disabled",false);
		$("#dscpBt3").attr("disabled",false);
		$("#dscpBt4").attr("disabled",false);
		R.saveBt.setDisabled(true);
	}
}
</script>
<style type="text/css">
.DscpClass{color:red;height:25px;border:0px;background-color:#e3efff;}
#dscpBt0{color:green;height:25px;border:0px;background-color:white;}
.cosSelect{border:1px solid #8bb8f3;width:60px;text-align:center;display:none;}
</style>
</head>
<body class=openWinBody onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@QOS.onuQosTable@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>

<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
		<tr style="height:35px;"><td style="width:100px;" align=center>@QOS.mapRule@
		</td><td align=left style="width:100px;">
			<select id="qosMapRule" class="normalSel w80" onchange=qosMapRuleChange()>
				<option value="1">COS</option>
				<option value="2">TOS</option>
				<option value="3">DSCP</option>
			</select>
		</td><td id=errorText style="width:220px;">@QOS.errorTextTip@
		</td></tr>
		<tr><td colspan=3>
			<button id=dscpBt0 >@QOS.queneMap@</button>
			<button class="DscpClass" id=dscpBt onclick="dscpBtClick(1)">0-15</button>
			<button class="DscpClass" id=dscpBt2 onclick="dscpBtClick(2)">16-31</button>
			<button class="DscpClass" id=dscpBt3 onclick="dscpBtClick(3)">32-47</button>
			<button class="DscpClass" id=dscpBt4 onclick="dscpBtClick(4)">48-63</button>
				<div id="qosOnuCos" style="display: none; valign: center;">
					<table class="withoutBorderBottom zebraTableRows">
						<tr>
							<td class="w220 rightBlueTxt">@QOS.priority@ COS 0</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput0" onkeyup=keyup(0) onfocus=inputFocus(0) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v0" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect01" onclick=cosDivClick(0,1) class="cosSelect"></div></td>
							<td><div id="cosSelect02" onclick=cosDivClick(0,2) class="cosSelect"></div></td>
						</tr>
						<tr  class="darkZebraTr">
							<td class="rightBlueTxt">@QOS.priority@ COS 1</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput1"
								onkeyup=keyup(1) onfocus=inputFocus(1) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v1" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect11" onclick=cosDivClick(1,1) class="cosSelect"></div></td>
							<td><div id="cosSelect12" onclick=cosDivClick(1,2) class="cosSelect"></div></td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@QOS.priority@ COS 2</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput2" onkeyup=keyup(2) onfocus=inputFocus(2) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v2" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect21" onclick=cosDivClick(2,1) class="cosSelect"></div></td>
							<td><div id="cosSelect22" onclick=cosDivClick(2,2) class="cosSelect"></div></td>
						</tr>
						<tr  class="darkZebraTr">
							<td class="rightBlueTxt">@QOS.priority@ COS 3</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput3"
								onkeyup=keyup(3) onfocus=inputFocus(3) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v3" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect31" onclick=cosDivClick(3,1) class="cosSelect"></div></td>
							<td><div id="cosSelect32" onclick=cosDivClick(3,2) class="cosSelect"></div></td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@QOS.priority@ COS 4</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput4"
								onkeyup=keyup(4) onfocus=inputFocus(4) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v4" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect41" onclick=cosDivClick(4,1) class="cosSelect"></div></td>
							<td><div id="cosSelect42" onclick=cosDivClick(4,2) class="cosSelect"></div></td>
						</tr>
						<tr  class="darkZebraTr">
							<td class="rightBlueTxt">@QOS.priority@ COS 5</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput5"
								onkeyup=keyup(5) onfocus=inputFocus(5) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v5" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect51" onclick=cosDivClick(5,1) class="cosSelect"></div></td>
							<td><div id="cosSelect52" onclick=cosDivClick(5,2) class="cosSelect"></div></td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@QOS.priority@ COS 6</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput6"
								onkeyup=keyup(6) onfocus=inputFocus(6) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v6" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect61" onclick=cosDivClick(6,1) class="cosSelect"></div></td>
							<td><div id="cosSelect62" onclick=cosDivClick(6,2) class="cosSelect"></div></td>
						</tr>
						<tr  class="darkZebraTr">
							<td class="rightBlueTxt">@QOS.priority@ COS 7</td>
							<td style="width: 40px;" align=center>@QOS.chart@</td>
							<td><input value="@QOS.quene@0" id="cosInput7"
								onkeyup=keyup(7) onfocus=inputFocus(7) style="width: 100px;"
								title="@QOS.selectQueneTip@" type=text /></td>
							<td><div id="v7" class="cosSelect" style="display: none;">
									<font><img src='/images/silk/reverse_blue.png'></font>
								</div></td>
							<td><div id="cosSelect71" onclick=cosDivClick(7,1) class="cosSelect"></div></td>
							<td><div id="cosSelect72" onclick=cosDivClick(7,2) class="cosSelect"></div></td>
						</tr>
					</table>
				</div>
				<div id="qosOnuDscpTos" style="display:none;valign:center;">
				<table>
					<tr>
					<td id="sel0" class="w120 rightBlueTxt">@QOS.priority@TOS 0
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select0" class="normalSel w80"  onchange="selectChange(0)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel8" class="rightBlueTxt">@QOS.priority@TOS 8
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select8" class="normalSel w80"  onchange="selectChange(8)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr  class="darkZebraTr"><td id="sel1" class="rightBlueTxt">@QOS.priority@TOS 1
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select1" class="normalSel w80"  onchange="selectChange(1)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel9" class="rightBlueTxt">@QOS.priority@TOS 9
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select9" class="normalSel w80"  onchange="selectChange(9)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr><td id="sel2" class="rightBlueTxt">@QOS.priority@TOS 2
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select2" class="normalSel w80"  onchange="selectChange(2)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel10" class="rightBlueTxt">@QOS.priority@TOS 10
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select10" class="normalSel w80"  onchange="selectChange(10)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr  class="darkZebraTr"><td id="sel3" class="rightBlueTxt">@QOS.priority@TOS 3
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select3" class="normalSel w80"  onchange="selectChange(3)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel11" class="rightBlueTxt">@QOS.priority@TOS 11
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select11" class="normalSel w80"  onchange="selectChange(11)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr><td id="sel4" class="rightBlueTxt">@QOS.priority@TOS 4
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select4" class="normalSel w80"  onchange="selectChange(4)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel12" class="rightBlueTxt">@QOS.priority@TOS 12
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select12" class="normalSel w80"  onchange="selectChange(12)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr  class="darkZebraTr"><td id="sel5" class="rightBlueTxt">@QOS.priority@TOS 5
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select5" class="normalSel w80"  onchange="selectChange(5)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel13" class="rightBlueTxt">@QOS.priority@TOS 13
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select13" class="normalSel w80"  onchange="selectChange(13)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr><td id="sel6" class="rightBlueTxt">@QOS.priority@TOS 6
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select6" class="normalSel w80"  onchange="selectChange(6)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel14" class="rightBlueTxt">@QOS.priority@TOS 14
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select14" class="normalSel w80"  onchange="selectChange(14)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
					<tr  class="darkZebraTr"><td id="sel7" class="rightBlueTxt">@QOS.priority@TOS 7
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select7" class="normalSel w80"  onchange="selectChange(7)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td><td id="sel15" class="rightBlueTxt">@QOS.priority@TOS 15
					</td><td style="width:30px;" align=center>@QOS.chart@
					</td><td style="width:100px;" align=left>
						<select id="select15" class="normalSel w80"  onchange="selectChange(15)">
							<option value='0'>@QOS.quene@ 0</option>
							<option value='1'>@QOS.quene@ 1</option>
							<option value='2'>@QOS.quene@ 2</option>
							<option value='3'>@QOS.quene@ 3</option>
							<option value='4'>@QOS.quene@ 4</option>
							<option value='5'>@QOS.quene@ 5</option>
							<option value='6'>@QOS.quene@ 6</option>
							<option value='7'>@QOS.quene@ 7</option>
						</select>
					</td></tr>
				</table>
			</div>
		</td></tr>
	</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="saveClick()">@COMMON.saveCfg@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>