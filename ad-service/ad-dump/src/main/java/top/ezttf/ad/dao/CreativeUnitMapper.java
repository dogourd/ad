package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.dump.table.AdCreativeUnitTable;
import top.ezttf.ad.pojo.CreativeUnit;

import java.util.List;

@Mapper
public interface CreativeUnitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CreativeUnit record);

    int insertSelective(CreativeUnit record);

    CreativeUnit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreativeUnit record);

    int updateByPrimaryKey(CreativeUnit record);


    List<CreativeUnit> selectAll();

    List<AdCreativeUnitTable> selectCreativeUnitTable();
}