<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
	<title></title>
	<%@ include file="/include/nocache.inc"%>
	<%@ include file="/include/cssStyle.inc"%>
	<Zeta:Loader>
	    library ext
	    library jquery
	    library zeta
	    library FileUpload
	    module cmc
	    import js/jquery/Nm3kTabBtn
	    CSS js/webupload/webuploader
		IMPORT js/webupload/webuploader.min
	</Zeta:Loader>
	<style type="text/css">
		#picker .miniIcoEdit{
			width: 16px; height:16px; overflow:hidden; display:block; float:left; margin-right:2px;
		}
		#picker .webuploader-pick{
			padding: 7px 20px;
			border-left: 1px solid #ccc;
			-webkit-border-radius: 3px;
			-moz-border-radius: 3px;
			-ms-border-radius: 3px;
			-o-border-radius: 3px;
			border-radius: 3px;
		}
	</style>
	<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
	<script type="text/javascript">
var uploader = null;
var cmcId = ${ cmcId };
var textArea = "";
var flash;
var currentTab = 0;
function closeClick() {
	window.parent.closeWindow('distributeConfig');
}

$(function(){
	var tab = new Nm3kTabBtn({
	    renderTo:"putBtn",
	    callBack:"showOne",
	    tabs:["@network/ConfigOrder@","@network/ExecResult@"]
	});
	tab.init();
	
	showOne(0);
	newCreateUpload();
	
	/* flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        var chooseFileName = obj.name;
        var length = chooseFileName.length;
        var extension = "";
        if(length > 4){
        	var start = length - 4;
            var end = length;
            extension = chooseFileName.substring(start, end);
        }
        if(extension != '.cfg'){
        	window.parent.showMessageDlg('@network/COMMON.tip@', String.format("@network/FILETYPEERROR@", chooseFileName));
            return;
        }
        if(obj.size > 100*1024*1024){
            window.parent.showMessageDlg('@network/COMMON.tip@', String.format('@network/NAMEIMPORT.select100MFile@', obj.name));
            return;
        }else{
            $("#fileInput").val(chooseFileName);
        }
        importConfig()
    };
    flash.onComplete =function(result){
    	result = Ext.util.JSON.decode(result);   
    	Ext.getBody().unmask();
        if(!result.success){
            window.parent.showMessageDlg('@network/COMMON.tip@', '@network/NAMEIMPORT.fileError@');
            return;
        }
        var configText = "";
    	$.each(result.configList, function(index, config){
    		configText = configText + config + "\n";
    	})	
    	$("#inputText").val(configText);
    	showOne(0)
    }; */
    getConfig();
})

function importConfig(){
	var fileName = $("#fileInput").val();
	if( fileName ){
	   Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + '@network/NAMEIMPORT.readingFile@');
	   flash.upload("/cmc/importCommonConfig.tv");
	} else {
		window.parent.showMessageDlg('@network/COMMON.tip@', '@network/NAMEIMPORT.chooseFile@');
	}
}

	
function getConfig(){
	$.ajax({
		url: "/cmc/readCommonConfig.tv?cmcId=" + cmcId,
		type: "post",
		success: function(response){
			$("#inputText").val(response);
		}
	})
}
function distributeConfig(){
	$("#putBtn ul li:eq(1)").click();
	/* if(hasChinese){
		window.parent.showMessageDlg('@network/COMMON.tip@', '@network/CONFIGFILEERROR@');
		return;
	} */
	$.ajax({
		url: "/cmc/sendCommonConfig.tv?cmcId=" + cmcId,
		type: "post",
		success: function(json){
			$("#outputText").val(json);
			showOne(1)
		}
	}) 
}

function showOne(num){
	currentTab = num
	var $jsShow = $(".jsShow")
	$jsShow.css("display","none");
	$jsShow.eq(num).fadeIn(function(){
		//$jsShow.css("display","none");
	});
}

//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '/js/webupload/Uploader.swf',
	    server: '/cmc/importCommonConfig.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    accept: {
	    	extensions: 'cfg'
	    },
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 100*1024*1024,
	    auto: true,
	    timeout: 0/* ,
	    runtimeOrder :'flash' */
	});
	uploader.on( 'beforeFileQueued', function( file ) {
		//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
		uploader.reset();
		//console.log('beforeFileQueued')
	});
	//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
	uploader.on( 'fileQueued', function( file ) {
		//上传框中传入文件名+后缀;
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, result) {
		//拿到返回值，再去做其它事情;
		clearMask();
		if(!result.success){
            window.parent.showMessageDlg('@network/COMMON.tip@', '@network/NAMEIMPORT.fileError@');
            return;
        }
        var configText = "";
    	$.each(result.configList, function(index, config){
    		configText = configText + config + "\n";
    	})	
    	$("#inputText").val(configText);
    	showOne(0)
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	/*uploader.on( 'uploadComplete', function( file ) {
		console.log('uploadComplete')
		var json = uploader.getStats();
		if(json && json.successNum){
			console.log( String.format('成功上传{0}个文件', json.successNum) )
		}
		if(json && json.uploadFailNum){
			console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
		}
	});*/
	uploader.on( 'error', function(err, file) {
		switch(err){
    		case 'Q_TYPE_DENIED':
    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fileErrType@' ,null)
				//console.log('您选择的文件类型不符合规范');
				break;
    		case 'Q_EXCEED_SIZE_LIMIT':
    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.sizeLimit@' ,null)
    			//console.log('您选择的文件大小超出了范围');	
    			break;
		}
	});
}
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	@network/OPENWINTIP@
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<input id="fileInput" type="hidden"></input>
	<div id="putBtn" style="padding:5px"></div>
	<div id = "inputDiv" class="jsShow" style="margin: 5px; width: 99%">
		<textarea style="width: 98%" rows="25"  id="inputText" disabled>
		</textarea>
	</div>
	
	<div id = "outDiv" class="jsShow" style="margin: 5px; width: 99%">
		<textarea style="width: 98%" rows="25"  id="outputText" disabled>
		</textarea>
	</div>
	<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		     	 <li>
		     	 <div class="btns">
                 	<div id="picker"><i class="miniIcoEdit"></i>@network/MODIFYCONFIG@</div>
                 </div>
		     
		     	 <!-- <li><a onclick="" href="javascript:;" id="chooseFile" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@network/MODIFYCONFIG@</span></a></li> -->
		         <li><a  onclick="distributeConfig()" id=saveButton href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@network/EXEC@</span></a></li>
		         <li><a onclick="closeClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>