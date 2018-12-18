var Layout = new Function;
var operationPower = (operationDevicePower==undefined||operationDevicePower==null)?false:operationDevicePower;
Layout.prototype.upchannelRender = function (v,m,r){
	var cell = "";
	for(var i=0 ;i<v.length; i++){
		cell = cell.concat(".").concat(v[i].channelId);
	}
	return cell.substring(1);
}
Layout.prototype.downchannelRender = function (v,m,r){
	var cell = "";
	for(var i=0 ;i<v.length; i++){
		cell= cell.concat(".").concat(v[i].channelId);
	}
	return cell.substring(1);
}
Layout.prototype.rangeElTmpl= "...   <a href='#' style='color:blue;' onclick='showRangeDetail(\"{0}\")'>@COMMON.more@</a>";
Layout.prototype.macRangeRender = function(v,m,r){
	var cell = "",
		tooltip;
	for(var i=0 ;i<v.length; i++){
		var macArr = v[i].topLoadBalRestrictCmMacRang.split(" ");
		
		 cell= cell.concat(",").concat(Validator.convertMacToDisplayStyle(macArr[0], macDisplayFormat)+"--"+Validator.convertMacToDisplayStyle(macArr[1], macDisplayFormat));
	}
	tooltip = cell = cell.substring(1);
	var macCellWidth = WIN_WIDTH-595;
	var thisCellWidth = PIXELS_PERF_CHAR * cell.length;
	if( macCellWidth < thisCellWidth - 50 ){
		cell = cell.substring(0,cell.length-50) + String.format(layout.rangeElTmpl, tooltip);
	}else{
		cell = cell.substring(0);
	}
	return String.format("<div title='{0}'>{1}</div>", tooltip, cell);
}
Layout.prototype.manuRender = function(v,m,record){
	var imgStr1 = String.format("onclick='modifyBtClick(\"{0}\")' ", record.id );
    var imgStr2 = String.format("onclick='deleteBtClick(\"{0}\")' ", record.id );
    
    var result = String.format("<a href='javascript:;' {0} >@COMMON.modify@</a> / <a href='javascript:;' {1} >@COMMON.delete@</a>"
    		,imgStr1, imgStr2 );
	if(!operationPower){
		result = String.format("<span class='cccGrayTxt'>@COMMON.modify@</span>",imgStr1);
	}
    return result;
}
/*Layout.prototype.nameRender = function(v,m,r){
	if(!v){
		return "@loadbalance.goupTeml@" + r.data.grpId;
	}else{
		return v;
	}
}*/

Layout.prototype.showLoadBalanceGlobal = function(){
	//全局配置
	new Ext.Panel({
        renderTo : "LB_GLOBAL_PANEL",
        height : 180,
        width : WIN_WIDTH,
        contentEl : "LB-GLOBAL-CONT",
        tbar : [
				"<span style='font-weight:bold'>@loadbalance.globalCfg@</span>",
				{xtype: 'tbspacer', width: 20},
				{xtype: 'radio', width: 20, handler:LB_swichter,id:"globalControl", name:'switcher',value: 1,checked :isLBEnabled},
				{xtype:'label',text:"@loadbalance.openLB@"},
				{xtype: 'radio', width: 20, name:'switcher',checked: !isLBEnabled},
				{xtype:'label',text:"@loadbalance.closeLB@"},
				'->',
	        	{xtype: 'button',text:"@COMMON.saveCfg@", width: 20,id: 'saveGlobal', iconCls:"bmenu_save",handler: saveGlobal,disabled:!operationPower},
	        	'->',
	        	{xtype: 'button',text:"@COMMON.fetch@", width: 20,iconCls:"bmenu_equipment",handler: fetchHandler}
	    ]
    });
}

