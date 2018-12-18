//保存左侧，由于多个页面运用到了这个js文件，该函数根据实际情况override;
function saveLayout(){
	saveEponViewLayout();
}
$(function(){
	//左右拖拽;
 	nm3k.dragMiddle1 = new DragMiddle({ id: "viewLeftLine", leftId: "viewLeftPart", rightId: "viewMiddle", minWidth: 150, maxWidth:300,leftBar:true, leftCallBack:saveLayout });
 	nm3k.dragMiddle2 = new DragMiddle({ id: "viewRightLine", leftId: "viewMiddle", rightId: "viewRightPart", minWidth:255, maxWidth: 400, rightBar:true });
 	
 	nm3k.dragLee1 = new DragLee({ id: "viewLeftLine", leftId: "viewLeftPart", rightId: "viewRightPart", nextLine: "viewRightLine", minWidth: 150, lineEdge: 263, leftCallBack:saveLayout });
	nm3k.dragLee2 = new DragLee({ id: "viewRightLine", leftId: "viewRightPart", rightId: "viewMiddle", preLine: "viewLeftLine", maxWidth: 708, lineEdge: 263 });

	
	var ultabH = $(".ultab").outerHeight();
	$("#viewLeftPart, #viewLeftLine, #viewRightLine, #viewRightPart").css("top",ultabH);
	
	//读取数据库后，设置布局;
	$("#viewLeftPart").width(nm3k.leftWidth);//布局左侧宽度;
	var l = nm3k.leftWidth + $("#viewLeftLine").outerWidth();
	$("#viewLeftLine").css("left",nm3k.leftWidth);//布局左侧拖拽竖线;
	$("#viewRightPart").width(nm3k.rightWidth);
	if(nm3k.layoutToMiddle){//面板图居中；
		nm3k.tabBtnSelectedIndex = 0;			
		$("#viewRightLine").css("right",nm3k.rightWidth);
		var r = nm3k.rightWidth + $("#viewRightLine").outerWidth();
		var l2 = l + "px";
		var r2 = r + "px";
		$("#viewMiddle").css({marginLeft:l2,marginRight:r2,marginTop:0,marginBottom:0});
		
		nm3k.dragMiddle1.init();
		nm3k.dragMiddle2.init();
	}else{//面板图在右侧;
		nm3k.tabBtnSelectedIndex = 1;
		var leftPos = nm3k.leftWidth + $("#viewLeftLine").outerWidth();
		var rightLineLeftPos = nm3k.leftWidth + $("#viewLeftLine").outerWidth() + nm3k.rightWidth;
		var leftMarginPos = rightLineLeftPos + $("#viewRightLine").outerWidth();
		$("#viewRightPart").css("left",leftPos).css("right","auto");
		$("#viewRightLine").css("left",rightLineLeftPos).css("right","auto");
		$("#viewMiddle").css({marginLeft:leftMarginPos,marginTop:0,marginRight:0,marginBottom:0})
		
		nm3k.dragLee1.init();
		nm3k.dragLee2.init();
	};//end if;
	
	$("#viewMiddleBottomBody").height(nm3k.middleBottomHeight);
	if(nm3k.middleBottomOpen){//中间下部是展开的;
		$("#viewMiddleBottomTit a").attr("class","pannelTilArrDown");
		$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","block");
	}else{
		$("#viewMiddleBottomTit a").attr("class","pannelTilArrUp");
		$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","none");
	}
	
	if(nm3k.rightTopOpen){//右侧上部是展开的
		$("#viewRightPartTopBody").css("display","block").height(nm3k.rightTopHeight);
		$("#viewRightPartTopTit a").attr("class","pannelTilArrDown");		
	}else{
		$("#viewRightPartTopBody").css("display","none");
		$("#viewRightPartMiddleLine").css("display","none");
		$("#viewRightPartTopTit a").attr("class","pannelTilArrUp");
	}
	
	if(nm3k.rightBottomOpen){//右侧下部是展开的;
		$("#viewRightPartBottomBody").css("display","block");
		$("#viewRightPartBottomTit a").attr("class","pannelTilArrDown");
	}else{
		$("#viewRightPartBottomBody").css("display","none");
		$("#viewRightPartMiddleLine").css("display","none");
		$("#viewRightPartBottomTit a").attr("class","pannelTilArrUp");
	}
	//设置布局end;
	
	
	
	
	autoHeight();
	
	$(window).wresize(function(){
		autoHeight();
	});//end resize;
	
	//点击中间底部，展开设备说明;
	$("#viewMiddleBottomTit a").click(function(){
		//$("#viewMiddleBottomBody").toggle("display");
		if($(this).hasClass("pannelTilArrUp")){
			$(this).attr("class","pannelTilArrDown");
			$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","block");
		}else if($(this).hasClass("pannelTilArrDown")){
			$(this).attr("class","pannelTilArrUp");
			$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","none");
		}
		saveEponViewLayout();//点击后自动保存布局;
	});//end click;
	/*$("#viewMiddleBottomTit").click(function(){
		//$("#viewMiddleBottomBody").toggle("display");
		if($(this).find("a").hasClass("pannelTilArrUp")){
			$(this).find("a").attr("class","pannelTilArrDown");
			$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","block");
		}else{
			$(this).find("a").attr("class","pannelTilArrUp");
			$("#viewMiddleBottomBody, #viewMiddleBottomLine").css("display","none");
		}
		saveEponViewLayout();//点击后自动保存布局;
	});//end click;
*/	
	//渲染切换布局2个按钮; 		
	var btnTab1 = new Nm3kTabBtn({
	    renderTo: "putTabBtn",
	    callBack: "tabCss",
	    selectedIndex: nm3k.tabBtnSelectedIndex,
	 	//备注：tabs这里很难理解，因为该函数本来是在该数组中传入文字，但是这里传入的是两个和css挂钩的图标;
	    tabs:["<i class='icoView01'></i>","<i class='icoView02'></i>"]
	});
	btnTab1.init();
	
	
	
	//右侧上下拖拽;
	nm3k.rightPartDragY = new nm3kDragY({id:"viewRightPartMiddleLine", minTopPos:100+69, maxTopPos:440+69, callBack:"rightPartDragYFn"});
	nm3k.rightPartDragY.init();
	
	//中间底部上下拖拽;
	nm3k.middlePartDragY = new nm3kDragY({id:"viewMiddleBottomLine", minBottomPos:100+33, maxBottomPos:400, callBack:"middlePartDragYFn"});
	nm3k.middlePartDragY.init();
	
	
	//自适应高度;
	function autoHeight(){
		var h = $(window).height();
		var topH = $(".ultab").outerHeight();
		var mainH = h - topH;//去掉头部;
		if(mainH < 0){return;}
		var leftSideBodyH = mainH - 32;
		if(leftSideBodyH<0){leftSideBodyH = 100;}
		
		//修正高度;
		$("#viewLeftPart, #viewLeftLine, #viewMiddle, #viewRightLine, #viewRightPart").height(mainH);
		$("#viewLeftPartBody").height(leftSideBodyH);
		
		//修正右侧上下2个版块之间高度，右侧下部版块自适应;
		var rightTopTit = $("#viewRightPartTopTit").outerHeight();
		var rightTopBody = $("#viewRightPartTopBody").outerHeight();			
		var rightMiddleLine = $("#viewRightPartMiddleLine").outerHeight();
		var rightBottomTit = $("#viewRightPartBottomTit").outerHeight();		
		var rightBottomBody;
		if($("#viewRightPartTopTit a").hasClass("pannelTilArrUp")){//如果上部是收缩的;
			rightBottomBody = mainH - rightTopTit - rightMiddleLine - rightBottomTit;			
		}else{//上部是展开
			rightBottomBody = mainH - rightTopTit - rightTopBody - rightMiddleLine - rightBottomTit;
		}
		
		if(rightBottomBody < 0){
			rightBottomBody = 145;
		}
		
		//判断右侧下部是否收缩;
		if($("#viewRightPartBottomTit a").hasClass("pannelTilArrUp")){//下部已经收缩;
			var newH = mainH -  rightTopTit - rightBottomTit;		
			if(rightTopBody > 0 && newH > 0){
				$("#viewRightPartTopBody").height(newH);
			}
		}else{
			$("#viewRightPartBottomBody").height(rightBottomBody);
		}
	};//end autoHeight;

	
	//点击右侧上部，收缩;
	$("#viewRightPartTopTit a").click(function(){
		var h0 = $("#viewRightPart").innerHeight();//右侧整体;
		var h1 = $("#viewRightPartTopTit").outerHeight();//上部标题;
		var h2 = $("#viewRightPartTopBody").outerHeight();//上部body;
		var h3 = $("#viewRightPartBottomTit").outerHeight();//下部标题;
		var h4 = $("#viewRightPartBottomBody").outerHeight();//下部body;
		var h5 = $("#viewRightPartMiddleLine").outerHeight();//右侧中间拖拽线;
		
		var thisCls = $(this).attr("class");
		switch(thisCls){
			case "pannelTilArrDown":
				$(this).attr("class","pannelTilArrUp");
	 			$("#viewRightPartTopBody").css("display","none");	
	 			$("#viewRightPartMiddleLine").css("display","none");//将中线隐藏;
	 		 	if($("#viewRightPartBottomTit a").hasClass("pannelTilArrDown")){//下面那个是展开的;
	 		 		var h = h0 - h1 - h3;
	 		 		$("#viewRightPartBottomBody").height(h).css("display","block");
	 		 	} 		 			
				break;
			case "pannelTilArrUp":
				$(this).attr("class","pannelTilArrDown");
				
	 			if($("#viewRightPartBottomTit a").hasClass("pannelTilArrDown")){
	 				$("#viewRightPartMiddleLine").height(7).css("display","block");//将中线显示;
	 				$("#viewRightPartTopBody").height(nm3k.rightTopHeight).css("display","block");
	 				var h = h0 - h1 - nm3k.rightTopHeight - h3 - 7;
	 				$("#viewRightPartBottomBody").height(h).css("display","block");
	 			}else{
	 				var h = h0 - h1 - h3;
	 				$("#viewRightPartTopBody").height(h).css("display","block");
	 			}
				break;
		}
		saveEponViewLayout();//点击后自动保存布局;
	});//end click;
	
	//点击右侧下部，收缩;
	$("#viewRightPartBottomTit a").click(function(){ 			
		var h0 = $("#viewRightPart").innerHeight();//右侧整体;
		var h1 = $("#viewRightPartTopTit").outerHeight();//上部标题;
		var h2 = $("#viewRightPartTopBody").outerHeight();//上部body;
		var h3 = $("#viewRightPartBottomTit").outerHeight();//下部标题;
		var h4 = $("#viewRightPartBottomBody").outerHeight();//下部body;
		var h5 = $("#viewRightPartMiddleLine").outerHeight();//右侧中间拖拽线;

		var thisCls = $(this).attr("class");
		switch(thisCls){
			case "pannelTilArrDown":
				$(this).attr("class","pannelTilArrUp");
	 			$("#viewRightPartBottomBody").css("display","none");	 	
	 			$("#viewRightPartMiddleLine").css("display","none");//将中线隐藏;
	 			if($("#viewRightPartTopTit a").hasClass("pannelTilArrDown")){//如果上面的是展开的;
	 				var h = h0 - h1 - h3;
	 				$("#viewRightPartTopBody").height(h).css("display","block");
	 			}
				break;
			case "pannelTilArrUp":
				$(this).attr("class","pannelTilArrDown");				
				if($("#viewRightPartTopTit a").hasClass("pannelTilArrDown")){//上半部分是展开的;					
					$("#viewRightPartMiddleLine").height(7).css("display","block");//将中线显示;
					$("#viewRightPartTopBody").height(nm3k.rightTopHeight).css("display","block");
					var h = h0 - h1 - nm3k.rightTopHeight - h3 - 7;
	 				$("#viewRightPartBottomBody").height(h).css("display","block");	 				
				}else{//上半部分已经收缩了;					
					var h = h0 - h1 - h3 ;
	 				$("#viewRightPartBottomBody").height(h).css("display","block");
				}
				
				break;
		}
		saveEponViewLayout();//点击后自动保存布局;
	});//end click;
	
});//end document.ready;

