package com.daniel.rpc.demo.service.impl;

import com.daniel.rpc.demo.service.DemoService;

import java.awt.*;

/**
 * 接口实现
 * @author daniel
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String world) {
        return "hello " + world;
    }

    @Override
    public boolean multiPoint(Point point, int i) {
        return false;
    }
}
