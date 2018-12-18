var EntityType = function(){
	var typeMap,
		Unkown_Type = -1,//基本类型
		Base_Type = 1,//基本类型
		Category_Type = 2,//分类类型
		NetWork_Type = 4,
		OLT = 10000,
		STANDARD_OLT = 11000,
		ONU = 13000,
		CCMTS = 30000,
		CMTS = 40000,
		ENTITYWITHIP = 50000,
		CCMTSANDCMTS = 60000,
		CCMTSWITHAGENT = 70000,
		CCMTSWITHOUTAGENT = 80000,
		BSRCMTS = 90000,
		UBRCMTS = 100000,
		CASACMTS = 110000; 
		CC8800C_A = 30005,
		CC8800ES = 30013,
		CC8800CES = 30014,
		CC8800DES = 30015,
		CC8800C10GS = 30021,
		CC8800FS = 30023,
		CCC_B = 30006,
		ONU_8626 = 38;
		OLT_8602G = 10007
	
	Array.prototype.contains = function (elem) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == elem) {
				return true;
			}
		}
		return false;
	}
	
	$.ajax({
		url: '/entity/loadEntityType.tv',
		async: false, cache: false,
	    success: function(response) {
	    	if(response.success){
	    		typeMap = response;
	    	}
	    }, error: Ext.emptyFn
	});
	
	return {
		isUnkown_Type : function (typeId){
		    return typeId == Unkown_Type;
		},
		isBaseType : function (typeId){
			//return $.inArray(typeId, typeMap[Base_Type]) === -1;
			return typeMap[Base_Type] != null && typeMap[Base_Type].contains(typeId)
		},
		isCategoryType : function (typeId){
			return typeMap[Category_Type] != null && typeMap[Category_Type].contains(typeId)
		},
		isNetWorkType : function (typeId){
			return typeMap[NetWork_Type] != null && typeMap[NetWork_Type].contains(typeId)
		},
		isOltType : function (typeId){
			return typeMap[OLT] != null && typeMap[OLT].contains(typeId)
		},
		isOnuType:function (typeId){
			return typeMap[ONU] != null && typeMap[ONU].contains(typeId)
		},
		isCcmtsType : function (typeId){
			return typeMap[CCMTS] != null && typeMap[CCMTS].contains(typeId)
		},
		isCmtsType : function (typeId){
			return typeMap[CMTS] != null && typeMap[CMTS].contains(typeId)
		},
		isEntityWithIpType : function (typeId){
			return typeMap[ENTITYWITHIP] != null && typeMap[ENTITYWITHIP].contains(typeId)
		},
		isCcmtsAndCmtsType : function (typeId){
			return typeMap[CCMTSANDCMTS] != null && typeMap[CCMTSANDCMTS].contains(typeId)
		},
		isCcmtsWithAgentType : function (typeId){
			return typeMap[CCMTSWITHAGENT] != null && typeMap[CCMTSWITHAGENT].contains(typeId)
		},
		isCcmtsWithoutAgentType : function (typeId){
			return typeMap[CCMTSWITHOUTAGENT] != null && typeMap[CCMTSWITHOUTAGENT].contains(typeId)
		},
		isBsrCmtsType : function (typeId){
			return typeMap[BSRCMTS] != null && typeMap[BSRCMTS].contains(typeId)
		},
		isUbrCmtsType : function (typeId){
			return typeMap[UBRCMTS] != null && typeMap[UBRCMTS].contains(typeId)
		},
		isCasaCmtsType : function (typeId){
			return typeMap[CASACMTS] != null && typeMap[CASACMTS].contains(typeId)
		},
		isCC8800C_A : function (typeId){
			return typeMap[CC8800C_A] != null && typeMap[CC8800C_A].contains(typeId)
		},
		is8602G_OLT : function(typeId){
			return typeMap[OLT_8602G] != null && typeMap[OLT_8602G].contains(typeId)
		},
		isCC8800C_B : function (typeId){
			return typeId == CCC_B;
		},
		isCC8800ES : function(typeId){
			return typeId == CC8800ES 
		},
		isCC8800CES : function(typeId){
			return typeId == CC8800CES 
		},
		isCC8800DES: function(typeId){
			return typeId == CC8800DES 
		},
		isCC8800C10GS : function(typeId){
			return typeId == CC8800C10GS
		},
		isCC8800FS : function(typeId){
			return typeId == CC8800FS
		},
        is8626Onu : function(typeId){
            return typeId == ONU_8626
        },
		getCmtsType : function (){
			return CMTS;
		},
		getOltType : function (){
			return OLT;
		},
		getCcmtsType : function (){
			return CCMTS;
		},
		getOnuType : function (){
			return ONU;
		},
		getEntityWithIpType : function (){
			return ENTITYWITHIP;
		},
		getCcmtsWithAgentType : function (){
			return CCMTSWITHAGENT;
		},
		getCcmtsWithoutAgentType : function (){
			return CCMTSWITHOUTAGENT;
		},
		getUnkonwType : function (){
			return Unkown_Type;
		},
		getCCMTSAndCMTSType : function (){
			return CCMTSANDCMTS;
		},
		isStandardOlt : function (typeId){
			return typeMap[STANDARD_OLT] != null && typeMap[STANDARD_OLT].contains(typeId);
		}
	};
}();
