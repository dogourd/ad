package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdUnit;

@Mapper
public interface AdUnitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdUnit record);

    int insertSelective(AdUnit record);

    AdUnit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdUnit record);

    int updateByPrimaryKey(AdUnit record);
}