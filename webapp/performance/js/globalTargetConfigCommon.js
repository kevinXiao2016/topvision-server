///////////////////////////////////////////////////////
//           根据后台数据，动态生成页面;
//           o.data     后台json数据;
//           o.divId    要渲染到的div的id;
///////////////////////////////////////////////////////
function createMainPart(o){
	var dataStore = o.data,
	$mainPart = $("#" + o.divId);
	
	var str = '<table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">';
	for(var key in dataStore){
		var innerData = dataStore[key];
		str += '<thead>';
		str += 		'<tr>';
		str += 			'<th colspan="9"><b class="thTit openThTit" alt="'+ key +'">';
		str +=				nm3kBatchData[key].name;			
		str += 			'</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="@tip.openAll@"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="@tip.closeAll@"></a>';
		if( nm3kBatchData[key].hasOwnProperty("tip") ){
			str +=          '<span class="thTip">' + nm3kBatchData[key]["tip"] + '</span>';
		}
		str +=          '</th>';
		str += 		'</tr>';
		str += '</thead>';
		str += '<tbody>';
		var len = 0;
		var testLen = 0;
		for(var key2 in innerData){len++};//先获取一共有多少个属性;
		var nowNum = 0;
		for(var key2 in innerData){
			testLen ++;
			if(nowNum == 0){
				str += '<tr>';
			}
			nowNum++;
			if(nowNum == 4){
				nowNum = 1;
				str += '<tr>';
			}
			str += '<td class="rightBlueTxt w200">';
			if( nm3kBatchData[key2].hasOwnProperty("tip") ){
				var tipHtml = nm3kBatchData[key2]["tip"];
				str +=  '<img src="/images/performance/Help.png" class="tipImg" alt="'+ tipHtml +'" title="'+ tipHtml +'" />';
			}
			str += 		nm3kBatchData[key2].name;
			str += '</td>';
			str += '<td width="100">';
			var inputValue = innerData[key2].globalInterval;
			var isOpen = innerData[key2].globalEnable;
			if(isOpen == 1){
				if( nm3kBatchData[key2].hasOwnProperty("toolTip") ){
					var toolTipStr = nm3kBatchData[key2]["toolTip"];
					str += 		'<input type="text" toolTip="'+ toolTipStr +'" class="normalInput" alt="'+ key2 +'" style="width:50px;" value="'+ inputValue +'" /> @ENTITYSNAP.deviceUpTime.minute@';
				}else{
					str += 		'<input type="text" class="normalInput" alt="'+ key2 +'" style="width:50px;" value="'+ inputValue +'" /> @ENTITYSNAP.deviceUpTime.minute@';
				}
			}else {
				if( nm3kBatchData[key2].hasOwnProperty("toolTip") ){
					var toolTipStr = nm3kBatchData[key2]["toolTip"];
					str += 		'<input type="text" toolTip="'+ toolTipStr +'" class="normalInput normalInputDisabled" disabled="disabled" alt="'+ key2 +'" style="width:50px;" value="'+ inputValue +'" /> @ENTITYSNAP.deviceUpTime.minute@';
				}else{
					str += 		'<input type="text" class="normalInput normalInputDisabled" disabled="disabled" alt="'+ key2 +'" style="width:50px;" value="'+ inputValue +'" /> @ENTITYSNAP.deviceUpTime.minute@';
				}
			}
			str += '</td>';
			//通过对比，填补空白,如果最后一个不是3，那么他的colspan就该合并一些单元格了;
			if(testLen == len){
				switch(nowNum){
					case 1:
						str += '<td colspan="7">';
						break;
					case 2:
						str += '<td colspan="4">';
						break;
					case 3:
						str += '<td>';
						break;
				}
			}else{
				str += '<td>';
			}
			
			str += 		renderIsOpenImg(isOpen);
			str += '</td>';
			if(nowNum == 3){
				str += '</tr>';
			};
		};//end for in;
	};//end for in;
	str += '</tobdy>';
	str += '</table>';
	$mainPart.html(str);
	fBindClick(o.divId);
}
function renderIsOpenImg(para){
	var str;
	if(para == 1){
		str = '<img src="../../images/performance/on.png" alt="on" class="scrollBtn" />';
	}else{
		str = '<img src="../../images/performance/off.png" alt="off" class="scrollBtn" />';
	}
	return str;
}
//点击重置，改变顶部阈值模板的src和alt;
function changeSrcAndAlt(o){
	var $me = $("#" + o.id);
	switch(o.isOpen){
	    case 1:
	    	$me.attr({
	    		src : "../../images/performance/on.png",
	    		alt : "on"
	    	})
	    	break;
	    case 0:
	    	$me.attr({
	    		src : "../../images/performance/off.png",
	    		alt : "off"
	    	})
	    	break;
	}
};

