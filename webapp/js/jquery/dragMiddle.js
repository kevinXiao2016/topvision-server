/*
 * 拖拽左侧和右侧，控制左右列宽;
 * 
 * 
 *  var o1 = new DragMiddle({ id: "line1", leftId: "areaA", rightId: "areaB", minWidth: 120, maxWidth:300,leftBar:true });
 *  o1.init();
 *  
 *  
 *  var o2 = new DragMiddle({ id: "line2", leftId: "areaB", rightId: "areaC", minWidth:120, maxWidth: 300, rightBar:true });
 *	o2.init(); 
 * 
 */


function DragMiddle(obj) {
	this.leftOrRight = null;//操作的是左侧的line，还是右侧的line.左侧的line则控制的是left,右侧控制的是right属性;
	if(obj.leftBar){
		this.leftOrRight = "left";
	}else if(obj.rightBar){
		this.leftOrRight = "right";
	}else{
		alert("传入参数有错误，leftBar和rightBar必须传入一个!");
	}
	this.dragAble = false; //当前拖动状态,按下鼠标变成true,说明可以拖动了;
	this.xpos = 0; //点击下去与实际的div的x差值;
	this.targetId = obj.id; //存储当前对象的id;
	this.leftId = obj.leftId;
	this.rightId = obj.rightId;
	this.minWidth = obj.minWidth; //最小宽度;
	this.maxWidth = obj.maxWidth;//最大宽度;
	this.leftCallBack = obj.leftCallBack ? obj.leftCallBack : null;//左侧拖动后的回调;
	
	if(typeof this.init != "function"){			
		DragMiddle.prototype.init = function () {
			$("#" + this.targetId).bind("mousedown", this, this.downFn);
		}; //end init;
		
		DragMiddle.prototype.die = function (){
			$("#" + this.targetId).unbind("mousedown");
		};//end die;
		
		DragMiddle.prototype.downFn = function (e) {//鼠标按下去;				
			if (!e) e = window.event;				
			var w = $("#" + e.data.targetId).width();
			var h = $("#" + e.data.targetId).height();
			var t = $("#" + e.data.targetId).offset().top;
			var strDiv = '';
			var fullScreenDiv = '';
			fullScreenDiv += '<div id="fullScreenDiv" style="width:100%; height:100%; position:absolute; left:0; top:0; background:transparent; z-index:999998;">&nbsp;</div>';
			//如果是控制左侧的line;
			if(e.data.leftOrRight == "left"){
				e.data.xpos = e.clientX - $("#" + e.data.targetId).offset().left;		
				var l = $("#" + e.data.targetId).offset().left;							
				strDiv += '<div id="fakeDiv" style="position:absolute; width:' + w + 'px; height:' + h + 'px; left:' + l + 'px; top:' + t + 'px; background:#ccc; z-index:999999;"></div>';
			}else if(e.data.leftOrRight == "right"){//如果是控制左侧的line;
				e.data.xpos = $("#" + e.data.targetId).width() - (e.clientX - $("#" + e.data.targetId).offset().left);
				var r = $("body").innerWidth() - $("#" + e.data.targetId).offset().left - $("#" + e.data.targetId).width();
				strDiv += '<div id="fakeDiv" style="position:absolute; width:' + w + 'px; height:' + h + 'px; right:' + r + 'px; top:' + t + 'px; background:#ccc; z-index:999999;"></div>';
			};//end if;
			
			$("body").append(fullScreenDiv).append(strDiv);
			e.data.dragAble = true;
			$("body").bind("mousemove", e.data, e.data.moveFn);
			$("body").bind("mouseup", e.data, e.data.upFn);
			$("body").bind("mouseleave", e.data, e.data.upFn);
			return false;//解决chrome拖拽的时候会选中的问题;
		}; //end downFn;
		
		DragMiddle.prototype.moveFn = function (e) {//鼠标移动;
			if (e.data.dragAble) {
				if (!e) e = window.event;					
				//如果是控制左侧的line;
				if(e.data.leftOrRight == "left"){
					var mouseL = e.clientX - e.data.xpos;					
					if (mouseL < e.data.minWidth) {
						mouseL = e.data.minWidth;
					}
					if (mouseL > e.data.maxWidth) {
						mouseL = e.data.maxWidth;
					}
					$("#fakeDiv").css("left", mouseL);
				}else if(e.data.leftOrRight == "right"){
					var mouseL = $("body").innerWidth() - e.clientX - e.data.xpos;					
					if (mouseL < e.data.minWidth) {
						mouseL = e.data.minWidth;
					}
					if (mouseL > e.data.maxWidth) {
						mouseL = e.data.maxWidth;
					}
					$("#fakeDiv").css("right", mouseL);
				};//end if;
			};//end if;
		} //end moveFn;
		
		DragMiddle.prototype.upFn = function (e) {//鼠标松开;
			$("body").unbind("mousemove");
			$("body").unbind("mouseup");
			$("body").unbind("mouseleave");

			e.data.dragAble = false;
			$("#fullScreenDiv").remove();
			//如果是控制左侧的line;
			if(e.data.leftOrRight == "left"){
				var l = $("#fakeDiv").offset().left;
				$("#fakeDiv").remove();
				$("#" + e.data.targetId).css("left", l);
				$("#" + e.data.leftId).css("width",l);
				var rightEdge = $("#" + e.data.targetId).width() + $("#" + e.data.leftId).width();
				$("#" + e.data.rightId).css("marginLeft",rightEdge);
				if(e.data.leftCallBack != null && typeof(e.data.leftCallBack) === 'function'){
					e.data.leftCallBack();
				}
			}else if(e.data.leftOrRight == "right"){
				var r = $("body").innerWidth() - $("#fakeDiv").offset().left - $("#" + e.data.targetId).width();
				$("#fakeDiv").remove();
				$("#" + e.data.targetId).css("right", r);
				$("#" + e.data.rightId).css("width",r);
				var rightEdge = $("#" + e.data.targetId).width() + $("#" + e.data.rightId).width();
				$("#" + e.data.leftId).css("marginRight",rightEdge);
			};//end if;
			
		} //end upFn;
		
	};/*end if*/	
}//end DragMiddle;


