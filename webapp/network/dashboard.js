Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.Updater.defaults.disableCaching = true;
    Ext.Updater.defaults.timeout = 15;
 	var portletItems = [];
 	var size = 2;
 	for (var i = 0; i < size; i++) {
 		if(i%2 == 0){
 			portletItems[i] =  {columnWidth: .5, style: 'padding:10px 0px 10px 10px', items: []};
 		}else{
 			portletItems[i] =  {columnWidth: .5, style: 'padding:10px 10px 10px 10px', items: []};
 		}
 	}
 	//ccmts上行信道低信噪比排名top10
	var noise = new Ext.ux.Portlet({
		id:'noise',
	    tools: [{
		        id: 'gear',
		        qtip:'@COMMON.more@',
	            handler: function(event, toolEl, panel){
	                window.top.addView("CcmtsMoreNoise", '@CcmtsUpNoise@', "apListIcon", "/cmcperf/showNoiseRate.tv");
	            }
	        },{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: function(event, toolEl, panel){
	            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
		        	var h = panel.getInnerHeight()-20,
		        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
		        	    url = '/cmcperf/getTopLowNoiseLoading.tv';
		        	
		        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
		        		zebraTable();
		        	}});
	            	//getTopLowNoiseLoading();
	            }
	        }],
        title: '@NETWORK.ccmtsUsSnrTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        autoLoad: {url: '/cmcperf/getTopLowNoiseLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });

    //设备用户数排名top10;
    var ccUsers = new Ext.ux.Portlet({
        id:'ccUsers',	    
        title: '@NETWORK.ccmtsUsersTop10@',		
        bodyStyle: 'padding:10px',        
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("CcmtsMoreUser", '@CcmtsUser@', "apListIcon", "/cmcperf/showCmtsUser.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/cmcperf/getTopCcUsersLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopCcUsersLoading();
            }
        }],
        autoLoad: {url: '/cmcperf/getTopCcUsersLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });	
  	//上行用户数排名;
    var portletCcmtsUpChnluserNums = new Ext.ux.Portlet({
			id:'portletCcmtsUpChnluserNums',
	        title: '@NETWORK.ccmtsUsUsersTop10@',
			bodyStyle: 'padding:10px',
	        autoScroll:true,
	        tools: [{
		        id: 'gear',
		        qtip:'@COMMON.more@',
	            handler: function(event, toolEl, panel){
	                window.top.addView("CcmtsUpChannelUser", '@CcmtsUpChannelUser@', "apListIcon", "/cmcperf/showCmtsUpChannelUser.tv");
	            }
	        },{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: function(event, toolEl, panel){
	            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
		        	var h = panel.getInnerHeight()-20,
		        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
		        	    url = '/cmcperf/getTopUpChnUsersLoading.tv';
		        	
		        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
		        		zebraTable();
		        	}});
	            	//getTopUpChnUsersLoading();
	            }
	        }],
	        autoLoad: {url: '/cmcperf/getTopUpChnUsersLoading.tv', text: '@COMMON.loading@', disableCaching: true}
	    });
  	//下行用户数排名;
    var portletCcmtsDownChnluserNums = new Ext.ux.Portlet({
			id:'portletCcmtsDownChnluserNums',
	        title: '@NETWORK.ccmtsDsUsersTop10@',
			bodyStyle: 'padding:10px',
	        autoScroll:true,
	        tools: [{
		        id: 'gear',
		        qtip:'@COMMON.more@',
	            handler: function(event, toolEl, panel){
	                window.top.addView("CcmtsDownChannelUser", '@CcmtsDownChannelUser@', "apListIcon", "/cmcperf/showCmtsDownChannelUser.tv");
	            }
	        },{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: function(event, toolEl, panel){
	            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
		        	var h = panel.getInnerHeight()-20,
		        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
		        	    url = '/cmcperf/getTopDownChnUsersLoading.tv';
		        	
		        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
		        		zebraTable();
		        	}});
	            	//getTopDownChnUsersLoading();
	            }
	        }],
	        autoLoad: {url: '/cmcperf/getTopDownChnUsersLoading.tv', text: '@COMMON.loading@', disableCaching: true}
	    });
  	//网络基本信息;
	var portlet21 = new Ext.ux.Portlet({
			id:'portlet21',
	        title: '@RESOURCES/PortletCategory.getNetworkInfo@',
	        autoScroll:true,
	        tools:[{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: function(event, toolEl, panel){
	            	ZetaUtils.getIframe('networkInfoDist').redraw();
		            return false;
	            }
	        }],
	        html:"<iframe width=100% id='networkInfoDist' name='networkInfoDist' height=330 frameborder=0 src='/portal/getNetworkInfo.tv'></iframe>"
    });
	//信道利用率排名top10;
	var portletCcmtsChnlUtili = new Ext.ux.Portlet({
			id:'portletCcmtsChnlUtili',
	        tools: [{
		        id: 'gear',
		        qtip:'@COMMON.more@',
	            handler: function(event, toolEl, panel){
	                window.top.addView("channelUsed", '@NETWORK.channelUsed@', "apListIcon", "/cmcperf/getMoreChannelUsed.tv");
	            }
	        },{
	        	id: 'refresh',
		        qtip:'@COMMON.refresh@',
	            handler: function(event, toolEl, panel){
	            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
		        	var h = panel.getInnerHeight()-20,
		        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
		        	    url = '/cmcperf/getTopChnlUtiliLoading.tv';
		        	
		        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
		        		zebraTable();
		        	}});
	            	//getTopChnlUtiliLoading();
	            }
	        }],
	        title: '@NETWORK.channelUtiTop10@',
			bodyStyle: 'padding:10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmcperf/getTopChnlUtiliLoading.tv', text: '@COMMON.loading@', disableCaching: true}
	    });
	//sni口速率排名
	var portletSni = new Ext.ux.Portlet({
		id:'portletSni',
        title: '@NETWORK.sniWidthTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("sniSpeedList", '@NETWORK.sniPortSpeed@', "apListIcon", "/epon/perf/showSniPortSpeed.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/epon/perf/getTopSniLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopSniLoading();
            }
        }],
        autoLoad: {url: '/epon/perf/getTopSniLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
	//pon口速率排名;
    var portletPon = new Ext.ux.Portlet({
		id:'portletPon',
        title: '@NETWORK.ponWidthTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("ponSpeedList", '@NETWORK.ponPortSpeed@', "apListIcon", "/epon/perf/showPonPortSpeed.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/epon/perf/getTopPonLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopPonLoading();
            }
        }],
        autoLoad: {url: '/epon/perf/getTopPonLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
  	//ccmts 上行信道误码率排名top10;
	var portletErrorCodes = new Ext.ux.Portlet({		
        id:'portletErrorCodes',
        title: '@NETWORK.ccmtsUsBerTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("CcmtsMoreBer", '@CcmtsUpBer@', "apListIcon", "/cmcperf/showCmtsBerRate.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/cmcperf/getTopPortletErrorCodesLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopPortletErrorCodesLoading();
            }
        }],
        autoLoad: {url: '/cmcperf/getTopPortletErrorCodesLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
	//ccmts CM flap排名top10;
	var portletFlapInsGrowth = new Ext.ux.Portlet({		
        id:'portletFlapInsGrowth',
        title: '@NETWORK.cmFlapInsGrowthTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("CcmtsMoreFlap", '@CcmtsCmFlap@', "apListIcon", "/cmcperf/showCmFlapIns.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/cmcperf/getTopPortletFlapInsGrowthLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopPortletFlapInsGrowthLoading();
            }
        }],
        autoLoad: {url: '/cmcperf/getTopPortletFlapInsGrowthLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
	//网络设备响应延迟;
    var portletDelaying = new Ext.ux.Portlet({
		id:'portletDelaying',
        title: '@NETWORK.entityDelayTop10@',
        bodyStyle: 'padding:10px',
        autoScroll:true,
        tools:[{
        	 id: 'gear',
 	         qtip:'@COMMON.more@',
             handler: function(event, toolEl, panel){
                 window.top.addView("delayMore", '@NETWORK.entityDelay@', "apListIcon", "/portal/showDeviceDelayJsp.tv");
             }        	
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/portal/getDeviceDelayingTop.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getDeviceDelayingTop();
            }   
        }],
        autoLoad: {url: '/portal/getDeviceDelayingTop.tv', text: '@COMMON.loading@', disableCaching: true}
    });
    
  //网络设备响应超时;
    var portletDelayOut = new Ext.ux.Portlet({
		id:'portletDelayOut',
        title: '@NETWORK.entityDelayOut10@',
        bodyStyle: 'padding:10px',
        autoScroll:true,
        tools:[{
        	 id: 'gear',
 	         qtip:'@COMMON.more@',
             handler: function(event, toolEl, panel){
                 window.top.addView("delayOutMore", '@NETWORK.entityDelayOut@', "apListIcon", "/portal/showDeviceDelayOutJsp.tv");
             }        	
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/portal/getDeviceDelayingOut.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getDeviceDelayingOut();
            }   
        }],
        autoLoad: {url: '/portal/getDeviceDelayingOut.tv', text: '@COMMON.loading@', disableCaching: true}
    });
  	//网络设备cpu负载;
    var portlet1 = new Ext.ux.Portlet({
		id:'portlet1',
        title: '@NETWORK.entityCpuTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("cpuUsed", '@NETWORK.cpuUsed@', "apListIcon", "/portal/showDeviceCpuJsp.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/portal/getTopCpuLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopCpuLoading();
            }
        }],
        autoLoad: {url: '/portal/getTopCpuLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
  	//网络设备MEM负载;
    var portletMem = new Ext.ux.Portlet({
		id:'portletMem',
        title: '@NETWORK.entityMemTop10@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("memUsed", '@NETWORK.memUsed@', "apListIcon", "/portal/showDeviceMemJsp.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/portal/getTopMemLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopMemLoading();
            }
        }],
        autoLoad: {url: '/portal/getTopMemLoading.tv', text: '@COMMON.loading@', disableCaching: true}
    });
	//ccmts光功率top10
	var optical = new Ext.ux.Portlet({
		id:'optical',
	    tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("cmcMoreOptical", '@DEVICEVIEW.cmcOptical@', "apListIcon", "/cmcperf/showCmcOpticalInfo.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/cmcperf/loadCmcOpticalTop10.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopCmcOptical();
            }
        }],
        title: '@DEVICEVIEW.cmcOpticalRange@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        autoLoad: {url: '/cmcperf/loadCmcOpticalTop10.tv', text: '@COMMON.loading@', disableCaching: true}
    });
	//ccmts光机温度top10
	var opticalTemp = new Ext.ux.Portlet({
		id:'opticalTemp',
	    tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("cmcMoreOpticalTemp", '@DEVICEVIEW.cmcOpticalTemp@', "apListIcon", "/cmcperf/showCmcOpticalTempInfo.tv");
            }
        },{
        	id: 'refresh',
	        qtip:'@COMMON.refresh@',
            handler: function(event, toolEl, panel){
            	//先获取目前panel中body的高度，由于padding是10,所以减去上下共20像素距离;
	        	var h = panel.getInnerHeight()-20,
	        	    str = String.format('<div style="height:{0}px;">{1}</div>',h,'@COMMON.loading@'),
	        	    url = '/cmcperf/getTopCmcOpticalTempLoading.tv';
	        	
	        	panel.getUpdater().update({text:str, showLoadIndicator : true,disableCaching: true, url : url ,callback:function(){
	        		zebraTable();
	        	}});
            	//getTopCmcOptical();
            }
        }],
        title: '@DEVICEVIEW.cmcOpticalTempRange@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        autoLoad: {url: '/cmcperf/getTopCmcOpticalTempLoading.tv', text: '@COMMON.loading@', disableCaching: true}
	});
    //左侧板块，从数据库读取;
	var leftPartStr = deviceViewLeft;
	//右侧板块，从数据库读取;
	var rightPartStr = deviceViewRight;
	//如果是第一次加载,就进行默认初始化
	if(leftPartStr == '' && rightPartStr == ''){
		leftPartStr = "noise,optical,opticalTemp,ccUsers,portletCcmtsUpChnluserNums,portletCcmtsDownChnluserNums,portlet21,portletPon,portletDelayOut";
	 	rightPartStr = "portletCcmtsChnlUtili,portletSni,portletErrorCodes,portletFlapInsGrowth,portletDelaying,portlet1,portletMem";
	}
 	var leftPartArr = [];
 	var rightPartArr = [];
 	
 	//如果新加一个版块，数据库里面又有了。那么通过对比，放入右侧最后；
 	var newPart = {
 		//数据库读取的左侧数组；
 		leftPartArr : leftPartStr.split(","),
 		//数据库读取的右侧数组；
 		rightPartArr : rightPartStr.split(","),
 		//所有的
 		all : 'noise,optical,opticalTemp,ccUsers,portletCcmtsUpChnluserNums,portletCcmtsDownChnluserNums,portlet21,portletPon,portletDelayOut,portletCcmtsChnlUtili,portletSni,portletErrorCodes,portletFlapInsGrowth,portletDelaying,portlet1,portletMem',
 		allArr : []
 	};
 	newPart.allArr = newPart.all.split(",");
 	
 	for(var i=0,len=newPart.rightPartArr.length; i<len; i++){
 		var nowStr = newPart.rightPartArr[i],
 		    isInside = $.inArray(nowStr,newPart.allArr);
 		if(isInside == -1){
 			if(typeof nowStr !== 'undefined' && (eval(nowStr) instanceof Ext.ux.Portlet)){
 				rightPartStr += ("," + nowStr);
 			}
 		}
 	}
 	for(var i=0,len=newPart.leftPartArr.length; i<len; i++){
 		var nowStr = newPart.leftPartArr[i],
 		    isInside = $.inArray(nowStr,newPart.allArr);
 		if(isInside == -1){
	 		if(typeof nowStr !== 'undefined' && (eval(nowStr) instanceof Ext.ux.Portlet)){
	 			rightPartStr += ("," + nowStr);
	 		}
 		}
 	}
 	//开始添加板块;
    if(leftPartStr != ''){
		 var tempArr = leftPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 var temp = false;
			 temp = supportFn(tempArr[i]);
			 if(temp == false){
				 pushArr(tempArr[i]);
				 leftPartArr.push(eval(tempArr[i]));
			 }
		 }
	}
   //开始添加板块;
	if(rightPartStr != ''){
	    var tempArr = rightPartStr.split(",");
		for(var i=0; i<tempArr.length; i++){
			 var temp = false;
			 temp = supportFn(tempArr[i]);
			 if(temp == false){
				 pushArr(tempArr[i]);
				 rightPartArr.push(eval(tempArr[i]));
			 }
	    }
	}
 	portletItems[0].items = leftPartArr;
 	portletItems[1].items = rightPartArr;
 	
 	
 	viewport = new Ext.Viewport({
        layout: 'fit',
        items: [{xtype:'portal', margins:'0 0 0 0', border:false, items: portletItems}]
    });
    
    startDispatchInterval();
    function supportFn(para){
		if(para == "noise" || para == "ccUsers" || para == "portletCcmtsUpChnluserNums" || para == "portletCcmtsDownChnluserNums" || para == "portletFlapInsGrowth"
			|| para == "portletCcmtsChnlUtili" || para == "portletErrorCodes" || para == "optical" || para == "opticalTemp"){
			if(!supportCmc){//不支持cmc;
				return true;
			}else{
				return false;
			}
		}else if(para == "portletSni" || para == "portletPon"){
			if(!supportEpon){//不支持Epon;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
    function pushArr(para){
    	switch(para){
        	case "noise":
        		//portlets.push(noise);
        		//urls.push('/cmcperf/getTopLowNoiseLoading.tv');
        	    functions.push("getTopLowNoiseLoading()");
        		break;
        	case "ccUsers":
        		//portlets.push(ccUsers);	
        	    //urls.push('/cmcperf/getTopCcUsersLoading.tv');
        	    functions.push("getTopCcUsersLoading()");
        		break;
        	case "portletErrorCodes":
        		//portlets.push(portletErrorCodes);
        		//urls.push('/cmcperf/getTopPortletErrorCodesLoading.tv');
        	    functions.push("getTopPortletErrorCodesLoading()");
        		break;
        	case "portletFlapInsGrowth":
        		//portlets.push(portletFlapInsGrowth);
        		//urls.push('/cmcperf/getTopPortletFlapInsGrowthLoading.tv');
        		functions.push("getTopPortletFlapInsGrowthLoading()");
        		break;
        	case "portletCcmtsUpChnluserNums":
        		//portlets.push(portletCcmtsUpChnluserNums);
        		//urls.push('/cmcperf/getTopUpChnUsersLoading.tv');
        	    functions.push("getTopUpChnUsersLoading()");	
        		break;
        	case "portletCcmtsDownChnluserNums":
        		//portlets.push(portletCcmtsDownChnluserNums);
        	    //urls.push('/cmcperf/getTopDownChnUsersLoading.tv');
        	    functions.push("getTopDownChnUsersLoading()");
        		break;
        	case "portlet21":
        		//portlets.push(portlet21);
        		//urls.push('');
        	    functions.push('');
        		break;
        	case "portletCcmtsChnlUtili":
        		//portlets.push(portletCcmtsChnlUtili);
        		//urls.push('/cmcperf/getTopChnlUtiliLoading.tv');
                functions.push("getTopChnlUtiliLoading()");
        		break;
    		case "portletSni":
    			//portlets.push(portletSni);
    			//urls.push('/epon/perf/getTopSniLoading.tv');
    		    functions.push("getTopSniLoading()");
        		break;
    		case "portletPon":
    			//portlets.push(portletPon);
    			//urls.push('/epon/perf/getTopPonLoading.tv');
    		    functions.push("getTopPonLoading()");
    			break;
    		case "portletErrorCodes":
    			//portlets.push(portletErrorCodes);
    			//urls.push('/cmcperf/getTopPortletErrorCodesLoading.tv');
    		    functions.push("getTopPortletErrorCodesLoading()");
    			break;
    		case "portletDelaying":
    			//portlets.push(portletDelaying);
    			//urls.push('/portal/getDeviceDelayingTop.tv');
    		    functions.push("getDeviceDelayingTop()");
    		break;
    		case "portletDelayOut":
    			//portlets.push(portletDelaying);
    			//urls.push('/portal/getDeviceDelayingTop.tv');
    		    functions.push("getDeviceDelayingOut()");
    		break;
    		case "portlet1":
    			//portlets.push(portlet1);
    			//urls.push('/portal/getTopCpuLoading.tv');
    		    functions.push("getTopCpuLoading()");
    		break;
    		case "portletMem":
    			//portlets.push(portletMem);
    			//urls.push('/portal/getTopMemLoading.tv');
    		    functions.push("getTopMemLoading()");
    		break;
    		case "optical":
    			//portlets.push(optical);
    			//urls.push('/cmcperf/loadCmcOpticalTop10.tv');
    		    functions.push("getTopCmcOptical()");
    		    break;
    		case "opticalTemp":
    		    functions.push("getTopCmcOpticalTemp()");
    		    break;
    	}
    };//end pushArr;
	  //通过判断左右两侧id的顺序，判断布局是否改变，如果改变，那么保存布局(要求id和变量名一致);
		$(".x-panel-tl").live("mouseup",function(){
			 setTimeout(saveLayout,200);
			});//end live; 
			function saveLayout(){
				var leftArr = [];
				var rightArr = [];
				$(".x-portal-column").eq(0).find(".x-portlet").each(function(){
					leftArr.push($(this).attr("id"));
				})
				$(".x-portal-column").eq(1).find(".x-portlet").each(function(){
					rightArr.push($(this).attr("id"));
				})
				
				if(leftPartStr == leftArr.toString() && rightPartStr == rightArr.toString()){//没有变化;
					
				}else{//有变化;
					leftPartStr = leftArr.toString();
					rightPartStr = rightArr.toString();
					$.ajax({
						url: '/network/saveDeviceView.tv',
						cache:false, 
						method:'post',
						data: {
							deviceViewLeft : leftPartStr, 
							deviceViewRight : rightPartStr
						},
						success: function() {
						},
						error: function(){
						}
					});
				};//end if else;
			}
});//end document.ready; 	

function startDispatchInterval() {
	if (timer == null) {
    	timer = setInterval("doAutoRefresh()", dispatchInterval);//run,delay
    }
}
function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
		count = 0;
	}
}

