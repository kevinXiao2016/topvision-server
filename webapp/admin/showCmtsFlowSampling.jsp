<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module report
    import js/jquery/jquery.wresize
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/tools/treeBuild_0.1.js"></script>
<style>
.reportFolderIcon {background-image: url(../images/report/folder16.gif);}
.reportIcon {background-image: url(../images/report/report16.gif);}
.icoA1, .icoA2, .icoA3, .icoA4, .icoA5, .icoA6, .icoB1, .icoB2, .icoB3, .icoB4, .icoB5, .icoB6, .icoB7, .icoB8, .icoB9, .icoB10, .icoB11, .icoB13_notBtn, .icoC1, .icoC2, .icoC3, .icoC4, .icoD1, .icoD2, .icoD3, .icoD4, .icoD5, .icoD6, .icoE1, .icoE2, .icoE3, .icoE4, .icoE5, .icoE6, .icoE7, .icoE8, .icoE9, .icoE10, .icoE11, .icoG1, .icoG2, .icoG3, .icoG4, .icoG5, .icoG6, .icoG7, .icoG8, .icoG9, .icoG10, .icoG11, .icoG12, .icoF1, .icoF2, .icoF3, .icoF4, .icoF5, .icoH1, .icoH2, .icoH3, .icoH4, .icoH5, .icoH6, .icoH7{display: inline-block;}
.filetree span.folder, .filetree span.file{display: inline-block;}
.wrapper{
	overflow: auto;
	padding:10px;
}
</style>
<script>
$(function(){
	//异步加载数据
    $.ajax({
		url: '/admin/loadCmtsFlowSampling.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(result) {
   		    var cmtsTypes = result.cmtsTypes,
   		 		flowCollectTypeArr =  result.flowCollectType.split(',');
   		    var typeStr = '<span><input id="{0}" type="checkbox" class="typeCbx"/><label>{1}</label></span>';
   		    $.each(cmtsTypes, function(i, cmtsType){
   		      	$('#typeTd').append(String.format(typeStr, cmtsType.typeId, cmtsType.displayName));  
   		    })
   		    $.each(flowCollectTypeArr, function(i, typeId){
   		     	$('#'+typeId).attr('checked', true);
   		    })
   		    $("#samplingSwitch").val(result.samplingSwitch);
   		    if(result.samplingSwitch == 1){
	   		 	$("#sampleInterval").val(result.sampleInterval);
	   		 	$("#sampleInterval").attr("disabled",false);
   		    }else if(result.samplingSwitch == 0){
   		     	$("#sampleInterval").val(-1);
   		     	$("#sampleInterval").attr("disabled",true);
   		    }
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
});

function saveClick(){
    var samplingSwitch = $("#samplingSwitch").val();
    var sampleInterval = $("#sampleInterval").val();
    if(samplingSwitch == 0){
        sampleInterval = -1;
    }
    var flowCollectTypeArr = [];
    
    $.each($(".typeCbx"), function(i, _typeCbx){
        if(_typeCbx.checked){
	        flowCollectTypeArr.push(_typeCbx.id);
        }
    })
    var flowCollectType= flowCollectTypeArr.join(',');
    
    $.ajax({
		url: '/admin/modifyCmtsFlowSampling.tv?sampleInterval='+sampleInterval+"&flowCollectType="+flowCollectType,
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(result) {
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function changeSwitch(){
    var st = $("#samplingSwitch").val();
    if(st==1){
        $("#sampleInterval").val(30);
        $("#sampleInterval").attr("disabled",false);
    }else if(st==0){
        $("#sampleInterval").val(-1);
        $("#sampleInterval").attr("disabled",true);
    }
}
</script>
</head>
<body class="openWinBody">
	<div class="wrapper">
		<h1>CMTS速率采样</h1>
		
		<div id="reportTree">
			<table class="dataTableRows zebra" width="80%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="220">速率采样开关：</td>
					<td class="pR10">			
						<select class="normalSel" id="samplingSwitch" onchange="changeSwitch()">
	                        <option value="1">开启</option>
	                        <option value="0">关闭</option>
		                </select>
		            </td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="220">速率采样间隔：</td>
					<td class="pR10"><input id = "sampleInterval" type="text" class="spinnerInput"></input>秒</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="220">CMTS设备类型：</td>
					<td id="typeTd" class="pR10"></td>
				</tr>
			</table>
		</div>
		<button id="save" onclick = "saveClick()">保存</button>
	</div>
</body>
</Zeta:HTML>