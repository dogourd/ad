package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdUnitIt;

@Mapper
public interface AdUnitItMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdUnitIt record);

    int insertSelective(AdUnitIt record);

    AdUnitIt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdUnitIt record);

    int updateByPrimaryKey(AdUnitIt record);
}