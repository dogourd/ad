package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.ezttf.ad.dump.table.AdPlanTable;
import top.ezttf.ad.pojo.AdPlan;

import java.util.List;

@Mapper
public interface AdPlanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdPlan record);

    int insertSelective(AdPlan record);

    AdPlan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdPlan record);

    int updateByPrimaryKey(AdPlan record);




    List<AdPlanTable> selectPlanTableByPlanStatus(@Param(value = "planStatus") Integer planStatus);
}