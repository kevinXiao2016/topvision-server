function checkInterfaceId(){
	var reg = /^([0-9])+$/;
	var interfaceId = $("#interfaceId").val();
	if(reg.test(interfaceId)){
		if(interfaceType == 1){
			if(interfaceId > 4094 || interfaceId < 1){
				return false;
			}
			return true;
		}
	    if(interfaceType == 2){
			if(interfaceId > 8 || interfaceId < 1){
				return false;
			}
			return true;
		}
	}else{
		return false;
	}
}
		
function checkInterfaceDesc(){
	var reg = /^[a-zA-Z\d-_\[\]()\/\.:,]{1,31}$/;
	var interfaceDesc = $("#interfaceDesc").val();
	if(reg.test(interfaceDesc)){
		return true;
	}else{
		return false;
	}
	return true;
}

//点击按钮组;
function clickTabBtn(index){
	switch(index){
	case 0:
		window.location.href = '/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entityId + '&interfaceType=1';
		break;
	case 1:
		window.location.href = '/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entityId + '&interfaceType=2';
		break;
	case 2:
		window.location.href = '/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entityId + '&interfaceType=3';
		break;
	}
}

function renderAdminStatus(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			'@interface.adminStatusUp@');	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			'@interface.adminStatusDown@');	
	}
}

function renderOperaStatus(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			'UP');	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			'DOWN');	
	}
}
