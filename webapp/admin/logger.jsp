<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
</Zeta:Loader>
<style>
.left-navi {position : absolute;left:0px;top:0px;width:97px;height:100%;background-color:gray;}
.top-navi {position : absolute;left:100px;top:0px;width:85%;}
.left-navi button{width:100%;}
.body-content {position : absolute;left:100px;top:60px;width:93%;height:95%;overflow  : scroll;}
body{overflow:hidden;}
pre{margin : 0 0 0 0;}
.hiddenClass{display: none;}
</style>
<script type='text/javascript' src='../dwr/engine.js'> </script>
<script type='text/javascript' src='../dwr/interface/LoggerPusher.js'> </script>
<script type='text/javascript' src='../dwr/util.js'> </script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
</head>
<body onload="_init_()">
	<div class="left-navi">
		<!-- <button class="serviceBt" onclick="setAllLv(LEVEL.ALL)">所有级别</button>
		<button class="serviceBt" onclick="setSingleLv(LEVEL.DEBUG)">= DEBUG级别</button>
		<button class="serviceBt" onclick="setSingleLv(LEVEL.INFO)">= INFO级别</button>
		<button class="serviceBt" onclick="setSingleLv(LEVEL.WARN)">= WARN级别</button>
		<button class="serviceBt" onclick="setSingleLv(LEVEL.ERROR)">= ERROR级别</button>
		<button class="serviceBt" onclick="setMultiLv(LEVEL.DEBUG)">>= DEBUG级别</button>
		<button class="serviceBt" onclick="setMultiLv(LEVEL.INFO)">>= INFO级别</button>
		<button class="serviceBt" onclick="setMultiLv(LEVEL.WARN)">>= WARN级别</button>
		<button class="serviceBt" onclick="setMultiLv(LEVEL.ERROR)">>= ERROR级别</button> -->
	</div>
	<div class="top-navi">
		<!-- <button class="serviceBt" id="stopstart" onclick="stop();">停止</button>
		<button class="serviceBt" onclick="empty()">清空</button>
		<button class="serviceBt">浏览日志文件</button>
		<button class="serviceBt" onclick="setLevel('debug');">级别：DEBUG</button>
		<button class="serviceBt" onclick="setLevel('info');">级别：INFO</button>
		<button class="serviceBt" onclick="setLevel('warn');">级别：WARN</button>
		<button class="serviceBt" onclick="setLevel('error');">级别：ERROR</button> -->
		<button id="restoreFactory" onclick="restoreFactoryConfigj();">日志生效</button>
		<div id="entityList"></div>
	</div>
	<div class="body-content" id="loggers">
	<span>在./lib目录下新建logback-test.xml文件，编辑该文件，可以指定只打印关注的日志内容到某个文件中，具体事例见下面的xml<br>
	修改保存后点击上面的<button id="restoreFactory" onclick="restoreFactoryConfigj();">日志生效</button>按钮，就可以在./logs目录下的指定文件中去查看对应的日志了。</span>
	<br>
	<h3>logback-test.xml文件的例子</h3>
<xmp>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>logs/ems.logback</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/ems-%d{yyyy-MM-dd}.%i.logback</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <encoder>
            <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
        </encoder>
    </appender>    
    
    <appender name="mylog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>logs/mylog.logback</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/mylog-%d{yyyy-MM-dd}.%i.logback</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>4MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <encoder>
            <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
        </encoder>
    </appender>
    
    
    <root level="info">
        <appender-ref ref="ROLLING" />
    </root>

    <!-- 以下这个必须屏蔽，否则影响wrapper以windows服务启动 -->
    <logger name="org.springframework.beans.factory.support.DefaultListableBeanFactory"
        level="error">
    </logger>
    <logger name="com.topvision" level="info">
    </logger>
    <logger name="com.topvision.ems.cmts.performance.scheduler.CmtsSystemPerfScheduler" level="debug">
         <appender-ref ref="mylog" />
    </logger>
    <logger name="com.topvision.ems.cmts.performance.dbsaver.CmtsSystemPerfDBSaver" level="debug">
         <appender-ref ref="mylog" />
    </logger>
    <logger name="com.topvision.ems.network.dao.EntityDao" level="debug">
         <appender-ref ref="mylog" />
    </logger>
