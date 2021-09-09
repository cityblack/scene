package com.lzh.game.scene.api.scene.transport;

import com.lzh.game.scene.api.proto.SceneTransportRequest;
import com.lzh.game.scene.api.proto.SceneTransportVerifyRequest;
import com.lzh.game.scene.api.scene.TransportLocal;
import com.lzh.game.scene.api.scene.TransportRemote;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.codec.Serializer;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.lzh.game.scene.common.RequestSpace.*;

public abstract class AbstractTransport<K extends Serializable>
        implements TransportLocal<K> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTransport.class);

    private Serializer serializer;

    public AbstractTransport(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 是否需要远程端验证
     * @return
     */
    public abstract boolean isVerifyRemote();

    /**
     * 当{@link #isVerifyRemote()}TRUE的时候才会进行调用
     * @param requestData
     * @param <V>
     * @return
     */
    public abstract <V>V buildVerifyData(TransportSceneData<K> requestData);

    /**
     * 切场景前
     */
    public abstract void beforeTransport(TransportSceneData<K> request);

    /**
     * 切场景要带到远程端的数据
     * @param <V>
     * @return
     */
    public abstract <V>V buildTransportData(TransportSceneData<K> request);

    /**
     * 切换场景后
     */
    public abstract void afterTransport(TransportSceneData<K> requestData);

    @Override
    public void transport(SceneConnect connect, TransportSceneData<K> request, boolean local) {
        if (isVerifyRemote()) {
            if (!doRemoveVerify(connect, request, local)) {
                return;
            }
        }
        doTransport(connect, request, local);
    }

    private void doTransport(SceneConnect connect, TransportSceneData<K> requestData, boolean local) {
        beforeTransport(requestData);
        Object value = buildTransportData(requestData);
        if (local) {
            TransportRemote remote = getLocalRemoteTransport(requestData);
            remote.enterScene(value);
        } else {
            SceneTransportRequest data = new SceneTransportRequest();
            data.setStrategy(requestData.getStrategy());
            data.setRequestBody(serializer.encode(value));
            data.setBodyClassName(value.getClass().getName());

            Request request = Request.of(cmd(INSTANCE_SPACE, SCENE_TRANSPORT), data);
            connect.sendOneWay(request);
        }
        afterTransport(requestData);
    }

    private boolean doRemoveVerify(SceneConnect connect, final TransportSceneData<K> requestData, boolean local) {
        Object verifyData = buildVerifyData(requestData);
        int result = 0;
        if (local) {
            TransportRemote remote = getLocalRemoteTransport(requestData);
            result = remote.verify(verifyData);
        } else {
            SceneTransportVerifyRequest verifyRequest = new SceneTransportVerifyRequest();
            verifyRequest.setStrategy(requestData.getStrategy());
            verifyRequest.setRequestBody(serializer.encode(verifyData));
            verifyRequest.setBodyClassName(verifyData.getClass().getName());

            Request request = Request.of(cmd(INSTANCE_SPACE, SCENE_TRANSPORT_VERIFY), verifyRequest);
            CompletableFuture<Response<Integer>> verifyResult = connect.sendMessage(request);
            try {
                Response<Integer> response = verifyResult.get(5000, TimeUnit.SECONDS);
                result = response.getParam();
            } catch (Exception e) {
                logger.error("切场景远程验证异常", e);
                onError(requestData, REMOTE_VERIFY_ERROR);
                return false;
            }
        }
        if (result != 0) {
            onError(requestData, result);
        }
        return result == 0;
    }


    private TransportRemote getLocalRemoteTransport(TransportSceneData<K> request) {
        return null;
    }
}
