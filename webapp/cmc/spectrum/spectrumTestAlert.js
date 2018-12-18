Ext.onReady(function(){
	bulidVideoGrid();
})

var store;

function save(){
	var freq = $('#freq').val();
	var power = $('#power').val();
	if(!isDigit(freq) || !isDigit(power)){
		return;
	}
	$.ajax({
		url:'/cmcSpectrum/addTestFreq.tv',
		data: {freq: freq,
			   power: power},
		success:function(){
			store.reload();
		},error:function(){
		    top.afterSaveOrDelete({
				title: '@COMMON.tip@',
				html: '<b class="orangeTxt">@spectrum.addFreFail@</b>'
	   		});
		}
	});
}


//function getIndex(freq){
//	for(var i = 0 ; i <points.length ; i ++ ){
//		if(points[i][0] == freq){
//			return i;
//		}
//	}
//	return -1;
//}

function manuRender(v,m,r){
	return String.format("<a href='javascript:;' onClick='deleteFreq(" + r.data.freq + ")'>@COMMON.del@</a>");
}

function deleteFreq(freq){
		$.ajax({
			url:'/cmcSpectrum/deleteTestFreq.tv',
			data: {freq: freq},
			success:function(){
				store.reload();
//				store.removeAt(index);
//				data.splice(index,1);
			},error:function(){
			    top.afterSaveOrDelete({
					title: '@COMMON.tip@',
					html: '<b class="orangeTxt">@spectrum.delFreFail@</b>'
		   		});
			}
		});
}

function bulidVideoGrid(){
	var cm =  new Ext.grid.ColumnModel([
		{header:'@spectrum.fre@',dataIndex:'freq'},
		{header:'@spectrum.Power@',dataIndex:'power'},
		{header:'@COMMON.del@', dataIndex: 'op',  renderer : manuRender}
	]);
	store = new Ext.data.JsonStore({
	    url: '/cmcSpectrum/loadSpecialPoints.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['freq','power']
	});
	var grid = new Ext.grid.GridPanel({
		renderTo:'grid',
		store:store,
		height:1000,
		cm:cm
	})
	store.load();
}

/********************************************/
/*---------------判断是否为纯数字-------------*/
/********************************************/
function isDigit(s){ 
    var patrn=/^[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$/; 
    if(!patrn.exec(s)){
        return false; 
    }else{
        return true;
    }
}