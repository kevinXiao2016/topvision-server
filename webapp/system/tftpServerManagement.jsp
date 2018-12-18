<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<title><fmt:message bundle="${resources}" key="ftp.ftpServer" /></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

.required {
	font: 0.8em Verdana !important;
	color: #f68622;
}

input {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
}

fieldset {
	padding: 4px;
	margin: 5px 5px 5px 2px;
	border: 1px solid #7F9EB9;
	/* border: 1px solid #1E90FF; */
}

#content-tit{
	height: 50px !important;
}

#content-tit span:FIRST-CHILD {
	position: fixed;
	top: 20px;
	left: 20px;
}

#content-tit span img {
	position: absolute;
	top: 10px;
	right: 5px;
}

.normalInput{
	width: 240px;
	padding-top: 2px;
	border: 1px solid #7F9EB9;
	background: #FFF;
	height: 18px;
}

.normalInput:HOVER{
	border: 1px solid #1E90FF;
}

.normalInput:FOCUS{
	background: #FFFFBE;
	border: 1px solid #1E90FF;
}

.paramForm p {
	width: 298px;
	clear: left;
	padding: 4px;
	padding-left: 122px;
	text-align: left;
}

.paramForm p label {
	float: left;
	margin-left: -122px;
	width: 110px;
	padding: 4px 4px 0;
	text-align: right;
}

.combo_div{
	margin-top: 2px;
	margin-bottom: 2px;
}

.combo_div label{
	float: left;
	width: 110px;
	padding: 4px 4px 4px;
	text-align: right;
}

#rootPath_div div{
	position: absolute;
	top: 133px;
	left: 137px;
}


.status p {
	margin: 6px 0px;
	text-align: center;
}

#start_p{
	width: 298px;
	clear: left;
	padding: 4px;
	padding-left: 122px;
	text-align: left;
}

#start_p label {
	float: left;
	margin-left: -122px;
	width: 110px;
	padding: 4px 4px 0;
	text-align: right;
}

#startStatus_span{
	margin-right: 100px;
}

.status .status_span{
	vertical-align: middle !important;
}

.status_span label{
	width: 14px;
	height: 14px;
	vertical-align: middle !important;
}

.status_span img{
	vertical-align: middle !important;
}

.status input[type="button"] {
	margin-right: 8px;
}

div.submit {
	text-align: right;
	margin: 8px 10px;
}

</style>
<script type="text/javascript">
var tftpServer = ${tftpServer};
var ipStore;
var dirStore;
var portUsed = false;

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
} 

//修改FTP服务器基本参数时的验证函数
function validate(){
	var reg;

	//验证端口,必须为0-65535
	var port = trim($("#port").val());
	reg = /^\d{1,5}$/;
	if(!reg.test(port) || port>65535){
		$("#port").focus();
		return false;
	}
	
	//验证根路径是否合法，必须为以/开头的/与合法文件名的组合，且相邻的两个//之间不能为空格或者连续,路径最长为256位
	var rootPath = trim($("#rootPath").val());
	if(rootPath.length>256){
		$("#rootPath").focus();
		return false;
	}
	if(rootPath!="/"){
		//reg1匹配合法的路径
		reg1 = /^[\/][\/\w\-\(\)\u4E00-\u9FA5]*[\/\w\-\(\)\u4E00-\u9FA5]$/;
		//reg2匹配连续的//或者中间全为空格的//
		reg2 = /[\/][\s]*[\/]/;
		if(!reg1.test(rootPath) || reg2.test(rootPath)){
			$("#rootPath").focus();
			return false;
		}
	}		
	return true;
}