/****
 * var o1 = new DragLee({ id: "line1", leftId: "areaA", rightId: "areaB", nextLine: "line2", minWidth: 120, lineEdge: 50 });
	 o1.init();

	 var o2 = new DragLee({ id: "line2", leftId: "areaB", rightId: "areaC", preLine: "line1", maxWidth: 1000, lineEdge: 50 });
	 o2.init();
 */
function DragLee(obj) {
	this.dragAble = false; //当前拖动状态,按下鼠标变成true,说明可以拖动了;
	this.xpos = 0; //点击下去与实际的div的x差值;
	this.targetId = obj.id; //存储当前对象的id;
	this.leftId = obj.leftId;
	this.rightId = obj.rightId;
	this.minWidth = obj.minWidth; //最小宽度;
	this.lineEdge = obj.lineEdge; //两更拖动竖杆之间最小相差多少距离;
	this.leftCallBack = obj.leftCallBack ? obj.leftCallBack : null;//左侧拖动后的回调;

	if (obj.preLine) { this.preLine = obj.preLine; } //上一个可以拉动的竖杆;
	if (obj.nextLine) { this.nextLine = obj.nextLine; } //下一个可以拉动的竖杆;
	if (obj.maxWidth) {
		this.maxWidth = obj.maxWidth; //最大宽度;
	} else {
		obj.maxWidth = null;
	}
	
	
	if(typeof this.init != "function"){			
		DragLee.prototype.init = function () {
			$("#" + this.targetId).bind("mousedown", this, this.downFn);
		}; //end init;
		
		DragLee.prototype.die = function () {
			$("#" + this.targetId).unbind("mousedown");			
		}
		
		DragLee.prototype.downFn = function (e) {//鼠标按下去;	
			if (!e) e = window.event;
			e.data.xpos = e.clientX - $("#" + e.data.targetId).offset().left;
			var w = $("#" + e.data.targetId).width();
			var h = $("#" + e.data.targetId).height();
			var l = $("#" + e.data.targetId).offset().left;
			var t = $("#" + e.data.targetId).offset().top;

			var strDiv = '';
			strDiv += '<div id="fakeDiv" style="position:absolute; width:' + w + 'px; height:' + h + 'px; left:' + l + 'px; top:' + t + 'px; background:#ccc; z-index:999999;"></div>';
			var fullScreenDiv = '';
			fullScreenDiv += '<div id="fullScreenDiv" style="width:100%; height:100%; position:absolute; left:0; top:0; background:transparent; z-index:999998;">&nbsp;</div>';
			$("body").append(fullScreenDiv).append(strDiv);
			e.data.dragAble = true;
			$("body").bind("mousemove", e.data, e.data.moveFn);
			$("body").bind("mouseup", e.data, e.data.upFn);
			$("body").bind("mouseleave", e.data, e.data.upFn);
			return false;
		}; //end downFn;
		
		DragLee.prototype.moveFn = function (e) {//鼠标移动;
			if (e.data.dragAble) {
				//$("#fullScreenDiv").text((e.clientX).toString())
				if (!e) e = window.event;
				var mouseL = e.clientX - e.data.xpos;

				if (e.data.preLine) {
					e.data.minWidth = $("#" + e.data.preLine).offset().left + e.data.lineEdge;
				}
				if (mouseL < e.data.minWidth) {
					mouseL = e.data.minWidth;
				}
				if (e.data.nextLine) {
					e.data.maxWidth = $("#" + e.data.nextLine).offset().left - e.data.lineEdge;
				}
				if (mouseL > e.data.maxWidth) {
					mouseL = e.data.maxWidth;
				}
				$("#fakeDiv").css("left", mouseL);
				//$("#fullScreenDiv").find("input").focus();
			}
		} //end moveFn;
		
		DragLee.prototype.upFn = function (e) {//鼠标松开;
			$("body").unbind("mousemove");
			$("body").unbind("mouseup");
			$("body").unbind("mouseleave");

			e.data.dragAble = false;
			var l = $("#fakeDiv").offset().left;
			//$("#" + e.data.id).css("left", l);
			
			$("#" + e.data.targetId).css("left", l);
			if (e.data.nextLine) {
				$("#" + e.data.leftId).css("width", l);
				var rightIdLeft = $("#" + e.data.targetId).width() + l;
				$("#" + e.data.rightId).css("left", rightIdLeft);
				var rightIdWidth = $("#" + e.data.nextLine).offset().left - $("#" + e.data.targetId).offset().left - $("#" + e.data.targetId).width();
				$("#" + e.data.rightId).css("width", rightIdWidth);
			} else {
				var leftIdWidth = $("#" + e.data.targetId).offset().left - $("#" + e.data.preLine).offset().left - $("#" + e.data.targetId).width();
				$("#" + e.data.leftId).css("width", leftIdWidth);
				var rightIdMargin = $("#" + e.data.targetId).offset().left + $("#" + e.data.targetId).width();
				$("#" + e.data.rightId).css("marginLeft", rightIdMargin);
			}
			
			$("#fakeDiv").remove();
			$("#fullScreenDiv").remove();
			if(e.data.leftCallBack != null && typeof(e.data.leftCallBack) === 'function'){
				e.data.leftCallBack();
			}
		} //end upFn;
		
	};/*end if 这里init()方法不存在的情况下，才会添加到原型中，
	这段代码只会在初次调用构造函数时才会执行。
	此后，原型已经完成了初始化。其中if语句检查的是初始化之后应该存在任何属性和方法，只要检测一个就可以了。
	对于这种模式创建的对象，还可以使用instanceof 操作符确定它的类型 ;*/		
}//end dragLee;


