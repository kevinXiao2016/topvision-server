function createOltAuthGrid(){
	var expander = new Ext.ux.grid.RowExpander({
		id : 'expander' ,
		dataIndex : 'entityId',
	    enableCaching : false,
        tpl : ''
    });
	
	var w = $(window).width();
	var h = $(window).height();
	var oltColumns = [ expander,
	        {header : "@report.deviceName@", width : w/15, sortable : true, align : 'center', dataIndex : 'name', renderer : renderName},
			{header : "@resources/COMMON.manageIp@", width : w/15, sortable : true, align : 'center', dataIndex : 'ip'}, 
			{header : "@onuAuth.mac@", width : w/15, sortable : true, align : 'center', dataIndex : 'macAuthCount', renderer : oltMacAuthCountRender}, 
			{header : "@onuAuth.sn@", width : w/8, sortable : true, align : 'center', dataIndex : 'snAuthCount', renderer : oltSnAuthCountRender},
			{header : "@OnuAuth/OnuAuth.gponAuth@", dataIndex : 'gponAuthCount',renderer : oltGponAuthCountRender},
			{header : "@OnuAuth/OnuAuth.autoFind@", dataIndex : 'gponAutoFindCount',renderer : oltGponAutoFindRender},
			{header : "@onuAuth.authFail@", width : w/8, sortable : true, align : 'center', dataIndex : 'authFailCount', renderer : oltAuthFailCountRender}, 
	    	{ header : "@COMMON.manu@", width : w/9, sortable : true, align : 'center', dataIndex : 'count', renderer : operationRender}
		];

		oltStore = new Ext.data.JsonStore({
			url : '/epon/onuauth/getOltOnuAuthStatistics.tv',
			root : 'data',
			totalProperty: 'rowCount',
			fields : [ 'entityId', 'ip', 'name', 'macAuthCount','snAuthCount', 'authFailCount',"gponAuthCount","gponAutoFindCount"]
		});
		var oltCm = new Ext.grid.ColumnModel(oltColumns);

		var oltToolbar = new Ext.Toolbar();
		var oltToolbars = [buildName(), buildIp(),{xtype:"button", text:"@COMMON.query@", iconCls:"bmenu_find",handler: queryOltAuth}];
		oltToolbar.add(oltToolbars);
		oltToolbar.doLayout();

		oltGrid = new Ext.grid.GridPanel({
			id : 'oltGrid',
			bodyCssClass : 'normalTable',			
			plugins: expander,
			border : false,
			stripeRows:true,
			region : 'center',
			store : oltStore,
			tbar : new Ext.Toolbar({
				items : oltToolbars
			}),
			cm : oltCm,
			viewConfig : {
				forceFit: true
			},
			renderTo : 'oltAuthManage',
			loadMask : true, 
			bbar : buildOltPagingToolBar()
		});
		
		oltStore.baseParams={start: 0, limit: pageSize};
		oltStore.load();
		
		new Ext.Viewport({layout: 'border', items: [oltGrid]});
		
		expander.on("beforeexpand",function(expander,r,body,row){
			if(lastRow != null){
				expander.collapseRow(lastRow);
			}
			lastRow = row;
			var html = '';
			entityId = r.data.entityId;
			$.ajax({
	            url : "/epon/onuauth/getPonOnuAuthStatistics.tv?entityId=" + entityId,type: 'POST',async:false,cache : false,dataType:'json',
	            success : function(ponAuthData) {
	            	if(ponAuthData == null || ponAuthData.data == null){
	            		var tpl1 = new Ext.Template('No Data' );
		    			expander.tpl = tpl1;
		    			expander.tpl.compile();
	            	}else{
	            		var $list = ponAuthData.data
	            		var size = ponAuthData.data.length;
            			var html = '';
            			for(var i = 0; i < size; i++){
            				var $ponStatic = $list[i];
	                		if($ponStatic.ponPortType == @{GponConstant.PORT_TYPE_GPON}@){
	                			html += "<td align='center'>" + renderPon($ponStatic.ponIndex) + "</td>" +
					                    "<td align='center'>" + renderGponAuthMode($ponStatic.gponAuthMode) + "</td>" +
					                    "<td align='center'>--</td>" +
					                    "<td align='center'>--</td>" +
					                    "<td align='center'>--</td>" +
	                			 		"<td align='center'>"  + String.format('<a href="#" onclick="showOltGponAuth({0}, {1})">' + 
	                			 				$ponStatic.gponAuthCount + '</a>' + '&nbsp;&nbsp;', $ponStatic.entityId, $ponStatic.ponIndex) + "</td>"+
			                			"<td align='center'>"  + String.format('<a href="#" onclick="showOltGponAutoFind({0}, {1})">' + 
		            			 				$ponStatic.gponAutoFindCount + '</a>' + '&nbsp;&nbsp;',$ponStatic.entityId, $ponStatic.ponIndex) + "</td>";
	                		}else{
	                			html += "<td align='center'>" + renderPon($ponStatic.ponIndex) + "</td>" +
			                    		"<td align='center'>" + renderAuthMode($ponStatic.ponAuthMode) + "</td>" +
			                    		"<td align='center'>" + String.format('<a href="#" onclick="showPonMacAuth({0}, {1})">' + 
			                    				$ponStatic.macAuthCount + '</a>' + '&nbsp;&nbsp;', 
			                    				$ponStatic.entityId, $ponStatic.ponIndex) + "</td>" +
			                    		"<td align='center'>" + String.format('<a href="#" onclick="showPonSnMacAuth({0}, {1})">' + 
			                    				$ponStatic.snAuthCount + '</a>' + '&nbsp;&nbsp;', 
			                    				$ponStatic.entityId, $ponStatic.ponIndex) + "</td>" +
			                    		"<td align='center'>"  + String.format('<a href="#" onclick="showPonAuthFail({0}, {1})">' + 
			                    				$ponStatic.authFailCount + '</a>' + '&nbsp;&nbsp;', 
			                    				$ponStatic.entityId, $ponStatic.ponIndex) + "</td>"+
	                					"<td align='center'>--</td>"+
	                					"<td align='center'>--</td>";
	                		}
	                		if(operationDevicePower){
		                    	html = html + "<td align='center'>" +  String.format('<a href="#" onclick="modifyPonAuthMode({0}, {1}, \'{2}\',{3},\'{4}\')">@onuAuth.modifyAuthMode@</a>' + '&nbsp;&nbsp;', 
			                    		$ponStatic.entityId, $ponStatic.ponIndex, $ponStatic.ponAuthMode,$ponStatic.ponPortType,$ponStatic.gponAuthMode)  + "</td>" +
					                    "</tr>";
		                    }else{
		                    	html = html + "</tr>";
		                    }
            			}
	            		
            			var template = '<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">@onuAuth.ponAuthInfo@</span></p>' + 
    	        		"<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
    	                "<thead>" +
    	                "<tr>" +
    	                "<th align='center'>@onuAutoUpg.he.ponPort@</th>" +
    	                "<th align='cent" +
    	                "" +
    	                "" +
    	                "er'>"+"@onuAuth.authMode@"+"</th>" +
    	                "<th align='center' width='130px'>"+"@onuAuth.mac@"+"</th>" +
    	                "<th align='center'>"+"@onuAuth.sn@"+"</th> " +
    	                "<th align='center' width='200px'>"+"@onuAuth.authFail@"+"</th>"+
            			"<th align='center'>@OnuAuth/OnuAuth.gponAuth@</th> "+
            			"<th align='center'>@OnuAuth/OnuAuth.autoFind@</th> ";
            			if(operationDevicePower){
            				template = template + "<th align='center' width='200px'>"+"@COMMON.manu@"+"</th>" +
        	                "</tr>" +
        	                "</thead>" +
        	                "<tbody id='tbody-append-child'>" + html + "</tbody></table></div>"  
            			}else{
            				template = template + 
        	                "</tr>" +
        	                "</thead>" +
        	                "<tbody id='tbody-append-child'>" + html + "</tbody></table></div>"  
            			}
    	                
	            		var tpl1 = new Ext.Template(template);
		            	
		    	        expander.tpl = tpl1;
		    			expander.tpl.compile();
	            	}

	            }
	        });  
	  	}); 
}


