<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    library FileUpload
    module network
    plugin DropdownTree
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<style type="text/css">
    .noConfigInfoTip{ display:none;}
    .btns{ line-height:1.3em; margin-right: 10px;}
    .webuploader-pick{ 
    	border-left:1px solid #B7B8CA;
    	border-radius: 2px;
    	padding-left: 27px;
    	background: #fefefd url(../../css/white/miniIcoInport.png) no-repeat 11px 3px;
    }
</style>
<script type="text/javascript">
var textArea = "";
var flash;
var hasSupportEpon = '<s:property value="hasSupportEpon"/>';
var regionTree;

var uploader = null;
var fileName = '';
 
var oltTip = '@sendConfig.noConfigInfoTip@'
           + '\n@sendConfig.configInfoTipOlt@'
           + '\n@sendConfig.configView@ (config)#' 
           + '\n@sendConfig.configInfoCommandList@' 
           + '\nip route-static 0.0.0.0 0 172.17.2.254 '  
           + '\n! ssh configuration: ' 
           + '\nboard assign 2 epub '  
	       + '\nboard admin 2 is '  
	       + '\n! alarm/event configuration:'
	       + '\nsnmp-trapserver 172.17.2.4 172.17.2.4 162 public'
	       + '\n......'
	       + '\n! end of configuration';
	       
var cmc1Tip = '@sendConfig.noConfigInfoTip@' 
           + '\n@sendConfig.configInfoTipCmcI@'
           + '\n@sendConfig.configView@ (config)#'
           + '\n@sendConfig.configInfoCommandList@'
           + '\ninterface bundle 1'
           + '\ncable helper-address 172.17.2.253'
           + '\nip address 172.17.2.146 255.255.255.0'
           + '\nexit'
           + '\ncable dhcp-l2-relay cm'
           + '\ncable dhcp-l2-relay host'
           + '\ncable dhcp-l2-relay mta'
           + '\ncable dhcp-l2-relay stb'
           + '\n!'
           + '\ncable modem remote-query interval 60'
           + '\n!'
           + '\ninterface ccmts 1/1/1'
           + '\nno cable upstream 1 shutdown'
           + '\nno cable upstream 2 shutdown'
           + '\nno cable upstream 3 shutdown'
           + '\nno cable upstream 4 shutdown'
           + '\nexit'
           + '\nsyslog'
           + '\nexit'
           + '\n!'
           + '\nend'
var cmc2Tip = '@sendConfig.noConfigInfoTip@' 
           + '\n@sendConfig.configInfoTipCmcII@'
           + '\n@sendConfig.configView@ (config-if-ccmts-2/3/1)#'
           + '\n@sendConfig.configInfoCommandList@'
           + '\nno cable upstream 1 shutdown'
           + '\nno cable upstream 2 shutdown'
           + '\nno cable upstream 3 shutdown'
           + '\nno cable upstream 4 shutdown'
           + '\n......'
           
var onuTip = '@sendConfig.noConfigInfoTip@'
           + '\n@sendConfig.configInfoTipOnu@'
           + '\n@sendConfig.configView@ (config-if-onu-2/3:3)#'
           + '\n@sendConfig.configInfoCommandList@'
           + '\nport mac-age-limit 3001'
           + '\nstatistics enable'
           + '\nstatistics uni 1 enable'
           + '\n......'
	
$(function(){
	
	autoHeight();
	$(window).resize(function(){
		//autoHeight();
		throttle(autoHeight,window);
	});//end resize;
	
	regionTree = $('#region_tree').dropdowntree({
		width: 200,
		multi:false,
		onCheck: function(){
			getConfig();
		}
	}).data('nm3k.dropdowntree');
	//默认选中默认地域
	regionTree.checkNodes([10]);
	
	//createUploader();
	
	flash = new TopvisionUpload("chooseFile");
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
        	window.parent.showMessageDlg('@network/COMMON.tip@', String.format("@NAMEIMPORT.fileError@--{0}", chooseFileName));
            return;
        }
        if(obj.size > 256*1024){
            window.parent.showMessageDlg('@network/COMMON.tip@', String.format('@network/sendConfig.select256kbFile@', obj.name));
            return;
        }else {
            $("#fileInput").val(chooseFileName);
        }
        importConfig()
    };
    flash.onComplete =function(result){
    	Ext.getBody().unmask();
    	result = Ext.util.JSON.decode(result);   
        if(!result.success){
            window.parent.showMessageDlg('@network/COMMON.tip@', '@network/NAMEIMPORT.fileError@');
            return;
        }
        getConfig();
    	/* $.each(result.configList, function(index, config){
    		configText = configText + config + "\n";
    	})	
    	$("#inputDivText").val(configText); */
    };
    getConfig();
    
    $("#saveConfig").hide()
    
    $('#inputDivText').attr("disabled",true);
})

