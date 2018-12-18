<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources"
	var="resources" />
<title><fmt:message bundle="${resources}" key="ftp.ftpConnect" /></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/mytheme.css" />
<%@include file="/include/tabPatch.inc"%>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src ="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kPassword.js"></script>

<style type="text/css">
div.status {text-align: center;}

.status_ul{
	float: left;
	vertical-align: middle !important;
}

.status_ul li{
	float: left;
	margin-left: 10px;
	vertical-align: middle !important;
}

.status_ul li img {vertical-align: middle !important;}
.status_ul li label {vertical-align: middle !important;}
.status_ul li input {vertical-align: middle !important;}

#refresh {margin-left: 25px;}

#loading_div {margin-top: 5px;	margin-bottom: 5px;	display: none;}
#loading_div img {width: 20px; height: 20px; vertical-align: middle !important;}
#loading_div span {vertical-align: middle !important;}

div.submit {text-align: right; margin: 10px 10px;}
#pwd{width: 232px;}

.required{ color:#f00;}
</style>

<script type="text/javascript">
//页面的连接参数全局变量
var ftpConnect = ${ftpConnect};

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
}

//修改FTP连接参数时的验证函数
function validate(){
	var reg;
	//验证IP地址
	var ip = trim($("#ip").val());
	reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if(!reg.test(ip)){
		$("#ip").focus();
		return false;
	}
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
	//验证根路径是否合法，必须为空或者/与合法文件名的组合，且相邻的两个//之间不能为空格或者连续,路径最长为256位
	var remotePath = trim($("#remotePath").val());
	if(remotePath.length>256){
		$("#remotePath").focus();
		return false;
	}
	reg = /[/][\s]*[/]/;
	if(remotePath!="" && remotePath!="/"){
		//reg1匹配合法的路径
		reg1 = /^[\/][\/\w\-\(\)\u4E00-\u9FA5]*[\/\w\-\(\)\u4E00-\u9FA5]$/;
		//reg2匹配连续的//或者中间全为空格的//
		reg2 = /[\/][\s]*[\/]/;
		if(!reg1.test(remotePath) || reg2.test(remotePath)){
			$("#remotePath").focus();
			return false;
		}
	}
	return true;
}

//获取FTP连接的状态值(type:{refresh:点击刷新按钮;load:加载页面;apply:点击应用按钮;fileManage:点击文件管理按钮})
function getFtpConnectStatus(type){
	if(type=="refresh"){
		//还原按钮的样式
		//$("#refresh").attr("class", "BUTTON75");
		window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.loadingStatus);
	}else{
		//隐藏状态DIV，显示刷新DIV
		$("#status_div").css("display", "none");
		$("#loading_div").css("display", "inline");
	}
	$.ajax({
		url: '/system/getFtpConnectStatus.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			if(type=="refresh"){
   				window.parent.closeWaitingDlg();
   			}else{
   				//隐藏刷新DIV，显示状态DIV
				$("#loading_div").css("display", "none");
 				$("#status_div").css("display", "inline");
   			}
   			//设置状态的值
   			if(json.ftpConnectStatus.reachable==true){
   				setStatusArea(true, json.ftpConnectStatus.readable, json.ftpConnectStatus.writeable);
   				if(type=="fileManage"){
   					//如果是文件管理按钮触发，则打开文件管理页面
   					window.parent.closeWaitingDlg();
   	   				var url = "system/showFtpFiles.tv?remotePath="+trim($("#remotePath").val())
   							+"&ip="+ftpConnect.ip+"&port="+ftpConnect.port+"&userName="+ftpConnect.userName
   							+"&pwd="+ftpConnect.pwd;
   					window.top.createDialog('fileManage', I18N.ftp.fileManage, 800, 500, url, null, true, true);
   				}
   			}else{
   				setStatusArea(false, false, false);
   				//确定不可达时是否弹窗提示读写权限不可获取，只有刷新和加载时获取
   	   			if(type=="load" || type=="refresh"){
   	   			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.connectFailed);
   	   			}else if(type=="fileManage"){
   	   				//如果是文件管理按钮触发，则不打开文件管理页面，并提示不可达
   	   	   			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.unReachable);
   	   			}
   			}
   			
   		},error:function(json){
   			if(type=="refresh"){
   				window.parent.closeWaitingDlg();
   			}else{
   				//隐藏刷新DIV，显示状态DIV
				$("#loading_div").css("display", "none");
 				$("#status_div").css("display", "inline");
   			}
   			//设置状态的值
   			setStatusArea(false, false, false);
   			//确定不可达时是否弹窗提示读写权限不可获取，只有刷新和加载时获取
	   		if(type=="load" || type=="refresh"){
	   			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.connectFailed);
	   		}else if(type=="fileManage"){
	   				//如果是文件管理按钮触发，则不打开文件管理页面，并提示不可达
	   				window.parent.closeWaitingDlg();
	   	   			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.unReachable);
	   		}
   		},cache: false,
   		complete: function (XHR, TS) { XHR = null }
	});
}

