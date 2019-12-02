package org.eram.oc.communication;

import android.util.Log;

import org.eram.common.Utils;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClearConnection extends Connection {

    public  ClearConnection()
    {
        super();

    }
    @Override
    public boolean connect(String ipAdress, int port, int timeOut) {
        try {

            long sTime = System.nanoTime();

            this.setIp(ipAdress);

            Log.e(TAG, "Connecting in CLEAR with MEC on: " + ipAdress+ ":" + port);
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(ipAdress, port), timeOut);

            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            long dur = System.nanoTime() - sTime;

            Log.e(TAG, "Socket connection set-up time - " + dur / 1000000 + "ms");

        } catch (Exception e) {
            Log.e(TAG,"Connection setup with the VM failed - " + e);
            return false;
        }

        return true;
    }

    @Override
    public boolean close() {

        if(getOutput()==null || getInput() == null)
            return false;

        Utils.close(getOutput());
        Utils.close(getInput());

        return false;
    }
}
