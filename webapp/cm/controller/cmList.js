/* ==========================================================
 * 这段内容是CM列表功能所能公用的全局变量
 * ========================================================== */
//当前查询条件
var queryData = {};
var lastRow = null;
var partitionData = {
	upSnrMin: '',
    upSnrMax: '',
    downSnrMin: '',
    downSnrMax: '',
    upPowerMin: '',
    upPowerMax: '',
    downPowerMin: '',
    downPowerMax: ''
};
var chlDisplayMode = 'all';

var default_columns = [
    {header: "<div class='txtCenter'>IP</div>", width: 100, sortable: true, align: 'left', dataIndex: 'displayIp', renderer: renderIp}, 
    {header: "MAC", width: 150, sortable: true, align: 'center', dataIndex: 'statusMacAddress', renderer: renderMac}, 
    {header: "@CMC.title.status@", width: 50, sortable: false, align: 'center', dataIndex: 'statusValue', renderer: CmUtil.statusValueRender}, 
    {header: "<div class='txtCenter'>@CM.ServiceType@</div>", width: 150, sortable: false, align: 'left', dataIndex: 'cmServiceType'}, 
    {header: "@CM.docsicVersion@", width: 100, sortable: false, align: 'center', dataIndex: 'docsisMode',renderer: renderDocsisMode}, 
    {header: "<div class='txtCenter'>@CMCPE.upDevice@</div>", width: 90, sortable: false, align: 'left', dataIndex: 'cmcName'}, 
    {header: "@cmcUserNum.lastCollectTime@", width: 127, sortable: true, align: 'center', dataIndex: 'lastRefreshTime'}, 
    {header: "<div class='txtCenter'>@CM.partialUpChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialUpChannels'},
    {header: "<div class='txtCenter'>@CM.partialDownChl@</div>", width: 62, sortable: false, align: 'left', dataIndex: 'partialDownChannels'},
    {header: "<div class='txtCenter'>@upchannel@</div>", width: 62, sortable: true, align: 'left', dataIndex: 'statusUpChannelIfIndex', renderer: renderUpChannel}, 
    {header: "<div class='txtCenter'>@CCMTS.downStreamChannel@</div>", width: 62, sortable: true, align: 'left', dataIndex: 'statusDownChannelIfIndex',renderer: renderDownChannel}, 
    {header: "<div class='txtCenter'>@CM.upSnr@(dB)</div>", width: 62, sortable: false, align: 'left', dataIndex: 'statusSignalNoise', renderer: renderUpChannelSnr}, 
    {header: "<div class='txtCenter'>@CM.downSnr@(dB)</div>", width: 62, sortable: false, align: 'left', dataIndex: 'downChannelSnr', renderer: renderDownChannelSnr}, 
    {header: "<div class='txtCenter'>@CM.upSendPower@(@{unitConfigConstant.elecLevelUnit}@)</div>", width: 100, sortable: false, align: 'left', dataIndex: 'upChannelTx', renderer: renderUpChannelTx}, 
    {header: "<div class='txtCenter'>@CM.downReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</div>", width: 100, sortable: false, align: 'left', dataIndex: 'downChannelTx', renderer: renderDownChannelTx},
    {header: "@CM.previousState@", width: 100, sortable: false, align: 'center', dataIndex: 'preStatus',renderer: renderPreviousState}, 
    {header: 'FLAP Ins', width: 100, sortable: true, align: 'center', dataIndex: 'topCmFlapInsertionFailNum', renderer: renderFlapChart},
    {header: '@CMCPE.CPENUM@', width: 80, sortable: false, align: 'center', dataIndex: 'cpeNum',renderer: renderCpeNum}, 
    {header: '@CMC.title.Alias@', width: 100, sortable: false, align: 'center', dataIndex: 'cmAlias'}, 
    {header: '@CMC.title.usage@', width: 100, sortable: false, align: 'center', dataIndex: 'cmClassified'}, 
    {header: '@CM.userNo@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userId'}, 
    {header: '@CM.userName@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userName'}, 
    {header: '@CM.userAddr@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userAddr'}, 
    {header: '@CM.userPhone@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'userPhoneNo'}, 
    {header: '@CM.packageType@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'offerName'}, 
    {header: '@CM.effectTime@', width: 140, sortable: false, hidden:true, align: 'center', dataIndex: 'effDate'}, 
    {header: '@CM.expirationTime@', width: 140, sortable: false, hidden:true, align: 'center', dataIndex: 'expDate'}, 
    {header: '@CM.configFile@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'configFile'},
    {header: '@CM.extension@', width: 100, sortable: false, hidden:true, align: 'center', dataIndex: 'extension'},
    {header: "<div class='txtCenter'>@VLAN.vlanOpera@</div>", width: 180, fixed: true, sortable: false, align: 'left', dataIndex: 'cmClassified',renderer: renderOperation}
];

//其他菜单
var grid = null;
var entityMenu = null;
var refreshCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='refreshCmInfo(\"{0}\",\"{1}\")' >@route.button.refresh@</a>";
var restartCmFormatStr = "<a class='yellowLink' href='javascript:;' onclick='restarCm(\"{0}\",\"{1}\",\"{2}\",\"{3}\")'/>@CCMTS.cm.reset@</a>";
var otherOperFormatStr = "<a class='withSub mR10' href='javascript:;' onclick='showMoreOperation({0},event)'>@TIP.other@</a>";
var refreshedCmCounter; //已刷新CM计数器
var responseTimer; //Action回复的定时器，如果超时将页面上恢复到刷新前
var dwrResponseTimer; //DWR回复的定时器，如果超时将页面上恢复到刷新前
var expander;
var cmNumDivStr = '<div id="cm-num-div">'
  .concat('<label>@CHANNEL.totalCmNum@</label><span style="margin-right:10px;" id="cm-total-num-span">0</span>')
  .concat('<label>@CM.regestNum@</label><span id="cm-online-num-span">0</span>')
  .concat('</div>');
/* ======================================================================
 * 这段内容是页面上的一些效果，比如快捷查询和高级查询之间的切换，resize事件等
 * ==================================================================== */

function openPartitionSelect(){
	window.top.createDialog("modalDlg",'@CM.setPartition@',800, 370, '/cmlist/showPartitionSelect.tv?partitionDataStr='+encodeURI(JSON.stringify(partitionData)), null, true,true);
}

function partitionChanged(data){
	partitionData = data;
	$('#partition').cmIndexPartition.change(partitionData);
	var tmpData = $.extend(queryData, partitionData);
	store.load({
        params: tmpData
    });
}

//保存用户选择的查询模式
function saveUserQueryMode(isSimpleSearch){
	$.ajax({
		url: '/cmlist/saveCmQueryView.tv',
		cache:false, 
		method:'post',
		data: {
			simpleModeFlag : isSimpleSearch
		},
		success: function() {
		},
		error: function(){
		}
	});
}

function showAdvanceQuery() {
	saveUserQueryMode(false);
  //隐藏简单查询DIV
  $('#simple-toolbar-div').effect("drop", {
    times: 1
  }, 200, function() {
    //显示高级查询DIV
    $('#advance-toolbar-div').effect("slide", {
      times: 1
    }, 200);
  });
}


function createOltCombo() {
  window.selector = new NetworkNodeSelector({
    id: 'select_olt',
    renderTo: "oltContainer",
    //value : window["entityId"], //@赋值的方式一：配置默认值 
    autoLayout: true,
    listeners: {
      selectChange: oltSelChanged
    }
  });
  //@赋值方法二：  调用 setValue({value}). eg : selector.setValue( window["entityId"] );
}

function renderTime(value){
	if(value==null){return '-';}
	var date = new Date();
	date.setTime(value.time);
	return Ext.util.Format.date(date, 'Y-m-d H:i:s');
}

function showSimpleQuery() {
	saveUserQueryMode(true);
  //隐藏高级查询DIV
  $('#advance-toolbar-div').effect("drop", {
    times: 1
  }, 200, function() {
    //显示快捷查询DIV
    $('#simple-toolbar-div').effect("slide", {
      times: 1
    }, 200);
  });
}

/* ==========================================================
 * 这段内容是页面上的下拉框事件及内容加载
 * ========================================================== */

function deviceTypeSelChanged(callback) {
  var deviceTypeId = parseInt($('#select_deviceType').val());
  var oltType = EntityType.getOltType();
  var cmtsType = EntityType.getCmtsType();
  //当设备类型下拉框发生改变时，应当首先重置各下拉框，然后初始化相应的下拉框

  if (!window.selector) {
    createOltCombo();
  }
  selector.setValue(-1);

  $('#select_pon').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_ccmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_upCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_downCnl').empty().append('<option value="0">@CMC.select.select@</option>');

  if (deviceTypeId == 0) {
    $('#td_olt').next().andSelf().hide();
    $('#td_pon').next().andSelf().hide();
    $('#td_ccmts').next().andSelf().hide();
    $('#td_cmts').next().andSelf().hide();
    $('#td_upCnl').next().andSelf().hide();
    $('#td_downCnl').next().andSelf().hide();
    if (typeof callback === 'function') {
      callback();
    }
  } else if (deviceTypeId == oltType) {
    $('#td_olt').next().andSelf().show();
    $('#td_pon').next().andSelf().show();
    $('#td_ccmts').next().andSelf().show();
    $('#td_cmts').next().andSelf().hide();
    $('#td_upCnl').next().andSelf().show();
    $('#td_downCnl').next().andSelf().show();
    if (typeof callback === 'function') {
      callback();
    }
  } else if (EntityType.isCcmtsWithAgentType(deviceTypeId)) {
    $('#td_olt').next().andSelf().hide();
    $('#td_pon').next().andSelf().hide();
    $('#td_ccmts').next().andSelf().show();
    $('#td_cmts').next().andSelf().hide();
    $('#td_upCnl').next().andSelf().show();
    $('#td_downCnl').next().andSelf().show();
    //初始化CCMTS下拉框
    var loadCcmtsPromise = $.post('/cmlist/loadDeviceListByTypeId.tv', {deviceType: deviceTypeId});
    loadCcmtsPromise.done(function(response) {
      $.each(response, function(index, cmcPair) {
        $('#select_ccmts').append(String.format('<option value="{0}">{1}</option>', cmcPair.entityId, cmcPair.ip));
      });
      if (typeof callback === 'function') {
        callback();
      }
    });
  } else if (deviceTypeId == cmtsType) {
    $('#td_olt').next().andSelf().hide();
    $('#td_pon').next().andSelf().hide();
    $('#td_ccmts').next().andSelf().hide();
    $('#td_cmts').next().andSelf().show();
    $('#td_upCnl').next().andSelf().show();
    $('#td_downCnl').next().andSelf().show();
    //初始化CMTS下拉框
    var loadCmtsPromise = $.post('/cmlist/loadDeviceListByType.tv', {deviceType: deviceTypeId});
    loadCmtsPromise.done(function(response) {
      $.each(response, function(index, cmcPair) {
        $('#select_cmts').append(String.format('<option value="{0}">{1}</option>', cmcPair.entityId, cmcPair.ip));
      });
      if (typeof callback === 'function') {
        callback();
      }
    });
  }
}

function oltSelChanged(callback) {
  var oltEntityId = $('#select_olt').val();
  //首先重置PON/CCMTS/CMTS/上行及下行信息
  $('#select_pon').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_ccmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_upCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_downCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  if (oltEntityId == 0 || oltEntityId == -1 || oltEntityId == "") {
    return;
  }
  //动态获取该OLT下的PON口信息加载给PON口下拉框
  var loadOltPromise = $.post('/eponcmlist/loadPonOfOlt.tv', {entityId: oltEntityId});
  loadOltPromise.done(function(ponList){
	//如果此时PON口有数据，表明已有返回数据填充了，取消操作 
	  if($('#select_pon > option').length === 1){
		//在进行插入前，需要对pon口进行排序
		    ponList.sort(function(a, b) {
		      if (a.ponIndex > b.ponIndex) {
		        return 1;
		      } else if (a.ponIndex == b.ponIndex) {
		        return 0;
		      } else {
		        return -1;
		      }
		    });
		    $.each(ponList, function(index, pon) {
		      $('#select_pon').append(String.format('<option value="{0}">{1}</option>', pon.ponId, pon.ponIndex));
		    });
	  }
	
    if (typeof callback === 'function') {
      callback();
    }
  });
}

function ponSelChanged(callback) {
  var oltEntityId = $('#select_olt').val();
  var ponId = $('#select_pon').val();
  //首先重置CCMTS/CMTS/上行及下行信息
  $('#select_ccmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_upCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_downCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  if (oltEntityId == 0 || ponId == 0) {
    return;
  }
  //动态获取该PON口下的CCMTS数据加载给CCMTS下拉框
  var loadPonPromise = $.post('/cmlist/loadCcmtsOfPon.tv', {entityId: oltEntityId, ponId: ponId});
  loadPonPromise.done(function(ccmtsList){
	$.each(ccmtsList, function(index, ccmts) {
	  $('#select_ccmts').append(String.format('<option value="{0}">{1}</option>', ccmts.cmcId, ccmts.name));
	});
	if (typeof callback === 'function') {
	  callback();
	}
  });
}

function ccmtsSelChanged(callback) {
  var cmcId = $('#select_ccmts').val();
  //首先重置CMTS/上行及下行信息
  $('#select_cmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_upCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_downCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  if (cmcId == 0) {
    return;
  }
  //CCMTS的上行固定为4个，下行固定为16个
  for (var i = 1; i <= 4; i++) {
    $('#select_upCnl').append(String.format('<option value="{0}">{0}</option>', i));
  }
  for (var i = 1; i <= 16; i++) {
    $('#select_downCnl').append(String.format('<option value="{0}">{0}</option>', i));
  }
  if (typeof callback === 'function') {
    callback();
  }
}

function cmtsSelChanged(callback) {
  var cmtsId = $('#select_cmts').val();
  //首先重置CCMTS/上行及下行信息
  $('#select_ccmts').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_upCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  $('#select_downCnl').empty().append('<option value="0">@CMC.select.select@</option>');
  if (cmtsId == 0) {
    return;
  }
  //动态获取CMTS的上下行信息
  var loadChannelsPromise = $.post('/cmlist/loadUpDownChlOfCmts.tv', {cmtsId: cmtsId});
  loadChannelsPromise.done(function(json){
	$.each(json.cmtsUpChannelList, function(index, upChannel) {
	  $('#select_upCnl').append(String.format('<option value="{0}">{1}</option>', upChannel.channelIndex, upChannel.ifDescr));
	});
	$.each(json.cmtsDownChannelList, function(index, downChannel) {
	  $('#select_downCnl').append(String.format('<option value="{0}">{1}</option>', downChannel.channelIndex, downChannel.ifDescr));
	});
	if (typeof callback === 'function') {
	  callback();
	}
  });
}

function showLoading(message) {
  $("#loading").text(message);
  var lPos = ($(window).width() - $("#loading").outerWidth()) / 2;
  var tPos = 150;
  $("#loading").show().css({
    top: tPos,
    left: lPos
  });
}

function hideLoading() {
  $("#loading").hide();
}

/* ==========================================================
 * 这段内容是查询CM列表的方法
 * ========================================================== */

function queryCmList() {
	lastRow = null;
  //获取查询条件
  var deviceType = parseInt($('#select_deviceType').val()),
    entityId = parseInt($('#select_olt').val()),
    ponId = parseInt($('#select_pon').val()),
    cmcId = parseInt($('#select_ccmts').val()),
    cmtsId = parseInt($('#select_cmts').val()),
    upChannelId = parseInt($('#select_upCnl').val()),
    downChannelId = parseInt($('#select_downCnl').val()),
    cmMac = $('#cmMacAddress').val(),
    cmIp = $('#cmIpAddress').val(),
    statusValue = $('#select_status').val(),
    docsisMode = $('#select_docsisMode').val(),
    cmServiceType = $('#cmServiceType').val();
  //高级查询状态下验证MAC地址和IP地址是否合法
  if (cmIp != "" && !Validator.isFuzzyIpAddress(cmIp)) {
    top.afterSaveOrDelete({
      title: '@COMMON.tip@',
      html: '<b class="orangeTxt">@CM.ipIllegal@</b>'
    });
    $('#cmIpAddress').focus();
    return;
  }

  if (cmMac != "" && !Validator.isFuzzyMacAddress(cmMac)) {
    top.afterSaveOrDelete({
      title: '@COMMON.tip@',
      html: '<b class="orangeTxt">@ccm/CCMTS.CmList.errorMac@</b>'
    });
    $('#cmMacAddress').focus();
    return;
  }
  var cmtsType = EntityType.getCmtsType();
  var oltType = EntityType.getOltType();
  //组装查询条件
  if (oltType == deviceType || deviceType == 0) {
    queryData = {
      queryModel: 'advance',
      deviceType: deviceType,
      entityId: entityId,
      ponId: ponId,
      cmcId: cmcId,
      upChannelId: upChannelId,
      downChannelId: downChannelId,
      cmMac: cmMac,
      cmIp: cmIp,
      statusValue: statusValue,
      docsisMode: docsisMode,
      cmServiceType: cmServiceType,
      start: 0,
      limit: pageSize,
      userId: $('#userId').val(),
      userName: $('#userName').val(),
      userAddr: $('#userAddr').val(),
      userPhoneNo: $('#userPhoneNo').val(),
      offerName: $('#offerName').val(),
      configFile: $('#configFile').val()
    };
  } else if (EntityType.isCcmtsWithAgentType(deviceType)) {
    queryData = {
      queryModel: 'advance',
      deviceType: deviceType,
      cmcId: cmcId,
      upChannelId: upChannelId,
      downChannelId: downChannelId,
      cmMac: cmMac,
      cmIp: cmIp,
      statusValue: statusValue,
      docsisMode: docsisMode,
      cmServiceType: cmServiceType,
      start: 0,
      limit: pageSize,
      userId: $('#userId').val(),
      userName: $('#userName').val(),
      userAddr: $('#userAddr').val(),
      userPhoneNo: $('#userPhoneNo').val(),
      offerName: $('#offerName').val(),
      configFile: $('#configFile').val()
    }
  } else if (cmtsType == deviceType) {
    queryData = {
      queryModel: 'advance',
      deviceType: deviceType,
      cmtsId: cmtsId,
      upChannelIndex: upChannelId,
      downChannelIndex: downChannelId,
      cmMac: cmMac,
      cmIp: cmIp,
      statusValue: statusValue,
      docsisMode: docsisMode,
      cmServiceType: cmServiceType,
      start: 0,
      limit: pageSize,
      userId: $('#userId').val(),
      userName: $('#userName').val(),
      userAddr: $('#userAddr').val(),
      userPhoneNo: $('#userPhoneNo').val(),
      offerName: $('#offerName').val(),
      configFile: $('#configFile').val()
    };
  }else{
	  //没有组装查询条件
  }
  //加载store
  var tmpdata = $.extend(queryData, partitionData);
  store.baseParams = tmpdata;
  store.load({
	  callback: function(){
		var sm = grid.getSelectionModel();
		if(sm.getCount() === grid.store.getCount()){
  			sm.selectAll();
  		}
	  }
  });
}

function simpleQuery() {
  var queryContent = $.trim($('#queryContent').val());
  // add by fanzidong,需要支持多条件
  var array = queryContent.split(/[\n\s]+/);
  // 最多支持100个查询条件，总长度不能长于2000
  if(array.length > 100) {
	  return window.parent.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit1@');
  } else if(queryContent.length > 2000) {
	  return window.parent.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit2@');
  }
  
  
  // 针对每一个查询条件，如果是完整的MAC地址或者模糊的MAC地址，尝试进行转换
  // modify by fanzidong，不应该进行转换，难免将IP之类的查询条件错误转换，而应该进行添加
  // add by fanzidong, 顺便进行字符校验
  var toBeAddArray = [];
  var passValidate = true;
  $.each(array, function(index, txt) {
	  if(txt && !V.isAnotherName(txt)) {
		  passValidate = false;
		  return false;
	  }
	  if(top.MacUtil.isFuzzyMacAddress(txt)) {
		  toBeAddArray.push(top.MacUtil.formatQueryMac(txt));
	  }
  });
  if(!passValidate) {
	  return window.parent.showMessageDlg('@COMMON.tip@', '@CM.simpleLimit3@');
  }
  
  array = array.concat(toBeAddArray);
  
  queryData = {
    queryModel: 'simple',
    queryContent: array.join('|'),
    start: 0,
    limit: pageSize
  };
  store.baseParams = queryData;
  store.load({
	  callback: function(){
			var sm = grid.getSelectionModel();
			if(sm.getCount() === grid.store.getCount()){
	  			sm.selectAll();
	  		}
		  }
	  });
}

function updateCmNum() {
  var loadCmNumPromise = $.post('/cmlist/loadCmNum.tv', queryData);
  loadCmNumPromise.done(function(json){
	$('#cm-total-num-span').text(json.cmTotalNum);
    $('#cm-online-num-span').text(json.cmOnlineNum);
  });
}

function refreshGrid() {
  store.reload();
}

function hideTr(cmCollectMode) {
	if (cmCollectMode != null && cmCollectMode == 1) {
		  $("#frequency_tr").css("display","");
	} else {
		  $("#frequency_tr").css("display","none");
	}
}

function signalNoiseRender(value, p, record) {
	return record.data.docsIfCmtsCmStatusSignalNoiseString;
}

/* ==========================================================
 * 这段内容是页面grid的初始化方法
 * ========================================================== */
$(document).ready(function() {
	var $simple = $("#simple-toolbar-div"),
	    $advance = $("#advance-toolbar-div");
	if(isSimpleSearch){ //如果是简单查询;
		$simple.css({display : 'block'});
		$advance.css({display : 'none'});
	}else{ //如果是高级查询;
		$simple.css({display : 'none'});
		$advance.css({display : 'block'});
	}	
	
	//modify by fanzidong, 需要一开始加载好设备类型下拉框，为后续从跳转选择服务
	buildEntityTypeSelect();
	
	Ext.Ajax.timeout = 500000;
	
  Ext.QuickTips.init();
	var aaaa=$('#partition').cmIndexPartition();
	aaaa.bind('click', openPartitionSelect);
  $(".olt-sidebar").hide();
  if (cmCollectMode != null && cmCollectMode) {
	  $("#frequency_tr").css("display","");
  }
  
  //代理3.0CM信号质量tooltip显示事件
  $('body').delegate('div.docsis3', 'mouseenter', function(e){
	  var $target = $(e.currentTarget),
	  	cmId = $target.data('cmid'),
	  	type = $target.data('type');

	  //找到对应的数据
	  var record = store.getById(cmId);
	  if(record){
		  var cm = record.data;
		  var tipTitle = cm.displayIp + '(' + cm.statusMacAddress + ') ';
		  var datas = [];
		  switch(type){
		  case 'upChannelSnr':
			  tipTitle += '@CM.upSnr@';
			  $.each(cm.upChannelCm3Signal, function(i, cm3Signal){
				  datas.push({
					  id: cm3Signal.channelId,
					  value: cm3Signal.upChannelSnr + 'dB'
				  });
			  });
			  break;
		  case 'downChannelSnr':
			  tipTitle += '@CM.downSnr@';
			  $.each(cm.downChannelCm3Signal, function(i, cm3Signal){
				  datas.push({
					  id: cm3Signal.channelId,
					  value: cm3Signal.downChannelSnr + 'dB'
				  });
			  });
			  break;
		  case 'upChannelTx':
			  tipTitle += '@CM.upSendPower@';
			  $.each(cm.upChannelCm3Signal, function(i, cm3Signal){
				  datas.push({
					  id: cm3Signal.channelId,
					  value: cm3Signal.upChannelTx + '@{unitConfigConstant.elecLevelUnit}@'
				  });
			  });
			  break;
		  case 'downChannelTx':
			  tipTitle += '@CM.downReceivePower@';
			  $.each(cm.downChannelCm3Signal, function(i, cm3Signal){
				  datas.push({
					  id: cm3Signal.channelId,
					  value: cm3Signal.downChannelTx + '@{unitConfigConstant.elecLevelUnit}@'
				  });
			  });
			  break;
		  }
		  
		  //显示tooltip
		  var $cm3Tip = $('#cm3Tip');
		  $cm3Tip.empty();
			
		  //创建内容
		  var cm3Tpl = new Ext.XTemplate(
			  '<div class="bubbleTip" id="cm3Tip">',
			  	'<div class="bubbleBody">',
			  		'<p class="pT5"><b class="gray555">{tipTitle}</b></p>',
			  		'<tpl for="datas">',
			  			'<tpl if="xindex%4 == 1">',
			  				'<div class="subDashedLine wordBreak">',
			  			'</tpl>',
			  				'<span style="display:inline-block;width:80px;color:#0267B7;">{id}({value})</span>',
			  			'<tpl if="xindex%4 == 0">',
			  				'</div>',
			  			'</tpl>',
				  		'<tpl if="xindex == xcount && xcount%4 != 0">',
		  					'</div>',
		  				'</tpl>',
			  		'</tpl>',
				'</div>',
				'<div class="bubbleTipArr"></div>',
			  '</div>'
		  );
		
		  cm3Tpl.overwrite('cm3Tip', {
			  tipTitle: tipTitle,
			  datas: datas
		  });
		
		  $cm3Tip.css({
			  left: $target.offset().left - 376, 
			  top: $target.offset().top
		  }).show();
		  
		  var h = $(window).height(),
    	  	h2 = h - $target.offset().top;
		  
		  if( h2 > $cm3Tip.outerHeight() ){
			  $cm3Tip.find(".bubbleTipArr").css("top",0);	
		  }else{
			  $cm3Tip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr2");
			  $cm3Tip.css({
				  top : $target.offset().top - $cm3Tip.outerHeight() + 10
			  });
		  }
	  }
  }).delegate('div.docsis3', 'mouseleave', function(e){
	  $('#cm3Tip').empty().hide();
  });
  
  //初始化高级查询样式
  $('#select_deviceType').val(0).trigger("onchange");

  //设置右侧箭头国际化
  $("#cmListSideArrow").attr("class", language == 'en_US' ? "cmListSideEnglish" : "cmListSideChinese");

  function autoSetArrPosition() { //设置右侧箭头在屏幕中间;
    var h = $(window).height(),
      h2 = (h - 140) / 2;
    if (h2 < 0) {
      h2 = 0;
    }
    $("#cmListSideArrow").css({
      top: h2
    });
  }
  autoSetArrPosition();
  $(window).resize(function() {
    autoSetArrPosition();
  });
  $("#cmListSideArrow").click(function() { //展开，折叠;
    if ($("#arrow").hasClass("cmListSideArrLeft")) { //展开
      showHideSide("show");
    } else { //折叠;
      showHideSide("hide");
    }
  });

  function showHideSide(str) { //展开，折叠;
    switch (str) {
      case "show":
        $("#arrow").removeClass("cmListSideArrLeft").addClass("cmListSideArrRight");
        $("#cmListSideArrow").animate({
          right: 399
        });
        $("#cmListSidePart").animate({
          right: 0
        });
        break;
      case "hide":
        $("#arrow").removeClass("cmListSideArrRight").addClass("cmListSideArrLeft");
        $("#cmListSideArrow").animate({
          right: 0
        });
        $("#cmListSidePart").animate({
          right: -400
        });
        break;
    }
  }
  var w = ($(window).width() >= $(document.body).width()) ? $(window).width() : $(document.body).width() - 35;

  expander = new Ext.ux.grid.RowExpander({
		id : 'expander' ,
		dataIndex : 'cmId',
	    enableCaching : false,
	  tpl : ''
	});
  
  Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected"); 
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
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
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
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

  var sm = new Ext.grid.CheckboxSelectionModel({
  	listeners : {
  		rowselect : function(sm,rowIndex,record){
  			disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
  		},
  		rowdeselect : function(sm,rowIndex,record){
  			disabledToolbarBtn(grid.getSelectionModel().getSelections().length)
  		}
  	}
  });
  var cmConfig = CustomColumnModel.init('cmlist', default_columns, cpeSwitch ? {sm: sm, expander: expander} : {sm: sm}),
  	        cm = cmConfig.cm,
  	  sortInfo = cmConfig.sortInfo;

  store = new Ext.data.JsonStore({
    url: '/cmlist/loadCmList.tv',
    root: 'data',
    idProperty: "cmId",
    totalProperty: 'rowCount',
    remoteSort: true,
    fields: [
      'oltId', 'ponId', 'cmId', 'cmcId', 'cmcName', 'name', 'cmcDeviceStyle', 'upChannelIndexString', 'downChannelIndexString',
      'displayIp', 'displayStatus','supportWebProxy',
      'docsIfCmtsCmStatusInetAddressTypeString', 'cmAlias', 'cmClassified', 'topCmFlapInsertionFailNum', 'downChannelId',
      'statusIpAddress', 'statusMacAddress', 'statusDownChannelIfIndex', 'statusUpChannelIfIndex', 'docsIf3CmtsCmUsStatusList',
      'docsIfSigQSignalNoiseForUnit', 'docsIfCmtsCmStatusRxPowerString', 'statusSignalNoise', 'downChannelSnr', 'docsIfCmtsCmStatusSignalNoiseString',
      'downChannelTx', 'upChannelTx', 'docsIfCmtsCmStatusValueString', 'statusValue', 'upChannelId', 'upChannelSnr', 'partialUpChannels', 'partialDownChannels',
      'lastRefreshTime', 'downChannelFrequency', 'upChannelFrequency', 'collectTime', 'statusIndex', 'supportStaticIp', 'supportCpeInfo', 'supportReset', 'supportCmUpgrade','supportClearSingleCm','downChannelRecvPower','upChannelTransPower', 'cpeNum',
      'userId', 'userName', 'userAddr', 'userPhoneNo', 'offerName', 'effDate', 'expDate', 'configFile', 'extension','preStatus','docsisMode', 'cmSignal', 'upChannelCm3Signal', 'downChannelCm3Signal','cmServiceType'
      ]
  });
  
  //store在翻页之前加上查询参数
  store.on('beforeload', function() {
    store.baseParams = queryData;
  });
  /*if(cmConfig.sortInfo){
	  store.setDefaultSort(cmConfig.sortInfo.field, cmConfig.sortInfo.direction);
  }*/

  //store在加载完数据后更新CM数目信息
  store.on('load', function() {
    //刷新页面统计信息
    updateCmNum();
    //清除批量操作信息
    $('#suc-num-dd').text(0);
    $('#fail-num-dd').text(0);
    if(lastRow !== null) {
    	expander.expandRow(lastRow);
    } else {
    	// 不希望显示
    }
  });
  //store.setDefaultSort(sortInfo.field, sortInfo.direction);
  tbar = new Ext.Toolbar({
    items: [
      {text: '@ccm/CCMTS.CmList.clearOfflineCm@', id: 'clearSingleCm', disabled: !operationDevicePower,disabled:true, iconCls: 'bmenu_delete', handler: clearThisCM}, 
      '-',       
      {text: '@ccm/CCMTS.CmList.resetOnlineCm@', id: 'refreshAllCm_button', disabled: !operationDevicePower, iconCls: 'bmenu_restart', handler: restartAllCm}, 
      '-', 
      {text: '@contactCmList.refreshCmSq@', id: 'refreshCmSignal_button', iconCls: 'bmenu_refresh', handler: refreshCmSignal, disabled:!refreshDevicePower}, 
      '-', 
      {xtype: 'radiogroup', id: 'switchChannelMode', width: 190, items: [{
    	  boxLabel: '@CM.allChannel@', name: 'channelViewMode', inputValue: 'all', checked: true
      }, {
    	  boxLabel: '@CM.mainChannel@', name: 'channelViewMode', inputValue: 'main'
      }], listeners: {
		  'change': {
			  fn: switchChannelViewMode,
			  scope: this
		  }
	  }}, 
      '->', 
      {xtype: 'tbtext', text: cmNumDivStr}
    ]
  });
  bbar = new Ext.PagingToolbar({
    id: 'extPagingBar',
    pageSize: pageSize,
    store: store,
    displayInfo: true,
    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
  });
  grid = new Ext.grid.EditorGridPanel({
    cls: 'normalTable',
	bodyCssClass : 'normalTable',	
    region: 'center',
    border: true,
    totalProperty: 'rowCount',
    enableColumnMove: true,
	plugins: expander,
    store: store,
    enableColumnMove: true,
    cm: cm,
    sm:sm,
    tbar: tbar,
    loadMask: true,
    bbar: bbar,
    margins: '0px 22px 0px 5px',
//    sm: new Ext.grid.RowSelectionModel({
//      singleSelect: true
//    }),
    listeners: {
      rowclick: function(grid, rowIndex, e) {
        showCmLocate(grid.getStore().getAt(rowIndex));
      },
      sortchange : function(grid,sortInfo){
		  CustomColumnModel.saveSortInfo('cmlist', sortInfo);
	  },
      columnresize: function(){
    	  CustomColumnModel.saveCustom('cmlist', cm.columns);
      }
    },
    viewConfig: {
      scrollOffset: 0
    }
  });
  new Ext.Viewport({
    layout: 'border',
    items: [{
        region: 'north',
        border: false,
        contentEl: 'query-container',
        height: 155
      },
      grid
    ]
  });

	expander.on("beforeexpand",function(expander,r,body,row){
		if(lastRow != null && lastRow !== row){
			expander.collapseRow(lastRow);
		}
		lastRow = row;
		var cmId = r.data.cmId;
		$.ajax({
          url : "/cmCpe/cpeDetail.tv?cmId=" + cmId,
          type: 'POST',
          async:false,
          cache : false,
          dataType:'json',
          success : function(cpeDetails) {
        	  if(cpeDetails == null ){
            		var tpl1 = new Ext.Template(
  	    	        		'No Data' );
  	    			expander.tpl = tpl1;
  	    			expander.tpl.compile();
            	}else{
        			var cmcHtml = '';
            		$.each(cpeDetails,function(i,cpeDetail){
        				cmcHtml = cmcHtml +
        					"<tr>" +
  	                    "<td align='center'>" + cpeDetail.topCmCpeIpAddress + "</td>" +
  	                    "<td align='center'>" + cpeDetail.topCmCpeMacAddressString + "</td>" +
  	                    "<td align='center'>" + cpeDetail.topCmCpeTypeString + "</td>" +
  	                    "<td align='center'>" +
  		                    "<a class='yellowLink' href='javascript:;' onclick='pingCpe(&quot;"+cpeDetail.topCmCpeIpAddress +"&quot;)' >Ping</a>" + " / " +
  	                    	"<a class='yellowLink' href='javascript:;' onclick='traceRouteCpe(&quot;"+cpeDetail.topCmCpeIpAddress+"&quot;)' >Traceroute</a>" +
  	                    "</td>" +
  	                    "</tr>";
        				
                  });
            		
            		var tpl1 = new Ext.Template(
  	        		'<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">@cm.cpeInfo@</span></p>' + 
  	        		"<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
  	                "<thead>" +
  	                "<tr>" +
  	                "<th align='center'>IP</th>" +
  	                "<th align='center'>MAC</th>" +
  	                "<th align='center'>@CMC.text.type@</th>" +
  	                "<th align='center'>@CM.operate@</th>" +
  	                "</tr>" +
  	                "</thead>" +
  	                "<tbody id='tbody-append-child'>" + cmcHtml + "</tbody></table></div>"  
                  );
              	
      	        expander.tpl = tpl1;
      			expander.tpl.compile();
        	 }
          }
      });  
	}); 
  //构建其他菜单
  var menuItem = [
	{text: '@ccm/CCMTS.CmList.clearOfflineCm@', id: 'clear', handler: clearThisCM},
    {text: 'Ping CM', id: 'pingCm', handler: pingCm}, 
    {text: 'Trace Route', id: 'traceRoute', handler: traceRoute}, 
    {text: 'MIB Browser', handler: onMibbleBrowserClick}, 
    {text: '@cmList.cmhistory@', handler: onCmHistoryClick}, 
    {text: '@cmList.cmAction@', id: 'cmAction', handler: showCmAction}, 
    {text: '@text.modifyCmImportInfo@', id: 'cmImport', handler: editCmImportInfo}, 
    {id: "cpeInfo", text: '@cm.cpeInfo@', menu: [
      {text: '@cm.viewCmCpe@', id: 'showCmCpeInfo', handler: showCmCpeInfo}, 
      {text: '@cmList.cpeAction@', id: 'cpeAction', handler: showCpeAction}
    ]}, 
    {text: '@cm.viewCmStaticIp@', id: 'showCmStaticIp', handler: showCmStaticIpInfo}, 
    {text: '@CM.upgrade@', id:'cmUpgrade', handler: showCmUpgrade , disabled: !operationDevicePower},
    '-', 
    {text: '@CM.cmOnOlt@', id: 'getCmListByOlt', handler: loadCmListByOltJson}, 
    {text: '@CM.cmOnPon@', id: 'getCmListByPon', handler: loadCmListByPonJson}, 
    {text: '@CM.cmOnCcmts@', id: 'getCmListByCmc', handler: loadCmListByCmcJson}, 
    {text: '@CM.cmOnUpChannel@', id: 'getCmListByUpPort', handler: loadCmListByUpPortJson}, 
    {text: '@CM.cmOnDownChannel@', id: 'getCmListByDownPort', handler: loadCmListByDownPortJson}
  ];
  entityMenu = new Ext.menu.Menu({
    id: 'cmMenu',
    enableScrolling: false,
    minWidth: 160,
    items: menuItem
  });
  //需要判断是否从CCMTS/CMTS的portal页跳转过来
  if (queryInitData.portal !== undefined) {
    if ($('#advance-toolbar-div').css("display") == "none") {
      //showAdvanceQuery();
      $('#simple-toolbar-div').hide();
      $('#advance-toolbar-div').show();
      setTimeout(function() {
        setAdvanceQueryValueByLink();
      }, 400);
    } else {
      setAdvanceQueryValueByLink();
    }
  } else {
	  simpleQuery();
  }

  if (!operationDevicePower) {
    $("#restartCm").attr("disabled", true);
  }
  
  if(!refreshDevicePower){
      $("#refreshCmInfo").attr("disabled", true);
  }
});

function disabledBtn(id, disabled){
	var t=Ext.getCmp(id);
	Ext.getCmp(id).setDisabled(disabled); 
}

function disabledToolbarBtn(num){ //num为选中的的行的个数;
	if(num > 0){ 
		if(operationDevicePower){ 
            disabledBtn("clearSingleCm", false);
        }
	}else{
		if(operationDevicePower){ 
            disabledBtn("clearSingleCm", true);
        }
	}
}

function showCm3Tip(tipTitle, datas){
	var $cm3Tip = $('#cm3Tip');
	$cm3Tip.empty();
	
	//创建内容
	var cm3Tpl = new Ext.XTemplate(
		'<div class="bubbleTip" id="cm3Tip">',
			'<div class="bubbleBody">',
				'<p class="pT5"><b class="gray555">{tipTitle}</b></p>',
			'</div>',
			'<div class="bubbleTipArr"></div>',
		'</div>'
	);
	
	cm3Tpl.overwrite('cm3Tip', {
		tipTitle: tipTitle,
		datas: datas
	});
	
	cm3Tpl.css({left : 300, top : 300});
}

/**
 * 从其他页面跳转过来设置高级查询条件
 */

function setAdvanceQueryValueByLink() {
  if(queryInitData.cmcDeviceStyle == undefined){
	  $('#select_deviceType').val(EntityType.getOltType());
	  deviceTypeSelChanged(function(){
		  if (!window.selector) {
		    createOltCombo();
		  }
		  window.selector.setValue(queryInitData.oltId);
		  $('#cmIpAddress').val("");
		  $('#cmMacAddress').val("");
		  $('#select_status').val(0);
		  oltSelChanged();
		  setTimeout(function(){
			  $("#select_pon").val(queryInitData.ponId);
			  ponSelChanged(function(){
				  //进行查询
				  queryCmList();
			  }); 
		  },300);
	  });
  }else{
	  if (EntityType.isCmtsType(queryInitData.cmcDeviceStyle)) {
		    $('#select_deviceType').val(EntityType.getCmtsType());
		    deviceTypeSelChanged(function(){
		    	$("#select_cmts").val(queryInitData.cmtsId);
		    	cmtsSelChanged(function(){
		    		if (queryInitData.channelType == 0) {
		    			$("#select_upCnl").val(queryInitData.channelIndex);
				    } else {
				        $("#select_downCnl").val(queryInitData.channelIndex);
				    }
		    		//进行查询
			        queryCmList();
		    	});
		    })
		  } else if (EntityType.isCcmtsWithAgentType(queryInitData.cmcDeviceStyle)) {
		    $('#select_deviceType').val(queryInitData.cmcDeviceStyle);
		    deviceTypeSelChanged(function(){
		    	//此时需要设置CCMTS下拉框、上行信道下拉框的数据
		        $("#select_ccmts").val(queryInitData.cmcId);
		        ccmtsSelChanged(function(){
		        	 if (queryInitData.channelType == 0) {
		                 $("#select_upCnl").val(queryInitData.channelId)
		               } else {
		                 $("#select_downCnl").val(queryInitData.channelId)
		               }
		               //进行查询
		               queryCmList();
		        });
		    });
		  } else if (EntityType.isCcmtsWithoutAgentType(queryInitData.cmcDeviceStyle)) {
		    $('#select_deviceType').val(EntityType.getOltType());
		    deviceTypeSelChanged(function(){
		    	//此时需要设置OLT、PON口、CCMTS下拉框、下行信道的数据
		        if (!window.selector) {
		          createOltCombo();
		        }
		        window.selector.setValue(queryInitData.entityId);
		        oltSelChanged(function(){
		        	$.ajax({
		        		url: '/eponcmlist/loadPonAttrByCmcId.tv',
		        		data:{cmcId: queryInitData.cmcId},
		        		type:"POST",
		        		dataType: 'json',
		        		success:function(json){
		        			queryInitData.ponId = parseInt(json.ponId);
		        			$("#select_pon").val(queryInitData.ponId);
		        			ponSelChanged(function(){
		        				$("#select_ccmts").val(queryInitData.cmcId);
		        				ccmtsSelChanged(function(){
		        					if (queryInitData.channelType == 0) {
		            					$("#select_upCnl").val(queryInitData.channelId)
		            				} else {
		            					$("#select_downCnl").val(queryInitData.channelId)
		            				}
		            				//进行查询
		            				queryCmList();
		        				});
		        			});
		        		},
		        		error: function(response) {},
		        	    cache: false,
		        	    complete: function(XHR, TS) {
		        	      XHR = null
		        	    }
		        	});
		        });
		    });
		  }
  }
}

/* ==========================================================
 * 这段内容是页面grid的renderer方法
 * ========================================================== */

function renderOperation(value, p, record) {
  var statusMacAddress = record.data.statusMacAddress,
    cmIp = record.data.statusInetAddress,
    cmId = record.data.cmId,
    status = record.data.docsIfCmtsCmStatusValueString,
    cmcId = record.data.cmcId,
    status = record.data.statusValue,
    returnStr = "";
  
  if(!refreshDevicePower){
      returnStr += "<span>@route.button.refresh@</span> / ";
  }else{
      returnStr += String.format(refreshCmFormatStr, statusMacAddress, cmcId) + " / ";
  }
  
  if (!isCmOnline(status)) {
    //离线状态的CM，只有刷新CM和其他操作
    returnStr += String.format(otherOperFormatStr, cmId);
    return returnStr;
  } else {
    //在线状态的CM
    if (operationDevicePower) {
      //如果有操作设备的权限
	  returnStr += String.format(restartCmFormatStr, statusMacAddress, cmIp, cmId, status) + " / ";
      returnStr += String.format(otherOperFormatStr, cmId);
      return returnStr;
    } else {
      //如果没有操作设备的权限
      returnStr += String.format(otherOperFormatStr, cmId);
      return returnStr;
    }
  }
}
/*function renderCpeNum(value, p, record) {
	//判断应该屏蔽哪些菜单
	var cmcDeviceStyle = record.data.cmcDeviceStyle;
	//如果是CMTS，则修改相关为CMTS并屏蔽CPE相关
	if (EntityType.isCmtsType(cmcDeviceStyle)) {
	    p.attr = 'ext:qtip="@CPE.CMTSNOTSUPPORT@"';  
		return "--"
	} else {
	    p.attr = 'ext:qtip="'+value+'"';  
		return value;
	}
}*/


function showMoreOperation(cmId, event) {
  var record = grid.getStore().getById(cmId),
    status = record.data.statusValue,
    x = event.clientX,
    y = event.clientY;
  grid.getSelectionModel().selectRecords([record]);
  //判断应该屏蔽哪些菜单
  //首先需要获取版本控制信息
  $.get('/cmlist/loadCmcVersion.tv', {
	  cmcId: record.data.cmcId
  }, function(json){
	  if(!json) return;
	  var cmcDeviceStyle = record.data.cmcDeviceStyle;
	  if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
		// 屏蔽所在OLT/PON下的CM选项
		Ext.getCmp("cmMenu").findById("getCmListByOlt").setDisabled(true);
		Ext.getCmp("cmMenu").findById("getCmListByPon").setDisabled(true);
		Ext.getCmp("cmMenu").findById("cpeInfo").setDisabled(false);
		Ext.getCmp("cmMenu").findById("showCmStaticIp").setDisabled(false);
		Ext.getCmp("cmMenu").findById("getCmListByCmc").setText("@CM.cmOnCcmts@");
	} else if (EntityType.isCmtsType(cmcDeviceStyle)) {
		Ext.getCmp("cmMenu").findById("getCmListByOlt").setDisabled(true);
		Ext.getCmp("cmMenu").findById("getCmListByPon").setDisabled(true);
		Ext.getCmp("cmMenu").findById("cpeInfo").setDisabled(true);
		Ext.getCmp("cmMenu").findById("showCmStaticIp").setDisabled(true);
		Ext.getCmp("cmMenu").findById("getCmListByCmc") .setText("@CM.cmOnCmts@");
		Ext.getCmp("cmMenu").findById("getCmListByOlt").setDisabled(true);
		Ext.getCmp("cmMenu").findById("getCmListByPon").setDisabled(true);
	} else {
		Ext.getCmp("cmMenu").findById("getCmListByOlt").setDisabled(false);
		Ext.getCmp("cmMenu").findById("getCmListByPon").setDisabled(false);
		Ext.getCmp("cmMenu").findById("cpeInfo").setDisabled(false);
		Ext.getCmp("cmMenu").findById("showCmStaticIp").setDisabled(false);
		Ext.getCmp("cmMenu").findById("getCmListByCmc").setText("@CM.cmOnCcmts@");
	}
  //版本控制
  if(!EntityType.isCmtsType(cmcDeviceStyle)){
	  if(json.supportStaticIp==false){
		  Ext.getCmp("cmMenu").findById("showCmStaticIp").hide();
	  }else{
		  Ext.getCmp("cmMenu").findById("showCmStaticIp").show();
	  }
	  
	  if(json.supportCpeInfo==false){
		  Ext.getCmp("cmMenu").findById("cpeInfo").hide();
	  }else{
		  Ext.getCmp("cmMenu").findById("cpeInfo").show();
	  }
	  if(json.supportCmUpgrade==false){
		  Ext.getCmp("cmMenu").findById("cmUpgrade").hide();
	  }else{
		  Ext.getCmp("cmMenu").findById("cmUpgrade").show();
	  }
	  if(json.supportClearSingleCm==false){
		  Ext.getCmp("cmMenu").findById("clear").hide();
	  }else{
		  Ext.getCmp("cmMenu").findById("clear").show();
	  }
  }
  
  if(!hasSupportEpon){
	  Ext.getCmp("cmMenu").findById("getCmListByOlt").hide();
	  Ext.getCmp("cmMenu").findById("getCmListByPon").hide();
  }else{
	  Ext.getCmp("cmMenu").findById("getCmListByOlt").show();
	  Ext.getCmp("cmMenu").findById("getCmListByPon").show();
  }
  
  
  entityMenu.showAt([x - 150, y]);
  });
}

/* ==========================================================
 * 以下内容是刷新CM信息的方法
 * ========================================================== */

function refreshCmInfo(a,b,c,d,e) {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    return;
  }
  window.top.showWaitingDlg('@COMMON.waiting@', '@CMC.ipqam.doingRefreshDevice@', 'ext-mb-waiting');
  var cmMac = record.data.statusMacAddress,
    cmcId = record.data.cmcId,
    cmId = record.data.cmId,
    statusIndex = record.data.statusIndex,
    cmIp = record.data.statusInetAddress,
    cmClassified = record.data.cmClassified,
    cmAlias = record.data.cmAlias,
    cmcDeviceStyle = record.data.cmcDeviceStyle,
    rowIndex = store.indexOf(record);
  $.ajax({
    url: '/cmlist/refreshCm.tv',
    data: {
      cmMac: cmMac,
      cmId: cmId,
      cmIp: cmIp,
      cmcId: cmcId,
      statusIndex: statusIndex,
      cmcDeviceStyle: cmcDeviceStyle
    },
    type: 'post',
    dataType: "json",
    success: function(json) {
      if (json != null) {
        if (json.success) {
          window.top.closeWaitingDlg();
          top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@CMC.tip.refreshSuccess@</b>'
          });
          //window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.refreshSuccess@');
          //更新该行数据
          var cmAttribute = json.cmAttribute;
          record.beginEdit();
          for (var key in cmAttribute) {
        	  //非版本控制内容则更新
        	  if($.inArray(key, ['supportStaticIp', 'supportCpeInfo', 'supportReset', 'supportCmUpgrade','supportClearSingleCm']) ===-1){
        		  cmAttribute[key] && record.set(key, cmAttribute[key]);
        	  }
          }

          record.commit();
          record.endEdit();
          //刷新定位图
          refreshCmLocation();
          //更新CM数量信息
          updateCmNum();
          var cls = expander.state[record.id] ? 'x-grid3-row-expanded' : 'x-grid3-row-collapsed';
          if (cls == 'x-grid3-row-expanded') {
              expander.expandRow(rowIndex);
          }
        } else {
          window.parent.showMessageDlg('@COMMON.tip@', '@CMC.tip.refreshFailure@');
        }
      }
    },
    error: function(response) {},
    cache: false,
    complete: function(XHR, TS) {
      XHR = null
    }
  });
}

