/* ========================================================================
 * Topvision: customReportTemplate.js v1.3
 * ========================================================================
 *
 * 报表新框架的通用前台逻辑模版.
 * ======================================================================== */

/**
 * 报表模版构造函数
 * 
 * @param {String}
 *            reportId : 报表的唯一ID
 * @param {Object}
 *            queryCondition : 报表的查询条件
 * @param {Object}
 *            reportContent : 报表的内容数据
 */
var ReportTemplate = function(reportId, allColumns){
	this.reportId = reportId;
	this.allColumns = allColumns;
	this.queryCondition = null;
	this.reportContent = null;
	// 报表查询条件
	this.queryData = {};
	// 报表列宽数组
	this.colsWidth = [];
}

ReportTemplate.SUMMARYFOLDERID = -999;

/**
 * 初始化报表页面
 */
ReportTemplate.prototype.init = function(){
	var _self = this;
	// 获取报表的查询条件
	_self.fetchConditionAndColumn(function(){
		// 报表按钮区域调整
		_self.adjustButtonView();
		// 查询条件区域构建
		_self.buildQueryConditionView();
		// 查询条件初始化
		_self.queryData = _self.buildQueryData();
		// 报表内容初始化
		initContent();
	});
	
	function initContent(){
		// 隐藏查询条件区域
		$('#queryContainer').hide();
		window.top.showWaitingDlg('@report.waitingBox@', '@report.waitingFor@');
		$.get('/report/initContent.tv', {reportId: reportId, detailReportInitCondition: JSON.stringify(_self.queryData)}, function(reportContent){
			window.top.closeWaitingDlg();
			_self.reportContent = reportContent;
			// 报表内容填充标题、时间、统计条件等
			_self.buildContentConditionView();
			// 输出表头信息
			_self.buildContentHeaderView();
			// add by fanzidong,判断数据条数是否超过限制
			if(reportContent.exceedLimit){
				window.top.showMessageDlg('@report.error@', '@report.exceedlimiterror@');
			}else{
				// 输出报表内容
				_self.buildContentDataView();
			}
			// 绑定事件
			_self.bindEvent();
		});
	}
}

/**
 * 获取报表的查询条件和列信息
 */
ReportTemplate.prototype.fetchConditionAndColumn = function(callback){
	var _self = this;
	$.ajax({
		url: '/report/fetchConditionAndColumn.tv',
		data: {reportId: _self.reportId},
		dataType: 'json',
		success: function(ret){
			_self.queryCondition = ret.queryCondition;
			_self.allColumns = ret.allColumns;
			if(typeof callback === 'function'){
				callback();
			}
		},
		error: function(){},
		cache: false
	})
}

/**
 * 判断该报表是否拥有查询条件
 */
ReportTemplate.prototype.hasQueryCondition = function(){
	return this.queryCondition !== null && this.queryCondition.length !== 0;
}

/**
 * 判断该报表是否拥有时间查询条件
 */
ReportTemplate.prototype.hasTimeQueryCondition = function(){
	if(!this.hasQueryCondition()) return false;
	for(var i=0, len=this.queryCondition.length; i<len; i++){
		if(this.queryCondition[i].type==='timeField'){
			return true;
		}
	}
	return false;
}

/**
 * 根据报表是否拥有查询条件，来自动适配顶部和底部按钮组
 */
ReportTemplate.prototype.adjustButtonView = function(){
	var _self = this;
	new Ext.SplitButton({
		renderTo : "topPutExportBtn",
		text : "@BUTTON.export@",
		handler : function(){this.showMenu()},
		iconCls : "bmenu_exportWithSub",
		menu : new Ext.menu.Menu({
			items : [{text : "EXCEL",   handler : function(){_self.exportExcel()} }]
		})
	});
	new Ext.SplitButton({
		renderTo : "bottomPutExportBtn",
		text : "@BUTTON.export@",
		handler : function(){this.showMenu()},
		iconCls : "bmenu_exportWithSub",
		menu : new Ext.menu.Menu({
			items : [{text : "EXCEL",   handler : function(){_self.exportExcel()} }]
		})
	});
	if(this.hasQueryCondition()){
		// 如果拥有查询条件，则显示选项按钮，隐藏查询按钮
		$('.optionButton').show();
		$('.searchButton').hide();
	}else{
		// 如果没有查询条件，则显示查询按钮，隐藏选项按钮
		$('.optionButton').hide();
		$('.searchButton').show();
	}
}

/**
 * 生成报表查询条件内容
 */
ReportTemplate.prototype.buildQueryConditionView = function(){
	var _self = this;
	if(!this.hasQueryCondition()) return;
	var queryCondition = this.queryCondition;
	// 构造查询条件基本样式
	$('#queryContainer').addClass('zebraTableCaption')
		.append('<div class="zebraTableCaptionTitle"><span>@tip.queryCondition@</span></div>\
				<table class="zebraTableRows"><tbody id="queryTbody"></tbody></table>').show();
	this.buildConditionCoreView(false);
	// 添加查询按钮，有时间查询条件的和没有时间查询条件的不一样
	if(_self.hasTimeQueryCondition()){
		$('#queryTbody').append('<tr><td></td>\
				<td><a href="javascript:;" class="normalBtn normalBtnWithDateTime" id="search_button">\
					<span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>\
				</td></tr>');
		nm3kPickData({
		    startTime : window.startTime,
		    endTime : window.endTime,
		    searchFunction : function(){
		    	_self.loadReportContent();
		    }
		});
	} else {
		$('#queryTbody').append('<tr><td></td><td>\
				<a style="float: left;" href="javascript:;" class="normalBtn" id="search_button">\
					<span><i class="miniIcoSearch"></i>@COMMON.query@</span>\
				</a></td></tr>');
	}
}

