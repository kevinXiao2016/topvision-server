/**
 * EponIndexUtil.js (c)2012, Topvision Copyrights
 * 
 * @author Bravin
 */


var IndexUtil = {};
/*************************
 		端口监视指标
 *************************/
var PORT_INDEX = [{index : "InOctets" , value : I18N.PERF.moI.InOctets } , 
                  {index : "InPkts" , value : I18N.PERF.moI.InPkts } , 
                  {index : "InBroadcastPkts" , value : I18N.PERF.moI.InBroadcastPkts } , 
                  {index : "InMulticastPkts" , value : I18N.PERF.moI.InMulticastPkts } , 
                  {index : "InPkts64Octets" , value : I18N.PERF.moI.InPkts64Octets } , 
                  {index : "InPkts65to127Octets" , value : I18N.PERF.moI.InPkts65to127Octets } , 
                  {index : "InPkts128to255Octets" , value : I18N.PERF.moI.InPkts128to255Octets } , 
                  {index : "InPkts256to511Octets" , value : I18N.PERF.moI.InPkts256to511Octets } , 
                  {index : "InPkts512to1023Octets" , value : I18N.PERF.moI.InPkts512to1023Octets } , 
                  {index : "InPkts1024to1518Octets" , value : I18N.PERF.moI.InPkts1024to1518Octets } , 
                  {index : "InPkts1519to1522Octets" , value : I18N.PERF.moI.InPkts1519to1522Octets } , 
                  {index : "InUndersizePkts" , value : I18N.PERF.moI.InUndersizePkts } , 
                  {index : "InOversizePkts" , value : I18N.PERF.moI.InOversizePkts } , 
                  {index : "InFragments" , value : I18N.PERF.moI.InFragments } , 
                  {index : "InMpcpFrames" , value : I18N.PERF.moI.InMpcpFrames } , 
                  {index : "InMpcpOctets" , value : I18N.PERF.moI.InMpcpOctets } , 
                  {index : "InOAMFrames" , value : I18N.PERF.moI.InOAMFrames } , 
                  {index : "InOAMOctets" , value : I18N.PERF.moI.InOAMOctets } , 
                  {index : "InCRCErrorPkts" , value : I18N.PERF.moI.InCRCErrorPkts } , 
                  {index : "InDropEvents" , value : I18N.PERF.moI.InDropEvents } , 
                  {index : "InJabbers" , value : I18N.PERF.moI.InJabbers } , 
                  {index : "InCollision" , value : I18N.PERF.moI.InCollision } , 
                  {index : "OutOctets" , value : I18N.PERF.moI.OutOctets } , 
                  {index : "OutPkts" , value : I18N.PERF.moI.OutPkts } , 
                  {index : "OutBroadcastPkts" , value : I18N.PERF.moI.OutBroadcastPkts } , 
                  {index : "OutMulticastPkts" , value : I18N.PERF.moI.OutMulticastPkts } , 
                  {index : "OutPkts64Octets" , value : I18N.PERF.moI.OutPkts64Octets } , 
                  {index : "OutPkts65to127Octets" , value : I18N.PERF.moI.OutPkts65to127Octets } , 
                  {index : "OutPkts128to255Octets" , value : I18N.PERF.moI.OutPkts128to255Octets } , 
                  {index : "OutPkts256to511Octets" , value : I18N.PERF.moI.OutPkts256to511Octets } , 
                  {index : "OutPkts512to1023Octets" , value : I18N.PERF.moI.OutPkts512to1023Octets } , 
                  {index : "OutPkts1024to1518Octets" , value : I18N.PERF.moI.OutPkts1024to1518Octets } , 
                  {index : "OutPkts1519to1522Octets" , value : I18N.PERF.moI.OutPkts1519to1522Octets } , 
                  {index : "OutUndersizePkts" , value : I18N.PERF.moI.OutUndersizePkts } , 
                  {index : "OutOversizePkts" , value : I18N.PERF.moI.OutOversizePkts } , 
                  {index : "OutFragments" , value : I18N.PERF.moI.OutFragments } , 
                  {index : "OutMpcpFrames" , value : I18N.PERF.moI.OutMpcpFrames } , 
                  {index : "OutMpcpOctets" , value : I18N.PERF.moI.OutMpcpOctets } , 
                  {index : "OutOAMFrames" , value : I18N.PERF.moI.OutOAMFrames } , 
                  {index : "OutOAMOctets" , value : I18N.PERF.moI.OutOAMOctets } , 
                  {index : "OutCRCErrorPkts" , value : I18N.PERF.moI.OutCRCErrorPkts } , 
                  {index : "OutDropEvents" , value : I18N.PERF.moI.OutDropEvents } , 
                  {index : "OutJabbers" , value : I18N.PERF.moI.OutJabbers } , 
                  {index : "OutCollision" , value : I18N.PERF.moI.OutCollision}];

