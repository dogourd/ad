package top.ezttf.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuwen
 * @date 2019/1/27
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    private static Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    /**
     * 全量索引的实现类{@link IndexFileLoader}需要依赖该bean, 此处直接将注册优先级调到最高
     * @return
     */
    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    @SuppressWarnings("all")
    public static<T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);
        if (instance != null) {
            return instance;
        }
        dataTableMap.put(clazz, getBean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    @SuppressWarnings("all")
    private static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    private static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