</configuration>

</xmp>
	</div>
	<script type="text/javascript">
		var LEVEL = {
			ALL : 	-2147483648,
			TRACE : 5000,
			DEBUG : 10000,
			INFO : 20000,
			WARN : 30000,
			ERROR : 40000,
			OFF : 2147483647
		} ;
		var $entityList,
			displayEntityList = new Array;
		var singleLv = LEVEL.ALL,multiLv = LEVEL.ALL,allLvMark = true,isStoped;
		
		function setSingleLv(lv){
			allLvMark = false;
			window.singleLv = lv;
		}
		
		function setAllLv(){
			allLvMark = true;
		}
		
		function stop(){
			isStoped = true;
			$("#stopstart").html("开启")[0].onclick=start;
		}
		function start(){
			isStoped = false;
			$("#stopstart").html("停止")[0].onclick=stop
		}
		
		
		function setMultiLv(lv){
			allLvMark = false;
			window.singleLv = null;
			window.multiLv = lv;
		}
		
		function _init_(){
			dwr.engine.setActiveReverseAjax(true);
			LoggerPusher.registerPusher(function(data) {
			   /*  if(data != null){
			       $entityList = $("#entityList");
			       $.each(data,function(index, entity){
			           $entityList.append(String.format("<input type=checkbox checked class=entityItem value='{0}' />{1}- {2} | ",entity.entityId,entity.ip,entity.name));
			           displayEntityList.add( entity.entityId );
			       });
			       $entityList.append("<input type=checkbox checked class=entityItem value='0' />其他所有");
			       displayEntityList.add(0);
			       $entityList.append("<button onclick='filterLogger()'>过滤日志</button>");
			    } */
			});
		}
		
		function filterLogger(){
		    displayEntityList = new Array;
		    var $checkedEntity = $(".entityItem:checked");
		    $(".loggerClazz").hide();
		    $.each($checkedEntity, function(idx, entity){
		    	displayEntityList.add( entity.value );
		    	$(".loggerRecord" + entity.value ).show();
		    });
		}
		
		function restoreFactoryConfigj(){
			LoggerPusher.restoreFactoryConfig(function(res){
				if(res){
					alert("恢复出厂设置成功");
					/* $("#restoreFactory").html("开启日志功能")[0].onclick = restoreMirrorLogger
					$(".serviceBt").attr("disabled",true)
					window.useMirror = false; */
				}else{
					alert("恢复出厂设置失败");
				}
			});
			
		}
		
		function restoreMirrorLogger(){
			LoggerPusher.reUseMirrorLogback(function(res){
				if(res){
					alert("使用镜像日志成功");
					$(".serviceBt").attr("disabled",false)
					$("#restoreFactory").html("恢复出厂设置")[0].onclick = restoreFactoryConfig
					window.useMirror = true;
				}else{
					alert("使用镜像日志失败");
					window.useMirror = false;
				}
			})
		}
		var $logger =  $("#loggers")
		
		function receiveLoggers(data){
			if(isStoped){
				return;
			}
			var $fra = $("<div>")
			$.each(data,function(i,message){
				if(allLvMark){
				    recordMessage(message , $fra);
				}else if(singleLv != null){
					if(singleLv == message.level){
					    recordMessage(message , $fra);
					} 
				}else if(singleLv == null){
					if(multiLv == message.level || multiLv < message.level){
					    recordMessage(message , $fra);
					}
				}
			})
			$logger.append($fra)
			$fra = null;
			$logger[0].scrollTop = $logger[0].scrollHeight
		}
		
		function recordMessage(message, $fra){
		    //if( displayEntityList.contains(message.entityId) ){
			    $fra.append(String.format("<pre class='loggerClazz loggerRecord{0}'>{1}</pre>",message.entityId, message.data));
		    /* }else{
		        $fra.append(String.format("<pre class='loggerClazz loggerRecord{0} hiddenClass'>{1}</pre>",message.entityId, message.data));
		    } */
		}		
		
		function empty(){
			$("#loggers").empty();
		}
		
		function setLevel(level){
			LoggerPusher.configLoggerLevel(level);
		}
		
		function restoreFactoryConfig(){
			LoggerPusher.restoreFactoryConfig();
		}
	</script>
	</body>
</Zeta:HTML>