/* ==========================================================
 * 以下内容是重启CM信息的方法
 * ========================================================== */

function restarCm() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    return;
  }
  var cmId = record.data.cmId,
    cmIp = record.data.statusInetAddress,
    status = "";
  if (isCmOnline(record.data.statusValue)) {
    status = "Online";
  }
  window.parent.showConfirmDlg('@COMMON.tip@', '@CM.confirmResetCm@', function(type) {
    if (type == 'no') {
      return;
    } else {
      window.top.showWaitingDlg('@COMMON.waiting@', '@cmList.resetting@', 'ext-mb-waiting');
      var refreshCmPromise = $.post('/cm/resetCm.tv',{cmId: cmId, cmIp: cmIp, status: status});
      refreshCmPromise.done(function(response){
    	  window.top.closeWaitingDlg();
          refreshCmLocation();
          updateCmNum();
          var msg = "";
          if (response.message == 'success') {
            msg = '@cmList.resetCmdDone@';
            //重启成功，则直接将CM状态置为下线
            var sm = grid.getSelectionModel(),
              record = sm.getSelected();
            if (record == null) {
              return;
            }
            record.beginEdit();
            record.set('statusValue', 1);
            record.commit();
            record.endEdit();
          } else {
            if (response.reason == "SnmpException") {
              msg = '@CMC.tip.snmpUnreachable@';
            } else if (response.reason == "PingException") {
              msg = '@CMC.tip.icmpUnreachable@';
            } else if (response.reason == "offline") {
              msg = '@CMC.tip.cmOfflineTip@';
            }
          }
          top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">' + msg + '</b>'
          });
      });
    }
  });
}

