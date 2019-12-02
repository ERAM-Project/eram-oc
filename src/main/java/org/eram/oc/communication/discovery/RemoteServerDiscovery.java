package org.eram.oc.communication.discovery;

public class RemoteServerDiscovery implements Runnable {

    private DiscoveryCallBack discoveryCallBack;
    public RemoteServerDiscovery(DiscoveryCallBack discoveryCallBack)
    {
        this.discoveryCallBack = discoveryCallBack;
    }

    @Override
    public void run() {
        this.discoveryCallBack.registerToRemoteServer();
    }

    public interface DiscoveryCallBack{
        void registerToRemoteServer();
    }
}
