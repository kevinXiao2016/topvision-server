function TabIframeSrc(obj){
	this.callBack = obj.callBack;//点击之后要执行的函数，并且传出点击的index;
	this.tabs = obj.tabs;//各个按钮的名称，不能少于2个;
	this.renderTo = obj.renderTo;//渲染到哪个div;
	this.selectedIndex = obj.selectedIndex ? obj.selectedIndex : 0;//初始化选中哪个;
	if(obj.selectedIndex == "first"){ this.selectedIndex = 0};
	var last = obj.tabs.length-1;
	if(obj.selectedIndex == "last" || obj.selectedIndex > last){
		this.selectedIndex = (obj.tabs.length-1);
	}
}
TabIframeSrc.prototype.init = function(){
	var tabsArr = this.tabs;
	var str ='';
	str += '<ul class="whiteTabUl">';
	for(var i=0; i<tabsArr.length; i++){
		if(i==0){
	str +=     '<li><a href="javascript:;" class="first"><span>'+ tabsArr[i].text +'</span></a></li>';	
		}else if(i == tabsArr.length-1){
	str +=     '<li><a href="javascript:;" class="last"><span>'+ tabsArr[i].text +'</span></a></li>';	
		}else{
	str +=     '<li><a href="javascript:;"><span>'+ tabsArr[i].text +'</span></a></li>';		
		}
	}
	$("#" + this.renderTo).html(str);	
	var selecedCls = "selected";
	if(this.selectedIndex == 0 || this.selectedIndex == "first"){
		selecedCls = "selectedFirst";
	}else if(this.selectedIndex == "last" || this.selectedIndex >= (this.tabs.length-1)){
		selecedCls = "selectedLast";	
	}
	$("#" + this.renderTo +" .whiteTabUl li").eq(this.selectedIndex).find("a").addClass(selecedCls);
	$("#" + this.renderTo +" .whiteTabUl").find("li").bind("click",this,this.tabClickLi);
}
TabIframeSrc.prototype.tabClickLi = function(e){//点击li;
	if (!e) e = window.event;
	var index = $(this).index();
	var theUl = $("#" + e.data.renderTo +" .whiteTabUl");
	index = theUl.find("li").index(this);
	
	var lastOne = theUl.find("li").length - 1;
	theUl.find("li a").attr("class","");
	theUl.find("li a:first").attr("class","first");
	theUl.find("li a:last").attr("class","last");
	var cls;
	switch(index){
		case 0:
			cls = "first selectedFirst";
		break;
		case lastOne:
			cls = "last selectedLast";
		break;
		default:
			cls = "selected";
		break;
	};//end switch;
	$(this).find("a").attr("class",cls);
	e.data.callBack(index, e.data);
};//end tabClickLi;

$(function(){
	$("#modeSel").val(window.mode);
	init();
	
	autoHeight();
	$(window).resize(function(){
		//autoHeight();
		throttle(autoHeight,window);
	});//end resize;
});//end document.ready;
//resize事件增加 函数节流;
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}
//根据模式 加载按钮组;
function init(){
	//填写好界面上当前模式是什么;
	$("#putMode").text(IGMP[window.mode].text);
	//根据模式 加载按钮组;
	$("#putBtnGroup").empty();
	var tab1 = new TabIframeSrc({
	    renderTo : "putBtnGroup",
	    tabs     : IGMP[window.mode].tabs,
	    callBack : function(index, me){
	    	var src = me.tabs[index].src;
	    	changeFramePage(src);
	    }
	});
	tab1.init();
	//根据模式加载按钮组第一个按钮的jsp界面;
	changeFramePage(IGMP[window.mode].tabs[0].src);
}

function changeFramePage(src){
	window.frames["pageFrame"].location.href = src;
}
//自动调整
function autoHeight(){
	var $body = $('body'),
	    $pageFrame = $('#pageFrame'),
	    w = $body.width(),
	    h = $body.height() - 96;
	
	if(w < 100){ w = 100}
	if(h < 100){ h = 100}
	$pageFrame.width(w).height(h);
}
//显示或者隐藏遮罩;
function displayMask(display){
	var $mask = $("#mask");
	if($mask.length === 0){
		$('body').append('<div id="mask" class="fakeMask"></div>');
	}
	$("#mask").css({display:display});
}
function changeMode(){
	var modeValue = parseInt($("#modeSel").val(), 10),
	    modeText = IGMP[modeValue].text,
	    concent = String.format('@confirm.changeMode@',IGMP[window.mode].text, modeText);
	
	top.showConfirmDlg('@COMMON.tip@', concent, function(para){
		if(para === 'yes'){
			//在模式切换成功后实现与业务逻辑
			modifyIgmpMode(modeValue);
		}else if(para === 'no'){
			$("#modeSel").val(window.mode);
		}
	});
}

function modifyIgmpMode(igmpMode){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/modifyIgmpMode.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			currentMode : mode,
			igmpMode : igmpMode
		},
		dataType :　'json',
		success : function(json) {
			if(json.result){
				window.mode = parseInt($("#modeSel").val(), 10); 
				init();
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@tip.changeModeS@</b>'
	       	    });
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@tip.changeModeError@");
			}
		},
		error : function(json) {
			$("#modeSel").val(window.mode);
			window.parent.showMessageDlg("@COMMON.tip@", "@tip.changeModeF@");
		},
		cache : false
	});
}

//同步所有IGMP业务数据
function refreshAllIgmpData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshAllIgmpData.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.href = window.location.href;
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}