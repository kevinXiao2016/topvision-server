<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<Zeta:HTML>
<head>
<script src="/performance/js/jquery-1.8.3.js"></script>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    CSS  css.performanceMajorStyle
    CSS  cmc.cmCpeQuery
    CSS  css.white.disabledStyle
    MODULE cmc
    IMPORT cmc.js.cmCpeQuery
    IMPORT cmc.js.cmCpeQuery2
    IMPORT js.nm3k.Nm3kProgressBar
</Zeta:Loader>
<title></title>
<script type="text/javascript">
var KeyEvent = {}
//virtual key
KeyEvent.VK_BACKSPACE = 8;
KeyEvent.VK_TAB = 9;
KeyEvent.VK_ESC = 27;
KeyEvent.VK_UP = 38;
KeyEvent.VK_DOWN = 40;
KeyEvent.VK_DEL = 46;
KeyEvent.VK_0 = 47;
KeyEvent.VK_NUM0 = 95;
KeyEvent.VK_9 = 58;
KeyEvent.VK_A = 65;
KeyEvent.VK_C = 67;
KeyEvent.VK_P = 80;
KeyEvent.VK_V = 86;
KeyEvent.VK_X = 88;
KeyEvent.VK_NUM9 = 106;
KeyEvent.VK_F4 = 115;
KeyEvent.VK_F5 = 116;
KeyEvent.VK_ENTER = 13;
KeyEvent.VK_LEFT = 37;
KeyEvent.VK_RIGHT = 39;
KeyEvent.VK_PAGEDOWN = 34;
KeyEvent.VK_PAGEUP = 33;
</script>
<script type="text/javascript" src="/js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="/js/jquery/dragMiddle.js"></script>
<script type="text/javascript" src="/js/highstock/highstock.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
	var closedData = '${cmCpeView}';//外部要收起来的div的id;
	var bar; //头部进度条;
    var queryMode = ${queryMode};
    var otherCmts = false;
    var versionSupport = false;
	var cmId;
	var URL_LIST = [{url:"loadCmAttribute.tv" , handler: loadCmAttribute,justifyInHandler : true},
                    {url:"loadCmSF.tv"  , handler :  loadCmSF, body:"cmsf"},
					{url:"loadCpeList.tv"  , handler :  loadCpeList, body:"cpeList"},
                   	{url:"loadCpeActHistory.tv" , handler : loadCpeActHistory, body:"cpeAct"},
                   	{url:"loadCmActHistory.tv" , handler : loadCmActHistory, body:"cmAct"},
                   	{url:"loadCmServiceType.tv" , handler : loadCmServiceType, body:"cmServiceType"},
                   	{url:"loadOltAlertList.tv" , handler : loadOltAlertList, body:"oltAlert"},
                   	{url:"loadCmcAlertList.tv" , handler : loadCmcAlertList, body:"cmcAlert"},
                   	{url:"loadOltRunningInfo.tv" , handler : loadOltRunningInfo, body:"oltRunTimeInfoTable"},
                   	{url:"loadCmcRunningInfo.tv" , handler : loadCmcRunningInfo, body:"cmcRunTimeTable"},
                   	{url:"loadCmFlap.tv" , handler : loadCmFlap, body:"cmFlapInfo"}];
	var STEP_CURSOR;
	
	function isIP(strIP) { 
		var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g //匹配IP地址的正则表达式 
		if(re.test(strIP)) { 
			if( RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) return true; 
		} 
		return false; 
	}
	
	//记录所有关闭的外层div,存入数据库;
	function saveData(){
        var aData = [];
        $("#mainTableContainer").find("label.flagClose").each(function(){
        	var $outerDiv = $(this).parent().parent().parent();
        	if( $outerDiv.get(0).tagName == "DIV" ){
        		aData.push($outerDiv.attr("id"));
        	}
        })
        $.ajax({
			url: '/cmCpe/saveCmCpeView.tv', 
			cache:false, 
			method:'post',
			data: {
				cmCpeView : aData.join(",")
			},
			success: function() {
			},
			error: function(){
			}
		});
	}
	
	//用于从CM列表跳转过来
	var sourcePage = '${sourcePage}';
	var cmMac = '${cmMac}';
	
        $(function(){
			function autoHeight(){
				var h = $("#wh100").height() - 40;
				if(h>0){
					$("#openLayerLeft, #openLayerLine, #openLayerMain").height(h);
					var h2 = $("#putProgressBar").outerHeight(),
					    h3 = h-h2;
					if(h3 < 10) {h3 = 10;}
					$("#mainTableContainer").height(h3);
				}
			};//end autoHeight;
			
			autoHeight();
			$(window).wresize(function(){
				autoHeight();
			});//end wresize;

			$("#openLayerLeft div.leftPartAlink").live("click",function(){
				$("#openLayerLeft div.leftPartAlink").removeClass("leftPartAlinkSel");
				$(this).addClass("leftPartAlinkSel");
			});

			var o1 = new DragMiddle({ id: "openLayerLine", leftId: "openLayerLeft", rightId: "openLayerMain", minWidth: 200, maxWidth:300,leftBar:true });
			o1.init();
			
			if(sourcePage=='cmListPage'){
    	        $('#cmmacCondition').val(cmMac);
    	        queryData(true);
    	    }
            $("#queryButton").click(queryData);

            $(".flagInfo label").live("click",function(){
                var that = $(this);
                var cartoon = false;
                if($(this).hasClass("flagOpen")){
                    var nextObj = $(this).parent().parent().next();
                    if(nextObj.hasClass("dataTableAll") && nextObj[0].tagName == "TABLE" && cartoon == false){
                        cartoon = true;
                        nextObj.fadeOut("normal",function(){
                            that.removeClass("flagOpen").addClass("flagClose");
                            cartoon = false;
                            saveData();
                        })
                    }
                }else if($(this).hasClass("flagClose")){
                    var nextObj = $(this).parent().parent().next();
                    if(nextObj.hasClass("dataTableAll") && nextObj[0].tagName == "TABLE" && cartoon == false){
                        cartoon = true;
                        nextObj.fadeIn("normal",function(){
                            that.removeClass("flagClose").addClass("flagOpen");
                            cartoon = false;
                            saveData();
                        });
                    }
                }
            });
            //加入黑色tooltip;
            $("#putProgressBar").delegate(".nm3kProgressTxt", "mouseenter",function(){
            	var $bootStrapTip = $("#bootStrapTip"),
            		tOffset = 5, //偏移距离;
            		$me = $(this),
            		w = $me.outerWidth() / 2,
            		h = $me.outerHeight(),
            	    t = $me.offset().top + h + tOffset,
            	    l = $me.offset().left;
            	
            	if( $bootStrapTip.length === 0 ){
            		var str = '';
            		    str += '<div id="bootStrapTip" style="top:'+ t +'px">';
            		    str +=     '<div class="tooltip-arrow"></div>';
            		    str +=     '<div class="tooltip-inner">';
            		    str +=         '@COMMON.clickScroll@';
            		    str +=     '</div>';
            		    str += '</div>';
            		$("body").append(str);
            	}else{
            		$bootStrapTip.css({
            			display : 'block',
            			top : t
            		});
            	}
            	var tipW = $("#bootStrapTip").outerWidth() / 2;
            	$("#bootStrapTip").css({left: l - tipW + w});
            });
            $("#putProgressBar").delegate(".nm3kProgressTxt", "mouseout",function(){
            	$("#bootStrapTip").css({display: 'none'});
            });
            
            //数据库得到的是字符串，将其转为数组;
            if( closedData == '' ){
            	closedData = [];
            }else{
            	var arr = closedData.split(",");
            	closedData = arr;
            }
			//数据库中存了要收缩起来的table的外部div的id,将这些div收缩起来;
			$.each(closedData,function(i,n){
				var $n = $( "#"+n );
				if( $n.length === 1 ){
					var $p = $n.find(".flagP"),
					    $table = $p.next();
					
					$p.find("label").attr({"class" : "flagClose"});
					if( $table.get(0).tagName == "TABLE" ){
						$table.css({display : 'none'});
					}
				}
			});//end each;
        });//end document.ready;


