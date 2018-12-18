var progressWin; //显示进度的windows;
var progressCir;
var errWin; //错误窗口;
var heartWin; //心跳窗口;
var cmWebWin; //单机web窗口;
var boxProgress;
var steps = [
	'<p>====== <b class="lightBlueTxt">@webProxy.init@</b> ======</p>',
	'<p class="pT20 pB5">1.@webProxy.createCcProxy@</p><p>CMTS IP@COMMON.maohao@<label class="pR20">{cmcIp}</label>@webProxy.proxyPort@@COMMON.maohao@{cmcPort}</p>',
	'<p class="pT20 pB5">2.@webProxy.createTansPort@</p><p>CM IP@COMMON.maohao@<label class="pR20">{cmIp}</label>@webProxy.portNum@@COMMON.maohao@{nm3000Port}</p>',
	'<p class="pT20 pB5">====== <b class="lightBlueTxt">@webProxy.success@</b> ======</p>'
]


Ext.onReady(function(){
	createErrWin();
	creatProgressWin();
	createHeartWin();
});

function initAllWindow(str){
	if(errWin){
		var html = String.format('<div id="withoutErr" class="pT5 pB5">====== {0} ======</div><div id="mainErr"></div>', str);
		errWin.removeClass('errCls');
		var $errWin = $('#errWindow').find('.x-window-body');
		$errWin.append(html);
		$errWin.scrollTop(10000);
		//errWin.update(html);
	}
	if(progressWin){
		var html = String.format('<div id="putCir" class="txtCenter"></div><div id="putDot" class="pT40 yellowTxt"><p>{0}</p></div><div id="putSubBox"></div><div id="putSubTxt" class="yellowTxt"></div>', str);
		progressWin.removeClass('errCls');
		progressWin.update(html);
		progressCir = new RaphaelCir({
			renderTo : "putCir",
			title : "",
			notGetData : "@epon/Tip.failedGetData@",
			NaNData : "@epon/ONU.loadDataError@",
			percent : 0
		});
		boxProgress = new ProgressBox({
			renderTo: 'putSubBox',
			num : 50
		});
		boxProgress.init();
		$('#putSubTxt').html('<div class="yellowLine2">@webProxy.disconnect@</div>');
	}
	if(heartWin){
		heartWin.removeClass('errCls');
		heartWin.update('<p class="lightBlueTxt needClear">====== @webProxy.disconnect@ ======</p>');
	}
	
}
	
