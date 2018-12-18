////////////////////////////////////////////////////////////弹出框;
//作者：leexiang,最后修改日期2013-9-25;
/*
* position 提示信息显示的位置,topLeft,topRight,bottomLeft,bottomRight;
* autoHide 是否自动消失;
* showTime 显示毫秒数,默认为2000;大概是10秒钟;
* okBtn 是否有“确定”按钮;
* okBtnTxt 确定按钮上的文字，默认是“确定”;
* cancelBtn 是否有取消按钮;
* cancelBtnTxt 取消按钮上文字;
* closeBtn 是否有关闭按钮
* title 标题
* html 内容
* unique 是否唯一，如果true则必须有id;
* id 
* okBtnCallBack 确定按钮的回调,注意回调函数不能传递参数,经过修改已经将所有回调绑定到内部页面上(从子页面调取父页面的弹框，点击回调的还是子页面的函数(2013-9-25修改); 
* cancelBtnCallBack 取消按钮的回调;
* closeBtnCallBack 关闭按钮的回调;
* icoCls 左侧图标class;
* width 默认为352;建议不要修改它;
* timeLoading 记录时间并且整个logo显示完毕则时间到了;
* timeLoadingCartoon 动画是从上往下，还是从左上角开始 top或者topLeft，默认为从上到下;
* oldShowTime 记录一开始设置的显示时间，由于showTime会减少，所以需要showTime / oldShowTime * 图标(div)高度算出百分比;
*/

function Nm3kMsg(obj){
	this.hasOne = false;//系统里面有没有这个id的div.如果有了，那么先remove掉他，再innerHTML新的内容;
	if(obj.id){
		var len = $("#nm3kMsg-Contaner").find("#"+ obj.id).length;
		if(len > 0 ){//系统中已经有这个div了;
			this.hasOne = true;
			this.msgDiv = null;			
			$("#nm3kMsg-Contaner").find("#"+ obj.id).remove();			
			//return;
		}
	}
	if(!obj.html){
		alert("参数不正确，html参数是必须项");
		return;
	}	
	this.position = obj.position ? obj.position : "rightBottom";
	this.autoHide = (obj.autoHide || obj.autoHide == false) ? obj.autoHide : true;
	this.showTime = obj.showTime ? obj.showTime : 2000;
	this.oldShowTime =  obj.showTime ? obj.showTime : 2000;
	this.okBtn = obj.okBtn ? obj.okBtn : false;	
	this.okBtnTxt = obj.okBtnTxt ? obj.okBtnTxt : "确定";
	this.cancelBtn = obj.cancelBtn ? obj.cancelBtn : false;	
	this.cancelBtnTxt = obj.cancelBtnTxt ? obj.cancelBtnTxt : "取消";
	this.closeBtn = (obj.closeBtn || obj.closeBtn == false) ? obj.closeBtn : true;
	this.movement = null;//存储是否在执行动画;
	this.msgDiv = null;
	this.width = obj.width ? obj.width : 352;
	this.timeLoading = obj.timeLoading ? obj.timeLoading : false;
	this.timeLoadingCartoon = "top";
	if(obj.timeLoadingCartoon){
		switch(obj.timeLoadingCartoon){
			case "topLeft":
			case "leftTop":
				this.timeLoadingCartoon = "topLeft";
			break;
		}
	};//end if;		
	if(obj.title){ this.title = obj.title;}
	if(obj.html){ this.html = obj.html;}
	
	this.unique = obj.unique ? obj.unique : false;
	if(obj.unique){
		if(!obj.id){
			alert("参数错误，有unique属性就必须有id属性");
		}else{
			this.id = obj.id;
		}
	}	
	
	if(obj.okBtnCallBack){ this.okBtnCallBack = obj.okBtnCallBack;}
	if(obj.cancelBtnCallBack){ this.cancelBtnCallBack = obj.cancelBtnCallBack;	}
	if(obj.closeBtnCallBack){ this.closeBtnCallBack = obj.closeBtnCallBack; }
	if(obj.icoCls){ this.icoCls = obj.icoCls;}	
};//end Nm3kMsg;

Nm3kMsg.prototype.constructor = "Nm3kMsg";

