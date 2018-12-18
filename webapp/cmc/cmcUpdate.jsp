<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<HTML>
<HEAD>
<TITLE>Software Upgrading</TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.workbench.resources"
    var="workbench" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources"
    var="cmc" /> 
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/ux-all.css" />
<link rel="stylesheet" type="text/css" href="../css/samples.css" />
<link href="css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link rel="STYLESHEET" type="text/css"
    href="../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
    href="../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/FileUpload/TopvisionUpload.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="js/progressBarScript.js"></script>
<script type="text/javascript"
	src="../network/network-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<style type=text/css>
    #submit_btn {
    	width: 80px;height: 50px;background: url(../image/power.png) no-repeat center;
    	cursor: pointer;display: block;font-size: 0;line-height: 0;text-indent: -9999px;
    }
    .pbar {
		position: absolute;
		left: 15px;
		top: 156px;
		width: 270px;
		height: 22px
	}
	
	.percent {
		position: absolute;
		left: 150px;
		top: 159px;
		z-index: 1000000;
	}
</style>
<script type="text/javascript">
var fp = null ;
var cmcId = '<s:property value="cmcId"/>';
var fileSize = 0;
var chooseFileName = "";
var flash;
var tftpStatus ;
function upload(obj){
    window._fileName = Ext.getCmp('destFileName').getValue();
    var fileType = Ext.getCmp('fileType').getValue();
    if(!tftpStatus){
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.tftpServerIsClosed, null);
    }else{
        if (fp.getForm().isValid()) {
    	    Ext.getCmp('destFileName').regex = null;
    	    if($('.ui-progressbar-value')){$('.ui-progressbar-value').css('width', '0%')}
    	    if (!chooseFileName.length>0){
    		    window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.selectFileFirst, null); 
    		}else if(fileSize > 50 * 1024 * 1024 ) {
        	    window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.fileSizeBiggerThan50M, null);  
            }else if(fileSize == 0){
            	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.fileSizeIs0, null);  
            }else{
            	 $("#uploadBT").attr('disabled',true).text(I18N.CMC.button.uploading);	
                 flash.upload("/cmc/updateCmc.tv?cmcId="+cmcId+"&destFileName="+Ext.getCmp('destFileName').getValue() + "&fileType=" + fileType);
                 show(cmcId);
            }
        }
    }
}

Ext.onReady(function () {
	$('#progress').children('.pbar').children('.ui-progressbar-value').css('width', '100%');
	Ext.QuickTips.init();
	var filechoose = new Ext.form.ComboBox({  //升级类型
		id: 'fileType',
		renderTo : 'updateType' ,
        forceSelection: true,
        store: new Ext.data.SimpleStore({
            fields: ['value', 'text'],
            data : [
            	['4', 'config'],
            	['5', 'image']
            ]
        }),
        value: '5',
        width: 218,
        valueField: 'value',
        displayField: 'text',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        selectOnFocus:true, //用户不能自己输入,只能选择列表中有的记录  
        allowBlank: false
    })
    
	 fp = new Ext.FormPanel({ 
            renderTo: 'fu',   
            fileUpload: true, 
            height :  Ext.getBody().getHeight(),
            width: 450,
            frame:true,
            autoHeight: true,
            bodyStyle:'padding:0;',
            labelWidth: 110,    
            defaults: {      
                anchor: '95%',      
                allowBlank: false,    
                msgTarget: 'side'    
            },     
            items: [{
                    xtype: 'textfield',
                    fieldLabel:  I18N.CMC.label.saveName,
                    maxLength:31,
                    minLength:1,
                    regex: /^[a-zA-Z0-9.()_-]+$/,
                    regexText: I18N.CMC.tip.filenameRegTip,
                    emptyText: I18N.CMC.tip.filenameRegTip,
                    allowBlank:false,
                    id:"destFileName",
                    name:"destFileName",
                    listeners  : {   
                    }                 
                },{     
                    height:80,
                    contentEl:'upload'
            }]
    });
    flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        chooseFileName = obj.name;
        if(obj.size > 100000000){
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.CMC.tip.select100MFile, obj.name));
            return;
        }else{
            $("#fileName").val(chooseFileName);
    		Ext.getCmp('destFileName').setValue(chooseFileName); 
        }
    };
    flash.onComplete =function(result){
    	$("#destFileName").val(window._fileName);
    	window.tranparent = false;
		if(window.parent.progressTimer){
			window.parent.stopProgress();
		};
	    if(result=="updateSuccess"){
	    	$("#uploadBT").attr("disabled",false).text(I18N.CMC.button.updateDevice);
	    	$('#progress').children('.pbar').children('.ui-progressbar-value').css('width', '100%');
	    	$('#progress').children('.percent').html(I18N.CMC.tip.uploadComplete);
	   		//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.uploadSuccessResetToUpdate,null,function(){
		      //  window.parent.closeWindow('uploadfile');
	    	//})
	    	top.afterSaveOrDelete({
   				title: I18N.CMC.tip.tipMsg,
   				html: '<b class="orangeTxt">' + I18N.CMC.tip.uploadSuccessResetToUpdate + '</b>'
   			});
	    	cancelclick();
	       	return;
	    }
	    Ext.getCmp('destFileName').regex = /^[a-zA-Z0-9.()_-]+$/;
	    $("#uploadBT").attr("disabled",false).text(I18N.CMC.button.updateDevice);
		$('#progress').children('.percent').html(I18N.CMC.tip.uploadFail);
	    if(result=="entityBusy"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.deviceisInUpdating,'error');
	    } else if(result=="fileTransTimeout"){
	   	 	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.uploadFileError,'error');
	    } else if(result=="eraseError"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.eraseFileError,'error');
	    } else if(result == "snmpTimeout"){
	    	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.snmpRequestTimeout,'error');
	    }
    };
    tftpStatus = getTftpServerStatus();
});  

