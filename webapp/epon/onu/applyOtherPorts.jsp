<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    CSS css.white.disabledStyle
    CSS css/animate
    IMPORT js.nm3k.Nm3kTools
</Zeta:Loader>
<style type="text/css">
.leftFloatUl input{ position:relative; top:3px;}
#console{ background:#f9f9f9; border:1px solid #ccc; padding:5px; height:155px; overflow:auto; display:none;}
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
#w1600{ width:1600px; position:absolute; top:0; left:0;}
#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
.forceHeight{ height:100px; overflow:auto;}
.successTd span, .failTd span{ padding:0px 5px 0px 5px; display:inline-block;}
#resultDiv{ height:350px; overflow:auto;}

</style>
<script type="text/javascript">
var portsBox,serviceBox;
var sourceIndex = "${sourceIndex}";
$(function(){
	//UNI的数据;
	var uniList = ${uniList};
	var portsData  = [];
	$.each(uniList,function(index,$port){
		portsData.add({
			label  : 'UNI '+$port.uniNo,
			value : $port.uniId,
			entityId : ${entityId},
			uniIndex : $port.uniIndex,
			uniNo : $port.uniNo
		});
	});
	
	portsBox = new Nm3kTools.createCheckBoxTable({
		renderId   : 'putPorts',
		lineNum    : 4,
		title      : '@ONU.uniList@',
		data       : portsData
	});
	serviceBox = new Nm3kTools.createCheckBoxTable({
		renderId : 'putConfig',
		lineNum  : 4,
		title    : '@ONU.selectConfig@',
		data     : [{
			label   : '@ONU.portEnable@',
			checked : true,
			value   : 'portEnable'
		},{
			label   : '@ONU.uniVlanInfo@',
			checked : true,
			value   : 'univlan'
		},{
			label   : '@ONU.uniSpeedDec@',
			checked : true,
			value   : 'uniportRate'
		},{
			label   : '@ONU.workMode@',
			checked : true,
			value   : 'portWorkMode'
		},{
			label   : '@ONU.uniUSUtgPriSimple@',
			checked : true,
			value   : 'untagPri'
		},{
			label   : '@ONU.macLearn@',
			checked : true,
			value   : 'macLearn'
		},{
			label   : '@ONU.stasitcEnable@',
			checked : true,
			value   : 'perfManage'
		}] 
	});
});//end document.ready;

