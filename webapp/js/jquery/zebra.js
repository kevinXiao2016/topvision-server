$(function(){
	$(window).load(function(){
		setTimeout(zebraTable,3000)
	});//end load;
})

function zebraTable(){
	/**斑马线***/
	$(".zebra").each(function(){
		$(this).find("tbody").eq(0).find("tr:even").addClass("light");
		$(this).find("tbody:eq(0) tr").hover(function(){
			$(this).addClass("selectedTrBg");
		},function(){
			$(this).removeClass("selectedTrBg");
		});//end hover;	
	});//end each;
	
}