//面板图，收缩右侧
		$(function(){
	 		var divH2 = 280;
	 		var divH4 = 145;
	 			
	 		//点击配置，收缩;
	 		$(".pannelTit a").click(function(){
	 			//var grid = window.grid;
	 			
	 			var h0 = $(".viewRightSidePart").innerHeight();
	 			var h1 = $(".viewRightSidePart .pannelTit").outerHeight();
	 			var h2 = $(".viewRightSidePart .rightPannel").outerHeight();
	 			var h3 = $(".viewRightSidePart .pannelTitWithTopLine").outerHeight();
	 			var h4 = $(".viewRightSidePart .rightPannel2").outerHeight();
	 			
	 			var thisCls = $(this).attr("class");
	 			switch(thisCls){
	 				case "pannelTilArrDown":
	 					$(this).attr("class","pannelTilArrUp");
	 		 			$(this).parent().next().css("display","none").height(0);	 		 			
	 		 		 	if($(this).parent().next().next().find("a").hasClass("pannelTilArrDown")){//下面那个是展开的;
	 		 		 		var h = h0 - h1 - h3;
	 		 		 		$(this).parent().next().next().next().height(h).css("display","block");
	 		 		 	} 		 			
	 					break;
	 				case "pannelTilArrUp":
	 					$(this).attr("class","pannelTilArrDown");
	 		 			$(this).parent().next().css("display","block");
	 		 			if($(this).parent().next().next().find("a").hasClass("pannelTilArrDown")){
	 		 				$(this).parent().next().height(divH2).css("display","block");
	 		 				$(this).parent().next().next().next().height(divH4).css("display","block");
	 		 			}else{
	 		 				var h = h0 - h1 - h3;
	 		 				$(this).parent().next().height(h).css("display","block");
	 		 			}
	 					break;
	 			}
	 		});//end click;
	 		
	 		
	 		
	 		//点击设备说明，收缩;
	 		$(".pannelTitWithTopLine a").click(function(){ 			
	 			var h0 = $(".viewRightSidePart").innerHeight();
	 			var h1 = $(".viewRightSidePart .pannelTit").outerHeight();
	 			var h2 = $(".viewRightSidePart .rightPannel").outerHeight();
	 			var h3 = $(".viewRightSidePart .pannelTitWithTopLine").outerHeight();
	 			var h4 = $(".viewRightSidePart .rightPannel2").outerHeight();

	 			var thisCls = $(this).attr("class");
	 			switch(thisCls){
	 				case "pannelTilArrDown":
	 					$(this).attr("class","pannelTilArrUp");
	 		 			$(this).parent().next().css("display","none").height(0);	 		 			
	 		 			if($(this).parent().prev().prev().find("a").hasClass("pannelTilArrDown")){
	 		 				var h = h0 - h1 - h3;
	 		 				$(this).parent().prev().height(h).css("display","block");
	 		 			}
	 		 					 			
	 					break;
	 				case "pannelTilArrUp":
	 					$(this).attr("class","pannelTilArrDown");
	 		 			$(this).parent().next().css("display","block");
	 					if($(this).parent().prev().prev().find("a").hasClass("pannelTilArrDown")){
	 						$(this).parent().prev().height(divH2).css("display","block");
	 		 				$(this).parent().next().height(divH4).css("display","block");
	 					}else{
	 						var h = h0 - h1 - h3;
	 		 				$(this).parent().next().height(h).css("display","block");
	 					}
	 					
	 					break;
	 			}
	 		});//end click;
	 	});//end document.ready;