Layout.prototype.showLoadBalanceGroups = function(h){
	//负载均衡组列表
	window.lb_table = new Ext.grid.GridPanel({
	    stripeRows:true,region: "center",bodyCssClass: 'normalTable',viewConfig: {  forceFit:true,  hideGroupedColumn: true,enableNoGroups: true},
		store: new Ext.data.JsonStore({
			url : '/cmc/loadbalance/getLoadBalanceGroupList.tv',
			baseParams : {
				cmcId : cmcId
			},
			autoLoad : true,
			fields  :["grpId","devceGrpId","docsLoadBalGrpId", "cmcId","groupName","upchannels","downchannels","ranges", "upchannelIds", "downchannelIds"]
		}),
		height : h-30,//230,
		border: true,
        columns: [
        	{header: "@loadbalance.groupId@", width:80, dataIndex: 'devceGrpId'},
        	{header: "@loadbalance.groupName@", width:120,  dataIndex: 'groupName'},
        	/*{header: "<img src='/cmc/image/upchannel.png' /><span>@CCMTS.upStreamChannel@</span>", width:150, dataIndex: 'upchannels',renderer : layout.upchannelRender},
        	{header: "<img src='/cmc/image/downchannel.png' /><span>@CCMTS.downStreamChannel@</span>", width:150, dataIndex: 'downchannels',renderer : layout.downchannelRender},*/
        	{header: "<img src='/cmc/image/upchannel.png' /><span>@CCMTS.upStreamChannel@</span>", width:150, dataIndex: 'upchannelIds'},
        	{header: "<img src='/cmc/image/downchannel.png' /><span>@CCMTS.downStreamChannel@</span>", width:150, dataIndex: 'downchannelIds'},
        	{header: "@loadbalance.macRange@", width: WIN_WIDTH-605, dataIndex: 'ranges',renderer : layout.macRangeRender},
        	{header: "@COMMON.manu@", width:90, dataIndex: 'cmcId',renderer: layout.manuRender, fixed:true}
        ]
	});
	
	new Ext.Panel({
        renderTo : "LBGridPanel",
        height : h,//260,
        width : WIN_WIDTH,
        items :[lb_table],
        tbar : [
        	"<span style='font-weight:bold'>@loadbalance.LBList@</span>",{xtype: 'tbspacer', width: 20},
        	{xtype:'button',text:"@loadbalance.addLB@",id:'addLB',handler: addLoadBalance,iconCls:'bmenu_new'}        
        ]
    });
}

Layout.prototype.showExcMacRange = function(h){
	var el = DOC.createElement("div");
	el.style.height = h-35 + 'px';//140;
	$.ajax({
		url: '/cmc/loadbalance/getLoadBalanceExcMacRangeList.tv',cache:false,dataType:'json',
		data : {
			cmcId : cmcId
		},success:function(segs){
			el.style.paddingTop = 10 + 'px';
			el.style.paddingLeft = 30 + 'px';
			el.style.overflow = 'scroll';
			var table = document.createElement("table");
	 	    var flag = 0;
	 	    var tr;
	 	    for(var i=0; i<segs.length ;i++){
	 			if(i % 4 == 0){
	 				tr = table.insertRow(table.rows.length);
	 				//左边间隔
	 				flag = 0;
	 			}
	 			tr.insertCell(0).style.width = '10px';
	 		    var td = tr.insertCell(flag+1);
	 		    var str = "";
	 		    var macArr = segs[i].topLoadBalExcludeCmMacRang.split(" ");
	 		    if(!operationPower){
	 		    	str = String.format("<span href='#'>{0}</span>", Validator.convertMacToDisplayStyle(macArr[0], macDisplayFormat) +"--"+Validator.convertMacToDisplayStyle(macArr[1], macDisplayFormat));
	 		    }else{
	 		    	str = String.format("<span href='#'>{0}</span><img style='cursor:pointer' src='/images/close.gif' alt='{0}' onclick='deleteExcRange({1},this)' />",Validator.convertMacToDisplayStyle(macArr[0], macDisplayFormat)+"--"+Validator.convertMacToDisplayStyle(macArr[1], macDisplayFormat),segs[i].topLoadBalExcludeCmIndex);
	 		    }
	 		    td.innerHTML = str;
	 	    }
	 	   el.appendChild(table);
		},error:function(){
		}
	});
	if(Ext.getCmp("excludeMacExtPanel")){
		Ext.getCmp("excludeMacExtPanel").destroy();
	}
	new Ext.Panel({
        renderTo : "excludeMacPanel",
        height : h,//175,
        id : 'excludeMacExtPanel',
        width : WIN_WIDTH,
        contentEl :[el],
        tbar : [
            "<span  style='font-weight:bold'>@loadbalance.excRange@</span>",{xtype: 'tbspacer', width: 20},
            {xtype:'button',text:"@loadbalance.addExcRange@",id:'addExcRange',handler:addExcRange,iconCls:'bmenu_new'}        
	    ]
    });
}

function getTimeMillis(timeString){
	var d = timeString.split(":");
	var hourMilis = parseInt(d[0],10)*60*60;
	var secondMilis = parseInt(d[1],10)*60;
	return hourMilis + secondMilis;
}