//更新进度和日志里面的文字;
function updateTxt(stepNum, o){
	var flash = '<span class="">_</span>';
	var str = '<div class="yellowLine2">';
	for(var i=0; i<=stepNum; i++){
	    str += String.format(window.steps[i], o);
	}
	str += '</div>';
	$('#putSubTxt').html(str);
	
	var log =  String.format(window.steps[stepNum], o);
	var $err = $('#mainErr');
	$err.append(log);
	$('#errWindow').find('.x-window-body').scrollTop(100000);
	//$('p:last').append(flash);
}
//将true，false转换成勾差图片;
function booleanChangeToImg(b){
	if(b){
		return '<img class="geekImg" src="../images/geekOn.png" border="0" />';
	}else{
		return '<img class="geekImg" src="../images/geekOff.png" border="0" />';
	}
}
//更新心跳窗口的内容;
function updateHeartBeatWin(o){
	if(heartWin){
		var html = [
		   '<div class="lightBlueLine mB5 mT5"><p class="lightBlueTxt">@webProxy.heartbeatTime@@COMMON.maohao@{1}</p></div>',
		   '<div class="yellowLine1">',
			   '<p class="pB5 yellowTxt">@webProxy.heartbeatId@@COMMON.maohao@{0}</p>',
			   '<p class="pB5 yellowTxt">@webProxy.heartbeatPort@@COMMON.maohao@{2}</p>',
			   '<p class="pB5 yellowTxt">@webProxy.loginStatus@@COMMON.maohao@{3}</p>',
			   '<p class="pB5 yellowTxt">@webProxy.tansPortStatus@@COMMON.maohao@{4}</p>',
			   '<p class="pB5 yellowTxt">@webProxy.ccProxyPortStatus@@COMMON.maohao@{5}</p>',
		   '</div>',
		].join('');
		var d = new Date(o.heartbeatTime);
		var time = d.doFormat('yyyy-MM-dd hh:mm:ss');
		html = String.format(html, o.heartbeatId, time, 
				booleanChangeToImg(o.heartbeatStatus), 
				booleanChangeToImg(o.loginStatus), 
				booleanChangeToImg(o.cmPortMapStatus), 
				booleanChangeToImg(o.cmcProxyStatus));
		//heartWin.update(html);
		
		var $body = $('#heartWindow').find('.x-window-body'); 
		$body.find('.needClear').remove();
		$body.append(html);
		setTimeout(function(){
			var pos = $('.yellowLine1').length * 200;
			$body.animate({scrollTop: pos}, 500);
			if($('.lightBlueLine').length >= 20){ //当有20条日志的时候,清除8条;
				$('.lightBlueLine').each(function(i){
					if(i <= 10){
						var $me = $('.lightBlueLine').eq(i),
						    $next = $me.next();
						$me.remove();
						$next.remove();
					}
					
				})
				
			}
		}, 100);
		
	}
}
//更新圆圈进度;
function updateProgress(num){
	if(progressCir){
		progressCir.update(num);
	}
}
//更新进度条，边上的文字;
function updateProgressText(html){
	$('#putDot').append(html);
}
//更新box进度;
function updateBoxProgress(num){
	if(boxProgress){
		boxProgress.setProgress(num);
	}
}
//更新错误面板的内容;
function updateErrText(html){
	var $p = $('#mainErr p');
	if($p.length > 1000){ //错误信息超过1000条，清屏一次;
		$('#mainErr').empty();
	}
	var $mainErr = $('#mainErr');
	$mainErr.append(html);
	$('#errWindow').find('.x-window-body').scrollTop(10000000);
	if(!$('#progresswindow').hasClass('errCls')){
		if(progressWin){
			progressWin.addClass('errCls');
		}
		if(errWin){
			errWin.addClass('errCls');
		}
		if(heartWin){
			heartWin.addClass('errCls');
		}
		if(cmWebWin){
			cmWebWin.addClass('errCls');
		}
	}
}



function creatProgressWin(){
	progressWin = createWin({
		id : 'progressWindow',
		title : '@webProxy.progress@',
		x: 642,
		y: 58,
		width : 400,
		height: 400,
		bodyCssClass: 'edge5',
		html : '<div id="putCir" class="txtCenter"></div><div id="putDot" class="pT40 yellowTxt"><p>@webProxy.init@...</p></div><div id="putSubBox"></div><div id="putSubTxt" class="yellowTxt"></div>',
		listeners : {
			'afterrender': function(){
				progressCir = new RaphaelCir({
					renderTo : "putCir",
					title : "",
					notGetData : "@epon/Tip.failedGetData@",
					NaNData : "@epon/ONU.loadDataError@",
					percent : 0
				});
				boxProgress = new ProgressBox({
					renderTo: 'putSubBox',
					num : 50
				});
				boxProgress.init();
			},
			'beforehide': function(){
				createFadeInWin({
					srcObj : progressWin,
					targetObj: 'progressFloder'
				});
			}
		}
	});
}
function createErrWin(){
	errWin = createWin({
		id : 'errWindow',
		title : '@webProxy.log@',
		x: 200,
		y: 15,
		width : 400,
		height: 240,
		autoScroll: true,
		listeners : {
			'beforehide': function(){
				createFadeInWin({
					srcObj : errWin,
					targetObj: 'errFloder'
				});
			}
		},
		bodyCssClass: 'edge10 lightBlueTxt',
		html : String.format('<div id="withoutErr"><p class="txtCenter" style="font-size:100px;">☺<p><p class="txtCenter ">====== @webProxy.noErr@ ======</p></div><div id="mainErr"></div>')
		
	});
}
function createHeartWin(){
	heartWin = createWin({
		id : 'heartWindow',
		title : '@webProxy.heartbeat@',
		x: 34,
		y: 274,
		width : 400,
		height: 240,
		autoScroll: true,
		bodyCssClass: 'edge10 audio',
		listeners : {
			'beforehide': function(){
				createFadeInWin({
					srcObj : heartWin,
					targetObj: 'heartFloder'
				});
			}
		},
		html : '<p class="lightBlueTxt needClear">====== @webProxy.init@ ======</p>'
	});
}
function createCmWebWin(src){
	cmWebWin = createWin({
		id : 'cmWebWindow',
		title : '@webProxy.web@',
		x: 40,
		y: 40,
		width : 960,
		height: 500,
		maximizable: true,
		html : String.format('<iframe width="100%" height="100%" frameborder="0" src="{0}" style="background:#fff;"></iframe>', src),
		listeners: {
			'afterrender': function(){
				setTimeout(function(){
					cmWebWin.maximize();	
				},800);
			},
			'beforehide': function(){
				createFadeInWin({
					srcObj : cmWebWin,
					targetObj: 'cmWebFolder'
				});
				
			}
		}
	});
}
function removeCmWeb(){
	cmWebWin.destroy();
	$('#cmWebFolder').remove();
	cmWebWin = null;
}
//收缩小动画;
function createFadeInWin(obj){
	var o = {}
	var size = obj.srcObj.getSize();
	var pos = obj.srcObj.getPosition();
	o.width = size.width;
	o.height = size.height;
	o.x = pos[0];
	o.y = pos[1];
	var target = $('#'+ obj.targetObj).find('.folder-body');
	var targetW = target.width();
	var targetH = target.height();
	var targetX = target.offset().left;
	var targetY =target.offset().top;
		
	var str = String.format('<div class="moveTo{4}" style="width:{0}px; height:{1}px; position:absolute; left:{2}px; top:{3}px; z-index:999999; border:1px dashed #01effc; background:rgba(0,0,0,.8)"></div>', o.width, o.height, o.x, o.y, obj.targetObj);
	$('body').append(str);
	$('.moveTo'+ obj.targetObj).animate({width:targetW, height:targetH, top:targetY, left:targetX},function(){
		$(this).remove();
	})
}

