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
    MODULE cmc
</Zeta:Loader>
<style type="text/css">
.lookLikeBaidu{ width:440px; height:30px; padding-left:10px; float:left;}
.putSearch{ width:680px; margin:0 auto; margin-top:50px;}
#result{ margin-top:15px;}
#result td{ padding:5px 2px;}
.searchBtn{ padding:9px 20px 8px; border:1px solid #B7B8CA; border-left:none; cursor:pointer; color:#676767;  text-shadow: 0px 1px 0px #fff;
    background: -moz-linear-gradient( top,#fff,#f5f5f5);
    background: -webkit-linear-gradient(top,#fff,#f5f5f5);
    background: -ms-linear-gradient(top,#fff,#f5f5f5);
    background: -o-linear-gradient(top,#fff,#f5f5f5);
    background: linear-gradient(top,#fff,#f5f5f5);
    filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0, startColorstr=#ffffff, endColorstr=#f5f5f5);
}
.searchBtn:hover{ 
    background: -moz-linear-gradient( top,#f5f5f5,#fff);
    background: -webkit-linear-gradient(top,#f5f5f5,#fff);
    background: -ms-linear-gradient(top,#f5f5f5,#fff);
    background: -o-linear-gradient(top,#f5f5f5,#fff);
    background: linear-gradient(top,#f5f5f5,#fff);
    filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0, startColorstr=#f5f5f5, endColorstr=#ffffff);
}
.searchBtn i{ width:18px; height:16px; overflow:hidden; display:block; float:left;}
#dropDownArrow span{ color:#B3711A;}
#drowDownDiv{ width:90px; position:absolute; background:#fff; border:1px solid #ccc; border-top:none;
 	-webkit-box-shadow:0 2px 2px #ccc;
 	   -moz-box-shadow:0 2px 2px #ccc;
 	    -ms-box-shadow:0 2px 2px #ccc;
 	     -o-box-shadow:0 2px 2px #ccc;
 	        box-shadow:0 2px 2px #ccc;
}
#drowDownDiv li a{ padding:5px 0px; display:block; text-indent: 10px;}
#drowDownDiv li a:hover,#drowDownDiv li a.sel{ background:#dfe8f6;}
</style>
<script type="text/javascript">
$(function(){
	$("#dropDownArrow").click(function(){
		var $drowDownDiv = $("#drowDownDiv"),
		    $dropDownArrow = $("#dropDownArrow"),
		    l = $dropDownArrow.offset().left,
		    t = $dropDownArrow.offset().top + $dropDownArrow.outerHeight() - 1;
		
		if( $drowDownDiv.length === 0 ){
			createComboBox();
		}else{
			if( $drowDownDiv.is(":visible") ){
				$drowDownDiv.hide();
			}else{
				$drowDownDiv.show();
			}
		}
		$("#drowDownDiv").css({left:l, top:t});
	});	
});


function locate(){
	var para = $("#dropDownArrow span").text(),
	    $mac = $("#macAddress").val(),
	    tpl;
	
	$("#putResult").empty();
	if( !V.isMac($mac) ){
		$("#macAddress").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@Locate.locating@");
	if(para === 'CM'){ //定位cm
		$.ajax({
			url:'/cmCpe/queryCmLocate.tv',dataType:'json',
			data:{cmMac : $mac},
			success:function(data){
				window.top.closeWaitingDlg();
				if(data == null){
					//return window.top.showMessageDlg("@COMMON.tip@", "@Locate.cmNotFound@");
					$("#putResult").html("<p class='pT20' style='font-size:14px'><b class='orangeTxt'>@Locate.cmNotFound@</b></p>");
					return;
				}
				WIN.THIS_CENTRAL_ID = data.entityId;
				WIN.THIS_CENTRAL_GOVNAME = data.govName;
				WIN.THIS_CMC_ID = data.cmcId;
				WIN.THIS_CMC_NAME = data.ccName;
				showCmResult(data);
			},error:function(){
				window.top.showMessageDlg("@COMMON.error@", "@Locate.locateEr@");
			}
		});
	}else if(para === 'ONU CPE'){//定位CPE;
		 var data = {
			oltName : '150',
			oltIp : '172.17.2.150',
			slotLocation : 3,
			portLocation : 4,
			onuLocation :  1, //slot/pon:onu
			uniLocation : 1,
			entityId : null,
			onuId: null,
			onuName:  null,
			macLocation : null
		};
		 alert(1)
			WIN.THIS_CENTRAL_ID = data.entityId;
			WIN.THIS_CENTRAL_GOVNAME = data.oltName;
			WIN.THIS_ONU_ID = data.onuId;
			WIN.THIS_ONU_NAME = data.onuName;
			showCpeResult(data);
			return;
		$.ajax({
			url     :'/onu/cpelocation/loadOnuCpeLoc.tv',
			data    : {cpeMac : $mac},dataType : 'json',
			success : function($data){
				window.top.closeWaitingDlg();
				if(data == null){
					$("#putResult").html("<p class='pT20' style='font-size:14px'><b class='orangeTxt'>@Locate.cmNotFound@</b></p>");
					return;
				}
				WIN.THIS_CENTRAL_ID = data.entityId;
				WIN.THIS_CENTRAL_GOVNAME = data.oltName;
				WIN.THIS_ONU_ID = data.onuId;
				WIN.THIS_ONU_NAME = data.onuName;
				showCpeResult(data);
			},
			error : function(){
				window.top.showMessageDlg("@COMMON.error@", "@Locate.locateEr@");
			} 
		});//end $.ajax;
	};//end if else;
}

function createComboBox(){
	var dropStr = ['<ul id="drowDownDiv">',
                   '<li><a href="javascript:;" class="sel">CM</a></li>',
                   '<li><a href="javascript:;">ONU CPE</a></li>',
               '</ul>'
               ].join('');
	$("body").append(dropStr);
	$("#drowDownDiv").delegate("a","click",function(){
		var text = $(this).text();
		$("#dropDownArrow").find('span').text(text);
		$("#drowDownDiv a").removeClass("sel");
		$(this).addClass("sel");
		$("#drowDownDiv").hide();
	});
	$(window).resize(function(){
		if( $("#drowDownDiv").is(":visible") ){
			$("#drowDownDiv").hide();
		}
	});
	$(document).click(function(e){ //点击了空白区域，如果下拉菜单是展开的，则必须收起来;
		if( !$(e.target).hasClass("jsShow") ){ 
			if( $("#drowDownDiv").is(":visible") ){
				$("#drowDownDiv").hide();
			}
		}
	})
}

function showCmResult(data){
	tpl = new Ext.XTemplate(
			'<table cellpadding="0" cellspacing="0" rules="none" border="0" id="result">',
				'<tbody>',
					'<tr>',
						'<td class="blueTxt">@Locate.centralDevice@:</td>',
						'<td colspan="3"><a id="central" href="javascript:;" class="yellowLink" onclick="showEntity(true);">{govName}</a></td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt" width="60">CMTS:</td>',
						'<td  width="220">',
							'<a id="CMTSandCCMTS" href="javascript:;" class="yellowLink" onclick="showEntity(false);">{ccName}</a>',
						'</td>',
						'<td class="blueTxt" width="60">@COMMON.folder@:</td>',
						'<td id="folderName">{folderName}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt">CM@COMMON.location@:</td>',
						'<td id="location">{location}</td>',
						'<td class="blueTxt">CM IP:</td>',
						'<td id="ip">{ip}</td>',
					'</tr>',
				'</tbody>',
			'</table>'
		);				
	tpl.overwrite('putResult', data);
}
function showCpeResult(data){
	tpl = new Ext.XTemplate(
			'<table cellpadding="0" cellspacing="0" rules="none" border="0" id="result">',
				'<tbody>',
					'<tr>',
						'<td class="blueTxt">@Locate.centralDevice@:</td>',
						'<td colspan="3"><a href="javascript:;" class="yellowLink" onclick="showEntity(true);">{oltIp} ({oltName})</a></td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt" width="60">@Locate.slot@@COMMON.maohao@</td>',
						'<td  width="220">{slotLocation}</td>',
						'<td class="blueTxt" width="60">@Locate.ponport@@COMMON.maohao@</td>',
						'<td>{portLocation}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt">ONU:</td>',
						'<td><a href="javascript:;" class="yellowLink" onclick="showOnu();">{slotLocation}/{portLocation}:{onuLocation}</a></td>',
						'<td class="blueTxt">@Locate.uniport@:</td>',
						'<td>{uniLocation}</td>',
					'</tr>',
				'</tbody>',
			'</table>'
		);
	tpl.overwrite('putResult', data);
}

function showEntity(showCentral) {
	if(showCentral){
		window.top.addView('entity-' + THIS_CENTRAL_ID,  THIS_CENTRAL_GOVNAME , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + THIS_CENTRAL_ID);
	}else{
		window.top.addView('entity-' + THIS_CMC_ID,  THIS_CMC_NAME , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + THIS_CMC_ID);
	}
}

function showOnu(){
	 window.parent.addView('entity-' + THIS_ONU_ID, THIS_ONU_NAME, 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + THIS_ONU_ID );
}

</script>
	</head>
	<body class="whiteToBlack">
		<div class="txtCenter mT100">
			<img src="../images/@Locate.logoImg@.png" />
		</div>
		<div class="putSearch">
		    <div id="dropDownArrow" class="dropDownArrow jsShow" onselectstart="return false"><span class="jsShow">CM</span><a href="javascript:;" class="smallArrow jsShow"></a></div>
			<input class="normalInput lookLikeBaidu" id="macAddress" placeholder="MAC:" toolTip="@Locate.macIpt@<br> XX:XX:XX:XX:XX:XX<br> XX-XX-XX-XX-XX-XX<br> XX XX XX XX XX XX<br> XXXX.XXXX.XXXX<br> XXXX-XXXX-XXXX<br> XXXXXXXXXXXX" />
			<button class="searchBtn" onclick="locate()"><i class="miniIcoSearch"></i>@Locate.locateBtn@</button>
			
			<div id="putResult">
				
			</div>
			
			<!-- <table cellpadding="0" cellspacing="0" rules="none" border="0" id="result">
				<tbody>
					<tr>
						<td class="blueTxt">@Locate.centralDevice@:</td>
						<td><a id="central" href="javascript:;" class="yellowLink" onclick="showEntity(true);"></a></td>
					</tr>
					<tr>
						<td class="blueTxt" width="60">CMTS:</td>
						<td  width="220">
							<a id="CMTSandCCMTS" href="javascript:;" class="yellowLink" onclick="showEntity(false);"></a>
						</td>
						<td class="blueTxt" width="60">@COMMON.folder@:</td>
						<td id="folderName"></td>
					</tr>
					<tr>
						<td class="blueTxt">CM@COMMON.location@:</td>
						<td id="location"></td>
						<td class="blueTxt">CM IP:</td>
						<td id="ip"></td>
					</tr>
				</tbody>
			</table> -->
						
		</div>
	</body>
</Zeta:HTML>