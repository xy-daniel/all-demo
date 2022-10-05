package com.daniel.springcloudhystrixconsumer.define;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 通过这个demo可以理解hystrix的future返回机制
 */
public class TimeoutBreakerDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        RandomCommand randomCommand = new RandomCommand();
        Future<String> future = executorService.submit(randomCommand::run);
        String result = null;
        try {
            result = future.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            result = randomCommand.fallback();
        }
        System.out.println("执行结果" + result);

    }

    private static final Random random = new Random();

    public static class RandomCommand implements Command<String> {
        @Override
        public String run() {
            try {
                //休眠时间
                int timeout = random.nextInt(150);
                System.out.println("休眠时间" + timeout);
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "正常返回";
        }

        @Override
        public String fallback() {
            return "出错了,超时";
        }
    }
}