//获取TFTP服务器的启动状态
function getTftpServerStatus(){
	$.ajax({
		url: '/system/getTftpServerStatus.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(json) {
   			tftpStatus = json.result;
   		}, error: function(json) {
   			//获取TFTP服务器状态失败后，提示修改失败
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
	return status;
}
  
function cancelclick(){
    if (window.tranparent) {
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.notCloseWindows);
        return;
    }
	window.parent.closeWindow('uploadfile');
}
</script>
</head>
<body style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div id="fu"></div>
	<div id="upload">
		<table>
			<tr>
				<td width="80px"><fmt:message bundle='${cmc}' key='CMC.label.selectFile'/>:</td>
				<td><input type="text"
					style="width: 218px; margin-left: 50px;"
					id="fileName" value="<fmt:message bundle='${cmc}' key='CMC.tip.fileSize50MLimit'/>" disabled />
				</td>
				<td>
				<button type="button" id="chooseFile" class=BUTTON75 style="margin-top: 15px"
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'"><fmt:message bundle='${cmc}' key='CMC.button.scan'/></button> &nbsp;
						</td>
			</tr>
			<tr>
	 			<td width=80px><fmt:message bundle='${cmc}' key='CMC.label.uploadMode'/>:</td>
	 			<td>
	 				<span id=updateType style="margin-left: 50px; height:21px; "></span>
	 			</td>
	 		</tr>
		</table>
	</div>
	<div id="file"
		style="position: absolute; z-index: 1; left: 120px; top: 120px; width: 80px; height: 24px;">
	</div>
	<div style="padding-top: 20px; padding-left: 280px">
		<!-- ProgressBar -->
		<div id="progress">
			<div class="percent"></div>
	    	<div class="pbar"></div>
	    </div>
		<table>
			<tr>
				<td>
					<button type="button" class=BUTTON75 id="uploadBT"
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'" onclick="upload()"><fmt:message bundle='${cmc}' key='CMC.button.updateDevice'/></button>&nbsp;</td>
				<td>
					<button type="button" class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'"
						onclick="cancelclick()"><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></button></td>
			</tr>
		</table>
	</div>
	<div >
		<fmt:message bundle='${cmc}' key='CMC.label.uploadAttention'/>:<br>
			<fmt:message bundle='${cmc}' key='CMC.tip.imageAttention'/><br>
			<fmt:message bundle='${cmc}' key='CMC.tip.configAttention'/><br>
			<fmt:message bundle='${cmc}' key='CMC.tip.resetAttention'/><br>
	</div>
</body>
</html>