<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
</Zeta:Loader>
<style type="text/css">
.timeLineDl { 
    float:left; 
    position:absolute; 
}
.timeLineDl dd{ 
    float:left; 
    overflow:hidden; 
    text-align:left; 
    line-height:2em;
}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var productType = ${productType};
var cmcId='${cmcId}';
var entityId='${entityId}';
var cmcPolicyId='${policyId}';// CC使用时间策略

// 删除记录
function deletePolicy(policyId){
    window.top.showConfirmDlg("@COMMON.tip@","@removeRecordTip@",function(t){
        if(t=='yes'){
            window.top.showWaitingDlg("@COMMON.wat@","@CMC.text.doingdel@");
            $.ajax({
                url: '/cmc/loadbalance/deleteLoadBalPolicy.tv',
                type: 'POST',
                cache:false,
                dataType:'json',
                data:{
                    "policyId":policyId
                },
                success: function(data) {
                    window.top.closeWaitingDlg();
                    if(!data.success){
                        window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");
                    }
                    
                    __refreshPolicyList();
                }
            });
        }
    });
}

// 进入修改界面
function enterPolicyModifyPage(policyId){
    var win = window.top.createDialog('modifyLoadBalPolicyWin', "@modifyPolicy@",600, 355, 
            "/cmc/loadbalance/enterLoadBalPolicyModifyPage.tv?policyId="+policyId, null, true, true);
    win.on('close', function(){
        __refreshPolicyList();
    });
}
//应用或解除应用的生效时间策略
function updateCcmtsPolicy(cmcId,policyId){
	if(policyId==null||policyId==undefined){//表示解除bind，数据库id
		policyId=0;
	}
	window.top.showWaitingDlg("@COMMON.wat@","@CMC.text.beingModifiedConfig@");
    $.ajax({
        url: '/cmc/loadbalance/modifyCcmtsPolicy.tv',
        type: 'POST',
        cache:false,
        dataType:'json',
        data:{
            "policyId":policyId,
            "cmcId":cmcId
        },
        success: function(data) {
            window.top.closeWaitingDlg();
            if(data.success){
            	top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">@operationSuccess@</b>'
       			});
                if(policyId!=0){
                	window.top.closeModalDlg();
                }else{
                	//__refreshPolicyList();//vp.forceLayout(true);
                	window.self.location.reload();
                }
            }else{
                window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");
            }
        }
    });
}
$(function() {
    var cm, store,grid,toolbar,sm;
    var winWidth=$(window).width()-10;
    var periodColWidth=winWidth*0.6-10;
    
    //------------------------------方法定义-------------------
    // 全局的
    window.self.__refreshPolicyList=function(){
        store.load();
    }
    
    /*
    * 渲染时间段，此数据在后台经过了去重叠及合并处理
    * disSections格式 08:00-12:00,12:30-13:00,...
    */
    function renderSections(disSections,m,r){
         var validColor="#5DF25A;";
         var invalidColor="#EDF25A;";
         var _policyType=r.data.policyType;
         var _ses=disSections?disSections.split(","):[];
         var $div=$("<div>");
         var $dl=$("<dl class='timeLineDl' style='z-index:2;'>");
         var w=parseInt(periodColWidth/24);
         for(var i=0;i<24;i++){
             $dl.append("<dd style='width:"+w+"px;height:20px;'>"+i+"</dd>");
         }
         
         $div.append($dl);
         
         for(var i=0;i<_ses.length;i++){
            var startTime=_ses[i].split('-')[0];
            var endTime=_ses[i].split('-')[1];
            
            var startTimeHour=parseInt(startTime.split(":")[0],10);
            var startTimeMinute=parseInt(startTime.split(":")[1],10);
            var endTimeHour=parseInt(endTime.split(":")[0],10);
            var endTimeMinute=parseInt(endTime.split(":")[1],10);
            
            var startPos=(startTimeHour+startTimeMinute/60)*w;
            var periodW=(endTimeHour+endTimeMinute/60)*w-startPos+3;
            $div.append('<div style="position:absolute; top:0px; left:'+startPos+'px; height:20px; background-color:#EDF25A; width:'+periodW+'px; z-index:1;"></div>');
            $div.append('<div style="position:absolute; top:0px; left:'+startPos+'px; height:20px; width:'+periodW+'px; z-index:3;" title="'+startTime+'-'+endTime+'">&nbsp;</div>');
         }
         if(_policyType==2){
             return "<div style='position:relative;height:20px;background-color:"+invalidColor+"'>"+$div.html()+"</div>";
         }else{
             return "<div style='position:relative;height:20px;background-color:"+validColor+"'>"+$div.html()+"</div>";
         }
    }
    
    //---------------------------------------------------------
    sm=new Ext.grid.RowSelectionModel();
    
    cm = new Ext.grid.ColumnModel([
    {
    	header : "ID",
        dataIndex : 'docsLoadBalPolicyId',
        width : winWidth*0.05,
        align : 'center'
    }, {
        header : "@policy@",
        dataIndex : 'policyType',
        width : winWidth*0.15,
        align : 'center',
        renderer:function(v,m,r){
            var _policyId=r.data.policyId;
            var s="";
            if(v=='1'){// 一直有效
                s = "@policyEnabled@";
            }else if(v=='2'){// 一直无效
                s = "@policyDisabled@";
            }else if(v=='3'){// 周期性无效
                s = "@disabedPeriod@";    
            }
            
            if(_policyId==cmcPolicyId){
                s="<img src='/images/silk/flag_red.png' align='absmiddle'>"+s;
            }
            return s;
        }
    }, {
        header : "@timeSection@",
        width : periodColWidth,
        dataIndex : 'disSections',
        align : 'center'
        /*,
        renderer:renderSections
        */
    }, {
        header : "@COMMON.opration@",
        dataIndex:'',
        id:'operation',
        width : winWidth*0.2,
        align : 'center',
        renderer:function(v,m,r){
            var _policyId=r.data.policyId;
            var _html="";
            if(_policyId!=cmcPolicyId){
            	if(operationDevicePower){
                    _html+="<a href='javascript:;' onclick='deletePolicy("+_policyId+")''>@COMMON.delete@</a> / ";            		
            	}
                _html+="<a href='javascript:;' onclick='enterPolicyModifyPage("+_policyId+")'>@COMMON.modify@</a>";
            }else{
            	if(operationDevicePower){
              		_html+="<a href='javascript:;' title='@dismissPolicyApplied@' onclick='updateCcmtsPolicy("+cmcId+")'>@dismissPolicyApplied@</a>";
            	}else{
            		_html+="@unOper@";
            	}
            }
            return _html;
        }
    }]);

    store = new Ext.data.JsonStore({
        url : '/cmc/loadbalance/getLoadBalPolicyList.tv?entityId='+entityId,
        root : 'data',
        fields : [ 'policyId','docsLoadBalPolicyId', 'policyType', 'disSections' ]
    });

    toolbar = new Ext.Toolbar(
            {
                items : [
                        {
                        	cls: 'mL10',
                            iconCls : 'bmenu_new',
                            text : '@COMMON.add@',
                            handler : function() {
                            	if(EntityType.isCcmtsWithAgentType(productType)&&store.data.length>=1){
                                    window.top.showMessageDlg("@COMMON.tip@", "@loadbalance.onlyOnePolicyOnCC8800b@");
                            	}else{
                            		var win = window.top.createDialog('addLoadBalPolicyWin', '@addPolicy@', 600, 355, 
                                            "/cmc/loadbalance/enterLoadBalPolicyAddPage.tv?entityId="+entityId, null, true, true);
                                    win.on('close', function(){
                                        __refreshPolicyList();
                                    });	
                            	}
                            }
                            
                        },
                        {
                        	iconCls : 'bmenu_save',
                            text : '@apply@',
                            handler : function() {
                                var rs=sm.getSelected();
                                if(rs){
                                	updateCcmtsPolicy(cmcId,rs.data.policyId);
                                }
                            },
                            disabled:!operationDevicePower
                        }/* ,
                        {
                        	icon:'/images/cut.png',
                        	text:'@dismissPolicyApplied@',
                        	handler:function(){
                        		updateCcmtsPolicy(cmcId);
                        	}
                        } */,
                        {
                        	iconCls : 'bmenu_equipment',
                            text : '@COMMON.fetch@',
                            handler : function() {
                            	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
                                $.ajax({
                                    url: '/cmc/loadbalance/syncLoadBalPolicy.tv',
                                    type: 'POST',
                                    cache:false,
                                    dataType:'json',
                                    data:{
                                        "cmcId":cmcId
                                    },
                                    success: function(data) {
                                        window.top.closeWaitingDlg();
                                        if(data.success){
                                            //__refreshPolicyList();
                                            window.self.location.reload();
                                        }else{
                                            window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");
                                        }
                                    }
                                });
                            }
                        },
                        {
                        	iconCls : 'bmenu_refresh',
                            text : '@COMMON.refresh@',
                            handler : function() {
                                __refreshPolicyList();
                            }
                        }
                        ]
            });

    grid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		viewConfig:{
   			forceFit: true
   		},
        store : store,
        border : false,
        renderTo : document.body,
        tbar : toolbar,
        height : $(window).height(),
        cm : cm,
        sm : sm
    });

    new Ext.Viewport({layout: 'fit', items: [grid]});
    window.self.__refreshPolicyList();
});
</script>
</head>
<body>
</body>
</Zeta:HTML>