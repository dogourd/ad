package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.dump.table.AdUnitDistrictTable;
import top.ezttf.ad.pojo.AdUnitDistrict;

import java.util.List;

@Mapper
public interface AdUnitDistrictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdUnitDistrict record);

    int insertSelective(AdUnitDistrict record);

    AdUnitDistrict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdUnitDistrict record);

    int updateByPrimaryKey(AdUnitDistrict record);


    List<AdUnitDistrictTable> selectAdUnitDistrictTable();
}