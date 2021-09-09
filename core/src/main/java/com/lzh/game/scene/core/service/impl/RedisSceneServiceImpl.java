package com.lzh.game.scene.core.service.impl;

import com.google.common.eventbus.Subscribe;
import com.lzh.game.scene.common.ContextConstant;
import com.lzh.game.scene.common.RequestSpace;
import com.lzh.game.scene.common.SceneChangeStatus;
import com.lzh.game.scene.common.SceneInstance;
import com.lzh.game.scene.common.connect.Connect;
import com.lzh.game.scene.common.connect.ConnectEvent;
import com.lzh.game.scene.common.connect.Request;
import com.lzh.game.scene.common.connect.Response;
import com.lzh.game.scene.common.connect.scene.SceneConnect;
import com.lzh.game.scene.common.proto.NodeInfo;
import com.lzh.game.scene.common.utils.EventBusUtils;
import com.lzh.game.scene.core.node.NodeService;
import com.lzh.game.scene.core.service.RedisClusterServer;
import com.lzh.game.scene.core.service.SceneService;
import com.lzh.game.scene.core.service.impl.mode.InstanceSubscribe;
import com.lzh.game.scene.core.service.impl.mode.SceneInstanceTop;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lzh.game.scene.common.RequestSpace.*;

/**
 * 使用redis做共享 满足ap
 * 场景实例分组
 * 场景缓存数据结构 -> 场景节点+address+group做key Map<String(场景主键), SceneInstance>
 * 场景根据mapId索引(根据mapId分组) -> 场景节点+address+group+mapId Set<String(场景主键)>
 */
public class RedisSceneServiceImpl implements SceneService {

    private static final Logger logger = LoggerFactory.getLogger(RedisSceneServiceImpl.class);

    private static final String INSTANCE_TOP = "map_instance_top";

    private static final String INSTANCE_PRE = "instance_";

    private static final Function<String, String> TO_INSTANCE_PRE = s -> INSTANCE_PRE + s;

    private RedissonClient client;

    private InstanceSubscribe listener;

    private RedisClusterServer redisClusterServer;

    private NodeService nodeService;

    public RedisSceneServiceImpl(RedissonClient client, InstanceSubscribe listener, NodeService nodeService, RedisClusterServer clusterServer) {
        this.client = client;
        this.listener = listener;
        this.nodeService = nodeService;
        this.redisClusterServer = clusterServer;
        this.init();
    }

    protected void init() {
        this.client.getTopic(INSTANCE_TOP)
                .addListenerAsync(SceneInstanceTop.class, this::onMessage)
                .onComplete((id, throwable) -> {
                    if (Objects.nonNull(throwable)) {
                        logger.error("Register instance listener error!", throwable);
                    } else {
                        logger.info("Register redis top. id:{}", id);
                    }
                });
        EventBusUtils.getInstance().register(this);
    }

    /**
     * 批量注册 批量推送?? 延迟队列?
     *
     * @param group
     * @param instance
     */
    @Override
    public void registerSceneInstance(String group, SceneInstance instance) {

        RMap<String, SceneInstance> contain = getGroupContain(group);
        String key = instance.getUnique();
        if (contain.containsKey(key)) {
            logger.error("Repeated registration instance [{}-{}-{}]", instance.getGroup(), instance.getMap(), instance.getUnique());
        }
        contain.put(key, instance);
        logger.info("Register instance:{}", instance);
        RSet<String> keys = getInstanceMapKeys(group, instance.getMap());
        keys.add(key);

        SceneInstanceTop top = SceneInstanceTop.of(instance, SceneChangeStatus.CHANGE.ordinal());
        this.client
                .getTopic(INSTANCE_TOP)
                .publish(top);
    }

    @Override
    public void removeSceneInstance(String group, String unique) {
        RMap<String, SceneInstance> contain = getGroupContain(group);
        SceneInstance instance = contain.remove(unique);
        if (Objects.nonNull(instance)) {
            SceneInstanceTop message = SceneInstanceTop.of(instance, SceneChangeStatus.DESTROY.ordinal());
            getInstanceMapKeys(group, instance.getMap()).remove(instance.getGroup());
            this.client
                    .getTopic(INSTANCE_TOP)
                    .publish(message);
        }
    }

    @Override
    public List<SceneInstance> getSceneInstances(String group, int map) {
        if (map == ContextConstant.ALL_MAP_LISTEN_KEY) {
            return new ArrayList<>(getGroupContain(group).values());
        }
        RMap<String, SceneInstance> contain = getGroupContain(group);
        RSet<String> keys = getInstanceMapKeys(group, map);
        return new ArrayList<>(contain.getAll(keys).values());
    }

    @Override
    public SceneInstance getSceneInstance(String group, String unique) {
        RMap<String, SceneInstance> contain = getGroupContain(group);
        if (Objects.isNull(contain)) {
            return null;
        }
        return contain.get(unique);
    }

    @Override
    public void subscribe(Connect connect, String group, SceneChangeStatus status, int map) {
        this.listener.addListener(group, connect.key(), map, status);
    }

    @Override
    public void keepMapInstances(String group, int map, int numLimit) {

    }

    @Override
    public CompletableFuture<SceneInstance> createScene(SceneConnect connect, String group, int mapId, int weight) {
        NodeInfo nodeInfo = selectNode();

        Connect sceneNodeConnect = redisClusterServer.getConnectManage().getConnect(nodeInfo.getKey());

        Request request = Request.of(cmd(NODE_SPACE, NODE_CLIENT_CREATE), mapId);
        CompletableFuture<Response<SceneInstance>> future = sceneNodeConnect.sendMessage(request);
        CompletableFuture<SceneInstance> response = future.thenApply(Response::getParam);
        response.thenAccept(e -> this.registerSceneInstance(e.getGroup(), e));
        return response;
    }

    private NodeInfo selectNode() {
        List<NodeInfo> nodes = nodeService.getSceneNode().collect(Collectors.toList());
        Random random = new Random();
        int index = random.nextInt(nodes.size());
        return nodes.get(index);
    }

    /**
     * Redis获取订阅的场景信息 进行推送
     *
     * @param channel
     * @param msg
     */
    private void onMessage(CharSequence channel, SceneInstanceTop msg) {
        SceneInstance instance = msg.getSceneInstance();
        this.listener
                .notifyListener(instance.getGroup(), instance, SceneChangeStatus.values()[msg.getEventType()]);
    }

    public void setClient(RedissonClient client) {
        this.client = client;
    }

    public RMap<String, SceneInstance> getContain(String group) {
        return this.client.getMap(group);
    }

    protected RMap<String, SceneInstance> getGroupContain(String group) {
        return this.getContain(TO_INSTANCE_PRE.apply(group));
    }

    private String getInstanceMapIndexKey(String group, int map) {
        return TO_INSTANCE_PRE.apply(group) + "_" + map;
    }

    private RSet<String> getInstanceMapKeys(String group, int map) {
        return this.client.getSet(getInstanceMapIndexKey(group, map));
    }

    @Subscribe
    public void onConnectClose(ConnectEvent connectEvent) {
        if (connectEvent.getType() != ConnectEvent.CLOSED) {
            return;
        }
        SceneConnect connect = connectEvent.getConnect().getAttr(ContextConstant.SCENE_CONNECT_RELATION);
        if (Objects.isNull(connect)) {
            return;
        }
        this.listener
                .removeListener(connect.key());
    }
}
