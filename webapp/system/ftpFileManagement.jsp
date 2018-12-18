<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
    CSS css/white/disabledStyle
</Zeta:Loader>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var ftpConnect = ${ftpConnect};
var cm = null;
var store = null;
var grid = null;
var tbar = null;

//除去字符串前后的空格
function trim(s)
{
   s = s.replace(/^\s+/,"");
   return s.replace(/\s+$/,"");
}

//设置列是否可排序,sortable为true或false
function setSortable(sortable){
	if(typeof(sortable)=="boolean"){
		if(cm!=null){
			cm.setConfig([
	  			{header:I18N.ftp.fileType, dataIndex: 'type', align:"center", hidden: true, resizable: false},
				{header:I18N.ftp.filePath, dataIndex: 'pathName', align:"center", hidden: true, resizable: false},
				{header:I18N.ftp.fileName, id:'fileNames', dataIndex: 'name', width: 280, align:"center", sortable: sortable, renderer: addIcon, resizable: false},
				{header:I18N.ftp.fileUpadteTime, dataIndex: 'updateTime', width: 150, align:"center", sortable: sortable, resizable: false},
				{header:I18N.ftp.fileSize, dataIndex: 'size', width: 80, align:"center", renderer: formatSize, sortable: sortable, resizable: false},
				{header:I18N.ftp.fileOperate, dataIndex: 'operation', width: 100, align:"center", renderer: addOper, resizable: false}
			]);
		}
	}else{
		return ;
	}
}

