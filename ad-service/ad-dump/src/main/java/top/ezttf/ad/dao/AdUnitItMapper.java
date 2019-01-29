package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.dump.table.AdUnitItTable;
import top.ezttf.ad.pojo.AdUnitIt;

import java.util.List;

@Mapper
public interface AdUnitItMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdUnitIt record);

    int insertSelective(AdUnitIt record);

    AdUnitIt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdUnitIt record);

    int updateByPrimaryKey(AdUnitIt record);


    List<AdUnitItTable> selectAdUnitItTable();
}