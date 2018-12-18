var batchTopo = {};
batchTopo.reloadStore = function(){
	batchTopo.store.reload();
}

function modifyNetSegment(){
	var url = "/batchautodiscovery/showModifyNetSegment.tv?action=modify";
	//获取选中行
	var sm = grid.getSelectionModel(),
    	record = sm.getSelected();
	url += '&id=' + record.data.id;
	window.parent.createDialog("netSegment", '@batchtopo.editingnetworksegment@',  800, 500, url, null, true, true);
}
function updateBatchTopoSwitch(){
    var record = grid.getSelectionModel().getSelected();
    var id = record.data.id;
    var autoDiscovery = (record.data.autoDiscovery == 0)?1:0;
    $.ajax({
        url:"/batchautodiscovery/modifyAutoDiscovery.tv",
        type:"post",
        data:{"id":id,"autoDiscovery":autoDiscovery},
        dataType:"text",
        success:function (message){
            batchTopo.store.load();
        },error: function(message) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '<b class="orangeTxt">@batchtopo.editingFailed@</b>'
            });
        }, cache: false     
    });
}
function deleteNetSegment(){
	//获取选中行
	var sm = grid.getSelectionModel(),
		selectedNetSegment = sm.getSelections();
	//取出所有待删除的id
	var deleteIds = [];
	for(var i=0; i<selectedNetSegment.length; i++){
		deleteIds.push(selectedNetSegment[i].data.id);
	}
	if(deleteIds.length===0){
		window.parent.showMessageDlg('@COMMON.tip@', '@batchtopo.selectnetworksegment@');
		return;
	}
	window.top.showConfirmDlg('@COMMON.tip@', '@batchtopo.suretodeletearea@', function(type) {
		if (type == 'no') {
	           return;
	    }
		var deletePromise = $.post("/batchautodiscovery/deleteNetSegment.tv", {
			ids: deleteIds.join(',')
		});
		deletePromise.done(function(){
			batchTopo.reloadStore();
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.deletedsuccessful@'
			});
		})
	});
}
function editSnmpTab(index){
	//获取对应的SNMP标签
	var snmpTab = batchTopoData.snmpConfigs[index];
	var url = "/batchautodiscovery/showModifySnmpTab.tv?action=modify";
	//获取选中行
	url += '&id=' + snmpTab.id;
	window.parent.createDialog("snmpTabDetail", '@batchtopo.editsnmplabel@',  600, 470, url, null, true, true);
}
function addSnmpTab(){
	//添加之前需要校验是否已经达到最大值
	var snmpPromise = $.get("/batchautodiscovery/loadSnmpTabs.tv");
	snmpPromise.done(function(response){
		batchTopoData.snmpConfigs = response.snmpConfigs;
		if(batchTopoData.snmpConfigs.length >=5){
			//表明别的客户端已经添加了snmp
			window.parent.showMessageDlg('@COMMON.tip@', '@batchtopo.snmpMax@');
			refreshSnmpTabs();
		}else{
			var url = "/network/batchAutoDiscovery/snmpTabDetail.jsp?action=add&r="+Math.random();
			window.parent.createDialog("snmpTabDetail", '@batchtopo.addsnmplabel@',  600, 470, url, null, true, true);
		}
	});
}
function deleteSnmpTab(id){
	window.top.showConfirmDlg('@COMMON.tip@', '@batchtopo.suretodeletesnmptab@', function(type) {
		if (type == 'no') {
	           return;
	    }
		var deletePromise = $.post("/batchautodiscovery/deleteSnmpTab.tv", {
			id: id
		});
		deletePromise.done(function(){
			refreshSnmpTabs();
		})
	});
}
function refreshSnmpTabs(){
	//获取SNMP标签信息
	var snmpPromise = $.get("/batchautodiscovery/loadSnmpTabs.tv");
	snmpPromise.done(function(response){
		batchTopoData.snmpConfigs = response.snmpConfigs;
		var snmpTabStr = '';
		$.each(batchTopoData.snmpConfigs, function(index, snmpConfig){
			var version = (snmpConfig.version==1) ? 'V2C' : 'V3';
			snmpTabStr += String.format('<span class="deleteSpan"><a id="{0}" onclick="editSnmpTab({2})" href="javascript:;" class="deleteSpanLink">{1}</a><b style="color:#8A8A8A"> ({3}) </b><a nm3ktip="@batchtopo.deletelabel@" onclick="deleteSnmpTab({0})" class="deleteSpanClose nm3kTip" href="javascript:;"></a></span>', snmpConfig.id, snmpConfig.name, index, version);		
		});
		if(batchTopoData.snmpConfigs.length<5){
			//加上添加标签
			snmpTabStr += String.format('<span><a onclick="addSnmpTab()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></span>');
		} else {
			//加上添加标签(置灰)
			snmpTabStr += String.format('<span><a onclick="addSnmpTab()" href="javascript:;" class="normalBtn" disabled><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></span>');
		}
		$('#snmpConfigTd').empty().append(snmpTabStr);
	});
}
function batchScan(){
	//获取选中行
	var sm = grid.getSelectionModel(),
		selectedNetSegment = sm.getSelections();
	if(selectedNetSegment.length===0){
		window.parent.showMessageDlg('@COMMON.tip@', '@batchtopo.selectnetworksegment@');
		return
	}
	
	window.top.showConfirmDlg('@COMMON.tip@', '@batchtopo.scannetworksegment@', function(type) {
		if (type == 'no') {
	           return;
	    }
		//获取选中行
		var sm = grid.getSelectionModel(),
			selectedNetSegment = sm.getSelections();
		//取出所有待删除的id
		var scanIds = [];
		for(var i=0; i<selectedNetSegment.length; i++){
			scanIds.push(selectedNetSegment[i].data.id);
		}
		window.parent.createDialog("scanResult", '@batchtopo.scanresults@', 650,470,
				"/network/batchAutoDiscovery/scanResult.jsp?scanIds="+scanIds.join(','), null, true, false);
	});
}
function validateTimeStrategy(){
	var strategyType = $('#strategyType').val(),	//策略类型
		periodHour = $('#periodHour').val(),		//定时策略的时
		peroidMinute = $('#periodMinute').val(),	//定时策略的分
		peroidSecond = $('#periodSecond').val(),	//定时策略的秒
		intervalPeriod = $('#period').val();		//间隔策略的分钟单位
	//这四个值必须为正整数
	var reg = /^\d+$/;
	//定时策略的时必须为正整数，而且必须小于24
	if(!reg.test(periodHour) || parseInt(periodHour, 10)>23){
		if($('#periodStart').css('display')=='none'){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.pleaseEnterVaildValue@'
			});
		}else{
			$('#periodHour').focus();
		}
		return false;
	}
	//定时策略的分必须为小于60的整数
	if(!reg.test(peroidMinute) || parseInt(peroidMinute, 10)>59){
		if($('#periodStart').css('display')=='none'){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.pleaseEnterVaildMinValue@'
			});
		}else{
			$('#periodMinute').focus();
		}
		return false;
	}
	//定时策略的秒必须为小于60的整数
	if(!reg.test(peroidSecond) || parseInt(peroidSecond, 10)>59){
		if($('#periodStart').css('display')=='none'){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.pleaseEnterVaildSecValue@'
			});
		}else{
			$('#peroidSecond').focus();
		}
		return false;
	}
	//间隔策略的值必须为正整数
	if(!reg.test(intervalPeriod) || parseInt(intervalPeriod, 10)<10 || parseInt(intervalPeriod, 10)>10080){
		if($('#periodSpan').css('display')=='none'){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.pleaseEnterVaildIntervalValue@'
			});
		}else{
			$('#period').focus();
		}
		return false;
	}
	return true;
}

