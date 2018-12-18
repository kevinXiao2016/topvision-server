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
<script type="text/javascript" src="../js/tools/treeBuild_0.1.js"></script>
<style>
.reportFolderIcon {background-image: url(../images/report/folder16.gif);}
.reportIcon {background-image: url(../images/report/report16.gif);}
.icoA1, .icoA2, .icoA3, .icoA4, .icoA5, .icoA6, .icoB1, .icoB2, .icoB3, .icoB4, .icoB5, .icoB6, .icoB7, .icoB8, .icoB9, .icoB10, .icoB11, .icoB13_notBtn, .icoC1, .icoC2, .icoC3, .icoC4, .icoD1, .icoD2, .icoD3, .icoD4, .icoD5, .icoD6, .icoE1, .icoE2, .icoE3, .icoE4, .icoE5, .icoE6, .icoE7, .icoE8, .icoE9, .icoE10, .icoE11, .icoG1, .icoG2, .icoG3, .icoG4, .icoG5, .icoG6, .icoG7, .icoG8, .icoG9, .icoG10, .icoG11, .icoG12, .icoF1, .icoF2, .icoF3, .icoF4, .icoF5, .icoH1, .icoH2, .icoH3, .icoH4, .icoH5, .icoH6, .icoH7{display: inline-block;}
.filetree span.folder, .filetree span.file{display: inline-block;}
.wrapper{
	overflow: auto;
	padding:10px;
}
.btn{
	padding: 6px;
	cursor: pointer;
}
</style>
<script>
$(function(){
	//获取wrapper应有的高度
	//$('.wrapper').height($(document).outerHeight())
	//加载报表树
    $.ajax({
		url: '/report/loadAllReportTemplate.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(array) {
   			$.each(array, function(index, reportFolder){
   				treeBuild.generateTree('tree', reportFolder, true, 'display');
   			})
   			insertJarReport();
   			$("#tree").treeview({ 
				animated: "fast",
				control:"#sliderOuter",
				collapsed: false
			});
   			
   			//加载树形菜单;
			/*  */
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
	
  	//插入原报表树
	function insertJarReport(){
		$.get('/report/loadAvailableReports.tv', function(reportList){
			//遍历报表，来进行插入
			for(var i=0; i<reportList.length; i++){
				var	currentReport = reportList[i],
					induction =  currentReport.induction.split('\.')[1],
					id = currentReport.id,
					title = currentReport.title,
					parentUl = null;
				if(induction==='resourceReport'){//资源报表
					parentUl = $('#tree_10000 > ul');
				}else if(induction==='performanceReport'){
					//性能报表
					parentUl = $('#tree_20000 > ul');
				}else if(induction==='alertReport'){//alert report
					parentUl = $('#tree_30000 > ul');
				}
				// insert
				if(parentUl){
					//delete old report
					deleteReport(id);
				}
			}
		});
	}
	
	function deleteReport(reportId){
		var reportMap = {
			'oltList': 10001,
			'oltBoardList': 10002,
			'oltSniPortList': 10003,
			'oltPonPortList': 10004,
			'onuList': 10005,
			'ccmtsList': 10006,
			'cmtsList': 10009,
			'oltCpuRank': 20001,
			'oltMemRank': 20002,
			'oltDelayRank': 20005
		}
		var oldReportId = reportMap[reportId];
		if(oldReportId){
			$('#tree').find('#tree_'+oldReportId).remove();
		}
	}
	
	function getI18NString(str){
		var parts = str.split('\.'), resultStr;
		for(var i=0; i<parts.length; i++){
			if(resultStr){
				resultStr = resultStr[parts[i]];
			}else{
				resultStr = I18N[parts[0]];
			}
		}
		return resultStr;
	}
	
	//为报表树绑定事件
	$('#reportTree').bind('click',function(e){
		//只响应checkbox的事件
		var target = e.target, type = target.type;
		if(type !== 'checkbox'){
			e.preventDefault();
			e.stopPropagation();
			return;
		}
		//获取该checkbox的父li的id，如果id的后面是10000....这种整数，表明是大类型，否则是小类型
		var parentLiId = $(target).parent().attr('id').split('_')[1];
		if(parentLiId%10000===0){
			//此时选中的是大类型，保证其子类型checkbox状态与其一致
			$(target).parent().find('ul').find('input[type="checkbox"]').attr("checked",target.checked);
		}else{
			//此时选中的是小类型,如果所有checkbox都被未选中，则父节点不选中，否则选中父节点
			//父节点下的checkbox数目
			var parentUL = $(target).parent().parent(),
				allLength = parentUL.find('li').length,
				selectedLength = parentUL.find('input[type="checkbox"]:checked').length;
			if(selectedLength===0){
				parentUL.prevAll('input[type="checkbox"]').attr("checked",false);
			}else{
				parentUL.prevAll('input[type="checkbox"]').attr("checked",true);
			}
		}
	})
	
	$('#saveReportDisplay').bind('click', function(){
		var reportDisplay = [];
		//获取所有报表的选中状态
		$.each($('#tree').find('input[type="checkbox"]'), function(index, checkbox){
			//获取当前checkbox的选中状态
			var checked = $(checkbox).attr("checked"),
				reportTempleteId = $(checkbox).parent().attr('id').split('_')[1];
			reportDisplay[reportDisplay.length] = {
				id : reportTempleteId,
				display : checked
			};
		});
		//进行修改操作
	   $.ajax({
			url: '/report/updateReportDisplay.tv',
	    	type: 'POST',
	    	data:{reportDisplay : JSON.stringify(reportDisplay)},
	    	dataType:'json',
	   		success: function() {
	   		}, error: function() {
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	})
});
</script>
</head>
<body class="openWinBody">
	<div class="wrapper">
		<h1>@label.pleaseselectreport@</h1>
		
		<div id="reportTree">
			<ul id="tree" class="filetree">
			</ul>
		</div>
		
		<button id="saveReportDisplay" class="btn">@label.save@</button>
	</div>
</body>
</Zeta:HTML>