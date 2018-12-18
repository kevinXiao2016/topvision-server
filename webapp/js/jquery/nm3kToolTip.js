/***************遇到带有toolTip的文本框则会自动弹出提示*******************/
$(function(){
	$(" input[tooltip], textarea[toolTip]").live("focus",function(){
		if($("#nm3kToolTip").length == 0){
			var tips = $(this).attr("toolTip");
			var str = '';
			str += '<div id="nm3kToolTip" style="display:none;">';
			str += '	<div class="nm3kToolTipTxt"></div>';
			str += '	<div class="nm3kArr"></div>';
			str += '</div>';
			$("body").append(str);
		}
		$("#nm3kToolTip").css("display","block");
		var content = $(this).attr("toolTip");
		if(content.startWith("String.")){
			content = eval(content);
		}else if(content.startWith("javascript:")){
			content = eval(content.substring(11));
		}
		var leftPos = $(this).offset().left;
		$("#nm3kToolTip .nm3kToolTipTxt").html(content);
		var h = $("#nm3kToolTip").height();
		var topPos = $(this).offset().top - h;
		$("#nm3kToolTip").css({"left":leftPos, "top":topPos});
		if($(this).hasClass("normalInput")){
			$(this).removeClass("normalInput").addClass("normalInputFocus");			
		}
	}).live("blur",function(){
		$("#nm3kToolTip").css("display","none");
		if($(this).hasClass("normalInputFocus")){
			$(this).removeClass("normalInputFocus").addClass("normalInput");			
		}
	});
	$("#nm3kToolTip").live("click",function(){
		$("#nm3kToolTip").css("display","none");
	});
	//end toolTip;
	
	//类似google翻译的一种黑色的提示框，主要用于对小图标的提示，比如告警的级别;
	$(".nm3kTip").live("mouseover",function(){
		var tips = $(this).attr("nm3kTip");
		if($("#nm3kTip").length == 0){
			$("body").append('<div id="nm3kTip"><div class="nm3kTipTxt"></div></div>');
		};		
		$('#nm3kTip > .nm3kTipTxt').empty().append(tips);
		$("#nm3kTip").css({left:-9999,top:-9999}).show();
		var w = $("#nm3kTip").outerWidth();
		var w2 = $(this).outerWidth();
		var l = $(this).offset().left - w/2 + w2/2;
		var t = $(this).offset().top - $("#nm3kTip").outerHeight();	
		$("#nm3kTip").css({left:l,top:t});
	}).live("mouseout",function(){
		$("#nm3kTip").hide();
	})
	$("#nm3kTip").live("click",function(){
		$("#nm3kTip").hide();
	});
	/*
	//给gridPanel在bar在刷新加toolTip;
	$("#extPagingBar .x-tbar-loading, .extPagingBar .x-tbar-loading").live("mouseover",function(){
		var tips = '@BASE/COMMON.refresh@';
		if($("#nm3kTip").length == 0){
			var str = '<div id="nm3kTip"><div class="nm3kTipTxt">'+ tips +'</div></div>';
			$("body").append(str);
		};		
		var $txt = $("#nm3kTip .nm3kTipTxt");
		$txt.text(tips);
		var w = $("#nm3kTip").outerWidth();
		var w2 = $(this).outerWidth();
		var l = $(this).offset().left - w/2 + w2/2;
		var t = $(this).offset().top - 34;	
		$("#nm3kTip").css({left:l,top:t,display:"block"});
	}).live("mouseout",function(){
		$("#nm3kTip").css("display","none");
	});*/
});//end document.ready;

var HeadMessage = {
	id: 'nm3kHeadMessage',
	position: 'bottom', //top,bottom
	afterMsg: '@Base/COMMON.alermAfterMsg@',
	//发送消息;
	sendMsg: function(txt){
		if(!this.exist()){
			this.createMsgDom(txt);
		}else{
			this.updateMsgDom(txt);
		}
		this.show();
	},
	//界面上是否已经存在消息窗口了;
	exist: function(){
		return $('#'+this.id).length === 1;
	},
	//创建一个消息窗口;
	createMsgDom: function(txt){
		var dom = [
		    '<div id="{0}" class="topMsgTips {1} displayNone">',
		    	'<div class="headMsgLeft">',
		    		'<dl>',
		    			'<dt>{2}</dt>',
		    			'<dd>{3}</dd>',
		    			'<dd class="mL10 lightGray">{4}</dd>',
		    		'</dl>',
		    	'</div>',
		    	'<div class="headMsgRight">',
	    			'<dl>',
		    			'<dd>',
		    				'<a href="javascript:;" class="normalBtn refreshBtn"><span><i class="miniIcoRefresh"></i>@Base/COMMON.refreshPage@</span></a>',
		    			'</dd>',
		    			'<dd>',
		    				'<a href="javascript:;" class="normalBtn closeBtn"><span><i class="miniIcoClose"></i>@Base/COMMON.closeTip@</span></a>',
		    			'</dd>',
		    		'</dl>',
		    	'</div>',
		    '</div>'
		].join('');
		var cls = 'bigZindex';
		if(this.position === 'bottom'){ cls='bigZindex HeadMessageBottom';}
		dom = String.format(dom, this.id, cls, '@Base/COMMON.alarmTip@@Base/COMMON.maohao@', txt, this.afterMsg);
		$('body').append(dom);
		this.initBtnClick();
	},
	//更新消息窗口的信息;
	updateMsgDom: function(txt){
		//提示被关闭，则更新dom再显示;
		//提示是打开的，则收起来，更新dom再显示;
		var $tip = $('#'+this.id),
		    me = this;
		
		if($tip.is(':hidden')){
			me.changeMsgDom(txt);
		}else{
			$tip.slideUp('fast', function(){
				me.changeMsgDom(txt);
			});
		}
	},
	//改变提示框的内容信息;
	changeMsgDom: function(txt){
		$('#'+this.id).find('.headMsgLeft dd:eq(0)').text(txt);
	},
	//绑定按钮点击事件;
	initBtnClick: function(){
		var me = this;
		$('#'+this.id).delegate('.headMsgRight a.refreshBtn', 'click', function(){
			me.refreshPage();
		});
		$('#'+this.id).delegate('.headMsgRight a.closeBtn', 'click', function(){
			me.closeMsg();
		});
	},
	//显示提示信息;
	show: function(){
		$('#'+this.id).slideDown('slow');
	},
	//刷新页面;
	refreshPage: function(){
		window.location.reload();
	},
	//关闭提示框;
	closeMsg: function(){
		$('#'+this.id).slideUp('slow');
	}
}