function clearThisCM(){
	var sm = grid.getSelectionModel();
    if (sm == null || !sm.hasSelection()) {
    	return window.parent.showMessageDlg("@COMMON.tip@", "@text.pleaseSelectCm@");
    }
    //获取选中的cmId
    var selections = sm.getSelections(), 
    	cmIds = [];
    for (var i = 0, len=selections.length; i < len; i++) {
    	cmIds.push(selections[i].data.cmId);
    }
	window.parent.showConfirmDlg("@COMMON.tip@", "@CCMTS.clearSomeOfflineCM.confirmToClear@",function(button,text){
        if(button == "yes"){
            window.top.showWaitingDlg("@COMMON.waiting@", "@CMC.tip.clearCmIng@", 'ext-mb-waiting');
            $.post("/cmlist/clearSingleCM.tv?cmIds=" + cmIds, function(data){
            	window.top.closeWaitingDlg();
            	if(data.success){
            		var successNum=data.clearSuccess;
            		var failedNum=selections.length-successNum;
                    top.afterSaveOrDelete({
                        title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@ccm/CCMTS.CmList.clearSingleCmCmComplete@<br/>@CMC.tip.successfully@:' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@:' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
                    });
                    store.reload();
                    sm.clearSelections();
                }else{
                    window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.clearOfflineCM.fail@");
                }
            });
        }
    });
}