//获取当前所在的FTP目录
function getWorkDir(){
	var ip = ftpConnect.ip;
	var port = ftpConnect.port;
	var userName = ftpConnect.userName;
	var pwd = ftpConnect.pwd;
	var remotePath = ftpConnect.remotePath;
	$.ajax({
		url: '/system/getWorkDir.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath},
    	dataType:"json",
   		success: function(json) {
   			if(json.success==false){
   				//获取路径出错
   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.failedToChangeDir);
   			}else{
   				$("#currentDir").val(json.workDir);
   	   			ftpConnect.remotePath = json.workDir;
   	   			if(store!=null){
   	   				grid.store.baseParams.remotePath = json.workDir;
   	   			}
   	   			//获取当前目录下的文件列表
   	   			getFileList();
   			}
   		}, error: function(json) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//获取当前目录下的文件列表
function getFileList(){
	//如果是初次加载，则创建store对象并绑定数据源
	//否则直接reload()
	if(store==null){
		cm = new Ext.grid.ColumnModel([
			{header:I18N.ftp.fileType, dataIndex: 'type', align:"center", hidden: true, resizable: false},
			{header:I18N.ftp.filePath, dataIndex: 'pathName', align:"center", hidden: true, resizable: false},
			{header:'<div class="txtCenter">' + I18N.ftp.fileName + '</div>', id:'fileNames', dataIndex: 'name', width: 280, align:"center", sortable: true, resizable: false, renderer: addIcon},
			{header:'<div class="txtCenter">' + I18N.ftp.fileUpadteTime + '</div>', dataIndex: 'updateTime', width: 150, align:"center", sortable: true, resizable: false},
			{header:'<div class="txtCenter">' + I18N.ftp.fileSize + '</div>', dataIndex: 'size', width: 80, align:"center", renderer: formatSize, sortable: true, resizable: false},
			{header:I18N.ftp.fileOperate, dataIndex: 'operation', width: 100, align:"center", renderer: addOper, resizable: false}
		]);
		
		store = new Ext.data.JsonStore({
		    url: 'system/getFtpFileList.tv',
		    baseParams: {ip:ftpConnect.ip, port:ftpConnect.port, userName:ftpConnect.userName, pwd:ftpConnect.pwd, remotePath:ftpConnect.remotePath},
	        root: 'data',
	        totalProperty:'fileNumber',
	        fields:['type', 'pathName', 'name', 'updateTime', 'size'],
	        sortInfo:{field:'type',direction:'asc'},
	        //自定义排序规则
	        sortData:function(f, direction){ 
	        	var st = this.fields.get(f).sortType;
	        	var fn = function(record1, record2){ 
	        		//文件夹永远排在文件前面
	        	 	if(record1.get('type')==record2.get('type')){
	        			var value1 = st(record1.data[f]), value2 = st(record2.data[f]);  
	        			return (value1 > value2) ? 1 : ((value1 < value2) ? -1 : 0);
	        		}else{
	        			return (record1.get('type')=="1") ? -1 : 1;
	        		} 
	        	};
	        	this.data.sort(direction, fn);
	        	if(this.snapshot && this.snapshot != this.data){  
	        		this.snapshot.sort(direction, fn);  
	        	} 
	        }
		});	
		store.load();
		
		tbar = new Ext.Toolbar(['-', {
			text: I18N.ftp.goback ,
			iconCls: 'bmenu_back',
			handler: function(){
				//返回上级目录
				var currentPath = $("#currentDir").val();
				if(currentPath=="/"){
					window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.AtRootPath);
				} else{
					//获取当前路径的父目录
					var reg = new RegExp("(.*/)");
					var prevFolder = reg.exec(currentPath);
					//改变全局变量ftpConnect的remotePath
					ftpConnect.remotePath = prevFolder[0];		
					getWorkDir();
				}
			}
		}, '-',{
			text: I18N.ftp.createFolder,
			iconCls: 'bmenu_new',
			handler: function(){
				//创建文件夹
				//设置工具条、上传文件按钮、排序为不可用（传递出的意思：先把这事做完，或者退出）
				tbar.disable();
				$("#uploadFile").attr('disabled',true);
				setSortable(false);
				//拼接好放在record区的内容
				var pathName = $("#currentDir").val();
				var updateTime = "";
				var name = newFolderCreateArea();
				var record = new Ext.data.Record({     
					type:'3',  
					pathName: pathName,  
					name: name, 
					updateTime:updateTime, 
					size:'0'
	               });
				grid.stopEditing();   
	            store.insert(0,record);
	            grid.startEditing(0,0);
	            //
	            $("#newFolderName").focus(function(){
	            	inputFocused('newFolderName', '<fmt:message bundle="${resources}" key="ftp.pathRule" />')
	        	});	
	            $("#newFolderName").blur(function(){
	            	inputBlured(this);
	            });
	            $("#newFolderName").click(function(){
	            	clearOrSetTips(this);
	            });
			}
		}, '-', {
			text: I18N.ftp.refresh,
			iconCls: 'bmenu_refresh',
			handler: function(){
				//刷新时先去检测可达性，可达才去reload
				$.ajax({
					url: '/system/getFtpConnectStatus.tv',
			    	type: 'POST',
			    	dataType:"json",
			   		success: function(json) {
			   			if(json.ftpConnectStatus.reachable==true){
			   				grid.store.reload();
			   			}else{
			   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.unReachable);
			   			}
			   		},error:function(json){
			   			window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.unReachable);
			   		},cache: false,
			   		complete: function (XHR, TS) { XHR = null }
				});
			}
		},'-']);
		
		grid = new Ext.grid.EditorGridPanel({
			height: 290,
			sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
			renderTo: 'grid',
			store: store,
			cm: cm,
			loadMask: true,
			tbar: tbar,
			bodyCssClass: 'normalTable',
			viewConfig:{
				forceFit: true
			}
		});
		
		grid.addListener('rowdblclick', function(){
			grid.getSelectionModel().each(function(record){  
		    	//对于文件夹执行进入操作
		    	if(record.get('type')=='1'){
		    		var remotePath = record.get('pathName') + '/' + record.get('name');
		    		//改变全局变量ftpConnect的remotePath
		    		ftpConnect.remotePath = remotePath;
		    		//刷新表格数据
		    		getWorkDir();
		    	}
		    }); 
		});
	}else{
		grid.store.reload();
	}
}