ReportTemplate.prototype.buildTaskConditionView = function(){
	var _self = this;
	_self.fetchConditionAndColumn(function(){
		_self.buildConditionCoreView(true);
	})
}

/**
 * 查询条件内容生成核心方法
 * 
 * @param {Boolean}
 *            forTask : 是否是任务调用，任务调用没有时间条件等参数
 */
ReportTemplate.prototype.buildConditionCoreView = function(forTask){
	var _self = this;
	if(!this.hasQueryCondition()) return;
	var queryCondition = this.queryCondition;
	
	// 遍历所有的查询条件，进行输出
	for(var i=0, len=queryCondition.length; i<len; i++){
		var curCondition = queryCondition[i];
		switch(curCondition.type){
		case 'checkboxGroup':
			buildCheckboxGroupCondition(curCondition);
			break;
		case 'radioGroup':
			buildRadioGroupCondition(curCondition);
			break;
		case 'timeField':
			buildTimeFieldCondition(curCondition);
			break;
		case 'singleSelect':
			buildSingleSelectCondition(curCondition);
			break;
		case 'multiSelect':
			buildMultiSelectCondition(curCondition);
			break;
		case 'multiDropdownTree':
			buildMultiDropdownTreeCondition(curCondition);
			break;
		case 'input':
			buildInputCondition(curCondition);
			break;
		case 'textarea':
			buildTextAreaCondition(curCondition);
			break;
		case 'range':
			bulidRangeCondition(curCondition);
			break;
		case 'hiddenGroup':
			bulidhHiddenGroupCondition(curCondition);
			break;
		}
	}
	// 初始化查询条件
	var	queryData = this.buildQueryData();
	this.queryData = queryData;
	
	function buildCheckboxGroupCondition(condition){
		var fieldStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td id="{1}"></td></tr>',
			columnStr = '<span class="columnSpan">\
							<input type="checkbox" id="checkbox_{0}" name="checkbox_{1}" {3} {4}/>\
							<label for="checkbox_{0}">{2}</label>\
						</span>';
		$('#queryTbody').append(String.format(fieldStr, condition.labelName, condition.id));
		var sortColumns = condition.value.split(','),
			curColumnId,
			curColumnDisabled,
			curColumnCheck;
		for(var i=0, iL=sortColumns.length; i<iL; i++){
			curColumnId = sortColumns[i];
			curColumnCheck = false;
			if(curColumnId.indexOf('|')!=-1){
				curColumnId = sortColumns[i].split('|')[0];
				curColumnDisabled = sortColumns[i].split('|')[1]==='disabled'
				curColumnCheck = sortColumns[i].split('|')[2]==='true';
			}
			$('#'+condition.id).append(String.format(columnStr, curColumnId, condition.id, getColumnNameById(curColumnId), curColumnCheck ? 'checked="checked"' : '', curColumnDisabled ? 'disabled="disabled"' : ''));
		}
	}
	
	function buildRadioGroupCondition(condition){
		var fieldStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td id="{1}"></td></tr>',
			columnStr = '<span class="columnSpan">\
							<input type="radio" id="radio_{0}" name="radio_{1}"/>\
							<label for="radio_{0}">{2}</label>\
						</span>';
		$('#queryTbody').append(String.format(fieldStr, condition.labelName, condition.id));
		var sortColumns = condition.value.split(',');
		for(var i=0, iL=sortColumns.length; i<iL; i++){
			$('#'+condition.id).append(String.format(columnStr, sortColumns[i], condition.id, getColumnNameById(sortColumns[i])));
		}
		$('#'+condition.id).find('input').first().attr('checked','checked');
	}
	
	function buildTimeFieldCondition(condition){
		if(forTask) return;
		var fieldStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td><div id="{1}"></div></td></tr>';
		$('#queryTbody').append(String.format(fieldStr, condition.labelName, condition.id));
		// 构建时间控件
		window[condition.id] = new Ext.ux.form.DateTimeField({
			width: 250,
			renderTo: condition.id,
			editable: false,
			emptyText: '@report.plsFullfillDate@',
		    blankText: '@report.plsFullfillDate@'
		});
	}
	
	function buildSingleSelectCondition(condition){
		var moduleStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td><select id="{1}" class="normalSel"></select></td></tr>',
			optionStr = '<option value="{0}">{1}</option>';
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, condition.id));
		$.ajax({
			url: '/report/fetchSelectConditionList.tv',
			data: {reportId: reportId, conditionId: condition.id},
			async: false,
			dataType: 'json',
			success: function(selectModels){
				for(var i=0, len=selectModels.length; i<len; i++){
					$('#'+condition.id).append(String.format(optionStr, selectModels[i].id, ReportTemplate.getI18NString(selectModels[i].name)));
				}
			}
		});
	}
	
	function buildMultiSelectCondition(condition){
		$.ajax({
			url: '/report/fetchSelectConditionList.tv',
			data: {reportId: reportId, conditionId: condition.id},
			async: false,
			dataType: 'json',
			success: function(selectModels){
				var typeList = [];
				for(var i=0, len=selectModels.length; i<len; i++){
					typeList.push([selectModels[i].name, selectModels[i].id]);
				}
				var DeviceType = Ext.data.Record.create([
	      		    {name: 'name', type: 'string'},
	      		    {name: 'id', type: 'int'}
	      		]);
	     		$('#queryTbody').append('<tr><td class="rightBlueTxt">'+condition.labelName+'@report.maohao@</td><td><div id="'+condition.id+'_div"></div></td></tr>');
	     		var deviceTypeStore = new Ext.data.Store({
	     			proxy: new Ext.data.MemoryProxy(typeList),
	     			reader: new Ext.data.ArrayReader({}, DeviceType)
	     		});
	     		deviceTypeStore.load();
	     		var deviceTypeCombo = new Ext.ux.form.LovCombo({
	     	        width: 250,
	     	        id: condition.id,
	     	        hideOnSelect : true,
	     	        editable : false,
	     	        renderTo:condition.id+"_div",  
	     	        store : deviceTypeStore,
	     	        valueField: 'id',
	     			displayField: 'name',
	     	        triggerAction : 'all',  
	     	        mode:'local',
	     	        emptyText : "@Tip.select@",
	     	        showSelectAll : true,
	     	        beforeBlur : function(){}
	     		});
	     		deviceTypeCombo.selectAll();
			}
		});
	}
	
	function buildMultiDropdownTreeCondition(condition){
		var moduleStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td><div id="{1}"></div></td></tr>';
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, condition.id));
		window[condition.id] = $('#'+condition.id).dropdowntree({
	        width: 250
	    }).data('nm3k.dropdowntree');
		window[condition.id].checkAllNodes();
	}
	
	function buildInputCondition(condition){
		var moduleStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td><input type="text" id="{1}" class="normalInput w200"></input></td></tr>';
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, condition.id));
	}
	
	function buildTextAreaCondition(condition) {
		var moduleStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td><textarea type="text" id="{1}" class="queryTextarea" placeHolder="{2}"></textarea></td></tr>';
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, condition.id, condition.placeHolder));
	}
	
	// 时间区域查询条件
	function bulidRangeCondition(condition){
		var moduleStr = '<tr>\
				<td class="rightBlueTxt">{0}@report.maohao@</td>\
				<td><div class="inline-div" id="{1}"></div><div class="inline-div"> - </div><div class="inline-div" id="{2}"></div></td>\
			</tr>',
			startRange = 'start_'+condition.id,
			endRange = 'end_'+condition.id;
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, startRange, endRange));
		// 构建时间控件
		window[startRange] = new Ext.ux.form.DateTimeField({
			width: 150,
			renderTo: startRange,
			editable: false,
			emptyText: '@report.plsFullfillDate@',
		    blankText: '@report.plsFullfillDate@'
		});
		// 构建时间控件
		window[endRange] = new Ext.ux.form.DateTimeField({
			width: 150,
			renderTo: endRange,
			editable: false,
			emptyText: '@report.plsFullfillDate@',
		    blankText: '@report.plsFullfillDate@'
		});
	}
	
	function bulidhHiddenGroupCondition(condition){
		var moduleStr = '<tr><td class="rightBlueTxt">{0}@report.maohao@</td><td id="{1}"></td></tr>',
			radioStr = '<span class="columnSpan">\
				<input type="radio" id="radio_{1}" name="hiddengroup" value="{0}"/>\
				<label for="radio_{1}">{2}</label>\
			</span>';
		$('#queryTbody').append(String.format(moduleStr, condition.labelName, condition.id));
		var hiddengroups = condition.value.split(';');
		for(var i=0; i<hiddengroups.length; i++){
			var txt = hiddengroups[i].split(':')[0],
				val = hiddengroups[i].split(':')[1];
			$('#'+condition.id).append(String.format(radioStr, val, txt, ReportTemplate.getI18NString(txt)));
		}
		$('#radio_'+hiddengroups[0].split(':')[0]).attr('checked', 'checked');
	}
	
	function getColumnNameById(id){
		var columns = _self.allColumns;
		for(var i=0, len=columns.length; i<len; i++){
			if(columns[i].id==id){
				return columns[i].name;
			}
		}
		return '';
	}
}

