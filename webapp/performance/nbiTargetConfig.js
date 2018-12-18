$(function(){
	$.ajax({
	    url: '/nbi/loadNbiTargetList.tv',
	    dataType:"json",
	    success: function(json) {
	    	//console.log(json)
	    	createToolbar();
	    	var o = {
		    		divId : 'mainPart',
		    		data : json
		    	}
	    	createMainPart(o);
	    }, cache: false
	});
});//end document.ready;

function createToolbar(){
	new Ext.Toolbar({
        renderTo: "topPart",
        id:"toolBar",
        items: [
			{text: '@Tip.save@',cls:'mL10', iconCls: 'bmenu_data', handler: saveClick},
			'-',
			{text:'@COMMON.ConfigurationTime@', iconCls:'bmenu_clock', 
    			menu:[{
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
			   }
		   ]}
		]
	});//end ToolBar;
}
//保存;
function saveClick(){
	var data = [],
	    flag = true;
	
	$("#mainPart :text").each(function(){
		var $me = $(this);
		var reg = /^[0-9]\d*$/;
		if(parseInt($me.val(),10) < 1 || parseInt($me.val(),10) > 1440 || !reg.test($me.val())){
			if($me.prop("disabled")){
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
		data.push({
			selected : !$me.prop("disabled"),
			period : $me.val(),
			perfindex : parseInt($me.attr("data-perfindex"), 10)
		})
	});
	if(!flag){return;}
	$.ajax({
        url: '/nbi/updateNbiTargetConfig.tv',
        type: 'POST',
        data: {
        	nbiArrStr: JSON.stringify(data)
        },
        success: function(text) {
			top.afterSaveOrDelete({
				title : '@COMMON.tip@',
				html : '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
			});
        }, 
        error: function(text) {
        	window.top.showErrorDlg();
    }, cache: false
    });
}

///////////////////////////////////////////////////////
//           根据后台数据，动态生成页面;
//           o.data     后台json数据;
//           o.divId    要渲染到的div的id;
///////////////////////////////////////////////////////
function createMainPart(o){
	var dataStore = o.data,
	$mainPart = $("#" + o.divId);
	
	var str = '<table cellpadding="0" cellspacing="0" border="0" class="modifyTable" width="100%">';
	for(var i=0; i<dataStore.length; i++){
		str += '<thead>';
		str += 		'<tr>';
		str += 			'<th colspan="9"><b class="thTit openThTit">';
		str +=				dataStore[i].displayName;			
		str += 			'</b><a href="javascript:;" class="blueArr nm3kTip" nm3kTip="@COMMON.openAll@"></a><a href="javascript:;" class="redArr nm3kTip" nm3kTip="@COMMON.closeAll@"></a>';
		str +=          '</th>';
		str += 		'</tr>';
		str += '</thead>';
		str += '<tbody>';
		
		var nbiTargetList = dataStore[i].nbiTargetList,
            len = nbiTargetList.length, //一共有多少个子对象,每个tr内将会放置3个子对象;
            nowNum = 0,                 //现在是第几个;
            testLen = 0;                //判断最后一个，当最后一个不是3的倍数的时候，需要适当的加上colspan;
		
		for(var j=0; j<len; j++){
			var nowNbiTargetList = nbiTargetList[j];
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
			str += 		nowNbiTargetList.displayName;
			str += '</td>';
			str += '<td width="100">';
			if(nowNbiTargetList.selected === true){
				str += 		String.format('<input type="text" class="normalInput" data-period="{0}" tooltip="@Performance.inputCycle@" data-perfIndex="{1}" style="width:50px;" value="{0}" /> @ENTITYSNAP.deviceUpTime.minute@', nowNbiTargetList.period, nowNbiTargetList.perfIndex);
			}else{
				str += 		String.format('<input type="text" disabled="dsiabled" class="normalInputDisabled"  tooltip="@Performance.inputCycle@" data-period="{0}" data-perfIndex="{1}" style="width:50px;" value="{0}" /> @ENTITYSNAP.deviceUpTime.minute@', nowNbiTargetList.period, nowNbiTargetList.perfIndex);
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
			str += 		renderIsOpenImg(nowNbiTargetList.selected);
			str += '</td>';
			
			
			if(nowNum == 3){
				str += '</tr>';
			};
		}
	};//end for in;
	str += '</tbody>';
	str += '</table>';
	$mainPart.html(str);
	fBindClick(o.divId);
}

function renderIsOpenImg(para){
	var str;
	if(para === true){
		str = '<img src="../../images/performance/on.png" alt="on" class="scrollBtn" />';
	}else{
		str = '<img src="../../images/performance/off.png" alt="off" class="scrollBtn" />';
	}
	return str;
}
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
//设置统一修改时间;
function fSetAllTime(num){
	$("#mainPart :input").val(num);
}