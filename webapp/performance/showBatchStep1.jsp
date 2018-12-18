<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	library Highchart
	plugin DateTimeField
    module performance
    IMPORT js/entityType
    IMPORT performance/nm3kBatchData
</Zeta:Loader>
<style type="text/css">
body,html{ height:100%; overflow:hidden;}
#putData{ overflow:auto;}
#step2{ width:800px; position:absolute; top:140px; left:100px; z-index:5; display:none;}
#fullScreenBg{ width:100%; height:100%; position:absolute; top:79px; left:0; background:#fff; opacity:0.9; filter:alpha(opacity=90); z-index:4; display:none;}
#step3{ display:none; width:100%; height:100%; position:absolute; top:79px; left:0; z-index:5; background:#fff;}
#backBtn{ position:absolute; top:20px; left:440px; display:none;}
#globalTip{ color:#666;/*  padding-top:5px; text-indent: 16px; */}
#showBtn img{ cursor:pointer;}
#topPart{ overflow:hidden; height:55px;}
.pTitle{ font:bold 24px Verdana, Arial, Helvetica, sans-serif; padding:60px 0px 6px; color: #222; border-bottom:2px solid #6593CF;}

</style>
</head>

<body>
	<div class="greenBarStepWrap" id="topPart">
		<div class="greenBarStep">
			<div class="greenBarStepBg"></div>
			<a href="javascript:;" class="greenBarCir greenBarCirIng" style="left:16px;"></a>
			<a href="javascript:;" class="greenBarCir" style="left:188px;"></a>
			<a href="javascript:;" class="greenBarCir" style="left:355px;"></a>
			<span class="greenBarTxt greenBarTxtSelected" style="left:8px;">@tip.firstStep@</span>
			<span class="greenBarTxt" style="left:180px">@tip.secondStep@</span>
			<span class="greenBarTxt" style="left:347px">@tip.thirdStep@</span>
		</div>
		<p class="greenBarTip">@tip.selectTarget@</p>
		<a id="backBtn" href="javascript:;" class="normalBtn" onclick=""><span><i class="miniIcoArrLeft"></i>@tip.backUp@</span></a>
	</div>
	<div class="pT20 pL20 pR20 batchBg" id="putData">
		<!-- <div class="batchTit batchTitClose">
			要组成的数据的格式就是注释的这一部分;
		</div>
		<dl class="batchBody">
			<dd><a href="#">cpu</a></dd>
			<dd><a href="#">cpu</a></dd>
			<dd><a href="#">cpu</a></dd>
			<dd><a href="#">cpu</a></dd>
			<dd><a href="#">cpu</a></dd>
		</dl> -->
	</div>
	
	<div id="step2">
		<div class="zebraTableCaption">
			<div class="zebraTableCaptionTitle"><span id="showTit"></span></div>
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		             <tr>
		                 <td class="rightBlueTxt" width="350" id="showName"></td>
		                 <td width="105"><input id="showInput" type="text" class="normalInput w50" /> @tip.seconds@</td>
		                 <td id="showBtn"></td>
		             </tr>
		             <tr class="darkZebraTr">
		             	<td colspan="3" align="center">
		             		<p id="globalTip"></p>
		             	</td>
		             </tr>
		             <tr>
		                <td colspan="3" class="withoutBorderBottom">
		                	<ol class="upChannelListOl pB10 pT20 noWidthCenter">
							    <li><a href="javascript:;" class="normalBtnBig" id="gotoStep3Btn"><span><i class="miniIcoArrRight"></i>@tip.nextStep@</span></a></li>
							</ol>
		                </td>
		             </tr>
		         </tbody>
		     </table>
		</div>
		
	</div>
	<div id="fullScreenBg"></div>
	
	<div id="step3">
		<iframe id="step3Frame" frameborder="0" width="100%" height="100%" scrolling="auto" src=""></iframe>
	</div>
	
	<script type="text/javascript">
		var oltType = EntityType.getOltType();
		var ccType = EntityType.getCcmtsType();
		var cmtsType = EntityType.getCmtsType();
		var onuType = EntityType.getOnuType();
		//模块支持情况,在单独安装CC模块的时候使用
		var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
		var eponSupport = <%= uc.hasSupportModule("olt")%>;
	
		var typeObj = {
			OLT : oltType,
			CCMTS : ccType,
			CMTS : cmtsType,
			ONU : onuType
		};
	
		var oltTartgetJson = ${oltTargetJson};
		var ccmtsTargetJson = ${ccmtsTargetJson};
		var onuTargetJson = ${onuTargetJson}; 
		
		function numberToTypeStr(str){
			var num;
			for(var key in typeObj){
				if(key == str){
					num = typeObj[key];
					break;
				}	
			}
			return num;
		};
		
		//在单独安装CC模块的时候,子类型下拉菜单默认只显示CMTS选择
		if(cmcSupport && !eponSupport){
			addDataToDom(ccmtsTargetJson,"CMTS");
		}else{
			addDataToDom(oltTartgetJson,"OLT");
			if(cmcSupport){
				addDataToDom(ccmtsTargetJson,"CMTS");
			}
			addDataToDom(onuTargetJson,"ONU");
		}
		//addDataToDom(cmtsTargetJson,"CMTS222");
			
		function addDataToDom(jsonData,typePara){
			var str = '<p class="clearBoth pTitle">'+ typePara +'</p>';
			if(typePara=="OLT"){
				str = '<p class="clearBoth pTitle" style="padding-top:0px;">'+ typePara +'</p>';
			}
			for(var key in jsonData){
				var node1 = jsonData[key];
				str += '<div class="batchTit batchTitClose">';
				str += 	nm3kBatchData[key].name;
				str += '</div>';
				str += '<dl class="batchBody">';
				for(var key2 in node1){
					var node2 = node1[key2];
					var open = node2.targetEnable;         //是否打开;
					var time = node2.collectInterval;       //间隔时间;
					var gOpen = node2.globalEnable;  //全局配置是否打开;
					var gTime = node2.globalInterval;//全局配置时间间隔;
					var parentType = node2.parentType;
					
					if( nm3kBatchData[key2].hasOwnProperty("tip") ){
						var tips = nm3kBatchData[key2].tip;
						str += '<dd><a href="javascript:;" parentType="'+ parentType +'" tip="'+ tips +'" name="'+ key2 +'" alt="'+ typePara +'" enable='+ open +' time='+ time +' gEnable='+ gOpen +' gTime='+ gTime +'>';
					}else{
						str += '<dd><a href="javascript:;" parentType="'+ parentType +'" name="'+ key2 +'" alt="'+ typePara +'" enable='+ open +' time='+ time +' gEnable='+ gOpen +' gTime='+ gTime +'>';	
					}
					str += nm3kBatchData[key2].name
					str += '</a></dd>';
				}
				str += '</dl>';
			}
			$("#putData").append(str);
		}	
			
		$("#gotoStep3Btn").click("click",function(){
			var $showInput = $("#showInput");
			var time = $showInput.val();
			var name = $showInput.attr("name");
			//验证;
			var testReg = (nm3kBatchData[name].reg)(Number(time));
			if(!testReg){
				if( $showInput.attr("disabled") == true || $showInput.attr("disabled") == "disabled"){
					if( !$showInput.is(":animated") ){
						$showInput.animate({opacity:0},"fast",function(){
							$(this).animate({opacity:1})
						})
					}
				}else{
					$showInput.focus();
				}
				return;
			}
			
			$("#step3").css({display : "block"});
			$("#backBtn").unbind("click",backToStep1).bind("click",backToStep2);
			setGreenBar(3);
			
			var open = $("#showBtn").find("img").attr("name");
			var type = numberToTypeStr($showInput.attr("alt"));
			var parentType = Number($showInput.attr("parentType"));
			//alert(你选择的类型是：+type + 时间是：+ time +  开启是： + open +  属性名称是： + name +  parnetType: + parentType)
			var queryStr = 'parentType='+parentType+'&entityType='+type+"&collectInterval="+time+"&targetEnable="+open+"&perfTargetName="+name;
			$("#step3Frame").attr("src","/performance/showSupportTargetDeviceList.tv?"+queryStr);		
			
		});
			
		function autoHeight(){
			var h = $(window).height(),
			w = $(window).width(),
			$step2 = $("#step2"),
			topH = $("#topPart").outerHeight(),
			bodyH = h - topH,
			bottomH = h - topH - 20;//因为class=pT20;
			if(bottomH < 10){bottom = 10;}
			if(bodyH < 10){ bodyH = 10;}
			$("#putData").height(bottomH);
			$("#step3, #step3Frame").height(bodyH);
			
			var l = ( w-$step2.outerWidth() )/2;
			if(l < 0){ l=0;}
			$step2.css({left: l});
		};//end autoHeight;
		autoHeight();
		
		$(window).resize(function(){
			throttle(autoHeight,document);
		});//end resize;
		
		//resize事件增加 函数节流;
		function throttle(method, context){
			clearTimeout(method.tId);
			method.tId = setTimeout(function(){
				method.call(context);
			},100);
		};
		
		//列表收缩;
		$(".batchTit").live("click",function(){
			var $me = $(this);
			if($me.hasClass("batchTitClose")){
				$me.removeClass("batchTitClose").addClass("batchTitOpen");
				$me.next().css({display:"none"})
			}else{
				$me.removeClass("batchTitOpen").addClass("batchTitClose");
				$me.next().css({display:"block"})
			}
		});//end live;
		//点击某个具体的标签;
		$("#putData .batchBody a").live("click",function(){
			$("#fullScreenBg, #step2").css({display:"block"});
			showStep2();
			var $me = $(this),
			tit = $me.text(),
			enable = $me.attr("enable"),
			time = $me.attr("time"),
			gEnable = $me.attr("gEnable"),
			gTime = $me.attr("gTime"),
			typeAlt = $me.attr("alt"),
			pro = $me.attr("name"),
			parentType = $me.attr("parentType"),
			$showInput = $("#showInput");
			
			var toolTip = nm3kBatchData[pro].toolTip;
			$("#showInput").attr({
				"alt": typeAlt,
				"name": pro,
				"toolTip" : toolTip,
				"parentType" : parentType
			});
			if(Number(gEnable) == 0){
				$("#showInput").attr({
					"disabled" : true	
				});
				if( !$showInput.hasClass("normalInputDisabled") ){
					$showInput.addClass("normalInputDisabled");
				}
			}else if(Number(gEnable) == 1){
				$("#showInput").attr({
					"disabled" : false
				});
				if( $showInput.hasClass("normalInputDisabled") ){
					$showInput.removeClass("normalInputDisabled");
				}
			}
			
			$("#showTit").text(tit);
			$("#showName").text(tit + " :");
			$("#showInput").val(gTime);
			var img = showOnOrOffImg(gEnable);
			$("#showBtn").html(img);
			
							
			var str = String.format("@tip.globalTip@", tit, gTime, showOnOrOffTxt(gEnable) );
			$("#globalTip").html(str);
			
			if($me.attr("tip")){
				$("#globalTip").append('<br /><span style="color:#666">' + $me.attr("tip") + '</span>');
			}
		});//end live;
		//点击开关图片;
		$("#showBtn img").live("click",function(){
			var $me = $(this),
			$showInput = $("#showInput"),
			isOpen = $me.attr("name");
			if(isOpen == "0"){//是关闭的;
				$me.attr({
					src : '/images/performance/on.png',
					name : "1"
				});
				$showInput.removeClass("normalInputDisabled").attr({
					disabled : false
				});
			}else{//是开启的;
				$me.attr({
					src : '/images/performance/off.png',
					name : "0"
				});
				$showInput.addClass("normalInputDisabled").attr({
					disabled : true
				});
			}
			
		})
		
		function showOnOrOffImg(para){
			var flag;
			if(para == "0"){
				flag = '<img src="/images/performance/off.png" name="0">';//用name存储开关;
			}else{
				flag = '<img src="/images/performance/on.png" name="1">';
			}
			return flag;
		};//end showOnOrOffImg;
		function showOnOrOffTxt(para){
			var flag;
			if(para == "0"){
				flag = "<b style='color:#CD2020;'>@Tip.off@</b>";
			}else{
				flag = "<b style='color:#0C9A08;'>@Tip.open@</b>";
			}
			return flag;
		};//end showOnOrOffImg;
		
		//显示第2步;
		function showStep2(){
			$("#backBtn").css({display:"block"}).bind("click",backToStep1);
			setGreenBar(2);
		}
		//回到第1步;
		function backToStep1(){
			$("#fullScreenBg, #step2, #backBtn").css({display:"none"});
			$("#backBtn").unbind("click",backToStep1);
			setGreenBar(1);
		}
		//回到第2步;
		function backToStep2(){
			showStep2();
			$("#step3").css({display:"none"});
			$("#backBtn").unbind("click",backToStep2);
		}
		
		var aGreenBarTip = ["@tip.step1Tip@","@tip.step2Tip@","@tip.step3Tip@","@tip.finishTip@"];
		function setGreenBar(num){
			$("#topPart .greenBarStep a").attr({"class": "greenBarCir"});
			$("#topPart .greenBarTxt").removeClass("greenBarTxtSelected");
			$("#topPart .greenBarTxt").eq(num-1).addClass("greenBarTxtSelected");
			$("#topPart .greenBarTip").text(aGreenBarTip[num-1]);
			switch(num){
				case 1:
					$("#topPart .greenBarStep a:eq(0)").addClass("greenBarCirIng");
					$("#topPart .greenBarStepBg").attr({"class" : "greenBarStepBg"});
					break;
				case 2:
					$("#topPart .greenBarStep a:eq(0)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(1)").addClass("greenBarCirIng");
					$("#topPart .greenBarStepBg").attr({"class" : "greenBarStepBg greenBarStepBg2"});
					break;
				case 3:
					$("#topPart .greenBarStep a:eq(0)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(1)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(2)").addClass("greenBarCirIng");
					$("#topPart .greenBarStepBg").attr({"class" : "greenBarStepBg greenBarStepBg3"});
					break;
				case 4:
					$("#topPart .greenBarStep a:eq(0)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(1)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(2)").addClass("greenBarCirOK");
					$("#topPart .greenBarStep a:eq(3)").addClass("greenBarCirIng");
					$("#topPart .greenBarStepBg").attr({"class" : "greenBarStepBg greenBarStepBg4"});
					break;
			}
		};//end function;
		function completeStep(){
			$("#backBtn").css({display:"none"})
			setGreenBar(4);
		}
	</script>
</body>
</Zeta:HTML>
