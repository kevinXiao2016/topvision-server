<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library Ext
    library Zeta
    module  performance
    import performance/js/jquery-ui-1.10.3.custom.min
    css js/tools/css/numberInput
    css performance/css/select2
    import js.tools.numberInput
    import performance.js.select2
    import performance.js.addPerfTargetsToTemplate
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
.perfVlue{
	width: 60px;
}
.vertical-middle {
	vertical-align: middle;
}

.vertical-middle label, .vertical-middle span,.vertical-middle input,.vertical-middle img,.vertical-middle div {
	vertical-align: middle;
}
.timeInput{
	width: 46px;
}
.select2-container .select2-choice{
	height: 20px;
	line-height: 20px;
}
.select2-container .select2-choice > .select2-chosen{
	color: #00156E;
}
.select2-container .select2-choice .select2-arrow b{
	background-position: 0 -2px;
}
.unitSpan{
	margin-right: 10px;
	line-height: 2em;
	min-width : 20px;
	min-height : 10px;
}

<style type="text/css">
#mainFloatDiv{/* height:270px;*/ overflow:hidden; background:#F1F1F1; padding-bottom:5px; border-bottom:1px solid #D6D6D6;}
.leftPartFloat{ width:83px; overflow:hidden; float:left; text-align:right; padding-right:6px;}
.rightPartFloat{ width:690px; float:left; overflow:auto; max-height:116px;}
.floatDiv{ padding-top:10px;}
.upArrow{ width:11px; height:10px; overflow:hidden; border:1px solid #b7b8ca; cursor:pointer; background:#fff url(../../images/upDownArrow.gif) no-repeat; display:block;}
.upArrow:hover{ background:#fff url(../../images/upDownArrow.gif) no-repeat 0px -10px;}
.downArrow{ width:11px; height:10px; overflow:hidden; border:1px solid #b7b8ca; cursor:pointer; position:relative; top:-1px; background:#fff url(../../images/upDownArrow.gif) no-repeat 0px -20px; display:block;}
.downArrow:hover{ background:#fff url(../../images/upDownArrow.gif) no-repeat 0px -30px;}
.blankBg{ background:#fff; cursor:default; border:1px dotted #b7b8ca;}
.blankBg:hover{ background:#fff;}
#arrowTip{ width:135px; height:190px; overflow:hidden; overflow:hidden; position:absolute; top:112px; left:409px; }
.tipPicture{ position:absolute; top:60px; left:0px; z-index:1;}
.iknow{ position:absolute; top:145px; left:0px; width:100px; height:42px; overflow:hidden; z-index:2; display:block;display:block; background:url(../../images/redArrow.png) no-repeat 500px 500px;}
.redArrow{ width:73px; height:28px; overflow:hidden; background:url(../../images/redArrow.png) no-repeat; position:absolute; top:30px; left:40px;
	-webkit-animation: shake 1s infinite linear;
	   -moz-animation: shake 1s infinite linear;
	     -o-animation: shake 1s infinite linear;
		    animation: shake 1s infinite linear;
}
@-webkit-keyframes shake {
	0%,49% { -webkit-transform:  rotate(-14deg);}
	50%,90% {  -webkit-transform:  rotate(0deg);}
	91,100% { -webkit-transform:rotate(-14deg);}
}
@-o-keyframes shake {
	0%,49% { -o-transform:  rotate(-14deg);}
	50%,90% {  -o-transform:  rotate(0deg);}
	91,100% { -o-transform:rotate(-14deg);}
}
@-moz-keyframes shake {
	0%,49% { -moz-transform:  rotate(-14deg);}
	50%,90% {  -moz-transform:  rotate(0deg);}
	91,100% { -moz-transform:rotate(-14deg);}
}
@keyframes shake {
	0%,49% { transform:  rotate(-14deg);}
	50%,90% {  transform:  rotate(0deg);}
	91,100% { transform:rotate(-14deg);}
}
.current{
	-webkit-animation: toBig 0.4s 1 ease-in;
	   -moz-animation: toBig 0.4s 1 ease-in;
	     -o-animation: toBig 0.4s 1 ease-in;
		    animation: toBig 0.4s 1 ease-in;
}

@-webkit-keyframes toBig {
	0% { -webkit-transform:scale(0,0);}
	92% { -webkit-transform:scale(1.2,1.2);}
	100% { -webkit-transform:scale(1,1);}
}
@-moz-keyframes toBig {
	0% { -moz-transform:scale(0,0);}
	92% { -moz-transform:scale(1.2,1.2);}
	100% { -moz-transform:scale(1,1);}
}
@-o-keyframes toBig {
	0% { -o-transform:scale(0,0);}
	92% { -o-transform:scale(1.2,1.2);}
	100% { -o-transform:scale(1,1);}
}
@keyframes toBig {
	0% { transform:scale(0,0);}
	92% { transform:scale(1.2,1.2);}
	100% { transform:scale(1,1);}
}
.vertical-middle{vertical-align: middle;}
.vertical-middle label, .vertical-middle span, .vertical-middle input, .vertical-middle img, .vertical-middle div {vertical-align: middle;}
.timeInput {width: 46px;}
.zebraTableRows td table td{ border-bottom:none; padding:0px;}
</style>



<script type="text/javascript">
var perfTargets = ${perfTargets};
var perfThresholdTargets = ${perfThresholdTargets};
var unit = "";
var originalFrame = '${originalFrame}';
//温度单位
var tempUnit = '@{unitConfigConstant.tempUnit}@';
//校验
var maxValue;
var minValue;
var regValue;
var regRule;
</script>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edge10 pT5">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			 <tbody>	
				 <tr>	
					 <td class="rightBlueTxt" width="80" id="firstLabel" >	
						@Performance.targetName@:
					 </td>	
					 <td >	
					 	<ul class="leftFloatUl" id="putSelectedUl0">
							<select id="perfTargetSelect" style="width:260px;"></select>
						</ul>
					</td>	
				 </tr>
			 </tbody>
		 </table>
		 <div class="floatDiv" id="mainFloatDiv">
		 	<div class="leftPartFloat blueTxt">
				<img src="/images/performance/Help.png" alt="" id="tipsImg" title="@tip.thdRule@" style="vertical-align: middle;" />
				<span>@Performance.threshold@:</span>
		 	</div>
			<div class="rightPartFloat" id="putSubInput">
			</div>
		 </div>
		 <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			 <tbody>
			 	<tr class="darkZebraTr">
			 		<td class="rightBlueTxt" width="80" style="border-top:1px solid #d6d6d6;">@tip.trigger@:</td>
					<td style="border-top:1px solid #d6d6d6;">
						<div class='triggerRule'>
							<input type="text" id="minuteLength" class="timeInput normalInput" value ='1' maxlength="4" toolTip="1-1440(@tip.Integer@)"/>
							<span>@tip.tgrRdr1@</span>
							<input id="count" type="text" class="timeInput normalInput" value ='1' maxlength="3" toolTip="1-100(@tip.Integer@)"/>
							<span>@tip.tgrRdr2@</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@tip.timePolice@:</td>
					<td class="vertical-middle">
						<input type="checkbox" id="useTimePeriod" checked="checked"/>
						<span class="spanWidth30 mR10">@label.started@</span>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td></td>
					<td>
						<table>
							<tr>
								<td><div id="startHourDiv" style="display: inline;" class="timePeriod"></div></td>
								<td><span class="timePeriod">@tip.hour@</span></td>
								<td><div id="startMinDiv" style="display: inline-table;" class="timePeriod"></div></td>
								<td><span class="spanWidth30 timePeriod mR10">@tip.min@</span></td>
								<td><span class="spanWidth30 timePeriod mR10">@tip.to@</span></td>
								<td><div id="endHourDiv" style="display: inline;" class="timePeriod"></div></td>
								<td><span class="timePeriod">@tip.hour@</span></td>
								<td><div id="endMinDiv" style="display: inline;" class="timePeriod"></div></td>
								<td><span class="spanWidth30 timePeriod">@tip.min@</span></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td></td>
					<td class="vertical-middle">
			    		<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="1"/><span class="mR10 timePeriod">@tip.sun@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="2"/><span class="mR10 timePeriod">@tip.mon@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="3"/><span class="mR10 timePeriod">@tip.thu@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="4"/><span class="mR10 timePeriod">@tip.wed@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="5"/><span class="mR10 timePeriod">@tip.thr@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="6"/><span class="mR10 timePeriod">@tip.fri@</span>
						<input type="checkbox" class="timePeriod weekCbx" checked="checked" value="7"/><span class="mR10 timePeriod">@tip.sat@</span>
					</td>
				</tr>
			 </tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
			 <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="addPerfTarget()"><span><i class="miniIcoAdd"></i>@tip.add@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="closeClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
		<div id="arrowTip" style="display:none;">
			<div class="redArrow"></div>
			<div class="tipPicture"><img src="../../images/@COMMON.redArrowTip@.png" alt="" /></div>
			<a href="javascript:;" class="iknow" alt="no" onclick="hideIknow()"></a>
		</div>
	</div>
	
	<script type="text/javascript">
		var page = {
			lineNum : 10,      //最多添加10条阈值;
			toolTip : ''
		};
		
		var json = {
			   unit   : '',             //单位;
			   config   : [             //配置
					{
					   action : 1,       //选择5个中的一个,1大于，2大于等于，3等于，4小于等于，5小于;
					   value : '',      //后面input的值;
					   level : 5        //告警级别;
					}
			   ],
			   clearRules   : [         //配置
			        {
			        	action : 1,    //选择5个中的一个,1大于，2大于等于，3等于，4小于等于，5小于;
			        	value : ''     //后面input的值;
			        }
			   ]
		};
		
		var $firstLabel = $("#firstLabel"),
    	    $putSelectedUl = $("#putSelectedUl"); //放置下拉选择框的selected;
			
		function jsonToPage(json){
			var jsonInner = json,
			     aSelect = json.aSelect;
			
			showSubInput(jsonInner);
		
			var btnStr = addBtns();
			$("#putSubInput").after(btnStr);
			correctToolTip(); //更新一下toolTip和验证;
			updateRedArrowLeft();
			checkBtnDisabled();
		};
		function showSubInput(json){
			var sel = $putSelectedUl.find("select:last option:selected").val();
			    config = json.config,
			    clearRules = json.clearRules,
				len = config.length,
				$putSubInput = $("#putSubInput"),
				str = '';
			for(var i=0; i<len; i++){
					 str += '<ul class="leftFloatUl clearBoth pB5 oneLine">';
					 var rangeStr = renderRange(parseInt(config[i].action,10));
					 var clearRulesStr = renderRange(parseInt(clearRules[i].action,10));
					 str +=     '<li>'+ rangeStr +'</li>';
					 str +=     '<li><input type="text" class="normalInput w40" value="'+ config[i].value +'" maxlength="10"/></li>';
					 str +=     '<li class="unitSpan">'+ json.unit +'</li>';
					 str +=     '<li>'; 
					 var level = renderLevelStr(parseInt(config[i].level,10));
					 str += 		level
					 str +=     '</li>';
					 str +=     '<li class="pT3 pL10 blueTxt">@Performance.clearRule@</li>';
					 str +=     '<li>'+ clearRulesStr +'</li>';
					 str +=     '<li><input type="text" class="normalInput w40" value="'+ clearRules[i].value +'" maxlength="10"/></li>';
					 str +=     '<li class="unitSpan">'+ json.unit +'</li>';
					 str +=     '<li class="lastLi">';
					 if(len != 1){ //只有一个，就不需要移动位置;
						var arrStr = '';
						if(i == 0){ //第一个
							arrStr = addArrow('first');
						}else if( i == len-1){//最后一个;
							arrStr = addArrow('last');
						}else{//中间的，可以上下移动;
							arrStr = addArrow('middle');
						}
						str += arrStr;
					 }
					 str +=     '</li>';
					 str += '</ul>';
			}
			$putSubInput.append(str);
			if(len > 1){ //显示红色箭头;
				$("#arrowTip").css({"display":"block"})
				$("#arrowTip").addClass("current");
			}
		};

		//生成告警级别select;
		function renderLevelStr(num){
			var aLevel = [{
				label : '@Performance.alert6@',
				value : 6
			},{
				label : '@Performance.alert5@',
				value : 5
			},{
				label : '@Performance.alert4@',
				value : 4
			},{
				label : '@Performance.alert3@',
				value : 3
			},{
				label : '@Performance.alert2@',
				value : 2
			}];
			var str = '<select class="normalSel">';
				for(var i=0; i<aLevel.length; i++){
					var nowLevel = aLevel[i];
					if(num === nowLevel.value){
						str += '<option value="'+ nowLevel.value +'" selected="selected">'+ nowLevel.label +'</option>';
					}else{
						str += '<option value="'+ nowLevel.value +'">'+ nowLevel.label +'</option>';
					}
				}
			    str += '</select>'
			return str;
		};
		
		//生成范围select;
		function renderRange(num){
			var aRange = [{
					label : '@Performance.than@',
					value : 1
				},{
					label : '@Performance.thanOrEqual@',
					value : 2
				},{
					label : '@Performance.equal@',
					value : 3
				},{
					label : '@Performance.lessOrEqual@',
					value : 4
				},{
					label : '@Performance.less@',
					value : 5
				}],
				rangeStr = '<select class="normalSel" style="width:130px;">';
				
			for(var i=0,len=aRange.length; i<len; i++){
				var r = aRange[i];
				if(num === r.value){
					rangeStr += '<option value="'+ r.value +'" selected="selected">'+ r.label +'</option>';
				}else{
					rangeStr += '<option value="'+ r.value +'">'+ r.label +'</option>';
				}
			}
			rangeStr += '</select>'
			return rangeStr;
		};
		function correctToolTip(){
			$("#putSubInput ul").each(function(){
				$(this).find(":text").attr("toolTip",page.toolTip);
			})
		};
		function addArrow(para){
			var str = '';
			switch(para){
				case 'first':
					str = '<a htef="javascript:;" class="upArrow blankBg"></a><a htef="javascript:;" class="downArrow nm3kTip" nm3kTip="@Performance.down@"></a>';
				break;
				case 'middle':
					str = '<a htef="javascript:;" class="upArrow nm3kTip" nm3kTip="@Performance.up@"></a><a htef="javascript:;" class="downArrow nm3kTip" nm3kTip="@Performance.down@"></a>';
				break;
				case 'last':
					str = '<a htef="javascript:;" class="upArrow nm3kTip" nm3kTip="@Performance.up@"></a><a htef="javascript:;" class="downArrow blankBg"></a>';
				break;
			}
			return str;
		};
		function addBtns(){
			var str = '<ul class="leftFloatUl clearBoth pT5 pB10">';
				str +=        '<li style="margin-left:110px;"><a id="addBtn" href="javascript:;" class="normalBtn" onclick="addOneLine()"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>';
				str +=        '<li><a id="removeBtn" href="javascript:;" class="normalBtn" onclick="removeOneLine()"><span><i class="miniIcoReduce"></i>@COMMON.delete@</span></a></li>';
			    str += '</ul>';
			return str;
		}
		//增加一行;
		function addOneLine(){
			var $putSubInput = $("#putSubInput"),
			    $oneLine = $putSubInput.find(".oneLine"),
				$ikonw = $("#arrowTip").find(".iknow"),
				config = json.config,
			    clearRules = json.clearRules,
				jsonUnit = '',
				str = '';
				
			if($oneLine.length < page.lineNum){
				str += '<ul class="leftFloatUl clearBoth pB5 oneLine">';
				var rangeStr = renderRange(parseInt(config[0].action,10));
				var clearRulesStr = renderRange(parseInt(clearRules[0].action,10));
				str +=     '<li>'+ rangeStr +'</li>';
				str +=     '<li><input type="text" class="normalInput w40" value="" maxlength="10"/></li>';	
				jsonUnit = json.unit;
				str +=     '<li class="unitSpan">'+ jsonUnit +'</li>';
				var level = renderLevelStr(5);
				str +=     '<li>'+ level +'</li>'; 
				
				str +=     '<li class="pT3 pL10 blueTxt">@Performance.clearRule@</li>';
				str +=     '<li>'+ clearRulesStr +'</li>';
				str +=     '<li><input type="text" class="normalInput w40" value="'+ clearRules[0].value +'" maxlength="10"/></li>';
				str +=     '<li class="unitSpan">'+ json.unit +'</li>'; 
				
				var arrowStr = addArrow('last');
				str +=     '<li class="lastLi">'+ arrowStr +'</li>';
				str += '</ul>';
				$oneLine.last().after(str);
				upDateArr();
				correctToolTip(); //更新一下toolTip和验证;
			}else{
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@Performance.atMostItems@'+ page.lineNum +'@Performance.item@</b>'
				})
			}
			if($oneLine.length > 0 && $ikonw.attr("alt") === "no"){ //alt是no,说明没有关闭过;
				$("#arrowTip").css({display:'block'});
				$("#arrowTip").addClass("current");
			}
			//$(".rightPartFloat").scrollTop(1000);
			$(".rightPartFloat").stop(true,true).animate({scrollTop:1000});
			checkBtnDisabled();
		};
		//删除一行;
		function removeOneLine(){
			var $putSubInput = $("#putSubInput"),
			    $oneLine = $putSubInput.find(".oneLine"),
				len = $oneLine.length;
				$ikonw = $("#arrowTip").find(".iknow");
			if( len >= 2 ){
				$oneLine.last().remove();
				upDateArr();
			}else{
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@Performance.atLestOne@</b>'
				});
			}
			if( len == 2){
				$("#arrowTip").css({display:'none'});
				$("#arrowTip").removeAttr("class");
			}
			$(".rightPartFloat").scrollTop(1000);
			checkBtnDisabled();
		}
		function hideIknow(){
			var $arrowTip = $("#arrowTip"),
			     $link = $arrowTip.find(".iknow");
			$link.attr("alt", "yes");
			$arrowTip.css({"display":"none"});
		}
		function upDateArr(){//更新向上移动，向下移动的箭头;
			var $putSubInput = $("#putSubInput"),
			     $oneLine = $putSubInput.find(".oneLine"),
				 len = $oneLine.length;
			$oneLine.each(function(i){
				var $lastLi = $(this).find("li:lastLi"),
				     str = '';
				
				$lastLi.empty();//先移除掉;
				if(len != 1){ //如果只有一个，则不调整;
					if(i === 0){//第一个;
						str = addArrow("first");
					}else if(i === (len-1) ){//最后一个;
						str = addArrow("last");
					}else{//中间;
						str = addArrow("middle");
					}
					$lastLi.html(str);
				}
			});
		}
		function updateRedArrowLeft(){//更新红色提示箭头的left，因为存在中英文差异;
			var $last = $("#putSubInput .oneLine:eq(0)").find(".lastLi").find(".downArrow"),
			     len = $last.length,
				 l = 409;

			if(len == 0){ //最后是select
				var $sel = $("#putSubInput .oneLine:eq(0)").find(".lastLi");
				l = $sel.offset().left + $sel.outerWidth() + 18;
			}else{//最后是上移、下移小箭头;
				l = $last.offset().left + 18;
			}
			$("#arrowTip").css({left: l-40});
		}
		function checkBtnDisabled(){
			var len = $("#putSubInput").find(".oneLine").length,
			     $addBtn = $("#addBtn"),
				 $removeBtn = $("#removeBtn");
				 
			if(len === 1){
				$removeBtn.attr({disabled: "disabled"});
			}else if( len === page.lineNum){
				$addBtn.attr({disabled: "disabled"});
			}else{
				$addBtn.removeAttr("disabled");
				$removeBtn.removeAttr("disabled");
			}
		}
		
		
		$(function(){
			//点击向上的箭头;
			$("#putSubInput").delegate("a.upArrow","click",function(){
				var $me = $(this),
				     $parentUl = $me.parent().parent();
					 
				if( !$me.hasClass("blankBg") ){
					var num =  $parentUl.index();
					$parentUl.insertBefore( $("#putSubInput ul").eq(num-1) );
					upDateArr();
				}
			});
			//点击向下的箭头;
			$("#putSubInput").delegate("a.downArrow","click",function(){
				var $me = $(this),
				     $parentUl = $me.parent().parent();
					 
				if( !$me.hasClass("blankBg") ){
					var num =  $parentUl.index();
					$parentUl.insertAfter( $("#putSubInput ul").eq(num+1) );
					upDateArr();
				}
			});
			jsonToPage(json);
		});//end document.ready; 
		
		
	</script>
	
</body>
</Zeta:HTML>