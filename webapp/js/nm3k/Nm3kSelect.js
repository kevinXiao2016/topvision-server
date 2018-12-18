//作者：leexiang ,最后更新时间：2013-09-27;
/*	 
 * var testArr = [{text:"one",value:"a1"},{text:"two",value:"b1"}];
 * var s = new Nm3kSelect({
		id : "one",
		renderTo : "putSel2",
		width : 200,
		firstSelect : 0,
		rowNum : 2,
		subWidth : 180,
		//subHeight : 200,
		dataArr : testArr
	})
	s.init();
*/

function Nm3kSelect(o){
	if(!o.id || !o.renderTo || !o.width || !o.dataArr || $("#"+o.renderTo).length == 0){
		alert("参数传递错误，请查看Nm3kSelect函数传入的数据!");
		return;
	}
	this.id = o.id;
	this.renderTo = o.renderTo;
	this.width = o.width; //上面selected的宽度;
	this.subWidth = o.subWidth ? o.subWidth : 220;//底部弹出窗的宽度;
	if(this.subWidth < this.width){this.subWidth = this.width-10};//下部宽度不能小于上部宽度，否则会很丑,减去10是因为padding:5px;
	this.subMinHeight = o.subMinHeight ? o.subMinHeight : 40;//底部弹出框的最小高度,默认为40;
	this.rowNum = o.rowNum ? o.rowNum : 2; //每行显示几个,默认显示2个;
	this.firstSelect = o.firstSelect ? o.firstSelect : 0;//默认选择第1个;
	this.dataArr = o.dataArr;//数据的数组;
	this.subId = o.id + "_Nm3kSelect";//底部弹出的id,自动生成;		
	this.isOpen = false;//是否已经打开下面选择部分;
	if(o.subHeight){ this.subHeight = o.subHeight;}
}
Nm3kSelect.prototype.init = function(){		
	//生成上半部分，下拉选框;
	var str = ''
	str += '<div class="Nm3kSelect" style="width:'+ this.width +'px;">';
	str +=	'<p></p>';
	str +=	'<a class="Nm3kSelectArr" href="javascript:;"></a>';
	str +=  '<input type="hidden" id="'+ this.id +'" name="'+ this.id +'" />'
	str += '</div>'
	$("#"+this.renderTo).html(str);
	var firstShowNum = this.firstSelect;
	if(firstShowNum > this.dataArr.length){firstShowNum = this.dataArr.length};
	var defaultTxt = (this.dataArr)[this.firstSelect].text;
	$("#" + this.renderTo).find(".Nm3kSelect p").text(defaultTxt).attr("title",defaultTxt);
	//生成下半部分,弹出层;
	var subStr = '';
	subStr += '<div class="Nm3KSelectSub" id="'+ this.subId +'" style="width:'+ this.subWidth +'px; display:none;">';
	subStr +=	'	<table cellpadding="0" cellspacing="0" rules="all" border="1" bordercolor="#E0E0E0" style="border-collapse:collapse;">';
	subStr +=	'		<tbody>';		
		var tdNum = this.rowNum;
		//先把数组除以个数，得到要分成几个tr;
		var trNum = Math.ceil(this.dataArr.length / tdNum);	
		for(var i=0; i<trNum; i++){
			subStr += '<tr>';
				for(var k=0; k<tdNum; k++){
					subStr += '<td>';
					var v = '';
					if(this.dataArr[i*tdNum + k] != undefined){
						v = '<a href="javascript:;" name="'+ (this.dataArr[i*tdNum + k]).value +'">' + (this.dataArr[i*tdNum + k]).text + '</a>';
					}
					subStr += v
					subStr += '</td>';
				};//end for k;
			subStr += '</tr>';
		};//end for i;		
	subStr +=	'		</tbody>';
	subStr +=	'	</table>';
	subStr +=	'</div>';
	$("body").append(subStr);
	var $topDiv = $("#" + this.renderTo).find(".Nm3kSelect");
	var l = $topDiv.offset().left;
	var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
	var $subDiv = $("#" + this.subId);
	$subDiv.css({"left": l, "top": t});
	$subDiv.find("td").eq(firstShowNum).addClass("selected");
	var getValue = $subDiv.find("td").eq(firstShowNum).find("a").attr("name");
	$("#"+ this.id).val(getValue);
	if(this.subHeight){
		$subDiv.height(this.subHeight);
	}
	
	//点击上部div,弹出下面弹出框;
	$topDiv.bind("click",this,this.showSub);		
	//绑定td点击事件
	$("#" + this.subId).find("tbody td").bind("click",this,this.tdClick);
	$("html").bind("click",this,this.bodyClick);
	$(window).bind("resize",this,this.bodyClick);
}
	
//对空白处点击;
Nm3kSelect.prototype.bodyClick = function(e){
	if(!e){e = window.event;}
	if(e.data.isOpen){
		e.data.isOpen = false;
		var $subId = $("#"+ e.data.subId);
		$subId.css({display:"none"});
	}
}

//点击上面，出现下部div;
Nm3kSelect.prototype.showSub = function(e){
	if(!e){e = window.event;}		
	e.stopPropagation();
	var $subId = $("#"+ e.data.subId);	
	if(e.data.isOpen == false){			
		$subId.css("display","block");//必须先显示，才能获取正确高度;
		var $topDiv = $("#" + e.data.renderTo).find(".Nm3kSelect");
		var l = $topDiv.offset().left;
		var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
		var h = $(window).height() - t - 50;
		var subTableH = $("#"+ e.data.subId).find("table").height();			
		if(subTableH < h){ h = subTableH}
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
}

//对td进行点击,下拉框消失，数据到上面;
Nm3kSelect.prototype.tdClick = function(e){
	if($(this).find("a").length == 0){return;}
	var $subId = $("#"+ e.data.subId);		
	if(!e){e = window.event;}
	e.stopPropagation();
	var o = {};
	o.text = $(this).find("a").text();
	$subId.find("td").removeClass("selected");
	$(this).addClass("selected");
	var $topDiv = $("#" + e.data.renderTo).find(".Nm3kSelect");
	$topDiv.find("p").text(o.text).attr("title",o.text);
	$("#"+ e.data.id).val($(this).find("a").attr("name"));
	$subId.css("display","none");
	e.data.isOpen = false;
}