function updateTftpServer(){
	if(!validate()){return;}
	
	var ip = trim($("#ip").val());
	var port = trim($("#port").val());
	var rootPath = trim($("#rootPath").val());

	//如果参数没变的话，则无需修改
	if((ip==tftpServer.ip) && (port==tftpServer.port) 
			&& (rootPath==tftpServer.rootPath)){
		top.afterSaveOrDelete({
	        title: I18N.sys.tip,
	        html: '<b class="orangeTxt">'+I18N.sys.applyed+'</b>'
	    });
		//window.parent.showMessageDlg(I18N.sys.tip, I18N.sys.applyed);
		return;
	}

	//如果端口更改，则判断所选端口是否已被占用
	if(port!=tftpServer.port){
		isPortUsed(port);
		if(portUsed){
			top.afterSaveOrDelete({
		      title: I18N.sys.tip,
		      html: '<b class="orangeTxt">'+I18N.ftp.portUsed+'</b>'
		    });
			//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.portUsed);
			$("#port").focus();
			return;
		}
	}

	//判断所选根路径是否存在
	var exist = isRootPathExist(rootPath);
	if(!exist){
		window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.rootPathNotFound , function (confirm) {
			if(confirm == "ok"){
				//更新TFTP服务器参数
				updateTftpServerAjax(ip, port, rootPath);
			}
		});
	}else{
		//更新TFTP服务器参数
		updateTftpServerAjax(ip, port, rootPath);
	}	
}

