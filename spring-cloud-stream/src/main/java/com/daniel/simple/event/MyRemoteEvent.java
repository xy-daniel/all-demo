package com.daniel.simple.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class MyRemoteEvent extends RemoteApplicationEvent {
    public MyRemoteEvent() {
    }

    public MyRemoteEvent(Object source, String originService) {
        super(source, originService, "**");
    }
}
