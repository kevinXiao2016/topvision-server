<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources"
	var="resources" />
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>



<script type="text/javascript">
var cm = null;
var store = null;
var grid = null;
var tbar = null;

//除去字符串前后的空格
function trim(s) {
	s = s.replace(/^\s+/, "");
	return s.replace(/\s+$/, "");
}

//获取文件列表
function getFileList() {
	//如果是初次加载，则创建store对象并绑定数据源
	//否则直接reload()
	if (store == null) {
		cm = new Ext.grid.ColumnModel([ {
			header : '<div class="txtCenter">' + I18N.ftp.fileName + '</div>',
			id : 'fileNames',
			dataIndex : 'name',
			width : 400,
			align : "left",
			sortable : true,
			resizable : false
		}, {
			header : '<div class="txtCenter">' + I18N.ftp.fileUpadteTime + '</div>',
			dataIndex : 'updateTime',
			width : 150,
			align : "left",
			sortable : true,
			resizable : false
		}, {
			header : '<div class="txtCenter">' + I18N.ftp.fileSize + '</div>',
			dataIndex : 'size',
			width : 80,
			align : "left",
			renderer : formatSize,
			sortable : true,
			resizable : false
		}, {
			header :'<div class="txtCenter">' + I18N.ftp.fileOperate + '</div>',
			dataIndex : 'operation',
			width : 100,
			align : "left",
			renderer : addOper,
			resizable : false
		} ]);

		store = new Ext.data.JsonStore({
			url : 'system/getTftpFileList.tv',
			root : 'data',
			totalProperty : 'fileNumber',
			fields : [ 'name', 'updateTime', 'size' ]
			/* ,sortInfo : {
				field : 'name',
				direction : 'asc'
			} */
		});
		store.load();

		tbar = new Ext.Toolbar([ '-', {
			text : I18N.ftp.refresh,
			handler : function() {
				grid.store.reload();
			}
		}, '-' ]);

		grid = new Ext.grid.EditorGridPanel({
			height : 320,
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			}),
			renderTo : 'grid',
			store : store,
			cm : cm,
			loadMask : true,
			tbar : tbar,
			cls:"normalTable"
		});
	} else {
		grid.store.reload();
	}
}

//格式化文件大小
function formatSize(value, p, record) {
	var str = value;
	if (str > 1024 && str < 1024 * 1024) {
		str = DecimalNum(str / 1024) + 'KB'
	} else if (str > 1024 * 1024 && str < 1024 * 1024 * 1024) {
		str = DecimalNum(str / (1024 * 1024)) + 'MB'
	} else if (str > 1024 * 1024 * 1024) {
		str = DecimalNum(str / (1024 * 1024 * 1024)) + 'GB'
	} else {
		str = str + 'B'
	}
	return str;
}

function DecimalNum(text) {
	text = text + "";
	var result = null;
	if (text.indexOf("\.") != -1) {
		var integer = text.split('\.')[0];
		if (text.split('\.')[1].length >= 2) {
			var decimal = text.split('\.')[1].substring(0, 2);
			result = integer + "." + decimal;
		} else {
			result = integer + "." + text.split('\.')[1];
		}
	} else {
		result = text
	}
	return result
}

//添加操作列
function addOper(value, p, record) {
	var str ="<a onclick='deleteFile(\""+record.get('name')+"\")' class='yellowLink'>"+I18N.engine.delete_+"</a> / <a class='yellowLink' title='" +I18N.sys.download +"'  onclick='downloadFile(\""+record.get('name')+"\")'>"+ I18N.sys.download +"</a>";
	return str;
}

//删除文件
function deleteFile(name) {
	window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.confirmDelete,
			function(confirm) {
				if (confirm == "ok") {
					var fileName = name;
					window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.isDeleting);
					$.ajax({
						url: '/system/deleteTftpFile.tv',
				    	type: 'POST',
				    	data: {fileName:fileName},
				    	dataType:"json",
				   		success: function(json) {
				   			window.parent.closeWaitingDlg();
				   			if(json.result==true){
				   				top.afterSaveOrDelete({
			     	  		      title: I18N.sys.tip,
			     	  		      html: '<b class="orangeTxt">'+I18N.ftp.deleteSuccess+'</b>'
			     	  		    });
				   				grid.store.reload();
				   			}else{
				   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.deleteFailed);
				   			}
				   		}, error: function(json) {
				   			window.parent.closeWaitingDlg();
				   			window.parent.showErrorDlg();
						}, cache: false,
						complete: function (XHR, TS) { XHR = null }
					});
				}
			});
}

//下载文件
function downloadFile(name){
	var remoteFileName = name;
	//直接下载
	window.location.href="/system/downloadTftpFile.tv?remoteFileName="+remoteFileName;
}

function upload(){
	window.top.createDialog('fileUpload', I18N.ftp.uploadFile, 600, 370, 'system/showTftpFileUpload.tv', null, true, true);
}

function cancleClick() {
	window.parent.closeWindow("fileManage");
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	//获取TFTP服务器目录下的所有文件
	getFileList();
});
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="grid"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="uploadFile" onclick="upload()"  href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i><fmt:message bundle="${resources}" key="ftp.uploadFile" /></span></a></li>
		         <li><a id="cancel" onclick="cancleClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
				
		
	</div>
		<div class=formtip id=tips style="display: none"></div>
</body>
</html>