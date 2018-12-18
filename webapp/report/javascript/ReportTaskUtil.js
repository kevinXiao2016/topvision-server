/********************************************************
						任务报表的通用类
 *******************************************************/
var ReportTaskUtil = function(){}

function validate(){
    var reg;
    //input验证 ，title，author
    reg = /^[^\\/:*?"<>|]{1,32}$/;
    var title = $("#title").val();
    if(!reg.test(title)){
        $("#title").focus();
        return false;
    }
    var author = $("#author").val();
    if(!reg.test(author)){
        $("#author").focus();
        return false;
    }
    return true;
}

/**
 * 任务报表的通用检查方法
 */
ReportTaskUtil.check = function(zetaCallback){
		var reportTitle = $("#title").val();
		var reportSigner = $("#author").val();
		
/*		if(!validate){
			return window.parent.showMessageDlg("@COMMON.tip@", "@report.reportTilteNotNull@","error",function(){
				$("#title").focus();
			}) && false;
		}
		if(!reportSigner){
			return window.parent.showMessageDlg("@COMMON.tip@", "@report.reportAuthorNotNull@","error",function(){
				$("#author").focus();
			}) && false;
		}*/
		
		if(!validate()){return;}
		//生成报表类型
		zetaCallback.pdfSupport = Ext.get("pdfSupport").dom.checked;
		zetaCallback.excelSupport = Ext.get("excelSupport").dom.checked;
		zetaCallback.htmlSupport = Ext.get("htmlSupport").dom.checked;
		//对应的模板
		zetaCallback.reportCreatorBeanName = zetaCallback.templateName;
		var conditionMap = zetaCallback.conditionMap;
			conditionMap.title = reportTitle;
			conditionMap.author = reportSigner;
		return true;
}

/**
 * 任务报表的通用提交方法
 */
ReportTaskUtil.commit = function(zetaCallback){
	//window.top.showWaitingDlg("@COMMON.wait@", "@report.creating@", 'ext-mb-waiting');
	$.ajax({
		url:'createReportTask.tv',cache:false,metho:'POST',
		data: zetaCallback,
		success:function(){
			//window.parent.showMessageDlg("@COMMON.tip@", String.format("@report.createOk@", zetaCallback.taskName ));
			cancelClick();
			var frame = window.top.getFrame('allreporttask');
            if (frame != null) {
                frame.onRefreshClick();
            }else{
            	window.parent.addView("allreporttask", '@report.allReportTask@', "taskIcon", "report/showAllReportTask.tv");
            }
            top.afterSaveOrDelete({
				title: '@COMMON.tip@',
				html: '<b class="orangeTxt">' + String.format("@report.createOk@", zetaCallback.taskName) + '</b>'
			});
		},error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", String.format("@report.createER@", zetaCallback.taskName ));
		}
	});
}

ReportTaskUtil.refreshTaskTree = function(){
	//如果该frame已经打开，则刷新报表任务结构树
	var $frame = window.top.getFrame('allreport');
	$frame && $frame.templateTree && $frame.templateTree.root && $frame.templateTree.root.reload && $frame.templateTree.root.reload();
}