/**
 * 加载报表的内容数据并输出
 */
ReportTemplate.prototype.loadReportContent = function(){
	var	_self = this,
		queryCondition = _self.queryCondition;
	// 进行校验
	if(!this.validate()) return;
	// 获取所有的查询条件，进行组装
	var	queryData = _self.buildQueryData();
	this.queryData = queryData;
	// 发送 查询请求
	window.top.showWaitingDlg('@report.waitingBox@', '@report.waitingFor@');
	var loadContentPromise = $.get('/report/loadReportContent.tv', {reportId: reportId, detailReportInitCondition: JSON.stringify(queryData)});
	loadContentPromise.done(function(reportContent){
		$('#queryContainer').hide();
		_self.reportContent = reportContent;
		// 清空内容
		emptyReportContent();
		// 报表内容填充标题、时间、统计条件等
		_self.buildContentConditionView();
		// 输出表头信息
		_self.buildContentHeaderView();
		// add by fanzidong,判断数据条数是否超过限制
		if(reportContent.exceedLimit){
			window.top.closeWaitingDlg('@report.waitingBox@');
			window.top.showMessageDlg('@report.error@', '@report.exceedlimiterror@');
		}else{
			// 输出报表内容
			_self.buildContentDataView();
			window.top.closeWaitingDlg('@report.waitingBox@');
		}
	});
	loadContentPromise.fail(function(){
		window.top.closeWaitingDlg('@report.waitingBox@');
		window.top.showMessageDlg('@report.error@', '@report.loadError@');
	});
	
	/**
	 * 清空报表内容
	 */
	function emptyReportContent(){
		$('#reportTitle').empty();
		$('#queryTime').empty();
		$('#staticConditionHd').hide();
		$('#staticConditionContent').empty();
		$('#reportTableHeader').empty();
		$('#reportTableBody').empty();
	}
}

