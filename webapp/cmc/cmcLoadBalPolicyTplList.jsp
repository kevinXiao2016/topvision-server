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
    /*text-align:left; */
    text-align:center; 
    line-height:2em;
}
</style>
<script type="text/javascript">
// 删除记录
function deleteTpl(policyTplId){
    window.top.showConfirmDlg("@COMMON.tip@","@removeRecordTip@",function(t){
        if(t=='yes'){
            $.ajax({
                url: '/cmc/loadbalance/deleteLoadBalPolicyTpl.tv',
                type: 'POST',
                cache:false,
                dataType:'json',
                data:{
                    "policyTplId":policyTplId
                },
                success: function(data) {
                    if(data.success){
                    	 top.afterSaveOrDelete({
                				title: '@COMMON.tip@',
                				html: '<b class="orangeTxt">@resources/COMMON.deleteSuccess@</b>'
                		});
                    }else{
                    	 window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");
                    }
                    
                    __refreshPolicyTplList();
                }
            });
        }
    });
}

// 进入修改界面
function enterPolicyTplModifyPage(tplId){
    window.top.showModalDlg("@modifyPolicyTpl@",800,500,"/cmc/loadbalance/enterLoadBalPolicyTplModifyPage.tv?policyTplId="+tplId,function(){
        __refreshPolicyTplList();
    });
}