function restartAllCm() {
  window.parent.showConfirmDlg('@COMMON.tip@', '@CMC.tip.confirmResetAllCm@', function(button, text) {
    if (button == "yes") {
    	//获取当前页的所有在线CM的行号
        var onlineCmIndex = [],	//批量操作在线CM数组
        	finishedCmIndex = [],	//完成重启操作的CM数组
        	curCm, 
        	successNum = 0, 	//重启成功CM数
        	failedNum = 0, 	//重启失败CM数
        	dwrId = 'cmList_restartAllCm', 
        	cmIds = [],
        	refreshedCmCounter=0;
        for (var i = 0; i < store.getCount(); i++) {
        	curCm = store.getAt(i).data;
        	if (isCmOnline(curCm.statusValue)) {
        		onlineCmIndex.push(i);
        	}
        }
        if (onlineCmIndex.length === 0) {
          window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.noCm@');
          return;
        }
        //DWR推送处理方法
        window.top.addCallback("refreshAllOnLineCm", function(json) {
        	//重置计时器
        	clearTimeout(timeoutTimer);
        	timeoutTimer = setTimeout(function(){
            	unexpectedEnd();
            }, 60000);
        	//更新对应行
        	var cmId = json.cmId;
        	for (var j = 0; j < store.getCount(); j++) {
        		if (store.getAt(j).get('cmId') == cmId) {
        			finishedCmIndex.push(j);
        			if (json.success == true) {
        				//重启成功后，将在线状态置为下线
        				grid.getView().removeRowClass(j, 'yellow-row');
        				successNum++;
        				var record = store.getAt(j);
        				record.beginEdit();
        				record.set('statusValue', 1);
        				record.commit();
        				record.endEdit();
        				$('#suc-num-dd').text(successNum);
        			} else {
        				grid.getView().addRowClass(j, 'red-row');
        				failedNum++;
        				$('#fail-num-dd').text(failedNum);
        			}
        		}
        	}
        	refreshedCmCounter++;
        	updateCmNum();
        	//如果全部刷新完成，则表示正常结束
        	if (refreshedCmCounter >= onlineCmIndex.length) {
        		//取消dwr推送接收
            	window.top.removeCallback("refreshAllOnLineCm", dwrId);
            	clearTimeout(timeoutTimer);
        		outputResult();
        	}
    	}, dwrId);
        //初始化
        initBatch(onlineCmIndex);
        //发送查询请求
        for (i = 0; i < onlineCmIndex.length; i++) {
            cmIds.push(store.getAt(onlineCmIndex[i]).data.cmId);
        }
        $.post('/cmlist/resetOnLineCm.tv',{
	        cmIds: cmIds.join(','),
	        dwrId: dwrId
        });
        //发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
        timeoutTimer = setTimeout(function(){
        	unexpectedEnd();
        }, 60000);
    }
  
    //批量初始化
    function initBatch(indexArray){
  	  //初始化统计数据
  	  $('#suc-num-dd').text(0);
  	  $('#fail-num-dd').text(0);
  	  //将各行标记为正在刷新
  	  for (i = 0; i < indexArray.length; i++) {
  		  grid.getView().addRowClass(indexArray[i], 'yellow-row');
  	  }
  	  //禁用查询功能、刷新功能、翻页功能
  	  tbar.setDisabled(true);
  	  bbar.setDisabled(true);
  	  $('#simple-query').attr('disabled', 'disabled');
  	  $('#advance-query').attr('disabled', 'disabled');
  	  //展示等待框
  	  showLoading("@ccm/CCMTS.CmList.restartingCm@");
    }
    
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("refreshAllOnLineCm", dwrId);
    	var curCmIndex;
    	//将正在处理行全部置为失败
    	for(var i=0, len=onlineCmIndex.length; i<len; i++){
    		curCmIndex = onlineCmIndex[i];
    		if(!contains(finishedCmIndex, curCmIndex)){
    			grid.getView().addRowClass(i, 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	tbar.setDisabled(false);
    	bbar.setDisabled(false);
    	$('#simple-query').removeAttr('disabled');
    	$('#advance-query').removeAttr('disabled');
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@ccm/CCMTS.CmList.resetCmComplete@<br/>@CMC.tip.successfully@' + successNum + '@CCMTS.view.cmNumOffline.unit@,@CMC.label.failure@' + failedNum + '@CCMTS.view.cmNumOffline.unit@</b>'
    	});
    }
    
    //判断数组中是否包含指定元素
    function contains(array, obj){
		var i = array.length;
		while(i--){
			if(array[i] == obj){
				return true;
			}
		}
		return false;
	}
  
  });
}

