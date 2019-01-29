package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdCreative;

@Mapper
public interface AdCreativeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdCreative record);

    int insertSelective(AdCreative record);

    AdCreative selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdCreative record);

    int updateByPrimaryKey(AdCreative record);
}