function viewPortFlow(entityId, entityIp, ifIndex) {
		window.open('../realtime/showPortFlowInfo.tv?entityId=' + entityId +
				'&ip=' + entityIp + '&ifIndex=' + ifIndex, 'realtime' + entityId);
}
function showAlertByLevel(index, key) {
	alert(index + "  " + key);
}

function showEntitySnap(entityId, $name,cmcId,mac) {
    if(cmcId){
        showCcmtsPortal(cmcId,null, $name);
    }else{
        window.top.addView("entity-" + entityId,   $name , 'entityTabIcon',
                "/portal/showEntitySnapJsp.tv?module=1&entityId=" + entityId);
    }
}

function showUnKnownEntityJsp(entityId, $name,ip) {
    window.parent.createDialog("entityBasicInfo", String.format("@NETWORK.entityBasicInfo@",ip),600,510,
    	    "portal/showUnknownEntityJsp.tv?entityId=" + entityId, null, true, true);
}

function showCcmtsPortal(cmcId , cmcMac ,cmcName, typeId){
	if(typeId != null && EntityType.isCmtsType(typeId)){
		window.parent.addView('entity-' + cmcId, cmcName, 'entityTabIcon',
	            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
	}else{
		window.parent.addView('entity-' + cmcId, cmcName , 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId);
	}
}
function showDevInfo(cmcId,perfType){
	window.parent.addView("historyShow"+cmcId, '@NETWORK.hisPerf@', "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId +
			"&timeType=Today&perfType="+perfType+"&num=" + Math.random()); 
}
function showPortInfo(cmcId,channelIndex,perfType){
	window.parent.addView("historyShow"+cmcId + "-"+channelIndex + "-"+perfType, '@NETWORK.hisPerf@', "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId +
			"&timeType=Today&index="+channelIndex+"&perfType="+perfType+"&num=" + Math.random()); 
}
function showEntityByState(index, key) {
}

function showEntityByType(type, name) {
	window.parent.addView('entityType-' + type, name, 'entityTabIcon', 'entity/showEntityByTypeJsp.tv?typeId=' + type);
}

function viewAlertByIp(ip) {
	window.top.addView("entity-" + ip, '@NETWORK.entity@[' + ip + ']', 'entityTabIcon',
		"alert/showEntityAlertJsp.tv?module=6&ip=" + ip);
}


function showChannel(cmcPortId,cmcId,cmcPortName,channelIndex,direction){
	if("US"== direction){
		showUpChannelInfo(cmcPortId, cmcId ,cmcPortName,channelIndex);
	}else{
		showDownChannelInfo(cmcPortId, cmcId ,cmcPortName,channelIndex);
	}
}
function showUpChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    window.parent.addView("channelPortal-"+cmcId + "-"+channelIndex, '@RESOURCES/COMMON.portHeader@['+cmcPortName+"]", "apListIcon", "cmc/showUpChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}

function showDownChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    window.parent.addView("channelPortal-"+cmcId + "-"+channelIndex, '@RESOURCES/COMMON.portHeader@['+cmcPortName+"]", "apListIcon", "cmc/showDownChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}

function getTopChnlUtiliLoading() {
    $.ajax({
        url: '/cmcperf/getTopChnlUtiliLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopChnlUtiliLoading").replaceWith(text);
        }
    });
}

function getNetworkInfo() {
    $.ajax({
        url: '/portal/getNetworkInfo.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#mapStateDist").remove();
            $("#getNetworkInfo").replaceWith(text);
        }
    });
}

function getTopSniLoading() {
    $.ajax({
        url: '/epon/perf/getTopSniLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopSniLoading").replaceWith(text);
        }
    });
}