//TabLee;
/**
 * 选项卡，宽度超出自动出现左右箭头;
 */

function TabLee(topId,bottomId,selectedClass,speed){//上部分的id,下部分的id,选中选项卡的id,速度（1最快）;
	this.topId = topId;//选项卡上部分的id;
	this.bottomId = bottomId;//下部分的id;
	this.speed = speed;
	this.selectedClass = selectedClass;//选中的class;
	this.downAble = false;
	this.movement = null;
	this.oldThis = null;
};//end TabLee;

TabLee.prototype.init = function(){			
	$("#"+ this.bottomId +" .tabContentDiv:eq(0)").css("display","block");
	$("#"+ this.topId +" table tr td:eq(0) a").addClass("tabSelected");	
	//第一次执行autoWidth;
	var tabW = $("#"+ this.topId).innerWidth();			
	//计算tabMiddle的宽度,table的宽度，进行比较;
	var tabMiddleW = $("#"+ this.topId +" .tabMiddle").innerWidth();
	var tableW = $("#"+ this.topId +" table").innerWidth();
	if(tableW > tabW){//要出现左右箭头的情况;
		//这里还要考虑left的情况;
		if(tabW < 40) tabW = 40;
		$("#"+ this.topId +" .tabArrLeft, #"+ this.topId +" .tabArrRight").css("display","block");
		var arrWidth = $("#"+ this.topId +" .tabArrLeft").innerWidth();
		tabMiddleW = tabW - arrWidth * 2;
		$("#"+ this.topId +" .tabMiddle").width(tabMiddleW).css("left",arrWidth);				
		var tableLeftPos = $("#"+ this.topId +" table").position().left;
		if(tableLeftPos<0){
			tableLeftPos = -tableLeftPos;
		}
		var tableCanSeeW = $("#"+ this.topId +" table").innerWidth() - tableLeftPos;				
		if(tableCanSeeW < tabMiddleW){
			var toRight = $("#"+ this.topId +" table").innerWidth() - tabMiddleW;
			$("#"+ this.topId +" table").css("left",-toRight);
		};//end if;
	}else{//不出现左右箭头的情况;
		$("#"+ this.topId +" .tabArrLeft, #"+ this.topId +" .tabArrRight").css("display","none");
		$("#"+ this.topId +" .tabMiddle").width(tabW).css("left",0);
		$("#"+ this.topId +" table").css("left",0);
	};//end if else;
	//autoWidth执行结束;
	this.oldThis = this;
	$(window).bind("resize",this,this.autoWidth);
	$("#"+ this.topId +" table a").bind("click",this,this.tabAlinkClick);//点击选项卡上的a标签;
	$("#"+ this.topId +" .tabArrRight").bind("mousedown",this,this.rightArrDown);//点击右侧arrow,通过setTimeout持续向左移动;
	$("#"+ this.topId +" .tabArrRight").bind("mouseup",this,this.stopMove);	
	$("#"+ this.topId +" .tabArrRight").bind("mouseleave",this,this.stopMove);
	$("#"+ this.topId +" .tabArrLeft").bind("mousedown",this,this.leftArrDown);//点击右侧arrow,通过setTimeout持续向左移动;
	$("#"+ this.topId +" .tabArrLeft").bind("mouseup",this,this.stopMove);	
	$("#"+ this.topId +" .tabArrLeft").bind("mouseleave",this,this.stopMove);
};//end init;