function pingCpe(ip) {
	window.parent.createDialog("modalDlg", 'Ping' + " - " + ip, 600, 400,
	  "entity/runCmd.tv?cmd=ping&ip=" + ip, null, true, true);
}
//YangYi Add @ 2013-11-15 Trace Route

function traceRouteCpe(ip) {
    window.parent.createDialog("modalDlg", 'Tracert ' + ip, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + ip, null, true, true);
}

/* ==========================================================
 * 以下内容是其他菜单中的的方法(CM上下线行为、CPE信息、查看大客户IP信息、所在OLT/PON/CCMTS/上行/下行下CM信息等)
 * ========================================================== */
//YangYi Add @ 2013-11-15 Ping CM

function pingCm() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmIp = record.data.displayIp;
    cmcId = record.data.cmcId;
    cmcdevicestyle = record.data.cmcDeviceStyle;
    if (cmIp) {
	  //if(cmPingMode == 2 && EntityType.isCcmtsWithAgentType(cmcdevicestyle)) {
      if(cmPingMode == 2) {
          window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400, 
        	      "entity/snmpPing.tv?cmd=snmpping&entityId="+cmcId+"&ip=" + cmIp, null, true, true);
      }
	  else{
		  window.parent.createDialog("modalDlg", 'Ping' + " - " + cmIp, 600, 400,
		          "entity/runCmd.tv?cmd=ping&ip=" + cmIp, null, true, true);
	  }
    }
}
//YangYi Add @ 2013-11-15 Trace Route

