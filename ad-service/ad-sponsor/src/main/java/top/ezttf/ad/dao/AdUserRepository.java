package top.ezttf.ad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ezttf.ad.pojo.AdUser;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    /**
     * <h2> 根据用户名查找用户记录 </h2>
     * @param username
     * @return
     */
    AdUser findByUsername(String username);


}