/**
 * 报表查询校验
 */
ReportTemplate.prototype.validate = function(forTask){
	var	_self = this,
		queryCondition = _self.queryCondition;
	
	if(!queryCondition){return true}
	// 如果有时间查询条件，则必须填写的时间正确，且必须开始时间小于结束时间
	if(!forTask && _self.hasTimeQueryCondition()){
		var startTimeValue = startTime.value,
			endTimeValue = endTime.value;
		if(startTimeValue && endTimeValue && startTimeValue >= endTimeValue){
			window.top.showMessageDlg('@report.error@', '@report.timeRule@');
			return false;
		}
	}
	// 遍历各查询条件，根据条件类型进行校验
	var ret = true;
	for(var i=0, len=queryCondition.length; i<len; i++){
		var curCondition = queryCondition[i];
		switch(curCondition.type){
		case 'multiSelect':
			ret = vaildateMultiSelectCondition(curCondition);
			break;
		case 'multiDropdownTree':
			ret = vaildateMultiDropdownTreeCondition(curCondition);
			break;
		case 'range':
			ret = vaildateRangeCondition(curCondition);
			break;
		}
		if(ret === false){
			return false;
		}
	}
	
	function vaildateMultiSelectCondition(curCondition) {
		if(Ext.getCmp(curCondition.id) && !Ext.getCmp(curCondition.id).getCheckedValue()){
			//找到对应的查询条件对应的label
			var label = $('#'+curCondition.id+'_div').parent().prev().text().replace('@COMMON.maohao@', '');
			window.top.showMessageDlg('@report.error@', '@report.pleaseSelectStart@'+label);
    		return false;
		}else{
			return true;
		}
	}
	
	function vaildateMultiDropdownTreeCondition(curCondition) {
		if(window[curCondition.id] && !window[curCondition.id].getSelectedIds().length){
			window.top.showMessageDlg('@report.error@', '@report.pleaseSelectFolder@');
    		return false;
		}else{
			return true;
		}
	}
	
	function vaildateRangeCondition(curCondition) {
		var rangeStart = 'start_'+curCondition.id,
			rangeEnd = 'end_'+curCondition.id;
		if($('#'+rangeStart) && $('#'+rangeEnd)){
			var startTimeValue = window[rangeStart].value;
			var endTimeValue = window[rangeEnd].value;
			if(startTimeValue && endTimeValue && startTimeValue >= endTimeValue){
				window.top.showMessageDlg('@report.error@', '@report.timeRule@');
				return false;
			}
		}
		return true;
	}
	
	return true;
}

/**
 * 报表内容 标题、时间、统计条件等信息输出
 */
ReportTemplate.prototype.buildContentConditionView = function(){
	var _self = this,
		reportContent = this.reportContent,
		queryCondition = this.queryCondition,
		outputStr = '<div class="conditionDiv">{0}@report.maohao@{1}</div>';
	// 填入报表标题
	$('#reportTitle').text(reportContent.title);
	// 填入生成时间
	$('#queryTime').text(reportContent.time);
	// 如果存在统计条件，则输出
	var queryData = this.queryData;
	if(queryData){
		for(var i=0, len=queryCondition.length; i<len; i++){
			var curCondition = queryCondition[i];
			// 查询条件及显示（Display）均存在的才被认为需要输出
			if(queryData[curCondition.id] && queryData[curCondition.id+'Display']){
				$('#staticConditionHd').show();
				$('#staticConditionContent').append(String.format(outputStr, curCondition.labelName, queryData[curCondition.id+'Display']));
			}
		}
	}
	// 如果存在描述信息，则输出
	if(reportContent.description && reportContent.description.length){
		var curDesc;
		for(var di=0, dlen=reportContent.description.length; di<dlen; di++){
			curDesc = reportContent.description[di];
			$('#'+curDesc.position).html(curDesc.text);
		}
	}
	
	/**
	 * 国际化描述信息
	 */
	function i18NDescInfo(text){
		var iStartIndex = text.indexOf('@');
		if(iStartIndex!=-1){
			//只国际化@@中间的部分
			var regRex = /@([\w\.]+)@/g,
				matchArray = text.match(regRex);
			for(var i=0, len=matchArray.length; i<len; i++){
				var part = matchArray[i].substring(1, matchArray[i].length-1);
				text = text.replace(matchArray[i], ReportTemplate.getI18NString(part));
			}
			return text;
		}else{
			//直接当作一个完整的key，做国际化，返回
			return ReportTemplate.getI18NString(text);
		}
	}
}

/**
 * 生成报表的表头
 */
