<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    LIBRARY FileUpload
    MODULE camera
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%; width:100%; overflow:hidden;}
#addDiv{ position:absolute; top:100px; left:150px; z-index:2; width:500px;}
#addDivBg{ width:100%; height:100%; overflow:hidden; position:absolute; top:0; left:0;opacity: 0.8; filter: alpha(opacity=85);background: #F7F7F7; z-index:1;}
</style>
<script type="text/javascript">
var uploader = null;
var uploadFileName = null; //上传的文件名;

var pageSize = <%= uc.getPageSize() %>;
var noteReg = /^[a-zA-Z\d\u4e00-\u9fa5-_\[\]()\/\.:\s\@]{1,120}$/;
Ext.onReady(function(){
	Ext.override(Ext.grid.GridView,{  
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected");  
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){
	            var r = this.getRow(i);
	            if(r){
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;
	            }
	        }

	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }
	    },
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");
	            var selected = 0;
	            var len = this.grid.store.getCount();
	            for(var i = 0; i < len; i++){
	                var r = this.getRow(i);
	                if(r){
	                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;
	                }
	            }
	            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();
	            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');
	            }  
	    }  
	});  
	var sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn}); 
	var cm = new Ext.grid.ColumnModel([
        sm,
		{header:"@CAMERA.type@", dataIndex:"type", width:60},
		{header:"<div class='txtCenter'>" + "MAC" + "</div>", dataIndex:"mac",width:80},
		{header:"<div class='txtCenter'>" + "@COMMON.note@" + "</div>", dataIndex:"note"},
		{header:"@COMMON.manu@", dataIndex:"manu", width:80,renderer:function(v,m,r){
			return String.format("<a href='javascript:;' onclick='deleteRow(\"{0}\");'>@COMMON.delete@</a> / <a href='javascript:;' onclick = 'edit(\"{0}\")'>@COMMON.edit@</a>",r.id);
		}}
	]);//end cm;
	
	WIN.store = new Ext.data.JsonStore({
		url: "/camera/loadCameraPhyList.tv",
		fields : ["mac","type","note"],
		root : 'data',
		totalProperty: 'rowCount',
		baseParams:{
			start : 0,
			limit : pageSize
		}
	});
	store.on("beforeload",function($this,options){
		var mac = $("#mac").val();
		var type = $("#type").val();
		var note = $("#note").val();
		if(!Validator.isFuzzyMacAddress(mac) && mac){
			return top.showMessageDlg("@COMMON.error@", "@COMMON.reqValidMac@",function(){
				$("#mac").focus();
			});
		}
		if(note){
			options.params.note = note;
		}
		if(type){
			options.params.type = type;
		}
		if(mac){
			options.params.mac = mac;
		}
	});
	store.load();
	
	WIN.grid = new Ext.grid.GridPanel({
		renderTo: "putTable",
		stripeRows:true,
		height: 340,
		bbar: new Ext.PagingToolbar({
			pageSize: pageSize,
			store: store,
			displayInfo: true,
			items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-'],
			emptyMsg: "@CAMERA.noRecord@",
			cls: 'extPagingBar'
		}),
		cls: 'normalTable',
		store: store,
		cm : cm, sm: sm,
		viewConfig:{
			forceFit: true
		}
	});
	
	/* WIN.browser =  new TopvisionUpload("browserBt", null, {
		onSelect : function( file ){
			this.selected = true;
			this.filename = file.name;
	        $("#filepath").val(this.filename);
			var fileSize = this.fileSize = file.size;
			if(fileSize > 15 * 1024 * 1024) {
	            top.showMessageDlg("@COMMON.tip@", "@SERVICE.fileSizeBig@");
	        }else if(fileSize == 0){
	        	top.showMessageDlg("@COMMON.tip@", "@SERVICE.fileSize0@");
	        }else{
		        R.importBt.setDisabled(false);
		        return;
	        }
			R.importBt.setDisabled(true);
		},
		onComplete : function(hasIllegalInfo){
			top.closeWaitingDlg();
			if("[false]" == hasIllegalInfo){
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: "@CAMERA.uploadOk@"
				});
			}else{
				top.showMessageDlg("@COMMON.tip@","@CAMERA.illegalInfoTip@");
			}
			store.reload();
		},
		onIOError : function(e){
			top.showMessageDlg("@COMMON.error@","@CAMERA.uploadEr@");
		}
	}); */
	
	newCreateUpload();
});//end document.ready;

