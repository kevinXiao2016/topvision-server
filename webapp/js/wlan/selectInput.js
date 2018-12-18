$(document).ready(function(){	 
$("#inputer").autocomplete(data, {
       	minChars: 0,
		max: 12,
		width: 260,
		autoFill: true,
		matchContains: true,
		scrollHeight: 220,
        max:20
});			 			 
function ajaxAddOptions(url, target, id, text){
	target.empty();
	$.getJSON(url, function(json){
	    target.append("<option value='choose'>请选择地域</option>" );	    
		target.append("<option value='" + json.folderId + "'>" + json.name + "</option>" );
		$(json.childs).each(function(i){
			    if(i<json.childs.length-1)
				target.append("<option value='" + json.childs[i].folderId + "'>  &nbsp;&nbsp;├" + json.childs[i].name + "</option>" );
			    else 
			    	target.append("<option value='" + json.childs[i].folderId + "'>  &nbsp;&nbsp;└" + json.childs[i].name + "</option>" );
				  		
		})	
	});
}  
ajaxAddOptions("ap/autoComplete.tv", $("#folder"), "id", "text");
function ajaxRegionOptions(url, target, fid ,  id, text){   
	target.empty();
	target.append("<option value='allIp'>请选择IP</option>" );	    			
	$.getJSON(url,function(json){
		$(json).each(function(i){  
			var x = json[i];
			target.append("<option value='" + eval("x." + id) + "'>" + eval("x." + text) + "</option>" );
		})
	});
}
  $("#folder").bind("change",function(){
		 var flag = null ;	
		 if($("#folder").val()=='choose')
		 { 
		  flag  =  false ; 
		 }
		else if($("#folder").val()!='choose')
		{
		 flag =  true ;  
		 ajaxRegionOptions("ap/regionIp.tv?folderId="+$("#folder").val(), $("#selecter"), $("#province").val(), "ip", "ip");   
	    }
			     $("#inputer").toggle(1,function(){
			     $("#selecter").toggle(flag);
			     if(flag == true)
			      $("#inputer").toggle(false);
			     else if (flag == false)
			     $("#inputer").toggle(true);		     
			    });		    
			    });
});