var resultCache = [];
function batchApply(){
	if(portsBox.getSelectValue().length === 0){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
		    html: '@ONU.noSelectPort@'
		})
		return;
	}
	
	if(serviceBox.getSelectValue().length === 0){
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
		    html: '@ONU.noSelectConfig@'
		})
		return;
	}
	resultCache = [];
	//如果端口大于4个，则界面显示不下.强制固定端口table的div的高度;
	if(portsBox.data.length > 4){
		$("#putPorts").addClass("forceHeight");
	}
	//显示结果框;
	$("#console").css({display : 'block'});
	//隐藏应用按钮;
	$("#applyBtnLi a").attr({disabled : 'disabled'});
	$("#applyBtnLi").css({display:'none'});
	portsBox.setDisable(true);
	serviceBox.setDisable(true);
	
	var zetaDataBind = top.getFrame("uniserviceconfig-${onuId}").zetaDataBind;
	var $console = $("#console");
	var $ports = portsBox.getSelectObject();
	var $services = serviceBox.getSelectValue();
	/**递归发送请求*/
	iterateApply();
	
	function iterateApply(){
		if($ports.length > 0){
			var $port = $ports.shift();
			handleApply($port,$services);
		}else{
			$console.append('<p class="pB5"><b>@ONU.applyComplete@<a onclick="seeAllResult()" href="javascript:;" class="yellowLink edge5">[ @ONU.here@ ]</a>@ONU.showAllResult@<p></b>');
			scrollBottom();
		}
	}
	
	function handleApply(port, services){
		$console.append("<p>@ONU.runningApply@ UNI["+port.uniNo+"]</p>");
		scrollBottom();
		portsBox.setStatus({
			value : port.value,
			text : '@ONU.runningApply@',
			cls : 'blueLabel animated flash infinite'
		});
		var requestQuene = {};
		var length = 0;
		$.each(services,function($index,$service){
			var $data = zetaDataBind.returnConfig( $service );
			var $attributes = $data.attributes;
			$attributes.uniId = port.value;
			$attributes.entityId = port.entityId;
			$attributes.uniIndex = port.uniIndex;
			$attributes.sourceIndex = sourceIndex;
			requestQuene[$service] = $data;
			length++;
		});
		
		
		var successList = [];
		var faildList = [];
		for(var $param in requestQuene){
			var $data = requestQuene[$param];
			/**overide request params*/
			sendRequest($data.url, $data.attributes, $data.title);
		}
		
		function sendRequest(url,data,title){
			$.ajax({
				url:url,data:data,cache:false,
				success:function(){
					successList.add(title);
					futureCheck();
				},error:function(){
					faildList.add(title);
					futureCheck();
				}
			});
		}

		function futureCheck(){
			var $length = successList.length+faildList.length;
			if(length == $length){
				if(faildList.length  == 0){
					portsBox.setStatus({
						value : port.value,
						text : '@ONU.applySuccess@',
						cls : 'greenLabel'
					});
				}else{
					portsBox.setStatus({
						value : port.value,
						text : '@COMMON.fail@ '+faildList.length,
						cls : 'redLabel'
					});
				}
				successList.sort();
				faildList.sort();
				resultCache.add({
					text:"UNI"+port.uniNo,
					success: successList,
					successNum : successList.length,
					fail : faildList,
					failNum : faildList.length
				});
				$console.append("<p class='pB5'>UNI"+port.uniNo+"  <b class='lightGreenTxt pL5'>@COMMON.success@ "+successList.length+"</b>  <b class='lightRedTxt pL5'>@COMMON.fail@ "+faildList.length+"</b></p>");
				iterateApply();
				scrollBottom();
			}
		}
	}
}

//结果滚动到底部;
function scrollBottom(){
	$("#console").scrollTop(10000);
}

//close the dialog
function cancelClick() {
	window.top.closeModalDlg();
}
function nextPage(){
	$("#w1600").animate({left:-800},'fast');
}
function prevPage(){
	$("#w1600").animate({left:0},'fast');
}
function seeAllResult(){
	createResultTable(resultCache);
	nextPage();
}

function createResultTable(data){
	var tpl = new Ext.XTemplate(
		    '<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">',
		    	'<thead><tr>',
		    	    '<th width="130">@ONU.applyResult@</th>',
		    	    '<th>@COMMON.success@</th>',
		    	    '<th>@COMMON.fail@</th>',
		    	'</tr></thead>',
		    	'<tbody>',
		    	    '<tpl for=".">',
			    		'<tr>',
			    			'<td align="center">',
			    			    '<p class="pT5">{text}</p>',
			    			    '<tpl for="success">',
			    			    '</tpl>',
			    			    '<p class="pB5"><b class="lightGreenTxt pR5">@COMMON.success@ {successNum}</b><b class="lightRedTxt">@COMMON.fail@ {failNum}</b></p>',
			    			'</td>',
			    			'<td class="successTd">',
			    				'<tpl for="success">',
			    				    '<span>{.}</span>',
			    			    '</tpl>',
			    			'</td>',
			    			'<td class="failTd">',
			    				'<tpl for="fail">',
			    				    '<span>{.}</span>',
			    			    '</tpl>',
			    			'</td>',
			    		'</tr>',
			    	'</tpl>',
		    	'</tbody>',
		    '</table>'
		);
	$("#resultDiv").empty();
	tpl.overwrite(Ext.get('resultDiv'), data);
}
</script>
</head>
<body class="openWinBody">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div class="edge10">
					<div id="putPorts"></div>
					<div id="putConfig" class="pT10 pB10"></div>
					<div id="console"></div>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li id="applyBtnLi"><a href="javascript:;" class="normalBtnBig" onclick="batchApply()"><span><i class="miniIcoData"></i>@COMMON.apply@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
			</div>
			<div id="step1">
			    <div id="resultDiv" class="edge10"></div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" onclick="prevPage()"><span><i class="miniIcoArrLeft"></i>@COMMON.back@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>