TabLee.prototype.autoWidth = function(e){
	if(!e) e = window.event;
	var _this = e.data;
	//计算出外部包含选项卡的div的宽度;
	var tabW = $("#"+ _this.topId).innerWidth();			
	//计算tabMiddle的宽度,table的宽度，进行比较;
	var tabMiddleW = $("#"+ _this.topId +" .tabMiddle").innerWidth();
	var tableW = $("#"+ _this.topId +" table").innerWidth();
	if(tableW > tabW){//要出现左右箭头的情况;
		//alert("要出现左右箭头的")
		//这里还要考虑left的情况;
		if(tabW < 40) tabW = 40;
		$("#"+ _this.topId +" .tabArrLeft, #"+ _this.topId +" .tabArrRight").css("display","block");
		var arrWidth = $("#"+ _this.topId +" .tabArrLeft").innerWidth();
		tabMiddleW = tabW - arrWidth * 2;
		$("#"+ _this.topId +" .tabMiddle").width(tabMiddleW).css("left",arrWidth);				
		var tableLeftPos = $("#"+ _this.topId +" table").position().left;
		if(tableLeftPos<0){
			tableLeftPos = -tableLeftPos;
		}
		var tableCanSeeW = $("#"+ _this.topId +" table").innerWidth() - tableLeftPos;				
		if(tableCanSeeW < tabMiddleW){
			var toRight = $("#"+ _this.topId +" table").innerWidth() - tabMiddleW;
			$("#"+ _this.topId +" table").css("left",-toRight);
		};//end if;
	}else{//不出现左右箭头的情况;
		//alert("不出现左右箭头的情况")
		$("#"+ _this.topId +" .tabArrLeft, #"+ _this.topId +" .tabArrRight").css("display","none");
		$("#"+ _this.topId +" .tabMiddle").width(tabW).css("left",0);
		$("#"+ _this.topId +" table").css("left",0);
	};//end if else;
};//end autoWidth();


