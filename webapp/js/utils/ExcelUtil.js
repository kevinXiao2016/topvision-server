/**
* # 描述
* 这是NM3000通用EXCEL工具类，包含了诸如将gridpanel导出为excel等方法，如有遗漏，请在此添加
*
* **使用范例**：
*
*     @example
*     ExcelUtil.exportGridToExcel(gridPanel, 'CPU使用率TOP10');
* @class IpUtil
*/
var ExcelUtil = {
	/**
	* 将gridpanel导出为excel
	* @param {Object} gridPanel 包含数据的表格控件
	* @param {String} fileTitle 下载后excel文件名
	* @param {Object} options 一些配置项，可配置
	*        currentPage: 是否仅仅当前页
	*        allColumn: 是否导出所有字段
	* @return
	*/
	exportGridToExcel: function(gridPanel, fileTitle, options){
		// 获取store的url和params
		var store = gridPanel.getStore(),
			url = store.proxy.url,
			baseParams = store.baseParams;
		
		// 拼接出要发送给后台的url
		if(url.indexOf('?') !== -1) {
			url += '&_dt=' + (new Date()).getTime();
		} else {
			url += '?_dt=' + (new Date()).getTime();
		}
		//store.totalLength
		//if(store.totalLength > 10) {
			//return window.top.showMessageDlg('@COMMON.tip@', '@COMMON.exportRowLimit@');
		//}
		
		//TODO 根据rowCount，提示无法下载
		if(!options || !options.currentPage) {
			// 导出全部数据
			// 删除分页参数会导致系统传递默认分页参数，我们将个数的上限设置为1200W，即最多200个sheet * 6W行/sheet
			baseParams.start = 0;
			//baseParams.limit = 600000;
			baseParams.limit = 12000000;
		}
		for(var key in baseParams) {
			url += '&' + key + '=' + baseParams[key];
		}
		
		// 获取要展示的列
		var cm = gridPanel.getColumnModel(),
			curColumn, columnId = '', columnTitle = '', columnWidth = 0, columnIndex = '';
		
		var columns = [];
		for (var i = 0; i < cm.getColumnCount(); i++) {
			// 只需要非checkbox列，非操作列，非隐藏列
			curColumn = cm.columns[i];
			if((!options || !options.allColumn) && cm.isHidden(i)) {
				continue;
			}
			if(curColumn.exportable === false || curColumn.id === 'checker' || curColumn.constructor.ptype === 'rowexpander') {
				continue;
			}
			
			columnTitle = this.getTextFromColumnHeader(cm.getColumnHeader(i));
			columnWidth = cm.getColumnWidth(i);
			columnIndex = getCorrectDataIndex(curColumn);
			
			var column = {
				index: columnIndex,
				title: columnTitle,
				width: columnWidth
			};
			if (cm.config[i].exportRenderer) {
				column.renderer = cm.config[i].exportRenderer.toString();
				column.rendererName = cm.config[i].exportRenderer.name;
				//column.renderer = 'function percentRendererIn100(v) {return v + "%"}';
			}
			columns.push(column);
		}
		
		var random = randomInteger(0, 100);
		var operationId = 'exportGridToExcel_' + random;
		//创建回调
		window.top.addCallback(operationId, function(response) {
			top.closeWaitingDlg();
			if(!response.complete) {
				window.top.showWaitingDlg('@COMMON.wait@', response.text, 'ext-mb-waiting');
			}
		});
		
		// 发送请求
		window.top.showWaitingDlg('@COMMON.wait@', '@COMMON.querying@', 'ext-mb-waiting');
		$.ajax({
			url: '/export/exportGridToExcel.tv',
			data: {
				jconnectedId: WIN.top.GLOBAL_SOCKET_CONNECT_ID,
				operationId: operationId,
				url: encodeURI(url),
				fileName: fileTitle,
				columnsStr: JSON.stringify(columns),
				rootProperty: store.root
			},
			timeout: 300000,
	    	type: 'POST',
	    	async: true,
	    	dataType:"json",
	   		success: function(response) {
                top.closeWaitingDlg();
	   			document.getElementById('main_download_iframe').src = "/export/downloadGenerateFile.tv?fileName="+encodeURIComponent(response.fileName);
	   		}, error: function(json) {
	   			top.closeWaitingDlg();
	   			window.top.showMessageDlg('@COMMON.tip@', '@COMMON.exportFailed@');
			}, 
			cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
		
		function getCorrectDataIndex(column) {
			if(column.exportDataIndex) {
				return column.exportDataIndex;
			}
			var map = store.fields.map[column.dataIndex];
			return map.mapping || map.name;
		}
		
		function randomInteger(min, max) {
			var r = Math.random() * (max - min);
		    var re = Math.round(r + min);
		    re = Math.max(Math.min(re, max), min)
		    return re;
		}
	},
	/**
	 * 从column的header中获取有效的文本信息
	 */
	getTextFromColumnHeader: function(header) {
		var reg = /<\s*.*>(\s*.*)<\/\s*.*>/;
		var matches = reg.exec(header);
		if(matches && matches.length > 1) {
			return matches[1];
		} else {
			return header;
		}
	}
};