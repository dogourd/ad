package top.ezttf.ad.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.pojo.AdPlan;
import top.ezttf.ad.vo.AdPlanGetRequest;

import java.util.Collections;
import java.util.List;

/**
 * @author yuwen
 * @date 2019/2/13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService iAdPlanService;

    @Test
    public void testGetAdPlanByIds() throws AdException {
        List<AdPlan> adPlanList = iAdPlanService.getAdPlanByIdList(
                new AdPlanGetRequest(10L, Collections.singletonList(10L))
        );
        log.info("{}", adPlanList);
    }
}
