//按钮组控件;

function Nm3kTabBtn(obj){
	if(!obj.callBack || !obj.tabs || !obj.renderTo || obj.tabs.length < 2){
		alert("参数传递错误\n必须存在callBack,tabs,\n且tabs为一个数组，个数不少于2");
		return false;
	}
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

Nm3kTabBtn.prototype.init = function(){
	var tabsArr = this.tabs;
	
	var str ='';
	str += '<ul class="whiteTabUl">';
	for(var i=0; i<tabsArr.length; i++){
		if(i==0){
	str +=     '<li><a href="javascript:;" class="first"><span><label>'+ tabsArr[i] +'</label></span></a></li>';	
		}else if(i == tabsArr.length-1){
	str +=     '<li><a href="javascript:;" class="last"><span><label>'+ tabsArr[i] +'</label></span></a></li>';	
		}else{
	str +=     '<li><a href="javascript:;"><span><label>'+ tabsArr[i] +'</label></span></a></li>';		
		}
	}
	str += '</ul>';
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

Nm3kTabBtn.prototype.tabClickLi = function(e){//点击li;
	if (!e) e = window.event;
	var index = $(this).index();
	var theUl = $("#" + e.data.renderTo +" .whiteTabUl");
	index = theUl.find("li").index(this);//为了兼容jquery1.3;
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
	
	var _callBack = e.data.callBack;
	window[_callBack].call(this,index);
//	eval( _fn +"("+ index +")");
//  window[_fn](index);			
};//end tabClickLi;