TabLee.prototype.tabAlinkClick = function(e){//点击选项卡上的a标签;
	if(!e) e = window.event;
	var _this = e.data;
	var aIndex = $("#"+ _this.topId +" table a").index($(this));
	$("#"+ _this.topId +" table a").removeClass("tabSelected");
	$(this).addClass("tabSelected");
	$("#"+ _this.bottomId +" .tabContentDiv").css("display","none");
	$("#"+ _this.bottomId +" .tabContentDiv").eq(aIndex).css("display","block");
};//end tabAlinkClick;
TabLee.prototype.stopMove = function(e){//停止移动;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = false;
	if(_this.movement){
		clearTimeout(_this.movement);
	}
}
TabLee.prototype.rightArrDown = function(e){//点击右侧箭头;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = true;
	_this.movement = setTimeout(createDelegate(_this,_this.rightArrDown2),_this.speed);
			
};//end rightArrDown;

TabLee.prototype.rightArrDown2 = function(){
	if(this.movement){
		clearTimeout(this.movement);
	}
	if(this.downAble){
		var leftPos = $("#"+ this.topId +" table").position().left;
			var newLeftPos = leftPos-1;
			var tableW = $("#"+ this.topId +" table").innerWidth();
			var tableMiddleW = $("#"+ this.topId +" .tabMiddle").innerWidth();
			var minLeft = tableMiddleW - tableW;
			if(minLeft >= newLeftPos){
				newLeftPos = minLeft;
				$("#"+ this.topId +" table").css("left",newLeftPos);
				this.movement = null;
				return;
			}
			$("#"+ this.topId +" table").css("left",newLeftPos);
			this.movement = setTimeout(createDelegate(this,this.rightArrDown2),this.speed);
	}else{
		this.movement = null;
	};//end if;
};//end rightArrDown2;

TabLee.prototype.leftArrDown = function(e){//点击左侧箭头;
	if(!e) e = window.event;
	var _this = e.data;
	_this.downAble = true;
	_this.movement = setTimeout(createDelegate(_this,_this.leftArrDown2),_this.speed);
			
};//end leftArrDown;
TabLee.prototype.leftArrDown2 = function(e){
	if(this.movement){
		clearTimeout(this.movement);
	}
	if(this.downAble){
		var leftPos = $("#"+ this.topId +" table").position().left;
		var newLeftPos = leftPos + 1;
		if(newLeftPos >= 0){
			newLeftPos = 0;
			$("#"+ this.topId +" table").css("left",newLeftPos);
			movement = null;
			return;
		}
		$("#"+ this.topId +" table").css("left",newLeftPos);
		this.movement = setTimeout(createDelegate(this,this.leftArrDown2),this.speed);
	}else{
		this.movement = null;
	};//end if;
}		

function createDelegate(object, method) {
    var delegate = function(){
		method.call(object);
	}
     return delegate;
};//end createDelegate;




/***
 * 
 * @param id 选项卡ul元素的id;
 * @param fn 点击之后要执行的函数，并且传出点击的index;
 * @returns
 * 要配合系统中 class="whiteTabUl" 那个ul代码使用; 
 * 这段代码在引用jquery-1.3的时候获取index()不正确，必须用1.6,通过id下面去find li修复了这个问题;
 */
function TabBtn(id,fn){
	this.id = id;
	this.fn = fn;
}

TabBtn.prototype.init = function(){
	$("#"+ this.id).find("li").bind("click",this,this.tabClickLi);
}

TabBtn.prototype.tabClickLi = function(e){//点击li;
	if (!e) e = window.event;
	var index = $(this).index();
	index = $("#" + e.data.id).find("li").index(this);
	var lastOne = $("#"+e.data.id).find("li").length - 1;
	$("#"+e.data.id).find("li a").attr("class","");
	$("#"+e.data.id).find("li a:first").attr("class","first");
	$("#"+e.data.id).find("li a:last").attr("class","last");
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
	var _fn = e.data.fn;
	window[_fn].call(this,index);
//	eval( _fn +"("+ index +")");
//  window[_fn](index);			
};//end tabClickLi;






//////////////////////////////时间记录控件/////////////////////////////////////

