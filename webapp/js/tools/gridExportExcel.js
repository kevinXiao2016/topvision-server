function exportGridToExcel(gridPanel, fileTitle){
	if(gridPanel){
		//构建临时store，用于获取不分页的数据
		var tmpStore = gridPanel.getStore();
		var tmpParam = $.extend({}, {params: tmpStore.baseParams}, tmpStore.lastOptions);
		if (tmpParam && tmpParam.params) {
			//删除分页参数会导致系统传递默认分页参数，我们将分页的start和limit设置为-1
			tmpParam.params.start = -1;
			tmpParam.params.limit = -1;
            //delete (tmpParam.params[tmpStore.paramNames.start]); 
            //delete (tmpParam.params[tmpStore.paramNames.limit]);
        }
		//重新定义一个数据源
		var tmpAllStore = new Ext.data.GroupingStore({
            proxy: tmpStore.proxy,
            reader: tmpStore.reader
        });
		//获取数据后对数据进行处理
		tmpAllStore.on('load', function (store) {
			//用于传递给后端处理的数据
			var excelData = {
				columns:{},
				columnSort:[],
				data: []
			};
			//整理出需要导出的列
			var cm = gridPanel.getColumnModel(),
				columnTitle = '', columnWidth = 0, columnIndex = '';
			for (var i = 0; i < cm.getColumnCount(); i++) {
				if(!cm.isHidden(i)){//如果该列未隐藏，则统计
					//获取该列的头部
					columnIndex = cm.getDataIndex(i);
					columnTitle = cm.getColumnHeader(i);
					//获取该列的宽度
					columnWidth = cm.getColumnWidth(i);
					//加入
					excelData.columns[columnIndex] = {
						title: columnTitle,
						width: columnWidth	
					}
					excelData.columnSort.push(columnIndex);
				}
			}
			//整理需要导出的数据
			for (var i = 0, it = store.data.items, l = it.length; i < l; i++) {
				//初始化需要整理的该行数据
				var arrangedRecord = {};
				var record = it[i].data;
				for (var j = 0; j < cm.getColumnCount(); j++) {
					if(!cm.isHidden(j)){//如果该列未隐藏，则统计
						//获得该行该列的值
						var value = record[cm.getDataIndex(j)];
						//如果该列有renderer函数，则需要获取renderer后的值
						if (typeof cm.config[j].renderer === 'function') {
	                        var tmpvalue = cm.config[j].renderer(value, {}, it[i], i, j, store);
	                        if(tmpvalue){
	                        	tmpvalue = tmpvalue.toString();
	                        	if(tmpvalue.indexOf('<img')!==-1){//如果含有图片，则截取其提示
	                        		var tipReg = /nm3kTip=\"([\u4e00-\u9fa5\w]+)\"/;
	                        		var matched = tipReg.exec(tmpvalue);
	                        		if(matched && matched[1]){
	                        			value = matched[1];
	                        		}
	                        	}else{
	                        		var re = /<[^>]+>/g;
	                        		tmpvalue = tmpvalue.toString().replace(re, '');
	                        		if(tmpvalue!==''){
		                        		value=tmpvalue;
		                        	}
	                        	}
	                        }
						}
						//将该列加入arrangedRecord
						columnIndex = cm.getDataIndex(j);
						arrangedRecord[columnIndex] = value;
					}
				}
				//该行数据加入数组中
				excelData.data.push(arrangedRecord);
			}
			//将整理后的数据传递给后端进行处理
			$.post('/system/exportGridToExcel.tv',{
				excelData: JSON.stringify(excelData),
				exportFile: fileTitle
			}, function(response){
				if(response.error){
					
				}else{
					var filePath = response.filePath;
					window.location.href = encodeURI('/system/downloadExcel.tv?filePath=' + filePath);
				}
				
			});
		});
		//加载数据
		tmpAllStore.load(tmpParam);
	}
}