<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	css css.main
	css css.reset
	library Jquery
	library ext
	library zeta
    module network
    import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js.nm3k.menuNewTreeTip
</Zeta:Loader>
<style type="text/css">
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
</style>
</head>
<body>
	<!-- 滑动收缩栏 -->
	<div class="putSlider" id="putSlider">
		<div class="sliderOuter" id="sliderOuter">
			<a id="sliderLeftBtn" href="javascript:;"></a>
			<div id="slider" class="slider">
				<span id="bar" class="bar"></span>
			</div>
			<a id="sliderRightBtn" href="javascript:;"></a>
		</div>
	</div>	
	<div class="putTree" id="putTree">
		<div style="width:100%; overflow:hidden;">
			<ul id="tree" class="filetree">	
				<%if(uc.hasPower("deviceView")){ %>
				<li>
					<span class="icoB1">
						<a href="javascript:;" class="linkBtn" onclick="showNetworkDashboard()">@fault/ALERT.topN@</a>
					</span>
				</li>
				<%} %>
				<li>
					<span class="icoB13">
						<a href="javascript:;" class="linkBtn" onclick="showTerminalLocate()">@a.title.cmLocate@</a>
					</span>
				</li>
				<% if(uc.hasSupportModule("cmc")){%>
					<%if(uc.hasPower("cmList")){ %>	
					<li>
						<span class="icoB6">
							<a href="javascript:;" class="linkBtn" onclick="showCmSnapNew()">@a.title.cmList@</a>
						</span>
					</li>
					<%} %>
				<%} %>
				<% if(uc.hasSupportModule("cmc")){%>
					<%if(uc.hasPower("cmCpeQuery")){ %>
					<li>
						<span class="icoG13">
							<a href="javascript:;" class="linkBtn" onclick="showCmCpeQuery()">@a.title.cmCpeQuery@</a>
						</span>
					</li>
					<%} %>
				<%} %>
				<% if(uc.hasSupportModule("cmc")){%>
                    <li>
                        <span class="icoB5">
                            <a href="javascript:;" class="linkBtn" onclick="showCmtsInfo()">@a.title.cmtsInfo@</a>
                        </span>
                    </li>
                <%} %>
				<% if(uc.hasSupportModule("cmc")){%>
					<%if(uc.hasPower("dispersion")){ %>
					<li>
						<span class="icoG14">
							<a href="javascript:;" class="linkBtn" onclick="showDispersion()">@performance/Performance.dispersion@</a>
						</span>
					</li>
					<%} %>
				<%} %>
				<li>
					<span class="folder">@COMMON.performanceSetting@</span>
					<ul>
						<%if (uc.hasPower("performanceBatchConfig")) {%>
						<li>
							<span class="icoE12">
								<a href="javascript:;" class="linkBtn" onclick="showBatch()">@performance/Performance.batchConfig@</a>	
							</span>
						</li>
						<%}%>
						<%if (uc.hasPower("perfTemplateMgmt")) {%>
						<li>
							<span class="icoE1">
								<a href="javascript:;" class="linkBtn" onclick="showTargetManage()">@performance/Performance.targetManage@</a>	
							</span>
						</li>
						<li>
							<span class="icoE1">
								<a href="javascript:;" class="linkBtn" onclick="showPerfTemplate()">@performance/Performance.perfTemplateMgmt@</a>	
							</span>
						</li>
						<%}%>
						<% if(uc.hasSupportModule("cmc")){%>
							<%if(uc.hasPower("terminalCollectConfig")) {%>
							<li>
								<span class="icoE13">
									<a href="javascript:;" class="linkBtn" onclick="showTerminalCollectConfig()">@resources/cmCpe.terminalConfig@</a>	
								</span>
							</li>
							<%} %>
						<%} %>
					</ul>
				</li>
				<% if(uc.hasSupportModule("cmc")){%>
				<li>
					<span class="folder">@COMMON.spectrumSetting@</span>
					<ul>
						<% if(uc.hasSupportModule("olt") && uc.hasPower("oltSpectrumConfig")){ %>
						<li>
							<span class="icoG7">
								<a href="javascript:;" class="linkBtn" onclick="showOltSpectrumConfig()">@spectrum/spectrum.oltSpectrumCollectConfig@</a>	
							</span>
						</li>
						<% }if(uc.hasPower("cmtsSpectrumConfig")){ %>
						<li>
							<span class="icoG7">
								<a href="javascript:;" class="linkBtn" onclick="showCmtsSpectrumConfig()">@spectrum/spectrum.spectrumCollectConfig@</a>	
							</span>
						</li>
						<%} if(uc.hasPower("spectrumAlertConfig")){ %> 
						<li>
							<span class="icoG7">
								<a href="javascript:;" class="linkBtn" onclick="showSpectrumAlertConfig()">@spectrum/spectrum.alertConfig@</a>	
							</span>
						</li>
						<%} %> 
					</ul>
				</li>
				<li>
					<span class="folder">@COMMON.spectrumVidio@</span>
					<ul>
						<% if(uc.hasPower("spectrumVideoMgmt")){ %>
						<li>
							<span class="icoE14">
								<a href="javascript:;" class="linkBtn" onclick="showSpectrumVideoMgmt()">@performance/performance.spectrumVideoMgmt@</a>	
							</span>
						</li>
						<%}%>
					</ul>
				</li>
				<%} %>
			</ul>
		</div>
	</div>	
	<div id="threeBoxLine"></div>
	<!-- 功能区 -->
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<%if (uc.hasPower("attentionAlarm")) {%>
			<li><a href="#" class="icoF2" name="" onclick="showAttentionAlert()">@fault/ALERT.attentionAlert@</a></li>
			<% } %>
			<%if (uc.hasPower("setAttentionAlarm")) {%>
			<li><a href="#" class="icoF4" name="" onclick="setAttentionAlert()">@fault/ALERT.setAttentionAlert@</a></li>
			<% } %>
		</ol>
	</div>
	<!-- 刷新树时候的loading -->
	<div id="treeLoading" class="treeLoading">
		Loading...
	</div>