//添加;
function openAddDivFn(){
	var $addDivBg = $("#addDivBg"),
	$addDiv = $("#addDiv");
	$addDivBg.css({display:"block"});
	$addDiv.css({display:"block"});
}
//取消
function closeAddLayer(){
	$("#addDivBg, #addDiv").fadeOut();
}

function deleteRow(rid){
	var $selectionModel = grid.getSelectionModel();
	if($selectionModel.getCount() > 1){
		window.parent.showConfirmDlg("@COMMON.tip@", String.format("@CAMERA.confirmselect@", $selectionModel.getCount() ), function(type) {
			if (type == 'no') return
			var $selections = $selectionModel.getSelections();
			var macList = [];
			for(var i=0; i<$selections.length; i++){
				var $mac = $selections[i].data.mac;
				macList.add( $mac );
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@");
	        $.ajax({
	            url: '/camera/deletePhyInfo.tv',type: 'POST',
	            data: "mac=" + macList.join("$"),
	            success: function() {
	            	top.closeWaitingDlg();
	            	top.nm3kRightClickTips({
	    				title: "@COMMON.tip@", html: "@CAMERA.delPlanOk@"
	    			});
	            	store.reload();
	            }, error: function(json) {
	                top.showMessageDlg("@COMMON.error@", "@CAMERA.delPlanEr@");
	            }, cache: false
	        });
		});
		return;
	}
	var data = store.getById(rid).data;
	window.parent.showConfirmDlg("@COMMON.tip@", String.format("@CAMERA.cfmDeletePhy@", data.mac), function(type) {
        if (type == 'no') return                      
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@");
        $.ajax({
            url: '/camera/deletePhyInfo.tv',type: 'POST',
            data: "mac=" + data.mac,
            success: function() {
            	top.closeWaitingDlg();
            	top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: "@CAMERA.delPhyOk@"
    			});
            	store.reload();
            }, error: function(json) {
                top.showMessageDlg("@COMMON.tip@", "@CAMERA.delPhyEr@");
            }, cache: false
        });
    });
}

function edit(rid) {
	var data = store.getById(rid).data;
	var url = String.format("/camera/showCameraPhyInfo.tv?mac={0}&type={1}&note={2}", data.mac, encodeURIComponent(data.type), encodeURIComponent(data.note));
	top.createDialog("editcameraInfo", '@CAMERA.editPhy@',  600, 300, url, null, true, true,function(){
		store.reload();
	});
}

//--------------------------------------
//			批量导入规划信息
//--------------------------------------
function batchimport(){
	if( browser.selected ) {
		top.showWaitingDlg("@COMMON.wait@","@COMMON.uploading@");
		browser.upload("/camera/uploadPhysicalInfo.tv",{
			fileName : browser.filename
		});
	}
}

function cancelclick(){
	top.closeWindow("cameraInfo");
}

function query(){
	store.reload({
		params: {
			start: 0,
			limit: pageSize
		}
	});
}

function addCameraPhy(){
	var mac = $("#macAdd").val();
	var type = $("#typeAdd").val();
	var note = $("#noteAdd").val();
	if(!type || !Validator.isAnotherName(type)){
		return $("#typeAdd").focus();
	}
	if(!mac || !V.isMac(mac)){
		return $("#macAdd").focus();
	}
	if(!note || $.trim(note).toLowerCase() === 'null' || !noteReg.test(note)){
		return $("#noteAdd").focus();
	}
	$.ajax({
		url:"/camera/addCameraPhy.tv",cache:false,
		data:{
			mac : mac,
			note : note,
			type : type
		},success:function(){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: "@CAMERA.addPhyOk@"
			});
			store.reload();
			closeAddLayer();
		},error:function(e){
			if( "DuplicateKeyException" == e.simpleName){
				top.showMessageDlg("@COMMON.error@", "@CAMERA.addMacExist@");
			}else{
				top.showMessageDlg("@COMMON.error@", "@CAMERA.addPhyEr@");
			}
		}
	});
}