function createOnuAuthGrid(){
	var w = $("#openLayerMain").width();
	var h = $("#openLayerMain").height();
	var onuAuthColumns;
	if(operationDevicePower){
		onuAuthColumns = [{header : "@IGMP.onuLoc@", width : w/5, sortable : true, align : 'center', dataIndex : 'onuIndex', renderer : renderIndex},
							{header : "MAC", width : w/5, sortable : true, align : 'center', dataIndex : 'mac'}, 
							{header : "@onuAuth.action@", width : w/10, sortable : true, align : 'center', dataIndex : 'action', renderer: renderAction}, 
							{header : "SN", width : w/5, sortable : true, align : 'center', dataIndex : 'sn'}, 
							{header : "@onuAuth.password@", width : w/5, sortable : true, align : 'center', dataIndex : 'password'}, 
							{header : "@onuAuth.preType@", width : w/5, sortable : true, align : 'center', dataIndex : 'onuPreType', renderer: typeRange}, 
		                	{ header : "@COMMON.manu@", width : w/10, sortable : true, align : 'center', renderer : deleteAuthRender}
		        			];
	}else{
		onuAuthColumns = [{header : "@IGMP.onuLoc@", width : w/5, sortable : true, align : 'center', dataIndex : 'onuIndex', renderer : renderIndex},
							{header : "MAC", width : w/5, sortable : true, align : 'center', dataIndex : 'mac'}, 
							{header : "@onuAuth.action@", width : w/10, sortable : true, align : 'center', dataIndex : 'action', renderer: renderAction}, 
							{header : "SN", width : w/5, sortable : true, align : 'center', dataIndex : 'sn'}, 
							{header : "@onuAuth.password@", width : w/5, sortable : true, align : 'center', dataIndex : 'password'}, 
							{header : "@onuAuth.preType@", width : w/5, sortable : true, align : 'center', dataIndex : 'onuPreType', renderer: typeRange}
		        			];
	}
	

		onuAuthStore = new Ext.data.JsonStore({
			url : '/epon/onuauth/getOnuAuth.tv',
			root : 'data',
			totalProperty: 'rowCount',
			fields : [ 'entityId', 'ponId', 'onuIndex', 'mac','action', 'sn', 'password', 'onuPreType', 'authType']
		});
		
		onuAuthStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId, 
			    authType: getAuthType()
			}
		
		var onuAuthCm = new Ext.grid.ColumnModel(onuAuthColumns);


		onuAuthGrid = new Ext.grid.GridPanel({
			id : 'onuAuthGrid',
			bodyCssClass : 'normalTable',			
			border : false,
			stripeRows:true,
			region : 'center',
			width:w,
			height:h,
			store : onuAuthStore,
			cm : onuAuthCm,
			viewConfig : {
				forceFit: true
			},
			renderTo : 'onuAuthManage',
			loadMask : true, 
			bbar : buildOnuAuthPagingToolBar()
		});
		onuAuthStore.load()
		
}

