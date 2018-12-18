//定义一些全局变量
var vlanList, //此OLT下VLAN 列表数据(不分页)
	sniTabInited = false,
	ponTabInited = false,
	uniTabInited = false,
	curTab = 0,
	vlanStore,
	vlanGrid,
	sniVlanstore,
	uniVlanStore,
	uniVlanGrid;

$(function(){
	//初始化切换tab签
	initTabs();
	
	//初始化页面布局
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'header-container',
	        height: 80
	      },
	      {
	    	  region: 'center',
	    	  border: false,
	    	  contentEl: 'center-container'
	      }
	    ]
	});
	
	//TODO resize
	$(window).resize(function() {
		//获取当前center-container的size
		var centerWidth = $('#center-container').width(),
			centerHeight = $('#center-container').height();
		switch(curTab){
		case 0:
			vlanGrid.setSize(centerWidth, centerHeight);
			break;
		case 1:
			sniVlanGrid.setSize(centerWidth, centerHeight);
			break;
		case 2:
			if(typeId != 10007){
				ponVlanGrid.setSize(centerWidth, centerHeight);
			}else{
				uniVlanGrid.setSize($('#uniGridPanelContainer').width(), $('#uniGridPanelContainer').height());
			}
			break;
		case 3:
			uniVlanGrid.setSize($('#uniGridPanelContainer').width(), $('#uniGridPanelContainer').height());
			break;
		}
	});
	
	//初始化VLAN列表
	initVlanTab();
})

/**
 * 初始化tab
 */
function initTabs(){
	if(typeId != 10007){
		(new Nm3kTabBtn({
			renderTo:"putTab",
			callBack:"switchTab",
			tabs:['@VLAN.vlanList@', '@VLAN.sniVlan@', '@VLAN.ponVlan@', '@VLAN.uniVlan@']
		})).init();
	}else{
		(new Nm3kTabBtn({
			renderTo:"putTab",
			callBack:"switchTab",
			tabs:['@VLAN.vlanList@', '@VLAN.sniVlan@', '@VLAN.uniVlan@']
		})).init();
	}
}

/**
 * 切换tab
 * @param {Integer} index tab在tab签中的index
 */
function switchTab (index) {
	$(".tabBody").css("display","none");
	curTab = index;
	switch(index){
	case 0:
		$(".tabBody").eq(index).fadeIn();
		refreshVlanList();
		break;
	case 1:
		$(".tabBody").eq(index).fadeIn();
		sniTabInited ? refreshSniPortList() : createSniPortList();
		break;
	case 2:
		if(typeId != 10007){
			$(".tabBody").eq(index).fadeIn();
			ponTabInited ? refreshPonList() : initPonTab();
		}else{
			$(".tabBody").eq(3).fadeIn();
			uniTabInited ? refreshUniVlanList() : createUniVlanList();
		}
		break;
	case 3:
		$(".tabBody").eq(index).fadeIn();
		uniTabInited ? refreshUniVlanList() : createUniVlanList();
		break;
	}
}