// 创建统一设置时间的按钮组;
function fCreateSetTimeButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.UnitedConfigCycle@',
     	iconCls: 'bmenu_clock', 
     	handler: function(){this.showMenu()},
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.5Min@',
	       	        handler: function(){fSetAllTime(5)}
	       	    },{
	       	        text:'@Tip.10Min@',
	       	     handler: function(){fSetAllTime(10)}
	       	    },{
	       	        text:'@Tip.15Min@',
	       	     handler: function(){fSetAllTime(15)}
	       	    },{
	       	        text:'@Tip.30Min@',
	       	     handler: function(){fSetAllTime(30)}
	       	    },{
	       	        text:'@Tip.60Min@',
	       	     handler: function(){fSetAllTime(60)}
	       	    }]
     	})
	})
};

//事件代理(委托)，绑定点击事件;
function fBindClick(divId){
	$("#"+ divId).click(function(e){
		var $me = $(e.target);
		if($me.hasClass("scrollBtn")){//点击滑动按钮;
			fChangeOnOff($me);
		}
		if($me.hasClass("blueArr")){
			fOpenOrCloseAll($me,"on");
		}
		if($me.hasClass("redArr")){
			fOpenOrCloseAll($me,"off");
		}
		if($me.hasClass("thTit")){
			var $tbody = $me.parent().parent().parent().next();
			if($me.hasClass("openThTit")){//点击的是减号;
				$me.removeClass("openThTit").addClass("closeThTit");
				$tbody.css({display : "none"});
			}else if($me.hasClass("closeThTit")){
				$me.removeClass("closeThTit").addClass("openThTit");
				$tbody.css({display : ""});
			}
		}
	});//end click;
};//end fBindClick;

//改变图片按钮;
function fChangeOnOff(para){
	var $me = para;
	var $input = $me.parent().prev().find(":text");
	if($me.attr("alt") == "on"){
		$me.attr({
			"alt" : "off",
			"src" : "../../images/performance/off.png"
		});
		if($input.length == 1){
			$input.attr({
				"disabled" : true
			}).addClass("normalInputDisabled");
		};
	}else{
		$me.attr({
			"alt" : "on",
			"src" : "../../images/performance/on.png"
		});
		if($input.length == 1){
			$input.attr({
				"disabled" : false
			}).removeClass("normalInputDisabled")
		};
	}
};
//全部开启或去部关闭;
function fOpenOrCloseAll($me,para){
	var $thead = $me.parent().parent().parent();
	if($thead.next().get(0).tagName == "TBODY"){
		var $img = $thead.next().find(".scrollBtn"); 
		if(para == "on"){
			$img.each(function(){
				var $me = $(this);
				var $input = $me.parent().prev().find(":text");
				$me.attr({
					"alt" : "on",
					"src" : "../../images/performance/on.png"
				})
				if($input.length == 1){
					$input.attr({
						"disabled" : false
					}).removeClass("normalInputDisabled");
				};
			})
		}else if(para == "off"){
			$img.each(function(){
				var $me = $(this);
				var $input = $me.parent().prev().find(":text");
				$me.attr({
					"alt" : "off",
					"src" : "../../images/performance/off.png"
				})
				if($input.length == 1){
					$input.attr({
						"disabled" : true
					}).addClass("normalInputDisabled");
				};
			})
		}
	}
};

function onOffRenderToNum(str){
	switch(str){
		case "on":
			return 1;
			break;
		case "off":
			return 0;
			break;
	}
}

//验证;
function testAllInput(){
	var flag = true;
	$("table.modifyTable:eq(1)").find(":text").each(function(){
		var $me = $(this),
		inputAlt = $me.attr("alt");
		if( nm3kBatchData[inputAlt].hasOwnProperty("reg") ){
			var myTest = (nm3kBatchData[inputAlt].reg)( Number($me.val()) );
			if(!myTest){
				if($me.attr("disabled") == true || $me.attr("disabled") == "disabled"){
					if( !$me.is(":animated") ){
						$me.animate({opacity:0},"fast",function(){
							$(this).animate({opacity:1})
						})
					}
				}else{
					$me.focus();
				}
				flag = false;
				return false;
			}
		}
	});//end each;
	return flag;
}

function setOnOff(o){
	//{id:,isOpen:}
	var $me = $("#"+ o.id);
	switch( Number(o.isOpen) ){
		case 0:
			$me.attr({
				src : "../../images/performance/off.png",
				alt : "off"
			})
			break;
		case 1:
			$me.attr({
				src : "../../images/performance/on.png",
				alt : "on"
			})
			break;
	}
}

//main.js中可以刷新，函数名已经定死，请查看main.js中_addView函数;
function doRefresh(){
	var hrefSrc = window.location.href;
	window.location.href = hrefSrc; 
}