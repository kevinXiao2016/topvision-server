var nm3k = {};
nm3k.aHelp = [
    {
		id : 'cmListNew', //cm列表;
		src : 'cmlistNew.jsp'
	},
	{
		id : 'alertViewer', //告警查看器;
		src : 'alertViewer.jsp'
	},
	{
		id : 'batchTopo', //批量拓扑;
		src : 'batchTopo.jsp'
	}
];

/*
 * 根据传递过来的id，判断是否要显示帮助信息;
 * 所有信息存储在nm3k的aHelp下;
*/

function displayHelp(paraId){
	//src = paraId + '_zh_CN' + '.jsp
	var $helpCir = $("#helpCir"),
	    aHelp = nm3k.aHelp,
	    len = aHelp.length;
	
	for(var i=0; i<len; i++){
		if(paraId === aHelp[i].id){
			$helpCir.css({display:'block'});
			$helpCir.data({src : aHelp[i].src});
			return;
		}
	}
	$helpCir.css({display:'none'});	
}

/**
 * 当前页面是否有提示信息
 * @returns {Boolean}
 */
function pageHasHelp() {
	return $("#helpCir").css("display") !== "none";
}
/*
 * iframe内部的页面，通过document.ready的时候显示帮助信息;
 */
function showHelpIco(para){
	var $helpCir = $("#helpCir");
	$helpCir.css({display:'block'}).data({src : para});
}

/*
 * 点击cm列表的第一次黑色提示信息，逐渐消失到右上角;
 */
function fadeCmlistTip(){
	if($("#cmlistTip").length === 0){
		var w = contentPanel.getInnerWidth()-2,
	        h = contentPanel.getInnerHeight()-2,
			p = contentPanel.getPosition(),
			l = p[0],
			t = p[1] + 42,
			$helpCir = $("#helpCir"),
			t_w = $helpCir.width(),
		    t_h = $helpCir.height(),
			t_l = $helpCir.offset().left,
		    t_t = $helpCir.offset().top;
		
		$("body").append('<div id="cmlistTip" style="width:'+ w +'px;height:'+ h +'px; background:#000; -webkit-opacity:0.5; -moz-opacity:0.5; -o-opacity:0.5; opacity:0.5; filter:alpha(opacity=50); border:1px dashed #fff; position:absolute; left:'+ l +'px; top:'+ t +'px; z-index:99999"></div>');
		$("#cmlistTip").animate({width : t_w, height : t_h, left : t_l, top : t_t}, 'slow',function(){
			var $me = $(this);
			$me.fadeOut(function(){
				$me.fadeIn(function(){
					$me.fadeOut(function(){
						$me.remove();
					})
				});
			});
		});//end animate;		
	}
}

//加载提示信息页面;
function addCartoonTip(){
	if($("#pngTipBg").length == 0){
		var str  = '<div id="pngTipBg"></div>';
			str += '<div id="noEqTip" class="noEqTip @list.noEqCss@">';
			str += 		'<a href="javascript:;" class="close" onclick="closeCartoonTip()"></a>';
			str += 		'<a href="javascript:;" class="iSee" onclick="closeCartoonTip()"></a>';
			str += 		'<a href="javascript:;" class="noMore" onclick="noTrouble()"></a>';
			str += 		'<a href="javascript:;" class="downloadMovie" onclick="downloadMovie()"></a>';
			str += '</div>';
		$("body").append(str);
		if(frames["menuFrame"].$("#batchDevice").length == 1){//通过计算，算出按钮的位置;
			var $link = frames["menuFrame"].$("#batchDevice");
			var bottomPos = frames["menuFrame"].$("body").outerHeight() - $link.offset().top - $link.outerHeight() - 17;
			$("#noEqTip").css({bottom:bottomPos});
		}
	}else{
		$("#pngTipBg, #noEqTip").css({display: "block"});		
	}
}
//关闭提示信息;
function closeCartoonTip(){
	$("#pngTipBg, #noEqTip").css({display:"none"});
	
}
//不再提示;
function noTrouble(){
	closeCartoonTip();
	//ajax后台操作数据库;
	$.ajax({
		url : '/network/saveEntitySnapView.tv',
		type : 'POST',
		data : {
			showCartoon: true
		},
		dataType : 'json',
		success : function(json) {
		},
		error : function(json) {
		},
		cache : false
	});
}
//下载视频;
function downloadMovie(){
	closeCartoonTip();
	if($("#downloadFrame").length == 1){
		$("#downloadFrame").remove();		
	}	
	var elemIF = document.createElement("iframe");
	elemIF.src = "/references/ufo/@list.noEqCss@.avi";//文件路径
	elemIF.id = "downloadFrame";
	elemIF.style.display = "none";  
	document.body.appendChild(elemIF);  
}
/***************************显示提示信息，当左侧文字较长的时候******/
function redTip(o){	
	switch(o.display){
		case "show":
			var txt = o.text;
			$("#menu_tips dd").text(txt);
			var tip_top = o.top + 4;
			$("#menu_tips").css({top:tip_top, display:"block", left: o.pLeft || "190px"}).stop().animate({left:o.pLeft+10 || "200px", opacity:0.9})
			break;
		case "hide":
			if($("#menu_tips").is(":visible")){
				$("#menu_tips").css({display:"none", left:"190px"});
			}
			break;
	};
}