package com.lzh.game.scene.api.connect.sofa;

import com.lzh.game.scene.api.config.ApiConfig;
import com.lzh.game.scene.api.config.Member;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.NodeType;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.proto.NodeInfoRequest;
import com.lzh.game.scene.common.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.lzh.game.scene.common.RequestSpace.*;

/**
 * 业务Api client 将业务连接都放置在该类
 */
public class ApiClient extends SofaConnectClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    public ApiClient(ApiConfig config) {
        super(config);
    }

    @Override
    protected void doStart() {
        super.doStart();
        this.connectCluster(this.getConfig().getCluster());
        this.registerNode();
    }

    private void connectCluster(List<Member> members) {
        for (Member member : members) {
            getConnect(member.getHost(), member.getPort(), NodeType.SCENE_NODE);
        }
    }

    private void registerNode() {
        ApiConfig config = getConfig();

        NodeInfoRequest nodeInfo = new NodeInfoRequest();
        nodeInfo.setType(config.getNodeType());
        nodeInfo.setWeight(0);
        nodeInfo.setPort(config.getPort());

        Request request = Request.of(cmd(NODE_SPACE, NODE_REGISTER), nodeInfo);

        CompletableFuture<Response<List<NodeInfo>>> future = sendMessage(request);

        if (NodeType.isClientNode(nodeType())) {
            // 获取所有的场景节点进行连接
            try {
                Response<List<NodeInfo>> response = future.get(5000, TimeUnit.SECONDS);
                logger.info("注册完毕后收到服务端数据数量:{}", response.getParam().size());
                if (response.getStatus() == ContextConstant.RIGHT_RESPONSE) {
                    List<NodeInfo> infos = response.getParam();
                    Map<String, Integer> retryTimes = new HashMap<>();
                    String self = IpUtils.localIp();

                    for (NodeInfo info : infos) {
                        connect(info, self, retryTimes);
                    }
                }
            } catch (Exception e) {
                logger.error("注册当前节点信息失败", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void connect(NodeInfo info, String selfIp, Map<String, Integer> retryTimes) {
        // 这种情况下 当前节点可能也是场景节点
        if (NodeType.isSceneNode(info.getType())) {
            if (Objects.equals(info.getIp(), "127.0.0.1")
                    || Objects.equals(info.getIp(), "0.0.0.0") || Objects.equals(info.getIp(), selfIp)) {
                if (info.getPort() == getConfig().getPort()) {
                    return;
                }
            }

            try {
                getConnect(info.getIp(), info.getPort(), NodeType.SCENE_NODE);
            } catch (Exception e) {
                int times = retryTimes.getOrDefault(info.getKey(), 0);
                logger.error("连接节点[{}:{}]失败, 重试次数{}", info.getIp(), info.getPort(), times);
                if (times < getConfig().getConnectErrorRetryTimes()) {
//                    connect(info, selfIp, retryTimes);
                }
            }
        }
    }

    public void onNodeChange(NodeInfo info) {
        if (info.getStatus() == 0) {
            String self = IpUtils.localIp();
            connect(info, self, new HashMap<>(2));
        }
    }
}