function createOnuAuthFailGrid(){
	var w = $("#openLayerMain").width();
	var h = $("#openLayerMain").height();
	var onuAuthFailColumns;
	if(operationDevicePower){
		onuAuthFailColumns = [{header : "@IGMP.onuLoc@", width : w/15, sortable : true, align : 'center', dataIndex : 'onuIndex', renderer : renderIndex},
		  					{header : "MAC", width : w/15, sortable : true, align : 'center', dataIndex : 'mac'}, 
		  					{header : "SN", width : w/8, sortable : true, align : 'center', dataIndex : 'sn'}, 
		  					{header : "@onuAuth.password@", width : w/8, sortable : true, align : 'center', dataIndex : 'password'}, 
		  					{header : "@onuAuth.authTime@", width : w/8, sortable : true, align : 'center', dataIndex : 'lastAuthTime' , renderer : renderAuthTime}, 
		                  	{ header : "@COMMON.manu@", width : w/9, sortable : true, align : 'center', dataIndex : 'count', renderer : addAuthRender}
		          			];
	}else{
		onuAuthFailColumns = [{header : "@IGMP.onuLoc@", width : w/15, sortable : true, align : 'center', dataIndex : 'onuIndex', renderer : renderIndex},
		  					{header : "MAC", width : w/15, sortable : true, align : 'center', dataIndex : 'mac'}, 
		  					{header : "SN", width : w/8, sortable : true, align : 'center', dataIndex : 'sn'}, 
		  					{header : "@onuAuth.password@", width : w/8, sortable : true, align : 'center', dataIndex : 'password'}, 
		  					{header : "@onuAuth.authTime@", width : w/8, sortable : true, align : 'center', dataIndex : 'lastAuthTime' , renderer : renderAuthTime} 
		          			];
	}

		onuAuthFailStore = new Ext.data.JsonStore({
			url : '/epon/onuauth/getOnuAuthFail.tv',
			root : 'data',
			totalProperty: 'rowCount',
			fields : [ 'entityId', 'onuIndex', 'mac', 'sn','ponIndex',
					'password', 'lastAuthTime']
		});
		
		onuAuthFailStore.baseParams={
				start: 0,
				limit: pageSize,
				entityId: globalEntityId
			}
		var onuAuthFailCm = new Ext.grid.ColumnModel(onuAuthFailColumns);

		onuAuthFailGrid = new Ext.grid.GridPanel({
			id : 'onuAuthFailGrid',
			bodyCssClass : 'normalTable',	
			width:w,
			height:h,
			border : false,
			stripeRows:true,
			region : 'center',
			store : onuAuthFailStore,
			cm : onuAuthFailCm,
			viewConfig : {
				forceFit: true
			},
			renderTo : 'onuAuthFail',
			loadMask : true, 
			bbar : buildOnuAuthFailPagingToolBar()
		});
		
		onuAuthFailStore.load();
}