//设置FTP连接状态的值
function setStatusArea(reachable, readable, writeable){
	$("#reachable").attr("src", reachable?"/images/fault/service_up.png":"/images/fault/service_down.png");
	$("#readable").attr("checked",readable);
	$("#writeable").attr("checked",writeable);
	$("#reachable").attr("alt",reachable?"up":"down");
}

//更新FTP服务器基本参数
function update(){
	if(!validate()){return;}
	
	var ip = trim($("#ip").val());
	var port = trim($("#port").val());
	var userName = trim($("#userName").val());
	var pwd = trim($("#pwd").val());
	var remotePath = trim($("#remotePath").val());
	
	//如果参数没变的话，则无需修改
	if((ip==ftpConnect.ip) && (port==ftpConnect.port) 
			&& (userName==ftpConnect.userName) && (pwd==ftpConnect.pwd) 
			&& (remotePath==ftpConnect.remotePath)){
		top.afterSaveOrDelete({
	      title: I18N.sys.tip,
	      html: '<b class="orangeTxt">'+I18N.sys.saved+'</b>'
	    });
		return;
	}
	
	$.ajax({
		url: '/system/updateFtpConnect.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath},
   		success: function() {
   			//更新FTP连接参数成功后，提示修改成功并更新全局的FTP服务器变量ftpServer
   			ftpConnect = {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath};
   			top.afterSaveOrDelete({
   		      title: I18N.sys.tip,
   		      html: '<b class="orangeTxt">'+I18N.sys.saved+'</b>'
   		    });
   			//更新FTP状态区的信息
   			getFtpConnectStatus("apply");
   		}, error: function() {
   			//更新FTP连接参数失败后，提示修改失败
   			window.parent.showErrorDlg();
		},cache: false,
		complete: function (XHR, TS) { XHR = null }
	}); 
}

function fileManage(){
	
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.openingFileManage);
	getFtpConnectStatus("fileManage");
}

function cancleClick(){
	window.parent.closeWindow("ftpConnectManage");
}

Ext.onReady(function(){
	//填充输出框的值
	$("#ip").attr("value",ftpConnect.ip);
	$("#port").attr("value",ftpConnect.port);
	$("#userName").attr("value",ftpConnect.userName);
	$("#pwd").attr("value",ftpConnect.pwd);
	$("#remotePath").attr("value",ftpConnect.remotePath);
	//给状态灯加上提示
	$("#reachable").hover(
		function(){
			if($(this).attr("alt")=="up"){
	        	inputFocused($(this).attr("id"), I18N.ftp.reachable);
			}else{
				inputFocused($(this).attr("id"), I18N.ftp.reachless);
			}
		},
		function(){
			inputBlured(this);
		}
	);
	//获取服务器上的所有IP地址
	ipStore = new Ext.data.JsonStore({
	    url: '/system/getConnectIpAddress.tv',
        fields: ['ipIndex', 'ip'],
        sortInfo:{field:'ipIndex',direction:'asc'}
	});	
	ipStore.load();
	//生成带有所有IP地址的下拉菜单
	var combo = new Ext.form.ComboBox({
        store: ipStore,
        valueField: 'ipIndex',
        displayField: 'ip',
        triggerAction: 'all',
        editable: true,
        mode: 'local',
        applyTo : 'ip'
	});
	//查询FTP服务器的状态,
	getFtpConnectStatus("load");
});

</script>

