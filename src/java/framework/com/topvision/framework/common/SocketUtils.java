package com.topvision.framework.common;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public final class SocketUtils {
    public static void close(DatagramSocket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    public static void close(ServerSocket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    public static void close(Socket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    public static void closeQuietly(DatagramSocket socket) {
        if (socket != null) {
            socket.close();
        }
    }

    public static void closeQuietly(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static boolean isConnected(Socket socket) {
        return socket.isConnected() && !socket.isClosed();
    }
}