function buildOltPagingToolBar() {
	var pagingToolbar = new Ext.PagingToolbar({
		id : 'oltExtPagingBar',
		pageSize : pageSize,
		store : oltStore,
		displayInfo : true,
		items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
				'-' ]
	});
	return pagingToolbar;
}

function buildOnuAuthPagingToolBar() {
	var pagingToolbar = new Ext.PagingToolbar({
		id : 'onuAuthExtPagingBar',
		pageSize : pageSize,
		store : onuAuthStore,
		displayInfo : true,
		items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
				'-' ]
	});
	return pagingToolbar;
}

function buildOnuAuthFailPagingToolBar() {
	var pagingToolbar = new Ext.PagingToolbar({
		id : 'onuAuthFailExtPagingBar',
		pageSize : pageSize,
		store : onuAuthFailStore,
		displayInfo : true,
		items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
				'-' ]
	});
	return pagingToolbar;
}

function buildPonSelect(entityId, ponIndex){
	$.ajax({
        url : "/epon/onuauth/getOltPonIndex.tv?entityId=" + entityId,
        type: 'POST',
        async:false,
        cache : false,
        dataType:'json',
        success : function(ponIndexListJson) {
        	var ponIndexPosition = Zeta$('ponIndex');
        	for (var i = 1,len = ponIndexPosition.length; i <= len; i++)
            {
        		ponIndexPosition.options[1] = null;
            }
        	if(ponIndexListJson != null || ponIndexListJson.data != null){
        		var ponIndexList = ponIndexListJson.data
        		var size = ponIndexList.length;
    			for(var i = 0; i < size; i++){
    				var option = document.createElement('option');
                    option.value = ponIndexList[i];
                    option.text = renderPon(ponIndexList[i]);
                    try {
                    	ponIndexPosition.add(option, null);
                    } catch(ex) {
                    	ponIndexPosition.add(option);
                    }
    			}
        	}
        	if(ponIndex != null){
        		Zeta$('ponIndex').value = ponIndex;
        	}
        }
    }); 
}

//render函数
function renderName(value, p, record){
	return  String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>' + '&nbsp;&nbsp;',
			record.data.entityId, value, value);
}
function oltMacAuthCountRender(value, p, record){
	return  String.format('<a href="#" onclick="showOltMacAuth({0})">' + record.data.macAuthCount + '</a>' + '&nbsp;&nbsp;', record.data.entityId);
}

function oltSnAuthCountRender(value, p, record){
	return  String.format('<a href="#" onclick="showOltSnAuth({0})">' + record.data.snAuthCount + '</a>' + '&nbsp;&nbsp;', record.data.entityId);
}
function oltGponAuthCountRender(value, p, record){
	return  String.format('<a href="#" onclick="showOltGponAuth({0})">' + record.data.gponAuthCount + '</a>' + '&nbsp;&nbsp;', record.data.entityId);
}

function oltGponAutoFindRender(value, p, record){
	return  String.format('<a href="#" onclick="showOltGponAutoFind({0})">' + record.data.gponAutoFindCount + '</a>' + '&nbsp;&nbsp;', record.data.entityId);
}

