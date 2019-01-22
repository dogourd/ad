package top.ezttf.ad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ezttf.ad.pojo.AdPlan;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface AdPlanRepository extends JpaRepository<AdPlan, Long> {

    AdPlan findByIdAndUserId(Long id, Long userId);

    List<AdPlan> findAllByIdInAndUserId(List<Long> idList, Long userId);

    AdPlan findByUserIdAndPlanName(Long userId, String planName);

    List<AdPlan> findAllByPlanStatus(Integer planStatus);

}
