package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdPlan;

@Mapper
public interface AdPlanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdPlan record);

    int insertSelective(AdPlan record);

    AdPlan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdPlan record);

    int updateByPrimaryKey(AdPlan record);
}