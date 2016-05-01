package com.main.game.network.udp;

import java.net.InetAddress;

public class UDPClient {

    private String ip;

    private InetAddress inetAddress;

    private int port;

    public UDPClient() {};

    public UDPClient(String ip, int port) {
        this();
        setIp(ip);
        setPort(port);
    }

    public UDPClient(InetAddress inetAddress, int port) {
        this();
        setIp(inetAddress);
        setPort(port);
    }

    public String getIp() {
        if(ip == null && inetAddress != null)
            return inetAddress.toString();
        else return ip;
    }

    public int getPort() { return port; }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIp(InetAddress inetAddress) {
        this.ip = null;
        this.inetAddress = inetAddress;
    }

    public void setPort(int port) { this.port = port; }

    public InetAddress getInetAddress() {
        if(ip == null && inetAddress != null) {
            return inetAddress;
        } else {
            try {
                return InetAddress.getByName(ip);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
