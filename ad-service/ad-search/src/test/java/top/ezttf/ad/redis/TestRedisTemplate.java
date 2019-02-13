package top.ezttf.ad.redis;

import com.google.common.collect.Lists;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.index.DataTable;
import top.ezttf.ad.index.adplan.AdPlanObject;
import top.ezttf.ad.index.district.UnitDistrictIndex;
import top.ezttf.ad.index.keyword.UnitKeywordIndex;
import top.ezttf.ad.search.vo.feature.DistrictFeature;
import top.ezttf.ad.util.RedisUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author yuwen
 * @date 2019/1/25
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedisTemplate {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UnitKeywordIndex keywordIndex;

    @Autowired
    private UnitDistrictIndex districtIndex;
    @Test
    public void nativeAPI() {
        RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();
        String set = commands.set("String_key", "String_value");
        String value = commands.get("String_key");
        log.debug(value);

        Object _value = redisTemplate.opsForValue().get("String_key");
        log.debug("{}", _value);
    }

    @Test
    public void testRedisTemplate() {
        String value = stringRedisTemplate.opsForValue().get("String_key");
        log.debug("the value in redis is {} where key is \"String_key\"", value);
    }

    @Test
    public void testScanCommand() {
        RedisConnection connection = Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("*").count(Integer.MAX_VALUE).build());
        while (cursor.hasNext()) {
            byte[] next = cursor.next();
            log.info(new String(next));
        }
    }

    @Test
    public void testType() {
        AdPlanObject oldAdPlanObject = new AdPlanObject(1L, 1L, 1,
                LocalDateTime.now(), LocalDateTime.of(2019, 1, 1, 0, 0,0 ));
        redisTemplate.opsForValue().set("adPlanObject", oldAdPlanObject);
        AdPlanObject newAdPlanObject = (AdPlanObject) redisTemplate.opsForValue().get("adPlanObject");
        assert newAdPlanObject != null;
        log.debug("adPlanObject.endDate is {}", newAdPlanObject.getEndDate());
    }


    @Test
    public void testCanReturnNull() {
        Set<String> keys = redisTemplate.keys("sdhgihgoegu");
        if (keys == null) {
            log.debug("lettuce command --> keys() return null is possible");
        } else {
            log.debug("lettuce command --> keys() return null is impossible");
            log.debug("the result is {}", keys);
        }
    }

    @Test
    public void testOpsForSet() {
        Set<Long> longSet = new HashSet<>();
        for (long i = 1L; i <= 7L; i ++) {
            longSet.add(i);
        }
        Long setKey = redisTemplate.opsForSet().add("set_key", longSet.toArray());
        log.debug("opsForSet -> add() return {}", setKey);
        // result is
        // top.ezttf.ad.redis.TestRedisTemplate     : opsForSet -> add() return 7
        Set set = redisTemplate.opsForSet().members("set_key");
        log.debug("the set is {}", set);
        // result is
        // top.ezttf.ad.redis.TestRedisTemplate     : the set is [1, 2, 3, 4, 5, 6, 7]
        AdPlanObject adPlanObject1 = new AdPlanObject(1L, 2L, 3,
                LocalDateTime.now(), LocalDateTime.now());
        AdPlanObject adPlanObject2 = new AdPlanObject(2L, 3L, 4,
                LocalDateTime.now(), LocalDateTime.now());
        AdPlanObject adPlanObject3 = new AdPlanObject(3L, 4L, 5,
                LocalDateTime.now(), LocalDateTime.now());
        Long result = redisTemplate.opsForSet().add("set_object_key", adPlanObject1, adPlanObject2, adPlanObject3);
        log.debug("object set count is {}", result);
        Set<Object> objectSet = redisTemplate.opsForSet().members("set_object_key");
        log.debug("object set result is {}", objectSet);
        // result is
        // object set result is
        // [AdPlanObject(planId=1, userId=2, planStatus=3, startDate=2019-01-27, endDate=2019-01-27),
        // AdPlanObject(planId=3, userId=4, planStatus=5, startDate=2019-01-27, endDate=2019-01-27),
        // AdPlanObject(planId=2, userId=3, planStatus=4, startDate=2019-01-27, endDate=2019-01-27)]
    }


    @Test
    public void testKeys() {
        Set<String> set = redisTemplate.keys("long*");
        log.debug("keys result is {}", set);
    }



    @Test
    public void testDataTable() {
        UnitKeywordIndex unitKeywordIndex = DataTable.of(UnitKeywordIndex.class);
        Set<Long> set = new HashSet<>();
        for (long i = 0; i < 10L; i++) {
            set.add(i);
        }
        unitKeywordIndex.add("my_test_dataTable_key", set);
        Set<Long> longSet = unitKeywordIndex.get("my_test_dataTable_key");
        log.debug("use dataTable get UnitKeywordIndex result is {}", longSet);
    }


    @Test
    public void testScan() {
        Set<String> set = RedisUtils.scan(redisTemplate, "*", -1);
        set.forEach(log::debug);
    }


    @Test
    public void testCast() {
        Set<Long> longSet = keywordIndex.get("奥迪");
        longSet.forEach(element -> log.debug(element.getClass().getName()));

        List<DistrictFeature.ProvinceAndCity> directs = Lists.newArrayList();
        directs.add(new DistrictFeature.ProvinceAndCity("安徽省", "合肥市"));
        log.debug("{}", districtIndex.match(10L, directs));
    }

}