function downloadTempl(){
	WIN.location.href = "/camera/downloadPhyTemplate.tv";
}

//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '/js/webupload/Uploader.swf',
	    server: '/camera/uploadPhysicalInfo.tv',
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    accept: {
	    	extensions: 'xls,xlsx'
	    },
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 15*1024*1024,
	    auto: false,
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
		if(file && file.name){
			$('#filepath').val(file.name);
			uploadFileName = file.name; 
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, result) {
		$('#filepath').val('');
		clearMask();
		//拿到返回值，再去做其它事情;
		if(result[0] === false){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: "@CAMERA.uploadOk@"
			});
		}else{
			top.showMessageDlg("@COMMON.tip@","@CAMERA.illegalInfoTip@");
		}
		store.reload();
	});
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
//导入;
function startUpload(){
	var fileName = $("#filepath").val();
	if( fileName ){
	    showBodyMask('@UPLOAD.loading@');
	    uploader.options.formData = {
	    	fileName: window.uploadFileName
		}
	    uploader.upload();
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@network/NAMEIMPORT.chooseFile@');
	}
}
</script>
</head>
<body class=openWindow>
	<div class="edge10">
		<table cellpadding="0" cellspacing="0" border="0" rules="none">
			<tr>
				<td class="rightBlueTxt">@CAMERA.type@@COMMON.maohao@</td>
				<td class="pR10">
					<input class="normalInput w140" type="text" id="type" />
				</td>
				<td class="rightBlueTxt">MAC@COMMON.maohao@</td>
				<td class="pR10">
					<input class="normalInput w140" type="text" id="mac"/>
				</td>
				<td class="rightBlueTxt">@COMMON.note@@COMMON.maohao@</td>
				<td class="pR10">
					<input class="normalInput w140" type="text" id="note"/>
				</td>
				<td>
					<a href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> 
				</td>
			</tr>
		</table>
		<div id="putTable" class="pT10"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		     	 <li class="pT5 pR20">
		     		<input id="filepath" class="normalInputDisabled floatLeft" type="text" disabled="disabled" />
		     		<div class="btns" style="float:left;">
              			<div id="picker">@COMMON.browse@</div>
              		</div>
					<!-- <a id="browserBt" href="javascript:;" class="nearInputBtn"><span>@COMMON.browse@</span></a> -->
					<a href="javascript:;" class="nearInputBtn" onclick="downloadTempl()"><span>@CAMERA.templateDownload@</span></a>
				 </li>
	         	 <li>
				     <a onclick="startUpload()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoInport"></i>@COMMON.import@</span></a>		         		
	         	 </li>
		         <%-- <Zeta:Button onclick="batchimport()" disabled="true" id="importBt" icon="miniIcoInport">@COMMON.import@</Zeta:Button> --%>
				 <Zeta:Button onclick="openAddDivFn()" icon="miniIcoAdd">@BUTTON.add@</Zeta:Button>
				 <Zeta:Button onclick="cancelclick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		     </ol>
		</div>
	</div>
	
	<div id="addDiv" class="displayNone">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@CAMERA.addPhy@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
							<tr>
				                <td class="rightBlueTxt" width="140">@CAMERA.type@<font class="redTxt">*</font>:</td>
								<td>
									<input type="text" class="normalInput w180" id="typeAdd" tooltip="@COMMON.anotherName@" maxlength="63"/>
								</td>
								<td></td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="140">MAC<font class="redTxt">*</font>:</td>
								<td>
									<input type="text" class="normalInput w180" id="macAdd" tooltip="@CAMERA.macTip@" maxlength="30"/>
								</td>
								<td></td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="140">@COMMON.note@<font class="redTxt">*</font>:</td>
								<td>
									<textarea rows="3" class="normalInput w180" id="noteAdd" style="height:50px;" tooltip="@CAMERA.noteTip@" maxlength="120"></textarea>
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="addCameraPhy()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
	</div>
	<div class="displayNone" id="addDivBg"></div>
</body>
</Zeta:HTML>