var cm,sm, columnModels, store, grid, tbar, baseCm, baseColumnModels, baseSm, baseStore, baseGrid, 
	uploader = null,
	addTilteTxt = '@CM.addUpgradeFileConfig@',   //添加升级配置文件;
	editTitleTxt = '@CM.editUpgradeFileConfig@', //编辑升级配置文件;
    MAX_ITEMS = 10,                              //最多可支持 10 条配置文件;
	flash = null,                                //上传控件;
    tpl,                                         //添加的模板页面;
    regionTree,                                  //地域下拉树;
    firstShowNextPage = false;                   //第一次显示下一步的那个页面;

Ext.onReady(function(){
    columnModels = [
        {header: "<div class=txtCenter>ModelNum</div>", width:100, dataIndex: "modulNum", 
        	align: "left", sortable: true},
        {header: "<div class=txtCenter>@CMC.text.softwarevision@</div>", width:100, dataIndex: "softVersion", 
        	align: "left", sortable: true},
        {header: "<div class=txtCenter>@CM.softFileName@</div>", width:100, dataIndex: "versionFileName", 
        	align: "left", sortable: true},
        {header: "@COMMON.manu@", width:100, dataIndex: "configId", 
        	align: "center", sortable: false, renderer: renderManu}
    ];
    cm = new Ext.grid.ColumnModel({
        defaults: {
            menuDisabled : true
        },
        columns: columnModels
    });
    store = new Ext.data.JsonStore({
        url: "/cmupgrade/loadAutoUpgradeConfig.tv",
        root: "data",
        totalProperty: "rowCount",
        remoteSort: true,
        fields: ["modulNum","softVersion","versionFileName","configId"]
    });
    store.load();
    
    tbar = new Ext.Toolbar({
        items : [{
            text    : "@COMMON.add@",
            iconCls : "bmenu_new",
            handler : addUpgradeFile
        },'->',{
        	cls   : 'lightGrayTxt',
        	id    : 'maxLengthTip',
        	html  : String.format('@tip.maxSize@', MAX_ITEMS),
        	xtype : 'component'
        },{
        	xtype : 'spacer',
        	width : 6 
        }]
    });
    grid = new Ext.grid.GridPanel({
        cm: cm,
        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        cls: "normalTable",
        tbar: tbar,
        store: store,
        height: 380,
        region: "center",
        renderTo: 'putGrid1',
        stripeRows:true,
        bodyCssClass: "normalTable",
        viewConfig:{ forceFit: true }
    });
});//end document.ready;
function renderManu(v, m, r){
	var configId = r.data.configId;
	var str = String.format('<a href="javascript:;" onclick="editUpgradeConfig({0})">@COMMON.edit@</a> / ' + 
				'<a href="javascript:;" onclick="deleteUpgradeConfig({0})">@COMMON.delete@</a>', configId);
	return str;
}
//创建弹出层html模板;
function createOpenLayerTemp(){
	window.tpl = new Ext.XTemplate(
		'<div class="openLayerOuter" id="openLayerOuter">',
			'<div class="openLayerMainDiv">',
				'<div class="zebraTableCaption">',
		 			'<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt" id="openLayerTitle">{title}</label></span></div>',
						'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
							'<tbody>',
								'<tr>',
									'<td class="rightBlueTxt" width="160">',
										'ModelNum:',
									'</td>',
									'<td>',
										'<select id="ModulNumSel" class="normalSel w220" ','<tpl if="edit == true">','disabled="disabled"','</tpl>','>',
											'<tpl for="ModulNumArr">',
												'<option value="{.}">{.}</option>',
											'</tpl>',
										'</select>',
									'</td>',
								'</tr>',
								'<tr class="darkZebraTr">',
									'<td class="rightBlueTxt" width="160">',
										'@CCMTS.softVersion@:',
									'</td>',
									'<td>',
										'<input id="softVersionInput" toolTip="@COMMON.anotherName@" type="text" class="normalInput w220" value="{softVersion}" />',
									'</td>',
								'</tr>',
								/*'<tr>',
									'<td class="rightBlueTxt" width="160">',
										'@CM.upgradeFile@:',
									'</td>',
									'<td>',
										'<input id="fileInput" class="normalInput floatLeft" style="width:172px;" type="text" disabled="disabled" value="{versionFileName}" placeholder="@SERVICE.fileSize@" />',
										'<a id="chooseFile" href="javascript:;" class="nearInputBtn"><span>@COMMON.upload@</span></a>',
									'</td>',
								'</tr>',*/
								'<tr>',
									'<td class="rightBlueTxt" width="160">',
										'@CM.upgradeFile@:',
									'</td>',
									'<td>',
										'<input id="newFileInput" class="normalInput floatLeft" style="width:172px;" type="text" disabled="disabled" value="{versionFileName}" placeholder="@SERVICE.fileSize@" />',
										'<div id="picker" style="line-height: 1.2em;">@COMMON.upload@</div>',
									'</td>',
								'</tr>',
							'</tbody>',
						'</table>',
						'<ol class="upChannelListOl pB0 pT10 noWidthCenter">',
							'<li><a href="javascript:;" class="normalBtnBig" onclick="newAddOrEditUpgradeFileConfig()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>',
							/*'<li><a href="javascript:;" class="normalBtnBig" onclick="addOrEditUpgradeFileConfig()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>',*/
						    '<li><a href="javascript:;" class="normalBtnBig" onclick="cancelInner()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
						'</ol>',
						'<div class="clearBoth"></div>',
					 '</div>',
				'</div>',
			'</div>',
		'</div>',
		'<div class="openLayerBg"></div>'
	);
}
//上一页;
function prevPage(){
    $("#w1600").animate({left:0},"fast");
}
//下一页(如果是第一次点击下一页，则需要创建设备类型下拉框 & 地域下拉框 & 选择设备的gridPanel);
function nextPage(){
	if(store.getTotalCount() <= 0){
		window.parent.showMessageDlg('@COMMON.tip@', '@tip.addNone@');
		return;
	}
    $("#w1600").animate({left:-800},"fast");
    if(!firstShowNextPage){
    	firstShowNextPage = true;
    	buildDeviceTypeSel();
    	buildRegionTree();
    	buildGrid2();
    }	
}
//取消;
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
//取消添加升级配置的弹出框;
function cancelInner(){
	$("#openLayerOuter, .openLayerBg").fadeOut();
}
//创建地域
function buildRegionTree(){
	window.regionTree = $('#region_tree').dropdowntree({
		multi: true,
		width: 200
	}).data('nm3k.dropdowntree');
	regionTree.checkAllNodes();
}
//创建上传控件;
function buildUpload(){
	if(flash !== null){return;}
	flash = new TopvisionUpload("chooseFile");
    flash.onSelect = function(obj){
    	fileSize = obj.size;
        var chooseFileName = obj.name;
        var length = chooseFileName.length;
        //单个文件不能大于20M
        var maxSize = 20*1024*1024;
        if(fileSize > maxSize){
        	top.showMessageDlg('@COMMON.tip@', '@SERVICE.fileSize@');
        	return;
        }
        $("#fileInput").val(obj.name);
    }
    flash.onComplete =function(result){
        Ext.getBody().unmask();
        if(result == "failure"){
            window.parent.showMessageDlg('@COMMON.tip@', '@network/NAMEIMPORT.fileError@');
            return;
        }else if(result == "success"){
        	top.afterSaveOrDelete({
        		title : '@COMMON.tip@',
        		html  : '@CM.addUpgradeFileConfig@@COMMON.success@'
        	})
        	addUpgradeFileSuccess();
            return;
        }else if(result == "fileSizeExceed"){
        	top.showMessageDlg('@COMMON.tip@', '@SERVICE.allfileSize@');
        	return;
        }else{
        	window.parent.showMessageDlg('@COMMON.tip@', '@SERVICE.uploadFail@');
        	return;
        }
    }
}
//需要将上传的action地址传进来，添加和编辑是不同的;
function buildWebUploader(serverUrl){
	if(window.uploader != null){
		window.uploader.destroy();
		window.uploader = null;
	}
	uploader = WebUploader.create({
	    swf: '../js/webupload/Uploader.swf',
	    server: serverUrl,
	    pick: {
	    	id: '#picker',
	    	multiple : false
	    },
	    /*accept: {
	    	extensions: 'xls,xlsx'
	    },*/
	    resize: false,
	    duplicate: true,
	    fileNumLimit: 1,
	    fileSizeLimit: 20*1024*1024,
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
			$('#newFileInput').val(file.name);
			window.fileName = file.name;
		}
		//console.log('fileQueued');
	});
	//上传过程中触发，携带上传进度。
	uploader.on('uploadProgress', function( file, percentage ) {
		//console.log('uploadProgress:' + percentage)
	});
	//当文件上传成功时触发。
	uploader.on( 'uploadSuccess', function(file, ret) {
		clearMask();
		//拿到返回值，再去做其它事情;
		if (!ret)
			return;
		//result = Ext.decode(result);
		if (ret.result) {
			if(ret.result == "success"){
				addUpgradeFileSuccess();
			}else if(ret.result == "failure"){
				// 文件解析失败
				top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
			}
		}
	});
	//当文件上传出错时触发
	uploader.on( 'uploadError', function( file ) {
		//console.log('uploadError上传出错');
		//clearMask();
		top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
	});
	//不管成功或者失败，文件上传完成时触发。
	//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
	uploader.on( 'uploadComplete', function( file ) {
		//console.log('uploadComplete')
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
		clearMask();
	});
}


