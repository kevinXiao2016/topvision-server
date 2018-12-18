<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript" src ="/js/jquery/nm3kPassword.js"></script>
<Zeta:HTML>
	<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
	css css.reset
	library ext
	library jquery
	library zeta
	module platform
    import js.tools.ipText
</Zeta:Loader>
<head>
<script>
var nbiConfig = ${nbiConfigJson};
	//save config
	function okClick() {
	    var nbiIpAddress = getIpValue("alertNorthIp");
	    if(!checkedIpValue(nbiIpAddress)){
	        top.afterSaveOrDelete({        
				title: '@COMMON.tip@',        
				html: '<b class="orangeTxt">@RESOURCES/WorkBench.ipIsInvalid@</b>'    
			});
	        return;
	    } 
	    var nbiPort = $("#northPort").val().trim();
	    if(!checkPortInput()){
	        $("#northPort").focus();
	        return;
	    }
	    var nbiCommunity = $("#pwd").val().trim();
	    if(nbiCommunity == "" || nbiCommunity == null){
	        $("#pwd").focus();
	        return;
	    }
		var nbiHeartBeatSwitch = $("#switch").is(":checked");
		var nbiHeartBeatInterval = $("#northIntervel").val().trim();;
		if(!checkCycleInput()){
	        $("#northIntervel").focus();
	        return;
	    }
		var nbiHeartBeatLabel = $("#northLabel").val().trim();;
		if(nbiHeartBeatLabel == "" || nbiHeartBeatLabel == null){
		    $("#northLabel").focus();
	        return;
		}
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		$.ajax({
			url : '/fault/saveNorthBoundConfig.tv',
			type : 'POST',
			data : {
			    nbiIpAddress:nbiIpAddress,
			    nbiPort:nbiPort,
			    nbiCommunity:nbiCommunity,
			    nbiHeartBeatSwitch:nbiHeartBeatSwitch, 
			    nbiHeartBeatInterval:nbiHeartBeatInterval,
			    nbiHeartBeatLabel:nbiHeartBeatLabel
			},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@sys.tip@',
					html : '<b class="orangeTxt">@sys.saved@</b>'
				});
				cancelClick();
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
	    window.parent.closeWindow("modalDlg");
	}
	
	function sendTrapTest(){
	    var nbiIpAddress = getIpValue("alertNorthIp");
	    if(!checkedIpValue(nbiIpAddress)){
	        top.afterSaveOrDelete({        
				title: '@COMMON.tip@',        
				html: '<b class="orangeTxt">@RESOURCES/WorkBench.ipIsInvalid@</b>'    
			});
	        return;
	    } 
	    var nbiPort = $("#northPort").val().trim();;
	    if(!checkPortInput()){
	        $("#northPort").focus();
	        return;
	    }
	    var nbiCommunity = $("#pwd").val().trim();
	    if(nbiCommunity == "" || nbiCommunity == null){
	        $("#pwd").focus();
	        return;
	    }
	    var nbiHeartBeatSwitch = $("#switch").is(":checked");
		var nbiHeartBeatInterval = $("#northIntervel").val().trim();
		if(!checkCycleInput()){
	        $("#northIntervel").focus();
	        return;
	    }
		var nbiHeartBeatLabel = $("#northLabel").val().trim();
		if(nbiHeartBeatLabel == "" || nbiHeartBeatLabel == null){
		    $("#northLabel").focus();
	        return;
		}
		$.ajax({
			url : '/fault/sendNorthBoundTestTrap.tv',
			type : 'POST',
			data : {
			    nbiIpAddress:nbiIpAddress,
			    nbiPort:nbiPort,
			    nbiCommunity:nbiCommunity,
			    nbiHeartBeatSwitch:nbiHeartBeatSwitch, 
			    nbiHeartBeatInterval:nbiHeartBeatInterval,
			    nbiHeartBeatLabel:nbiHeartBeatLabel
			},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@sys.tip@',
					html : '<b class="orangeTxt">@sys.sendsuccess@</b>'
				});
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}

	Ext.onReady(function() {
	    initData();
	});
	
	function initData(){
	    //初始化IP
	    var alertNorthIp = new ipV4Input("alertNorthIp","span1");
	    alertNorthIp.width(200);
	    if(nbiConfig.nbiIpAddress !=null){
		    setIpValue("alertNorthIp",nbiConfig.nbiIpAddress);
	    }
	    
	    //初始化端口
	    if(nbiConfig.nbiPort!=null){
		    $("#northPort").val(nbiConfig.nbiPort);
	    }
	    
	    //初始化共同体名
	    if(nbiConfig.nbiCommunity!=null){
		    $("#pwd").val(nbiConfig.nbiCommunity)
	    }
	    
	    //初始化心跳开关
	    if(nbiConfig.nbiHeartBeatSwitch!=null){
	        $("#switch").attr("checked",nbiConfig.nbiHeartBeatSwitch);
	    }
	    
	    //初始化心跳间隔
	    if(nbiConfig.nbiHeartBeatInterval!= null ){
	        $("#northIntervel").val(nbiConfig.nbiHeartBeatInterval)
	    }
	    
	    //初始化心跳标识
	    if(nbiConfig.nbiHeartBeatLabel!= null ){
	        $("#northLabel").val(nbiConfig.nbiHeartBeatLabel)
	    }
	}
	function checkPortInput() {
		var reg = /^[0-9]\d*$/;
		var northPort = $.trim($("#northPort").val());
		
		if(northPort == null || northPort ==''){
			return false;
		}else if (reg.exec($("#northPort").val())
				&& parseInt($("#northPort").val()) <= 65535
				&& parseInt($("#northPort").val()) >= 0) {
			return true;
		} else {
			return false;
		}
    }
	
	function checkCycleInput() {
		var reg = /^[0-9]\d*$/;
		var northIntervel = $.trim($("#northIntervel").val());
		
		if(northIntervel == null || northIntervel ==''){
			return false;
		}else if (reg.exec($("#northIntervel").val())
				&& parseInt($("#northIntervel").val()) <= 1440
				&& parseInt($("#northIntervel").val()) >= 15) {
			return true;
		} else {
			return false;
		}
    }
