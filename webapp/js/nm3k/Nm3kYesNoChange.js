/*
 * 作者:leexiang
 * 时间:2013-11-20
 *  
 *  
 *  var n = new Nm3kYesNoChange({
			renderTo:"test123",
			id:"abcd",
			//value:"500",
			yesTip:"激活",
			noTip:"删除",
			firstShow:"no",
			toolTip:"减肥了的撒",
			width: 100
			yesValue:"已删除"
			
		});
		n.init();
*/ 
function Nm3kYesNoChange(obj){
	this.renderTo = obj.renderTo;
	this.id = obj.id;
	this.firstShow = "no";//初始显示no图标;
	this.width = 100;
	if(obj.width){this.width = obj.width};
	this.value = "";
	if(typeof obj.value == "number"){
		this.value = obj.value;
	}
	if(obj.firstShow == "yes"){this.firstShow = "yes"};
	if(obj.toolTip){this.toolTip = obj.toolTip}//input的toolTip;
	if(obj.yesTip && obj.noTip){
		this.yesTip = obj.yesTip;
		this.noTip = obj.noTip;
	}
	if(obj.yesValue){this.yesValue = obj.yesValue};
}
Nm3kYesNoChange.prototype.init = function(){
	var str = '';
	str += '<input type="text" id="'+ this.id +'" class="normalInput floatLeft" value="'+ this.value +'" style="width:'+ this.width +'px;"';
	if(this.toolTip){
	str += 'toolTip="'+ this.toolTip +'"';
	}	
	str += '/>'
	str += '<a href="javascript:;" class="nearInputBtn nm3kTip"';
	if(this.yesTip && this.noTip){
		if(this.firstShow == "no"){
			str += 'nm3kTip="'+ this.noTip +'"';
		}else{
			str += 'nm3kTip="'+ this.yesTip +'"'		
		}
	}
	str +=	'><span><i class="';
	if(this.firstShow == "yes"){
		str += 'yesIco';
	}else{
		str += 'noIco';
	}
	str +=  '"></i></span></a>';
	str += '<div style="clear:both; width:1px; height:0px; overflow:hidden;"></div>';
	$("#"+this.renderTo).html(str);
	$("#"+this.renderTo).find("a").bind("click",this,this.clickFn);//对a标签绑定点击事件;
	if(this.firstShow == "yes"){
		$("#"+this.id).attr("disabled",true).attr("class","normalInputDisabled floatLeft");
		if(this.yesValue){
			$("#"+this.id).val(this.yesValue);
		}
	}
}
//点击事件;
Nm3kYesNoChange.prototype.clickFn = function(e){
	var theI = $("#"+e.data.renderTo).find("i");	
	if(theI.hasClass("noIco")){//如果是no,则变成yes;
		theI.attr("class","yesIco");
		if(e.data.yesTip && e.data.noTip){
			$("#"+e.data.renderTo).find("a").attr("nm3kTip",e.data.yesTip);
			if($("#nm3kTip").length == 1){$("#nm3kTip").find(".nm3kTipTxt").empty().text(e.data.yesTip)}
		}
		if(e.data.yesValue){
			$("#"+e.data.id).val(e.data.yesValue).attr("class","normalInputDisabled floatLeft").attr("disabled",true);
		}
	}else{
		theI.attr("class","noIco");
		if(e.data.yesTip && e.data.noTip){
			$("#"+e.data.renderTo).find("a").attr("nm3kTip",e.data.noTip);
			if($("#nm3kTip").length == 1){$("#nm3kTip").find(".nm3kTipTxt").empty().text(e.data.noTip)}
		}
		$("#"+e.data.id).val("").attr("class","normalInput floatLeft").attr("disabled",false);
	}
}