//切换布局;
function tabCss(index){
	if(index == nm3k.tabBtnSelectedIndex){return;}
	nm3k.tabBtnSelectedIndex = index;
	switch(index){
 		case 0:
 			nm3k.dragLee1.die();
 			nm3k.dragLee2.die();
 			mainPartToCenter( saveEponViewLayout );
 			nm3k.dragMiddle1.init();
 			nm3k.dragMiddle2.init();	 			
 			break;
 		case 1:
 			nm3k.dragMiddle1.die();
 			nm3k.dragMiddle2.die();
 			mainPartToRight( saveEponViewLayout );
 			nm3k.dragLee1.init();
 			nm3k.dragLee2.init();
 			break;
 	};//end switch;
 	saveEponViewLayout();//切换后自动保存布局;
 };//end tabCss;

//主体面板图布局到右侧;
 	function mainPartToRight( fn ){
 		nm3k.tabBtnSelectedIndex = 1;//为了保持的时候使用;
 		nm3k.layoutToMiddle = false;
 		
 		
 		var leftPos = $("#viewLeftPart").outerWidth() + $("#viewLeftLine").outerWidth();
 		var leftPosLine = leftPos + $("#viewRightPart").outerWidth();
 		var leftMargin = leftPosLine + $("#viewRightLine").outerWidth();
 		//将右侧面板移动到左边,将右侧line移动到左边，将中间部分移动到右边;
 		$("#viewRightPart").css({right:"auto",left:leftPos});
 		$("#viewRightLine").css({right:"auto",left:leftPosLine});
 		$("#viewMiddle").css({marginLeft:leftMargin,marginRight:0});
 		//window.top.nm3kMsgRight( fn );
 	}
 	//主体面板图布局到中间;
 	function mainPartToCenter( fn ){
 		nm3k.tabBtnSelectedIndex = 0;
 		nm3k.layoutToMiddle = true;
 		
 		var rightPartW = $("#viewRightPart").outerWidth();
 		var leftMargin = $("#viewLeftPart").outerWidth() + $("#viewLeftLine").outerWidth();
 		var rightMargin = rightPartW + $("#viewRightLine").outerWidth();
 		$("#viewRightPart").css({left:"auto",right:0});
 		$("#viewRightLine").css({left:"auto",right:rightPartW});
 		$("#viewMiddle").css({marginLeft:leftMargin,marginRight:rightMargin});
 		//window.top.nm3kMsgMiddle( fn );	
 	};
 	
 	//拖拽右侧的回调函数,参数是拖拽后的offset().top;
 	function rightPartDragYFn(paraTopPos){
 		var h = $("#viewRightPart").outerHeight();//右侧总体高度;
 		var h1 = $(".ultab").outerHeight();//菜单的高度;
 		var h2 = $("#viewRightPartTopTit").outerHeight();//右侧上半部分title的高度;
 		var h3 = paraTopPos - h1 - h2;//得到的右侧上半部分的body高度;
 		var h4 = $("#viewRightPartMiddleLine").outerHeight();//拖拽线的高度;
 		var h5 = $("#viewRightPartBottomTit").outerHeight();//右侧下半部分title的高度;
 		var h6 = h - h2 - h3 - h4 - h5; 
 		if(h3 > 0 && h6 > 0){
 			nm3k.rightTopHeight = h3;
 			$("#viewRightPartTopBody").height(h3);
 			$("#viewRightPartBottomBody").height(h6);
 		}
 		saveEponViewLayout();//拖拽后自动保存布局;
 	};//end rightPartDragYFn
 	
 	//拖拽中间的回调函数;
 	function middlePartDragYFn(paraTopPos){
 		var h = $(window).height();//总体高度;
 		var h1 = $(".ultab").outerHeight();//菜单的高度;
 		var h2 = $("#viewMiddleBottomLine").outerHeight();//中间拖拽条高度;
 		var h3 = $("#viewMiddleBottomTit").outerHeight();
 		var h4 = h-h2-h3-paraTopPos;
 		if(h4 > 0){
 			$("#viewMiddleBottomBody").height(h4);
 			nm3k.middleBottomHeight = h4;
 		}
 		saveEponViewLayout();//拖拽后自动保存布局;
 	};//end middlePartDragYFn;
 	
 	
 	
 	//获取用户设置后相关参数的值
	function setLayoutValue(){
		//左侧宽度;
		nm3k.leftWidth = $("#viewLeftPart").width();
		//右侧宽度;
		nm3k.rightWidth = $("#viewRightPart").width();
		
		//先判断右侧上部是否打开;		
		if($("#viewRightPartTopTit a").hasClass("pannelTilArrUp")){//是关闭的;
			nm3k.rightTopOpen = false;						
		}else{//是打开的;
			nm3k.rightTopOpen = true;
			if($("#viewRightPartBottomTit a").hasClass("pannelTilArrDown")){//右侧下部也必须是打开的才存储此值;
				nm3k.rightTopHeight = $("#viewRightPartTopBody").height();
			}
		}		
		//判断右侧下部是否打开;
		if($("#viewRightPartBottomTit a").hasClass("pannelTilArrUp")){//是关闭的;
			nm3k.rightBottomOpen = false;			
		}else{//打开的;
			nm3k.rightBottomOpen = true;			
		}		
		//判断中间下部是否打开;
		if($("#viewMiddleBottomBody").css("display") == "none"){//是关闭的;
			nm3k.middleBottomOpen = false;			
		}else{//是打开的;
			nm3k.middleBottomOpen = true;			
		}
		nm3k.middleBottomHeight = $("#viewMiddleBottomBody").height();		
		//面板图在右侧还是在中间，在mainPartToRight函数中已经做保存;
	}	
	
	//保存用户设置的值
	function saveEponViewLayout(){
		currentId = null;
		setLayoutValue();
		$.ajax({
			url: '/epon/saveOltFaceView.tv', //这里读取8602或者8603都没关系，因为都是同一套布局;
			cache:false, 
			method:'post',
			data: {
				rightTopHeight : nm3k.rightTopHeight, 
				middleBottomHeight : nm3k.middleBottomHeight, 
				leftWidth : nm3k.leftWidth, 
				rightWidth : nm3k.rightWidth, 
				rightTopOpen : nm3k.rightTopOpen, 
				rightBottomOpen : nm3k.rightBottomOpen, 
				middleBottomOpen : nm3k.middleBottomOpen, 
				layoutToMiddle : nm3k.layoutToMiddle,
				tabBtnSelectedIndex : nm3k.tabBtnSelectedIndex
			},
			success: function() {
				//alert(nm3k.rightTopHeight)
			    //window.parent.showMessageDlg(I18N.EPON.tip,  "保存布局设置成功!")
			},
			error: function(){
			    //window.parent.showMessageDlg(I18N.EPON.tip, "保存布局设置失败!")
			}
		});
	}
 