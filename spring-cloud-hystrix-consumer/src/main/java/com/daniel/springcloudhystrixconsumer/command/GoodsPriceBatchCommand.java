package com.daniel.springcloudhystrixconsumer.command;

import com.daniel.springcloudhystrixconsumer.service.DatabaseService;
import com.netflix.hystrix.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 真正执行实际逻辑的命令
 */
@Component
public class GoodsPriceBatchCommand extends HystrixCommand<List<Map<String, Object>>> {

    DatabaseService databaseService;
    List<String> goodsIds;

    public GoodsPriceBatchCommand(DatabaseService databaseService, List<String> goodsIds) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("goodsPriceBatchCommand"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionTimeoutInMilliseconds(10000)
                        )
        );
        this.databaseService = databaseService;
        this.goodsIds = goodsIds;
    }

    @Override
    protected List<Map<String, Object>> run() throws Exception {
        System.out.println("调用批量查询,数量:" + goodsIds.size());
        return databaseService.queryPriceList(goodsIds);
    }

    @Override
    protected List<Map<String, Object>> getFallback() {
        //核心方法,降级之后会来实现这个
        System.out.println("降级啦......");
        return null;
    }
}
