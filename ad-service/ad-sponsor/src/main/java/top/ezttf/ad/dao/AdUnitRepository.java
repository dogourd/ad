package top.ezttf.ad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ezttf.ad.pojo.AdUnit;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    AdUnit findByPlanIdAndUnitName(Long planId, String unitName);

    List<AdUnit> findAllByUnitStatus(Integer unitStatus);
}