//*************** SNI COLLECTION ******************//
IndexUtil.SNI_COLLECTION = [ 1, 23];
//*************** PON COLLECTION ******************//
IndexUtil.PON_COLLECTION = [1, 23];
//*************** ONU PON COLLECTION ***************//
IndexUtil.ONU_COLLECTION = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 17, 19, 20, 23, 24, 25, 26, 27, 28,
                            29, 30, 31, 32, 37, 39, 42];
//*************** ONU UNI COLLECTION ***************//
IndexUtil.UNI_COLLECTION = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 19, 23, 24, 25, 26, 27, 28, 29, 30, 31,
                            32, 42, 44]

/**
 * 根据下标获得对应的指标名字
 */
IndexUtil.getCertainIndex = function(index,en){
	if('zh' == en){
		return PORT_INDEX[index-1].value
	}else{
		return PORT_INDEX[index-1].index
	}
}

/**
 * 得到SNI端口的指标,英文或中文
 */
IndexUtil.getSniIndex = function(en){
	var RES = [];
	for(var i=IndexUtil.SNI_COLLECTION.length ; i>0 ; i--){
		if(en)
			RES.push(PORT_INDEX[IndexUtil.SNI_COLLECTION[i-1]-1].index);
		else
			RES.push(PORT_INDEX[IndexUtil.SNI_COLLECTION[i-1]-1].value);
	}
	return RES;
}

/**
 * 得到SNI端口的指标对象，英文和中文
 */
IndexUtil.getSni = function(){
	var RES = [];
	for(var i=IndexUtil.SNI_COLLECTION.length ; i>0 ; i--){
		var o = PORT_INDEX[IndexUtil.SNI_COLLECTION[i-1]-1]
		o.cursor = IndexUtil.SNI_COLLECTION[i-1]
		RES.push(o);
	}
	return RES;
}



/**
 * 得到PON端口的指标,英文或中文
 */
IndexUtil.getPonIndex = function(en){
	var RES = [];
	for(var i=IndexUtil.PON_COLLECTION.length ; i>0 ; i--){
		if(en)
			RES.push(PORT_INDEX[IndexUtil.PON_COLLECTION[i-1]-1].index);
		else
			RES.push(PORT_INDEX[IndexUtil.PON_COLLECTION[i-1]-1].value);
	}
	return RES;
}

/**
 * 得到PON端口的指标对象，英文和中文
 */
IndexUtil.getPon = function(en){
	var RES = [];
	for(var i=IndexUtil.PON_COLLECTION.length ; i>0 ; i--){
		var o = PORT_INDEX[IndexUtil.PON_COLLECTION[i-1]-1]
		o.cursor = IndexUtil.PON_COLLECTION[i-1]
		RES.push(o);
	}
	return RES;
}

/**
 * 得到ONU的pon端口的指标,英文或中文
 */
IndexUtil.getOnuIndex = function(en){
	var RES = [];
	for(var i=IndexUtil.ONU_COLLECTION.length ; i>0 ; i--){
		if(en)
			RES.push(PORT_INDEX[IndexUtil.ONU_COLLECTION[i-1]-1].index);
		else
			RES.push(PORT_INDEX[IndexUtil.ONU_COLLECTION[i-1]-1].value);
	}
	return RES;
}

