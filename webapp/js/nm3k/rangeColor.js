//分区间展示;
/*  var obj = {
	  title: "某某统计",
	  renderTo: "putRangeColor",
	  box : [
	    {
	      "start" : -15,
	      "end" : -10,
	      "color" : "#B638FF",
	      "num" : 0,
	      "href" : "http://www.baidu.com"
	    },
	    {
	      "start" : -10,
		  "end" : -5,
	      "color" : "#B638FF",
	      "num" : 0,
	      "href" : "http://www.baidu.com"
	    },
	    {
	      "start" : -5,
		  "end" : 0,
		  "color" : "#0071FF",
		  "num" : 0,
		  "href" : "http://www.baidu.com"
		},
	    {
		  "start" : 0,
		  "end" : 10,
		  "color" : "#2DD8CA",
		  "num" : 0,
		  "href" : "http://www.baidu.com"
		}
	  ],
	  na : {
	      "color" : "#E41A14",
	      "num" : 5,
	      "href" : "http://www.baidu.com"
	  }
	}*/

var addRangeColor = (function(){
	var oRange = {
		title : '<p class="edge5 txtCenter"><b>{0}</b></p>',
		outerStart : '<dl class="nm3kColorRange" style="margin: 0px auto; width: {0}px;"><dd><ol class="nm3kColorRangeOl">',
		firstLine : '<li class="colorTableFirst"></li>',
		box : '<li style="background:{0}; width:27px; border:1px solid #2a2a2a; border-right:none;"><a class="nm3kTip" href="javascript:;" nm3ktip="{1}" style="text-decoration:none; cursor:default;">{2}</a></li>',
		lastBox : '<li style="background:{0}; width:27px; border:1px solid #2a2a2a;"><a style="text-decoration:none; cursor:default;" class="nm3kTip" href="javascript:;" nm3ktip="{1}">{2}</a></li>',
		lastArrLine : '<li class="lastArr"></li>',
		outerEnd : '</dd></dl>',
		subTxt : '<span style="left:{0}px;">{1}</span>',
		subTxtMax : '<span style="left:{0}px;"><b>{1}</b></span>',
		boxWidth : 27,   //单个盒子宽度;
		aSubTxt : [], //底部的文字;
		level0 : '@OLTREAL.normal@',
		level2 : '@EPON.warnAlert@',
		level3 : '@EPON.minorAlert@',
		level4 : '@EPON.majorAlert@',
		level5 : '@EPON.criticalAlert@',
		level6 : '@report.blockAlert@',
		na : '@OLTREAL.unNormal@'
		
	}
	function numToLevel(num){
		var tip;
		switch(num){
		case 0:
			tip = oRange.level0;
			break;
		case 2:
			tip = oRange.level2;
			break;
		case 3:
			tip = oRange.level3;
			break;
		case 4:
			tip = oRange.level4;
			break;
		case 5:
			tip = oRange.level5;
			break;
		case 6:
			tip = oRange.level6;
			break;
		}
		return tip;
	}
	return function(arg){
		if(!arg.renderTo){return;}
		oRange.aSubTxt.length = 0;
		this.box = arg.box; 
		var str = '';
		if(arg.title){
			str += String.format(oRange.title, arg.title);
		}
			str += String.format(oRange.outerStart, (this.box.length+2)*28 + 18  );
			str += oRange.firstLine;
			for(var i=0; i<this.box.length; i++){
				var nowBox = this.box[i];
				str += String.format(oRange.box, nowBox.color, numToLevel(nowBox.level), nowBox.num);
				oRange.aSubTxt.push(nowBox.start);
				if(i == (nowBox.length-1)){
					oRange.aSubTxt.push(nowBox.end);
				}				
			}
			str += oRange.lastArrLine;
			str += String.format(oRange.lastBox, arg.na.color, oRange.na, arg.na.num);
			str += '</ol>'
			for(i=0; i<oRange.aSubTxt.length; i++){
				var nowTxt = oRange.aSubTxt[i];
				if( nowTxt.length>4 ){
					str += String.format(oRange.subTxtMax, i*oRange.boxWidth+8 , nowTxt);
				}else{
					str += String.format(oRange.subTxt, i*oRange.boxWidth+8 , nowTxt);
				}
			}
			str += String.format(oRange.subTxt, (this.box.length+1)*oRange.boxWidth+8+16, "N / A");
			str += oRange.outerEnd;
		$("#"+arg.renderTo).html(str);
		
	};//end return;
})();//end

