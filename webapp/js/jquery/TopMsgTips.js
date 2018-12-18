/*
 * 		var tMsg = new TopMsgTips({
			user:'admin',
			time:'2012-09-30 14:22:06',
			content:'请大家注意,10分钟后数据库重新启动!',
			nowPage:'1',
			allPage:'10'			
		});
		tMsg.init();
 */
function TopMsgTips(o){
		this.user = o.user;
		this.time = o.time;
		this.content = o.content;
		this.nowPage = o.nowPage;
		this.allPage = o.allPage;
	}
	//TopMsgTips.prototype.constructor = "TopMsgTips";
	TopMsgTips.prototype.init = function(){
		if($("#topMsgTips").length == 0){
			var str = '';
			str += '<div id="topMsgTips" class="topMsgTips displayNone">';
			str += '	<table>';
			str += '		<tr>';
			str += '			<td class="pL10"><a id="topMsgTipLeftArr" href="javascript:;" class="leftArrBlue"></a></td>';
			str += '			<td class="pL10 pR10"><span class="jsNowPage">'+this.nowPage+'</span>/<span class="jsAllPage">'+this.allPage+'</span></td>';
			str += '			<td><a id="topMsgTipRightArr" href="javascript:;" class="rightArrBlue"></a></td>';
			str += '			<td><b class="orangeTxt pL20 jsUser">'+ this.user +'：</b></td>';
			str += '			<td class="jsContent">'+ this.content +'</td>';
			str += '			<td class="miniTxt pL5 jsTime">'+ this.time +'</td>';
			str += '		</tr>';
			str += '	</table>';
			str += '	<a class="topMsgTipsClose" href="javascript:;"></a>';
			str += '</div>'; 
			$("body").append(str);			
			this.correctArr();			
			$("#topMsgTips").slideDown('slow');
			var _this = this;
			$("#topMsgTips").find(".topMsgTipsClose").bind("click",function(){
				_this.die();
			})
		}else{
			$("#topMsgTipLeftArr").unbind("click",this.leftArrClick);//移除点击左侧箭头;
			$("#topMsgTipRightArr").unbind("click",this.rightArrClick);//移除点击右侧箭头;
			var _this = this;
			$tip = $("#topMsgTips");						
			$tip.slideUp('normal',function(){
				//先收缩;
				$tip.find(".jsNowPage").text(_this.nowPage);
				$tip.find(".jsAllPage").text(_this.allPage);				
				$tip.find(".jsUser").text(_this.user + "：");
				$tip.find(".jsContent").html(_this.content);
				$tip.find(".jsTime").text(_this.time);
				_this.correctArr();
				$tip.slideDown();
			});
		}
		$("#topMsgTipLeftArr").bind("click",this,this.leftArrClick);//点击左侧箭头;
		$("#topMsgTipRightArr").bind("click",this,this.rightArrClick);//点击右侧箭头;
	};//end init;
	
	TopMsgTips.prototype.leftArrClick = function(e){
		var _this = e.data;
		if($(this).attr("class") == "leftArrBlueDisabled"){//如果是第一个，则无法点击;
			return;
		}else{//切换到上一个;			
			var tabPage = _this.nowPage - 1;			
			var dataIndex = tabPage-1;			
			if( billboardStore.length>0 ){
				var notice = billboardStore[dataIndex];
				tMsg = new TopMsgTips({
					user: notice.username,
					time: notice.createTimeString,
					content: notice.content,
					nowPage: tabPage,
					allPage: billboardStore.length
				});
				tMsg.init();
			}
		};//end if;
	};//end leftArrClick;
	
	TopMsgTips.prototype.rightArrClick = function(e){
		var _this = e.data;
		if($(this).attr("class") == "rightArrBlueDisabled"){//如果是最后一个，则无法点击;
			return;
		}else{//切换到下一个;
			var tabPage = _this.nowPage + 1;
			if( billboardStore.length>0 ){
				var notice = billboardStore[ tabPage-1 ];
				tMsg = new TopMsgTips({
					user: notice.username,
					time: notice.createTimeString,
					content: notice.content,
					nowPage: tabPage,
					allPage: billboardStore.length
				});
				tMsg.init();
			}
		};//end if;
	};//end rightArrClick;
	
	TopMsgTips.prototype.correctArr = function(){
		var $tips = $("#topMsgTips");
		if($tips.find(".jsNowPage").text() == "1"){//如果当前是第一页,那么没有向左的箭头(变灰色);
			$tips.find("#topMsgTipLeftArr").attr("class","leftArrBlueDisabled"); 
		}else{
			$tips.find("#topMsgTipLeftArr").attr("class","leftArrBlue");
		}
		if($tips.find(".jsNowPage").text() == $tips.find(".jsAllPage").text()){//如果是最后一页,那么没有向右的箭头;
			$tips.find("#topMsgTipRightArr").attr("class","rightArrBlueDisabled");
		}else{
			$tips.find("#topMsgTipRightArr").attr("class","rightArrBlue");
		}
	}
	
	//点击关闭,提示信息消失，并且指定位置，告诉你它去了哪里;
	TopMsgTips.prototype.die = function(){
		var $msg = $("#topMsgTips");
		var h = $msg.outerHeight()+"px";
		var w = $msg.outerWidth() + "px";
		var str = '<div class="fackTopMsgTips" style="width:'+ w +'; height:'+h+'; background:#FFFFE1; position:absolute; top:0px; left:0px;border:1px dashed #333;"></div>'
		$("body").append(str);
		var $t = $("a.headerRightBtn5");
		var targetL = $t.offset().left;
		var targetT = $t.offset().top;
		var targetW = $t.outerWidth();
		var targetH = $t.outerHeight();
		
		$(".fackTopMsgTips").animate({left: targetL,top: targetT,width: targetW,height: targetH},"normal",function(){
			var $this = $(this)
			$this.fadeTo('fast',0.2,function(){
				$this.fadeTo('fast',1,function(){
					$this.remove();					
				})	
			})
		})
		
		$("#topMsgTips").fadeOut('fast',function(){
			$(this).empty().remove();			
		});
	};//end die
	
	
	