# coding: UTF-8
require 'java'

java_import 'com.topvision.ems.fault.parser.TrapParser'
java_import 'com.topvision.ems.fault.domain.Event'
java_import 'com.topvision.ems.fault.service.EventSender'
java_import 'java.util.Iterator'
class RubyTrapParser
    def getTrapCos()
        return 10000;
    end
	def parse(entityId,trap)
		@@trap = trap
		begin
			uptime = trap.getVarialbleValue("1.3.6.1.2.1.1.3.0")
			type = trap.getVarialbleValue("1.3.6.1.6.3.1.1.4.1.0")
			event = EventSender.getInstance().createEvent(1,trap.getAddress(),trap.getVarialbleValue("1.3.6.1.6.3.18.1.3.0"))
			# coldStart
			if(type == "1.3.6.1.6.3.1.1.5.1")
				event.setTypeId(10004)
				#event.setMessage("设备冷启动")
				event.setMessage("\350\256\276\345\244\207\345\206\267\345\220\257\345\212\250")
			# warmStart
			elsif(type == "1.3.6.1.6.3.1.1.5.2")
				event.setTypeId(10005)
				#event.setMessage("设备热启动")
				event.setMessage("\350\256\276\345\244\207\347\203\255\345\220\257\345\212\250")
			# linkDown
			elsif(type == "1.3.6.1.6.3.1.1.5.3")
				event.setTypeId(30003)
				trap.getVariableBindings().keySet.each{|key|
				    key =~ /1\.3\.6\.1\.2\.1\.2\.2\.1\.1\./
				    if($' != nil)
				        @ifIndex = trap.getVariableBindings().get(key).to_i
				    end
				}
				event.setSource("#{@ifIndex}")
				#event.setMessage("#{@ifIndex}端口Down")
				event.setMessage("#{@ifIndex}\347\253\257\345\217\243Down")
			# linkUp
			elsif(type == "1.3.6.1.6.3.1.1.5.4")
				event.setTypeId(30004)
				trap.getVariableBindings().keySet.each{|key|
				    key =~ /1\.3\.6\.1\.2\.1\.2\.2\.1\.1\./
				    if($' != nil)
				        @ifIndex = trap.getVariableBindings().get(key).to_i
				    end
				}
				event.setSource("#{@ifIndex}")
				#event.setMessage("#{@ifIndex}端口Up")
				event.setMessage("#{@ifIndex}\347\253\257\345\217\243Up")
				event.setClear(true)
			# authenticationFailure
			elsif(type == "1.3.6.1.6.3.1.1.5.5")
				event.setTypeId(60003)
				#event.setMessage("登录认证失败")
				event.setMessage("\347\231\273\345\275\225\350\256\244\350\257\201\345\244\261\350\264\245")
			else
				#puts '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
				#puts trap.to_s
				#puts '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
				event = nil
			end
			if(event != nil)
				EventSender.getInstance().send(event)
				return false
			end
		rescue
		    puts "#{$!}"
		    puts "#{$@}"
		end
		return true
	end
end