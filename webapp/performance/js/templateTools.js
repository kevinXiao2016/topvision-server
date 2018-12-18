function setGridSize(){
	var h = $(window).height() - 90;
	if(h<400) h = 400;
	var w = $(document.body).width() -40;
	grid.setSize(w, h);
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
                       		]});
   return pagingToolbar;
}

function onRefreshClick() {
	var name = $("#name").val();
	var ip = $("#ip").val();
	store.load({params: {entityName: name,entityIp: ip ,start:0,limit:pageSize}});
}