function editConfig(){
	$("#editConfig").hide()
	$("#saveConfig").show()
	$('#inputDivText').attr("disabled",false);
	$('#inputDivText').focus();
}

function saveConfig(){
	$("#saveConfig").hide()
	$("#editConfig").show()
	$('#inputDivText').attr("disabled",true);
	saveCommonConfig();
}

function importConfig(){
	var fileName = $("#fileInput").val();
	var type = $("#entityType").val();
	var folderId = regionTree.getSelectedIds()[0];
	if( fileName ){
	   Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + '@network/NAMEIMPORT.readingFile@');
	   flash.upload("/entity/commonConfig/importCommonConfig.tv?type=" + type +"&folderId=" + folderId);
	} else {
		window.parent.showMessageDlg('@network/COMMON.tip@', '@network/NAMEIMPORT.chooseFile@');
	}
}

function getConfig(){
	var type = $("#entityType").val();
	var folderId = regionTree.getSelectedIds()[0];
	$.ajax({
		url: "/entity/commonConfig/readCommonConfig.tv",
		data:{type: type, folderId: folderId},
		type: "post",
		success: function(response){
			if(response != null && response == ''){
                var html;
                if (type == 10000) {
                    //html = $("#olt").html();
                    html = oltTip;
                } else if (type == 30000) {
                    html = cmc1Tip;
                } else if (type == 330000) {
                    html = cmc2Tip;
                } else if (type == 13000) {
                    html = onuTip;
                }
				//$("#inputDiv").html(html);
				$("#inputDivText").val(html);
			}else{
				//$("#inputDiv").html(response.replace(/\n/g,"<br/>"));
				$("#inputDivText").val(response);
			}
		}
	})
}

function saveCommonConfig(){
	var type = $("#entityType").val();
	var folderId = regionTree.getSelectedIds()[0];
	var textArea = $("#inputDivText").val().replace(/\n/g,"<br/>");
	$.ajax({
		url: "/entity/commonConfig/saveCommonConfig.tv",
		data:{type: type, textArea: textArea, folderId: folderId},
		type: "post",
		success: function(response){
			getConfig();
			top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: '<b class="orangeTxt">' + '@topo.saveSuccess@' + '</b>'
   			});
		}
	})
}

//自适应高度;
function autoHeight(){
	var h = $(window).height(),
	    $inputDivText = $("#inputDivText");
	
	var h2 = h - 120;
	if(h2 < 150){ h2 = 150;}
	//$inputDiv.height(h2);
	$inputDivText.height(h2);
	
};//end autoHeight;

//resize事件增加函数节流;
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}

function entityTypeChange(){
	getConfig();
	$("#saveConfig").hide()
	$("#editConfig").show()
	$('#inputDivText').attr("disabled",true);
}

function createUploader(){
	uploader = WebUploader.create({
	    swf: '../../js/webupload/Uploader.swf',
	    server: '/entity/commonConfig/importCommonConfig.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    accept: {
	    	extensions: 'cfg',
	    },
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 2,
	    fileSizeLimit: 256*1024,
	    auto: true,
	    timeout: 0  /* ,
	    runtimeOrder :'flash' */  
	});
	uploader.on( 'beforeFileQueued', function( file ) {
		//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
		//uploader.reset();
		console.log('beforeFileQueued')
		var type = $("#entityType").val();
		var folderId = regionTree.getSelectedIds()[0];
		uploader.options.formData = {
			type: type, 
			folderId: folderId
		}
		console.log(uploader)
	});
	//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
	uploader.on( 'fileQueued', function( file ) {
		//上传框中传入文件名+后缀;
		if(file && file.name){
			$('#fileInput').val(file.name);
			window.fileName = file.name;
		}
		console.log('fileQueued');
		
		showBodyMask('@network/NAMEIMPORT.readingFile@');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, result) {
		//拿到返回值，再去做其它事情;
		clearMask();
		console.log(result)
		if(!result.success){
            window.parent.showMessageDlg('@network/COMMON.tip@', '@UPLOAD.fail@');
            return;
        }
		getConfig();
	})
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
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
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}

