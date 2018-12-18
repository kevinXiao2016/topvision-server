<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery	
	library js/jquery/Nm3kTabBtn
</Zeta:Loader>

<style type="text/css">
.dataTable th.firstThTd, .dataTable td.firstThTd{ border-right:none;}
.dataTable td.firstThTd{border-top:none; border-bottom:none; background:#F9F9F9;}

.dataTable th.clearLR, .dataTable td.clearLR{ border-left:none; border-right:none;}
.dataTable th.clearL, .dataTable td.clearL{ border-left:none;}
.dataTable td.clearAll{ border:none; background:#F9F9F9;}
.dataTable td.yellowTxt{ color:#B3711A;}
table.dataTable tr.light{ background:#F9F9F9;}
table.dataTable tr.completedPage{ background: #eee;}
table.dataTable tr.hightBg{ background: #eee;}

.dataTable tr.displayNone3{ display:none;}
.dataTable tr.displayNone2{ display:none;}
.dataTable tr.displayNone{ display:none;}
.dataTable tr.one .yellowTxt span{font-weight:bold; padding-left:5px; color:#0267AF;}
.dataTable tr.two .yellowTxt span{padding-left:20px; }
.dataTable tr.three .yellowTxt span{padding-left:40px; }
.dataTable tr.four .yellowTxt span{padding-left:60px; }
 
table.dataTable td.openWithSub{background:#F9F9F9 url(../css/white/tree/openWithSub.gif) no-repeat 0 0; cursor:pointer;}
table.dataTable td.openWithoutSub{background:#F9F9F9 url(../css/white/tree/openWithoutSub.gif) no-repeat 0 0; cursor:pointer;}
table.dataTable td.closeWithSub{background:#F9F9F9 url(../css/white/tree/closeWithSub.gif) no-repeat 0 0; cursor:pointer;}
table.dataTable td.closeWithoutSub{background:#F9F9F9 url(../css/white/tree/closeWithoutSub.gif) no-repeat 0 0; cursor:pointer;}
table.dataTable td.shu{ background:#F9F9F9 url(../css/white/tree/shu.gif) repeat-y 7px 0}
table.dataTable td.heng{ background:#F9F9F9 url(../css/white/tree/heng.gif) repeat-x 0 14px}
table.dataTable td.hengWithSub{ background:#F9F9F9 url(../css/white/tree/hengWithSub.gif) no-repeat 0 0}
table.dataTable td.lastNodeWithSub{background:#F9F9F9 url(../css/white/tree/lastNodeWithSub.gif) no-repeat 0 0}
table.dataTable td.lastNodeWithoutSub{background:#F9F9F9 url(../css/white/tree/lastNodeWithoutSub.gif) no-repeat 0 0}



</style>
<script type="text/javascript" src="../js/jquery/Nm3kTabBtn.js"></script>
<script type="text/javascript">
function completeImg(str){
	if(str == "false"){
		return "<img src='../images/wrong.png' />" ;
	}else if(str == "true"){
		return "<img src='../images/yes.png' />" ;
	}else{
		return str
	}
}

//week表示现在是第几周，如果是0,则表示全部;
var week = 0;
var manNum = 0;//表示人所在按钮组的index;
function showTime(index){
	week = index;
	showJob(manNum);
}

function showJob(index){
	manNum = index;
	var leeArr = 0;//共完成的页面数量;
	var shouldCompleted = 0; //共分配需要完成的页面数量;
	$(".dataTable tr").removeClass("completedPage");
	
	if(week == 0){//时间选择全部的情况;
		if(index == 0){//人员也选择全部;
			$(".dataTable tr").each(function(i){
		    	var jsCompleted = $(this).find("td.jsCompleted").attr("alt");
		    	if(jsCompleted == 'true'){
					leeArr++;
					$(this).addClass("completedPage");
				}
	    	});
	    	var allNum = $(".dataTable tr").length;
	    	var msg = "2.0 目前共分配了 <b style='color:#f00'>"+ allNum +"</b>个页面，共完成了 <b style='color:#f00'>" + leeArr + " </b>个页面";	    	
		}else{//统计具体某个人全部工作量;
			var man = ($("#putBtnGroup .selected").length > 0) ? $("#putBtnGroup .selected").text() : $("#putBtnGroup .selectedLast").text();
			$(".dataTable tr").each(function(i){
	    		var jsName = $(this).find("td.jsName").text();
	    		var jsCompleted = $(this).find("td.jsCompleted").attr("alt");
	    		
	    		if(jsName == man){
	    			shouldCompleted++;
	    			if(jsCompleted == 'true'){
	    				leeArr++;
	    				$(this).addClass("completedPage");
	    			}
	    		}
	    	})
	    	msg =  man +"共分配了<b style='color:#f00'> " + shouldCompleted + "</b> 个页面,共完成 <b style='color:#f00'> " + leeArr + "</b> 个页面";
					
		}		
	}else{//选择某一周的情况;		
		if(index == 0){//人员选择全部;
			$(".dataTable tr").each(function(i){
		    	var jsCompleted = $(this).find("td.jsCompleted").attr("alt");
		    	var jsTime = $(this).find("td.jsTime").text();
		    	if(jsCompleted == 'true' && jsTime == ("第"+ week +"周")){
					leeArr++;
					$(this).addClass("completedPage");
				}
	    	});
			msg = "第<b style='color:#f00'> "+ week +"</b> 周共完成了<b style='color:#f00'> " + leeArr + "</b>个页面";
		}else{//选择具体某个人;
			var man = ($("#putBtnGroup .selected").length > 0) ? $("#putBtnGroup .selected").text() : $("#putBtnGroup .selectedLast").text();
			$(".dataTable tr").each(function(i){
	    		var jsName = $(this).find("td.jsName").text();
	    		var jsCompleted = $(this).find("td.jsCompleted").attr("alt");
	    		var jsTime = $(this).find("td.jsTime").text();
	    		if(jsName == man && jsCompleted == 'true' && jsTime=="第"+ week +"周"){
	    			leeArr++;
	    			$(this).addClass("completedPage");
	    		}
	    	})
	    	msg = man +"第<b style='color:#f00'> "+ week +"</b> 周共完成<b style='color:#f00'> " + leeArr + "</b> 个页面";
		}		
	}
	
	$("#putResult").html(msg);
	
	
}

$(function(){
	var tab1 = new Nm3kTabBtn({
	    renderTo:"putBtnGroup",
	    callBack:"showJob",
	    tabs:["全部","李翔","张东明"]

	});
	tab1.init();
	
	var tab2 = new Nm3kTabBtn({
	    renderTo:"putTime",
	    callBack:"showTime",
	    tabs:["全部","第1周","第2周","第3周","第4周","第5周"]

	});
	tab2.init();

	


	
	
	$.ajax({
        url: "v2.xml",cache:false, 
        dataType: 'xml',
        type: 'GET',
        timeout: 2000,
        error: function(xml)
        {
            alert("加载XML 文件出错！");
        },
        success: function(xml)
        {
        	var str = '';
        	str += '<table class="dataTable zebra noWrap" width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">';
        	str += '<thead>';
        	str += '    <tr>';
        	str += '        <th width="15" class="firstThTd">&nbsp;</th>';
        	str += '        <th width="15" class="clearLR">&nbsp;</th>';
        	str += '        <th width="15" class="clearLR">&nbsp;</th>';
        	str += '        <th width="15" class="clearL">&nbsp;</th>';	           
        	str += '        <th>页面名称 </th>';
        	str += '        <th width="120">所属板块</th>';
        	str += '        <th width="100">修改人</th>';
        	str += '        <th width="60">完成时间</th>';
        	str += '        <th width="60">完成情况</th>';
        	str += '        <th width="60">备注</th>';
        	str += '    </tr>';
        	str += '</thead>';	
        	var tbodyNum = $(xml).children().children().length;//记录有几个板块，也就是有几个tbody;        	
        	$(xml).children().children().each(function(i){
	        	var oneName = $(this).attr("name");
	        	var oneBelong = $(this).attr("belong");
	        	var oneMan = $(this).attr("man");
	        	var oneTime = $(this).attr("time");
	        	var oneComplete = $(this).attr("complete");
	        	var oneRemark = $(this).attr("remark");
	        	
	        	str += '<tbody>';
	        		str += '<tr class="light one">';
	        		if(i == tbodyNum-1){//最后一个
	        			str += '<td class="firstThTd closeWithoutSub"></td>';
	        		}else{
	        			str += '<td class="firstThTd closeWithSub"></td>';
	        		}
	        		str += '    <td class="clearAll hengWithSub"></td>';
		      		 /* str += '    <td class="clearAll heng"></td>';
		      		 str += '    <td class="clearAll heng"></td>';  */           
	        		str += '    <td colspan="3" class="txtLeft wordBreak yellowTxt"><span>'+ oneName +'</span></td>';
	        		str += '    <td class="txtCenter">'+ oneBelong +'</td>';
	        		str += '    <td class="txtCenter jsName">'+ oneMan +'</td>';
	        		str += '    <td class="txtCenter jsTime">'+ oneTime +'</td>';
	        		str += '    <td class="txtCenter jsCompleted" alt="'+ oneComplete +'">'+ completeImg(oneComplete) +'</td>';
	        		str += '    <td class="txtCenter">'+ oneRemark +'</td>';
	        		str += '</tr>';
	        		
	        		var secondTrNum = $(this).children().length; 	        		
	        		$(this).children().each(function(j){
	        			var twoName = $(this).attr("name");	        			
	    	        	var twoMan = $(this).attr("man");
	    	        	var twoTime = $(this).attr("time");
	    	        	var twoComplete = $(this).attr("complete");
	    	        	var twoRemark = $(this).attr("remark");
	    	        	
	        			str += '<tr class="light two">';
	        			if(i == tbodyNum-1){//最后一个,判断第一个td有没有竖线;
	        				str += '<td class="firstThTd"></td>';
	    	        	}else{
	    	        		str += '<td class="firstThTd shu"></td>';
	    	        	}
	        			if($(this).children().length > 0){//如果存在第三级别;
	        				if(j == secondTrNum-1){//最后一个
	        					str += '<td class="clearAll closeWithoutSub"></td>';	
	        				}else{
	        					str += '<td class="clearAll closeWithSub"></td>';
	        				}	        				
	        				str += '<td class="clearAll hengWithSub"></td>';
	        			}else{//不存在第三级别;
	        				if(j == secondTrNum-1){//最后一个
	        					str += '<td class="clearAll lastNodeWithoutSub"></td>';
	        				}else{
	        					str += '<td class="clearAll lastNodeWithSub"></td>';
	        				}	        				
	        				str += '<td class="clearAll heng"></td>';
	        			};//end if else;
	        			
	        			
	        			str += '	<td class="clearAll heng"></td>';	            
	        			str += '	<td class="txtLeft wordBreak yellowTxt"><span>'+ twoName +'</span></td>';
	        			str += '	<td class="txtCenter">'+ oneBelong +'</td>';
	        			str += '	<td class="txtCenter jsName">'+ twoMan +'</td>';
	        			str += '	<td class="txtCenter jsTime">'+ twoTime +'</td>';
	        			str += '	<td class="txtCenter jsCompleted"  alt="'+ twoComplete +'">'+ completeImg(twoComplete) +'</td>';
	        			str += '	<td class="txtCenter">'+ twoRemark +'</td>';
	        			str += '</tr>';
	        			
	        			var thirdTrNum = $(this).children().length; 
	        			$(this).children().each(function(k){
	        				var threeName = $(this).attr("name");	        			
		    	        	var threeMan = $(this).attr("man");
		    	        	var threeTime = $(this).attr("time");
		    	        	var threeComplete = $(this).attr("complete");
		    	        	var threeRemark = $(this).attr("remark");
		    	        	
							str += '<tr class="light three">';
							if(i == tbodyNum-1){//最后一个
								str += '    <td class="firstThTd"></td>';
			        		}else{
			        			str += '    <td class="firstThTd shu"></td>';
			        		}
							
							//判断上一级别是不是最后一个
							//先判断我所在的父一级别也就是第二级别所在的index;
							//在判断第二级别有几个孩子;
							var parentNum = $(this).parent().index();
							
							if(parentNum == (secondTrNum - 1)){
								str += '	<td class="clearAll"></td>';
							}else{
								str += '	<td class="clearAll shu"></td>';
							}
							
							
							if($(this).children().length > 0){//如果存在第4级别;
								if(k == thirdTrNum-1){//最后一个
									str += '	<td class="clearAll closeWithoutSub"></td>';
								}else{
									str += '	<td class="clearAll closeWithSub"></td>';
								}								
								str += '	<td class="clearAll hengWithSub"></td>';
							}else{
								if(k == thirdTrNum-1){//最后一个
									str += '	<td class="clearAll lastNodeWithoutSub"></td>';
								}else{
									str += '	<td class="clearAll lastNodeWithSub"></td>';	
								}								
								str += '	<td class="clearAll heng"></td>';
							}
								            
							str += '	<td class="txtLeft wordBreak yellowTxt"><span>'+ threeName +'</span></td>';
							str += '	<td class="txtCenter">'+ oneBelong +'</td>';
							str += '	<td class="txtCenter jsName">'+ threeMan +'</td>';
							str += '	<td class="txtCenter jsTime">'+ threeTime +'</td>';
							str += '	<td class="txtCenter jsCompleted"  alt="'+ threeComplete +'">'+ completeImg(threeComplete) +'</td>';
							str += '	<td class="txtCenter">'+ threeRemark +'</td>';
							str += '</tr>';
							
							var fourthTrNum = $(this).children().length;
							$(this).children().each(function(m){
								var fourName = $(this).attr("name");	        			
			    	        	var fourMan = $(this).attr("man");
			    	        	var fourTime = $(this).attr("time");
			    	        	var fourComplete = $(this).attr("complete");
			    	        	var fourRemark = $(this).attr("remark");
								
			    	        	str +='<tr class="light four">';
			    	        	if(i == tbodyNum-1){//最后一个
			    	        		str +='    <td class="firstThTd"></td>';
			    	        	}else{
			    	        		str +='    <td class="firstThTd shu"></td>';
			    	        	}
			    	        	
			    	        	//判断上一级别是不是最后一个
								//先判断我所在的父一级别也就是第二级别所在的index;
								//在判断第二级别有几个孩子;
								var parentParentNum = $(this).parent().parent().index();								
								if(parentParentNum == (secondTrNum - 1)){
									str +='    <td class="clearAll"></td>';
								}else{
									str +='    <td class="clearAll shu"></td>';
								}
			    	        	
								var parentNum = $(this).parent().index();
								if(parentNum == (thirdTrNum - 1)){
									str +='    <td class="clearAll"></td>';
								}else{
									str +='    <td class="clearAll shu"></td>';
								}
			    	        	
			    	        	if(m == (fourthTrNum-1)){//第四级别,最后一个;
			    	        		str +='    <td class="clearAll lastNodeWithoutSub"></td>';
			    	        	}else{
			    	        		str +='    <td class="clearAll lastNodeWithSub"></td>';
			    	        	}
			    	        	str +='    <td class="txtLeft wordBreak yellowTxt"><span>'+ fourName +'</span></td>';
			    	        	str +='    <td class="txtCenter">'+ oneBelong +'</td>';
			    	        	str +='    <td class="txtCenter jsName">'+ fourMan +'</td>';
			    	        	str +='    <td class="txtCenter jsTime">'+ fourTime +'</td>';
			    	        	str +='    <td class="txtCenter jsCompleted" alt="'+ fourComplete +'">'+ completeImg(fourComplete) +'</td>';
			    	        	str +='    <td class="txtCenter">'+ fourRemark +'</td>';
			    	        	str +='</tr>';
			    	        	
							});//end each 第四级别;							
	        			});//end each 第三级别;
	        		});//end each 第二级别;
	        		
	        	str+= '</tbody>';
        	});//end each 第一级别;
        	str += '</table>';
        	$("#putTable").html(str);
        	
            /* $(xml).find("node").each(function(i)
            {
                var nodeName = $(this).attr("name");
                alert(nodeName)
            }); */
        }
    });//end $.ajax;	
})//end document.ready;
</script>

</head>
<body class="whiteToBlack">
	<div id="putBtnGroup"></div>
	<div id="putTime" class="pL20 floatLeft">&nbsp;</div>
	<div id="putResult" class="clearBoth pT5">&nbsp;</div>
	
	
	<div id="putTable" class="clearBoth pT10"></div>
	<%-- <table class="dataTable zebra noWrap" width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">
	    <thead>
	        <tr>
	            <th width="16" class="firstThTd">&nbsp;</th>
	            <th width="16" class="clearLR">&nbsp;</th>
	            <th width="16" class="clearLR">&nbsp;</th>
	            <th width="16" class="clearL">&nbsp;</th>	           
	            <th>页面名称 </th>
	             <th width="120">所属板块</th>
	            <th width="100">修改人</th>
	            <th width="60">完成时间</th>
	            <th width="60">完成情况</th>
	            <th width="60">备注</th>
	        </tr>
	    </thead>	   
	    <tbody>
	        <tr class="light one">
	            <td class="firstThTd closeWithoutSub"></td>
	            <td class="clearAll hengWithSub"></td>
	            <td class="clearAll heng"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>设备管理</span></td>
	            <td class="txtCenter">02 设备管理</td>	            
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll lastNodeWithSub"></td>
	            <td class="clearAll heng"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>设备视图</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll lastNodeWithSub"></td>
	            <td class="clearAll heng"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>资源列表</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	         <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll closeWithSub"></td>
	            <td class="clearAll hengWithSub"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>Epon设备列表</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	         <tr class="light three">
	            <td class="firstThTd"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll closeWithSub"></td>
	            <td class="clearAll hengWithSub"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>OLT设备列表</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light four">
	            <td class="firstThTd"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll lastNodeWithSub"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>OLT设备列表 增加</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	         <tr class="light four">
	            <td class="firstThTd"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll lastNodeWithoutSub"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>OLT设备列表 删除</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        
	        <tr class="light three">
	            <td class="firstThTd"></td>
	            <td class="clearAll shu"></td>
	            <td class="clearAll lastNodeWithoutSub"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>ONU设备列表</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll lastNodeWithSub"></td>
	            <td class="clearAll heng"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>新建设备</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll lastNodeWithSub"></td>
	            <td class="clearAll heng"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>批量拓扑</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light two">
	            <td class="firstThTd"></td>
	            <td class="clearAll closeWithoutSub"></td>
	            <td class="clearAll hengWithSub"></td>
	            <td class="clearAll heng"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>批量拓扑a</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	        <tr class="light three">
	            <td class="firstThTd"></td>
	            <td class="clearAll"></td>
	            <td class="clearAll closeWithoutSub"></td>
	            <td class="clearAll hengWithSub"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>批量拓扑b</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	         <tr class="light three">
	            <td class="firstThTd"></td>
	            <td class="clearAll"></td>
	            <td class="clearAll "></td>
	            <td class="clearAll lastNodeWithoutSub"></td>	            
	            <td class="txtLeft wordBreak yellowTxt"><span>批量拓扑b</span></td>
	            <td class="txtCenter">02 设备管理</td>
	            <td class="txtLeft"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter"></td>
	            <td class="txtCenter">无</td>
	        </tr>
	    </tbody>
	</table> --%>
<script type="text/javascript">
$(function(){
	$("table.dataTable tr").live("mouseover",function(){		
		$(this).addClass("hightBg");		
	}).live("mouseout",function(){
		$(this).removeClass("hightBg");
	})
	
	$(".closeWithoutSub").live("click", function(){
		var num = $(this).index();
		$(this).removeClass("closeWithoutSub").addClass("openWithoutSub");
		$(this).next().removeClass("hengWithSub").addClass("heng");
		switch(num){
			case 0://第一级别;
				//$(this).parent().parent().find("tr:not(.one)").css("display","none");
				$(this).parent().parent().find("tr:not(.one)").addClass("displayNone");
				break;
			case 1://第二级别;
				var clickTrNum = $(this).parent().index();
				$(this).parent().parent().find("tr").eq(clickTrNum).nextAll().addClass("displayNone2");					
				break;
			case 2://第三级别;
				var clickTrNum = $(this).parent().index();										
				var len = $(this).parent().parent().find("tr").length;				
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("four")){
						arr.push($tBody.find("tr").eq(i));						
					}else{						
						for(var j=0; j<arr.length; j++){
							arr[j].css("display","none");
						}
						return;
					}
				}
				for(var j=0; j<arr.length; j++){
					arr[j].css("display","none");
				}
				break;
		}
	});//end click;
	$(".openWithoutSub").live("click", function(){
		var num = $(this).index();
		$(this).removeClass("openWithoutSub").addClass("closeWithoutSub");
		$(this).next().removeClass("heng").addClass("hengWithSub");
		switch(num){
			case 0://第一级别;
				$(this).parent().parent().find("tr:not(.one)").removeClass("displayNone");
				//$(this).parent().parent().find("tr").css("display","");
				break;
			case 1://第二级别;
				var clickTrNum = $(this).parent().index();
				$(this).parent().parent().find("tr").eq(clickTrNum).nextAll().removeClass("displayNone2");	
				break;
			case 2://第三级别;
				var clickTrNum = $(this).parent().index();				
				var len = $(this).parent().parent().find("tr").length;				
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("four")){
						arr.push($tBody.find("tr").eq(i));						
					}else{						
						for(var j=0; j<arr.length; j++){
							arr[j].css("display","");
						}
						return;
					}
				}
				for(var j=0; j<arr.length; j++){
					arr[j].css("display","");
				}
				break;
		}
	});//end click;
	
	
	
	$(".closeWithSub").live("click" ,function(){
		var num = $(this).index();
		$(this).removeClass("closeWithSub").addClass("openWithSub");
		$(this).next().removeClass("hengWithSub").addClass("heng");
		switch(num){
			case 0://第一级别;
				//$(this).parent().parent().find("tr:not(.one)").css("display","none");
				$(this).parent().parent().find("tr:not(.one)").addClass("displayNone");
				break;
			case 1://第二级别;
				var clickTrNum = $(this).parent().index();
				var len = $(this).parent().parent().find("tr").length;
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("two")){
						for(var j=0; j<arr.length; j++){
							//arr[j].css("display","none");
							arr[j].addClass("displayNone2");
						}
						return;
					}else{						
						arr.push($tBody.find("tr").eq(i));
					}
				}				
				break;
			case 2://第三级别;
				var clickTrNum = $(this).parent().index();
				var len = $(this).parent().parent().find("tr").length;
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("four")){
						arr.push($tBody.find("tr").eq(i));						
					}else{						
						for(var j=0; j<arr.length; j++){
							arr[j].css("display","none");
						}
						return;
					}
				}
				break;
		}
	});	
	
	$(".openWithSub").live("click" ,function(){
		var num = $(this).index();
		$(this).removeClass("openWithSub").addClass("closeWithSub");
		$(this).next().removeClass("heng").addClass("hengWithSub");
		switch(num){
			case 0://第一级别;
				$(this).parent().parent().find("tr:not(.one)").removeClass("displayNone");
				//$(this).parent().parent().find("tr").css("display","");
				break;
			case 1://第二级别;
				var clickTrNum = $(this).parent().index();
				var len = $(this).parent().parent().find("tr").length;
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("two")){
						for(var j=0; j<arr.length; j++){
							//arr[j].css("display","");
							arr[j].removeClass("displayNone2");
						}
						return;
					}else{						
						arr.push($tBody.find("tr").eq(i));
					}
				}
				break;
			case 2://第三级别;
				var clickTrNum = $(this).parent().index();
				var len = $(this).parent().parent().find("tr").length;
				var arr = [];
				var $tBody = $(this).parent().parent();
				for(var i=clickTrNum+1; i<len; i++){
					if($tBody.find("tr").eq(i).hasClass("four")){
						arr.push($tBody.find("tr").eq(i));						
					}else{						
						for(var j=0; j<arr.length; j++){
							arr[j].css("display","");
						}
						return;
					}
				}
				break;
		}
	});
})




</script>
</body>
</Zeta:HTML>
