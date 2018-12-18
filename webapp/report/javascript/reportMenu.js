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
			e.stopPropagation();
			var target = e.target;
			if(target.nodeName.toLowerCase()!=='a'){
				return false;
			}
			var $target = $(target);
			if($target.hasClass('jarReport')){ 
				//jar包的报表
				showReportDetail($target.attr('id'), $target.text())
			}else if($target.hasClass('linkBtn')){	
				//数据库中的报表
				lookForReportDetail($target.attr('id'), $target.attr('name'), $target.attr('url'));
			}
		});
			
		//生成树控件
		treeBasicHandle();
		treeFloatTip();
	});
});//end document.ready;