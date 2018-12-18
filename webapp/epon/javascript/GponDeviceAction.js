function gponOnuAuthHandler(){
	var slotId = olt.slotList[currentId.split("_")[1]-1].slotId;
    var ponId = getActualId();
    onuAuthenData = {slotId: slotId, ponId: ponId};
    window.top.createDialog('gponOnuAuthen', I18N.GPON.onuAuthMgmt, 800, 490, '/gpon/onuauth/showOnuAuthen.tv?entityId=' + entityId 
            + '&slotId=' + slotId + '&ponId=' + ponId+"&ponIndex="+getActualIndex(), null, true, true);	
}