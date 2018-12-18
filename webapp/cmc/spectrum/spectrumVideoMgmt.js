Ext.onReady(function(){
    bulidToolbar();
    bulidVideoGrid();
    
    new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        contentEl: 'queryDiv',
	        height: 63,
	        border: false,
	        margins: "10px 0px 10px 0px",
	        cls:'clear-x-panel-body'
	    },grid]
    });
})

function bulidToolbar(){
    startTime = new Ext.ux.form.DateTimeField({
		width:150,
		value:'${startTime}',
	    renderTo: 'startTime',
	    editable: false,
		emptyText:'@spectrum.pleaseInputDate@',
	    blankText:'@spectrum.pleaseInputDate@'
	});
    
    endTime = new Ext.ux.form.DateTimeField({
		width:150,
		value:'${endTime}',
	    renderTo: 'endTime',
	    editable: false,
		emptyText:'@spectrum.pleaseInputDate@',
	    blankText:'@spectrum.pleaseInputDate@'
	}); 
    //添加快捷查询;
    nm3kPickData({
        startTime : startTime,
        endTime : endTime,
        searchFunction : query
    })
}
function disabledToolbarBtn(num){ //num为选中的的行的个数;
	var $btn2 = $("#btn2");
    if(num > 0){
    	$btn2.removeAttr("disabled");
    }else{
    	$btn2.attr({disabled: "disabled"});
    }
};
function bulidVideoGrid(){
    var w = $(window).width() - 35;
	store = new Ext.data.JsonStore({
	    url: '/cmcSpectrum/loadSpectrumVideo.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['videoId','videoName', 'startTimeString', 'endTimeString' , 'videoType', 'videoTypeString', 'entityIp', 'entityType', 'oltAlias', 'cmtsAlias',
	             'userName','terminalIp','url','timeInterval']
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	var cm = new Ext.grid.ColumnModel([
		sm,	                       
		{header:'<div style="text-align:center">'+ '@spectrum.videoId@' + '</div>', dataIndex: 'videoId',hidden:true,sortable: false},
		{header:'<div style="text-align:center">'+ '@spectrum.videoInterval@' + '</div>', dataIndex: 'timeInterval',hidden:true,sortable: false},
       	{header:'<div style="text-align:center">'+ '@spectrum.videoName@' + '</div>', width: w/8, dataIndex: 'videoName', sortable: false},
       	{header:'<div style="text-align:center">'+ '@spectrum.videoType@' + '</div>', width: w/8, dataIndex: 'videoType', sortable: false, renderer : typeRender},
       	{header:'<div style="text-align:center">'+ '@COMMON.startTime@' + '</div>', width: w/8, dataIndex: 'startTimeString',  sortable: false},
    	{header:'<div style="text-align:center">'+ '@COMMON.endTime@' + '</div>', width: w/8, dataIndex: 'endTimeString',  sortable: false},
    	{header:'<div style="text-align:center">'+ '@COMMON.alias@' + '</div>', width: w/8, dataIndex: 'cmtsAlias',  sortable: false},
    	{header:'<div style="text-align:center">'+ '@spectrum.userName@' + '</div>', width: w/8, dataIndex: 'userName',  sortable: false},
    	{header:'<div style="text-align:center">'+ '@spectrum.terminalIp@' + '</div>', width: w/8, dataIndex: 'terminalIp',  sortable: false},
    	{header:'<div style="text-align:center">'+ 'URL' + '</div>', dataIndex: 'url',hidden:true , sortable: false},
    	{header:'@COMMON.manu@', width: 140, dataIndex: 'op',  sortable: false,renderer : manuRender,fixed:true}
    ]);
	grid = new Ext.grid.GridPanel({
		border: true, 
		region: 'center',
		bodyCssClass:"normalTable",
        store: store, 
        cm: cm, 
        sm: sm,
        viewConfig: {
            forceFit: true
        },
		bbar: buildPagingToolBar()
    });
	store.load({params:{start:0, limit: pageSize}});
}

function manuRender(v,m,r){
	return String.format("<a href='javascript:;' onClick='playVideo(" + r.data.videoId + ")'>@spectrum.videoPlay@</a>/<a href='javascript:;' onClick='deleteVideo()'>@COMMON.del@</a>/<a href='javascript:;' style='padding-right:5px;' onClick = 'renameVideo()'>@COMMON.rename@</a>");
}
function typeRender(value, m, record){
	return (value==1) ? "@spectrum.realTimeVideo@" : "@spectrum.historyVideo@";
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({
    	id: 'extPagingBar', 
    	pageSize: pageSize,
    	store : store,
       	displayInfo: true, 
       	items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'],
       	listeners : {
       		beforechange : function(){
       			disabledToolbarBtn(0);
       		}
       	}
    });
   return pagingToolbar;
}

function playVideo(videoId){
	window.top.addView("videoPlayer", "@spectrum.videoPlay@", "apListIcon", "/cmcSpectrum/playVideo.tv?videoId="+videoId, "", true);
}

function query(){
    var stTime = startTime.value;
	var etTime = endTime.value;
	if((stTime!=null&&stTime!="")&&(etTime!=null&&etTime!="")&&stTime>etTime){
	    top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@spectrum.periodConflict@</b>'
   		});
		return;
	}
	
    store.baseParams={
           start: 0, 
           limit: pageSize,
           videoName : $('#videoName').val(),
           startTime : stTime?stTime.format("yyyy-mm-dd HH:MM:ss"):"",
           endTime : etTime?etTime.format("yyyy-mm-dd HH:MM:ss"):"",
           cmtsAlias : $('#cmtsAlias').val(),
           userName : $('#userName').val(),
           terminalIp : $('#terminalIp').val(),
           videoType : $('#videoType').val()
    };
	store.load({
		callback : function(){
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
	});
}

function deleteVideo() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var videoIdList = [];
		for (var i = 0; i < selections.length; i++) {
		    videoIdList[i] = selections[i].data.videoId;
		}
		window.top.showConfirmDlg("@COMMON.tip@", "@spectrum.confirmDeleteVideo@", function(type) {
			if (type == 'no') {return;}
			$.ajax({
				url:'/cmcSpectrum/deleteVideo.tv',
				data: {videoIds : videoIdList},
				success:function(){
					   refresh();
				},error:function(){
				    top.afterSaveOrDelete({
						title: '@COMMON.tip@',
							html: '<b class="orangeTxt">@spectrum.deleteVideoFail@</b>'
			   		});
				}
			});
		});
	} else {
	    top.afterSaveOrDelete({
			title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@spectrum.pleaseSelectVideoDelete@</b>'
   		});
	}
}

function clearVideo(){
    window.top.showConfirmDlg("@COMMON.tip@", "@spectrum.clearVideo@", function(type) {
        if (type == 'no') {return;}
        $.ajax({
            url:'/cmcSpectrum/clearVideo.tv',
            success:function(){
                refresh();
            },error:function(){
                top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                        html: '<b class="orangeTxt">@spectrum.clearVideoFail@</b>'
                });
            }
        });
    });
}

function renameVideo(){
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var videoId = record.data.videoId;
    var videoName = record.data.videoName;
    window.top.createDialog('renameVideo', '@spectrum.renameVideo@', 600, 250,
            '/cmcSpectrum/showRenameVideo.tv?videoId=' + videoId+'&videoName='+videoName , null, true, true);
}

function refresh(){
    store.reload({
    	callback : function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
    	}
    });
}