package top.ezttf.ad.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.pojo.AdUser;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/28
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageHelperTest {

    @Autowired
    private AdUserMapper userMapper;

    @Test
    public void testPage() {
        PageHelper.startPage(1, 1);
        List<AdUser> adUsers = userMapper.selectAll();
        PageInfo<AdUser> pageInfo = new PageInfo<>(adUsers);
        log.debug(JSON.toJSONStringWithDateFormat(pageInfo, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
        adUsers.forEach(adUser -> log.debug(adUser.getUsername()));
    }
}
