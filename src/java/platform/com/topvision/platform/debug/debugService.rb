# coding: UTF-8
require 'java'
java_import 'com.topvision.platform.debug.BeanService'
java_import 'com.topvision.ems.engine.performance.PerfExecutorService'
java_import 'com.topvision.ems.engine.common.EngineThreadPool'
java_import 'com.topvision.framework.ping.PingExecutorService'
java_import 'com.topvision.framework.snmp.SnmpExecutorService'

java_import 'org.slf4j.Logger'
java_import 'org.slf4j.LoggerFactory'

class RubyDebugService
	def debug(beanFactory)
        @@beanFactory = beanFactory
		begin
			log = LoggerFactory.getLogger("com.topvision.platform.action.AdminAction");
			log.info("debug running...");
			
			perfExecutorService = beanFactory.getBean("perfExecutorService");
			scheduledThreadPoolExecutor = perfExecutorService.getScheduledThreadPoolExecutor();
			realtimeScheduledThreadPoolExecutor = perfExecutorService.getRealtimeScheduledThreadPoolExecutor();
			engineThreadPool = beanFactory.getBean("engineThreadPool");
			pingExecutorService = beanFactory.getBean("pingExecutorService");
			snmpExecutorService = beanFactory.getBean("snmpExecutorService");
			perfSnmpExecutorService = beanFactory.getBean("perfSnmpExecutorService");
			
			#perfSnmpExecutorService.getService().setCorePoolSize(256);
			#snmpExecutorService.getService().setCorePoolSize(64);
			
			s = java.lang.StringBuilder.new;
			s.append("Print the thread pool status:<br />");
			
			s.append("<p>Snmp Pool:").append(snmpExecutorService.getQueueSize()).append(",ActiveCount/PoolSize:");
			s.append(snmpExecutorService.getService().getActiveCount()).append("/").append(snmpExecutorService.getService().getPoolSize());
			s.append(",Queue Size:").append(snmpExecutorService.getService().getQueue().size()).append("</p>");
			
			s.append("<p>Perf Snmp Pool:").append(perfSnmpExecutorService.getQueueSize()).append(",ActiveCount/PoolSize:");
			s.append(perfSnmpExecutorService.getService().getActiveCount()).append("/").append(perfSnmpExecutorService.getService().getPoolSize());
			s.append(",Queue Size:").append(perfSnmpExecutorService.getService().getQueue().size()).append("</p>");
			
			s.append("<p>Ping Pool:").append(pingExecutorService.getQueueSize()).append(",ActiveCount/PoolSize:");
			s.append(pingExecutorService.getService().getActiveCount()).append("/").append(pingExecutorService.getService().getPoolSize());
			s.append(",Queue Size:").append(pingExecutorService.getService().getQueue().size()).append("</p>");
			
			s.append("<p>Thread Pool:").append(engineThreadPool.getQueueSize()).append(",ActiveCount/PoolSize:");
			s.append(engineThreadPool.getActiveCount()).append("/").append(engineThreadPool.getPoolSize());
			s.append(",Queue Size:").append(engineThreadPool.getQueue().size()).append("</p>");
			
			s.append("<p>Perfomance Tasks:").append(perfExecutorService.getScheduledFutureConcurrentHashMaps().size());
			s.append(",ActiveCount/PoolSize:").append(scheduledThreadPoolExecutor.getActiveCount()).append("/").append(perfExecutorService.getPoolSize());
			s.append(",Queue Size:").append(scheduledThreadPoolExecutor.getQueue().size()).append("</p>");
			
			s.append("<p>Realtime Perfomance Tasks:ActiveCount/PoolSize:").append(realtimeScheduledThreadPoolExecutor.getActiveCount()).append("/").append(perfExecutorService.getRealtimePoolSize());
			s.append(",Queue Size:").append(realtimeScheduledThreadPoolExecutor.getQueue().size()).append("</p>");

			return s.toString();
		rescue
		    puts "#{$!}";
		    puts "#{$@}";
		    return "#{$@}";
		end
	end
end