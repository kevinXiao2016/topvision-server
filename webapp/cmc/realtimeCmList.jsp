<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    library jquery
    module CMC
    import cmc/js/realtimeCmList
    import cmc/js/cmIndexPartition
    plugin LovCombo
</Zeta:Loader>
<title>Cm List</title>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script src="../js/ext/ux/LockingGridView.js"></script>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<script type="text/javascript">
var cmcId = '${cmcId}';
var cmStatus = '${cmStatus}';
var cmIndexs = '${cmIndexs}';
var cmPingMode = '${cmPingMode}';
var isSupportRealtimeCpeQuery = '${isSupportRealtimeCpeQuery}';
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
</script>
<style type="text/css">
.red{color: #FF0000}
.yellow{color: #FF9966}
.green{color: #009933}
.showBubbleTip{ width:100%;}
.subDashedLine{ border-bottom:1px dashed #ccc; padding-bottom:2px; padding-top:2px;} 
.formLine{ border-bottom:1px solid #ccc; border-right:1px solid #ccc;border-left:1px solid #ccc;padding-bottom:2px; 
          border-top:1px solid #ccc;padding-top:2px;}
.formLine td{ height:16px;} 
.formLine th{ height:16px;} 
.borderNone{ border:none;}
.rightTopLink{ position:absolute; top:0px; right:55px;}
.tt{font-size : 2em;color : #FF0000;}
.w100{width:100px;}
#partition{width:780px;}
#cmFaultInfo{
position:relative;
top:4px;
left:12px;
}
.st{padding-bottom:6px;padding-left:16px;}

</style>
</head>
<body class="whiteToBlack">
	<div id="topPart">
		<div id="query-container">
			<div id="advance-toolbar-div" >
				<table class="queryTable">
				<tr>
				    <td>
				        <img id='cmFaultInfo' src="/images/performance/Help.png" />
				        <span class="rightBlueTxt st" cellpadding="10">@CCMTS.entityStatus@@COMMON.maohao@</span>
				    </td>
					<td><div id="select_cmMacStatus"></div></td>				
				</tr>
					
				<tr>
					<td class="rightBlueTxt">@CM.partition@@COMMON.maohao@</td>
	    			<td colspan="15"><div id="partition"></div></td>
	    			<td>
						<ul class="myquery">
							<li>
								<a href="javascript:;" class="normalBtn"style="margin-right: 5px;" onclick="onSearchClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
							</li>
						</ul>
					</td>
	    		</tr>
				</table>
			</div>
		</div>
	</div>	
	
	<div class='faultTips' id="faultTips" style="display:none;">
	<table style="text-align:left;table-layout:fixed;">
	
	<tr class="formLine">
	  <th width="200" height="10" style="text-align:center;" class="formLine">@CM.faultType@</th>
	  <th width="400" height="10" style="text-align:center;" class="formLine">@CM.faultReason@</th>
	  <th width="500" height="10" style="text-align:center;" class="formLine">@CM.faultSolve@</th>
	</tr>
	
	<tr >
	  <td class="formLine">@CM.TYPE1@</td>
	  <td class="formLine">
	    <table>
	      <tr><td>*@CM.reason11@</td></tr>
	      <tr><td>*@CM.reason12@</td></tr>
	      <tr><td>*@CM.reason13@</td></tr>
	      <tr><td>*@CM.reason14@</td></tr>
	    </table>
	  </td>
	  <td class="formLine">
	  	<table>
	      <tr><td>*@CM.solve11@</td></tr>
	      <tr><td>*@CM.solve12@</td></tr>
	      <tr><td>*@CM.solve13@</td></tr>
	      <tr><td>*@CM.solve14@</td></tr>
	    </table>
	  </td>
	</tr>
		
	<tr >
	  <td class="formLine">@CM.TYPE2@</td>
	  <td class="formLine">
	    <table>
	      <tr><td>*@CM.reason21@</td></tr>
	      <tr><td>*@CM.reason22@</td></tr>
	      <tr><td>*@CM.reason23@</td></tr>
	    </table>
	  </td>
	  <td class="formLine">
	  	<table>
	      <tr><td>*@CM.solve21@</td></tr>
	      <tr><td>*@CM.solve22@</td></tr>
	      <tr><td>*@CM.solve23@</td></tr>
	    </table>
	  </td>
	</tr>
	
	<tr >
	  <td class="formLine">@CM.TYPE3@</td>
	  <td class="formLine">
	    <table>
	      <tr><td>*@CM.reason31@</td></tr>
	      <tr><td>*@CM.reason32@</td></tr>
	      <tr><td>*@CM.reason34@</td></tr>
	      <tr><td>*@CM.reason35@</td></tr>
	      <tr><td>*@CM.reason36@</td></tr>
	      <tr><td>*@CM.reason37@</td></tr>
	    </table>
	  </td>
	  <td class="formLine">
	  	<table>
	      <tr><td>*@CM.solve31@</td></tr>
	      <tr><td>*@CM.solve32@</td></tr>
	      <tr><td>*@CM.solve34@</td></tr>
	      <tr><td>*@CM.solve35@</td></tr>
	      <tr><td>*@CM.solve36@</td></tr>
	      <tr><td>*@CM.solve37@</td></tr>
	    </table>
	  </td>
	</tr>
	
	<tr >
	  <td class="formLine">@CM.TYPE4@</td>
	  <td class="formLine">
	    <table>
	      <tr><td>*@CM.reason41@</td></tr>
	      <tr><td>*@CM.reason42@</td></tr>
	    </table>
	  </td>
	  <td class="formLine">
	  	<table>
	      <tr><td>*@CM.solve41@</td></tr>
	      <tr><td>*@CM.solve42@</td></tr>
	    </table>
	  </td>
	</tr>
	
	<tr>
	  <td class="formLine">@CM.TYPE5@</td>
	  <td class="formLine">
	    <table>
	      <tr><td>*@CM.reason51@</td></tr>
	      <tr><td>*@CM.reason52@</td></tr>
	      <tr><td>*@CM.reason53@</td></tr>
	    </table>
	  </td>
	  <td class="formLine">
	  	<table>
	      <tr><td>*@CM.solve51@</td></tr>
	      <tr><td>*@CM.solve52@</td></tr>
	      <tr><td>*@CM.solve53@</td></tr>
	    </table>
	  </td>
	</tr>		
	</table>
	</div>	
	
	<script type="text/javascript">	

	 function createBubble(o){
		var str = '',
		    firstStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.firstHtml),
		    secondStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.secondStr),
		    thirdStr = String.format('<div class="subDashedLine wordBreak">{0}</div>', o.thirdStr),
		    fourthStr = String.format('<div class="subDashedLine wordBreak borderNone">{0}</div>', o.fourthStr);
		    		    
		str += '<div class="bubbleTip" id="bubbleTip">';
		str += '	<div class="bubbleTipArr"></div>';
		str += '	<div class="bubbleBody">';
		str += '		<p class="pT5"><b class="gray555">' + I18N.CM.upSnr + '(dB)</b></p>';
		str += 			firstStr;
		str += '		<p class="pT5"><b class="gray555">'+ I18N.CM.downSnr +'(dB)</b></p>';
		str += 			secondStr;
		str += '		<p class="pT5"><b class="gray555">' + I18N.CM.upSendPower + '(@{unitConfigConstant.elecLevelUnit}@)</b></p>';
		str += 			thirdStr;
		str += '		<p class="pT5"><b class="gray555">' + I18N.CM.downReceivePower + '(@{unitConfigConstant.elecLevelUnit}@)</b></p>';
		str += 			fourthStr;
		str += '	</div>';
		str += '</div>';
		return str;
	}
	 		
	$("label.showBubbleTip").live("mouseenter",function(e){
		var $me = $(this),
		    $tr = $me.parent().parent().parent(),
		    $bubbleTip = $("#bubbleTip"),
		    xpos =  $me.offset().left- 376,
			ypos = $me.offset().top,
		    o = {};
		var t1=$tr.find(".first")
		o.firstHtml = $tr.find(".first").html();
		var t2=$tr.find(".second")
		o.secondStr = $tr.find(".second").html();
		var t3=$tr.find(".third");
		o.thirdStr = $tr.find(".third").html();
		var t4=$tr.find(".fourth");
		o.fourthStr = $tr.find(".fourth").html();
		
		var str = createBubble(o);
		if($bubbleTip.length >= 1){
			$bubbleTip.remove();	  
		}
		$("body").append(str);
		$bubbleTip = $("#bubbleTip");//需要重新获取一次;
		$bubbleTip.css({left : xpos, top : ypos});
		
		var h = $(window).height(),
		    h2 = h - ypos;
		var outH=$bubbleTip.outerHeight();
		if( h2 > $bubbleTip.outerHeight() ){
			$bubbleTip.find(".bubbleTipArr").css("top",0);	
		}else{
			$bubbleTip.find(".bubbleTipArr").css("bottom",0).addClass("bubbleTipArr2");
			$bubbleTip.css({
				top : ypos - $bubbleTip.outerHeight() + 10
			});
		}		
		if( $(this).hasClass("first") ){
			$bubbleTip.find("b").eq(0).attr("class","orangeTxt");
		}else if( $(this).hasClass("second") ){
			$bubbleTip.find("b").eq(1).attr("class","orangeTxt");
		}else if( $(this).hasClass("third") ){
			$bubbleTip.find("b").eq(2).attr("class","orangeTxt");
		}else if( $(this).hasClass("fourth") ){
			$bubbleTip.find("b").eq(3).attr("class","orangeTxt");
		}
	}).live("mouseleave",function(){
		var $bubbleTip = $("#bubbleTip");
		$bubbleTip.remove();
	});
	</script>
</body>
</Zeta:HTML>