Nm3kMsg.prototype.init = function(){
	var container = $("#nm3kMsg-Contaner");
	if(container.length == 0){//没有外层的时候;
		var pos = "";
		switch(this.position){
			case "topLeft":
			case "leftTop":
				pos = "top:10px; left:10px; width:"+ this.width +"px;";
			break;
			case "bottomLeft":
			case "leftBottom":
				pos = "bottom:10px; left:10px; width:"+ this.width +"px;";
			break;
			case "topRight":
			case "rightTop":
				pos = "top:10px; right:10px; width:"+ this.width +"px;";
			break;
			case "bottomRight":
			case "rightBottom":
				pos = "bottom:10px; right:10px; width:"+ this.width +"px;";
			break;
			default:
				pos = "bottom:10px; left:10px; width:"+ this.width +"px;";
			break;
		}
		var outer = '<div id="nm3kMsg-Contaner" style="position:absolute;z-index:999999999;'+ pos +'"></div>';
		$("body").append(outer);
	}else{//有外层的情况;
		 var len = $("#nm3kMsg-Contaner").find("#"+ this.id).length;
		 if(len > 0){//系统中已经有这个弹出框;			 
			 /*var theMsg = $("#nm3kMsg-Contaner").find("#"+ this.id);
			 if(this.title){
			 	theMsg.find(".nm3kMsg-header").html(this.title);
			 }
			 theMsg.find(".nm3kMsg-body").html(this.html);			 
			 this.msgDiv = theMsg;*/
			 //return;
		 }		
	}
	
	var msg = '';
	//msg += '<div style="position:relative;" class="nm3kMsg">';
	msg += 	   '<div class="nm3kMsg-tl"><div class="nm3kMsg-tr"><div class="nm3kMsg-tc"></div></div></div>';//头部圆角;
	msg +=     '<div class="nm3kMsg-ml">';
	msg += 			'<div class="nm3kMsg-mr">';
	if(this.icoCls){    //如果有左侧图标;
	msg += 				'<div class="nm3kMsg-mc nm3kMsg-icon '+ this.icoCls +'">';
	}else{
		if(this.timeLoading){
	msg += 				'<div class="nm3kMsg-mc nm3kMsg-icon">';
		}else{
	msg += 				'<div class="nm3kMsg-mc">';	
		}
	
	}
	if(this.title){     //如果存在标题;
	msg += 				'<div class="nm3kMsg-header">'+ this.title +'</div>';
	}
	msg += 				'<div class="nm3kMsg-body">'+ this.html +'</div>';
	if(this.okBtn || this.cancelBtn){//如果存在“确定”或者“取消”按钮;
	msg += 				'<ul class="nm3kMsg-btn">';
		if(this.okBtn){ //确定按钮;			
	msg +=					'<li><a href="javascript:void(0)" class="okBtn"><span>'+ this.okBtnTxt +'</span></a></li>';		
		}
		if(this.cancelBtn){ //取消按钮;
	msg +=					'<li><a href="javascript:void(0)" class="cancelBtn"><span>'+ this.cancelBtnTxt +'</span></a></li>';		
		}
	msg += 				'</ul><div style="clear:both; height:0px; overflow:hidden;"></div>';
	}
	msg += 				'</div>'; //mc的结束;
	msg += 				'</div>'; //mr的结束;
	msg += 		'</div>';// ml 的结束;
	msg += 				'<div class="nm3kMsg-bl"><div class="nm3kMsg-br"><div class="nm3kMsg-bc"></div></div></div>';//底部带阴影的圆角;
	if(this.closeBtn){ //如果存在关闭按钮;
	msg += 				'<a href="javascript:;" class="nm3kMsg-close"></a>';
	}
	if(this.timeLoading){
	msg += 				'<div class="nm3kMsg-timeLoadingBg"><div class="nm3kMsg-timeLoading"></div></div>'
	}
	//msg += 				'</div>';
	
	var msgPanel = document.createElement("div");//不用str形式加进去的原因是创建div元素后，可以对这个元素进行bind事件;
	msgPanel.style.position = "relative";
	msgPanel.className = "nm3kMsg";
	if(this.unique && this.id){
		msgPanel.id = this.id;
	}
	$("#nm3kMsg-Contaner").append(msgPanel);	
	
	msgPanel.innerHTML = msg;
	this.msgDiv = $(msgPanel);//将这个div存入属性;
	
	//更新timeloading的位置，让它垂直居中,并且判断动画是那种形式;
	if(this.timeLoading){
		var timeLoadingT = (this.msgDiv.innerHeight() - this.msgDiv.find(".nm3kMsg-timeLoadingBg").height())/2;
		if(timeLoadingT < 10){ timeLoadingT = 10 }
		this.msgDiv.find(".nm3kMsg-timeLoadingBg").css("top",timeLoadingT);
		if(this.timeLoadingCartoon == "topLeft"){
			this.msgDiv.find(".nm3kMsg-timeLoading").css("width",0);
		}
	}

	$(msgPanel).find(".nm3kMsg-close").bind("click",this,this.closeMsg);//点击关闭按钮，需要消失消息框;
	if(this.closeBtnCallBack){//如果存在关闭按钮;
		$(msgPanel).find(".nm3kMsg-close").bind("click",this.closeBtnCallBack);
	}
	
	if(this.okBtn){
		$(msgPanel).find(".okBtn").bind("click",this,this.closeMsg);//点击确定按钮，需要消失消息框;
		if(this.okBtnCallBack){//如果存在确定按钮的回调;
			$(msgPanel).find(".okBtn").bind("click",this.okBtnCallBack);
		}
	}
	if(this.cancelBtn){
		$(msgPanel).find(".cancelBtn").bind("click",this,this.closeMsg);//点击取消按钮，需要消失消息框;
		if(this.cancelBtnCallBack){//如果存在取消按钮的回调;
			$(msgPanel).find(".cancelBtn").bind("click",this.cancelBtnCallBack);
		}
	}
	if(this.autoHide){
		//$(msgPanel).find(".nm3kMsg-header").html(this.showTime);		
		this.movement = setTimeout(createDelegate(this,this.timeUp),1)
	}
};//end init;

