package top.ezttf.ad.index.adplan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

/**
 * @author yuwen
 * @date 2019/1/25
 */
@Slf4j
@Component
public class AdPlanIndex implements IIndexAware<Long, AdPlanObject> {
    @Override
    public AdPlanObject get(Long key) {
        return null;
    }

    @Override
    public void add(Long key, AdPlanObject value) {

    }

    @Override
    public void update(Long key, AdPlanObject value) {

    }

    @Override
    public void delete(Long key, AdPlanObject value) {

    }
}
