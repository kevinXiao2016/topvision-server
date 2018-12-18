var Nm3kDropDownTree = (function(){
	var nm3kDropDownTree = function(o){
		this.id = o.id;
		this.renderTo = o.renderTo;
		this.width = o.width; //上面selected的宽度;
		this.subWidth = o.subWidth ? o.subWidth : 220;//底部弹出窗的宽度;
		if(this.subWidth < this.width){this.subWidth = this.width-10};//下部宽度不能小于上部宽度，否则会很丑,减去10是因为padding:5px;
		this.subMinHeight = o.subMinHeight ? o.subMinHeight : 40;//底部弹出框的最小高度,默认为40;
		this.subId = o.id + "_Nm3kDropDownTreeBody";//底部弹出的id,自动生成;		
		this.isOpen = false;//是否已经打开下面选择部分;
		if(o.subHeight){ this.subHeight = o.subHeight;}//是否要固定下部的高度;
		this.subTree = null;//存zTree对象;
		this.treeData = o.treeData;
		this.setting = o.setting;
	}
	
	nm3kDropDownTree.prototype = {
		init : function(){  //生成上半部分，下拉选框;
			var topStr = ''
			topStr += '<div class="Nm3kLevelSelect" style="width:'+ this.width +'px;">';
			topStr +=	'<p></p>';
			topStr +=	'<a class="Nm3kLevelSelectArr" href="javascript:;"></a>';
			topStr +=  '<input type="hidden" id="'+ this.id +'" name="'+ this.id +'" />'
			topStr += '</div>'
			$("#"+this.renderTo).html(topStr);
			this.createSub(); //生成下半部分dom;
			this.bindEvent(); //绑定事件;
			
		},
		createSub : function(){    //生成下班部分;
			var $topDiv = $("#" + this.renderTo).find(".Nm3kLevelSelect");
			var l = $topDiv.offset().left;
			var t = $topDiv.offset().top + $topDiv.outerHeight() - 1;
			
			var str = '';
			str += '<div class="Nm3KLevelSelectSub" id="'+ this.subId +'" style="width:'+ this.subWidth +'px; display:none; left:'+ l +'px; top:'+ t +'px;"><div class="getHeight">';
			str += '<ul class="ztree" id="'+ this.subId+'_ul' +'"></ul>';
			str += '</div></div>';
			$("body").append(str);
			this.subTree = $.fn.zTree.init($("#"+ this.subId + " ul.ztree"), this.setting, this.treeData);
			if(this.subHeight){
				$("#"+ this.subId).height(this.subHeight);
			}
		},
		bindEvent : function(){
			var $topDiv = $("#" + this.renderTo).find(".Nm3kLevelSelect");
			//点击上部div,弹出下面弹出框;
			$topDiv.bind("click",this,this.showSub);
			$("html").bind("click",this,this.bodyClick);
			$(window).bind("resize",this,this.bodyClick);
		},
		showSub : function(e){
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
		},
		bodyClick : function(e){
			if(!e){e = window.event;}
			var obj = e.srcElement ? e.srcElement : e.target;
			if( e.data.isOpen && obj.id.indexOf(e.data.id) !== 0){
				e.data.isOpen = false;
				var $subId = $("#"+ e.data.subId);
				$subId.css({display:"none"});
			}
		}
	};//end prototype;
	return nm3kDropDownTree;
})();