ReportTemplate.prototype.buildContentHeaderView = function(){
	var _self = this,
		reportContent = this.reportContent,
		contentHeader = reportContent.contentHeader,
		columns = contentHeader.columns,
		headerTr = $('<tr></tr>');
	if(reportContent.combination){
		// 该报表的表头是复合表头
		var prevComb = '', curComb = '', firstTr = $('<tr></tr>'), secTr = $('<tr></tr>'), count=0;
		for(var i=0; i<columns.length; i++){
			var currentCol = columns[i];
			prevComb = curComb;
			curComb = currentCol.combination;
			if(!curComb){ // 该行没有父级表头
				// 判断前面是否有需要合并的表头
				if(prevComb){
					// 需要结束表头合并，并放入第一行
					firstTr.append(String.format('<th colspan="{0}">{1}</th>', count, prevComb));
					count=0;
				}
				// 该行没有父级表头，放入第一行
				if(columns[i].nowrap){
					firstTr.append(String.format('<th rowspan="2" width="{1}">{0}</th>', columns[i].name, columns[i].width));
				}else{
					firstTr.append(String.format('<th rowspan="2">{0}</th>', columns[i].name));
				}
			}else{ // 该行有父级表头
				// 判断前面是否有表头需要合并
				if(prevComb && curComb!=prevComb){
					// 将前面的表头合并放入第一行
					firstTr.append(String.format('<th colspan="{0}">{1}</th>', count, prevComb));
					count=0;
					prevComb = curComb;
				}
				count++;
				// 如果已经是最后一列，则直接输出第一列
				if(i == columns.length-1){
					firstTr.append(String.format('<th colspan="{0}">{1}</th>', count, curComb));
					count=0;
				}
				// 直接加入第二行
				if(columns[i].nowrap){
					secTr.append(String.format('<th width="{1}">{0}</th>', columns[i].name, columns[i].width));
				}else{
					secTr.append(String.format('<th>{0}</th>', columns[i].name));
				}
			}
		}
		firstTr.appendTo($('#reportTableHeader'));
		secTr.appendTo($('#reportTableHeader'));
	}else{
		// 该报表的表头是单行表头
		for(var i=0; i<columns.length; i++){
			if(columns[i].nowrap){
				headerTr.append(String.format('<th width="{1}">{0}</th>', columns[i].name, columns[i].width));
			}else{
				headerTr.append(String.format('<th>{0}</th>', columns[i].name));
			}
		}
		headerTr.appendTo($('#reportTableHeader'));
	}
	// 调整表头宽度
	var headerTrs = $('#reportTableHeader').find('tr'), curTh, k=0;
	for(var i=0; i<headerTrs.length; i++){
		var curTrThs = $(headerTrs[i]).find('th');
		for(var j=0; j<curTrThs.length; j++){
			curTh = curTrThs[j];
			if(curTh.colSpan==1){
				if(!curTh.width){
					curTh.width = $(curTh).outerWidth();
				}
				_self.colsWidth[k++] = curTh.width;
			}
		}
	}
	queryCloseHeaderTop = $('#reportTableHeader').position().top;
	queryOpenHeaderTop = queryCloseHeaderTop + $('#queryContainer').outerHeight();
	headerTop = queryCloseHeaderTop;
	if(!$.browser.msie || parseInt($.browser.version)>=9){ // just use when browser support it
		$('#reportTableHeader').smartFixed();
	}
	$('#reportTableHeader').width($('#reportTableHeader').outerWidth()+2);
}

/**
 * 输出报表内容数据
 */