</script>
<style type="text/css">
body,html{ width:100%; height:100%; overflow:hidden;}
.side{ width:200px; height:100%; overflow:auto; position:absolute; top:0; left:0;}
.middleLine{width:8px; overflow: hidden; height: 100%;  position:absolute; top:0; left:200px;}
.mainBody{ margin:0px 0px 0px 208px; height:100%; overflow:auto;}

</style>
</head>
<body class="whiteToBlack">
	<div class="leftMain-LR side" style="padding:0px;">
		<p class="pannelTit"  style="padding: 13px 10px 0px 10px;">@sendConfig.tip@</p>
		<div class="edge10">
				
				<p class="pT10">@sendConfig.fileRule1@</p>
				<br />
				<p>@sendConfig.fileRule2@</p>
				<br />
				<p>@sendConfig.fileRule3@</p>
				<br />
				<% if(uc.hasSupportModule("cmc")){%>
				<p>@sendConfig.fileRule4@</p>
				<%}%>
		</div>
	</div>
	<div class="middleLine line-LR" style="left:200px; cursor:default"></div>
	<div class="mainBody">
		<ul class="pannelTit leftFloatUl" style="padding:8px 0px 5px 0px; width:100%; overflow: visible;">
			<li class="pL10 pT5">
				@sendConfig.area@@COMMON.maohao@
			</li>
			<li id="region_tree">
			
			</li>
			<li class="pL10 pT5">
				@sendConfig.fileType@@COMMON.maohao@
			</li>
			<li>
				<select id="entityType" class="normalSel w120" onchange="entityTypeChange()">
				<% if(uc.hasSupportModule("cmc")){%>
					<option value="30000">CMC-I</option>
					<% if(uc.hasSupportModule("olt")){%>
						<option value="330000">CMC-II</option>
					<%}%>
				<%}%>
				<% if(uc.hasSupportModule("olt")){%>
                    <% if(uc.hasSupportModule("onu")){%>
                    <option value="13000">ONU</option>
                    <%}%>
                    <option value="10000">OLT</option>
				<%}%>
			</select>
			</li>
			
		</ul>		
		<!-- <div id="inputDiv" style="height:300px; border:1px solid #ccc; margin:10px; background:#fff; overflow:auto;"> -->
			<textarea id="inputDivText" rows="10" cols="10" style="width:96%; height:300px;  border:1px solid #ccc; background:#fff;  margin:10px;"></textarea>
		<!-- </div> -->
	    <div class="noWidthCenterOuter clearBoth">
	    	 <!-- <ol class="upChannelListOl pB0 p130 noWidthCenter">
	    	 	<li>
	    	 		<div class="btns">
                		<div id="picker">@sendConfig.modifyConfig@</div>
                	</div>
                </li>
	    	 </ol> -->
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		     	 <li><a onclick="" href="javascript:;" id="chooseFile" class="normalBtn"><span><i class="miniIcoInport"></i>@sendConfig.modifyConfig@</span></a></li>
		     </ol>
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		     	 <li><a onclick="editConfig()" href="javascript:;" id="editConfig" class="normalBtn"><span><i class="miniIcoEdit"></i>@sendConfig.editConfig@</span></a></li>
		     </ol>
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		     	 <li><a onclick="saveConfig()" href="javascript:;" id="saveConfig" class="normalBtn"><span><i class="miniIcoSave"></i>@MENU.saveConfig@</span></a></li>
		     </ol>
		</div>
	</div>
	<input id="fileInput" hidden/>
</body>

</Zeta:HTML>