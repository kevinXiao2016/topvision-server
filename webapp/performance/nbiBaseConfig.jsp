<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript" src ="/js/jquery/nm3kPassword.js"></script>
<Zeta:HTML>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
	module  performance
	IMPORT js/jquery/Nm3kTabBtn
	CSS css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.scrollBtn{ cursor:pointer;}
</style>
<script type="text/javascript">
var putModeValue;
	$(function(){
		putModeValue = ${nbiBaseConfig.mode} - 1;
		var tab1 = new Nm3kTabBtn({
		    renderTo:"putMode",
		    callBack:"changeMode",
		    tabs:["PULL","PUSH"],//pull是1，push是2;
		    selectedIndex : putModeValue
		});
		tab1.init();
		changeMode(putModeValue);
		$("#ftpAddr").val("${nbiBaseConfig.ftpAddr}");
		$("#ftpPort").val(${nbiBaseConfig.ftpPort});
		$("#ftpUser").val("${nbiBaseConfig.ftpUser}");
		$("#ftpPwd").val("${nbiBaseConfig.ftpPwd}");
		$("#filePath").val("${nbiBaseConfig.filePath}");
		$("#recordMax").val(${nbiBaseConfig.recordMax});
		$("#fileSaveTime").val(${nbiBaseConfig.fileSaveTime});
		$("#nbiAddr").val("${nbiBaseConfig.nbiAddr}");
		$("#nbiPort").val(${nbiBaseConfig.nbiPort});
		$("#encoding").val("${nbiBaseConfig.encoding}");
		
		
		var imgSrc;
		if("${nbiBaseConfig.nbiSwitch}" == 1){
			imgSrc = "on"
			$("#nbiAddress_port").css({visibility : 'visible'});
		}else{
			imgSrc="off"
			$("input,select").attr("disabled","disabled");
		}
		$("#scrollBtn").attr({alt : "${nbiBaseConfig.nbiSwitch}"});
		$("#scrollBtn").attr({src : '../../images/performance/'+ imgSrc +'.png'});
		
		//点击on或者off;
		$(".scrollBtn").click(function(){
			var $me = $(this),
		        //如果没有传参数，则去读取img上alt值，如果传了一个参数，则通过参数判断;
	            alt = $me.attr("alt");
			switch(alt){
			case '1':
				$("input,select").attr("disabled","disabled");
				$me.attr({alt : '0', src : '../../images/performance/off.png'});
				break;
			case '0':
				$("input,select").removeAttr("disabled");
				$me.attr({alt : '1', src : '../../images/performance/on.png'});
				break;
			}
		}); 
		
	});//end document.ready;
	
	
	
	//切换模式，push模式下，不显示第二个tbody内的内容;
	function changeMode(num){
		putModeValue = num;
		var $tbody1 = $("#contentTable tbody:eq(0)"),
		    $tbody2 = $("#contentTable tbody:eq(1)"),
		    $tbody3 = $("#contentTable tbody:eq(2)");
		$("#contentTable tbody").css({display : ""});
		
		switch (num){
			case 0:
				$tbody3.css({display : "none"});
			break;
			case 1:
				//$tbody2.css({display : "none"});
			break;
		}
	};
	
	function checkPortInput(value) {
		var reg = /^[0-9]\d*$/;
		if(value == null || value ==''){
			return false;
		}else if (parseInt(value) <= 65535 && parseInt(value) >= 0) {
			return true;
		} else {
			return false;
		}
    }
	
	function checkIpInput(value) {
		reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		if(value == null || value ==''){
			return false;
		}else if (reg.test(value)){
			return true;
		} else {
			return false;
		}
    }
	
	function checkFtpRule(value) {
		reg = /^\w{3,15}$/;
		if(value == null || value ==''){
			return false;
		}else if (reg.test(value)){
			return true;
		} else {
			return false;
		}
    }
	
	//点击保存按钮;
	function saveClick(){
		var scrollBtn = $("#scrollBtn").attr("alt"); //北向接口开关，值分别是on 和 off; 
		var nbistate = scrollBtn == 1;
		var ftpAddr = $("#ftpAddr").val().trim();
		var ftpPort = $("#ftpPort").val().trim() === ""?0:$("#ftpPort").val().trim();
		var ftpPwd = $("#ftpPwd").val().trim();
		var ftpUser = $("#ftpUser").val().trim();
		var filePath = $("#filePath").val().trim();
		var recordMax = $("#recordMax").val().trim();
		var fileSaveTime = $("#fileSaveTime").val().trim();
		var nbiAddr = $("#nbiAddr").val().trim();
		var nbiPort = $("#nbiPort").val().trim() === ""?0:$("#nbiPort").val().trim();
		var encoding = $("#encoding").val();
		var reg = /^[0-9]\d*$/;
		if(nbistate && !reg.test(recordMax) || recordMax > 10000 ||recordMax < 1){
			$("#recordMax").focus();
			return false;
		}
		if(nbistate){
			if(putModeValue == 0){
				if(!checkIpInput(nbiAddr) && scrollBtn == 1){
					$("#nbiAddr").focus();
				    return;
				}
				if(!checkPortInput(nbiPort) && scrollBtn == 1){
					 $("#nbiPort").focus();
				        return;
				}
				var reg = /^[0-9]\d*$/;
				if(!reg.test(fileSaveTime) || fileSaveTime > 3 ||fileSaveTime < 1){
					$("#fileSaveTime").focus();
					return false;
				}
			}else{
				if(!checkIpInput(ftpAddr)){
					$("#ftpAddr").focus();
				    return;
				}
				if(!checkPortInput(ftpPort)){
					$("#ftpPort").focus();
				    return;
				}
				if(!checkFtpRule(ftpUser)){
					$("#ftpUser").focus();
				    return;
				}
				if(!checkFtpRule(ftpPwd)){
					$("#ftpPwd").focus();
				    return;
				}
				if(!checkIpInput(nbiAddr) && scrollBtn == 1){
					$("#nbiAddr").focus();
				    return;
				}
				if(!checkPortInput(nbiPort) && scrollBtn == 1){
					 $("#nbiPort").focus();
				        return;
				}
			}
		}
		$.ajax({
			url : '/nbi/saveNbiBaseConfig.tv',
			type : 'POST',
			data : {
				mode:putModeValue + 1,
				ftpAddr:ftpAddr,
				ftpPort:ftpPort,
				ftpUser:ftpUser,
				ftpPwd:ftpPwd, 
				filePath:filePath,
				recordMax:recordMax,
				fileSaveTime:fileSaveTime,
				nbiAddr:nbiAddr,
				nbiPort:nbiPort,
				encoding:encoding,
				nbiSwitch:scrollBtn
			},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
				});
				cancelClick();
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	
	//打开修改指标配置窗口
	function openExportWin(){
		window.parent.addView("nbiTargetConfig", '@Performance.nbiEditIndex@', "icoD1", "/nbi/showNbiTargetConfig.tv");
		cancelClick();
	}
	
	//打开配置多周期导出窗口;
	function openIndexWin(){
		window.top.createDialog('nbiExportConfig', '@Performance.nbiPerferformanceExport@', 800, 500, '/nbi/showNbiExportConfig.tv', null, true, true);
	}
	
	//点击取消按钮;
	function cancelClick() {
	    window.parent.closeWindow("nbiBaseConfig");
	}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@platform/sys.nbiPerformanceConfig@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
    	<table id="contentTable" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
    		<tbody>
    			<tr>
    				<td class="rightBlueTxt">@Performance.nbiSwitch@</td>
             		<td>
             			<img src="../../images/performance/off.png" alt="0" class="scrollBtn" id="scrollBtn" />
             		</td>
                	<td class="rightBlueTxt">@Performance.mode@:</td>
                 	<td id="putMode"  colspan="3"></td>
            	</tr>
    		</tbody>
            <tbody>
             	<tr class="darkZebraTr">
             		<td class="rightBlueTxt" width="140">@Performance.dataMenu@:</td>
                 	<td width="190">
                 		<input type="text" id="filePath" maxlength = 32 tooltip="@tip.max32@" class="w180 normalInput" />
                 	</td>
                	<td width="140" class="rightBlueTxt">@Performance.singleFileMaxRecord@:</td>
                 	<td>
                 		<input type="text" id="recordMax" maxlength = 5 tooltip="@tip.recordMax@" class="w180 normalInput" />
                 	</td>
             	</tr>
             	<tr>
                	<td class="rightBlueTxt">@Performance.maxSaveTime@:</td>
                 	<td>
                 		<input type="text" id="fileSaveTime" maxlength = 1 tooltip="@tip.fileSaveTime@" class="w180 normalInput" />
                 	</td>
                 	<td class="rightBlueTxt">@Performance.encoding@:</td>
                 	<td>
                 		<select class="normalSel"  id="encoding">
							<option value="GBK">GBK</option>
							<option value="UTF-8">UTF-8</option>
						</select>
                 	</td>
             	</tr>
             	
             	<tr class="darkZebraTr" id="nbiAddress_port">
                 	<td class="rightBlueTxt">@Performance.nbiInterfaceAddress@</td>
                 	<td>
                 		<input type="text" id="nbiAddr" tooltip="@platform/ftp.ipRule@" class="w180 normalInput" />
                 	</td>
                 	<td class="rightBlueTxt">@Performance.nbiPort@</td>
                 	<td>
                 		<input type="text" id="nbiPort" maxlength = 5 tooltip="@platform/ftp.portRule@" class="w180 normalInput" />
                 	</td>
             	</tr>
        	</tbody>
        	<tbody>
             	<tr class="darkZebraTr">
             		<td class="rightBlueTxt" width="140">@Performance.FTPadress@:</td>
    	            <td width="190">
    	            	<input type="text" id="ftpAddr" tooltip="@platform/ftp.ipRule@" class="w180 normalInput" />
    	            </td>
                	<td class="rightBlueTxt" width="140">@Performance.FTPport@:</td>
                 	<td>
                 		<input type="text" id="ftpPort" maxlength = 5 tooltip="@platform/ftp.portRule@" class="w180 normalInput" />
                 	</td>
             	</tr>
             	<tr>
             		<td class="rightBlueTxt">@Performance.FTPuser@:</td>
                 	<td>
                 		<input type="text" id="ftpUser" maxlength = 15 tooltip="@platform/ftp.nameAndPwdRule@" class="w180 normalInput" />
                 	</td>
                	<td class="rightBlueTxt">@Performance.FTPpassword@:</td>
                 	<td>
                 		<input type="text" id="ftpPwd" maxlength = 15 tooltip="@platform/ftp.nameAndPwdRule@" class="w180 normalInput" />
                 	</td>
             	</tr>
             </tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
            <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		        <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
            	<li><a href="javascript:;" class="normalBtnBig" onclick="openIndexWin()"><span><i class="miniIcoManager"></i>@Performance.nbiPerferformanceExport@</span></a></li>
            	<li><a href="javascript:;" class="normalBtnBig" onclick="openExportWin()"><span><i class="miniIcoEdit"></i>@Performance.nbiEditIndex@</span></a></li>
		        <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		    </ol>
		</div>
	</div>
</body>
</Zeta:HTML>