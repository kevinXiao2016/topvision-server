//生成结果table的字符串;
function createResultTable(o){
    var str = '<div class="edge10" style="height:390px; overflow:auto;">';
	    str +=     '<p class="flagInfo">@Tip.successNum@:'+ o.succeedNum +', @Tip.failNum@:'+ o.failedNum +'</p>'
	    str +=     '<table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
		str +=         '<thead>';
		str +=             '<tr>';
		str +=                 '<th>@Performance.deviceName@</th>';
		str +=                 '<th width="160">@Performance.manageIp@</th>';
		str +=                 '<th width="160">@Performance.result@</th>';
		str +=             '</tr>';
		str +=         '</thead>';
		str +=         '<tbody>';
		for(var i=0; i<o["succeedDetail"].length; i++){//成功的部分;
			var obj = o["succeedDetail"][i];
		    str +=         '<tr>';
		    str +=             '<td class="pL5">'+ obj["deviceName"] +'</td>';
	        str +=             '<td class="pL5 txtCenter">'+ obj["deviceIp"] +'</td>';
		    str +=             '<td class="txtCenter"><b class="greenTxt">@Tip.success@</b></td>';
		    str +=         '</tr>';
		}
		for(i=0; i<o["failedDetail"].length; i++){
			var obj2 = o["failedDetail"][i];
			str +=     '<tr>';
		    str +=         '<td class="pL5">'+ obj2["deviceName"] +'</td>';
	        str +=         '<td class="pL5 txtCenter">'+ obj2["deviceIp"] +'</td>';
		    str +=         '<td class="txtCenter"><b class="redTxt">@Tip.fail@</b></td>';
		    str +=     '</tr>';
		}
		str +=         '</tbody>';
		str +=     '</table>'
		str += '</div>';
		str += '<div class="noWidthCenterOuter clearBoth">';
		str +=     '<ol class="upChannelListOl pB0 pT0 noWidthCenter">';
		str +=         '<li><a href="javascript:;" class="normalBtnBig" onclick="closeMessageWindow(\'applyAllTipWin\')"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>';
		str +=     '</ol>';
		str += '</div>';
		return str;
}


