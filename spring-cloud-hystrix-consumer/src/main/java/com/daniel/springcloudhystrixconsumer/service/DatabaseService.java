package com.daniel.springcloudhystrixconsumer.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    public List<Map<String, Object>> queryPriceList(List<String> goodIds) {
        return new ArrayList<>();
    }

}
