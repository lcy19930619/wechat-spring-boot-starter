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
    /**
     * 默认的处理数量
     */
    private static final long DEFAULT_LIMIT = 500;
    @Autowired
    @Qualifier("batchExecuteThreadPool")
    public ThreadPoolTaskExecutor batchExecuteThreadPool;

    /**
     * 批量执行，每次执行512条数据，不使用线程池
     *
     * @param data     要处理的数据
     * @param consumer 执行的具体业务
     * @param <T>      具体的数据类型
     */
    private <T> void batchExecute(List<T> data, Consumer<List<T>> consumer) {
        execute(false,data,consumer,DEFAULT_LIMIT);
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
        execute(useThreadPool,data,consumer,DEFAULT_LIMIT);
    }

    /**
     * 批量处理
     *
     * @param useThreadPool 是否使用线程池
     * @param data          待处理的数据
     * @param consumer      处理数据的逻辑
     * @param limit         批量处理数据的大小
     * @param <T>           数据的类型
     */
    public <T> void execute(boolean useThreadPool,List<T> data, Consumer<List<T>> consumer, long limit) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("limit 不能小于等于0 ");
        }
        if (Objects.isNull(consumer)) {
            throw new IllegalArgumentException("consumer 不应为null");
        }

        long endIndex = data.size() - 1;
        long startIndex = 0;
        do {
            List<T> collect = data
                    .stream()
                    .skip(startIndex)
                    .limit(limit)
                    .collect(Collectors.toList());
            if(useThreadPool){
                batchExecuteThreadPool.execute(()-> consumer.accept(collect));
            }else{
                consumer.accept(collect);
            }
            startIndex += limit;
        } while (startIndex <= endIndex);
    }
}
