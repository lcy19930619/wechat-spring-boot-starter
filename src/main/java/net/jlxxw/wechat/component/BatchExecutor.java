package net.jlxxw.wechat.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 批量执行工具
 *
 * @author chunyang.leng
 * @date 2020/12/31 3:08 下午
 */
@Component
public class BatchExecutor {

    @Autowired
    @Qualifier("batchExecuteThreadPool")
    private ThreadPoolTaskExecutor batchExecuteThreadPool;

    /**
     * 批量执行，每次执行512条数据，不使用线程池
     *
     * @param data     要处理的数据
     * @param consumer 执行的具体业务
     * @param <T>      具体的数据类型
     */
    private <T> void batchExecute(List<T> data, Consumer<List<T>> consumer) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        // 统计需要分成几次
        int countFor = countFor(data.size());
        int start = 0;
        int end = 0;

        //每512条处理一次
        for (int i = 0; i < countFor; i++) {
            if (i < countFor - 1) {
                start = i << 9;
                end = (i + 1) << 9;
            } else {
                start = i << 9;
                end = data.size();
            }
            int finalStart = start;
            int finalEnd = end;
            List<T> tempList = data.subList(finalStart, finalEnd);
            consumer.accept(tempList);
        }
    }

    /**
     * 灵活执行方法，批量执行，每次处理512条
     *
     * @param data          需要处理的数据
     * @param consumer      处理逻辑
     * @param useThreadPool 是否使用线程池进行处理push
     * @param <T>
     */
    public <T> void batchExecute(boolean useThreadPool, List<T> data, Consumer<List<T>> consumer) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        if (useThreadPool) {
            // 统计需要分成几次
            int countFor = countFor(data.size());
            int start = 0;
            int end = 0;

            //每512条执行一次
            for (int i = 0; i < countFor; i++) {
                if (i < countFor - 1) {
                    start = i << 9;
                    end = (i + 1) << 9;
                } else {
                    start = i << 9;
                    end = data.size();
                }
                int finalStart = start;
                int finalEnd = end;
                batchExecuteThreadPool.execute(() -> {
                    List<T> tempList = data.subList(finalStart, finalEnd);
                    consumer.accept(tempList);
                });
            }
        } else {
            batchExecute(data, consumer);
        }
    }

    private int countFor(int size) {
        //批量插入数据大小
        int i = 1 << 9;

        if (size % i == 0) {
            return size / i;
        } else {
            return size / i + 1;
        }
    }

    /**
     * 批量处理
     * @param useThreadPool 是否使用线程池
     * @param data 待处理的数据
     * @param consumer 处理数据的逻辑
     * @param limit 批量处理数据的大小
     * @param <T> 数据的类型
     */
    public <T> void  batchExecute(boolean useThreadPool, List<T> data, Consumer<List<T>> consumer,long limit) {
        if(CollectionUtils.isEmpty(data)){
            return;
        }
        if(limit <= 0 ){
            throw new IllegalArgumentException("limit 不能小雨等于0 ");
        }
        if(Objects.isNull(consumer)){
            throw new IllegalArgumentException("consumer 不应为null");
        }
        int size = data.size();
        long count = 0;
        if (size % limit == 0) {
            count= size / limit;
        } else {
            count= size / limit + 1;
        }

        for (int i = 0; i < count; i++) {
            List<T> collect = data
                                .stream()
                                .skip(i * limit)
                                .limit(limit)
                                .collect(Collectors.toList());
            if(useThreadPool){
                batchExecuteThreadPool.execute(()->{
                    consumer.accept(collect);
                });
            }else{
                consumer.accept(collect);
            }
        }
    }

}
