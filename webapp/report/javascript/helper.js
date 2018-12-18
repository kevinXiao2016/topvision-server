//记录查询区域的高度
var	queryCloseHeaderTop = 0,
	queryOpenHeaderTop = 0;
function toogleOption(){
	$('#queryContainer').toggle();
	headerTop = $('#queryContainer').is(':visible') ? queryOpenHeaderTop : queryCloseHeaderTop;
}

function b_toogleOption(){
	$(window).scrollTop(0);
	$('#queryContainer').toggle();
	headerTop = $('#queryContainer').is(':visible') ? queryOpenHeaderTop : queryCloseHeaderTop;
}

/**
 * 打印预览
 */
function printview(){
	//先回到顶部，再执行打印操作，解决表头在顶部的问题
	$(window).scrollTop(0);
	
	setTimeout(function(){
		openPrintWindow();
	}, 0);
    
}

function print(){
	//先回到顶部，再执行打印操作，解决表头在顶部的问题
	$(window).scrollTop(0);
	
	setTimeout(function(){
		openPrintWindow().print();
	}, 0);
}

function openPrintWindow(){
	var wnd = window.open();
    doc = wnd.document;
    var formatHtml = document.getElementById('resultContainer').innerHTML.replace(/href="#"/g, '');
    formatHtml = formatHtml.replace(/onclick="([^"]*)"/g, '');
    doc.write('<!DOCTYPE html>');
    doc.write('<html>');
    doc.write('<head>');
    doc.write('<title>' + $('#reportTitle').text() + '</title>');
    doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
    doc.write('<link rel="stylesheet" type="text/css" href="/css/reportTemplate.css"/>');
    doc.write('</head>');
    doc.write('<body style="margin:50px auto;"><div id="wrapper"><div id="resultContainer">');
    doc.write(formatHtml);
    doc.write('</div></div></body>');
    doc.write('</html>');
    doc.close();
    return wnd;
}

$(function(){
	$(window).scroll(function(){  //只要窗口滚动,就触发下面代码 
		 var dt = $(document).scrollTop(),
		 	wt = $(window).height();
        if (dt <= 0) {
            $("#totop").hide();
            return;
        }
        $("#totop").show();
        if ($.browser.msie && $.browser.version == 6.0) {//IE6返回顶部
            $("#totop").css("top", wt + dt - 110 + "px");
        }
    });
    $("#totop").click(function(){ //当点击标签的时候,使用animate在200毫秒的时间内,滚到顶部
        $("html,body").animate({scrollTop:"0px"},200);
    });
})