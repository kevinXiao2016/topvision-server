var imgBtn;
$(function(){
	imgBtn = new Nm3kSwitch("putSwitch", pppoEPlusEnable,{
		yesNoValue: [1, 2],
		afterChangeCallback: function(selectValue){
			setPppoeSwitch(selectValue);
		}
	});
	imgBtn.init();
	if(!operationDevicePower) {
		imgBtn.setDisabled(true);
	}
	switchPPPoE(imgBtn.getValue());
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [
	     	{
	            region: 'north',
		        height: 80,
		        contentEl: 'topPart',
		        border: false
	     	},{
	     	    region: 'center',
	     		contentEl: 'mainPart',
	     		cls: 'clear-x-panel-body',
	     		border: false,
	     		margins: '0 0 0 0'
	     	}
	     ],
	     listeners: {
	    	 afterRender: function(){
	    		 autoHeight();
	    		 initTab();
	    	 }
	     }
	});
	$(window).resize(function(){
		//autoHeight();
		throttle(autoHeight,window);
	});//end resize;
});
function initTab(){
	var data = [{
	    name: '@oltdhcp.pppoeConfig@',
	    value: '/epon/oltdhcp/showOltDhcpPppoeCfg.tv?entityId='+entityId
	},{
		name: '@oltdhcp.portAttribute@',
		value: '/epon/oltdhcp/showPortAttributeList.tv?entityId='+entityId+"&&portProtIndex=2"
	},{
		name: '@oltdhcp.statistics@',
		value: '/epon/oltdhcp/showOltPppoeStatistics.tv?entityId='+entityId
	}];
	var tab = new SegmentButton('putTabDiv', data, {
	    callback: function(value){
	    	changeFramePage(value);
	    }
	});
	tab.init();
}
//显示下半部分页面;
function changeFramePage(src){
	window.frames["pageFrame"].src = src;
	window.frames["pageFrame"].location.href = src;
}
//自动调整;
function autoHeight(){
	var $body = $('body'),
	    $pageFrame = $('#pageFrame'),
	    w = $body.width(),
	    h = $body.height() - 86;
	
	if(w < 100){ w = 100}
	if(h < 100){ h = 100}
	$pageFrame.width(w).height(h);
}
//resize事件增加 函数节流;
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}

function switchPPPoE(selectValue){
	switch(selectValue){
	case 1:
		hideBottomMask();
		break;
	case 2:
		showBottomMask();
		break;
	}
}
//隐藏底部遮罩;
function hideBottomMask(){
	var $mask = $('#bottomMask');
	$mask.fadeOut();
}
//显示底部遮罩;
function showBottomMask(){
	var $mask = $('#bottomMask');
	if($mask.length == 0){ 
		//不存在，创建;
		$('body').append('<div id="bottomMask" class="loadingMaskBg txtCenter"><span>@oltdhcp.pppoeCloseTip@</span></div>')
	}else{ 
		//已存在，显示;
		$mask.fadeIn();
	}
}

function setPppoeSwitch(selectValue){
	var tip;
	if(selectValue == 1){
		tip = "@oltdhcp.open@";
	}else {
		tip = "@oltdhcp.close@";
	}
	if(selectValue != 1) {
		window.top.showConfirmDlg("@COMMON.tip@", "@oltdhcp.confirmDown@", function(type){
			if (type == 'yes') {
				window.top.showWaitingDlg("@COMMON.wait@", tip + "PPPoE....", 'ext-mb-waiting');
			    $.ajax({
			        url:"/epon/oltdhcp/modifyOltPppoeEnable.tv?entityId="+entityId+"&&pppoEPlusEnable="+selectValue,
			        method:"post",cache: false,dataType:'text',
			        success:function (text) {
			            window.top.closeWaitingDlg();
			            top.afterSaveOrDelete({
			                   title: '@COMMON.tip@',
			                   html: '<b class="orangeTxt">'+tip+'PPPoE@oltdhcp.success@！</b>'
			            });
			            switchPPPoE(selectValue)
			        },error:function(){
			        	window.top.closeWaitingDlg();
			            window.parent.showMessageDlg("@COMMON.tip@", tip+"PPPoE@oltdhcp.fail@！");
			        }
			    });
			}else{
				imgBtn.setValue(1);
			}
		});
	}else{
		window.top.showWaitingDlg("@COMMON.wait@", tip + "PPPoE....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/modifyOltPppoeEnable.tv?entityId="+entityId+"&&pppoEPlusEnable="+selectValue,
	        method:"post",cache: false,dataType:'text',
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">'+tip+'PPPoE@oltdhcp.success@！</b>'
	            });
	            switchPPPoE(selectValue);
	            window.location.reload();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", tip+"PPPoE@oltdhcp.fail@！");
	        }
	    });
	}
}

function refresh(){
	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshPPPoEData@....", 'ext-mb-waiting');
    $.ajax({
        url:"/epon/oltdhcp/refreshOltPppoeData.tv?entityId="+entityId,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@oltdhcp.refreshPPPoEData@@oltdhcp.success@！</b>'
            });
            window.location.reload();
        },error:function(){
        	window.top.closeWaitingDlg();
            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshPPPoEData@@oltdhcp.fail@！");
        }
    });
}