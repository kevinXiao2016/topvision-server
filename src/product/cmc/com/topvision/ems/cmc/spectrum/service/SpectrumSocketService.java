package com.topvision.ems.cmc.spectrum.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.spectrum.exception.CmtsSwitchOffException;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.service.impl.WebCallback;
import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;
import com.topvision.ems.socketserver.socket.SocketRequestExecutor;
import com.topvision.ems.socketserver.websocket.TopWebSocketHandler;
import com.topvision.framework.service.BaseService;

import net.sf.json.JSONObject;

@Service("spectrumSocketService")
public class SpectrumSocketService extends BaseService implements SocketRequestExecutor, BeanFactoryAware, TopWebSocketHandler {
    private BeanFactory beanFactory;
    @Autowired
    private SpectrumHeartbeatService spectrumHeartbeatService;

    //modify by jay 2016-11-09 调整调用顺序 启用heatbeat机制
    /*
     1、通过socket发送cmcId
     2、将socketResponce修改为MAINTAIN_SOCKET
     3、在SpectrumSocketService里面调用heartbeatService获取到callbackId回写到js
     4、在WebCallback里面保存SocketResponse，接收到数据就往回推送
     5、
     */
    @Override
    public boolean execute(SocketRequest socketRequest, SocketResponse socketResponse) {
        onConnected(socketRequest);
        return MAINTAIN_SOCKET;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public void onConnected(SocketRequest socketRequest) {
        Long cmcId = Long.parseLong(socketRequest.getString("cmcId"));
        logger.debug("SpectrumSocketService.execute  cmcId = " + cmcId);
        JSONObject responseJson = new JSONObject();
        Long callbackId = -1L;
        try {
            WebCallback webCallback = (WebCallback)beanFactory.getBean("webCallback");
            webCallback.setCmcId(cmcId);
            webCallback.setSocketResponse(socketRequest.getResponse());
            webCallback.setStartTime(System.currentTimeMillis());
            webCallback.setTerminalIp(socketRequest.getRequestHost());
            callbackId = spectrumHeartbeatService.addHeartbeat(cmcId, webCallback);
            logger.debug("SpectrumSocketService.execute  callbackId = " + callbackId);
            webCallback.setCallbackId(callbackId);
        } catch (OltSwitchOffException e) {
            responseJson.put("error", "oltSwitchClosed");
            logger.error("Spectrum addCallBack fail!", e);
        } catch (CmtsSwitchOffException e) {
            responseJson.put("error", "cmtsSwitchClosed");
            logger.error("Spectrum addCallBack fail!", e);
        } catch (Exception e) {
            logger.error("Spectrum addCallBack fail!", e);
        } finally {
            responseJson.put("callbackId", callbackId);
            try {
                socketRequest.getResponse().write(responseJson.toString());
                socketRequest.getResponse().flush();
            } catch (Exception e) {
                logger.debug("",e);
            }
        }
    }


    @Override
    public void onClose(SocketRequest socketRequest) {
    }


    @Override
    public void onMessage(SocketRequest socketRequest) {
    }

}