ReportTemplate.prototype.buildContentDataView = function(){
	var _self = this,
		documentFragment = document.createDocumentFragment(),
		columns =  this.reportContent.contentHeader.columns,
		contentData = this.reportContent.contentData;
	// 进行递归输出
	outputContent(contentData, 0, 0, [], true);
	// 将结果输出到页面上
	document.getElementById("reportTableBody").appendChild(documentFragment); 
	
	function outputContent(reportData, depth, startTrNum, summaryList, firstLine){
		var contentLineNum = 0,
			isLeaf = reportData.leaf || false;
		if (isLeaf) {
            // 叶子节点直接输出
            outputLine(columns, reportData, summaryList, firstLine);
            firstLine = false;
            return 1;
        }
		// 获取其子节点
		var childrens = reportData.childrens;
		if (childrens == null || childrens.length == 0) {
            // 子节点为空则返回
            return 0;
        }
		// 获取该节点是否需要统计
		if(reportData.groups){
			reportData.groups.sumedKeys = [];
			reportData.groups.parentKeyList = {};
			// 填写改层统计应有的keyList
			// 继承该层父属性
			if(reportData.parentKeyList){
				for(var key in reportData.parentKeyList){
					reportData.groups.parentKeyList[key] = reportData.parentKeyList[key];
				}
			}
			if(reportData.key !== 'ReportRoot'){
				reportData.groups.parentKeyList[reportData.key] = reportData.value;
			}
			summaryList.push(reportData.groups);
		}
		// 输出各子节点内容
		for (var i = 0, iL=childrens.length; i < iL; i++) {
			var curChild = childrens[i];
			// 将该层级的key-value赋予其子节点
			curChild.parentKeyList = {};
			if(reportData.parentKeyList){
				for(var key in reportData.parentKeyList){
					curChild.parentKeyList[key] = reportData.parentKeyList[key];
				}
			}
			if(reportData.key !== 'ReportRoot'){
				curChild.parentKeyList[reportData.key] = reportData.value;
			}
			var currentLineNum = outputContent(curChild, depth + 1, startTrNum + contentLineNum, summaryList, firstLine);
            contentLineNum += currentLineNum;
            // 输出叶子节点后，对本节点的相关数据进行统计
        }
		// 如果该节点有小计，则输出小计
		// modify by fanzidong，如果该节点value为null并且不是总计，表示无需统计
		var summary = false;
		if(reportData.groups) {
			if(reportData.value || reportData.key=="root") {
				summary = true;
				outputSummary(columns, summaryList.pop());
			} else {
				summaryList.pop();
			}
		}
		// 合并该depth对应列的行
        if (depth > 0 && contentLineNum>1) {
            for (var j = columns.length-1; j >= 0; j--) {
                var column = columns[j],
                	columnDepth = column.level;
                if (columnDepth == depth) {
                	// 合并startTrNum行到startTrNum+ contentLineNum - 1行的第j列
                	mergeCells(startTrNum, startTrNum+ contentLineNum - 1, j);
                }
            }
        }
        if (summary) {
            contentLineNum++;
        }
        return contentLineNum;
	}
	
	function outputLine(columns, reportData, summaryList, firstLine){
		// 遍历每一列，找出属于当前深度的列，进行输出
		var tr = document.createElement('tr');
		var isSummary = false;
		if(reportData.folderId && reportData.folderId == ReportTemplate.SUMMARYFOLDERID){
			isSummary = true;
		}
		for(var i=0; i<columns.length; i++){
			var td = document.createElement('td');  
			if(isSummary){
				td.className = 'summaryTd';
			}
			if(firstLine){
				// 如果是首行，需要设置宽度
				td.width = _self.colsWidth[i];
			}
			// 判断该cell输出是否需要加上链接
			if(columns[i].linkId && columns[i].linkId !== "" 
				&& reportData[columns[i].id] && reportData[columns[i].id] != 0){
				var aLink = document.createElement('a');
				aLink.className = 'reportContentLink';
				aLink.href = 'javascript:;';
				aLink.setAttribute('data-detail', 'reportId:' + columns[i].linkId + ';linkName:'+columns[i].linkName);
				for(var key in reportData.parentKeyList){
					aLink.setAttribute('data-detail', aLink.getAttribute('data-detail') + ';' + key + ':' + reportData.parentKeyList[key]);
				}
				aLink.setAttribute('data-detail', aLink.getAttribute('data-detail') + ';' + reportData.key + ':' + reportData.value);
				aLink.setAttribute('data-detail', aLink.getAttribute('data-detail') + ';columnCondition:' + columns[i].id);
				var innerIext = reportData[columns[i].id] || '-';
				aLink.appendChild(document.createTextNode(innerIext)); 
				td.appendChild(aLink);
			}else{
				var innerIext = reportData[columns[i].id] || '-';
				td.appendChild(document.createTextNode(innerIext)); 
			}
			tr.appendChild(td);
		}
		documentFragment.appendChild(tr);
		// 进行统计
		if(summaryList && summaryList.length>0){
			for(var i=0; i<summaryList.length; i++){
				// 获取当前统计元素需要统计的元素
				var currentSummary = summaryList[i],
					countColumns = currentSummary.groups;
				// 判断该行是否已经统计过
				if(contains(currentSummary.sumedKeys, reportData.groupKey)){
					continue;
				}
				currentSummary.sumedKeys.push(reportData.groupKey)
				for(var j=0; j<countColumns.length; j++){
					var	curCol = countColumns[j],
						curColName = curCol.groupColumn,
						curColCompute = curCol.compute;
					if(currentSummary[curColName] === undefined){
						currentSummary[curColName] = 0;
					}
					if(reportData[curColName]){
						if(curColCompute.toLowerCase()==='avg'){// TODO
							if(curCol.count === undefined){
								curCol.count = 0;
							}
							currentSummary[curColName] = (currentSummary[curColName] * curCol.count + parseFloat(reportData[curColName]))/(curCol.count+1) ;
							curCol.count++;
						}else if(curColCompute.toLowerCase()==='rowcount'){
							currentSummary[curColName]++; 
						}else{
							currentSummary[curColName] += parseFloat(reportData[curColName]);
						}
					}
				}
			}
		}
	}
	
	function outputSummary(columns, summaryData){
		// 填充该summary对应填入的总计列及对应文字
		summaryData[summaryData.relative] = summaryData.displayName;
		// 遍历每一列，找出属于当前深度的列，进行输出
		var tr = document.createElement('tr');
		for(var i=0; i<columns.length; i++){
			var td = document.createElement('td');  
			td.className = 'summaryTd';
			// 判断该cell输出是否需要加上链接
			var innerIext = summaryData[columns[i].id]!== undefined ? summaryData[columns[i].id] : '-';
			if(typeof innerIext != 'string' && innerIext.toString().indexOf('.')!=-1){
				try{
					innerIext = parseFloat(innerIext).toFixed(1);
				}catch(e){
				}
			}
			if(summaryData.link && columns[i].linkId && innerIext!='-' && innerIext!=0 && isColumnInGroup(columns[i].id)){
				var aLink = document.createElement('a');
				aLink.className = 'reportContentLink';
				aLink.href = 'javascript:;';
				aLink.setAttribute('data-detail', 'reportId:' + columns[i].linkId + ';linkName:'+columns[i].linkName);
				for(var key in summaryData.parentKeyList){
					aLink.setAttribute('data-detail', aLink.getAttribute('data-detail') + ';' + key + ':' + summaryData.parentKeyList[key]);
				}
				aLink.setAttribute('data-detail', aLink.getAttribute('data-detail') + ';columnCondition:' + columns[i].id);
				aLink.appendChild(document.createTextNode(innerIext)); 
				td.appendChild(aLink);
			}else{
				td.appendChild(document.createTextNode(innerIext)); 
			}
			tr.appendChild(td);
		}
		documentFragment.appendChild(tr);
		
		function isColumnInGroup(columnId){
			var groups = summaryData.groups;
			for(var k=0; k<groups.length; k++){
				if(groups[k].groupColumn === columnId){
					return true;
				}
			}
			return false;
		}
	}
	
	function transferData(value, needLink){
		if(needLink){
			return String.format('<a href="#">{0}</a>', value);
		}else{
			return value;
		}
	}
	
	function mergeCells(startTr, endTr, columnNum){
		var currentRow,
			toBeDelTd,
			startRow,
			startTd;
		// 将第startTr+1行至第endTr行的第columnNum个td删掉
		for(var i=startTr+1; i<=endTr; i++){
			currentRow = documentFragment.childNodes[i];
			toBeDelTd = currentRow.childNodes[columnNum];
			currentRow.removeChild(toBeDelTd);
		}
		// 将第startTr行tr的第columnNum个td的rowspan设置为endTr-startTr+1
		startTd = documentFragment.childNodes[startTr].childNodes[columnNum];
		startTd.rowSpan =  endTr - startTr + 1;
	}
	
	function contains(array, obj){
		var i = array.length;
		while(i--){
			if(array[i] == obj){
				return true;
			}
		}
		return false;
	}
}

