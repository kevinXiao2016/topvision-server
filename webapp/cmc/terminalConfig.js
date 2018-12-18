var switchName = 'GlobalCmList', //GlobalCmList(使用全网CM轮询) SpecifiedCmList(使用管理列表轮询) CmPollClosed(关闭轮询)
    aSwitchName = ['GlobalCmList','SpecifiedCmList','CmPollClosed'],
	exportBtn, 
    segmentBtn, 
    bCallback = false; 

$(function(){
	if(window.cmPollManager){
		//先读取下拉框的value，设置界面显示隐藏;
		readSwitchName(initSel);
		loadSpecifiedCmListCount();
		createExportBtn();
	}
});//end document.ready;
//先读取状态,设置正确的下拉框和布局;
function readSwitchName(callback){
	$.ajax({
        url: '/cmpoll/loadSwitchName.tv',
        type: 'POST',
        dateType: 'json',
        success: function(json) {
        	json = Ext.decode(json);
        	if(json && json.switchName && $.inArray(json.switchName, window.aSwitchName) != -1){
        		window.switchName = json.switchName;
        	}
        },
        error: function() {
        	top.showMessageDlg('@COMMON.tip@', '@cmPoll.loadFail@');
        },
        complete: function(){
        	callback();
        },
        cache: false
    });
}
//下拉框改变状态，界面变化,设置正确的tooltip;
function changeCmPollSel(){
	var $cmPollConfigSel = $('#cmPollConfigSel'),
	    value = $cmPollConfigSel.val(),
	    tip1 = '@cmc/cmPoll.pollIntervalTip@',
    	tip2 = '@cmc/cmPoll.pollIntervalTip2@',
	    $cmPollInterval = $('#cmPollInterval'),
        $tbody = $('#cmPollConfigTbody'),
        $jsManageList = $('.jsManageList');

	switch(value){
		case aSwitchName[0]:
			$tbody.css({display: ''});
			$jsManageList.css({visibility: 'hidden'});
			$cmPollInterval.attr({toolTip: tip1});
			break;
		case aSwitchName[1]:
			$tbody.css({display: ''});
			$jsManageList.css({visibility: 'visible'});
			$cmPollInterval.attr({toolTip: tip2});
			break;
		case aSwitchName[2]:
			$tbody.css({display: 'none'});
			break;
	}
}
//初始化下拉框的值，界面显示隐藏;
function initSel(){
	var $cmPollConfigSel = $('#cmPollConfigSel');
	$cmPollConfigSel.val(window.switchName);
	changeCmPollSel();
}
//导出为Excel按钮;
function createExportBtn(){
	window.exportBtn = new Ext.SplitButton({
		renderTo : "putExportBtn",
		text : "@network/BUTTON.export@",
		handler : function(){this.showMenu()},
		iconCls : "bmenu_exportWithSubMin",
		menu : new Ext.menu.Menu({
			items : [{text : "EXCEL",   handler : exportExcel}]
		})
	});
}
//导出;
function exportExcel(){
	top.showWaitingDlg('@sys.waiting@', '@cmPoll.importing@');
	$.ajax({
	    url: '/cmpoll/generateSpecifiedCmListFile.tv',
        type: 'post',
        success: function(response) {
    	    top.closeWaitingDlg();
    	  	  if(response.msg){
    	  		  top.showMessageDlg("@COMMON.tip@", "@cmPoll.importFail@"+ response.msg);
    	  	  } else if( response.data) {
    	  		  document.getElementById('download_iframe').src = "/cmpoll/exportSpecifiedCmListFile.tv?fileName="+encodeURIComponent(response.data);
    	  	  }
		}, error: function(response) {
		    top.closeWaitingDlg();
		    top.showMessageDlg("@COMMON.tip@", "@cmPoll.importFail2@");
		}, cache: false
	});
}
//加载当前导入了多少个;
function loadSpecifiedCmListCount(){
	$.ajax({
	    url: '/cmpoll/loadSpecifiedCmListCount.tv',
        type: 'post',
        success: function(json) {
        	json = Ext.decode(json);
        	if(json && json.rowcount){
        		updateRowCount(json.rowcount);
        	}
		}, error: function(response) {
		}, cache: false
	});
}
//更新当前导入了多少个;
function updateRowCount(num){
	$("#cmRollCount").find('b').text(num);
}
//点击导入按钮，打开导入界面;
function openImportPage(){
	top.createDialog("modalDlg", "@cmPoll.importManageList@", 800, 500, "/cmpoll/showSpecifiedCmListImport.tv", null, true, true);
}
//点击保存按钮;
function saveCmPollConfigClick(){
	var $cmPollInterval = $('#cmPollInterval'),
	    pollInterval = $cmPollInterval.val(), 
	    pollValue = parseInt(pollInterval, 10),
	    reg = /^[0-9]{1,5}$/,
	    minNum = 30;
	
	switch($('#cmPollConfigSel').val()){
		case aSwitchName[0]:
			minNum = 30;
			break;
		case aSwitchName[1]:
			minNum = 1;
			break;
	}
	//验证 1-10080 或 30-10080;
	if(!reg.test(pollInterval) ||  pollValue < minNum || pollValue > 10080){
		$cmPollInterval.focus();
		return;
	}
	var cmPollInterval = pollValue * 60;
	var obj = {
		switchName : $('#cmPollConfigSel').val()
	}
	if($('#cmPollConfigSel').val() != aSwitchName[2]){
		obj.cmPollInterval = pollValue * 60
	}
	window.top.showWaitingDlg("@COMMON.waiting@", "@cmc/text.modifyConfig@", 'ext-mb-waiting',null,false);
	$.ajax({
        url: '/cmpoll/changeSwitchForCmPoll.tv',
        data : obj, 
        type: 'POST',
        dateType: 'json',
        success: function(json) {
            top.afterSaveOrDelete({
                title: '@COMMON.tip@',
                html: '@cmPoll.saveSuccess@'
            });
        },
        error: function() {
        	top.showMessageDlg('@COMMON.tip@', '@cmPoll.tabFail@');
        },
        cache: false
    });
}