function ipQueryClick(){
	var nmName = $('#inputNet').val().trim();
	var match=new matchNetSeg();
	if(match.checkType(nmName)==4){
		nmName=null;
	}
	batchTopo.store.baseParams={
		queryName:nmName
	}
	batchTopo.store.load();
}

function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn("refreshCmSignal_button", false);
    }else{
        disabledBtn("refreshCmSignal_button", true);
    }
};
function disabledBtn(id, disabled){
    Ext.getCmp(id).setDisabled(disabled);
};
$(function(){
	//填充SNMP基本参数
	$('#snmpTimeout').val(batchTopoData.snmpTimeout);
	$('#snmpRetries').val(batchTopoData.snmpRetries);
	$('#pingTimeoutConfig').val(batchTopoData.pingTimeoutConfig);
	//加载SNMP标签
	refreshSnmpTabs();
	//加上所有设备类型
	var deviceStr='';
	$.each(batchTopoData.allTypes, function(index, type){
		deviceStr += String.format('<span class="block140"><input class="deviceTypeCbx" id="deviceTypeCbx_{0}" type="checkbox" value="{0}"/><label>{1}</label></span>', type.typeId, type.displayName);
	})
	$('#allTypesP').append(deviceStr);
	//填充选中的设备类型
	$.each(batchTopoData.typeIds, function(index, type){
		$('#deviceTypeCbx_'+type.typeId).attr('checked',true);
	})
	if($('.deviceTypeCbx').length===$('.deviceTypeCbx:checked').length){
		$('#allTypeCbx').attr('checked', true);
	}else{
		$('#allTypeCbx').attr('checked', false);
	}
	$('#allTypeCbx').bind('click', function(){
		if($(this).attr("checked")){
			$('#allTypesP').find(".deviceTypeCbx").attr("checked", true);
		}else{
			$('#allTypesP').find(".deviceTypeCbx").attr("checked", false);
		}
	});
	$('#allTypesP').bind('click', function(){
		if($('.deviceTypeCbx').length===$('.deviceTypeCbx:checked').length){
			$('#allTypeCbx').attr('checked', true);
		}else{
			$('#allTypeCbx').attr('checked', false);
		}
	});
	//填充扫描策略
	$('#strategyType').val(batchTopoData.batchAutoDiscoveryPeriod.strategyType);
	var periodStart = batchTopoData.batchAutoDiscoveryPeriod.periodStart;
	$('#periodHour').val(Math.floor(periodStart/3600));
	$('#periodMinute').val((periodStart%3600)/60);
	$('#periodSecond').val((periodStart%3600)%60);
	$('#period').val(batchTopoData.batchAutoDiscoveryPeriod.period/60);
	$('#strategyType').bind('change', function(){
		var value = $(this).val();
		$('.strategyType').hide();
		if(value==1){
			$('#periodStart').show();
		}else{
			$('#periodSpan').show();
		}
	});
	$('#strategyType').trigger('change');
	//为定时策略的执行时间加上转换逻辑
	$('#periodHour').spinner({
		step:1,
		max:23,
		min:0
	});
	$('#periodMinute').spinner({
		step:1,
		max:59,
		min:0
	});
	$('#periodSecond').spinner({
		step:1,
		max:59,
		min:0
	});
	//为间隔策略的执行时间加上转换逻辑
	$('#period').spinner({
		step:1,
		max:10080,
		min:10
	});
	//扫描配置的保存
	$('#saveScanConfig').bind('click', function(){
		//获取所有设备类型
		var typeIds = [];
		$('.deviceTypeCbx:checked').each(function(){
			typeIds.push($(this).val());
		})
		//获取时间策略各时间输入框的值
		var strategyType = $('#strategyType').val(),
			periodHour = parseInt($('#periodHour').val(), 10),
			peroidMinute = parseInt($('#periodMinute').val(), 10),
			peroidSecond = parseInt($('#periodSecond').val(), 10);
		//对输入框进行校验
		if(!validateTimeStrategy()){
			return
		}
		var period = parseInt($('#period').val(), 10)*60;
		var periodStart = Math.floor(periodHour*3600) + Math.floor(peroidMinute *60) + peroidSecond;
		//下发保存请求
		var saveScanConfigPromise = $.post('/batchautodiscovery/saveScanConfig.tv', {
			ids: typeIds.join(','),
			strategyType: strategyType,
			periodStart: periodStart,
			period: period
		});
		saveScanConfigPromise.done(function(response){
			if(response.error){
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: '@batchtopo.savefail@'
				});
			}else{
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: '@batchtopo.savesuccessfull@'
				});
			}
		});
	})
	
	
	//为tab加上响应
	$("#tabUl").tabs("#tabContent");
	
	$('#tabContent').height($('body').height()-$('#tabUl').outerHeight()-30)
	
	batchTopo.sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	Ext.data.Store.prototype.applySort = function() { // 重写函数方法，实现中文按拼音排序
          if (this.sortInfo 
//        	&& !this.remoteSort) {
        	  ){
              var s = this.sortInfo, f = s.field;
              var st = this.fields.get(f).sortType;
              var fn = function(r1, r2) {
                  var v1 = st(r1.data[f]), v2 = st(r2.data[f]);
                  if (typeof(v1) == "string") {
                      return v1.localeCompare(v2);
                  }
                  return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
              };
              this.data.sort(s.direction, fn);
              if (this.snapshot && this.snapshot != this.data) {
                  this.snapshot.sort(s.direction, fn);
              }
          }
        };
	
