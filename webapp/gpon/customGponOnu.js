var GPON_ONU = {
	codeDuplex : {
		"0":"Auto",
		"1":"10Mbit/s only(Full)",
		"2":"100Mbit/s only(Full)",
		"3":"1000Mbit/s only(Full)",
		"4":"Auto(Full)",
		"5":"10Gb/s only(Full)",
		"10":"10Mbit/s only(Full)",
		"11":"10Mbit/s only(Half)",
		"12":"100Mbit/s only(Half)",
		"13":"1000Mbit/s only(Half)",
		"14":"Auto(Half)",
		"20":"1000Mbit/s only(Auto)",
		"30":"100Mbit/s only(Auto)"
	},
    connectCapbility : [
        '',
        '(LSB) N:1 bridging',
        '1:M mapping',
        '1:P filtering',
        'N:M bridge-mapping',
        '1:MP map-filtering',
        'N:P bridge-filtering',
        'N:MP bridge-map-filtering',
        ''
    ],
    qosFlexibility : [
        '',
        '(LSB) Priority queue ME: Port field of related port attribute is read-write and can point to any T-CONT or UNI port in the same slot',
        'Priority queue ME: The traffic scheduler pointer is permitted to refer to any other traffic scheduler in the same slot',
        'Traffic scheduler ME: T-CONT pointer is read-write',
        'Traffic scheduler ME: Policy attribute is read-write',
        'T-CONT ME: Policy attribute is read-write',
        'Priority queue ME: Priority field of related port attribute is read-write',
        ''
    ],
    renderConnectCapbility : function(str){
    	var connect = str;
    	var connectStr = "";
    	if(connect != null && connect != ""){
    		var connectII = parseInt(connect,16).toString(2);
    		for(var i = 0;i<connectII.length;i++){
    			connectStr += '<label class="floatLabel">';
    			if(connectII[i] == 1){
    				connectStr = connectStr + this.connectCapbility[i+1] + "</label>";
    			}
    		}
    		return connectStr;
    	}else{
    		return "--";
    	} 
    },
    renderQosFlexibility : function(str){
    	var qosFix = str;
    	var qosFixStr = "";
    	if(qosFix != null && qosFix != ""){
    		var qosFixStr = parseInt(qosFix,16).toString(2);
    		if(qosFixStr == 0){
    			return "--";
    		}
    		for(var i = 0;i<qosFixII.length;i++){
    			if(qosFixII[i] == 1){
    				qosFixStr = qosFixStr + this.qosFlexibility[i+1] + "<br>";
    			}
    		}
    		return qosFixStr;
    	}else{
    		return "--";
    	} 
    }
}