function traceRoute() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmIp = record.data.displayIp;
  if (cmIp) {
    window.parent.createDialog("modalDlg", 'Tracert ' + cmIp, 600, 400,
      "entity/runCmd.tv?cmd=tracert&ip=" + cmIp, null, true, true);
  }
}
//YangYi Add @ 2013-11-15 MibbleBrowser

function onMibbleBrowserClick() {
  var sm = grid.getSelectionModel(),
      record = sm.getSelected(),
      cmIp = record.data.displayIp,
      mibbleType = 35000,
  	  enterCount = 1;
  
  /*if (cmIp) {
    window.top.addView("cmMibbleBrowser", "CM MIB Browser", null, "/mibble/showMibbleBrowser.tv?host=" + cmIp + "&mibbleType=" + mibbleType, null, true);
  } else {
    window.top.addView("cmMibbleBrowser", "CM MIB Browser", null, "/mibble/showMibbleBrowser.tv?mibbleType=" + mibbleType, null, true);
  }*/
  window.top.addView('mibbleBrowser', 'MIB Browser','entityTabIcon', '/mibble/showMibbleBrowser.tv?host=' + cmIp + '&mibbleType=' + mibbleType + '&enterCount=' + enterCount,null, true);
}

function onCmHistoryClick(){
	 var sm = grid.getSelectionModel(),
	     record = sm.getSelected(),
	     cmId = record.data.cmId,
	     cmIp = record.data.displayIp,
	     cmMac = record.data.statusMacAddress;
	 window.parent.addView("showCmSignalHistory", '@cmList.cmhistory@', "icoG7", "/cmHistory/showCmHistory.tv?cmId="+cmId+"&cmIp="+cmIp+"&cmMac="+cmMac,null,true);
}