//判断所选端口是否已被占用
function isPortUsed(port){
	$.ajax({
		url: '/system/getPortOccupancy.tv',
    	type: 'POST',
    	async: false,
    	data: {port:port},
    	dataType:"json",
   		success: function(json) {
   			//根据结果来告知用户其端口是否可用
   			portUsed = json.result;
   		}, error: function(json) {
   			//获取端口占用情况失败后....只能认为这个端口可以使用...
   			portUsed = false;
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//判断用户所选择/输入的根路径是否存在
function isRootPathExist(rootPath){
	var exist = false;
	var records = dirStore.getRange();
	for(i=0; i<dirStore.getCount(); i++){
		if(records[i].get('dirName')==rootPath){
			exist = true;
		}
	}
	return exist;
}

//更新TFTP服务器参数的AJAX操作
function updateTftpServerAjax(ip, port, rootPath){
	$.ajax({
		url: '/system/updateTftpServer.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, rootPath:rootPath},
   		success: function() {
   			//更新TFTP服务器参数成功后，提示修改成功并更新全局的TFTP服务器变量tftpServer
   			tftpServer = {ip:ip, port:port, rootPath:rootPath};
   			top.afterSaveOrDelete({
  		      title: I18N.sys.tip,
  		      html: '<b class="orangeTxt">'+I18N.sys.saved+'</b>'
  		    });
   			//window.parent.showMessageDlg(I18N.sys.tip, I18N.sys.saved);
   		}, error: function() {
   			//更新TFTP服务器参数失败后，提示修改失败
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//启动TFTP服务器
function startTftp(){
	
	var ip = tftpServer.ip;
	var port = tftpServer.port;
	var rootPath = tftpServer.rootPath;
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.startingFtp);
	$.ajax({
		url: '/system/startTftpServer.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, rootPath:rootPath},
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(json.result);
   			//判断是否启动成功，给出相应的提示信息
   			if(json.result==false){
   				window.parent.showMessageDlg(I18N.sys.tip, I18N.tftp.failedToStart);
   			}else{
   				top.afterSaveOrDelete({
   	  		      title: I18N.sys.tip,
   	  		      html: '<b class="orangeTxt">'+I18N.ftp.startOK+'</b>'
   	  		    });
   				//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.startOK);
   			}	     	
   		},  error: function(json) {
   			//启动TFTP服务器失败后，提示修改失败
   			window.parent.closeWaitingDlg();
   			setStatusStyle(false);
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//停止TFTP服务器
function stopTftp(){
	
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.stopFtp);
	
	$.ajax({
		url: '/system/stopTftpServer.tv',
    	type: 'POST',
   		success: function() {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(false);	 
   			top.afterSaveOrDelete({
  		      title: I18N.sys.tip,
  		      html: '<b class="orangeTxt">'+I18N.ftp.stopOK+'</b>'
  		    });
   			//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.stopOK);
   		}, error: function() {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//重启TFTP服务器
function reStartTftp(){
	
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.reStartFtp);
	var ip = tftpServer.ip;
	var port = tftpServer.port;
	var rootPath = tftpServer.rootPath;
	
	$.ajax({
		url: '/system/reStartTftpServer.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, rootPath:rootPath},
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(json.result);
   			//判断是否启动成功，给出相应的提示信息
   			if(json.result==false){
   				window.parent.showMessageDlg(I18N.sys.tip, I18N.tftp.failedToReStart);
   			}else{
   				top.afterSaveOrDelete({
   	  		      title: I18N.sys.tip,
   	  		      html: '<b class="orangeTxt">'+I18N.ftp.reStartOK+'</b>'
   	  		    });
   				//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.reStartOK);
   			}
   		}, error: function(json) {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//获取TFTP服务器的启动状态,参数type为load表示进入时所调用的，refresh表示点击刷新按钮时所调用的
function getTftpServerStatus(type){

	if(type=="refresh"){
		
		window.top.showWaitingDlg(I18N.sys.waiting, I18N.tftp.loadingStatus);
	}
		
	$.ajax({
		url: '/system/getTftpServerStatus.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			if(type=="refresh"){
   				window.parent.closeWaitingDlg();
   			}
   			setStatusStyle(json.result);
   		}, error: function(json) {
   			//获取FTP服务器状态失败后，提示修改失败
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function validateDisable(obj){
	if($(obj).attr('disabled') === 'disabled'){
        return false;
    }
    return true; 
}

//根据启动状态设置按钮及状态灯的样式
function setStatusStyle(started){
	//设置状态灯
	$("#started").attr("src", started ? "/images/fault/service_up.png" : "/images/fault/service_down.png");
	$("#started").attr("alt",started?"up":"down");
	//设置按钮的状态
	$("#start").attr("disabled", started);
	$("#stop").attr("disabled", !started);
	$("#reStart").attr("disabled", !started); 
	if(started){
		$("#start").addClass("disabledAlink");
		$("#stop").removeClass("disabledAlink");
		$("#reStart").removeClass("disabledAlink");
	}else{
		$("#start").removeClass("disabledAlink");
		$("#stop").addClass("disabledAlink");
		$("#reStart").addClass("disabledAlink");
	}
}

//查看文件
function fileManage(){
	
	var url = "system/showTftpFiles.tv";
   	window.top.createDialog('fileManage', I18N.ftp.fileManage, 800, 500, url, null, true, true);
}

function cancleClick(){
	window.parent.closeWindow("tftpServerManage");
}

Ext.onReady(function(){
	//给各输入框赋值
	$("#ip").val(tftpServer.ip);
	$("#port").val(tftpServer.port);
	$("#rootPath").val(tftpServer.rootPath);
	//生成带IP地址的下拉框
	/* ipStore = new Ext.data.JsonStore({
	    url: '/system/getTftpServerIpAddress.tv',
        fields: ['ip']
	});	
	ipStore.load();
	var ipCombo = new Ext.form.ComboBox({
        store: ipStore,
        valueField: 'ip',
        displayField: 'ip',
        triggerAction: 'all',
        editable: true,
        mode: 'local',
        applyTo : 'ip'
	}); */
	//生成带有所有目录的下拉菜单
	dirStore = new Ext.data.JsonStore({
	    url: '/system/getTftpServerDirectories.tv',
        fields: ['dirName']
	});	
	dirStore.load();
	var combo = new Ext.form.ComboBox({
        store: dirStore,
        valueField: 'dirName',
        displayField: 'dirName',
        triggerAction: 'all',
        editable: true,
        mode: 'local',
        applyTo : 'rootPath'
	});
	//为状态灯添加提示信息
	$("#started").hover(
		function(){
			if($(this).attr("alt")=="up"){
	        	inputFocused($(this).attr("id"), I18N.ftp.start);
			}else{
				inputFocused($(this).attr("id"), I18N.ftp.notStart);
			}
		},
		function(){
			inputBlured(this);
		}
	);
	//获取FTP服务器的状态
	getTftpServerStatus("load");
});
</script>

</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"><fmt:message bundle="${resources}" key="ftp.setTftpServer" /></div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	<input id="ip" type="hidden" disabled="disabled"/>
	<div class="edge10">
		<div class="zebraTableCaption">
	     	<div class="zebraTableCaptionTitle"><span><fmt:message bundle="${resources}" key="ftp.basicParam" /></span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                 <td class="rightBlueTxt" width="140">
		                    <label>
								<span class="required">*</span>
								<fmt:message bundle="${resources}" key="ftp.port" />
							</label>
		                </td>
		                <td>
		                    <input class="normalInputDisabled" id="port" type="text" style="width: 180px;" disabled="disabled" 
							toolTip='<fmt:message bundle="${resources}" key="ftp.portRule" />'	/>
		                </td>
		                <td class="rightBlueTxt">
		                    <label>
								<span class="required">*</span>
								<fmt:message bundle="${resources}" key="ftp.rootPath" />
							</label> 
		                </td>
		                <td>
		                    <input id="rootPath" type="text" class="normalInput" style="width: 160px;" 
							toolTip='<fmt:message bundle="${resources}" key="ftp.rootPathRule" />' />
		                </td>
		            </tr>
		        </tbody>
		    </table>
		</div>    
		<div class="zebraTableCaption pT10 mT10">
			<div class="zebraTableCaptionTitle "><span><fmt:message bundle="${resources}" key="ftp.statusInfo" /></span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td>
		                    <div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
							         <li>
										<label><fmt:message bundle="${resources}" key="ftp.startStatus" /></label> 
							         </li>
							         <li>
							         	<img class="statusImg" id="started" src="/images/fault/service_down.png" alt="down"/>
							         </li>
							     </ol>
							</div>
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td>
			                   <div class="noWidthCenterOuter clearBoth">
								    <ol class="upChannelListOl pB5 pT5 noWidthCenter">
								        <li>
								            <a onclick="return validateDisable(this)" id="start" href="javascript:startTftp();" class="normalBtnBig disabledAlink">
								                <span>
								                    <i class="miniIcoPlay">
								                    </i>
								                    <fmt:message bundle="${resources}" key="ftp.start" />
								                </span>
								            </a>
								        </li>
								        <li>
								            <a onclick="return validateDisable(this)" id="stop" href="javascript:stopTftp();" class="normalBtnBig disabledAlink">
								                <span>
								                    <i class="miniIcoStop">
								                    </i>
								                  	<fmt:message bundle="${resources}" key="ftp.stop" />
								                </span>
								            </a>
								        </li>
								        <li>
								            <a onclick="return validateDisable(this)" id="reStart" href="javascript:reStartTftp();" class="normalBtnBig disabledAlink">
								                <span>
								                    <i class="miniIcoPower">
								                    </i>
								                  	<fmt:message bundle="${resources}" key="ftp.reStart" />
								                </span>
								            </a>
								        </li>
								        <li>
								            <a onclick="getTftpServerStatus('refresh')" id="refresh" href="javascript:;" class="normalBtnBig">
								                <span>
								                    <i class="miniIcoRefresh">
								                    </i>
								                  	<fmt:message bundle="${resources}" key="ftp.refresh" />
								                </span>
								            </a>
								        </li>						        
								    </ol>
								</div>
		                </td>
		            </tr>
		        </tbody>
		    </table>
	    </div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a onclick="updateTftpServer()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i><fmt:message bundle="${resources}" key="sys.apply" /></span></a></li>
		         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
				
	</div>
	
		
	<div class=formtip id=tips style="display: none"></div>
	
	
</body>
</html>