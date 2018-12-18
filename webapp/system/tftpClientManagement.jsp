<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>
<script type="text/javascript" src="/js/jQueryFileUpload/ajaxfileupload.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
var tftpClientJson = ${tftpClientJson},
	ipReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
	fileNameReg = /^[^\\\/\:\*\?\"\<\>\|]+$/,
	fileName = '';
$(document).ready(function(){
	$('#ip').val(tftpClientJson.ip);
	$('#port').val(tftpClientJson.port);
	
	//为IP输入框绑定事件，只允许输入0-9和.
	$('#ip').bind('keydown',function(event){
		var keyCode = event.which;
		if((48<=keyCode && keyCode<=57) || (96<=keyCode && keyCode<=105)){
			//输入的是0-9
		}else if(keyCode==110 || keyCode==190){
			//输入的是.
		}else if(keyCode!=8 && keyCode!=46){
			event.returnValue=false;  
            return false;
		}
	}).bind('change keyup',function(event){
		checkIpValid()
	});
	
	//为端口输入框绑定事件，只允许输入0-9
	$('#port').bind('keydown',function(event){
		var keyCode = event.which;
		if((48<=keyCode && keyCode<=57) || (96<=keyCode && keyCode<=105)){
			//输入的是0-9
		}else if(keyCode!=8 && keyCode!=46){
			event.returnValue=false;  
            return false;
		}
	}).bind('change keyup',function(event){
		var port = parseInt($('#port').val());
		if(port<0){
			port=0;
		}else if(port>65535){
			port = 65535;
		}
		$('#port').val(port)
	});
	//为文件名输入框绑定事件
	$('#remoteFileName').bind('keypress',function(event){
		var keyCode = event.which,
			illegalArray = [34, 42, 58, 60, 62, 63, 124, 191, 220];
		if($.inArray(keyCode, illegalArray)!=-1){
			event.returnValue=false;  
            return false;
		}
	});
	$('#modifyTftpClient').bind('click',function(){modifyTftpClient()});
	$('#downloadFile').bind('click',function(){downloadFile()});
	$('#uploadFile').bind('click',function(){uploadFile()});
	
	$("#localFile").fakeUpload("init",{    
		"tiptext":'@ftp.emptyFileDeined@' ,
		"width":200,
		"btntext":'@ftp.selectFile@',
		"checkfn":function(filePath,name){
			//获取文件名
			var reg = /[^\\]+\.[\w]+$/;
			fileName = reg.exec(filePath);
			return true;
		}
	}); 
	
	function checkIpValid(){
		if(!ipReg.test($('#ip').val())){
			$('#ip').css({
				border:'1px solid red'
			});
			return false;
		}else{
			$('#ip').css({
				border:'1px solid #B7B8CA'
			});
			return true;
		}
	}
	
	function modifyTftpClient(){
		if(!checkIpValid()){
			$('#ip').focus();
			return;
		}
		var ip = $('#ip').val(), port = $('#port').val();
		if(ip==tftpClientJson.ip && port==tftpClientJson.port){
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@Common.saveSuc@</b>'
   		    });
		}
		//修改TFTP客户端
		$.ajax({
			url: '/system/modifyTftpClient.tv',
	    	type: 'POST',
	    	data:{ip:$('#ip').val(), port:$('#port').val()},
	    	dataType:"json",
	   		success: function() {
	   			top.afterSaveOrDelete({
					title: '@COMMON.tip@',
	   		      	html: '<b class="orangeTxt">@Common.saveSuc@</b>'
	   		    });
	   			tftpClientJson.ip = ip;
	   			tftpClientJson.port = port;
	   		},error:function(){
	   			window.parent.showMessageDlg('@COMMON.tip@', '@Common.saveFailed@');
	   			/* top.afterSaveOrDelete({
					title: '@COMMON.tip@',
	   		      	html: '<b class="alert-danger">@Common.saveFailed@</b>'
	   		    }); */
	   		},cache: false,
	   		complete: function (XHR, TS) { XHR = null }
		});
	}
	
	function downloadFile(){
		//校验文件名是否合法
		var remoteFileName = $('#remoteFileName').val();
		if(!fileNameReg.test(remoteFileName)){
			$('#remoteFileName').focus().css({
				border:'1px solid red'
			});
			return false;
		}
		$('#remoteFileName').css({
			border:'1px solid #B7B8CA'
		});
		window.top.showWaitingDlg('@sys.waiting@', '@tftp.downloading@');
		$.ajax({
			url: '/system/loadTftpFileToServer.tv',
	    	type: 'POST',
	    	data:{remoteFileName:remoteFileName},
	    	dataType:"json",
	   		success: function(json) {
	   			window.parent.closeWaitingDlg();
	   			if(!json.success){
	   				window.parent.showMessageDlg('@COMMON.tip@', '@Common.fileDownloadFailed@');
	   				/* top.afterSaveOrDelete({
						title: '@COMMON.tip@',
		   		      	html: '<b class="alert-danger">@Common.fileDownloadFailed@</b>'
		   		    }); */
	   			}else{
	   				//文件已下载到服务器端,可以下载到本地
	   				window.location.href="/system/tftpFileDownload.tv?remoteFileName="+$('#remoteFileName').val();
	   			}
	   		},error:function(json){
	   			window.parent.closeWaitingDlg();
	   			window.parent.showMessageDlg('@COMMON.tip@', '@Common.fileDownloadFailed@');
	   			/* top.afterSaveOrDelete({
					title: '@COMMON.tip@',
	   		      	html: '<b class="alert-danger">@Common.fileDownloadFailed@</b>'
	   		    }); */
	   		},cache: false,
	   		complete: function (XHR, TS) { XHR = null }
		});
	}
	
	function uploadFile(){
		//需要验证文件名和文件大小(不能包含中文)
		var reg = /[\u4E00-\u9FA5]/g;
		if(reg.test(fileName)){
			//window.parent.showMessageDlg('@COMMON.tip@', '@tftp.fileNameRule2@');
			top.afterSaveOrDelete({
				title: '@COMMON.tip@',
   		      	html: '<b class="orangeTxt">@tftp.fileNameRule2@</b>'
   		    }); 
			return;
		}
		
		window.top.showWaitingDlg('@sys.waiting@', '@Common.uploading@');
		$.ajaxFileUpload({
			url:'/system/upLoadFileToTftpServer.tv',
			secureuri:false,
			timeout: 3000000,
			fileElementId:'localFile',
			data: {remoteFileName:fileName},
			dataType: 'json',
			success: function (data, status){
				window.parent.closeWaitingDlg();
				if(data.code=="1"){
					top.afterSaveOrDelete({
						title: '@COMMON.tip@',
		   		      	html: '<b class="orangeTxt">@Common.uploadSuc@</b>'
		   		    });
				}else{
					window.parent.showMessageDlg('@COMMON.tip@', '@Common.uploadFailed@');
					/* top.afterSaveOrDelete({
						title: '@COMMON.tip@',
		   		      	html: '<b class="alert-danger">@Common.uploadFailed@</b>'
		   		    }); */
				}
				location.reload();
	      	}, error: function (data, status, e){
	      		window.parent.closeWaitingDlg();
	      		window.parent.showMessageDlg('@COMMON.tip@', '@Common.uploadFailed@');
	      		/* top.afterSaveOrDelete({
					title: '@COMMON.tip@',
	   		      	html: '<b class="alert-danger">@Common.uploadFailed@</b>'
	   		    }); */
	      		location.reload();
	        },cache: false,
			complete: function (XHR, TS) {XHR = null ;}
		});
	}
});