</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"><fmt:message bundle="${resources}" key="ftp.setFtpConnect" /></div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	<div class="edge10">
		<div class="zebraTableCaption">
	     	<div class="zebraTableCaptionTitle"><span><fmt:message bundle="${resources}" key="ftp.basicParam" /></span></div>
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="140">
		                	<label>
								<span class="required">*</span>
								<fmt:message bundle="${resources}" key="ftp.ip" />
							</label>
		                </td>
		                <td width="180">
		                    <input id="ip" type="text" class="normalInput w160" toolTip='<fmt:message bundle="${resources}" key="ftp.ipRule" />' />
		                </td>
		                <td class="rightBlueTxt" width="140">
		                	<label>
								<span class="required">*</span> 
								<fmt:message bundle="${resources}" key="ftp.port" />
							</label>
		                </td>
		                <td>
		                	<input disabled="disabled"
							class="normalInputDisabled w180" id="port" type="text" toolTip='<fmt:message bundle="${resources}" key="ftp.portRule" />' />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                    <label><span class="required">*</span> <fmt:message
							bundle="${resources}" key="ftp.userName" /></label>
		                </td>
		                <td>
		                    <input	class="normalInput w180" id="userName" type="text" 
		                    toolTip='<fmt:message bundle="${resources}" key="ftp.nameAndPwdRule" />' />
		                </td>
		                <td class="rightBlueTxt">
		                    <label><span class="required">*</span> <fmt:message
							bundle="${resources}" key="ftp.password" /></label>
		                </td>
		                <td id="putPwd">
		                    <%-- <Zeta:Password width="255px" id="pwd"/> --%>
		                    <script type="text/javascript">
			                    var pass1 = new nm3kPassword({
									id : "pwd",
									renderTo : "putPwd",									
									width : 140,
									firstShowPassword : true,
									maxlength : 15,
									toolTip : '<fmt:message bundle="${resources}" key="ftp.nameAndPwdRule" />'
								})
								pass1.init();
							</script>
		                </td>
		            </tr>
		            <tr>
		                <td class="rightBlueTxt">
		                    <label><fmt:message bundle="${resources}"
							key="ftp.remotePath" /></label> 
		                </td>
		                <td>
		                    <input class="normalInput w180"
							id="remotePath" type="text" toolTip='<fmt:message bundle="${resources}" key="ftp.pathRule" />' />
		                </td>
		                <td class="rightBlueTxt">
		                    
		                </td>
		                <td>
		                    
		                </td>
		            </tr>
		        </tbody>
		    </table>
	    </div>
	    <div class="zebraTableCaption pT20 mT10">
	     	<div class="zebraTableCaptionTitle"><span><fmt:message bundle="${resources}" key="ftp.statusInfo" /></span></div>	    
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			    <tbody>
			        <tr>
			            <td>
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
						         <li>
						         	<label><fmt:message bundle="${resources}" key="ftp.reachableStatus" /></label> 
						         </li>
						         <li style="margin-right:26px;">
						         	<img id="reachable" src="/images/fault/service_down.png" alt="down" />
						         </li>
						         <li>
						         	<label><fmt:message bundle="${resources}" key="ftp.readable" /></label> 
						         </li>
						         <li style="margin-right:26px;">
						         	<input id="readable" type="checkbox" disabled="disabled" />
						         </li>
						         <li>
						         	<label><fmt:message bundle="${resources}" key="ftp.writeable" /></label> 
						         </li>
						         <li>
						         	<input id="writeable" type="checkbox" disabled="disabled" />
						         </li>
						     </ol>
						</div>												
			            </td>			           
			        </tr>	
			        <tr class="darkZebraTr">
			        	 <td>
			            	<div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
							         <li><a id="refresh"  onclick="getFtpConnectStatus('refresh')" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoRefresh"></i><fmt:message bundle="${resources}" key="ftp.refresh" /></span></a></li>
							     </ol>
							</div>
			            </td>
			        </tr>	        
			    </tbody>
			</table>
	    </div>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="fileManage" onclick="fileManage()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoInfo"></i><fmt:message bundle="${resources}" key="ftp.fileManage" /></span></a></li>
		         <li><a  onclick="update()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i><fmt:message bundle="${resources}" key="sys.apply" /></span></a></li>
		         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
	</div>
	
	<div id="loading_div">
		<img src="/images/blue_loading.gif" alt="" /> <span><fmt:message
				bundle="${resources}" key="ftp.loadingStatus" /></span>
	</div>

	<div class=formtip id="tips" style="display: none"></div>
	
	
	<!-- <div id="status" class="status">
		<fieldset>			
			
		</fieldset>
	</div> -->
	
</body>
</html>