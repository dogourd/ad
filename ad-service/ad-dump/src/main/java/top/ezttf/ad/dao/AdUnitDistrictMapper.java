package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdUnitDistrict;

@Mapper
public interface AdUnitDistrictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdUnitDistrict record);

    int insertSelective(AdUnitDistrict record);

    AdUnitDistrict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdUnitDistrict record);

    int updateByPrimaryKey(AdUnitDistrict record);
}