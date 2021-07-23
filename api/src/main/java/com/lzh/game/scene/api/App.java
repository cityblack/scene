package com.lzh.game.scene.api;

import com.alipay.remoting.InvokeCallback;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcResponseFuture;
import com.lzh.game.scene.api.client.Request;
import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.api.connect.ConnectClient;
import com.lzh.game.scene.api.connect.sofa.SofaConnectClient;
import com.lzh.game.scene.common.SceneChangeStatus;

import java.util.concurrent.Executor;

public class App {

    public static void main(String[] args) {

        ApiConfig config = new ApiConfig();
        Member member = new Member();
        member.setHost("127.0.0.1");
        member.setPort(8081);
        config.addMember(member);

        ConnectClient client = new SofaConnectClient();
        client.init(config);

        AsyncSceneApi api = new AsyncSceneApiImpl();
        String group = "test";

        api.subscribe(group, SceneChangeStatus.CREATE, instance -> System.out.println(instance));


    }
}