/**
 * 为报表元素绑定事件
 */
ReportTemplate.prototype.bindEvent = function(){
	var _self = this,
		queryCondition = this.queryCondition;
	
	// 导出excel事件绑定
	$('.exportExcelButton').bind('click', function(e){
		_self.exportExcel();
	});
	
	// 查询事件绑定
	$('#searchTopButton,#search_button').bind('click', function(e){
		e.stopPropagation();
		//add by fanzidong，此时应回滚至顶端
		$(window).scrollTop(0);
		_self.loadReportContent();
	});
	//searchBottomButton
	$('#searchBottomButton').bind('click', function(e){
		$(window).scrollTop(0);
		e.stopPropagation();
		//add by fanzidong，此时应回滚至顶端
		$(window).scrollTop(0);
		_self.loadReportContent();
	});
	
	// 链接跳转事件绑定
	$('#reportTable').bind('click', function(e){
		var target = e.target;
		if(target.className!=="reportContentLink"){
			return;
		}
		// 获取出链接查询数据(we can use element.data() to get object instead)
		var attributes = target.getAttribute('data-detail').split(';'),
			map = {};
		for(var i=0; i<attributes.length; i++){
			map[attributes[i].split(':')[0]] = attributes[i].split(':')[1];
		}
		var queryData = _self.buildQueryData(map);
		window.parent.addView("report-" + encodeURI(JSON.stringify(queryData)), queryData.linkName, "reportIcon", '/report/showSingleReport.tv?reportId='+queryData.reportId+'&detailReportInitCondition='+encodeURI(JSON.stringify(queryData)));
	});
}

/**
 * 导出excel
 */
ReportTemplate.prototype.exportExcel = function(){
	var	_self = this,
		downloaded = false,
		queryData = _self.queryData;
	// 进行校验
	if(!this.validate()) return;
	window.top.showWaitingDlg('@report.waitingBox@', '@report.exporting@');
	$.ajax({
		url: '/report/generateExcelFile.tv',
		async: true,
		type: 'POST',
    	dataType:"json",
		cache: false,
		data: {
			reportId: reportId,
			detailReportInitCondition: JSON.stringify(queryData)
		},
		success: function(response) {
			window.top.closeWaitingDlg('@report.waitingBox@');
			if(response.error){
				window.top.showMessageDlg('@report.error@', '@report.exportError@');
			}else{
				// 发送下载请求
				document.getElementById('download_iframe').src = "/report/downloadGenerateFile.tv?fileName="+encodeURIComponent(response.fileName);
			}
   		}, 
   		error: function() {
   			window.top.closeWaitingDlg('@report.waitingBox@');
   			window.top.showMessageDlg('@report.error@', '@report.exportError@');
		}, 
		complete: function (XHR, TS) { XHR = null }
	});
	
}

/**
 * 组装查询条件，包含默认条件和查询框中的内容 params: extraQueryData 额外添加的查询条件
 */
