<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css css.reset
	library Jquery
	library ext
	library zeta
    module platform
</Zeta:Loader>
<title>@ftp.ftpServer@</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript" src ="/js/jquery/nm3kPassword.js"></script>

<style type="text/css">
input[type="checkbox"]{width:17px;height:17px;line-height:17px;margin-right:2px; vertical-align:-2px;*vertical-align:middle;_vertical-align:3px;}
.status p {margin: 6px 0px;	text-align: center;}
#startStatus_span{margin-right: 100px;}
.status .status_span{vertical-align: middle !important;}
.status_span label{width: 14px;	height: 14px; vertical-align: middle !important;}
.status_span img{vertical-align: middle !important;}
.status input[type="button"] {margin-right: 8px;}
.required{ color:#f00;}
#started, #reachable{
	display:none;
}
</style>
<script src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var ftpServer = ${ftpServer};
var store;
var ipStore;

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
	//验证用户名,必须为3-15位的由数字、字母、下划线组成的字符串
	var userName = trim($("#userName").val());
	reg = /^\w{3,15}$/;
	if(!reg.test(userName)){
		$("#userName").focus();
		return false;
	}
	//验证密码,必须为3-15位的由数字、字母、下划线组成的字符串
	var pwd = trim($("#pwd").val());
	reg = /^\w{3,15}$/;
	if(!reg.test(pwd)){
		$("#pwd").focus();
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

//判断用户所选择/输入的根路径是否存在
function isRootPathExist(rootPath){
	var exist = false;
	var records = store.getRange();
	for(i=0; i<store.getCount(); i++){
		if(records[i].get('dirName')==rootPath){
			exist = true;
		}
	}
	return exist;
}

//获取FTP服务器的状态,参数type为load表示进入时所调用的，refresh表示点击刷新按钮时所调用的
function getFtpServerStatus(type){
	
	if(type=="refresh"){
		window.top.showWaitingDlg('@sys.waiting@', '@ftp.loadingStatus@');
		$('#started_loading').show();
		$('#reachable_loading').show();
		$('#started').hide();
		$('#reachable').hide();
	}
		
	$.ajax({
		url: '/system/getFtpServerStatus.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(ftpServerStatus) {
   			if(type=="refresh"){
   				window.parent.closeWaitingDlg();
   			}
   			setStatusStyle(ftpServerStatus.started, ftpServerStatus.reachable);
   		}, error: function() {
   			//获取FTP服务器状态失败后，提示修改失败
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//更新FTP服务器参数
function updateFtpServer(){
	if(!validate()){return;}
	
	var ip = trim($("#ip").val());
	var port = trim($("#port").val());
	var userName = trim($("#userName").val());
	var pwd = trim($("#pwd").val());
	var rootPath = trim($("#rootPath").val());
	var writeable = $("#writeable").is(':checked');
	
	//如果参数没变的话，则无需修改
	if((ip==ftpServer.ip) && (port==ftpServer.port) 
			&& (userName==ftpServer.userName) && (pwd==ftpServer.pwd) 
			&& (rootPath==ftpServer.rootPath) && (writeable==ftpServer.writeable)){
		top.afterSaveOrDelete({
	      title: '@COMMON.tip@',
	      html: '<b class="orangeTxt">@sys.applyed@</b>'
	    });
		return;
	}
	
	//判断所选根路径是否存在
	var exist = isRootPathExist(rootPath);
	if(!exist){
		window.top.showOkCancelConfirmDlg('@sys.tip@', '@ftp.rootPathNotFound@' , function (confirm) {
			if(confirm == "ok"){
				//更新FTP服务器参数
				updateFtpServerAjax(ip, port, userName, pwd, rootPath, writeable);
			}
		});
	}else{
		//更新FTP服务器参数
		updateFtpServerAjax(ip, port, userName, pwd, rootPath, writeable);
	}	 
}

//更新FTP服务器参数的AJAX操作
function updateFtpServerAjax(ip, port, userName, pwd, rootPath, writeable){
	$.ajax({
		url: '/system/updateFtpServer.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, userName:userName, pwd:pwd, rootPath:rootPath, writeable:writeable},
   		success: function() {
   			//更新FTP服务器参数成功后，提示修改成功并更新全局的FTP服务器变量ftpServer
   			ftpServer = {ip:ip, port:port, userName:userName, pwd:pwd, rootPath:rootPath, writeable:writeable};
   			top.afterSaveOrDelete({
   		      title: '@COMMON.tip@',
   		      html: '<b class="orangeTxt">@sys.saved@</b>'
   		    });
   			//window.parent.showMessageDlg('@sys.tip@', '@sys.saved@');
   		}, error: function() {
   			//更新FTP服务器参数失败后，提示修改失败
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//启动FTP服务器
function startFtp(){
	window.top.showWaitingDlg('@sys.waiting@', '@ftp.startingFtp@');
	$.ajax({
		url: '/system/startFtpServer.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(json.ftpServerStatus.started, json.ftpServerStatus.reachable);
   			//判断是否启动成功，给出相应的提示信息
   			if(json.success==false){
   				window.parent.showMessageDlg('@sys.tip@', '@ftp.failedToStart@');
   			}else{
   				top.afterSaveOrDelete({
   	   		      title: '@COMMON.tip@',
   	   		      html: '<b class="orangeTxt">@ftp.startOK@</b>'
   	   		    });
   				//window.parent.showMessageDlg('@sys.tip@', '@ftp.startOK@');
   			}	     	
   		},  error: function(json) {
   			//启动FTP服务器失败后，提示修改失败
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//停止FTP服务器
function stopFtp(){
	window.top.showWaitingDlg('@sys.waiting@', '@ftp.stopFtp@');
	$.ajax({
		url: '/system/stopFtpServer.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(ftpServerStatus) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(ftpServerStatus.started, ftpServerStatus.reachable);	 
   			top.afterSaveOrDelete({
   		      title: '@COMMON.tip@',
   		      html: '<b class="orangeTxt">@ftp.stopOK@</b>'
   		    });
   			//window.parent.showMessageDlg('@sys.tip@', '@ftp.stopOK@');
   		}, error: function(ftpServerStatus) {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//重启FTP服务器
function reStartFtp(){
	window.top.showWaitingDlg('@sys.waiting@', '@ftp.reStartFtp@');
	$.ajax({
		url: '/system/reStartFtpServer.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(json.ftpServerStatus.started, json.ftpServerStatus.reachable);
   			//判断是否启动成功，给出相应的提示信息
   			if(json.success==false){
   				window.parent.showMessageDlg('@sys.tip@', '@ftp.failedToReStart@');
   			}else{
   				top.afterSaveOrDelete({
   	   		      title: '@COMMON.tip@',
   	   		      html: '<b class="orangeTxt">@ftp.reStartOK@</b>'
   	   		    });
   				//window.parent.showMessageDlg('@sys.tip@', '@ftp.reStartOK@');
   			}
   		}, error: function(json) {
   			window.parent.closeWaitingDlg();
   			setStatusStyle(json.ftpServerStatus.started, json.ftpServerStatus.reachable);
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function cancleClick(){
	window.parent.closeWindow("ftpServerManage");
}

function validateDisable(obj){
	 if($(obj).attr('disabled') === 'disabled'){
         return false;
     }
     return true; 
}

//根据启动及可达状态设置按钮及状态灯的样式
function setStatusStyle(started, reachable){
	//设置状态灯
	$("#started").attr("src", started ? "/images/fault/service_up.png" : "/images/fault/service_down.png");
	$("#reachable").attr("src", reachable ? "/images/fault/service_up.png" : "/images/fault/service_down.png");
	$("#started").attr("nm3kTip",started?"@ftp.start@":"@ftp.notStart@");
	$("#reachable").attr("nm3kTip",reachable?"@ftp.reachable@":"@ftp.reachless@");
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
	$('#started_loading').hide();
	$('#reachable_loading').hide();
	$('#started').show();
	$('#reachable').show();
}

Ext.onReady(function(){
	var width = $("#pwd")[0].offsetWidth - 4;
	 $("#pwd").parent().find("input").css("width" , width );
	//填充输出框的值
	$("#ip").val(ftpServer.ip);
	$("#port").val(ftpServer.port);
	$("#userName").val(ftpServer.userName);
	$("#pwd").val(ftpServer.pwd);
	$("#writeable").attr("checked",ftpServer.writeable);
	$("#rootPath").val(ftpServer.rootPath);
	//获取服务器上的所有IP地址
	/* ipStore = new Ext.data.JsonStore({
	    url: '/system/getServerIpAddress.tv',
        fields: ['ipIndex', 'ip'],
        sortInfo:{field:'ipIndex',direction:'asc'}
	});	
	ipStore.load(); */
	//生成带有所有IP地址的下拉菜单
	/* var combo = new Ext.form.ComboBox({
        store: ipStore,
        valueField: 'ipIndex',
        displayField: 'ip',
        triggerAction: 'all',
        editable: true,
        mode: 'local',
        applyTo : 'ip'
	}); */
	
	//获取FTP服务器当前的所有目录
	store = new Ext.data.JsonStore({
	    url: '/system/getFtpServerDirectories.tv',
        fields: ['dirIndex', 'dirName'],
        sortInfo:{field:'dirIndex',direction:'asc'}
	});	
	store.load();
	//生成带有所有目录的下拉菜单
	var combo = new Ext.form.ComboBox({
        store: store,
        valueField: 'dirIndex',
        displayField: 'dirName',
        triggerAction: 'all',
        editable: true,
        mode: 'local',
        applyTo : 'rootPath'
	});
	
	//获取FTP服务器的状态
	getFtpServerStatus("load");
	
});
</script>

</head>
<body class="openWinBody" >
	<!-- <div class="openWinHeader">
	    <div class="openWinTip">@ftp.setFtpServer@</div>
	    <div class="rightCirIco earthCirIco"></div>
	</div> -->
	<input id="ip" type="hidden" disabled="disabled" />
	<div class="edge10" style="margin-top: 20px;">
		<div class="zebraTableCaption" class="paramForm">
     		<div class="zebraTableCaptionTitle"><span>@ftp.basicParam@</span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="100"><label><span class="required">*</span>@ftp.port@</label></td>
		                <td><input class="normalInputDisabled " id="port" type="text" disabled="disabled"  style="width:222px" toolTip="@ftp.portRule@" /></td>
		           		<td class="rightBlueTxt"><label><span class="required">*</span>@ftp.rootPath@</label></td>
		                <td><input id="rootPath" type="text" class="normalInput w200" toolTip="@ftp.pathRule@" /></td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt"><label><span class="required">*</span>@ftp.userName@</label></td>
		                <td><input class="normalInput" id="userName" type="text" style="width:218px;"  toolTip="@ftp.nameAndPwdRule@" /></td>
		                <td class="rightBlueTxt"><label><span class="required">*</span>@ftp.password@</label></td>
		                <td id="putPwd">
							<script type="text/javascript">
								var pass1 = new nm3kPassword({
									id : "pwd",
									renderTo : "putPwd",
									width : 180,
									firstShowPassword : true,
									disabled : false,
									maxlength : 15,
									toolTip : '@ftp.nameAndPwdRule@'
								})
								pass1.init();
							</script>
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt"><label>@ftp.writeable@</label></td>
		                <td><input type="checkbox" id="writeable" /></td>
		                <td colspan="2"></td>
		            </tr>
		            <tr class="darkZebraTr">
		            	<td colspan="4">
		            		 <ol class="upChannelListOl noWidthCenter">
						        <li>
									<a href="javascript:;" class="normalBtnBig" onclick="updateFtpServer()">
				            			<span><i class="miniIcoData"></i>@sys.save@</span>
				            		</a>
								</li>
						    </ol>
		            	</td>
		            </tr>
		        </tbody>
		    </table>
		</div>		
		<div class="zebraTableCaption pT10 mT20">
   			<div class="zebraTableCaptionTitle"><span>@ftp.statusInfo@</span></div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			    <tbody>
			        <tr>
			            <td class="rightBlueTxt" width="200">
			                
			            </td>
			            <td width="200">
			                <span id="startStatus_span" class="status_span">
								<label>@ftp.startStatus@</label>
								<span id="started_loading">@ftp.loadingStatus@</span>
								<img class="statusImg nm3kTip" id="started" src="/images/fault/service_down.png" alt="down" nm3kTip="@ftp.notStart@"/>
							</span>
			            </td>
			            <td width="200">
			                <span id="reachableStatus_span" class="status_span">
								<label>@ftp.reachableStatus@</label>
								<span id="reachable_loading">@ftp.loadingStatus@</span>
								<img class="statusImg nm3kTip" id="reachable" src="/images/fault/service_down.png" alt="down" nm3kTip="@ftp.reachless@"/>
							</span>	
			            </td>
			            <td width="200">
			                
			            </td>
			        </tr>
			        <tr class="darkZebraTr">
			            <td colspan="4">
			               <div class="noWidthCenterOuter clearBoth">
							    <ol class="upChannelListOl pB5 pT5 noWidthCenter">
							        <li>
							            <a href="javascript:startFtp();" class="normalBtnBig disabledAlink"  id="start" onclick="return validateDisable(this);" disabled="true">
							                <span><i class="miniIcoPlay"></i>@ftp.start@</span>
							            </a>
							        </li>
							        <li>
							            <a href="javascript:stopFtp();" class="normalBtnBig disabledAlink"  id="stop" onclick="return validateDisable(this);" disabled="true">
							                <span><i class="miniIcoStop"></i>@ftp.stop@</span>
							            </a>
							        </li>
							        <li>
							            <a href="javascript:reStartFtp();" class="normalBtnBig disabledAlink"  id="reStart" onclick="return validateDisable(this);" disabled="true">
							                <span><i class="miniIcoPower"></i>@ftp.reStart@</span>
							            </a>
							        </li>
							        <li>
							            <a href="javascript:;" class="normalBtnBig"  id="refresh" onclick="getFtpServerStatus('refresh')">
							                <span><i class="miniIcoRefresh"></i>@ftp.refresh@</span>
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
		    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		        <li><a href="javascript:;" class="normalBtnBig" onclick="cancleClick()"><span><i class="miniIcoForbid"></i>@sys.cancel@</span></a></li>
		    </ol>
		</div>
	</div>
</body>
</Zeta:HTML>