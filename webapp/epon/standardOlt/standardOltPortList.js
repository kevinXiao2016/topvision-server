var ponColumn,ponStore,ponGrid,sniCloumn,sniStore,sniGrid;
var grid1_height = null, grid2_height = null;

//自适应设置列表的宽度
function autoHeight(){
	var w = $(window).width();
	ponGrid.setWidth(w);
	sniGrid.setWidth(w);
};//end autoHeight;

//resize事件增加函数节流
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}

function renderStatus(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			"@resources/COMMON.offline@");
	}
}

function renderMode(value, p, record){
	switch(value){
		case 1: return "Auto-Negotiate";
		case 2: return "Half-10M";
		case 3: return "Full-10M";
		case 4: return "Half-100M";
		case 5: return "Full-100M";
		case 6: return "Full-1000M";
		case 7: return "Full-10000M";
	}
}

function renderMediaType(value, p, record){
	switch(value){
		case 1: return "TwistedPair";
		case 2: return "Fiber";
		case 3: return "Other";
		default: return "Other";
	}
}

function renderPerfEnable(value, p, record){
	if(value == 1){
		return str = '<img src="/images/performance/on2.png">';
	}else if(value == 2){
		return str = '<img src="/images/performance/off2.png">';
	}else{
		return "--";
	}
}

function renderPonType(value, p, record){
	switch(value){
		case 1: return "Ge-epon";
		case 2: return "Tenge-epon";
		case 3: return "Gpon";
		case 4: return "Other";
		default: return "Other";
	}
}

function renderSniNo(value, p, record){
	return record.data.slotNo + "/" + record.data.sniNo;
}

function renderPonNo(value, p, record){
	return record.data.slotNo + "/" + record.data.ponNo;
}

function renderPonName(value, p, record){
	return "PORT-" + record.data.slotNo + "/" + record.data.ponNo;
}

$(document).ready(function() {
	$("#tb1_tit").click(function(){
		var $me = $(this),
		    $table = $("#tb1");
		if($me.hasClass("flagOpen")){
			$me.attr({"class":"flagClose"});
			$table.stop().fadeOut();
		}else if($me.hasClass("flagClose")){
			$me.attr({"class":"flagOpen"});
			$table.stop().fadeIn(function(){
				$table.css("opacity",1);
			});
		}
	});//end click;
	
	/********************************************************************
		加载SNI口信息
	*******************************************************************/
	sniCloumn = new Ext.grid.ColumnModel([
		{header:"@ELEC.portNo@", dataIndex:"sniLocation", width:55, renderer : renderSniNo},
		{header:"<div class='txtCenter'>@VLAN.portName@</div>", dataIndex:"sniPortName", align:"left"},
		{header:"@COMMON.status@", dataIndex:"sniOperationStatus", width:60, renderer : renderStatus},
		{header:"@EPON.mediaType@", dataIndex:"sniMediaType", width:70, renderer:  renderMediaType},
		{header:"<div class='txtCenter'>@REALTIME.negoMode@</div>", dataIndex:"sniAutoNegotiationMode", width:80, renderer : renderMode, align:"left"},
		{header:"@EPON.stasticPerfEnable@", dataIndex:"sniPerfStats15minuteEnable",width:80, renderer : renderPerfEnable},
		{header:"@SERVICE.macMaxLearn@", dataIndex:"sniMacAddrLearnMaxNum",width:80}
	]);//end cm;
	
	sniStore = new Ext.data.JsonStore({
		url : '/epon/standardOlt/loadSniList.tv',
		fields : ["slotNo","sniNo", "sniPortName", "sniMediaType", "sniOperationStatus","sniAutoNegotiationMode", "sniPerfStats15minuteEnable", "sniMacAddrLearnMaxNum"],
		baseParams : {
			entityId : entityId
		},
		listeners:{
			load:function(){
				grid2_height = sniStore.data.items.length * 32 + 85;
				if(sniGrid){
					sniGrid.setHeight(grid2_height);
				}
			}
		}
	});

	sniGrid = new Ext.grid.GridPanel({
		renderTo: 'putSniGrid',
		title: "<label class='extGridTit flagOpen'>@PERF.sniPortList@</label>",
		height:200,
		stripeRows:true,
		enableColumnMove: false,
		enableColumnResize: true,
		cls: 'normalTable edge10',
		store: sniStore,
		cm : sniCloumn,
		loadMask : {
        msg :'@entitySnapPage.loading@'
    },
		viewConfig:{
			forceFit: true
		}
	});
	sniStore.load();
	
	/********************************************************************
		加载PON口信息
	*******************************************************************/
	ponColumn = new Ext.grid.ColumnModel([
  		{header:"@VLAN.portNum@", dataIndex:"slotNo", width:55, renderer: renderPonNo},
  		{header:"<div class='txtCenter'>@VLAN.portName@</div>", dataIndex:"slotNo", align:"left",width:60, renderer: renderPonName},
  		{header:"@COMMON.status@", dataIndex:"ponOperationStatus",width:60, renderer : renderStatus},
  		{header:"@VLAN.portType@", dataIndex:"ponPortType", width:80, renderer: renderPonType},
  		{header:"@EPON.stasticPerfEnable@", dataIndex:"perfStats15minuteEnable",width:80, renderer : renderPerfEnable},
  		{header:"@REALTIME.downLink@", dataIndex:"ponPortUpOnuNum", width:80}
  		
  	]);//end cm;
	ponStore = new Ext.data.JsonStore({
		url : '/epon/standardOlt/loadPonList.tv',
		fields : ["slotNo", "ponNo", "ponOperationStatus", "ponPortType", "perfStats15minuteEnable", "ponPortUpOnuNum"],
		baseParams : {
			entityId : entityId
		},
		listeners:{
			load:function(){
				grid1_height = ponStore.data.items.length * 32 + 85; 
				if(ponGrid){
					ponGrid.setHeight(grid1_height);
				}
			}
		}
	});
 	
  	ponGrid = new Ext.grid.GridPanel({
  		renderTo: 'putPonGrid',
  		title: "<label class='extGridTit flagOpen'>@onuAutoUpg.he.ponList@</label>",
  		height: 200,
  		stripeRows:true,
  		enableColumnMove: false,
  		enableColumnResize: true,
  		cls: 'normalTable pL10 pR10',
  		store: ponStore,
  		cm : ponColumn,
  		loadMask : {
             msg :'@entitySnapPage.loading@'
        },
  		viewConfig:{
  			forceFit: true
  		}
  	});
  	ponStore.load();
  	
  	$(window).wresize(function(){
		throttle(autoHeight,window)
	});//end resize;
  	
  	$(".extGridTit").click(function(){
		var $me = $(this),
		    $table = $me.parent().parent().next();
		if($me.hasClass("flagOpen")){
			$me.attr({"class" : "flagClose extGridTit"});
			$table.stop().fadeOut();
		}else if($me.hasClass("flagClose")){
			$me.attr({"class" : "flagOpen extGridTit"});
			$table.stop().fadeIn(function(){
				$table.css("opacity",1);
			});
		};//end if;
	});//end click;
});
