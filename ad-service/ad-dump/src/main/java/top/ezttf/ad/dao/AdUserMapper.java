package top.ezttf.ad.dao;

import org.apache.ibatis.annotations.Mapper;
import top.ezttf.ad.pojo.AdUser;

import java.util.List;

@Mapper
public interface AdUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdUser record);

    int insertSelective(AdUser record);

    AdUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdUser record);

    int updateByPrimaryKey(AdUser record);






    List<AdUser> selectAll();
}