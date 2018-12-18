<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    CSS js/webupload/webuploader
    IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<title>@SERVICE.fileMgmt@</title>
<script type="text/javascript" src="../js/wlan/error.js?v=ap2983"></script>
<style type="text/css">
#upload {padding:15px;}
#backgroundCanvas {position: absolute;background-color:#DFE8F6;border:1px solid #6593CF;width:400px;height:138px;z-index:-1000;}
.webuploader-pick{ border-left: 1px solid #B7B8CA; border-radius:3px;}

</style>

<script type="text/javascript">
var topOnuUpgradeSlotNum = '${topOnuUpgradeSlotNum}';
var topOnuUpgradeOnuType = '${topOnuUpgradeOnuType}';
var topOnuUpgradeFileName = '${topOnuUpgradeFileName}';
var topOnuUpgradeMode = '${topOnuUpgradeMode}';
var topOnuHwVersion;
var topOnuUpgradeOption;
var topOnuUpgradeOnuList = '${topOnuUpgradeOnuList}';
var topOnuUpgradeOperAction = '${topOnuUpgradeOperAction}';
var topOnuUpgradeStatus = '${topOnuUpgradeStatus}';
var onuUpgradeFileList = ${onuUpgradeFileList};
var onuHwList = ${onuHardwareVersionList};
var entityId = '${entityId}';
var boardGponType = '${boardGponType}';
var fileSize = 0;
var chooseFileName = "";
var chooseFilePath = "";
window.SelectionModel = "select";
window.top.addCallback("onuUpgrade",function(data){
	if(data == 'success'){
		window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.resetMessage@");
		cancelClick()
	}else{
		window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.uploadComEr@")
	}
});

/* function upload(obj) {
	topOnuUpgradeMode = Ext.getCmp('fileType').getValue();
	topOnuHwVersion = $("#hardwareVersion").val();
	if(topOnuUpgradeMode == 1){//TK
		topOnuUpgradeOption = $("#optionTK").val();
	}else{//CTC
		topOnuUpgradeOption = $("#optionCTC").val();
	}
    if (Ext.getCmp("onuUpdateName").isValid()) {
        chooseFileName = Ext.getCmp("onuUpdateName").getValue();
    	topOnuUpgradeFileName = Ext.getCmp('onuUpdateName').getValue()
    	if(topOnuUpgradeFileName == "@SERVICE.plsUploadFile@" ){
    		window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.plsUploadOnuFile@");
    		return;
        }
        if(window.SelectionModel == 'select'){
    	    window.parent.showConfirmDlg("@EPON.tip@", "@SERVICE.onuUpdateMessage@", function(type) {
	            if (type == 'no') {return;}
	            var data ="entityId=" + entityId + "&oltUpdateType=onuUpgrade&topOnuUpgradeSlotNum=" + topOnuUpgradeSlotNum + 
			                "&topOnuUpgradeOnuType=" + topOnuUpgradeOnuType + "&topOnuUpgradeFileName=" + topOnuUpgradeFileName + "&topOnuUpgradeMode=" + topOnuUpgradeMode + "&topOnuUpgradeOnuList=" + topOnuUpgradeOnuList + "&topOnuUpgradeOperAction=" +
			                topOnuUpgradeOperAction + "&topOnuUpgradeStatus=" + topOnuUpgradeStatus+"&topOnuHwVersion="+topOnuHwVersion+"&topOnuUpgradeOption="+topOnuUpgradeOption;
	            
	            window.top.showWaitingDlg("@COMMON.wait@", "@SERVICE.onuUpdating@" );
	            $.ajax({
	        		url: '/onu/onuUpdate.tv',data: data,cache: false
	        	});
        	});
        	
        }else{
        	if (!chooseFileName.length > 0) {
            	window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.plsSelectFileFirst@" , null);
        	} else {
            	if (fileSize > 50 * 1024 * 1024) {
                	window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.fileSizeBig@" , null);
           	    } else if (fileSize == 0) {
                    window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.fileSize0@" , null);
           	    } else {
           		  window.parent.showConfirmDlg("@EPON.tip@", "@SERVICE.onuUpdateMessage@", function(type) {
                  if (type == 'no') {
                     return
                 }
                 window.top.showWaitingDlg("@COMMON.wait@", "@SERVICE.onuUpdating@" );
                 thisFlash.upload("/epon/oltUpdate.tv?entityId=" + entityId + "&oltUpdateType=onuUpgrade&topOnuUpgradeSlotNum=" + topOnuUpgradeSlotNum + 
                	"&topOnuUpgradeOnuType=" + topOnuUpgradeOnuType + "&topOnuUpgradeFileName=" + topOnuUpgradeFileName + "&topOnuUpgradeMode=" + topOnuUpgradeMode + "&topOnuUpgradeOnuList=" + topOnuUpgradeOnuList + "&topOnuUpgradeOperAction=" +
                	topOnuUpgradeOperAction + "&topOnuUpgradeStatus=" + topOnuUpgradeStatus+"&topOnuHwVersion="+topOnuHwVersion+"&topOnuUpgradeOption="+topOnuUpgradeOption);
           		 }); 
            }
       	  }
       }
    }
} */
function cancelClick() {
	window.parent.closeWindow('onuUpgradeFileUpload');
}
Ext.onReady(function () {
    Ext.QuickTips.init();
     var sData = (boardGponType == 'false') ? [['4', 'CTC']] : 
    			[['16', 'baseline'],
                 ['32', 'extend']];
    
	var filechoose = new Ext.form.ComboBox({  //升级类型
		id: 'fileType',
		renderTo : 'updateType' ,
        forceSelection: true,
        store: new Ext.data.SimpleStore({
            fields: ['value', 'text'],
            /* data : [
            	['4', 'CTC'],
            	['16', 'baseline'],
            	['32', 'extend'],
            	['64', 'auto']
            ] */
            data : sData
        }),
        value: sData[0][0],
        width: 280,
        valueField: 'value',
        displayField: 'text',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        selectOnFocus:true, //用户不能自己输入,只能选择列表中有的记录  
        allowBlank: false
    });
	filechoose.on("select",function(c, r){
		if(r.data.value == 1){
			$("#optionTK").show();
			$("#optionCTC").hide();
		}else{
			$("#optionCTC").show();
			$("#optionTK").hide();
		}
	});
    var combo = new Ext.form.ComboBox({  //升级文件
        id : 'onuUpdateName',
        forceSelection: true,
        store: new Ext.data.JsonStore({
            fields: ['fileName'],
            data : onuUpgradeFileList
        }),
        value: typeof onuUpgradeFileList[0] != 'undefined' ? onuUpgradeFileList[0].fileName : "@SERVICE.plsUploadFile@" ,
        width: 280,
        valueField: 'fileName',
        displayField: 'fileName',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        selectOnFocus:true, //用户不能自己输入,只能选择列表中有的记录  
        allowBlank: false ,
        editable : false,
        listeners : {
			'select' : function(){
				window.SelectionModel = "select"
			}
        },
		applyTo : 'fileName'
    })
    $("#hardwareVersion").append('<option value="any">ANY</option>');
	for(var i=0;i<onuHwList.length;i++){
		$("#hardwareVersion").append('<option value="' + onuHwList[i] + '">' + onuHwList[i] + '</option>');
	}
 	/* window.thisFlash = new TopvisionUpload("chooseFile");
    
    thisFlash.onSelect = function (obj){
    	fileSize = obj.fileSize;
        chooseFileName = obj.name;
        window.SelectionModel = "view"
        Ext.getCmp("onuUpdateName").setValue(chooseFileName)
    }
    
    thisFlash.onComplete = function (result){
    	if (result == "inProgress") {
   	        window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.uploadBusy@" , 'error')
   	    } else if (result == "fileNotExists") {
   	        window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.uploadAg@" , 'error')
   	    } else {
   	    	window.parent.showMessageDlg("@COMMON.tip@", result, 'error');
   	    }
    } */
    
    createUpload();    
});



//创建上传控件;
function createUpload(){
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: '/epon/oltUpdate.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    /* accept: {
	    	extensions: 'xlsx'
	    }, */
	    duplicate: true,
	    resize: false,
	    fileNumLimit: 1,
	    fileSizeLimit: 50*1024*1024,
	    auto: false,
	    timeout: 0/*,
	    runtimeOrder :'flash'*/
	});
	uploader.on( 'beforeFileQueued', function( file ) {
		//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
		uploader.reset();
		//console.log('beforeFileQueued')
	});
	//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
	uploader.on( 'fileQueued', function( file ) {
		fileSize = file.fileSize;
        chooseFileName = file.name;
        window.SelectionModel = "view"
        Ext.getCmp("onuUpdateName").setValue(chooseFileName)
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, response) {
		clearMask();
		if(response.result == 'success'){
			top.afterSaveOrDelete({
		        title: '@COMMON.tip@',
		        html: '<b class="orangeTxt">@Business.onuUpdateSuc@</b>'
		    });
			cancelClick();
		}else{
			var result = response.result;
			if (result == "fileNotExists") {
				showErrorUploadMsg( "@UPGRADE.fileNotExists@" );
	        } else if (result == "fileNoTime"){
	        	showErrorUploadMsg( "@SERVICE.uploadSlow@" );
	        } else if(result == 'onuUpdateFail'){
	        	showErrorUploadMsg( "@Business.onuUpdateFail@" );
	        } else {
	        	showErrorUploadMsg( "@SERVICE.uploadEr@" );
	        }
		}
		//拿到返回值，再去做其它事情;
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	uploader.on( 'uploadComplete', function( file ) {
		clearMask();
		var json = uploader.getStats();
		if(json && json.successNum){
			//console.log( String.format('成功上传{0}个文件', json.successNum) )
		}
		if(json && json.uploadFailNum){
			//console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
		}
	});
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

function showErrorUploadMsg(message){
	top.showMessageDlg("@COMMON.tip@", message, 'error');
}

function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
function startUpload(){
	topOnuUpgradeMode = Ext.getCmp('fileType').getValue();
	topOnuHwVersion = $("#hardwareVersion").val();
	if(topOnuUpgradeMode == 1){//TK
		topOnuUpgradeOption = $("#optionTK").val();
	}else{//CTC
		topOnuUpgradeOption = $("#optionCTC").val();
	}
	if (Ext.getCmp("onuUpdateName").isValid()) {
        chooseFileName = Ext.getCmp("onuUpdateName").getValue();
    	topOnuUpgradeFileName = Ext.getCmp('onuUpdateName').getValue()
    	if(topOnuUpgradeFileName == "@SERVICE.plsUploadFile@" ){
    		window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.plsUploadOnuFile@");
    		return;
        }
    	 if(window.SelectionModel == 'select'){
    		 window.parent.showConfirmDlg("@EPON.tip@", "@SERVICE.onuUpdateMessage@", function(type) {
 	            if (type == 'no') {return;}
 	            var data ="entityId=" + entityId + "&oltUpdateType=onuUpgrade&topOnuUpgradeSlotNum=" + topOnuUpgradeSlotNum + 
 			                "&topOnuUpgradeOnuType=" + topOnuUpgradeOnuType + "&topOnuUpgradeFileName=" + topOnuUpgradeFileName + "&topOnuUpgradeMode=" + topOnuUpgradeMode + "&topOnuUpgradeOnuList=" + topOnuUpgradeOnuList + "&topOnuUpgradeOperAction=" +
 			                topOnuUpgradeOperAction + "&topOnuUpgradeStatus=" + topOnuUpgradeStatus+"&topOnuHwVersion="+topOnuHwVersion+"&topOnuUpgradeOption="+topOnuUpgradeOption;
 	            
 	            window.top.showWaitingDlg("@COMMON.wait@", "@SERVICE.onuUpdating@" );
 	            $.ajax({
 	        		url: '/onu/onuUpdate.tv',data: data,cache: false,
 	        		success:function(json){
 	        			top.closeWaitingDlg();
 	        			if(json && json.result == 'success'){
 	        				top.afterSaveOrDelete({
 		 	       		        title: '@COMMON.tip@',
 		 	       		        html: '<b class="orangeTxt">@Business.onuUpdateSuc@</b>'
 		 	       		    });
 		 	       			cancelClick();	
 	        			} else if (json.result == 'onuUpdateFail'){
 	        		       	showErrorUploadMsg("@Business.onuUpdateFail@");
 	        			} else {
 	        				showErrorUploadMsg("@SERVICE.uploadEr@");
 	        			}
 	        	    },
 	        	    error:function(err){
 	        	    	showErrorUploadMsg("@SERVICE.uploadEr@");
 	        	    }
 	        	});
         	});
    	 }else{
       		showBodyMask("@SERVICE.onuUpdating@")
       		uploader.options.formData = {
       			entityId: entityId,
       			oltUpdateType: 'onuUpgrade',
       			topOnuUpgradeSlotNum: topOnuUpgradeSlotNum,
       			topOnuUpgradeOnuType: topOnuUpgradeOnuType,
       			topOnuUpgradeFileName: topOnuUpgradeFileName,
       			topOnuUpgradeMode: topOnuUpgradeMode,
       			topOnuUpgradeOnuList: topOnuUpgradeOnuList,
       			topOnuUpgradeOperAction: topOnuUpgradeOperAction,
       			topOnuUpgradeStatus: topOnuUpgradeStatus,
       			topOnuHwVersion: topOnuHwVersion,
       			topOnuUpgradeOption: topOnuUpgradeOption
  			}
  			uploader.upload();
    	 }
	}
}

</script>
</head>
<body  class="openWinBody">
<div class="openWinHeader">
    <div class="openWinTip">
    @SERVICE.selectUdtFile@<br /><b class="orangeTxt">@SERVICE.fileSize8@</b>
    </div>
    <div class="rightCirIco pageCirIco"></div>
</div>

<div id="fu" class="edgeTB10LR20 pT0">
	<div id="upload">
    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
        <tbody>
            <tr>
                <td class="rightBlueTxt" width="150">
                    <label >@SERVICE.udtType@:</label>
                </td>
                <td colspan=2>
					<span id="updateType" style="width:280px; height:21px; "></span>
                </td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt"  width="150">
                    <label >@SERVICE.setHaw@:</label>
                </td>
                <td colspan=2>
					<select id="hardwareVersion" style="width:280px; height:21px; "></select>
                </td>
            </tr>
            <tr>
            	<td class="rightBlueTxt"  width="150">
            		<label >@SERVICE.setOption@:</label>
            	</td>
            	<td colspan=2>
					<select id="optionCTC" style="width:280px; height:21px; ">
		 			 <!-- <option value="0">none</option>
		 			 <option value="1">active</option> -->
		 			 <option value="2" selected>active&commit</option>
		 			</select>
		 			<select id="optionTK" style="width:280px; height:21px;display:none ">
			 			 <option value="0" selected>none</option>
			 			 <option value="3">reset</option>
		 			</select>
            	</td>
            </tr>
            <tr class="darkZebraTr">
            	<td class="rightBlueTxt"  width="150">
            		<label >@SERVICE.selectFile@:</label>
            	</td>
            	<td >
	 				<input id="fileName" type="text" style="width:280px;"  
	 					 />
	 			</td>
	 			<td>
		            <!-- <a id="chooseFile" href="javascript:;" class="normalBtnBig" >
		                <span id="flashContainer">
		            		@COMMON.browse@
		        		</span>
		            </a> -->
		            <div class="btns">
					    <div id="picker">@COMMON.browse@...</div>
					</div>
	 			</td>
            </tr>
        </tbody>
    </table>
    <input type=file id="selectFilePath" style="position:absolute;filter:alpha(opacity=0);width:0px;height:1px;background-color : red;position:relative;top:-27px;left:180px;z-index:-1000"  />
    <div id="file" style="position: absolute; z-index: 1;left:319;top:61px">
</div>
   </div>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	    	<li>
	    		<a href="javascript:;" class="normalBtnBig" onclick="startUpload()"><span><i class="miniIcoSaveOK"></i>@COMMON.confirm@</span></a>
	    	</li>
	        <!-- <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="upload()">
	                <span>@COMMON.confirm@ </span>
	            </a>
	        </li> -->
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
	                <span><i class="miniIcoClose"></i>@COMMON.close@ </span>
	            </a>
	        </li>
	    </ol>
	</div>
</div>	
</body>
</Zeta:HTML>