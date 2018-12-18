var ponTransparentData = [],
	ponTransparentStore,
	ponTransparentGrid;

/**
 * 初始化VLAN透传表格
 */
function initVlanTransparent(){
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.transparent@ VLAN ID', dataIndex:'transparentId', align:'center', width: 0.4, sortable:true},
  	    {header:'@VLAN.transparentMode@', dataIndex:'transparentMode', align:'center', width: 0.4 ,sortable:true, renderer: transparentModeRender},
  	    {header: "@COMMON.manu@", dataIndex:'transparentId', width: 100, align:'center', fixed : true, renderer: transparentMenuRender}
  	]);
	ponTransparentStore = new Ext.data.SimpleStore({
		data: ponTransparentData,
		fields: ['transparentId', 'transparentMode']
	});
	ponTransparentGrid = new Ext.grid.GridPanel({
   		stripeRows: true,
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: ponTransparentStore,
   		cm: cm,
   		viewConfig:{
   			forceFit: true
   		},
   		height: $('#ponPassContainer').height() - 65,
	    renderTo: "ponVlanPassGridContainer"
   	});
	
	//加载PON VLAN透传数据
	loadPonTransparentData();
	
	//VLAN ID输入框绑定过滤事件
	$('#transparentInput').on('keyup', function(){
		var text = $(this).val();
		if(!text){
			//输入框为空值，加载所有数据
			addValidStyle($("#transparentInput"));
			ponTransparentStore.loadData(ponTransparentData);
			return
		}
		if (!isVlanIdStrValid(text)) {
			addInValidStyle($("#transparentInput"));
			return;
		}
		addValidStyle($("#transparentInput"));
		
		//将查询字符串整理为数组形式
		var arr = convertVlanStrToArray(text);
		//显示过滤后的数据
		var selectList = new Array();
		$.each(ponTransparentData, function(index, item){
			if(arr.indexOf(item[0]) > -1){
				selectList.push(item);
			}
		})
		ponTransparentStore.loadData(selectList);
	})
}

function transparentModeRender(v){
	var color = v ? 'red' : 'green';
	var str = v ? 'untag' : 'tag';
	return String.format("<font style='color:{0}'>{1}</font>", color, str);
}

function transparentMenuRender(transparentId){
	return String.format('<a class="yellowLink" href="javascript: deleteTransparent({0});">@COMMON.delete@</a>', transparentId);
}

/**
 * 刷新VLAN透传区域
 */
function refreshPonTransparent(){
	loadPonTransparentData();
	$("#transparentInput").val("");
}

function resizeTransparent(){
	ponTransparentGrid.setSize($('#ponPassContainer').width() - 10, $('#ponPassContainer').height() - 65);
}

/**
 * 加载VLAN透传数据
 * @param entityId
 * @param ponId
 */
function loadPonTransparentData(){
	$.ajax({
		url: '/epon/vlan/loadTransparentData.tv',
		cache:false, 
		dataType:'json',
		data: {entityId : entityId, ponId: ponId},
	    success: function(json) {
	    	if(!json || !json.length || json[0] == false){
	    		ponTransparentData = [];
	    	}else{
	    		ponTransparentData = json;
	    	}
	    	ponTransparentStore.loadData(ponTransparentData);
	    },
	    error: function(){
	   		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.contentError@');
	    }
	});
}

/**
 * 添加VLAN透传
 */
function addTransparentVlan(){
	//判断能不能添加
	if(!transparentValidate()){
		return;
	}
	
	//转换成数组形式
	var text = $("#transparentInput").val(), 
		idList = convertVlanStrToArray(text),
		modeList = new Array();
	if($("#transparentModeSel").val() == 1){
		//如果选择的是untag模式，将这批VLAN ID也标记为modeList
		modeList = convertVlanStrToArray(text);
	}
	
	window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.transparentAdding@', 'ext-mb-waiting');
	var url, params;
	if(ponTransparentData.length == 0){
		//当前还未配置透传VLAN，走添加逻辑
		url = '/epon/vlan/addTransparentRule.tv';
	}else{
		//当前已配置透传VLAN规则，走修改逻辑
		url = '/epon/vlan/modifyTransparentRule.tv';
		for(var a=0; a<ponTransparentData.length; a++){
			if(idList.indexOf(ponTransparentData[a][0]) == -1){
				idList.push(ponTransparentData[a][0]);
				if(ponTransparentData[a][1] == 1){
					modeList.push(ponTransparentData[a][0]);
				}
			}
		}
	}
	
	$.ajax({
		url: url,
		data: {
			entityId : entityId, 
			ponId: ponId, 
			transparentIdStr: idList.join(","), 
			transparentModeStr: modeList.join(",")
		},
		cache: false,
	    success: function() {
	    	top.closeWaitingDlg();
	    	refreshPonTransparent();
	    	refreshSCVID();
	    	top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '<b class="orangeTxt">@VLAN.transparentAddSuc@</b>'
	    	});
	    },
	    error: function(){
	    	window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.transparentAddFailed@");
	    }
	});
}