/**
 * 得到ONU的PON端口的指标对象，英文和中文
 */
IndexUtil.getOnu = function(){
	var RES = [];
	for(var i=IndexUtil.ONU_COLLECTION.length ; i>0 ; i--){
		var o = PORT_INDEX[IndexUtil.ONU_COLLECTION[i-1]-1]
		o.cursor = IndexUtil.ONU_COLLECTION[i-1]
		RES.push(o)
	}
	return RES;
}

/**
 * 得到ONU的uni端口的指标,英文或中文
 */
IndexUtil.getUniIndex = function(en){
	var RES = [];
	for(var i=IndexUtil.UNI_COLLECTION.length ; i>0 ; i--){
		if(en)
			RES.push(PORT_INDEX[IndexUtil.UNI_COLLECTION[i-1]-1].index);
		else
			RES.push(PORT_INDEX[IndexUtil.UNI_COLLECTION[i-1]-1].value);
	}
	return RES;
}

/**
 * 得到ONU的UNI端口的指标对象，英文和中文
 */
IndexUtil.getUni = function(){
	var RES = [];
	for(var i=IndexUtil.UNI_COLLECTION.length ; i>0 ; i--){
		var o = PORT_INDEX[IndexUtil.UNI_COLLECTION[i-1]-1]
		o.cursor = IndexUtil.UNI_COLLECTION[i-1]
		RES.push(o);
	}
	return RES;
}

/**
 * 得到所有端口的指标,英文或中文
 */
IndexUtil.getIndex = function(type,en){
	type = type.toLowerCase();
	var RES = [];
	switch(type){
		case 'sni':
			if(en)
				return IndexUtil.getSniIndex(en)
			else
				return IndexUtil.getSni()
		case 'pon':
			if(en)
				return IndexUtil.getPonIndex(en)
			else
				return IndexUtil.getPon()
		case 'onu':
			if(en)
				return IndexUtil.getOnuIndex(en)
			else
				return IndexUtil.getOnu()
		case 'uni':
			if(en)
				return IndexUtil.getUniIndex(en)
			else
				return IndexUtil.getUni()
		default:
			for(var i=PORT_INDEX.length ; i>0 ; i--){
				if(en)
					RES.push(PORT_INDEX[i-1].index);
				else
					RES.push(PORT_INDEX[i-1].value);
			}
			return RES;
	}
	
}

IndexUtil.getChineseName  = function(index){
	for(var i=0 ; i<PORT_INDEX.length ; i++){
		if( index == PORT_INDEX[i].index ){
			return PORT_INDEX[i].value
		}
	}
}

/**
 * 得到所有端口的指标对象，英文和中文
 */
IndexUtil.getIndexObject = function(){
	var RES = [];
	for(var i=PORT_INDEX.length ; i>0 ; i--)
		RES.push(PORT_INDEX[i-1]);
	return RES;
}


var DateUtil = {
	getFlowDisplayRest : function(value , type){
		var param;
		if( type == 0 ){
			param = 30 / 8;
		}else if(type == 1 ){
			param = 15 * 60 / 8;  
		}else if(type == 2 ) {
			param = 24 * 60 * 60 / 8;
		}
		if(value < 1024 * param ){
			return (value/ param).toFixed(2) + 'bps';
		}else if(value < 1024*1024  * param ){
			return (value / 1024 / param).toFixed(2) + "Kbps";
		}else if(value < 1024*1024*1024  * param ){
			return (value / 1024 / 1024/ param).toFixed(2)  + "Mbps";
		}else{
			return (value / 1024 / 1024 / 1024/ param).toFixed(2)  + "Gbps";
		}
	},
	
	getOctDisplayRest : function(value){
		if(value < 1024 ){
			oct = value.toFixed(2) + 'B';
		}else if(value < 1024*1024 ){
			oct = (value/1024).toFixed(2) + 'KB';
		}else if(value < 1024*1024*1024 ){
			oct = (value/1024/1024).toFixed(2) + 'MB';
		}else{
			oct = (value/1024/1024/1024).toFixed(2) + 'GB';
		}
		return oct;
	}
};