</script>
<%
    String queryMode = (String)request.getAttribute("queryMode");
    String versionSupport = (String)request.getAttribute("versionSupport");
    String otherCmts = (String)request.getAttribute("otherCmts");
%>
</head>
<body>
	<div id="wh100">
		<div class="topLineBar">
			<ul class="topLineBarUl">
				<li>
					CM MAC:
				</li>
				<li class="mR15">
					<input type="text" class="normalInput" id="cmmacCondition"/>
				</li>
				<li>
					CPE MAC:
				</li>
				<li>
					<input type="text" class="normalInput" id="cpemacCondition"/>
				</li>
				<li>
					CPE IP:
				</li>
				<li class="mR15">
					<input type="text" class="normalInput" id="cpeipCondition"/>
				</li>
				<li>
					@CMC.title.Alias@:
				</li>
				<li class="mR15">
					<input type="text" class="normalInput" id="cmLocationCondition"/>
				</li>
				<li>
					<a id="queryButton" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i>@entity.alert.queryButton@</span></a>
				</li>
			</ul>
			
		</div>		
		
		<div id="openLayerLeft">
		</div>
		<div id="openLayerLine"></div>
		<div id="openLayerMain">
			<div id="putProgressBar" class="putProgressBar"></div>
			<a id="cancelProgress" href="javascript:;" class="normalBtn" onclick="cancelProgress(true)" style="display:none;"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a>
			<div id="mainTableContainer">	
				<div class="edge10">
					<div class="relativeDiv displayNone" id="cmPropertyOuter">
						<p class="flagP"><span class="flagInfo"><label class="flagOpen" id="label_cmPerp">@CM.cmProperty@</label></span></p>
						<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows" id="cmPropertyTable">
						 <thead>
							 <tr>
								 <th width="150">@PERF.mo.Index@</th>
								 <th>@CMCPE.value@</th>
							 </tr>
						 </thead>
						 <tbody style="display:none;">
							 <tr>
								 <td class="rightBlueTxt">@CMCPE.pingTest@:
								 </td>
								 <td id="cmping"></td>
							</tr>
							 <tr class="dataTableBlueBg">
								 <td class="rightBlueTxt"> @CM.serNum@:
								 </td>
								 <td id="cmsn"></td>
							</tr>
							 <tr>
								 <td class="rightBlueTxt"> @CM.docsisRegMod@:
								 </td>
								 <td id="cmregmode"></td>
							</tr>
							 <tr class="dataTableBlueBg">
								 <td class="rightBlueTxt"> @CM.softName@:
								 </td>
								 <td id="cmsoftware"></td>
							</tr>
							 <tr>
								 <td class="rightBlueTxt"> @CM.sysUpTime@:
								 </td>
								 <td id="cmuptime"></td>
							</tr>
							 <tr class="dataTableBlueBg">
								 <td class="rightBlueTxt"> @CM.configFileName@:
								 </td>
								 <td id="configurationfilename"></td>
							</tr>
							 <tr>
								 <td class="rightBlueTxt"> @CM.timingServer@:
								 </td>
								 <td id="cmntp"></td>
							</tr>
							 <tr class="dataTableBlueBg">
								 <td class="rightBlueTxt"> @CM.tftpServer@:
								 </td>
								 <td id="cmtftp"></td>
							</tr>
							<tr>
								 <td class="rightBlueTxt">
								 </td>
								 <td id="cmdhcp"></td>
							</tr>
                             <tr>
                                 <td class="rightBlueTxt"> Previous State:
                                 </td>
                                 <td id="previousState">123</td>
                             </tr>
						</tbody>
						<tbody>
							<tr>
								<td colspan="2" align="center">
									<span class="tableTdLoading">@COMMON.loading@</span>
								</td>
							</tr>
						</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(0,this)" id="cmPropertyTableBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="CmUpChannelOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_upChannel">@upchannel@</label></span></p>
						<table id="CmUpChannel" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						 <thead>
							 <tr>
							 	 <% if("2".equals(queryMode)){ %>
							 	 <th  colspan="5">@CCMTS.rfModuleStatus@</th>
								 <th colspan="2">@text.signalQuality@</th>
							 	 <%} else {%>
								 <th  colspan="5">@CCMTS.rfModuleStatus@</th>
								 <th colspan="1">@text.signalQuality@</th>
								 <% } %>
							 </tr>
							 <tr>
								 <th>@CHANNEL.channel@</th>
								 <th>@CMC.label.bandwidth@(MHz)</th>
                                 <% if(!"2".equals(queryMode)){ %>
                                 <th>@CHANNEL.width@(Mbps)</th>
                                 <% } %>
                                 <% if("2".equals(queryMode)){ %>
                                 <th>@CHANNEL.usBps@</th>
                                 <% } %>
								 <th>@cm.Frequency@(MHz)</th>
								 <th>@cm.SendPower@(@{unitConfigConstant.elecLevelUnit}@)</th>
								 <th>SNR(dB)</th>
								 <% if("2".equals(queryMode)){ %>
								 <th>@CHANNEL.usErrorRation@</th>
								 <% } %>
							 </tr>
						 </thead>
						 <tbody style="display:none;">
						</tbody>
						<tbody>
							<tr>
								<% if("2".equals(queryMode)){ %>
								<td colspan="7" align="center">
								<%} else {%>
								<td colspan="6" align="center">
								<% } %>
									<span class="tableTdLoading">@COMMON.loading@</span>
								</td>
							</tr>
						</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(0,this)" id="CmUpChannelBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="CmDownChannelOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_downChannel">@downchannel@</label></span></p>
						<table id="CmDownChannel" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						 <thead>
							 <tr>
							  	<% if("2".equals(queryMode)){ %>
							  	<th colspan="6">@CCMTS.rfModuleStatus@</th>
								<th colspan="2">@text.signalQuality@</th>
							  	<% } else { %>
							  	<th colspan="6">@CCMTS.rfModuleStatus@</th>
								<th colspan="1">@text.signalQuality@</th>
							  	<% } %>
								
							 </tr>
							 <tr>
								 <th>@CHANNEL.channel@</th>
								 <th>@CMC.label.bandwidth@(MHz)</th>
								 <% if(!"2".equals(queryMode)){ %>
								 <th>@CHANNEL.width@(Mbps)</th>
								 <% } %>
                                 <% if("2".equals(queryMode)){ %>
                                 <th>@CHANNEL.dsBps@</th>
                                 <% } %>
								 <th>@cm.Frequency@(MHz)</th>
								 <th>@cm.ReceivePower@(@{unitConfigConstant.elecLevelUnit}@)</th>
								 <th>@CHANNEL.modulationType@</th>
								 <th>SNR(dB)</th>
								  <% if("2".equals(queryMode)){ %>
                                 <th>@CHANNEL.dsErrorRation@</th>
								  <% } %>
							 </tr>
						 </thead>
						 <tbody style="display:none;">
						</tbody>
						<tbody>
							<tr>
							<% if("2".equals(queryMode)){ %>
								<td colspan="8" align="center">
							<% } else { %>
								<td colspan="7" align="center">
							<% } %>
									<span class="tableTdLoading">@COMMON.loading@</span>
								</td>
							</tr>
						</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(0,this)" id="CmDownChannelBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>

                    <div class="relativeDiv displayNone" id="cmifdiv">
                        <p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmif">@cm.interface@</label></span></p>
                        <table id="cmif" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                            <thead>
                            <tr>
                                <th align='center'>@cm.interface.portId@</th>
                                <th align='center'>@cm.interface.desc@</th>
                                <th align='center'>@cm.interface.status@</th>
                            </tr>
                            </thead>
                            <tbody style="display:none;">
                            </tbody>
                            <tbody>
                            <tr>
                                <td colspan="4" align="center">
                                    <span class="tableTdLoading">@COMMON.loading@</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(0,this);" id="cmifRefreshBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
                        </a>
                    </div>

                    <div class="relativeDiv displayNone" id="cmsfdiv">
                        <p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmsf">@cm.serviceflow@</label></span></p>
                        <table id="cmsf" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                            <thead>
                            <tr>
                                <th align='center'>@cm.serviceflow.id@</th>
                                <th align='center'>@cm.serviceflow.type@</th>
                                <th align='center'>@cm.serviceflow.MaxSusRate@</th>
                            </tr>
                            </thead>
                            <tbody style="display:none;">
                            </tbody>
                            <tbody>
                            <tr>
                                <td colspan="4" align="center">
                                    <span class="tableTdLoading">@COMMON.loading@</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(1,this);" id="cmsfRefreshBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
                        </a>
                    </div>

                    <div class="relativeDiv displayNone" id="cpelistdiv">
                        <p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cpeList">@cm.cpeInfo@</label></span></p>
                        <table id="cpeList" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                            <thead>
                            <tr>
                                <th align='center'>IP</th>
                                <th align='center'>MAC</th>
                                <th align='center'>@CMC.text.type@</th>
                                <th align='center'>@CM.operate@</th>
                            </tr>
                            </thead>
                            <tbody style="display:none;">
                            </tbody>
                            <tbody>
                            <tr>
                                <td colspan="4" align="center">
                                    <span class="tableTdLoading">@COMMON.loading@</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <ul class="leftFloatUl clearBoth" style="position:absolute; right:0px; top:0px;">
                        	<li>
                        		<a href="javascript:;" class="normalBtn" onclick="fdbAddressShow();" id="fdbAddressRefreshBtn" >
					                <span>
					                    <i class="miniIcoInfo"></i>@CM.fdbinfo@
					                </span>
		                        </a>
                        	</li>
                        	<li>
                        		<a href="javascript:;" class="normalBtn rightTopLink" style="position:static;" onclick="refreshTable(2,this);" id="cpeListRefreshBtn" >
					                <span>
					                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
					                </span>
		                        </a>
                        	</li>
                        </ul>
                    </div>

					<div class="relativeDiv displayNone" id="cpeActOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cpeAct">CPE@CMCPE.lastSevenDayAction@</label></span></p>
						<table id="cpeAct" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
							 <thead>
								 <tr>
									 <th>CPE MAC</th>
									 <th>CPE IP</th>
									 <th>@cmList.action@</th>
									 <th>@PERF.mo.time@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
							</tbody>
							<tbody>
								<tr>
									<td colspan="4" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(3,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="cmActOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmAct">CM@CMCPE.lastSevenDayAction@</label></span></p>
						<table id="cmAct" class="dataTableAll"  width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
							<thead>
								 <tr>
								 	 <th>@resources/COMMON.uplinkDevice@</th>
								 	 <th>@CMCPE.interface@</th>
									 <th>CM IP</th>
									 <th>@cmList.action@</th>
									 <th>@PERF.mo.time@</th>
								 </tr>
							</thead>
							<tbody style="display:none;">
							</tbody>
							<tbody>
								<tr>
									<td colspan="5" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(4,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="cmServiceTypeDiv">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmServiceType">@CM.serviceTypelog@</label></span></p>
						<table id="cmServiceType" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
							 <thead>
								 <tr>
					                <th align='center'>@CM.beforeModify@</th>
					                <th align='center'>@CM.afterModify@</th>
					                <th align='center'>@CM.modifytime@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
							</tbody>
							<tbody>
								<tr>
									<td colspan="4" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(5,this);" id="cmServiceTypeRefreshBtn" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div id="oltAlertOuter" class="relativeDiv displayNone">	
						<p class="flagP mT10" id="pOltALert"><span class="flagInfo"><label class="flagOpen" id="label_oltAlert">@CMCPE.oltAlert@</label></span></p>
						<table id="oltAlert" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						 <thead>
							 <tr>
								 <th>@CCMTS.entityDescrib@</th>
								 <th>@entity.alert.type@</th>
								 <th>@entity.alert.firstTimeStr@</th>
								 <th>@entity.alert.lastTimeStr@</th>
								 <th>@entity.alert.confirmStatus@</th>
								 <th>@resources/FAULT.clearStatus@</th>
							 </tr>
						 </thead>
						 <tbody style="display:none;">
						</tbody>
						<tbody>
								<tr>
									<td colspan="7" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(6,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="cmcAlertOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmcAlert">@CMCPE.cmcAlert@</label></span></p>
						<table id="cmcAlert" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
							 <thead>
								 <tr>
									 <th>@CCMTS.entityDescrib@</th>
									 <th>@entity.alert.type@</th>
									 <th>@entity.alert.firstTimeStr@</th>
									 <th>@entity.alert.lastTimeStr@</th>
									 <th>@entity.alert.confirmStatus@</th>
									 <th>@resources/FAULT.clearStatus@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
							</tbody>
							<tbody>
								<tr>
									<td colspan="7" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(7,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div id="oltRunTimeInfoOuter" class="relativeDiv displayNone">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmcRuntimeInfo">@CMCPE.oltRunTimeInfo@</label></span></p>
						<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows" id="oltRunTimeInfoTable">
							 <thead>
								 <tr>
									 <th width="100">@PERF.mo.Index@</th>
									 <th>@CMCPE.value@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
								 <!-- <tr>
									 <td class="rightBlueTxt"> @CMCPE.inPon@:</td>
									 <td id="ponId"></td>
								</tr>
								 <tr class="dataTableBlueBg">
									 <td class="rightBlueTxt"> PON@CMC.title.rxOpticalPower@:</td>
									 <td id="txPower"></td>
								</tr>
								 <tr>
									 <td class="rightBlueTxt"> PON@CMC.title.receiveOpticalPower@:</td>
									 <td id="rxPower"></td>
								</tr> -->
							</tbody>
							<tbody>
								<tr>
									<td colspan="2" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(8,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div class="relativeDiv displayNone" id="cmcRunTimeOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmcRuntimeInfo">@CMCPE.cmcRunTimeInfo@</label></span></p>
						<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows" id="cmcRunTimeTable">
							 <thead>
								 <tr>
									 <th width="150">@PERF.mo.Index@</th>
									 <th>@CMCPE.value@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
								<!-- <tr>
									 <td class="rightBlueTxt">@CCMTS.entityType@:</td>
									 <td id="cmcType"></td>
								</tr>
								 <tr class="dataTableBlueBg">
									 <td class="rightBlueTxt"> @CMCPE.interface@:</td>
									 <td id="interface"></td>
								</tr>
								 <tr>
									 <td class="rightBlueTxt"> SNR:</td>
									 <td id="snr"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt"> @CM.correcteds@:</td>
									 <td id="bitErrorRate"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt"> @CM.uncorrectRate@:</td>
									 <td id="unBitErrorRate"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt"> @CMCPE.CMONLINENUM@:</td>
									 <td id="cmOnlineNum"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt"> @CMCPE.CMOFFLINENUM@:</td>
									 <td id="cmOfflineNum"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt"> @CMCPE.inFlow@:</td>
									 <td id="channelInOctetsRate"></td>
								</tr> -->
							</tbody>
							<tbody>
								<tr>
									<td colspan="2" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(9,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<div id="snrHisPerf" style="margin: 10px 0px;" ></div>
					<div id="bitErrorRateHisPerf" style="margin: 10px 0px;"></div>
					<div id="unBitErrorRateHisPerf" style="margin: 10px 0px;"></div>
					<div id="cmOnlineNumHisPerf" style="margin: 10px 0px;"></div>
					<div id="cmOfflineNumHisPerf" style="margin: 10px 0px;"></div>
					<div id="channelInOctetsRateHisPerf" style="margin: 10px 0px;"></div>
					
					<div class="relativeDiv displayNone" id="cmFlapInfoOuter">
						<p class="flagP mT10"><span class="flagInfo"><label class="flagOpen" id="label_cmFlapInfo">CM FLAP</label></span></p>
						<table id="cmFlapInfo" class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
							 <thead>
								 <tr>
									 <th width="150">@PERF.mo.Index@</th>
									 <th>@CMCPE.value@</th>
								 </tr>
							 </thead>
							 <tbody style="display:none;">
								 <tr>
									 <td class="rightBlueTxt">@CMCPE.CmFlap.PowerAdjTimes@:
									 </td>
									 <td id="powerAdj"></td>
								</tr>
								 <tr>
									 <td class="rightBlueTxt">@CMCPE.CmFlap.RangeHitRate@:
									 </td>
									 <td id="range"></td>
								</tr>
								<tr>
									 <td class="rightBlueTxt">@cmc.flap.onlineCounter@:
									 </td>
									 <td id="flapIns"></td>
								</tr>
							</tbody>
							<tbody>
								<tr>
									<td colspan="2" align="center">
										<span class="tableTdLoading">@COMMON.loading@</span>
									</td>
								</tr>
							</tbody>
						</table>
						<a href="javascript:;" class="normalBtn rightTopLink" onclick="refreshTable(10,this)" disabled="disabled">
			                <span>
			                    <i class="miniIcoRefresh"></i>@CMC.button.refresh@
			                </span>
			            </a>
					</div>
					
					<!-- <div style="margin: 10px 0px;" id="cmFlapInsFailNumChart"></div> -->
					<div style="margin: 10px 0px;"  id="cmFlapPowerAdjNumChart"></div>
					<div style="margin: 10px 0px;"  id="cmFlapRangeChart"></div>
					<div style="margin: 10px 0px;"  id="cmFlapInsRealChart"></div>
					
	            	</div>
           </div>
		</div>
	</div>
	
</body>
</Zeta:HTML>