Layout.prototype.createTime = function(rule,scale){
	var start = getTimeMillis(rule[0]) / HOUR_SECONDS,
		period = (getTimeMillis(rule[1]) - getTimeMillis(rule[0]) )/ HOUR_SECONDS,
		startTime = rule[0],
		endTime = rule[1];
	var timeEl = DOC.createElement("div");
	timeEl.className = "disable-peroid-time";
	if(endTime=="24:00"){
		timeEl.style.width = scale*period+.5*DAY_TIME_COUNT+1 + 'px';//根据展示栏增加的长度进行增加，并+1
	}else{
		timeEl.style.width = scale*period + 'px';
	}
	timeEl.style.left = scale*start + 'px';
	timeEl.title = String.format("@loadbalance.LBdisableTimeFmt@",startTime ,endTime);
	timeDisplayer.appendChild(timeEl);
	
	var startPoint = DOC.createElement("div");
	startPoint.style.left = scale*start + 'px';
	startPoint.style.position = 'absolute';
	startPoint.style.top = -15 + 'px';
	startPoint.style.width = 1 + 'px';
	startPoint.style.backgroundColor = 'green';
	timeDisplayer.appendChild(startPoint);
	
	var startPoint = DOC.createElement("div");
	if(endTime=="24:00"){
		startPoint.style.left = scale*start+scale*period+5 + 'px';//根据展示栏增加的长度进行增加，并+1		
	}else{
		startPoint.style.left = scale*start+scale*period + 'px';//根据展示栏增加的长度进行增加，并+1
	}
	startPoint.style.position = 'absolute';
	startPoint.style.top = -15 + 'px';
	startPoint.style.width = 1 + 'px';
	startPoint.style.backgroundColor = 'green';
	timeDisplayer.appendChild(startPoint);
	
	var teml = '<v:line strokecolor="green"  from="{1},-15" to="{0},-15" ><v:stroke EndArrow="Open"/><v:stroke StartArrow="Open"/></v:line>';
	if(endTime=="24:00"){
		$(timeDisplayer).append($(String.format(teml,scale*start,scale*start+scale*period+5)));
	}else{
		$(timeDisplayer).append($(String.format(teml,scale*start,scale*start+scale*period)));
	}
	var desc = DOC.createElement("div");
	desc.style.left = scale*start + 'px';
	desc.style.color = "gray";
	desc.style.position = 'absolute';
	desc.style.top = -13 + 'px';
	desc.style.fontSize = 10 + 'px';
	//desc.innerHTML = "需详细标注起始时间";//无效时间段（
	desc.innerHTML = "@loadbalance.invalid@";
	timeDisplayer.appendChild(desc);
	$(timeDisplayer).bind("selectstart",function(){
		return false;
	});
}

Layout.prototype.displayTimePolicy =  function(json){
	/*设置时间策略模式 **/
	$("#timePolicy").text(timePolicySections[json.LBEnabled]);
	var rules = json.disSections.split(",");
	var timeStartor = WIN_WIDTH - 30 ;
	var scale = timeStartor / DAY_TIME_COUNT;
	
	timeDisplayer.style.left = OFFSET_TIME_AXIAS;
	var timeEl = DOC.createElement("div");
	timeEl.className = "enable-peroid-time";
	timeEl.style.width = (scale+.5) * DAY_TIME_COUNT + "px";//展示栏增加半个长度
	timeEl.style.left = 0;
	timeDisplayer.appendChild(timeEl);
	
		
	if(json.LBEnabled == 2){
		this.createTime(["00:00","24:00"],scale);
	}else if(json.LBEnabled == 3){
		/*目前支持的最小刻度为小时，应该支持到分钟级别*/
		for(var i=0; i< rules.length; i++){
			var rule = rules[i].split("-");
			this.createTime(rule,scale);
		}
	}
	var scaleLine = DOC.createElement("div");
	scaleLine.id = "scaleLine";
	for(var i =0 ;i<25;i++){
		var point = DOC.createElement("span");
		point.className = "time-point";
		if(i==24){
			point.style.left = i*scale-10 + "px";//17;
		}else{
			point.style.left = i*scale + "px";
		}
		point.style.top = 3;
		if(i == 0){
			point.innerHTML = "<b>"+i+"</b>"+"<font class=font10>(@CALENDAR.WeeHours@)</font>";
		}else if(i == 12){
			point.innerHTML = "<b>"+i+"</b>"+"<font class=font10>(@CALENDAR.Noon@)</font>";
		}else if(i == 20){
			point.innerHTML = "<b>"+i+"</b>"+"<font class=font10>(@CALENDAR.Nightfall@)</font>";
		}else if(i == 6){
			point.innerHTML = "<b>"+i+"</b>"+"<font class=font10>(@CALENDAR.Morning@)</font>";
		}else{
			point.innerHTML = "<b>"+i+"</b>";
		}
		scaleLine.appendChild(point);
	}
	timeDisplayer.appendChild(scaleLine);
}