var vp;
$(function() {
	
    var cm, store,grid,toolbar,sm;
    var winWidth=$(window).width()-10;
    var periodColWidth=winWidth*0.7-10;
    var w=parseFloat(periodColWidth/24);
    
    
    //------------------------------方法定义-------------------
    // 全局的
    window.self.__refreshPolicyTplList=function(){
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
         
         for(var i=0;i<=24;i++){
        	 if(i==24){
        		 $dl.append("<dd style='width:"+w*4/5+"px;height:20px;text-align:right;float:right; '>24</dd>");
        	 }else if(i==0){
        		 $dl.append("<dd style='width:"+w/2+"px;height:20px;text-align:left; '>"+i+"</dd>");
        	 }else{
        		 $dl.append("<dd style='width:"+w+"px;height:20px;'>"+i+"</dd>");
        	 }
         }
         $div.append($dl);
         
         for(var i=0;i<_ses.length;i++){
            var startTime=_ses[i].split('-')[0];
            var endTime=_ses[i].split('-')[1];
            
            var startTimeHour=parseInt(startTime.split(":")[0],10);
            var startTimeMinute=parseInt(startTime.split(":")[1],10);
            var endTimeHour=parseInt(endTime.split(":")[0],10);
            var endTimeMinute=parseInt(endTime.split(":")[1],10);
            var startPos = 0;
            startPos =(startTimeHour+startTimeMinute/60)*w;
            if(startTimeHour!=0&&startTimeHour>=10){
            	startPos =startPos+w/12;
            }else if(startTimeHour!=0&&startTimeHour<=9){
            	startPos =startPos+w/18;
            }
            
            var periodW=(endTimeHour+endTimeMinute/60)*w-startPos+w/12;
			if(endTime=="24:00"){
            	//periodW -=w/8
            	periodW +=10;
            }
            $div.append('<div style="position:absolute; top:0px; left:'+startPos+'px; height:20px; background-color:#EDF25A; width:'+periodW+'px; z-index:1;"></div>');
            $div.append('<div style="position:absolute; top:0px; left:'+startPos+'px; height:20px; width:'+periodW+'px; z-index:3;" title="'+startTime+'-'+endTime+'">&nbsp;</div>');
         }
         var res=""; 
         if(_policyType==2){
             res =  "<div style='position:relative;height:20px;background-color:"+invalidColor+"'>"+$div.html()+"</div>";
         }else{
             res =  "<div style='position:relative;height:20px;background-color:"+validColor+"'>"+$div.html()+"</div>";
         }
         //alert(res)
         return res;
    }
    
    //---------------------------------------------------------
    sm=new Ext.grid.RowSelectionModel();
    
    cm = new Ext.grid.ColumnModel([
    {
        header : "@COMMON.name@",
        dataIndex : 'policyName',
        width : winWidth*0.1,
        align : 'center'
    }, {
        header : "@policy@",
        dataIndex : 'policyType',
        width : winWidth*0.1,
        align : 'center',
        renderer:function(v){
            if(v=='1'){// 一直有效
                return "@policyEnabled@";
            }else if(v=='2'){// 一直无效
                return "@policyDisabled@";
            }else if(v=='3'){// 周期性无效
                return "@disabedPeriod@";
            }
            
            return "";
        }
    }, {
        header : "@timeSection@",
        width : periodColWidth,
        dataIndex : 'disSections',
        align : 'center',
        renderer:renderSections
    }, {
        header : "@COMMON.opration@",
        width : winWidth*0.1,
        align : 'center',
        renderer:function(v,m,r){
            var _policyTplId=r.data.policyTplId;
            var _html="";
            _html+="<a href='javascript:;' onclick='enterPolicyTplModifyPage("+_policyTplId+")'>@COMMON.modify@</a> / ";
          	_html+="<a href='javascript:;' onclick='deleteTpl("+_policyTplId+")'>@COMMON.delete@</a>";
            //_html+="<img src='/images/delete.gif' title='@COMMON.delete@' onclick='deleteTpl("+_policyTplId+")' style='cursor: pointer;'>&nbsp;";
            //_html+="<img src='/images/edit.gif' title='@COMMON.modify@' onclick='enterPolicyTplModifyPage("+_policyTplId+")' style='cursor: pointer;'>";
            return _html;
        }
    }]);

    store = new Ext.data.JsonStore({
        url : '/cmc/loadbalance/getLoadBalPolicyTplList.tv',
        root : 'data',
        fields : [ 'policyTplId', 'policyName', 'policyType', 'disSections' ]
    });

    toolbar = new Ext.Toolbar(
            {
                items : [
                        {                 
                        	cls: "mL10",
                            iconCls : 'bmenu_new',
                            text : "@COMMON.add@",
                            handler : function() {
                                // 进入添加界面
                                window.top.showModalDlg("@addPolicyTpl@",800,500,"/cmc/loadbalance/enterLoadBalPolicyTplAddPage.tv",function(){
                                    __refreshPolicyTplList();
                                });
                            }
                        },
                        '-',
                        {
                            iconCls: 'bmenu_refresh',
                            text : "@COMMON.refresh@",
                            handler : function() {
                                __refreshPolicyTplList();
                            }
                        },
                        "->",
                        "<span class='blueTxt'>@colorTip@:</span>",
                        "<div style='width:40px;background-color:#5DF25A;padding:3px;'>@valid@</div>",
                        "<div style='width:40px;background-color:#EDF25A;padding:3px;'>@invalid@</div>"
                        ]
            });

    grid = new Ext.grid.GridPanel({
        store : store,
        border : false,
        renderTo : document.body,
        tbar : toolbar,
        height : $(window).height(),
        bodyCssClass : "normalTable",
        cm : cm,
        sm : sm,
        viewConfig:{
        	forceFit: true
        },
        loadMask : true, 
        listeners: {  
            render: function(grid){  
                var store = grid.getStore();   
                var view = grid.getView();  
                grid.tip = new Ext.ToolTip({       
                    target: view.mainBody,    // The overall target element.        
                    delegate: '.x-grid3-row', // Each grid row causes its own seperate show and hide.        
                    trackMouse: true,         // Moving within the row should not hide the tip.        
                    renderTo: document.body,  // Render immediately so that tip.body can be referenced prior to the first show.        
                    listeners: {              // Change content dynamically depending on which element triggered the show.        
                        beforeshow: function updateTipBody(tip) {       
                               var rowIndex = view.findRowIndex(tip.triggerElement);  
                               var word = store.getAt(rowIndex).get("policyName") || "";
                               tip.body.dom.innerHTML = '<div style="word-wrap:break-word;">'+word+'</div>';  
                        }       
                    }       
                });  
            }  
        }  
    });

    vp = new Ext.Viewport({layout: 'fit', items: [grid]});
    window.self.__refreshPolicyTplList();
});
</script>
</head>
<body>
</body>
</Zeta:HTML>