//	batchTopo.sm = new Ext.grid.CheckboxSelectionModel();
	batchTopo.cm = new Ext.grid.ColumnModel([
	    batchTopo.sm,
      	{header: "@batchtopo.networksegment@", align: 'center', dataIndex: 'ipInfo', renderer: modifyRender}, 
		{header: "@COMMON.name@", align: 'center', dataIndex: 'name', renderer: modifyRender}, 
		{header: "@COMMON.folder@", align: 'center', dataIndex: 'folderName', renderer: folderRender}, 
		{header: "@batchtopo.createtime@", align: 'center', dataIndex: 'createtime', renderer: timeRender}, 
		{header: "@batchtopo.lastscantime@", align: 'center', dataIndex: 'lastDiscoveryTime', renderer: timeRender},
		{header: "@batchtopo.expectednextscantime@", align: 'center', dataIndex: 'nextFireTime', renderer: nextFirtimeRender},
      	{header: "@batchtopo.autoscanswitch@", align: 'center', dataIndex: 'autoDiscovery', renderer: scanRender}, 
      	{header: "@batchtopo.scanstatus@", align: 'center', dataIndex: 'autoDiscoveryStatus', renderer: scanOperationRender}, 
      	{header: "<div class='txtCenter'>@batchtopo.operate@</div>", fixed: true, dataIndex: 'operation', align: 'center', renderer: operationRender}
    ]);
	var columns=[	    
	    batchTopo.sm,
      	{header: "@batchtopo.networksegment@", align: 'center',sortable:true, dataIndex: 'ipInfo', renderer: modifyRender}, 
		{header: "@COMMON.name@", align: 'center', sortable:true,dataIndex: 'name', renderer: modifyRender}, 
		{header: "@COMMON.folder@", align: 'center', sortable:true,dataIndex: 'folderName', renderer: folderRender}, 
		{header: "@batchtopo.createtime@", align: 'center', sortable:true,dataIndex: 'createtime', renderer: timeRender}, 
		{header: "@batchtopo.lastscantime@", align: 'center', sortable:true,dataIndex: 'lastDiscoveryTime', renderer: timeRender},
		{header: "@batchtopo.expectednextscantime@", align: 'center', sortable:true,dataIndex: 'nextFireTime', renderer: nextFirtimeRender},
      	{header: "@batchtopo.autoscanswitch@", align: 'center', sortable:true,dataIndex: 'autoDiscovery', renderer: scanRender}, 
      	{header: "@batchtopo.scanstatus@", align: 'center', sortable:true,dataIndex: 'autoDiscoveryStatus', renderer: scanOperationRender}, 
      	{header: "<div class='txtCenter'>@batchtopo.operate@</div>", fixed: true, dataIndex: 'operation', align: 'center', renderer: operationRender}
      	]
	batchTopo.store = new Ext.data.JsonStore({
		url: '/batchautodiscovery/loadNetSegments.tv',
		root: 'data',
		idProperty: "id",
		remoteSort:true,
		fields: ['id', 'ipInfo', 'name', 'folderId', 'folderName', 'autoDiscovery', 'createtime', 'lastDiscoveryTime', 'nextFireTime', 'autoDiscoveryStatus']
	});