function oltAuthFailCountRender(value, p, record){
	return  String.format('<a href="#" onclick="showOltAuthFail({0})">' + record.data.authFailCount + '</a>' + '&nbsp;&nbsp;', record.data.entityId);
}
function operationRender(value, p, record){
    if(!refreshDevicePower){
        return  '<span>@COMMON.refresh@</span>';
    }else{
        return  String.format('<a href="#" onclick="refreshOltAuth({0})">@COMMON.refresh@</a> / ' + '<a href="#" onclick="modifyMode({0})">@onuAuth.modifyAuthMode@</a>', record.data.entityId);
    }
}

function deleteAuthRender(value, p, record){
	return  String.format('<a href="#" onclick="deleteOnuAuth()">@onuAuth.deleteAuth@</a>' + '&nbsp;&nbsp;');
}

function addAuthRender(value, p, record){
	return  String.format('<a href="#" onclick="addOnuAuth()">@onuAuth.addAuth@</a>' + '&nbsp;&nbsp;');
}

function renderIndex(value, p, record){
	var t = parseInt(value / 256) + (value % 256);
	return getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
}

function renderPon(ponIndex){
	var ponIndex = parseInt(ponIndex / 256) + (ponIndex % 256);
	return ((ponIndex & 0xFF000000) >> 24) + '/' + ((ponIndex & 0xFF0000) >> 16);
}

function ponIndexChange(){
	var ponIndex = $("#ponIndex").val();
	if(nowTab > 0){
		onuAuthFailStore.baseParams={
			start: 0,
			limit: pageSize,
			entityId: globalEntityId,
			ponIndex: ponIndex
		}
		onuAuthFailStore.load();
	}else{
		var authType = getAuthType();
		onuAuthStore.baseParams={
			start: 0,
			limit: pageSize,
			entityId: globalEntityId,
			ponIndex: ponIndex,
			authType: authType
		}
		onuAuthStore.load();
	}
}

function renderAuthMode(authMode){
	if(authMode == 1){
		return "@onuAuth.autoMode@"
	}else if(authMode == 2){
		return "@onuAuth.mac@"
	}else if(authMode == 3){
		return "@onuAuth.mix@"
	}else if(authMode == 4){
		return "@onuAuth.sn@"
	}else if(authMode == 5){
		return "@onuAuth.snPwdMode@"
	}
	return authMode;
}
function renderGponAuthMode(gponauthmode){
    switch(gponauthmode){
	    case 1:return "@OnuAuth/OnuAuth.snAuth@";
	    case 2:return "@OnuAuth/OnuAuth.snPassAuth@";
	    case 3:return "@OnuAuth/OnuAuth.loidAuth@";
	    case 4:return "@OnuAuth/OnuAuth.loidPassAuth@";
	    case 5:return "@OnuAuth/OnuAuth.passAuth@";
	    case 6:return "@OnuAuth/OnuAuth.autoAuth@";
	    case 7:return "@OnuAuth/OnuAuth.mixAuth@";
    }
}



function renderAction(value, p, record){
	if(record.data.action == 1){
		return "@Business.authAccept@"
	}else{
		return "@Business.authReject@"
	}
}

function renderAuthTime(value, p, record){
	var timeString;
    if (value != null && value != -1) {
        timeString = arrive_timer_format(value)
    }
    return timeString;
}
function arrive_timer_format(s) {
    var t
    if (s > -1) {
        hour = Math.floor(s / 360000);
        min = Math.floor(s / 6000) % 60;
        sec = Math.floor(s / 100) % 60;
        day = parseInt(hour / 24);
        if (day > 0) {
            hour = hour - 24 * day
            t = day + I18N.COMMON.D + hour + I18N.COMMON.H
        } else {
            t = hour + I18N.COMMON.H
        }
        if (min < 10) {
            t += "0"
        }
        t += min + I18N.COMMON.M
        if (sec < 10) {
            t += "0"
        }
        t += sec + I18N.COMMON.S
    }
    return t + '@onuAuth.before@'
}

function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
		break;
	case 3: num = (index & 0xFF00) >> 8;//ONU
		break;
	case 4: num = index & 0xFF;//UNI
		break;
	}
	return num;
}