//创建窗口;
function createWin(o){
	var win = new Ext.Window({
		x: o.x,
		y: o.y,
		id: o.id, 
		title: o.title, 
		width: o.width, 
		height: o.height, 
		html: o.html,
		bodyCssClass: o.bodyCssClass || '',
		minimizable: false, 
		maximizable: o.maximizable || false,
		closable: true,
		autoScroll: o.autoScroll || false,
		resizable: false,
		shadow: false,
		modal: false,
		closeAction: 'hide',
		listeners : o.listeners || {},
		tools : o.tools || []
	});
	if (o.closeHandler) {
		win.on("close", o.closeHandler);
	}
	win.show();
	return win;
}
//显示进度窗口;
function showProgressWin(){
	if(progressWin){
		progressWin.show();
	}
}
//显示错误端口;
function showErrorWin(){
	if(errWin){
		errWin.show();
	}
}
//显示心跳窗口;
function showHeartWin(){
	if(heartWin){
		heartWin.show();
	}
}
//显示单机web页面;
function showCmWebWin(){
	if(cmWebWin){
		cmWebWin.show();
	}
}
//创建文件夹和窗口;
function createCmWeb(url){
	//创建文件夹;
	createCmWebFolder();
	createCmWebWin(url);
}
function createCmWebFolder(){
	var str = '<div id="cmWebFolder" class="folder pointer mT30 animated bounceIn" onclick="showCmWebWin()"><div class="folder-title"></div>'
	+'<div class="folder-body"></div><p class="txtCenter pT5">@webProxy.web@</p></div>';
	$('.folders').append(str)
}

var ProgressBox = (function(){
	var progressBox = function(o){
		this.renderTo = o.renderTo;
		this.num = o.num;
	}
	progressBox.prototype = {
		init : function(){
			var temp = '<div class="progressBoxTop darkYellowBg"></div>';
			var temp2 = '<div class="progressBoxBottom darkBlueBg"></div>';
			var str = '';
			for(var i=0; i<this.num; i++){
				str += temp;
			}
			for(var i=0; i<this.num; i++){
				str += temp2;
			}
			$('#'+this.renderTo).append(str);
		},
		setProgress : function(num){ //0-1;
			var $render = $('#'+this.renderTo);
			var $top = $render.find('.progressBoxTop');
			var $bottom = $render.find('.progressBoxBottom');
			//先都清空;
			$top.removeClass('yellowBg');
			$bottom.removeClass('blueBg');
			var precent = num*100/2;
			$top.each(function(i){
				if(i <= precent){
					$(this).addClass('yellowBg');
				}
			});
			$bottom.each(function(i){
				if(i <= precent){
					$(this).addClass('blueBg');
				}
			})
		}
	}
	return progressBox;
})();