Nm3kMsg.prototype.closeMsg = function(e){//关闭按钮的点击事件;
	if(e.data.showTime && e.data.autoHide){
		if(e.data.movement){ clearTimeout(e.data.movement);}		
	}
	if(!e){e = window.event;}
	var cls = $(this).attr("class")
	switch(cls){
		case "nm3kMsg-close"://点击的是右上角关闭叉叉按钮;
			if(e.data.closeBtnCallBack){
				e.data.msgDiv.find(".nm3kMsg-close").unbind("click",this.closeBtnCallBack);
			}			
		break;
		case "okBtn":
			if(e.data.okBtnCallBack){
				e.data.msgDiv.find(".okBtn").unbind("click",this.okBtnCallBack);
			}
		break;
		case "cancelBtn":			
			if(e.data.cancelBtnCallBack){
				e.data.msgDiv.find(".cancelBtn").unbind("click",this.cancelBtnCallBack);
			}
		break;
	}
	e.data.msgDiv.remove();
}

Nm3kMsg.prototype.timeUp = function(){//循环减去showTime的值，谁的值到了0，谁就消失;
	if(this.movement){
		clearTimeout(this.movement);
	}
	this.showTime --;
	if(this.showTime <= 0){
		this.movement = null;
		this.msgDiv.remove();
		return;
	}
	//this.msgDiv.find(".nm3kMsg-header").text(this.showTime);
	var percent = (1 - this.showTime / this.oldShowTime) * this.msgDiv.find(".nm3kMsg-timeLoadingBg").height();
	this.msgDiv.find(".nm3kMsg-timeLoading").height(percent);
	if(this.timeLoadingCartoon == "topLeft"){
		this.msgDiv.find(".nm3kMsg-timeLoading").width(percent);
	}
	this.movement = setTimeout(createDelegate(this,this.timeUp),1)
}

Nm3kMsg.prototype.update = function(obj){
	this.msgDiv.css("opacity", 0.2);
	this.msgDiv.animate({"opacity":1},"fast");
	this.showTime = obj.showTime ? obj.showTime : 2000;
	this.oldShowTime = obj.oldShowTime ? obj.oldShowTime : 2000;
	if(obj.title){
		this.msgDiv.find(".nm3kMsg-header").html(obj.title);
	}
	if(obj.html){
		 this.msgDiv.find(".nm3kMsg-body").html(obj.html);
	}
	if(obj.icoCls){
		this.msgDiv.find(".nm3kMsg-mc").attr("class","nm3kMsg-mc nm3kMsg-icon").addClass(obj.icoCls);
	}
}

Nm3kMsg.prototype.clearOldTimeout = function(obj){
	if(this.showTime){
		this.showTime = obj.showTime;
	}	
}

Nm3kMsg.prototype.die = function(){
	if(this.movement){
		clearTimeout(this.movement);
	}	
	this.msgDiv.remove();
}


function createDelegate(object, method) {
	var delegate = function(){
		method.call(object);
	}
	 return delegate;
};//end createDelegate;

/////////////////////////////////////////////////////////////////////////////////////////////////