//确定创建文件夹
function confirmNewFolder(){
	var folderName = trim($("#newFolderName").val());
	//验证输入的文件夹名称是否合法,文件名为中文、字母、数字、-_(),且不能超过32位
	var reg = /^[\w\-\(\)\u4E00-\u9FA5]+$/;
	if(!reg.test(folderName) || folderName.length>=32){
		$("#newFolderName").focus();
	}else{ 
		//检测是否存在同名文件夹
		var records =  store.getRange();
		var hasSameName = false;
		for(i=0; i<records.length; i++){
			var fileName = records[i].get('name');
			if( records[i].get('name')==folderName){
				hasSameName = true;
			}
		}
		if(hasSameName){
			top.afterSaveOrDelete({
		      title: I18N.sys.tip,
		      html: '<b class="orangeTxt">'+I18N.ftp.hasSameFolder+'</b>'
		    });
			//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.hasSameFolder);
			$("#newFolderName").focus();
		}else{
			//删除这一行
			grid.store.removeAt(0);
			//设置工具条、上传文件按钮、排序为可用
			tbar.enable();
			$("#uploadFile").attr('disabled',false);
			setSortable(true);
			//创建文件夹
			var ip = ftpConnect.ip;
			var port = ftpConnect.port;
			var userName = ftpConnect.userName;
			var pwd = ftpConnect.pwd;
			var remotePath = $("#currentDir").val();
			$.ajax({
				url: '/system/newFolder.tv',
		    	type: 'POST',
		    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath, fileName:folderName},
		    	dataType:"text",
		   		success: function(result) {
		   			if(result=="true"){
		   				grid.store.reload();
		   			}else{
		   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.createFolderFailed);
		   			}
		   		}, error: function(result) {
		   			window.parent.closeWaitingDlg();
		   			window.parent.showErrorDlg();
				}, cache: false,
				complete: function (XHR, TS) { XHR = null }
			});
		}
	}	
}

//取消创建文件夹
function cancelNewFolder(){
	//删除这一行
	grid.store.removeAt(0);
	//设置工具条、上传文件按钮、排序为可用
	tbar.enable();
	$("#uploadFile").attr('disabled',false);
	setSortable(true);
}

//创建文件夹时的文件名称区域内容
function newFolderCreateArea(){
	var str = "";
	str += "<input id='newFolderName' type='text'/>";
	str += " <a id='confirmNewFolder' href='javascript:;' onclick='confirmNewFolder()'>@COMMON.ok@</a>  ";
	str += "  <a id='cancelNewFolder' href='javascript:;' onclick='cancelNewFolder()'>@COMMON.cancel@</a>";
	return str;
}

//给文件标题添加图标
function addIcon(value, p, record){
		var str;
		if(record.get('type')=='1' || record.get('type')=='3'){
			str= "<input type='image' src='/images/folder.gif'>" + record.get('name');
		}else{
			str= "<input type='image' src='/images/txt.png'>" + record.get('name');
		}
		return str;
}

//格式化文件大小
function formatSize(value, p, record){
	var str = value;
	if(record.get('type')=='0'){
		if(str>1024&&str<1024*1024){
			str = DecimalNum(str/1024) + 'KB'
		}else if(str>1024*1024&&str<1024*1024*1024){
			str = DecimalNum(str/(1024*1024)) + 'MB'
		}else if(str>1024*1024*1024){
			str = DecimalNum(str/(1024*1024*1024)) + 'GB'
		}else {
			str = str + 'B'
		}
	}else{
		str = '';
	}
	return str;
}

function DecimalNum(text){
	text = text+"";
	var result = null;
	if(text.indexOf("\.")!=-1){
       var integer = text.split('\.')[0];
       if(text.split('\.')[1].length >= 2){
           var decimal = text.split('\.')[1].substring(0,2);
           result = integer+"."+decimal;
       }else{
    	   result = integer+"."+text.split('\.')[1];
           }
	}else{
       result = text
	}
	return result
} 

//添加操作列
function addOper(value, p, record){
	if(record.get('type')=='0'){		
		//var str ="<input type='image' src='/images/system/erase.png' onclick='deleteFile(\""+record.get('type')+"\",\""+record.get('name')+"\",\""+record.get('pathName')+"\")'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='image' src='/images/download.gif'  onclick='downloadFile(\""+record.get('name')+"\",\""+record.get('pathName')+"\")'>";
		var str = "<a href='javascript:;' onclick='deleteFile(\""+record.get('type')+"\",\""+record.get('name')+"\",\""+record.get('pathName')+"\")'>@COMMON.del@</a> / <a href='javascript:;'  onclick='downloadFile(\""+record.get('name')+"\",\""+record.get('pathName')+"\")'>@COMMON.downloadHeader@</a>";
		return str;
	}else if(record.get('type')=='1'){
		var str ="<a href='javascript:;' onclick='deleteFile(\""+record.get('type')+"\",\""+record.get('name')+"\",\""+record.get('pathName')+"\")'>@COMMON.del@</a>";
		return str;
	}
}