//获取ModuleNum内的数据,生成添加升级配置文件里面的ModulNum内的值;
function getModuleNumData(callback){
	$.ajax({
		url: '/cmupgrade/showAddAutoUpgradeConfig.tv',
		type: 'POST',
		async: false,
		cache: false,
		dataType: 'json', 
		success: function(json) {
			callback(json);
		},
		error: function() {
			window.top.showErrorDlg();
		}
	});
}
//添加升级配置文件（点击添加按钮，创建或者弹出层）;
function addUpgradeFile(){
	//最多可支持 10 条配置文件,超过的时候，点击添加按钮，提示信息闪烁;
	if(store.getTotalCount() >= MAX_ITEMS){
		$("#maxLengthTip").fadeOut('fast',function(){
			$(this).fadeIn('fast');
		});
		return;
	}
	//如果没有创建弹出层，则创建一个;
	if($("#openLayerOuter").length < 1){
		createOpenLayerTemp();
		getModuleNumData(openAddLayerFirst);
	}else{
		$("#openLayerOuter, .openLayerBg").show();
		$("#ModulNumSel").removeAttr("disabled");
		$("#softVersionInput, #fileInput, #newFileInput").val('');
		$("#openLayerTitle").text(window.addTilteTxt);
	}
}
//点击弹出层的"保存"按钮（循环数据，如果没有，则添加，如果有，提示并编辑）
function newAddOrEditUpgradeFileConfig(){
	var modulNum = $("#ModulNumSel").val(),
	$softVersionInput = $("#softVersionInput"),
	$fileInput = $("#newFileInput"),
	fileNameReg = /^[a-zA-Z0-9.()_-]{1,31}$/,
    flag = false,
    modfyId,
    oldValue;
	//验证modulNum,如果为空，则说明没有加载升级文集;
	if(modulNum == ""){
		top.showMessageDlg('@COMMON.tip@', '@tip.withoutModulNum@', 'ext-mb-question', function(){});
		return;
	}
	//验证软件版本;
	if( !V.isAnotherName($softVersionInput.val()) ){
		$softVersionInput.focus();
		return;
	}
	//验证上传文件的文件名;
	if( !fileNameReg.test($fileInput.val()) ){
		top.showMessageDlg('@COMMON.tip@','@SERVICE.fileNameErr@@SERVICE.destFileTip@','ext-mb-question', function(){});
		return;
	}
	//判断是否使用过这个ModulNum,如果使用过，则就是编辑;
	store.each(function(v, i, num){
		if(v.data && v.data.modulNum && v.data.modulNum == modulNum){
			flag = true;
			modfyId = v.data.configId;
			//TODO 如何break each还要研究下;
		}
	});
	if(flag){ //已经存在这个modulNum,编辑;
		/*
		 * 点击编辑，就一定是编辑，上传控件sever的url绝对不会有问题(点击按钮的时候，就设置好了是添加还是编辑的sever);
		 * 但是点击添加，这个ModulNum又使用过，就变成编辑了，上传控件sever的url又要变成编辑了;
		 * 由于添加和编辑都用了同一个弹窗，导致这里复杂了点;
		 */
		uploader.options.server ='/cmupgrade/modifyAutoUpgradeConfig.tv'; 
		newSaveUpgradeFileConfig('modifyAutoUpgradeConfig', 'edit', modfyId);
	}else{ //不存在这个modulNum,添加;
		uploader.options.server ='/cmupgrade/addAutoUpgradeConfig.tv';
		newSaveUpgradeFileConfig('addAutoUpgradeConfig', 'add');
	}
}
//添加升级配置文件,点击保存(获取输入框中参数，执行html5上传);
function newSaveUpgradeFileConfig(actionSrc, type, modfyId){
	var modulNum = $("#ModulNumSel").val(),
	    softVersion = $("#softVersionInput").val(),
	    fileName = $("#newFileInput").val();
	
	if(fileName){
		showBodyMask('@network/NAMEIMPORT.readingFile@');
		
		uploader.options.formData = {
			modulNum: modulNum,
			softVersion: softVersion,
			versionFileName: fileName
		}
		switch(type){
		case 'add':
			uploader.upload();
			break;
		case 'edit':
			//fileUpdateFlag  1表示编辑时有文件修改  0表示没有修改
			var aFiles = uploader.getFiles();
			if(aFiles.length == 0){ //上传控件中，一个文件都没有（比如我点了编辑，只改名字，没有上传新的文件）;
				
				var url = String.format("/cmupgrade/{3}.tv?modulNum={0}&softVersion={1}&versionFileName={2}", 
						modulNum, softVersion, fileName, actionSrc);
				eidteWithoutUpLoad(String.format(url + '&configId={0}&fileUpdateFlag={1}', modfyId, 0));
				
			}else{ //length肯定是1;
				uploader.options.formData = {
					modulNum: modulNum,
					softVersion: softVersion,
					versionFileName: fileName,
					configId: modfyId,
					fileUpdateFlag: 1
				}
				uploader.upload();
			}
			break;
		}
	}
}

