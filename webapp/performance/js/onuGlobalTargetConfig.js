$(window).load(function(){
	createMainPart({
		"data" : oBackData,
		"divId" : "mainPart"
	});
	addOther();
	setOnOff({
		id : "bindTemp",
		isOpen : isBindDftTemp
	});
	setOnOff({
		id : "perfAlert",
		isOpen : isPerfThdOn
	});
	
	createToolBar();
});//end document.ready;

function refreshFunc(){
	window.location.href = window.location.href;
}

//建立toolbar;
function createToolBar(){
	new Ext.Toolbar({
        renderTo: "topPart",
        id:"toolBar",
        items: [
			{text: '@Tip.save@',cls:'mL10', iconCls: 'bmenu_data', handler: fSave},'-',
       		{text: '@Tip.reset@', iconCls: 'bmenu_miniIcoBack', handler: fRest},'-',
       		fCreateSetTimeButton(),'-',
       		{text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: refreshFunc}
		]
	});//end ToolBar;
}
//获取json数据给后台，用于保存;
function getAllSetData(){
	var data = {};
	$("table.modifyTable:eq(1) thead").each(function(i){
		var $me = $(this);
		var alt = $me.find(".thTit").attr("alt");
		data[alt] = {};
		$tbody = $("table.modifyTable:eq(1) tbody").eq(i);
		$tbody.find(":text").each(function(){
			var $this = $(this),
			alt2 = $this.attr("alt");

			(data[alt])[alt2] = {};
			((data[alt])[alt2])["collectInterval"] = Number($this.val());
			var enableStr = $this.parent().next().find("img").attr("alt");
			var enable = renderOnOffNumber(enableStr);
			((data[alt])[alt2])["targetEnable"] = enable; 
		})
	});//end each;
	return data;
};//end getAllSetData
function renderOnOffNumber(para){
	var num;
	if(para == "on"){
		num = 1;
	}else if(para == "off"){
		num = 0;
	}
	return num;
}
function jsonToArr(data){
	//后台不需要json对象，需要字符串,因此将json解析成包含字符串的数组;
	var targetArray = [];
	for(var key in data){
		var innerData = data[key];
		for(var key2 in innerData){
			var tempStr = key2 + "#";
			tempStr += innerData[key2].collectInterval + "#";
			tempStr += innerData[key2].targetEnable + "#";
			tempStr += key;
			targetArray.push(tempStr)
		}
	}
	return targetArray;
}
//保存;
function fSave(){
	var reg = testAllInput();
	if(!reg){return};
	
	var o = getAllSetData();
	var arr = jsonToArr(o);
	
	var bindTempNum = onOffRenderToNum( $("#bindTemp").attr("alt") );
	var perfAlertNum = onOffRenderToNum( $("#perfAlert").attr("alt") );
	//保存为全局配置
	$.ajax({
		url: '/onu/onuPerfGraph/modifyOnuGlobalTarget.tv',
    	type: 'POST',
    	data: {
    		targetData : arr,
    		isBindDftTemp : bindTempNum,
    		isPerfThdOn : perfAlertNum
    	},
    	dataType:"json",
   		success: function(result) {
   			oBackData = result;
   			isBindDftTemp = bindTempNum;
   			isPerfThdOn = perfAlertNum;
   			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '<p><b class="orangeTxt">@Performance.saveGlobalSuc@</b></p>'
			})
   		}, error: function(result) {
		}, 
		async : false,
		cache: false
	});			
}
//重置;
function fRest(){
	var dataStore = window.oBackData;
	for(var key in dataStore){
		var innerData = dataStore[key];
		for(var key2 in innerData){
			var $input = $(".modifyTable:eq(1) tbody").find(':text[alt="'+ key2 +'"]');
			$input.val(innerData[key2].globalInterval);
			if( Number(innerData[key2].globalEnable) == 1){
				$input.removeClass("normalInputDisabled").attr({
					"disabled" : false
				});
			}else{
				if( !$input.hasClass("normalInputDisabled") ){
					$input.addClass("normalInputDisabled")
				}
				$input.attr({"disabled" : true});
			}
			var imgSrc = renderIsOpenImg( Number(innerData[key2].globalEnable) );
			var $imgTd = $input.parent().next();
			$imgTd.html(imgSrc);
		}
	}
	//上半部分内容不重置，先注释掉;
	changeSrcAndAlt({
		id : "bindTemp",
		isOpen : Number(window.isBindDftTemp)	
	});
	changeSrcAndAlt({
		id : "perfAlert",
		isOpen : Number(window.isPerfThdOn)	
	});
}


//设置统一时间;
function fSetAllTime(para){
	$("#mainPart").find("input").each(function(){
		var $me = $(this);
		if( !$me.hasClass("normalInputDisabled") ){
			$me.val(para);
		}
	})
};

function addOther(){
	var str = '<div class="pB0"><table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">';
	str += '<thead>';
	str += 		'<tr>';
	str += 			'<th colspan="9"><b class="thTit openThTit" alt="">';
	str +=				'@Performance.thresholdTemplate@';			
	str += 			'</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="@tip.openAll@"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="@tip.closeAll@"></a></th>';
	str += 		'</tr>';
	str += '</thead>';
	str += '<tbody>';
	str +=      '<tr>';
	str +=          '<td class="rightBlueTxt w200">@Performance.relaDefualtTemplate@:</td>';
	str +=          '<td width="100"><img id="bindTemp" src="../../images/performance/on.png" alt="on" class="scrollBtn"></td>';
	str +=          '<td colspan="7" class="orangeTxt">@Performance.relaDefualtTemplateDesc@</td>';
	str +=      '</tr>';
	str +=      '<tr>';
	str +=          '<td class="rightBlueTxt w200">@Performance.perfAlert@:</td>';
	str +=          '<td><img id="perfAlert" src="../../images/performance/on.png" alt="on" class="scrollBtn"></td>';
	str +=          '<td colspan="7" class="orangeTxt">@Performance.perfAlertDesc@</td>';
	str +=      '</tr>';
	str += '</tbody>';	
	str += '</table></div>';
	$("#mainPart").prepend(str);
}	