//根据输入框判断输入格式，匹配相应格式

	var match=new matchNetSeg();	
	batchTopo.store.on("load",function(){
		var inputNetIpVal=$('#inputNet').val();
		if(match.checkType(inputNetIpVal)==4){
	    	var count=batchTopo.store.getCount();
			for(var i=0;i<batchTopo.store.getCount();i++){
				if(count>0){
					var temp=batchTopo.store.getAt(i).json.ipInfo
					if(!match.strictMatch(temp,inputNetIpVal)){
						batchTopo.store.remove(batchTopo.store.getAt(i--));
						count--;
					}	
				}
			}	
		}   	
    });
	batchTopo.store.load();
	var config=CustomColumnModel.init('batchTopo',columns,{});
	var sortInfo=config.sortInfo||{field:'ipInfo',direction:'ASC'};
	var cm=config.cm;
	batchTopo.store.setDefaultSort(sortInfo.field,sortInfo.direction);
	
	var tbar = new Ext.Toolbar({
	    items: [
	      	{text: '@batchtopo.addnetworksegment@', id: 'refreshAllCm_button', iconCls: 'bmenu_new', handler: function(){
	      		var url = "/network/batchAutoDiscovery/netSegment.jsp?action=add";
	      		window.parent.createDialog("netSegment", '@batchtopo.addnetworksegment@',  800, 500, url, null, true, true);
	      	}}, 
	      	'-',
	      	{text: '@BUTTON.refresh@', id: '22', iconCls: 'bmenu_refresh', handler: function(){
	      		batchTopo.store.load({
	      			callback : function(){
	      				disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	      			}
	      		});
	      	}},
	      	'-',
	      	{text: '@batchtopo.deletenetworksegment@', id: 'refreshCmSignal_button', iconCls: 'bmenu_delete', disabled: true, handler: function(){
	      		deleteNetSegment();
	      	}}
	    ]
	});
	var $container = $('#tabContent');
	grid = new Ext.grid.EditorGridPanel({
	    cls: 'normalTable',
	    renderTo: 'gridContainer',
	    border: true,
	    store: batchTopo.store,
	    columns: columns,
	    loadMask: true,
	    sm: batchTopo.sm,
	    width: $container.width(),
	    height: $container.height(),
	    tbar: tbar,
	    autoScroll: false,
	    listeners:{
	    	sortchange:function(grid,sortInfo){
	    		CustomColumnModel.saveSortInfo('batchTopo',sortInfo);
	    	}
	    },
	    viewConfig: {
	      forceFit: true
	    }
	});
	
	$("#snmpTimeout").spinner({
		step:1,
		max:10000,
		min:1
	});
	
	$("#pingTimeoutConfig").spinner({
		step:1,
		max:30000,
		min:1
	});
	
	//数字输入框输入限制
	$(".spinnerInput").bind('keypress', function(e){
		var keychar = String.fromCharCode(e.which);
		var numReg = /\d/;
		return numReg.test(keychar)
	})
	
	$('#saveSnmpConfig').bind('click', function(){
		//校验SNMP超时时间和PING超时时间
		/*var snmpTimeout = parseInt($('#snmpTimeout').val(), 10),
		if(snmpTimeout<0 || snmpTimeout>10000){
			return $('#snmpTimeout').focus();
		}*/
		var reg = /^[0-9]\d*$/;
		if(!reg.test($('#pingTimeoutConfig').val())){
			return $('#pingTimeoutConfig').focus();
		}
		var pingTimeoutConfig = parseInt($('#pingTimeoutConfig').val(), 10);
		if(pingTimeoutConfig <= 0 || pingTimeoutConfig > 30000){
			return $('#pingTimeoutConfig').focus();
		}
		var saveSnmpConfigPromise = $.post('/batchautodiscovery/saveSnmpConfig.tv',{
			//snmpTimeout: snmpTimeout,
			//snmpRetries: $('#snmpRetries').val(),
			pingTimeoutConfig: pingTimeoutConfig
		});
		saveSnmpConfigPromise.done(function(){
			top.nm3kRightClickTips({
				title: "@COMMON.tip@",
				html: '@batchtopo.savesuccessfull@'
			});
		});
	});
	
	var resizeGrid = function(){
		$('#tabContent').height($('body').height()-$('#tabUl').outerHeight()-30)
		grid.setSize($container.width(), $container.height());
	}
	$(window).resize(function(){
		throttle(resizeGrid)
	});
	
	function throttle(method, context) { 
		clearTimeout(method.tId);
		method.tId= setTimeout(function(){ 
			method.call(context); 
		}, 200);
	}
	
	$('#queryIpClick').live('click',function(){
//		batchTopo.store.reload();
		ipQueryClick();
	})


	
	function modifyRender(value){
		return String.format('<a class="yellowLink" href="javascript:;" onclick="modifyNetSegment()">{0}</a>', value);
	}
	
	function scanRender(value, p, record){
		if (value) {
	    	return String.format('<img nm3kTip="{0}" class="nm3kTip clickable" onclick="updateBatchTopoSwitch();" src="/images/performance/on.png" border=0 align=absmiddle>',
	      	"@batchtopo.clicktoclose@");
	  	} else {
	    	return String.format('<img nm3kTip="{0}" class="nm3kTip clickable" onclick="updateBatchTopoSwitch();" src="/images/performance/off.png" border=0 align=absmiddle>',
	      	"@batchtopo.clicktoopen@");
	  	}
	}
	
	function operationRender(){
		var str = '';
		str += "<a class='yellowLink' href='javascript:;' onclick='batchScan()' >@batchtopo.scan@</a>" + " / "
		str += "<a class='yellowLink mR10' href='javascript:;' onclick='deleteNetSegment()' >@COMMON.delete@</a>";
		return str;
		var str = '';
	}
	function folderRender(value){
		if(value && value!==''){
			return value;
		}
		return '@batchtopo.nofolder@';
	}
	function timeRender(value){
		if(value){
			var date = new Date;
			date.setTime(value.time);
			return formatTime(date);
		}
		return '@batchtopo.notperformed@';
		
		function formatTime(date) {
		    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + " " + formatNum(date.getHours()) + ":" + formatNum(date.getMinutes()) + ":" + formatNum(date.getSeconds());

		    function formatNum(num) {
		      return (num < 10) ? '0' + num : num;
		    }
		  }
	}
	
	function nextFirtimeRender(value){
		if(value){
			var date = new Date;
			date.setTime(value.time);
			return formatTime(date);
		}
		return '@batchtopo.unknown@';
		
		function formatTime(date) {
		    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + " " + formatNum(date.getHours()) + ":" + formatNum(date.getMinutes()) + ":" + formatNum(date.getSeconds());

		    function formatNum(num) {
		      return (num < 10) ? '0' + num : num;
		    }
		  }
	}
	
	function scanOperationRender(value){
		var str="";
		switch(value){
		case 0:
			str = '<span class="">@batchtopo.notscanned@</span>';
			break;
		case 1:
			str = '<span class="progressMsg">@batchtopo.scanning@</span>';
			break;
		case 2:
			str = '<span class="successMsg">@batchtopo.scansuccessful@</span>';
			break;
		case 3:
			str = '<span class="failMsg">@batchtopo.scanfail@</span>';
			break;
		}
		return str;
	}
});