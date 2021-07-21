package com.lzh.game.scene.api;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.InvokeContext;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcServer;
import org.junit.jupiter.api.Test;

public class ApiTestApp {

    @Test
    public void createScene() throws RemotingException, InterruptedException {

        String addr = "127.0.0.1:8081";
        SimpleClientUserProcessor clientUserProcessor = new SimpleClientUserProcessor();
        CONNECTEventProcessor clientConnectProcessor = new CONNECTEventProcessor();
        DISCONNECTEventProcessor clientDisConnectProcessor = new DISCONNECTEventProcessor();
        RpcClient client = new RpcClient();
        client.addConnectionEventProcessor(ConnectionEventType.CONNECT, clientConnectProcessor);
        client.addConnectionEventProcessor(ConnectionEventType.CLOSE, clientDisConnectProcessor);
        client.init();

        RequestBody req = new RequestBodyTest();
        req.setId(10086);
        req.setMsg("i am 10086");
        InvokeContext invokeContext = new InvokeContext();
//        invokeContext.put(InvokeContext.SERVER_LOCAL_IP);
        try {
            ResponseBody res = (ResponseBody) client.invokeSync(addr, req, 30000);
            System.out.println("invoke sync result = [" + res.getMsg() + "]");
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void server() {
        SimpleServerUserProcessor processor = new SimpleServerUserProcessor();
        RpcServer server = new RpcServer(8081);
        server.registerUserProcessor(processor);
        server.start();
    }
}
