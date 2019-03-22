package top.ezttf.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.ezttf.ad.mysql.listener.AggregationListener;

import java.util.concurrent.*;

/**
 * @author yuwen
 * @date 2019/1/31
 */
@Slf4j
@Component
public class BinlogClient {

    private BinaryLogClient client;

    private final BinlogConfig config;

    private final AggregationListener listener;

    @Autowired
    public BinlogClient(BinlogConfig config, AggregationListener listener) {
        this.config = config;
        this.listener = listener;
    }

    /**
     * 该方法应该在应用程序启动时刻 就开始进行binlog 监听,
     * 可以使用springboot 提供的{@link org.springframework.boot.CommandLineRunner}
     * 或者{@link org.springframework.boot.ApplicationRunner}默认优先级更高一点
     * @see org.springframework.boot.SpringApplication#callRunners(ApplicationContext, ApplicationArguments)
     *
     * 此处实现了在 {@link top.ezttf.ad.runner.BinlogRunner} 中直接调用
     */
    public void connect() {
        int corePoolSize = 1, maximumPoolSize = 1;
        long keepAliveTime = 0L;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("binlog-thread-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                workQueue,
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        executor.submit(() -> {
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
            if (StringUtils.isNotBlank(config.getBinlogName())
                    && !config.getPosition().equals(-1L)) {
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);
            try {
                log.info("connecting to mysql...");
                client.connect();
                log.info("connected to mysql");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("mysql binlog connect error");
            }
        });
        executor.shutdown();
    }

    public void close() {
        try {
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("mysql binlog disconnect error");
        }
    }
}
