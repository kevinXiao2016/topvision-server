//重写多选框的onMonuseDown, onHdMouseDown方法
//选择行多选框
function onMonuseDown(e, t){
    if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if
                                                                // left-click
        e.stopEvent();
        var row = e.getTarget('.x-grid3-row');

        // mouseHandled flag check for a duplicate selection (handleMouseDown)
        // call
        if(!this.mouseHandled && row){
            // alert(this.grid.store.getCount());
            var gridEl = this.grid.getEl();// 得到表格的EL对象
            var hd = gridEl.select('div.x-grid3-hd-checker');// 得到表格头部的全选CheckBox框
            var index = row.rowIndex;
            if(this.isSelected(index)){
                this.deselectRow(index);
                var isChecked = hd.hasClass('x-grid3-hd-checker-on');
                // 判断头部的全选CheckBox框是否选中，如果是，则删除
                if(isChecked){
                    hd.removeClass('x-grid3-hd-checker-on');
                }
            }else{
                this.selectRow(index, true);
                // 判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
                if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
                    hd.addClass('x-grid3-hd-checker-on');
                };
            }
        }
    }
    this.mouseHandled = false;
}
//选择表头多选框
function onHdMouseDown(e, t){
    if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
        e.stopEvent();
        var hd = Ext.fly(t.parentNode);
        var isChecked = hd.hasClass('x-grid3-hd-checker-on');
        if(isChecked){
            hd.removeClass('x-grid3-hd-checker-on');
            this.clearSelections();
        }else{
            hd.addClass('x-grid3-hd-checker-on');
            this.selectAll();
        }
    }
}
Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
    onHdMouseDown : onHdMouseDown
});