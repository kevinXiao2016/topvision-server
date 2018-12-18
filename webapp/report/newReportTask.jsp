<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module report
    import js/jquery/jquery.wresize
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/reset.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<style>
.reportFolderIcon {background-image: url(../images/report/folder16.gif);}
.reportIcon {background-image: url(../images/report/report16.gif);}
.content{
	margin-top: 10px;
}
</style>
</head>
<body class="openWinBody">
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@report.selectReportTemplate@</div>
		<div class="rightCirIco linkCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<!-- 第二部分，核心布局区域 -->
	<div class="content">
		<table class="mCenter tdEdged4">
			<tr>
				<td style="padding: 5px 10px 0 10px">
					<table width=100% height=100% cellspacing=0 cellpadding=0>
						<tr>
							<td class=TREE-CONTAINER>
								<div class="putTree" id="putTree" style="width: 540px; height: 280px;">
									<div style="width:100%; overflow:hidden;">
										<ul id="tree" class="filetree">
											<li id="resource_root">
												<span class="folder"><a href="javascript:;">@report.resourceReport@</a></span>
												<ul></ul>
											</li>
											<li id="performance_root">
												<span class="folder"><a href="javascript:;">@report.performanceReport@</a></span>
												<ul></ul>
											</li>
											<li id="alert_root">
												<span class="folder"><a href="javascript:;">@report.alertReport@</a></span>
												<ul></ul>
											</li>
											<li id="custom_root">
												<span class="folder"><a href="javascript:;">@report.customReport@</a></span>
												<ul></ul>
											</li>
										</ul>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<!-- 第二部分，核心布局区域 -->
	
	<!-- 第三部分，按钮组合 -->
	<div class="noWidthCenterOuter clearBoth">
	    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	    	<li><a href="javascript:;" id="okBtn" class="normalBtnBig disabledAlink" onclick="nextClick()"><span><i class="miniIcoArrRight"></i>@COMMON.next@</span></a></li>
	        <li><a href="javascript:;" id="cancelBtn" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	    </ol>
	</div>
	<!-- 第三部分，按钮组合 -->
	
	<script type="text/javascript">
		var tree = null;
		function nextClick() {
			if($('#okBtn').hasClass('disabledAlink')) return;
			//报表条件配置页面
			window.top.getWindow("modalDlg").setTitle("@report.reportTemplate@:"+window.top.reportTask.reportName);
			location.href = 'showTaskSchedule.tv?r='+Math.random();
		}
		function cancelClick() {
			window.top.ZetaCallback = null;
			window.top.closeWindow("modalDlg");
		}
		function trimAllBlank(value){
			var chars = [];
			for(var i=0, len = value.length; i<len; i++){
				if(value[i]!=' '){
					chars.push(value[i]);
				}
			}
			return chars.join('');
		}
		$(function(){
			//加载新框架报表，整理报表数据
			$.get('/report/loadAvailableReports.tv', function(reportList){
				//sort
			    reportList.sort(function(a, b){
			        var aTitle = a.title,
			            bTtile = b.title;
			        if(aTitle > bTtile){
			            return 1;
			        }else if(aTitle < bTtile){
			            return -1;
			        }else{
			            return 0;
			        }
			    });
			    //遍历报表，来进行插入
			    var curReport;
				for(var i=0, len=reportList.length; i<len; i++){
					curReport = reportList[i];
					var parentUl = $('#'+curReport.type+'_root > ul');
					//判断当前报表是否有多层路径
					var reportPaths = curReport.path.split('/');
					if(reportPaths.length>1){
						//为每一层创建结构
						for(var j=1, jlen=reportPaths.length; j<jlen; j++){
							if(!$('#'+curReport.type+'_'+reportPaths[j]).length){
								parentUl.append(String.format('<li id="{0}"><span class="folder"><a href="javascript:;" class="blueLink">{1}</a></span><ul></ul></li>', curReport.type+'_'+reportPaths[j], reportPaths[j]));
								parentUl = $('#'+curReport.type+'_'+reportPaths[j]+' > ul');
							}
							parentUl = $('#'+curReport.type+'_'+reportPaths[reportPaths.length-1]+' > ul');
						}
					}
					parentUl.append(String.format('<li id="{0}"><span class="{1}"><a href="javascript:;" class="linkBtn jarReport" id="{0}">{2}</a></span></li>', curReport.id, 'icoG1', curReport.title));
				}
				//为报表树中的叶子节点报表绑定事件
				$('#tree').bind('click', function(e){
					//停止冒泡
					//e.stopPropagation();
					var $target = $(e.target);
			    	if(!$target.hasClass('linkBtn')){
			    		$('#okBtn').addClass('disabledAlink');
			    		return;
			    	}
			    	$('#okBtn').removeClass('disabledAlink');
			    	if(!window.top.reportTask){
			    		window.top.reportTask = {};
			    	}
			    	window.top.reportTask.reportId = $target.attr("id");
			    	window.top.reportTask.reportName = trimAllBlank($target[0].innerHTML);
			    	window.top.reportTask.taskName = trimAllBlank($target[0].innerHTML);
				});
					
				//生成树控件
				treeBasicHandle();
				//treeFloatTip();
			});
		});//end document.ready;
	</script>
</body>
</Zeta:HTML>