//下载文件
function downloadFile(name, pathName){
	var ip = ftpConnect.ip;
	var port = ftpConnect.port;
	var userName = ftpConnect.userName;
	var pwd = ftpConnect.pwd;
	var remotePath = pathName;
	var fileName = name;
	//发出请求将FTP文件下载到服务器端
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.loadingFile);
	$.ajax({
		url: '/system/loadFtpFileToServer.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath, fileName:fileName},
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
   			if(json.success==false){
   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.downloadFailed);
   			}else{
   				//文件已下载到服务器端,可以下载到本地
   				window.location.href="/system/downloadFtpFile.tv?serverFileName="+json.serverFilePathName+"&fileName="+fileName;
   			}
   		}, error: function(json) {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

//删除文件/文件夹
function deleteFile(type, name, pathName){
	window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.confirmDelete , function (confirm) {
		if(confirm == "ok"){
			var ip = ftpConnect.ip;
			var port = ftpConnect.port;
			var userName = ftpConnect.userName;
			var pwd = ftpConnect.pwd;
			var remotePath = pathName;
			var fileName = name;
			var fileType = type;
			//判断删除的是文件还是文件夹，如果是文件夹需要去检测其是否为空，不为空则不允许删除
			if(type==1){
				//检测是否为空
				remotePath = remotePath + '/' + fileName;
				$.ajax({
					url: '/system/getFtpFileList.tv',
			    	type: 'POST',
			    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath, fileName:fileName, fileType:fileType},
			    	dataType:"json",
			   		success: function(json) {
			   			if(json.success==false){
			   				
			   			}else{
			   				if(json.data.length>0){
			   					//文件夹不为空,不允许删除
			   					window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.notAnEmptyFolder);
			   				}else{
			   					remotePath = pathName;
			   					deleteFileAjax(ip, port, userName, pwd, remotePath, fileName, fileType);
			   				}
			   			}
			   		}, error: function(json) {
			   			window.parent.closeWaitingDlg();
			   			window.parent.showErrorDlg();
					}, cache: false,
					complete: function (XHR, TS) { XHR = null }
				});
			}else{
				//文件可以直接删除
				deleteFileAjax(ip, port, userName, pwd, remotePath, fileName, fileType);
			}
		}
	});
}

//删除文件/文件夹的ajax操作
function deleteFileAjax(ip, port, userName, pwd, remotePath, fileName, fileType){
	window.top.showWaitingDlg(I18N.sys.waiting, I18N.ftp.isDeleting);
	$.ajax({
		url: '/system/deleteFtpFile.tv',
    	type: 'POST',
    	data: {ip:ip, port:port, userName:userName, pwd:pwd, remotePath:remotePath, fileName:fileName, fileType:fileType},
    	dataType:"text",
   		success: function(result) {
   			window.parent.closeWaitingDlg();
   			if(result=="true"){
   				top.afterSaveOrDelete({
   			      title: I18N.sys.tip,
   			      html: '<b class="orangeTxt">'+I18N.ftp.deleteSuccess+'</b>'
   			    });
   				//window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.deleteSuccess);
   				grid.store.reload();
   			}else{
   				window.parent.showMessageDlg(I18N.sys.tip, I18N.ftp.deleteFailed);
   			}
   		}, error: function(result) {
   			window.parent.closeWaitingDlg();
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function upload(){
	window.top.createDialog('fileUpload', I18N.ftp.uploadFile, 600, 370, 'system/showFtpFileUpload.tv', null, true, true);
}

function cancleClick() {
	window.parent.closeWindow("fileManage");
}

Ext.onReady(function() {
	//这里面的remotePath应该是父页面的远程目录框里面的值
	//Ext.QuickTips.init();
	//在连接没有错误的情况下，去获取数据 
	getWorkDir();
});
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p>
				<span><fmt:message bundle="${resources}" key="ftp.currentDir" /></span>
				<input id="currentDir" disabled="disabled" type="text" class="normalInput" style="width:500px;" />
			</p>
	    </div>
	    <div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10">
		<div id="grid"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="uploadFile" onclick="upload()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i><fmt:message bundle="${resources}" key="ftp.uploadFile" /></span></a></li>
		         <li><a id="cancel" onclick="cancleClick()"  href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${resources}" key="sys.cancel" /></span></a></li>
		     </ol>
		</div>
	</div>
	<div id="tips"></div>
</body>
</Zeta:HTML>