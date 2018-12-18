<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>
<script type="text/javascript" src="/js/jQueryFileUpload/ajaxfileupload.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>




<script type="text/javascript">
var ftpConnect;

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
} 

function validate(){
	//验证是否选择文件
	if($("#localFile").fakeUpload("getPath")==""){
		top.afterSaveOrDelete({
	      title: I18N.sys.tip,
	      html: '<b class="orangeTxt">'+I18N.ftp.pleaseSelectFile+'</b>'
	    });
		//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.pleaseSelectFile);
		return false;
	}
	//验证文件名,文件名不能为\/:*?"<>|
	var remoteFileName = trim($("#remoteFileName").val());
	var reg = /^[^\\\/\:\*\?\"\<\>\|]+$/;
	if(remoteFileName=="" || !reg.test(remoteFileName)){
		$("#remoteFileName").focus();
		return false;
	}
	return true;
}

function validateFileType(){
	var fileName = $("#remoteFileName").val();
	//获取文件的后缀名
	var reg = new RegExp(".*\\.(.*)");
	var typeName = reg.exec(fileName)[1];
	if(typeName=="exe" || typeName=="bat" || typeName=="dll"){
		return false;
	}
	return true;
}

function validateFileSize(){
	
}

function startTrap() {  
	document.getElementById('trapProgress').click(); 
}  
	  
function trapProgressBar() {  
}

function cancleClick(){
	startTrap();
	window.parent.getWindow("fileManage").body.dom.firstChild.contentWindow.store.reload();
	window.parent.closeWindow("fileUpload");
}

function ajaxFileUpload(){
	var ip = ftpConnect.ip;
	var port = ftpConnect.port;
	var userName = ftpConnect.userName;
	var pwd = ftpConnect.pwd;
	var remotePath = ftpConnect.remotePath;
	var remoteFileName = trim($("#remoteFileName").val());
	
	if(!validate()){return;}	
	
	//验证在当前目录下。是否存在同名文件
	var records =  window.parent.getWindow("fileManage").body.dom.firstChild.contentWindow.store.getRange();
	var hasSameName = false;
	for(i=0; i<records.length; i++){
		var fileName = records[i].get('name');
		if( records[i].get('name')==remoteFileName){
			hasSameName = true;
		}
	}
	
	if(hasSameName==true){
		window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.hasSameNameFile , function (confirm) {
			if(confirm == "ok"){
				uploadFileAjax(ip, port, userName, pwd, remotePath, remoteFileName);
			}
		});
	}else{
		uploadFileAjax(ip, port, userName, pwd, remotePath, remoteFileName);
	}
	return false;
}

//上传文件的AJAX操作
function uploadFileAjax(ip, port, userName, pwd, remotePath, remoteFileName){
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.uploading);
	$.ajaxFileUpload({
		url:'system/uploadFileToFtp.tv',
		secureuri:false,
		fileElementId:'localFile',
		data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath, remoteFileName:remoteFileName},
		dataType: 'json',
		success: function (data, status){
			if(data.message=="true"){
				top.afterSaveOrDelete({
			      title: I18N.sys.tip,
			      html: '<b class="orangeTxt">'+I18N.ftp.uploadSuccess+'</b>'
			    });
				//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.uploadSuccess);
			}else if(data.message=="false"){
				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.uploadFailed);
			}else{
				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.fileNotExist);
			}
      	}, error: function (data, status, e){
        	window.parent.closeWaitingDlg();
        },cache: false,
		complete: function (XHR, TS) {XHR = null ; cancleClick(); }
	})
}
$(document).ready(function(){
	//从父页面获取FTP连接全局变量
	ftpConnect = window.parent.getWindow("fileManage").body.dom.firstChild.contentWindow.ftpConnect;
	$("#localFile").fakeUpload("init",{    
		"tiptext":I18N.ftp.emptyFileDeined ,
		"width":200,
		"btntext":I18N.ftp.selectFile ,
		"checkfn":function(filePath,name){
			//获取文件名
			var reg = /[^\\]+\.[\w]+$/;
			var fileName = reg.exec(filePath);
			//若未填写文件名，将文件名赋给文本框：服务器端名称
			var len = $.trim($("#remoteFileName").val()).length;
			if(len == 0){
				$("#remoteFileName").val(fileName);
			}
			return true;
		}
	}); 
});

</script>

</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco upArrCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="140">
	                    <label><fmt:message bundle="${resources}" key="ftp.remoteName" /></label>
	                </td>
	                <td>
	                    <input type="text" id="remoteFileName" toolTip='<fmt:message bundle="${resources}" key="ftp.fileNameRule" />'
						class="normalInput w300" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   <label><fmt:message bundle="${resources}" key="ftp.localFile" /></label>
	                </td>
	                <td>
	                    <span id="localFile" name="localFile"></span>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a onclick="return ajaxFileUpload();" id="confirm" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i><fmt:message bundle="${resources}" key="ftp.upload" /></span></a></li>
		         <li><a onclick="cancleClick()" id="cancel"  href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
	</div>


<div class=formtip id=tips style="display: none"></div>
<div style="display: none;">
<a href="javascript:trapProgressBar();" onclick="window.status='<fmt:message bundle="${resources}" key="sys.finished" />';return true;" id="trapProgress" style="display: none" ></a>
</div>
</body>
</html>