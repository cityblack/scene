package com.lzh.game.scene.common.utils;

import com.google.common.eventbus.EventBus;

public class EventBusUtils {

    private static final EventBus EVENT_BUS = new EventBus("EVENT_BUS");

    private EventBusUtils() {}

    public static EventBus getInstance() {
        return EVENT_BUS;
    }
}
