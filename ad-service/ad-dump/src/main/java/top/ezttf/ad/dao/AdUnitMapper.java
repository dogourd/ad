package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.ezttf.ad.dump.table.AdUnitTable;
import top.ezttf.ad.pojo.AdUnit;

import java.util.List;

@Mapper
public interface AdUnitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdUnit record);

    int insertSelective(AdUnit record);

    AdUnit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdUnit record);

    int updateByPrimaryKey(AdUnit record);





    List<AdUnitTable> selectUnitTableByUnitStatus(@Param(value = "unitStatus") Integer unitStatus);
}