/*
**
**var clock6 = new Nm3kClock({
**			renderTo: 'seven',//要渲染到哪个div的id;
**			startTime: 4678915,//开始时间（秒数）;
**			type: 'cir',//类型：cir,grayCalendar,whiteCalendar;
**			language: 'Chinese'//语言，Chinese,English;
**		});
**		clock6.init();
**
**
*/
function Nm3kClock(obj){
	this.renderTo = obj.renderTo; //渲染到某个div的id;
	this.startTime = obj.startTime;// 开始时间，以秒计算;		
	this.movement = null;
	this.type = obj.type;//灰色grayCalendar;
	this.language = "Chinese";//默认语言为中文;
	this.language = (obj.language == "English") ? "English" : "Chinese";

}
Nm3kClock.prototype.constructor = "Nm3kClock";
Nm3kClock.prototype.init = function(){
	var str = '';
	
	switch(this.type){
		case "grayCalendar":
			if(this.language == "English"){//如果是英文;
				str+= '<div class="nm3kClockContainerEnglish">';
			}else{//中文;
				str+= '<div class="nm3kClockContainer">';
			};//end if;
			
			str+= '	<ul class="nm3kGrayClock">';
			str+= '		<li class="nm3kGrayClockDay2 dayContainer">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockHour">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockMin">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '		<li class="nm3kGrayClockSec">';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '			<div class="nm3kGrayClockNum0"></div>';
			str+= '		</li>';
			str+= '	</ul>';
			str+= '</div>';
		break;
		case "whiteCalendar":
			if(this.language == "English"){//如果是英文;
				str += '<div class="nm3kWhiteClockContainerEnglish">';			
			}else{
				str += '<div class="nm3kWhiteClockContainer">';
			}
			str += '	<ul class="nm3kWhiteClock">';
			str += '		<li class="nm3kWhiteClockFirst"></li>';
			str += '		<li class="nm3kWhiteClockDay2 dayContainer">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockHour">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockMin">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockSec">';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '			<div class="nm3kWhiteClockNum0"></div>';
			str += '		</li>';
			str += '		<li class="nm3kWhiteClockLast"></li>';
			str += '	</ul>';
			str += '</div>';
		break;
		case "cir":
			str += '<div class="nm3kCirClockContainer">';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirDayNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirDaysEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirDays"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirHourNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirHoursEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirHours"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirMinNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirMinsEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirMins"></div>';
			}
			str += '	</div>';
			str += '	<div class="nm3kCirBg">';
			str += '		<ul class="nm3kCirNumUl nm3kCirSecNum">';
			str += '			<li class="nm3kCirNum0"></li>';
			str += '			<li class="nm3kCirNum0"></li>';				
			str += '		</ul>';
			if(this.language == "English"){
			str += '		<div class="nm3kCirSecsEnglish"></div>';
			}else{
			str += '		<div class="nm3kCirSecs"></div>';
			}
			str += '	</div>';
			str += '</div>';
		break;
	}
	$("#"+this.renderTo).html(str);
	this.movement = setInterval(createDelegate(this,this.addSeconds),1000);
};// end init;
Nm3kClock.prototype.addSeconds = function(){
	this.startTime++;
	var day = parseInt(this.startTime / 60 / 60 / 24);
	var hour = parseInt((this.startTime / 60 / 60) % 24);
	var mins = parseInt((this.startTime / 60 ) % 60);
	var sec = parseInt((this.startTime) % 60);
	
	var day4 = parseInt(day / 1000);
	var day3 = parseInt(day % 1000 / 100);
	var day2 = parseInt(day % 100 / 10);
	var day1 = parseInt(day % 10);
	var hour2 = parseInt(hour / 10);
	var hour1 = parseInt(hour % 10);
	var min2 = parseInt(mins / 10);
	var min1 = parseInt(mins % 10);
	var sec2 = parseInt(sec / 10);
	var sec1 = parseInt(sec % 10);
	
	switch(this.type){
		case "grayCalendar":
			var secStr = '<div class="nm3kGrayClockNum'+ sec2 +'"></div><div class="nm3kGrayClockNum'+ sec1 +'"></div>';
			$("#"+ this.renderTo +" .nm3kGrayClockSec").html(secStr);
			var minStr = '<div class="nm3kGrayClockNum'+ min2 +'"></div><div class="nm3kGrayClockNum'+ min1 +'"></div>';
			$("#"+ this.renderTo +" .nm3kGrayClockMin").html(minStr);
			var hourStr = '<div class="nm3kGrayClockNum'+ hour2 +'"></div><div class="nm3kGrayClockNum'+ hour1 +'"></div>'; 
			$("#"+ this.renderTo +" .nm3kGrayClockHour").html(hourStr);
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay2 dayContainer").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<div class="nm3kGrayClockNum'+ day3 +'"></div><div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay3 dayContainer").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<div class="nm3kGrayClockNum'+ day4 +'"></div><div class="nm3kGrayClockNum'+ day3 +'"></div><div class="nm3kGrayClockNum'+ day2 +'"></div><div class="nm3kGrayClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kGrayClockDay4 dayContainer").html(dayStr);
			};//end if day4;
		break;
		case "whiteCalendar":
			var secStr = '<div class="nm3kWhiteClockNum'+ sec2 +'"></div><div class="nm3kWhiteClockNum'+ sec1 +'"></div>';
			var minStr = '<div class="nm3kWhiteClockNum'+ min2 +'"></div><div class="nm3kWhiteClockNum'+ min1 +'"></div>';
			var hourStr = '<div class="nm3kWhiteClockNum'+ hour2 +'"></div><div class="nm3kWhiteClockNum'+ hour1 +'"></div>'; 
			
			$("#"+ this.renderTo +" .nm3kWhiteClockSec").html(secStr);				
			$("#"+ this.renderTo +" .nm3kWhiteClockMin").html(minStr);
			$("#"+ this.renderTo +" .nm3kWhiteClockHour").html(hourStr);
			
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay2 dayContainer").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<div class="nm3kWhiteClockNum'+ day3 +'"></div><div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay3 dayContainer").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<div class="nm3kWhiteClockNum'+ day4 +'"></div><div class="nm3kWhiteClockNum'+ day3 +'"></div><div class="nm3kWhiteClockNum'+ day2 +'"></div><div class="nm3kWhiteClockNum'+ day1 +'"></div>';
					$("#"+ this.renderTo +" .dayContainer").attr("class","nm3kWhiteClockDay4 dayContainer").html(dayStr);
			};//end if day4;				
		break;
		case "cir":
			var secStr = '<li class="nm3kCirNum'+ sec2 +'"></li><li class="nm3kCirNum'+ sec1 +'"></li>';
			var minStr = '<li class="nm3kCirNum'+ min2 +'"></li><li class="nm3kCirNum'+ min1 +'"></li>';
			var hourStr = '<li class="nm3kCirNum'+ hour2 +'"></li><li class="nm3kCirNum'+ hour1 +'"></li>'; 				
			
			var parentClsSec = this.getLoading(sec);
			var parentClsMin = this.getLoading(mins);
			var parentClsHour = this.getLoading(hour);
			
			$("#"+ this.renderTo +" .nm3kCirSecNum").html(secStr).parent().attr("class",parentClsSec);
			$("#"+ this.renderTo +" .nm3kCirMinNum").html(minStr).parent().attr("class",parentClsMin);
			$("#"+ this.renderTo +" .nm3kCirHourNum").html(hourStr).parent().attr("class",parentClsHour);
			
			var dayStr = '';
			if(day4 == 0){
				if(day3 == 0){//天数为2位数字;				
					dayStr = '<li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);
				}else{//天数为3位数字;
					dayStr = '<li class="nm3kCirNum'+ day3 +'"></li><li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);;
				};//end if day3;
			}else{//天数为4位数字;
					dayStr = '<li class="nm3kCirNum'+ day4 +'"></li><li class="nm3kCirNum'+ day3 +'"></li><li class="nm3kCirNum'+ day2 +'"></li><li class="nm3kCirNum'+ day1 +'"></li>';
					$("#"+ this.renderTo +" .nm3kCirDayNum").html(dayStr);
			};//end if day4;
			if(day != 0){
				$("#"+ this.renderTo +" .nm3kCirDayNum").parent().attr("class","nm3kCirBg1");
			}
		break;
	}
	
	
	
	
};//end addSeconds;
Nm3kClock.prototype.stopRecord = function(){
	clearInterval(this.movement);
	this.movement = null;
};//end stopRecord;

