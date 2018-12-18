<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
    library zeta
    library FileUpload
    library ext
    module network
    CSS js/webupload/webuploader
	IMPORT js/webupload/webuploader.min
</Zeta:Loader>
<style type="text/css">
.rightConner{ position: absolute; bottom:10px; right:10px;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script> 
<script type="text/javascript">
var folderId = <%= request.getParameter("folderId") %>;
var successList = null;
var errorList = null;
var notFindList = null;
var showData = new Array(); //显示数据
var spareData = new Array(); //剩余数据
var baseGrid = null;
var flag = 0;//标志目前列表中数据 0:导入成功  1:导入失败   2:未皮配
var flash;
$( DOC ).ready(function(){    
	newCreateUpload();
	
    /* flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
        fileSize = obj.size;
        var chooseFileName = obj.name;
        if(obj.size > 100*1024*1024){
            window.parent.showMessageDlg('@COMMON.tip@', String.format('@NAMEIMPORT.select100MFile@', obj.name));
            return;
        }else{
            $("#fileInput").val(chooseFileName);
        }
    };
    flash.onComplete =function(result){
    	result = Ext.util.JSON.decode(result);     
        Ext.getBody().unmask();
        if(result.message == "FileWrong"){
            window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
            return;
        }else if(result.message == "success"){
        	$("#step1").hide();
        	successList = result.successList
        	errorList = result.errorList
        	notFindList = result.notFindList
        	
        	$('#importSuccess').text(result.successNum);
        	$('#importError').text(result.errorNum);
        	$('#importNoMatch').text(result.notFindNum);

        	$("#step2").show();
        	showSuccessList();
        	
            return;
        }else{
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
        	return;
        }
    }; */
    
    
    var w = $(window).width() - 30;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [
		{header: '@NAMEIMPORT.rowNum@', width: parseInt(w/10), sortable: true, align: 'center', dataIndex: 'rowId'},
	    {header: '@COMMON.alias@', width: parseInt(w/4), sortable: true, align: 'center', dataIndex: 'name', renderer: renderName},
		{header: "IP",  width: parseInt(w/5), sortable: false, align: 'center', dataIndex: 'ip' , renderer: renderIp},
		{header: "MAC", width: parseInt(w/5), sortable: false, align: 'center', dataIndex: 'mac', renderer: renderMac},		
		{header: "@COMMON.contact@", width: parseInt(w/5), sortable: true, align: 'center', dataIndex: 'contact', renderer: renderContact},		
		{header: "@COMMON.location@", width: parseInt(w/5), sortable: false, align: 'center', dataIndex: 'location', renderer: renderLocation},		
		{header: "@COMMON.note@", width: parseInt(w/5), sortable: false, align: 'center', dataIndex: 'note', renderer: renderNote}		
	];

	baseStore = new Ext.data.JsonStore({
	    fields: ['id','rowId','ipStatus', 'nameStatus', 'macStatus', 'name','ip', 'mac', 'contact', 'location', 'note','contactStatus','locationStatus',
	             'noteStatus']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		height: 330,
		renderTo: "putGridPanel",
		border: true, 
		store: baseStore, 
		cm: baseCm,
		region:'center',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
});

function renderIp(value, p, record){
	if(record.data.ipStatus == 1){
		return '<span style="color:#f00">' + record.data.ip + '---IP' + '@NAMEIMPORT.addressError@' + '</span>';
	}else if(record.data.ipStatus == 2){
		return '<span style="color:#f00">' + record.data.ip + '---IP' + '@NAMEIMPORT.addressRepeat@' + '</span>';
	}else{
		return record.data.ip;
	}
	
}

function renderMac(value, p, record){
	if(record.data.macStatus == 1){
		return '<span style="color:#f00">' + record.data.mac + '---MAC' + '@NAMEIMPORT.addressError@' + '</span>';
	}else if(record.data.macStatus == 2){
		return '<span style="color:#f00">' + record.data.mac + '---MAC' + '@NAMEIMPORT.addressRepeat@' + '</span>';
	} else{
		return record.data.mac;
	}
	
}

function renderName(value, p, record){
	if(record.data.nameStatus == 1){
		return '<span style="color:#f00">' + record.data.name + '@NAMEIMPORT.aliasError@' + '</span>';
	}else if(record.data.nameStatus == 2){
		return '<span style="color:#f00">' + record.data.name + '@NAMEIMPORT.aliasRepeat@' + '</span>';
	} else{
		return record.data.name;
	}
	
}

function renderContact(value, p, record){
	if(record.data.contactStatus == 1){
		return '<span style="color:#f00">' + record.data.contact + '@NAMEIMPORT.contactError@' + '</span>';
	}else{
		return record.data.contact;
	}
	
}

function renderLocation(value, p, record){
	if(record.data.locationStatus == 1){
		return '<span style="color:#f00">' + record.data.location + '@NAMEIMPORT.locationError@' + '</span>';
	}else{
		return record.data.location;
	}
	
}
function renderNote(value, p, record){
	if(record.data.noteStatus == 1){
		return '<span style="color:#f00">' + record.data.note + '@NAMEIMPORT.noteError@' + '</span>';
	}else{
		return record.data.note;
	}
	
}
function showSuccessList(){
	if(successList.length < 25){
		showData = successList;
		spareData = new Array();
	}else{
		showData = successList.slice(0, 25)
		baseStore.loadData(showData);
		spareData = successList.slice(25);
	}
	baseStore.loadData(showData);
}
function okBtClick(){
	var overwrite = $("#overwirte").attr("checked");
	var fileName = $("#fileInput").val();
	if( fileName ){
	   Ext.getBody().mask("<img src='/images/blue_loading.gif' class='loadingmask'/> " + '@NAMEIMPORT.readingFile@');
	   flash.upload("/entity/import/importEntityName.tv?overwrite=" + overwrite);
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.chooseFile@');
	}
}

function cancelClick() {
    window.top.closeWindow('importName');
}

function exportEntity(){
	window.top.closeWindow('importName');
}

var addData = null;

function showErrorData(){
	flag = 1;
	if(errorList.length < 25){
		showData = errorList;
		spareData = new Array();
	}else{
		showData = errorList.slice(0, 25)
		baseStore.loadData(showData);
		spareData = errorList.slice(25);
	}
	baseStore.loadData(showData)
}

function showUnMatchData(){
	flag = 2
	if(notFindList.length < 25){
		showData = notFindList;
		spareData = new Array();
	}else{
		showData = notFindList.slice(0, 25)
		baseStore.loadData(showData);
		spareData = notFindList.slice(25);
	}
	baseStore.loadData(showData)
}

function showMore(){
		if(spareData.length > 25){
			addData = spareData.slice(0,25);
			showData = showData.concat(addData)
		    var length = showData.length - 25;
			
			var nowRecord = new Ext.data.Record({     
				rowId: addData[0].rowId,  
				name: addData[0].name,  
				ip: addData[0].ip, 
				mac: addData[0].mac,
				contact: addData[0].contact,
				location: addData[0].location,
				note: addData[0].note,
	        });
			
			$.each(addData, function(index, data){
				//data -> record
				var ipStatus = data.ipStatus;
				var macStatus = data.macStatus;
				var ip;
				var mac;
					if(ipStatus == 1){
						ip = '<span style="color:#f00">' + data.ip + '---IP' + '@NAMEIMPORT.addressError@' + '</span>';
					}else if(ipStatus == 2){
						ip = '<span style="color:#f00">' + data.ip + '---IP' + '@NAMEIMPORT.addressRepeat@' + '</span>';
					}else{
						ip = data.ip;
					}
					if(macStatus == 1){
						mac = '<span style="color:#f00">' + data.mac + '---MAC' + '@NAMEIMPORT.addressError@' + '</span>';
					}else if(macStatus == 2){
						mac = '<span style="color:#f00">' + data.mac + '---MAC' + '@NAMEIMPORT.addressRepeat@' + '</span>';
					} else{
						mac = data.mac;
					}
				var record = new Ext.data.Record({     
					rowId: data.rowId,  
					name: data.name,  
					ip: ip, 
					mac: mac,
                    contact: data.contact,
                    location: data.location,
                    note: data.note
		        });
				//record insert into grid
				baseStore.insert(length + index, record);
			})
			spareData = spareData.splice(25, spareData.length)
			baseGrid.getSelectionModel().selectRecords([nowRecord], false);
			return;
		}else if(spareData.length > 0){
			addData = spareData
			showData = showData.concat(spareData)
		    var length = showData.length - addData.length;
			
			var nowRecord = new Ext.data.Record({     
				rowId: addData[0].rowId,  
				name: addData[0].name,  
				ip: addData[0].ip, 
				mac: addData[0].mac,
                contact: addData[0].contact,
                location: addData[0].location,
                note: addData[0].note,
	        });
			
			$.each(addData, function(index, data){
				//data -> record
				var ipStatus = data.ipStatus;
				var macStatus = data.macStatus;
				var ip;
				var mac;
					if(ipStatus == 1){
						ip = '<span style="color:#f00">' + data.ip + '---IP' + '@NAMEIMPORT.addressError@' + '</span>';
					}else if(ipStatus == 2){
						ip = '<span style="color:#f00">' + data.ip + '---IP' + '@NAMEIMPORT.addressRepeat@' + '</span>';
					}else{
						ip = data.ip;
					}
					if(macStatus == 1){
						mac = '<span style="color:#f00">' + data.mac + '---MAC' + '@NAMEIMPORT.addressError@' + '</span>';
					}else if(macStatus == 2){
						mac = '<span style="color:#f00">' + data.mac + '---MAC' + '@NAMEIMPORT.addressRepeat@' + '</span>';
					} else{
						mac = data.mac;
					}
				var record = new Ext.data.Record({     
					rowId: data.rowId,  
					name: data.name,  
					ip: ip, 
					mac: mac,
		            contact: data.contact,
		            location: data.location,
		            note: data.note
		        });
				//record insert into grid
				baseStore.insert(length + index, record);
			})
			spareData = [];
			baseGrid.getSelectionModel().selectRecords([nowRecord], false);
			return;
		}else{
			window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.noMoreData@');
		}
	//grid.getSelectionModel().selectRecords([record], false);
}

function downloadTemplate(){
	window.location.href="entity/import/downLoadTemplate.tv";
}

function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
//导入;
function startUpload(){
	var overwrite = $("#overwirte").attr("checked");
	var fileName = $("#fileInput").val();
	if( fileName ){
	    showBodyMask('@UPLOAD.loading@');
	    uploader.options.formData = {
			   overwrite: overwrite
		}
	    uploader.upload();
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.chooseFile@');
	}
}
//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '/js/webupload/Uploader.swf',
	    server: '/entity/import/importEntityName.tv',
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
	    fileSizeLimit: 100*1024*1024,
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
			$('#fileInput').val(file.name);
		}
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
        Ext.getBody().unmask();
        if(result.message == "FileWrong"){
            window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
            return;
        }else if(result.message == "success"){
        	$("#step1").hide();
        	successList = result.successList
        	errorList = result.errorList
        	notFindList = result.notFindList
        	
        	$('#importSuccess').text(result.successNum);
        	$('#importError').text(result.errorNum);
        	$('#importNoMatch').text(result.notFindNum);

        	$("#step2").show();
        	showSuccessList();
        	
            return;
        }else{
        	window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.fileError@');
        	return;
        }
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
</script>
</head>
	<body class=openWinBody>
		<div id="step1">
			<div class=formtip id=tips style="display: none"></div>
			<div class="openWinHeader">
				<div class="openWinTip">@NETWORK.importDeviceInfo@</div>
				<div class="rightCirIco folderCirIco"></div>
			</div>

			<div class="edge10 pT5">
				<form onsubmit="return false;">
					<table class="mCenter zebraTableRows">
						<tr>
							<td width="260" class="rightBlueTxt"><label id="fileLabel"
								style='width: 60px;'>@NAMEIMPORT.selectFile@:</label></td>
							<td>
								<div id='file_div' style='margin-top: 5px;'>
									<input id="fileInput" class="normalInputDisabled floatLeft"
										type="text" disabled="disabled" />
									<div class="btns">
		                    			<div id="picker">@NAMEIMPORT.import@ Excel</div>
		                    		</div> 
										<%-- <a id="chooseFile"
										href="javascript:;" class="nearInputBtn"><span>@NAMEIMPORT.import@ Excel</span></a> --%>
								</div>
							</td>
							<td>
								
							</td>
						</tr>
						<tr class="darkZebraTr" align="center" id="deleteForm">
                            <td class="rightBlueTxt">@NAMEIMPORT.overwriteAllName@:</td>
                            <td align="left"><input type="checkbox" class="normalInput" id="overwirte" checked="checked"/></td>
                        </tr>
					</table>
				</form>
			</div>

			<Zeta:ButtonGroup>
				<li>
					<a onclick="startUpload()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoInport"></i>@resources/COMMON.importAction@</span></a>
				</li>
				<%-- <Zeta:Button onClick="okBtClick()" icon="miniIcoInport">@resources/COMMON.importAction@</Zeta:Button> --%>
				<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
			</Zeta:ButtonGroup>
			<div class="edge10 pT20">
				<div class="yellowTip">
					<b class="orangeTxt">@NAMEIMPORT.importRule@:</b>
					<p class="pT10">1:@NAMEIMPORT.Rule1@</p>
					<p>2:@NAMEIMPORT.Rule2@</p>
					<p>3:@NAMEIMPORT.Rule3@</p>
					<p>4:@NAMEIMPORT.Rule4@</p>
					<p>5:@NAMEIMPORT.Rule5@</p>
					<p>6:@NAMEIMPORT.Rule6@</p>
					<p>7:@NAMEIMPORT.Rule7@</p>
				</div>
			</div>
			<div class="rightConner">
				<a href="javascript:downloadTemplate();" class="normalBtn" style="margin-right: 2px;"><span><i class="miniIcoArrDown"></i>@NAMEEXPORT.templateDownload@</span></a>
			</div>
		</div>
		<!-- 导入结果 -->
		<div id="step2" style="display:none; position:absolute; top:0; left:0; background:#f7f7f7; width:100%; height:448px; overflow:hidden;">
			<div class="pL10 clearBoth" >
				<ol class="upChannelListOl pB0 pT10 ">
					<li><a href="javascript:showSuccessList();" class="normalBtnBig"><span><i class="miniIcoCorrect"></i>@NAMEIMPORT.importSuccess@</span></a></li>
					<li><a href="javascript:showErrorData()" class="normalBtnBig"><span><i class="miniIcoClose"></i>@NAMEIMPORT.importFail@</span></a></li>
					<li><a href="javascript:showUnMatchData()" class="normalBtnBig"><span><i class="miniIcoQuestion"></i>@NAMEIMPORT.notMatch@</span></a></li>
					
				</ol>
			</div> 
			<div style="position: absolute; top:10px; right:10px;">
				<b>@NAMEIMPORT.importSuccess@:</b><span id="importSuccess" style="color:#437003;">0</span> 
				<b class="pL5">@NAMEIMPORT.importFail@:</b><span id="importError"  style="color:#f00;">0</span> 
				<b class="pL5">@NAMEIMPORT.notMatch@:</b><span id="importNoMatch">0</span> 
			</div>
			
			<div id="putGridPanel" class="edge10 clearBoth"></div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB0 pT10 noWidthCenter">
					<li><a href="javascript:showMore();" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@NAMEIMPORT.loadMore@</span></a></li>
					<li><a href="javascript:cancelClick()" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
				</ol>
			</div>
		</div>
	</body>
</Zeta:HTML>