function operationAuthInit(power,ids){
    if(!power){
    	for(var i=0; i<ids.length; i++){
    		$("#"+ids[i]).attr("disabled",true);
    	}
    } 
}