function getTopPonLoading() {
    $.ajax({
        url: '/epon/perf/getTopPonLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopPonLoading").replaceWith(text);
        }
    });
}
function getTopLowNoiseLoading() {
    $.ajax({
        url: '/cmcperf/getTopLowNoiseLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopLowNoiseLoading").replaceWith(text);
        }
    });
}

function getTopPortletErrorCodesLoading() {
    $.ajax({
        url: '/cmcperf/getTopPortletErrorCodesLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopPortletErrorCodesLoading").replaceWith(text);
        }
    });
}
function getTopPortletFlapInsGrowthLoading() {
    $.ajax({
        url: '/cmcperf/getTopPortletFlapInsGrowthLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopPortletFlapInsGrowthLoading").replaceWith(text);            
        }
    });
}
function getTopCcUsersLoading() {
    $.ajax({
        url: '/cmcperf/getTopCcUsersLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopCcUsersLoading").replaceWith(text);
        }
    });
}

function getTopUpChnUsersLoading() {
    $.ajax({
        url: '/cmcperf/getTopUpChnUsersLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopUpChnUsersLoading").replaceWith(text);
        }
    });
}

function getTopDownChnUsersLoading() {
    $.ajax({
        url: '/cmcperf/getTopDownChnUsersLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopDownChnUsersLoading").replaceWith(text);
        }
    });
}