Nm3kClock.prototype.getLoading = function(para){
	var parentClassStr = "";
	if(para >= 1 && para <= 7){
		parentClassStr = "nm3kCirBg1";
	}else if(para >= 8 && para <= 22){
		parentClassStr = "nm3kCirBg2";
	}else if(para >= 23 && para <= 37){
		parentClassStr = "nm3kCirBg3";
	}else if(para >= 38 && para <= 59){
		parentClassStr = "nm3kCirBg4";
	}else{
		parentClassStr = "nm3kCirBg";
	};//end if else;
	return parentClassStr;
}
//////////////////////////////时间记录控件/////////////////////////////////////

//////////////////////////上下拖动改变高度//////////////////////
function nm3kDragY(obj){
	this.dragAble = false; //当前拖动状态,按下鼠标变成true,说明可以拖动了;
	this.ypos = 0; //点击下去与实际的div的y差值;
	
	this.callBack = obj.callBack;//回调函数;
	this.id = obj.id;//拖拽横向bar的id;	
	if(obj.minTopPos && obj.maxTopPos){
		this.minTopPos = obj.minTopPos;//记录上半部分最小高度;			
		this.maxTopPos = obj.maxTopPos;//记录上半部分最大高度;
	}else if(obj.minBottomPos && obj.maxBottomPos){
		this.minBottomPos = obj.minBottomPos;//记录下半部分最小高度;
		this.maxBottomPos = obj.maxBottomPos;//记录下半部分最大高度;
	}else{
		alert("传入参数错误！nm3kDragTopBottom函数无法执行");
	}
};//end nm3kDragY;