ReportTemplate.prototype.buildQueryData = function(extraQueryData, forTask){
	// 获取所有的查询条件，进行组装
	var	_self = this,
		queryCondition = this.queryCondition,
		queryData = {reportId: reportId};
	if(detailReportInitCondition){
		for(var key in detailReportInitCondition){
			queryData[key] = detailReportInitCondition[key];
		}
	}
	if(_self.hasQueryCondition()){
		// 遍历当前的查询条件的值，进行组装
		for(var i=0, len=queryCondition.length; i<len; i++){
			var curCondition = queryCondition[i];
			switch(curCondition.type){
			case 'checkboxGroup':
				buildCheckboxGroupData(queryData, curCondition);
				break;
			case 'radioGroup':
				buildRadioGroupData(queryData, curCondition);
				break;
			case 'timeField':
				buildTimeFieldData(queryData, curCondition);
				break;
			case 'singleSelect':
				buildSingleSelectData(queryData, curCondition);
				break;
			case 'multiSelect':
				buildMultiSelectData(queryData, curCondition);
				break;
			case 'multiDropdownTree':
				buildMultiDropdownTreeData(queryData, curCondition);
				break;
			case 'input':
				buildInputData(queryData, curCondition);
				break;
			case 'textarea':
				buildTextareaData(queryData, curCondition);
				break;
			case 'range':
				buildRangeData(queryData, curCondition);
				break;
			case 'hiddenGroup':
				buildHiddenGroupData(queryData, curCondition);
				break;
			}
		}
	}
	// 整合额外的查询参数
	for(var key in extraQueryData){
		queryData[key] = extraQueryData[key];
	}
	return queryData;
	
	function buildCheckboxGroupData(queryData, curCondition){
		var selectedCheckboxs = $('#'+curCondition.id).find('input:checked');
		if(selectedCheckboxs && selectedCheckboxs.length){
			var hiddenColumns = [];
			for(var i=0, len = selectedCheckboxs.length; i<len; i++){
				hiddenColumns.push(selectedCheckboxs[i].id.split('_')[1]);
			}
			queryData[curCondition.id] = hiddenColumns.join(',');
		}
	}
	
	function buildRadioGroupData(queryData, curCondition){
		var checkedRadioId = $('#'+curCondition.id).find('input:checked').attr('id');
		if(checkedRadioId){
			queryData[curCondition.id] = checkedRadioId.split('_')[1];
			queryData[curCondition.id+'Display'] = $('#'+curCondition.id).find('input:checked').next('label').text();
		}
	}
		
	function buildTimeFieldData(queryData, curCondition){
		if(forTask) return;
		if(window[curCondition.id]){
			queryData[curCondition.id] = window[curCondition.id].value;
			queryData[curCondition.id+'Display'] = window[curCondition.id].value;
		}
	}
	
	function buildSingleSelectData(queryData, curCondition){
		if($('#'+curCondition.id).val()!=0 && $('#'+curCondition.id).val()!=-1){
			queryData[curCondition.id] = $('#'+curCondition.id).val();
			queryData[curCondition.id+'Display']= document.getElementById(curCondition.id).options[document.getElementById(curCondition.id).selectedIndex].text;
		}
	}
	
	function buildMultiSelectData(queryData, curCondition){
		if(Ext.getCmp(curCondition.id)){
			queryData[curCondition.id] = Ext.getCmp(curCondition.id).getCheckedValue();
			queryData[curCondition.id+'Display']= Ext.getCmp(curCondition.id).getCheckedDisplay();
		}
	}
	
	function buildMultiDropdownTreeData(queryData, curCondition) {
		if(window[curCondition.id]){
			queryData[curCondition.id] = window[curCondition.id].getSelectedIds().join(',');
			queryData[curCondition.id+'Display'] = window[curCondition.id].getSelectedNames().join(',');
		}
	}
	
	function buildInputData(queryData, curCondition) {
		if($('#'+curCondition.id)){
			queryData[curCondition.id] = $('#'+curCondition.id).val();
			queryData[curCondition.id+'Display'] = $('#'+curCondition.id).val();
		}
	}
	
	function buildTextareaData() {
		if($('#'+curCondition.id)){
			var content = $.trim($('#'+curCondition.id).val());
			var array = content.split(/[\n\s]+/);
			
			// 针对每一个查询条件，如果是完整的MAC地址或者模糊的MAC地址，尝试进行转换
			var formatArray = array.map(function(mac) {
				return top.MacUtil.formatQueryMac(mac);
			});
			
			queryData[curCondition.id] = formatArray.join('|');
			queryData[curCondition.id+'Display'] = array.join('     ');
		}
	}
	
	function buildRangeData(queryData, curCondition){
		// 查看开始和结束是否存在
		var rangeStart = 'start_'+curCondition.id,
			rangeEnd = 'end_'+curCondition.id;
		if($('#'+rangeStart) && $('#'+rangeEnd)){
			if(!window[rangeStart].value && !window[rangeEnd].value){
				return
			}
			queryData[curCondition.id] = 'has condition';
			queryData[curCondition.id+'Display'] = (window[rangeStart].value || '@report.noselect@') + ' - ' + (window[rangeEnd].value || '@report.noselect@');
			if(window[rangeStart].value){
				queryData[rangeStart] = queryData[rangeStart+'Display'] = window[rangeStart].value;
			}
			if(window[rangeEnd].value){
				queryData[rangeEnd] = queryData[rangeEnd+'Display'] = window[rangeEnd].value;
			}
		}
	}
	
	function buildHiddenGroupData(queryData, curCondition){
		var checkedRadio = $('#'+curCondition.id).find('input:checked');
		if(checkedRadio){
			var radioValue = checkedRadio.attr('value');
			queryData[curCondition.id] = queryData.hiddenColumns = radioValue;
			queryData[curCondition.id+'Display'] = checkedRadio.next('label').text();
		}
	}
}

/**
 * 国际化(做了缓存处理)
 */
ReportTemplate.getI18NString = function(key){
	var parts = key.split('\.'), 
		resultStr,
		queryedMap = {};
	if(queryedMap[key]){
		return queryedMap[key];
	}
	for(var i=0; i<parts.length; i++){
		if(resultStr){
			resultStr = resultStr[parts[i]];
		}else{
			resultStr = I18N[parts[0]];
		}
	}
	if(resultStr){
		queryedMap[key] = resultStr;
	}
	return resultStr || key;
}