//作者：leexiang ,最后更新时间：2013-10-26;
/*	 最多支持3级，value是唯一的;
 * var data2 = [
			{text:"所有", value:"0",child:null},
			{text:"环境告警", value:"1",child:[
				{text:"温度告警", value:"22",child:null},
				{text:"端口链路告警", value:"222",child:null},
				{text:"CM阈值告警", value:"555",child:[
					{text:"CM接收电平性能越界", value:"33", child:null}
				]}
			]}
		];
 * var s = new Nm3kLevelSelect({
			id : 'one',
			renderTo : 'test',
			width : 130,
			subWidth : 300,
			dataArr : window.data2
		});
		s.init();
*/

function Nm3kLevelSelect(o){
	if(!o.id || !o.renderTo || !o.width || !o.dataArr || $("#"+o.renderTo).length == 0){
		alert("参数传递错误，请查看Nm3kLevelSelect函数传入的数据!");
		return;
	}
	this.id = o.id;
	this.renderTo = o.renderTo;
	this.width = o.width; //上面selected的宽度;
	this.subWidth = o.subWidth ? o.subWidth : 220;//底部弹出窗的宽度;
	if(this.subWidth < this.width){this.subWidth = this.width-10};//下部宽度不能小于上部宽度，否则会很丑,减去10是因为padding:5px;
	this.subMinHeight = o.subMinHeight ? o.subMinHeight : 40;//底部弹出框的最小高度,默认为40;
	this.firstSelectValue = o.firstSelectValue ? o.firstSelectValue : null;//通过value，判断要选择哪一个;
	this.firstSelectTxt = null;//如果给出了firstSelectValue,那么循环的时候，把text给this.firstSelectTxt;
	this.dataArr = o.dataArr;//数据的数组;
	this.subId = o.id + "_Nm3kLevelSelect";//底部弹出的id,自动生成;		
	this.isOpen = false;//是否已经打开下面选择部分;
	if(o.subHeight){ this.subHeight = o.subHeight;}
}
Nm3kLevelSelect.prototype.init = function(){
	//生成上半部分，下拉选框;
	var topStr = ''
	topStr += '<div class="Nm3kLevelSelect" style="width:'+ this.width +'px;">';
	topStr +=	'<p></p>';
	topStr +=	'<a class="Nm3kLevelSelectArr" href="javascript:;"></a>';
	topStr +=  '<input type="hidden" id="'+ this.id +'" name="'+ this.id +'" />'
	topStr += '</div>'
	$("#"+this.renderTo).html(topStr);
	//生成下半部分,有级别的弹出层（最多3级）;
	var str = '';
	str += '<div class="Nm3KLevelSelectSub" id="'+ this.subId +'" style="width:'+ this.subWidth +'px; display:none;"><div class="getHeight">';
	for(var i=0; i<this.dataArr.length; i++){////////////////先循环一次把第一级别读取出来;
		var arr1 = this.dataArr[i];
		//if(arr1.clickAble){//第一级别可以点击;
			str += '<div class="Nm3kLevelSelectLevel1"><a href="javascript:;" name="'+ arr1.value +'">'+ arr1.text +'</a></div>';
		/*}else{//第一级别不可以点击;
			str += '<div class="Nm3kLevelSelectLevel1"><span>'+ arr1.text +'</a></span></div>';
		}*/
		
		var arr2 = arr1.child;
		if(arr2 != null){//有第2级别
			str += '<dl>';
			for(var j=0; j<arr2.length;j++){
				//没有第三级别，直接生成第2级别;
				if(arr2[j].child == null){
					//if(arr2[j].clickAble){//第三级别可以点击;
						str += '<dt><a href="javascript:;" name="'+ arr2[j].value +'">'+ arr2[j].text +'</a></dt>';
					/*}else{//第三级别不可以点击;
						str += '<dt><span>'+ arr2[j].text +'</span></dt>';
					}*/
				}else{//有第三级别，生成第二级别;
					//if(arr2[j].clickAble){//第2级别可以点击;
						str += '<dt><a href="javascript:;" name="'+ arr2[j].value +'">'+ arr2[j].text +'</a></dt>';
					/*}else{//第二级别不可以点击;
						str += '<dt><span>'+ arr2[j].text +'</span></dt>';
					}*/
					for(var k=0; k<arr2[j].child.length;k++){
						var arr3 = arr2[j].child[k];
						//if(arr3.clickAble){//第三级别可以点击;
							str += '<dd><a href="javascript:;" name="'+ arr3.value +'">'+ arr3.text +'</a></dd>';
						/*}else{//第三级别不可以点击;
							str += '<dd><span>'+ arr3.text +'</span></dd>';
						}*/
					}
				}
			};//end for;
			str += '</dl>';
		}
	};//end for;
	str += '</div></div>';
	$("body").append(str);
	
	var $topDiv = $("#" + this.renderTo).find(".Nm3kLevelSelect");
	var l = $topDiv.offset().left;
	var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
	var $subDiv = $("#" + this.subId);
	$subDiv.css({"left": l, "top": t});
	
	if(this.firstSelectValue == null){//如果没有给出选择,或者给出选择的没有匹配，那么默认选择第一个;
		var defaultTxt = (this.dataArr)[0].text;
		$("#" + this.renderTo).find(".Nm3kLevelSelect p").text(defaultTxt).attr("title",defaultTxt);
		$("#"+ this.id).val((this.dataArr)[0].value);
		$("#" + this.subId).find("a").eq(0).addClass("selected");
	}else{//如果给出了默认选择;
		var _this = this;
		$("#" + this.subId).find("a").each(function(){				
			if($(this).attr("name") == _this.firstSelectValue){
				var $this = $(this);
				$this.addClass("selected");
				$("#" + _this.renderTo).find(".Nm3kLevelSelect p").text($this.text()).attr("title",$this.text());
				$("#"+ _this.id).val(_this.firstSelectValue);
			}
		});//end each;
	}
	if(this.subHeight){
		$subDiv.height(this.subHeight);
	}
	//点击上部div,弹出下面弹出框;
	$topDiv.bind("click",this,this.showSub);		
	//绑定td点击事件
	$("#" + this.subId).find("a").bind("click",this,this.alinkClick);
	$("#" + this.subId).find("span").bind("click",this,this.spanClick);
	$("html").bind("click",this,this.bodyClick);
	$(window).bind("resize",this,this.bodyClick);
	
	
};//end init;

