package org.eram.oc.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Connection {
    protected final String TAG = "Connection";
    protected ObjectOutputStream output;
    protected ObjectInputStream input;
    protected String ip;
    protected Socket socket;
    protected double connectionDuration;

    protected  boolean status;

    public Connection()
    {
        this.connectionDuration = 0.0;
        this.status = false;
    }

    public abstract boolean connect(String ipAdress, int port, int timeOut);

    public abstract boolean close();


    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public double getConnectionDuration() {
        return connectionDuration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean getStatus(){

        return socket.isConnected();
    }
}
