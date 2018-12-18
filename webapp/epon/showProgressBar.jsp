<!DOCTYPE html>
<html lang="en">
<head>
<link href="css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css"/>
<link href="css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script> 
<script type="text/javascript" src="js/progressBarScript.js"></script>
<script>
	var entityId = ${entityId};
	var filePath = '${filePath}';
	var destinationFile = '${destinationFile}';
</script>
<style>
.pbar {
	position:absolute; left:0px; top:0px; width:300px;
}
.percent {
	position:absolute; left:140px; top:12px; z-index:1000000;
}
.elapsed {
	position:absolute; left:210px;
}
</style>
</head>
<body>
      <div id="progress3">
          <div class="percent"></div>
          <div class="pbar"></div>
         <!--  <div class="elapsed"></div> -->
      </div>
</body>
</html>