//点击上部，显示下面div;
Nm3kLevelSelect.prototype.showSub = function(e){
	if(!e){e = window.event;}		
	e.stopPropagation();
	var $subId = $("#"+ e.data.subId);	
	if(e.data.isOpen == false){			
		$subId.css("display","block");//必须先显示，才能获取正确高度;
		var $topDiv = $("#" + e.data.renderTo).find(".Nm3kLevelSelect");
		var l = $topDiv.offset().left;
		var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
		var h = $(window).height() - t - 50;
		var subDivH = $("#"+ e.data.subId).find(".getHeight").height();
		if(subDivH < h){ h = subDivH}
		if(h < e.data.subMinHeight){ h = e.data.subMinHeight;}
		e.data.isOpen = true;
		$subId.css({display:"block", top: t, left: l, height: h});
		if(e.data.subHeight){
			$subId.height(e.data.subHeight);
		}
	}else{
		$subId.css({display:"none"});
		e.data.isOpen = false;
	}
};//end showSub;

//对span点击，应该不调用任何函数，也不关闭弹出层;
Nm3kLevelSelect.prototype.spanClick = function(e){
	if(!e){e = window.event;}
	e.stopPropagation();
}

//对a进行点击,下拉框消失，数据到上面;
Nm3kLevelSelect.prototype.alinkClick = function(e){
	var $subId = $("#"+ e.data.subId);		
	if(!e){e = window.event;}
	e.stopPropagation();
	var o = {};
	o.text = $(this).text();
	$subId.find("a").removeClass("selected");
	$(this).addClass("selected");
	var $topDiv = $("#" + e.data.renderTo).find(".Nm3kLevelSelect");
	$topDiv.find("p").text(o.text).attr("title",o.text);
	$("#"+ e.data.id).val($(this).attr("name"));
	$subId.css("display","none");
	e.data.isOpen = false;
}

//对空白处点击;
Nm3kLevelSelect.prototype.bodyClick = function(e){
	if(!e){e = window.event;}
	if(e.data.isOpen){
		e.data.isOpen = false;
		var $subId = $("#"+ e.data.subId);
		$subId.css({display:"none"});
	}
}