function typeRange(value){
	if(value == 33){
		return "PN8621"
	}else if(value == 34){
		return "PN8622"
	}else if(value == 36){
		return "PN8624"
	}else if(value == '37'){
		return "PN8625"
	}else if(value == '38'){
		return "PN8626"
	}else if(value == '40'){
		return "PN8628"
	}else if(value == '48'){
		return "PN8630"
	}else if(value == '49'){
		return "PN8631"
	}else if(value == '65'){
		return "PN8641"
	}else if(value == '68'){
		return "PN8644"
	}else if(value == '71'){
		return "PN8647"
	}else if(value == '81'){
		return "PN8651"
	}else if(value == '82'){
		return "PN8652"
	}else if(value == '83'){
		return "PN8653"
	}else if(value == '84'){
		return "PN8654"
	}else if(value == '241'){
		return "CC8800"
	}else{
		return "NONE"
	}
}

function handlerRender(value, m, record){
    if(operationDevicePower){
    	var tmpl = "<img src='/images/delete.gif' onclick='deleteGponAuth(\"{entityId}\",\"{authenOnuId}\",\"{ponIndex}\")' title='@COMMON.del@'/>";
        return String.format( tmpl, record.data );
    }else{
    	return "<img src='/images/deleteDisable.gif'  title='@COMMON.del@'/>";
    }
}

function deleteGponAuth(entityId,authenOnuId,ponIndex){
	window.parent.showConfirmDlg(I18N.COMMON.tip, "@onuAuth.confirmDeleteAuth@", function(button, text) {
		if (button == "yes") {
			window.top.showWaitingDlg("@COMMON.wait@", "@OnuAuth/OnuAuth.deletingGponOnuAuth@");
			$.ajax({
		        url: '/gpon/onuauth/deleteGponOnuAuth.tv',type: 'POST',
		        data: {
		        	'gponOnuAuthConfig.entityId':entityId,
		        	'gponOnuAuthConfig.authenOnuId':authenOnuId,
		        	'gponOnuAuthConfig.ponIndex':ponIndex
		        },
		        success: function() {
		        	top.closeWaitingDlg();
		        	top.nm3kRightClickTips({
						title: "@COMMON.tip@", html: "@OnuAuth/OnuAuth.deleteGponAuthOk@"
					});
		        	gponOnuAuthstore.reload();
		        }, error: function(json) {
		            top.showMessageDlg("@COMMON.error@", "@OnuAuth/OnuAuth.deleteGponAuthEr@");
		        }, cache: false
		    });
		}
	});
}

function contentRender(v){
	return v || "--";
}
function snRender(v){
	return v.replaceAll(":","");
}
function renderGponAuthGrid(entityId,ponIndex){
	var w = $("#gponauth-openLayerMain").width();
	var h = $("#gponauth-openLayerMain").height();
	var columns = [
		{header: "@OnuAuth/OnuAuth.onu.location@",dataIndex: 'location',width:50},
		{header: "SN",dataIndex: 'sn',renderer:snRender},
		{header: "@COMMON.password@",dataIndex: 'password',renderer:contentRender},
		{header: "LOID",dataIndex: 'loid',renderer:contentRender},
		{header: "LOID@COMMON.password@",dataIndex: 'loidPassword',renderer:contentRender},
		{header: "@OnuAuth/OnuAuth.lineProfile@",dataIndex: 'lineProfileId',width:50},
		{header: "@OnuAuth/OnuAuth.srvProfile@",dataIndex: 'srvProfileId',width:50},
		{header: "@COMMON.manu@",dataIndex: 'sn',width:80,renderer:handlerRender}];

	WIN.gponOnuAuthstore = new Ext.data.JsonStore({
		url:"/gpon/onuauth/loadOnuAuthConfigList.tv",
		baseParams:{entityId:entityId,ponIndex:ponIndex},
		autoLoad:false,
        fields : ['authenOnuId',"entityId","location","sn","password","loid","loidPassword","lineProfileId","srvProfileId","ponIndex"]
	});
	
	var gponOnuAuthGrid = new Ext.grid.GridPanel({
		bodyCssClass : 'normalTable',
		border : false,
		stripeRows:true,
		region : 'center',
		width:w,
		height:h,
		store : gponOnuAuthstore,
		columns : columns,
		viewConfig : {forceFit: true},
		renderTo : 'gponOnuAuthManage'
	});
}

var grid;
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