function getDeviceDelayingTop() {
    $.ajax({
        url: '/portal/getDeviceDelayingTop.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getDeviceDelayingTop").replaceWith(text);
        }
    });
}

function getDeviceDelayingOut() {
    $.ajax({
        url: '/portal/getDeviceDelayingOut.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getDeviceDelayingOut").replaceWith(text);
        }
    });
}

function getTopCpuLoading() {
    $.ajax({
        url: '/portal/getTopCpuLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopCpuLoading").replaceWith(text);
        }
    });
}

function getTopMemLoading() {
    $.ajax({
        url: '/portal/getTopMemLoading.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopMemLoading").replaceWith(text);
        }
    });
}

function getTopCmcOptical(){
	 $.ajax({
        url: '/cmcperf/loadCmcOpticalTop10.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getTopCmcOptical").replaceWith(text);
        }
    });
}

function getTopCmcOpticalTemp(){
	 $.ajax({
      url: '/cmcperf/getTopCmcOpticalTempLoading.tv?r=' + Math.random(),
      type: 'POST',
      dataType:'text',
      success: function(text) {
          $("#getTopCmcOpticalTemp").replaceWith(text);
      }
  });
}

function tabActivate() {
	doAutoRefresh();
	startDispatchInterval();
}
function tabDeactivate() {
	doOnunload();
}
function tabRemoved() {
	doOnunload();
}
// for tab changed end

var timer = null;
var syncount = 0;
var synMax = 2;
function doAutoRefresh() {
    for (var i = 0; i < functions.length; i++) {
//		if (i % synMax == syncount) {
//			var mgr = portlets[i].getUpdater();
//			mgr.update({disableCaching: true, url: urls[i]});
            try {
                eval(functions[i]);
            }catch (e) {
            }
//        }
	}
    // added by @bravin.网络基本信息没有必要定时刷新，一般都是稳定的，那么大的一个东西定时刷新也不太合适
//	syncount++;
//	if (syncount >= synMax) {
//		syncount = 0;
//	}
}
window.onresize = function(){
	//由于 navigator是 缓慢的缩小，所以resize事件刚触发的时候doc内部的宽度并没有变，所以加个延时
	 setTimeout(function(){
		 try{
		 	ZetaUtils.getIframe('networkInfoDist').redraw();
		 }catch(e){}
		 try{
			 ZetaUtils.getIframe('cmCpeNumStatistic').redraw();
		 }catch(e){}
	 },500);
}