function showCmAction() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
  window.parent.createDialog("cmAction", '@cmList.cmAction@', 600, 370, "/cm/showCmActionInfo.tv?cmId=" + cmId, null, true, true);
}

function editCmImportInfo() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmMacAddress = record.data.statusMacAddress;
  window.top.createDialog('modifyCmProperty', '@text.modifyCmImportInfo@', 600, 370,
    'cm/showCmInfoConfig.tv?cmMac=' + cmMacAddress, null, true, true);
}

function showCpeAction() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
  window.parent.createDialog("cpeAction", '@cmList.cpeAction@', 600, 370, "/cm/showCpeActionInfo.tv?cmId=" + cmId, null, true, true);
}

function showCmCpeInfo() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
  window.parent.createDialog("cmCpeInfo", '@cm.cpeInfo@', 600, 370, "/cmlist/showCmCpeInfo.tv?cmId=" + cmId, null, true, true);
}

function showCmStaticIpInfo() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
  window.parent.createDialog("cmStaticIpInfo", '@cm.cmStaticIp@', 600, 370, "/cm/showCmStaticIpInfo.tv?cmId=" + cmId, null, true, true);
}

function showCmUpgrade(){
  var sm = grid.getSelectionModel(),
    record = sm.getSelected(),
    cmId = record.data.cmId;
   window.parent.createDialog("cmUpgrade", '@CM.upgrade@', 600, 370, "/cmupgrade/showUpgradeSingleCm.tv?entityId=" + cmId, null, true, true);
}

//所在OLT下CM

function loadCmListByOltJson() { 
  //获取当前所选CM的数据，组装成对应的查询条件
  //设备类型=10000，entityId即可
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  if ($('#advance-toolbar-div').css("display") == "none") {
    showAdvanceQuery();
  }
  //修改查询条件的值
  $('#select_deviceType').val(EntityType.getOltType()).trigger("onchange");
  if (!window.selector) {
    createOltCombo();
  }
  window.selector.setValue(record.data.oltId);
  oltSelChanged();
  $('#cmIpAddress').val("");
  $('#cmMacAddress').val("");
  $('#select_status').val(0);
  //进行查询
  queryCmList();
}

//所在PON口下CM

function loadCmListByPonJson() {
  //获取当前所选CM的数据，组装成对应的查询条件
  //设备类型=10000，entityId=oltId, ponId=ponId
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  if ($('#advance-toolbar-div').css("display") == "none") {
    showAdvanceQuery();
  }
  //修改查询条件的值
  $('#select_deviceType').val(EntityType.getOltType()).trigger("onchange");
  if (!window.selector) {
    createOltCombo();
  }
  window.selector.setValue(record.data.oltId);
  $('#cmIpAddress').val("");
  $('#cmMacAddress').val("");
  $('#select_status').val(0);
  oltSelChanged(function() {
    $("#select_pon").val(record.data.ponId);
    $("#select_pon").trigger("onchange");
    //进行查询
    queryCmList();
  });
}

//所在CCMTS下CM

function loadCmListByCmcJson() {
  //获取当前所选CM的数据，组装成对应的查询条件，要分8800A/8800B/CMTS三种情况进行判断
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  if ($('#advance-toolbar-div').css("display") == "none") {
    showAdvanceQuery();
  }
  $('#cmIpAddress').val("");
  $('#cmMacAddress').val("");
  $('#select_status').val(0);
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getOltType());
    deviceTypeSelChanged(function() {
      //此时需要设置OLT、PON口、CCMTS下拉框的数据
      if (!window.selector) {
        createOltCombo();
      }
      window.selector.setValue(record.data.oltId);
      oltSelChanged(function() {
        $("#select_pon").val(record.data.ponId);
        ponSelChanged(function() {
          $("#select_ccmts").val(record.data.cmcId);
          ccmtsSelChanged(function() {
            //进行查询
            queryCmList();
          });
        });
      });
    });
  } else if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(cmcDeviceStyle);
    deviceTypeSelChanged(function() {
      $("#select_ccmts").val(record.data.cmcId);
      ccmtsSelChanged(function() {
        //进行查询
        queryCmList();
      });
    });
  } else if (EntityType.isCmtsType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getCmtsType());
    deviceTypeSelChanged(function() {
      //此时需要设置CMTS下拉框的数据
      $("#select_cmts").val(record.data.cmcId);
      cmtsSelChanged(function() {
        //进行查询
        queryCmList();
      })
    });
  }
}

//所在上行信道下CM

function loadCmListByUpPortJson() {
  //获取当前所选CM的数据，组装成对应的查询条件，要分8800A/8800B/CMTS三种情况进行判断
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  if ($('#advance-toolbar-div').css("display") == "none") {
    showAdvanceQuery();
  }
  $('#cmIpAddress').val("");
  $('#cmMacAddress').val("");
  $('#select_status').val(0);
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getOltType());
    deviceTypeSelChanged(function() {
      //此时需要设置OLT、PON口、CCMTS下拉框、上行信道的数据
      if (!window.selector) {
        createOltCombo();
      }
      window.selector.setValue(record.data.oltId);
      oltSelChanged(function() {
        $("#select_pon").val(record.data.ponId);
        ponSelChanged(function() {
          $("#select_ccmts").val(record.data.cmcId);
          ccmtsSelChanged(function() {
            $("#select_upCnl").val(record.data.upChannelId)
            //进行查询
            queryCmList();
          })
        });
      });
    });
  } else if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(cmcDeviceStyle);
    deviceTypeSelChanged(function() {
      //此时需要设置CCMTS下拉框、上行信道下拉框的数据
      $("#select_ccmts").val(record.data.cmcId);
      ccmtsSelChanged(function() {
        $("#select_upCnl").val(record.data.upChannelId)
        //进行查询
        queryCmList();
      });
    });
  } else if (EntityType.isCmtsType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getCmtsType());
    deviceTypeSelChanged(function() {
      //此时需要设置CMTS下拉框的数据
      $("#select_cmts").val(record.data.cmcId);
      cmtsSelChanged(function() {
        $("#select_upCnl").val(record.data.statusUpChannelIfIndex);
        //进行查询
        queryCmList();
      });
    });
  }
}

//所在下行信道下CM

function loadCmListByDownPortJson() {
  //获取当前所选CM的数据，组装成对应的查询条件，要分8800A/8800B/CMTS三种情况进行判断
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  if ($('#advance-toolbar-div').css("display") == "none") {
    showAdvanceQuery();
  }
  $('#cmIpAddress').val("");
  $('#cmMacAddress').val("");
  $('#select_status').val(0);
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getOltType());
    deviceTypeSelChanged(function() {
      //此时需要设置OLT、PON口、CCMTS下拉框、下行信道的数据
      if (!window.selector) {
        createOltCombo();
      }
      window.selector.setValue(record.data.oltId);
      oltSelChanged(function() {
        $("#select_pon").val(record.data.ponId);
        ponSelChanged(function() {
          $("#select_ccmts").val(record.data.cmcId);
          ccmtsSelChanged(function() {
            $("#select_downCnl").val(record.data.downChannelId);
            //进行查询
            queryCmList();
          });
        });
      });
    });
  } else if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
    $('#select_deviceType').val(cmcDeviceStyle);
    deviceTypeSelChanged(function() {
      //此时需要设置CCMTS下拉框、下行信道下拉框的数据
      $("#select_ccmts").val(record.data.cmcId);
      ccmtsSelChanged(function() {
        $("#select_downCnl").val(record.data.downChannelId);
        //进行查询
        queryCmList();
      })
    });
  } else if (EntityType.isCmtsType(cmcDeviceStyle)) {
    $('#select_deviceType').val(EntityType.getCmtsType());
    deviceTypeSelChanged(function() {
      //此时需要设置CMTS下拉框的数据
      $("#select_cmts").val(record.data.cmcId);
      cmtsSelChanged(function() {
        $("#select_downCnl").val(record.data.statusDownChannelIfIndex)
        //进行查询
        queryCmList();
      });
    });
  }
}