//resize事件增加[函数节流];
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
};
//全部开启或去部关闭;
function openOrCloseAll($me,para){
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
function renderOnOffNumber(para){
	var num;
	if(para == "on"){
		num = 1;
	}else if(para == "off"){
		num = 0;
	}
	return num;
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
//改变图片按钮;
function changeOnOff(para){
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
//改变图片按钮;
function renderNumToImg(num){
	var imgStr = '';
	switch(num){
		case 0:
			imgStr = '<img class="myImg" src="../../images/performance/off2.png" />';
			break;
		case 1:
			imgStr = '<img class="myImg" src="../../images/performance/on2.png" />';
			break;
	}
	return imgStr;
};
//设置统一时间;
function setAllTime(para){
	var notLoadingEnd = isLoadingEndStep2();
	if(notLoadingEnd == true){return;}
	$("#modifyMainPart").find("input").each(function(){
		var $me = $(this);
		if( !$me.hasClass("normalInputDisabled") ){
			$me.val(para);
		}
	})
};
//创建统一设置时间的按钮组;
function createSetTimeButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.UnitedConfigCycle@',
     	iconCls: 'bmenu_clock', 
     	handler: function(){this.showMenu()},
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.5Min@',
	       	        handler: function(){setAllTime(5)}
	       	    },{
	       	        text:'@Tip.10Min@',
	       	     handler: function(){setAllTime(10)}
	       	    },{
	       	        text:'@Tip.15Min@',
	       	     handler: function(){setAllTime(15)}
	       	    },{
	       	        text:'@Tip.30Min@',
	       	     handler: function(){setAllTime(30)}
	       	    },{
	       	        text:'@Tip.60Min@',
	       	     handler: function(){setAllTime(60)}
	       	    }]
     	})
	})
};
//获取step2中数据给后台，用于保存;
function getAllSetData(){
	var gData = flagObj.gData;
	var data = {};
	$("#modifyMainPart .modifyTable thead").each(function(i){
		var $me = $(this);
		var alt = $me.find(".thTit").attr("alt");
		data[alt] = {};
		$tbody = $("#modifyMainPart .modifyTable tbody").eq(i);
		var gInnerData = gData[alt];
		$tbody.find(":text").each(function(){
			var $this = $(this),
			alt2 = $this.attr("alt");

			(data[alt])[alt2] = {};
			((data[alt])[alt2])["collectInterval"] = Number($this.val());
			var enableStr = $this.parent().next().find("img").attr("alt");
			var enable = renderOnOffNumber(enableStr);
			((data[alt])[alt2])["targetEnable"] = enable;
			((data[alt])[alt2])["globalInterval"] = gInnerData[alt2].globalInterval;
			((data[alt])[alt2])["globalEnable"] = gInnerData[alt2].globalEnable; 
		})
		
	});//end each;
	return data;
};//end getAllSetData;
//高亮显示不同;
function showDifferent(){
	$(".differentTr").each(function(){
		var $this = $(this);
		if( $this.hasClass("redBg") ){
			$this.removeClass("redBg");		
		}else{
			$this.addClass("redBg");
		}
	})
};//end function;
function compare(str){
	return function(obj1, obj2) {
        var value1 = obj1[str];
        var value2 = obj2[str]
        if (value1 < value2) {
            return -1;
        } else if (value1 > value2) {
            return 1;
        } else {
            return 0;
        }
    }
}
//json动态生成板块,返回String;
function createJsonTable(jsonData){
	var dataStore = jsonData;
	var str = '<table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">';
	for(var key in dataStore){
		var innerData = dataStore[key];
		str += '<thead>';
		str += 		'<tr>';
		str += 			'<th colspan="9"><b class="thTit openThTit" alt="'+ key +'">';
		str +=				nm3kBatchData[key].name;			
		str += 			'</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="@COMMON.openAll@"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="@COMMON.closeAll@"></a>';
		if( nm3kBatchData[key].hasOwnProperty("tip") ){
			str +=          '<span class="thTip">' + nm3kBatchData[key]["tip"] + '</span>';
		}
		str +=          '</th>';
		str += 		'</tr>';
		str += '</thead>';
		str += '<tbody>';
		var len = 0;
		var testLen = 0;
		var innerArr = [];
		for(var key2 in innerData){
			innerArr.push(innerData[key2]);
			len++
		};//先获取一共有多少个属性;
		var nowNum = 0;
		
		
		
		innerArr.sort(compare('perfTargetName'));
		for(var i=0; i<innerArr.length; i++){
			var key2 = innerArr[i].perfTargetName;
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
			var inputValue = innerData[key2].collectInterval;
			var isOpen = innerData[key2].targetEnable;
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
		}
		/*for(var key2 in innerData){
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
			var inputValue = innerData[key2].collectInterval;
			var isOpen = innerData[key2].targetEnable;
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
*/	};//end for in;
	str += '</tobdy>';
	str += '</table>';
	return str;
}
function autoHeight(){
	var $modifyDiv = $("#modifyDiv");
	if( !$modifyDiv.hasClass("displayNone") ){
		var h = $(window).height(),
		h1 = $("#modifyToolBar").outerHeight(),
		h2 = h - h1 - 20;//因为padding:10px;
		if(h2 < 10){ h2 = 10}
		$("#modifyMainPart").height(h2);
	}
	var $step3Div = $("#contrastDiv");
	if( !$step3Div.hasClass("displayNone") ){
		var h = $(window).height(),
		h1 = $("#step3ToolBar").outerHeight(),
		h2 = h - h1 - 20;//因为padding:10px;
		if(h2 < 10){ h2 = 10}
		$("#step3Body").height(h2);
	}
}
function isLoadingEndStep2(){
	if( !flagObj.step2 ){
		
		return true;
	}else{
		return false;
	};
}
//返回按钮;
function backFn(){
	flagObj.step2 = false;
	$("#modifyDiv").addClass("displayNone");	
	try{
		//fRefresh();
		queryClick();
	}catch(ex){
		
	}
}
//应用到所有设备，生成表格;
function createContrastTable(thArr){
	//获取全部数据;
	var data = getAllSetData();
	$("#step3Body").empty();
	var str = '';
	str += '<table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
	str +=     '<thead>';
	str += 	       '<tr>';
	str += 	           '<th width="180"><label><input class="selectedAll" type="checkbox" /> '+ thArr[0] +'</label></th><th>'+ thArr[1] +'</th><th width="200">'+ thArr[2] +'</th><th width="200">'+ thArr[3] +'</th>';
	str += 	       '</tr>';
	str +=     '</thead>';
	for(var key in data){
		var innerData = data[key];
		str +=     '<thead>';
		str +=         '<tr>';
		
		str +=             '<td colspan="4" class="yellowBg"><b class="pL20 orangeTxt">'+ nm3kBatchData[key].name +'</b></td>';
		str +=         '</tr>';
		str +=     '</thead>';
		str +=     '<tbody>';
		for(var key2 in innerData){
			if(innerData[key2].collectInterval != innerData[key2].globalInterval || innerData[key2].targetEnable != innerData[key2].globalEnable){//用来做对比不同;
				str += '<tr class="differentTr">';
			}else{
				str += '<tr>';
			}
			var backDatar = key2 + "#" + innerData[key2].collectInterval + "#" + innerData[key2].targetEnable + "#" + key;//后台需要的数据格式;
			var modifyTip = nm3kBatchData[key].name + "#" + nm3kBatchData[key2].name + "#" + innerData[key2].collectInterval + "#" + innerData[key2].targetEnable; 
			str +=     '<td class="txtCenter">'+ '<input class="selectedOne" alt="' + modifyTip + '" type="checkbox" name="'+ backDatar +'" value=""  />' +'</td>';
			str +=     '<td class="txtCenter">'+ nm3kBatchData[key2].name +'</td>';
			var img1 = renderNumToImg(innerData[key2].targetEnable);
			var img2 = renderNumToImg(innerData[key2].globalEnable);
			str +=     '<td class="txtCenter">';
			str +=	       '<ul class="putUl">';
			str +=	           '<li class="putNumLi">';
			str +=    		       innerData[key2].collectInterval;
			str +=	           '</li>';
			str +=	           '<li>';
			str +=                 img1;
			str +=	           '</li>';
			str +=	       '</ul>';
			str +=     '</td>';
			str +=     '<td class="txtCenter">';
			str +=	       '<ul class="putUl">';
			str +=	           '<li class="putNumLi">';
			str +=    		       innerData[key2].globalInterval;
			str +=	           '</li>';
			str +=	           '<li>';
			str +=                 img2; 
			str +=	           '</li>';
			str +=	       '</ul>';
			str +=     '</td>';
			str += '</tr>';
		}
		str +=     '</tbody>';
	}
	str += '</table>';
	$("#step3Body").append(str);
};
function createGlobalTable(thArr){//创建全局配置的对比表格;
	//获取全部数据;
	var data = flagObj.gData;
	$("#step3Body").empty();
	var str = '';
	str += '<table class="contrastTable noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
	str +=     '<thead>';
	str += 	       '<tr>';
	str += 	           '<th width="180"><label><input class="selectedAll" type="checkbox"  /> '+ thArr[0] +'</label></th><th>'+ thArr[1] +'</th><th width="200">'+ thArr[2] +'</th><th width="200">'+ thArr[3] +'</th>';
	str += 	       '</tr>';
	str +=     '</thead>';
	for(var key in data){
		var innerData = data[key];
		str +=     '<thead>';
		str +=         '<tr>';
		
		str +=             '<td colspan="4" class="yellowBg"><b class="pL20 orangeTxt">'+ nm3kBatchData[key].name +'</b></td>';
		str +=         '</tr>';
		str +=     '</thead>';
		str +=     '<tbody>';
		for(var key2 in innerData){
			if(innerData[key2].collectInterval != innerData[key2].globalInterval || innerData[key2].targetEnable != innerData[key2].globalEnable){//用来做对比不同;
				str += '<tr class="differentTr">';
			}else{
				str += '<tr>';
			}
			var backDatar = key2 + "#" + innerData[key2].collectInterval + "#" + innerData[key2].targetEnable + "#" + key;//后台需要的数据格式;
			var modifyTip = key2 + "#" + innerData[key2].globalInterval + "#" + innerData[key2].globalEnable; 
			str +=     '<td class="txtCenter">'+ '<input class="selectedOne" alt="' + modifyTip + '" type="checkbox" name="'+ backDatar +'" value=""  />' +'</td>';
			str +=     '<td class="txtCenter">'+ nm3kBatchData[key2].name +'</td>';
			var img1 = renderNumToImg(innerData[key2].targetEnable);
			var img2 = renderNumToImg(innerData[key2].globalEnable);
			str +=     '<td class="txtCenter">';
			str +=	       '<ul class="putUl">';
			str +=	           '<li class="putNumLi">';
			str +=    		       innerData[key2].globalInterval;
			str +=	           '</li>';
			str +=	           '<li>';
			str +=                 img2;
			str +=	           '</li>';
			str +=	       '</ul>';
			str +=     '</td>';
			str +=     '<td class="txtCenter">';
			str +=	       '<ul class="putUl">';
			str +=	           '<li class="putNumLi">';
			str +=    		       innerData[key2].collectInterval;
			str +=	           '</li>';
			str +=	           '<li>';
			str +=                 img1; 
			str +=	           '</li>';
			str +=	       '</ul>';
			str +=     '</td>';
			str += '</tr>';
		}
		str +=     '</tbody>';
	}
	str += '</table>';
	$("#step3Body").append(str);
}
//点击使用全局配置中的使用按钮;
function inputGlobalData(){
	$(".contrastTable tbody").find(".selectedOne").each(function(){
		var $me = $(this);
		if( $me.attr("checked") == true || $me.attr("checked") == "checked" ){
			var altArr = $me.attr("alt").split("#");
			var $input = $(".modifyTable :text[alt='"+ altArr[0] +"']");
			$input.val(altArr[1]);
			var isOpen = Number(altArr[2]);
			if(isOpen == 1){
				$input.removeClass("normalInputDisabled").attr({
					"disabled" : false
				})
			}else{
				if( !$input.hasClass("normalInputDisabled") ){
					$input.addClass("normalInputDisabled")
				}
				$input.attr({
					"disabled" : true
				})
			}
			var img = renderIsOpenImg( isOpen );
			$input.parent().next().empty().html(img);
		}
	});//end each;
	backStep3ApplyAll();
};//end inputGlobalData;
function clickApplyAll(e){
	var $target = $(e.target);
	if( $target.hasClass("selectedAll") ){//点击的是全选;
		var ck = $target.attr("checked");
		if(ck == true || ck=="checked"){//全选;
			$("#step3Body .selectedOne").each(function(){
				var $me = $(this);
				if( $me.attr("disabled") == true || $me.attr("disabled") == "disabled"){
					//不对disabled的操作;
				}else{
					$me.attr({
						"checked" : true
					});
				}
			})
		}else{//全不选;
			$("#step3Body .selectedOne").attr({
				"checked" : false
			});
		}
	};
	if( $target.hasClass("selectedOne") ){//点击的是选择一个;
		var ck = $target.attr("checked");
		var flag = false;
		if(ck == true || ck=="checked"){//选中;
			$("#step3Body .selectedOne").each(function(){
				var $this = $(this);
				if( $this.attr("checked") == false){
					if($this.attr("disabled")== true || $this.attr("disabled") == "disabled"){
						//disabled不计算在内;
					}else{
						flag = true;
						return false;
					};
				};//end if;
			});//end each;
			if(flag == false){
				$("#step3Body .selectedAll").attr({
					"checked" : true
				})
			}
		}else{
			$("#step3Body .selectedAll").attr({
				"checked" : false
			});
		}
	};
	/*var $target = $(e.target);
	if( $target.hasClass("selectedAll") ){//点击的是全选;
		var ck = $target.attr("checked");
		var $tbody = $target.parent().parent().parent().next();
		if(ck == true || ck=="checked"){//全选;
			$tbody.find(":checkbox").attr({
				"checked" : true
			});
		}else{//全不选;
			$tbody.find(":checkbox").attr({
				"checked" : false
			});
		}
	};
	if( $target.hasClass("selectedOne") ){//点击的是选择一个;
		var ck = $target.attr("checked");
		var $thead = $target.parent().parent().parent().prev();
		var $tbody = $target.parent().parent().parent();
		if(ck == true || ck=="checked"){//选中;
			var flag = false;
			$tbody.find(":checkbox").each(function(){
				var $this = $(this);
				if( $this.attr("checked") == false ){
					flag = true;
					return false;
				}
			})
			if(flag == false){
				$thead.find(".selectedAll").attr({
					"checked" : true
				})
			}
		}else{//不选中;
			$thead.find(".selectedAll").attr({
				"checked" : false
			});
		};//end if;
	}*/
};//end function;
//如果和全局相同,则没有必要选中,默认高亮显示;
function hideSameColumn(){
	$(".contrastTable tbody :checkbox").each(function(){
		var $this = $(this);
		var $tr = $this.parent().parent();
		if( !$tr.hasClass("differentTr") ){
			$this.attr({
				"checked" : false,
				"disabled" : "disabled"
			})
			$tr.addClass("opacity5");
			$tr.find("td").addClass("opacity3");
			if( $tr.parent().get(0).tagName == "THEAD" ){
				$tr.removeClass("opacity5").find("td").removeClass("opacity3");
			}
		}
	})
}
//应用到所有的返回;
function backStep3ApplyAll(){
	Ext.getCmp("applyAllToolBar").destroy();
	var $contrastDiv = $("#contrastDiv");
	$contrastDiv.addClass("displayNone");
	$("#step3Body").empty();
	$("#step3Body").unbind("click",clickApplyAll);
}
//main.js中可以刷新，函数名已经定死，请查看main.js中_addView函数;
function doRefresh(url){
	if(url){
		window.location.href = url;
	}else{
		var currentUrl = window.location.href;
		window.location.href = currentUrl;
	}
}
//验证;
function testAllInput(){
	var flag = true;
	$("table.modifyTable").find(":text").each(function(){
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