/*function addRangeColor(arg){
	var boxArr = arg.box, 
	na = arg.na,
	normal = null,
	boxWidth = 27,       //单个盒子宽度;
	blankWidth = 26,     //空白处的宽度;
	startArr = [];       //范围开始文字;
	endArr = [];         //范围结束文字;
	var startEdge = 55;  //盒子的宽度+空白的宽度,29+26=55;
	if(arg.normal){
		normal = arg.normal;
		normal.tip = (typeof(normal.tip) == "string") ? normal.tip : "normal"; 
	}
	//根据不同的type,采取不同的填充方式;
	var type = (arg.type == "fill_parent") ? "fill_parent": "wrap_content";
	if(type == "fill_parent"){
		//采取最长为322px设计。盒子的空白为盒子个数减去1,空白的宽度为26px;
		//盒子的宽度为29，实际是27，因为有border,因此还要减去盒子个数*2;
		boxWidth = (250 - (boxArr.length - 1) * 26 - (boxArr.length * 2)) / boxArr.length; 
		startEdge = boxWidth + 2 + 26;
	}
	
	
	
	var str = '';
	    str += '<p class="edge5 txtCenter"><b>'+ arg.title +'</b></p>';
		str += '<dl class="nm3kColorRange" style="margin:0 auto;"><dd>';
		str +=     '<ol class="nm3kColorRangeOl">';
		str +=         '<li class="colorTableFirst"></li>';
	for(var i=0; i<boxArr.length; i++){
		var currentBox = boxArr[i],
		    num = currentBox.num,
		    bg = currentBox.color,
		    start = currentBox.start,
		    end = currentBox.end;

		str +=         '<li class="box2" style="background:'+ bg +'; width:'+ boxWidth +'px">';
		str +=   	       '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ num +'">'+ num +'</a>';
		str +=	       '</li>';
		
		startArr.push(start);
		endArr.push(end);
	  
	  if( i != (boxArr.length-1) ){
		str +=         '<li class="rangeEdge"></li>';     	
	  }
	};//end for;
		str +=         '</li>';
		str +=         '<li class="lastArr"></li>'
		str +=         '<li class="box2" style="background:'+ na.color +';">';
		str +=             '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ na.num +'">'+ na.num +'</a>';
		str +=         '</li>';
		if(normal != null){
			str +=         '<li class="box2" style="background:'+ normal.color +'; margin-left:5px;">';
			str +=             '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ normal.num +'">'+ normal.num +'</a>';
			str +=         '</li>';
		}
		str +=     '</ol>';
	for(i=0; i<startArr.length; i++){
		str += '<span style="left:'+ (3+ i*startEdge) +'px;">';
		if(startArr[i].length > 4){
			str += '<b>' + startArr[i] + '</b>'; 
		}else{
			str += startArr[i];
		}
		str += '</span>';
	}//end for;
	for(i=0; i<endArr.length; i++){
		str += '<span style="left:'+ (3+ i*startEdge + boxWidth+2) +'px;">';
		if(endArr[i].length > 4){
			str += '<b>' + endArr[i] + '</b>';
		}else{
			str += endArr[i];
		}
		str += '</span>';
	}
		str += '<span style="left:'+ (3+ i*startEdge + 15) +'px;">N / A</span>'
	if(normal != null){
		str += '<span style="left:'+ (3+ i*startEdge + 15 + 27 ) +'px; width:40px">';
		if(normal.tip.length > 4){
			str += '<b>' + normal.tip + '</b>';
		}else{
			str += normal.tip;
		}
		str += '</span>'
	}
		str += '</dd></dl>';
	var $renderTo = $("#"+arg.renderTo); 
	$renderTo.html(str);
	var w = $renderTo.find(".nm3kColorRangeOl").outerWidth();
	$renderTo.find(".nm3kColorRange").width(w);
} 

function addCcRangeColor(arg){
	var boxArr = arg.box, 
	na = arg.na,
	subTxtArr = [];//记录下标文字;
	
	var str = '';
	    str += '<p class="edge5 txtCenter"><b>'+ arg.title +'</b></p>';
		str += '<dl class="nm3kColorRange" style="margin:0 auto;"><dd>';
		str +=     '<ol class="nm3kColorRangeOl">';
		str +=         '<li class="colorTableFirst"></li>';
	for(var i=0; i<boxArr.length; i++){
		var currentBox = boxArr[i],
		num = currentBox.num,
		bg = currentBox.color,
		start = currentBox.start,
		end = currentBox.end;
		
	  if( i==0 ){
		str +=             '<li class="box" style="border-left:1px solid #2a2a2a; background:'+ bg +';">';
		str +=   	           '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ num +'">'+ num +'</a>';
		str +=	           '</li>';
		subTxtArr.push(start);
	  }else{
		str +=             '<li class="box" style="background:'+ bg +';">';
		str +=   	           '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ num +'">'+ num +'</a>';
		str +=	           '</li>';
		subTxtArr.push(start);
	  }
	  if( i == (boxArr.length-1) ){
			subTxtArr.push(end);
	  }
	};//end for;
		str +=         '</li>';
		str +=         '<li class="lastArr"></li>'
		str +=         '<li class="box" style="border-left:1px solid #2a2a2a; background:'+ na.color +';">';
		str +=             '<a class="nm3kTip" href="javascript:;" nm3kTip="'+ na.num +'">'+ na.num +'</a>';
		str +=         '</li>';
		str +=     '</ol>';
	for(i=0; i<subTxtArr.length; i++){
		str += '<span style="left:'+ (3+ i*28) +'px;">'+ subTxtArr[i] +'</span>';
	}//end for;
		str += '<span style="left:'+ (3+ i*28 + 15) +'px;">N/A</span>'
		str += '</dd></dl>';
	var $renderTo = $("#"+arg.renderTo); 
	$renderTo.html(str);
	var w = $renderTo.find(".nm3kColorRangeOl").outerWidth();
	$renderTo.find(".nm3kColorRange").width(w);
} */