//点击弹出层的"保存"按钮（循环数据，如果没有，则添加，如果有，提示并编辑）
function addOrEditUpgradeFileConfig(){
	var modulNum = $("#ModulNumSel").val(),
		$softVersionInput = $("#softVersionInput"),
		$fileInput = $("#fileInput"),
		fileNameReg = /^[a-zA-Z0-9.()_-]{1,31}$/,
	    flag = false,
	    modfyId,
	    oldValue;
	//验证modulNum,如果为空，则说明没有加载升级文集;
	if(modulNum == ""){
		top.showMessageDlg('@COMMON.tip@', '@tip.withoutModulNum@', 'ext-mb-question', function(){});
		return;
	}
	//验证软件版本;
	if( !V.isAnotherName($softVersionInput.val()) ){
		$softVersionInput.focus();
		return;
	}
	//验证上传文件的文件名;
	if( !fileNameReg.test($fileInput.val()) ){
		top.showMessageDlg('@COMMON.tip@','@SERVICE.fileNameErr@@SERVICE.destFileTip@','ext-mb-question', function(){});
		return;
	}
	//判断是否使用过这个ModulNum,如果使用过，则就是编辑;
	store.each(function(v, i, num){
		if(v.data && v.data.modulNum && v.data.modulNum == modulNum){
			flag = true;
			modfyId = v.data.configId;
			//TODO 如何break each还要研究下;
		}
	});
	if(flag){ //已经存在这个modulNum,编辑;
		saveUpgradeFileConfig('modifyAutoUpgradeConfig', 'edit', modfyId);
	}else{ //不存在这个modulNum,添加;
		saveUpgradeFileConfig('addAutoUpgradeConfig', 'add');
	}
}
//添加升级配置文件,点击保存(获取输入框中参数，执行flash上传);
function saveUpgradeFileConfig(actionSrc, type, modfyId){
	var modulNum = $("#ModulNumSel").val(),
	    softVersion = $("#softVersionInput").val(),
	    fileName = $("#fileInput").val();
	
	if(fileName){
		Ext.getBody().mask("<img src='/images/refreshing2.gif' class='loadingmask'/> " + 
		    '@network/NAMEIMPORT.readingFile@');
		var url = String.format("/cmupgrade/{3}.tv?modulNum={0}&softVersion={1}&versionFileName={2}", 
			modulNum, softVersion, fileName, actionSrc);
		switch(type){
		case 'add':
			flash.upload(url);
			break;
		case 'edit':
			//fileUpdateFlag  1表示编辑时有文件修改  0表示没有修改
			try{
				flash.upload(String.format(url + '&configId={0}&fileUpdateFlag={1}', modfyId, 1));
			}catch(err){
				eidteWithoutUpLoad(String.format(url + '&configId={0}&fileUpdateFlag={1}', modfyId, 0));
			}
			break;
		}
	}
}
function eidteWithoutUpLoad(url){
	$.ajax({
		url: url,
		type: 'POST',
		cache: false,
		dataType: 'text', 
		success: function(json) {
			Ext.getBody().unmask();
			top.afterSaveOrDelete({
        		title : '@COMMON.tip@',
        		html  : '@CM.editUpgradeFileConfig@@COMMON.success@'
        	})
        	addUpgradeFileSuccess();
		},
		error: function() {
			window.top.showErrorDlg();
		}
	});
}
//添加升级配置文件，升级成功(清空输入框 & 关闭弹出层 & 重载数据);
function addUpgradeFileSuccess(){
	$("#softVersionInput, #fileInput").val('');
	cancelInner();
	store.reload();
}

