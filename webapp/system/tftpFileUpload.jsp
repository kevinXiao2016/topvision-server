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
<script type="text/javascript" src="/js/FileUpload/TopvisionUpload.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>

<style type="text/css">

.selectDiv{
	margin: 20px;
}

.mainbody{
	filter: progid:DXImageTransform.Microsoft.gradient(startcolorstr=#E3EFFF,endcolorstr=#FFFFFF,gradientType=0); 
}

fieldset{
	border: 1px solid #7F9EB9;
}

fieldset div{
	clear: left;
	padding: 4px;
	padding-left: 70px;
	text-align: left;
	margin: 5px 0 10px 0;
}

fieldset div label{
	float: left;
	margin-left: -100px;
	width: 70px;
	padding: 4px 4px 0;
	text-align: right;
	vertical-align: middle !important;	
}

.operationDIV{
	margin-right: 22px;
	margin-bottom: 20px;
	text-align: right;
}


.selectDiv label {
	width: 100px;
	padding: 4px 4px 0 20px;
	text-align: right;
}

.selectDiv input {
	vertical-align: middle !important;	
}

.normalInput{
	width: 240px;
	padding-top: 2px;
	border: 1px solid #7F9EB9;
	background: #FFF;
	 height: 18px;
}

.normalInput:FOCUS{
	background: #FFFFBE;
	border: 1px solid #1E90FF;
}

</style>

<script type="text/javascript">
var myUpload;

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
} 

//文件上传时的验证函数
function validate(){
	//验证是否选择文件
	if(chooseFileName==null ||chooseFileName==""){
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
	//不做大小验证
	/* //验证文件大小，不能大于10M
	if(fileSize>10 * 1024 * 1024){
		window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.fileSizeLimit);
		return false;
	} */
	return true;
}

function uploadFile(){
	if(!validate()){return;}
	var remoteFileName = trim($("#remoteFileName").val());
	//验证所输入文件名是否等同于列表中某一项
	var isExists = hasSameFile(remoteFileName);
	if(isExists){
		window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.hasSameNameFile , function (confirm) {
			if(confirm == "ok"){
				//开始上传，给出等待框，并置上传按钮不可用
				window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.uploading);
				$("#confirm").attr("disabled",true);
				myUpload.upload('/system/uploadFileToTftp.tv?',{remoteFileName:remoteFileName});
			}
		});
	}else{
		//开始上传，给出等待框，并置上传按钮不可用
		window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.uploading);
		$("#confirm").attr("disabled",true);
		myUpload.upload('/system/uploadFileToTftp.tv?',{remoteFileName:remoteFileName});
	}
	return false;
}

function hasSameFile(remoteFileName){
	var records =  window.parent.getWindow("fileManage").body.dom.firstChild.contentWindow.store.getRange();
	var hasSameName = false;
	for(i=0; i<records.length; i++){
		var fileName = records[i].get('name');
		if( fileName==remoteFileName){
			hasSameName = true;
		}
	}
	return hasSameName;
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

$(document).ready(function(){
	//构造FLASH
	myUpload = new TopvisionUpload("selectFile");
	//为flash添加事件
	myUpload.onSelect = function(obj){
		fileSize = obj.size;
    	chooseFileName = obj.name;
    	//将文件名赋给两个输入框
    	$("#localFileName").val(chooseFileName);
    	//若未填写文件名，将文件名赋给文本框：服务器端名称
    	var len = $.trim($("#remoteFileName").val()).length;
    	if(len == 0){
    	$("#remoteFileName").val(chooseFileName);
    	}
	};
	myUpload.onComplete = function(result){
		//上传完成后的操作，置值为空，并关闭等待框,使上传按钮可用
		$("#remoteFileName").val('');
    	$("#localFileName").val('');
    	window.parent.closeWaitingDlg();
    	$("#confirm").attr("disabled",false);
		if(result=="false"){
			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.uploadFailed);
		}else{
			top.afterSaveOrDelete({
		      title: I18N.sys.tip,
		      html: '<b class="orangeTxt">'+I18N.ftp.uploadSuccess+'</b>'
		    });
			//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.uploadSuccess);
			cancleClick();
		}
	};
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
	                    <input type="text" id="remoteFileName" class="normalInput"
					onfocus="inputFocused('remoteFileName', '<fmt:message bundle="${resources}" key="ftp.fileNameRule" />')"
					onblur="inputBlured(this);" onclick="clearOrSetTips(this);" />
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   <label><fmt:message bundle="${resources}" key="ftp.localFile" /></label>
	                </td>
	                <td>
	                    <input class="normalInput" id="localFileName" style="width: 160px;" disabled="disabled"/>
				<button type="button" id="selectFile" class="BUTTON75"
					onmouseover="this.className='BUTTON_OVER75'"
					onmouseout="this.className='BUTTON75'"
					onmousedown="this.className='BUTTON_PRESSED75'"><fmt:message bundle="${resources}" key="ftp.selectFile" /></button>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
		         <li><a onclick="return uploadFile();" id="confirm" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${resources}" key="ftp.upload" /></span></a></li>
		         <li><a onclick="cancleClick()" id="cancel"  href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
	</div>
	
<div class=formtip id=tips style="display: none"></div>
<div style="display: none;">
<a href="javascript:trapProgressBar();" onclick="window.status='<fmt:message bundle="${resources}" key="sys.finished" />';return true;" id="trapProgress" style="display: none" ></a>
</div>
</body>
</html>