/* ==========================================================
 * 以下内容是刷新信号质量的方法
 * ========================================================== */

function refreshCmSignal() {
	var random = randomInteger(0, 100);
	refreshCmSignalQuality('cmlist_refreshCmSignal_' + random, store, true)
}

/* ==========================================================
 * 以下内容是CM定位的方法
 * ========================================================== */
//告警ID及其name对应关系
var alarms = {
  "2": '@performance/Performance.alert2@',
  "3": '@performance/Performance.alert3@',
  "4": '@performance/Performance.alert4@',
  "5": '@performance/Performance.alert5@',
  "6": '@performance/Performance.alert6@'
};
var oltNameFormatStr = '<a href="javascript:;" onclick="showOltSnap({2},\'{0}\')" class="yellowLink" title="{0}">{1}({0})</a>',
  cmcNameWithIpFormatStr = '<a href="javascript:;" onclick="showCmcSnap({2},\'{0}\', {3})" class="yellowLink" title="{0}">{1}({0})</a>',
  cmcNameWithoutIpFormatStr = '<a class="yellowLink" href="javascript:;" onclick="showCmcSnap({2},\'{0}\', {3})" title="{0}">{0}</a>',
  cmNameWithIpFormatStr = '{1}({0})',
  alarmFormatStr = '<img class="nm3kTip" nm3kTip="{0}" src="/images/fault/level{1}.png" border="0" />',
  statusFormatStr = '<img class="nm3kTip" nm3kTip="{0}" src="{1}" border="0" />';
//黄色：orangeBg， 红色：redBg

function showCmLocate(record) {
  var cmId = record.data.cmId,
    cmcDeviceStyle = record.data.cmcDeviceStyle;
  if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)) {
    $(".olt-sidebar").show();
    $('#ccTitle').text("CMTS");
    $('#spectrum-li').hide();
  } else if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
    $(".olt-sidebar").hide();
    $('#ccTitle').text("CMTS");
    $('#spectrum-li').show();
  } else if (EntityType.isCmtsType(cmcDeviceStyle)) {
    $(".olt-sidebar").hide();
    $('#ccTitle').text("CMTS");
    $('#spectrum-li').hide();
  }
  $.ajax({
    url: '/cmlist/loadCmLocationInfo.tv?cmId=' + cmId + '&cmcDeviceStyle=' + cmcDeviceStyle,
    type: 'POST',
    dateType: 'json',
    success: function(json) {
      if (json.cmTopos != null && json.cmTopos.length > 0) {
        $('#folderName-b').text(json.cmTopos[0].folderName);
      } else {
        $('#folderName-b').text('');
      }
      //填充OLT框信息
      var oltLocation = json.oltLocation;
      if (oltLocation != undefined && oltLocation != null) {
        var oltId = record.data.oltId;
        $('#oltLocation-name').text("").append(String.format(oltNameFormatStr, oltLocation.name, oltLocation.ip, oltId));
        convertAlarm('oltLocation', oltLocation.maxAlarmLevel);
        convertStatus('oltLocation', oltLocation.state);
        convertLocationValue('oltLocation', oltLocation);
      }
      //填充OLT-CC连线信息
      var oltCcmtsRela = json.oltCcmtsRela;
      if (oltCcmtsRela != undefined && oltCcmtsRela != null) {
        convertLinkValue('oltCcmtsRela', oltCcmtsRela);
      }
      //填充CC框信息
      var ccmtsLocation = json.ccmtsLocation;
      if (ccmtsLocation != undefined && ccmtsLocation != null) {
        if (ccmtsLocation.ip != undefined && ccmtsLocation.ip != null && ccmtsLocation.ip != "") {
          $('#ccmtsLocation-name').text("").append(String.format(cmcNameWithIpFormatStr, ccmtsLocation.name, ccmtsLocation.ip, record.data.cmcId, record.data.cmcDeviceStyle));
        } else {
          $('#ccmtsLocation-name').text("").append(String.format(cmcNameWithoutIpFormatStr, ccmtsLocation.name, ccmtsLocation.ip, record.data.cmcId, record.data.cmcDeviceStyle));
        }
        convertAlarm('ccmtsLocation', ccmtsLocation.maxAlarmLevel);
        convertStatus('ccmtsLocation', ccmtsLocation.state, cmcDeviceStyle);
        convertLocationValue('ccmtsLocation', ccmtsLocation);
        $('#cmc-cm-num').text(ccmtsLocation.cmNumOnline + "/" + ccmtsLocation.cmNumTotal);
      }
      //填充CC-CM连线信息
      var ccmtsCmRela = json.ccmtsCmRela;
      if (ccmtsCmRela != undefined && ccmtsCmRela != null) {
        convertLinkValue('ccmtsCmRela', ccmtsCmRela);
      }
      //展示CM框信息
      var cmLocation = json.cmLocation;
      if (cmLocation != undefined && cmLocation != null) {
        $('#cmName-dd').text(cmLocation.ip);
        convertAlarm('cmLocation', cmLocation.maxAlarmLevel);
        convertStatus('cmLocation', cmLocation.state);
        convertLocationValue('cmLocation', cmLocation);
        $('#cpe-num').text(cmLocation.cpeNum);
      }
    },
    error: function() {

    },
    cache: false
  });
}

function convertAlarm(location, alarmLevel) {
  if (alarmLevel != 0) {
    $('#' + location + '-maxAlarmLevel').text("").append(String.format(alarmFormatStr, alarms[alarmLevel], alarmLevel));
  } else {
    $('#' + location + '-maxAlarmLevel').text("");
  }
}

function convertStatus(location, state, cmcDeviceStyle) {
  if (location == "oltLocation") {
    if (state == 1) {
      $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CMC.label.online@", "/images/yes16.png"));
    } else {
      $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CCMTS.status.2@", "/images/no16.png"));
    }
  } else if (location == "ccmtsLocation") {
    if (EntityType.isCmtsType(cmcDeviceStyle)) {
      if (state == 1) {
        $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CMC.label.online@", "/images/yes16.png"));
      } else {
        $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CCMTS.status.2@", "/images/no16.png"));
      }
    } else {
      if (state == 4) {
        $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CMC.label.online@", "/images/yes16.png"));
      } else {
        $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CCMTS.status.2@", "/images/no16.png"));
      }
    }
  } else if (location == "cmLocation") {
    if (isCmOnline(state)) {
      $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CMC.label.online@", "/images/yes16.png"));
    } else {
      $('#' + location + '-state').text("").append(String.format(statusFormatStr, "@CCMTS.status.2@", "/images/no16.png"));
    }
  }
}

function convertLinkValue(location, relaObj) {
  for (var key in relaObj) {
    if (relaObj[key] != "") {
      $('#' + location + '-' + key).text(relaObj[key]);
    } else {
      $('#' + location + '-' + key).text('--');
    }
  }
}

function convertLocationValue(location, locationObj) {
  for (var key in locationObj) {
    if ($('#' + location + '-' + key).hasClass('perf')) {
      if (locationObj[key] !== null && locationObj[key] !== undefined) {
        $('#' + location + '-' + key).text(locationObj[key]);
      } else {
        $('#' + location + '-' + key).text("@ccm/CCMTS.CmList.failedGetData@");
      }
    }
  }
}

function showOltSnap(entityId, entityName) {
  window.parent.addView('entity-' + entityId, entityName, 'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + entityId);
}

function showCmcSnap(cmcId, entityName, cmcDeviceStyle) {
  if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
    window.parent.addView('entity-' + cmcId, entityName, 'entityTabIcon',
      '/cmc/showCmcPortal.tv?cmcId=' + cmcId + "&productType=" + cmcDeviceStyle);
  } else if (EntityType.isCcmtsWithoutAgentType(cmcDeviceStyle)){
    window.parent.addView('entity-' + cmcId, entityName, 'entityTabIcon',
      '/cmc/showCmcPortal.tv?cmcId=' + cmcId);
  }else{
	  window.parent.addView('entity-' + cmcId, entityName, 'entityTabIcon', '/cmts/showCmtsPortal.tv?cmcId=' + cmcId)
  }
}

function showCcmtsSpectrum() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    return;
  }
  var cmcDeviceStyle = record.data.cmcDeviceStyle;
  var cmcId = record.data.cmcId,
    cmcName = record.data.cmcName;
  window.top.addView('entity-' + cmcId, cmcName, 'entityTabIcon',
    '/cmcSpectrum/showCmcCurSpectrum.tv?module=16&cmcId=' + cmcId + '&productType=' + cmcDeviceStyle, null, true);
}

function showCmDetail(macAddr) {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    return;
  }
  var cmMac = macAddr || record.data.statusMacAddress;
  window.parent.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "icoG13", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + cmMac, null, true);
}

function refreshCmLocation() {
  var sm = grid.getSelectionModel(),
    record = sm.getSelected();
  if (record == null) {
    window.parent.showMessageDlg('@COMMON.tip@', '@ccm/CCMTS.CmList.selectCm@');
    return;
  }
  showCmLocate(record)
} 

function buildEntityTypeSelect(){
	var deviceTypePosition = Zeta$('select_deviceType');
	for(var i = 0; i < entityTypes.length; i++){
        var option = document.createElement('option');
        option.value = entityTypes[i].typeId;
        option.text = entityTypes[i].displayName;
        try {
        	deviceTypePosition.add(option, null);
        } catch(ex) {
        	deviceTypePosition.add(option);
        }
  }
}
