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
    MODULE network
    IMPORT js.utils.IpUtil
</Zeta:Loader>
<style type="text/css">
.lookLikeBaidu{ width:440px; height:30px; padding-left:10px; float:left;}
.putSearch{ width:680px; margin:0 auto; margin-top:50px;}
.queryTab{ width:680px; margin:0 auto; margin-top:30px;}
.queryTab a{ color:#00c; text-decoration: underline; font-size:14px; padding-left:20px;}
.queryTab a.sel{ text-decoration: none; font-weight:bold; color:#333;}
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
var terminalView = '${terminalView}';

var supportCmc = false;
<% if(uc.hasSupportModule("cmc")){%>
	supportCmc = true;
<% } %>
//license不支持CMTS，选项只会是ONU CPE;
if(!supportCmc){ terminalView = 'ONU CPE'};

var CPE_TYPE=[null,"host","mta","stb"];
var MAC_TYPE = [null,"@COMMON.static@","@COMMON.dynamic@"];
$(function(){
	$("#dropDownArrow").find('span').text(terminalView);
	showTooltip(terminalView);
	$("#dropDownArrow").click(function(){
		var $drowDownDiv = $("#drowDownDiv"),
		    $dropDownArrow = $("#dropDownArrow"),
		    l = $dropDownArrow.offset().left,
		    t = $dropDownArrow.offset().top + $dropDownArrow.outerHeight() - 1;
		
		if( $drowDownDiv.length === 0 ){
			createComboBox(terminalView);
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
	    $mac = $("#macAddress").val().trim(),
	    tpl;
	
	$("#putResult").empty();
	
	if(para === 'CM'){ //定位cm
		if( !V.isMac($mac) ){
			$("#macAddress").focus();
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@Locate.locating@");
		$.ajax({
			url:'/cmCpe/queryCmLocate.tv',
			dataType:'json',
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
				data.mac =$mac;
				showCmResult(data);
			},error:function(){
				window.top.showMessageDlg("@COMMON.error@", "@Locate.locateEr@");
			}
		});
	}else if(para === 'ONU CPE'){//定位CPE;
		if( !V.isMac($mac) ){
			if(!IpUtil.isIpAddress($mac)){
				$("#macAddress").focus();
				return;
			}
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@Locate.locating@");
		$.ajax({
			url     :'/onu/cpelocation/loadOnuCpeLoc.tv',
			data    : {cpeMac : $mac},
			dataType:'json',
			success : function(data){
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

function createComboBox(terminalSelect){
	var dropStr = ['<ul id="drowDownDiv">',
                   '<li><a href="javascript:;" class="{0}">CM</a></li>',
                   '<li><a href="javascript:;" class="{1}">ONU CPE</a></li>',
               '</ul>'
               ].join('');
	if(terminalSelect === "CM"){
		dropStr = String.format(dropStr,"sel","");
	}else{
		dropStr = String.format(dropStr,"","sel");
	}	
	$("body").append(dropStr);
	//lisence不支持CMTS,移除掉CM选项;
	if(!supportCmc){
		$('#drowDownDiv li:eq(0)').remove();	
	}
	$("#drowDownDiv").delegate("a","click",function(e){
		var text = $(this).text();
		showTooltip(text);
		if( !$(e.target).hasClass("sel") ){
			saveTerminalSelect(text);
		}
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

function showTooltip(text){
	/* if(text == 'CM'){
		$("#macAddress").attr("toolTip","@Locate.macIpt@");	
	}else{
		$("#macAddress").attr("toolTip","@Locate.macAndIpIpt@");
	} */
}
function showCmResult(data){
	tpl = new Ext.XTemplate(
			'<table cellpadding="0" cellspacing="0" rules="none" border="0" id="result">',
				'<tbody>',
					'<tr>',
						'<td width="60" class="blueTxt">MAC:</td>',
						'<td width="220">'+returnMac(data.mac)+'</td>',
						'<td width="60" class="blueTxt">@COMMON.folder@:</td>',
						'<td>{folderName}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt">@Locate.centralDevice@:</td>',
						'<td id="folderName"><a id="central" href="javascript:;" class="yellowLink" onclick="showEntity(true);">{govName}</a></td>',
						'<td class="blueTxt" width="60">CMTS:</td>',
						'<td>',
							'<a id="CMTSandCCMTS" href="javascript:;" class="yellowLink" onclick="showEntity(false);">{ccName}</a>',
						'</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt">CM@COMMON.location@:</td>',
						'<td id="location">{location}</td>',
						'<td class="blueTxt">CM IP:</td>',
						'<td id="ip">'+returnIP(data.ip)+'</td>',
					'</tr>',
				'</tbody>',
			'</table>'
		);				
	tpl.overwrite('putResult', data);
}

/* ==========================================================
 * 这段内容是页面grid的renderer方法
 * ========================================================== */

function returnIP(value) {
	if (value === null || value === "" || value === "noSuchObject" || value === "noSuchInstance" || value === "0.0.0.0" || value==='--') {
		return "--"
	} else {
		//cm单机Web的跳转
		return String.format('<a href="http://{0}" class="yellowLink" target="_blank">{0}</a>', value);
 	}
}

function returnMac(value) {
	if (value != "") {
    	return String.format('<a href="javascript:;" class="yellowLink" onclick="showCmDetail(\'{0}\')">{0}</a>', value);
  	} else {
    	return value;
  	}
}

function showCmDetail(macAddr) {
  	window.top.addView("CmCpeQuery", '@ccm/CCMTS.CmList.cmCpeQuery@', "mydesktopIcon", "/cmCpe/showCmCpeQuery.tv?sourcePage=cmListPage&cmMac=" + macAddr, null, true);
}


function showCpeResult(data){
	data.cpeType = CPE_TYPE[data.cpeType]==null?"--":CPE_TYPE[data.cpeType];
	data.macType = MAC_TYPE[data.macType]==null?"--":MAC_TYPE[data.macType]; 
	if(data.ipAddress==null||data.ipAddress==""){
		data.ipAddress="--";
	}
	data.vlanId = data.vlanId ? data.vlanId : "--";
	tpl = new Ext.XTemplate(
			'<table cellpadding="0" cellspacing="0" rules="none" border="0" id="result">',
				'<tbody>',
					'<tr>',
						'<td class="blueTxt">@Locate.centralDevice@:</td>',
						'<td><a href="javascript:;" class="yellowLink" onclick="showEntity(true);">{oltName} ({oltIp})</a></td>',
						'<td class="blueTxt">MAC:</td>',
						'<td>{macLocation}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt">ONU:</td>',
						'<td><a href="javascript:;" class="yellowLink" onclick="showOnu();">{onuName}</a></td>',
						'<td class="blueTxt">@Locate.uni@:</td>',
						'<td>{uniDisplay}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt" width="60">VLAN:</td>',
						'<td  width="220">{vlanId}</td>',
						'<td class="blueTxt" width="60">@COMMON.macType@@COMMON.maohao@</td>',
						'<td>{macType}</td>',
					'</tr>',
					'<tr>',
						'<td class="blueTxt" width="60">IP:</td>',
						'<td  width="220">{ipAddress}</td>',
						'<td class="blueTxt" width="60">@COMMON.TerminalType@@COMMON.maohao@</td>',
						'<td>{cpeType}</td>',
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

function saveTerminalSelect(terminalSelect){
	$.ajax({
		url :'/network/saveTerminalUserView.tv',
		data : {
			terminalView : terminalSelect
		},
		dataType:'json',
		success : function(data){
			
		},
		error : function(){
			
		} 
	});
}

</script>
	</head>
	<body class="whiteToBlack">
		<div class="txtCenter mT100">
			<img src="../images/@Locate.logoImg@.png" />
		</div>
		<!-- <p class="queryTab"><a href="javascript:;" class="sel">CM</a><a href="javascript:;">ONU CPE</a></p> -->
		<div class="putSearch">
		    <div id="dropDownArrow" class="dropDownArrow jsShow" onselectstart="return false"><span class="jsShow">CM</span><a href="javascript:;" class="smallArrow jsShow"></a></div>
			<input class="normalInput lookLikeBaidu" id="macAddress" placeholder="MAC:" toolTip="@Locate.macIpt@" />
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