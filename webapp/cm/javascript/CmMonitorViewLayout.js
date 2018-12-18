var cmMonitorCmStore;

/**
 * Mac地址校验,模糊校验
 * @author YangYi
 * @param str
 * @returns {Boolean}
 */
function testMacAddress(str){
	if(str.length > 18){
		return false;
	}
	if(str.length == 0){
		return true;
	}
	var macFormat = /[A-Fa-f0-9]{1}/;
	for(var i = 0 ; i < str.length; i++){
		if((i+1) % 3 == 0){
			if(str[i] != ':'){
				return false;
			}
		}else{
			if(!macFormat.test(str[i])){
				return false;
			}
		}
	}
	return true;
}

function bulidToolbar(){
        var macField=new Ext.form.TextField();
        
		var jtb = new Ext.Toolbar();
		var items = [];
		items[items.length] = { text: "@cmPoll.pollTask@: ",xtype: "label" ,margins : " 0 3 0 3"};
	    items[items.length] = { text:'<select id="sel_task" style="width:120px;"></select>',xtype: 'tbtext'};
		items[items.length] = '-';
		items[items.length] = { text: "MAC: ",xtype: "label" ,margins : " 0 3 0 3"};
        items[items.length] = macField;
		items[items.length] = '-';
	    items[items.length] = { 
	            xtype:"button",
	            text:"@COMMON.query@",
	            iconCls:"bmenu_find",
	            margins : " 0 3 0 3",
	            listeners:{
	                "click":function(){
	                	var mac = $.trim(macField.getValue());
	                	if(mac=="" || Validator.isFuzzyMacAddress(mac)){
	                		cmMonitorCmStore.load({
	                			params:{
	                				cmMac:mac,
	                				taskId:$("#sel_task").val()
		                        }
		                    });
	                	}else{
	                		window.parent.showMessageDlg("@COMMON.tip@","@cmPoll.macTip@");
	                	}		
	                }
	            }
	    };
	    items[items.length] = {
	            text : '@COMMON.refresh@',
	            iconCls : 'bmenu_refresh',
	            handler : function(){
	                window.location.reload();
	            }
	    };
	    
		jtb.add(items);
		jtb.render('toolbar');
		jtb.doLayout();
	}

	function bulidHistoryToolbar(){
		var jtb = new Ext.Toolbar({width : perfPanelWidth+10, border :true});
		var items = [
	     	"<span style='font-weight:bold'>@monitor.historyStat@</span>",
	     	'->',
	     	/*{ xtype: 'button', text: "@COMMON.query@",iconCls : 'bmenu_find',handler:query},*/
	     	'<div style="width:95px;"><a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span ><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a></div>',
	     	'->',
	     	{xtype: 'tbspacer', width: 2},
	     	'->',
	     	{ xtype: 'datetimefield',id: "endTime",enableKeyEvents:false, editable: false},
	     	'->',
	     	{ xtype: 'label', text: "@monitor.endTime@: "},
	     	'->',
	     	{xtype: 'tbspacer', width: 2},
	     	'->',
	     	{ xtype: 'datetimefield',id: "startTime",enableKeyEvents:true, editable: false},
	     	'->',
	     	{ xtype: 'label', text: "@monitor.startTime@: "},
	     	'->',
	     	{xtype: 'tbspacer', width: 50},
	     	'->',
	     	new Ext.Toolbar.SplitButton({
	    		text: "@monitor.monitorQuots@", menu: {items: [
	               {value:true,text:'@monitor.selectAll@',handler: chooseIndex},
                   {checked:true,value:"upChannelFlow",text:'@monitor.upChannelRate@',handler: chooseIndex},
                   {checked:true,value:"upChannelSpeed",text:'@monitor.upChannelBandWidth@',handler: chooseIndex},
                   {checked:true,value:"downChannelFlow",text:'@monitor.downChannelRate@',handler: chooseIndex},
                   {checked:true,value:"downChannelSpeed",text:'@monitor.downChannelBandWidth@',handler: chooseIndex},
                   {checked:true,value:"snr",text:'@monitor.snr@',handler: chooseIndex},
                   {checked:true,value:"sigQMicroreflections",text:'@monitor.microRelect@',handler: chooseIndex},
                   {checked:true,value:"sendPower",text:'@monitor.sendPower@',handler: chooseIndex},
                   {checked:true,value:"receivePower",text:'@monitor.recvPower@',handler: chooseIndex},
                   {checked:true,value:"sigQCorrecteds",text:'@monitor.corecError@',handler: chooseIndex},
                   {checked:true,value:"sigQUnerroreds",text:'@monitor.sigQUnerroreds@',handler: chooseIndex},
                   {value:false,text:'@monitor.deselectAll@',handler: chooseIndex}
                ],id:"indexMenu"}
	    	})
	    ];
		jtb.add(items);
		jtb.render('historyToolbar');
		jtb.doLayout();
		var timeCheck = function(f,n,o){
			var _startTime_ = Ext.getCmp("startTime").getValue();
			var _endTime_ = Ext.getCmp("endTime").getValue();
			if( !!_endTime_ && !!_startTime_){
				if( _startTime_ > _endTime_ ){
					//this.setValue("");
					return window.parent.showMessageDlg("@COMMON.tip@","@monitor.timeTip@");
				}
			}
		}
		var inputCheck = function(){
			if(this.validate()){
				timeCheck.call(this);
			}
		}
		Ext.getCmp("endTime").on("change" , timeCheck);
		Ext.getCmp("endTime").on("select" , timeCheck);
		Ext.getCmp("startTime").on("change" , timeCheck);
		Ext.getCmp("startTime").on("select" , timeCheck);
		Ext.getCmp("endTime").on("keyup", inputCheck);
		Ext.getCmp("startTime").on("keyup", inputCheck);
		
		nm3kPickData({
		    startTime : Ext.getCmp("startTime"),
		    endTime : Ext.getCmp("endTime"),
		    searchFunction : query
		})
	}
	
	function chooseIndex(o){
		var index = o.value;
		if(typeof index == 'boolean'){
			Ext.getCmp("indexMenu").findBy(function(item){
				if(typeof item.value != 'boolean'){
					item.setChecked(index);
					if(index){
						quots.add(item.value);
					}
				}
			});
			if(!index){
				quots = [];
			}
			return false;
		}
		var selected = o.checked;
		if(selected){
			quots.remove(index);
		}else{
			quots.add(index);
		}
		o.setChecked(!selected);
		return false;
	}

	function renderCmList(){
	    cmMonitorCmStore=new Ext.data.JsonStore({
            autoLoad : true,
            url : '/cmpoll/loadCmList.tv',
            root : 'data',
            fields : ['mac','ip','status']
        });
	    var cmListH = $("#cmList").outerHeight() - $("#cmListTit").outerHeight();
	    if(cmListH < 0){ cmListH = 100;}
		new Ext.grid.GridPanel({
		    stripeRows:true,region: "center",bodyCssClass: 'normalTable',
			height: cmListH,
	        store : cmMonitorCmStore,         
	        border:false,
	        renderTo: "cmListBody",
	        //title: "@cmPoll.cmList@",
	        hideHeaders:true,
	        viewConfig:{forceFit:true},
	        bbar:[{
	            xtype: 'tbtext',
                text:'<span>@cmPoll.all@:</span><span id="cm_total_num"></span><span>&nbsp;&nbsp;&nbsp;&nbsp;@monitor.online@:</span><span id="cm_online_num"></span>'
            }],
	        selModel : new Ext.grid.RowSelectionModel({singleSelect : true}),
			cm : new Ext.grid.ColumnModel([            
	             {header: "@cmPoll.cmList@" ,dataIndex:'mac',renderer: macRender, align: 'left',sortable: true,resizable: false,menuDisabled :true}
			]),
			listeners:{
				rowclick : showCm
			}
	    });
		
		cmMonitorCmStore.on("load",function(_store,records){
		    $("#cm_total_num").text(records.length);
		    var onlineNum=0;
		    $.each(records,function(){
		        if(this.data.status=='6'){
		            onlineNum++;
		        }
		    });
		    $("#cm_online_num").text(onlineNum);
		});
	}
	
	function macRender(v,m,r){
		if(isCmOnline(r.data.status)){ //online
			v = '<img alt="@monitor.online@" src="../images/fault/trap_on.png" border=0 align=absmiddle> '.concat(v)
		}else{ // offline
			v = '<img alt="@monitor.offline@" src="../images/fault/trap_off.png" border=0 align=absmiddle> '.concat(v)
		}//其他状态不管
		return v;
	}
	
	
	function createInfoTable(){
		 	Ext.grid.PropertyGrid.prototype.setSource = function(source) {
		        delete this.propStore.store.sortInfo;
		        this.propStore.setSource(source);
		  	}; 
		    return new Ext.grid.PropertyGrid({
		    	hideHeaders:true,
		    	border: false,		    	
		    	height : 220,//实际上只有197px,但是为了不出现滚动条;
		    	listeners:{
		    		beforeedit: function(e) {
		    	        e.cancel = true;
		    	        return false;
		    	    },
			    	contextmenu : function(e){
						e.preventDefault();		
					},
					/*rowdblclick:function(g,r,e ){
		            	//-------双击时默认复制行内容    
		                var thisText = e.getTarget().innerHTML;
		                //-----属性值可能是number型，故强转string
		                clipboardData.setData("text",thisText);
		                window.parent.showMessageDlg( "@COMMON.tip@", "@COMMON.copyOk@@COMMON.maohao@" + thisText );
		            },*/
		            render: function(proGrid) {
		                var view = proGrid.getView();
		                var store = proGrid.getStore();
		                proGrid.tip = new Ext.ToolTip({
		                    target: view.mainBody,
		                    delegate: '.x-grid3-row',
		                    trackMouse: true,
		                    renderTo: document.body,
		                    listeners: {
		                        beforeshow: function updateTipBody(tip) {
		                            var rowIndex = view.findRowIndex(tip.triggerElement);
		                            tip.body.dom.innerHTML = store.getAt(rowIndex).get('value');
		                        }
		                    }
		                });
		            }
			   	},
		        frame: false
		    });
	}