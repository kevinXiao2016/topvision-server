/**
 * 根据Col id获取ColIndex
 * @param id
 * @returns
 */
function getColIndex(grid, id){
	if(grid){
		return grid.getColumnModel().getIndexById(id);
	}
}

/**
 * 设置某列是否隐藏
 * @param grid
 * @param colIndex
 * @param hidden
 */
function setColHidden(grid, colIndex, hidden){
	if(grid){
		grid.getColumnModel().setHidden(colIndex,hidden);
	}
}

/**
 * 获取单选行行号 
 */
function getSelectedRowIndex(grid){
	var rowIndex = -1;
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		rowIndex = grid.getStore().indexOf(record);
	}
	return rowIndex;
}

/**
 * 获取多选行行号数组
 * @param grid
 */
function getSelectedRowIndexs(grid){
	var rowIndexs = [];
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var records = sm.getSelections();
		for(var i=0; i<records.length; i++){
			rowIndexs[i] = grid.getStore().indexOf(records[i]);
		}
	}
	return rowIndexs;
}