package com.daniel.springcloudhystrixconsumer.command;

import com.daniel.springcloudhystrixconsumer.service.DatabaseService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 请求合并
 */
public class GoodsPriceCollapseCommand extends HystrixCollapser<List<Map<String, Object>>, String, String> {

    private DatabaseService databaseService;
    private String goodsId;

    public GoodsPriceCollapseCommand(DatabaseService databaseService, String goodsId) {
        super(Setter
                .withCollapserKey(HystrixCollapserKey.Factory.asKey("goodsPriceCollapseCommand"))
                .andCollapserPropertiesDefaults(
                        //10毫秒内的请求合并
                        HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(10)
                                .withMaxRequestsInBatch(200))
                .andScope(Scope.GLOBAL)
        );
        this.databaseService = databaseService;
        this.goodsId = goodsId;
    }

    @Override
    public String getRequestArgument() {
        return goodsId;
    }

    @Override
    protected HystrixCommand<List<Map<String, Object>>> createCommand(Collection<CollapsedRequest<String, String>> collection) {
        //取出这个时间窗口的所有请求
        List<String> goodsIds = new ArrayList<>(collection.size());
        goodsIds.addAll(collection.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        System.out.println("查询数据:" + goodsIds);
        return new GoodsPriceBatchCommand(databaseService, goodsIds);
    }

    @Override
    protected void mapResponseToRequests(List<Map<String, Object>> batchResponse, Collection<CollapsedRequest<String, String>> collapsedRequests) {
        //结果和请求映射起来
        Map<String, String> responseMap = new HashMap<>();
        for (Map<String, Object> response : batchResponse) {
            responseMap.put(response.get("goods_id").toString(), response.get("goods_price").toString());
        }
        //根据请求附带的参数,找到对应的结果,设置每个请求线程的结果
        for (CollapsedRequest<String, String> collapsedRequest : collapsedRequests) {
            String price = responseMap.get(collapsedRequest.getArgument());
            collapsedRequest.setResponse(price); //future.get() -- 阻塞知道有结果返回
        }

    }
}
