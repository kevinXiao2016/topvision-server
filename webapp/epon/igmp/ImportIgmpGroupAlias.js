var fileName;
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
//创建上传控件;
function newCreateUpload(){
	uploader = WebUploader.create({
	    swf: '/js/webupload/Uploader.swf',
	    server: '/epon/igmpconfig/importIgmpGroupAlias.tv',
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
		//上传框中传入文件名+后缀;
		if(file && file.name){
			$('#fileInput').val(file.name);
			window.fileName = file.name;
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, response) {
		clearMask();
		//拿到返回值，再去做其它事情;
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
//点击导入按钮;
function importClick(){
	if($('#fileInput').val() == ''){
		top.showMessageDlg('@COMMON.tip@', '@network/NAMEIMPORT.chooseFile@');
		return;
	}
	if(uploader != null){
		showBodyMask('@UPLOAD.loading@');
		uploader.options.formData = {
			fileName: window.fileName,
			entityId: window.entityId,
			jconnectionId: top.GLOBAL_SOCKET_CONNECT_ID
		}
		uploader.options.server = '/epon/igmpconfig/importIgmpGroupAlias.tv?fileName={0}&entityId={1}&jconnectionId={2}'.format(window.fileName, window.entityId, top.GLOBAL_SOCKET_CONNECT_ID);
		uploader.upload();
		$("#step1").hide();
		$("#step2").show();
	}
}


function showMore(){
	if(spareData.length > 25){
		addData = spareData.slice(0,25);
		showData = showData.concat(addData)
	    var length = showData.length - 25;
		
		var nowRecord = new Ext.data.Record(addData[0]);
		
		$.each(addData, function(index, data){
			//data -> record
			var record = new Ext.data.Record(data);
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
			id: data.groupId,
			name: data.groupName
        });
		
		$.each(addData, function(index, data){
			//data -> record
			var record = new Ext.data.Record(data);
			//record insert into grid
			baseStore.insert(length + index, record);
		})
		spareData = [];
		baseGrid.getSelectionModel().selectRecords([nowRecord], false);
		return;
	}else{
		window.parent.showMessageDlg('@COMMON.tip@', '@network/NAMEIMPORT.noMoreData@');
	}
}

var addData = null;

function okBtClick(){
	var fileName = $("#fileInput").val();
	if( fileName ){
	   flash.upload("/epon/igmpconfig/importIgmpGroupAlias.tv?fileName=" + fileName+"&entityId="+entityId+"&jconnectionId="+ top.GLOBAL_SOCKET_CONNECT_ID);
	   $("#step1").hide();
	   $("#step2").show();
	} else {
		window.parent.showMessageDlg('@COMMON.tip@', '@network/NAMEIMPORT.chooseFile@');
	}
}

function cancelClick() {
    window.top.closeWindow('importIgmpGroupAlias');
}

function downloadTemplate(){
	window.location.href="/epon/igmp/igmpAlias.xlsx";
}

function resultRender(v,m,r){
	if(v){
		return "<div class=greenTxt>@COMMON.success@</div>";
	}else{
		return "<div class=redTxt>@COMMON.fail@</div>";
	}
	
}

function renderResultTable(){
	var w = $(window).width() - 30;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [
		{header: '@igmp.vlanGroupId@',  dataIndex: 'groupId'},
		{header: '@igmp.vlanGroupAlias@',  dataIndex: 'groupName'},
		{header:"@COMMON.status@",dataIndex:'result',renderer:resultRender}
	];

	baseStore = new Ext.data.JsonStore({
	    fields: ['groupId',"groupName","result"]
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
}


function showErrorData(){
	baseStore.loadData(errorList)
}

function showSuccessList(){
	baseStore.loadData(successList);
}

var successCount = 0,
	faildCount  =0;
function registerCallback(){
	top.addCallback("@{IgmpConstants.IGMP_GROUP_CALLBACK_HANDLER}@",function(data){
		var $data = data.data;
			$data.result = data.success;
		if(data.success){
			successList.add($data);
			$("#sucFont").text(successList.length);
		}else{
			errorList.add($data);
			$("#failFont").text(errorList.length);
		}
		var record = new Ext.data.Record($data);
		baseStore.add([ record ]);
	});
}