function renderAutoFindGrid(entityId){
	var w = $("#autofind-openLayerMain").width();
	var h = $("#autofind-openLayerMain").height();
	var columns = [sm,{header: "@OnuAuth/OnuAuth.onu.logicalLocation@",dataIndex: 'location'},
		{header: "@OnuAuth/OnuAuth.onuType@",dataIndex: 'onuType'},
		{header: "@onuAuth.authMode@",dataIndex: 'ponOnuAuthenMode',renderer:renderGponAuthMode},
		{header: "@OnuAuth/OnuAuth.gponSN@",dataIndex: 'serialNumber',wdith:200,renderer:snRender},
		{header: "@COMMON.password@",dataIndex: 'password',renderer:contentRender},
		{header: "LOID",dataIndex: 'loid',renderer:contentRender},
		{header: "LOID@COMMON.password@",dataIndex: 'loidPassword',renderer:contentRender},
		{header: "@OnuAuth/OnuAuth.software.version@",dataIndex: 'softwareVersion'},
		{header: "@OnuAuth/OnuAuth.hardware.version@",dataIndex: 'hardwareVersion'},
		{header: "@OnuAuth/OnuAuth.lastAuthTime@",dataIndex: 'findTime',width:200},
		{header: "@COMMON.manu@",dataIndex: 'location',width:80,renderer: autoFindHandlerRender}];

	WIN.gponOnuAutoFindstore = new Ext.data.JsonStore({
		url:"/gpon/onuauth/loadAutoFindOnuList.tv",
		root : "data",
		totalProperty : "rowCount",
		baseParams:{entityId:entityId,start:0,limit:pageSize},
		autoLoad:true,
        fields : ['entityId',"location",'onuIndex','ponOnuAuthenMode','autoFindTime',"findTime",'hardwareVersion','loid','loidPassword','onuType','password','serialNumber','softwareVersion']
	});
	var tbar = new Ext.Toolbar(
            {
                items : [
                        {                 
                            iconCls : 'bmenu_new',
                            id:"batchAuthButton",
                            text : "@OnuAuth/OnuAuth.batchAuth@",
                            disabled:true,
                            handler : batchAuth
                        }
                   ]
            });
	//分页工具
	var bbar = new Ext.PagingToolbar({
		id : "extPagingBar",
		pageSize : pageSize,
		store : gponOnuAutoFindstore,
		displayInfo : true,
		items : [ "-",
				String.format("@COMMON.displayPerPage@", pageSize),
				"-" ]
	});
	grid = new Ext.grid.GridPanel({
		bodyCssClass : 'normalTable',
		border : false,
		stripeRows:true,
		region : 'center',
		width:w,
		height:h,
		store : gponOnuAutoFindstore,
		columns : columns,
		bbar : bbar,
		sm:sm,
		tbar:tbar,
		viewConfig : {forceFit: true},
		renderTo : 'gponOnuAutoFind'
	});
}
//控制“批量认证”按钮操作与不可操作
function disabledToolbarBtn(num){ //num为选中的的行的个数;
	if(num > 0){ 
        disabledBtn("batchAuthButton", false);
	}else{
        disabledBtn("batchAuthButton", true);
	}
}
function disabledBtn(id, disabled){
	var t=Ext.getCmp(id);
	Ext.getCmp(id).setDisabled(disabled); 
}
//批量认证
function batchAuth(){
	var rs = sm.getSelections();
	for(var i = 0;i < rs.length;i++){
		if(rs[i].data.ponOnuAuthenMode != 1){
			window.parent.showMessageDlg('@COMMON.tip@','@OnuAuth/OnuAuth.batchAuthTip@');
			return;
		}
	}
	var onuNumber = rs.length;
	var onuIndexs = [];
	var entityIds = [];
	 for(var i = 0; i < rs.length; i++){
		    entityIds[i] = rs[i].data.entityId;
	    	onuIndexs[i] = rs[i].data.onuIndex;
	    }
	window.top.createDialog('batchAddGponOnuAuth', "@OnuAuth/OnuAuth.batchAddGponOnuAuth@", 600, 370,"/gpon/onuauth/showBatchAddGponOnuAuth.tv?entityIds="+entityIds.join(",")+"&onuNumber="+onuNumber+"&onuIndexs="+onuIndexs.join(","), null, true, true,function(){
		refreshAutoFindGrid();
	});
}

function autoFindHandlerRender(v,m,r){
	if(operationDevicePower){
		var tmpl = "<a href='javascript:;' onClick='addAutoFindToGponAuth({entityId},{onuIndex})'>@OnuAuth/OnuAuth.joinAuth@</a> ";
		return String.format( tmpl, r.data );
	}else{
		return "--";
	}
}

