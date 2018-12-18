package com.topvision.ems.cmc.spectrum.performance;

import com.topvision.ems.cmc.spectrum.utils.SpectrumByteRead;
import com.topvision.ems.cmc.spectrum.utils.SpectrumIIDataParse;
import com.topvision.ems.cmc.spectrum.utils.SpectrumIIStringRead;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * Created by jay on 16-9-14.
 */

@Service("spectrumUdpClient")
public class SpectrumUdpClient {
    private byte[] buffer = new byte[10240];
    private DatagramSocket ds = null;

    /**
     * 构造函数，创建UDP客户端
     * @throws Exception
     */
    public SpectrumUdpClient() throws Exception {
        ds = new DatagramSocket();
    }

    /**
     * 设置超时时间，该方法必须在bind方法之后使用.
     * @param timeout 超时时间
     * @throws Exception
     */
    public void setSoTimeout(final int timeout) throws Exception {
        ds.setSoTimeout(timeout);
    }

    /**
     * 获得超时时间.
     * @return 返回超时时间
     * @throws Exception
     */
    public int getSoTimeout() throws Exception {
        return ds.getSoTimeout();
    }

    public DatagramSocket getSocket() {
        return ds;
    }

    /**
     * 向指定的服务端发送数据信息.
     * @param host 服务器主机地址
     * @param port 服务端端口
     * @param bytes 发送的数据信息
     * @return 返回构造后俄数据报
     * @throws IOException
     */
    public DatagramPacket send(String host, int port,
                                     byte[] bytes) throws IOException {
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress
                .getByName(host), port);
        ds.send(dp);
        return dp;
    }

    /**
     * 接收从指定的服务端发回的数据.
     * @param host 服务端主机
     * @param port 服务端端口
     * @return 返回从指定的服务端发回的数据.
     * @throws Exception
     */
    public byte[] receive(String host, int port)
            throws Exception {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ds.receive(dp);
        return dp.getData();
    }

    /**
     * 关闭udp连接.
     */
    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 测试客户端发包和接收回应信息的方法.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpectrumUdpClient client = new SpectrumUdpClient();
        String serverHost = "172.17.2.148";
        int serverPort = 3000;
        client.send(serverHost, serverPort, ("fft-get").getBytes());
        byte[] info = client.receive(serverHost, serverPort);
        SpectrumByteRead sbr = new SpectrumByteRead(info);
        System.out.println("******************mac*****************");
        String mac = sbr.readNByteString(6);
        System.out.println("mac = " + mac);
        System.out.println("******************length*****************");
        int length = (int)sbr.readNByteToNumber(2);
        System.out.println("length = " + length);
        System.out.println("******************data*****************");
        sbr.setIndex(6);
        String compstr = sbr.readNByteString(length + 2);
        SpectrumIIDataParse spectrumIIDataParse = new SpectrumIIDataParse();
        spectrumIIDataParse.setSrcData(compstr);
        spectrumIIDataParse.prase();
        List<List<Number>> list = spectrumIIDataParse.getPoints();
        System.out.println("list.size() = " + list.size());
    }
}