/**
 * 添加透传VLAN规则时的校验
 */
function transparentValidate(){
	var text = $("#transparentInput").val();
	
	//必须为合法的格式
	if(!isVlanIdStrValid(text)){
		$('#transparentInput').focus();
		return false;
	}
	
	//转换成数组形式
	var idList = convertVlanStrToArray(text);
	
	//判断是否有不存在的VLAN
	var noVlans = [];
	$.each(idList, function(i, vlanId){
		if(!isVlanExist(vlanId)){
			noVlans.push(vlanId);
		}
	})
	if(noVlans.length){
		top.showMessageDlg('@COMMON.tip@', String.format('@VLAN.vlanCountTip@', noVlans.join()));
		return false;
	}
	
	//进行SVID校验
	var existSvidMsg = [];
	$.each(idList, function(i, vlanId){
		var existObj = isSvidExist(vlanId);
		if(existObj.exist){
			existSvidMsg.push(existObj.msg);
		}
	})
	if(existSvidMsg.length){
		top.showMessageDlg('@COMMON.tip@', existSvidMsg.join('</br>'));
		return false;
	}
	
	//进行CVID校验
	var existCvidMsg = [];
	$.each(idList, function(i, vlanId){
		var existObj = isCvidExist(vlanId);
		if(existObj.exist){
			existCvidMsg.push(existObj.msg);
		}
	})
	if(existCvidMsg.length){
		top.showMessageDlg('@COMMON.tip@', existCvidMsg.join('</br>'));
		return false;
	}
	return true;
}

/**
 * 删除单条VLAN透传
 * @param vlanId
 */
function deleteTransparent(vlanId){
	window.parent.showConfirmDlg('@COMMON.tip@', '@VLAN.transparentDelConfirm@', function(type) {
		if (type == 'no') {
			return;
		}
		var url, params;
		if(ponTransparentData.length == 1){
			//当前只剩下一条透传VLAN，下发删除命令
			url = '/epon/vlan/delTransparentRule.tv';
			params = {
				entityId: entityId, 
				ponId: ponId
			};
		}else{
			//还存在多条，下发修改命令
			url = '/epon/vlan/modifyTransparentRule.tv';
			
			var newIdList = [], newModeList = [];
			$.each(ponTransparentData, function(i, curRule){
				if(curRule[0]!=vlanId){
					newIdList.push(curRule[0]);
					if(curRule[1] == 1){
						newModeList.push(curRule[0]);
					}
				}
			});
			
			params = {
			   	entityId : entityId, 
			   	ponId: ponId, 
			   	transparentIdStr: newIdList.join(","), 
			   	transparentModeStr: newModeList.join(",")
			};
		}
		
		window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.transparentDeleting@', 'ext-mb-waiting');
		$.ajax({
			url: url,
		   	data: params,
		   	cache: false,
		    success: function() {
		    	top.closeWaitingDlg();
		    	refreshPonTransparent();
		    	refreshSCVID();
		    	top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: '<b class="orangeTxt">@VLAN.transparentDelSuc@</b>'
		    	});
		    },
		    error: function(){
		   		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.transparentDelFailed@');
		    }
		});
	});
}

//批量删除透传Vlan
function batchDeleteTransClick(){
	var text = $("#transparentInput").val();
	if(!isVlanIdStrValid(text)){
		$("#transparentInput").focus();
		return;
	}
	if(!ponTransparentStore.getCount()){
		return window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.noTransparaentVlan@');
	}
	
	var deleteList = convertVlanStrToArray(text);
	window.parent.showConfirmDlg('@COMMON.tip@', '@VLAN.transparentDelConfirm@', function(type) {
		if (type == 'no') {
			return;
		}
		
		//整理出如果被删除后的透传VLAN
		var newIdList = [], newModeList = [];
		$.each(ponTransparentData, function(i, curRule){
			if(!deleteList.contains(curRule[0])){
				newIdList.push(curRule[0]);
				if(curRule[1] == 1){
					newModeList.push(curRule[0]);
				}
			}
		});
		if(newIdList.length){
			//修改
			url = '/epon/vlan/modifyTransparentRule.tv';
			params = {
			   	entityId : entityId, 
			   	ponId: ponId, 
			   	transparentIdStr: newIdList.join(","), 
			   	transparentModeStr: newModeList.join(",")
			};
		}else{
			//删除
			url = '/epon/vlan/delTransparentRule.tv';
			params = {
				entityId: entityId, 
				ponId: ponId
			};
		}
		
		$.ajax({
			url: url,
		   	data: params,
		   	cache: false,
		    success: function() {
		    	refreshPonTransparent();
		    	refreshSCVID();
		    	top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: '<b class="orangeTxt">@VLAN.transparentDelSuc@</b>'
		    	});
		    },
		    error: function(){
		   		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.transparentDelFailed@');
		    }
		});
	}); 
}