<script type="text/javascript">
	$(function(){
		//加载树形菜单;
		$("#tree").treeview({ 
			animated: "fast",
			control:"#sliderOuter"
		});	//end treeview;
		$("#sliderLeftBtn").click(function(){
			$("#bar").stop().animate({left:0});			
		})
		$("#sliderRightBtn").click(function(){
			$("#bar").stop().animate({left:88});		
		})
		
		//点击树形节点变橙色背景;
		$(".linkBtn").live("click",function(){
			$(".linkBtn").removeClass("selectedTree");
			$(this).addClass("selectedTree");
		});//end live;
	}); //end document.ready;
		
		
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; 
		var h3 = $("#threeBoxLine").outerHeight();
		var h4 = $("#putBtn").outerHeight();
		var putTreeH = h - h1 - h2 - h3 - h4;
		if(putTreeH > 20){
			$("#putTree").height(putTreeH);
		}	
	};//end autoHeight;
	
	//resize事件增加[函数节流];
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	autoHeight();
	$(window).wresize(function(){
		throttle(autoHeight,window)
	});//end resize;

	//指标排行
	function showNetworkDashboard() {
	    top.addView("networkdashboard2", '@fault/ALERT.topN@', "tabIcoB1", "/network/showDeviceViewJsp.tv", null, true);
	}
	//查看终端实时定位
	function showTerminalLocate(){
		top.addView("CmLocate", "@a.title.cmLocate@" , "icoB13", "/network/showTerminalLocation.tv");
	}
	//cm列表
	function showCmSnapNew() {
	    top.addView("cmListNew", '@a.title.cmList@', "tabIcoB6", "/cmlist/showCmListPage.tv");
	}
	//查看CM\CPE综合查询页面
	function showCmCpeQuery(){
		top.addView("CmCpeQuery", "@a.title.cmCpeQuery@", "icoG13", "/cmCpe/showCmCpeQuery.tv");
	}
	//查看cmts网络概况
	function showCmtsInfo(){
		top.addView("cmtsInfo","@a.title.cmtsInfo@","icoB5","/cmtsInfo/showCmtsInfoPage.tv")
	}
	//离散度;
	function showDispersion(){
		top.addView("dispersion-list", "@performance/Performance.dispersion@", "icoG14", "/dispersion/showDispersion.tv");
	}
	//批量配置;
	function showBatch(){
	 	top.addView("batch", "@performance/Performance.batchConfig@", "icoE12", "/performance/showBatchConfigTarget.tv",null,true);
	}
	//阈值指标管理;
	function showTargetManage(){
		top.addView("thresholdTargetManage", "@performance/Performance.targetManage@", "icoE1", "/performance/perfThreshold/showTargetManage.tv",null,true);
	}
	//模板管理
	function showPerfTemplate(){
		top.addView("perfTemplate", "@performance/Performance.perfTemplateMgmt@", "icoE1", "/performance/perfThreshold/showPerfTemplate.tv",null,true);
	}
	//查看终端采集配置页面
	function showTerminalCollectConfig(){
		top.addView("terminalCollectConfig", "@resources/cmCpe.terminalConfig@", "icoE13", "/cmCpe/showTerminalCollectConfig.tv");
	}
	//OLT频谱采集配置页面
	function showOltSpectrumConfig(){
		top.addView("showOltSpectrumConfig", "@spectrum/spectrum.oltSpectrumCollectConfig@", "icoG7", "/cmcSpectrum/showOltSpectrumConfig.tv");
	}
	//CMTS频谱采集配置页面
	function showCmtsSpectrumConfig(){
		top.addView("showCmtsSpectrumConfig", "@spectrum/spectrum.spectrumCollectConfig@", "icoG7", "/cmcSpectrum/showCmtsSpectrumConfig.tv");
	}
	//频谱噪声告警配置页面
	function showSpectrumAlertConfig(){
		top.addView("showSpectrumAlertConfig", "@spectrum/spectrum.alertConfig@", "icoG7", "/cmcSpectrum/showSpectrumAlertConfig.tv");
	}
    //CMTS频谱录像管理页面
	function showSpectrumVideoMgmt(){
		top.addView("showSpectrumVideoMgmt", "@performance/performance.spectrumVideoMgmt@", "icoE14", "/cmcSpectrum/showSpectrumVideoMgmt.tv");
	}
    //关注告警
    function showAttentionAlert(){
    	top.addView('attentionAlert','@fault/ALERT.attentionAlert@', "icoF2", "alert/showCurrentAlertList.tv?alertId=0&userAlert=true" ,null,true);	
    }
    //设置我关注的告警;
    function setAttentionAlert(){
    	top.addView("setAttentionAlert", "@fault/ALERT.setAttentionAlert@", "icoF4", "alert/showAttentionAlert.tv");
    }
</script>
</body>
</Zeta:HTML> 
