var renderImgs = [
    '<img nm3ktip="" class="" src="../images/fault/trap_on.png" border="0" />',
    '<img nm3ktip="" class="" src="../images/fault/trap_off.png" border="0" />'
]
$(function(){
	//创建tbar;
	createTbar();
		
	//创建布局;
	initLayout();
});//end docuemnt.ready;

function createTbar(){
	var edgeCls = 'pR40';
	tb = new Ext.Toolbar({
	    renderTo: 'putTbar',
	    items: [{
	    	xtype: 'tbspacer',
	    	width: 10
	    },{
	    	xtype: 'tbtext', 
	    	cls: 'blueTxt',
	    	text: '@webProxy.heartbeat@@COMMON.maohao@'
	    },{
	    	xtype: 'component',
	    	cls: edgeCls,
	    	id : 'putHeartBeat',
	    	html: '@epon/entitySnapPage.loading@'
	    },{
	    	xtype: 'tbtext', 
	    	cls: 'blueTxt',
	    	text: '@webProxy.ccProxy@@COMMON.maohao@'
	    },{
	    	xtype: 'component',
	    	cls: edgeCls,
	    	id: 'putCcDelegate',
	    	html: '@epon/entitySnapPage.loading@'
	    },{
	    	xtype: 'tbtext',
	    	cls: 'blueTxt',
	    	text: '@webProxy.transPort@@COMMON.maohao@'
	    },{
	    	xtype: 'component',
	    	cls : edgeCls,
	    	id : 'putPortChange',
	    	html: '@epon/entitySnapPage.loading@'
	    },{
	    	text: '@webProxy.turnOff@',
	    	id: 'turnOff',
	    	hidden: true,
	    	iconCls: 'miniIcoClose',
	    	handler:function(){
	    		$.ajax({
	    	        url: '/webproxy/addHeartbeat.tv',
	    	        data: {
	    	        	heartbeatId : window.heartbeatId
	    	        },
	    	        dataType: "json",
	    	        success: function(json) {
	    	        	stepComplete = false;
	    	        	Ext.getCmp('turnOff').setVisible(false);
	    	    		Ext.getCmp('reConnect').setVisible(true);
	    	    		initAllWindow('@webProxy.disconnect@');
	    	    		removeFrameCmWeb();
		    		}, error: function(text) {
		    			window.parent.showMessageDlg("@COMMON.tip@", "@webProxy.turnOffFail@");
		        	}, cache: false
	    		})
	    	}	
	    },{
	    	text: '@webProxy.reconnect@',
	    	id: 'reConnect',
	    	hidden: true,
	    	iconCls: 'miniIcoBack',
	    	handler:function(){
	    		stepComplete = false;
	    		Ext.getCmp('reConnect').setVisible(false);
	    		initAllWindow('@webProxy.reconnect2@');
	    		removeFrameCmWeb();
	    		addHeartbeat();
	    	}	
	    }]
	});

}
//创建布局;
function initLayout(){
	new Ext.Viewport({
		defaults: {
			border: false
		},
	    layout: 'border',
	    items: [{
	    	region: 'north',
	    	contentEl: 'topPart'
	    },{
	    	region: 'center',
	    	contentEl: 'webProxyFrame'
	    }],
	    listeners: {
	    	'afterrender': function(){
	    		addHeartbeat();
	    	}
	    }
	});
}
//第一次进来，添加一个心跳;
function addHeartbeat(){
	$.ajax({
        url: '/webproxy/addHeartbeat.tv',
        data: {
        	cmId : window.cmId
        },
        dataType: "json",
        success: function(json) {
        	//console.log(json)
        	renderHeartbeat(json);
        	//如果心跳状态正常，建立cc代理;拿到cmId,heartbeatId,heartbeatStatus,heartbeatTime(可选项)
        	if(json && json.heartbeatStatus && json.heartbeatId && json.cmId){
        		pickCCProxyByCmcId(json);
        		window.heartbeatId = json.heartbeatId;
        		//得到心跳ID后，每隔5秒去拿一次心跳;
        		interval = setInterval(function(){
        			getHeatbeat();
        		}, 5000);
        	}else{
        		var err = '';
        		if(!json || !json.heartbeatStatus){
        			err += '<p>@webProxy.err.heartbeatStatus@</p>';
        		}
        		updateFrameErrText('<p>@webProxy.err.addHeartBeat@</p>'+ err);
        	}
        }, error: function(text) {
        	updateFrameErrText('<p>@webProxy.err.addHeartBeatFirst@</p>')
    	}, cache: false
    });
}
//建立CC代理;
function pickCCProxyByCmcId(json){
	$.ajax({
        url: '/webproxy/pickCCProxyByCmcId.tv',
        data: {
        	cmId : json.cmId,
        	heartbeatId: json.heartbeatId
        },
        dataType: "json",
        success: function(json) {
        	//console.log(json)
        	//必须拿到cmId,cmcId,cmcIp,cmcPort,cmcProxyStatus,heartbeatId,heartbeatStatus,heartbeatTime(可选项),loginStatus
        	if(json && json.cmcProxyStatus && json.cmcIp && json.cmcPort && json.cmId && json.heartbeatId && json.heartbeatStatus && json.loginStatus){
        		renderCcProxy(json);
        		setTimeout(function(){
        			updateSubFrameTxt(1, json);
        			updateSubFrameProgress(0.33);
        			updateProgressTxt('<p>@webProxy.loaded@33%...</p>');
        			pickPortByCmId(json);//建立转换端口;
        		}, 300);
        	}else{
        		var err = '';
        		if(!json || !json.cmcProxyStatus){
        			err += '<p>@webProxy.err.cmcProxyStatus@</p>';
        		}
        		if(!json || !json.heartbeatStatus){
        			err += '<p>@webProxy.err.heartbeatStatus@</p>';
        		}
        		if(!json || !json.loginStatus){
        			err += '<p>@webProxy.err.loginStatus@</p>';
        		}
        		updateFrameErrText('<p>@webProxy.err.builtCcProxy@</p>'+ err);
        	}
        	
        }, error: function(text) {
        	updateFrameErrText('<p>@webProxy.err.builtCcProxyAjax@</p>')
    	}, cache: false
	});
}
//建立转换端口;
function pickPortByCmId(json){
	$.ajax({
        url: '/webproxy/pickPortByCmId.tv',
        data: {
        	cmId : json.cmId,
        	heartbeatId: json.heartbeatId,
        	manageIp: json.cmcIp, 
        	proxyPort: json.cmcPort
        },
        dataType: "json",
        success: function(json) {
        	//console.log(json);
        	//必须拿到cmId,cmIp,cmPortMapStatus,cmcId,cmcIp,cmcPort,cmcProxyStatus,heartbeatId,heartbeatStatus,heartbeatTime(可选项),loginStatus,natIp,nm3000Port,url;
        	if(json && json.cmId && json.cmIp && json.cmPortMapStatus && json.cmcId && json.cmcIp &&
        			json.cmcPort && json.cmcProxyStatus && json.heartbeatId && json.heartbeatStatus && 
        			json.loginStatus && json.natIp && json.nm3000Port && json.url){
        		setTimeout(function(){
        			updateSubFrameTxt(2, json);
        			updateSubFrameProgress(0.66);
        			updateProgressTxt('<p>@webProxy.loaded@66%...</p>');
        			renderPortChange(json);
        			updateSubFrameTxt(3, json);
        			updateSubFrameProgress(1);
        			updateProgressTxt('<p class="lightBlueTxt"><b>@webProxy.loadSuccess@</b></p>');
        			createFrameCmWeb(json.url);
        			stepComplete = true;
        			//所有过程完成，断开按钮展示;
        			Ext.getCmp('turnOff').setVisible(true);
        			//$('#webProxyFrame').attr({src: json.url});
        		}, 300);
        	}else{
        		var err = '';
        		if(!json || !json.cmcProxyStatus){
        			err += '<p>@webProxy.err.cmcProxyStatus@</p>';
        		}
        		if(!json || !json.heartbeatStatus){
        			err += '<p>@webProxy.err.heartbeatStatus@</p>';
        		}
        		if(!json || !json.loginStatus){
        			err += '<p>@webProxy.err.loginStatus@</p>';
        		}
        		if(!json || !json.cmPortMapStatus){
        			err += '<p>@webProxy.err.cmPortMapStatus@</p>';
        		}
        		updateFrameErrText('<p>@webProxy.err.builtPortFail@</p>'+ err);
        	}
		}, error: function(text) {
			updateFrameErrText('<p>@webProxy.err.builtPortFailAjax@</p>')    	
		}, cache: false
	});
}
//获取心跳;
function getHeatbeat(){
	$.ajax({
        url: '/webproxy/heartbeat.tv',
        data: {
        	heartbeatId : window.heartbeatId
        },
        dataType: "json",
        success: function(json) {
        	renderCcProxy(json);
        	renderHeartbeat(json);
        	renderPortChange(json);
        	if(stepComplete){ //在连接成功过的情况下,下面4个状态，任何一个为false，都是出错了;
        		if(!json || !json.cmPortMapStatus || !json.cmcProxyStatus || !json.heartbeatStatus || !json.loginStatus ){
        			var err = '';
        			if(!json || !json.cmcProxyStatus){
            			err += '<p>@webProxy.err.cmcProxyStatus@</p>';
            		}
            		if(!json || !json.heartbeatStatus){
            			err += '<p>@webProxy.err.heartbeatStatus@</p>';
            		}
            		if(!json || !json.loginStatus){
            			err += '<p>@webProxy.err.loginStatus@</p>';
            		}
            		if(!json || !json.cmPortMapStatus){
            			err += '<p>@webProxy.err.cmPortMapStatus@</p>';
            		}
            		updateFrameErrText('<p class="pT5">@webProxy.err.heartBeatResult@</p>'+ err);
            		
        		}
        	}
        	updateFrameHeartBeatWin(json);
        }, error: function(text) {
        	updateFrameErrText('<p>@webProxy.err.heartBeatResultAjaX@</p>')
    	}, cache: false
    });
}