nm3kDragY.prototype.init = function(){
	$("#" + this.id).bind("mousedown", this, this.downFn);
};

//鼠标点击下去，开始进入拖拽;
nm3kDragY.prototype.downFn = function(e){
	if (!e) e = window.event;
	e.data.ypos = e.clientY - $("#" + e.data.id).offset().top;
	var w = $(this).width();
	var h = $(this).height();
	var l = $(this).offset().left;
	var t = $(this).offset().top;
	
	var strDiv = '';
	strDiv += '<div id="fakeDiv" style="position:absolute; width:' + w + 'px; height:' + h + 'px; left:' + l + 'px; top:' + t + 'px; background:#ccc; z-index:999999;"></div>';
	var fullScreenDiv = '';
	fullScreenDiv += '<div id="fullScreenDiv" style="width:100%; height:100%; position:absolute; left:0; top:0; background:#fff; z-index:999998; opacity:0.05; filter:alpha(opacity=1); cursor:move"></div>';
	$("body").append(fullScreenDiv).append(strDiv);
	e.data.dragAble = true;
	$("body").bind("mousemove", e.data, e.data.moveFn);
	$("body").bind("mouseup", e.data, e.data.upFn);
	$("body").bind("mouseleave", e.data, e.data.upFn);
};//end downFn;

//点击下去后移动;
nm3kDragY.prototype.moveFn = function(e){
	if (e.data.dragAble) {
		if (!e) e = window.event;
		var mouseT = e.clientY - e.data.ypos;			
		if(e.data.minTopPos && e.data.maxTopPos){//不要超出最大值和最小值;
			if(mouseT < e.data.minTopPos){mouseT = e.data.minTopPos};
			if(mouseT > e.data.maxTopPos){mouseT = e.data.maxTopPos};
		}
		if(e.data.minBottomPos && e.data.maxBottomPos){//不要超过底部的最低限制和最高限制;
			var windowH = $("#fullScreenDiv").height();
			var lineH = $("#"+e.data.id).outerHeight();
			if(windowH - lineH - mouseT < e.data.minBottomPos){
				mouseT = windowH - e.data.minBottomPos - lineH; 
			};
			if(windowH - lineH - mouseT > e.data.maxBottomPos){
				mouseT = windowH - e.data.maxBottomPos - lineH;
			};				
		}
		$("#fakeDiv").css("top", mouseT);			
		
	}		
};//end moveFn;

//鼠标松开;
nm3kDragY.prototype.upFn = function(e){
	$("body").unbind("mousemove");
	$("body").unbind("mouseup");
	$("body").unbind("mouseleave");
	e.data.dragAble = false;
	
	var t = $("#fakeDiv").offset().top;
	$("#fakeDiv").remove();
	$("#fullScreenDiv").remove();
	
	//执行回调函数,把top值传出去;
	var _callBack = e.data.callBack;
	window[_callBack].call(this,t);
	
};//end upFn;