</script>
	</head>
	<body class="openWinBody">
	<div class="edge10" style="margin-top: 20px;">
		<div class="zebraTableCaption" class="paramForm">
     		<div class="zebraTableCaptionTitle"><span>@ftp.basicParam@</span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt">@sys.nortchinterfaceip@</td>
		                <td><span id="span1" class="modifiedFlag w200"></span></td>
		           		<td class="rightBlueTxt">@sys.nortchinterfaceport@</td>
		                <td><input id="northPort" type="text" class="normalInput w200" tooltip = "0-65535"/></td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">@sys.snmpversion@</td>
		                <td class="pR10"><select class="normalSel"><option value="2">SNMP V2C</option></select></td>
		                <td class="rightBlueTxt">@sys.snmpcommunity@</td>
		                <td id="community">
							<script type="text/javascript">
								var pass1 = new nm3kPassword({
									id : "pwd",
									renderTo : "community",
									width : 160,
									firstShowPassword : true,
									disabled : false,
									maxlength : 32,
									toolTip : '@sys.inputcommunity@'
								})
								pass1.init();
							</script>
		                </td>
		            </tr>
		        </tbody>
		    </table>
		</div>		
		<div class="zebraTableCaption pT10 mT20" style="padding-top:20px">
   			<div class="zebraTableCaptionTitle"><span>@sys.heartbeattrpconfig@</span></div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			    <tbody>
			    	<tr>
		                <td class="rightBlueTxt">@sys.heartbeattrapswitch@</td>
		                <td><input type="checkbox" id="switch"/></td>
		            </tr>
			        <tr>
		                <td class="rightBlueTxt">@sys.heartbeattrapcycle@</td>
		                <td><input id="northIntervel" type="text" class="normalInput w200" tooltip = "15-1440"/>(@sys.Unit@:@sys.second@)</td>
		            </tr>
		            <tr>
		           		<td class="rightBlueTxt">@sys.heartbeattraplabel@</td>
		                <td><input id="northLabel" type="text" maxlength ="20" class="normalInput w200"/></td>
		            </tr>
			    </tbody>
			</table>
		</div>
		<div class="noWidthCenterOuter clearBoth" style="padding-top:20px">
		    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		    	<li><a href="javascript:;" class="normalBtnBig" onclick="sendTrapTest()"><span><i class="miniIcoSaveOK miniIcoInfo"></i>@sys.sendtesttrap@</span></a></li>
		        <li><a href="javascript:;" class="normalBtnBig" onclick="okClick()"><span><i class="miniIcoData"></i>@sys.save@</span></a></li>
		    	<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@sys.cancel@</span></a></li>
		    </ol>
		</div>
	</div>
	</body>
</Zeta:HTML>