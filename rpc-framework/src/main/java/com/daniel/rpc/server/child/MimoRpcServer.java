package com.daniel.rpc.server.child;

import com.daniel.rpc.server.handler.RequestHandler;
import com.daniel.rpc.server.RpcServer;

/**
 * mimo rpc server
 * @author daniel
 */
public class MimoRpcServer extends RpcServer {
    public MimoRpcServer(int port, String protocol, RequestHandler handler) {
        super(port, protocol, handler);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