//创建“添加升级配置文件”弹出层，并且第一打开;
function openAddLayerFirst(data){
	var obj = {
		edit : false,
		ModulNumArr : data,
		title : window.addTilteTxt,
		softVersion : '',
		versionFileName : ''
	}
	window.tpl.append(document.body, obj);
	//buildUpload();
	buildWebUploader('/cmupgrade/addAutoUpgradeConfig.tv');
}
function openEditLayerFirst(data){
	var r = grid.getSelectionModel().getSelected();
	var obj = {
		edit : true,
		ModulNumArr : data,
		title : window.editTitleTxt,
		softVersion : r.data.softVersion,
		versionFileName : r.data.versionFileName
	}
	window.tpl.append(document.body, obj);
	$("#ModulNumSel").val(r.data.modulNum);
	//buildUpload();
	buildWebUploader('/cmupgrade/modifyAutoUpgradeConfig.tv');
}
//编辑升级配置文件（点击编辑按钮，创建或者弹出层）;;
function editUpgradeConfig(id){
	try{
		uploader.reset();
	}catch(err){}
	//如果没有创建弹出层，则创建一个;
	if($("#openLayerOuter").length < 1){
		createOpenLayerTemp();
		getModuleNumData(openEditLayerFirst);
	}else{
		var r = grid.getSelectionModel().getSelected();
		$("#openLayerOuter, .openLayerBg").show();
		$("#ModulNumSel").val(r.data.modulNum).attr({disabled: 'disabled'});
		$("#softVersionInput").val(r.data.softVersion);
		$("#newFileInput").val(r.data.versionFileName);
		$("#openLayerTitle").text(window.editTitleTxt);
	}
}
//删除升级配置文件;
function deleteUpgradeConfig(id){
	window.top.showConfirmDlg("@COMMON.tip@", "@CMC.tip.sureToDeleteConfig@", function(type){
		if (type == 'no') {return;}
		top.showWaitingDlg("@COMMON.wait@", "@COMMON.deleting@",'waitingMsg','ext-mb-waiting');
		$.ajax({
			url: String.format('/cmupgrade/deleteAutoUpgradeConfig.tv?configId={0}', id),
			type: 'POST',
			cache: false,
			dataType: 'json', 
			success: function(json) {
				top.closeWaitingDlg();
				store.reload();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@resources/COMMON.deleteSuccess@</b>'
	   		   });
			},
			error: function() {
				window.top.showErrorDlg();
			}
		});
	});
}
//创建"设备类型"下拉框;
function buildDeviceTypeSel(){
	// 获取设备类型
	var deviceType = EntityType.getEntityWithIpType();
	var options = '<select id="deviceTypeSel" class="w200 normalSel">';
	options += '<option value="">@COMMON.pleaseSelect@</option>'
	$.ajax({
		url : '/cmupgrade/loadEntityType.tv',
		type : 'POST',
		dateType : 'json',
		success : function(responseObj) {
			for(i in responseObj){
				options += String.format('<option value="{0}">{1}</option>', responseObj[i], i)
			}
			options += '</select>';
			$("#putDeviceType").html(options);
		},
		error : function(entityTypes) {
		},
		async : false,
		cache : false
	});
}
//创建标题为"请选择设备"的gridPanel;
function buildGrid2(){
	baseSm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledBtn(baseGrid.getSelectionModel().getCount());
	        },
	        rowdeselect : function(sm,rowIndex,record){
	        	disabledBtn(baseGrid.getSelectionModel().getCount());
	        }
	    }
	});
    baseColumnModels = [
        baseSm,
        {header: "<div class=txtCenter>@COMMON.alias@</div>", width:100, dataIndex: "name", 
        	align: "left", sortable: true},
        {header: "<div class=txtCenter>@CCMTS.uplinkdevice@</div>", width:100, dataIndex: "ip", 
        	align: "left", sortable: true},
        {header: "MAC", width:100, dataIndex: "mac", align: "center", sortable: true},
        {header: "<div class=txtCenter>@COMMON.name@</div>", width:100, dataIndex: "sysName", 
        	align: "left", sortable: true},
        {header: "@COMMON.status@", width:50, dataIndex: "status", 
        	align: "center", sortable: true, renderer: renderStatus},
        {header: "<div class=txtCenter>@CMC.text.softwarevision@</div>", width:100, dataIndex: "softversion", 
        	align: "left", sortable: false}
    ];
    baseCm = new Ext.grid.ColumnModel({
        defaults : {
            menuDisabled : true
        },
        columns: baseColumnModels
    });
     baseStore = new Ext.data.JsonStore({
        url: "/cmupgrade/loadCcmtsE.tv",
        root: "data",
        totalProperty: "rowCount",
        remoteSort: true,
        fields: ["name", "ip", "mac", "sysName", "status", "softversion", "entityId"]
    });
    baseStore.load(); 
    baseGrid = new Ext.grid.GridPanel({
    	title: "@COMMON.pleaseSelectDevice@",
        stripeRows:true,
        renderTo : "putGrid2",
        height: 320,
        cls:"normalTable",
        bodyCssClass: "normalTable",
        region: "center",
        store: baseStore,
        sm: baseSm,
        cm: baseCm,
        viewConfig:{ forceFit: true }
    });
}
function renderStatus(v, m, r){
	var bStatus = r.data.status;
	if(bStatus){
		return '<img nm3ktip="@COMMON.online@" alt="@COMMON.online@" class="nm3kTip" ' + 
				'src="../images/correct.png" border="0" />';
	}else{
		return '<img nm3ktip="@COMMON.offline@" alt="@COMMON.offline@" class="nm3kTip" ' + 
				'src="../images/wrong.png" border="0" />';
	}
}
function disabledBtn(num){
	var $applyBtn = $("#applyBtn");
	if(num == 0){
		$applyBtn.attr({disabled: 'disabled'});
	}else{
		$applyBtn.removeAttr("disabled");
	}
}
//点击"查询"按钮
function queryClick(){
	var nameIpMac = $("#aliasNameIpMac").val(),
	    status = $("#onlineStatus").val(),
	    deviceTypeSel = $("#deviceTypeSel").val();
	    folderId = window.regionTree.getSelectedIds().join(",");
	    
	//验证别名/名称/IP/MAC
	if(!(nameIpMac == "" || V.isAnotherName(nameIpMac))){
		$("#aliasNameIpMac").focus();
		return;
	}
	baseStore.load({
		params: {
			search : nameIpMac,
			status : status,
			typeId : deviceTypeSel,
			folderId : folderId 
		},
		callback : function(){
			disabledBtn(baseGrid.getSelectionModel().getCount());
		}
	});
}
//点击"应用"按钮;
function applyClick(){
	var num = baseGrid.getSelectionModel().getCount();
	if(num <= 0){ //没有选择任何数据;
		top.showMessageDlg("@COMMON.tip@", "@tip.selectOne@");
		return;
	}
	var arr = baseGrid.getSelectionModel().getSelections(),
	    selectArr = [];
	
	$.each(arr, function(i, v){ //循环一下选中数据，剔除"离线"状态的数据;
		var status = v.data.status;
		if(status){
			selectArr.push(v.data.entityId);
		}
	});
	if(selectArr.length == 0){
		top.showMessageDlg("@COMMON.tip@", "@tip.allOffline@");
		return;
	}
	window.top.showConfirmDlg("@COMMON.tip@", "@tip.confirmApply@", function(type){
		if (type == 'yes') {
			var chooseEntityIds = selectArr.join(",");
			window.location.href = '/cmupgrade/showAutoUpgradeResult.tv?chooseEntityIds=' + chooseEntityIds;
		}
	});
}
function showBodyMask(str){
	Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
}
function clearMask(){
	Ext.getBody().unmask();
}