//端口转换;
function renderPortChange(o){
	var $putPortChange = $('#putPortChange');
	if(o && o.nm3000Port){
		$putPortChange.text(o.nm3000Port);
	}else{
		$putPortChange.html(renderImgs[1]);
	}
	
}
//CC代理;
function renderCcProxy(o){
	var $putCcDelegate = $('#putCcDelegate'),
	    img;
	
	if(o && o.cmcProxyStatus){
		img = renderImgs[0];
	}else{
		img = renderImgs[1];
	}
	$putCcDelegate.html(img);
}
//心跳;
function renderHeartbeat(o){
	var $putHeartBeat = $('#putHeartBeat'),
	    img;
	
	if(o && o.heartbeatStatus){
		img = renderImgs[0];
	}else{
		img = renderImgs[1];
	}
	$putHeartBeat.html(img);
}

function updateSubFrameTxt(stepNum, json){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.updateTxt(stepNum, json);
		}catch(e){
			
		}
	}
}
function updateSubFrameProgress(num){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.updateProgress(num);
		}catch(e){}
		try{
			page.updateBoxProgress(num);
		}catch(e){}
	}
}
function updateProgressTxt(html){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.updateProgressText(html)
		}catch(e){}
	}
}
function createFrameCmWeb(url){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.createCmWeb(url);
		}catch(e){
			
		}
	}
}
function removeFrameCmWeb(){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.removeCmWeb();
		}catch(e){
			
		}
	}
}
function updateFrameHeartBeatWin(json){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.updateHeartBeatWin(json);
		}catch(e){}
	}
}
//更新错误信息;
function updateFrameErrText(html){
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			setTimeout(function(){
				
				//断开按钮消失，重连按钮显示;
				Ext.getCmp('turnOff').setVisible(false);
				Ext.getCmp('reConnect').setVisible(true);
				
				page.updateErrText(html);
			}, 1000);
		}catch(e){}
	}
}
function initAllWindow(str){
	window.clearInterval(interval);
	var page = window.frames['webProxyFrame'];
	if(page){
		try{
			page.initAllWindow(str);
		}catch(e){}
	}
}