function cancleClick(){
	window.parent.closeWindow("tftpClientManage");
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@tftp.topTip@</div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	
	<div class="edge10">
		<div class="zebraTableCaption" class="paramForm">
			<div class="zebraTableCaptionTitle"><span>@ftp.basicParam@</span></div>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt w110">@tftp.ip@</td>
					<td width="300"><input id="ip" type="text" class="normalInput" style="width: 264px;" toolTip="@ftp.ipRule@" /></td>
					<td class="rightBlueTxt">@tftp.port@</td>
					<td class="w80"><input class="normalInput" id="port" type="text" style="width:50px" toolTip="@ftp.portRule@" /></td>
					<td><a id="modifyTftpClient" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></td>
				</tr>
			</table>
		</div>
	</div>
	
	<div class="edge10">
		<div class="zebraTableCaption" class="paramForm">
			<div class="zebraTableCaptionTitle"><span>@Common.sendFile@</span></div>
			<form action="">
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="rightBlueTxt w110">@Common.localFileName@</td>
						<td width="300"><span id="localFile" name="localFile"></span></td>
						<td><a id="uploadFile" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrUp"></i>@Common.upload@</span></a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<div class="edge10">
		<div class="zebraTableCaption" class="paramForm">
			<div class="zebraTableCaptionTitle"><span>@Common.downloadFile@</span></div>
			<table cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
		           		<td class="rightBlueTxt w110">@Common.remoteFileName@</td>
		                <td width="300"><input id="remoteFileName" type="text" class="normalInput" style="width:264px;" toolTip='@Common.fileNameRule@' /></td>
						<td><a id="downloadFile" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrDown"></i>@Common.download@</span></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	 <div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	         <li><a onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@sys.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>