# coding: UTF-8
require 'java'
java_import 'org.slf4j.Logger'
java_import 'org.slf4j.LoggerFactory'
class RubySyslogParser
	def parse(entityId,syslog)
		@@syslog = syslog
		begin
			# puts syslog.to_s
	        # event = EventSender.getInstance().createEvent(9,syslog.getHost(),syslog.getHost())
	        # event.setMessage(syslog.getMessage())
			# EventSender.getInstance().send(event)
			# Modify by Rod
			log = LoggerFactory.getLogger("com.topvision.syslog");
			log.info(syslog.getText());
		rescue
		    puts "#{$!}"
		    puts "#{$@}"
		end
		return true
	end
end